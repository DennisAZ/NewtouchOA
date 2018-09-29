package oa.spring.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.PoProMapper;
import oa.spring.po.PoPro;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class PoProService {
	private PoProMapper poProMapper;
	
	public PoProMapper getPoProMapper() {
		return poProMapper;
	}
	public void setPoProMapper(PoProMapper poProMapper) {
		this.poProMapper = poProMapper;
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
	public List<PoPro> getOrder() {

		return poProMapper.getOrder();
	}

	public void addsaleOrder(List list) {
		poProMapper.addOrder(list);
	}

	public List getSaleOrderByIds(String id) {
		return poProMapper.getOrderProductOutByIds(id);

	}


	public void saleOrderDelete(String id) {
		poProMapper.poProDelete(id);
	}
}
