package yh.rad.velocity;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.data.YHDsTable;
import yh.core.util.db.YHORM;
import yh.core.util.db.YHStringFormat;

public class YHCodeUtil{

  public static void code2Java(){

  }

  public static void code2DB(String sql){
    // 创建表
    // 通过数据字典创建数据表
    // 单表 : tableName.字段名、字段类型、字段长度、是否自动增长、是否主键、是否唯一、是否能为空
  }

  public static void autoCode(Connection conn, String tableNo, Map req)
      throws Exception{
    //POJO类
    String pojoOutPath = (String) req.get("pojoOutPath");
    String pojoPagckageName = (String) req.get("pojoPagckageName");
    String pojoTemplateName = (String) req.get("pojoTemplateName");
    String pojoTemlateUrl = (String) req.get("pojoTemlateUrl");
    
    YHCode2DbUtil cd = new YHCode2DbUtil();
    Map result = null;
    result = cd.db2JavaCodefNo(conn, tableNo, pojoPagckageName);
    //System.out.println("result :" + result);
    YHvelocityUtil.velocity(result, pojoOutPath, pojoTemplateName, pojoTemlateUrl);
    
    //ACT层
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
    Object list = req.get("listFields");
    request.put("list", list);
    request.put("tableNameReal", YHStringFormat.format(tableName,true));//
    
    YHvelocityUtil.velocity(request, actOutPath, actTemplateName, actTemlateUrl);
    //增删改页面index
    String pageUrl = (String) req.get("pageUrl") + "/" + tableName.toLowerCase();
    String pageOutPath = (String) req.get("pageOutPath") + "/" + tableName.toLowerCase();
    String indexFileName = (String) req.get("indexFileName");
    String indexTitle = (String) req.get("indexTitle");
    String indexTemplateName = (String) req.get("indexTemplateName");
    String pageTemlateUrl = (String) req.get("pageTemlateUrl");

    request = new HashMap();
    request.put("url", pageUrl);
    request.put("title", indexTitle);
    request.put("fileName", indexFileName + ".jsp");
    YHvelocityUtil.velocity(request, pageOutPath, indexTemplateName, pageTemlateUrl);
    String inputActUrl = "/" + actPackageName.replaceAll("\\.", "/")+ "/" + actName;

    //list
    list = req.get("listFields");
    String listFileName = (String) req.get("listFileName");
    request = new HashMap();
    request.put("list", list);
    request.put("tabNo", tableNo);
    request.put("fileName", listFileName + ".jsp");
    request.put("url", pageUrl);
    request.put("acturl",inputActUrl);
    String listTemplateName =  (String) req.get("listTemplateName");
    YHvelocityUtil.velocity(request, pageOutPath, listTemplateName, pageTemlateUrl);
    
    //input
    Object sf = req.get("sf");
    Object rf = req.get("rf");
    Object fields = req.get("inputFields");
    String inputFileName = (String) req.get("inputFileName");
    String inputTemplateName = (String) req.get("inputTemplateName");
    //System.out.println("acturl : " + inputActUrl);
    request = new HashMap();
    request.put("fields", fields);
    request.put("sf", sf);
    request.put("rf", rf);
    request.put("classPath", pojoPagckageName + "." + className);
    request.put("fileName", inputFileName + ".jsp");
    request.put("acturl",inputActUrl);
    request.put("url", pageUrl);//
    YHvelocityUtil.velocity(request, pageOutPath, inputTemplateName, pageTemlateUrl);
    //System.out.println(rf);
  }

  /**
   * 得到所有表的信息，用于页面表的生成
   * 
   * @param conn
   * @return
   * @throws Exception
   */
  public static Map<String, String> dsTableList(Connection conn)
      throws Exception{
    //
    Map<String, String> result = new HashMap<String, String>();
    YHORM orm = new YHORM();
    Map m =null;
    List<YHDsTable> dsts = orm.loadListSingle(conn, YHDsTable.class, m);
    for (YHDsTable dst : dsts){
      result.put(dst.getTableNo(), dst.getTableName());
    }
    return result;
  }
}
