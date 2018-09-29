package yh.subsys.oa.rollmanage.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.rollmanage.data.YHRmsFile;
import yh.subsys.oa.rollmanage.data.YHRmsLend;

public class YHRmsLendLogic {
	private static Logger log = Logger.getLogger(YHRmsLendLogic.class);

	/**
	 * 获取审批人	 * 
	 * @param conn
	 * @param secret
	 * @return
	 * @throws Exception
	 */
	public String getRmsLendManage(Connection conn, int rollId) throws Exception {
		String result = "";
		String sql = "select MANAGER from oa_archives_volume where SEQ_ID=" + rollId;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				String toId = rs.getString(1);
				if (toId != null) {
					result = toId;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		return result;
	}

	/**
	 * 借阅时短信提醒	 * 
	 * @param conn
	 * @param content
	 * @param fromId
	 * @param toId
	 * @param type
	 * @param remindUrl
	 * @throws Exception
	 */
	public static void doSmsBack(Connection conn, String content, int fromId, String toId, String type, String remindUrl) throws Exception {
		YHSmsBack sb = new YHSmsBack();
		sb.setContent(content);
		sb.setFromId(fromId);
		sb.setToId(toId);
		sb.setSmsType(type);
		sb.setRemindUrl(remindUrl);
		YHSmsUtil.smsBack(conn, sb);
	}

	/**
	 * 借阅查询
	 * 
	 * @param dbConn
	 * @param request
	 * @param person
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String queryLendFileLogic(Connection dbConn, Map request, YHPerson person, Map<Object, Object> map) throws Exception {

		String roomName = (String) map.get("roomName");
		String rollName = (String) map.get("rollName");
		String fileCode = (String) map.get("fileCode");
		String fileSubject = (String) map.get("fileSubject");

		String fileTitle = (String) map.get("fileTitle");
		String fileTitleo = (String) map.get("fileTitleo");
		String sendUnit = (String) map.get("sendUnit");
		String remark = (String) map.get("remark");
		String sendTimeMin = (String) map.get("sendTimeMin");
    String sendTimeMax = (String) map.get("sendTimeMax");
    String fileWord = (String) map.get("fileWord");
    String fileYear = (String) map.get("fileYear");
    String  issueNum= (String) map.get("issueNum");
    
		String conditionStr = "";
		String roomIdStr = "";
		String rollIdStr = "";
		YHPageDataList pageDataList = null;
		try {
			if (!YHUtility.isNullorEmpty(roomName)) {
				String sql = "SELECT SEQ_ID from oa_archives_volume_ROOM where ROOM_NAME like '%" + YHDBUtility.escapeLike(roomName) + "%'" + YHDBUtility.escapeLike();
				roomIdStr = this.getRoomIdStr(dbConn, sql);
			}
			if (!YHUtility.isNullorEmpty(roomIdStr)) {
				if (!YHUtility.isNullorEmpty(rollName)) {
					String sql = "SELECT SEQ_ID from oa_archives_volume  where ROOM_ID in (" + roomIdStr + ") or ROLL_NAME like '%" + YHDBUtility.escapeLike(rollName)
							+ "%'" + YHDBUtility.escapeLike();
					rollIdStr = this.getRollIdStr(dbConn, sql);
				} else {
					String sql = "SELECT SEQ_ID from oa_archives_volume  where ROOM_ID in (" + roomIdStr + ")";
					rollIdStr = this.getRollIdStr(dbConn, sql);
				}
			} else if (!YHUtility.isNullorEmpty(rollName)) {
				String sql = "SELECT SEQ_ID from oa_archives_volume  where ROLL_NAME like '%" + YHDBUtility.escapeLike(rollName) + "%'" + YHDBUtility.escapeLike();
				rollIdStr = this.getRollIdStr(dbConn, sql);
			} else {
				String sql = "SELECT SEQ_ID from oa_archives_volume ";
				rollIdStr = this.getRollIdStr(dbConn, sql);
			}
			if (!YHUtility.isNullorEmpty(fileCode)) {
				conditionStr += " and FILE_CODE like '%" + YHDBUtility.escapeLike(fileCode) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(fileSubject)) {
				conditionStr += " and FILE_SUBJECT like '%" + YHDBUtility.escapeLike(fileSubject) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(fileTitle)) {
				conditionStr += " and FILE_TITLE like '%" + YHDBUtility.escapeLike(fileTitle) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(fileTitleo)) {
				conditionStr += " and FILE_TITLEO like '%" + YHDBUtility.escapeLike(fileTitleo) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(sendUnit)) {
				conditionStr += " and SEND_UNIT like '%" + YHDBUtility.escapeLike(sendUnit) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(remark)) {
				conditionStr += " and REMARK like '%" + YHDBUtility.escapeLike(remark) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(sendTimeMin)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("SEND_DATE", sendTimeMin, ">=");
      }
      if (!YHUtility.isNullorEmpty(sendTimeMax)) {
        conditionStr += " and " + YHDBUtility.getDateFilter("SEND_DATE", sendTimeMax, "<=");
      }
			if (!YHUtility.isNullorEmpty(roomName) || !YHUtility.isNullorEmpty(rollName)) {
				if (!YHUtility.isNullorEmpty(rollIdStr)) {
					conditionStr += "  and ROLL_ID in (" + rollIdStr + ")";
				}
			}
			if (!YHUtility.isNullorEmpty(fileWord)) {
        conditionStr += " and FILE_WORD like  '%" + YHDBUtility.escapeLike(fileWord) + "%'" + YHDBUtility.escapeLike();
      }
      if (!YHUtility.isNullorEmpty(fileYear)) {
        conditionStr += " and FILE_YEAR = '" + YHDBUtility.escapeLike(fileYear) + "'";
      }
      if (!YHUtility.isNullorEmpty(issueNum)) {
        conditionStr += " and ISSUE_NUM like  '%" + YHDBUtility.escapeLike(issueNum) + "%'" + YHDBUtility.escapeLike();
      }
			
			String query = "SELECT SEQ_ID,FILE_CODE,FILE_TITLE,SECRET,SEND_UNIT,SEND_DATE,URGENCY,ROLL_ID from oa_archives_attach where (DEL_USER='' or del_user is null) "
					+ conditionStr;

			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, query);

		} catch (Exception e) {
			throw e;
		}
		return pageDataList.toJson();

	}

	/**
	 * 获取卷库id
	 * 
	 * @param dbConn
	 * @param roomName
	 * @return
	 * @throws Exception
	 */
	public String getRoomIdStr(Connection dbConn, String sql) throws Exception {
		String roomIdStr = "";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String seqId = rs.getString(1);
				if (seqId != null) {
					roomIdStr += seqId + ",";
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}

		if (roomIdStr.trim().endsWith(",")) {
			roomIdStr = roomIdStr.substring(0, roomIdStr.length() - 1);
		}

		return roomIdStr;
	}

	/**
	 * 获取案卷id
	 * 
	 * @param dbConn
	 * @param roomName
	 * @return
	 * @throws Exception
	 */
	public String getRollIdStr(Connection dbConn, String sql) throws Exception {
		String rollIdStr = "";
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String seqId = rs.getString(1);
				if (seqId != null) {
					rollIdStr += seqId + ",";
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		if (rollIdStr.trim().endsWith(",")) {
			rollIdStr = rollIdStr.substring(0, rollIdStr.length() - 1);
		}
		return rollIdStr;
	}

	
/**
 * 待批准借阅
 * @param dbConn
 * @param person
 * @return
 * @throws Exception
 */
	public StringBuffer getApprovalToBorrow(Connection dbConn, YHPerson person, String allow) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHRmsFile rmsFile = null;
    YHRmsLend rmsLend = null;
    int seqId = 0;
    int fileId = 0;
    String fileCode = "";
    Date addTime = null;
    Date returnTime = null;
    Date allowTime = null;
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    try {
      stmt = dbConn.createStatement();
      String sql = "select "
          + "RMS_LEND.SEQ_ID"
          + ",RMS_LEND.FILE_ID"
          + ",RMS_FILE.FILE_CODE"
          + ",RMS_LEND.ADD_TIME"
          + ",RMS_LEND.RETURN_TIME"
          + ",RMS_LEND.ALLOW_TIME"
          + " from oa_archives_lend as  RMS_LEND left join  oa_archives_attach as RMS_FILE on RMS_FILE.SEQ_ID = RMS_LEND.FILE_ID where RMS_LEND.USER_ID =" + person.getSeqId() + " and ALLOW = '" + allow + "'";
      if ("0".equals(allow)) {
        sql = sql + " order by RMS_LEND.ADD_TIME desc";
      } else if ("3".equals(allow)) {
        sql = sql + " order by RMS_LEND.RETURN_TIME desc";
      } else {
        sql = sql + " order by RMS_LEND.ALLOW_TIME desc";
      }
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
      	sb.append("{");
        rmsFile = new YHRmsFile();
        rmsLend = new YHRmsLend();
        seqId = rs.getInt("SEQ_ID");
        fileId =  rs.getInt("FILE_ID");
        fileCode = rs.getString("FILE_CODE");
        addTime = rs.getTimestamp("ADD_TIME");
        returnTime = rs.getTimestamp("RETURN_TIME");
        allowTime = rs.getTimestamp("ALLOW_TIME");
        sb.append("seqId:\"" + seqId + "\"");
        sb.append(",fileId:\"" + fileId + "\"");
        sb.append(",fileCode:\"" + (fileCode == null ? "" : YHUtility.encodeSpecial(fileCode)) + "\"");
        sb.append(",addTime:\"" + (addTime == null ? "" : String.valueOf(addTime).subSequence(0, String.valueOf(addTime).length() - 2)) + "\"");
        sb.append(",returnTime:\"" + (returnTime == null ? "" : String.valueOf(returnTime).subSequence(0, String.valueOf(returnTime).length() - 2)) + "\"");
        sb.append(",allowTime:\"" + (allowTime == null ? "" : String.valueOf(allowTime).subSequence(0, String.valueOf(allowTime).length() - 2)) + "\"");
        sb.append("},");
      }
      if(sb.length() > 1){
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return sb;
  }

	/**
	 * 已批准借阅
	 * @param dbConn
	 * @param person
	 * @return
	 * @throws Exception
	 */
  public StringBuffer getApprovaledLend(Connection dbConn, YHPerson person, String allow) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHRmsFile rmsFile = null;
    YHRmsLend rmsLend = null;
    int seqId = 0;
    int fileId = 0;
    String fileCode = "";
    Date addTime = null;
    Date returnTime = null;
    Date allowTime = null;
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    try {
      stmt = dbConn.createStatement();
      String sql = "select "
          + "RMS_LEND.SEQ_ID"
          + ",RMS_LEND.FILE_ID"
          + ",RMS_FILE.FILE_CODE"
          + ",RMS_LEND.ADD_TIME"
          + ",RMS_LEND.RETURN_TIME"
          + ",RMS_LEND.ALLOW_TIME"
          + " , u.USER_NAME"
          + " from oa_archives_lend as RMS_LEND left join oa_archives_attach as RMS_FILE on RMS_FILE.SEQ_ID = RMS_LEND.FILE_ID left join PERSON u on u.SEQ_ID = RMS_LEND.USER_ID where ALLOW = '" + allow + "' and ((RMS_LEND.APPROVE = '' or RMS_LEND.APPROVE is null) or RMS_LEND.APPROVE = '" + String.valueOf(person.getSeqId()) + "')";
      if ("0".equals(allow)) {
        sql = sql + " order by RMS_LEND.ADD_TIME desc";
      } else if ("3".equals(allow)) {
        sql = sql + " order by RMS_LEND.RETURN_TIME desc";
      } else {
        sql = sql + " order by RMS_LEND.ALLOW_TIME desc";
      }
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        sb.append("{");
        rmsFile = new YHRmsFile();
        rmsLend = new YHRmsLend();
        seqId = rs.getInt("SEQ_ID");
        fileId =  rs.getInt("FILE_ID");
        fileCode = rs.getString("FILE_CODE");
        addTime = rs.getTimestamp("ADD_TIME");
        returnTime = rs.getTimestamp("RETURN_TIME");
        allowTime = rs.getTimestamp("ALLOW_TIME");
        String userName = rs.getString("USER_NAME");
        sb.append("seqId:\"" + seqId + "\"");
        sb.append(",fileId:\"" + fileId + "\"");
        sb.append(",fileCode:\"" + (fileCode == null ? "" : YHUtility.encodeSpecial(fileCode)) + "\"");
        sb.append(",addTime:\"" + (addTime == null ? "" : String.valueOf(addTime).subSequence(0, String.valueOf(addTime).length() - 2)) + "\"");
        sb.append(",returnTime:\"" + (returnTime == null ? "" : String.valueOf(returnTime).subSequence(0, String.valueOf(returnTime).length() - 2)) + "\"");
        sb.append(",allowTime:\"" + (allowTime == null ? "" : String.valueOf(allowTime).subSequence(0, String.valueOf(allowTime).length() - 2)) + "\"");
        sb.append(",userName:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(userName)) + "\"");
        sb.append("},");
      }
      if(sb.length() > 1){
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return sb;
  }
}
