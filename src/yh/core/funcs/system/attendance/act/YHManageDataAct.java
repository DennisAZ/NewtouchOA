package yh.core.funcs.system.attendance.act;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.attendance.logic.YHManageDataLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHManageDataAct {
  /***
   * 删除数据 根据前台条件(对上下班,出差,请假,外出)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String userIds = request.getParameter("user");
      String duty = request.getParameter("duty");
      String out = request.getParameter("out");
      String leave = request.getParameter("leave");
      String evection = request.getParameter("evection");
      String minTime = request.getParameter("minTime");
      String maxTime = request.getParameter("maxTime");
      YHManageDataLogic dataLogic = new YHManageDataLogic();
      if(duty!=null){
        if(userIds.equals("")){
          dataLogic.deleteDutyDate(dbConn, minTime, maxTime);
        }else{
          dataLogic.deleteDutyDate(dbConn, userIds, minTime, maxTime);
        }
      }
      if(out!=null){
        if(userIds.equals("")){
          dataLogic.deleteOutDate(dbConn, minTime, maxTime);
        }else{
          dataLogic.deleteOutDate(dbConn, userIds, minTime, maxTime);
        }
      }
      if(leave!=null){
        if(userIds.equals("")){
          dataLogic.deleteLeaveDate(dbConn, minTime, maxTime);
        }else{
          dataLogic.deleteLeaveDate(dbConn, userIds, minTime, maxTime);
        }
      }
      if(evection!=null){
        if(userIds.equals("")){
          dataLogic.deleteEvectionDate(dbConn, minTime, maxTime);
        }else{
          dataLogic.deleteEvectionDate(dbConn, userIds, minTime, maxTime);
        }
      }
      /**

       * 保存系统日志

       * @param conn

       * @param type 类型[1登录日志|2登录密码错误|3添加部门|4编辑部门|5删除部门|6添加用户|7编辑用户|8删除用户

       * |9 非法IP登录|10错误用户名|11admin密码清空|12系统资源回收|13考勤数据管理|14修改登录密码|15公告通知管理

       * |16 公共文件柜|17网络硬盘|18软件注册|19用户批量设置|20培训课程管理|21用户KEY验证失败|22退出系统|23员工离职]

       * @param remark 日志说明

       * @param userId 用户Id

       * @param ip IP地址

       * @throws Exception

       */
      InetAddress inet = InetAddress.getLocalHost();
      String ip = inet.getHostAddress();
      yh.core.funcs.system.syslog.logic.YHSysLogLogic.addSysLog(dbConn,"13","删除考勤记录",userId,ip);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public static void main(String[] args) throws UnknownHostException {
    InetAddress inet = InetAddress.getLocalHost();
    //System.out.println("本机的ip=" + inet.getHostAddress());
  }
}
