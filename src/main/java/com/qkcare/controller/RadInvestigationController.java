package com.qkcare.controller;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.qkcare.domain.GenericDto;
import com.qkcare.domain.SearchCriteria;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.Prescription;
import com.qkcare.model.PrescriptionDiagnosis;
import com.qkcare.model.PrescriptionMedicine;
import com.qkcare.model.imaging.RadInvestigation;
import com.qkcare.model.imaging.RadInvestigationComment;
import com.qkcare.model.imaging.RadInvestigationExam;
import com.qkcare.service.GenericService;
import com.qkcare.service.RadInvestigationService;
import com.qkcare.util.Constants;


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
				
				if (((com.qkcare.model.imaging.RadInvestigation) obj).getInvestigationComments() != null) {
					for (RadInvestigationExam rie : ((com.qkcare.model.imaging.RadInvestigation) obj).getInvestigationExams()) {
						rie.setInvestigation(null);
					}
				}
				if (((com.qkcare.model.imaging.RadInvestigation) obj).getInvestigationComments() != null) {
					for (RadInvestigationComment ric : ((com.qkcare.model.imaging.RadInvestigation) obj).getInvestigationComments()) {
						ric.setInvestigation(null);
					}
				}
				
			} catch(Exception e) {
				e.printStackTrace();
				obj.setErrors(Arrays.asList(e.getMessage()));
			}
			
			return obj;
		}
		
		
		@RequestMapping(value = "investigation/get/{id}", method = RequestMethod.GET)
		public BaseEntity getInvestigation(@PathVariable("id") Long id) throws ClassNotFoundException {
			BaseEntity result = investigationService.findInvestigation(Class.forName(Constants.PACKAGE_IMAGING_NAME + "RadInvestigation"),
					id);

			return result;
		}
		
		
		@RequestMapping(value = "/investigation/search", method = RequestMethod.POST)
		public List<BaseEntity> searchInvestigations(@RequestBody SearchCriteria searchCriteria) throws ClassNotFoundException {
			List<BaseEntity> investigations = this.investigationService.getInvestigations(searchCriteria);
			return investigations;
		}
		
		
		@RequestMapping(value = "/upload", method = RequestMethod.POST)
		public BaseEntity save(@RequestPart("file") MultipartFile file, @RequestPart RadInvestigation entity)
				throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
			BaseEntity obj = null;
			try {
				Long myId = entity.getId();
			} catch (Exception e) {
				e.printStackTrace();
				obj.setErrors(Arrays.asList(e.getMessage()));
			}

			return obj;
		}
		
}
