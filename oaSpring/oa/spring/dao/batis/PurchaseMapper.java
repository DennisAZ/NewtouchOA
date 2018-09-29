package oa.spring.dao.batis;

import java.util.List;
import java.util.Map;

public interface PurchaseMapper {
	//添加采购申请单
	public void addPRequest(Map<String,Object> map);
	public void updatePRequest(Map<String,Object> map);
	//添加供货商记录
	public void addSuppReq(List<Map<String,Object>> list);
	//查看采购单信息
	public List<Map<String, Object>>getPurchaseById(String purchaseId);
	//添加采购订单
	public void addPurchase(Map map);
	//添加采购货单
	public void addPpo(Map map);
	//添加财务应付单
	public void addOut(Map map);
	//添加出款单
	public void addPpp(Map map);
	//添加采购产品信息
	public void addProduct(List list);

	//添加采购产品关联
	public void addPpoPro(List list);
	//添加供货商缓存信息
	public void addPSupplier(Map map);
	//删除缓存产品关联信息
	public void deletePpoPro(String ppopId);
	//删除缓存产品信息
	public void deleteProduct(String purchaseId);
	//删除缓存供货商信息
	public void deletePSupplier(String purchaseId);
	//删除出款单
	public void deletePpp(String purchaseId);
	//删除采购货单信息
	public void deletePpo(String purchaseId);
	//删除采购订单信息
	public void deletePurchase(String purchaseId);
	//删除采购财务单
	public void deleteOut(String purchaseId);
	//更新缓存供货商信息
	public void updatePSupplier(Map map);
	//更新出款单
	public void updatePpp(Map map);
	//更新采购货单信息
	public void updatePpo(Map map);
	//更新采购财务单信息
	public void updateOut(Map map);
	//更新采购订单信息
	public void updatePurchase(Map map);
	//删除采购申请单供货商记录信息
	public void deleteSuppReq(String reqId);
	//删除采购申请单信息
	public void deletePRequest(String reqId);
	//查看申请单明细
	public List<Map<String,Object>> getRequestById(String reqId);
	public List<Map<String,Object>> getRequestSupById(Map map);
	public List<Map<String,Object>> getRequestByProId(Map map);
	public List<Map<String,Object>> getSupplierByProId(Map map);
	
	public void openAccount(Map map);
	
	public void updatePurchaseStatus(Map map);
	public void updatePPOStatus(Map map);
	public void updatePPPStatus(Map map);
	public void updateFinanceOutStatus(Map map);
	
}
