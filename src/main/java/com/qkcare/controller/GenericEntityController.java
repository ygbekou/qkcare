package com.qkcare.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qkcare.domain.GenericDto;
import com.qkcare.domain.GenericResponse;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.SearchAttribute;
import com.qkcare.service.GenericService;
import com.qkcare.util.Constants;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/service/{entity}")
@CrossOrigin
public class GenericEntityController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(AccountController.class);

	@Autowired
	@Qualifier("genericService")
	GenericService genericService;

	@Autowired
	private ApplicationContext context;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public BaseEntity get(@PathVariable("entity") String entity, @PathVariable("id") Long id)
			throws ClassNotFoundException {
		BaseEntity result = genericService.find(this.getClass(this.convertEntity(entity)), id);
		return result;
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<BaseEntity> getAll(@PathVariable("entity") String entity) throws ClassNotFoundException {
		List<BaseEntity> entities = genericService.getAll(this.getClass(this.convertEntity(entity)));
		System.out.println(entities);
		return entities;
	}

	@RequestMapping(value = "/allByCriteria", method = RequestMethod.POST)
	public List<BaseEntity> getAllByCriteria(@PathVariable("entity") String entity,
			@RequestBody List<String> parameters) throws ClassNotFoundException {

		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		for (String parameter : parameters) {
			String[] paramSplit = parameter.split("\\|");
			paramTupleList.add(Quartet.with(paramSplit[0], paramSplit[1], paramSplit[2], paramSplit[3]));
		}

		String queryStr = "SELECT e FROM " + this.convertEntity(entity) + " e WHERE 1 = 1";
		List<BaseEntity> entities = genericService.getByCriteria(queryStr, paramTupleList, null);
		System.out.println(entities);
		return entities;
	}

	@RequestMapping(value = "/allByCriteriaAndOrderBy", method = RequestMethod.POST)
	public List<BaseEntity> getAllByCriteriaAndOrderBy(@PathVariable("entity") String entity,
			@RequestBody SearchAttribute searchAttribute) throws ClassNotFoundException {

		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		for (String parameter : searchAttribute.getParameters()) {
			String[] paramSplit = parameter.split("\\|");
			paramTupleList.add(Quartet.with(paramSplit[0], paramSplit[1], paramSplit[2], paramSplit[3]));
		}
		String convertedEntity = this.convertEntity(entity);
		String queryStr = "SELECT e FROM " + convertedEntity + " e WHERE 1 = 1 " + getExtraWhereClause(convertedEntity);
		List<BaseEntity> entities = genericService.getByCriteria(queryStr, paramTupleList,
				searchAttribute.getOrderBy());

		return entities;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public BaseEntity save(@PathVariable("entity") String entity, @RequestBody GenericDto dto)
			throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			InstantiationException {
		BaseEntity obj = (BaseEntity) this.genericDtoToEntiityClassObject(dto, entity);

		Pair<Boolean, List<String>> results = this.validateEntity(context, obj, entity);
		BaseEntity savedObject = null;
		if (results.getValue0()) {
			savedObject = genericService.save(obj);
			savedObject = this.genericService.find(obj.getClass(), obj.getId());
		} else {
			savedObject = obj;
			savedObject.setErrors(results.getValue1());
		}
		return savedObject;
	}

	@RequestMapping(value = "/saveHospital", method = RequestMethod.POST)
	public BaseEntity saveHospital(@PathVariable("entity") String entity, @RequestPart("logo") MultipartFile logo,
			@RequestPart("favicon") MultipartFile favicon,
			@RequestPart("backgroundSlider") MultipartFile backgroundSlider, @RequestPart("dto") GenericDto dto)
			throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
				this.getClass(entity));

		this.genericService.saveWithFiles(obj, Arrays.asList(logo, favicon, backgroundSlider),
				Arrays.asList("logo", "favicon", "backgroundSlider"), false);

		return obj;
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET, produces = "application/json")
	public GenericResponse delete(@PathVariable("entity") String entity, @PathVariable("id") Long id)
			throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		try {
			List<Long> ids = new ArrayList<>();
			ids.add(id);
			this.genericService.delete(this.getClass(entity), ids);
			return new GenericResponse("SUCCESS");
		} catch (Exception e) {
			if (e.getMessage().contains("foreign key") || e.getMessage().contains("ConstraintViolationException")) {
				return new GenericResponse("FOREIGN_KEY_FAILURE", e.getMessage());
			} else {
				return new GenericResponse("GENERIC_FAILURE", e.getMessage());
			}
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@PathVariable("entity") String entity, @RequestBody List<Long> ids)
			throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		this.genericService.delete(this.getClass(entity), ids);
		return "SUCCESS";
	}

	@RequestMapping(value = "/cascade/delete", method = RequestMethod.POST)
	public String cascadeDelete(@PathVariable("entity") String entity, @RequestBody List<Long> ids)
			throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		this.genericService.delete(this.getClass(entity), ids);
		return "SUCCESS";
	}

	@RequestMapping(value = "/saveWithFile", method = RequestMethod.POST)
	public BaseEntity saveWithFile(@PathVariable("entity") String entity, @RequestPart("file") MultipartFile[] files,
			@RequestPart("dto") GenericDto dto) throws JsonParseException, JsonMappingException, IOException,
			ClassNotFoundException, NoSuchMethodException, SecurityException, BeansException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		BaseEntity obj = (BaseEntity) mapper.readValue(
				dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/").replaceAll("&#039;", "'"),
				this.getClass(entity));

		Pair<Boolean, List<String>> results = Pair.with(true, new ArrayList());
		try {
			Class validator = this.getClass(Constants.VALIDATOR_PACKAGE_NAME + entity + "CustomValidator");
			Method aMethod = validator.getMethod("validate", BaseEntity.class);
			results = (Pair<Boolean, List<String>>) aMethod.invoke(context.getBean(validator), obj);
		} catch (ClassNotFoundException ex) {

		}

		if (results.getValue0()) {
			this.genericService.saveWithFiles(obj, Arrays.asList(files), true, Arrays.asList("fileLocation"));
		} else {
			obj.setErrors(results.getValue1());
		}
		return obj;
	}
	
	@RequestMapping(value = "/saveFiles", method = RequestMethod.POST)
	public BaseEntity saveFiles(@PathVariable("entity") String entity, @RequestPart("file") MultipartFile[] files,
			@RequestPart("dto") GenericDto dto) throws JsonParseException, JsonMappingException, IOException,
			ClassNotFoundException, NoSuchMethodException, SecurityException, BeansException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		BaseEntity obj = (BaseEntity) mapper.readValue(
				dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/").replaceAll("&#039;", "'"),
				this.getClass(entity));
		

		this.genericService.saveWithFiles(obj, Arrays.asList(files), false, Arrays.asList("fileLocation"), 
				obj.getId().toString(), true);
		return obj;
	}
	
	
	@RequestMapping(value = "/readFiles/{id}", method = RequestMethod.GET)
	public List<String> saveFiles(@PathVariable("entity") String entity, @PathVariable("id") String id) {
		
		return this.genericService.readFiles(entity, id);
		
	}
	
	@RequestMapping(value = "/deleteFile/{id}", method = RequestMethod.POST)
	public GenericResponse deleteFile(@PathVariable("entity") String entity, @PathVariable("id") String id, 
			@RequestBody GenericDto dto) {
		try {
			String fileName  = dto.getJson().replaceAll("'", "").replaceAll("\"", "").replaceAll("&#039;", "'");
			this.genericService.deleteFiles(entity, id, Arrays.asList(fileName));
		} catch(Exception ex) {
			return new GenericResponse("FILE_NOT_DELETED", ex.getMessage());
		}
		return new GenericResponse("SUCCESS");
	}
	

	@RequestMapping(value = "/saveCompany", method = RequestMethod.POST)
	public BaseEntity saveCompany(@PathVariable("entity") String entity, @RequestPart("file[]") MultipartFile[] files,
			@RequestPart("dto") GenericDto dto)
			throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		BaseEntity obj = (BaseEntity) mapper.readValue(
				dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/").replaceAll("&#039;", "'"),
				this.getClass(entity));

		this.genericService.saveWithFiles(obj, Arrays.asList(files), false, null);

		return obj;
	}
}
