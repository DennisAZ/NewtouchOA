package yh.rad.dbexputil.transplant.logic.core.util.raw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.print.DocFlavor.STRING;

import org.apache.velocity.runtime.directive.Foreach;

import yh.core.data.YHDbRecord;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.util.db.YHDBUtility;
import yh.rad.dbexputil.transplant.logic.core.data.YHColumnInfo;
import yh.rad.dbexputil.transplant.logic.core.data.YHSpecialHandFun;
import yh.rad.dbexputil.transplant.logic.core.praser.YHDBExcute;
import yh.rad.dbexputil.transplant.logic.core.util.db.YHTransplantUtil;

public class YHOaTabMapping {
/**
 * 
 * @return
 */
  public static Map<String, String> getDiffTableNameMap(){
    HashMap<String, String> map = new HashMap<String, String>();
    
    map.put("tran1", "TRANSPLANT");
   
    return map;
  }
  /**
   * 
   * @param oldConn
   * @param oldSch
   * @param oldTab
   * @param newConn
   * @param newSch
   * @param newTab
   * @return
   * @throws Exception
   */
  public static Map<String, String> getColumns(Connection oldConn,String oldSch,String oldTab
      ,Connection newConn,String newSch,String newTab) throws Exception{
    HashMap<String, String> map = new HashMap<String, String>();
    ArrayList<String> oldColumns = YHDBExcute.getColumnNames(oldConn.getMetaData(), oldConn.getCatalog(), oldSch, oldTab, null);
    ArrayList<String> newColumns = YHDBExcute.getColumnNames(newConn.getMetaData(), newConn.getCatalog(), newSch, newTab, null);
    Iterator< String> itr = oldColumns.iterator();
    for (; itr.hasNext();) {
      String oldCloumn = (String) itr.next();
      Iterator< String> newitr = newColumns.iterator();
      for (; newitr.hasNext();) {
        String newColumn = (String) newitr.next();
        if(newColumn.equalsIgnoreCase(oldCloumn)){
          map.put(oldCloumn, newColumn);
          newitr.remove();
          itr.remove();
          break;
        }
      }
    }
 
    for (String newColumn : newColumns) {
      String key = "nullOld_" + newColumns.indexOf(newColumn) ;
      map.put(key, newColumn);
    }
    for (String oldColumn : oldColumns) {
      String key = "nullNew_" + oldColumns.indexOf(oldColumn) ;
      map.put(oldColumn, key);
    }
    return map;
  }
  /**
   * 
   * @param oldConn
   * @param oldSch
   * @param newConn
   * @param newSch
   * @return
   * @throws Exception
   */
  public static Map<String, String> getTableNames(Connection oldConn,String oldSch
      ,Connection newConn,String newSch) throws Exception{
    HashMap<String, String> map = new HashMap<String, String>();
    ArrayList<String> oldTable = YHDBExcute.getTableNames(oldConn, oldSch);
    ArrayList<String> newTable = YHDBExcute.getTableNames(newConn, newSch);
    Map<String, String> diffTab = getDiffTableNameMap();
    Iterator< String> itr = oldTable.iterator();
    for (; itr.hasNext();) {
      String oldCloumn = (String) itr.next();
      if(diffTab.get(oldCloumn) != null){
        map.put(oldCloumn, diffTab.get(oldCloumn));
        itr.remove();
        newTable.remove(diffTab.get(oldCloumn));
        continue;
      }
      Iterator< String> newitr = newTable.iterator();
      for (; newitr.hasNext();) {
        String newColumn = (String) newitr.next();
        if(newColumn.equalsIgnoreCase(oldCloumn)){
          map.put(oldCloumn, newColumn);
          newitr.remove();
          itr.remove();
          break;
        }
      }
    }
    for (String newColumn : newTable) {
      String key = "nullOld_" + newTable.indexOf(newColumn) ;
      map.put(key, newColumn);
    }
    for (String oldColumn : oldTable) {
      String key = "nullNew_" + oldTable.indexOf(oldColumn) ;
      map.put(oldColumn, key);
    }
    return map;
  }
 /**
  * 
  * @return
  */
  public static ArrayList<YHSpecialHandFun> getSpecialHandFunMapping(){
    ArrayList<YHSpecialHandFun> refers = new ArrayList<YHSpecialHandFun>();
    YHSpecialHandFun sphf = new YHSpecialHandFun();
    sphf.setColumnName("from_id");
    sphf.setTabName("oa_msg_body");
    HashMap<String, String > params = new HashMap<String, String>();
    params.put("dbType", "1");
    sphf.setParams(params);
    sphf.setRefersType("1");
    refers.add(sphf);
    return refers;
  }
  
  public static ArrayList<YHSpecialHandFun> getSpecialMappingForOa(String tableName,String path) throws Exception{
    System.out.println(tableName);
    ArrayList<YHSpecialHandFun> refers = new ArrayList<YHSpecialHandFun>();
    String cfgPath = path + "/" + tableName + ".xls"; 
    File file = new File(cfgPath);
    if(!file.exists()){
      return refers;
    }
    InputStream ins = new FileInputStream(file); 
    ArrayList<YHDbRecord> dbrs = YHJExcelUtil.readExc(ins, true);
    
    for (YHDbRecord record : dbrs) {
      YHSpecialHandFun sphf = new YHSpecialHandFun();
      String columnName = String.valueOf(record.getValueByName("column"));
      double value = (Double) record.getValueByName("type");
      
      String type = String.valueOf(value);
      
      sphf.setColumnName(columnName);
      sphf.setTabName(tableName);
      HashMap<String, String > params = new HashMap<String, String>();
      params.put("dbType", "1");
      sphf.setParams(params);
      sphf.setRefersType(type.substring(0, 1));
      refers.add(sphf);
    }
    System.out.println("tableName:" +  tableName + " >>>>" + refers);
    return refers;
  }
  /**
   * 
   * @param oldConn
   * @param oldSch
   * @param newConn
   * @param newSch
   * @return
   * @throws Exception
   */
  public static void getTableNames(Connection oldConn,String oldSch
      ,Connection newConn,String newSch ,String xlsPath) throws Exception{
    Map<String, String> map = getTableNames(oldConn, oldSch, newConn, newSch);
    ArrayList<YHDbRecord> records = new ArrayList<YHDbRecord>();
    xlsPath += "/tableMapping.xls";
    File file = new File(xlsPath);
    Set<String> oldTables = map.keySet();
    for (String oldTableName : oldTables) {
      YHDbRecord record = new YHDbRecord();
      String newTableName  = map.get(oldTableName);
      record.addField("oldTableName", oldTableName);
      record.addField("newTableName", newTableName);
      records.add(record);
    }
    OutputStream ops = new FileOutputStream(file);
    YHJExcelUtil.writeExc(ops, records);
  }
  /**
   * 
   * @param oldConn
   * @param oldSch
   * @param newConn
   * @param newSch
   * @return
   * @throws Exception
   */
  public static Map<String, String> getTableNames(String xlsPath) throws Exception{
    File file = new File(xlsPath);
    InputStream ins = new FileInputStream(file);
    ArrayList<YHDbRecord> tableNames = YHJExcelUtil.readExc(ins, true);
    HashMap<String,String> result = new HashMap<String, String>();
    for (YHDbRecord record : tableNames) {
      String newTableName = (String) record.getValueByName("newTableName");
      String oldTableName = (String) record.getValueByName("oldTableName");
      result.put(oldTableName, newTableName);
    }
    return result;
  }
  public static Map<String, String> getColumns(String xlsPath,String tableName) throws Exception{
    String filePath = xlsPath + "/" + tableName + ".xls";
    File file = new File(filePath);
    InputStream ins = new FileInputStream(file);
    ArrayList<YHDbRecord> columnNames = YHJExcelUtil.readExc(ins, true);
    HashMap<String,String> result = new HashMap<String, String>();
    for (YHDbRecord record : columnNames) {
      String oldColumnName = (String) record.getValueByName("oldColumnName");
      String newColumnName = (String) record.getValueByName("newColumnName");
      result.put(oldColumnName, newColumnName);
    }
    return result;
  }
  public static void getColumns(Connection oldConn,String oldSch
      ,Connection newConn,String newSch,String xlsPath,String createPath) throws Exception{
    File file = new File(xlsPath);
    InputStream ins = new FileInputStream(file);
    ArrayList<YHDbRecord> tableNames = YHJExcelUtil.readExc(ins, true);
    for (YHDbRecord tableName : tableNames) {
      String newName = tableName.getValueByName("newTableName").toString();
      String oldName = tableName.getValueByName("oldTableName").toString();
      Map<String, String> columns = getColumns(oldConn, oldSch, oldName, newConn, newSch, newName);
      ArrayList<YHDbRecord> records = new ArrayList<YHDbRecord>();
      Set<String> oldTables = columns.keySet();
      for (String oldTableName : oldTables) {
        YHDbRecord record = new YHDbRecord();
        String newTableName  = columns.get(oldTableName);
        record.addField("oldColumnName", oldTableName);
        record.addField("newColumnName", newTableName);
        records.add(record);
      }
      File f = new File(createPath);
      if(!f.exists()){
        f.mkdirs();
      }
      String truecreatePath = createPath + "/" + oldName + ".xls";
      File outFile = new File(truecreatePath);
      OutputStream ops = new FileOutputStream(outFile);
      YHJExcelUtil.writeExc(ops, records);
    }
  }
  
  public static void createFile(File file,String createPath) throws Exception{
    InputStream ins = new FileInputStream(file);
    ArrayList<YHDbRecord> tableNames = YHJExcelUtil.readExc(ins, false);
    for (YHDbRecord yhDbRecord : tableNames) {
      String tableName = (String)yhDbRecord.getValueByIndex(0);
      String Item = (String)yhDbRecord.getValueByIndex(1);
      File f = new File(createPath + "/" + tableName + ".xls");
      ArrayList<YHDbRecord> temps = new ArrayList<YHDbRecord>();
      YHDbRecord r = new YHDbRecord();
      r.addField("column", Item);
      temps.add(r);
      System.out.println(tableName);
      if(!f.exists()){
        System.out.println(tableName + " >.......... 1");
        OutputStream ops = new FileOutputStream(f);
        YHJExcelUtil.writeExc(ops, temps);
      }else{
        System.out.println(tableName + " >.......... 2");
        System.out.println(temps.size());
        OutputStream ops = new FileOutputStream(f,true);
        YHJExcelUtil.writeExc(ops, temps);
      }
    }
  }
  
  public static void spHtml(String[] params , int dbType) throws Exception{
    Connection conn = YHTransplantUtil.getDBConn2(true, dbType);
    PreparedStatement ps = null;
    PreparedStatement ps2 = null;
    ResultSet rs = null;
    for (String param : params) {
      String[] info = param.split(",");
      String tableName = info[0];
      String c1 = info[1];
      String c2 = info[2];
      try {
        String sql2 = "select T1.SEQ_ID , T1." + c2 + " from " + tableName + " T1";
        ps = conn.prepareStatement(sql2);
        rs = ps.executeQuery();
        System.out.println("正在对" + tableName + "表进行特殊处理...");
        int count = 0;
        try {
          while (rs.next()) {
           int seqId = rs.getInt(1);
           String temp = rs.getString(2);
           if(temp == null){
             continue;
           }
           temp = YHDiaryUtil.cutHtml(temp);
           String sql = "update " + tableName + " T1 set " + c1 + " =?  where T1.SEQ_ID = ?";
           
             ps2 = conn.prepareStatement(sql);
             ps2.setString(1, temp);
             ps2.setInt(2, seqId);
             ps2.addBatch();
             if(count%1000== 0){
               ps2.executeBatch();
               ps2.clearBatch();
               conn.commit();
               System.out.println("处理" + tableName + "表                                  " + count + " 条数据...");
             }
             count ++;
           }
          ps2.executeBatch();
          System.out.println("处理" + tableName + "表                                  " + count + " 条数据...");
        } catch (Exception e) {
          e.printStackTrace();
        }finally{
          YHDBUtility.close(ps2, null, null);
          conn.commit();
        }  
      } catch (Exception e) {
        throw e;
      } finally{
        YHDBUtility.close(ps, rs, null);
        conn.commit();
      }
    }
  }
}
