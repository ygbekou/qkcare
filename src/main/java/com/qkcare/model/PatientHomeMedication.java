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
@Table(name = "PATIENT_HOME_MEDICATION")
public class PatientHomeMedication extends BaseEntity {
	
	@Id
	@Column(name = "PATIENT_HOME_MEDICATION_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;
	@ManyToOne
	@JoinColumn(name = "MEDICINE_ID")
	private Product medicine;
	private String dosage;
	private Integer quantity;
	private String frequency;
	@Column(name = "START_DATE")
	private Date startDate;
	@Column(name = "END_DATE")
	private Date endDate;
	@Column(name = "NUMBER_OF_DAYS")
	private Integer numberOfDays;
	private String comments;
	private int status;
	
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
	public String getComments() {
		return comments != null ? comments : "";
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Product getMedicine() {
		return medicine;
	}
	public void setMedicine(Product medicine) {
		this.medicine = medicine;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getNumberOfDays() {
		return numberOfDays;
	}
	public void setNumberOfDays(Integer numberOfDays) {
		this.numberOfDays = numberOfDays;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	// Transient
	public String getMedicineType() {
		return this.medicine.getCategoryName();
	}
	
	public String getHomeMedicationDescription() {
		return this.medicine.getName() + "    " + this.getDosage() + " " + this.getQuantity() + " " + this.getFrequency() + " " 
						+ this.getComments() + " " + this.getStatusDesc();
	}
	
	public String getStatusDesc() {
		return this.getStatus() == 0 ? "Active" : "Inactif";
	}
}
