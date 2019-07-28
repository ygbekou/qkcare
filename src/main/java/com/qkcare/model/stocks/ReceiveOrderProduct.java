package com.qkcare.model.stocks;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.Product;

@Entity
@Table(name = "RECEIVE_ORDER_PRODUCT")
public class ReceiveOrderProduct extends BaseEntity {
	
	@Id
	@Column(name = "RECEIVE_ORDER_PRODUCT_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "RECEIVE_ORDER_ID")
	private ReceiveOrder receiveOrder;
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
	@Column(name = "QUANTITY")
	private Integer quantity;
	private String notes;
	private Integer status;
	
	@Transient
	private int originalQuantity;
	
	public ReceiveOrderProduct() {}
	
	public ReceiveOrderProduct(Long id, ReceiveOrder receiveOrder, Product product, Integer originalQuantity, Integer quantity) {
		this.id = id;
		this.receiveOrder = receiveOrder;
		this.product = product;
		this.originalQuantity = originalQuantity;
		this.quantity = quantity;
		this.status = 0;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ReceiveOrder getReceiveOrder() {
		return receiveOrder;
	}
	public void setReceiveOrder(ReceiveOrder receiveOrder) {
		this.receiveOrder = receiveOrder;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

	
	// Transients
	public int getOriginalQuantity() {
		return originalQuantity;
	}
	public void setOriginalQuantity(int originalQuantity) {
		this.originalQuantity = originalQuantity;
	}
	public String getProductName() {
		return this.product != null ? this.product.getName() : "";
	}
	
	public String getProductDescription() {
		return this.product != null ? this.product.getDescription() : "";
	}
	
}
