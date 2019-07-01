package com.qkcare.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qkcare.util.DateUtil;

@Entity
@Table(name = "APPOINTMENT")
public class Appointment extends BaseEntity {

	@Id
	@Column(name = "APPOINTMENT_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "DOCTOR_ID")
	private Employee doctor;
	@ManyToOne
	@JoinColumn(name = "DEPARTMENT_ID")
	private Department department;
	@ManyToOne
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;
	@ManyToOne
	@JoinColumn(name = "HOSPITAL_LOCATION_ID")
	private HospitalLocation hospitalLocation;
	@JsonFormat(timezone = "GMT-05:00")
	@Column(name = "APPOINTMENT_DATE")
	private Date appointmentDate;
	@Column(name = "BEGIN_TIME")
	private String beginTime;
	@Column(name = "END_TIME")
	private String endTime;
	private String problem;
	private int status;

	@Transient
	private String statusDesc;

	public Appointment() {
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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public HospitalLocation getHospitalLocation() {
		return hospitalLocation;
	}

	public void setHospitalLocation(HospitalLocation hospitalLocation) {
		this.hospitalLocation = hospitalLocation;
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

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getDoctorName() {
		return this.doctor != null ? this.doctor.getName() : "";
	}

	public String getDepartmentName() {
		return this.department != null ? this.department.getName() : "";
	}

	public String getDescription() {
		return DateUtil.formatDate(this.getAppointmentDate(), "dd/MM/yyyy") + " - " + this.getBeginTime() + " - "
				+ this.getDoctorName();
	}

	private String toValue(String value) {
		return StringUtils.isEmpty(value) ? "" : value;
	}

	public String getStatusDesc() {
		switch (status) {
		case 0:
			this.statusDesc = "Nouveau";
			break;
		case 1:
			this.statusDesc = "Confirme";
			break;
		case 2:
			this.statusDesc = "Complete";
			break;
		case 3:
			this.statusDesc = "Annule";
			break;
		}
		return this.statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		// 0 for Canceled, 1 for Saved, 2 for Comfirmed, 3 for Honored
		switch (status) {
		case 0:
			this.statusDesc = "Nouveau";
			break;
		case 1:
			this.statusDesc = "Confirme";
			break;
		case 2:
			this.statusDesc = "Complete";
			break;
		case 3:
			this.statusDesc = "Annule";
			break;
		}
	}

}
