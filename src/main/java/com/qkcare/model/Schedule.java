package com.qkcare.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.exolab.castor.types.DateTime;

@Entity
@Table(name = "SCHEDULE")
public class Schedule extends BaseEntity {

	@Id
	@Column(name = "SCHEDULE_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "DOCTOR_ID")
	private Employee doctor;
	@ManyToOne
	@JoinColumn(name = "HOSPITAL_LOCATION_ID")
	private HospitalLocation hospitalLocation;
	@ManyToOne
	@JoinColumn(name = "WEEK_DAY_ID")
	private Weekday weekday;
	@Column(name = "BEGIN_TIME")
	private String beginTime;
	@Column(name = "END_TIME")
	private String endTime;
	@Column(name = "PER_PATIENT_TIME")
	private String perPatientTime;
	private int status;

	public Schedule() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getDoctor() {
		return doctor;
	}

	public void setDoctor(Employee doctor) {
		this.doctor = doctor;
	}

	public HospitalLocation getHospitalLocation() {
		return hospitalLocation;
	}

	public void setHospitalLocation(HospitalLocation hospitalLocation) {
		this.hospitalLocation = hospitalLocation;
	}

	public Weekday getWeekday() {
		return weekday;
	}

	public void setWeekday(Weekday weekday) {
		this.weekday = weekday;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPerPatientTime() {
		return perPatientTime;
	}

	public void setPerPatientTime(String perPatientTime) {
		this.perPatientTime = perPatientTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	// TRansient fields for UI

	public String getDoctorName() {
		return this.doctor.getFirstName() + " " + toValue(this.doctor.getMiddleName() + " ")
				+ this.doctor.getLastName();
	}

	public String getDepartmentName() {
		return this.doctor.getDepartmentName();
	}

	public String getDay() {
		return this.weekday.getName();
	}

	public String getLocationName() {
		return this.hospitalLocation != null ? this.hospitalLocation.getName() : "";
	}

	private String toValue(String value) {
		return StringUtils.isEmpty(value) ? "" : value;
	}

	public LocalDateTime getBeginDateTime() {
		LocalDateTime dateTime = LocalDateTime.parse(LocalDate.now().toString() + "T" + this.getBeginTime());

		return dateTime;
	}

	public LocalDateTime getEndDateTime() {
		LocalDateTime dateTime = LocalDateTime.parse(LocalDate.now() + "T" + this.getEndTime());

		return dateTime;
	}

	public String getCustomValidator() {
		return "ScheduleCustomValidator";
	}
}
