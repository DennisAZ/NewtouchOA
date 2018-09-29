package yh.subsys.oa.meeting.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.meeting.data.YHMeetingEquipment;
import yh.subsys.oa.meeting.data.YHMeetingRoom;

public class YHMeetingEquipmentLogic {
	private static Logger log = Logger.getLogger(YHMeetingEquipmentLogic.class);

	/**
	 * 设备管理列表--cc
	 * @param dbConn
	 * @param request
	 * @param cycleNo
	 * @return
	 * @throws Exception
	 */
	public String getMeetingEquipmentList(Connection dbConn, Map request, String cycleNo) throws Exception {
    String sql = "select " 
            + "  SEQ_ID" 
            + ", GROUP_NO" 
            + ", EQUIPMENT_NO" 
            + ", EQUIPMENT_NAME" 
            + ", MR_ID" 
            + ", EQUIPMENT_STATUS" 
            + ", GROUP_YN"
            + ", REMARK"
            + " from oa_conference_equipment order by MR_ID";
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
    return pageDataList.toJson();
  }
	
	/**
	 * 获取小编码表内容--同类设备名称--cc
	 * @param conn
	 * @param classCode
	 * @param classNo
	 * @return
	 * @throws Exception
	 */
	public String getCodeNameLogic(Connection conn, String classCode) throws Exception {
    String result = "";
    String sql = " select CLASS_DESC from oa_kind_dict_item where CLASS_CODE = '" + classCode + "' and CLASS_NO = 'MEETING_EQUIPMENT'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
	
	
	 /**
   * 删除单个设备管理--cc
   * 
   * @param conn
   * @param seqId
   * @throws Exception
   */
  public void deleteSingle(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.deleteSingle(conn, YHMeetingEquipment.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }

  /**
   * 设备查询--cc
   * @param dbConn
   * @param request
   * @param person
   * @param mName
   * @param mProposer
   * @param beginDate
   * @param endDate
   * @param mRoom
   * @param mStatus
   * @return
   * @throws Exception
   */
  public String getMeetingEquiomentSearchJson(Connection dbConn, Map request,  String equipmentNo, String equipmentName, String equipmentStatus,
      String mrId, String remark) throws Exception {
    String sql = "select " 
      + "  SEQ_ID" 
      + ", GROUP_NO" 
      + ", EQUIPMENT_NO" 
      + ", EQUIPMENT_NAME" 
      + ", MR_ID" 
      + ", EQUIPMENT_STATUS" 
      + ", GROUP_YN"
      + ", REMARK"
      + " from oa_conference_equipment where 1=1";

    if (!YHUtility.isNullorEmpty(equipmentNo)) {
      sql = sql + " and EQUIPMENT_NO like '%" + equipmentNo + "%'" + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(equipmentName)) {
      sql = sql + " and EQUIPMENT_NAME like '%" + equipmentName + "%'" + YHDBUtility.escapeLike();
    }
    
    if (!YHUtility.isNullorEmpty(equipmentStatus)) {
      sql = sql + " and EQUIPMENT_STATUS = '" + equipmentStatus + "'";
    }

    if (!YHUtility.isNullorEmpty(mrId)) {
      sql = sql + " and MR_ID ='" + mrId + "'";
    }

    if (!YHUtility.isNullorEmpty(remark)) {
      sql = sql + " and REMARK like '%" + remark + "%'" + YHDBUtility.escapeLike();
    }
    sql = sql + " order by EQUIPMENT_NO";

    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);

    return pageDataList.toJson();
  }
	/**
	 * 获取会议室名称
	 * 
	 * @param dbConn
	 * @return
	 * @throws Exception
	 */
	public String getMRoomNameLogic(Connection dbConn) throws Exception {
		StringBuffer buffer = new StringBuffer("[");
		try {
			List<YHMeetingRoom> meetingRooms = this.getMRoomList(dbConn);
			boolean isHave = false;
			if (meetingRooms != null && meetingRooms.size() > 0) {
				for (YHMeetingRoom meetingRoom : meetingRooms) {
					int dbSeqId = meetingRoom.getSeqId();
					String mrName = YHUtility.null2Empty(meetingRoom.getMrName());
					buffer.append("{");
					buffer.append("value:" + dbSeqId);
					buffer.append(",text:\"" + YHUtility.encodeSpecial(mrName) + "\"");
					buffer.append("},");
					isHave = true;
				}
			}
			if (isHave) {
				buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
		} catch (Exception e) {
			throw e;
		}
		return buffer.toString();
	}

	public List<YHMeetingRoom> getMRoomList(Connection dbConn) throws Exception {
		YHORM orm = new YHORM();
		try {
			Map map = new HashMap();
			return orm.loadListSingle(dbConn, YHMeetingRoom.class, map);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 添加设备信息
	 * 
	 * @param conn
	 * @param equipment
	 * @throws Exception
	 */
	public void addEquipmentLogic(Connection dbConn, YHMeetingEquipment equipment) throws Exception {
		try {
			YHORM orm = new YHORM();
			orm.saveSingle(dbConn, equipment);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 根据会议室seqId获取设备信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getEquipmentByIdLogic(Connection dbConn, int mRooId) throws Exception {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{selectDiv:\"");
		boolean isHaveBuffer = this.getEquipmentCheckBoxLogic(dbConn, mRooId, buffer);
		
		YHORM orm =new YHORM();
		String[] filters = { " SEQ_ID in(select min(SEQ_ID) from oa_conference_equipment where GROUP_YN='1' and EQUIPMENT_STATUS='1' and MR_ID=" + mRooId + " group by GROUP_NO)" };
		List<YHMeetingEquipment > equipments=orm.loadListSingle(dbConn, YHMeetingEquipment.class, filters);
	
		if (equipments!=null && equipments.size()>0) {
			
			for(YHMeetingEquipment equipment:equipments){
				int groupNoCount =0;
				String  groupNo1= YHUtility.null2Empty(equipment.getGroupNo());
				
				String[] queryStr = {" GROUP_YN='1' and EQUIPMENT_STATUS='1' and MR_ID=" + mRooId + " and GROUP_NO='" + groupNo1 + "'"};
				List<YHMeetingEquipment > equipments2 = orm.loadListSingle(dbConn, YHMeetingEquipment.class, queryStr);
				
				boolean isHave = false;
				if (equipments2 != null && equipments2.size() > 0) {
					for(YHMeetingEquipment equ:equipments2){
						int equId = equ.getSeqId();
						String equipmentName = YHUtility.null2Empty(equ.getEquipmentName());
//						String remark = YHUtility.null2Empty(equ.getRemark());
						groupNoCount ++;
						
						if (groupNoCount == 1) {
							String classDesc = this.getEquClassDescName(dbConn, groupNo1);
							buffer.append(" &nbsp;&nbsp;<select name='checkSelectStr' class=''> ");
							buffer.append("  <option value=''>选择" + classDesc + "</option>");
							isHave = true;
						}
						buffer.append("<option value='" + equId + "' title='" + equId + "' > " + YHUtility.encodeSpecial(equipmentName) + " </option>" );
					}
					if (isHave) {
						buffer.append("</select>");
					}
				}
			}
			buffer.append( "\"}");
		}else {
			if (isHaveBuffer) {
				buffer.append( "\"}");			
			}else {
				buffer.append("0");
				buffer.append( "\"}");			
			}
		}
		return buffer.toString();
	}
	/**
	 * 根据会议室seqId获取设备复选框信息
	 * @return
	 * @throws Exception
	 */
	public boolean getEquipmentCheckBoxLogic(Connection dbConn, int mRooId,StringBuffer buffer) throws Exception {
		YHORM orm =new YHORM();
		boolean isHave = false;
		String[] filters = { " GROUP_YN='0' and EQUIPMENT_STATUS='1' and MR_ID=" + mRooId + " order by EQUIPMENT_NO" };
		List<YHMeetingEquipment > equipments=orm.loadListSingle(dbConn, YHMeetingEquipment.class, filters);
		if (equipments!=null && equipments.size()>0) {
			for(YHMeetingEquipment equipment:equipments){
				int equId = equipment.getSeqId();
				String equipmentName = YHUtility.null2Empty(equipment.getEquipmentName());
				buffer.append("<input type='checkbox' name='checkStr' id='SB_" + equId + "' value='" + equId + "' > ");
				buffer.append("<label title='' for='SB_" + equId + "'>");
				buffer.append(YHUtility.encodeSpecial(equipmentName));
				buffer.append("</label>");
				isHave = true;
			}
		}
		return isHave;
	}
	
	

	
	

	/**
	 * 获取下拉列表值	 * @param dbConn
	 * @param parentNo
	 * @return
	 * @throws Exception
	 */
	public String getSelectOption(Connection dbConn, String classNo) throws Exception {
		String data = "";
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		StringBuffer sb = new StringBuffer("[");
		classNo = YHUtility.null2Empty(classNo);
		String query = "select SEQ_ID,CLASS_CODE,CLASS_DESC from oa_kind_dict_item where CLASS_NO='" + classNo + "'";
		Statement stm1 = null;
		ResultSet rs1 = null;
		try {
			stm1 = dbConn.createStatement();
			rs1 = stm1.executeQuery(query);
			while (rs1.next()) {
				Map<Object, Object> objMap = new HashMap<Object, Object>();
				objMap.put("seqId", rs1.getInt("SEQ_ID"));
				objMap.put("codeNo", YHUtility.null2Empty(rs1.getString("CLASS_CODE")));
				objMap.put("codeName", YHUtility.null2Empty(rs1.getString("CLASS_DESC")));
				list.add(objMap);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			YHDBUtility.close(stm1, rs1, log);
		}
		boolean isHave = false;
		if (list != null && list.size() > 0) {
			for (Map<Object, Object> map : list) {
				int seqId = (Integer) map.get("seqId");
				String codeNo = (String) map.get("codeNo");
				String codeName = (String) map.get("codeName");
				sb.append("{");
				sb.append("seqId:\"" + seqId + "\"");
				sb.append(",value:\"" + YHUtility.encodeSpecial(codeNo) + "\"");
				sb.append(",text:\"" + YHUtility.encodeSpecial(codeName) + "\"");
				sb.append("},");
				isHave = true;
			}
			if (isHave) {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("]");
		} else {
			sb.append("]");
		}
		data = sb.toString();
		return data;
	}
	
	/**
	 * 获取设备详细信息--wyw
	 * @param conn
	 * @param seqId
	 * @return
	 * @throws Exception
	 */
	public YHMeetingEquipment getEquipmentLogic(Connection dbConn, int seqId) throws Exception {
		try {
			YHORM orm = new YHORM();
			return (YHMeetingEquipment) orm.loadObjSingle(dbConn, YHMeetingEquipment.class, seqId);
		} catch (Exception ex) {
			throw ex;
		} 
	}
	
	/**
	 * 更新设备信息--wyw
	 * @param dbConn
	 * @param equipment
	 * @throws Exception 
	 */
	public void updateEquipmentLogic(Connection dbConn,YHMeetingEquipment equipment) throws Exception{
		YHORM orm = new YHORM();
		try {
			orm.updateSingle(dbConn, equipment);
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 获取CODE_ITEM表设备中的CLASS_CODE-- wyw
	 * @param dbConn
	 * @param classCode
	 * @return
	 * @throws Exception
	 */
	public String getEquClassDescName(Connection dbConn,String classCode) throws Exception{
		String returnData = "";
		if (YHUtility.isNullorEmpty(classCode)) {
			classCode = "";
		}
		String sql  = "select SEQ_ID,CLASS_CODE,CLASS_DESC from oa_kind_dict_item where CLASS_NO='MEETING_EQUIPMENT' and CLASS_CODE='" + classCode + "'";
		PreparedStatement stmt =null;
		ResultSet rs=null;
		try {
			stmt =dbConn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				returnData = YHUtility.null2Empty(rs.getString("CLASS_DESC"));
			}
		} catch (Exception e) {
			throw e;
		}finally{
			YHDBUtility.close(stmt, rs, log);
		}
		
		return returnData;
	}
	
	
	
	
	

}
