package com.qkcare.model;

import java.util.List;

public class SearchAttribute {
	private List<String> parameters;
	private String orderBy;
	public List<String> getParameters() {
		return parameters;
	}
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}
