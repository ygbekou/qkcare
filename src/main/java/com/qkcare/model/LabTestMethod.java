package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LAB_TEST_METHOD")
public class LabTestMethod extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "LAB_TEST_METHOD_ID")
	private Long id;	
	private String name;
	private String description;
	private int status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
}
