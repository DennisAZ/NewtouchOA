package yh.core.funcs.doc.flowrunRec.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHTurnConditionUtility;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.doc.data.YHDocFlowRunPrcs;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.data.YHDocRun;
import yh.core.funcs.doc.logic.YHFlowRunLogLogic;
import yh.core.funcs.doc.logic.YHFlowRunLogic;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.email.logic.YHEmailUtil;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;


public class YHMyWorkLogic {
  /**
   * 去掉最后一个逗号
   * @param str
   * @return
   */
  public static String getOutOfTail(String str) {
    if (str == null) {
      return str ;
    }
    if (str.endsWith(",") ) {
      str = str.substring(0, str.length() - 1);
    }
    return str;
  }
  public String getMyWorkList( YHPerson user , int pageIndex, String flowIdStr  , int showLen , String typeStr , String sortId , Connection conn , String filedList, String realPath, boolean isDesk) throws Exception{
    StringBuffer sb = new StringBuffer();
    Statement stm =null;
    ResultSet rs = null;//结果集
    List<Map> list = new ArrayList();
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
        + " ,b.FREE_OTHER "
        + " , b.SEQ_ID As FLOW_ID"
        + " ,b.FLOW_NAME "
        + " ,b.FLOW_TYPE "
        + " ,b.LIST_FLDS_STR "
        + " ,b.FORM_SEQ_ID "
        + " ,c.RUN_NAME "
        + " , p.USER_NAME"
        + " , c.BEGIN_USER as userId"
        + ", r.title"
        + "  from oa_officialdoc_fl_run_PRCS  a, oa_officialdoc_fl_type  b, oa_officialdoc_run  c  , PERSON p , oa_officialdoc_fl_sort s , oa_officialdoc_rec_register r"
        + " WHERE  "
        + " p.SEQ_ID = c.BEGIN_USER AND"
        + "  a.RUN_ID=c.RUN_ID and  "
        + "  c.FLOW_ID=b.SEQ_ID and  "
        + "  s.SEQ_ID=b.FLOW_SORT and  "
        + "  c.DEL_FLAG='0' and  "
        + " a.CHILD_RUN = 0 AND "
        + " c.RUN_ID = r.RUN_ID AND "
        + "  a.USER_ID=" + user.getSeqId();
        
      if (isDesk) {
        query += " and c.END_TIME IS NULL ";
      }
      if("6".equals(typeStr)){
        query += "  and (a.PRCS_FLAG='1' or a.PRCS_FLAG='2')";
      } else {
        query += "  and a.PRCS_FLAG='"+ typeStr +"' ";
      }
      
      if (flowIdStr != null 
          && !"".equals(flowIdStr) 
          && !"0".equals(flowIdStr) ) {
        query += " and b.SEQ_ID = " + flowIdStr;
      }
      if (!YHUtility.isNullorEmpty(sortId)) {
        sortId = YHWorkFlowUtility.getOutOfTail(sortId);
        query += " and s.SEQ_ID IN (" + sortId + ")";
      }
      query += " order by a.RUN_ID DESC,a.PRCS_TIME DESC";
      
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
     //开始索引

      pgStartRecord = (pageIndex - 1) * showLen + 1;
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
        mapTmp.put("PRCS_FLAG",  rs.getString("PRCS_FLAG"));
        mapTmp.put("TITLE", rs.getString("TITLE"));
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
      String prcsFlag = (String)  map.get("PRCS_FLAG");
      String title = YHUtility.encodeSpecial(YHUtility.null2Empty((String)  map.get("TITLE")));
      boolean isHaveDelPriv = this.isHaveDelPriv(runId, user, conn);
      sb.append("{");
      sb.append("seqId:" +  map.get("SEQ_ID"));
      sb.append(",prcsFlag:\"" + prcsFlag +"\"");
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
      if ("1".equals(flowType)) {
        Statement stm4 = null;
        ResultSet rs4 = null;
        String prcsName = "流程步骤已删除";
        try {
          String queryStr = "SELECT PRCS_NAME from oa_officialdoc_fl_process WHERE FLOW_SEQ_ID="+ flowId +" AND PRCS_ID=" + flowPrcs; 
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
      sb.append(",opFlag:" + opFlag);
      sb.append(",flowPrcs:" + flowPrcs);
      sb.append(",title:\"" + title + "\"");
      sb.append(",freeOther:" +  (Integer)map.get("FREE_OTHER"));
      sb.append(",isHaveDelPriv:" + isHaveDelPriv);
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
  public String getEndWorkList1( YHPerson user,
      int pageIndex, String flowIdStr, int showLen, String opFlag  , String sortId , Connection conn , String filedList, String realPath, boolean isDesk) throws Exception {
    StringBuffer sb = new StringBuffer();
    Statement stm =null;
    ResultSet rs = null;//结果集
    int pageCount = 0;//页码数


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
        + ", r.title"
        + "  from oa_officialdoc_fl_run_PRCS  a, oa_officialdoc_fl_type  b, oa_officialdoc_run  c , person p , oa_officialdoc_rec_register r "
        + " WHERE  "
        + " p.seq_id = c.begin_user "
        + "  and a.RUN_ID=c.RUN_ID and  "
        + "  a.RUN_ID = r.RUN_ID and"
        + "  c.FLOW_ID=b.SEQ_ID and  "
        + "  a.CHILD_RUN = 0 AND "
        + " a.PRCS_ID = (SELECT MAX(f.prcs_id) from oa_officialdoc_fl_run_PRCS f where f.USER_ID = a.USER_ID AND f.RUN_ID = c.RUN_ID) AND "
        + "  c.DEL_FLAG='0' and  "
        + "  a.USER_ID=" + user.getSeqId()
         + " and   (a.PRCS_FLAG = '4' or a.PRCS_FLAG = '3') " + tmp;
      
      if (isDesk) {
        query += " and c.END_TIME IS NULL ";
      }
      
      if (flowIdStr != null 
          && !"".equals(flowIdStr) 
          && !"0".equals(flowIdStr) ) {
        query += " and b.SEQ_ID = " + flowIdStr;
      }
      if (!YHUtility.isNullorEmpty(sortId)) {
        sortId = YHWorkFlowUtility.getOutOfTail(sortId);
        query += " and b.FLOW_SORT IN (" + sortId + ")";
      }
      query += " order by a.RUN_ID DESC,a.PRCS_TIME DESC";
      
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
     //开始索引
      pgStartRecord = (pageIndex - 1) * showLen + 1;
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
        mapTmp.put("TITLE",rs.getString("TITLE"));
        mapTmp.put("FREE_OTHER", rs.getInt("FREE_OTHER"));
        mapTmp.put("state", state);
        mapTmp.put("userName", rs.getString("USER_NAME"));
        mapTmp.put("FLOW_PRCS",  String.valueOf(rs.getInt("FLOW_PRCS")));
        mapTmp.put("OP_FLAG",  rs.getInt("OP_FLAG"));
        list.add(mapTmp);
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
      String title = YHUtility.encodeSpecial(YHUtility.null2Empty((String)  map.get("TITLE")));
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
      sb.append(",title:\"" + title + "\"");
      sb.append("},");
    }
    if( list.size() >0 ) {
      sb.deleteCharAt(sb.length() - 1); 
    }
    long date4 = System.currentTimeMillis();
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
  /**
   * 判断当前用户是否有些流程的删除权限

   * @return false 为没有 , true为有
   * @throws Exception 
   */
  public boolean isHaveDelPriv(int runId , YHPerson user , Connection conn) throws Exception{
    YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
    //取出权限字符串

    String roleStr = roleUtility.runRole(runId,  1, user , conn);
    
    boolean flag = true;
    String query = "select 1 from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" WHERE RUN_ID="+ runId +" AND PRCS_ID>1";
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
    boolean contion = !(YHWorkFlowUtility.findId(roleStr, "2") && flag)
                         && !YHWorkFlowUtility.findId(roleStr , "1")
                         && !YHWorkFlowUtility.findId(roleStr , "3");
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
    String query = "SELECT 1 from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" WHERE  " 
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
          + " "+ YHWorkFlowConst.FLOW_RUN_PRCS +" "
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
        String query1 = "select FLOW_PRCS,PARENT from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" where "
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
                YHWorkFlowUtility.deleteTable(YHWorkFlowConst.FLOW_RUN_PRCS, "RUN_ID="+ runId +" and PRCS_ID >=" + prcsIdNew, conn);
              } else {
                parent = YHWorkFlowUtility.getOutOf(parent, String.valueOf(flowPrcs));
                String condition = "RUN_ID="+ runId 
                                      +" and PRCS_ID >="+ prcsIdNew 
                                      +" and FLOW_PRCS ="+ flowPrcsNext;
                                     // +" AND FIND_IN_SET('$FLOW_PRCS',PARENT)";
                YHWorkFlowUtility.updateTable(YHWorkFlowConst.FLOW_RUN_PRCS, "PARENT='" + parent + "'", condition, conn);
              }
            } else {
              YHWorkFlowUtility.deleteTable(YHWorkFlowConst.FLOW_RUN_PRCS, "RUN_ID="+ runId +" and PRCS_ID >=" + prcsIdNew, conn);
            }
          } else {//自由流程
            YHWorkFlowUtility.deleteTable(YHWorkFlowConst.FLOW_RUN_PRCS, "RUN_ID="+ runId +" and PRCS_ID=" + prcsIdNew, conn);
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
    YHWorkFlowUtility.updateTable(YHWorkFlowConst.FLOW_RUN_PRCS, " PRCS_FLAG='2'", condition, conn);
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

    String query = "SELECT 1 from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" WHERE  " 
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
      if(rs4.next()){
        flag = true;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs4, null); 
    }
    //是管理员或者是主办人且下一步没有接收

    boolean contion = (YHWorkFlowUtility.findId(roleStr, "1") || YHWorkFlowUtility.findId(roleStr, "2")) && !flag ;
    return contion;
  }
  /**
   * 删除流程实例 
   * @param runId
   * @param loginUser
   * @param conn
   * @throws Exception
   */
  public void delRun(int runId ,YHPerson loginUser, Connection conn) throws Exception{
    //没有权限删除
    if (!this.isHaveDelPriv(runId, loginUser, conn)) {
      return ;
    }
    String update = "update "+ YHWorkFlowConst.FLOW_RUN +" set DEL_FLAG=1 where RUN_ID=" + runId;
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
  public void cancelRun( int flowId, int runId, int prcsId, int flowPrcs , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    String query =  "SELECT 1 "
        + "FROM "+ YHWorkFlowConst.FLOW_RUN_PRCS +"  WHERE "
        + "RUN_ID='"+runId+"' AND PRCS_ID>1";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      boolean flag = rs.next();
      if(flag){
        return ;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    
    YHDocRun flowRun = this.getFlowRunByRunId(runId , conn);
    if(flowRun != null && flowRun.getParentRun() != 0){
      return ;
    }
    YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
    YHDocFlowType flowType = flowTypeLogic.getFlowTypeById(flowId , conn);
    //处理自动文号
    if(flowType.getAutoName() != null && flowType.getAutoName().indexOf("{N}") != -1){
    //没有大于此流水号的工作，则处理自动文号表达式
      String query1 = "select 1 from "+ YHWorkFlowConst.FLOW_RUN +" WHERE "
        + " RUN_ID>'"+ runId +"'  "
        + "AND FLOW_ID='"+ flowId +"'";
      Statement stm2 = null;
      ResultSet rs2 = null;
      try {
        stm2 = conn.createStatement();
        rs2 = stm2.executeQuery(query1);
        if(!rs2.next()){
          flowType.setAutoNum(flowType.getAutoNum() - 1);
          orm.updateSingle(conn, flowType);
          //conn.commit();
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
    }
    //---删除附件 --
    //---删除会签附件 --
      String deleteFeedback ="delete from "+ YHWorkFlowConst.FLOW_RUN_FEEDBACK +" where  "
      + "RUN_ID=" + runId;
      YHWorkFlowUtility.updateTableBySql(deleteFeedback, conn);
      String deleteRunData = "delete from "+ YHWorkFlowConst.FLOW_RUN_DATA +" where  "
        + "RUN_ID=" + runId;
      YHWorkFlowUtility.updateTableBySql(deleteRunData, conn);
      String deleteFlowRunPrcs = "delete from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" where  "
        + "RUN_ID=" + runId;
      YHWorkFlowUtility.updateTableBySql(deleteFlowRunPrcs, conn);
      String deleteFlowRun="delete from "+ YHWorkFlowConst.FLOW_RUN +" where "
        + " RUN_ID=" + runId;
      YHWorkFlowUtility.updateTableBySql(deleteFlowRun, conn);
  }
  /**
   * 根据runID取得工作
   * @param runId
   * @return
   * @throws Exception
   */
  public YHDocRun getFlowRunByRunId(int runId , Connection conn) throws Exception {
    // TODO Auto-generated method stub
    HashMap map = new HashMap();
    map.put("RUN_ID", runId);
    YHORM orm = new YHORM();
    YHDocRun flowRun = (YHDocRun) orm.loadObjSingle(conn, YHDocRun.class, map);
    return flowRun;
  }
  /**
   * 提醒所有经办人和流程发起人
   * @param conn
   * @param remindFlag
   * @param content
   * @param contextPath
   * @param runId
   * @throws Exception 
   */
  public void remindAllAndSend(Connection conn , int remindFlag , String content , String contextPath , int runId , int userId , int flowId) throws Exception{
    Map map2 = new HashMap();
    map2.put("RUN_ID", runId);
    YHORM orm = new YHORM();
    YHDocRun flowRun = (YHDocRun) orm.loadObjSingle(conn, YHDocRun.class  , map2);
    String id = String.valueOf(flowRun.getBeginUser());
    boolean has = false;
    if ((remindFlag&0x20)>0 ) {
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType("40");
      sb.setContent(content);
      sb.setFromId(userId);
      sb.setToId(id);
      sb.setRemindUrl(YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrunRec/list/print/index.jsp?runId="+runId+"&flowId="+ flowId + "&openFlag=1&width=800&height=600");
      YHSmsUtil.smsBack(conn, sb);
      has = true;
    }
    //短信提醒所有经办人
    String ids = "";
    String query = "SELECT USER_ID from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" where RUN_ID=" + runId;
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        int userId1 = rs.getInt("USER_ID");
        if (!YHWorkFlowUtility.findId(ids, String.valueOf(userId)) 
            && userId1 != userId) {
          //没有，或者userId
          if (!has || userId1 != flowRun.getBeginUser()) {
            ids += userId1 + ",";
          } 
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    if ((remindFlag&4 ) >0 ) {
      if (!"".equals(ids)){
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("41");
        sb.setContent(content);
        sb.setFromId(userId);
        sb.setToId(ids);
        sb.setRemindUrl(YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrunRec/list/print/index.jsp?runId="+runId+"&flowId="+ flowId + "&openFlag=1&width=800&height=600");
        YHSmsUtil.smsBack(conn, sb);
      }
    //短信提醒发起人
    } 
    //手机提醒发起人


    if ((remindFlag&0x10)>0) {
      YHMobileSms2Logic ms2l = new YHMobileSms2Logic(); 
      ms2l.remindByMobileSms(conn, String.valueOf(id),userId, content, null);
    }
    //手机提醒所有人
    if ((remindFlag&2)>0) {
      YHMobileSms2Logic ms2l = new YHMobileSms2Logic(); 
      ms2l.remindByMobileSms(conn, ids ,userId, content, null);
    }
  //短信提醒下一步经办人 短信(remindFlag&0x100)>0 邮件(remindFlag&0x40)>0
  //短信提醒发起人 短信(remindFlag&0x20)>0 邮件 (remindFlag&8)>0
 //短信提醒所有经办人 短信(remindFlag&4)>0 邮件 (remindFlag&1)>0
  }
  /**
   * 提醒下一步经办人
   * @param conn
   * @param content
   * @param contextPath
   * @param runId
   * @param userId
   * @param skin 
   * @throws Exception 
   */
  public void remindNext(Connection conn  
      , int runId , int flowId , int prcsId , int flowPrcs
      , String content , String contextPath , String prcsUser , int userId , String sortId, String skin) throws Exception {
    YHSmsBack sb = new YHSmsBack();
    YHORM orm = new YHORM();
    YHDocFlowType flowType = (YHDocFlowType) orm.loadObjSingle(conn, YHDocFlowType.class , flowId);
    YHTurnConditionUtility turnUtility = new YHTurnConditionUtility();
    //Map formData = turnUtility.getForm(flowType.getFormSeqId(), runId, conn);
    String[] ss = prcsUser.split(",");
    for (String tmp : ss) {
      String sql = " select USER_NAME from PERSON where SEQ_ID = " + tmp ;
      PreparedStatement ps = null;
      ResultSet rs = null ;
      try {
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        if(rs.next()){
          String toId = rs.getString(1);
          if(toId != null){
            sb.setSmsType("7");
            //content = content.replace("#{办理人}" , toId);
            //content = this.replaceAllFromItem(content, formData);
            sb.setContent(content);
            sb.setFromId(userId);
            sb.setToId(tmp);
            sb.setRemindUrl(YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrunRec/list/inputform/index.jsp?skin="+skin+"&sortId="+sortId +"&runId=" + runId + "&flowId=" + flowId + "&prcsId=" + prcsId + "&flowPrcs=" + flowPrcs);
            YHSmsUtil.smsBack(conn, sb);
          }
        }
      } catch (Exception e) {
        throw e;
      } finally {
        YHDBUtility.close(ps, rs, null);
      }
      
    }
  }
  public void remaindEmail(int flowId  , int prcsId , int runId ,  Connection conn , YHPerson user , String imgPath , String contextPath) throws Exception {
    String query = "SELECT MAIL_TO from "+ YHWorkFlowConst.FLOW_PROCESS +" where FLOW_SEQ_ID="+flowId+" and PRCS_ID=" + prcsId;
    Statement stm = null;
    ResultSet rs = null;
    String mailTo = "";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()) {
        mailTo = rs.getString("MAIL_TO");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    if (mailTo != null && !"".equals(mailTo)) {
      Map map = new HashMap();
      map.put("RUN_ID", runId);
      YHFlowRunLogic frl = new YHFlowRunLogic();
      YHDocRun flowRun = frl.getFlowRunByRunId(runId , conn);
      YHFlowTypeLogic ftl = new YHFlowTypeLogic();
      YHDocFlowType ft = ftl.getFlowTypeById(flowId, conn);
      String  form = "";
      if (flowRun != null) {
        Map result = frl.getPrintForm(user, flowRun, ft ,true, conn , imgPath ) ;
        form = (String)result.get("form2");
      }
      //处理附件
      YHEmailUtil.emailNotifier(conn, user.getSeqId(), mailTo, flowRun.getRunName(), form, new java.util.Date(), "1" , contextPath , true);
    }
    
  }
  /**
   * 回退到prcsOld这步
   * @param loginUser
   * @param runId
   * @param flowId
   * @param prcsId
   * @param flowPrcs
   * @param prcsChoose
   * @param ip
   * @param skin 
   * @throws Exception
   */
  public void backTo(YHPerson loginUser
      , int runId
      , int flowId
      , int prcsId
      , int flowPrcs
      , String prcsPre
      , String ip 
      , String contextPath
      , String sortId
      , String skin, Connection conn) throws Exception{
    YHORM orm = new YHORM();
    Map map = new HashMap();
    map.put("RUN_ID", runId);
    map.put("PRCS_ID", Integer.parseInt(prcsPre));//回退的实际步骤
    List<YHDocFlowRunPrcs> runPrcs =  orm.loadListSingle(conn, YHDocFlowRunPrcs.class , map);
    if (runPrcs.size() > 0 ){
      String prcsUser = "";//经办人
      String prcsOpUser = "";//主办人
      String topFlag = "0";//主办方式,
      YHDocFlowRunPrcs flowPrcsTmp = runPrcs.get(0);//主要是取出flowPrcs和topFlag
      topFlag = flowPrcsTmp.getTopFlag();
      String flowPrcsTo = String.valueOf(flowPrcsTmp.getFlowPrcs());
      for(YHDocFlowRunPrcs tmp : runPrcs){
        //1说明是主办
        if ("1".equals(tmp.getOpFlag())) {
          prcsOpUser = String.valueOf(tmp.getUserId()) ;
        }
        prcsUser += tmp.getUserId() + ",";
      }
      Map opUserMap = new HashMap();
      opUserMap.put("prcsOpUser_" + flowPrcsTo , prcsOpUser);
      opUserMap.put("prcsUser_" + flowPrcsTo, prcsUser);
      opUserMap.put("topFlag_" + flowPrcsTo, topFlag);
      YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
      flowRunLogic.turnNext( runId, flowId, prcsId, flowPrcs, flowPrcsTo,  opUserMap , conn);
      //工作流日志
      String userNameStr = "";
      YHPersonLogic personLogic = new YHPersonLogic();
      String content = "回退至步骤" + prcsPre ;
      YHFlowRunLogLogic logLogic = new YHFlowRunLogLogic();
      logLogic.runLog(runId,prcsId ,flowPrcs,loginUser.getSeqId(),1,content,ip ,conn);
      String prcsUser2 = (String)opUserMap.get("prcsUser_" + flowPrcsTo);
      YHSmsBack sb = new YHSmsBack();
      sb.setSmsType("7");
      String remindContent = "工作流回退提醒：流水号"+runId+"的工作回退给您处理！";
      sb.setContent(remindContent);
      sb.setFromId(loginUser.getSeqId());
      sb.setToId(prcsUser2);
      sb.setRemindUrl(YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrunRec/list/inputform/index.jsp?skin="+skin+"&sortId="+ sortId +"&runId=" + runId + "&flowId=" + flowId + "&prcsId=" + (prcsId + 1)+ "&flowPrcs=" + prcsPre);
      YHSmsUtil.smsBack(conn, sb);
    }
  }
}
