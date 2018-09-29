package oa.spring.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.FinanceMapper;
import oa.spring.util.OpenAccountUtil;
import oa.spring.util.StaticData;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class FinanceService {
	private FinanceMapper financeMapper;
	
	public FinanceMapper getFinanceMapper() {
		return financeMapper;
	}

	public void setFinanceMapper(FinanceMapper financeMapper) {
		this.financeMapper = financeMapper;
	}

	public String yhPageDataJson(Connection dbConn,Map map,String sql){
		try {
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String,Object> getCusMsg(String order_id){
		return financeMapper.getCusMsg(order_id);
	}
	public Map<String,Object> getOut(String fId){
		return financeMapper.getOut(fId);
	}
	public Map<String,Object> getFinanceInMsg(String fi_id){
		return financeMapper.getFinanceInMsg(fi_id);
	}
	
	public Map<String,Object> getPPDMsg(String fi_id){
		return financeMapper.getPPDMsg(fi_id);
	}
	public Map<String,Object> getFIMsg(String fi_id){
		return financeMapper.getFIMsg(fi_id);
	}
	public List<Map<String,Object>> getBankMsg(){
		return financeMapper.getBankMsg();
	}
	public Map<String,Object> getFIDMsg(String fid_id){
		return financeMapper.getFIDMsg(fid_id);
	}
	public Map<String,Object> getOutDetail(String fdId){
		return financeMapper.getOutDetail(fdId);
	}
	
	public void addPPD(Map<String,Object> map){
		financeMapper.addPPD(map);
	}
	public void addOut(Map<String,Object> map){
		financeMapper.addOut(map);
	}
	public void addOutDetail(Map<String,Object> map){
		financeMapper.addOutDetail(map);
	}
	public void addFinanceIn(Map map){
		financeMapper.addFinanceIn(map);
	}
	public void addFID(Map map){
		financeMapper.addFID(map);
	}
	public void updateFinanceIn(Map map){
		financeMapper.updateFinanceIn(map);
	}
	public void updatePPD(Map map){
		financeMapper.updatePPD(map);
	}
	public void updateFID(Map map){
		financeMapper.updateFID(map);
	}
	public void updateOut(Map map){
		financeMapper.updateOut(map);
	}
	public void updateOutDetail(Map map){
		financeMapper.updateOutDetail(map);
	}
	public void deletePPD(String ppd_id){
		financeMapper.deletePPD(ppd_id);
	}
	public void deleteFID(String fid_id){
		financeMapper.deleteFID(fid_id);
	}
	public void deleteOutDetail(String fdId){
		financeMapper.deleteOutDetails(fdId);
	}
	public void deleteOut(String fId){
		financeMapper.deleteOut(fId);
		financeMapper.deleteOutDetail(fId);
	}
	public void deleteBank(String bankId){
		financeMapper.deleteBank(bankId);
	}
	public void addBank(Map map){
		financeMapper.addBank(map);
	}
	public void updateBank(Map map){
		financeMapper.updateBank(map);
	}
	public List<Map<String, Object>> getBankById(String bankId){
		return financeMapper.getBankById(bankId);
	}
	
	public void openAccount(String fi_id){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fi_id", fi_id);
		map.put("status", StaticData.RUNNING);
		financeMapper.openAccount(map);
	}
	public void openAccountFinanceOut(String fId){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", fId);
		map.put("status", StaticData.RUNNING);
		financeMapper.openAccountFinanceOut(map);
	}
	
	public void openAccountFID(String fid_id,Connection dbConn) throws Exception{
		dbConn.setAutoCommit(false);
		Statement st = null;
		try {
			// 更新财务应收明细的状态为“已完成”
			st = dbConn.createStatement();
			String sql = "update erp_finance_in_detial set status = '"+ StaticData.OVER + "' where id='" + fid_id + "'";
			st.executeUpdate(sql);
			
			Map<String, String> map = OpenAccountUtil.getFI(dbConn, fid_id);
			if(map != null){
				String fi_id = map.get("fi_id");
				String order_id = map.get("type_id");
				String pp_id = OpenAccountUtil.getPaidPlanId(dbConn,order_id);
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
	}
	public void openAccountFinanceOutDetial(String fid_id,Connection dbConn) throws Exception{
		dbConn.setAutoCommit(false);
		String outDId = fid_id;
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
				String purId = getPurById(dbConn, foutId);
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
					String status = getPoutById(dbConn, purId);
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
