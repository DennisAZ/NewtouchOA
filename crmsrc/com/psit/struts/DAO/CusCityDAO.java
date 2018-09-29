package com.psit.struts.DAO;

import java.util.List;

import com.psit.struts.entity.CusCity;
/**
 * 
 * 城市DAO <br>
 *
 * create_date: Aug 13, 2010,9:49:23 AM<br>
 * @author zjr
 */
public interface CusCityDAO 
{
	
	 /**
	 * 获取我自己管辖省份范围内的城市
	 * @param id
	 * @return
	 */
	public List<java.lang.Long> getmyManageCity(long id);
	
	/**
	 * 获取我自己管辖范围内的城市名称
	 * @param city_id 城市Id
	 * @return
	 */
	public List<CusCity> getmyManageCityName(long city_id);
	 /**
	  * 
	  * 获得省份下已启用的城市 <br>
	  * create_date: Aug 13, 2010,9:54:24 AM <br>
	  * @param id 省份id
	  * @return List 返回城市列表
	  */ 
	 public List getCusCity(long id);
	 /**
	  * 
	  * 保存城市 <br>
	  * create_date: Aug 13, 2010,9:55:25 AM <br>
	  * @param cusCity 城市实体
	  */
	 public void saveCity(CusCity cusCity);
	 /**
	  * 
	  * 获得所有城市 <br>
	  * create_date: Aug 13, 2010,9:59:39 AM <br>
	  * @return List 返回城市列表 
	  */
	 public List getAllCity();
	 
	 /**
	  * 获得城市<br>
	  * create_date: Dec 18, 2010,10:40:26 AM <br>
	  * @param id
	  * @return CusCity 城市<br>
	  */
	 public CusCity showCity(long id);
}