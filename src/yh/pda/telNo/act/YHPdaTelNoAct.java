package yh.pda.telNo.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHBeanKeys;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.pda.telNo.data.YHPostTel;

public class YHPdaTelNoAct {

  public void search(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String area = request.getParameter("area");
      String telNo = request.getParameter("telNo");
      String postNo = request.getParameter("postNo");
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      StringBuffer sql = new StringBuffer(" SELECT PROVINCE , CITY , COUNTY , TOWN , TEL_NO , POST_NO from oa_post_tel_num where 1=1 ");
      if(!YHUtility.isNullorEmpty(area)){
        sql.append(" and (CITY like '%"+area+"%' or COUNTY like '%"+area+"%' or TOWN like '%"+area+"%') ");
      }
      if(!YHUtility.isNullorEmpty(telNo)){
        sql.append(" and TEL_NO like '%"+telNo+"%' ");
      }
      if(!YHUtility.isNullorEmpty(postNo)){
        sql.append(" and POST_NO like '%"+postNo+"%' ");
      }
      
      List<YHPostTel> list = new ArrayList<YHPostTel>();
      ps = dbConn.prepareStatement(sql.toString());
      rs = ps.executeQuery();
      while(rs.next()) {
        YHPostTel postTel = new YHPostTel();
        postTel.setProvince(rs.getString("PROVINCE"));
        postTel.setCity(rs.getString("CITY"));
        postTel.setCounty(rs.getString("COUNTY"));
        postTel.setTown(rs.getString("TOWN"));
        postTel.setTelNo(rs.getString("TEL_NO"));
        postTel.setPostNo(rs.getString("POST_NO"));
        list.add(postTel);
      }
      request.setAttribute("postTels", list);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    request.getRequestDispatcher("/pda/telNo/search.jsp").forward(request, response);
    return;
  }
}
