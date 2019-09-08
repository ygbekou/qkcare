package com.qkcare.model.imaging;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qkcare.model.BaseEntity;

@Entity
@Table(name = "RAD_INVESTIGATION_COMMENT")
public class RadInvestigationComment extends BaseEntity {
	
	@Id
	@Column(name = "INVESTIGATION_COMMENT_ID")
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name = "INVESTIGATION_ID")
	private RadInvestigation investigation;
	@Column(name = "COMMENT_DATETIME")
	private Timestamp commentDatetime;
	private String title;
	@Column(name = "COMMENTS")
	private String comments;
	
	// Transient
	
	public RadInvestigationComment () {
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public RadInvestigation getInvestigation() {
		return investigation;
	}
	public void setInvestigation(RadInvestigation investigation) {
		this.investigation = investigation;
	}
	public Timestamp getCommentDatetime() {
		return commentDatetime;
	}
	public void setCommentDatetime(Timestamp commentDatetime) {
		this.commentDatetime = commentDatetime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	// Transient
}
