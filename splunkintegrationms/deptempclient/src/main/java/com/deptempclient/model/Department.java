package com.deptempclient.model;

import javax.validation.constraints.Size;

public class Department {
	
	private int deptId;
	@Size(min=2,max=12,message="atleat 2 char req")
	private String deptName;
	@Size(min=2,max=10,message="atleasr 2 char req")
	private String deptLoc;
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptLoc() {
		return deptLoc;
	}
	public void setDeptLoc(String deptLoc) {
		this.deptLoc = deptLoc;
	}
	public Department(int deptId, String deptName, String deptLoc) {
		super();
		this.deptId = deptId;
		this.deptName = deptName;
		this.deptLoc = deptLoc;
	}
	public Department() {
		super();
	}
	
	

}
