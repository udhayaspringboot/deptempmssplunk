package com.deptempclient.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.deptempclient.DeptempclientApplication;
import com.deptempclient.model.Department;
import com.deptempclient.model.Departments;
import com.deptempclient.model.Employee;
import com.deptempclient.model.Employees;

import com.splunk.Args;
import com.splunk.Receiver;
import com.splunk.Service;


@RestController
public class DeptEmpController {

	@Autowired
	RestTemplate restTemplate;

	//Logger log = LoggerFactory.getLogger(DeptEmpController.class);
	// UserDetailss useDet;
		
	 Map<String,Object> connection =DeptempclientApplication.splunkConnect();
	 Service   service = Service.connect(connection);
	 Receiver receiver = service.getReceiver();
	 
		 Args logArgs=new Args();
		 
		 

	@RequestMapping("/")
	public ModelAndView chd() {
		
		return new ModelAndView("redirect:/homeserv");// redirect:/homeserv
	}

	/*
	 * @RequestMapping("/login")
	 * 
	 * public ModelAndView startCont() {
	 * 
	 * log.info("login page");
	 * 
	 * 
	 * return new ModelAndView("login"); }
	 * 
	 * @RequestMapping("/usercheck") public ModelAndView
	 * loginCheck(HttpServletRequest request,HttpServletResponse response) {
	 * System.out.println("usercheck");
	 * 
	 * String uname=request.getParameter("userName"); String pass =
	 * request.getParameter("password"); System.out.println("uname "+uname);
	 * System.out.println("usercheck"+uname); userDetServ.loadUserByUsername(uname);
	 * ModelAndView mdv = new ModelAndView("login");
	 * if(uname.equalsIgnoreCase("admin") && pass.equalsIgnoreCase("admin")) {
	 * HttpSession sess = request.getSession(); sess.setAttribute("unam", "admin");
	 * return new ModelAndView("redirect:/homeserv");
	 * 
	 * 
	 * }else { String message = "UserName and Password didnt match";
	 * request.getSession().setAttribute("message", message); return mdv; }
	 * 
	 * }
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping("/homeserv")
	public ModelAndView homePage(@ModelAttribute("deptpage") Department det, HttpServletRequest request,
			HttpServletResponse response, /* Pageable pageable */@RequestParam(required = false) Integer page) {
		// DeptEmpService dede = new DeptEmpServImpl();
		HttpSession sess = request.getSession();
		logArgs.put("sourcetype", "Test_splunk");
		receiver.log("main", logArgs, "Entering homeserv");
		
		Departments departments = restTemplate.getForObject("http://gateway-service/department/listDept",
				Departments.class);

		System.out.println("departments " + departments.getDepartments().get(0).getDeptName());

		List<Department> ldeptj = new ArrayList<>();

		for (int i = 0; i < departments.getDepartments().size(); i++) {

			ldeptj.add(departments.getDepartments().get(i));
		}

		int size = ldeptj.size();

		// System.out.println("lang code "+cLoc.getLanguage());
		// System.out.println("lang name"+cLoc.getDisplayLanguage());

		/*
		 * String uName = useDet.getUsername(); String password = useDet.getPassword();
		 * System.out.println("uName "+uName + " password"+ password +"in controller");
		 */
		// List<Employee> lemps = dede.readAllEmp();
		// request.setAttribute("empall", lemps);
		ModelAndView mdc = new ModelAndView("home3");

		System.out.println("page val " + page);

		sess.setAttribute("pageval", page);

		PagedListHolder<Department> pagedListHolder = new PagedListHolder<Department>(ldeptj);

		pagedListHolder.setPageSize(3);
		
		receiver.log("main", logArgs, "pagination for department");
		System.out.println("page count" + pagedListHolder.getPageCount());

		System.out.println(".......................About page Holder....................");

		System.out.println("getFirstElementOnPage " + pagedListHolder.getFirstElementOnPage());
		System.out.println("getFirstLinkedPage" + pagedListHolder.getFirstLinkedPage());
		System.out.println("getLastElementOnPage" + pagedListHolder.getLastElementOnPage());
		System.out.println("getLastLinkedPage " + pagedListHolder.getLastLinkedPage());
		System.out.println("getMaxLinkedPages" + pagedListHolder.getMaxLinkedPages());
		System.out.println("getNrOfElements" + pagedListHolder.getNrOfElements());
		System.out.println("getPage " + pagedListHolder.getPage());
		System.out.println("getPageCount" + pagedListHolder.getPageCount());
		System.out.println("getPageSize" + pagedListHolder.getPageSize());

		mdc.addObject("maxPages", pagedListHolder.getPageCount());
		// System.out.println("list of elements on this page " +page+"is :
		// "+pagedListHolder.get);
		// page=null;
		if (page == null || page < 1 || page > pagedListHolder.getPageCount())

			page = 1;

		mdc.addObject("page", page);
		
		if (page == null || page < 1 || page > pagedListHolder.getPageCount())

		{
			pagedListHolder.setPage(0);
			
			mdc.addObject("deptlv", pagedListHolder.getPageList());
			
		} else if (page <= pagedListHolder.getPageCount()) {
			
			pagedListHolder.setPage(page - 1);
			
			mdc.addObject("deptlv", pagedListHolder.getPageList());
		}

		/*
		 * int x =3; log.info("excecuting pagination"); mdc.addObject("number",
		 * pages.getNumber()); mdc.addObject("totalPages", pages.getTotalPages());
		 * mdc.addObject("totalElements", pages.getTotalElements());
		 * mdc.addObject("size", x); mdc.addObject("deptlv",pages.getContent());
		 */
		sess.setAttribute("ldeptj", ldeptj);

		// mdc.addObject("loggedInUser",loggedInUser );
		mdc.addObject("size", size);
		// mdc.addObject("deptlv", ldeptj);
		mdc.addObject("hoser", "hseval");
		return mdc;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/regDept")
	public ModelAndView addDepartment(@ModelAttribute("deptpage") Department dept, HttpServletRequest request,
			HttpServletResponse response/* Pageable pageable */) {

		HttpSession sed = request.getSession();

		receiver.log("main", logArgs, "Adding department");
		
		List<Department> ldepty = (List<Department>) sed.getAttribute("ldeptj");
		// Page<Department> pages =deptEmpService.readAllDeptPage(pageable);

		//log.info("Getting list of departments");
		ModelAndView mvn = new ModelAndView("home3");
		//Integer page = (Integer) sed.getAttribute("pageval");
		
		PagedListHolder<Department> pagedListHolder = new PagedListHolder<Department>(ldepty);
		pagedListHolder.setPageSize(3);
		Integer page = pagedListHolder.getPageCount();
		mvn.addObject("maxPages", pagedListHolder.getPageCount());

		
		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		mvn.addObject("page", page);
		if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(0);
			mvn.addObject("deptlv", pagedListHolder.getPageList());
		} else if (page <= pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(page - 1);
			mvn.addObject("deptlv", pagedListHolder.getPageList());
		}

		/*
		 * mvn.addObject("number", pages.getNumber()); mvn.addObject("totalPages",
		 * pages.getTotalPages()); mvn.addObject("totalElements",
		 * pages.getTotalElements()); mvn.addObject("size", 3);
		 * mvn.addObject("deptlv",pages.getContent());
		 */

		mvn.addObject("loggedInUser", sed.getAttribute("loggedInUser"));
		mvn.addObject("adddept", "regdept");

		// mvn.addObject("deptlv", ldepty);
		mvn.addObject("hoser", "hseval");
		request.setAttribute("deptva", 0);
		return mvn;

	}

	@SuppressWarnings("unchecked")
	@PostMapping("/savedept")
	public ModelAndView saveDept(@Valid @ModelAttribute("deptpage") Department dept, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession sez = request.getSession();
		logArgs.put("sourcetype", "Test_splunk");
		
		List<Department> lisDept = (List<Department>) sez.getAttribute("ldeptj");
		Integer page = (Integer) sez.getAttribute("pageval");
		PagedListHolder<Department> pagedListHolder = new PagedListHolder<Department>(lisDept);
		pagedListHolder.setPageSize(3);
		if (errors.hasErrors()) {
			ModelAndView mvs = new ModelAndView("home3");
			receiver.log("main",logArgs,"validation error for department save");
			mvs.addObject("maxPages", pagedListHolder.getPageCount());

			if (page == null || page < 1 || page > pagedListHolder.getPageCount())
				page = 1;

			mvs.addObject("page", page);
			if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
				pagedListHolder.setPage(0);
				mvs.addObject("deptlv", pagedListHolder.getPageList());
			} else if (page <= pagedListHolder.getPageCount()) {
				pagedListHolder.setPage(page - 1);
				mvs.addObject("deptlv", pagedListHolder.getPageList());
			}

			mvs.addObject("loggedInUser", sez.getAttribute("loggedInUser"));
			mvs.addObject("adddept", "regdept");
			// mvs.addObject("deptlv", sez.getAttribute("ldeptj"));
			mvs.addObject("hoser", "hseval");
			return mvs;
		} else {

			Department bool = restTemplate.postForObject("http://gateway-service/department/saveDept", dept,
					Department.class);
			HttpSession sem = request.getSession();
			receiver.log("main",logArgs,"department saved successfully");
			sem.setAttribute("submitDoneDept", "done");
			int pagecot = pagedListHolder.getPageCount();

			/*
			 * if (page != null) { if (page == pagecot) { return new
			 * ModelAndView("redirect:homeserv?page=" + (page + 1)); } else { return new
			 * ModelAndView("redirect:homeserv?page=" + pagecot); } } return new
			 * ModelAndView("redirect:homeserv");
			 */
			if((pagedListHolder.getNrOfElements()%3)==0)
			{
				System.out.println("new page created");
				return new ModelAndView("redirect:homeserv?page=" + (pagecot+1));
			}
			return new ModelAndView("redirect:homeserv?page=" + pagecot);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/editdepartment")
	public ModelAndView editDepartment(@ModelAttribute("deptpage") Department dept, @RequestParam("depId") int deptId,
			HttpServletRequest request, HttpServletResponse response) {
		logArgs.put("sourcetype", "Test_splunk");
		// int deptId= Integer.parseInt(request.getParameter("depId"));
		HttpSession cvb = request.getSession();
		
		receiver.log("main",logArgs,"Editing department");
		// page=2;
		Integer page = (Integer) cvb.getAttribute("pageval");
		System.out.println("page is " + page);
		List<Department> ldpl = (List<Department>) cvb.getAttribute("ldeptj");

		// Department dt =deptEmpService.showDeptServ(deptId);
		ModelAndView mch = new ModelAndView("home3");
		PagedListHolder<Department> pagedListHolder = new PagedListHolder<Department>(ldpl);
		pagedListHolder.setPageSize(3);
		receiver.log("main",logArgs,"pagination for edit department");
		mch.addObject("maxPages", pagedListHolder.getPageCount());

		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		mch.addObject("page", page);
		if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(0);
			mch.addObject("deptlv", pagedListHolder.getPageList());
		} else if (page <= pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(page - 1);
			mch.addObject("deptlv", pagedListHolder.getPageList());
		}

		// request.setAttribute("deptvalid", "editdept");

		mch.addObject("loggedInUser", cvb.getAttribute("loggedInUser"));
		mch.addObject("deptva", deptId);
		mch.addObject("hoser", "hseval");
		mch.addObject("page", page);
		// mch.addObject("deptlv", ldpl);
		cvb.setAttribute("sdt", deptId);
		return mch;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/updatedept/{deptId}")
	public ModelAndView updateDepartment(@PathVariable("deptId") int deptId,
			@ModelAttribute("deptpage") Department dept, HttpServletRequest request, HttpServletResponse response) {
		logArgs.put("sourcetype", "Test_splunk");
		
		HttpSession sed = request.getSession();
		List<Department> lDep = (List<Department>) sed.getAttribute("ldeptj");
		Integer page = (Integer) sed.getAttribute("pageval");

		System.out.println("dept id" + dept.getDeptId() + "name " + dept.getDeptName() + " " + dept.getDeptLoc());
		for (Department department : lDep) {
			if (department.getDeptId() == deptId) {
				restTemplate.put("http://gateway-service/department/updateDept/" + deptId, dept);
				receiver.log("main",logArgs,"Department updated");
				HttpSession sel = request.getSession();
				sel.setAttribute("EditDept", "done");
			}
		}

		if (page != null) {
			return new ModelAndView("redirect:/homeserv?page=" + page);
		}

		return new ModelAndView("redirect:/homeserv");
	}

	@RequestMapping("/deledept")
	public ModelAndView deleteDept(@ModelAttribute("deptpage") Department dept, HttpServletRequest request,
			HttpServletResponse response) {
		logArgs.put("sourcetype", "Test_splunk");
		/*
		 * String[] arry = request.getParameterValues("ids"); List<Integer> drt = new
		 * ArrayList<>(); for (int i = 0; i < arry.length; i++) {
		 * System.out.println(arry[i]);
		 * deptEmpService.delteDeptServ(Integer.parseInt(arry[i]));
		 * drt.add(Integer.parseInt(arry[i])); }
		 */
		int deptId = Integer.parseInt(request.getParameter("deptId"));

		/*
		 * String depIds = Arrays.toString(arry); String sen = depIds.replaceAll("\\[",
		 * "").replaceAll("\\]", "").replaceAll(" ", ""); System.out.println("sen"+sen);
		 */
		// int xf = Integer.parseInt(sen);
		// System.out.println("xf value "+xf);

		restTemplate.delete("http://gateway-service/department/deleteDept/" + deptId);
		receiver.log("main",logArgs,"Department Deleted");
		// System.out.println("to string values deptIds :"+depIds);
		HttpSession sep = request.getSession();
		Integer page = (Integer) sep.getAttribute("pageval");
		
		sep.setAttribute("deleteDoneDept", "done");
		if(page != null)
		{
			return new ModelAndView("redirect:/homeserv?page="+page);
		}
		return new ModelAndView("redirect:/homeserv");

	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/home")
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) Integer page) {
		int deptId;
		try {
			HttpSession sessi = request.getSession();
			List<Department> ldept = (List<Department>) sessi.getAttribute("ldeptj");
			sessi.setAttribute("lisdept", ldept);
			
			deptId = ldept.get(0).getDeptId();

		} catch (NullPointerException ne) {
			return new ModelAndView("redirect:/login");
		}
		if (page != null) {
			return new ModelAndView("redirect:/listEmp?deptId=" + deptId + "&page=" + page);
		} else {
			return new ModelAndView("redirect:/listEmp?deptId=" + deptId);
		}

	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/listEmp")
	public ModelAndView listDepartment(@ModelAttribute("emppage") Employee emp, @RequestParam("deptId") int deptId,@RequestParam(required = false) Integer page,
			HttpServletRequest request, HttpServletResponse response) {
		logArgs.put("sourcetype", "Test_splunk");
		
		receiver.log("main",logArgs,"Entering to list employee");
		HttpSession ses = request.getSession();
		System.out.println("get");

		System.out.println("int val " + deptId);
		Employees emps = restTemplate.getForObject("http://gateway-service/department/employee/listEmp/" + deptId,
				Employees.class);

		
		receiver.log("main",logArgs,"Getting all employee list");
		List<Employee> ldeptu = new ArrayList<>();
		for (int i = 0; i < emps.getEmployees().size(); i++) {
			ldeptu.add(emps.getEmployees().get(i));
		}
		// Department det =deptEmpService.showDeptServ(xt);
		List<Department> lks = (List<Department>) ses.getAttribute("lisdept");

		ses.setAttribute("deIdFromLis", deptId);
		ses.setAttribute("emplvaldept", ldeptu);
		ses.setAttribute("lis", lks);
		ses.setAttribute("pagee", page);
		ses.setAttribute("val", ldeptu);
		if (ldeptu.isEmpty()) {
			ses.setAttribute("depIdx", deptId);
		}
		System.out.println("values from listemp : ");

		for (Employee employee : ldeptu) {
			System.out.println(employee.getEmpName());
		}
		ModelAndView mdb = new ModelAndView("home3");

		PagedListHolder<Employee> pagedListHolder = new PagedListHolder<Employee>(ldeptu);
		pagedListHolder.setPageSize(2);


		
		receiver.log("main",logArgs,"pagination for employee list");
		mdb.addObject("maxPages", pagedListHolder.getPageCount());

		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		mdb.addObject("page", page);
		if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(0);
			
			mdb.addObject("val", pagedListHolder.getPageList());
		} else if (page <= pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(page - 1);
			mdb.addObject("val", pagedListHolder.getPageList());
		}

		mdb.addObject("loggedInUser", ses.getAttribute("loggedInUser"));
       mdb.addObject("lisde", deptId);
		// mdb.addObject("val", ldeptu);
		mdb.addObject("lis", lks);

		// mdb.addObject("dval", xt);
		mdb.addObject("hom", "homep");
		// request.setAttribute("countv", sess.getAttribute("couval"));
		// request.setAttribute("mess", "no data available");
		// request.setAttribute("deptnam", arg1);

		return mdb;
	}

	@RequestMapping("/deleteemployee")
	public ModelAndView deleteEmployee(@ModelAttribute("emppage") Employee emp, HttpServletRequest request,
			HttpServletResponse response) {

		// String [] argf = request.getParameterValues("idsemp");
			logArgs.put("sourcetype", "Test_splunk");
		
		receiver.log("main",logArgs,"Entering to delete employee");
		int empId = Integer.parseInt(request.getParameter("empId"));
		int deptempid = Integer.parseInt(request.getParameter("deptId"));
		
		
		
		/*
		 * int yt=0; for (int i = 0; i < argf.length; i++) {
		 * 
		 * 
		 * System.out.println("argf values "+argf[i]); String[] x = argf[i].split(",");
		 * int xt = Integer.parseInt(x[0]); yt = Integer.parseInt(x[1]);
		 * deptEmpService.deleteEmpFromDeptServ(yt,xt);
		 * System.out.println(" x and y "+xt + " val " +yt);
		 */
		// int y = argf[i].charAt(1);
		// System.out.println("x and y value "+x+y);
		// map.put(x,y);
		// }

		// System.out.println("emp id "+empId +"deptid "+deptEmpId);

		restTemplate.delete("http://gateway-service/department/employee/deleteEmp/" + empId + "/" + deptempid);
		
         
		
		receiver.log("main",logArgs,"employee deleted");
		System.out.println("deleting at del emplo");
		HttpSession sen = request.getSession();
		sen.setAttribute("deleteDone", "done");
Integer page = (Integer) sen.getAttribute("pagee");
		
		if(page !=null)
		{
			return new ModelAndView("redirect:/listEmp?deptId=" + deptempid+"&page="+page);
		}
		// response.sendRedirect("listEmp?deptId="+deptempid);
		return new ModelAndView("redirect:/listEmp?deptId=" + deptempid);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/editemployee")
	public ModelAndView editEmployee(@ModelAttribute("emppage") Employee emp, @PathParam("empId") int empId,
			@PathParam("deptId") int deptId, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());

		// int empId=Integer.parseInt(request.getParameter("empId"));
		
		
                logArgs.put("sourcetype", "Test_splunk");
		
		receiver.log("main",logArgs,"Entering to edit employee");
		
		System.out.println("employee id at edit employee is " + empId);
		// int deptId = Integer.parseInt(request.getParameter("deptId"));
		HttpSession sek = request.getSession();
		// Employee emp = (Employee) deptEmpService.readEmployeeServ(empId);
		// Department df = deptEmpService.showDeptServ(emp.getDepartment().getDeptId());
		List<Department> ldpnt = (List<Department>) sek.getAttribute("lisdept");
		String deptName = null;
		for (Department department : ldpnt) {
			if (department.getDeptId() == deptId) {
				deptName = department.getDeptName();
			}
		}
		// System.out.println("edit page value"+emp.getEmpName());

		List<Employee> listFromDept = (List<Employee>) sek.getAttribute("emplvaldept");
		ModelAndView mcn = new ModelAndView("home3");
		PagedListHolder<Employee> pagedListHolder = new PagedListHolder<Employee>(listFromDept);
		pagedListHolder.setPageSize(2);

		Integer page = (Integer) sek.getAttribute("pagee");

		mcn.addObject("maxPages", pagedListHolder.getPageCount());

		if (page == null || page < 1 || page > pagedListHolder.getPageCount())
			page = 1;

		mcn.addObject("page", page);
		if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(0);
			mcn.addObject("val", pagedListHolder.getPageList());
		} else if (page <= pagedListHolder.getPageCount()) {
			pagedListHolder.setPage(page - 1);
			mcn.addObject("val", pagedListHolder.getPageList());
		}

		sek.setAttribute("empp", empId);
		mcn.addObject("lisde", deptId);
		mcn.addObject("loggedInUser", sek.getAttribute("loggedInUser"));
		mcn.addObject("mainemps", "checktableedit");
		mcn.addObject("empl", empId);
		mcn.addObject("hom", "homep");
		mcn.addObject("addlin", "anemp");
		mcn.addObject("lis", ldpnt);
		// mcn.addObject("val", listFromDept);
		mcn.addObject("deptName", deptName);

		return mcn;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/updateemployee/{empId}")

	public ModelAndView updateEmployee(@ModelAttribute("emppage") Employee empk, @PathVariable("empId") int empId,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
			logArgs.put("sourcetype", "Test_splunk");
		
		
		HttpSession mlk = request.getSession();

		// String idv = request.getParameter("empId");
		// int empId = (int)mlk.getAttribute("empp");
		System.out.println("id val" + empId);
		String empName = empk.getEmpName();
		System.out.println("employee Name" + empName);
		String dob = empk.getDateOfBirth();
		System.out.println("dob " + dob);
		String mailId = empk.getMailId();
		System.out.println("mail Id" + mailId);
		String depsample = request.getParameter("deptEmpName");
		List<Department> lsv = (List<Department>) mlk.getAttribute("lisdept");

		int studeptid = 0;
		for (Department department : lsv) {
			if (department.getDeptName().equals(depsample)) {
				studeptid = department.getDeptId();
			}
		}

		System.out.println("values update employee " + studeptid);
		long mob = empk.getMobileNo();
		float sal = empk.getSalary();
		String comName = empk.getCompanyName();
		Employee emp = new Employee();
		emp.setEmpId(empId);
		emp.setEmpName(empName);
		emp.setMailId(mailId);
		emp.setDateOfBirth(dob);
		emp.setDeptEmpFk(studeptid);
		emp.setMobileNo(mob);
		emp.setSalary(sal);
		emp.setCompanyName(comName);

		System.out.println("Values from update employee" + empId + " " + empName + " " + mailId + " " + dob + " "
				+ studeptid + " " + mob + " " + sal + " " + comName);

		System.out.println("values for updating");
		// System.out.println(empId+" "+empName + " "+ mailId+" "+dob+" "+studeptid);

		restTemplate.put("http://gateway-service/department/employee/updateEmp/" + emp.getEmpId(), emp);

		
		receiver.log("main",logArgs,"Employee updated successfully");
		HttpSession sea = request.getSession();
		sea.setAttribute("submitDone", "done");

		Integer page = (Integer) mlk.getAttribute("pagee");
		// response.sendRedirect("listEmp?deptId="+studeptid);
		if (page != null) {
			return new ModelAndView("redirect:/listEmp?deptId=" + studeptid + "&page=" + page);
		}
		return new ModelAndView("redirect:/listEmp?deptId=" + studeptid);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/addemployee")
	public ModelAndView addEmployee(@ModelAttribute("emppage") Employee emp, HttpServletRequest request,
			HttpServletResponse response) {
			logArgs.put("sourcetype", "Test_splunk");
		
		receiver.log("main",logArgs,"Entering to add employee");
		HttpSession sef = request.getSession();
		int deptId = (int) sef.getAttribute("deIdFromLis");
		List<Employee> lsty = (List<Employee>) sef.getAttribute("emplvaldept");

		List<Department> ldeptval = (List<Department>) sef.getAttribute("lisdept");
		String stg = request.getParameter("empId");
		System.out.println("stg in add emo" + stg);
		ModelAndView mdv = new ModelAndView("home3");

		sef.setAttribute("stg", stg);
		sef.setAttribute("deIdfrom", deptId);
		sef.setAttribute("lstyc", lsty);
		sef.setAttribute("ldvlt", ldeptval);
		if (stg == null) {

			// request.setAttribute("dept",sef.getAttribute("lisvaldept") );
			PagedListHolder<Employee> pagedListHolder = new PagedListHolder<Employee>(lsty);
			pagedListHolder.setPageSize(2);

			//Integer page = (Integer) sef.getAttribute("pagee");
			Integer page =pagedListHolder.getPageCount();
			mdv.addObject("maxPages", pagedListHolder.getPageCount());

			if (page == null || page < 1 || page > pagedListHolder.getPageCount())
				page = 1;

			mdv.addObject("page", page);
			if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
				pagedListHolder.setPage(0);
				mdv.addObject("val", pagedListHolder.getPageList());
			} else if (page <= pagedListHolder.getPageCount()) {
				pagedListHolder.setPage(page - 1);
				mdv.addObject("val", pagedListHolder.getPageList());
			}

			mdv.addObject("hom", "homep");
			mdv.addObject("valcheck", "regemployee");
			mdv.addObject("lis", ldeptval);
			mdv.addObject("depIdx", deptId);
			mdv.addObject("loggedInUser", sef.getAttribute("loggedInUser"));
			// request.setAttribute("dval",sef.getAttribute("dval"));
			mdv.addObject("empl", 0);
			// mdv.addObject("val",lsty);
			return mdv;

		} else {
			int x = Integer.parseInt(stg);
			if (x == 0) {
				mdv.addObject("hom", "homep");
				mdv.addObject("newtab", "ntabl");
				mdv.addObject("depIdx", deptId);
				mdv.addObject("loggedInUser", sef.getAttribute("loggedInUser"));
				mdv.addObject("empl", 0);
				mdv.addObject("val", lsty);
				// return mdv;
			}
			return mdv;
		}

	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/saveemployee")
	public ModelAndView saveEmployee(@Valid @ModelAttribute("emppage") Employee emp, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response)

	{
		
			logArgs.put("sourcetype", "Test_splunk");
		
		

		HttpSession scf = request.getSession();
		String stg = (String) scf.getAttribute("stg");
		int depIdx = (int) scf.getAttribute("deIdfrom");
		System.out.println("stg in saveemp" + stg + "emp val " + emp.getEmpName() + "emp val " + emp.toString());
		System.out.println("error ields " + errors.getObjectName() + errors.getErrorCount() + " " + errors.toString());
		List<Employee> litEmp = (List<Employee>) scf.getAttribute("lstyc");
		PagedListHolder<Employee> pagedListHolder = new PagedListHolder<Employee>(litEmp);
		pagedListHolder.setPageSize(2);
		if (errors.hasErrors()) {
			ModelAndView mdvl = new ModelAndView("home3");

			if (stg == null) {

				// pagedListHolder.getPageCount();
				Integer page = (Integer) scf.getAttribute("pagee");
				
				
				receiver.log("main",logArgs,"Validation error for employee");
				mdvl.addObject("maxPages", pagedListHolder.getPageCount());

				if (page == null || page < 1 || page > pagedListHolder.getPageCount())
					page = 1;

				mdvl.addObject("page", page);
				if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
					pagedListHolder.setPage(0);
					mdvl.addObject("val", pagedListHolder.getPageList());
				} else if (page <= pagedListHolder.getPageCount()) {
					pagedListHolder.setPage(page - 1);
					mdvl.addObject("val", pagedListHolder.getPageList());
				}

				// request.setAttribute("dept",sef.getAttribute("lisvaldept") );
				mdvl.addObject("hom", "homep");
				mdvl.addObject("valcheck", "regemployee");
				mdvl.addObject("lis", scf.getAttribute("ldvlt"));
				mdvl.addObject("loggedInUser", scf.getAttribute("loggedInUser"));
				System.out.println("depIdx " + depIdx);
				// request.setAttribute("dval",sef.getAttribute("dval"));
				mdvl.addObject("empl", 0);
				// mdvl.addObject("val",scf.getAttribute("lstyc"));
				return mdvl;

			} else {
				int x = Integer.parseInt(stg);
				if (x == 0) {
					ModelAndView mdvlk = new ModelAndView("home3");

					System.out.println("empid 0 executes");
					mdvlk.addObject("hom", "homep");
					mdvlk.addObject("newtab", "ntabl");
					mdvlk.addObject("depIdx", depIdx);
					System.out.println("depIdx " + depIdx);
					mdvlk.addObject("loggedInUser", scf.getAttribute("loggedInUser"));
					mdvlk.addObject("empl", 0);
					mdvlk.addObject("val", scf.getAttribute("lstyc"));

					// return mdv;
					return mdvlk;

				}

			}

		}

		Employee eml = new Employee();

		// int empId = request.getParameter("empId");
		String name = emp.getEmpName();
		String mailId = emp.getMailId();
		String dob = emp.getDateOfBirth();
		long mob = emp.getMobileNo();
		float sal = emp.getSalary();
		String comName = emp.getCompanyName();
		String deptempName = request.getParameter("deptEmpNa");
		List<Department> lsv = (List<Department>) scf.getAttribute("lisdept");

		int studeptid = 0;
		for (Department department : lsv) {
			if (department.getDeptName().equals(deptempName)) {
				studeptid = department.getDeptId();
			}
		}

		System.out.println("dept id" + studeptid);

		// eml.setEmpId(0);
		eml.setEmpName(name);
		eml.setMailId(mailId);
		eml.setDateOfBirth(dob);
		eml.setDeptEmpFk(studeptid);
		eml.setCompanyName(comName);
		eml.setMobileNo(mob);
		eml.setSalary(sal);
		System.out.println("values from save employee" + name);
		Employee empp = restTemplate.postForObject(
				"http://gateway-service/department/employee/saveEmp/" + eml.getDeptEmpFk(), eml, Employee.class);
			logArgs.put("sourcetype", "Test_splunk");
		
		receiver.log("main",logArgs,"Updated employee successfully");
		HttpSession sem = request.getSession();
		sem.setAttribute("submitDoneEmp", "done");
		//Integer page = (Integer) sem.getAttribute("pagee");

		int pagecot = pagedListHolder.getPageCount();
		System.out.println(pagedListHolder.getNrOfElements());

		// pagedListHolder.
		if ((pagedListHolder.getNrOfElements()%2)==0) {

			// page+=(pagecot - page)+1;

			/*
			 * if(page == pagecot) {
			 * 
			 * return new
			 * ModelAndView("redirect:listEmp?deptId="+studeptid+"&page="+(page+1)); }else
			 */

			return new ModelAndView("redirect:listEmp?deptId=" + studeptid + "&page=" + (pagecot+1));

		}
		return new ModelAndView("redirect:listEmp?deptId=" + studeptid+ "&page=" + pagecot);
		/*
		 * HttpSession scf = request.getSession(); ModelAndView mdv = new
		 * ModelAndView("home3"); System.out.println(emp.getEmpName());
		 * if(!(errors.hasErrors())) {System.out.println("inside "+emp.getEmpName());
		 * Employee eml = new Employee();
		 * 
		 * //int empId = request.getParameter("empId"); String name = emp.getEmpName();
		 * String mailId = emp.getMailId(); String dob = emp.getDateOfBirth(); long mob
		 * = emp.getMobileNo(); float sal =emp.getSalary(); String comName =
		 * emp.getCompanyName(); String deptempName =request.getParameter("deptEmpNa");
		 * List<Department> lsv = (List<Department>) scf.getAttribute("lisdept");
		 * 
		 * int studeptid = 0; for (Department department : lsv) {
		 * if(department.getDeptName().equals(deptempName)) { studeptid=
		 * department.getDeptId(); } }
		 * 
		 * System.out.println("dept id"+studeptid);
		 * 
		 * Department df = new Department(); df.setDeptId(studeptid); //eml.setEmpId(0);
		 * eml.setEmpName(name); eml.setMailId(mailId); eml.setDateOfBirth(dob);
		 * eml.setDepartment(df);; eml.setCompanyName(comName); eml.setMobileNo(mob);
		 * eml.setSalary(sal); System.out.println("values from save employee"+name);
		 * deptEmpService.createEmpServ(eml); HttpSession sem = request.getSession();
		 * sem.setAttribute("submitDoneEmp","done");
		 * 
		 * return new ModelAndView("redirect:listEmp?deptId="+studeptid); }
		 * 
		 * else
		 * 
		 * { String stg = (String) scf.getAttribute("stg");
		 * System.out.println("stg"+stg); System.out.println("excecuting has error");
		 * 
		 * int x = Integer.parseInt(stg);
		 * 
		 * if(x != 0) { System.out.println("excecutes ne 0");
		 * //request.setAttribute("dept",sef.getAttribute("lisvaldept") );
		 * mdv.addObject("hom", "homep"); mdv.addObject("valcheck", "regemployee");
		 * mdv.addObject("lis",scf.getAttribute("ldvlt")); mdv.addObject("loggedInUser",
		 * scf.getAttribute("loggedInUser"));
		 * //request.setAttribute("dval",sef.getAttribute("dval"));
		 * mdv.addObject("empl", 0); mdv.addObject("val",scf.getAttribute("lstyc"));
		 * return mdv;
		 * 
		 * 
		 * } else { System.out.println("excecutes eq 0"); mdv.addObject("hom", "homep");
		 * mdv.addObject("newtab", "ntabl"); mdv.addObject("depIdx",
		 * scf.getAttribute("depIdx")); mdv.addObject("loggedInUser",
		 * scf.getAttribute("loggedInUser")); mdv.addObject("empl", 0);
		 * mdv.addObject("val",scf.getAttribute("lstyc")); //return mdv; }
		 * 
		 * 
		 * }
		 * 
		 * return mdv;
		 */
	}

	@RequestMapping("/admintab")
	public ModelAndView adminTag(HttpServletRequest request, HttpServletResponse response) {
		HttpSession sacc = request.getSession();

		
		receiver.log("main",logArgs,"Entering to admin tab ");
		ModelAndView mdv = new ModelAndView("home3");
		mdv.addObject("loggedInUser", sacc.getAttribute("loggedInUser"));
		mdv.addObject("adtag", "admintag");
		return mdv;
	}

	@RequestMapping("/hrtab")
	public ModelAndView hrTab(HttpServletRequest request, HttpServletResponse response) {
		HttpSession sac = request.getSession();
			logArgs.put("sourcetype", "Test_splunk");
		
		receiver.log("main",logArgs,"Entering to hr tab employee");

		ModelAndView mdv = new ModelAndView("home3");
		mdv.addObject("loggedInUser", sac.getAttribute("loggedInUser"));
		mdv.addObject("hrtag", "Hrtag");
		return mdv;
	}

	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
logArgs.put("sourcetype", "Test_splunk");
		
		
		HttpSession sess = request.getSession();
		sess.removeAttribute("loggedInUser");

		sess.invalidate();

		
		receiver.log("main",logArgs,"logged out successfully");
		return new ModelAndView("redirect:/");
	}

}
