package yh.core.funcs.system.syslog.logic;

import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.syslog.data.YHSysLog;
import yh.core.util.db.YHORM;

public class YHSysLogLogic {
  /**
   * 保存系统日志
   * 
   * @param conn
   * @param type
   *          类型[1登录日志|2登录密码错误|3添加部门|4编辑部门|5删除部门|6添加用户|7编辑用户|8删除用户 |9
   *          非法IP登录|10错误用户名|11admin密码清空|12系统资源回收|13考勤数据管理|14修改登录密码|15公告通知管理 |16
   *          公共文件柜|17网络硬盘|18软件注册|19用户批量设置|20培训课程管理|21用户KEY验证失败|22退出系统|23员工离职]
   * @param remark
   *          日志说明
   * @param userId
   *          用户Id
   * @param ip
   *          IP地址
   * @throws Exception
   */
  public static void addSysLog(Connection conn, String type, String remark,
    int userId, String ip) throws Exception {
    YHSysLog syslog = new YHSysLog();
    YHORM orm = new YHORM();
    YHPersonLogic logic = new YHPersonLogic();
    try {
      syslog.setRemark(remark);
      syslog.setType(type);  
      syslog.setUserId(userId);
      String userName = logic.getNameBySeqIdStr(userId + "", conn);
      syslog.setUserName(userName);
      syslog.setTime(new Date());
      syslog.setIp(ip);
      orm.saveSingle(conn, syslog);
    } catch (Exception e) {
      throw e;
    }
  }
  
  
  /**
   * 保存系统日志
   * 
   * @param conn
   * @param type
   *          类型[1登录日志|2登录密码错误|3添加部门|4编辑部门|5删除部门|6添加用户|7编辑用户|8删除用户 |9
   *          非法IP登录|10错误用户名|11admin密码清空|12系统资源回收|13考勤数据管理|14修改登录密码|15公告通知管理 |16
   *          公共文件柜|17网络硬盘|18软件注册|19用户批量设置|20培训课程管理|21用户KEY验证失败|22退出系统|23员工离职]
   * @param remark
   *          日志说明
   * @param person
   *          用户对象
   * @param ip
   *          IP地址
   * @throws Exception
   */
  public static void addSysLog(Connection conn, String type, String remark,YHPerson person, String ip) throws Exception {
    YHSysLog syslog = new YHSysLog();
    YHORM orm = new YHORM();
    YHPersonLogic logic = new YHPersonLogic();
    try {
      syslog.setRemark(remark);
      syslog.setType(type);    
      syslog.setUserId(person.getSeqId());
      syslog.setUserName(person.getUserName());
      syslog.setTime(new Date());
      syslog.setIp(ip);
      orm.saveSingle(conn, syslog);
    } catch (Exception e) {
      throw e;
    }
  }


  /**
   * 取得IP地址
   * @param request
   * @return
   */
  public static String getIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
}
