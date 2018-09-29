package com.psit.struts.DAO.impl;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RNewLimDAO;
import com.psit.struts.entity.RNewLim;
/**
 * 新闻公告接收人DAO实现类 <br>
 */
public class RNewLimDAOImpl extends HibernateDaoSupport implements RNewLimDAO{
	/**
	 * 保存新闻公告接收人 <br>
	 * @param rnewLim 新闻接收人实体
	 */
	public void save(RNewLim rnewLim) {
		super.getHibernateTemplate().save(rnewLim);
		
	}
}