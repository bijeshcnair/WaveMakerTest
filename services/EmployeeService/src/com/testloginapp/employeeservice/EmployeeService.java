/*Copyright (c) 2017-2018 vanenburgsoftware.com All Rights Reserved.
 This software is the confidential and proprietary information of vanenburgsoftware.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with vanenburgsoftware.com*/
package com.testloginapp.employeeservice;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;

/*Copyright (c) 2017-2018 vanenburgsoftware.com All Rights Reserved.
This software is the confidential and proprietary information of vanenburgsoftware.com You shall not disclose such Confidential Information and shall use it only in accordance
with the terms of the source code license agreement you entered into with vanenburgsoftware.com*/


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wavemaker.runtime.WMAppContext; // used to load beans from app context
import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;
import com.zaxxer.hikari.HikariConfig;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;


//import com.testloginapp.employeeservice.model.*;

/**
 * This is a singleton class with all its public methods exposed as REST APIs via generated controller class.
 * To avoid exposing an API for a particular public method, annotate it with @HideFromClient.
 *
 * Method names will play a major role in defining the Http Method for the generated APIs. For example, a method name
 * that starts with delete/remove, will make the API exposed as Http Method "DELETE".
 *
 * Method Parameters of type primitives (including java.lang.String) will be exposed as Query Parameters &
 * Complex Types/Objects will become part of the Request body in the generated API.
 */
@ExposeToClient
public class EmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private SecurityService securityService;

	/**
	 * This is sample java operation that accepts an input from the caller and responds with "Hello".
	 *
	 * SecurityService that is Autowired will provide access to the security context of the caller. It has methods like isAuthenticated(),
	 * getUserName() and getUserId() etc which returns the information based on the caller context.
	 *
	 * Methods in this class can declare HttpServletRequest, HttpServletResponse as input parameters to access the
	 * caller's request/response objects respectively. These parameters will be injected when request is made (during API invocation).
	 */
	public String sampleJavaOperation(String name, HttpServletRequest request) {
		logger.debug("Starting sample operation with request url " + request.getRequestURL().toString());

		String result = null;
		if (securityService.isAuthenticated()) {
			result = "Hello " + name + ", You are logged in as "+  securityService.getLoggedInUser().getUserName();
		} else {
			result = "Hello " + name + ", You are not authenticated yet!";
		}
		logger.debug("Returning {}", result);
		return result;
	}

	public  void generatePdfReport(String jrxml, String database,HttpServletResponse response)
	{
		try
		{
			//Fetching database connection from spring bean

			HikariConfig ds = WMAppContext.getInstance().getSpringBean("TestDBHikariConfig");
			
			Connection conn = ds.getDataSource().getConnection(); // get connected to database
			
			
			//Opening jrxml input stream file from class loader
			InputStream jrxmlInput = getClass().getClassLoader().getResource(jrxml).openStream();

			// loads the jrxml file
			JasperDesign design = JRXmlLoader.load(jrxmlInput); 

			//Compiling jrxml file 
			JasperReport jasperReport = JasperCompileManager.compileReport(design); 

			//Print jasper report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), conn);
			logger.info("JasperPrint" + jasperPrint);

			//Export report to pdf format
			JRPdfExporter pdfExporter = new JRPdfExporter();
			pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
			pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
			pdfExporter.exportReport();

			//Setting response header
			response.setContentType("application/pdf");
			response.setHeader("Content-Length", String.valueOf(pdfReportStream.size()));
			response.addHeader("Content-Disposition", "inline; filename=jasper.pdf;");

			//Closing stream
			OutputStream responseOutputStream = response.getOutputStream();
			responseOutputStream.write(pdfReportStream.toByteArray());
			responseOutputStream.close();
			pdfReportStream.close();

			//closing database connection
			conn.close();
			logger.info("Completed Successfully: "); // logger will log the error into the studio logs
		}catch (Exception e)
		{
			logger.info("Error: ", e);
		}

	}

}