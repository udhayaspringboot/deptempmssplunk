package com.deptempclient.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Employee {
	private int empId;
	@Size(min=3,max=12,message="atleast 3 char req")
	@NotNull
	private String empName;
	@NotNull
	@Size(min=4,message="Enter dob ")
	private String dateOfBirth;
	@Email
	  @Pattern(regexp=".+@.+\\..+", message="Enter The correct email")
	private String mailId;
	@Min(111111111L)
	@NotNull(message="mob no req")
	private long mobileNo;
	
	@Min(value=4,message="min 4 digit")
	private float salary;
	@NotNull
	@Size(min=2,max=10,message="atleast 2 char req")
	private String companyName;

	private int deptEmpFk;

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getDeptEmpFk() {
		return deptEmpFk;
	}

	public void setDeptEmpFk(int deptEmpFk) {
		this.deptEmpFk = deptEmpFk;
	}

	public Employee(int empId, String empName, String dateOfBirth, String mailId, long mobileNo, float salary,
			String companyName, int deptEmpFk) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.dateOfBirth = dateOfBirth;
		this.mailId = mailId;
		this.mobileNo = mobileNo;
		this.salary = salary;
		this.companyName = companyName;
		this.deptEmpFk = deptEmpFk;
	}

	public Employee() {
		super();
	}
	
	

}
