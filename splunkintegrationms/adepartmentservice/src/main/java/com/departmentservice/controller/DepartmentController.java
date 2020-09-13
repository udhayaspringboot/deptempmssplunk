package com.departmentservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.departmentservice.AdepartmentserviceApplication;
import com.departmentservice.model.Department;
import com.departmentservice.model.Departments;
import com.departmentservice.model.Employee;
import com.departmentservice.model.Employees;
import com.departmentservice.service.DepartmentService;
import com.departmentservice.service.EmployeeService;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.splunk.Args;
import com.splunk.Receiver;
import com.splunk.Service;
@RestController
@RequestMapping("/department")
@RefreshScope
public class DepartmentController {
	
	@Autowired
	DepartmentService departmentService;
	@Autowired
	EmployeeService employeeService;
	
	 Map<String,Object> connection =AdepartmentserviceApplication.splunkConnect();
	 Service   service = Service.connect(connection);
	 Receiver receiver = service.getReceiver();
	 Args logArgs=new Args();
	
	@Value("${message}")
	
	private String message;
	
	@PostMapping("/saveDept")
	public Department saveDept(@RequestBody Department dept)
	{
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Department Controller......................");
		
		receiver.log("main", logArgs, "Calling department save service method");
		return departmentService.createDeptServ(dept);
		
		
		
	}
	
	
	
	
	@GetMapping("/listDept")
	public Departments getAllDept()
	{logArgs.put("sourcetype", "Test_splunk");
	receiver.log("main", logArgs, " ..............From Department Controller......................");
	
	
		List<Department> lis = departmentService.readAllDeptServ();
		Departments depts = new Departments();
		depts.setDepartments(lis);
		
		receiver.log("main", logArgs, "calling department list service method ");
		System.out.println("lis vals message"+message);
		return depts;
	}
	
	@PutMapping("/updateDept/{depId}")
	public boolean updateDept(@RequestBody Department dept,@PathVariable int depId)
	{
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Department Controller......................");
		
		
		System.out.println("dept updating values"+depId + "name "+dept.getDeptName() +"loc "+dept.getDeptLoc());
		dept.setDeptId(depId);
		departmentService.updateDeptServ(dept);
		
		receiver.log("main", logArgs, "calling department update service method");
		return true;
		
	}
	
	@DeleteMapping("/deleteDept/{depIds}")
	public boolean deleteDept(@PathVariable int depIds)
	{
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Department Controller......................");
		
		receiver.log("main", logArgs, "calling  delete department service method");
		departmentService.delteDeptServ(depIds);
		employeeService.deleteEmpByDeptId(depIds);
		
		
		//receiver.log("main", logArgs, "Department associated employee also deleted Successfully");
		return true;
		
	}
	
	@GetMapping("/employee/listEmp/{deptId}")
	public Employees getEmployees(@PathVariable int deptId)
	{
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Department Controller......................");
		receiver.log("main", logArgs, " calling employee list service method");
		
		//receiver.log("main", logArgs, "Department Saved Successfully");
		Employees emp = employeeService.readEmpFromDeptServ(deptId);
		
		return emp;
		
	}
	
	@PostMapping("/employee/saveEmp/{deptEmpFk}")
	public Employee  saveEmp(@RequestBody Employee emp,@PathVariable int deptEmpFk)
	{
		List<Department> lisDept = departmentService.readAllDeptServ();
		logArgs.put("sourcetype", "Test_splunk");
		
		
		
		for (Department department : lisDept) {
			
			if(department.getDeptId() == deptEmpFk)
			{
				emp.setDeptEmpFk(deptEmpFk);
				receiver.log("main", logArgs, " ..............From Department Controller......................");
				receiver.log("main", logArgs, " calling employee save service method ");
				
				
				
				return employeeService.createEmpServ(emp);
			}
		}
		return emp;
		
	}
	
	@PutMapping("/employee/updateEmp/{empId}")
	public String updateEmp(@RequestBody Employee emp,@PathVariable int empId)
	{
		emp.setEmpId(empId);
		employeeService.updateEmpServ(emp);
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Department Controller......................");
		receiver.log("main", logArgs, " calling employee update service method");
		
		
		
		return "Employee Updated Successfully";
	}
	
	@DeleteMapping("/employee/deleteEmp/{empId}/{deptEmpFk}")
	public String deleteEmp(@PathVariable int empId,@PathVariable int deptEmpFk)
	{
		
		employeeService.deleteEmpFromDeptServ(empId, deptEmpFk);
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Department Controller......................");
		receiver.log("main", logArgs, " calling employee delete service method");
		
		
		return "Employee Deleted Successfullly";
	}
	

}
