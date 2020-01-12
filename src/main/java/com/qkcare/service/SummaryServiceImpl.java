package com.qkcare.service;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkcare.domain.GenericVO;
import com.qkcare.model.Admission;
import com.qkcare.model.AdmissionDiagnosis;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.PhysicalExam;
import com.qkcare.model.PhysicalExamResult;
import com.qkcare.model.Summary;
import com.qkcare.model.SystemReview;
import com.qkcare.model.SystemReviewResult;
import com.qkcare.model.Visit;
import com.qkcare.model.VisitSymptom;
import com.qkcare.model.VitalSign;
import com.qkcare.util.Utils;

@Service(value="summaryService")
public class SummaryServiceImpl  implements SummaryService {
	
	@Autowired
	GenericService genericService;
	
	@Transactional
	public BaseEntity save(Summary summary) {
		Summary su = (Summary)this.genericService.save(summary);	
		
		Admission admission = (Admission) this.genericService.find(Summary.class, summary.getAdmission().getId());
		
		if (summary.getVisit() != null) {
			Visit visit = (Visit) this.genericService.find(Summary.class, summary.getVisit().getId());
			visit.setChiefOfComplain(summary.getChiefOfComplain());
		}
		
		
				
		
		return su;
	}
	
	
	public BaseEntity findSummaryByPresence(String label, Long labelId) {
		Summary summary = new Summary();
		summary.setSummaryDatetime(Timestamp.valueOf(LocalDateTime.now()));
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		
		paramTupleList.clear();
		if ("Admission".equals(label)) {
			paramTupleList.add(Quartet.with("e.admission.id = ", "admissionId", labelId + "", "Long"));
		} else if ("Visit".equals(label)){
			paramTupleList.add(Quartet.with("e.visit.id = ", "visitId", summary.getVisit().getId() + "", "Long"));
		}
		
		// Get diagnosis
		
		String queryStr =  "SELECT e FROM AdmissionDiagnosis e WHERE 1 = 1 ";
		
		List<BaseEntity> vss = genericService.getByCriteria(queryStr, paramTupleList, " ");
		
		for (BaseEntity entity : vss) {
			AdmissionDiagnosis ad = (AdmissionDiagnosis)entity;
			summary.addMedicalHistory(ad.getDiagnosisName());
		}
		
		return summary;
		
	}
	
	public BaseEntity findSummary(Class cl, Long key) {
		Summary summary = (Summary) this.genericService.find(cl, key);
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.summary.id = ", "summaryId", key + "", "Long"));
		
		if (key == 0) {
			summary = new Summary();
		
			paramTupleList.clear();
			if (summary.getAdmission() != null) {
				paramTupleList.add(Quartet.with("e.admission.id = ", "admissionId", summary.getAdmission().getId() + "", "Long"));
			} else if (summary.getVisit() != null){
				paramTupleList.add(Quartet.with("e.visit.id = ", "visitId", summary.getVisit().getId() + "", "Long"));
			}
			
			// Get physical exam systems
			
			String queryStr =  "SELECT e FROM AdmissionDiagnosis e WHERE 1 = 1 ";
			
			List<BaseEntity> vss = genericService.getByCriteria(queryStr, paramTupleList, " ");
			
			for (BaseEntity entity : vss) {
				AdmissionDiagnosis ad = (AdmissionDiagnosis)entity;
				summary.setMedicalHistory(ad.getMedicalHistoryValue() + "\n");
			}
		}
		
		return summary;
		
	}
	
	
	@Transactional
	public BaseEntity save(PhysicalExam physicalExam) {
		PhysicalExam pe = (PhysicalExam)this.genericService.save(physicalExam);		
		
		// save physicalExamSystems 
		List<Long> addedPhysicalExamSystemIds = this.genericService.deriveAddedChilds("PHYSICAL_EXAM", "PhysicalExam", "PHYSICAL_EXAM_ID", 
				physicalExam.getId(), physicalExam.getSelectedPhysicalExamSystems(), "PhysicalExamSystem", 
				"PHYSICAL_EXAM_SYSTEM", "PhysicalExamResult", "PHYSICAL_EXAM_RESULT");
		// Insert newly added ones
		for (Long addedId : addedPhysicalExamSystemIds) {
			PhysicalExamResult per = new PhysicalExamResult(physicalExam.getId(), addedId);
			this.genericService.save(per);
		}
				
		
		return pe;
	}
	
	public Map<String, Map<String, List<GenericVO>>> getPhysicalExamSystemsBySummaryType(Long summaryTypeId) {
		
		// Get the user resources 
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("P.SUMMARY_TYPE_ID = ", "summaryTypeId", summaryTypeId + "", "Long"));
		String queryStr =  "SELECT L1_ID, L1_NAME, L2_ID, L2_NAME, L3_ID, L3_NAME, P.SUMMARY_TYPE_ID\r\n" + 
				"FROM (SELECT L1_ID, L1_NAME, L2_ID, L2_NAME, \r\n" + 
				"    L3.PHYSICAL_EXAM_SYSTEM_ID L3_ID, L3.NAME L3_NAME\r\n" + 
				"    FROM (SELECT L1.PHYSICAL_EXAM_SYSTEM_ID L1_ID, L1.NAME L1_NAME, \r\n" + 
				"			L2.PHYSICAL_EXAM_SYSTEM_ID L2_ID, L2.NAME L2_NAME\r\n" + 
				"		FROM (SELECT PHYSICAL_EXAM_SYSTEM_ID, NAME\r\n" + 
				"			FROM PHYSICAL_EXAM_SYSTEM \r\n" + 
				"			WHERE PARENT_ID IS NULL) L1\r\n" + 
				"		LEFT OUTER JOIN PHYSICAL_EXAM_SYSTEM L2 ON L1.PHYSICAL_EXAM_SYSTEM_ID = L2.PARENT_ID) L2\r\n" + 
				"	LEFT OUTER JOIN PHYSICAL_EXAM_SYSTEM L3 ON L2.L2_ID = L3.PARENT_ID) L3\r\n" + 
				"JOIN PHYS_EXAM_TYPE_ASSIGNMENT P ON P.PHYSICAL_EXAM_SYSTEM_ID = L3.L1_ID\r\n" + 
				"WHERE 1 = 1 ";
		List<Object[]> list = genericService.getNativeByCriteria(queryStr, paramTupleList, " ORDER BY L1_ID, L2_ID, L3_ID ", "  ");
		
		Map<String, Map<String, List<GenericVO>>> level1Map = new HashMap<>();
		
		for (Object[] objects : list) {
			Long level1Id = Utils.getLongValue(objects[0]);
			String level1Name = Utils.getStrValue(objects[1]);
			Long level2Id = Utils.getLongValue(objects[2]);
			String level2Name = Utils.getStrValue(objects[3]);
			Long level3Id = Utils.getLongValue(objects[4]);
			String level3Name = Utils.getStrValue(objects[5]);
			Long sumTypeId = Utils.getLongValue(objects[6]);
			
			//GenericVO level1 = new GenericVO(Level1Id, level1Name);
			String level1 = level1Id + "|" + level1Name;
			if (level1Map.get(level1) == null) {
				level1Map.put(level1, new HashMap<String, List<GenericVO>>());
			}
			Map<String, List<GenericVO>> level2Map = level1Map.get(level1);
			//GenericVO level2 = new GenericVO(Level2Id, level2Name);
			String level2 = level2Id + "|" + level2Name;
			if (level2Map.get(level2) == null) {
				level2Map.put(level2, new ArrayList<GenericVO>());
			}
			level2Map.get(level2).add(new GenericVO(level3Id, level3Name));
			
			
		}
		
		return level1Map;
	}
	
	public BaseEntity findPhysicalExam(Class cl, Long key) {
		PhysicalExam physicalExam = (PhysicalExam) this.genericService.find(cl, key);
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.physicalExam.id = ", "physicalExamId", key + "", "Long"));
		
		// Get physical exam systems
		String queryStr =  "SELECT e FROM PhysicalExamResult e WHERE 1 = 1 ";
		List<BaseEntity> vss = genericService.getByCriteria(queryStr, paramTupleList, " ");
		Set<Long> physicalExamSystemIds = new HashSet<Long>();
		
		for (BaseEntity entity : vss) {
			PhysicalExamResult physicalExamResult = (PhysicalExamResult)entity;
			physicalExamSystemIds.add(physicalExamResult.getPhysicalExamSystem().getId());
		}
		physicalExam.setSelectedPhysicalExamSystems(physicalExamSystemIds);
		
		return physicalExam;
		
	}
	
	
	
	// System Review section
	@Transactional
	public BaseEntity save(SystemReview systemReview) {
		SystemReview sr = (SystemReview)this.genericService.save(systemReview);		
		
		// save systemReviewQuestions 
		List<Long> addedSystemReviewQuestionIds = this.genericService.deriveAddedChilds("SYSTEM_REVIEW", "SystemReview", "SYSTEM_REVIEW_ID", 
				systemReview.getId(), systemReview.getSelectedSystemReviewQuestions(), "SystemReviewQuestion", 
				"SYSTEM_REVIEW_QUESTION", "SystemReviewResult", "SYSTEM_REVIEW_RESULT");
		// Insert newly added ones
		for (Long addedId : addedSystemReviewQuestionIds) {
			SystemReviewResult srr = new SystemReviewResult(systemReview.getId(), addedId);
			this.genericService.save(srr);
		}
				
		
		return sr;
	}
	
	public Map<String, Map<String, List<GenericVO>>> getSystemReviewQuestionsBySummaryType(Long summaryTypeId) {
		
		// Get System review questions 
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("P.SUMMARY_TYPE_ID = ", "summaryTypeId", summaryTypeId + "", "Long"));
		String queryStr =  "SELECT L1_ID, L1_NAME, L2_ID, L2_NAME, L3_ID, L3_NAME, P.SUMMARY_TYPE_ID\r\n" + 
				"FROM (SELECT L1_ID, L1_NAME, L2_ID, L2_NAME, \r\n" + 
				"    L3.SYSTEM_REVIEW_QUESTION_ID L3_ID, L3.NAME L3_NAME\r\n" + 
				"    FROM (SELECT L1.SYSTEM_REVIEW_QUESTION_ID L1_ID, L1.NAME L1_NAME, \r\n" + 
				"			L2.SYSTEM_REVIEW_QUESTION_ID L2_ID, L2.NAME L2_NAME\r\n" + 
				"		FROM (SELECT SYSTEM_REVIEW_QUESTION_ID, NAME\r\n" + 
				"			FROM SYSTEM_REVIEW_QUESTION \r\n" + 
				"			WHERE PARENT_ID IS NULL) L1\r\n" + 
				"		LEFT OUTER JOIN SYSTEM_REVIEW_QUESTION L2 ON L1.SYSTEM_REVIEW_QUESTION_ID = L2.PARENT_ID) L2\r\n" + 
				"	LEFT OUTER JOIN SYSTEM_REVIEW_QUESTION L3 ON L2.L2_ID = L3.PARENT_ID) L3\r\n" + 
				"JOIN SYSTEM_REVIEW_Q_ASSIGNMENT P ON P.SYSTEM_REVIEW_QUESTION_ID = L3.L1_ID\r\n" + 
				"WHERE 1 = 1 ";
		List<Object[]> list = genericService.getNativeByCriteria(queryStr, paramTupleList, " ORDER BY L1_ID, L2_ID, L3_ID ", "  ");
		
		Map<String, Map<String, List<GenericVO>>> level1Map = new HashMap<>();
		
		for (Object[] objects : list) {
			Long level1Id = Utils.getLongValue(objects[0]);
			String level1Name = Utils.getStrValue(objects[1]);
			Long level2Id = Utils.getLongValue(objects[2]);
			String level2Name = Utils.getStrValue(objects[3]);
			Long level3Id = Utils.getLongValue(objects[4]);
			String level3Name = Utils.getStrValue(objects[5]);
			Long sumTypeId = Utils.getLongValue(objects[6]);
			
			//GenericVO level1 = new GenericVO(Level1Id, level1Name);
			String level1 = level1Id + "|" + level1Name;
			if (level1Map.get(level1) == null) {
				level1Map.put(level1, new HashMap<String, List<GenericVO>>());
			}
			Map<String, List<GenericVO>> level2Map = level1Map.get(level1);
			//GenericVO level2 = new GenericVO(Level2Id, level2Name);
			String level2 = level2Id + "|" + level2Name;
			if (level2Map.get(level2) == null) {
				level2Map.put(level2, new ArrayList<GenericVO>());
			}
			level2Map.get(level2).add(new GenericVO(level3Id, level3Name));
			
			
		}
		
		return level1Map;
	}
	
	public BaseEntity findSystemReview(Class cl, Long key) {
		SystemReview systemReview = (SystemReview) this.genericService.find(cl, key);
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.systemReview.id = ", "systemReviewId", key + "", "Long"));
		
		// Get system review questions
		String queryStr =  "SELECT e FROM SystemReviewResult e WHERE 1 = 1 ";
		List<BaseEntity> vss = genericService.getByCriteria(queryStr, paramTupleList, " ");
		Set<Long> systemReviewQuestionIds = new HashSet<Long>();
		
		for (BaseEntity entity : vss) {
			SystemReviewResult systemReviewResult = (SystemReviewResult)entity;
			systemReviewQuestionIds.add(systemReviewResult.getSystemReviewQuestion().getId());
		}
		systemReview.setSelectedSystemReviewQuestions(systemReviewQuestionIds);
		
		return systemReview;
		
	}

}
