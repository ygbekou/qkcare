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
	
	public SearchCriteria() {}

	public Integer getTopN() {
		return topN;
	}

	public void setTopN(Integer topN) {
		this.topN = topN;
	}

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
	
	public boolean hasDoctorId() {
		return getDoctor() != null && getDoctor().getId()!=null&& getDoctor().getId() > 0;
	}
	
	public boolean hasHospitalLocationId() {
		return getHospitalLocation() != null&& getHospitalLocation().getId()!=null && getHospitalLocation().getId() > 0;
	}
	
	public boolean hasDepartmentId() {
		return getDepartment() != null&& getDepartment().getId()!=null && getDepartment().getId() > 0;
	}
}
