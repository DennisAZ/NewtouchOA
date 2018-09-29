package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RMessLimDAO;
import com.psit.struts.entity.RMessLim;
/**
 * 消息接收人DAO实现类 <br>
 */
public class RMessLimDAOImpl extends HibernateDaoSupport implements RMessLimDAO
{
	/**
	 * 保存接收人 <br>
	 * @param rmessLim 接收人实体
	 */
	public void save(RMessLim rmessLim) 
	{
		super.getHibernateTemplate().save(rmessLim);
	}
	/**
	 * 得到接收人实体 <br>
	 * @param id 接收人id
	 * @return RMessLim 返回接收人实体  
	 */
	public RMessLim getRMessLim(Long id) {
		return (RMessLim) super.getHibernateTemplate().get(RMessLim.class, id);
	}
	/**
	 * 更新接收人消息 <br>
	 * @param rmessLim 接收人实体
	 */
	public void update(RMessLim rmessLim) {
		super.getHibernateTemplate().update(rmessLim);
		
	}
	/**
	 * 获得待删除的所有已收消息 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回消息列表 
	 */
	public List findDelMail(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="from RMessLim where  rmlIsdel='0' order by rmlId desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 获得待删除的所有已收消息数量 <br>
	 * @return int 返回消息列表数量 
	 */
	public int findDelMailCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from RMessLim where  rmlIsdel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	/**
	 * 删除已收消息 <br>
	 * @param rmessLim 接收人实体
	 */
	public void delRMessLim(RMessLim rmessLim) {
		super.getHibernateTemplate().delete(rmessLim);
	}
	
}