package yh.core.frame.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.act.YHSystemAct;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.docReceive.data.YHJhDocrecvInfo;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendInfo;

public class YHTdoaLogic {
  private static Logger log = Logger.getLogger(YHTdoaLogic.class);
  
  public YHPerson updateUserParam(Connection dbConn, String oaItem, YHPerson person) throws Exception {
    
    YHORM orm = new YHORM();
    person = (YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, person.getSeqId());
    Map<String, String> param = YHFOM.json2Map(person.getParamSet());
    param.put("oaItem", oaItem);
    
    String sql = " UPDATE person  SET PARAM_SET='"+YHFOM.toJson(param)+"' WHERE SEQ_ID="+person.getSeqId();
    PreparedStatement ps = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
    return (YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, person.getSeqId());
  }
  

  public String getUserParamOaItem(Connection dbConn, YHPerson person,String contextPath) throws Exception {
    
    StringBuffer sb = new StringBuffer("[");
    
    YHORM orm = new YHORM();
    person = (YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, person.getSeqId());
    
    Map<String, String> param = YHFOM.json2Map(person.getParamSet());
    String oaItemSre = param.get("oaItem");
    
    if(!YHUtility.isNullorEmpty(oaItemSre)){
      String oaItemsArr[] =  oaItemSre.split("\\|");
      for(String oaItems : oaItemsArr){
        String oaItemArr[] = oaItems.split(",");
        sb.append("[");
        for(String oaItem : oaItemArr){
          String sql = " SELECT FUNC_CODE,ICON,FUNC_NAME,MENU_ID FROM oa_sys_func s WHERE s.MENU_ID='"+oaItem+"'";
          PreparedStatement ps = null;
          ResultSet rs = null;
          try {
            ps = dbConn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
              sb.append("{");
              String funcCode = rs.getString("FUNC_CODE");
              String icon = rs.getString("ICON");
              String funcName = rs.getString("FUNC_NAME");
              String menuId = rs.getString("MENU_ID");
              
              int count = 0;
              if("待发公文".equals(funcName)){
                String[] filters = {"SEND_DATETIME is null"};
                List<YHJhDocsendInfo> docsendInfoList = orm.loadListSingle(dbConn, YHJhDocsendInfo.class, filters);
                count = docsendInfoList.size();
              }
              else if("文件签收".equals(funcName)){
                String[] filters = {"status=0"};
                List<YHJhDocrecvInfo> docsendInfoList = orm.loadListSingle(dbConn, YHJhDocrecvInfo.class, filters);
                count = docsendInfoList.size();
              }
              
              icon = parseMenuIcon(icon);
              
              /*
               * syl ，将单位、部门、人员菜单代码转换成真实路径
               */
              String url = YHSystemAct.parseMenuUrl(funcCode, contextPath);
              
              sb.append("icon:\""+icon+"\",");
              sb.append("url:\""+url+"\",");
            //  sb.append("url:\""+funcCode+"\",");
              sb.append("text:\""+funcName+"\",");
              sb.append("id:\""+menuId+"\",");
              sb.append("count:\""+count+"\"");
              sb.append("},");
            }
          } catch(Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(ps, rs, log);
          }
        }
        if(sb.toString().endsWith(",")){
          sb = sb.deleteCharAt(sb.length()-1);
        }
        sb.append("],");
      }
      if(sb.toString().endsWith(",")){
        sb = sb.deleteCharAt(sb.length()-1);
      }
    }
    sb.append("]");
    return sb.toString();
  }
  
  public String parseMenuIcon(String icon) {
    String folder = YHSysProps.getWebPath() + "/core/frame/5/styles/style1/css/images/app_icons/";
    File iconFile = new File(folder + icon);
    if (!iconFile.exists() || !iconFile.isFile()) {
      return "default.png";
    }
    else{
      return icon;
    }
  }
  
  public void updateUserParamStyle(Connection dbConn, String oaStyle, YHPerson person) throws Exception {
    
    YHORM orm = new YHORM();
    person = (YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, person.getSeqId());
    Map<String, String> param = YHFOM.json2Map(person.getParamSet());
    param.put("oaStyle", oaStyle);
    person.setParamSet(YHFOM.toJson(param).toString());
    
    String sql = " UPDATE person  SET PARAM_SET='"+YHFOM.toJson(param)+"' WHERE SEQ_ID="+person.getSeqId();
    PreparedStatement ps = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.executeUpdate();
    } catch(Exception ex) {
      throw ex;
    } finally {
      person = (YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, person.getSeqId());
      YHDBUtility.close(ps, null, log);
    }
  }
  
  public String getUserParamOaStyle(Connection dbConn, YHPerson person) throws Exception {
    
    YHORM orm = new YHORM();
    person = (YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, person.getSeqId());
    
    Map<String, String> param = YHFOM.json2Map(person.getParamSet());
    String oaStyle = param.get("oaStyle");
    
    String sb = "{\"oaStyle\":\""+oaStyle+"\"}";
    return sb;
  }
}
