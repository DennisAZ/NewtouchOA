package com.psit.struts.action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.psit.struts.BIZ.EmpBIZ;
import com.psit.struts.BIZ.MessageBIZ;
import com.psit.struts.BIZ.UserBIZ;
import com.psit.struts.entity.LimRight;
import com.psit.struts.entity.LimRole;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.RUserRig;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.SalOrg;
import com.psit.struts.util.EncryptGen;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.JdbcCon;
import com.psit.struts.util.OperateDate;
import com.psit.struts.util.format.StringFormat;
import com.psit.struts.util.format.TransStr;

/**
 * 系统初始化、登陆、职位设置 <br>
 * create_date: Aug 24, 2010,2:45:56 PM<br>
 */
public class UserAction extends BaseDispatchAction {
	UserBIZ userBiz = null;
	MessageBIZ messageBiz = null;
	EmpBIZ empBiz = null;
	private String ORGTOPCODE = EmpAction.getORGTOPCODE();//部门顶级号
//	private static long count = 1;
	private long init = 0;
//	static {
//		count--;
//	}

	/**
	 * 判断用户是否有账号职位管理操作的权限 <br>
	 * create_date: Aug 6, 2010,11:40:32 AM <br>
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 有权限继续执行原有方法，没有权限跳转到limError页面
	 * @throws Exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (isLimitAllow(request)) {
			return super.execute(mapping, form, request, response);
		} else {
			return mapping.findForward("limError");
		}
	}
	
	/**
	 * 检测用户是否有账号职位管理的操作的权限码 <br>
	 * create_date: Aug 6, 2010,11:40:32 AM <br>
	 * @param request
	 * @return boolean 有相应的权限码返回true，没有返回false<br>
	 */
	protected boolean isLimitAllow(HttpServletRequest request) {
		String methodName = request.getParameter("op");
		String methLim[][] = { { "userManage", "asy001" },//账号设置是否可见
				{ "searchLimRole", "asy006" },//职位设置是否可见
				{ "saveRole", "sy002" },//添加职位
				{ "updateRole", "sy003" },{"goUpdateRole","sy003"},//编辑职位
				{ "delRole", "sy004" },//删除职位
		};
		LimUser limUser=(LimUser)request.getSession().getAttribute("limUser");
		return userBiz.getLimit(limUser, methodName, methLim);
	}
	
	/**
	 * ajax判断是否有权限 <br>
	 * create_date: Sep 15, 2010,9:54:46 AM <br>
	 * @param request parameter > rig:要判断的权限码（多个权限码以'|'分隔）
	 * @param response void 如果当前账户有传入的权限码，out返回true，否则返回false(如果传入多个权限码则返回以'|'分隔的字符串)
	 */
	public void hasLimByAjax(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		LimUser limUser = (LimUser)request.getSession().getAttribute("limUser");
		String rig = request.getParameter("rig");
		String[] rigArr = rig.split("\\|");//传入多个权限码
		
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			boolean[] rs = userBiz.isInUserLims(rigArr,limUser);
			for(int i = 0; i < rs.length; i++){
				if(i == 0){
					out.print(rs[i]);
				}
				else{
					out.print("|"+rs[i]);
				}
			}
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 工作台 <br>
	 * @return ActionForward deskTop
	 */
	public ActionForward toDeskTop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("deskTop");
	}

	/**
	 * 登录 <br>
	 * @param request
	 *      parameter > loginName:登陆名; pwd:密码; isAutoLogin:是否检测系统授权信息（是否第一次进入系统）;
	 * @return ActionForward 不符合登陆条件跳转到login页面,成功登录跳转到index页面<br>
	 * 		attribute > isHavLogin:值为1系统不自动登陆; msg:提示信息; isReg:值为1显示系统激活页面; 
	 * 					isUpdSys:是否更新授权; lName:登陆名; lpwd:密码;
	 * 					session >
	 * 						CUR_EMP:当前用户; rightsList:用户权限码列表; TODAY_E/TODAY/TMR/AF_TMR/YDAY/BF_YDAY:最近日期;
	 */
	public ActionForward doLogin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String loginName = request.getParameter("loginName");
		String pwd = request.getParameter("pwd");
		String isAutoLogin = request.getParameter("isAutoLogin");
		
//		init = count + init;
		if (init == 0) {
			userBiz.recInit();//恢复登录初始状态
			init++;
		}
		if (isAutoLogin != null && isAutoLogin.equals("1")) {
			Date currDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			int userNum = userBiz.getCountAllUse();
			try {
				String fileName = request.getSession().getServletContext()
						.getRealPath("/") + "system\\init.dll";
				File file = new File(fileName);
				if (!file.exists()) {
					request.setAttribute("isHavLogin", "1");
					request.setAttribute("msg", "配置文件丢失，系统无法启动，请重新激活！");
					request.setAttribute("isReg", "1");
					return mapping.findForward("login");
				}
				else{
					String[] relutValue = EncryptGen.decryptFile(fileName);
					if (relutValue == null || relutValue[0].equals("")) {
						request.setAttribute("isHavLogin", "1");
						request.setAttribute("isReg", "1");
						return mapping.findForward("login");
					} else if (dateFormat.parse(relutValue[1]).before(currDate)) {
						request.setAttribute("msg", "您的授权已过期，请联系我们购买新的授权！");
						request.setAttribute("isHavLogin", "1");
						request.setAttribute("isReg", "1");
						request.setAttribute("isUpdSys", "1");
						return mapping.findForward("login");
					} else if (userNum > Integer.parseInt(relutValue[0])) {
						request.setAttribute("msg","您当前系统的账户数超过授权数量，系统已锁定！");
						request.setAttribute("isHavLogin", "1");
						request.setAttribute("isReg", "1");
						request.setAttribute("isUpdSys", "1");
						return mapping.findForward("login");
					} else {
						request.setAttribute("isHavLogin", "1");
						return mapping.findForward("login");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (userBiz.checkLogin(loginName, pwd)) {
				LimUser limUser = userBiz.getUserByName(loginName);
				if (limUser.getUserIsenabled().equals("2")) {
					request.setAttribute("isHavLogin", "1");
					request.setAttribute("msg", "该账号已被锁定暂时不能登录!");
					return mapping.findForward("login");
				} else {
					String IP = "";
					if (request.getHeader("x-forwarded-for") == null) {
						IP = request.getRemoteAddr();
					} else {
						IP = request.getHeader("x-forwarded-for");
					}
					List<RUserRig> rightsList = userBiz.getRUserRig(limUser.getUserCode());
					/* 放弃使用 以下session */
					request.getSession().setAttribute("limUser", limUser);
					request.getSession().setAttribute("userCode",
							limUser.getUserCode());
					request.getSession().setAttribute("userId",
							limUser.getSalEmp().getSeNo().toString());
					request.getSession().setAttribute("userName",
							limUser.getSalEmp().getSeName());
					request.getSession().setAttribute("userNum",
							limUser.getUserNum());
					/* 使用 以下session替换 */
					request.getSession().setAttribute("CUR_EMP", limUser.getSalEmp());
					request.getSession().setAttribute("CUR_USER", limUser);
					
					SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy年MM月dd日  E");
					String time = dateformat1.format(new Date());
					String time1 = dateformat.format(new Date());
					String time2 = dateformat.format(OperateDate.getDate(new Date(), 1));//明天
					String time3 = dateformat.format(OperateDate.getDate(new Date(), 2));//后天
					String time4 = dateformat.format(OperateDate.getDate(new Date(), -1));//昨天
					String time5 = dateformat.format(OperateDate.getDate(new Date(), -2));//前天
					/* 放弃使用 以下session */
					request.getSession().setAttribute("time", time);
					request.getSession().setAttribute("time1", time1);
					request.getSession().setAttribute("time2", time2);
					request.getSession().setAttribute("time3", time3);
					request.getSession().setAttribute("time4", time4);
					request.getSession().setAttribute("time5", time5);
					/* 使用 以下session替换 */
					request.getSession().setAttribute("TODAY_E", time);
					request.getSession().setAttribute("TODAY", time1);
					request.getSession().setAttribute("TMR", time2);
					request.getSession().setAttribute("AF_TMR", time3);
					request.getSession().setAttribute("YDAY", time4);
					request.getSession().setAttribute("BF_YDAY", time5);
					
					request.getSession().setAttribute("rightsList", rightsList);
					limUser.setUserIsLogin("1");
					limUser.setUserIp(IP);
					userBiz.updateUser(limUser);
					try {
						response.sendRedirect("index1.jsp");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				request.setAttribute("isHavLogin", "1");
				request.setAttribute("msg", "登录名或密码错误,请重新登录!");
				request.setAttribute("lName", loginName);
				request.setAttribute("lpwd", pwd);
				return mapping.findForward("login");
			}
		}
		return null;
	}

	/**
	 * Ajax登录名称判断 <br>
	 * create_date: Aug 24, 2010,3:28:04 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > loginName:登陆名 divCount:错误提示层序号
	 * @param response
	 * @return ActionForward null 登陆名重复输出1，不重复输出0(如果有多个提示层会加上层序号)<br>
	 */
	public ActionForward checkLoginName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String loginName = TransStr.transStr(request.getParameter("loginName"));
		String divCount = request.getParameter("divCount");//错误提示层序号
		List list = userBiz.getLoginName(loginName);
		PrintWriter out = null;
		String isRep = "";
		
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		try {
			out = response.getWriter();
			if (list.size() > 0) {
				isRep = "1";
			} else {
				isRep = "0";
			}
			if(divCount!=null&&!divCount.equals("")){
				isRep += "," + divCount;//如果有多个错误提示层，out返回层序号
			}
			out.print(isRep);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * Ajax职位名称判断 <br>
	 * create_date: Aug 24, 2010,3:28:04 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > rolName:职位名
	 * @param response
	 * @return ActionForward null 职位名重复输出1不重复输出0<br>
	 */
	public ActionForward checkRoleName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String rolName = TransStr.transStr(request.getParameter("rolName"));
		List list = userBiz.getRoleByName(rolName);
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if (list.size() > 0) {
				out.print("1");
			}
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			out.close();
		}
		return null;
	}

	/**
	 * 显示账号信息 <br>
	 * create_date: Aug 24, 2010,3:47:08 PM
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward 跳转到limUser页面<br>
	 * 		attribute > allUse:账号总数 isUse:已分配的账号数 notUse:未启用的账号数
	 *                  limUser:用户实体 startDate:系统启用时间 endDate:系统到期日期 userType:标识系统是正式版还是试用版
	 *                  isDefault:是否默认页面（显示授权信息） comOrg:公司信息
	 */
	public ActionForward userManage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userCode = (String) request.getSession().getAttribute("userCode");
		String endDate = "";//系统到期日期
		String startDate = "";//系统启用时间
		String userType = "";//系统类别
		
		int allUse = userBiz.getCountAllUse();//账号总数
		int notUse = userBiz.getCountNotUse();//未启用的账号数
		int isUse=allUse-notUse;//已启用的账号数
		LimUser limUser = userBiz.findById(userCode);
		SalOrg comOrg = empBiz.salOrgDesc(ORGTOPCODE);
		try {
			String fileName = request.getSession().getServletContext()
					.getRealPath("/")
					+ "system\\init.dll";
			String[] relutValue = EncryptGen.decryptFile(fileName);
			startDate = relutValue[2];
			endDate = relutValue[1];
			userType = relutValue[3];
			request.setAttribute("startDate", startDate);
			request.setAttribute("endDate", endDate);
			request.setAttribute("userType", userType);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		request.setAttribute("allUse", allUse);
		request.setAttribute("isUse", isUse);
		request.setAttribute("notUse", notUse);
		request.setAttribute("limUser", limUser);
		request.setAttribute("comOrg", comOrg);
		request.setAttribute("isDefault", "1");
		return mapping.findForward("limUser");
	}

	/**
	 * 跳转到新建账号页面 <br>
	 * @param request
	 * 		para >	curUserCode:添加下级帐号时所传当前帐号参数;
	 * @param response
	 * @return ActionForward addUser<br>
	 * 		attr > userCode:未启用的userCode最小值;	upUserCode:添加下级时的上级账号;	upUserName:上级账号的姓名和职位;
 *                  upUserRolLev:上级账号的职级;		isAddLowLev:0表示新建账号,1表示添加下级;
	 */
	public ActionForward goAddUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String curUserCode=request.getParameter("curUserCode");
		if(curUserCode!=null&&!curUserCode.equals("")){
			LimUser limUser = userBiz.findById(curUserCode);
			String name=limUser.getSalEmp().getSeName()+"["+limUser.getLimRole().getRolName()+"]";
			request.setAttribute("upUserCode", curUserCode);//添加下级时的上级账号
			request.setAttribute("upUserName", name);//上级账号的姓名和职位
			request.setAttribute("upUserRolLev",limUser.getLimRole().getRolLev());//上级账号的职级
			request.setAttribute("isAddLowLev", "1");
		}
		else{
			request.setAttribute("isAddLowLev", "0");
		}
		return mapping.findForward("addUser");
	}
	/**
	 * 添加账号 <br>
	 * @param request
	 *      para >	upCode:上级账号;	seNo:当前账号使用人ID;
	 */
	public ActionForward addEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		LimUser limUser = (LimUser) form1.get("limUser");
		String upCode=request.getParameter("upCode");//上级账号
		String seNo=request.getParameter("seNo");//当前账号使用人ID
		String userCode=userBiz.getMinCode();//得到usercode
		if(StringFormat.isEmpty(userCode)){
			request.setAttribute("errorMsg", "帐号数已达上限，无法新建帐号！");
			return mapping.findForward("error");
		}
		else{
			LimUser limUser1 = userBiz.findById(userCode);
			if(upCode!= null &&!upCode.equals("")){
				LimUser upUser = userBiz.findById(upCode);
				limUser1.setLimUser(upUser);
				String upUserNum = upUser.getUserNum();
				String userNum = userCode + upUserNum;
				limUser1.setUserNum(userNum);
			} else {
				limUser1.setLimUser(null);
				limUser1.setUserNum(userCode);
			}
			SalEmp salEmp = empBiz.salEmpDesc(Long.valueOf(seNo));
			String seName = salEmp.getSeName();
			limUser1.setSalEmp(salEmp);
			limUser1.setSalOrg(salEmp.getSalOrg());
			limUser1.setLimRole(salEmp.getLimRole());
			limUser1.setUserSeName(seName);
			String log = salEmp.getSeLog();
			String seLog = "";
			if (log != null && !log.equals("")) {
				seLog = log + "<br>" + seName + "于"
						+ GetDate.parseStrTime(GetDate.getCurTime()) + "启用" + userCode
						+ "账号。";
			} else {
				seLog = seName + "于" + GetDate.parseStrTime(GetDate.getCurTime()) + "启用"
						+ userCode + "账号。";
			}
			salEmp.setSeLog(seLog);
			salEmp.setSeUserCode(userCode);
		    limUser1.setUserIsenabled("1");
			limUser1.setUserLoginName(limUser.getUserLoginName());
			limUser1.setUserDesc(limUser.getUserDesc());
			limUser1.setUserPwd(limUser.getUserPwd());
			userBiz.updateUser(limUser1);
			empBiz.updateSalEmp(salEmp);
			request.setAttribute("msg", "新建账号");
			return mapping.findForward("popDivSuc");
		}
	}

	/**
	 * 跳转到修改账号信息页面 <br>
	 * create_date: Aug 24, 2010,5:13:21 PM
	 * @param mapping
	 * @param form
	 * @param request
	 *      parameter > userCode:要修改的账号
	 * @param response
	 * @return ActionForward 跳转到updateAddEmp页面<br>
	 * 		attribute > upUserCode:待编辑的账号的上级账号 upUserRolLev:待编辑的账号的上级账号的职级 limUser:要修改的账号实体
	 *                  upUserName:待编辑的账号的上级账号的姓名和职位
	 */
	public ActionForward goUpdate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String curUserCode = request.getParameter("curUserCode");
		LimUser limUser = userBiz.findById(curUserCode);
		request.setAttribute("limUser", limUser);
		return mapping.findForward("updateAddEmp");
	}

	/**
	 * 修改账号使用信息 <br>
	 * @param request
	 *      para >	curUserCode:要修改的账号;	seNo:账号使用人员ID;	oldSeNo:原账号使用人员ID;	userCode:上级账号;
	 */
	public ActionForward updateAddEmp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		LimUser limUser = (LimUser) form1.get("limUser");
		String curUserCode = request.getParameter("curUserCode");//待修改的账号
		String seNo = request.getParameter("seNo");//待修改账号当前使用人ID
		String oldSeNo = request.getParameter("oldSeNo");//待修改账号原使用人ID
		String upCode = request.getParameter("userCode");//上级账号
		String userNum = "";
		
		LimUser limUser1 = userBiz.findById(curUserCode);
		//保存对应员工、部门和职位（更新员工表内使用日志）
		if (!seNo.equals(oldSeNo)) {
			SalEmp oldEmp = limUser1.getSalEmp();
			if(oldEmp != null){
				String oldLog = oldEmp.getSeLog();
				if (oldLog != null && !oldLog.equals("")) {
					oldLog = oldLog + "<br>" + oldEmp.getSeName() + "于"
							+ GetDate.parseStrTime(GetDate.getCurTime()) + "停用" + curUserCode
							+ "账号。";
				} else {
					oldLog = oldEmp.getSeName() + "于" + GetDate.parseStrTime(GetDate.getCurTime()) + "停用"
							+ curUserCode + "账号。";
				}
				oldEmp.setSeLog(oldLog);
				oldEmp.setSeUserCode(null);
				empBiz.updateSalEmp(oldEmp);
			}
			SalEmp salEmp = empBiz.salEmpDesc(Long.valueOf(seNo));
			String log = salEmp.getSeLog();
			String seName = salEmp.getSeName();
			
			limUser1.setSalEmp(salEmp);
			limUser1.setSalOrg(salEmp.getSalOrg());
			limUser1.setLimRole(salEmp.getLimRole());
			limUser1.setUserSeName(seName);
			//limUser1.setUserIsenabled("1");//状态改为启用
			String seLog = "";
			if (log != null && !log.equals("")) {
				seLog = log + "<br>" + seName + "于"
						+ GetDate.parseStrTime(GetDate.getCurTime()) + "开始使用" + curUserCode
						+ "账号。";
			} else {
				seLog = seName + "于" + GetDate.parseStrTime(GetDate.getCurTime()) + "开始使用"
						+ curUserCode + "账号。";
			}
			salEmp.setSeLog(seLog);
			salEmp.setSeUserCode(curUserCode);
			empBiz.updateSalEmp(salEmp);
		}
		limUser1.setUserLoginName(limUser.getUserLoginName());
		limUser1.setUserPwd(limUser.getUserPwd());
		limUser1.setUserDesc(limUser.getUserDesc());
		//设置用户码
		if (upCode == null || upCode.equals("")) {//无上级账号
			userNum = limUser1.getUserCode();
			limUser1.setLimUser(null);
			limUser1.setUserNum(userNum);
		} else {
			LimUser upUser = userBiz.findById(upCode);
			userNum = limUser1.getUserCode() + upUser.getUserNum();
			limUser1.setLimUser(upUser);
			limUser1.setUserNum(userNum);
		}
		userBiz.UpdateLowUserNum(limUser1, userNum);//为下级重新生成用户码
		userBiz.updateUser(limUser1);
		request.setAttribute("msg", "编辑账号");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 锁定用户 <br>
	 * @param request
	 *      para >	userCode:账号;	islock:值为0表示解除锁定值为1表示锁定账号;
	 * @return ActionForward 重定向到"userAction.do?op=getUser&userCode="+userCode
	 */
	public ActionForward lockUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userCode = request.getParameter("userCode");
		String islock = request.getParameter("islock");
		LimUser limUser = userBiz.findById(userCode);
		if (islock != null && !islock.equals("")) {
			if (islock.equals("1")){
				limUser.setUserIsenabled("2");
			}
			else {
				limUser.setUserIsenabled("1");
			}
			userBiz.updateUser(limUser);
		}
		String url = "userAction.do?op=getUser&userCode=" + userCode;
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 跳转到确认删除 <br>
	 * @param request
	 *      para >	code:要删除的实体ID;	delType:删除类型;
	 * @param response
	 * @return ActionForward 跳转到delConfirm页面<br>
	 * 		attribute > code:要删除的实体ID delType:删除类型
	 */
	public ActionForward delConfirm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//		if (request.getParameter("type") != null) {
		//			String type = request.getParameter("type");
		//			request.setAttribute("type", type);
		//		}
		String code = request.getParameter("code");
		String delType = request.getParameter("delType");
		request.setAttribute("code", code);
		request.setAttribute("delType", delType);
		return mapping.findForward("delConfirm");
	}

	/**
	 * 关闭账号 <br>
	 * @param request
	 *      para >	userCode:待关闭的账号;
	 * @param response
	 * @return ActionForward 如果该账号存在下级账号跳转到faileDel页面 成功关闭账号跳转到popDivSuc页面<br>
	 * 		attribute > 该账号存在下级账号 ms:不能关闭的信息提示<br>
	 *      attribute > 成功关闭账号 msg:成功关闭账号的信息提示<br>
	 */
	public ActionForward delUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userCode = request.getParameter("userCode");
		LimUser limUser = userBiz.findById(userCode);
		//检查该用户没有下级
		List list = userBiz.checkDownCode(userCode);
		if (list.size() > 0) {
			request.setAttribute("ms", "请确认该用户没有下级用户！");
			return mapping.findForward("faileDel");
		} else {
			SalEmp salEmp = limUser.getSalEmp();
			if(salEmp != null){
				String oldLog = salEmp.getSeLog();
				if (oldLog != null && !oldLog.equals("")) {
					oldLog = oldLog + "<br>" + salEmp.getSeName() + "于"
							+ GetDate.parseStrTime(GetDate.getCurTime()) + "停用" + userCode
							+ "账号。";
				} else {
					oldLog = salEmp.getSeName() + "于" + GetDate.parseStrTime(GetDate.getCurTime()) + "停用"
							+ userCode + "账号。";
				}
				salEmp.setSeLog(oldLog);
				salEmp.setSeUserCode(null);
				empBiz.updateSalEmp(salEmp);
			}
			limUser.setUserNum(null);
			limUser.setLimUser(null);
			limUser.setUserSeName(null);
			limUser.setSalOrg(null);
			limUser.setLimRole(null);
			limUser.setSalEmp(null);
			limUser.setUserDesc(null);
			limUser.setUserIsenabled(null);
			limUser.setUserLoginName(null);//登录名
			limUser.setUserPwd(null);//密码
			userBiz.updateUser(limUser);
			request.setAttribute("msg", "关闭用户");
			return mapping.findForward("popDivSuc");
		}
	}

	/**
	 * 跳转到修改登录名密码页面 <br>
	 * @return ActionForward 跳转到updatePwd页面<br>
	 */
	public ActionForward goUpdatePwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		LimUser limUser = (LimUser) request.getSession().getAttribute("limUser");
		request.setAttribute("limUser", limUser);
		return mapping.findForward("updatePwd");
	}

	/**
	 * 修改登录名密码 <br>
	 * @param request
	 *      para >	userCode:要修改的账号;		userPwd:密码;	loginName:登陆名;
	 * @param response
	 * @return ActionForward 跳转到updatePwd页面<br>
	 * 		attr >	msg:修改成功信息提示
	 */
	public ActionForward updatePwd(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userCode = request.getParameter("userCode");
		LimUser limUser = userBiz.findById(userCode);
		String userPwd = request.getParameter("userPwd");
		String loginName = request.getParameter("loginName");
		limUser.setUserPwd(userPwd);
		limUser.setUserLoginName(loginName);
		userBiz.updateUser(limUser);
		request.setAttribute("limUser", limUser);
		request.setAttribute("msg", "信息修改成功！");
		return mapping.findForward("updatePwd");
	}

	/**
	 * 查看用户详细信息 <br>
	 * @param request
	 *      para >	userCode:账号;
	 * @param response
	 * @return ActionForward 跳转到limUser页面 <br>
	 * 		attr >	allUse:账号总数 isUse:已分配的账号数 notUse:未启用的账号数
	 *                  limUser:用户实体
	 */
	public ActionForward getUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userCode = request.getParameter("userCode");
		LimUser limUser = userBiz.getUser(userCode);
		
		if(limUser == null){
			return userManage(mapping, form, request, response);
		}
		else{
			int allUse = userBiz.getCountAllUse();//账号总数
			int notUse = userBiz.getCountNotUse();//未启用的账号数
			int isUse=allUse-notUse;
			request.setAttribute("allUse", allUse);
			request.setAttribute("isUse", isUse);
			request.setAttribute("notUse", notUse);
			request.setAttribute("limUser", limUser);
			return mapping.findForward("limUser");
		}
	}

	/**
	 * 职位查询 <br>
	 * @param request
	 *      para >	p:当前页码
	 * @return ActionForward 跳转到userRole页面<br>
	 * 		attr >	limRole:职位列表 page:分页信息
	 */
	public ActionForward searchLimRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		List list = userBiz.roleSerach();
		request.setAttribute("limRole", list);
		return mapping.findForward("userRole");
	}

	/**
	 * 保存用户职位 <br>
	 */
	public ActionForward saveRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm form1 = (DynaActionForm) form;
		LimRole limRole = (LimRole) form1.get("limRole");
		userBiz.saveRole(limRole);
		request.setAttribute("msg", "添加职位");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 跳转到修改职位页面 <br>
	 * @param request
	 *      para >	rolId:职位ID
	 * @param response
	 * @return ActionForward 跳转到updateRole页面<br>
	 * 		attr > limRole:职位实体
	 */
	public ActionForward goUpdateRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rolId = Long.parseLong(request.getParameter("rolId"));
		LimRole limRole = userBiz.getRole(rolId);
		request.setAttribute("limRole", limRole);
		return mapping.findForward("updateRole");
	}

	/**
	 * 修改职位信息 <br>
	 * @param request
	 *      para > rolId:职位ID rolName:职位名称 rolLev:职级 roleDesc:职位描述
	 * @param response
	 * @return ActionForward 跳转到popDivSuc页面<br>
	 * 		attr > msg:修改成功信息提示
	 */
	public ActionForward updateRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long rolId = Long.parseLong(request.getParameter("rolId"));
		LimRole limRole = userBiz.getRole(rolId);
		limRole.setRolName(request.getParameter("rolName"));
		limRole.setRolLev(Integer.parseInt(request.getParameter("rolLev")));
		limRole.setRoleDesc(request.getParameter("roleDesc"));
		userBiz.updateRole(limRole);
		request.setAttribute("msg", "修改职位");
		return mapping.findForward("popDivSuc");
	}

	/**
	 * 删除职位 <br>
	 * @param request
	 *      para > rolId:职位ID
	 * @param response
	 * @return ActionForward 不符合删除条件的跳转到error页面 成功删除跳转到popDivSuc页面<br>
	 * 		attr > 不符合删除条件 errorMsg:不符合删除条件信息提示(职位已被使用)<br>
	 *      attr > 成功删除 ，msg:成功删除信息提示<br>
	 */
	public ActionForward delRole(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String rolId = request.getParameter("rolId");
		LimRole limRole = userBiz.getRole(Long.parseLong(rolId));
		boolean isDel = true;
		StringBuffer eMsg = new StringBuffer();

		if(limRole.getSalEmps()!= null){
			int count = 0;
			int countDel = 0;
			Set<SalEmp> list2 = limRole.getSalEmps();
			Iterator<SalEmp> it = list2.iterator();
			while(it.hasNext()){
				SalEmp salEmp = it.next();
				if(salEmp.getSeIsenabled().equals("1")){
					count++;
				}else{
					countDel++;
				}
			}
			if(count>0 || countDel >0){
				isDel = false;
				StringFormat.getDelEMsg(eMsg,count,countDel,"员工");
			}
		}
		if(userBiz.checkRole(rolId)!= null){
			int count = 0;
			int countDel = 0;
			List<LimUser> list1 = userBiz.checkRole(rolId);
			Iterator<LimUser> it1 = list1.iterator();
			while(it1.hasNext()){
				LimUser limUser = it1.next();
				if(limUser.getUserIsenabled().equals("1")){
					count++;
				}else{
					countDel++;
				}
			}
			if(count > 0 || countDel >0){
				isDel = false;
				StringFormat.getDelEMsg(eMsg,count,countDel,"用户");
			}
		}
		if(isDel){
			userBiz.delRole(limRole);
			request.setAttribute("msg", "删除职位");
			return mapping.findForward("popDivSuc");
		}else{
			request.setAttribute("errorMsg", "请先删除该职位下的关联数据："+eMsg.toString());
			return mapping.findForward("error");
		}
	
	}

	/**
	 * 修改职级 <br>
	 * @param request
	 *      para > xmlNum:职级
	 * @param response
	 * @return ActionForward 设置的最大职级小于正在使用的最大职级跳转到roleLev页面 成功修改跳转到popDivSuc页面<br>
	 * 		attr > 设置的最大职级小于正在使用的最大职级 msg:不能修改职级的信息提示<br>
	 *      attr > 成功修改 msg:成功修改的信息提示<br>
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public ActionForward roleXml(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws DocumentException, IOException {
		response.setContentType("application/xml;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		SAXReader reader = new SAXReader();
		String url = request.getSession().getServletContext().getRealPath("/")
				+ "system\\role.xml";
		Document document = reader.read(new File(url));
		Element root = document.getRootElement();
		List<Element> rolenums = root.elements("rolenum");
		String xlnum = request.getParameter("xmlNum");
		String maxLev = userBiz.maxRole();
		if (Integer.parseInt(maxLev) > Integer.parseInt(xlnum)) {
			request.setAttribute("msg", "您设置的最大职级必须大于正在使用中的最大职级！");
			return mapping.findForward("roleLev");
		} else {
			for (Element rolenum : rolenums) {
				Element num = rolenum.element("num");
				num.setText(xlnum);//修改值   
			}
			XMLWriter xmlWriter;
			try {
				xmlWriter = new XMLWriter(new FileWriter(url));
				xmlWriter.write(document);
				xmlWriter.flush();
				xmlWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		request.setAttribute("ms", xlnum);
		request.setAttribute("msg", "修改职级");
		return mapping.findForward("popDivSuc");
	}
	/**
	 * 生成负责账号树 <br>
	 * @param request
	 * 		para > type:判断选择树节点后的方法
	 * @param response
	 * @return ActionForward limUserTree<br>
	 *      attr > orgList:部门列表 orgTopCode:部门顶级号（公司编号）
	 */
	public ActionForward limUserTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String type = request.getParameter("type");
		String removeUser = request.getParameter("removeUser");
		//		String roleLev=request.getParameter("roleLev");
		List<LimUser> limUsers = null;
		if (removeUser != null && !removeUser.equals("")) {//树中不显示此账号
			limUsers = userBiz.getUserWithOut(removeUser);
		} else {
			limUsers = userBiz.listValidUser();
		}
		request.setAttribute("limUsers", limUsers);
		request.setAttribute("type", type);
		return mapping.findForward("limUserTree");
	}
	/**
	 * 设置某一账号的权限 <br>
	 * @param request
	 *      parameter > userCode:账号 funType:功能类型 setLim:表示设置仓库权限 chkbLim1:仓库权限码 chkbLim:权限码
	 * @param response
	 * @return ActionForward 重定向到"userAction.do?op=getFunOperate&funType="+funType+"&userCode="+userCode<br>
	 */
	public ActionForward setFunLim(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String userCode = request.getParameter("userCode");
		String funType = request.getParameter("funType");
		String limType = request.getParameter("setLim");
		String[] userLim1 = request.getParameterValues("chkbLim1");
		String[] userLim2 = request.getParameterValues("chkbLim");
		
		userBiz.delRUserRig(userCode, funType);//删除原用户权限码
		if (limType != null && limType.equals("wms") && userLim1 != null
				&& userLim2 != null) {
			String[] userLims = new String[userLim1.length + userLim2.length];
			int n = 0;
			for (int i = 0; i < userLim1.length; i++) {
				userLims[n++] = userLim1[i];
			}
			for (int i = 0; i < userLim2.length; i++) {
				userLims[n++] = userLim2[i];
			}
			userBiz.addRUserRig(userLims, userCode, funType);
		} else {
			userBiz.addRUserRig(userLim2, userCode, funType);
		}
		String url = "userAction.do?op=getFunOperate&funType=" + funType
				+ "&userCode=" + userCode;
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得所有的操作权限 <br>
	 * @param request
	 *       parameter > userCode:账号 funType:功能类型
	 * @param response
	 * @return ActionForward funType值为cus跳转到cusLimSetting页面，funType值为sal跳转到salLimSetting页面，funType值为wms跳转到wmsLimSetting页面，
	 *                       funType值为proj跳转到proLimSetting页面，funType值为oa跳转到copLimSetting页面，funType值为hr跳转到hrLimSetting页面，
	 *                       funType值为pur跳转到purLimSetting页面，funType值为acc跳转到accLimSetting页面，funType值为sys跳转到sysLimSetting页面，
	 *                       funType值为sta跳转到staLimSetting页面<br>
	 * 		 attribute > funType值为cus user:账号实体 cusOperList:客户管理权限列表<br>
	 *       attribute > funType值为sal user:账号实体 salOperList:销售管理权限列表<br>
	 *       attribute > funType值为wms user:账号实体 wmsOperList:库存管理权限列表<br>
	 *       attribute > funType值为proj user:账号实体 proOperList:项目管理权限列表<br>
	 *       attribute > funType值为oa user:账号实体 copOperList:协同办公权限列表<br>
	 *       attribute > funType值为hr user:账号实体 hrOperList:人事管理权限列表<br>
	 *       attribute > funType值为pur user:账号实体 purOperList:采购管理权限列表<br>
	 *       attribute > funType值为acc user:账号实体 accOperList:财务管理权限列表<br>
	 *       attribute > funType值为sys user:账号实体 sysOperList:系统设置权限列表<br>
	 *       attribute > funType值为sta user:账号实体 staOperList:统计分析权限列表<br>
	 */
	public ActionForward getFunOperate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String funType = request.getParameter("funType");
		String userCode = request.getParameter("userCode");
		
		LimUser limUser = userBiz.findById(userCode);
		List<RUserRig> userLimList = userBiz.getRUserRig(userCode, funType);//获得某个用户在某个功能类型下的所有权限码
		List<LimRight> list = userBiz.getLimRight(funType);//获得某个功能类型下的所有权限码
		Iterator<RUserRig> userLim = userLimList.iterator();
		while (userLim.hasNext()) {
			RUserRig ruserRig = (RUserRig) userLim.next();
			Iterator<LimRight> lims = list.iterator();
			while (lims.hasNext()) {
				LimRight limRight = (LimRight) lims.next();
				if (ruserRig.getLimRight().getRigCode().equals(
						limRight.getRigCode())) {
					limRight.setEnabledType("checked");
				}
			}
		}
		request.setAttribute("user", limUser);
		request.setAttribute("funType",funType);
		if (funType != null && funType.equals("cus")) {
			request.setAttribute("cusOperList", list);
			return mapping.findForward("cusLimSetting");
		}else if (funType != null && funType.equals("serv")) {
			request.setAttribute("servOperList", list);
			return mapping.findForward("servLimSetting");
		} else if (funType != null && funType.equals("sal")) {
			request.setAttribute("salOperList", list);
			return mapping.findForward("salLimSetting");
		} else if (funType != null && funType.equals("wms")) {
			request.setAttribute("wmsOperList", list);
			return mapping.findForward("wmsLimSetting");
		} else if (funType != null && funType.equals("proj")) {
			request.setAttribute("proOperList", list);
			return mapping.findForward("proLimSetting");
		} else if (funType != null && funType.equals("oa")) {
			request.setAttribute("copOperList", list);
			return mapping.findForward("copLimSetting");
		} else if (funType != null && funType.equals("hr")) {
			request.setAttribute("hrOperList", list);
			return mapping.findForward("hrLimSetting");
		} else if (funType != null && funType.equals("pur")) {
			request.setAttribute("purOperList", list);
			return mapping.findForward("purLimSetting");
		} else if (funType != null && funType.equals("acc")) {
			request.setAttribute("accOperList", list);
			return mapping.findForward("accLimSetting");
		} else if (funType != null && funType.equals("sys")) {
			request.setAttribute("sysOperList", list);
			return mapping.findForward("sysLimSetting");
		} else if (funType != null && funType.equals("sta")) {
			request.setAttribute("staOperList", list);
			return mapping.findForward("staLimSetting");
		} else
			return null;
	}

	/**
	 * 系统初始化 <br>
	 * @param request
	 *      parameter > cusName:注册名称 password:授权码
	 * @return ActionForward 初始化成功跳转到iniSuc页面 注册名或密码错误跳转到login页面<br>
	 * 		attribute > 注册名或密码错误 msg:注册名或密码错误信息提示 isHavLogin:值为1系统不自动登陆 isReg:值为1显示系统激活页面
	 */
	public ActionForward iniSystem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusName = request.getParameter("cusName");
		String passWord = request.getParameter("password");
		String initInfo[] = new String[4];
		String fileName = request.getSession().getServletContext().getRealPath(
				"/")+ "system\\init.dll";
		JdbcCon jdbcCon = new JdbcCon();
		try{
			if (jdbcCon.check(cusName, passWord)) {
				initInfo = jdbcCon.getInitInfor(cusName, passWord);
				String initInfor = initInfo[0] + "," + initInfo[1] + ","
						+ initInfo[2] + "," + initInfo[3];
				EncryptGen.encryptFile(fileName, initInfor);
				//重新激活判断
				SalOrg salOrg1 = empBiz.salOrgDesc(ORGTOPCODE);
				if (salOrg1 == null) {
	
					//初始化组织表
					SalOrg salOrg = new SalOrg();
					salOrg.setSoCode(ORGTOPCODE);
					salOrg.setSoName(cusName);
					salOrg.setSoIsenabled("1");
					empBiz.save(salOrg);
					//初始化角色表
	//				LimRole limRole = new LimRole();
	//				limRole.setRolLev(1);
	//				limRole.setRolName("boss");//最高职位
	//				userBiz.saveRole(limRole);
					//初始化员工表
					SalEmp salEmp =new SalEmp();
					salEmp.setSeIsenabled("2");//隐藏不可操作
					salEmp.setSeName("超级管理员");
					empBiz.save(salEmp);
					//初始化用户表
					String userCode = "u000";
					LimUser limUser = new LimUser();
					limUser.setUserCode(userCode);
					limUser.setUserIsenabled("3");
					limUser.setUserLoginName("admin");
					limUser.setUserPwd("admin");
					//limUser.setLimRole(limRole);
					//limUser.setSalOrg(salOrg);
					//limUser.setUserNum(userCode);
					limUser.setSalEmp(salEmp);
					limUser.setUserSeName(salEmp.getSeName());
					userBiz.saveLim(limUser);
	
					//初始化账户
					for (int i = 1; i <= Integer.parseInt(initInfo[0]); i++) {
						String uCode = "";
						if (i < 10) {
							uCode = "u00" + i;
						}
						if (i >= 10 && i < 100) {
							uCode = "u0" + i;
						}
						if (i >= 100 && i < 1000) {
							uCode = "u" + i;
						}
						LimUser limUser1 = new LimUser();
						limUser1.setUserCode(uCode);
						userBiz.saveLim(limUser1);
					}
				}
				jdbcCon.updUserState(cusName, passWord);
				jdbcCon.closeAll();
				return mapping.findForward("iniSuc");
			}
			else {
				request.setAttribute("msg", "注册名称或授权码填写不正确！");
				request.setAttribute("isReg", "1");
				request.setAttribute("isHavLogin", "1");
				jdbcCon.closeAll();
				return mapping.findForward("login");
			}
		}
		catch(ClassNotFoundException ex){
			ex.printStackTrace();
			request.setAttribute("msg", "SQL驱动文件丢失，无法执行查询！");
			request.setAttribute("isReg", "1");
			request.setAttribute("isHavLogin", "1");
			jdbcCon.closeAll();
			return mapping.findForward("login");
		}
		catch(SQLException ex){
			ex.printStackTrace();
			request.setAttribute("msg", "无法连接远程服务器，请确保已正确连接网络！");
			request.setAttribute("isReg", "1");
			request.setAttribute("isHavLogin", "1");
			jdbcCon.closeAll();
			return mapping.findForward("login");
		}
		catch(Exception ex){
			ex.printStackTrace();
			request.setAttribute("errorMsg", ex.toString());
			return mapping.findForward("error");
		}
	}

	/**
	 * 修改授权信息 <br>
	 * @param request
	 *      parameter > cusName:注册名称 password:授权码 isLoginPage:标识是否是重新激活
	 * @return ActionForward 注册名授权码符合但实际账号数量大于配置文件中的账号数量跳转到error页面 
	 *                       条件都符合isLoginPage不为空跳转到iniSuc页面否则跳转到popDivSuc页面
	 *                       注册名或授权码不符合isLoginPage不为空跳转到login页面
	 *                       注册名或授权码不符合isLoginPage为空跳转到error页面<br>
	 * 		attribute > 注册名授权码符合但实际账号数量大于配置文件中的账号数量 errorMsg:提示导入授权信息失败<br>
	 *      attribute > 条件都符合isLoginPage为空 msg:提示导入授权信息成功<br>
	 *      attribute > 注册名或授权码不符合isLoginPage不为空 msg:提示注册名称或授权码填写不正确 isHavLogin:值为1系统不自动登陆 isReg:值为1显示系统激活页面<br>
	 *      attribute > 注册名或授权码不符合isLoginPage为空 errorMsg:提示授权信息更新失败<br>
	 */
	public ActionForward updateSystem(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String cusName = request.getParameter("cusName");
		String passWord = request.getParameter("password");
		String isLoginPage = request.getParameter("isLoginPage");
		String initInfo[] = new String[4];
		String fileName = request.getSession().getServletContext().getRealPath(
				"/") + "system\\init.dll";
		JdbcCon jdbcCon = new JdbcCon();
		try {
			if (jdbcCon.check(cusName, passWord)) {
				initInfo = jdbcCon.getInitInfor(cusName, passWord);
				int userNum1 = userBiz.getCountAllUse();//账号总数量
				int userNum = 0;
				String maxCode = userBiz.getMaxCode().substring(1);
				if (maxCode != null && !maxCode.equals(""))
					userNum = Integer.parseInt(maxCode);//最大账号code
				if (userNum1 <= Integer.parseInt(initInfo[0])) {
					File file = new File(fileName);
					if (file.exists()) {
						file.delete();//删除.dll文件
					}
					String initInfor = initInfo[0] + "," + initInfo[1] + ","
							+ initInfo[2] + "," + initInfo[3];
					try {
						EncryptGen.encryptFile(fileName, initInfor);//重新创建.dll文件
					} catch (Exception e) {
						e.printStackTrace();
					}
					int len = Integer.parseInt(initInfo[0]);
					for (int i = 1; i <= len - userNum1; i++) {
						String uCode = "";
						int addCode = userNum + i;
						if (userNum < 10) {
							uCode = "u00" + addCode;
						}
						if (userNum >= 10 && userNum < 100) {

							uCode = "u0" + addCode;
						}
						if (userNum >= 100 && userNum < 1000) {
							uCode = "u" + addCode;
						}
						LimUser limUser1 = new LimUser();
						limUser1.setUserCode(uCode);
						userBiz.saveLim(limUser1);//修改授权信息
					}
					jdbcCon.updUserState(cusName, passWord);
				} else {
					jdbcCon.closeAll();
					request.setAttribute("errorMsg", "导入授权信息失败");
					return mapping.findForward("error");
				}
				jdbcCon.closeAll();
				if (isLoginPage != null && isLoginPage.equals("1")) {
					return mapping.findForward("iniSuc");
				} else {
					request.setAttribute("msg", "导入授权信息");
					return mapping.findForward("popDivSuc");
				}
			} else {
				jdbcCon.closeAll();
				if (isLoginPage != null && isLoginPage.equals("1")) {
					request.setAttribute("errorMsg", "注册名称或授权码填写不正确！");
					request.setAttribute("isReg", "1");
					request.setAttribute("isHavLogin", "1");
					return mapping.findForward("login");
				} else {
					request.setAttribute("errorMsg", "授权信息更新失败！");
					return mapping.findForward("error");
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", e.toString());
			return mapping.findForward("error");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", "SQL驱动文件丢失，无法执行查询！");
			return mapping.findForward("error");
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", "无法连接远程服务器，请确保已正确连接网络！");
			return mapping.findForward("error");
		}
	}


	public void setUserBiz(UserBIZ userBiz) {
		this.userBiz = userBiz;
	}

	public void setEmpBiz(EmpBIZ empBiz) {
		this.empBiz = empBiz;
	}
	
	public void setMessageBiz(MessageBIZ messageBiz) {
		this.messageBiz = messageBiz;
	}
}