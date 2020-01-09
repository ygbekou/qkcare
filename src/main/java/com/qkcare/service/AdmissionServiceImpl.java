package com.qkcare.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkcare.model.BaseEntity;
import com.qkcare.model.BedAssignment;
import com.qkcare.model.DoctorAssignment;
import com.qkcare.model.Prescription;
import com.qkcare.model.PrescriptionDiagnosis;
import com.qkcare.model.PrescriptionMedicine;
import com.qkcare.model.Admission;

@Service(value="admissionService")
public class AdmissionServiceImpl  implements AdmissionService {
	
	@Autowired
	GenericService genericService;
	
	@Transactional
	public BaseEntity save(Admission admission) {
		
		Admission pa = (Admission)this.genericService.save(admission);
		pa.setPckage(null);
		
		BedAssignment bedAssignment = admission.getBedAssignment();
		bedAssignment.setAdmission(pa);
		bedAssignment.setStartDate(pa.getAdmissionDatetime());
		this.genericService.save(bedAssignment);
		
		DoctorAssignment doctorAssignment = admission.getDoctorAssignment();
		doctorAssignment.setAdmission(pa);
		doctorAssignment.setStartDate(pa.getAdmissionDatetime());
		this.genericService.save(doctorAssignment);
		
		return pa;
	}
	
	
	public BaseEntity findAdmission(Class cl, Long key) {
		Admission admission = (Admission) this.genericService.find(cl, key);
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.admission.id = ", "admissionId", key + "", "Long"));
		String queryStr =  "SELECT e FROM BedAssignment e WHERE 1 = 1 ";
		List<BaseEntity> assignments = genericService.getByCriteria(queryStr, paramTupleList, " ORDER BY e.startDate DESC ");
		List<BedAssignment> bedAssignments = new ArrayList<BedAssignment>();
		
		for (BaseEntity entity : assignments) {
			BedAssignment bedAssignment = (BedAssignment)entity;
			bedAssignment.setAdmission(null);
			admission.setBedAssignment(bedAssignment);
			break;
		}
		
		
		paramTupleList.clear();
		paramTupleList.add(Quartet.with("e.admission.id = ", "admissionId", key + "", "Long"));
		queryStr =  "SELECT e FROM DoctorAssignment e WHERE 1 = 1 ";
		List<BaseEntity> dassignments = genericService.getByCriteria(queryStr, paramTupleList, " ORDER BY e.startDate DESC");
		List<DoctorAssignment> doctorAssignments = new ArrayList<DoctorAssignment>();
		
		for (BaseEntity entity : dassignments) {
			DoctorAssignment doctorAssignment = (DoctorAssignment)entity;
			doctorAssignment.setAdmission(null);
			admission.setDoctorAssignment(doctorAssignment);
			break;
		}
		return admission;
		
	}
	
	public List<BaseEntity> getAdmissionChilds(String child, Long admissionId) {
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.admission.id = ", "admissionId", admissionId + "", "Long"));
		String queryStr =  "SELECT e FROM " + child + " e WHERE 1 = 1 ";
		List<BaseEntity> childs = genericService.getByCriteria(queryStr, paramTupleList, " ORDER BY e.modDate DESC ");
		
		return childs;
	}
	
	@Transactional
	public BaseEntity transferDoctor(DoctorAssignment doctorAssignment) {
		
		DoctorAssignment currentDoctorAssignment = (DoctorAssignment) 
				this.genericService.find(DoctorAssignment.class, doctorAssignment.getId());
		List<String> errors = this.validateDoctorTransfer(currentDoctorAssignment, doctorAssignment);
	
		if (errors.isEmpty()) {
			currentDoctorAssignment.setEndDate(doctorAssignment.getStartDate());
			this.genericService.save(currentDoctorAssignment);
			
			doctorAssignment.setId(null);
			doctorAssignment.setAdmission(currentDoctorAssignment.getAdmission());
			doctorAssignment.setStartDate(doctorAssignment.getTransferDate());
			doctorAssignment.setDoctor(doctorAssignment.getTransferDoctor());
			this.genericService.save(doctorAssignment);
		} else {
			doctorAssignment.setErrors(errors);
		}
		
		return doctorAssignment;
	}
	
	@Transactional
	public BaseEntity transferBed(BedAssignment bedAssignment) {
		
		BedAssignment currentBedAssignment = (BedAssignment) 
				this.genericService.find(BedAssignment.class, bedAssignment.getId());
		List<String> errors = this.validateBedTransfer(currentBedAssignment, bedAssignment);
	
		if (errors.isEmpty()) {
			currentBedAssignment.setEndDate(bedAssignment.getStartDate());
			this.genericService.save(currentBedAssignment);
			
			bedAssignment.setId(null);
			bedAssignment.setAdmission(currentBedAssignment.getAdmission());
			bedAssignment.setStartDate(bedAssignment.getTransferDate());
			bedAssignment.setBed(bedAssignment.getTransferBed());
			this.genericService.save(bedAssignment);
		} else {
			bedAssignment.setErrors(errors);
		}
		
		return bedAssignment;
	}
	
	private List<String> validateDoctorTransfer(DoctorAssignment currentDoctorAssignment, 
			DoctorAssignment newDoctorAssignment) {
		List<String> errors = new ArrayList<String>();
		
		if (currentDoctorAssignment.getDoctor().equals(newDoctorAssignment)) {
			errors.add("ADM_ASS_001");
		}
		
		return errors;
	}
	
	private List<String> validateBedTransfer(BedAssignment currentBedAssignment, 
			BedAssignment newBedAssignment) {
		List<String> errors = new ArrayList<String>();
		
		if (currentBedAssignment.getBed().equals(newBedAssignment)) {
			errors.add("ADM_ASS_002");
		}
		
		return errors;
	}
	
	public BaseEntity findPrescription(Class cl, Long key) {
		Prescription prescription = (Prescription) this.genericService.find(cl, key);
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.prescription.id = ", "prescriptionId", key + "", "Long"));
		String queryStr =  "SELECT e FROM PrescriptionMedicine e WHERE 1 = 1 ";
		List<BaseEntity> medicines = genericService.getByCriteria(queryStr, paramTupleList, " ");
		List<PrescriptionMedicine> prescriptionMedicines = new ArrayList<PrescriptionMedicine>();
		
		for (BaseEntity entity : medicines) {
			PrescriptionMedicine prescriptionMedicine = (PrescriptionMedicine)entity;
			prescriptionMedicine.setPrescription(null);
			prescriptionMedicines.add(prescriptionMedicine);
		}
		
		prescription.setPrescriptionMedicines(prescriptionMedicines);
		
		queryStr =  "SELECT e FROM PrescriptionDiagnosis e WHERE 1 = 1 ";
		List<BaseEntity> diagnoses = genericService.getByCriteria(queryStr, paramTupleList, " ");
		List<PrescriptionDiagnosis> prescriptionDiagnoses = new ArrayList<PrescriptionDiagnosis>();
		
		for (BaseEntity entity : diagnoses) {
			PrescriptionDiagnosis prescriptionDiagnosis = (PrescriptionDiagnosis)entity;
			prescriptionDiagnosis.setPrescription(null);
			prescriptionDiagnoses.add(prescriptionDiagnosis);
		}
		
		prescription.setPrescriptionDiagnoses(prescriptionDiagnoses);
		
		return prescription;
		
	}
	
	public Map<Integer, List<Admission>> getAdmissionsByMonth(Long id) {
		
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.withDayOfMonth(1).plusMonths(-12);
		LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.admissionDatetime >= ", "admissionStartDate", startDate.format(formatter), "Date"));
		paramTupleList.add(Quartet.with("e.admissionDatetime <= ", "admissionEndDate", endDate.format(formatter), "Date"));
		String queryStr =  "SELECT e FROM Admission e WHERE 1 = 1";
		if (id != null && id > 0) {
			queryStr += " AND e.patient.user.id=" + id;
		}
		List<Admission> admissions = (List)this.genericService.getByCriteria(queryStr, 
				paramTupleList, " ORDER BY admissionDatetime");
		
		Map<Integer, List<Admission>> dataMap = new HashMap<>();
		
		for (Admission admission : admissions) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(admission.getAdmissionDatetime());
			Integer monthIndex = calendar.get(Calendar.MONTH);
			
			if (dataMap.get(monthIndex) == null) {
				dataMap.put(monthIndex, new ArrayList<Admission>());
			}
			
			dataMap.get(monthIndex).add(admission);
		}
		
		return dataMap;
	}
	
	public Map<Integer, List<Admission>> getAdmissionsByYear(Long id) { 
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
 		String queryStr =  "SELECT e FROM Admission e WHERE 1 = 1";
		if (id != null && id > 0) {
			queryStr += " AND e.patient.user.id=" + id;
		}
		List<Admission> admissions = (List)this.genericService.getByCriteria(queryStr, 
				paramTupleList, " ORDER BY admissionDatetime");
		
		Map<Integer, List<Admission>> dataMap = new HashMap<>();
		
		for (Admission admission : admissions) { 
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(admission.getAdmissionDatetime());
			Integer monthIndex = calendar.get(Calendar.YEAR); 
			if (dataMap.get(monthIndex) == null) {
				dataMap.put(monthIndex, new ArrayList<Admission>());
			}
			
			dataMap.get(monthIndex).add(admission);
		}
		
		return dataMap;
	}
}
