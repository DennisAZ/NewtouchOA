package yh.core.module.org_select.act;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHRoleSelectAct {
  public String getRoles(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String moduleId = request.getParameter("moduleId");
    String privOp = request.getParameter("privOp");
    String privNoFlagStr = request.getParameter("privNoFlag");
    int privNoFlag = 0;
    if (!YHUtility.isNullorEmpty(privNoFlagStr)) {
      privNoFlag = Integer.parseInt(privNoFlagStr);
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHUserPrivLogic logic = new YHUserPrivLogic();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      List<YHUserPriv> list = null;
      if ((moduleId != null && !"".equals(moduleId)) || !YHUtility.isNullorEmpty(privOp)) {
        list = logic.getRoleList(dbConn , moduleId , loginUser , privNoFlag , privOp);
      } else {
        list = logic.getRoleList(dbConn);
      }
      StringBuffer sb = new StringBuffer();
      for (YHUserPriv up : list) {
        String str = "{";
        str += "privNo:" + up.getSeqId() + ",";  
        str += "privName:\"" + YHUtility.encodeSpecial(up.getPrivName()) + "\"";  
        str += "},";
        sb.append(str);
      }
      if (list.size() > 0) {
        sb.deleteCharAt(sb.length() - 1);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "get Success");
      request.setAttribute(YHActionKeys.RET_DATA, "[" + sb.toString() + "]");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
