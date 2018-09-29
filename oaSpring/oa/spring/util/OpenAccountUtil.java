package oa.spring.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenAccountUtil {
	public void updatePP(Connection dbConn,String pod_id)throws Exception{
		List<Map<String,Object>> ppList = getPP(dbConn, pod_id);
		List<Map<String,Object>> podList = getPOD(dbConn, pod_id);
		Statement st  = dbConn.createStatement();
		try{
			if(ppList != null && podList != null){
				for(int i=0;i<ppList.size();i++){
					String pp_id = (String) ppList.get(i).get("id");
					String po_id = (String) ppList.get(i).get("po_id");
					String pro_id = (String) ppList.get(i).get("pro_id");
					int order_num = Integer.parseInt(ppList.get(i).get("order_num").toString());
					int already_send_num =  Integer.parseInt(ppList.get(i).get("already_send_num").toString());
					String number = getPODNum(pro_id, podList);
					if(number != null){
						int pod_num = Integer.parseInt(number);
						if(order_num < already_send_num + pod_num){
							throw new Exception("发货总数已经超出订单数量");
						}
						else if(order_num == already_send_num + pod_num){
							//先更新erp_po_pro的状态
							String sql = "update erp_po_pro set already_send_num = '"+order_num+"',status='"+StaticData.OVER+"' where id='"+pp_id+"'";
							st.executeUpdate(sql);
							//erp_po_pro的状态的变化将影响erp_order_product_out状态的变化，erp_order_product_out状态的变化又将影响erp_sale_order状态的变化
							if(checkPOStatus(dbConn,po_id,pp_id)){
								//如果对应po_id的erp_po_pro所有记录的状态都为“已完成”，修改erp_order_product_out的状态为“已完成”
								String sql1 = "update erp_order_product_out set po_status='"+StaticData.OVER+"' where id='"+po_id+"'";
								st.executeUpdate(sql1);
								String order_id = getSaleOrderId(dbConn,po_id);
								//如果erp_order_product_out的状态为“已完成”，这时要更新回款单 erp_paid_plan中的销售支出金额
								String total = getOrderSalePaid(dbConn, po_id);
								total = total==null?"0":total;
								String sql3 = "update erp_paid_plan set sale_paid = sale_paid + '"+total+"' where order_id='"+order_id+"'";
								st.executeUpdate(sql3);
								
								//对应订单的回款状态如果也是“已完成”，则修改erp_sale_order的状态为“已完成”
								if(checkPaidPlanStatus(dbConn,order_id)){
									String sql2 = "update erp_sale_order set order_status='"+StaticData.OVER+"' where id='"+order_id+"'";
									st.executeUpdate(sql2);
								}
							}
						}
						else{
							String sql = "update erp_po_pro set already_send_num = '"+(already_send_num+pod_num)+"' where id='"+pp_id+"'";
							st.executeUpdate(sql);
						}
						
					}
				}
			}
		}
		catch (Exception e) {
			throw e;
		}
	}
	
	private String getOrderSalePaid(Connection dbConn,String po_id)throws Exception{
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT sum(total) as total FROM erp_order_product_out_detail WHERE po_id='"+po_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				return rs.getString("total");
			}
		}
		catch (Exception e) {
			throw e;
		}
		return null;
	}
	private boolean checkPaidPlanStatus(Connection dbConn,String order_id)throws Exception{
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT paid_status FROM erp_paid_plan WHERE order_id='"+order_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				if(!StaticData.OVER.equals(rs.getString("paid_status"))){
					return false;
				}
			}
		}
		catch (Exception e) {
			throw e;
		}
		return true;
	}
	private String getSaleOrderId(Connection dbConn,String po_id)throws Exception{
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT order_id FROM erp_order_product_out WHERE id='"+po_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				return rs.getString("order_id");
			}
		}
		catch (Exception e) {
			throw e;
		}
		return null;
	}
	private boolean checkPOStatus(Connection dbConn,String po_id,String pp_id)throws Exception{
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT status from erp_po_pro where id <> '"+pp_id+"' and po_id='"+po_id+"'";
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
	private String getPODNum(String pro_id,List<Map<String,Object>> podList){
		for(int i=0;i<podList.size();i++){
			if(pro_id.equals((String)podList.get(i).get("pro_id"))){
				return (String)podList.get(i).get("pod_num");
			}
		}
		return null;
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
			String sql = "SELECT pp.id,pp.po_id,pp.pro_id,pp.order_num,pp.already_send_num FROM erp_order_product_out_detail pod,erp_po_pro pp WHERE pod.po_id = pp.po_id and pod.id='"+pod_id+"' ";
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
	 * 根据仓库发货单id查询查询该货单的各产品发送数量
	 * @param dbConn
	 * @param pod_id
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> getPOD(Connection dbConn,String pod_id)throws Exception{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT podpro.pro_id, podpro.pod_num FROM erp_order_product_out_detail pod,erp_pod_pro podpro WHERE pod.id = podpro.pod_id AND pod.id = '"+pod_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("pro_id", rs.getString("pro_id"));
				map.put("pod_num", rs.getString("pod_num"));
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

	public static Map<String, String> getFI(Connection dbConn, String fid_id)	throws Exception {
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
	
	public static String getPaidPlanId(Connection dbConn, String type_id)throws Exception {
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
	public static boolean checkPOStatus(Connection dbConn,String order_id) throws Exception{
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
