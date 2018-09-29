package yh.subsys.oa.addworkfee.logic;

import java.sql.Connection;
import java.util.List;

import yh.core.util.YHUtility;
import yh.subsys.oa.addworkfee.data.YHCalendary;
import yh.subsys.oa.addworkfee.data.YHChangeRest;
import yh.subsys.oa.addworkfee.data.YHDateJuge;
import yh.subsys.oa.addworkfee.data.YHFestival;

/**
 * 加班费计算
 * @author Administrator
 *
 */
public class YHAddWorkFeeLogic{
  
  private YHChangeRestLogic restLogic = new YHChangeRestLogic();
  private YHFestivalLogic festLogic = new YHFestivalLogic();
  private YHRoleBaseFeeLogic feeLogic = new YHRoleBaseFeeLogic();
  private YHOndutyLogic dutyLogic = new YHOndutyLogic();
  
  /**
   * 计算加班费 1.平时， 2.周末  3.节假日
   * @param conn
   * @param date
   * @return
   * @throws Exception 
   */
  public String accountAddWorkFee(Connection conn, String date, String begin, String end, int roleId) throws Exception{
    String year = "";
    if(YHUtility.isNullorEmpty(date)){
      year = YHUtility.getCurDateTimeStr("yyyy");
    }else{
      year = date.substring(0,date.indexOf("-"));
    }
    double cha = YHCalendary.getHourDiff(begin, end, YHCalendary.PATTEN_SECOND);
    List<YHFestival> festList = festLogic.findFestival(conn, year); //某一年所有的节假日    boolean isFest = YHCalendary.isFestival(festList, date, YHCalendary.PATTERN); //是节假日
    boolean  isWeek = YHCalendary.isWeekend(date, "yyyy-MM-dd");        //是周末    List<YHChangeRest> restList = restLogic.findChangeRestList(conn, year); //某一年的所有的调休
    boolean isRest = YHCalendary.isChangeRest(restList, date, "yyyy-MM-dd");//是调休    YHDateJuge ju = new YHDateJuge();
    ju.setTimeDiff(cha);
    double fee = 0;
    if(isFest){
      ju.setDateType(3);
      fee = feeLogic.getMoney(conn, 3, roleId) * cha;
    }else if(isWeek){
      if(isRest){
        ju.setDateType(1);
        fee = feeLogic.getMoney(conn, 1, roleId) * cha;
      }else{
        ju.setDateType(2);
        fee = feeLogic.getMoney(conn, 2, roleId) * cha;
      }
    }else{
      if(isRest){
        ju.setDateType(2);
        fee = feeLogic.getMoney(conn, 2, roleId) * cha;
      }
      ju.setDateType(1);
      fee = feeLogic.getMoney(conn, 1, roleId) * cha;
    }
    ju.setTotalFee(YHCalendary.round(fee, null));
    return ju.toJson();
  }
  
  /**
   * 计算值班费 1.平时， 2.周末  3.节假日

   * @param conn
   * @param date
   * @return
   * @throws Exception 
   */
  public String accountAddDutyFee(Connection conn, String date, String begin, String end, int roleId) throws Exception{
    String year = "";
    if(YHUtility.isNullorEmpty(date)){
      year = YHUtility.getCurDateTimeStr("yyyy");
    }else{
      year = date.substring(0,date.indexOf("-"));
    }
    double cha = YHCalendary.getHourDiff(begin, end, YHCalendary.PATTEN_SECOND);
    List<YHFestival> festList = festLogic.findFestival(conn, year); //某一年所有的节假日    boolean isFest = YHCalendary.isFestival(festList, date, YHCalendary.PATTERN); //是节假日
    boolean  isWeek = YHCalendary.isWeekend(date, "yyyy-MM-dd");        //是周末    List<YHChangeRest> restList = restLogic.findChangeRestList(conn, year); //某一年的所有的调休
    boolean isRest = YHCalendary.isChangeRest(restList, date, "yyyy-MM-dd");//是调休    YHDateJuge ju = new YHDateJuge();
    ju.setTimeDiff(cha);
    double fee = 0;
    if(isFest){
      ju.setDateType(3);
      fee = dutyLogic.getMoney(conn, 3, roleId) * cha;
    }else if(isWeek){
      if(isRest){
        ju.setDateType(1);
        fee = dutyLogic.getMoney(conn, 1, roleId) * cha;
      }else{
        ju.setDateType(2);
        fee = dutyLogic.getMoney(conn, 2, roleId) * cha;
      }
    }else{
      if(isRest){
        ju.setDateType(2);
        fee = dutyLogic.getMoney(conn, 2, roleId) * cha;
      }
      ju.setDateType(1);
      fee = dutyLogic.getMoney(conn, 1, roleId) * cha;
    }
    ju.setTotalFee(YHCalendary.round(fee, null));
    return ju.toJson();
  }
}
