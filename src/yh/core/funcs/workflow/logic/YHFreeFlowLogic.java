package yh.core.funcs.workflow.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.workflow.data.YHFlowRunPrcs;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHTurnConditionUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHFreeFlowLogic {
  /**
   * 取得自由流程转交页面的相关数据

   * @param loginUser
   * @param runId
   * @param prcsId
   * @return String {flowName:'自由流程',isPreview:true , runName:'自由流程测试(2010-03-09 10:56:34)', formItem:'',prcsList:[{prcsId:1,handlerState:'<font color=blue>系统管理员[主办人](已办结)</font>'},{prcsId:2,handlerState:'<font color=red>系统管理员[主办人](办理中)</font>,<font color=red>dddd(未接收)</font>' , isNowPrcs:true}]};
   * @throws Exception
   */
  public String getTurnData(YHPerson loginUser,int flowId, int runId, int prcsId , Connection conn ,boolean isManage) throws Exception{
    StringBuffer sb = new StringBuffer();
    
    YHORM orm = new YHORM();
    String formItem = "";
    YHFlowTypeLogic flowTypelogic = new YHFlowTypeLogic();
    YHFlowType ft = flowTypelogic.getFlowTypeById(flowId , conn);
    if (prcsId == 1) {
      YHFlowFormLogic ffLogic = new YHFlowFormLogic();
      String flowDoc = ft.getFlowDoc();
      int formId = ft.getFormSeqId();
      formItem = ffLogic.getTitle(conn, formId);

      if (flowDoc.equals("1")) {
        formItem += ",[A@],";
      } else {
        formItem += ",[B@],";
      }
    } else {
      Map map = new HashMap();
      map.put("RUN_ID", runId);
      map.put("PRCS_ID", prcsId);
      map.put("OP_FLAG", 1);
      YHFlowRunPrcs runPrcs = (YHFlowRunPrcs) orm.loadObjSingle(conn, YHFlowRunPrcs.class, map);
      if (runPrcs != null) {
        formItem = runPrcs.getFreeItem();
      }
    }
    //取出流程名以及流程中的预设自段
    String flowName = "";
    boolean freePreSet = false;
    if (ft != null) {
      flowName =  ft.getFlowName();
      if (ft.getFreePreset() != null 
          && "1".equals(ft.getFreePreset())) {
        freePreSet = true;
      }
    }
    //取出实例 名
    String query2 = "SELECT  " 
        + " RUN_NAME  " 
        + " from oa_fl_run WHERE  " 
        + " RUN_ID=" + runId;
    String runName = "";
    Statement stm1 = null;
    ResultSet rs1 = null;
    try {
      stm1 = conn.createStatement();
      rs1 = stm1.executeQuery(query2);
      if ( rs1.next()) {
        runName = rs1.getString("RUN_NAME");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm1, rs1, null); 
    }
    sb.append("{");
    sb.append("flowName:'" + flowName +"'");
    sb.append(",isPreview:" + freePreSet);
    runName = YHWorkFlowUtility.getRunName(runName);
    sb.append(",runName:'"  + runName + "'");
    sb.append(",formItem:'" + formItem + "'");
    sb.append(",prcsList:[");
    String notAllFinish = "";
    int turnforbindden = 0 ;
    for (int i = 1 ;i <= prcsId ;i ++ ) {
      String query3 = "SELECT * from oa_fl_run_prcs where  " 
        + " RUN_ID="+ runId +"  " 
        + " and PRCS_ID=" + i 
        + " order by OP_FLAG desc";
      Statement stm3 = null;
      ResultSet rs3 = null;
      String userNameStr = "";
      try {
        stm3 = conn.createStatement();
        rs3 = stm3.executeQuery(query3);
        while (rs3.next()) {
          int userId = rs3.getInt("USER_ID");
          int prcsFlag = Integer.parseInt(rs3.getString("PRCS_FLAG"));
          int opFlag = Integer.parseInt(rs3.getString("OP_FLAG"));
          
          String name = this.getUserName(userId, conn);
          if (name != null) {
            if (opFlag == 1) {
              name += "[主办人]";
            }
            if (prcsFlag == 1) {
              userNameStr += "<font color=red>"  + name +"(未接收)</font>,";
            } else if (prcsFlag == 2) {
              userNameStr += "<font color=red>"  + name +"(办理中)</font>,";
            } else if (prcsFlag == 4) {
              userNameStr += "<font color=blue>"  + name +"(已办结)</font>,";
            } else {
              userNameStr += name + ",";
            }
          }
          //为主办
          if (i == prcsId && prcsFlag != 4 && userId != loginUser.getSeqId()) {
            notAllFinish += name + ",";
          }
          if (i == prcsId 
              && (prcsFlag == 3 || prcsFlag == 4 )
              && userId == loginUser.getSeqId()
              && !isManage) {
            turnforbindden = 1;
          } else {
            turnforbindden = 0;
          }
        }
        if (userNameStr.endsWith(",")) {
          userNameStr = userNameStr.substring(0, userNameStr.length() - 1);
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm3, rs3, null); 
      }
      sb.append("{");
      sb.append("prcsId:" + i);
      sb.append(",handlerState:'" + userNameStr + "'");
      if (i == prcsId ) {
        sb.append(",isNowPrcs:true");
      }
      sb.append("},");
    }
    if (prcsId > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    if (turnforbindden == 1) {
      sb = new StringBuffer();
      sb.append("{error:\"已经转交，不能重复转交\"}");
      return sb.toString();
    }
    String preSet = "";
    String queryPre = "SELECT * from oa_fl_run_prcs where " 
        + "  RUN_ID= "+ runId +"  " 
        + " and PRCS_ID="+ (prcsId + 1) 
        + " and PRCS_FLAG='5'  " 
        + " order by OP_FLAG desc";
    Statement stm5 = null;
    ResultSet rs5 = null;
    try {
      stm5 = conn.createStatement();
      rs5 = stm5.executeQuery(queryPre);
      while ( rs5.next()) {
        int userId = rs5.getInt("USER_ID");
        int opFlag = Integer.parseInt(rs5.getString("OP_FLAG"));
        String userName = this.getUserName(userId, conn);
        if (userName != null) {
          if (opFlag == 1 ){
            userName += "[主办人]";
          }
          preSet += userName + ",";
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm5, rs5, null); 
    }
    if (preSet != null && !"".equals(preSet)) {
      preSet += "<input type=hidden name=preSet id=preSet value=true><input type=hidden name=prcsUser id=prcsUser value=1><input type=hidden name=prcsOpUser id=prcsOpUser value=1><input type=hidden name=topFlag id=topFlag value=1>";
      sb.append(",nextPre:'<font color=blue>" + preSet + "</font>'");
    }
    YHConfigLogic logic = new YHConfigLogic();
    String paraValue = logic.getSysPar("SMS_REMIND", conn);
    String[] remindArray = paraValue.split("\\|");
    String smsRemind = "";
    String sms2remind = "";
    if (remindArray.length == 1) {
      smsRemind = remindArray[0];
    } else if (remindArray.length >= 2) {
      smsRemind = remindArray[0];
      sms2remind = remindArray[1];
    } 
    sb.append(",smsRemind:'"  + smsRemind + "', sms2Remind:'" + sms2remind + "'");
    String query = "select TYPE_PRIV,SMS2_REMIND_PRIV from oa_msg2_priv";
    String typePriv = "";
    String sms2RemindPriv = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if(rs.next()){
        typePriv = rs.getString("TYPE_PRIV");
        sms2RemindPriv = rs.getString("SMS2_REMIND_PRIV");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    //检查该模块是否允许手机提醒
    boolean sms2PrivNext = false ;
    if (YHWorkFlowUtility.findId(typePriv, "7") 
        && YHWorkFlowUtility.findId(sms2RemindPriv , String.valueOf(loginUser.getSeqId()))) {
      sms2PrivNext = true;
    }
    sb.append (",sms2Priv:" + sms2PrivNext) ;
    
    sb.append("}");
    return sb.toString();
  }
  /**
   * 根据userId取得用户名
   * @param userId
   * @param conn
   * @return
   * @throws Exception 
   */
  public String getUserName (int userId , Connection conn) throws Exception {
    String query4 = "select  " 
        + " USER_NAME  " 
        + " from PERSON where  " 
        + " SEQ_ID = " + userId;
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query4);
      if ( rs4.next()) {
        String name = rs4.getString("USER_NAME");
        return name ;
      } else {
        return null;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs4, null); 
    }
  }
  public String  turnNext(YHPerson user ,int flowId , int runId , int prcsId , Connection conn , boolean preSet , List<Map> preList , String itemOld) throws Exception {
    YHORM orm = new YHORM();
    YHFlowType flowType = (YHFlowType) orm.loadObjSingle(conn, YHFlowType.class, flowId);
    String freeOther = "";
    if (flowType != null && flowType.getFreeOther() != null) {
      freeOther = flowType.getFreeOther();
    }
    //Date date = new Date();
    //String prcsUserNext = prcsUser;
    String remindUser = "";
    int prcsIdNext = prcsId + 1 ;
    //是否为预设的步骤
    if (!preSet) {
      for (int i = 0 ; i < preList.size() ; i++) {
        Map tmp = preList.get(i) ;
        int prcsIdTmp = (Integer) tmp.get("prcsId");
        String prcsOpUser = (String) tmp.get("prcsOpUser");
        String prcsOpUserOld = prcsOpUser;
       
        String prcsUser1 = (String) tmp.get("prcsUser");
        Map otherMap = new HashMap();
        if ("2".equals(freeOther)) {
          YHTurnConditionUtility tu = new YHTurnConditionUtility();
          prcsOpUser = tu.turnOther(prcsOpUser, flowId, runId, prcsId, "0", "", otherMap, conn);
          //委托经办人
          prcsUser1 = tu.turnOther(prcsUser1, flowId, runId, prcsId, "0", prcsOpUserOld, otherMap, conn);
        }
        int topFlag = 0 ;
        String sTopFlag = (String) tmp.get("topFlag");
        if (sTopFlag != null && !"".equals(sTopFlag)) {
          topFlag = Integer.parseInt(sTopFlag);
        }
        int prcsFlag = 5;
        if (i == 0) {
          prcsFlag = 1;
          remindUser = prcsUser1;
        }
        String freeItem = (String) tmp.get("freeItem");
        if (freeItem == null || "".equals(freeItem)) {
          freeItem = itemOld;
        }
        String[] str = prcsUser1.split(",");
        for (String tmpStr : str) {
          int opFlag = 0;
          if (tmpStr.equals(prcsOpUser) || topFlag == 1) {
            opFlag = 1;
          } 
          //无主办会签
          if (topFlag == 2) {
            opFlag = 0 ;
          }
          if (opFlag == 0 ) {
            freeItem = "";
          }
          String insertRunPrcs = "insert into oa_fl_run_prcs("
            + " RUN_ID"
            + " ,PRCS_ID"
            + " ,USER_ID"
            + " ,PRCS_FLAG"
            + " ,OP_FLAG"
            + " ,TOP_FLAG"
            + " ,FREE_ITEM"
            + " ) values ("
            + runId
            + " ," + prcsIdTmp
            + " ," + tmpStr
            + " ,'"+ prcsFlag +"'"
            + " ,'"+ opFlag +"'"
            + " ,'"+ topFlag +"'"
            + " ,'"+ freeItem +"'"
            + " )";
          Statement stm = null;
          try {
            stm = conn.createStatement();
            stm.executeUpdate(insertRunPrcs);
          } catch (Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm, null, null);
          }
        }
      }
    } else {
      String query =  "select USER_ID FROM oa_fl_run_prcs WHERE " 
        + "  PRCS_FLAG='5'  " 
        + " AND  RUN_ID='"+ runId +"'  " 
        + " and PRCS_ID='"+ prcsIdNext +"'";
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        while (rs.next()){
          remindUser += rs.getInt("USER_ID") + ",";
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null); 
      }
      
      String updateFlowRun =  "update oa_fl_run_prcs set " 
        + "  PRCS_FLAG='1'  " 
        + " WHERE " 
        + "  RUN_ID='"+ runId +"'  " 
        + " and PRCS_ID='"+ prcsIdNext +"'";
      Statement stm2 = null;
      try {
        stm2 = conn.createStatement();
        stm2.executeUpdate(updateFlowRun);
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, null, null);
      }
    }
  //-- 设置当前步骤所有人为已转交 --
   String query = "update oa_fl_run_prcs set  " 
     + " PRCS_FLAG='3' " 
     + " WHERE RUN_ID="+ runId
     + " and PRCS_ID=" + prcsId;
   Statement stm = null;
   try {
     stm = conn.createStatement();
     stm.executeUpdate(query);
   } catch (Exception ex) {
     throw ex;
   } finally {
     YHDBUtility.close(stm, null, null);
   }

    //-- 设置当前步骤自己的结束时间 --
   Timestamp time =  new  Timestamp(new Date().getTime());
    query = "update oa_fl_run_prcs set  " 
     + " DELIVER_TIME=?" 
     + " WHERE  " 
     + " RUN_ID=" + runId 
     + " and PRCS_ID=" + prcsId 
     + " and USER_ID=" + user.getSeqId();
    
    PreparedStatement stm1 = null;
    try {
      stm1 = conn.prepareStatement(query);
      stm1.setTimestamp(1, time);
      stm1.executeUpdate();
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm1, null, null);
    }
    YHFlowRunLogLogic logLogic = new YHFlowRunLogLogic();
    YHPersonLogic pLogic = new YHPersonLogic();
    String userName = pLogic.getNameBySeqIdStr(remindUser, conn);
    String content = "转交至下一步,办理人:" + userName;
    logLogic.runLog(runId, prcsId, 0, user.getSeqId(), 1, content, "", conn);
    return remindUser;
  }
  /**
   * 自由流程的结束
   * @param runId
   * @param flowId
   * @param prcsId
   * @param user
   * @param conn
   * @return
   * @throws Exception
   */
  public String stop(int runId , int flowId ,int prcsId , YHPerson user ,Connection conn) throws Exception {
    YHORM orm = new YHORM();
    boolean isManage = false;//是否是管理
    YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
    //验证是否有权限,并取出权限字符串
    String roleStr = roleUtility.runRole(runId, flowId, prcsId, user , conn);
    if ("".equals(roleStr)) {
      return "没有此流程办理权限！";
    }
    //--- 检查有无后续预设步骤 ---
    int prcsIdNext = prcsId + 1;
    String query = "select 1 from oa_fl_run_prcs where "
        + " RUN_ID=" + runId 
        + " and PRCS_ID=" + prcsIdNext
        + " and PRCS_FLAG='5'";
    
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if(rs.next()){
        if (isManage) {
          //是管理者,删除后面预设步骤
          YHWorkFlowUtility.deleteTable("oa_fl_run_prcs", " RUN_ID="+ runId +" and PRCS_ID >= " + prcsIdNext, conn);
        } else {
          //不是管理者
          return "本流程存在后续预设步骤，不能结束!";
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    
    //--- 写自己的办理完毕状态 ---
    if(!isManage) {
      Map map = new HashMap();
      map.put("RUN_ID", runId);
      map.put("USER_ID", user.getSeqId());
      map.put("PRCS_ID", prcsId);
      YHFlowRunPrcs flowRun = (YHFlowRunPrcs) orm.loadObjSingle(conn, YHFlowRunPrcs.class, map);
      flowRun.setDeliverTime(new Date());
      orm.updateSingle(conn, flowRun);
    } else {//监控人结束补全步骤开始和结束时间为当前时间
      String sql = "update oa_fl_run_prcs  set " 
        + " DELIVER_TIME=? " 
        + " WHERE  " 
        + " RUN_ID=" + runId 
        + " and DELIVER_TIME is null";
      Timestamp time =  new  Timestamp(new Date().getTime());
      PreparedStatement stm2 = null;
      try{
        stm2 = conn.prepareStatement(sql);
        stm2.setTimestamp(1, time);
        stm2.executeUpdate();
      }catch(Exception ex){
        throw ex;
      }finally{
        YHDBUtility.close(stm2, null , null);
      }
      query = "update oa_fl_run_prcs set  " 
        + " PRCS_TIME=?" 
        + " WHERE  " 
        + " RUN_ID=" + runId
        + " and PRCS_TIME is null";
      
      PreparedStatement stm3 = null;
      try {
        stm3 = conn.prepareStatement(sql);
        stm3.setTimestamp(1, time);
        stm3.executeUpdate();
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm3, null , null);
      }
      
    }
    //--- 结束本流程 ---
    YHWorkFlowUtility.updateTable("oa_fl_run_prcs", "PRCS_FLAG='4'", "RUN_ID=" +runId, conn);
    //--- 写入结束时间 ---
    String writeEndTime = "update oa_fl_run set   " 
        + " END_TIME=?   " 
        + " where   " 
        + " RUN_ID="+ runId;
    Timestamp time =  new  Timestamp(new Date().getTime());
    PreparedStatement stm4 = null;
    try {
      stm4 = conn.prepareStatement(writeEndTime);
      stm4.setTimestamp(1, time);
      stm4.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, null , null);
    }
    //--- 流程日志 ---
    String content = "";
    if (!isManage) {
      content = "结束流程";
    } else {
      content = user.getUserName() + "强制结束流程";
    }
    YHFlowRunLogLogic logic = new YHFlowRunLogLogic();
    logic.runLog(runId, prcsId, 0, user.getSeqId(), 1, content, "", conn);
    return null;
  }
}
