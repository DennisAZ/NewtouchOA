/**
 * 
 */
package yh.core.funcs.netdisk.logic;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.netdisk.data.YHNetdisk;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.portal.util.YHPortalProducer;
import yh.core.funcs.portal.util.rules.YHImgRule;
import yh.core.funcs.portal.util.rules.YHLinkRule;
import yh.core.funcs.system.filefolder.logic.YHFileSortLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;

/**
 * @author Administrator
 * 
 */
public class YHNetDiskLogic {

	public void saveFileFolder(Connection dbConn, YHNetdisk netDisk) {
		YHORM orm = new YHORM();
		try {
			orm.saveSingle(dbConn, netDisk);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<YHNetdisk> getNetDisksInfo(Connection dbConn) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHNetdisk.class, new HashMap());
	}

	public YHNetdisk getFileSortInfoById(Connection dbConn, String seqId) throws NumberFormatException, Exception {
		YHORM orm = new YHORM();
		if (seqId == null) {
			seqId = "0";
		}
		return (YHNetdisk) orm.loadObjSingle(dbConn, YHNetdisk.class, Integer.parseInt(seqId));
	}

	public void updateFileSortInfoById(Connection dbConn, YHNetdisk fileSort) throws Exception {
		YHORM orm = new YHORM();
		orm.updateSingle(dbConn, fileSort);
	}

	public void delFileSortInfoById(Connection dbConn, YHNetdisk fileSort) throws Exception {
		YHORM orm = new YHORM();
		orm.deleteSingle(dbConn, fileSort);
	}

	public List<YHNetdisk> getNetDisks(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		return (List<YHNetdisk>) orm.loadListSingle(dbConn, YHNetdisk.class, map);
	}

	public int isHaveChild(File file) throws Exception {
		int is = 0;
		File[] files = file.listFiles();
		for (File file2 : files) {
			if (file.isDirectory()) {
				is = 1;
			}
		}
		return 0;
	}

	/**
	 * 得到访问人员的id字符串
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
		YHNetdisk netdisk = (YHNetdisk) orm.loadObjSingle(dbConn, YHNetdisk.class, map);
		return this.getUserIds(dbConn, netdisk, action);
	}
	public String getUserIds(Connection dbConn, YHNetdisk netdisk, String action) throws Exception {
    YHORM orm = new YHORM();
    String ids = "";
    if (netdisk != null) {
      if ("USER_ID".equals(action)) {
        if (!"".equals(netdisk.getUserId()) && netdisk.getUserId() != null) {
          String idString = YHUtility.null2Empty(netdisk.getUserId());
          String[] idsStrings = idString.split("\\|");
          if (idsStrings.length == 2) {
            ids = "";
          } else if (idsStrings.length == 1) {
            ids = "";

          } else {
            ids = idsStrings[2];
          }
        }
      } else if ("MANAGE_USER".equals(action)) {
        if (!"".equals(netdisk.getManageUser()) && netdisk.getManageUser() != null) {
          String idString = YHUtility.null2Empty(netdisk.getManageUser());
          String[] idsStrings = idString.split("\\|");
          if (idsStrings.length == 2) {
            ids = "";
          } else if (idsStrings.length == 1) {
            ids = "";

          } else {
            ids = idsStrings[2];
          }
        }
      } else if ("NEW_USER".equals(action)) {
        if (!"".equals(netdisk.getNewUser()) && netdisk.getNewUser() != null) {
          String idString = YHUtility.null2Empty(netdisk.getNewUser());
          String[] idsStrings = idString.split("\\|");
          if (idsStrings.length == 2) {
            ids = "";
          } else if (idsStrings.length == 1) {
            ids = "";

          } else {
            ids = idsStrings[2];
          }
        }
      } else if ("DOWN_USER".equals(action)) {
        if (!"".equals(netdisk.getDownUser()) && netdisk.getDownUser() != null) {
          String idString = YHUtility.null2Empty(netdisk.getDownUser());
          String[] idsStrings = idString.split("\\|");
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

    // System.out.println("ids=====" + ids);
    return ids;
  }

	/**
	 * 得到角色人员的id字符串
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
		YHNetdisk netdisk = (YHNetdisk) orm.loadObjSingle(dbConn, YHNetdisk.class, map);
		return this.getRoleIds(dbConn, netdisk, action);
	}
	public String getRoleIds(Connection dbConn, YHNetdisk netdisk , String action) throws Exception {
    YHORM orm = new YHORM();
    String ids = "";
    if (netdisk != null) {
      if ("USER_ID".equals(action)) {
        if (!"".equals(netdisk.getUserId()) && netdisk.getUserId() != null) {
          String idString = YHUtility.null2Empty(netdisk.getUserId());
          String[] idsStrings = idString.split("\\|");
          if (idsStrings.length == 1) {
            ids = "";
          } else {
            ids = idsStrings[1];
          }
        }
      } else if ("MANAGE_USER".equals(action)) {
        if (!"".equals(netdisk.getManageUser()) && netdisk.getManageUser() != null) {
          String idString = YHUtility.null2Empty(netdisk.getManageUser());
          String[] idsStrings = idString.split("\\|");
          if (idsStrings.length == 1) {
            ids = "";
          } else {
            ids = idsStrings[1];
          }
        }
      } else if ("NEW_USER".equals(action)) {
        if (!"".equals(netdisk.getNewUser()) && netdisk.getNewUser() != null) {
          String idString = YHUtility.null2Empty(netdisk.getNewUser());
          String[] idsStrings = idString.split("\\|");
          if (idsStrings.length == 1) {
            ids = "";
          } else {
            ids = idsStrings[1];
          }
        }
      } else if ("DOWN_USER".equals(action)) {
        if (!"".equals(netdisk.getDownUser()) && netdisk.getDownUser() != null) {
          String idString = YHUtility.null2Empty(netdisk.getDownUser());
          String[] idsStrings = idString.split("\\|");
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
	 * 得到部门人员的id字符串
	 * 
	 * 
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
		YHNetdisk netdisk = (YHNetdisk) orm.loadObjSingle(dbConn, YHNetdisk.class, map);
		return this.getDeptIds(dbConn, netdisk, action);
	}
	public String getDeptIds(Connection dbConn, YHNetdisk netdisk, String action) throws Exception {
    YHORM orm = new YHORM();
    String ids = "";
    if (netdisk != null) {
      if ("USER_ID".equals(action)) {
        if (!"".equals(netdisk.getUserId()) && netdisk.getUserId() != null) {
          String idString = YHUtility.null2Empty(netdisk.getUserId());
          String[] idsStrings = idString.split("\\|");
          if (idsStrings[0].length() != 0) {
            ids = idsStrings[0];
          }
        }
      } else if ("MANAGE_USER".equals(action)) {
        if (!"".equals(netdisk.getManageUser()) && netdisk.getManageUser() != null) {
          String idString = YHUtility.null2Empty(netdisk.getManageUser());
          String[] idsStrings = idString.split("\\|");
          if (idsStrings[0].length() != 0) {
            ids = idsStrings[0];
          }
        }
      } else if ("NEW_USER".equals(action)) {
        if (!"".equals(netdisk.getNewUser()) && netdisk.getNewUser() != null) {
          String idString = YHUtility.null2Empty(netdisk.getNewUser());
          String[] idsStrings = idString.split("\\|");
          if (idsStrings[0].length() != 0) {
            ids = idsStrings[0];
          }
        }
      } else if ("DOWN_USER".equals(action)) {
        if (!"".equals(netdisk.getDownUser()) && netdisk.getDownUser() != null) {
          String idString = YHUtility.null2Empty(netdisk.getDownUser());
          String[] idsStrings = idString.split("\\|");
          if (idsStrings[0].length() != 0) {
            ids = idsStrings[0];
          }
        }
      }

    }
    return ids;
  }
	/**
	 * 获取访问权限
	 * 
	 * @param dbConn
	 * @param map
	 * @param loginUserSeqId
	 * @param loginUserDeptId
	 * @param loginUserRoleId
	 * @return
	 * @throws Exception
	 */
	public String getVisiPriv(Connection dbConn, Map map, int loginUserSeqId, int loginUserDeptId, String loginUserRoleId , String otherUserPriv , String otherDept, String pathId)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		int visiPrivFlag = 0;
		int managePrivFlag = 0;
		int newPrivFlag = 0;
		int downPrivFlag = 0;
		int id = 0;
		YHORM orm = new YHORM();
		YHNetdisk netdisk = (YHNetdisk) orm.loadObjSingle(dbConn, YHNetdisk.class, map);
		String diskPath = YHUtility.null2Empty(netdisk.getDiskPath());
		File file2 = new File(diskPath);

		pathId = pathId.replace('\\', '/'); // D:\aa\\
		File file = new File(pathId); // D:\aa

		int rootDir = 0;
		if (file2.getPath().equals(file.getPath())) {
			rootDir = 1;
		}

		if (netdisk != null) {
			sb.append("[");
			id = netdisk.getSeqId();
			String[] actions = new String[] { "USER_ID", "MANAGE_USER", "NEW_USER", "DOWN_USER" };
			for (int i = 0; i < actions.length; i++) {
				if ("USER_ID".equals(actions[i])) {
					String userPrivs = this.getUserIds(dbConn, netdisk, "USER_ID");
					String rolePrivs = this.getRoleIds(dbConn, netdisk, "USER_ID");
					String deptPrivs = this.getDeptIds(dbConn, netdisk, "USER_ID");

					YHFileSortLogic sortLogic = new YHFileSortLogic();
					boolean userFlag = sortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = sortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = sortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					
					boolean otherPrivFlag = !"".equals(YHWorkFlowUtility.checkId(otherUserPriv, rolePrivs, true))  ;
					boolean otherDeptFlag = !"".equals(YHWorkFlowUtility.checkId(otherDept, deptPrivs, true))  ;
					if (userFlag || deptFlag || roleFlag || otherPrivFlag ||otherDeptFlag ) {
						visiPrivFlag = 1;
					}
				}
				if ("MANAGE_USER".equals(actions[i])) {
					String userPrivs = this.getUserIds(dbConn, netdisk, "MANAGE_USER");
					String rolePrivs = this.getRoleIds(dbConn, netdisk, "MANAGE_USER");
					String deptPrivs = this.getDeptIds(dbConn, netdisk, "MANAGE_USER");

					YHFileSortLogic sortLogic = new YHFileSortLogic();
					boolean userFlag = sortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = sortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = sortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					boolean otherPrivFlag = !"".equals(YHWorkFlowUtility.checkId(otherUserPriv, rolePrivs, true))  ;
          boolean otherDeptFlag = !"".equals(YHWorkFlowUtility.checkId(otherDept, deptPrivs, true))  ;
          if (userFlag || deptFlag || roleFlag || otherPrivFlag ||otherDeptFlag ) {
            managePrivFlag = 1;
          }
				}
				if ("NEW_USER".equals(actions[i])) {
					String userPrivs = this.getUserIds(dbConn, netdisk, "NEW_USER");
					String rolePrivs = this.getRoleIds(dbConn, netdisk, "NEW_USER");
					String deptPrivs = this.getDeptIds(dbConn, netdisk, "NEW_USER");

					YHFileSortLogic sortLogic = new YHFileSortLogic();
					boolean userFlag = sortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = sortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = sortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					boolean otherPrivFlag = !"".equals(YHWorkFlowUtility.checkId(otherUserPriv, rolePrivs, true))  ;
          boolean otherDeptFlag = !"".equals(YHWorkFlowUtility.checkId(otherDept, deptPrivs, true))  ;
          if (userFlag || deptFlag || roleFlag || otherPrivFlag ||otherDeptFlag ) {
            newPrivFlag = 1;
          }
					
				}
				if ("DOWN_USER".equals(actions[i])) {
					String userPrivs = this.getUserIds(dbConn, netdisk, "DOWN_USER");
					String rolePrivs = this.getRoleIds(dbConn, netdisk, "DOWN_USER");
					String deptPrivs = this.getDeptIds(dbConn, netdisk, "DOWN_USER");

					YHFileSortLogic sortLogic = new YHFileSortLogic();
					boolean userFlag = sortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = sortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = sortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					boolean otherPrivFlag = !"".equals(YHWorkFlowUtility.checkId(otherUserPriv, rolePrivs, true))  ;
          boolean otherDeptFlag = !"".equals(YHWorkFlowUtility.checkId(otherDept, deptPrivs, true))  ;
          if (userFlag || deptFlag || roleFlag || otherPrivFlag ||otherDeptFlag ) {
            downPrivFlag = 1;
          }
				}
			}
			if (managePrivFlag == 1) {
				downPrivFlag = 1;
				visiPrivFlag = 1;
			}
			sb.append("{");
			sb.append("visiPriv:\"" + visiPrivFlag + "\"");
			sb.append(",managePriv:\"" + managePrivFlag + "\"");
			sb.append(",newPriv:\"" + newPrivFlag + "\"");
			sb.append(",downPriv:\"" + downPrivFlag + "\"");
			sb.append(",rootDir:\"" + rootDir + "\"");
			sb.append(",seqId:\"" + id + "\"");
			sb.append("}");
			sb.append("]");
		} else {
			sb.append("[]");
		}

		return sb.toString();
	}

	/**
	 * 获取文件类型名
	 * 
	 * 
	 * 
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	public String getFileTypeName(String fileType) throws Exception {
		String typeName = "-";
		String[] typeArray = { "txt","text", "jpeg", "jpg", "doc", "docx", "xls", "xlsx", "ppt", "bmp", "db", "vsd", "html", "exe", "rar", "iso", "avi", "ini",
				"xml", "Zip", "gif", "png" };
		try {
			if (fileType != null && !"".equals(fileType)) {
				for (int i = 0; i < typeArray.length; i++) {
					if ("txt".equalsIgnoreCase(fileType) || "text".equalsIgnoreCase(fileType)) {
						typeName = "文本文件";
					}
					if ("db".equalsIgnoreCase(fileType)) {
						typeName = "Data Base 文件";
					}
					if ("doc".equalsIgnoreCase(fileType)) {
						typeName = "Word 97-2003 文档";
					}

					if ("docx".equalsIgnoreCase(fileType)) {
						typeName = "Word 2007 文档";
					}

					if ("jpg".equalsIgnoreCase(fileType) || "jpeg".equalsIgnoreCase(fileType)) {
						typeName = "JPEG 图象";
					}
					if ("xlsx".equalsIgnoreCase(fileType)) {
						typeName = "Excel 2007 工作表";
					}
					if ("vsd".equalsIgnoreCase(fileType)) {
						typeName = "Visio 97-2003 文件";
					}
					if ("html".equalsIgnoreCase(fileType)) {
						typeName = "HTML 网页文件";
					}
					if ("xls".equalsIgnoreCase(fileType)) {
						typeName = "Excel 97-2003 工作表";
					}
					if ("bmp".equalsIgnoreCase(fileType)) {
						typeName = "BMP 图像";
					}
					if ("exe".equalsIgnoreCase(fileType)) {
						typeName = "应用程序";
					}
					if ("rar".equalsIgnoreCase(fileType)) {
						typeName = "RAR 压缩文件";
					}
					if ("iso".equalsIgnoreCase(fileType)) {
						typeName = "ISO 光盘映像文件";
					}
					if ("avi".equalsIgnoreCase(fileType)) {
						typeName = "Windows Media 音频/视频文件";
					}
					if ("ini".equalsIgnoreCase(fileType)) {
						typeName = "配置设置";
					}
					if ("xml".equalsIgnoreCase(fileType)) {
						typeName = "XML 文档";
					}
					if ("ppt".equalsIgnoreCase(fileType)) {
						typeName = "Powerpoint 97-2003 演示文稿";
					}
					if ("Zip".equalsIgnoreCase(fileType)) {
						typeName = "Zip 压缩文件";
					}
					if ("gif".equalsIgnoreCase(fileType)) {
						typeName = "GIF 图像";
					}
					if ("png".equalsIgnoreCase(fileType)) {
						typeName = "PNG 图像";
					}

				}

			}

		} catch (Exception e) {
			throw e;
		}
		return typeName;
	}

	/**
	 *获取文件夹大小
	 * 
	 * 
	 * 
	 * @param f
	 * @return
	 */
	public long getFolderSize(File f) {
		// String isImage = "^.*?(\\.(png|gif|jpg|bmp|PNG|GIF|JPG|BMP))$";
		String ispath = "[a-zA-Z]:\\\\$";
		long size = 0;
		if (f.getPath().trim().matches(ispath)) {
			size = f.getTotalSpace();
		} else {
			File flist[] = f.listFiles();
			if (flist != null) {
				for (int i = 0; i < flist.length; i++) {
					if (flist[i].isDirectory()) {
						size = size + getFolderSize(flist[i]);
					} else {
						size = size + flist[i].length();
					}
				}

			}

		}
		return size;
	}

	/**
	 * M转换成B
	 * 
	 * @param size
	 * @return
	 */
	public long transFolderSize(int size) {
		long result = 0;
		if (size != 0) {
			result = (long) size * (1024 * 1024);
		}
		return result;

	}

	/**
	 * 转换大小
	 * 
	 * @param size
	 * @return
	 */
	public String transformSize(long size) {
		DecimalFormat df = new DecimalFormat("#.0");

		String result = new String();
		if (size >= 0 && size < 1024) {
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
			result = "errorData";
		}
		return result;
	}

	/**
	 * 根据人员id字符串得到name字符串
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public String getPersonNamesByIds(Connection conn, String ids) throws Exception {
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
	 * 得到有权限的部门id串
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param loginUserDeptId
	 * @param ids
	 * @return
	 */
	public String getPrivDeptIdStr(Connection dbConn, int loginUserDeptId, String ids) {
		String idstr = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp.trim()) && !"0".equals(tmp.trim())) {
					if (Integer.parseInt(tmp) == loginUserDeptId) {
						idstr += tmp + ",";
					}
				} else if (!"".equals(tmp.trim()) && "0".equals(tmp.trim())) {

					idstr += tmp + ",";

				}
			}
		}

		return idstr.trim();
	}

	/**
	 * 得到有权限的角色id串(有权限)
	 * 
	 * @param dbConn
	 * @param loginUserDeptId
	 * @param ids
	 * @return
	 */
	public String getPrivRoleIdStr(Connection dbConn, int loginUserRoleId, String ids) {
		String idstr = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp)) {
					if (Integer.parseInt(tmp) == loginUserRoleId) {
						idstr += tmp;
					}
				}
			}
		}

		return idstr.trim();
	}

	/**
	 * 得到该部门下的全体人员id串(无权限)
	 * 
	 * @param dbConn
	 * @param deptIdStr
	 * @return
	 * @throws Exception
	 */
	public String getDeptPersonIds(String ids, Connection dbConn) throws Exception {
		String idStr = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp.trim()) && !"0".equals(tmp.trim())) {
					Map deptIdmMap = new HashMap();
					deptIdmMap.put("DEPT_ID", tmp);
					List<YHPerson> list = this.getPersonsByDeptId(dbConn, deptIdmMap);
					if (list != null && list.size() != 0) {
						for (YHPerson person : list) {
							idStr += person.getSeqId() + ",";
						}
					}
				} else if (!"".equals(tmp.trim()) && "0".equals(tmp.trim())) {
					String[] filters = { "dept_id!=0 and not_login=0" };
					List<YHPerson> list = this.getPersonsByDeptIdStr(dbConn, filters);
					if (list != null && list.size() != 0) {
						for (YHPerson person : list) {
							idStr += person.getSeqId() + ",";
						}
					}
				}
			}
		}
		return idStr.trim();
	}

	/**
	 * 得到该部门下的全体人员id串
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param deptIdStr
	 * @return
	 * @throws Exception
	 */
	public String getDeptPersonIdStr(int userSeqId, String ids, Connection dbConn) throws Exception {
		String idStr = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp.trim()) && !"0".equals(tmp.trim())) {
					if (Integer.parseInt(tmp) == userSeqId) {

						Map deptIdmMap = new HashMap();
						deptIdmMap.put("DEPT_ID", tmp);
						List<YHPerson> list = this.getPersonsByDeptId(dbConn, deptIdmMap);

						if (list != null && list.size() != 0) {
							for (YHPerson person : list) {
								idStr += person.getSeqId() + ",";
							}

						}
					}
				} else if (!"".equals(tmp.trim()) && "0".equals(tmp.trim())) {
					String[] filters = { "dept_id!=0 and not_login=0" };
					List<YHPerson> list = this.getPersonsByDeptIdStr(dbConn, filters);

					if (list != null && list.size() != 0) {
						for (YHPerson person : list) {
							idStr += person.getSeqId() + ",";
						}

					}

				}
			}
		}

		return idStr.trim();
	}

	public List<YHPerson> getPersonsByDeptId(Connection dbConn, Map deptIdmMap) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListComplex(dbConn, YHPerson.class, deptIdmMap);
	}

	public List<YHPerson> getPersonsByDeptIdStr(Connection dbConn, String[] filters) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHPerson.class, filters);
	}

	/**
	 * 得到该角色下的全体人员id串
	 * 
	 * @param dbConn
	 * @param deptIdStr
	 * @return
	 * @throws Exception
	 */
	public String getRolePersonIdStr(int userSeqId, String ids, Connection dbConn) throws Exception {
		String idStr = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp)) {
					if (Integer.parseInt(tmp) == userSeqId) {
						Map deptIdmMap = new HashMap();
						deptIdmMap.put("USER_PRIV  ", tmp);
						List<YHPerson> list = this.getPersonsByDeptId(dbConn, deptIdmMap);
						if (list != null && list.size() != 0) {
							for (YHPerson person : list) {
								idStr += person.getSeqId() + ",";
							}
						}
					}
				}
			}
		}
		return idStr.trim();
	}

	/**
	 * 得到该角色下的全体人员id串(无权限)
	 * 
	 * @param dbConn
	 * @param deptIdStr
	 * @return
	 * @throws Exception
	 */
	public String getRolePersonIds(String ids, Connection dbConn) throws Exception {
		String idStr = "";
		if (ids != null && !"".equals(ids)) {
			String[] aId = ids.split(",");
			for (String tmp : aId) {
				if (!"".equals(tmp)) {
					Map deptIdmMap = new HashMap();
					deptIdmMap.put("USER_PRIV  ", tmp);
					List<YHPerson> list = this.getPersonsByDeptId(dbConn, deptIdmMap);
					if (list != null && list.size() != 0) {
						for (YHPerson person : list) {
							idStr += person.getSeqId() + ",";
						}
					}
				}
			}
		}
		return idStr.trim();
	}

	public YHNetdisk getNetdiskInfoById(Connection dbConn, int seqId) throws Exception {
		YHORM orm = new YHORM();
		return (YHNetdisk) orm.loadObjSingle(dbConn, YHNetdisk.class, seqId);

	}

	/**
	 * 判断是否为office文件
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public boolean is_office(String fileType) throws Exception {
		boolean flag = false;
		try {
			if (fileType != null && !"".equals(fileType.trim())) {
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
	 * 复制中，文件夹里文件已存在的处理
	 * 
	 * @param srcPath
	 * @param desPath
	 * @param fileName
	 * @param nameTitle
	 * @param fileType
	 * @throws Exception
	 */
	public void copyEexistsFile(String srcPath, String desPath, String fileName, String nameTitle, String fileType) throws Exception {
		try {
			String temp = nameTitle + " - 复件";
			String subjectSuffix = temp;
			String newFileStr = desPath + "/" + subjectSuffix.trim() + fileType.trim();
			File newFile = new File(newFileStr);
			int repeat = 1;
			while (newFile.exists()) {
			  repeat++;
			  subjectSuffix = temp + "(" + repeat + ")";
			  newFileStr = desPath + "/" + subjectSuffix.trim() + fileType.trim();
			  newFile = new File(newFileStr);
			}
			YHFileUtility.copyFile(srcPath + "/" + fileName, newFile.getAbsolutePath());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 剪切中，文件夹里文件已存在的处理
	 * 
	 * @param srcPath
	 * @param desPath
	 * @param fileName
	 * @param nameTitle
	 * @param fileType
	 * @throws Exception
	 */
	public void cutEexistsFile(String srcPath, String desPath, String fileName, String nameTitle, String fileType) throws Exception {

		try {
			String newFileName = nameTitle + " - 复件";
			String newFileStr = desPath + "/" + newFileName.trim() + fileType.trim();
			File newFile = new File(newFileStr);
			if (newFile!=null) {
				if (!newFile.exists()) {
					YHFileUtility.xcopyFile(srcPath + "/" + fileName, newFile.getAbsolutePath());
				} else {
					cutEexistsFile(srcPath, desPath, fileName, newFileName.trim(), fileType.trim());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 上传文件中，文件夹里文件已存在的处理
	 * @param desPath
	 * @param fileName
	 * @param nameTitle
	 * @param fileType
	 * @throws Exception
	 */
	public void uploadEexistsFile(StringBuffer buffer, String desPath, String fileName, String nameTitle, String fileType) throws Exception {
		String str = "";
		try {
			String newFileName = nameTitle + " - 复件";
			String newFileStr = desPath + "/" + newFileName.trim() + fileType.trim();
			File newFile = new File(newFileStr);
			if (newFile!=null) {
				if (!newFile.exists()) {
					str = newFile.getName();
					buffer.append(str);
				} else {
					uploadEexistsFile(buffer,desPath, fileName, newFileName.trim(), fileType.trim());
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 删除指定文件或者目录,
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void deleteAllToRecycle(File file, String recyclePath) throws Exception {
		YHInnerEMailUtilLogic emut = new YHInnerEMailUtilLogic();

		if (file.isFile()) {
			String rand = emut.getRandom();
			String fileName = rand + "_" + file.getName();
			YHFileUtility.xcopyFile(file.getAbsolutePath(), recyclePath + File.separator + fileName);
			return;
		}
		File[] fileList = file.listFiles();
		for (int i = 0; fileList != null && i < fileList.length; i++) {
			deleteAllToRecycle(fileList[i], recyclePath);
		}
		file.delete(); // 遍历完一个文件夹后删除该文件夹
	}

	/**
	 * 取得访问权限（只取userId下的权限）
	 * 
	 * 
	 * @param dbConn
	 * @param loginUser
	 * @param fileSort
	 * @return
	 * @throws Exception
	 */
	public boolean getUserIdAccessPriv(Connection dbConn, YHPerson loginUser, YHNetdisk netdisk) throws Exception {
		boolean returnFlag = false;
		YHFileSortLogic sortLogic = new YHFileSortLogic();

		// 获取登录用户信息
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String userDeptOther = YHUtility.null2Empty(loginUser.getDeptIdOther());
		String userPrivOther = YHUtility.null2Empty(loginUser.getUserPrivOther());
		
		String loginUserRoleId = loginUser.getUserPriv();
		String[] actions = new String[] { "USER_ID" };

		try {
			for (int i = 0; i < actions.length; i++) {
				if ("USER_ID".equals(actions[i])) {
					String userPrivs = this.getUserIdString(dbConn, netdisk, "USER_ID");
					String rolePrivs = this.getRoleIdString(dbConn, netdisk, "USER_ID");
					String deptPrivs = this.getDeptIdString(dbConn, netdisk, "USER_ID");

					boolean userFlag = sortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = sortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = sortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					boolean roleOtherFlag = sortLogic.getRoleIdOtherStr(userPrivOther, rolePrivs);
					boolean deptOtherFlag = sortLogic.getDeptOtherStr(userDeptOther, deptPrivs);

					if (userFlag || deptFlag || roleFlag || roleOtherFlag || deptOtherFlag) {
						returnFlag = true;
					}
				}

			}

		} catch (Exception e) {
			throw e;
		}

		return returnFlag;
	}

	/**
	 * 取得管理权限（只取MANAGE_USER下的权限）
	 * 
	 * 
	 * @param dbConn
	 * @param loginUser
	 * @param fileSort
	 * @return
	 * @throws Exception
	 */
	public boolean getManageAccessPriv(Connection dbConn, YHPerson loginUser, YHNetdisk netdisk) throws Exception {
		boolean returnFlag = false;
		YHFileSortLogic sortLogic = new YHFileSortLogic();

		// 获取登录用户信息
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();
		String userDeptOther = YHUtility.null2Empty(loginUser.getDeptIdOther());
    String userPrivOther = YHUtility.null2Empty(loginUser.getUserPrivOther());
		String[] actions = new String[] { "MANAGE_USER" };

		try {
			for (int i = 0; i < actions.length; i++) {
				if ("MANAGE_USER".equals(actions[i])) {
					String userPrivs = this.getUserIdString(dbConn, netdisk, "MANAGE_USER");
					String rolePrivs = this.getRoleIdString(dbConn, netdisk, "MANAGE_USER");
					String deptPrivs = this.getDeptIdString(dbConn, netdisk, "MANAGE_USER");
					
			    
					
					boolean userFlag = sortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = sortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = sortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					boolean roleOtherFlag = sortLogic.getRoleIdOtherStr(userPrivOther, rolePrivs);
          boolean deptOtherFlag = sortLogic.getDeptOtherStr(userDeptOther, deptPrivs);

          if (userFlag || deptFlag || roleFlag || roleOtherFlag || deptOtherFlag) {
            returnFlag = true;
          }
				}

			}

		} catch (Exception e) {
			throw e;
		}

		return returnFlag;
	}

	/**
	 * 取得下载权限（只取DOWN_USER下的权限）
	 * 
	 * 
	 * @param dbConn
	 * @param loginUser
	 * @param fileSort
	 * @return
	 * @throws Exception
	 */
	public boolean getDownAccessPriv(Connection dbConn, YHPerson loginUser, YHNetdisk netdisk) throws Exception {
		boolean returnFlag = false;
		YHFileSortLogic sortLogic = new YHFileSortLogic();

		// 获取登录用户信息
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String userDeptOther = YHUtility.null2Empty(loginUser.getDeptIdOther());
    String userPrivOther = YHUtility.null2Empty(loginUser.getUserPrivOther());
    
		String loginUserRoleId = loginUser.getUserPriv();
		String[] actions = new String[] { "DOWN_USER" };

		try {
			for (int i = 0; i < actions.length; i++) {
				if ("DOWN_USER".equals(actions[i])) {
					String userPrivs = this.getUserIdString(dbConn, netdisk, "DOWN_USER");
					String rolePrivs = this.getRoleIdString(dbConn, netdisk, "DOWN_USER");
					String deptPrivs = this.getDeptIdString(dbConn, netdisk, "DOWN_USER");

					boolean userFlag = sortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = sortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = sortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					boolean roleOtherFlag = sortLogic.getRoleIdOtherStr(userPrivOther, rolePrivs);
          boolean deptOtherFlag = sortLogic.getDeptOtherStr(userDeptOther, deptPrivs);

          if (userFlag || deptFlag || roleFlag || roleOtherFlag || deptOtherFlag) {
            returnFlag = true;
          }
				}

			}

		} catch (Exception e) {
			throw e;
		}

		return returnFlag;
	}

	/**
	 * 取得下载权限（只取NEW_USER下的权限）
	 * 
	 * 
	 * @param dbConn
	 * @param loginUser
	 * @param fileSort
	 * @return
	 * @throws Exception
	 */
	public boolean getNewAccessPriv(Connection dbConn, YHPerson loginUser, YHNetdisk netdisk) throws Exception {
		boolean returnFlag = false;
		YHFileSortLogic sortLogic = new YHFileSortLogic();

		// 获取登录用户信息
		int loginUserSeqId = loginUser.getSeqId();
		int loginUserDeptId = loginUser.getDeptId();
		String loginUserRoleId = loginUser.getUserPriv();
		String userDeptOther = YHUtility.null2Empty(loginUser.getDeptIdOther());
    String userPrivOther = YHUtility.null2Empty(loginUser.getUserPrivOther());
		String[] actions = new String[] { "NEW_USER" };

		try {
			for (int i = 0; i < actions.length; i++) {
				if ("NEW_USER".equals(actions[i])) {
					String userPrivs = this.getUserIdString(dbConn, netdisk, "NEW_USER");
					String rolePrivs = this.getRoleIdString(dbConn, netdisk, "NEW_USER");
					String deptPrivs = this.getDeptIdString(dbConn, netdisk, "NEW_USER");

					boolean userFlag = sortLogic.getUserIdStr(loginUserSeqId, userPrivs, dbConn);
					boolean deptFlag = sortLogic.getDeptIdStr(loginUserDeptId, deptPrivs, dbConn);
					boolean roleFlag = sortLogic.getRoleIdStr(loginUserRoleId, rolePrivs, dbConn);
					boolean roleOtherFlag = sortLogic.getRoleIdOtherStr(userPrivOther, rolePrivs);
          boolean deptOtherFlag = sortLogic.getDeptOtherStr(userDeptOther, deptPrivs);

          if (userFlag || deptFlag || roleFlag || roleOtherFlag || deptOtherFlag) {
            returnFlag = true;
          }
				}

			}

		} catch (Exception e) {
			throw e;
		}

		return returnFlag;
	}

	/**
	 * 数据库中取得人员串
	 * 
	 * 
	 * @param dbConn
	 * @param netdisk
	 * @param action
	 * @return
	 */
	public String getUserIdString(Connection dbConn, YHNetdisk netdisk, String action) {
		String ids = "";
		if (netdisk != null) {
			if ("USER_ID".equals(action)) {
				if (!"".equals(netdisk.getUserId()) && netdisk.getUserId() != null) {
					String idString = YHUtility.null2Empty(netdisk.getUserId());
					String[] idsStrings = idString.split("\\|");

					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 2) {
							ids = "";
						} else if (idsStrings.length == 1) {
							ids = "";

						} else {
							ids = idsStrings[2];
						}

					}

				}
			} else if ("MANAGE_USER".equals(action)) {
				if (!"".equals(netdisk.getManageUser()) && netdisk.getManageUser() != null) {
					String idString = YHUtility.null2Empty(netdisk.getManageUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 2) {
							ids = "";
						} else if (idsStrings.length == 1) {
							ids = "";

						} else {
							ids = idsStrings[2];
						}

					}
				}
			} else if ("NEW_USER".equals(action)) {
				if (!"".equals(netdisk.getNewUser()) && netdisk.getNewUser() != null) {
					String idString = YHUtility.null2Empty(netdisk.getNewUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 2) {
							ids = "";
						} else if (idsStrings.length == 1) {
							ids = "";

						} else {
							ids = idsStrings[2];
						}

					}
				}
			} else if ("DOWN_USER".equals(action)) {
				if (!"".equals(netdisk.getDownUser()) && netdisk.getDownUser() != null) {
					String idString = YHUtility.null2Empty(netdisk.getDownUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
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

		}

		return ids;
	}

	/**
	 * 数据库中取得角色id串
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param map
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String getRoleIdString(Connection dbConn, YHNetdisk netdisk, String action) throws Exception {
		String ids = "";
		if (netdisk != null) {
			if ("USER_ID".equals(action)) {
				if (!"".equals(netdisk.getUserId()) && netdisk.getUserId() != null) {
					String idString = YHUtility.null2Empty(netdisk.getUserId());
					String[] idsStrings = idString.split("\\|");

					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 1) {
							ids = "";
						} else {
							ids = idsStrings[1];
						}
					}

				}
			} else if ("MANAGE_USER".equals(action)) {
				if (!"".equals(netdisk.getManageUser()) && netdisk.getManageUser() != null) {
					String idString = YHUtility.null2Empty(netdisk.getManageUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 1) {
							ids = "";
						} else {
							ids = idsStrings[1];
						}
					}
				}
			} else if ("NEW_USER".equals(action)) {
				if (!"".equals(netdisk.getNewUser()) && netdisk.getNewUser() != null) {
					String idString = YHUtility.null2Empty(netdisk.getNewUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 1) {
							ids = "";
						} else {
							ids = idsStrings[1];
						}
					}
				}
			} else if ("DOWN_USER".equals(action)) {
				if (!"".equals(netdisk.getDownUser()) && netdisk.getDownUser() != null) {
					String idString = YHUtility.null2Empty(netdisk.getDownUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						if (idsStrings.length == 1) {
							ids = "";
						} else {
							ids = idsStrings[1];
						}
					}
				}
			}
		}
		return ids;
	}

	/**
	 * 数据库中取得部门id串
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param map
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String getDeptIdString(Connection dbConn, YHNetdisk netdisk, String action) throws Exception {
		String ids = "";
		if (netdisk != null) {
			if ("USER_ID".equals(action)) {
				if (!"".equals(netdisk.getUserId()) && netdisk.getUserId() != null) {
					String idString = YHUtility.null2Empty(netdisk.getUserId());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			} else if ("MANAGE_USER".equals(action)) {
				if (!"".equals(netdisk.getManageUser()) && netdisk.getManageUser() != null) {
					String idString = netdisk.getManageUser();
					String[] idsStrings = idString.split("\\|");

					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}

				}
			} else if ("NEW_USER".equals(action)) {
				if (!"".equals(netdisk.getNewUser()) && netdisk.getNewUser() != null) {
					String idString = YHUtility.null2Empty(netdisk.getNewUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			} else if ("DOWN_USER".equals(action)) {
				if (!"".equals(netdisk.getDownUser()) && netdisk.getDownUser() != null) {
					String idString = YHUtility.null2Empty(netdisk.getDownUser());
					String[] idsStrings = idString.split("\\|");
					if (!"".equals(idString.trim()) && idsStrings.length != 0) {
						ids = idsStrings[0];
					}
				}
			}

		}
		return ids;
	}

	/**
	 * 全局搜索
	 * 
	 * @param dbConn
	 * @param map
	 * @param loginUser
	 * @return
	 * @throws Exception
	 */
	public List<Map<Object, Object>> getGlobalFileContentsByList(Connection dbConn, Map map, YHPerson loginUser) throws Exception {
		List list = new ArrayList();

		int seqId = (Integer) map.get("seqId");
		String pathIdStr = (String) map.get("pathIdStr");

		String fileNameStr = (String) map.get("fileNameStr");
		String keyStr = (String) map.get("keyStr");
		YHNetDiskLogic logic = new YHNetDiskLogic();

		try {

			YHNetdisk netdisk = logic.getNetdiskInfoById(dbConn, seqId);

			boolean userIdPriv = false;
			boolean manageUserPriv = false;
			boolean downUserPriv = false;

			int managePriv = 0;
			int downPriv = 0;
			int userPriv = 0;

			int contentValue = -1;
			int titleValue = -1;

			if (netdisk != null) {

				userIdPriv = logic.getUserIdAccessPriv(dbConn, loginUser, netdisk);
				manageUserPriv = logic.getManageAccessPriv(dbConn, loginUser, netdisk);
				downUserPriv = logic.getDownAccessPriv(dbConn, loginUser, netdisk);

				if (manageUserPriv) {
					userIdPriv = true;
					downUserPriv = true;

					downPriv = 1;
					managePriv = 1;

				}

				if (userIdPriv) {
					userPriv = 1;
				}

			}

			if (userIdPriv) {

				Map<Object, Object> toMap = new HashMap<Object, Object>();

				toMap.put("managePriv", managePriv);
				toMap.put("downPriv", downPriv);
				toMap.put("userPriv", userPriv);

				toMap.put("contentValue", contentValue);
				toMap.put("titleValue", titleValue);
				toMap.put("managePriv", managePriv);

				toMap.put("fileNameStr", fileNameStr);
				toMap.put("keyStr", keyStr);

				this.queryFileInfo(pathIdStr, list, toMap, netdisk);

				// File pathFile = new File(pathIdStr);
				// if (pathFile.exists()) {
				// File[] files = pathFile.listFiles();
				// for (File file : files) {
				//
				// if (file.isDirectory()) {
				// continue;
				// }
				//
				// String fileName = file.getName(); // eeeaaa.jpg
				//
				// String fileType =
				// YHFileUtility.getFileExtName(file.getAbsolutePath()); // txt
				//
				// titleValue = fileName.indexOf(fileNameStr.trim());
				//
				// String typeString = "." + fileType;
				// if (!"".equals(fileNameStr.trim()) && titleValue == -1 ||
				// !"".equals(keyStr.trim()) && !".txt".equals(typeString.trim())
				// && !".htm".equals(typeString.trim()) &&
				// !".html".equals(fileType.trim())) {
				// continue;
				// }
				//
				// if (!"".equals(keyStr.trim())) {
				// StringBuffer buffer =
				// YHFileUtility.loadLine2Buff(file.getAbsolutePath(), "GBK");
				// contentValue = buffer.indexOf(keyStr.trim());
				//
				// if (contentValue == -1) {
				// continue;
				// }
				//
				// }
				//
				// Map<Object, Object> returnMap = new HashMap<Object, Object>();
				//
				// String typeName = logic.getFileTypeName(fileType);
				//
				// long fileSizeLong = file.length();
				//
				// String urlString = "";
				// String filePathString = file.getParent().replace("\\", "/");
				//
				// String absolutePath = file.getAbsolutePath().replace("\\", "/");
				//
				// if (filePathString.trim().indexOf("/") != -1) {
				// urlString =
				// filePathString.trim().substring(filePathString.indexOf("/") + 1,
				// filePathString.length());
				// urlString = YHUtility.null2Empty(netdisk.getDiskName()) + "/" +
				// urlString;
				// }
				//
				// returnMap.put("dbSeqId", netdisk.getSeqId());
				// returnMap.put("urlString", urlString);
				// returnMap.put("absolutePath", absolutePath);
				// returnMap.put("filePath", filePathString);
				// returnMap.put("fileName", file.getName());
				// returnMap.put("fileSize", this.transformSize(fileSizeLong));
				// returnMap.put("fileModifyTime", YHUtility.getDateTimeStr(new
				// Date(file.lastModified())));
				// // returnMap.put("img", "");
				// returnMap.put("typeName", typeName);
				// returnMap.put("userPriv", userPriv);
				// returnMap.put("managePriv", managePriv);
				// returnMap.put("downPriv", downPriv);
				//
				// list.add(returnMap);
				//
				// }
				//
				// }

			}

		} catch (Exception e) {
			throw e;
		}

		return list;
	}

	public List queryFileInfo(String dirPath, List list, Map toMap, YHNetdisk netdisk) throws Exception {

		int managePriv = (Integer) toMap.get("managePriv");
		int downPriv = (Integer) toMap.get("downPriv");
		int userPriv = (Integer) toMap.get("userPriv");

		int contentValue = (Integer) toMap.get("contentValue");
		int titleValue = (Integer) toMap.get("titleValue");

		String fileNameStr = (String) toMap.get("fileNameStr");
		String keyStr = (String) toMap.get("keyStr");

		YHNetDiskLogic logic = new YHNetDiskLogic();

		try {
			File pathFile = new File(dirPath);
			if (pathFile.exists()) {
				File[] files = pathFile.listFiles();
				if (files == null) {
					return list;

				}
				for (File file : files) {

					if (file.isDirectory() && "tdoa_cache".equals(file.getName())) {
						continue;
					}

					if (file.isDirectory()) {
						queryFileInfo(file.getAbsolutePath(), list, toMap, netdisk);
					}

					String fileName = file.getName(); // eeeaaa.jpg

					String fileType = YHFileUtility.getFileExtName(file.getAbsolutePath()); // txt

					titleValue = fileName.indexOf(fileNameStr.trim());

					String typeString = "." + fileType;
					if (!"".equals(fileNameStr.trim()) && titleValue == -1 || !"".equals(keyStr.trim()) && !".txt".equals(typeString.trim())
							&& !".htm".equals(typeString.trim()) && !".html".equals(fileType.trim())) {
						continue;
					}

					if (!"".equals(keyStr.trim())) {
						StringBuffer buffer = YHFileUtility.loadLine2Buff(file.getAbsolutePath(), "GBK");
						contentValue = buffer.indexOf(keyStr.trim());

						if (contentValue == -1) {
							continue;
						}

					}

					Map<Object, Object> returnMap = new HashMap<Object, Object>();

					String typeName = logic.getFileTypeName(fileType);

					long fileSizeLong = file.length();

					String urlString = "";
					String filePathString = file.getParent().replace("\\", "/");

					String absolutePath = file.getAbsolutePath().replace("\\", "/");

					if (filePathString.trim().indexOf("/") != -1) {
						urlString = "/" + filePathString.trim().substring(filePathString.indexOf("/") + 1, filePathString.length());
					}

					boolean isOffice = logic.is_office(typeString);
					int officeFlag = 0;
					if (isOffice) {
						officeFlag = 1;
					}

					returnMap.put("dbSeqId", netdisk.getSeqId());
					returnMap.put("urlString", urlString);
					returnMap.put("absolutePath", absolutePath);
					returnMap.put("filePath", filePathString);
					returnMap.put("officeFlag", officeFlag);
					returnMap.put("fileName", file.getName());
					returnMap.put("fileSize", this.transformSize(fileSizeLong));
					returnMap.put("fileModifyTime", YHUtility.getDateTimeStr(new Date(file.lastModified())));
					// returnMap.put("img", "");
					returnMap.put("typeName", typeName);
					returnMap.put("userPriv", userPriv);
					returnMap.put("managePriv", managePriv);
					returnMap.put("downPriv", downPriv);

					list.add(returnMap);

				}

			}

		} catch (Exception e) {
			throw e;
		}

		return list;
	}

	public List<Map<String, String>> getfileInforByList(Connection dbConn, YHPerson loginUser, YHNetdisk netdisk, String fromDirPath) throws Exception {
		List list = new ArrayList();

		int visiPrivFlag = 0;
		int managePrivFlag = 0;
		int newPrivFlag = 0;
		int downPrivFlag = 0;

		try {

			// 取得权限
			boolean userIdPriv = this.getUserIdAccessPriv(dbConn, loginUser, netdisk);
			boolean manageUserPriv = this.getManageAccessPriv(dbConn, loginUser, netdisk);
			boolean downUserPriv = this.getDownAccessPriv(dbConn, loginUser, netdisk);
			boolean newUserPriv = this.getNewAccessPriv(dbConn, loginUser, netdisk);

			if (manageUserPriv) {
				userIdPriv = true;
				downUserPriv = true;
			}
			if (manageUserPriv) {
				managePrivFlag = 1;
			}
			if (downUserPriv) {
				downPrivFlag = 1;
			}
			if (newUserPriv) {
				newPrivFlag = 1;
			}
			if (userIdPriv) {
				visiPrivFlag = 1;
				String diskPath = YHUtility.null2Empty(netdisk.getDiskPath());
				File file2 = new File(diskPath);
				if (fromDirPath == null) {
					fromDirPath = "";
				}
				fromDirPath = fromDirPath.replace('\\', '/'); // D:\aa\\
				File file = new File(fromDirPath); // D:\aa
				int rootDir = 0; // 用于判断是根目录还是子目录,1为根目录
				if (file2.getPath().equals(file.getPath())) {
					rootDir = 1;
				}

				File[] files = file.listFiles();
				if (files != null && files.length != 0) {
					for (int i = 0; i < files.length; i++) {
						File subfile = files[i];
						if (!subfile.isDirectory()) {
							Map<String, String> m = new HashMap<String, String>();
							String fileType = YHFileUtility.getFileExtName(subfile.getAbsolutePath());
							String typeName = this.getFileTypeName(fileType);
							boolean isOffice = this.is_office("." + fileType);
							String officeFlag = "0";
							if (isOffice) {
								officeFlag = "1";
							}
							long fileSize = subfile.length();
							m.put("fileName", subfile.getName());
							m.put("fileSpace", String.valueOf(fileSize));
							m.put("fileType", typeName);
							m.put("officeFlag", officeFlag);
							m.put("rootDir", String.valueOf(rootDir));
							m.put("filePath", subfile.getAbsolutePath().replace('\\', '/'));
							m.put("fileModifyTime", YHUtility.getDateTimeStr(new Date(subfile.lastModified())));
							// m.put("visiPrivFlag", String.valueOf(visiPrivFlag));
							// m.put("managePrivFlag", String.valueOf(managePrivFlag));
							// m.put("newPrivFlag", String.valueOf(newPrivFlag));
							// m.put("downPrivFlag", String.valueOf(downPrivFlag));
							list.add(m);
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return list;
	}

	/**
	 * 获取网络硬盘信息到桌面模块（须分布）
	 * 
	 * @param dbConn
	 * @param startCount
	 * @param endCount
	 * @return
	 * @throws Exception
	 */
	public YHPortalProducer getNetdiskInfoToDeskTop(Connection dbConn, YHPerson person, String filePath, int startCount, int endCount) throws Exception {
		int k = 0;
		List<Object> list = new ArrayList<Object>();
		YHPortalProducer rule = new YHPortalProducer();
		try {
			if (YHUtility.isNullorEmpty(filePath)) {
				filePath = "";
			}
			filePath = filePath.replace('\\', '/');
			File file = new File(filePath);
			File[] files = file.listFiles();
			if (files != null && files.length != 0) {
				for (int i = startCount; i < startCount + endCount && k <= endCount && i < files.length; i++) {
					String isDir = "false";
					File subfile = files[i];
					if (subfile.isDirectory()) {
						isDir = "true";
					}
					Map<String, String> map = new HashMap<String, String>();
					String fileType = YHFileUtility.getFileExtName(subfile.getAbsolutePath());
					boolean isOffice = this.is_office("." + fileType);
					String officeFlag = "0";
					if (isOffice) {
						officeFlag = "1";
					}
					long fileSize = subfile.length();
					map.put("fileName", subfile.getName()); // 文件名稱
					map.put("fileSpace", String.valueOf(fileSize)); // 文件大小
					map.put("fileType", fileType);// 类型名称
					map.put("isDir", isDir); // 文件是否是目录
					map.put("officeFlag", officeFlag); // 是否是 office 文件类型的
					map.put("filePath", subfile.getAbsolutePath().replace('\\', '/'));// 文件路径
					map.put("fileModifyTime", YHUtility.getDateTimeStr(new Date(subfile.lastModified())) + "=======");
					list.add(map);
					k++;
				}
				rule.setData(list);
				YHLinkRule lr = new YHLinkRule("fileName", "filePath");
				YHImgRule imag = new YHImgRule("fileName", "filePath");
				rule.addRule(lr);
				rule.addRule(imag);
				// rule.toJson();
			}
//			System.out.println(rule.toJson());
		} catch (Exception e) {
			throw e;
		}
		return rule;
	}
	
	public String getInfoFromTextLogic(StringBuffer buffer,String filePath) throws Exception{
		if (YHUtility.isNullorEmpty(filePath)) {
			return "";
		}
		try {
			
			File file = new File(filePath);
			if (file!=null) {
				 buffer = YHFileUtility.loadLine2Buff(filePath, "gbk");
//				FileInputStream fileInputStream = new FileInputStream(file);
//				Reader reader= new InputStreamReader(fileInputStream);
//				char [] charBuffer = new char[100];
//				int charLength ;
//				while((charLength = reader.read(charBuffer))!=-1){
//					buffer.append(charLength);
//				}
//				reader.close();
			}
			return buffer.toString();
		} catch (Exception e) {
			throw e;
		}
	}
	

}
