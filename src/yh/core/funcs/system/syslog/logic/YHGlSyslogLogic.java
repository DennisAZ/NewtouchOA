package yh.core.funcs.system.syslog.logic;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHGlSyslogLogic {
  public String getglsyslog(String type, String users, String statrtime, String endtime,String ip, String remark, String copytime,Connection conn, YHPerson user)
    throws Exception {
    StringBuffer sb = new StringBuffer();
    Statement stmt = null;
    ResultSet rs = null;
    try{
         sb.append("{");
         sb.append("listData:[");
         stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
         ResultSet.CONCUR_READ_ONLY);
         String queryGllogSql="";
         String startdate = "";
         String enddate = "";
         //YHOut.println(endtime.trim());
         if(statrtime != null && !statrtime.equals("")){
            startdate= YHDBUtility.getDateFilter("TIME", statrtime, ">=");
         }
         if(!YHUtility.isNullorEmpty(endtime)){
            enddate = YHDBUtility.getDateFilter("TIME", endtime, "<=");
            //YHOut.println("enddate::::::"+enddate);
         }
         if(copytime.equals("on")){
             if( YHUtility.isNullorEmpty(statrtime) && !YHUtility.isNullorEmpty(endtime) ) {
                 //queryGllogSql = "select * from (select seq_id, user_id, time, ip, type, remark from sys_log where time <= to_date('" + endtime + "','yyyy-mm-dd hh24:mi:ss') order by time desc)where rownum <= 90";
                 queryGllogSql = "select seq_id, user_id, time, ip, type, remark,user_name  from oa_sys_log where "+enddate+"";
             }else if( YHUtility.isNullorEmpty(endtime) && !YHUtility.isNullorEmpty(statrtime)){
                 queryGllogSql = "select seq_id, user_id, time, ip, type, remark,user_name  from oa_sys_log where "+startdate+" ";
             }else{
                 queryGllogSql = "select seq_id, user_id, time, ip, type, remark,user_name  from oa_sys_log where "+startdate+" " +
               	"and "+enddate+" ";
             }
         }else{
             if( YHUtility.isNullorEmpty(statrtime) && !YHUtility.isNullorEmpty(endtime)){
                queryGllogSql = "select seq_id, user_id, time, ip, type, remark,user_name  from oa_sys_log_"+copytime+" where "+enddate+" ";
             }
             else if( YHUtility.isNullorEmpty(endtime) && !YHUtility.isNullorEmpty(statrtime)){
                queryGllogSql = "select seq_id, user_id, time, ip, type, remark,user_name  from oa_sys_log_"+copytime+" where "+startdate+" ";
              }else{
                queryGllogSql = "select seq_id, user_id, time, ip, type, remark,user_name  from oa_sys_log_"+copytime+" where "+startdate+"" +
                   "and "+enddate+"";
              }
          }
          if (!"".equals(users) && users != null){
              queryGllogSql = queryGllogSql + " and user_id in ("
               + users + ")";
          }
          if (!"".equals(type) && type != null){
               queryGllogSql = queryGllogSql + " and type='"
               + type + "'";
           }
          if(!"".equals(ip) && ip != null){
             queryGllogSql = queryGllogSql + " and ip='"
             + ip + "'";
           }
          if(!"".equals(remark) && remark != null){
               queryGllogSql = queryGllogSql + " and remark like "
               + "'%" +YHDBUtility.escapeLike(remark)+ "%'" + YHDBUtility.escapeLike();
           }
        queryGllogSql = queryGllogSql + "order by time desc";
        rs = stmt.executeQuery(queryGllogSql);
        List<Map> list = new ArrayList();
        String seqIdStr = "";
        String state = "";
        int rowCnt =0;
        while (rs.next()) {
             int seqId = rs.getInt("seq_id");
             int userId = rs.getInt("user_id");
             Timestamp t = rs.getTimestamp("time");
             Date date = new Date(t.getTime());
             String dates= YHUtility.getDateTimeStr(date);
             String Ip = rs.getString("ip");
             String types = rs.getString("type");
             String remarks = rs.getString("remark");
             String userName = rs.getString("user_name");
             Map mapTmp = new HashMap();
             mapTmp.put("seqId", String.valueOf(seqId));
             mapTmp.put("userId",String.valueOf(userId));
             mapTmp.put("date", dates);
             mapTmp.put("Ip", Ip);
             mapTmp.put("types", types);
             mapTmp.put("remarks", remarks);
             mapTmp.put("userName", userName);
             list.add(mapTmp);
             if (++rowCnt > 90) {
               break;
            }
        }
        //int k = (list.size()>150? 150: list.size());
        for(int i=0; i< list.size(); i++){
           Map tmpMap = list.get(i);
           sb.append("{");
           sb.append("seqId:" + tmpMap.get("seqId"));
           sb.append(",userId:\"" + tmpMap.get("userId") + "\"");
           sb.append(",date:\"" + tmpMap.get("date") + "\"");
           sb.append(",Ip:\"" + tmpMap.get("Ip") + "\"");
           sb.append(",types:\"" + tmpMap.get("types") + "\"");
           sb.append(",remarks:\"" + YHUtility.encodeSpecial(String.valueOf(tmpMap.get("remarks"))) + "\"");
           sb.append(",userName:\"" + YHUtility.encodeSpecial(String.valueOf(tmpMap.get("userName"))) + "\"");
           
           sb.append("},");
        }
        if(list.size() > 0)
          sb.deleteCharAt(sb.length() - 1);
          sb.append("]");
          sb.append("}");
   }catch(Exception ex){
        throw ex;
   }finally {
        YHDBUtility.close(stmt, rs, null);
   }
     return sb.toString();
  }
  /**
   * 根据单选框  删除
   * @param request
   * @return
   */
  public String deletRadio(String type, String users, String statrtime, String endtime,String ip, String remark,String copytime,Connection conn, YHPerson user)
    throws Exception {
    StringBuffer sb = new StringBuffer();
    Statement stmt = null;
    ResultSet rs = null;
    Statement stmt1 = null;
    ResultSet rs1 = null;
    Statement stmt2 = null;
    ResultSet rs2 = null;
    String deleteCha = "";
    String startdate = "";
    String enddate = "";
    if(statrtime != null && !"".equals(statrtime)){
       startdate= YHDBUtility.getDateFilter( "TIME", statrtime, " >=");
    }
    if(endtime != null && !"".equals(endtime)){
       enddate = YHDBUtility.getDateFilter("TIME", endtime, "<=");
    } 
    try{
         stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
         ResultSet.CONCUR_READ_ONLY);
         stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
         ResultSet.CONCUR_READ_ONLY);
         stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
         ResultSet.CONCUR_READ_ONLY);
         String queryGllogSql = "";
         String querybeforCount = "";
         String queryafterCount = "";
         int beforCount =0;
         int afterCount = 0;
         int cha = 0;
         //删除之前的 总数
         if(copytime.equals("on")){
            querybeforCount = "select count(*) from oa_sys_log";
         }else{
            querybeforCount = "select count(*) from sys_log_"+copytime+"";
         }
          rs1 = stmt1.executeQuery(querybeforCount);
          if(rs1.next()){
          beforCount = Integer.parseInt(rs1.getString(1));
         }
         if(copytime.equals("on")){  //等于on 是单选按钮  选重了当前日志             if(YHUtility.isNullorEmpty(statrtime) && !YHUtility.isNullorEmpty(endtime)){
               queryGllogSql = "delete from oa_sys_log where " + enddate + " ";
             }
             else if(YHUtility.isNullorEmpty(endtime) && !YHUtility.isNullorEmpty(statrtime)){
               queryGllogSql = "delete from oa_sys_log where " + startdate + " ";
             }else{
               queryGllogSql = "delete from oa_sys_log where " + startdate + "" +
                  " and " + enddate + " ";
             }
         }else{
             if(YHUtility.isNullorEmpty(statrtime) && !YHUtility.isNullorEmpty(endtime)){
                 queryGllogSql = "delete from oa_sys_log_"+ copytime +" where "+ enddate +"";
             }
               else if(YHUtility.isNullorEmpty(endtime) && !YHUtility.isNullorEmpty(statrtime)){
                 queryGllogSql = "delete from oa_sys_log_"+ copytime +" where "+ startdate +"";
             }else{
                 queryGllogSql = "delete from oa_sys_log_" + copytime + " where " + startdate + "" +
                  " and "+enddate+"";
                 //queryGllogSql = "delete from sys_log_"+copytime+" where time >= to_date('" + statrtime + "','yyyy-mm-dd hh24:mi:ss')" +
                   // "and time <= to_date('" + endtime + "','yyyy-mm-dd hh24:mi:ss')";
             }
         }
        if (!"".equals(type) && type != null){
            queryGllogSql = queryGllogSql + " and type='"
            + type + "'";
        }
        if(!"".equals(ip) && ip != null){
            queryGllogSql = queryGllogSql + " and ip='"
            + ip + "'";
        }
        if(!"".equals(remark) && remark != null){
            queryGllogSql = queryGllogSql + " and remark like "
            + "'%" + YHDBUtility.escapeLike(remark) + "%'" + YHDBUtility.escapeLike();
        }
        if(!"".equals(users) && users != null){
        
            queryGllogSql = queryGllogSql + " and user_id in ("
             + users +")";
        }
        //System.out.println(queryGllogSql);
        stmt.executeUpdate(queryGllogSql);
        // 删除之后的总数
        if(copytime.equals("on")){
           queryafterCount = "select count(*) from oa_sys_log";
        }else{
           queryafterCount = "select count(*) from sys_log_" + copytime + "";
        }
        rs2 = stmt2.executeQuery(queryafterCount);
        if(rs2.next()){
           afterCount = Integer.parseInt(rs2.getString(1));
        } // 删除之前总数 - 删除之后的总数 = 删除多少条 
          cha = beforCount - afterCount;
          deleteCha =String.valueOf(cha);
    }catch(Exception ex){
       throw ex;
    }finally {
       YHDBUtility.close(stmt, rs, null);
    }
     return deleteCha;
   }
  
  public String getEndWorkList(int pageIndex, String flowIdStr, int showLen,
    String flowlist, String flow_status, String starttime, String endtime,
    String run_id, String run_name, Connection conn, YHPerson user)
    throws Exception {
    int pageCount = 0;// 页码数    int recordCount = 0;// 总记录数
    int pgStartRecord = 0;// 开始索引    int pgEndRecord = 0;// 结束索引
    int userId = user.getSeqId();// 用户ID
    String userName = user.getUserName();// 用户名称
    StringBuffer sb = new StringBuffer();
    Statement stmt = null;
    ResultSet rs = null;// 结果集    try {
        sb.append("{");
        sb.append("listData:[");
        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY);
        String startdate= YHDBUtility.getDateFilter("BEGIN_TIME", starttime, ">=");
        String enddate = YHDBUtility.getDateFilter("BEGIN_TIME", endtime, "<="); 
        String queryFlowRunPrcsSql = "select r.RUN_ID" + " ,RUN_NAME"
            + " ,BEGIN_TIME" + " ,END_TIME" + " ,f.SEQ_ID" + " ,ATTACHMENT_NAME"
            + " ,COMMENT_PRIV" + " ,QUERY_USER"
            + " from oa_fl_type f,oa_fl_run r where " + " f.SEQ_ID=r.FLOW_ID"
            + " and r.DEL_FLAG=0";
        /*
         * 选中的流程定义ID 如果选了一个流程定义ID，则SQL语句中加上这个字段的条件 flowlist相当于 选中的id
         */
        if (!"".equals(flowlist) && flowlist != null && !"0".equals(flowlist)) {
           queryFlowRunPrcsSql = queryFlowRunPrcsSql + " and f.SEQ_ID='"
              + Integer.parseInt(flowlist) + "'";
        }
        // --- “流水号”条件 ---
        if (!"".equals(run_id) && run_id != null && !"0".equals(run_id)) {
            queryFlowRunPrcsSql = queryFlowRunPrcsSql + " and r.RUN_ID='"
                + Integer.parseInt(run_id) + "'";
        }
        // --- “工作名称/文号”条件 ---sql=sql+" and PSN_NAME like '%" + psnName + "%'" + YHDBUtility.escapeLike();
        if (!"".equals(run_name) && run_name != null && !"0".equals(run_name)) {
            queryFlowRunPrcsSql = queryFlowRunPrcsSql + " and r.RUN_NAME like "
                + "'%" + YHDBUtility.escapeLike(run_name) + "%'" + YHDBUtility.escapeLike();
        }
        // --- “日期范围”条件，对应流程实例的创建时间BEGIN_TIME ---
        if (!"".equals(starttime) && starttime != null) {
            queryFlowRunPrcsSql = queryFlowRunPrcsSql
               // + " and BEGIN_TIME >=to_date('" + starttime + "','yyyy-MM-dd')";
                  + " and "+startdate+"";
        }
        if (!"".equals(starttime) && starttime != null) {
            queryFlowRunPrcsSql = queryFlowRunPrcsSql
                + " and "+enddate+"";
        }
        // --- “流程状态”条件 ---
        if (!flow_status.equals(("ALL"))) {
          if (flow_status.equals("0")) {
              queryFlowRunPrcsSql = queryFlowRunPrcsSql + " and r.END_TIME is null";
          } else {
              queryFlowRunPrcsSql = queryFlowRunPrcsSql
                + " and r.END_TIME is not null";
          }
        }
        queryFlowRunPrcsSql = queryFlowRunPrcsSql + " order by r.RUN_ID desc"; // 按照流水号（实例ID）倒排序        rs = stmt.executeQuery(queryFlowRunPrcsSql);
        List<Map> list = new ArrayList();
        String seqIdStr = "";
        String state = "";
        while (rs.next()) {
            int runId = rs.getInt("RUN_ID");
            String run_name1 = rs.getString("RUN_NAME");
            Date date = rs.getDate("BEGIN_TIME");
            Date date1 = rs.getDate("END_TIME");
            String attachmentName = rs.getString("ATTACHMENT_NAME");
            if (!YHWorkFlowUtility.findId(seqIdStr, String.valueOf(runId))) {
                seqIdStr += rs.getInt("RUN_ID") + ",";
                String comment_priv = rs.getString("COMMENT_PRIV");
                String query_user = rs.getString("QUERY_USER");
                if ("3".equals(comment_priv)
                    && YHWorkFlowUtility.findId(comment_priv, String.valueOf(userId))
                    || YHWorkFlowUtility.findId(query_user, String.valueOf(userId))
                    || "2".equals(comment_priv)
                    && YHWorkFlowUtility.findId(query_user, String.valueOf(userId))
                    || "1".equals(comment_priv)
                    && YHWorkFlowUtility.findId(comment_priv, String.valueOf(userId))) {
                  state = "点评";
              } else {
                 state = "办理";
              }
              Map mapTmp = new HashMap();
              mapTmp.put("RUN_ID", String.valueOf(runId));
              mapTmp.put("RUN_NAME", String.valueOf(run_name1));
              mapTmp.put("BEGIN_TIME", date);
              mapTmp.put("END_TIME", date1);
              mapTmp.put("ATTACHMENT_NAME", attachmentName);
              mapTmp.put("FLOW_STATUS", flow_status);
              mapTmp.put("STATE", state);
              list.add(mapTmp);
            }
        }
        recordCount = list.size(); // 一共有多少数据
        pageCount = recordCount / showLen; // 总的数据 / 每页显示的数据 = 总页数  
        if (recordCount % showLen != 0) { // 如果总记录数% 每页的数据 ！=0 总页数+1
          pageCount++;
        }
        if (pageIndex < 1) { // 当前索引页小于 1 默认显示第一页          pageIndex = 1;
        }
        if (pageIndex > pageCount) { // 索引页 大于总页数 显示总页数          pageIndex = pageCount;
        }
        // 开始 检索 是从第几页 多少数据开始的
        pgStartRecord = (pageIndex - 1) * showLen + 1;
        int count = 0;
        int i = pgStartRecord;
        if (i <= 0) { // 如果检索的数据 <=0 默认显示第一页的数据
          i = 1;
        }
        for (; i < pgStartRecord + showLen && i <= list.size(); i++) {
            Map tmpMap = list.get(i - 1); // 数据库是从第0行开始读取的
            count++;
            sb.append("{");
            sb.append("run_id:" + tmpMap.get("RUN_ID"));
            sb.append(",runName:\"" + tmpMap.get("RUN_NAME") + "\"");
            sb.append(",begin_time:\"" + tmpMap.get("BEGIN_TIME") + "\"");
            sb.append(",end_time:\"" + tmpMap.get("END_TIME") + "\"");
            sb.append(",attachmentName:\"" + tmpMap.get("ATTACHMENT_NAME") + "\"");
            sb.append(",flow_status:\"" + tmpMap.get("FLOW_STATUS") + "\"");
            sb.append(",state:\"" + tmpMap.get("STATE") + "\"");
            sb.append("},");
        }
        // 最后 检索了 多少数据
        pgEndRecord = (pageIndex - 1) * showLen + count;
        if (count > 0) {
           sb.deleteCharAt(sb.length() - 1); // 剔除更新的意思        }
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
  /**
   * 删除 所选 文件
   * @param request
   * @return
   */
  public boolean deleteChecknews(Connection dbConn,YHPerson loginperson,String deleteStr) throws IOException, Exception {
    String deletenewsCommentSql = "";
    String deletenewsSql = "";
   
    Statement stmt = null;
    ResultSet rs = null;
      
    if(deleteStr != null && !"".equals(deleteStr)){
        String[] deleteStrs = deleteStr.split(",");
        deleteStr = "";
        for(int i = 0 ;i < deleteStrs.length ; i++){
           deleteStr +=  "'" + deleteStrs[i] + "'" + ",";
        }
           deleteStr = deleteStr.substring(0, deleteStr.length() - 1);
    }
    boolean success = false;
    try {
        stmt = dbConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
        deletenewsCommentSql = "delete  from oa_sys_log  where seq_id in  ("+ deleteStr + ")";
        success = stmt.execute(deletenewsCommentSql);
    } catch (Exception e) {
        e.printStackTrace();
    }finally {
         YHDBUtility.close(stmt, rs, null);
    }
     return success;
  }
  /**
   * 删除 sys_log 表中的所有数据
   * @param request
   * @return
   */
  public boolean deleteAll(String copytime, Connection dbConn,YHPerson loginperson) throws IOException, Exception {
    String deletenewsCommentSql = "";
    String deletenewsSql = ""; 
    Statement stmt = null;
    ResultSet rs = null;
    boolean success = false;
    try {
        stmt = dbConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
       //qingkong xitong rizhi xingongneng    if(copytime.equals("on")){
        deletenewsCommentSql = "delete from oa_sys_log ";
      //}else{
      // eletenewsCommentSql = "drop table sys_log_"+copytime+"";
      //}
      //stmt = dbConn.createStatement();
        stmt.executeUpdate(deletenewsCommentSql);
        success=true;
      return  success;
    } catch (Exception e) {
        e.printStackTrace();
    }finally {
        YHDBUtility.close(stmt, rs, null);
    }
    return success;
  }

}
