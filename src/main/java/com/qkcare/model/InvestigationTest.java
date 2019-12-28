package com.qkcare.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qkcare.util.DateUtil;

@Entity
@Table(name = "INVESTIGATION_TEST")
public class InvestigationTest extends BaseEntity {
	
	@Id
	@Column(name = "INVESTIGATION_TEST_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "INVESTIGATION_ID")
	private Investigation investigation;
	@ManyToOne
	@JoinColumn(name = "LAB_TEST_ID")
	private LabTest labTest;
	@Column(name = "RESULT_DATETIME")
	private Timestamp resultDatetime;
	@Column(name = "RESULT_COMMENTS")
	private String resultComments;
	@Column(name = "DISPATCH_DATETIME")
	private Timestamp dispatchDatetime;
	@Column(name = "DISPATCH_COMMENTS")
	private String dispatchComments;
	private String result;
	private String interpretation;
	private String impression;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Investigation getInvestigation() {
		return investigation;
	}
	public void setInvestigation(Investigation investigation) {
		this.investigation = investigation;
	}
	public LabTest getLabTest() {
		return labTest;
	}
	public void setLabTest(LabTest labTest) {
		this.labTest = labTest;
	}
	public Timestamp getResultDatetime() {
		return resultDatetime;
	}
	public void setResultDatetime(Timestamp resultDatetime) {
		this.resultDatetime = resultDatetime;
	}
	public String getResultComments() {
		return resultComments;
	}
	public void setResultComments(String resultComments) {
		this.resultComments = resultComments;
	}
	public Timestamp getDispatchDatetime() {
		return dispatchDatetime;
	}
	public void setDispatchDatetime(Timestamp dispatchDatetime) {
		this.dispatchDatetime = dispatchDatetime;
	}
	public String getDispatchComments() {
		return dispatchComments;
	}
	public void setDispatchComments(String dispatchComments) {
		this.dispatchComments = dispatchComments;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getInterpretation() {
		return interpretation;
	}
	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}
	public String getImpression() {
		return impression;
	}
	public void setImpression(String impression) {
		this.impression = impression;
	}
	
	
	// Transient
	public String getName() {
		return this.getLabTest().getName();
	}
	
	public String getNormalRange() {
		return this.getLabTest().getNormalRange();
	}
	
	public String getMethod() {
		return this.getLabTest().getLabTestMethod().getName();
	}
	public String getUnit() {
		return this.getLabTest().getLabTestUnit() == null ? "" : this.getLabTest().getLabTestUnit().getName();
	}
	public String getInvestigationName() {
		return this.getInvestigation().getName();
	}
	public String getInvestigationDate() {
		return DateUtil.formatDate(this.getInvestigation().getInvestigationDatetime(), DateUtil.DATE_TIME_WITHOUT_SECONDS);
	}
}
