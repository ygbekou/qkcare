package com.qkcare.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "INVESTIGATION")
public class Investigation extends BaseEntity {
	
	@Id
	@Column(name = "INVESTIGATION_ID")
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
	@ManyToOne
	@JoinColumn(name = "LAB_TEST_ID")
	private LabTest labTest;
	@Column(name = "INVESTIGATION_DATETIME")
	private Timestamp investigationDatetime;
	private String name;
	private String description;
	private int status;
	@Column(name = "COLLECTION_DATETIME")
	private Timestamp collectionDatetime;
	@Column(name = "COLLECTION_COMMENTS")
	private String collectionComments;
	@Column(name = "REJECTION_DATETIME")
	private Timestamp rejectionDatetime;
	@Column(name = "REJECTION_COMMENTS")
	private String rejectionComments;
	@Column(name = "FINALIZATION_DATETIME")
	private Timestamp finalizationDatetime;
	@Column(name = "FINALIZATION_COMMENTS")
	private String finalizationComments;
	
	// Transient
	@Transient
	private List<InvestigationTest> investigationTests;
	
	public Investigation () {
		
	}
	
	public Investigation (DoctorOrder doctorOrder, LabTest labTest) {
		this.setAdmission(doctorOrder.getAdmission());
		this.setVisit(doctorOrder.getVisit());
		this.setName(labTest.getName());
		this.setLabTest(labTest);
		this.setInvestigationDatetime(doctorOrder.getDoctorOrderDatetime());
		this.setDoctorOrder(doctorOrder);
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
	public LabTest getLabTest() {
		return labTest;
	}
	public void setLabTest(LabTest labTest) {
		this.labTest = labTest;
	}
	public Timestamp getInvestigationDatetime() {
		return investigationDatetime;
	}
	public void setInvestigationDatetime(Timestamp investigationDatetime) {
		this.investigationDatetime = investigationDatetime;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Timestamp getCollectionDatetime() {
		return collectionDatetime;
	}
	public void setCollectionDatetime(Timestamp collectionDatetime) {
		this.collectionDatetime = collectionDatetime;
	}
	public String getCollectionComments() {
		return collectionComments;
	}
	public void setCollectionComments(String collectionComments) {
		this.collectionComments = collectionComments;
	}
	public Timestamp getRejectionDatetime() {
		return rejectionDatetime;
	}
	public void setRejectionDatetime(Timestamp rejectionDatetime) {
		this.rejectionDatetime = rejectionDatetime;
	}
	public String getRejectionComments() {
		return rejectionComments;
	}
	public void setRejectionComments(String rejectionComments) {
		this.rejectionComments = rejectionComments;
	}
	public Timestamp getFinalizationDatetime() {
		return finalizationDatetime;
	}
	public void setFinalizationDatetime(Timestamp finalizationDatetime) {
		this.finalizationDatetime = finalizationDatetime;
	}
	public String getFinalizationComments() {
		return finalizationComments;
	}
	public void setFinalizationComments(String finalizationComments) {
		this.finalizationComments = finalizationComments;
	}
	
	// Transient
	public String getLabTestName() {
		return this.getLabTest().getName();
	}
	public List<InvestigationTest> getInvestigationTests() {
		return investigationTests;
	}
	public void setInvestigationTests(List<InvestigationTest> investigationTests) {
		this.investigationTests = investigationTests;
	}
	
	
	
}
