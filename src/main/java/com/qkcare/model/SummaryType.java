package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SUMMARY_TYPE")
public class SummaryType extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "SUMMARY_TYPE_ID")
	private Long id;	
	private String name;
	@ManyToOne(optional = true)
	@JoinColumn(name = "USER_GROUP_ID")
	private UserGroup userGroup;
	public SummaryType() {}
	
	public SummaryType(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}
	
	
	// Transient
	public String getRoleName() {
		return this.userGroup.getName();
	}
	
}
