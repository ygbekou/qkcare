package com.qkcare.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qkcare.domain.GenericDto;
import com.qkcare.domain.GenericResponse;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.User;
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
	public @ResponseBody User getTempUser(@RequestBody User user) {
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
	public @ResponseBody User saveUserAndLogin(@RequestBody User user) {
		LOGGER.info("User Login :" + user);
		try {
			return (User) userService.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			return new User();
		}

	}

}
