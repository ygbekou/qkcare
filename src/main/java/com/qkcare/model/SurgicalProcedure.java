package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SURGICAL_PROCEDURE")
public class SurgicalProcedure extends BaseEntity {
	
	@Id
	@Column(name = "SURGICAL_PROCEDURE_ID")
	@GeneratedValue
	private Long id;
	private String name;
	private String description;
	private int status;
	
	public SurgicalProcedure() {}

	public SurgicalProcedure(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
	
	// TRansient fields for UI
	
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
}
