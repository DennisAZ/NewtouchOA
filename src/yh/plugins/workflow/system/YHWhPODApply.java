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

public class YHWhPODApply implements YHIWFHookPlugin{

  /* 
   * 审核通过修改仓库发货单状态为”已完成“
   * (non-Javadoc)
 * @see yh.core.funcs.workflow.util.YHIWFHookPlugin#execute(java.sql.Connection, int, java.util.Map, java.util.Map, boolean)
 */
@Override
  public String execute(Connection dbConn, int runId, Map arrayHandler, Map formData, boolean agree) throws Exception {
	  	
		dbConn.setAutoCommit(false);
		String pod_id = (String) arrayHandler.get("KEY");//仓库发货单id
		Statement st  = null;
		
		try{
			st = dbConn.createStatement();
			String sql = "update erp_order_product_out_detail set pod_status = '"+StaticData.OVER+"' where id='"+pod_id+"'";
			st.executeUpdate(sql);
			updatePP(dbConn, pod_id);
			dbConn.commit();
		}
		catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
		return null;
  }

	public void updatePP(Connection dbConn,String pod_id)throws Exception{
		List<Map<String,Object>> list = getMsg(dbConn, pod_id);
		Statement st  = dbConn.createStatement();
		try{
			if(list != null){
				for(int i=0;i<list.size();i++){
					String pp_id = (String) list.get(i).get("id");
					String po_id = (String) list.get(i).get("po_id");
					String order_num = list.get(i).get("order_num").toString();
					String already_send_num =  list.get(i).get("already_send_num").toString();
					String pod_num = (String) list.get(i).get("pod_num");
					
					if(pod_num != null && !"".equals(pod_num)){
						if(Double.parseDouble(order_num) == Double.parseDouble(already_send_num)){
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
						else if(Double.parseDouble(order_num) < DoubleCaulUtil.sum(already_send_num,pod_num)){
							throw new Exception("发货总数已经超出订单数量");
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
	/**
	 * 根据仓库发货单id查询各个产品的订单数量和已经发送的数量,已经本次发货数量
	 * @param dbConn
	 * @param pod_id
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> getMsg(Connection dbConn,String pod_id)throws Exception{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT pp.id,pp.po_id,pp.pro_id,pp.order_num,pp.already_send_num,podpro.pro_id,podpro.pod_num FROM erp_order_product_out_detail pod LEFT JOIN erp_pod_pro podpro ON pod.id=podpro.pod_id LEFT JOIN  erp_po_pro pp ON  pod.po_id = pp.po_id AND podpro.pro_id=pp.pro_id WHERE  pod.id='"+pod_id+"'";
			rs = st.executeQuery(sql);
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", rs.getString("id"));
				map.put("po_id", rs.getString("po_id"));
				map.put("pro_id", rs.getString("pro_id"));
				map.put("order_num", rs.getString("order_num"));
				map.put("already_send_num", rs.getString("already_send_num"));
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
}
