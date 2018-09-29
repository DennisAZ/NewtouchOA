package yh.core.funcs.smartform.act;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.servlet.YHServletUtility;
import yh.core.util.file.YHFileUtility;

public class YHFormAct {
  private static Logger log = Logger.getLogger(YHFormAct.class);
  public String getForm(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String formHtmlPage = "";
    try {
      String contextRealPath = YHServletUtility.getWebAppDir(request.getSession().getServletContext()) ;
      
      String formTemplt = request.getParameter("formTemplt");
      String formDataPathStr = request.getParameter("formDataPath");
      
      
      int i = formTemplt.lastIndexOf("/");
      String fileName = formTemplt.substring(i + 1);
      String dataPath = contextRealPath + formDataPathStr + "/" + fileName;
      
      
      if(new File(dataPath).exists()){
        formHtmlPage = "/" + formDataPathStr + "/" + fileName;
      }else{
        formHtmlPage = "/" + formTemplt;
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }
    return formHtmlPage;
  }
  public String saveFormData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String formHtml = request.getParameter("html"); 
      String contextRealPath = YHServletUtility.getWebAppDir(request.getSession().getServletContext()) ;
      
      String formTemplt = request.getParameter("formTemplt");
      String formDataPathStr = request.getParameter("formDataPath");
      
      int i = formTemplt.lastIndexOf("/");
      String fileName = formTemplt.substring( i + 1);
      
      String dataPath = contextRealPath + formDataPathStr + "/" + fileName;
      YHFileUtility.storeString2File(dataPath, formHtml);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }finally {
      
    }
    return "/core/inc/rtjson.jsp";
  }
  public String doSaveForm(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String path = request.getParameter("path");
      String formHtml = request.getParameter("content"); 
      String contextRealPath = YHServletUtility.getWebAppDir(request.getSession().getServletContext()) ;
      Date date = new Date();
      SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
      String fileName = sf.format(date);
      
      String dataPath = contextRealPath + path + "/" + fileName + ".html";
      YHFileUtility.storeString2File(dataPath, formHtml);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }finally {
      
    }
    return "/core/inc/rtjson.jsp";
   
  }
}