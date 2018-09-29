package yh.subsys.portal.guoyan.module.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import yh.core.data.YHDatabase;
import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.portal.guoyan.module.data.YHPortalDataRule;

public class YHPortalUtil {

  /**
   * 组装json数据
   * @return
   */
  public static StringBuffer toJson(YHPageDataList pageDataList){
    StringBuffer result = new StringBuffer();
    StringBuffer field = new StringBuffer();
    int recordCnt = pageDataList.getRecordCnt();
    for (int i = 0; i < recordCnt; i++) {
      YHDbRecord record = pageDataList.getRecord(i);
      if (!"".equals(field.toString())) {
        field.append(",");
      }
      field.append(record.toJson());
    }
    result.append("[").append(field).append("]");
    return result;
  }
  
  /**
   * 组装json数据
   * @return
   */
  public static StringBuffer oneDatatoJson(YHPageDataList pageDataList){
    StringBuffer result = new StringBuffer();
    YHDbRecord record = null;
    if(pageDataList.getRecordCnt() > 0){
      record = pageDataList.getRecord(0);
      result.append(record.toJson());
    }else{
      result.append("{}");
    }
    return result;
  }
  /**
   * 解析页面穿过来的参数
   * @param params
   * @return
   */
  public static Map<String, String> praserParams(Map params){
    Map<String, String> resultParams = new HashMap<String, String>();
    Set<String> keys = params.keySet();
    for (String key : keys) {
      String value = "";
      Object paramValue = params.get(key);
      if (paramValue instanceof String) {
        value = (String)paramValue;
      }else {
        value = ((String[])paramValue)[0];
      }
      resultParams.put(key, value);
    }
    return resultParams;
  }
  
  public static String getUserNameById(Connection conn,String userId) throws Exception{
    String sql = "select USER_NAME from user where USER_ID='" + userId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String result = "";
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        result = rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  /**
   * 
   * @param fileName
   * @return
   * @throws Exception
   */
  public static Map<String,YHPortalDataRule> loadDataRules(String fileName) throws Exception {
    Map<String,YHPortalDataRule> rtList = new  HashMap<String,YHPortalDataRule>();
    
    if (new File(fileName).exists()) {
      Map<String, String> ruleMap = new HashMap<String, String>();
      YHFileUtility.load2Map(fileName, ruleMap);
      Iterator<String> iKeys = ruleMap.keySet().iterator();
      while (iKeys.hasNext()) {
        String key = iKeys.next();
        String dbConfJson = ruleMap.get(key).trim();
        if (YHUtility.isNullorEmpty(dbConfJson)) {
          continue;
        }
        try {
          YHPortalDataRule pdr = (YHPortalDataRule) YHFOM.json2Obj(dbConfJson, YHPortalDataRule.class);
          rtList.put(key,pdr);
        }catch(Exception ex) {
          throw ex;
        }
      }
    }
    return rtList;
  } 
  /**
   * 取得配置文件的位置
   * @return
   */
  public static String getConfigPath(){
    String configPath = "";
    configPath = YHSysProps.getRootPath() 
      + "\\" + YHSysProps.getString(YHSysPropKeys.WEB_ROOT_DIR)
      + "\\" + YHSysProps.getString(YHSysPropKeys.JSP_ROOT_DIR)
      + "\\subsys\\portal\\guoyan\\config";
    return configPath;
  }
  /**
   * 取得配置文件的位置
   * @return
   */
  public static String getConfigFileName(){
    String configFileName = "";
    configFileName = getConfigPath() + "\\" + "datarule.properties";
    return configFileName;
  }
}
