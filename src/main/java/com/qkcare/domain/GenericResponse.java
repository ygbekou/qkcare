package com.qkcare.domain;

public class GenericResponse {
	
	private String result;
	
	public GenericResponse() {}
	
	public GenericResponse(String result) {
		this.result = result;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}