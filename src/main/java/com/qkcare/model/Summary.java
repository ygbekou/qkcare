package com.qkcare.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.json.simple.JSONObject;

import com.qkcare.util.DateUtil;


@Entity
@Table(name = "SUMMARY")
public class Summary extends BaseEntity {
	
	@Id
	@Column(name = "SUMMARY_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne(optional = true)
	@JoinColumn(name = "ADMISSION_ID", nullable = true)
	private Admission admission;
	@ManyToOne(optional = true)
	@JoinColumn(name = "VISIT_ID", nullable = true)
	private Visit visit;
	@ManyToOne
	@JoinColumn(name = "SUMMARY_TYPE_ID")
	private SummaryType summaryType;
	@ManyToOne
	@JoinColumn(name = "SUMMARY_STATUS_ID")
	private SummaryStatus summaryStatus;
	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID")
	private Employee author;
	@Column(name = "SUMMARY_DATETIME")
	private Timestamp summaryDatetime;
	@Column(name = "HISTORY_OF_PRESENT_ILLNESS")
	private String historyOfPresentIllness;
	private String description;
	private String subject;
	private String subjective;
	private String objective;
	@Column(name = "CHIEF_OF_COMPLAIN")
	private String chiefOfComplain;
	@Column(name = "MEDICAL_HISTORY")
	private String pastMedicalHistory;
	@Column(name = "FAMILY_HISTORY")
	private String familyHistory;
	@Column(name = "SOCIAL_HISTORY")
	private String socialHistory;
	@Column(name = "SURGICAL_HISTORY")
	private String surgicalHistory;
	@Column(name = "HOME_MEDICATIONS")
	private String homeMedications;
	@Column(name = "MEDICATIONS")
	private String medications;
	@Column(name = "ALLERGIES")
	private String allergies;
	@Column(name = "IMMUNIZATIONS")
	private String immunizations;
	@Column(name = "PLAN")
	private String plan;
	@Column(name = "ASSESSMENT")
	private String assessment;
	@Column(name = "POWER_OF_ATTORNEY")
	private String powerOfAttorney;
	@Column(name = "PRIMARY_CARE_PHYSICIAN")
	private String primaryCarePhysician;
	@ManyToOne
	@JoinColumn(name = "CODE_STATUS_ID")
	private CodeStatus codeStatus;
	
	@Transient
	private List<AdmissionDiagnosis> AdmissionDiagnoses;
	
	@Transient
	private Set<Long> selectedSystemReviewQuestionIds; 
	
	@Transient
	private Set<Long> selectedPhysicalExamSystemIds;
	
	@Transient
	private List<VitalSign> vitalSigns; 
	
	@Transient
	private List<SummaryVitalSign> summaryVitalSigns; 
	
	@Transient
	private List<InvestigationTest> investigationTests; 
	
	@Transient
	List<List<JSONObject>> investigationJsonObjects;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Admission getAdmission() {
		return admission;
	}
	public void setAdmission(Admission admission) {
		this.admission = admission;
	}
	public Visit getVisit() {
		return visit;
	}
	public void setVisit(Visit visit) {
		this.visit = visit;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public SummaryType getSummaryType() {
		return summaryType;
	}
	public void setSummaryType(SummaryType summaryType) {
		this.summaryType = summaryType;
	}
	public SummaryStatus getSummaryStatus() {
		return summaryStatus;
	}
	public void setSummaryStatus(SummaryStatus summaryStatus) {
		this.summaryStatus = summaryStatus;
	}
	public Employee getAuthor() {
		return author;
	}
	public void setAuthor(Employee author) {
		this.author = author;
	}
	public Timestamp getSummaryDatetime() {
		return summaryDatetime;
	}
	public void setSummaryDatetime(Timestamp summaryDatetime) {
		this.summaryDatetime = summaryDatetime;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getHistoryOfPresentIllness() {
		return historyOfPresentIllness;
	}
	public void setHistoryOfPresentIllness(String historyOfPresentIllness) {
		this.historyOfPresentIllness = historyOfPresentIllness;
	}
	public String getSubjective() {
		return subjective;
	}
	public void setSubjective(String subjective) {
		this.subjective = subjective;
	}
	public String getObjective() {
		return objective;
	}
	public void setObjective(String objective) {
		this.objective = objective;
	}
	public String getPastMedicalHistory() {
		return pastMedicalHistory;
	}
	public void setPastMedicalHistory(String pastMedicalHistory) {
		this.pastMedicalHistory = pastMedicalHistory;
	}
	public String getFamilyHistory() {
		return familyHistory;
	}
	public void setFamilyHistory(String familyHistory) {
		this.familyHistory = familyHistory;
	}
	public String getSocialHistory() {
		return socialHistory;
	}
	public void setSocialHistory(String socialHistory) {
		this.socialHistory = socialHistory;
	}
	
	public String getSurgicalHistory() {
		return surgicalHistory;
	}
	public void setSurgicalHistory(String surgicalHistory) {
		this.surgicalHistory = surgicalHistory;
	}
	public String getHomeMedications() {
		return homeMedications;
	}
	public void setHomeMedications(String homeMedications) {
		this.homeMedications = homeMedications;
	}
	public String getMedications() {
		return medications;
	}
	public void setMedications(String medications) {
		this.medications = medications;
	}
	public String getAllergies() {
		return allergies;
	}
	public void setAllergies(String allergies) {
		this.allergies = allergies;
	}
	public String getImmunizations() {
		return immunizations;
	}
	public void setImmunizations(String immunizations) {
		this.immunizations = immunizations;
	}
	public CodeStatus getCodeStatus() {
		return codeStatus;
	}
	public void setCodeStatus(CodeStatus codeStatus) {
		this.codeStatus = codeStatus;
	}
	public String getChiefOfComplain() {
		return chiefOfComplain;
	}
	public void setChiefOfComplain(String chiefOfComplain) {
		this.chiefOfComplain = chiefOfComplain;
	}
	
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public String getAssessment() {
		return assessment;
	}
	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}
	public String getPowerOfAttorney() {
		return powerOfAttorney;
	}
	public void setPowerOfAttorney(String powerOfAttorney) {
		this.powerOfAttorney = powerOfAttorney;
	}
	public String getPrimaryCarePhysician() {
		return primaryCarePhysician;
	}
	public void setPrimaryCarePhysician(String primaryCarePhysician) {
		this.primaryCarePhysician = primaryCarePhysician;
	}
	public List<AdmissionDiagnosis> getAdmissionDiagnoses() {
		return AdmissionDiagnoses;
	}
	public void setAdmissionDiagnoses(List<AdmissionDiagnosis> admissionDiagnoses) {
		AdmissionDiagnoses = admissionDiagnoses;
	}
	public Set<Long> getSelectedSystemReviewQuestionIds() {
		return selectedSystemReviewQuestionIds;
	}
	public void setSelectedSystemReviewQuestionIds(Set<Long> selectedSystemReviewQuestionIds) {
		this.selectedSystemReviewQuestionIds = selectedSystemReviewQuestionIds;
	}
	public Set<Long> getSelectedPhysicalExamSystemIds() {
		return selectedPhysicalExamSystemIds;
	}
	public void setSelectedPhysicalExamSystemIds(Set<Long> selectedPhysicalExamSystemIds) {
		this.selectedPhysicalExamSystemIds = selectedPhysicalExamSystemIds;
	}
	public List<VitalSign> getVitalSigns() {
		return vitalSigns;
	}
	public void setVitalSigns(List<VitalSign> vitalSigns) {
		this.vitalSigns = vitalSigns;
	}
	public List<SummaryVitalSign> getSummaryVitalSigns() {
		return summaryVitalSigns;
	}
	public void setSummaryVitalSigns(List<SummaryVitalSign> summaryVitalSigns) {
		this.summaryVitalSigns = summaryVitalSigns;
	}
	public List<InvestigationTest> getInvestigationTests() {
		return investigationTests;
	}
	public void setInvestigationTests(List<InvestigationTest> investigationTests) {
		this.investigationTests = investigationTests;
	}
	public List<List<JSONObject>> getInvestigationJsonObjects() {
		return investigationJsonObjects;
	}
	public void setInvestigationJsonObjects(List<List<JSONObject>> investigationJsonObjects) {
		this.investigationJsonObjects = investigationJsonObjects;
	}
	// Transient attributes
	public String getSummaryDate() {
		return DateUtil.formatDate(this.getSummaryDatetime(), DateUtil.DATE_FORMAT);
	}
	
	public String getShortMenu() {
		return DateUtil.formatDate(this.getSummaryDatetime(), DateUtil.TIME_WITHOUT_SECONDS_FORMAT) 
				+ " " + (this.getAuthor() != null ? this.getAuthor().getName() : "") + " - " + this.getSubject();
	}
	
	public void addMedicalHistory(String medicalHistory) {
		this.setPastMedicalHistory((this.getPastMedicalHistory() != null ? (this.getPastMedicalHistory() + "\n") : "")  + medicalHistory);
	}
	
	public void addFamilyHistory(String familyHistory) {
		this.setFamilyHistory((this.getFamilyHistory() != null ? (this.getFamilyHistory() + "\n") : "")  + familyHistory);
	}
	
	public void addSocialHistory(String socialHistory) {
		this.setSocialHistory((this.getSocialHistory() != null ? (this.getSocialHistory() + "\n") : "")  + socialHistory);
	}
	
	public void addSurgicalHistory(String surgicalHistory) {
		this.setSurgicalHistory((this.getSurgicalHistory() != null ? (this.getSurgicalHistory() + "\n") : "")  + surgicalHistory);
	}
	public void addHomeMedication(String homeMedication) {
		this.setHomeMedications((this.getHomeMedications() != null ? (this.getHomeMedications() + "\n") : "")  + homeMedication);
	}
	public void addMedication(String medication) {
		this.setMedications((this.getMedications() != null ? (this.getMedications() + "\n") : "")  + medication);
	}
	public void addAllergy(String allergy) {
		this.setAllergies((this.getAllergies() != null ? (this.getAllergies() + "\n") : "")  + allergy);
	}
	public void addImmunization(String immunization) {
		this.setImmunizations((this.getImmunizations() != null ? (this.getImmunizations() + "\n") : "")  + immunization);
	}
	public void addVitalSign(VitalSign vitalSign) {
		if (this.getVitalSigns() == null) {
			this.setVitalSigns(new ArrayList<VitalSign>());
		}
		this.getVitalSigns().add(vitalSign);
	}
	public void addInvestigationTest(InvestigationTest investigationTest) {
		if (this.getInvestigationTests() == null) {
			this.setInvestigationTests(new ArrayList<InvestigationTest>());
		}
		this.getInvestigationTests().add(investigationTest);
	}
	
	public void addSystemReviewQuestionId(Long systemReviewQuestionId) {
		if (selectedSystemReviewQuestionIds == null) {
			this.selectedSystemReviewQuestionIds = new HashSet<>();
		}
		this.selectedSystemReviewQuestionIds.add(systemReviewQuestionId);
	}
	
	public void addPhysicalExamSystemId(Long physicalExamSystemId) {
		if (selectedPhysicalExamSystemIds == null) {
			this.selectedPhysicalExamSystemIds = new HashSet<>();
		}
		this.selectedPhysicalExamSystemIds.add(physicalExamSystemId);
	}
	
	public PhysicalExam derivePhysicalExam() {
		PhysicalExam physicalExam = new PhysicalExam();
		physicalExam.setAdmission(this.getAdmission());
		physicalExam.setVisit(this.getVisit());
		physicalExam.setSelectedPhysicalExamSystems(this.getSelectedPhysicalExamSystemIds());
		physicalExam.setSummary(this);
		physicalExam.setAuthor(author);
		return physicalExam;
	}
	
	public SystemReview deriveSystemReview() {
		SystemReview systemReview = new SystemReview();
		systemReview.setAdmission(this.getAdmission());
		systemReview.setVisit(this.getVisit());
		systemReview.setSelectedSystemReviewQuestions(this.getSelectedSystemReviewQuestionIds());
		systemReview.setSummary(this);
		systemReview.setAuthor(author);
		return systemReview;
	}
}
