package com.qkcare.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PACKAGE")
public class Package extends BaseEntity {
	
	@Id
	@Column(name = "PACKAGE_ID")
	@GeneratedValue
	private Long id;
	private String name;
	private String description;
	private Double rate;
	private Double discount;
	private int status;
	
	@Transient
	List<PackageService> packageServices;
	
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
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<PackageService> getPackageServices() {
		return packageServices;
	}
	public void setPackageServices(List<PackageService> packageServices) {
		this.packageServices = packageServices;
	}
	
	
	// Transient
	public String getStatusDesc() {
		return this.getStatus() == 0 ? "Actif" : "Inactif";
	}
	
	
}
