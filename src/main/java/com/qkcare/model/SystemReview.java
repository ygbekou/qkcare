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
@Table(name = "SYSTEM_REVIEW")
public class SystemReview extends BaseEntity {
	
	@Id
	@Column(name = "SYSTEM_REVIEW_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne(optional = true)
	@JoinColumn(name = "ADMISSION_ID", nullable = true)
	private Admission admission;
	@ManyToOne(optional = true)
	@JoinColumn(name = "VISIT_ID", nullable = true)
	private Visit visit;
	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID")
	private Employee author;
	@Column(name = "SYSTEM_REVIEW_DATETIME")
	private Timestamp systemReviewDatetime;
	
	
	// Transient
	@Transient
	private Set<Long> selectedSystemReviewQuestions;

	public SystemReview() {
	}

	public SystemReview(Long id) {
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
	public Employee getAuthor() {
		return author;
	}
	public void setAuthor(Employee author) {
		this.author = author;
	}
	
	public Timestamp getSystemReviewDatetime() {
		return systemReviewDatetime;
	}

	public void setSystemReviewDatetime(Timestamp systemReviewDatetime) {
		this.systemReviewDatetime = systemReviewDatetime;
	}

	public Set<Long> getSelectedSystemReviewQuestions() {
		return selectedSystemReviewQuestions;
	}

	public void setSelectedSystemReviewQuestions(Set<Long> selectedSystemReviewQuestions) {
		this.selectedSystemReviewQuestions = selectedSystemReviewQuestions;
	}

	// Transients
	public String getSystemReviewDate() {
		return DateUtil.formatDate(this.getSystemReviewDatetime(), DateUtil.DATE_FORMAT);
	}
	public String getShortMenu() {
		return DateUtil.formatDate(this.getSystemReviewDatetime(), DateUtil.TIME_WITHOUT_SECONDS_FORMAT) 
				+ " " + this.getAuthor().getName();
	}
}
