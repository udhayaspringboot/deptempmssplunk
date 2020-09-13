package com.departmentservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.splunk.Application;
import com.splunk.Args;
import com.splunk.HttpService;
import com.splunk.Receiver;
import com.splunk.SSLSecurityProtocol;
import com.splunk.Service;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class AdepartmentserviceApplication {

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}
	public static void main(String[] args) {
		SpringApplication.run(AdepartmentserviceApplication.class, args);
		
		splunkConnect();
		getConnection();
	}
	public static Map<String,Object> splunkConnect()
	{
		HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
        // Create a map of arguments and add login parameters
		
		Map<String,Object> connection = new HashMap<>();
		connection.put("username", "udhayasplunk");
		connection.put("password", "Qwerty123#");
		connection.put("host", "LAPTOP-1NN8E0HU");
		connection.put("port", 8089);
		
		return connection;
        
       
       
	}
	
	public static void getConnection()
	{
		try
		{
		Map<String,Object> connection =splunkConnect();
		 Service service = Service.connect(connection); 
	        
	        for (Application app : service.getApplications().values()) {
	            System.out.println(app.getName());
	        }
	        
	     Receiver receiver=service.getReceiver();
	        Args logArgs=new Args();
	       logArgs.put("sourcetype", "Test_splunk");
	  
	       //receiver.log("main", logArgs, "splunk integration");
	        receiver.log("main", logArgs, "department microservice application is running");
	       // receiver.
		}
		catch(Exception ex)
		{
			 //receiver.w("main", logArgs, "department microservice application is running");
		}
	}
	
}
