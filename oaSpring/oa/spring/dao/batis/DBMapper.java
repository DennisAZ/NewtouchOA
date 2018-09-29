package oa.spring.dao.batis;

import java.util.List;
import java.util.Map;

public interface DBMapper {
	public void insertBatch(List<Map<String, Object>> list);
	public void addDbLog(List<Map<String, Object>> list);
	public void addDbLogTest(Map map);
	public void updateDB(Map<String, String> map);
	public void updateDBLog(Map<String, String> map);
	public int checkBatckOnly(Map<String, Object> map);
	public void deleteDBById(String id);
	public void deleteDBLog(Map<String, String> map);
	public Map<String,Object> getDBMsgById(String id);
	public Map<String,Object> getDBById(String id);
	public Map<String,Object>getWhType(String whId);
	//批量更新库存
	public void updateDbBatch(Map map);
}
