package yh.core.util.db.generics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.db.YHDBUtility;

public class YHJObject2SQLHepler {

  public static Map<String, Object> javaObject2InsertSQL(
      Map<String, Object> fieldInfo, Connection conn, PreparedStatement pstmt)
      throws Exception {

    String tableName = (String) fieldInfo.get("tableName");
    fieldInfo.remove("tableName");
    String seqIdField = "SEQ_ID";
    if ("oa_im_group".equalsIgnoreCase(tableName)) {
      seqIdField = "GROUP_ID";
      fieldInfo.remove(seqIdField);
    }
    if ("oa_im_offline_attach".equalsIgnoreCase(tableName)) {
      seqIdField = "ID";
      fieldInfo.remove(seqIdField);
    }
    if ("oa_im_group_maxmsgid".equalsIgnoreCase(tableName)) {
      seqIdField = "MSG_ID";
      fieldInfo.remove(seqIdField);
    }
    if ("oa_im_group_msg".equalsIgnoreCase(tableName)) {
      seqIdField =  "MSG_ID";
      fieldInfo.remove(seqIdField);
    }
    fieldInfo.remove("SEQ_ID");
    StringBuffer sql = new StringBuffer("insert into ").append(tableName)
        .append(" ( ");
    StringBuffer fields = new StringBuffer();
    StringBuffer values = new StringBuffer();

    int length = fieldInfo.size();
    List paramList = new ArrayList<Object>();
    String[] fN = new String[length];
    int i = 0;
    Set set = fieldInfo.keySet();
    Object[] keys = set.toArray();
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    for (int j = 0; j < keys.length; j++) {
      String key = (String) keys[j];
      Object value = fieldInfo.get(keys[j]);
      // 判断是否为list类型，如果是list类型则为从表信息,忽略
      if (value != null && List.class.isAssignableFrom(value.getClass())) {
        continue;
      }
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        if("".equals(fields.toString())){
          fields.append("[").append(key).append("]");
        }else{
          fields.append(",[").append(key).append("]");
        }
      }else {
        if("".equals(fields.toString())){
          fields.append(key);
        }else{
          fields.append(",").append(key);
        }
      }      
      if("".equals(values.toString())){
        values.append(" ? ");
      }else {
        values.append(",").append("?");
      }
       
      paramList.add(value);
      fN[i] = key;
      i++;
    }
    sql.append(fields).append(" ) values ( ").append(values).append(" ) ");
    try {
      pstmt = conn.prepareStatement(sql.toString());
      YHSQLParamHepler.javaParam2SQLParam(paramList.toArray(), pstmt, tableName);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
    return fieldInfo;
  }

  /**
   * 2.0版 组织SQL的update语句 需要注意的特殊情况 更新主表是的级联更新从表
   * 采取的策略是：查出所有从表信息然后删除所有从表，更新主表，跟新从表，将从表信息再插入数据库中
   * 总的实现思路为：从数据字典中查询出从表的外键关联字段，通过主表的seq_id查询出次关联字段对应的值，再通过这个值进行操作
   * 
   * @param fieldInfo
   * @param sqls
   * @throws Exception
   */
  public static void javaObject2UpdateSQL(Map<String, Object> fieldInfo,
      Connection conn, PreparedStatement pstmt) throws Exception {

    /*
     * 此处得到的数据有两种可能： 1.SEQ_ID 表明主从表之间是通过SEQ_ID关联的，主要是业务实体之间关联
     * 2.编码字段，表明主从表之间是通过编码关联的，主要应用场景有如数据字典的维护
     */
    
    String tableName = (String) fieldInfo.get("tableName");
    String seqIdField = "SEQ_ID";
   
    if ("oa_im_group".equalsIgnoreCase(tableName)) {
      seqIdField = "GROUP_ID";
    }
    if ("oa_im_offline_attach".equalsIgnoreCase(tableName)) {
      seqIdField = "ID";
    }
    if ("oa_im_group_maxmsgid".equalsIgnoreCase(tableName)) {
      seqIdField = "MSG_ID";
    }
    if ("oa_im_group_msg".equalsIgnoreCase(tableName)) {
      seqIdField =  "MSG_ID";
    }
    fieldInfo.remove("tableName");
    int id = (Integer) fieldInfo.get(seqIdField);
    int length = fieldInfo.size();
    fieldInfo.remove(seqIdField);
    fieldInfo.remove("SEQ_ID");
    StringBuffer sql = new StringBuffer("update ").append(tableName).append(
        " set ");
    StringBuffer field = new StringBuffer();
    List paramList = new ArrayList<Object>();
    String[] fN = new String[length];
    int i = 0;
    String key = null;
    Object value = null;
    Set set = fieldInfo.keySet();
    Object[] keys = set.toArray();
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    for (int j = 0; j < keys.length; j++) {
      key = (String) keys[j];
      value = fieldInfo.get(key);
      // 判断是否为list类型，如果是list类型则为从表信息
      if (value != null && List.class.isAssignableFrom(value.getClass())) {
        continue;
      }
      if(!"".equals(field.toString())){
        field.append(",");
      }
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        field.append("[").append(key).append("]").append(" = ").append(" ? ");
      }else {
        field.append(key).append(" = ").append(" ? ");
      }
      paramList.add(value);
      i++;
    }

    sql.append(field).append(" where "+ seqIdField +" = ? ");
    paramList.add(id);
    fN[i] = seqIdField ;
    //System.out.println(sql.toString());
    try {
      pstmt = conn.prepareStatement(sql.toString());
      YHSQLParamHepler.javaParam2SQLParam(paramList.toArray(), pstmt, tableName);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }

  /**
   * 2.0版 组织SQL的delete语句
   * 
   * @param fieldInfo
   * @param sqls
   * @throws Exception
   */
  public static void javaObject2DeleteSQL(Map<String, Object> fieldInfo,
      Connection conn, PreparedStatement pstmt) throws Exception {
    // 业务代码
    String tableName = (String) fieldInfo.get("tableName");
    fieldInfo.remove("tableName");
    String seqIdField = "SEQ_ID";
    if ("oa_im_group".equalsIgnoreCase(tableName)) {
      seqIdField = "GROUP_ID";
    }
    if ("oa_im_offline_attach".equalsIgnoreCase(tableName)) {
      seqIdField = "ID";
    }
    if ("oa_im_group_maxmsgid".equalsIgnoreCase(tableName)) {
      seqIdField = "MSG_ID";
    }
    if ("oa_im_group_msg".equalsIgnoreCase(tableName)) {
      seqIdField =  "MSG_ID";
    }
    int id = (Integer) fieldInfo.get(seqIdField);
    fieldInfo.remove(seqIdField);
    fieldInfo.remove("SEQ_ID");
    
    StringBuffer sql = new StringBuffer("delete from  ").append(tableName);
    sql.append(" where "+ seqIdField +" = ? ");
       
    try {
      pstmt = conn.prepareStatement(sql.toString());
      pstmt.setInt(1, id);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }

  }

  /**
   * 2.0版 组织SQL的select语句
   * 
   * @param fieldInfo
   * @param sqls
   * @throws Exception
   */
  public static PreparedStatement javaObject2QuerySQL(
      Map<String, Object> fieldInfo, Connection conn, Map<String, Object> filter)
      throws Exception {
    PreparedStatement pstmt = null;
    String tableName = (String) fieldInfo.get("tableName");
    fieldInfo.remove("tableName");
    StringBuffer sql = new StringBuffer("select ");
    StringBuffer where = new StringBuffer();
    List paramList = new ArrayList<Object>();
    int i = 0;
    Iterator iter = fieldInfo.keySet().iterator();
    while (iter.hasNext()) {
      String key = (String) iter.next();
      Object value = fieldInfo.get(key);
      // 判断是否为list类型，如果是list类型则为表信息
      if (value != null && List.class.isAssignableFrom(value.getClass())) {
        List clsInfo = (List) value;
        Iterator clsInfoIter = clsInfo.iterator();
        while (clsInfoIter.hasNext()) {
          String val = (String) clsInfoIter.next();
          if (YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS).equals(YHConst.DBMS_SQLSERVER)) {
            val = "[" + val + "]";
          }
          sql.append(val);
          if (clsInfoIter.hasNext()) {
            sql.append(" , ");
          }
        }
        continue;
      }
    }

    iter = filter.keySet().iterator();
    while (iter.hasNext()) {
      Object o = iter.next();
      String subKey = (String) o;
      Object va = filter.get(o);
      if(!"".equals(where.toString())){
        where.append(" and ");
      }
      where.append(subKey).append(" = ").append(" ? ");
      paramList.add(va) ;
      i++;
    }
    sql.append(" from ").append(tableName);
    if(!"".equals(where.toString())){
      sql.append(" where ").append(where);
    }
    try {
      pstmt = conn.prepareStatement(sql.toString());
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    YHSQLParamHepler.javaParam2SQLParam(paramList.toArray(), pstmt, tableName);

    return pstmt;
  }
  /**
   * 2.0版 组织SQL的select语句
   * 
   * @param fieldInfo
   * @param sqls
   * @throws Exception
   */
  public static PreparedStatement javaObject2QuerySQL(
      Map<String, Object> fieldInfo, Connection conn, String[] filter)
      throws Exception {
    PreparedStatement pstmt = null;
    String tableName = (String) fieldInfo.get("tableName");
    fieldInfo.remove("tableName");
    StringBuffer sql = new StringBuffer("select ");
    StringBuffer where = new StringBuffer();
    Iterator iter = fieldInfo.keySet().iterator();
    while (iter.hasNext()) {
      String key = (String) iter.next();
      Object value = fieldInfo.get(key);
      // 判断是否为list类型，如果是list类型则为表信息
      if (value != null && List.class.isAssignableFrom(value.getClass())) {
        List clsInfo = (List) value;
        Iterator clsInfoIter = clsInfo.iterator();
        String ms = YHSysProps.getProp("db.jdbc.dbms");
        while (clsInfoIter.hasNext()) {
          String val = (String) clsInfoIter.next();
          if ("sqlserver".equals(ms)) {
            val = "["+ val +"]";
          }
          sql.append(val);
          if (clsInfoIter.hasNext()) {
            sql.append(" , ");
          }
        }
        continue;
      }
    }

    for(int i=0; filter != null && i< filter.length ;i++){
      String subKey = filter[i];
      if(!"".equals(where.toString())){
        where.append(" and ");
      }
      where.append(subKey);
    }
    sql.append(" from ").append(tableName);
    if(!"".equals(where.toString())){
      sql.append(" where ").append(where);
    }
    try {
      pstmt = conn.prepareStatement(sql.toString());
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return pstmt;
  }
}
