package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SERVICE")
public class Service extends BaseEntity {
	
	@Id
	@Column(name = "SERVICE_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "DOCTOR_ORDER_TYPE_ID")
	private DoctorOrderType serviceType;
	@ManyToOne
	@JoinColumn(name = "LAB_TEST_ID")
	private LabTest labTest;
	@ManyToOne
	@JoinColumn(name = "MEDICINE_ID")
	private Product medicine;
	private String name;
	private String description;
	private int quantity;
	private Double rate;
	private int status;
	
	public Service() {}
	
	public Service(LabTest labTest, DoctorOrderType serviceType) {
		this.name = labTest.getName();
		this.description = labTest.getDescription();
		this.labTest = labTest;
		this.quantity = 1;
		this.serviceType = serviceType;
		this.rate = labTest.getPrice();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public DoctorOrderType getServiceType() {
		return serviceType;
	}
	public void setServiceType(DoctorOrderType serviceType) {
		this.serviceType = serviceType;
	}
	public LabTest getLabTest() {
		return labTest;
	}
	public void setLabTest(LabTest labTest) {
		this.labTest = labTest;
	}
	public Product getMedicine() {
		return medicine;
	}
	public void setMedicine(Product medicine) {
		this.medicine = medicine;
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
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	// Transient attributes
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
	
}
