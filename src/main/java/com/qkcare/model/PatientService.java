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
@Table(name = "PATIENT_SERVICE")
public class PatientService extends BaseEntity {
	
	@Id
	@Column(name = "PATIENT_SERVICE_ID")
	@GeneratedValue
	private Long id;
	@Column(name = "SERVICE_DATE")
	private Date serviceDate;
	@ManyToOne
	@JoinColumn(name = "VISIT_ID")
	private Visit visit;
	@ManyToOne
	@JoinColumn(name = "ADMISSION_ID")
	private Admission admission;
	@ManyToOne
	@JoinColumn(name = "SERVICE_ID")
	private Service service;
	private String notes;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getServiceDate() {
		return serviceDate;
	}
	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}
	public Visit getVisit() {
		return visit;
	}
	public void setVisit(Visit visit) {
		this.visit = visit;
	}
	public Admission getAdmission() {
		return admission;
	}
	public void setAdmission(Admission admission) {
		this.admission = admission;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	
	// Transient data
	public Employee getDoctor() {
		if (this.visit != null) {
			return this.getVisit().getDoctor();
		}
		if (this.admission != null) {
			return this.getAdmission().getDoctorAssignment().getDoctor();
		}
		return null;
	}
	
	public String getServiceName() {
		return this.getService().getName();
	}

}
