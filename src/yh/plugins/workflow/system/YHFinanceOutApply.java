package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

import oa.spring.util.StaticData;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;

public class YHFinanceOutApply implements YHIWFHookPlugin{

  /* 
   * 审核通过修改财务应付单状态为”执行中“
   * (non-Javadoc)
 * @see yh.core.funcs.workflow.util.YHIWFHookPlugin#execute(java.sql.Connection, int, java.util.Map, java.util.Map, boolean)
 */
@Override
  public String execute(Connection dbConn, int runId, Map arrayHandler,
      Map formData, boolean agree) throws Exception {
	  	
		dbConn.setAutoCommit(false);
		String outId = (String) arrayHandler.get("KEY");//订单id
		Statement st  = null;
		
		try{
			st = dbConn.createStatement();
			String sql = "update erp_finance_out set status = '"+StaticData.RUNNING+"' where id='"+outId+"'";
			st.executeUpdate(sql);
			dbConn.commit();
		}
		catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
  }
}
