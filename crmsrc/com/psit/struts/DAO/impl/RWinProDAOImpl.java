package com.psit.struts.DAO.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.RWinProDAO;
import com.psit.struts.entity.RWinPro;
/**
 * 
 * 入库明细DAO实现类 <br>
 *
 * create_date: Aug 17, 2010,4:20:35 PM<br>
 * @author zjr
 */
@SuppressWarnings("unchecked")
public class RWinProDAOImpl extends HibernateDaoSupport implements RWinProDAO  {
	private static final Log log = LogFactory.getLog(RWinProDAOImpl.class);
	/**
	 * 
	 * 保存入库明细 <br>
	 * create_date: Aug 11, 2010,2:43:37 PM <br>
	 * @param rwinPro 入库明细实体
	 */
	public void save(RWinPro rwinPro){
		super.getHibernateTemplate().save(rwinPro);
	}
	//查询所有入库商品
//	public List<RWinPro> findAll(){
//		log.debug("finding all RWinPro instances");
//		try {
//			String queryString = "from RWinPro";
//			return getHibernateTemplate().find(queryString);
//		} catch (RuntimeException re) {
//			log.error("find all failed", re);
//			throw re;
//		}
//	}
	/**
	 * 
	 * 查询所有入库商品 <br>
	 * create_date: Aug 11, 2010,2:44:10 PM <br>
	 * @param wwiId 入库明细id
	 * @return List<RWinPro> 返回入库明细列表
	 */
	public List<RWinPro> getRwinPro(Long wwiId){
		Query query;
		Session session;
	    session = (Session) super.getSession();
		String sql ="select rWinPro from RWinPro as rWinPro where rWinPro.wmsWarIn.wwiId = "+wwiId+"order by rwiId desc";
		query=session.createQuery(sql);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 
	 * 根据入库单号删除入库明细 <br>
	 * create_date: Aug 11, 2010,3:00:06 PM <br>
	 * @param wwiId 入库单id
	 */
	public void delRwinPro(Long wwiId){
		Query query;
		Session session;
		session = (Session) super.getSession();
		log.debug("finding all RWinPro instances");
		try {
			String queryString ="delete from RWinPro as rWinPro where rWinPro.wmsWarIn.wwiId = "+wwiId;
			 int i=session.createQuery(queryString).executeUpdate();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 根据id查询入库明细实体 <br>
	 * create_date: Aug 11, 2010,2:50:18 PM <br>
	 * @param rwiId 入库明细id
	 * @return RWinPro 返回入库明细实体
	 */
	public RWinPro getRWinPro(Long rwiId){
		return (RWinPro)super.getHibernateTemplate().get(RWinPro.class, rwiId);
	}
	/**
	 * 
	 * 更新入库商品 <br>
	 * create_date: Aug 11, 2010,2:51:24 PM <br>
	 * @param rwinPro 入库明细实体
	 */
	public void updateRwp(RWinPro rwinPro){
		super.getHibernateTemplate().update(rwinPro);
	}
	/**
	 * 
	 * 删除入库商品 <br>
	 * create_date: Aug 11, 2010,2:53:45 PM <br>
	 * @param rwinPro 入库明细实体
	 */
	public void delRwp(RWinPro rwinPro){
		super.getHibernateTemplate().delete(rwinPro);
	}
	/**
	 * 
	 * 查询入库明细中的商品 <br>
	 * create_date: Aug 11, 2010,3:23:32 PM <br>
	 * @param wprId 商品id
	 * @return List<RWinPro> 返回入库明细列表
	 */
	public List<RWinPro> findByWpr(Long wprId) {
		log.debug("finding all RWinPro instances");
		try {
			String queryString = "from RWinPro where wmsProduct.wprId = "+wprId;
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 
	 * 按入库单号和商品id查询入库明细 <br>
	 * create_date: Aug 11, 2010,3:25:55 PM <br>
	 * @param wprId 商品id
	 * @param wwiCode 入库单号
	 * @return List<RWinPro> 返回入库明细列表
	 */
	public List<RWinPro> findByWpr(Long wprId,String wwiCode) {
		log.debug("finding all RWinPro instances");
		try {
			String queryString = "from RWinPro where wmsProduct.wprId = "+wprId+" and wmsWarIn.wwiCode = '"+wwiCode+"'";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
}
