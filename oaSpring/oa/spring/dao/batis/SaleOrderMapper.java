package oa.spring.dao.batis;

import java.util.List;
import java.util.Map;

import oa.spring.po.OrderProductOut;
import oa.spring.po.PaidPlan;
import oa.spring.po.SaleOrder;

public interface SaleOrderMapper {
	//查询所有订单信息
	public List<SaleOrder> getOrder();
	//根据id查询出unitName
	public String getUnitById(String unitId);
	//根据id查询出tyleName
	public String getTypeById(String typeId);
	//根据id查询出styleName
	public String getStyleById(String styleId);
	//添加订单信息
	public void addOrder(Map<String,Object> map);
	//添加财务应收单
	public void addIn(Map map);
	//根据id查询产品类别
	public List getSaleOrderById(String orderId);
	//更新产品类别
	public void saleOrderUpdate(SaleOrder sale);
	//根据cadId查询收货地址对象
	public Map<String,Object> getAddressById(String cadId);
	//根据cusId查询客户对象
	public Map<String,Object> getCustById(String cusId);
	//根据contactId查询联系人对象
	public Map<String,Object> getContactById(String contactId);
	//根据proId查询产品对象
	public Map<String,Object> getProById(String proId);
	//添加客户信息
	public void addCust(Map map);
	//添加联系人信息
	public void addContact(Map map);
	//添加收货地址信息
	public void addAddress(Map map);
	//删除销售订单信息
	public void deleteSaleOrder(String orderId);
	public void deleteFinanceIn(String orderId);
	//删除客户信息
	public void deleteCust(String orderId);
	//删除联系人信息
	public void deleteContact(String orderId);
	//删除收货地址信息
	public void deleteAddress(String orderId);
	//删除出货产品信息
	public void deleteOutProduct(String orderId);
	//删除回款单信息
	public void deletePaid(String orderId);
	//删除出货产品中间信息
	public void deletePoPro(String proId);
	//查询销售订单基本属性
	public Map<String,Object> getOrder(String orderId);
	public List<Map<String, Object>> getPoPro(String po_id);
	//更新订单信息
	public void updateOrder(SaleOrder sale);
	//更新产品中间表信息
	public void updatePro(List list);
	//更新客户信息
	public void updateCust(Map map);
	//更新联系人信息
	public void updateContact(Map map);
	//更新收货地址信息
	public void updateAddress(Map map);
	//更新财务应收单
	public void inUpdate(Map map);
	
	//开账，修改状态
	public void updateSaleOrderStatus(Map<String,Object> map);
	public void updateProductOutStatus(Map<String,Object> map);
	public void updatePaidPlanStatus(Map<String,Object> map);
	public void updateFinanceInStatus(Map<String,Object> map);

}
