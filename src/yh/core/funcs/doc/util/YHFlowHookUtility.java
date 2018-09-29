package yh.core.funcs.doc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;


public class YHFlowHookUtility {
  private String PLUGINPACKAGE = "yh.plugins.workflow.system";
  public String runHook(Connection conn, YHPerson user  ,Map dataMap,  String module ) throws Exception {
    String query = "select * from "+ YHWorkFlowConst.FLOW_HOOK +" where hmodule='"+ module +"' and status >0";
    Statement stm = null; 
    ResultSet rs = null; 
    Map arrayData = new HashMap();
    int runId = 0;
    try { 
      stm = conn.createStatement(); 
      rs = stm.executeQuery(query); 
      if (rs.next()){ 
        int seqId = rs.getInt("SEQ_ID");
        int flowId = rs.getInt("FLOW_ID");
        String hName = rs.getString("hname");
        String hmodule = rs.getString("hmodule");
        String map = rs.getString("map");
        map = YHUtility.null2Empty(map);
        map = map.trim();
        String[] maps = map.split(",");
        for (String s : maps) {
          if (!"".equals(s)) {
            String[] keyValue = s.split("=>");
            if (dataMap.containsKey(keyValue[0])) {
              arrayData.put(keyValue[1], dataMap.get(keyValue[0]));
            }
          }
        }
        String attachmentId = YHUtility.null2Empty((String)dataMap.get("ATTACHMENT_ID"));
        String attachmentName = YHUtility.null2Empty((String)dataMap.get("ATTACHMENT_NAME"));
        String moduleSrc =(String) dataMap.get("MODULE_SRC");
        String moduleDesc =(String) dataMap.get("MODULE_DESC");
        String keyId = (String)dataMap.get("KEY");
        String field = (String)dataMap.get("FIELD");
        String newAttachmentId = YHWorkFlowUtility.copyAttach(attachmentId, attachmentName, moduleSrc, moduleDesc);
        YHFlowRunUtility util = new YHFlowRunUtility();
        runId = util.createNewWork(conn, flowId, user, newAttachmentId, attachmentName, arrayData) ;
        String query1 = "insert into "+ YHWorkFlowConst.FLOW_RUN_HOOK +" (run_id,module,field,key_id) values('"+runId+"','"+module+"','"+field+"','"+keyId+"')";
        YHWorkFlowUtility.updateTableBySql(query1, conn);
        return YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrun/list/turn/turnnext.jsp?runId=" + runId + "&flowId=" + flowId + "&prcsId=1&flowPrcs=1";
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, rs, null); 
    } 
    return "";
  }
  public String runHookPlugin(Connection conn , int runId) throws Exception {
    String query = "SELECT * from "+ YHWorkFlowConst.FLOW_HOOK +" a left outer join "+ YHWorkFlowConst.FLOW_RUN_HOOK +" b on a.hmodule = b.module where b.run_id='"+runId+"'";
    Statement stm = null; 
    ResultSet rs = null; 
    Map arrayData = new HashMap();
    try { 
      stm = conn.createStatement(); 
      rs = stm.executeQuery(query); 
      if (rs.next()){ 
        int seqId = rs.getInt("SEQ_ID");
        String condition = rs.getString("condition");
        String conditionSet = rs.getString("condition_set");
        String plugin = rs.getString("plugin");
        
        YHTurnConditionUtility tu = new YHTurnConditionUtility();
        String notPass = "";
        String query2 = "SELECT FORM_SEQ_ID from "+ YHWorkFlowConst.FLOW_TYPE +" FLOW_TYPE, "+ YHWorkFlowConst.FLOW_RUN +" FLOW_RUN WHERE RUN_ID=" + runId + " AND FLOW_RUN.FLOW_ID = FLOW_TYPE.SEQ_ID";
        int formId = 0;
        Statement stm2 = null; 
        ResultSet rs2 = null; 
        try { 
          stm2 = conn.createStatement(); 
          rs2 = stm2.executeQuery(query2); 
          if (rs2.next()) {
            formId = rs2.getInt("FORM_SEQ_ID");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm2, rs2, null); 
        }
        //------------------------------------------- 转出条件检查 ----------------------------------
        YHTurnConditionUtility turnUtility = new YHTurnConditionUtility();
        Map formData = turnUtility.getForm(formId, runId, conn);
        if (!YHUtility.isNullorEmpty(condition)) {
          notPass = tu.checkCondition(formData, condition, conditionSet);
        }
        Statement stm3 = null; 
        ResultSet rs3 = null; 
        String map = "";
        String query3 = "select map from "+ YHWorkFlowConst.FLOW_HOOK +" where SEQ_ID='"+seqId+"'";
        try { 
          stm3 = conn.createStatement(); 
          rs3 = stm3.executeQuery(query3); 
          if (rs3.next()) {
            map = YHUtility.null2Empty(rs3.getString("map"));
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm3, rs3, null); 
        }
        String[] mapArray = map.split(",");
        Map arrayHandler = new HashMap();
        for (String ss : mapArray) {
          if (!YHUtility.isNullorEmpty(ss)) {
            String[] items = ss.split("=>");
            if (formData.containsKey(items[1])) {
              arrayHandler.put(items[0], formData.get(items[1]));
            }
          }
        }
        boolean agree = false;
        if ("setOk".equals(notPass) || "".equals(notPass)) {
          agree = true;
        }
        YHIWFHookPlugin  pluginObj = null;
        if (plugin != null
            && !"".equals(plugin)) {
          String className = PLUGINPACKAGE + "." + plugin;
          try{
            pluginObj = (YHIWFHookPlugin) Class.forName(className).newInstance();
            if (pluginObj != null) {
              String str = pluginObj.execute( conn , runId  ,  arrayHandler , formData ,agree);
            }
          } catch(ClassNotFoundException ex){
          }
        }
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, rs, null); 
    } 
    return "";
  }
  public int getFlowId(Connection conn , String field ,String keyId) throws Exception{
    String query  = "select run_id from "+ YHWorkFlowConst.FLOW_RUN_HOOK +" where field='"+ field + "' and key_id='"+keyId+"'";
    Statement stm = null; 
    ResultSet rs = null; 
    int runId = 0;
    try { 
      stm = conn.createStatement(); 
      rs = stm.executeQuery(query); 
      if (rs.next()){ 
        runId = rs.getInt("run_id");
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, rs, null); 
    } 
    return runId;
  }
  public int isRunHook(Connection conn , String field ,String keyId) throws Exception{
    String query  = "select run_id from "+ YHWorkFlowConst.FLOW_RUN_HOOK +" where field='"+ field + "' and key_id='"+keyId+"'";
    Statement stm = null; 
    ResultSet rs = null; 
    int runId = 0;
    try { 
      stm = conn.createStatement(); 
      rs = stm.executeQuery(query); 
      if (rs.next()){ 
        runId = rs.getInt("run_id");
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, rs, null); 
    } 
    return runId;
  }
  public void deleteHook(Connection dbConn , String field , String keyId) throws Exception {
    int runId = this.isRunHook(dbConn, field, keyId);
    if (runId != 0) {
      String delete = "delete from "+ YHWorkFlowConst.FLOW_RUN_HOOK +" where  field='"+field+"' and key_id='"+keyId+"'";
      YHWorkFlowUtility.updateTableBySql(delete, dbConn);
    }
  }
  public int getMax(Connection conn , String sql) throws Exception {
    Statement stm = null; 
    ResultSet rs = null; 
    int max = 0;
    try { 
      stm = conn.createStatement(); 
      rs = stm.executeQuery(sql); 
      if (rs.next()){ 
        max = rs.getInt(1);
      } 
    } catch(Exception ex) { 
      throw ex; 
    } finally { 
      YHDBUtility.close(stm, rs, null); 
    } 
    return max;
  }
}
