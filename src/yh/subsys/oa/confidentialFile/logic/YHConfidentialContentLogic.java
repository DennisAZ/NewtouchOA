package yh.subsys.oa.confidentialFile.logic;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.office.ntko.logic.YHNtkoLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.confidentialFile.act.YHConfidentialContentAct;
import yh.subsys.oa.confidentialFile.data.YHConfidentialContent;
import yh.subsys.oa.confidentialFile.data.YHConfidentialSort;

public class YHConfidentialContentLogic {
	private static Logger log = Logger.getLogger(YHConfidentialContentLogic.class);
	public static String COPYPATH = File.separator + "subsys" + File.separator + "oa" + File.separator + "confidentialFile" + File.separator + "fileUtil";

	public List<YHConfidentialContent> getFileContentsInfo(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHConfidentialContent.class, map);
	}

	public YHConfidentialContent getFileContentInfoById(Connection dbConn, int contentId) throws Exception {
		YHORM orm = new YHORM();
		return (YHConfidentialContent) orm.loadObjSingle(dbConn, YHConfidentialContent.class, contentId);
	}

	/**
	 * 拼接附件Id与附件名
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
	 * 得到附件的Id
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
	 * 得到该文件的文件夹名
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
	 * 根据seqId串删除文件
	 * 
	 * @param dbConn
	 * @param seqIdStrs
	 * @param filePath
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public void delFile(Connection dbConn, String seqIdStrs, String filePath, int loginUserSeqId, String ipStr, String recycle, String recyclePath)
			throws NumberFormatException, Exception {
		YHORM orm = new YHORM();

		String[] seqIdStr = seqIdStrs.split(",");
		if (!"".equals(seqIdStrs) && seqIdStrs.split(",").length > 0) {
			// 遍历要选择删除的附件id串
			for (String seqId : seqIdStr) {
				YHConfidentialContent fileContent = this.getFileContentInfoById(dbConn, Integer.parseInt(seqId));
				String attachmentId = YHUtility.null2Empty(fileContent.getAttachmentId());
				String attachmentName = YHUtility.null2Empty(fileContent.getAttachmentName());
				String[] attIdArray = {};
				String[] attNameArray = {};
				if (!YHUtility.isNullorEmpty(attachmentId) && !YHUtility.isNullorEmpty(attachmentName)) {
					attIdArray = attachmentId.trim().split(",");
					attNameArray = attachmentName.trim().split("\\*");
				}
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
								if ("1".equals(recycle.trim())) {
									YHFileUtility.xcopyFile(file.getAbsolutePath(), recyclePath + File.separator + fileNameValue);
								} else {

									YHFileUtility.deleteAll(file.getAbsoluteFile());
								}
							} else if (oldFile.exists()) {
								if ("1".equals(recycle.trim())) {
									YHFileUtility.xcopyFile(oldFile.getAbsolutePath(), recyclePath + File.separator + fileNameValue);
								} else {

									YHFileUtility.deleteAll(oldFile.getAbsoluteFile());
								}

							}

						}
					}

				}

				// 删除数据库信息
				YHConfidentialContent delContent = new YHConfidentialContent();
				delContent.setSeqId(fileContent.getSeqId());
				orm.deleteSingle(dbConn, delContent);

				// 写入系统日志
				String remark = "删除文件,名称:" + fileContent.getSubject();
				YHSysLogLogic.addSysLog(dbConn, YHLogConst.FILE_FOLDER, remark, loginUserSeqId, ipStr);
			}
		}
	}

	/**
	 * 获取文件夹下的文列表
	 * 
	 * @param dbConn
	 * @param request
	 * @param sortIdStr
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getContentFileListJson(Connection dbConn, Map request, String sortIdStr, YHPerson person) throws Exception {
		int sortId = -1;
		if (!YHUtility.isNullorEmpty(sortIdStr)) {
			sortId = Integer.parseInt(sortIdStr);
		}
		try {
			String sql = "select " + " SEQ_ID" + ", SUBJECT" + ", ATTACHMENT_ID" + ", ATTACHMENT_NAME" + ", SEND_TIME" + ", CONTENT_NO" + ", FILE_SEND"
					+ ", SORT_ID" + " from oa_secret_content where SORT_ID=" + sortId + " order by SEND_TIME desc";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
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
			// 保存从文件柜、网络硬盘选择附件
			YHSelAttachUtil sel = new YHSelAttachUtil(fileForm, attachmentFolder);
			String attIdStr = sel.getAttachIdToString(",");
			String attNameStr = sel.getAttachNameToString("*");
			boolean fromFolderFlag = false;
			String forlderAttchId = "";
			String forlderAttchName = "";
			if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
				forlderAttchId = attIdStr + ",";
				forlderAttchName = attNameStr + "*";
				fromFolderFlag = true;
			}

			Iterator<String> iKeys = fileForm.iterateFileFields();
			boolean uploadFlag = false;
			String uploadAttchId = "";
			String uploadAttchName = "";
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
				uploadAttchId += currDate + "_" + rand + ",";
				uploadAttchName += fileName + "*";
				uploadFlag = true;

				fileName = rand + "_" + fileName;
				fileForm.saveFile(fieldName, filePath + File.separator + fileName);
			}
			result.put("fromFolderFlag", fromFolderFlag);
			result.put("forlderAttchId", forlderAttchId);
			result.put("forlderAttchName", forlderAttchName);

			result.put("uploadFlag", uploadFlag);
			result.put("uploadAttchId", uploadAttchId);
			result.put("uploadAttchName", uploadAttchName);
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	public void addNewFileInfoLogic(Connection dbConn, Map<Object, Object> map, YHPerson loginUser) throws Exception {
		YHORM orm = new YHORM();
		try {
			String seqIdStr = (String) map.get("seqIdStr");
			String type = (String) map.get("type");
			String subject = (String) map.get("subject");
			String contentNo = (String) map.get("contentNo");
			String content = (String) map.get("content");
			String attachmentName = (String) map.get("attachmentName");
			String attachmentDesc = (String) map.get("attachmentDesc");
			String contentIdStr = (String) map.get("contentIdStr");
			String smsPerson = (String) map.get("smsPerson");
			String mobileSmsPerson = (String) map.get("mobileSmsPerson");
			String folderPath = (String) map.get("folderPath");
			String attIdStr = (String) map.get("attIdStr");
			String attNameStr = (String) map.get("attNameStr");
			String ipStr = (String) map.get("ipStr");
			String realPath = (String) map.get("realPath");

			if (folderPath == null) {
				folderPath = "";
			}
			int contentId = 0;
			int sortId = 0;
			if (!YHUtility.isNullorEmpty(seqIdStr)) {
				sortId = Integer.parseInt(seqIdStr);
			}
			if (!YHUtility.isNullorEmpty(contentIdStr)) {
				contentId = Integer.parseInt(contentIdStr);
			}
			if (smsPerson == null) {
				smsPerson = "";
			}
			if (mobileSmsPerson == null) {
				mobileSmsPerson = "";
			}
			int loginUserSeqId = loginUser.getSeqId();
			int loginUserDeptId = loginUser.getDeptId();
			String loginUserRoleId = loginUser.getUserPriv();

			boolean fromFolderFlag = false;
			String newAttchId = "";
			String newAttchName = "";
			if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
				newAttchId = attIdStr + ",";
				newAttchName = attNameStr + "*";
				fromFolderFlag = true;
			}
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(new Date());

			int newContentId = 0;
			int newSortId = 0;
			if (contentId != 0) {
				YHConfidentialContent fileContentStr = this.getFileContentInfoById(dbConn, contentId);
				String attIdString = YHUtility.null2Empty(fileContentStr.getAttachmentId());
				String attNameString = YHUtility.null2Empty(fileContentStr.getAttachmentName());
				fileContentStr.setAttachmentId(attIdString + newAttchId.trim());
				fileContentStr.setAttachmentName(attNameString + newAttchName.trim());
				fileContentStr.setContentNo(contentNo);
				fileContentStr.setSubject(subject);
				fileContentStr.setContent(content);
				fileContentStr.setAttachmentDesc(attachmentDesc);
				fileContentStr.setSendTime(YHUtility.parseTimeStamp());
				orm.updateSingle(dbConn, fileContentStr);
				// 系统日志
				String remark = "编辑文件,名称:" + subject;
				YHSysLogLogic.addSysLog(dbConn, YHLogConst.CONFIDENTIAL, remark, loginUserSeqId, ipStr);
			} else {
				YHConfidentialContent fileContent = new YHConfidentialContent();
				boolean newTypeFlag = false;
				String attIdString = "";
				String attNameString = "";
				if (type.trim() != null && !"".equals(type)) {
					String rand = this.createFile(type, attachmentName, realPath);
					if (!rand.equals("0")) {
						attIdString = currDate + "_" + String.valueOf(rand) + ",";
						attNameString = attachmentName + "." + type.trim() + "*";
						newTypeFlag = true;
					}
				}

				if (newTypeFlag && fromFolderFlag) {
					fileContent.setAttachmentId(newAttchId.trim() + attIdString.trim());
					fileContent.setAttachmentName(newAttchName.trim() + attachmentName.trim());
				} else if (newTypeFlag) {
					fileContent.setAttachmentId(attIdString.trim());
					fileContent.setAttachmentName(attNameString.trim());
				} else if (fromFolderFlag) {
					fileContent.setAttachmentId(newAttchId.trim());
					fileContent.setAttachmentName(newAttchName.trim());
				}

				fileContent.setSortId(sortId);
				fileContent.setContentNo(contentNo);
				fileContent.setSubject(subject);
				fileContent.setContent(content);
				fileContent.setAttachmentDesc(attachmentDesc);
				fileContent.setSendTime(YHUtility.parseTimeStamp());
				fileContent.setCreater(String.valueOf(loginUserSeqId));
				fileContent.setFileSend("0");
				orm.saveSingle(dbConn, fileContent);
				newContentId = this.getMaxSeqId(dbConn);

				// 内部短信
				YHSmsBack sms = new YHSmsBack();
				String loginName = this.getPersonNamesByIds(dbConn, String.valueOf(loginUserSeqId));
				String smsContent = loginName + " 在机要文件 " + folderPath + " 下更新文件:" + YHUtility.null2Empty(fileContent.getSubject());
				String remindUrl = "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/read.jsp?sortId=" + sortId + "&contentId=" + contentId
						+ "&newFileFlag=1&openFlag=1";

				if ("allPrivPerson".equals(smsPerson)) {
					YHShowConfidentialSortLogic sortLogic = new YHShowConfidentialSortLogic();
					YHSetConfidentialSortLogic setSortLogic = new YHSetConfidentialSortLogic();
					YHConfidentialSort fileSort2 = sortLogic.getfileSortById(dbConn, sortId);

					String personIdStr = setSortLogic.selectManagerIds(dbConn, fileSort2, "USER_ID");
					String roleIdStr = setSortLogic.getRoleIds(dbConn, fileSort2, "USER_ID");
					String deptIdStr = setSortLogic.getDeptIds(dbConn, fileSort2, "USER_ID");

					if (!YHUtility.isNullorEmpty(personIdStr)) {
						personIdStr += ",";
					}

					// 获取该部门的Id串
					String deptPrivIdStrs = setSortLogic.getPrivDeptIdStr(dbConn, loginUser.getDeptId(), deptIdStr);
					String rolePrivIdStrs = setSortLogic.getPrivRoleIdStr(dbConn, Integer.parseInt(loginUser.getUserPriv()), roleIdStr);
					// 如有权限，获取该部门下的所有人员id串
					String deptPersonIdStr = setSortLogic.getDeptPersonIdStr(loginUser.getDeptId(), deptPrivIdStrs, dbConn);
					String rolePersonIdStr = setSortLogic.getRolePersonIdStr(Integer.parseInt(loginUser.getUserPriv()), rolePrivIdStrs, dbConn);
					String allPersonIdStr = personIdStr + deptPersonIdStr + rolePersonIdStr;
					String allpersonStr = "";
					ArrayList al = new ArrayList();
					String[] arr = allPersonIdStr.split(",");
					for (int i = 0; i < arr.length; i++) {
						if (al.contains(arr[i]) == false) {
							al.add(arr[i]);
							allpersonStr += arr[i] + ",";
						}
					}
					if (allpersonStr.endsWith(",")) {
						allpersonStr = allpersonStr.substring(0, allpersonStr.length()-1);
					}
					if (!YHUtility.isNullorEmpty(allpersonStr)) {
						sms.setFromId(loginUserSeqId);
						sms.setToId(allpersonStr.trim());
						sms.setContent(smsContent);
						sms.setSendDate(YHUtility.parseTimeStamp());
						sms.setSmsType(YHLogConst.CONFIDENTIAL);
						sms.setRemindUrl(remindUrl);
						YHSmsUtil.smsBack(dbConn, sms);
					}

				} else if (!YHUtility.isNullorEmpty(smsPerson)) {
					sms.setFromId(loginUserSeqId);
					sms.setToId(smsPerson);
					sms.setContent(smsContent);
					sms.setSendDate(YHUtility.parseTimeStamp());
					sms.setSmsType(YHLogConst.CONFIDENTIAL);
					sms.setRemindUrl(remindUrl);
					YHSmsUtil.smsBack(dbConn, sms);
				}
				// 手机短信提醒mobileSmsPerson
				String mobileSmsContent = loginName + " 在机要文件 " + folderPath + " 下更新文件:" + YHUtility.null2Empty(fileContent.getSubject());
				YHMobileSms2Logic mobileSms = new YHMobileSms2Logic();
				if ("allPrivPerson".equals(mobileSmsPerson.trim())) {
					YHShowConfidentialSortLogic sortLogic = new YHShowConfidentialSortLogic();
					YHSetConfidentialSortLogic setSortLogic = new YHSetConfidentialSortLogic();
					YHConfidentialSort fileSort2 = sortLogic.getfileSortById(dbConn, sortId);

					String personIdStr = setSortLogic.selectManagerIds(dbConn, fileSort2, "USER_ID");
					String roleIdStr = setSortLogic.getRoleIds(dbConn, fileSort2, "USER_ID");
					String deptIdStr = setSortLogic.getDeptIds(dbConn, fileSort2, "USER_ID");
					if (!"".equals(personIdStr)) {
						personIdStr += ",";
					}
					// 获取该部门的Id串
					String deptPrivIdStrs = setSortLogic.getPrivDeptIdStr(dbConn, loginUser.getDeptId(), deptIdStr);
					String rolePrivIdStrs = setSortLogic.getPrivRoleIdStr(dbConn, Integer.parseInt(loginUser.getUserPriv()), roleIdStr);
					// 如有权限，获取该部门下的所有人员id串
					String deptPersonIdStr = setSortLogic.getDeptPersonIdStr(loginUser.getDeptId(), deptPrivIdStrs, dbConn);
					String rolePersonIdStr = setSortLogic.getRolePersonIdStr(Integer.parseInt(loginUser.getUserPriv()), rolePrivIdStrs, dbConn);
					String allPersonIdStr = personIdStr + deptPersonIdStr + rolePersonIdStr;
					String allpersonStr = "";
					ArrayList al = new ArrayList();
					String[] arr = allPersonIdStr.split(",");
					for (int i = 0; i < arr.length; i++) {
						if (al.contains(arr[i]) == false) {
							al.add(arr[i]);
							allpersonStr += arr[i] + ",";
						}
					}
					if (allpersonStr.endsWith(",")) {
						allpersonStr = allpersonStr.substring(0, allpersonStr.length()-1);
					}
					if (!YHUtility.isNullorEmpty(allpersonStr)) {
						mobileSms.remindByMobileSms(dbConn, allpersonStr, loginUserSeqId, mobileSmsContent, new Date());
					}
				} else if (!YHUtility.isNullorEmpty(mobileSmsPerson)) {
					mobileSms.remindByMobileSms(dbConn, mobileSmsPerson, loginUserSeqId, mobileSmsContent, new Date());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public String createFile(String fileType, String fileName, String webrootPath) throws Exception {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(new Date());
			String separator = File.separator;
			String filePath = YHSysProps.getAttachPath() + separator + YHConfidentialContentAct.attachmentFolder + separator + currDate;

			YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();
			String rand = emut.getRandom();
			String newFileName = rand + "_" + fileName + "." + fileType;
			String tmp = filePath + separator + newFileName;

			String type = fileType.trim();
			if ("xls".equals(type)) {
				String srcFile = webrootPath + this.COPYPATH + File.separator + "copy.xls";
				YHFileUtility.copyFile(srcFile, tmp);
			} else if ("ppt".equals(type)) {
				String srcFile = webrootPath + this.COPYPATH + File.separator + "copy.ppt";
				YHFileUtility.copyFile(srcFile, tmp);
			} else {
				File file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String createPath = file.getPath().replace("\\", "/");
				File createFile = new File(createPath + "/" + newFileName);
				createFile.createNewFile();
			}
			return rand;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取最大的SeqId值
	 * 
	 * @param dbConn
	 * @return
	 */
	public int getMaxSeqId(Connection dbConn) {
		String sql = "select SEQ_ID from oa_secret_content where SEQ_ID=(select MAX(SEQ_ID) from oa_secret_content ) ";
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

	// 新建文件单个文件上传
	public int setNewFileValueLogic(Connection dbConn, YHFileUploadForm fileForm, YHPerson person, Map map) throws Exception {
		YHORM orm = new YHORM();
		try {
			boolean fromFolderFlag = (Boolean) map.get("fromFolderFlag");
			String forlderAttchId = (String) map.get("forlderAttchId");
			String forlderAttchName = (String) map.get("forlderAttchName");

			boolean uploadFlag = (Boolean) map.get("uploadFlag");
			String uploadAttchId = (String) map.get("uploadAttchId");
			String uploadAttchName = (String) map.get("uploadAttchName");

			int sortId = (Integer) map.get("sortId");
			int contentId = (Integer) map.get("contentId");

			String actionFlag = (String) fileForm.getParameter("actionFlag");
			String retrunFlag = fileForm.getParameter("retrunFlag"); // 返回页面returnFolderFlag
			String returnFolderFlag = fileForm.getParameter("returnFolderFlag");

			String subject1 = fileForm.getParameter("subject1");
			String contentNo1 = fileForm.getParameter("contentNo1");
			String content1Str = fileForm.getParameter("content1");
			String attachmentName1 = fileForm.getParameter("attachmentName1");
			String fileType1 = fileForm.getParameter("fileType1");
			String attachmentDesc1 = fileForm.getParameter("attachmentDesc1");

			if (actionFlag == null || "".equals(actionFlag)) {
				actionFlag = "new";
			}
			if (returnFolderFlag == null) {
				returnFolderFlag = "";
			}
			if (retrunFlag == null) {
				retrunFlag = "";
			}
			if (subject1 == null) {
				subject1 = "";
			}
			if (contentNo1 == null) {
				contentNo1 = "";
			}
			if (content1Str == null) {
				content1Str = "";
			}
			if (attachmentName1 == null) {
				attachmentName1 = "";
			}
			if (fileType1 == null) {
				fileType1 = "";
			}
			if (attachmentDesc1 == null) {
				attachmentDesc1 = "";
			}
			if (!"".equals(returnFolderFlag.trim())) {
				retrunFlag = returnFolderFlag;
			}
			int loginUserSeqId = person.getSeqId();

			if ("edit".equals(actionFlag.trim())) {
				YHConfidentialContent content = this.getFileContentInfoById(dbConn, contentId);
				String attIdString = YHUtility.null2Empty(content.getAttachmentId());
				String attNameString = YHUtility.null2Empty(content.getAttachmentName());
				if (fromFolderFlag && uploadFlag) {
					content.setAttachmentId(attIdString.trim() + forlderAttchId.trim() + uploadAttchId.trim());
					content.setAttachmentName(attNameString.trim() + forlderAttchName.trim() + uploadAttchName.trim());
				} else if (fromFolderFlag) {
					content.setAttachmentId(attIdString.trim() + forlderAttchId.trim());
					content.setAttachmentName(attNameString.trim() + forlderAttchName.trim());
				} else if (uploadFlag) {
					content.setAttachmentId(attIdString.trim() + uploadAttchId.trim());
					content.setAttachmentName(attNameString.trim() + uploadAttchName.trim());
				}
				content.setSendTime(YHUtility.parseTimeStamp());
				content.setSubject(subject1);
				content.setContent(content1Str);
				content.setAttachmentDesc(attachmentDesc1);
				content.setContentNo(contentNo1);
				orm.updateSingle(dbConn, content);

			} else if ("new".equals(actionFlag.trim())) {
				YHConfidentialContent fileContent = new YHConfidentialContent();
				fileContent.setSortId(sortId);
				fileContent.setSubject(subject1);
				fileContent.setContent(content1Str);
				fileContent.setSendTime(YHUtility.parseTimeStamp());

				if (fromFolderFlag && uploadFlag) {
					fileContent.setAttachmentId(forlderAttchId.trim() + uploadAttchId.trim());
					fileContent.setAttachmentName(forlderAttchName.trim() + uploadAttchName.trim());
				} else if (fromFolderFlag) {
					fileContent.setAttachmentId(forlderAttchId.trim());
					fileContent.setAttachmentName(forlderAttchName.trim());
				} else if (uploadFlag) {
					fileContent.setAttachmentId(uploadAttchId.trim());
					fileContent.setAttachmentName(uploadAttchName.trim());
				}
				fileContent.setAttachmentDesc(attachmentDesc1);
				fileContent.setContentNo(contentNo1);
				fileContent.setCreater(String.valueOf(loginUserSeqId));
				fileContent.setFileSend("0");
				orm.saveSingle(dbConn, fileContent);

				// 得到最大id
				contentId = this.getMaxSeqId(dbConn);
			}
			actionFlag = "edit";
			return contentId;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据contentId获得文件夹下的内容信息
	 * 
	 * @param dbConn
	 * @param person
	 * @param contentId
	 * @return
	 * @throws Exception
	 */
	public YHConfidentialContent getFileContentByIdLogic(Connection dbConn, int contentId) throws Exception {
		try {
			YHORM orm = new YHORM();
			return (YHConfidentialContent) orm.loadObjSingle(dbConn, YHConfidentialContent.class, contentId);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 新建文件中的"新建附件"
	 * 
	 * @param dbConn
	 * @param person
	 * @param map
	 * @throws Exception
	 */
	public Map<Object, Object> setCreateFileLogic(Connection dbConn, YHPerson person, Map map) throws Exception {
		YHORM orm = new YHORM();
		String actionFlag = "";
		try {
			String seqIdStr = (String) map.get("seqIdStr");
			String contentIdStr = (String) map.get("contentIdStr");
			String fileType = (String) map.get("fileType");
			String attachmentName = (String) map.get("attachmentName");
			String subject = (String) map.get("subject");
			String contentNo = (String) map.get("contentNo");
			String content = (String) map.get("content");
			String attDesc = (String) map.get("attDesc");

			String realPath = (String) map.get("realPath");

			int seqId = 0;
			int contentId = 0;
			if (contentIdStr != null) {
				contentId = Integer.parseInt(contentIdStr);
			}
			if (!YHUtility.isNullorEmpty(seqIdStr)) {
				seqId = Integer.parseInt(seqIdStr);
			}
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(new Date());
			int loginUserSeqId = person.getSeqId();

			String attachmentIds = "";
			String attachmentNames = "";

			String newAttachIdStr = "";
			String newAttachNameStr = "";

			if (!YHUtility.isNullorEmpty(fileType) && !YHUtility.isNullorEmpty(attachmentName)) {
				String rand = this.createFile(fileType, attachmentName, realPath);

				if (!YHUtility.isNullorEmpty(rand)) {
					attachmentIds = currDate + "_" + String.valueOf(rand) + ",";
					attachmentNames = attachmentName + "." + fileType + "*";

					newAttachIdStr = currDate + "_" + String.valueOf(rand);
					newAttachNameStr = attachmentName + "." + fileType;
				}

				if (contentId != 0) {
					YHConfidentialContent eitContent = this.getFileContentInfoById(dbConn, contentId);
					String attIdString = YHUtility.null2Empty(eitContent.getAttachmentId());
					String attNameString = YHUtility.null2Empty(eitContent.getAttachmentName());

					eitContent.setAttachmentId(attIdString + attachmentIds);
					eitContent.setAttachmentName(attNameString + attachmentNames);
					eitContent.setSendTime(YHUtility.parseTimeStamp());
					eitContent.setSubject(subject);
					eitContent.setContent(content);
					eitContent.setAttachmentDesc(attDesc);
					eitContent.setContentNo(contentNo);
					orm.updateSingle(dbConn, eitContent);
				} else {
					YHConfidentialContent fileContent = new YHConfidentialContent();

					fileContent.setSortId(seqId);
					fileContent.setAttachmentId(attachmentIds); // 1006_2a4c3044f00b76b721a3374c587c4146,
					fileContent.setAttachmentName(attachmentNames); // 新建gggg.doc*
					fileContent.setAttachmentDesc(attDesc);

					fileContent.setContent(content);
					fileContent.setContentNo(contentNo);
					fileContent.setSubject(subject);
					fileContent.setSendTime(YHUtility.parseTimeStamp());
					fileContent.setCreater(String.valueOf(loginUserSeqId));
					fileContent.setFileSend("0");
					orm.saveSingle(dbConn, fileContent);
					// 得到最大id
					contentId = this.getMaxSeqId(dbConn);
				}
			}
			actionFlag = "edit";
			Map<Object, Object> returnMap = new HashMap<Object, Object>();
			returnMap.put("contentId", contentId);
			returnMap.put("actionFlag", actionFlag);
			returnMap.put("newAttachIdStr", newAttachIdStr);
			returnMap.put("newAttachNameStr", newAttachNameStr);
			return returnMap;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 浮动菜单文件删除
	 * 
	 * @param dbConn
	 * @param attId
	 * @param attName
	 * @param contentId
	 * @return
	 * @throws Exception
	 */
	public boolean delFloatFile(Connection dbConn, String attId, String attName, int contentId) throws Exception {
		YHORM orm = new YHORM();
		boolean updateFlag = false;
		try {
			if (YHUtility.isNullorEmpty(attId)) {
				attId = "";
			}
			if (YHUtility.isNullorEmpty(attName)) {
				attName = "";
			}
			YHConfidentialContent fileContent = this.getFileContentByIdLogic(dbConn, contentId);
			String[] attIdArray = {};
			String[] attNameArray = {};
			if (fileContent != null) {
				String attachmentId = YHUtility.null2Empty(fileContent.getAttachmentId());
				String attachmentName = YHUtility.null2Empty(fileContent.getAttachmentName());

				if (!YHUtility.isNullorEmpty(attachmentId) && !YHUtility.isNullorEmpty(attachmentName)) {
					attIdArray = attachmentId.trim().split(",");
					attNameArray = attachmentName.trim().split("\\*");
				}
				String attaId = "";
				String attaName = "";
				for (int i = 0; i < attIdArray.length; i++) {
					if (attId.equals(attIdArray[i])) {
						continue;
					}
					attaId += attIdArray[i] + ",";
					attaName += attNameArray[i] + "*";
				}
				fileContent.setAttachmentId(attaId.trim());
				fileContent.setAttachmentName(attaName.trim());
				orm.updateSingle(dbConn, fileContent);
				updateFlag = true;
			}
		} catch (Exception e) {
			throw e;
		}

		return updateFlag;
	}

	/**
	 * 批量上传文件
	 * 
	 * @param dbConn
	 * @param content
	 * @param request
	 * @throws Exception
	 */
	public void uploadFileLogic(Connection dbConn, HttpServletRequest request) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		fileForm.parseUploadRequest(request);

		String seqId = request.getParameter("seqId");
		String smsPerson = fileForm.getParameter("smsPerson");
		String mobileSmsPerson = fileForm.getParameter("mobileSmsPerson");
		String folderPath = fileForm.getParameter("folderPath");

		int sortId = 0;
		if (seqId != null) {
			sortId = Integer.parseInt(seqId);
		}
		if (smsPerson == null) {
			smsPerson = "";
		}
		if (mobileSmsPerson == null) {
			mobileSmsPerson = "";
		}
		if (folderPath == null) {
			folderPath = "";
		}
		String subjectStr = "";
		YHORM orm = new YHORM();
		YHConfidentialContent content = new YHConfidentialContent();
		try {
			YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
			int loginUserSeqId = loginUser.getSeqId();
			int loginUserDeptId = loginUser.getDeptId();
			String loginUserRoleId = loginUser.getUserPriv();
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(date);
			String separator = File.separator;
			String filePath = YHSysProps.getAttachPath() + separator + YHConfidentialContentAct.attachmentFolder + separator + currDate;

			Iterator<String> keysIte = fileForm.iterateFileFields();
			while (keysIte.hasNext()) {

				String fieldName = keysIte.next();
				String fileName = fileForm.getFileName(fieldName);
				if (YHUtility.isNullorEmpty(fileName)) {
					continue;
				}

				YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();
				content.setSortId(sortId);
				content.setSendTime(YHUtility.parseTimeStamp());
				content.setAttachmentName(fileName.trim() + "*");
				String[] fName = fileName.split("\\.");
				content.setSubject(fName[0]);
				subjectStr = fName[0];

				String rand = emut.getRandom();
				fileName = rand + "_" + fileName;
				fileForm.saveFile(fieldName, filePath + File.separator + fileName);
				content.setAttachmentId(currDate + "_" + String.valueOf(rand) + ",");
				content.setCreater(String.valueOf(loginUserSeqId));
			}
			orm.saveSingle(dbConn, content);
			// int contentId = this.getMaxSeqId(dbConn);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取cookie值
	 * 
	 * @param request
	 * @param cName
	 * @return
	 * @throws Exception
	 */
	public String getCookieValue(HttpServletRequest request, String cookieName) throws Exception {
		String value = "";
		if (YHUtility.isNullorEmpty(cookieName)) {
			cookieName = "";
		}
		try {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					if (cookieName.equals(cookie.getName())) {
						value = cookie.getValue();
						break;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return value;
	}

	/**
	 * 获取cookie
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		if (YHUtility.isNullorEmpty(cookieName)) {
			cookieName = "";
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookieName.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}

	/**
	 * 复制文件操作
	 * 
	 * @param dbConn
	 * @param seqIdStrs
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public void copyFile(Connection dbConn, String seqIdStrs, String sortId, String filePath) throws NumberFormatException, Exception {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(date);
		YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();

		YHORM orm = new YHORM();
		String randFlag = "";
		String newAttName = "";
		String[] seqIdStr = seqIdStrs.split(",");
		if (!"".equals(seqIdStrs) && seqIdStr.length > 0) {
			// 遍历要选择附件id串
			for (String seqId : seqIdStr) {
				boolean isHave = false;
				YHConfidentialContent fileContent = this.getFileContentInfoById(dbConn, Integer.parseInt(seqId));
				if (fileContent != null) {
					String attachmentId = YHUtility.null2Empty(fileContent.getAttachmentId());
					String attachmentName = YHUtility.null2Empty(fileContent.getAttachmentName());

					String[] attIdArray = {};
					String[] attNameArray = {};
					if (attachmentId != null && attachmentName != null) {
						attIdArray = attachmentId.split(",");
						attNameArray = attachmentName.split("\\*");
					}
					for (int i = 0; i < attIdArray.length; i++) {
						Map<String, String> map = this.getFileName(attIdArray[i], attNameArray[i]);
						// 遍历Set集合
						if (map.size() != 0) {
							Set<String> set = map.keySet();
							for (String keySet : set) {
								String rand = emut.getRandom();
								String key = keySet;
								String keyValue = map.get(keySet);
								String attaIdStr = this.getAttaId(keySet);
								String newAttaName = rand + "_" + keyValue;
								String fileNameValue = attaIdStr + "_" + keyValue;
								String fileFolder = this.getFilePathFolder(key);
								String oldfileNameValue = attaIdStr + "." + keyValue;
								String fileNamePath = filePath + File.separator + fileFolder + File.separator + fileNameValue;
								String oldFileNamePath = filePath + File.separator + fileFolder + File.separator + oldfileNameValue;
								File file = new File(fileNamePath);
								File oldFile = new File(oldFileNamePath);
								if (file.exists()) {
									YHFileUtility.copyFile(file.getPath(), filePath + File.separator + currDate + File.separator + newAttaName);
									randFlag += currDate + "_" + rand + ",";
									newAttName += keyValue + "*";
									isHave = true;
								} else if (oldFile.exists()) {
									YHFileUtility.copyFile(oldFile.getPath(), filePath + File.separator + currDate + File.separator + newAttaName);
									randFlag += currDate + "_" + rand + ",";
									newAttName += keyValue + "*";
									isHave = true;
								}
							}
						}
					}
					if (isHave) {
						// 保存到数据库
						fileContent.setAttachmentId(randFlag);
						fileContent.setAttachmentName(newAttName.trim());
					}
					fileContent.setSortId(Integer.parseInt(sortId));
					fileContent.setSendTime(YHUtility.parseTimeStamp());
					orm.saveSingle(dbConn, fileContent);
				}
			}
		}
	}

	/**
	 * 剪切文件操作
	 * 
	 * @param dbConn
	 * @param seqIdStrs
	 * @param sortId
	 * @param filePath
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public void cutFile(Connection dbConn, String seqIdStrs, String sortId, String filePath) throws NumberFormatException, Exception {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(date);
		YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();

		String randFlag = "";
		String newAttName = "";
		boolean isHave = false;

		YHORM orm = new YHORM();
		String[] seqIdStr = seqIdStrs.split(",");
		if (!"".equals(seqIdStrs) && seqIdStrs.split(",").length > 0) {
			// 遍历选择的附件Id串

			for (String seqId : seqIdStr) {
				YHConfidentialContent fileContent = this.getFileContentInfoById(dbConn, Integer.parseInt(seqId));
				String attachmentId = "";
				String attachmentName = "";
				if (fileContent != null) {
					attachmentId = YHUtility.null2Empty(fileContent.getAttachmentId());
					attachmentName = YHUtility.null2Empty(fileContent.getAttachmentName());

					YHConfidentialContent delContent = new YHConfidentialContent();
					String[] attIdArray = {};
					String[] attNameArray = {};
					if (!YHUtility.isNullorEmpty(attachmentId) && !YHUtility.isNullorEmpty(attachmentName)) {
						attIdArray = attachmentId.split(",");
						attNameArray = attachmentName.split("\\*");
					}
					for (int i = 0; i < attIdArray.length; i++) {
						Map<String, String> map = this.getFileName(attIdArray[i], attNameArray[i]);
						if (map.size() != 0) {
							Set<String> set = map.keySet();
							// 遍历Set集合
							for (String keySet : set) {
								String rand = emut.getRandom();
								String key = keySet;
								String keyValue = map.get(keySet);
								String attaIdStr = this.getAttaId(keySet);
								String fileNameValue = attaIdStr + "_" + keyValue;
								String newAttaName = rand + "_" + keyValue;
								String fileFolder = this.getFilePathFolder(key);

								String oldfileNameValue = attaIdStr + "." + keyValue;
								String fileNamePath = filePath + File.separator + fileFolder + File.separator + fileNameValue;
								String oldFileNamePath = filePath + File.separator + fileFolder + File.separator + oldfileNameValue;
								File file = new File(fileNamePath);
								File oldFile = new File(oldFileNamePath);

								if (file.exists()) {
									YHFileUtility.xcopyFile(file.getPath(), filePath + File.separator + currDate + File.separator + newAttaName);
									randFlag += currDate + "_" + rand + ",";
									newAttName += keyValue + "*";
									isHave = true;
								} else if (oldFile.exists()) {
									YHFileUtility.xcopyFile(oldFile.getPath(), filePath + File.separator + currDate + File.separator + newAttaName);
									randFlag += currDate + "_" + rand + ",";
									newAttName += keyValue + "*";
									isHave = true;
								}
							}
						}
					}
					if (isHave) {
						delContent.setSeqId(fileContent.getSeqId());
						// 删除旧信息
						orm.deleteSingle(dbConn, delContent);
						// 插入新信息
						fileContent.setSortId(Integer.parseInt(sortId));
						fileContent.setSendTime(YHUtility.parseTimeStamp());
						fileContent.setAttachmentId(randFlag.trim());
						fileContent.setAttachmentName(newAttName.trim());
						orm.saveSingle(dbConn, fileContent);
					} else {

						delContent.setSeqId(fileContent.getSeqId());
						orm.deleteSingle(dbConn, delContent);
						fileContent.setSortId(Integer.parseInt(sortId));
						fileContent.setSendTime(YHUtility.parseTimeStamp());
						orm.saveSingle(dbConn, fileContent);
					}
				}
			}
		}
	}

	/**
	 * 文件下载
	 * 
	 * @param attachmentName
	 * @param attachmentId
	 * @param module
	 * @return
	 * @throws Exception
	 */
	public HashMap toZipInfoMapFile(Connection dbConn, int sortId, String seqIds, String module, YHPerson loginUser)
			throws Exception {
		HashMap result = new HashMap();
		YHORM orm = new YHORM();
		if (seqIds == null || "".equals(seqIds.trim())) {
			return result;
		}
		if (seqIds.trim().endsWith(",")) {
			seqIds = seqIds.trim().substring(0, seqIds.trim().length() - 1);
		}
		String[] filters = { "SEQ_ID IN(" + seqIds + ")" };
		ArrayList<YHConfidentialContent> fileContents = (ArrayList<YHConfidentialContent>) orm.loadListSingle(dbConn, YHConfidentialContent.class,
				filters);
		HashMap<String, Integer> subjectNames = new HashMap<String, Integer>();
		YHNtkoLogic ntkoLogic = new YHNtkoLogic();
		for (int j = 0; j < fileContents.size(); j++) {
			YHConfidentialContent fileContent = fileContents.get(j);
			String[] attachmentArray = YHUtility.null2Empty(fileContent.getAttachmentName()).split("\\*");
			String[] attachmentIdArray = YHUtility.null2Empty(fileContent.getAttachmentId()).split(",");
			String subject = YHUtility.null2Empty(fileContent.getSubject());
			if (subjectNames.keySet().contains(subject.trim())) {
				int count = subjectNames.get(subject.trim());
				subject = subject + "_" + count;
				subjectNames.put(subject.trim(), count + 1);
			} else {
				subjectNames.put(subject.trim(), 1);
			}
			HashMap<String, Integer> filesName = new HashMap<String, Integer>();

			YHConfidentialSort fileSort = new YHConfidentialSort();
			boolean downPriv = true;
			if (sortId != 0) {
				fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, sortId);
				// 取得权限
				downPriv = this.getDownPriv(dbConn, fileSort, loginUser);
			}
			for (int i = 0; i < attachmentIdArray.length; i++) {
				if ("".equals(attachmentIdArray[i].trim()) || "".equals(attachmentArray[i].trim())) {
					continue;
				}
				String fileType = YHFileUtility.getFileExtName(attachmentArray[i].trim());
				// 判断是否为office文件
				boolean isOffice = this.isOfficeFile("." + fileType);
				if (isOffice && !downPriv) {
					continue;
				}

				String attachName = attachmentArray[i].trim();
				String temp = ntkoLogic.getAttachBytes(attachName, attachmentIdArray[i].trim(), module);
				String fileName = "";
				if (temp != null) {
					String preName = attachName.substring(0, attachName.lastIndexOf("."));
					if (filesName.keySet().contains(attachName.trim())) {
						int count = filesName.get(attachName.trim());
						String extName = attachName.substring(attachName.lastIndexOf("."), attachName.length());
						fileName = preName + "_" + count + extName;
						filesName.put(attachName.trim(), count + 1);
					} else {
						filesName.put(attachName.trim(), 1);
						fileName = attachName;
					}
					result.put(subject + "/" + "附件" + "/" + fileName, temp); // 附件内容
				}
				result.put(subject + "/" + "附件" + "/", null); // 标题为文件夹下的附件文件夹
			}
			result.put(subject + "/", null); // 以标题为文件夹名，

			String createName = this.getPersonNamesByIds(dbConn, String.valueOf(fileContent.getCreater()));

			String html = "<html><head><title>" + subject + "</title></head>";
			html += "<style>body{font-size:12px;} table{border:1px #000 solid;border-collapse:collapse;} table td{border:1px #000 solid;}</style>";
			html += "<body><table width='70%' align='center'><tr><td align='center' colspan='2'><b><span class='big'>" + subject
					+ "&nbsp;</span></b></td></tr>";
			html += "<tr><td height='250' valign='top' colspan='2'>" + YHUtility.encodeSpecial(YHUtility.null2Empty(fileContent.getContent()))
					+ "&nbsp;</td></tr>";
			html += "<tr class=small><td width='100'>创建人：</td><td width='400'>" + createName + "&nbsp;</td></tr></table></body></html>";
			/* FileInputStream htmlIn = new FileInputStream( html.getBytes()); */
			InputStream in = new ByteArrayInputStream(html.getBytes());
			result.put(subject + "/" + subject + ".html", in); // 生成的hmtl页面
		}

		return result;
	}

	/**
	 * 取得下载权限
	 * 
	 * @param dbConn
	 * @param fileSort
	 * @return
	 * @throws Exception
	 */
	public boolean getDownPriv(Connection dbConn, YHConfidentialSort fileSort, YHPerson user) throws Exception {
		boolean flag = false;
		int loginUserSeqId = user.getSeqId();
		int loginUserDeptId = user.getDeptId();
		String loginUserRoleId = user.getUserPriv();

		int downPrivFlag = 0;
		int managePrivFlag = 0;
		YHShowConfidentialSortLogic showLogic = new YHShowConfidentialSortLogic();
		YHSetConfidentialSortLogic logic = new YHSetConfidentialSortLogic();
		String[] actions = new String[] { "DOWN_USER", "MANAGE_USER" };
		try {

			for (int i = 0; i < actions.length; i++) {
				if ("DOWN_USER".equals(actions[i])) {
					String userPrivs = logic.selectManagerIds(dbConn, fileSort, "DOWN_USER");
					String rolePrivs = logic.getRoleIds(dbConn, fileSort, "DOWN_USER");
					String deptPrivs = logic.getDeptIds(dbConn, fileSort, "DOWN_USER");

					boolean userFlag = showLogic.checkUserIdPriv(loginUserSeqId, userPrivs);
					boolean roleFlag = showLogic.checkUserIdPriv(Integer.parseInt(loginUserRoleId), rolePrivs);
					boolean deptFlag = showLogic.chekDeptIdPriv(loginUserDeptId, deptPrivs);
					if (userFlag || deptFlag || roleFlag) {
						downPrivFlag = 1;
					}
				}
				if ("MANAGE_USER".equals(actions[i])) {
					String userPrivs = logic.selectManagerIds(dbConn, fileSort, "MANAGE_USER");
					String rolePrivs = logic.getRoleIds(dbConn, fileSort, "MANAGE_USER");
					String deptPrivs = logic.getDeptIds(dbConn, fileSort, "MANAGE_USER");

					boolean userFlag = showLogic.checkUserIdPriv(loginUserSeqId, userPrivs);
					boolean roleFlag = showLogic.checkUserIdPriv(Integer.parseInt(loginUserRoleId), rolePrivs);
					boolean deptFlag = showLogic.chekDeptIdPriv(loginUserDeptId, deptPrivs);
					if (userFlag || deptFlag || roleFlag) {
						managePrivFlag = 1;
					}
				}
			}
			if (downPrivFlag == 1 || managePrivFlag == 1) {
				flag = true;
			}
		} catch (Exception e) {
			throw e;
		}

		return flag;
	}

	/**
	 * 判断是否为office文件
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public boolean isOfficeFile(String fileType) throws Exception {
		boolean flag = false;
		try {
			if (!YHUtility.isNullorEmpty(fileType)) {
				if (".doc".equals(fileType) || ".xls".equals(fileType) || ".ppt".equals(fileType) || ".pps".equals(fileType) || ".docx".equals(fileType)
						|| ".xlsx".equals(fileType) || ".pptx".equals(fileType) || ".ppsx".equals(fileType) || "wps".equals(fileType) || ".et".equals(fileType)
						|| ".ett".equals(fileType)) {
					flag = true;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

	/**
	 * 根据人员id字符串得到name字符串
	 * 
	 * @param dbConn
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public String getPersonNamesByIds(Connection conn, String ids) throws Exception {
		String names = "";
		if (!YHUtility.isNullorEmpty(ids)) {
			if (ids.endsWith(",")) {
				ids = ids.substring(0, ids.length() - 1);
			}
			String query = "select USER_NAME from PERSON where SEQ_ID in (" + ids + ")";
			Statement stm = null;
			ResultSet rs = null;
			try {
				stm = conn.createStatement();
				rs = stm.executeQuery(query);
				while (rs.next()) {
					names += rs.getString("USER_NAME") + ",";
				}
			} catch (Exception ex) {
				throw ex;
			} finally {
				YHDBUtility.close(stm, rs, log);
			}
		}
		if (names.endsWith(",")) {
			names = names.substring(0, names.length() - 1);
		}
		return names;
	}

	/**
	 * 更新文件内容信息
	 * 
	 * @param dbConn
	 * @param map
	 * @param loginUser
	 * @throws Exception
	 */
	public void updateFileInfoLogic(Connection dbConn, Map<Object, Object> map, YHPerson loginUser) throws Exception {
		YHORM orm = new YHORM();
		try {
			String seqIdStr = (String) map.get("seqIdStr");
			String type = (String) map.get("type");
			String subject = (String) map.get("subject");
			String contentNo = (String) map.get("contentNo");
			String content = (String) map.get("content");
			String attachmentName = (String) map.get("attachmentName");
			String attachmentDesc = (String) map.get("attachmentDesc");
			String contentIdStr = (String) map.get("contentIdStr");
			String smsPerson = (String) map.get("smsPerson");
			String mobileSmsPerson = (String) map.get("mobileSmsPerson");
			String folderPath = (String) map.get("folderPath");
			String attIdStr = (String) map.get("attIdStr");
			String attNameStr = (String) map.get("attNameStr");
			String ipStr = (String) map.get("ipStr");
			String realPath = (String) map.get("realPath");

			if (folderPath == null) {
				folderPath = "";
			}

			int contentId = 0;
			int sortId = 0;
			if (!YHUtility.isNullorEmpty(seqIdStr)) {
				sortId = Integer.parseInt(seqIdStr);
			}
			if (!YHUtility.isNullorEmpty(contentIdStr)) {
				contentId = Integer.parseInt(contentIdStr);
			}
			if (smsPerson == null) {
				smsPerson = "";
			}
			if (mobileSmsPerson == null) {
				mobileSmsPerson = "";
			}
			int loginUserSeqId = loginUser.getSeqId();
			// int loginUserDeptId = loginUser.getDeptId();
			// String loginUserRoleId = loginUser.getUserPriv();

			boolean fromFolderFlag = false;
			String newAttchId = "";
			String newAttchName = "";
			if (!"".equals(attIdStr) && !"".equals(attNameStr)) {
				newAttchId = attIdStr + ",";
				newAttchName = attNameStr + "*";
				fromFolderFlag = true;
			}
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(new Date());

			boolean newTypeFlag = false;
			String attIdString = "";
			String attNameString = "";
			if (type.trim() != null && !"".equals(type)) {
				String rand = this.createFile(type, attachmentName, realPath);
				if (!rand.equals("0")) {
					attIdString = currDate + "_" + String.valueOf(rand) + ",";
					attNameString = attachmentName + "." + type.trim() + "*";
					newTypeFlag = true;
				}
			}

			YHConfidentialContent dbFileContent = this.getFileContentInfoById(dbConn, contentId);
			String dbAttIdStr = "";
			String dbAttNameStr = "";
			if (dbFileContent != null) {
				dbAttIdStr = YHUtility.null2Empty(dbFileContent.getAttachmentId());
				dbAttNameStr = YHUtility.null2Empty(dbFileContent.getAttachmentName());
				if (newTypeFlag && fromFolderFlag) {
					dbFileContent.setAttachmentId(dbAttIdStr.trim() + newAttchId.trim() + attIdString.trim());
					dbFileContent.setAttachmentName(dbAttNameStr.trim() + newAttchName.trim() + attachmentName.trim());
				} else if (newTypeFlag) {
					dbFileContent.setAttachmentId(dbAttIdStr.trim() + attIdString.trim());
					dbFileContent.setAttachmentName(dbAttNameStr.trim() + attNameString.trim());
				} else if (fromFolderFlag) {
					dbFileContent.setAttachmentId(dbAttIdStr.trim() + newAttchId.trim());
					dbFileContent.setAttachmentName(dbAttNameStr.trim() + newAttchName.trim());
				}

				dbFileContent.setContentNo(contentNo);
				dbFileContent.setSubject(subject);
				dbFileContent.setContent(content);
				dbFileContent.setAttachmentDesc(attachmentDesc);
				dbFileContent.setSendTime(YHUtility.parseTimeStamp());

				String createName = this.getPersonNamesByIds(dbConn, String.valueOf(loginUserSeqId));
				String logStr = YHUtility.null2Empty(dbFileContent.getLogs());
				String brStr = "";
				if (!YHUtility.isNullorEmpty(logStr)) {
//					brStr = "\r\n";
					brStr = "<br>";
				}
				Date date = new Date();
				String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				String logString = "";
				if (newTypeFlag || fromFolderFlag) {
					logString = logStr + brStr + timeStr + " " + createName + " " + "增加附件";
				} else {
					logString = logStr + brStr + timeStr + " " + createName + " " + "修改文件";
				}
				dbFileContent.setLogs(logString.trim());
				orm.updateSingle(dbConn, dbFileContent);

				// 短信提醒
				YHSmsBack sms = new YHSmsBack();
				String loginName = this.getPersonNamesByIds(dbConn, String.valueOf(loginUserSeqId));
				String smsContent = loginName + " 在机要文件 " + folderPath + " 下更新文件:" + YHUtility.null2Empty(dbFileContent.getSubject());
				String remindUrl = "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/read.jsp?sortId=" + sortId + "&contentId=" + contentId
						+ "&newFileFlag=1&openFlag=1";

				if ("allPrivPerson".equals(smsPerson)) {
					YHShowConfidentialSortLogic sortLogic = new YHShowConfidentialSortLogic();
					YHSetConfidentialSortLogic setSortLogic = new YHSetConfidentialSortLogic();
					YHConfidentialSort fileSort2 = sortLogic.getfileSortById(dbConn, sortId);

					String personIdStr = setSortLogic.selectManagerIds(dbConn, fileSort2, "USER_ID");
					String roleIdStr = setSortLogic.getRoleIds(dbConn, fileSort2, "USER_ID");
					String deptIdStr = setSortLogic.getDeptIds(dbConn, fileSort2, "USER_ID");

					if (!YHUtility.isNullorEmpty(personIdStr)) {
						personIdStr += ",";
					}

					// 获取该部门的Id串
					String deptPrivIdStrs = setSortLogic.getPrivDeptIdStr(dbConn, loginUser.getDeptId(), deptIdStr);
					String rolePrivIdStrs = setSortLogic.getPrivRoleIdStr(dbConn, Integer.parseInt(loginUser.getUserPriv()), roleIdStr);
					// 如有权限，获取该部门下的所有人员id串
					String deptPersonIdStr = setSortLogic.getDeptPersonIdStr(loginUser.getDeptId(), deptPrivIdStrs, dbConn);
					String rolePersonIdStr = setSortLogic.getRolePersonIdStr(Integer.parseInt(loginUser.getUserPriv()), rolePrivIdStrs, dbConn);
					String allPersonIdStr = personIdStr + deptPersonIdStr + rolePersonIdStr;
					String allpersonStr = "";
					ArrayList al = new ArrayList();
					String[] arr = allPersonIdStr.split(",");
					for (int i = 0; i < arr.length; i++) {
						if (al.contains(arr[i]) == false) {
							al.add(arr[i]);
							allpersonStr += arr[i] + ",";
						}
					}
					if (!YHUtility.isNullorEmpty(allpersonStr)) {
						sms.setFromId(loginUserSeqId);
						sms.setToId(allpersonStr.trim());
						sms.setContent(smsContent);
						sms.setSendDate(YHUtility.parseTimeStamp());
						sms.setSmsType(YHLogConst.CONFIDENTIAL);
						sms.setRemindUrl(remindUrl);
						YHSmsUtil.smsBack(dbConn, sms);
					}

				} else if (!YHUtility.isNullorEmpty(smsPerson)) {
					sms.setFromId(loginUserSeqId);
					sms.setToId(smsPerson);
					sms.setContent(smsContent);
					sms.setSendDate(YHUtility.parseTimeStamp());
					sms.setSmsType(YHLogConst.CONFIDENTIAL);
					sms.setRemindUrl(remindUrl);
					YHSmsUtil.smsBack(dbConn, sms);
				}
				// 手机短信提醒mobileSmsPerson
				String mobileSmsContent = loginName + " 在机要文件 " + folderPath + " 下更新文件:" + YHUtility.null2Empty(dbFileContent.getSubject());
				YHMobileSms2Logic mobileSms = new YHMobileSms2Logic();
				if ("allPrivPerson".equals(mobileSmsPerson.trim())) {
					YHShowConfidentialSortLogic sortLogic = new YHShowConfidentialSortLogic();
					YHSetConfidentialSortLogic setSortLogic = new YHSetConfidentialSortLogic();
					YHConfidentialSort fileSort2 = sortLogic.getfileSortById(dbConn, sortId);

					String personIdStr = setSortLogic.selectManagerIds(dbConn, fileSort2, "USER_ID");
					String roleIdStr = setSortLogic.getRoleIds(dbConn, fileSort2, "USER_ID");
					String deptIdStr = setSortLogic.getDeptIds(dbConn, fileSort2, "USER_ID");
					if (!"".equals(personIdStr)) {
						personIdStr += ",";
					}
					// 获取该部门的Id串
					String deptPrivIdStrs = setSortLogic.getPrivDeptIdStr(dbConn, loginUser.getDeptId(), deptIdStr);
					String rolePrivIdStrs = setSortLogic.getPrivRoleIdStr(dbConn, Integer.parseInt(loginUser.getUserPriv()), roleIdStr);
					// 如有权限，获取该部门下的所有人员id串
					String deptPersonIdStr = setSortLogic.getDeptPersonIdStr(loginUser.getDeptId(), deptPrivIdStrs, dbConn);
					String rolePersonIdStr = setSortLogic.getRolePersonIdStr(Integer.parseInt(loginUser.getUserPriv()), rolePrivIdStrs, dbConn);
					String allPersonIdStr = personIdStr + deptPersonIdStr + rolePersonIdStr;
					String allpersonStr = "";
					ArrayList al = new ArrayList();
					String[] arr = allPersonIdStr.split(",");
					for (int i = 0; i < arr.length; i++) {
						if (al.contains(arr[i]) == false) {
							al.add(arr[i]);
							allpersonStr += arr[i] + ",";
						}
					}
					if (!YHUtility.isNullorEmpty(allpersonStr)) {
						mobileSms.remindByMobileSms(dbConn, allpersonStr, loginUserSeqId, mobileSmsContent, new Date());
					}
				} else if (!YHUtility.isNullorEmpty(mobileSmsPerson)) {
					mobileSms.remindByMobileSms(dbConn, mobileSmsPerson, loginUserSeqId, mobileSmsContent, new Date());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 编辑文件中的新建附件
	 * 
	 * @param dbConn
	 * @param map
	 * @throws Exception
	 */
	public Map<Object, Object> newAttrachFileLogic(Connection dbConn, Map<Object, Object> map) throws Exception {
		YHORM orm = new YHORM();
		try {
			Map<Object, Object> objMap = new HashMap<Object, Object>();
			int contentId = (Integer) map.get("contentId");
			String attachmentName = (String) map.get("attachmentName");
			String fileType = (String) map.get("fileType");
			String subject = (String) map.get("subject");
			String contentNo = (String) map.get("contentNo");
			String content = (String) map.get("content");
			String atttDesc = (String) map.get("atttDesc");
			String realPath = (String) map.get("realPath");
			String newAttachIdStr = "";
			String newAttachNameStr = "";
			SimpleDateFormat format = new SimpleDateFormat("yyMM");
			String currDate = format.format(new Date());
			YHConfidentialContent fileContent = this.getFileContentInfoById(dbConn, contentId);
			if (fileContent != null) {
				String attachId = YHUtility.null2Empty(fileContent.getAttachmentId());
				String attachName = YHUtility.null2Empty(fileContent.getAttachmentName());
				String attachmentIds = attachId;
				String attachmentNames = attachName;

				String rand = this.createFile(fileType, attachmentName, realPath);
				if (!rand.equals("0")) {
					attachmentIds = attachId + currDate + "_" + String.valueOf(rand) + ",";
					attachmentNames = attachName + attachmentName + "." + fileType + "*";
					newAttachIdStr = currDate + "_" + String.valueOf(rand);
					newAttachNameStr = attachmentName.trim() + "." + fileType;
				}
				fileContent.setSubject(subject);
				fileContent.setAttachmentId(attachmentIds);
				fileContent.setAttachmentName(attachmentNames);
				fileContent.setContentNo(contentNo);
				fileContent.setContent(content);
				fileContent.setAttachmentDesc(atttDesc);
				orm.updateSingle(dbConn, fileContent);

			}

			objMap.put("newAttachIdStr", newAttachIdStr);
			objMap.put("newAttachNameStr", newAttachNameStr);
			return objMap;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 文件报送	 * 
	 * @param dbConn
	 * @param contentIds
	 * @param person
	 * @throws Exception
	 */
	public void updateFileSendLogic(Connection dbConn, String contentIds, YHPerson person) throws Exception {
	  updateFileSendLogic(dbConn, contentIds, person, "1");
	}
	
	/**
	 * 文件报送	 * 
	 * @param dbConn
	 * @param contentIds
	 * @param person
	 * @throws Exception
	 */
	public void updateFileSendLogic(Connection dbConn, String contentIds, YHPerson person, String send) throws Exception {
	  YHORM orm = new YHORM();
	  try {
	    String[] seqIdStr = contentIds.split(",");
	    if (!YHUtility.isNullorEmpty(contentIds) && seqIdStr.length > 0) {
	      for (String seqId : seqIdStr) {
	        if (YHUtility.isNumber(seqId)) {
	          YHConfidentialContent fileContent = this.getFileContentInfoById(dbConn, Integer.parseInt(seqId));
	          if (fileContent != null) {
	            String dbFileSend = YHUtility.null2Empty(fileContent.getFileSend());
	            if (!YHUtility.isNullorEmpty(dbFileSend) && send.equals(dbFileSend.trim())) {
	              continue;
	            }
	            fileContent.setFileSend(send);
	            orm.updateSingle(dbConn, fileContent);
	          }
	        }
	      }
	    }
	  } catch (Exception e) {
	    throw e;
	  }
	}

	/**
	 * 查询文件
	 * 
	 * @param dbConn
	 * @param request
	 * @param sortIdStr
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String queryFileByIdJsonLogic(Connection dbConn, Map request, YHPerson person, Map<Object, Object> map, String filePath) throws Exception {
		String seqIdStr = (String) map.get("seqId");
		int sortId = -1;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			sortId = Integer.parseInt(seqIdStr);
		}
		try {
			boolean accessPriv = this.getAccessOrOwnerPriv(dbConn, person, sortId);
			if (!accessPriv) {
				YHPageDataList pageDataList = new YHPageDataList();
				return pageDataList.toJson();
			}
			String dbSeqIds = this.getQuerySeqId(dbConn, map, filePath); // 取出符合条件的seqId串
			String seqIdString = this.getSqlIn(dbSeqIds, "SEQ_ID"); // SEQ_ID
			// IN('141','61')
			// OR SEQ_ID
			// IN('85','82')
			String sql = "select " 
				+ " SEQ_ID" 
				+ ", SUBJECT" 
				+ ", ATTACHMENT_ID" 
				+ ", ATTACHMENT_NAME" 
				+ ", ATTACHMENT_DESC" 
				+ ", SEND_TIME"
				+ ", FILE_SEND" 
				+ ", SORT_ID" 
				+ " from oa_secret_content where " + seqIdString + " order by SEND_TIME desc";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取“访问” 或 “拥有者”权限（userId 或 owner权限）
	 * 
	 * @param dbConn
	 * @param person
	 * @param fileSort
	 * @return
	 * @throws Exception
	 */
	public boolean getAccessOrOwnerPriv(Connection dbConn, YHPerson person, int seqId) throws Exception {
		YHORM orm = new YHORM();
		boolean flag = false;
		YHSetConfidentialSortLogic setLogic = new YHSetConfidentialSortLogic();
		YHShowConfidentialSortLogic sortLogic = new YHShowConfidentialSortLogic();
		try {
			int visiPrivFlag = 0;
			int ownerPrivFlag = 0;
			YHConfidentialSort fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId);
			if (fileSort != null) {
				String userIdPrivs = setLogic.selectManagerIds(dbConn, fileSort, "USER_ID");
				String userRolePrivs = setLogic.getRoleIds(dbConn, fileSort, "USER_ID");
				String userDeptPrivs = setLogic.getDeptIds(dbConn, fileSort, "USER_ID");
				boolean userIdFlag = sortLogic.checkUserIdPriv(person.getSeqId(), userIdPrivs);
				boolean userRoleFlag = sortLogic.checkUserIdPriv(Integer.parseInt(person.getUserPriv()), userRolePrivs);
				boolean userDeptFlag = sortLogic.chekDeptIdPriv(person.getDeptId(), userDeptPrivs);
				if (userIdFlag || userRoleFlag || userDeptFlag) {
					visiPrivFlag = 1;
				}
				String ownerUserIdPrivs = setLogic.selectManagerIds(dbConn, fileSort, "OWNER");
				String ownerUserRolePrivs = setLogic.getRoleIds(dbConn, fileSort, "OWNER");
				String ownerUserDeptPrivs = setLogic.getDeptIds(dbConn, fileSort, "OWNER");
				boolean ownerUserIdFlag = sortLogic.checkUserIdPriv(person.getSeqId(), ownerUserIdPrivs);
				boolean ownerUserRoleFlag = sortLogic.checkUserIdPriv(Integer.parseInt(person.getUserPriv()), ownerUserRolePrivs);
				boolean ownerUserDeptFlag = sortLogic.chekDeptIdPriv(person.getDeptId(), ownerUserDeptPrivs);
				if (ownerUserIdFlag || ownerUserRoleFlag || ownerUserDeptFlag) {
					ownerPrivFlag = 1;
				}
				if (visiPrivFlag == 1 || ownerPrivFlag == 1) {
					flag = true;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}

	/**
	 * 获取符合条件的seqId串
	 * 
	 * @param dbConn
	 * @param map
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public String getQuerySeqId(Connection dbConn, Map<Object, Object> map, String filePath) throws Exception {

		String subject = (String) map.get("subject");
		String contentNo = (String) map.get("contentNo");
		String key1 = (String) map.get("key1");
		String key2 = (String) map.get("key2");
		String key3 = (String) map.get("key3");

		String attachmentDesc = (String) map.get("attachmentDesc");
		String attachmentName = (String) map.get("attachmentName");
		String attachmentData = (String) map.get("attachmentData");
		String sendTimeMin = (String) map.get("sendTimeMin");
		String sendTimeMax = (String) map.get("sendTimeMax");
		String seqIdStr = (String) map.get("seqId");
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}
		if (YHUtility.isNullorEmpty(subject)) {
			subject = "";
		}
		if (YHUtility.isNullorEmpty(contentNo)) {
			contentNo = "";
		}
		if (YHUtility.isNullorEmpty(key1)) {
			key1 = "";
		}
		if (YHUtility.isNullorEmpty(key2)) {
			key2 = "";
		}
		if (YHUtility.isNullorEmpty(key3)) {
			key3 = "";
		}
		if (YHUtility.isNullorEmpty(attachmentDesc)) {
			attachmentDesc = "";
		}
		if (YHUtility.isNullorEmpty(attachmentName)) {
			attachmentName = "";
		}
		if (YHUtility.isNullorEmpty(attachmentData)) {
			attachmentData = "";
		}
		if (YHUtility.isNullorEmpty(sendTimeMin)) {
			sendTimeMin = "";
		}
		if (YHUtility.isNullorEmpty(sendTimeMax)) {
			sendTimeMax = "";
		}
		StringBuffer sb = new StringBuffer();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String where_str = "";
			if (!YHUtility.isNullorEmpty(subject.trim())) {
				where_str += " and SUBJECT like '%" + YHDBUtility.escapeLike(subject) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(contentNo.trim())) {
				where_str += " and CONTENT_NO like '%" + YHDBUtility.escapeLike(contentNo) + "%'" + YHDBUtility.escapeLike();
			}

			if (!YHUtility.isNullorEmpty(attachmentDesc.trim())) {
				where_str += " and ATTACHMENT_DESC like '%" + YHDBUtility.escapeLike(attachmentDesc) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(key1.trim())) {
				where_str += " and CONTENT like '%" + YHDBUtility.escapeLike(key1) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(key2.trim())) {
				where_str += " and CONTENT like '%" + YHDBUtility.escapeLike(key2) + "%'" + YHDBUtility.escapeLike();
			}

			if (!YHUtility.isNullorEmpty(key3.trim())) {
				where_str += " and CONTENT like '%" + YHDBUtility.escapeLike(key3) + "%'" + YHDBUtility.escapeLike();
			}

			if (!YHUtility.isNullorEmpty(attachmentName.trim())) {
				where_str += " and ATTACHMENT_NAME like '%" + YHDBUtility.escapeLike(attachmentName) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(sendTimeMin.trim())) {
				String temp = YHDBUtility.getDateFilter("SEND_TIME", sendTimeMin.trim(), ">=");
				where_str += " and " + temp;
			}
			if (!YHUtility.isNullorEmpty(sendTimeMax.trim())) {
				String temp = YHDBUtility.getDateFilter("SEND_TIME", sendTimeMax.trim(), "<=");
				where_str += " and " + temp;
			}
			String query = "SELECT SEQ_ID,ATTACHMENT_ID,ATTACHMENT_NAME from oa_secret_content where SORT_ID=" + seqId + where_str;
			stmt = dbConn.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String dbAttachmentIds = YHUtility.encodeSpecial(rs.getString("ATTACHMENT_ID"));
				String dbAttachmentNames = YHUtility.encodeSpecial(rs.getString("ATTACHMENT_NAME"));
				if (!"".equals(attachmentData.trim()) && "".equals(dbAttachmentNames.trim())) {
					continue;
				}
				// 匹配文件里的内容
				if (!YHUtility.isNullorEmpty(attachmentData) && !YHUtility.isNullorEmpty(dbAttachmentNames)) {
					String[] attIdArray = dbAttachmentIds.trim().split(",");
					String[] attNameArray = dbAttachmentNames.trim().split("\\*");
					int contentValue = -1;
					for (int i = 0; i < attIdArray.length; i++) {
						String attId = this.getAttaId(attIdArray[i]);
						String attFolder = this.getFilePathFolder(attIdArray[i]);
						String newAttName = attId + "_" + attNameArray[i];
						String oldAttName = attId + "." + attNameArray[i];

						String newFilePath = filePath + "/" + attFolder + "/" + newAttName;
						String oldFilePath = filePath + "/" + attFolder + "/" + oldAttName;
						File newFile = new File(newFilePath);
						File oldFile = new File(oldFilePath);

						String fileType = "";
						String attName = attNameArray[i];
						if (attName.trim().lastIndexOf(".") != -1) {
							fileType = attName.substring(attName.trim().lastIndexOf(".")); // .doc
						}
						StringBuffer buffer = new StringBuffer();
						if (newFile.exists()) {
							if (".htm".equals(fileType.trim()) || ".html".equals(fileType.trim())) {
								buffer = YHFileUtility.loadLine2Buff(newFile.getAbsolutePath(), "GBK");
								contentValue = buffer.indexOf(attachmentData.trim());
							} else if (".txt".equals(fileType.trim())) {
								buffer = YHFileUtility.loadLine2Buff(newFile.getAbsolutePath(), "GBK");
								contentValue = buffer.indexOf(attachmentData.trim());
							}
							if (contentValue >= 0) {
								break;
							}
						} else if (oldFile.exists()) {
							if (".htm".equals(fileType.trim()) || ".html".equals(fileType.trim())) {
								buffer = YHFileUtility.loadLine2Buff(oldFile.getAbsolutePath(), "GBK");
								contentValue = buffer.indexOf(attachmentData.trim());
							} else if (".txt".equals(fileType.trim())) {
								buffer = YHFileUtility.loadLine2Buff(oldFile.getAbsolutePath(), "GBK");
								contentValue = buffer.indexOf(attachmentData.trim());
							}
							if (contentValue >= 0) {
								break;
							}
						} else {
							break;
						}
					}
					if (contentValue == -1) {
						continue;
					}
				}
				int dbSeqId = rs.getInt("SEQ_ID");
				sb.append(dbSeqId + ",");
			}
			if (sb.length() > 0) {
				sb = sb.deleteCharAt(sb.length() - 1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return sb.toString();
	}

	/**
	 * 生成符合条件的sql语句,解决in最多只支持1000个对象问题,结果如:SEQ_ID IN('141','61') OR SEQ_ID
	 * IN('85','82')
	 * 
	 * @param sqlParam
	 *          需要处理的参数的字符串格式，例如 1，2，3，4，5 参数以“，”隔开
	 * @param columnName
	 *          数据库中匹配的字段 比如，你想查看 seqId in(1,2,3)，那么columnName 为seqId
	 * @return
	 * @throws Exception
	 */
	public String getSqlIn(String sqlParam, String columnName) throws Exception {
		try {
			int buff_length = 0;
			int spIndex = 900;
			if (sqlParam == null || "".equals(sqlParam)) {
				return columnName + " in(-1)";
			}
			String[] str_arr = sqlParam.split(",");
			int width = str_arr.length;
			int arr_width = width / spIndex;
			if (width % spIndex != 0) {
				arr_width += 1;
			}
			StringBuffer buffer = new StringBuffer("");
			for (int i = 0; i < arr_width; i++) {
				buffer.append(" " + columnName + " IN(");
				for (int j = i * spIndex, k = 0; j < width && k < spIndex; j++, k++) {
					buffer.append("'" + str_arr[j] + "',");
				}
				buff_length = buffer.length();
				buffer = buffer.delete(buff_length - 1, buff_length).append(") OR");
			}
			return buffer.substring(0, buffer.length() - 2);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取未报送文件列表
	 * 
	 * @param dbConn
	 * @param request
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getFileNotSendListJson(Connection dbConn, Map request, YHPerson person, String fileSendFlag) throws Exception {
		if (YHUtility.isNullorEmpty(fileSendFlag)) {
			fileSendFlag = "";
		}
		String conditionStr = " FILE_SEND ='0' or FILE_SEND is null ";
		try {

			if ("1".equals(fileSendFlag)) {
				conditionStr = " FILE_SEND ='1' and FILE_SEND is not null ";
			}
			String sql = "select " + " SEQ_ID" + ", SUBJECT" + ", ATTACHMENT_ID" + ", ATTACHMENT_NAME" + ", SEND_TIME" + ", CONTENT_NO" + ", FILE_SEND"
					+ ", SORT_ID" + " from oa_secret_content where " + conditionStr + " order by SEND_TIME desc";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 全局搜索文件列表
	 * 
	 * @param dbConn
	 * @param request
	 * @param sortIdStr
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getGlobalFileJsonLogic(Connection dbConn, Map request, YHPerson person, Map<Object, Object> map, String filePath) throws Exception {
		try {
			String dbSeqIds = this.getGlobalFileSeqId(dbConn, person, map, filePath); // 取出符合条件的seqId串
			String seqIdString = this.getSqlIn(dbSeqIds, "SEQ_ID"); // SEQ_ID
			// IN('141','61')
			// OR SEQ_ID
			// IN('85','82')
			String sql = "select " + " SEQ_ID" + ", SORT_ID" + ", SUBJECT" + ", ATTACHMENT_ID" + ", ATTACHMENT_NAME" + ", ATTACHMENT_DESC" + ", SEND_TIME"
					+ ", FILE_SEND" + " from oa_secret_content where " + seqIdString + " order by SEND_TIME desc";
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取符合条件的seqId串
	 * 
	 * @param dbConn
	 * @param map
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public String getGlobalFileSeqId(Connection dbConn, YHPerson person, Map<Object, Object> map, String filePath) throws Exception {
		String subject = (String) map.get("subject");
		String contentNo = (String) map.get("contentNo");
		String key1 = (String) map.get("key1");
		String key2 = (String) map.get("key2");
		String key3 = (String) map.get("key3");

		String attachmentDesc = (String) map.get("attachmentDesc");
		String attachmentName = (String) map.get("attachmentName");
		String attachmentData = (String) map.get("attachmentData");
		String sendTimeMin = (String) map.get("sendTimeMin");
		String sendTimeMax = (String) map.get("sendTimeMax");

		if (YHUtility.isNullorEmpty(subject)) {
			subject = "";
		}
		if (YHUtility.isNullorEmpty(contentNo)) {
			contentNo = "";
		}
		if (YHUtility.isNullorEmpty(key1)) {
			key1 = "";
		}
		if (YHUtility.isNullorEmpty(key2)) {
			key2 = "";
		}
		if (YHUtility.isNullorEmpty(key3)) {
			key3 = "";
		}
		if (YHUtility.isNullorEmpty(attachmentDesc)) {
			attachmentDesc = "";
		}
		if (YHUtility.isNullorEmpty(attachmentName)) {
			attachmentName = "";
		}
		if (YHUtility.isNullorEmpty(attachmentData)) {
			attachmentData = "";
		}
		if (YHUtility.isNullorEmpty(sendTimeMin)) {
			sendTimeMin = "";
		}
		if (YHUtility.isNullorEmpty(sendTimeMax)) {
			sendTimeMax = "";
		}
		StringBuffer sb = new StringBuffer();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String where_str = "";
			if (!YHUtility.isNullorEmpty(subject.trim())) {
				where_str += " and SUBJECT like '%" + YHDBUtility.escapeLike(subject) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(contentNo.trim())) {
				where_str += " and CONTENT_NO like '%" + YHDBUtility.escapeLike(contentNo) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(attachmentDesc.trim())) {
				where_str += " and ATTACHMENT_DESC like '%" + YHDBUtility.escapeLike(attachmentDesc) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(key1.trim())) {
				where_str += " and CONTENT like '%" + YHDBUtility.escapeLike(key1) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(key2.trim())) {
				where_str += " and CONTENT like '%" + YHDBUtility.escapeLike(key2) + "%'" + YHDBUtility.escapeLike();
			}

			if (!YHUtility.isNullorEmpty(key3.trim())) {
				where_str += " and CONTENT like '%" + YHDBUtility.escapeLike(key3) + "%'" + YHDBUtility.escapeLike();
			}

			if (!YHUtility.isNullorEmpty(attachmentName.trim())) {
				where_str += " and ATTACHMENT_NAME like '%" + YHDBUtility.escapeLike(attachmentName) + "%'" + YHDBUtility.escapeLike();
			}
			if (!YHUtility.isNullorEmpty(sendTimeMin.trim())) {
				String temp = YHDBUtility.getDateFilter("SEND_TIME", sendTimeMin.trim(), ">=");
				where_str += " and " + temp;
			}
			if (!YHUtility.isNullorEmpty(sendTimeMax.trim())) {
				String temp = YHDBUtility.getDateFilter("SEND_TIME", sendTimeMax.trim(), "<=");
				where_str += " and " + temp;
			}
			String query = "SELECT SEQ_ID,SORT_ID,ATTACHMENT_ID,ATTACHMENT_NAME from oa_secret_content where 1=1 " + where_str;

			stmt = dbConn.prepareStatement(query);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int dbSortId = rs.getInt("SORT_ID");
				boolean accessPriv = false;
				// 判断访问权限
				if (dbSortId != 0) {
					accessPriv = this.getAccessPriv(dbConn, person, dbSortId);
				}
				if (!accessPriv) {
					continue;
				}
				String dbAttachmentIds = YHUtility.encodeSpecial(rs.getString("ATTACHMENT_ID"));
				String dbAttachmentNames = YHUtility.encodeSpecial(rs.getString("ATTACHMENT_NAME"));
				if (!"".equals(attachmentData.trim()) && "".equals(dbAttachmentNames.trim())) {
					continue;
				}
				// 匹配文件里的内容
				if (!YHUtility.isNullorEmpty(attachmentData) && !YHUtility.isNullorEmpty(dbAttachmentNames)) {
					String[] attIdArray = dbAttachmentIds.trim().split(",");
					String[] attNameArray = dbAttachmentNames.trim().split("\\*");
					int contentValue = -1;
					for (int i = 0; i < attIdArray.length; i++) {
						String attId = this.getAttaId(attIdArray[i]);
						String attFolder = this.getFilePathFolder(attIdArray[i]);
						String newAttName = attId + "_" + attNameArray[i];
						String oldAttName = attId + "." + attNameArray[i];

						String newFilePath = filePath + "/" + attFolder + "/" + newAttName;
						String oldFilePath = filePath + "/" + attFolder + "/" + oldAttName;
						File newFile = new File(newFilePath);
						File oldFile = new File(oldFilePath);

						String fileType = "";
						String attName = attNameArray[i];
						if (attName.trim().lastIndexOf(".") != -1) {
							fileType = attName.substring(attName.trim().lastIndexOf(".")); // .doc
						}
						StringBuffer buffer = new StringBuffer();
						if (newFile.exists()) {
							if (".htm".equals(fileType.trim()) || ".html".equals(fileType.trim())) {
								buffer = YHFileUtility.loadLine2Buff(newFile.getAbsolutePath(), "GBK");
								contentValue = buffer.indexOf(attachmentData.trim());
							} else if (".txt".equals(fileType.trim())) {
								buffer = YHFileUtility.loadLine2Buff(newFile.getAbsolutePath(), "GBK");
								contentValue = buffer.indexOf(attachmentData.trim());
							}
							if (contentValue >= 0) {
								break;
							}
						} else if (oldFile.exists()) {
							if (".htm".equals(fileType.trim()) || ".html".equals(fileType.trim())) {
								buffer = YHFileUtility.loadLine2Buff(oldFile.getAbsolutePath(), "GBK");
								contentValue = buffer.indexOf(attachmentData.trim());
							} else if (".txt".equals(fileType.trim())) {
								buffer = YHFileUtility.loadLine2Buff(oldFile.getAbsolutePath(), "GBK");
								contentValue = buffer.indexOf(attachmentData.trim());
							}
							if (contentValue >= 0) {
								break;
							}
						} else {
							break;
						}
					}
					if (contentValue == -1) {
						continue;
					}
				}
				int dbSeqId = rs.getInt("SEQ_ID");
				sb.append(dbSeqId + ",");
			}
			if (sb.length() > 0) {
				sb = sb.deleteCharAt(sb.length() - 1);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(stmt, rs, log);
		}
		return sb.toString();
	}

	/**
	 * 获取“访问”权限（userId权限）
	 * 
	 * @param dbConn
	 * @param person
	 * @param fileSort
	 * @return
	 * @throws Exception
	 */
	public boolean getAccessPriv(Connection dbConn, YHPerson person, int seqId) throws Exception {
		YHORM orm = new YHORM();
		boolean flag = false;
		YHSetConfidentialSortLogic setLogic = new YHSetConfidentialSortLogic();
		YHShowConfidentialSortLogic sortLogic = new YHShowConfidentialSortLogic();
		try {
			YHConfidentialSort fileSort = (YHConfidentialSort) orm.loadObjSingle(dbConn, YHConfidentialSort.class, seqId);
			if (fileSort != null) {
				String userIdPrivs = setLogic.selectManagerIds(dbConn, fileSort, "USER_ID");
				String userRolePrivs = setLogic.getRoleIds(dbConn, fileSort, "USER_ID");
				String userDeptPrivs = setLogic.getDeptIds(dbConn, fileSort, "USER_ID");
				boolean userIdFlag = sortLogic.checkUserIdPriv(person.getSeqId(), userIdPrivs);
				boolean userRoleFlag = sortLogic.checkUserIdPriv(Integer.parseInt(person.getUserPriv()), userRolePrivs);
				boolean userDeptFlag = sortLogic.chekDeptIdPriv(person.getDeptId(), userDeptPrivs);
				if (userIdFlag || userRoleFlag || userDeptFlag) {
					flag = true;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return flag;
	}
	/**
	 * 获取创建人名称
	 * @param dbConn
	 * @param createrId
	 * @return
	 * @throws Exception 
	 */
	public String getCreaterNameByIdLogic(Connection dbConn,String createrId) throws Exception{
		if (YHUtility.isNullorEmpty(createrId)) {
			createrId = "";
		}
		try {
			String createtName = this.getPersonNamesByIds(dbConn, createrId);
			String data = "{createtName:\"" + YHUtility.encodeSpecial(createtName) + "\"}";
			return data;
		} catch (Exception e) {
			throw e;
		}
	}

}
