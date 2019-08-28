package com.qkcare.controller;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
import com.qkcare.service.GenericService;
import com.qkcare.service.RadInvestigationService;


@RestController
@RequestMapping(value="/service/radiology")
@CrossOrigin
public class RadInvestigationController extends BaseController {
	
		private static final Logger LOGGER = Logger.getLogger(RadInvestigationController.class);
	
		@Autowired 
		@Qualifier("radInvestigationService")
		RadInvestigationService investigationService;
		
		@Autowired
		@Qualifier("genericService")
		GenericService genericService;
				
		@RequestMapping(value="/investigation/save",method = RequestMethod.POST)
		public BaseEntity save(@RequestBody GenericDto dto) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			BaseEntity obj = null;
			try {
				obj = (BaseEntity) this.genericDtoToEntiityClassObject(dto, "com.qkcare.model.imaging.RadInvestigation");
				investigationService.save((com.qkcare.model.imaging.RadInvestigation)obj);
			} catch(Exception e) {
				e.printStackTrace();
				obj.setErrors(Arrays.asList(e.getMessage()));
			}
			
			return obj;
		}
		
		
		@RequestMapping(value = "/investigation/search", method = RequestMethod.POST)
		public List<BaseEntity> searchInvestigations(@RequestBody SearchCriteria searchCriteria) throws ClassNotFoundException {
			List<BaseEntity> investigations = this.investigationService.getInvestigations(searchCriteria);
			return investigations;
		}
		
}
