package com.qkcare.model.authorization;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qkcare.model.BaseEntity;

@Entity
@Table(name = "ROLE")
public class Role extends BaseEntity {

	@Id
	@Column(name = "ROLE_ID")
	@GeneratedValue
	private Long id;
	private String name;
	private String description;
	private int status;
	@ManyToOne
	@JoinColumn(name = "RESOURCE_ID")
	private Resource homePage;
	
	@Transient
	private String statusDesc;

	// Transient
	@Transient
	List<Permission> permissions;

 

	public Resource getHomePage() {
		return homePage;
	}

	public void setHomePage(Resource homePage) {
		this.homePage = homePage;
	}

	public Role() {
	}

	public Role(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
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
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	public String getStatusDesc() {
		return this.status == 0 ? "Actif" : "Inactif";
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

}
