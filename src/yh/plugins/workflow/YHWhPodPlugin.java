package yh.plugins.workflow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.util.DoubleCaulUtil;
import oa.spring.util.StaticData;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;

public class YHWhPodPlugin implements YHIWFPlugin{

	@Override
	public String after(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
		      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		      Connection dbConn = requestDbConn.getSysDbConn();
		      String flowIdStr = request.getParameter("flowId");
		      String runIdStr = request.getParameter("runId");
		    
		      int runId = Integer.parseInt(runIdStr);
		      int flowId = Integer.parseInt(flowIdStr);
		     
		      YHFlowRunUtility wf = new YHFlowRunUtility();
		    
		      String pod_id =  wf.getData(dbConn, flowId, runId, "发货单id");
		      dbConn.setAutoCommit(false);
				Statement st  = null;
				
				try{
					st = dbConn.createStatement();
					//修改当前发货单的状态为“已发货，客户还没验收”
					String sql = "update erp_order_product_out_detail set pod_status = '"+StaticData.ALREADY_SEND+"' where id='"+pod_id+"'";
					st.executeUpdate(sql);
					updatePP(dbConn, pod_id);
					dbConn.commit();
				}
				catch (Exception e) {
					dbConn.rollback();
					throw e;
				}
		      
		} catch(Exception ex) {
		      throw ex;
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
					String pro_id = (String) list.get(i).get("pro_id");
					String order_num = list.get(i).get("order_num").toString();
					String already_send_num =  list.get(i).get("already_send_num").toString();
					String pod_num = (String) list.get(i).get("pod_num");
					if(pod_num != null && !"".equals(pod_num)){
						if(Double.parseDouble(order_num) < DoubleCaulUtil.sum(already_send_num,pod_num)){
							throw new Exception("发货总数已经超出订单数量");
						}
						else {
							//这里只修改erp_po_pro的已发数量,在没有得到客户确认之前这里只体现出货量的多少，而不体现状态，状态等该工作流结束后由引擎自己来判断。
							String sql = "update erp_po_pro set already_send_num = '"+DoubleCaulUtil.sum(already_send_num,pod_num)+"' where id='"+pp_id+"'";
							st.executeUpdate(sql);
							//库存日志的状态设置为已完成，表示货已经出去（这个是实实在在发生的事情，所以已经结束）
							String sql2 = "update erp_db_log set status='"+StaticData.OVER+"' where pod_id='"+pod_id+"' and flag='"+StaticData.SALE+"'";
							st.executeUpdate(sql2);
							
							List<Map<String,Object>> dbLogList = getDb(dbConn, pod_id,pro_id);
							if(dbLogList != null){
								for(int k=0;k<dbLogList.size();k++){
									String batch = (String) dbLogList.get(k).get("batch");
									String wh_id = (String) dbLogList.get(k).get("wh_id");
									String num = dbLogList.get(k).get("number").toString();
									
									Map<String,String> map = getDbById(dbConn,batch,wh_id,pro_id);
									String  dbNum = map.get("dbNum").toString();
									if(Double.parseDouble(dbNum) < Double.parseDouble(num)){
										throw new Exception("发货总数已经超出库存数量");	
									}else{
										String sql5= "update erp_db set num='"+DoubleCaulUtil.sub(dbNum, num)+"' where pro_id='"+pro_id+"' and wh_id='"+wh_id+"' and batch='"+batch+"'";
										st.executeUpdate(sql5);
									}
								}
							}
						}
						
					}
				}
			}
		}
		catch (Exception e) {
			throw e;
		}
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

	private List<Map<String,Object>> getDb(Connection dbConn,String pod_id,String proId)throws Exception{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Statement st  = null;
		ResultSet rs = null;
		try{
			st = dbConn.createStatement();
			String sql = "SELECT batch,wh_id,number FROM erp_db_log WHERE pod_id= '"+pod_id+"' and flag='"+StaticData.SALE+"' and pro_id='"+proId+"'";
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

	@Override
	public String before(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}

}
