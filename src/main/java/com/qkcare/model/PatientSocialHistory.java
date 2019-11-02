package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PATIENT_SOCIALHISTORY")
public class PatientSocialHistory extends BaseEntity {
	
	@Id
	@Column(name = "PATIENT_SOCIALHISTORY_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PATIENT_ID")
	private Patient patient;
	@ManyToOne
	@JoinColumn(name = "SOCIALHISTORY_ID")
	private SocialHistory socialHistory;
	
	public PatientSocialHistory() {}
	
	public PatientSocialHistory(Long patientId, Long socialHistoryId) {
		this.patient = new Patient(patientId);
		this.socialHistory = new SocialHistory(socialHistoryId);
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
	public SocialHistory getSocialHistory() {
		return socialHistory;
	}

	public void setSocialHistory(SocialHistory socialHistory) {
		this.socialHistory = socialHistory;
	}
}
