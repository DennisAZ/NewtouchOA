package yh.subsys.jtgwjh.certificate.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agile.zip.CZipInputStream;
import com.agile.zip.ZipEntry;

import yh.core.data.YHRequestDbConn;
import yh.core.esb.server.update.logic.YHUpdateServerLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.jtgwjh.certificate.logic.YHJhCertificateLogic;



public class YHJhCertificateAct{
	/**
	 * 
	 * 导入证书信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	
	public String  importCert(HttpServletRequest request, HttpServletResponse response) throws Exception {
		 String path = YHSysProps.getRootPath() + "/certificate";//接收解压文件存放路径
		 String result="";
		  Connection dbConn = null;
		  try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			  YHFileUploadForm fileForm = new YHFileUploadForm();
			  YHJhCertificateLogic logic = new YHJhCertificateLogic();
		      fileForm.parseUploadRequest(request);
		      Iterator<String> iKeys = fileForm.iterateFileFields();
		      String savePath="";
		      if (iKeys.hasNext()) {
			        String fieldName = iKeys.next();
			        String fileName =fileForm.getFileName(fieldName);
			        savePath = path + File.separator + fileName;
			        File parentFile = new File(path);
			        if (!parentFile.exists()) {
			          parentFile.mkdir();
			        }
			  fileForm.saveFile(savePath);
		      }
		      File file = new File(savePath);
			  InputStream rarFile = new FileInputStream(file);
			    if (rarFile == null) {
			      return null;
			    }
			    CZipInputStream zip = new CZipInputStream(rarFile);// 支持中文目录
			    ZipEntry entry;
			    String certFile="";
			    String certPath="";
			    while ((entry = zip.getNextEntry()) != null) {// 循环zip下的所有文件和目录
			      String fileNames = entry.getName();
			      if (YHUtility.isNullorEmpty(fileNames)) {
			        continue;
			      }
			      File outFile = new File(path + "/" + fileNames);
			      File outPath = outFile.getParentFile();
			      if (!outPath.exists()) {
			        outPath.mkdirs();
			      }
			      if (!entry.isDirectory()) {
			        try {
			          certFile=entry.getName();
			          certPath=path + File.separator   + certFile;
			          outFile = new File(path + File.separator   + fileNames);
			          outFile.createNewFile();
			          FileOutputStream out = new FileOutputStream(outFile);
			          int len = 0;
			          byte[] buff = new byte[4096];
			          while ((len = zip.read(buff)) != -1) {
			            out.write(buff, 0, len);
			          }
			          out.close();
			        } catch (IOException e) {
			          e.printStackTrace();
			        }
			      }
			      if(entry.isDirectory()){
			    	  File zipFolder = new File(savePath + File.separator   + fileNames);  
			    	   if (!zipFolder.exists()) {  
			               zipFolder.mkdirs();  
			    		  }  
			      }
			    }
		      zip.closeEntry();
			    if (!YHUtility.isNullorEmpty(certFile)) {//解析文件获取证书信息
			    	boolean flag=logic. parseFile(certPath, dbConn);
			    	if(flag==true){
			    		   request.setAttribute(YHActionKeys.RET_MSRG, "成功导入证书信息！");
			    		   result= "/subsys/jtgwjh/certificate/success.jsp";
			    	}
			    	else {
			    		result= "/subsys/jtgwjh/certificate/failure.jsp";
			    	}
				    }
		   return result;
		  }catch(Exception ex){
		      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		      throw ex;
		  }
	}
	
	/**
	 * 获取证书信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	 public String getCertificateList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		    Connection dbConn = null;
		    try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		      YHJhCertificateLogic logic = new YHJhCertificateLogic();
		      String data = logic.getJsonLogic(dbConn, request.getParameterMap(), person, request);
		      PrintWriter pw = response.getWriter();
		      pw.println(data);
		      pw.flush();
		    } catch (Exception ex) {
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		      throw ex;
		    }
		    return null;
		  }
	 /**
	  * 删除证书信息
	  * @param request
	  * @param response
	  * @return
	  * @throws Exception
	  */
	  public String deleteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		    String seqIdStr = request.getParameter("seqId");
		    Connection dbConn = null;
		    try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      dbConn = requestDbConn.getSysDbConn();
		      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

		      YHJhCertificateLogic logic = new YHJhCertificateLogic();
		      String temp = logic.deleteFileLogic(dbConn, seqIdStr);
		      
		      //系统日志
		      YHSysLogLogic.addSysLog(dbConn, "60", "删除证书信息。"  ,person.getSeqId(), request.getRemoteAddr());
		      
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		    } catch (Exception e) {
		      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
		      throw e;
		    }
		    return "/core/inc/rtjson.jsp";
		  }
}