package com.psit.struts.DAO.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.psit.struts.DAO.RUserRigDAO;
import com.psit.struts.entity.LimRight;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.RUserRig;
/**
 * 用户权限DAO实现类 <br>
 */
public class RUserRigDAOImpl extends HibernateDaoSupport implements RUserRigDAO {
	/**
	 * 根据账号获取该账号的所有权限 <br>
	 * @param userCode 账号
	 * @return List 返回权限列表
	 */
	public List getRUserRig(String userCode) {
		return super.getHibernateTemplate().find("from RUserRig where limUser.userCode='"+userCode+"'");
	}
	/**
	 * 为某账号添加权限 <br>
	 * @param userLims 权限数组
	 * @param userCode 账号
	 * @param rurType 功能类型
	 */
	public void addRUserRig(String[] userLims,String userCode,String rurType) {
		   Session session;
		   session = (Session) super.getSession();
		   if(userLims!=null)
		    {  
				 for(int i=0;i<userLims.length;i++)
				   {
					   RUserRig ruserRig=new RUserRig();
					   ruserRig.setLimRight(new LimRight(userLims[i]));
					   ruserRig.setLimUser(new LimUser(userCode));
					   ruserRig.setRurType(rurType);
					   session.save(ruserRig);
					   if(userLims.length>0&&userLims.length<20)
					   {
						   session.flush();
						   session.clear();
					   }
					   else if(i%20==0)
					   {
						   session.flush();
						   session.clear();
					   }
				   }
		    }
	}
	/**
	 * 删除特定账号的权限 <br>
	 * create_date: Aug 11, 2010,11:56:58 AM <br>
	 * @param userCode 账号
	 * @param funType 功能类型
	 */
	public void delRUserRig(String userCode,String funType) {
		Session session=(Session) super.getSession();
		String sql="delete from RUserRig where  rurType=:funType and limUser.userCode='"+userCode+"'";
		Query query=session.createQuery(sql).setString("funType", funType);
		query.executeUpdate();
	}
	/**
	 * 根据账号获取该账号在某个功能类型下的所有权限 <br>
	 * @param userCode 账号
	 * @param rurType 功能类型
	 * @return List 返回权限列表
	 */
	public List getRUserRig(String userCode, String rurType) {
		Session session=(Session) super.getSession();
		String sql="from RUserRig where limUser.userCode='"+userCode+"' and rurType=:rType";
		Query query=session.createQuery(sql).setString("rType", rurType);
		return query.list();
	}
}