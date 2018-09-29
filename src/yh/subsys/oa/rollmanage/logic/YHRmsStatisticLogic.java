package yh.subsys.oa.rollmanage.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHRmsStatisticLogic {
  private static Logger log = Logger.getLogger(YHRmsStatisticLogic.class);

  /**
   * 查看文件
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
   public String getRmsFileJosn(Connection dbConn, Map request, YHPerson person) throws Exception {
     String sql="SELECT SEQ_ID," +
                     "FILE_CODE," +
                     "FILE_TITLE," +
                     "SECRET," +
                     "SEND_UNIT," +
                     "SEND_DATE," +
                     "URGENCY from oa_archives_attach where not ROLL_ID = 0 and ( DEL_USER = '' or DEL_USER is null )";

     YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
     YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
     return pageDataList.toJson();
   }
   
   /**
    * 获取借阅次数
    * @param dbConn
    * @param seqId
    * @return
    * @throws Exception
    */
   public long getRmsLendCount(Connection dbConn, int seqId)
   throws Exception {
     Statement stmt = null;
     ResultSet rs = null;
     long count = 0;
     try {
       stmt = dbConn.createStatement();
       String sql = "SELECT count(*) FROM oa_archives_lend WHERE FILE_ID = " + seqId;
       rs = stmt.executeQuery(sql);
       if (rs.next()) {
         count = rs.getLong(1);
       }
     } catch (Exception ex) {
       throw ex;
     } finally {
       YHDBUtility.close(stmt, rs, log);
     }
    return count;
  }
   
   /**
    * 获取文件个数
    * @param dbConn
    * @param seqId
    * @return
    * @throws Exception
    */
   public long getRmsRollCount(Connection dbConn, int seqId)
   throws Exception {
     Statement stmt = null;
     ResultSet rs = null;
     long count = 0;
     try {
       stmt = dbConn.createStatement();
       String sql = "SELECT count(*) FROM oa_archives_attach WHERE ROLL_ID = " + seqId + " and (DEL_USER is null or DEL_USER = '')";
       rs = stmt.executeQuery(sql);
       if (rs.next()) {
         count = rs.getLong(1);
       }
     } catch (Exception ex) {
       throw ex;
     } finally {
       YHDBUtility.close(stmt, rs, log);
     }
    return count;
  }
   
   public String getRmsStatisticJson(Connection dbConn, Map request, String seqId) throws Exception {
     String sql  = "select "
                + "RMS_ROLL.SEQ_ID"
                + ",RMS_ROLL.ROLL_CODE"
                + ",RMS_ROLL.ROLL_NAME"
                + ",RMS_ROLL.ROOM_ID"
                + ",RMS_ROLL.CATEGORY_NO"
                + ",RMS_ROLL.CERTIFICATE_KIND"
                + ",RMS_ROLL.SECRET"
                + ",RMS_ROLL.STATUS"
                + " from oa_archives_volume as RMS_ROLL left join oa_archives_volume_room as RMS_ROLL_ROOM on RMS_ROLL.ROOM_ID = RMS_ROLL_ROOM.SEQ_ID where 1=1";
     if(!YHUtility.isNullorEmpty(seqId)){
       sql = sql + " and RMS_ROLL_ROOM.SEQ_ID=" + Integer.parseInt(seqId);
     }
     sql=sql+" order by (case when PRIORITY is null then 99999 else PRIORITY end)";
     YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
     YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
     return pageDataList.toJson();
   }
}
