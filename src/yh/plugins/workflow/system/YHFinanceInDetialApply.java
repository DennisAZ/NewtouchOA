package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import oa.spring.util.OpenAccountUtil;
import oa.spring.util.StaticData;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;

public class YHFinanceInDetialApply implements YHIWFHookPlugin {

	/*
	 * 审核通过修改财务应收明细状态为”已完成“ (non-Javadoc)
	 * 
	 * @see
	 * yh.core.funcs.workflow.util.YHIWFHookPlugin#execute(java.sql.Connection,
	 * int, java.util.Map, java.util.Map, boolean)
	 */
	@Override
	public String execute(Connection dbConn, int runId, Map arrayHandler,
			Map formData, boolean agree) throws Exception {
		dbConn.setAutoCommit(false);
		String fid_id = (String) arrayHandler.get("KEY");// 财务财务应收明细id
		Statement st = null;
		try {
			// 更新财务应收明细的状态为“已完成”
			st = dbConn.createStatement();
			String sql = "update erp_finance_in_detial set status = '"+ StaticData.OVER + "' where id='" + fid_id + "'";
			st.executeUpdate(sql);
			
			Map<String, String> map = getFI(dbConn, fid_id);
			if(map != null){
				String fi_id = map.get("fi_id");
				String order_id = map.get("type_id");
				String pp_id = getPaidPlanId(dbConn,order_id);
				String bank_id = map.get("bank_id");
				double total = Double.parseDouble(map.get("total"));
				double already_total = Double.parseDouble(map.get("already_total"));
				double money = Double.parseDouble(map.get("money"));
				if(total <= already_total + money){
					//如果应收的金额已完成，则修改erp_finance_in的状态为“已完成”
					String sql1 = "update erp_finance_in set status = '"+StaticData.OVER+"',already_total='"+(already_total + money)+"' where id='"+fi_id+"'";
					st.executeUpdate(sql1);
					//同步erp_paid_plan的状态
					if(pp_id != null){
						String sql4 = "update erp_paid_plan set paid_status='"+StaticData.OVER+"',already_total='"+(already_total + money)+"' where id='"+pp_id+"'";
						st.executeUpdate(sql4);
						//查看对应order_id的出货单的状态，如果是已完成，则修改订单的状态为已完成
						if(OpenAccountUtil.checkPOStatus(dbConn, order_id)){
							String sql2 = "update erp_sale_order set order_status='"+StaticData.OVER+"' where id='"+order_id+"'";
							st.executeUpdate(sql2);
						}
					}
				}
				else{
					String sql1 = "update erp_finance_in set already_total='"+(already_total + money)+"' where id='"+fi_id+"'";
					st.executeUpdate(sql1);
					if(pp_id != null){
						String sql4 = "update erp_paid_plan set already_total='"+(already_total + money)+"' where id='"+pp_id+"'";
						st.executeUpdate(sql4);
					}
				}
				
				//在现金银行中添加金额
				String sql3 = "update erp_company_bank set money = money+'"+money+"' where id = '"+bank_id+"'";
				st.executeUpdate(sql3);
				dbConn.commit();
				
			}
			
			dbConn.commit();
		} catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
	}

	private Map<String, String> getFI(Connection dbConn, String fid_id)	throws Exception {
		Map<String, String> map = null;
		try {
			Statement st = dbConn.createStatement();
			String sql = "SELECT fi.id,fi.type_id,fi.total,fi.already_total,fid.money,fid.bank_id FROM erp_finance_in fi,erp_finance_in_detial fid WHERE fid.fi_id = fi.id AND fid.id = '"+fid_id+"'";
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				map = new HashMap<String,String>();
				map.put("fi_id", rs.getString("id"));
				map.put("type_id", rs.getString("type_id"));
				map.put("total", rs.getString("total"));
				map.put("already_total", rs.getString("already_total"));
				map.put("money", rs.getString("money"));
				map.put("bank_id", rs.getString("bank_id"));
			}
		} catch (Exception e) {
			throw e;
		}
		return map;
	}
	
	private String getPaidPlanId(Connection dbConn, String type_id)throws Exception {
		String pp_id = null;
		try{
			if(!"0".equals(type_id)){
				String sql = "select id from erp_paid_plan where order_id='"+type_id+"'";
				Statement st = dbConn.createStatement();
				ResultSet rs = st.executeQuery(sql);
				while(rs.next()){
					pp_id = rs.getString("id");
				}
			}
		}
		catch (Exception e) {
			throw e;
		}
		
		return pp_id;
	}
	
	/**
	 * 判断该订单对应的货单的状态是不是“已完成”
	 * @param dbConn
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	private  boolean checkPOStatus(Connection dbConn,String order_id) throws Exception{
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
