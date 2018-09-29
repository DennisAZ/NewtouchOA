package yh.core.funcs.person.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.person.logic.YHBindUsersLogic;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;


public class YHBindUsersAct {
  
  public static final String BIND_SYS_ID = "gpowersoft=通元";
  private YHBindUsersLogic logic = new YHBindUsersLogic();
  
  public String bindInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String userId = request.getParameter("userId");
    
    Connection dbConn = null;
    try {
      
      if (userId == null || "".endsWith(userId.trim())){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "USERID为空");
        return "/core/inc/rtjson.jsp";
      }
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String data = null;
      if (this.logic.isBind(dbConn, Integer.parseInt(userId))){
        data = this.logic.queryBindInfo(dbConn, Integer.parseInt(userId));
      }
      else{
        data = "0";
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String bindUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    String userDescOther = request.getParameter("userDescOther");
    String userIdOther = request.getParameter("userIdOther");
    String userId = request.getParameter("userId");
    
    Connection dbConn = null;
    try {
      
      if (userIdOther == null || "".equals(userIdOther.trim())){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有传递用户id");
        return "/core/inc/rtjson.jsp";
      }
      
      if (userId == null || "".endsWith(userId.trim())){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "USERID为空");
        return "/core/inc/rtjson.jsp";
      }
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      if (this.logic.isBind(dbConn, Integer.parseInt(userId))){
        this.logic.rebindUser(dbConn, Integer.parseInt(userId), userIdOther, userDescOther, BIND_SYS_ID);
      }
      else{
        this.logic.bindUser(dbConn, Integer.parseInt(userId), userIdOther, userDescOther, BIND_SYS_ID);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "绑定成功");
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  public String removeBind(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    Connection dbConn = null;
    String userId = request.getParameter("userId");
    
    try {
      
      if (userId == null || "".endsWith(userId.trim())){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "USERID为空");
        return "/core/inc/rtjson.jsp";
      }
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      this.logic.removeBind(dbConn, Integer.parseInt(userId));
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "解除绑定成功");
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
