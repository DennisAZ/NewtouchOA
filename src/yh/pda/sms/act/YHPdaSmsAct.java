package yh.pda.sms.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.pda.sms.data.YHPdaSms;

public class YHPdaSmsAct {

  public void doint(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      List<YHPdaSms> list = new ArrayList<YHPdaSms>();
      
      //查询日程
      String sql = " SELECT sms.SEQ_ID, FROM_ID, SEND_TIME, SMS_TYPE, CONTENT, USER_NAME "
                 + " from sms,oa_msg_body as sms_body,person "
                 + " where sms.BODY_SEQ_ID=sms_body.SEQ_ID "
                 + " and person.SEQ_ID=sms_body.FROM_ID "
                 + " and TO_ID=" + person.getSeqId()
                 + " and " + YHDBUtility.getDateFilter("SEND_TIME", YHUtility.getDateTimeStr(null), "<=")
                 + " and REMIND_FLAG!='0' "
                 + " and DELETE_FLAG!='1' "
                 + " order by SEND_TIME desc ";
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()){
        YHPdaSms sms = new YHPdaSms();
        sms.setSeqId(rs.getInt("SEQ_ID"));
        sms.setFromId(rs.getInt("FROM_ID"));
        sms.setSendTime(rs.getTimestamp("SEND_TIME"));
        sms.setSmsType(rs.getString("SMS_TYPE"));
        sms.setContent(rs.getString("CONTENT"));
        sms.setUserName(rs.getString("USER_NAME"));
        list.add(sms);
      }
      request.setAttribute("smses", list);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    request.getRequestDispatcher("/pda/sms/index.jsp").forward(request, response);
    return;
  }
  
  @SuppressWarnings("static-access")
  public void sendSms(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String toName = (String)request.getParameter("toName");
      String content = (String)request.getParameter("content");
      
      String toId = "0";
      String sql = " select SEQ_ID from person where USER_NAME = '"+toName+"'";
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        toId = rs.getString("SEQ_ID");
      }
      if("0".equals(toId)){
        request.setAttribute("flag", 0);
      }else{
        this.doSmsBackTime(dbConn, content, person.getSeqId(), toId, "0", "", new Date());
        request.setAttribute("flag", 1);
      }
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    request.getRequestDispatcher("/pda/sms/send.jsp").forward(request, response);
    return;
  }
  
  public static void doSmsBackTime(Connection conn, String content, int fromId, String toId, String type, String remindUrl, Date sendDate) throws Exception {
    YHSmsBack sb = new YHSmsBack();
    sb.setContent(content);
    sb.setFromId(fromId);
    sb.setToId(toId);
    sb.setSmsType(type);
    sb.setRemindUrl(remindUrl);
    sb.setSendDate(sendDate);
    YHSmsUtil.smsBack(conn, sb);
  }
  
  public void cancelSms(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    try{
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String seqId = (String)request.getParameter("seqId");
      
      String sql = " update SMS set REMIND_FLAG='0' where TO_ID="+person.getSeqId()+" and SEQ_ID in ("+seqId+") ";
      ps = dbConn.prepareStatement(sql);
      ps.executeUpdate();
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, null, null);
    }
    request.getRequestDispatcher("/yh/pda/sms/act/YHPdaSmsAct/doint.act").forward(request, response);
    return;
  }
}
