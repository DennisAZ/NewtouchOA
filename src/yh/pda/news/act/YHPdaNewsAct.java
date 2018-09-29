package yh.pda.news.act;

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
import yh.pda.news.data.YHPdaNews;

public class YHPdaNewsAct {

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
      String isClob = "=";
      if(dbms.equals(YHConst.DBMS_ORACLE)){
        isClob = "like";
      }
      String sql = " SELECT n.SEQ_ID, PROVIDER, SUBJECT, NEWS_TIME, FORMAT,TYPE_ID, ATTACHMENT_ID, ATTACHMENT_NAME, p.USER_NAME, c.CLASS_DESC, CONTENT "
                 + " from oa_news n"
                 + " left join oa_kind_dict_item c on c.SEQ_ID = n.TYPE_ID "
                 + " join PERSON p on p.SEQ_ID = PROVIDER "
                 + " where PUBLISH='1' "
                 + " and (TO_ID " + isClob + " '0' or " + YHDBUtility.findInSet(String.valueOf(person.getDeptId()), "TO_ID") 
                 + " or " + YHDBUtility.findInSet(String.valueOf(person.getUserPriv()), "PRIV_ID") 
                 + " or " + YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "n.USER_ID") +") "
                 + " order by NEWS_TIME desc ";
      
      List<YHPdaNews> list = new ArrayList<YHPdaNews>();
      ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      rs.last();
      int totalSize = rs.getRow();
      if (totalSize == 0) {
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("thisPage", 0);
        request.setAttribute("totalPage", 0);
        request.setAttribute("news", list);
        request.getRequestDispatcher("/pda/news/index.jsp").forward(request, response);
        return;
      }
      rs.absolute((thisPage-1) * pageSize + 1);
      int count = 0;
      while(!rs.isAfterLast()) {
        if(count >= pageSize)
          break;
        YHPdaNews news = new YHPdaNews();
        news.setSeqId(rs.getInt("SEQ_ID"));
        news.setProvider(rs.getString("PROVIDER"));
        news.setUserName(rs.getString("USER_NAME"));
        news.setSubject(rs.getString("SUBJECT"));
        news.setNewsTime(rs.getTimestamp("NEWS_TIME"));
        news.setFormat(rs.getString("FORMAT"));
        news.setTypeId(rs.getString("TYPE_ID"));
        news.setClassDesc(rs.getString("CLASS_DESC"));
        news.setAttachmentId(rs.getString("ATTACHMENT_ID"));
        news.setAttachmentName(rs.getString("ATTACHMENT_NAME"));
        String content = rs.getString("CONTENT");
        content = content == null ? "" : YHUtility.cutHtml(content);
        news.setContent(content);
        list.add(news);
        rs.next();
        count++;
      }
      request.setAttribute("pageSize", pageSize);
      request.setAttribute("thisPage", thisPage);
      request.setAttribute("totalPage", totalSize/pageSize + (totalSize%pageSize == 0 ? 0 : 1));
      request.setAttribute("news", list);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    request.getRequestDispatcher("/pda/news/index.jsp").forward(request, response);
    return;
  }
}
