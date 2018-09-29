/**
 * 
 */
package oa.core.funcs.bbs.act;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;

/**
 * @author lanjinsheng
 * 
 */
public class BbsService {
	public static boolean isStringNull(String str){
		if(str!=null && !str.trim().equalsIgnoreCase("")){
			return false;
		} else
		 return true;
	}

	public List getAllBoard(HttpServletRequest request) throws Exception {

		DBManager db = new DBManager(request);
		String sql = "select *from oa_bbs_board order by board_no";
		ResultSet rs = db.executeQuery(sql);
		List bdlist = new ArrayList();
		try {
			while (rs.next()) {
				BbsBoard bbsB = new BbsBoard();
				String hosters = rs.getString("BOARD_HOSTER");
				bbsB.setBoarderId(rs.getInt("board_id"));
				bbsB.setBoaderHoster(hosters);
				bbsB.setAnonymity(rs.getString("ANONYMITY_YN"));
				bbsB.setBoardName(rs.getString("BOARD_NAME"));
				bbsB.setBoardNo(rs.getInt("BOARD_NO"));
				bbsB.setDeptId(rs.getString("DEPT_ID"));
				bbsB.setPrivId(rs.getString("PRIV_ID"));
				bbsB.setUserId(rs.getString("USER_ID"));
				bbsB.setWelcome(rs.getString("WELCOME_TEXT"));
				bdlist.add(bbsB);
			}

			db.closeAll(rs);
		
			for (int i = 0; i < bdlist.size(); i++) {
				BbsBoard bbsB = (BbsBoard) bdlist.get(i);
				if(!isStringNull( bbsB.getBoaderHoster())){
				String hosterSql = "select user_name from person where seq_id in("
						+ bbsB.getBoaderHoster() + ")";
				
				ResultSet rs2 = db.executeQuery(hosterSql);
				if (rs2 == null)
					bbsB.setBoaderHosterName("无");
				else {
					while (rs2.next()) {
						String userName = rs2.getString("user_name");
						bbsB.setBoaderHosterName(bbsB.getBoaderHosterName()
								+ userName + ",");
					}
				}
				db.closeAll(rs2);
				}
				if(!isStringNull( bbsB.getUserId())){
				String userSql = "select user_name from person where seq_id in("
						+ bbsB.getUserId() + ")";
				ResultSet rs3 = db.executeQuery(userSql);
				if (rs3 == null)
					bbsB.setUserName("无");
				else {
					while (rs3.next()) {
						String userName = rs3.getString("user_name");
							bbsB.setUserName(bbsB.getUserName() + userName
									+ ",");
					}
				}
				db.closeAll(rs3);
				}
				if(!isStringNull( bbsB.getPrivId())){
				String privSql = "select priv_name from user_priv where seq_id in("
						+ bbsB.getPrivId() + ")";
				ResultSet rs4 = db.executeQuery(privSql);
				if (rs4 == null)
					bbsB.setPrivName("无");
				else {

					while (rs4.next()) {
						String privname = rs4.getString("priv_name");
							bbsB.setPrivName(bbsB.getPrivName() + privname
									+ ",");
					}
				}
				db.closeAll(rs4);
				}
				if(!isStringNull( bbsB.getDeptId())){
				if (bbsB.getDeptId().equalsIgnoreCase("0")
						|| bbsB.getDeptId().contains(",0,")) {
					bbsB.setDeptName("全体部门");
				} else {
					String deptSql = "select dept_name from oa_department where seq_id in("
							+ bbsB.getDeptId() + ")";
					ResultSet rs5 = db.executeQuery(deptSql);
					if (rs5 == null)
						bbsB.setDeptName("无");
					else {
						while (rs5.next()) {
							String deptname = rs5.getString("dept_name");
							bbsB.setDeptName(bbsB.getDeptName() + deptname
									+ ",");
						}
					}
					db.closeAll(rs5);
				}
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				db.closeAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bdlist;
	}

	public BbsBoard getBBSBoardByBoardId(HttpServletRequest request)
			throws Exception {
		String boardId= request.getParameter("boardId");
		String sql = "select *from oa_bbs_board where board_id="+boardId;
		BbsBoard bbsB = new BbsBoard();
		DBManager db = new DBManager(request);
		try {
			ResultSet rs = db.executeQuery(sql);
	
			if (rs.next()) {
				
				String hosters = rs.getString("BOARD_HOSTER");
				bbsB.setBoaderHoster(hosters);
				bbsB.setAnonymity(rs.getString("ANONYMITY_YN"));
				bbsB.setBoardName(rs.getString("BOARD_NAME"));
				bbsB.setBoardNo(rs.getInt("BOARD_NO"));
				bbsB.setDeptId(rs.getString("DEPT_ID"));
				bbsB.setPrivId(rs.getString("PRIV_ID"));
				bbsB.setUserId(rs.getString("USER_ID"));
				bbsB.setWelcome(rs.getString("WELCOME_TEXT"));
				bbsB.setBoarderId(rs.getInt("board_id"));
			}

			db.closeAll(rs);
				if(!isStringNull( bbsB.getBoaderHoster())){
				String hosterSql = "select user_name from person where seq_id in("
						+ bbsB.getBoaderHoster() + ")";
				
				ResultSet rs2 = db.executeQuery(hosterSql);
				if (rs2 == null)
					bbsB.setBoaderHosterName("");
				else {
					while (rs2.next()) {
						String userName = rs2.getString("user_name");
						bbsB.setBoaderHosterName(bbsB.getBoaderHosterName()
								+ userName + ",");
					}
				}
				db.closeAll(rs2);
				}
				if(!isStringNull( bbsB.getUserId())){
				String userSql = "select user_name from person where seq_id in("
						+ bbsB.getUserId() + ")";
				ResultSet rs3 = db.executeQuery(userSql);
				if (rs3 == null)
					bbsB.setUserName("");
				else {
					while (rs3.next()) {
						String userName = rs3.getString("user_name");
						bbsB.setUserName(bbsB.getUserName() + userName + ",");
					}
				}
				db.closeAll(rs3);
				}
				if(!isStringNull( bbsB.getPrivId())){
				String privSql = "select priv_name from user_priv where seq_id in("
						+ bbsB.getPrivId() + ")";
				ResultSet rs4 = db.executeQuery(privSql);
				if (rs4 == null)
					bbsB.setPrivName("");
				else {

					while (rs4.next()) {
						String privname = rs4.getString("priv_name");
						bbsB.setPrivName(bbsB.getPrivName() + privname + ",");
					}
				}
				db.closeAll(rs4);
				}
				if(!isStringNull( bbsB.getDeptId())){
				if (bbsB.getDeptId().equalsIgnoreCase("0")
						|| bbsB.getDeptId().contains(",0,")) {
					bbsB.setDeptName("全体部门");
				} else {
					String deptSql = "select dept_name from oa_department where seq_id in("
							+ bbsB.getDeptId() + ")";
					ResultSet rs5 = db.executeQuery(deptSql);
					if (rs5 == null)
						bbsB.setDeptName("");
					else {
						while (rs5.next()) {
							String deptname = rs5.getString("dept_name");
							bbsB.setDeptName(bbsB.getDeptName() + deptname
									+ ",");
						}
					}
					db.closeAll(rs5);
				}
				}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				db.closeAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bbsB;
	}

	public List getUserBBSBoard(Object o, HttpServletRequest request)
			throws Exception {

        String userId=BBSUtil.getFieldValueByName("seqId", o).toString();
        String userDept=BBSUtil.getFieldValueByName("deptId", o).toString();
        Object tempUserDeptOther=BBSUtil.getFieldValueByName("deptIdOther", o);
        String userDeptOther = "";
        if(tempUserDeptOther!=null)
        	userDeptOther=tempUserDeptOther.toString();
		
        String userPriv=BBSUtil.getFieldValueByName("userPriv", o).toString();
		String userPrivOther ="";
		Object tempUserPrivOther=BBSUtil.getFieldValueByName("userPrivOther", o);
		if(tempUserPrivOther!=null)
		userPrivOther=tempUserPrivOther.toString();
		DBManager db = new DBManager(request);
		String sql = "select *from oa_bbs_board order by board_no";
		
		ResultSet rs = db.executeQuery(sql);
//		ResultSet rsFilter = db.executeQuery(userPrivSql);	
		List bdlist = new ArrayList();
		try {
			while (rs.next()) {
				BbsBoard bbsB = new BbsBoard();
				String hosters = rs.getString("BOARD_HOSTER");
				bbsB.setBoaderHoster(hosters);
				bbsB.setAnonymity(rs.getString("ANONYMITY_YN"));
				bbsB.setBoardName(rs.getString("BOARD_NAME"));
				bbsB.setBoardNo(rs.getInt("BOARD_NO"));
				bbsB.setDeptId(rs.getString("DEPT_ID"));
				bbsB.setPrivId(rs.getString("PRIV_ID"));
				bbsB.setUserId(rs.getString("USER_ID"));
				bbsB.setWelcome(rs.getString("WELCOME_TEXT"));
				bbsB.setBoarderId(rs.getInt("board_id"));
				if (userId.equals("1") || bbsB.getDeptId().equals("0")
						|| BBSUtil.hasPriv(userId, bbsB.getUserId())
						|| BBSUtil.hasPriv(userDept, bbsB.getDeptId())
						|| BBSUtil.hasPriv(userDeptOther, bbsB.getDeptId())
						|| BBSUtil.hasPriv(userPriv, bbsB.getPrivId())
						|| BBSUtil.hasPriv(userPrivOther, bbsB.getPrivId())) {
					bdlist.add(bbsB);
				}
			}

			db.closeAll(rs);
			
			for (int i = 0; i < bdlist.size(); i++) {
				BbsBoard bbsB = (BbsBoard) bdlist.get(i);
				int boardid=bbsB.getBoarderId();
				String tieshuSql = "select count(*) from oa_bbs_comment where board_id="
						+ boardid;
				ResultSet rsTS = db.executeQuery(tieshuSql);
				if(rsTS.next()){
					bbsB.setTieshu(rsTS.getInt(1));
				}
				db.closeAll(rsTS);
				
				String zhutiSql = "select count(*) from oa_bbs_comment where board_id="
						+ boardid + " and parent=0";
				ResultSet rsZT= db.executeQuery(zhutiSql);
				if(rsZT.next()){
					bbsB.setZhuti(rsZT.getInt(1));
				}
				db.closeAll(rsZT);
				String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
				String lastPubSql="";
				if (dbms.equals(YHConst.DBMS_ORACLE)) {
//					select * from ( select author_name,submit_time from oa_bbs_comment where board_id=2 order by submit_time desc) where rownum=1
					 lastPubSql = "select * from (select author_name,submit_time from oa_bbs_comment where board_id="
							+ boardid + " order by submit_time desc) where rownum=1";
				}else{
				 lastPubSql = "select author_name,submit_time from oa_bbs_comment where board_id="
						+ boardid + " order by submit_time desc limit 1";
				}
				ResultSet rsLP= db.executeQuery(lastPubSql);
				if(rsLP.next()){
					bbsB.setLastAuthor(rsLP.getString(1));
					bbsB.setLastTime(rsLP.getDate(2).toLocaleString());
				}
				db.closeAll(rsLP);
				
			}
			
			for (int i = 0; i < bdlist.size(); i++) {
				BbsBoard bbsB = (BbsBoard) bdlist.get(i);
				if(!isStringNull( bbsB.getBoaderHoster())){
				String hosterSql = "select user_name from person where seq_id in("
						+ bbsB.getBoaderHoster() + ")";
				
				ResultSet rs2 = db.executeQuery(hosterSql);
				if (rs2 == null)
					bbsB.setBoaderHosterName("无");
				else {
					while (rs2.next()) {
						String userName = rs2.getString("user_name");
						bbsB.setBoaderHosterName(bbsB.getBoaderHosterName()
								+ userName + ",");
					}
				}
				db.closeAll(rs2);
				}
				if(!isStringNull( bbsB.getUserId())){
				String userSql = "select user_name from person where seq_id in("
						+ bbsB.getUserId() + ")";
				ResultSet rs3 = db.executeQuery(userSql);
				if (rs3 == null)
					bbsB.setUserName("无");
				else {
					while (rs3.next()) {
						String userName = rs3.getString("user_name");
							bbsB.setUserName(bbsB.getUserName() + userName
									+ ",");
					}
				}
				db.closeAll(rs3);
				}
				if(!isStringNull( bbsB.getPrivId())){
				String privSql = "select priv_name from user_priv where seq_id in("
						+ bbsB.getPrivId() + ")";
				ResultSet rs4 = db.executeQuery(privSql);
				if (rs4 == null)
					bbsB.setPrivName("无");
				else {

					while (rs4.next()) {
						String privname = rs4.getString("priv_name");
							bbsB.setPrivName(bbsB.getPrivName() + privname
									+ ",");
					}
				}
				db.closeAll(rs4);
				}
				if(!isStringNull( bbsB.getDeptId())){
				if (bbsB.getDeptId().equalsIgnoreCase("0")
						|| bbsB.getDeptId().contains(",0,")) {
					bbsB.setDeptName("全体部门");
				} else {
					String deptSql = "select dept_name from oa_department where seq_id in("
							+ bbsB.getDeptId() + ")";
					ResultSet rs5 = db.executeQuery(deptSql);
					if (rs5 == null)
						bbsB.setDeptName("无");
					else {
						while (rs5.next()) {
							String deptname = rs5.getString("dept_name");
							bbsB.setDeptName(bbsB.getDeptName() + deptname
									+ ",");
						}
					}
					db.closeAll(rs5);
				}
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				db.closeAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bdlist;
	
	}

	public List getUserBBSCommentByBoardId(HttpServletRequest request,
			Object o, String boardId) throws Exception {
		DBManager db = new DBManager(request);
		List bdlist = new ArrayList();
		request.getSession().setAttribute(BBSUtil.bbsHoster, "0");
		request.getSession().setAttribute(BBSUtil.bbsAnonyKey, "0");
		try {
			String sqlBoardAuth = "select ANONYMITY_YN,BOARD_HOSTER from oa_bbs_board where board_id="
					+ boardId;
			String seqId=BBSUtil.getFieldValueByName("seqId", o).toString();
			String userId=BBSUtil.getFieldValueByName("userId", o).toString();
		    ResultSet rsauth = db.executeQuery(sqlBoardAuth);
		if (rsauth.next()) {
			String isAnonymity =rsauth.getString("ANONYMITY_YN");
				request.getSession().setAttribute(BBSUtil.bbsAnonyKey,
						isAnonymity);
			String hoster =rsauth.getString("BOARD_HOSTER");
			if(BBSUtil.hasPriv(userId, hoster) || seqId.equals("1")){
			request.getSession().setAttribute(BBSUtil.bbsHoster, "1");	
			}
		}
		db.closeAll(rsauth);
			String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
			String sql = "";
			if (dbms.equals(YHConst.DBMS_ORACLE)) {
				sql = "select a.*,instr(readeder,'"
						+ userId
						+ ",') as hadread from oa_bbs_comment a where board_id="
						+ boardId
						+ " and parent=0 order by top desc,jing desc,comment_id desc";
			} else {
				sql = "select *,find_in_set('"
						+ userId
						+ "',readeder) as hadread from oa_bbs_comment where board_id="
						+ boardId
						+ " and parent=0 order by top desc,jing desc,comment_id desc";
			}
		ResultSet rs = db.executeQuery(sql);
//		ResultSet rsFilter = db.executeQuery(userPrivSql);	

			while (rs.next()) {
				BbsComment bbsB = new BbsComment();
//				String commentId=ds.get("comment_id");
//				String boardId=ds.get("board_id");
//				String userId=ds.get("user_id");
//				String authorName=ds.get("author_name");
//				String subject=ds.get("subject");
//				String content=ds.get("content");
//				
//				String attachmentId=ds.get("attachment_id");
//				String attachmentName=ds.get("attachment_name");
//				String submitTime=ds.get("submit_time");
//				String replyCont=ds.get("reply_cont");
//				String readCont=ds.get("read_cont");
//				String parent=ds.get("parent");
//				String oldSubmitTime=ds.get("old_submit_time");
//				String top=ds.get("top");
//				String jing=ds.get("jing");
//				String readeder=ds.get("readeder");
//				String signedYn=ds.get("signed_yn");
//				String lockYn=ds.get("lock_yn");
				
				bbsB.setCommentId(rs.getInt("comment_id"));
				bbsB.setBoardId(rs.getInt("board_id"));
				bbsB.setUserId(rs.getString("user_id"));
				bbsB.setAuthorName(rs.getString("author_name"));
				bbsB.setSubject(rs.getString("subject"));
				bbsB.setContent(rs.getString("content"));
				bbsB.setAttachmentId(rs.getString("attachment_id"));
				bbsB.setAttachmentName(rs.getString("attachment_name"));
				bbsB.setSubmitTime(rs.getTimestamp("submit_time")
						.toLocaleString());
				bbsB.setReplyCont(rs.getString("reply_cont"));
				bbsB.setReadCont(rs.getString("read_cont"));
				bbsB.setParent(rs.getString("parent"));
				bbsB.setOldSubmitTime(rs.getTimestamp("old_submit_time")
						.toLocaleString());
				bbsB.setTop(rs.getString("top")==null?"0":rs.getString("top"));
				bbsB.setJing(rs.getString("jing")==null?"0":rs.getString("jing"));
				bbsB.setReadeder(rs.getString("readeder"));
				bbsB.setSignedYn(rs.getString("signed_yn"));
				bbsB.setLockYn(rs.getString("lock_yn"));
				bbsB.setHadRead(rs.getInt("hadread"));
				bdlist.add(bbsB);
			}

			db.closeAll(rs);
			
		
			for (int i = 0; i < bdlist.size(); i++) {
				BbsComment bbsB = (BbsComment) bdlist.get(i);
				String lstSql = "";
				if (dbms.equals(YHConst.DBMS_ORACLE)) {
				    lstSql = "select *from (select author_name,submit_time from oa_bbs_comment where comment_id="
						+ bbsB.getCommentId()
						+ " order by submit_time desc) where rownum=1";
				}else{
					lstSql="select author_name,submit_time from oa_bbs_comment where comment_id="
						+ bbsB.getCommentId()
						+ " order by submit_time desc limit 1";
				}
				
				ResultSet rsLS = db.executeQuery(lstSql);
				if(rsLS.next()){
					bbsB.setLastAuthor(rsLS.getString(1));
					bbsB.setLastTime(rsLS.getTimestamp(2).toLocaleString());
				}
				db.closeAll(rsLS);
			}	
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				db.closeAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bdlist;
	
	}
	
	public BbsComment getUserBBSCommentDetailByCommentId(
			HttpServletRequest request, Object o, String commentId)
			throws Exception {
		DBManager db = new DBManager(request);

		BbsComment bbsB = new BbsComment();
//		 request.getSession().setAttribute(BBSUtil.bbsAuthor, "0");	
		try {
			String sessionUserId = BBSUtil.getFieldValueByName("userId", o)
					.toString();
			if (request.getParameter("flag") != null
					&& request.getParameter("flag").equals("l")) {
				String lockSql = "update oa_bbs_comment set lock_yn=ABS(lock_yn-1) where comment_id="
						+ commentId;
				db.executeUpdate(lockSql);
			}
			
			String sql = "select *from oa_bbs_comment where comment_id="
					+ commentId;
		
		ResultSet rs = db.executeQuery(sql);
//		ResultSet rsFilter = db.executeQuery(userPrivSql);	
			if(rs.next()) {
//				String commentId=ds.get("comment_id");
//				String boardId=ds.get("board_id");
//				String userId=ds.get("user_id");
//				String authorName=ds.get("author_name");
//				String subject=ds.get("subject");
//				String content=ds.get("content");
//				
//				String attachmentId=ds.get("attachment_id");
//				String attachmentName=ds.get("attachment_name");
//				String submitTime=ds.get("submit_time");
//				String replyCont=ds.get("reply_cont");
//				String readCont=ds.get("read_cont");
//				String parent=ds.get("parent");
//				String oldSubmitTime=ds.get("old_submit_time");
//				String top=ds.get("top");
//				String jing=ds.get("jing");
//				String readeder=ds.get("readeder");
//				String signedYn=ds.get("signed_yn");
//				String lockYn=ds.get("lock_yn");
				
				bbsB.setCommentId(rs.getInt("comment_id"));
				bbsB.setBoardId(rs.getInt("board_id"));
				bbsB.setUserId(rs.getString("user_id"));
				if(sessionUserId.equalsIgnoreCase(bbsB.getUserId())){
					bbsB.setIsAuthor(1);
				}
				bbsB.setAuthorName(rs.getString("author_name"));
				bbsB.setSubject(rs.getString("subject"));
				bbsB.setContent(rs.getString("content"));
				bbsB.setAttachmentId(rs.getString("attachment_id")==null?"":rs.getString("attachment_id"));
				bbsB.setAttachmentName(rs.getString("attachment_name")==null?"":rs.getString("attachment_name"));
				bbsB.setSubmitTime(rs.getTimestamp("submit_time")
						.toLocaleString());
				bbsB.setReplyCont(rs.getString("reply_cont")==null?"0":rs.getString("reply_cont"));
				bbsB.setReadCont(rs.getString("read_cont")==null?"0":rs.getString("read_cont"));
				bbsB.setParent(rs.getString("parent"));
				bbsB.setOldSubmitTime(rs.getTimestamp("old_submit_time")
						.toLocaleString());
				bbsB.setTop(rs.getString("top")==null?"0":rs.getString("top"));
				bbsB.setJing(rs.getString("jing")==null?"0":rs.getString("jing"));
				bbsB.setReadeder(rs.getString("readeder"));
				bbsB.setSignedYn(rs.getString("signed_yn")==null?"0":rs.getString("signed_yn"));
				bbsB.setLockYn(rs.getString("lock_yn")==null?"0":rs.getString("lock_yn"));
			}

			db.closeAll(rs);
			
			String sql2 = "select * from oa_bbs_comment where parent="
					+ bbsB.getCommentId() + " order by submit_time";
				ResultSet rs2 = db.executeQuery(sql2);
				List bdlist = new ArrayList();
					while (rs2.next()) {
						BbsComment bbsB2 = new BbsComment();
						
						bbsB2.setCommentId(rs2.getInt("comment_id"));
						bbsB2.setBoardId(rs2.getInt("board_id"));
						bbsB2.setUserId(rs2.getString("user_id"));
						if(sessionUserId.equalsIgnoreCase(bbsB2.getUserId())){
							bbsB2.setIsAuthor(1);
						}
						bbsB2.setAuthorName(rs2.getString("author_name"));
						bbsB2.setSubject(rs2.getString("subject"));
						bbsB2.setContent(rs2.getString("content"));
				bbsB2.setAttachmentId(rs2.getString("attachment_id")==null?"":rs2.getString("attachment_id"));
				bbsB2.setAttachmentName(rs2.getString("attachment_name")==null?"":rs2.getString("attachment_name"));
				bbsB2.setSubmitTime(rs2.getTimestamp("submit_time")
						.toLocaleString());
				bbsB2.setReplyCont(rs2.getString("reply_cont")==null?"0":rs2.getString("reply_cont"));
				bbsB2.setReadCont(rs2.getString("read_cont")==null?"0":rs2.getString("read_cont"));
						bbsB2.setParent(rs2.getString("parent"));
				bbsB2.setOldSubmitTime(rs2.getTimestamp("old_submit_time")
						.toLocaleString());
				bbsB2.setTop(rs2.getString("top")==null?"0":rs2.getString("top"));
				bbsB2.setJing(rs2.getString("jing")==null?"0":rs2.getString("jing"));
						bbsB2.setReadeder(rs2.getString("readeder"));
				bbsB2.setSignedYn(rs2.getString("signed_yn")==null?"0":rs2.getString("signed_yn"));
				bbsB2.setLockYn(rs2.getString("lock_yn")==null?"0":rs2.getString("lock_yn"));
						bdlist.add(bbsB2);
					}

					 db.closeAll(rs2);
					 bbsB.setReplyComments(bdlist);
			String sqlUpdateR = "update oa_bbs_comment set read_cont=read_cont+1 where comment_id="
					+ commentId;
				     db.executeUpdate(sqlUpdateR);
			String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
			String sqlUpdateRD = "";
			if (dbms.equals(YHConst.DBMS_ORACLE)) {
				sqlUpdateRD = "update oa_bbs_comment set readeder=CONCAT(readeder,'"
						+ sessionUserId
						+ ",') where comment_id="
						+ commentId
						+ " and  instr(readeder,'" + sessionUserId + ",') is null";
			} else {
				sqlUpdateRD = "update oa_bbs_comment set readeder=CONCAT(readeder,'"
						+ sessionUserId
						+ ",') where comment_id="
						+ commentId
						+ " and NOT find_in_set('"
						+ sessionUserId
						+ "',readeder)";
			}
				     db.executeUpdate(sqlUpdateRD);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				db.closeAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return bbsB;
	
	}
	
	public BbsComment getEditBBSCommentByCommentId(HttpServletRequest request,
			Object o, String commentId) throws Exception {
		DBManager db = new DBManager(request);

		BbsComment bbsB = new BbsComment();
//		 request.getSession().setAttribute(BBSUtil.bbsAuthor, "0");	
		try {
			String sessionUserId = BBSUtil.getFieldValueByName("userId", o)
					.toString();
	
			String sql = "select *from oa_bbs_comment where comment_id="
					+ commentId;
		
		ResultSet rs = db.executeQuery(sql);
//		ResultSet rsFilter = db.executeQuery(userPrivSql);	
			if(rs.next()) {
//				String commentId=ds.get("comment_id");
//				String boardId=ds.get("board_id");
//				String userId=ds.get("user_id");
//				String authorName=ds.get("author_name");
//				String subject=ds.get("subject");
//				String content=ds.get("content");
//				
//				String attachmentId=ds.get("attachment_id");
//				String attachmentName=ds.get("attachment_name");
//				String submitTime=ds.get("submit_time");
//				String replyCont=ds.get("reply_cont");
//				String readCont=ds.get("read_cont");
//				String parent=ds.get("parent");
//				String oldSubmitTime=ds.get("old_submit_time");
//				String top=ds.get("top");
//				String jing=ds.get("jing");
//				String readeder=ds.get("readeder");
//				String signedYn=ds.get("signed_yn");
//				String lockYn=ds.get("lock_yn");
				
				bbsB.setCommentId(rs.getInt("comment_id"));
				bbsB.setBoardId(rs.getInt("board_id"));
				bbsB.setUserId(rs.getString("user_id"));
				if(sessionUserId.equalsIgnoreCase(bbsB.getUserId())){
					bbsB.setIsAuthor(1);
				}
				bbsB.setAuthorName(rs.getString("author_name"));
				bbsB.setSubject(rs.getString("subject"));
				bbsB.setContent(rs.getString("content"));
				bbsB.setAttachmentId(rs.getString("attachment_id"));
				bbsB.setAttachmentName(rs.getString("attachment_name"));
				bbsB.setSubmitTime(rs.getTimestamp("submit_time")
						.toLocaleString());
				bbsB.setReplyCont(rs.getString("reply_cont"));
				bbsB.setReadCont(rs.getString("read_cont"));
				bbsB.setParent(rs.getString("parent"));
				bbsB.setOldSubmitTime(rs.getTimestamp("old_submit_time")
						.toLocaleString());
				bbsB.setTop(rs.getString("top"));
				bbsB.setJing(rs.getString("jing"));
				bbsB.setReadeder(rs.getString("readeder"));
				bbsB.setSignedYn(rs.getString("signed_yn"));
				bbsB.setLockYn(rs.getString("lock_yn"));
			}
			db.closeAll(rs);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				db.closeAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return bbsB;
	
	}
	
	public void addBoard(HttpServletRequest request) throws Exception{
//		System.out.println("22222222222222222222222222");
		String boardId=request.getParameter("boardId");
		
		String dept=request.getParameter("dept");
	      String role=request.getParameter("role");
	      String user=request.getParameter("user");
	      String hoster=request.getParameter("hoster");
	      String bbsNo=request.getParameter("NO");
	      String bbsName=request.getParameter("bbsName");
	      String welcome=request.getParameter("welcome");
	      String anonymity=request.getParameter("ANONYMITY_YN");
	      List array=new ArrayList();
	      array.add(bbsName);
	      array.add(anonymity);
	      array.add(welcome);
	      array.add(hoster);
	      array.add(dept);
	      array.add(role);
	      array.add(user);
	      array.add(bbsNo);
	      DBManager db=new DBManager(request);
	      String sql="";
		if (boardId != null && !boardId.equals("")
				&& Integer.parseInt(boardId) > 0) {
			sql = "update oa_bbs_board set BOARD_NAME=?,ANONYMITY_YN=?,WELCOME_TEXT=?,BOARD_HOSTER=?,DEPT_ID=?,PRIV_ID=?,USER_ID=?,BOARD_NO=? where board_id="
					+ boardId;
			}else{
	           sql="insert into oa_bbs_board (BOARD_NAME,ANONYMITY_YN,WELCOME_TEXT,BOARD_HOSTER,DEPT_ID,PRIV_ID,USER_ID,BOARD_NO)";
	          sql=sql+"values(?,?,?,?,?,?,?,?)";   
			}
	      try {
			db.executePrepared(sql,array);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				db.closeAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int addComment(HttpServletRequest request, String attachIds,
			String attachNames) throws Exception {
		HttpSession s =request.getSession();
		int keyId=0;
		Object o=s.getAttribute("LOGIN_USER");
//		System.out.println(o);
		String userId=BBSUtil.getFieldValueByName("userId", o).toString();
		String userName=BBSUtil.getFieldValueByName("userName", o).toString();
	      DBManager db=new DBManager(request);
		  String subject=request.getAttribute("subject").toString();
//	      String content=request.getAttribute("content").toString();
	      String econtent=request.getAttribute("Econtent").toString();
//	      System.out.println(econtent);
	      String boardId=request.getAttribute("boardId").toString();
	      String parent=request.getAttribute("parent").toString();
	      String authName=request.getAttribute("AUTHOR_NAME").toString();
	      if(authName.equalsIgnoreCase("2")){
	    	  userName=request.getAttribute("NICK_NAME").toString();
	      }
	      if(parent==null || parent.equalsIgnoreCase("")){
	    	  parent="0";
	      }
	      List array=new ArrayList();
		String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
		String sql = "";
		String keySql="";
//		if (dbms.equals(YHConst.DBMS_ORACLE)) {
//			array.add(subject);
//		}
	      array.add(subject);
	      array.add(econtent);
	      array.add(boardId);
	      array.add(parent);
	      array.add(attachIds);
	      array.add(attachNames);
	      array.add(userId);
	      array.add(userName);
	      array.add(new Date());
	      array.add(new Date());
	      array.add("");
	
		if (dbms.equals(YHConst.DBMS_ORACLE)) {
			sql = "insert into oa_bbs_comment"
					+ " (comment_id,reply_cont,read_cont,jing,top,lock_yn,subject,content,board_id,parent,attachment_id,attachment_name,"
					+ "user_id,author_name,submit_time,old_submit_time,readeder)";
			sql = sql + "values(?,0,0,'0','0',0,?,?,?,?,?,?,?,?,?,?,?)";
			keySql="select seq_BBS_COMMENT.nextval from dual";
		} else {
			sql = "insert into oa_bbs_comment"
					+ " (subject,content,board_id,parent,attachment_id,attachment_name,"
					+ "user_id,author_name,submit_time,old_submit_time,readeder)";
	      sql=sql+"values(?,?,?,?,?,?,?,?,?,?,?)";   
		}
	      try {
			keyId = db.executePreparedReturnKey(sql, array, keySql);
			 if(Integer.parseInt(parent)>0){
				db
						.executeUpdate("update oa_bbs_comment set reply_cont=reply_cont+1 where comment_id="
								+ parent);
		      }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				db.closeAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return keyId;
	}
	
	public int editComment(HttpServletRequest request, String attachIds,
			String attachNames) throws Exception {
		HttpSession s =request.getSession();
		int keyId=0;
		Object o=s.getAttribute("LOGIN_USER");
//		System.out.println(o);
		String userId=BBSUtil.getFieldValueByName("userId", o).toString();
		String userName=BBSUtil.getFieldValueByName("userName", o).toString();
	      DBManager db=new DBManager(request);
		  String subject=request.getAttribute("subject").toString();
		  String commentId=request.getAttribute("commentId").toString();
//	      String content=request.getAttribute("content").toString();
	      String econtent=request.getAttribute("Econtent").toString();
//	      System.out.println(econtent);
	      String boardId=request.getAttribute("boardId").toString();
	      String parent=request.getAttribute("parent").toString();
	      String authName=request.getAttribute("AUTHOR_NAME").toString();
	      String attachIdsDel=request.getAttribute("attachsDelIds").toString();
//	      System.out.println("33333333333333:"+attachIdsDel);
	      if(authName.equalsIgnoreCase("2")){
	    	  userName=request.getAttribute("NICK_NAME").toString();
	      }
	      if(parent==null || parent.equalsIgnoreCase("")){
	    	  parent="0";
	      }
	     
		String sql = "update oa_bbs_comment set subject=?,content=?,attachment_id=?,attachment_name=?,author_name=?,submit_time=?,old_submit_time=? where comment_id="
				+ commentId;
 
	      try {
	    	
			String searchSql = "select attachment_id,attachment_name,submit_time from oa_bbs_comment where comment_id="
					+ commentId;
		      
		      ResultSet rs=db.executeQuery(searchSql);
		      String orgIds="";
		      String orgNames="";
		      Date oldSubmitTime=null;
	    	  if(rs.next()){
	    		  orgIds= rs.getString("attachment_id");
	    		  orgNames=rs.getString("attachment_name");
	    		  oldSubmitTime=rs.getTimestamp("submit_time");
				if (attachIdsDel != null && !attachIdsDel.equals("")) {
					List arrayList = BBSUtil.moveAttach(orgIds, attachIdsDel,
							orgNames);
	    			orgIds=arrayList.get(0).toString();
	    			orgNames=arrayList.get(1).toString();
	    		 }
	    		  
	    	  }
	    	  db.closeAll(rs);
	    	  List array=new ArrayList();
	    	  array.add(subject);
		      array.add(econtent);
		      array.add(orgIds+attachIds);
		      array.add(orgNames+attachNames);
		      array.add(userName);
		      array.add(new Date());
		      array.add(oldSubmitTime);
			db.executePrepared(sql, array);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				db.closeAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Integer.parseInt(commentId);
	}
	
	public void addRead(int commentId, HttpServletRequest request)
			throws Exception {
		 DBManager db=new DBManager(request);
		try {
			String sql = "update oa_bbs_comment set read_cont=read_cont+1 where comment_id="
					+ commentId;
	      db.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				db.closeAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void deleteComment(HttpServletRequest request) throws Exception{
		String commentId=request.getParameter("commentId");
		String sql = "delete from oa_bbs_comment where comment_id=" + commentId
				+ " or parent=" + commentId;
		 DBManager db=new DBManager(request);
		try {

		      db.executeUpdate(sql);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					db.closeAll();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}

	public void deleteBoard(HttpServletRequest request,boolean isCloseConn) throws Exception {
		String boardId=request.getParameter("boardId");
		String sql="delete from oa_bbs_comment where board_id="+boardId;
		String sql2="delete from oa_bbs_board where board_id="+boardId;
		 DBManager db=new DBManager(request);
		try {

		      db.executeUpdate(sql);
		      db.executeUpdate(sql2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
				if(isCloseConn)
					db.closeAll();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}

	public void mOrUpdateComments(HttpServletRequest request) throws Exception{
		String flag=request.getParameter("flag");
		String commentIds=request.getParameter("commentIds");
		if(commentIds.endsWith(",")){
			commentIds=commentIds.substring(0,commentIds.length()-1);
		}
		if(flag.equals("d")){
			String sql = "delete from oa_bbs_comment where comment_id in("
					+ commentIds + ") or parent in(" + commentIds + ")";
			 DBManager db=new DBManager(request);
			try {

			      db.executeUpdate(sql);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						db.closeAll();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		} else if (flag.equals("j")) {
			
			String sql = "update  oa_bbs_comment set jing=ABS(jing-1)where comment_id in("
					+ commentIds + ")";
			 DBManager db=new DBManager(request);
			try {
			      db.executeUpdate(sql);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						db.closeAll();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
		}else if(flag.equals("t")){
			String sql = "update  oa_bbs_comment set top=ABS(top-1)where comment_id in("
					+ commentIds + ")";
			 DBManager db=new DBManager(request);
			try {
			      db.executeUpdate(sql);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						db.closeAll();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}
	}
}
