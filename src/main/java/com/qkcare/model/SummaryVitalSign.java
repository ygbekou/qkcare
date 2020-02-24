package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SUMMARY_VITAL_SIGN")
public class SummaryVitalSign extends BaseEntity {
	
	@Id
	@Column(name = "SUMMARY_VITAL_SIGN_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "SUMMARY_ID")
	private Summary summary;
	private String name;
	private String lastResult;
	private String minimum;
	private String maximum;
	
	public SummaryVitalSign() {}

	public SummaryVitalSign(Long id) {
		this.id = id;
	}
	
	public SummaryVitalSign(String name, String lastResult, String minimum, String maximum) {
		this.name = name;
		this.lastResult = lastResult;
		this.minimum = minimum;
		this.maximum = maximum;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Summary getSummary() {
		return summary;
	}
	public void setSummary(Summary summary) {
		this.summary = summary;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastResult() {
		return lastResult;
	}
	public void setLastResult(String lastResult) {
		this.lastResult = lastResult;
	}
	public String getMinimum() {
		return minimum;
	}
	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}
	public String getMaximum() {
		return maximum;
	}
	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}
	
}
