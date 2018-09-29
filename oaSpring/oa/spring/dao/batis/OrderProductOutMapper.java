package oa.spring.dao.batis;

import java.util.List;
import java.util.Map;

import oa.spring.po.OrderProductOut;

public interface OrderProductOutMapper {
	//查询所有货单信息
	public List<OrderProductOut> getOrder();
	//添加订单信息
	public void addOrder(Map<String,Object> map);
	//根据id查寻货单
	public List getOrderProductOutByIds(String id);
	//更新出货信息
	public void saleOrderUpdate(OrderProductOut opo);
	//删除货单
	public void SaleOrderDelete(String id);
}
