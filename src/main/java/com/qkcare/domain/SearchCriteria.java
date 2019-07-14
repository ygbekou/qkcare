package com.qkcare.domain;

import java.io.Serializable;

import com.qkcare.model.Department;
import com.qkcare.model.Employee;
import com.qkcare.model.HospitalLocation;

public class SearchCriteria implements Serializable {
	
	Long id;
	String lastName;
	String firstName;
	String birthDate;
	Department department;
	HospitalLocation hospitalLocation;
	Employee doctor;
	Integer topN;
	String medicalRecordNumber;
	Long visitId;
	Long admissionId;
	String visitDate;
	String admissionDate;
	String investigationDateStart;
	String investigationDateEnd;
	
	public SearchCriteria() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public HospitalLocation getHospitalLocation() {
		return hospitalLocation;
	}

	public void setHospitalLocation(HospitalLocation hospitalLocation) {
		this.hospitalLocation = hospitalLocation;
	}

	public Employee getDoctor() {
		return doctor;
	}

	public void setDoctor(Employee doctor) {
		this.doctor = doctor;
	}

	public Integer getTopN() {
		return topN;
	}

	public void setTopN(Integer topN) {
		this.topN = topN;
	}

	public String getMedicalRecordNumber() {
		return medicalRecordNumber;
	}

	public void setMedicalRecordNumber(String medicalRecordNumber) {
		this.medicalRecordNumber = medicalRecordNumber;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Long getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Long admissionId) {
		this.admissionId = admissionId;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public String getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}

	public String getInvestigationDateStart() {
		return investigationDateStart;
	}

	public void setInvestigationDateStart(String investigationDateStart) {
		this.investigationDateStart = investigationDateStart;
	}

	public String getInvestigationDateEnd() {
		return investigationDateEnd;
	}

	public void setInvestigationDateEnd(String investigationDateEnd) {
		this.investigationDateEnd = investigationDateEnd;
	}

	public boolean hasDoctorId() {
		return getDoctor() != null && getDoctor().getId() != null && getDoctor().getId() > 0;
	}
	
	public boolean hasHospitalLocationId() {
		return getHospitalLocation() != null && getHospitalLocation().getId() != null && getHospitalLocation().getId() > 0;
	}
	
	public boolean hasDepartmentId() {
		return getDepartment() != null && getDepartment().getId() != null && getDepartment().getId() > 0;
	}
}
