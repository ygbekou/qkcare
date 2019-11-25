package com.qkcare.model.authorization;

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
@Table(name = "PERMISSION")
public class Permission extends BaseEntity {
	
	@Id
	@Column(name = "PERMISSION_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "ROLE_ID")
	private Role role;
	@ManyToOne
	@JoinColumn(name = "RESOURCE_ID")
	private Resource resource;
	@Column(name = "CAN_ADD")
	private String canAdd="N";
	@Column(name = "CAN_VIEW")
	private String canView="N";
	@Column(name = "CAN_EDIT")
	private String canEdit="N";
	@Column(name = "CAN_DELETE")
	private String canDelete="N";
	private String description;
	
	// Transient
	@Transient
	private Boolean canAddBool=false;
	@Transient
	private Boolean canEditBool=false;
	@Transient
	private Boolean canViewBool=false;
	@Transient
	private Boolean canDeleteBool=false;
	
	public Permission () {
	}
	
	public Permission (Long id, Role role, Resource resource, String canAdd, 
			String canView, String canEdit, String canDelete) {
		this.id = id;
		this.setRole(role);
		this.setResource(resource);
		this.canAdd = canAdd;
		this.canView = canView;
		this.canEdit = canEdit;
		this.canDelete = canDelete;
	}
	
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
	public String getCanAdd() {
		return getCanAddBool() ? "Y" : "N";
	}
	public void setCanAdd(String canAdd) {
		this.canAdd = canAdd;
	}
	public String getCanView() {
		return canView;
	}
	public void setCanView(String canView) {
		this.canView = canView;
	}
	public String getCanEdit() {
		return canEdit;
	}
	public void setCanEdit(String canEdit) {
		this.canEdit = canEdit;
	}
	public String getCanDelete() {
		return canDelete;
	}
	public void setCanDelete(String canDelete) {
		this.canDelete = canDelete;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	// Transient
	
	public String getResourceName() {
		return this.getResource().getName();
	}
	
	public String getResourceUrlPath() {
		return this.getResource().getUrlPath();
	}

	public Boolean getCanAddBool() {
		return "Y".equals(canAdd);
	}
	public void setCanAddBool(Boolean canAddBool) {
		this.canAddBool = canAddBool;
		if (canAddBool) {
			this.setCanAdd("Y");
		} else {
			this.setCanAdd("N");
		}
	}
	public Boolean getCanEditBool() {
		return "Y".equals(canEdit);
	}
	public void setCanEditBool(Boolean canEditBool) {
		this.canEditBool = canEditBool;
		if (canEditBool) {
			this.setCanEdit("Y");
		} else {
			this.setCanEdit("N");
		}
	}
	public Boolean getCanViewBool() {
		return "Y".equals(canView);
	}
	public void setCanViewBool(Boolean canViewBool) {
		this.canViewBool = canViewBool;
		if (canViewBool) {
			this.setCanView("Y");
		} else {
			this.setCanView("N");
		}
	}
	public Boolean getCanDeleteBool() {
		return "Y".equals(canDelete);
	}
	public void setCanDeleteBool(Boolean canDeleteBool) {
		this.canDeleteBool = canDeleteBool;
		if (canDeleteBool) {
			this.setCanDelete("Y");
		} else {
			this.setCanDelete("N");
		}
	}
	
}
