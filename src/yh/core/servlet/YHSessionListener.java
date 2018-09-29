package yh.core.servlet;

import java.sql.Connection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.logic.YHSystemLogic;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHLogConst;
import yh.core.util.db.YHDBUtility;

/**
 * Session监听
 * 
 * @author jpt
 * 
 */
public class YHSessionListener implements HttpSessionListener {
  /**
   * session创建
   */
  public void sessionCreated(HttpSessionEvent event) {
    HttpSession session = event.getSession();
    //向session中添加6位随机数字    Random random = new Random();
    int randomInt = random.nextInt(999999);
    while (randomInt < 100000) {
      randomInt = random.nextInt(999999);
    }
    session.setAttribute("RANDOM_NUMBER", Integer.valueOf(randomInt));
  }

  /**
   * session销毁   */
  public void sessionDestroyed(HttpSessionEvent event) {
    HttpSession session = event.getSession();
    
    //HttpServletRequest request = (HttpServletRequest) session.getAttribute(YHBeanKeys.CURR_REQUEST);
    Boolean isRequest = (Boolean) session.getAttribute(YHBeanKeys.CURR_REQUEST_FLAG);
    
    YHPerson person = (YHPerson) session.getAttribute("LOGIN_USER");

    //当request不为null时,是用户点击注销销毁session
    if (isRequest != null){
     // YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      YHSystemLogic logic = new YHSystemLogic();
      try {
        YHRequestDbConn requestDbConn = new YHRequestDbConn("");
        Connection dbConn = null;
        try {
          dbConn =  requestDbConn.getSysDbConn();
          if (person != null){
            String currRequestAddress = (String) session.getAttribute(YHBeanKeys.CURR_REQUEST_ADDRESS);
            // 系统日志-退出登录
            YHSysLogLogic.addSysLog(dbConn, YHLogConst.LOGOUT, "退出系统", person.getSeqId(), currRequestAddress);
            //以sessionToken作为引索引,删除USER_ONLINE中信息
            logic.deleteOnline(dbConn, String.valueOf(session.getAttribute("sessionToken")));
          }
          dbConn.commit();
        } catch (Exception ex) {
          try {
            dbConn.rollback();
          }catch(Exception ex2) {        
          }
          throw ex;
        } finally {
          requestDbConn.closeAllDbConns();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    else{
      //当request为null时,session是自动过期
      try {
        if (person != null){
          
          //以sessionToken作为引索引,删除USER_ONLINE中信息          String delSql = "delete" +
          " from oa_online" +
          " where SESSION_TOKEN = '" + String.valueOf(session.getAttribute("sessionToken")) + "'";
          YHDBUtility.executeUpdate(delSql);
          
          String sql = "insert into oa_sys_log (USER_ID, TIME, IP, TYPE, REMARK)" +
          " values(%d, %s,'%s','%s','%s')";
          
          String insSql = String.format(sql
              , person.getSeqId()
              , YHDBUtility.currDateTime()
              , session.getAttribute("LOGIN_IP")
              , YHLogConst.LOGOUT
              , "");
          
          YHDBUtility.executeUpdate(insSql);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    //处理在线时长
    try {
      if (person != null) {
        //登陆的秒数
        long time= (new Date().getTime() - person.getLastVisitTime().getTime()) / 1000;
        
        long online = person.getOnLine();
        online += time;
        String sql = "update PERSON" +
        		" set ON_LINE = " + 
        		online +
        		" where SEQ_ID = " + person.getSeqId();
        YHDBUtility.executeUpdate(sql);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    Enumeration<String> en = session.getAttributeNames();
    while (en.hasMoreElements()) {
      session.removeAttribute((String) en.nextElement());
    }
  }
}
