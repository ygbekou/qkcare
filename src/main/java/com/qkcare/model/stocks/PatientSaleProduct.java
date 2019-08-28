package com.qkcare.model.stocks;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.Product;

@Entity
@Table(name = "PATIENT_SALE_PRODUCT")
public class PatientSaleProduct extends BaseEntity {
	
	@Id
	@Column(name = "PATIENT_SALE_PRODUCT_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PATIENT_SALE_ID")
	private PatientSale patientSale;
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
	private Integer quantity;
	@Column(name = "UNIT_PRICE")
	private Double unitPrice;
	@Column(name = "DISCOUNT_PERCENTAGE")
	private Double discountPercentage;
	@Column(name = "DISCOUNT_AMOUNT")
	private Double discountAmount;
	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public PatientSale getPatientSale() {
		return patientSale;
	}
	public void setPatientSale(PatientSale patientSale) {
		this.patientSale = patientSale;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Double getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	// Transient data
	public Date getSaleDatetime() {
		return this.getPatientSale().getSaleDatetime();
	}
	
	public String getProductName() {
		return this.getProduct().getName();
	}
	
	public String getNotes() {
		return this.getPatientSale().getNotes();
	}
}
