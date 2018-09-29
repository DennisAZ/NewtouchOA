package yh.core.funcs.personfolder.logic;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.personfolder.data.YHFileContent;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;

public class YHPersonFileContentLogic {
	private static Logger log = Logger.getLogger(YHPersonFileContentLogic.class);
	public static String COPYPATH = File.separator+"core"+File.separator+"funcs"+File.separator+"filefolder"+File.separator+"fileUtil";

	public List<YHFileContent> getFileContentsInfo(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHFileContent.class, map);

	}

	public List<YHFileContent> getFileContentsByFilters(Connection dbConn, String[] filters) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHFileContent.class, filters);

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
	 * 文件批量上传
	 * 
	 * @param dbConn
	 * @param content
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String uploadFileLogic(Connection dbConn, YHFileContent content, YHFileUploadForm fileForm,YHPerson loginUser,String seqId,String filePath) throws Exception {
		// 获取登录用户信息
		int loginUserSeqId = loginUser.getSeqId();
		YHORM orm = new YHORM();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(date);
		int sortId = 0;
		if (!YHUtility.isNullorEmpty(seqId)) {
			sortId = Integer.parseInt(seqId);
		}

		Iterator<String> keysIte = fileForm.iterateFileFields();
		while (keysIte.hasNext()) {
			String fieldName = keysIte.next();
			String fileName = fileForm.getFileName(fieldName);
			if (YHUtility.isNullorEmpty(fileName)) {
				continue;
			}
			String nameTitle = "";
			String newSubject = "";
			if (fileName.lastIndexOf(".") != -1) {
				nameTitle = fileName.substring(0, fileName.lastIndexOf("."));
			}
			boolean haveFile = this.isExistFile(dbConn, sortId, nameTitle);
			if (haveFile) {
				StringBuffer buffer = new StringBuffer();
				this.copyExistFile(dbConn, buffer, sortId, nameTitle);
				newSubject = buffer.toString().trim();
			}else {
				newSubject = nameTitle;
			}
			YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();
			content.setSortId(sortId);
			content.setUserId(String.valueOf(loginUserSeqId));
			content.setCreater(String.valueOf(loginUserSeqId));
			content.setSendTime(YHUtility.parseTimeStamp());
			content.setAttachmentName(fileName.trim() + "*");
			content.setSubject(newSubject);
			String rand = emut.getRandom();
			fileName = rand + "_" + fileName;
			fileForm.saveFile(fieldName, filePath  + File.separator+  fileName);
			content.setAttachmentId(currDate + "_" + String.valueOf(rand) + ",");
		}
		orm.saveSingle(dbConn, content);
		return "";
	}

	public YHFileContent getFileContentInfoById(Connection dbConn, int contentId) throws Exception {
		YHORM orm = new YHORM();
		return (YHFileContent) orm.loadObjSingle(dbConn, YHFileContent.class, contentId);
	}

	public YHFileContent getFileInfoByMap(Connection dbConn, Map map) throws NumberFormatException, Exception {
		YHORM orm = new YHORM();
		return (YHFileContent) orm.loadObjSingle(dbConn, YHFileContent.class, map);
	}

	public String createFile(String fileType, String fileName, String webrootPath) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(new Date());
		String separator = File.separator;
		String filePath = YHSysProps.getAttachPath() + separator + "file_folder" + separator + currDate + separator;

		YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();
		String rand = emut.getRandom();
		String newFileName = rand + "_" + fileName + "." + fileType;
		String tmp = filePath + newFileName;

		String type = fileType.trim();
		if ("xls".equals(type)) {
			String srcFile = webrootPath + this.COPYPATH + File.separator+ "copy.xls";
			YHFileUtility.copyFile(srcFile, tmp);
		} else if ("ppt".equals(type)) {
			String srcFile = webrootPath + this.COPYPATH  + File.separator+ "copy.ppt";
			YHFileUtility.copyFile(srcFile, tmp);
		} else if ("doc".equals(type)) {
      String srcFile = webrootPath + this.COPYPATH  + File.separator+ "copy.doc";
      YHFileUtility.copyFile(srcFile, tmp);
    } else {
			File file = new File(tmp);
			file.createNewFile();
		}

		return rand;

	}

	// public String createType(String type, String fileName) throws
	// NoSuchAlgorithmException {
	// // Date date=new Date();
	// SimpleDateFormat format = new SimpleDateFormat("yyMM");
	// String currDate = format.format(new Date());
	// String separator = File.separator;
	// // String filePath = "d:" + separator + "tmp" + separator +
	// // "upload"+separator+"file_folder"+ separator+currDate;
	// String filePath = YHSysProps.getAttachPath() + separator + "file_folder" +
	// separator + currDate;
	//
	// YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();
	// String rand = emut.getRandom();
	// fileName = rand + "_" + fileName;
	//
	// File f = new File(filePath, fileName + "." + type);
	// // boolean flag = false;
	// if (f.exists()) {
	// System.out.println("文件名：" + f.getAbsolutePath());
	// System.out.println("文件大小：" + f.length());
	// } else {
	// f.getParentFile().mkdirs();
	// System.out.println("f.getParentFile():====" + f.getParentFile() +
	// "    f.getParentFile().mkdirs()======" + f.getParentFile().mkdirs());
	// try {
	// f.createNewFile();
	// // flag = true;
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return rand;
	// }

	public void updateSingleObj(Connection dbConn, YHFileContent fileContent) throws Exception {
		YHORM orm = new YHORM();
		orm.updateSingle(dbConn, fileContent);
	}

	public boolean updateSingle(Connection dbConn, YHFileContent fileContent) {
		boolean flag = false;

		YHORM orm = new YHORM();
		try {
			orm.updateSingle(dbConn, fileContent);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public void saveSingleObj(Connection dbConn, YHFileContent fileContent) throws Exception {
		YHORM orm = new YHORM();
		orm.saveSingle(dbConn, fileContent);
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
	public void delFile(Connection dbConn, String seqIdStrs, String filePath) throws NumberFormatException, Exception {
		YHORM orm = new YHORM();
		String[] seqIdStr = seqIdStrs.split(",");
		if (!"".equals(seqIdStrs) && seqIdStrs.split(",").length > 0) {
			// 遍历要选择删除的附件id串
			for (String seqId : seqIdStr) {
				YHFileContent fileContent = this.getFileContentInfoById(dbConn, Integer.parseInt(seqId));
				String attachmentId = YHUtility.null2Empty(fileContent.getAttachmentId());
				String attachmentName = YHUtility.null2Empty(fileContent.getAttachmentName());
				String[] attIdArray = {};
				String[] attNameArray = {};
				if (!"".equals(attachmentId.trim()) && attachmentId != null && attachmentName != null) {
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
								YHFileUtility.deleteAll(file.getAbsoluteFile());
							} else if (oldFile.exists()) {
								YHFileUtility.deleteAll(oldFile.getAbsoluteFile());

							}

						}
					}

				}

				// 删除数据库信息
				YHFileContent delContent = new YHFileContent();
				delContent.setSeqId(fileContent.getSeqId());
				orm.deleteSingle(dbConn, delContent);

			}
		}
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
	// public String getAttaId(String keyId) {
	// String attaId = "";
	// if (keyId != null && !"".equals(keyId)) {
	// String[] ids = keyId.split("_");
	//
	// if (ids.length > 0) {
	// attaId = ids[1];
	// }
	// }
	// return attaId;
	// }

	/**
	 * 得到附件的Id 兼老数据
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
	 * 复制文件操作
	 * 
	 * @param dbConn
	 * @param seqIdStrs
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	public void copyFile(Connection dbConn, String seqIdStrs, String sortId, String filePath, int loginUserSeqId) throws NumberFormatException,
			Exception {
		// getFileContentInfoById(dbConn,);
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(date);
		YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();

		YHORM orm = new YHORM();
		// String randFlag = "";
		// String newAttName = "";
		String[] seqIdStr = seqIdStrs.split(",");
		if (!"".equals(seqIdStrs) && seqIdStrs.split(",").length > 0) {
			// 遍历要选择附件id串			for (String seqId : seqIdStr) {
				String randFlag = "";
				String newAttName = "";
				boolean isHave = false;
				YHFileContent fileContent = this.getFileContentInfoById(dbConn, Integer.parseInt(seqId));
				if (fileContent!=null) {
					String subject = YHUtility.null2Empty(fileContent.getSubject());
					boolean haveFile = this.isExistFile(dbConn, Integer.parseInt(sortId), subject);
					if (haveFile) {
						StringBuffer buffer = new StringBuffer();
						this.copyExistFile(dbConn, buffer, Integer.parseInt(sortId), subject);
						String newSubject = buffer.toString().trim();
						fileContent.setSubject(newSubject);
					}
					
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

								String oldFileNameValue = attaIdStr + "." + keyValue;

								File file = new File(filePath + File.separator + fileFolder + File.separator + fileNameValue);
								File oldFile = new File(filePath + File.separator + fileFolder + File.separator + oldFileNameValue);

								if (file.exists()) {
									YHFileUtility.copyFile(file.getAbsolutePath(), filePath + File.separator + currDate + File.separator + newAttaName);
									randFlag += currDate + "_" + rand + ",";
									newAttName += keyValue + "*";
									isHave = true;
								} else if (oldFile.exists()) {
									YHFileUtility.copyFile(oldFile.getAbsolutePath(), filePath + File.separator + currDate + File.separator + newAttaName);
									randFlag += currDate + "_" + rand + ",";
									newAttName += keyValue + "*";
									isHave = true;
								}
							}
						}
					}

					if (isHave) {
						// 保存到数据库
						fileContent.setSortId(Integer.parseInt(sortId));
						fileContent.setUserId(String.valueOf(loginUserSeqId));
						fileContent.setCreater(String.valueOf(loginUserSeqId));
						fileContent.setSendTime(YHUtility.parseTimeStamp());
						fileContent.setAttachmentId(randFlag.trim());
						fileContent.setAttachmentName(newAttName.trim());
						orm.saveSingle(dbConn, fileContent);
					} else {
						fileContent.setSortId(Integer.parseInt(sortId));
						fileContent.setSendTime(YHUtility.parseTimeStamp());
						orm.saveSingle(dbConn, fileContent);
					}
					
					
					
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
	public void cutFile(Connection dbConn, String seqIdStrs, String sortId, String filePath, int loginUserSeqId) throws NumberFormatException,
			Exception {
		// YHFileUtility.xcopyFile(srcPath + fileName, desPath + fileName);
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(date);
		YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();
		boolean isHave = false;
		YHORM orm = new YHORM();
		String[] seqIdStr = seqIdStrs.split(",");
		if (!"".equals(seqIdStrs) && seqIdStrs.split(",").length > 0) {
			// 遍历选择的附件Id串			for (String seqId : seqIdStr) {
				String randFlag = "";
				String newAttName = "";
				YHFileContent fileContent = this.getFileContentInfoById(dbConn, Integer.parseInt(seqId));
				if (fileContent!=null) {
					String subject = YHUtility.null2Empty(fileContent.getSubject());
					boolean haveFile = this.isExistFile(dbConn, Integer.parseInt(sortId), subject);
					if (haveFile) {
						StringBuffer buffer = new StringBuffer();
						this.copyExistFile(dbConn, buffer, Integer.parseInt(sortId), subject);
						String newSubject = buffer.toString().trim();
						fileContent.setSubject(newSubject);
					}
					
					String attachmentId = YHUtility.null2Empty(fileContent.getAttachmentId());
					String attachmentName = YHUtility.null2Empty(fileContent.getAttachmentName());
					YHFileContent delContent = new YHFileContent();
					String[] attIdArray = {};
					String[] attNameArray = {};
					if (attachmentId != null && attachmentName != null) {
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

								String oldNaveValue = attaIdStr + "." + keyValue;

								File file = new File(filePath + File.separator + fileFolder + File.separator + fileNameValue);
								File oldFile = new File(filePath + File.separator + fileFolder + File.separator + oldNaveValue);

								if (file.exists()) {
									YHFileUtility.xcopyFile(file.getAbsolutePath(), filePath + File.separator + currDate + File.separator + newAttaName);

									randFlag += currDate + "_" + rand + ",";
									newAttName += keyValue + "*";
									isHave = true;
								} else if (oldFile.exists()) {
									YHFileUtility.xcopyFile(oldFile.getAbsolutePath(), filePath + File.separator + currDate + File.separator + newAttaName);

									randFlag += currDate + "_" + rand + ",";
									newAttName += keyValue + "*";
									isHave = true;
								}

								// String[] fileList = file.list();
								// // 遍历文件目录下的文件
								// for (String fileStr : fileList) {
								// if (fileStr.equals(fileNameValue)) {
								// YHFileUtility.xcopyFile(filePath + File.separator + fileFolder
								// + File.separator + fileNameValue, filePath + File.separator
								// + currDate + File.separator + newAttaName);
								//
								// randFlag += currDate + "_" + rand + ",";
								// newAttName += keyValue + "*";
								// isHave = true;
								// break;
								// }
								// }
							}
						}
					}
					if (isHave) {
						delContent.setSeqId(fileContent.getSeqId());
						// 删除旧信息

						orm.deleteSingle(dbConn, delContent);
						// 插入新信息

						// fileContent.setSortId(Integer.parseInt(sortId));
						// fileContent.setSendTime(YHUtility.parseTimeStamp());
						// fileContent.setAttachmentId(currDate + "_" + String.valueOf(rand) +
						// ",");
						fileContent.setSortId(Integer.parseInt(sortId));
						fileContent.setUserId(String.valueOf(loginUserSeqId));
						fileContent.setCreater(String.valueOf(loginUserSeqId));
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
	 * 获取最大的SeqId值
	 * 
	 * 
	 * @param dbConn
	 * @return
	 */
	public YHFileContent getMaxSeqId(Connection dbConn) {
		// String sql="select MAX(SEQ_ID) from file_sort";
		String sql = "select SEQ_ID from oa_file_content where SEQ_ID=(select MAX(SEQ_ID) from oa_file_content ) ";
		YHFileContent content = null;
		int seqId = 0;
		// String sortName = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = dbConn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				content = new YHFileContent();
				seqId = rs.getInt("SEQ_ID");
				content.setSeqId(seqId);
//				content.setSortId(rs.getInt("SORT_ID"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			YHDBUtility.close(ps, rs, log);
		}
		return content;
	}

	/**
	 * 根据seqid串返回一个名字串
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public String getNameBySeqIdStr(String ids, Connection conn) throws Exception, Exception {
		String names = "";
		if (ids != null && !"".equals(ids.trim())) {
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
				YHDBUtility.close(stm, rs, null);
			}
		}
		if (names.endsWith(",")) {
			names = names.substring(0, names.length() - 1);
		}
		return names;
	}

	/**
	 * 得到该文件的文件夹名 兼老数据
	 * 
	 * @param key
	 * @return
	 */
	public String getAttFolderName(String key) {
		String folder = "";
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(date);

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

	public boolean transferFolder(Connection dbConn, int loginUserSeqId, int seqId, String attachId, String attachName, String subject,
			String filePath, String folderPath) throws Exception {
		boolean flag = false;
		YHORM orm = new YHORM();
		String fileFolder = filePath + File.separator + this.getAttFolderName(attachId);
		String fileName = this.getAttaId(attachId) + "_" + attachName;
		String oldFileName = this.getAttaId(attachId) + "." + attachName;

		YHFileContent fileContent = new YHFileContent();

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyMM");
		String currDate = format.format(date);
		YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();
		String rand;
		try {
			rand = emut.getRandom();
			String newAttaName = rand + "_" + attachName;

			File file = new File(fileFolder.trim() + "/" + fileName);
			File oldFile = new File(fileFolder.trim() + "/" + oldFileName);

			String subjectStr = "文件转存";
			String newSubject = "";
			if (!YHUtility.isNullorEmpty(subject)) {
				subjectStr = subject;
			}
			boolean haveFile = this.isExistFile(dbConn, seqId, subjectStr);
			if (haveFile) {
				StringBuffer buffer = new StringBuffer();
				this.copyExistFile(dbConn, buffer, seqId, subjectStr);
				newSubject = buffer.toString().trim();
			}
			if (file.exists()) {
				YHFileUtility.copyFile(file.getAbsolutePath(), folderPath + File.separator + currDate + File.separator + newAttaName);
				fileContent.setSortId(seqId);
				fileContent.setSubject(newSubject);
				fileContent.setSendTime(YHUtility.parseTimeStamp());
				fileContent.setAttachmentId(currDate + "_" + rand + ",");
				fileContent.setAttachmentName(attachName + "*");
				fileContent.setUserId(String.valueOf(loginUserSeqId));
				orm.saveSingle(dbConn, fileContent);
				flag = true;
			} else if (oldFile.exists()) {
				YHFileUtility.copyFile(oldFile.getAbsolutePath(), folderPath + File.separator + currDate + File.separator + newAttaName);
				fileContent.setSortId(seqId);
				fileContent.setSubject(newSubject);
				fileContent.setSendTime(YHUtility.parseTimeStamp());
				fileContent.setAttachmentId(currDate + "_" + rand + ",");
				fileContent.setAttachmentName(attachName + "*");
				fileContent.setUserId(String.valueOf(loginUserSeqId));
				orm.saveSingle(dbConn, fileContent);
				flag = true;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 判断文件名是否已存在
	 * 
	 * @param dbConn
	 * @param subjectName
	 * @return
	 * @throws Exception
	 */
	public String checkSubjectName(Connection dbConn, int loginUserSeqId, int seqId, int subContentId, String subjectName) throws Exception {
		String data = "";
		boolean isHave = false;
		int isHaveFlag = 0;

		Map map = new HashMap();
		map.put("SORT_ID", seqId);
		try {

			if (seqId == 0) {
				map.put("USER_ID", String.valueOf(loginUserSeqId));
				List<YHFileContent> contentList = this.getFileContentsInfo(dbConn, map);
				// contentList.contains(o)

				if (subContentId != 0) {
					if (contentList != null && contentList.size() > 0) {
						for (YHFileContent content : contentList) {
							String subject = YHUtility.null2Empty(content.getSubject());
							int contentId = content.getSeqId();
							if (subContentId != contentId) {
								if (subjectName.trim().equals(subject.trim())) {
									isHave = true;
									break;
								}
							}

						}
					}

				} else {
					if (contentList != null && contentList.size() > 0) {
						for (YHFileContent content : contentList) {
							String subject = YHUtility.null2Empty(content.getSubject());
							if (subjectName.trim().equals(subject.trim())) {
								isHave = true;
								break;
							}
						}
					}

				}

			} else {
				List<YHFileContent> contentList = this.getFileContentsInfo(dbConn, map);

				if (subContentId != 0) {
					if (contentList != null && contentList.size() > 0) {
						for (YHFileContent content : contentList) {
							String subject = YHUtility.null2Empty(content.getSubject());
							int contentId = content.getSeqId();
							if (subContentId != contentId) {
								if (subjectName.trim().equals(subject.trim())) {
									isHave = true;
									break;
								}
							}

						}
					}

				} else {

					if (contentList != null && contentList.size() > 0) {
						for (YHFileContent content : contentList) {
							String subject = YHUtility.null2Empty(content.getSubject());
							if (subjectName.trim().equals(subject.trim())) {
								isHave = true;
								break;
							}
						}
					}

				}

			}
			if (isHave) {
				isHaveFlag = 1;
			}
			data = "{isHaveFlag:\"" + isHaveFlag + "\" }";

		} catch (Exception e) {
			throw e;
		}
		return data;
	}

	/**
	 * 判断编辑文件名是否已存在
	 * 
	 * @param dbConn
	 * @param subjectName
	 * @return
	 * @throws Exception
	 */
	public String checkEditSubjectName(Connection dbConn, int loginUserSeqId, int seqId, int subContentId, String subjectName) throws Exception {
		String data = "";
		boolean isHave = false;
		int isHaveFlag = 0;

		Map map = new HashMap();
		map.put("SORT_ID", seqId);
		try {
			if (seqId == 0) {
				map.put("USER_ID", String.valueOf(loginUserSeqId));
			}

			List<YHFileContent> contentList = this.getFileContentsInfo(dbConn, map);
			// contentList.contains(o)
			if (contentList != null && contentList.size() > 0) {
				for (YHFileContent content : contentList) {
					String subject = YHUtility.null2Empty(content.getSubject());
					int contentId = content.getSeqId();
					if (subContentId != contentId) {
						if (subjectName.trim().equals(subject.trim())) {
							isHave = true;
							break;
						}
					}

				}
			}

			if (isHave) {
				isHaveFlag = 1;
			}
			data = "{isHaveFlag:\"" + isHaveFlag + "\" }";

		} catch (Exception e) {
			throw e;
		}
		return data;
	}

	public String isHaveFileInDisk(String attIdStr, String attNameStr) throws Exception {

		int isHaveFile = 0;
		String filePath = "";
		String oldFilePath = "";
		try {

			if (!"".equals(attIdStr.trim()) && !"".equals(attNameStr.trim())) {

				String currDate = this.getFilePathFolder(attIdStr);

				String fileId = this.getAttaId(attIdStr);
				// String fileName = logic.getAttachName(fileId, attNameStr);

				String fileName = fileId + "_" + attNameStr;
				String oldFileName = fileId + "." + attNameStr;

				filePath = YHSysProps.getAttachPath() + File.separator + "file_folder" + File.separator + currDate + File.separator + fileName; // YHSysProps.getAttachPath()得到
				oldFilePath = YHSysProps.getAttachPath() + File.separator + "file_folder" + File.separator + currDate + File.separator + oldFileName; // YHSysProps.getAttachPath()得到
			}

			File file = new File(filePath);
			File oldFile = new File(oldFilePath);

			if (file.exists()) {
				isHaveFile = 1;
			} else if (oldFile.exists()) {
				isHaveFile = 1;
			}

			String data = "{isHaveFile:\"" + isHaveFile + "\"}";

			return data;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 获取附件的id和name
	 * 
	 * @param dbConn
	 * @param contentStrs
	 * @return
	 * @throws Exception
	 */
	public String getDownFileInfo(Connection dbConn, String contentStrs) throws Exception {
		String data = "";
		String attIdArry = "";
		String attNamedArry = "";
		int counter = 0;
		StringBuffer buffer = new StringBuffer();
		try {
			String[] seqIdStr = contentStrs.split(",");
			if (!"".equals(contentStrs) && contentStrs.split(",").length > 0) {
				for (String seqId : seqIdStr) {
					YHFileContent fileContent = this.getFileContentInfoById(dbConn, Integer.parseInt(seqId));
					String attachmentId = YHUtility.null2Empty(fileContent.getAttachmentId());
					String attachmentName = YHUtility.null2Empty(fileContent.getAttachmentName());

					if (!"".equals(attachmentId.trim()) && !"".equals(attachmentName.trim())) {
						attIdArry += attachmentId;
						attNamedArry += attachmentName;
						// counter++;
					}

				}

			}

			String[] counterArry = attIdArry.split(",");
			if (counterArry != null && counterArry.length != 0) {

				for (int i = 0; i < counterArry.length; i++) {
					counter++;
					if (counter >= 3) {
						break;
					}
				}

			}

			data = "{attIdArry:\"" + attIdArry + "\",attNamedArry:\"" + attNamedArry + "\",counter:" + counter + "}";

		} catch (Exception e) {
			throw e;
		}

		return data;
	}

	/**
	 * 重命名文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean reNameOndisk(String oldNameStr, String attachId, String renameStr, String folderPath) throws Exception {
		boolean returnFlag = false;

		String fileFolder = this.getFilePathFolder(attachId); // 1006
		String filePath = folderPath + "/" + fileFolder;

		String attIdStr = this.getAttaId(attachId); // f40b24778d538764c902bbd547aa7802

		String oldName = attIdStr + "_" + oldNameStr; // f40b24778d538764c902bbd547aa7802_公告通知状态显示逻辑.docx
		String newName = attIdStr + "_" + renameStr; // f40b24778d538764c902bbd547aa7802_公告cc.docx

		String oldName2 = attIdStr + "." + oldNameStr;
		String newName2 = attIdStr + "." + renameStr;

		try {
			if (filePath != null && !"".equals(filePath.trim())) {

				File oldFile = new File(filePath + "/" + oldName);
				File newFile = new File(filePath + "/" + newName);

				File oldFile2 = new File(filePath + "/" + oldName2);
				File newFile2 = new File(filePath + "/" + newName2);

				if (!newFile.exists()) {
					if (oldFile.renameTo(newFile)) {
						returnFlag = true;

					}
				} else if (!newFile2.exists()) {
					if (oldFile2.renameTo(newFile2)) {
						returnFlag = true;
					}
				}

			}

		} catch (Exception e) {
			throw e;
		}

		return returnFlag;
	}

	public String updateAttachName(Connection dbConn, int contentId, String attachName, String attachId, String renameStr, String folderPath)
			throws Exception {
		YHORM orm = new YHORM();
		try {
			YHFileContent content = this.getFileContentInfoById(dbConn, contentId);
			String attIdDb = "";
			String attNameDb = "";
			if (content != null) {
				attIdDb = content.getAttachmentId();
				attNameDb = content.getAttachmentName();
			}
			String[] attIdArry = attIdDb.split(",");
			String[] attNameArry = attNameDb.split("\\*");
			String updateAttId = "";
			String updateAttName = "";
			if (attIdArry != null && attIdArry.length > 0) {
				for (int i = 0; i < attIdArry.length; i++) {
					String attIdTemp = attIdArry[i];
					if (attIdTemp.equals(attachId.trim())) {
						continue;
					}
					updateAttId += attIdTemp + ",";
					updateAttName += attNameArry[i] + "*";
				}
			}

			updateAttId = updateAttId + attachId + ",";
			updateAttName = updateAttName + renameStr + "*";

			content.setAttachmentId(updateAttId.trim());
			content.setAttachmentName(updateAttName.trim());
			orm.updateSingle(dbConn, content);
		} catch (Exception e) {
			throw e;
		}
		return "";
	}
	/**
	 * 判断库是否已有文件
	 * 
	 * @param dbConn
	 * @param sortId
	 * @param subject
	 * @return
	 * @throws Exception
	 */
	public boolean isExistFile(Connection dbConn, int sortId, String subject) throws Exception {
		boolean flag = false;
		int counter = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select count(SEQ_ID) from oa_file_content where SORT_ID = ? and SUBJECT=?";
		try {
			stmt = dbConn.prepareStatement(sql);
			stmt.setInt(1, sortId);
			stmt.setString(2, subject);
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
	 * 文件夹里文件已存在的处理方法
	 * 
	 * @param dbConn
	 * @param buffer
	 * @param sortId
	 * @param subject
	 * @throws Exception
	 */
	public void copyExistFile(Connection dbConn, StringBuffer buffer, int sortId, String subject) throws Exception {
		
	  try {
      String temp = subject + " - 复件";
      String subjectSuffix = temp;
      int repeat = 1;
      while (this.isExistFile(dbConn, sortId, subjectSuffix)) {
        repeat++;
        subjectSuffix = temp + "(" + repeat + ")";
      }
      buffer.append(subjectSuffix);
    } catch (Exception e) {
      throw e;
    }
	}
	
	

}
