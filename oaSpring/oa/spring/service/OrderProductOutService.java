package oa.spring.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.OrderProductOutMapper;
import oa.spring.po.OrderProductOut;
import oa.spring.po.SaleOrder;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class OrderProductOutService {
	private OrderProductOutMapper orderProductOutMapper;

	

	

	

	public OrderProductOutMapper getOrderProductOutMapper() {
		return orderProductOutMapper;
	}
	public void setOrderProductOutMapper(OrderProductOutMapper orderProductOutMapper) {
		this.orderProductOutMapper = orderProductOutMapper;
	}
	public String queryProduct(Connection dbConn, Map map) {
		try {
			String sql = "SELECT  cor_code,cor_name ,cor_mne ,cor_phone,cor_cell_phone,cor_address,cor_email FROM crm_cus_cor_cus";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String queryContact(Connection dbConn,String cusId, Map map) {
		try {
			String sql = "SELECT con_id,con_name,con_sex,con_dep,con_pos,con_add,con_phone FROM crm_cus_contact WHERE con_cor_code='"+cusId+"'";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<OrderProductOut> getOrder() {

		return orderProductOutMapper.getOrder();
	}

	public List getSaleOrderByIds(String id) {
		return orderProductOutMapper.getOrderProductOutByIds(id);

	}


	public void saleOrderDelete(String id) {
		orderProductOutMapper.SaleOrderDelete(id);
	}
}
