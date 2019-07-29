package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ROOM")
public class Room extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "ROOM_ID")
	private Long id;	
	@ManyToOne
	@JoinColumn(name = "FLOOR_ID")
	private Floor floor;
	private String name;
	private String description;
	private int status;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Floor getFloor() {
		return floor;
	}
	public void setFloor(Floor floor) {
		this.floor = floor;
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
	public String getFloorName() {
		return this.getFloor().getName();
	}
	
	public String getBuildingName() {
		return this.getFloor().getBuilding().getName();
	}
	
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
}
