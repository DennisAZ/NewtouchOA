/*
 * Copyright 2010-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package oa.spring.control.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.service.FileUploadService;
import oa.spring.util.ContractCont;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/fileUpload")
public class FileUploadController {
	@Autowired
	private FileUploadService uploadService;
	
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	/**
	 * Simply selects the view to render by returning its name.
	 */
	@RequestMapping(value = "fileupload")
	public  String fileUpload(HttpServletRequest request,HttpServletResponse response) {
	    PrintWriter pw = null;
	    try {
	    	request.setCharacterEncoding("UTF-8");
	    	response.setCharacterEncoding("UTF-8");
	    	
	    	FileItemFactory factory = new DiskFileItemFactory();
	    	ServletFileUpload upload = new ServletFileUpload(factory);
	    	upload.setHeaderEncoding("UTF-8");
	    	List items = upload.parseRequest(request);
	    	
	      StringBuffer sb = uploadService.uploadMsrg2Json(items);
	      String data = "{'state':'0','data':" + sb.toString() + "}";
	      pw = response.getWriter();
	      pw.println(data.trim());
	      pw.flush();
	    }catch(Exception e){
	      try {
			pw = response.getWriter();
			pw.println("{'state':'1'}".trim());
			pw.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    } finally {
	      pw.close();
	    }
	    return null;
	}
	
	@RequestMapping(value="deleteFile",method=RequestMethod.POST)
	public @ResponseBody String deleteFile(HttpServletRequest request,HttpServletResponse response) {
		String attachmentName = request.getParameter("attachmentName");
		String attachmentId = request.getParameter("attachmentId");
		if(!"".equals(attachmentId) && attachmentId!=null){
			String folder = attachmentId.split("_")[0];
			String fileName = attachmentId.split("_")[1]+attachmentName.substring(attachmentName.lastIndexOf("."));
			String module = request.getParameter("module");
			String filePath = ContractCont.UPLOAD_HOME+File.separator+module+File.separator+folder+File.separator+fileName;
			File file = new File(filePath);
			if(file.exists()){
				file.delete();
				return "0";
			}
		}
		return null;
	}
	
	@RequestMapping(value = "downFile")
	 public String downFile(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String attachmentName = request.getParameter("attachmentName");
		String attachmentId = request.getParameter("attachmentId");
		String folder = attachmentId.split("_")[0];
		String fileName = attachmentId.split("_")[1]+attachmentName.substring(attachmentName.lastIndexOf("."));
		String module = request.getParameter("module");
		String filePath = ContractCont.UPLOAD_HOME+File.separator+module+File.separator+folder+File.separator+fileName;
		    OutputStream ops = null;
		    File file = new File(filePath);
		    InputStream is = new FileInputStream(file);
		    try {
		      HashMap<String, String> contentTypeMap = (HashMap<String, String>) uploadService.getAttachHeard(attachmentName, null);
		      String contentTypeDesc = contentTypeMap.get("contentTypeDesc");
		      //设置html 头信息

		      String fName = URLEncoder.encode(attachmentName,"UTF-8");
		      if (fName.length() > 150) {
		    	  fName =new String(attachmentName.getBytes("UTF-8") , "ISO-8859-1");
		      }
		      fName = fName.replaceAll("\\+", "%20");
		      response.setHeader("Cache-control","private");
		      if(contentTypeDesc != null){
		        response.setContentType(contentTypeDesc);
		      }else {
		        response.setContentType("application/octet-stream");
		      }
		      response.setHeader("Accept-Ranges","bytes");
		      response.setHeader("Cache-Control","maxage=3600");
		      response.setHeader("Pragma","public");
		      response.setHeader("Accept-Length",String.valueOf(file.length()));
		      response.setHeader("Content-Length",String.valueOf(file.length()));
		      response.setHeader("Content-disposition","attachment; filename=\"" + fName + "\"");
		      ops = response.getOutputStream();
		      if(is != null){
		        byte[] buff = new byte[8192];
		        int byteread = 0;
		        while( (byteread = is.read(buff)) != -1){
		          ops.write(buff,0,byteread);
		          ops.flush();
		        }
		      }
		    } catch (Exception ex) {
		      ex.printStackTrace();
		      throw ex;
		    } finally {
		      if (is != null) {
		        is.close();
		      }
		    }
		    return null;
		  }
}
