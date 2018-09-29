package com.psit.struts.DAO.impl;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.CusProvinceDAO;
import com.psit.struts.entity.CusProvince;
/**
 * 省份DAO实现类 <br>
 */
public class CusProvinceDAOImpl extends HibernateDaoSupport implements CusProvinceDAO{
	
	/**
	 * 获取管辖范围
	 */
	public List<java.lang.Long> getMyManageCusProvince(long id) {
		String HQL = "select distinct(b.province_id) from CrmManagementArea a,CrmManagementAreaRange b where a.Id=b.ma_id and b.country_id="+id+" order by b.province_id";
		List<java.lang.Long> list =  super.getHibernateTemplate().find(HQL);
		return list;
	}
	
	/**
	 * 获取管辖的省份
	 */
	public List<CusProvince> getmyManageProvinceName(long privId) {
		return super.getHibernateTemplate().find("from CusProvince p where p.prvIsenabled=1 and p.prvId="+privId);
	}
	/**
	 * 获得已启用的省 <br>
	 * @param id 省份id
	 * @return List 返回省份列表 
	 */
	public List getCusProvince(long id) {
		return super.getHibernateTemplate().find("from CusProvince where prvIsenabled=1 and cusArea.areId="+id);
	}
	/**
	 * 获取所有省份 <br>
	 * @return List 返回省份列表 
	 */
	public List getAllProvince() {
		return super.getHibernateTemplate().find("from CusProvince as cp order by cp.cusArea.areId");
	}
	/**
	 * 保存省份信息 <br>
	 * @param cusProvince 省份实体
	 */
	public void saveProvince(CusProvince cusProvince) {
		super.getHibernateTemplate().save(cusProvince);
	}
	/**
	 * 得到省份实体 <br>
	 * @param id 省份id
	 * @return CusProvince 返回省份实体 
	 */
	public CusProvince showProvince(long id){
		return (CusProvince)super.getHibernateTemplate().get(CusProvince.class, id);
	}
}