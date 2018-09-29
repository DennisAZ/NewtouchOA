package yh.subsys.oa.rollmanage.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.rollmanage.data.YHRmsRollRoom;

public class YHRmsRollRoomLogic {
  
 public void add(Connection conn, YHRmsRollRoom rmsRollRoom) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(conn, rmsRollRoom);
    } catch(Exception ex) {
      throw ex;
    } finally {
      
    }
  }
 
 public String getRmsRollRoomJson(Connection dbConn, Map request, YHPerson person) throws Exception {
   String sql = "";
   if(person.isAdminRole()){
     sql = "select SEQ_ID,ROOM_CODE,ROOM_NAME,DEPT_ID from oa_archives_volume_room ORDER BY seq_id DESC LIMIT 300";
   }else{
     sql = "select "
       + "SEQ_ID"
       + ",ROOM_CODE"
       + ",ROOM_NAME"
       + ",DEPT_ID"
       + " from oa_archives_volume_room where ADD_USER = '" + String.valueOf(person.getSeqId()) + "'";
   }
   YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
   YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
   return pageDataList.toJson();
 }
 
 public YHRmsRollRoom getRmsRollRoomDetail(Connection conn,int seqId) throws Exception {
   
   try {
     YHORM orm = new YHORM();
     return (YHRmsRollRoom)orm.loadObjSingle(conn, YHRmsRollRoom.class, seqId);
   } catch(Exception ex) {
     throw ex;
   } finally {
     
   }
 }
 
 public void updateRmsRollRoom(Connection conn,YHRmsRollRoom rmsRollRoom) throws Exception {
   try {
     YHORM orm = new YHORM();
     orm.updateSingle(conn, rmsRollRoom);
   } catch(Exception ex) {
     throw ex;
   } finally {
   }
 }
 
 /**
  * 删除一条卷库
  * @param conn
  * @param seqId
  * @throws Exception
  */
 public void deleteSingle(Connection conn,int seqId) throws Exception {
   try {
     YHORM orm = new YHORM();
     orm.deleteSingle(conn, YHRmsRollRoom.class, seqId);
   } catch(Exception ex) {
     throw ex;
   } finally {
   }
 }
 
 public void deleteAll(Connection conn, String loginUserId, YHPerson person) throws Exception {
   String sql = "";
   if(person.isAdminRole()){
     sql = "DELETE FROM oa_archives_volume_room";
   }else{
     sql = "DELETE FROM oa_archives_volume_room WHERE ADD_USER = '" + loginUserId + "'";
   }
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
 
}
