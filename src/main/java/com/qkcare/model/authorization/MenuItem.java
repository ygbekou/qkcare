package com.qkcare.model.authorization;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qkcare.model.BaseEntity;

@Entity
@Table(name = "MENU_ITEM")
public class MenuItem extends BaseEntity {

	@Id
	@Column(name = "MENU_ITEM_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private MenuItem parent;
	@ManyToOne
	@JoinColumn(name = "RESOURCE_ID")
	private Resource resource;
	private String language;
	private String label;
	private String icon;
	private String description;
	private int status;
	
	public MenuItem() {}
	
	public MenuItem(Long id, String label) {
		this.id = id;
		this.label = label;
	}
	
	public MenuItem(Long id, Long parentId, String label, String icon, String language, String parentLabel) {
		this.id = id;
		this.parent = new MenuItem(parentId, parentLabel);
		this.label = label;
		this.icon = icon;
		this.language = language;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public MenuItem getParent() {
		return parent;
	}
	public void setParent(MenuItem parent) {
		this.parent = parent;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
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

	public String getParentLabel() {
		return this.getParent() != null ? this.getParent().getLabel() : "";
	}
	
	public String getResourceName() {
		return this.getResource() != null ? this.getResource().getName() : "";
	}
	
	public String getResourceUrlPath() {
		return this.getResource() != null ? this.getResource().getUrlPath() : "";
	}
	
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
}
