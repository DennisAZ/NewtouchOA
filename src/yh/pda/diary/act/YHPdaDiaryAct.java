package yh.pda.diary.act;

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
import yh.core.util.YHRegexpUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.pda.diary.data.YHPdaDiary;

public class YHPdaDiaryAct {

  public void doint(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
      
      String sql = " SELECT d.SEQ_ID , d.DIA_DATE , d.DIA_TYPE , d.CONTENT from oa_journal d "
                 + " where d.USER_ID='"+person.getSeqId()+"' order by d.SEQ_ID desc ";
      
      List<YHPdaDiary> list = new ArrayList<YHPdaDiary>();
      ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      rs.last();
      int totalSize = rs.getRow();
      if (totalSize == 0) {
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("thisPage", 0);
        request.setAttribute("totalPage", 0);
        request.setAttribute("diarys", list);
        request.getRequestDispatcher("/pda/diary/index.jsp").forward(request, response);
        return;
      }
      rs.absolute((thisPage-1) * pageSize + 1);
      int count = 0;
      while(!rs.isAfterLast()) {
        if(count >= pageSize)
          break;
        YHPdaDiary diary = new YHPdaDiary();
        diary.setSeqId(rs.getInt("SEQ_ID"));
        diary.setDiaDate(rs.getTimestamp("DIA_DATE"));
        diary.setDiaType(rs.getInt("DIA_TYPE"));
        diary.setContent(YHRegexpUtility.cutHtml(rs.getString("CONTENT")));
        list.add(diary);
        rs.next();
        count++;
      }
      request.setAttribute("pageSize", pageSize);
      request.setAttribute("thisPage", thisPage);
      request.setAttribute("totalPage", totalSize/pageSize + (totalSize%pageSize == 0 ? 0 : 1));
      request.setAttribute("diarys", list);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    request.getRequestDispatcher("/pda/diary/index.jsp").forward(request, response);
    return;
  }
  
  public void edit(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    try{
      String seqId = (String)request.getParameter("seqId");
      String diaType = (String)request.getParameter("diaType");
      String content = (String)request.getParameter("content");
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String sql = " update oa_journal set DIA_TYPE ="+ diaType +",CONTENT ='"+content+"' where SEQ_ID = "+seqId;
      ps = dbConn.prepareStatement(sql);
      int flag = ps.executeUpdate();
      request.setAttribute("flag", flag);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, null, null);
    }
    request.getRequestDispatcher("/pda/diary/send.jsp").forward(request, response);
    return;
  }
  
  public void newDiary(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    try{
      String diaType = (String)request.getParameter("diaType");
      String content = (String)request.getParameter("content");
      
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String sql = " insert into oa_journal(USER_ID , DIA_TYPE , content , DIA_DATE , DIA_TIME) values("+person.getSeqId()+" , "+diaType+",'"+content+"',?,?)";
      ps = dbConn.prepareStatement(sql);
      
      String date = YHUtility.getDateTimeStr(null).substring(0, 10);
      
      ps.setTimestamp(1, YHUtility.parseTimeStamp(YHUtility.parseDate(date+" 00:00:00").getTime()));
      ps.setTimestamp(2, YHUtility.parseTimeStamp());
      int flag = ps.executeUpdate();
      request.setAttribute("flag", flag);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, null, null);
    }
    request.getRequestDispatcher("/pda/diary/send.jsp").forward(request, response);
    return;
  }
}
