package com.qkcare.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qkcare.config.JwtTokenUtil;
import com.qkcare.domain.AuthToken;
import com.qkcare.domain.GenericDto;
import com.qkcare.domain.GenericResponse;
import com.qkcare.domain.LoginUser;
import com.qkcare.domain.MenuVO;
import com.qkcare.domain.PermissionVO;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.User;
import com.qkcare.model.authorization.Role;
import com.qkcare.model.authorization.UserRole;
import com.qkcare.service.AuthorizationService;
import com.qkcare.service.GenericService;
import com.qkcare.service.UserService;
import com.qkcare.util.Constants;
import com.qkcare.util.SimpleMail;
import com.qkcare.util.Utils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/service/user/{entity}")
@CrossOrigin
public class UserController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(UserController.class);

	@Autowired
	@Qualifier("userService")
	UserService userService;
	@Autowired
	BCryptPasswordEncoder encoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private AuthorizationService authorizationService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BaseEntity save(@PathVariable("entity") String entity, @RequestPart("file") MultipartFile file,
			@RequestPart GenericDto dto)
			throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		BaseEntity obj = null;
		try {
			obj = (BaseEntity) this.genericDtoToEntiityClassObject(dto, entity);
			userService.save(obj, file);
		} catch (Exception e) {
			e.printStackTrace();
			obj.setErrors(Arrays.asList(e.getMessage()));
		}

		return obj;
	}

	@RequestMapping(value = "/saveWithoutPicture", method = RequestMethod.POST)
	public BaseEntity saveWithoutPicture(@PathVariable("entity") String entity, @RequestBody GenericDto dto)
			throws Exception {

		BaseEntity obj = null;
		try {
			obj = (BaseEntity) this.genericDtoToEntiityClassObject(dto, entity);
			if (obj.getErrors() == null || obj.getErrors().size() == 0) {
				userService.save(obj, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			obj.setErrors(Arrays.asList(e.getMessage()));
		}

		return obj;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody User login(@RequestBody User user) {
		LOGGER.info("User Login :" + user);
		if (user.getEmail() != null && !user.getEmail().contains("@")) {
			user.setUserName(user.getEmail());
		}
		user = userService.getUser(user.getEmail(), user.getUserName(), user.getPassword());

		if (user != null) {
			return user;
		}
		return new User();

	}

	@RequestMapping(value = "/sendPassword", method = RequestMethod.POST)
	public GenericResponse sendPassword(@PathVariable("entity") String entity, @RequestBody User user) {
		GenericResponse gr = new GenericResponse();
		if (user == null || (user.getEmail() == null && user.getUserName() == null)) {
			gr.setResult("Failure");
			return gr;
		}

		User storedUser = this.userService.getUser(user.getEmail(), user.getUserName(), null);

		if (storedUser == null) {
			gr.setResult("Failure");
			return gr;
		}

		try {

			if (storedUser.getEmail() != null) {
				String mail = "<blockquote><h2><b>Bonjour "
						+ (storedUser.getSex() != null && storedUser.getSex().equals("M") ? "Madame" : "Monsieur")
						+ "</b></h2><h2>Votre Mot de passe est:" + storedUser.getPassword()
						+ "  </h2><h2>Veuillez le garder secret en supprimant cet e-mail.</h2><h2>Encore une fois, merci de votre interet en notre organisation.</h2><h2><b>Le Directeur.</b></h2></blockquote>";
				SimpleMail.sendMail("Votre Mot de passe", mail, "softenzainc@gmail.com", storedUser.getEmail(),
						"smtp.gmail.com", "softenzainc@gmail.com", "softenza123", false);
			} else {
				gr.setResult("Failure");
				return gr;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			gr.setResult("Failure");
			return gr;
		}
		gr.setResult("Success");
		return gr;
	}

	@RequestMapping(value = "/getTempUser", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody User getTempUser(@PathVariable("entity") String entity, @RequestBody User user) {
		LOGGER.info("User Login :" + user);

		try {
			user = userService.getTempUser(user.getUserName(), user.getBirthDate());

			if (user != null) {
				return user;
			}
			return new User();
		} catch (Exception e) {
			e.printStackTrace();
			return new User();
		}

	}

	@RequestMapping(value = "/saveUserAndLogin", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> saveUserAndLogin(@PathVariable("entity") String entity, @RequestBody User user) {
		LOGGER.info("User Login :" + user);
		try {
			LoginUser lu = new LoginUser();
			lu.setPassword(user.getPassword());
			lu.setUserName(user.getEmail());
			user.setPassword(encoder.encode(user.getPassword()));
			user.setFirstTimeLogin("N");
			user.setUserName(user.getEmail());
			userService.save(user);
			UserRole ur = new UserRole();
			ur.setUser(user);
			ur.setRole((Role) userService.find(Role.class, 2L));
			userService.save(ur);
			return this.register(lu);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().contains("ConstraintViolationException")) {
				return ResponseEntity.ok(new AuthToken("", user.getUserName(), user.getPassword(), null, null, null,
						null, null, null, null, null, null));
			} else {
				return ResponseEntity.ok(new AuthToken(null, user.getUserName(), user.getPassword(), null, null, null,
						null, null, null, null, null, null));
			}
		}
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public @ResponseBody GenericResponse changePassword(@PathVariable("entity") String entity, @RequestBody User user) {

		if (user == null || (user.getUserName() == null)) {
			return new GenericResponse("Failure");
		}

		return new GenericResponse(this.userService.changePassword(user, user.getPassword()));
	}

	@RequestMapping(value = "/generate-token", method = RequestMethod.POST)
	public ResponseEntity<?> register(@RequestBody LoginUser loginUser) throws AuthenticationException {

		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final User user = userService.getUser(null, loginUser.getUserName(), null);
		final String token = jwtTokenUtil.generateToken(user);

		Pair<List<MenuVO>, List<PermissionVO>> resources = this.authorizationService.getUserResources(user.getId(),
				loginUser.getLang());

		return ResponseEntity.ok(new AuthToken(token, loginUser.getUserName(), loginUser.getPassword(),
				user.getFirstName(), user.getLastName(), user.getUserGroup().getName(), user.getPicture(),
				user.getFirstTimeLogin(), Arrays.asList(new Long[] { user.getUserGroup().getId() }),
				resources.getValue0(), resources.getValue1(), user.getId()));

	}

}
