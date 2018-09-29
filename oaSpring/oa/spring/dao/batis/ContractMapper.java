package oa.spring.dao.batis;

import java.util.Map;

public interface ContractMapper {
	public void addContract(Map map);
	public void updateContract(Map map);
	public void deleteContract(String conId );
	public Map<String,Object>getContract(String conId);
}
