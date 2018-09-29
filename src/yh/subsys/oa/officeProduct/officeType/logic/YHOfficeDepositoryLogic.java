package yh.subsys.oa.officeProduct.officeType.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHFlowHookUtility;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.subsys.oa.book.data.YHPage;
import yh.subsys.oa.officeProduct.commentOffice;
import yh.subsys.oa.officeProduct.officeType.data.YHOfficeDepository;
import yh.subsys.oa.officeProduct.officeType.data.YHOfficeType;
import yh.subsys.oa.officeProduct.person.data.YHOfficeProducts;
import yh.subsys.oa.officeProduct.person.data.YHOfficeTranshistory;
import yh.subsys.oa.officeProduct.person.logic.YHPersonalOfficeRecordLogic;

/**
 * 增加办公用品库
 * 
 * @author Administrator
 * 
 */
public class YHOfficeDepositoryLogic {
	commentOffice comment = new commentOffice();

	public void setOfficeInfoValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();

		String depositoryName = fileForm.getParameter("DEPOSITORY_NAME"); // 库名称
		String deptId = fileForm.getParameter("dept");// 所属部门
		String userManager = fileForm.getParameter("user"); // 库管理员
		String user1 = fileForm.getParameter("user1");// 物品调度员
		// System.out.println(dept+"====="+user);
		try {
			YHOfficeDepository officeDe = new YHOfficeDepository();
			if (!YHUtility.isNullorEmpty(depositoryName)) {
				officeDe.setDepositoryName(depositoryName);
			}
			officeDe.setDeptId(deptId);
			officeDe.setManager(userManager);
			officeDe.setProKeeper(user1);

			orm.saveSingle(dbConn, officeDe);

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 查询办公用品设置
	 * 
	 * @param dbConn
	 * @param user
	 * @param page
	 * @return
	 * @throws Exception
	 */

	public List<YHOfficeDepository> findOfficeDepositorySet(Connection dbConn, YHPerson user) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select SEQ_ID,DEPOSITORY_NAME,OFFICE_TYPE_ID,DEPT_ID,MANAGER,PRO_KEEPER from oa_office_repertory where 1=1";
		List<YHOfficeDepository> offices = new ArrayList<YHOfficeDepository>();
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				YHOfficeDepository office = new YHOfficeDepository();
				office.setSeqId(rs.getInt("SEQ_ID"));
				office.setDepositoryName(rs.getString("DEPOSITORY_NAME"));
				office.setOfficeTypeId(this.getOfficeTypeNamesByIdLogic(dbConn, rs.getString("OFFICE_TYPE_ID")));
				// System.out.println(office.getOfficeTypeId()+"=========");
				// rs.getString("DEPT_ID");
				String deptId = rs.getString("DEPT_ID");
				if (!YHUtility.isNullorEmpty(deptId) && deptId.equals("0") || deptId == "0") {
					office.setDeptId("全体部门");
				} else {
					office.setDeptId(comment.findDept(dbConn, user, rs.getString("DEPT_ID")));
				}
				office.setManager(comment.findManager(dbConn, user, rs.getString("MANAGER")));
				office.setProKeeper(comment.findProKeeper(dbConn, user, rs.getString("PRO_KEEPER")));
				offices.add(office);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return offices;
	}
	
	/**
	 * 获取办公用品类别名称
	 * 2011-3-30
	 * @param dbConn
	 * @param typeIdStr
	 * @return
	 * @throws Exception
	 */
	
	public String getOfficeTypeNamesByIdLogic(Connection dbConn, String typeIdStr) throws Exception {
		StringBuffer buffer = new StringBuffer();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		if (YHUtility.isNullorEmpty(typeIdStr)) {
			typeIdStr = "0";
		}
		if (typeIdStr.endsWith(",")) {
			typeIdStr = typeIdStr.substring(0, typeIdStr.length() - 1);
		}
		String sql = "select SEQ_ID,TYPE_NAME from oa_office_kind  where SEQ_ID in(" + typeIdStr + ")";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			boolean isHave = false;
			while (rs.next()) {
//				int dbSeqId = rs.getInt("SEQ_ID");
				String typeName = YHUtility.null2Empty(rs.getString("TYPE_NAME"));
				buffer.append(typeName + ",");
				isHave = true;
			}
			if (isHave) {
				buffer = buffer.deleteCharAt(buffer.length()-1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return buffer.toString();
	}

	public List<Map<String, String>> findOfficeDepository(Connection dbConn, YHPerson user, int seqId, String typeId, String proSeqId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String deptIds = "select a.seq_id as deptId, a.depository_name, b.seq_id as typeId, b.type_name, c.seq_id as proId, c.pro_name from oa_office_repertory a, oa_office_kind b, oa_office_goods c "
				+ " where a.seq_id ="
				+ seqId
				+ " and b.seq_id="
				+ typeId
				+ " and c.seq_id ="
				+ proSeqId
				+ " and a.seq_id= b.type_depository and b.seq_id= c.office_protype";
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		YHOfficeDepository office = new YHOfficeDepository();
		try {
			ps = dbConn.prepareStatement(deptIds);
			rs = ps.executeQuery();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("deptId", rs.getString("deptId"));
				map.put("deptName", rs.getString("depository_name"));
				map.put("typeId", rs.getString("typeId"));
				map.put("typeName", rs.getString("type_name"));
				map.put("proId", rs.getString("proId"));
				map.put("proName", rs.getString("pro_name"));
				list.add(map);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return list;
	}

	/**
	 * 删除办公用品设置
	 * 
	 * @param conn
	 * @param person
	 * @param noHiddenId
	 * @return
	 * @throws Exception
	 */
	public static int delOfficeDepository(Connection conn, YHPerson person, int noHiddenId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ok = 0;
		String deleteSql = "delete from oa_office_repertory where seq_id =" + noHiddenId;

		try {
			ps = conn.prepareStatement(deleteSql);
			ok = ps.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}

		return ok;
	}

	/**
	 * 修改办公用品库之前先进行查询
	 * 
	 * @param dbConn
	 * @param user
	 * @param licenseSeqId
	 * @return
	 * @throws Exception
	 */
	public YHOfficeDepository officeDepositoryInfo(Connection dbConn, YHPerson user, int officeSeqId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select SEQ_ID,DEPOSITORY_NAME,OFFICE_TYPE_ID,DEPT_ID,MANAGER,PRO_KEEPER from oa_office_repertory where SEQ_ID=" + officeSeqId;
		YHOfficeDepository office = new YHOfficeDepository();
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				office.setSeqId(rs.getInt("SEQ_ID"));
				office.setDepositoryName(rs.getString("DEPOSITORY_NAME"));
				office.setOfficeTypeId(rs.getString("OFFICE_TYPE_ID"));
				office.setDeptId(rs.getString("DEPT_ID"));
				office.setManager(rs.getString("MANAGER"));
				office.setProKeeper(rs.getString("PRO_KEEPER"));
				/*
				 * office.setDeptId(comment.findDept(dbConn, user,
				 * rs.getString("DEPT_ID")));
				 * office.setManager(comment.findManager(dbConn,
				 * user,rs.getString("MANAGER")));
				 * office.setProKeeper(comment.findProKeeper(dbConn,
				 * user,rs.getString("PRO_KEEPER")));
				 */
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return office;
	}

	/**
	 * 修改办公库设置
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public static int updateLicenseInfo(Connection conn, YHPerson person, YHOfficeDepository office) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ok = 0;
		String updateSql = "update oa_office_repertory set DEPOSITORY_NAME=?, OFFICE_TYPE_ID=?,DEPT_ID=?,MANAGER=?,PRO_KEEPER=?" + " where SEQ_ID=?";

		try {
			ps = conn.prepareStatement(updateSql);
			ps.setString(1, office.getDepositoryName());
			ps.setString(2, office.getOfficeTypeId());
			ps.setString(3, office.getDeptId());
			ps.setString(4, office.getManager());
			ps.setString(5, office.getProKeeper());
			ps.setInt(6, office.getSeqId());
			ok = ps.executeUpdate();
		} catch (Exception e) {
			throw e;
		}
		return ok;
	}

	public void setUpOfficeInfoLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String seqid = fileForm.getParameter("seqid");
		String depositoryName = fileForm.getParameter("DEPOSITORY_NAME"); // 库名称
		String deptId = fileForm.getParameter("dept");// 所属部门
		String userManager = fileForm.getParameter("user"); // 库管理员
		String user1 = fileForm.getParameter("user1");// 物品调度员
		Map typeName = fileForm.getParamMap();// 类别名称
		try {
			YHOfficeDepository office = new YHOfficeDepository();
			office.setSeqId(Integer.valueOf(seqid));
			office.setDepositoryName(depositoryName);
			office.setDeptId(deptId);
			office.setManager(userManager);
			office.setProKeeper(user1);
			// office.setOfficeTypeId(typeName);
			orm.updateSingle(dbConn, office);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询办公用品设置
	 * 
	 * @param dbConn
	 * @param user
	 * @param page
	 * @return
	 * @throws Exception
	 */

	public List<YHOfficeDepository> findOfficeDepositoryInfo(Connection dbConn, YHPerson user) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select SEQ_ID,DEPOSITORY_NAME,OFFICE_TYPE_ID,DEPT_ID,MANAGER,PRO_KEEPER from oa_office_repertory where 1=1";
		List<YHOfficeDepository> offices = new ArrayList<YHOfficeDepository>();
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				YHOfficeDepository office = new YHOfficeDepository();
				office.setSeqId(rs.getInt("SEQ_ID"));
				office.setDepositoryName(rs.getString("DEPOSITORY_NAME"));
				office.setOfficeTypes(getOfficeTypeInfo(dbConn, user, String.valueOf(office.getSeqId())));// 获得类别
				office.setOfficeTypeId(rs.getString("OFFICE_TYPE_ID"));
				offices.add(office);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return offices;
	}

	/**
	 * 通过办公用品库 获得办公类型(第二个标签)
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public static List<YHOfficeType> getOfficeTypeInfo(Connection conn, YHPerson user, String officeId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int SysTen = 0;
		String findSeq = " select SEQ_ID, TYPE_NAME from oa_office_kind where type_depository = " + officeId + "";
		List<YHOfficeType> officeTypes = new ArrayList<YHOfficeType>();
		try {
			ps = conn.prepareStatement(findSeq);
			rs = ps.executeQuery();
			while (rs.next()) {
				YHOfficeType officeType = new YHOfficeType();
				officeType.setSeqId(rs.getInt("SEQ_ID"));
				officeType.setTypeName(rs.getString("TYPE_NAME"));
				officeType.setProducts(getOfficeProductsInfo(conn, user, String.valueOf(officeType.getSeqId())));
				officeTypes.add(officeType);
			}

		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return officeTypes;
	}

	/**
	 * 通过办公用品类型 获得办公用品（第二个标签）
	 * 
	 * @param conn
	 * @param user
	 * @param officeId
	 * @return
	 * @throws Exception
	 */
	public static List<YHOfficeProducts> getOfficeProductsInfo(Connection conn, YHPerson user, String officeId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String findSql = "select SEQ_ID, PRO_NAME,PRO_STOCK from oa_office_goods where OFFICE_PROTYPE =" + officeId + "";
		List<YHOfficeProducts> products = new ArrayList<YHOfficeProducts>();
		try {
			ps = conn.prepareStatement(findSql);
			rs = ps.executeQuery();

			while (rs.next()) {
				YHOfficeProducts product = new YHOfficeProducts();
				product.setSeqId(rs.getInt("SEQ_ID"));
				product.setProName(rs.getString("PRO_NAME"));
				product.setProStock(rs.getInt("PRO_STOCK"));
				products.add(product);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return products;
	}

	/**
	 * 增加办公用品批量申请
	 * 
	 * @param conn
	 * @param person
	 * @param office
	 * @return
	 * @throws Exception
	 */
	public int newOfficeProducts(Connection conn, YHPerson person, YHOfficeTranshistory office) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ok = 0;
		String sql = "insert into oa_office_trans_records (PRO_ID,BORROWER,TRANS_FLAG,TRANS_QTY,PRICE,REMARK,TRANS_DATE,OPERATOR,TRANS_STATE,DEPT_ID,CYCLE_NO,CYCLE,RUNID,FACT_QTY) values"
				+ " (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, office.getProId());
			ps.setString(2, office.getBorrower());
			ps.setString(3, office.getTransFlag());
			ps.setInt(4, office.getTransQty());
			ps.setInt(5, office.getPrice());
			ps.setString(6, office.getRemark());
			ps.setDate(7, new java.sql.Date(office.getTransDate().getTime()));
			ps.setString(8, office.getOperator());
			ps.setString(9, office.getTransState());
			ps.setInt(10, office.getDeptId());
			ps.setInt(11, office.getCycleNo());
			ps.setString(12, office.getCycle());
			ps.setInt(13, 0);
			ps.setInt(14, office.getFactQty());
			ok = ps.executeUpdate();
			if (ok != 0) {
				getOfficeTranshistoryInfo(conn, person, office);// 增加办公用品批量申请后 发送提示内部信息
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return ok;
	}

	public static void getOfficeTranshistoryInfo(Connection conn, YHPerson user, YHOfficeTranshistory office) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String proAuditer = "";
		String manager = "";
		String recordType = "";
		String smsContent = "";
		String proKeeper = "";
		recordType = office.getTransFlag();
		String findSql = "select a.PRO_AUDITER,c.MANAGER,c.PRO_KEEPER from oa_office_goods a left outer join oa_office_kind b on"
				+ " a.OFFICE_PROTYPE=b.seq_id left outer join oa_office_repertory c on b.TYPE_DEPOSITORY= c.seq_id where a.seq_id= " + office.getProId();

		try {
			ps = conn.prepareStatement(findSql);
			rs = ps.executeQuery();

			while (rs.next()) {
				proAuditer = rs.getString("PRO_AUDITER");
				manager = rs.getString("MANAGER");
				proKeeper = rs.getString("PRO_KEEPER");
			}
			if (recordType.equals("1")) {
				recordType = "领用";
			}
			if (recordType.equals("2")) {
				recordType = "借用";
			}
			if (recordType.equals("3")) {
				recordType = "归还";
			}
			smsContent = "请审批" + user.getUserName() + "的办公用品" + recordType + "申请。";
			String url = "/subsys/oa/officeProduct/manage/transInfo.jsp";
			if (!YHUtility.isNullorEmpty(proAuditer)) {
				YHPersonalOfficeRecordLogic.sendSms(user, conn, smsContent, url, proAuditer, null, "43");
			}
			if (YHUtility.isNullorEmpty(proAuditer) && !YHUtility.isNullorEmpty(manager)) {
				YHPersonalOfficeRecordLogic.sendSms(user, conn, smsContent, url, manager, null, "43");
			}
//      if (!YHUtility.isNullorEmpty(proKeeper)) {
//        YHPersonalOfficeRecordLogic.sendSms(user, conn, smsContent, url, proKeeper, null, "43");
//      }
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
	}

	/**
	 * 获取最大 申请标记
	 * 
	 * @param conn
	 * @param person
	 * @param office
	 * @return
	 * @throws Exception
	 */
	public int getCycleNo(Connection conn, YHPerson person) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ok = 0;
		String sql = "select max(CYCLE_NO) as CYCLE_NO from oa_office_trans_records";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("CYCLE_NO");
			}

		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return 0;
	}

	/**
	 * 查询共有多少条 办公用品登记信息
	 * 
	 * @param dbConn
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public int count(Connection dbConn, YHPerson user) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select COUNT(*) from oa_office_trans_records";

		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return 0;
	}

	/**
	 * 办公用品登记信息查询 （带分页）
	 * 
	 * @param dbConn
	 * @param user
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public Map findofficeRecordInfoLogic(Connection dbConn, YHPerson user, YHPage page) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String transName = "";// 登记类型名称
		String transStateName = "";// 状态名称		Map result = new HashMap();
		Map map2 = new HashMap();
		 
		String sql = "select SEQ_ID,PRO_ID,BORROWER,TRANS_FLAG,TRANS_QTY,PRICE,REMARK,TRANS_DATE,OPERATOR,TRANS_STATE,FACT_QTY,REASON,"
				+ " CYCLE,CYCLE_NO,COMPANY,BAND,DEPT_ID from oa_office_trans_records where 1=1 AND (BORROWER != '' or BORROWER is not null) AND OPERATOR = '"+ user.getSeqId() +"' order by SEQ_ID desc";
		List<YHOfficeTranshistory> office = new ArrayList<YHOfficeTranshistory>();
		try {
			ps = dbConn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ps.setMaxRows(page.getCurrentPageIndex() * page.getPageSize());// 游标用法，预显示最大行数
			rs = ps.executeQuery();
			rs.first();// 如指针用法，指向最上面位置
			 YHFlowHookUtility fu = new YHFlowHookUtility();
	      YHFlowRunUtility ru = new YHFlowRunUtility();
	     
			rs.relative((page.getCurrentPageIndex() - 1) * page.getPageSize() - 1);// 相对于求出每页显示的长度
			while (rs.next()) {
				YHOfficeTranshistory officeT = new YHOfficeTranshistory();
				officeT.setSeqId(rs.getInt("SEQ_ID"));
				officeT.setProId(rs.getInt("PRO_ID"));
				officeT.setTransFlag(rs.getString("TRANS_FLAG"));
				officeT.setTransQty(Math.abs(rs.getInt("TRANS_QTY")));
				officeT.setRemark(rs.getString("REMARK"));
				officeT.setTransDate(rs.getDate("TRANS_DATE"));
				officeT.setTransState(rs.getString("TRANS_STATE"));
				officeT.setReason(rs.getString("REASON"));
				officeT.setOfficeProductName(YHOfficeDepositoryLogic.getProductName(dbConn, officeT.getProId()));// 通过PRO_ID
																																																					// 查询office_products表中的name
        if (officeT.getTransFlag().equals("0")) {
          transName = "采购";
        }
        else if (officeT.getTransFlag().equals("1")) {
					transName = "领用";
				}
        else if (officeT.getTransFlag().equals("2")) {
					transName = "借用";
				}
        else if (officeT.getTransFlag().equals("3")) {
					transName = "归还";
				}
        else if (officeT.getTransFlag().equals("4")) {
          transName = "报废";
        }
        else if (officeT.getTransFlag().equals("5")) {
          transName = "维护";
        }
        else {
          transName = "";
        }
				officeT.setTransName(transName);// 登记类型名称
				if (!YHUtility.isNullorEmpty(officeT.getTransState())) {
					if (officeT.getTransState().equals("0")) {
						transStateName = "待批准";
					}
					if (officeT.getTransState().equals("1")) {
						transStateName = "已批准";
					}
					if (officeT.getTransState().equals("2")) {
						transStateName = "被驳回";
					}
					if (officeT.getTransState().equals("3")) {
						transStateName = "已归还";
					}
				}
				officeT.setTransStateName(transStateName);
				office.add(officeT);
				
				
				int runId = fu.isRunHook(dbConn, "TRANS_ID", officeT.getSeqId() + "");
        int flowId = 0;
        if (runId != 0) {
          flowId = ru.getFlowId(dbConn, runId);
        }
        Map map = new HashMap();
        map.put("runId", runId);
        map.put("flowId", flowId);
        map2.put(officeT.getSeqId(), map);
				// licenseC.setStaffName(hrPublicIdTransName.getUserName(dbConn,
				// Integer.valueOf(rs.getString("STAFF_NAME"))));
				// licenseC.setLicenseType(rs.getString("LICENSE_TYPE"));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		result.put("list", office);
		result.put("flow", map2);
		return result;
	}

	/**
	 * 通过PRO_ID 获取办公用品名称
	 * 
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public static String getProductName(Connection dbConn, int proId) throws Exception {
		String sql = "SELECT PRO_NAME from oa_office_goods where SEQ_ID =" + proId;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("PRO_NAME");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, null, null);
		}
		return null;
	}

	/**
	 * 删除办公用品登记信息的单条信息
	 * 
	 * @param conn
	 * @param person
	 * @param noHiddenId
	 * @return
	 * @throws Exception
	 */
	public static int deleteofficeRecordInfoLogic(Connection conn, YHPerson person, int noHiddenId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int ok = 0;
		String deleteSql = "delete from oa_office_trans_records where SEQ_ID =" + noHiddenId;
		try {
			ps = conn.prepareStatement(deleteSql);
			ok = ps.executeUpdate();
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}

		return ok;
	}

	/**
	 * 查询办公用品登记信息的详细信息
	 * 
	 * @param dbConn
	 * @param user
	 * @param ContractSeqId
	 * @return
	 * @throws Exception
	 */
	public List<YHOfficeTranshistory> findOfficeXxInfoLogic(Connection dbConn, YHPerson user, int officeId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select SEQ_ID,PRO_ID,BORROWER,TRANS_FLAG,TRANS_QTY,PRICE,REMARK,TRANS_DATE,OPERATOR,TRANS_STATE,FACT_QTY,"
				+ " REASON,CYCLE,CYCLE_NO,COMPANY,BAND,DEPT_ID from oa_office_trans_records where SEQ_ID=" + officeId;

		List<YHOfficeTranshistory> officets = new ArrayList<YHOfficeTranshistory>();
		// YHORM orm =new YHORM();
		try {
			// YHOfficeTranshistory sle= (YHOfficeTranshistory)
			// orm.loadObjSingle(dbConn, YHOfficeTranshistory.class, officeId);
			// officets.add(sle);
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				YHOfficeTranshistory office = new YHOfficeTranshistory();
				office.setSeqId(rs.getInt("SEQ_ID"));
				office.setProId(rs.getInt("PRO_ID"));
				office.setTransFlag(rs.getString("TRANS_FLAG"));
				String transFlag = office.getTransFlag();
				if("1".equals(transFlag) || "2".equals(transFlag) || "3".equals(transFlag) || "4".equals(transFlag)){
				  office.setTransQty(-rs.getInt("TRANS_QTY"));
				}
				office.setRemark(rs.getString("REMARK"));
				office.setTransDate(rs.getDate("TRANS_DATE"));
				office.setTransState(rs.getString("TRANS_STATE"));
				office.setBorrower(rs.getString("BORROWER"));
				office.setFactQty(rs.getInt("FACT_QTY"));
				office.setOperator(rs.getString("OPERATOR"));
				officets.add(office);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return officets;
	}

	/**
	 * 通过oa_office_trans_records 表中的pro_id 查询出办公用品表（office_products）中的详细信息
	 * 
	 * @param dbConn
	 * @param user
	 * @param officeProductsId
	 * @return
	 * @throws Exception
	 */
	public List<YHOfficeProducts> findOfficeProductsXxInfoLogic(Connection dbConn, YHPerson user, int officeProductsId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select SEQ_ID,PRO_NAME,PRO_DESC,OFFICE_PROTYPE, PRO_CODE,PRO_UNIT,PRO_PRICE,PRO_SUPPLIER,PRO_LOWSTOCK,PRO_MAXSTOCK,"
				+ " PRO_STOCK,PRO_DEPT,PRO_MANAGER,PRO_CREATOR,PRO_AUDITER from oa_office_goods where SEQ_ID=" + officeProductsId;
		List<YHOfficeProducts> officetProducts = new ArrayList<YHOfficeProducts>();
		YHORM orm = new YHORM();
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				YHOfficeProducts officeProduct = new YHOfficeProducts();
				officeProduct.setSeqId(rs.getInt("SEQ_ID"));
				officeProduct.setProName(rs.getString("PRO_NAME"));
				officeProduct.setOfficeProtype(rs.getString("OFFICE_PROTYPE"));
				officeProduct.setProPrice(rs.getDouble("PRO_PRICE"));
				officeProduct.setProLowstock(rs.getInt("PRO_LOWSTOCK"));
				officeProduct.setProMaxstock(rs.getInt("PRO_MAXSTOCK"));
				officeProduct.setProStock(rs.getInt("PRO_STOCK"));
				officetProducts.add(officeProduct);
			}

			/*
			 * Map map =new HashMap(); map.put("", officeProductsId); YHOfficeProducts
			 * sle= (YHOfficeProducts) orm.loadObjSingle(dbConn,
			 * YHOfficeProducts.class, officeProductsId); officetProducts.add(sle);
			 */
		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return officetProducts;
	}

	/**
	 * 通过office_products 表中的OFFICE_PROTYPE 查询出办公用品类型表（type_name）中的详细信息
	 * 
	 * @param dbConn
	 * @param user
	 * @param officeProductsId
	 * @return
	 * @throws Exception
	 */
	public YHOfficeType findOfficeTypesXxInfoLogic(Connection dbConn, YHPerson user, int officeTypeId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select SEQ_ID,TYPE_NAME,TYPE_ORDER,TYPE_PARENT_ID,TYPE_DEPOSITORY from oa_office_kind where SEQ_ID=" + officeTypeId;
		List<YHOfficeType> officetTypes = new ArrayList<YHOfficeType>();
		YHOfficeType officeType = new YHOfficeType();
		try {// YHORM orm =new YHORM();
			// sle= (YHOfficeType) orm.loadObjSingle(dbConn, YHOfficeType.class,
			// officeTypeId);
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {

				officeType.setSeqId(rs.getInt("SEQ_ID"));
				officeType.setTypeName(rs.getString("TYPE_NAME"));
				officeType.setTypeOrder(rs.getString("TYPE_ORDER"));
				officeType.setTypeParentId(rs.getInt("TYPE_PARENT_ID"));
				officeType.setTypeDepository(rs.getInt("TYPE_DEPOSITORY"));
				// officetTypes.add(officeType);
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, null);
		}
		return officeType;
	}

	/**
	 * 部门领用汇总
	 * 
	 * @param toDate
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getGiftOutByDeptId(Connection dbConn, String deptId) throws Exception {
		//System.out.println(deptId + "==========================");
		String sql = "select a.seq_id, a.depository_name, b.type_name, c.pro_name from oa_office_repertory a left outer join"
				+ " oa_office_kind b on a.seq_id = b.type_depository left outer join oa_office_goods c on b.seq_id = c.office_protype  where "
				+ YHDBUtility.findInSet(deptId, "a.dept_id");
		Statement stmt = null;
		ResultSet rs = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		try {
			stmt = dbConn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("seqId", rs.getString("seq_id"));
				map.put("depositoryName", rs.getString("depository_name"));
				map.put("typeName", rs.getString("type_name"));
				map.put("proName", rs.getString("pro_name"));
				list.add(map);

				/*
				 * for(int i=0; i<list.size(); i++){
				 * System.out.println(list.get(i).get("")); }
				 */
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stmt, rs, null);
		}
		return list;
	}

}
