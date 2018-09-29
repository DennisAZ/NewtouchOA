package yh.subsys.oa.meeting.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.subsys.oa.meeting.data.YHMeetingComment;

public class YHMeetingCommentLogic {
	private static Logger log = Logger.getLogger(YHMeetingCommentLogic.class);

	/**
	 * 新建会议纪要设置值--wyw
	 * @param dbConn
	 * @param fileForm
	 * @param person
	 * @param map
	 * @throws Exception
	 */
	public void setCommentValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person, Map<Object, Object> map) throws Exception {
		YHORM orm = new YHORM();
		try {
			boolean fromFolderFlag = (Boolean) map.get("fromFolderFlag");
			String newAttchId = (String) map.get("newAttchId");
			String newAttchName = (String) map.get("newAttchName");

			boolean uploadFlag = (Boolean) map.get("uploadFlag");
			String attachmentId = (String) map.get("attachmentId");
			String attachmentName = (String) map.get("attachmentName");

			YHMeetingComment comment = new YHMeetingComment();
			if (fromFolderFlag && uploadFlag) {
				comment.setAttachmentId(newAttchId.trim() + attachmentId.trim());
				comment.setAttachmentName(newAttchName.trim() + attachmentName.trim());
			} else if (fromFolderFlag) {
				comment.setAttachmentId(newAttchId.trim());
				comment.setAttachmentName(newAttchName.trim());
			} else if (uploadFlag) {
				comment.setAttachmentId(attachmentId.trim());
				comment.setAttachmentName(attachmentName.trim());
			}
			String meetingIdStr = fileForm.getParameter("meetingId");
			String content = fileForm.getParameter("content");

			int meetingId = 0;
			if (!YHUtility.isNullorEmpty(meetingIdStr)) {
				meetingId = Integer.parseInt(meetingIdStr);
			}
			comment.setMeetingId(meetingId);
			comment.setContent(content);
			comment.setReTime(YHUtility.parseTimeStamp());
			comment.setUserId(String.valueOf(person.getSeqId()));
			orm.saveSingle(dbConn, comment);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 获取评论信息--wyw
	 * @param conn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public String getCommentInfoLogic(Connection dbConn, YHPerson person,int meetingId) throws Exception {
		YHPersonLogic personLogic = new YHPersonLogic();
		YHMeetingLogic meetingLogic = new YHMeetingLogic();
		StringBuffer buffer = new StringBuffer("["); 
		String query = "select SEQ_ID,CONTENT,RE_TIME,USER_ID,ATTACHMENT_ID,ATTACHMENT_NAME from oa_conference_comment where MEETING_ID =" + meetingId + " order by RE_TIME desc";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = dbConn.createStatement();
			rs = stmt.executeQuery(query);
			int count = 0;
			while(rs.next() && count++ < 5){
				int commentId = rs.getInt("SEQ_ID");
				String dbContent = YHUtility.null2Empty(rs.getString("CONTENT"));
				Date dbReTime = rs.getTimestamp("RE_TIME");
				String dbUserIdStr = YHUtility.null2Empty(rs.getString("USER_ID"));
				String dbAttachmentId = YHUtility.null2Empty(rs.getString("ATTACHMENT_ID"));
				String dbAttachmentName = YHUtility.null2Empty(rs.getString("ATTACHMENT_NAME"));
				
				int dbUserId=-1;
				if (!YHUtility.isNullorEmpty(dbUserIdStr)) {
					dbUserId=Integer.parseInt(dbUserIdStr);
				}
				YHPerson objPerson=getPersonObj(dbConn, dbUserId);
				String personName = objPerson.getUserName();
				String deptName=personLogic.getDeptName(dbConn, person.getDeptId());
				boolean isLoginUser = meetingLogic.isLonginUser(dbUserIdStr, String.valueOf(person.getSeqId()));
				int delPrivFlag=0;
				if (isLoginUser || person.isAdminRole()) {
					delPrivFlag = 1;
				}
				buffer.append("{");
				buffer.append("commentId:\"" + commentId + "\"");
				buffer.append(",content:\"" + YHUtility.encodeSpecial(dbContent) + "\"");
				buffer.append(",content:\"" + YHUtility.encodeSpecial(dbContent) + "\"");
				buffer.append(",reTime:\"" + YHUtility.getDateTimeStr(dbReTime) + "\"");
				buffer.append(",attachmentId:\"" + YHUtility.encodeSpecial(dbAttachmentId) + "\"");
				buffer.append(",attachmentName:\"" + YHUtility.encodeSpecial(dbAttachmentName) + "\"");
				buffer.append(",personName:\"" + YHUtility.encodeSpecial(personName) + "\"");
				buffer.append(",deptName:\"" + YHUtility.encodeSpecial(deptName) + "\"");
				buffer.append(",delPrivFlag:\"" + delPrivFlag + "\"");
				buffer.append("},");
			}
			if(buffer.length() > 1){
				buffer.deleteCharAt(buffer.length() - 1);
      }
			buffer.append("]");
		} catch (Exception ex) {
			throw ex;
		}finally {
			YHDBUtility.close(stmt, rs, log);
		} 
		return buffer.toString();
	}
	
	/**
	 * 获取人员对象
	 * @param dbConn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public YHPerson getPersonObj(Connection dbConn,int seqId) throws Exception{
		YHORM orm=new YHORM();
		try {
			return (YHPerson) orm.loadObjSingle(dbConn, YHPerson.class, seqId);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 删除会议记录（含附件）--wyw
	 * @param dbConn
	 * @param seqId
	 * @throws Exception 
	 */
	public void delCommentLogic(Connection dbConn, int seqId,String filePath) throws Exception{
		YHMeetingLogic meetingLogic = new YHMeetingLogic();
		YHORM orm = new YHORM();
		try {
			StringBuffer attIdBuffer = new StringBuffer();
			StringBuffer attNameBuffer = new StringBuffer();
			YHMeetingComment comment=(YHMeetingComment) orm.loadObjSingle(dbConn, YHMeetingComment.class, seqId);
			String dbAttachmentId = "";
			String dbAttachmentName= "";
			if (comment != null) {
				dbAttachmentId = YHUtility.null2Empty(comment.getAttachmentId());
				dbAttachmentName = YHUtility.null2Empty(comment.getAttachmentName());
				attIdBuffer.append(dbAttachmentId.trim());
				attNameBuffer.append(dbAttachmentName.trim());
			}
			String[] attIdArray = {};
			String[] attNameArray = {};
			if (!YHUtility.isNullorEmpty(attIdBuffer.toString()) && !YHUtility.isNullorEmpty(attNameBuffer.toString()) && attIdBuffer.length() > 0) {
				attIdArray = attIdBuffer.toString().trim().split(",");
				attNameArray = attNameBuffer.toString().trim().split("\\*");
			}
			if (attIdArray != null && attIdArray.length > 0) {
				for (int i = 0; i < attIdArray.length; i++) {
					Map<String, String> map = meetingLogic.getFileName(attIdArray[i], attNameArray[i]);
					if (map.size() != 0) {
						Set<String> set = map.keySet();
						// 遍历Set集合
						for (String keySet : set) {
							String key = keySet;
							String keyValue = map.get(keySet);
							String attaIdStr = meetingLogic.getAttaId(keySet);
							String fileNameValue = attaIdStr + "_" + keyValue;
							String fileFolder = meetingLogic.getFilePathFolder(key);
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
			// 删除数据库信息			orm.deleteSingle(dbConn, comment);
		} catch (Exception e) {
			throw e;
		}
	}
}
