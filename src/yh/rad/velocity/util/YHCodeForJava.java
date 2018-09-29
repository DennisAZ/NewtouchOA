package yh.rad.velocity.util;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import yh.rad.velocity.YHCode2DbUtil;
import yh.rad.velocity.YHvelocityUtil;

public class YHCodeForJava{
  
  /**
   * 根据数据字典自动生成java实体类
   * @param conn  数据库连接
   * @param tableNo 数据字典中的表编码
   * @param pojoOutPath 实体类的输出地址
   * @param pojoPagckageName 实体类的包名
   * @param pojoTemplateName 实体类的模板名
   * @param pojoTemlateUrl 实体类的模板地址
   * @return 返回代码是否生成成功 -如果成功则返回true,否则抛出异常
   * @throws Exception 
   */
  public static boolean codeForPojo(Connection conn, String tableNo
      ,String pojoOutPath,String pojoPagckageName
      ,String pojoTemplateName,String pojoTemlateUrl) throws Exception{
    try{
      YHCode2DbUtil cd = new YHCode2DbUtil();
      Map result = null;
      result = cd.db2JavaCodefNo(conn, tableNo, pojoPagckageName);
      YHvelocityUtil.velocity(result, pojoOutPath, pojoTemplateName, pojoTemlateUrl);
    } catch (Exception e){
      throw e;
    }
    return true;
  }
  /**
   * 根据Pojo类生成Act
   * @param conn
   * @param tableNo
   * @param pojoOutPath
   * @param pojoPagckageName
   * @param pojoTemplateName
   * @param pojoTemlateUrl
   * @return
   * @throws Exception
   * 
    String classPath = pojoPagckageName; //pojo类路径
    String className = (String) result.get("className"); //pojo类名称
    Map request = new HashMap();
    
    String actName = "";
    String actOutPath =  (String) req.get("actOutPath");
    String actPackageName = (String) req.get("actPackageName");
    String actTemplateName = (String) req.get("actTemplateName");
    String actTemlateUrl = (String) req.get("actTemlateUrl");
    String actFileNamePre = (String) req.get("actFileNamePre");
    if( actFileNamePre == null || "".equals(actFileNamePre)) {
      actName = className + "Act";
    }else {
      actName = className + actFileNamePre;
    }
    String tableName = className.substring(2, 3).toLowerCase() + className.substring(3);
    request.put("fileName", actName + ".java");
    request.put("className", className);//
    request.put("tableName", tableName);//
    request.put("classPath", classPath);//
    request.put("packageName", actPackageName);
    YHvelocityUtil.velocity(request, actOutPath, actTemplateName, actTemlateUrl);
   */
  public static boolean codeForAct(Connection conn, String tableNo
      ,String pojoOutPath,String pojoPagckageName
      ,String pojoTemplateName,String pojoTemlateUrl) throws Exception{
    try{
      YHCode2DbUtil cd = new YHCode2DbUtil();
      Map result = null;
      result = cd.db2JavaCodefNo(conn, tableNo, pojoPagckageName);
      YHvelocityUtil.velocity(result, pojoOutPath, pojoTemplateName, pojoTemlateUrl);
    } catch (Exception e){
      throw e;
    }
    return true;
  }
}
