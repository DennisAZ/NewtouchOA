package oa.spring.control.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.po.WareHouse;
import oa.spring.service.SaleOrderService;
import oa.spring.service.WareHouseService;
import oa.spring.util.StaticData;
import oa.spring.util.DoubleCaulUtil;

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
@RequestMapping("/warehouse")
public class WareHouseController {

	@Autowired
	private WareHouseService whService;
	@Autowired
	private SaleOrderService saleOrderService;

	private static final Logger logger = Logger.getLogger(WareHouseController.class);

	/**
	 * 
	 * @param tableName
	 *            表名称
	 * 
	 * @return 要展示的对象的集合
	 */
	@RequestMapping(value = "showWHTree/{timestamp}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody
	List<WareHouse> getWareHouse1() {
		List<WareHouse> list = whService.getWareHouse();
		return list;
	}

	@RequestMapping(value = "getWareHouse/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody
	String getWareHouse(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String sql = "select id,name,address,type,remark from erp_warehouse";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(whService.yhPageDataJson(dbConn,
					request.getParameterMap(),sql).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	@RequestMapping(value = "getPCode", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getPCode(HttpServletRequest request,
			HttpServletResponse response) {
		String typeId = request.getParameter("typeId");
		String flag = request.getParameter("flag");
		Map<String, Object> map = whService.getPCode(typeId, flag);
		return map;
	}
	//查询每个仓库的库存批次号是否存在
	@RequestMapping(value = "getBatch", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getBatch(HttpServletRequest request,
			HttpServletResponse response) {
		String batch = request.getParameter("batch");
		String whId = request.getParameter("whId");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("batch", batch);
		map.put("whId", whId);
		Map<String, Object> boolMap = whService.getBatch(map);
		return boolMap;
	}

	// 根据商品id出库选择
	@RequestMapping(value = "getWhByProdId", method = RequestMethod.POST)
	public @ResponseBody
	String getWhByProdId(HttpServletRequest request,
			HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String proId = request.getParameter("proId");
		String type = request.getParameter("type");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql = "SELECT db.id AS dbId,wh.id AS whId,pro.id AS proId,wh.name AS whName,pro.pro_code,pro.pro_name,db.batch,db.num,db.invalid_time" +
					" FROM erp_db db LEFT JOIN erp_product pro ON pro.id = db.pro_id LEFT JOIN erp_warehouse wh ON wh.id = db.wh_id" +
					" WHERE db.pro_id = '"+proId+"' and db.num > 0";
			if("2".equals(type)){
				//来自生产退料
				sql = "SELECT db.id AS dbId,wh.id AS whId,pro.id AS proId,wh.name AS whName,pro.pro_code,pro.pro_name,db.batch,db.num,db.invalid_time" +
				" FROM erp_db db LEFT JOIN erp_product pro ON pro.id = db.pro_id LEFT JOIN erp_warehouse wh ON wh.id = db.wh_id" +
				" WHERE db.pro_id = '"+proId+"'";
			}
			str = new String(whService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	// 根据商品id出库选择
	@RequestMapping(value = "getWhProdId/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody
	String getWhProdId(HttpServletRequest request,
			HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql = "SELECT wh.id AS whId,wh.name AS whName,ps.name AS psName FROM erp_warehouse wh LEFT JOIN erp_product_style ps ON ps.id=wh.type";
			str = new String(whService.yhPageDataJson(dbConn,request.getParameterMap(),sql).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	

	@RequestMapping(value = "sendFormManage", method = RequestMethod.POST)
	public @ResponseBody
	String sendFormManage(HttpServletRequest request,
			HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String status = request.getParameter("status");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String sql = "";
		if ("-2".equals(status)) {
			sql = "SELECT po.id,o.order_code,po.po_code,po.po_title,po.order_id,po.po_status,po.remark FROM erp_order_product_out po,erp_sale_order o WHERE o.id = po.order_id AND (po.po_status='"
					+ StaticData.RUNNING
					+ "' or po.po_status='"
					+ StaticData.NEW_CREATE + "') ";
		} else if ("-3".equals(status)) {
			sql = "SELECT po.id,o.order_code,po.po_code,po.po_title,po.order_id,po.po_status,po.remark FROM erp_order_product_out po,erp_sale_order o WHERE o.id = po.order_id ";
		} else {
			sql = "SELECT po.id,o.order_code,po.po_code,po.po_title,po.order_id,po.po_status,po.remark FROM erp_order_product_out po,erp_sale_order o WHERE o.id = po.order_id AND (po.po_status='"
					+ status + "' )";
		}
		if (!"".equals(beginTime)) {
			sql += " and date_format(sign_date,'%Y-%m-%d') >= '"
					+ beginTime + "'";
		}
		if (!"".equals(endTime)) {
			sql += " and date_format(sign_date,'%Y-%m-%d') <= '" + endTime
					+ "'";
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(whService.yhPageDataJson(dbConn,
					request.getParameterMap(), sql)
					.getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	@RequestMapping(value = "returnManage", method = RequestMethod.POST)
	public @ResponseBody
	String returnManage(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String status = request.getParameter("status");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String sql = "";
		if ("-2".equals(status)) {
			sql = "SELECT ret.id AS retId,TYPE,CODE,reason,STATUS FROM erp_return ret where  STATUS='"+StaticData.NEW_CREATE+"'";
		} else if ("-3".equals(status)) {
			sql = "SELECT ret.id AS retId,TYPE,CODE,reason,STATUS FROM erp_return ret where 1=1";
		} else {
			sql = "SELECT ret.id AS retId,TYPE,CODE,reason,STATUS FROM erp_return ret  where STATUS='"+status+"'";


		}
		if (!"".equals(beginTime)) {
			sql += " and date_format(return_date,'%Y-%m-%d') >= '" + beginTime
					+ "'";
		}
		if (!"".equals(endTime)) {
			sql += " and date_format(return_date,'%Y-%m-%d') <= '" + endTime
					+ "'";
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(whService.yhPageDataJson(dbConn,
					request.getParameterMap(), sql)
					.getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	@RequestMapping(value = "showDetial", method = RequestMethod.POST)
	public @ResponseBody
	String showDetial(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String po_id = request.getParameter("po_id");
		String str = "";
		String status = request.getParameter("status");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String sql = "";
		if ("-2".equals(status)) {
			sql = "SELECT pod.id,pod.order_id,pod.po_id,o.order_code,po.po_code,pod.pod_code,pod.pod_status FROM erp_order_product_out_detail pod,erp_order_product_out po,erp_sale_order o WHERE pod.po_id = po.id AND po.order_id=o.id and  (pod_status='"
					+ StaticData.NEW_CREATE
					+ "' or pod_status ='"
					+ StaticData.RUNNING
					+ "') and pod.po_id='"
					+ po_id
					+ "'";
		} else if ("-3".equals(status)) {
			sql = "SELECT pod.id,pod.order_id,pod.po_id,o.order_code,po.po_code,pod.pod_code,pod.pod_status FROM erp_order_product_out_detail pod,erp_order_product_out po,erp_sale_order o WHERE pod.po_id = po.id AND po.order_id=o.id  and pod.po_id='"
					+ po_id + "'";
		} else {
			sql = "SELECT pod.id,pod.order_id,pod.po_id,o.order_code,po.po_code,pod.pod_code,pod.pod_status FROM erp_order_product_out_detail pod,erp_order_product_out po,erp_sale_order o WHERE pod.po_id = po.id AND po.order_id=o.id AND pod_status='"
					+ status + "'and pod.po_id='" + po_id + "'";

		}
		if (!"".equals(beginTime)) {
			sql += " and date_format(pod_date,'%Y-%m-%d') >= '" + beginTime
					+ "'";
		}
		if (!"".equals(endTime)) {
			sql += " and date_format(pod_date,'%Y-%m-%d') <= '" + endTime
					+ "'";
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(whService.yhPageDataJson(dbConn,
					request.getParameterMap(),sql).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	@RequestMapping(value = "whShowDetial", method = RequestMethod.POST)
	public @ResponseBody
	String whShowDetial(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String status = request.getParameter("status");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String sql = "";
		if ("-2".equals(status)) {
			sql = "SELECT pod.id,pod.order_id,pod.po_id,o.order_code,po.po_code,pod.pod_code,pod.pod_status FROM erp_order_product_out_detail pod,erp_order_product_out po,erp_sale_order o WHERE pod.po_id = po.id AND po.order_id=o.id and  (pod_status='"
					+ StaticData.NEW_CREATE
					+ "' or pod_status ='"
					+ StaticData.RUNNING+"') ";
		} else if ("-3".equals(status)) {
			sql = "SELECT pod.id,pod.order_id,pod.po_id,o.order_code,po.po_code,pod.pod_code,pod.pod_status FROM erp_order_product_out_detail pod,erp_order_product_out po,erp_sale_order o WHERE pod.po_id = po.id AND po.order_id=o.id ";
		} else {
			sql = "SELECT pod.id,pod.order_id,pod.po_id,o.order_code,po.po_code,pod.pod_code,pod.pod_status FROM erp_order_product_out_detail pod,erp_order_product_out po,erp_sale_order o WHERE pod.po_id = po.id AND po.order_id=o.id AND pod_status='"
					+ status + "'";

		}
		if (!"".equals(beginTime)) {
			sql += " and date_format(pod_date,'%Y-%m-%d') >= '" + beginTime
					+ "'";
		}
		if (!"".equals(endTime)) {
			sql += " and date_format(pod_date,'%Y-%m-%d') <= '" + endTime
					+ "'";
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(whService.yhPageDataJson(dbConn,
					request.getParameterMap(), sql)
					.getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	//退货单查询
	@RequestMapping(value = "dbLossManage", method = RequestMethod.POST)
	public @ResponseBody
	String dbLossManage(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String status = request.getParameter("status");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String sql = "";
		if ("-2".equals(status)) {
			sql = "SELECT id,CODE,reason,user_id,STATUS,loss_date,remark FROM erp_loss where status='"
					+ StaticData.RUNNING + "' or status='"+StaticData.NEW_CREATE+"'";
		} else if ("-3".equals(status)) {
			sql = "SELECT id,CODE,reason,user_id,STATUS,loss_date,remark FROM erp_loss WHERE 1=1";
		} else {
			sql = "SELECT id,CODE,reason,user_id,STATUS,loss_date,remark FROM erp_loss where status='"
					+ status + "'";
		}
		if (!"".equals(beginTime)) {
			sql += " and date_format(loss_date,'%Y-%m-%d') >= '"
					+ beginTime + "'";
		}
		if (!"".equals(endTime)) {
			sql += " and date_format(loss_date,'%Y-%m-%d') <= '" + endTime
					+ "'";
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(whService.yhPageDataJson(dbConn,
					request.getParameterMap(),sql)
					.getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	//查询所有大类
	@RequestMapping(value = "chooseType/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody
	String chooseType(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String sql = "select id,name,remark from erp_product_style where is_del='0'";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(whService.yhPageDataJson(dbConn,
					request.getParameterMap(),sql).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 采购货单查询
	@RequestMapping(value = "queryPur", method = RequestMethod.POST)
	public @ResponseBody
	String queryPur(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String purId = request.getParameter("purId");
		String status = request.getParameter("status");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		try {

			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			if (purId.equals("0") || purId == "0") {
				String sql = "";
				if ("-2".equals(status)) {
					sql = "SELECT  pout.id AS ppoId,pur.id AS purId,pd.id AS pdId,pur.code AS purCode,pout.ppo_code AS ppoCode,pd.code AS pdCode,pd.status AS pdStatus FROM  erp_purchase_product_in_detail pd LEFT JOIN erp_purchase pur ON pur.id=pd.purchase_id LEFT JOIN  erp_purchase_product_out pout  ON pout.purchase_id=pd.purchase_id where (pd.status='"
							+ StaticData.NEW_CREATE
							+ "' or pd.status='"
							+ StaticData.RUNNING + "') ";
				} else if ("-3".equals(status)) {
					sql = "SELECT  pout.id AS ppoId,pur.id AS purId,pd.id AS pdId,pur.code AS purCode,pout.ppo_code AS ppoCode,pd.code AS pdCode,pd.status AS pdStatus FROM  erp_purchase_product_in_detail pd LEFT JOIN erp_purchase pur ON pur.id=pd.purchase_id LEFT JOIN  erp_purchase_product_out pout  ON pout.purchase_id=pd.purchase_id where 1=1";
				} else {
					sql = "SELECT  pout.id AS ppoId,pur.id AS purId,pd.id AS pdId,pur.code AS purCode,pout.ppo_code AS ppoCode,pd.code AS pdCode,pd.status AS pdStatus FROM  erp_purchase_product_in_detail pd LEFT JOIN erp_purchase pur ON pur.id=pd.purchase_id LEFT JOIN  erp_purchase_product_out pout  ON pout.purchase_id=pd.purchase_id where  pd.status='"
							+ status + "'";
				}
				if (!"".equals(beginTime)) {
					sql += " and date_format(date,'%Y-%m-%d') >= '" + beginTime
							+ "'";
				}
				if (!"".equals(endTime)) {
					sql += " and date_format(date,'%Y-%m-%d') <= '" + endTime + "'";
				}
				str = new String(whService.yhPageDataJson(dbConn,
						request.getParameterMap(), sql)
						.getBytes("UTF-8"), "ISO-8859-1");

			} else {
				String sql = "";
				if ("-2".equals(status)) {
					sql = "SELECT  pout.id AS ppoId,pur.id AS purId,pd.id AS pdId,pur.code AS purCode,pout.ppo_code AS ppoCode,pd.code AS pdCode,pd.status AS pdStatus FROM  erp_purchase_product_in_detail pd LEFT JOIN erp_purchase pur ON pur.id=pd.purchase_id LEFT JOIN  erp_purchase_product_out pout  ON pout.purchase_id=pd.purchase_id where pd.purchase_id='"
							+ purId + "'";
				} else if ("-3".equals(status)) {
					sql = "SELECT  pout.id AS ppoId,pur.id AS purId,pd.id AS pdId,pur.code AS purCode,pout.ppo_code AS ppoCode,pd.code AS pdCode,pd.status AS pdStatus FROM  erp_purchase_product_in_detail pd LEFT JOIN erp_purchase pur ON pur.id=pd.purchase_id LEFT JOIN  erp_purchase_product_out pout  ON pout.purchase_id=pd.purchase_id where pd.purchase_id='"
							+ purId + "'";
				} else {
					sql = "SELECT  pout.id AS ppoId,pur.id AS purId,pd.id AS pdId,pur.code AS purCode,pout.ppo_code AS ppoCode,pd.code AS pdCode,pd.status AS pdStatus FROM  erp_purchase_product_in_detail pd LEFT JOIN erp_purchase pur ON pur.id=pd.purchase_id LEFT JOIN  erp_purchase_product_out pout  ON pout.purchase_id=pd.purchase_id where pd.purchase_id='"
							+ purId + "' and pd.status='" + status + "'";
				}
				if (!"".equals(beginTime)) {
					sql += " and date_format(date,'%Y-%m-%d') >= '" + beginTime
							+ "'";
				}
				if (!"".equals(endTime)) {
					sql += " and date_format(date,'%Y-%m-%d') <= '" + endTime + "'";
				}
				str = new String(whService.yhPageDataJson(dbConn,
						request.getParameterMap(), sql).getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 采购货单查询
	@RequestMapping(value = "queryPurOut", method = RequestMethod.POST)
	public @ResponseBody
	String queryPurOut(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String status = request.getParameter("status");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		String sql = "";
		if ("-2".equals(status)) {
			sql = "SELECT  pout.id AS ppoId,pur.id AS purId,pur.code, ppo_code,ppo_title,ppo_status,pout.remark AS ppoRemark FROM erp_purchase_product_out pout LEFT JOIN erp_purchase pur ON pur.id=pout.purchase_id WHERE pout.ppo_status='"
					+ StaticData.RUNNING + "'";
		} else if ("-3".equals(status)) {
			sql = "SELECT  pout.id AS ppoId,pur.id AS purId,pur.code, ppo_code,ppo_title,ppo_status,pout.remark AS ppoRemark FROM erp_purchase_product_out pout LEFT JOIN erp_purchase pur ON pur.id=pout.purchase_id WHERE 1=1";
		} else {
			sql = "SELECT  pout.id AS ppoId,pur.id AS purId,pur.code, ppo_code,ppo_title,ppo_status,pout.remark AS ppoRemark FROM erp_purchase_product_out pout LEFT JOIN erp_purchase pur ON pur.id=pout.purchase_id WHERE pout.ppo_status='"
					+ status + "'";
		}
		if (!"".equals(beginTime)) {
			sql += " and date_format(sign_date,'%Y-%m-%d') >= '"
					+ beginTime + "'";
		}
		if (!"".equals(endTime)) {
			sql += " and date_format(sign_date,'%Y-%m-%d') <= '" + endTime
					+ "'";
		}
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(whService.yhPageDataJson(dbConn,
					request.getParameterMap(), sql)
					.getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	
	@RequestMapping(value = "getPur", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String,Object>> getPur(HttpServletRequest request) {
		String pDeId = request.getParameter("pDeId");
		List<Map<String,Object>> list = whService.getPur(pDeId);
		return list;
	}

	// 明细
	@RequestMapping(value = "getPurDb", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String,Object>> getPurDb(HttpServletRequest request) {
		String pDeId = request.getParameter("pDeId");
		List<Map<String,Object>> list = whService.getPurDb(pDeId);
		return list;
	}

	// 销售出货单明细
@RequestMapping(value = "getPurs", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String,Object>> getPurs(HttpServletRequest request) {
		String purId = request.getParameter("purId");
		List<Map<String,Object>> list = whService.getPurs(purId);
		return list;
	}

// 采购收货单明细
@RequestMapping(value = "getPod", method = RequestMethod.POST)
public @ResponseBody
Map<String,Object> getPod(HttpServletRequest request) {
	String pdId = request.getParameter("pdId");
	Map<String,Object> map = whService.getPod(pdId);
	return map;
}
	@RequestMapping(value = "getPodPro", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String,Object>> getPodPro(HttpServletRequest request) {
		String pdId = request.getParameter("pdId");
		List<Map<String,Object>> list = whService.getPodPro(pdId);
		return list; 
	}

	// 删除收货明细
	@RequestMapping(value = "deletesPur", method = RequestMethod.POST)
	public @ResponseBody
	String deletesPur(HttpServletRequest request) {
		String rtStr = "0";
		String podId = request.getParameter("podId");
		try {
			whService.deletesPur(podId);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	// 添加收货单信息
	@RequestMapping(value = "addPur", method = RequestMethod.POST)
	public @ResponseBody
	String addPur(HttpServletRequest request) {
		String rtStr = "0";
		
		YHPerson user = (YHPerson) request.getSession().getAttribute(
				"LOGIN_USER");// 获得登陆用户
		

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> ppoList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dbList = new ArrayList<Map<String, Object>>();
		//添加收货明细表erp_purchase_product_in_detail--start
		Map<String, Object> podMap = new HashMap<String, Object>();
		String pDeId = UUID.randomUUID().toString();
		String ppoCode = request.getParameter("ppoCode");
		String ppoStatus = request.getParameter("ppoStatus");
		String purId = request.getParameter("purId");
		String supId = request.getParameter("supId");
		String ppoId = request.getParameter("ppoId");
		String purDate = request.getParameter("purDate");
		String ppoRemark = request.getParameter("ppoRemark");
		podMap.put("pDeId", pDeId);
		podMap.put("ppoStatus", ppoStatus);
		podMap.put("purId", purId);
		podMap.put("supId", supId);
		podMap.put("ppoId", ppoId);
		podMap.put("purDate", purDate);
		podMap.put("ppoRemark", ppoRemark);
		podMap.put("purPerson", user.getUserName());
		podMap.put("ppoCode", ppoCode);
		//添加收货明细表erp_purchase_product_in_detail--end
		//添加收货明细产品关联表erp_pur_pro--start和
		String strPur = request.getParameter("arrChildPro");
		String[] strPurs = strPur.split(";");
		
		for (int i = 0; i < strPurs.length; i++) {
			String purPoId = UUID.randomUUID().toString();
			
		
			//添加收货明细产品关联表erp_pur_pro--start
			Map<String, Object> ppoMap = new HashMap<String, Object>();
			
			String[] strPurs1 = strPurs[i].split(",",-1);
			System.out.println(strPurs1.length);
			ppoMap.put("id", purPoId);
			ppoMap.put("pur_id", pDeId);
			ppoMap.put("ppoId", ppoId);
			ppoMap.put("pro_id", strPurs1[0]);
			ppoMap.put("pur_num", Double.parseDouble(strPurs1[4]));
			ppoList.add(ppoMap);
			//添加收货明细产品关联表erp_pur_pro--end
			Map<String, Object> dbLogMap = new HashMap<String, Object>();
			String dbLogId = UUID.randomUUID().toString();
			Double price = Double.parseDouble(strPurs1[1]);
			double num = Double.parseDouble(strPurs1[4]);
			Double total = num * price;
			dbLogMap.put("id", dbLogId);
			dbLogMap.put("batch", strPurs1[10]);
			dbLogMap.put("wh_id", strPurs1[3]);
			dbLogMap.put("pro_id", strPurs1[0]);
			dbLogMap.put("pod_id", pDeId);
			dbLogMap.put("price", price);
			dbLogMap.put("number", Double.parseDouble(strPurs1[4]));
			dbLogMap.put("total", total);
			dbLogMap.put("invalid_time", strPurs1[11]);
			dbLogMap.put("status", StaticData.NEW_CREATE);
			dbLogMap.put("flag", StaticData.PUR);
			dbLogMap.put("time", sdf.format(new Date()));
			dbLogMap.put("remark", ppoRemark);
			dbList.add(dbLogMap);
		}
		//添加收货明细产品关联表erp_pur_pro--end
		
		
		try {
			whService.addPur(podMap, ppoList,dbList);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	// 更新收货单信息
	@RequestMapping(value = "updatePur", method = RequestMethod.POST)
	public @ResponseBody
	String updatePur(HttpServletRequest request) {
String rtStr = "0";
		
		YHPerson user = (YHPerson) request.getSession().getAttribute(
				"LOGIN_USER");// 获得登陆用户
		String batch = request.getParameter("batch");//批次号

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> ppoList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dbList = new ArrayList<Map<String, Object>>();
		//添加收货明细表erp_purchase_product_in_detail--start
		Map<String, Object> podMap = new HashMap<String, Object>();
		String pDeId = request.getParameter("pDeId");
		String ppoCode = request.getParameter("ppoCode");
		String ppoStatus = request.getParameter("ppoStatus");
		String purId = request.getParameter("purId");
		String supId = request.getParameter("supId");
		String ppoId = request.getParameter("ppoId");
		String purDate = request.getParameter("purDate");
		String ppoRemark = request.getParameter("ppoRemark");
		podMap.put("pDeId", pDeId);
		podMap.put("ppoStatus", ppoStatus);
		podMap.put("purId", purId);
		podMap.put("supId", supId);
		podMap.put("ppoId", ppoId);
		podMap.put("purDate", purDate);
		podMap.put("ppoRemark", ppoRemark);
		podMap.put("purPerson", user.getUserName());
		podMap.put("ppoCode", ppoCode);
		//添加收货明细表erp_purchase_product_in_detail--end
		//添加收货明细产品关联表erp_pur_pro--start和
		String strPur = request.getParameter("arrChildPro");
		String[] strPurs = strPur.split(";");
	
		for (int i = 0; i < strPurs.length; i++) {
			String purPoId = UUID.randomUUID().toString();
			
		
			//添加收货明细产品关联表erp_pur_pro--start
			Map<String, Object> ppoMap = new HashMap<String, Object>();
			String[] strPurs1 = strPurs[i].split(",");
			ppoMap.put("id", purPoId);
			ppoMap.put("pur_id", pDeId);
			ppoMap.put("ppoId", ppoId);
			ppoMap.put("pro_id", strPurs1[0]);
			ppoMap.put("pur_num", Double.parseDouble(strPurs1[4]));
			ppoList.add(ppoMap);
			//添加收货明细产品关联表erp_pur_pro--end
			Map<String, Object> dbLogMap = new HashMap<String, Object>();
			String dbLogId = UUID.randomUUID().toString();
			Double price = Double.parseDouble(strPurs1[1]);
			double num = Double.parseDouble(strPurs1[4]);
			Double total = num * price;
			dbLogMap.put("id", dbLogId);
			dbLogMap.put("batch", strPurs1[10]);
			dbLogMap.put("wh_id", strPurs1[3]);
			dbLogMap.put("pro_id", strPurs1[0]);
			dbLogMap.put("pod_id", pDeId);
			dbLogMap.put("price", price);
			dbLogMap.put("number", Double.parseDouble(strPurs1[4]));
			dbLogMap.put("total", total);
			dbLogMap.put("invalid_time", strPurs1[11]);
			dbLogMap.put("status", StaticData.NEW_CREATE);
			dbLogMap.put("flag", StaticData.PUR);
			dbLogMap.put("time", sdf.format(new Date()));
			dbLogMap.put("remark", ppoRemark);
			dbList.add(dbLogMap);
		}
		//添加收货明细产品关联表erp_pur_pro--end
		try {
			whService.updatePur(podMap, ppoList,dbList, pDeId);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "addWareHouse", method = RequestMethod.POST)
	public @ResponseBody
	String addWareHouse(HttpServletRequest request) {
		String rtStr = "0";
		String person_id = request.getParameter("person_id");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String type = request.getParameter("type");
		String remark = request.getParameter("remark");
		WareHouse wh = new WareHouse(name, address, type, remark);
		try {
			whService.addWareHouse(wh, person_id);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "checkHasValue", method = RequestMethod.POST)
	public @ResponseBody
	String checkHasValue(HttpServletRequest request) {
		String rtStr = "";
		String order_id = request.getParameter("order_id");
		try {
			// 判断某个订单，仓库有没有发货记录
			if (whService.checkHasValue(order_id) > 0) {
				rtStr = "0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "updateWareHouse", method = RequestMethod.POST)
	public @ResponseBody
	String updateWareHouse(HttpServletRequest request) {
		String rtStr = "2";
		String person_id = request.getParameter("person_id");
		String wh_id = request.getParameter("id");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String type = request.getParameter("type");
		String remark = request.getParameter("remark");
		WareHouse wh = new WareHouse(name, address, type, remark);
		wh.setId(wh_id);
		try {
			whService.updateWareHouse(wh, person_id);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "deleteWareHouseById", method = RequestMethod.POST)
	public @ResponseBody
	String deleteWareHouseById(HttpServletRequest request) {
		String rtStr = "0";
		String id = request.getParameter("id");
		try {
			whService.deleteWareHouseById(id);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}
	//查询所有状态
	@RequestMapping(value = "getStatusName", method = RequestMethod.POST)
	public @ResponseBody
	String getStatusName(HttpServletRequest request,
			HttpServletResponse response) {
		response.setHeader("Charset", "UTF-8");
		String status = request.getParameter("status");
		try {
			response.getWriter().print(StaticData.getOrderStatus(status));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "getWareHouseById", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String,Object>> getWareHouseById(HttpServletRequest request) {
		String id = request.getParameter("id");
		List<Map<String,Object>>  list = whService.getWareHouseById(id);
		return list;
	}

	@RequestMapping(value = "getStyle/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String,Object>>  getStyle(HttpServletRequest request) {

		List<Map<String,Object>>  list = whService.getStyle();

		return list;
	}

	@RequestMapping(value = "getWHTypeName", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String,Object>>  getWHTypeName(HttpServletRequest request) {
		List<Map<String,Object>>  rtList = new ArrayList<Map<String,Object>>();
		String type = request.getParameter("type");
		String[] arr = type.split(",");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		if (list.size() > 0) {
			rtList = whService.getWHTypeName(list);
		}
		return rtList;
	}

	@RequestMapping(value = "getWHAdmin", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String,Object>> getWHAdmin(HttpServletRequest request) {
		String id = request.getParameter("id");
		List<Map<String,Object>> list = whService.getWHAdmin(id);
		return list;
	}

	@RequestMapping(value = "getProductList", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> getProductList(HttpServletRequest request) {
		String po_id = request.getParameter("po_id");
		List<Map<String, Object>> list = whService.getProductList(po_id);
		return list;
	}

	@RequestMapping(value = "getProducts", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> getProducts(HttpServletRequest request) {
		String pod_id = request.getParameter("pod_id");
		List<Map<String, Object>> list = whService.getProducts(pod_id);
		return list;
	}

	@RequestMapping(value = "getNumbers", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> getNumbers(HttpServletRequest request) {
		String pod_id = request.getParameter("pod_id");
		String proId = request.getParameter("proId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pod_id", pod_id);
		map.put("proId", proId);
		List<Map<String, Object>> list = whService.getNumbers(map);
		return list;
	}
	@RequestMapping(value = "getPudNumbers", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> getPudNumbers(HttpServletRequest request) {
		String pdId = request.getParameter("pdId");
		String proId = request.getParameter("proId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pdId", pdId);
		map.put("proId", proId);
		List<Map<String, Object>> list = whService.getPudNumbers(map);
		return list;
	}

	@RequestMapping(value = "getCusMsg", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getCusMsg(HttpServletRequest request) {
		String order_id = request.getParameter("order_id");
		Map<String, Object> map = whService.getCusMsg(order_id);
		return map;
	}

	@RequestMapping(value = "getPODMsg", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> getPODMsg(HttpServletRequest request) {
		String pod_id = request.getParameter("pod_id");
		Map<String, Object> map = whService.getPODMsg(pod_id);
		return map;
	}

	@RequestMapping(value = "addPOD", method = RequestMethod.POST)
	public @ResponseBody
	String addPOD(HttpServletRequest request) {
		String rtStr = "0";
		String pod_code = request.getParameter("pod_code");//发货单编号
		String pod_status = oa.spring.util.StaticData.NEW_CREATE;//发货单状态
		String order_id = request.getParameter("order_id");//订单id
		String po_id = request.getParameter("po_id");//出货单id
		String person = request.getParameter("person");//收货人
		String address = request.getParameter("address");//收货地址
		String phone = request.getParameter("phone");//联系方式
		String zip_code = request.getParameter("zip_code");//邮编
		String pod_date = request.getParameter("pod_date");//发货时间
		String pod_sender_id = request.getParameter("userId");//发货人Id
		String pod_sender = request.getParameter("pod_sender");//发货人
		String pod_send_way = request.getParameter("pod_send_way");//发货方式
		double total = Double.parseDouble(request.getParameter("total"));
		String remark = request.getParameter("remark");
		
		//构建erp_order_product_out_detail表数据--start
		String podId = UUID.randomUUID().toString();
		Map<String,Object> podMap = new HashMap<String,Object>();
		podMap.put("id", podId);
		podMap.put("pod_code", pod_code);
		podMap.put("pod_status", pod_status);
		podMap.put("order_id", order_id);
		podMap.put("po_id", po_id);
		podMap.put("person", person);
		podMap.put("address", address);
		podMap.put("phone", phone);
		podMap.put("zip_code", zip_code);
		podMap.put("pod_date", pod_date);
		podMap.put("pod_sender", pod_sender);
		podMap.put("pod_send_way", pod_send_way);
		podMap.put("total", total);
		podMap.put("remark", remark);
		podMap.put("pod_sender_id", pod_sender_id);
		//构建erp_order_product_out_detail表数据--end
		
		//构建erp_pod_pro表数据--start
		List<Map<String, Object>> podProList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dbLogList = new ArrayList<Map<String, Object>>();
		
		String str = request.getParameter("str");
		String[] arrChildStr = str.split(";");
		
		for (int i = 0; i < arrChildStr.length; i++) {
			//dbId+","+whId+","+proId+","+batch+","+number
			String arrChildStrs[] = arrChildStr[i].split(",");
			String podProId = UUID.randomUUID().toString();
			Map<String, Object> podProMap = new HashMap<String, Object>();
			podProMap.put("id", podProId);
			podProMap.put("pod_id", podId);
			podProMap.put("pro_id", arrChildStrs[2]);
			podProMap.put("pod_num", arrChildStrs[4]);
			podProList.add(podProMap);
			//构建erp_db_log表数据--start
			Map<String, Object> dbLogMap = new HashMap<String, Object>();
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("po_id", po_id);
			paramMap.put("pro_id", arrChildStrs[2]);
			String sale_price = whService.getSalePrice(paramMap);
			String dbLogId = UUID.randomUUID().toString();
			dbLogMap.put("id", dbLogId);
			dbLogMap.put("batch", arrChildStrs[3]);
			dbLogMap.put("wh_id", arrChildStrs[1]);
			dbLogMap.put("pro_id", arrChildStrs[2]);
			dbLogMap.put("pod_id", podId);
			dbLogMap.put("price", sale_price);//销售单价
			dbLogMap.put("number", arrChildStrs[4]);
			dbLogMap.put("total", DoubleCaulUtil.mul(sale_price,arrChildStrs[4]));
			dbLogMap.put("flag", StaticData.SALE);
			dbLogMap.put("status", StaticData.NEW_CREATE);
			dbLogMap.put("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			dbLogMap.put("remark", remark);
			dbLogMap.put("invalid_time", null);
			dbLogList.add(dbLogMap);
			//构建erp_db_log表数据--end
		}
		//构建erp_pod_pro表数据--end
		try {
				whService.addPODBatch(podMap,podProList, dbLogList);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "updatePOD", method = RequestMethod.POST)
	public @ResponseBody
	String updatePOD(HttpServletRequest request) {
		String rtStr = "0";
		String pod_code = request.getParameter("pod_code");//发货单编号
		String person = request.getParameter("person");//收货人
		String address = request.getParameter("address");//收货地址
		String phone = request.getParameter("phone");//联系方式
		String zip_code = request.getParameter("zip_code");//邮编
		String pod_date = request.getParameter("pod_date");//发货时间
		String pod_sender_id = request.getParameter("userId");//发货人Id
		String pod_sender = request.getParameter("pod_sender");//发货人
		String pod_send_way = request.getParameter("pod_send_way");//发货方式
		double total = Double.parseDouble(request.getParameter("total"));
		String remark = request.getParameter("remark");
		
		//构建erp_order_product_out_detail表数据--start
		String podId = request.getParameter("pod_id");
		Map<String,Object> podMap = new HashMap<String,Object>();
		podMap.put("id", podId);
		podMap.put("pod_code", pod_code);
		podMap.put("person", person);
		podMap.put("address", address);
		podMap.put("phone", phone);
		podMap.put("zip_code", zip_code);
		podMap.put("pod_date", pod_date);
		podMap.put("pod_sender", pod_sender);
		podMap.put("pod_send_way", pod_send_way);
		podMap.put("total", total);
		podMap.put("remark", remark);
		podMap.put("pod_sender_id", pod_sender_id);
		//构建erp_order_product_out_detail表数据--end
		
		//构建erp_pod_pro表数据--start
		List<Map<String, Object>> podProList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dbLogList = new ArrayList<Map<String, Object>>();
		
		String str = request.getParameter("str");
		String[] arrChildStr = str.split(";");
		for (int i = 0; i < arrChildStr.length; i++) {
			//dbId+","+whId+","+proId+","+batch+","+number
			String arrChildStrs[] = arrChildStr[i].split(",");
			String podProId = UUID.randomUUID().toString();
			Map<String, Object> podProMap = new HashMap<String, Object>();
			podProMap.put("id", podProId);
			podProMap.put("pod_id", podId);
			podProMap.put("pro_id", arrChildStrs[2]);
			podProMap.put("pod_num", arrChildStrs[4]);
			podProList.add(podProMap);
			//构建erp_db_log表数据--start
			Map<String, Object> dbLogMap = new HashMap<String, Object>();
			Map<String,Object> paramMap = new HashMap<String,Object>();
			String po_id = request.getParameter("po_id");
			paramMap.put("po_id", po_id);
			paramMap.put("pro_id", arrChildStrs[2]);
			String sale_price = whService.getSalePrice(paramMap);
			String dbLogId = UUID.randomUUID().toString();
			dbLogMap.put("id", dbLogId);
			dbLogMap.put("batch", arrChildStrs[3]);
			dbLogMap.put("wh_id", arrChildStrs[1]);
			dbLogMap.put("pro_id", arrChildStrs[2]);
			dbLogMap.put("pod_id", podId);
			dbLogMap.put("price", sale_price);//销售单价
			dbLogMap.put("number", arrChildStrs[4]);
			dbLogMap.put("total", DoubleCaulUtil.mul(sale_price,arrChildStrs[4]));
			dbLogMap.put("flag", StaticData.SALE);
			dbLogMap.put("status", StaticData.NEW_CREATE);
			dbLogMap.put("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			dbLogMap.put("invalid_time", null);
			dbLogMap.put("remark", remark);
			dbLogList.add(dbLogMap);
			//构建erp_db_log表数据--end
		}
		//构建erp_pod_pro表数据--end
		try {

			whService.updatePODBatch(podMap, podProList, dbLogList, podId);

		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "deletePOD", method = RequestMethod.POST)
	public @ResponseBody
	String deletePOD(HttpServletRequest request) {
		String rtStr = "0";
		String pod_id = request.getParameter("pod_id");
		try {
			whService.deletePOD(pod_id);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "openAccount", method = RequestMethod.POST)
	public @ResponseBody
	String openAccount(HttpServletRequest request) {
		String rtStr = "0";
		String pod_id = request.getParameter("pod_id");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			Connection dbConn = requestDbConn.getSysDbConn();
			whService.openAccount(pod_id, dbConn);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "openAccountPurchaseDetial", method = RequestMethod.POST)
	public @ResponseBody
	String openAccountPurchaseDetial(HttpServletRequest request) {
		String rtStr = "0";
		String pDeId = request.getParameter("pDeId");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			Connection dbConn = requestDbConn.getSysDbConn();
			whService.openAccountPurchaseDetial(pDeId, dbConn);
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "checkData", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> checkData(HttpServletRequest request,HttpServletResponse response) {
		String typeId = request.getParameter("typeId");
		String flag = request.getParameter("flag");
		Map<String, Object> map = whService.checkData(typeId, flag);
		return map;
	}

	
	@RequestMapping(value = "returnAdd", method = RequestMethod.POST)
	public @ResponseBody
	String returnAdd(HttpServletRequest request) {
		String rtStr = "0";
		
		String flag = request.getParameter("flag");//退货类型
		//构建erp_return表数据--start
		Map<String, Object> returnMap = new HashMap<String,Object>();
		String saleReturnId = UUID.randomUUID().toString();//退货id
		String code = request.getParameter("code");//退货编号
		String type = request.getParameter("type");//退货类型
		String typeId = request.getParameter("typeId");//退货类型 id
		String reason = request.getParameter("reason");//退货原因
		String status = StaticData.NEW_CREATE;//状态
		String return_date = request.getParameter("return_date");//退货时间
		String remark = request.getParameter("remark");
		returnMap.put("id", saleReturnId);
		returnMap.put("code", code);
		returnMap.put("type", type);
		returnMap.put("type_id", typeId);
		returnMap.put("reason", reason);
		returnMap.put("status", status);
		returnMap.put("return_date", return_date);
		returnMap.put("remark", remark);
		//构建erp_return表数据--end
		
		List<Map<String, Object>> rtProList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dbLogList = new ArrayList<Map<String, Object>>();
		String str = request.getParameter("str");
		String[] arrChildStr = str.split(";");
		for (int i = 0; i < arrChildStr.length; i++) {
			String[] arrChildStrs = arrChildStr[i].split(",");
			//构建erp_return_pro表数据--start
			Map<String, Object> podProMap = new HashMap<String, Object>();
			podProMap.put("id", UUID.randomUUID().toString());
			podProMap.put("return_id", saleReturnId);
			podProMap.put("pro_id", arrChildStrs[1]);
			podProMap.put("num", arrChildStrs[2]);
			rtProList.add(podProMap);
			//构建erp_return_pro表数据--end
			//构建erp_db_log表数据--start
			Map<String, Object> dbLogMap = new HashMap<String, Object>();
			String dbLogId = UUID.randomUUID().toString();
			dbLogMap.put("id", dbLogId);
			dbLogMap.put("batch", arrChildStrs[4]);
			dbLogMap.put("wh_id", arrChildStrs[3]);
			dbLogMap.put("pro_id", arrChildStrs[1]);
			dbLogMap.put("pod_id", saleReturnId);
			dbLogMap.put("price", "0");//退货的情况下，价格和总额没有意义，这里就赋值为0
			dbLogMap.put("number", arrChildStrs[2]);
			dbLogMap.put("total", "0");
			if("1".equals(flag)){
				dbLogMap.put("flag", StaticData.SALE_RETURN);
			}else{
				dbLogMap.put("flag", StaticData.PUR_RETURN);
			}
			dbLogMap.put("status", StaticData.NEW_CREATE);
			dbLogMap.put("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			dbLogMap.put("remark", remark);
			dbLogList.add(dbLogMap);
			//构建erp_db_log表数据--end
		}
		try {
			whService.addReturn(returnMap, rtProList, dbLogList);
		} catch (Exception e) {
			e.printStackTrace();

			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "inDb", method = RequestMethod.POST)
	public @ResponseBody
	String inDb(HttpServletRequest request) {
		String str = "0";
		String retId = request.getParameter("retId");
		String type = request.getParameter("type");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("retId", retId);
		map.put("status", StaticData.OVER);
		map.put("type", type);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = whService.getReturnDbNum(map);
		try {
			whService.updateDb(list, map);
		} catch (Exception e) {
			e.printStackTrace();
			str = "1";

		}
		return str;
	}

	
	@RequestMapping(value = "returnUpdate", method = RequestMethod.POST)
	public @ResponseBody
	String returnUpdate(HttpServletRequest request) {
		String rtStr = "0";
		
		String flag = request.getParameter("flag");//退货类型
		//构建erp_return表数据--start
		Map<String, Object> returnMap = new HashMap<String,Object>();
		String saleReturnId = request.getParameter("retId");;//退货id
		String code = request.getParameter("code");//退货编号
		String type = request.getParameter("type");//退货类型
		String typeId = request.getParameter("typeId");//退货类型 id
		String reason = request.getParameter("reason");//退货原因
		String status = StaticData.NEW_CREATE;//状态
		String return_date = request.getParameter("return_date");//退货时间
		String remark = request.getParameter("remark");
		returnMap.put("id", saleReturnId);
		returnMap.put("code", code);
		returnMap.put("type", type);
		returnMap.put("type_id", typeId);
		returnMap.put("reason", reason);
		returnMap.put("status", status);
		returnMap.put("return_date", return_date);
		returnMap.put("remark", remark);
		//构建erp_return表数据--end
		
		List<Map<String, Object>> rtProList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> dbLogList = new ArrayList<Map<String, Object>>();
		String str = request.getParameter("str");
		String[] arrChildStr = str.split(";");
		for (int i = 0; i < arrChildStr.length; i++) {
			String[] arrChildStrs = arrChildStr[i].split(",");
			//构建erp_return_pro表数据--start
			Map<String, Object> podProMap = new HashMap<String, Object>();
			podProMap.put("id", UUID.randomUUID().toString());
			podProMap.put("return_id", saleReturnId);
			podProMap.put("pro_id", arrChildStrs[1]);
			podProMap.put("num", arrChildStrs[2]);
			rtProList.add(podProMap);
			//构建erp_return_pro表数据--end
			//构建erp_db_log表数据--start
			Map<String, Object> dbLogMap = new HashMap<String, Object>();
			String dbLogId = UUID.randomUUID().toString();
			dbLogMap.put("id", dbLogId);
			dbLogMap.put("batch", arrChildStrs[4]);
			dbLogMap.put("wh_id", arrChildStrs[3]);
			dbLogMap.put("pro_id", arrChildStrs[1]);
			dbLogMap.put("pod_id", saleReturnId);
			dbLogMap.put("price", "0");//退货的情况下，价格和总额没有意义，这里就赋值为0
			dbLogMap.put("number", arrChildStrs[2]);
			dbLogMap.put("total", "0");
			if("1".equals(flag)){
				flag = StaticData.SALE_RETURN;
				dbLogMap.put("flag", flag);
			}else{
				flag = StaticData.PUR_RETURN;
				dbLogMap.put("flag", flag);
			}
			dbLogMap.put("status", StaticData.NEW_CREATE);
			dbLogMap.put("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			dbLogMap.put("remark", remark);
			dbLogList.add(dbLogMap);
			//构建erp_db_log表数据--end
		}
		try {
			whService.returnUpdate(returnMap, rtProList, dbLogList,flag);
		} catch (Exception e) {
			e.printStackTrace();

			rtStr = "1";
		}
		return rtStr;
	}

	@RequestMapping(value = "getReturn", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> getReturn(HttpServletRequest request) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String retId = request.getParameter("retId");
		try {

			list = whService.getReturn(retId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	@RequestMapping(value = "getReturnDb", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> getReturnDb(HttpServletRequest request) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String retId = request.getParameter("retId");
		String proId = request.getParameter("proId");
		String flag = request.getParameter("flag");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("retId", retId);
		map.put("proId", proId);
		if("1".equals(flag)){
			map.put("flag", StaticData.SALE_RETURN);
		}else{
			map.put("flag", StaticData.PUR_RETURN);
		}
		try {
			list = whService.getReturnDb(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	@RequestMapping(value = "getLoss", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> getLoss(HttpServletRequest request) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String lossId = request.getParameter("lossId");
		try {

			list = whService.getLoss(lossId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	@RequestMapping(value = "getLossDb", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> getLossDb(HttpServletRequest request) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String lossId = request.getParameter("lossId");
		String proId = request.getParameter("proId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lossId", lossId);
		map.put("proId", proId);
		try {

			list = whService.getLossDb(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public String toString() {
		return "WareHouseController [saleOrderService=" + saleOrderService
				+ ", whService=" + whService + "]";
	}

	@RequestMapping(value = "deleteReturn", method = RequestMethod.POST)
	public @ResponseBody
	String deleteReturn(HttpServletRequest request) {
		String str = "";
		String retId = request.getParameter("retId");
		try {
			whService.deleteReturn(retId);

		} catch (Exception e) {
			e.printStackTrace();
			str = "1";
		}
		return str;
	}

	// 报销单添加
	@RequestMapping(value = "lossAdd", method = RequestMethod.POST)
	public @ResponseBody
	String lossAdd(HttpServletRequest request) {
		String str = "0";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listPro = new ArrayList<Map<String, Object>>();
		String lossId = UUID.randomUUID().toString();
		String code = request.getParameter("code");
		String reason = request.getParameter("reason");
		String beginTime = request.getParameter("beginTime");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String remark = request.getParameter("remark");
		String childStr = request.getParameter("arrChildStr");
		YHPerson user = (YHPerson) request.getSession().getAttribute(
				"LOGIN_USER");// 获得登陆用户
		String[] arrChildStr = childStr.split(";");

		for (int i = 0; i < arrChildStr.length; i++) {
			if (arrChildStr[i] == "" || "".equals(arrChildStr[i])) {

			} else {

				String[] arrChildStrs = arrChildStr[i].split(",");
				String dbLogId = UUID.randomUUID().toString();
				String rpId = UUID.randomUUID().toString();
				Map<String, Object> map = new HashMap<String, Object>();
				String proUuId = UUID.randomUUID().toString();// 缓存产品id
				Map<String, Object> map4 = saleOrderService
						.getProById(arrChildStrs[0]);
				String unitId = map4.get("unit_id").toString();
				String unitName = saleOrderService.getUnitById(unitId);
				String typeIds = map4.get("pt_id").toString();
				String typeName = saleOrderService.getTypeById(typeIds);
				String styleId = map4.get("ps_id").toString();
				String styleName = saleOrderService.getStyleById(styleId);
				map4.put("useful_id", lossId);
				map4.put("proUuId", proUuId);
				map4.put("unit_name", unitName);
				map4.put("pt_name", typeName);
				map4.put("ps_name", styleName);
				map4.put("proId", arrChildStrs[0]);
				map4.put("flag", StaticData.LOSS);
				listPro.add(map4);
				map.put("dbLogId", dbLogId);
				map.put("dbId", arrChildStrs[3]);
				map.put("rpId", rpId);
				map.put("pDeId", lossId);
				map.put("lossId", lossId);
				map.put("proId", arrChildStrs[0]);
				map.put("cpId", proUuId);
				map.put("num", arrChildStrs[5]);
				map.put("podNum", arrChildStrs[5]);
				map.put("whId", arrChildStrs[4]);
				map.put("price", arrChildStrs[2]);
				Double total = Double.parseDouble(arrChildStrs[5])
						* Double.parseDouble(arrChildStrs[2]);
				map.put("total", total);
				map.put("flag", StaticData.LOSS);
				map.put("time", sdf.format(new Date()));
				map.put("dbStatus", StaticData.NEW_CREATE);
				map.put("remark", remark);
				map.put("ppoRemark", remark);
				list.add(map);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("lossId", lossId);
		map.put("reason", reason);
		map.put("user_id", user.getSeqId());
		map.put("lossDate", beginTime);
		map.put("remark", remark);
		map.put("status", StaticData.NEW_CREATE);
		try {
			whService.addLoss(map, list, listPro);
		} catch (Exception e) {
			e.printStackTrace();

			str = "1";
		}
		return str;
	}

	// 库存报损更新
	@RequestMapping(value = "lossUpdate", method = RequestMethod.POST)
	public @ResponseBody
	String lossUpdate(HttpServletRequest request) {
		String str = "0";
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listPro = new ArrayList<Map<String, Object>>();
		String lossId = request.getParameter("lossId");
		String code = request.getParameter("code");
		String beginTime = request.getParameter("beginTime");
		String reason = request.getParameter("reason");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String remark = request.getParameter("remark");
		String childStr = request.getParameter("arrChildStr");
		String[] arrChildStr = childStr.split(";");
		int k = 0;
		for (int i = 0; i < arrChildStr.length; i++) {
			if (arrChildStr[i] == "" || "".equals(arrChildStr[i])) {

			} else {

				String[] arrChildStrs = arrChildStr[i].split(",");
				String dbLogId = UUID.randomUUID().toString();
				String rpId = UUID.randomUUID().toString();
				Map<String, Object> map = new HashMap<String, Object>();
				String proUuId = UUID.randomUUID().toString();// 缓存产品id
				Map<String, Object> map4 = saleOrderService
						.getProById(arrChildStrs[0]);
				String unitId = map4.get("unit_id").toString();
				String unitName = saleOrderService.getUnitById(unitId);
				String typeIds = map4.get("pt_id").toString();
				String typeName = saleOrderService.getTypeById(typeIds);
				String styleId = map4.get("ps_id").toString();
				String styleName = saleOrderService.getStyleById(styleId);
				map4.put("useful_id", lossId);
				map4.put("proUuId", proUuId);
				map4.put("unit_name", unitName);
				map4.put("pt_name", typeName);
				map4.put("ps_name", styleName);
				map4.put("proId", arrChildStrs[0]);
				map4.put("flag", StaticData.LOSS);
				listPro.add(map4);
				map.put("dbLogId", dbLogId);
				map.put("dbId", arrChildStrs[3]);
				map.put("rpId", rpId);
				map.put("pDeId", lossId);
				map.put("lossId", lossId);
				map.put("proId", arrChildStrs[0]);
				map.put("cpId", proUuId);
				map.put("num", arrChildStrs[5]);
				map.put("podNum", arrChildStrs[5]);
				map.put("whId", arrChildStrs[4]);
				map.put("price", arrChildStrs[2]);
				Double total = Double.parseDouble(arrChildStrs[5])
						* Double.parseDouble(arrChildStrs[2]);
				map.put("total", total);
				map.put("time", sdf.format(new Date()));
				map.put("dbStatus", StaticData.NEW_CREATE);
				map.put("remark", remark);
				map.put("ppoRemark", remark);
				map.put("flag", StaticData.LOSS);
				list.add(map);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code", code);
		map.put("lossId", lossId);
		map.put("lossDate", beginTime);
		map.put("reason", reason);
		map.put("remark", remark);
		try {
			whService.lossUpdate(map, list, lossId, listPro);
		} catch (Exception e) {
			e.printStackTrace();

			str = "1";
		}
		return str;
	}

	@RequestMapping(value = "deleteLoss", method = RequestMethod.POST)
	public @ResponseBody
	String deleteLoss(HttpServletRequest request) {
		String str = "";
		String lossId = request.getParameter("lossId");
		try {
			whService.deleteLoss(lossId);

		} catch (Exception e) {
			e.printStackTrace();
			str = "1";
		}
		return str;
	}

}
