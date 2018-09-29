package yh.core.funcs.workflow.act;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.data.YHFlowFormReglex;
import yh.core.funcs.workflow.data.YHFlowFormType;
import yh.core.funcs.workflow.logic.YHFlowFormLogic;
import yh.core.funcs.workflow.logic.YHFlowTypeLogic;
import yh.core.funcs.workflow.logic.YHFormVersionLogic;
import yh.core.funcs.workflow.logic.YHWorkflowSave2DataTableLogic;
import yh.core.funcs.workflow.praser.YHFormPraser;
import yh.core.funcs.workflow.util.YHFlowFormUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;

public class YHFlowImportAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHFlowImportAct");
  public String importForm(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    InputStream in = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      //注意这里的      int newId = 0 ;
      fileForm.parseUploadRequest(request);
      int type = 0 ;
      String formId = fileForm.getParameter("formId");
      int seqId = Integer.parseInt(formId);
      String ext = fileForm.getFileExt("htmlFile");
      if ("txt".equals(ext) 
          || "html".equals(ext)
          || "htm".equals(ext)) {
        FileItem fileItem =  fileForm.getFileItem("htmlFile");
        InputStream is = fileItem.getInputStream();
        String isOa = fileForm.getParameter("isOa");
        String utf8 = "utf-8";
        if ("on".equals(isOa)) {
          utf8 = "GBK";
        }
        BufferedReader reader =  new  BufferedReader( new  InputStreamReader(is, utf8));    
        StringBuffer sb = new StringBuffer();
        String line =  null ;    
        try {    
            while((line = reader.readLine()) !=  null ) {    
              sb.append(line );    
           }    
        } catch (IOException e) {    
          throw e;   
        } finally{    
          try{    
            is.close();    
          }catch (IOException e) {    
            throw e;  
          }    
        }    
        String printModel = sb.toString();
        if (printModel == null) {
          printModel = "";
        }
        printModel = printModel.replaceAll("[\n-\r]", "");
        printModel = getOutCss(printModel , seqId , dbConn);
        printModel = getOutScript(printModel , Integer.parseInt(formId ) , dbConn);
        printModel = printModel.replaceAll("\"", "\\\\\"");
        
        
        YHORM orm = new YHORM();
        YHFlowFormType form = (YHFlowFormType) orm.loadObjSingle(dbConn, YHFlowFormType.class, seqId);
        
        YHFlowFormType form2 = new YHFlowFormType();
        form2.setFormName(form.getFormName());
        form2.setItemMax(form.getItemMax());
        form2.setPrintModel(printModel);
        form2.setDeptId(form.getDeptId());
        
        String printModelNew = "";
        YHFlowFormLogic ffl = new YHFlowFormLogic();
        HashMap hm = (HashMap) YHFormPraser.praserHTML2Dom(printModel);
        Map<String, Map> m1 = YHFormPraser.praserHTML2Arr(hm);
        printModelNew = YHFormPraser.toShortString(m1, printModel, YHFlowFormReglex.CONTENT);
        form2.setPrintModelShort(printModelNew);
        form2.setFormId(0);
        form2.setVersionNo(form.getVersionNo() + 1);
        form2.setVersionTime(new Date());
        //创建新的数据
        orm.saveSingle(dbConn, form2);
        YHFormVersionLogic logic =new YHFormVersionLogic();
         newId = logic.getMaxFormId(dbConn);
        YHFlowFormUtility ffu = new YHFlowFormUtility();
        ffu.cacheForm(newId, dbConn);
        YHWorkflowSave2DataTableLogic logic4 = new YHWorkflowSave2DataTableLogic();
        logic4.updateItemMax(dbConn, newId);
        //创建表结构
        String flowTypes = logic4.getFlowTypeByFormId(dbConn, seqId + "");
        logic4.createFlowFormTable(dbConn, newId, flowTypes);
        //更新flow关联
        logic4.updateFlowTypeByFormId(dbConn, seqId + "", newId);
        //修改formId
        logic4.updateFormTypeByFormId(dbConn, seqId + "", newId);
        type = 1;
      } else {
        type = 0 ;
      }
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html");
      response.setHeader("Cache-Control", "no-cache");  
      PrintWriter out = response.getWriter();
      out.print("<body onload=\"window.parent.tooltip("+type +" ,null, "+newId +")\"/>");
      out.flush();
      out.close();
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  } 
  public String importFlow(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    InputStream in = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      int type = 0 ;
      String flowId = fileForm.getParameter("flowId");
      String sUserOn = fileForm.getParameter("userOn");
      boolean isUserOn = false;
      if ("on".equals(sUserOn)) {
        isUserOn = true;
      }
      int seqId = Integer.parseInt(flowId);
      String ext = fileForm.getFileExt("attachment");
      if ("xml".equals(ext)) {
        FileItem fileItem =  fileForm.getFileItem("attachment");
        InputStream is = fileItem.getInputStream();
        BufferedReader reader =  new  BufferedReader(new  InputStreamReader(is , "UTF-8") );    
        StringBuffer sb = new StringBuffer(); 
        String line =  null ;    
        try {    
          while((line = reader.readLine()) !=  null ) {    
            sb.append(line );    
          }    
        } catch (IOException e) {    
          throw e;   
        } finally{    
          try{    
            is.close();    
          }catch (IOException e) {    
            throw e;  
          }    
        }    
        StringReader rs = new StringReader(sb.toString());
        SAXReader saxReader = new SAXReader();   
        Document document = saxReader.read(rs);
        YHFlowTypeLogic logic = new YHFlowTypeLogic();
        Element root =  document.getRootElement();
        logic.importFlow(root, Integer.parseInt(flowId), isUserOn, dbConn);
        type = 1;
      } else {
        type = 0 ;
      }
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html");
      response.setHeader("Cache-Control", "no-cache");  
      PrintWriter out = response.getWriter();
      out.print("<body onload=\"window.parent.tooltip("+type +")\"/>");
      out.flush();
      out.close();
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public static void main(String[] args) {
    InputStream in = null;
    try{
      File file = new File("C:\\Users\\Think\\Desktop\\督查督办.xml");
      InputStream is = new FileInputStream(file);
      in = new BufferedInputStream(is);
      StringBuffer sb = new StringBuffer();
      BufferedReader reader =  new  BufferedReader(new  InputStreamReader(is));    
      String line =  null ;    
      try {    
        while((line = reader.readLine()) !=  null ) {    
          sb.append(line );    
        }    
      } catch (IOException e) {    
        throw e;   
      } finally{    
        try{    
          is.close();    
        }catch (IOException e) {    
          throw e;  
        }    
      }    
      StringReader rs = new StringReader(sb.toString());
      SAXReader saxReader = new SAXReader();   
      Document document = saxReader.read(rs);
      
      Element root =  document.getRootElement();
      Element flowMsg = root.element("BaseInfo");
     
      List<Element> iterator = flowMsg.elements();
      String query = "update oa_fl_type set ";
      for (Element el : iterator){
        String name = el.getName();
        if (name.equals("FLOW_ID") 
            || name.equals("FLOW_NAME")
            || name.equals("FLOW_SORT")
            || name.equals("FORM_ID")) {
          continue; 
        }
        if ("".equals(el.getText())) {
          query += " " + el.getName() + "=null,";
        } else {
          query += " " + el.getName() + "='" + el.getText() + "',";
        }
      }
      query = query.substring(0, query.length() - 1);
      
      query = "delete from oa_fl_process where FLOW_ID=" ;
      
      //System.out.println(query);
//      List<Node> rowList = document.selectNodes("/WorkFlow/Process/ID"); 
//      for (Node node : rowList) {
        //System.out.println(flowMsg.getName() + ":" + flowMsg.getText());
//      }
      
    }catch(Exception ex){
      
    }
  }
  public  String getOutCss(String printModel , int formId , Connection conn) throws Exception {
    String css = "";
    int index1 =  printModel.indexOf("<style>");
    if ( index1 != -1) {
      printModel = printModel.substring(index1 + "<style>".length());
      int index = printModel.indexOf("</style>");
      if (index != -1){
        css = printModel.substring(0 , index);
        printModel = printModel.substring(index + "</style>".length());
      }
    }
    css = css.replaceAll("'", "''");
    String query = "update oa_fl_form_type set CSS='"+ css +"' where SEQ_ID=" + formId;
    PreparedStatement stm3 = null;
    try {
      stm3 = conn.prepareStatement(query);
      stm3.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, null, null); 
    }
    return printModel;
  }
  public  String getOutScript(String printModel , int formId , Connection conn) throws Exception {
    String script = "";
    int index1 =  printModel.indexOf("<script>");
    if ( index1 != -1) {
      printModel = printModel.substring(index1 + "<script>".length());
      int index = printModel.indexOf("</script>");
      if (index != -1){
        script = printModel.substring(0 , index);
        printModel = printModel.substring(index + "</script>".length());
      }
    }
    script = script.replaceAll("'", "''");
    String query = "update oa_fl_form_type set SCRIPT='"+ script +"' where SEQ_ID=" + formId;
    PreparedStatement stm3 = null;
    try {
      stm3 = conn.prepareStatement(query);
      stm3.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, null, null); 
    }
    return printModel;
  }
  
}
