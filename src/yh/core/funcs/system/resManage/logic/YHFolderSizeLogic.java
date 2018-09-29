package yh.core.funcs.system.resManage.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHSysProps;
import yh.core.util.db.YHDBUtility;


public class YHFolderSizeLogic {
  public int fileCount = 0;
	public long getSize(Connection conn, int seqId) throws Exception {
		String query = "select SEQ_ID, USER_ID from oa_file_sort where SORT_TYPE=4  and SORT_PARENT=0";
		long sortSize = 0;
		String userId = String.valueOf(seqId);
		PreparedStatement stm1 = null;
		ResultSet rs1 = null;
		try {
			stm1 = conn.prepareStatement(query);
			rs1 = stm1.executeQuery();
			while (rs1.next()) {
				Clob userIdClob = rs1.getClob("USER_ID");
				String userId2 = this.clob2String(userIdClob);
				if (userId2.equals(userId)) {
					int sortIdTmp = rs1.getInt("SEQ_ID");
					sortSize += this.getTreeSize(sortIdTmp, conn);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stm1, rs1, null);
		}
		sortSize += this.getTreeSizeRoot(conn, seqId);

//		long fileSize = 0;
//		double tmp = (double) sortSize / 1024 / 1024;
//		String result = "";
//		if (Math.floor(tmp) > 0) {
//			result = Math.round(tmp) + "MB";
//
//		} else {
//			result = Math.ceil(sortSize / 1024) + "KB";
//		}
//
//
//		long tmpToB = (long) (tmp * 1024 * 1024);
		return sortSize;
	}

	public long getTreeSize(int sortId, Connection conn) throws Exception {
		String query = "select * from oa_file_content where sort_ID=" + sortId;
		long sortSize = 0;
		Statement stm1 = null;
		ResultSet rs1 = null;
		try {
			stm1 = conn.createStatement();
			rs1 = stm1.executeQuery(query);
			while (rs1.next()) {
			  fileCount++;
				String subject = rs1.getString("SUBJECT");
				String content = rs1.getString("CONTENT");
				String attachId = rs1.getString("ATTACHMENT_ID");
				String attachName = rs1.getString("ATTACHMENT_NAME");

				if (subject == null) {
					subject = "";
				}
				if (content == null) {
					content = "";
				}
				sortSize += content.length() + subject.length();
				sortSize += this.getAttachSize(attachId, attachName);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stm1, rs1, null);
		}
		return sortSize;
	}

	public long getTreeSizeRoot(Connection conn, int userId) throws Exception {
		String query = "select * from oa_file_content where sort_ID=0 and user_id='" + userId + "'";
		Statement stm1 = null;
		ResultSet rs1 = null;
		long sortSize = 0;
		try {
			stm1 = conn.createStatement();
			rs1 = stm1.executeQuery(query);
			while (rs1.next()) {
			  fileCount++;
				String subject = rs1.getString("SUBJECT");
				String content = rs1.getString("CONTENT");
				String attachId = rs1.getString("ATTACHMENT_ID");
				String attachName = rs1.getString("ATTACHMENT_NAME");

				if (subject == null) {
					subject = "";
				}
				if (content == null) {
					content = "";
				}
				sortSize += content.length() + subject.length();
				sortSize += this.getAttachSize(attachId, attachName);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stm1, rs1, null);
		}
		return sortSize;
	}

	public int getAttachSize(String attachId, String attachName) throws IOException {
		int size = 0;
		if (attachId == null) {
			attachId = "";
		}
		if (attachName == null) {
			attachName = "";
		}
		String[] aId = attachId.split(",");
		String[] aName = attachName.split("\\*");
		if (aId.length != aName.length) {
			return 0;
		}
		for (int i = 0; i < aId.length; i++) {
			size += this.attachSize(aId[i], aName[i], "file_folder");
		}
		return size;
	}

	public int attachSize(String aId, String aName, String module) throws IOException {
		int size = 0;
		String filePath = YHSysProps.getAttachPath()  + File.separator  + module;
		int index = aId.indexOf("_");
		String hard = "";
		String str = "";
		if (index > 0) {
			hard = aId.substring(0, index);
			str = aId.substring(index + 1);
		} else {
			hard = "all";
			str = aId;
		}

		String path = filePath  + File.separator  +  hard  + File.separator  +  str + "_" + aName;

		File file = new File(path);
		if (!file.exists()) {
			// 兼容老的数据
			String path2 = filePath + File.separator  +  hard  + File.separator  +  str + "." + aName;
			file = new File(path2);
		}
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);
			try {
				size = fis.available();
				fis.close();
			} catch (IOException e) {
				throw e;
			}
		}
		//System.out.println(path + ":" + size);
		return size;
	}

	/**
	 *将CLOB转成String ,静态方法
	 * 
	 * 
	 * @param clob
	 *          字段
	 * @return 内容字串，如果出现错误，返回
	 * @throws Exception
	 */
	public String clob2String(Clob clob) throws Exception {
		if (clob == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer(65535);// 64K
		Reader clobStream = null;
		try {
			clobStream = clob.getCharacterStream();
			char[] b = new char[60000];// 每次获取60K
			int i = 0;
			while ((i = clobStream.read(b)) != -1) {
				sb.append(b, 0, i);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				if (clobStream != null) {
					clobStream.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		if (sb == null)
			return "";
		else
			return sb.toString();
	}
}
