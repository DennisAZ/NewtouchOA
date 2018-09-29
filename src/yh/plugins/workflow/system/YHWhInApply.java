package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import oa.spring.util.StaticData;
import yh.core.funcs.workflow.util.YHIWFHookPlugin;

public class YHWhInApply implements YHIWFHookPlugin {

	/*
	 * 审核通过修改仓库发货单状态为”已完成“ (non-Javadoc)
	 * 
	 * @see yh.core.funcs.workflow.util.YHIWFHookPlugin#execute(java.sql.Connection,
	 *      int, java.util.Map, java.util.Map, boolean)
	 */
	@Override
	public String execute(Connection dbConn, int runId, Map arrayHandler,
			Map formData, boolean agree) throws Exception {

		dbConn.setAutoCommit(false);
		String pid = (String) arrayHandler.get("PDEID");// 仓库发货单id
		Statement st = null;

		try {
			st = dbConn.createStatement();
			updatePP(dbConn, pid);
			String sql = "update erp_purchase_product_in_detail set status = '"
				+ StaticData.OVER + "' where id='" + pid + "'";
		st.executeUpdate(sql);
			dbConn.commit();
		} catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
	}

	public void updatePP(Connection dbConn, String pod_id) throws Exception {
		List<Map<String, Object>> ppList = getPP(dbConn, pod_id);

		Statement st = dbConn.createStatement();
		try {
			if (ppList != null) {
				for (int i = 0; i < ppList.size(); i++) {
					String pp_id = (String) ppList.get(i).get("ppidId");//收货明细id
					String pproId = (String) ppList.get(i).get("pproId");//采购货单-产品关联id
					String po_id = (String) ppList.get(i).get("ppo_id");//采购货单id
					String proId = (String) ppList.get(i).get("pro_id");//产品主键id
					String purId = (String) ppList.get(i).get("purId");//采购单id
					
					int purchase_num = Integer.parseInt(ppList.get(i).get("purchase_num").toString());//采购单总数
					int already_purchase_num = Integer.parseInt(ppList.get(i).get("already_purchase_num").toString());
					
					String number = getPODNum(dbConn, pp_id,proId);//本次采购该产品的数量
					if (number != null) {
						int pponum= Integer.parseInt(number);
						if (purchase_num <= already_purchase_num + pponum) {
							// 先更新erp_ppo_pro的状态
							String sql = "update erp_ppo_pro set already_purchase_num = '"
										+ purchase_num
										+ "',status='"
										+ StaticData.OVER
										+ "' where id='"
										+ pproId
										+ "'";
							st.executeUpdate(sql);
							
							// erp_ppo_pro的状态的变化将影响erp_purchase_product_out状态的变化，erp_purchase_product_out状态的变化又将影响erp_purchase状态的变化
							if (checkPOStatus(dbConn, pproId,po_id)) {
								// 如果对应po_id的erp_ppo_pro所有记录的状态都为“已完成”，修改erp_purchase_product_out的状态为“已完成”
								String sql1 = "update erp_purchase_product_out set ppo_status='"
										+ StaticData.OVER
										+ "' where id='"
										+ po_id + "'";
								st.executeUpdate(sql1);
								String status = this.getFinanceById(dbConn,purId);
								if (status != null) {
									// 对应订单的回款状态如果也是“已完成”，则修改erp_purchase的状态为“已完成”
									if (StaticData.OVER.equals(status)) {

										String sql2 = "update erp_purchase set status='"
												+ StaticData.OVER
												+ "' where id='" + purId + "'";
										st.executeUpdate(sql2);

									}
								}
							}
							
						} else {
							String sql = "update erp_ppo_pro set already_purchase_num = '"
									+ (already_purchase_num + pponum)
									+ "' where id='" + pproId + "'";
							st.executeUpdate(sql);
						}
						
						//库存数量修改
						String sqls = "update erp_db_log set status= '"+ StaticData.OVER + "' where pod_id='"+ pod_id + "' and pro_id='"+proId+"'";
						st.executeUpdate(sqls);
						List<Map<String, Object>> dbLogList = this.getDbLogById(dbConn, pod_id,proId);
						for (int k = 0; k < dbLogList.size(); k++) {
							String whId = (String) dbLogList.get(k).get("whId");
							String price = (String) dbLogList.get(k).get("price");
							String num = (String) dbLogList.get(k).get("num");
							String batch = (String) dbLogList.get(k).get("batch");
							String invalid_time = (String) dbLogList.get(k).get("invalid_time");
							String remark = (String) dbLogList.get(k).get("remark");
							Map<String, Object> dbMap = this.getDbById(dbConn, proId,whId);
							String dbId=UUID.randomUUID().toString();
							
								String sql4 = "insert into erp_db(id,wh_id,pro_id,price,num,batch,invalid_time,remark) values('"+dbId+"','"
										+ whId
										+ "','"
										+ proId
										+ "','"
										+ price
										+ "'," + num + ",'"+batch+"','"+invalid_time+"','" + remark + "')";
								st.executeUpdate(sql4);

						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private String getFinanceById(Connection dbConn, String purId)
			throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = "SELECT STATUS FROM erp_purchase_paid_plan WHERE purchase_id='"
					+ purId + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString("STATUS");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private List<Map<String, Object>> getDbLogById(Connection dbConn,String podId,String proId) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = "SELECT wh_id,pro_id,price,number,batch,invalid_time,remark FROM erp_db_log WHERE pod_id='"
					+ podId + "' and pro_id='"+proId+"'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("whId", rs.getString("wh_id"));
				map.put("proId", rs.getString("pro_id"));
				map.put("price", rs.getString("price"));
				map.put("num", rs.getString("number"));
				map.put("batch", rs.getString("batch"));
				map.put("invalid_time", rs.getString("invalid_time"));
				map.put("remark", rs.getString("remark"));
				list.add(map);
			}
			if (list.size() > 0) {
				return list;
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private Map<String, Object> getDbById(Connection dbConn, String proId,String whId)
			throws Exception {
		Statement st = null;
		ResultSet rs = null;
		//dsadsad
		Map<String, Object> map =null;
		
		try {
			st = dbConn.createStatement();
			String sql = "SELECT wh_id,pro_id,price,num,remark FROM erp_db WHERE pro_id='"
					+ proId + "' and wh_id='"+whId+"'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				map=new HashMap<String, Object>();
				map.put("whId", rs.getString("wh_id"));
				map.put("proId", rs.getString("pro_id"));
				map.put("price", rs.getString("price"));
				map.put("num", rs.getString("num"));
				map.put("remark", rs.getString("remark"));

			}
		} catch (Exception e) {
			throw e;
		}
		return map;
	}

	private boolean checkPOStatus(Connection dbConn, String pproId, String po_id)
			throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = "SELECT status from erp_ppo_pro where id <> '"+ pproId + "' and ppo_id='" + po_id + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				if (!StaticData.OVER.equals(rs.getString("status"))) {
					return false;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return true;
	}

	/**
	 * 根据仓库发货单id查询“销售发货单与产品关联表”中的各个产品的订单数量和已经发送的数量
	 * 
	 * @param dbConn
	 * @param pod_id
	 * @return
	 * @throws Exception
	 */
	private List<Map<String, Object>> getPP(Connection dbConn, String pod_id)
			throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = "SELECT  ppid.purchase_id, ppro.id as pproId,ppid.id AS ppidId,ppid.ppo_id AS ppoId,ppro.pro_id AS proId,ppro.purchase_num,ppro.already_purchase_num FROM erp_purchase_product_in_detail ppid LEFT JOIN erp_ppo_pro ppro ON ppro.ppo_id=ppid.ppo_id WHERE ppid.id='"
					+ pod_id + "' ";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("ppidId", rs.getString("ppidId"));
				map.put("purId", rs.getString("purchase_id"));
				map.put("pproId", rs.getString("pproId"));
				map.put("ppo_id", rs.getString("ppoId"));
				map.put("pro_id", rs.getString("proId"));
				map.put("purchase_num", rs.getString("purchase_num"));
				map.put("already_purchase_num", rs
						.getString("already_purchase_num"));
				list.add(map);
			}
			if (list.size() > 0) {
				return list;
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	/**
	 * 根据仓库收货单id查询查询该货单的各产品发送数量
	 * 
	 * @param dbConn
	 * @param pod_id
	 * @return
	 * @throws Exception
	 */
	private String getPODNum(Connection dbConn, String pp_id,
			String proId) throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = " SELECT SUM(purPro.pur_num) as pur_num FROM erp_pur_pro purPro WHERE purPro.pur_id = '"
					+ pp_id + "' AND purPro.pro_id = '" + proId + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString("pur_num");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

}
