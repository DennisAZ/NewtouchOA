package yh.subsys.oa.officeProduct.product.logic;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.officeProduct.manage.data.YHOfficeTranshistory;
import yh.subsys.oa.officeProduct.officeType.data.YHOfficeDepository;
import yh.subsys.oa.officeProduct.product.data.YHOfficeProducts;
import yh.subsys.oa.officeProduct.query.data.YHOfficeType;

public class YHOfficeProductsLogic {
	private static Logger log = Logger.getLogger(YHOfficeProductsLogic.class);

	/**
	 * 获取办公用品库(返回类别seq_id)
	 * 
	 * @param dbConn
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getOfficeDepositoryNamesLogic(Connection dbConn, YHPerson person) throws Exception {
		StringBuffer buffer = new StringBuffer("[");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String conditionStr = YHDBUtility.findInSet(String.valueOf(person.getDeptId()), "DEPT_ID")
				+ " or DEPT_ID = '' or DEPT_ID = 'ALL_DEPT' or DEPT_ID='0' ";
		String sql = "select OFFICE_TYPE_ID,DEPOSITORY_NAME from oa_office_repertory where " + conditionStr;
		boolean isHave = false;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String typeIdStr = YHUtility.null2Empty(rs.getString("OFFICE_TYPE_ID"));
				String depositoryName = YHUtility.null2Empty(rs.getString("DEPOSITORY_NAME"));
				buffer.append("{");
				buffer.append("typeId:\"" + YHUtility.encodeSpecial(typeIdStr) + "\"");
				buffer.append(",name:\"" + YHUtility.encodeSpecial(depositoryName) + "\"");
				buffer.append("},");
				isHave = true;
			}
			if (isHave) {
				buffer = buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return buffer.toString();
	}
	/**
	 * 获取办公用品库(返回类别seq_id)产品编辑页面,如与extData值相等则选中
	 * 
	 * @param dbConn
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getproEditDepositoryNamesLogic(Connection dbConn, YHPerson person,String extData) throws Exception {
		StringBuffer buffer = new StringBuffer("[");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String conditionStr = YHDBUtility.findInSet(String.valueOf(person.getDeptId()), "DEPT_ID")
		+ " or DEPT_ID = '' or DEPT_ID = 'ALL_DEPT' or DEPT_ID='0' ";
		String sql = "select OFFICE_TYPE_ID,DEPOSITORY_NAME from oa_office_repertory where " + conditionStr;
		boolean isHave = false;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String typeIdStr = YHUtility.null2Empty(rs.getString("OFFICE_TYPE_ID"));
				String depositoryName = YHUtility.null2Empty(rs.getString("DEPOSITORY_NAME"));
				boolean isHaveFlag = this.isFindInSet(typeIdStr.split(","), extData);
				int selectFlag = 0;
				if (isHaveFlag) {
					selectFlag = 1;
				}
				buffer.append("{");
				buffer.append("typeId:\"" + YHUtility.encodeSpecial(typeIdStr) + "\"");
				buffer.append(",name:\"" + YHUtility.encodeSpecial(depositoryName) + "\"");
				buffer.append(",selectFlag:\"" + selectFlag + "\"");
				buffer.append("},");
				isHave = true;
			}
			if (isHave) {
				buffer = buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return buffer.toString();
	}

	/**
	 * 获取办公用品库(返回库seq_id)
	 * 
	 * @param dbConn
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getOfficeDepositoryNameLogic(Connection dbConn, YHPerson person) throws Exception {
		StringBuffer buffer = new StringBuffer("[");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select SEQ_ID,DEPOSITORY_NAME from oa_office_repertory";
		boolean isHave = false;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int dbSeqId = rs.getInt("SEQ_ID");
				String depositoryName = YHUtility.null2Empty(rs.getString("DEPOSITORY_NAME"));
				buffer.append("{");
				buffer.append("typeId:\"" + dbSeqId + "\"");
				buffer.append(",name:\"" + YHUtility.encodeSpecial(depositoryName) + "\"");
				buffer.append("},");
				isHave = true;
			}
			if (isHave) {
				buffer = buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return buffer.toString();
	}

	/**
	 * 获取办公用品类别
	 * 
	 * @param dbConn
	 * @param typeIdStr
	 * @return
	 * @throws Exception
	 */
	public String getOfficeTypeNamesByIdLogic(Connection dbConn, String typeIdStr) throws Exception {
		StringBuffer buffer = new StringBuffer("[");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (YHUtility.isNullorEmpty(typeIdStr)) {
			typeIdStr = "0";
		}
		if (typeIdStr.endsWith(",")) {
			typeIdStr = typeIdStr.substring(0, typeIdStr.length() - 1);
		}
		String sql = "select SEQ_ID,TYPE_NAME from oa_office_kind  where SEQ_ID in(" + typeIdStr + ")";
		boolean isHave = false;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int dbSeqId = rs.getInt("SEQ_ID");
				String typeName = YHUtility.null2Empty(rs.getString("TYPE_NAME"));
				buffer.append("{");
				buffer.append("typeId:\"" + dbSeqId + "\"");
				buffer.append(",name:\"" + YHUtility.encodeSpecial(typeName) + "\"");
				buffer.append("},");
				isHave = true;
			}
			if (isHave) {
				buffer = buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return buffer.toString();
	}

	public int addOfficeProductsLogic(Connection dbConn, YHOfficeProducts products) throws Exception {
		YHORM orm = new YHORM();
		try {
			orm.saveSingle(dbConn, products);
			int proId = this.getMaSeqId(dbConn, "oa_office_goods");
			
			YHOfficeTranshistory officeTranshistory = new YHOfficeTranshistory();
			officeTranshistory.setProId(proId);
			officeTranshistory.setTransFlag("6");
			officeTranshistory.setTransQty(products.getProStock());
			officeTranshistory.setPrice(products.getProPrice());
			
			officeTranshistory.setTransDate(YHUtility.parseTimeStamp());
			officeTranshistory.setOperator(products.getProCreator());
			officeTranshistory.setFactQty(0);
			officeTranshistory.setCycleNo(0);
			officeTranshistory.setDeptId(0);
			orm.saveSingle(dbConn, officeTranshistory);
			
			return  proId;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 判断是否存在办公用品
	 * 
	 * @param dbConn
	 * @param officeProtype
	 * @param proName
	 * @param prodSeqIdStr
	 * @return
	 * @throws Exception
	 */
	public int isHaveValue(Connection dbConn, String officeProtype, String proName, String prodSeqIdStr) throws Exception {
		int counter = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		int prodSeqId = 0;
		String conditionStr = "";
		if (YHUtility.isNumber(prodSeqIdStr) && !"0".equals(prodSeqIdStr)) {
			prodSeqId = Integer.parseInt(prodSeqIdStr);
			conditionStr = " and SEQ_ID	<>" + prodSeqId;
		}
		String sql = "select count(*) from oa_office_goods where OFFICE_PROTYPE=? and PRO_NAME=?" + conditionStr;
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, YHUtility.null2Empty(officeProtype));
			stmt.setString(2, YHUtility.null2Empty(proName));
			rs = stmt.executeQuery();
			if (rs.next()) {
				counter = rs.getInt(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return counter;
	}

	/**
	 * 返回最大的SEQ_Id
	 * 
	 * @param dbConn
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public int getMaSeqId(Connection dbConn, String tableName) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		int maxSeqId = 0;
		String sql = "select max(SEQ_ID) as SEQ_ID from " + tableName;
		try {
			stmt = dbConn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				maxSeqId = rs.getInt("SEQ_ID");
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return maxSeqId;
	}

	public YHOfficeProducts getOfficeProductsById(Connection dbConn, String objId) throws Exception {
		YHORM orm = new YHORM();
		int seqId = 0;
		if (YHUtility.isNumber(objId)) {
			seqId = Integer.parseInt(objId);
		}
		try {
			return (YHOfficeProducts) orm.loadObjSingle(dbConn, YHOfficeProducts.class, seqId);
		} catch (Exception e) {
			throw e;
		}
	}

	public void delOfficeProductsByIdLogic(Connection dbConn, String proSeqIdStr) throws Exception {
		if (YHUtility.isNullorEmpty(proSeqIdStr)) {
			proSeqIdStr = "0";
		}
		if (proSeqIdStr.endsWith(",")) {
			proSeqIdStr = proSeqIdStr.substring(0, proSeqIdStr.length() - 1);
		}
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "delete from oa_office_goods where SEQ_ID in(" + proSeqIdStr + ")";
		try {
			stmt = dbConn.createStatement();
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}

	public void delTranshistoryLogic(Connection dbConn, String prodSeqIdStr) throws Exception {
		if (YHUtility.isNullorEmpty(prodSeqIdStr)) {
			prodSeqIdStr = "0";
		}
		if (prodSeqIdStr.endsWith(",")) {
			prodSeqIdStr = prodSeqIdStr.substring(0, prodSeqIdStr.length() - 1);
		}
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "delete from oa_office_trans_records where PRO_ID in(" + prodSeqIdStr + ")";
		try {
			stmt = dbConn.createStatement();
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}

	/**
	 * 判断是否存在类别
	 * 
	 * @param dbConn
	 * @param depositorySeqId
	 * @param typeNameStr
	 * @return
	 * @throws Exception
	 */
	public boolean isHaveOfficeTypeLogic(Connection dbConn, String depositorySeqId, String typeNameStr) throws Exception {
		boolean flag = false;
		YHORM orm = new YHORM();
		int depSeqId = 0;
		if (YHUtility.isNumber(depositorySeqId)) {
			depSeqId = Integer.parseInt(depositorySeqId);
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			int counter = 0;
			YHOfficeDepository depository = (YHOfficeDepository) orm.loadObjSingle(dbConn, YHOfficeDepository.class, depSeqId);
			String officeTypeIdStr = "";
			if (depository != null) {
				officeTypeIdStr = YHUtility.null2Empty(depository.getOfficeTypeId());
			}
			if (YHUtility.isNullorEmpty(officeTypeIdStr)) {
				officeTypeIdStr = "0";
			}
			if (officeTypeIdStr.endsWith(",")) {
				officeTypeIdStr = officeTypeIdStr.substring(0, officeTypeIdStr.length() - 1);
			}
			String sql = "select count(*) from oa_office_kind where SEQ_ID IN(" + officeTypeIdStr + ") AND TYPE_NAME =?";
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, YHUtility.null2Empty(typeNameStr));
			rs = stmt.executeQuery();
			if (rs.next()) {
				counter = rs.getInt(1);
			}
			if (counter > 0) {
				flag = true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return flag;
	}

	/**
	 * 新增类别
	 * 
	 * @param dbConn
	 * @param depository
	 * @param typeNameStr
	 * @param typeOrder
	 * @throws Exception
	 */
	public void addOfficeTypeLogic(Connection dbConn, String depositorySeqId, String typeNameStr, String typeOrder) throws Exception {
		YHORM orm = new YHORM();
		int depSeqId = 0;
		if (YHUtility.isNumber(depositorySeqId)) {
			depSeqId = Integer.parseInt(depositorySeqId);
		}
		try {
			YHOfficeType officeType = new YHOfficeType();
			if (depSeqId == 1) {
				officeType.setTypeDepository(1);
			} else {
				officeType.setTypeDepository(depSeqId);
			}
			officeType.setTypeName(YHUtility.null2Empty(typeNameStr));
			officeType.setTypeOrder(YHUtility.null2Empty(typeOrder));
			officeType.setTypeDepository(depSeqId);
			orm.saveSingle(dbConn, officeType);
			int maxSqlId = this.getMaSeqId(dbConn, "oa_office_kind");
			YHOfficeDepository depository = (YHOfficeDepository) orm.loadObjSingle(dbConn, YHOfficeDepository.class, depSeqId);
			String officeTypeIdStr = "";
			if (depository != null) {
				officeTypeIdStr = YHUtility.null2Empty(depository.getOfficeTypeId());

				if (!YHUtility.isNullorEmpty(officeTypeIdStr)) {
					officeTypeIdStr = officeTypeIdStr + "," + maxSqlId;
				} else {
					officeTypeIdStr = String.valueOf(maxSqlId);
				}
				depository.setOfficeTypeId(officeTypeIdStr);
				orm.updateSingle(dbConn, depository);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 更新类别
	 * @param dbConn
	 * @param depositorySeqId
	 * @param typeNameStr
	 * @param typeIdStr
	 * @throws Exception
	 */
	public void updateOfficeTypeLogic(Connection dbConn, String depositorySeqId, String typeNameStr, String typeIdStr) throws Exception {
		YHORM orm = new YHORM();
		int depSeqId = 0;
		if (YHUtility.isNumber(depositorySeqId)) {
			depSeqId = Integer.parseInt(depositorySeqId);
		}
		int typeId= 0;
		if (YHUtility.isNumber(typeIdStr)) {
			typeId = Integer.parseInt(typeIdStr);
		}
		try {
			Map<Object, Object> map = this.getOfficeTypeInfo(dbConn, typeIdStr);
			int depoSeqId = (Integer)map.get("depoSeqId");
			String officeTypeIdStr = (String)map.get("officeTypeIdStr");
			String delStrValue = this.delStrVale(officeTypeIdStr.split(","), String.valueOf(typeId));
			YHOfficeDepository depository = (YHOfficeDepository) orm.loadObjSingle(dbConn, YHOfficeDepository.class, depoSeqId);
			if (depository!=null) {
				depository.setOfficeTypeId(delStrValue);
				orm.updateSingle(dbConn, depository);
			}
			
			YHOfficeDepository depository1 = (YHOfficeDepository) orm.loadObjSingle(dbConn, YHOfficeDepository.class, depSeqId);
			if (depository1!=null) {
				String officeTypeSeqIdStr = YHUtility.null2Empty(depository1.getOfficeTypeId());
				if (!YHUtility.isNullorEmpty(officeTypeSeqIdStr)) {
					officeTypeSeqIdStr = officeTypeSeqIdStr + "," + String.valueOf(typeId);
				}else {
					officeTypeSeqIdStr = String.valueOf(typeId);
				}
				depository1.setOfficeTypeId(officeTypeSeqIdStr);
				orm.updateSingle(dbConn, depository1);
			}
			
			YHOfficeType officeType = (YHOfficeType) orm.loadObjSingle(dbConn, YHOfficeType.class, typeId); 
			if (officeType!=null) {
				officeType.setTypeName(typeNameStr);
				officeType.setTypeDepository(depSeqId);
				orm.updateSingle(dbConn, officeType);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 替换值
	 * @param str
	 * @param oldIdStr
	 * @param replaceVale
	 * @return
	 */
	public String replaceVale(String[] str, String oldIdStr,String replaceVale) {
		StringBuffer buffer = new StringBuffer();
		for (int y = 0; y < str.length; y++) {
			if (str[y].equals(oldIdStr)) {
				str[y]=replaceVale;
			}
			buffer.append(str[y] + ",");
		}
		String data = buffer.toString();
		if (data.endsWith(",")) {
			data = data.substring(0, data.length()-1);
		}
		return data;
	}

	/**
	 * 删除字符串中的值
	 * 
	 * @param str
	 * @param delStr
	 * @return
	 */
	public static String delStrVale(String[] str, String delStr) {
		StringBuffer buffer = new StringBuffer();
		for (int y = 0; y < str.length; y++) {
			if (!str[y].equals(delStr)) {
				buffer.append(str[y] + ",");
			}
		}
		String data = buffer.toString();
		if (data.endsWith(",")) {
			data = data.substring(0, data.length() - 1);
		}
		return data;
	}
	/**
	 * 判断数组中是否有该值
	 * @param str
	 * @param deptIdStr
	 * @return
	 */
	public boolean isFindInSet(String[] str, String deptIdStr) {
		for (int y = 0; y < str.length; y++) {
			if (str[y].equals(deptIdStr)) {
				return true;
			} else {
				continue;
			}
		}
		return false;
	}
	public Map<Object, Object> getOfficeTypeInfo(Connection dbConn,String typeIdStr) throws Exception{
		Map<Object, Object> map = new HashMap<Object, Object>();
		int typeId= 0;
		if (YHUtility.isNumber(typeIdStr)) {
			typeId = Integer.parseInt(typeIdStr);
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select a.SEQ_ID as depoId,OFFICE_TYPE_ID from oa_office_repertory a left outer join oa_office_kind b on a.SEQ_ID=b.TYPE_DEPOSITORY where b.SEQ_ID=? ";
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setInt(1, typeId);
			rs = stmt.executeQuery();
			int depoId = 0;
			String officeTypeIdStr = "";
			if (rs.next()) {
				depoId = rs.getInt("depoId");
				officeTypeIdStr = YHUtility.null2Empty(rs.getString("OFFICE_TYPE_ID"));
			}
			map.put("depoSeqId", depoId);
			map.put("officeTypeIdStr", officeTypeIdStr);
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return map;
	}
	
	

	public ArrayList<YHDbRecord> downCSVTempletLogic() throws Exception {
		ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
		YHDbRecord record = new YHDbRecord();
		try {
			record.addField("办公用品名称", "");
			record.addField("办公用品库", "");
			record.addField("办公用品类别", "");
			record.addField("编码", "");
			record.addField("单价", "");
			record.addField("办公用品描述", "");
			record.addField("计量单位", "");
			record.addField("供应商", "");
			record.addField("最低警戒库存", "");
			record.addField("最高警戒库存", "");

			record.addField("当前库存", "");
//			record.addField("创建人", "");
			record.addField("登记权限(用户)", "");
			record.addField("审批人", "");
			result.add(record);
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 导入办公用品信息
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public Map<Object, Object> impOfficeProToCsvLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person,StringBuffer buffer) throws Exception {
		YHORM orm = new YHORM();
		Map<Object, Object> returnMap = new HashMap<Object, Object>();
		
		Map<Object, Object> bufferMap = new HashMap<Object, Object>();
		int isCount = 0;
		String infoStr = "";
		String color = "";
		try {
			InputStream is = fileForm.getInputStream();
			ArrayList<YHDbRecord> dbRecords = YHCSVUtil.CVSReader(is, YHConst.CSV_FILE_CODE);
			for (YHDbRecord record : dbRecords) {
				String proName = (String) record.getValueByName("办公用品名称");
				String officeDepository = (String) record.getValueByName("办公用品库");
				String officeProtype = (String) record.getValueByName("办公用品类别");
				String query = "SELECT SEQ_ID , TYPE_DEPOSITORY FROM oa_office_kind  where TYPE_NAME = '" + officeProtype + "'";
				PreparedStatement stmt = null;
		    ResultSet rs = null;
		    
		    try {
		      stmt = dbConn.prepareStatement(query);
		      rs = stmt.executeQuery();
		      if (rs.next()) {
		        officeProtype  = rs.getInt("SEQ_ID") + "";
		        officeDepository = rs.getString("TYPE_DEPOSITORY");
		      }
		    } catch (Exception e) {
		      throw e;
		    }finally{
		      YHDBUtility.close(stmt, rs, log);
		    }
				String proCode = (String) record.getValueByName("编码");
				String proPriceStr = (String) record.getValueByName("单价");
				String proDesc = (String) record.getValueByName("办公用品描述");
				String proUnit = (String) record.getValueByName("计量单位");
				String proSupplier = (String) record.getValueByName("供应商");
				String proLowstockStr = (String) record.getValueByName("最低警戒库存");
				String proMaxstockStr = (String) record.getValueByName("最高警戒库存");
				String proStockStr = (String) record.getValueByName("当前库存");
//				String proCreator = (String) record.getValueByName("创建人");
				String proManager = (String) record.getValueByName("登记权限(用户)");
				String proAuditer = (String) record.getValueByName("审批人");
				
				double proPrice = 0;
				if (YHUtility.isNumber(proPriceStr)) {
					proPrice = Double.parseDouble(proPriceStr);
				}
				int proLowstock = 0;
				if (YHUtility.isNumber(proLowstockStr)) {
					proLowstock = Integer.parseInt(proLowstockStr);
				}
				int proMaxstock = 0;
				if (YHUtility.isNumber(proMaxstockStr)) {
					proMaxstock = Integer.parseInt(proMaxstockStr);
				}
				int proStock = 0;
				if (YHUtility.isNumber(proStockStr)) {
					proStock = Integer.parseInt(proStockStr);
				}
				
				if (YHUtility.isNullorEmpty(officeDepository)) {
					infoStr = "导入失败，办公用品库为空!";
					color = "red";
					bufferMap.put("officeDepository", YHUtility.null2Empty(officeDepository));
					bufferMap.put("officeProtype", YHUtility.null2Empty(officeProtype));
					bufferMap.put("proName", YHUtility.null2Empty(proName));
					bufferMap.put("proUnit", YHUtility.null2Empty(proUnit));
					bufferMap.put("proStockStr", YHUtility.null2Empty(proStockStr));

					bufferMap.put("infoStr", infoStr);
					bufferMap.put("color", color);
					sbStrJson(buffer, bufferMap);
					continue;
				}
				if (YHUtility.isNullorEmpty(officeProtype)) {
					infoStr = "导入失败，办公用品类别为空!";
					color = "red";
					bufferMap.put("officeDepository", YHUtility.null2Empty(officeDepository));
					bufferMap.put("officeProtype", YHUtility.null2Empty(officeProtype));
					bufferMap.put("proName", YHUtility.null2Empty(proName));
					bufferMap.put("proUnit", YHUtility.null2Empty(proUnit));
					bufferMap.put("proStockStr", YHUtility.null2Empty(proStockStr));
					
					bufferMap.put("infoStr", infoStr);
					bufferMap.put("color", color);
					sbStrJson(buffer, bufferMap);
					continue;
				}
				if (YHUtility.isNullorEmpty(proName)) {
					infoStr = "导入失败，办公用品名称为空!";
					color = "red";
					bufferMap.put("officeDepository", YHUtility.null2Empty(officeDepository));
					bufferMap.put("officeProtype", YHUtility.null2Empty(officeProtype));
					bufferMap.put("proName", YHUtility.null2Empty(proName));
					bufferMap.put("proUnit", YHUtility.null2Empty(proUnit));
					bufferMap.put("proStockStr", YHUtility.null2Empty(proStockStr));
					
					bufferMap.put("infoStr", infoStr);
					bufferMap.put("color", color);
					sbStrJson(buffer, bufferMap);
					continue;
				}
				if (YHUtility.isNullorEmpty(proUnit)) {
					infoStr = "导入失败，计量单位为空!";
					color = "red";
					bufferMap.put("officeDepository", YHUtility.null2Empty(officeDepository));
					bufferMap.put("officeProtype", YHUtility.null2Empty(officeProtype));
					bufferMap.put("proName", YHUtility.null2Empty(proName));
					bufferMap.put("proUnit", YHUtility.null2Empty(proUnit));
					bufferMap.put("proStockStr", YHUtility.null2Empty(proStockStr));
					
					bufferMap.put("infoStr", infoStr);
					bufferMap.put("color", color);
					sbStrJson(buffer, bufferMap);
					continue;
				}

				YHOfficeProducts products = new YHOfficeProducts();
				products.setProName(proName);

				products.setOfficeProtype(officeProtype);
				products.setProCode(proCode);
				products.setProPrice(proPrice);
				products.setProDesc(proDesc);
				products.setProUnit(proUnit);
				products.setProSupplier(proSupplier);
				products.setProLowstock(proLowstock);
				products.setProMaxstock(proMaxstock);
				products.setProStock(proStock);
				products.setProCreator(String.valueOf(person.getSeqId()));
				
				if (!YHUtility.isNumber(proManager)) {
					proManager = "";
				}
				if (!YHUtility.isNumber(proAuditer)) {
					proAuditer = "";
				}
				
				products.setProManager(proManager);
				products.setProAuditer(proAuditer);
				orm.saveSingle(dbConn, products);
				isCount++;
				
				infoStr = "导入成功!";
				color = "green";
				bufferMap.put("officeDepository", YHUtility.null2Empty(officeDepository));
				bufferMap.put("officeProtype", YHUtility.null2Empty(officeProtype));
				bufferMap.put("proName", YHUtility.null2Empty(proName));
				bufferMap.put("proUnit", YHUtility.null2Empty(proUnit));
				bufferMap.put("proStockStr", YHUtility.null2Empty(proStockStr));
				
				bufferMap.put("infoStr", infoStr);
				bufferMap.put("color", color);
				sbStrJson(buffer, bufferMap);
			}
			returnMap.put("isCount", isCount);
			return returnMap;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String sbStrJson(StringBuffer sb, Map<Object, Object> map) {
		String officeDepository = (String) map.get("officeDepository");
		String officeProtype = (String) map.get("officeProtype");
		String proName = (String) map.get("proName");
		String proUnit = (String) map.get("proUnit");
		String proStockStr = (String) map.get("proStockStr");
		
		String infoStr = (String) map.get("infoStr");
		String color = (String) map.get("color");

		sb.append("{");
		sb.append("officeDepository:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(officeDepository)) + "\""); 
		sb.append(",officeProtype:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(officeProtype)) + "\"");
		sb.append(",proName:\"" + YHUtility.null2Empty(proName) + "\"");
		sb.append(",proUnit:\"" + YHUtility.null2Empty(proUnit) + "\"");
		sb.append(",proStockStr:\"" + YHUtility.null2Empty(proStockStr) + "\"");
		
		sb.append(",infoStr:\"" + YHUtility.null2Empty(infoStr) + "\"");// 信息
		sb.append(",color:\"" + YHUtility.null2Empty(color) + "\"");// 颜色
		sb.append("},");
		return sb.toString();
	}
	
	
	

	/**
	 * 取办公用品类别
	 * 
	 * @param dbConn
	 * @return
	 * @throws Exception
	 */
	public String getOfficeTypeLogic(Connection dbConn) throws Exception {
		StringBuffer buffer = new StringBuffer("[");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select a.SEQ_ID as typeId," +
				"TYPE_NAME," +
				"TYPE_DEPOSITORY," +
				"DEPOSITORY_NAME " +
				" from oa_office_kind a  left outer join oa_office_repertory b on a.TYPE_DEPOSITORY=b.SEQ_ID order by a.TYPE_DEPOSITORY";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			boolean isHave = false;
			while (rs.next()) {
				int typeId = rs.getInt("typeId");
				String typeName = YHUtility.null2Empty(rs.getString("TYPE_NAME"));
				String typeDepository = YHUtility.null2Empty(rs.getString("TYPE_DEPOSITORY"));
				String depositoryName = YHUtility.null2Empty(rs.getString("DEPOSITORY_NAME"));
				buffer.append("{");
				buffer.append("typeId:\"" + typeId + "\"");
				buffer.append(",typeName:\"" + YHUtility.encodeSpecial(typeName) + "\"");
				buffer.append(",typeDepository:\"" + YHUtility.encodeSpecial(typeDepository) + "\"");
				buffer.append(",depositoryName:\"" + YHUtility.encodeSpecial(depositoryName) + "\"");
				buffer.append("},");
				isHave = true;
			}
			if (isHave) {
				buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");

		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return buffer.toString();
	}
	
	/**
	 * 判断是否存在类别
	 * 
	 * @param dbConn
	 * @param depositorySeqId
	 * @param typeNameStr
	 * @return
	 * @throws Exception
	 */
	public boolean isHaveOfficeType2Logic(Connection dbConn, String depositorySeqId, String typeNameStr) throws Exception {
		boolean flag = false;
		int depSeqId = 0;
		if (YHUtility.isNumber(depositorySeqId)) {
			depSeqId = Integer.parseInt(depositorySeqId);
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			int counter = 0;
			String sql = "select count(*) from oa_office_kind where TYPE_NAME=? and TYPE_DEPOSITORY=?";
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, YHUtility.null2Empty(typeNameStr));
			stmt.setInt(2, depSeqId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				counter = rs.getInt(1);
			}
			if (counter > 0) {
				flag = true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return flag;
	}
	
	public void delTypeNameLogic(Connection dbConn,String typeIdStr) throws Exception{
		YHORM orm = new YHORM();
		int typeId = 0;
		if (YHUtility.isNumber(typeIdStr)) {
			typeId = Integer.parseInt(typeIdStr);
		}
		try {
			orm.deleteSingle(dbConn, YHOfficeType.class, typeId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 获取办公用品类别(根据库的id)
	 * 2011-3-29
	 * @param dbConn
	 * @param storeIdStr
	 * @return
	 * @throws Exception
	 */
	public String getTypeNamesByStoreIdLogic(Connection dbConn, String storeIdStr) throws Exception {
		StringBuffer buffer = new StringBuffer("[");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (YHUtility.isNullorEmpty(storeIdStr)) {
			storeIdStr = "0";
		}
		if (storeIdStr.endsWith(",")) {
			storeIdStr = storeIdStr.substring(0, storeIdStr.length() - 1);
		}
		String sql = "select SEQ_ID,TYPE_NAME from oa_office_kind  where TYPE_DEPOSITORY in(" + storeIdStr + ")";
		boolean isHave = false;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int dbSeqId = rs.getInt("SEQ_ID");
				String typeName = YHUtility.null2Empty(rs.getString("TYPE_NAME"));
				buffer.append("{");
				buffer.append("typeId:\"" + dbSeqId + "\"");
				buffer.append(",name:\"" + YHUtility.encodeSpecial(typeName) + "\"");
				buffer.append("},");
				isHave = true;
			}
			if (isHave) {
				buffer = buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return buffer.toString();
	}
	
	

}
