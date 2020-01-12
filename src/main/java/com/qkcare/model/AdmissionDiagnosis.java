package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ADMISSION_DIAGNOSIS")
public class AdmissionDiagnosis extends BaseEntity {
	
	@Id
	@Column(name = "ADMISSION_DIAGNOSIS_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "ADMISSION_ID")
	private Admission admission;
	@ManyToOne
	@JoinColumn(name = "VISIT_ID")
	private Visit visit;
	@ManyToOne
	@JoinColumn(name = "DIAGNOSIS_ID")
	private Diagnosis diagnosis;
	private String instructions;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Admission getAdmission() {
		return admission;
	}
	public void setAdmission(Admission admission) {
		this.admission = admission;
	}
	public Visit getVisit() {
		return visit;
	}
	public void setVisit(Visit visit) {
		this.visit = visit;
	}
	public Diagnosis getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(Diagnosis diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	
	// Transient

	public String getDiagnosisName() {
		return this.diagnosis.getName();
	}
	
	public String getMedicalHistoryValue() {
		return this.getDiagnosis().getDescription() + " - " + this.getDiagnosis().getName() + "\n";
	}

}
