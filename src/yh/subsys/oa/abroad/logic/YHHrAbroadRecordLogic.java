package yh.subsys.oa.abroad.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import yh.subsys.oa.abroad.data.YHHrAbroadRecord;

public class YHHrAbroadRecordLogic {
  private static Logger log = Logger.getLogger(YHHrAbroadRecordLogic.class);
  
  public void add(Connection dbConn, YHHrAbroadRecord record) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, record);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  public String getAbroadRecordListJson(Connection dbConn, Map request, YHPerson person) throws Exception {
    String sql = "";
    if(person.isAdminRole()){
      sql = "select " 
        + "  SEQ_ID" 
        + ", ABROAD_USER_ID" 
        + ", ABROAD_NAME" 
        + ", BEGIN_DATE" 
        + ", END_DATE" 
        + " from oa_pm_overseas where 1=1 ORDER BY SEQ_ID desc";
    }else{
      sql = "select " 
        + "  SEQ_ID" 
        + ", ABROAD_USER_ID" 
        + ", ABROAD_NAME" 
        + ", BEGIN_DATE" 
        + ", END_DATE" 
        + " from oa_pm_overseas where CREATE_USER_ID = '"+person.getSeqId()+"' ORDER BY SEQ_ID desc";
    }

    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
    return pageDataList.toJson();
  }

  public String getUserNameLogic(Connection conn, String userIdStr) throws Exception {
    String result = "";
    String sql = " select USER_NAME from PERSON where SEQ_ID IN (" + userIdStr + ")";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        String toId = rs.getString(1);
        if (!"".equals(result)) {
          result += ",";
        }
        result += toId;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 删除一条记录--cc
   * @param conn
   * @param seqId
   * @throws Exception
   */
  public void deleteSingle(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.deleteSingle(conn, YHHrAbroadRecord.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  /**
   * 批量删除--cc
   * @param conn
   * @param seqIdStr
   * @throws Exception
   */
  public void deleteAll(Connection conn, String seqIdStr) throws Exception {
    String sql = "DELETE FROM oa_pm_overseas WHERE SEQ_ID IN (" + seqIdStr + ")";
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
  
  public YHHrAbroadRecord getRecordDetail(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHHrAbroadRecord) orm.loadObjSingle(conn, YHHrAbroadRecord.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  public void updateRecord(Connection conn, YHHrAbroadRecord record) throws Exception {
  try {
        YHORM orm = new YHORM();
        orm.updateSingle(conn, record);
      } catch (Exception ex) {
        throw ex;
      } finally {
    }
  }
  
  /**
   * 出国记录查询  --wyw
   * @param dbConn
   * @param request
   * @param map
   * @param person
   * @return
   * @throws Exception
   */
	public String queryAbroadRecordListJsonLogic(Connection dbConn, Map request, Map map, YHPerson person) throws Exception {
		String abroadUserId = (String) map.get("abroadUserId");
		String abroadName = (String) map.get("abroadName");
		String beginDate = (String) map.get("beginDate");
		String beginDate1 = (String) map.get("beginDate1");
		String endDate = (String) map.get("endDate");
		String endDate1 = (String) map.get("endDate1");
		String remark = (String) map.get("remark");
		String conditionStr = "";
		String sql = "";
		try {
			if (!YHUtility.isNullorEmpty(abroadUserId)) {
				conditionStr = " and ABROAD_USER_ID ='" + YHDBUtility.escapeLike(abroadUserId) + "'";
			}
			if (!YHUtility.isNullorEmpty(abroadName)) {
				conditionStr += " and ABROAD_NAME ='" + YHDBUtility.escapeLike(abroadName) + "'";
			}
			if(!YHUtility.isNullorEmpty(beginDate)){ 
				 conditionStr += " and "+ YHDBUtility.getDateFilter("BEGIN_DATE", beginDate, ">=");
			} 
			if(!YHUtility.isNullorEmpty(beginDate1)){ 
				 conditionStr += " and "+ YHDBUtility.getDateFilter("BEGIN_DATE", beginDate1, "<=");
			}
			if(!YHUtility.isNullorEmpty(endDate)){ 
				conditionStr += " and "+ YHDBUtility.getDateFilter("BEGIN_DATE", endDate, ">=");
			} 
			if(!YHUtility.isNullorEmpty(endDate1)){ 
				conditionStr += " and "+ YHDBUtility.getDateFilter("BEGIN_DATE", endDate1, "<=");
			}
			if (!YHUtility.isNullorEmpty(remark)) {
				conditionStr += " and REMARK like '%" + YHDBUtility.escapeLike(remark) + "%'" + YHDBUtility.escapeLike();
			}
			
		  if(person.isAdminRole()){
	      sql = "select " 
	        + "  SEQ_ID" 
	        + ", ABROAD_USER_ID" 
	        + ", ABROAD_NAME" 
	        + ", BEGIN_DATE" 
	        + ", END_DATE" 
	        + " from oa_pm_overseas where 1=1 " + conditionStr + " ORDER BY SEQ_ID desc";
	    }else{
	      sql = "select " 
	        + "  SEQ_ID" 
	        + ", ABROAD_USER_ID" 
	        + ", ABROAD_NAME" 
	        + ", BEGIN_DATE" 
	        + ", END_DATE" 
	        + " from oa_pm_overseas where CREATE_USER_ID = '" + person.getSeqId()+ "'" + conditionStr + " ORDER BY SEQ_ID desc";
	    }
			YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
			YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
			return pageDataList.toJson();
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
   * 返回两个日期的相隔月份
   * @param startDate
   * @param endDate
   * @return
   * @throws Exception
   */
  public List<String> getDateValue(String startDateStr, String endDateStr) throws Exception {
    List<String> list = new ArrayList<String>();
    if (YHUtility.isNullorEmpty(endDateStr) && !YHUtility.isNullorEmpty(startDateStr)) {
      endDateStr = startDateStr;
      list.add(startDateStr);
      list.add(endDateStr);
      return list;
    } else if (YHUtility.isNullorEmpty(startDateStr) && !YHUtility.isNullorEmpty(endDateStr)) {
      startDateStr = endDateStr;
      list.add(startDateStr);
      list.add(endDateStr);
      return list;
    }
    try {
      if (!YHUtility.isNullorEmpty(startDateStr) && !YHUtility.isNullorEmpty(endDateStr)) {
        String startDateArry[] = startDateStr.split("-");
        String endDateArry[] = endDateStr.split("-");
        int startYear = Integer.parseInt(startDateArry[0]);
        int startMonth = Integer.parseInt(startDateArry[1]);
        int endMonth = Integer.parseInt(endDateArry[1]);
        String result = "";
        if (startMonth < endMonth) {
          list.add(startDateStr);
          int tmp = endMonth - startMonth;
          if (tmp <= 11) {
            for (int i = 1; i < tmp; i++) {
              int tmpMonth = startMonth + i;
              String str = "";
              if (tmpMonth < 10) {
                str = "0";
              }
              result = startYear + "-" + str + tmpMonth;
              list.add(result);
            }
          }
          list.add(endDateStr);
        } else if (startMonth == endMonth) {
          list.add(startDateStr);
          list.add(endDateStr);

        } else if (startMonth > endMonth) {
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return list;
  }
  
  /**
   * 获取开始日期和结束日期之间的所有日期串
   * @param dbConn
   * @param beginTime
   * @param endTime
   * @return
   * @throws Exception
   */
  public String getDayList(Connection dbConn, String beginTime,String endTime) throws Exception {
    //相隔多少天
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    long daySpace = YHUtility.getDaySpan(dateFormat1.parse(beginTime),dateFormat1.parse(endTime))+1;
    //得到到之间的天数数组
    List daysList = new ArrayList();
    String days = "";
    Calendar calendar = new GregorianCalendar();
    for(int i = 0;i<daySpace;i++){
      calendar.setTime(dateFormat1.parse(beginTime));
      calendar.add(Calendar.DATE,+i) ;
      Date dateTemp = calendar.getTime();
      String dateTempStr = dateFormat1.format(dateTemp);
      daysList.add(dateTempStr);
      days = days + dateTempStr + ",";
    }
    if(daySpace>0){
      days = days.substring(0,days.length()-1);
    }
    return days;
  }
}
