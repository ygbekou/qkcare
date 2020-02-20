package com.qkcare.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.qkcare.domain.ScheduleEvent;
import com.qkcare.domain.SearchCriteria;
import com.qkcare.model.Appointment;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.Prescription;


@Service(value="appointmentService")
public interface AppointmentService {
	
	public List<ScheduleEvent> getScheduleEvents(SearchCriteria searchCriteria);
	
	public List<ScheduleEvent> getTodayAppointments(SearchCriteria searchCriteria);	
	
	public BaseEntity save(Prescription prescription);
	
	public List<Appointment> getUpcomingAppointments();
	
	public Map<Integer, List<Appointment>> getAppointmentsByMonth(Long id);

	public Appointment getNextAppointment(Long id);

	public Map<Integer, List<Appointment>> getAppointmentsByYear(Long id);

	public Prescription getLastPrescription(Long id);

	public List<ScheduleEvent> getFutureAvailableSpots(SearchCriteria searchCriteria);

	public Map<Integer, List<Prescription>>  getUserPrescriptions(Long id);
}
