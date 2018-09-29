package yh.core.funcs.system.info.act;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import yh.core.data.YHAuthKeys;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.setdescktop.userinfo.act.YHUserinfoAct;
import yh.core.funcs.system.info.logic.YHInfoLogic;
import yh.core.funcs.system.logic.YHSystemLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHRegistProps;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.load.YHConfigLoader;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHRegistUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class YHInfoAct {
  public static final String REG_FILE_PATH = ""+File.separator+"WEB-INF"+File.separator+"config"+File.separator+"regist"+File.separator+"";
  
  public static final String LICENSE_FILE_PATH = ""+File.separator+"WEB-INF"+File.separator+"classes"+File.separator+"";
  /**
   * 获取系统信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSystemInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    YHInfoLogic logic = new YHInfoLogic();
    String postfix = ".yh";
    try {
      String regUnitName = "";
      String regUserAmount = "";
      String regImUserAmount = "";
      String regMachineCode = "";
      String regOrNot = "0";
      if (YHRegistProps.isEmpty()) {
        String sp = System.getProperty("file.separator");
        String path = request.getSession().getServletContext().getRealPath(sp) 
        + REG_FILE_PATH + "yh.regist";
        Map<String, String> regMap = YHRegistUtility.loadRegist(path);
        regUnitName = regMap.get(YHAuthKeys.REGIST_ORG + postfix);
        regMachineCode = regMap.get(YHAuthKeys.MACHINE_CODE + postfix);
        regUserAmount = String.valueOf(YHRegistUtility.getUserCnt());
        regImUserAmount = "30";
      }else {
        regOrNot = "1";
        regUnitName = YHRegistProps.getString(YHAuthKeys.REGIST_ORG + postfix);
        regMachineCode = YHRegistProps.getString(YHAuthKeys.MACHINE_CODE + postfix);
//        regUserAmount = YHRegistProps.getString(YHAuthKeys.USER_CNT + postfix);
        regUserAmount = String.valueOf(YHRegistUtility.getUserCnt());
        int userCnt = YHRegistProps.getInt("im.userCnt.yh");
        if (userCnt <= 0) {
          userCnt = 30;
        }
        regImUserAmount = String.valueOf(userCnt);
      }
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      Map<String,String> map = new HashMap<String,String>();
      
      int userAmount = logic.getUserAmount(dbConn);
      int userAmountNotLogin = logic.getUserAmountNotLogin(dbConn);
      Map<String,String> version = logic.getVersion(dbConn);
      
      map.put("regOrNot", regOrNot);
      map.put("userAmount", String.valueOf(userAmount));
      map.put("regUserAmount", regUserAmount);
      map.put("regImUserAmount", regImUserAmount);
      map.put("userAmountNotLogin", String.valueOf(userAmountNotLogin));
      map.putAll(version);
      map.put("systemInfo", getSystemInfo());
      map.put("port", String.valueOf(getPort(request)));
      map.put("serverInfo", String.valueOf(getServerInfo(request)));
      map.put("setupPath", String.valueOf(getSetupPath()));
      map.put("unitName", YHUtility.encodeSpecial(logic.getUnitName(dbConn)));
      map.put("regUnitName", regUnitName);
      map.put("reg" + YHAuthKeys.MACHINE_CODE, regMachineCode);
      map.put(YHAuthKeys.MACHINE_CODE, YHRegistUtility.getMchineCode());
      map.put("serialId", YHRegistProps.getString(YHAuthKeys.SERIAL_ID + ".yh"));
      map.put("remainDays", String.valueOf(YHRegistUtility.remainDays()));
      String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
      if (dbms.equals(YHConst.DBMS_ORACLE)) {
        map.put("dbms", "Oracle");
      }else if (dbms.equals(YHConst.DBMS_MYSQL)) {
        map.put("dbms", "Mysql");
      }else if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        map.put("dbms", "SqlServer");
      }
      
      String installTimeStr = YHSysProps.getString(YHSysPropKeys.INSTALL_TIME);
      String timeStr = null;
      if (!YHUtility.isNullorEmpty(installTimeStr)) {
        timeStr = YHUtility.getDateTimeStr(new Date(Long.parseLong(installTimeStr)));
      }else {
        timeStr = "";
      }
      if (YHUtility.isNullorEmpty(YHRegistProps.getString(YHAuthKeys.REGIST_ORG + ".yh"))) {
        int remainDays = YHRegistUtility.remainDays();
        if (remainDays < 1) {
          timeStr += "&nbsp;软件已过期！";
        }else {
        }
      }
      map.put("installTime", timeStr);
      String data = YHFOM.toJson(map).toString();
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 处理上传的注册文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String reg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String postfix = ".yh";
    Connection dbConn = null;
    YHInfoLogic logic = new YHInfoLogic();
    
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    
    Iterator<String> iKeys = fileForm.iterateFileFields();
    
    String path = null;
    
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      String fileName = fileForm.getFileName(fieldName);
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      String sp = System.getProperty("file.separator");
      path = request.getSession().getServletContext().getRealPath(sp) + YHInfoAct.REG_FILE_PATH + fileName;
      //path = request.getSession().getServletContext().getRealPath(sp) + YHInfoAct.LICENSE_FILE_PATH + fileName;
      fileForm.saveFile(fieldName, path);
    }
    
    try {
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      Map<String, String> registInfo = YHRegistUtility.loadRegist(path);
      
      if (registInfo == null || registInfo.size() < 1) {
        request.setAttribute("rtState", YHConst.RETURN_ERROR);
        request.setAttribute("rtMsg", "加载注册文件失败，请确认注册文件的合法性!");
        return "/core/funcs/system/info/result.jsp";
      }
      
      String regOrg = registInfo.get(YHAuthKeys.REGIST_ORG + postfix);
      String org = logic.getUnitName(dbConn);
      
      YHRegistProps.clear(".yh");
      if (regOrg == null || !regOrg.equals(org)) {
        request.setAttribute("rtState", YHConst.RETURN_ERROR);
        request.setAttribute("rtMsg", "用户单位错误,注册失败!");
        return "/core/funcs/system/info/result.jsp";
      }
      
      String regMachinCode = registInfo.get(YHAuthKeys.MACHINE_CODE + postfix);
      String machinCode = YHRegistUtility.getMchineCode();
      
      if (regMachinCode == null || !regMachinCode.equals(machinCode)) {
        request.setAttribute("rtState", YHConst.RETURN_ERROR);
        request.setAttribute("rtMsg", "机器码错误,注册失败!");
        return "/core/funcs/system/info/result.jsp";
      }
      
      YHRegistProps.addProps(registInfo);
      
      String sn = registInfo.get(YHAuthKeys.SERIAL_ID + postfix);
      int imUserCnt = Integer.parseInt(registInfo.get("im.userCnt" + postfix));
      
      logic.updateSN(dbConn, sn);
      logic.updateIM(dbConn, imUserCnt);
      
      request.setAttribute("rtState", YHConst.RETURN_OK);
      request.setAttribute("rtMsg", "注册成功!");
    } catch (Exception ex) {
      request.setAttribute("rtState", YHConst.RETURN_ERROR);
      request.setAttribute("rtMsg", "登录失败");
      throw ex;
    }
    return "/core/funcs/system/info/result.jsp";
  }
  
  /**
   * 重新载入注册信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String reloadRegInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String postfix = ".yh";
    Connection dbConn = null;
    YHInfoLogic logic = new YHInfoLogic();
    
    try {
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      List<String> registInfo = YHSystemLogic.loadRegistRequires(dbConn);
      String webInfoPath = YHSysProps.getWebInfPath();
      String path = webInfoPath + ""+File.separator+"config"+File.separator+"regist"+File.separator+"yh.regist";
      String disk = webInfoPath.substring(0, 3);
      String sn = registInfo.get(0);
      String unitName = registInfo.get(1);
      
      System.out.println("路径:" + webInfoPath + ""+File.separator+"config"+File.separator+"regist"+File.separator+"yh.regist");
      System.out.println("盘符:" + disk);
      System.out.println("SN:" + sn);
      System.out.println("单位名称:" + unitName);
      
      Map registMap = YHRegistUtility.loadRegist(path, disk, sn, unitName);
      if (registMap != null && registMap.size() > 0) {
        YHRegistProps.clear(".yh");
        YHRegistProps.addProps(registMap);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功");
      }
      else {
        YHRegistProps.clear(".yh");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "请确认注册文件是否正确！");
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "请确认注册文件是否正确！");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查看注册文件中的信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String detailRegInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    try {
      
      String webInfoPath = YHSysProps.getWebInfPath();
      Map registMap = YHRegistUtility.loadRegist(webInfoPath + File.separator + "config" + File.separator + "regist" + File.separator + "yh.regist");
      if (registMap != null && registMap.size() > 0) {
        String data = YHUserinfoAct.toJson(registMap);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_DATA, data);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功");
      }
      else {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "请确认注册文件是否正确！");
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "请确认注册文件是否正确！");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 延长试用天数，增加试用人数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String triaReg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      Iterator<String> iKeys = fileForm.iterateFileFields();
      InputStream is = fileForm.getInputStream((String)fileForm.iterateFileFields().next());
      Map map = fileForm.getParamMap();
      String fileName = "";
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        fileName = fileForm.getFileName(fieldName);
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        String sp = System.getProperty("file.separator");
        String filePath = YHSysProps.getWebInfPath() + File.separatorChar + "config" + File.separatorChar;
        fileForm.saveFile(fieldName, filePath + "patchadeval.properties");
      }
      String patchadeval = YHSysProps.getWebInfPath() + File.separator + "config" + File.separator + "patchadeval.properties";
      YHSysProps.addProps(YHConfigLoader.loadSysProps(patchadeval));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "增加试用人数延长试用期限成功！");
      request.setAttribute("rtMsg", "增加试用人数延长试用期限成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "请确认授权文件是否正确！");
      request.setAttribute("rtMsg", "请确认授权文件是否正确！");
      throw ex;
    }
    return "/core/funcs/system/info/result.jsp";
  }
  
  /**
   * 获取安装路径
   * @return
   */
  private String getSetupPath() {
    
    return YHUtility.encodeSpecial(YHSysProps.getRootPath());
  }
  
  private String getSystemInfo() {
    return System.getProperty("os.name") + " " + System.getProperty("os.arch");
  }
  
  private int getPort(HttpServletRequest request) {
    int port = request.getServerPort();
    return port;
  }
  
  private String getDBMS() {
    String dbms = YHSysProps.getString("db.jdbc.dbms");
    return dbms;
  }
    
  private String getServerInfo(HttpServletRequest request) {
    ServletContext application = request.getSession().getServletContext();
    String serverInfo = application.getServerInfo();
    return serverInfo;
  }
  
}
