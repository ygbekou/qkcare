package com.qkcare.model.imaging;

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

import com.qkcare.model.Admission;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.DoctorOrder;
import com.qkcare.model.Employee;
import com.qkcare.model.ExamStatus;
import com.qkcare.model.User;
import com.qkcare.model.Visit;

@Entity
@Table(name = "RAD_INVESTIGATION")
public class RadInvestigation extends BaseEntity {

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
	@JoinColumn(name = "REFERRING_PHYSICIAN_ID")
	private User referringPhysician;
	@ManyToOne
	@JoinColumn(name = "assign_to")
	private Employee assignTo;
	@Column(name = "INVESTIGATION_DATETIME")
	private Timestamp investigationDatetime;
	private String name;
	private String description;
	@ManyToOne
	@JoinColumn(name = "EXAM_STATUS_ID")
	private ExamStatus examStatus;
	@Column(name = "REJECTION_DATETIME")
	private Timestamp rejectionDatetime;
	@Column(name = "COMPLETE_DATETIME")
	private Timestamp completeDatetime;

	@Transient
	private List<RadInvestigationExam> investigationExams;
	@Transient
	private List<RadInvestigationComment> investigationComments;

	public RadInvestigation() {

	}

	public RadInvestigation(DoctorOrder doctorOrder) {
		this.setAdmission(doctorOrder.getAdmission());
		this.setVisit(doctorOrder.getVisit());
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

	public User getReferringPhysician() {
		return referringPhysician;
	}

	public void setReferringPhysician(User referringPhysician) {
		this.referringPhysician = referringPhysician;
	}

	public Employee getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(Employee assignTo) {
		this.assignTo = assignTo;
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

	public ExamStatus getExamStatus() {
		return examStatus;
	}

	public void setExamStatus(ExamStatus examStatus) {
		this.examStatus = examStatus;
	}

	public Timestamp getCompleteDatetime() {
		return completeDatetime;
	}

	public void setCompleteDatetime(Timestamp completeDatetime) {
		this.completeDatetime = completeDatetime;
	}

	public Timestamp getRejectionDatetime() {
		return rejectionDatetime;
	}

	public void setRejectionDatetime(Timestamp rejectionDatetime) {
		this.rejectionDatetime = rejectionDatetime;
	}

	// Transient
	public List<RadInvestigationExam> getInvestigationExams() {
		return investigationExams;
	}

	public void setInvestigationExams(List<RadInvestigationExam> investigationExams) {
		this.investigationExams = investigationExams;
	}

	public List<RadInvestigationComment> getInvestigationComments() {
		return investigationComments;
	}

	public void setInvestigationComments(List<RadInvestigationComment> investigationComments) {
		this.investigationComments = investigationComments;
	}

}
