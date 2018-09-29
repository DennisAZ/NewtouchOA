package com.psit.struts.DAO.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.psit.struts.DAO.SalEmpDAO;
import com.psit.struts.entity.SalEmp;
import com.psit.struts.entity.YHPerson;

/**
 * 员工个人资料数据库表实现类 <br>
 */

public class SalEmpDAOImpl extends HibernateDaoSupport implements SalEmpDAO{
	private static final Log log = LogFactory.getLog(SalEmpDAOImpl.class);
	// property constants
	static final String SE_NO = "seNo";
	static final String SE_IDE_CODE = "seIdeCode";
	static final String SE_SEX = "seSex";
	static final String SE_CODE = "seCode";
	static final String SE_NAME = "seName";
	static final String SE_EDU = "seEdu";
	static final String SE_POS = "sePos";
	static final String SE_AGE = "seAge";
	static final String SE_ADD = "seAdd";
	static final String SE_TEL = "seTel";
	static final String SE_EMAIL = "seEmail";
	static final String SE_QQ = "seQq";
	static final String SE_PHONE = "sePhone";
	static final String SE_MSN = "seMsn";
	static final String SE_RAP = "seRap";
	static final String SE_SAL = "seSal";
	static final String SE_REMARK = "seRemark";
	static final String SE_STATE = "seState";
    static final String SE_JOB_DATE = "seJobDate";
    static final String SE_END_DATE = "seEndDate";
	static final String SE_TYPE = "seType";
	static final String SE_ROL_NAME = "limRole.rolName";
	static final String SE_SO_NAME = "salOrg.soName";
	
	static final String SE_NO_DB = "se_no";
	static final String SE_NAME_DB = "se_name";
	
	protected void initDao() {
		// do nothing
	}
    /**
     * 保存员工信息<br>
     * @param transientInstance 员工表对象<br>
     */
	public void save(SalEmp transientInstance) {
		log.debug("saving SalEmp instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	/**
	 * 删除员工<br>
	 * @param persistentInstance 员工对象<br>
	 */
	public void delete(SalEmp persistentInstance) {
		log.debug("deleting SalEmp instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	/**
	 * 根据员工id获得员工<br>
	 * @param id 员工id
	 * @return SalEmp 员工对象<br>
	 */
	public SalEmp findById(java.lang.Long id) {
		log.debug("getting SalEmp instance with id: " + id);
		try {
			SalEmp instance = (SalEmp) getHibernateTemplate().get(
					"com.psit.struts.entity.SalEmp", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/**
	 * 员工列表 <br>
	 * @param seCode 员工编号
	 * @param seName 员工名称
	 * @param workstate 是否离职 0是在职 1是离职
	 * @param currentPage 当前页数
	 * @param pageSize 每页数量 String seCode,String seName
	 * @return List 返回员工列表 
	 */
	public List<SalEmp> salEmpSerach(String[]args,String orderItem,String isDe,int currentPage,int pageSize){
		Session session = (Session) super.getSession();
		Query query = getListHql(session, args, orderItem, isDe, false);
		query.setFirstResult((currentPage-1)*pageSize);
		query.setMaxResults(pageSize);
		List<SalEmp> list=query.list();
		return list;
	}
	public int getCountEmp(String[]args){
		Session session = (Session) super.getSession();
		Query query = getListHql(session, args, null, null, true);
        int count = Integer.parseInt(query.uniqueResult().toString());
		return count;
	}
	
	/**
	 * 生成员工查询的hql语句 <br>
	 * @param args 条件参数[0:员工编号，1:员工名称，2:是否离职,3:申请时间开始范围,4:申请时间结束范围]
	 * @param orderItem 排序字段
	 * @param isDe 是否逆序
	 * @param isCount 是否返回数量
	 * @return String SQL语句
	 */
	public Query getListHql (Session session, String[] args,String orderItem,String isDe,boolean isCount){
		StringBuffer appendSql = new StringBuffer();
		String sql = "from SalEmp salEmp where salEmp.seIsenabled='1' ";
		String tab = "salEmp.";
  		if(isCount){
  			sql = "select count(*) " + sql+" ";
  		}else{
  			sql = "select salEmp " + sql+" ";
  		}
		if(args != null){
		    if(args[0]!=null && !args[0].equals("")){
		    	appendSql.append("and "+tab+"seCode like :seCode ");
			}
		    if(args[1]!=null && !args[1].equals("")){
		    	appendSql.append("and "+tab+"seName like :seName ");
		    }
		    if(args[2]!=null && !args[2].equals("")){
		    	if(args[2].equals("0")){
		    		args[2] = "在职";
		    	}else if(args[2].equals("1")){
		    		args[2] = "离职";
		    	}
		    	appendSql.append("and "+tab+"seRap like '%"+args[2]+"%' ");
		    }
		}
		if(!isCount){
			if(orderItem != null && !orderItem.equals("")){
				String[] items = { "seCode", "seName", "seSex","seEdu","seJobDate","seEndDate", "rolName","seType", "soName","sePhone","seTel","qq","msn","email"};
				String cols[] = {SE_CODE,SE_NAME,SE_SEX,SE_EDU,SE_JOB_DATE,SE_END_DATE,SE_ROL_NAME,SE_TYPE,SE_SO_NAME,SE_PHONE,SE_TEL,SE_QQ,SE_MSN,SE_EMAIL};
				for(int i = 0; i < items.length; i++){
					if(orderItem.equals(items[i])){
						  orderItem = cols[i];
						}
				}
				appendSql.append("order by " + tab+orderItem + " ");
				if(isDe!=null && isDe.equals("1")){
					appendSql.append("desc ");
				}
			}
			else {//默认排序
				appendSql.append(" order by "+tab+SE_NO+" desc ");
			}
		}
		sql+=appendSql.toString();
		Query query = session.createQuery(sql);
		if(args!=null){
			if(args[0]!=null && !args[0].equals("")){
				query.setString("seCode", "%"+args[0]+"%");
			}
			if(args[1]!=null && !args[1].equals("")){
				query.setString("seName","%"+args[1]+"%");
			}
		}
		return query;
	}
	
	/**
	 * 查看员工详情<br>
	 * @param seNo 员工编号
	 * @return SalEmp 员工对象<br>
	 */
	public SalEmp salEmpDesc(Long seNo){	
		return (SalEmp)super.getHibernateTemplate().get(SalEmp.class,seNo);
	}
	/**
	 * 按员工工号查询<br>
	 * @param seCode 员工工号
	 * @return List 员工记录列表<br>
	 */
	public List getEmpByCode(String seCode){
		Session session = (Session) super.getSession();
		String sql="select seCode from SalEmp where seCode= :code ";
		Query query=session.createQuery(sql).setString("code", seCode);
		List list=(List)query.list();
		return list;
	}
	/**
	 * 根据编号查询<br>
	 * @param seNo 员工编号
	 * @return List 员工记录列表<br>
	 */
	public List getEmp(String seNo){
		log.debug("finding all SalEmp instances");
		try {
			String queryString = "from SalEmp where seIsenabled='1' and seNo="+Long.valueOf(seNo);
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	/**
	 * 修改员工信息<br>
	 * @param salEmp 员工对象<br>
	 */
	public void updateSalEmp(SalEmp salEmp){
		getHibernateTemplate().merge(salEmp);
	}
   /**
    * 根据部门编号查询与员工关联的部门<br>
    * @param soCode 员工编号
	* @return List 员工记录列表<br>
    */
	public List getSalOrgByCode(String soCode) {
		return super.getHibernateTemplate().find("from SalEmp where salOrg.soCode='"+soCode+"'");
	}
	
	/**
	 * 得到比传入职级小的未离职员工（无分页） <br>
	 * @param seLev 职级
	 * @return List<SalEmp> 员工list
	 */
	public List<SalEmp> getEmpsByRole(String seLev) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql = "from SalEmp where seIsenabled='1' and  seRap!='离职' ";
		if(seLev!=null&&!seLev.equals("")){
			hql += "and limRole.rolLev > " + seLev + " ";
		}
		hql += "order by seName";
		query = session.createQuery(hql);
		List<SalEmp> list = query.list();
		return list;
	}
	
	/**
	 * 查询所有未离职的员工（无分页，根据员工姓名排序） <br>
	 * @return List<SalEmp> 未离职的员工列表
	 */
	public List<SalEmp> getEmpList(){
		Session session= (Session) super.getSession();
		String sql ="from SalEmp as salEmp where seRap!='离职' and seIsenabled='1' order by seName";
		Query query=session.createQuery(sql);
		List<SalEmp> list = query.list();
		return list;
	}
	
	/**
	 * 获得待删除的所有员工<br>
	 * @param pageCurrentNo 当前页码
	 * @param pageSize 每页显示的记录数
	 * @return List 员工记录列表<br>
	 */
	public List findDelSalEmp(int pageCurrentNo, int pageSize) {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="from SalEmp where seIsenabled='0' order by seInserDate desc ";
		query=session.createQuery(hql);	
		query.setFirstResult((pageCurrentNo - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list=(List)query.list();
		return list;
	}

	/**
	 * 获得待删除的所有员工记录数量<br>
	 * @return 员工记录数量<br>
	 */
	public int findDelSalEmpCount() {
		Query query;
		Session session;
		session = (Session) super.getSession();
		String hql ="select count(*) from SalEmp where seIsenabled='0'";
		query=session.createQuery(hql);	
		int count = (Integer.parseInt(query.uniqueResult().toString()));
		return count;
	}
	@Override
	public YHPerson findPersonById(Long id) {
		// TODO Auto-generated method stub
		log.debug("getting YHPerson instance with id: " + id);
		try {
			YHPerson instance = (YHPerson) getHibernateTemplate().get(
					"com.psit.struts.entity.YHPerson", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}