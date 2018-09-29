package yh.plugins.workflow.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import oa.spring.util.DoubleCaulUtil;
import oa.spring.util.StaticData;

import yh.core.funcs.workflow.util.YHIWFHookPlugin;

public class YHReturnApply implements YHIWFHookPlugin {

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
		String pod_id = (String) arrayHandler.get("KEY");// 仓库发货单id
		Statement st = dbConn.createStatement();
		// 1.将仓库发货单的状态为“客户验收不通过，有退货”
		try {
			String sql = "update erp_order_product_out_detail set pod_status = '"+ StaticData.CUSTOMER_NO_AGREE + "' where id='"+ pod_id + "'";
			st.executeUpdate(sql);
			updatePP(dbConn, pod_id);
			dbConn.commit();
		} catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
	}
	
	public void updatePP(Connection dbConn,String podId)throws Exception{
		List<Map<String,Object>> ppList = getPP(dbConn, podId);
		String return_id = getReturnIdByPodId(dbConn,podId);
		Statement st  = dbConn.createStatement();
		try{
			if(ppList != null){
				for(int i=0;i<ppList.size();i++){
					
					String pp_id = (String) ppList.get(i).get("id");
					String pro_id = (String) ppList.get(i).get("pro_id");
					String order_num = ppList.get(i).get("order_num").toString();
					String already_send_num =  ppList.get(i).get("already_send_num").toString();
					
					String number = getReturnNum(dbConn, return_id,pro_id);
					if(number != null && !"".equals(number)){
						String sql = "update erp_po_pro set status='"+StaticData.NEW_CREATE+"',already_send_num = '"+DoubleCaulUtil.sub(already_send_num,number)+"' where id='"+pp_id+"'";
						st.executeUpdate(sql);
						
						String sql2 = "update erp_db_log set status='"+StaticData.OVER+"' where pod_id='"+return_id+"' and flag='"+StaticData.SALE_RETURN+"' and pro_id='"+pro_id+"'";
						st.executeUpdate(sql2);
						
						List<Map<String,Object>> dbLogList=getDb(dbConn, return_id,pro_id);
						if(dbLogList != null){
							for(int k=0;k<dbLogList.size();k++){
								String batch = (String) dbLogList.get(k).get("batch");
								String wh_id = (String) dbLogList.get(k).get("wh_id");
								String num = dbLogList.get(k).get("number").toString();
								Map<String,String> map = getDbById(dbConn,batch,wh_id,pro_id);
								String  dbNum = map.get("dbNum").toString();
								String sql5= "update erp_db set num="+DoubleCaulUtil.sum(dbNum, num)+" where pro_id='"+pro_id+"' and wh_id='"+wh_id+"' and batch='"+batch+"'";
								st.executeUpdate(sql5);
							}
						}
					}
					else{
						//当前产品没有退货，这时需要比较该产品的订单数量和已发货数量，如果相等，更新状态为”已完成“
						if(Double.parseDouble(order_num) == Double.parseDouble(already_send_num)){
							String sql = "update erp_po_pro set status='"+StaticData.OVER+"' where id='"+pp_id+"'";
							st.executeUpdate(sql);
						}
					}
				}
			}
			
			//修改该退货单的状态为已完成
			String sql = "update erp_return set status = '"+StaticData.OVER+"' where id='"+return_id+"'";
			st.executeUpdate(sql);
		}
		catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 根据仓库发货单id查询“销售发货单与产品关联表”中的各个产品的订单数量和已经发送的数量
	 * @param dbConn
	 * @param pod_id
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> getPP(Connection dbConn,String pod_id)throws Exception{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT pp.id,pp.po_id,pp.pro_id,pp.order_num,pp.already_send_num FROM erp_order_product_out_detail pod,erp_po_pro pp WHERE pod.po_id = pp.po_id and pod.id='"+pod_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", rs.getString("id"));
				map.put("po_id", rs.getString("po_id"));
				map.put("pro_id", rs.getString("pro_id"));
				map.put("order_num", rs.getString("order_num"));
				map.put("already_send_num", rs.getString("already_send_num"));
				list.add(map);
			}
			if(list.size() > 0){
				return list;
			}
		}
		catch (Exception e) {
			throw e;
		}
		return null;
	}

	/**
	 * 查询该货单的各产品退货数量
	 * @param dbConn
	 * @param pod_id
	 * @return
	 * @throws Exception
	 */
	private String getReturnNum(Connection dbConn,String return_id,String pro_id)throws Exception{
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT num from erp_return_pro where return_id='"+return_id+"' and pro_id='"+pro_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				return rs.getString("num");
			}
		}
		catch (Exception e) {
			throw e;
		}
		return null;
	}
	private String getReturnIdByPodId(Connection dbConn, String pod_id) throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = "SELECT id FROM erp_return WHERE type_id='"+ pod_id + "' and type='销售'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString("id");
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	private Map<String,String> getDbById(Connection dbConn,String batch,String wh_id,String pro_id)throws Exception{
		Statement st  = null;
		ResultSet rs = null;
		Map<String,String> map =new HashMap<String,String>();
		try{
			st = dbConn.createStatement();
			String sql = "SELECT num FROM erp_db WHERE pro_id='"+pro_id+"' and wh_id='"+wh_id+"' and batch='"+batch+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				map.put("dbNum", rs.getString("num"));
			}
		}
		catch (Exception e) {
			throw e;
		}
		return map;
	}
	private List<Map<String,Object>> getDb(Connection dbConn,String return_id,String pro_id)throws Exception{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT batch,wh_id,number FROM erp_db_log WHERE pod_id= '"+return_id+"' and flag='"+StaticData.SALE_RETURN+"' and pro_id='"+pro_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("batch", rs.getString("batch"));
				map.put("wh_id", rs.getString("wh_id"));
				map.put("number", rs.getString("number"));
				list.add(map);
			}
			if(list.size() > 0){
				return list;
			}
		}
		catch (Exception e) {
			throw e;
		}
		return null;
	}

}
