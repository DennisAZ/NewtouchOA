package oa.spring.control.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.service.FinanceService;
import oa.spring.service.SystemService;
import oa.spring.util.OpenAccount;
import oa.spring.util.StaticData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

@Controller
@RequestMapping("/system")
public class SystemController {

	@Autowired
	private FinanceService financeService;
	@Autowired
	private SystemService systemService;
	
	private static final Logger logger = Logger.getLogger(SystemController.class);

	/**
	 * 
	 * @param tableName
	 *            表名称
	 * 
	 * @return 要展示的对象的集合
	 */
	@RequestMapping(value = "getCodeMsg", method = RequestMethod.POST)
	public @ResponseBody String getWareHouse1(HttpServletRequest request,HttpServletResponse response) {
		 Connection dbConn = null;
    	 String str = "";
        try {
        	YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        	dbConn = requestDbConn.getSysDbConn();
        	String sql="SELECT id,code_type,value,remark from erp_code_style";
        	logger.debug(sql);
        	str = new String(financeService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return str;
	}
	
	@RequestMapping(value = "getCodeType", method = RequestMethod.POST)
    public @ResponseBody String  getCodeType(HttpServletRequest request,HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String code_type =  request.getParameter("code_type");
		try {
		if("CODE1".equals(code_type)){
				 response.getWriter().write(StaticData.CODE1);
			}else if("CODE2".equals(code_type)){
				response.getWriter().write(StaticData.CODE2);
			}else if("CODE3".equals(code_type)){
				response.getWriter().write(StaticData.CODE3);
			}else if("CODE4".equals(code_type)){
				response.getWriter().write(StaticData.CODE4);
			}else if("CODE5".equals(code_type)){
				response.getWriter().write(StaticData.CODE5);
			}else if("CODE6".equals(code_type)){
				response.getWriter().write(StaticData.CODE6);
			}else if("CODE7".equals(code_type)){
				response.getWriter().write(StaticData.CODE7);
			}else if("CODE8".equals(code_type)){
				response.getWriter().write(StaticData.CODE8);
			}else if("CODE9".equals(code_type)){
				response.getWriter().write(StaticData.CODE9);
			}else if("CODE10".equals(code_type)){
				response.getWriter().write(StaticData.CODE10);
			}else if("CODE11".equals(code_type)){
				response.getWriter().write(StaticData.CODE11);
			}else if("CODE12".equals(code_type)){
				response.getWriter().write(StaticData.CODE12);
			}else if("CODE13".equals(code_type)){
				response.getWriter().write(StaticData.CODE13);
			}else if("CODE14".equals(code_type)){
				response.getWriter().write(StaticData.CODE14);
			}else if("CODE15".equals(code_type)){
				response.getWriter().write(StaticData.CODE15);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "addCode", method = RequestMethod.POST)
    public @ResponseBody String addCode(HttpServletRequest request) {
		 String rtStr = "0";
		 
		 String code_type = request.getParameter("code_type");
		 String value = request.getParameter("value");
		 String remark = request.getParameter("remark");
		 Map<String,Object> map = new HashMap<String,Object>();
		 map.put("code_type", code_type);
		 map.put("value", value);
		 map.put("remark", remark);
		 
		try {
			systemService.addCode(map);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
    }
	@RequestMapping(value = "updateCode", method = RequestMethod.POST)
	public @ResponseBody String updateCode(HttpServletRequest request) {
		String rtStr = "0";
		
		String id = request.getParameter("id");
		String code_type = request.getParameter("code_type");
		String value = request.getParameter("value");
		String remark = request.getParameter("remark");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", id);
		map.put("code_type", code_type);
		map.put("value", value);
		map.put("remark", remark);
		
		try {
			systemService.updateCode(map);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "deleteCodeById", method = RequestMethod.POST)
	public @ResponseBody String deleteCodeById(HttpServletRequest request) {
		String rtStr = "0";
		String id = request.getParameter("id");
		
		try {
			systemService.deleteCodeById(id);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	
	@RequestMapping(value = "getCodeMsgById", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getCodeMsgById(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		String id = request.getParameter("id");
		try {
			map = systemService.getCodeMsgById(id);
		} catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value = "getAutoCode", method = RequestMethod.POST)
    public @ResponseBody String  getAutoCode(HttpServletRequest request,HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		String code_type =  request.getParameter("code_type");
		YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
		try {
			response.getWriter().write(systemService.getAutoCode(code_type,person.getUserName()));;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(value = "openAccount", method = RequestMethod.POST)
	public @ResponseBody String  openAccount() {
		OpenAccount openAccount = new OpenAccount();
		return openAccount.openAccount();
	}
	//查询物流信息
	@RequestMapping(value = "getLogistics/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody String getLogistics(HttpServletRequest request,HttpServletResponse response) {
		 Connection dbConn = null;
    	 String str = "";
        try {
        	YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        	dbConn = requestDbConn.getSysDbConn();
        	String sql="SELECT id,name from erp_logistics";
        	logger.debug(sql);
        	str = new String(financeService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"),"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return str;
	}
	//查看物流信息
	@RequestMapping(value = "getLogisticsById", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getLogisticsById(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		String id = request.getParameter("id");
		try {
			map = systemService.getLogisticsById(id);
		} catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	//查看物流信息
	@RequestMapping(value = "getSaleLogistics/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> getSaleLogistics(HttpServletRequest request) {
			List<Map<String,Object>> list = systemService.getLogistics();
		return list;
	}
	
	//设置物流信息
	@RequestMapping(value = "addLogistics", method = RequestMethod.POST)
    public @ResponseBody String addLogistics(HttpServletRequest request) {
		 String rtStr = "0";
		 
		 String logistics = request.getParameter("logistics");
		 Map<String ,Object>logisticsMap=new HashMap<String ,Object>();
		 String logisticsId=UUID.randomUUID().toString();
		 logisticsMap.put("logistics", logistics);
		 logisticsMap.put("logisticsId", logisticsId);
		 
		try {
			systemService.addLogistics(logisticsMap);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
    }
	//设置物流信息
	@RequestMapping(value = "editLogistics", method = RequestMethod.POST)
	public @ResponseBody String editLogistics(HttpServletRequest request) {
		String rtStr = "0";
		
		String logistics = request.getParameter("logistics");
		String id = request.getParameter("id");
		Map<String ,Object>logisticsMap=new HashMap<String ,Object>();
		logisticsMap.put("logistics", logistics);
		logisticsMap.put("id", id);
		
		try {
			systemService.editLogistics(logisticsMap);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	//设置物流信息
	@RequestMapping(value = "deleteLogistics", method = RequestMethod.POST)
	public @ResponseBody String deleteLogistics(HttpServletRequest request) {
		String rtStr = "0";
		String id = request.getParameter("id");
		
		try {
			systemService.deleteLogistics(id);
		} catch(Exception e){
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
}
