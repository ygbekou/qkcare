package com.qkcare.service;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qkcare.domain.SearchCriteria;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.Investigation;
import com.qkcare.model.InvestigationTest;
import com.qkcare.model.LabTest;
import com.qkcare.util.DateUtil;

@Service(value="investigationService")
public class InvestigationServiceImpl  implements InvestigationService {
	
	private static Logger logger = Logger.getLogger(InvestigationServiceImpl.class);
	
	@Autowired
	GenericService genericService;
	
	@Autowired
	private EntityManager entityManager;
	
	@Transactional
	public BaseEntity save(Investigation investigation) {
		boolean isAddition = investigation.getId() == null;
		
		BaseEntity toReturn = this.genericService.save(investigation);
		
		if (isAddition) {
			// Initial save 
			List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
			paramTupleList.add(Quartet.with("e.parent.id = ", "parentId", investigation.getLabTest().getId() + "", "Long"));
			String queryStr =  "SELECT e FROM LabTest e WHERE 1 = 1 ";
			List<BaseEntity> labTests = genericService.getByCriteria(queryStr, paramTupleList, null);
			
			for (BaseEntity lt : labTests) {
				InvestigationTest investigationTest = new InvestigationTest();
				investigationTest.setLabTest((LabTest)lt);
				investigationTest.setInvestigation((Investigation)toReturn);
				this.genericService.save(investigationTest);
			}
		} 
		return toReturn;
	}
	
	
	@Transactional
	public void saveInvestigationTests(List<InvestigationTest> investigationTests) {
		
		// Get 
		for (InvestigationTest investigationTest : investigationTests) {
			this.genericService.save(investigationTest);
		}
		
	}
	
	
	@Transactional
	public List<BaseEntity> getInvestigations(SearchCriteria searchCriteria) {
		
		StringBuilder queryBuilder = new StringBuilder("SELECT e FROM Investigation e "
				+ "LEFT OUTER JOIN e.visit v "
				+ "LEFT OUTER JOIN e.admission a "
				+ "WHERE 1 = 1 ");

		// Build the query
		if (StringUtils.isNotBlank(searchCriteria.getMedicalRecordNumber())) {
			queryBuilder.append(" AND (v.patient.id = :patientId OR a.patient.id = :patientId) ");
		}
		
		if (searchCriteria.getAdmissionId() != null) {
			queryBuilder.append(" AND (a.id = :admissionId) ");
		}
		
		if (searchCriteria.getVisitId() != null) {
			queryBuilder.append(" AND (v.id = :visitId) ");
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getInvestigationDateStart())) {
			queryBuilder.append(" AND (e.investigationDatetime >= :investigationDateStart) ");
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getInvestigationDateEnd())) {
			queryBuilder.append(" AND (e.investigationDatetime < :investigationDateEnd) ");
		}

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
				query.setParameter("investigationDateStart", DateUtil.parseDate(searchCriteria.getInvestigationDateStart(), "MM/dd/yyyy"),
					TemporalType.DATE);
			} catch (ParseException pe) {
				logger.error("Exception happened: " + pe);
			}
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getInvestigationDateEnd())) {
			try {
				query.setParameter("investigationDateEnd", DateUtil.parseDate(searchCriteria.getInvestigationDateEnd(), "MM/dd/yyyy"),
					TemporalType.DATE);
			} catch (ParseException pe) {
				logger.error("Exception happened: " + pe);
			}
		}
		return query.getResultList();
		
	}
	
}
