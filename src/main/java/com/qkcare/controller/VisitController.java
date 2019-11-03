package com.qkcare.controller;


import java.io.IOException;
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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkcare.domain.GenericDto;
import com.qkcare.domain.GenericResponse;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.DoctorOrder;
import com.qkcare.model.Patient;
import com.qkcare.model.Visit;
import com.qkcare.model.VitalSign;
import com.qkcare.service.DoctorOrderService;
import com.qkcare.service.GenericService;
import com.qkcare.service.VisitService;
import com.qkcare.util.Constants;


@RestController
@RequestMapping(value="/service/visit")
@CrossOrigin
public class VisitController extends BaseController {
	
		private static final Logger LOGGER = Logger.getLogger(VisitController.class);
	
		@Autowired 
		@Qualifier("genericService")
		GenericService genericService;
		
		@Autowired 
		@Qualifier("visitService")
		VisitService visitService;
		
		@Autowired 
		@Qualifier("doctorOrderService")
		DoctorOrderService doctorOrderService;
		
		@RequestMapping(value="/visit/save",method = RequestMethod.POST)
		public BaseEntity saveVisit(@RequestBody GenericDto dto) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
					Class.forName(Constants.PACKAGE_NAME + "Visit"));
			visitService.save((Visit)obj);
			
			VitalSign vs = ((Visit)obj).getVitalSign();
			vs.setVisit(null);
				
			return obj;
		}
		
		@RequestMapping(value="/allergies/save",method = RequestMethod.POST)
		public BaseEntity saveAllergies(@RequestBody Patient patient) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			visitService.saveAllergies(patient);
			
			return patient;
		}
		
		@RequestMapping(value="/medicalHistories/save",method = RequestMethod.POST)
		public BaseEntity saveMedicalHistories(@RequestBody Patient patient) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			visitService.saveMedicalHistories(patient);
			
			return patient;
		}
		
		@RequestMapping(value="/socialHistories/save",method = RequestMethod.POST)
		public BaseEntity saveSocilaHistories(@RequestBody Patient patient) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			visitService.saveSocialHistories(patient);
			
			return patient;
		}
		
		@RequestMapping(value="/visit/updateStatus", method = RequestMethod.POST)
		public BaseEntity updateStatus(@RequestBody Visit visit) {
			Visit oldVisit = (Visit) this.genericService.find(Visit.class, visit.getId());
			oldVisit.setStatus(visit.getStatus());
			this.genericService.save(oldVisit);			
			return oldVisit;
		}
		
		@RequestMapping(value="visit/get/{id}", method = RequestMethod.GET)
		public BaseEntity getVisit(@PathVariable("id") Long id) throws ClassNotFoundException{
			BaseEntity result = visitService.findVisit(Class.forName(Constants.PACKAGE_NAME + "Visit"), id);
			
			return result;
		}
		
		@RequestMapping(value="diagnosis/{id}/all", method = RequestMethod.GET)
		public List<BaseEntity> getVisitDiagnoses(@PathVariable("id") Long id) throws ClassNotFoundException{
			List<BaseEntity> results = visitService.getVisitChilds("VisitDiagnosis", id);
			
			return results;
		}
		
		@RequestMapping(value="prescription/{id}/all", method = RequestMethod.GET)
		public List<BaseEntity> getVisitPrescriptions(@PathVariable("id") Long id) throws ClassNotFoundException{
			List<BaseEntity> results = visitService.getVisitChilds("Prescription", id);
			
			return results;
		}
		
		@RequestMapping(value="prescription/get/{id}",method = RequestMethod.GET)
		public BaseEntity getPrescription(@PathVariable("id") Long id) throws ClassNotFoundException{
			BaseEntity result = visitService.findPrescription(Class.forName(Constants.PACKAGE_NAME + "Prescription"), id);
			
			return result;
		}
		
		@RequestMapping(value="/doctororder/save",method = RequestMethod.POST)
		public BaseEntity saveDoctorOrder(@RequestBody GenericDto dto) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
					Class.forName(Constants.PACKAGE_NAME + "DoctorOrder"));
			doctorOrderService.save((DoctorOrder)obj);
			
			return obj;
		}
		
		
		@RequestMapping(value="doctororder/get/{id}",method = RequestMethod.GET)
		public BaseEntity getDoctorOrder(@PathVariable("id") Long id) throws ClassNotFoundException{
			BaseEntity result = doctorOrderService.getById(id);
			
			return result;
		}
		
		
		@RequestMapping(value="/doctororder/changeStatus",method = RequestMethod.POST)
		public BaseEntity changeStatus(@RequestBody GenericDto dto) throws JsonParseException, 
		JsonMappingException, IOException, ClassNotFoundException {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
					Class.forName(Constants.PACKAGE_NAME + "DoctorOrder"));
			
			doctorOrderService.save((DoctorOrder)obj, true);
			
			return obj;
		}
		
		@RequestMapping(value = "/list/byMonth", method = RequestMethod.GET, headers = "Accept=application/json")
		public Map<Integer, List<Visit>> getVisitsByMonth() {
			Map<Integer, List<Visit>> visites= this.visitService.getVisitsByMonth();
			return visites;
		}
		
		@RequestMapping(value = "/getWaitList/{topN}", method = RequestMethod.GET, headers = "Accept=application/json")
		public  List<Visit>  getWaitList(@PathVariable("topN") int topN) {
			return this.visitService.getWaitList(topN);
		}
		
		@RequestMapping(value = "/endVisit", method = RequestMethod.POST)
		public GenericResponse endVisit(@RequestBody Long id) {
			GenericResponse gr = new GenericResponse();
			try {
				// Confirm appointment
				Visit visit = (Visit) genericService.find(Visit.class, id);
				visit.setStatus(1);
				genericService.save(visit); 
				gr.setResult("Success");
			} catch (Exception e) {
				gr.setResult(e.getMessage());
			}
			return gr;
		}
		
		@RequestMapping(value = "/cancelVisit", method = RequestMethod.POST)
		public GenericResponse cancelVisit(@RequestBody Long id) {
			GenericResponse gr = new GenericResponse();
			try {
				// Confirm appointment
				Visit visit = (Visit) genericService.find(Visit.class, id);
				visit.setStatus(3);
				genericService.save(visit); 
				gr.setResult("Success");
			} catch (Exception e) {
				gr.setResult(e.getMessage());
			}
			return gr;
		}
		
		@RequestMapping(value="patient/allergies/{id}", method = RequestMethod.GET)
		public BaseEntity getPatientAllergies(@PathVariable("id") Long id) throws ClassNotFoundException{
			
			BaseEntity result = visitService.getAllergies(new Patient(id));
			
			return result;
		}
		
		@RequestMapping(value="patient/medicalHistories/{id}", method = RequestMethod.GET)
		public BaseEntity getPatientMedicalHistories(@PathVariable("id") Long id) throws ClassNotFoundException{
			
			BaseEntity result = visitService.getMedicalHistories(new Patient(id));
			
			return result;
		}
		
		@RequestMapping(value="patient/socialHistories/{id}", method = RequestMethod.GET)
		public BaseEntity getPatientSocialHistories(@PathVariable("id") Long id) throws ClassNotFoundException{
			
			BaseEntity result = visitService.getSocialHistories(new Patient(id));
			
			return result;
		}

}
