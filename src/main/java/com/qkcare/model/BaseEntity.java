package com.qkcare.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient; 

@MappedSuperclass
public class BaseEntity {


	@Column(name = "CREATE_DATE")
	private Date createDate = new Date();
	
	@Column(name = "MOD_DATE")
	private Date modDate = new Date();

	@Column(name = "MOD_BY")
	private Long modifiedBy;
	
	@Transient
	private List<String> errors;
	@Transient
	public String customValidator = "";
	
	public Long getId() {
		return null;
	}

	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	
	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	public String getCustomValidator() {
		return customValidator;
	}

	public void setCustomValidator(String customValidator) {
		this.customValidator = customValidator;
	}

	@Override
	public String toString() {
		return "BaseEntity [createDate=" + createDate + ", modDate=" + modDate
				+ ", modifiedBy=" + modifiedBy + "]";
	}

}
