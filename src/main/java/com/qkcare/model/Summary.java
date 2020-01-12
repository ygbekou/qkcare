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

import com.qkcare.util.DateUtil;


@Entity
@Table(name = "SUMMARY")
public class Summary extends BaseEntity {
	
	@Id
	@Column(name = "SUMMARY_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne(optional = true)
	@JoinColumn(name = "ADMISSION_ID", nullable = true)
	private Admission admission;
	@ManyToOne(optional = true)
	@JoinColumn(name = "VISIT_ID", nullable = true)
	private Visit visit;
	@ManyToOne
	@JoinColumn(name = "SUMMARY_TYPE_ID")
	private SummaryType summaryType;
	@ManyToOne
	@JoinColumn(name = "SUMMARY_STATUS_ID")
	private SummaryStatus summaryStatus;
	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID")
	private Employee author;
	@Column(name = "SUMMARY_DATETIME")
	private Timestamp summaryDatetime;
	private String description;
	private String subject;
	@Column(name = "CHIEF_OF_COMPLAIN")
	private String chiefOfComplain;
	@Column(name = "MEDICAL_HISTORY")
	private String medicalHistory;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public SummaryType getSummaryType() {
		return summaryType;
	}
	public void setSummaryType(SummaryType summaryType) {
		this.summaryType = summaryType;
	}
	public SummaryStatus getSummaryStatus() {
		return summaryStatus;
	}
	public void setSummaryStatus(SummaryStatus summaryStatus) {
		this.summaryStatus = summaryStatus;
	}
	public Employee getAuthor() {
		return author;
	}
	public void setAuthor(Employee author) {
		this.author = author;
	}
	public Timestamp getSummaryDatetime() {
		return summaryDatetime;
	}
	public void setSummaryDatetime(Timestamp summaryDatetime) {
		this.summaryDatetime = summaryDatetime;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	// Transient attributes
	public String getSummaryDate() {
		return DateUtil.formatDate(this.getSummaryDatetime(), DateUtil.DATE_FORMAT);
	}
	
	public String getShortMenu() {
		return DateUtil.formatDate(this.getSummaryDatetime(), DateUtil.TIME_WITHOUT_SECONDS_FORMAT) 
				+ " " + (this.getAuthor() != null ? this.getAuthor().getName() : "") + " - " + this.getSubject();
	}
	
	
	public String getChiefOfComplain() {
		return chiefOfComplain;
	}
	public void setChiefOfComplain(String chiefOfComplain) {
		this.chiefOfComplain = chiefOfComplain;
	}
	public String getMedicalHistory() {
		return medicalHistory;
	}
	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
	}
	
	public String addMedicalHistory(String medicalHistory) {
		this.setMedicalHistory((this.getMedicalHistory() != null ? (this.getMedicalHistory() + "<br/>") : "")  + medicalHistory);
		return this.getMedicalHistory();
	}
	
	
	
}
