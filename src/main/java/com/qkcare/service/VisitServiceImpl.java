package com.qkcare.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkcare.domain.GenericVO;
import com.qkcare.model.Allergy;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.MedicalHistory;
import com.qkcare.model.Prescription;
import com.qkcare.model.PrescriptionDiagnosis;
import com.qkcare.model.PrescriptionMedicine;
import com.qkcare.model.SocialHistory;
import com.qkcare.model.Visit;
import com.qkcare.model.VisitAllergy;
import com.qkcare.model.VisitMedicalHistory;
import com.qkcare.model.VisitSocialHistory;
import com.qkcare.model.VisitSymptom;
import com.qkcare.model.VisitVaccine;
import com.qkcare.model.VitalSign;

@Service(value="visitService")
public class VisitServiceImpl  implements VisitService {
	
	@Autowired
	GenericService genericService;
	
	@Transactional
	public BaseEntity save(Visit visit) {
		Visit v = (Visit)this.genericService.save(visit);
		
		VitalSign vitalSign = visit.getVitalSign();
		
		if (vitalSign.getVitalSignDatetime() == null) {
			vitalSign.setVitalSignDatetime(visit.getVisitDatetime());
		}
		vitalSign.setVisit(visit);
		this.genericService.save(vitalSign);
		
		// save allergies 
		List<Long> addedAllergyIds = deriveAddedChilds(visit.getId(), visit.getSelectedAllergies(), "Allergy");
		// Insert newly added ones
		for (Long addedId : addedAllergyIds) {
			VisitAllergy va = new VisitAllergy(visit.getId(), addedId);
			this.genericService.save(va);
		}
		
		// save allergies 
		List<Long> addedSymptomIds = deriveAddedChilds(visit.getId(), visit.getSelectedSymptoms(), "Symptom");
		// Insert newly added ones
		for (Long addedId : addedSymptomIds) {
			VisitSymptom vs = new VisitSymptom(visit.getId(), addedId);
			this.genericService.save(vs);
		}
				
		// save medical histories 
		List<Long> addedMedicalHistoryIds = deriveAddedChilds(visit.getId(), visit.getSelectedMedicalHistories(), "MedicalHistory");
		// Insert newly added ones
		for (Long addedId : addedMedicalHistoryIds) {
			VisitMedicalHistory vm = new VisitMedicalHistory(visit.getId(), addedId);
			this.genericService.save(vm);
		}
		
		// save social histories 
		List<Long> addedSocialHistoryIds = deriveAddedChilds(visit.getId(), visit.getSelectedSocialHistories(), "SocialHistory");
		// Insert newly added ones
		for (Long addedId : addedSocialHistoryIds) {
			VisitSocialHistory vs = new VisitSocialHistory(visit.getId(), addedId);
			this.genericService.save(vs);
		}
		
		// Save Vaccines
		for (VisitVaccine vv : visit.getGivenVaccines()) {
			if (vv.getVaccine() != null && vv.getVaccine().getId() != null) {
				vv.setVisit(visit);
				this.genericService.save(vv);
			}
		}
		
		return v;
	}


	private List<Long> deriveAddedChilds(Long visitId, Set<Long> selectedIds, String childEntity) {
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("VISIT_ID = ", "visitId", visitId + "", "Long"));
		List<Object[]> list = this.genericService.getNativeByCriteria("SELECT " + childEntity.toUpperCase() + "_ID FROM VISIT_" + childEntity.toUpperCase() + " WHERE 1 = 1 ", paramTupleList, null, null);
		Set<Long> existingAllergyIds = new HashSet<Long>();
		
		for (Object object : list) {
			existingAllergyIds.add(new Long(object.toString()));
		}
		
		// Find differences in both list
		List<Long> removedIds = existingAllergyIds.stream().filter(aObject -> {
		     return !selectedIds.contains(aObject);
		 }).collect(Collectors.toList());
		
		List<Long> addedIds = selectedIds.stream().filter(aObject -> {
		     return !existingAllergyIds.contains(aObject);
		 }).collect(Collectors.toList());

		// delete allergies 
		if (removedIds.size() > 0) {
			paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
			paramTupleList.add(Quartet.with("e." + childEntity.toLowerCase() + ".id IN ", childEntity.toLowerCase() + "Id", 
					removedIds.toString().substring(1, removedIds.toString().length() - 1) + "", "List"));
			int deleteds = this.genericService.deleteByCriteria("DELETE FROM Visit" + childEntity + " e WHERE 1 = 1 ", paramTupleList);
		}
		
		return addedIds;
	}
	
	
	public BaseEntity findVisit(Class cl, Long key) {
		Visit visit = (Visit) this.genericService.find(cl, key);
		
		// Get Vital Sign
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.visit.id = ", "visitId", key + "", "Long"));
		String queryStr =  "SELECT e FROM VitalSign e WHERE 1 = 1 ";
		List<BaseEntity> vitalSigns = genericService.getByCriteria(queryStr, paramTupleList, " ");
		
		for (BaseEntity entity : vitalSigns) {
			VitalSign vitalSign = (VitalSign)entity;
			vitalSign.setVisit(null);
			visit.setVitalSign(vitalSign);
			break;
		}
		
		// Get vaccines
		queryStr =  "SELECT e FROM VisitVaccine e WHERE 1 = 1 ";
		List<BaseEntity> vaccines = genericService.getByCriteria(queryStr, paramTupleList, " ");
		List<VisitVaccine> givenVaccines = new ArrayList<VisitVaccine>();
		
		for (BaseEntity entity : vaccines) {
			VisitVaccine givenVaccine = (VisitVaccine)entity;
			givenVaccine.setVisit(null);
			givenVaccines.add(givenVaccine);
		}
		visit.setGivenVaccines(givenVaccines);
		
		// Get allergies
		queryStr =  "SELECT e FROM VisitAllergy e WHERE 1 = 1 ";
		List<BaseEntity> vas = genericService.getByCriteria(queryStr, paramTupleList, " ");
		Set<Long> allergyIds = new HashSet<Long>();
		
		for (BaseEntity entity : vas) {
			VisitAllergy visitAllergy = (VisitAllergy)entity;
			allergyIds.add(visitAllergy.getAllergy().getId());
		}
		visit.setSelectedAllergies(allergyIds);
		
		// Get symptom
		queryStr =  "SELECT e FROM VisitSymptom e WHERE 1 = 1 ";
		List<BaseEntity> vss = genericService.getByCriteria(queryStr, paramTupleList, " ");
		Set<Long> symptomIds = new HashSet<Long>();
		
		for (BaseEntity entity : vss) {
			VisitSymptom visitSymptom = (VisitSymptom)entity;
			symptomIds.add(visitSymptom.getSymptom().getId());
		}
		visit.setSelectedSymptoms(symptomIds);
		
		// Get medical histories
		queryStr =  "SELECT e FROM VisitMedicalHistory e WHERE 1 = 1 ";
		List<BaseEntity> visitMedicalHistories = genericService.getByCriteria(queryStr, paramTupleList, " ");
		Set<Long> medicalHistoryIds = new HashSet<Long>();
		
		for (BaseEntity entity : visitMedicalHistories) {
			VisitMedicalHistory visitMedicalHistory = (VisitMedicalHistory)entity;
			medicalHistoryIds.add(visitMedicalHistory.getMedicalHistory().getId());
		}
		visit.setSelectedMedicalHistories(medicalHistoryIds);
		
		// Get social histories
		queryStr =  "SELECT e FROM VisitSocialHistory e WHERE 1 = 1 ";
		List<BaseEntity> visitSocialHistories = genericService.getByCriteria(queryStr, paramTupleList, " ");
		Set<Long> socialHistoryIds = new HashSet<Long>();
		
		for (BaseEntity entity : visitSocialHistories) {
			VisitSocialHistory visitSocialHistory = (VisitSocialHistory)entity;
			socialHistoryIds.add(visitSocialHistory.getSocialHistory().getId());
		}
		visit.setSelectedSocialHistories(socialHistoryIds);
				
		return visit;
		
	}
	
	public List<BaseEntity> getVisitChilds(String child, Long visitId) {
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.visit.id = ", "visitId", visitId + "", "Long"));
		String queryStr =  "SELECT e FROM " + child + " e WHERE 1 = 1 ";
		List<BaseEntity> childs = genericService.getByCriteria(queryStr, paramTupleList, " ORDER BY e.modDate DESC ");
		
		return childs;
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
	
	public Set<GenericVO> getCategoryAllergies() {
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		String queryStr =  "SELECT e FROM Allergy e WHERE 1 = 1 AND status = 0 ";
		List<BaseEntity> childs = genericService.getByCriteria(queryStr, paramTupleList, " ORDER BY e.name ");
		Map<GenericVO, List<GenericVO>> resultMap = new HashMap<>();
		List<GenericVO> results = new ArrayList<>();
		Map<Long, GenericVO> keyMap = new HashMap<Long, GenericVO>();
		
		for (BaseEntity ch : childs) {
			Allergy allergy = (Allergy)ch;
			if (keyMap.get(allergy.getCategory().getId()) == null) {
				keyMap.put(allergy.getCategory().getId(), 
						new GenericVO(allergy.getCategory().getId(), allergy.getCategory().getName()));
			}
			GenericVO keyVO = keyMap.get(allergy.getCategory().getId());
			if (resultMap.get(keyVO) == null) {
				resultMap.put(keyVO, new ArrayList<>());
			}
			resultMap.get(keyVO).add(new GenericVO(allergy.getId(), allergy.getName()));
		}
		
		for (GenericVO vo : resultMap.keySet()) {
			vo.setChilds(resultMap.get(vo));
		}
		return resultMap.keySet();
	}
	
	public List<GenericVO> getMedicalHistories() {
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		String queryStr =  "SELECT e FROM MedicalHistory e WHERE 1 = 1 AND status = 0 ";
		List<BaseEntity> childs = genericService.getByCriteria(queryStr, paramTupleList, " ORDER BY e.name ");
		List<GenericVO> results = new ArrayList<>();
		
		for (BaseEntity ch : childs) {
			MedicalHistory medicalHistory = (MedicalHistory)ch;
			results.add(new GenericVO(medicalHistory.getId(), medicalHistory.getName()));
		}
		
		return results;
	}
	
	public List<GenericVO> getSocialHistories() {
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		String queryStr =  "SELECT e FROM SocialHistory e WHERE 1 = 1 AND status = 0 ";
		List<BaseEntity> childs = genericService.getByCriteria(queryStr, paramTupleList, " ORDER BY e.name ");
		List<GenericVO> results = new ArrayList<>();
		
		for (BaseEntity ch : childs) {
			SocialHistory socialHistory = (SocialHistory)ch;
			results.add(new GenericVO(socialHistory.getId(), socialHistory.getName()));
		}
		
		return results;
	}
	
	public Map<Integer, List<Visit>> getVisitsByMonth() {
		
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.withDayOfMonth(1).plusMonths(-12);
		LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.visitDatetime >= ", "visitStartDate", startDate.format(formatter), "Date"));
		paramTupleList.add(Quartet.with("e.visitDatetime <= ", "visitEndDate", endDate.format(formatter), "Date"));
		String queryStr =  "SELECT e FROM Visit e WHERE 1 = 1";
		
		List<Visit> visits = (List)this.genericService.getByCriteria(queryStr, 
				paramTupleList, " ORDER BY visitDatetime");
		
		Map<Integer, List<Visit>> dataMap = new HashMap<>();
		
		for (Visit visit : visits) {
			Integer monthIndex = visit.getVisitDatetime().getMonth();
			
			if (dataMap.get(monthIndex) == null) {
				dataMap.put(monthIndex, new ArrayList<Visit>());
			}
			
			dataMap.get(monthIndex).add(visit);
		}
		
		return dataMap;
	}


	@Override
	public List<Visit> getWaitList(int topN) {
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		
		String queryStr =  "SELECT e FROM Visit e WHERE DATE_FORMAT(SYSDATE(), '%Y-%m-01') = DATE_FORMAT(e.visitDatetime, '%Y-%m-01') "
				+ "AND e.status=0 ";
		
		List<Visit> visits = (List)this.genericService.getByCriteria(queryStr, 
				paramTupleList, " ORDER BY e.id",topN);
		return visits;
	}
}
