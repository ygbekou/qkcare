package com.qkcare.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ADMISSION")
public class Admission extends BaseEntity {

	@Id
	@Column(name = "ADMISSION_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;
	@ManyToOne
	@JoinColumn(name = "PACKAGE_ID")
	private com.qkcare.model.Package pckage;
	@Column(name = "ADMISSION_DATETIME")
	private Timestamp admissionDatetime;
	@Column(name = "CONTACT_NAME")
	private String contatName;
	@Column(name = "CONTACT_RELATION")
	private String contactRelation;
	@Column(name = "CONTACT_PHONE")
	private String contactPhone;
	@Column(name = "CONTACT_ADDRESS")
	private String contactAddress;
	private int status;
	
	// Transient object
	@Transient
	BedAssignment bedAssignment;
	@Transient
	DoctorAssignment doctorAssignment;
	

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
	public Timestamp getAdmissionDatetime() {
		return admissionDatetime;
	}
	public void setAdmissionDatetime(Timestamp admissionDatetime) {
		this.admissionDatetime = admissionDatetime;
	}
	public String getContatName() {
		return contatName;
	}
	public void setContatName(String contatName) {
		this.contatName = contatName;
	}
	public String getContactRelation() {
		return contactRelation;
	}
	public void setContactRelation(String contactRelation) {
		this.contactRelation = contactRelation;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactAddress() {
		return contactAddress;
	}
	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public BedAssignment getBedAssignment() {
		return bedAssignment;
	}
	public void setBedAssignment(BedAssignment bedAssignment) {
		this.bedAssignment = bedAssignment;
	}
	public DoctorAssignment getDoctorAssignment() {
		return doctorAssignment;
	}
	public void setDoctorAssignment(DoctorAssignment doctorAssignment) {
		this.doctorAssignment = doctorAssignment;
	}
	
	
	// Transient attributes
	public String getPatientMRN() {
		return this.getPatient().getMedicalRecordNumber();
	}
	
	public String getPatientName() {
		return this.getPatient().getName();
	}
	
	public String getAdmissionNumber() {
		return this.getId() == null ? "" : this.getId().toString();
	}
}
