package com.qkcare.model.stocks;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.qkcare.model.BaseEntity;

@Entity
@Table(name = "PATIENT_SALE_STATUS")
public class PatientSaleStatus extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "PATIENT_SALE_STATUS_ID")
	private Long id;	
	private String name;
	
	public PatientSaleStatus() {}
	
	public PatientSaleStatus(Long id) {
		this.id = id;
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
	
}
