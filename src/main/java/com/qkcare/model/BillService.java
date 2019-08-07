package com.qkcare.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qkcare.model.enums.DoctorOrderTypeEnum;
import com.qkcare.model.stocks.PatientSaleProduct;

@Entity
@Table(name = "BILL_SERVICE")
public class BillService extends BaseEntity {
	
	@Id
	@Column(name = "BILL_SERVICE_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "BILL_ID")
	private Bill bill;
	@Column(name = "DOCTOR_ORDER_TYPE_ID")
	private DoctorOrderTypeEnum doctorOrderTypeEnum;
	@ManyToOne
	@JoinColumn(name = "SERVICE_ID")
	private Service service;
	@ManyToOne
	@JoinColumn(name = "PACKAGE_ID")
	private Package pckage;
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
	@ManyToOne
	@JoinColumn(name = "LAB_TEST_ID")
	private LabTest labTest;
	@ManyToOne
	@JoinColumn(name = "BED_ID")
	private Bed bed;
	@ManyToOne
	@JoinColumn(name = "DOCTOR_ID")
	private Employee doctor;
	@Column(name = "SERVICE_DATE")
	private Date serviceDate;
	private String description;
	private int quantity;
	@Column(name = "UNIT_AMOUNT")
	private Double unitAmount;
	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;
	@Column(name = "DISCOUNT_PERCENTAGE")
	private Double discountPercentage;
	@Column(name = "DISCOUNT_AMOUNT")
	private Double discountAmount;
	@Column(name = "NET_AMOUNT")
	private Double netAmount;
	@Column(name = "PAYER_AMOUNT")
	private Double payerAmount;
	@Column(name = "PATIENT_AMOUNT")
	private Double patientAmount;
	
	public BillService() {}
	
	public BillService(PatientService patientService) {
		this.setServiceDate(patientService.getServiceDate());
		this.setDoctorOrderType(new DoctorOrderType(Long.valueOf(DoctorOrderTypeEnum.MEDICAL.getType()), 
				DoctorOrderTypeEnum.MEDICAL.getDescription()));
		this.setService(patientService.getService());
		this.setQuantity(1);
		this.setUnitAmount(patientService.getService().getRate());
		this.setTotalAmount(patientService.getService().getRate());
		this.setDiscountAmount(0d);
		this.setDiscountPercentage(0d);
		this.setNetAmount(patientService.getService().getRate());
		this.setDoctor(patientService.getDoctor());
		this.setDescription(patientService.getService().getDescription());
		this.setPayerAmount(0d);
		this.setPatientAmount(this.getNetAmount());
	} 
	
	public BillService(PatientPackage patientPackage) {
		this.setServiceDate(patientPackage.getPackageDate());
		this.setDoctorOrderType(new DoctorOrderType(Long.valueOf(DoctorOrderTypeEnum.MEDICAL.getType()), 
				DoctorOrderTypeEnum.MEDICAL.getDescription()));
		this.setPckage(patientPackage.getPckage());
		this.setQuantity(1);
		this.setUnitAmount(patientPackage.getPckage().getRate());
		this.setTotalAmount(patientPackage.getPckage().getRate());
		this.setDiscountAmount(patientPackage.getPckage().getDiscount());
		this.setDiscountPercentage((this.getDiscountAmount() * 100) / this.getTotalAmount());
		this.setNetAmount(patientPackage.getPckage().getRate());
		this.setDoctor(patientPackage.getDoctor());
		this.setDescription(patientPackage.getPckage().getDescription());
		this.setPayerAmount(0d);
		this.setPatientAmount(this.getNetAmount());
	} 
	
	public BillService(PatientSaleProduct patientSaleProduct, Employee doctor) {
		this.setServiceDate(patientSaleProduct.getPatientSale().getSaleDatetime());
		this.setDoctorOrderType(new DoctorOrderType(Long.valueOf(DoctorOrderTypeEnum.PHARMACY.getType()), 
				DoctorOrderTypeEnum.PHARMACY.getDescription()));
		this.setProduct(patientSaleProduct.getProduct());
		this.setQuantity(patientSaleProduct.getQuantity());
		this.setUnitAmount(patientSaleProduct.getUnitPrice());
		this.setTotalAmount(this.getUnitAmount() * this.getQuantity());
		this.setDiscountAmount(patientSaleProduct.getDiscountAmount());
		this.setDiscountPercentage(patientSaleProduct.getDiscountPercentage());
		this.setNetAmount(this.getTotalAmount() - this.getDiscountAmount());
		this.setDoctor(patientSaleProduct.getPatientSale().getDoctorOrder() != null 
				? patientSaleProduct.getPatientSale().getDoctorOrder().getDoctor() : doctor);
		this.setDescription(patientSaleProduct.getProduct().getDescription());
		this.setPayerAmount(0d);
		this.setPatientAmount(this.getNetAmount());
	} 
	
	public BillService(Investigation investigation, Employee doctor) {
		this.setServiceDate(investigation.getInvestigationDatetime());
		this.setDoctorOrderType(new DoctorOrderType(Long.valueOf(DoctorOrderTypeEnum.LABORATORY.getType()), 
				DoctorOrderTypeEnum.LABORATORY.getDescription()));
		this.setLabTest(investigation.getLabTest());
		this.setQuantity(1);
		this.setUnitAmount(investigation.getLabTest().getPrice());
		this.setTotalAmount(this.getUnitAmount() * this.getQuantity());
		this.setDiscountAmount(0d);
		this.setDiscountPercentage(0d);
		this.setNetAmount(this.getTotalAmount() - this.getDiscountAmount());
		this.setDoctor(investigation.getDoctorOrder() != null && investigation.getDoctorOrder().getDoctor() != null
				? investigation.getDoctorOrder().getDoctor() : doctor);
		this.setDescription(investigation.getLabTest().getDescription());
		this.setPayerAmount(0d);
		this.setPatientAmount(this.getNetAmount());
	} 
	
	public BillService(BedAssignment bedAssignment, Employee doctor) {
		this.setServiceDate(bedAssignment.getStartDate());
		this.setDoctorOrderType(new DoctorOrderType(Long.valueOf(DoctorOrderTypeEnum.BED.getType()), 
				DoctorOrderTypeEnum.BED.getDescription()));
		this.setBed(bedAssignment.getBed());
		Date endDate  = bedAssignment.getEndDate() == null ? new Date() : bedAssignment.getEndDate();
		int days = Long.valueOf(Math.round((endDate.getTime() - bedAssignment.getStartDate().getTime()) / (double) 86400000)).intValue();
		this.setQuantity(days > 0 ? days : 1);
		this.setUnitAmount(bedAssignment.getBed().getRate());
		this.setTotalAmount(this.getUnitAmount() * this.getQuantity());
		this.setDiscountAmount(0d);
		this.setDiscountPercentage(0d);
		this.setNetAmount(this.getTotalAmount() - this.getDiscountAmount());
		this.setDoctor(doctor);
		this.setDescription(bedAssignment.getBed().getDescription());
		this.setPayerAmount(0d);
		this.setPatientAmount(this.getNetAmount());
	} 
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Bill getBill() {
		return bill;
	}
	public void setBill(Bill bill) {
		this.bill = bill;
	}
	public DoctorOrderTypeEnum getDoctorOrderTypeEnum() {
		return doctorOrderTypeEnum;
	}
	public void setDoctorOrderTypeEnum(DoctorOrderTypeEnum doctorOrderTypeEnum) {
		this.doctorOrderTypeEnum = doctorOrderTypeEnum;
	}
	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Package getPckage() {
		return pckage;
	}
	public void setPckage(Package pckage) {
		this.pckage = pckage;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public LabTest getLabTest() {
		return labTest;
	}
	public void setLabTest(LabTest labTest) {
		this.labTest = labTest;
	}
	public Bed getBed() {
		return bed;
	}
	public void setBed(Bed bed) {
		this.bed = bed;
	}
	public Employee getDoctor() {
		return doctor;
	}
	public void setDoctor(Employee doctor) {
		this.doctor = doctor;
	}
	public Date getServiceDate() {
		return serviceDate;
	}
	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
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
	public Double getUnitAmount() {
		return unitAmount;
	}
	public void setUnitAmount(Double unitAmount) {
		this.unitAmount = unitAmount;
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
		this.discountPercentage = discountPercentage != null ? discountPercentage :  0;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount != null ? discountAmount : 0;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Double getPayerAmount() {
		return payerAmount;
	}
	public void setPayerAmount(Double payerAmount) {
		this.payerAmount = payerAmount != null ? payerAmount : 0d;
	}
	public Double getPatientAmount() {
		return patientAmount;
	}
	public void setPatientAmount(Double patientAmount) {
		this.patientAmount = patientAmount;
	}
	public void setDefaultService(DoctorOrder doctorOrder) {
		this.setDoctor(doctorOrder.getDoctor());
	}
	
	
	// Transient
	public DoctorOrderType getDoctorOrderType() {
		return new DoctorOrderType(Long.valueOf(doctorOrderTypeEnum.getType()), doctorOrderTypeEnum.getDescription());
	}
	public void setDoctorOrderType(DoctorOrderType doctorOrderType) {
		this.setDoctorOrderTypeEnum(DoctorOrderTypeEnum.valueOf(doctorOrderType.getName()));
	}
	
	public String getDoctorOrderTypeName() {
		return this.getDoctorOrderType().getName();
	}
}
