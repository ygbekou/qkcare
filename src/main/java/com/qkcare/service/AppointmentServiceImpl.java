package com.qkcare.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkcare.dao.AppointmentDao;
import com.qkcare.dao.GenericDao;
import com.qkcare.domain.ScheduleEvent;
import com.qkcare.domain.SearchCriteria;
import com.qkcare.model.Appointment;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.Prescription;
import com.qkcare.model.PrescriptionDiagnosis;
import com.qkcare.model.PrescriptionMedicine;
import com.qkcare.model.Schedule;

@Service(value = "appointmentService")
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	GenericService genericService;

	@Autowired
	AppointmentDao appointmentDao;

	@Autowired
	GenericDao genericDao;

	@Transactional
	public List<ScheduleEvent> getScheduleEvents(SearchCriteria searchCriteria) {
		List<ScheduleEvent> scheduleEvents = this.appointmentDao.getScheduleEvents(searchCriteria);
		Map<String, String> apptMap = scheduleEvents.stream()
				.collect(Collectors.toMap(ScheduleEvent::getStart, ScheduleEvent::getEnd));
		scheduleEvents.addAll(this.generateDoctorAvailabilities(searchCriteria, apptMap));

		return scheduleEvents;
	}

	/**
	 * Get today's appointments
	 */
	public List<ScheduleEvent> getTodayAppointments(SearchCriteria searchCriteria) {
		List<ScheduleEvent> scheduleEvents = this.appointmentDao.getTodayAppointments(searchCriteria);
		return scheduleEvents;
	}

	@Transactional
	public BaseEntity save(Prescription prescription) {

		BaseEntity toReturn = this.genericService.save(prescription);

		for (PrescriptionMedicine pm : prescription.getPrescriptionMedicines()) {
			if (pm.getMedicine() != null && pm.getMedicine().getId() != null) {
				pm.setPrescription((Prescription) toReturn);
				this.genericService.save(pm);
			}
		}

		for (PrescriptionDiagnosis pd : prescription.getPrescriptionDiagnoses()) {
			if (StringUtils.isNotEmpty(pd.getDiagnosis())) {
				pd.setPrescription((Prescription) toReturn);
				this.genericService.save(pd);
			}
		}

		return toReturn;
	}

	public Map<Integer, List<Appointment>> getAppointmentsByMonth(Long id) {

		LocalDate today = LocalDate.now();
		LocalDate startDate = today.withDayOfMonth(1).plusMonths(-12);
		LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(
				Quartet.with("e.appointmentDate >= ", "appointmentStartDate", startDate.format(formatter), "Date"));
		paramTupleList
				.add(Quartet.with("e.appointmentDate <= ", "appointmentEndDate", endDate.format(formatter), "Date"));
		String queryStr = "SELECT e FROM Appointment e WHERE 1 = 1";

		if (id != null && id > 0) {
			queryStr += " AND e.patient.user.id=" + id;
		}
		List<Appointment> appointments = (List) this.genericService.getByCriteria(queryStr, paramTupleList,
				" ORDER BY appointmentDate");

		Map<Integer, List<Appointment>> dataMap = new HashMap<>();

		for (Appointment appointment : appointments) { 		 
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(appointment.getAppointmentDate());
			Integer monthIndex = calendar.get(Calendar.MONTH);
			if (dataMap.get(monthIndex) == null) {
				dataMap.put(monthIndex, new ArrayList<Appointment>());
			}

			dataMap.get(monthIndex).add(appointment);
		}

		return dataMap;
	}

	/**
	 * This method is really meant for patients
	 */
	public Map<Integer, List<Appointment>> getAppointmentsByYear(Long id) { 

		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
 
		String queryStr = "SELECT e FROM Appointment e WHERE 1 = 1";

		if (id != null && id > 0) {
			queryStr += " AND e.patient.user.id=" + id;
		}
		List<Appointment> appointments = (List) this.genericService.getByCriteria(queryStr, paramTupleList,
				" ORDER BY appointmentDate");

		Map<Integer, List<Appointment>> dataMap = new HashMap<>();

		for (Appointment appointment : appointments) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(appointment.getAppointmentDate());
			Integer monthIndex = calendar.get(Calendar.YEAR);
			if (dataMap.get(monthIndex) == null) {
				dataMap.put(monthIndex, new ArrayList<Appointment>());
			}

			dataMap.get(monthIndex).add(appointment);
		}

		return dataMap;
	}
	public List<Appointment> getUpcomingAppointments() {

		LocalDate startDate = LocalDate.now().plusDays(-365);
		LocalDate endDate = LocalDate.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(
				Quartet.with("e.appointmentDate >= ", "appointmentStartDate", startDate.format(formatter), "Date"));
		paramTupleList
				.add(Quartet.with("e.appointmentDate <= ", "appointmentEndDate", endDate.format(formatter), "Date"));
		paramTupleList.add(Quartet.with("e.status <= ", "status", "0", "Integer"));

		String queryStr = "SELECT e FROM Appointment e WHERE 1 = 1";

		List<Appointment> appointments = (List) this.genericService.getByCriteria(queryStr, paramTupleList,
				" ORDER BY appointmentDate");

		return appointments;
	}

	private List<ScheduleEvent> generateDoctorAvailabilities(SearchCriteria searchCriteria,
			Map<String, String> apptMap) {
		List<ScheduleEvent> events = new ArrayList<ScheduleEvent>();
		LocalDate startDate = LocalDate.now().withDayOfMonth(1).plusMonths(-1);
		LocalDate endDate = LocalDate.now().withDayOfMonth(1).plusMonths(2);

		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		if (searchCriteria.getDoctor().getId() != null) {
			paramTupleList
					.add(Quartet.with("e.doctor.id = ", "doctorId", searchCriteria.getDoctor().getId() + "", "Long"));
		}
		if (searchCriteria.getDepartment().getId() != null) {
			paramTupleList.add(Quartet.with("e.doctor.department.id = ", "departmentId",
					searchCriteria.getDepartment().getId() + "", "Long"));
		}
		if (searchCriteria.getHospitalLocation().getId() != null) {
			paramTupleList.add(Quartet.with("e.hospitalLocation.id = ", "hospitalLocationId",
					searchCriteria.getHospitalLocation().getId() + "", "Long"));
		}
		String queryStr = "SELECT e FROM Schedule e WHERE 1 = 1";
		List<Schedule> schedules = (List) this.genericService.getByCriteria(queryStr, paramTupleList, null);

		for (Schedule schedule : schedules) {
			for (String dateStr : getDates(startDate, endDate, schedule.getWeekday().getName())) {

				for (Pair<String, String> timePair : getTimes(schedule.getBeginTime(), schedule.getEndTime(),
						schedule.getPerPatientTime())) {
					String start = dateStr + "T" + timePair.getValue0();
					if (apptMap.get(start) == null) {
						ScheduleEvent event = new ScheduleEvent();
						event.setTitle(timePair.getValue0() + " - " + timePair.getValue1());

						event.setStart(start);
						event.setEnd(dateStr + "T" + timePair.getValue1());
						events.add(event);
					}
				}
			}
		}

		return events;

	}

	private List<String> getDates(LocalDate startDate, LocalDate endDate, String weekDay) {
		List<String> days = new ArrayList<String>();
		LocalDate thisDate = null;

		thisDate = this.getDate(weekDay, startDate);

		if (thisDate != null) {
			if (startDate.isAfter(thisDate)) {
				thisDate = thisDate.plusWeeks(1); // start on next week
			}

			while (thisDate.isBefore(endDate) || thisDate.isEqual(endDate)) {
				days.add(thisDate.toString());
				thisDate = thisDate.plusWeeks(1);
			}
		}
		return days;
	}

	private List<Pair<String, String>> getTimes(final String beginTime, final String endTime, String timePerPatient) {
		List<Pair<String, String>> times = new ArrayList<Pair<String, String>>();

		Integer beginHour = Integer.valueOf(beginTime.split(":")[0]);
		Integer beginMinute = Integer.valueOf(beginTime.split(":")[1]);

		Integer endHour = Integer.valueOf(beginTime.split(":")[0]);
		Integer endMinute = Integer.valueOf(beginTime.split(":")[1]);

		Integer finalEndHour = Integer.valueOf(endTime.split(":")[0]);
		Integer finalEndMinute = Integer.valueOf(endTime.split(":")[1]);

		Integer hourPerPatient = Integer.valueOf(timePerPatient.split(":")[0]);
		Integer mnPerPatient = Integer.valueOf(timePerPatient.split(":")[1]);

		while (endHour < finalEndHour || (endHour == finalEndHour && endMinute < finalEndMinute)) {
			endHour += hourPerPatient;
			endMinute += mnPerPatient;

			if (endMinute >= 60) {
				++endHour;
				endMinute -= 60;
			}
			times.add(Pair.with(
					StringUtils.leftPad(beginHour.toString(), 2, "0") + ":"
							+ StringUtils.leftPad(beginMinute.toString(), 2, "0"),
					StringUtils.leftPad(endHour.toString(), 2, "0") + ":"
							+ StringUtils.leftPad(endMinute.toString(), 2, "0")));

			beginHour = endHour;
			beginMinute = endMinute;
		}

		return times;
	}

	private LocalDate getDate(String weekDay, LocalDate relativeDate) {
		LocalDate myDate = null;
		if ("LUNDI".equalsIgnoreCase(weekDay) || "MONDAY".equalsIgnoreCase(weekDay))
			myDate = relativeDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
		else if ("MARDI".equalsIgnoreCase(weekDay) || "TUESDAY".equalsIgnoreCase(weekDay))
			myDate = relativeDate.with(TemporalAdjusters.previous(DayOfWeek.TUESDAY));
		else if ("MERCREDI".equalsIgnoreCase(weekDay) || "WEDNESDAY".equalsIgnoreCase(weekDay))
			myDate = relativeDate.with(TemporalAdjusters.previous(DayOfWeek.WEDNESDAY));
		else if ("JEUDI".equalsIgnoreCase(weekDay) || "THURSDAY".equalsIgnoreCase(weekDay))
			myDate = relativeDate.with(TemporalAdjusters.previous(DayOfWeek.THURSDAY));
		else if ("VENDREDI".equalsIgnoreCase(weekDay) || "FRIDAY".equalsIgnoreCase(weekDay))
			myDate = relativeDate.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
		else if ("SAMEDI".equalsIgnoreCase(weekDay) || "SATURDAY".equalsIgnoreCase(weekDay))
			myDate = relativeDate.with(TemporalAdjusters.previous(DayOfWeek.SATURDAY));
		else if ("DIMANCHE".equalsIgnoreCase(weekDay) || "SUNDAY".equalsIgnoreCase(weekDay))
			myDate = relativeDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));

		return myDate;
	}

	@Override
	public Appointment getNextAppointment(Long id) {
		// TODO Auto-generated method stub
		LocalDate startDate = LocalDate.now();
		// LocalDate endDate = LocalDate.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(
				Quartet.with("e.appointmentDate >= ", "appointmentStartDate", startDate.format(formatter), "Date"));

		paramTupleList.add(Quartet.with("e.status <= ", "status", "0", "Integer"));

		paramTupleList.add(Quartet.with("e.patient.user.id = ", "userId", id + "", "Long"));

		String queryStr = "SELECT e FROM Appointment e WHERE 1 = 1";

		List<Appointment> appointments = (List) this.genericService.getByCriteria(queryStr, paramTupleList,
				" ORDER BY appointmentDate");

		if (appointments != null && appointments.size() > 0) {
			return appointments.get(0);
		}
		return null;
	}

}
