package yh.core.funcs.mysqldb.logic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import yh.core.funcs.mysqldb.data.YHMySqlTabInfo;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;

public class YHMySqlDBLogic {
  private static final String[][] String = null;

  /**
   * 
   * @return
   * @throws Exception
   */
  public List<YHMySqlTabInfo> getTableInfo(Connection conn) throws Exception {
    ArrayList<YHMySqlTabInfo> tabInfo = new ArrayList<YHMySqlTabInfo>();
    String sql = "SHOW TABLE STATUS";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        YHMySqlTabInfo mytab = new YHMySqlTabInfo();
        long size = rs.getLong("Data_length");
        String dataLength = "";
        if (Math.floor(size / 1024 / 1024) > 0) {
          dataLength = Math.round(size / 1024 / 1024) + "M";
        } else if (Math.floor(size / 1024) > 0) {
          dataLength = Math.round(size / 1024) + "K";
        } else {
          dataLength = size + "B";
        }
        mytab.setName(rs.getString("Name"));
        mytab.setRows(rs.getInt("Rows"));
        mytab.setDataLength(dataLength);
        String createTime = YHUtility.getDateTimeStr(
            rs.getTimestamp("Create_time")).substring(0, 19);
        mytab.setCreateTime(createTime);
        mytab.setUpdateTime(YHUtility.getDateTimeStr(
            rs.getTimestamp("Update_time")).substring(0, 19));
        mytab.setEngine(YHUtility.null2Empty(rs.getString("Engine")).toLowerCase());
        tabInfo.add(mytab);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return tabInfo;
  }

  /**
   * 
   * @param conn
   * @param action
   * @param tables
   * @return
   * @throws Exception
   */
  public StringBuffer getActionMrsg(Connection conn, String action,
      String tables) throws Exception {
    StringBuffer result = new StringBuffer();
    StringBuffer field = new StringBuffer();
    String sql = action + " TABLE " + tables;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        StringBuffer tabInfo = new StringBuffer();
        String msgText = rs.getString("Msg_text");
        String msgTextTr = "";
        if ("Table is already up to date".equalsIgnoreCase(msgText)) {
          msgTextTr = "无需优化";
        }
        if ("ok".equalsIgnoreCase(msgText)) {
          msgTextTr = "成功";
        }
        if ("doesn't support repair".equalsIgnoreCase(msgText)) {
          msgTextTr = "<font color=red>无法修复</font>";
        }
        tabInfo.append("{tableName:\"").append(rs.getString("Table")).append(
            "\",").append("msgText:\"").append(msgTextTr).append("\",").append(
            "op:\"").append(rs.getString("Op")).append("\",").append(
            "msgType:\"").append(rs.getString("Msg_type")).append("\"}");

        if (!"".equals(field.toString())) {
          field.append(",");
        }
        field.append(tabInfo);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    result.append("[").append(field).append("]");
    return result;
  }

  public void updateLastTime(Connection conn) throws Exception{
    String sql = "update oa_office_task set LAST_EXEC = ?";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.setDate(1, YHUtility.parseSqlDate());
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, null, null);
    }
  }
  /**
   * 
   * @param conn
   * @return
   * @throws Exception
   */
  public StringBuffer getDataBaseNames(Connection conn) throws Exception {
    StringBuffer sb = new StringBuffer();
    StringBuffer field = new StringBuffer();
    String sql = "show databases ";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String dbName = rs.getString(1);
        if ("bus".equalsIgnoreCase(dbName) || "train".equalsIgnoreCase(dbName)
            || "information_schema".equalsIgnoreCase(dbName)
            || "mysql".equalsIgnoreCase(dbName)) {
          continue;
        }
        String dbname = "{dbName:\"" + dbName + "\"}";
        if (!"".equals(field.toString())) {
          field.append(",");
        }
        field.append(dbname);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return sb.append("[").append(field).append("]");
  }

  /**
   * @param conn
   * @param dbName
   * @return
   * @throws Exception
   */
  public StringBuffer backup(Connection conn, String dbNames, String backUpPath)
      throws Exception {
    StringBuffer sb = new StringBuffer();
    String dbDir = getDBDir(conn);
    String backUpDir = getBackUpDir(backUpPath);
    int backUpCount = 0;
    int backUpFileCount = 0;
    String[] dbNameArray = dbNames.split(",");
    lockDbData(conn);
    try {
      for (int i = 0; i < dbNameArray.length; i++) {
        String dbName = dbNameArray[i];
        if ("".equals(dbName)) {
          continue;
        }
        backUpCount++;
        String dataDir = dbDir + dbName;
        if (backUpDir != null && !backUpDir.endsWith(File.separator) && !backUpDir.endsWith("/") ) {
          backUpDir += File.separator;
        } 
        String dataBackUpDir = backUpDir  + dbName + getDateStr();
        File dataBackUpDirF = new File(dataBackUpDir);
        if (!dataBackUpDirF.exists()) {
          dataBackUpDirF.mkdirs();
        }
        File dir = new File(dataDir);
        String[] dataFile = dir.list();
        for (int j = 0; j < dataFile.length; j++) {
          String extName = dataFile[j].substring(dataFile[j].lastIndexOf("."),
              dataFile[j].length()).toLowerCase();
          if (!".frm".equals(extName) && !".myd".equals(extName)
              && !".myi".equals(extName)) {
            continue;
          }
          String pathname = dataBackUpDir + "/" + dataFile[j];
          String srcFile = dataDir + "/" + dataFile[j];
          YHFileUtility.copyFile(srcFile, pathname);
          backUpFileCount++;
        }
      }
    }catch(Exception ex){
      throw ex;
    } finally {
      unlockDbData(conn);
    }
    int msrgType = 1;
    String msrg = "共" + backUpCount + "个数据库（" + backUpFileCount
        + "个文件）备份成功，文件备份至“" + backUpDir.replace("\\", "/") + "”";
    String message = "{type:\"" + msrgType + "\",data:\"" + msrg + "\"}";
    sb.append(message);
    return sb;
  }

  public void backUpauto(Connection conn) throws Exception{
     String dbNames = getBackUpDataBaseName(conn);
     String backUpPath = YHSysProps.getProp("dataBaseBackupDir");
     if (YHUtility.isNullorEmpty(backUpPath)) {
       return ;
     }
     if (!backUpPath.endsWith(File.separator) && !backUpPath.endsWith("/")) {
       backUpPath += File.separator;
     } 
     backup(conn, dbNames, backUpPath);
  }
  /**
   * 
   * @param conn
   * @return
   * @throws Exception
   */
  public String getDBDir(Connection conn) throws Exception {
    String dbDir = "";
    String sql = "SHOW VARIABLES";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        if (rs.getString("Variable_name").equalsIgnoreCase("datadir")) {
          dbDir = rs.getString("Value");
          break;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    if ("".equals(dbDir)) {
      // 设定一个地址
    }
    return dbDir;
  }

  /**
   * 
   * @param backUpPath
   * @return
   */
  public String getBackUpDir(String backUpPath) {
    String result = "";
    if (backUpPath != null && !"".equals(backUpPath)) {
      result = backUpPath;
    }
    return result;
  }

  /**
   * 
   * @param conn
   * @throws Exception
   */
  public void lockDbData(Connection conn) throws Exception {
    String sql = "FLUSH TABLES WITH READ LOCK ";
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      stmt.execute(sql);
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, null);
    }
  }

  /**
   * 
   * @param conn
   * @throws Exception
   */
  public void unlockDbData(Connection conn) throws Exception {
    String sql = "UNLOCK TABLES ";
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      stmt.execute(sql);
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, null);
    }
  }

  /**
   * 
   * @return
   */
  public String getDateStr() {
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    return format.format(new Date());
  }

  /**
   * 取得定时任务信息
   * 
   * @param conn
   * @return
   * @throws Exception
   */
  public StringBuffer getBackUpTask(Connection conn) throws Exception {
    StringBuffer result = new StringBuffer();
    String sql = "select " + " SEQ_ID" + ",TASK_TYPE" + ",`INTERVAL`"
        + ",EXEC_TIME" + ",LAST_EXEC" + ",TASK_NAME" + ",TASK_DESC"
        + ",USE_FLAG " + " from oa_office_task where TASK_CODE = ?";
    String sql2 = "insert into oa_office_task(TASK_TYPE,TASK_DESC,TASK_NAME,TASK_CODE,USE_FLAG) values(?,?,?,?,?)";
    PreparedStatement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String desc = "";
      String name = "";
      String taskType = "1";
      String taskUseFlag = "0";
      String backUpDataBaseName = "";
      int seqId = 0;
      int interval = 0;
      String execTime = "";
      Date lastTime = null;
      stmt = conn.prepareStatement(sql);
      stmt.setString(1, "db_backup");
      rs = stmt.executeQuery();
      if (rs.next()) {
        seqId = rs.getInt(1);
        taskType = rs.getString(2);
        interval = rs.getInt(3);
        execTime = rs.getString(4);
        lastTime = rs.getDate(5);
        name = rs.getString(6);
        desc = rs.getString(7);
        taskUseFlag = rs.getString(8);
      } else {
        desc = "根据设定的间隔天数和备份时间，定时热备份数据库";
        name = "数据库定时备份";
        ps = conn.prepareStatement(sql2);
        ps.setString(1, "1");
        ps.setString(2, desc);
        ps.setString(3, name);
        ps.setString(4, "db_backup");
        ps.setString(5, "0");
        ps.executeUpdate();
        seqId = getSeqId(conn);
      }
      backUpDataBaseName = getBackUpDataBaseName(conn);
      
      String lastTimeStr = lastTime == null ? "" : YHUtility
          .getDateTimeStr(lastTime);
      if(lastTimeStr.length() >= 10){
        lastTimeStr = (String) lastTimeStr.subSequence(0, 10);
      }
      execTime =  execTime == null ? "" : execTime;
      result.append("{").append("seqId:").append(seqId).append(",").append(
          "taskDesc:\"").append(YHUtility.encodeSpecial(desc)).append("\",")
          .append("taskType:\"").append(YHUtility.encodeSpecial(taskType))
          .append("\",").append("taskUse:\"").append(
              YHUtility.encodeSpecial(taskUseFlag)).append("\",").append(
              "taskName:\"").append(YHUtility.encodeSpecial(name))
          .append("\",").append("backUpDataBaseName:\"").append(
              YHUtility.encodeSpecial(backUpDataBaseName)).append("\",")
          .append("interval:").append(interval).append(",").append(
              "execTime:\"").append(execTime).append("\",").append(
              "lastTime:\"").append(lastTimeStr).append("\"").append("}");
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
      YHDBUtility.close(ps, null, null);
    }
    return result;
  }

  /**
   * 
   * @param conn
   * @return
   * @throws Exception
   */
  public int getSeqId(Connection conn) throws Exception {
    String sql = "select seq_id from oa_office_task where TASK_CODE='db_backup'";
    int result = 0;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  /**
   * 
   * @param conn
   * @return
   * @throws Exception
   */
  public String getBackUpDataBaseName(Connection conn) throws Exception {
    String sql = "select PARA_VALUE from SYS_PARA  where PARA_NAME = 'BACKUP_DATABASES'";
    String result = "yh";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }

  /**
   * 
   * @param conn
   * @param seqId
   * @throws Exception
   */
  public void updateOfficeTask(Connection conn, int seqId, Map request)
      throws Exception {
    int interval = Integer.valueOf(((String[]) request.get("interval"))[0]);
    String execTimestr = ((String[]) request.get("execTime"))[0];
    String lastTimestr = ((String[]) request.get("lastTime"))[0];
    String[] backupDb = (String[]) request.get("dbName");
    String backupDbs = "";
    for (int i = 0; backupDb != null && i < backupDb.length; i++) {
      if (!"".equals(backupDbs)) {
        backupDbs += ",";
      }
      backupDbs += backupDb[i];
    }
    java.sql.Date lastTime = YHUtility.parseSqlDate(lastTimestr);
    String useFlag = ((String[]) request.get("taskUse"))[0];
    String sql = "update oa_office_task set `INTERVAL` = ?,EXEC_TIME=?,LAST_EXEC=?,USE_FLAG=? where SEQ_ID = "
        + seqId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, interval);
      ps.setString(2, execTimestr);
      ps.setDate(3, lastTime);
      ps.setString(4, useFlag);
      ps.executeUpdate();
      updateSysPara(conn, backupDbs);
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }

  /**
   * 
   * @param conn
   * @param backupDbs
   * @throws Exception
   */
  public void updateSysPara(Connection conn, String backupDbs) throws Exception {
    if (backupDbs == null) {
      backupDbs = "";
    }
    String sql = "update SYS_PARA set PARA_VALUE = '" + backupDbs
        + "' where PARA_NAME = 'BACKUP_DATABASES'";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, null, null);
    }
  }

  /**
   * 
   * @param conn
   * @param tables
   * @return
   * @throws Exception
   */
  public void exportMysqlTable(Connection conn, String tables,ZipOutputStream baos)
      throws Exception {
    ArrayList<String> tableList = new ArrayList<String>();
    String[] tableArray = tables.split(",");
    for (int i = 0; i < tableArray.length; i++) {
      tableList.add(tableArray[i]);
    }
    String sch = YHSysProps.getSysDbName();
    YHExpDataMySql.expData(conn, tableList, sch,baos);
  }

  /**
   * 
   * @param sqlFile
   * @return
   * @throws Exception
   */
  public ArrayList<String> importSql(Connection conn, InputStream sqlFileIn)
      throws Exception {
    ArrayList<String> warring = new ArrayList<String>();
    int count = 0;
    try {
      StringBuffer sqlSb = new StringBuffer();
      byte[] buff = new byte[1024];
      int byteRead = 0;
      while ((byteRead = sqlFileIn.read(buff)) != -1) {
        sqlSb.append(new String(buff, 0, byteRead,YHConst.DEFAULT_CODE));
      } // Windows 下换行是 \r\n, Linux 下是 \n
      ArrayList<String> sqlArr = splitSql(sqlSb.toString().trim(), ';');
      for (int i = 0; i < sqlArr.size(); i++) {
        String sql = sqlArr.get(i).replaceAll("\\r\\n",
            "").trim();
        if (!sql.equals("")) {
          try {
            execSql(conn, sql);
            String tabName = "";
            String ps1 = "CREATE TABLE `([a-z0-9_]+)`";
            Pattern pattern1 = Pattern.compile(ps1);
            Matcher matcher = pattern1.matcher(sql.toUpperCase());
            if (matcher.find()) {
              tabName = matcher.group(0);
            }
            if (!"".equals(tabName)) {
              warring.add("成功建立表格 " + tabName);
            }
          } catch (Exception e) {
            count++;
            warring.add("数据脚本导入出现错误\r\n错误原因：" + e.getMessage()
                + "<br><br>错误语句：" + sql);
          }
        }
      }
      String mrs = "数据库脚本导入完成!";
      if (count == 0) {
        mrs += "<br>全部脚本导入成功！";
      } else {
        mrs += "<br>共发生" + count + "次错误！";
      }
      warring.add(mrs);
    } catch (Exception ex) {
      throw ex;
    }
    return warring;
  }

  /**
   * 
   * @param conn
   * @param sql
   * @return
   * @throws Exception
   */
  public void execSql(Connection conn, String sql) throws Exception {
    Statement ps = null;
    try {
      ps = conn.createStatement();
      ps.executeUpdate(sql);
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, null, null);
    }
  }
/**
 * 
 * @param sql
 * @param par
 * @return
 */
  public ArrayList<String> splitSql(String sql, char par) {
    boolean inString = false;
    boolean escaped_backslash = false;
    char lastChar = ' ';
    char string_start = ' ';
    ArrayList<String> ret = new ArrayList<String>();
    for (int i = 0; i < sql.trim().length(); i++) {
      char c = sql.charAt(i);
      if (c == par && !inString) {
        ret.add(sql.substring(0, i));
        sql = sql.substring(i + 1).trim();
        i = 0;
        lastChar = ' ';
      }
      if (inString) {
        if (c == '\\') {
          if (lastChar != '\\') {
            escaped_backslash = false;
          } else {
            escaped_backslash = !escaped_backslash;
          }
        }
        if ((c == string_start)
            && (c == '`' || !((lastChar == '\\') && !escaped_backslash))) {
          inString = false;
          string_start = ' ';
        }
      } else {
        if ((c == '"') || (c == '\'') || (c == '`')) {
          inString = true;
          string_start = c;
        }
      }
      lastChar = c;
    }
    if (!"".equals(sql.trim())) {
      ret.add(sql);
    }
    return ret;
  }
/**
 * 
 * @param conn
 * @throws Exception
 */
  public void clearOnLineUser(Connection conn) throws Exception{
    Date now = new Date();
    long time = now.getTime();
    String onlineRefSec = YHSysProps.getProp("$ONLINE_REF_SEC");
    if (!YHUtility.isInteger(onlineRefSec)) {
      onlineRefSec = "120"; 
    }
    long filterTime = time - (Integer.valueOf(onlineRefSec) - 5)*1000;
    
    String dateFilter = YHDBUtility.getDateFilter("LOGIN_TIME", YHUtility.getDateTimeStr(new Date(filterTime)), "<");
    String sql = "delete from oa_online where " + dateFilter;
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, null, null);
    }
  }
  /**
   * 
   * @param conn
   * @throws Exception
   */
    public void clearOnLineUser(Connection conn,String userIds) throws Exception{
      String sql = "delete from oa_online where user_id in(" + userIds + ")";
      Statement stmt = null;
      try {
        stmt = conn.createStatement();
        stmt.executeUpdate(sql);
      } catch (Exception e) {
        throw e;
      } finally {
        YHDBUtility.close(stmt, null, null);
      }
    }
  /**
   * 
   * @return
   */
  public boolean checkDb(){
    String dbName = YHSysProps.getProp("db.jdbc.dbms");
    if("mysql".equalsIgnoreCase(dbName)){
      return true;
    }else{
      return false;
    }
  }
}
