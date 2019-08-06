package com.qkcare.controller;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
		
		
		@RequestMapping(value = "/investigation/search", method = RequestMethod.POST)
		public List<BaseEntity> searchInvestigations(@RequestBody SearchCriteria searchCriteria) throws ClassNotFoundException {
			List<BaseEntity> investigations = this.investigationService.getInvestigations(searchCriteria);
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
