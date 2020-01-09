package com.qkcare.domain;

import java.io.Serializable;
import java.util.List;

public class GenericVO implements Serializable {
	
	private Long id;
	private String name;

	List<GenericVO> childs;
	
	public GenericVO() {}
	
	public GenericVO(Long id, String name) {
		this.id = id;
		this.name = name;
	}

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

	public List<GenericVO> getChilds() {
		return childs;
	}

	public void setChilds(List<GenericVO> childs) {
		this.childs = childs;
	}
	
	public boolean equals(Object o) {
		if (o == null) return false;
		
		return this.id.intValue() == ((GenericVO)o).getId().intValue();
	}
	
	@Override
    public int hashCode()
    {
		return this.id.intValue();
    }
    
}
