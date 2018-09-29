package yh.subsys.oa.meeting.act;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.address.data.YHAddress;
import yh.core.funcs.calendar.data.YHCalendar;
import yh.core.funcs.calendar.logic.YHCalendarLogic;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.funcs.email.data.YHEmail;
import yh.core.funcs.email.data.YHEmailBody;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.meeting.data.YHMeeting;
import yh.subsys.oa.meeting.data.YHMeetingRoom;
import yh.subsys.oa.meeting.logic.YHMeetingLogic;

public class YHMeetingAct {
	
	/**
	 * 附件保存文件夹
	 */
	public static final String attachmentFolder = "meeting";
	private YHMeetingLogic logic = new YHMeetingLogic();

	/**
	 * 新建会议申请信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addMeetingInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);

		String contexPath = request.getContextPath();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			
			int mRoom = 0;
			if (!YHUtility.isNullorEmpty(fileForm.getParameter("mRoom"))) {
				mRoom = Integer.parseInt(fileForm.getParameter("mRoom"));
			}
			String mStart =  fileForm.getParameter("mStart");
			String mEnd =  fileForm.getParameter("mEnd");

			Date mStartDateTime = YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", mStart);
			Date mEndDateTime = YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", mEnd);

			boolean isHaveMeeting = this.logic.checkRoom(dbConn, 0, mRoom, mStartDateTime, mEndDateTime);
			String  meetingIdStr = this.logic.checkRoomData(dbConn, 0, mRoom, mStartDateTime, mEndDateTime);
			if (isHaveMeeting) {
				String message = mStart + "有会议冲突";
				response.sendRedirect(contexPath + "/subsys/oa/meeting/apply/existMeeting.jsp?message=" +URLEncoder.encode( message,"UTF-8") + "&meetingIdStr=" + meetingIdStr);
				return null;
			}
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "meeting");
			String attIdStr = sel.getAttachIdToString(",");
			String attNameStr = sel.getAttachNameToString("*");

			boolean fromFolderFlag = false;
			String newAttchId = "";
			String newAttchName = "";
			if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
				newAttchId = attIdStr + ",";
				newAttchName = attNameStr + "*";
				fromFolderFlag = true;
			}
			Iterator<String> iKeys = fileForm.iterateFileFields();
			boolean uploadFlag = false;
			String attachmentId = "";
			String attachmentName = "";
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(date);
			String separator = File.separator;
			String filePath = YHSysProps.getAttachPath() + separator + "meeting" + separator + currDate;

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
			
			String pageAttachmentId = YHUtility.null2Empty(fileForm.getParameter("attachmentId"));
			String pageattachmentName =YHUtility.null2Empty(fileForm.getParameter("attachmentName"));
			
			if (!YHUtility.isNullorEmpty(pageAttachmentId)) {
				if (pageAttachmentId.lastIndexOf(",")==-1) {
					pageAttachmentId = pageAttachmentId + ",";
				}
				if (pageattachmentName.lastIndexOf("*")==-1) {
					pageattachmentName = pageattachmentName + "*";
				}
			}
			boolean pageAttIdFlag =false;
			if (!YHUtility.isNullorEmpty(pageAttachmentId)) {
				pageAttIdFlag = true;
			}
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("fromFolderFlag", fromFolderFlag);
			map.put("newAttchId", newAttchId);
			map.put("newAttchName", newAttchName);
			
			map.put("uploadFlag", uploadFlag);
			map.put("attachmentId", attachmentId);
			map.put("attachmentName", attachmentName);
			
			map.put("pageAttIdFlag", pageAttIdFlag);
			map.put("pageAttachmentId", pageAttachmentId);
			map.put("pageattachmentName", pageattachmentName);
			
			String url = this.logic.setMeetingValueLogic(dbConn, fileForm, person, map);
			if (!YHUtility.isNullorEmpty(url)) {
        String path = request.getContextPath();
        response.sendRedirect(path+ url);
        return null;
      }
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		// request.getRequestDispatcher("").forward(request, response);
		response.sendRedirect(contexPath + "/subsys/oa/meeting/apply/newMeetingWarn.jsp");
		return null;
	}

	/**
	 *会议管理 通用列表 -cc
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMeetingListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String mStatus = request.getParameter("mStatus");
			String data = this.logic.getMeetingListJson(dbConn, request.getParameterMap(), mStatus, person);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return null;
	}

	/**
	 *会议申请 通用列表 -wyw
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getAppMeetingListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String mStatus = request.getParameter("mStatus");
			String data = this.logic.getMeetingListJson2(dbConn, request.getParameterMap(), mStatus, person);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return null;
	}

	/**
	 *待批周期性列表 -cc
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMeetingCycleListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String mStatus = request.getParameter("mStatus");
			String data = this.logic.getMeetingCycleListJson(dbConn, request.getParameterMap(), person);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return null;
	}

	/**
	 *待批周期性列表详情 -cc
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMeetingManageCycleList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;

		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String cycleNo = request.getParameter("cycleNo");
			String flag = request.getParameter("flag");
			String data = this.logic.getMeetingManageCycleList(dbConn, request.getParameterMap(), request, cycleNo, person, flag);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return null;
	}

	/**
	 * 删除单个会议记录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String deleteSingle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqId = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.deleteSingle(dbConn, Integer.parseInt(seqId));
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "删除成功");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 会议审批通用方法 -cc
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String seqId2 = request.getParameter("seqId");
			int seqId = 0;
			if (!YHUtility.isNullorEmpty(seqId2)) {
				seqId = Integer.parseInt(seqId2);
			}
			String mStatus = request.getParameter("mStatus");
			String contexPath = request.getContextPath();
			
			YHMeeting meetingTemp = (YHMeeting) this.logic.getMeetingRoomDetail(dbConn, seqId);
	    boolean isHaveMeeting = this.logic.checkRoom(dbConn, 0, meetingTemp.getMRoom(), meetingTemp.getMStart(), meetingTemp.getMEnd());
      String  meetingIdStr = this.logic.checkRoomData(dbConn, 0, meetingTemp.getMRoom(), meetingTemp.getMStart(), meetingTemp.getMEnd());
      if (isHaveMeeting && "1".equals(mStatus)) {
        String message = "有会议冲突";
        String data = "{path:\"" + contexPath + "/subsys/oa/meeting/apply/existMeeting.jsp?message=" +URLEncoder.encode( message,"UTF-8") + "&meetingIdStr=" + meetingIdStr + "\"}";
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }
      else{
      
  			// 短消息  			// if (!mStatus.equals("0")) {
  			String content = "";
  			if ("0".equals(mStatus)) {
  				content = "您的会议申请已被撤销！";
  			} else if ("1".equals(mStatus)) {
  				content = "您的会议申请已被批准！";
  			} else if ("3".equals(mStatus)) {
  				content = "您的会议申请未被批准!";
  			}
  			this.logic.updateStatus(dbConn, seqId, mStatus);
  			YHORM orm = new YHORM();
  			YHMeeting meeting = (YHMeeting) orm.loadObjSingle(dbConn, YHMeeting.class, seqId);
  			String meetingRoom = this.logic.getMeetingRoomNameLogic(dbConn, meeting.getMRoom());
  
  			YHSmsBack sb = new YHSmsBack();
  			YHMobileSms2Logic sbl = new YHMobileSms2Logic();
  			String mStartStr = String.valueOf(meeting.getMStart());
  			String mStart = "";
  			if(!YHUtility.isNullorEmpty(mStartStr)){
  			  mStart = mStartStr.substring(0, 19);
  			}
  			if (("0".equals(mStatus) || "1".equals(mStatus) || "3".equals(mStatus)) && !meeting.getMProposer().equals("")
  					&& meeting.getSmsRemind().equals("1")) {
  				sb.setSmsType("8");
  				sb.setContent(content);
  				sb.setFromId(person.getSeqId());
  				sb.setToId(meeting.getMProposer());
  				sb.setRemindUrl("/subsys/oa/meeting/query/meetingdetail.jsp?seqId=" + seqId +"&openFlag=1&openWidth=860&openHeight=650");
  				YHSmsUtil.smsBack(dbConn, sb);
  			}
  			String remindUrl = "/subsys/oa/meeting/query/meetingdetail.jsp?seqId=" + seqId +"&openFlag=1&openWidth=860&openHeight=650";
  			if ("1".equals(mStatus) && meeting.getSmsRemind().equals("1") && !YHUtility.isNullorEmpty(meeting.getMAttendee())) {
  
  				String contentSms = "通知您于" + mStart + "在" + meetingRoom + "开会，会议名称：" + meeting.getMName();
  				this.logic.doSmsBack(dbConn, contentSms, Integer.parseInt(meeting.getMProposer()), meeting.getMAttendee(), "8", remindUrl);
  			}
  			if ("1".equals(mStatus) && meeting.getSms2Remind().equals("1") && !YHUtility.isNullorEmpty(meeting.getMAttendee())) {
  
  				String contentMobile = "通知您于" + mStart + "在" + meetingRoom + "开会，会议名称：" + meeting.getMName();
  				sbl.remindByMobileSms(dbConn, meeting.getMAttendee(), Integer.parseInt(meeting.getMProposer()), contentMobile, new java.util.Date());
  			}
  
  			if ("0".equals(mStatus) && !YHUtility.isNullorEmpty(meeting.getMAttendee())) {
  				String contentSms2 = "会议" + meeting.getMRoom() + "已经被取消！";
  				this.logic.doSmsBack(dbConn, contentSms2, Integer.parseInt(meeting.getMProposer()), meeting.getMAttendee(), "8", remindUrl);
  
  				String contentMobile2 = "会议" + meeting.getMRoom() + "已经被取消！";
  				sbl.remindByMobileSms(dbConn, meeting.getMAttendee(), Integer.parseInt(meeting.getMProposer()), contentMobile2, new java.util.Date());
  			}
  			int resendSeveral = 0;
  			if (meeting.getResendSeveral() > 4) {
  				resendSeveral = 4;
  			} else {
  				resendSeveral = meeting.getResendLong();
  			}
  			if (meeting.getResendLong() > 0 && resendSeveral > 0 && !meeting.getMStart().equals("null") && !YHUtility.isNullorEmpty(meeting.getCalendar())) {
  				for (int i = 0; i < resendSeveral; i++) {
  					long sendTime = meeting.getMStart().getTime() / 1000 - meeting.getResendLong() * 3600 - i * 20 * 60;
  					Date sendDate = new Date(sendTime * 1000);
  					String contentSms3 = mStart + "在" + meetingRoom + "开会，请按时参加。";
  					this.logic.doSmsBack2(dbConn, contentSms3, Integer.parseInt(meeting.getMProposer()), meeting.getMAttendee(), "8", remindUrl, sendDate);
  				}
  			}
  
  			if (!YHUtility.isNullorEmpty(meeting.getCalendar())) {
  				String cont = "会议: " + meeting.getMName();
  				YHCalendar calendar = new YHCalendar();
  				YHCalendarLogic calendarLogic = new YHCalendarLogic();
  				String mAttend = meeting.getMAttendee();
  				String[] mAttendStr = mAttend.split(",");
  				for(int x = 0; x < mAttendStr.length; x++){
  				  calendar.setEndTime(meeting.getMEnd());
  	        calendar.setCalTime(meeting.getMStart());
  	        calendar.setUserId(mAttendStr[x]);
  	        calendar.setCalType("1");
  	        calendar.setCalLevel("1");
  	        calendar.setContent(cont);
  	        calendar.setManagerId(String.valueOf(person.getSeqId()));
  	        calendar.setOverStatus("0");
  	        calendarLogic.addCalendar(dbConn, calendar);
  				}
  			}
  			if (!YHUtility.isNullorEmpty(meeting.getCalendar())) {
  				this.logic.doEmailBack(dbConn, content, Integer.parseInt(meeting.getMProposer()), meeting.getMAttendee(), "8", remindUrl);
  			}
  			
  			YHEmailBody eb = new YHEmailBody();
  			String subject = "会议：" + meeting.getMName();
  			String contentEb = "通知您于" + mStart + "在" + meetingRoom + "开会，会议名称：" + meeting.getMName();
  			eb.setFromId(Integer.parseInt(meeting.getMProposer()));
  			eb.setToId(meeting.getMAttendee());
  			eb.setSubject(subject);
  			eb.setContent(contentEb);
  			eb.setSendFlag("1");
  			eb.setSendTime(new Date());
  			eb.setCompressContent(YHDiaryUtil.cutHtml(eb.getContent()));
  			orm.saveSingle(dbConn, eb);
  			// }
  			
  			YHEmail email = new YHEmail();
  			YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
  			String mAttends = meeting.getMAttendee();
        String[] mAttendStrs = mAttends.split(",");
        for(int y = 0; y < mAttendStrs.length; y++){
          email.setToId(mAttendStrs[y]);
          email.setReadFlag("0");
          email.setDeleteFlag("0");
          email.setBoxId(0);
          email.setBodyId(emul.getBodyId(dbConn));
          email.setReceipt("0");
          orm.saveSingle(dbConn, email);
        }
      }
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "修改成功！");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 批量批准待批会议
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String checkUpAllCycle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String idStrs = request.getParameter("idStrs");
			YHMobileSms2Logic sbl = new YHMobileSms2Logic();
			ArrayList<YHMeeting> meetingList = this.logic.getMeetingInfo(dbConn, idStrs);
			String calmName = "";
			String calmStatus = "";
			String calSmsRemind = "";
			String calSms2Remind = "";
			String calmAttendee = "";
			String calmProposter = "";
			int count = 0;
			int resendSeveral = 0;
			String content = "";
			for (int i = 0; i < meetingList.size(); i++) {
				YHORM orm = new YHORM();
				int seqId = meetingList.get(i).getSeqId();
				String remindUrl = "/subsys/oa/meeting/query/meetingdetail.jsp?seqId=" + seqId +"&openFlag=1&openWidth=860&openHeight=650";
				String mStatus = "1";
				YHMeeting meetingBool = (YHMeeting) orm.loadObjSingle(dbConn, YHMeeting.class, seqId);
				boolean bool = this.logic.checkRoom(dbConn, seqId, meetingBool.getMRoom(), meetingBool.getMStart(), meetingBool.getMEnd());
				YHMeeting meeting = (YHMeeting) orm.loadObjSingle(dbConn, YHMeeting.class, seqId);
				String mStartStr = String.valueOf(meeting.getMStart());
	      String mStart = "";
	      if(!YHUtility.isNullorEmpty(mStartStr)){
	        mStart = mStartStr.substring(0, 19);
	      }
				if (!bool) {
					count++;
					calmName = meeting.getMName();
					calmStatus = meeting.getMStatus();
					calSmsRemind = meeting.getSmsRemind();
					calSms2Remind = meeting.getSms2Remind();
					calmAttendee = meeting.getMAttendee();
					calmProposter = meeting.getMProposer();
					this.logic.updateStatus(dbConn, seqId, mStatus);
					String meetingRoom = this.logic.getMeetingRoomNameLogic(dbConn, meeting.getMRoom());
					if (meeting.getResendSeveral() > 4) {
						resendSeveral = 4;
					} else {
						resendSeveral = meeting.getResendSeveral();
					}
					if (YHUtility.isNullorEmpty(meeting.getMProposer())) {
						this.logic.doSmsBack(dbConn, content, person.getSeqId(), meeting.getMProposer(), "8", remindUrl);
					}
					if (meeting.getResendLong() > 0 && resendSeveral > 0 && !meeting.getMStart().equals("null")
							&& !YHUtility.isNullorEmpty(meeting.getCalendar())) {
						for (int x = 0; x < resendSeveral; x++) {
							long sendTime = meeting.getMStart().getTime() / 1000 - meeting.getResendLong() * 3600 - x * 20 * 60;
							Date sendDate = new Date(sendTime * 1000);
							String contentSms3 = mStart + "在" + meetingRoom + "开会，请按时参加。";
							this.logic.doSmsBack2(dbConn, contentSms3, Integer.parseInt(meeting.getMProposer()), meeting.getMAttendee(), "8", remindUrl, sendDate);
						}
					}
					if (!YHUtility.isNullorEmpty(meeting.getCalendar())) {
						String cont = "会议: " + meeting.getMName();
						YHCalendar calendar = new YHCalendar();
						YHCalendarLogic calendarLogic = new YHCalendarLogic();
						calendar.setEndTime(meeting.getMEnd());
						calendar.setCalTime(meeting.getMStart());
						calendar.setUserId(meeting.getMAttendee());
						calendar.setCalType("1");
						calendar.setCalLevel("1");
						calendar.setContent(cont);
						calendar.setManagerId(String.valueOf(person.getSeqId()));
						calendar.setOverStatus("0");
						calendarLogic.addCalendar(dbConn, calendar);
					}
				} else {
					String contStr = "您" + meeting.getMStart() + "的会议申请时间冲突，进入待审状态！";
					if (YHUtility.isNullorEmpty(meeting.getMProposer())) {
						this.logic.doSmsBack(dbConn, content, person.getSeqId(), meeting.getMProposer(), "8", remindUrl);
						continue;
					}
				}
			}
			// 因为周期性会议内容都是一样的，所以取最后一条记录发内部短信和手机短信
			if (count > 0) {
				String remindUrls = "1:calendar/arrange/";
				String contents = "参加周期性会议，会议名称：" + calmName + "，详情查看个人日程安排";
				if ("1".equals(calmStatus) && "1".equals(calSmsRemind) && !YHUtility.isNullorEmpty(calmAttendee)) {
					this.logic.doSmsBack(dbConn, content, Integer.parseInt(calmProposter), calmAttendee, "8", remindUrls);

				}
				if ("1".equals(calmStatus) && "1".equals(calSmsRemind) && !YHUtility.isNullorEmpty(calmAttendee)) {
					sbl.remindByMobileSms(dbConn, calmAttendee, Integer.parseInt(calmProposter), content, new java.util.Date());
				}
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "删除成功");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 自动开始和结束--cc
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getAutoBeginEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			this.logic.getAutoBegin(dbConn);
			this.logic.getAutoEnd(dbConn);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
			// request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 预约会议室是否冲突--c
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String checkRoom(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			int seqId = Integer.parseInt(request.getParameter("seqId"));
			YHORM orm = new YHORM();
			YHMeeting meeting = (YHMeeting) orm.loadObjSingle(dbConn, YHMeeting.class, seqId);

			boolean bool = this.logic.checkRoom(dbConn, seqId, meeting.getMRoom(), meeting.getMStart(), meeting.getMEnd());
			String daStr = this.logic.checkRoomData(dbConn, seqId, meeting.getMRoom(), meeting.getMStart(), meeting.getMEnd());
			String data = "";
			if (!"".equals(daStr)) {
				data = daStr;
			} else {
				data = "0";
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
			request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 预约冲突列表--cc
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMeetingConflict(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String seqId = request.getParameter("seqId");
			//String data = this.logic.getMeetingConflict(dbConn, request.getParameterMap(), seqId);
			List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"SEQ_ID IN(" + seqId + ")"};
      List funcList = new ArrayList();
      funcList.add("meeting");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_CONFERENCE"));
      
      for(Map ms : list){
        sb.append("{");
        sb.append("seqId:\"" + ms.get("seqId") + "\"");
        sb.append(",mName:\"" + (ms.get("mName") == null ? "" : ms.get("mName")) + "\"");
        sb.append(",mStart:\"" + (ms.get("mStart") == null ? "" : ms.get("mStart")) + "\"");
        sb.append(",mEnd:\"" + (ms.get("mEnd") == null ? "" : ms.get("mEnd")) + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1); 
      if (list.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 会议预约情况图表--cc
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMeetingInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			ArrayList<YHMeetingRoom> meetingRoomList = this.logic.selectMeetingRoom(dbConn, person); // 得到所有会议室

			// 得到未来7天			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd");
			SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM-dd HH:mm");
			SimpleDateFormat dateFormat4 = new SimpleDateFormat("HH:mm");
			Date curDate = new Date();

			List daysList = new ArrayList();
			String days = "";
			String[] weekDays = { "周天", "周一", "周二", "周三", "周四", "周五", "周六" };
			Calendar calendar = new GregorianCalendar();
			String severDayTr = "<tr class='TableHeader'><td width='10%' align='center' nowrap>未来7天</td>";
			for (int i = 0; i < 7; i++) {
				calendar.setTime(new Date());
				calendar.add(Calendar.DATE, +i);
				Date dateTemp = calendar.getTime();
				int week = calendar.get(Calendar.DAY_OF_WEEK);
				String dateTempStr = dateFormat2.format(dateTemp);
				daysList.add(dateFormat.format(dateTemp));
				severDayTr = severDayTr + "<td  width='10%' align='center' nowrap>" + dateTempStr + "(" + weekDays[week - 1] + ")</td>";
			}
			severDayTr = severDayTr + "</tr>";
			String trStr = "";
			for (int i = 0; i < meetingRoomList.size(); i++) {// 循环所有车辆				YHMeetingRoom vehicle = meetingRoomList.get(i);
				int roomId = vehicle.getSeqId();
				String mrName = vehicle.getMrName();
				if (YHUtility.isNullorEmpty(vehicle.getMrName())) {
					mrName = "";
				}
				trStr = trStr + "<tr ><td width='20%'  class='TableData'>" + YHUtility.encodeSpecial(mrName) + "</td>";// 循环TR
				for (int j = 0; j < daysList.size(); j++) {// 循环未来7天					String date = (String) daysList.get(j);
					List<YHMeeting> usageList = this.logic.selectMeetingStatus(dbConn, roomId, date);
					if (usageList.size() > 0) {
						trStr = trStr + "<td width='10%' nowrap >";// 循环TD
						trStr = trStr + "<table style='border:1px #7b7b7b solid; border-collapse:collapse;'  width=100% height=100%><tr>";
						for (int k = 0; k < usageList.size(); k++) {// 循环查询出来的车辆使用记录							YHMeeting usage = usageList.get(k);
							String vuStatusColorType = "";
							String vuStatus = usage.getMStatus();
							if (!YHUtility.isNullorEmpty(vuStatus)) {
								if (vuStatus.equals("0")) {
									vuStatusColorType = "#ff33ff";
								}
								if (vuStatus.equals("1")) {
									vuStatusColorType = "#00ff00";
								}
								if (vuStatus.equals("2")) {
									vuStatusColorType = "#ff0000";
								}
							}
							String vuStart = "";// MM-dd
							String vuEnd = "";
							String vuStartMMdd = "";// HH:mm
							String vuEndMMdd = "";

							String curDateStr = dateFormat.format(curDate);// yyyy-MM-dd
							String vuStartY = "";
							String vuEndY = "";
							if (usage.getMStart() != null) {
								vuStart = dateFormat3.format(usage.getMStart());
								vuStartMMdd = dateFormat4.format(usage.getMStart());
								vuStartY = dateFormat.format(usage.getMStart());
							}
							if (usage.getMEnd() != null) {
								vuEnd = dateFormat3.format(usage.getMEnd());
								vuEndMMdd = dateFormat4.format(usage.getMEnd());
								vuEndY = dateFormat.format(usage.getMEnd());
							}
							// 得到开始时间HH：mm
							if (!date.equals(vuStartY)) {// ||vuStartMMdd.compareTo("08:00")<0
								vuStartMMdd = "08:00";
							}
							// 得到结束时间HH：mm
							if (!date.equals(vuEndY)) {// ||vuEndMMdd.compareTo("17:00")>0
								vuEndMMdd = "17:00";
							}
							trStr = trStr + "<td title='" + vuStart + " 至 " + vuEnd + "' bgColor='" + vuStatusColorType + "' width='20%'> " + vuStartMMdd + "-<BR>"
									+ vuEndMMdd + "</td>";
						}
						trStr = trStr + "</tr></table></td>";
					} else {
						trStr = trStr + "<td width='20%'  bgColor='#378CD9' ></td>";// 循环TD
					}
				}
				trStr = trStr + "</tr>";
			}
			String trsStr = severDayTr + trStr;
			String data = "{AllTr:\"" + trsStr + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取会议室管理员名称
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMManagerName(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getMManagerLogic(dbConn);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取有权限的会议室名称
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMRoomName(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String data = this.logic.getMRoomNameLogic(dbConn, person);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 会议查询 -cc
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getSearchMeeting(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		ArrayList<YHAddress> addressList = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			String mName = YHDBUtility.escapeLike(request.getParameter("mName"));
			String mProposer = YHDBUtility.escapeLike(request.getParameter("mProposer"));
			String beginDate = YHDBUtility.escapeLike(request.getParameter("beginDate"));
			String endDate = YHDBUtility.escapeLike(request.getParameter("endDate"));
			String mRoom = YHDBUtility.escapeLike(request.getParameter("mRoom"));
			String mStatus = YHDBUtility.escapeLike(request.getParameter("mStatus"));
			String data = "";
			data = this.logic.getMeetingSearchJson(dbConn, request.getParameterMap(), person, mName, mProposer, beginDate, endDate, mRoom, mStatus);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return null;
	}

	/**
	 * 获取用户名称 -cc
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getUserName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String userId = request.getParameter("userId");
			String data = "";
			if (YHUtility.isInteger(userId)) {
			  data = this.logic.getUserNameLogic(dbConn, Integer.parseInt(userId));
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取会议室名称 -cc
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMeetingRoomName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String seqId = request.getParameter("seqId");
			String data = this.logic.getMeetingRoomNameLogic(dbConn, Integer.parseInt(seqId));
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, "\"" + YHUtility.encodeSpecial(data) + "\"");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 会议查询中－会议纪要－添加 获取会议纪要详情 -cc
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMeetingSummaryDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String seqIdStr = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			int seqId = 0;
			if(!YHUtility.isNullorEmpty(seqIdStr)){
			  seqId = Integer.parseInt(seqIdStr);
			}
			YHMeeting rmsRoll = (YHMeeting) this.logic.getMeetingRoomDetail(dbConn, seqId);
			if (YHUtility.isNullorEmpty(rmsRoll.getSummary())) {
			  StringBuffer data = YHFOM.toJson(rmsRoll);
			  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "无会议记录");
        request.setAttribute(YHActionKeys.RET_DATA, data.toString());
        return "/core/inc/rtjson.jsp";
			}
			if (rmsRoll == null) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				request.setAttribute(YHActionKeys.RET_MSRG, "会议记录已删除");
				return "/core/inc/rtjson.jsp";
			}
			StringBuffer data = YHFOM.toJson(rmsRoll);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
			request.setAttribute(YHActionKeys.RET_DATA, data.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 会议查询中－会议纪要－添加 -wyw
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateMeetingSummary(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHORM orm = new YHORM();
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		
		String optFlag = fileForm.getParameter("optFlag");
		if (YHUtility.isNullorEmpty(optFlag)) {
			optFlag = "0";
		}
		String seqIdStr = fileForm.getParameter("seqId");
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		String contextPath = request.getContextPath();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHMeeting meeting = (YHMeeting) orm.loadObjSingle(dbConn, YHMeeting.class, seqId);
			if(meeting == null){
				response.sendRedirect(contextPath + "/subsys/oa/meeting/summary/summary.jsp?seqId=" + seqId + "&optFlag=1" );
				return null;
			}
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "meeting");
			String attIdStr = sel.getAttachIdToString(",");
			String attNameStr = sel.getAttachNameToString("*");

			boolean fromFolderFlag = false;
			String newAttchId = "";
			String newAttchName = "";
			if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
				newAttchId = attIdStr + ",";
				newAttchName = attNameStr + "*";
				fromFolderFlag = true;
			}
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
				fileForm.saveFile(fieldName, filePath+File.separator +fileName);
			}

			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("fromFolderFlag", fromFolderFlag);
			map.put("newAttchId", newAttchId);
			map.put("newAttchName", newAttchName);
			map.put("uploadFlag", uploadFlag);
			map.put("attachmentId", attachmentId);
			map.put("attachmentName", attachmentName);
			this.logic.updateMeetingSummaryLogic(dbConn, fileForm, person, map);
			
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		
		if ("1".equals(optFlag)) {
			response.sendRedirect(contextPath + "/subsys/oa/meeting/summary/summary.jsp?seqId=" + seqId + "&optFlag=" +optFlag);
		}else {
			response.sendRedirect(contextPath + "/subsys/oa/meeting/summary/summary.jsp?seqId=" + seqId);
		}
		return null;
	}

	/**
	 * 获取会议详情 -cc
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMeetingDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String seqId = request.getParameter("seqId");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHMeeting meeting = (YHMeeting) this.logic.getMeetingRoomDetail(dbConn, Integer.parseInt(seqId));
			if (meeting == null) {
				request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
				request.setAttribute(YHActionKeys.RET_MSRG, "会议信息详情不存在");
				return "/core/inc/rtjson.jsp";
			}
			StringBuffer data = YHFOM.toJson(meeting);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
			request.setAttribute(YHActionKeys.RET_DATA, data.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取会议规则
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getMeetingRule(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String data = this.logic.getMeetingRuleLogic(dbConn);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 新建会议申请信息（周期性会议）--wyw
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addCycleMeetingInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String contexPath = request.getContextPath();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			int mRoom = 0;
			if (!YHUtility.isNullorEmpty(fileForm.getParameter("mRoom"))) {
				mRoom = Integer.parseInt(fileForm.getParameter("mRoom"));
			}
			String mStartDateStr = YHUtility.null2Empty(fileForm.getParameter("M_START_DATE"));
			String mEndDateStr = YHUtility.null2Empty(fileForm.getParameter("M_END_DATE"));
			String mStarTimeStr = YHUtility.null2Empty(fileForm.getParameter("M_START_TIME"));
			String mEndTimeStr = YHUtility.null2Empty(fileForm.getParameter("M_END_TIME"));
			mStartDateStr = mStartDateStr + " " + mStarTimeStr;
			mEndDateStr = mEndDateStr + " " + mEndTimeStr;

			Date mStartDateTime = YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", mStartDateStr);
			Date mEndDateTime = YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", mEndDateStr);

			boolean isHaveMeeting = this.logic.checkRoom(dbConn, 0, mRoom, mStartDateTime, mEndDateTime);
			String  meetingIdStr = this.logic.checkRoomData(dbConn, 0, mRoom, mStartDateTime, mEndDateTime);
			if (isHaveMeeting) {
				String message = mStartDateStr + "有会议冲突";
				response.sendRedirect(contexPath + "/subsys/oa/meeting/apply/existMeeting.jsp?message=" +URLEncoder.encode( message,"UTF-8") + "&meetingIdStr=" + meetingIdStr);
				return null;
			}
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "meeting");
			String attIdStr = sel.getAttachIdToString(",");
			String attNameStr = sel.getAttachNameToString("*");

			boolean fromFolderFlag = false;
			String newAttchId = "";
			String newAttchName = "";
			if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
				newAttchId = attIdStr + ",";
				newAttchName = attNameStr + "*";
				fromFolderFlag = true;
			}
			Iterator<String> iKeys = fileForm.iterateFileFields();
			boolean uploadFlag = false;
			String attachmentId = "";
			String attachmentName = "";
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(date);
			String separator = File.separator;
			String filePath = YHSysProps.getAttachPath() + separator + "meeting" + separator + currDate;

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
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("fromFolderFlag", fromFolderFlag);
			map.put("newAttchId", newAttchId);
			map.put("newAttchName", newAttchName);
			map.put("uploadFlag", uploadFlag);
			map.put("attachmentId", attachmentId);
			map.put("attachmentName", attachmentName);
			this.logic.setCycleMeetingValueLogic(dbConn, fileForm, person, map);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		response.sendRedirect(contexPath + "/subsys/oa/meeting/apply/newMeetingWarn.jsp");
		return null;
	}

	/**
	 * 获取出席人员名称 -wyw
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */

	public String getAttendeeName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String seqIdStr = request.getParameter("seqId");
			int seqId = 0;
			if (!YHUtility.isNullorEmpty(seqIdStr)) {
				seqId = Integer.parseInt(seqIdStr);
			}
			String data = this.logic.getAttendeeNameLogic(dbConn, seqId);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 更新会议信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String updateMeetingInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		String contexPath = request.getContextPath();
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			int mRoom = 0;
			if (!YHUtility.isNullorEmpty(fileForm.getParameter("mRoom"))) {
				mRoom = Integer.parseInt(fileForm.getParameter("mRoom"));
			}
			String mStart =  fileForm.getParameter("mStart");
			String mEnd =  fileForm.getParameter("mEnd");
			Date mStartDateTime = YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", mStart);
			Date mEndDateTime = YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", mEnd);
			boolean isHaveMeeting = this.logic.checkRoom(dbConn, seqId, mRoom, mStartDateTime, mEndDateTime);
			String  meetingIdStr = this.logic.checkRoomData(dbConn, seqId, mRoom, mStartDateTime, mEndDateTime);
			if (isHaveMeeting) {
				String message = mStart + "有会议冲突";
				response.sendRedirect(contexPath + "/subsys/oa/meeting/apply/existMeeting.jsp?returnSeqId=" + seqId + "&message=" +URLEncoder.encode( message,"UTF-8") + "&meetingIdStr=" + meetingIdStr);
				return null;
			}

			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, "meeting");
			String attIdStr = sel.getAttachIdToString(",");
			String attNameStr = sel.getAttachNameToString("*");
			boolean fromFolderFlag = false;
			String newAttchId = "";
			String newAttchName = "";
			if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
				newAttchId = attIdStr + ",";
				newAttchName = attNameStr + "*";
				fromFolderFlag = true;
			}
			
			Map<Object, Object> uploadMap = this.logic.fileUploadLogic(fileForm,this.attachmentFolder);
			String attachmentId = (String) uploadMap.get("attachmentId");
			String attachmentName = (String) uploadMap.get("attachmentName");
			boolean uploadFlag = (Boolean) uploadMap.get("uploadFlag");
			
			String pageAttachmentId = YHUtility.null2Empty(fileForm.getParameter("returnAttId"));
			String pageattachmentName = YHUtility.null2Empty(fileForm.getParameter("returnAttName"));
			if (!YHUtility.isNullorEmpty(pageAttachmentId)) {
				if (pageAttachmentId.lastIndexOf(",")==-1) {
					pageAttachmentId = pageAttachmentId + ",";
				}
				if (pageattachmentName.lastIndexOf("*")==-1) {
					pageattachmentName = pageattachmentName + "*";
				}
			}
			boolean pageAttIdFlag =false;
			if (!YHUtility.isNullorEmpty(pageAttachmentId)) {
				pageAttIdFlag = true;
			}
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("fromFolderFlag", fromFolderFlag);
			map.put("newAttchId", newAttchId);
			map.put("newAttchName", newAttchName);
			
			map.put("uploadFlag", uploadFlag);
			map.put("attachmentId", attachmentId);
			map.put("attachmentName", attachmentName);
			
			map.put("pageAttIdFlag", pageAttIdFlag);
			map.put("pageAttachmentId", pageAttachmentId);
			map.put("pageattachmentName", pageattachmentName);
			this.logic.updateMeetingValueLogic(dbConn, fileForm, person, map,seqId);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		response.sendRedirect(contexPath + "/subsys/oa/meeting/apply/editMeetingWarn.jsp?seqId=" + seqId);
		return null;
	}
	
	/**
   * 单文件附件上传--wyw
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    try{
    	Map<Object, Object> map = this.logic.fileUploadLogic(fileForm,this.attachmentFolder);
    	String attachmentId = (String) map.get("attachmentId");
			String attachmentName = (String) map.get("attachmentName");
    	
			String data = "{attrId:\"" + attachmentId + "\",attrName:\"" + attachmentName + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
      throw e;
    }
    return "/core/inc/rtuploadfile.jsp";
  }
	
	/**
	 * 浮动菜单文件删除--wyw
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
  public String delFloatFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String seqIdStr = request.getParameter("seqId");
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		String delOpt = request.getParameter("delOpt");
		String attachId = request.getParameter("delAttachId");
		String attachName = request.getParameter("delAttachName");
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			boolean updateFlag = this.logic.updateFloadFile(dbConn,seqId, YHUtility.null2Empty(attachId), YHUtility.null2Empty(attachName),YHUtility.null2Empty(delOpt));
			int returnFlag = 0;
			if (updateFlag) {
				returnFlag = 1;
			}
			String data = "{updateFlag:\"" + returnFlag + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
  }
  
  
  
	/**
	 * 删除会议信息--wyw
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delMeetingInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String seqIdStr = request.getParameter("seqId");
		int seqId=0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId=Integer.parseInt(seqIdStr);
		}
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String filePath = YHSysProps.getAttachPath() + File.separator + attachmentFolder + File.separator;
			this.logic.delMeetingLogic(dbConn, seqId,filePath);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 会议纪要查询 --wyw
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryMeetingSummary(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("mName", YHDBUtility.escapeLike(request.getParameter("mName")));
			map.put("mProposer", YHDBUtility.escapeLike(request.getParameter("mProposer")));
			map.put("beginDate", YHDBUtility.escapeLike(request.getParameter("beginDate")));
			map.put("endDate", YHDBUtility.escapeLike(request.getParameter("endDate")));
			map.put("mRoom", YHDBUtility.escapeLike(request.getParameter("mRoom")));
			map.put("keyWord1", YHDBUtility.escapeLike(request.getParameter("keyWord1")));
			map.put("keyWord2", YHDBUtility.escapeLike(request.getParameter("keyWord2")));
			map.put("keyWord3", YHDBUtility.escapeLike(request.getParameter("keyWord3")));
			String data = "";
			data = this.logic.queryMeetingSummaryLogic(dbConn, request.getParameterMap(), person, map);
			PrintWriter pw = response.getWriter();
			pw.println(data);
			pw.flush();
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return null;
	}
	
	/**
	 * 取得在线调度人名称--wyw
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getOnlineUsers(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			
			String data=this.logic.getOnlineUsersLogic(dbConn);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
}
