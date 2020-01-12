package com.qkcare.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.qkcare.domain.GenericVO;
import com.qkcare.model.BaseEntity;
import com.qkcare.model.PhysicalExam;
import com.qkcare.model.Summary;
import com.qkcare.model.SystemReview;


@Service(value="summaryService")
public interface SummaryService {
	
	public BaseEntity save(Summary summary);
	public BaseEntity findSummary(Class cl, Long key);
	public BaseEntity findSummaryByPresence(String label, Long labelId);
	
	public BaseEntity save(PhysicalExam physicalExam);
	public Map<String, Map<String, List<GenericVO>>> getPhysicalExamSystemsBySummaryType(Long summaryTypeId);
	public BaseEntity findPhysicalExam(Class cl, Long key);
	
	
	public BaseEntity save(SystemReview systemReview);
	public Map<String, Map<String, List<GenericVO>>> getSystemReviewQuestionsBySummaryType(Long summaryTypeId);
	public BaseEntity findSystemReview(Class cl, Long key);
}
