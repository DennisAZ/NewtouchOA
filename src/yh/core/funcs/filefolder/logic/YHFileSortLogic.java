package yh.core.funcs.filefolder.logic;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.filefolder.data.YHFileContent;
import yh.core.funcs.filefolder.data.YHFileSort;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;

public class YHFileSortLogic {

	public void saveFileSortInfo(Connection dbConn, YHFileSort fileSort) {
		YHORM orm = new YHORM();
		try {
			orm.saveSingle(dbConn, fileSort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<YHFileSort> getFileSortsInfo(Connection dbConn) throws Exception {
		YHORM orm = new YHORM();
		return orm.loadListSingle(dbConn, YHFileSort.class, new HashMap());
	}

	public YHFileSort getFileSortInfoById(Connection dbConn, String seqIdStr) throws NumberFormatException, Exception {
		YHORM orm = new YHORM();
		int seqId = 0;
		if (!YHUtility.isNullorEmpty(seqIdStr)) {
			seqId = Integer.parseInt(seqIdStr);
		}

		return (YHFileSort) orm.loadObjSingle(dbConn, YHFileSort.class, seqId);
	}

	public void updateFileSortInfoById(Connection dbConn, YHFileSort fileSort) throws Exception {
		YHORM orm = new YHORM();
		orm.updateSingle(dbConn, fileSort);
	}

	/**
	 * 递归删除文件夹及下的所有文件信息
	 * @param dbConn
	 * @param fileSort
	 * @throws Exception
	 */
	public void delFileSortInfoById(Connection dbConn, YHFileSort fileSort, int loginUserSeqId, String ipStr) throws Exception {
		YHFileContentLogic contentLogic = new YHFileContentLogic();
		String separator = File.separator;
		String filePath = YHSysProps.getAttachPath() + separator + "file_folder" + separator;
		String seqIdStrs = "";
		YHORM orm = new YHORM();
		Map map = new HashMap();
		map.put("SORT_PARENT", fileSort.getSeqId());
		List<YHFileSort> fileSortList = orm.loadListComplex(dbConn, YHFileSort.class, map);

		Map contentMap = new HashMap();
		contentMap.put("SORT_ID", fileSort.getSeqId());
		List<YHFileContent> fileContents = new ArrayList<YHFileContent>();
		fileContents = contentLogic.getFileContentsInfo(dbConn, contentMap);
		if (fileContents != null && fileContents.size() > 0) {
			for (int i = 0; i < fileContents.size(); i++) {
				YHFileContent content = fileContents.get(i);
				seqIdStrs += content.getSeqId() + ",";
			}
			if (seqIdStrs.endsWith(",")) {
				seqIdStrs = seqIdStrs.trim().substring(0, seqIdStrs.trim().length() - 1);
			}
			contentLogic.delFile(dbConn, seqIdStrs, filePath, loginUserSeqId, ipStr, "", "");
		}
		orm.deleteSingle(dbConn, fileSort);
		for (int i = 0; i < fileSortList.size(); i++) {
			delFileSortInfoById(dbConn, fileSortList.get(i), loginUserSeqId, ipStr);
		}
	}
}
