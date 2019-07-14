package com.qkcare.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.qkcare.model.enums.DoctorOrderStatusEnum;
import com.qkcare.model.enums.DoctorOrderTypeEnum;


@Entity
@Table(name = "DOCTOR_ORDER")
public class DoctorOrder extends BaseEntity {
	
	@Id
	@Column(name = "DOCTOR_ORDER_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne(optional = true)
	@JoinColumn(name = "ADMISSION_ID", nullable = true)
	private Admission admission;
	@ManyToOne(optional = true)
	@JoinColumn(name = "VISIT_ID", nullable = true)
	private Visit visit;
	@Column(name = "DOCTOR_ORDER_TYPE_ID")
	private DoctorOrderTypeEnum doctorOrderTypeEnum;
	@ManyToOne
	@JoinColumn(name = "DOCTOR_ORDER_PRIORITY_ID")
	private DoctorOrderPriority doctorOrderPriority;
	@Column(name = "DOCTOR_ORDER_DATETIME")
	private Timestamp doctorOrderDatetime;
	private String description;
	@ManyToOne
	@JoinColumn(name = "DOCTOR_ORDER_KIND_ID")
	private DoctorOrderKind doctorOrderKind;
	@ManyToOne
	@JoinColumn(name = "DOCTOR_ID")
	private Employee doctor;
	@Column(name = "RECEIVED_DATETIME")
	private Timestamp receivedDatetime;
	@Column(name = "DOCTOR_ORDER_STATUS_ID")
	private DoctorOrderStatusEnum statusEnum;
	
	
	// Transient
	@Transient
	List<LabTest> labTests;
	
	@Transient
	List<Product> products;
	
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
	public DoctorOrderTypeEnum getDoctorOrderTypeEnum() {
		return doctorOrderTypeEnum;
	}
	public void setDoctorOrderTypeEnum(DoctorOrderTypeEnum doctorOrderTypeEnum) {
		this.doctorOrderTypeEnum = doctorOrderTypeEnum;
	}
	public DoctorOrderStatusEnum getStatusEnum() {
		return statusEnum;
	}
	public void setStatusEnum(DoctorOrderStatusEnum statusEnum) {
		this.statusEnum = statusEnum;
	}
	public DoctorOrderPriority getDoctorOrderPriority() {
		return doctorOrderPriority;
	}
	public void setDoctorOrderPriority(DoctorOrderPriority doctorOrderPriority) {
		this.doctorOrderPriority = doctorOrderPriority;
	}
	public Timestamp getDoctorOrderDatetime() {
		return doctorOrderDatetime;
	}
	public void setDoctorOrderDatetime(Timestamp doctorOrderDatetime) {
		this.doctorOrderDatetime = doctorOrderDatetime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public DoctorOrderKind getDoctorOrderKind() {
		return doctorOrderKind;
	}
	public void setDoctorOrderKind(DoctorOrderKind doctorOrderKind) {
		this.doctorOrderKind = doctorOrderKind;
	}
	public Employee getDoctor() {
		return doctor;
	}
	public void setDoctor(Employee doctor) {
		this.doctor = doctor;
	}
	public Timestamp getReceivedDatetime() {
		return receivedDatetime;
	}
	public void setReceivedDatetime(Timestamp receivedDatetime) {
		this.receivedDatetime = receivedDatetime;
	}
	public List<LabTest> getLabTests() {
		return labTests;
	}
	public void setLabTests(List<LabTest> labTests) {
		this.labTests = labTests;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	// Transient attributes
	public String getDoctorOrderTypeName() {
		return this.getDoctorOrderTypeEnum().getDescription();
	}
	
	public String getDoctorOrderStatusName() {
		return this.getStatusEnum().getDescription();
	}
	
	
	// From Model to Enum
	public DoctorOrderType getDoctorOrderType() {
		return new DoctorOrderType(Long.valueOf(doctorOrderTypeEnum.getType()), doctorOrderTypeEnum.getDescription());
	}
	public void setDoctorOrderType(DoctorOrderType doctorOrderType) {
		this.setDoctorOrderTypeEnum(DoctorOrderTypeEnum.valueOf(doctorOrderType.getName()));
	}
	
	public DoctorOrderStatus getStatus() {
		return new DoctorOrderStatus(Long.valueOf(statusEnum.getStatus()), statusEnum.getDescription());
	}
	public void setStatus(DoctorOrderStatus status) {
		this.setStatusEnum(DoctorOrderStatusEnum.valueOf(status.getName()));
	}
}
