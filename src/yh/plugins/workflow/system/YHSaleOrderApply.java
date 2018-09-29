package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

import oa.spring.util.StaticData;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;

public class YHSaleOrderApply implements YHIWFHookPlugin{

  /* 
   * 审核通过修改销售订单状态、出货单状态、回款单状态为”执行中“
   * (non-Javadoc)
 * @see yh.core.funcs.workflow.util.YHIWFHookPlugin#execute(java.sql.Connection, int, java.util.Map, java.util.Map, boolean)
 */
@Override
  public String execute(Connection dbConn, int runId, Map arrayHandler,
      Map formData, boolean agree) throws Exception {
	  	
		dbConn.setAutoCommit(false);
		String order_id = (String) arrayHandler.get("KEY");//订单id
		Statement st  = null;
		
		try{
			st = dbConn.createStatement();
			String sql = "update erp_sale_order set order_status = '"+StaticData.RUNNING+"' where id='"+order_id+"'";
			String sql1 = "update erp_order_product_out set po_status = '"+StaticData.RUNNING+"' where order_id = '"+order_id+"'";
			String sql2 = "update erp_paid_plan set paid_status = '"+StaticData.RUNNING+"' where order_id = '"+order_id+"'";
			String sql3 = "update erp_finance_in set status = '"+StaticData.RUNNING+"' where type_id = '"+order_id+"'";
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
