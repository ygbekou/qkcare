package com.qkcare.service;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.qkcare.dao.UserDao;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.Company;
import com.qkcare.model.Patient;
import com.qkcare.model.User;
import com.qkcare.model.Visit;
import com.qkcare.util.Constants;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

@Service(value = "userService")
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	GenericService genericService;

	@Autowired
	UserDao userDao;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	@Qualifier("myMailSender")
	MyMailSender mailSender;

	private static RandomStringGenerator stringGenerator;

	static {
		stringGenerator = new RandomStringGenerator.Builder().withinRange('0', 'z').filteredBy(LETTERS, DIGITS).build();
	}

	@Transactional
	public BaseEntity save(BaseEntity entity, MultipartFile file) throws Exception {
		User user;
		try {

			Field userField = entity.getClass().getDeclaredField("user");
			userField.setAccessible(true);
			Field matriculeField = null;
			Field passwordField = null;
			try {
				matriculeField = entity.getClass().getDeclaredField("medicalRecordNumber");
				if (matriculeField != null)
					matriculeField.setAccessible(true);

			} catch (NoSuchFieldException nsfe) {

			}
			user = (User) userField.get(entity);
			Patient p = (Patient) entity;
			if (p.getVisitReason() != null && p.getVisitReason().trim().equals("")) {// this is from Kiosk
				List<User> users = userDao.getExistingUser(user.getFirstName(), user.getLastName(), user.getSex(),
						user.getBirthDate());
				if (users != null && users.size() > 1) {
					User exUser = users.get(0);

					List<BaseEntity> patients = genericService.getByCriteria(Patient.class, "user", exUser.getId());
					if (patients != null || patients.size() > 0) {
						Patient pp = (Patient) patients.get(0);
						Visit v = new Visit();
						v.setPatient(pp);
						v.setChiefOfComplain(p.getVisitReason());
						v.setVisitDatetime(new Timestamp(new Date().getTime()));
						genericService.save(v);
						return entity;
					}
				}
			}

			if (p.getVisitReason() == null || p.getVisitReason().trim().equals("")) {
				List<User> users = userDao.findMembers(user.getFirstName(), user.getLastName(), user.getUserName(),
						user.getEmail());
				if (users != null && users.size() > 0) {
					if (users.get(0).getId().intValue() != (user.getId() == null ? 0 : user.getId().intValue())
							|| users.size() > 1) {
						if (users.size() == 1) {
							throw new Exception("Echec: Un utilisateur similaire existe deja: Nom: "
									+ users.get(0).getFirstName() + " " + users.get(0).getLastName()
									+ ", Recherchez-le pour eviter les doublons.");
						} else {
							for (User u : users) {
								if (u.getId().intValue() != (user.getId() == null ? 0 : user.getId().intValue())) {
									throw new Exception("Echec: Un utilisateur similaire existe deja: Nom: "
											+ users.get(0).getFirstName() + " " + users.get(0).getLastName()
											+ ", Recherchez-le pour eviter les doublons.");
								}
							}
						}
					}
				}
			}

			passwordField = user.getClass().getDeclaredField("password");
			if (passwordField != null) {
				passwordField.setAccessible(true);
				passwordField.set(user, "1234");
			}
			user = (User) genericService.save(user);

			if (user != null) {
				if (matriculeField != null) {
					matriculeField.set(entity, StringUtils.leftPad(user.getId().toString(), 8));
				}

				if (file != null && !file.isEmpty()) {
					try {
						String originalFileExtension = file.getOriginalFilename()
								.substring(file.getOriginalFilename().lastIndexOf("."));

						// transfer to upload folder
						String storageDirectory = null;
						if (entity != null) {
							storageDirectory = Constants.IMAGE_FOLDER + "User" + File.separator;
							File dir = new File(storageDirectory);
							if (!dir.exists()) {
								dir.mkdirs();
							}

						} else {
							throw new Exception("Unknown");
						}

						String newFilename = null;
						newFilename = user.getId() + originalFileExtension;

						File newFile = new File(storageDirectory + "/" + newFilename);
						Field pictureField = user.getClass().getDeclaredField("picture");
						pictureField.setAccessible(true);
						pictureField.set(user, newFilename);
						user = (User) genericService.save(user);

						file.transferTo(newFile);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				userField.set(entity, user);
			}
		} catch (Exception e) {
			throw e;
		}

		genericService.save(entity);

		Visit v = new Visit();
		v.setPatient((Patient) entity);
		v.setChiefOfComplain(((Patient) entity).getVisitReason());
		v.setVisitDatetime(new Timestamp(new Date().getTime()));
		genericService.save(v);

		return entity;

	}

	@Transactional
	public User getUser(String email, String userName, String password) {
		return userDao.getUser(email, userName, password);
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userDao.getUser(null, userName, null);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				getAuthority());
	}

	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ADMIN"));
	}

	@Transactional
	public String sendPassword(User user, String password) {
		String generatedPassword = StringUtils.isEmpty(password) ? stringGenerator.generate(8) : password;
		try {
			User storedUser = this.getUser(user.getEmail(), user.getUserName(), null);

			if (StringUtils.isEmpty(password)) {
				storedUser.setPassword(encoder.encode(generatedPassword));
				storedUser.setFirstTimeLogin("Y");
				this.genericService.save(storedUser);
			}
			Company company = this.genericService.getCompany("EN");
			String emailMessage = "Your password is: " + generatedPassword + ". Please keep it safe.";
			mailSender.sendMail(company.getFromEmail(), storedUser.getEmail().split("'"),
					"Message from " + company.getName(), emailMessage);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Failure";
		}
		return "Success";
	}

	@Transactional
	public String changePassword(User user, String password) {
		try {
			User storedUser = this.getUser(user.getEmail(), user.getUserName(), null);
			if (!StringUtils.isEmpty(password)) {
				storedUser.setPassword(encoder.encode(password));
				storedUser.setFirstTimeLogin("N");
				this.genericService.save(storedUser);
			}
		} catch (Exception ex) {
			return "Failure";
		}
		return "Success";
	}
}
