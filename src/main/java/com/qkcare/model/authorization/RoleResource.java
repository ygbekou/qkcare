package com.qkcare.model.authorization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.User;

@Entity
@Table(name = "ROLE_RESOURCE")
public class RoleResource extends BaseEntity {
	
	@Id
	@Column(name = "ROLE_RESOURCE_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "ROLE_ID")
	private Role role;
	@ManyToOne
	@JoinColumn(name = "RESOURCE_ID")
	private Resource resource;
	private String access;
	private String description;
	

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
