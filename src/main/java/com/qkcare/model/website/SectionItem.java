package com.qkcare.model.website;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qkcare.model.BaseEntity;


@Entity
@Table(name="SECTION_ITEM")
public class SectionItem extends BaseEntity {
	
	@Id
	@Column(name ="SECTION_ITEM_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "SECTION_ID")
	private Section section;
	
	private String title;
	
	private String description;
	
	@Column(name ="PICTURE")
	private String fileLocation;

	private Integer status;
	
	private String language;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getSectionName() {
		return this.getSection().getName();
	}
	
	public String getStatusDesc() {
		return status == 0 ? "Actif" : "Inactif";
	}
	
	
	// Transient
	public String getText1() {
		String[] texts = description.split("\\|");
		
		return texts.length > 0 ? texts[0] : "" ;
	}
	
	public String getText2() {
		String[] texts = description.split("\\|");
		
		return texts.length > 1 ? texts[1] : "" ;
	}
	
	public String getText3() {
		String[] texts = description.split("\\|");
		
		return texts.length > 2 ? texts[2] : "" ;
	}

}
