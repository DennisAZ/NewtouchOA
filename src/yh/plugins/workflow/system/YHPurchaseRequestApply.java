package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

import oa.spring.util.StaticData;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;

public class YHPurchaseRequestApply implements YHIWFHookPlugin{

  /* 
   * 审核通过修改采购订单状态、出货单状态、回款单状态为”执行中“
   * (non-Javadoc)
 * @see yh.core.funcs.workflow.util.YHIWFHookPlugin#execute(java.sql.Connection, int, java.util.Map, java.util.Map, boolean)
 */
@Override
  public String execute(Connection dbConn, int runId, Map arrayHandler,
      Map formData, boolean agree) throws Exception {
	  	
		dbConn.setAutoCommit(false);
		String reqId = (String) arrayHandler.get("KEY");//订单id
		Statement st  = null;
		
		try{
			st = dbConn.createStatement();
			String sql = "update erp_purchase_request set status = '"+StaticData.OVER+"' where id='"+reqId+"'";
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
