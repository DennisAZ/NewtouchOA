package yh.core.funcs.system.picture.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.filefolder.logic.YHFileSortLogic;
import yh.core.funcs.system.picture.data.YHPicture;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHPictureLogic {
	private static Logger log = Logger.getLogger(YHPictureLogic.class);

	/**
	 * 保存图片目录信息
	 * 
	 * @param dbConn
	 * @param picture
	 */
	public void savePicSortInfo(Connection dbConn, YHPicture picture) {
		YHORM orm = new YHORM();
		try {
			orm.saveSingle(dbConn, picture);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取图片目录信息
	 * 
	 * @param dbConn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<YHPicture> getPicSortInfo(Connection dbConn) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHPicture.class, new HashMap());
	}

	public YHPicture selectManagerIds(Connection dbConn, Map map) throws Exception {
		YHORM orm = new YHORM();
		return (YHPicture) orm.loadObjSingle(dbConn, YHPicture.class, map);

	}

	/**
	 * 根据人员id字符串得到name字符串,不需要以"|"分离
	 * 
	 * @param dbConn
	 * @param map
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String getNamesByIds(Connection dbConn, Map map, String action) throws Exception {
		String names = "";
		String ids = "";
		YHPicture picture = selectManagerIds(dbConn, map);
		if ("userId".equals(action) && !YHUtility.isNullorEmpty(picture.getToUserId())) {
			ids = picture.getToUserId();
			YHPersonLogic tpl = new YHPersonLogic();
			names = tpl.getNameBySeqIdStr(ids, dbConn);

		} else if ("roleId".equals(action) && !YHUtility.isNullorEmpty(picture.getToPrivId())) {
			ids = picture.getToPrivId();
			YHFileSortLogic fileSortLogic = new YHFileSortLogic();
			names = fileSortLogic.getRoleNameBySeqIdStr(dbConn, ids);
		} else if ("deptId".equals(action) && !YHUtility.isNullorEmpty(picture.getToDeptId())) {
			ids = picture.getToDeptId();
			YHFileSortLogic fileSortLogic = new YHFileSortLogic();
			if (!"ALL_DEPT".equals(ids.trim())) {
				names = fileSortLogic.getDeptNameBySeqIdStr(dbConn, ids);
			}
		}
		return names;
	}

	/**
	 * 更新编辑文件夹信息
	 * 
	 * @param dbConn
	 * @param map
	 * @throws Exception
	 */
	public void updatePicSort(Connection dbConn, Map map) {
		YHORM orm = new YHORM();
		PreparedStatement ps = null;
		String sql = "UPDATE oa_picture SET " + "PIC_NAME=?" + ",PIC_PATH=?" + ",TO_DEPT_ID=?" + ",TO_PRIV_ID=?" + ",TO_USER_ID=? " + "where SEQ_ID=? ";
		try {
			ps = dbConn.prepareStatement(sql);
			ps.setString(1, (String) map.get("picName"));
			ps.setString(2, (String) map.get("picPath"));
			ps.setString(3, (String) map.get("toDeptId"));
			ps.setString(4, (String) map.get("toPrivId"));
			ps.setString(5, (String) map.get("toUserId"));
			ps.setInt(6, (Integer) map.get("seqId"));
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			YHDBUtility.close(ps, null, log);
		}
	}

	/**
	 * 更新访问权限信息
	 * 
	 * @param dbConn
	 * @param map
	 * @throws Exception
	 */
	public void updatePrivateById(Connection dbConn, Map map, String action) {
		YHORM orm = new YHORM();
		PreparedStatement ps = null;
		try {
			if ("UPLOAD_USER".equals(action)) {
				String sql = "UPDATE oa_picture SET PRIV_STR=? where SEQ_ID=? ";
				ps = dbConn.prepareStatement(sql);
				ps.setString(1, (String) map.get("uploadUser"));
			}
			if ("MANAGE_USER".equals(action)) {
				String sql = "UPDATE oa_picture SET DEL_PRIV_STR=? where SEQ_ID=? ";
				ps = dbConn.prepareStatement(sql);
				ps.setString(1, (String) map.get("manageUser"));
			}

			ps.setInt(2, (Integer) map.get("seqId"));
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			YHDBUtility.close(ps, null, log);
		}
	}

	/**
	 * 得到人员的id字符串
	 * 
	 * @param dbConn
	 * @param map
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String selectManagerIds(Connection dbConn, Map map, String action) throws Exception {
		YHORM orm = new YHORM();
		String ids = "";
		String idString = "";
		YHPicture picture = (YHPicture) orm.loadObjSingle(dbConn, YHPicture.class, map);
		if (picture != null && !"".equals(picture)) {
			if ("UPLOAD_USER".equals(action)) {
				idString = YHUtility.null2Empty(picture.getPrivStr());
			} else if ("MANAGE_USER".equals(action)) {
				idString = YHUtility.null2Empty(picture.getDelPrivStr());
			}
			if (idString != null && !"".equals(idString)) {
				String[] idsStrings = idString.split("\\|");
				//System.out.println(idsStrings);
				if (idsStrings.length != 0) {

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
	 * 根据人员id字符串得到name字符串
	 * 
	 * @param dbConn
	 * @param map
	 * @param action
	 * @return
	 * @throws Exception
	 */
	public String getNamesById(Connection dbConn, Map map, String action) throws Exception {
		String names = "";
		YHPersonLogic tpl = new YHPersonLogic();
		String ids = this.selectManagerIds(dbConn, map, action);
		names = tpl.getNameBySeqIdStr(ids, dbConn);
		return names;
	}

	/**
	 * 得到角色人员的id字符串
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
		String idString = "";
		YHPicture picture = (YHPicture) orm.loadObjSingle(dbConn, YHPicture.class, map);
		if (picture != null && !"".equals(picture)) {
			if ("UPLOAD_USER".equals(action)) {
				idString = YHUtility.null2Empty(picture.getPrivStr());
			} else if ("MANAGE_USER".equals(action)) {
				idString = YHUtility.null2Empty(picture.getDelPrivStr());
			}
			if (idString != null && !"".equals(idString)) {
				String[] idsStrings = idString.split("\\|");
				//System.out.println("角色idsStrings:" + idsStrings.length);
				if (idsStrings.length != 0) {

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
	 * 根据角色人员id字符串得到name字符串
	 * 
	 * @param dbConn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getRoleNamesByIds(Connection dbConn, Map map, String action) throws Exception {
		String names = "";
		YHFileSortLogic tpl = new YHFileSortLogic();
		String ids = this.getRoleIds(dbConn, map, action);
		//System.out.println(ids);
		names = tpl.getRoleNameBySeqIdStr(dbConn, ids);
		return names;
	}

	/**
	 * 得到部门人员的id字符串
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
		String idString = "";
		YHPicture picture = (YHPicture) orm.loadObjSingle(dbConn, YHPicture.class, map);
		if (picture != null && !"".equals(picture)) {
			if ("UPLOAD_USER".equals(action)) {
				idString = YHUtility.null2Empty(picture.getPrivStr());
			} else if ("MANAGE_USER".equals(action)) {
				idString = YHUtility.null2Empty(picture.getDelPrivStr());
			}
			if (idString != null && !"".equals(idString)) {
				String[] idsStrings = idString.split("\\|");
				//System.out.println(idsStrings);
				if (idsStrings.length != 0) {
					ids = idsStrings[0];
				}
			}
		}
		return ids;
	}

	/**
	 * 根据部门id字符串得到name字符串
	 * 
	 * @param dbConn
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String getDeptByIds(Connection dbConn, Map map, String action) throws Exception {
		String names = "";
		YHFileSortLogic tpl = new YHFileSortLogic();
		String ids = this.getDeptIds(dbConn, map, action);
		//System.out.println(ids);
		names = tpl.getDeptNameBySeqIdStr(dbConn, ids);
		return names;
	}

	/**
	 * 删除信息
	 * 
	 * @param dbConn
	 * @param netdisk
	 * @throws Exception
	 */
	public void delPicFolderInfoById(Connection dbConn, YHPicture picture) throws Exception {
		YHORM orm = new YHORM();
		orm.deleteSingle(dbConn, picture);
	}

}
