package yh.core.funcs.doc.act;

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
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.doc.data.YHDocFlowFormReglex;
import yh.core.funcs.doc.logic.YHFlowFormLogic;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.doc.praser.YHFormPraser;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;

public class YHFlowImportAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.doc.act.YHFlowImportAct");
  public String importForm(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    InputStream in = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      //注意这里的
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
        BufferedReader reader =  new  BufferedReader( new  InputStreamReader(is, "utf-8"));    
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
        
        YHFlowFormLogic ffl = new YHFlowFormLogic();
        
        HashMap hm = (HashMap) YHFormPraser.praserHTML2Dom(printModel);
        Map<String, Map> m1 = YHFormPraser.praserHTML2Arr(hm);
        
        String data = YHFormPraser.toJson(m1).toString();
        String printModelNew = YHFormPraser.toShortString(m1, printModel, YHDocFlowFormReglex.CONTENT);
        ffl.updateFlowForm(dbConn, seqId, new String[]{"PRINT_MODEL","PRINT_MODEL_SHORT"},  new String[]{printModel,printModelNew});
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
      String query = "update "+ YHWorkFlowConst.FLOW_TYPE +" set ";
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
      
      query = "delete from "+ YHWorkFlowConst.FLOW_PROCESS +" where FLOW_ID=" ;
      
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
    String query = "update "+ YHWorkFlowConst.FLOW_FORM_TYPE +" set CSS='"+ css +"' where SEQ_ID=" + formId;
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
    String query = "update "+ YHWorkFlowConst.FLOW_FORM_TYPE +" set SCRIPT='"+ script +"' where SEQ_ID=" + formId;
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
