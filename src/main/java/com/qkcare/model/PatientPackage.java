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
@Table(name = "PATIENT_PACKAGE")
public class PatientPackage extends BaseEntity {
	
	@Id
	@Column(name = "PATIENT_PACKAGE_ID")
	@GeneratedValue
	private Long id;
	@Column(name = "PACKAGE_DATE")
	private Date packageDate;
	@ManyToOne
	@JoinColumn(name = "VISIT_ID")
	private Visit visit;
	@ManyToOne
	@JoinColumn(name = "ADMISSION_ID")
	private Admission admission;
	@ManyToOne
	@JoinColumn(name = "PACKAGE_ID")
	private Package pckage;
	private String notes;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getPackageDate() {
		return packageDate;
	}
	public void setPackageDate(Date packageDate) {
		this.packageDate = packageDate;
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
	public Package getPckage() {
		return pckage;
	}
	public void setPckage(Package pckage) {
		this.pckage = pckage;
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
}
