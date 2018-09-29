package com.psit.struts.util.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
/**
 * 
 * 修改类别 <br>
 *
 * create_date: Aug 23, 2010,3:43:50 PM<br>
 * @author zjr
 */
public class ModifyTypeDAO extends HibernateDaoSupport{

	/**
	 * 
	 * 修改类别 <br>
	 * create_date: Aug 23, 2010,3:49:14 PM <br>
	 * @param tableName 表名
	 * @param prefix    表前缀名
	 * @param typeNames 类别名数组
	 * @param typeIds	类别id数组
	 * @param enabledIds 是否启用多选框值的数组<br>
	 */
	public void modType(String tableName,String prefix,String[] typeNames,String[] typeIds,String[] enabledIds){
		Session session = super.getSession();
		Transaction tx = session.beginTransaction();

		for (int i = 0; i < typeIds.length; i++) {
			int mark = 0;//标识是否已启用
			if (enabledIds != null) {
				for (int j = 0; j < enabledIds.length; j++) {
					if (typeIds[i].equals(enabledIds[j])) {
						mark = 1;
						break;
					}
				}
			}
			if (mark == 1) {
				String hqlUpdate = "update " + tableName
							+ " set " + prefix + "Name = :typeName,"
							+ prefix + "Isenabled = :isEnabled"
							+ " where " + prefix + "Id = :typeId";
				int updatedEntities = session.createQuery(hqlUpdate)
									.setString("typeName", typeNames[i])
									.setString("isEnabled", "1")
									.setString("typeId", typeIds[i]).executeUpdate();
			} else {
				String hqlUpdate = "update "+tableName
							+ " set " + prefix + "Name = :typeName,"
							+ prefix + "Isenabled = :isEnabled"
							+ " where " + prefix + "Id = :typeId";
				int updatedEntities = session.createQuery(hqlUpdate)
									.setString("typeName",typeNames[i])
									.setString("isEnabled", "0")
									.setString("typeId", typeIds[i]).executeUpdate();
			}
		}
		tx.commit();
		session.close();
	}
	 /**
	  * 
	  * 修改类别(重载) <br>
	  * create_date: Aug 23, 2010,3:53:11 PM <br>
	  * @param tableName 表名
	  * @param prefix	表前缀名
	  * @param typeNames 类别名数组
	  * @param typeIds	类别id数组
	  * @param enabledIds 是否启用多选框值的数组
	  * @param type 类别类型标识<br>
	  */
	public void modType(String tableName,String prefix,String[] typeNames,String[] typeIds,String[] enabledIds,String type){
		Session session = super.getSession();
		Transaction tx = session.beginTransaction();

		for (int i = 0; i < typeIds.length; i++) {
			int mark = 0;//标识是否已启用
			if (enabledIds != null) {
				for (int j = 0; j < enabledIds.length; j++) {
					if (typeIds[i].equals(enabledIds[j])) {
						mark = 1;
						break;
					}
				}
			}
			if (mark == 1) {
				String hqlUpdate = "update " + tableName
							+ " set " + prefix + "Name = :typeName,"
							+ prefix + "Isenabled = :isEnabled"
							+ " where " + prefix + "Id = :typeId"
							+" and "+ prefix +"Type= :type";
				int updatedEntities = session.createQuery(hqlUpdate)
									.setString("typeName", typeNames[i])
									.setString("isEnabled", "1")
									.setString("typeId", typeIds[i])
									.setString("type", type).executeUpdate();
			} else {
				String hqlUpdate = "update "+tableName
							+ " set " + prefix + "Name = :typeName,"
							+ prefix + "Isenabled = :isEnabled"
							+ " where " + prefix + "Id = :typeId"
							+" and "+ prefix +"Type= :type";
				int updatedEntities = session.createQuery(hqlUpdate)
									.setString("typeName",typeNames[i])
									.setString("isEnabled", "0")
									.setString("typeId", typeIds[i])
									.setString("type", type).executeUpdate();
			}
		}
		tx.commit();
		session.close();
	}
}
