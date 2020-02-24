package com.qkcare.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PATIENT_SURGICAL_HISTORY")
public class PatientSurgicalHistory extends BaseEntity {
	
	@Id
	@Column(name = "PATIENT_SURGICAL_HISTORY_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;
	@ManyToOne
	@JoinColumn(name = "SURGICAL_PROCEDURE_ID")
	private SurgicalProcedure surgicalProcedure;
	@Column(name = "SURGERY_DATE")
	private Date surgeryDate;
	
	public PatientSurgicalHistory() {}
	
	public PatientSurgicalHistory(Long patientId, Long surgicalProcedureId) {
		this.patient = new Patient(patientId);
		this.surgicalProcedure = new SurgicalProcedure(surgicalProcedureId);
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
	public SurgicalProcedure getSurgicalProcedure() {
		return surgicalProcedure;
	}
	public void setSurgicalProcedure(SurgicalProcedure surgicalProcedure) {
		this.surgicalProcedure = surgicalProcedure;
	}
	public Date getSurgeryDate() {
		return surgeryDate;
	}
	public void setSurgeryDate(Date surgeryDate) {
		this.surgeryDate = surgeryDate;
	}

	// Transients
	public String getSurgicalProcedureName() {
		return this.getSurgicalProcedure().getName();
	}
	
}
