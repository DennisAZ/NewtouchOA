package yh.core.funcs.workflow.act;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.data.YHPlugin;
import yh.core.funcs.workflow.logic.YHPluginLogic;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUtility;

public class YHPluginAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHPluginAct");
  private String PLUGINPATH = "WEB-INF/classes/yh/plugins/workflow";
  private String jarPath = "/yh/plugins/workflow";
  public String getPluginListTwo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try{
      String contextRealPath = request.getSession().getServletContext().getRealPath("/");
      String path = contextRealPath + "WEB-INF/classes/yh/plugins/workflow/system";
      File catalog = new File(path);
      if(!catalog.exists()){
        catalog.mkdirs();
      }
      List<YHPlugin> list = new ArrayList();
      catalog = new File(path);
      File[] files = catalog.listFiles();
      for (File file : files) {
        String ext = YHFileUtility.getFileExtName(file.getName());
        if ("class".equals(ext)) {
          String fileName = YHFileUtility.getFileNameNoExt(file.getName());
          YHPlugin p = new YHPlugin();
          p.setPluginFile(fileName);
          String pluginName = "";
          String pluginDesc = "";
          String configPath = path  + File.separator +  fileName + ".ini";
          File configFile = new File(configPath);
          if (configFile.exists()) {
            Map map = new HashMap();
            YHFileUtility.load2Map(configPath, map);
            pluginName = (String) map.get("name");
            pluginDesc = (String) map.get("desc");
            if (pluginName == null) {
              pluginName = "";
            }
            if (pluginDesc == null) {
              pluginDesc = "";
            }
          }
          p.setPluginDesc(pluginDesc);
          p.setPluginName(pluginName);
          list.add(p);
        }
      }
      request.setAttribute("path", path);
      request.setAttribute("files", list);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/workflow/flowdesign/viewlist/setproperty/plugin.jsp";
  }
  
  public String getPluginList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try{
      String contextRealPath = request.getSession().getServletContext().getRealPath("/");
      String path = contextRealPath + this.PLUGINPATH;
      File catalog = new File(path);
      if(!catalog.exists()){
        catalog.mkdirs();
      }
      List<YHPlugin> list = new ArrayList();
      catalog = new File(path);
      File[] files = catalog.listFiles();
      for (File file : files) {
        String ext = YHFileUtility.getFileExtName(file.getName());
        if ("class".equals(ext)) {
          String fileName = YHFileUtility.getFileNameNoExt(file.getName());
          YHPlugin p = new YHPlugin();
          p.setPluginFile(fileName);
          String pluginName = "";
          String pluginDesc = "";
          String configPath = path  + File.separator +  fileName + ".ini";
          File configFile = new File(configPath);
          if (configFile.exists()) {
            Map map = new HashMap();
            YHFileUtility.load2Map(configPath, map);
            pluginName = (String) map.get("name");
            pluginDesc = (String) map.get("desc");
            if (pluginName == null) {
              pluginName = "";
            }
            if (pluginDesc == null) {
              pluginDesc = "";
            }
          }
          p.setPluginDesc(pluginDesc);
          p.setPluginName(pluginName);
          list.add(p);
        }
      }
      request.setAttribute("path", path);
      request.setAttribute("files", list);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/workflow/flowdesign/viewlist/setproperty/plugin.jsp";
  }
  
  public String selectData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sqlId = request.getParameter("sqlId");
//      sqlId = "assetReturn";
      String contextRealPath = request.getSession().getServletContext().getRealPath("/");
      String path = contextRealPath + this.PLUGINPATH;
      String configPath = path  + File.separator + "selectDataSql.properties";
      File configFile = new File(configPath);
      String sql = "";
      Map map = new HashMap();
      if (configFile.exists()) {
        YHFileUtility.load2Map(configPath, map);
        sql = (String)map.get(sqlId);
      } else {
        configPath = jarPath + File.separator +   "selectDataSql.properties";
        InputStream is=this.getClass().getResourceAsStream(configPath);   
        BufferedReader br=new BufferedReader(new InputStreamReader(is)); 
        String line = br.readLine() ;
        while (line != null) {
          int start = line.indexOf("=");
          if (start != -1) {
            String key = line.substring(0 , start);
            String value = line.substring(start + 1);
            if (sqlId.equals(key)) {
              sql = value;
              break;
            }
          }
          line = br.readLine() ;
        }
      }
      String findStr = request.getParameter("findStr");
      YHPluginLogic logic = new YHPluginLogic();
      StringBuffer result = logic.getSelectData(dbConn, request.getParameterMap(), findStr, sql);
      System.out.println(result);
      PrintWriter pw = response.getWriter();
      pw.println(result.toString());
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public String getField(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection conn = null;
    String flowIdStr = request.getParameter("flowId");
    int flowId = 0 ;
    if (YHUtility.isInteger(flowIdStr)) {
      flowId = Integer.parseInt(flowIdStr);
    }
    String formIdStr = request.getParameter("formId");
    int formId = 0 ;
    if (YHUtility.isInteger(formIdStr)) {
      formId = Integer.parseInt(formIdStr);
    }
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String valueField = request.getParameter("valueField");
      YHFlowRunUtility ut = new YHFlowRunUtility();
      valueField = ut.getField(conn, flowId , formId, valueField);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, "'" + valueField + "'");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public static void main(String[] args) throws Exception {
    String jarPath = "/yh/plugins/workflow";
    String configPath = jarPath + File.separator +  "selectDataSql.properties";
    InputStream is= YHPluginAct.class.getResourceAsStream(configPath);   
    BufferedReader br=new BufferedReader(new InputStreamReader(is)); 
    String line = br.readLine() ;
    String sqlId = "assetReturn";
    String sql  = "";
    while (line != null) {
      int start = line.indexOf("=");
      if (start != -1) {
        String key = line.substring(0 , start);
        String value = line.substring(start + 1);
        if (sqlId.equals(key)) {
          sql = value;
          break;
        }
      }
      line = br.readLine() ;
    }
    System.out.println(sql);
  }
}
