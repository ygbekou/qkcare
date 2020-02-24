package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PATIENT_FAMILY_HISTORY")
public class PatientFamilyHistory extends BaseEntity {
	
	@Id
	@Column(name = "PATIENT_FAMILY_HISTORY_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;
	@ManyToOne
	@JoinColumn(name = "MEDICALHISTORY_ID")
	private MedicalHistory medicalHistory;
	
	public PatientFamilyHistory() {}
	
	public PatientFamilyHistory(Long patientId, Long medicalHistoryId) {
		this.patient = new Patient(patientId);
		this.medicalHistory = new MedicalHistory(medicalHistoryId);
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
	public MedicalHistory getMedicalHistory() {
		return medicalHistory;
	}
	public void setMedicalHistory(MedicalHistory medicalHistory) {
		this.medicalHistory = medicalHistory;
	}
}
