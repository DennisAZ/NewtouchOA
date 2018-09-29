package yh.core.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oa.spring.util.StaticDataUtil;

import org.apache.log4j.Logger;

import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;

/**
 * 处理Request的过滤器
 * 
 * @author ljs
 * 
 */
public class YHRequestFilter implements Filter {
	// 编码
	protected String encoding = null;
	// 过滤器配置

	protected FilterConfig filterConfig = null;

	private static Logger log = Logger.getLogger(YHRequestFilter.class);

	/**
	 * 释放资源
	 */
	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	/**
	 * 执行过滤器
	 * 
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		String qUri = ((HttpServletRequest) request).getRequestURI();
		
		//添加对erp开账前的拦截
		if(StaticDataUtil.sysInitDataFlag == 0){
			if(!qUri.contains("/initData/")){
				if(qUri.contains("springViews/erp")){
					YHServletUtility.forward("/springViews/erp/initData/leadPage.jsp",
							(HttpServletRequest) request,
							(HttpServletResponse) response);
					return;
				}
			}
		}
		
		if (!qUri.contains("SpringR") && qUri.endsWith("/")) {
			qUri += "index.jsp";
		}
		String requestType = ((HttpServletRequest) request).getParameter("requestType"); //用于android客户端 shenrm 2013-02-07
		String method = ((HttpServletRequest) request).getParameter("method"); //用于android客户端下载文件所用的参数
		/**
		 * 验证是否已经登录
		 */
		boolean isValidSession =YHServletUtility.isValidSession(session, "LOGIN_USER"); //进行session权限的验证
		if ((qUri.endsWith(".jsp") || qUri.endsWith(".act"))
				&& !YHServletUtility.isLoginAction((HttpServletRequest) request)) {
			if(requestType != null && method != null){ //判断
				if("android".equalsIgnoreCase(requestType) && "downFile".equalsIgnoreCase(method)){
					isValidSession = true; //是android客户端下载文件，就放开权限，不需要session的验证
				}
			}
			if (!isValidSession) {
				if (((HttpServletRequest) request).getRequestURI().contains(
						"/pda/")) {
					YHServletUtility.forward("/pda/pdaSessionerror.jsp",
							(HttpServletRequest) request,
							(HttpServletResponse) response);

				} else {
					YHServletUtility.forward("/core/inc/sessionerror.jsp",
							(HttpServletRequest) request,
							(HttpServletResponse) response);
				}
				return;
			}
		}
//    System.out.println("执行请求："+qUri);
		if (qUri.endsWith("yh/core/funcs/sms/act/YHSmsAct/remindCheck.act")) {
			Long lastOptTime = (Long) session.getAttribute("LAST_OPT_TIME");
			Long lockSec = (Long) session.getAttribute("OFFLINE_TIME_MIN");

			// 设置了空闲强制自动离线时间并且上次操作时间不为空时判断是否过期
			if (lockSec > 0 && lastOptTime != null
					&& lastOptTime + lockSec < new Date().getTime()) {

				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
				request.setAttribute(YHActionKeys.RET_MSRG, "空闲自动离线");
				request.setAttribute(YHActionKeys.RET_DATA, "-1");
				YHServletUtility.forward("/core/inc/rtjson.jsp",
						(HttpServletRequest) request,
						(HttpServletResponse) response);
				return;
			}
		} else {
			session.setAttribute("LAST_OPT_TIME", new Date().getTime());
		}

		response.setCharacterEncoding(encoding);
		long t1 = System.currentTimeMillis();
		chain.doFilter(request, response);
		long t2 = System.currentTimeMillis();
		if ((t2 - t1) > Long.parseLong(YHSysProps.getProp("time"))) {
			if ((qUri.endsWith(".jsp") || qUri.endsWith(".act"))) {
				log.debug("time:" + (t2 - t1) + "ms requestURI:" + qUri);
			}
		}

	}

	/**
	 * 过滤器初始化
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
	}

	/**
	 * 取得编码
	 * 
	 * @return
	 */
	protected String getEncoding() {
		return (this.encoding);
	}
}
