package com.psit.struts.util.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BatchOperateDAO extends HibernateDaoSupport {

	/**
	 * 更新实体删除状态 <br>
	 * @param entityName 对应实体名
	 * @param priKeyMarker 对应表的主键标识
	 * @param code 要删除的主键值
	 * @param delMarker 对应实体的删除标识 <br>
	 */
	private void batchUpdate(String entityName, String priKeyMarker,
			String code, String delMarker, Integer z) {
		Session session = super.getSession();
		Transaction ts = session.beginTransaction();
		try {
			String sql = "";
			if (entityName.equals("RMessLim")) {
				sql = "delete from " + entityName + " where " + priKeyMarker
						+ "=" + code;
			} else {
				sql = "update " + entityName + " set " + delMarker + "=" + z
						+ " where " + priKeyMarker + "=" + code;
			}
			session.createQuery(sql).executeUpdate();

			ts.commit();
			session.close();

		} catch (Exception e) {
			ts.rollback();
			session.close();
		}

	}

	/**
	 * 批量更新 <br>
	 * @param entityName 对应实体名
	 * @param priKeyMarker 对应表的主键标识
	 * @param code 要删除的主键值
	 * @param delMarker 对应实体的删除标识 <br>
	 */
	public void batchUpdDelMark(String entityName, String priKeyMarker,
			String[] code, String delMarker, Integer z) {
		for (int i = 0; i < code.length; i++) {
			if (code[i] != null) {
				batchUpdate(entityName, priKeyMarker, code[i], delMarker, z);
			}
		}
	}

	/**
	 * 批量彻底删除<br>
	 * @param type 删除类型
	 * @param code  主键Ids<br>
	 */
	public void recBatchDel(String entityName,String priKeyMarker, String ids){
		Session session = super.getSession();
		Transaction ts = session.beginTransaction();
		if(ids.lastIndexOf(",")==(ids.length()-1)){
			ids = ids.substring(0, ids.length()-1);
		}
		try {
			String sql = "delete from "+entityName+" where "+priKeyMarker+" in("+ids+")";
			session.createQuery(sql).executeUpdate();
			ts.commit();
			session.close();
		} catch (Exception e) {
			ts.rollback();
			session.close();
		}
	}
}
