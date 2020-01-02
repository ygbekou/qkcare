package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SUMMARY_TYPE_TEMPLATE")
public class SummaryTypeTemplate extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "SUMMARY_TYPE_TEMPLATE_ID")
	private Long id;	
	private String template;
	@ManyToOne()
	@JoinColumn(name = "SUMMARY_TYPE_ID")
	private SummaryType summaryType;
	public SummaryTypeTemplate() {}

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public SummaryType getSummaryType() {
		return summaryType;
	}
	public void setSummaryType(SummaryType summaryType) {
		this.summaryType = summaryType;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}


	// Transient
	public String getRoleName() {
		return this.getSummaryType().getUserGroup().getName();
	}
	
	public String getSummaryTypeName() {
		return this.getSummaryType().getName();
	}
}
