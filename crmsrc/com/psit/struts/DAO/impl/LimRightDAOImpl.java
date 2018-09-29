package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.psit.struts.DAO.LimRightDAO;
import com.psit.struts.entity.LimRight;
/**
 * 权限DAO实现类 <br>
 */
public class LimRightDAOImpl extends HibernateDaoSupport implements LimRightDAO{
	/**
	 * 获得某一功能模块的所有操作<br>
	 * @param funType 功能类型
	 * @return List 权限列表
	 */
	public List getLimRight(String funType) {
		Session session = super.getSession();
		String sql ="from LimRight where limFunction.funType=:funType order by rigCode";
		Query query=session.createQuery(sql).setString("funType", funType);
		return query.list();
	}
	/**
	 * 删除仓库对应的权限 <br>
	 * @param rigCode 权限编号
	 */
	public void delLimRight(String rigCode) {
		Session session = super.getSession();
		String sql ="delete from LimRight where rigCode =:rigCode";
		Query query=session.createQuery(sql).setParameter("rigCode", rigCode);
		query.executeUpdate();
	}
	/**
	 * 保存仓库的权限码 <br>
	 * @param limRight 权限实体
	 */
	public void savLimRight(LimRight limRight) {
		super.getHibernateTemplate().save(limRight);
	}
	/**
	 * 根据权限码获得仓库 <br>
	 * @param rigCode 权限编号
	 * @return LimRight 返回权限实体
	 */
	public LimRight findLimRight(String rigCode) {
		return (LimRight) super.getHibernateTemplate().get("com.psit.struts.entity.LimRight", rigCode);
	}
	/**
	 * 修改权限实体 <br>
	 * @param limRight 权限实体
	 */
	public void updLimRight(LimRight limRight) {
		super.getHibernateTemplate().update(limRight);
	}

}