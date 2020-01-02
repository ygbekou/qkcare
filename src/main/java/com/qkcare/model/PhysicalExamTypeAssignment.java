package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PHYS_EXAM_TYPE_ASSIGNMENT")
public class PhysicalExamTypeAssignment extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "PHYS_EXAM_TYPE_ASSIGNMENT_ID")
	private Long id;	
	private String description;
	@ManyToOne(optional = true)
	@JoinColumn(name = "CATEGORY_ID")
	private Category physicalExamType;
	@ManyToOne()
	@JoinColumn(name = "SUMMARY_TYPE_ID")
	private SummaryType summaryType;
	public PhysicalExamTypeAssignment() {}

	
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

	public Category getPhysicalExamType() {
		return physicalExamType;
	}

	public void setPhysicalExamType(Category physicalExamType) {
		this.physicalExamType = physicalExamType;
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
	
	public String getPhysicalExamTypeName() {
		return this.getPhysicalExamType().getName();
	}
	
}
