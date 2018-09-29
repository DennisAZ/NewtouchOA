package oa.spring.control.rest;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.po.OrderProductOut;
import oa.spring.po.PaidPlan;
import oa.spring.po.PoPro;
import oa.spring.po.Product;
import oa.spring.po.SaleOrder;
import oa.spring.service.SaleOrderService;
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

import com.psit.struts.entity.CusCorCus;

@Controller
@RequestMapping("/saleOrder")
public class SaleOrderController {

	@Autowired
	private SaleOrderService saleOrderService;

	private static final Logger logger = Logger.getLogger(SaleOrderController.class);

	/**
	 * 
	 * @param tableName
	 *            表名称
	 * 
	 * @return 要展示的对象的集合
	 */

	@RequestMapping(value = "custList", method = RequestMethod.POST)
	public @ResponseBody
	String custList(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String custName=request.getParameter("custName");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
			"LOGIN_USER");// 获得登陆用户
			str = new String(saleOrderService.queryProduct(dbConn,
					request.getParameterMap(),custName,user.getSeqId()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	@RequestMapping(value = "queryCust", method = RequestMethod.POST)
	public @ResponseBody
	String queryCust(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String custName=request.getParameter("custName");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
			.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson user = (YHPerson) request.getSession().getAttribute(
					"LOGIN_USER");// 获得登陆用户
			str = new String(saleOrderService.queryCust(dbConn,
					request.getParameterMap(),custName,user.getSeqId()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 查询销售订单信息
	@RequestMapping(value = "orderList", method = RequestMethod.POST)
	public @ResponseBody
	String orderList(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		String status=request.getParameter("status");
		String beginTime=request.getParameter("beginTime");
		String endTime=request.getParameter("endTime");
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(saleOrderService.queryOrder(dbConn,
					request.getParameterMap(),status,beginTime,endTime).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 根据crm客户id获取信息
	@RequestMapping(value = "custContact", method = RequestMethod.POST)
	public @ResponseBody
	String custContact(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String cusId = request.getParameter("cusId");
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(saleOrderService.queryContact(dbConn, cusId,
					request.getParameterMap()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 根据crm收货地址获取信息
	@RequestMapping(value = "custAddress", method = RequestMethod.POST)
	public @ResponseBody
	String custAddress(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String cusId = request.getParameter("cusId");
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			str = new String(saleOrderService.queryAddress(dbConn, cusId,
					request.getParameterMap()).getBytes("UTF-8"), "ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 保存订单信息
	@RequestMapping(value = "saleOrderSave", method = RequestMethod.POST)
	public @ResponseBody
	String saleOrderSave(HttpServletRequest request) {
		String stStr = "0";
		
		//构建erp_sale_order表数据--start
		String orderId = UUID.randomUUID().toString();// 订单id
		String orderCode = request.getParameter("orderCode");//订单编号
		String orderTitle = request.getParameter("orderTitle");//订单主题
		String orderStatus = StaticData.NEW_CREATE;//订单状态
		String cusUuId = UUID.randomUUID().toString();// 缓存客户id
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		String Salesperson = user.getUserName();//销售员
		String SignDate = request.getParameter("signDate");//签约时间
		String conId = request.getParameter("contractId");//合同id
		String remark = request.getParameter("remark1");//备注
		Map<String, Object> saleOrderMap = new HashMap<String,Object>();
		saleOrderMap.put("id", orderId);
		saleOrderMap.put("orderCode", orderCode);
		saleOrderMap.put("orderTitle", orderTitle);
		saleOrderMap.put("orderStatus", orderStatus);
		saleOrderMap.put("cusUuId", cusUuId);
		saleOrderMap.put("Salesperson", Salesperson);
		saleOrderMap.put("SignDate", SignDate);
		saleOrderMap.put("contractId", conId);
		saleOrderMap.put("remark", remark);
		saleOrderMap.put("personId", String.valueOf(user.getSeqId()));
		//构建erp_sale_order表数据--end
		
		//构建erp_order_cus表数据--start
		String cusId = request.getParameter("cusId");
		Map<String, Object> orderCusCacheMap = saleOrderService.getCustById(cusId);
		orderCusCacheMap.put("cusUuid", cusUuId);// 缓存客户id
		orderCusCacheMap.put("order_id", orderId);//订单id
		String contactId = UUID.randomUUID().toString();// 联系人id
		orderCusCacheMap.put("contact_id", contactId);
		String addressId = UUID.randomUUID().toString();// 联系地址id
		orderCusCacheMap.put("cad_id", addressId);
		//构建erp_order_cus表数据--end
		
		//构建erp_order_cus_contact表数据--start
		String contact_id = request.getParameter("contactId");
		Map<String, Object> orderCusContractMap = saleOrderService.getContactById(contact_id);
		orderCusContractMap.put("order_id", orderId);
		orderCusContractMap.put("contactId", contactId);
		//构建erp_order_cus_contact表数据--end
		
		//构建erp_order_cus_address表数据--start
		String addId = request.getParameter("addId");
		Map<String,Object> orderCusAddress = saleOrderService.getAddressById(addId);
		orderCusAddress.put("order_id", orderId);
		orderCusAddress.put("addressId", addressId);
		//构建erp_order_cus_address表数据--end
		
		//构建出货单 erp_order_product_out表数据--start
		Map<String, Object> orderProductOutMap = new HashMap<String,Object>();
		String po_id = UUID.randomUUID().toString();// 出货单id
		orderProductOutMap.put("id", po_id);
		String opoCode = request.getParameter("opoCode");//出货单编号
		orderProductOutMap.put("po_code", opoCode);
		String opoTitle = request.getParameter("opoTitle");//出货单主题
		orderProductOutMap.put("po_title", opoTitle);
		orderProductOutMap.put("order_id", orderId);
		orderProductOutMap.put("po_status", StaticData.NEW_CREATE);
		String po_remark = request.getParameter("remark2");
		orderProductOutMap.put("remark", po_remark);
		//构建出货单 erp_order_product_out表数据--end
		
		//构建erp_po_pro表数据--start
		String productIds = request.getParameter("productIds");//产品id集合
		String prices = request.getParameter("prices");//单价集合
		String numbers = request.getParameter("numbers");//数量集合
		String totals = request.getParameter("totals");//总价集合
		String sendDate = request.getParameter("sendDate");//交货日期
		String[] arrPro = productIds.split(",");
		String[] arrPrice = prices.split(",");
		String[] arrNum = numbers.split(",");
		String[] arrTotal = totals.split(",");
		String[] arrSendDate = sendDate.split(",");
		
		List<Map<String,Object>> poProductList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < arrPro.length; i++) {
			Map<String,Object> map = new HashMap<String,Object>();
			String poproId = UUID.randomUUID().toString();//erp_po_pro表的id
			map.put("id", poproId);
			map.put("po_id", po_id);
			map.put("pro_id", arrPro[i]);
			map.put("order_num", arrNum[i]);
			map.put("already_send_num", 0);
			map.put("status", StaticData.NEW_CREATE);
			map.put("sale_price", arrPrice[i]);
			map.put("total", arrTotal[i]);
			map.put("send_date", arrSendDate[i]);
			map.put("remark", "");
			poProductList.add(map);
		}
		//构建erp_po_pro表数据--end
		
		//构建erp_paid_plan表数据--start
		Map<String,Object> paidPlanMap=new HashMap<String,Object>();
		String paidCode = request.getParameter("paidCode");//回款单编号
		String paidName = request.getParameter("paidName");//回款单主题
		String total = request.getParameter("total2");//应收金额
		paidPlanMap.put("paid_code", paidCode);
		paidPlanMap.put("paid_title", paidName);
		paidPlanMap.put("order_id", orderId);
		paidPlanMap.put("paid_status", StaticData.NEW_CREATE);
		paidPlanMap.put("total", total);
		String sale_paid = request.getParameter("sale_paid");
		String already_total = request.getParameter("already_total");
		paidPlanMap.put("already_total", already_total);
		paidPlanMap.put("sale_paid", sale_paid);
		String ppRemark = request.getParameter("remark3");
		paidPlanMap.put("remark", ppRemark);
		
			//构建erp_finance_in表数据--start
			Map<String,Object> fiMap=new HashMap<String,Object>();
			fiMap.put("code", paidCode);
			fiMap.put("status", StaticData.HAND_CREATE);
			fiMap.put("type", "销售");
			fiMap.put("type_id", orderId);
			String custName = request.getParameter("custName");
			fiMap.put("customer",custName);
			fiMap.put("total", total);
			fiMap.put("already_total", 0);
			fiMap.put("person",Salesperson);
			fiMap.put("invoice_id","");
			fiMap.put("remark",ppRemark);
			//构建erp_finance_in表数据--end
		
		//构建erp_paid_plan表数据--end
		try {
			stStr = saleOrderService.result(saleOrderMap,orderCusCacheMap,orderCusContractMap,orderCusAddress,orderProductOutMap,poProductList,paidPlanMap,fiMap);
		} catch (Exception e) {
			e.printStackTrace();
			stStr = "1";
		}
		return stStr;
	}

	// 删除订单信息
	@RequestMapping(value = "deleteOrder", method = RequestMethod.POST)
	public @ResponseBody
	String deleteOrder(HttpServletRequest request) {
		String orderId = request.getParameter("orderId");
		String po_id = request.getParameter("po_id");
		String str = "0";
		try {
			str = saleOrderService.deleteResult(orderId, po_id);
		} catch (Exception e) {
			e.printStackTrace();
			str = "1";
		}
		return str;
	}
	
	@RequestMapping(value = "openAccount", method = RequestMethod.POST)
	public @ResponseBody String openAccount(HttpServletRequest request) {
		String orderId = request.getParameter("orderId");
		String str = "0";
		try {
			saleOrderService.openAccount(orderId);
		} catch (Exception e) {
			e.printStackTrace();
			str = "1";
		}
		return str;
	}

	// 查询销售订单信息
	@RequestMapping(value = "getOrder", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getOrder(HttpServletRequest request) {
		String orderId = request.getParameter("orderId");
		return saleOrderService.getOrder(orderId);
	}
	@RequestMapping(value = "getPoPro", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getPoPro(HttpServletRequest request) {
		String po_id = request.getParameter("po_id");
		return saleOrderService.getPoPro(po_id);
	}

	// 更新订单信息
	@RequestMapping(value = "saleOrderUpdate", method = RequestMethod.POST)
	public @ResponseBody
	String saleOrderUpdate(HttpServletRequest request) {
		String stStr = "0";
		//构建erp_sale_order表数据--start
		String orderId = request.getParameter("order_id");;// 订单id
		String orderCode = request.getParameter("orderCode");//订单编号
		String orderTitle = request.getParameter("orderTitle");//订单主题
		String orderStatus = StaticData.NEW_CREATE;//订单状态
		String cusUuId = UUID.randomUUID().toString();// 缓存客户id
		YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
		String Salesperson = user.getUserName();//销售员
		String SignDate = request.getParameter("signDate");//签约时间
		String conId = request.getParameter("contractId");//合同id
		String remark = request.getParameter("remark1");//备注
		Map<String, Object> saleOrderMap = new HashMap<String,Object>();
		saleOrderMap.put("id", orderId);
		saleOrderMap.put("orderCode", orderCode);
		saleOrderMap.put("orderTitle", orderTitle);
		saleOrderMap.put("orderStatus", orderStatus);
		saleOrderMap.put("cusUuId", cusUuId);
		saleOrderMap.put("Salesperson", Salesperson);
		saleOrderMap.put("SignDate", SignDate);
		saleOrderMap.put("contractId", conId);
		saleOrderMap.put("remark", remark);
		saleOrderMap.put("personId", String.valueOf(user.getSeqId()));
		//构建erp_sale_order表数据--end
		
		//构建erp_order_cus表数据--start
		String cusId = request.getParameter("cusId");
		Map<String, Object> orderCusCacheMap = saleOrderService.getCustById(cusId);
		orderCusCacheMap.put("cusUuid", cusUuId);// 缓存客户id
		orderCusCacheMap.put("order_id", orderId);//订单id
		String contactId = UUID.randomUUID().toString();// 联系人id
		orderCusCacheMap.put("contact_id", contactId);
		String addressId = UUID.randomUUID().toString();// 联系地址id
		orderCusCacheMap.put("cad_id", addressId);
		//构建erp_order_cus表数据--end
		
		//构建erp_order_cus_contact表数据--start
		String contact_id = request.getParameter("contactId");
		Map<String, Object> orderCusContractMap = saleOrderService.getContactById(contact_id);
		orderCusContractMap.put("order_id", orderId);
		orderCusContractMap.put("contactId", contactId);
		//构建erp_order_cus_contact表数据--end
		
		//构建erp_order_cus_address表数据--start
		String addId = request.getParameter("addId");
		Map<String,Object> orderCusAddress = saleOrderService.getAddressById(addId);
		orderCusAddress.put("order_id", orderId);
		orderCusAddress.put("addressId", addressId);
		//构建erp_order_cus_address表数据--end
		
		//构建出货单 erp_order_product_out表数据--start
		Map<String, Object> orderProductOutMap = new HashMap<String,Object>();
		String po_id = request.getParameter("po_id");// 出货单id
		orderProductOutMap.put("id", po_id);
		String opoCode = request.getParameter("opoCode");//出货单编号
		orderProductOutMap.put("po_code", opoCode);
		String opoTitle = request.getParameter("opoTitle");//出货单主题
		orderProductOutMap.put("po_title", opoTitle);
		orderProductOutMap.put("order_id", orderId);
		orderProductOutMap.put("po_status", StaticData.NEW_CREATE);
		String po_remark = request.getParameter("remark2");
		orderProductOutMap.put("remark", po_remark);
		//构建出货单 erp_order_product_out表数据--end
		
		//构建erp_po_pro表数据--start
		String productIds = request.getParameter("productIds");//产品id集合
		String prices = request.getParameter("prices");//单价集合
		String numbers = request.getParameter("numbers");//数量集合
		String totals = request.getParameter("totals");//总价集合
		String sendDate = request.getParameter("sendDate");//交货日期
		String[] arrPro = productIds.split(",");
		String[] arrPrice = prices.split(",");
		String[] arrNum = numbers.split(",");
		String[] arrTotal = totals.split(",");
		String[] arrSendDate = sendDate.split(",");
		
		List<Map<String,Object>> poProductList = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < arrPro.length; i++) {
			Map<String,Object> map = new HashMap<String,Object>();
			String poproId = UUID.randomUUID().toString();//erp_po_pro表的id
			map.put("id", poproId);
			map.put("po_id", po_id);
			map.put("pro_id", arrPro[i]);
			map.put("order_num", arrNum[i]);
			map.put("already_send_num", 0);
			map.put("status", StaticData.NEW_CREATE);
			map.put("sale_price", arrPrice[i]);
			map.put("total", arrTotal[i]);
			map.put("send_date", arrSendDate[i]);
			map.put("remark", "");
			poProductList.add(map);
		}
		//构建erp_po_pro表数据--end
		
		//构建erp_paid_plan表数据--start
		Map<String,Object> paidPlanMap=new HashMap<String,Object>();
		String paidCode = request.getParameter("paidCode");//回款单编号
		String paidName = request.getParameter("paidName");//回款单主题
		String total = request.getParameter("total2");//应收金额
		paidPlanMap.put("paid_code", paidCode);
		paidPlanMap.put("paid_title", paidName);
		paidPlanMap.put("order_id", orderId);
		paidPlanMap.put("paid_status", StaticData.NEW_CREATE);
		paidPlanMap.put("total", total);
		String sale_paid = request.getParameter("sale_paid");
		String already_total = request.getParameter("already_total");
		paidPlanMap.put("already_total", already_total);
		paidPlanMap.put("sale_paid", sale_paid);
		String ppRemark = request.getParameter("remark3");
		paidPlanMap.put("remark", ppRemark);
		
			//构建erp_finance_in表数据--start
			Map<String,Object> fiMap=new HashMap<String,Object>();
			fiMap.put("code", paidCode);
			fiMap.put("status", StaticData.HAND_CREATE);
			fiMap.put("type", "销售");
			fiMap.put("type_id", orderId);
			String custName = request.getParameter("custName");
			fiMap.put("customer",custName);
			fiMap.put("total", total);
			fiMap.put("already_total", 0);
			fiMap.put("person",Salesperson);
			fiMap.put("invoice_id","");
			fiMap.put("remark",ppRemark);
			//构建erp_finance_in表数据--end
		
		//构建erp_paid_plan表数据--end
			//构建erp_paid_plan表数据--end
			try {
				stStr = saleOrderService.updateResult(saleOrderMap,orderCusCacheMap,orderCusContractMap,orderCusAddress,orderProductOutMap,poProductList,paidPlanMap,fiMap);
			} catch (Exception e) {
				e.printStackTrace();
				stStr = "1";
			}
			return stStr;
	}
}
