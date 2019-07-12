package com.qkcare.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WEEK_DAY")
public class Weekday extends BaseEntity {
	
	@Id
	@Column(name ="WEEK_DAY_ID")
	private Long id;
	
	@Column(name ="NAME")
	private String name;
	private String language;

	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Weekday(){}
	public Weekday(String name) {
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Weekday [name=" + name + "]";
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return id.intValue();
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

}
