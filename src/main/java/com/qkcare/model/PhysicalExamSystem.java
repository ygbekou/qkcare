package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PHYSICAL_EXAM_SYSTEM")
public class PhysicalExamSystem extends BaseEntity {
	
	@Id
	@Column(name = "PHYSICAL_EXAM_SYSTEM_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private PhysicalExamSystem parent;
	private String name;
	private String description;
	private int status;
	
	
	public PhysicalExamSystem() {}

	public PhysicalExamSystem(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public PhysicalExamSystem getParent() {
		return parent;
	}
	public void setParent(PhysicalExamSystem parent) {
		this.parent = parent;
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
	
	public String getParentName() {
		return this.parent != null ? this.parent.getName() : "";
	}
	
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
}
