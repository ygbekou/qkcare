package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SUMMARY_STATUS")
public class SummaryStatus extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "SUMMARY_STATUS_ID")
	private Long id;	
	private String name;
	
	public SummaryStatus() {}
	
	public SummaryStatus(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
