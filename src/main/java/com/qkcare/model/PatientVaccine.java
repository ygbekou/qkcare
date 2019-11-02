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
@Table(name = "PATIENT_VACCINE")
public class PatientVaccine extends BaseEntity {
	
	@Id
	@Column(name = "PATIENT_VACCINE_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;
	@ManyToOne
	@JoinColumn(name = "VACCINE_ID")
	private Vaccine vaccine;
	@Column(name = "GIVEN_DATE")
	private Date givenDate;
	
	public PatientVaccine() {}
	
	public PatientVaccine(Long patientId, Long vaccineId) {
		this.patient = new Patient(patientId);
		this.vaccine = new Vaccine(vaccineId);
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
	public Vaccine getVaccine() {
		return vaccine;
	}
	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}
	public Date getGivenDate() {
		return givenDate;
	}
	public void setGivenDate(Date givenDate) {
		this.givenDate = givenDate;
	}
	
	// Transients
	public String getVaccineName() {
		return this.getVaccine().getName();
	}
	
}
