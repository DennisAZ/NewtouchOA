package oa.spring.dao.batis;

import java.util.List;
import java.util.Map;

import oa.spring.po.PaidPlan;

public interface PaidPlanMapper {
	//查询所有收款单
	public List<PaidPlan> getPaidPlan();
	//添加收款单信息
	public void addPaidPlan(Map<String, Object> map);
	//根据id查寻收款单
	public List getPaidPlanByIds(String id);
	//更新收款单
	public void paidPlanUpdate(PaidPlan pnaidPlan);
	
	//删除收款单
	public void paidPlanDelete(String id);
}
