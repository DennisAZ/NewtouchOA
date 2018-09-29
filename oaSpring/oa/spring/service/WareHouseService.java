package oa.spring.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.PurchaseMapper;
import oa.spring.dao.batis.WareHouseMapper;
import oa.spring.po.WareHouse;
import oa.spring.util.OpenAccountUtil;
import oa.spring.util.StaticData;
import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class WareHouseService {
	private WareHouseMapper whMapper;
	private PurchaseMapper purchaseMapper;

	public WareHouseMapper getWhMapper() {
		return whMapper;
	}

	public void setWhMapper(WareHouseMapper whMapper) {
		this.whMapper = whMapper;
	}

	public List<WareHouse> getWareHouse() {
		return whMapper.getWareHouse();
	}

	public PurchaseMapper getPurchaseMapper() {
		return purchaseMapper;
	}

	public void setPurchaseMapper(PurchaseMapper purchaseMapper) {
		this.purchaseMapper = purchaseMapper;
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
	public String getWhByProdId(Connection dbConn, String proId, Map map) {
		try {
			String sql = "SELECT db.id AS dbId,wh.id AS whId,pro.id AS proId,wh.name AS whName,ps.name AS psName,pro.pro_code,pro.pro_name,db.price,db.num"
					+ " FROM erp_db db LEFT JOIN erp_product pro ON pro.id=db.pro_id LEFT JOIN erp_warehouse wh ON wh.id=db.wh_id LEFT JOIN erp_product_style ps ON ps.id=wh.type where db.pro_id='"
					+ proId + "'";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getWhByProdBatchId(Connection dbConn, String proId, Map map) {
		try {
			String sql = "SELECT db.id AS dbId,wh.id AS whId,pro.id AS proId,wh.name AS whName,ps.name AS psName,pro.pro_code,pro.pro_name,db.price,db.num"
				+ " FROM erp_db db LEFT JOIN erp_product pro ON pro.id=db.pro_id LEFT JOIN erp_warehouse wh ON wh.id=db.wh_id LEFT JOIN erp_product_style ps ON ps.id=wh.type where db.pro_id='"
				+ proId + "'";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, Object> getPCode(String typeId, String flag) {
		if ("1".equals(flag)) {
			// 销售退货
			return whMapper.getPodCode(typeId);
		} else {
			return whMapper.getPidCode(typeId);
		}
	}
	public Map<String, Object> getPod(String pdId) {
			return whMapper.getPod(pdId);
	}
	public Map<String, Object> getBatch(Map<String,Object> map) {
		return whMapper.getBatch(map);
	}

	public Map<String, Object> checkData(String typeId, String flag) {
		if ("1".equals(flag)) {
			// 销售退货
			return whMapper.checkPODData(typeId);
		} else {
			return whMapper.checkPIDData(typeId);
		}
	}

	//退货管理信息
	public String returnManage(Connection dbConn, Map map,
			String status, String beginTime, String endTime) {
		try {
			String sql = "";
			if ("-2".equals(status)) {
				sql = "SELECT ret.id AS retId,TYPE,CODE,reason,STATUS FROM erp_return ret where  STATUS='"+StaticData.NEW_CREATE+"'";
			} else if ("-3".equals(status)) {
				sql = "SELECT ret.id AS retId,TYPE,CODE,reason,STATUS FROM erp_return ret where 1=1";
			} else {
				sql = "SELECT ret.id AS retId,TYPE,CODE,reason,STATUS FROM erp_return ret  where STATUS='"+status+"'";


			}
			if (!"".equals(beginTime)) {
				sql += " and date_format(return_date,'%Y-%m-%d') >= '" + beginTime
						+ "'";
			}
			if (!"".equals(endTime)) {
				sql += " and date_format(return_date,'%Y-%m-%d') <= '" + endTime
						+ "'";
			}
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(map);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
					queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void addWareHouse(WareHouse wh, String ids) {
		insertBatch(ids, wh.getId());
		whMapper.addWareHouse(wh);
	}

	public int checkHasValue(String order_id) {
		return whMapper.checkHasValue(order_id);
	}

	public void insertBatch(String ids, String wh_id) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String[] arr = ids.split(",");
		for (int i = 0; i < arr.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("person_id", arr[i]);
			map.put("wh_id", wh_id);
			list.add(map);
		}
		if (list.size() > 0) {
			whMapper.insertBatch(list);
		}
	}

	public void updateBatch(String ids, String wh_id) {
		whMapper.deleteWHPerson(wh_id);
		insertBatch(ids, wh_id);
	}

	public void updateWareHouse(WareHouse wh, String ids) {
		updateBatch(ids, wh.getId());
		whMapper.updateWareHouse(wh);
	}

	public void deletesPur(String podId) {
		whMapper.deletesPur(podId);
		whMapper.deletesPurDetail(podId);
	}

	public void deleteWareHouseById(String id) {
		whMapper.deleteWareHouseById(id);
	}

	public List<Map<String,Object>>  getWareHouseById(String id) {
		return whMapper.getWareHouseById(id);
	}

	public List<Map<String, Object>> getStyle() {
		return whMapper.getStyle();
	}

	public List<Map<String,Object>>  getWHTypeName(List<String> list) {
		return whMapper.getWHTypeName(list);
	}

	public List<Map<String,Object>>  getWHAdmin(String id) {
		return whMapper.getWHAdmin(id);
	}
	
	public List<Map<String,Object>>  getPodPro(String pdId) {
		return whMapper.getPodPro(pdId);
	}
	//库存excel报表导出
	public ArrayList<YHDbRecord>  getExcelDb() {
		return whMapper.getExcelDb();
	}

	public List<Map<String, Object>> getProductList(String po_id) {
		return whMapper.getProductList(po_id);
	}

	public List<Map<String, Object>> getReturn(String retId) {
		return whMapper.getReturn(retId);
	}
	public List<Map<String, Object>> getLoss(String lossId) {
		return whMapper.getLoss(lossId);
	}

	public List<Map<String, Object>> getLossDb(Map<String,Object> map) {
		return whMapper.getLossDb(map);
	}
	public List<Map<String, Object>> getReturnDb(Map<String,Object> map) {
		return whMapper.getReturnDb(map);
	}

	public List<Map<String, Object>> getReturnDbNum(Map<String, Object> map) {
		return whMapper.getReturnDbNum(map);
	}

	public Map<String, Object> getPro(String proId) {
		return whMapper.getPro(proId);
	}

	public List<Map<String, Object>> getProducts(String pod_id) {
		return whMapper.getProducts(pod_id);
	}

	public List<Map<String, Object>> getNumbers(Map<String,Object> map) {
		return whMapper.getNumbers(map);
	}
	public List<Map<String, Object>> getPudNumbers(Map<String,Object> map) {
		return whMapper.getPudNumbers(map);
	}

	public List<Map<String, Object>> getDbLogList(Map<String,Object> map) {
		return whMapper.getDbLogList(map);
	}

	public Map<String, Object> getCusMsg(String order_id) {
		return whMapper.getCusMsg(order_id);
	}

	public Map<String, Object> getPODMsg(String pod_id) {
		return whMapper.getPODMsg(pod_id);
	}
	public Map<String, Object> getReturnByTypeId(String typeId) {
		return whMapper.getReturnByTypeId(typeId);
	}
	public String getSalePrice(Map<String, Object> map) {
		return whMapper.getSalePrice(map);
	}

	public void addPODBatch(Map<String,Object> podMap,List<Map<String,Object>> podProList,List<Map<String,Object>> dbLogList) {
		whMapper.addPOD(podMap);
		whMapper.addPODBatch(podProList);
		whMapper.addDBLog(dbLogList);
	}

	public void updatePODBatch(Map<String,Object> podMap,List<Map<String,Object>> podProList,List<Map<String,Object>> dbLogList,String pod_id) {
			whMapper.updatePOD(podMap);
			whMapper.deletePODPRO(pod_id);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("pod_id", pod_id);
			map.put("flag", StaticData.SALE);
			whMapper.deleteDbLog(map);
			whMapper.addPODBatch(podProList);
			whMapper.addDBLog(dbLogList);
	}
	
	public void addPur(Map<String,Object> map, List<Map<String,Object>> ppoList,List<Map<String,Object>> dbList) {

		whMapper.addPur(map);
		whMapper.addPpo(ppoList);
		whMapper.addDBLog(dbList);
	}

	public void addReturn(Map<String,Object> returnMap, List<Map<String,Object>> rtProList, List<Map<String,Object>> dbLogList) {
		whMapper.addReturn(returnMap);
		whMapper.addReturnPro(rtProList);
		whMapper.addDBLog(dbLogList);
	}
	public void addLoss(Map<String,Object> map, List<Map<String,Object>> list, List<Map<String,Object>> listPro) {
		purchaseMapper.addProduct(listPro);
		whMapper.addLoss(map);
		whMapper.addDBLog(list);
		whMapper.addLossPro(list);
	}

	public void returnUpdate(Map<String,Object> returnMap, List<Map<String,Object>> rtProList, List<Map<String,Object>> dbLogList,String flag) {
		String return_id = returnMap.get("id").toString();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pod_id", return_id);
		map.put("flag", flag);
		whMapper.deletesDBLog(map);
		whMapper.deleteReturnPro(return_id);
		whMapper.updateReturn(returnMap);
		whMapper.addReturnPro(rtProList);
		whMapper.addDBLog(dbLogList);
	}
	public void lossUpdate(Map<String,Object> map, List<Map<String,Object>> list, String lossId, List<Map<String,Object>> listPro) {
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("pod_id", lossId);
		m.put("flag", StaticData.PUR);
		whMapper.deletesDBLog(m);
		whMapper.deleteLossPro(lossId);
		purchaseMapper.deleteProduct(lossId);
		purchaseMapper.addProduct(listPro);
		updateDbBatch(list);
		whMapper.updateLoss(map);
		whMapper.addDBLog(list);
		whMapper.addLossPro(list);
	}

	public void updateDbBatch(List<Map<String, Object>> list) {
		for (int i = 0; i < list.size(); i++) {
			whMapper.updateDbBatch(list.get(i));
		}
	}

	public void updatePur(Map<String,Object> map, List<Map<String,Object>> ppoList,List<Map<String,Object>> dbList, String podId) {
		whMapper.updatePur(map);
		whMapper.deletesPur(podId);
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("pod_id", podId);
		m.put("flag", StaticData.PUR);
		whMapper.deletesDBLog(m);
		whMapper.addPpo(ppoList);
		whMapper.addDBLog(dbList);
	}

	public void updatePpo(List<Map<String, Object>> list, Map<String,Object> map) {
		for (int i = 0; i < list.size(); i++) {
			whMapper.updatePpo(list.get(i));
		}
	}

	public void updatePurPros(List<Map<String, Object>> list, Map<String,Object> map) {
		for (int i = 0; i < list.size(); i++) {
			whMapper.updatePurPro(list.get(i));
		}
	}

	public void updateDb(List<Map<String, Object>> list, Map<String,Object> map) {
		whMapper.updateDbStatus(map);
		whMapper.updateReturnStatus(map);
		if ("销售".equals(map.get("type").toString())) {
			for (int i = 0; i < list.size(); i++) {
				whMapper.updateDbBatch(list.get(i));
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				whMapper.updateDbJs(list.get(i));
			}
		}
	}

	public void deletePOD(String pod_id) {
		whMapper.deletePOD(pod_id);
		whMapper.deletePODPRO(pod_id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pod_id", pod_id);
		map.put("flag", StaticData.SALE);
		whMapper.deleteDbLog(map);
	}

	public void deleteReturn(String retId) {
		whMapper.deleteReturn(retId);
		whMapper.deleteReturnPro(retId);
	}
	public void deleteLoss(String lossId) {
		whMapper.deleteLoss(lossId);
		whMapper.deleteLossPro(lossId);
	}

	public List<Map<String, Object>> getPur(String outDId) {
		return whMapper.getPur(outDId);
	}

	public List<Map<String, Object>> getPurDb(String outDId) {
		return whMapper.getPurDb(outDId);
	}

	public List<Map<String, Object>> getPurs(String purId) {
		return whMapper.getPurs(purId);
	}

	// 开账
	public void openAccount(String pod_id, Connection dbConn) throws Exception {
		dbConn.setAutoCommit(false);
		Statement st = null;
		try {
			st = dbConn.createStatement();
			String sql = "update erp_order_product_out_detail set pod_status = '"
					+ StaticData.OVER + "' where id='" + pod_id + "'";
			st.executeUpdate(sql);
			OpenAccountUtil util = new OpenAccountUtil();
			util.updatePP(dbConn, pod_id);
			dbConn.commit();
		} catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
	}

	// 开账
	public void openAccountPurchaseDetial(String pDeId, Connection dbConn)
			throws Exception {
		dbConn.setAutoCommit(false);
		String pid = pDeId;// 仓库发货单id
		Statement st = null;

		try {
			st = dbConn.createStatement();
			String sql = "update erp_purchase_product_in_detail set status = '"
					+ StaticData.OVER + "' where id='" + pid + "'";
			st.executeUpdate(sql);
			updatePP(dbConn, pid);
			dbConn.commit();
		} catch (Exception e) {
			dbConn.rollback();
			throw e;
		}
	}

	public void updatePP(Connection dbConn, String pod_id) throws Exception {
		List<Map<String, Object>> ppList = getPP(dbConn, pod_id);

		Statement st = dbConn.createStatement();
		try {
			if (ppList != null) {
				for (int i = 0; i < ppList.size(); i++) {
					String pp_id = (String) ppList.get(i).get("ppidId");
					String pproId = (String) ppList.get(i).get("pproId");
					String po_id = (String) ppList.get(i).get("ppo_id");
					String pro_id = (String) ppList.get(i).get("pro_id");
					String purId = (String) ppList.get(i).get("purId");
					List<Map<String, Object>> podList = getPOD(dbConn, pp_id,
							pro_id);
					int purchase_num = Integer.parseInt(ppList.get(i).get(
							"purchase_num").toString());
					int already_purchase_num = Integer.parseInt(ppList.get(i)
							.get("already_purchase_num").toString());
					String number = getPODNum(pro_id, podList);
					if (number != null) {
						int pponum = Integer.parseInt(number);
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
							if (checkPOStatus(dbConn, po_id, pro_id)) {
								// 如果对应po_id的erp_ppo_pro所有记录的状态都为“已完成”，修改erp_purchase_product_out的状态为“已完成”
								String sql1 = "update erp_purchase_product_out set ppo_status='"
										+ StaticData.OVER
										+ "' where id='"
										+ po_id + "'";
								st.executeUpdate(sql1);
								String status = this.getFinanceById(dbConn,
										purId);
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
			String sql = "SELECT STATUS FROM erp_purchase_paid_plan WHERE purchase_id=' "
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

	private boolean checkPOStatus(Connection dbConn, String po_id, String pro_id)
			throws Exception {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = "SELECT status from erp_ppo_pro where pro_id <> '"
					+ pro_id + "' and ppo_id='" + po_id + "'";
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

	private String getPODNum(String pro_id, List<Map<String, Object>> podList) {
		for (int i = 0; i < podList.size(); i++) {
			if (pro_id.equals((String) podList.get(i).get("pro_id"))) {
				return (String) podList.get(i).get("pponum");
			}
		}
		return null;
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
	private List<Map<String, Object>> getPOD(Connection dbConn, String pp_id,
			String proId) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.createStatement();
			String sql = " SELECT purPro.pro_id,purPro.pur_num FROM erp_purchase_product_in_detail ppid LEFT JOIN erp_pur_pro purPro ON purPro.pur_id = ppid.id WHERE purPro.pur_id = '"
					+ pp_id + "' AND purPro.pro_id = '" + proId + "'";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("pro_id", rs.getString("pro_id"));
				map.put("pponum", rs.getString("pur_num"));
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
}
