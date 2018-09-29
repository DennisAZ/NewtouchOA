package yh.core.funcs.system.resManage.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHEmailResManageLogic {
  public static String filePath = YHSysProps.getAttachPath() +File.separator+"email";
  public String searchEmail(Connection conn, String toId, String copyToId, String subject, String attachmentName, String content, String beginDate, String endDate) throws Exception {
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    String query = "select " +
    		" oa_email.SEQ_ID AS SEQ_ID " +
    		", FROM_ID" +
    		", oa_email.TO_ID AS TO_ID" +
    		", READ_FLAG" +
    		",  DELETE_FLAG" +
    		", SUBJECT " +
    		" ,ATTACHMENT_ID " +
    		" ,ATTACHMENT_NAME " +
    		" , SEND_TIME  from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID  and oa_email.TO_ID is not null  and FROM_ID is not null ";
    if(!YHUtility.isNullorEmpty(toId)) {
      query += " and (" + YHWorkFlowUtility.createFindSql("FROM_ID", toId) + ") ";
    }
    if(!YHUtility.isNullorEmpty(copyToId)) {
      query += " and (" + YHWorkFlowUtility.createFindSql("oa_email.TO_ID", copyToId) + ") ";
    }
    if(!YHUtility.isNullorEmpty(beginDate)) {
      query +=" and "  + YHDBUtility.getDateFilter("SEND_TIME", beginDate, " >= ");
    }
    if(!YHUtility.isNullorEmpty(endDate)) {
      query +=" and "  + YHDBUtility.getDateFilter("SEND_TIME", endDate, " <= ");
    } if(!YHUtility.isNullorEmpty(subject))
       query+=" and SUBJECT like '%"+subject+"%'";
    if(!YHUtility.isNullorEmpty(content)) {
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("mysql")) {
        query+=" and CONTENT like '%"+content+"%'";
      }
    }
    if(!YHUtility.isNullorEmpty(attachmentName)) {
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("mysql")) {
        query+=" and ATTACHMENT_NAME like '%"+attachmentName+"%'";
      }
    }
    query +=" order by SEND_TIME desc ";
    int count = 0 ;
   
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query);
      YHPersonLogic logic = new YHPersonLogic();
      
      while (rs2.next() && count < 30){
        int emailId = rs2.getInt("SEQ_ID");
        int fromId = rs2.getInt("FROM_ID");
        String toIdStr = rs2.getString("TO_ID");
        String readFlag = rs2.getString("READ_FLAG");
        String deleteFlag = rs2.getString("DELETE_FLAG");
        String subjectStr = rs2.getString("SUBJECT");
        subjectStr = subjectStr.replaceAll("\"", "“");
        subjectStr = subjectStr.replaceAll("'", "’");
        Timestamp sendTime = rs2.getTimestamp("SEND_TIME");
        String dateStr = YHUtility.getDateTimeStr(sendTime);
        String attId = rs2.getString("ATTACHMENT_ID");
        String attName = rs2.getString("ATTACHMENT_NAME");
        if (attId == null) {
          attId = "";
        }
        if (attName == null) {
          attName = "";
        }
        String statusDesc = readFlag;
        
        String fromName = "";
        String query2  = "SELECT USER_NAME from PERSON where SEQ_ID='"+ fromId +"'";
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query2);
          if (rs.next()){
            fromName = rs.getString("USER_NAME");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
        String toName = logic.getNameBySeqIdStr(toIdStr, conn);
        count ++;
        sb.append("{");
        sb.append("emailId:'" + emailId  +"'");
        sb.append(",statusDesc:'" + statusDesc  +"'");
        sb.append(",deleteFlag:'" + deleteFlag  +"'");
        sb.append(",subject:'" + subjectStr  +"'");
        sb.append(",attId:'" + attId  +"'");
        sb.append(",attName:'" + attName  +"'");
        sb.append(",sendTime:'" + dateStr  +"'");
        sb.append(",fromName:'" + fromName  +"'");
        sb.append(",toName:'" + toName  +"'");
        sb.append("},");
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
    return sb.toString();
  }

  public int deleteEmail(Connection conn ,String idStr) throws Exception {
    // TODO Auto-generated method stub
    int count = 0 ;
    if (YHUtility.isNullorEmpty(idStr)) {
      return count;
    }
    idStr = idStr.trim();
    if (idStr.endsWith(",")) {
      idStr = idStr.substring(0, idStr.length() - 1);
    }
     String query  = "select * from oa_email where SEQ_ID in ("+idStr+")";
     Statement stm2 = null;
     ResultSet rs2 = null;
     try {
       stm2 = conn.createStatement();
       rs2 = stm2.executeQuery(query);
       while(rs2.next()) {
         int emailId = rs2.getInt("SEQ_ID");
         int bodyId = rs2.getInt("BODY_ID");
         String  query2 ="select count(*) as c from oa_email where BODY_ID='"+bodyId+"'";
         Statement stm = null;
         ResultSet rs = null;
         int groupCount = 0;
         try {
           stm = conn.createStatement();
           rs = stm.executeQuery(query2);
           if (rs.next()){
             groupCount = rs.getInt("C");
           }
         } catch(Exception ex) {
           throw ex;
         } finally {
           YHDBUtility.close(stm, rs, null); 
         }
         if (groupCount <= 1) {
           String query3 = "select * from oa_email_body where SEQ_ID='"+bodyId+"'";
           Statement stm3 = null;
           ResultSet rs3 = null;
           try {
             stm3 = conn.createStatement();
             rs3 = stm3.executeQuery(query3);
             if (rs3.next()){
               String attachmentId = rs3.getString("ATTACHMENT_ID");
               String attachmentName = rs3.getString("ATTACHMENT_NAME");
               if (!YHUtility.isNullorEmpty(attachmentName)) {
                 //删除附件
                 deleteAttachments(attachmentId , attachmentName);
               }
             }
           } catch(Exception ex) {
             throw ex;
           } finally {
             YHDBUtility.close(stm3, rs3, null); 
           }
           String query4 = "delete from oa_email_body where SEQ_ID='"+ bodyId +"'";
           Statement stm4 = null;
           try {
             stm4 = conn.createStatement();
             stm4.executeUpdate(query4);
           } catch(Exception ex) {
             throw ex;
           } finally {
             YHDBUtility.close(stm4,null, null); 
           }
           count++;
         }
         String query5 ="delete from oa_email where SEQ_ID='"+ emailId +"'";
         Statement stm5 = null;
         try {
           stm5 = conn.createStatement();
           stm5.executeUpdate(query5);
         } catch(Exception ex) {
           throw ex;
         } finally {
           YHDBUtility.close(stm5,null, null); 
         }
         count++;
       }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
    return count;
  }
/**
 * 删除指定的公共附件

 * @param attachmentId
 * @param attachmentName
 */
public void deleteAttachments (String attachmentId , String attachmentName) {
  if(attachmentId == null 
      || "".equals(attachmentId)
      || attachmentName == null 
      || "".equals(attachmentName)){
    return ;
  }
  String[] attachmentIdArray = attachmentId.split(",");
  String[] attachmentNameArray = attachmentName.split("\\*");
  for (int i = 0 ; i< attachmentIdArray.length ; i++) {
    //处理文件
    String tmpId = attachmentIdArray[i];
    String tmpName = attachmentNameArray[i];
    
    this.deleteAttachement(tmpId, tmpName);
  }
}
public static void deleteAttachement(String aId , String aName) {
  //处理文件
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
    String path = filePath + File.separator  + hard  + File.separator  +  str + "_" + aName;
    File file = new File(path);
    if(file.exists()){
      file.delete();
    } else {
      //兼容老的数据
      String path2 = filePath + File.separator  +  hard + File.separator  +  str + "." + aName;
      File file2 = new File(path2);
      if(file2.exists()){
        file2.delete();
      }
    }
  }

public String readEmail(Connection conn, String emailId) throws Exception {
  // TODO Auto-generated method stub
  StringBuffer sb = new StringBuffer();
  String query  = "SELECT * from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and oa_email.SEQ_ID='"+emailId+"'";
  Statement stm2 = null;
  ResultSet rs2 = null;
  try {
    stm2 = conn.createStatement();
    rs2 = stm2.executeQuery(query);
    YHPersonLogic logic = new YHPersonLogic();
    if (rs2.next()){
      int fromId = rs2.getInt("FROM_ID");
      String toIdStr = rs2.getString("TO_ID");
      String copyToId = rs2.getString("COPY_TO_ID");
      String subjectStr = rs2.getString("SUBJECT");
      subjectStr = subjectStr.replaceAll("\"", "“");
      subjectStr = subjectStr.replaceAll("'", "’");
      Timestamp sendTime = rs2.getTimestamp("SEND_TIME");
      String dateStr = YHUtility.getDateTimeStr(sendTime);
      String attId = rs2.getString("ATTACHMENT_ID");
      attId = (attId == null ? "":attId); 
      String attName = rs2.getString("ATTACHMENT_NAME");
      attName = (attName == null ? "":attName);
      String important = rs2.getString("important");
      String content = rs2.getString("COMPRESS_CONTENT");
      if (YHUtility.isNullorEmpty(content)) {
        content = rs2.getString("CONTENT");
      }
      content = content.replaceAll("\"", "“");
      content = content.replaceAll("'", "’");
      content = content.replaceAll("\\s", "");
      String fromName = this.getUserName(conn, fromId);
      String toName = logic.getNameBySeqIdStr(toIdStr, conn);
      String copyIdName = logic.getNameBySeqIdStr(copyToId, conn);
      sb.append("{");
      sb.append("emailId:'" + emailId  +"'");
      sb.append(",important:'" + important  +"'");
      sb.append(",subject:'" + subjectStr  +"'");
      sb.append(",attId:'" + attId  +"'");
      sb.append(",attName:'" + attName  +"'");
      sb.append(",content:'" + content  +"'");
      sb.append(",sendTime:'" + dateStr  +"'");
      sb.append(",fromName:'" + fromName  +"'");
      sb.append(",toName:'" + toName  +"'");
      sb.append(",copyIdName:'" + copyIdName  +"'");
      sb.append("}");
    }
  } catch(Exception ex) {
    throw ex;
  } finally {
    YHDBUtility.close(stm2, rs2, null); 
  }
  return sb.toString();
}
public String getUserName(Connection conn , int id) throws Exception {
  String userName = "";
  String query2  = "SELECT USER_NAME from PERSON where SEQ_ID='"+ id +"'";
  Statement stm = null;
  ResultSet rs = null;
  try {
    stm = conn.createStatement();
    rs = stm.executeQuery(query2);
    if (rs.next()){
      userName = rs.getString("USER_NAME");
    }
  } catch(Exception ex) {
    throw ex;
  } finally {
    YHDBUtility.close(stm, rs, null); 
  }
  return userName;
}
}
