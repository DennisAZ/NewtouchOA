package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.MemDateDAO;
import com.psit.struts.entity.MemDate;

public class MemDateDAOImpl extends HibernateDaoSupport implements MemDateDAO{
	private static final Log log = LogFactory.getLog(MemDateDAO.class);
	// property constants
	public static final String MD_ID = "mdId";
	public static final String MD_NAME = "mdName";
	public static final String MD_DATE = "mdDate";
	public static final String MD_CON_ID = "mdContact.conId";
	
	protected void initDao() {
		// do nothing
	}

	/**
	 * 纪念日列表<br>
	 * @param conId:联系人Id
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量
	 * @return List<MemDate>
	 */
	public List<MemDate> listByCon(String conId, String orderItem,
			String isDe, int currentPage, int pageSize) {
		Session session = (Session)super.getSession();
		String sql = "from MemDate where "+MD_CON_ID+"="+conId+" order by "+MD_ID+" desc";
		Query query = session.createQuery(sql);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<MemDate> list = query.list();
		return list;
	}
	public int listByConCount(String conId) {
		Session session = (Session)super.getSession();
		String sql = "select count(*) from MemDate where "+MD_CON_ID+"="+conId;
		Query query = session.createQuery(sql);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	public void update(MemDate memDate) {
		super.getHibernateTemplate().merge(memDate);
	}
	
	public void save(MemDate memDate) {
		log.debug("saving MemDate instance");
		try {
			getHibernateTemplate().save(memDate);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(MemDate memDate) {
		log.debug("deleting MemDate instance");
		try {
			getHibernateTemplate().delete(memDate);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MemDate findById(Long id) {
		log.debug("getting MemDate instance with id: " + id);
		try {
			MemDate instance = (MemDate) getHibernateTemplate().get(
					"com.psit.struts.entity.MemDate", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
