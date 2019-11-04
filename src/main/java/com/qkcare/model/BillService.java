package com.qkcare.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import com.qkcare.model.enums.DoctorOrderTypeEnum;
import com.qkcare.model.stocks.PatientSaleProduct;

import javassist.expr.Instanceof;

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
	@JoinColumn(name = "PATIENT_SERVICE_ID")
	private PatientService patientService;
	@ManyToOne
	@JoinColumn(name = "PATIENT_PACKAGE_ID")
	private PatientPackage patientPackage;
	@ManyToOne
	@JoinColumn(name = "PATIENT_SALE_PRODUCT_ID")
	private PatientSaleProduct patientSaleProduct;
	@ManyToOne
	@JoinColumn(name = "INVESTIGATION_ID")
	private Investigation investigation;
	@ManyToOne
	@JoinColumn(name = "BED_ASSIGNMENT_ID")
	private BedAssignment bedAssignment;
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
	@Column(name = "SYSTEM_GENERATED")
	private String systemGenerated;
	
	
	public BillService() {}
	
	public BillService(PatientService patientService) {
		this.setServiceDate(patientService.getServiceDate());
		this.setDoctorOrderType(new DoctorOrderType(Long.valueOf(DoctorOrderTypeEnum.Medical.getType()), 
				DoctorOrderTypeEnum.Medical.getDescription()));
		this.setPatientService(patientService);
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
		this.setSystemGenerated("Y");
	} 
	
	public BillService(PatientPackage patientPackage) {
		this.setServiceDate(patientPackage.getPackageDate());
		this.setDoctorOrderType(new DoctorOrderType(Long.valueOf(DoctorOrderTypeEnum.Medical.getType()), 
				DoctorOrderTypeEnum.Medical.getDescription()));
		this.setPatientPackage(patientPackage);
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
		this.setSystemGenerated("Y");
	} 
	
	public BillService(PatientSaleProduct patientSaleProduct, Employee doctor) {
		this.setServiceDate(patientSaleProduct.getPatientSale().getSaleDatetime());
		this.setDoctorOrderType(new DoctorOrderType(Long.valueOf(DoctorOrderTypeEnum.Pharmacie.getType()), 
				DoctorOrderTypeEnum.Pharmacie.getDescription()));
		this.setPatientSaleProduct(patientSaleProduct);
		this.setQuantity(patientSaleProduct.getQuantity());
		this.setUnitAmount(patientSaleProduct.getUnitPrice());
		this.setTotalAmount(this.getUnitAmount() * this.getQuantity());
		this.setDiscountAmount(patientSaleProduct.getDiscountAmount());
		this.setDiscountPercentage(patientSaleProduct.getDiscountPercentage());
		this.setNetAmount(this.getTotalAmount() - this.getDiscountAmount());
		this.setDoctor(patientSaleProduct.getPatientSale().getDoctorOrder() != null 
				&& patientSaleProduct.getPatientSale().getDoctorOrder().getDoctor() != null
				? patientSaleProduct.getPatientSale().getDoctorOrder().getDoctor() : doctor);
		this.setDescription(patientSaleProduct.getProduct().getDescription());
		this.setPayerAmount(0d);
		this.setPatientAmount(this.getNetAmount());
		this.setSystemGenerated("Y");
	} 
	
	public BillService(Investigation investigation, Employee doctor) {
		this.setServiceDate(investigation.getInvestigationDatetime());
		this.setDoctorOrderType(new DoctorOrderType(Long.valueOf(DoctorOrderTypeEnum.Laboratoire.getType()), 
				DoctorOrderTypeEnum.Laboratoire.getDescription()));
		this.setInvestigation(investigation);
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
		this.setSystemGenerated("Y");
	} 
	
	public BillService(BedAssignment bedAssignment, Employee doctor) {
		this.setServiceDate(bedAssignment.getStartDate());
		this.setDoctorOrderType(new DoctorOrderType(Long.valueOf(DoctorOrderTypeEnum.Lit.getType()), 
				DoctorOrderTypeEnum.Lit.getDescription()));
		this.setBedAssignment(bedAssignment);
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
		this.setSystemGenerated("Y");
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
	public PatientService getPatientService() {
		return patientService;
	}
	public void setPatientService(PatientService patientService) {
		this.patientService = patientService;
	}
	public PatientPackage getPatientPackage() {
		return patientPackage;
	}
	public void setPatientPackage(PatientPackage patientPackage) {
		this.patientPackage = patientPackage;
	}
	public PatientSaleProduct getPatientSaleProduct() {
		return patientSaleProduct;
	}
	public void setPatientSaleProduct(PatientSaleProduct patientSaleProduct) {
		this.patientSaleProduct = patientSaleProduct;
	}
	public Investigation getInvestigation() {
		return investigation;
	}
	public void setInvestigation(Investigation investigation) {
		this.investigation = investigation;
	}
	public BedAssignment getBedAssignment() {
		return bedAssignment;
	}
	public void setBedAssignment(BedAssignment bedAssignment) {
		this.bedAssignment = bedAssignment;
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
	public String getSystemGenerated() {
		return systemGenerated;
	}
	public void setSystemGenerated(String systemGenerated) {
		this.systemGenerated = systemGenerated;
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
	public String getServiceName() {
		if (this.getService() != null)
			return this.getService().getName();
		
		if (this.getProduct() != null) 
			return this.getProduct().getName();
		
		if (this.getPckage() != null) 
			return this.getPckage().getName();
		
		if (this.getLabTest() != null) 
			return this.getLabTest().getName();
		
		
		if (this.getPatientService() != null)
			return this.getPatientService().getServiceName();
		
		if (this.getPatientSaleProduct() != null) 
			return this.getPatientSaleProduct().getProductName();
		
		if (this.getPatientPackage() != null) 
			return this.getPatientPackage().getPackageName();
		
		if (this.getInvestigation() != null) 
			return this.getInvestigation().getLabTestName();
		
		if (this.getBedAssignment() != null) 
			return this.getBedAssignment().getBedName();
		
		return this.description;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) 
			return false;
		
		if (!(o instanceof BillService)) 
			return false;
		
		if (this.getPatientService() != null 
				&& ((BillService)o).getPatientService() != null  
				&& this.getPatientService().getId().equals((((BillService)o).getPatientService()).getId())) 
			return true;
		
		if (this.getPatientSaleProduct() != null 
				&& ((BillService)o).getPatientSaleProduct() != null  
				&& this.getPatientSaleProduct().getId().equals((((BillService)o).getPatientSaleProduct()).getId())) 
			return true;
		
		if (this.getPatientPackage() != null 
				&& ((BillService)o).getPatientPackage() != null  
				&& this.getPatientPackage().getId().equals((((BillService)o).getPatientPackage()).getId())) 
			return true;
		
		if (this.getInvestigation() != null 
				&& ((BillService)o).getInvestigation() != null  
				&& this.getInvestigation().getId().equals((((BillService)o).getInvestigation()).getId())) 
			return true;
		
		if (this.getBedAssignment() != null 
				&& ((BillService)o).getBedAssignment() != null  
				&& this.getBedAssignment().getId().equals((((BillService)o).getBedAssignment()).getId())) 
			return true;
		
		return false;
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getId().intValue();  
        return result;
    }
}
