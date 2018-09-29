package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import oa.spring.util.StaticData;
import yh.core.funcs.workflow.util.YHIWFHookPlugin;

public class YHFinanceOutDetailApply implements YHIWFHookPlugin {

	/*
	 * 审核通过修改应付单明细 (non-Javadoc)
	 * 
	 * @see yh.core.funcs.workflow.util.YHIWFHookPlugin#execute(java.sql.Connection,
	 *      int, java.util.Map, java.util.Map, boolean)
	 */
	@Override
	public String execute(Connection dbConn, int runId, Map arrayHandler,
			Map formData, boolean agree) throws Exception {

		dbConn.setAutoCommit(false);
		String outDId = (String) arrayHandler.get("KEY");// 订单id
		Statement st = null;

		try {
			st = dbConn.createStatement();
			String sql2 = "update erp_finance_out_detial set status = '"
				+ StaticData.OVER + "' where id = '" + outDId + "'";
		st.executeUpdate(sql2);	
			Map<String, String> map = getFI(dbConn, outDId);
			if (map != null) {
				String foutId = map.get("foutId");
				String bank_id = map.get("bank_id");
				double total = Double.parseDouble(map.get("total"));
				double already_total = Double.parseDouble(map
						.get("already_total"));
				double money = Double.parseDouble(map.get("money"));
				String purId = this.getPurById(dbConn, foutId);
				if(total < already_total + money){
					throw new Exception("付款金额大于应付金额！");
				}
				else if (total == already_total + money) {
					// 如果应收的金额已完成，则修改erp_finance_out的状态为“已完成”
					String sql1 = "update erp_finance_out set status = '"
							+ StaticData.OVER + "',already_paid_total='"
							+ (already_total + money) + "' where id='" + foutId
							+ "'";
					st.executeUpdate(sql1);
					String sql3 = "update erp_purchase_paid_plan set status = '"
							+ StaticData.OVER
							+ "', already_total='"
							+ (already_total + money)
							+ "' where purchase_id='"
							+ purId + "'";
					st.executeUpdate(sql3);
					String status = this.getPoutById(dbConn, purId);
					if (status != null) {
						if (StaticData.OVER.equals(status)) {
							String sql5 = "update erp_purchase set status='"
									+ StaticData.OVER + "' where id='" + purId
									+ "'";
							st.executeUpdate(sql5);

						}
					}

				} else {
					String sql1 = "update erp_finance_out set already_paid_total='"
							+ (already_total + money)
							+ "' where id='"
							+ foutId
							+ "'";
					st.executeUpdate(sql1);
					String sql3 = "update erp_purchase_paid_plan set already_total='"
							+ (already_total + money)
							+ "' where purchase_id='"
							+ purId + "'";
					st.executeUpdate(sql3);
				}
				Double bankMoney = Double.parseDouble(this.getCompanyById(
						dbConn, bank_id));
				// 在现金银行中添加金额
				if (bankMoney < money) {
					throw new Exception("出款金额大于本现金银行金额！");

				} else {
					String sql3 = "update erp_company_bank set money = money-'"
							+ money + "' where id = '" + bank_id + "'";
					st.executeUpdate(sql3);
				}
			}
			
			dbConn.commit();
		} catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
	}

	private String getCompanyById(Connection dbConn, String bankId)
			throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = "SELECT money FROM erp_company_bank WHERE id='"
					+ bankId + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString("money");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private String getPurById(Connection dbConn, String foutId)
			throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = "SELECT type_id FROM erp_finance_out WHERE id='"
					+ foutId + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString("type_id");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private String getPoutById(Connection dbConn, String purId)
			throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = "SELECT ppo_status FROM erp_purchase_product_out WHERE purchase_id='"
					+ purId + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString("ppo_status");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private Map<String, String> getFI(Connection dbConn, String fdId)
			throws Exception {
		Map<String, String> map = null;
		try {
			Statement st = dbConn.createStatement();
			String sql = "SELECT fout.id AS foutId,fout.already_paid_total,fout.paid_total,fod.money,fod.bank_id FROM erp_finance_out  fout LEFT JOIN erp_finance_out_detial fod ON fod.fo_id=fout.id where fod.id='"
					+ fdId + "'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				map = new HashMap<String, String>();
				map.put("foutId", rs.getString("foutId"));
				map.put("total", rs.getString("paid_total"));
				map.put("already_total", rs.getString("already_paid_total"));
				map.put("money", rs.getString("money"));
				map.put("bank_id", rs.getString("bank_id"));
			}
		} catch (Exception e) {
			throw e;
		}
		return map;
	}
}
