package oa.spring.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.ProductMapper;
import oa.spring.dao.batis.PurchaseMapper;
import oa.spring.po.Product;
import oa.spring.util.StaticData;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class PurchaseService {
	private PurchaseMapper purchaseMapper;

	public PurchaseMapper getPurchaseMapper() {
		return purchaseMapper;
	}

	public void setPurchaseMapper(PurchaseMapper purchaseMapper) {
		this.purchaseMapper = purchaseMapper;
	}

	// 分页显示信息
	public String yhRequestDbConn(Connection dbConn, String sql, Map map) {
		try {
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}




	// 添加采购单信息
	public String addResult(Map map, List listPro, List listPpop) {
		purchaseMapper.addPurchase(map);
		purchaseMapper.addPSupplier(map);
		purchaseMapper.addPpo(map);
		purchaseMapper.addPpp(map);
		purchaseMapper.addPpoPro(listPpop);
		purchaseMapper.addOut(map);
		return "0";
	}

	// 删除采购单相关信息
	public String deleteResult(String purchaseId, String purOutId) {
		purchaseMapper.deletePpoPro(purOutId);
		purchaseMapper.deleteProduct(purchaseId);
		purchaseMapper.deletePSupplier(purchaseId);
		purchaseMapper.deletePpp(purchaseId);
		purchaseMapper.deletePpo(purchaseId);
		purchaseMapper.deletePurchase(purchaseId);
		purchaseMapper.deleteOut(purchaseId);

		return "0";
	}

	// 删除采购订单单相关信息
	public String deletePResult(String reqId) {
		purchaseMapper.deleteSuppReq(reqId);
		purchaseMapper.deletePRequest(reqId);
		return "0";
	}

	// 查看采购单详细
	public List<Map<String, Object>> getPurchaseById(String purchaseId) {
		return purchaseMapper.getPurchaseById(purchaseId);
	}

	// 更新采购订单信息
	public String updateResult(Map map, String purchaseId, String purOutId,
			List listPro, List listPpop) {
		purchaseMapper.deletePpoPro(purOutId);
		purchaseMapper.addPpoPro(listPpop);
		purchaseMapper.updatePurchase(map);
		purchaseMapper.updatePSupplier(map);
		purchaseMapper.updatePpp(map);
		purchaseMapper.updatePpo(map);
		purchaseMapper.updateOut(map);
		return "0";
	}

	// 添加申请单
	public String addRequest(Map<String,Object> map, List<Map<String,Object>> list) {
		purchaseMapper.addPRequest(map);
		purchaseMapper.addSuppReq(list);
		return "0";
	}

	// 更新申请单
	public String updateRequest(Map<String,Object> map, List<Map<String,Object>> list) {
		String reqId = map.get("id").toString();
		purchaseMapper.deleteSuppReq(reqId);
		purchaseMapper.updatePRequest(map);
		purchaseMapper.addSuppReq(list);
		return "0";
	}

	// 申请单明细
	public List<Map<String, Object>> getRequestById(String reqId) {
		return purchaseMapper.getRequestById(reqId);
	}
	public List<Map<String, Object>> getRequestSupById(Map map) {
		return purchaseMapper.getRequestSupById(map);
	}

	// 申请单明细
	public List<Map<String, Object>> getRequestByProId(Map Map) {
		return purchaseMapper.getRequestByProId(Map);
	}

	// 申请单明细根据供货商查询商品
	public List<Map<String, Object>> getSupplierByProId(Map Map) {
		return purchaseMapper.getSupplierByProId(Map);
	}

	public void openAccount(String reqId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reqId", reqId);
		map.put("status", StaticData.OVER);
		purchaseMapper.openAccount(map);
	}

	public void openAccountPurchase(String purId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("purId", purId);
		map.put("status", StaticData.RUNNING);
		purchaseMapper.updatePurchaseStatus(map);
		purchaseMapper.updatePPOStatus(map);
		purchaseMapper.updatePPPStatus(map);
		purchaseMapper.updateFinanceOutStatus(map);
	}
	
	
}
