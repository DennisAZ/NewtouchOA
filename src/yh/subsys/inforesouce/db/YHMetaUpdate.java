package yh.subsys.inforesouce.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.subsys.inforesouce.data.YHSignFile;

public class YHMetaUpdate {
  private static Logger log = Logger.getLogger(YHMetaUpdate.class); 
  /**
   * 更新元数据，元数据不存在，则插入，存在则更新
   * 要求该文件已经在文件中心中已经存在，否则不更新
   * @param dbConn
   * @param guid           
   * @param dataMap
   * @throws Exception
   */
  public void updateMetadata(Connection dbConn,
      String guid, Map<String, String> dataMap) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "select SEQ_ID from oa_seal_attach"
        + " where FILE_ID='" + guid + "'";
      int seqId = 0;
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        seqId = rs.getInt(1);
      }
      if (seqId < 1) {
        return;
      }
      List<Map> mapList = splitRawData(dataMap);
      Map dataMap1 = mapList.get(0);
      Map dataMap2 = mapList.get(1);
      
      doUpdateAttr(stmt, "oa_file_attrs01", seqId, dataMap1);
      doUpdateAttr(stmt, "oa_file_attrs02", seqId, dataMap2);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 删除文件注册
   * @param dbConn
   * @param guid
   * @throws Exception
   */
  public void deleteMetaData(Connection dbConn,
      String guid) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "select SEQ_ID from oa_seal_attach"
        + " where FILE_ID='" + guid + "'";
      int seqId = 0;
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        seqId = rs.getInt(1);
      }
      if (seqId < 1) {
        return;
      }
      sql = "delete from oa_seal_attach where SEQ_ID=" + seqId;
      stmt.executeUpdate(sql);
      sql = "delete from oa_file_attrs01 where FILE_SEQ_ID=" + seqId;
      stmt.executeUpdate(sql);
      sql = "delete from oa_file_attrs02 where FILE_SEQ_ID=" + seqId;
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 查找文件列表
   */
  public List searchFileList(Connection dbConn,
      List<String> moduleList,
      Map metaFilters,
      String idFilter) throws Exception {

    List rtList = new ArrayList();
    
    Statement stmt = null;
    PreparedStatement preStmt1 = null;
    PreparedStatement preStmt2 = null;
    PreparedStatement preStmtMain = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      StringBuffer filterBuf = new StringBuffer();
      
      if (!YHUtility.isNullorEmpty(idFilter)) {
        idFilter = idFilter.trim();
        if (idFilter.startsWith(",")) {
          idFilter = idFilter.substring(1);
        }
        if (idFilter.endsWith(",")) {
          idFilter = idFilter.substring(0, idFilter.length() - 1);
        }
        idFilter = idFilter.trim();
        if (!YHUtility.isNullorEmpty(idFilter)) {
          filterBuf.append("FILE_PATH in (" + idFilter + ") and ");
        }
      }
      
      StringBuffer moduleFilter = new StringBuffer();
      for (int i = 0; i < moduleList.size(); i++) {
        String module = moduleList.get(i);
        moduleFilter.append(module);
        moduleFilter.append(",");
      }
      if (moduleFilter.length() > 0) {
        moduleFilter.delete(moduleFilter.length() - 1, moduleFilter.length());
        filterBuf.append("MODULE_NO in (");
        filterBuf.append(moduleFilter);
        filterBuf.append(") and ");
      }
      if (filterBuf.length() > 5) {
        filterBuf.delete(filterBuf.length() - 5, filterBuf.length());
      }
      String sql = "select SEQ_ID from oa_seal_attach";
      if (filterBuf.length() > 0) {
        sql = sql + " where " + filterBuf;
      }
      sql = sql + " order by UPATE_TIME DESC";
      rs = stmt.executeQuery(sql);
      List<Integer> idList = new ArrayList<Integer>();
      int cnt = 0;
      while (rs.next()) {
        idList.add(new Integer(rs.getInt(1)));
        cnt++;
        //最多取前200条
        if (cnt >= 200) {
          break;
        }
      }
      if (idList.size() < 1) {
        return rtList;
      }
      
      preStmtMain = createPreStmtMain(dbConn);
      preStmt1 = createPreStmtMeta(dbConn, "oa_file_attrs01", 1);
      preStmt2 = createPreStmtMeta(dbConn, "oa_file_attrs02", 100);
      for (int i = 0; i < idList.size(); i++) {
        int seqId = idList.get(i).intValue();
        Map fileMetas = loadMetaData(preStmt1, preStmt2, seqId);
        if (fileMetas.size() < 1) {
          continue;
        }
        YHSignFile file = loadMainData(preStmtMain, seqId);
        file.putAttr(fileMetas);
        rtList.add(file);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
      YHDBUtility.close(preStmt1, rs, log);
      YHDBUtility.close(preStmt2, rs, log);
      YHDBUtility.close(preStmtMain, rs, log);
    }
    
    return rtList;
  }
  
  /**
   * 构造查询文件主表数据的语句
   * @param dbConn
   * @return
   * @throws Exception
   */
  private PreparedStatement createPreStmtMain(Connection dbConn) throws Exception {
    String sql = "select SEQ_ID, MODULE_NO, RECORD_ID, FILE_ID, FILE_PATH"
      + ", CREATE_TIME, UPDATE_TIME, SING_FLAG, LAST_SIGN_TIME, SIGN_TYPE, FULL_TEXT_FLAG, FULL_TEXT_TIME"
      + " from oa_seal_attach where SEQ_ID=?";
    return dbConn.prepareStatement(sql);
  }
  
  /**
   * 构造查询元数据语句
   * @param dbConn
   * @param tableName
   * @param startIndex           1=FILE_ATTRS01;      100=FILE_ATTRS02
   * @return
   */
  private PreparedStatement createPreStmtMeta(Connection dbConn,
      String tableName,
      int startIndex) throws Exception {
    
    StringBuffer fieldBuf = new StringBuffer();
    for (int i = 0; i < 100; i++) {
      fieldBuf.append("ATTR_" + YHUtility.getFixLengthStringFront(String.valueOf(i + startIndex), 3));
      fieldBuf.append(",");
    }
    fieldBuf.delete(fieldBuf.length() - 1, fieldBuf.length());
    String sql = "select " + fieldBuf + " from " + tableName
      + " where FILE_SEQ_ID=?";
    
    return dbConn.prepareStatement(sql);
  }
  
  /**
   * 加载主表数据
   * @param stmt
   * @param seqId
   * @return
   * @throws Exception
   */
  private YHSignFile loadMainData(PreparedStatement stmt, int seqId) throws Exception {
    YHSignFile rtFile = new YHSignFile();
    ResultSet rs = null;
    try {
      stmt.clearParameters();
      stmt.setInt(1, seqId);
      if (rs.next()) {
        int fieldCnt = 0;
        rtFile.setSeqId(rs.getInt(++fieldCnt));
        rtFile.setModuleNo(rs.getString(++fieldCnt));
        rtFile.setRecordId(rs.getString(++fieldCnt));
        rtFile.setFileId(rs.getString(++fieldCnt));
        rtFile.setFilePath(rs.getString(++fieldCnt));
        rtFile.setCreateTime(rs.getTimestamp(++fieldCnt));
        rtFile.setUpdateTime(rs.getTimestamp(++fieldCnt));
        rtFile.setSignFlag(rs.getString(++fieldCnt));
        rtFile.setLastSignTime(rs.getTimestamp(++fieldCnt));
        rtFile.setSignType(rs.getString(++fieldCnt));        
        rtFile.setFullTextFlag(rs.getString(++fieldCnt));
        rtFile.setFullTextTime(rs.getTimestamp(++fieldCnt));
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(null, rs, log);
    }
    
    return rtFile;
  }
  
  /**
   * 提取文件元数据
   */
  private Map loadMetaData(PreparedStatement stmt1,
      PreparedStatement stmt2,
      int seqId) throws Exception {
    Map rtMap = new HashMap();
    ResultSet rs = null;
    try {
      stmt1.clearParameters();
      stmt1.setInt(1, seqId);
      rs = stmt1.executeQuery();
      if (rs.next()) {
        for (int i = 0; i < 100; i++) {
          String value = rs.getString(i + 1);
          if (value == null) {
            continue;
          }
          value = value.trim();
          if (value.length() < 1) {
            continue;
          }
          rtMap.put("ATTR_" + YHUtility.getFixLengthStringFront(String.valueOf(i + 1), 3), value);
        }
      }
      stmt2.clearParameters();
      stmt2.setInt(1, seqId);
      rs = stmt2.executeQuery();
      if (rs.next()) {
        for (int i = 0; i < 100; i++) {
          String value = rs.getString(i + 1);
          if (value == null) {
            continue;
          }
          value = value.trim();
          if (value.length() < 1) {
            continue;
          }
          rtMap.put("ATTR_" + YHUtility.getFixLengthStringFront(String.valueOf(i + 100), 3), value);
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(null, rs, log);
    }
    return rtMap;
  }
  
  /**
   * 更新属性表
   * @param stmt
   * @param tableName
   * @param dataMap
   * @throws Exception
   */
  private void doUpdateAttr(Statement stmt, String tableName, int fileSeqId, Map<String, String> dataMap) throws Exception {
    if (fileSeqId < 1 || dataMap == null || dataMap.size() < 1) {
      return;
    }
    ResultSet rs = null;
    try {
      int recordCnt = 0;
      String sql = "select count(SEQ_ID) from " + tableName
        + " where FILE_SEQ_ID=" + fileSeqId;      
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        recordCnt = rs.getInt(1);
      }
      //已经存在该文件的元数据
      if (recordCnt < 1) {
        StringBuffer fieldBuf = new StringBuffer();
        StringBuffer valueBuf = new StringBuffer();
        Iterator<String> iKeys = dataMap.keySet().iterator();
        while (iKeys.hasNext()) {
          String fieldName = iKeys.next();
          String value = dataMap.get(fieldName);
          fieldBuf.append(fieldName);
          fieldBuf.append(",");
          valueBuf.append("'");
          valueBuf.append(value);
          valueBuf.append("',");
        }
        if (fieldBuf.length() > 0) {
          fieldBuf.delete(fieldBuf.length() - 1, fieldBuf.length());
        }
        if (valueBuf.length() > 0) {
          valueBuf.delete(valueBuf.length() - 1, valueBuf.length());
        }
        sql = "insert into " + tableName
          + "(" + fieldBuf + ")"
          + " values (" + valueBuf + ")";
        stmt.executeUpdate(sql);
      //该文件的元数据还不存在
      }else {
        StringBuffer setBuf = new StringBuffer();
        Iterator<String> iKeys = dataMap.keySet().iterator();
        while (iKeys.hasNext()) {
          String fieldName = iKeys.next();
          String value = dataMap.get(fieldName);
          setBuf.append(fieldName);
          setBuf.append("=");
          setBuf.append("'");
          setBuf.append(value);
          setBuf.append("',");
        }
        if (setBuf.length() > 0) {
          setBuf.delete(setBuf.length() - 1, setBuf.length());
        }
        sql = "update " + tableName
          + " set " + setBuf
          + " where FILE_SEQ_ID=" + fileSeqId;
        stmt.executeUpdate(sql);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(null, rs, log);
    }
  }

  /**
   * 把原始的数据进行分离，分离成两个表需要的数据，同时将编码代表的Key转换成字段名称
   * @param dataMap
   * @return
   */
  private List<Map> splitRawData(Map<String, String> dataMap) {
    List<Map> rtList = new ArrayList<Map>();
    Map map1 = new HashMap();
    Map map2 = new HashMap();
    rtList.add(map1);
    rtList.add(map2);
    
    Iterator<String> iKeys = dataMap.keySet().iterator();
    while(iKeys.hasNext()) {
      String key = iKeys.next();
      String value = dataMap.get(key);
      if (key.startsWith("MEX")) {
        map2.put("ATTR_" + YHUtility.getFixLengthStringFront(key.substring(3), 3) , value);
      }else if (key.startsWith("M")) {
        map1.put("ATTR_" + YHUtility.getFixLengthStringFront(key.substring(1), 3), value);
      }
    }
    return rtList;
  }
}
