package yh.core.funcs.userinfo.act;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.calendar.data.YHCalendar;
import yh.core.funcs.calendar.logic.YHCalendarLogic;
import yh.core.funcs.diary.data.YHDiary;
import yh.core.funcs.diary.logic.YHDiaryLogic;
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.userinfo.logic.YHUserInfoLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;

public class YHUserInfoAct {
      private YHUserInfoLogic logic=new YHUserInfoLogic();

      /**
       * 获取登录用户菜单权限
       * 
       * @param request
       * @param response
       * @return
       * @throws Exception
       */
 public String getFuncStrAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
   Connection dbConn=null;
  try{
   YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   dbConn = requestDbConn.getSysDbConn();
   String userId = request.getParameter("userId");
   int uId = 0 ;
   if (YHUtility.isInteger(userId)) {
     uId = Integer.parseInt(userId);
   }
   YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
   String userPriv=person.getUserPriv();
   String login_funcs_str=this.logic.getFuncStrLogic(dbConn,userPriv);
  // userId
   YHMyPriv mp = new YHMyPriv();
   mp = YHPrivUtil.getMyPriv(dbConn, person, "3", 2);
   boolean isShow = false;
   if (YHPrivUtil.isUserPriv(dbConn, uId, mp, person)) {
     isShow = true;
   }
   login_funcs_str="{login_funcs_str:'"+login_funcs_str+"' , isShow:"+ isShow +"}";

   request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
   request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
   request.setAttribute(YHActionKeys.RET_DATA, login_funcs_str);
  } catch (Exception ex) {
   request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
   request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
   throw ex;
 }
   return "/core/inc/rtjson.jsp";
 }
 
 /**
  * 获取查看用户的详细信息
  * 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
public String getUserDetailAct(HttpServletRequest request, HttpServletResponse response) throws Exception {
Connection dbConn=null;
try{
YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
dbConn = requestDbConn.getSysDbConn();
YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

String userId=request.getParameter("userId");
if(YHUtility.isNullorEmpty(userId)){
  userId=person.getSeqId()+"";
}
YHPerson login_user=this.logic.getUserDetailLogic(dbConn,person,userId);

if (login_user == null) {
  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
  request.setAttribute(YHActionKeys.RET_MSRG, "未找到相应记录");
  return "/core/inc/rtjson.jsp";
}

StringBuffer data = YHFOM.toJson(login_user);
//System.out.println(data);
request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
request.setAttribute(YHActionKeys.RET_DATA, data.toString());
} catch (Exception e) {
request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
throw e;
}
return "/core/inc/rtjson.jsp";
}
 /**
  * 获取共享日志
  * 
  * 
  * */

/**
 * 列出当前用户最新的十条工作日志
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
public String getDiaryShare(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
  Connection dbConn = null;
  try {
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    int login_user_Id = person.getSeqId();
    String userId=request.getParameter("userId");
    YHDiaryLogic dl = new YHDiaryLogic();
    List<YHDiary> diaryList = this.logic.getDiaryShareLogic(dbConn, login_user_Id,userId);
    StringBuffer data = dl.toJson(dbConn, diaryList);
    
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_DATA, data.toString());
  } catch (Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
  }
  return "/core/inc/rtjson.jsp";
}

/**
 * 日程安排
 * 
 * */
public String selectCalendarByTerm(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
  Connection dbConn = null;
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
  try {
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();

    String userId = request.getParameter("userId");

    YHPerson user = (YHPerson) request.getSession().getAttribute(
        YHConst.LOGIN_USER);
    int login_userId = user.getSeqId();
    YHCalendarLogic tcl = new YHCalendarLogic();
    List<YHCalendar> calendarList =this.logic.selectCalendarByTerm(dbConn, userId);
    List<Map<String, String>> calendarListCopy = new ArrayList<Map<String, String>>();
    Date date = new Date();
    long dateTime = date.getTime();
    long begin = 0;
    long end = 0;
    for (int i = 0; i < calendarList.size(); i++) {
      String status = "0";// 进行中 判断判断状态

      Map<String, String> map = new HashMap<String, String>();
      YHCalendar calendar = calendarList.get(i);
      map.put("seqId", String.valueOf(calendar.getSeqId()));
      map.put("userId", calendar.getUserId());
      map.put("calLevel", calendar.getCalLevel());
      map.put("calType", calendar.getCalType());
      map.put("content", calendar.getContent());
      map.put("managerId", calendar.getManagerId());
      if (calendar.getCalTime() != null) {
        map.put("calTime", dateFormat.format(calendar.getCalTime()));
      } else {
        map.put("calTime", "");
      }
      if (calendar.getEndTime() != null) {
        map.put("endTime", dateFormat.format(calendar.getEndTime()));
      } else {
        map.put("endTime", "");
      }

      map.put("overStatus", calendar.getOverStatus());
      // System.out.println(calendar.getManagerId());
      if (calendar.getManagerId() != null) {
        YHPersonLogic tpl = new YHPersonLogic();
        map.put("managerName", tpl.getNameBySeqIdStr(calendar.getManagerId(),
            dbConn));
      } else {
        map.put("managerName", "");
      }
      String overStatus1 = calendar.getOverStatus();
      if (overStatus1 == null || overStatus1.equals("0")
          || overStatus1.trim().equals("")) {
        begin = calendar.getCalTime().getTime();
        end = calendar.getEndTime().getTime();
        if (dateTime < begin) {
          status = "1";// 未开始

        }
        if (dateTime > end) {
          status = "2";// 超时
        }
      }
      map.put("status", status);
      calendarListCopy.add(map);
    }
    // Map<String,String> map = tcl.selectPersonById(dbConn, userId);
    request.setAttribute("calendarList", calendarListCopy);
    // request.setAttribute("person", map);
  } catch (Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
  }

  return "/core/funcs/userinfo/showcalendar.jsp";
}
public String getUserPrivAct(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
  Connection dbConn = null;
  try {
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
   
    String userPriv=request.getParameter("userPriv");
    
    String data = this.logic.getUserPrivLogic(dbConn, userPriv);
  
     data="{userName:'"+data+"'}";
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
    request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
    }
  return "/core/inc/rtjson.jsp";
}


public String getDeptAct(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
  Connection dbConn = null;
  try {
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
   
    String deptId=request.getParameter("deptId");
    
    String data = this.logic.getDeptNameLogic(dbConn,deptId);
  
     data="{deptName:'"+data+"'}";
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
    request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
    }
  return "/core/inc/rtjson.jsp";
}

public String getAvatarAct(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
  Connection dbConn = null;
  try {
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
   
    String userId=request.getParameter("userId");
    String data = this.logic.getAvatarLogic(dbConn,userId);
  
    if(data==null){
        data="";
    }
    
     data="{hrms_photo:'"+data+"'}";
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
    request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
    }
  return "/core/inc/rtjson.jsp";
}

public String getDeptNoAct(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
  Connection dbConn = null;
  try {
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
   
    String deptId=request.getParameter("deptId");
    String[] data = this.logic.getDeptTelNoLogic(dbConn, deptId);
    if(data[0] == null){
        data[0] ="";
    }
    if(data[1] == null){
      data[1] ="";
    }
    String data1 = "{deptNo:'"+data[0]+"',faxNo:'"+ data[1] +"'}";
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
    request.setAttribute(YHActionKeys.RET_DATA, data1);
    } catch (Exception e) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
    }
  return "/core/inc/rtjson.jsp";
}

public String getOnStatusAct(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
  Connection dbConn = null;
  try {
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
   
    String userId=request.getParameter("userId");
    String data = this.logic.getOnStatusLogic(dbConn,userId);
     
     data="{status:'"+data+"'}";
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
    request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
    }
  return "/core/inc/rtjson.jsp";
}

public String getAuatarExitAct(HttpServletRequest request,
    HttpServletResponse response) throws Exception {

  try {
   
    String photo=request.getParameter("photo");
    String data = this.logic.getAuatarExitLogic(photo);
     
     data="{exit:'"+data+"'}";
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
    request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
    }
  return "/core/inc/rtjson.jsp";
}


public String getUserPrivOtherNameAct(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
  Connection dbConn = null;
  try {
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
   
    String id=request.getParameter("id");
    String data = this.logic.getUserPrivOtherNameLogic(dbConn,id);
  
    if(data==null){
        data="";
    }
   
     data="{name:'"+data+"'}";
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
    request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    throw e;
    }
  return "/core/inc/rtjson.jsp";
}

}
