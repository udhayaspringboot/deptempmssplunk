package com.employeeservice.dao;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.employeeservice.AemployeeserviceApplication;
import com.employeeservice.model.Employee;
import com.splunk.Args;
import com.splunk.Receiver;
import com.splunk.Service;
@Repository
@Transactional
public class EmployeeDaoImpl implements EmployeeDao {

	@Autowired
	EmployeeRepo empRepo;
	
	 Map<String,Object> connection =AemployeeserviceApplication.splunkConnect();
	 Service   service = Service.connect(connection);
	 Receiver receiver = service.getReceiver();
	 
		 Args logArgs=new Args();
	
	@Override
	public Employee createEmp(Employee employee) {
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Employee Dao......................");
		
		receiver.log("main", logArgs, "Employee saved successfully");
		return empRepo.save(employee);
	}

	@Override
	public boolean updateEmp(Employee employee) {
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Employee dao......................");
		
		
		empRepo.save(employee);
		receiver.log("main", logArgs, "employee updated successfully");
		return true;
	}

	@Override
	public List<Employee> readEmpFromDept(int deptEmpFk) {
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Employee dao......................");
		
		
		List<Employee> lisEmp = (List<Employee>) empRepo.findByDeptEmpFk(deptEmpFk);
		receiver.log("main", logArgs, "employee listed successfullly");
		return lisEmp;
	}

	@Override
	public boolean deleteEmpFromDept(int empId, int deptEmpFk) {
		System.out.println("employee id "+empId + "dept id  "+deptEmpFk);
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From Employee Dao......................");
		
		
		empRepo.deleteByEmpIdAndDeptEmpFk(empId,deptEmpFk);
		receiver.log("main", logArgs, "Employee deleted successfully");
		return true;
	}

	@Override
	public boolean deleteEmpsFromDept(int deptEmpFk) {
		
		empRepo.deleteByDeptEmpFk(deptEmpFk);
		
		return false;
	}

}
