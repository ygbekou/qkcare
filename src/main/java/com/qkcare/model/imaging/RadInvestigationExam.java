package com.qkcare.model.imaging;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qkcare.model.BaseEntity;

@Entity
@Table(name = "RAD_INVESTIGATION_EXAM")
public class RadInvestigationExam extends BaseEntity {
	
	@Id
	@Column(name = "INVESTIGATION_EXAM_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "INVESTIGATION_ID")
	private RadInvestigation investigation;
	@ManyToOne
	@JoinColumn(name = "EXAM_ID")
	private RadExam exam;
	private String comments;
	private int status;
	
	// Transient
	
	public RadInvestigationExam () {
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public RadInvestigation getInvestigation() {
		return investigation;
	}
	public void setInvestigation(RadInvestigation investigation) {
		this.investigation = investigation;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public RadExam getExam() {
		return exam;
	}
	public void setExam(RadExam exam) {
		this.exam = exam;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	// Transient
	public String getExamName() {
		return this.getExam().getName();
	}
}
