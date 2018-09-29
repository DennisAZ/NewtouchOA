package yh.subsys.oa.meeting.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.meeting.data.YHMeetingRoom;

public class YHMeetingRoomLogic {

	private static Logger log = Logger.getLogger(YHMeetingRoomLogic.class);

	public String getMeetingRoomJson(Connection dbConn, Map request) throws Exception {
		String sql = "";
		sql = "select " + "  SEQ_ID" + ", MR_NAME" + ", MR_CAPACITY" + ", MR_DEVICE" + ", MR_PLACE" + ", MR_DESC" + ", OPERATOR" + ", TO_ID"
				+ ", SECRET_TO_ID" + " from oa_conference_room  order by SEQ_ID";

		YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
		YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
		return pageDataList.toJson();
	}

	public YHMeetingRoom getMeetingRoomDetail(Connection conn, int seqId) throws Exception {
		try {
			YHORM orm = new YHORM();
			return (YHMeetingRoom) orm.loadObjSingle(conn, YHMeetingRoom.class, seqId);
		} catch (Exception ex) {
			throw ex;
		} finally {

		}
	}

	/**
	 * 编辑案卷
	 * 
	 * @param conn
	 * @param rmsRoll
	 * @throws Exception
	 */
	public void updateMeetingRoom(Connection conn, YHMeetingRoom meetingRoom) throws Exception {
		try {
			YHORM orm = new YHORM();
			orm.updateSingle(conn, meetingRoom);
		} catch (Exception ex) {
			throw ex;
		} finally {
		}
	}

	/**
	 * 删除单个会议室记录
	 * 
	 * @param conn
	 * @param seqId
	 * @throws Exception
	 */
	public void deleteSingle(Connection conn, int seqId) throws Exception {
		try {
			YHORM orm = new YHORM();
			orm.deleteSingle(conn, YHMeetingRoom.class, seqId);
		} catch (Exception ex) {
			throw ex;
		} finally {
		}
	}

	/**
	 * 全部删除会议室记录
	 * 
	 * @param conn
	 * @throws Exception
	 */
	public void deleteAll(Connection conn) throws Exception {
		String sql = "delete from oa_conference_room";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			YHDBUtility.close(pstmt, null, null);
		}
	}

	/**
	 * 获取会议详细信息 --wyw
	 * @param dbConn
	 * @param person
	 * @return
	 * @throws Exception
	 */
	public String getRoomDetailLogic(Connection dbConn, YHPerson person) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		YHORM orm = new YHORM();
		StringBuffer buffer=new StringBuffer("[");
		
		try {
			List<YHMeetingRoom> mRooms = orm.loadListSingle(dbConn, YHMeetingRoom.class, new HashMap());
			if (mRooms != null && mRooms.size() > 0) {
				for(YHMeetingRoom mRoom:mRooms){
					String mrName=YHUtility.null2Empty(mRoom.getMrName());
					String mrCapacity=YHUtility.null2Empty(mRoom.getMrCapacity());
					String mrDevice=YHUtility.null2Empty(mRoom.getMrDevice());
					String mrDesc=YHUtility.null2Empty(mRoom.getMrDesc());
					String mrPlace=YHUtility.null2Empty(mRoom.getMrPlace());
					String secretToId=YHUtility.null2Empty(mRoom.getSecretToId());
					String operator=YHUtility.null2Empty(mRoom.getOperator());
					String toId=YHUtility.null2Empty(mRoom.getToId());
					boolean secretToIdPriv=this.isHavePriv(person.getSeqId(), secretToId, dbConn);
					boolean operatorPriv=this.isHavePriv(person.getSeqId(), operator, dbConn);
					boolean toIdPriv=this.getDeptIdPriv(person, toId, dbConn);
					
					if (secretToIdPriv || operatorPriv || toIdPriv || ("".equals(toId) &&  "".equals(secretToId)) ) {
						buffer.append("{");
						buffer.append("seqId:" + mRoom.getSeqId() );
						buffer.append(",mrName:\"" + YHUtility.encodeSpecial(mrName) + "\"");
						buffer.append(",mrCapacity:\"" + YHUtility.encodeSpecial(mrCapacity) + "\"");
						buffer.append(",mrDevice:\"" + YHUtility.encodeSpecial(mrDevice) + "\"");
						buffer.append(",mrDesc:\"" + YHUtility.encodeSpecial(mrDesc) + "\"");
						buffer.append(",mrPlace:\"" + YHUtility.encodeSpecial(mrPlace) + "\"");
						buffer.append("},");
					}
				}
			}
			if(buffer.length() > 1){
				buffer.deleteCharAt(buffer.length() - 1);
      }
			buffer.append("]");

		} catch (Exception e) {
			throw e;
		}
		return buffer.toString();
	}
	
	/**
	 * 根据登录用户的部门Id与权限中的部门Id对比返回boolean
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public boolean getDeptIdPriv(YHPerson person, String ids, Connection dbConn) throws Exception, Exception {
		boolean flag = false;
		int loginUserDeptId=person.getDeptId();
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
	 * 获取访问权限：根据ids串返回与登录的seqId比较判断是否相等，返回boolean类型。
	 * @param ids
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public boolean isHavePriv(int userSeqId, String ids, Connection dbConn) throws Exception, Exception {
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
}
