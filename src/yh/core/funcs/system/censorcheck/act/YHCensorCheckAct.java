package yh.core.funcs.system.censorcheck.act;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.mobilesms.act.YHMobileSms2Act;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.censorcheck.data.YHCensorCheck;
import yh.core.funcs.system.censorcheck.logic.YHCensorCheckLogic;
import yh.core.funcs.system.censorwords.logic.YHCensorWordsLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;

public class YHCensorCheckAct {
  private static Logger log = Logger.getLogger(YHCensorCheckAct.class);
  
  /**
   * 判段id是不是在str里面
   * @param str
   * @param id
   * @return
   */
  public static boolean findId(String str, String id) {
    if(str == null || id == null || "".equals(str) || "".equals(id)){
      return false;
    }
    String[] aStr = str.split(",");
    for(String tmp : aStr){
      if(tmp.equals(id)){
        return true;
      }
    }
    return false;
  }
  /**
   * 查询CENSOR_WORDS表中信息
   * 不良词语：find
   * 替换词语：replacement
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCensorWords(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"1=1"};
      List funcList = new ArrayList();
      funcList.add("oaExamineWords");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_EXAMINE_WORDS"));
      if(list.size() > 1){
        for(Map ms : list){
          sb.append("{");
          sb.append("find:\"" + ms.get("find") + "\"");
          sb.append(",replacement:\"" + ms.get("replacement") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map ms : list){
          sb.append("{");
          sb.append("find:\"" + ms.get("find") + "\"");
          sb.append(",replacement:\"" + ms.get("replacement") + "\"");
          sb.append("}");
        }
      }    
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查询EMAIL_BODY表中信息
   * 
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCensorEmailBody(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
      String bodyId = request.getParameter("bodyId");
      int seqIdBody = Integer.parseInt(bodyId);
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"SEQ_ID=" + seqIdBody};
      List funcList = new ArrayList();
      funcList.add("emailBody");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_EMAIL_BODY"));
      for(Map ms : list){
        String subject = (String) ms.get("subject");
        subject = YHUtility.encodeSpecial(subject);
        sb.append("{");
        sb.append("copyToId:\"" + (ms.get("copyToId") == null ? "" : ms.get("copyToId")) + "\"");
        sb.append(",attachmentId:\"" + (ms.get("attachmentId") == null ? "" : ms.get("attachmentId")) + "\"");
        sb.append(",attachmentName:\"" + (ms.get("attachmentName") == null ? "" : ms.get("attachmentName")) + "\"");
        sb.append(",toId:\"" + (ms.get("toId") == null ? "" : ms.get("toId")) + "\"");
        sb.append(",subject:\"" + subject + "\"");
        sb.append(",important:\"" + ms.get("important") + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1); 
      if(list.size() == 0){
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb+"IOU");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 把用户ID转换成对映的姓名（包含串 例如：196，201  转换成对映的姓名）
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserNameStrs(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    String userNameStrs = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
      String idStrs = request.getParameter("idStrs");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      if(!YHUtility.isNullorEmpty(idStrs)){
        userNameStrs = ccl.getUserId(dbConn, idStrs);
      }
      StringBuffer sb = new StringBuffer("[");
      sb.append("{");
      sb.append("userNameStrs:\"" + userNameStrs + "\"");
      sb.append("}");
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getCensorBanned(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    String bannedHint = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
      String moduleCode = request.getParameter("moduleCode");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      bannedHint = ccl.getBannedHint(dbConn, moduleCode);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/system/censorcheck/email/banned.jsp?bannedHint="+bannedHint;
  }
  
  public String getCensorMod(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    String modHint = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
//      ArrayList<YHCensorCheck> checkList = null;
//      YHCensorCheckLogic ccld = new YHCensorCheckLogic();
//      checkList = ccld.getCensorConetentJson(dbConn);
//      for(int i = 0; i < checkList.size(); i++){
//        YHCensorCheck a = checkList.get(i);
//        System.out.println(a);
//      }
      String moduleCode = request.getParameter("moduleCode");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      modHint = ccl.getModHint(dbConn, moduleCode);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/system/censorcheck/email/mod.jsp?modHint="+modHint;
  }
  
  /**
   * 内部邮件审核:待审核邮件查询列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCensorContentJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    ArrayList<YHCensorCheck> checkList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
      StringBuffer sb = new StringBuffer("[");
      String moduleCode = request.getParameter("moduleCode");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      checkList = ccl.getCensorConetentJson(dbConn, moduleCode);
      for(int i = 0; i < checkList.size(); i++){
        YHCensorCheck a = checkList.get(i);
        sb.append("{");
        sb.append("seqId:\"" + a.getSeqId() + "\"");
        sb.append(","+"cont:"+a.getContent());
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (checkList.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb+">>>>>>>>>>");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 内部短信审核:通过审核，发短信
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPassEmailJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    ArrayList<YHCensorCheck> checkList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
      StringBuffer sb = new StringBuffer("[");
      String seqIdStr = request.getParameter("seqId");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      checkList = ccl.getPassEmailJson(dbConn, seqIdStr);
      for(int i = 0; i < checkList.size(); i++){
        YHCensorCheck a = checkList.get(i);
        sb.append("{");
        sb.append("seqId:\"" + a.getSeqId() + "\"");
        sb.append(","+"cont:"+a.getContent());
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (checkList.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb+">>>>>>>>>>");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 内部短信审核:待审核邮件查询列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCensorContentSmsJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    ArrayList<YHCensorCheck> checkList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
      StringBuffer sb = new StringBuffer("[");
      String moduleCode = request.getParameter("moduleCode");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      checkList = ccl.getCensorConetentSmsJson(dbConn, moduleCode);
      for(int i = 0; i < checkList.size(); i++){
        YHCensorCheck a = checkList.get(i);
        sb.append("{");
        sb.append("seqId:\"" + a.getSeqId() + "\"");
        sb.append(","+"cont:"+a.getContent());
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (checkList.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb+">>>>>>>>>>");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 内部短信审核:通过审核，发短信
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPassSmsJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    ArrayList<YHCensorCheck> checkList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
      StringBuffer sb = new StringBuffer("[");
      String seqIdStr = request.getParameter("seqId");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      checkList = ccl.getPassSmsJson(dbConn, seqIdStr);
      for(int i = 0; i < checkList.size(); i++){
        YHCensorCheck a = checkList.get(i);
        sb.append("{");
        sb.append("seqId:\"" + a.getSeqId() + "\"");
        sb.append(","+"cont:"+a.getContent());
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (checkList.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb+">>>>>>>>>>");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 手机短信审核:待审核邮件查询列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCensorContentMobilSmsJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    ArrayList<YHCensorCheck> checkList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
      StringBuffer sb = new StringBuffer("[");
      String moduleCode = request.getParameter("moduleCode");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      checkList = ccl.getMobilSmsJson(dbConn, moduleCode);
      for(int i = 0; i < checkList.size(); i++){
        YHCensorCheck a = checkList.get(i);

        sb.append("{");
        sb.append("seqId:\"" + a.getSeqId() + "\"");
        sb.append(","+"cont:"+ a.getContent());
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (checkList.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb+">>>>>>>>>>");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 手机短信审核:通过审核，发短信
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPassMobileSmsJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    ArrayList<YHCensorCheck> checkList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
      StringBuffer sb = new StringBuffer("[");
      String seqIdStr = request.getParameter("seqId");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      checkList = ccl.getPassSmsJson(dbConn, seqIdStr);
      for(int i = 0; i < checkList.size(); i++){
        YHCensorCheck a = checkList.get(i);
        sb.append("{");
        sb.append("seqId:\"" + a.getSeqId() + "\"");
        sb.append(","+"cont:"+a.getContent());
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (checkList.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb+">>>>>>>>>>");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
 /**
  * 条件查询后的列表
  * 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
  
  public String getCensorContentJson2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    ArrayList<YHCensorCheck> checkList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //int seqId = person.getSeqId();
      userName = person.getUserName();
      String censorFlag = request.getParameter("censorFlag");
      String checkEnd = request.getParameter("checkEnd");
      String checkBegin = request.getParameter("checkBegin");
      String fromId = request.getParameter("fromId");
      String toId = request.getParameter("toId");
      String subject = request.getParameter("subject");
      String content = request.getParameter("content");
      String endDate = request.getParameter("endDate");
      String beginDate = request.getParameter("beginDate");
      String moduleCode = request.getParameter("moduleCode");
      String limit = request.getParameter("limit");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      //if(censorFlag.equals("")){
        //checkList = ccl.getCensorConetentJsonAll(dbConn, censorFlag);
      //}else{
        checkList = ccl.getCensorConetentJson2(dbConn, censorFlag, fromId, checkEnd, checkBegin, subject, content, toId, beginDate, endDate, moduleCode, limit);
      //}
      String checkTime = "";
      StringBuffer sb = null;
      sb = new StringBuffer("[");
      for(int i = 0; i < checkList.size(); i++){
        
        YHCensorCheck a = checkList.get(i);
        if (a.getCheckTime() != null) { 
          checkTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getCheckTime()); 
        } 
        String sendTime = checkTime == null ? "" : checkTime;
        sb.append("{");
        sb.append("seqId:\"" + a.getSeqId() + "\"");
        sb.append(",censorFlag:\"" + a.getCensorFlag() + "\"");
        sb.append(",checkUser:\"" + a.getCheckUser() + "\"");
        sb.append(",checkTime:\"" + sendTime + "\"");
        sb.append(","+"cont:"+a.getContent());
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (checkList.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb+"?????????");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 内部短信：条件查询后的列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getSmsSearchJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    ArrayList<YHCensorCheck> checkList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //int seqId = person.getSeqId();
      userName = person.getUserName();
      String censorFlag = request.getParameter("censorFlag");
      String checkEnd = request.getParameter("checkEnd");
      String checkBegin = request.getParameter("checkBegin");
      String fromId = request.getParameter("fromId");
      String toId = request.getParameter("toId");
      String content = request.getParameter("content");
      String endDate = request.getParameter("endDate");
      String beginDate = request.getParameter("beginDate");
      String moduleCode = request.getParameter("moduleCode");
      String limit = request.getParameter("limit");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      //if(censorFlag.equals("")){
        //checkList = ccl.getCensorConetentJsonAll(dbConn, censorFlag);
      //}else{
        checkList = ccl.getCensorConetentJsonSms(dbConn, censorFlag, fromId, checkBegin, checkEnd, content, toId, beginDate, endDate, moduleCode, limit);
      //}
      String checkTime = "";
      StringBuffer sb = null;
      sb = new StringBuffer("[");
      for(int i = 0; i < checkList.size(); i++){
        
        YHCensorCheck a = checkList.get(i);
        if (a.getCheckTime() != null) { 
          checkTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getCheckTime()); 
        } 
        String sendTime = checkTime == null ? "" : checkTime;
        sb.append("{");
        sb.append("seqId:\"" + a.getSeqId() + "\"");
        sb.append(",censorFlag:\"" + a.getCensorFlag() + "\"");
        sb.append(",checkUser:\"" + a.getCheckUser() + "\"");
        sb.append(",checkTime:\"" + sendTime + "\"");
        sb.append(","+"cont:"+a.getContent());
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (checkList.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb+"?????????");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 手机短信：条件查询后的列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getMobilSmsSearchJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    ArrayList<YHCensorCheck> checkList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //int seqId = person.getSeqId();
      userName = person.getUserName();
      String censorFlag = request.getParameter("censorFlag");
      String checkEnd = request.getParameter("checkEnd");
      String checkBegin = request.getParameter("checkBegin");
      String fromId = request.getParameter("fromId");
      String toId = request.getParameter("toId");
      String content = request.getParameter("content");
      String phone1 = request.getParameter("phone1");
      String endDate = request.getParameter("endDate");
      String beginDate = request.getParameter("beginDate");
      String moduleCode = request.getParameter("moduleCode");
      String limit = request.getParameter("limit");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      //if(censorFlag.equals("")){
        //checkList = ccl.getCensorConetentJsonAll(dbConn, censorFlag);
      //}else{
        checkList = ccl.getCensorConetentJsonMobilSms(dbConn, censorFlag, fromId, checkBegin, checkEnd, content, toId, beginDate, endDate, moduleCode, limit, phone1);
      //}
      String checkTime = "";
      StringBuffer sb = null;
      sb = new StringBuffer("[");
      for(int i = 0; i < checkList.size(); i++){
        
        YHCensorCheck a = checkList.get(i);
        if (a.getCheckTime() != null) { 
          checkTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(a.getCheckTime()); 
        } 
        String sendTime = checkTime == null ? "" : checkTime;
        sb.append("{");
        sb.append("seqId:\"" + a.getSeqId() + "\"");
        sb.append(",censorFlag:\"" + a.getCensorFlag() + "\"");
        sb.append(",checkUser:\"" + a.getCheckUser() + "\"");
        sb.append(",checkTime:\"" + sendTime + "\"");
        sb.append(","+"cont:"+a.getContent());
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (checkList.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb+"?????????");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteCensor(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
    
      String sumStrs = request.getParameter("sumStrs");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      ccl.deleteAll(dbConn, sumStrs);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String checkPassCensor(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      
      Calendar cal = Calendar.getInstance();        
      java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        
      String cdate = sdf.format(cal.getTime());  
    
      String sumStrs = request.getParameter("sumStrs");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      ccl.passAll(dbConn, sumStrs, seqId, cdate);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String checkDenyCensor(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
    
      Calendar cal = Calendar.getInstance();        
      java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        
      String cdate = sdf.format(cal.getTime()); 
      String sumStrs = request.getParameter("sumStrs");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      ccl.denyAll(dbConn, sumStrs, seqId, cdate);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getcheckDenyCensor(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
    
      String idStrs = request.getParameter("idStrs");
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      //System.out.println(idStrs+"TTT");
      String[] filters = new String[] { "SEQ_ID=" + idStrs };
      List funcList = new ArrayList();
      funcList.add("censorData");
      map = (HashMap) orm.loadDataSingle(dbConn, funcList, filters);
      List<Map> list = (List<Map>) map.get("OA_EXAMINE_DATA");
      for (Map m : list) {
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",censorFlag:\"" + m.get("censorFlag") + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]");
      //System.out.println(sb+"YYY");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getCensorChangeName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String formId = request.getParameter("formId");
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String userNum = "";
      String[] userSum = formId.split(",");
   
      String[] filters = new String[] { "SEQ_ID IN (" + formId +")"};
      List funcList = new ArrayList();
      funcList.add("person");
      map = (HashMap) orm.loadDataSingle(dbConn, funcList, filters);
      List<Map> list = (List<Map>) map.get("PERSON");
      for (Map m : list) {
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",userName:\"" + m.get("userName") + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getCensorChangeName2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String formId = request.getParameter("formId");
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String userNum = "";
      String[] userSum = formId.split(",");
      String[] filters = new String[] { "SEQ_ID=" + formId };
      List funcList = new ArrayList();
      funcList.add("person");
      map = (HashMap) orm.loadDataSingle(dbConn, funcList, filters);
      List<Map> list = (List<Map>) map.get("PERSON");
      for (Map m : list) {
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",userName:\"" + m.get("userName") + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]");
      //System.out.println(sb + "RRR");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateMore99Words(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
     
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic cwLogic = new YHCensorWordsLogic();
      String find = request.getParameter("find");
      String replacement = request.getParameter("replacement");
      List<Map> list = new ArrayList();
      YHORM t = new YHORM();
      Map m =new HashMap();
      m.put("find", find);
      m.put("replacement", replacement);
      
      String censorVal = request.getParameter("censorVal");
      //String censorSum[] = censorVal.split(",");
     // String findStr = "";
     // for(int x = 0; x < censorSum.length; x++){
      //  findStr = censorSum[x];
     // }
      if(cwLogic.existsCensorWords(dbConn, find)){
        //request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        //return "/core/funcs/system/censorwords/new/import.jsp?find="+find+"&replacement="+replacement;
        
        YHORM orm = new YHORM();
        HashMap map = null;
        StringBuffer sb = new StringBuffer("[");
        String[] filters = new String[]{"FIND="+find};
        List funcList = new ArrayList();
        funcList.add("censorWords");
        map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
        list.addAll((List<Map>) map.get("OA_EXAMINE_WORDS"));
        
        if(list.size() > 1){
          for(Map ms : list){
            sb.append("{");
            sb.append("find:\"" + ms.get("find") + "\"");
            sb.append(",replacement:\"" + ms.get("replacement") + "\"");
            sb.append("},");
          }
          sb.deleteCharAt(sb.length() - 1); 
        }else{
          for(Map ms : list){
            sb.append("{");
            sb.append("find:\"" + ms.get("find") + "\"");
            sb.append(",replacement:\"" + ms.get("replacement") + "\"");
            sb.append("}");
          }
        }    
        sb.append("]");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
        request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      }else{
        t.saveSingle(dbConn, "censorWords", m);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
      }
      //t.updateSingle(dbConn, "censorWords", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/system/censorwords/new/import.jsp";
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateMore0Words(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic cwLogic = new YHCensorWordsLogic();
      String find = request.getParameter("find");
      String replacement = request.getParameter("replacement");
      List<Map> list = new ArrayList();
      String findStrs = "";
      String replacementStrs = "";
      String sumStr = "";
      YHORM t = new YHORM();
      Map m =new HashMap();
      StringBuffer sb = new StringBuffer("[");
      String censorVal = request.getParameter("find");
      YHORM orm = new YHORM();
      HashMap map = null;
      String censorSum[] = censorVal.split(",");
      String findStr = "";
      String finds = "";
      int okCount = 0;
      int errCount = 0;
      String replacements = "";
      for(int x = 0; x < censorSum.length; x++){
        findStr = censorSum[x];
        if(censorSum[x].indexOf("=")!=-1){
          finds = censorSum[x].substring(0,censorSum[x].indexOf("="));
          replacements = censorSum[x].substring(censorSum[x].indexOf("=")+1, censorSum[x].length());
        }else{
          finds = findStr;
          replacements = "";
        }
        if(cwLogic.existsCensorWords(dbConn, finds)){
          errCount++;
          findStrs += finds + ",";
          replacementStrs += replacements + ",";
          if(!YHUtility.isNullorEmpty(replacements)){
            sumStr += finds+"="+replacements+",";
          }else{
            sumStr += finds;
          }
          continue;
        }else{
          okCount++;
          m.put("find", finds);
          m.put("replacement", replacements);
          m.put("userId", userId);
          t.saveSingle(dbConn, "censorWords", m);
        }
      }
      //System.out.println(sumStr+"YYYYYYYYYYYYY");
      String str[] = sumStr.split(",");
      String reStr = "";
      for(int i = 0; i < str.length; i++){
        reStr = str[i];
        sb.append("{");
        sb.append("find:\"" + reStr + "\"");
        sb.append(",errCount:\"" + errCount + "\"");
        sb.append(",okCount:\"" + okCount + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1); 
      sb.append("]");
      //System.out.println(sb);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/system/censorwords/new/import.jsp";
    return "/core/inc/rtjson.jsp";
  }
  
  public String addJsonContentTest(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String jsonStr = request.getParameter("jsonStr");
      String moduleCode = request.getParameter("moduleCode");

      Map m =new HashMap();
      m.put("jsonStr", jsonStr);
      m.put("moduleCode", moduleCode);
      YHORM checkCensor = new YHORM();
      checkCensor.saveSingle(dbConn, "addressGroup", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"添加成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 
   * @param dbConn
   * @param moduleCode  模块编号：0-邮件；1-短信；2-手机短信
   * @param jsonStr     邮件/短信/手机短信 的json数据
   */
  
  public void addJsonContent(Connection dbConn, String moduleCode, String jsonStr){
    try {
      Map m =new HashMap();
      m.put("jsonStr", jsonStr);
      m.put("moduleCode", moduleCode);
      YHORM checkCensor = new YHORM();
      checkCensor.saveSingle(dbConn, "censorData", m);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
  public String sendMailByCoren(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String bodyId = request.getParameter("bodyId");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      ccl.sendMailByCoren(dbConn, bodyId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"邮件发送成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String doSmsBack(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String content = request.getParameter("content");
      int fromId = Integer.parseInt(request.getParameter("formId"));
      String toId = request.getParameter("toId");
      String type = request.getParameter("type");
      String remindUrl = request.getParameter("remindUrl");
      
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      ccl.doSmsBack(dbConn, content, fromId, toId, type, remindUrl);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"短信发送成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String doEmailBack(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String content = request.getParameter("content");
      int fromId = Integer.parseInt(request.getParameter("formId"));
      String toId = request.getParameter("toId");
      String emailBodyId = request.getParameter("emailBodyId");
      String type = "2";
      String remindUrl = "/core/funcs/email/inbox/read_email/index.jsp?&seqId=" + emailBodyId ;
      
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      ccl.doSmsBack(dbConn, content, fromId, toId, type, remindUrl);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"短信发送成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String doMobileSmsBack(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String content = request.getParameter("content");
      String fromId = request.getParameter("formId");
      String toId = request.getParameter("toId");
      String sendTime = request.getParameter("sendTime");
      String phone = request.getParameter("phone");
      
      Map map = request.getParameterMap();
      YHMobileSms2Act.addMobileSms(dbConn, map);
      //YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      //ccl.doMobileSmsBack(dbConn, map);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"短信发送成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 是否用内部短信提醒
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getSmsRemind(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String data = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);

      String moduleCode = request.getParameter("moduleCode");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      data = ccl.getSmsRemindLogic(dbConn, moduleCode);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 是否对信息过滤有审核权限
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCheckUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String data = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int loginUserId = person.getSeqId();

      String moduleCode = request.getParameter("moduleCode");
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      data = ccl.getCheckUserLogic(dbConn, moduleCode);
      String isPriv = "0";
      if(!findId(data, String.valueOf(loginUserId))){
        isPriv = "1";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + isPriv + "\"");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getCensorEmailContent(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    ArrayList<YHCensorCheck> checkList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      StringBuffer sb = new StringBuffer("[");
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHCensorCheckLogic ccl = new YHCensorCheckLogic();
      checkList = ccl.getCensorEmailContent(dbConn, seqId);
      for(int i = 0; i < checkList.size(); i++){
        YHCensorCheck a = checkList.get(i);
        sb.append("{");
        sb.append("seqId:\"" + a.getSeqId() + "\"");
        sb.append(","+"cont:"+a.getContent());
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      if (checkList.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb+">>>>>>>>>>");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
