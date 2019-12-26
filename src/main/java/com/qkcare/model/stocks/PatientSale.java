package com.qkcare.model.stocks;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qkcare.model.Admission;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.DoctorOrder;
import com.qkcare.model.Product;
import com.qkcare.model.Visit;

@Entity
@Table(name = "PATIENT_SALE")
public class PatientSale extends BaseEntity {
	
	@Id
	@Column(name = "PATIENT_SALE_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "ADMISSION_ID")
	private Admission admission;
	@ManyToOne
	@JoinColumn(name = "VISIT_ID")
	private Visit visit;
	@ManyToOne
	@JoinColumn(name = "DOCTOR_ORDER_ID")
	private DoctorOrder doctorOrder;
	@Column(name = "SALE_DATETIME")
	private Timestamp saleDatetime;
	private String notes;
	@Column(name = "SUB_TOTAL")
	private Double subTotal;
	private Double taxes;
	private Double discount;
	@Column(name = "GRAND_TOTAL")
	private Double grandTotal;
	@ManyToOne
	@JoinColumn(name = "PATIENT_SALE_STATUS_ID")
	private PatientSaleStatus patientSaleStatus;
	private int status;
	
	// Transient
	@Transient
	private List<PatientSaleProduct> patientSaleProducts;
	
	public PatientSale() {
		this.patientSaleProducts = new ArrayList<PatientSaleProduct>();
		this.subTotal = new Double(0);
		this.grandTotal = new Double(0);
		this.discount = new Double(0);
		this.taxes = new Double(0);
	}
	
	public PatientSale(Long id, Timestamp saleDatetime) {
		this.id = id;
		this.saleDatetime = saleDatetime;
	}
	
	public PatientSale (DoctorOrder doctorOrder) {
		this();
		this.setAdmission(doctorOrder.getAdmission());
		this.setVisit(doctorOrder.getVisit());
		this.setNotes(doctorOrder.getDescription());
		this.setSaleDatetime(doctorOrder.getDoctorOrderDatetime());
		this.setDoctorOrder(doctorOrder);
		
		for (Product product : doctorOrder.getProducts()) {
			PatientSaleProduct patientSaleProduct = new PatientSaleProduct();
			patientSaleProduct.setProduct(product);
			patientSaleProduct.setQuantity(1);
			patientSaleProduct.setUnitPrice(product.getPrice());
			patientSaleProduct.setTotalAmount(patientSaleProduct.getQuantity() * patientSaleProduct.getUnitPrice());
			this.addPatientSaleProduct(patientSaleProduct);
		}
		
		this.setPatientSaleStatus(new PatientSaleStatus(2L));
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Admission getAdmission() {
		return admission;
	}
	public void setAdmission(Admission admission) {
		this.admission = admission;
	}
	public Visit getVisit() {
		return visit;
	}
	public void setVisit(Visit visit) {
		this.visit = visit;
	}
	public DoctorOrder getDoctorOrder() {
		return doctorOrder;
	}
	public void setDoctorOrder(DoctorOrder doctorOrder) {
		this.doctorOrder = doctorOrder;
	}
	public Timestamp getSaleDatetime() {
		return saleDatetime;
	}
	public void setSaleDatetime(Timestamp saleDatetime) {
		this.saleDatetime = saleDatetime;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	public Double getTaxes() {
		return taxes;
	}
	public void setTaxes(Double taxes) {
		this.taxes = taxes;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(Double grandTotal) {
		this.grandTotal = grandTotal;
	}
	public PatientSaleStatus getPatientSaleStatus() {
		return patientSaleStatus;
	}
	public void setPatientSaleStatus(PatientSaleStatus patientSaleStatus) {
		this.patientSaleStatus = patientSaleStatus;
	}

	// Transient
	public List<PatientSaleProduct> getPatientSaleProducts() {
		return patientSaleProducts;
	}
	public void setPatientSaleProducts(List<PatientSaleProduct> patientSaleProducts) {
		this.patientSaleProducts = patientSaleProducts;
	}
	
	public void addPatientSaleProduct(PatientSaleProduct p) {
		this.patientSaleProducts.add(p);
		if (p.getUnitPrice() != null) {
			this.subTotal  = this.subTotal + p.getUnitPrice();
			this.grandTotal  = this.grandTotal + p.getUnitPrice();
		}
	}
	
	public void decreaseAmount(List<PatientSaleProduct> psps) {
		for (PatientSaleProduct psp : psps) {
			this.setSubTotal(this.getSubTotal() - psp.getTotalAmount());
			this.setGrandTotal(this.getGrandTotal() - psp.getTotalAmount());
			//this.setDue(this.getDue() - psp.getTotalAmount());
		}
		if (this.getPatientSaleProducts().isEmpty()) {
			this.setTaxes(0d);
			this.setDiscount(0d);
			this.setGrandTotal(0d);
			//this.setDue(0d);
		}
	}
	
	public String getPatientId() {
		return this.getVisit() != null ? this.getVisit().getPatient().getMedicalRecordNumber() : "";
	}
	public String getPatientName() {
		return this.getVisit() != null ? this.getVisit().getPatientName() 
				: (this.getAdmission() != null ? this.getAdmission().getPatientName() : "");
	}
	public String getPatientSaleStatusDesc() {
		return this.getPatientSaleStatus().getName();
	}
}
