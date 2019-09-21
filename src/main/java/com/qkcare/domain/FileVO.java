package com.qkcare.domain;

import java.io.Serializable;
import java.util.List;

public class FileVO implements Serializable {
	
	private String path;
	
	public FileVO() {}
	
	public FileVO(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean equals(FileVO o) {
		if (o == null) return false;
		
		return this.path == o.getPath();
	}
	
	@Override
    public int hashCode()
    {
		return this.getPath().hashCode();
    }
    
}
