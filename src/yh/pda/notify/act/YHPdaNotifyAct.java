package yh.pda.notify.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.pda.notify.data.YHPdaNotify;

public class YHPdaNotifyAct {

  public void search(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      int pageSize = Integer.parseInt(request.getParameter("pageSize") == "" || request.getParameter("pageSize") == null ? "5" : request.getParameter("pageSize"));
      int thisPage = Integer.parseInt(request.getParameter("thisPage") == "" || request.getParameter("thisPage") == null ? "1" : request.getParameter("thisPage"));
      //int totalPage = Integer.parseInt(request.getParameter("totalPage") == "" || request.getParameter("totalPage") == null ? "1" : request.getParameter("totalPage"));
      
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
      
      String sql = " SELECT n.SEQ_ID, FROM_ID, p.USER_NAME, SUBJECT, ";
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        sql = sql + "[TOP] ";
      }else {
        sql = sql + "TOP";
      }
      
      String isClob = "=";
      if(dbms.equals(YHConst.DBMS_ORACLE)){
        isClob = "like";
      }
      sql = sql  + " , n.TYPE_ID, c.CLASS_DESC, BEGIN_DATE, ATTACHMENT_ID, ATTACHMENT_NAME, CONTENT "
                 + " from oa_notify n "
                 + " left join oa_kind_dict_item c on c.SEQ_ID = n.TYPE_ID "
                 + " join PERSON p on p.SEQ_ID = FROM_ID "
                 + " where (TO_ID " + isClob + " '0' "
                 + " or " + YHDBUtility.findInSet(String.valueOf(person.getDeptId()), "TO_ID")
                 + " or " + YHDBUtility.findInSet(String.valueOf(person.getDeptIdOther()), "TO_ID")
                 + " or " + YHDBUtility.findInSet(String.valueOf(person.getUserPriv()), "PRIV_ID") 
                 + " or " + YHDBUtility.findInSet(String.valueOf(person.getUserPrivOther()), "PRIV_ID") 
                 + " or " + YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "n.USER_ID") +")"
                 + " and " + YHDBUtility.getDateFilter("begin_date", YHUtility.getDateTimeStr(null).substring(0, 10)+" 00:00:00", "<=")
                 + " and (" + YHDBUtility.getDateFilter("end_date", YHUtility.getDateTimeStr(null).substring(0, 10)+" 00:00:00", ">=") +" or end_date is null) "
                 + " and PUBLISH='1' ";
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        sql = sql + " order by [TOP] desc,BEGIN_DATE desc,SEND_TIME desc ";
      }else {
        sql = sql + " order by TOP desc,BEGIN_DATE desc,SEND_TIME desc ";
      }
      
      List<YHPdaNotify> list = new ArrayList<YHPdaNotify>();
      ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      rs.last();
      int totalSize = rs.getRow();
      if (totalSize == 0) {
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("thisPage", 0);
        request.setAttribute("totalPage", 0);
        request.setAttribute("notifies", list);
        request.getRequestDispatcher("/pda/notify/index.jsp").forward(request, response);
        return;
      }
      rs.absolute((thisPage-1) * pageSize + 1);
      int count = 0;
      while(!rs.isAfterLast()) {
        if(count >= pageSize)
          break;
        YHPdaNotify notify = new YHPdaNotify();
        notify.setSeqId(rs.getInt("SEQ_ID"));
        notify.setFromId(rs.getString("FROM_ID"));
        notify.setUserName(rs.getString("USER_NAME"));
        notify.setSubject(rs.getString("SUBJECT"));
        notify.setTop(rs.getString("TOP"));
        notify.setTypeId(rs.getString("TYPE_ID"));
        notify.setClassDesc(rs.getString("CLASS_DESC"));
        notify.setBeginDate(rs.getTimestamp("BEGIN_DATE"));
        notify.setAttachmentId(rs.getString("ATTACHMENT_ID"));
        notify.setAttachmentName(rs.getString("ATTACHMENT_NAME"));
        String content = rs.getString("CONTENT");
        content = content == null ? "" : YHUtility.cutHtml(content);
        notify.setContent(content);
        list.add(notify);
        rs.next();
        count++;
      }
      request.setAttribute("pageSize", pageSize);
      request.setAttribute("thisPage", thisPage);
      request.setAttribute("totalPage", totalSize/pageSize + (totalSize%pageSize == 0 ? 0 : 1));
      request.setAttribute("notifies", list);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    request.getRequestDispatcher("/pda/notify/index.jsp").forward(request, response);
    return;
  }
}
