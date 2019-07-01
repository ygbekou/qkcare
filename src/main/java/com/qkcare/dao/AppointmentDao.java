package com.qkcare.dao;

import java.util.List;

import com.qkcare.domain.ScheduleEvent;
import com.qkcare.domain.SearchCriteria;

public interface AppointmentDao {
	public List<ScheduleEvent> getScheduleEvents(SearchCriteria searchCriteria);

	public List<ScheduleEvent> getTodayAppointments(SearchCriteria searchCriteria);
}
