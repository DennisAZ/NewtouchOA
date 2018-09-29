package yh.subsys.portal.bjca.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.subsys.portal.bjca.logic.YHBjcaSSOLogic;

public class YHBjcaSSOAct {
  public String getUserNameByCa(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
     
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userUnId = request.getParameter("userId");
      String data = "";
      if(userUnId != null && !"".equals(userUnId)){
        YHBjcaSSOLogic bsso = new YHBjcaSSOLogic();
        data = bsso.getUserNameByCa(dbConn, userUnId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "取得数据成功"); 
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\""); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    
    return "/core/inc/rtjson.jsp";
  }
}
