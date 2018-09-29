package yh.pda.address.act;

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
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.pda.address.data.YHPdaAddress;

public class YHPdaAddressAct {

  public void search(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      String psnName = request.getParameter("psnName");
      String deptName = request.getParameter("deptName");
      
      int pageSize = Integer.parseInt(request.getParameter("pageSize") == "" || request.getParameter("pageSize") == null ? "5" : request.getParameter("pageSize"));
      int thisPage = Integer.parseInt(request.getParameter("thisPage") == "" || request.getParameter("thisPage") == null ? "1" : request.getParameter("thisPage"));
      //int totalPage = Integer.parseInt(request.getParameter("totalPage") == "" || request.getParameter("totalPage") == null ? "1" : request.getParameter("totalPage"));
      
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String sql = " select SEQ_ID from oa_address_team "
                 + " where USER_ID='"+person.getSeqId()+"' "
                 + " or (USER_ID='' and "
                 + "   (" + YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "PRIV_USER") 
                 + "   or PRIV_DEPT='ALL_DEPT' "
                 + "   or " + YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "PRIV_DEPT") 
                 + "   or " + YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "PRIV_ROLE") + "))";
      
      String groupIdStr = "";
      
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()) {
        groupIdStr = groupIdStr + rs.getInt("SEQ_ID") + ",";
      }
      groupIdStr = groupIdStr + "0";
      
      sql = " SELECT ag.GROUP_NAME , a.GROUP_ID , a.PSN_NAME , a.SEX , a.BIRTHDAY , a.MINISTRATION , a.DEPT_NAME , " 
      		+ " a.TEL_NO_DEPT , a.TEL_NO_HOME , a.MOBIL_NO , a.EMAIL from oa_address a " 
      		+ " left join oa_address_team ag on a.GROUP_ID = ag.SEQ_ID "
      		+ " where a.GROUP_ID in ("+groupIdStr+") ";
      if(!YHUtility.isNullorEmpty(psnName))
        sql += " and a.PSN_NAME like '%"+psnName+"%'";
      if(!YHUtility.isNullorEmpty(deptName))
        sql += " and a.DEPT_NAME like '%"+deptName+"%'";
      
      List<YHPdaAddress> list = new ArrayList<YHPdaAddress>();
      ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      rs.last();
      int totalSize = rs.getRow();
      if (totalSize == 0) {
        request.setAttribute("psnName", psnName);
        request.setAttribute("deptName", deptName);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("thisPage", 0);
        request.setAttribute("totalPage", 0);
        request.setAttribute("addresses", list);
        request.getRequestDispatcher("/pda/address/search.jsp").forward(request, response);
        return;
      }
      rs.absolute((thisPage-1) * pageSize + 1);
      int count = 0;
      while(!rs.isAfterLast()) {
        if(count >= pageSize)
          break;
        YHPdaAddress address = new YHPdaAddress();
        address.setGroupName(rs.getString("GROUP_NAME") == null ? "默认" : rs.getString("GROUP_NAME"));
        address.setGroupId(rs.getInt("GROUP_ID"));
        address.setPsnName(rs.getString("PSN_NAME"));
        address.setSex(rs.getString("SEX"));
        address.setBirthday(rs.getTimestamp("BIRTHDAY"));
        address.setMinistration(rs.getString("MINISTRATION"));
        address.setDeptName(rs.getString("DEPT_NAME"));
        address.setTelNoDept(rs.getString("TEL_NO_DEPT"));
        address.setTelNoHome(rs.getString("TEL_NO_HOME"));
        address.setMobilNo(rs.getString("MOBIL_NO"));
        address.setEmail(rs.getString("EMAIL"));
        list.add(address);
        rs.next();
        count++;
      }
      request.setAttribute("psnName", psnName);
      request.setAttribute("deptName", deptName);
      request.setAttribute("pageSize", pageSize);
      request.setAttribute("thisPage", thisPage);
      request.setAttribute("totalPage", totalSize/pageSize + (totalSize%pageSize == 0 ? 0 : 1));
      request.setAttribute("addresses", list);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    request.getRequestDispatcher("/pda/address/search.jsp").forward(request, response);
    return;
  }
}
