package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SYSTEM_REVIEW_Q_ASSIGNMENT")
public class SystemReviewQuestionAssignment extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "SYSTEM_REVIEW_Q_ASSIGNMENT_ID")
	private Long id;	
	private String description;
	@ManyToOne(optional = true)
	@JoinColumn(name = "SYSTEM_REVIEW_QUESTION_ID")
	private SystemReviewQuestion systemReviewQuestion;
	@ManyToOne()
	@JoinColumn(name = "SUMMARY_TYPE_ID")
	private SummaryType summaryType;
	public SystemReviewQuestionAssignment() {}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SystemReviewQuestion getSystemReviewQuestion() {
		return systemReviewQuestion;
	}

	public void setSystemReviewQuestion(SystemReviewQuestion systemReviewQuestion) {
		this.systemReviewQuestion = systemReviewQuestion;
	}

	public SummaryType getSummaryType() {
		return summaryType;
	}

	public void setSummaryType(SummaryType summaryType) {
		this.summaryType = summaryType;
	}

	// Transient
	public String getRoleName() {
		return this.getSummaryType().getUserGroup().getName();
	}
	
	public String getSummaryTypeName() {
		return this.getSummaryType().getName();
	}
	
	public String getSystemReviewQuestionName() {
		return this.getSystemReviewQuestion().getName();
	}
	
}
