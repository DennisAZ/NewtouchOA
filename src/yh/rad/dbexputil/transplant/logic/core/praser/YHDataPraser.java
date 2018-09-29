package yh.rad.dbexputil.transplant.logic.core.praser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.util.db.YHDBUtility;
import yh.rad.dbexputil.transplant.logic.core.cfg.YHTableMappingCfg;
import yh.rad.dbexputil.transplant.logic.core.data.YHSpecialHandFun;
import yh.rad.dbexputil.transplant.logic.core.processor.YHProcI;
import yh.rad.dbexputil.transplant.logic.core.processor.YHProcessorFactory;
import yh.rad.dbexputil.transplant.logic.core.processor.YHSpecialHandler;
import yh.rad.dbexputil.transplant.logic.core.processor.YHSpecialHandlerFactory;
import yh.rad.dbexputil.transplant.logic.core.util.db.YHTransplantUtil;

/**
 * 生成data.xml文件 解析data.xml文件
 * 
 * @author Think
 * 
 */
public class YHDataPraser {
  /**
   * 生成数据文件
   */
  public void toDataXml(String dataCfgDir, String path) {

  }

  /**
   * 生成数据文件
   * 
   * @param dataCfgDir
   * @param path
   * @throws Exception
   */
  public void toDataXml(File inputXml, String path) throws Exception {
    Document document = YHTableMappingCfg.getDocument(inputXml);
    Element root = document.getRootElement();
    List<Element> tablesElement = root.elements("tables");
    String oldSch = root.attributeValue("old_name");
    String newSch = root.attributeValue("new_name");
    String newDbType = root.attributeValue("new_db_type");
    String oldDbType = root.attributeValue("old_db_type");
    ArrayList<Element> tables = null;
    Connection oldConn = YHTransplantUtil.getDBConn(false, Integer
        .valueOf(oldDbType));
    try {
      if (tablesElement.size() > 0) {
        tables = YHTableMappingCfg.getElementsByName(tablesElement.get(0),
            "table");
        for (int i = 0; i < tables.size(); i++) {
          Element table = tables.get(i);
          String oldName = table.attributeValue("old_name");
          String newName = table.attributeValue("new_name");
          String fkRefers = table.attributeValue("fkrefers");
          YHProcI newProc = YHProcessorFactory.getProcessorInstance(Integer
              .valueOf(newDbType));
          YHProcI oldProc = YHProcessorFactory.getProcessorInstance(Integer
              .valueOf(oldDbType));

          ArrayList<Element> columns = YHTableMappingCfg.getElementsByName(
              table, "column");
          String field = "";
          String newField = "";
          Map<String, Integer> newColumnInfo = new HashMap<String, Integer>();
          Map<String, String> newColumnNames = new HashMap<String, String>();
          ArrayList<YHSpecialHandFun> refers = new ArrayList<YHSpecialHandFun>();
          for (int j = 0; j < columns.size(); j++) {
            Element column = columns.get(j);
            String newColName = column.attributeValue("new_name");
            String newType = column.attributeValue("new_type");
            String oldColName = column.attributeValue("old_name");
            String oldType = column.attributeValue("old_type");
            ArrayList<Element> sphandlers = YHTableMappingCfg
                .getElementsByName(column, "sphandler");
            if (sphandlers.size() > 0) {
              Element sphandler = sphandlers.get(0);
              YHSpecialHandFun sphf = new YHSpecialHandFun();
              String refersType = sphandler.attributeValue("type");
              ArrayList<Element> paramsEl = YHTableMappingCfg
                  .getElementsByName(sphandler, "param");
              sphf.setColumnName(oldColName);
              sphf.setTabName(oldName);
              HashMap<String, String> params = new HashMap<String, String>();
              for (Element param : paramsEl) {
                String paraName = param.attributeValue("name");
                String paraValue = param.attributeValue("value");
                params.put(paraName, paraValue);
              }
              sphf.setParams(params);
              sphf.setRefersType(refersType);
              refers.add(sphf);
            }
            if (!"".equals(field)) {
              field += ",";
            }
            field += "`" + oldColName + "`";
            if (!"".equals(newField)) {
              newField += ",";
            }

            newField += "\"" + newColName + "\"";
            newColumnInfo.put(oldColName, Integer.valueOf(newType));
            newColumnNames.put(oldColName, newColName);
          }
          String query = " select " + field + " from " + oldName;
          createXml(oldConn, query, newColumnInfo, newProc, oldProc, newName,
              newSch, newDbType, newColumnNames, path, newField, refers);
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      oldConn.commit();
      YHDBUtility.closeDbConn(oldConn, null);
    }

  }

  /**
   * 
   * @param oldConn
   * @param sql
   * @param newColumnInfo
   * @param newProc
   * @param oldProc
   * @throws Exception
   */
  public void createXml(Connection oldConn, String sql,
      Map<String, Integer> newColumnInfo, YHProcI newProc, YHProcI oldProc,
      String newTable, String newSch, String newDbType,
      Map<String, String> newNames, String path, String field,
      ArrayList<YHSpecialHandFun> refers) throws Exception {
    System.out.println("开始生成  " + newTable + " 表的xml数据文件...");
    System.out.println("sql:" + sql);
    Document document = DocumentHelper.createDocument();

    Element tableElement = document.addElement("table");
    tableElement.addAttribute("name", newTable);
    tableElement.addAttribute("sechma", newSch);
    tableElement.addAttribute("database_type", newDbType);
    Element columnsElement = tableElement.addElement("columns");
    columnsElement.addAttribute("value", field);
    PreparedStatement ps = null;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    try {
      ps = oldConn.prepareStatement(sql);
      rs = ps.executeQuery();
      rsmd = rs.getMetaData();
      int count = 1;
      while (rs.next()) {
        try {
          Element recordElement = tableElement.addElement("record");
          for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            Element columnElement = recordElement.addElement("column");
            Object o = null;
            try {
              o = oldProc.sqlParam2JavaParam(rs, i, rsmd.getColumnType(i));
            } catch (Exception e) {
              o = null;
            }
            YHSpecialHandFun sphf = YHTableMappingCfg.isHasSpecial(refers, rsmd
                .getColumnName(i), rsmd.getTableName(i));
            if (refers != null && sphf != null) {
              // System.out.println("生成特殊处理字段信息： 字段名：" + rsmd.getColumnName(i) +
              // " 处理类型: " + sphf.getRefersType());
              YHSpecialHandler sph = YHSpecialHandlerFactory
                  .getSpecialHandlerInstance(sphf.getRefersType());
              HashMap<String, String> params = sphf.getParams();
              o = sph.excute(o, params);
            }
            int newDataType = newColumnInfo.get(rsmd.getColumnName(i)) == null ? newColumnInfo
                .get("'" + rsmd.getColumnName(i) + "'")
                : newColumnInfo.get(rsmd.getColumnName(i));
            String testValue = newProc.xmlDataHandler(o, newDataType, rsmd
                .getColumnType(i));
            String newColumnName = newNames.get(rsmd.getColumnName(i));
            columnElement.addAttribute("name", newColumnName);
            columnElement.addAttribute("index", String.valueOf(i));
            columnElement.addAttribute("type", String.valueOf(newDataType));
            columnElement.addAttribute("javaType", "");
            if (testValue == null) {
              testValue = "";
            }
            columnElement.addCDATA(CheckUnicodeString(testValue));
          }
          if ((count % 1000 == 0)) {
            System.out
                .println("以生成"
                    + newTable
                    + "表                                                                                                                     "
                    + count + "条数据.");
          }
          count++;
        } catch (Exception e) {
          System.err.println("一条错误记录 :" + e.getMessage());
        }
       
      }
      System.out
          .println("共生成"
              + newTable
              + "表                                                                                                                     "
              + count + "条数据");
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    File file = new File(path);
    if (!file.exists()) {
      file.mkdirs();
    }
    String dataPath = path + "/" + newTable + ".xml";
    YHTableMappingCfg.saveXmlFile(document, dataPath, "UTF-8");
    System.out.println(newTable + " 表的xml数据文件生成成功！");

  }

  public String CheckUnicodeString(String value) {
    char[] values = value.toCharArray();
    for (int i=0; i < value.length(); ++i) {
      if (values[i] > 0xFFFD) {
        values[i] = 'n';
      } else if (values[i] < 0x20 && values[i] != 't' & values[i] != 'n' & values[i] != 'r'){
        values[i] = 't';
      }
    }
    return String.valueOf(values);
  }

  /**
   * 解析数据文件
   * 
   * @throws Exception
   */
  public void praserDataXml(File inputXml) throws Exception {
    int updateCount = 0;
    Document document = YHTableMappingCfg.getDocument(inputXml);
    Element root = document.getRootElement();
    List<Element> columns = root.elements("columns");
    System.out.println("                               33 ");
    String tabName = root.attributeValue("name");
    String schName = root.attributeValue("sechma");
    String dbType = root.attributeValue("database_type");
    Connection conn = YHTransplantUtil
        .getDBConn(false, Integer.valueOf(dbType));
    try {
      YHProcI newProc = YHProcessorFactory.getProcessorInstance(Integer
          .valueOf(dbType));
      System.out.println("开始导入" + tabName + "表的数据....");
      String insertSql = "insert into " + tabName + "(";
      if (columns.size() > 0) {
        Element columnName = columns.get(0);
        String field = columnName.attributeValue("value");
        insertSql += field + ")values(";
        String indexCent = "";
        for (int i = 0; i < field.split(",").length; i++) {
          if (!"".equals(indexCent)) {
            indexCent += ",";
          }
          indexCent += "?";
        }
        insertSql += indexCent + ")";
        columnName.detach();
        System.out.println(insertSql);
        List<Element> records = root.elements("record");
        PreparedStatement ps = null;
        try {
          ps = conn.prepareStatement(insertSql);
          int count = 1;
          for (Element record : records) {
            try {
              List<Element> columnArray = record.elements("column");
              for (Element column : columnArray) {
                String index = column.attributeValue("index");
                String dataType = column.attributeValue("type");
                String value = column.getText();
                if ("".equals(value.trim())) {
                  value = null;
                }
                Object o = newProc
                    .dbDataHandler(value, Integer.valueOf(dataType));
                ps = newProc.java2sqlParam(o, ps, Integer.valueOf(index));
                column.detach();
              }
              ps.addBatch();
              if ((count % 1000 == 0) || count >= records.size()) {
                ps.executeBatch();
                ps.clearBatch();
                conn.commit();
                System.out
                    .println("导入"
                        + tabName
                        + "表                                                                               "
                        + count + "条数据.");
              }
              count++;
              record.detach();
            } catch (Exception e) {
              System.err.println("一条数据导入失败 ： " + e.getMessage());
            }
           
          }
          System.out.println(tabName + "表导入结束：导入数据" + count + "条");
          conn.commit();
        } catch (Exception e) {
          throw e;
        } finally {
          YHDBUtility.close(ps, null, null);
        }
      } else {
        return;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      conn.commit();
      YHDBUtility.closeDbConn(conn, null);
    }

  }
}
