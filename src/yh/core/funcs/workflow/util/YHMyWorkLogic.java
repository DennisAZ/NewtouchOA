package yh.core.funcs.workflow.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowProcess;
import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.data.YHFlowRunPrcs;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;


public class YHMyWorkLogic {
  public String getFildList(Connection conn , String flowId) throws Exception {
    String query = "select LIST_FLDS_STR FROM oa_fl_type WHERE SEQ_ID='" + flowId + "'";
    Statement stm4 = null;
    ResultSet rs = null;
    String str = "";
    try {
      stm4 = conn.createStatement();
      rs = stm4.executeQuery(query);
      if (rs.next()) {
        str = YHUtility.null2Empty(rs.getString("LIST_FLDS_STR"));
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs, null); 
    }
    return str;
  }
  public String getEndWorkList1( YHPerson user,
      int pageIndex, String flowIdStr, int showLen, String opFlag  , String sortId , Connection conn , String filedList, String sortField) throws Exception {
    StringBuffer sb = new StringBuffer();
    Statement stm =null;
    ResultSet rs = null;//结果集    int pageCount = 0;//页码数

    int recordCount = 0;//总记录数
    int pgStartRecord = 0;//开始索引
    List<Map> list = new ArrayList();
    int pgEndRecord = 0;//结束索引
    try {
     
      String tmp = "";
      if(opFlag != null && ("1".equals(opFlag) || "0".equals(opFlag))){
        tmp = " and a.OP_FLAG = '"+ opFlag +"' ";
      }
      String query = "SELECT "
        + " a.SEQ_ID "
        + " ,a.RUN_ID "
        + " ,a.PRCS_ID "
        + " ,a.FLOW_PRCS "
        + " ,a.PRCS_FLAG "
        + " ,a.OP_FLAG "
        + " ,a.PRCS_TIME "
        + " ,a.CREATE_TIME "
        + " ,b.FREE_OTHER "
        + " , b.SEQ_ID As FLOW_ID"
        + " ,b.FLOW_NAME "
        + " ,c.END_TIME"
        + " ,b.FLOW_TYPE "
        + " ,b.LIST_FLDS_STR "
        + " ,b.FORM_SEQ_ID "
        + " ,c.RUN_NAME "
        + "  , p.USER_NAME"
        + " , c.BEGIN_USER as userId"
        + "  from oa_fl_run_prcs  a, oa_fl_type  b, oa_fl_run  c , person p "
        + " WHERE  "
        + " p.seq_id = c.begin_user "
        + "  and a.RUN_ID=c.RUN_ID and  "
        + "  c.FLOW_ID=b.SEQ_ID and  "
        + "  a.CHILD_RUN = 0 AND "
        + " a.PRCS_ID = (SELECT MAX(f.prcs_id) from oa_fl_run_prcs f where f.USER_ID = a.USER_ID AND f.RUN_ID = c.RUN_ID) AND "
        + "  c.DEL_FLAG='0' and  "
        + "  a.USER_ID=" + user.getSeqId()
         + " and   (a.PRCS_FLAG = '4' or a.PRCS_FLAG = '3') " + tmp;
      
      if (flowIdStr != null 
          && !"".equals(flowIdStr) 
          && !"0".equals(flowIdStr) ) {
        query += " and b.SEQ_ID = " + flowIdStr;
      }
      if (!YHUtility.isNullorEmpty(sortId)) {
        sortId = YHWorkFlowUtility.getOutOfTail(sortId);
        query += " and b.FLOW_SORT IN (" + sortId + ")";
      }
      query += " order by a.RUN_ID "+ sortField +",a.PRCS_TIME DESC";
      
      stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      long date1 = System.currentTimeMillis();
      rs = stm.executeQuery(query);
      rs.last(); 
      recordCount = rs.getRow(); //总记录数 
       //总页数 
      pageCount = recordCount / showLen; 
      if (recordCount % showLen != 0) { 
        pageCount++; 
      } 
      if (pageIndex < 1) { 
        pageIndex = 1; 
      } 
      if (pageIndex > pageCount) { 
        pageIndex = pageCount; 
      } 
     //开始索引      pgStartRecord = (pageIndex - 1) * showLen + 1;
      rs.absolute(pgStartRecord); 
      sb.append("{");
      sb.append("listData:[");
      for (int i = 0; i < showLen && !rs.isAfterLast()&&recordCount > 0; i++) { 
        Map mapTmp = new HashMap();
        Date date = rs.getDate("END_TIME");
        String state = "办理中";
        if(date != null){
          state = "已结束";
        }
        int seqId = rs.getInt("SEQ_ID");
        mapTmp.put("SEQ_ID", String.valueOf(seqId));
        mapTmp.put("RUN_ID", rs.getInt("RUN_ID"));
        mapTmp.put("FLOW_ID", rs.getInt("FLOW_ID"));
        mapTmp.put("FLOW_NAME",  rs.getString("FLOW_NAME"));
        mapTmp.put("RUN_NAME", rs.getString("RUN_NAME"));
        mapTmp.put("userId", rs.getInt("userId"));
        mapTmp.put("PRCS_ID", rs.getInt("PRCS_ID"));
        mapTmp.put("FLOW_TYPE",rs.getString("FLOW_TYPE"));
        mapTmp.put("FREE_OTHER", rs.getInt("FREE_OTHER"));
        mapTmp.put("state", state);
        mapTmp.put("userName", rs.getString("USER_NAME"));
        mapTmp.put("FLOW_PRCS",  String.valueOf(rs.getInt("FLOW_PRCS")));
        mapTmp.put("OP_FLAG",  rs.getInt("OP_FLAG"));
        mapTmp.put("FORM_SEQ_ID", rs.getInt("FORM_SEQ_ID"));
        list.add(mapTmp);;
        rs.next(); 
      }
      long date2 = System.currentTimeMillis();
      long date3 = date2 - date1;
      
       
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);;
    }
  //结束索引
    pgEndRecord =(pageIndex - 1) * showLen + list.size();
    
    for (Map map : list) {
      int runId = (Integer) map.get("RUN_ID");
      int prcsId = (Integer) map.get("PRCS_ID");
      int flowId = (Integer)  map.get("FLOW_ID");
      int formId = (Integer)  map.get("FORM_SEQ_ID");
      boolean isHaveDelPriv = this.isHaveDelPriv(runId, user, conn);
      boolean calBackPriv = this.callBackPriv(runId , prcsId, user, conn);
      sb.append("{");
      sb.append("seqId:" +  map.get("SEQ_ID"));
      sb.append(",runId:" + runId);
      sb.append(",flowId:" + flowId);
      sb.append(",flowName:\"" +  map.get("FLOW_NAME") + "\"");
      String runName = (String)map.get("RUN_NAME");
      runName = YHWorkFlowUtility.getRunName(runName);
      sb.append(",runName:'" + runName  + "'");
       
      int userSeqId =  (Integer)map.get("userId");
      sb.append(",userId:\"" + userSeqId + "\"");
      
      sb.append(",userName:\"" + (String)map.get("userName") + "\"");
      String flowType =  (String)map.get("FLOW_TYPE");
      String flowPrcs = (String)map.get("FLOW_PRCS");
      if ("1".equals(flowType)) {
        Statement stm4 = null;
        ResultSet rs4 = null;
        String prcsName = "流程步骤已删除";
        try {
          String queryStr = "SELECT PRCS_NAME from oa_fl_process WHERE FLOW_SEQ_ID="+ flowId +" AND PRCS_ID=" + flowPrcs; 
          stm4 = conn.createStatement();
          rs4 = stm4.executeQuery(queryStr);
          if (rs4.next()) {
            prcsName = rs4.getString("PRCS_NAME");
          }
        }catch(Exception ex) {
          throw ex;
        }finally {
          YHDBUtility.close(stm4 , rs4 , null);
        }
        sb.append(",prcsName:\"" + prcsName + "\"");
      } else {
        sb.append(",prcsName:\"自由流程\"");
      }
      sb.append(",flowType:" + flowType);
      sb.append(",prcsId:" + prcsId);
      int opFlag1 = (Integer)map.get("OP_FLAG");
      sb.append(",opFlag:" + opFlag1);
      sb.append(",flowPrcs:" + flowPrcs);
      sb.append(",freeOther:" +  (Integer)map.get("FREE_OTHER"));
      sb.append(",isHaveDelPriv:" + isHaveDelPriv);
      sb.append(",state:'" + (String)map.get("state") + "'");
      sb.append(",calBackPriv:" + calBackPriv);
      if (!YHUtility.isNullorEmpty(filedList)) {
        sb.append("," + this.getFieldValue(conn, runId, flowId, filedList , formId));
      }
      sb.append("},");
    }
    if( list.size() >0 ) {
      sb.deleteCharAt(sb.length() - 1); 
    }
    long date4 = System.currentTimeMillis();
    //long date5 = date4 - date2;
    sb.append("]");
    sb.append(",pageData:");
    sb.append("{");
    sb.append("pageCount:" + pageCount);
    sb.append(",recordCount:" + recordCount);
    sb.append(",pgStartRecord:" + pgStartRecord);
    sb.append(",pgEndRecord:" + pgEndRecord);
    sb.append("}");
    sb.append("}");
    return sb.toString();
  }
  public String getMyWorkList( YHPerson user , int pageIndex, String flowIdStr  , int showLen , String typeStr , String sortId , Connection conn , String filedList, String sortField) throws Exception{
    StringBuffer sb = new StringBuffer();
    Statement stm =null;
    ResultSet rs = null;//结果集    List<Map> list = new ArrayList();
    int pageCount = 0;//页码数

    int recordCount = 0;//总记录数
    int pgStartRecord = 0;//开始索引

    int pgEndRecord = 0;//结束索引
    int type = Integer.parseInt(typeStr);
    int userId = user.getSeqId();//用户ID
    try {
      Map nameMap = new HashMap();
      
      String query = "SELECT "
        + " a.SEQ_ID "
        + " ,a.RUN_ID "
        + " ,a.PRCS_ID "
        + " ,a.FLOW_PRCS "
        + " ,a.PRCS_FLAG "
        + " ,a.OP_FLAG "
        + " ,a.PRCS_TIME "
        + " ,a.CREATE_TIME "
        + " , a.DELIVER_TIME"
        + " ,b.FREE_OTHER "
        + " , b.SEQ_ID As FLOW_ID"
        + " ,b.FLOW_NAME "
        + " ,b.FLOW_TYPE "
        + " ,b.LIST_FLDS_STR "
        + " ,b.FORM_SEQ_ID "
        + " ,c.RUN_NAME "
        + " , p.USER_NAME"
        + " , c.BEGIN_USER as userId"
        + " , a.TIME_OUT_FLAG "
        + "  from oa_fl_run_prcs  a, oa_fl_type  b, oa_fl_run  c  , PERSON p , oa_fl_sort s"
        + " WHERE  "
        + " p.SEQ_ID = c.BEGIN_USER AND"
        + "  a.RUN_ID=c.RUN_ID and  "
        + "  c.FLOW_ID=b.SEQ_ID and  "
        + "  s.SEQ_ID=b.FLOW_SORT and  "
        + "  c.DEL_FLAG='0' and  "
        + " a.CHILD_RUN = 0 AND "
        + "  a.USER_ID=" + user.getSeqId()
        + "  and a.PRCS_FLAG='"+ typeStr +"' ";
      if (flowIdStr != null 
          && !"".equals(flowIdStr) 
          && !"0".equals(flowIdStr) ) {
        query += " and b.SEQ_ID = " + flowIdStr;
      }
      if (!YHUtility.isNullorEmpty(sortId)) {
        sortId = YHWorkFlowUtility.getOutOfTail(sortId);
        query += " and s.SEQ_ID IN (" + sortId + ")";
      }
      
      query += " order by a.RUN_ID  "+ sortField +",a.PRCS_TIME DESC";
      
      long date1 = System.currentTimeMillis();
      stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      rs = stm.executeQuery(query);
      long date2 = System.currentTimeMillis();
      long date3 = date2 - date1;
      rs.last(); 
      recordCount = rs.getRow(); //总记录数 
       //总页数 
      pageCount = recordCount / showLen; 
      if (recordCount % showLen != 0) { 
        pageCount++; 
      } 
      if (pageIndex < 1) { 
        pageIndex = 1; 
      } 
      if (pageIndex > pageCount) { 
        pageIndex = pageCount; 
      } 
     //开始索引      pgStartRecord = (pageIndex - 1) * showLen + 1;
      rs.absolute(pgStartRecord); 
      sb.append("{");
      sb.append("listData:[");
      for (int i = 0; i < showLen && !rs.isAfterLast()&&recordCount > 0; i++) { 
        Map mapTmp = new HashMap();
        int seqId = rs.getInt("SEQ_ID");
        mapTmp.put("SEQ_ID", String.valueOf(seqId));
        mapTmp.put("RUN_ID", rs.getInt("RUN_ID"));
        mapTmp.put("FLOW_ID", rs.getInt("FLOW_ID"));
        mapTmp.put("FLOW_NAME",  rs.getString("FLOW_NAME"));
        mapTmp.put("RUN_NAME", rs.getString("RUN_NAME"));
        mapTmp.put("userId", rs.getInt("userId"));
        mapTmp.put("PRCS_ID", rs.getInt("PRCS_ID"));
        mapTmp.put("FLOW_TYPE",rs.getString("FLOW_TYPE"));
        mapTmp.put("FREE_OTHER", rs.getInt("FREE_OTHER"));
        mapTmp.put("USER_NAME", rs.getString("USER_NAME"));
        mapTmp.put("FLOW_PRCS",  rs.getInt("FLOW_PRCS"));
        mapTmp.put("OP_FLAG",  rs.getInt("OP_FLAG"));
        mapTmp.put("FORM_SEQ_ID", rs.getInt("FORM_SEQ_ID"));
        mapTmp.put("TIME_OUT_FLAG", rs.getString("TIME_OUT_FLAG"));
        mapTmp.put("PRCS_TIME", rs.getTimestamp("PRCS_TIME"));
        mapTmp.put("CREATE_TIME", rs.getTimestamp("CREATE_TIME"));
        mapTmp.put("DELIVER_TIME", rs.getTimestamp("DELIVER_TIME"));
        list.add(mapTmp);
        rs.next(); 
      } 
       //结束索引
      pgEndRecord =(pageIndex - 1) * showLen + list.size();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);;
    }
    for (Map map : list) {
      int runId = (Integer) map.get("RUN_ID");
      int prcsId = (Integer) map.get("PRCS_ID");
      int flowId = (Integer)  map.get("FLOW_ID");
      int flowPrcs = (Integer)  map.get("FLOW_PRCS") ; 
      int opFlag =  (Integer)  map.get("OP_FLAG");
      int FORM_SEQ_ID =  (Integer)  map.get("FORM_SEQ_ID");
      String timeOutFlag = (String)map.get("TIME_OUT_FLAG");
      boolean isHaveDelPriv = this.isHaveDelPriv(runId, user, conn);
      sb.append("{");
      sb.append("seqId:" +  map.get("SEQ_ID"));
      sb.append(",runId:" + runId);
      sb.append(",flowId:" + flowId);
      sb.append(",flowName:\"" +  map.get("FLOW_NAME") + "\"");
      String runName = (String)map.get("RUN_NAME");
      runName = YHWorkFlowUtility.getRunName(runName);
      sb.append(",runName:'" +  runName + "'");
      
      int userSeqId =  (Integer)map.get("userId");
      sb.append(",userId:\"" + userSeqId + "\"");
      sb.append(",userName:\"" + (String)map.get("USER_NAME") + "\"");
      String flowType =  (String)map.get("FLOW_TYPE");
      String timeOutType = "";
      String timeOut = "";
      String timeExcept = "01"; 
      if ("1".equals(flowType)) {
        Statement stm4 = null;
        ResultSet rs4 = null;
        String prcsName = "流程步骤已删除";
        try {
          String queryStr = "SELECT PRCS_NAME , TIME_EXCEPT, TIME_OUT_TYPE , TIME_OUT from oa_fl_process WHERE FLOW_SEQ_ID="+ flowId +" AND PRCS_ID=" + flowPrcs; 
          stm4 = conn.createStatement();
          rs4 = stm4.executeQuery(queryStr);
          if (rs4.next()) {
            prcsName = rs4.getString("PRCS_NAME");
            timeOutType = rs4.getString("TIME_OUT_TYPE");
            timeOut = rs4.getString("TIME_OUT");
            timeExcept = rs4.getString("TIME_EXCEPT");
            if (timeExcept == null) {
              timeExcept = "01"; 
            }
          }
        }catch(Exception ex) {
          throw ex;
        }finally {
          YHDBUtility.close(stm4 , rs4 , null);
        }
        sb.append(",prcsName:\"" + prcsName + "\"");
      } else {
        sb.append(",prcsName:\"自由流程\"");
      }
      sb.append(",flowType:" + flowType);
      sb.append(",prcsId:" + prcsId);
      sb.append(",opFlag:" + opFlag);
      sb.append(",flowPrcs:" + flowPrcs);
      sb.append(",freeOther:" +  (Integer)map.get("FREE_OTHER"));
      sb.append(",isHaveDelPriv:" + isHaveDelPriv);
      
      Timestamp prcsTime = (Timestamp) map.get("PRCS_TIME");
      Timestamp createTime = (Timestamp) map.get("CREATE_TIME");
      //Timestamp deliverTime = (Timestamp) map.get("DELIVER_TIME");
      
      if ("1".equals(timeOutFlag)) {
        Date prcsBeginTime = null;
        if("0".equals(timeOutType)) {
          prcsBeginTime = prcsTime;
          if(prcsTime == null)
             prcsBeginTime = createTime;
        } else {
          prcsBeginTime = createTime;
        }
        
        if (prcsBeginTime == null) {
          prcsBeginTime = new Date();
        }
        String timeDesc = YHWorkFlowUtility.getTimeOut(timeOut, prcsBeginTime, new Date(), "dhms", timeExcept);
        if(!"".equals(timeDesc)){
          //timeStr = timeDesc;
          sb.append(",timeUse:'" + timeDesc + "'");
        }
      }
      if (!YHUtility.isNullorEmpty(filedList)) {
        sb.append("," + this.getFieldValue(conn, runId, flowId, filedList , FORM_SEQ_ID));
      }
      sb.append("},");
    }
//    long date4 = System.currentTimeMillis();
//    long date5 = date4 - date2;
    if( list.size() >0 ) {
      sb.deleteCharAt(sb.length() - 1); 
    }
    sb.append("]");
    sb.append(",pageData:");
    sb.append("{");
    sb.append("pageCount:" + pageCount);
    sb.append(",recordCount:" + recordCount);
    sb.append(",pgStartRecord:" + pgStartRecord);
    sb.append(",pgEndRecord:" + pgEndRecord);
    sb.append("}");
    sb.append("}");
    return sb.toString();
  }
  public String getFieldValue(Connection conn , int runId , int flowId , String fields , int formId) throws Exception {
    fields = YHWorkFlowUtility.getInStr(fields);
    if ("".equals(fields)) {
      return "";
    }
    YHFlowRunUtility ut = new YHFlowRunUtility();
    Map map = ut.getRunData(runId, conn, formId, flowId, fields);
    StringBuffer sb = new StringBuffer();
    int count = 0 ;
    Set<String> keys = map.keySet();
    for (String key : keys) {
      if (!YHUtility.isNullorEmpty(key)) {
        String value = YHUtility.encodeSpecial(YHUtility.null2Empty((String)map.get(key)));
        String k =  YHUtility.encodeSpecial(key);
        sb.append("\"" + k + "\":\"" + value + "\",");
        count++;
      }
    }
    if (count > 0 ) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return sb.toString();
  }
  /**
   *
   * @param user 用户名
   * @param flowId 流程id
   * @param showLen 取出列表最大长度
   * @param type 查找的类型  1 - 未接收 ,2- 办理中, 3, 已办结
   * @param pageIndex 页码
   * @return page
   */
  /**
   *返加数据格式:{pageData:{pageCount:20 , recordCount : 100, pgStartRecord : 1 , pgEndRecord:40}
   * ,listData:[{seqId:1,runId:1, flowId:2, flowName :'111' , runName :'ddd' , userId:'ddd' , userName :'sss' , prcsId:1 , prcsName:'aaa' ,flowPrcs:4}
   * ,{seqId:1,runId:1, flowId:2, flowName :'111' , runName :'ddd' , userId:'ddd' , userName :'sss' , prcsId:1 , prcsName:'aaa' ,flowPrcs:4}
   * ,{seqId:1,runId:1, flowId:2, flowName :'111' , runName :'ddd' , userId:'ddd' , userName :'sss' , prcsId:1 , prcsName:'aaa' ,flowPrcs:4}
   * ]}
   */
  public String getMyWorkList1( YHPerson user , int pageIndex, String flowIdStr , int showLen , String typeStr , Connection conn) throws Exception{
   // YHORM 
    StringBuffer sb  = new StringBuffer();
    Statement stmt =null;
    ResultSet rs = null;//结果集    try {
      int pageCount = 0;//页码数      int recordCount = 0;//总记录数
      int pgStartRecord = 0;//开始索引      int pgEndRecord = 0;//结束索引
      int type = Integer.parseInt(typeStr);
      int userId = user.getSeqId();//用户ID
      String userName = user.getUserName();//用户名称
      List flowRunPrcsIdList = new ArrayList();
      sb.append("{");
      sb.append("listData:[");
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
      String queryFlowRunPrcsSql ="select r.FLOW_ID"
        + ",f.FLOW_PRCS"
        + ",r.RUN_NAME"
        + ",p.PRCS_NAME"
        + ",f.PRCS_ID"
        + ",r.RUN_ID"
        + ",t.FLOW_NAME"
        + ",f.SEQ_ID"
        + ",u.USER_NAME "
        + ", u.SEQ_ID AS userId "
        + ",f.OP_FLAG" 
        + ", t.FLOW_TYPE"
        + ", t.FREE_OTHER"
        + " from oa_fl_type t,oa_fl_run r,oa_fl_run_prcs f,oa_fl_process p , PERSON u where "
        + " t.SEQ_ID=r.FLOW_ID "
        + " and t.SEQ_ID=r.FLOW_ID"
        + " and f.RUN_ID=r.RUN_ID "
        + " AND f.CHILD_RUN = 0 "
        + " and ((t.FLOW_TYPE = '1' AND t.SEQ_ID=p.FLOW_SEQ_ID  AND f.FLOW_PRCS=p.PRCS_ID) OR t.FLOW_TYPE = '2' )"
        + " and f.PRCS_FLAG='"+ type +"' "
        + " and f.USER_ID="+ userId 
        + " and r.DEL_FLAG<>'1'" 
        + " and r.BEGIN_USER = u.SEQ_ID ";
      if (!"".equals(flowIdStr) && flowIdStr != null && !"0".equals(flowIdStr)) {
        queryFlowRunPrcsSql = queryFlowRunPrcsSql + " and t.SEQ_ID='" + Integer.parseInt(flowIdStr)+ "'";
      }
      queryFlowRunPrcsSql += " order by r.RUN_ID desc";
      rs = stmt.executeQuery(queryFlowRunPrcsSql); 
      List<Map> list = new ArrayList();
      
      String seqIdStr = "";
      while (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        if (!YHWorkFlowUtility.findId(seqIdStr, String.valueOf(seqId))) {
          seqIdStr += rs.getInt("SEQ_ID") + ",";
          Map mapTmp = new HashMap();
          mapTmp.put("SEQ_ID", String.valueOf(seqId));
          mapTmp.put("RUN_ID", rs.getInt("RUN_ID"));
          mapTmp.put("FLOW_ID", String.valueOf(rs.getInt("FLOW_ID")));
          mapTmp.put("FLOW_NAME",  rs.getString("FLOW_NAME"));
          mapTmp.put("RUN_NAME", rs.getString("RUN_NAME"));
          mapTmp.put("userId", rs.getInt("userId"));
          mapTmp.put("USER_NAME", rs.getString("USER_NAME"));
          mapTmp.put("PRCS_ID", String.valueOf(rs.getInt("PRCS_ID")));
          mapTmp.put("OP_FLAG",  rs.getString("OP_FLAG"));
          if ("1".equals(rs.getString("FLOW_TYPE"))) {
            mapTmp.put("PRCS_NAME", rs.getString("PRCS_NAME"));
            mapTmp.put("FLOW_TYPE", "1");
          } else {
            mapTmp.put("PRCS_NAME", "自由流程");
            mapTmp.put("FLOW_TYPE", "2");
          }
          mapTmp.put("FREE_OTHER", rs.getString("FREE_OTHER"));
          mapTmp.put("FLOW_PRCS",  String.valueOf(rs.getInt("FLOW_PRCS")));
          list.add(mapTmp);
        }
      }
      recordCount = list.size(); 
       //总页数 
      pageCount = recordCount / showLen; 
      if (recordCount % showLen != 0) { 
        pageCount++; 
      } 
      if (pageIndex < 1) { 
        pageIndex = 1; 
      } 
      if (pageIndex > pageCount) { 
        pageIndex = pageCount; 
      } 
      //开始索引
      pgStartRecord = (pageIndex - 1) * showLen + 1;
      int count = 0 ;
      int i = pgStartRecord ;
      if (i <= 0) {
        i = 1 ;
      }
      for (;i < pgStartRecord + showLen && i <= list.size() ;i ++) {
        Map tmpMap = list.get(i - 1);
        count ++ ;
        int runId = (Integer) tmpMap.get("RUN_ID");
        boolean isHaveDelPriv = this.isHaveDelPriv(runId, user, conn);
        sb.append("{");
        sb.append("seqId:" + tmpMap.get("SEQ_ID"));
        sb.append(",runId:" + runId);
        sb.append(",flowId:" + tmpMap.get("FLOW_ID"));
        sb.append(",flowName:\"" +tmpMap.get("FLOW_NAME") + "\"");
        sb.append(",runName:\"" + tmpMap.get("RUN_NAME") + "\"");
        sb.append(",userId:\"" + tmpMap.get("userId") + "\"");
        sb.append(",userName:\"" + tmpMap.get("USER_NAME") + "\"");
        sb.append(",prcsId:" + tmpMap.get("PRCS_ID"));
        sb.append(",opFlag:" + tmpMap.get("OP_FLAG"));
        sb.append(",state:'" + tmpMap.get("state") + "'");
        sb.append(",prcsName:\"" + tmpMap.get("PRCS_NAME") + "\"");
        sb.append(",flowPrcs:" + tmpMap.get("FLOW_PRCS"));
        sb.append(",flowType:" + tmpMap.get("FLOW_TYPE"));
        sb.append(",freeOther:" + tmpMap.get("FREE_OTHER"));
        sb.append(",isHaveDelPriv:" + isHaveDelPriv);
        sb.append("},");
      } 
       //结束索引
      pgEndRecord =(pageIndex - 1) * showLen + count;
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1); 
      }
      sb.append("]");
      sb.append(",pageData:");
      sb.append("{");
      sb.append("pageCount:" + pageCount);
      sb.append(",recordCount:" + recordCount);
      sb.append(",pgStartRecord:" + pgStartRecord);
      sb.append(",pgEndRecord:" + pgEndRecord);
      sb.append("}");
      sb.append("}");
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null); 
    }
    return sb.toString();
  }
  public String getEndWorkList( YHPerson user,
       int pageIndex, String flowIdStr, int showLen, String opFlag , String sortId, Connection conn) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb  = new StringBuffer();
    Statement stm =null;
    ResultSet rs = null;//结果集    try {
      int pageCount = 0;//页码数      int recordCount = 0;//总记录数
      int pgStartRecord = 0;//开始索引      int pgEndRecord = 0;//结束索引
      int userId = user.getSeqId();//用户ID
      List flowRunPrcsIdList = new ArrayList();
      sb.append("{");
      sb.append("listData:[");
      
      String tmp = "";
      if(opFlag != null && ("1".equals(opFlag) || "0".equals(opFlag))){
        tmp = " and f.OP_FLAG = '"+ opFlag +"' ";
      }
      stm = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
      String queryFlowRunPrcsSql ="select r.FLOW_ID"
        + ",f.FLOW_PRCS"
        + ",r.RUN_NAME"
        + ",p.PRCS_NAME"
        + ",f.PRCS_ID"
        + ",r.RUN_ID"
        + ",t.FLOW_NAME"
        + ",f.SEQ_ID"
        + ",f.OP_FLAG"
        + ",r.END_TIME"
        + ",u.USER_NAME"
        + ",u.SEQ_ID as userId" 
        + ",t.FLOW_TYPE"
        + " from oa_fl_type t,oa_fl_run r,oa_fl_run_prcs f,oa_fl_process p, PERSON u , oa_fl_sort s where "
        + " t.SEQ_ID=r.FLOW_ID "
        + " AND f.CHILD_RUN = 0 "
        + " and s.SEQ_ID=t.FLOW_SORT "
        + " and ((t.FLOW_TYPE = '1' AND t.SEQ_ID=p.FLOW_SEQ_ID  AND f.FLOW_PRCS=p.PRCS_ID) OR t.FLOW_TYPE = '2' )"
        + " and r.BEGIN_USER = u.SEQ_ID "
        + " and f.RUN_ID=r.RUN_ID "
        + " and r.DEL_FLAG<>'1'" 
        + " and  ( f.PRCS_FLAG='3' or f.PRCS_FLAG = '4' ) "
        + tmp 
        + " and f.USER_ID="+ userId ;
      if(YHUtility.isInteger(flowIdStr) && !"0".equals(flowIdStr)) {
        queryFlowRunPrcsSql = queryFlowRunPrcsSql + " and t.SEQ_ID='" + Integer.parseInt(flowIdStr)+ "'";
      }
      if (!YHUtility.isNullorEmpty(sortId)) {
        sortId = YHWorkFlowUtility.getOutOfTail(sortId);
        queryFlowRunPrcsSql += " and s.SEQ_ID IN (" + sortId + ")";
      }
      queryFlowRunPrcsSql += " order by r.RUN_ID desc , f.PRCS_ID desc";
      rs = stm.executeQuery(queryFlowRunPrcsSql); 
      List<Map> list = new ArrayList();
      String seqIdStr = "";
      String runIdStr = "";
      while (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        int runId = rs.getInt("RUN_ID");
        if (!YHWorkFlowUtility.findId(seqIdStr, String.valueOf(seqId))
            && !YHWorkFlowUtility.findId(runIdStr, String.valueOf(runId))) {
          seqIdStr += rs.getInt("SEQ_ID") + ",";
          runIdStr += runId + ",";
          
          Date date = rs.getDate("END_TIME");
          String state = "办理中";
          if(date != null){
            state = "已结束";
          }
          Map mapTmp = new HashMap();
          mapTmp.put("SEQ_ID", String.valueOf(seqId));
          mapTmp.put("RUN_ID", runId);
          mapTmp.put("FLOW_ID", String.valueOf(rs.getInt("FLOW_ID")));
          mapTmp.put("FLOW_NAME",  rs.getString("FLOW_NAME"));
          mapTmp.put("RUN_NAME", rs.getString("RUN_NAME"));
          mapTmp.put("userId", String.valueOf(rs.getInt("userId")));
          mapTmp.put("USER_NAME", rs.getString("USER_NAME"));
          mapTmp.put("PRCS_ID", rs.getInt("PRCS_ID"));
          mapTmp.put("OP_FLAG",  rs.getString("OP_FLAG"));
          mapTmp.put("state",  state);
          mapTmp.put("PRCS_NAME", rs.getString("PRCS_NAME"));
          mapTmp.put("FLOW_PRCS",  String.valueOf(rs.getInt("FLOW_PRCS")));
          mapTmp.put("FLOW_TYPE", rs.getString("FLOW_TYPE"));
          list.add(mapTmp);
        }
      }
     // rs.last(); 
     // recordCount = rs.getRow(); //总记录数 
       recordCount = list.size(); 
      //总页数 
      pageCount = recordCount / showLen; 
      if (recordCount % showLen != 0) { 
        pageCount++; 
      } 
      if (pageIndex < 1) { 
        pageIndex = 1; 
      } 
      if (pageIndex > pageCount) { 
        pageIndex = pageCount; 
      } 
       //开始索引       pgStartRecord = (pageIndex - 1) * showLen + 1;
     
       //rs.absolute( (pageIndex - 1) * showLen + 1); 
       int i = pgStartRecord ;
       if (i <= 0) {
         i = 1 ;
       }
       int count = 0 ;
       //for (int i = 0; i < showLen && !rs.isAfterLast()&&recordCount > 0; i++) { 
       for ( ;i < pgStartRecord + showLen && i <= list.size() ;i ++) {
         Map tmpMap = list.get(i - 1);
         count ++ ;
         int runId = (Integer) tmpMap.get("RUN_ID");
         boolean isHaveDelPriv = this.isHaveDelPriv(runId, user, conn);
         int prcsId = (Integer) tmpMap.get("PRCS_ID");
         boolean calBackPriv = this.callBackPriv(runId , prcsId, user, conn);
         
         sb.append("{");
         sb.append("seqId:" + tmpMap.get("SEQ_ID"));
         sb.append(",runId:" + tmpMap.get("RUN_ID"));
         sb.append(",flowId:" + tmpMap.get("FLOW_ID"));
         sb.append(",flowName:\"" +tmpMap.get("FLOW_NAME") + "\"");
         String runName = (String)tmpMap.get("RUN_NAME");
         runName = YHWorkFlowUtility.getRunName(runName);
         sb.append(",runName:'" + runName + "'");
         sb.append(",userId:\"" + tmpMap.get("userId") + "\"");
         sb.append(",userName:\"" + tmpMap.get("USER_NAME") + "\"");
         sb.append(",prcsId:" + prcsId);
         sb.append(",opFlag:" + tmpMap.get("OP_FLAG"));
         sb.append(",state:'" + tmpMap.get("state") + "'");
         String prcsName = (String)tmpMap.get("PRCS_NAME");
         String flowType = (String)tmpMap.get("FLOW_TYPE");
         if ("2".equals(flowType)) {
           prcsName = "自由流程";
         }
         sb.append(",prcsName:\"" + prcsName + "\"");
         sb.append(",flowPrcs:" + tmpMap.get("FLOW_PRCS"));
         sb.append(",flowType:" + tmpMap.get("FLOW_TYPE"));
         sb.append(",isHaveDelPriv:" + isHaveDelPriv);
         sb.append(",calBackPriv:" + calBackPriv);
         sb.append("},");
       } 
         //结束索引
       pgEndRecord =(pageIndex - 1) * showLen + count;
       if (count > 0 ) {
         sb.deleteCharAt(sb.length() - 1); 
       }
       sb.append("]");
       sb.append(",pageData:");
       sb.append("{");
       sb.append("pageCount:" + pageCount);
       sb.append(",recordCount:" + recordCount);
       sb.append(",pgStartRecord:" + pgStartRecord);
       sb.append(",pgEndRecord:" + pgEndRecord);
       sb.append("}");
       sb.append("}");
     } catch (Exception ex) {
       throw ex;
     } finally {
       YHDBUtility.close(stm, rs, null); 
     }
     return sb.toString();
  }
  /**
   * 取得工作信息
   * @param flowId
   * @param runId
   * @return
   * @throws Exception 
   */
  public String getWorkMsg(int flowId , int runId , Connection conn) throws Exception{
    StringBuffer sb = new StringBuffer();
    YHFlowTypeLogic flowLogic = new YHFlowTypeLogic();
    YHFlowType flowType = flowLogic.getFlowTypeById(flowId , conn);
    YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
    YHFlowRun flowRun = flowRunLogic.getFlowRunByRunId(runId ,conn);
    sb.append("{");
    String runName = flowRun.getRunName();
    runName = YHWorkFlowUtility.getRunName(runName);
    sb.append("runName:'" + runName + "'");
    sb.append(",flowName:'" + flowType.getFlowName() + "'");
    sb.append(",flowType:" + flowType.getFlowType());
    sb.append("}");
    return sb.toString();
  }
  /**
   * 取得实际步骤的列表
   * @param flowId
   * @param runId
   * @return
   * @throws Exception 
   */
  //{prcsId:1,prcsName:'开始',flowPrcs:1,prcsTitle:'第1步.开始(刘涵)',leftVml:'21',topVml:'85',prcsTo:'2',state:4
  //,user:[{userName:'ddd',state:4,beginTime:'',endTime:'',timeCount:'timeCount',isOp:true}
  public String getPrcsListMsg(int flowId, int runId , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    YHFlowTypeLogic flowLogic = new YHFlowTypeLogic();
    YHFlowType flowType = flowLogic.getFlowTypeById(flowId , conn);
    YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
    YHFlowRun flowRun = flowRunLogic.getFlowRunByRunId(runId ,conn);
    boolean isEnd = false;
    if(flowRun.getEndTime() != null && !"".equals(flowRun.getEndTime()) ){
      isEnd = true;
    }
    sb.append("runMsg:{");
    String runName = flowRun.getRunName();
    runName = YHWorkFlowUtility.getRunName(runName);
    sb.append("runName:'" + runName + "'");
    sb.append(",flowName:'" + flowType.getFlowName() + "'");
    sb.append(",flowType:" + flowType.getFlowType());
    sb.append(",isEnd:" + isEnd);
    sb.append("},");
    this.getPrcsList(runId, flowType , conn , sb);
    sb.append("}");
    return sb.toString();
  }
  /**
   * 取得实际步骤的列表

   * @param flowId
   * @param runId
   * @return
   * @throws Exception 
   */
  //{prcsId:1,prcsName:'开始',flowPrcs:1,prcsTitle:'第1步.开始(刘涵)',leftVml:'21',topVml:'85',prcsTo:'2',state:4
  //,user:[{userName:'ddd',state:4,beginTime:'',endTime:'',timeCount:'timeCount',isOp:true}
  public Map getPrcsListMsg1(int flowId, int runId , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    Map map = new HashMap();
    YHFlowTypeLogic flowLogic = new YHFlowTypeLogic();
    YHFlowType flowType = flowLogic.getFlowTypeById(flowId , conn);
    YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
    YHFlowRun flowRun = flowRunLogic.getFlowRunByRunId(runId ,conn);
    boolean isEnd = false;
    if(flowRun.getEndTime() != null && !"".equals(flowRun.getEndTime()) ){
      isEnd = true;
    }
    String runName = flowRun.getRunName();
    runName = YHWorkFlowUtility.getRunName(runName);
    map.put("runName", runName);
    map.put("flowName", flowType.getFlowName());
    map.put("flowType", flowType.getFlowType());
    map.put("isEnd", isEnd);
    List<Map> list = new ArrayList();
    String timeToId = this.getPrcsList(runId, flowType , conn ,list );
    map.put("prcsList", list);
    map.put("timeToId" ,timeToId);
    return map;
  }
  /**
   * 取得日志信息列表
   * @param flowId
   * @param runId
   * @return
   * @throws Exception 
   */
  //[{prcsId:1,prcsName:'aaaaa',userName:'刘涵',time:'2008-01-21 23:09:11',content:'ddd'}];
  public String getLogList(int flowId, int runId , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer("{");
    YHFlowTypeLogic typeLogic = new YHFlowTypeLogic();
    YHFlowType flowType = typeLogic.getFlowTypeById(flowId , conn);
    boolean flag =  true;//默认为固定流程
    if("2".equals(flowType.getFlowType())){
      flag = false;//为自流程
    }
    sb.append("flag:" + flag + ",logList:[");
    String query = "select "
      + " USER_NAME"
      + ",PRCS_ID"
      + ",CONTENT"
      + ",FLOW_PRCS"
      + ",TIME AS DATETIME"
      + " FROM oa_fl_run_log as FLOW_RUN_LOG,PERSON WHERE RUN_ID='"+ runId +"' and FLOW_RUN_LOG.USER_ID =  PERSON.SEQ_ID ORDER BY PRCS_ID Desc,TIME Desc,FLOW_PRCS Desc";
    int count = 0 ;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while(rs.next()){
        String userName = rs.getString("USER_NAME");
        int prcsId = rs.getInt("PRCS_ID");
        String content = rs.getString("CONTENT");
        Timestamp tTime = rs.getTimestamp("DATETIME");
        String time = YHUtility.getDateTimeStr(tTime);
        String prcsName = "-";
        int flowPrcs = rs.getInt("FLOW_PRCS");
        //固定流程
        if(flag){
          query = "select PRCS_NAME from oa_fl_process where FLOW_SEQ_ID = " + flowId + " and PRCS_ID=" + flowPrcs;
          Statement stm1 = null;
          ResultSet rs1 = null;
          try {
            stm1 = conn.createStatement();
            rs1 = stm1.executeQuery(query);
            if(rs1.next()){
              prcsName = rs1.getString("PRCS_NAME");
            } else {
              prcsName = "<font color=red>流程步骤已删除</font>";
            }
          } catch(Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm1, rs1, null); 
          }
        }
        sb.append("{");
        sb.append("prcsId:" + prcsId);
        sb.append(",prcsName:'" + prcsName + "'");
        sb.append(",userName:'" + userName + "'");
        sb.append(",time:'" + time + "'");
        sb.append(",content:'" + content + "'");
        sb.append("},");
        count++ ;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    if(count > 0 ){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]}");
    return sb.toString();
  }
  /**
   * 取得流程列表
   * @param runId
   * @param flowId
   * @return
   * @throws Exception
   */
  public String getPrcsList(int runId , int flowId  , Connection conn) throws Exception{
    YHFlowTypeLogic flowLogic = new YHFlowTypeLogic();
    YHFlowType flowType = flowLogic.getFlowTypeById(flowId , conn);
    StringBuffer sb = new StringBuffer();
    this.getPrcsList(runId, flowType , conn , sb);
    return sb.toString();
  }
  public int getMax(Connection conn , String sql) throws Exception {
    Statement stm = null;
    ResultSet rs = null;
    int max = 0 ;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(sql);
      if(rs.next()){
        max = rs.getInt(1);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return max;
  }
  /**
   * 取得json形式流程列表
   * @param runId
   * @param flowId
   * @return
   * @throws Exception
   */
  public void getPrcsList(int runId , YHFlowType flowType , Connection conn , StringBuffer sb) throws Exception{
    YHORM orm = new YHORM();
    String timeToId = "";
    String queryMax = "SELECT MAX(PRCS_ID) as max "
      + " from oa_fl_run_prcs where  "
      + " RUN_ID=" + runId;
    int  prcsMax = this.getMax(conn, queryMax);
   // String queryMax2 = "SELECT MAX(PRCS_ID) from FLOW_RUN_PRCS where RUN_ID='" 
    //  + runId +"' and PRCS_FLAG<>'5'";
    ///int prcsMax2 = this.getMax(conn, queryMax2);
    int topAuto = 70;
    int leftAuto = 10;
    sb.append("prcsList:[");
    for(int i = 1 ; i <= prcsMax ;i ++){
      sb.append("{");
      sb.append("prcsId:" + i + ",list:[");
      String query = "SELECT * from oa_fl_run_prcs where "
      + "  RUN_ID="+ runId +"  "
      + " and PRCS_ID=" + i + " order by FLOW_PRCS";
      int flowPrcs = 0;
      YHFlowProcess fp = null;
      Statement stm2 = null;
      ResultSet rs2 = null;
      String ss = "";
      int count = 0;
      try {
        stm2 = conn.createStatement();
        rs2 = stm2.executeQuery(query);
        // 获取步骤信息
        while (rs2.next()){
          flowPrcs = rs2.getInt("FLOW_PRCS");
          if (YHWorkFlowUtility.findId(ss, flowPrcs + "")) {
            continue;
          }
          count++;
          ss += flowPrcs + ",";
          String prcsName = "";
          String timeOut = "";
          String except = "00";
          if(flowType != null 
              && "1".equals(flowType.getFlowType())){
            YHFlowProcessLogic logic = new YHFlowProcessLogic();
            fp = logic.getFlowProcessById(flowType.getSeqId(), String.valueOf(flowPrcs) , conn);
            timeOut = fp.getTimeOut();
            except = fp.getTimeExcept();
            if(fp != null){
              prcsName = fp.getPrcsName();
            }else{
              prcsName = "<font color=red>流程步骤已删除</font>";
            }
          }
          String parent = rs2.getString("PARENT");
          int childRun = rs2.getInt("CHILD_RUN");
          
          int childFlowId = 0 ;
          if (childRun != 0) {
            YHFlowRunUtility u = new YHFlowRunUtility();
            childFlowId = u.getFlowId(conn, childRun);
          }
          sb.append("{prcsName:'" + prcsName + "',flowPrcs:" + flowPrcs + ",sourceId:'"+ i +"_" + flowPrcs +"_" + parent + "',childRun:'" + childRun + "', childFlowId:'" + childFlowId + "'");
          //---------- while2 获得此步骤、此序号的办理信息 ---------------
          int prcsState = 0;
          String opUserName = "";
          //$query1 = "SELECT * from FLOW_RUN_PRCS where RUN_ID=$RUN_ID and PRCS_ID=$PRCS_ID_I and FLOW_PRCS='$FLOW_PRCS' AND PARENT='$PARENT' order by OP_FLAG,PRCS_FLAG DESC,PRCS_TIME";
          Map map = new HashMap();
          map.put("RUN_ID", runId);
          map.put("PRCS_ID", i);
          map.put("FLOW_PRCS", flowPrcs);
          //map.put("PARENT", parent);//暂不考虑父流程
          List<YHFlowRunPrcs> list = orm.loadListSingle(conn, YHFlowRunPrcs.class, map);
          StringBuffer sb2 = new StringBuffer();
          sb2.append("[");
          for(YHFlowRunPrcs flowRunPrcs : list){
            sb2.append("{");
            //处理办理人信息
            String queryPerson = "SELECT u.USER_NAME "
              + " ,d.DEPT_NAME  "
              + " from PERSON u,oa_department d where  "
              + " u.SEQ_ID="+ flowRunPrcs.getUserId()
              + " AND d.SEQ_ID = u.DEPT_ID";
            String userName = "";
            Statement stm4 = null;
            ResultSet rs4 = null;
            try {
              stm4 = conn.createStatement();
              rs4 = stm4.executeQuery(queryPerson);
              if(rs4.next()){
                userName = rs4.getString("USER_NAME");
                sb2.append("userName:'" + userName + "'");
                sb2.append(",deptName:'" + rs4.getString("DEPT_NAME") + "'");
              } else {
                sb2.append("userName:'此人员已删除'");
                sb2.append(",deptName:'此人员已删除'");
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm4, rs4, null); 
            }
            //true-我主办的，false-我会签
            boolean isOp = false;
            //未接收中
            int prcsFlag = 0 ;
            if(flowRunPrcs.getPrcsFlag() != null){
              prcsFlag = Integer.parseInt(flowRunPrcs.getPrcsFlag());
              prcsState = prcsFlag;
            }
            //主办的
            if("1".equals(flowRunPrcs.getOpFlag())){
              isOp = true;
              opUserName = userName;
              prcsState = prcsFlag;
            }
            if("2".equals(flowRunPrcs.getTopFlag())){
              opUserName = "无主办人会签";
              if(prcsState == 0){
                prcsState = prcsFlag;
              }else if(prcsFlag < prcsState){
                prcsState = prcsFlag;
              }
            }
            sb2.append(",isOp:" + isOp);
            //计算用户用时
            long timeUsed = 0 ;
            if( flowRunPrcs.getPrcsTime() != null){
              if(prcsFlag == 1){
              }else if(prcsFlag == 2){
                Date date = new Date();
                timeUsed = date.getTime() - flowRunPrcs.getPrcsTime().getTime();
              }else{
                if(flowRunPrcs.getDeliverTime() != null){
                  timeUsed = flowRunPrcs.getDeliverTime().getTime() - flowRunPrcs.getPrcsTime().getTime();
                }
              }
            }
            String timeStr = "";
            long day=timeUsed/(24*60*60*1000); 
            long hour=(timeUsed/(60*60*1000)-day*24); 
            long min=((timeUsed/(60*1000))-day*24*60-hour*60); 
            long s=(timeUsed/1000-day*24*60*60-hour*60*60-min*60);
            
            if ( day > 0 ) {
              timeStr = day + "天";
            }
            if ( hour>0){
              timeStr +=hour + "时";
            }
            if(min>0){
              timeStr +=min + "分";
            }
            if(s>0){
              timeStr +=s + "秒";
            }
            //-- 超时信息 --  
            int timeOutFlag = 0 ;
            Date beginTime = flowRunPrcs.getPrcsTime();
            if((prcsFlag == 2 || prcsFlag == 1) 
                && !"".equals(timeOut) 
                && timeOut != null){
              if(timeOutFlag == 0){
                if(flowRunPrcs.getPrcsTime() != null){
                  beginTime = flowRunPrcs.getPrcsTime();
                }else{
                  beginTime = flowRunPrcs.getCreateTime();
                }
              }else{
                beginTime = flowRunPrcs.getCreateTime();
              }
              //如果不是自由流程
              if ("1".equals(flowType.getFlowType())) {
                String timeDesc = YHWorkFlowUtility.getTimeOut(timeOut, beginTime, new Date(), "dhms", except);
                //超时
                if(!"".equals(timeDesc)){
                  timeStr = timeDesc;
                  timeOutFlag = 1;
                }
              }
            }
            if(!"".equals(timeStr)){
              sb2.append(",timeUsed:'" + timeStr + "'");
            } else {
              sb2.append(",timeUsed:'0'");
            }
            //记录未接收或者超时用户

            if (prcsFlag == 1 || timeOutFlag == 1) {
              timeToId += flowRunPrcs.getUserId() + ",";
            }
            if (beginTime != null) {
              sb2.append(",beginTime:'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beginTime) + "'");
            } else {
              sb2.append(",beginTime:''");
            }
            if(flowRunPrcs.getDeliverTime() != null){
              sb2.append(",deliverTime:'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flowRunPrcs.getDeliverTime()) + "'");
            } else {
              sb2.append(",deliverTime:''");
            }
            sb2.append(",state:" + flowRunPrcs.getPrcsFlag());
            if(flowRunPrcs.getCreateTime() != null){
              sb2.append(",createTime:'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flowRunPrcs.getCreateTime()) + "'");
            } else {
              sb2.append(",createTime:''");
            }
            sb2.append("},");
          }
          if(list.size() > 0){
            sb2.deleteCharAt(sb2.length() - 1);
          }
          sb2.append("]");
          
          sb.append(",state:" + prcsState);
          String prcsTitle = "第" + i + "步." + prcsName + "(" + opUserName + ")";
          sb.append(",prcsTitle:'" + prcsTitle + "'");
          //不是实际流程最后一步          if(i != prcsMax ){
            String query2 = "select FLOW_PRCS  , PARENT FROM oa_fl_run_prcs where " 
              + " RUN_ID = " + runId  
              + " and  PRCS_ID =" + (i + 1) ;
            if ("1".equals(flowType.getFlowType()) ) {
              query2 += " and " + YHDBUtility.findInSet(flowPrcs + "", "PARENT");
            }
            Statement stm8 = null;
            ResultSet rs8 = null;
            sb.append(",prcsTo:[");
            try {
              stm8 = conn.createStatement();
              rs8 = stm8.executeQuery(query2);
              int countTmp = 0;
              String ss2 = "";
              while (rs8.next()){
                int flowPrcs1 = rs8.getInt("FLOW_PRCS");
                String parent1 = rs8.getString("PARENT");
                String ids = (i+1) + "_" + flowPrcs1 + "_" + parent1;
                if (YHWorkFlowUtility.findId(ss2, ids)) {
                  continue;
                }
                countTmp++;
                ss2 += ids + ",";
                sb.append("'" + ids + "',");
              }
              if (countTmp > 0) {
                sb.deleteCharAt(sb.length() - 1);
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm8, rs8, null); 
            }
            sb.append("]");
          } else {
            sb.append(",prcsTo:[]");
          }
          sb.append(",leftVml:'" + leftAuto + "'");
          sb.append(",topVml:'" + topAuto + "'");
          if (count > 0) {
            topAuto += 70;
          }
          sb.append(",user:" + sb2.toString() + "},");
        }
        leftAuto += 150;
        topAuto = 70;
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
      if (count > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]},");
    } 
    if(prcsMax > 0 ){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    if (!"".equals(timeToId)) {
      sb.append(",timeToId:'" + timeToId + "'");
    }
  }
  /**
   * 取得json形式流程列表
   * @param runId
   * @param flowId
   * @return
   * @throws Exception
   */
  public String getPrcsList(int runId , YHFlowType flowType , Connection conn ,   List<Map> list) throws Exception{
    YHORM orm = new YHORM();
    String timeToId = "";
    String queryMax = "SELECT MAX(PRCS_ID) as max "
      + " from oa_fl_run_prcs where  "
      + " RUN_ID=" + runId;
    int  prcsMax = this.getMax(conn, queryMax);
   // String queryMax2 = "SELECT MAX(PRCS_ID) from FLOW_RUN_PRCS where RUN_ID='" 
    //  + runId +"' and PRCS_FLAG<>'5'";
    ///int prcsMax2 = this.getMax(conn, queryMax2);
    int topAuto = 70;
    int leftAuto = 10;
    for(int i = 1 ; i <= prcsMax ;i ++){
      Map map = new HashMap();
      map.put("prcsId", i + "");
      List<Map> childList = new ArrayList();
      //sb.append("{");
      //sb.append("prcsId:" + i + ",list:[");
      String query = "SELECT * from oa_fl_run_prcs where "
      + "  RUN_ID="+ runId +"  "
      + " and PRCS_ID=" + i + " order by FLOW_PRCS";
      int flowPrcs = 0;
      YHFlowProcess fp = null;
      Statement stm2 = null;
      ResultSet rs2 = null;
      String ss = "";
      int count = 0;
      try {
        stm2 = conn.createStatement();
        rs2 = stm2.executeQuery(query);
        // 获取步骤信息
        while (rs2.next()){
          flowPrcs = rs2.getInt("FLOW_PRCS");
          if (YHWorkFlowUtility.findId(ss, flowPrcs + "")) {
            continue;
          }
          count++;
          ss += flowPrcs + ",";
          String prcsName = "";
          String timeOut = "";
          String except = "00";
          if(flowType != null 
              && "1".equals(flowType.getFlowType())){
            YHFlowProcessLogic logic = new YHFlowProcessLogic();
            fp = logic.getFlowProcessById(flowType.getSeqId(), String.valueOf(flowPrcs) , conn);
            timeOut = fp.getTimeOut();
            except = fp.getTimeExcept();
            if(fp != null){
              prcsName = fp.getPrcsName();
            }else{
              prcsName = "<font color=red>流程步骤已删除</font>";
            }
          }
          String parent = rs2.getString("PARENT");
          int childRun = rs2.getInt("CHILD_RUN");
          
          int childFlowId = 0 ;
          if (childRun != 0) {
            YHFlowRunUtility u = new YHFlowRunUtility();
            childFlowId = u.getFlowId(conn, childRun);
          }
          Map map2 = new HashMap();
          map2.put("prcsName", prcsName);
          map2.put("flowPrcs", flowPrcs);
          map2.put("sourceId",  i +"_" + flowPrcs +"_" + parent );
          map2.put("childRun", childRun + "");
          map2.put("childFlowId", childFlowId + "");
          map2.put("flowType", flowType.getFlowType());
         // sb.append("{prcsName:'" + prcsName + "',flowPrcs:" + flowPrcs + ",sourceId:'"+ i +"_" + flowPrcs +"_" + parent + "',childRun:'" + childRun + "', childFlowId:'" + childFlowId + "'");
          //---------- while2 获得此步骤、此序号的办理信息 ---------------
          int prcsState = 0;
          String opUserName = "";
          //$query1 = "SELECT * from FLOW_RUN_PRCS where RUN_ID=$RUN_ID and PRCS_ID=$PRCS_ID_I and FLOW_PRCS='$FLOW_PRCS' AND PARENT='$PARENT' order by OP_FLAG,PRCS_FLAG DESC,PRCS_TIME";
          Map map3 = new HashMap();
          map3.put("RUN_ID", runId);
          map3.put("PRCS_ID", i);
          map3.put("FLOW_PRCS", flowPrcs);
          //map.put("PARENT", parent);//暂不考虑父流程

          List<YHFlowRunPrcs> list2 = orm.loadListSingle(conn, YHFlowRunPrcs.class, map3);
         // StringBuffer sb2 = new StringBuffer();
         // sb2.append("[");
          List<Map> userList = new ArrayList();
          for(YHFlowRunPrcs flowRunPrcs : list2){
            Map map4 = new HashMap();
            //处理办理人信息

            String queryPerson = "SELECT u.USER_NAME "
              + " ,d.DEPT_NAME  "
              + " from PERSON u,oa_department d where  "
              + " u.SEQ_ID="+ flowRunPrcs.getUserId()
              + " AND d.SEQ_ID = u.DEPT_ID";
            String userName = "";
            Statement stm4 = null;
            ResultSet rs4 = null;
            try {
              stm4 = conn.createStatement();
              rs4 = stm4.executeQuery(queryPerson);
              if(rs4.next()){
                userName = rs4.getString("USER_NAME");
                map4.put("userName",YHUtility.encodeSpecial( userName));
                map4.put("deptName", YHUtility.encodeSpecial(rs4.getString("DEPT_NAME")));
              } else {
                map4.put("userName", "此人员已删除");
                map4.put("deptName", "此人员已删除");
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm4, rs4, null); 
            }
            //true-我主办的，false-我会签

            boolean isOp = false;
            //未接收中
            int prcsFlag = 0 ;
            if(flowRunPrcs.getPrcsFlag() != null){
              prcsFlag = Integer.parseInt(flowRunPrcs.getPrcsFlag());
              prcsState = prcsFlag;
            }
            //主办的

            if("1".equals(flowRunPrcs.getOpFlag())){
              isOp = true;
              opUserName = userName;
              prcsState = prcsFlag;
            }
            if("2".equals(flowRunPrcs.getTopFlag())){
              opUserName = "无主办人会签";
              if(prcsState == 0){
                prcsState = prcsFlag;
              }else if(prcsFlag < prcsState){
                prcsState = prcsFlag;
              }
            }
            map4.put("isOp", isOp);
            //计算用户用时
            long timeUsed = 0 ;
            if( flowRunPrcs.getPrcsTime() != null){
              if(prcsFlag == 1){
              }else if(prcsFlag == 2){
                Date date = new Date();
                timeUsed = date.getTime() - flowRunPrcs.getPrcsTime().getTime();
              }else{
                if(flowRunPrcs.getDeliverTime() != null){
                  timeUsed = flowRunPrcs.getDeliverTime().getTime() - flowRunPrcs.getPrcsTime().getTime();
                }
              }
            }
            String timeStr = "";
            long day=timeUsed/(24*60*60*1000); 
            long hour=(timeUsed/(60*60*1000)-day*24); 
            long min=((timeUsed/(60*1000))-day*24*60-hour*60); 
            long s=(timeUsed/1000-day*24*60*60-hour*60*60-min*60);
            
            if ( day > 0 ) {
              timeStr = day + "天";
            }
            if ( hour>0){
              timeStr +=hour + "时";
            }
            if(min>0){
              timeStr +=min + "分";
            }
            if(s>0){
              timeStr +=s + "秒";
            }
            //-- 超时信息 --  
            int timeOutFlag = 0 ;
            Date beginTime = flowRunPrcs.getPrcsTime();
            if((prcsFlag == 2 || prcsFlag == 1) 
                && !"".equals(timeOut) 
                && timeOut != null){
              if(timeOutFlag == 0){
                if(flowRunPrcs.getPrcsTime() != null){
                  beginTime = flowRunPrcs.getPrcsTime();
                }else{
                  beginTime = flowRunPrcs.getCreateTime();
                }
              }else{
                beginTime = flowRunPrcs.getCreateTime();
              }
              //如果不是自由流程
              if ("1".equals(flowType.getFlowType())) {
                String timeDesc = YHWorkFlowUtility.getTimeOut(timeOut, beginTime, new Date(), "dhms", except);
                //超时
                if(!"".equals(timeDesc)){
                  timeStr = timeDesc;
                  timeOutFlag = 1;
                }
              }
            }
            if(!"".equals(timeStr)){
              map4.put("timeUsed", timeStr);
            } else {
              map4.put("timeUsed", "0");
            }
            //记录未接收或者超时用户


            if (prcsFlag == 1 || timeOutFlag == 1) {
              timeToId += flowRunPrcs.getUserId() + ",";
            }
            if (beginTime != null) {
              map4.put("beginTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beginTime));
            } else {
              map4.put("beginTime", "");
            }
            if(flowRunPrcs.getDeliverTime() != null){
              map4.put("deliverTime",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flowRunPrcs.getDeliverTime()));
            } else {
              map4.put("deliverTime", "");
            }
            map4.put("state", flowRunPrcs.getPrcsFlag());
            if(flowRunPrcs.getCreateTime() != null){
              map4.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(flowRunPrcs.getCreateTime()) );
            } else {
              map4.put("createTime", "");
            }
            userList.add(map4);
          }
          map2.put("state", prcsState);
          String prcsTitle = "第" + i + "步." + prcsName + "(" + opUserName + ")";
          map2.put("prcsTitle", prcsTitle);
          //不是实际流程最后一步
          List<String> prcsTos = new ArrayList<String>(); 
          if(i != prcsMax ){
            String query2 = "select FLOW_PRCS  , PARENT FROM oa_fl_run_prcs where " 
              + " RUN_ID = " + runId  
              + " and  PRCS_ID =" + (i + 1) ;
            if ("1".equals(flowType.getFlowType()) ) {
              query2 += " and " + YHDBUtility.findInSet(flowPrcs + "", "PARENT");
            }
            Statement stm8 = null;
            ResultSet rs8 = null;
            try {
              stm8 = conn.createStatement();
              rs8 = stm8.executeQuery(query2);
              int countTmp = 0;
              String ss2 = "";
              while (rs8.next()){
                int flowPrcs1 = rs8.getInt("FLOW_PRCS");
                String parent1 = rs8.getString("PARENT");
                String ids = (i+1) + "_" + flowPrcs1 + "_" + parent1;
                if (YHWorkFlowUtility.findId(ss2, ids)) {
                  continue;
                }
                countTmp++;
                ss2 += ids + ",";
                prcsTos.add(ids);
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm8, rs8, null); 
            }
          } 
          map2.put("prcsTo", prcsTos);
          map2.put("leftVml", leftAuto);
          map2.put("topVml", topAuto);
          if (count > 0) {
            topAuto += 70;
          }
          map2.put("user", userList);
          childList.add(map2);
        }
        leftAuto += 150;
        topAuto = 70;
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
      map.put("list", childList);
      list.add(map);
    } 
    return timeToId;
  }
  /**
   * 取得html形式流程列表
   * @param runId
   * @param flowId
   * @return
   * @throws Exception
   */
  public String getPrcsHtml(int runId , YHFlowType flowType ,  Connection conn) throws Exception{
    YHORM orm = new YHORM();
    String timeToId = "";
    int prcsMax = 0 ;
    String queryMax = "SELECT MAX(PRCS_ID) as max "
      + " from oa_fl_run_prcs where  "
      + " RUN_ID=" + runId;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(queryMax);
      if(rs.next()){
        prcsMax = rs.getInt("max");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    StringBuffer sb = new StringBuffer();
    for(int i = 1 ; i <= prcsMax ;i ++){
      String query = "SELECT * from oa_fl_run_prcs where "
      + "  RUN_ID="+ runId +"  "
      + " and PRCS_ID=" + i + " order by FLOW_PRCS";
      Statement stm6 = null;
      ResultSet rs6= null;
      String ss1 = "";
      int count = 0 ;
      try {
        stm6 = conn.createStatement();
        rs6 = stm6.executeQuery(query);
        while(rs6.next()){
          int  flowPrcs1 = rs6.getInt("FLOW_PRCS");
          if (YHWorkFlowUtility.findId(ss1, flowPrcs1 + "")) {
            continue;
          }
          count++;
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm6, rs6, null); 
      }
      // 获取步骤信息
      sb.append("<TR class=TableLine"+ (i%2 == 0 ? 1 : 2) +">");
      sb.append("<TD  rowspan="+count+">第<FONT color=red>" + i + "</FONT>步</TD>");
      int flowPrcs = 0;
      String prcsName = "";
      String timeOut = "";
      String except = "00";
      YHFlowProcess fp = null;
      Statement stm2 = null;
      ResultSet rs2 = null;
      String ss = "";
      count = 0;
      try {
        stm2 = conn.createStatement();
        rs2 = stm2.executeQuery(query);
        while (rs2.next()){
          flowPrcs = rs2.getInt("FLOW_PRCS");
          if (YHWorkFlowUtility.findId(ss, flowPrcs + "")) {
            continue;
          }
          count++;
          if("1".equals(flowType.getFlowType())){
            YHFlowProcessLogic logic = new YHFlowProcessLogic();
            fp = logic.getFlowProcessById(flowType.getSeqId(), String.valueOf(flowPrcs) , conn);
            timeOut = fp.getTimeOut();
            except = fp.getTimeExcept();
            if(fp != null){
              prcsName = fp.getPrcsName();
            }else{
              prcsName = "<font color=red>流程步骤已删除</font>";
            }
          }
          if (count > 1) {
            sb.append("<TR class=TableLine"+ (i%2 == 0 ? 1 : 2) +">");
          }
          sb.append("<TD>&nbsp;序号" + flowPrcs + ":" + prcsName + "</TD>");
          //---------- while2 获得此步骤、此序号的办理信息 ---------------
          int prcsState = 0;
          //$query1 = "SELECT * from FLOW_RUN_PRCS where RUN_ID=$RUN_ID and PRCS_ID=$PRCS_ID_I and FLOW_PRCS='$FLOW_PRCS' AND PARENT='$PARENT' order by OP_FLAG,PRCS_FLAG DESC,PRCS_TIME";
          Map map = new HashMap();
          map.put("RUN_ID", runId);
          map.put("PRCS_ID", i);
          map.put("FLOW_PRCS", flowPrcs);
          //map.put("", arg1)//暂不考虑父流程
          List<YHFlowRunPrcs> list = orm.loadListSingle(conn, YHFlowRunPrcs.class, map);
          sb.append("<TD>");
          for(YHFlowRunPrcs flowRunPrcs : list){
            //处理办理人信息
            String queryPerson = "SELECT u.USER_NAME "
              + " ,d.DEPT_NAME  "
              + " from PERSON u,oa_department d where  "
              + " u.SEQ_ID="+ flowRunPrcs.getUserId()
              + " AND d.SEQ_ID = u.DEPT_ID";
            String userName = "";
            String deptName = "";
            Statement stm4 = null;
            ResultSet rs4 = null;
            try {
              stm4 = conn.createStatement();
              rs4 = stm4.executeQuery(queryPerson);
              if(rs4.next()){
                userName = rs4.getString("USER_NAME");
                deptName =  rs4.getString("DEPT_NAME") ;
              } else {
                userName = "此人员已删除";
                deptName =  "此人员已删除";
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm4, rs4, null); 
            }
            //true-我主办的，false-我会签
            boolean isOp = false;
            //未接收中
            int prcsFlag = 0 ;
            if(flowRunPrcs.getPrcsFlag() != null){
              prcsFlag = Integer.parseInt(flowRunPrcs.getPrcsFlag());
              prcsState = prcsFlag;
            }
            //主办的
            if("1".equals(flowRunPrcs.getOpFlag())){
              isOp = true;
              prcsState = prcsFlag;
            }
            if("2".equals(flowRunPrcs.getTopFlag())){
              if(prcsState == 0){
                prcsState = prcsFlag;
              }else if(prcsFlag < prcsState){
                prcsState = prcsFlag;
              }
            }
            String prcsUserName = "";
            if ( isOp ) {
              prcsUserName = "<span style=\"text-decoration:underline;font-weight:bold;color:red\" title=\"部门："+deptName+"\">"+ userName +" 主办</span>";
            }else{
              prcsUserName = "<span style=\"text-decoration:underline;font-weight:bold\" title=\"部门："+deptName+"\">"+ userName+"</span>";
            }
            //计算用户用时
            long timeUsed = 0 ;
            if( flowRunPrcs.getPrcsTime() != null){
              if(prcsFlag == 1){
              }else if(prcsFlag == 2){
                Date date = new Date();
                timeUsed = date.getTime() - flowRunPrcs.getPrcsTime().getTime();
              }else{
                if(flowRunPrcs.getDeliverTime() != null){
                  timeUsed = flowRunPrcs.getDeliverTime().getTime() - flowRunPrcs.getPrcsTime().getTime();
                }
              }
            }
            String timeStr = "";
            long day=timeUsed/(24*60*60*1000); 
            long hour=(timeUsed/(60*60*1000)-day*24); 
            long min=((timeUsed/(60*1000))-day*24*60-hour*60); 
            long s=(timeUsed/1000-day*24*60*60-hour*60*60-min*60);
            
            if ( day > 0 ) {
              timeStr = day + "天";
            }
            if ( hour>0){
              timeStr +=hour + "时";
            }
            if(min>0){
              timeStr +=min + "分";
            }
            if(s>0){
              timeStr +=s + "秒";
            }
            //-- 超时信息 --  
            int timeOutFlag = 0 ;
            Date beginTime = flowRunPrcs.getPrcsTime();
            if((prcsFlag == 2 || prcsFlag == 1) 
                && !"".equals(timeOut) 
                && timeOut != null){
              if(timeOutFlag == 0){
                if(flowRunPrcs.getPrcsTime() != null){
                  beginTime = flowRunPrcs.getPrcsTime();
                }else{
                  beginTime = flowRunPrcs.getCreateTime();
                }
              }else{
                beginTime = flowRunPrcs.getCreateTime();
              }
              //如果不是自由流程
              if ("1".equals(flowType.getFlowType())) {
                String timeDesc = YHWorkFlowUtility.getTimeOut(timeOut, beginTime, new Date(), "dhms", except);
                //超时
                if(!"".equals(timeDesc)){
                  timeStr = timeDesc;
                  timeOutFlag = 1;
                }
              }
            }
            
            //记录未接收或者超时用户

            if (prcsFlag == 1 || timeOutFlag == 1) {
              timeToId += flowRunPrcs.getUserId() + ",";
            }
            String title = "";
            int state = Integer.parseInt(flowRunPrcs.getPrcsFlag());
            if(state == 1){
              title += "&nbsp;" + prcsUserName + "&nbsp;[<font color=green>未接收办理</font>]";
            //办理中

            }else if(state == 2){
              title += "&nbsp;" + prcsUserName + "&nbsp;[<font color=green>办理中,已用时：" + timeStr + "</font>]";
              if(timeOutFlag == 1){
                title += "<br><span style=\"color:red\">限时"+ timeOut +"小时," + timeStr + "</span>";
              }
              title += "<br> 开始于：" + beginTime;
            //已转交下步
            }else if(state == 3){
              title += "&nbsp;"+ prcsUserName +"&nbsp;[<font color=green>已转交下步,用时：" + timeStr + "</font>]";
              title += "<br> 开始于：" + beginTime;
              if(flowRunPrcs.getDeliverTime() != null) {
                title += "<br> 结束于：" + YHUtility.getDateTimeStr(flowRunPrcs.getDeliverTime());
              }
              //已办结
            }else if(state == 4){
              title += "&nbsp;"+ prcsUserName +"&nbsp;[<font color=green>已办结,用时：" + timeStr + "</font>]";
              title += "<br> 开始于：" + beginTime;
              if(flowRunPrcs.getDeliverTime() != null) {
                title += "<br> 结束于：" + YHUtility.getDateTimeStr(flowRunPrcs.getDeliverTime());
              }
            }else if(state == 5){
              title += prcsUserName + "&nbsp;[预设经办人]";
            }
            sb.append(title);
          }
          sb.append("</TD>");
          sb.append("</TR>");
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
      //不是实际流程最后一步    }
    return sb.toString();
  }
  /**
   * 删除流程实例 
   * @param runId
   * @param loginUser
   * @param conn
   * @throws Exception
   */
  public void delRun(int runId ,YHPerson loginUser, Connection conn ) throws Exception{
    //没有权限删除
    if (!this.isHaveDelPriv(runId, loginUser, conn)) {
      return ;
    }
    String update = "update oa_fl_run set DEL_FLAG=1 where RUN_ID=" + runId;
    Statement stm5 = null;
    try {
      stm5 = conn.createStatement();
      int i = stm5.executeUpdate(update);
      if (i > 0) {
        String content = "删除此工作";
        YHFlowRunLogLogic logic = new YHFlowRunLogLogic();
        logic.runLog(runId, 0, 0, loginUser.getSeqId(), 3, content, "", conn);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm5, null, null); 
    }
  }
  /**
   * 判断当前用户是否有些流程的删除权限
   * @return false 为没有 , true为有
   * @throws Exception 
   */
  public boolean isHaveDelPriv(int runId , YHPerson user , Connection conn) throws Exception{
    YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
    //取出权限字符串    String roleStr = roleUtility.runRole(runId,  1, user , conn);
    
    boolean flag = true;
    String query = "select 1 from oa_fl_run_prcs WHERE RUN_ID="+ runId +" AND PRCS_ID>1";
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      if(rs4.next()){
        flag = false;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs4, null); 
    }
    //第一步是主办人，且没有第二步
    boolean A = !(YHWorkFlowUtility.findId(roleStr, "2") && flag);
    //是系统管理员
    boolean B = !YHWorkFlowUtility.findId(roleStr , "1"); 
    //是该流程管理员
    boolean C = !YHWorkFlowUtility.findId(roleStr , "3");
    boolean contion =  A && B && C;
    return !contion;
  }
  /**
   * 回收
   * @param runId
   * @param prcsId
   * @param user
   * @param conn
   * @return
   * @throws Exception
   */
  public String callBack(int runId ,int prcsId , int flowPrcs , YHPerson user , Connection conn) throws Exception{
    //取出权限字符串
    if (!this.callBackPriv(runId, prcsId, user, conn)) {
      return "你没有此权限";
    }
  //---------- 自由流程预设步骤检查 ----------
    int prcsIdNew =  prcsId + 1;
    String query = "SELECT 1 from oa_fl_run_prcs WHERE  " 
      + " RUN_ID=" + runId 
      + " and PRCS_ID >=" + prcsIdNew
      + " and PRCS_FLAG = 5";
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      if (rs4.next()) {
        //存在预设步骤，更改其标记为5
        String update =  "update " 
          + " oa_fl_run_prcs " 
          + "  set PRCS_FLAG='5' " 
          + "  WHERE  " 
          + " RUN_ID="+ runId +"  " 
          + " and PRCS_ID>=" + prcsIdNew;
        Statement stm5 = null;
        try {
          stm5 = conn.createStatement();
          stm5.executeUpdate(update);
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm5, null, null); 
        }
      } else {
        String query1 = "select FLOW_PRCS,PARENT from oa_fl_run_prcs where "
          + " RUN_ID = " + runId 
          + " and PRCS_ID >="+ prcsIdNew ;
          //+ " and FIND_IN_SET('$FLOW_PRCS',PARENT)";
        
        //固定流程
        Statement stm6 = null;
        ResultSet rs6 = null;
        try {
          stm6 = conn.createStatement();
          rs6 = stm6.executeQuery(query1);
          if (rs6.next()) {
            String parent = rs6.getString("PARENT");
            String flowPrcsNext = rs6.getString("FLOW_PRCS");
            
            if (parent != null ) {
              String[] aParent = parent.split(",");
              //删除后续步骤或者更新一下步骤的PARENT
              if (aParent.length == 1 ) {
                //and FIND_IN_SET('$FLOW_PRCS',PARENT)
                YHWorkFlowUtility.deleteTable("oa_fl_run_prcs", "RUN_ID="+ runId +" and PRCS_ID >=" + prcsIdNew, conn);
              } else {
                parent = YHWorkFlowUtility.getOutOf(parent, String.valueOf(flowPrcs));
                String condition = "RUN_ID="+ runId 
                                      +" and PRCS_ID >="+ prcsIdNew 
                                      +" and FLOW_PRCS ="+ flowPrcsNext;
                                     // +" AND FIND_IN_SET('$FLOW_PRCS',PARENT)";
                YHWorkFlowUtility.updateTable("oa_fl_run_prcs", "PARENT='" + parent + "'", condition, conn);
              }
            } else {
              YHWorkFlowUtility.deleteTable("oa_fl_run_prcs", "RUN_ID="+ runId +" and PRCS_ID >=" + prcsIdNew, conn);
            }
          } else {//自由流程
            YHWorkFlowUtility.deleteTable("oa_fl_run_prcs", "RUN_ID="+ runId +" and PRCS_ID=" + prcsIdNew, conn);
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm4, rs6, null); 
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs4, null); 
    }
    //工作流日志
    String content = "收回工作";
    YHFlowRunLogLogic logic = new YHFlowRunLogLogic();
    logic.runLog(runId, prcsId, flowPrcs, user.getSeqId(), 1, content, "", conn);
    
    //更改本步骤状态为办理中
    String condition =  "RUN_ID=" + runId 
                           + " and PRCS_ID="+ prcsId 
                           +" and FLOW_PRCS="+ flowPrcs 
                           + " and USER_ID="+ user.getSeqId()
                           + " and OP_FLAG=1";
    YHWorkFlowUtility.updateTable("oa_fl_run_prcs", " PRCS_FLAG='2'", condition, conn);
    return null;
  }
  /**
   * 验证当前用户是否能回收
   * @param runId
   * @param prcsId
   * @param user
   * @param conn
   * @return  false 为没有 , true为有
   * @throws Exception
   */
  public boolean callBackPriv(int runId ,int prcsId , YHPerson user , Connection conn) throws Exception {
    YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
    //取出权限字符串
    String roleStr = roleUtility.runRole(runId,  prcsId, user , conn);
    //and (PARENT='' OR FIND_IN_SET('$FLOW_PRCS',PARENT)) 并发时
    String query = "SELECT 1 from oa_fl_run_prcs WHERE  " 
      + " RUN_ID="+ runId +"   " 
      + " and PRCS_ID="+ (prcsId + 1)
      + " and PRCS_FLAG in('2','3','4')";
    boolean flag = false;
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      //下一步已经接收了
      if (rs4.next()){
        flag = true;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs4, null); 
    }
    //是管理员或者是主办人且下一步没有接收    boolean contion = (YHWorkFlowUtility.findId(roleStr, "1") || YHWorkFlowUtility.findId(roleStr, "2")) && !flag ;
    return contion;
  }
  /**
   * 叛断是否有未接收的工作
   * @param conn
   * @param userId
   * @return
   * @throws Exception 
   */
  public boolean hasWork(Connection conn , int userId  , String sortId , int flowId) throws Exception {
    boolean flag = false;//没有
    String query = "SELECT 1 from oa_fl_run_prcs as FLOW_RUN_PRCS,oa_fl_type as FLOW_TYPE,oa_fl_run as FLOW_RUN,oa_fl_sort as FLOW_SORT WHERE FLOW_RUN_PRCS.RUN_ID=FLOW_RUN.RUN_ID and FLOW_RUN.FLOW_ID=FLOW_TYPE.SEQ_ID and FLOW_RUN.DEL_FLAG='0' AND FLOW_TYPE.FLOW_SORT = FLOW_SORT.SEQ_ID and USER_ID=" + userId;
      query += " and PRCS_FLAG='1'";
    if (flowId != 0) {
      query += " and FLOW_TYPE.SEQ_ID=" + flowId;
    }
    if (!YHUtility.isNullorEmpty(sortId)) {
      sortId = YHWorkFlowUtility.getOutOfTail(sortId);
      query += " and FLOW_SORT.SEQ_ID in (" + sortId + ")";
    }
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      if(rs4.next()){
        flag = true;//有
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs4, null); 
    }
    return flag;
  }
  public String getMyWork(Connection conn , YHPerson user , int maxCount  , String sortId) throws Exception {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    String query = "SELECT FLOW_RUN_PRCS.PRCS_ID " 
      + " ,FLOW_RUN.RUN_ID " 
      + " ,FLOW_RUN.FLOW_ID " 
      + " ,FLOW_RUN_PRCS.PRCS_FLAG " 
      + " ,FLOW_RUN_PRCS.FLOW_PRCS " 
      + " ,FLOW_TYPE.FLOW_NAME " 
      + " ,FLOW_RUN.RUN_NAME " 
      + " ,FLOW_TYPE.FLOW_TYPE " 
      + " ,FLOW_TYPE.FORM_SEQ_ID  " 
      
      + " , FLOW_RUN_PRCS.TIME_OUT_FLAG"
      + " , FLOW_RUN_PRCS.CREATE_TIME"
      + " , FLOW_RUN_PRCS.PRCS_TIME"
      
      
      + " from oa_fl_run_prcs as FLOW_RUN_PRCS,oa_fl_run as FLOW_RUN,oa_fl_type as FLOW_TYPE WHERE  " 
      + " FLOW_RUN_PRCS.RUN_ID=FLOW_RUN.RUN_ID " 
      + "  and FLOW_RUN.FLOW_ID=FLOW_TYPE.SEQ_ID  " 
      + " and USER_ID=" + user.getSeqId()
      + " and FLOW_RUN.DEL_FLAG='0'  " 
      + " and FLOW_RUN_PRCS.PRCS_TIME IS NULL  " ;
     
    if (!YHUtility.isNullorEmpty(sortId)) {
      sortId = YHWorkFlowUtility.getOutOfTail(sortId);
      query += " and FLOW_TYPE.FLOW_SORT IN (" + sortId + ")";
    } else {
      YHWorkFlowUtility util = new YHWorkFlowUtility();
      String sortIds = util.getNotDisplaySort(conn);
      if (!YHUtility.isNullorEmpty(sortIds)) {
        query += " and FLOW_TYPE.FLOW_SORT NOT IN (" + sortIds + ")";
      }
    }
     
     query += " order by FLOW_RUN_PRCS.PRCS_FLAG,PRCS_TIME desc ";
    int count = 0 ;
    
    Statement stm4 = null;
    ResultSet rs = null;
    try {
      stm4 = conn.createStatement();
      rs = stm4.executeQuery(query);
      while (rs.next()) {
        count ++ ;
        if(count > maxCount )
           break;
        int prcsId = rs.getInt("PRCS_ID");
        int runId = rs.getInt("RUN_ID");
        int flowId = rs.getInt("FLOW_ID");
        String  prcsFlag = rs.getString("PRCS_FLAG");
        int flowPrcs = rs.getInt("FLOW_PRCS");
        String flowName = rs.getString("FLOW_NAME");
        String runName = rs.getString("RUN_NAME");
        String flowType = rs.getString("FLOW_TYPE");
        
        Timestamp prcsTime = rs.getTimestamp("PRCS_TIME");
        Timestamp createTime = rs.getTimestamp("CREATE_TIME");
        String timeOutFlag = rs.getString("TIME_OUT_FLAG");
        
        
        int formId = rs.getInt("FORM_SEQ_ID");
        
        String prcsName = "";
        String sFlowPrcsI = String.valueOf(flowPrcs);
        Map prcsNameMap = new HashMap();
        
        String timeOutType = "";
        String timeOut = "";
        String timeExcept = "01"; 
        if ("1".equals(flowType)) {
          if (!prcsNameMap.containsKey(sFlowPrcsI)) {
            String query3 = "select PRCS_NAME, TIME_EXCEPT, TIME_OUT_TYPE , TIME_OUT from oa_fl_process WHERE FLOW_SEQ_ID="+ flowId +" AND PRCS_ID=" + flowPrcs;
            Statement stm3 = null;
            ResultSet rs3 = null;
            try {
              stm3 = conn.createStatement();
              rs3 = stm3.executeQuery(query3);
              if (rs3.next()) {
                String tmp = rs3.getString("PRCS_NAME");
                prcsNameMap.put(sFlowPrcsI, tmp);
                
                timeOutType = rs3.getString("TIME_OUT_TYPE");
                timeOut = rs3.getString("TIME_OUT");
                timeExcept = rs3.getString("TIME_EXCEPT");
                if (timeExcept == null) {
                  timeExcept = "01"; 
                }
                
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm3, rs3, null); 
            }
          }
        } else {
          prcsNameMap.put(sFlowPrcsI, "");
        }
        sb.append("{");
        sb.append("runId:" + runId);
        sb.append(",prcsId:" + prcsId);
        sb.append(",flowId:" + flowId);
        sb.append(",prcsFlag:'" + prcsFlag + "'");
        sb.append(",flowPrcs:'" + flowPrcs + "'");
        sb.append(",flowName:'" + flowName + "'");
        sb.append(",runName:'" + runName + "'");
        sb.append(",flowType:'" + flowType + "'");
        sb.append(",formId:" + formId);
        
        if ("1".equals(timeOutFlag)) {
          Date prcsBeginTime = null;
          if("0".equals(timeOutType)) {
            prcsBeginTime = prcsTime;
            if(prcsTime == null)
               prcsBeginTime = createTime;
          } else {
            prcsBeginTime = createTime;
          }
          
          if (prcsBeginTime == null) {
            prcsBeginTime = new Date();
          }
          String timeDesc = YHWorkFlowUtility.getTimeOut(timeOut, prcsBeginTime, new Date(), "dhms", timeExcept);
          if(!"".equals(timeDesc)){
            //timeStr = timeDesc;
            sb.append(",timeUse:'" + timeDesc + "'");
          }
        }
        
        sb.append(",prcsName:'" + (String)prcsNameMap.get(sFlowPrcsI) + "'");
        sb.append("},");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs, null); 
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public String getSign(Connection conn , YHPerson user , int maxCount , String sortId) throws Exception {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    String query = "SELECT FLOW_RUN_PRCS.PRCS_ID " 
      + " ,FLOW_RUN.RUN_ID " 
      + " ,FLOW_RUN.FLOW_ID " 
      + " ,PRCS_FLAG " 
      + " ,FLOW_PRCS " 
      + " ,FLOW_NAME " 
      + " ,RUN_NAME " 
      + "  from oa_fl_run_prcs as FLOW_RUN_PRCS,oa_fl_run as FLOW_RUN,oa_fl_type as FLOW_TYPE WHERE " 
      + "  FLOW_RUN_PRCS.RUN_ID=FLOW_RUN.RUN_ID  " 
      + " and FLOW_RUN.FLOW_ID=FLOW_TYPE.Seq_ID  and FLOW_RUN_PRCS.CHILD_RUN = 0" 
      + " and FLOW_RUN.DEL_FLAG=0  " 
      + " and USER_ID=" + user.getSeqId() ;
    if (!YHUtility.isNullorEmpty(sortId)) {
      sortId = YHWorkFlowUtility.getOutOfTail(sortId);
      query += " and FLOW_TYPE.FLOW_SORT IN (" + sortId + ")";
    } else {
      YHWorkFlowUtility util = new YHWorkFlowUtility();
      String sortIds = util.getNotDisplaySort(conn);
      if (!YHUtility.isNullorEmpty(sortIds)) {
        query += " and FLOW_TYPE.FLOW_SORT NOT IN (" + sortIds + ")";
      }
    }
     query += "  and OP_FLAG='0' order by FLOW_RUN_PRCS.PRCS_FLAG,PRCS_TIME desc ";
    int count = 0 ;
    Map prcsNameMap = new HashMap();
    Statement stm4 = null;
    ResultSet rs = null;
    try {
      stm4 = conn.createStatement();
      rs = stm4.executeQuery(query);
      while (rs.next()) {
        count ++ ;
        if(count > maxCount )
           break;
        int prcsId = rs.getInt("PRCS_ID");
        int runId = rs.getInt("RUN_ID");
        int flowId = rs.getInt("FLOW_ID");
        String  prcsFlag = rs.getString("PRCS_FLAG");
        int flowPrcs = rs.getInt("FLOW_PRCS");
        String flowName = rs.getString("FLOW_NAME");
        String runName = rs.getString("RUN_NAME");
        
        sb.append("{");
        sb.append("runId:" + runId);
        sb.append(",prcsId:" + prcsId);
        sb.append(",flowId:" + flowId);
        sb.append(",prcsFlag:'" + prcsFlag + "'");
        sb.append(",flowPrcs:'" + flowPrcs + "'");
        sb.append(",flowName:'" + flowName + "'");
        sb.append(",runName:'" + runName + "'");
        sb.append("},");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs, null); 
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public String getFocusWork(Connection conn, YHPerson user, int maxCount , String sortId) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    
    String query = "SELECT FLOW_RUN.RUN_ID " 
      + " ,FLOW_RUN.FLOW_ID " 
      + " ,FLOW_RUN.RUN_NAME " 
      + " ,FLOW_TYPE.FLOW_NAME " 
      + " ,FLOW_RUN.FOCUS_USER " 
      + "  FROM oa_fl_run as FLOW_RUN ,oa_fl_type as FLOW_TYPE  WHERE  " 
      + " FLOW_RUN.FLOW_ID=FLOW_TYPE.SEQ_ID  " 
      + " AND DEL_FLAG=0  ";
    if (!YHUtility.isNullorEmpty(sortId)) {
      sortId = YHWorkFlowUtility.getOutOfTail(sortId);
      query += " and FLOW_TYPE.FLOW_SORT IN (" + sortId + ")";
    } else {
      YHWorkFlowUtility util = new YHWorkFlowUtility();
      String sortIds = util.getNotDisplaySort(conn);
      if (!YHUtility.isNullorEmpty(sortIds)) {
        query += " and FLOW_TYPE.FLOW_SORT NOT IN (" + sortIds + ")";
      }
    }
    String userSeqId= String.valueOf(user.getSeqId());
    query +=" and (FLOW_RUN.FOCUS_USER like '"+userSeqId+",%' or FLOW_RUN.FOCUS_USER like '%,"+userSeqId+",%')";
    query +="  ORDER BY RUN_ID DESC";
    int count = 0 ;
    Map prcsNameMap = new HashMap();
    Statement stm4 = null;
    ResultSet rs = null;
    try {
      stm4 = conn.createStatement();
      rs = stm4.executeQuery(query);
      while (rs.next()) {
//        String focus = rs.getString("FOCUS_USER");
//        if (YHWorkFlowUtility.findId(focus, String.valueOf(user.getSeqId()))) {
          count ++ ;
          if(count > maxCount )
             break;
          int runId = rs.getInt("RUN_ID");
          int flowId = rs.getInt("FLOW_ID");
          String flowName = rs.getString("FLOW_NAME");
          String runName = rs.getString("RUN_NAME");
          
          sb.append("{");
          sb.append("runId:" + runId);
          sb.append(",flowId:" + flowId);
          sb.append(",flowName:'" + flowName + "'");
          sb.append(",runName:'" + runName + "'");
          sb.append("},");
//        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs, null); 
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public Map<String , InputStream> getExportZip(String runId , Connection conn ,YHPerson user , String imgPath) throws Exception {
    Map map = new HashMap();
    try {
      String[] ris = runId.split(",");
      YHFlowTypeLogic ftl = new YHFlowTypeLogic();
      YHFlowRunLogic frl = new YHFlowRunLogic();
      YHAttachmentLogic attachLogic = new YHAttachmentLogic();
      YHFeedbackLogic feedbackLogic = new YHFeedbackLogic();
      YHMyWorkLogic workLogic = new YHMyWorkLogic();
      for (String sRunId : ris) {
        YHFlowRun flowRun = frl.getFlowRunByRunId(Integer.parseInt(sRunId) , conn);
        String runName = flowRun.getRunName();
        if (runName != null) {
          runName = runName.replace(":", "-");
        }
        String aIds = flowRun.getAttachmentId();
        String aNames = flowRun.getAttachmentName();
        YHFlowType ft = ftl.getFlowTypeById(flowRun.getFlowId(),conn);
        
        StringBuffer sb = new StringBuffer();
        Map result = frl.getPrintForm(user, flowRun, ft ,true, conn , imgPath) ;
        String  form = (String)result.get("form");
        sb.append("<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><head><title></title></head><body><div id=\"form\" style=\"margin-top:5px;margic-bottom:5px\">");
        form = form.replaceAll("\\\\\"", "\"");
        sb.append(form).append("</div><div id=\"attachment\"><table width='100%'><tr class=TableHeader><td colspan=3>公共附件</td></tr><tbody id=\"attachmentsList\">");
        String attachment = attachLogic.getAttachmentsHtml(user, flowRun.getRunId() , conn);
        if (attachment == null) {
          attachment = "";
        }
        sb.append(attachment).append("</tbody></table></div><div id=\"feedBack\"><table width='100%'><tr class=TableHeader><td colspan=3>会签与点评</td></tr><tbody id=\"feedbackList\">");
        String feedbacks = feedbackLogic.getFeedbacksHtml(user, flowRun.getFlowId() , flowRun.getRunId() ,conn);
        if (feedbacks == null) {
          feedbacks = "";
        }
        sb.append(feedbacks).append("</tbody></table></div><div id=\"prcss\"><table width='100%'><tr class=TableHeader><td colspan=3>流程图</td></tr><tbody id=\"listTbody\">");
        String prcs =  workLogic.getPrcsHtml(flowRun.getRunId(), ft , conn );
        if (prcs == null) {
          prcs = "";
        }
        sb.append(prcs);
        sb.append("</tbody></table></div></body></html>");
        InputStream isb = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
        String fileName =  runName + File.separator +  runName + ".html";
        map.put(fileName, isb);
        List<File> list = this.getAttachement(aIds, aNames) ;
        for (int i = 0; i < list.size(); i++) {
          File file = list.get(i);
          InputStream in = new FileInputStream(file);
          String tmp = file.getName();
          int index = tmp.indexOf("_") + 1;
          tmp = tmp.substring(index);
          String fileName2 = runName + File.separator +  tmp;
          map.put(fileName2, in);
        }
      }
    } catch (Exception ex) {
      throw ex;
    } 
    return map;
  }
  public static void output(InputStream in ,  org.apache.tools.zip.ZipOutputStream out  , String fileName) {
    byte[] buf = new byte[1024];
    try {
      org.apache.tools.zip.ZipEntry ss =  new org.apache.tools.zip.ZipEntry(fileName);
      out.putNextEntry(ss);
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      out.closeEntry();
      out.flush();
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public List<File> getAttachement(String aIds , String aNames) {
    //处理文件
    List<File> list = new ArrayList<File>();
    if (aIds == null) {
      return list;
    }
    if (aNames == null) {
      return list;
    }
    
    String[] aI = aIds.split(",");
    String[] aN = aNames.split("\\*");
    for (int i = 0 ;i <  aI.length ;i ++) {
      String aId = aI[i];
      String aName = aN[i];
      int index = aId.indexOf("_");
      String hard = "";
      String str = "";
      if (index > 0) {
        hard = aId.substring(0, index);
        str = aId.substring(index + 1);
      } else {
        hard = "all";
        str = aId;
      }
      String path = YHAttachmentLogic.filePath + File.separator +  hard + File.separator +  str + "_" + aName;
      File file = new File(path);
      if(file.exists()){
        list.add(file);
      } else {
        String path2 = YHAttachmentLogic.filePath + File.separator + hard + File.separator +  str + "." + aName;
        File file2 = new File(path2);
        if(file2.exists()){
          list.add(file2);
        }
      }
    }
    return list;
  }
public static void main(String[] args) {
  File zipfile = new File("c:\\da.zip");
  File[] srcfile = new File[2];
  srcfile[0] = new File("c:\\aaaa.txt");
  srcfile[1] = new File("c:\\bb.txt");
  try {
    org.apache.tools.zip.ZipOutputStream out = new  org.apache.tools.zip.ZipOutputStream(new FileOutputStream(
       zipfile));
    out.setEncoding("GBK");
     StringBuffer sb = new StringBuffer();
     sb.append("aaaaaasss");
     InputStream isb = new ByteArrayInputStream(sb.toString().getBytes());
     //output(isb , out ,"dir" , "哈哈.html");
     for (int i = 0; i < srcfile.length; i++) {
     InputStream in = new FileInputStream(srcfile[i]);
     //output(in , out,"dir" , srcfile[i].getName());
   }
   out.close();
  } catch (IOException e) {
   e.printStackTrace();
  }
}
}
