package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VITAL_SIGN_CONFIG")
public class VitalSignConfig extends BaseEntity {
	
	@Id
	@Column(name = "VITAL_SIGN_CONFIG_ID")
	@GeneratedValue
	private Long id;
	private String name;
	private String description;
	@Column(name = "CONFIG_TYPE")
	private String configType;
	private int status;
	
	public VitalSignConfig() {}

	public VitalSignConfig(Long id) {
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
	public String getConfigType() {
		return configType;
	}
	public void setConfigType(String configType) {
		this.configType = configType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	// Transient fields for UI
	
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
}
