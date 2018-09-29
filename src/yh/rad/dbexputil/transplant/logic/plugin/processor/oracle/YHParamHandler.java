package yh.rad.dbexputil.transplant.logic.plugin.processor.oracle;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import yh.core.util.YHUtility;
import yh.rad.dbexputil.transplant.logic.core.processor.YHProcI;

public class YHParamHandler implements YHProcI{
/**
 * 6.1.3根据新的数据类型的到数据
 * 需要保证的是数据类型之间的可容性   比如数值那么至少o应该是一串数字的字符串
 * 
 * @param o
 * @param dbType
 * @return
 * @throws Exception 
 */
  public  Object dbDataHandler(String o , int newDbType) throws Exception {
    Object result = null;
    if(o == null){
      return null;
    }
    if (newDbType == Types.TINYINT) {
      result = Byte.valueOf(o);
    } else if (newDbType == Types.SMALLINT) {
      result = Short.valueOf(o);
    } else if (newDbType == Types.INTEGER || newDbType == Types.NUMERIC) {
      result = Integer.valueOf(o);
    } else if (newDbType == Types.BIGINT) {
      result = Long.valueOf(o);
    } else if (newDbType == Types.FLOAT || newDbType == Types.REAL) {
      result = Float.valueOf(o);
    } else if (newDbType == Types.DOUBLE) {
      result = Double.valueOf(o);
    } else if (newDbType == Types.DECIMAL) {
      result = Integer.valueOf(o);
    } else if (newDbType == Types.BIT) {
      result = Boolean.valueOf(o);
    } else if (newDbType == Types.CHAR || newDbType == Types.VARCHAR
        || newDbType == Types.LONGVARCHAR) {
      result = o;
    } else if (newDbType == Types.CLOB) {
      result = o;
    } else if (newDbType == Types.DATE 
        || newDbType == Types.TIME
        || newDbType == Types.TIMESTAMP) { // 继承于 java.util.Date 类
        try {
          long time = Long.valueOf(o);
          result = YHUtility.parseTimeStamp(time);
        } catch (Exception e) {
          result = YHUtility.parseTimeStamp(o);
        }
    } else if (newDbType == Types.BINARY || newDbType == Types.VARBINARY
        || newDbType == Types.LONGVARBINARY || newDbType == Types.BLOB) {
        result = o.getBytes();
    } else {
      throw new Exception("数据库中包含不支持的自动映射数据类型：" + newDbType);
    }
    return result;
  }
/**
 * 
 */
  public  String xmlDataHandler(Object o , int newDbType,int oldDbType) throws Exception {
    String result = null;
    if(o == null){
      return null;
    }
    if (newDbType == Types.TINYINT) {
      result = o.toString();
    } else if (newDbType == Types.SMALLINT) {
      result = o.toString();
    } else if (newDbType == Types.INTEGER || newDbType == Types.NUMERIC) {
      result = o.toString();
    } else if (newDbType == Types.BIGINT) {
      result = o.toString();
    } else if (newDbType == Types.FLOAT || newDbType == Types.REAL) {
      result = o.toString();
    } else if (newDbType == Types.DOUBLE) {
      result = o.toString();
    } else if (newDbType == Types.DECIMAL) {
      result = o.toString();
    } else if (newDbType == Types.BIT) {
      result = o.toString();
    } else if (newDbType == Types.CHAR || newDbType == Types.VARCHAR
        || newDbType == Types.LONGVARCHAR) {
      result = o.toString();
    } else if (newDbType == Types.CLOB) {
      result = o.toString();
    } else if (newDbType == Types.DATE 
        || newDbType == Types.TIME
        || newDbType == Types.TIMESTAMP) { // 继承于 java.util.Date 类
      if(o instanceof Date){
        result = YHUtility.getDateTimeStr((Date) o);
      } else if(o instanceof String){
        try {
          long time = Long.valueOf(o.toString());
          result = YHUtility.getDateTimeStr(new Date(time));
        } catch (Exception e) {
          if(o.toString().length() >= 10 && o.toString().length() < 19){
            result = o.toString().substring(0,10);
          }else if(o.toString().length() >= 19){
            result = o.toString().substring(0,19);
          }else {
            result = o.toString();
          }
        }
      } 
    } else if (newDbType == Types.BINARY || newDbType == Types.VARBINARY
        || newDbType == Types.LONGVARBINARY || newDbType == Types.BLOB) {
        result = o.toString();
    } else {
      throw new Exception("数据库中包含不支持的自动映射数据类型：" + newDbType);
    }
    return result;
  }
/**
 * 6.1.1根据老数据类型的到数据
 * @param rs
 * @param index
 * @param dbType
 * @return
 * @throws Exception
 */
  public  Object sqlParam2JavaParam(ResultSet rs, int index, int dbType)
      throws Exception {
    Object o = null;
    int i = index;
    if (dbType == Types.TINYINT) {
      o = rs.getByte(i);
    } else if (dbType == Types.SMALLINT) {
      o = rs.getShort(i);
    } else if (dbType == Types.INTEGER || dbType == Types.NUMERIC) {
      o = rs.getInt(i);
    } else if (dbType == Types.BIGINT) {
      o = rs.getLong(i);
    } else if (dbType == Types.FLOAT || dbType == Types.REAL) {
      o = rs.getFloat(i);
    } else if (dbType == Types.DOUBLE) {
      o = rs.getDouble(i);
    } else if (dbType == Types.DECIMAL) {
      o = rs.getDouble(i);
    } else if (dbType == Types.BIT) {
      o = rs.getBoolean(i);
    } else if (dbType == Types.CHAR || dbType == Types.VARCHAR
        || dbType == Types.LONGVARCHAR) {
      o = rs.getString(i);
    } else if (dbType == Types.CLOB) {
      o = rs.getString(i);
    } else if (dbType == Types.DATE) { // 继承于 java.util.Date 类
      java.util.Date date = rs.getDate(i);
      o = date;
    } else if (dbType == Types.TIME) { // 继承于 java.util.Date 类
      o = rs.getTime(i);
    } else if (dbType == Types.TIMESTAMP) { // 继承于 java.util.Date 类
      o = rs.getTimestamp(i);
    } else if (dbType == Types.BINARY || dbType == Types.VARBINARY
        || dbType == Types.LONGVARBINARY || dbType == Types.BLOB) {
      o = rs.getBytes(i);
    } else {
      throw new Exception("数据库中包含不支持的自动映射数据类型：" + dbType);
    }
    return o;
  }
  /**
   * 
   * @param pa
   * @param ps
   * @param i
   * @return
   * @throws Exception
   */
  public PreparedStatement java2sqlParam(Object pa , PreparedStatement ps , int i) throws Exception{
    try {
        if (pa == null) {
          ps.setObject(i, null);
          return ps;
        }
        // 将第i个参数的Java数据类型通过其对应的包装器类人工映射并设定SQL命令参数
        if (Boolean.class.isInstance(pa)) { // 映射boolean类型
          ps.setBoolean(i, Boolean.parseBoolean(pa.toString()));
        } else if (Byte.class.isInstance(pa)) { // 映射byte类型
          ps.setByte(i, Byte.parseByte(pa.toString()));
        } else if (byte[].class.isInstance(pa)) {
          ps.setBytes(i, (pa.toString()).getBytes()); // 映射byte[]类型
        } else if (Character.class.isInstance(pa)
            || String.class.isInstance(pa)) { // 映射char和String类型
          String st = String.valueOf(pa);
          ps.setString(i, st);
        } else if (Short.class.isInstance(pa)) { // 映射short类型
          ps.setShort(i, Short.parseShort(pa.toString()));
        } else if (Integer.class.isInstance(pa)) { // 映射int类型
          ps.setInt(i, Integer.parseInt(pa.toString()));
        } else if (Long.class.isInstance(pa)) { // 映射long类型
          ps.setLong(i, Long.parseLong(pa.toString()));
        } else if (Float.class.isInstance(pa)) { // 映射float类型
          ps.setFloat(i, Float.parseFloat(pa.toString()));
        } else if (Double.class.isInstance(pa)) { // 映射double类型
          ps.setDouble(i, Double.parseDouble(pa.toString()));
        } else if (BigDecimal.class.isInstance(pa)) { // 映射BigDecimal类型
          ps.setBigDecimal(i, new BigDecimal(pa.toString()));
        } else if (java.sql.Date.class.isInstance(pa)
            || java.util.Date.class.isInstance(pa)) { // 映射Date类型
          try {
            ps.setDate(i,  (java.sql.Date)pa);
            //System.out.println("日期类型：" + pa);
          } catch (Exception e) {
            Timestamp sqlDate = YHUtility.parseTimeStamp(((java.util.Date)pa).getTime());
            //System.out.println("日期类型：" + sqlDate);
            ps.setTimestamp(i, sqlDate);
          }
        } else if (Time.class.isInstance(pa)) { // 映射Time类型
          ps.setTime(i, Time.valueOf(pa.toString()));
        } else if (Timestamp.class.isInstance(pa)) { // 映射Timestamp类型
          ps.setTimestamp(i, Timestamp.valueOf(pa.toString()));
        } else {
          throw new Exception("Java程序中包含不支持的自动映射数据类型："
              + pa.getClass().getName());
        }
        //System.out.println(i);
    } catch (Exception ex) {
      System.err.println("异常信息：特定数据库访问及其他相关错误！\r\n" + ex.getMessage());
      ex.printStackTrace();
      throw ex;
    }
    return ps;
  }
}
