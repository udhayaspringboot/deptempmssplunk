package com.departmentservice.dao;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.departmentservice.AdepartmentserviceApplication;
import com.departmentservice.model.Department;
import com.splunk.Args;
import com.splunk.Receiver;
import com.splunk.Service;
@Repository
@Transactional
public class DepartmentDaoImpl implements DepartmentDao {

	@Autowired
	DepartmentRepo deptRepo;
	
	Map<String,Object> connection =AdepartmentserviceApplication.splunkConnect();
	 Service   service = Service.connect(connection);
	 Receiver receiver = service.getReceiver();
	 Args logArgs=new Args();
	 
	
	@Override
	public Department createDept(Department dept) {
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From department dao......................");
		
		receiver.log("main", logArgs, "department saved successfully");
		return deptRepo.save(dept);
	}

	@Override
	public boolean updateDept(Department dept) {
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From department dao......................");
		
		receiver.log("main", logArgs, "updated department successfully");
		deptRepo.save(dept);
		return true;
	}

	@Override
	public List<Department> readAllDept() {
		List<Department> lisDept = (List<Department>) deptRepo.findAll();
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From department dao......................");
		
		receiver.log("main", logArgs, "listed department successfully");
		return lisDept;
	}

	@Override
	public boolean delteDept(int deptId) {
		deptRepo.deleteByDeptId(deptId);
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From department dao......................");
		
		receiver.log("main", logArgs, "department deleted successfully");
		return false;
	}

}
