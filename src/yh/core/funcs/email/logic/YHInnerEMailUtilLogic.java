package yh.core.funcs.email.logic;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import yh.core.funcs.filefolder.data.YHFileContent;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;

public class YHInnerEMailUtilLogic {
  /**
   * 得到邮件总数量（收件箱和已删除邮件箱）
   * 
   * @param conn
   * @param fields
   * @param filters
   * @param type
   *          [1收件箱和已删除|2已发送邮件箱|3草稿箱]
   * @return
   * @throws Exception
   */
  public int getCount(Connection conn, String filters, int type, String field)
      throws Exception {
    int result = -1;
    String sql = "";
    if (type == 1) {// 收件箱和已删除      sql = "select " + field + " from " + "oa_email_body T0," + "oa_email T1  ";
    } else if (type == 2) {// 已发送邮件箱
      sql = "select " + field + " from " + " oa_email_body T0";
    } else if (type == 3) {// 草稿箱      sql = "select " + field + " from " + " oa_email_body T0";
    } 
    if (filters != null && !"".equals(filters)) {
      sql += " where " + filters;
    }
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    // select COUNT(*) from EMAIL where TO_ID = '196' AND DELETE_FLAG IN(0, 2)
    // System.out.println(sql);
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }

  /**
   * 得到邮箱的邮件数（已发送邮件箱）
   * 
   * @param conn
   * @param tableName
   * @param filters
   * @return
   * @throws Exception
   */
  public int getCount(Connection conn, int userId, String tableName,
      String filters) throws Exception {
    int result = -1;
    String sql = "select count(*) from " + tableName
        + " where BODY_ID IN(SELECT SEQ_ID FROM oa_email_body WHERE FROM_ID ="
        + userId + ")";
    if (filters != null && !"".equals(filters)) {
      sql += filters;
    }
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    // select COUNT(*) from EMAIL where TO_ID = '196' AND DELETE_FLAG IN(0, 2)
    // System.out.println(sql);
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }

  /**
   * 取得发件箱的邮件总数
   * 
   * @param conn
   * @param userId
   * @param filters
   * @return
   * @throws Exception
   */
  public int getCount2Send(Connection conn, int userId, String filters)
      throws Exception {
    int result = -1;
    String sql = "select COUNT(BODY_ID) from oa_email e,oa_email_body b where e.body_id=b.seq_id and b.from_id="+ userId ;

    if (filters != null && !"".equals(filters)) {
      sql += filters;
    }
    sql += " GROUP BY BODY_ID";
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    // select COUNT(*) from EMAIL where TO_ID = '196' AND DELETE_FLAG IN(0, 2)
    // System.out.println(sql);
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }

  /**
   * 得到所有符合条件的bodyId 只能取到收件箱和已删除邮件箱，自定义邮箱的bodyId
   * 
   * @param conn
   * @param filters
   * @return
   * @throws Exception
   */
  public String getMailIds(Connection conn, String filters) throws Exception {
    String result = "";
    String sql = "select BODY_ID from oa_email ";
    if (filters != null && !"".equals(filters)) {
      sql += " where " + filters;
    }
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    // select COUNT(*) from EMAIL where TO_ID = '196' AND DELETE_FLAG IN(0, 2)
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      int i = 0;
      while (rs.next()) {
        i++;
        int seqId = rs.getInt(1);
        if (!"".equals(result) && i < 500) {
          result += ",";
        }
        if (i == 500) {
          result += "*";
          i = 0;
        }
        result += seqId;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }
  /**
   * 得到所有符合条件的bodyId 只能取到收件箱和已删除邮件箱，自定义邮箱的bodyId
   * 
   * @param conn
   * @param filters
   * @return
   * @throws Exception
   */
  public String getMailIds(Connection conn, String filters,String type,String shortType) throws Exception {
    String result = "";
    String sql = "select BODY_ID from oa_email ";
    if (filters != null && !"".equals(filters)) {
      sql += " where " + filters;
    }
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    // select COUNT(*) from EMAIL where TO_ID = '196' AND DELETE_FLAG IN(0, 2)
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      int i = 0;
      while (rs.next()) {
        
        int seqId = rs.getInt(1);
        if("3".equals(type) && isMore(conn, seqId) && isMoreCanDel(conn, seqId, shortType)){
          continue;
        }
        i++;
        if (!"".equals(result) && i < 500) {
          result += ",";
        }
        if (i == 500) {
          result += "*";
          i = 0;
        }
        result += seqId;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }
  private boolean isMore(Connection conn,int bodyId) throws Exception{
    boolean result = false;
    String sql = "select count(*) from oa_email where body_id=" + bodyId; 
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        if (rs.getInt(1) > 1){
          result = true;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  public boolean isMoreCanDel(Connection conn, int bodyId, String shortType) throws Exception{
    boolean result = true;
    String sql = "select READ_FLAG, DELETE_FLAG from oa_email where body_id=" + bodyId; 
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        String deleteFlag = rs.getString(2);
        String readFlag = rs.getString(1);
        if("1".equals(shortType)){//已永久删除邮件          if (!"1".equals(deleteFlag)) {
            return false;
          }
        }else if("2".equals(shortType)){//未读邮件
          if ("1".equals(readFlag) || "1".equals(deleteFlag)) {
            return  false;//发件箱未读          }
        }else if("3".equals(shortType)){//已读邮件
          if ("0".equals(readFlag) || "1".equals(deleteFlag)) {
            return false;// 发件箱未读          }
        }
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  /**
   * 得到所有符合条件的bodyId 只能取到收件箱和已删除邮件箱，自定义邮箱的bodyId
   * 
   * @param conn
   * @param filters
   *          T0代表EMAIL 表 、T1代表EMAIL_BODY 表
   * 
   * @return
   * @throws Exception
   */
  public String getMailBodyIdFout(Connection conn, String filters)
      throws Exception {
    String result = "";
    String sql = "select T1.SEQ_ID from oa_email T0,oa_email_body T1 ";
    if (filters != null && !"".equals(filters)) {
      sql += " where " + filters;
    }
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    // select COUNT(*) from EMAIL where TO_ID = '196' AND DELETE_FLAG IN(0, 2)
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      int i = 0;
      while (rs.next()) {
        i++;
        int seqId = rs.getInt(1);
        if (!"".equals(result) && i < 200) {
          result += ",";
        }
        if (i == 200) {
          result += "*";
          i = 0;
        }
        result += seqId;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }
  /**
   * 得到所有符合条件的bodyId 只能取到发件箱
   * 
   * @param conn
   * @param filters
   *          T0代表EMAIL 表 、T1代表EMAIL_BODY 表
   * 
   * @return
   * @throws Exception
   */
  public String getMailBodyIdFout(Connection conn, String filters,String type,String shortType )
      throws Exception {
    String result = "";
    String sql = "select T1.SEQ_ID from oa_email T0,oa_email_body T1 ";
    if (filters != null && !"".equals(filters)) {
      sql += " where " + filters;
    }
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    // select COUNT(*) from EMAIL where TO_ID = '196' AND DELETE_FLAG IN(0, 2)
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      int i = 0;
      while (rs.next()) {
        int seqId = rs.getInt(1);
        if("3".equals(type) && isMore(conn, seqId) && !isMoreCanDel(conn, seqId, shortType)){
          continue;
        }
        i++;
        if (!"".equals(result) && i < 200) {
          result += ",";
        }
        if (i == 200) {
          result += "*";
          i = 0;
        }
        result += seqId;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }
  /**
   * 得到草稿箱的总数
   * 
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  public int getCountForOut(Connection conn, int userId) throws Exception {
    int result = -1;
    String sql = "select count(*) from oa_email_BODY WHERE FROM_ID = " + userId
        + " AND oa_email_body.SEQ_ID NOT IN ( SELECT BODY_ID FROM oa_email )";
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }

  /**
   * 扩展list添加数组
   * 
   * @param al
   * @param os
   * @return
   */
  public ArrayList addArray(ArrayList al, Object[] os) {
    for (Object obj : os) {
      if (obj == null || "".equals(obj.toString())) {
        continue;
      }
      al.add(obj);
    }
    return al;
  }

  /**
   * 得到email表的SEQ_ID
   * 
   * @param conn
   * @return
   * @throws SQLException
   */
  public int getBodyId(Connection conn) throws Exception {
    String sql = "select Max(SEQ_ID) FROM oa_email_body";
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        return rs.getInt(1);
      }
      return 0;
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
  }

  /**
   * 得到email表的SEQ_ID
   * 
   * @param conn
   * @return
   * @throws SQLException
   */
  public int getBodyId(Connection conn, String table) throws Exception {
    String sql = "select Max(SEQ_ID) FROM " + table;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        return rs.getInt(1);
      }
      return 0;
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
  }

  /**
   * 标记标记位
   * 
   * @param conn
   * @param field
   * @param value
   * @param eBodyId
   * @param tableName
   * @throws SQLException
   */
  public void updateFlag(Connection conn, String field, String value,
      String eBodyId, String tableName, int userId) throws Exception {
    String sql = "UPDATE " + tableName + " SET " + field + " = '" + value + "'"
        + " WHERE BODY_ID IN (" + eBodyId + ") AND TO_ID = '" + userId + "'";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    try {
      pstmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }

  }

  /**
   * 标记标记位
   * 
   * @param conn
   * @param field
   * @param value
   * @param emailId
   * @param tableName
   * @throws SQLException
   */
  public void updateFlag(Connection conn, String field, String value,
      String emailId, String tableName) throws Exception {
    String sql = "UPDATE " + tableName + " SET " + field + " = '" + value + "'"
        + " WHERE SEQ_ID IN (" + emailId + ")";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    try {
      pstmt.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }

  /**
   * 标记标记位
   * 
   * @param conn
   * @param field
   * @param value
   * @param emailId
   * @param tableName
   * @throws SQLException
   */
  public void updateFlag(Connection conn, List<String> sqls) throws Exception {

    Statement pstmt = null;
    try {
      pstmt = conn.createStatement();
      int i = 0;
      String sql = "";
      for (; i < sqls.size(); i++) {
        sql = sqls.get(i);
        // System.out.println(sql);
        pstmt.addBatch(sql);

        // System.out.println("1");
        // System.out.println("2");
        // conn.commit();
        // }
      }
      pstmt.executeBatch();
      // String sql = "update SD set ID=332";
      // pstmt.executeUpdate(sql);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }

  /**
   * 标记标记位
   * 
   * @param field
   * @param value
   * @param emailId
   * @param tableName
   * @throws SQLException
   */
  public String updateFlagStr(String field, String value, String emailId,
      String tableName) throws Exception {
    String sql = "UPDATE " + tableName + " SET " + field + " = '" + value + "'"
        + " WHERE SEQ_ID IN (" + emailId + ")";
    return sql;
  }

  /**
   * 标记标记位
   * 
   * @param field
   * @param value
   * @param emailId
   * @param tableName
   * @throws SQLException
   */
  public String checkDel(Connection conn, List<Integer> ids) throws Exception {
    String result = "";
    for (Integer id : ids) {
      String sql = "select " + " count(SEQ_ID) " + " from " + " oa_email "
          + " where " + " BODY_ID =" + id;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        if (rs.next()) {
          int count = rs.getInt(1);
          if (count == 0) {
            if (!"".equals(result)) {
              result += ",";
            }
            result += id;
          }
        }
      } catch (Exception e) {
        throw e;
      } finally {
        YHDBUtility.close(ps, rs, null);
      }
    }
    return result;
  }

  /**
   * 查看邮件是否处于可删除状态
   * 
   * 1收件人，2发件人，3永久删除 普通标记为为
   * 1000,1001,1010,1011,1000,1001,1010,1011(1是否读，2是否永久删除，3发件人是否删除，4收件人是否删除)
   * 永久删除1100 ,1101,1110
   * 
   * @param conn
   * @param seqId
   *          {seqId,seqId,seqId...}
   * @param deletFlag
   * @param readFlag
   * @throws SQLException
   */
  public boolean isCanDeleteMail(Connection conn, String isFrom, String seqId,
      String deletFlag, String readFlag) throws Exception {
    int deletbit = Integer.valueOf(deletFlag.trim());
    int readbit = Integer.valueOf(readFlag.trim());
    boolean bool = false;
    // 1.收件人删除01/11
    if ("1".equals(isFrom)) {
      if (!((4 ^ deletbit) == 0))
        deletbit = deletbit | 1;
    } else if ("2".equals(isFrom)) {
      // 2.发件人删除10/11
      deletbit = deletbit | 2;
    } else if ("3".equals(isFrom)) {
      deletbit = 5;
    }
    readbit = (readbit * 4) | deletbit;
    // System.out.println("TAG:阅读标记,Flag:" + readbit);
    if ((2 ^ readbit) == 0 || (3 ^ readbit) == 0 || (7 ^ readbit) == 0
        || (4 ^ deletbit) == 0 || (5 ^ deletbit) == 0) {
      // 永久删除
      if ((7 ^ readbit) == 0) {
        bool = false;
        updateFlag(conn, "DELETE_FLAG", 4 + "", seqId, "oa_email");
      } else {
        bool = true;
        updateFlag(conn, "DELETE_FLAG", deletbit + "", seqId, "oa_email");
      }
    } else {
      bool = false;
      updateFlag(conn, "DELETE_FLAG", deletbit + "", seqId, "oa_email");
    }
    return bool;
  }

  /**
   * 查找所有标记
   * 
   * @param conn
   * @param fields
   * @param filters
   * @throws SQLException
   */
  public ResultSet findFlag(Connection conn, String tableName, String[] fields,
      String[] filters) throws SQLException {
    String fieldNames = "";
    String filter = "";
    if (fields != null) {
      for (String field : fields) {
        if (!"".equals(fieldNames)) {
          fieldNames += ",";
        }
        fieldNames += field;
      }
    }
    if (filters != null) {
      for (String f : filters) {
        if (!"".equals(filter)) {
          filter += " AND ";
        }
        filter += f;
      }
    }
    String sql = "SELECT " + fieldNames + " FROM " + tableName;
    if (!"".equals(filter)) {
      sql += " WHERE " + filter;
    }
    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      int seqId = rs.getInt("SEQ_ID");
      String deleteFlag = rs.getString("DELETE_FLAG");
      String readFlag = rs.getString("READ_FLAG");
      String toId = rs.getString("TO_ID");
    }
    return rs;
  }

  /**
   * 产生一个随机数
   * 
   * @return
   * @throws NoSuchAlgorithmException
   */
  public String getRandom() throws NoSuchAlgorithmException {
    String result = "";
    // Random rand = new Random();
    // result = rand.nextLong();
    // System.out.println(result);
    result = YHGuid.getRawGuid();
    return result;
  }

  /**
   * 判断 文件是否存在
   * 
   * @param savePath
   * @param fileExtName
   * @return
   * @throws IOException
   */
  public boolean getExist(String savePath, String fileExtName)
      throws IOException {
    String filePath = savePath + File.separator + fileExtName;
    if (new File(filePath).exists()) {
      return true;
    }
    return false;
  }

  /**
   * 
   * @param dbConn
   * @param seqId
   * @param attachId
   * @param attachName
   * @param subject
   * @param filePath
   * @param folderPath
   * @return
   * @throws Exception
   */
  public String[] transferFolder(String attachId,
      String attachName,  String filePath, String folderPath,String[] attachInfo)
      throws Exception {

    if(attachInfo == null){
      attachInfo = new String[2];
    }
    boolean flag = false;
    String fileFolder = filePath + File.separator
        + this.getAttFolderName(attachId);
    String fileName = this.getOldAttaId(attachId) + "_" + attachName;
    String oldFileName = this.getOldAttaId(attachId) + "." + attachName;
    Date date = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyMM");
    String currDate = format.format(date);
    YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();
    String rand = emut.getRandom();
    String newAttaName = rand + "_" + attachName;
    String newAttaId = currDate + "_" + rand ;
    try {
      File file = new File(fileFolder + "/" + fileName);
      File oldFile = new File(fileFolder + "/" + oldFileName);
      if (file.exists()) {
        YHFileUtility.copyFile(file.getAbsolutePath(), folderPath
            + File.separator + currDate + File.separator + newAttaName);
      } else if (oldFile.exists()) {
        YHFileUtility.copyFile(oldFile.getAbsolutePath(), folderPath
            + File.separator + currDate + File.separator + newAttaName);
      }
    } catch (Exception e) {
      throw e;
    }
    String attaId = attachInfo[0] == null ? "" : attachInfo[0];
    String attaName = attachInfo[1] == null ? "" : attachInfo[1];
    if(attaId != null && !"".equals(attaId)){
      attaId += ",";
    }
    attaId += newAttaId;
    if(attaName != null && !"".equals(attaName)){
      attaName += "*";
    }
    attaName += attachName;
    attachInfo[0] = attaId;
    attachInfo[1] = attaName;
    return attachInfo;

  }
  
  /**
   * 得到附件的Id 兼老数据

   * 
   * @param keyId
   * @return
   */
  public String getOldAttaId(String keyId) {
    String attaId = "";
    if (keyId != null && !"".equals(keyId)) {
      if (keyId.indexOf('_') != -1) {
        String[] ids = keyId.split("_");
        if (ids.length > 0) {
          attaId = ids[1];
        }
      } else {
        attaId = keyId;
      }
    }
    return attaId;
  }
  
  /**
   * 得到该文件的文件夹名 兼老数据

   * 
   * @param key
   * @return
   */
  public String getAttFolderName(String key) {
    String folder = "";
    if (key != null && !"".equals(key)) {

      if (key.indexOf('_') != -1) {
        String[] str = key.split("_");
        for (int i = 0; i < str.length; i++) {
          folder = str[0];
        }
      } else {
        folder = "all";
      }
    }
    return folder;
  }
  

  public int getCountTwo(Connection conn, String sql)
      throws Exception {
    int result = 0;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    // select COUNT(*) from EMAIL where TO_ID = '196' AND DELETE_FLAG IN(0, 2)
    // System.out.println(sql);
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      if (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }

  public int getCountTwo2(Connection conn, String sql)
      throws Exception {
    int result = 0;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    // select COUNT(*) from EMAIL where TO_ID = '196' AND DELETE_FLAG IN(0, 2)
    // System.out.println(sql);
    try {
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while (rs.next()) {
        result++;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
    return result;
  }

}
