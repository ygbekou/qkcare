package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PACKAGE_SERVICE")
public class PackageService extends BaseEntity {
	
	@Id
	@Column(name = "PACKAGE_SERVICE_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PACKAGE_ID")
	private Package pckage;
	@ManyToOne
	@JoinColumn(name = "SERVICE_ID")
	private Service service;
	private String description;
	private int quantity;
	private Double rate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Package getPckage() {
		return pckage;
	}
	public void setPckage(Package pckage) {
		this.pckage = pckage;
	}
	public Service getService() {
		return service;
	}
	public void setService(Service service) {
		this.service = service;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
