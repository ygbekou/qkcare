package com.qkcare.model.stocks;


import java.sql.Timestamp;

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
@Table(name = "PURCHASE_ORDER_PRODUCT")
public class PurchaseOrderProduct extends BaseEntity {
	
	@Id
	@Column(name = "PURCHASE_ORDER_PRODUCT_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PURCHASE_ORDER_ID")
	private PurchaseOrder purchaseOrder;
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
	@Column(name = "QUANTITY")
	private Integer quantity;
	@Column(name = "RECEIVED_QUANTITY")
	private Integer receivedQuantity;
	@Column(name = "RECEIVED_DATETIME")
	private Timestamp receivedDatetime;
	@Column(name = "UNIT_PRICE")
	private Double unitPrice;
	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;
	@Column(name = "DISCOUNT_PERCENTAGE")
	private Double discountPercentage;
	@Column(name = "DISCOUNT_AMOUNT")
	private Double discountAmount;
	private int status;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}
	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
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
	public Integer getReceivedQuantity() {
		return receivedQuantity;
	}
	public void setReceivedQuantity(Integer receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}
	public Timestamp getReceivedDatetime() {
		return receivedDatetime;
	}
	public void setReceivedDatetime(Timestamp receivedDatetime) {
		this.receivedDatetime = receivedDatetime;
	}
	public Double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
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
	
	public PurchaseOrderProduct clone(PurchaseOrderProduct pop) {  
		PurchaseOrderProduct popCopy = new PurchaseOrderProduct();
		popCopy.setProduct(pop.getProduct());
		popCopy.setUnitPrice(pop.getUnitPrice());
		popCopy.setQuantity(pop.getQuantity() - pop.getReceivedQuantity());
		popCopy.setTotalAmount(popCopy.getUnitPrice() * popCopy.getQuantity());
		
		return popCopy;
	}  
	
	public boolean equals(Object a) { 
		if (!(a instanceof PurchaseOrderProduct)) {
			return false;
		}
		
		return this.getId() == ((PurchaseOrderProduct)a).getId();
	}
}
