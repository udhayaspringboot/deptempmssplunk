package com.employeeservice.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;import com.employeeservice.AemployeeserviceApplication;
import com.employeeservice.dao.EmployeeDao;
import com.employeeservice.model.Employee;
import com.splunk.Args;
import com.splunk.Receiver;
import com.splunk.Service;
@org.springframework.stereotype.Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeDao empDao;
	
	 Map<String,Object> connection =AemployeeserviceApplication.splunkConnect();
	 Service   service = Service.connect(connection);
	 Receiver receiver = service.getReceiver();
	 
		 Args logArgs=new Args();
	
	
	@Override
	public Employee createEmpServ(Employee employee) {
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Employee Service......................");
		
		receiver.log("main", logArgs, "Calling employee save  dao method");
		return empDao.createEmp(employee);
	}

	@Override
	public boolean updateEmpServ(Employee employee) {
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Employee Service......................");
		
		receiver.log("main", logArgs, "Calling employee update  dao method");
		empDao.updateEmp(employee);
		return true;
	}

	@Override
	public List<Employee> readEmpFromDeptServ(int empId) {
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Employee service......................");
		
		receiver.log("main", logArgs, "Calling employee list  dao method");
		List<Employee> lisEmp = empDao.readEmpFromDept(empId);
		return lisEmp;
	}

	@Override
	public boolean deleteEmpFromDeptServ(int deptId, int empId) {
		
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Employee service......................");
		
		receiver.log("main", logArgs, "Calling employee delete  dao method");
		empDao.deleteEmpFromDept(deptId, empId);
		return true;
	}

	@Override
	public boolean deleteEmpsFromDeptServ(int deptEmpFk) {
		empDao.deleteEmpsFromDept(deptEmpFk);
		return true;
	}

}
