package com.qkcare.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private String label;
	private List<String> routerLink;
	private String icon;
	private Integer mIOrder;
	
	List<MenuVO> items;
	
	public MenuVO() {}
	
	public MenuVO(Long id, String label, List<String> routerLink, String icon, Integer miOrder) {
		this.id = id;
		this.label = label;
		this.routerLink = routerLink;
		this.icon = icon;
		this.mIOrder = miOrder;
	}
	
	public void addItem(MenuVO menu) {
		if (this.items == null) {
			this.items = new ArrayList<>();
		}
		this.items.add(menu);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public List<String> getRouterLink() {
		return routerLink;
	}

	public void setRouterLink(List<String> routerLink) {
		this.routerLink = routerLink;
	}

	public Integer getmIOrder() {
		return mIOrder;
	}

	public void setmIOrder(Integer mIOrder) {
		this.mIOrder = mIOrder;
	}

	public List<MenuVO> getItems() {
		return items;
	}

	public void setItems(List<MenuVO> items) {
		this.items = items;
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
