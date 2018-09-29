package oa.spring.dao.batis;

import java.util.List;

import oa.spring.po.PoPro;

public interface PoProMapper {
	//查询所有货单信息
	public List<PoPro> getOrder();
	//添加订单信息
	public void addOrder(List list);
	//根据id查寻货单
	public List getOrderProductOutByIds(String id);
	//更新产品销售中间信息
	public void poProUpdate(List list);
	//删除货单
	public void poProDelete(String id);
}
