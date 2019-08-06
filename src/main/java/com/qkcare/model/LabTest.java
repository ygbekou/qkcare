package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "LAB_TEST")
public class LabTest extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "LAB_TEST_ID")
	private Long id;	
	@ManyToOne
	@JoinColumn(name = "PARENT_ID", nullable = true)
	private LabTest parent;
	@ManyToOne
	@JoinColumn(name = "LAB_TEST_METHOD_ID", nullable = true)
	private LabTestMethod labTestMethod;
	@ManyToOne
	@JoinColumn(name = "LAB_TEST_UNIT_ID", nullable = true)
	private LabTestUnit labTestUnit;
	private String name;
	private String description;
	@Column(name = "NORMAL_RANGE_MINIMUM", nullable = true)
	private Double normalRangeMinimum;
	@Column(name = "NORMAL_RANGE_MAXIMUM", nullable = true)
	private Double normalRangeMaximum;
	@Column(name = "CRITICAL_LOW", nullable = true)
	private Double criticalLow;
	@Column(name = "CRITICAL_HIGH", nullable = true)
	private Double criticalHigh;
	@Column(name = "PRICE", nullable = false)
	private Double price;
	@Column(name = "PAYER_PRICE", nullable = false)
	private Double payerPrice;
	private int status;
	
	public LabTest() {
		
	}
	
	public LabTest(Long id) {
		this.id = id;
	}

	public LabTest(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LabTest getParent() {
		return parent;
	}
	public void setParent(LabTest parent) {
		this.parent = parent;
	}
	public LabTestMethod getLabTestMethod() {
		return labTestMethod;
	}
	public void setLabTestMethod(LabTestMethod labTestMethod) {
		this.labTestMethod = labTestMethod;
	}
	public LabTestUnit getLabTestUnit() {
		return labTestUnit;
	}
	public void setLabTestUnit(LabTestUnit labTestUnit) {
		this.labTestUnit = labTestUnit;
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
	public Double getNormalRangeMinimum() {
		return normalRangeMinimum;
	}
	public void setNormalRangeMinimum(Double normalRangeMinimum) {
		this.normalRangeMinimum = normalRangeMinimum;
	}
	public Double getNormalRangeMaximum() {
		return normalRangeMaximum;
	}
	public void setNormalRangeMaximum(Double normalRangeMaximum) {
		this.normalRangeMaximum = normalRangeMaximum;
	}
	public Double getCriticalLow() {
		return criticalLow;
	}
	public void setCriticalLow(Double criticalLow) {
		this.criticalLow = criticalLow;
	}
	public Double getCriticalHigh() {
		return criticalHigh;
	}
	public void setCriticalHigh(Double criticalHigh) {
		this.criticalHigh = criticalHigh;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getPayerPrice() {
		return payerPrice;
	}
	public void setPayerPrice(Double payerPrice) {
		this.payerPrice = payerPrice;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	// Transient
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
	
	public String getMethodName() {
		return this.labTestMethod != null ? this.labTestMethod.getName() : "";
	}
	
	public String getGroupName() {
		return this.parent != null ? this.parent.getName() : "";
	}
	
	public String getNormalRange() {
		if (this.getNormalRangeMinimum() != null && this.getNormalRangeMaximum() != null)
			return this.getNormalRangeMinimum()  + " - " + this.getNormalRangeMaximum();
		else if (this.getNormalRangeMinimum() != null)
			return "> " + this.getNormalRangeMinimum();
		else if (this.getNormalRangeMaximum() != null)
			return "< " + this.getNormalRangeMaximum();
		else 
			return "Not Numeric";
	}
}
