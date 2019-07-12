package com.qkcare.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.qkcare.domain.ScheduleEvent;
import com.qkcare.domain.SearchCriteria;

@SuppressWarnings("unchecked")
@Repository
public class AppointmentDaoImpl implements AppointmentDao {
	private static Logger logger = Logger.getLogger(UserDaoImpl.class);

	@Autowired
	private EntityManager entityManager;

	@Value("${schedule.appointment.new.color}")
	private String scheduleAppointmentNewColor;

	@Value("${schedule.appointment.confirm.color}")
	private String scheduleAppointmentConfirmColor;

	public List<ScheduleEvent> getScheduleEvents(SearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		List<ScheduleEvent> events = new ArrayList<ScheduleEvent>();
		try {
			StringBuilder sqlBuilder = new StringBuilder("SELECT AP.APPOINTMENT_ID, U.FIRST_NAME, U.MIDDLE_NAME, "
					+ "U.LAST_NAME, AP.APPOINTMENT_DATE, AP.BEGIN_TIME, AP.END_TIME, AP.STATUS "
					+ "FROM APPOINTMENT AP " + "JOIN PATIENT P ON AP.PATIENT_ID = P.PATIENT_ID "
					+ "JOIN USERS U ON P.USER_ID = U.USER_ID " + "WHERE 1 = 1 AND AP.STATUS <3 ");

			if (searchCriteria.hasDoctorId()) {
				sqlBuilder.append(" AND AP.DOCTOR_ID = :doctorId ");
			}
			if (searchCriteria.hasHospitalLocationId()) {
				sqlBuilder.append(" AND AP.HOSPITAL_LOCATION_ID = :locationId ");
			}
			if (searchCriteria.hasDepartmentId()) {
				sqlBuilder.append(" AND AP.DEPARTMENT_ID = :departmentId ");
			}

			Query query = entityManager.createNativeQuery(sqlBuilder.toString());

			if (searchCriteria.hasDoctorId()) {
				query.setParameter("doctorId", searchCriteria.getDoctor().getId());
			}
			if (searchCriteria.hasDepartmentId()) {
				query.setParameter("departmentId", searchCriteria.getDepartment().getId());
			}
			if (searchCriteria.hasHospitalLocationId()) {
				query.setParameter("locationId", searchCriteria.getHospitalLocation().getId());
			}

			List<Object[]> list = query.getResultList();

			for (Object[] obj : list) {
				ScheduleEvent event = new ScheduleEvent();
				event.setId(new Long(obj[0].toString()));
				event.setTitle((String) (obj[1] == null ? "" : obj[1]) + " " + (String) (obj[2] == null ? "" : obj[2])
						+ " " + (String) (obj[3] == null ? "" : obj[3]));
				event.setStart(obj[4].toString().split(" ")[0] + "T" + obj[5].toString());
				event.setEnd(obj[4].toString().split(" ")[0] + "T" + obj[6].toString());
				event.setClassName("availability");
				if (obj[7].toString().equals("0")) // This is for saved ones
					event.setColor(scheduleAppointmentNewColor);
				else if (obj[7].toString().equals("1")) // This is for confirmed ones
					event.setColor(scheduleAppointmentConfirmColor);
				events.add(event);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return events;
		}

	}

	@Override
	public List<ScheduleEvent> getTodayAppointments(SearchCriteria searchCriteria) {
		// TODO Auto-generated method stub
		List<ScheduleEvent> events = new ArrayList<ScheduleEvent>();
		try {
			/**
			 * Status 0- new STatus 1- confirmed
			 */
			StringBuilder sqlBuilder = new StringBuilder("SELECT AP.APPOINTMENT_ID, U.FIRST_NAME, U.MIDDLE_NAME, "
					+ "U.LAST_NAME, AP.APPOINTMENT_DATE, AP.BEGIN_TIME, AP.END_TIME, AP.STATUS, U.HOME_PHONE, "
					+ "CONCAT_WS (' ',EMP.DESIGNATION,U2.FIRST_NAME, U2.MIDDLE_NAME, U2.LAST_NAME) DOCNAME, "
					+ "P.PATIENT_ID, EMP.EMPLOYEE_ID ,U2.FIRST_NAME DRFNAME, U2.LAST_NAME DRLASTNAME, EMP.DESIGNATION "
					+ "FROM APPOINTMENT AP " + "JOIN PATIENT P ON AP.PATIENT_ID = P.PATIENT_ID "
					+ "JOIN USERS U ON P.USER_ID = U.USER_ID "
					+ "JOIN EMPLOYEE EMP ON (EMP.EMPLOYEE_ID = AP.DOCTOR_ID) "
					+ "JOIN USERS U2 ON (U2.USER_ID = EMP.USER_ID) " + "WHERE 1 = 1 AND AP.STATUS IN (0,1) "
					+ "AND DATE_FORMAT(SYSDATE(), '%Y-%m-01') = DATE_FORMAT(AP.APPOINTMENT_DATE, '%Y-%m-01') "
					+ "ORDER BY AP.APPOINTMENT_DATE, AP.BEGIN_TIME  " + "LIMIT " + searchCriteria.getTopN());

			Query query = entityManager.createNativeQuery(sqlBuilder.toString());

			List<Object[]> list = query.getResultList();

			for (Object[] obj : list) {
				ScheduleEvent event = new ScheduleEvent();
				event.setId(new Long(obj[0].toString()));
				event.setTitle((String) obj[1] + " " + (String) (obj[2] == null ? "" : obj[2]) + " " + (String) obj[3]);
				event.setStart(obj[5].toString());
				event.setEnd(obj[6].toString());
				event.setClassName("availability");
				if (obj[7].toString().equals("0")) // This is for saved ones
					event.setColor(scheduleAppointmentNewColor);
				else if (obj[7].toString().equals("1")) // This is for confirmed ones
					event.setColor(scheduleAppointmentConfirmColor);
				event.setPhone((String) obj[8]);
				event.setDocName((String) obj[9]);
				event.setPatientId(new Long(obj[10].toString()));
				event.setEmployeeId(new Long(obj[11].toString()));
				event.setDocFirstName((String) obj[12]);
				event.setDocLastName((String) obj[13]);
				event.setDesignation((String) obj[14]);
				events.add(event);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return events;
	}

}
