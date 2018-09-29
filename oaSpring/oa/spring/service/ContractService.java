package oa.spring.service;

import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.ContractMapper;

public class ContractService {
	private ContractMapper contractMapper;
	
	public ContractMapper getContractMapper() {
		return contractMapper;
	}

	public void setContractMapper(ContractMapper contractMapper) {
		this.contractMapper = contractMapper;
	}

	public void addContract(Map map){
		contractMapper.addContract(map);
	}
	public void deleteContract(String conId ){
		contractMapper.deleteContract(conId);
	}
	public void updateContract(Map map){
		contractMapper.updateContract(map);
	}
	public Map<String,Object>getContract(String conId){
		return contractMapper.getContract(conId);
	}
}
