package com.qkcare.service;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;

import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkcare.domain.GenericVO;
import com.qkcare.model.Admission;
import com.qkcare.model.AdmissionDiagnosis;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.PatientAllergy;
import com.qkcare.model.PatientFamilyHistory;
import com.qkcare.model.PatientHomeMedication;
import com.qkcare.model.PatientMedicalHistory;
import com.qkcare.model.PatientSocialHistory;
import com.qkcare.model.PatientSurgicalHistory;
import com.qkcare.model.PatientVaccine;
import com.qkcare.model.PhysicalExam;
import com.qkcare.model.PhysicalExamResult;
import com.qkcare.model.Summary;
import com.qkcare.model.SummaryVitalSign;
import com.qkcare.model.SystemReview;
import com.qkcare.model.SystemReviewResult;
import com.qkcare.model.Visit;
import com.qkcare.model.VitalSign;
import com.qkcare.model.stocks.PatientSaleProduct;
import com.qkcare.util.DateUtil;
import com.qkcare.util.Utils;

@Service(value="summaryService")
public class SummaryServiceImpl  implements SummaryService {
	
	@Autowired
	GenericService genericService;
	
	@Autowired
	AdmissionService admissionService;
	
	@Autowired
	InvestigationService investigationService;
	
	@Autowired
	private EntityManager entityManager;

	
	@Transactional
	public BaseEntity save(Summary summary) {
		Summary su = (Summary)this.genericService.save(summary);	
		
		if (summary.getAdmissionDiagnoses() != null) {
			for (AdmissionDiagnosis admissionDiagnosis: summary.getAdmissionDiagnoses()) {
				if (admissionDiagnosis.getId() == null) {
					admissionDiagnosis.setAdmission(summary.getAdmission());
					admissionDiagnosis.setVisit(summary.getVisit());
					this.genericService.save(admissionDiagnosis);
				}
			}
		}
		
		this.save(summary.derivePhysicalExam());
		
		this.save(summary.deriveSystemReview());
		
		return su;
	}
	
	
	public BaseEntity findSummaryByPresence(String label, Long labelId) {
		Summary summary = new Summary();
		summary.setSummaryDatetime(Timestamp.valueOf(LocalDateTime.now()));
		List<Quartet<String, String, String, String>> patientParamTupleList = new ArrayList<Quartet<String, String, String, String>>();
		List<Quartet<String, String, String, String>> patientSaleParamList = new ArrayList<Quartet<String, String, String, String>>();
		List<Quartet<String, String, String, String>> labelParamList = new ArrayList<Quartet<String, String, String, String>>();
		List<Quartet<String, String, String, String>> investigationParamList = new ArrayList<Quartet<String, String, String, String>>();
		Admission admission = null;
		Visit visit  = null;
		
		if ("Admission".equals(label)) {
			admission = (Admission) this.genericService.find(Admission.class, labelId);
			patientParamTupleList.add(Quartet.with("e.patient.id = ", "patientId", admission.getPatient().getId() + "", "Long"));
			patientSaleParamList.add(Quartet.with("e.patientSale.admission.id = ", "admissionId", admission.getId() + "", "Long"));
			labelParamList.add(Quartet.with("e.admission.id = ", "admissionId", admission.getId() + "", "Long"));
			investigationParamList.add(Quartet.with("e.investigation.admission.id = ", "admissionId", admission.getId() + "", "Long"));
			summary.setChiefOfComplain(admission.getAdmissionReason());
			summary.setAdmissionDiagnoses((List)admissionService.getAdmissionChilds("AdmissionDiagnosis", admission.getId()));
		} else if ("Visit".equals(label)){
			visit = (Visit) this.genericService.find(Visit.class, labelId);
			patientParamTupleList.add(Quartet.with("e.patient.id = ", "patientId", visit.getPatient().getId() + "", "Long"));
			patientSaleParamList.add(Quartet.with("e.patientSale.visit.id = ", "visitId", visit.getId() + "", "Long"));
			labelParamList.add(Quartet.with("e.visit.id = ", "visitId", visit.getId() + "", "Long"));
			investigationParamList.add(Quartet.with("e.investigation.visit.id = ", "visitId", visit.getId() + "", "Long"));
			summary.setChiefOfComplain(visit.getChiefOfComplain());
		}
		
		
		this.getPatientMedicalHistory(summary, patientParamTupleList);
		
		this.getPatientSocialHistory(summary, patientParamTupleList);
		
		this.getPatientFamilyHistory(summary, patientParamTupleList);

		this.getPatientSurgicalHistory(summary, patientParamTupleList);
		
		this.getPatientHomeMedications(summary, patientParamTupleList);
		
		this.getPatientMedications(summary, patientSaleParamList);
		
		this.getPatientAllergies(summary, patientParamTupleList);
		
		this.getPatientImmunizations(summary, patientParamTupleList);
		
		this.getReviewSystems(summary, admission, visit, patientParamTupleList);
		
		this.getPhysicalExams(summary, admission, visit, patientParamTupleList);
		
		this.getPatientSummaryVitalSigns(summary, admission, visit, labelParamList);
		
		this.investigationService.getPatientPastInvestigations(summary, investigationParamList);
					
		return summary;
		
	}


	private void getReviewSystems(Summary summary, Admission admission, Visit visit, 
			List<Quartet<String, String, String, String>> paramTupleList) {
		paramTupleList.clear();
		String queryString = "SELECT SRR.SYSTEM_REVIEW_ID, SRR.SYSTEM_REVIEW_RESULT_ID, SRR.SYSTEM_REVIEW_QUESTION_ID  "
				+ "FROM SYSTEM_REVIEW_RESULT SRR "
				+ "WHERE SRR.SYSTEM_REVIEW_ID = (SELECT MAX(SYSTEM_REVIEW_ID) FROM SYSTEM_REVIEW SR WHERE 1 = 1 ";
		
		if (admission != null) {
			queryString += (" AND SR.ADMISSION_ID = " + admission.getId());
		}
		else if (visit != null) {
			queryString += (" AND SR.VISIT_ID = " + visit.getId());
		} 
		if (summary.getId() != null) {
			queryString += (" AND SR.SUMMARY_ID = " + summary.getId());
		}
		
		queryString += " )";
		
		List<Object[]> list = this.genericService.getNativeByCriteria(queryString, paramTupleList, 
				" ", " ");

		for (Object[] objects : list) {
			summary.addSystemReviewQuestionId(new Long(objects[2].toString()));
		}
	}
	
	private void getPhysicalExams(Summary summary, Admission admission, Visit visit, 
			List<Quartet<String, String, String, String>> paramTupleList) {
		paramTupleList.clear();
		String queryString = "SELECT PER.PHYSICAL_EXAM_ID, PER.PHYSICAL_EXAM_RESULT_ID, PER.PHYSICAL_EXAM_SYSTEM_ID  "
				+ "FROM PHYSICAL_EXAM_RESULT PER "
				+ "WHERE PER.PHYSICAL_EXAM_ID = (SELECT MAX(PHYSICAL_EXAM_ID) FROM PHYSICAL_EXAM PE WHERE 1 = 1 ";
		
				if (admission != null) {
					queryString += (" AND PE.ADMISSION_ID = " + admission.getId());
				}
				else if (visit != null) {
					queryString += (" AND PE.VISIT_ID = " + visit.getId());
				} 
				if (summary.getId() != null) {
					queryString += (" AND PE.SUMMARY_ID = " + summary.getId());
				}
				
				queryString += " )";

		List<Object[]> list = this.genericService.getNativeByCriteria(queryString, paramTupleList, 
				" ", " ");

		for (Object[] objects : list) {
			summary.addPhysicalExamSystemId(new Long(objects[2].toString()));
		}
	}
	
	private void getPatientMedicalHistory(Summary summary, List<Quartet<String, String, String, String>> paramTupleList) {
		String queryStr =  "SELECT e FROM PatientMedicalHistory e WHERE 1 = 1 ";
		
		List<BaseEntity> pmhs = genericService.getByCriteria(queryStr, paramTupleList, " ");
		
		for (BaseEntity entity : pmhs) {
			PatientMedicalHistory pmh = (PatientMedicalHistory)entity;
			summary.addMedicalHistory(pmh.getMedicalHistory().getName());
		}
	}
	
	private void getPatientFamilyHistory(Summary summary, List<Quartet<String, String, String, String>> paramTupleList) {
		String queryStr =  "SELECT e FROM PatientFamilyHistory e WHERE 1 = 1 ";
		
		List<BaseEntity> pfhs = genericService.getByCriteria(queryStr, paramTupleList, " ");
		
		for (BaseEntity entity : pfhs) {
			PatientFamilyHistory pfh = (PatientFamilyHistory)entity;
			summary.addFamilyHistory(pfh.getMedicalHistory().getName());
		}
	}
	
	private void getPatientSocialHistory(Summary summary, List<Quartet<String, String, String, String>> paramTupleList) {
		String queryStr =  "SELECT e FROM PatientSocialHistory e WHERE 1 = 1 ";
		
		List<BaseEntity> pfhs = genericService.getByCriteria(queryStr, paramTupleList, " ");
		
		for (BaseEntity entity : pfhs) {
			PatientSocialHistory psh = (PatientSocialHistory)entity;
			summary.addSocialHistory(psh.getSocialHistory().getName());
		}
	}
	
	private void getPatientSurgicalHistory(Summary summary, List<Quartet<String, String, String, String>> paramTupleList) {
		String queryStr =  "SELECT e FROM PatientSurgicalHistory e WHERE 1 = 1 ";
		
		List<BaseEntity> pfhs = genericService.getByCriteria(queryStr, paramTupleList, " ");
		
		for (BaseEntity entity : pfhs) {
			PatientSurgicalHistory psh = (PatientSurgicalHistory)entity;
			summary.addSurgicalHistory(psh.getSurgicalProcedureName());
		}
	}
	
	private void getPatientHomeMedications(Summary summary, List<Quartet<String, String, String, String>> paramTupleList) {
		String queryStr =  "SELECT e FROM PatientHomeMedication e WHERE 1 = 1 ";
		
		List<BaseEntity> phms = genericService.getByCriteria(queryStr, paramTupleList, " ");
		
		for (BaseEntity entity : phms) {
			PatientHomeMedication phm = (PatientHomeMedication)entity;
			summary.addHomeMedication(phm.getHomeMedicationDescription());
		}
	}
	
	private void getPatientMedications(Summary summary, List<Quartet<String, String, String, String>> paramTupleList) {
		String queryStr =  "SELECT e FROM PatientSaleProduct e WHERE 1 = 1 ";
		
		List<BaseEntity> pms = genericService.getByCriteria(queryStr, paramTupleList, " ");
		
		for (BaseEntity entity : pms) {
			PatientSaleProduct pm = (PatientSaleProduct)entity;
			summary.addMedication(pm.getProcductDescription());
		}
	}
	
	private void getPatientAllergies(Summary summary, List<Quartet<String, String, String, String>> paramTupleList) {
		String queryStr =  "SELECT e FROM PatientAllergy e WHERE 1 = 1 ";
		
		List<BaseEntity> pas = genericService.getByCriteria(queryStr, paramTupleList, " ");
		
		for (BaseEntity entity : pas) {
			PatientAllergy pa = (PatientAllergy)entity;
			summary.addAllergy(pa.getAllergyDescription());
		}
	}
	
	private void getPatientImmunizations(Summary summary, List<Quartet<String, String, String, String>> paramTupleList) {
		String queryStr =  "SELECT e FROM PatientVaccine e WHERE 1 = 1 ";
		
		List<BaseEntity> pvs = genericService.getByCriteria(queryStr, paramTupleList, " ");
		
		for (BaseEntity entity : pvs) {
			PatientVaccine pv = (PatientVaccine)entity;
			summary.addImmunization(pv.getImmunizationDescription());
		}
	}
	
	private void getPatientSummaryVitalSigns(Summary summary, Admission admission, Visit visit, 
			List<Quartet<String, String, String, String>> paramTupleList) {
		String queryStr =  "SELECT VS2.VITAL_SIGN_ID, MIN(VS1.TEMPERATURE), MAX(VS1.TEMPERATURE), VS2.TEMPERATURE, " +
				"MIN(VS1.HEART_RATE), MAX(VS1.HEART_RATE), VS2.HEART_RATE, " + 
				"MIN(VS1.RESPIRATORY_RATE), MAX(VS1.RESPIRATORY_RATE), VS2.RESPIRATORY_RATE, " + 
				"MIN(VS1.SYSTOLIC_BLOOD_PRESSURE), MAX(VS1.SYSTOLIC_BLOOD_PRESSURE), VS2.SYSTOLIC_BLOOD_PRESSURE, " + 
				"MIN(VS1.DIASTOLIC_BLOOD_PRESSURE), MAX(VS1.DIASTOLIC_BLOOD_PRESSURE), VS2.DIASTOLIC_BLOOD_PRESSURE, " + 
				"MIN(VS1.MEAN_BLOOD_PRESSURE), MAX(VS1.MEAN_BLOOD_PRESSURE), VS2.MEAN_BLOOD_PRESSURE, " + 
				"MIN(VS1.PULSE), MAX(VS1.PULSE), VS2.PULSE, " + 
				"MIN(VS1.BLOOD_SUGAR), MAX(VS1.BLOOD_SUGAR), VS2.BLOOD_SUGAR " + 
				"FROM VITAL_SIGN VS1, VITAL_SIGN VS2 " + 
				"WHERE VS2.VITAL_SIGN_ID = (SELECT MAX(VITAL_SIGN_ID) FROM VITAL_SIGN "
								+ "WHERE ADMISSION_CRITERIA "
								+ "AND VITAL_SIGN_DATETIME >= :vitalSignDatetimeStart "
								+ "AND VITAL_SIGN_DATETIME <= :vitalSignDatetimeEnd ) "
				+ "AND VS1.ADMISSION_CRITERIA AND VS1.VITAL_SIGN_DATETIME >= :vitalSignDatetimeStart "
				+ "AND VS1.VITAL_SIGN_DATETIME <= :vitalSignDatetimeEnd " ;
		
		if (admission != null) {
			queryStr = queryStr.replaceAll("ADMISSION_CRITERIA", "ADMISSION_ID = :admissionId ");
		} else {
			queryStr = queryStr.replaceAll("ADMISSION_CRITERIA", "VISIT_ID = :visitId ");
		}
		
		LocalDateTime endDateTime = LocalDateTime.now();
		LocalDateTime startDateTime = endDateTime.minusDays(100);
		
		
		Query query = entityManager.createNativeQuery(queryStr);
		query.setParameter(paramTupleList.get(0).getValue1(), new Long(paramTupleList.get(0).getValue2()));
		query.setParameter("vitalSignDatetimeEnd", DateUtil.asDate(endDateTime), TemporalType.TIMESTAMP);
		query.setParameter("vitalSignDatetimeStart", DateUtil.asDate(startDateTime), TemporalType.TIMESTAMP);
		

		List<Object[]> list = query.getResultList();
		List<SummaryVitalSign> summaryVitalSigns = new ArrayList<>();
		
		String[] vitalSignTypeList = {"TEMPERATURE", "HEART_RATE", "RESPIRATORY_RATE", "NISBP", "NIDBP", "NIMBP", "PULSE", "BLOOD_SUGAR"};
		
		for (Object[] obj : list) {
			if (obj[0] != null) {
				int i = 1;
				for (String vitalSignType: vitalSignTypeList) {
					
					SummaryVitalSign summaryVitalSign = new SummaryVitalSign(vitalSignType,
						Utils.getStrValue(obj[i]), Utils.getStrValue(obj[i + 1]), Utils.getStrValue(obj[i + 2]));
					
					summaryVitalSigns.add(summaryVitalSign);
					if (i == 24) break;
					i += 3;
				}
			}
		}	
			
		summary.setSummaryVitalSigns(summaryVitalSigns);
	}
	
	private void getPatientVitalSigns(Summary summary, List<Quartet<String, String, String, String>> paramTupleList) {
		String queryStr =  "SELECT e FROM VitalSign e WHERE 1 = 1 ";
		LocalDateTime endDateTime = LocalDateTime.now();
		LocalDateTime startDateTime = endDateTime.minusDays(1);
		List<Quartet<String, String, String, String>> paramTupleListCopy = new ArrayList<>();
		paramTupleListCopy.addAll(paramTupleList);
		
		paramTupleListCopy.add(Quartet.with("e.vitalSignDatetime >= ", "vitalSignDatetimeStart", 
				DateUtil.formatDate(startDateTime, "MM/dd/yyyy hh:mm:ss a"), "Timestamp"));
		paramTupleListCopy.add(Quartet.with("e.vitalSignDatetime <= ", "vitalSignDatetimeEnd", 
				DateUtil.formatDate(endDateTime, "MM/dd/yyyy hh:mm:ss a"), "Timestamp"));
		
		List<BaseEntity> vss = genericService.getByCriteria(queryStr, paramTupleListCopy, " ");
		
		for (BaseEntity entity : vss) {
			VitalSign vs = (VitalSign)entity;
			summary.addVitalSign(vs);
		}
	}
	
	public BaseEntity findSummary(Class cl, Long key) {
		Summary summary = (Summary) this.genericService.find(cl, key);
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.summary.id = ", "summaryId", key + "", "Long"));
		
		this.getReviewSystems(summary, summary.getAdmission(), summary.getVisit(), paramTupleList);
		
		this.getPhysicalExams(summary, summary.getAdmission(), summary.getVisit(), paramTupleList);
		
		if (key == 0) {
			summary = new Summary();
		
			paramTupleList.clear();
			if (summary.getAdmission() != null) {
				paramTupleList.add(Quartet.with("e.admission.id = ", "admissionId", summary.getAdmission().getId() + "", "Long"));
			} else if (summary.getVisit() != null){
				paramTupleList.add(Quartet.with("e.visit.id = ", "visitId", summary.getVisit().getId() + "", "Long"));
			}
			
			// Get admission diagnosis list
			
//			String queryStr =  "SELECT e FROM AdmissionDiagnosis e WHERE 1 = 1 ";
//			
//			List<BaseEntity> vss = genericService.getByCriteria(queryStr, paramTupleList, " ");
//			
//			for (BaseEntity entity : vss) {
//				AdmissionDiagnosis ad = (AdmissionDiagnosis)entity;
//				summary.setMedicalHistory(ad.getMedicalHistoryValue() + "\n");
//			}
			
			
			
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
