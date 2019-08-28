package com.qkcare.model.imaging;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.qkcare.model.BaseEntity;


@Entity
@Table(name="LATERALITY")
public class Laterality extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LATERALITY_ID")
	private Long id;
	private String name;
	private String description;
	private int status; 
		
	public Laterality() {}
	
	public Laterality(Long id, String name) {
		this.id = id;
		this.name = name;
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

	// Transient
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
}
