package yh.subsys.oa.fillRegister.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.fillRegister.data.YHAttendFill;

public class YHAttendApprovalLogic {

  private static Logger log = Logger.getLogger(YHAttendApprovalLogic.class);
  
  /**
   * 获取补登记审批列表
   * @param dbConn
   * @param request
   * @param assessingStatus
   * @param person
   * @return
   * @throws Exception
   */
  public String getRegisterApprovalListJson(Connection dbConn, Map request, String assessingStatus, YHPerson person) throws Exception {
    String sql = "";
//    if(person.isAdminRole()){
      sql = "select " 
        + "  SEQ_ID" 
        + ", ASSESSING_STATUS" 
        + ", PROPOSER" 
        + ", REGISTER_TYPE" 
        + ", FILL_TIME"
        + ", ASSESSING_OFFICER"
        + " from oa_attendance_add where  ASSESSING_STATUS = '" + assessingStatus + "' ORDER BY FILL_TIME, REGISTER_TYPE asc";
//    }else{
//      sql = "select " 
//        + "  SEQ_ID" 
//        + ", ASSESSING_STATUS" 
//        + ", PROPOSER" 
//        + ", REGISTER_TYPE" 
//        + ", FILL_TIME"
//        + ", ASSESSING_OFFICER"
//        + " from oa_attendance_add where (PROPOSER = '" + person.getSeqId() + "' or ASSESSING_OFFICER = '" + person.getSeqId() + "') and ASSESSING_STATUS = '" + assessingStatus + "' ORDER BY FILL_TIME, REGISTER_TYPE asc";
//    }
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
    return pageDataList.toJson();
  }
  
  public String getRegisterApprovalPassJson(Connection dbConn, Map request, String assessingStatus, YHPerson person, String begin, String end) throws Exception {
    String sql = "";
      sql = "select " 
        + "  SEQ_ID" 
        + ", ASSESSING_STATUS" 
        + ", PROPOSER" 
        + ", REGISTER_TYPE" 
        + ", FILL_TIME"
        + ", ASSESSING_OFFICER"
        + " from oa_attendance_add where  ASSESSING_STATUS = '" + assessingStatus + "' and " + YHDBUtility.getDateFilter("FILL_TIME", end+ " 23:59:59", "<=") + " and "+YHDBUtility.getDateFilter("FILL_TIME", begin+ " 00:00:00", ">=") +"ORDER BY FILL_TIME, REGISTER_TYPE asc";

    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
    return pageDataList.toJson();
  }
  /**
   * 取得用户名称--cc
   * 
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */

  public String getUserNameLogic(Connection conn, int userId) throws Exception {
    String result = "";
    String sql = " select USER_NAME from PERSON where SEQ_ID = " + userId;
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
  
  public static void doSmsBackTime(Connection conn, String content, int fromId, String toId, String type, String remindUrl, Date sendDate)
  throws Exception {
    YHSmsBack sb = new YHSmsBack();
    sb.setContent(content);
    sb.setFromId(fromId);
    sb.setToId(toId);
    sb.setSmsType(type);
    sb.setRemindUrl(remindUrl);
    sb.setSendDate(sendDate);
    YHSmsUtil.smsBack(conn, sb);
  }
  
  public YHAttendFill getPlanDetail(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHAttendFill) orm.loadObjSingle(conn, YHAttendFill.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  /**
   * 处长主观分
   * @param conn
   * @param year
   * @param month
   * @param userId
   * @return
   * @throws Exception
   */
  public String getDirectorScoreStr(Connection conn, String year, String month, String userId) throws Exception {
    String result = "";
    String ymd = "";
    if(year == null){
      ymd = year+"-"+month+"-"+"07";
   }else{
      ymd = year+"-"+month+"-"+"07";
   }
    String sql = " select SCORE from oa_score_data where PARTICIPANT='"+userId+"' and "+ YHDBUtility.getMonthFilter("RANK_DATE", YHUtility.parseDate(ymd));
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result += toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  public int getDirectorScore(Connection conn, String year, String month, String userId) throws Exception {
    String data = getDirectorScoreStr(conn, year, month, userId);
    int result = 0;
 
    try {
      String dataStr[] = data.split(",");
      for(int i = 0; i < dataStr.length; i++){
        if(!YHUtility.isNullorEmpty(dataStr[i])){
          int val = Integer.parseInt(dataStr[i]);
          result = result + val;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      //YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 某月份对映的考核分数

   * @param conn
   * @param year
   * @param month
   * @param userId
   * @return
   * @throws Exception
   */
  public String getAttendScore(Connection conn, String year, String month, String userId) throws Exception {
    String result = "";
    String ymd = year + "-" + month + "-" + "01";
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance(); 
    calendar.setTime(dateFormat1.parse(ymd)); 
    int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数 
    double totalScore = 0;
    for(int i = 0;i < maxDay; i++){
      calendar.setTime(dateFormat1.parse(ymd));
      calendar.add(Calendar.DATE,+i) ;
      Date dateTemp = calendar.getTime();
      String dateTempStr = dateFormat1.format(dateTemp);
      String beginDate = dateTempStr + " 00:00:00";
      String endDate = dateTempStr + " 23:59:59";
      double reduceScore1 = getAttendScoreStr(conn, beginDate,endDate, userId, "1");  //扣分
      double reduceScore2 = getAttendScoreStr(conn, beginDate,endDate, userId, "2");
      double reduceScore3 = getAttendScoreStr(conn, beginDate,endDate, userId, "3");
      double reduceScore4 = getAttendScoreStr(conn, beginDate,endDate, userId, "4");
      double reduceScore5 = getAttendScoreStr(conn, beginDate,endDate, userId, "5");
      double reduceScore6 = getAttendScoreStr(conn, beginDate,endDate, userId, "6");
      //System.out.println(reduceScore);
      double type1 = Double.parseDouble(getAttendFillStr(conn, beginDate,endDate, userId, "1"));     //回冲分
      double type2 = Double.parseDouble(getAttendFillStr(conn, beginDate,endDate, userId, "2"));
      double type3 = Double.parseDouble(getAttendFillStr(conn, beginDate,endDate, userId, "3"));
      double type4 = Double.parseDouble(getAttendFillStr(conn, beginDate,endDate, userId, "4"));
      double type5 = Double.parseDouble(getAttendFillStr(conn, beginDate,endDate, userId, "5"));
      double type6 = Double.parseDouble(getAttendFillStr(conn, beginDate,endDate, userId, "6"));
      double num = reduceScore1*type1 + reduceScore2*type2 + reduceScore3*type3 + reduceScore4*type4 + reduceScore5*type5 + reduceScore6*type6;
      if(num > Double.parseDouble(getMaxScore(conn))){
        num = Double.parseDouble(getMaxScore(conn));
      }      
      
      
      totalScore += num;
    }
    return result;
  }
  
  /**
   * 出差统计天数－－进行自动补登记
   * @param conn
   * @param year
   * @param month
   * @param userId
   * @return
   * @throws Exception
   */
  public String getAttendEvection(Connection conn, String year, String month, String userId) throws Exception {
    String result = "";
    String ymd = "";
    if(year == null){
      ymd = year+"-"+month+"-"+"07";
   }else{
      ymd = year+"-"+month+"-"+"07";
   }
    String sql = " select EVECTION_DATE1, EVECTION_DATE2 from oa_attendance_trip where ALLOW ='1' and STATUS = '1' and PARTICIPANT='"+userId+"' and "+ YHDBUtility.getMonthFilter("EVECTION_DATE1", YHUtility.parseDate(ymd));
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        Date beginDate = rs.getDate(1);
        Date endDate = rs.getDate(2);
        String toId = "";
        if (toId != null) {
          result += toId;
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
   * 获取扣的分数
   * @param conn
   * @param year
   * @param userId
   * @return
   * @throws Exception
   */
  public double getAttendScoreStr(Connection conn, String beginDate, String endDate, String userId, String registerType) throws Exception {
     double result = 0;
     double num = 0;
     String sql = " select SCORE from oa_attendance_integral where USER_ID = '" + userId 
                 + "' and " + YHDBUtility.getDateFilter("CREATE_TIME", beginDate, ">=") 
                 + " and " +  YHDBUtility.getDateFilter("CREATE_TIME", endDate, "<=") 
                 + " and REGISTER_TYPE = '" + registerType + "'";
     PreparedStatement ps = null;
     ResultSet rs = null;
     try {
       ps = conn.prepareStatement(sql);
       rs = ps.executeQuery();
       while (rs.next()) {
         String toId = rs.getString(1);
         if (!YHUtility.isNullorEmpty(toId)) {
           result += Double.parseDouble(toId);
           if(result > Double.parseDouble(getMaxScore(conn))){
             result = Double.parseDouble(getMaxScore(conn));
             break;
           }
         }else{
           result += Double.parseDouble(getMaxScore(conn));
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
  * 获取补登记状态
  * @param conn
  * @param year
  * @param userId
  * @return
  * @throws Exception
  */
   public String getAttendFillStr(Connection conn, String beginDate, String endDate, String userId, String registerType) throws Exception {
     String result = "1";
     String sql = " select ATTEND_FLAG from oa_attendance_add where PROPOSER ='" + userId 
                 + "' and " + YHDBUtility.getDateFilter("FILL_TIME", beginDate, ">=") 
                 + " and " +  YHDBUtility.getDateFilter("FILL_TIME", endDate, "<=") 
                 + " and REGISTER_TYPE = '" + registerType + "'";
     PreparedStatement ps = null;
     ResultSet rs = null;
     try {
       ps = conn.prepareStatement(sql);
       rs = ps.executeQuery();
       while (rs.next()) {
         String toId = rs.getString(1);
         if(toId != null){
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
  * 获取最大扣除的分数
  * @param conn
  * @return
  * @throws Exception
  */
   public String getMaxScore(Connection conn) throws Exception {
     String result = "0";
     String sql = " select MAX(SCORE) from oa_attendance_time";
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
 	 * 补登记(审批)查询
 	 * @param dbConn
 	 * @param request
 	 * @param map
 	 * @param person
 	 * @return
 	 * @throws Exception
 	 */
 	public String queryApprovalListJsonLogic(Connection dbConn, Map request, Map map, YHPerson person) throws Exception {
 		String assessingOfficer = (String) map.get("assessingOfficer");
 		String assessingStatus = (String) map.get("assessingStatus");
 		String beginDate = (String) map.get("beginDate");
 		String endDate = (String) map.get("endDate");
 		String conditionStr = "";
 		String sql = "";
 		try {
 			if (!YHUtility.isNullorEmpty(assessingOfficer)) {
 				conditionStr = " and ASSESSING_OFFICER ='" + YHDBUtility.escapeLike(assessingOfficer) + "'";
 			}
 			if (!YHUtility.isNullorEmpty(assessingStatus)) {
 				conditionStr += " and ASSESSING_STATUS ='" + YHDBUtility.escapeLike(assessingStatus) + "'";
 			}
 			if (!YHUtility.isNullorEmpty(beginDate)) {
 				conditionStr += " and " + YHDBUtility.getDateFilter("ASSESSING_TIME", beginDate, ">=");
 			}
 			if (!YHUtility.isNullorEmpty(endDate)) {
 				conditionStr += " and " + YHDBUtility.getDateFilter("ASSESSING_TIME", endDate, "<=");
 			}
 			if(person.isAdminRole()){
 				sql = "select " 
 					+ "  SEQ_ID" 
 					+ ", PROPOSER" 
 					+ ", REGISTER_TYPE" 
 					+ ", FILL_TIME"
 					+ ", ASSESSING_OFFICER"
 					+ ", ASSESSING_TIME"
 					+ ", ASSESSING_STATUS" 
 					+ " from oa_attendance_add where 1=1" + conditionStr + " ORDER BY SEQ_ID desc";
 			}else{
 				sql = "select " 
 					+ "  SEQ_ID" 
 					+ ", PROPOSER" 
 					+ ", REGISTER_TYPE" 
 					+ ", FILL_TIME"
 					+ ", ASSESSING_OFFICER"
 					+ ", ASSESSING_TIME"
 					+ ", ASSESSING_STATUS" 
 					+ " from oa_attendance_add where PROPOSER = '" + person.getSeqId() + "'" + conditionStr + " ORDER BY SEQ_ID desc";
 			}
 			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
 			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
 			return pageDataList.toJson();
 		} catch (Exception e) {
 			throw e;
 		}
 	}
 	
 	public void deleteSingle(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.deleteSingle(conn, YHAttendFill.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }
 	
 	public void deleteAll(Connection conn, String seqIdStr) throws Exception {
    String sql = "DELETE FROM oa_attendance_add WHERE SEQ_ID IN(" + seqIdStr + ")";
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
