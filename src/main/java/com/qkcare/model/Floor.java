package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FLOOR")
public class Floor extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "FLOOR_ID")
	private Long id;	
	@ManyToOne
	@JoinColumn(name = "BUILDING_ID")
	private Building building;
	private String name;
	private String description;
	private int status;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Building getBuilding() {
		return building;
	}
	public void setBuilding(Building building) {
		this.building = building;
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
	
	// Transient
	public String getBuildingName() {
		return this.getBuilding().getName();
	}
	
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
}
