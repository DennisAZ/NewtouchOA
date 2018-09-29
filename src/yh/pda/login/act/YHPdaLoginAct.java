package yh.pda.login.act;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.act.YHSystemAct;
import yh.core.funcs.system.act.filters.YHPasswordValidator;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.db.YHORM;
import yh.pda.login.logic.YHPdaSystemLoginLogic;

public class YHPdaLoginAct {

  @SuppressWarnings("unchecked")
  public void doLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String username = (String)request.getParameter("username");
      
      YHPdaSystemLoginLogic logic = new YHPdaSystemLoginLogic();

      //验证用户否存在      if(!logic.validateUser(dbConn , username)){
        request.setAttribute("errorMsg", "用户不存在");
        request.setAttribute("errorNo", "-1");
        request.setAttribute("username", username);
        request.getRequestDispatcher("/pda/index.jsp").forward(request, response);
        return;
      }   
      
      YHPerson person = null;
      try{
        YHORM orm = new YHORM();
        String[] filters = new String[]{"USER_ID = '" + username + "' or BYNAME = '" + username + "'"};
        List<YHPerson> list = orm.loadListSingle(dbConn, YHPerson.class, filters);
        if (list.size() > 0){
          person = list.get(0);
        }
      }catch(Exception ex) {
        throw ex;
      }
      
      //验证密码
      YHPasswordValidator passwordValidator = new YHPasswordValidator();
      if(!passwordValidator.isValid(request, person, dbConn)){
        request.setAttribute("errorMsg", "密码错误");
        request.setAttribute("errorNo", "-2");
        request.setAttribute("username", username);
        request.getRequestDispatcher("/pda/index.jsp").forward(request, response);
        return;
      }
      
      this.loginSuccess(dbConn , person, request, response);
      request.getSession().setAttribute("P_VER", (String)request.getParameter("P_VER"));
      
    } catch (Exception ex) {
      request.setAttribute("errorMsg", "登录失败");
      throw ex;
    }
    request.getRequestDispatcher("/pda/main.jsp").forward(request, response);
  }
  
	/**
	 * add by zyy 张银友 手机登录验证
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	public void checkLogin(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String username = (String) request.getParameter("username");
		Connection dbConn = null;
		
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHPdaSystemLoginLogic logic = new YHPdaSystemLoginLogic();

			// 验证用户否存在
			if (!logic.validateUser(dbConn, username)) {
				response.getWriter().print(0);
				return;
			}

			YHPerson person = null;
			try {
				YHORM orm = new YHORM();
				String[] filters = new String[] { "USER_ID = '" + username
						+ "' or BYNAME = '" + username + "'" };
				List<YHPerson> list = orm.loadListSingle(dbConn,YHPerson.class, filters);
				if (list.size() > 0) {
					person = list.get(0);
				}
			} catch (Exception ex) {
				throw ex;
			}

			// 验证密码
			YHPasswordValidator passwordValidator = new YHPasswordValidator();
			if (!passwordValidator.isValid(request, person, dbConn)) {
				response.getWriter().print(0); // 密码错误
				return;
			}

			this.loginSuccess(dbConn, person, request, response);
			request.getSession().setAttribute("P_VER",(String) request.getParameter("P_VER"));
			response.getWriter().print(1); // 登录成功
		} catch (Exception ex) {
			response.getWriter().print(0); // 验证失败
		}
	}
  
  
  
  /**
   * 登录成功的处理

   * @param conn
   * @param person
   * @param request
   * @throws Exception
   */
  private void loginSuccess(Connection conn, YHPerson person , HttpServletRequest request, HttpServletResponse response) throws Exception{
    
    //获取用户当前的session,如果不存在就生成一个新的session
    HttpSession session = request.getSession(true);
    //判断用户是否已经登录
    if (session.getAttribute("LOGIN_USER") == null){
      YHSystemAct logic = new YHSystemAct();
      logic.setUserInfoInSession(person, session, request.getRemoteAddr(), request);
      session.setAttribute("LOGIN_USER", person);
      session.setAttribute("ATTACH_LOCK_REF_SEC", 0l);
    }
    else {
      YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
      
      //如果是新用户登录时,销毁原有的session
      if (loginPerson.getSeqId() != person.getSeqId()) {
        
        //销毁session
        session.invalidate();
        
        //重新调用登录成功的处理
        loginSuccess(conn, person, request, response);
      }
    }
  }
}
