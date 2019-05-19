package com.qkcare.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkcare.domain.GenericDto;
import com.qkcare.model.BaseEntity;
import com.qkcare.util.Constants;

public class BaseController {

	private static final Logger LOGGER = Logger.getLogger(AccountController.class);
	
	protected Class getClass(String entity) throws ClassNotFoundException {
		return Class.forName(entity.contains(".") ? entity : Constants.PACKAGE_NAME + entity);
	}
	
	protected String convertEntity(String entity) {
		return entity.replaceAll("_", ".");
	}

	protected BaseEntity genericDtoToEntiityClassObject(GenericDto dto, String entity) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, 
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		BaseEntity obj = null;
		List<String> errors = new ArrayList<>();
		Class myClass = this.getClass(this.convertEntity(entity));
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj =  (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"), 
					myClass);
		} catch(Exception e) {
			obj = new BaseEntity();
			errors.add(e.getMessage());
			obj.setErrors(errors);
		}
		
		return obj;
	}
	
	protected Pair<Boolean, List<String>> validateEntity(ApplicationContext context, Object obj, String entity) 
			throws NoSuchMethodException, SecurityException, BeansException, IllegalAccessException, 
			IllegalArgumentException, InvocationTargetException {
		Pair<Boolean, List<String>> results = Pair.with(true, new ArrayList());
		try {
			Class validator = this.getClass(Constants.VALIDATOR_PACKAGE_NAME + entity + "CustomValidator"); 
			Method aMethod = validator.getMethod("validate", BaseEntity.class);
			results = (Pair<Boolean, List<String>>) aMethod.invoke(context.getBean(validator), obj);
		}
		catch (ClassNotFoundException ex) {
			LOGGER.error("No valid validator found");
		}
		
		return results;
	}
}
