package oa.spring.control.rest;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.service.DBService;
import oa.spring.util.DoubleCaulUtil;
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

@Controller
@RequestMapping("/db")
public class DBController {

	@Autowired
	private DBService dbService;

	private static final Logger logger = Logger.getLogger(DBController.class);

	@RequestMapping(value = "getDBMsg", method = RequestMethod.POST)
	public @ResponseBody
	String getDBMsg(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String param=request.getParameter("param");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(dbService.getDBMsg(dbConn,param,
					request.getParameterMap()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "getDBDetail", method = RequestMethod.POST)
	public @ResponseBody
	String getDBDetail(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String whId=request.getParameter("whId");
		String proId=request.getParameter("proId");
		String batch=request.getParameter("batch");
		String beginTime=request.getParameter("beginTime");
		String endTime=request.getParameter("endTime");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql = "";
			sql = "SELECT wh.name,pro.pro_code,pro.pro_name,dbLog.batch,dblog.flag,dbLog.number,dbLog.time" +
					" FROM erp_db_log dbLog LEFT JOIN erp_warehouse wh ON dbLog.wh_id = wh.id LEFT JOIN erp_product pro ON dbLog.pro_id = pro.id" +
					" WHERE dbLog.batch='"+batch+"' and dbLog.wh_id='"+whId+"' and dbLog.pro_id='"+proId+"'";
			if (!"".equals(beginTime)) {
				sql += " and date_format(time,'%Y-%m-%d') >= '"
					+ beginTime + "'";
			}
			if (!"".equals(endTime)) {
				sql += " and date_format(time,'%Y-%m-%d') <= '" + endTime
				+ "'";
			}
			
			sql+=" ORDER BY time desc";
			str = new String(dbService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "getDBLog", method = RequestMethod.POST)
	public @ResponseBody
	String getDBLog(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String flag=request.getParameter("flag");
		String beginTime=request.getParameter("beginTime");
		String endTime=request.getParameter("endTime");
		String proName=request.getParameter("proName");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
			.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(dbService.getDBLog(dbConn,flag,beginTime,endTime,proName,
					request.getParameterMap()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	@RequestMapping(value = "getDBMsgById", method = RequestMethod.POST)
	public @ResponseBody
	Map<String,Object> getDBMsgById(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map<String,Object> map = dbService.getDBMsgById(id);
		return map;
	}

	@RequestMapping(value = "getWareHouse/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody
	List getWareHouse() {
		List list = new ArrayList();
		list = dbService.getWareHouse();
		return list;
	}
	@RequestMapping(value = "getWhType", method = RequestMethod.POST)
	public @ResponseBody
	Map getWhType(HttpServletRequest request) {
		String whId=request.getParameter("whId");
		Map map = dbService.getWhType(whId);
		return map;
	}

	@RequestMapping(value = "addDB", method = RequestMethod.POST)
	public @ResponseBody
	String addDB(HttpServletRequest request) {
		String rtStr = "0";
//		var para = "productIds="+productIds+"&prices="+prices+"&numbers="+numbers+"&batchs="+batchs+"&invalid_times="+invalid_times+"&warehouseId="+warehouseId;
		String wh_id = request.getParameter("warehouseId");//仓库
		String productIds = request.getParameter("productIds");//产品
		String prices = request.getParameter("prices");
		String numbers = request.getParameter("numbers");
		String batchs = request.getParameter("batchs");//产品批次
		String invalid_times = request.getParameter("invalid_times");//产品失效时间
		
		String[] idArr = productIds.split(",");
		String[] priceArr = prices.split(",");
		String[] numberArr = numbers.split(",");
		String[] batchArr = batchs.split(",");
		String[] invalid_timeArr = invalid_times.split(",");
		
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			
		for (int i = 0; i < idArr.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			//-------DB表所需数据------------
			map.put("wh_id", wh_id);
			map.put("pro_id", idArr[i]);
			map.put("price", priceArr[i]);
			map.put("number", numberArr[i]);
			map.put("batch", batchArr[i]);
			map.put("invalid_time", invalid_timeArr[i]);
			map.put("remark", "--操作员："+user.getUserName()+"-时间："+sdf.format(new Date()));
			//-------DB_log表所需数据------------
//			map.put("total", DoubleCaulUtil.round(DoubleCaulUtil.mul(priceArr[i],numberArr[i])));
			map.put("flag", StaticData.DB);
			map.put("DbStatus", StaticData.OVER);
			map.put("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

			list.add(map);
		}
		try {
				dbService.insertBatch(list);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "updateDB", method = RequestMethod.POST)
	public @ResponseBody
	String updateDB(HttpServletRequest request) {
		String rtStr = "0";
		//"id=<%=id%>&batch="+batch+"&price="+price+"&number="+number+"&invalid_time="+invalid_time;
		String id = request.getParameter("id");
		String batch = request.getParameter("batch");
		String invalid_time = request.getParameter("invalid_time");
		String price = request.getParameter("price");
		String number = request.getParameter("number");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("batch", batch);
		map.put("invalid_time", invalid_time);
		map.put("price", price);
		map.put("number", number);
		
		Map<String,Object> m = dbService.getDBById(id);
		//添加原来的库存、产品、批次编号是为了根据这些信息更新db_log的数据。
		map.put("wh_id", m.get("wh_id").toString());
		map.put("pro_id", m.get("pro_id").toString());
		map.put("old_batch", m.get("batch").toString());
		try {
			dbService.updateDB(map);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "deleteDBById", method = RequestMethod.POST)
	public @ResponseBody
	String deleteDBById(HttpServletRequest request) {
		String rtStr = "0";
		String id = request.getParameter("id");
		Map<String,Object> map=dbService.getDBById(id);
		try {
			dbService.deleteDBById(id,map);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	@RequestMapping(value = "checkBatckOnly", method = RequestMethod.POST)
	public @ResponseBody String checkBatckOnly(HttpServletRequest request) {
		String rtStr = "1";
		String wh_id = request.getParameter("whId");
		String pro_id = request.getParameter("proId");
		String batch = request.getParameter("batch");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("wh_id", wh_id);
		map.put("pro_id", pro_id);
		map.put("batch", batch);
		try {
			int i = dbService.checkBatckOnly(map);
			if(i > 0){
				rtStr = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtStr;
	}
}
