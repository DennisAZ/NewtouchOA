package com.psit.struts.DAO.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.WmsProductDAO;
import com.psit.struts.entity.WmsProduct;
import com.psit.struts.entity.WmsProType;

/**
 * 商品DAO实现类 <br>
 */
public class WmsProductDAOImpl extends HibernateDaoSupport implements WmsProductDAO{
	private static final Log log = LogFactory.getLog(WmsProductDAOImpl.class);
	// property constants
	public static final String WPR_ID = "wprId";
	public static final String WPR_ISDEL = "wprIsdel";
	public static final String WPR_NAME = "wprName";
	public static final String WPR_CODE = "wprCode";
	public static final String WPR_MOD = "wprModel";
	public static final String WPR_STATE = "wprStates";
	public static final String WPR_COST_PRC = "wprCostPrc";
	public static final String WPR_SALE_PRC = "wprSalePrc";
	public static final String WPR_NORMAL_PER = "wprNormalPer";
	public static final String WPR_OVER_PER = "wprOverPer";
	public static final String WPR_LOW_PER = "wprLowPer";
	public static final String WPR_DESC = "wprDesc";
	public static final String WPR_REMARK = "wprRemark";
	public static final String WPR_TYPE = "wmsProType";
	public static final String WPR_TYPE_ID = "wmsProType.wptId";
	public static final String WPR_TYPE_NAME = "wmsProType.wptName";
	public static final String WPR_UNIT = "typeList";
	public static final String WPR_UNIT_NAME = "typeList.typName";
	
	/**
	 * 保存商品 <br>
	 * @param transientInstance 商品实体
	 */
	public void save(WmsProduct transientInstance) {
		log.debug("saving WmsProduct instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	/**
	 * 删除商品 <br>
	 * @param persistentInstance 商品实体
	 */
	public void delete(WmsProduct persistentInstance) {
		log.debug("deleting WmsProduct instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	/**
	 * 商品列表<br>
	 * @param args 	[0]是否删除(1已删除,0未删除);  [1]商品名;  [2]商品编号;  [3]商品型号;  
	 * 				[4]商品分类;
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param currentPage 当前页
	 * @param pageSize 每页条数
	 * @return List<WmsProduct> 商品列表
	 */
	public List<WmsProduct> listProds(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize){
		Session session = (Session) super.getSession();
		Query query = getProdSql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<WmsProduct> list = query.list();
		return list;
	}
	public int listProdsCount(String[]args){
		Session session = (Session) super.getSession();
		Query query = getProdSql(session, args, null, null, true);
		int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	private Query getProdSql(Session session, String[]args, String orderItem, String isDe,
			boolean isCount) {
		StringBuffer appendSql = new StringBuffer();
		String tab = "prod.";//表更名
		String sql = "from WmsProduct prod ";
  		if(isCount){
  			sql = "select count(*) " + sql;
  		}
  		else{
  			sql = "select prod " + sql;
  		}
		if(args != null){
			if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("where ");
		    	appendSql.append(tab+WPR_ISDEL+" = '"+args[0]+"' ");
		    }
			if(args[1]!=null && !args[1].equals("")){
	    		appendSql.append("and ");
	    		appendSql.append(tab+WPR_NAME +" like :proName ");
			}
			if(args[2]!=null && !args[2].equals("")){
		    	appendSql.append("and ");
		    	appendSql.append(tab+WPR_CODE+" like :proCode ");
		    }
		    if(args[3]!=null && !args[3].equals("")){
		    	appendSql.append("and ");
		    	appendSql.append(tab+WPR_MOD+" like :proMod ");
		    }
	    	if (args[4] != null && !args[4].equals("")) {
				appendSql.append("and ");
				if (args[4].equals("none")) {
					appendSql.append(tab + WPR_TYPE_ID + " is null ");
				} else {
					// curIndex为当前链表遍历位置
					int curIndex = 0;
					// 定义指针链表wptId保存某个id以及所有属于这个id下级的id
					List wptId = new LinkedList();
					// 初始化将“头”id赋值给数组第一个元素
					wptId.add(0, args[4]);
					// 循环获得所有id，当某一次遍历未查询过id的下级为空时，结束循环
					while (true) {
						List list1 = new LinkedList(), list2;
						// 循环获得当前列表中未查询过id的下级id
						for (; curIndex < wptId.size(); curIndex++) {
							String sql5 = "select wpt from WmsProType as wpt where wmsProType.wptId= "
									+ wptId.get(curIndex);
							Query query5 = session.createQuery(sql5);
							list2 = query5.list();

							Iterator it2 = list2.iterator();
							while (it2.hasNext()) {
								list1.add(it2.next());
							}
						}
						// 判断此次查询是否为空，表明已经没有下级id了，跳出循环
						if (list1.size() == 0){
							break;
						}

						Iterator it = list1.iterator();
						// 将下级id加入链表中以备查询
						while (it.hasNext()) {
							WmsProType wms = (WmsProType) it.next();
							wptId.add(wms.getWptId().toString());
						}
					}
					// 将链表中所有的id加入sql语句中
					appendSql.append(tab + WPR_TYPE_ID + " in (");
					for (int j = 0; j < wptId.size(); j++) {
						appendSql.append(wptId.get(j));
						if (j != (wptId.size()-1)) {
							appendSql.append(", ");
						}
					}
					appendSql.append(") ");
				}
			}
		}
		if (!isCount) {
			if (orderItem != null && !orderItem.equals("")) {
				String[] items = { "proState", "proName","proMod", "proCode",
						"proType", "proUnit","price","normalPer","overPer","lowPer"};
				String[] cols = { WPR_STATE, WPR_NAME, WPR_MOD, WPR_CODE,
						WPR_TYPE_NAME, WPR_UNIT_NAME ,WPR_SALE_PRC,WPR_NORMAL_PER,
						WPR_OVER_PER,WPR_LOW_PER};
				for (int i = 0; i < items.length; i++) {
					if (orderItem.equals(items[i])) {
						switch (i) {
						case 4:
							sql += "left join " + tab + WPR_TYPE + " ";
							break;
						case 5:
							sql += "left join " + tab + WPR_UNIT + " ";
							break;
						}
						orderItem = cols[i];
						break;
					}
				}
				appendSql.append("order by " +tab+orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			} else {//默认排序
				appendSql.append("order by "+tab+WPR_ID+" desc ");
			}
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args[1]!=null && !args[1].equals("")){
				query.setString("proName", "%"+args[1]+"%");
			}
			if(args[2]!=null && !args[2].equals("")){
				query.setString("proCode", "%"+args[2]+"%");
			}
			if(args[3]!=null && !args[3].equals("")){
				query.setString("proMod","%"+args[3]+"%");
			}
		}
		return query;
	}
	/**
	 * 根据商品id查询商品实体 <br>
	 * @param wprId 商品id
	 * @return WmsProduct 返回商品实体
	 */
	public WmsProduct wmsProDesc(Long wprId){	
		return (WmsProduct)super.getHibernateTemplate().get(WmsProduct.class,wprId);
	}
	/**
	 * 根据商品的编号查询商品实体 <br>
	 * @param wprCode 商品编号
	 * @return List 返回商品列表
	 */
	public List findbyWprCode(String wprCode) {
		Session session = (Session) super.getSession();
		String sql ="select wprCode from WmsProduct where  wprCode =:wprCode";
		Query query=session.createQuery(sql).setParameter("wprCode", wprCode);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 修改商品信息 <br>
	 * @param wmsProduct 商品实体
	 */
	public void wmsProUpdate(WmsProduct wmsProduct){	
		super.getHibernateTemplate().merge(wmsProduct);	
	}
	/**
	 * 按商品名称/编号查询 <br>
	 * @param wprName 商品名称/型号/编号
	 * @param agr 是否计算库存标识
	 * @return List 返回商品列表
	 */
	public List proSearch(String wprName,String agr){
		Session session = (Session) super.getSession();
		String sq = "";
		if (agr.equals("2")) {
			sq = "and wprIscount='1'";
		}
		String sql = "from WmsProduct where wprIsdel='0'" + sq + " and "
				+ "(wprName  like :wName or wprCode like :wCode) order by wprId desc";
		Query query = session.createQuery(sql)
						.setString("wName", "%" + wprName + "%")
						.setString("wCode", "%" + wprName + "%");
		List list = (List) query.list();
		return list;
	}
	
	/**
	 * 按商品名称/型号/编号查询 <br>
	 * @param wprName 商品名称/型号/编号
	 * @return List<WmsProduct> 返回商品列表
	 */
	public List<WmsProduct> searchProdByName(String wprName){
		Session session = (Session) super.getSession();
		String sql = "from WmsProduct where "+WPR_ISDEL+"='0' " +
				"and ("+WPR_NAME+" like :prodName or "+WPR_CODE+" like :prodCode) " +
				"order by "+WPR_ID+" desc";
		Query query = session.createQuery(sql)
						.setString("prodName", "%" + wprName + "%")
						.setString("prodCode", "%" + wprName + "%");
		List<WmsProduct> list = query.list();
		return list;
	}
	/**
	 * 判断商品名称型号是否重复 <br>
	 * @param wprName 商品名称
	 * @param wprMod 型号
	 * @return List 返回商品列表
	 */
	public List proCheck(String wprName){
		Session session = (Session) super.getSession();
		String sql ="select wprName from WmsProduct where wprName =:wprName ";
		Query query=session.createQuery(sql).setParameter("wprName", wprName);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 查询未分类的商品 <br>
	 * @return List 返回商品列表
	 */
	public List<WmsProduct> noTypePro(){
		Query query;
		Session session;
		session = (Session) super.getSession();
		String sql ="from WmsProduct where wprIsdel='0' and wmsProType is null";
		query=session.createQuery(sql);
		List<WmsProduct> list= query.list();
		return list;
	}
	/**
	 * 获得待删除的所有商品列表 <br>
	 * @param pageCurrentNo 当前页数
	 * @param pageSize 每页数量
	 * @return List 返回商品列表
	 */
	public List findDelProduct(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="from WmsProduct where  wprIsdel='1' order by wprId desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}
	public int findDelProductCount() {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="select count(*) from WmsProduct where  wprIsdel='1'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
}