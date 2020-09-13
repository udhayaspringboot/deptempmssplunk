package com.employeeservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employeeservice.AemployeeserviceApplication;
import com.employeeservice.model.Employee;
import com.employeeservice.model.Employees;
import com.employeeservice.service.EmployeeService;
import com.splunk.Args;
import com.splunk.Receiver;
import com.splunk.Service;

@RestController
@RequestMapping("employee")
public class EmployeeController {
	
	@Autowired
	EmployeeService empServ;
	
	 Map<String,Object> connection =AemployeeserviceApplication.splunkConnect();
	 Service   service = Service.connect(connection);
	 Receiver receiver = service.getReceiver();
	 
		 Args logArgs=new Args();
	
	
	
	@PostMapping("/saveEmp/{deptEmpFk}")
	
	public Employee saveEmp(@RequestBody Employee emp,@PathVariable int deptEmpFk)
	{
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Employee Controller......................");
		
		
		emp.setDeptEmpFk(deptEmpFk);
		empServ.createEmpServ(emp);
		receiver.log("main", logArgs, "Calling employee save service method");
		return empServ.createEmpServ(emp);
	}
	@GetMapping("/listEmp/{deptEmpFk}")
	public Employees getAllEmp(@PathVariable int deptEmpFk)
	{
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Employee Controller......................");
		
		receiver.log("main", logArgs, "Calling employee list  service method");
		List<Employee> lisEmp = (List<Employee>) empServ.readEmpFromDeptServ(deptEmpFk);
		
		Employees emp =new Employees();
		emp.setEmployees(lisEmp);
		
		return emp;
	}
	
	@PutMapping("/updateEmp/{empId}")
	public boolean updateEmp(@RequestBody Employee emp,@PathVariable int empId)
	{
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Employee Controller......................");
		
		
		emp.setEmpId(empId);
		empServ.updateEmpServ(emp);
		receiver.log("main", logArgs, "Calling employee update  service method");
		return true;
	}
	
	@DeleteMapping("/deleteEmp/{empId}/{deptEmpFk}")
	public boolean deleteEmp(@PathVariable int empId,@PathVariable int deptEmpFk)
	{
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Employee Controller......................");
		
		receiver.log("main", logArgs, "Calling employee delete  service method");
		empServ.deleteEmpFromDeptServ(empId, deptEmpFk);
		
		return true;
	}
	
	@DeleteMapping("/deleteEmp/{deptEmpFk}")
	public boolean deleteEmps(@PathVariable int deptEmpFk)
	{
		
		empServ.deleteEmpsFromDeptServ( deptEmpFk);
		
		return true;
	}
}
