package yh.core.funcs.workflow.logic;

import java.sql.Connection;
import java.util.Date;
import java.util.Map;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHPluginLogic {
  public StringBuffer getSelectFlowData( Connection conn, Map request , String findStr , String flowId, String field , YHPerson user)
  throws Exception { 
StringBuffer resualt = new StringBuffer();
try {
  String query2 = "select FLOW_RUN.RUN_ID , RUN_NAME,";
  field = YHWorkFlowUtility.getOutOfTail(field, "`");
  String[] fields = field.split("`");
  
  for (int i = 0 ;i < fields.length ; i++) {
    if ("[文号]".equals(fields[i])
        || "[流水号]".equals(fields[i])) {
      continue;
    }
    query2 += "1,";
  }
  if (query2.endsWith(",")) {
    query2 =  YHWorkFlowUtility.getOutOfTail(query2);
  }
  query2 += " FROM oa_fl_run as FLOW_RUN  where  FLOW_RUN.FLOW_ID =" + flowId + " " + this.getRunId(user.getSeqId());
  
  String[] value = (String[])request.get("runName");
  if (value != null && value.length > 0){
    if (!YHUtility.isNullorEmpty(value[0])) {
      query2 += " and RUN_NAME like  '%" + YHUtility.encodeLike(value[0]) + "%' " + YHDBUtility.escapeLike() ;
    }
  }
  value = (String[])request.get("runId");
  if (value != null && value.length > 0){
    if (!YHUtility.isNullorEmpty(value[0])) {
      query2 += " and FLOW_RUN.RUN_ID =  '" + YHUtility.encodeLike(value[0]) + "' " ;
    }
  }
  
  query2 += " order by FLOW_RUN.RUN_ID";
  YHFlowRunUtility util = new YHFlowRunUtility();
  YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
  YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,query2);
  for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
    YHDbRecord record = pageDataList.getRecord(i);
    int runId = YHUtility.cast2Long(record.getValueByName("runId")).intValue();
    String runName = (String)record.getValueByName("runName");
    String values = "";
    for (int j = 0 ;j < fields.length ; j++) {
      String fi = fields[j];
      
      if ("[文号]".equals(fi)
          || "[流水号]".equals(fi) 
          || !YHUtility.isInteger(fi)) {
        if ("[文号]".equals(fi)) {
          values += runName + "++";
        }
        if ("[流水号]".equals(fi)) {
          values += runId + "++";
        }
        continue;
      }
      YHFlowRunData data  = util.getFlowRunData(conn, runId,Integer.parseInt(fi) , Integer.parseInt(flowId));
      String v = "";
      if (data != null) {
        v = data.getItemData();
        record.updateField("DATA_" + fi,  data.getItemData());
      } 
      record.updateField("DATA_" + fi,  v);
      values += v + "++"; 
    }
    record.addField("value", values);
  }
  resualt.append(pageDataList.toJson());
} catch (Exception ex) {
  ex.printStackTrace();
  throw ex;
}
return resualt;
}
  public String getRunId(int userId) {
    String myRunId  = " select  DISTINCT(FLOW_RUN.RUN_ID) from oa_fl_run_prcs where  USER_ID=" + userId + " and PRCS_FLAG <> 5 ";
    return " and FLOW_RUN.RUN_ID IN (" + myRunId + ")";
  }
  public StringBuffer getSelectData( Connection conn, Map request , String findStr , String sql)
  throws Exception { 
StringBuffer resualt = new StringBuffer();
String query = "";
try {
  if (!YHUtility.isNullorEmpty(findStr)) {
    String[] items = findStr.split(",");
    for (String tmp : items) {
      if (!YHUtility.isNullorEmpty(tmp)){
        String[] value = (String[])request.get(tmp);
        if (value != null && value.length > 0){
          if (!YHUtility.isNullorEmpty(value[0])) {
            query += " and " + tmp + " like '%" + YHUtility.encodeLike(value[0]) + "%' " + YHDBUtility.escapeLike() ;
          }
        }
      }
    }
  }
  sql += query;
  YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
  YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
  for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
    YHDbRecord record = pageDataList.getRecord(i);
    String values = "";
    int fieldCnt = record.getFieldCnt();
    for (int j= 0 ;j < fieldCnt; j++) {
      Object colObj =  record.getValueByIndex(j);
      String val = "";
      if (colObj != null) {
        Class fieldType = colObj.getClass(); 
        if (Integer.class.equals(fieldType)) {        
          val = String.valueOf(((Integer)colObj).intValue());
        }else if (Long.class.equals(fieldType)) {        
          val = String.valueOf(((Long)colObj).longValue());
        }else if (Double.class.equals(fieldType)) {        
          val = YHUtility.getFormatedStr(((Double)colObj).doubleValue(), YHUtility.WITHOUTGROUP);
        }else if (Date.class.equals(fieldType)) {
          val =  YHUtility.getDateTimeStr((Date)colObj);
        }else {
          if (colObj == null) {
            val = "";
          }else {
            String tmpStr = YHUtility.null2Empty(colObj.toString());
            tmpStr = tmpStr.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "").replace("\'", "\\\'");
            val = tmpStr;
          }
        }
        values += val + "++";
      }
      record.addField("value", values);
    }
  }
  resualt.append(pageDataList.toJson());
} catch (Exception ex) {
  ex.printStackTrace();
  throw ex;
}
return resualt;
}
}
