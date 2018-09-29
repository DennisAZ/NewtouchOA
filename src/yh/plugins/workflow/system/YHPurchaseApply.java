package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

import oa.spring.util.StaticData;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;

public class YHPurchaseApply implements YHIWFHookPlugin{

  /* 
   * 审核通过修改采购订单状态、收货单状态、出款单状态为”执行中“
   * (non-Javadoc)
 * @see yh.core.funcs.workflow.util.YHIWFHookPlugin#execute(java.sql.Connection, int, java.util.Map, java.util.Map, boolean)
 */
@Override
  public String execute(Connection dbConn, int runId, Map arrayHandler,
      Map formData, boolean agree) throws Exception {
	  	
		dbConn.setAutoCommit(false);
		String purId = (String) arrayHandler.get("KEY");//订单id
		Statement st  = null;
		
		try{
			st = dbConn.createStatement();
			String sql = "update erp_purchase set status = '"+StaticData.RUNNING+"' where id='"+purId+"'";
			String sql1 = "update erp_purchase_product_out set ppo_status = '"+StaticData.RUNNING+"' where purchase_id = '"+purId+"'";
			String sql2 = "update erp_purchase_paid_plan set status = '"+StaticData.RUNNING+"' where purchase_id = '"+purId+"'";
			String sql3 = "update erp_finance_out set status = '"+StaticData.RUNNING+"' where type_id = '"+purId+"'";
			st.executeUpdate(sql);
			st.executeUpdate(sql1);
			st.executeUpdate(sql2);
			st.executeUpdate(sql3);
			dbConn.commit();
		}
		catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
  }
}
