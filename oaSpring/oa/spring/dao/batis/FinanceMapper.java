package oa.spring.dao.batis;

import java.util.List;
import java.util.Map;


public interface FinanceMapper {
	public Map<String,Object> getCusMsg(String order_id);
	public Map<String,Object> getOut(String fId);
	public Map<String,Object> getPPDMsg(String fi_id);
	public Map<String,Object> getFIMsg(String fi_id);
	public Map<String,Object> getOutDetail(String fdId);
	public List<Map<String,Object>> getBankMsg();
	public Map<String,Object> getFIDMsg(String fid_id);
	public void addPPD(Map<String,Object> map);
	public void addOut(Map<String,Object> map);
	public void addOutDetail(Map<String,Object> map);
	public void updatePPD(Map map);
	public void updateFID(Map map);
	public void updateOut(Map map);
	public void updateOutDetail(Map map);
	public void deletePPD(String ppd_id);
	public void deleteFID(String fid_id);
	public void deleteOut(String fId);
	public void deleteOutDetail(String fId);
	public void deleteOutDetails(String fdId);
	public void deleteBank(String bankId);
	public void addFinanceIn(Map map);
	public void addBank(Map map);//添加公司银行
	public void updateBank(Map map);//更新公司银行
	public void addFID(Map map);
	public List<Map<String, Object>> getBankById(String bankId);
	public void updateFinanceIn(Map map);
	public Map<String,Object> getFinanceInMsg(String fi_id);
	public void openAccount(Map map);
	public void openAccountFinanceOut(Map map);
}
