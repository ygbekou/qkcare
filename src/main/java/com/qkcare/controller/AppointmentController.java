package com.qkcare.controller;

import java.io.IOException;
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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qkcare.domain.GenericDto;
import com.qkcare.domain.GenericResponse;
import com.qkcare.domain.ScheduleEvent;
import com.qkcare.domain.SearchCriteria;
import com.qkcare.model.Appointment;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.Prescription;
import com.qkcare.model.PrescriptionDiagnosis;
import com.qkcare.model.PrescriptionMedicine;
import com.qkcare.model.Visit;
import com.qkcare.service.AppointmentService;
import com.qkcare.service.GenericService;
import com.qkcare.util.Constants;

@RestController
@RequestMapping(value = "/service/appointment")
@CrossOrigin
public class AppointmentController extends BaseController {

	private static final Logger LOGGER = Logger.getLogger(AppointmentController.class);

	@Autowired
	@Qualifier("appointmentService")
	AppointmentService appointmentService;

	@Autowired
	@Qualifier("genericService")
	GenericService genericService;

	@RequestMapping(value = "/scheduleAndAppointments", method = RequestMethod.POST)
	public List<ScheduleEvent> get(@RequestBody SearchCriteria searchCriteria) throws ClassNotFoundException {
		List<ScheduleEvent> result = appointmentService.getScheduleEvents(searchCriteria);
		return result;
	}

	@RequestMapping(value = "/getTodayAppointments", method = RequestMethod.POST)
	public List<ScheduleEvent> getTodayAppointments(@RequestBody SearchCriteria searchCriteria)
			throws ClassNotFoundException {
		List<ScheduleEvent> result = appointmentService.getTodayAppointments(searchCriteria);
		return result;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public GenericResponse cancel(@RequestBody Long id) {
		GenericResponse gr = new GenericResponse();
		try {
			// Cancel appointment
			Appointment apt = (Appointment) genericService.find(Appointment.class, id);
			apt.setStatus(3);
			genericService.save(apt);
			// cancel visits
			List<BaseEntity> visits = genericService.getByCriteria(Visit.class, "appointment", id);
			if (visits != null && visits.size() > 0) {
				for (BaseEntity b : visits) {
					Visit v = (Visit) b;
					v.setStatus(3);
					genericService.save(v);
				}
			}
			gr.setResult("Success");
		} catch (Exception e) {
			gr.setResult(e.getMessage());
		}
		return gr;
	}

	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public GenericResponse confirm(@RequestBody Long id) {
		GenericResponse gr = new GenericResponse();
		try {
			// Confirm appointment
			Appointment apt = (Appointment) genericService.find(Appointment.class, id);
			apt.setStatus(1);
			genericService.save(apt);
			// cancel visits
			List<BaseEntity> visits = genericService.getByCriteria(Visit.class, "appointment", id);
			if (visits == null || visits.size() < 1) {
				genericService.save(new Visit(apt));
			}
			gr.setResult("Success");
		} catch (Exception e) {
			gr.setResult(e.getMessage());
		}
		return gr;
	}

	@RequestMapping(value = "/prescription/save", method = RequestMethod.POST)
	public BaseEntity save(@RequestBody GenericDto dto)
			throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		BaseEntity obj = (BaseEntity) mapper.readValue(dto.getJson().replaceAll("'", "\"").replaceAll("/", "\\/"),
				Class.forName(Constants.PACKAGE_NAME + "Prescription"));
		appointmentService.save((Prescription) obj);

		for (PrescriptionMedicine pm : ((Prescription) obj).getPrescriptionMedicines()) {
			pm.setPrescription(null);
		}
		for (PrescriptionDiagnosis pd : ((Prescription) obj).getPrescriptionDiagnoses()) {
			pd.setPrescription(null);
		}
		return obj;
	}

	@RequestMapping(value = "/list/byMonth", method = RequestMethod.GET, headers = "Accept=application/json")
	public Map<Integer, List<Appointment>> getAppointmentsByMonth() {
		return this.appointmentService.getAppointmentsByMonth();
	}

	@RequestMapping(value = "/list/upcomings", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Appointment> getUpcomingAppointments() {
		return this.appointmentService.getUpcomingAppointments();
	}

}
