package com.psit.struts.DAO.impl;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.CusAreaDAO;
import com.psit.struts.entity.CusArea;
/**
 * 国家DAO实现类 <br>
 */
public class CusAreaDAOImpl extends HibernateDaoSupport implements CusAreaDAO {
	
	/**
     * 获得已启用的国家 <br>
     * @return List 返回国家列表
     */
	public List getCusArea() {
		return super.getHibernateTemplate().find("from CusArea where areIsenabled=1");
	}
	/**
	 * 获得所有国家 <br>
	 * @return List 返回国家列表
	 */
	public List getAllCusArea() {
		return super.getHibernateTemplate().find("from CusArea");
	}
	/**
	 * 保存国家 <br>
	 * @param cusArea 国家实体
	 */
	public void saveCountry(CusArea cusArea) {
		super.getHibernateTemplate().save(cusArea);
		
	}
	/**
	 * 根据国家id获得国家实体 <br>
	 * @param id 国家id
	 * @return CusArea 返回国家实体
	 */
	public CusArea showCountry(long id){
		return (CusArea)super.getHibernateTemplate().get(CusArea.class, id);
	}
}