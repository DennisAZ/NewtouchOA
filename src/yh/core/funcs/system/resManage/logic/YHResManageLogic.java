package yh.core.funcs.system.resManage.logic;

import java.io.File;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import yh.core.data.YHDBMapUtil;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHResManageLogic {
  public static String filePath = YHSysProps.getAttachPath() + File.separator  + "email";
  private int fileCount = 0 ;
  public String getModuleData(Connection conn) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer("[");
    String[] str = {"EMAIL","oa_email_body","SMS","oa_msg_body","FLOW_RUN","FLOW_RUN_DATA","file_sort","oa_file_content","NOTIFY","DIARY"};
    int count = 0 ;
    for (String tmp : str) {
      Map map = this.getTableInfoByName(conn, tmp);
      String tableDesc = (String)map.get("table_desc");
      String recordRows = (String)map.get("record_rows");
      String dataSize = (String)map.get("data_size");
      
      sb.append("{");
      sb.append("tableDesc:'" + tableDesc + "'");
      sb.append(",recordRows:'" + recordRows + "'");
      sb.append(",dataSize:'" + dataSize + "'");
      sb.append("},");
      count++;
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    
    return sb.toString();
  }
  public Map getTableInfoByName(Connection conn ,String tableName) throws Exception {
	 tableName=YHDBMapUtil.getMapDBName(tableName);
    Map map = new HashMap();
    String query = "show table status like '"+tableName+"'";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if (rs.next()){
        int recordRows = rs.getInt("Rows");
        int dataLength = rs.getInt("Data_length");
        int indexLength = rs.getInt("Index_length");
        String dataSize =  Math.round((dataLength + indexLength) / 1024) + "KB";
        
        String tableDesc = "";
        if ("oa_email".equalsIgnoreCase(tableName)) {
          tableDesc = "邮件";
       } else if ("oa_email_body".equalsIgnoreCase(tableName)) {
         tableDesc = "邮件内容";
       } else if ("oa_msg".equalsIgnoreCase(tableName)) {
         tableDesc = "短信";
       } else if ("oa_msg_body".equalsIgnoreCase(tableName)) {
         tableDesc = "短信内容";
       } else if ("oa_fl_run".equalsIgnoreCase(tableName)) {
         tableDesc = "工作流";
       } else if ("oa_fl_run_data".equalsIgnoreCase(tableName)) {
         tableDesc = "工作流数据";
       } else if ("oa_file_sort".equalsIgnoreCase(tableName)) {
         tableDesc = "文件柜文件夹";
       } else if ("oa_file_content".equalsIgnoreCase(tableName)) {
         tableDesc = "文件柜文件";
       } else if ("oa_notify".equalsIgnoreCase(tableName)) {
         tableDesc = "公告通知";
       } else if ("oa_journal".equalsIgnoreCase(tableName)) {
         tableDesc = "工作日志";
       } 
       map.put("table_name", tableName);
       map.put("table_desc", tableDesc);
       map.put("record_rows", recordRows + " 条");
       map.put("data_size", dataSize);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    return map;
  }
  public void deleteDelBox(Connection conn) throws Exception {
    // TODO Auto-generated method stub
    String query  = "select * from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and BOX_ID='0' and SEND_FLAG='1' and DELETE_FLAG='4'";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      String bodyStr = "";
      while (rs.next()){
        String attId = rs.getString("ATTACHMENT_ID");
        String attName = rs.getString("ATTACHMENT_NAME");
        String deleteFlag = rs.getString("DELETE_FLAG");
        int bodyId = rs.getInt("BODY_ID");
        String query2 = "select count(*) as c from oa_email where BODY_ID='"+bodyId+"'";
        Statement stm2 = null;
        ResultSet rs2 = null;
        int groupCount = 0;
        try {
          stm2 = conn.createStatement();
          rs2 = stm2.executeQuery(query2);
          if (rs2.next()){
            groupCount = rs2.getInt("C");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm2, rs2, null); 
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
          bodyStr += bodyId + ","; 
        }
      }
      if (!"".equals(bodyStr)) {
        bodyStr = bodyStr.substring(0, bodyStr.length() - 1);
        String query5 ="delete from oa_email_body where SEQ_ID in ("+bodyStr+")";
        this.executeSql(query5, conn);
      }
      String query6 = "delete from oa_email where DELETE_FLAG='4' and BOX_ID='0'";
      this.executeSql(query6, conn);
      String query7 = "update oa_email set DELETE_FLAG='1' where DELETE_FLAG='3' and BOX_ID='0'";
      this.executeSql(query7, conn);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
  }
  public int executeSql(String sql , Connection conn) throws Exception{
    Statement stm5 = null;
    int result = 0;
    try {
      stm5 = conn.createStatement();
      result = stm5.executeUpdate(sql);
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm5,null, null); 
    }
    return result;
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
      
      this.deleteAttachement(tmpId, tmpName , "email");
    }
  }
  public static void deleteAttachement(String aId , String aName ,String  module) {
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
      String path = YHSysProps.getAttachPath() + File.separator  +  module + File.separator  +  hard  + File.separator  + str + "_" + aName;
      File file = new File(path);
      if(file.exists()){
        file.delete();
      } else {
        //兼容老的数据
        String path2 = YHSysProps.getAttachPath() + File.separator  + module  + File.separator  +  hard  + File.separator  + str + "." + aName;
        File file2 = new File(path2);
        if(file2.exists()){
          file2.delete();
        }
      }
    }
  public void deleteDeletedDelbox(Connection conn) throws Exception {
    // TODO Auto-generated method stub
    String query  = "select  BODY_ID from oa_email where DELETE_FLAG='1'";
    Statement stm3 = null;
    ResultSet rs3 = null;
    String sqlStr = "";
    try {
      stm3 = conn.createStatement();
      rs3 = stm3.executeQuery(query);
      while (rs3.next()) {
        int bodyId = rs3.getInt("BODY_ID");
        if (this.findId(sqlStr, bodyId + "")) {
          continue;
        }
        sqlStr += bodyId + ",";
        String query2 = "select BODY_ID from oa_email where BODY_ID='"+bodyId+"' and DELETE_FLAG<>'1'";
        Statement stm2 = null;
        ResultSet rs2 = null;
        try {
          stm2 = conn.createStatement();
          rs2 = stm2.executeQuery(query2);
          if (!rs2.next()){
            String query3 = "delete from oa_email where BODY_ID = '"+bodyId+"'";
            this.executeSql(query3, conn);
            String query4  = "select ATTACHMENT_ID, ATTACHMENT_NAME from oa_email_body where SEQ_ID = '"+bodyId+"'";
            Statement stm5 = null;
            ResultSet rs5 = null;
            try {
              stm5 = conn.createStatement();
              rs5 = stm5.executeQuery(query4);
              if (rs5.next()) {
                String attId = rs5.getString("ATTACHMENT_ID");
                String attName = rs5.getString("ATTACHMENT_NAME");
                if (!YHUtility.isNullorEmpty(attName))  {
                  this.deleteAttachments(attId, attName);
                }
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm5,rs5, null); 
            }
            String query10 = "delete from oa_email_body where SEQ_ID = '"+bodyId+"'";
            this.executeSql(query10, conn);
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm2, rs2, null); 
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, rs3, null); 
    }
  }
  public void woriteLog(Connection conn , String remark , int userId , String ip) throws Exception {
    YHSysLogLogic logic = new YHSysLogLogic();
    logic.addSysLog(conn, "12", remark, userId, ip);
  }
  public void deleteSms(Connection conn) throws Exception {
    // TODO Auto-generated method stub
    String curDate = YHUtility.getCurDateTimeStr();
    String query = "select SMS.SEQ_ID,SMS.BODY_SEQ_ID from SMS, oa_msg_body as SMS_BODY where SMS.BODY_SEQ_ID=SMS_BODY.SEQ_ID and  REMIND_FLAG='0' and DELETE_FLAG='2' and " + YHDBUtility.getDateFilter("SEND_TIME", curDate, " <= ");
    Statement stm5 = null;
    ResultSet rs5 = null;
    String bodyIdDel = "";
    String smsIdDel = "";
    try {
      stm5 = conn.createStatement();
      rs5 = stm5.executeQuery(query);
      while (rs5.next()) {
        int smsId = rs5.getInt("SEQ_ID");
        int bodyId = rs5.getInt("BODY_SEQ_ID");
        String query1 ="select count(*) as c from SMS where BODY_SEQ_ID='"+bodyId+"'";
        int count = 0 ;
        Statement stm3 = null;
        ResultSet rs3 = null;
        try {
          stm3 = conn.createStatement();
          rs3 = stm3.executeQuery(query1);
          if (rs3.next()){
            count = rs3.getInt("c");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm3, rs3, null); 
        }
        if (count <= 1) {
          bodyIdDel += bodyId + ",";
        }
        smsIdDel += smsId + ",";
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm5,rs5, null); 
    }
    if (!"".equals(smsIdDel)) {
      smsIdDel = smsIdDel.substring(0, smsIdDel.length() - 1);
      String query1 = "delete from SMS where SEQ_ID in ("+smsIdDel+")";
      this.executeSql(query1, conn);
    }
    if (!"".equals(bodyIdDel)) {
      bodyIdDel = bodyIdDel.substring(0, bodyIdDel.length() - 1);
      String query1 = "delete from oa_msg_body where SEQ_ID in ("+bodyIdDel+")";
      this.executeSql(query1, conn);
    }
    String sql2 = "update SMS a set DELETE_FLAG = '1' where  exists (select 1 from oa_msg_body b where b.SEQ_ID = a.BODY_SEQ_ID and "  + YHDBUtility.getDateFilter("b.SEND_TIME", curDate, " <= ") + " ) and REMIND_FLAG='0' and DELETE_FLAG='0'";
   // String sql ="update SMS a,SMS_BODY set DELETE_FLAG='1' where SMS.BODY_SEQ_ID=SMS_BODY.SEQ_ID  and REMIND_FLAG='0' and DELETE_FLAG='0' and " + YHDBUtility.getDateFilter("SEND_TIME", curDate, " <= ");
    this.executeSql(sql2, conn);
  }
  public void deleteDeletedSms(Connection conn) throws Exception {
    // TODO Auto-generated method stub
    String query ="select  BODY_SEQ_ID from SMS where DELETE_FLAG='1'";
    Statement stm5 = null;
    ResultSet rs5 = null;
    String sqlStr = "";
    try {
      stm5 = conn.createStatement();
      rs5 = stm5.executeQuery(query);
      while (rs5.next()) {
        int bodyId = rs5.getInt("BODY_SEQ_ID");
        if (this.findId(sqlStr, bodyId + "")) {
          continue;
        }
        sqlStr += bodyId + ",";
        String query1 = "select BODY_SEQ_ID from SMS where BODY_SEQ_ID='"+bodyId+"' and DELETE_FLAG<>'1'";
        Statement stm3 = null;
        ResultSet rs3 = null;
        try {
          stm3 = conn.createStatement();
          rs3 = stm3.executeQuery(query1);
          if (!rs3.next()){
            String sql = "delete from SMS where BODY_SEQ_ID = '"+bodyId+"'";
            this.executeSql(sql, conn);
            sql = "delete from oa_msg_body where SEQ_ID = '"+bodyId+"'";
            this.executeSql(sql, conn);
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm3, rs3, null); 
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm5,rs5, null); 
    }
  }
  public String getDeptRes(Connection conn, int deptId) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    this.getDeptRes(conn , sb, deptId , 0);
    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    return "[" + sb.toString() + "]";
  }
  public void getDeptRes(Connection conn , StringBuffer sb  ,int deptId , int level) throws Exception{
    String deptName = this.getDeptName(deptId, conn);
    String tmp = "";
    for(int j = 0 ;j < level ; j++){
      tmp += "&nbsp;&nbsp;";
    }
    deptName = tmp + deptName;
    this.getUserResByDeptId(conn, sb, deptId, deptName);
    String query = "select SEQ_ID from oa_department where DEPT_PARENT=" + deptId;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        int seqId = rs.getInt("SEQ_ID");
        this.getDeptRes(conn, sb, seqId, level + 1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
  public String getDeptName(int deptId , Connection conn) throws Exception {
    String query1 = "select dept_name from oa_department where seq_id=" + deptId;
    Statement stm3 = null;
    ResultSet rs3 = null;
    String deptName = "";
    try {
      stm3 = conn.createStatement();
      rs3 = stm3.executeQuery(query1);
      if (rs3.next()){
        deptName = rs3.getString("dept_Name");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, rs3, null); 
    }
    return deptName;
  }
  public int getEmailSizeBySql(String sql , Connection conn) throws Exception {
    int count = 0;
    Statement stm3 = null;
    ResultSet rs3 = null;
    try {
      stm3 = conn.createStatement();
      rs3 = stm3.executeQuery(sql);
      if (rs3.next()){
        count = rs3.getInt(1);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, rs3, null); 
    }
    return count;
  }
  public long getEmailSize( String query , Connection conn) throws Exception {
    Statement stm3 = null;
    ResultSet rs3 = null;
    long EMAIL_SIZE3 = 0;
    try {
      stm3 = conn.createStatement();
      rs3 = stm3.executeQuery(query);
      while (rs3.next()){
        String ATTACEMENT_ID = rs3.getString("ATTACHMENT_ID");
        if (!YHUtility.isNullorEmpty(ATTACEMENT_ID)) {
          EMAIL_SIZE3 += rs3.getInt("ENSIZE");
        } else {
          EMAIL_SIZE3 += 1024;
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, rs3, null); 
    }
    return EMAIL_SIZE3;
  }
  public long getEmailSize(int seqId , Connection conn) throws Exception {
    String query = "SELECT ENSIZE,ATTACHMENT_ID from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and oa_email.TO_ID='"+seqId+"' and SEND_FLAG='1' and DELETE_FLAG!='1'";
    long EMAIL_SIZE1=getEmailSize(query, conn);
    query = "SELECT ENSIZE,ATTACHMENT_ID from oa_email_body where FROM_ID='"+seqId+"' and SEND_FLAG='0'";
    long EMAIL_SIZE2= this.getEmailSize(query, conn);
    String sql = "SELECT  oa_email_body.SEQ_ID, ENSIZE , ATTACHMENT_ID from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and FROM_ID='"+seqId+"' and SEND_FLAG='1' and DELETE_FLAG!='2' and DELETE_FLAG!='4'";
    Statement stm3 = null;
    ResultSet rs3 = null;
    long EMAIL_SIZE3 = 0;
    String sqlStr = "";
    try {
      stm3 = conn.createStatement();
      rs3 = stm3.executeQuery(sql);
      while (rs3.next()){
        int seqId1 = rs3.getInt(1);
        if (!this.findId(sqlStr, seqId1 + "")) {
          String ATTACEMENT_ID = rs3.getString("ATTACHMENT_ID");
          if (!YHUtility.isNullorEmpty(ATTACEMENT_ID)) {
            EMAIL_SIZE3 += rs3.getInt("ENSIZE");
          } else {
            EMAIL_SIZE3 += 1024;
          }
           sqlStr += seqId1 + ",";
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, rs3, null); 
    }
    long EMAIL_SIZE4=EMAIL_SIZE1+EMAIL_SIZE2+EMAIL_SIZE3;
    return EMAIL_SIZE4;
  }
  public void getUserResByDeptId(Connection conn , StringBuffer sb  ,int deptId , String deptName) throws Exception{
    String query = "SELECT  A.SEQ_ID,USER_NAME,EMAIL_CAPACITY,FOLDER_CAPACITY from PERSON A , USER_PRIV  where A.USER_PRIV = USER_PRIV.SEQ_ID AND  A.DEPT_ID='"+ deptId +"' order by USER_PRIV.PRIV_NO , USER_NO DESC ,A.SEQ_ID";
    Statement stm3 = null;
    ResultSet rs3 = null;
    try {
      stm3 = conn.createStatement();
      rs3 = stm3.executeQuery(query);
      while (rs3.next()){
         int seqId = rs3.getInt("SEQ_ID");
         String userName = rs3.getString("USER_NAME");
         int emailCapacity = rs3.getInt("EMAIL_CAPACITY");
         int folderCapacity = rs3.getInt("FOLDER_CAPACITY");
         long emailSize = this.getEmailSize(seqId, conn);
         YHFolderSizeLogic logic = new YHFolderSizeLogic();
         long sortSize = logic.getSize(conn, seqId);
         int fileCount = logic.fileCount;
         
         int flag1 = 0;
         if (emailCapacity > 0) {
           if(emailSize >= emailCapacity*1024*1024)
             flag1=2;
           else if(emailSize >= emailCapacity*1024*1024*0.8)
             flag1=1;
         }
         
         int flag2 = 0;
         if (folderCapacity > 0) {
           if(sortSize >= folderCapacity*1024*1024)
             flag2=2;
           else if(sortSize >= folderCapacity*1024*1024*0.8)
             flag2=1;
         }
         
         sb.append("{");
         sb.append("userName:'" + userName + "'");
         sb.append(",deptName:'" +  deptName + "'");
         sb.append(",emailSize:'" +  this.getSizeStr(emailSize) + "'");
         sb.append(",fileSize:'" + this.getSizeStr(sortSize) + "'");
         sb.append(",flag1:'" + flag1 + "'");
         sb.append(",flag2:'" + flag2 + "'");
         sb.append(",fileCount:'" + fileCount + "'");
         sb.append("},");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, rs3, null); 
    }
  }
  public String getSizeStr(double sortSize) {
    double tmp2 = (double) sortSize / 1024 / 1024;
    String result2 = "";
    if (Math.floor(tmp2) > 0) {
      result2 = Math.round(tmp2) + "MB";
    } else {
      result2 = Math.ceil(sortSize / 1024) + "KB";
    }
    return result2;
  }
  public String getUserRes(Connection conn, int userId) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb  = new StringBuffer();
    String query = "SELECT  SEQ_ID,USER_NAME,EMAIL_CAPACITY,FOLDER_CAPACITY from PERSON   where SEQ_ID=" + userId;
    Statement stm3 = null;
    ResultSet rs3 = null;
    try {
      stm3 = conn.createStatement();
      rs3 = stm3.executeQuery(query);
      if (rs3.next()){
         int seqId = rs3.getInt("SEQ_ID");
         String userName = rs3.getString("USER_NAME");
         int emailCapacity = rs3.getInt("EMAIL_CAPACITY");
         int folderCapacity = rs3.getInt("FOLDER_CAPACITY");
         query = "SELECT ENSIZE, ATTACHMENT_ID from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and oa_email.TO_ID='"+seqId+"' and SEND_FLAG='1' and DELETE_FLAG!='1'";
         long EMAIL_SIZE1=this.getEmailSize(query, conn);
         query  = "SELECT count(*) from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and oa_email.TO_ID='"+seqId+"' and SEND_FLAG='1' and (DELETE_FLAG='' or  DELETE_FLAG='0' or DELETE_FLAG='2')";
         int incount = this.getEmailSizeBySql(query, conn);
         query = "SELECT ENSIZE, ATTACHMENT_ID from oa_email_body where FROM_ID='"+seqId+"' and SEND_FLAG='0'";
         long EMAIL_SIZE2= this.getEmailSize(query, conn);
         query = "SELECT count(*) from oa_email_body where FROM_ID='"+seqId+"' and SEND_FLAG='0'";
         int outboxCount = this.getEmailSizeBySql(query, conn);
         query = "SELECT count(*) from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and FROM_ID='"+seqId+"' and SEND_FLAG='1' and DELETE_FLAG!='2' and DELETE_FLAG!='4'";
         int sentboxCount = this.getEmailSizeBySql(query, conn);
         String sql = "SELECT  oa_email_body.SEQ_ID, ENSIZE, ATTACHMENT_ID from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and FROM_ID='"+seqId+"' and SEND_FLAG='1' and DELETE_FLAG!='2' and DELETE_FLAG!='4'";
         Statement stm4 = null;
         ResultSet rs4 = null;
         long EMAIL_SIZE3 = 0;
         String sqlStr = "";
         try {
           stm4 = conn.createStatement();
           rs4 = stm4.executeQuery(sql);
           while (rs4.next()){
             int seqId1 = rs4.getInt(1);
             if (!this.findId(sqlStr, seqId1 + "")) {
               String ATTACEMENT_ID = rs4.getString("ATTACHMENT_ID");
               if (!YHUtility.isNullorEmpty(ATTACEMENT_ID)) {
                 EMAIL_SIZE3 += rs4.getInt("ENSIZE");
               } else {
                 EMAIL_SIZE3 += 1024;
               }
               sqlStr += seqId1 + ",";
             }
           }
         } catch(Exception ex) {
           throw ex;
         } finally {
           YHDBUtility.close(stm4, rs4, null); 
         }
         long emailSize=EMAIL_SIZE1+EMAIL_SIZE2+EMAIL_SIZE3;
         
         YHFolderSizeLogic logic = new YHFolderSizeLogic();
         long sortSize = logic.getSize(conn, seqId);
         int fileCount = logic.fileCount;
         
         int boxCount = outboxCount + incount  + sentboxCount ;
         long allSize = emailSize + sortSize;
         sb.append("{");
         sb.append("userName:'" + userName + "'");
         sb.append(",inboxCount:'" + incount + "'");
         sb.append(",outboxCount:'" + outboxCount + "'");
         sb.append(",sentboxCount:'" + sentboxCount + "'");
         sb.append(",boxCount:'" + boxCount + "'");
         
         sb.append(",emailSize4:'" + emailSize + "'");
         sb.append(",emailSize4M:'" + this.getSizeStr(emailSize) + "'");
         sb.append(",emailSize1:'" + EMAIL_SIZE1 + "'");
         sb.append(",emailSize1M:'" + this.getSizeStr(EMAIL_SIZE1) + "'");
         sb.append(",emailSize2:'" + EMAIL_SIZE2 + "'");
         sb.append(",emailSize2M:'" + this.getSizeStr(EMAIL_SIZE2) + "'");
         sb.append(",emailSize3:'" + EMAIL_SIZE3 + "'");
         sb.append(",emailSize3M:'" + this.getSizeStr(EMAIL_SIZE3) + "'");
         sb.append(",sortSize:'" + sortSize + "'");
         sb.append(",sortSizeM:'" + this.getSizeStr(sortSize) + "'");
         sb.append(",allSize:'" + this.getSizeStr(allSize) + "'");
         sb.append(",sortCount:'" + fileCount + "'");
         sb.append(",folderCapacity:'" + folderCapacity + "'");
         sb.append(",emailCapacity:'" + emailCapacity + "'");
         sb.append("}");
      } else {
        sb.append("''");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, rs3, null); 
    }
    return sb.toString();
  }
  public String getUser(Connection conn) throws Exception {
    // TODO Auto-generated method stub
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    Statement stm4 = null;
    ResultSet rs4 = null;
    int count = 0 ;
    try {
      stm4 = conn.createStatement();
      String sql = "select PERSON.SEQ_ID , USER_NAME , PRIV_NAME FROM PERSON , USER_PRIV WHERE PERSON.USER_PRIV = USER_PRIV.SEQ_ID AND PERSON.DEPT_ID = 0";
      rs4 = stm4.executeQuery(sql );
      while (rs4.next()){
        int seqId = rs4.getInt("SEQ_ID");
        String userName = rs4.getString("USER_NAME");
        String privName = rs4.getString("PRIV_NAME");
        
        sb.append("{");
        sb.append("userId:'" + seqId  + "'" );
        sb.append(",userName:'" + userName  + "'" );
        sb.append(",privName:'" + privName  + "'" );
        sb.append("},");
        count ++;
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs4, null); 
    }
    if (count > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    return sb.toString();
  }
  public String delRs(Connection conn, String endDate, String startDate , String sms , String email , String emailInbox) throws Exception {
    // TODO Auto-generated method stub
    String remark = "";
    if (!YHUtility.isNullorEmpty(endDate)) {
      remark = "删除" + endDate + "之前";
    }
    if (!YHUtility.isNullorEmpty(startDate) && !YHUtility.isNullorEmpty(endDate)) {
      remark = "删除" + startDate + "-" + endDate;
    }
    String str = "";
    if (!YHUtility.isNullorEmpty(startDate)) {
      str = YHDBUtility.getDateFilter("SEND_TIME", startDate, " >= ") + " AND ";
    }
    if (!YHUtility.isNullorEmpty(endDate)) {
      str += YHDBUtility.getDateFilter("SEND_TIME", endDate, " <= ")+ " AND ";
    }
    if (str.endsWith(" AND ")) {
      str = str.substring(0, str.length() - 5);
    }
    if ("on".equals(sms)) {
      String query = "select SEQ_ID from SMS_BODY where " + str ;
      Statement stm3 = null;
      ResultSet rs3 = null;
      String bodyIdStr = "";
      try {
        stm3 = conn.createStatement();
        rs3 = stm3.executeQuery(query);
        while (rs3.next()){
          bodyIdStr += rs3.getInt(1) + ",";
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm3, rs3, null); 
      }
      if (bodyIdStr.endsWith(",")) {
        bodyIdStr = bodyIdStr.substring(0, bodyIdStr.length() - 1);
      }
      if (!"".equals(bodyIdStr)) {
        String query2 = "delete from SMS where BODY_SEQ_ID in (" + bodyIdStr + ")";
        this.executeSql(query2, conn);
        query2 = "delete from oa_msg_body where " + str;
        this.executeSql(query2, conn);
      }
      remark += "的所有内部短信";
    }
    if ("on".equals(email) || "on".equals(emailInbox)) {
      String query = "select oa_email.SEQ_ID AS EMAIL_ID ,BODY_ID ,ATTACHMENT_ID,ATTACHMENT_NAME   from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and  " + str;
      if ("on".equals(emailInbox)) {
        query +=  " and BOX_ID='0' and SEND_FLAG='1' and (DELETE_FLAG='' or DELETE_FLAG='0' or DELETE_FLAG='2')";
      }
      Statement stm3 = null;
      ResultSet rs3 = null;
      String bodyIdStr = "";
      String emailIdStr = "";
      try {
        stm3 = conn.createStatement();
        rs3 = stm3.executeQuery(query);
        while (rs3.next()){
          String emailId = rs3.getString("EMAIL_ID");
          int bodyId = rs3.getInt("BODY_ID");
          String attId = rs3.getString("ATTACHMENT_ID");
          String attName = rs3.getString("ATTACHMENT_NAME");
          if (!YHUtility.isNullorEmpty(attId)) {
            this.deleteAttachments(attId, attName);
          }
          bodyIdStr += bodyId + ",";
          emailIdStr += emailId  +  ",";
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm3, rs3, null); 
      }
      bodyIdStr = this.getOutOfTail(bodyIdStr);
      emailIdStr = this.getOutOfTail(emailIdStr);
      if (!"".equals(emailIdStr)) {
        String query1 = "delete from oa_email where SEQ_ID in (" +  emailIdStr + ")";
        this.executeSql(query1, conn);
      }
      if (!"".equals(bodyIdStr)) {
        String query1 = "delete from oa_email_body where SEQ_ID in (" +  bodyIdStr + ")";
        this.executeSql(query1, conn);
      }
      if ("on".equals(sms)) {
        remark = "和所有内部邮件";
      } else if  ("on".equals(email))  {
        remark = "的所有内部邮件";
      } else {
        remark ="的所有内部邮件收件箱邮件";
      }
    }
    return remark;
  }
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
  public String batRes(Connection conn, String endDate, String startDate,
      String sms, String email, String emailInbox , String path ) throws Exception {
    // TODO Auto-generated method stub
    String exportStr = "";
    if (!YHUtility.isNullorEmpty(path) && !path.endsWith( File.separator  )) {
      path +=  File.separator  ;
    }
    path += "email";
    String str = "";
    if (!YHUtility.isNullorEmpty(startDate)) {
      str = YHDBUtility.getDateFilter("SEND_TIME", startDate, " >= ") + " AND ";
      String tmp = startDate.replace("-", "");
      tmp = tmp.replace(" ", "");
      tmp = tmp.replace(":", "");
      path += tmp + "-";
    }
    if (!YHUtility.isNullorEmpty(endDate)) {
      str += YHDBUtility.getDateFilter("SEND_TIME", endDate, " <= ")+ " AND ";
      String tmp = endDate.replace("-", "");
      tmp = tmp.replace(" ", "");
      tmp = tmp.replace(":", "");
      path += tmp;
    }
    if (str.endsWith(" AND ")) {
      str = str.substring(0, str.length() - 5);
    }
    if ("on".equals(email) || "on".equals(emailInbox)) {
      String query = "select oa_email.SEQ_ID AS EMAIL_ID , ATTACHMENT_ID , ATTACHMENT_NAME from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID  and " + str ;
      if ("on".equals(emailInbox)) {
        query += "  and BOX_ID='0' and SEND_FLAG='1' and (DELETE_FLAG='' or DELETE_FLAG='0' or DELETE_FLAG='2')";
      }
      //query += " group by EMAIL.BODY_ID ";
      PreparedStatement stm3 = null;
      ResultSet rs3 = null;
      try {
        stm3 = conn.prepareStatement(query);
        rs3 = stm3.executeQuery();
        while (rs3.next()){
          String attId = rs3.getString("ATTACHMENT_ID");
          String attName = rs3.getString("ATTACHMENT_NAME") ;
          if (!YHUtility.isNullorEmpty(attId)) {
            String[] attIds = attId.split(",");
            String[] attNames  = attName.split("\\*");
            for (int i = 0 ;i < attIds.length ; i++) {
              String att = attIds[i];
              String attN = attNames[i];
              if ("".equals(att)) {
                continue;
              }
              String fileSrc = "";
              String fileDes = "";
              if (att.indexOf("_") != -1) {
                 fileSrc = "\"" + this.getRealPath(attN, att) + "\"";
                 int index = att.indexOf("_");
                 String hard = att.substring(0, index);
                 fileDes = path + "email"  + File.separator   + hard +   File.separator ;
              } else {
                fileSrc = "\"" + this.getRealPath(attN, att) + "\"";
                fileDes = path + att +  File.separator ;
              }
              exportStr += "xcopy /Q/R/Y " + fileSrc + " \"" + fileDes + "\"\r\n";
            }
          }
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm3, rs3, null); 
      }
    }
    return exportStr;
  }
  public String getRealPath2(String aName , String aId , String moduleName ) {
    String filePath = YHSysProps.getAttachPath()  + File.separator  +  moduleName;
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
    String path = filePath +  File.separator  + hard +  File.separator  + str + "_" + aName;
    File file = new File(path);
    if(file.exists()){
      return path;
    } else {
      //兼容老的数据
      String path2 = filePath +  File.separator   + hard +  File.separator + str + "." + aName;
      File file2 = new File(path2);
      if(file2.exists()){
        return path2;
      }
    }
    return path;
  }
  public String getRealPath(String aName , String aId ) {
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
    String path = filePath +  File.separator   + hard +  File.separator  + str + "_" + aName;
    File file = new File(path);
    if(file.exists()){
      return path;
    } else {
      //兼容老的数据
      String path2 = filePath +  File.separator  + hard +  File.separator + str + "." + aName;
      File file2 = new File(path2);
      if(file2.exists()){
        return path2;
      }
    }
    return path;
  }
  public String delUserRs(Connection conn 
      , String toId 
      ,String address
      , String calendar
      ,String diary
      , String url
      ,String email
      , String folder
  ) throws Exception {
    // TODO Auto-generated method stub
    if (toId == null) {
      toId = "";
    }
    toId = this.getOutOfTail(toId);
    int rowCount = 0;
    long spaceCount = 0;
     
    if ("on".equals(address)) {
      String query = "delete from oa_address where USER_ID!='' and USER_ID is not null and " + YHDBUtility.findInSet(toId, "USER_ID") ;;
      rowCount += this.executeSql(query, conn);
      query = "delete from oa_address_team where USER_ID!='' and USER_ID is not null and " + YHDBUtility.findInSet(toId, "USER_ID") ;;
      rowCount += this.executeSql(query, conn);
    }
  //日程安排
    if ("on".equals(calendar)) {
      String query = "delete from oa_affairs where   " + YHDBUtility.findInSet(toId, "USER_ID") ;;
      rowCount += this.executeSql(query, conn);
      query = "delete from oa_schedule where  " + YHDBUtility.findInSet(toId, "USER_ID") ;;
      rowCount += this.executeSql(query, conn);
    }
  //工作日志
    if ("on".equals(diary)) {
      String query = "delete from oa_journal where   " + YHDBUtility.findInSet(toId, "USER_ID") ;;
      rowCount += this.executeSql(query, conn);
    }
  //个人网址
    if ("on".equals(url)) {
      String query = "delete from URL where  USER!='' and USER is not null AND  " + YHDBUtility.findInSet(toId, "USER") ;
      rowCount += this.executeSql(query, conn);
    }
  //邮件
    if ("on".equals(email)) {
      rowCount += this.delUserEmail(conn, toId);
    }
    if ("on".equals(folder)) {
      String query  = "select * from oa_file_sort where SORT_TYPE='4' and SORT_PARENT=0 and " +  YHDBUtility.findInSet(toId, "USER_ID") ;
      Statement stm3 = null;
      ResultSet rs3 = null;
      try {
        stm3 = conn.createStatement();
        rs3 = stm3.executeQuery(query);
        while (rs3.next()){
          rowCount ++;
          int sortId = rs3.getInt("SORT_ID");
          //删除子文件
          Map map2 = new HashMap();
          map2.put("ROW_COUNT", rowCount);
          map2.put("SPACE_COUNT", spaceCount);
          this.deleteChildren(sortId, conn, map2);
          rowCount =(Integer) map2.get("ROW_COUNT");
          spaceCount =(Long) map2.get("SPACE_COUNT");
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm3, rs3, null); 
      }
      
      query = "delete from oa_file_sort where SORT_TYPE='4' and SORT_PARENT=0 and "  +  YHDBUtility.findInSet(toId, "USER_ID") ;
      rowCount += this.executeSql(query, conn);

      query = "select * from oa_file_content where SORT_ID=0 and " +  YHDBUtility.findInSet(toId, "USER_ID") ;
      Statement stm4 = null;
      ResultSet rs4 = null;
      try {
        stm4 = conn.createStatement();
        rs4 = stm4.executeQuery(query);
        while (rs4.next()){
          rowCount ++;
          String attId = rs4.getString("ATTACHMENT_ID");
          String attName = rs4.getString("ATTACHMENT_NAME");
          YHFolderSizeLogic logic = new YHFolderSizeLogic();
          spaceCount += logic.getAttachSize(attId, attName);
          this.deleteAttachments(attId, attName , "file_folder");
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm4, rs4, null); 
      }
      query = "delete from oa_file_content where SORT_ID=0 and " +  YHDBUtility.findInSet(toId, "USER_ID") ;
      rowCount += this.executeSql(query, conn);
    }
    return null;
  }
  private void deleteAttachments(String attachmentId, String attachmentName, String module) {
    // TODO Auto-generated method stub
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
      this.deleteAttachement(tmpId, tmpName , module);
    }
  }
  public int delUserEmail(Connection conn , String toId) throws Exception{
    int rowCount = 0;
    String query ="delete from oa_email_box where "  + YHDBUtility.findInSet(toId, "USER_ID") ;
    rowCount += this.executeSql(query, conn);
    //收到的邮件
    String bodyIdStr = "";
    String query1 ="select * from oa_email where (DELETE_FLAG='2' or DELETE_FLAG='4') and " + YHDBUtility.findInSet(toId, "TO_ID") ;
    Statement stm3 = null;
    ResultSet rs3 = null;
    try {
      stm3 = conn.createStatement();
      rs3 = stm3.executeQuery(query1);
      while (rs3.next()){
        int bodyId = rs3.getInt("BODY_ID");
        String query2 = "select count(*) from oa_email where BODY_ID='" + bodyId + "'";
        int groupCount =  getEmailSizeBySql(query2 , conn);
        if (groupCount <= 1) {
          query2 = "select * from oa_email_body where SEQ_ID='" + bodyId + "'";
          Statement stm5 = null;
          ResultSet rs5 = null;
          try {
            stm5 = conn.createStatement();
            rs5 = stm5.executeQuery(query2);
            if (rs5.next()){
              String attId = rs5.getString("ATTACHMENT_ID");
              String attName = rs5.getString("ATTACHMENT_NAME");
              if (YHUtility.isNullorEmpty(attName)) {
                this.deleteAttachments(attId, attName);
              }
            }
            bodyIdStr += bodyId + ",";
          } catch(Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm5, rs5, null); 
          }
        }
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, rs3, null); 
    }
    bodyIdStr = this.getOutOfTail(bodyIdStr);
    if (!"".equals(bodyIdStr)) {
       query = "delete from oa_email_body where SEQ_ID in(" + bodyIdStr + ")";
       rowCount += this.executeSql(query, conn);
    }
    query = "delete from oa_email where (DELETE_FLAG='2' or DELETE_FLAG='4') and " + YHDBUtility.findInSet(toId, "TO_ID") ;;
    rowCount += this.executeSql(query, conn);
    
    query = "update oa_email set DELETE_FLAG='1' where (DELETE_FLAG='3' or DELETE_FLAG='' or DELETE_FLAG='0') and " + YHDBUtility.findInSet(toId, "TO_ID") ;
    this.executeSql(query, conn);
    //发送的邮件
    query = "select oa_email.SEQ_ID AS EMAIL_ID , BODY_ID , SUBJECT , FROM_ID  ,oa_email.TO_ID , READ_FLAG,DELETE_FLAG , ATTACHMENT_ID ,ATTACHMENT_NAME from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and (DELETE_FLAG='1' or READ_FLAG!='1') and " + YHDBUtility.findInSet(toId, "FROM_ID") ;;
    Statement stm5 = null;
    ResultSet rs5 = null;
    String emailIdStr = "";
    try {
      stm5 = conn.createStatement();
      rs5 = stm5.executeQuery(query);
      if (rs5.next()){
        int emailId = rs5.getInt("EMAIL_ID");
        int bodyId = rs5.getInt("BODY_ID");
        String subject = rs5.getString("SUBJECT");
        String fromId =rs5.getString("FROM_ID");
        String toIdStr = rs5.getString("TO_ID");
        String readFlag = rs5.getString("READ_FLAG");
        String deleteFlag = rs5.getString("DELETE_FLAG");
        String attId = rs5.getString("ATTACHMENT_ID");
        String attName = rs5.getString("ATTACHMENT_NAME");
        query = "select count(*) from oa_email where BODY_ID='"+bodyId+"'";
        int groupCount = this.getEmailSizeBySql(query, conn);
        if (groupCount <= 1) {
          if (YHUtility.isNullorEmpty(attName)) {
            this.deleteAttachments(attId, attName);
          }
          bodyIdStr += bodyId + ",";
        }
        if ("0".equals(readFlag)) {
          String smsContent = "请查收我的邮件！\n主题：" + subject.substring(0, 100);
          //删除短信
          
        }
        emailIdStr += emailId + ",";
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm5, rs5, null); 
    }
    emailIdStr = this.getOutOfTail(emailIdStr); 
    if(!"".equals(emailIdStr))
    {
       query = "delete from oa_email where EMAIL_ID in ("+ emailIdStr +")";
       rowCount += this.executeSql(query, conn);
    }
    
    bodyIdStr = this.getOutOfTail(bodyIdStr); 
    if(!"".equals(bodyIdStr))
    {
      query = "delete from oa_email_body where SEQ_ID in ("+ bodyIdStr +")";
      rowCount += this.executeSql(query, conn);
    }
    query = "update oa_email a set DELETE_FLAG='2' where exists (select 1 from oa_email_body b where a.BODY_ID=b.SEQ_ID and ("+YHWorkFlowUtility.createFindSql("b.FROM_ID", toId)+"))" 
      + " and  (DELETE_FLAG='' or DELETE_FLAG='0') and READ_FLAG='1'" ;
    this.executeSql(query, conn);
    
    query = "update oa_email a set DELETE_FLAG='4' where  exists (select 1 from oa_email_body b where a.BODY_ID=b.SEQ_ID and ("+YHWorkFlowUtility.createFindSql("b.FROM_ID", toId)+"))"
      + " and  DELETE_FLAG='3' and READ_FLAG='1' " ;
    this.executeSql(query, conn);
    return rowCount;
  }
  public void deleteChildren(int sortId , Connection conn , Map map) throws Exception {
    int rowCount = (Integer)map.get("ROW_COUNT");
    Object s = map.get("SPACE_COUNT");
    long spaceCount = 0;
    if (s.getClass() == Integer.class) {
      spaceCount = (Integer)map.get("SPACE_COUNT");
    } else {
      spaceCount = (Long)map.get("SPACE_COUNT");
    }
     
    
    String query = "select * from oa_file_content where SORT_ID='" + sortId + "'";
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()){
        rowCount ++;
        String attId = rs4.getString("ATTACHMENT_ID");
        String attName = rs4.getString("ATTACHMENT_NAME");
        YHFolderSizeLogic logic = new YHFolderSizeLogic();
        spaceCount += logic.getAttachSize(attId, attName);
        this.deleteAttachments(attId, attName , "file_folder");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs4, null); 
    }
     query = "delete from oa_file_content where SORT_ID='" + sortId + "'";
     this.executeSql(query, conn);
     query = "select * from oa_file_sort where SORT_PARENT='" + sortId + "'";
     Statement stm5 = null;
     ResultSet rs5 = null;
     try {
       stm5 = conn.createStatement();
       rs5 = stm5.executeQuery(query);
       while (rs5.next()){
         rowCount ++;
         sortId = rs5.getInt("SEQ_ID");
         Map map2 = new HashMap();
         map2.put("ROW_COUNT", rowCount);
         map2.put("SPACE_COUNT", spaceCount);
         this.deleteChildren(sortId, conn, map2);
       }
     } catch(Exception ex) {
       throw ex;
     } finally {
       YHDBUtility.close(stm5, rs5, null); 
     }
     query = "delete from oa_file_sort where SEQ_ID='"+sortId+"'";
     this.executeSql(query, conn);
  }
  public String batUserRes(Connection conn, String diary, String email,
      String folder , String toId , String path) throws Exception {
    // TODO Auto-generated method stub
    String exportStr = "";
    if (!YHUtility.isNullorEmpty(path) && !path.endsWith( File.separator )) {
      path +=  File.separator ;
    }
    String path1 = path +  "email";
    toId = this.getOutOfTail(toId);
    
    String query = "";
    if ("on".equals(email)) {
      query = "select ATTACHMENT_ID , ATTACHMENT_NAME from oa_email,oa_email_body whereoa_email.BODY_ID=oa_email_body.SEQ_ID  and " 
        + "("+  YHWorkFlowUtility.createFindSql("FROM_ID", toId)  +" or "+   YHWorkFlowUtility.createFindSql("oa_email.TO_ID", toId) +") ";
      Statement stm3 = null;
      ResultSet rs3 = null;
      try {
        stm3 = conn.createStatement();
        rs3 = stm3.executeQuery(query);
        while (rs3.next()){
          String attId = rs3.getString("ATTACHMENT_ID");
          String attName = rs3.getString("ATTACHMENT_NAME");
          if (!YHUtility.isNullorEmpty(attId)) {
            String[] attIds = attId.split(",");
            String[] attNames  = attName.split("\\*");
            for (int i = 0 ;i < attIds.length ; i++) {
              String att = attIds[i];
              String attN = attNames[i];
              if ("".equals(att)) {
                continue;
              }
              String fileSrc = "";
              String fileDes = "";
              if (att.indexOf("_") != -1) {
                 fileSrc = "\"" + this.getRealPath(attN, att) + "\"";
                 int index = att.indexOf("_");
                 String hard = att.substring(0, index);
                 fileDes = path1 + "email" +  File.separator   + hard +  File.separator ;
              } else {
                fileSrc = "\"" + this.getRealPath(attN, att) + "\"";
                fileDes = path1 + att +  File.separator ;
              }
              exportStr += "xcopy /Q/R/Y " + fileSrc + " \"" + fileDes + "\"\r\n";
            }
          }
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm3, rs3, null); 
      }
    }
    //个人文件柜
    String path2 = path +  "file_folder";
    if ("on".equals(folder)) {
      String[] toIds = toId.split(",");
      for (String id : toIds) {
        if ("".equals(id)) {
          continue;
        }
        query = "select * from oa_file_sort where SORT_TYPE='4' and SORT_PARENT=0 and " + YHDBUtility.findInSet(id, "USER_ID");
        Statement stm5 = null;
        ResultSet rs5 = null;
        try {
          stm5 = conn.createStatement();
          rs5 = stm5.executeQuery(query);
          while (rs5.next()){
            int sortId = rs5.getInt("SEQ_ID");
            //删除子文件

            Map map2 = new HashMap();
            map2.put("ROW_COUNT", 0);
            map2.put("SPACE_COUNT", 0);
            this.deleteChildren(sortId, conn, map2);
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm5, rs5, null); 
        }
        query = "select * from oa_file_content where SORT_ID=0  and " + YHDBUtility.findInSet(id, "USER_ID") ;
        Statement stm4 = null;
        ResultSet rs4 = null;
        try {
          stm4 = conn.createStatement();
          rs4 = stm4.executeQuery(query);
          while (rs4.next()){
            String attId = rs4.getString("ATTACHMENT_ID");
            String attName = rs4.getString("ATTACHMENT_NAME");
            if (!YHUtility.isNullorEmpty(attId)) {
              String[] attIds = attId.split(",");
              String[] attNames  = attName.split("\\*");
              for (int i = 0 ;i < attIds.length ; i++) {
                String att = attIds[i];
                String attN = attNames[i];
                if ("".equals(att)) {
                  continue;
                }
                String fileSrc = "";
                String fileDes = "";
                if (att.indexOf("_") != -1) {
                   fileSrc = "\"" + this.getRealPath2(attN, att , "file_folder") + "\"";
                   int index = att.indexOf("_");
                   String hard = att.substring(0, index);
                   fileDes = path2 + "file_folder" +  File.separator   + hard +  File.separator ;
                } else {
                  fileSrc = "\"" + this.getRealPath(attN, att) + "\"";
                  fileDes = path2 + att +  File.separator ;
                }
                exportStr += "xcopy /Q/R/Y " + fileSrc + "\"" + fileDes + "\"\r\n";
              }
            }
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm4, rs4, null); 
        } 
      }
    } 
    String path3 = path +  "diary";
    if ("on".equals(diary)) {
      query = "select * from oa_journal where " + YHDBUtility.findInSet(toId, "USER_ID") ;
      Statement stm6 = null;
      ResultSet rs6 = null;
      try {
        stm6 = conn.createStatement();
        rs6 = stm6.executeQuery(query);
        while (rs6.next()){
          String attId = rs6.getString("ATTACHMENT_ID");
          String attName = rs6.getString("ATTACHMENT_NAME");
          if (!YHUtility.isNullorEmpty(attId)) {
            String[] attIds = attId.split(",");
            String[] attNames  = attName.split("\\*");
            for (int i = 0 ;i < attIds.length ; i++) {
              String att = attIds[i];
              String attN = attNames[i];
              if ("".equals(att)) {
                continue;
              }
              String fileSrc = "";
              String fileDes = "";
              if (att.indexOf("_") != -1) {
                 fileSrc = "\"" + this.getRealPath2(attN, att , "diary") + "\"";
                 int index = att.indexOf("_");
                 String hard = att.substring(0, index);
                 fileDes = path3 + "diary" + File.separator   + hard +  File.separator ;
              } else {
                fileSrc = "\"" + this.getRealPath(attN, att) + "\"";
                fileDes = path3 + att +  File.separator ;
              }
              exportStr += "xcopy /Q/R/Y " + fileSrc + "\"" + fileDes + "\"\r\n";
            }
          }
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm6, rs6, null); 
      }
    }
    return exportStr;
  }
  /**
   * 判段id是不是在str里面
   * @param str
   * @param id
   * @return
   */
  public static boolean findId(String str, String id) {
    if(str == null || id == null || "".equals(str) || "".equals(id)){
      return false;
    }
    String[] aStr = str.split(",");
    for(String tmp : aStr){
      if(tmp.equals(id)){
        return true;
      }
    }
    return false;
  }
  public String delGarbageCon(Connection conn) throws Exception {
    String query = "select SEQ_ID from PERSON order by SEQ_ID";
    Statement stm5 = null;
    ResultSet rs5 = null;
    String userIdStr = "";
    try {
      stm5 = conn.createStatement();
      rs5 = stm5.executeQuery(query);
      while (rs5.next()){
        int seqId = rs5.getInt("SEQ_ID");
        userIdStr += seqId + ",";
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm5, rs5, null); 
    }
    String userGarbageStr = "";
    //个人通讯簿
    query = "select USER_ID from oa_address where USER_ID!='' and USER_ID is not null and not "  + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    userGarbageStr = this.getUserIds(conn, userGarbageStr, query);
    query = "select USER_ID from oa_address where USER_ID!='' and USER_ID is not null and not "  + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    userGarbageStr = this.getUserIds(conn, userGarbageStr, query);
    //日程安排
    query  ="select USER_ID from oa_affairs where not " + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    userGarbageStr = this.getUserIds(conn, userGarbageStr, query);
    query = "select USER_ID from oa_schedule where not " + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    userGarbageStr = this.getUserIds(conn, userGarbageStr, query);
    //工作日志
    query = "select USER_ID from oa_journal where not " + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    userGarbageStr = this.getUserIds(conn, userGarbageStr, query);
    //内部邮件
    query = "select USER_ID from oa_email_box where not " + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    userGarbageStr = this.getUserIds(conn, userGarbageStr, query);
    //收到的邮件
    query = "select TO_ID from oa_email where (DELETE_FLAG='2' or DELETE_FLAG='4') and not " + YHDBUtility.findInSet(userIdStr, "TO_ID") ;
    userGarbageStr = this.getUserIds(conn, userGarbageStr, query);
    //发送的邮件
    query = "select FROM_ID from oa_email,oa_email_body where oa_email.BODY_ID=oa_email_body.SEQ_ID and (DELETE_FLAG='1' or READ_FLAG!='1') and not " + YHDBUtility.findInSet(userIdStr, "FROM_ID") ;
    userGarbageStr = this.getUserIds(conn, userGarbageStr, query);
    //个人文件柜
    query = "select USER_ID from oa_file_sort where SORT_TYPE='4' and SORT_PARENT=0 and not  " + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    userGarbageStr = this.getUserIds(conn, userGarbageStr, query);
    query ="select USER_ID from oa_file_content where SORT_ID=0 and not " + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    userGarbageStr = this.getUserIds(conn, userGarbageStr, query);
    //人事档案
    String userIdStr2 =  this.getUserIdById(conn, userIdStr) ;
    query = "select USER_ID from oa_pm_employee_info where not " + YHDBUtility.findInSet(userIdStr2, "USER_ID") ;
    userGarbageStr = this.getUserIds2(conn, userGarbageStr, query);
    //个人网址
    query ="select USER from URL where USER!='' and not  " + YHDBUtility.findInSet(userIdStr, "USER") ;
    userGarbageStr = this.getUserIds(conn, userGarbageStr, query);
    return userIdStr;
  }
  public String getUserIdById(Connection conn , String ids) throws Exception {
    String[] idss = ids.split(",");
    String result = "";
    for (String id : idss) {
      if (!"".equals(id)) {
        String query = "SELECT USER_ID FROM PERSON WHERE SEQ_ID = " + id;
        Statement stm6 = null;
        ResultSet rs6 = null;
        try {
          stm6 = conn.createStatement();
          rs6 = stm6.executeQuery(query);
          while (rs6.next()){
            result += rs6.getString("USER_ID") + ",";
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm6, rs6, null); 
        }
      }
    }
    result = this.getOutOfTail(result);
    return result;
  }
  public String delGarbage(Connection conn) throws Exception {
    String query = "select SEQ_ID from PERSON order by SEQ_ID";
    Statement stm5 = null;
    ResultSet rs5 = null;
    String userIdStr = "";
    try {
      stm5 = conn.createStatement();
      rs5 = stm5.executeQuery(query);
      while (rs5.next()){
        int seqId = rs5.getInt("SEQ_ID");
        userIdStr += seqId + ",";
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm5, rs5, null); 
    }
    int rowCount = 0;
    long spaceCount = 0;
  //个人通讯簿
    query = "delete from oa_address where USER_ID!='' and not "  + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    rowCount += this.executeSql(query, conn);

    query ="delete from oa_address_team where USER_ID!='' and not "  + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    rowCount += this.executeSql(query, conn);
    

    //日程安排
    query="delete from oa_affairs where not "  + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    rowCount += this.executeSql(query, conn);

    query="delete from oa_schedule where not "  + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    rowCount += this.executeSql(query, conn);

    //工作日志
    query="delete from oa_journal where not "  + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    rowCount += this.executeSql(query, conn);
    //个人网址
    query="delete from URL where USER!='' and not "  + YHDBUtility.findInSet(userIdStr, "USER") ;
    rowCount += this.executeSql(query, conn);
    //邮件
    rowCount +=  this.delUserEmail(conn, userIdStr);
    
  //个人文件柜
    query ="select * from oa_file_sort where SORT_TYPE='4' and SORT_PARENT=0 and not "  + YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    Statement stm3 = null;
    ResultSet rs3 = null;
    try {
      stm3 = conn.createStatement();
      rs3 = stm3.executeQuery(query);
      while (rs3.next()){
        rowCount ++;
        int sortId = rs3.getInt("SEQ_ID");
        //删除子文件
        Map map2 = new HashMap();
        map2.put("ROW_COUNT", rowCount);
        map2.put("SPACE_COUNT", spaceCount);
        this.deleteChildren(sortId, conn, map2);
        rowCount =(Integer) map2.get("ROW_COUNT");
        spaceCount =(Long) map2.get("SPACE_COUNT");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm3, rs3, null); 
    }
    
    query = "delete from oa_file_sort where SORT_TYPE='4' and SORT_PARENT=0 and "  +  YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    rowCount += this.executeSql(query, conn);

    query = "select * from oa_file_content where SORT_ID=0 and " +  YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    Statement stm4 = null;
    ResultSet rs4 = null;
    try {
      stm4 = conn.createStatement();
      rs4 = stm4.executeQuery(query);
      while (rs4.next()){
        rowCount ++;
        String attId = rs4.getString("ATTACHMENT_ID");
        String attName = rs4.getString("ATTACHMENT_NAME");
        YHFolderSizeLogic logic = new YHFolderSizeLogic();
        spaceCount += logic.getAttachSize(attId, attName);
        this.deleteAttachments(attId, attName , "file_folder");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm4, rs4, null); 
    }
    query = "delete from oa_file_content where SORT_ID=0 and " +  YHDBUtility.findInSet(userIdStr, "USER_ID") ;
    this.executeSql(query, conn);
    //人事档案
    String userIdStr2 =  this.getUserIdById(conn, userIdStr) ;
    query ="select * from oa_pm_employee_info where not " +  YHDBUtility.findInSet(userIdStr2, "USER_ID") ;
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query);
      while (rs2.next()){
        rowCount ++;
        String photo = rs2.getString("PHOTO_NAME");
        photo =YHSysProps.getAttachPath()  +  File.separator + "hrms_pic" +  File.separator  + photo;
        File file = new File(photo);
        if (file.exists() && file.isFile()) {
          file.delete();
        }
        String attId = rs2.getString("ATTACHMENT_ID");
        String attName = rs2.getString("ATTACHMENT_NAME");
        YHFolderSizeLogic logic = new YHFolderSizeLogic();
        spaceCount += logic.getAttachSize(attId, attName);
        this.deleteAttachments(attId, attName , "file_folder");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    query = "delete from oa_pm_employee_info where not  " + YHDBUtility.findInSet(userIdStr2, "USER_ID") ;
    this.executeSql(query, conn);

    //删除空附件目录
    int folderCount = 0 ;
    String path = YHSysProps.getAttachPath();
    File dir = new File(path);
    File[] files = dir.listFiles();
    for (File tmp : files) {
      if (tmp.isDirectory()) {
        File[] files2 = tmp.listFiles();
        for (File tmp2 : files2) {
          if (tmp2.isDirectory()) {
            File[] files3 = tmp2.listFiles();
            if (files3.length < 1) {
              tmp2.delete();
              folderCount ++;
            }
          }
        }
      }
    }
    double tmp = (double) spaceCount / 1024 / 1024;
    DecimalFormat format = new DecimalFormat("0.0"); 
    String remark ="删除空目录："+folderCount+" 个<br>删除数据库记录："+rowCount+" 条<br>回收硬盘空间："+format.format(tmp)+" MB";
    return remark;
  }
  public String getUserIds(Connection conn , String userIds , String sql) throws Exception {
    Statement stm6 = null;
    ResultSet rs6 = null;
    String userIdAddress = "";
    try {
      stm6 = conn.createStatement();
      rs6 = stm6.executeQuery(sql);
      while (rs6.next()){
        int seqId = rs6.getInt(1);
        if (!this.findId(userIds , seqId + "")) {
          userIdAddress += seqId + ",";
        }
        userIds += seqId + ",";
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm6, rs6, null); 
    }
    return userIds;
  }
  public String getUserIds2(Connection conn , String userIds , String sql) throws Exception {
    Statement stm6 = null;
    ResultSet rs6 = null;
    String userIdAddress = "";
    try {
      stm6 = conn.createStatement();
      rs6 = stm6.executeQuery(sql);
      while (rs6.next()){
        String seqId = rs6.getString(1);
        if (!this.findId(userIds , seqId + "")) {
          userIdAddress += seqId + ",";
        }
        userIds += seqId + ",";
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm6, rs6, null); 
    }
    return userIds;
  }
}
