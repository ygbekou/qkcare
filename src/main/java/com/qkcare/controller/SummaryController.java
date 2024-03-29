package com.qkcare.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkcare.domain.GenericDto;
import com.qkcare.domain.GenericVO;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.PhysicalExam;
import com.qkcare.model.Summary;
import com.qkcare.model.SystemReview;
import com.qkcare.service.GenericService;
import com.qkcare.service.SummaryService;
import com.qkcare.util.Constants;

@RestController
@RequestMapping(value = "/service/summary")
@CrossOrigin
public class SummaryController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(SummaryController.class);

	@Autowired
	@Qualifier("genericService")
	GenericService genericService;
	
	@Autowired
	@Qualifier("summaryService")
	SummaryService summaryService;

	@RequestMapping(value = "/physicalExam/list/summaryType/{summaryTypeId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Map<String, Map<String, List<GenericVO>>> getPhysicalExamSystemBySummaryType(@PathVariable("summaryTypeId") Long summaryTypeId) {
		Map<String, Map<String, List<GenericVO>>> physicalExamSystems = 
				this.summaryService.getPhysicalExamSystemsBySummaryType(summaryTypeId);
		return physicalExamSystems;
	}
	
	@RequestMapping(value="/summary/save",method = RequestMethod.POST)
	public BaseEntity saveSummary(@RequestBody GenericDto dto) throws JsonParseException, 
	JsonMappingException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, 
	IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
		BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
				Class.forName(Constants.PACKAGE_NAME + "Summary"));
		summaryService.save((Summary)obj);
			
		return obj;
	}
	
	@RequestMapping(value="/get/{id}", method = RequestMethod.GET)
	public BaseEntity getSummary(@PathVariable("id") Long id) throws ClassNotFoundException {
		BaseEntity result = summaryService.findSummary(Class.forName(Constants.PACKAGE_NAME + "Summary"), id);
		
		return result;
	}
	
	@RequestMapping(value="/getByPresence/{label}/{labelId}", method = RequestMethod.GET)
	public BaseEntity getSummaryByPresence(@PathVariable("label") String label, @PathVariable("labelId") Long labelId) 
			throws ClassNotFoundException {
		BaseEntity result = summaryService.findSummaryByPresence(label, labelId);
		
		return result;
	}
	
	@RequestMapping(value="/physicalExam/save",method = RequestMethod.POST)
	public BaseEntity savePhysicalExam(@RequestBody GenericDto dto) throws JsonParseException, 
	JsonMappingException, IOException, ClassNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
				Class.forName(Constants.PACKAGE_NAME + "PhysicalExam"));
		summaryService.save((PhysicalExam)obj);
			
		return obj;
	}
	
	@RequestMapping(value="/physicalExam/get/{id}", method = RequestMethod.GET)
	public BaseEntity getPhysicalExam(@PathVariable("id") Long id) throws ClassNotFoundException{
		BaseEntity result = summaryService.findPhysicalExam(Class.forName(Constants.PACKAGE_NAME + "PhysicalExam"), id);
		
		return result;
	}
	
	
	
	// System Review section
	@RequestMapping(value = "/systemReview/list/summaryType/{summaryTypeId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Map<String, Map<String, List<GenericVO>>> getSystemReviewQuestionBySummaryType(@PathVariable("summaryTypeId") Long summaryTypeId) {
		Map<String, Map<String, List<GenericVO>>> systemReviewQuestions = 
				this.summaryService.getSystemReviewQuestionsBySummaryType(summaryTypeId);
		return systemReviewQuestions;
	}
	
	@RequestMapping(value="/systemReview/save",method = RequestMethod.POST)
	public BaseEntity saveSystemReview(@RequestBody GenericDto dto) throws JsonParseException, 
	JsonMappingException, IOException, ClassNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
				Class.forName(Constants.PACKAGE_NAME + "SystemReview"));
		summaryService.save((SystemReview)obj);
			
		return obj;
	}
	
	@RequestMapping(value="/systemReview/get/{id}", method = RequestMethod.GET)
	public BaseEntity getSystemReview(@PathVariable("id") Long id) throws ClassNotFoundException{
		BaseEntity result = summaryService.findSystemReview(Class.forName(Constants.PACKAGE_NAME + "SystemReview"), id);
		
		return result;
	}

}
