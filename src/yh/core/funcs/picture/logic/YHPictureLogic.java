package yh.core.funcs.picture.logic;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.picture.act.YHImageUtil;
import yh.core.funcs.picture.data.YHPicture;
import yh.core.funcs.portal.act.YHPublicPortAct;
import yh.core.funcs.portal.util.YHPortalProducer;
import yh.core.funcs.portal.util.rules.YHImgRule;
import yh.core.funcs.system.filefolder.logic.YHFileSortLogic;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;

public class YHPictureLogic {

	public List<YHPicture> getPicFolderInfo(Connection dbConn) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHPicture.class, new HashMap());
	}

	public YHPicture getPicFolderInfoById(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		return (YHPicture) orm.loadObjSingle(dbConn, YHPicture.class, map);

	}

	public YHPicture getPicInfoById(Connection dbConn, int seqId) throws Exception {
		YHORM orm = new YHORM();
		return (YHPicture) orm.loadObjSingle(dbConn, YHPicture.class, seqId);

	}

	/**
	 *创建缓存缩略图片
	 * 
	 * @param picture
	 * @throws IOException
	 */
	public boolean createCache(String picPath, String nameStr) throws Exception {
		YHImageUtil imageUtil = new YHImageUtil();
		boolean flag = false;
		String filePath = picPath + File.separator;
		String fileName = nameStr;
		String picName = filePath + fileName;

		String newPath = picPath + File.separator + "tdoa_cache" + File.separator;
		File newFilePath = new File(newPath);
		if (!newFilePath.exists()) {
			newFilePath.mkdir();
		}

		if (picName != null) {
			File file = new File(picName);
			if (file.isFile()) {
				String cachPath = newPath + nameStr.trim();
				File cachFile = new File(cachPath.trim());
				if (cachFile.exists()) {
					return flag;
				} else {
					flag = imageUtil.saveImageAsImage(picName, cachFile.toString());
				}
			}
		}
		return flag;
	}

	/**
	 * 返回文件大小
	 * 
	 * @param size
	 * @return
	 */
	public String transformSize(long size) {
		DecimalFormat df = new DecimalFormat("#.0");

		String result = new String();
		if (size < 1024 && size > 0) {
			result = String.valueOf(size);
			result += "B";
		} else if (size > 1024 && size < 1024 * 1024) {
			// result = String.valueOf((float)size / 1024);
			result = df.format((double) size / 1024);
			result += "K";
		} else if (size > 1024 * 1024 && size < 1024 * 1024 * 1024) {
			// result = String.valueOf(size / 1024*1024);
			result = df.format((double) size / (1024 * 1024));
			result += "M";
		} else if (size > 1024 * 1024 * 1024) {
			// result = String.valueOf(size / 1024*1024*1024);
			result = df.format((double) size / (1024 * 1024 * 1024));
			result += "G";
		} else {
			result = "0";
		}
		return result;
	}

	/**
	 *获取上传与管理权限
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param map
	 * @param loginUserSeqId
	 * @param loginUserDeptId
	 * @param loginUserRoleId
	 * @param pathId
	 * @return
	 * @throws Exception
	 */
	public String getPrivate(Connection dbConn, Map map, int loginUserSeqId, int loginUserDeptId, String loginUserRoleId, String pathId)
			throws Exception {
		YHPictureLogic logic = new YHPictureLogic();
		StringBuffer sb = new StringBuffer();
		YHPicture picture = new YHPicture();
		int uploadPrivFlag = 0;
		int managePrivFlag = 0;
		int rootDir = 0;
		YHORM orm = new YHORM();
		picture = (YHPicture) orm.loadObjSingle(dbConn, YHPicture.class, map);
		String[] action = new String[] { "UPLOAD_ID", "MANAGE_ID" };

		if ("".equals(pathId.trim())) {
			rootDir = 1;
		}

		if (picture != null) {
			sb.append("[");
			for (int i = 0; i < action.length; i++) {
				if ("UPLOAD_ID".equals(action[i])) {
					String userPrivs = logic.getUserIds(dbConn, map, "UPLOAD_ID");
					String rolePrivs = logic.getRoleIds(dbConn, map, "UPLOAD_ID");
					String deptPrivs = logic.getDeptIds(dbConn, map, "UPLOAD_ID");

					YHFileSortLogic sortLogic = new YHFileSortLogic();
					boolean userFlag = sortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = sortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = sortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					if (userFlag || deptFlag || roleFlag) {
						uploadPrivFlag = 1;
					}
				}
				if ("MANAGE_ID".equals(action[i])) {
					String userPrivs = logic.getUserIds(dbConn, map, "MANAGE_ID");
					String rolePrivs = logic.getRoleIds(dbConn, map, "MANAGE_ID");
					String deptPrivs = logic.getDeptIds(dbConn, map, "MANAGE_ID");

					YHFileSortLogic sortLogic = new YHFileSortLogic();
					boolean userFlag = sortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = sortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = sortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					if (userFlag || deptFlag || roleFlag) {
						managePrivFlag = 1;
					}
				}
			}

			sb.append("{");
			sb.append("uploadPriv:\"" + uploadPrivFlag + "\"");
			sb.append(",managePriv:\"" + managePrivFlag + "\"");
			sb.append(",rootDir:\"" + rootDir + "\"");
			sb.append("}");
			sb.append("]");

		} else {
			sb.append("[]");
		}
		return sb.toString();
	}

	/**
	 * 得到授权人员的id字符串
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param map
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String getUserIds(Connection dbConn, Map map, String action) throws Exception {
		YHORM orm = new YHORM();
		String ids = "";
		YHPicture picture = (YHPicture) orm.loadObjSingle(dbConn, YHPicture.class, map);
		if (picture != null && "UPLOAD_ID".equals(action)) {
			if (!"".equals(picture.getPrivStr()) && picture.getPrivStr() != null) {
				String idString = YHUtility.null2Empty(picture.getPrivStr());
				String[] idsStrings = idString.split("\\|");
				// System.out.println(idsStrings.length);
				if (idsStrings.length > 0) {
					if (idsStrings.length == 2) {
						ids = "";
					} else if (idsStrings.length == 1) {
						ids = "";
					} else {
						ids = idsStrings[2];
					}

				}
			}
		} else if (picture != null && "MANAGE_ID".equals(action)) {
			if (!"".equals(picture.getDelPrivStr()) && picture.getDelPrivStr() != null) {
				String idString = YHUtility.null2Empty(picture.getDelPrivStr());
				String[] idsStrings = idString.split("\\|");
				if (idsStrings.length > 0) {
					if (idsStrings.length == 2) {
						ids = "";
					} else if (idsStrings.length == 1) {
						ids = "";
					} else {
						ids = idsStrings[2];
					}

				}

			}
		}
		return ids;
	}

	/**
	 * 得到授权角色人员的id字符串
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param map
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String getRoleIds(Connection dbConn, Map map, String action) throws Exception {
		YHORM orm = new YHORM();
		String ids = "";
		YHPicture picture = (YHPicture) orm.loadObjSingle(dbConn, YHPicture.class, map);
		if (picture != null && "UPLOAD_ID".equals(action)) {
			if (!"".equals(picture.getPrivStr()) && picture.getPrivStr() != null) {
				String idString = YHUtility.null2Empty(picture.getPrivStr());
				String[] idsStrings = idString.split("\\|");
				if (idsStrings.length > 0) {
					if (idsStrings.length == 1) {
						ids = "";
					} else {
						ids = idsStrings[1];
					}

				}
			}
		} else if (picture != null && "MANAGE_ID".equals(action)) {
			if (!"".equals(picture.getDelPrivStr()) && picture.getDelPrivStr() != null) {
				String idString = YHUtility.null2Empty(picture.getDelPrivStr());
				String[] idsStrings = idString.split("\\|");
				if (idsStrings.length > 0) {
					if (idsStrings.length == 1) {
						ids = "";
					} else {
						ids = idsStrings[1];
					}

				}
			}
		}
		return ids;
	}

	/**
	 * 得到授权部门人员的id字符串
	 * 
	 * @param dbConn
	 * @param map
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String getDeptIds(Connection dbConn, Map map, String action) throws Exception {
		YHORM orm = new YHORM();
		String ids = "";
		YHPicture picture = (YHPicture) orm.loadObjSingle(dbConn, YHPicture.class, map);
		if (picture != null && "UPLOAD_ID".equals(action)) {
			if (!"".equals(picture.getPrivStr()) && picture.getPrivStr() != null) {
				String idString = YHUtility.null2Empty(picture.getPrivStr());
				String[] idsStrings = idString.split("\\|");
				if (idsStrings.length > 0) {
					if (idsStrings.length != 0) {
						if (idsStrings[0].length() != 0) {
							ids = idsStrings[0];
						}

					}

				}
			}
		} else if (picture != null && "MANAGE_ID".equals(action)) {
			if (!"".equals(picture.getDelPrivStr()) && picture.getDelPrivStr() != null) {
				String idString = YHUtility.null2Empty(picture.getDelPrivStr());
				String[] idsStrings = idString.split("\\|");

				if (idsStrings.length > 0) {
					if (idsStrings.length != 0) {
						if (idsStrings[0].length() != 0) {
							ids = idsStrings[0];
						}

					}

				}

			}
		}
		return ids;
	}

	/**
	 * 获取访问权限：根据ids串返回与登录的seqId比较判断是否相等，返回boolean类型。
	 * 
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public boolean getPrivate(int userSeqId, String ids) throws Exception, Exception {
		boolean flag = false;
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp)) {
					if (Integer.parseInt(tmp) == userSeqId) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 根据登录用户的部门Id与权限中的部门Id对比返回boolean
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public boolean getDeptIdPriv(int loginUserDeptId, String ids) throws Exception, Exception {
		boolean flag = false;
		if (ids != null && !"".equals(ids)) {
			if ("0".equals(ids.trim()) || "ALL_DEPT".equals(ids.trim())) {
				return flag = true;
			}
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp.trim())) {
					if (loginUserDeptId == Integer.parseInt(tmp)) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 获取图片缩略图信息到桌面模块（须分页）
	 * 
	 * @param dbConn
	 * @param filePath
	 * @param startCount
	 * @param endCount
	 * @return
	 * @throws Exception
	 */
	public YHPortalProducer getNetdiskInfoToDeskTop(Connection dbConn, String filePath, int startCount, int endCount) throws Exception {
		int k = 0;
		YHPortalProducer rule = new YHPortalProducer();
		List<Object> list = new ArrayList<Object>();
		String imageFlag = "^.*?(\\.(png|gif|jpg|bmp|PNG|GIF|JPG|BMP))$";
		try {
			if (YHUtility.isNullorEmpty(filePath)) {
				filePath = "";
				return rule;
			}
			filePath = filePath.replace('\\', '/');
			File file = new File(filePath);
			File[] files = file.listFiles();
			if (files != null && files.length != 0) {
				for (int i = startCount; i < startCount + endCount && k <= endCount && i < files.length; i++) {
					String isDir = "isFile";
					String fileType = "";
					boolean isImageFlag = false;
					if (files[i].isDirectory() && !"tdoa_cache".equals(files[i].getName())) {// 判断是目录
						isDir = "isDir";
					} else {
						if (files[i].getName().matches(imageFlag)) {
							fileType = YHFileUtility.getFileExtName(files[i].getAbsolutePath());
							this.createCache(file.getPath(), files[i].getName());
							isImageFlag = true;
						}
					}
					if ("isDir".equals(isDir) || isImageFlag) {// 是目录 并且读取原图图片 和缩略图
						Map<String, String> map = new HashMap<String, String>();
						long fileSize = files[i].length();
						map.put("fileName", files[i].getName());// 文件的名称
						map.put("fileSize", String.valueOf(fileSize));// 文件的大小
						map.put("fileType", fileType);// 判断文件后缀类型 是图片 还是其他
						map.put("isDir", isDir); // 是目录
						if (!"isDir".equals(isDir)) {
							String contextPath = YHSysProps.getString(YHSysPropKeys.JSP_ROOT_DIR);
							map.put("tdoaCachePath", "/" + contextPath + "/getFile?uploadFileNameServer="
									+ YHUtility.encodeURL(file.getAbsolutePath().replace('\\', '/') + "/tdoa_cache/" + files[i].getName()));// 缩略图路径
						} else {
							map.put("tdoaCachePath", YHPublicPortAct.ICON_FOLDER);// 缩略图路径
						}
						map.put("filePath", files[i].getAbsolutePath().replace('\\', '/'));// 原图路径
						map.put("fileModifyTime", YHUtility.getDateTimeStr(new Date(files[i].lastModified())) + "=======");
						list.add(map);
					}
					k++;
				}
				rule.setData(list);
				YHImgRule imag = new YHImgRule("fileName", "tdoaCachePath", "filePath");
				rule.addRule(imag);
				// rule.toJson();
			}
//			System.out.println(list.toString());
		} catch (Exception e) {
			throw e;
		}
		return rule;
	}

	/**
	 * 上传文件中，文件夹里文件已存在的处理
	 * 
	 * @param desPath
	 * @param fileName
	 * @param nameTitle
	 * @param fileType
	 * @throws Exception
	 */
	public void uploadEexistsFile(StringBuffer buffer, String desPath, String fileName, String nameTitle, String fileType) throws Exception {
		try {
			String newFileName = nameTitle + " - 复件";
			String newFileStr = desPath + "/" + newFileName.trim() + fileType.trim();
			File newFile = new File(newFileStr);
			if (newFile != null) {
				if (!newFile.exists()) {
					buffer.append(newFile.getName());
				} else {
					uploadEexistsFile(buffer, desPath, fileName, newFileName.trim(), fileType.trim());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	// public static void main(String[] args) throws Exception {
	// Connection dbConn = null;
	// String filePath = "D:/tt";
	// YHPictureLogic lc = new YHPictureLogic();
	// lc.getNetdiskInfoToDeskTop(dbConn, filePath, 3, 5);
	//
	// }

}
