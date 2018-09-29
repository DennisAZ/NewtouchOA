package yh.core.funcs.attendance.manage.act;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.attendance.manage.logic.YHExpAttendLogic;
import yh.core.funcs.attendance.manage.logic.YHManageAttendLogic;
import yh.core.funcs.attendance.manage.logic.YHManageEvectionLogic;
import yh.core.funcs.attendance.manage.logic.YHManageLeaveLogic;
import yh.core.funcs.attendance.manage.logic.YHManageOutLogic;
import yh.core.funcs.attendance.personal.act.YHAttendLeaveAct;
import yh.core.funcs.attendance.personal.data.YHAttendDuty;
import yh.core.funcs.attendance.personal.data.YHAttendEvection;
import yh.core.funcs.attendance.personal.data.YHAttendLeave;
import yh.core.funcs.attendance.personal.data.YHAttendOut;
import yh.core.funcs.attendance.personal.logic.YHAttendDutyLogic;
import yh.core.funcs.attendance.personal.logic.YHAttendLeaveLogic;
import yh.core.funcs.calendar.info.logic.YHInfoLogic;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.attendance.data.YHAttendConfig;
import yh.core.funcs.system.attendance.data.YHAttendHoliday;
import yh.core.funcs.system.attendance.logic.YHAttendConfigLogic;
import yh.core.funcs.system.attendance.logic.YHSysParaLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.fillRegister.data.YHAttendFill;

public class YHManageAttendAct {
  /*
   * 根据userId 得到排班类型
   * 
   */
  public String selectConfigByUserId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      String userId = request.getParameter("userId");//得到指定用户的ID
      //根据userId得到DUTY_TYPE对应的是attend_config表中的SEQ_ID;
      YHPersonLogic personLogic  = new YHPersonLogic();
      YHPerson person = personLogic.getPerson(dbConn, userId);
      int configSeqId = person.getDutyType();//5
      YHAttendDuty duty = new YHAttendDuty();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      YHAttendConfig config = yhadl.selectConfigById(dbConn, String.valueOf(configSeqId));
      String data  = YHFOM.toJson(config).toString();
      //System.out.println(data);
      //得到用户的姓名和部门
      YHPersonLogic tpl = new YHPersonLogic();
      YHManageOutLogic yhaol = new YHManageOutLogic();
      String userName = tpl.getNameBySeqIdStr(userId, dbConn);
      if(userName!=null&&!userName.equals("")){
        userName = YHUtility.encodeSpecial(userName);
      }
      String deptName = yhaol.selectByUserIdDept(dbConn, userId);
      if(data.equals("")){
        data = "{}"; 
      }
      data = "[{deptName:\""+deptName+"\",userName:\""+userName+"\"}," + data+"]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, deptName+","+userName);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /*
   * 根据排班类型得到所有用户
   * 
   */
  public String selectUserBydutyType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      String dutyType = request.getParameter("dutyType");
      YHManageAttendLogic logic = new YHManageAttendLogic();
      List<YHPerson> personList = new ArrayList<YHPerson>();
     
      if(!userId.equals("")){
        String newUserId = "";
        String[] userIdArray = userId.split(",");
        for (int i = 0; i < userIdArray.length; i++) {
          newUserId = newUserId + "'" + userIdArray[i] + "',"; 
        }
        newUserId = newUserId.substring(0, newUserId.length()-1);
        String[] str = {"SEQ_ID in("+newUserId+")","DUTY_TYPE ="+dutyType};
        personList = logic.selectPerson(dbConn, str);
      }
      String userSeqId = "";
      String data = "[";
      YHManageOutLogic yhaol = new YHManageOutLogic();
      for (int i = 0; i < personList.size(); i++) {
        userSeqId = userSeqId  + personList.get(i).getSeqId()+",";
        YHPerson person = personList.get(i);
        String deptName = yhaol.selectByUserIdDept(dbConn, String.valueOf(person.getSeqId()));
        data = data + YHFOM.toJson(person).toString().substring(0, YHFOM.toJson(person).toString().length()-1)+",deptName:\""+deptName+"\"},";
      }
      if(personList.size()>0){
        userSeqId = userSeqId.substring(0, userSeqId.length()-1);
        data = data.substring(0, data.length()-1);
      }
      data = data+"]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, userSeqId);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询排班类型 根据用户的id
   * 为刚加载的时候得到排班的Name
   * 并得到当天的考勤登记
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String selectDutyByUserIdName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      String userId = request.getParameter("userId");//得到指定用户的ID
      //根据userId得到DUTY_TYPE对应的是attend_config表中的SEQ_ID;
      YHPersonLogic personLogic  = new YHPersonLogic();
      YHPerson person = personLogic.getPerson(dbConn, userId);
      int configSeqId = person.getDutyType();//5
      YHAttendDuty duty = new YHAttendDuty();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      YHAttendConfig config = yhadl.selectConfigById(dbConn, String.valueOf(configSeqId));
      String configJson  = YHFOM.toJson(config).toString();
      //得到指定当天登记的记录
      YHDBUtility yhdbu = new YHDBUtility();
      String DBStr =  yhdbu.curDayFilter("REGISTER_TIME");
      Date date = new Date();
      String dateStr = dateFormat.format(date);
      List<YHAttendDuty> dutyList = yhadl.selectDuty(dbConn, String.valueOf(userId), DBStr,"","");
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      String data = "";
      if(config!=null){
        String dutyName = config.getDutyName();
        String dutyTime1 = config.getDutyTime1();
        String dutyTime2 = config.getDutyTime2();
        String dutyTime3 = config.getDutyTime3();
        String dutyTime4 = config.getDutyTime4();
        String dutyTime5 = config.getDutyTime5();
        String dutyTime6 = config.getDutyTime6();
        String dutyType1 = config.getDutyType1();
        String dutyType2 = config.getDutyType2();
        String dutyType3 = config.getDutyType3();
        String dutyType4 = config.getDutyType4();
        String dutyType5 = config.getDutyType5();
        String dutyType6 = config.getDutyType6();
        int c_zStatus =0;//判断迟到早退0为正常1为迟到,2为早退

        for (int i = 0; i < dutyList.size(); i++) {
          duty = dutyList.get(i);
          data = data + YHFOM.toJson(duty).toString()+",";
        }
        if(dutyList.size()>0){
          data = data.substring(0, data.length()-1);
        }
        if(dutyList.size()<=0){
          data ="[" + configJson +"]";
        }else{
          data = "[" + configJson + "," + data + "]";
        }
      }else{
        data = "[]";
      }
      
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String selectDutyByUserIdNameTemp(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
      String userId = request.getParameter("userId");//得到指定用户的ID
      String days = request.getParameter("days");
      //根据userId得到DUTY_TYPE对应的是attend_config表中的SEQ_ID;
      YHPersonLogic personLogic  = new YHPersonLogic();
      YHPerson person = personLogic.getPerson(dbConn, userId);
      int configSeqId = person.getDutyType();//5
      YHAttendDuty duty = new YHAttendDuty();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      YHAttendConfig config = yhadl.selectConfigById(dbConn, String.valueOf(configSeqId));
      String configJson  = YHFOM.toJson(config).toString();
      //得到指定当天登记的记录
      YHDBUtility yhdbu = new YHDBUtility();
      String DBStr =  yhdbu.curDayFilter("REGISTER_TIME");
      Date date = new Date();
      String dateStr = dateFormat.format(date);
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      String data = "";
      if(config!=null){
        String dutyName = config.getDutyName();
        String dutyTime1 = config.getDutyTime1();
        String dutyTime2 = config.getDutyTime2();
        String dutyTime3 = config.getDutyTime3();
        String dutyTime4 = config.getDutyTime4();
        String dutyTime5 = config.getDutyTime5();
        String dutyTime6 = config.getDutyTime6();
        String dutyType1 = config.getDutyType1();
        String dutyType2 = config.getDutyType2();
        String dutyType3 = config.getDutyType3();
        String dutyType4 = config.getDutyType4();
        String dutyType5 = config.getDutyType5();
        String dutyType6 = config.getDutyType6();
        String general = "";
        if(config.getGeneral()!=null&& !config.getGeneral().trim().equals("")){
          general = config.getGeneral();
        }
        //得到是排版类型的总登记数
        int configCount =0;
        if(dutyTime1!=null&&!dutyTime1.trim().equals("")){configCount++;}
        if(dutyTime2!=null&&!dutyTime2.trim().equals("")){configCount++;}
        if(dutyTime3!=null&&!dutyTime3.trim().equals("")){configCount++;}
        if(dutyTime4!=null&&!dutyTime4.trim().equals("")){configCount++;}
        if(dutyTime5!=null&&!dutyTime5.trim().equals("")){configCount++;}
        if(dutyTime6!=null&&!dutyTime6.trim().equals("")){configCount++;}
        //全部参数
        long perfectCount = 0;//全勤
        long lateCount = 0; //迟到
        long dutyOnTotal = 0;//上班登记总数
        long dutyOffTotal = 0;//下班登记总数
        long earlyCount = 0;//早退
        long addDutyOnCount = 0;//加班上班登记
        long addDutyOffCount = 0;//加班下班登记
        int c_zStatus =0;//判断迟到早退0为正常1为迟到,2为早退
        //在指定时间段内的日期数组
        String[]  dayArray =  days.split(",");
        String dutyTime = "";
        String dutyType = "";
        for (int i = 0; i < dayArray.length; i++) {
          String holidayStr = "";
          Map map = new HashMap();
          //判断是否为节假日
          String holidayStatus = checkHolidayTemp(request,response,dayArray[i]);
          if(holidayStatus.equals("1")){
            holidayStr = "1";
          }
          //判断是否为公休日
          String generalStatus = IsGeneral(general, dayArray[i]);
          if(generalStatus.equals("1")){
            holidayStr = "1";
          }
          //判断是否出差
          String IsEvection = isEvectionTemp(request, response, dayArray[i], userId);
          if(IsEvection.equals("1")){
            holidayStr = "1";
          }

          //得到一天的上下班登记  
          //6次循环

          for(int j = 1;j<=6;j++){
            if(j==1){
              dutyTime = dutyTime1;
              dutyType = dutyType1;
            }
            if(j==2){
              dutyTime = dutyTime2;
              dutyType = dutyType2;
            }
            if(j==3){
              dutyTime = dutyTime3;
              dutyType = dutyType3;
            }
            if(j==4){
              dutyTime = dutyTime4;
              dutyType = dutyType4;
            }
            if(j==5){
              dutyTime = dutyTime5;
              dutyType = dutyType5;
            }
            if(j==6){
              dutyTime = dutyTime6;
              dutyType = dutyType6;
            }
            if(dutyTime==null|| dutyTime.trim().equals("")){continue;}
            if(dutyTime!=null&&!dutyTime.trim().equals("")){
              if(dutyType.equals("1")){
                dutyOnTotal = dutyOnTotal + 1;
              }else{
                dutyOffTotal = dutyOffTotal + 1;
              }
              //判断是否请假
              String isLeave = isLeaveTemp(request, response, dayArray[i], userId);
              List<YHAttendDuty> dutyList = getAttendDuty(request,response,dayArray[i],String.valueOf(j),userId,config);
              if(dutyList.size()>0){
                for (int k = 0; k < dutyList.size(); k++)  {
                  duty = dutyList.get(k);
                  Date registerTime = duty.getRegisterTime();
                  String registerType = duty.getRegisterType();
                  if(holidayStr.equals("1")||isLeave.equals("1")){
                    if(dutyType.equals("1")){
                    //上班登记总数-1
                      dutyOnTotal = dutyOnTotal -1;
                      //加班上班+1
                      addDutyOnCount = addDutyOnCount+1;  
                    }else{
                      //下班登记总数-1
                      dutyOffTotal = dutyOffTotal -1;
                      //加班下班+1
                      addDutyOffCount = addDutyOffCount + 1;
                  }
                }else{
                        //不是加班的

                  long dutyTimeInt =getLongByDutyTime(dutyTime);
                  long registerTimeInt = getLongByDutyTime(dateFormat2.format(registerTime));
                  if(dutyType.equals("1")){
                    //上班登记总数-1
                    dutyOnTotal = dutyOnTotal -1;
                    if(dutyTimeInt<registerTimeInt){
                      //迟到+1
                      lateCount = lateCount + 1;
                    }
                  }else{
                    //下班登记总数-1
                    dutyOffTotal = dutyOffTotal -1;
                    if(dutyTimeInt>registerTimeInt){
                      //早退+1
                      earlyCount = earlyCount + 1;
                    }
                  }
                }
               }
             }else{
               if(holidayStr.equals("1")|| isLeave.equals("1")){
                 if(dutyType.equals("1")){
                   dutyOnTotal = dutyOnTotal -1;
                 }else{
                   dutyOffTotal = dutyOffTotal -1;
                 }
               }
            }
          }
       }
          //判断全勤天

            List<YHAttendDuty> dutyList1 =getAttendDuty(request,response,dayArray[i],"",userId,config);
            int dutyCount = 0;
            for(int j = 1; j< dutyList1.size(); j++){
              duty = dutyList1.get(j);
              Date registerTime = duty.getRegisterTime();
              String registerType = duty.getRegisterType();
              String dutyTimeTemp ="";
              String dutyTypeTemp = "";
              if(registerType.equals("1")){
                dutyTimeTemp = dutyTime1;
                dutyTypeTemp = dutyType1;
              }
              if(registerType.equals("2")){
                dutyTimeTemp = dutyTime2;
                dutyTypeTemp = dutyType2;
              }
              if(registerType.equals("3")){
                dutyTimeTemp = dutyTime3;
                dutyTypeTemp = dutyType3;
              }
              if(registerType.equals("4")){
                dutyTimeTemp = dutyTime4;
                dutyTypeTemp = dutyType4;
              }
              if(registerType.equals("5")){
                dutyTimeTemp = dutyTime5;
                dutyTypeTemp = dutyType5;
              }
              if(registerType.equals("6")){
                dutyTimeTemp = dutyTime6;
                dutyTypeTemp = dutyType6;
              }
             
              //判断是否请假
              String IsLeave = "0";
              String strLeave[] = {"USER_ID in(" + userId + ")","ALLOW=1",YHDBUtility.getDateFilter("LEAVE_DATE1", dayArray[i]+ " 23:59:59", "<="),YHDBUtility.getDateFilter("LEAVE_DATE2", dayArray[i], ">=")};
              List<YHAttendLeave> leaveList = attendLogic.selectLeave(dbConn, strLeave);
              if(leaveList.size()>0){
                IsLeave = "1";
              }
              //判断是否节假日.公休日,出差,请假;
              if(!(holidayStr.equals("1")||IsLeave.equals("1"))){
                //System.out.println(dutyTimeTemp!=null+":"+dutyTimeTemp);
                if(dutyTimeTemp!=null&&!dutyTimeTemp.trim().equals("")){
                  long dutyTimeInt =getLongByDutyTime(dutyTimeTemp);
                  long registerTimeInt = getLongByDutyTime(dateFormat2.format(registerTime));
                  if(dutyTypeTemp.equals("1")){
                    if(dutyTimeInt<registerTimeInt){
                      dutyCount = dutyCount +1;
                    }
                  }else{
                    if(dutyTimeInt>registerTimeInt){
                      dutyCount = dutyCount +1;
                    }
                  }
                }
              }
            }
          if(dutyCount==configCount){
            perfectCount = perfectCount + 1;
          }

     }
      data = "{perfectCount:"+perfectCount+",dutyOnTotal:"+dutyOnTotal+",dutyOffTotal:"+dutyOffTotal+",lateCount:"+lateCount+",earlyCount:"+earlyCount+",addDutyOnCount:"+addDutyOnCount+",addDutyOffCount:"+addDutyOffCount+"}";
      
      }else{
        data = "{}";
      }
  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到一天的登记情况
   * @param request
   * @param response
   * @param date 时间字符串
   * @param registerType 登记类型(1-6)
   * @param userId 登记人SeqId
   * @param config 排版类型 
   * @return
   * @throws Exception
   */
  public List<YHAttendDuty> getAttendDuty(HttpServletRequest request,
      HttpServletResponse response,String date,String registerType,String userId,YHAttendConfig config) throws Exception {
    Connection dbConn = null;
    List<YHAttendDuty> dutyList = new ArrayList<YHAttendDuty>();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //得到指定当天登记的记录
      YHDBUtility yhdbu = new YHDBUtility();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      String date1 = date + " 00:00:00";
      String date2 = date + " 23:59:59";
      String DBStr =  yhdbu.curDayFilter("REGISTER_TIME");
      date1 = yhdbu.getDateFilter("REGISTER_TIME", date1, ">=");
      date2 = yhdbu.getDateFilter("REGISTER_TIME", date2, "<=");
      //System.out.println(userId);
      dutyList = yhadl.selectDuty(dbConn, String.valueOf(userId), date1,date2,registerType);
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      String dutyName = config.getDutyName();
      String dutyTime1 = config.getDutyTime1();
      String dutyTime2 = config.getDutyTime2();
      String dutyTime3 = config.getDutyTime3();
      String dutyTime4 = config.getDutyTime4();
      String dutyTime5 = config.getDutyTime5();
      String dutyTime6 = config.getDutyTime6();
      String dutyType1 = config.getDutyType1();
      String dutyType2 = config.getDutyType2();
      String dutyType3 = config.getDutyType3();
      String dutyType4 = config.getDutyType4();
      String dutyType5 = config.getDutyType5();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return dutyList;
  }
  public long getLongByDutyTime(String dutyTime){
    long time = 0;
    String times[] = dutyTime.split(":");
    int length = times.length;
    for (int i = 0; i < times.length; i++) {
      time = time + Long.parseLong(times[i])* (long)(Math.pow(60, length-1-i)) ;
    }
    return time;
  }
  /**
   * 判断是否是公休日
   * @param general
   * @param date
   * @return
   * @throws ParseException
   */
  public String IsGeneral(String general,String date) throws ParseException{
    String isGeneral = "0";
    Date dateTemp = YHUtility.parseDate(date); 
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dateTemp);
    int week = calendar.get(Calendar.DAY_OF_WEEK);
    if(week == 1){
      week = 7;
    }else{
      week = week-1;
    }
    for (int k = 0; k < general.length(); k++) {
      if(String.valueOf(general.charAt(k)).equals(String.valueOf(week))){
        isGeneral = "1";
        break;
      }
    }
    return isGeneral;
  }
  /**
   * 考勤统计查出所有人的考勤情况
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String allUserDutyInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
      String userId = request.getParameter("userId");//得到指定部门的所有的ID
      String dutyType = request.getParameter("dutyType");
      String days = request.getParameter("days");//得到指定的所有日期
      String[] userIdArray = {};
      if(!userId.equals("")){
        userIdArray = userId.split(",");
      }
      String[] dayArray = days.split(",");
      String data = "[";
      //System.out.println(userIdArray.length+userId);
      YHManageOutLogic yhaol = new YHManageOutLogic();
      //对所有用户循环
      for(int i = 0;i<userIdArray.length;i++){
        String deptName = yhaol.selectByUserIdDept(dbConn, userIdArray[i]);
        long perfectCount = 0;//全勤
        long hourTotal = 0;//总时长
        long lateCount = 0; //迟到
        long dutyOnTotal = 0;//上班登记总数
        long dutyOffTotal = 0;//下班登记总数
        long earlyCount = 0;//早退
        long addDutyOnCount = 0;//加班上班登记
        long addDutyOffCount = 0;//加班下班登记
        long dutyOn = 0;//排班中有多少上班的
        long dutyOff = 0;//排班中有多少下班的
        String userSeqId = userIdArray[i];
        //根据用户得到相应的排班类型
        //根据userId得到DUTY_TYPE对应的是attend_config表中的SEQ_ID;
        YHPersonLogic personLogic  = new YHPersonLogic();
        YHPerson person = personLogic.getPerson(dbConn, userIdArray[i]);
        int configSeqId = person.getDutyType();//5
        YHAttendDuty duty = new YHAttendDuty();
        YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
        YHAttendConfig config = yhadl.selectConfigById(dbConn, String.valueOf(configSeqId));
        String dutyName = config.getDutyName();
        String dutyTime1 = config.getDutyTime1();
        String dutyTime2 = config.getDutyTime2();
        String dutyTime3 = config.getDutyTime3();
        String dutyTime4 = config.getDutyTime4();
        String dutyTime5 = config.getDutyTime5();
        String dutyTime6 = config.getDutyTime6();
        String dutyType1 = config.getDutyType1();
        String dutyType2 = config.getDutyType2();
        String dutyType3 = config.getDutyType3();
        String dutyType4 = config.getDutyType4();
        String dutyType5 = config.getDutyType5();
        String dutyType6 = config.getDutyType6();
        String general = "";
        if(config.getGeneral()!=null&& !config.getGeneral().trim().equals("")){
          general = config.getGeneral();
        }
        //得到是排版类型的总登记数
        int configCount =0;
        if(dutyTime1!=null&&!dutyTime1.trim().equals("")){
          if(dutyType1.equals("1")){
            dutyOn++;
          }else{
            dutyOff++;
          }
          configCount++;}
        if(dutyTime2!=null&&!dutyTime2.trim().equals("")){
          if(dutyType2.equals("1")){
            dutyOn++;
          }else{
            dutyOff++;
          }
          configCount++;}
        if(dutyTime3!=null&&!dutyTime3.trim().equals("")){
          if(dutyType3.equals("1")){
            dutyOn++;
          }else{
            dutyOff++;
          }
          configCount++;}
        if(dutyTime4!=null&&!dutyTime4.trim().equals("")){
          if(dutyType4.equals("1")){
            dutyOn++;
          }else{
            dutyOff++;
          }
          configCount++;}
        if(dutyTime5!=null&&!dutyTime5.trim().equals("")){
          if(dutyType5.equals("1")){
            dutyOn++;
          }else{
            dutyOff++;
          }
          configCount++;}
        if(dutyTime6!=null&&!dutyTime6.trim().equals("")){
          if(dutyType6.equals("1")){
            dutyOn++;
          }else{
            dutyOff++;
          }
          configCount++;}
        dutyOnTotal = dutyOn*dayArray.length;
        dutyOffTotal = dutyOff*dayArray.length;
      //对指定天数循环
        for(int j = 0;j<dayArray.length;j++){
          int configCountTmp = 0;
          String holidayStr = "";
          //判断是否公休日
          if(IsGeneral(general, dayArray[j]).equals("1")){
            holidayStr = "1";
          }else{
            //判断是否节假日;
            if(checkHolidayTemp(request, response, dayArray[j]).equals("1")){
              holidayStr = "2";
            }else{
              //判断是否出差
              if( isEvectionTemp(request, response,dayArray[j],userIdArray[i]).equals("1")){
                holidayStr = "2";
              }else{
                //判断时候请假
                if(isLeaveTemp(request, response,dayArray[j],userIdArray[i]).equals("1")){
                  holidayStr = "2";
                }
              }
            }
          }  

          //得到当天的上下班登记情况
          List<YHAttendDuty> dutyList =  getAttendDuty(request,response,dayArray[j],"",userIdArray[i],config);
          long charTemp = 0;
          if(dutyList.size()>0){
            String registerTime1 = "";
            String registerTime2 = "";
            String registerTime3 = "";
            String registerTime4 = "";
            String registerTime5 = "";
            String registerTime6 = "";
            String dutyTypeTemp1 = "";
            String dutyTypeTemp2 = "";
            String dutyTypeTemp3 = "";
            String dutyTypeTemp4 = "";
            String dutyTypeTemp5 = "";
            String dutyTypeTemp6 = "";
            long char1 = 0;
            long char2 = 0;
            long char3= 0;
            long char4 = 0;
            long char5 = 0;
            long char6 = 0;
            for(int k = 0; k< dutyList.size(); k++){
              duty = dutyList.get(k);
              Date registerTime = duty.getRegisterTime();
              String registerType = duty.getRegisterType();
              String dutyTypeTemp = "";
              long dutyTimeInt = 0;
              long registerTimeInt = getLongByDutyTime(dateFormat2.format(registerTime));
              //判断时长
              if(dutyTime1!=null&&!dutyTime1.trim().equals("") &&registerType.equals("1")){
                registerTime1 = dateFormat2.format(registerTime);
                dutyTypeTemp1 = dutyType1;
                dutyTypeTemp = dutyType1;
                dutyTimeInt =getLongByDutyTime(dutyTime1);
              }
              if(dutyTime2!=null&&!dutyTime2.trim().equals("") &&registerType.equals("2")){
                registerTime2 = dateFormat2.format(registerTime);
                dutyTypeTemp2 = dutyType2;
                dutyTypeTemp = dutyType2;
                dutyTimeInt =getLongByDutyTime(dutyTime2);
              }
              if(dutyTime3!=null&&!dutyTime3.trim().equals("") &&registerType.equals("3")){
                registerTime3 = dateFormat2.format(registerTime);
                dutyTypeTemp3 = dutyType3;
                dutyTypeTemp = dutyType3;
                dutyTimeInt =getLongByDutyTime(dutyTime3);
              }
              if(dutyTime4!=null&&!dutyTime4.trim().equals("") &&registerType.equals("4")){
                registerTime4 = dateFormat2.format(registerTime);
                dutyTypeTemp4 = dutyType4;
                dutyTypeTemp = dutyType4;
                dutyTimeInt =getLongByDutyTime(dutyTime4);
              }
              if(dutyTime5!=null&&!dutyTime5.trim().equals("") &&registerType.equals("5")){
                registerTime5 = dateFormat2.format(registerTime);
                dutyTypeTemp5 = dutyType5;
                dutyTypeTemp = dutyType5;
                dutyTimeInt =getLongByDutyTime(dutyTime5);
              }
              if(dutyTime6!=null&&!dutyTime6.trim().equals("") &&registerType.equals("6")){
                registerTime6 = dateFormat2.format(registerTime);
                dutyTypeTemp6 = dutyType6;
                dutyTypeTemp = dutyType6;
                dutyTimeInt =getLongByDutyTime(dutyTime6);
              }  
              if(dutyTypeTemp.equals("1")){
                if(holidayStr.equals("1")){
                  //上班加班+1
                  addDutyOnCount = addDutyOnCount+1;
                  dutyOnTotal = dutyOnTotal -1;
                }else if(holidayStr.equals("2")){
                  dutyOnTotal = dutyOnTotal -1;
                }else{
                  dutyOnTotal = dutyOnTotal -1;
                  if(dutyTimeInt<registerTimeInt){
                    //迟到+1
                    lateCount = lateCount + 1;
                    //判断迟到是否有过迟到
                  }else{
                    configCountTmp = configCountTmp+1;
                  }
                }
              }else{
                if(holidayStr.equals("1")){
                  //下班加班+1
                  addDutyOffCount = addDutyOffCount+1;
                  dutyOffTotal = dutyOffTotal -1;
                }else if(holidayStr.equals("2")){
                  dutyOffTotal = dutyOffTotal -1;
                }else{
                  //下班登记总数-1
                  dutyOffTotal = dutyOffTotal -1;
                  //alert(dutyTimeInt+":"+registerTimeInt+":"+seqId);
                  if(dutyTimeInt>registerTimeInt){
                    //早退+1
                    earlyCount = earlyCount + 1;
                    //是否有早退的
                  }else{
                    configCountTmp = configCountTmp+1;
                  }
                }
              }
            }
            //得到时长
            if(dutyTime1!=null&&!dutyTime1.trim().equals("")&&!registerTime1.trim().equals("")&&dutyType1.equals("1")){
              for(int m = 2;m<=6;m++){
                String dutyTimeTemp = "" ;
                String registerTimeTemp = "";
                String dutyTypeTemp = "";
                if(m==2){dutyTimeTemp = dutyTime2;registerTimeTemp = registerTime2;dutyTypeTemp=dutyTypeTemp1; }
                if(m==3){dutyTimeTemp = dutyTime3;registerTimeTemp = registerTime3;dutyTypeTemp=dutyTypeTemp2; }
                if(m==4){dutyTimeTemp = dutyTime4;registerTimeTemp = registerTime4;dutyTypeTemp=dutyTypeTemp3; }
                if(m==5){dutyTimeTemp = dutyTime5;registerTimeTemp = registerTime5;dutyTypeTemp=dutyTypeTemp4; }
                if(m==6){dutyTimeTemp = dutyTime6;registerTimeTemp = registerTime6;dutyTypeTemp=dutyTypeTemp5; }
                if(dutyTimeTemp!=null&&!dutyTimeTemp.trim().equals("")){
                   if(!registerTimeTemp.equals("")&&dutyTypeTemp.equals("2")){
                     long registerTime1Int = getLongByDutyTime(registerTime1);
                     long registerTimeTempInt = getLongByDutyTime(registerTimeTemp);
                     char1 = registerTimeTempInt - registerTime1Int;
                   }
                   break;
                }
              }
            }  
            //第2次做比较
            if(dutyTime2!=null&&!dutyTime2.trim().equals("")&&!registerTime2.equals("")&&dutyType2.equals("1")){
              for(int m = 3;m<=6;m++){
                String dutyTimeTemp = "";
                String registerTimeTemp = "";
                String dutyTypeTemp = "";
                if(m==3){dutyTimeTemp = dutyTime3;registerTimeTemp = registerTime3;dutyTypeTemp=dutyTypeTemp3;}
                if(m==4){dutyTimeTemp = dutyTime4;registerTimeTemp = registerTime4;dutyTypeTemp=dutyTypeTemp4;}
                if(m==5){dutyTimeTemp = dutyTime5;registerTimeTemp = registerTime5;dutyTypeTemp=dutyTypeTemp5;}
                if(m==6){dutyTimeTemp = dutyTime6;registerTimeTemp = registerTime6;dutyTypeTemp=dutyTypeTemp6;}
                if(dutyTimeTemp!=null&&!dutyTimeTemp.trim().equals("")){
                   if(!registerTimeTemp.equals("")&&dutyTypeTemp.equals("2")){
                     long registerTime2Int = getLongByDutyTime(registerTime2);
                     long registerTimeTempInt = getLongByDutyTime(registerTimeTemp);
                     char2 = registerTimeTempInt - registerTime2Int;
                   }
                   break;
                }
              }
            }  
            //第3次做比较
            if(dutyTime3!=null&&!dutyTime3.trim().equals("")&&!registerTime3.equals("")&&dutyType3.equals("1")){
              for(int m = 4;m<=6;m++){
                String dutyTimeTemp = "";
                String dutyTypeTemp = "";
                String registerTimeTemp = "";
                if(m==4){dutyTimeTemp = dutyTime4;registerTimeTemp = registerTime4;dutyTypeTemp=dutyTypeTemp4;}
                if(m==5){dutyTimeTemp = dutyTime5;registerTimeTemp = registerTime5;dutyTypeTemp=dutyTypeTemp5;}
                if(m==6){dutyTimeTemp = dutyTime6;registerTimeTemp = registerTime6;dutyTypeTemp=dutyTypeTemp6;}
                if(dutyTimeTemp!=null&&!dutyTimeTemp.trim().equals("")){
                   if(!registerTimeTemp.equals("")&&dutyTypeTemp.equals("2")){
                     long registerTime3Int = getLongByDutyTime(registerTime3);
                     long registerTimeTempInt = getLongByDutyTime(registerTimeTemp);
                     char3 = registerTimeTempInt - registerTime3Int;
                   }
                   break;
                }
              }
            }
            //第4次做比较
            if(dutyTime4!=null&&!dutyTime4.trim().equals("")&&!registerTime4.equals("")&&dutyType4.equals("1")){
              for(int m = 5;m<=6;m++){
                String dutyTimeTemp = "";
                String dutyTypeTemp = "";
                String registerTimeTemp = "";
                if(m==5){dutyTimeTemp = dutyTime5;registerTimeTemp = registerTime5;dutyTypeTemp=dutyTypeTemp5;}
                if(m==6){dutyTimeTemp = dutyTime6;registerTimeTemp = registerTime6;dutyTypeTemp=dutyTypeTemp6;}
                if(dutyTimeTemp!=null&&!dutyTimeTemp.trim().equals("")){
                   if(!registerTimeTemp.equals("")&&dutyTypeTemp.equals("2")){
                     long registerTime4Int = getLongByDutyTime(registerTime4);
                     long registerTimeTempInt = getLongByDutyTime(registerTimeTemp);
                     char4 = registerTimeTempInt - registerTime4Int;
                   }
                   break;
                }
              }
            }
            //第5次做比较
            if(dutyTime5!=null&&!dutyTime5.trim().equals("")&&!registerTime5.equals("")&&dutyType5.equals("1")){
              if(dutyTime6!=null&&!dutyTime6.trim().equals("")&&!registerTime6.equals("")&&dutyTypeTemp6.equals("2")){
                //System.out.println(registerTime5);
                long registerTime5Int = getLongByDutyTime(registerTime5);
                long registerTimeTempInt = getLongByDutyTime(registerTime6);
                char5 = registerTimeTempInt - registerTime5Int;
              }
            }
        
           charTemp = char1+char2+char3+char4+char5;
           //全勤天
           if(holidayStr.equals("")&&configCountTmp==configCount){
             perfectCount = perfectCount + 1;
           }
          }else{
            if(!holidayStr.equals("")){
              dutyOnTotal = dutyOnTotal-dutyOn;
              dutyOffTotal = dutyOffTotal-dutyOff;
            }
          }
          hourTotal = hourTotal + charTemp;
        }
        String userName = person.getUserName();
        if(userName!=null&&!userName.equals("")){
          userName = userName.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
        }
        data = data + "{deptName:\"" + deptName + "\",userName:\"" + userName+ "\",perfectCount:"+perfectCount+",hourTotal:"+hourTotal
                + ",lateCount:"+lateCount+",earlyCount:"+earlyCount+",dutyOnTotal:"+dutyOnTotal+",dutyOffTotal:"+dutyOffTotal+",addDutyOnCount:"+addDutyOnCount+",addDutyOffCount:"+addDutyOffCount+",userSeqId:"+userIdArray[i]+"},";
      }
      if(userIdArray.length>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询在一段时间内得到考勤登记信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserDutyInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      String userId = request.getParameter("userId");//得到指定用户的ID
      String userName = YHInfoLogic.getUserName(userId, dbConn);
      String days = request.getParameter("days");//得到指定的所有日期
      String[] dayArray = days.split(",");
      String data = "{trTemp:\"";
      YHManageOutLogic yhaol = new YHManageOutLogic();
      //根据用户得到相应的排班类型
      //根据userId得到DUTY_TYPE对应的是attend_config表中的SEQ_ID;
      YHPersonLogic personLogic  = new YHPersonLogic();
      YHPerson person = personLogic.getPerson(dbConn, userId);
      int configSeqId = person.getDutyType();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      YHAttendConfig config = yhadl.selectConfigById(dbConn, String.valueOf(configSeqId));
      String dutyName = config.getDutyName();
      String dutyTime1 = config.getDutyTime1();
      String dutyTime2 = config.getDutyTime2();
      String dutyTime3 = config.getDutyTime3();
      String dutyTime4 = config.getDutyTime4();
      String dutyTime5 = config.getDutyTime5();
      String dutyTime6 = config.getDutyTime6();
      String dutyType1 = config.getDutyType1();
      String dutyType2 = config.getDutyType2();
      String dutyType3 = config.getDutyType3();
      String dutyType4 = config.getDutyType4();
      String dutyType5 = config.getDutyType5();
      String dutyType6 = config.getDutyType6();
      String general = "";
      String fillStr = "";
      if(config.getGeneral()!=null&& !config.getGeneral().trim().equals("")){
        general = config.getGeneral();
      }
      String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
      //对日期循环
      for(int i = 0; i < dayArray.length; i++){
        Date dateTemp = YHUtility.parseDate(dayArray[i]); 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTemp);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String weekStr = weeks[week-1];
        String trClass = "TableData";
        //判断当天是否是节假日.公休日
        String holidayStr = "未登记";
        String className = "#008000";
        String holidayTmp = "";
        if(checkHolidayTemp(request, response, dayArray[i]).equals("1")){
          holidayStr = "节假日";
          holidayTmp = "1";
        }else{
          //是否为公休日
          if(IsGeneral(general, dayArray[i]).equals("1")){
            holidayStr = "公休日";
            trClass = "TableContent";
            holidayTmp = "1";
          }
          //判断是否出差
          if(isFillRegister(request, response, dayArray[i], userId, "").equals("1")){
            fillStr = "(自动补登记)";
          }
          if(isEvectionTemp(request, response, dayArray[i], userId).equals("1")){
            holidayStr = "出差"+fillStr;
            holidayTmp = "1";
          }
        }
        data = data + "<tr align='center' class='"+ trClass+"'>" ;
        data = data + "<td align='center' nowrap>" + dayArray[i]+ " (" + weekStr + ")</td>";
        //对排版类型的6循环
        for(int j = 1;j<=6;j++){
          String dutyTime = "";
          String dutyType = "";
          if(j==1){
            dutyTime = dutyTime1;
            dutyType = dutyType1;
          }
          if(j==2){
            dutyTime = dutyTime2;
            dutyType = dutyType2;
          }
          if(j==3){
            dutyTime = dutyTime3;
            dutyType = dutyType3;
          }
          if(j==4){
            dutyTime = dutyTime4;
            dutyType = dutyType4;
          }
          if(j==5){
            dutyTime = dutyTime5;
            dutyType = dutyType5;
          }
          if(j==6){
            dutyTime = dutyTime6;
            dutyType = dutyType6;
          }
          if(isFillRegister(request, response, dayArray[i], userId, dutyType).equals("1")){
            fillStr = "(自动补登记)";
          }
          if(dutyTime!=null&&!dutyTime.trim().equals("")){
            if(holidayStr.equals("")){
              //判断是否请假
              if(isLeaveTemp(request, response, dayArray[i] + "" + dutyTime, userId).equals("1")){
                holidayStr = "请假"+fillStr;
                holidayTmp = "1";
              }else{
                //判断是否外出
                if(isOutTemp(request, response, dayArray[i]+" "+dutyTime, userId).equals("1")){
                  holidayStr = "外出"+fillStr;
                  holidayTmp = "1";
                }
              }
            }
            //查出当天有没有登记 记录
            List<YHAttendDuty> dutyList =  getAttendDuty(request,response,dayArray[i],String.valueOf(j),userId,config);
            String td = "" ;
            if(dutyList.size()>0){
              YHAttendDuty duty = dutyList.get(0);
              Date registerTime = duty.getRegisterTime();
              String registerTimeStr = dateFormat.format(registerTime);
              String registerType = duty.getRegisterType();
              String registerIp = duty.getRegisterIp();
              int seqId = duty.getSeqId();
              String remark = duty.getRemark();
              String ZC = "";
              String update = "";
              long dutyTimeInt = getLongByDutyTime(dutyTime);
              long registerTimeInt = getLongByDutyTime(registerTimeStr);
              if(dutyType.equals("1")){
                if(dutyTimeInt<registerTimeInt&&holidayTmp.equals("")){
                  ZC = "<span class=big4>迟到</span><br>";
                }
              }else{
                if(dutyTimeInt>registerTimeInt&&holidayTmp.equals("")){
                  ZC = "<span class=big4>早退</span>";
                }
              }
              if(remark!=null&&!remark.trim().equals("")){
                remark = remark.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
                update = "</br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
              }
              data = data + "<td align='center' ><font>"+registerTimeStr + "(" + registerIp + ")" + ZC + update + "</font></td>";
            }else{
              if(isFillRegister(request, response, dayArray[i], userId, "").equals("1")){
                holidayStr = "已补登记";
              }
              if(holidayStr.equals("未登记")){
                className = "red";
              }
              data = data + "<td align='center' ><font color='"+className+"'>"+holidayStr+"</font></td>";
            }
          }
        }
        data = data + "</tr>";
      } 
      data = data + "\"}";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, userName);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询在一段时间内得到考勤登记信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAllUserDutyInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      String userId = request.getParameter("userId");//得到指定用户的ID
      String userName = YHInfoLogic.getUserName(userId, dbConn);
      String days = request.getParameter("days");//得到指定的所有日期
      String[] userIdArray = {};
      String[] dayArray = days.split(",");
    /*  if(!userId.equals("")){
        userIdArray = userId.split(",");
      }*/
      String data = "{trTemp:\"";
      YHManageOutLogic yhaol = new YHManageOutLogic();
      //根据用户得到相应的排班类型
      //根据userId得到DUTY_TYPE对应的是attend_config表中的SEQ_ID;
      YHPersonLogic personLogic  = new YHPersonLogic();
      YHPerson person = personLogic.getPerson(dbConn, userId);
      String deptName = yhaol.selectByUserIdDept(dbConn, userId);
      int configSeqId = person.getDutyType();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      YHAttendConfig config = yhadl.selectConfigById(dbConn, String.valueOf(configSeqId));
      String dutyName = config.getDutyName();
      String dutyTime1 = config.getDutyTime1();
      String dutyTime2 = config.getDutyTime2();
      String dutyTime3 = config.getDutyTime3();
      String dutyTime4 = config.getDutyTime4();
      String dutyTime5 = config.getDutyTime5();
      String dutyTime6 = config.getDutyTime6();
      String dutyType1 = config.getDutyType1();
      String dutyType2 = config.getDutyType2();
      String dutyType3 = config.getDutyType3();
      String dutyType4 = config.getDutyType4();
      String dutyType5 = config.getDutyType5();
      String dutyType6 = config.getDutyType6();
      String general = "";
      if(config.getGeneral()!=null&& !config.getGeneral().trim().equals("")){
        general = config.getGeneral();
      }
      String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
      //对日期循环
      for(int i =0;i<dayArray.length;i++){
        Date dateTemp = YHUtility.parseDate(dayArray[i]); 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTemp);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        String weekStr = weeks[week-1];
        String trClass = "TableData";
        //判断当天是否是节假日.公休日
        String holidayStr = "未登记";
        String className = "#008000";
        String holidayTmp = "";
        if(checkHolidayTemp(request, response, dayArray[i]).equals("1")){
          holidayStr = "节假日";
          holidayTmp = "1";
        }else{
          //是否为公休日
          if(IsGeneral(general, dayArray[i]).equals("1")){
            holidayStr = "公休日";
            trClass = "TableContent";
            holidayTmp = "1";
          }
          //判断是否出差
          if(isEvectionTemp(request, response, dayArray[i], userId).equals("1")){
            holidayStr = "出差";
            holidayTmp = "1";
          }
        }
        String userNameTemp = person.getUserName();
        if(userNameTemp!=null&&!userNameTemp.equals("")){
          userNameTemp = userNameTemp.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
        }
        
        data = data + "<tr align='center' class='"+ trClass+"'>" ;
        data = data + "<td align='center' nowrap>" + dayArray[i]+ " (" + weekStr + ")</td><td align='center' nowrap>" + userNameTemp +"</td><td align='center' nowrap>"+deptName+"</td>" ;
        //对排版类型的6循环
        for(int j = 1;j<=6;j++){
          String dutyTime = "";
          String dutyType = "";
          if(j==1){
            dutyTime = dutyTime1;
            dutyType = dutyType1;
          }
          if(j==2){
            dutyTime = dutyTime2;
            dutyType = dutyType2;
          }
          if(j==3){
            dutyTime = dutyTime3;
            dutyType = dutyType3;
          }
          if(j==4){
            dutyTime = dutyTime4;
            dutyType = dutyType4;
          }
          if(j==5){
            dutyTime = dutyTime5;
            dutyType = dutyType5;
          }
          if(j==6){
            dutyTime = dutyTime6;
            dutyType = dutyType6;
          }
          if(dutyTime!=null&&!dutyTime.trim().equals("")){
            if(holidayStr.equals("")){
              //判断是否请假
              if(isLeaveTemp(request, response, dayArray[i] + "" + dutyTime, userId).equals("1")){
                holidayStr = "请假";
                holidayTmp = "1";
              }else{
                //判断是否外出
                if(isOutTemp(request, response, dayArray[i]+" "+dutyTime, userId).equals("1")){
                  holidayStr = "外出";
                  holidayTmp = "1";
                }
              }
            }
            //查出当天有没有登记 记录
            List<YHAttendDuty> dutyList =  getAttendDuty(request,response,dayArray[i],String.valueOf(j),userId,config);
            String td = "" ;
            if(dutyList.size()>0){
              YHAttendDuty duty = dutyList.get(0);
              Date registerTime = duty.getRegisterTime();
              String registerTimeStr = dateFormat.format(registerTime);
              String registerType = duty.getRegisterType();
              String registerIp = duty.getRegisterIp();
              int seqId = duty.getSeqId();
              String remark = duty.getRemark();
              String ZC = "";
              String update = "";
              long dutyTimeInt = getLongByDutyTime(dutyTime);
              long registerTimeInt = getLongByDutyTime(registerTimeStr);
              if(dutyType.equals("1")){
                if(dutyTimeInt<registerTimeInt&&holidayTmp.equals("")){
                  ZC = "<span class=big4>迟到</span><br>";
                }
              }else{
                if(dutyTimeInt>registerTimeInt&&holidayTmp.equals("")){
                  ZC = "<span class=big4>早退</span>";
                }
              }
              if(remark!=null&&!remark.equals("")){
                remark = remark.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
                update = "</br>备注："+remark +" <a href='javascript:remark("+seqId+");' title='修改备注'>修改</a> ";
              }
              data = data + "<td align='center' ><font>"+registerTimeStr  + ZC + update + "</font></td>";
            }else{
              if(holidayStr.equals("未登记")){
                className = "red";
              }
              data = data + "<td align='center'><font color='"+className+"'>"+holidayStr+"</font></td>";
            }
          }
        }
        data = data + "</tr>";
      } 
      data = data + "\"}";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, userName);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询排班类型 根据用户的id
   * 为刚加载的时候得到排班的Name
   * 并得到指定一天的考勤登记
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectDutyByUserIdNameDate(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      String userId = request.getParameter("userId");//得到指定用户的ID
      String userName = YHInfoLogic.getUserName(userId, dbConn);
      String date = request.getParameter("date");
      //根据userId得到DUTY_TYPE对应的是attend_config表中的SEQ_ID;
      YHPersonLogic personLogic  = new YHPersonLogic();
      YHPerson person = personLogic.getPerson(dbConn, userId);
      int configSeqId = person.getDutyType();//5
      YHAttendDuty duty = new YHAttendDuty();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      YHAttendConfig config = yhadl.selectConfigById(dbConn, String.valueOf(configSeqId));
      String configJson  = YHFOM.toJson(config).toString();
      //得到指定当天登记的记录
      YHDBUtility yhdbu = new YHDBUtility();
      String date1 = date.trim() + " 00:00:00";
      String date2 = date.trim() + " 23:59:59";
      String DBStr =  yhdbu.curDayFilter("REGISTER_TIME");
      date1 = yhdbu.getDateFilter("REGISTER_TIME", date1, ">=");
      date2 = yhdbu.getDateFilter("REGISTER_TIME", date2, "<=");
      List<YHAttendDuty> dutyList = yhadl.selectDuty(dbConn, String.valueOf(userId), date1,date2,"");
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      String data = "";
      if(config!=null){
        String dutyName = config.getDutyName();
        String dutyTime1 = config.getDutyTime1();
        String dutyTime2 = config.getDutyTime2();
        String dutyTime3 = config.getDutyTime3();
        String dutyTime4 = config.getDutyTime4();
        String dutyTime5 = config.getDutyTime5();
        String dutyTime6 = config.getDutyTime6();
        String dutyType1 = config.getDutyType1();
        String dutyType2 = config.getDutyType2();
        String dutyType3 = config.getDutyType3();
        String dutyType4 = config.getDutyType4();
        String dutyType5 = config.getDutyType5();
        String dutyType6 = config.getDutyType6();
        int c_zStatus =0;//判断迟到早退0为正常1为迟到,2为早退
      
        for (int i = 0; i < dutyList.size(); i++) {
          duty = dutyList.get(i);
          String dutyType = "";
          String dutyTime = "";
          if(duty.getRegisterType().equals("1")){
            dutyType = dutyType1;
            dutyTime = dutyTime1;
          }
          if(duty.getRegisterType().equals("2")){
            dutyType = dutyType2;
            dutyTime = dutyTime2;
          }
          if(duty.getRegisterType().equals("3")){
            dutyType = dutyType3;
            dutyTime = dutyTime3;
          }
          if(duty.getRegisterType().equals("4")){
            dutyType = dutyType4;
            dutyTime = dutyTime4;
          }
          if(duty.getRegisterType().equals("5")){
            dutyType = dutyType5;
            dutyTime = dutyTime5;
          }
          if(duty.getRegisterType().equals("6")){
            dutyType = dutyType6;
            dutyTime = dutyTime6;
          }
          //System.out.println(duty.getRemark()+"........."+duty.getSeqId());
          data = data + YHFOM.toJson(duty).toString().substring(0, YHFOM.toJson(duty).toString().length()-1)+",dutyTime:\""+dutyTime+"\",dutyType:"+dutyType+"},";
        }
     
        if(dutyList.size()>0){
          data = data.substring(0, data.length()-1);
        }
        if(dutyList.size()<=0){
          data ="[" + configJson +"]";
        }else{
          data = "[" + configJson + "," + data + "]";
        }
      }else{
        data = "[]";
      }
      
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, userName);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  //得到当天的上下班的登记 1次
  public String selectDutyByDay(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
      String userId = request.getParameter("userId");//得到指定用户的ID
      String userName = YHInfoLogic.getUserName(userId, dbConn);
      String date = request.getParameter("date");
      String registerType = request.getParameter("registerType");
      //System.out.println(registerType+"...........");
      //根据userId得到DUTY_TYPE对应的是attend_config表中的SEQ_ID;
      YHPersonLogic personLogic  = new YHPersonLogic();
      YHPerson person = personLogic.getPerson(dbConn, userId);
      int configSeqId = person.getDutyType();//5
      YHAttendDuty duty = new YHAttendDuty();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      YHAttendConfig config = yhadl.selectConfigById(dbConn,String.valueOf(configSeqId) );
      String configJson  = YHFOM.toJson(config).toString();
      //得到指定当天登记的记录
      YHDBUtility yhdbu = new YHDBUtility();
      String date1 = date + " 00:00:00";
      String date2 = date + " 23:59:59";
      String DBStr =  yhdbu.curDayFilter("REGISTER_TIME");
      date1 = yhdbu.getDateFilter("REGISTER_TIME", date1, ">=");
      date2 = yhdbu.getDateFilter("REGISTER_TIME", date2, "<=");
      List<YHAttendDuty> dutyList = yhadl.selectDuty(dbConn, String.valueOf(userId), date1,date2,registerType);
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      String dutyName = config.getDutyName();
      String dutyTime1 = config.getDutyTime1();
      String dutyTime2 = config.getDutyTime2();
      String dutyTime3 = config.getDutyTime3();
      String dutyTime4 = config.getDutyTime4();
      String dutyTime5 = config.getDutyTime5();
      String dutyTime6 = config.getDutyTime6();
      String dutyType1 = config.getDutyType1();
      String dutyType2 = config.getDutyType2();
      String dutyType3 = config.getDutyType3();
      String dutyType4 = config.getDutyType4();
      String dutyType5 = config.getDutyType5();
      String dutyType6 = config.getDutyType6();
      int c_zStatus =0;//判断迟到早退0为正常1为迟到,2为早退
      String data = "";
      for (int i = 0; i < dutyList.size(); i++) {
        duty = dutyList.get(i);
        String dutyType = "";
        String dutyTime = "";
        if(duty.getRegisterType().equals("1")){
          dutyType = dutyType1;
          dutyTime = dutyTime1;
        }
        if(duty.getRegisterType().equals("2")){
          dutyType = dutyType2;
          dutyTime = dutyTime2;
        }
        if(duty.getRegisterType().equals("3")){
          dutyType = dutyType3;
          dutyTime = dutyTime3;
        }
        if(duty.getRegisterType().equals("4")){
          dutyType = dutyType4;
          dutyTime = dutyTime4;
        }
        if(duty.getRegisterType().equals("5")){
          dutyType = dutyType5;
          dutyTime = dutyTime5;
        }
        if(duty.getRegisterType().equals("6")){
          dutyType = dutyType6;
          dutyTime = dutyTime6;
        }
        //System.out.println(duty.getRemark()+"........."+duty.getSeqId());
        data = data + YHFOM.toJson(duty).toString().substring(0, YHFOM.toJson(duty).toString().length()-1)+",dutyType:"+dutyType+",dutyTime:\""+dutyTime+"\"},";
      }
   
      if(dutyList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = "[" + data + "]";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, userName);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  //得到用户信息
  public String selectUserInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      String userName = YHInfoLogic.getUserName(userId, dbConn);
      String curDateStr = YHUtility.getCurDateTimeStr();
      String year = curDateStr.substring(0, 4);
      //System.out.println(year);
      String date1 = year + "-01-01 00:00:00";
      String date2 = year + "-12-20 23:59:59";
      String str[] = {"USER_ID='" + userId+"'" ,"(ALLOW='1' or ALLOW='3')",YHDBUtility.getDateFilter("LEAVE_DATE1", date1, ">="), YHDBUtility.getDateFilter("LEAVE_DATE2", date2, "<=")};
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      List<YHAttendLeave> leaveList = attendLogic.selectLeave(dbConn, str);
      int annualLeave = 0 ;
      for (int i = 0; i < leaveList.size(); i++) {
        YHAttendLeave leave = leaveList.get(i);
        annualLeave = annualLeave+ leave.getAnnualLeave();
      }
      //得到用户今年请假多长时间 精确到时
      YHAttendLeaveLogic yhall = new YHAttendLeaveLogic();
      YHAttendLeaveAct leaveAct = new YHAttendLeaveAct();
      long leaveDaysTotal = 0;
      String leaveDaysTotalStr = "";
      if(!YHUtility.isNullorEmpty(userId)){
        leaveDaysTotal = yhall.selectLeaveDaysByUserId(dbConn, userId);
        leaveDaysTotalStr = leaveAct.getDateTimeStr(leaveDaysTotal);
      }
    
      String data = "{userName:\"" + userName + "\",annualLeave:" + annualLeave +",leaveDaysTotal:\"" + leaveDaysTotalStr + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /*
   * 判断今天是否出差
   * 
   */
  public String isEvection(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      String date = request.getParameter("date");
      String dateStr = date.substring(0, 10); 
      //System.out.println(userId);
      String str[] = {"USER_ID in('" + userId + "')","ALLOW=1",YHDBUtility.getDateFilter("EVECTION_DATE1", date, "<="),YHDBUtility.getDateFilter("EVECTION_DATE2", date, ">=")};
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      List<YHAttendEvection> evectionList = attendLogic.selectEvection(dbConn, str);
      String data = "{evection:0}";
      if(evectionList.size()<=0){
        data = "{evection:1}";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
/**
 * 
 * @param request
 * @param response
 * @param date 时间 String 类型
 * @param userId 
 * @return
 * @throws Exception
 */
  public String isEvectionTemp(HttpServletRequest request,
      HttpServletResponse response,String date,String userId) throws Exception {
    Connection dbConn = null;
    String isEvection = "0";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String str[] = {"USER_ID in('" + userId + "')","ALLOW=1",YHDBUtility.getDateFilter("EVECTION_DATE1", date, "<="),YHDBUtility.getDateFilter("EVECTION_DATE2", date, ">=")};
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      List<YHAttendEvection> evectionList = attendLogic.selectEvection(dbConn, str);
      if(evectionList.size()>0){
        isEvection = "1";
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return isEvection;
  }
  
  /**
   * 是否自动补登记
   * @param request
   * @param response
   * @param date
   * @param userId
   * @param dutyType
   * @return
   * @throws Exception
   */
  public String isFillRegister(HttpServletRequest request,
      HttpServletResponse response,String date,String userId, String dutyType) throws Exception {
    Connection dbConn = null;
    String isFillRegister = "0";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String whereStr = "";
      if(!YHUtility.isNullorEmpty(dutyType)){
        whereStr += " and REGISTER_TYPE = '" + dutyType + "'";
      }
      String str[] = {"PROPOSER in('" + userId + "') and ASSESSING_STATUS=1" + whereStr + " and "+YHDBUtility.getDateFilter("FILL_TIME", date, "=")};
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      List<YHAttendFill> evectionList = attendLogic.getFillRegister(dbConn, str);
      if(evectionList.size()>0){
        isFillRegister = "1";
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return isFillRegister;
  }
  /*
   * 判断当天时间段是否请假
   */
  public String isLeave(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      String date = request.getParameter("date");
      //System.out.println(userId);
      String str[] = {"USER_ID in('" + userId + "')","ALLOW=1",YHDBUtility.getDateFilter("LEAVE_DATE1", date, "<="),YHDBUtility.getDateFilter("LEAVE_DATE2", date, ">=")};
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      List<YHAttendLeave> leaveList = attendLogic.selectLeave(dbConn, str);
      String data = "{leave:0}";
      if(leaveList.size()<=0){
        data = "{leave:1}";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @param date 时间字符串
   * @param userId
   * @return
   * @throws Exception
   */
  public String isLeaveTemp(HttpServletRequest request,
      HttpServletResponse response,String date,String userId) throws Exception {
    Connection dbConn = null;
    String isLeave = "0";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String str[] = {"USER_ID in('" + userId + "')","ALLOW=1",YHDBUtility.getDateFilter("LEAVE_DATE1", date, "<="),YHDBUtility.getDateFilter("LEAVE_DATE2", date, ">=")};
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      List<YHAttendLeave> leaveList = attendLogic.selectLeave(dbConn, str);
      if(leaveList.size()>0){
        isLeave = "1";
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return isLeave;
  }
  /*
   * 判断当天时间段是否外出 
   */
  public String isOut(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      String date = request.getParameter("date");
      //System.out.println(userId);
      String str[] = {"USER_ID in('" + userId + "')","ALLOW=1",YHDBUtility.getDateFilter("SUBMIT_TIME", date, "<="),YHDBUtility.getDateFilter("SUBMIT_TIME", date.substring(0, 10)+" 23:59:59", ">=")};
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      List<YHAttendOut> outList = attendLogic.selectOut(dbConn, str);
      String data = "{out:0}";
      List<YHAttendOut> outList2= new ArrayList<YHAttendOut>();
      for (int i = 0; i < outList.size(); i++) {
        YHAttendOut out = outList.get(i);
        if(out.getOutTime2().compareTo(date.substring(11,date.length()))>=0){
          outList2.add(out);
        }
      }
      if(outList2.size()<=0){
        data = "{out:1}";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String isOutTemp(HttpServletRequest request,
      HttpServletResponse response,String date,String userId) throws Exception {
    Connection dbConn = null;
    String isOut = "0";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String str[] = {"USER_ID in(" + userId + ")","ALLOW=1",YHDBUtility.getDateFilter("SUBMIT_TIME", date, "<="),YHDBUtility.getDateFilter("SUBMIT_TIME", date.substring(0, 10)+" 23:59:59", ">=")};
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      List<YHAttendOut> outList = attendLogic.selectOut(dbConn, str);
      if(outList.size()>0){
        isOut = "1";
      }

    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return isOut;
  }
  /**
   * 检查是否是公假日
   * status =0显示登记TABLE 1 为 不显示
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String checkHoliday(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendDuty duty = new YHAttendDuty();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
      String dateStr = request.getParameter("date");
      Map map = new HashMap();
      String data = "{status:1}";
      List<YHAttendHoliday> holidayList = yhadl.selectHoliday(dbConn, map);
      for (int i = 0; i < holidayList.size(); i++) {
        YHAttendHoliday holiday = holidayList.get(i);
        Date beginDate = holiday.getBeginDate();
        Date endDate = holiday.getEndDate();
        String beginDateStr = dateFormat.format(beginDate);
        String endDateStr = dateFormat.format(endDate);
          if(beginDateStr.compareTo(dateStr) <= 0 && endDateStr.compareTo(dateStr) >= 0){
            data = "{status:0}";
            break ;
          }
        }
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String checkHolidayTemp(HttpServletRequest request,
      HttpServletResponse response,String date) throws Exception {
    Connection dbConn = null;
    String isHoliday = "0";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendDuty duty = new YHAttendDuty();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
      Map map = new HashMap();
      List<YHAttendHoliday> holidayList = yhadl.selectHoliday(dbConn, map);
      for (int i = 0; i < holidayList.size(); i++) {
        YHAttendHoliday holiday = holidayList.get(i);
        Date beginDate = holiday.getBeginDate();
        Date endDate = holiday.getEndDate();
        String beginDateStr = dateFormat.format(beginDate);
        String endDateStr = dateFormat.format(endDate);
          if(beginDateStr.compareTo(date) <= 0 && endDateStr.compareTo(date) >= 0){
            isHoliday = "1";
            break ;
          }
        }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return isHoliday;
  }
  //根据userId 修改当天的上下班登记
  public String updateDuty(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userSeqId = user.getSeqId();
      String userName = user.getUserName();
      String userId = request.getParameter("userId");
      String date = request.getParameter("date");
      String registerTime1 = request.getParameter("registerTime1");
      String registerTime2 = request.getParameter("registerTime2");
      String registerTime3 = request.getParameter("registerTime3");
      String registerTime4 = request.getParameter("registerTime4");
      String registerTime5 = request.getParameter("registerTime5");
      String registerTime6 = request.getParameter("registerTime6");
      String seqId1 = request.getParameter("seqId1");
      String seqId2 = request.getParameter("seqId2");
      String seqId3 = request.getParameter("seqId3");
      String seqId4 = request.getParameter("seqId4");
      String seqId5 = request.getParameter("seqId5");
      String seqId6 = request.getParameter("seqId6");
      String dutyType1 = request.getParameter("dutyType1");
      String dutyType2 = request.getParameter("dutyType2");
      String dutyType3 = request.getParameter("dutyType3");
      String dutyType4 = request.getParameter("dutyType4");
      String dutyType5 = request.getParameter("dutyType5");
      String dutyType6 = request.getParameter("dutyType6");
      //System.out.println((registerTime1!=null));
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      YHAttendDutyLogic dutyLogic = new YHAttendDutyLogic();
      YHAttendDuty  duty = new  YHAttendDuty ();
      if(registerTime1!=null){
        if(seqId1!=null){
          if(!registerTime1.equals("")){
            //修改
            duty = yhadl.selectDutyById(dbConn, seqId1);
            duty.setRegisterTime(YHUtility.parseDate(date + " " + registerTime1));
            duty.setRegisterIp(userName+"修改");
            yhadl.updateDutyById(dbConn, duty);
          }else{
            //删除
            duty.setSeqId(Integer.parseInt(seqId1));
            yhadl.deleteDutyById(dbConn, duty);
          }
        }else {
          if(!registerTime1.equals("")){
          //添加
            duty.setRegisterType("1");
            duty.setRegisterTime(YHUtility.parseDate(date + " " + registerTime1));
            duty.setRegisterIp(userName+"修改");
            duty.setUserId(userId);
            yhadl.addDuty(dbConn, duty);
          }
        }
      }
      //2
      if(registerTime2!=null){
        if(seqId2!=null){
          if(!registerTime2.equals("")){
            //修改
            duty = yhadl.selectDutyById(dbConn, seqId2);
            duty.setRegisterTime(YHUtility.parseDate(date + " " + registerTime2));
            duty.setRegisterIp(userName+"修改");
            yhadl.updateDutyById(dbConn, duty);
          }else{
            //删除
            duty.setSeqId(Integer.parseInt(seqId2));
            yhadl.deleteDutyById(dbConn, duty);
          }
        }else{
          if(!registerTime2.equals("")){
            //添加
            duty.setRegisterType("2");
            duty.setRegisterTime(YHUtility.parseDate(date + " " + registerTime2));
            duty.setRegisterIp(userName+"修改");
            duty.setUserId(userId);
            yhadl.addDuty(dbConn, duty);
          }
        }
      }
      //3
      if(registerTime3!=null){
        if(seqId3!=null){
          if(!registerTime3.equals("")){
            //修改
            duty = yhadl.selectDutyById(dbConn, seqId3);
            duty.setRegisterTime(YHUtility.parseDate(date + " " + registerTime3));
            duty.setRegisterIp(userName+"修改");
            yhadl.updateDutyById(dbConn, duty);
          }else{
            //删除
            duty.setSeqId(Integer.parseInt(seqId3));
            yhadl.deleteDutyById(dbConn, duty);
          }
        }else{
          if(!registerTime3.equals("")){
            //添加
            duty.setRegisterType("3");
            duty.setRegisterTime(YHUtility.parseDate(date + " " + registerTime3));
            duty.setRegisterIp(userName+"修改");
            duty.setUserId(userId);
            yhadl.addDuty(dbConn, duty);
          }
        }
      }
      //4
      if(registerTime4!=null){
        if(seqId4!=null){
          if(!registerTime4.equals("")){
            //修改
            duty = yhadl.selectDutyById(dbConn, seqId4);
            duty.setRegisterTime(YHUtility.parseDate(date + " " + registerTime4));
            duty.setRegisterIp(userName+"修改");
            yhadl.updateDutyById(dbConn, duty);
          }else{
            //删除
            duty.setSeqId(Integer.parseInt(seqId4));
            yhadl.deleteDutyById(dbConn, duty);
          }
        }else{
          if(!registerTime4.equals("")){
            //添加
            duty.setRegisterType("4");
            duty.setRegisterTime(YHUtility.parseDate(date + " " + registerTime4));
            duty.setRegisterIp(userName+"修改");
            duty.setUserId(userId);
            yhadl.addDuty(dbConn, duty);
          }
        }
      }
      //5
      if(registerTime5!=null){
        if(seqId5!=null){
          if(!registerTime5.equals("")){
            //修改
            duty = yhadl.selectDutyById(dbConn, seqId5);
            duty.setRegisterTime(YHUtility.parseDate(date + " " + registerTime5));
            duty.setRegisterIp(userName+"修改");
            yhadl.updateDutyById(dbConn, duty);
          }else{
            //删除
            duty.setSeqId(Integer.parseInt(seqId5));
            yhadl.deleteDutyById(dbConn, duty);
          }
        }else{
          if(!registerTime5.equals("")){
            //添加
            duty.setRegisterType("5");
            duty.setRegisterTime(YHUtility.parseDate(date + " " + registerTime5));
            duty.setRegisterIp(userName+"修改");
            duty.setUserId(userId);
            yhadl.addDuty(dbConn, duty);
          }
        }
      }
      //6
      if(registerTime6!=null){
        if(seqId6!=null){
          if(!registerTime6.equals("")){
            //修改
            duty = yhadl.selectDutyById(dbConn, seqId6);
            duty.setRegisterTime(YHUtility.parseDate(date + " " + registerTime6));
            duty.setRegisterIp(userName+"修改");
            yhadl.updateDutyById(dbConn, duty);
          }else{
            //删除
            duty.setSeqId(Integer.parseInt(seqId6));
            yhadl.deleteDutyById(dbConn, duty);
          }
        }else{
          if(!registerTime6.equals("")){
            //添加
            duty.setRegisterType("6");
            duty.setRegisterTime(YHUtility.parseDate(date + " " + registerTime6));
            duty.setRegisterIp(userName+"修改");
            duty.setUserId(userId);
            yhadl.addDuty(dbConn, duty);
          }
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "{}");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  //得到所有排版类型
  public String selectAllConfig(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      String dutyType = request.getParameter("dutyType");
      YHAttendConfig config = new YHAttendConfig();
      YHAttendConfigLogic yhacl = new YHAttendConfigLogic();
      String data = "[";
      Map map = new HashMap();
      //判断得到哪一种排班类型
   /*   if(dutyType!=null&&!dutyType.equals("0")){
        map.put("SEQ_ID", dutyType);
      }*/
      List<YHAttendConfig> configList = yhacl.selectConfig(dbConn, map);
      for (int i = 0; i < configList.size(); i++) {
        data = data+(YHFOM.toJson(configList.get(i))).toString() + ",";
      }
      if(configList.size() > 0 ){
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  //根据部门得到所有人员Id
  public String selectUserIds(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userIds = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      String deptId = request.getParameter("deptId");
      YHManageAttendLogic logic = new YHManageAttendLogic();
      
      userIds = logic.selectUserIds(Integer.parseInt(deptId), dbConn);
      //System.out.println(userIds);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "{userIds:\""+userIds+"\"}");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  //根据用户的Ids和排班类型来得到userIds
  public String selectuserIdByDutyType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userIds = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      userIds = request.getParameter("userId");
      String dutyType = request.getParameter("dutyType");
      YHManageAttendLogic logic = new YHManageAttendLogic();
      List<YHPerson> personList = new ArrayList<YHPerson>();
      //如果是所有的,那么查出所有类型的排班类型
      if(dutyType.equals("0")){
        YHAttendConfigLogic yhacl = new YHAttendConfigLogic();
        Map map = new HashMap();
        List<YHAttendConfig> configList = yhacl.selectConfig(dbConn, map);
        for (int i = 0; i < configList.size(); i++) {
          dutyType = dutyType + configList.get(i).getSeqId()+",";
        }
        if(configList.size()>0){
          dutyType = dutyType.substring(0, dutyType.length()-1);
        }
      }
   /*   String newDutyType = "";
      if(dutyType!=null&&!dutyType.equals("")){
        String[] dutyArray = dutyType.split(",");
        for (int i = 0; i < dutyArray.length; i++) {
          newDutyType = newDutyType + "'" + dutyArray[i] + "',";
        }
        if(dutyArray.length>0){
          newDutyType = newDutyType.substring(0, newDutyType.length()-1);
        }
      }*/
      String[] str = {"SEQ_ID in(" + userIds + ")","DUTY_TYPE in(" + dutyType + ")"}; 
      if(!userIds.equals("")&&!dutyType.equals("0")){
        personList = logic.selectPerson(dbConn, str);   
      } 
      String userIds2 = "";
      for (int i = 0; i <personList.size(); i++) {
        userIds2 = userIds2 + personList.get(i).getSeqId() + ",";
      }
      if(!userIds2.equals("")){
        userIds2 = userIds2.substring(0, userIds2.length()-1);
      }
      //查询免签人员的Id
      YHSysParaLogic yhpl = new YHSysParaLogic();
      String noDutyUserId = yhpl.selectPara(dbConn, "NO_DUTY_USER");
      if(noDutyUserId==null||noDutyUserId.trim().equals("")){
        noDutyUserId = "";
      }
      String[] noDutyUserIds = noDutyUserId.split(",");
      String[] userIds2Array = userIds2.split(",");
      List userSeqIds =  new ArrayList();
      for (int i = 0; i < userIds2Array.length; i++) {
        for (int j = 0; j < noDutyUserIds.length; j++) {
          if(userIds2Array[i].equals(noDutyUserIds[j])){
            break;
          }else{
            if(j==noDutyUserIds.length-1&&!userIds2Array[i].equals(noDutyUserIds[j])){
              userSeqIds.add(userIds2Array[i]);
            }
          } 
        }    
      }
      String userSeqId = "";
      for (int i = 0; i < userSeqIds.size(); i++) {
        userSeqId = userSeqId + userSeqIds.get(i) + ",";
      }
      if(!userSeqId.equals("")){
        userSeqId = userSeqId.substring(0, userSeqId.length()-1);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, "{userIds:\""+userSeqId+"\"}");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *   //得到在一段时间没内的所有节假日
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getHolidayList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendDuty duty = new YHAttendDuty();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      String[] str = {YHDBUtility.getDateFilter("BEGIN_DATE", endTime, "<="),YHDBUtility.getDateFilter("END_DATE", beginTime, ">=")};
      //System.out.println(beginTime);
      String data = "[";
      List<YHAttendHoliday> holidayList = yhadl.selectHoliday(dbConn, str);
      for (int i = 0; i < holidayList.size(); i++) {
        YHAttendHoliday holiday = holidayList.get(i);
          data = data + YHFOM.toJson(holiday).toString() + ",";
        }
      if(holidayList.size()>0){
         data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *   //得到在一段时间没内的所有出差
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getEvectionList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendDuty duty = new YHAttendDuty();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
      String userId = request.getParameter("userId");
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
  
      beginTime = beginTime + " 00:00:00";
      endTime = endTime + " 23:59:59";
      String str[] = {"USER_ID in(" + userId + ")","ALLOW=1",YHDBUtility.getDateFilter("EVECTION_DATE1", endTime, "<="),YHDBUtility.getDateFilter("EVECTION_DATE2", beginTime, ">=")};
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      List<YHAttendEvection> evectionList = attendLogic.selectEvection(dbConn, str);
      String data = "[";
      for (int i = 0; i < evectionList.size(); i++) {
        YHAttendEvection evection = evectionList.get(i);
          data = data + YHFOM.toJson(evection).toString() + ",";
        }
      if(evectionList.size()>0){
         data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *   //得到在一段时间没内的所有请假
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getLeaveList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHAttendDuty duty = new YHAttendDuty();
      YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
      String userId = request.getParameter("userId");
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
  
      beginTime = beginTime + " 00:00:00";
      endTime = endTime + " 23:59:59";
      String str[] = {"USER_ID in(" + userId + ")","ALLOW=1",YHDBUtility.getDateFilter("LEAVE_DATE1", endTime, "<="),YHDBUtility.getDateFilter("LEAVE_DATE2", beginTime, ">=")};
      YHManageAttendLogic attendLogic = new YHManageAttendLogic();
      List<YHAttendLeave> leaveList = attendLogic.selectLeave(dbConn, str);
      String data = "[";
      for (int i = 0; i < leaveList.size(); i++) {
        YHAttendLeave leave = leaveList.get(i);
          data = data + YHFOM.toJson(leave).toString() + ",";
        }
      if(leaveList.size()>0){
         data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * cvs导出
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportCVS(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    OutputStream ops = null;
    InputStream is = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String  expType = request.getParameter("expType");//1为外出2为请假3为出差
      SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
      String userId = request.getParameter("userId");
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      beginTime = beginTime + " 00:00:00";
      endTime = endTime + " 23:59:59";
      String fileName = "";
      if(expType!=null&&!expType.equals("")){
        if(expType.equals("1")){
          fileName = "考勤外出数据.cvs";
        }
        if(expType.equals("2")){
          fileName = "考勤请假数据.cvs";
        }
        if(expType.equals("3")){
          fileName = "考勤出差数据.cvs";
        }
      }
      //fileName = URLEncoder.encode(fileName, "UTF-8");//fileName.getBytes("GBK"), "iso8859-1")
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition", "attachment; filename="
          + new String(fileName.getBytes("GBK"), "iso8859-1") );
      ops = response.getOutputStream();
      ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
      List<YHAttendOut> outList = new ArrayList<YHAttendOut>();
      YHExpAttendLogic el = new YHExpAttendLogic();
      YHManageOutLogic yhaol = new YHManageOutLogic();
      if(expType!=null&&!expType.equals("")){
        if(expType.equals("1")){
      /*    if(!userId.equals("")){
            String sql = "select o.USER_ID as USER_ID ,p.USER_NAME as USER_NAME,o.OUT_TYPE as OUT_TYPE,o.REGISTER_IP as REGISTER_IP,o.SUBMIT_DATE as SUBMIT_DATE,o.OUT_TIME1 as OUT_TIME2,o.OUT_TIME2 as OUT_TIME2 ,o.LEADER_ID as LEADER_ID,o.STATUS as STATUS "
              + "from ATTEND_OUT o left outer join PERSON p on o.USER_ID = cast(p.SEQ_ID as varchar(40)) where 1=1 ";
            String newUserId = "";
            String[] userIdArray = userId.split(",");
            for (int i = 0; i < userIdArray.length; i++) {
              newUserId = newUserId + "'" + userIdArray[i] + "',";
            }
            sql = sql + " and o.USER_ID in (" + newUserId + ")";
            if(beginTime!=null&&!beginTime.equals("")){
              sql = sql + " and " + YHDBUtility.getDateFilter("SUBMIT_TIME", beginTime, ">=");
            }
            if(endTime!=null&&!endTime.equals("")){
              sql = sql + " and " + YHDBUtility.getDateFilter("SUBMIT_TIME", endTime, ">=");
            }
            YHExpLogic el = new YHExpLogic();
            dbL =  el.getCVS(sql, conn, expType);
          }*/
    
          if(!userId.equals("")){
            String newUserId = "";
            String[] userIdArray = userId.split(",");
            for (int i = 0; i < userIdArray.length; i++) {
              newUserId = newUserId + "'" + userIdArray[i] + "',";
            }
            newUserId = newUserId.substring(0, newUserId.length()-1);
            String str[] = {"USER_ID in(" + newUserId + ")",YHDBUtility.getDateFilter("SUBMIT_TIME", beginTime, ">="),YHDBUtility.getDateFilter("SUBMIT_TIME", endTime, "<=")+" order by SUBMIT_TIME"};
            outList = yhaol.selectOutManage(conn, str);
            dbL = el.getOutCVS(conn, outList);
          }
          
        }
        if(expType.equals("2")){
          YHManageLeaveLogic leaveLogic = new YHManageLeaveLogic();
          List<YHAttendLeave> leaveList = new ArrayList<YHAttendLeave>();
          if(!userId.equals("")){
            String newUserId = "";
            String[] userIdArray = userId.split(",");
            for (int i = 0; i < userIdArray.length; i++) {
              newUserId = newUserId + "'" + userIdArray[i] + "',";
            }
            newUserId = newUserId.substring(0, newUserId.length()-1);
            String str[] = {"USER_ID in(" + userId + ")",YHDBUtility.getDateFilter("LEAVE_DATE1", beginTime, ">="),YHDBUtility.getDateFilter("LEAVE_DATE2", endTime, "<="),"ALLOW in('1','3') order by LEAVE_DATE1"};
            leaveList =  leaveLogic.selectLeave(conn, str);
            dbL = el.getLeaveCVS(conn, leaveList);
          }
        }
        if(expType.equals("3")){
          YHManageEvectionLogic evectionLogic = new YHManageEvectionLogic();
          List<YHAttendEvection> evectionList = new ArrayList<YHAttendEvection>();
          if(!userId.equals("")){
            String newUserId = "";
            String[] userIdArray = userId.split(",");
            for (int i = 0; i < userIdArray.length; i++) {
              newUserId = newUserId + "'" + userIdArray[i] + "',";
            }
            newUserId = newUserId.substring(0, newUserId.length()-1);
            String str[] = {"USER_ID in(" + userId + ")",YHDBUtility.getDateFilter("EVECTION_DATE1", beginTime, ">="),YHDBUtility.getDateFilter("EVECTION_DATE2", endTime, "<="),"ALLOW='1' order by EVECTION_DATE1"};
            evectionList = evectionLogic.selectEvectionManage(conn, str);
            dbL = el.getEvectionCVS(conn, evectionList);
          }
        }
      }
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
  /**
   * cvs导出
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exprotAttendCVS(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    OutputStream ops = null;
    InputStream is = null;
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String  dutyType = request.getParameter("dutyType");
      SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
      String userId = request.getParameter("userId");
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      String days = request.getParameter("days");
      beginTime = beginTime + " 00:00:00";
      endTime = endTime + " 23:59:59";
      String fileName = "上下班登记数据.xls";

      //fileName = URLEncoder.encode(fileName, "UTF-8");//fileName.getBytes("GBK"), "iso8859-1")
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition", "attachment; filename="
          + new String(fileName.getBytes("GBK"), "iso8859-1") );
      ops = response.getOutputStream();
      ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
      String[] userIdArray = {};
      if(!userId.equals("")){
        userIdArray = userId.split(",");
      }
      String[] dayArray = days.split(",");
      String data = "[";
      //System.out.println(userIdArray.length+userId);
      YHManageOutLogic yhaol = new YHManageOutLogic();
      //对所有用户循环

      for(int i = 0;i<userIdArray.length;i++){
        String deptName = yhaol.selectByUserIdDept(dbConn, userIdArray[i]);
        long perfectCount = 0;//全勤
        long hourTotal = 0;//总时长

        long lateCount = 0; //迟到
        long dutyOnTotal = 0;//上班登记总数
        long dutyOffTotal = 0;//下班登记总数
        long earlyCount = 0;//早退
        long addDutyOnCount = 0;//加班上班登记
        long addDutyOffCount = 0;//加班下班登记
        long dutyOn = 0;//排班中有多少上班的

        long dutyOff = 0;//排班中有多少下班的

        String userSeqId = userIdArray[i];
        //根据用户得到相应的排班类型

        //根据userId得到DUTY_TYPE对应的是attend_config表中的SEQ_ID;
        YHPersonLogic personLogic  = new YHPersonLogic();
        YHPerson person = personLogic.getPerson(dbConn, userIdArray[i]);
        int configSeqId = person.getDutyType();//5
        YHAttendDuty duty = new YHAttendDuty();
        YHAttendDutyLogic yhadl = new YHAttendDutyLogic();
        YHAttendConfig config = yhadl.selectConfigById(dbConn, String.valueOf(configSeqId));
        String dutyName = config.getDutyName();
        String dutyTime1 = config.getDutyTime1();
        String dutyTime2 = config.getDutyTime2();
        String dutyTime3 = config.getDutyTime3();
        String dutyTime4 = config.getDutyTime4();
        String dutyTime5 = config.getDutyTime5();
        String dutyTime6 = config.getDutyTime6();
        String dutyType1 = config.getDutyType1();
        String dutyType2 = config.getDutyType2();
        String dutyType3 = config.getDutyType3();
        String dutyType4 = config.getDutyType4();
        String dutyType5 = config.getDutyType5();
        String dutyType6 = config.getDutyType6();
        String general = "";
        if(config.getGeneral()!=null&& !config.getGeneral().trim().equals("")){
          general = config.getGeneral();
        }
        //得到是排版类型的总登记数
        int configCount =0;
        if(dutyTime1!=null&&!dutyTime1.trim().equals("")){
          if(dutyType1.equals("1")){
            dutyOn++;
          }else{
            dutyOff++;
          }
          configCount++;}
        if(dutyTime2!=null&&!dutyTime2.trim().equals("")){
          if(dutyType2.equals("1")){
            dutyOn++;
          }else{
            dutyOff++;
          }
          configCount++;}
        if(dutyTime3!=null&&!dutyTime3.trim().equals("")){
          if(dutyType3.equals("1")){
            dutyOn++;
          }else{
            dutyOff++;
          }
          configCount++;}
        if(dutyTime4!=null&&!dutyTime4.trim().equals("")){
          if(dutyType4.equals("1")){
            dutyOn++;
          }else{
            dutyOff++;
          }
          configCount++;}
        if(dutyTime5!=null&&!dutyTime5.trim().equals("")){
          if(dutyType5.equals("1")){
            dutyOn++;
          }else{
            dutyOff++;
          }
          configCount++;}
        if(dutyTime6!=null&&!dutyTime6.trim().equals("")){
          if(dutyType6.equals("1")){
            dutyOn++;
          }else{
            dutyOff++;
          }
          configCount++;}
        dutyOnTotal = dutyOn*dayArray.length;
        dutyOffTotal = dutyOff*dayArray.length;
      //对指定天数循环

        for(int j = 0;j<dayArray.length;j++){
          int configCountTmp = 0;
          String holidayStr = "";
          //判断是否公休日

          if(IsGeneral(general, dayArray[j]).equals("1")){
            holidayStr = "1";
          }else{
            //判断是否节假日;
            if(checkHolidayTemp(request, response, dayArray[j]).equals("1")){
              holidayStr = "2";
            }else{
              //判断是否出差
              if( isEvectionTemp(request, response,dayArray[j],userIdArray[i]).equals("1")){
                holidayStr = "2";
              }else{
                //判断时候请假

                if(isLeaveTemp(request, response,dayArray[j],userIdArray[i]).equals("1")){
                  holidayStr = "2";
                }
              }
            }
          }  

          //得到当天的上下班登记情况
          List<YHAttendDuty> dutyList =  getAttendDuty(request,response,dayArray[j],"",userIdArray[i],config);
          long charTemp = 0;
          if(dutyList.size()>0){
            String registerTime1 = "";
            String registerTime2 = "";
            String registerTime3 = "";
            String registerTime4 = "";
            String registerTime5 = "";
            String registerTime6 = "";
            String dutyTypeTemp1 = "";
            String dutyTypeTemp2 = "";
            String dutyTypeTemp3 = "";
            String dutyTypeTemp4 = "";
            String dutyTypeTemp5 = "";
            String dutyTypeTemp6 = "";
            long char1 = 0;
            long char2 = 0;
            long char3= 0;
            long char4 = 0;
            long char5 = 0;
            long char6 = 0;
            for(int k = 0; k< dutyList.size(); k++){
              duty = dutyList.get(k);
              Date registerTime = duty.getRegisterTime();
              String registerType = duty.getRegisterType();
              String dutyTypeTemp = "";
              long dutyTimeInt = 0;
              long registerTimeInt = getLongByDutyTime(dateFormat2.format(registerTime));
              //判断时长
              if(dutyTime1!=null&&!dutyTime1.trim().equals("") &&registerType.equals("1")){
                registerTime1 = dateFormat2.format(registerTime);
                dutyTypeTemp1 = dutyType1;
                dutyTypeTemp = dutyType1;
                dutyTimeInt =getLongByDutyTime(dutyTime1);
              }
              if(dutyTime2!=null&&!dutyTime2.trim().equals("") &&registerType.equals("2")){
                registerTime2 = dateFormat2.format(registerTime);
                dutyTypeTemp2 = dutyType2;
                dutyTypeTemp = dutyType2;
                dutyTimeInt =getLongByDutyTime(dutyTime2);
              }
              if(dutyTime3!=null&&!dutyTime3.trim().equals("") &&registerType.equals("3")){
                registerTime3 = dateFormat2.format(registerTime);
                dutyTypeTemp3 = dutyType3;
                dutyTypeTemp = dutyType3;
                dutyTimeInt =getLongByDutyTime(dutyTime3);
              }
              if(dutyTime4!=null&&!dutyTime4.trim().equals("") &&registerType.equals("4")){
                registerTime4 = dateFormat2.format(registerTime);
                dutyTypeTemp4 = dutyType4;
                dutyTypeTemp = dutyType4;
                dutyTimeInt =getLongByDutyTime(dutyTime4);
              }
              if(dutyTime5!=null&&!dutyTime5.trim().equals("") &&registerType.equals("5")){
                registerTime5 = dateFormat2.format(registerTime);
                dutyTypeTemp5 = dutyType5;
                dutyTypeTemp = dutyType5;
                dutyTimeInt =getLongByDutyTime(dutyTime5);
              }
              if(dutyTime6!=null&&!dutyTime6.trim().equals("") &&registerType.equals("6")){
                registerTime6 = dateFormat2.format(registerTime);
                dutyTypeTemp6 = dutyType6;
                dutyTypeTemp = dutyType6;
                dutyTimeInt =getLongByDutyTime(dutyTime6);
              }  
              if(dutyTypeTemp.equals("1")){
                if(holidayStr.equals("1")){
                  //上班加班+1
                  addDutyOnCount = addDutyOnCount+1;
                  dutyOnTotal = dutyOnTotal -1;
                }else if(holidayStr.equals("2")){
                  dutyOnTotal = dutyOnTotal -1;
                }else{
                  dutyOnTotal = dutyOnTotal -1;
                  if(dutyTimeInt<registerTimeInt){
                    //迟到+1
                    lateCount = lateCount + 1;
                    //判断迟到是否有过迟到
                  }else{
                    configCountTmp = configCountTmp+1;
                  }
                }
              }else{
                if(holidayStr.equals("1")){
                  //下班加班+1
                  addDutyOffCount = addDutyOffCount+1;
                  dutyOffTotal = dutyOffTotal -1;
                }else if(holidayStr.equals("2")){
                  dutyOffTotal = dutyOffTotal -1;
                }else{
                  //下班登记总数-1
                  dutyOffTotal = dutyOffTotal -1;
                  //alert(dutyTimeInt+":"+registerTimeInt+":"+seqId);
                  if(dutyTimeInt>registerTimeInt){
                    //早退+1
                    earlyCount = earlyCount + 1;
                    //是否有早退的

                  }else{
                    configCountTmp = configCountTmp+1;
                  }
                }
              }
            }
            //得到时长
            if(dutyTime1!=null&&!dutyTime1.trim().equals("")&&!registerTime1.trim().equals("")&&dutyType1.equals("1")){
              for(int m = 2;m<=6;m++){
                String dutyTimeTemp = "" ;
                String registerTimeTemp = "";
                String dutyTypeTemp = "";
                if(m==2){dutyTimeTemp = dutyTime2;registerTimeTemp = registerTime2;dutyTypeTemp=dutyTypeTemp1; }
                if(m==3){dutyTimeTemp = dutyTime3;registerTimeTemp = registerTime3;dutyTypeTemp=dutyTypeTemp2; }
                if(m==4){dutyTimeTemp = dutyTime4;registerTimeTemp = registerTime4;dutyTypeTemp=dutyTypeTemp3; }
                if(m==5){dutyTimeTemp = dutyTime5;registerTimeTemp = registerTime5;dutyTypeTemp=dutyTypeTemp4; }
                if(m==6){dutyTimeTemp = dutyTime6;registerTimeTemp = registerTime6;dutyTypeTemp=dutyTypeTemp5; }
                if(dutyTimeTemp!=null&&!dutyTimeTemp.trim().equals("")){
                   if(!registerTimeTemp.equals("")&&dutyTypeTemp.equals("2")){
                     long registerTime1Int = getLongByDutyTime(registerTime1);
                     long registerTimeTempInt = getLongByDutyTime(registerTimeTemp);
                     char1 = registerTimeTempInt - registerTime1Int;
                   }
                   break;
                }
              }
            }  
            //第2次做比较
            if(dutyTime2!=null&&!dutyTime2.trim().equals("")&&!registerTime2.equals("")&&dutyType2.equals("1")){
              for(int m = 3;m<=6;m++){
                String dutyTimeTemp = "";
                String registerTimeTemp = "";
                String dutyTypeTemp = "";
                if(m==3){dutyTimeTemp = dutyTime3;registerTimeTemp = registerTime3;dutyTypeTemp=dutyTypeTemp3;}
                if(m==4){dutyTimeTemp = dutyTime4;registerTimeTemp = registerTime4;dutyTypeTemp=dutyTypeTemp4;}
                if(m==5){dutyTimeTemp = dutyTime5;registerTimeTemp = registerTime5;dutyTypeTemp=dutyTypeTemp5;}
                if(m==6){dutyTimeTemp = dutyTime6;registerTimeTemp = registerTime6;dutyTypeTemp=dutyTypeTemp6;}
                if(dutyTimeTemp!=null&&!dutyTimeTemp.trim().equals("")){
                   if(!registerTimeTemp.equals("")&&dutyTypeTemp.equals("2")){
                     long registerTime2Int = getLongByDutyTime(registerTime2);
                     long registerTimeTempInt = getLongByDutyTime(registerTimeTemp);
                     char2 = registerTimeTempInt - registerTime2Int;
                   }
                   break;
                }
              }
            }  
            //第3次做比较
            if(dutyTime3!=null&&!dutyTime3.trim().equals("")&&!registerTime3.equals("")&&dutyType3.equals("1")){
              for(int m = 4;m<=6;m++){
                String dutyTimeTemp = "";
                String dutyTypeTemp = "";
                String registerTimeTemp = "";
                if(m==4){dutyTimeTemp = dutyTime4;registerTimeTemp = registerTime4;dutyTypeTemp=dutyTypeTemp4;}
                if(m==5){dutyTimeTemp = dutyTime5;registerTimeTemp = registerTime5;dutyTypeTemp=dutyTypeTemp5;}
                if(m==6){dutyTimeTemp = dutyTime6;registerTimeTemp = registerTime6;dutyTypeTemp=dutyTypeTemp6;}
                if(dutyTimeTemp!=null&&!dutyTimeTemp.trim().equals("")){
                   if(!registerTimeTemp.equals("")&&dutyTypeTemp.equals("2")){
                     long registerTime3Int = getLongByDutyTime(registerTime3);
                     long registerTimeTempInt = getLongByDutyTime(registerTimeTemp);
                     char3 = registerTimeTempInt - registerTime3Int;
                   }
                   break;
                }
              }
            }
            //第4次做比较
            if(dutyTime4!=null&&!dutyTime4.trim().equals("")&&!registerTime4.equals("")&&dutyType4.equals("1")){
              for(int m = 5;m<=6;m++){
                String dutyTimeTemp = "";
                String dutyTypeTemp = "";
                String registerTimeTemp = "";
                if(m==5){dutyTimeTemp = dutyTime5;registerTimeTemp = registerTime5;dutyTypeTemp=dutyTypeTemp5;}
                if(m==6){dutyTimeTemp = dutyTime6;registerTimeTemp = registerTime6;dutyTypeTemp=dutyTypeTemp6;}
                if(dutyTimeTemp!=null&&!dutyTimeTemp.trim().equals("")){
                   if(!registerTimeTemp.equals("")&&dutyTypeTemp.equals("2")){
                     long registerTime4Int = getLongByDutyTime(registerTime4);
                     long registerTimeTempInt = getLongByDutyTime(registerTimeTemp);
                     char4 = registerTimeTempInt - registerTime4Int;
                   }
                   break;
                }
              }
            }
            //第5次做比较
            if(dutyTime5!=null&&!dutyTime5.trim().equals("")&&!registerTime5.equals("")&&dutyType5.equals("1")){
              if(dutyTime6!=null&&!dutyTime6.trim().equals("")&&!registerTime6.equals("")&&dutyTypeTemp6.equals("2")){
                //System.out.println(registerTime5);
                long registerTime5Int = getLongByDutyTime(registerTime5);
                long registerTimeTempInt = getLongByDutyTime(registerTime6);
                char5 = registerTimeTempInt - registerTime5Int;
              }
            }
        
           charTemp = char1+char2+char3+char4+char5;
           //全勤天

           if(holidayStr.equals("")&&configCountTmp==configCount){
             perfectCount = perfectCount + 1;
           }
          }else{
            if(!holidayStr.equals("")){
              dutyOnTotal = dutyOnTotal-dutyOn;
              dutyOffTotal = dutyOffTotal-dutyOff;
            }
          }
          hourTotal = hourTotal + charTemp;
        }
        YHDbRecord rc = new YHDbRecord();
        rc.addField("部门", deptName);
        rc.addField("姓名", person.getUserName());
        rc.addField("全勤天 ", perfectCount);
        rc.addField("迟到", lateCount);
        rc.addField("上班未登记 ", dutyOnTotal);
        
        rc.addField("早退", earlyCount);
        rc.addField("下班未登记  ", dutyOffTotal);
        rc.addField("加班上班登记", addDutyOnCount);
        rc.addField("加班下班登记", addDutyOffCount);
        dbL.add(rc);
    }
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
  
}
