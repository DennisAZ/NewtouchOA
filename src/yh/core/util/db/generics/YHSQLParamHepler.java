package yh.core.util.db.generics;

import java.io.BufferedReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Map;

import yh.core.util.YHUtility;
import yh.core.util.db.YHStringFormat;

public class YHSQLParamHepler {

  /**
   * 2.0将Java类型的值转换成数据库中对应类型的值
   * 
   * @param param
   *          PreparedStatement传参是所需的值对象数组
   * @param ps
   *          PreparedStatement对象
   * @return ps
   * @throws Exception
   */
  public static PreparedStatement javaParam2SQLParam(Object[] param,
      PreparedStatement ps, String tableName) throws Exception {
    //System.out.println("开始 javaParam2SQLParam 方法");
    // 使用Class.isInstance()方法判定指定的 Object
    // 是否与此 Class 所表示的对象赋值兼容。
    // 此方法是 Java 语言 instanceof 运算符的动态等效方法。
    // 如果当前SQL命令包含参数则进行Java数据类型与SQL命令参数的人工映射    if (param != null && param.length != 0) {
      try {
        for (int i = 0; i < param.length; i++) {
          Object pa = param[i];
          if (pa == null) {
            ps.setObject(i + 1, null);
            continue;
          }
          // 将第i个参数的Java数据类型通过其对应的包装器类人工映射并设定SQL命令参数
          if (Boolean.class.isInstance(pa)) { // 映射boolean类型
            ps.setBoolean(i + 1, Boolean.parseBoolean(pa.toString()));
          } else if (Byte.class.isInstance(pa)) { // 映射byte类型
            ps.setByte(i + 1, Byte.parseByte(pa.toString()));
          } else if (byte[].class.isInstance(pa)) {
            ps.setBytes(i + 1, (byte[])pa); // 映射byte[]类型
          } else if (Character.class.isInstance(pa)
              || String.class.isInstance(pa)) { // 映射char和String类型
            String st = String.valueOf(pa);
            ps.setString(i + 1, st);
          } else if (Short.class.isInstance(pa)) { // 映射short类型
            ps.setShort(i + 1, Short.parseShort(pa.toString()));
          } else if (Integer.class.isInstance(pa)) { // 映射int类型
            ps.setInt(i + 1, Integer.parseInt(pa.toString()));
          } else if (Long.class.isInstance(pa)) { // 映射long类型
            ps.setLong(i + 1, Long.parseLong(pa.toString()));
          } else if (Float.class.isInstance(pa)) { // 映射float类型
            ps.setFloat(i + 1, Float.parseFloat(pa.toString()));
          } else if (Double.class.isInstance(pa)) { // 映射double类型
            ps.setDouble(i + 1, Double.parseDouble(pa.toString()));
          } else if (BigDecimal.class.isInstance(pa)) { // 映射BigDecimal类型
            ps.setBigDecimal(i + 1, new BigDecimal(pa.toString()));
          } else if (Date.class.isInstance(pa)
              || java.util.Date.class.isInstance(pa)) { // 映射Date类型
            try {
              ps.setDate(i + 1,  (Date)pa);
            } catch (Exception e) {
              Timestamp sqlDate = YHUtility.parseTimeStamp(((java.util.Date)pa).getTime());
              ps.setTimestamp(i + 1, sqlDate);
            }
          } else if (Time.class.isInstance(pa)) { // 映射Time类型
            ps.setTime(i + 1, Time.valueOf(pa.toString()));
          } else if (Timestamp.class.isInstance(pa)) { // 映射Timestamp类型
            ps.setTimestamp(i + 1, Timestamp.valueOf(pa.toString()));
          } else {
            throw new Exception("Java程序中包含不支持的自动映射数据类型："
                + pa.getClass().getName());
          }
        }
      } catch (Exception ex) {
        System.err.println("异常信息：特定数据库访问及其他相关错误！\r\n" + ex.getMessage());
        ex.printStackTrace();
        throw ex;
      }
    }
    
    return ps;
  }

  /**
   * 自动将SQL数据类型参数映射转换为Java数据类型
   * 
   * @param <T>
   *          泛型类型参数
   * @param rsmd
   *          关于 ResultSet 对象中列的类型和属性信息的 ResultSetMetaData 对象
   * @param pojoClass
   *          利用Java反射机制查找对应 pojo 的Class
   * @param rs
   *          结果集对象
   * @return 泛型 pojo 实例
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  // 禁用对此方法的类型安全检查
  public static Object sQLParam2JavaParam(Class pojoClass, ResultSet rs,
      ResultSetMetaData rsmd) throws Exception {

    Object pojo = null;
    int index = 0;
    try {
      index = rsmd.getColumnCount();
      pojo = pojoClass.newInstance();
      //System.out.println("pojo ==== "+pojo);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      for (int i = 1; i <= index; i++) {
        // 遍历数据集的每一列，通过共同遵守的Pascal命名规则反射查找并执行对应
        // pojo 类的赋值(getter)方法以实现结果集到pojo泛型集合的自动映射
        // 取得第i列列名
        String setMethodName = rsmd.getColumnName(i);
        // 通过命名规则处理第i列列名,取得 pojo 中对应字段的取值(setter)方法名
        // 转换set方法
        setMethodName = YHStringFormat.unformat(setMethodName);
        setMethodName = "set" + setMethodName.substring(0, 1).toUpperCase()
            + setMethodName.substring(1);
        
        // 取得第i列的数据类型
        int dbType = rsmd.getColumnType(i);
        // 当前反射方法
        Method method = null;
        // 对应第i列的SQL数据类型人工映射到对应的Java数据类型，
        // 并反射执行该列的在 pojo 中对应属性的 setter 方法完成赋值        if (dbType == Types.TINYINT) {
          method = pojoClass.getMethod(setMethodName, byte.class);
          method.invoke(pojo, rs.getByte(i));
        } else if (dbType == Types.SMALLINT) {
          method = pojoClass.getMethod(setMethodName, short.class);
          method.invoke(pojo, rs.getShort(i));
        } else if (dbType == Types.INTEGER || dbType == Types.NUMERIC) {
          try{
            method = pojoClass.getMethod(setMethodName, int.class);
            method.invoke(pojo, rs.getInt(i));
          }catch(NoSuchMethodException e){
            method = pojoClass.getMethod(setMethodName, double.class);
            method.invoke(pojo, rs.getDouble(i));
          }
        } else if (dbType == Types.BIGINT) {
          method = pojoClass.getMethod(setMethodName, long.class);
          method.invoke(pojo, rs.getLong(i));
        } else if (dbType == Types.FLOAT || dbType == Types.REAL) {
          method = pojoClass.getMethod(setMethodName, float.class);
          method.invoke(pojo, rs.getFloat(i));
        } else if (dbType == Types.DOUBLE) {
          method = pojoClass.getMethod(setMethodName, double.class);
          method.invoke(pojo, rs.getDouble(i));
        } else if (dbType == Types.DECIMAL) {
          method = pojoClass.getMethod(setMethodName, double.class);
          method.invoke(pojo, rs.getDouble(i));
        } else if (dbType == Types.BIT) {
          method = pojoClass.getMethod(setMethodName, boolean.class);
          method.invoke(pojo, rs.getBoolean(i));
        } else if (dbType == Types.CHAR || dbType == Types.VARCHAR
            || dbType == Types.LONGVARCHAR ) {
          method = pojoClass.getMethod(setMethodName, String.class);
          method.invoke(pojo, rs.getString(i));
        } else if(dbType == Types.CLOB){
          String clobStr = "";
          Clob cl = rs.getClob(i);
          method = pojoClass.getMethod(setMethodName, String.class);
          method.invoke(pojo,clobToString(cl) );
        } else if (dbType == Types.DATE) { // 继承于 java.util.Date 类
          method = pojoClass.getMethod(setMethodName, java.util.Date.class);
          java.util.Date date = rs.getDate(i);
          method.invoke(pojo, date);
        } else if (dbType == Types.TIME) { // 继承于 java.util.Date 类          method = pojoClass.getMethod(setMethodName, java.util.Date.class);
          method.invoke(pojo, rs.getTime(i));
        } else if (dbType == Types.TIMESTAMP) { // 继承于 java.util.Date 类          method = pojoClass.getMethod(setMethodName, java.util.Date.class);
          if (rs.getTimestamp(i) != null) {
            method.invoke(pojo, rs.getTimestamp(i));
          }
        } else if (dbType == Types.BINARY || dbType == Types.VARBINARY
            || dbType == Types.LONGVARBINARY || dbType == Types.BLOB) {
          method = pojoClass.getMethod(setMethodName, byte[].class);
          method.invoke(pojo, rs.getBytes(i));
        } else {
          throw new Exception("数据库中包含不支持的自动映射数据类型：" + dbType);
        }
      }
    } catch (InstantiationException ex) {
      throw new Exception("异常信息：指定的类对象无法被 Class 类中的 newInstance 方法实例化！\r\n"
          + ex.getMessage());
    } catch (NoSuchMethodException ex) {
      throw new Exception("异常信息：无法找到某一特定的方法！\r\n" + ex.getMessage());
    } catch (IllegalAccessException ex) {
      throw new Exception("异常信息：对象定义无法访问，无法反射性地创建一个实例！\r\n" + ex.getMessage());
    } catch (InvocationTargetException ex) {
      throw new Exception("异常信息：由调用方法或构造方法所抛出异常的经过检查的异常！\r\n" + ex.getMessage());
    } catch (SecurityException ex) {
      throw new Exception("异常信息：安全管理器检测到安全侵犯！\r\n" + ex.getMessage());
    } catch (IllegalArgumentException ex) {
      throw new Exception("异常信息：向方法传递了一个不合法或不正确的参数！\r\n" + ex.getMessage());
    } catch (SQLException ex) {
      throw new Exception("异常信息：获取数据库连接对象错误！\r\n" + ex.getMessage());
    } catch (Exception ex) {
      throw new Exception("异常信息：程序兼容问题！\r\n" + ex.getMessage());
    }
    // 返回结果
    return pojo;
  }
  public static String clobToString(Clob cl) throws Exception{
    String res = "";
    Reader is = null;
    if(cl == null ){
      return "";
    }
    try{
      is = cl.getCharacterStream();// 得到流 
      BufferedReader br = new BufferedReader(is); 
      String s = br.readLine(); 
      StringBuffer sb = new StringBuffer(); 
      while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING 
        sb.append(s); 
        s = br.readLine(); 
        if (s != null) {
          sb.append("\r\n");
        }
      } 
      res = sb.toString(); 
      //System.out.println(res);
      return res;
    }catch(Exception e){
      e.printStackTrace();
      throw e;
    }finally{
      is.close();
    }
  }
  /*
   * public static List sQLParam2JavaParamList(Class pojoClass, ResultSet
   * rs,ResultSetMetaData rsmd) { List<Object> pojoList = new
   * ArrayList<Object>(); try { while(rs.next()){ Object pojo =
   * sQLParam2JavaParam(pojoClass, rs, rsmd); pojoList.add(pojo); } } catch
   * (SQLException e) { e.printStackTrace(); } return pojoList; }
   */
  /**
   * 1.0版 frm
   * 
   * @param m
   * @param rs
   * @param rsmd
   * @return
   * @throws Exception
   */
  public static Map<String, Object> sQLParam2JavaParam(Map<String, Object> m,
      ResultSet rs, ResultSetMetaData rsmd) throws Exception {
    int index = 0;
    //System.out.println("rs.getObject============================ "+rs.getObject(1));
    try {
      index = rsmd.getColumnCount();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    try {
      for (int i = 1; i <= index; i++) {
        // 遍历数据集的每一列，通过共同遵守的Pascal命名规则反射查找并执行对应
        // pojo 类的赋值(getter)方法以实现结果集到pojo泛型集合的自动映射
        // 取得第i列列名
        String fieldName = rsmd.getColumnName(i);
        // 通过命名规则处理第i列列名,取得 pojo 中对应字段的取值(setter)方法名
        // 转换set方法
        String javaName = YHStringFormat.unformat(fieldName);
        // 取得第i列的数据类型
        int dbType = rsmd.getColumnType(i);
        // 当前反射方法
        Method method = null;
        // 对应第i列的SQL数据类型人工映射到对应的Java数据类型，
        // 并反射执行该列的在 pojo 中对应属性的 setter 方法完成赋值
        if (dbType == Types.TINYINT) {
          m.put(javaName, rs.getByte(i));
        } else if (dbType == Types.SMALLINT) {
          m.put(javaName, rs.getShort(i));
        } else if (dbType == Types.INTEGER || dbType == Types.NUMERIC) {
          m.put(javaName, rs.getInt(i));
        } else if (dbType == Types.BIGINT) {
          m.put(javaName, rs.getLong(i));
        } else if (dbType == Types.FLOAT || dbType == Types.REAL) {
          m.put(javaName, rs.getFloat(i));
        } else if (dbType == Types.DOUBLE) {
          m.put(javaName, rs.getDouble(i));
        } else if (dbType == Types.DECIMAL) {
          m.put(javaName, rs.getBigDecimal(i));
        } else if (dbType == Types.BIT) {
          m.put(javaName, rs.getBoolean(i));
        } else if (dbType == Types.CHAR || dbType == Types.VARCHAR
            || dbType == Types.LONGVARCHAR || dbType == Types.CLOB) {
          m.put(javaName, rs.getString(i));
        } else if (dbType == Types.DATE) { // 继承于 java.util.Date 类
          m.put(javaName, rs.getDate(i));
        } else if (dbType == Types.TIME) { // 继承于 java.util.Date 类
          m.put(javaName, rs.getTime(i));
        } else if (dbType == Types.TIMESTAMP) { // 继承于 java.util.Date 类
          m.put(javaName, rs.getTimestamp(i));
        } else if (dbType == Types.BINARY || dbType == Types.VARBINARY
            || dbType == Types.LONGVARBINARY || dbType == Types.BLOB) {
          m.put(javaName, rs.getBytes(i));
        } else {
          throw new Exception("数据库中包含不支持的自动映射数据类型：" + dbType);
        }
      }
    } catch (InstantiationException ex) {
      throw new Exception("异常信息：指定的类对象无法被 Class 类中的 newInstance 方法实例化！\r\n"
          + ex.getMessage());
    } catch (NoSuchMethodException ex) {
      throw new Exception("异常信息：无法找到某一特定的方法！\r\n" + ex.getMessage());
    } catch (IllegalAccessException ex) {
      throw new Exception("异常信息：对象定义无法访问，无法反射性地创建一个实例！\r\n" + ex.getMessage());
    } catch (InvocationTargetException ex) {
      throw new Exception("异常信息：由调用方法或构造方法所抛出异常的经过检查的异常！\r\n" + ex.getMessage());
    } catch (SecurityException ex) {
      throw new Exception("异常信息：安全管理器检测到安全侵犯！\r\n" + ex.getMessage());
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
      throw new Exception("异常信息：向方法传递了一个不合法或不正确的参数！\r\n" + ex.getMessage());
    } catch (SQLException ex) {
      ex.printStackTrace();
      throw new Exception("异常信息：获取数据库连接对象错误！\r\n" + ex.getMessage());
    } catch (Exception ex) {
      throw new Exception("异常信息：程序兼容问题！\r\n" + ex.getMessage());
    }
    // 返回结果
    return m;
  }
}
