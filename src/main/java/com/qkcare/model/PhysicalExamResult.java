package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PHYSICAL_EXAM_RESULT")
public class PhysicalExamResult extends BaseEntity {
	
	@Id
	@Column(name = "PHYSICAL_EXAM_RESULT_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "PHYSICAL_EXAM_ID")
	private PhysicalExam physicalExam;
	@ManyToOne
	@JoinColumn(name = "PHYSICAL_EXAM_SYSTEM_ID")
	private PhysicalExamSystem physicalExamSystem;
	
	public PhysicalExamResult() {}
	
	public PhysicalExamResult(Long physicalExamId, Long physicalExamSystemId) {
		this.physicalExam = new PhysicalExam(physicalExamId);
		this.physicalExamSystem = new PhysicalExamSystem(physicalExamSystemId);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public PhysicalExam getPhysicalExam() {
		return physicalExam;
	}
	public void setPhysicalExam(PhysicalExam physicalExam) {
		this.physicalExam = physicalExam;
	}
	public PhysicalExamSystem getPhysicalExamSystem() {
		return physicalExamSystem;
	}
	public void setPhysicalExamSystem(PhysicalExamSystem physicalExamSystem) {
		this.physicalExamSystem = physicalExamSystem;
	}
	
	
	
}
