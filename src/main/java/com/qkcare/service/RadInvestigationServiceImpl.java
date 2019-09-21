package com.qkcare.service;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;

import com.qkcare.domain.SearchCriteria;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.imaging.RadInvestigation;
import com.qkcare.model.imaging.RadInvestigationComment;
import com.qkcare.model.imaging.RadInvestigationExam;
import com.qkcare.util.DateUtil;

@org.springframework.stereotype.Service(value="radInvestigationService")
public class RadInvestigationServiceImpl  implements RadInvestigationService {
	
	private static Logger logger = Logger.getLogger(RadInvestigationServiceImpl.class);
	
	@Autowired
	GenericService genericService;
	
	@Autowired
	private EntityManager entityManager;
	
	@Transactional
	public BaseEntity save(RadInvestigation investigation) {
			
		BaseEntity toReturn = this.genericService.save(investigation);
		
		List<RadInvestigationExam> removedIEs = new ArrayList<>();
		
		for (RadInvestigationExam rie : investigation.getInvestigationExams()) {
			if (rie.getStatus() == 9) {
				this.genericService.delete(RadInvestigationExam.class, Arrays.asList(new Long[]{rie.getId()}));
				removedIEs.add(rie);
			} else {
				rie.setInvestigation((RadInvestigation)toReturn);
				this.genericService.save(rie);
			}
		}
		
		investigation.getInvestigationExams().removeAll(removedIEs);
		
		toReturn = this.genericService.save(investigation);
		
		return toReturn;
	}
	
	
	public BaseEntity findInvestigation(Class cl, Long key) {
		RadInvestigation investigation = (RadInvestigation) this.genericService.find(cl, key);
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.investigation.id = ", "investigationId", key + "", "Long"));
		String queryStr =  "SELECT e FROM RadInvestigationExam e WHERE 1 = 1 ";
		List<BaseEntity> exams = genericService.getByCriteria(queryStr, paramTupleList, " ");
		List<RadInvestigationExam> investigationExams = new ArrayList<RadInvestigationExam>();
		
		for (BaseEntity entity : exams) {
			RadInvestigationExam investigationExam = (RadInvestigationExam)entity;
			investigationExam.setInvestigation(null);
			investigationExams.add(investigationExam);
		}
		
		investigation.setInvestigationExams(investigationExams);
		
		queryStr =  "SELECT e FROM RadInvestigationComment e WHERE 1 = 1 ";
		List<BaseEntity> comments = genericService.getByCriteria(queryStr, paramTupleList, " ");
		List<RadInvestigationComment> investigationComments = new ArrayList<RadInvestigationComment>();
		
		for (BaseEntity entity : comments) {
			RadInvestigationComment investigationComment = (RadInvestigationComment)entity;
			investigationComment.setInvestigation(null);
			investigationComments.add(investigationComment);
		}
		
		investigation.setInvestigationComments(investigationComments);
		
		return investigation;
		
	}
	
	
	@Transactional
	public List<BaseEntity> getInvestigations(SearchCriteria searchCriteria) {
		
		boolean foundCriteria = false;
		
		StringBuilder queryBuilder = new StringBuilder("SELECT e FROM RadInvestigation e "
				+ "LEFT OUTER JOIN e.visit v "
				+ "LEFT OUTER JOIN e.admission a "
				+ "WHERE 1 = 1 ");
		
		

		// Build the query
		if (StringUtils.isNotBlank(searchCriteria.getMedicalRecordNumber())) {
			queryBuilder.append(" AND (v.patient.id = :patientId OR a.patient.id = :patientId) ");
			foundCriteria = true;
		}
		
		if (searchCriteria.getAdmissionId() != null) {
			queryBuilder.append(" AND (a.id = :admissionId) ");
			foundCriteria = true;
		}
		
		if (searchCriteria.getVisitId() != null) {
			queryBuilder.append(" AND (v.id = :visitId) ");
			foundCriteria = true;
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getInvestigationDateStart())) {
			queryBuilder.append(" AND (e.investigationDatetime >= :investigationDateStart) ");
			foundCriteria = true;
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getInvestigationDateEnd())) {
			queryBuilder.append(" AND (e.investigationDatetime < :investigationDateEnd) ");
			foundCriteria = true;
		}

		queryBuilder.append(" ORDER BY e.investigationDatetime DESC ");
		
		Query query = this.entityManager.createQuery(queryBuilder.toString());

		if (StringUtils.isNotBlank(searchCriteria.getMedicalRecordNumber())) {
			query.setParameter("patientId", new Long(searchCriteria.getMedicalRecordNumber()));
		}

		if (searchCriteria.getAdmissionId() != null) {
			query.setParameter("admissionId", searchCriteria.getAdmissionId());
		}
		
		if (searchCriteria.getVisitId() != null) {
			query.setParameter("visitId", searchCriteria.getVisitId());
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getInvestigationDateStart())) {
			try {
				query.setParameter("investigationDateStart", DateUtil.parseDate(
						searchCriteria.getInvestigationDateStart().substring(1, 3) + "/"
						+ searchCriteria.getInvestigationDateStart().substring(6, 8) + "/"
						+ searchCriteria.getInvestigationDateStart().substring(11, 15), "MM/dd/yyyy"),
					TemporalType.DATE);
			} catch (ParseException pe) {
				logger.error("Exception happened: " + pe);
			}
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getInvestigationDateEnd())) {
			try {
				query.setParameter("investigationDateEnd", DateUtil.parseDate(searchCriteria.getInvestigationDateEnd().substring(1, 3) + "/"
						+ searchCriteria.getInvestigationDateEnd().substring(6, 8) + "/"
						+ searchCriteria.getInvestigationDateEnd().substring(11, 15), "MM/dd/yyyy"),
					TemporalType.DATE);
			} catch (ParseException pe) {
				logger.error("Exception happened: " + pe);
			}
		}
		
		if (foundCriteria) {
			return query.getResultList();
		}
		else {
			return new ArrayList<BaseEntity>();
		}
		
	}
}
