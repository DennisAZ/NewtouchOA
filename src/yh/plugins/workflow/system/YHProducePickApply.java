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

public class YHProducePickApply implements YHIWFHookPlugin {

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
		String pp_id = (String) formData.get("生产计划id");// 生产计划id
		Statement st = dbConn.createStatement();
		try {
			//更新生产计划的状态
			String sql1 = "update erp_produce_plan set status='"+StaticData.PRODUCE_PICK_STATUS+"' where id='"+pp_id+"'";
			st.executeUpdate(sql1);
			//更新erp_produce_bom状态为结束
			String sql = "update erp_produce_bom set status='"+StaticData.OVER+"' where pp_id='"+pp_id+"' and bom_type='1'";
			st.executeUpdate(sql);
			//更新库存
			updateWareHouse(dbConn,pp_id);
			
			dbConn.commit();
		} catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
	}
	public void updateWareHouse(Connection dbConn,String pp_id)throws Exception{
		List<Map<String,Object>> bomList = getBOMList(dbConn,pp_id);
		
		Statement st  = dbConn.createStatement();
		try{
			if(bomList != null && bomList.size() > 0){
				for(int i=0;i<bomList.size();i++){
					    Map<String,Object> map = bomList.get(i);
						String sql2 = "update erp_db_log set status='"+StaticData.OVER+"' where id='"+map.get("id").toString()+"'";
						st.executeUpdate(sql2);
						double  dbNum = getDbById(dbConn, map.get("batch").toString(),map.get("pro_id").toString(), map.get("wh_id").toString());
						double num = dbNum-Double.parseDouble(map.get("number").toString());
						String sql5= "update erp_db set num="+num+" where batch='"+map.get("batch").toString()+"' and pro_id='"+map.get("pro_id").toString()+"' and wh_id='"+map.get("wh_id").toString()+"'";
						st.executeUpdate(sql5);
					}
				}
		}
		catch (Exception e) {
			throw e;
		}
	}
	private List<Map<String,Object>> getBOMList(Connection dbConn,String pp_id)throws Exception{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT db.id,db.batch,db.wh_id,db.pro_id,db.number FROM erp_produce_bom b LEFT JOIN erp_db_log db ON b.id =db.pod_id WHERE b.pp_id='"+pp_id+"' AND db.flag='"+StaticData.PRODUCE_PICK+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", rs.getString("id"));
				map.put("batch", rs.getString("batch"));
				map.put("wh_id", rs.getString("wh_id"));
				map.put("pro_id", rs.getString("pro_id"));
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
	
	
	private double getDbById(Connection dbConn,String batch,String proId,String whId)throws Exception{
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT num FROM erp_db WHERE batch='"+batch+"' and pro_id='"+proId+"' and wh_id='"+whId+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				return rs.getDouble("num");
			}
		}
		catch (Exception e) {
			throw e;
		}
		return 0.0;
	}

}
