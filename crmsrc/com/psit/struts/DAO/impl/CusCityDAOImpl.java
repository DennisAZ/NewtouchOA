package com.psit.struts.DAO.impl;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.CusCityDAO;
import com.psit.struts.entity.CusCity;
import com.psit.struts.entity.CusProvince;
/**
 * 城市DAO实现类 <br>
 */
public class CusCityDAOImpl extends HibernateDaoSupport implements CusCityDAO{
	
	
	/**
	 * 获取管辖范围内的城市
	 */
	public List<java.lang.Long> getmyManageCity(long id) {
		String HQL = "select distinct(a.city_id) from CrmManagementAreaRange a where a.province_id="+id+" order by a.city_id";
		List<java.lang.Long> list =  super.getHibernateTemplate().find(HQL);
		return list;
	}
	
	/**
	 * 获取管辖范围内的城市名称
	 */
	public List<CusCity> getmyManageCityName(long cityId) {
		return super.getHibernateTemplate().find("from CusCity c where c.cityIsenabled=1 and c.cityId="+cityId);
	}

	/**
	  * 获得省份下已启用的城市 <br>
	  * @param id 省份id
	  * @return List 返回城市列表
	  */
	public List getCusCity(long id) {
		
		return super.getHibernateTemplate().find("from CusCity where cityIsenabled=1 and cusProvince.prvId="+id);
	}
	 /**
	  * 获得所有城市 <br>
	  * @return List 返回城市列表
	  */
	public List getAllCity() {
		return super.getHibernateTemplate().find("from CusCity as cc order by cc.cusProvince.prvId");
	}
	/**
	 * 保存城市 <br>
	 * @param cusCity 城市实体
	 */
	public void saveCity(CusCity cusCity) {
		super.getHibernateTemplate().save(cusCity);
		
	}
	 /**
	  * 获得城市<br>
	  * @param id
	  * @return CusCity 城市<br>
	  */
	public CusCity showCity(long id) {
		return (CusCity)super.getHibernateTemplate().get(CusCity.class, id);
	}
}