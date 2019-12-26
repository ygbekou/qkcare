package com.qkcare.service;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.javatuples.Quartet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import com.qkcare.domain.SearchCriteria;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.DoctorOrderType;
import com.qkcare.model.Investigation;
import com.qkcare.model.InvestigationTest;
import com.qkcare.model.LabTest;
import com.qkcare.model.Service;
import com.qkcare.util.DateUtil;

@org.springframework.stereotype.Service(value="investigationService")
public class InvestigationServiceImpl  implements InvestigationService {
	
	private static Logger logger = Logger.getLogger(InvestigationServiceImpl.class);
	
	@Autowired
	GenericService genericService;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	BillingService billingService;
	
	@Transactional
	public BaseEntity save(Investigation investigation) {
		boolean isAddition = investigation.getId() == null;
		
		BaseEntity toReturn = this.genericService.save(investigation); 
		
		if (investigation.getStatus() == 4) {
			this.billingService.save(investigation);
		}
		
		if (isAddition) {
			// Initial save 
			List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
			paramTupleList.add(Quartet.with("e.parent.id = ", "parentId", investigation.getLabTest().getId() + "", "Long"));
			String queryStr =  "SELECT e FROM LabTest e WHERE 1 = 1 ";
			List<BaseEntity> labTests = genericService.getByCriteria(queryStr, paramTupleList, null);
			
			if (labTests.isEmpty()) {
				labTests.add(investigation.getLabTest());
			}

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
	public List<BaseEntity> getInvestigations(SearchCriteria searchCriteria, String queryHeader) {
		
		boolean foundCriteria = false;
		
		StringBuilder queryBuilder = new StringBuilder(queryHeader
				+ "LEFT OUTER JOIN e.visit v "
				+ "LEFT OUTER JOIN v.patient p1 "
				+ "LEFT OUTER JOIN p1.user u1 "
				+ "LEFT OUTER JOIN e.admission a "
				+ "LEFT OUTER JOIN a.patient p2 "
				+ "LEFT OUTER JOIN p2.user u2 "
				+ "WHERE 1 = 1 ");
		
		

		// Build the query
		if (StringUtils.isNotBlank(searchCriteria.getMedicalRecordNumber())) {
			queryBuilder.append(" AND (v.patient.id = :patientId OR a.patient.id = :patientId) ");
			foundCriteria = true;
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getFirstName())) {
			queryBuilder.append(" AND (u1.firstName like :patientFirstName OR u2.firstName like :patientFirstName) ");
			foundCriteria = true;
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getLastName())) {
			queryBuilder.append(" AND (u1.lastName like :patientLastName OR u2.lastName like :patientLastName) ");
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

		if (StringUtils.isNotBlank(searchCriteria.getFirstName())) {
			query.setParameter("patientFirstName", '%' + searchCriteria.getFirstName() + '%');
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getLastName())) {
			query.setParameter("patientLastName", '%' + searchCriteria.getLastName() + '%');
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
					TemporalType.TIMESTAMP);
			} catch (ParseException pe) {
				logger.error("Exception happened: " + pe);
			}
		}
		
		if (StringUtils.isNotBlank(searchCriteria.getInvestigationDateEnd())) {
			try {
				query.setParameter("investigationDateEnd", DateUtil.parseDate(searchCriteria.getInvestigationDateEnd(), "MM/dd/yyyy"),
					TemporalType.TIMESTAMP);
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
	
	
	@Transactional
	public LabTest saveLabTest(LabTest labTest) {
		
		this.genericService.save(labTest);
		
		List<Quartet<String, String, String, String>> paramTupleList = new ArrayList<Quartet<String, String, String, String>>();
		paramTupleList.add(Quartet.with("e.labTest.id = ", "labTestId", labTest.getId() + "", "Long"));
		String queryStr =  "SELECT e FROM Service e WHERE 1 = 1 ";
		List<BaseEntity> services = genericService.getByCriteria(queryStr, paramTupleList, null);
		
		if (services.isEmpty()) {
			DoctorOrderType serviceType = (DoctorOrderType) this.genericService.find(DoctorOrderType.class, 2L);
			Service service = new Service(labTest, serviceType);
			this.genericService.save(service);
		}
		
		return labTest;
	}
}
