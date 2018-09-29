package yh.core.funcs.netdisk.act;

import java.io.File;
import java.net.URLDecoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHMapComparator;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.netdisk.data.YHNetdisk;
import yh.core.funcs.netdisk.data.YHPage;
import yh.core.funcs.netdisk.logic.YHNetDiskLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHFileUtility;

public class YHNetDiskAct {
	private static Logger log = Logger.getLogger(YHNetDiskAct.class);

	/**
	 * 新建文件夹	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String addFileFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// request.setCharacterEncoding("utf-8");
		String path = request.getParameter("DISK_ID"); // d:/bjfaoitc/tdoa_cache/ssss/ssss
		String name = request.getParameter("FILE_NAME"); // aaa
		String seqIdStr = request.getParameter("seqId"); // 5
		String folderNamePath = path + "/" + name;

		String data = "";
		boolean flag = false;
		int sucuss = 1;
		int isExist = 1;

		String foldName = "";
		String returnDiskId = "";

		YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
			if (path != null && path.trim().length() > 0) {
				File file = new File(folderNamePath);
				if (!file.exists()) {
					if (file.mkdir()) {

						foldName = name;
						returnDiskId = file.getPath().replace('\\', '/'); // d:/bjfaoitc/~!@#$%^/bb

						request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
						request.setAttribute(YHActionKeys.RET_MSRG, "创建 x成功！");
						// sucuss = "创建成功";
						flag = true;
					} else {
						// System.out.println("创建不成功！");
						sucuss = 0;
					}
				} else {
					// System.out.println("文件夹已存在");
					isExist = 0;
				}
			}
			if (flag) {
	      // 写入系统日志
	        String remark = "新建子文件夹，名称为：" + foldName ;
	        YHSysLogLogic.addSysLog(dbConn, YHLogConst.NET_DISK, remark, person.getSeqId(), request.getRemoteAddr());
	      }

			data = "{sucuss:\"" + sucuss + "\",isExist:\"" + isExist + "\",flag:\"" + flag + "\",seqId:\"" + seqIdStr + "\",nodeName:\"" + YHUtility.encodeSpecial(foldName)
					+ "\",returnDiskId:\"" + YHUtility.encodeSpecial(returnDiskId) + "\"}";
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 重命名文件夹
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String renameFileFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// String path = URLDecoder.decode(request.getParameter("DISK_ID"),
		// "UTF-8");
		String path = request.getParameter("diskId"); // d:/bjfaoitc/tdoa_cache/ssss/ssss/aaa/kkkk
		String name = request.getParameter("FILE_NAME");
		String seqIdStr = request.getParameter("seqId");

		int seqId = 0;
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}

		if (path == null || "".equals(path)) {
			path = "D:\\tmp";
		}
		String newPath = path.replace('\\', '/').substring(0, path.lastIndexOf('/')); // d:/bjfaoitc/tdoa_cache/ssss/ssss/aaa
		path = path.replace('\\', '/');
		YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

			String data = "";
			boolean flag = false;
			int sucuss = 1;
			int isExist = 1;

			String foldName = "";
			String returnDiskId = "";
			String nameStr = "";

			File file = new File(path);
			File newFile = new File(newPath + "/" + name);
			if (!newFile.exists()) {
				if (file.renameTo(newFile)) {
					// System.out.println("重命名成功！");
					foldName = name;
					nameStr = file.getName();
					returnDiskId = newFile.getPath().replace('\\', '/');
					flag = true;

				} else {
					// System.out.println("重命名失败！");
					sucuss = 0;
				}
			} else {
				// System.out.println("文件夹已存在！");
				isExist = 0;

			}
			if (flag) {
        // 写入系统日志
          String remark = "重命名文件夹 " + nameStr + " 为：" + foldName ;
          YHSysLogLogic.addSysLog(dbConn, YHLogConst.NET_DISK, remark, person.getSeqId(), request.getRemoteAddr());
        }
			data = "{sucuss:\"" + sucuss + "\",isExist:\"" + isExist + "\",flag:\"" + flag + "\",seqId:\"" + seqId + "\",nodeName:\"" + YHUtility.encodeSpecial(foldName)
					+ "\",returnDiskId:\"" + YHUtility.encodeSpecial(returnDiskId) + "\"}";

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功重命名文件夹");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		// return "/core/funcs/netdisk/folder/result.jsp?seqId=" + seqId + "&idStr="
		// + path;
		return "/core/inc/rtjson.jsp";

	}

	/**
	 * 删除文件夹	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delFileFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String recycle = YHSysProps.getString("$MYOA_IS_RECYCLE");
		if (recycle == null) {
			recycle = "";
		}
		String recyclePath = YHSysProps.getAttachPath() + File.separator + "recycle" + File.separator + "netdisk"; // 文件回收站的路径

		String path = request.getParameter("DISK_ID");
		String seqId = request.getParameter("seqId");
		YHNetDiskLogic logic = new YHNetDiskLogic();

		if (path == null) {
			path = "";
		}
		YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

			File filePath = new File(path);
			if (filePath != null && filePath.exists()) {
				if ("1".equals(recycle.trim())) {
					logic.deleteAllToRecycle(filePath, recyclePath);
				} else {
					YHFileUtility.deleteAll(path);

				}
			// 写入系统日志
        String remark = "删除文件夹：" + filePath.getName() ;
        YHSysLogLogic.addSysLog(dbConn, YHLogConst.NET_DISK, remark, person.getSeqId(), request.getRemoteAddr());
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功删除文件夹");
			request.setAttribute(YHActionKeys.RET_DATA, "{seqId:" + seqId + ", diskId:\"" + YHUtility.encodeSpecial(path) + "\"}");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 文件操作(复制、剪贴)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String doAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getParameter("DISK_ID");
		String netdisk_sub_url = request.getParameter("netdisk_sub_url"); // 全文检索用到
		if (netdisk_sub_url == null) {
			netdisk_sub_url = "";
		}
		if (path == null) {
			path = "";
		}

		if (!"".equals(path.trim())) {
			netdisk_sub_url = "";
		} else if (!"".equals(netdisk_sub_url.trim())) {
			path = "";
		}

		if (path == null) {
			path = "";
		}
		path = path.replace('\\', '/');
		String fileList = request.getParameter("FILE_LIST");
		String action = request.getParameter("NETDISK_ACTION");
		request.getSession().setAttribute("NETDISK_PATH", path);
		request.getSession().setAttribute("NETDISK_FILENAME", fileList);
		request.getSession().setAttribute("netdisk_sub_url", netdisk_sub_url);

		request.getSession().setAttribute("NETDISK_ACTION", action);
		request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		request.setAttribute(YHActionKeys.RET_MSRG, "执行成功");
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 重命名文件
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String renameFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getParameter("diskId");
		String newName = request.getParameter("FILE_NAME");
		String oldName = request.getParameter("oldFileName");

		String seqIdStr = request.getParameter("seqId");

		int seqId = 0;
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}

		if (path == null) {
			path = "";
		}
		path = path.replace('\\', '/');

		if (path.endsWith(("*"))) {
			path = path.substring(0, path.length() - 1);
		}

		try {
			File file = new File(path + "/" + oldName);
			File newFile = new File(path + "/" + newName);

			int flag = 0;
			int sucuss = 1;
			int isExist = 0;
			String data = "";

			if (!newFile.exists()) {
				if (file.renameTo(newFile)) {
					// System.out.println("成功重命名文件");
					flag = 1;

				} else {
					// System.out.println("重命名文件失败");
					sucuss = 0;
				}

			} else {
				// System.out.println("文件已存在");
				isExist = 1;
			}

			data = "{sucuss:\"" + sucuss + "\",isExist:\"" + isExist + "\",flag:\"" + flag + "\",seqId:\"" + seqId + "\"}";

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功重命名文件");
			request.setAttribute(YHActionKeys.RET_DATA, data);

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		// return "/core/funcs/netdisk/result.jsp";
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 删除文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String delFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();
		String recycle = YHSysProps.getString("$MYOA_IS_RECYCLE");
		if (recycle == null) {
			recycle = "";
		}

		String path = request.getParameter("DISK_ID");
		String fileList = request.getParameter("FILE_LIST");
		String netdisk_sub_url = request.getParameter("netdisk_sub_url");

		if (path == null) {
			path = "";
		}
		if (fileList == null) {
			fileList = "";
		}
		if (netdisk_sub_url == null) {
			netdisk_sub_url = "";
		}

		String recyclePath = YHSysProps.getAttachPath() + File.separator + "recycle" + File.separator + "netdisk"; // 文件回收站的路径
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
    int loginUserSeqId = loginUser.getSeqId();
		Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      boolean delFlag = false;
      String addLogInfo = "";
			String[] names = null;
			if (fileList != null) {
				names = fileList.split("\\*");
			}

			if (!"".equals(netdisk_sub_url.trim())) {
				String[] diskPatthArry = null;
				if (netdisk_sub_url != null) {
					diskPatthArry = netdisk_sub_url.split("\\*");
				}

				if (names.length != 0) {
					for (int i = 0; i < names.length; i++) {
						String filePath = diskPatthArry[i];
						if ("1".equals(recycle.trim())) {
							String rand = emut.getRandom();
							String newFileName = rand + "_" + names[i];
							YHFileUtility.xcopyFile(filePath + File.separator + names[i], recyclePath + File.separator + newFileName);
							addLogInfo = (filePath + File.separator + names[i]).replace("\\", "/");
              delFlag = true;
						} else {
							YHFileUtility.deleteAll(filePath + File.separator + names[i]);
							addLogInfo = (filePath + File.separator + names[i]).replace("\\", "/");
              delFlag = true;
						}
						if (delFlag) {
	            // 写入系统日志
	              String remark = "删除 " + addLogInfo ;
	              YHSysLogLogic.addSysLog(dbConn, YHLogConst.NET_DISK, remark, loginUserSeqId, request.getRemoteAddr());
	            }
					}
				}

			} else {
				if (names.length != 0) {
					for (int i = 0; i < names.length; i++) {
						if ("1".equals(recycle.trim())) {
							String rand = emut.getRandom();
							String fileName = rand + "_" + names[i];
							YHFileUtility.xcopyFile(path + File.separator + names[i], recyclePath + File.separator + fileName);
							addLogInfo = (path + File.separator + names[i]).replace("\\", "/");
              delFlag = true;
						} else {
							YHFileUtility.deleteAll(path + File.separator + names[i]);
							addLogInfo = (path + File.separator + names[i]).replace("\\", "/");
              delFlag = true;
						}
						if (delFlag) {
              // 写入系统日志
                String remark = "删除 " + addLogInfo ;
                YHSysLogLogic.addSysLog(dbConn, YHLogConst.NET_DISK, remark, loginUserSeqId, request.getRemoteAddr());
              }

					}
					request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
					request.setAttribute(YHActionKeys.RET_MSRG, "成功删除文件夹");

				}

			}

		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 粘贴文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String pasteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String srcPath = (String) request.getSession().getAttribute("NETDISK_PATH"); // d:/test/tdoa_cache
		String fileList = (String) request.getSession().getAttribute("NETDISK_FILENAME"); // sa要.gif*
		String action = (String) request.getSession().getAttribute("NETDISK_ACTION"); // copy
		String netdisk_sub_url = (String) request.getSession().getAttribute("netdisk_sub_url"); // 全文检索用到
		String path = request.getParameter("DISK_ID"); // d:/bjfaoitc/ 要保存的路径

		if (fileList == null) {
			fileList = "";
		}

		if (srcPath == null) {
			srcPath = "";
		}
		if (netdisk_sub_url == null) {
			netdisk_sub_url = "";
		}

		if (path == null) {
			path = "";
		}
		String desPath = path.replace('\\', '/');

		YHNetDiskLogic logic = new YHNetDiskLogic();

		try {

			String[] names = null;
			if (fileList != null) {
				names = fileList.split("\\*");
			}

			if (!"".equals(srcPath.trim())) {

				if ("cut".equals(action.trim())) {
					for (int i = 0; i < names.length; i++) {
						String fileName = names[i];
						String fileType = "";
						String nameTitle = "";

						if (fileName.lastIndexOf(".") != -1) {
							fileType = fileName.substring(fileName.lastIndexOf(".")); // .doc
							nameTitle = fileName.substring(0, fileName.lastIndexOf("."));
						}
						File file = new File(desPath + "/" + fileName);
						if (!file.exists()) {

							YHFileUtility.xcopyFile(srcPath + "/" + fileName, file.getAbsolutePath());
						} else {
							logic.cutEexistsFile(srcPath, desPath, fileName, nameTitle, fileType);

						}

					}
				} else {
					for (int i = 0; i < names.length; i++) {
						String fileName = names[i];

						String fileType = "";
						String nameTitle = "";
						if (fileName.lastIndexOf(".") != -1) {
							fileType = fileName.substring(fileName.lastIndexOf(".")); // .doc
							nameTitle = fileName.substring(0, fileName.lastIndexOf("."));
						}

						File file = new File(desPath + "/" + fileName);
						if (!file.exists()) {
							YHFileUtility.copyFile(srcPath + "/" + fileName, file.getAbsolutePath());
						} else {
							logic.copyEexistsFile(srcPath, desPath, fileName, nameTitle, fileType);
						}

					}
				}

			} else if (!"".equals(netdisk_sub_url.trim())) {
				String[] diskPatthArry = null;
				if (netdisk_sub_url != null) {
					diskPatthArry = netdisk_sub_url.split("\\*");
				}

				if ("cut".equals(action.trim())) {
					for (int i = 0; i < names.length; i++) {

						String fileName = names[i];
						String filePath = diskPatthArry[i];

						String fileType = "";
						String nameTitle = "";

						if (fileName.lastIndexOf(".") != -1) {
							fileType = fileName.substring(fileName.lastIndexOf(".")); // .doc
							nameTitle = fileName.substring(0, fileName.lastIndexOf("."));
						}
						File file = new File(desPath + "/" + fileName);
						if (!file.exists()) {

							YHFileUtility.xcopyFile(filePath + "/" + fileName, file.getAbsolutePath());
						} else {
							logic.cutEexistsFile(filePath, desPath, fileName, nameTitle, fileType);

						}

					}

				} else {

					for (int i = 0; i < names.length; i++) {
						String fileName = names[i];
						String filePath = diskPatthArry[i];

						String fileType = "";
						String nameTitle = "";
						if (fileName.lastIndexOf(".") != -1) {
							fileType = fileName.substring(fileName.lastIndexOf(".")); // .doc
							nameTitle = fileName.substring(0, fileName.lastIndexOf("."));
						}

						File file = new File(desPath + "/" + fileName);
						if (!file.exists()) {
							YHFileUtility.copyFile(filePath + "/" + fileName, file.getAbsolutePath());
						} else {
							logic.copyEexistsFile(filePath, desPath, fileName, nameTitle, fileType);
						}

					}

				}

			}

			request.getSession().setAttribute("NETDISK_PATH", null);
			request.getSession().setAttribute("netdisk_sub_url", null);
			request.getSession().setAttribute("NETDISK_FILENAME", null);
			request.getSession().setAttribute("NETDISK_ACTION", null);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功删除文件夹");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 取得磁盘所有信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getNetDiskInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getParameter("DISK_ID");
		String seqIdStr = request.getParameter("seqId");

		// String currNoStr = request.getParameter("currNo"); // 当前的页码
		String pageNoStr = request.getParameter("pageNo");
		String pageSizeStr = request.getParameter("pageSize");

		String orderBy = request.getParameter("field");
		String ascDesc = request.getParameter("ascDescFlag");

		if (orderBy == null) {
			orderBy = "";
		}
		if (ascDesc == null) {
			ascDesc = "";
		}

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		YHNetDiskLogic logic = new YHNetDiskLogic();

		int seqId = 0;
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}

		int currNo = 1;
		if (YHUtility.isNullorEmpty(pageNoStr)) {
			currNo = 1;
		} else {
			currNo = Integer.parseInt(pageNoStr);
		}

		if (path == null || "".equals(path)) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			return "/core/inc/rtjson.jsp";
		}
		StringBuffer tmp = new StringBuffer("[");

		boolean isHave = false;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			YHNetdisk diNetdisk = logic.getFileSortInfoById(dbConn, seqIdStr);

			if ("".equals(orderBy)) {
				orderBy = YHUtility.null2Empty(diNetdisk.getOrderBy());

			}
			if ("".equals(ascDesc)) {
				ascDesc = YHUtility.null2Empty(diNetdisk.getAscDesc());
			}

			if ("taille".equals(orderBy) || "SIZE".equals(orderBy)) {
				orderBy = "SIZE";

			} else if ("mod".equals(orderBy) || "TIME".equals(orderBy)) {
				orderBy = "TIME";

			} else if ("type".equals(orderBy) || "TYPE".equals(orderBy)) {
				orderBy = "TYPE";

			} else {
				orderBy = "NAME";
			}

			String diskPath = YHUtility.null2Empty(diNetdisk.getDiskPath());
			File file2 = new File(diskPath);

			path = path.replace('\\', '/'); // D:\aa\\
			File file = new File(path); // D:\aa

			int rootDir = 0; // 用于判断是根目录还是子目录,1为根目录
			if (file2.getPath().equals(file.getPath())) {
				rootDir = 1;
			}

			// 获取系统设置的大小			int spaceLimit = diNetdisk.getSpaceLimit();

			long returnSize = 0; // 返回的大小
			long limitSize = 0; // 系统设置大小M转换为b
			long folderTotalSize = 0;
			if (spaceLimit != 0) {
				limitSize = logic.transFolderSize(spaceLimit);
				folderTotalSize = logic.getFolderSize(file2);
			}
			returnSize = limitSize - folderTotalSize;
			if (spaceLimit == 0) {
				returnSize = 0;
			} else if (returnSize < 0) {
				returnSize = 1;
			}
			//
			// File[] files = file.listFiles();
			// int j = 0;
			// if (files != null && files.length != 0) {
			// for (int i = 0; i < files.length; i++) {
			// File subfile = files[i];
			// if (!subfile.isDirectory()) {
			//
			// Map m = new HashMap();
			//
			// String fileType =
			// YHFileUtility.getFileExtName(subfile.getAbsolutePath());
			// String typeName = logic.getFileTypeName(fileType);
			// boolean isOffice = logic.is_office("." + fileType);
			// String officeFlag = "0";
			// if (isOffice) {
			// officeFlag = "1";
			// }
			//
			// long fileSize = subfile.length();
			//
			// m.put("fileName", subfile.getName());
			// m.put("fileSpace", String.valueOf(fileSize));
			// m.put("fileType", typeName);
			// m.put("officeFlag", officeFlag);
			// m.put("rootDir", String.valueOf(rootDir));
			// m.put("filePath", subfile.getAbsolutePath().replace('\\', '/'));
			// m.put("fileModifyTime", YHUtility.getDateTimeStr(new
			// Date(subfile.lastModified())));
			// list.add(m);
			// j++;
			// }
			// }
			// }
			//
			list = logic.getfileInforByList(dbConn, loginUser, diNetdisk, path);
			if (list != null && list.size() > 0) {

				if ("NAME".equals(orderBy.trim())) {

					if ("1".equals(ascDesc.trim())) {
						YHUtility.sortDesc(list, "fileName", YHMapComparator.TYPE_STR);
					} else {
						YHUtility.sortAsc(list, "fileName", YHMapComparator.TYPE_STR);
					}

				} else if ("SIZE".equals(orderBy.trim())) {
					if ("1".equals(ascDesc.trim())) {
						YHUtility.sortDesc(list, "fileSpace", YHMapComparator.TYPE_LONG);
					} else {
						YHUtility.sortAsc(list, "fileSpace", YHMapComparator.TYPE_LONG);
					}
				} else if ("TYPE".equals(orderBy.trim())) {
					if ("1".equals(ascDesc.trim())) {
						YHUtility.sortDesc(list, "fileType", YHMapComparator.TYPE_STR);
					} else {
						YHUtility.sortAsc(list, "fileType", YHMapComparator.TYPE_STR);
					}
				} else if ("TIME".equals(orderBy.trim())) {
					if ("1".equals(ascDesc.trim())) {
						YHUtility.sortDesc(list, "fileModifyTime", YHMapComparator.TYPE_STR);
					} else {
						YHUtility.sortAsc(list, "fileModifyTime", YHMapComparator.TYPE_STR);
					}
				}

			}

			long count = list.size();
			int pageSize = 20;// 一个页面显示的数目
			if (pageSizeStr != null) {
				pageSize = Integer.parseInt(pageSizeStr);
			}

			YHPage page = new YHPage(pageSize, count, (currNo + 1));
			long first = page.getFirstResult();
			long last = page.getLastResult();
			for (int i = (int) first; i < last; i++) {
				Map<String, String> map = list.get(i);

				String fileSizeString = map.get("fileSpace");
				long fileSizeLong = Long.parseLong(fileSizeString);

				tmp.append("{");
				tmp.append("fileName:\"" + YHUtility.encodeSpecial(map.get("fileName")) + "\"");
				tmp.append(",fileSpace:\"" + logic.transformSize(fileSizeLong) + "\"");
				tmp.append(",fileType:\"" + map.get("fileType") + "\"");
				tmp.append(",officeFlag:\"" + map.get("officeFlag") + "\"");
				tmp.append(",rootDir:\"" + map.get("rootDir") + "\"");
				tmp.append(",flolderSize:" + returnSize); // 返回文件夹大小单位为B
				tmp.append(",filePath:\"" + YHUtility.encodeSpecial(map.get("filePath")) + "\"");
				tmp.append(",fileModifyTime:\"" + map.get("fileModifyTime") + "\"");

				tmp.append(",totalRecord:\"" + count + "\"");
				tmp.append(",pageNo:\"" + currNo + "\"");
				tmp.append(",pageSize:\"" + pageSize + "\"");
				tmp.append(",ascDesc:\"" + ascDesc + "\"");
				tmp.append(",orderBy:\"" + orderBy + "\"");

				tmp.append("},");
				isHave = true;
			}
			if (isHave) {
				tmp.deleteCharAt(tmp.length() - 1);
				tmp.append("]");
			} else {
				tmp.append("]");
			}

			// request.getSession().setAttribute("FILE_COUNT", new Integer(j));
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, tmp.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 获取树形结构信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getTreebyFileSystem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idStr = request.getParameter("id");
		String path = request.getParameter("DISK_ID");
		String seqIdStr = request.getParameter("seqId");

		String id = "0";
		int seqId = 0;
		if (!"".equals(seqIdStr) && seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}
		if (idStr != null && !"".equals(idStr)) {
			id = idStr;
		}
		if (!"".equals(idStr) && idStr != null && !"0".equals(idStr) && "".equals(path)) {
			idStr = idStr.replace('\\', '/');
		}
		if ("0".equals(idStr) && path != null && !"".equals(path)) {
			idStr = path.replace('\\', '/');
		}
		if (idStr != null && !"".equals(idStr) && path != null && !"".equals(path)) {
			idStr = idStr.replace('\\', '/');
		}

		YHNetDiskLogic diskLogic = new YHNetDiskLogic();
		List<YHNetdisk> list = new ArrayList<YHNetdisk>();
		StringBuffer sb = new StringBuffer("[");

		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		// int loginUserSeqId = loginUser.getSeqId();
		// int loginUserDeptId = loginUser.getDeptId();
		// String loginUserRoleId = loginUser.getUserPriv();
		// boolean userFlag = false;
		// boolean roleFlag = false;
		// boolean deptFlag = false;

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			boolean isHave = false;
			
			boolean userDefined = false;//自定义菜单
			if (!YHUtility.isNullorEmpty(path) && seqId!=0) {
				userDefined = true;
			}
			if (userDefined) {
				if ("0".equalsIgnoreCase(id)) {
					YHNetdisk netdisk = diskLogic.getNetdiskInfoById(dbConn, seqId);
					if (netdisk != null) {
						String filePath = YHUtility.null2Empty(netdisk.getDiskPath());
						boolean userIdPriv = diskLogic.getUserIdAccessPriv(dbConn, loginUser, netdisk);
						boolean manageUserPriv = diskLogic.getManageAccessPriv(dbConn, loginUser, netdisk);
						if (userIdPriv || manageUserPriv) {
							if (!YHUtility.isNullorEmpty(filePath.trim())) {
								int isHaveChild = 0;
								File file = new File(filePath.trim());
								if (file != null) {
									File[] files = file.listFiles();
									if (files != null && files.length != 0) {
										for (File subFile : files) {
											if (subFile.isDirectory()) {
												isHaveChild = 1;
												break;
											}
										}
									}
								}
								sb.append("{");
								sb.append("nodeId:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(netdisk.getDiskPath()).replace('\\', '/')) + "\"");
								sb.append(",name:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(netdisk.getDiskName())) + "\"");
								sb.append(",isHaveChild:" + isHaveChild);
								sb.append(",extData:\"" + netdisk.getSeqId() + "\"");
								sb.append("},");
								isHave = true;
							}
						}
						if (isHave) {
							sb.deleteCharAt(sb.length() - 1);
						}
						sb.append("]");
					}else {
						sb.append("]");
					}
				}else {
					File file = new File(id);
					if (file!=null) {
						File[] files = file.listFiles();
						if (files != null && files.length > 0) {
							for (int i = 0; i < files.length; i++) {
								File subFile = files[i];
								if (subFile.isDirectory() && !"tdoa_cache".equalsIgnoreCase(subFile.getName().trim())) {
									int isHaveChild = 0;
									File[] subFiles = subFile.listFiles();
									if (subFiles != null) {
										for (int j = 0; j < subFiles.length; j++) {
											File ssFile = subFiles[j];
											if (ssFile.isDirectory()) {
												isHaveChild = 1;
												break;
											}
										}
									}
									String extData = "";
									sb.append("{");
									sb.append("nodeId:\"" + subFile.getAbsolutePath().replace('\\', '/') + "\"");
									sb.append(",name:\"" + YHUtility.encodeSpecial(subFile.getName()) + "\"");
									sb.append(",isHaveChild:" + isHaveChild + "");
									sb.append(",extData:\"" + seqId + "\"");
									sb.append("},");
									isHave = true;
								}
							}
						}
						if (isHave == true) {
							sb.deleteCharAt(sb.length() - 1);
							
						} 
						sb.append("]");
					}else {
						sb.append("]");
					}
				}
			}else {
				if (YHUtility.isNullorEmpty(path) && "0".equalsIgnoreCase(id)) {				
					list = diskLogic.getNetDisksInfo(dbConn);
					if (list.size() > 0) {
//						sb.append("[");
						// 遍历数据库中的目录
						for (YHNetdisk disk : list) {
							String filePath = YHUtility.null2Empty(disk.getDiskPath());
							// YHFileSortLogic fileSortLogic = new YHFileSortLogic();
							// Map map = new HashMap();
							// map.put("SEQ_ID", disk.getSeqId());
							// String userPrivs = diskLogic.getUserIds(dbConn, map, "USER_ID");
							// String rolePrivs = diskLogic.getRoleIds(dbConn, map, "USER_ID");
							// String deptPrivs = diskLogic.getDeptIds(dbConn, map, "USER_ID");
							// a
							// userFlag = fileSortLogic.getUserIdStr(loginUserSeqId, userPrivs,
							// dbConn);
							// roleFlag = fileSortLogic.getRoleIdStr(loginUserRoleId, rolePrivs,
							// dbConn);
							// deptFlag = fileSortLogic.getDeptIdStr(loginUserDeptId, deptPrivs,
							// dbConn);
							boolean userIdPriv = diskLogic.getUserIdAccessPriv(dbConn, loginUser, disk);
							boolean manageUserPriv = diskLogic.getManageAccessPriv(dbConn, loginUser, disk);

							if (userIdPriv || manageUserPriv) {
								if (!YHUtility.isNullorEmpty(filePath.trim())) {
									int isHaveChild = 0;
									File file = new File(filePath.trim());
									if (file != null) {
										File[] files = file.listFiles();
										if (files != null && files.length != 0) {
											for (File subFile : files) {
												if (subFile.isDirectory()) {
													isHaveChild = 1;
													break;
												}
											}
										}
									}
									sb.append("{");
									sb.append("nodeId:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(disk.getDiskPath()).replace('\\', '/')) + "\"");
									sb.append(",name:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(disk.getDiskName())) + "\"");
									sb.append(",isHaveChild:" + isHaveChild);
									sb.append(",extData:\"" + disk.getSeqId() + "\"");
									sb.append("},");
									isHave = true;
								}
							}
						}
						if (isHave) {
							sb.deleteCharAt(sb.length() - 1);
						}
						sb.append("]");
					} else {
						sb.append("]");
					}
				} else if (path != null && !"".equals(idStr)) {
					File file = new File(idStr);
					if (file!=null) {
						File[] files = file.listFiles();
//						sb.append("[");
						if (files != null && files.length > 0) {
							for (int i = 0; i < files.length; i++) {
								File subFile = files[i];
								if (subFile.isDirectory() && !"tdoa_cache".equalsIgnoreCase(subFile.getName().trim())) {
									int isHaveChild = 0;
									File[] subFiles = subFile.listFiles();
									if (subFiles != null) {
										for (int j = 0; j < subFiles.length; j++) {
											File ssFile = subFiles[j];
											if (ssFile.isDirectory()) {
												isHaveChild = 1;
												break;
											}
										}
									}
									String extData = "";
									// String imgAddress =
									// "/yh/core/styles/style1/img/dtree/folder.gif";
									sb.append("{");
									sb.append("nodeId:\"" + subFile.getAbsolutePath().replace('\\', '/') + "\"");
									sb.append(",name:\"" + YHUtility.encodeSpecial(subFile.getName()) + "\"");
									sb.append(",isHaveChild:" + isHaveChild + "");
									sb.append(",extData:\"" + seqId + "\"");
									// tmp.append(",imgAddress:\"" + imgAddress + "\"");
									sb.append("},");
									isHave = true;
								}
							}
						}
						if (isHave == true) {
							sb.deleteCharAt(sb.length() - 1);
							
						} 
						sb.append("]");
					}else {
						sb.append("]");
					}
				}
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
			request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 构造单个对象,上传单个文件、批量文件	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHFileUploadForm fileForm = new YHFileUploadForm();
		String contextPath = request.getContextPath();
    try {
      fileForm.parseUploadRequest(request);
    } catch (Exception e) {
      response.sendRedirect(contextPath + "/core/funcs/netdisk/uploadFailse.jsp");
      return null;
    }
		String path = fileForm.getParameter("DISK_ID");
		String seqIdStr = request.getParameter("seqId");
		String smsPerson = fileForm.getParameter("smsPerson");
		String mobileSmsPerson = fileForm.getParameter("mobileSmsPerson");

		if (path == null) {
			path = request.getParameter("DISK_ID");
		}
		if (path == null || "0".equals(path)) {
			path = new String("D:"+ File.separator +"tmp");
		}

		if (smsPerson == null) {
			smsPerson = "";
		}
		if (mobileSmsPerson == null) {
			mobileSmsPerson = "";
		}

		String fileName = "";

		// // 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		YHNetDiskLogic logic = new YHNetDiskLogic();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			String filePath = path.replace('\\', '/');
			String fileExists = fileForm.getExists(filePath);
			Iterator<String> iKeys = fileForm.iterateFileFields();
			String addLogInfo = "";
			while (iKeys.hasNext()) {
				String fieldName = iKeys.next();
				fileName = fileForm.getFileName(fieldName);
				if (YHUtility.isNullorEmpty(fileName)) {
					continue;
				}
				File file = new File(filePath+"/" + fileName);
				if (file!=null && !file.exists()) {
					fileForm.saveFile(fieldName, filePath + File.separator + fileName);
					addLogInfo = file.getPath().replace("\\", "/");
				}else {
					StringBuffer buffer = new StringBuffer();
					String fileType = "";
					String nameTitle = "";
					if (fileName.lastIndexOf(".") != -1) {
						fileType = fileName.substring(fileName.lastIndexOf(".")); // .doc
						nameTitle = fileName.substring(0, fileName.lastIndexOf("."));
					}
					logic.uploadEexistsFile(buffer,filePath, fileName, nameTitle, fileType);
					String newFileName = buffer.toString();
					if (!YHUtility.isNullorEmpty(newFileName)) {
						fileForm.saveFile(fieldName, filePath + File.separator + newFileName.trim());
						
						File file2 = new File(filePath + "/" + newFileName.trim());
		        fileName = newFileName.trim();
		        if (file2!=null) {
		          addLogInfo = file2.getPath().replace("\\", "/");
		        }
						
					}
				}
				 // 写入系统日志
			  String remark = "上传 " + addLogInfo ;
			  YHSysLogLogic.addSysLog(dbConn, YHLogConst.NET_DISK, remark, loginUserSeqId, request.getRemoteAddr());
			}

			// 短信提醒
			// YHSmsUtil sms=new YHSmsUtil();
			YHSmsBack sms = new YHSmsBack();
			String loginName = logic.getPersonNamesByIds(dbConn, String.valueOf(loginUserSeqId));
			String smsContent = loginName + " 在网络硬盘【" + filePath.replace('\\', '/') + "】 上传文件:" + fileName;
			String remindUrl = "/core/funcs/netdisk/fileList.jsp?smsRemindFlag=1&seqId=" + seqIdStr + "&DISK_ID=" + filePath;
		//	URLEncoder.encode(filePath,"UTF-8");
			if ("allPrivPerson".equals(smsPerson)) {

				Map map = new HashMap();
				map.put("SEQ_ID", Integer.parseInt(seqIdStr));

				YHNetdisk netdisk = logic.getFileSortInfoById(dbConn, seqIdStr);
				String deptIdStr = logic.getDeptIds(dbConn, map, "USER_ID");
				String roleIdStr = logic.getRoleIds(dbConn, map, "USER_ID");
				String personIdStr = logic.getUserIds(dbConn, map, "USER_ID");

				if (!"".equals(personIdStr)) {
					personIdStr += ",";
				}
				
				String deptPersonIdStr ="";
				String rolePersonIdStr ="";
				if (!YHUtility.isNullorEmpty(deptIdStr)) {
					deptPersonIdStr = logic.getDeptPersonIds(deptIdStr, dbConn);
				}
				if (!YHUtility.isNullorEmpty(roleIdStr)) {
					rolePersonIdStr = logic.getRolePersonIds(roleIdStr, dbConn);
				}
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
				if (allpersonStr != null && !"".equals(allpersonStr)) {
					sms.setFromId(loginUserSeqId);
					sms.setToId(allpersonStr.trim());
					sms.setContent(smsContent);
					sms.setSendDate(YHUtility.parseTimeStamp());
					sms.setSmsType(YHLogConst.NET_DISK);
					sms.setRemindUrl(remindUrl);
					YHSmsUtil.smsBack(dbConn, sms);
				}
			} else if (!"".equals(smsPerson)) {
				sms.setFromId(loginUserSeqId);
				sms.setToId(smsPerson);
				sms.setContent(smsContent);
				sms.setSendDate(YHUtility.parseTimeStamp());
				sms.setSmsType(YHLogConst.NET_DISK);
				sms.setRemindUrl(remindUrl);
				YHSmsUtil.smsBack(dbConn, sms);

			}

			// 手机短信提醒mobileSmsPerson
			String mobileSmsContent = loginName + " 在网络硬盘【" + filePath.replace('\\', '/') + "】 上传文件:" + fileName;
			YHMobileSms2Logic mobileSms = new YHMobileSms2Logic();
			if ("allPrivPerson".equals(mobileSmsPerson.trim())) {
				Map map = new HashMap();
				map.put("SEQ_ID", Integer.parseInt(seqIdStr));

				YHNetdisk netdisk = logic.getFileSortInfoById(dbConn, seqIdStr);
				String deptIdStr = logic.getDeptIds(dbConn, map, "USER_ID");
				String roleIdStr = logic.getRoleIds(dbConn, map, "USER_ID");
				String personIdStr = logic.getUserIds(dbConn, map, "USER_ID");

				if (!"".equals(personIdStr)) {
					personIdStr += ",";
				}
				
				String deptPersonIdStr ="";
				String rolePersonIdStr ="";
				if (!YHUtility.isNullorEmpty(deptIdStr)) {
					deptPersonIdStr = logic.getDeptPersonIds(deptIdStr, dbConn);
				}
				if (!YHUtility.isNullorEmpty(roleIdStr)) {
					rolePersonIdStr = logic.getRolePersonIds(roleIdStr, dbConn);
				}
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
				if (allpersonStr != null && !"".equals(allpersonStr)) {
					mobileSms.remindByMobileSms(dbConn, allpersonStr, loginUserSeqId, mobileSmsContent, null);
				}

			} else if (!"".equals(mobileSmsPerson.trim())) {
				mobileSms.remindByMobileSms(dbConn, mobileSmsPerson, loginUserSeqId, mobileSmsContent, null);
			}
			request.setAttribute("diskPath", path);
			request.setAttribute("seqId", seqIdStr);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/funcs/netdisk/uploadResult.jsp";
	}

	/**
	 * 通过id获取该共享目录的所有权限信息	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getPrivInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		YHNetDiskLogic logic = new YHNetDiskLogic();
		String pathId = request.getParameter("DISK_ID");
		String seqId = request.getParameter("seqId");
		// 获取登录用户信息
		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();

		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHNetDiskLogic diskLogic = new YHNetDiskLogic();

			// YHNetdisk diNetdisk = logic.getFileSortInfoById(dbConn, seqId);

			Map map = new HashMap();
			map.put("SEQ_ID", Integer.parseInt(seqId));
			String data = logic.getVisiPriv(dbConn, map, loginUserSeqId, loginUserDeptId, loginUserRoleId ,loginUser.getUserPrivOther() , loginUser.getDeptIdOther() , pathId);

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 根据seqId得到路径
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getFiilePathBySeqId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");

		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}

		YHNetDiskLogic logic = new YHNetDiskLogic();
		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			YHNetdisk netdisk = logic.getNetdiskInfoById(dbConn, seqId);
			String diskPath = netdisk.getDiskPath() == null ? "" : netdisk.getDiskPath();

			String data = "{folderPath:\"" + diskPath + "\" }";

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 全文检索
	 * 
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String queryNetdiskInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId"); // 5
		String pathIdStr = URLDecoder.decode(request.getParameter("DISK_ID"), "UTF-8"); // d:/bjfaoitc/~!@#$%^/aaacccwwww
		String fileNameStr = URLDecoder.decode(request.getParameter("fileName"), "UTF-8"); // aaaa
		String keyStr = request.getParameter("key"); // cccc

		int seqId = 0;
		if (seqIdStr != null && !"".equals(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}

		if (pathIdStr == null) {
			pathIdStr = "";
		}

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("seqId", seqId);
		map.put("pathIdStr", pathIdStr);
		map.put("fileNameStr", fileNameStr);
		map.put("keyStr", keyStr);

		YHPerson loginUser = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
		YHNetDiskLogic logic = new YHNetDiskLogic();

		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			boolean isHave = false;
			StringBuffer buffer = new StringBuffer("[");

			List<Map<Object, Object>> fileContentsList = logic.getGlobalFileContentsByList(dbConn, map, loginUser);

			if (fileContentsList != null && fileContentsList.size() != 0) {

				for (Map<Object, Object> fileContentMap : fileContentsList) {

					int dbSeqId = (Integer) fileContentMap.get("dbSeqId");
					String urlString = (String) fileContentMap.get("urlString");
					String filePath = (String) fileContentMap.get("filePath");
					String absolutePath = (String) fileContentMap.get("absolutePath");
					int officeFlag = (Integer) fileContentMap.get("officeFlag");
					String fileName = (String) fileContentMap.get("fileName");
					String fileSize = (String) fileContentMap.get("fileSize");
					String fileModifyTime = (String) fileContentMap.get("fileModifyTime");
					String typeName = (String) fileContentMap.get("typeName");

					int userPriv = (Integer) fileContentMap.get("userPriv");
					int managePriv = (Integer) fileContentMap.get("managePriv");
					int downPriv = (Integer) fileContentMap.get("downPriv");

					buffer.append("{");

					buffer.append("dbSeqId:\"" + dbSeqId + "\"");
					buffer.append(",urlString:\"" + urlString + "\"");
					buffer.append(",filePath:\"" + filePath + "\"");
					buffer.append(",absolutePath:\"" + absolutePath + "\"");
					buffer.append(",officeFlag:\"" + officeFlag + "\"");
					buffer.append(",fileName:\"" + fileName + "\"");
					buffer.append(",fileSize:\"" + fileSize + "\"");
					buffer.append(",fileModifyTime:\"" + fileModifyTime + "\"");
					buffer.append(",typeName:\"" + typeName + "\"");

					buffer.append(",userPriv:" + userPriv);
					buffer.append(",managePriv:" + managePriv);
					buffer.append(",downPriv:" + downPriv);

					buffer.append("},");
					isHave = true;

				}
				if (isHave) {
					buffer.deleteCharAt(buffer.length() - 1);
				}
				buffer.append("]");

			} else {
				buffer.append("]");
			}

			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, buffer.toString());
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}

		return "/core/inc/rtjson.jsp";
	}

	/**
	 * 取得要显示的路径名	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String showPathName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String seqIdStr = request.getParameter("seqId");
		String fromDiskPath = request.getParameter("diskPath");
		int seqId = 0;
		if (seqIdStr != null) {
			seqId = Integer.parseInt(seqIdStr);
		}
		if (YHUtility.isNullorEmpty(fromDiskPath)) {
			fromDiskPath = "";
		}
		YHNetDiskLogic logic = new YHNetDiskLogic();
		Connection dbConn;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();

			if (fromDiskPath.trim().replace("\\", "/").indexOf("/") != -1) {
				fromDiskPath = fromDiskPath.trim().substring(fromDiskPath.indexOf("/") + 1, fromDiskPath.length());

				if (!fromDiskPath.trim().endsWith("/")) {
					fromDiskPath += "/";
				}
			}
			YHNetdisk netdisk = logic.getNetdiskInfoById(dbConn, seqId);
			String diskName ="";
			if (netdisk!=null) {
				diskName=YHUtility.null2Empty(netdisk.getDiskName());
			}
			String returnName = diskName + "/" + fromDiskPath;
			String data = "{diskPathNameStr:\"" + YHUtility.encodeSpecial(returnName) + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, "要显示的路径名失败!");
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 读取txt/text文件内容
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getInfoFromText(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filePath = request.getParameter("filePath");
		StringBuffer buffer = new StringBuffer();
		try {
			YHNetDiskLogic logic = new YHNetDiskLogic();
			
			String str = logic.getInfoFromTextLogic(buffer,filePath);
			String data = "{textData:\""+  YHUtility.encodeHtml(str) + "\"}";
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
			request.setAttribute(YHActionKeys.RET_DATA, data);
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 * 保存txt/text文件内容
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String saveInfoToText(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filePath = request.getParameter("filePath");
		String textContent = request.getParameter("textContent");
		if (YHUtility.isNullorEmpty(filePath)) {
			filePath = "";
		}
		if (YHUtility.isNullorEmpty(textContent)) {
			textContent = "";
		}
		try {
//			YHNetDiskLogic logic = new YHNetDiskLogic();
			YHFileUtility.storBytes2File(filePath, textContent.getBytes("gbk") );
			
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
		} catch (Exception ex) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		}
		return "/core/inc/rtjson.jsp";
	}
	
	
	
	
	
	

}
