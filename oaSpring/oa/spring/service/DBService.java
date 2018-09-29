package oa.spring.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import oa.spring.dao.batis.DBMapper;
import oa.spring.dao.batis.WareHouseMapper;
import oa.spring.util.StaticData;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.form.YHFOM;

public class DBService {
	private WareHouseMapper whMapper;
	private DBMapper dbMapper;

	public DBMapper getDbMapper() {
		return dbMapper;
	}

	public void setDbMapper(DBMapper dbMapper) {
		this.dbMapper = dbMapper;
	}

	public WareHouseMapper getWhMapper() {
		return whMapper;
	}

	public void setWhMapper(WareHouseMapper whMapper) {
		this.whMapper = whMapper;
	}

	public String getDBMsg(Connection dbConn, String param, Map map) {
		try {
			String sql = "";
			String[] arrParam = param.split(",");
			if ("-1".equals(param)) {
				sql = "SELECT db.id AS dbId,pro.id AS proId,wh.id AS whId,wh.name as whName, ps.name AS psName,pro.pro_code,pro.pro_name,db.batch,db.invalid_time,db.price,db.num,db.remark FROM erp_db db LEFT JOIN erp_product pro ON pro.id=db.pro_id LEFT JOIN erp_warehouse wh ON wh.id=db.wh_id LEFT JOIN erp_product_style ps ON ps.id=wh.type ";
			} else {
				sql = "SELECT db.id AS dbId,pro.id AS proId,wh.id AS whId,wh.name AS whName, ps.name AS psName,pro.pro_code,pro.pro_name,db.batch,db.invalid_time,db.price,db.num,db.remark FROM erp_db db LEFT JOIN erp_product pro ON pro.id=db.pro_id LEFT JOIN erp_warehouse wh ON wh.id=db.wh_id LEFT JOIN erp_product_style ps ON ps.id=wh.type  WHERE 1=1";
				if (!"".equals(arrParam[0])) {
					sql += " and wh.name LIKE '%" + arrParam[0] + "%'";
				}
				 if (!"".equals(arrParam[1]) && !"0".equals(arrParam[1])) {
					sql += " and wh.type = '" + arrParam[1] + "'";
				}
				 if(!"0".equals(arrParam[2])){
						
						sql+=" and pro.pro_name like '%"+ arrParam[2]+"%'";
					}
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

	public String getDBLog(Connection dbConn, String flag, String beginTime,
			String endTime,String proName, Map map) {
		try {
			String sql = "";
			if(StaticData.SALE.equals(flag)){
			sql = "SELECT dbLog.remark,pod.id,wh.name,pod.pod_code, cp.pro_code,cp.pro_name,dbLog.price,dbLog.number,dbLog.time FROM erp_db_log dbLog LEFT JOIN erp_order_product_out_detail pod ON pod.id=dbLog.pod_id  LEFT JOIN erp_warehouse wh ON wh.id=dbLog.wh_id LEFT JOIN erp_cache_product cp ON cp.pro_id=dbLog.pro_id WHERE  dbLog.status='"+StaticData.OVER+"' and dbLog.flag='"+StaticData.SALE+"'";
			}
			if(StaticData.PUR.equals(flag)){
				sql = "SELECT dbLog.remark,pid.id,wh.name,pid.code,cp.pro_code,cp.pro_name,dbLog.price,dbLog.number,dbLog.time FROM erp_db_log dbLog LEFT JOIN erp_purchase_product_in_detail pid ON pid.id=dbLog.pod_id  LEFT JOIN erp_warehouse wh ON wh.id=dbLOg.wh_id LEFT JOIN erp_cache_product cp ON cp.pro_id=dbLog.pro_id WHERE  dbLog.status='"+StaticData.OVER+"' and dbLog.flag='"+StaticData.PUR+"'";
			}
			if(StaticData.DB.equals(flag)){
				sql = "SELECT dbLog.remark,db.id AS dbId,pro.id AS proId,wh.id AS whId,wh.name AS whName,pro.pro_code,pro.pro_name,dbLog.price,dbLog.number,dbLog.time FROM erp_db db LEFT JOIN erp_product pro ON pro.id=db.pro_id LEFT JOIN erp_warehouse wh ON wh.id=db.wh_id LEFT JOIN erp_product_style ps ON ps.id=wh.type  LEFT JOIN erp_db_log dbLog ON (dbLog.pro_id=db.pro_id AND dbLog.wh_id=db.wh_id) WHERE  dbLog.status='"+StaticData.OVER+"' and dbLog.flag='"+StaticData.DB+"'";
			}
			if(StaticData.SALE_RETURN.equals(flag)){
				sql = "SELECT dbLog.remark,ret.id, wh.name,ret.code,cp.pro_code,cp.pro_name,dbLog.price,dbLog.number,dbLog.time FROM erp_db_log dbLog LEFT JOIN erp_return ret ON ret.id=dbLog.pod_id  LEFT JOIN erp_warehouse wh ON wh.id=dbLOg.wh_id LEFT JOIN erp_cache_product cp ON cp.pro_id=dbLog.pro_id WHERE  dbLog.status='"+StaticData.OVER+"' and ret.type='销售'  and dbLog.flag='"+StaticData.SALE_RETURN+"'";
			}
			if(StaticData.PUR_RETURN.equals(flag)){
				sql = "SELECT dbLog.remark,ret.id,wh.name,ret.code,cp.pro_code,cp.pro_name,dbLog.price,dbLog.number,dbLog.time FROM erp_db_log dbLog LEFT JOIN erp_return ret ON ret.id=dbLog.pod_id  LEFT JOIN erp_warehouse wh ON wh.id=dbLOg.wh_id LEFT JOIN erp_cache_product cp ON cp.pro_id=dbLog.pro_id WHERE  dbLog.status='"+StaticData.OVER+"' and ret.type='采购'  and dbLog.flag='"+StaticData.PUR_RETURN+"'";
			}if(StaticData.LOSS.equals(flag)){
				sql = " SELECT loss.id, wh.name,loss.code,cp.pro_code,cp.pro_name,dbLog.price,dbLog.number,dbLog.time FROM erp_db_log dbLog LEFT JOIN erp_loss loss ON loss.id=dbLog.pod_id  LEFT JOIN erp_warehouse wh ON wh.id=dbLOg.wh_id LEFT JOIN erp_cache_product cp ON cp.pro_id=dbLog.pro_id WHERE  dbLog.status='"+StaticData.OVER+"' and dbLog.flag='"+StaticData.LOSS+"'";
			}
			if(StaticData.PRODUCE_PICK.equals(flag)){
				sql = " SELECT dbLog.remark,bom.id, wh.name,bom.code,cp.pro_code,cp.pro_name,dbLog.price,dbLog.number,dbLog.time FROM erp_db_log dbLog LEFT JOIN erp_produce_bom bom ON bom.id=dbLog.pod_id  LEFT JOIN erp_warehouse wh ON wh.id=dbLOg.wh_id LEFT JOIN erp_cache_product cp ON cp.pro_id=dbLog.pro_id WHERE  dbLog.status='"+StaticData.OVER+"' and bom.bom_type='1'   and dbLog.flag='"+StaticData.PRODUCE_PICK+"'";
			}
			if(StaticData.CHECK_IN.equals(flag)){
				sql = " SELECT dbLog.remark,exam.id, wh.name,exam.code,cp.pro_code,cp.pro_name,dbLog.price,dbLog.number,dbLog.time FROM erp_db_log dbLog LEFT JOIN erp_produce_exam  exam ON exam.id=dbLog.pod_id LEFT JOIN erp_warehouse wh ON wh.id=dbLOg.wh_id LEFT JOIN erp_cache_product cp ON cp.pro_id=dbLog.pro_id WHERE  dbLog.status='"+StaticData.OVER+"'  and dbLog.flag='"+StaticData.CHECK_IN+"'";
			}
			if(StaticData.PRODUCE_RETURN.equals(flag)){
				sql = " SELECT dbLog.remark,bom.id, wh.name,bom.code,cp.pro_code,cp.pro_name,dbLog.price,dbLog.number,dbLog.time FROM erp_db_log dbLog LEFT JOIN erp_produce_bom bom ON bom.id=dbLog.pod_id  LEFT JOIN erp_warehouse wh ON wh.id=dbLOg.wh_id LEFT JOIN erp_cache_product cp ON cp.pro_id=dbLog.pro_id WHERE  dbLog.status='"+StaticData.OVER+"' and bom.bom_type='2' and dbLog.flag='"+StaticData.PRODUCE_RETURN+"'";
			}
			if (!"".equals(beginTime)) {
				sql += " and date_format(time,'%Y-%m-%d') >= '"
						+ beginTime + "'";
			}
			if (!"".equals(endTime)) {
				sql += " and date_format(time,'%Y-%m-%d') <= '" + endTime
						+ "'";
			}if(!"".equals(proName)){
				sql+="  and pro_name like'%"+proName+"%'";
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

	public Map<String, Object> getWhType(String whId) {
		return dbMapper.getWhType(whId);
	}

	public Map<String, Object> getDBMsgById(String id) {
		return dbMapper.getDBMsgById(id);
	}
	public Map<String, Object> getDBById(String id) {
		return dbMapper.getDBById(id);
	}

	public List getWareHouse() {
		return whMapper.getWareHouse();
	}

	public void updateDB(Map<String, String> map) {
		dbMapper.updateDB(map);
		dbMapper.updateDBLog(map);
//		dbMapper.addDbLogTest(map);
	}

	public void deleteDBById(String id,Map map) {
		dbMapper.deleteDBById(id);
		dbMapper.deleteDBLog(map);
//		dbMapper.addDbLogTest(map);
	}
	public int checkBatckOnly(Map<String,Object> map) {
		return dbMapper.checkBatckOnly(map);
	}

	public void insertBatch(List<Map<String, Object>> list) {
		dbMapper.insertBatch(list);
		dbMapper.addDbLog(list);
	}

	public void updateDbBatch(List<Map<String, Object>> list) {
		for (int i = 0; i < list.size(); i++) {
			dbMapper.updateDbBatch(list.get(i));
		}
	}
}
