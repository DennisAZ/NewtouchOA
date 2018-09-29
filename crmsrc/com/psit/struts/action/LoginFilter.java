package com.psit.struts.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录过滤器 <br>
 * create_date: Aug 11, 2010,5:35:53 PM<br>
 */
public class LoginFilter extends HttpServlet implements Filter {
	/**
	 * 过滤器方法当session失效且op不等于doLogin，iniSystem，updateSystem时如果点击我的工作台将跳转到login页面
	 * 执行其他操作将跳到toLogin页面 <br>
	 * create_date: Aug 11, 2010,5:36:18 PM <br>
	 * 
	 * @param request
	 *         parameter > op:表单提交的方法名
	 * @param response
	 * @param filterChain
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) {
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse res = (HttpServletResponse) response;
//		String userCode = (String) req.getSession().getAttribute("userCode");
//		String op = "";
//		if (req.getParameter("op") != null) {
//			op = req.getParameter("op");
//		}
//		// if(false){
//		if ((userCode == null || userCode.equals("")) && !op.equals("doLogin")
//				&& !op.equals("iniSystem") && !op.equals("updateSystem")) {
//			PrintWriter out = null;
//			try {
//				out = res.getWriter();
//				if (op.equals("toDeskTop")) {
//					out.print("<script type='text/javascript'>"
//							+ "top.location.href='login.jsp';" + "</script>");
//				} else {
//					out.print("<script type='text/javascript'>"
//							+ "top.location.href='toLogin.jsp';" + "</script>");
//				}
//
//				out.flush();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				out.close();
//			}
//		} else {
			// 已经登录,继续此次请求
			try {
				filterChain.doFilter(request, response);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ServletException e) {
				e.printStackTrace();
			}
//		}

	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
