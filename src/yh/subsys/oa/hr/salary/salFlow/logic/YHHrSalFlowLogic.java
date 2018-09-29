package yh.subsys.oa.hr.salary.salFlow.logic;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.email.logic.YHEmailUtil;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.salary.insurancePara.salItem.data.YHSalItem;
import yh.subsys.oa.hr.salary.salFlow.data.YHSalFlow;

public class YHHrSalFlowLogic {

	private static Logger log = Logger.getLogger(YHHrSalFlowLogic.class);

	/**
	 * 新建工资流程
	 * 
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void setNewSalFlowValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String beginDateStr = fileForm.getParameter("beginDate");
		String endDateStr = fileForm.getParameter("endDate");
		String salYear = fileForm.getParameter("salYear");
		String salMonth = fileForm.getParameter("salMonth");
		String remark = fileForm.getParameter("remark");
		String smsRemind = fileForm.getParameter("smsRemind");
		String sms2Remind = fileForm.getParameter("sms2Remind");

		try {
			YHSalFlow salFlow = new YHSalFlow();
			if (!YHUtility.isNullorEmpty(beginDateStr)) {
				Date beginDate = YHUtility.parseDate("yyyy-MM-dd", beginDateStr);
				salFlow.setBeginDate(beginDate);
			}
			if (!YHUtility.isNullorEmpty(endDateStr)) {
				Date endDate = YHUtility.parseDate("yyyy-MM-dd", endDateStr);
				salFlow.setEndDate(endDate);
			}
			salFlow.setSalYear(salYear);
			salFlow.setSalMonth(salMonth);
			salFlow.setContent(remark);
			salFlow.setIssend("0");
			salFlow.setSendTime(YHUtility.parseTimeStamp());
			salFlow.setSalCreater(String.valueOf(person.getSeqId()));
			orm.saveSingle(dbConn, salFlow);
			int maxSeqId = this.getMaxSeqId(dbConn);
			YHMobileSms2Logic sbl = new YHMobileSms2Logic();
			String remindUrl = "/subsys/oa/hr/salary/manage/salFlow/manage.jsp?openFlag=1&openWidth=860&openHeight=650";
			String smsContent = "请进行工资上报！\n备注：" + remark;
			String getMenuManager = getMenuManager(dbConn, person);
			// 短信提醒
			if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
				this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), getMenuManager, "4", remindUrl, new Date());
			}
			// 手机提醒
			if (!YHUtility.isNullorEmpty(sms2Remind) && "1".equals(sms2Remind.trim())) {
				smsContent = "请进行工资上报！\n备注：" + remark;
				sbl.remindByMobileSms(dbConn, getMenuManager, person.getSeqId(), smsContent, new Date());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 工资流程 通用列表
	 * 
	 * @param dbConn
	 * @param request
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getSalFlowJsonLogic(Connection dbConn, Map request, YHPerson person) throws Exception {
		try {
			String sql = null;
			String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
			if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
			  sql = " select s1.SEQ_ID,s1.SEND_TIME,p1.USER_NAME,s1.BEGIN_DATE,s1.END_DATE,s1.SAL_YEAR + '年' + s1.SAL_MONTH + '月',s1.CONTENT,s1.ISSEND "
          + " from oa_sal_flow s1 " + " join person p1 on s1.SAL_CREATER = p1.SEQ_ID " + " ORDER BY s1.SEQ_ID desc ";
			}
			else if(dbms.equals(YHConst.DBMS_MYSQL)){
        sql = " select s1.SEQ_ID,s1.SEND_TIME,p1.USER_NAME,s1.BEGIN_DATE,s1.END_DATE,CONCAT(s1.SAL_YEAR , '年' , s1.SAL_MONTH , '月'),s1.CONTENT,s1.ISSEND "
          + " from oa_sal_flow s1 " + " join person p1 on s1.SAL_CREATER = p1.SEQ_ID " + " ORDER BY s1.SEQ_ID desc ";
			}
			else {
			  sql = " select s1.SEQ_ID,s1.SEND_TIME,p1.USER_NAME,s1.BEGIN_DATE,s1.END_DATE,s1.SAL_YEAR || '年' || s1.SAL_MONTH || '月',s1.CONTENT,s1.ISSEND "
          + " from oa_sal_flow s1 " + " join person p1 on s1.SAL_CREATER = p1.SEQ_ID " + " ORDER BY s1.SEQ_ID desc ";
			}
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 启用/终止 更改工资流程状态
	 * 
	 * 
	 * @param dbConn
	 * @param seqId
	 * @param flag
	 * @return
	 * @throws Exception
	 */
	public void doIssend(Connection dbConn, String seqId, String flag) {
		String sql = "";

		if (flag.equals("1")) {
			sql = " update oa_sal_flow  set ISSEND = " + flag + ",END_DATE = ? where SEQ_ID = " + seqId;
		} else {
			sql = " update oa_sal_flow  set ISSEND = " + flag + ",END_DATE = null where SEQ_ID = " + seqId;
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			if (flag.equals("1")) {
				ps.setTimestamp(1, new Timestamp(new Date().getTime()));
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
	}

	/**
	 * 获取详情
	 * 
	 * @param conn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public YHSalFlow getSalFlowDetailLogic(Connection conn, int seqId) throws Exception {
		try {
			YHORM orm = new YHORM();
			return (YHSalFlow) orm.loadObjSingle(conn, YHSalFlow.class, seqId);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 编辑工资流程
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @throws Exception
	 */
	public void updateSalFlowInfoLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String seqIdStr = fileForm.getParameter("seqId");
		String beginDateStr = fileForm.getParameter("beginDate");
		String endDateStr = fileForm.getParameter("endDate");
		String salYear = fileForm.getParameter("salYear");
		String salMonth = fileForm.getParameter("salMonth");
		String remark = fileForm.getParameter("remark");
		String smsRemind = fileForm.getParameter("smsRemind");
		String sms2Remind = fileForm.getParameter("sms2Remind");

		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}

		try {
			YHSalFlow salFlow = (YHSalFlow) orm.loadObjSingle(dbConn, YHSalFlow.class, seqId);
			if (salFlow != null) {
				if (!YHUtility.isNullorEmpty(beginDateStr)) {
					Date beginDate = YHUtility.parseDate("yyyy-MM-dd", beginDateStr);
					salFlow.setBeginDate(beginDate);
				}
				if (!YHUtility.isNullorEmpty(endDateStr)) {
					Date endDate = YHUtility.parseDate("yyyy-MM-dd", endDateStr);
					salFlow.setEndDate(endDate);
				}
				salFlow.setSalYear(salYear);
				salFlow.setSalMonth(salMonth);
				salFlow.setContent(remark);
				salFlow.setSendTime(YHUtility.parseTimeStamp());
				orm.updateSingle(dbConn, salFlow);
				YHMobileSms2Logic sbl = new YHMobileSms2Logic();
				String remindUrl = "/subsys/oa/hr/salary/manage/salFlow/manage.jsp?openFlag=1&openWidth=860&openHeight=650";
				String smsContent = "请进行工资上报！\n备注：" + remark;
				String getMenuManager = getMenuManager(dbConn, person);
				// 短信提醒
				if (!YHUtility.isNullorEmpty(smsRemind) && "1".equals(smsRemind.trim())) {
					this.doSmsBackTime(dbConn, smsContent, person.getSeqId(), getMenuManager, "4", remindUrl, new Date());
				}
				// 手机提醒
				if (!YHUtility.isNullorEmpty(sms2Remind) && "1".equals(sms2Remind.trim())) {
					smsContent = "请进行工资上报！\n备注：" + remark;
					sbl.remindByMobileSms(dbConn, getMenuManager, person.getSeqId(), smsContent, new Date());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 删除工资流程
	 * 
	 * @param dbConn
	 * @param seqIdStr
	 * @throws Exception
	 */
	public void deleteFileLogic(Connection dbConn, String seqIdStr) throws Exception {
		YHORM orm = new YHORM();
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			seqIdStr = "";
		}
		try {
			String seqIdArry[] = seqIdStr.split(",");
			if (!"".equals(seqIdArry) && seqIdArry.length > 0) {
				for (String seqId : seqIdArry) {
					YHSalFlow salFlow = (YHSalFlow) orm.loadObjSingle(dbConn, YHSalFlow.class, Integer.parseInt(seqId));

					// 删除数据库信息

					orm.deleteSingle(dbConn, salFlow);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public String getMenuManager(Connection dbConn, YHPerson person) {
		String sql = null;
		String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
		if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
		  sql = " select SEQ_ID from PERSON where USER_PRIV in ( " + " select SEQ_ID from USER_PRIV  " + " where FUNC_ID_STR like  "
      + " '%'+(select MENU_ID from oa_sys_func " + " where FUNC_NAME = '财务工资录入' )+'%' or "
      + " FUNC_ID_STR like '%'+(select MENU_ID from oa_sys_func " + " where FUNC_NAME = '部门工资上报' )+'%') ";
		}
		else if(dbms.equals(YHConst.DBMS_MYSQL)){
      sql = " select SEQ_ID from PERSON where USER_PRIV in ( " + " select SEQ_ID from USER_PRIV  " + " where FUNC_ID_STR like  "
      + " CONCAT('%',(select MENU_ID from oa_sys_func " + " where FUNC_NAME = '财务工资录入' ),'%') or "
      + " FUNC_ID_STR like CONCAT('%',(select MENU_ID from oa_sys_func " + " where FUNC_NAME = '部门工资上报' ),'%')) ";
		}
		else {
		  sql = " select SEQ_ID from PERSON where USER_PRIV in ( " + " select SEQ_ID from USER_PRIV  " + " where FUNC_ID_STR like  "
      + " '%'||(select MENU_ID from oa_sys_func " + " where FUNC_NAME = '财务工资录入' )||'%' or "
      + " FUNC_ID_STR like '%'||(select MENU_ID from oa_sys_func " + " where FUNC_NAME = '部门工资上报' )||'%') ";
		}
		String menuManager = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				menuManager += rs.getString("SEQ_ID") + ",";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		return menuManager;
	}

	/**
	 * 获取最大的SeqId值
	 * 
	 * 
	 * @param dbConn
	 * @return
	 */
	public int getMaxSeqId(Connection dbConn) {
		String sql = "select SEQ_ID from oa_pm_employee_transfer where SEQ_ID=(select MAX(SEQ_ID) from oa_pm_employee_transfer )";
		int seqId = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				seqId = rs.getInt("SEQ_ID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		return seqId;
	}

	/**
	 * 短信提醒(带时间)
	 * 
	 * @param conn
	 * @param content
	 * @param fromId
	 * @param toId
	 * @param type
	 * @param remindUrl
	 * @param sendDate
	 * @throws Exception
	 */
	public static void doSmsBackTime(Connection conn, String content, int fromId, String toId, String type, String remindUrl, Date sendDate)
			throws Exception {
		YHSmsBack sb = new YHSmsBack();
		sb.setContent(content);
		sb.setFromId(fromId);
		sb.setToId(toId);
		sb.setSmsType(type);
		sb.setRemindUrl(remindUrl);
		sb.setSendDate(sendDate);
		YHSmsUtil.smsBack(conn, sb);
	}

	/**
	 * 下载CSV模板
	 * 
	 * @return
	 * @throws Exception
	 */
	public ArrayList<YHDbRecord> downCSVTempletLogic(Connection dbConn) throws Exception {
		ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
		YHDbRecord record = new YHDbRecord();

		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT SLAITEM_ID,ITEM_NAME from oa_sal_item ORDER BY SLAITEM_ID";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			record.addField("用户名", "");
			record.addField("姓名", "");
			while (rs.next()) {
				String itemName = YHUtility.null2Empty(rs.getString("ITEM_NAME"));
				record.addField(itemName, "");
			}
			result.add(record);
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return result;
	}

	/**
	 * 导入CSV工资数据
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @param buffer
	 * @return
	 * @throws Exception
	 */
	public Map<Object, Object> impTransInfoToCsvLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person, StringBuffer buffer)
			throws Exception {
		Map<Object, Object> returnMap = new HashMap<Object, Object>();
		int isCount = 0;
		int updateCount = 0;
		PreparedStatement stmt = null;
		String sql = "";
		try {
			Map<Object, Object> bufferMap = new HashMap<Object, Object>();
			String infoStr = "";
			InputStream is = fileForm.getInputStream();
			String flowIdStr = fileForm.getParameter("flowId");
			int flowId = 0;
			if (YHUtility.isNumber(flowIdStr)) {
				flowId = Integer.parseInt(flowIdStr);
			}
			// 清除本次流程之前导入的数据
			this.delSalDataByFlowId(dbConn, flowId);

			ArrayList<YHDbRecord> dbRecords = YHCSVUtil.CVSReader(is, YHConst.CSV_FILE_CODE);
			for (YHDbRecord record : dbRecords) {
				int fieldCount = record.getFieldCnt();
				String userName = "";
				String userId = "";
				String keyStr = "";
				String valueStr = "";
				int usefulColumn = 0;
				if (fieldCount > 0) {
					for (int i = 0; i < fieldCount; i++) {
						String keyName = record.getNameByIndex(i);
						String value = (String) record.getValueByIndex(i);

						if (!"用户名".equals(keyName.trim()) && !"姓名".equals(keyName.trim())) {
							usefulColumn++;
							keyStr += "S" + usefulColumn + ",";
	            if(!YHUtility.isNumber(value)){
	              value = "0"; 
	            }
							valueStr += "'" + value + "',";

						} else if ("姓名".equals(keyName.trim())) {
							userName = value;
						} else if ("用户名".equals(keyName.trim())) {
							userId = value;
						}
					}

					if (keyStr.endsWith(",")) {
						keyStr = keyStr.substring(0, keyStr.length() - 1);
					}
					if (valueStr.endsWith(",")) {
						valueStr = valueStr.substring(0, valueStr.length() - 1);
					}
					Map<Object, Object> map = this.getPersonCountByUserName(dbConn, userId);

					boolean isHave = (Boolean) map.get("isHave");
					int personSeqId = (Integer) map.get("personId");
					if (!isHave) {
						String nameStr = "";
						if (!YHUtility.isNullorEmpty(userId)) {
							nameStr = userId;
						} else {
							nameStr = userName;
						}
						if(nameStr == null){
						  nameStr = "";
						}
						infoStr = "员工" + nameStr + "尚未在OA系统中注册！！";
						bufferMap.put("userId", userId);
						bufferMap.put("userName", userName);
						bufferMap.put("infoStr", infoStr);
						bufferMap.put("color", "red");
						this.sbStrJson(buffer, bufferMap);
						continue;
					}
					// csv文件需包含有效的工资项目
					if (usefulColumn > 0) {
						if (flowId != 0) {
							sql = "insert into oa_sal_data(FLOW_ID,USER_ID," + keyStr.trim() + " ) values (" + flowId + ", " + personSeqId + ", " + valueStr + " )";
							stmt = dbConn.prepareStatement(sql);
							stmt.executeUpdate();
							isCount++;

							infoStr = "员工" + userId + "的工资导入完成！！";
							bufferMap.put("userId", userId);
							bufferMap.put("userName", userName);
							bufferMap.put("infoStr", infoStr);
							bufferMap.put("color", "green");
							this.sbStrJson(buffer, bufferMap);
						} else {
							bufferMap.put("userId", userId);
							bufferMap.put("userName", userName);
							infoStr = "flowId不能为0!";
							bufferMap.put("infoStr", infoStr);
							bufferMap.put("color", "red");
							this.sbStrJson(buffer, bufferMap);
							break;
						}

					} else {
						bufferMap.put("userId", userId);
						bufferMap.put("userName", userName);
						infoStr = "工资项目没有定义!";
						bufferMap.put("infoStr", infoStr);
						bufferMap.put("color", "red");
						this.sbStrJson(buffer, bufferMap);
						break;
					}
				}
			}
			returnMap.put("isCount", isCount);
			returnMap.put("updateCount", updateCount);
			return returnMap;
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, null, log);
		}
	}

	public void delSalDataByFlowId(Connection dbConn, int flowId) throws Exception {
		PreparedStatement stmt = null;
		String sql = "DELETE FROM oa_sal_data WHERE FLOW_ID =" + flowId;
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, null, log);
		}
	}

	public Map<Object, Object> getPersonCountByUserName(Connection dbConn, String userName) throws Exception {
		Map<Object, Object> map = new HashMap<Object, Object>();
		boolean flag = false;
		int dbSeqId = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT SEQ_ID,USER_ID,USER_NAME from  PERSON where USER_ID=?";
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, YHUtility.null2Empty(userName));
			rs = stmt.executeQuery();
			if (rs.next()) {
				dbSeqId = rs.getInt("SEQ_ID");
				flag = true;
			}
			map.put("isHave", flag);
			map.put("personId", dbSeqId);
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return map;
	}

	public String getCountByNameAndUserId(Connection dbConn, String userName, String userId) throws Exception {
		String str = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT USER_ID from  PERSON where USER_NAME=? and USER_ID=?";
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, YHUtility.null2Empty(userName));
			stmt.setString(2, YHUtility.null2Empty(userId));
			rs = stmt.executeQuery();
			if (rs.next()) {
				str = rs.getString("USER_ID");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return str;
	}

	public int getSalDataCountByFlowIdLogic(Connection dbConn, int flowId) throws Exception {
		int count = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT count(SEQ_ID) from oa_sal_data where FLOW_ID=" + flowId;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return count;
	}

	public String sbStrJson(StringBuffer sb, Map<Object, Object> map) throws Exception {
		String userId = (String) map.get("userId");
		String userName = (String) map.get("userName");
		String infoStr = (String) map.get("infoStr");
		String color = (String) map.get("color");
		try {
			sb.append("{");
			sb.append("userId:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(userId)) + "\"");
			sb.append(",userName:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(userName)) + "\"");
			sb.append(",infoStr:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(infoStr)) + "\"");
			sb.append(",color:\"" + YHUtility.null2Empty(color) + "\"");
			sb.append("},");
			return sb.toString();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取导出可选字段
	 * 
	 * @param dbConn
	 * @param flowId
	 * @return
	 * @throws Exception
	 */
	public String getSalItemNamesLogic(Connection dbConn, int flowId) throws Exception {
		StringBuffer buffer = new StringBuffer("[");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT SLAITEM_ID,ITEM_NAME,ISPRINT from oa_sal_item where ISPRINT='1' ORDER BY SLAITEM_ID";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			boolean isHave = false;
			while (rs.next()) {
				String itemId = "S" + rs.getInt("SLAITEM_ID");
				String itemName = YHUtility.null2Empty(rs.getString("ITEM_NAME"));

				buffer.append("{");
				buffer.append("value:'" + itemId + "'");
				buffer.append(",text:'" + YHUtility.encodeSpecial(itemName) + "'");
				buffer.append("},");
				isHave = true;
			}
			if (isHave) {
				buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
			return buffer.toString();
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}

	/**
	 * 获取工资报表
	 * 
	 * @param Dbconn
	 * @return
	 * @throws Exception
	 */
	public String getSalItemTableLogic(Connection dbConn,Map map) throws Exception{
		String flowIdStr = (String)map.get("flowId");
		String userId = (String)map.get("userId");
		String deptId = (String)map.get("deptId");
		String fldStr = (String)map.get("fldStr");
		String deptFlag = (String)map.get("deptFlag");
		int flowId = 0;
		if (YHUtility.isNumber(flowIdStr)) {
			flowId = Integer.parseInt(flowIdStr);
		}
		
		StringBuffer buffer = new StringBuffer("[");
		YHORM orm = new YHORM();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			boolean isHaveFlag = this.getSalItemCount(dbConn); 
			if (!isHaveFlag) {
				String remindTable = "<table class='MessageBox' align='center' width='320'>"
					+ "<tr>"
					+ "<td class='msg info'><h4 class='title'>提示</h4>"
					+ "<div class='content' style='font-size:12pt'><b>尚未有数据项!</b></div>"
					+ "</td>"
					+ "</tr>"
					+ "</table>";
				String tableList= remindTable;
				String data = "{tableList:\"" + tableList.trim() + "\"}";
				return data;
			}
		//查询要显示的td信息
			List<String> fieldList = new ArrayList<String>();
			String itemTd = "";
			if (YHUtility.isNullorEmpty(fldStr)) {
				fieldList = this.getSalItemId(dbConn);
				fieldList.add("ALL_BASE");
				fieldList.add("PENSION_BASE");
				fieldList.add("PENSION_U");
				fieldList.add("PENSION_P");
				fieldList.add("MEDICAL_BASE");
				fieldList.add("MEDICAL_U");
				fieldList.add("MEDICAL_P");
				fieldList.add("FERTILITY_BASE");
				fieldList.add("FERTILITY_U");
				fieldList.add("UNEMPLOYMENT_BASE");
				fieldList.add("UNEMPLOYMENT_U");
				fieldList.add("UNEMPLOYMENT_P");
				fieldList.add("INJURIES_BASE");
				fieldList.add("INJURIES_U");
				fieldList.add("HOUSING_BASE");
				fieldList.add("HOUSING_U");
				fieldList.add("HOUSING_P");
				fieldList.add("INSURANCE_DATE");
				fieldList.add("MEMO");
			}else {
				fieldList = this.getArryListByStr(dbConn, fldStr);
			}
			if (fieldList!=null && fieldList.size()>0) {
				for(String field:fieldList){
					String statStr = field.substring(0, 1);
					if ("S".equals(statStr) && field.length()<=3) {
						String itemIdStr = field.substring(1, field.length());
						if (YHUtility.isNumber(itemIdStr) ) {
							Map filters = new HashMap();
							filters.put("SLAITEM_ID", itemIdStr);
							YHSalItem item = (YHSalItem) orm.loadObjSingle(dbConn, YHSalItem.class, filters);
							if (item!=null) {
								String itemName = YHUtility.null2Empty(item.getItemName());
								itemTd += "<td nowrap align='center'><b>" + itemName + "<b></td>";
								
							}
						}
					}else {
						if ("ALL_BASE".equals(field)) {
							itemTd += "<td nowrap align='center'><b>保险基数</b></td>";
						}
						if ("PENSION_BASE".equals(field)) {
							itemTd += "<td nowrap align='center'><b>养老保险</b></td>";
						}
						if ("PENSION_U".equals(field)) {
							itemTd += "<td nowrap align='center'><b>单位养老</b></td>";
						}
						if ("PENSION_P".equals(field)) {
							itemTd += "<td nowrap align='center'><b>个人养老</b></td>";
						}
						if ("MEDICAL_BASE".equals(field)) {
							itemTd += "<td nowrap align='center'><b>医疗保险</b></td>";
						}
						if ("MEDICAL_U".equals(field)) {
							itemTd += "<td nowrap align='center'><b>单位医疗</b></td>";
						}
						if ("MEDICAL_P".equals(field)) {
							itemTd += "<td nowrap align='center'><b>个人医疗</b></td>";
						}
						if ("FERTILITY_BASE".equals(field)) {
							itemTd += "<td nowrap align='center'><b>生育保险</b></td>";
						}
						if ("FERTILITY_U".equals(field)) {
							itemTd += "<td nowrap align='center'><b>单位生育</b></td>";
						}
						if ("UNEMPLOYMENT_BASE".equals(field)) {
							itemTd += "<td nowrap align='center'><b>失业保险</b></td>";
						}
						
						if ("UNEMPLOYMENT_U".equals(field)) {
							itemTd += "<td nowrap align='center'><b>单位失业</b></td>";
						}
						if ("UNEMPLOYMENT_P".equals(field)) {
							itemTd += "<td nowrap align='center'><b>个人失业</b></td>";
						}
						if ("INJURIES_BASE".equals(field)) {
							itemTd += "<td nowrap align='center'><b>工伤保险</b></td>";
						}
						if ("INJURIES_U".equals(field)) {
							itemTd += "<td nowrap align='center'><b>单位工伤</b></td>";
						}
						if ("HOUSING_BASE".equals(field)) {
							itemTd += "<td nowrap align='center'><b>住房公积金</b></td>";
						}
						if ("HOUSING_U".equals(field)) {
							itemTd += "<td nowrap align='center'><b>单位住房</b></td>";
						}
						if ("HOUSING_P".equals(field)) {
							itemTd += "<td nowrap align='center'><b>个人住房</b></td>";
						}
						if ("INSURANCE_DATE".equals(field)) {
							itemTd += "<td nowrap align='center'><b>投保日期</b></td>";
						}
						if ("MEMO".equals(field)) {
							itemTd += "<td nowrap align='center'><b>备注</b></td>";
						}
						
					}
				}
			}
			//查询人员信息
			String conditionStr ="";
			int userCount = 0;
			if(!YHUtility.isNullorEmpty(userId)){
				if (userId.endsWith(",")) {
					userId = userId.substring(0, userId.length()-1);
				}
				conditionStr = "  and PERSON.SEQ_ID in (" + userId + ")";
			}
			if(!YHUtility.isNullorEmpty(deptId) && !"ALL_DEPT".equals(deptId) && !"0".equals(deptId)){
				if (deptId.endsWith(",")) {
					deptId = deptId.substring(0, deptId.length()-1);
				}
				conditionStr = "  and oa_department.SEQ_ID in (" + deptId + ")";
			}
			if (!"1".equals(deptFlag.trim())) {
				sql = "SELECT PERSON.SEQ_ID as personId, USER_NAME,PERSON.DEPT_ID as deptId,PERSON.USER_PRIV as userPriv from PERSON,USER_PRIV,oa_department where oa_department.SEQ_ID=PERSON.DEPT_ID and PERSON.USER_PRIV=USER_PRIV.SEQ_ID " + conditionStr + " order by DEPT_NO,PRIV_NO,USER_NAME";
			}else {
				sql = "SELECT PERSON.SEQ_ID as personId, USER_NAME,PERSON.DEPT_ID as deptId,PERSON.USER_PRIV as userPriv from PERSON,USER_PRIV where PERSON.USER_PRIV=USER_PRIV.SEQ_ID and DEPT_ID=0";
			}
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			
			String defultTr = "";
			String tdValue= "";
			String lastValue = "";
			String lastTr = "";
		Map<String, Integer> mapList = new HashMap<String,Integer>();
			while(rs.next()){
				userCount ++;
				int dbSeqId = rs.getInt("personId");
				String dbUserName = YHUtility.null2Empty(rs.getString("USER_NAME"));
				int dbDeptId = rs.getInt("deptId");
				String dbUserPriv = YHUtility.null2Empty(rs.getString("userPriv"));
				
				String dbDeptName = ""; 
				String dbPrivName = ""; 
				if (dbDeptId==0) {
					dbDeptName = "离职人员/外部人员";
				}else {
					dbDeptName = this.getDeptName(dbConn, dbDeptId);
				}
				dbPrivName = this.getPrivName(dbConn, dbUserPriv);
				
				String itemTdValue = this.getItemTdValue(dbConn, fieldList, flowId, dbSeqId,mapList);
				tdValue += "<tr class='TableData'>"
					+ "<td nowrap align='center'>" + YHUtility.encodeSpecial(dbDeptName) + "</td>"
					+ "<td nowrap align='center'>" + YHUtility.encodeSpecial(dbUserName) + "</td>"
					+ "<td nowrap align='center'>" + YHUtility.encodeSpecial(dbPrivName) + "</td>"
					+ itemTdValue
					+ "</tr>";
			}
			
			if (userCount>0) {
				defultTr = "<tr  bgcolor='#D3E5FA' align='center'>"
					+ "<td nowrap align='center'><b>部门</b></td>"
					+ "<td nowrap align='center'><b>姓名</b></td>"
					+ "<td nowrap align='center'><b>角色</b></td>"
					+ itemTd
					+ "</tr>";
				if (fieldList!=null && fieldList.size()>0 && mapList!=null && mapList.size()>0) {
					for(String field:fieldList){
						if(!"MEMO".equals(field)&&!"INSURANCE_DATE".equals(field)){
							int mapValue = mapList.get(field);
							String fieldValueStr = "";
							if (mapValue !=0) {
								fieldValueStr = YHUtility.getFormatedStr(mapValue, 2);
							}
							lastValue += "<td nowrap align='center'>" + fieldValueStr + "</td>";
						}else {
							lastValue += "<td nowrap align='center'>&nbsp;</td>";
						}
					}
				}else {
					for(String field:fieldList){
						if(!"MEMO".equals(field)&&!"INSURANCE_DATE".equals(field)){
							String fieldValueStr = "&nbsp;";
							lastValue += "<td nowrap align='center'>" + fieldValueStr + "</td>";
						}else {
							lastValue += "<td nowrap align='center'>&nbsp;</td>";
						}
					}
				}
				lastTr = "<tr bgcolor='EEEEEE'>"
					+ "<td nowrap align='center' colspan='3'><b>合计</b></td>";
			}else {
				String remindTable = "<table class='MessageBox' align='center' width='320'>"
					+ "<tr>"
					+ "<td class='msg info'><h4 class='title'>提示</h4>"
					+ "<div class='content' style='font-size:12pt'><b>尚未定义用户</b></div>"
					+ "</td>"
					+ "</tr>"
					+ "</table>";
				defultTr = remindTable;
			}
			
			String tableList= defultTr + tdValue + lastTr + lastValue;
			String data = "{tableList:\"" + tableList.trim() + "\"}";
			return data;
		} catch (Exception e) {
			throw e;
		}
	}

	public String getItemTdValue(Connection dbConn,List<String>fieldList,int flowId,int personId,Map<String, Integer> mapList) throws Exception{
		String itemTdValue = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT * from oa_sal_data where FLOW_ID='" + flowId + "' and USER_ID='" + personId + "'";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				if (fieldList!=null && fieldList.size()>0) {
					for(String field:fieldList){
						if(!"MEMO".equals(field)&&!"INSURANCE_DATE".equals(field)){
							int fieldValue = rs.getInt(field);
							String fieldValueStr = YHUtility.getFormatedStr(fieldValue, 2);
							itemTdValue += "<td nowrap align='center'>" + fieldValueStr + "</td>";
							
							int mapValue = 0;
							if(mapList!=null && mapList.size()>0 && mapList.containsKey(field.trim())){
								mapValue = fieldValue + mapList.get(field.trim());
							}else {
								mapValue = fieldValue;
							}
							mapList.put(field.trim(), mapValue);
						}else {
							String fieldValue = YHUtility.null2Empty(rs.getString(field));
							itemTdValue += "<td nowrap align='center'>" + fieldValue + "</td>";
						}
					}
				}
			}else {
				if (fieldList!=null && fieldList.size()>0) {
					for(String field:fieldList){
						String fieldValue = "&nbsp;";
						itemTdValue += "<td nowrap align='center'>" + fieldValue + "</td>";
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return itemTdValue;
	}
	public String getItemTdValueYHExcel(Connection dbConn,List<String>fieldList,ArrayList<YHDbRecord> result,YHDbRecord record,int flowId,int personId,Map<String, Integer> mapList) throws Exception{
		String itemTdValue = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT * from oa_sal_data where FLOW_ID='" + flowId + "' and USER_ID='" + personId + "'";
		
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				if (fieldList!=null && fieldList.size()>0) {
					for(String field:fieldList){
					 
					  String fieldStr = getField(dbConn, field);
//						YHDbRecord record = new YHDbRecord();
						String fieldValueStr = "";
						if(!"MEMO".equals(field)&&!"INSURANCE_DATE".equals(field)){
							int fieldValue = rs.getInt(field);
							fieldValueStr = YHUtility.getFormatedStr(fieldValue, 2);
							int mapValue = 0;
							if(mapList!=null && mapList.size()>0 && mapList.containsKey(field.trim())){
								mapValue = fieldValue + mapList.get(field.trim());
							}else {
								mapValue = fieldValue;
							}
							mapList.put(field.trim(), mapValue);  //合计
						}else {
							fieldValueStr = YHUtility.null2Empty(rs.getString(field));
						}
						record.addField(fieldStr, fieldValueStr);
					}
				}
			}else {
				if (fieldList!=null && fieldList.size()>0) {
					for(String field:fieldList){
//						YHDbRecord record = new YHDbRecord();
						String fieldValue = "";
						String fieldStr = getField(dbConn, field);
						record.addField(fieldStr, fieldValue);
					}
				}
			}
			result.add(record);
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return itemTdValue;
	}
	
	public String getDeptName(Connection dbConn, int deptId) throws Exception {
		String deptName = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select DEPT_NAME from oa_department where SEQ_ID=" + deptId;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				deptName = rs.getString("DEPT_NAME");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return deptName;
	}
	public String getPrivName(Connection dbConn, String privIdStr) throws Exception {
		String privName = "";
		int privId = 0;
		if (YHUtility.isNumber(privIdStr)) {
			privId = Integer.parseInt(privIdStr);
		}
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT PRIV_NAME from USER_PRIV where SEQ_ID=" + privId;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				privName = rs.getString("PRIV_NAME");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return privName;
	}

	public List<String> getArryListByStr(Connection dbConn, String str) throws Exception {
		List<String> list = new ArrayList<String>();
		try {
			if (YHUtility.isNullorEmpty(str)) {
				return list;
			}
			if (str.endsWith(",")) {
				str = str.substring(0, str.length() - 1);
			}
			String[] strArrys = str.split(",");
			if (strArrys != null && strArrys.length > 0) {
				for (String strArry : strArrys) {
					list.add(strArry);
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	public List<String> getSalItemId(Connection dbConn) throws Exception {
		List<String> fieldList = new ArrayList<String>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT SLAITEM_ID from oa_sal_item ORDER BY SLAITEM_ID";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String itemId = "S" + rs.getInt("SLAITEM_ID");
				fieldList.add(itemId);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return fieldList;
	}

	public boolean getSalItemCount(Connection dbConn) throws Exception {
		int count = 0;
		boolean flag = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT count(SEQ_ID) from oa_sal_item";
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
			if (count > 0) {
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
	 * 根据条件导出数据到Excel文件
	 * @param dbConn
	 * @param map
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public ArrayList<YHDbRecord> exportToExcelLogic(Connection dbConn,Map<Object, Object> map,YHPerson person ) throws Exception {
		ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
		String flowIdStr = (String)map.get("flowId");
		String userId = (String)map.get("userId");
		String deptId = (String)map.get("deptId");
		String fldStr = (String)map.get("fldStr");
		String deptFlag = (String)map.get("deptFlag");
		int flowId = 0;
		if (YHUtility.isNumber(flowIdStr)) {
			flowId = Integer.parseInt(flowIdStr);
		}
		
		YHORM orm = new YHORM();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			boolean isHaveFlag = this.getSalItemCount(dbConn); 
			if (!isHaveFlag) {
			}
		//查询要显示的td信息
			List<String> fieldList = new ArrayList<String>();
			String itemTd = "";
			if (YHUtility.isNullorEmpty(fldStr)) {
				fieldList = this.getSalItemId(dbConn);
				fieldList.add("ALL_BASE");
				fieldList.add("PENSION_BASE");
				fieldList.add("PENSION_U");
				fieldList.add("PENSION_P");
				fieldList.add("MEDICAL_BASE");
				fieldList.add("MEDICAL_U");
				fieldList.add("MEDICAL_P");
				fieldList.add("FERTILITY_BASE");
				fieldList.add("FERTILITY_U");
				fieldList.add("UNEMPLOYMENT_BASE");
				fieldList.add("UNEMPLOYMENT_U");
				fieldList.add("UNEMPLOYMENT_P");
				fieldList.add("INJURIES_BASE");
				fieldList.add("INJURIES_U");
				fieldList.add("HOUSING_BASE");
				fieldList.add("HOUSING_U");
				fieldList.add("HOUSING_P");
				fieldList.add("INSURANCE_DATE");
				fieldList.add("MEMO");
			}else {
				fieldList = this.getArryListByStr(dbConn, fldStr);
			}
			
		//查询人员信息
			String conditionStr ="";
			int userCount = 0;
			if(!YHUtility.isNullorEmpty(userId)){
				if (userId.endsWith(",")) {
					userId = userId.substring(0, userId.length()-1);
				}
				conditionStr = "  and PERSON.SEQ_ID in (" + userId + ")";
			}
			if(!YHUtility.isNullorEmpty(deptId) && !"ALL_DEPT".equals(deptId) && !"0".equals(deptId)){
				if (deptId.endsWith(",")) {
					deptId = deptId.substring(0, deptId.length()-1);
				}
				conditionStr = "  and oa_department.SEQ_ID in (" + deptId + ")";
			}
			if (!"1".equals(deptFlag.trim())) {
				sql = "SELECT PERSON.SEQ_ID as personId, USER_NAME,PERSON.DEPT_ID as deptId,PERSON.USER_PRIV as userPriv from PERSON,USER_PRIV,oa_department where oa_department.SEQ_ID=PERSON.DEPT_ID and PERSON.USER_PRIV=USER_PRIV.SEQ_ID " + conditionStr + " order by DEPT_NO,PRIV_NO,USER_NAME";
			}else {
				sql = "SELECT PERSON.SEQ_ID as personId, USER_NAME,PERSON.DEPT_ID as deptId,PERSON.USER_PRIV as userPriv from PERSON,USER_PRIV where PERSON.USER_PRIV=USER_PRIV.SEQ_ID and DEPT_ID=0";
			}
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			
			Map<String, Integer> mapList = new HashMap<String,Integer>();
			while(rs.next()){
				userCount ++;
				int dbSeqId = rs.getInt("personId");
				String dbUserName = YHUtility.null2Empty(rs.getString("USER_NAME"));
				int dbDeptId = rs.getInt("deptId");
				String dbUserPriv = YHUtility.null2Empty(rs.getString("userPriv"));
				
				String dbDeptName = ""; 
				String dbPrivName = ""; 
				if (dbDeptId==0) {
					dbDeptName = "离职人员/外部人员";
				}else {
					dbDeptName = this.getDeptName(dbConn, dbDeptId);
				}
				dbPrivName = this.getPrivName(dbConn, dbUserPriv);
				
				YHDbRecord record = new YHDbRecord();
				record.addField("部门", dbDeptName);
				record.addField("姓名", dbUserName);
				record.addField("角色", dbPrivName);
				this.getItemTdValueYHExcel(dbConn, fieldList,result,record, flowId, dbSeqId,mapList);
			}
			if (userCount>0) {
				YHDbRecord record = new YHDbRecord();
				record.addField("部门", "合计");
				record.addField("姓名", "");
				record.addField("角色", "");
				if (fieldList!=null && fieldList.size()>0 && mapList!=null && mapList.size()>0) {
					for(int i=0;i<fieldList.size();i++){
						String field = fieldList.get(i);
						String fieldValueStr = "";
						if(!"MEMO".equals(field)&&!"INSURANCE_DATE".equals(field)){
							int mapValue = mapList.get(field);
							if (mapValue !=0) {
								fieldValueStr = YHUtility.getFormatedStr(mapValue, 2);
							}
						}
						String fieldStr = getField(dbConn, field);
						record.addField(fieldStr, fieldValueStr);
					}
				}
				result.add(record);
			}else {
				YHDbRecord rd = new YHDbRecord();
				rd.addField("部门", "");
				rd.addField("姓名", "");
				rd.addField("角色", "");
				if (fieldList!=null && fieldList.size()>0) {
					for(String field:fieldList){
						rd.addField(field, "");
					}
				}
				result.add(rd);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		return result;
	}
	
  public String sendEMail(Connection dbConn , Map<Object, Object> map , YHPerson person) throws Exception {
    String data = "";
	    
    YHEmailUtil email = new YHEmailUtil();
    String flowId = (String)map.get("flowId");
    String subject = (String)map.get("content");
    String isZero = (String)map.get("isZero");
    String fldStr = (String)map.get("fldStr");
    String fIdArray[] = fldStr.split(",");
    String fIdTempStr = "";
    for(int i = 0; i < fIdArray.length; i++){
      fIdTempStr += ","+fIdArray[i].substring(1, fIdArray[i].length());
    }
    
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = " select 'S'+SLAITEM_ID SID,ITEM_NAME from oa_sal_item "
        + " where SLAITEM_ID in (" + fIdTempStr.substring(1, fIdTempStr.length()) + ") ";
    }
    else if(dbms.equals(YHConst.DBMS_MYSQL)){
      sql = " select CONCAT('S',SLAITEM_ID) SID,ITEM_NAME from oa_sal_item "
        + " where SLAITEM_ID in (" + fIdTempStr.substring(1, fIdTempStr.length()) + ") ";
    }
    else {
      sql = " select 'S'||SLAITEM_ID SID,ITEM_NAME from oa_sal_item "
        + " where SLAITEM_ID in (" + fIdTempStr.substring(1, fIdTempStr.length()) + ") ";
    }
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Map<String,String> sName = new HashMap<String,String>();
    List<String> slist = new ArrayList<String>();
    Map<String,Double> smap = new HashMap<String,Double>();
    try{
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()){
        slist.add(rs.getString("SID"));
        sName.put(rs.getString("SID"),rs.getString("ITEM_NAME"));
      } 
      String toId = (String)map.get("userIdStr");
      List toIdArray = new ArrayList() ;
      if(YHUtility.isNullorEmpty(toId)){
        sql = " select SEQ_ID from PERSON ";
        stmt = dbConn.prepareStatement(sql);
        rs = stmt.executeQuery();
        while(rs.next()){
          toIdArray.add(rs.getString("SEQ_ID"));
        } 
      }
      else{
        toIdArray = Arrays.asList(toId.split(","));
      }
      
      for(int k = 0; k < toIdArray.size();k++){
        sql = " select " + fldStr.substring(0,fldStr.length()-1) + " from oa_sal_data where USER_ID =" + toIdArray.get(k) + " and FLOW_ID ="+flowId;
        stmt = dbConn.prepareStatement(sql);
        rs = stmt.executeQuery();      
        if(rs.next()){
          for(int i = 0; i < slist.size(); i++){
            smap.put(slist.get(i), rs.getDouble(slist.get(i)));
          }
        }else{
          sql = " select USER_NAME from PERSON where SEQ_ID =" + toIdArray.get(k);
          Statement stmtTemp = dbConn.createStatement();
          ResultSet rsTemp = stmtTemp.executeQuery(sql);   
          if(rsTemp.next()){
            data += rsTemp.getString("USER_NAME") + ",";
          }
          YHDBUtility.close(stmt, rs, null);
          continue;
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append("<table align=\"center\" id=\"cal_table\" class=\"TableBlock\" >");
        sb.append("<tbody>");
        sb.append("  <tr align=\"center\" class=\"TableHeader\">");
        sb.append("    <td nowrap width=\"120px\"><b>工资项目</b></td>");
        sb.append("    <td nowrap width=\"300px\" align=\"right\"><b style=\"padding-right:55px\">金额</b></td>");
        sb.append("  </tr>");
    
        for(int i = 0; i < slist.size(); i++){ 
          String s = fIdArray[i];
          double value = smap.get(s);
          String valueStr = YHUtility.getFormatedStr(value,1);
          if(!(isZero.equals("true") && valueStr.equals("0.00"))){
            sb.append("  <tr align=\"center\" class=\"TableLine1\">");
            sb.append("    <td nowrap width=\"120px\">" + sName.get(fIdArray[i]) + "</td>");
            sb.append("    <td nowrap width=\"300px\" align=\"center\">" + valueStr + "</td>");
            sb.append("  </tr>");
          }
        }  
        sb.append("</tbody>");
        sb.append("</table>");
        
        email.emailNotifier(dbConn, person.getSeqId(), (String)toIdArray.get(k), subject, sb.toString(), new Date(), null, "", true);
        YHDBUtility.close(stmt, rs, null);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    if(YHUtility.isNullorEmpty(data)){
      return "";
    }
    else{
      return data.substring(0,data.length()-1);
    }
  }
	
  public String sendMobile(Connection dbConn , Map<Object, Object> map , YHPerson person) throws Exception {
    StringBuffer data = new StringBuffer();
    String flowId = (String)map.get("flowId");
    String subject = (String)map.get("content");
    String fldStr = (String)map.get("fldStr");
    String fIdArray[] = fldStr.split(",");
    String fIdTempStr = "";
    for(int i = 0; i < fIdArray.length; i++){
      fIdTempStr += ","+fIdArray[i].substring(1, fIdArray[i].length());
    }
     
    String sql = null;
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      sql = " select 'S'+SLAITEM_ID SID,ITEM_NAME from oa_sal_item "
        + " where SLAITEM_ID in (" + fIdTempStr.substring(1, fIdTempStr.length()) + ") ";
    }
    else if(dbms.equals(YHConst.DBMS_MYSQL)){
      sql = " select CONCAT('S',SLAITEM_ID) SID,ITEM_NAME from oa_sal_item "
        + " where SLAITEM_ID in (" + fIdTempStr.substring(1, fIdTempStr.length()) + ") ";     
    }
    else {
      sql = " select 'S'||SLAITEM_ID SID,ITEM_NAME from oa_sal_item "
        + " where SLAITEM_ID in (" + fIdTempStr.substring(1, fIdTempStr.length()) + ") ";
    }
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Map<String,String> sName = new HashMap<String,String>();
    List<String> slist = new ArrayList<String>();
    Map<String,Double> smap = new HashMap<String,Double>();
    try{
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while(rs.next()){
        slist.add(rs.getString("SID"));
        sName.put(rs.getString("SID"),rs.getString("ITEM_NAME"));
      } 
      String toId = (String)map.get("userIdStr");
      List toIdArray = new ArrayList() ;
      if(YHUtility.isNullorEmpty(toId)){
        sql = " select SEQ_ID from PERSON ";
        stmt = dbConn.prepareStatement(sql);
        rs = stmt.executeQuery();
        while(rs.next()){
          toIdArray.add(rs.getString("SEQ_ID"));
        } 
      }
      else{
        toIdArray = Arrays.asList(toId.split(","));
      }
       
      for(int k = 0; k < toIdArray.size();k++){
        StringBuffer sb = new StringBuffer();
        sql = " select " + fldStr.substring(0,fldStr.length()-1) + " from oa_sal_data where USER_ID =" + toIdArray.get(k) + " and FLOW_ID ="+flowId;
        stmt = dbConn.prepareStatement(sql);
        rs = stmt.executeQuery();      
        if(rs.next()){
          for(int i = 0; i < slist.size(); i++){
            smap.put(slist.get(i), rs.getDouble(slist.get(i)));
          }
        }else{
          continue;
        }
         
        String userName = "";
        sql = " select USER_NAME from PERSON where SEQ_ID =" + toIdArray.get(k);
        Statement stmtTemp = dbConn.createStatement();
        ResultSet rsTemp = stmtTemp.executeQuery(sql);   
        if(rsTemp.next()){
          userName = rsTemp.getString("USER_NAME");
        }
         
        sb.append(subject+":"+userName+":");
     
        for(int i = 0; i < slist.size(); i++){ 
          String s = fIdArray[i];
          double value = smap.get(s);
          String valueStr = YHUtility.getFormatedStr(value,1);
          sb.append(sName.get(fIdArray[i])+":");
          sb.append(valueStr + " ");
        }  
        // 手机提醒
        YHMobileSms2Logic sbl = new YHMobileSms2Logic();
        sbl.remindByMobileSms(dbConn, (String)toIdArray.get(k), person.getSeqId(), sb.toString(), new Date());
        data.append(sb+"<br>");
        YHDBUtility.close(stmt, rs, null);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return data.toString();
 }
	
	public String getField(Connection dbConn, String field) throws Exception{
	  
    String fieldStr = "";
    String statStr = field.substring(0, 1);
    if ("S".equals(statStr) && field.length()<=3) {
      String itemIdStr = field.substring(1, field.length());
      if (YHUtility.isNumber(itemIdStr) ) {
        Map filters = new HashMap();
        filters.put("SLAITEM_ID", itemIdStr);
        YHORM orm = new YHORM();
        YHSalItem item = (YHSalItem) orm.loadObjSingle(dbConn, YHSalItem.class, filters);
        if (item!=null) {
          String itemName = YHUtility.null2Empty(item.getItemName());
          fieldStr = itemName;
        }
      }
    }
    else{
      if ("ALL_BASE".equals(field)) {
        fieldStr = "保险基数";
      }
      if ("PENSION_BASE".equals(field)) {
        fieldStr = "养老保险";
      }
      if ("PENSION_U".equals(field)) {
        fieldStr = "单位养老";
      }
      if ("PENSION_P".equals(field)) {
        fieldStr = "个人养老";
      }
      if ("MEDICAL_BASE".equals(field)) {
        fieldStr = "医疗保险";
      }
      if ("MEDICAL_U".equals(field)) {
        fieldStr = "单位医疗";
      }
      if ("MEDICAL_P".equals(field)) {
        fieldStr = "个人医疗";
      }
      if ("FERTILITY_BASE".equals(field)) {
        fieldStr = "生育保险";
      }
      if ("FERTILITY_U".equals(field)) {
        fieldStr = "单位生育";
      }
      if ("UNEMPLOYMENT_BASE".equals(field)) {
        fieldStr = "失业保险";
      }
      if ("UNEMPLOYMENT_U".equals(field)) {
        fieldStr = "单位失业";
      }
      if ("UNEMPLOYMENT_P".equals(field)) {
        fieldStr = "个人失业";
      }
      if ("INJURIES_BASE".equals(field)) {
        fieldStr = "工伤保险";
      }
      if ("INJURIES_U".equals(field)) {
        fieldStr = "单位工伤";
      }
      if ("HOUSING_BASE".equals(field)) {
        fieldStr = "住房公积金";
      }
      if ("HOUSING_U".equals(field)) {
        fieldStr = "单位住房";
      }
      if ("HOUSING_P".equals(field)) {
        fieldStr = "个人住房";
      }
      if ("INSURANCE_DATE".equals(field)) {
        fieldStr = "投保日期";
      }
      if ("MEMO".equals(field)) {
        fieldStr = "备注";
      }
    }
    return fieldStr;
	}
	

}
