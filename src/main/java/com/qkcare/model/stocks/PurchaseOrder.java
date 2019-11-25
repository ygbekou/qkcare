package com.qkcare.model.stocks;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.Employee;

@Entity
@Table(name = "PURCHASE_ORDER")
public class PurchaseOrder extends BaseEntity {
	
	@Id
	@Column(name = "PURCHASE_ORDER_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "SUPPLIER_ID")
	private Supplier supplier;
	@ManyToOne
	@JoinColumn(name = "REQUESTOR_ID")
	private Employee requestor;
	@ManyToOne
	@JoinColumn(name = "SHIP_TO")
	private Employee shipTo;
	@Column(name = "PURCHASE_ORDER_DATE")
	private Date purchaseOrderDate;
	private String comments;
	@Column(name = "SUB_TOTAL")
	private Double subTotal;
	private Double taxes;
	private Double discount;
	@Column(name = "GRAND_TOTAL")
	private Double grandTotal;
	private Double due;
	@ManyToOne
	@JoinColumn(name = "PURCHASE_ORDER_STATUS_ID")
	private PurchaseOrderStatus purchaseOrderStatus;
	private int status;
	
	// Transient
	@Transient
	private List<PurchaseOrderProduct> purchaseOrderProducts;
	
	public PurchaseOrder() {}
	
	public PurchaseOrder(Long id, Date purchaseOrderDate, Long supplierId, String supplierName) {
		this.id = id;
		this.purchaseOrderDate = purchaseOrderDate;
		this.supplier = new Supplier(supplierId, supplierName);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public Employee getRequestor() {
		return requestor;
	}
	public void setRequestor(Employee requestor) {
		this.requestor = requestor;
	}
	public Employee getShipTo() {
		return shipTo;
	}
	public void setShipTo(Employee shipTo) {
		this.shipTo = shipTo;
	}
	public Date getPurchaseOrderDate() {
		return purchaseOrderDate;
	}
	public void setPurchaseOrderDate(Date purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
	public Double getDue() {
		return due;
	}
	public void setDue(Double due) {
		this.due = due;
	}
	
	public PurchaseOrderStatus getPurchaseOrderStatus() {
		return purchaseOrderStatus;
	}

	public void setPurchaseOrderStatus(PurchaseOrderStatus purchaseOrderStatus) {
		this.purchaseOrderStatus = purchaseOrderStatus;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	public List<PurchaseOrderProduct> getPurchaseOrderProducts() {
		return purchaseOrderProducts;
	}
	public void setPurchaseOrderProducts(List<PurchaseOrderProduct> purchaseOrderProducts) {
		this.purchaseOrderProducts = purchaseOrderProducts;
	}
	
	
	
	public void decreaseAmount(List<PurchaseOrderProduct> pops) {
		for (PurchaseOrderProduct pop : pops) {
			this.setSubTotal(this.getSubTotal() - pop.getTotalAmount());
			this.setGrandTotal(this.getGrandTotal() - pop.getTotalAmount());
			this.setDue(this.getDue() - pop.getTotalAmount());
			
			
		}
		if (this.getPurchaseOrderProducts().isEmpty()) {
			this.setTaxes(0d);
			this.setDiscount(0d);
			this.setGrandTotal(0d);
			this.setDue(0d);
		}
	}
	
	
	// Transients
	public String getSupplierName() {
		return this.getSupplier() != null ? this.getSupplier().getName() : "";
	}
	public String getRequestorName() {
		return this.getRequestor() != null ? this.getRequestor().getName() : ""; 
	}
	public String getReceiverName() {
		return this.getShipTo() != null ? this.getShipTo().getName() : "";
	}
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
	public String getPurchaseOrderStatusDesc() {
		return this.getPurchaseOrderStatus().getName();
	}
	
}
