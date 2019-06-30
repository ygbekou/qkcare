package com.qkcare.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "VISIT")
public class Visit extends BaseEntity {

	@Id
	@Column(name = "VISIT_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;
	@ManyToOne
	@JoinColumn(name = "PACKAGE_ID")
	private com.qkcare.model.Package pckage;
	
	@ManyToOne
	@JoinColumn(name = "DOCTOR_ID")
	private Employee doctor;
	
	@ManyToOne
	@JoinColumn(name = "APPOINTMENT_ID")
	private Appointment appointment;
	
	@Column(name = "CHIEF_OF_COMPLAIN")
	private String chiefOfComplain;
	@Column(name = "VISIT_DATETIME")
	private Timestamp visitDatetime;
	@Column(name = "IS_HEALTH_CHECKUP")
	private int isHealthCheckup;
	private int status;
	
	// Transient
	@Transient
	private VitalSign vitalSign;
	@Transient 
	List<Integer> isHealthCheckupSel;
	
	@Transient
	List<VisitVaccine> givenVaccines;
	
	@Transient
	private Set<Long> selectedAllergies;
	
	@Transient
	private Set<Long> selectedSymptoms;
	
	@Transient
	private Set<Long> selectedMedicalHistories;
	
	@Transient
	private Set<Long> selectedSocialHistories;
	
	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public Visit() {}
	
	public Visit(Long id) {
		this.id = id;
	}

	public Visit(Appointment apt) {
		// TODO Auto-generated constructor stub
		this.appointment=apt;
		this.doctor=apt.getDoctor();
		this.status=0;
		this.patient=apt.getPatient();
		this.visitDatetime= new Timestamp(new Date().getTime());
		this.chiefOfComplain=apt.getProblem(); 
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public com.qkcare.model.Package getPckage() {
		return pckage;
	}
	public void setPckage(com.qkcare.model.Package pckage) {
		this.pckage = pckage;
	}
	public Employee getDoctor() {
		return doctor;
	}
	public void setDoctor(Employee doctor) {
		this.doctor = doctor;
	}
	public String getChiefOfComplain() {
		return chiefOfComplain;
	}
	public void setChiefOfComplain(String chiefOfComplain) {
		this.chiefOfComplain = chiefOfComplain;
	}
	public Timestamp getVisitDatetime() {
		return visitDatetime;
	}
	public void setVisitDatetime(Timestamp visitDatetime) {
		this.visitDatetime = visitDatetime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIsHealthCheckup() {
		return isHealthCheckup;
	}

	public void setIsHealthCheckup(int isHealthCheckup) {
		this.isHealthCheckup = isHealthCheckup;
	}
	public VitalSign getVitalSign() {
		return vitalSign;
	}
	public void setVitalSign(VitalSign vitalSign) {
		this.vitalSign = vitalSign;
	}
	public List<VisitVaccine> getGivenVaccines() {
		return givenVaccines;
	}
	public void setGivenVaccines(List<VisitVaccine> givenVaccines) {
		this.givenVaccines = givenVaccines;
	}
	public Set<Long> getSelectedAllergies() {
		return selectedAllergies;
	}
	public Set<Long> getSelectedSymptoms() {
		return selectedSymptoms;
	}
	public void setSelectedSymptoms(Set<Long> selectedSymptoms) {
		this.selectedSymptoms = selectedSymptoms;
	}
	public void setSelectedAllergies(Set<Long> selectedAllergies) {
		this.selectedAllergies = selectedAllergies;
	}
	public Set<Long> getSelectedMedicalHistories() {
		return selectedMedicalHistories;
	}
	public void setSelectedMedicalHistories(Set<Long> selectedMedicalHistories) {
		this.selectedMedicalHistories = selectedMedicalHistories;
	}
	public Set<Long> getSelectedSocialHistories() {
		return selectedSocialHistories;
	}
	public void setSelectedSocialHistories(Set<Long> selectedSocialHistories) {
		this.selectedSocialHistories = selectedSocialHistories;
	}

	public List<Integer> getIsHealthCheckupSel() {
		this.setIsHealthCheckupSel(Arrays.asList(isHealthCheckup));
		return isHealthCheckupSel;
	}

	public void setIsHealthCheckupSel(List<Integer> isHealthCheckupSel) {
		this.isHealthCheckupSel = isHealthCheckupSel;
		this.setIsHealthCheckup(this.isHealthCheckupSel.get(0));
	}
	
	
	// Transient
	
	public String getPatientId() {
		return this.getPatient().getMedicalRecordNumber();
	}
	public String getPatientName() {
		return this.getPatient().getName();
	}
	public String getDoctorName() {
		return this.getDoctor().getName();
	}
}
