package com.psit.struts.util.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
/**
 * 
 * 获得实体下的关联数据DAO实现类 <br>
 *
 * create_date: Aug 23, 2010,3:22:44 PM<br>
 * @author zjr
 */
public class GetAccDataDAOImpl extends HibernateDaoSupport implements GetAccDataDAO{

	/**
	 * 获得特定实体下的关联数据 <br>
	 * create_date: Aug 23, 2010,3:05:02 PM <br>
	 * @param id 特定实体的id值
	 * @param entityName 关联实体名
	 * @param relEntity 特定实体+特定实体的主键
	 * @param remLogo 关联实体的删除标识字段
	 * @param isDel 1或0
	 * @param ordLogo 排序标识
	 * @return List 返回关联数列表<br>
	 */
	public List getAccData(Long id,String entityName,String relEntity,String remLogo,String isDel,String ordLogo,int currentPage,int pageSize){
		Session session=super.getSession();
		String qurryString="";
		if(isDel!=null&&isDel.equals("n")){
			qurryString=" and "+remLogo+"!='1'";
		}
		else{
			qurryString=" and "+remLogo+"='"+isDel+"'";
		}
		String sql="from "+entityName+" where "+relEntity+"="+id+qurryString+" order by "+ordLogo+" desc";
		Query query=session.createQuery(sql);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List list=query.list();
		return list;
	 }
	/**
	 * 获得特定实体下的关联数据的数量 <br>
	 * create_date: Aug 23, 2010,3:16:45 PM <br>
	 * @param id 特定实体的id值
	 * @param entityName 关联实体名
	 * @param relEntity 特定实体+特定实体的主键
	 * @param remLogo 关联实体的删除标识字段
	 * @param isDel 1或0
	 * @return 返回关联数列表数量<br>
	 */
	 public int getAccDataCount(Long id,String entityName,String relEntity,String remLogo,String isDel){
		 Session session=super.getSession();
		 String qurryString="";
		 if(isDel!=null&&isDel.equals("n")){
			 qurryString=" and "+remLogo+"!='1'";
			}
		 else{
			 qurryString=" and "+remLogo+"='"+isDel+"'";
		 }
		 String sql="select count(*) from "+entityName+" where "+relEntity+"="+id+qurryString;
		 Query query=session.createQuery(sql);
		 int count=Integer.parseInt(query.uniqueResult().toString());
		 return count;
	 }
}
