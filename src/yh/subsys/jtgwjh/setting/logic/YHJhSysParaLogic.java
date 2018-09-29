package yh.subsys.jtgwjh.setting.logic;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.seclog.logic.YHSecLogUtil;
import yh.core.funcs.system.interfaces.data.YHSysPara;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHLogConst;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHPassEncrypt;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.jtgwjh.sealmanage.data.YHJhSeal;
import yh.subsys.jtgwjh.sealmanage.logic.YHJhSealLogic;
import yh.subsys.jtgwjh.setting.data.YHJhSysPara;

public class YHJhSysParaLogic {

  private static Logger log = Logger.getLogger(YHJhSysParaLogic.class);
  /**
   *  添加 JH
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static void addHall(Connection dbConn,YHSysPara hall) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, hall);
  }
  
  
  /**
   *  添加
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static void addJhHall(Connection dbConn,YHJhSysPara hall) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, hall);
  }
  /**
   *  修改
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static void updateHall(Connection dbConn,YHSysPara hall) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn,hall);
  }
  
  
  /**
   *  修改  jh
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static void updateJhHall(Connection dbConn,YHJhSysPara hall) throws Exception {
    YHORM orm = new YHORM();
    orm.updateComplex(dbConn,hall);
  }
  /**
   *  查询
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static YHJhSysPara jhHallObj(Connection dbConn,String paraName)throws Exception {
    String sql = "select SEQ_ID,PARA_NAME,PARA_VALUE from jh_sys_para where para_name=?";
    PreparedStatement ps = null;
    ResultSet rs = null;
    YHJhSysPara hall = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.setString(1,paraName);
      rs = ps.executeQuery();
      if (rs.next()) {
        hall = new YHJhSysPara();
        hall.setParaName(rs.getString("PARA_NAME"));
        hall.setSeqId(rs.getInt("SEQ_ID"));
        hall.setParaValue(rs.getString("PARA_VALUE"));
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return hall;
  }
  
  
  /**
   *  查询
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static YHSysPara hallObj(Connection dbConn,String paraName)throws Exception {
    String sql = "select SEQ_ID,PARA_NAME,PARA_VALUE from sys_para where para_name=?";
    PreparedStatement ps = null;
    ResultSet rs = null;
    YHSysPara hall = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.setString(1,paraName);
      rs = ps.executeQuery();
      if (rs.next()) {
        hall = new YHSysPara();
        hall.setParaName(rs.getString("PARA_NAME"));
        hall.setSeqId(rs.getInt("SEQ_ID"));
        hall.setParaValue(rs.getString("PARA_VALUE"));
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return hall;
  }

  /**
   *  查询登录者的部门ID
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static String paraStr(Connection dbConn,String userId)throws Exception {   
    String sql = "select SEQ_ID,DEPT_ID from person where SEQ_ID='" + userId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String paraString = "0";
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        paraString = rs.getString("DEPT_ID");
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return paraString;
  }

  /**
   * 判断不可以重复设置人员

   * @param dbConn
   * @return
   * @throws Exception
   */
  public static String nameStr(String benbuUser,String nanyinUser)throws Exception {   
    String str1 = benbuUser;
    String str2 = nanyinUser;
    //    if (YHUtility.isNullorEmpty(str1)) {
    //      str1 = "0";
    //    }
    //    if (YHUtility.isNullorEmpty(str2)) {
    //      str2 = "-1";
    //    }
    String array1[] = str1.split(",");   
    String array2[] = str2.split(",");    
    List list = new ArrayList();
    for (int i = 0;i < array1.length; i++){ 
      for (int j = 0;j < array2.length; j++){ 
        if (array1[i].equals(array2[j])) { 
          list.add(array1[i]);
          break;
        } 
      }
    }
    String strStr1 = "";
    for (int k = 0; k < list.size(); k++) {
      strStr1 += list.get(k) + ",";
    }
    return strStr1;
  }
  /**
   * seqId串转换成dept_name串

   * @return
   * @throws Exception
   */ 
  public static String strString(Connection dbConn,String seqId) throws Exception {
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    String sql = "select dept_name from oa_department where seq_id in (" + seqId + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String strString = "";
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        strString += rs.getString("dept_name") + ",";
      }
      if (strString.length() > 0) {
        strString = strString.substring(0,strString.length()-1);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
    return strString;
  }

  /**
   * seqId串转换成userName串

   * @return
   * @throws Exception
   */ 
  public static String strName(Connection dbConn,String seqId) throws Exception {
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    String sql = "select user_name from person where seq_id in (" + seqId + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String strString = "";
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        strString += rs.getString("user_name") + ",";
      }
      if (strString.length() > 0) {
        strString = strString.substring(0,strString.length()-1);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
    return strString;
  }
  
  
  
  
  
  
  

  /**
   * 
   * 解析人员
   * 
   * @param savePath
   *          :保存文件路径
   * @param dbConn
   *          ：数据库 fromUnit：发送单位 
   * @throws Exception
   */
  public static String parsePersonXML(String upXMLPath, Connection dbConn,YHPerson loginUser) throws Exception {
    SAXReader saxReader = new SAXReader();
    File XMLFile = new File(upXMLPath);
    Document document = saxReader.read(XMLFile);
    Element root = document.getRootElement();
    YHPersonLogic pl = new YHPersonLogic();
    if (!YHUtility.isNullorEmpty(root.getName()) && root.getName().equals("persons")) {
      List<Element> elements = root.elements();
      for (Element node : elements) {
        String elName = node.getName();
        String elData = (String) node.getData() == null ? "" : (String) node.getData();
        elData = elData.trim();
        
        String userId = YHUtility.empty2Default(node.element("userId").getText(), "");
        String userName = YHUtility.empty2Default(node.element("userName") .getText(), "");
        String deptId = YHUtility.empty2Default(node.element("deptId") .getText(), "");
        String sex = YHUtility.empty2Default(node.element("sex").getText(), "");
        String userPriv = YHUtility.empty2Default(node.element("userPriv").getText(), "");
        YHPerson person = new YHPerson();
        
        person.setUserId(userId);
        if (YHUtility.isInteger(deptId)) {
          person.setDeptId(Integer.parseInt(deptId));
        }
        person.setUserName(userName);
        person.setSex(sex);
        person.setUserPriv(userPriv);
        person.setPassword(YHPassEncrypt.encryptPass(""));//空密码
        person.setUseingKey("0");
        person.setPostPriv("0");
        YHORM orm = new YHORM();
        orm.saveSingle(dbConn, person);
        // 获取request
        // HttpServletRequest request = getWebserviceHttp();
        String IP = "127.0.0.1";
        // if (request != null) {
        // IP = request.getRemoteAddr();
        // }
     
        // 系统日志
        YHSysLogLogic.addSysLog(dbConn, YHLogConst.ADD_USER, "初始化：新建人员" + person.getUserName() , loginUser,IP);
        
        // 安全日志
        YHSecLogUtil.log(dbConn, person,IP, "210","初始化：新建人员","1", "添加人员姓名为" + person.getUserName() + ",用户Id为" + person.getUserId());

      }
    }
    return "";
  }
  /**
   * 
   *  
   *      upXMLPath    :文件路径

   * @param dbConn
   *          ：数据库 
   *          unitCode:单位嗲吗
   * @throws Exception
   */
  public static void parseSealXML(String upXMLPath,Connection dbConn,YHPerson loginUser) throws Exception {
    SAXReader saxReader = new SAXReader();
    File XMLFile = new File(upXMLPath);
    Document document = saxReader.read(XMLFile);
    Element root = document.getRootElement();
    if (!YHUtility.isNullorEmpty(root.getName()) && root.getName().equals("tasks")) {
      List<Element> elements = root.elements();//task对象
      for (Element node : elements) {
        YHJhSeal seal = new YHJhSeal();
        String seqIdName = node.element("seqId").getName() == null ? "" : node.element("seqId").getName();
        String seqIdValue = node.element("seqId").getText() == null ? "" : node.element("seqId").getText();
        String sealId = YHUtility.empty2Default(node.element("sealId").getText(), "");
        String deptId = YHUtility.empty2Default(node.element("deptId") .getText(), "");
        String sealName = YHUtility.empty2Default(node.element("sealName") .getText(), "");
        String certStr = YHUtility.empty2Default(node.element("certStr").getText(), "");

        String userStr = YHUtility.empty2Default(node.element("userStr").getText(), "");
        String sealData = YHUtility.empty2Default(node.element("sealData") .getText(), "");
        String createTime = YHUtility.empty2Default(node.element("createTime").getText(), "");
        String deptStr = YHUtility.empty2Default(node.element("deptStr") .getText(), "");
        String isFlag = YHUtility.empty2Default(node.element("isFlag") .getText(), "");
        
        String outDeptId = YHUtility.empty2Default(node.element("outDeptId") .getText(), "");
        String outDeptName = YHUtility.empty2Default(node.element("outDeptName") .getText(), "");
      seal.setSealId(sealId);
      seal.setDeptId(deptId);
      seal.setDeptStr(deptStr);
      seal.setSealData(sealData);
      seal.setSealName(sealName);
      seal.setUserStr(userStr);
      seal.setIsFlag(isFlag);
      seal.setCreateTime(new Date());
      seal.setOutDeptId(outDeptId);
      seal.setOutDeptName(outDeptName);
      seal.setCertStr(certStr);
      YHJhSealLogic.addSeal(dbConn,seal);
      }
    }

  }
 
}
