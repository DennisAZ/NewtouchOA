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

public class YHProduceExamApply implements YHIWFHookPlugin {

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
			String sql1 = "update erp_produce_plan set status='"+StaticData.CHECK_IN_STATUS+"' where id='"+pp_id+"'";
			st.executeUpdate(sql1);
			//更新状态为结束
			String sql = "update erp_produce_exam set status='"+StaticData.OVER+"' where pp_id='"+pp_id+"'";
			st.executeUpdate(sql);
			//更新库存
			updateWareHouse(dbConn,pp_id);
			//更新生产通知与产品关联表erp_produce_notify_product
			updateNotifyProduct(dbConn,pp_id);
			dbConn.commit();
		} catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
	}
	public void updateNotifyProduct(Connection dbConn,String pp_id)throws Exception{
		List<Map<String,Object>> examProList = getExamProList(dbConn,pp_id);
		String pn_id = getNotifyId(dbConn,pp_id); 
		Statement st  = dbConn.createStatement();
		try{
			if(examProList != null && examProList.size() > 0){
				for(int i=0;i<examProList.size();i++){
					Map<String,Object> map = examProList.get(i);
					String pro_id = map.get("pro_id").toString();
					double number = Double.parseDouble(map.get("qualified_number").toString());
					Map<String,String> map1 = getNotifyPro(dbConn,pn_id,pro_id); 
					double total_number = Double.parseDouble(map1.get("number"));
					double already_number = Double.parseDouble(map1.get("already_number"));
					String notify_pro_id = map1.get("id");//erp_produce_notify_product表的id
					if(total_number > already_number+number){
						//如果当前产品生产任务没有完成就累加已生产数量
						String sql="update erp_produce_notify_product set already_number='"+(already_number+number)+"' where id='"+notify_pro_id+"'";
						st.executeUpdate(sql);
					}
					else{
						//如果当前产品生产任务已完成，更新当前状态和已生产数量，并判断蝴蝶效应
						String sql="update erp_produce_notify_product set already_number='"+(already_number+number)+"' and status='"+StaticData.OVER+"' where id='"+notify_pro_id+"'";
						st.executeUpdate(sql);
						if(checkNotifyStatus(dbConn,pn_id,notify_pro_id)){
							//如果其他产品的生产任务都完成，则更新生产通知的状态为已完成
							String sql1 = "update erp_produce_notify set status='"+StaticData.OVER+"' where id='"+pn_id+"'";
							st.executeUpdate(sql1);
						}
					}
				}
			}
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	private boolean checkNotifyStatus(Connection dbConn,String pn_id,String notify_pro_id)throws Exception{
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT status from erp_produce_notify_product where id <> '"+notify_pro_id+"' and pn_id='"+pn_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				if(!StaticData.OVER.equals(rs.getString("status"))){
					return false;
				}
			}
		}
		catch (Exception e) {
			throw e;
		}
		return true;
	}
	public void updateWareHouse(Connection dbConn,String pp_id)throws Exception{
		List<Map<String,Object>> bomList = getExamList(dbConn,pp_id);
		
		Statement st  = dbConn.createStatement();
		try{
			if(bomList != null && bomList.size() > 0){
				for(int i=0;i<bomList.size();i++){
					    Map<String,Object> map = bomList.get(i);
						String sql2 = "update erp_db_log set status='"+StaticData.OVER+"' where id='"+map.get("id").toString()+"'";
						st.executeUpdate(sql2);
						String batch = map.get("batch").toString();
						String price = map.get("price").toString();
						String wh_id = map.get("wh_id").toString();
						String pro_id = map.get("pro_id").toString();
						String number = map.get("number").toString();
						String invalid_time = map.get("invalid_time").toString();
						String sql5= "insert into erp_db(id,wh_id,pro_id,price,num,batch,invalid_time) " +
								"values(uuid(),'"+wh_id+"','"+pro_id+"','"+price+"','"+number+"','"+batch+"','"+invalid_time+"')";
						st.executeUpdate(sql5);
					}
				}
		}
		catch (Exception e) {
			throw e;
		}
	}
	private List<Map<String,Object>> getExamList(Connection dbConn,String pp_id)throws Exception{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT db.id,db.batch,db.price,db.wh_id,db.pro_id,db.number,db.invalid_time FROM erp_produce_exam e LEFT JOIN erp_db_log db ON e.id = db.pod_id WHERE e.pp_id='"+pp_id+"' AND db.flag='"+StaticData.CHECK_IN+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", rs.getString("id"));
				map.put("batch", rs.getString("batch"));
				map.put("price", rs.getString("price"));
				map.put("wh_id", rs.getString("wh_id"));
				map.put("pro_id", rs.getString("pro_id"));
				map.put("number", rs.getString("number"));
				map.put("invalid_time", rs.getString("invalid_time"));
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
	private Map<String,String> getNotifyPro(Connection dbConn,String pn_id,String pro_id)throws Exception{
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "select id,number,already_number,status from erp_produce_notify_product where pn_id='"+pn_id+"' and pro_id='"+pro_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Map<String,String> map = new HashMap<String,String>();
				map.put("id", rs.getString("id"));
				map.put("number", rs.getString("number"));
				map.put("already_number", rs.getString("already_number"));
				map.put("status", rs.getString("status"));
				return map;
			}
		}
		catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	private List<Map<String,Object>> getExamProList(Connection dbConn,String pp_id)throws Exception{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT ep.pro_id,ep.qualified_number FROM erp_produce_exam e,erp_produce_exam_product ep WHERE e.id=ep.exam_id AND e.pp_id='"+pp_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("pro_id", rs.getString("pro_id"));
				map.put("qualified_number", rs.getString("qualified_number"));
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
	
	
	private String getNotifyId(Connection dbConn,String pp_id)throws Exception{
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "select pn_id from erp_produce_plan where id='"+pp_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				return rs.getString("pn_id");
			}
		}
		catch (Exception e) {
			throw e;
		}
		return null;
	}
}
