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

public class YHPurReturnApply implements YHIWFHookPlugin {

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
		String pid_id = (String) arrayHandler.get("KEY");// 仓库收货明细单id
		Statement st = dbConn.createStatement();
		// 1.将仓库发货单的状态为“客户验收不通过，有退货”
		try {
			String sql = "update erp_purchase_product_in_detail set status = '"+ StaticData.CUSTOMER_NO_AGREE + "' where id='"+ pid_id + "'";
			st.executeUpdate(sql);
			updatePP(dbConn, pid_id);
			dbConn.commit();
		} catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
	}

	public void updatePP(Connection dbConn, String pid_id) throws Exception {
		List<Map<String, Object>> ppList = getPP(dbConn, pid_id);
		String return_id = getReturnIdByPodId(dbConn,pid_id);//退货单id
		
		Statement st = dbConn.createStatement();
		try {
			if (ppList != null) {
				for (int i = 0; i < ppList.size(); i++) {
					String pproId = (String) ppList.get(i).get("pproId");//采购货单-产品关联id
					String pro_id = (String) ppList.get(i).get("pro_id");//产品缓存主键id
					String proId=this.getProductById(dbConn, pro_id);//产品id
					
					int purchase_num = Integer.parseInt(ppList.get(i).get("purchase_num").toString());//采购单总数
					int already_purchase_num = Integer.parseInt(ppList.get(i).get("already_purchase_num").toString());
					String number = getPODNum(dbConn, pid_id,pro_id);//本次仓库收货明细单的该产品的数量
					
					
					if (number != null) {
						int pponum= Integer.parseInt(number);
						int return_num = getReturnNum(dbConn,return_id,proId);
						if(return_num == 0){
							//该产品没有退货，这时需要比较该产品的订单数量和本次收货数量，如果相等，更新状态为”已完成“
							if(purchase_num == pponum){
								String sql = "update erp_ppo_pro set status='"+ StaticData.OVER+ "' where id='"+ pproId+ "'";
								st.executeUpdate(sql);
							}
						}
						String sql = "update erp_ppo_pro set already_purchase_num = '"+(already_purchase_num+pponum-return_num)+"' where id='"+ pproId+ "'";
						st.executeUpdate(sql);
						
						//库存数量修改
						 //将收货明细日志的状态改为完成
						String sqls = "update erp_db_log set status= '"+ StaticData.OVER + "' where pod_id='"+ pid_id + "' and pro_id='"+proId+"'";
						st.executeUpdate(sqls);
						//由于收货时人入的库可能和退货时出的库不一样，应该模拟下，先入库再出库操作，
						//比如产品A入库时选择一号库，出库则选择了二号库。
						List<Map<String, Object>> dbLogList = this.getDbLogById(dbConn, pid_id,proId);
						for (int k = 0; k < dbLogList.size(); k++) {
							String whId = (String) dbLogList.get(k).get("whId");
							String price = (String) dbLogList.get(k).get("price");
							String num = (String) dbLogList.get(k).get("num");
							String remark = (String) dbLogList.get(k).get("remark");
							Map<String, Object> dbMap = this.getDbById(dbConn, proId,whId);
							String dbId=UUID.randomUUID().toString();
							if (dbMap == null || "".equals(dbMap)) {
								price = "0-" + price;
								String sql4 = "insert into erp_db(id,wh_id,pro_id,price,num,remark) values('"+dbId+"','"
										+ whId
										+ "','"
										+ proId
										+ "','"
										+ price
										+ "'," + num + ",'" + remark + "')";
								st.executeUpdate(sql4);

							} else {
								String strPrice = (String) dbMap.get("price");
								String dbNum = (String) dbMap.get("num");
								Integer nums=Integer.parseInt(num);
								nums+= Integer.parseInt(dbNum);
								String[] arrPrice = strPrice.split("-");
								if (Double.parseDouble(arrPrice[0]) > Double.parseDouble(price)) {
									strPrice = Double.parseDouble(price)+ "-"+ Double.parseDouble(arrPrice[1]);
								} else if (Double.parseDouble(arrPrice[1]) < Double.parseDouble(price)) {
									strPrice = Double.parseDouble(arrPrice[0])+ "-"+ Double.parseDouble(price);
								}
								String sql4 = "update erp_db set price= '"
										+ strPrice + "',num='" + nums
										+ "' where pro_id='" + proId + "' and wh_id='"+whId+"'";
								st.executeUpdate(sql4);
							}
						}
						
						//将退货日志的状态改为完成
						sqls = "update erp_db_log set status= '"+ StaticData.OVER + "' where pod_id='"+ return_id + "' and pro_id='"+proId+"'";
						st.executeUpdate(sqls);
						
						List<Map<String, Object>> dbLogList1 = this.getDbLogById(dbConn, return_id,proId);
						for (int k = 0; k < dbLogList1.size(); k++) {
							String whId = (String) dbLogList1.get(k).get("whId");
							String num = (String) dbLogList1.get(k).get("num");
							
							Map<String, Object> dbMap = this.getDbById(dbConn, proId,whId);
							String dbNum = (String) dbMap.get("num");
							int dbnum = Integer.parseInt(dbNum);
							Integer nums=Integer.parseInt(num);
							if(dbnum < nums){
								throw new Exception("有产品的退货数量超过库存数量");
							}
							nums =  dbnum - nums;
							String sql4 = "update erp_db set num='" + nums
									+ "' where pro_id='" + proId + "' and wh_id='"+whId+"'";
							st.executeUpdate(sql4);
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	
	private String getReturnIdByPodId(Connection dbConn, String pid_id) throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = "SELECT id FROM erp_return WHERE type_id='"+ pid_id + "'";
//			String sql = "SELECT id FROM erp_return WHERE type_id='"+ pod_id + "' and type='销售'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString("id");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	private String getProductById(Connection dbConn, String proId)
	throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = "SELECT pro_id FROM erp_cache_product WHERE id='"
				+ proId + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString("pro_id");
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
			String sql = "SELECT wh_id,pro_id,price,number,remark FROM erp_db_log WHERE pod_id='"
					+ podId + "' and pro_id='"+proId+"'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("whId", rs.getString("wh_id"));
				map.put("proId", rs.getString("pro_id"));
				map.put("price", rs.getString("price"));
				map.put("num", rs.getString("number"));
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
	private int getReturnNum(Connection dbConn,String returnId,String proId) throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = "SELECT SUM(num) as num FROM erp_return_pro WHERE return_id = '"+returnId+"'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getInt("num");
			}
		} catch (Exception e) {
			throw e;
		}
		return 0;
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
