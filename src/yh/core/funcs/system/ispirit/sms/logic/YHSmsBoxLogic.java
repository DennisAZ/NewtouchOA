package yh.core.funcs.system.ispirit.sms.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import yh.core.funcs.message.logic.YHMessageLogic;
import yh.core.funcs.sms.logic.YHSmsLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHSmsBoxLogic {
  private YHSmsLogic sl = new YHSmsLogic();
  private YHMessageLogic ml = new YHMessageLogic();
  public String getRemindInBox(Connection conn,int userId) throws Exception{
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String dbDateFremind = YHDBUtility.getDateFilter("SMS.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String sql =  "select " +
        "SMS_BODY.SEQ_ID," +
        "SMS_BODY.FROM_ID," +
        "SMS_BODY.SMS_TYPE," +
        "SMS_BODY.CONTENT," +
        "SMS_BODY.SEND_TIME," +
        "SMS_BODY.REMIND_URL," +
        "SMS.REMIND_TIME," +
        "SMS.SEQ_ID," +
        "(select USER_NAME from PERSON where PERSON.SEQ_ID = SMS_BODY.FROM_ID) as USER_NAME," +
        "SMS.TO_ID" +
        " FROM " +
        "SMS," +
        " oa_msg_body  as SMS_BODY" +
        " WHERE " +
        " SMS.BODY_SEQ_ID = SMS_BODY.SEQ_ID" +
        " AND SMS.TO_ID=" + userId +
        " AND DELETE_FLAG IN(0,2) " +
        " AND " + dbDateF  +
        " AND (SMS.REMIND_TIME IS NULL OR " + dbDateFremind + ")" +
        " AND REMIND_FLAG IN(1,2)";
    sql += " ORDER BY SMS.REMIND_TIME DESC,SEND_TIME DESC";
    StringBuffer sb = new StringBuffer();
    String smsIds = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        int seqId = rs.getInt(1);
        int smsId = rs.getInt(8);
        String fromId = rs.getString(2);
        String smsType = rs.getString(3);
        if(YHUtility.isNullorEmpty(smsType)){
          smsType="0";
        }
        String content = rs.getString(4);
        Date sendTime = rs.getTimestamp(5);
        String remindUrl = rs.getString(6);
        Date remindTime = rs.getTimestamp(7);
        String userName = rs.getString(9);
        if (YHUtility.isNullorEmpty(userName)) {
          userName = "已删除用户";
        }
        String toId = rs.getString(10);
        if(remindTime != null){
          sendTime = remindTime;
        }
        if(remindUrl == null){
          remindUrl = "/yh/core/funcs/sms/act/YHSmsAct/acceptedSms.act?pageNo=0&pageSize=20";
        }
        if(!"".equals(sb.toString())){
          sb.append(",");
        }      
        //String remindTimeStr = YHUtility.g 
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String remindTimeStr  = sdf.format(sendTime);
        String smsTypeName = ml.getMessageTypeDesc(conn, Integer.parseInt(smsType.trim()), "SMS_REMIND");
        sb.append("{");
        sb.append("seqId:\"" + seqId + "\"");
        sb.append(",sms_id:" + seqId );
        sb.append(",from_id:\"" + fromId + "\"");
        sb.append(",to_id:"+ toId );
        sb.append(",unread:1");
        sb.append(",receive:1");
        sb.append(",type_id:\"" + smsType + "\"");
        sb.append(",type_name:\"" + smsTypeName + "\"");
        sb.append(",content:\"" + YHUtility.encodeSpecial(content) + "\"");
        sb.append(",send_time:\"" + remindTimeStr + "\"");
        sb.append(",url:\"" + YHUtility.encodeSpecial(remindUrl) + "\"");
        sb.append(",from_name:\"" + YHUtility.encodeSpecial(userName) + "\"");
        sb.append("}");
        if(!"".equals(smsIds)){
          smsIds += ",";
        }
        smsIds += smsId;
      }
      if(!"".equals(smsIds)){
        sl.updateFlag(conn, "REMIND_FLAG", "2", smsIds, "SMS");
      }
     
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return "[" + sb.toString() + "]";
  }
  
  
  

  public String getRemindInBox1(Connection conn,int userId) throws Exception{
    String dbDateF = YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String dbDateFremind = YHDBUtility.getDateFilter("MESSAGE.REMIND_TIME", YHUtility.getCurDateTimeStr(), " <= ");
    String sql =  "select " +
        "MESSAGE_BODY.SEQ_ID," +
        "MESSAGE_BODY.FROM_ID," +
        "MESSAGE_BODY.MESSAGE_TYPE," +
        "MESSAGE_BODY.CONTENT," +
        "MESSAGE_BODY.SEND_TIME," +
        "MESSAGE_BODY.REMIND_URL," +
        "MESSAGE.REMIND_TIME," +
        "MESSAGE.SEQ_ID," +
        "(select USER_NAME from PERSON where PERSON.SEQ_ID = MESSAGE_BODY.FROM_ID) as USER_NAME," +
        "MESSAGE.TO_ID" +
        " FROM " +
        "oa_message as MESSAGE," +
        "oa_message_body as MESSAGE_BODY" +
        " WHERE " +
        " MESSAGE.BODY_SEQ_ID = MESSAGE_BODY.SEQ_ID" +
        " AND MESSAGE.TO_ID=" + userId +
        " AND DELETE_FLAG IN(0,2) " +
        " AND " + dbDateF  +
        " AND (MESSAGE.REMIND_TIME IS NULL OR " + dbDateFremind + ")" +
        " AND REMIND_FLAG IN(1,2)";
    sql += " ORDER BY MESSAGE.REMIND_TIME DESC,SEND_TIME DESC";
    StringBuffer sb = new StringBuffer();
    String smsIds = "";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        int seqId = rs.getInt(1);
        int smsId = rs.getInt(8);
        String fromId = rs.getString(2);
        String smsType = rs.getString(3);
        if(YHUtility.isNullorEmpty(smsType)){
          smsType="0";
        }
        String content = rs.getString(4);
        Date sendTime = rs.getTimestamp(5);
        String remindUrl = rs.getString(6);
        Date remindTime = rs.getTimestamp(7);
        String userName = rs.getString(9);
        if (YHUtility.isNullorEmpty(userName)) {
          userName = "已删除用户";
        }
        String toId = rs.getString(10);
        if(remindTime != null){
          sendTime = remindTime;
        }
        if(remindUrl == null){
          remindUrl = "/yh/core/funcs/message/act/YHMessageAct/acceptedMessage.act?pageNo=0&pageSize=20";
        }
        if(!"".equals(sb.toString())){
          sb.append(",");
        }      
        //String remindTimeStr = YHUtility.g 
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String remindTimeStr  = sdf.format(sendTime);
        String smsTypeName = "微讯";
        sb.append("{");
        sb.append("seqId:\"" + seqId + "\"");
        sb.append(",sms_id:\"" + seqId+"r\"" );
        sb.append(",from_id:\"" + fromId + "\"");
        sb.append(",to_id:"+ toId );
        sb.append(",unread:1");
        sb.append(",receive:1");
        sb.append(",type_id:\"" + smsType + "\"");
        sb.append(",type_name:\"" + smsTypeName + "\"");
        sb.append(",content:\"" + YHUtility.encodeSpecial(content) + "\"");
        sb.append(",send_time:\"" + remindTimeStr + "\"");
        sb.append(",url:\"" + YHUtility.encodeSpecial(remindUrl) + "\"");
        sb.append(",from_name:\"" + YHUtility.encodeSpecial(userName) + "\"");
        sb.append("}");
        if(!"".equals(smsIds)){
          smsIds += ",";
        }
        smsIds += smsId;
      }
      if(!"".equals(smsIds)){
      //  sl.updateFlag(conn, "REMIND_FLAG", "2", smsIds, "MESSAGE");
        sl.updateFlag(conn, "REMIND_FLAG", "0", smsIds, "OA_MESSAGE");
      }
     
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    
   String data=sb.toString();
   if(data.endsWith(",")){
     data=data.substring(0, data.length()-1);
   }
    
    return data ;
  }
  
  
  public String getRemindIn(Connection conn,int userId) throws Exception{
     String dataOrg="";
     String data1=this.getRemindInBox1(conn, userId);//微讯
     String data2=this.getRemindInBox(conn, userId);//提醒
     data2=data2.replace("[", "").replace("]", "");
     if(!YHUtility.isNullorEmpty(data2)){
       
       dataOrg=data2+","+data1;
       
     }else{
       dataOrg=data1;
     }
     

     if(dataOrg.endsWith(",")){
       dataOrg=dataOrg.substring(0, dataOrg.length()-1);
     }
    
     return "["+dataOrg+"]";
  }
  
  
  
  
  
  
  
  
 /**
  * 
  * @param conn
  * @param smsIds
  * @return
  * @throws Exception
  */
  public List<Map<String, String>> viewDetailsLogic(Connection conn,String smsIds,int userId) throws Exception{
    List<Map<String, String>> l = new ArrayList<Map<String,String>>();
    if(smsIds == null || "".equals(smsIds.trim()) || ",".equals(smsIds.trim())){
      return l;
    }
    smsIds = smsIds.trim();
    if(smsIds.endsWith(",")){
      smsIds = smsIds.substring(0, smsIds.length() -1);
    }
    String sql = "select sms.seq_id,sms_body.sms_TYPE ,sms_body.content,sms_body.REMIND_URL from sms, oa_msg_body as sms_body where sms_body.seq_id = sms.body_seq_id and sms_body.seq_id in(" + smsIds + ") and sms.to_id = " + userId;
    PreparedStatement ps =null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      int count = 1;
      int personCount = 0;
      while (rs.next()) {
        int smsId = rs.getInt(1);
        String smsType = rs.getString(2);
        String content = rs.getString(3);
        String remindUrl = rs.getString(4);
        
        if("0".equals(smsType)){
          if(personCount != 0){
            continue;
          }
          remindUrl = "/yh/core/funcs/sms/act/YHSmsAct/acceptedSms.act?pageNo=0&pageSize=20";
          personCount ++;
        }
        if(content.length() > 30){
          content = content.substring(0, 30);
        }
        String img = "sms_type" + smsType + ".gif";
        Map<String, String> m = new HashMap<String, String>();
        m.put("text", "工作" + count);
        m.put("url", remindUrl);
        m.put("title", content);
        m.put("img", img);
        l.add(m);
        count ++;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return l;
  }
}
