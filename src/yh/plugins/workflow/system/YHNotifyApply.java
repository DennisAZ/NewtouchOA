package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

import oa.spring.util.StaticData;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;

public class YHNotifyApply implements YHIWFHookPlugin {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * yh.core.funcs.workflow.util.YHIWFHookPlugin#execute(java.sql.Connection,
	 * int, java.util.Map, java.util.Map, boolean)
	 */
	@Override
	public String execute(Connection dbConn, int runId, Map arrayHandler,
			Map formData, boolean agree) throws Exception {

		dbConn.setAutoCommit(false);
		String notifyId = (String) arrayHandler.get("KEY");// 生产通知单id
		Statement st = dbConn.createStatement();
		// 1.将生产通知单的状态为“over”
		try {
			String sql = "update erp_produce_notify set status = '"+ StaticData.RUNNING + "' where id='"+ notifyId + "'";
			st.executeUpdate(sql);
			dbConn.commit();
		} catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
	}
}
