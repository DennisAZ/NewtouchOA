package oa.spring.dao.batis;

import java.util.List;
import java.util.Map;


public interface SystemMapper {
	public void addCode(Map<String,Object> map);
	public void addLogistics(Map<String,Object> map);
	public void editLogistics(Map<String,Object> map);
	public void updateCode(Map<String,Object> map);
	public void deleteCodeById(String id);
	public void deleteLogistics(String id);
	public Map<String,Object> getCodeMsgById(String id);
	public Map<String,Object> getLogisticsById(String id);
	public List<Map<String,Object>>  getLogistics();
	public List<Map<String,Object>> getValueByCodeType(String code_type);
}
