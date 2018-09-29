package yh.core.funcs.workflow.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowHook;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHFlowHookLogic {
  public String configPath = "/core/funcs/workflow/workflowUtility/flow_hook_config.properties";
  public String getHook(Connection conn,int hid , String webrootPath)  throws Exception{
    String query = "select * from oa_fl_hook where SEQ_ID =  " + hid;
    Statement stm = null;
    ResultSet rs = null;
    StringBuffer sb = new StringBuffer();
    String module = "";
    int flowId = 0;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()){
        sb.append("{");
        module = rs.getString("HMODULE");
        sb.append("module:\"" + YHUtility.encodeSpecial(module) + "\"");
        sb.append(",status:\"" + rs.getString("STATUS") + "\"");
        sb.append(",name:\"" +  rs.getString("HNAME")  + "\"");
        sb.append(",desc:\"" +  rs.getString("HDESC") + "\"");
        flowId =  rs.getInt("FLOW_ID");
        sb.append(",flowId:" +flowId);
        sb.append(",map:\"" +  YHUtility.null2Empty(rs.getString("MAP")) + "\"");
        sb.append(",condition:\"" +  YHUtility.null2Empty(rs.getString("CONDITION")) + "\"");
        sb.append(",conditionSet:\"" + YHUtility.null2Empty(rs.getString("CONDITION_SET")) + "\"");
        sb.append(",system:\"" + rs.getString("SYSTEM")+ "\"");
        sb.append(",plugin:\"" + YHUtility.null2Empty(rs.getString("plugin"))+ "\"");
        sb.append("}");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    String query2 = "SELECT FLOW_TYPE.SEQ_ID,FLOW_NAME,FLOW_TYPE from oa_fl_type as FLOW_TYPE,oa_fl_sort as FLOW_SORT where FLOW_TYPE.FLOW_SORT=FLOW_SORT.SEQ_ID order by SORT_NO,FLOW_NO";
    Statement stm2 = null;
    ResultSet rs2 = null;
    sb.append(",flows:[");
    int count = 0 ;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query2);
      while (rs2.next()){
        sb.append("{");
        sb.append("flowName:\"" + YHUtility.encodeSpecial(rs2.getString("FLOW_NAME")) + "\"");
        sb.append(",flowId:\"" + rs2.getString("SEQ_ID") + "\"");
        sb.append("},");
        count++ ;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    sb.append(",module:");
    Properties p = new Properties();
    p.load(new InputStreamReader(new FileInputStream(new File(webrootPath + this.configPath)) , "UTF-8"));
    String keyValue = p.getProperty(module);
    if (!YHUtility.isNullorEmpty(keyValue)) {
      sb.append(keyValue);
    } else {
      sb.append("{}");
    }
    
    String query3 = "select FORM_SEQ_ID FROM oa_fl_type WHERE SEQ_ID =" + flowId;
    YHFlowTypeLogic flowTypelogic = new YHFlowTypeLogic();
    YHFlowFormLogic ffLogic = new YHFlowFormLogic();
    int formId2 = flowTypelogic.getIntBySeq(query3, conn) ;
    String formItem = ffLogic.getTitle(conn, formId2);
    sb.append(",formItem:\"" +YHUtility.encodeSpecial(formItem) + "\"");
    return sb.toString();
  }
  public void addHookLogic(Connection conn,Map request,YHPerson person)  throws Exception{

    
    String hmodule = request.get("hmodule") == null ? null : ((String[]) request.get("hmodule"))[0];
    String status = request.get("status") == null ? null : ((String[]) request.get("status"))[0];
    String hname = request.get("hname") == null ? null : ((String[]) request.get("hname"))[0];
    String hdesc = request.get("hdesc") == null ? null : ((String[]) request.get("hdesc"))[0];
    String plugin = request.get("plugin") == null ? null : ((String[]) request.get("plugin"))[0];

   
    try{
      YHFlowHook hook=new YHFlowHook();
      hook.setSystem("0");
      hook.setHmodule(hmodule);
      hook.setStatus(Integer.parseInt(status));
      hook.setHname(hname);
      hook.setHdesc(hdesc);
      hook.setPlugin(plugin);
      YHORM orm=new YHORM();
      orm.saveSingle(conn, hook);
    }catch(Exception e){
      throw e;
    }
    
  }
  
  public String getHookJsonLogic(Connection dbConn, Map request, YHPerson person ,String webrootPath ) throws Exception {
    try {
      String sql = " select c1.seq_id,c1.hmodule, c1.hname, c1.hdesc, c1.flow_id, c1.map, c1.plugin,c1.status,c1.system,1 "
                 + " from oa_fl_hook c1 "
                 + " ORDER BY c1.SEQ_ID desc ";
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      Properties p = new Properties();
      p.load(new InputStreamReader(new FileInputStream(new File(webrootPath + this.configPath)) , "UTF-8"));
      for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
        YHDbRecord record = pageDataList.getRecord(i);
        String hmodule = (String)record.getValueByName("hmodule");
        String keyValue = p.getProperty(hmodule);
        if (!YHUtility.isNullorEmpty(keyValue)) {
          record.addField("mapName", keyValue);
        } else {
          record.addField("mapName", "{}");
        }
      }
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  
  public String getFlowNameLogic(Connection dbConn,String flowId) throws Exception {
   Statement stmt=null;
   ResultSet rs=null;
   String flowName="";
    try {
      String sql ="select flow_name from oa_fl_type where seq_id='"+flowId+"'";
      stmt=dbConn.createStatement();
      rs=stmt.executeQuery(sql);
      if(rs.next()){
        flowName=rs.getString("flow_name");
      }  
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
      return flowName;
  }
  
  public void deleteHookLogic(Connection dbConn,String seqId) throws Exception {
    Statement stmt=null;
     try {
        String sql="delete from oa_fl_hook where seq_id='"+seqId+"'";
        stmt=dbConn.createStatement();
        stmt.executeUpdate(sql);
     } catch (Exception e) {
       throw e;
     } finally {
       YHDBUtility.close(stmt, null, null);
     }
    
   }
  
}
