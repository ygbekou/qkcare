package com.qkcare.controller;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.qkcare.domain.GenericDto;
import com.qkcare.domain.SearchCriteria;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.InvestigationTest;
import com.qkcare.model.LabTest;
import com.qkcare.service.GenericService;
import com.qkcare.service.InvestigationService;


@RestController
@RequestMapping(value="/service/laboratory")
@CrossOrigin
public class InvestigationController extends BaseController {
	
		private static final Logger LOGGER = Logger.getLogger(InvestigationController.class);
	
		@Autowired 
		@Qualifier("investigationService")
		InvestigationService investigationService;
		
		@Autowired
		@Qualifier("genericService")
		GenericService genericService;
				
		@RequestMapping(value="/investigation/save",method = RequestMethod.POST)
		public BaseEntity save(@RequestBody GenericDto dto) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			BaseEntity obj = null;
			try {
				obj = (BaseEntity) this.genericDtoToEntiityClassObject(dto, "Investigation");
				investigationService.save((com.qkcare.model.Investigation)obj);
			} catch(Exception e) {
				e.printStackTrace();
				obj.setErrors(Arrays.asList(e.getMessage()));
			}
			
			return obj;
		}
		
		@RequestMapping(value="/investigationTest/list/save",method = RequestMethod.POST)
		public Map saveInvestigationTests(@RequestBody List<InvestigationTest> investigationTests) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			investigationService.saveInvestigationTests(investigationTests);
			
			return Collections.singletonMap("response", "SUCCESS");
		}
		
		@RequestMapping(value="/investigationTest/list/byDate",method = RequestMethod.POST)
		public List<JSONObject> getInvestigationTests(@RequestBody SearchCriteria searchCriteria) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			String queryHeader = "SELECT it FROM InvestigationTest it "
				+ "JOIN it.investigation e ";
			List<BaseEntity> investigationTests = this.investigationService.getInvestigations(searchCriteria, queryHeader);
			Map<String, List<BaseEntity>> investMap = new HashMap<>();
			
			Map<String, List<BaseEntity>> invTestMap = investigationTests.stream()
				     .collect(Collectors.groupingBy(x -> ((InvestigationTest)x).getName())); 
			
			List<JSONObject> jsonObjects = new ArrayList<>();
			JSONObject jsonObject1 = new JSONObject();
			jsonObjects.add(jsonObject1);
			List<String> attributeList = new ArrayList<>();
			
			for (Map.Entry mapElement : invTestMap.entrySet()) { 
	            String key = (String)mapElement.getKey(); 
	            
	            JSONObject jsonObject = new JSONObject();
	            jsonObject.put("name", key);
	  
	            for (BaseEntity entity: (List<BaseEntity>)mapElement.getValue()) {
					InvestigationTest invTest = (InvestigationTest)entity;
					jsonObject.put(invTest.getInvestigationDate(), invTest.getResult());
					attributeList.add(invTest.getInvestigationDate());
				}
	            
	            jsonObjects.add(jsonObject);
	        } 
			
			Collections.sort(attributeList);
			Collections.reverse(attributeList);
			jsonObject1.put("attributes", attributeList);
			
			return jsonObjects;
		}
		
		
		@RequestMapping(value = "/investigation/search", method = RequestMethod.POST)
		public List<BaseEntity> searchInvestigations(@RequestBody SearchCriteria searchCriteria) throws ClassNotFoundException {
			List<BaseEntity> investigations = this.investigationService.getInvestigations(searchCriteria, "SELECT e FROM Investigation e ");
			return investigations;
		}

		
		@RequestMapping(value="/investigationTest/labTest/save",method = RequestMethod.POST)
		public LabTest saveLabTest(@RequestBody GenericDto dto) throws JsonParseException, 
					JsonMappingException, IOException, ClassNotFoundException {
			LabTest obj = null;
			try {
				obj = (LabTest) this.genericDtoToEntiityClassObject(dto, "LabTest");
				obj = investigationService.saveLabTest((com.qkcare.model.LabTest)obj);
			} catch(Exception e) {
				e.printStackTrace();
				obj.setErrors(Arrays.asList(e.getMessage()));
			}
			
			return obj;
		}

		
}
