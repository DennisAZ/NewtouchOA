package yh.core.funcs.doc.receive.act;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.doc.receive.logic.YHDoCReceiveDeskLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHDocReceiveDeskAct{
  /**
   * 查找10个批办单
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String findDocFileAjax(HttpServletRequest request, HttpServletResponse response)throws Exception{
    try{
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHDoCReceiveDeskLogic  docLogic = new YHDoCReceiveDeskLogic();
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
      String webroot =request.getRealPath("/");
      String docJson = docLogic.myDocReceiveJsonDesk(dbConn, user , webroot);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
      request.setAttribute(YHActionKeys.RET_DATA, docJson);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    
    return "/core/inc/rtjson.jsp";
  }
}
