package yh.subsys.jtgwjh.task.logic;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.funcs.calendar.logic.YHCalendarLogic;
import yh.core.load.YHPageLoader;
import yh.core.util.YHReflectUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.task.data.YHJhTaskLog;

public class YHJhTaskLogLogic {
  private static Logger log = Logger
      .getLogger("yh.subsys.jtgwjh.task.logic.YHJhTaskLogLogic");

  /**
   * 新建
   * 
   * @param dbConn
   * @param code
   * @return
   * @throws Exception
   */
  public static int add(Connection dbConn, YHJhTaskLog code)
      throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, code);
    return YHCalendarLogic.getMaSeqId(dbConn, "JH_TASK_LOG");
  }

  /**
   * 更新
   * 
   * @param dbConn
   * @param code
   * @return
   * @throws Exception
   */
  public static void updateNation(Connection dbConn, YHJhTaskLog nation)
      throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, nation);
  }

  /**
   * 查询ById
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static YHJhTaskLog getById(Connection dbConn, String seqId)
      throws Exception {
    YHORM orm = new YHORM();
    YHJhTaskLog code = (YHJhTaskLog) orm.loadObjSingle(dbConn,
        YHJhTaskLog.class, Integer.parseInt(seqId));
    return code;
  }

  /**
   * 条件查询
   * 
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static List<YHJhTaskLog> select(Connection dbConn, String[] str)
      throws Exception {
    YHORM orm = new YHORM();
    List<YHJhTaskLog> codeList = new ArrayList<YHJhTaskLog>();
    codeList = orm.loadListSingle(dbConn, YHJhTaskLog.class, str);
    return codeList;
  }

  /**
   * 删除ById
   * 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void delById(Connection dbConn, String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHJhTaskLog.class, Integer.parseInt(seqId));
  }

  /**
   * 根据用户得到分页列表---管理
   * 
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public static String toSearchData(Connection conn, Map request,
      HttpServletRequest request1) throws Exception {
    String status = request1.getParameter("status") == null ? "" : request1
        .getParameter("status");

    String sql = "select seq_id,doc_title,doc_no,doc_type,send_dept_name,send_datetime,return_reason,main_doc_id,main_doc_name from jh_docrecv_info ";
    sql = sql + " where status ='" + status + "' ";
    String docTitle = request1.getParameter("docTitle");
    String docNo = request1.getParameter("docNo");
    String sendDate = request1.getParameter("sendDate");
    String sendDate2 = request1.getParameter("sendDate2");
    if (!YHUtility.isNullorEmpty(docTitle)) {
      docTitle = docTitle.replace("'", "''");
      sql = sql + " and DOC_TITLE like '%" + YHDBUtility.escapeLike(docTitle)
          + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(docNo)) {
      docNo = docNo.replace("'", "''");
      sql = sql + " and DOC_NO like '%" + YHDBUtility.escapeLike(docNo) + "%' "
          + YHDBUtility.escapeLike();
    }

    if (!YHUtility.isNullorEmpty(sendDate)) {
      sql = sql + " and "
          + YHDBUtility.getDateFilter("SEND_DATETIME", sendDate, ">=");
    }
    if (!YHUtility.isNullorEmpty(sendDate2)) {
      sql = sql
          + " and "
          + YHDBUtility.getDateFilter("SEND_DATETIME", sendDate2 + " 23:59:59",
              "<=");
    }
    sql = sql + " order by send_datetime desc";

    // 下面是判断是否登录人部门是计划的接收部门，且团组号也要在这条计划中

    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn, queryParam,
        sql);
    return pageDataList.toJson();
  }

  /**
   * 根据任务的GUID查询是否存在
   * 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static boolean checkTask(Connection dbConn, String guId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    if (YHUtility.isNullorEmpty(guId)) {
      guId = "";
    }
    String sql = "SELECT * from JH_TASK_LOG where guId='" + guId+ "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return true;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return false;
  }
  
  
  /**
   * 转换成XMl对象字符串

   * @param obj
   * @return
   * @throws Exception
   */
  public static StringBuffer toXML(Object obj) throws Exception {
    if (obj == null) {
      return new StringBuffer("");
    }
    StringBuffer rtBuf = new StringBuffer("");
    Class cls = obj.getClass();
    Field[] fieldArray = cls.getDeclaredFields();
    int length = fieldArray.length;
    for (int i = 0; i < length; i++) {
      Field field = fieldArray[i];
      Class fieldType = field.getType(); 
      String fieldName = field.getName();
      rtBuf.append("<"+fieldName+">");
     // rtBuf.append("");
      Object value = null;
        if (int.class.equals(fieldType)
            || Integer.class.equals(fieldType)
            || double.class.equals(fieldType)
            || Double.class.equals(fieldType)) {
          value = YHReflectUtility.getValue(obj, fieldName);
        }else if (Date.class.equals(fieldType)) {
          if((Date)YHReflectUtility.getValue(obj, fieldName)!=null){
            value = YHUtility.getDateTimeStr((Date)YHReflectUtility.getValue(obj, fieldName)) ;
          }
        }else {
          Object valueObj = YHReflectUtility.getValue(obj, fieldName);
          if (valueObj == null) {
            value = "";
          }else {
            String tmpStr = YHUtility.null2Empty(valueObj.toString());

            value =  tmpStr ;
          }
        }
      rtBuf.append(value);
      rtBuf.append("</"+fieldName+">");
    }
    return rtBuf;
  }
  
  /**
   * 获取同步信息
   * 
   * @param dbConn
   * @param type:类型 1：外部组织机构数 2：公章
   * count：获取最近N条数据
   * @return 
   * @throws Exception
   */
  public static List<YHJhTaskLog> getSynTaskInfo(Connection dbConn, String type ,int count) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String  sql = "select * from JH_TASK_LOG where type='" + type + "' group by OPT_TIME order by OPT_TIME desc"; 
    List<YHJhTaskLog>  list = new  ArrayList<YHJhTaskLog>();
    try {
      stmt = dbConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery(sql);
      //总记录数
      rs.last();
      int recordCnt = rs.getRow();
      rs.beforeFirst();
       if (recordCnt > 0) {
         for (int i = 0; i < recordCnt; i++) {
           rs.next();
           YHJhTaskLog taskLog = new YHJhTaskLog();
           taskLog.setSeqId(rs.getInt("seq_id"));
           taskLog.setFromDept(rs.getString("FROM_DEPT"));
           taskLog.setFromDeptName(rs.getString("FROM_DEPT_NAME"));
           taskLog.setGuid(rs.getString("GUID"));
           taskLog.setUserId(rs.getString("user_id"));
           taskLog.setUserName(rs.getString("user_name"));
           taskLog.setToDept(rs.getString("to_dept"));
           taskLog.setToDeptName(rs.getString("to_dept_name"));
           taskLog.setStatus(rs.getString("status"));
           taskLog.setOptTime(rs.getTimestamp("opt_time"));
           taskLog.setType(rs.getString("type"));
           list.add(taskLog);

           if(i>=count){
             break;
           }
         }
       }
      } catch (SQLException e) {
        throw e;
      } finally {
        YHDBUtility.close(stmt, rs, log);
      }
    return list;
  }

  /**
   * 
   * 根据guid获取数量
   * @param dbConn
   * @param item
   * @return status：接收状态
   * @throws Exception
   */
  public String  getCountByGuid(Connection dbConn, String guId,String optTime,String status) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    if (YHUtility.isNullorEmpty(guId)) {
      guId = "";
    }
    String sql = "SELECT count(seq_id) from JH_TASK_LOG where guid='" +  guId + "'";
    
    if(!YHUtility.isNullorEmpty(optTime)){
       sql = "SELECT count(seq_id) from JH_TASK_LOG where " +  YHDBUtility.getDateFilter("OPT_TIME", optTime, "=");
      
    }
        if(!YHUtility.isNullorEmpty(status)){
          sql = sql + " and STATUS ='" + status + "'";
        }
        

    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return rs.getString(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return "0";
  }

  
  
  /**
   * 
   * 
   * @param dbConn
   * @param guid : 任务唯一标识
   * @return state：状态
   *      to:接收部门，client
   * @throws Exception
   */
  public static String updateSealStatus(Connection dbConn, String guid, int state, String to) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    if (YHUtility.isNullorEmpty(guid)) {
      guid = "";
    }
    if(checkTask(dbConn, guid)){//判断是否存在
      String sql = "update JH_TASK_LOG set STATUS = '" + state + "' where guid='" + guid+ "'";
      int Count = 0;
      if(!YHUtility.isNullorEmpty(to)){//如果是接收返回状态
        YHDeptTreeLogic dtl = new YHDeptTreeLogic();
        YHExtDept toDeptObj = dtl.getDeptByEsbUser(dbConn,to);//根据接收部门，查询外部组织机构
        String extDeptId = "";
        if(toDeptObj != null){
          extDeptId = toDeptObj.getDeptId()+"";
        }
         sql = "update JH_TASK_LOG set STATUS = '" + state + "' where guid='" + guid+ "' and to_dept='" + extDeptId  + "'"  ;
      }
      try {
        stmt = dbConn.createStatement();
        Count = stmt.executeUpdate(sql);
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stmt, rs, log);
      }
      return Count + "";
    }else{//不存在直接返回null
      return null;
    }
    
  }
  
}
