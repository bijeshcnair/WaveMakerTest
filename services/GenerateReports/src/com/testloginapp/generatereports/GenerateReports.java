/*Copyright (c) 2017-2018 vanenburgsoftware.com All Rights Reserved.
 This software is the confidential and proprietary information of vanenburgsoftware.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with vanenburgsoftware.com*/
package com.testloginapp.generatereports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wavemaker.runtime.security.SecurityService;
import com.wavemaker.runtime.service.annotations.ExposeToClient;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@ExposeToClient
public class GenerateReports {

   private static final Logger logger = LoggerFactory.getLogger(GenerateReports.class);

   @Autowired
   private SecurityService securityService;

   // This method generates a PDF report 
    public void generatePdfReport(String jrxml, String database, HttpServletResponse response) {
       try {
           InputStream jrxmlInput = this.getClass().getClassLoader().getResource("data.jrxml").openStream();
           JasperDesign design = JRXmlLoader.load(jrxmlInput);
           JasperReport jasperReport = JasperCompileManager.compileReport(design);
           logger.info("Report compiled");
           String data = invokeService();
           logger.info("data = "+ data);
           // It is possible to generate Jasper reports from a JSON source.
           JsonDataSource jsonDataSource = new JsonDataSource(new ByteArrayInputStream(data.getBytes()));
           JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap(), jsonDataSource);
           logger.info("JasperPrint" + jasperPrint);

           JRPdfExporter pdfExporter = new JRPdfExporter();
           pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
           ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
           pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
           pdfExporter.exportReport();

           response.setContentType("application/pdf");
           response.setHeader("Content-Length", String.valueOf(pdfReportStream.size()));
           response.addHeader("Content-Disposition", "inline; filename=jasper.pdf;");

           OutputStream responseOutputStream = response.getOutputStream();
           responseOutputStream.write(pdfReportStream.toByteArray());
           responseOutputStream.close();
           pdfReportStream.close();
           logger.info("Completed Successfully: ");
       } catch (Exception e) {
           logger.info("Error: ", e);
       }

   }

   // This method actually connects to the external web service
   private String invokeService() {
String url = "http://e1255fbaf8cb8.cloud.wavemakeronline.com/RestJasper/services/hrdb/Department?sort=location&r=json";
       String response = "";

       try {
           HttpClient httpClient = new HttpClient();
           HttpMethod httpMethod = new GetMethod(url);
           logger.info(" Invoking service : " + url);
           httpClient.executeMethod(httpMethod);
           response = httpMethod.getResponseBodyAsString();
       } catch (IOException e) {
           response = "Error encountered while invoking service at url: " + url + "Error message: " + e.toString();
           logger.error(response);
           return response;
       }
       logger.info(" Rest response : " + response);
       return response;
   }
}