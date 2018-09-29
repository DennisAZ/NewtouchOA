package com.psit.struts.DAO.impl;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.QuoteDAO;
import com.psit.struts.entity.Quote;

/**
 * 报价表的数据库操作实现类 <br>
 */
public class QuoteDAOImpl extends HibernateDaoSupport implements QuoteDAO{

	/**
	 * 保存报价信息<br>
	 * @param quote 报价记录对象
	 */
	public void saveQuote(Quote quote) {
		super.getHibernateTemplate().save(quote);
		
	}

	/**
	 * 根据Id获得报价<br>
	 * @param id 报价记录id
	 * @return Quote 返回报价实体
	 */
	public Quote showQuote(Long id) {
		return (Quote) super.getHibernateTemplate().get(Quote.class, id);
	}

	/**
	 * 更新报价信息<br>
	 * @param quote 报价记录对象
	 */
	public void updateQuo(Quote quote) {
		super.getHibernateTemplate().update(quote);
	}

	/**
	 * 删除机会报价<br>
	 * @param quote 报价记录对象
	 */
	public void delQuote(Quote quote) {
		super.getHibernateTemplate().delete(quote);
	}

	/**
	 * 获得待删除的所有报价记录<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示记录数
	 * @return List 返回报价记录列表
	 */
	public List findDelQuote(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		
		session = super.getSession();
		String hql ="from Quote where  quoIsDel='0' order by quoInsDate desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}

	/**
	 * 获得待删除的所有报价记录数量<br>
	 * create_date: Aug 9, 2010,10:38:14 AM<br>
	 * @return 返回报价记录数量
	 */
	public int findDelQuoteCount() {
		Query query;
		Session session;
		session = super.getSession();
		String hql ="select count(*) from Quote where  quoIsDel='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		
		 
		return count;
	}
}