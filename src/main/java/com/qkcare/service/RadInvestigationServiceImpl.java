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
import org.springframework.beans.factory.annotation.Autowired;

import com.qkcare.domain.SearchCriteria;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.imaging.RadInvestigation;
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
		boolean isAddition = investigation.getId() == null;
		
		BaseEntity toReturn = this.genericService.save(investigation);
		return toReturn;
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
