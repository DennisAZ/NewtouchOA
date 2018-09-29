package yh.core.util.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import yh.core.data.YHDsField;
import yh.core.data.YHDsTable;
import yh.core.data.YHDsType;
import yh.core.data.YHPageDataListNew;
import yh.core.data.YHPageQueryParamNew;
import yh.core.load.YHPageLoaderNew;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.db.YHStringFormat;
import yh.core.util.form.YHFOM;
/**
 * 通用的data-to-json转换工具
 * 
 * @author TTlang
 * 
 */
public class YHDTJ {
  private static Logger log = Logger.getLogger(YHDTJ.class);
  //private Map<String, String>
  /**
   * 通过tableNo组装成sql语句
   * @param conn
   * @param tableNo
   * @return
   * @throws Exception
   */
  private StringBuffer toSqlString(Connection conn, String tableNo,String[] filters) throws Exception{
    //System.out.println("                 toSqlString1                     ");
    StringBuffer sqlBuff = new  StringBuffer();
    StringBuffer fieldBuff = new StringBuffer();//查询到字段

    StringBuffer fromBuff = new StringBuffer();//查询的表
    StringBuffer whereBuff = new  StringBuffer();//查询的条件

    YHDsTable dsTable = null;//数据字典中的表信息

    YHDsField dsField = null;
    List<YHDsField> dsFieldList = null;//字段集合
    int tableIndex = 0;//定义table索引
    String tablePre = "T";//定义表别名的前缀
    String tableAile = tablePre + tableIndex;//组装table的别名  如：T0
    YHORM orm = new YHORM();
    //1.得到数据字典中的表定义dsTable
    Map<String, Object> filtersMap = new HashMap<String, Object>();
    filtersMap.put("TABLE_NO", tableNo);
    //System.out.println("fffffffffffffffffffffffff"+filtersMap);
    dsTable = (YHDsTable) orm.loadObjComplex(conn, YHDsTable.class, filtersMap);
    //2.数据库表名

    String tableName = dsTable.getTableName();
   //7.组装查询的表字段
    fromBuff.append(tableName).append(" ").append(tableAile);
    //3.所有字段

    dsFieldList = dsTable.getFieldList();
    Iterator<YHDsField> dsFielditer = dsFieldList.iterator();
    //4.遍历所有字段

   while(dsFielditer.hasNext()){
      //5.得到字段名

      dsField = dsFielditer.next();
      String fieldName = dsField.getFieldName();
      fieldBuff.append(tableAile).append(".").append(fieldName);
      //8.判断是否有外键

      if ("".equals(dsField.getFkTableNo()) || dsField.getFkTableNo() == null) {
        //6.组装查询字段
      }else{
        tableIndex++;//如果是外键则table索引++
        String tableAile1 = tablePre + tableIndex;//T1
        //外表的信息

        Map<String, Object> foreFilters = new HashMap<String, Object>();
        foreFilters.put("TABLE_NO", dsField.getFkTableNo());
        //System.out.println("ffffffffffffff2fffffffffff"+foreFilters);
        YHDsTable foreDsTable = (YHDsTable) orm.loadObjComplex(conn, YHDsTable.class, foreFilters);//数据字典中的表信息

        List<YHDsField> foreDsFieldList = foreDsTable.getFieldList();//字段集合
        String fkNameField = null;
        String fkRelaField = null;
        for (YHDsField foreDsField : foreDsFieldList) {
          
          if(dsField.getFkNameFieldNo()!=null&&dsField.getFkNameFieldNo().equals(foreDsField.getFieldNo())){
           fkNameField = foreDsField.getFieldName(); 
          }
          if(dsField.getFkRelaFieldNo().equals(foreDsField.getFieldNo())){
          //  System.out.println("sssssssssss : "+foreDsField.getFieldNo());
            fkRelaField = foreDsField.getFieldName(); 
           }
        }
        //得到外键的表名

        String foreTableName = foreDsTable.getTableName();
        //组装查询的表字段
        if("".equals(fromBuff.toString())){
          fromBuff.append(foreTableName).append(" ").append(tableAile1);
        }else{
          fromBuff.append(",").append(foreTableName).append(" ").append(tableAile1);
        }
        
        if("".equals(fieldBuff.toString())){
          fieldBuff.append(tableAile1).append(".").append(fkNameField).append(" ").append(fieldName).append("_Desc");
        }else{
          fieldBuff.append(",").append(tableAile1).append(".").append(fkNameField).append(" ").append(fieldName).append("_Desc");
        }
        if(!"".equals(whereBuff.toString())){
          whereBuff.append(" and ");
        }
        whereBuff.append(tableAile1).append(".").append(fkRelaField)
        .append(" = ").append(tableAile).append(".").append(fieldName);
        //判断是否为小编码
        if (!(dsField.getCodeClass() == null) &&! "".equals(dsField.getCodeClass())) {
            //得到小编码类型

          String codeClass = dsField.getCodeClass();
          if(!"".equals(whereBuff.toString())){
            whereBuff.append(" and ");
          }
          whereBuff.append(tableAile1).append(".").append("CLASS_NO")
                    .append(" = '").append(codeClass).append("'");
          //where T0.fieldName = T1.fkNameField and T1.codeClass = codeClass;
        }
      }
      if(dsFielditer.hasNext()){
        fieldBuff.append(",");
      }
    }
   if(filters != null && filters.length > 0){
     for (String wh : filters) {
       if(wh == null || "".equals(wh)){
         continue;
       }
       if(!"".equals(whereBuff.toString())){
         whereBuff.append(" and ");
       }
         whereBuff.append(wh);
    }
   }
   if(!"".equals(fieldBuff.toString())){
     sqlBuff.append("select ").append(fieldBuff);
     if(!"".equals(fromBuff.toString())){
       sqlBuff.append(" from ").append(fromBuff);
       if(!"".equals(whereBuff.toString())){
         sqlBuff.append(" where ").append(whereBuff);
       }
     }
   }
   //System.out.println("sql : "+sqlBuff.toString());
    return sqlBuff;
  }
    
   /**
    * 将rs得到的数据转换成json格式的数据

    * @param rs
    * @param pageNum
    * @param pageRows
    * @return
   * @throws Exception 
    */
   public StringBuffer toJson(Connection conn,String tableNo,String[] filters) throws Exception{
     PreparedStatement ps = null;     
     ResultSet rs = null;
     try {
       String sql = toSqlString(conn, tableNo,filters).toString();
       ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
           ResultSet.CONCUR_READ_ONLY);
       rs = ps.executeQuery();
       ResultSetMetaData rsmt = rs.getMetaData();
       StringBuffer json = new StringBuffer("{");
       StringBuffer field = null;
       StringBuffer record = new StringBuffer();
       rs.last();
       int total = rs.getRow();
       rs.beforeFirst();
       
       json = new StringBuffer("{'total':" + total + ",'records':[");
       while(rs.next()){
         if(!"".equals(record.toString())){
           record.append(",");
         }
         record.append("{ ");
         field = new StringBuffer();
         for (int i = 1; i <= rsmt.getColumnCount(); i++) {
          
           String dbFieldName = rsmt.getColumnName(i);
           int typeInt = rsmt.getColumnType(i);
           Object value = rs.getObject(i);
           if (YHDsType.isIntType(typeInt)
               || YHDsType.isLongType(typeInt)
               || YHDsType.isDecimalType(typeInt)) {
             value = YHUtility.null2Empty(value.toString());
           } else {
             //System.out.println(value);
             if (value == null) {
               value = "\"" + YHUtility.null2Empty(null) + "\"";
             } else {
               value = "\"" + YHUtility.null2Empty(value.toString()) + "\"";
             }
           }
           if (value == null) {
             value = "";
           }
           //System.out.println("........."+dbFieldName+" : "+ rsmt.getTableName(i));
           String fieldName = YHStringFormat.unformat(dbFieldName);
           if(!"".equals(field.toString())){
             field.append(",");
           }
           //System.out.println(fieldName);
           field.append(fieldName).append(":").append(value);
         }
         record.append(field);
         record.append(" }");
       }
       json.append(record);
       json.append(" ] ").append(" }");
       return json;
     }catch(Exception ex) {
       throw ex;
     }finally {
       YHDBUtility.close(ps, rs, log);
     }
   }
   /**
    * 将rs得到的数据转换成json格式的数据

    * @param rs
    * @param pageNum
    * @param pageRows
    * @return
   * @throws Exception 
   * new String[]{"SEQ_ID = 1","NAME='DDD'"}
    */
   public String toJson(Connection conn,String tableNo,Integer pageNum,Integer pageRows,String[] filters) throws Exception{
     PreparedStatement ps = null;     
     ResultSet rs = null;
     try {
       String sql = toSqlString(conn, tableNo,filters).toString();
       ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
           ResultSet.CONCUR_READ_ONLY);
       rs = ps.executeQuery();
       ResultSetMetaData rsmt = rs.getMetaData();
       StringBuffer json = new StringBuffer("{");
       StringBuffer field = null;
       StringBuffer record = new StringBuffer();
       if((pageNum != null && pageNum >= 0 )
           && (pageRows != null && pageRows > 0 )){//分页操作
         rs.last();
         int total = rs.getRow();
         // System.out.println();
         //System.out.println(total);
         json = new StringBuffer("{'total':" + total + ",'records':[");
         //System.out.println("pageNum : "+(pageNum * pageRows + 1));
         
         rs.absolute(pageNum * pageRows + 1);
         int index = (total - pageNum * pageRows) < pageRows ? (total - pageNum
             * pageRows) : pageRows;
         //System.out.println(index);
         for (int j = 0; j < index; j++) {
           if(!"".equals(record.toString())){
             record.append(",");
           }
           record.append("{ ");
           field = new StringBuffer();
           for (int i = 1; i <= rsmt.getColumnCount(); i++) {
             String dbFieldName = rsmt.getColumnName(i);
             //System.out.println(dbFieldName);
             int typeInt = rsmt.getColumnType(i);
             Object value = rs.getObject(i);
             if (YHDsType.isIntType(typeInt)
                 || YHDsType.isLongType(typeInt)
                 || YHDsType.isDecimalType(typeInt)) {
               if(value == null){
                 value = 0;
               }else{
                 value = YHUtility.null2Empty(value.toString());
               }
             } else {
               //System.out.println(value);
               if (value == null) {
                 value = "\"" + YHUtility.null2Empty(null) + "\"";
               } else {
                 value = "\"" + YHUtility.null2Empty(value.toString()) + "\"";
               }
             }
             if (value == null) {
               value = "";
             }
             //System.out.println(dbFieldName+" : "+ rsmt.getTableName(i));
             String fieldName = YHStringFormat.unformat(dbFieldName);
             //System.out.println(fieldName);
             if(!"".equals(field.toString())){
               field.append(",");
             }
             field.append(fieldName).append(":").append(value);
           }
           record.append(field);
           record.append(" }");
           rs.next();
         }
         json.append(record);
         json.append(" ]");
       }
       json.append(" }");
       return json.toString();
     }catch(Exception ex) {
       throw ex;
     }finally {
       YHDBUtility.close(ps, rs, log);
     }
   }
   
   /**
   * @param rs
   * @param pageNum
   * @param pageRows
   * @return
  * @throws Exception 
  * new String[]{"SEQ_ID = 1","NAME='DDD'"}
   */
  public String toJson(Connection dbConn, Map request, String categoryNo) throws Exception{
    try {
      String where = "";
      if(!"null".equals(categoryNo) && !YHUtility.isNullorEmpty(categoryNo)){
        where = " where d.CATEGORY_NO = " + categoryNo;
      }
      String sql = " select d.SEQ_ID, d.TABLE_NO, d.TABLE_NAME, d.TABLE_DESC, d.CLASS_NAME, T1.CLASS_DESC, d.CATEGORY_NO from oa_table_dicts d "
                 + " left join oa_kind_dict_item T1 on T1.CLASS_CODE = d.CATEGORY_NO and T1.CLASS_NO = 'S01' "
                 + where
                 + " order by TABLE_NO asc ";
      YHPageQueryParamNew queryParam = (YHPageQueryParamNew) YHFOM.build(request);
      YHPageDataListNew pageDataList = YHPageLoaderNew.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
   }
   
   
   /**
    * 将rs得到的数据转换成json格式的数据

    * @param rs
    * @param pageNum
    * @param pageRows
    * @return
   * @throws Exception 
    */
   public StringBuffer toJson2Flex(Connection conn,String tableNo,Integer pageNum,Integer pageRows,String[] filters) throws Exception{
     PreparedStatement ps = null;     
     ResultSet rs = null;
     try {
       String sql = toSqlString(conn, tableNo,filters).toString();
       ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
           ResultSet.CONCUR_READ_ONLY);
       rs = ps.executeQuery();
       ResultSetMetaData rsmt = rs.getMetaData();
       StringBuffer json = new StringBuffer("{");
       if((pageNum != null && pageNum >= 0 )
           && (pageRows != null && pageRows > 0 )){//分页操作
         rs.last();
         int total = rs.getRow();
         // System.out.println();
         //System.out.println(total);
         json = new StringBuffer("{'total':" + total + ",'records':[");
         //System.out.println(pageNum * pageRows + 1);
         rs.absolute(pageNum * pageRows + 1);
         int index = (total - pageNum * pageRows) < pageRows ? (total - pageNum
             * pageRows) : pageRows;
         //System.out.println(index);
         for (int j = 0; j < index; j++) {
           json.append("{ ");
           //System.out.println("====================");
           for (int i = 1; i <= rsmt.getColumnCount(); i++) {
             String dbFieldName = rsmt.getColumnName(i);
             Object value = rs.getObject(i);
             if (int.class.isInstance(value)
                 || Integer.class.isInstance(value)
                 || double.class.isInstance(value)
                 || Double.class.isInstance(value)) {
               value = YHUtility.null2Empty(value.toString());
             } else {
               //System.out.println(value);
               if (value == null) {
                 value = "\"" + YHUtility.null2Empty(null) + "\"";
               } else {
                 value = "\"" + YHUtility.null2Empty(value.toString()) + "\"";
               }
             }
             if (value == null) {
               value = "";
             }
             //System.out.println(dbFieldName+" : "+ rsmt.getTableName(i));
             String fieldName = YHStringFormat.unformat(dbFieldName);
             json.append(fieldName).append("_").append(j).append(":").append(value);
             if (i <= rsmt.getColumnCount() - 1) {
               json.append(",");
             }
           }
           json.append(" }");
           if (j < index - 1) {
             json.append(",");
           }
           rs.next();
         }
         json.append(" ]");
       }
       json.append(" }");
       return json;
     }catch(Exception ex) {
       throw ex;
     }finally {
       YHDBUtility.close(ps, rs, log);
     }
   }
}
