package com.qkcare.domain;

public class GenericResponse {
	
	private String result;
	
	private String message;
	
	public GenericResponse() {}
	
	public GenericResponse(String result) {
		this.result = result;
	}
	
	public GenericResponse(String result, String message) {
		this.result = result;
		this.message = message;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
