package yh.core.esb.server.system.act;

import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import yh.core.data.YHRequestDbConn;
import yh.core.esb.server.system.logic.SystemLoginLogic;
import yh.core.esb.server.user.data.TdUser;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.auth.YHRegistUtility;

public class SystemLoginAct {

  public String doLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userCode = new String(request.getParameter("userCode").getBytes("iso8859-1"), "utf-8");
      String pwd = request.getParameter("pwd");
      
      SystemLoginLogic logic = new SystemLoginLogic();

      //验证用户否存在
      if(!logic.validateUser(dbConn , userCode)){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "用户不存在");
        request.setAttribute(YHActionKeys.RET_DATA, "{\"code\":-1}");
        return "/core/inc/rtjson.jsp";
      }     
      
      //验证密码
      if(!logic.checkPwd(dbConn , userCode , pwd)){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "密码错误");
        request.setAttribute(YHActionKeys.RET_DATA, "{\"code\":-2}");
        return "/core/inc/rtjson.jsp";
      }
      TdUser tdUser = logic.queryPerson(dbConn, userCode);
      if (!YHRegistUtility.hasRegisted() && YHRegistUtility.isExpired()) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "软件已经过期");
        request.setAttribute(YHActionKeys.RET_DATA, "{\"code\":-3}");
        return "/core/inc/rtjson.jsp";
      }
      this.loginSuccess(dbConn , tdUser, request, response);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 登录成功的处理
   * @param conn
   * @param person
   * @param request
   * @throws Exception
   */
  private void loginSuccess(Connection conn, TdUser tdUser , HttpServletRequest request, HttpServletResponse response) throws Exception{
    
    //获取用户当前的session,如果不存在就生成一个新的session
    HttpSession session = request.getSession(true);
    //判断用户是否已经登录
    if (session.getAttribute("ESB_LOGIN_USER") == null){
      session.setAttribute("ESB_LOGIN_USER", tdUser);
    }
    else {
      TdUser loginPerson = (TdUser)session.getAttribute("ESB_LOGIN_USER");
      
      //如果是新用户登录时,销毁原有的session
      if (loginPerson.getSeqId() != tdUser.getSeqId()) {
        
        //销毁session
        session.invalidate();
        
        //重新调用登录成功的处理        loginSuccess(conn, tdUser, request, response);
      }
    }
  }
}
