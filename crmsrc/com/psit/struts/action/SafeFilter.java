package com.psit.struts.action;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
/**
 * 编码过滤器 <br>
 */
public class SafeFilter  extends HttpServlet implements Filter {
    /**
     * 将请求用utf-8编码 <br>
     * @param request
     * @param respnose
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
	public void doFilter(ServletRequest request, ServletResponse respnose,
			FilterChain filterChain) throws IOException, ServletException {
		try {
			request.setCharacterEncoding("utf-8");
		} catch (java.io.UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		filterChain.doFilter(request, respnose);
	}
	public void init(FilterConfig arg0) throws ServletException {
	}
}
