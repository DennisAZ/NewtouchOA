package com.psit.struts.action;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.psit.struts.util.JdbcCon;

/**
 * session监听器 <br>
 * create_date: Aug 23, 2010,10:11:01 AM<br>
 */
public class SessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent event) {
		//HttpSession ses = event.getSession();   
	}

	/**
	 * session销毁时执行将登陆状态改为未登录状态 <br>
	 * create_date: Aug 23, 2010,10:12:19 AM <br>
	 * @param event
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		//ServletContext application = session.getServletContext();
		// 取得登录的用户账号
		String userCode = (String) session.getAttribute("userCode");
		JdbcCon jdbcCon = new JdbcCon();
		if (userCode != null && !userCode.equals("")) {
			String sql = "update lim_user set user_islogin='0' where user_code='"
					+ userCode + "'";
			jdbcCon.updateSql(sql);
		}
		jdbcCon.closeAll();
//		System.out.println(userCode + "退出!session 失效！");
		// event.getSession().getServletContext().getRequestDispatcher( "/login.jsp ").forward(request,response); 
	}

}
