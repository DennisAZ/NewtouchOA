package yh.core.funcs.mobilesms.act;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.mobilesms.data.YHSms2;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.censorcheck.logic.YHCensorCheckLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHMobileSms2Act {
  private YHMobileSms2Logic logic = new YHMobileSms2Logic();
  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
  
  public String queryOutPriv(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      Map<String,String> map = this.logic.queryOutPriv(dbConn, user.getSeqId(), user.getDeptId());
      
      String outPriv = map.get("OUT_PRIV");
      
      String userIdStr = "," + user.getSeqId() + ",";
      map.put("OUT_PRIV", "");
      if (!YHUtility.isNullorEmpty(outPriv)) {
        outPriv = "," + outPriv + ",";
        if (outPriv.contains(userIdStr)) {
          map.put("OUT_PRIV", "1");
        }
      }
      
      request.setAttribute(YHActionKeys.RET_DATA, YHFOM.toJson(map).toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String addSms(HttpServletRequest request, HttpServletResponse response) throws Exception{

    Connection dbConn = null;
    try {
      
      String sendFlag = request.getParameter("sendFlag");
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      if (sendFlag == null){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "未传递发送类型标识!");
        return "/core/funcs/mobilesms/new/success.jsp";
      }
      
      if ("0".equals(sendFlag)){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "信息含有敏感词!");
        return "/core/funcs/mobilesms/new/success.jsp";
      }
      
      List<String> userNoMobile = new ArrayList<String>();
      
      Map map = request.getParameterMap();
      YHSms2 sms = (YHSms2)YHFOM.build(map,YHSms2.class, "");
      
      sms.setSendFlag("0");
      sms.setFromId(String.valueOf(user.getSeqId()));
      
      String toUser = request.getParameter("user");
      String outUser = request.getParameter("outUser");
      
      if(toUser != null && !"".equals(toUser)){
        for (String s : toUser.split(",")){
          String mobileNo = this.logic.queryMobileNo(dbConn, Integer.parseInt(s));
          
          if ("2".equals(sendFlag)){
            request.setAttribute("MOD", this.logic.getModHint(dbConn, "2"));
          }
          
          if (mobileNo == null || "".equals(mobileNo.trim())){
            userNoMobile.add(s);
          }
          else{
            sms.setPhone(mobileNo);
            
            StringBuffer sb = new StringBuffer();
            sb.append("{FROM_ID:");
            sb.append(sms.getFromId());
            sb.append(",TO_ID:\"");
            sb.append(s);
            sb.append("\",PHONE:\"");
            sb.append(sms.getPhone());
            sb.append("\",SEND_TIME:\"");
            sb.append(YHUtility.getCurDateTimeStr());
            sb.append("\"");
            sb.append(",CONTENT:\"");
            sb.append(sms.getContent().replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", ""));
            sb.append("\"}");
            
            if ("1".equals(sendFlag)){
              //YHCensorCheckLogic.addJsonContent(dbConn, "2", sb.toString(), user.getSeqId());
              this.logic.addSms(dbConn, sms);
            }
            else if ("2".equals(sendFlag)){
              YHCensorCheckLogic.addJsonContent(dbConn, "2", sb.toString(), user.getSeqId());
            }
          }
        }
      }
      
      if(outUser != null && !"".equals(outUser)){
        for (String s : outUser.split(",")){
          sms.setPhone(s);
          if ("1".equals(sendFlag)){
            this.logic.addSms(dbConn, sms);
          }
          else if ("2".equals(sendFlag)){
            StringBuffer sb = new StringBuffer();
            sb.append("{FROM_ID:");
            sb.append(sms.getFromId());
            sb.append(",PHONE1:\"");
            sb.append(sms.getPhone());
            sb.append("\",SEND_TIME:\"");
            sb.append(YHUtility.getCurDateTimeStr());
            sb.append("\"");
            sb.append(",CONTENT:\"");
            sb.append(sms.getContent().replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", ""));
            sb.append("\"}");
            YHCensorCheckLogic.addJsonContent(dbConn, "2", sb.toString(), user.getSeqId());
            request.setAttribute("MOD", this.logic.getModHint(dbConn, "2"));
          }
        }
      }
      
      if (userNoMobile.size() > 0){
        String data = "";
        for(String s : userNoMobile){
          data += this.logic.queryUserName(dbConn, Integer.parseInt(s)) + ",";
        }
        request.setAttribute("MESSAGE", data);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功增加短信");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/funcs/mobilesms/new/success.jsp";
  }

  /** 
  * 短信发送查询
  * @param request 
  * @param response 
  * @return 
  * @throws Exception 
  */ 
  public String listSendSms(HttpServletRequest request, 
  HttpServletResponse response) throws Exception { 

    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      String content = request.getParameter("content");
      
      
      if (content != null){
        content = URLDecoder.decode(content, "utf-8");
      }
      
      content = YHUtility.encodeLike(content);
      
      String sendFlag = request.getParameter("sendFlag");
      String phone = request.getParameter("phone");
      
      if(beginTime != null && !"".equals(beginTime)){
        beginTime = " and " + YHDBUtility.getDateFilter("SEND_TIME", beginTime, ">=");
      }
      
      if(endTime != null && !"".equals(endTime)){
        beginTime = " and " + YHDBUtility.getDateFilter("SEND_TIME", endTime, "<=");
      }
      
      if(sendFlag != null && !"".equals(sendFlag)){
        if ("ALL".equals(sendFlag)){
          sendFlag = "";
        }
        else{
          sendFlag = " and SEND_FLAG = '" + sendFlag + "'";
        }
      }
      
      if(phone != null && !"".equals(phone)){
        phone = " and PHONE like '%" + phone + "%'";
      }
      
      content = " and CONTENT like '%" + content + "%'";
        
      String sql = null;
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("sqlserver")) {
        sql = "select top 500 SEQ_ID" +
        ",(select max(USER_NAME) from PERSON where MOBIL_NO = PHONE) as USER_NAME" +
        ",PHONE" +
        ",CONTENT" +
        ",SEND_TIME" +
        ",SEND_FLAG" +
        ",FROM_ID" +
        " from oa_msg2" +
        " where" +
        " FROM_ID = '" + user.getSeqId() + "'" +
        beginTime +
        endTime +
        sendFlag +
        phone +
        " order by SEND_TIME desc";
      }else if (dbms.equals("mysql")){
        sql = "select SEQ_ID" +
          ",(select max(USER_NAME) from PERSON where MOBIL_NO = PHONE) as USER_NAME" +
          ",PHONE" +
          ",CONTENT" +
          ",SEND_TIME" +
          ",SEND_FLAG" +
          ",FROM_ID" +
          " from oa_msg2" +
          " where" +
          " FROM_ID = '" + user.getSeqId() + "'" +
          beginTime +
          endTime +
          sendFlag +
          phone +
          content +
          " order by SEND_TIME desc limit 500";
      }else if (dbms.equals("oracle")) {
        sql = "select SEQ_ID" +
          ",(select max(USER_NAME) from PERSON where MOBIL_NO = PHONE) as USER_NAME" +
          ",PHONE" +
          ",CONTENT" +
          ",SEND_TIME" +
          ",SEND_FLAG" +
          ",FROM_ID" +
          " from oa_msg2" +
          " where" +
          " FROM_ID = '" + user.getSeqId() + "'" +
          beginTime +
          endTime +
          sendFlag +
          phone +
          content +
          " and ROWNUM <= 500" +
          " order by SEND_TIME desc";
      }
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap());
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql); 
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 

      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
  
  /** 
   * 短信发送查询
   * @param request 
   * @param response 
   * @return 
   * @throws Exception 
   */ 
  public String deleteBatch(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 
    
    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      String content = request.getParameter("content");
      
      content = YHUtility.encodeLike(content);
      
      String sendFlag = request.getParameter("sendFlag");
      String phone = request.getParameter("phone");
      
      if(beginTime != null && !"".equals(beginTime)){
        beginTime = " and " + YHDBUtility.getDateFilter("SEND_TIME", beginTime, ">=");
      }
      
      if(endTime != null && !"".equals(endTime)){
        beginTime = " and " + YHDBUtility.getDateFilter("SEND_TIME", endTime, "<=");
      }
      
      if(sendFlag != null && !"".equals(sendFlag)){
        if ("ALL".equals(sendFlag)){
          sendFlag = "";
        }
        else{
          sendFlag = " and SEND_FLAG = '" + sendFlag + "'";
        }
      }
      
      if(phone != null && !"".equals(phone)){
        phone = " and PHONE like '%" + phone + "%'";
      }
      
      content = " and CONTENT like '%" + content + "%'";
      
      String sql = "delete" +
      " from oa_msg2" +
      " where" +
      " FROM_ID = '" + 
      user.getSeqId() +
      "'" +
      beginTime +
      endTime +
      sendFlag +
      phone +
      content;
      
      this.logic.excuteSql(dbConn, sql);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功!"); 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
    return "/core/funcs/mobilesms/sendManage/success.jsp"; 
  }
  
  /** 
   * 短信接收查询
   * @param request 
   * @param response 
   * @return 
   * @throws Exception 
   */ 
  public String listReceiveSms(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 
    
    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      String content = request.getParameter("content");
      String fromId = request.getParameter("fromId");      
      String phone = user.getMobilNo();
      
      
      if (content != null){
        content = URLDecoder.decode(content, "utf-8");
      }
      
      content = YHUtility.encodeLike(content);
      
      if(phone != null && !"".equals(phone)) {
        phone = " and PHONE = '" + phone + "'";
      }else {
        phone = "";
      }
      
      if(beginTime != null && !"".equals(beginTime)){
        beginTime = " and " + YHDBUtility.getDateFilter("SEND_TIME", beginTime, ">=");
      }
      
      if(endTime != null && !"".equals(endTime)){
        beginTime = " and " + YHDBUtility.getDateFilter("SEND_TIME", endTime, "<=");
      }
      
      if(fromId != null && !"".equals(fromId)){
        String result = "";
        for(String s : fromId.split(",")){
          s = "'" + s + "'";
          result += s;
        }
        result = result.replaceAll("''", "','");
        fromId = " and FROM_ID in (" + result + ")";
      }
      
      content = " and CONTENT like '%" + content + "%'";
      
      String sql = "select SEQ_ID" +
        ",(select USER_NAME from PERSON where SEQ_ID = FROM_ID) as USER_NAME" +
        ",CONTENT" +
        ",SEND_TIME" +
        ",SEND_FLAG" +
        " from oa_msg2" +
        " where" +
        " SEND_FLAG = '1'" + 
        phone +
        beginTime +
        endTime +
        content +
        fromId +
        " and ROWNUM <= 500" +
        " order by SEND_TIME desc";
      
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("sqlserver")) {
        sql = "select top 500 SEQ_ID" +
          ",(select USER_NAME from PERSON where SEQ_ID = FROM_ID) as USER_NAME" +
          ",CONTENT" +
          ",SEND_TIME" +
          ",SEND_FLAG" +
          " from oa_msg2" +
          " where" +
          " SEND_FLAG = '1'" + 
          phone +
          beginTime +
          endTime +
          content +
          fromId +
          " order by SEND_TIME desc";
      }else if (dbms.equals("mysql")){
        sql = "select SEQ_ID" +
          ",(select USER_NAME from PERSON where SEQ_ID = FROM_ID) as USER_NAME" +
          ",CONTENT" +
          ",SEND_TIME" +
          ",SEND_FLAG" +
          " from oa_msg2" +
          " where" +
          " SEND_FLAG = '1'" + 
          phone +
          beginTime +
          endTime +
          content +
          fromId +
          " order by SEND_TIME desc limit 500";
      }
      
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap());
      
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql); 
      
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 
      
      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
  
  /** 
   * 删除短信
   * @param request 
   * @param response 
   * @return 
   * @throws Exception 
   */ 
  public String deleteSms(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 
    
    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      
      String seqIdStr = request.getParameter("seqIdStr");
      
      if(seqIdStr != null && !"".equals(seqIdStr)){
        for(String s : seqIdStr.split(",")){
          this.logic.deleteSms(dbConn, user.getSeqId(), Integer.parseInt(s));
        }
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp"; 
  }

  /**
   * 添加手机短信的方法
   * @param dbConn
   * @param map               通过调用request.getparameterMap()方法得到的参数map
   * @throws Exception 
   */
  public static void addMobileSms(Connection dbConn, Map<String,String[]> map) throws NumberFormatException, Exception{
    YHSms2 sms2 = new YHSms2();
    
    sms2.setSendFlag("0");
    
    if (map.containsKey("content")){
      sms2.setContent(map.get("content")[0]);
    }
    
    if (map.containsKey("fromId")){
      sms2.setFromId(map.get("fromId")[0]);
    }
    
    if (map.containsKey("phone")){
      sms2.setPhone(map.get("phone")[0]);
    }
    
    if (map.containsKey("sendTime")){
      try{
        sms2.setSendTime(toDate(map.get("sendTime")[0], DATE_FORMAT));
      }catch(ParseException e){
        sms2.setSendTime(null);
      }
    }
    
    if (map.containsKey("toId")){
      String toId = map.get("toId")[0];
      if (toId != null && !"".equals(toId)){
        String mobileNo = YHMobileSms2Logic.queryMobileNo(dbConn, Integer.parseInt(toId));
        if (mobileNo != null && !"".equals(mobileNo.trim())){
          sms2.setPhone(mobileNo);
        }
      }
    }
    
    String phone = sms2.getPhone();
    if (!YHUtility.isNullorEmpty(phone)){
      YHMobileSms2Logic.addSms(dbConn, sms2);
    }
  }
  
  public static Date toDate(String dateStr, String dateFormat) throws ParseException{
    SimpleDateFormat df = new SimpleDateFormat(dateFormat);
    return df.parse(dateStr);
  }
  
  private StringBuffer toJson(Map<String,String> m) throws Exception{
    StringBuffer sb = new StringBuffer("{");
    for (Iterator<Entry<String,String>> it = m.entrySet().iterator(); it.hasNext();){
      Entry<String,String> e = it.next();
      sb.append(e.getKey());
      sb.append(":\"");
      sb.append(e.getValue());
      sb.append("\",");
    }
    if (sb.charAt(sb.length() - 1) == ','){
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("}");
    return sb;
  }
}