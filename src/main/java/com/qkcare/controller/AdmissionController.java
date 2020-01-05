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
import com.qkcare.model.BaseEntity;
import com.qkcare.model.BedAssignment;
import com.qkcare.model.DoctorAssignment;
import com.qkcare.model.Admission;
import com.qkcare.model.enums.TransferType;
import com.qkcare.service.AdmissionService;
import com.qkcare.service.GenericService;
import com.qkcare.util.Constants;

@RestController
@RequestMapping(value = "/service/admission")
@CrossOrigin
public class AdmissionController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(AdmissionController.class);

	@Autowired
	@Qualifier("genericService")
	GenericService genericService;

	@Autowired
	@Qualifier("admissionService")
	AdmissionService admissionService;

	@RequestMapping(value = "/admission/save", method = RequestMethod.POST)
	public BaseEntity saveAdmission(@RequestBody GenericDto dto)
			throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
				Class.forName(Constants.PACKAGE_NAME + "Admission"));
		admissionService.save((Admission) obj);

		BedAssignment ba = ((Admission) obj).getBedAssignment();
		ba.setAdmission(null);

		DoctorAssignment da = ((Admission) obj).getDoctorAssignment();
		da.setAdmission(null);

		return obj;
	}

	@RequestMapping(value = "admission/get/{id}", method = RequestMethod.GET)
	public BaseEntity getAdmission(@PathVariable("id") Long id) throws ClassNotFoundException {
		BaseEntity result = admissionService.findAdmission(Class.forName(Constants.PACKAGE_NAME + "Admission"), id);

		return result;
	}

	@RequestMapping(value = "diagnosis/{id}/all", method = RequestMethod.GET)
	public List<BaseEntity> getAdmissionDiagnoses(@PathVariable("id") Long id) throws ClassNotFoundException {
		List<BaseEntity> results = admissionService.getAdmissionChilds("AdmissionDiagnosis", id);

		return results;
	}

	@RequestMapping(value = "prescription/{id}/all", method = RequestMethod.GET)
	public List<BaseEntity> getAdmissionPrescriptions(@PathVariable("id") Long id) throws ClassNotFoundException {
		List<BaseEntity> results = admissionService.getAdmissionChilds("Prescription", id);

		return results;
	}

	@RequestMapping(value = "prescription/get/{id}", method = RequestMethod.GET)
	public BaseEntity getPrescription(@PathVariable("id") Long id) throws ClassNotFoundException {
		BaseEntity result = admissionService.findPrescription(Class.forName(Constants.PACKAGE_NAME + "Prescription"),
				id);

		return result;
	}

	@RequestMapping(value = "/{transferType}/transfer", method = RequestMethod.POST)
	public BaseEntity transferDoctor(@PathVariable("transferType") TransferType transferType,
			@RequestBody GenericDto dto)
			throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		BaseEntity obj = null;
		if (transferType.equals(TransferType.DOCTOR)) {
			obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
					DoctorAssignment.class);
			this.admissionService.transferDoctor((DoctorAssignment) obj);
		} else if (transferType.equals(TransferType.BED)) {
			obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
					BedAssignment.class);
			this.admissionService.transferBed((BedAssignment) obj);
		}

		return obj;
	}

	@RequestMapping(value = "/list/byMonth/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Map<Integer, List<Admission>> getAdmissionsByMonth(@PathVariable("id") Long id) {
		Map<Integer, List<Admission>> admissions= this.admissionService.getAdmissionsByMonth(id);
		return admissions;
	}
	
	@RequestMapping(value = "/list/byYear/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Map<Integer, List<Admission>> getAdmissionsByYear(@PathVariable("id") Long id) {
		Map<Integer, List<Admission>> admissions= this.admissionService.getAdmissionsByYear(id);
		return admissions;
	}
}
