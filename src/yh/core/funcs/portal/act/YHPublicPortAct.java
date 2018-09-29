package yh.core.funcs.portal.act;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.filefolder.logic.YHFileContentLogic;
import yh.core.funcs.netdisk.logic.YHNetDiskLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.picture.logic.YHPictureLogic;
import yh.core.funcs.portal.util.YHPortalProducer;
import yh.core.funcs.portal.util.rules.YHImgRule;
import yh.core.funcs.portal.util.rules.YHModulesRule;
import yh.core.funcs.portal.util.rules.YHTextRule;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;

public class YHPublicPortAct {
  private String sp = System.getProperty("file.separator");
  private String webPath = "core" + sp 
    + "funcs" + sp 
    + "portal" + sp 
    + "modules" + sp;
  /**type 1 网络硬盘， 2 文件柜，3 图片浏览， 4 图片新闻 ,5 数据库
   * @param args
   */
  public static final String ICON_FOLDER = "/" 
    + YHSysProps.getString(YHSysPropKeys.JSP_ROOT_DIR) 
    + "/core/styles/style1/img/folder.png";
  YHFileContentLogic fcl = new YHFileContentLogic();
  YHNetDiskLogic disk = new YHNetDiskLogic();
  YHPictureLogic lc = new YHPictureLogic();
  public String setPublicPort(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      String type = request.getParameter("type");//区分各个模块名称（或类型）      String publicPath = request.getParameter("publicPath");//接收文件名称路径
      String picName = request.getParameter("picName");
      
      YHPortalProducer producer = null;
      if("null".equalsIgnoreCase(picName)&&"".equalsIgnoreCase(picName)){
        picName = "";
      }
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int seqId = 0;
      int startPage = 1;
      int endPage = 6;
      if(type.equalsIgnoreCase("1")){
                           //调用网络硬盘模块 Loginc
        producer = disk.getNetdiskInfoToDeskTop(dbConn,loginUser,publicPath,startPage,endPage);
//        System.out.println("网络硬盘:::::"+publicPath+"===="+type);
      } else if(type.equalsIgnoreCase("2")){
                        //调用文件柜模块Logic
        producer = fcl.getFileFolderInfoToDeskTop(dbConn,Integer.parseInt(publicPath),1,6);
//       System.out.println("文件柜:::"+publicPath+"==="+type);
      } else if(type.equals("3")){
                        //调用图片浏览 ACT
       producer = lc.getNetdiskInfoToDeskTop(dbConn,publicPath,0,5);
//       System.out.println("图片浏览::::"+picName+"==="+ publicPath+"===="+type);
      }else if(type.equals("4")){
                        //调用图片新闻模块
      } else if ("5".equals(type)) {
        //根据配置的数据库文件取出列表
        String dataPath = request.getSession().getServletContext().getRealPath(sp) 
          + webPath
          + "data" + sp 
          + "data.properties"; 
        producer = new YHPortalProducer();
        String sqlkey = request.getParameter("sqlKey");
        String sql = request.getParameter("sql");
        String ruleList = request.getParameter("ruleList");
        if (!YHUtility.isNullorEmpty(sqlkey)) {
          Map map = getDefSql(dataPath , sqlkey);
          sql = (String)map.get("sql");
          ruleList = (String)map.get("ruleList");
        } 
        ruleList = ruleList.substring(1, ruleList.length() - 1);
        String[] rules =  ruleList.split("\\},\\{");
        for (String r : rules) {
          if (!r.startsWith("{")) {
            r = "{" + r;
          }
          if (!r.endsWith("}")) {
            r = r + "}";
          }
          Map map = YHFOM.json2Map(r);
          String typeStr = (String)map.get("type");
          String showText = (String)map.get("showText");
          String[] showTexts = YHPortalProducer.convert2Array(showText);
          YHModulesRule rule = null;
          if ("img".equals(typeStr)) {
            String imageAddress = (String)map.get("imageAddress");
            String linkAddress = (String)map.get("linkAddress");
            String[] las = YHPortalProducer.convert2Array(linkAddress);
            String[] ias = YHPortalProducer.convert2Array(imageAddress);
            rule = new YHImgRule(showTexts, ias , las);
          } else {
            rule = new YHTextRule(showTexts);
          }
          producer.addRule(rule);
        }
        producer.setData(dbConn, sql);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
      request.setAttribute(YHActionKeys.RET_DATA, producer.toJson());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public static Map getDefSql(String path , String key) throws Exception {
    Map map = new HashMap();
    try {
      File dataFile = new File(path);
      if (!dataFile.exists()) {
        dataFile.createNewFile();
      }
      Map<String , String> dataMap = new HashMap();
      YHFileUtility.load2Map(path, dataMap);
      String value = (String)dataMap.get(key).trim();
      int index = value.indexOf(":");
      int end = value.indexOf("\"ruleList\":[");
      String sqlStr = value.substring(index + 2 , end).trim();
      if (sqlStr.endsWith(",")) {
        sqlStr = sqlStr.substring(0 , sqlStr.length() - 1).trim();
      }
      if (sqlStr.endsWith("\"")) {
        sqlStr = sqlStr.substring(0 , sqlStr.length() - 1).trim();
      }
      map.put("sql", sqlStr);
      String ruleList = value.substring(end + "\"ruleList\":[".length(), value.length() - 2);
      map.put("ruleList", ruleList);
    } catch (Exception ex) {
      throw ex;
    }
    
    return map;
  }
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    String path = "D:\\project\\yh\\webroot\\yh\\core\\funcs\\portal\\modules\\data\\data.properties";
    try {
      Map map = getDefSql(path , "ddd");
      String sql = (String)map.get("sql");
      String ruleList = (String)map.get("ruleList");
      System.out.println(sql);
      System.out.print(ruleList);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

}
