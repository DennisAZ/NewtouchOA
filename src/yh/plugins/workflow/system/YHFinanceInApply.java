package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

import oa.spring.util.StaticData;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;

public class YHFinanceInApply implements YHIWFHookPlugin{

  /* 
   * 审核通过修改财务应收状态为”执行中“
   * (non-Javadoc)
 * @see yh.core.funcs.workflow.util.YHIWFHookPlugin#execute(java.sql.Connection, int, java.util.Map, java.util.Map, boolean)
 */
@Override
  public String execute(Connection dbConn, int runId, Map arrayHandler, Map formData, boolean agree) throws Exception {
	  	
		String fi_id = (String) arrayHandler.get("KEY");//财务财务应收单id
		Statement st  = null;
		
		try{
			st = dbConn.createStatement();
			//更新财务应收单的状态为“已完成”
			String sql = "update erp_finance_in set status = '"+StaticData.RUNNING+"' where id='"+fi_id+"'";
			st.executeUpdate(sql);
		}
		catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
  }
}
