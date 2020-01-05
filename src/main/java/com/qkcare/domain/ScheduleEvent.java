package com.qkcare.domain;

import com.qkcare.model.Appointment;

public class ScheduleEvent {

	private Long id;
	private Long patientId;
	private Long employeeId;
	private String title;
	private String start;
	private String end;
	private String className;
	private String color;
	private String phone;
	private String docName;
	private String docFirstName;
	private String docLastName;
	private String designation;
	private String location;

	public ScheduleEvent() {

	}

	public ScheduleEvent(Appointment apt) {
		this.start = apt.getBeginTime();
		this.end = apt.getEndTime();
		this.docName = apt.getDoctorName();
		this.docFirstName = apt.getDoctor() == null ? null : apt.getDoctor().getFirstName();
		this.docLastName = apt.getDoctor() == null ? null : apt.getDoctor().getLastName();
		this.location = apt.getHospitalLocation() == null ? null : apt.getHospitalLocation().getName();
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDocFirstName() {
		return docFirstName;
	}

	public void setDocFirstName(String docFirstName) {
		this.docFirstName = docFirstName;
	}

	public String getDocLastName() {
		return docLastName;
	}

	public void setDocLastName(String docLastName) {
		this.docLastName = docLastName;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
