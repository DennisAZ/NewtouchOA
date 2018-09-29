package oa.spring.service;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.FinanceMapper;
import oa.spring.dao.batis.SystemMapper;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class SystemService {
	private SystemMapper systemMapper;

	public SystemMapper getSystemMapper() {
		return systemMapper;
	}
	public void setSystemMapper(SystemMapper systemMapper) {
		this.systemMapper = systemMapper;
	}
	
	public void addCode(Map<String,Object> map){
		systemMapper.addCode(map);
	}
	public void addLogistics(Map<String ,Object> map){
		systemMapper.addLogistics(map);
	}
	public void editLogistics(Map<String ,Object> map){
		systemMapper.editLogistics(map);
	}
	public void updateCode(Map<String,Object> map){
		systemMapper.updateCode(map);
	}
	public void deleteCodeById(String id){
		systemMapper.deleteCodeById(id);
	}
	public void deleteLogistics(String id){
		systemMapper.deleteLogistics(id);
	}
	public Map<String,Object> getCodeMsgById(String id){
		return systemMapper.getCodeMsgById(id);
	}
	public Map<String,Object> getLogisticsById(String id){
		return systemMapper.getLogisticsById(id);
	}
	public List<Map<String,Object>>  getLogistics(){
		return systemMapper.getLogistics();
	}
	public String getAutoCode(String code_type,String userName){
		List<Map<String,Object>> list = systemMapper.getValueByCodeType(code_type);
		String value = (String) list.get(0).get("value");
		
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date date = new Date();
		 String dateStr = df.format(date);
		 String[] aDate = dateStr.split(" ");
		 
	      String curYear = aDate[0].split("-")[0];
	      String curMon =  aDate[0].split("-")[1];
	      String curDay =  aDate[0].split("-")[2];
	      
	      String curHour = aDate[1].split(":")[0];
	      String curMinite = aDate[1].split(":")[1];
	      String curSecond = aDate[1].split(":")[2];
	      
		value = value.replaceAll("\\{Y\\}", curYear);
		value = value.replaceAll("\\{M\\}", curMon);
		value = value.replaceAll("\\{D\\}", curDay);
	     value =value.replaceAll("\\{H\\}", curHour);
	     value =value.replaceAll("\\{I\\}", curMinite);
	     value =value.replaceAll("\\{S\\}", curSecond);
	     value =value.replaceAll("\\{U\\}", userName);
	     return value;
	}
}
