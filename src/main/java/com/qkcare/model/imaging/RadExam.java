package com.qkcare.model.imaging;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qkcare.model.BaseEntity;

@Entity
@Table(name = "RAD_EXAM")
public class RadExam extends BaseEntity {
	
	@Id
	@GeneratedValue
	@Column(name = "EXAM_ID")
	private Long id;	
	@ManyToOne
	@JoinColumn(name = "MODALITY_ID")
	private Modality modality;
	private String name;
	private String description;
	private Double rate;
	private int status;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Modality getModality() {
		return modality;
	}
	public void setModality(Modality modality) {
		this.modality = modality;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	// Transient
	public String getModalityName() {
		return this.getModality().getName();
	}
	
	public String getStatusDesc() {
		return status == 0 ? "Active" : "Inactive";
	}
}
