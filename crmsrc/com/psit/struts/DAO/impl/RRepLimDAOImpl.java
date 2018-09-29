package com.psit.struts.DAO.impl;


import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RRepLimDAO;
import com.psit.struts.entity.RRepLim;
/**
 * 报告接收人DAO实现类 <br>
 */
public class RRepLimDAOImpl extends HibernateDaoSupport implements RRepLimDAO
{
	/**
	 * 保存报告接收人 <br>
	 * @param rrepLim 报告接收人实体
	 */
	public void save(RRepLim rrepLim) 
	{
		super.getHibernateTemplate().save(rrepLim);
	}
	/**
	 * 报告接受人实体 <br>
	 * @param id 接受人id
	 * @return RRepLim 返回报告接受人实体
	 */
	public RRepLim getRRepLim(Long id) {
		return (RRepLim) super.getHibernateTemplate().get(RRepLim.class, id);
	}
	/**
	 * 更新审批内容 <br>
	 * @param rrepLim 报告接受人实体
	 */
	public void update(RRepLim rrepLim) {
		super.getHibernateTemplate().update(rrepLim);
	}
	/**
	 * 查看某一报告是否存在下一审批步骤 <br>
	 * @param appOrder 审批步骤
	 * @param repCode 报告id
	 * @return List 返回报告列表
	 */
	public List getRRepLim(Integer appOrder, String repCode) {
		String sql="from RRepLim where rrlAppOrder="+appOrder+" and report.repCode='"+repCode+"' and rrlIsdel='1' order by rrlId desc";
		return super.getHibernateTemplate().find(sql);
	}
	/**
	 * 查看某一报告是否审批结束 <br>
	 * @param repCode 报告id
	 * @return List 返回报告列表
	 */
	public List getRRepLim(String repCode) {
		String sql="from RRepLim where report.repCode='"+repCode+"' and rrlIsdel='1' order by rrlId desc";
		return super.getHibernateTemplate().find(sql);
	}

//	public void delRRepLim(RRepLim rrepLim) {
//		super.getHibernateTemplate().delete(rrepLim);
//		
//	}
	
}