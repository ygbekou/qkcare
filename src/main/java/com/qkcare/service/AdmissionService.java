package com.qkcare.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.BedAssignment;
import com.qkcare.model.DoctorAssignment;
import com.qkcare.model.Admission;


@Service(value="admissionService")
public interface AdmissionService {
	
	public BaseEntity save(Admission admission);
	
	public BaseEntity findAdmission(Class cl, Long key);
	
	public List<BaseEntity> getAdmissionChilds(String child, Long admissionId);
	
	public BaseEntity transferDoctor(DoctorAssignment doctorAssignment);
	
	public BaseEntity transferBed(BedAssignment bedAssignment);
	
	public BaseEntity findPrescription(Class cl, Long key);
	
	public Map<Integer, List<Admission>> getAdmissionsByMonth(Long id);
}
