package com.qkcare.model;

import java.sql.Timestamp;
import java.util.Set;

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
@Table(name = "PHYSICAL_EXAM")
public class PhysicalExam extends BaseEntity {
	
	@Id
	@Column(name = "PHYSICAL_EXAM_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne(optional = true)
	@JoinColumn(name = "ADMISSION_ID", nullable = true)
	private Admission admission;
	@ManyToOne(optional = true)
	@JoinColumn(name = "VISIT_ID", nullable = true)
	private Visit visit;
	@ManyToOne(optional = true)
	@JoinColumn(name = "SUMMARY_ID", nullable = true)
	private Summary summary;
	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID")
	private Employee author;
	@Column(name = "PHYSICAL_EXAM_DATETIME")
	private Timestamp physicalExamDatetime;
	
	
	// Transient
	@Transient
	private Set<Long> selectedPhysicalExamSystems;

	public PhysicalExam() {
	}

	public PhysicalExam(Long id) {
		this.id = id;
	}

	
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
	public Summary getSummary() {
		return summary;
	}
	public void setSummary(Summary summary) {
		this.summary = summary;
	}
	public Employee getAuthor() {
		return author;
	}
	public void setAuthor(Employee author) {
		this.author = author;
	}
	
	
	public Timestamp getPhysicalExamDatetime() {
		return physicalExamDatetime;
	}
	public void setPhysicalExamDatetime(Timestamp physicalExamDatetime) {
		this.physicalExamDatetime = physicalExamDatetime;
	}
	// Transient attributes
	
	public String getPhysicalExamDate() {
		return DateUtil.formatDate(this.getPhysicalExamDatetime(), DateUtil.DATE_FORMAT);
	}
	
	public Set<Long> getSelectedPhysicalExamSystems() {
		return selectedPhysicalExamSystems;
	}
	public void setSelectedPhysicalExamSystems(Set<Long> selectedPhysicalExamSystems) {
		this.selectedPhysicalExamSystems = selectedPhysicalExamSystems;
	}
	public String getShortMenu() {
		return DateUtil.formatDate(this.getPhysicalExamDatetime(), DateUtil.TIME_WITHOUT_SECONDS_FORMAT) 
				+ " " + this.getAuthor().getName();
	}
}
