package yh.subsys.oa.meeting.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.workflow.util.YHFlowHookUtility;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.meeting.data.YHMeeting;
import yh.subsys.oa.meeting.data.YHMeetingRoom;

public class YHMeetingLogic {
	private static Logger log = Logger.getLogger(YHMeetingLogic.class);

	/**
	 * 新建会议申请信息
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @param map
	 * @throws Exception
	 */
	public String setMeetingValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person, Map<Object, Object> map) throws Exception {

		try {
			boolean fromFolderFlag = (Boolean) map.get("fromFolderFlag");
			String newAttchId = (String) map.get("newAttchId");
			String newAttchName = (String) map.get("newAttchName");
			boolean uploadFlag = (Boolean) map.get("uploadFlag");
			String attachmentId = (String) map.get("attachmentId");
			String attachmentName = (String) map.get("attachmentName");
			boolean pageAttIdFlag = (Boolean) map.get("pageAttIdFlag");
			String pageAttachmentId = (String) map.get("pageAttachmentId");
			String pageattachmentName = (String) map.get("pageattachmentName");
			YHMeeting meeting = new YHMeeting();
			if (fromFolderFlag && uploadFlag && pageAttIdFlag) {
				meeting.setAttachmentId(newAttchId.trim() + attachmentId.trim() + pageAttachmentId.trim());
				meeting.setAttachmentName(newAttchName.trim() + attachmentName.trim() + pageattachmentName.trim());
			} else if (fromFolderFlag && uploadFlag) {
				meeting.setAttachmentId(newAttchId.trim() + attachmentId.trim());
				meeting.setAttachmentName(newAttchName.trim() + attachmentName.trim());
			} else if (fromFolderFlag && pageAttIdFlag) {
				meeting.setAttachmentId(newAttchId.trim() + pageAttachmentId.trim());
				meeting.setAttachmentName(newAttchName.trim() + pageattachmentName.trim());
			} else if (uploadFlag && pageAttIdFlag) {
				meeting.setAttachmentId(attachmentId.trim() + pageAttachmentId.trim());
				meeting.setAttachmentName(attachmentName.trim() + pageattachmentName.trim());
			} else if (fromFolderFlag) {
				meeting.setAttachmentId(newAttchId.trim());
				meeting.setAttachmentName(newAttchName.trim());
			} else if (uploadFlag) {
				meeting.setAttachmentId(attachmentId.trim());
				meeting.setAttachmentName(attachmentName.trim());
			} else if (pageAttIdFlag) {
				meeting.setAttachmentId(pageAttachmentId.trim());
				meeting.setAttachmentName(pageattachmentName.trim());
			}
			int mRoom = 0;
			if (!YHUtility.isNullorEmpty(fileForm.getParameter("mRoom"))) {
				mRoom = Integer.parseInt(fileForm.getParameter("mRoom"));
			}
			int resendLong = 0;
			if (!YHUtility.isNullorEmpty(fileForm.getParameter("resendLong"))) {
				resendLong = Integer.parseInt(fileForm.getParameter("resendLong"));
			}
			int resendSeveral = 0;
			if (!YHUtility.isNullorEmpty(fileForm.getParameter("resendSeveral"))) {
				resendSeveral = Integer.parseInt(fileForm.getParameter("resendSeveral"));
			}
			String smsRemind = fileForm.getParameter("smsRemind");
			if (YHUtility.isNullorEmpty(smsRemind)) {
				smsRemind = "0";
			}
			String sms2Remind = fileForm.getParameter("sms2Remind");
			if (YHUtility.isNullorEmpty(sms2Remind)) {
				sms2Remind = "0";
			}
			String calendar = fileForm.getParameter("calendar");
			if (YHUtility.isNullorEmpty(calendar)) {
				calendar = "0";
			}
			if ("on".equals(calendar)) {
				calendar = "1";
			}
			String recorder = fileForm.getParameter("recorder");
			if (YHUtility.isNullorEmpty(recorder)) {
				recorder = "";
			}
			String mStatus = fileForm.getParameter("mStatus");
			if (YHUtility.isNullorEmpty(mStatus)) {
				mStatus = "0";
			}
			String cycle = fileForm.getParameter("cycle");
			if (YHUtility.isNullorEmpty(cycle)) {
				cycle = "0";
			}
			String smsReminde1 = fileForm.getParameter("smsReminde1");
			String smsReminde2 = fileForm.getParameter("smsReminde2");
			if (YHUtility.isNullorEmpty(smsReminde1)) {
				smsReminde1 = "0";
			}
			if (YHUtility.isNullorEmpty(smsReminde2)) {
				smsReminde2 = "0";
			}
			String mName = fileForm.getParameter("mName");
			String mTopic = fileForm.getParameter("mTopic");
			String mDesc = fileForm.getParameter("mDesc");
			String mAttendee = fileForm.getParameter("mAttendee");
			String mStart = fileForm.getParameter("mStart");
			String mEnd = fileForm.getParameter("mEnd");
			String mManager = fileForm.getParameter("mManager");
			String mAttendeeOut = fileForm.getParameter("mAttendeeOut");
			String toId = fileForm.getParameter("toId");
			String privId = fileForm.getParameter("privId");
			String secretToId = fileForm.getParameter("secretToId");
			String equipmentIdStr = fileForm.getParameter("checkEquipmentes");

			meeting.setMName(mName);
			meeting.setMTopic(mTopic);
			meeting.setMDesc(mDesc);
			meeting.setMProposer(String.valueOf(person.getSeqId()));
			meeting.setMRequestTime(YHUtility.parseTimeStamp());
			meeting.setMAttendee(mAttendee);
			meeting.setMStart(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", mStart));
			meeting.setMEnd((YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", mEnd)));
			meeting.setMRoom(mRoom);
			meeting.setMManager(mManager);
			meeting.setMAttendeeOut(mAttendeeOut);
			meeting.setSmsRemind(smsRemind);
			meeting.setSms2Remind(sms2Remind);
			meeting.setToId(toId);
			meeting.setPrivId(privId);
			meeting.setSecretToId(secretToId);
			meeting.setResendLong(resendLong);
			meeting.setResendSeveral(resendSeveral);
			meeting.setEquipmentIdStr(equipmentIdStr);
			meeting.setCalendar(calendar);
			meeting.setRecorder(recorder);
			meeting.setMStatus(mStatus);
			meeting.setCycle(cycle);
			this.addMeetingInfo(dbConn, meeting);

			int maxSeqId = this.getMaxMeetingId(dbConn);
			String mManagerStr = mManager;
			String userName = person.getUserName();
			
			Map dataMap = new HashMap();
			dataMap.put("KEY", maxSeqId + "");
      dataMap.put("FIELD", "M_ID");
      dataMap.put("M_ATTENDEE_OUT", mAttendeeOut);
      dataMap.put("M_ATTENDEE", mAttendee);
      dataMap.put("M_START", mStart );
      dataMap.put("M_END", mEnd );
      dataMap.put("M_NAME", mName);
      dataMap.put("M_TOPIC", mTopic);
      dataMap.put("M_DESC", mDesc);
      dataMap.put("USER_ID", person.getSeqId() + "");

      dataMap.put("ATTACHMENT_ID", pageAttachmentId);
      dataMap.put("ATTACHMENT_NAME", pageattachmentName);
      dataMap.put("MODULE_SRC", "meeting");
      dataMap.put("MODULE_DESC", "workflow");
      
      YHFlowHookUtility ut = new YHFlowHookUtility();
      String url = ut.runHook(dbConn, person, dataMap, "meeting_apply");
      if (!"".equals(url)) {
        return url;
      }
			String content = userName + " 向您提交会议申请，请批示！";
			int fromId = person.getSeqId();
			if (!YHUtility.isNullorEmpty(mManagerStr) && "1".equals(smsReminde1)) {
				String remindUrl = "/subsys/oa/meeting/query/meetingdetail.jsp?seqId=" + maxSeqId + "&openFlag=1&openWidth=860&openHeight=650";
				this.doSmsBack2(dbConn, content, fromId, mManagerStr, "8", remindUrl, YHUtility.parseTimeStamp());
			}
			if (!YHUtility.isNullorEmpty(mManagerStr) && "1".equals(smsReminde2)) {
				YHMobileSms2Logic sbl = new YHMobileSms2Logic();
				sbl.remindByMobileSms(dbConn, mManagerStr, fromId, content, YHUtility.parseTimeStamp());
			}

		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	/**
	 * 获取最大的SeqId值
	 * 
	 * @param dbConn
	 * @return
	 */
	public int getMaxMeetingId(Connection dbConn) {
		String sql = "select SEQ_ID from oa_conference where SEQ_ID=(select MAX(SEQ_ID) from oa_conference )";
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
	 * 添加信息
	 * 
	 * @param dbConn
	 * @param rmsFile
	 * @throws Exception
	 */
	public void addMeetingInfo(Connection dbConn, YHMeeting meeting) throws Exception {
		try {
			YHORM orm = new YHORM();
			orm.saveSingle(dbConn, meeting);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 会议查询--cc
	 * 
	 * @param dbConn
	 * @param request
	 * @param person
	 * @param mName
	 * @param mProposer
	 * @param beginDate
	 * @param endDate
	 * @param mRoom
	 * @param mStatus
	 * @return
	 * @throws Exception
	 */
	public String getMeetingSearchJson(Connection dbConn, Map request, YHPerson person, String mName, String mProposer, String beginDate,
			String endDate, String mRoom, String mStatus) throws Exception {
		String sql = "";
		if (person.isAdminRole()) {
			sql = "select " +
					"" + "SEQ_ID" 
					+ ", M_NAME" 
					+ ", M_PROPOSER"
					+ ", M_ATTENDEE" 
					+ ", M_START" 
					+ ", M_STATUS" 
					+ ", M_ATTENDEE_OUT"
					+ ", M_ROOM"
					 + " , RECORDER"
			      + " , M_MANAGER "
					+ " from oa_conference where 1=1 ";
		} else {
			sql = "select " + "SEQ_ID" 
			+ ", M_NAME" 
			+ ", M_PROPOSER"
			+ ", M_ATTENDEE"
			+ ", M_START" 
			+ ", M_STATUS"
			+ ", M_ATTENDEE_OUT" 
			+ ", M_ROOM"
			+ " , RECORDER"
			+ " , M_MANAGER "
					+ " from oa_conference where (TO_ID='ALL_DEPT' or TO_ID='0' or " + YHDBUtility.findInSet(String.valueOf(person.getDeptId()), "TO_ID") + " or "
					+ YHDBUtility.findInSet(person.getUserPriv(), "PRIV_ID") + " or "
					+ YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "SECRET_TO_ID") + " or "
					+ YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "M_ATTENDEE")
					+ " or M_PROPOSER='" + String.valueOf(person.getSeqId())
					+ "' or M_MANAGER='" + String.valueOf(person.getSeqId()) + "')";
		}
		if (!YHUtility.isNullorEmpty(mName)) {
			sql = sql + " and M_NAME like '%" + mName + "%'" + YHDBUtility.escapeLike();
		}
		if (!YHUtility.isNullorEmpty(mProposer)) {
			sql = sql + " and M_PROPOSER like '%" + mProposer + "%'" + YHDBUtility.escapeLike();
		}
		if (!YHUtility.isNullorEmpty(beginDate)) {
			sql = sql + " and " + YHDBUtility.getDateFilter("M_REQUEST_TIME", beginDate, ">=");
		}
		if (!YHUtility.isNullorEmpty(endDate)) {
			sql = sql + " and " + YHDBUtility.getDateFilter("M_REQUEST_TIME", endDate, ">=");
		}
		if (!YHUtility.isNullorEmpty(mRoom)) {
			sql = sql + " and M_ROOM like '%" + mRoom + "%'" + YHDBUtility.escapeLike();
		}
		if (!YHUtility.isNullorEmpty(mStatus)) {
			sql = sql + " and M_STATUS like '%" + mStatus + "%'" + YHDBUtility.escapeLike();
		}
		sql = sql + " order by M_START desc, M_ROOM desc";

		YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
		YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);

		return pageDataList.toJson();
	}

	/**
	 * 会议纪要列表
	 * 
	 * @param dbConn
	 * @param request
	 * @param mName
	 * @param mProposer
	 * @param beginDate
	 * @param endDate
	 * @param keyWord1
	 * @param keyWord2
	 * @param keyWord3
	 * @param mRoom
	 * @return
	 * @throws Exception
	 */
	public String getMeetingSummarySearchJson(Connection dbConn, Map request, String mName, String mProposer, String beginDate, String endDate,
			String keyWord1, String keyWord2, String keyWord3, String mRoom) throws Exception {
		String sql = "select " + "SEQ_ID" + ", M_NAME" + ", M_PROPOSER" + ", M_START" + ", M_END" + " from oa_conference where 1=1 ";

		if (!YHUtility.isNullorEmpty(mName)) {
			sql = sql + " and M_NAME like '%" + mName + "%'" + YHDBUtility.escapeLike();
		}
		if (!YHUtility.isNullorEmpty(mProposer)) {
			sql = sql + " and M_PROPOSER like '%" + mProposer + "%'" + YHDBUtility.escapeLike();
		}
		if (!YHUtility.isNullorEmpty(beginDate)) {
			sql = sql + " and " + YHDBUtility.getDateFilter("M_START", beginDate, ">=");
		}
		if (!YHUtility.isNullorEmpty(endDate)) {
			sql = sql + " and " + YHDBUtility.getDateFilter("M_START", endDate, ">=");
		}
		if (!YHUtility.isNullorEmpty(mRoom)) {
			sql = sql + " and M_ROOM like '%" + mRoom + "%'" + YHDBUtility.escapeLike();
		}
		if (!YHUtility.isNullorEmpty(keyWord1)) {
			sql = sql + " and SUMMARY like '%" + keyWord1 + "%'" + YHDBUtility.escapeLike();
		}
		if (!YHUtility.isNullorEmpty(keyWord2)) {
			sql = sql + " and SUMMARY like '%" + keyWord2 + "%'" + YHDBUtility.escapeLike();
		}
		if (!YHUtility.isNullorEmpty(keyWord3)) {
			sql = sql + " and SUMMARY like '%" + keyWord3 + "%'" + YHDBUtility.escapeLike();
		}
		sql = sql + " order by M_START desc";

		YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
		YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);

		return pageDataList.toJson();
	}

	/**
	 * 取得用户名称--cc
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws Exception
	 */

	public String getUserNameLogic(Connection conn, int userId) throws Exception {
		String result = "";
		String sql = " select USER_NAME from PERSON where SEQ_ID = " + userId;
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
	 * 获取会议室名称--cc
	 * 
	 * 
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getMeetingRoomNameLogic(Connection conn, int seqId) throws Exception {
		String result = "";
		String sql = " select MR_NAME from oa_conference_room where SEQ_ID = " + seqId;
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
	 * 获取会议室详细信息--cc
	 * 
	 * @param conn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public YHMeeting getMeetingRoomDetail(Connection conn, int seqId) throws Exception {
		try {
			YHORM orm = new YHORM();
			return (YHMeeting) orm.loadObjSingle(conn, YHMeeting.class, seqId);
		} catch (Exception ex) {
			throw ex;
		} finally {

		}
	}

	/**
	 * 会议管理通用列表--cc
	 * 
	 * @param dbConn
	 * @param request
	 * @param mStatus
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getMeetingListJson(Connection dbConn, Map request, String mStatus, YHPerson person) throws Exception {
		String sql = "";
		if (person.isAdminRole()) {
			if ("0".equals(mStatus)) {
				sql = "select " 
				  + "  SEQ_ID"
				  + ", M_NAME" 
				  + ", M_PROPOSER"
				  + ", M_START"
				  + ", M_ROOM" 
				  + ", M_STATUS"
				  + ",1,1 "
				  + "  from oa_conference where M_STATUS='"
						+ mStatus + "' and not CYCLE = '1'";
			} else {
				sql = "select " 
				  + "  SEQ_ID"
				  + ", M_NAME"
				  + ", M_PROPOSER" 
				  + ", M_START" 
				  + ", M_ROOM" 
				  + ", M_STATUS" 
				  + ",1,1 "
				  + " from oa_conference where M_STATUS='"
						+ mStatus + "'";
			}

		} else {
			if ("0".equals(mStatus)) {
				sql = "select " 
				  + "  SEQ_ID" 
				  + ", M_NAME"
				  + ", M_PROPOSER" 
				  + ", M_START" 
				  + ", M_ROOM" 
				  + ", M_STATUS" 
				  + ",1,1 "
				  + " from oa_conference where M_STATUS='"
						+ mStatus + "' and not CYCLE = '1' and M_MANAGER = '" + String.valueOf(person.getSeqId()) + "'";
			} else {
				sql = "select "
				  + "  SEQ_ID"
				  + ", M_NAME"
				  + ", M_PROPOSER" 
				  + ", M_START" 
				  + ", M_ROOM"
				  + ", M_STATUS" 
				  + ",1,1 "
				  + " from oa_conference where M_STATUS='"
						+ mStatus + "' and M_MANAGER = '" + String.valueOf(person.getSeqId()) + "'";
			}
		}
		YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
		YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
		YHFlowHookUtility fu = new YHFlowHookUtility();
    YHFlowRunUtility ru = new YHFlowRunUtility();
		for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
      YHDbRecord r = pageDataList.getRecord(i);
      int seqId = YHUtility.cast2Long(r.getValueByName("seqId")).intValue();
      int runId = fu.isRunHook(dbConn, "M_ID", seqId + "");
      int flowId = 0;
      if (runId != 0) {
        flowId = ru.getFlowId(dbConn, runId);
      }
      r.addField("flowId", flowId);
      r.addField("runId", runId);
    }
		return pageDataList.toJson();
	}

	/**
	 * 会议申请通用列表--wyw
	 * 
	 * @param dbConn
	 * @param request
	 * @param mStatus
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getMeetingListJson2(Connection dbConn, Map request, String mStatus, YHPerson person) throws Exception {
		String sql = "select " + " MEETING.SEQ_ID" + ", M_NAME"
				// + ", M_TOPIC"
				+ ", M_PROPOSER"
				// + ", M_REQUEST_TIME"
				+ ", M_ATTENDEE" + ", M_START" + ", M_END" + ", M_ROOM" + ", M_STATUS "
				// + ", M_MANAGER"
				+ ", M_ATTENDEE_OUT , 1 , 1 " + " from oa_conference as MEETING, PERSON where M_STATUS='" + mStatus + "' and M_PROPOSER = '" + String.valueOf(person.getSeqId())
				+ "' and PERSON.SEQ_ID=" + person.getSeqId() + " order by M_STATUS";
		YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
		YHFlowHookUtility fu = new YHFlowHookUtility();
    YHFlowRunUtility ru = new YHFlowRunUtility();
		YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
		for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
      YHDbRecord r = pageDataList.getRecord(i);
      int seqId = YHUtility.cast2Long(r.getValueByName("seqId")).intValue();
      int runId = fu.isRunHook(dbConn, "M_ID", seqId + "");
      int flowId = 0;
      if (runId != 0) {
        flowId = ru.getFlowId(dbConn, runId);
      }
      r.addField("flowId", flowId);
      r.addField("runId", runId);
    }
		return pageDataList.toJson();
	}

	/**
	 * 待批周期性列表--cc
	 * 
	 * @param dbConn
	 * @param request
	 * @param mStatus
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getMeetingCycleListJson(Connection dbConn, Map request, YHPerson person) throws Exception {
		String sql = "";
		if (person.isAdminRole()) {
			sql = "select " + "  SEQ_ID" + ", M_NAME" + ", M_PROPOSER" + ", M_START" + ", M_ROOM" + ", CYCLE_NO" + ", M_STATUS"
					+ " from oa_conference where SEQ_ID IN (select min(SEQ_ID) from oa_conference where M_STATUS='0' and CYCLE='1' group by CYCLE_NO) ";
		} else {
			sql = "select " + "  SEQ_ID" + ", M_NAME" + ", M_PROPOSER" + ", M_START" + ", M_ROOM" + ", CYCLE_NO" + ", M_STATUS"
					+ " from oa_conference where SEQ_ID IN (select min(SEQ_ID) from oa_conference where M_STATUS='0' and M_MANAGER = " + person.getSeqId()
					+ " and CYCLE='1' group by CYCLE_NO) ";
		}

		YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
		YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
		return pageDataList.toJson();
	}

	public String getMStartLogic(Connection conn, String cyleNo) throws Exception {
		String result = "";
		String sql = " select M_START from oa_conference where CYCLE_NO = " + cyleNo;
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

	public String getMeetingManageCycleSeqIdStr(Connection dbConn, Map request, HttpServletRequest fileForm, String cycleNo, YHPerson person,
			String flag) throws Exception, Exception {
		String result = "";
		String sql = "";
		if (person.isAdminRole()) {
			sql = "select " + "  SEQ_ID" + ", M_NAME" + ", M_PROPOSER" + ", M_START" + ", M_ROOM" + ", CYCLE_NO" + ", M_STATUS"
					+ " from oa_conference where M_STATUS='0' and CYCLE='1' and CYCLE_NO = '" + cycleNo + "'";
		} else {
			sql = "select " + "  SEQ_ID" + ", M_NAME" + ", M_PROPOSER" + ", M_START" + ", M_ROOM" + ", CYCLE_NO" + ", M_STATUS"
					+ " from oa_conference where M_STATUS='0' and CYCLE='1' and CYCLE_NO = '" + cycleNo + "' and M_MANAGER = '" + String.valueOf(person.getSeqId())
					+ "'";
		}
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = dbConn.createStatement();
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				String seqIdStr = String.valueOf(rs.getInt("SEQ_ID"));
				String mStart = String.valueOf(rs.getDate("M_START"));
				int curDay = this.getDateWeek(mStart);
				if (!"0".equals(flag)) {
					if (curDay == 1 && YHUtility.isNullorEmpty(fileForm.getParameter("W11"))) {
						continue;
					}
					if (curDay == 2 && YHUtility.isNullorEmpty(fileForm.getParameter("W12"))) {
						continue;
					}
					if (curDay == 3 && YHUtility.isNullorEmpty(fileForm.getParameter("W13"))) {
						continue;
					}
					if (curDay == 4 && YHUtility.isNullorEmpty(fileForm.getParameter("W14"))) {
						continue;
					}
					if (curDay == 5 && YHUtility.isNullorEmpty(fileForm.getParameter("W15"))) {
						continue;
					}
					if (curDay == 6 && YHUtility.isNullorEmpty(fileForm.getParameter("W16"))) {
						continue;
					}
					if (curDay == 7 && YHUtility.isNullorEmpty(fileForm.getParameter("W17"))) {
						continue;
					}
				}
				if (!"".equals(result)) {
					result += ",";
				}
				result += seqIdStr;
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stm, rs, null);
		}
		return result;
	}

	public String getMeetingManageCycleList(Connection dbConn, Map request, HttpServletRequest fileForm, String cycleNo, YHPerson person, String flag)
			throws Exception {
		String idStr = getMeetingManageCycleSeqIdStr(dbConn, request, fileForm, cycleNo, person, flag);
		String sql = "";
		if (YHUtility.isNullorEmpty(idStr)) {
			idStr = "-1";
		}
		sql = "select " + "  SEQ_ID" + ", M_NAME" + ", M_PROPOSER" + ", M_START" + ", M_ROOM" + ", CYCLE_NO" + ", M_STATUS"
				+ " from oa_conference where SEQ_ID IN (" + idStr + ")";
		YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
		YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
		return pageDataList.toJson();
	}

	/**
	 * 删除单个会议记录--cc
	 * 
	 * @param conn
	 * @param seqId
	 * @throws Exception
	 */
	public void deleteSingle(Connection conn, int seqId) throws Exception {
		try {
			YHORM orm = new YHORM();
			orm.deleteSingle(conn, YHMeeting.class, seqId);
		} catch (Exception ex) {
			throw ex;
		} finally {
		}
	}

	/**
	 * 删除会议记录（含附件）--wyw
	 * 
	 * @param dbConn
	 * @param seqId
	 * @throws Exception
	 */
	public void delMeetingLogic(Connection dbConn, int seqId, String filePath) throws Exception {
		YHORM orm = new YHORM();
		try {
			StringBuffer attIdBuffer = new StringBuffer();
			StringBuffer attNameBuffer = new StringBuffer();
			YHMeeting meeting = (YHMeeting) orm.loadObjSingle(dbConn, YHMeeting.class, seqId);
			String dbAttachmentId = "";
			String dbAttachmentName = "";
			String dbAttachmentId1 = "";
			String dbAttachmentName1 = "";
			if (meeting != null) {
				dbAttachmentId = YHUtility.null2Empty(meeting.getAttachmentId());
				dbAttachmentName = YHUtility.null2Empty(meeting.getAttachmentName());

				// 会议纪要附件
				dbAttachmentId1 = YHUtility.null2Empty(meeting.getAttachmentId1());
				dbAttachmentName1 = YHUtility.null2Empty(meeting.getAttachmentName1());

				attIdBuffer.append(dbAttachmentId.trim() + dbAttachmentId1.trim());
				attNameBuffer.append(dbAttachmentName.trim() + dbAttachmentName1.trim());
			}
			String[] attIdArray = {};
			String[] attNameArray = {};
			if (!YHUtility.isNullorEmpty(attIdBuffer.toString()) && !YHUtility.isNullorEmpty(attNameBuffer.toString()) && attIdBuffer.length() > 0) {
				attIdArray = attIdBuffer.toString().trim().split(",");
				attNameArray = attNameBuffer.toString().trim().split("\\*");
			}

			if (attIdArray != null && attIdArray.length > 0) {
				for (int i = 0; i < attIdArray.length; i++) {
					Map<String, String> map = this.getFileName(attIdArray[i], attNameArray[i]);
					if (map.size() != 0) {
						Set<String> set = map.keySet();
						// 遍历Set集合
						for (String keySet : set) {
							String key = keySet;
							String keyValue = map.get(keySet);
							String attaIdStr = this.getAttaId(keySet);
							String fileNameValue = attaIdStr + "_" + keyValue;
							String fileFolder = this.getFilePathFolder(key);
							String oldFileNameValue = attaIdStr + "." + keyValue;
							File file = new File(filePath + File.separator + fileFolder + File.separator + fileNameValue);
							File oldFile = new File(filePath + File.separator + fileFolder + File.separator + oldFileNameValue);
							if (file.exists()) {
								YHFileUtility.deleteAll(file.getAbsoluteFile());
							} else if (oldFile.exists()) {
								YHFileUtility.deleteAll(oldFile.getAbsoluteFile());
							}
						}
					}
				}
			}
			// 删除数据库信息
			orm.deleteSingle(dbConn, meeting);
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 拼接附件Id与附件名--wyw
	 * 
	 * @param attachmentId
	 * @param attachmentName
	 * @return
	 */
	public Map<String, String> getFileName(String attachmentId, String attachmentName) {
		Map<String, String> map = new HashMap<String, String>();
		if (attachmentId == null || attachmentName == null) {
			return map;
		}
		if (!"".equals(attachmentId.trim()) && !"".equals(attachmentName.trim())) {
			String attachmentIds[] = attachmentId.split(",");
			String attachmentNames[] = attachmentName.split("\\*");
			if (attachmentIds.length != 0 && attachmentNames.length != 0) {
				for (int i = 0; i < attachmentIds.length; i++) {
					map.put(attachmentIds[i], attachmentNames[i]);
				}
			}
		}
		return map;
	}

	/**
	 * 得到附件的Id 兼老数据 --wyw
	 * 
	 * @param keyId
	 * @return
	 */
	public String getAttaId(String keyId) {
		String attaId = "";
		if (keyId != null && !"".equals(keyId)) {
			if (keyId.indexOf('_') != -1) {
				String[] ids = keyId.split("_");
				if (ids.length > 0) {
					attaId = ids[1];
				}

			} else {
				attaId = keyId;
			}
		}
		return attaId;
	}

	/**
	 * 得到该文件的文件夹名--wyw
	 * 
	 * @param key
	 * @return
	 */
	public String getFilePathFolder(String key) {
		String folder = "";
		if (key != null && !"".equals(key)) {
			if (key.indexOf('_') != -1) {
				String[] str = key.split("_");
				for (int i = 0; i < str.length; i++) {
					folder = str[0];
				}
			} else {
				folder = "all";
			}
		}
		return folder;
	}

	/**
	 * 修改 m_status --cc
	 * 
	 * @param dbConn
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public static void updateStatus(Connection dbConn, int seqId, String mStatus) throws Exception {
		String sql = "update oa_conference set M_STATUS = ? where SEQ_ID = ?";
		PreparedStatement ps = null;
		try {
			ps = dbConn.prepareStatement(sql);
			ps.setString(1, mStatus);
			ps.setInt(2, seqId);
			ps.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, null, log);
		}
	}

	/**
	 * 短信提醒 --cc
	 * 
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
	 * 短信提醒(带时间)--cc
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
	public static void doSmsBack2(Connection conn, String content, int fromId, String toId, String type, String remindUrl, Date sendDate)
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
	 * 邮件提醒 --cc
	 * 
	 * @param conn
	 * @param content
	 * @param fromId
	 * @param toId
	 * @param type
	 * @param remindUrl
	 * @throws Exception
	 */
	public static void doEmailBack(Connection conn, String content, int fromId, String toId, String type, String remindUrl) throws Exception {
		YHSmsBack sb = new YHSmsBack();
		sb.setContent(content);
		sb.setFromId(fromId);
		sb.setToId(toId);
		sb.setSmsType(type);
		sb.setRemindUrl(remindUrl);
		YHSmsUtil.smsBack(conn, sb);
	}

	/**
	 * 自动开始--cc
	 * 
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public void getAutoBegin(Connection conn) throws Exception {
		String curDateStr = YHUtility.getCurDateTimeStr();
		String sql = " SELECT SEQ_ID, M_START from oa_conference where M_STATUS = 1 ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				int seqId = rs.getInt("SEQ_ID");
				Date mStart = rs.getTimestamp("M_START");
				if (mStart.before(new Date()) || curDateStr.equals(mStart)) {
					updateStatus(conn, seqId, "2");
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
	}

	/**
	 * 自动结束--cc
	 * 
	 * @param conn
	 * @throws Exception
	 */
	public void getAutoEnd(Connection conn) throws Exception {
		String curDateStr = YHUtility.getCurDateTimeStr();
		String sql = " SELECT SEQ_ID, M_END from oa_conference where M_STATUS = 2 ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				int seqId = rs.getInt("SEQ_ID");
				Date mStart = rs.getTimestamp("M_END");
				if (mStart.before(new Date()) || mStart.equals(new Date())) {
					updateStatus(conn, seqId, "4");
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
	}

	/**
	 * 预约会议室是否冲突(返回true或者false)--cc
	 * 
	 * 
	 * @param conn
	 * @param seqId
	 * @param mRoom
	 * @param mStarts
	 * @param mEnds
	 * @return
	 * @throws Exception
	 */
	public boolean checkRoom(Connection conn, int seqId, int mRoom, Date mStart, Date mEnd) throws Exception {
		String result = "";
		int count = 0;
		String sql = "select SEQ_ID, M_START, M_END from oa_conference where not SEQ_ID = " + seqId + " and M_ROOM = " + mRoom
				+ " and (M_STATUS = '1' or M_STATUS = '2')";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String seqIdStr = String.valueOf(rs.getInt("SEQ_ID"));
				Date mStart1 = rs.getTimestamp("M_START");
				Date mEnd1 = rs.getTimestamp("M_END");
				if (((mStart1.after(mStart) || mStart1.equals(mStart)) && mEnd1.before(mEnd)) || (mStart1.before(mStart) && mEnd1.after(mStart))
						|| (mStart1.before(mEnd) && mEnd1.after(mEnd)) || (mStart1.before(mStart) && mEnd1.after(mEnd))) {
					count++;
					if (!"".equals(result)) {
						result += ",";
					}
					result += seqIdStr;
				}
			}
			if (count >= 1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
	}

	/**
	 * 预约会议室是否冲突(返回冲突会议的ID串)--cc
	 * 
	 * 
	 * @param conn
	 * @param seqId
	 * @param mRoom
	 * @param mStarts
	 * @param mEnds
	 * @return
	 * @throws Exception
	 */
	public String checkRoomData(Connection conn, int seqId, int mRoom, Date mStart, Date mEnd) throws Exception {
		String result = "";
		int count = 0;
		String sql = "select SEQ_ID, M_START, M_END from oa_conference where not SEQ_ID = " + seqId + " and M_ROOM = " + mRoom
				+ " and (M_STATUS = '1' or M_STATUS = '2')";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String seqIdStr = String.valueOf(rs.getInt("SEQ_ID"));
				Date mStart1 = rs.getTimestamp("M_START");
				Date mEnd1 = rs.getTimestamp("M_END");
				if (((mStart1.after(mStart) || mStart1.equals(mStart)) && mEnd1.before(mEnd)) || (mStart1.before(mStart) && mEnd1.after(mStart))
						|| (mStart1.before(mEnd) && mEnd1.after(mEnd)) || (mStart1.before(mStart) && mEnd1.after(mEnd))) {
					count++;
					if (!"".equals(result)) {
						result += ",";
					}
					result += seqIdStr;
				}
			}
			if (count >= 1) {
				return result;
			} else {
				return "";
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
	}

	/**
	 * 与下列会议冲突列表--cc
	 * 
	 * 
	 * @param dbConn
	 * @param request
	 * @param cycleNo
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getMeetingConflict(Connection dbConn, Map request, String seqId) throws Exception {
		String sql = "";
		sql = "select " + "  SEQ_ID" + ", M_NAME" + ", M_START" + ", M_END" + " from oa_conference where SEQ_ID IN ('" + seqId + "')";

		YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
		YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
		return pageDataList.toJson();
	}

	/**
	 * 预约情况－获取会议室信息--cc
	 * 
	 * @param dbConn
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public ArrayList<YHMeetingRoom> selectMeetingRoom(Connection dbConn, YHPerson person) throws Exception {
		YHORM orm = new YHORM();
		String[] str = { YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "SECRET_TO_ID") + " or "
				+ YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "OPERATOR") + " or "
				+ YHDBUtility.findInSet(String.valueOf(person.getDeptId()), "TO_ID")
				+ " or (TO_ID like 'ALL_DEPT' or TO_ID like '0') or (TO_ID is null and SECRET_TO_ID is null)" };

		ArrayList<YHMeetingRoom> meetingRoomList = (ArrayList<YHMeetingRoom>) orm.loadListSingle(dbConn, YHMeetingRoom.class, str);
		return meetingRoomList;
	}

	/**
	 * 预约情况－获取会议信息--cc
	 * 
	 * @param dbConn
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public List<YHMeeting> selectMeetingStatus(Connection dbConn, int roomId, String date) throws Exception {
		YHORM orm = new YHORM();
		String[] str2 = { "M_STATUS <> '3' and M_STATUS <> '4' and M_ROOM='" + roomId + "' and ((" + YHDBUtility.getDateFilter("M_START", date, ">")
				+ " and " + YHDBUtility.getDateFilter("M_START", date + " 23:59:59", "<") + ")" + " or (" + YHDBUtility.getDateFilter("M_END", date, ">")
				+ " and " + YHDBUtility.getDateFilter("M_END", date + " 23:59:59", "<") + ")" + " or (" + YHDBUtility.getDateFilter("M_START", date, "<")
				+ " and " + YHDBUtility.getDateFilter("M_END", date + " 23:59:59", ">") + ")) order by M_START" };
		List<YHMeeting> usageList = new ArrayList<YHMeeting>();
		usageList = orm.loadListSingle(dbConn, YHMeeting.class, str2);
		return usageList;
	}

	/**
	 * 获取会议信息--cc
	 * 
	 * @param dbConn
	 * @param idStr
	 * @return
	 * @throws Exception
	 */
	public ArrayList<YHMeeting> getMeetingInfo(Connection dbConn, String idStrs) throws Exception {
		YHORM orm = new YHORM();
		String[] str = { "SEQ_ID IN (" + idStrs + ")" };
		ArrayList<YHMeeting> meetingList = (ArrayList<YHMeeting>) orm.loadListSingle(dbConn, YHMeeting.class, str);
		return meetingList;
	}

	// cc end
	/**
	 * 获取会议室管理员名称--wyw
	 */
	public String getMManagerLogic(Connection dbConn) throws Exception {
		String data = "";
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			StringBuffer buffer = new StringBuffer("[");
			String paraValue = this.getParaValue(dbConn);
			if (YHUtility.isNullorEmpty(paraValue)) {
				paraValue = "-1";
			}
			String sql = "SELECT SEQ_ID,USER_NAME from person where SEQ_ID in(" + paraValue + ")  order by USER_NO,USER_NAME";
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			boolean isHave = false;
			while (rs.next()) {
				int dbSeqId = rs.getInt("SEQ_ID");
				String userName = YHUtility.null2Empty(rs.getString("USER_NAME"));
				buffer.append("{");
				buffer.append("value:" + dbSeqId);
				buffer.append(",text:\"" + YHUtility.encodeSpecial(userName) + "\"");
				buffer.append("},");
				isHave = true;
			}
			if (isHave) {
				buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
			data = buffer.toString();
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return data;
	}

	public String getParaValue(Connection dbConn) throws Exception {
		String sql = "SELECT PARA_VALUE from SYS_PARA where PARA_NAME='MEETING_OPERATOR'";
		String paraValue = "";

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				paraValue = YHUtility.null2Empty(rs.getString("PARA_VALUE"));
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stmt, null, log);
		}
		return paraValue;
	}

	/**
	 * 获取有权限的会议室名称(暂留)
	 * 
	 * @param dbConn
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getMRoomNameLogic1(Connection dbConn, YHPerson person) throws Exception {
		StringBuffer buffer = new StringBuffer("[");
		try {
			List<YHMeetingRoom> meetingRooms = this.getMRoomList(dbConn);
			boolean isHave = false;
			if (meetingRooms != null && meetingRooms.size() > 0) {
				for (YHMeetingRoom meetingRoom : meetingRooms) {
					String secretToId = YHUtility.null2Empty(meetingRoom.getSecretToId());
					String operator = YHUtility.null2Empty(meetingRoom.getOperator());
					boolean secretToIdFlag = this.isLonginUser(secretToId, String.valueOf(person.getSeqId()));
					boolean operatorFlag = this.isLonginUser(operator, String.valueOf(person.getSeqId()));
					if (secretToIdFlag || operatorFlag) {
						int dbSeqId = meetingRoom.getSeqId();
						String mrName = YHUtility.null2Empty(meetingRoom.getMrName());
						String mrDesc = YHUtility.null2Empty(meetingRoom.getMrDesc());
						buffer.append("{");
						buffer.append("value:" + dbSeqId);
						buffer.append(",text:\"" + YHUtility.encodeSpecial(mrName) + "\"");
						buffer.append(",mrDesc:\"" + YHUtility.encodeSpecial(mrDesc) + "\"");
						buffer.append("},");
						isHave = true;
					}
				}
			}
			if (isHave) {
				buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
		} catch (Exception e) {
			throw e;
		}
		return buffer.toString();
	}

	/**
	 * 获取有权限的会议室名称
	 * 2011-4-13
	 * @param dbConn
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getMRoomNameLogic(Connection dbConn, YHPerson person) throws Exception {
		StringBuffer buffer = new StringBuffer("[");
		// String sql =
		// "SELECT SEQ_ID,MR_NAME,MR_DESC from MEETING_ROOM where  find_in_set('$LOGIN_USER_ID',SECRET_TO_ID) or find_in_set('$LOGIN_USER_ID',OPERATOR) or TO_ID='ALL_DEPT' or find_in_set('$LOGIN_DEPT_ID',TO_ID) or (TO_ID='' and SECRET_TO_ID='')";
		String sql = "SELECT SEQ_ID,MR_NAME,MR_DESC from oa_conference_room where " + YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "SECRET_TO_ID")
				+ " or " + YHDBUtility.findInSet(String.valueOf(person.getSeqId()), "OPERATOR") + " or TO_ID like 'ALL_DEPT' or TO_ID like '0' or "
				+ YHDBUtility.findInSet(String.valueOf(person.getDeptId()), "TO_ID") + " or (TO_ID  like '' and SECRET_TO_ID like '')";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			boolean isHave = false;
			while (rs.next()) {
				int dbSeqId = rs.getInt("SEQ_ID");
				String mrName = YHUtility.null2Empty(rs.getString("MR_NAME"));
				String mrDesc = YHUtility.null2Empty(rs.getString("MR_NAME"));
				buffer.append("{");
				buffer.append("value:" + dbSeqId);
				buffer.append(",text:\"" + YHUtility.encodeSpecial(mrName) + "\"");
				buffer.append(",mrDesc:\"" + YHUtility.encodeSpecial(mrDesc) + "\"");
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

	public List<YHMeetingRoom> getMRoomList(Connection dbConn) throws Exception {
		YHORM orm = new YHORM();
		try {
			Map map = new HashMap();
			return orm.loadListSingle(dbConn, YHMeetingRoom.class, map);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 比较idStrs串是否与loginUserSeqId匹配
	 * 
	 * @param idStrs
	 * @param loginUserSeqId
	 * @return
	 * @throws Exception
	 */
	public boolean isLonginUser(String idStrs, String loginUserSeqId) throws Exception {
		boolean flag = false;
		try {
			if (YHUtility.isNullorEmpty(idStrs)) {
				idStrs = "";
			}
			String[] idstrArry = idStrs.split(",");
			if (idstrArry != null && idstrArry.length != 0) {
				for (String id : idstrArry) {
					if (loginUserSeqId.equals(id)) {
						flag = true;
						return flag;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

	/**
	 * 获取会议规则
	 * 
	 * @param dbConn
	 * @return
	 * @throws Exception
	 */
	public String getMeetingRuleLogic(Connection dbConn) throws Exception {
		String sql = "select PARA_VALUE from SYS_PARA where PARA_NAME = 'MEETING_ROOM_RULE'";
		String paraVale = "";
		String data = "";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				paraVale = YHUtility.null2Empty(rs.getString("PARA_VALUE"));
			}
			data = "{meetingRule:\"" + YHUtility.encodeSpecial(paraVale) + "\"}";
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}

		return data;
	}

	/**
	 * 新建周期性会议设置--wyw
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @param map
	 * @throws Exception
	 */
	public void setCycleMeetingValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person, Map<Object, Object> map) throws Exception {
		try {
			boolean fromFolderFlag = (Boolean) map.get("fromFolderFlag");
			String newAttchId = (String) map.get("newAttchId");
			String newAttchName = (String) map.get("newAttchName");
			boolean uploadFlag = (Boolean) map.get("uploadFlag");
			String attachmentId = (String) map.get("attachmentId");
			String attachmentName = (String) map.get("attachmentName");

			YHMeeting meeting = new YHMeeting();
			if (fromFolderFlag && uploadFlag) {
				meeting.setAttachmentId(newAttchId.trim() + attachmentId.trim());
				meeting.setAttachmentName(newAttchName.trim() + attachmentName.trim());
			} else if (fromFolderFlag) {
				meeting.setAttachmentId(newAttchId.trim());
				meeting.setAttachmentName(newAttchName.trim());
			} else if (uploadFlag) {
				meeting.setAttachmentId(attachmentId.trim());
				meeting.setAttachmentName(attachmentName.trim());
			}
			int mRoom = 0;
			if (!YHUtility.isNullorEmpty(fileForm.getParameter("mRoom"))) {
				mRoom = Integer.parseInt(fileForm.getParameter("mRoom"));
			}
			int resendLong = 0;
			if (!YHUtility.isNullorEmpty(fileForm.getParameter("resendLong"))) {
				resendLong = Integer.parseInt(fileForm.getParameter("resendLong"));
			}
			int resendSeveral = 0;
			if (!YHUtility.isNullorEmpty(fileForm.getParameter("resendSeveral"))) {
				resendSeveral = Integer.parseInt(fileForm.getParameter("resendSeveral"));
			}
			String smsRemind = fileForm.getParameter("smsRemind");
			if (YHUtility.isNullorEmpty(smsRemind)) {
				smsRemind = "0";
			}
			String sms2Remind = fileForm.getParameter("sms2Remind");
			if (YHUtility.isNullorEmpty(sms2Remind)) {
				sms2Remind = "0";
			}
			String calendar = fileForm.getParameter("calendar");
			if (YHUtility.isNullorEmpty(calendar)) {
				calendar = "0";
			}
			if ("on".equals(calendar)) {
				calendar = "1";
			}
			String recorder = fileForm.getParameter("recorder");
			if (YHUtility.isNullorEmpty(recorder)) {
				recorder = "";
			}
			String mStatus = fileForm.getParameter("mStatus");
			if (YHUtility.isNullorEmpty(mStatus)) {
				mStatus = "0";
			}
			String cycle = fileForm.getParameter("cycle");
			if (YHUtility.isNullorEmpty(cycle)) {
				cycle = "0";
			}
			String rd = fileForm.getParameter("RD");
			if ("1".equals(rd)) {
				cycle = "1";
			}
			int cycleNo = 0;
			if ("1".equals(cycle)) {
				cycleNo = this.getMaxCycleNo(dbConn);
				cycleNo++;
			}
			String mStartDateStr = fileForm.getParameter("M_START_DATE");
			String mEndDateStr = fileForm.getParameter("M_END_DATE");
			Date mStartDate = null;
			if (!YHUtility.isNullorEmpty(mStartDateStr)) {
				mStartDate = YHUtility.parseDate("yyyy-MM-dd", mStartDateStr);
			}
			Date mEndDate = null;
			if (!YHUtility.isNullorEmpty(mEndDateStr)) {
				mEndDate = YHUtility.parseDate("yyyy-MM-dd", mEndDateStr);
			}
			int temDate = YHUtility.getDaySpan(mStartDate, mEndDate) + 1;

			String smsReminde1 = fileForm.getParameter("smsReminde1");
			String smsReminde2 = fileForm.getParameter("smsReminde2");
			if (YHUtility.isNullorEmpty(smsReminde1)) {
				smsReminde1 = "0";
			}
			if (YHUtility.isNullorEmpty(smsReminde2)) {
				smsReminde2 = "0";
			}

			String mName = fileForm.getParameter("mName");
			String mTopic = fileForm.getParameter("mTopic");
			String mDesc = fileForm.getParameter("mDesc");
			String mAttendee = fileForm.getParameter("mAttendee");
			String mManager = fileForm.getParameter("mManager");
			String mAttendeeOut = fileForm.getParameter("mAttendeeOut");
			String toId = fileForm.getParameter("toId");
			String privId = fileForm.getParameter("privId");
			String secretToId = fileForm.getParameter("secretToId");
			String equipmentIdStr = fileForm.getParameter("equipmentIdStr");
			
			for (int i = 0; i < temDate; i++) {
				String mStarTimeStr = YHUtility.null2Empty(fileForm.getParameter("M_START_TIME"));
				String mEndTimeStr = YHUtility.null2Empty(fileForm.getParameter("M_END_TIME"));
				Date afterDate = YHUtility.getDayAfter(mStartDate, i);
				String formatDateStr = new SimpleDateFormat("yyyy-MM-dd").format(afterDate);
				String startAfterDateStr = formatDateStr + " " + mStarTimeStr;
				String endAfterDateStr = formatDateStr + " " + mEndTimeStr;
				int curDay = this.getDateWeek(formatDateStr);

				String w1 = fileForm.getParameter("W1");
				String w2 = fileForm.getParameter("W2");
				String w3 = fileForm.getParameter("W3");
				String w4 = fileForm.getParameter("W4");
				String w5 = fileForm.getParameter("W5");
				String w6 = fileForm.getParameter("W6");
				String w7 = fileForm.getParameter("W7");

				if (curDay == 1 && YHUtility.isNullorEmpty(w1)) {
					continue;
				}
				if (curDay == 2 && YHUtility.isNullorEmpty(w2)) {
					continue;
				}
				if (curDay == 3 && YHUtility.isNullorEmpty(w3)) {
					continue;
				}
				if (curDay == 4 && YHUtility.isNullorEmpty(w4)) {
					continue;
				}
				if (curDay == 5 && YHUtility.isNullorEmpty(w5)) {
					continue;
				}
				if (curDay == 6 && YHUtility.isNullorEmpty(w6)) {
					continue;
				}
				if (curDay == 7 && YHUtility.isNullorEmpty(w7)) {
					continue;
				}

				meeting.setMName(mName);
				meeting.setMTopic(mTopic);
				meeting.setMDesc(mDesc);
				meeting.setMProposer(String.valueOf(person.getSeqId()));
				meeting.setMRequestTime(YHUtility.parseTimeStamp());
				meeting.setMAttendee(mAttendee);
				meeting.setMStart(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", startAfterDateStr));
				meeting.setMEnd(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", endAfterDateStr));
				meeting.setMRoom(mRoom);
				meeting.setMManager(mManager);
				meeting.setMAttendeeOut(mAttendeeOut);
				meeting.setSmsRemind(smsRemind);
				meeting.setSms2Remind(sms2Remind);
				meeting.setToId(toId);
				meeting.setPrivId(privId);
				meeting.setSecretToId(secretToId);
				meeting.setResendLong(resendLong);
				meeting.setResendSeveral(resendSeveral);
				meeting.setEquipmentIdStr(equipmentIdStr);
				meeting.setCalendar(calendar);
				meeting.setRecorder(recorder);
				meeting.setMStatus(mStatus);
				meeting.setCycle(cycle);
				meeting.setCycleNo(cycleNo);
				this.addMeetingInfo(dbConn, meeting);
			}

			int maxSeqId = this.getMaxMeetingId(dbConn);
			String mManagerStr = mManager;
			String userName = person.getUserName();
      
			String content = userName + " 向您提交周期性会议申请，请批示！";
			int fromId = person.getSeqId();
			if (!YHUtility.isNullorEmpty(mManagerStr) && "1".equals(smsReminde1)) {
				String remindUrl = "/subsys/oa/meeting/manage/index.jsp";
				this.doSmsBack2(dbConn, content, fromId, mManagerStr, "8", remindUrl, YHUtility.parseTimeStamp());
			}
			if (!YHUtility.isNullorEmpty(mManagerStr) && "1".equals(smsReminde2)) {
				YHMobileSms2Logic sbl = new YHMobileSms2Logic();
				sbl.remindByMobileSms(dbConn, mManagerStr, fromId, content, YHUtility.parseTimeStamp());
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 返回某个日期为星期几 --wyw
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getDateWeek(String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar d = new GregorianCalendar();
		Date mydate = dateFormat.parse(date);
		d.setTime(mydate);
		int today = d.get(Calendar.DAY_OF_WEEK);
		if (today == 1) {
			today = 7;
		} else {
			today = today - 1;
		}
		return today;
	}

	/**
	 * 获取最大的周期性会议编号加
	 * 
	 * @param dbConn
	 * @return
	 * @throws Exception
	 */
	public int getMaxCycleNo(Connection dbConn) throws Exception {
		int reSult = 0;
		String sql = "select max(CYCLE_NO) as COUNT from oa_conference";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				reSult = rs.getInt("COUNT");
			}
		} catch (Exception e) {
			throw e;
		}
		return reSult;
	}

	/**
	 * 获取出席人员名称 --wyw
	 * 
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public String getAttendeeNameLogic(Connection dbConn, int seqId) throws Exception {
		YHORM orm = new YHORM();
		try {
			YHMeeting meeting = (YHMeeting) orm.loadObjSingle(dbConn, YHMeeting.class, seqId);
			String mAttendee = "";
			String mAttendeeOut = "";
			String mAttendeeNames = "";
			if (meeting != null) {
				mAttendee = YHUtility.null2Empty(meeting.getMAttendee());
				mAttendeeOut = YHUtility.null2Empty(meeting.getMAttendeeOut());
			}
			mAttendeeNames = this.getUserNameLogic2(dbConn, mAttendee);
			String data = "\"内部:" + YHUtility.encodeSpecial(mAttendeeNames) + "<br>外部:" + YHUtility.encodeSpecial(mAttendeeOut) + "\" ";
			return data;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 取得出席人员名称--wyw
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getUserNameLogic2(Connection dbConn, String seqIdStr) throws Exception {
		String result = "";
		if (YHUtility.isNullorEmpty(seqIdStr)) {
			return result;
		}
		if (seqIdStr.endsWith(",")) {
			seqIdStr = seqIdStr.substring(0, seqIdStr.length() - 1);
		}
		String sql = " select USER_NAME from PERSON where SEQ_ID in(" + seqIdStr + ")";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String toId = rs.getString(1);
				if (toId != null) {
					result += toId + ",";
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		if (result.endsWith(",")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * 更新会议纪要信息--wyw
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @param map
	 * @throws Exception
	 */
	public String updateMeetingSummaryLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person, Map<Object, Object> map) throws Exception {
		YHORM orm = new YHORM();
		String seqIdStr = fileForm.getParameter("seqId");
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		try {
			boolean fromFolderFlag = (Boolean) map.get("fromFolderFlag");
			String newAttchId = (String) map.get("newAttchId");
			String newAttchName = (String) map.get("newAttchName");

			boolean uploadFlag = (Boolean) map.get("uploadFlag");
			String attachmentId = (String) map.get("attachmentId");
			String attachmentName = (String) map.get("attachmentName");
			String summary = fileForm.getParameter("summary");
			String readPeopleId = fileForm.getParameter("readPeopleId");

			YHMeeting meeting = (YHMeeting) orm.loadObjSingle(dbConn, YHMeeting.class, seqId);

			if (meeting == null) {
				return null;
			}

			String dbAttachmentId1 = "";
			String dbAttachmentName = "";
			dbAttachmentId1 = YHUtility.null2Empty(meeting.getAttachmentId1());
			dbAttachmentName = YHUtility.null2Empty(meeting.getAttachmentName1());
			if (fromFolderFlag && uploadFlag) {
				meeting.setAttachmentId1(dbAttachmentId1.trim() + newAttchId.trim() + attachmentId.trim());
				meeting.setAttachmentName1(dbAttachmentName.trim() + newAttchName.trim() + attachmentName.trim());
			} else if (fromFolderFlag) {
				meeting.setAttachmentId1(dbAttachmentId1.trim() + newAttchId.trim());
				meeting.setAttachmentName1(dbAttachmentName.trim() + newAttchName.trim());
			} else if (uploadFlag) {
				meeting.setAttachmentId1(dbAttachmentId1.trim() + attachmentId.trim());
				meeting.setAttachmentName1(dbAttachmentName.trim() + attachmentName.trim());
			}
			meeting.setSummary(summary);
			meeting.setReadPeopleId(readPeopleId);
			orm.updateSingle(dbConn, meeting);
			;
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	/**
	 * 会议纪要查询--wyw
	 * 
	 * @param dbConn
	 * @param request
	 * @param person
	 * @param mName
	 * @param mProposer
	 * @param beginDate
	 * @param endDate
	 * @param mRoom
	 * @param mStatus
	 * @return
	 * @throws Exception
	 */
	public String queryMeetingSummaryLogic(Connection dbConn, Map request, YHPerson person, Map map) throws Exception {

		String mName = (String) map.get("mName");
		String mProposer = (String) map.get("mProposer");
		String beginDate = (String) map.get("beginDate");
		String endDate = (String) map.get("endDate");
		String mRoom = (String) map.get("mRoom");
		String keyWord1 = (String) map.get("keyWord1");
		String keyWord2 = (String) map.get("keyWord2");
		String keyWord3 = (String) map.get("keyWord3");
		String conditionStr = "";
		try {
			if (!YHUtility.isNullorEmpty(mName)) {
				conditionStr = " and M_NAME like '%" + YHDBUtility.escapeLike(mName) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(mProposer)) {
				conditionStr += " and M_PROPOSER ='" + YHDBUtility.escapeLike(mProposer) + "'";
			}
			if (!YHUtility.isNullorEmpty(beginDate)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("M_START", beginDate, ">=");
			}
			if (!YHUtility.isNullorEmpty(endDate)) {
				conditionStr += " and " + YHDBUtility.getDateFilter("M_START", endDate, "<=");
			}
			if (!YHUtility.isNullorEmpty(mRoom)) {
				conditionStr += " and M_ROOM ='" + YHDBUtility.escapeLike(mRoom) + "'";
			}
			if (!YHUtility.isNullorEmpty(keyWord1)) {
				conditionStr += " and SUMMARY like '%" + YHDBUtility.escapeLike(keyWord1) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(keyWord2)) {
				conditionStr += " and SUMMARY like '%" + YHDBUtility.escapeLike(keyWord2) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(keyWord3)) {
				conditionStr += " and SUMMARY like '%" + YHDBUtility.escapeLike(keyWord3) + "%'" + YHDBUtility.escapeLike();
			}
			String sql = "SELECT SEQ_ID,M_NAME,M_PROPOSER,M_START,M_END,SUMMARY,READ_PEOPLE_ID,M_ATTENDEE,TO_ID,PRIV_ID , SECRET_TO_ID from oa_conference where 1=1 " + conditionStr;
			String privSeqIdStr = this.getSeqIdStr(dbConn, sql, person);
			String query = "SELECT SEQ_ID,M_NAME,M_PROPOSER,M_START,M_END,SEQ_ID from oa_conference where SEQ_ID in(" + privSeqIdStr
					+ " )  order by M_START desc";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, query);
			return pageDataList.toJson();

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取满足条件的seqId串
	 * 
	 * @param dbConn
	 * @param sqlStr
	 * @return
	 * @throws Exception
	 */
	public String getSeqIdStr(Connection dbConn, String sqlStr, YHPerson person) throws Exception {
		String seqIdStr = "-1,";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConn.prepareStatement(sqlStr);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String readPeopleId = YHUtility.null2Empty(rs.getString("READ_PEOPLE_ID"));
				String mProposer = YHUtility.null2Empty(rs.getString("M_PROPOSER"));
				String mAttendee = YHUtility.null2Empty(rs.getString("M_ATTENDEE"));
				String summary = YHUtility.null2Empty(rs.getString("SUMMARY"));
				String toId = YHUtility.null2Empty(rs.getString("TO_ID"));
				String privId = YHUtility.null2Empty(rs.getString("PRIV_ID"));
				String secretToId = YHUtility.null2Empty(rs.getString("SECRET_TO_ID"));
			      
				int meetingId = rs.getInt("SEQ_ID");
				if (!YHUtility.isNullorEmpty(readPeopleId)) {
					readPeopleId += ",";
				}
				if (!YHUtility.isNullorEmpty(mProposer)) {
					mProposer += ",";
				}
				if (!YHUtility.isNullorEmpty(mAttendee)) {
					mAttendee += ",";
				}
				String idStrs = readPeopleId + mProposer + mAttendee;
				if (idStrs.endsWith(",")) {
					idStrs = idStrs.substring(0, idStrs.length() - 1);
				}
				boolean havePrivFlag = this.isLonginUser(idStrs, String.valueOf(person.getSeqId()));
				boolean havePrivFlag2 = !"ALL_DEPT".equals(toId) 
        && !"0".equals(toId) 
        && !YHWorkFlowUtility.findId(toId, String.valueOf(person.getDeptId()))
        && !YHWorkFlowUtility.findId(privId, person.getUserPriv())
        && !YHWorkFlowUtility.findId(secretToId, String.valueOf(person.getSeqId()));
				if (havePrivFlag2 && !havePrivFlag && !person.isAdminRole()) {
					continue;
				}
        
				if (!YHUtility.isNullorEmpty(summary)) {
					seqIdStr += String.valueOf(meetingId) + ",";
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		if (seqIdStr.endsWith(",")) {
			seqIdStr = seqIdStr.substring(0, seqIdStr.length() - 1);
		}
		return seqIdStr;
	}

	/**
	 * 取得在线调度人名称--wyw
	 * 
	 * @param dbConn
	 * @return
	 * @throws Exception
	 */
	public String getOnlineUsersLogic(Connection dbConn) throws Exception {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String data = "";
		try {
			String paraValue = this.getParaValue(dbConn);
			if (YHUtility.isNullorEmpty(paraValue)) {
				paraValue = "-1";
			}
			String sql = "SELECT distinct p.SEQ_ID,  p.USER_NAME from PERSON p,oa_online where p.SEQ_ID=oa_online.USER_ID and P.SEQ_ID in(" + paraValue
					+ ") order by USER_NAME";
			stmt = dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String userName = YHUtility.null2Empty(rs.getString("USER_NAME"));
				if (!YHUtility.isNullorEmpty(userName)) {
					data += userName + ",";
				}
			}
			if (data.endsWith(",")) {
				data = data.substring(0, data.length() - 1);
			}
			String returnStr = "{userName:\"" + YHUtility.encodeSpecial(data) + "\"}";
			return returnStr;

		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
	}

	/**
	 * 更新会议申请信息
	 * 
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @param map
	 * @throws Exception
	 */
	public void updateMeetingValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person, Map<Object, Object> map, int meetingId)
			throws Exception {
		YHORM orm = new YHORM();
		try {
			boolean fromFolderFlag = (Boolean) map.get("fromFolderFlag");
			String newAttchId = (String) map.get("newAttchId");
			String newAttchName = (String) map.get("newAttchName");
			boolean uploadFlag = (Boolean) map.get("uploadFlag");
			String attachmentId = (String) map.get("attachmentId");
			String attachmentName = (String) map.get("attachmentName");
			boolean pageAttIdFlag = (Boolean) map.get("pageAttIdFlag");
			String pageAttachmentId = (String) map.get("pageAttachmentId");
			String pageattachmentName = (String) map.get("pageattachmentName");
			YHMeeting meeting = (YHMeeting) orm.loadObjSingle(dbConn, YHMeeting.class, meetingId);
			if (fromFolderFlag && uploadFlag && pageAttIdFlag) {
				meeting.setAttachmentId(newAttchId.trim() + attachmentId.trim() + pageAttachmentId.trim());
				meeting.setAttachmentName(newAttchName.trim() + attachmentName.trim() + pageattachmentName.trim());
			} else if (fromFolderFlag && uploadFlag) {
				meeting.setAttachmentId(newAttchId.trim() + attachmentId.trim());
				meeting.setAttachmentName(newAttchName.trim() + attachmentName.trim());
			} else if (fromFolderFlag && pageAttIdFlag) {
				meeting.setAttachmentId(newAttchId.trim() + pageAttachmentId.trim());
				meeting.setAttachmentName(newAttchName.trim() + pageattachmentName.trim());
			} else if (uploadFlag && pageAttIdFlag) {
				meeting.setAttachmentId(attachmentId.trim() + pageAttachmentId.trim());
				meeting.setAttachmentName(attachmentName.trim() + pageattachmentName.trim());
			} else if (fromFolderFlag) {
				meeting.setAttachmentId(newAttchId.trim());
				meeting.setAttachmentName(newAttchName.trim());
			} else if (uploadFlag) {
				meeting.setAttachmentId(attachmentId.trim());
				meeting.setAttachmentName(attachmentName.trim());
			} else if (pageAttIdFlag) {
				meeting.setAttachmentId(pageAttachmentId.trim());
				meeting.setAttachmentName(pageattachmentName.trim());
			}

			String privId = fileForm.getParameter("privId");
			String toId = fileForm.getParameter("toId");
			String mName = fileForm.getParameter("mName");
			String mTopic = fileForm.getParameter("mTopic");
			String mDesc = fileForm.getParameter("mDesc");
			String secretToId = fileForm.getParameter("secretToId");
			String mAttendee = fileForm.getParameter("mAttendee");
			String mStart = fileForm.getParameter("mStart");
			String mEnd = fileForm.getParameter("mEnd");
			String mRoomStr = fileForm.getParameter("mRoom");
			String mManager = fileForm.getParameter("mManager");
			String mAttendeeOut = fileForm.getParameter("mAttendeeOut");
			String smsRemindStr = fileForm.getParameter("smsRemind");
			String sms2RemindStr = fileForm.getParameter("sms2Remind");
			String resendSeveralStr = fileForm.getParameter("resendSeveral");
			String resendLongStr = fileForm.getParameter("resendLong");
			String equipmentIdStr = fileForm.getParameter("checkEquipmentes");
			String calendarStr = fileForm.getParameter("calendar");
			String recorderStr = fileForm.getParameter("recorder");
			String mStatus = fileForm.getParameter("mStatus");
			String cycle = fileForm.getParameter("cycle");
			String smsReminde1 = fileForm.getParameter("smsReminde1");
			String smsReminde2 = fileForm.getParameter("smsReminde2");

			String attachmentIdStr = fileForm.getParameter("attachmentId");
			String attachmentNameStr = fileForm.getParameter("attachmentName");
			if (YHUtility.isNullorEmpty(smsReminde1)) {
				smsReminde1 = "0";
			}
			if (YHUtility.isNullorEmpty(smsReminde2)) {
				smsReminde2 = "0";
			}

			meeting.setPrivId(privId);
			meeting.setSecretToId(secretToId);
			meeting.setToId(toId);
			meeting.setMName(mName);
			meeting.setMTopic(mTopic);
			meeting.setMDesc(mDesc);
			meeting.setMRequestTime(YHUtility.parseTimeStamp());
			meeting.setMAttendee(mAttendee);
			meeting.setMStart(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", mStart));
			meeting.setMEnd(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", mEnd));
			int mRoom = 0;
			if (!YHUtility.isNullorEmpty(mRoomStr)) {
				mRoom = Integer.parseInt(mRoomStr);
			}
			meeting.setMRoom(mRoom);
			meeting.setMManager(mManager);
			meeting.setMAttendeeOut(mAttendeeOut);
			if (YHUtility.isNullorEmpty(smsRemindStr)) {
				smsRemindStr = "0";
			}
			meeting.setSmsRemind(smsRemindStr);

			if (YHUtility.isNullorEmpty(sms2RemindStr)) {
				sms2RemindStr = "0";
			}
			meeting.setSms2Remind(sms2RemindStr);
			int resendSeveral = 0;
			if (!YHUtility.isNullorEmpty(resendSeveralStr)) {
				resendSeveral = Integer.parseInt(resendSeveralStr);
			}
			meeting.setResendSeveral(resendSeveral);
			int resendLong = 0;
			if (!YHUtility.isNullorEmpty(resendLongStr)) {
				resendLong = Integer.parseInt(resendLongStr);
			}
			meeting.setResendLong(resendLong);
			if (!YHUtility.isNullorEmpty(equipmentIdStr)) {
			}
			meeting.setEquipmentIdStr(equipmentIdStr);
			if (YHUtility.isNullorEmpty(calendarStr)) {
				calendarStr = "0";
			}
			meeting.setCalendar(calendarStr);
			if (YHUtility.isNullorEmpty(recorderStr)) {
				recorderStr = "";
			}
			meeting.setRecorder(recorderStr);
			if (YHUtility.isNullorEmpty(mStatus)) {
				mStatus = "0";
			}
			meeting.setMStatus(mStatus);
			if (YHUtility.isNullorEmpty(cycle)) {
				cycle = "0";
			}
			meeting.setCycle(cycle);
			orm.updateSingle(dbConn, meeting);

			// int maxSeqId = this.getMaxMeetingId(dbConn);
			String mManagerStr = mManager;
			String userName = this.getUserNameLogic(dbConn, person.getSeqId());
			String content = userName + " 向您提交会议申请，请批示！";
			int fromId = person.getSeqId();
			if (!YHUtility.isNullorEmpty(mManagerStr) && "1".equals(smsReminde1)) {
				String remindUrl = "/subsys/oa/meeting/query/meetingdetail.jsp?seqId=" + meeting.getSeqId() + "&openFlag=1&openWidth=860&openHeight=650";
				this.doSmsBack2(dbConn, content, fromId, mManagerStr, "8", remindUrl, YHUtility.parseTimeStamp());
			}
			if (!YHUtility.isNullorEmpty(mManagerStr) && "1".equals(smsReminde2)) {
				YHMobileSms2Logic sbl = new YHMobileSms2Logic();
				sbl.remindByMobileSms(dbConn, mManagerStr, fromId, content, YHUtility.parseTimeStamp());
			}

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 处理上传附件，返回附件id，附件名称--wyw
	 * 
	 * @param fileForm
	 * @return
	 * @throws Exception
	 */
	public Map<Object, Object> fileUploadLogic(YHFileUploadForm fileForm, String attachmentFolder) throws Exception {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			Iterator<String> iKeys = fileForm.iterateFileFields();
			boolean uploadFlag = false;
			String attachmentId = "";
			String attachmentName = "";
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(date);
			String separator = File.separator;
			String filePath = YHSysProps.getAttachPath() + separator + attachmentFolder + separator + currDate;

			while (iKeys.hasNext()) {
				String fieldName = iKeys.next();
				String fileName = fileForm.getFileName(fieldName);
				if (YHUtility.isNullorEmpty(fileName)) {
					continue;
				}
				YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
				String rand = emul.getRandom();
				attachmentId += currDate + "_" + rand + ",";
				attachmentName += fileName + "*";
				uploadFlag = true;

				fileName = rand + "_" + fileName;
				fileForm.saveFile(fieldName, filePath +File.separator + fileName);
			}
			result.put("attachmentId", attachmentId);
			result.put("attachmentName", attachmentName);
			result.put("uploadFlag", uploadFlag);
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 更新浮动菜单附件
	 * 
	 * @param dbConn
	 * @param seqId
	 * @param attachId
	 * @param attachName
	 * @return
	 * @throws Exception
	 */
	public boolean updateFloadFile(Connection dbConn, int seqId, String attachId, String attachName, String delOpt) throws Exception {
		boolean flag = false;
		YHORM orm = new YHORM();
		try {
			YHMeeting meeting = (YHMeeting) orm.loadObjSingle(dbConn, YHMeeting.class, seqId);
			if (meeting != null) {
				if ("summary".equals(delOpt)) {
					meeting.setAttachmentId1(YHUtility.null2Empty(attachId));
					meeting.setAttachmentName1(YHUtility.null2Empty(attachName));
				} else {
					meeting.setAttachmentId(YHUtility.null2Empty(attachId));
					meeting.setAttachmentName(YHUtility.null2Empty(attachName));
				}
				orm.updateSingle(dbConn, meeting);
				flag = true;
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

}
