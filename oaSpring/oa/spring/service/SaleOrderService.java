package oa.spring.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.OrderProductOutMapper;
import oa.spring.dao.batis.PaidPlanMapper;
import oa.spring.dao.batis.PoProMapper;
import oa.spring.dao.batis.SaleOrderMapper;
import oa.spring.po.OrderProductOut;
import oa.spring.po.PaidPlan;
import oa.spring.po.PoPro;
import oa.spring.po.SaleOrder;
import oa.spring.util.StaticData;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class SaleOrderService {
	private SaleOrderMapper saleOrderMapper;
	private OrderProductOutMapper orderProductOutMapper;
	private PoProMapper poProMapper;
	private PaidPlanMapper paidPlanMapper;

	public SaleOrderMapper getSaleOrderMapper() {
		return saleOrderMapper;
	}

	public void setSaleOrderMapper(SaleOrderMapper saleOrderMapper) {
		this.saleOrderMapper = saleOrderMapper;
	}

	public OrderProductOutMapper getOrderProductOutMapper() {
		return orderProductOutMapper;
	}

	public void setOrderProductOutMapper(
			OrderProductOutMapper orderProductOutMapper) {
		this.orderProductOutMapper = orderProductOutMapper;
	}

	public PoProMapper getPoProMapper() {
		return poProMapper;
	}

	public void setPoProMapper(PoProMapper poProMapper) {
		this.poProMapper = poProMapper;
	}

	public PaidPlanMapper getPaidPlanMapper() {
		return paidPlanMapper;
	}

	public void setPaidPlanMapper(PaidPlanMapper paidPlanMapper) {
		this.paidPlanMapper = paidPlanMapper;
	}

	// 删除销售订单关联信息
	public String deleteResult(String id, String po_id) {
		saleOrderMapper.deleteAddress(id);
		saleOrderMapper.deleteContact(id);
		saleOrderMapper.deleteCust(id);
		saleOrderMapper.deleteOutProduct(id);
		saleOrderMapper.deletePaid(id);
		saleOrderMapper.deletePoPro(po_id);
		saleOrderMapper.deleteSaleOrder(id);
		saleOrderMapper.deleteFinanceIn(id);
		return "0";
	}

	public void openAccount(String orderId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", orderId);
		map.put("status", StaticData.RUNNING);
		saleOrderMapper.updateSaleOrderStatus(map);
		saleOrderMapper.updateProductOutStatus(map);
		saleOrderMapper.updatePaidPlanStatus(map);
		saleOrderMapper.updateFinanceInStatus(map);
	}

	// 添加销售订单，出货单，客户，收货地址，联系人，汇款单
	public String result(Map<String, Object> saleOrderMap,
						Map<String, Object> orderCusCacheMap,
						Map<String, Object> orderCusContractMap,
						Map<String, Object> orderCusAddress,
						Map<String, Object> orderProductOutMap,
						List<Map<String, Object>> poProductList,
						Map<String, Object> paidPlanMap,
						Map<String, Object> fiMap)
	{
		//添加缓存表
		//erp_order_cus
		saleOrderMapper.addCust(orderCusCacheMap);
		//erp_order_cus_contact
		saleOrderMapper.addContact(orderCusContractMap);
		//erp_order_cus_address
		saleOrderMapper.addAddress(orderCusAddress);
		
		//erp_sale_order
		saleOrderMapper.addOrder(saleOrderMap);
		//erp_order_product_out
		orderProductOutMapper.addOrder(orderProductOutMap);
		//erp_po_pro
		poProMapper.addOrder(poProductList);
		//erp_paid_plan
		paidPlanMapper.addPaidPlan(paidPlanMap);
		//erp_finance_in
		saleOrderMapper.addIn(fiMap);
		return "0";
	}

	// 更新销售订单，出货单，客户，收货地址，联系人，汇款单
	public String updateResult(Map<String, Object> saleOrderMap,
								Map<String, Object> orderCusCacheMap,
								Map<String, Object> orderCusContractMap,
								Map<String, Object> orderCusAddress,
								Map<String, Object> orderProductOutMap,
								List<Map<String, Object>> poProductList,
								Map<String, Object> paidPlanMap,
								Map<String, Object> fiMap)
	{
		String order_id = saleOrderMap.get("id").toString();
		String po_id = orderProductOutMap.get("id").toString();
		deleteResult(order_id, po_id);
		result(saleOrderMap,orderCusCacheMap,orderCusContractMap,orderCusAddress,orderProductOutMap,poProductList,paidPlanMap,fiMap);
		return "0";
	}

	// 更新销售订单，出货单，客户，收货地址，联系人，汇款单
	public String updateResult(PaidPlan paidPlan, OrderProductOut opos,
			PoPro pop, SaleOrder saleOrder, List list, List list1,
			String orderId, String proId) {

		saleOrderMapper.deletePoPro(proId);
		poProMapper.addOrder(list);
		saleOrderMapper.updateOrder(saleOrder);
		orderProductOutMapper.saleOrderUpdate(opos);
		paidPlanMapper.paidPlanUpdate(paidPlan);
		return "0";
	}

	// 分页查询客户信息
	public String queryProduct(Connection dbConn, Map map,String custName,int userId) {
		try {
			String sql="";
			if("-1".equals(custName)){
			sql= "SELECT  cor_code,cor_name ,cor_mne ,cor_phone,cor_cell_phone,cor_address,cor_email FROM crm_cus_cor_cus WHERE cor_isdelete<>'0' AND cor_se_no="+userId;
			}else{ 
			sql = "SELECT  cor_code,cor_name ,cor_mne ,cor_phone,cor_cell_phone,cor_address,cor_email FROM crm_cus_cor_cus where cor_isdelete<>'0' and cor_name like '%"+custName+"%' and cor_se_no="+userId;
			}YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String queryCust(Connection dbConn, Map map,String custName,int userId) {
		try {
			String sql="";
			if("-1".equals(custName)){
			sql= "SELECT  cor_code,cor_name ,cor_mne ,cor_phone,cor_cell_phone,cor_address,cor_email FROM crm_cus_cor_cus WHERE cor_isdelete<>'0'";
			}else{ 
			sql = "SELECT  cor_code,cor_name ,cor_mne ,cor_phone,cor_cell_phone,cor_address,cor_email FROM crm_cus_cor_cus where cor_isdelete<>'0' and cor_name like '%"+custName+"%' ";
			}YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// 根据客户id分页查询联系人信息
	public String queryContact(Connection dbConn, String cusId, Map map) {
		try {
			String sql = "SELECT con_id,con_name,con_sex,con_dep,con_pos,con_add,con_phone,con_work_pho,con_email FROM crm_cus_contact WHERE con_cor_code='"
					+ cusId + "'";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 根据客户id分页查询收货地址
	public String queryAddress(Connection dbConn, String cusId, Map map) {
		try {
			String sql = "SELECT  cad_id,cad_contact,cad_addr,cad_pho,cad_post_code FROM crm_cus_addr  WHERE cad_cor_code='"
					+ cusId + "'";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 分页查询销售订单
	public String queryOrder(Connection dbConn, Map map, String status,
			String beginTime, String endTime) {
		try {
			String sql = "";
			if ("0".equals(status)) {
				sql = "SELECT  sale.id,pout.id,order_code,order_title,salesperson,sign_date,order_status, sale.remark FROM erp_sale_order sale LEFT JOIN erp_order_product_out pout ON pout.order_id=sale.id where sale.order_status ='"
						+ StaticData.NEW_CREATE
						+ "'";
			} else if ("all".equals(status)) {
				sql = "SELECT  sale.id,pout.id,order_code,order_title,salesperson,sign_date,order_status, sale.remark FROM erp_sale_order sale LEFT JOIN erp_order_product_out pout ON pout.order_id=sale.id where 1=1";
			} else {
				sql = "SELECT  sale.id,pout.id,order_code,order_title,salesperson,sign_date,order_status, sale.remark FROM erp_sale_order sale LEFT JOIN erp_order_product_out pout ON pout.order_id=sale.id where  sale.order_status="
						+ status;
			}
			if (!"".equals(beginTime)) {
				sql += " and date_format(sign_date,'%Y-%m-%d') >= '"
						+ beginTime + "'";
			}
			if (!"".equals(endTime)) {
				sql += " and date_format(sign_date,'%Y-%m-%d') <= '" + endTime
						+ "'";
			}
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<SaleOrder> getOrder() {

		return saleOrderMapper.getOrder();
	}

	public List getSaleOrderById(String orderId) {
		return saleOrderMapper.getSaleOrderById(orderId);

	}

	public Map<String,Object> getAddressById(String cadId) {
		return saleOrderMapper.getAddressById(cadId);

	}

	public Map<String,Object> getContactById(String contactId) {
		return saleOrderMapper.getContactById(contactId);

	}

	public Map<String, Object> getCustById(String cusId) {
		return saleOrderMapper.getCustById(cusId);

	}

	public Map<String, Object> getProById(String proId) {
		return saleOrderMapper.getProById(proId);

	}

	public Map<String, Object> getCacheProById(String proId) {
		return null;//saleOrderMapper.getCacheProById(proId);

	}

	public String getUnitById(String unitId) {
		return saleOrderMapper.getUnitById(unitId);
	}

	public String getStyleById(String styleId) {
		return saleOrderMapper.getStyleById(styleId);
	}

	public String getTypeById(String typeId) {
		return saleOrderMapper.getTypeById(typeId);
	}

	public Map<String, Object> getOrder(String orderId) {
		return saleOrderMapper.getOrder(orderId);
	}
	public List<Map<String, Object>> getPoPro(String po_id) {
		return saleOrderMapper.getPoPro(po_id);
	}

	public Map getProductById(String proId) {
		return null;//saleOrderMapper.getProductById(proId);
	}
}
