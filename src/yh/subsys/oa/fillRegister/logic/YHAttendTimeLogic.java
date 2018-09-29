package yh.subsys.oa.fillRegister.logic;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.fillRegister.data.YHAttendTime;

public class YHAttendTimeLogic {
	private static Logger log = Logger.getLogger(YHAttendTimeLogic.class);

	/**
	 * 获取迟到时间管理列表
	 * 
	 * @param dbConn
	 * @return
	 * @throws Exception
	 */
	public String getAttendTimeListLogic(Connection dbConn, int dutyId) throws Exception {
		YHORM orm = new YHORM();
		StringBuffer buffer = new StringBuffer();
		try {
		  String[] filters = {" DUTY_ID = " + dutyId +" order by MIN_LATE_TIME ASC"};
			List<YHAttendTime> attendTimes = (List<YHAttendTime>) orm.loadListSingle(dbConn, YHAttendTime.class, filters);
			if (attendTimes == null || attendTimes.size() == 0) {
				return buffer.append("[]").toString();
			}
			buffer.append("[");
			for (YHAttendTime attendTime : attendTimes) {
				int minLateTime = attendTime.getMinLateTime();
				int maxLateTime = attendTime.getMaxLateTime();
				double score = attendTime.getScore();
				buffer.append("{");
				buffer.append("seqId:\"" + attendTime.getSeqId() + "\"");
				buffer.append(",minLateTime:\"" + minLateTime + "\"");
				buffer.append(",maxLateTime:\"" + maxLateTime + "\"");
				buffer.append(",score:\"" + score + "\"");
				buffer.append("},");
			}
			if (buffer.length() > 0) {
				buffer.deleteCharAt(buffer.length() - 1);
			}
			buffer.append("]");
		} catch (Exception e) {
			throw e;
		}
		return buffer.toString();
	}

	/**
	 * 增加迟到时间管理项
	 * 
	 * 
	 * 
	 * @param dbConn
	 * @param attendTime
	 * @throws Exception
	 */
	public void addAttendTimeItemLogic(Connection dbConn, YHAttendTime attendTime) throws Exception {
		try {
			YHORM orm = new YHORM();
			orm.saveSingle(dbConn, attendTime);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 更新迟到时间管理项
	 * 
	 * 
	 * @param dbConn
	 * @param attendTime
	 * @throws Exception
	 */
	public void updateAttendTimeItemLogic(Connection dbConn, YHAttendTime attendTime) throws Exception {
		try {
			YHORM orm = new YHORM();
			orm.updateSingle(dbConn, attendTime);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 删除迟到时间管理项
	 * 
	 * 
	 * @param dbConn
	 * @param attendTime
	 * @throws Exception
	 */
	public void delAttendTimeItemLogic(Connection dbConn, YHAttendTime attendTime) throws Exception {
		try {
			YHORM orm = new YHORM();
			orm.deleteSingle(dbConn, attendTime);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 *过滤周六日
	 * 
	 * @param dbConn
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static void getDate(String beginTime, String endTime) throws Exception {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		long daySpace = YHUtility.getDaySpan(dateFormat1.parse(beginTime), dateFormat1.parse(endTime)) + 1;
		// 得到到之间的天数数组
		Calendar calendar = new GregorianCalendar();
		for (int i = 0; i < daySpace; i++) {
			calendar.setTime(dateFormat1.parse(beginTime));
			calendar.add(Calendar.DATE, +i);
			Date dateTemp = calendar.getTime();
			if (getDateWeek(dateTemp) == 6 || getDateWeek(dateTemp) == 7) {

			} else {
				System.out.println(dateFormat1.format(dateTemp));
			}
		}
	}
	
	public static int getDateWeek(Date date) throws ParseException {
    GregorianCalendar d = new GregorianCalendar();
    d.setTime(date);
    int today = d.get(Calendar.DAY_OF_WEEK);
    if (today == 1) {
      today = 7;
    } else {
      today = today - 1;
    }
    return today;
  }

	/**
   * 获取迟到时间管理列表
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public String getAttendTimeListByIdLogic(Connection dbConn,String dutyIdStr,String dutyType, String registerType) throws Exception {
    YHORM orm = new YHORM();
    StringBuffer buffer = new StringBuffer();
    int dutyId = 0;
    if(!YHUtility.isNullorEmpty(dutyIdStr)){
      dutyId = Integer.parseInt(dutyIdStr);
    }
    if(YHUtility.isNullorEmpty(dutyType)){
      dutyType = "";
    }
    if(YHUtility.isNullorEmpty(registerType)){
      registerType = "";
    }
    try {
      String[] filters = {" DUTY_ID=" + dutyId + " and DUTY_TYPE='" + dutyType + "' and REGISTER_TYPE='" + registerType + "'" };
      orm.loadListSingle(dbConn, YHAttendTime.class, filters);
      List<YHAttendTime> attendTimes = (List<YHAttendTime>) orm.loadListSingle(dbConn, YHAttendTime.class, filters);
      if (attendTimes == null || attendTimes.size() == 0) {
        return buffer.append("[]").toString();
      }
      buffer.append("[");
      for (YHAttendTime attendTime : attendTimes) {
        int minLateTime = attendTime.getMinLateTime();
        int maxLateTime = attendTime.getMaxLateTime();
        double score = attendTime.getScore();
        buffer.append("{");
        buffer.append("seqId:\"" + attendTime.getSeqId() + "\"");
        buffer.append(",minLateTime:\"" + minLateTime + "\"");
        buffer.append(",maxLateTime:\"" + maxLateTime + "\"");
        buffer.append(",score:\"" + score + "\"");
        buffer.append("},");
      }
      if (buffer.length() > 0) {
        buffer.deleteCharAt(buffer.length() - 1);
      }
      buffer.append("]");
    } catch (Exception e) {
      throw e;
    }
    return buffer.toString();
  }
}
