package com.departmentservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;


import com.departmentservice.AdepartmentserviceApplication;
import com.departmentservice.dao.DepartmentDao;
import com.departmentservice.model.Department;
import com.splunk.Args;
import com.splunk.Receiver;
import com.splunk.Service;
@org.springframework.stereotype.Service

public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	DepartmentDao deptDao;
	
	Map<String,Object> connection =AdepartmentserviceApplication.splunkConnect();
	 Service   service = Service.connect(connection);
	 Receiver receiver = service.getReceiver();
	 Args logArgs=new Args();
	@Override
	public Department createDeptServ(Department dept) {
		
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From department service......................");
		
		receiver.log("main", logArgs, "Calling department save  dao method");
	return deptDao.createDept(dept);
		
	}

	@Override
	public boolean updateDeptServ(Department dept) {
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From department service......................");
		
		receiver.log("main", logArgs, "Calling department update  dao method");
		deptDao.updateDept(dept);
		return true;
	}

	@Override
	public List<Department> readAllDeptServ() {
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From department service......................");
		
		receiver.log("main", logArgs, "Calling department list  dao method");
		return deptDao.readAllDept();
	}

	@Override
	public boolean delteDeptServ(int depIds) {
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, " ..............From department service......................");
		
		receiver.log("main", logArgs, "Calling department delete  dao method");
		deptDao.delteDept(depIds);
		return true;
	}

}
