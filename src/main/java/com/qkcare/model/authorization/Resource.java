package com.qkcare.model.authorization;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.qkcare.model.BaseEntity;

@Entity
@Table(name = "RESOURCE")
public class Resource extends BaseEntity {

	@Id
	@Column(name = "RESOURCE_ID")
	@GeneratedValue
	private Long id;
	private String name;
	@Column(name = "URL_PATH")
	private String urlPath;
	private String description;
	private int status;
	
	public Resource() {}
	
	public Resource(Long id, String name, String urlPath) {
		this.id = id;
		this.name = name;
		this.urlPath = urlPath;
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
	public String getUrlPath() {
		return urlPath;
	}
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
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
	
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
}
