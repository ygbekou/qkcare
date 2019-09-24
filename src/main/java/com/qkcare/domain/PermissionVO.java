package com.qkcare.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PermissionVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String canAdd;
	private String canEdit;
	private String canView;
	private String canDelete;

	
	public PermissionVO() {}
	
	public PermissionVO(Long id, String name, String canAdd, String canEdit, String canView, String canDelete) {
		this.id = id;
		this.name = name;
		this.canAdd = canAdd;
		this.canEdit = canEdit;
		this.canView = canView;
		this.canDelete = canDelete;
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

	public String getCanAdd() {
		return canAdd;
	}

	public void setCanAdd(String canAdd) {
		this.canAdd = canAdd;
	}

	public String getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(String canEdit) {
		this.canEdit = canEdit;
	}

	public String getCanView() {
		return canView;
	}

	public void setCanView(String canView) {
		this.canView = canView;
	}

	public String getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(String canDelete) {
		this.canDelete = canDelete;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean equals(GenericVO o) {
		if (o == null) return false;
		
		return this.id.intValue() == o.getId().intValue();
	}
	
	@Override
    public int hashCode()
    {
		return this.id.intValue();
    }
    
}
