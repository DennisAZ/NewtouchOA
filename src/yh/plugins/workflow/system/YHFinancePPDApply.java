package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oa.spring.util.StaticData;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;

public class YHFinancePPDApply implements YHIWFHookPlugin{

  /* 
   * 审核通过修改财务回款明细状态为”已完成“
   * (non-Javadoc)
 * @see yh.core.funcs.workflow.util.YHIWFHookPlugin#execute(java.sql.Connection, int, java.util.Map, java.util.Map, boolean)
 */
@Override
  public String execute(Connection dbConn, int runId, Map arrayHandler, Map formData, boolean agree) throws Exception {
	  	
		dbConn.setAutoCommit(false);
		String ppd_id = (String) arrayHandler.get("KEY");//财务回款明细单id
		String order_id = (String) arrayHandler.get("ORDER_ID");
		Statement st  = null;
		
		try{
			st = dbConn.createStatement();
			//更新回款单的状态为“已完成”
			String sql = "update erp_finance_in set status = '"+StaticData.OVER+"' where id='"+ppd_id+"'";
			st.executeUpdate(sql);
			
			Map<String,String> map1 = getPPDTotal(dbConn, ppd_id);
			if(map1 != null){
				
				double total = Double.parseDouble(map1.get("total"));  //本次回款的金额
				String bank_id = map1.get("bank_id");
				
				Map<String,Object> map = getTotal(dbConn, order_id);
				if(map != null){
					double order_Total = Double.parseDouble((String) map.get("total"));//订单应收总金额
					double order_Already_Total = Double.parseDouble((String) map.get("already_total"));//订单已收总金额
					if(order_Total <= order_Already_Total + total){
						//如果应收的金额已完成，则修改erp_paid_plan的状态为“已完成”
						String sql1 = "update erp_paid_plan set paid_status = '"+StaticData.OVER+"',already_total='"+(order_Already_Total + total)+"' where order_id='"+order_id+"'";
						st.executeUpdate(sql1);
						//判断该订单对应的货单的状态是不是“已完成”,如果是，则修改erp_sale_order的状态
						if(checkPOStatus(dbConn, order_id)){
							String sql2 = "update erp_sale_order set order_status='"+StaticData.OVER+"' where id='"+order_id+"'";
							st.executeUpdate(sql2);
						}
					}
					else{
						order_Already_Total += total;
						String sql1 = "update erp_paid_plan set already_total = '"+order_Already_Total+"' where order_id='"+order_id+"'";
						st.executeUpdate(sql1);
					}
					
				}
				//在公司银行中添加金额total
				String sql3 = "update erp_company_bank set money = money+'"+total+"' where id = '"+bank_id+"'";
				st.executeUpdate(sql3);
				dbConn.commit();
			}
			
		}
		catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
  }
	public Map<String,Object> getTotal(Connection dbConn,String order_id) throws Exception{
		Map<String,Object> map = null;
		try{
			Statement st = dbConn.createStatement();
			String sql = "select total,already_total from erp_paid_plan where order_id='"+order_id+"'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				map = new HashMap<String,Object>();
				map.put("total", rs.getString("total"));
				map.put("already_total", rs.getString("already_total"));
			}
		}catch (Exception e) {
			throw e;
		}
		return map;
	}
	public Map<String,String> getPPDTotal(Connection dbConn,String ppd_id) throws Exception{
		Map<String,String> map = null;
		try{
			Statement st = dbConn.createStatement();
			String sql = "select bank_id,total from erp_finance_in where id='"+ppd_id+"'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				map = new HashMap<String,String>();
				map.put("bank_id", rs.getString("bank_id"));
				map.put("total", rs.getString("total"));
			}
		}catch (Exception e) {
			throw e;
		}
		return map;
	}
	/**
	 * 判断该订单对应的货单的状态是不是“已完成”
	 * @param dbConn
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	private boolean checkPOStatus(Connection dbConn,String order_id) throws Exception{
		try{
			Statement st = dbConn.createStatement();
			String sql = "select po_status from erp_order_product_out where order_id='"+order_id+"'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				if(!StaticData.OVER.equals(rs.getString("po_status"))){
					return false;
				}
			}
		}catch (Exception e) {
			throw e;
		}
		return true;
	}
}
