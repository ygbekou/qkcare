package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SYSTEM_REVIEW_RESULT")
public class SystemReviewResult extends BaseEntity {
	
	@Id
	@Column(name = "SYSTEM_REVIEW_RESULT_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "SYSTEM_REVIEW_ID")
	private SystemReview systemReview;
	@ManyToOne
	@JoinColumn(name = "SYSTEM_REVIEW_QUESTION_ID")
	private SystemReviewQuestion systemReviewQuestion;
	
	public SystemReviewResult() {}
	
	public SystemReviewResult(Long reviewSystemId, Long reviewSystemQuestionId) {
		this.systemReview = new SystemReview(reviewSystemId);
		this.systemReviewQuestion = new SystemReviewQuestion(reviewSystemQuestionId);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public SystemReview getSystemReview() {
		return systemReview;
	}
	public void setSystemReview(SystemReview systemReview) {
		this.systemReview = systemReview;
	}
	public SystemReviewQuestion getSystemReviewQuestion() {
		return systemReviewQuestion;
	}
	public void setSystemReviewQuestion(SystemReviewQuestion systemReviewQuestion) {
		this.systemReviewQuestion = systemReviewQuestion;
	}

}
