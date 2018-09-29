package yh.core.esb.client.doc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHEsbRecConfigAct {

  private static Logger log = Logger
      .getLogger("yh.core.esb.client.doc.YHEsbRecConfigAct");
    public String getPriv(HttpServletRequest request, HttpServletResponse response)
    throws Exception {
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPersonLogic personLogic = new YHPersonLogic();
        YHDeptLogic deptLogic = new YHDeptLogic();
        YHUserPrivLogic privLogic = new YHUserPrivLogic();
        String deptIds = "";
        String privIds ="";
        String userIds = "";
        String query = "select USER_ID , DEPT_ID, USER_PRIV from oa_esb_rec_person";
        Statement stmt = null;
        ResultSet rs = null;
        try {
          stmt = dbConn.createStatement();
          rs = stmt.executeQuery(query);
          if (rs.next()) {
            deptIds =YHUtility.null2Empty(rs.getString("DEPT_ID"));
             privIds =YHUtility.null2Empty(rs.getString("USER_PRIV"));
             userIds = YHUtility.null2Empty(rs.getString("USER_ID"));
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stmt, rs, null);
        }
        
        
        String deptNames = "";
        if ("0".equals(deptIds)) {
          deptNames = "全体部门";
        } else {
          deptNames = deptLogic.getNameByIdStr(deptIds , dbConn);
        }
        String privNames = privLogic.getNameByIdStr(privIds , dbConn);
        Map map = personLogic.getMapBySeqIdStr(userIds , dbConn);
        String userNames = (String)map.get("name");
        userIds = (String)map.get("id");
        StringBuffer sb = new StringBuffer("{");
        sb.append("userId:'" + userIds + "',");
        sb.append("deptId:'" + deptIds + "',");
        sb.append("privId:'" + privIds + "',");
        sb.append("userName:'" + userNames + "',");
        sb.append("deptName:'" + deptNames + "',");
        sb.append("privName:'" + privNames + "'");
        sb.append("}");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      } catch (Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
    public String savePriv(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      String userId = YHUtility.null2Empty(request.getParameter("user"));
      String deptId = YHUtility.null2Empty(request.getParameter("dept"));
      String privId = YHUtility.null2Empty(request.getParameter("role"));
      Connection dbConn = null;
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
          stmt = dbConn.createStatement();
          String query = "select 1 from oa_esb_rec_person";
          rs = stmt.executeQuery(query);
          if (rs.next()) {
             sql = "update oa_esb_rec_person set USER_ID='"+userId+"' , DEPT_ID ='"+deptId+"' , USER_PRIV='"+privId+"'";
          } else {
            sql = "insert into  oa_esb_rec_person  (USER_ID, DEPT_ID , USER_PRIV) VALUES ('"+userId+"' , '"+deptId+"' , '"+privId+"')";
          }
        } catch (Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stmt, rs, null);
        }
        
        Statement stm = null;
        try {
          stm = dbConn.createStatement();
          stm.executeUpdate(sql);
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, null, null); 
        }
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "修改成功");
      } catch (Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
}
