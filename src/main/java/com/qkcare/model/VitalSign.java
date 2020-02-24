package com.qkcare.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "VITAL_SIGN")
public class VitalSign extends BaseEntity {
	
	@Id
	@Column(name = "VITAL_SIGN_ID")
	@GeneratedValue
	private Long id;
	@OneToOne
	@JoinColumn(name = "VISIT_ID")
	private Visit visit;
	@OneToOne
	@JoinColumn(name = "ADMISSION_ID")
	private Admission admission;
	@Column(name = "VITAL_SIGN_DATETIME")
	private Timestamp vitalSignDatetime;
	private Double temperature;
	private String pulse;
	@Column(name = "RESPIRATORY_RATE")
	private Integer respiratoryRate;
	@Column(name = "HEART_RATE")
	private Integer heartRate;
	@Column(name = "SYSTOLIC_BLOOD_PRESSURE")
	private Integer systolicBloodPressure;
	@Column(name = "DIASTOLIC_BLOOD_PRESSURE")
	private Integer diastolicBloodPressure;
	@Column(name = "MEAN_BLOOD_PRESSURE")
	private Integer meanBloodPressure;
	@Column(name = "BLOOD_SUGAR")
	private Double bloodSugar;
	private String pain;
	private Double weight;
	private Double height;
	private Double bmi;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Timestamp getVitalSignDatetime() {
		return vitalSignDatetime;
	}
	public void setVitalSignDatetime(Timestamp vitalSignDatetime) {
		this.vitalSignDatetime = vitalSignDatetime;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public String getPulse() {
		return pulse;
	}
	public void setPulse(String pulse) {
		this.pulse = pulse;
	}
	public Integer getRespiratoryRate() {
		return respiratoryRate;
	}
	public void setRespiratoryRate(Integer respiratoryRate) {
		this.respiratoryRate = respiratoryRate;
	}
	public Integer getHeartRate() {
		return heartRate;
	}
	public void setHeartRate(Integer heartRate) {
		this.heartRate = heartRate;
	}
	public Integer getSystolicBloodPressure() {
		return systolicBloodPressure;
	}
	public void setSystolicBloodPressure(Integer systolicBloodPressure) {
		this.systolicBloodPressure = systolicBloodPressure;
	}
	public Integer getDiastolicBloodPressure() {
		return diastolicBloodPressure;
	}
	public void setDiastolicBloodPressure(Integer diastolicBloodPressure) {
		this.diastolicBloodPressure = diastolicBloodPressure;
	}
	public Integer getMeanBloodPressure() {
		return meanBloodPressure;
	}
	public void setMeanBloodPressure(Integer meanBloodPressure) {
		this.meanBloodPressure = meanBloodPressure;
	}
	public Double getBloodSugar() {
		return bloodSugar;
	}
	public void setBloodSugar(Double bloodSugar) {
		this.bloodSugar = bloodSugar;
	}
	public String getPain() {
		return pain;
	}
	public void setPain(String pain) {
		this.pain = pain;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getBmi() {
		return bmi;
	}
	public void setBmi(Double bmi) {
		this.bmi = bmi;
	}
	
	// Transient attributes
	public String getPatientMRN() {
			return (this.getVisit() != null && this.getVisit().getPatient() != null) 
					? this.getVisit().getPatient().getMedicalRecordNumber() : 
						(this.getAdmission() != null && this.getAdmission().getPatient() != null) 
							? this.getAdmission().getPatient().getMedicalRecordNumber() : 	null;
	}
	
	public String getPatientName() {
		return (this.getVisit() != null && this.getVisit().getPatient() != null) 
				? this.getVisit().getPatient().getName() : 
					(this.getAdmission() != null && this.getAdmission().getPatient() != null) 
						? this.getAdmission().getPatient().getName() : 	null;
	}

}
