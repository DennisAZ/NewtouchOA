package yh.core.funcs.notify.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.funcs.notify.data.YHNotify;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

/**
 * 公告通知审批
 * 
 * @author qwx110
 * 
 */
public class YHNotifyAuditingLogic {
  private static Logger log = Logger.getLogger(YHNotifyAuditingLogic.class);
  private YHNotifyManageUtilLogic notifyManageUtil = new YHNotifyManageUtilLogic();

  // 待批公告
  public String getUnAuditedList(Connection conn, YHPerson person, String type,
      String ascDesc,
      String field, int showLen, int pageIndex) throws Exception {
    Date currentDate = new Date();
    Statement stmt = null;
    ResultSet rs = null;
    int recordCount = 0;// 总记录数
    int pgStartRecord = 0;// 开始索引
    int pageCount = 0;// 页码数
    int pgEndRecord = 0;// 结束索引
    List<YHNotify> notifyManagerList = new ArrayList<YHNotify>();
    int userSeqId = person.getSeqId();
    StringBuffer sb = new StringBuffer();
    String notifyStatusStr = "";
    sb.append("{");
    sb.append("listData:[");
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    String querySql = null;
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      querySql = "SELECT n.SEQ_ID,n.FROM_ID,n.TO_ID,n.SUBJECT,n.FORMAT,n.[TOP],n.PRIV_ID,n.USER_ID,n.TYPE_ID,n.PUBLISH,"
        + "n.SEND_TIME,n.BEGIN_DATE,n.END_DATE,n.TOP_DAYS,p.USER_NAME,d.DEPT_NAME from oa_notify n,PERSON p,oa_department d where "
        + "n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID and n.PUBLISH='2' and n.AUDITER='"
        + userSeqId + "'";
    }else {
      querySql = "SELECT n.SEQ_ID,n.FROM_ID,n.TO_ID,n.SUBJECT,n.FORMAT,n.TOP,n.PRIV_ID,n.USER_ID,n.TYPE_ID,n.PUBLISH,"
        + "n.SEND_TIME,n.BEGIN_DATE,n.END_DATE,n.TOP_DAYS,p.USER_NAME,d.DEPT_NAME from oa_notify n,PERSON p,oa_department d where "
        + "n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID and n.PUBLISH='2' and n.AUDITER='"
        + userSeqId + "'";
    }
    if ("".equals(ascDesc)) {
      ascDesc = "1";
    }
    if ("".equals(type)) {// 选择“无类型“
      querySql = querySql
          + " and (n.TYPE_ID='' or n.TYPE_ID=' ' or n.TYPE_ID is null)";
    } else if (!"0".equals(type)) {// 选择的不是”所有类型“
      querySql = querySql + " and (n.TYPE_ID='" + type + "')";
    }
    if ("".equals(field)) {
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        querySql = querySql + " order by [TOP] desc,SEND_TIME desc";
      }else {
        querySql = querySql + " order by TOP desc,SEND_TIME desc";
      }
    } else {
      querySql = querySql + " order by " + field;
    }
    if ("1".equals(ascDesc)) {
      querySql = querySql + " desc";
    } else {
      querySql = querySql + " asc";
    }
    // YHOut.println(querySql+"***********");
    try {
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery(querySql);
      rs.last();
      recordCount = rs.getRow(); // 总记录数

      // 总页数
      pageCount = recordCount / showLen;
      if (recordCount % showLen != 0) {
        pageCount++;
      }
      if (pageIndex < 1) {
        pageIndex = 1;
      }
      if (pageIndex > pageCount) {
        pageIndex = pageCount;
      }
      // 开始索引
      pgStartRecord = (pageIndex - 1) * showLen + 1;

      rs.absolute((pageIndex - 1) * showLen + 1);
      for (int i = 0; i < showLen && !rs.isAfterLast() && recordCount > 0; i++) {
        YHNotify notify = new YHNotify();
        String subjectTitle = "";
        String publishDesc = "";
        String notifyStatus = "";
        Statement stmtt = null;
        ResultSet rss = null;
        String typeName = "";
        String toNameTitle = "";
        String toNameStr = "";
        int seqId = rs.getInt("SEQ_ID");
        String fromId = rs.getString("FROM_ID");
        String toId = rs.getString("TO_ID");
        String subject = rs.getString("SUBJECT");
        String format = rs.getString("FORMAT");
        String top = rs.getString("TOP");
        String privId = rs.getString("PRIV_ID");
        String userId = rs.getString("USER_ID");
        String typeId = rs.getString("TYPE_ID");
        String publish = rs.getString("PUBLISH");
        Date sendTime = rs.getTimestamp("SEND_TIME");
        Date beginDate = rs.getDate("BEGIN_DATE");
        Date endDate = rs.getDate("END_DATE");
        String fromName = rs.getString("USER_NAME");
        String deptName = rs.getString("DEPT_NAME");
        int days = rs.getInt("TOP_DAYS");
        if (subject != null && subject.length() > 50) {
          subjectTitle = subject;
          subject = subject.substring(0, 50) + "...";
        }
        if ("0".equals(publish)) {
          publishDesc = "<font color=red>未发布</font>";
        }
        if ("2".equals(publish)) {
          publishDesc = "<font color=blue>待审批</font>";
        }
        if ("3".equals(publish)) {
          publishDesc = "<font color=red>未通过</font><br><a href='#'>审批意见</a>";
        }
        if ("1".equals(publish)) {
          publishDesc = "";
        }

        String endDateStr = "";
        if (!"".equals(endDate) && endDate != null) {
          endDateStr = endDate.toString();
        }
        // 根据新闻类型字段的值，获取新闻类型的代码描述
        if (typeId != null) {
          if (!"".equals(typeId.trim()) && !"null".equals(typeId)) {
            String queryTypeNameStr = "SELECT CLASS_DESC from oa_kind_dict_item where SEQ_ID="
                + typeId;
            stmtt = conn.createStatement();
            rss = stmtt.executeQuery(queryTypeNameStr);
            if (rss.next()) {
              typeName = rss.getString("CLASS_DESC");
            }
          }
        } else {
          typeName = "";
        }

        // 得到发布范围-部门的名称（串）
        String toName = "";
        String queryToNameStr = "";
        if (toId != null && !"".equals(toId)) {
          String[] toIds = toId.split(",");
          toId = "";
          for (int j = 0; j < toIds.length; j++) {
            toId += "" + toIds[j] + "" + ",";
          }
          toId = toId.substring(0, toId.length() - 1);
        }
        if ("0".equals(toId) || "ALL_DEPT".equals(toId)) {
          toName = "全体部门";
        } else {
          queryToNameStr = "select DEPT_NAME from oa_department where SEQ_ID in ("
              + toId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryToNameStr);
          while (rss.next()) {
            toName = toName + rss.getString("DEPT_NAME") + ",";
          }
        }
        // 得到发布范围-角色的名称（串）
        String privName = "";
        String queryPrivNameStr = "";
        if (privId != null && !"".equals(privId)) {
          String[] privIds = privId.split(",");
          privId = "";
          for (int j = 0; j < privIds.length; j++) {
            privId += "'" + privIds[j] + "'" + ",";
          }
          privId = privId.substring(0, privId.length() - 1);
        }
        if (privId != null && !"".equals(privId)) {
          queryPrivNameStr = "select PRIV_NAME from USER_PRIV where SEQ_ID in ("
              + privId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryPrivNameStr);
          while (rss.next()) {
            privName = privName + rss.getString("PRIV_NAME") + ",";
          }
        }
        // 得到发布范围-人员的姓名（串）
        String userName = "";
        String queryUserNameStr = "";
        if (userId != null && !"".equals(userId)) {
          String[] userIds = userId.split(",");
          userId = "";
          for (int j = 0; j < userIds.length; j++) {
            userId += "'" + userIds[j] + "'" + ",";
          }
          userId = userId.substring(0, userId.length() - 1);
        }
        if (userId != null && !"".equals(userId)) {
          queryUserNameStr = "select USER_NAME from PERSON where SEQ_ID in ("
              + userId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryUserNameStr);
          while (rss.next()) {
            userName = userName + rss.getString("USER_NAME") + ",";
          }
        }

        // 设置好部门的名称（串）的显示格式
        if (!"".equals(toName)) {
          toNameTitle = "部门:" + toName;
          if (toName.length() > 20) {
            toName = toName.substring(0, 15) + "...";
          }
          toNameStr = "<font color=#0000FF><b>部门：</b></font>";
          toNameStr += toName;
          toNameStr += "<br>";
        }

        // 设置好角色的名称（串）的显示格式
        String privNameTitle = "";
        String privNameStr = "";
        if (!"".equals(privName)) {
          privNameTitle = "角色：" + privName;
          if (privName.length() > 20) {
            privName = privName.substring(0, 15) + "...";
          }
          privNameStr = "<font color=#0000FF><b>角色：</b></font>";
          privNameStr += privName;
          privNameStr += "<br>";
        }

        // 设置好人员的姓名（串）的显示格式
        String userNameTitle = "";
        String userNameStr = "";
        if (!"".equals(userName)) {
          userNameTitle = "人员：" + userName;
          if (userName.length() > 20) {
            userName = userName.substring(0, 15) + "...";
          }
          userNameStr = "<font color=#0000FF><b>人员：</b></font>";
          userNameStr += userName;
          userNameStr += "<br>";
        }

        if (currentDate.compareTo(beginDate) < 0) {
          notifyStatus = "1";
          notifyStatusStr = "待生效";
        } else {
          notifyStatus = "2";
          if (!"2".equals(publish) && !"3".equals(publish)) {
            notifyStatusStr = "<font color='#00AA00'><b>生效</font>";
          } else {
            if ("2".equals(publish)) {
              notifyStatusStr = "<font color='blue'><b>待审批</font>";
            }
            if ("3".equals(publish)) {
              notifyStatusStr = "<font color='red'><b>未通过</font><br><a href='javascript:my_affair("
                  + seqId + ")'; title='点击查看审批意见'>审批意见</a>";
            }
          }
        }
        if ((!"".equals(endDate) && endDate != null) || "0".equals(publish)) {
          if (currentDate.compareTo(endDate) > 0) {
            notifyStatus = "3";
            notifyStatusStr = "<font color='#FF0000'><b>终止</font>";
          }
        }
        if ("0".equals(publish)) {
          notifyStatusStr = "";
        }
        int dayCnt = 0;
        dayCnt = days;

        if ("1".equals(top)
            && dayCnt != 0
            && YHUtility.getDayAfter(beginDate, dayCnt).getTime() > YHUtility
                .getDayAfter(new Date(), 0).getTime()) {
          subject = "<font color=red><b>置顶:" + subject + "</b></font>";
        } else if ("1".equals(top) && dayCnt == 0) {
          subject = "<font color=red><b>置顶:" + subject + "</b></font>";
        }

        notify.setSeqId(seqId);
        sb.append("{");
        sb.append("seqId:" + seqId);
        sb.append(",fromName:\"" + YHUtility.encodeSpecial(fromName) + "\"");
        sb.append(",typeName:\"" + YHUtility.encodeSpecial(typeName) + "\"");
        sb.append(",toNameTitle:\"" + YHUtility.encodeSpecial(toNameTitle)
            + "\"");
        sb.append(",toNameStr:\"" + YHUtility.encodeSpecial(toNameStr) + "\"");
        sb.append(",privNameTitle:\"" + YHUtility.encodeSpecial(privNameTitle)
            + "\"");
        sb.append(",privNameStr:\"" + YHUtility.encodeSpecial(privNameStr)
            + "\"");
        sb.append(",userNameTitle:\"" + YHUtility.encodeSpecial(userNameTitle)
            + "\"");
        sb.append(",userNameStr:\"" + YHUtility.encodeSpecial(userNameStr)
            + "\"");
        sb.append(",subject:\"" + YHUtility.encodeSpecial(subject) + "\"");
        sb.append(",subjectTitle:\"" + YHUtility.encodeSpecial(subjectTitle)
            + "\"");
        sb.append(",notifyStatus:\"" + YHUtility.encodeSpecial(notifyStatus)
            + "\"");
        sb.append(",notifyStatusStr:\"" + notifyStatusStr + "\"");
        sb.append(",deptName:\"" + YHUtility.encodeSpecial(deptName) + "\"");
        sb.append(",format:\"" + format + "\"");
        sb.append(",top:\"" + top + "\"");
        sb.append(",sendTime:\"" + sendTime + "\"");
        sb.append(",beginDate:\"" + beginDate + "\"");
        sb.append(",endDate:\"" + endDateStr + "\"");
        sb.append("},");

        notifyManagerList.add(notify);
        rs.next();
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // 结束索引
    pgEndRecord = (pageIndex - 1) * showLen + notifyManagerList.size();
    if (notifyManagerList.size() > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    sb.append(",pageData:");
    sb.append("{");
    sb.append("pageCount:" + pageCount);
    sb.append(",recordCount:" + recordCount);
    sb.append(",pgStartRecord:" + pgStartRecord);
    sb.append(",pgEndRecord:" + pgEndRecord);
    sb.append("}");
    sb.append("}");
    // returnMap.put("listData", notifyManagerList);
    // returnMap.put("pageData", sb.toString());
    // YHOut.println(sb.toString());
    return sb.toString();
  }

  // 已批公告
  public String getAuditedList(Connection conn, YHPerson person, String type,
      String ascDesc,
      String field, int showLen, int pageIndex) throws Exception {
    Date currentDate = new Date();
    Statement stmt = null;
    ResultSet rs = null;
    int recordCount = 0;// 总记录数
    int pgStartRecord = 0;// 开始索引
    int pageCount = 0;// 页码数
    int pgEndRecord = 0;// 结束索引
    List<YHNotify> notifyManagerList = new ArrayList<YHNotify>();
    int userSeqId = person.getSeqId();
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    sb.append("listData:[");
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    String querySql = null;
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      querySql = "SELECT n.SEQ_ID,n.FROM_ID,n.TO_ID,n.SUBJECT,n.FORMAT,n.[TOP],n.PRIV_ID,n.USER_ID,n.TYPE_ID,n.PUBLISH,"
        + "n.SEND_TIME,n.BEGIN_DATE,n.END_DATE,p.USER_NAME,d.DEPT_NAME from oa_notify n,PERSON p,oa_department d where "
        + "n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID and n.PUBLISH!='2' and n.AUDITER='"
        + userSeqId + "'";
    }else {
      querySql = "SELECT n.SEQ_ID,n.FROM_ID,n.TO_ID,n.SUBJECT,n.FORMAT,n.TOP,n.PRIV_ID,n.USER_ID,n.TYPE_ID,n.PUBLISH,"
        + "n.SEND_TIME,n.BEGIN_DATE,n.END_DATE,p.USER_NAME,d.DEPT_NAME from oa_notify n,PERSON p,oa_department d where "
        + "n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID and n.PUBLISH!='2' and n.AUDITER='"
        + userSeqId + "'";
    }
    if ("".equals(ascDesc)) {
      ascDesc = "1";
    }
    if ("".equals(type)) {// 选择“无类型“
      querySql = querySql
          + " and (n.TYPE_ID='' or n.TYPE_ID=' ' or n.TYPE_ID is null)";
    } else if (!"0".equals(type)) {// 选择的不是”所有类型“
      querySql = querySql + " and (n.TYPE_ID='" + type + "')";
    }
    if ("".equals(field)) {
      if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
        querySql = querySql + " order by [TOP] desc,SEND_TIME desc";
      }else {
        querySql = querySql + " order by TOP desc,SEND_TIME desc";
      }
    } else {
      querySql = querySql + " order by " + field;
    }
    if ("1".equals(ascDesc)) {
      querySql = querySql + " desc";
    } else {
      querySql = querySql + " asc";
    }

    try {
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery(querySql);
      rs.last();
      recordCount = rs.getRow(); // 总记录数

      // 总页数
      pageCount = recordCount / showLen;
      if (recordCount % showLen != 0) {
        pageCount++;
      }
      if (pageIndex < 1) {
        pageIndex = 1;
      }
      if (pageIndex > pageCount) {
        pageIndex = pageCount;
      }
      // 开始索引
      pgStartRecord = (pageIndex - 1) * showLen + 1;

      rs.absolute((pageIndex - 1) * showLen + 1);
      for (int i = 0; i < showLen && !rs.isAfterLast() && recordCount > 0; i++) {
        YHNotify notify = new YHNotify();
        String subjectTitle = "";
        String publishDesc = "";
        Statement stmtt = null;
        ResultSet rss = null;
        String typeName = "";
        String toNameTitle = "";
        String toNameStr = "";
        int seqId = rs.getInt("SEQ_ID");
        String fromId = rs.getString("FROM_ID");
        String toId = rs.getString("TO_ID");
        String subject = rs.getString("SUBJECT");
        String format = rs.getString("FORMAT");
        String top = rs.getString("TOP");
        String privId = rs.getString("PRIV_ID");
        String userId = rs.getString("USER_ID");
        String typeId = rs.getString("TYPE_ID");
        String publish = rs.getString("PUBLISH");
        Date sendTime = rs.getTimestamp("SEND_TIME");
        Date beginDate = rs.getDate("BEGIN_DATE");
        Date endDate = rs.getDate("END_DATE");
        String fromName = rs.getString("USER_NAME");
        String deptName = rs.getString("DEPT_NAME");

        if (subject.length() > 50) {
          subjectTitle = subject;
          subject = subject.substring(0, 50) + "...";
        }
        if ("0".equals(publish)) {
          publishDesc = "<font color=red>未发布</font>";
        }
        if ("2".equals(publish)) {
          publishDesc = "<font color=blue>待审批</font>";
        }
        if ("3".equals(publish)) {
          publishDesc = "<font color=red>未通过</font><br><a href='#'>审批意见</a>";
        }
        if ("1".equals(publish)) {
          publishDesc = "";
        }

        String endDateStr = "";
        if (!"".equals(endDate) && endDate != null) {
          endDateStr = endDate.toString();
        }
        // 根据新闻类型字段的值，获取新闻类型的代码描述
        if (typeId != null) {
          if (!"".equals(typeId.trim()) && !"null".equals(typeId)) {
            String queryTypeNameStr = "SELECT CLASS_DESC from oa_kind_dict_item where SEQ_ID="
                + typeId;
            stmtt = conn.createStatement();
            rss = stmtt.executeQuery(queryTypeNameStr);
            if (rss.next()) {
              typeName = rss.getString("CLASS_DESC");
            }
          }
        } else {
          typeName = "";
        }

        // 得到发布范围-部门的名称（串）
        String toName = "";
        String queryToNameStr = "";
        if (toId != null && !"".equals(toId)) {
          String[] toIds = toId.split(",");
          toId = "";
          for (int j = 0; j < toIds.length; j++) {
            toId += "" + toIds[j] + "" + ",";
          }
          toId = toId.substring(0, toId.length() - 1);
        }
        if ("0".equals(toId)) {
          toName = "全体部门";
        } else {
          queryToNameStr = "select DEPT_NAME from oa_department where SEQ_ID in ("
              + toId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryToNameStr);
          while (rss.next()) {
            toName = toName + rss.getString("DEPT_NAME") + ",";
          }
        }
        // 得到发布范围-角色的名称（串）
        String privName = "";
        String queryPrivNameStr = "";
        if (privId != null && !"".equals(privId)) {
          String[] privIds = privId.split(",");
          privId = "";
          for (int j = 0; j < privIds.length; j++) {
            privId += "'" + privIds[j] + "'" + ",";
          }
          privId = privId.substring(0, privId.length() - 1);
        }
        if (privId != null && !"".equals(privId)) {
          queryPrivNameStr = "select PRIV_NAME from USER_PRIV where SEQ_ID in ("
              + privId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryPrivNameStr);
          while (rss.next()) {
            privName = privName + rss.getString("PRIV_NAME") + ",";
          }
        }
        // 得到发布范围-人员的姓名（串）
        String userName = "";
        String queryUserNameStr = "";
        if (userId != null && !"".equals(userId)) {
          String[] userIds = userId.split(",");
          userId = "";
          for (int j = 0; j < userIds.length; j++) {
            userId += "'" + userIds[j] + "'" + ",";
          }
          userId = userId.substring(0, userId.length() - 1);
        }
        if (userId != null && !"".equals(userId)) {
          queryUserNameStr = "select USER_NAME from PERSON where SEQ_ID in ("
              + userId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryUserNameStr);
          while (rss.next()) {
            userName = userName + rss.getString("USER_NAME") + ",";
          }
        }

        // 设置好部门的名称（串）的显示格式
        if (!"".equals(toName)) {
          toNameTitle = "部门:" + toName;
          if (toName.length() > 20) {
            toName = toName.substring(0, 15) + "...";
          }
          toNameStr = "<font color=#0000FF><b>部门：</b></font>";
          toNameStr += toName;
          toNameStr += "<br>";
        }

        // 设置好角色的名称（串）的显示格式
        String privNameTitle = "";
        String privNameStr = "";
        if (!"".equals(privName)) {
          privNameTitle = "角色：" + privName;
          if (privName.length() > 20) {
            privName = privName.substring(0, 15) + "...";
          }
          privNameStr = "<font color=#0000FF><b>角色：</b></font>";
          privNameStr += privName;
          privNameStr += "<br>";
        }

        // 设置好人员的姓名（串）的显示格式
        String userNameTitle = "";
        String userNameStr = "";
        if (!"".equals(userName)) {
          userNameTitle = "人员：" + userName;
          if (userName.length() > 20) {
            userName = userName.substring(0, 15) + "...";
          }
          userNameStr = "<font color=#0000FF><b>人员：</b></font>";
          userNameStr += userName;
          userNameStr += "<br>";
        }

        String result = "";
        if ("1".equals(publish)) {
          result = "<font color=blue>批准</font>";
        }
        if ("3".equals(publish)) {
          result = "<font color=red>未批准</font>";
        }
        if ("1".equals(top)) {
          subject = "<font color=red><b>置顶：" + subject + "</b></font>";
        }

        notify.setSeqId(seqId);
        sb.append("{");
        sb.append("seqId:" + seqId);
        sb.append(",fromName:\"" + YHUtility.encodeSpecial(fromName) + "\"");
        sb.append(",typeName:\"" + YHUtility.encodeSpecial(typeName) + "\"");
        sb.append(",toNameTitle:\"" + YHUtility.encodeSpecial(toNameTitle)
            + "\"");
        sb.append(",toNameStr:\"" + YHUtility.encodeSpecial(toNameStr) + "\"");
        sb.append(",privNameTitle:\"" + YHUtility.encodeSpecial(privNameTitle)
            + "\"");
        sb.append(",privNameStr:\"" + YHUtility.encodeSpecial(privNameStr)
            + "\"");
        sb.append(",userNameTitle:\"" + YHUtility.encodeSpecial(userNameTitle)
            + "\"");
        sb.append(",userNameStr:\"" + YHUtility.encodeSpecial(userNameStr)
            + "\"");
        sb.append(",subject:\"" + YHUtility.encodeSpecial(subject) + "\"");
        sb.append(",subjectTitle:\"" + YHUtility.encodeSpecial(subjectTitle)
            + "\"");
        sb.append(",deptName:\"" + YHUtility.encodeSpecial(deptName) + "\"");
        sb.append(",format:\"" + format + "\"");
        sb.append(",top:\"" + top + "\"");
        sb.append(",result:\"" + YHUtility.encodeSpecial(result) + "\"");
        sb.append(",sendTime:\"" + sendTime + "\"");
        sb.append(",beginDate:\"" + beginDate + "\"");
        sb.append(",endDate:\"" + endDateStr + "\"");
        sb.append("},");

        notifyManagerList.add(notify);
        rs.next();
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // 结束索引
    pgEndRecord = (pageIndex - 1) * showLen + notifyManagerList.size();
    if (notifyManagerList.size() > 0) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.append("]");
    sb.append(",pageData:");
    sb.append("{");
    sb.append("pageCount:" + pageCount);
    sb.append(",recordCount:" + recordCount);
    sb.append(",pgStartRecord:" + pgStartRecord);
    sb.append(",pgEndRecord:" + pgEndRecord);
    sb.append("}");
    sb.append("}");
    // returnMap.put("listData", notifyManagerList);
    // returnMap.put("pageData", sb.toString());
    return sb.toString();
  }

  // 进入批准页面之前
  public String beforeAuditingPass(Connection conn, String seqId)
      throws Exception {
    Date currentDate = new Date();
    Statement stmt = null;
    ResultSet rs = null;
    int recordCount = 0;// 总记录数
    int pgStartRecord = 0;// 开始索引
    int pageCount = 0;// 页码数
    int pgEndRecord = 0;// 结束索引
    List<YHNotify> notifyManagerList = new ArrayList<YHNotify>();
    StringBuffer sb = new StringBuffer();
    String notifyStatusStr = "";
    sb.append("{");
    String dbms = YHSysProps.getString(YHSysPropKeys.DBCONN_DBMS);
    String querySql = null;
    if (dbms.equals(YHConst.DBMS_SQLSERVER)) {
      querySql = "SELECT n.SEQ_ID,n.FROM_ID,n.TO_ID,n.SUBJECT,n.FORMAT,n.[TOP],n.PRIV_ID,n.USER_ID,n.TYPE_ID,n.PUBLISH,"
        + "n.SEND_TIME,n.BEGIN_DATE,n.END_DATE,n.TOP_DAYS,p.USER_NAME,d.DEPT_NAME from oa_notify n,PERSON p,oa_department d where "
        + "n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID  and n.SEQ_ID='"
        + seqId + "'";
    }else {
      querySql = "SELECT n.SEQ_ID,n.FROM_ID,n.TO_ID,n.SUBJECT,n.FORMAT,n.TOP,n.PRIV_ID,n.USER_ID,n.TYPE_ID,n.PUBLISH,"
        + "n.SEND_TIME,n.BEGIN_DATE,n.END_DATE,n.TOP_DAYS,p.USER_NAME,d.DEPT_NAME from oa_notify n,PERSON p,oa_department d where "
        + "n.FROM_ID=p.SEQ_ID and p.DEPT_ID=d.SEQ_ID  and n.SEQ_ID='"
        + seqId + "'";
    }
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(querySql);
      if (rs.next()) {
        String subjectTitle = "";
        String publishDesc = "";
        String notifyStatus = "";
        Statement stmtt = null;
        ResultSet rss = null;
        String toNameTitle = "";
        String toNameStr = "";
        String typeName = "";
        String fromId = rs.getString("FROM_ID");
        String toId = rs.getString("TO_ID");
        String subject = rs.getString("SUBJECT");
        String format = rs.getString("FORMAT");
        String top = rs.getString("TOP");
        int topDays = rs.getInt("TOP_DAYS");
        String privId = rs.getString("PRIV_ID");
        String userId = rs.getString("USER_ID");
        String typeId = rs.getString("TYPE_ID");
        String publish = rs.getString("PUBLISH");
        Date sendTime = rs.getTimestamp("SEND_TIME");
        Date beginDate = rs.getDate("BEGIN_DATE");
        Date endDate = rs.getDate("END_DATE");
        String fromName = rs.getString("USER_NAME");
        String deptName = rs.getString("DEPT_NAME");

        if (subject.length() > 50) {
          subjectTitle = subject;
          subject = subject.substring(0, 50) + "...";
        }
        if ("0".equals(publish)) {
          publishDesc = "<font color=red>未发布</font>";
        }
        if ("2".equals(publish)) {
          publishDesc = "<font color=blue>待审批</font>";
        }
        if ("3".equals(publish)) {
          publishDesc = "<font color=red>未通过</font><br><a href='#'>审批意见</a>";
        }
        if ("1".equals(publish)) {
          publishDesc = "";
        }

        String endDateStr = "";
        if (!"".equals(endDate) && endDate != null) {
          endDateStr = endDate.toString();
        }
        // 根据新闻类型字段的值，获取新闻类型的代码描述
        if (typeId != null) {
          if (!"".equals(typeId.trim()) && !"null".equals(typeId)) {
            String queryTypeNameStr = "SELECT CLASS_DESC from oa_kind_dict_item where SEQ_ID="
                + typeId;
            stmtt = conn.createStatement();
            rss = stmtt.executeQuery(queryTypeNameStr);
            if (rss.next()) {
              typeName = rss.getString("CLASS_DESC");
            }
          }
        } else {
          typeName = "";
        }

        // 得到发布范围-部门的名称（串）
        String toName = "";
        String queryToNameStr = "";
        if (toId != null && !"".equals(toId)) {
          String[] toIds = toId.split(",");
          toId = "";
          for (int j = 0; j < toIds.length; j++) {
            toId += "" + toIds[j] + "" + ",";
          }
          toId = toId.substring(0, toId.length() - 1);
        }
        if ("0".equals(toId)) {
          toName = "全体部门";
        } else {
          queryToNameStr = "select DEPT_NAME from oa_department where SEQ_ID in ("
              + toId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryToNameStr);
          while (rss.next()) {
            toName = toName + rss.getString("DEPT_NAME") + ",";
          }
        }
        // 得到发布范围-角色的名称（串）
        String privName = "";
        String queryPrivNameStr = "";
        if (privId != null && !"".equals(privId)) {
          String[] privIds = privId.split(",");
          privId = "";
          for (int j = 0; j < privIds.length; j++) {
            privId += "" + privIds[j] + "" + ",";
          }
          privId = privId.substring(0, privId.length() - 1);
        }
        if (privId != null && !"".equals(privId)) {
          queryPrivNameStr = "select PRIV_NAME from USER_PRIV where SEQ_ID in ("
              + privId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryPrivNameStr);
          while (rss.next()) {
            privName = privName + rss.getString("PRIV_NAME") + ",";
          }
        }
        // 得到发布范围-人员的姓名（串）
        String userName = "";
        String queryUserNameStr = "";
        if (userId != null && !"".equals(userId)) {
          String[] userIds = userId.split(",");
          userId = "";
          for (int j = 0; j < userIds.length; j++) {
            userId += "" + userIds[j] + "" + ",";
          }
          userId = userId.substring(0, userId.length() - 1);
        }
        if (userId != null && !"".equals(userId)) {
          queryUserNameStr = "select USER_NAME from PERSON where SEQ_ID in ("
              + userId + ")";
          stmtt = conn.createStatement();
          rss = stmtt.executeQuery(queryUserNameStr);
          while (rss.next()) {
            userName = userName + rss.getString("USER_NAME") + ",";
          }
        }

        // 设置好部门的名称（串）的显示格式
        if (!"".equals(toName)) {
          toNameTitle = "部门:" + toName;
          if (toName.length() > 20) {
            toName = toName.substring(0, 15) + "...";
          }
          toNameStr = "<font color=#0000FF><b>部门：</b></font>";
          toNameStr += toName;
          toNameStr += "<br>";
        }

        // 设置好角色的名称（串）的显示格式
        String privNameTitle = "";
        String privNameStr = "";
        if (!"".equals(privName)) {
          privNameTitle = "角色：" + privName;
          if (privName.length() > 20) {
            privName = privName.substring(0, 15) + "...";
          }
          privNameStr = "<font color=#0000FF><b>角色：</b></font>";
          privNameStr += privName;
          privNameStr += "<br>";
        }

        // 设置好人员的姓名（串）的显示格式
        String userNameTitle = "";
        String userNameStr = "";
        if (!"".equals(userName)) {
          userNameTitle = "人员：" + userName;
          if (userName.length() > 20) {
            userName = userName.substring(0, 15) + "...";
          }
          userNameStr = "<font color=#0000FF><b>人员：</b></font>";
          userNameStr += userName;
          userNameStr += "<br>";
        }

        if (currentDate.compareTo(beginDate) < 0) {
          notifyStatus = "1";
          notifyStatusStr = "待生效";
        } else {
          notifyStatus = "2";
          if (!"2".equals(publish) && !"3".equals(publish)) {
            notifyStatusStr = "<font color='#00AA00'><b>生效</font>";
          } else {
            if ("2".equals(publish)) {
              notifyStatusStr = "<font color='blue'><b>待审批</font>";
            }
            if ("3".equals(publish)) {
              notifyStatusStr = "<font color='red'><b>未通过</font><br><a href='javascript:my_affair("
                  + seqId + ")'; title='点击查看审批意见'>审批意见</a>";
            }
          }
        }
        if ((!"".equals(endDate) && endDate != null) || "0".equals(publish)) {
          if (currentDate.compareTo(endDate) > 0) {
            notifyStatus = "3";
            notifyStatusStr = "<font color='#FF0000'><b>终止</font>";
          }
        }
        if ("0".equals(publish)) {
          notifyStatusStr = "";
        }
        if ("1".equals(top)) {
          subject = "<font color=red><b>" + subject + "</b></font>";
        }
        sb.append("seqId:" + seqId);
        sb.append(",fromName:\"" + YHUtility.encodeSpecial(fromName) + "\"");
        sb.append(",typeName:\"" + YHUtility.encodeSpecial(typeName) + "\"");
        sb.append(",toNameTitle:\"" + YHUtility.encodeSpecial(toNameTitle)
            + "\"");
        sb.append(",toNameStr:\"" + YHUtility.encodeSpecial(toNameStr) + "\"");
        sb.append(",privNameTitle:\"" + YHUtility.encodeSpecial(privNameTitle)
            + "\"");
        sb.append(",privNameStr:\"" + YHUtility.encodeSpecial(privNameStr)
            + "\"");
        sb.append(",userNameTitle:\"" + YHUtility.encodeSpecial(userNameTitle)
            + "\"");
        sb.append(",userNameStr:\"" + YHUtility.encodeSpecial(userNameStr)
            + "\"");
        sb.append(",subject:\"" + YHUtility.encodeSpecial(subject) + "\"");
        sb.append(",subjectTitle:\"" + YHUtility.encodeSpecial(subjectTitle)
            + "\"");
        sb.append(",notifyStatus:\"" + YHUtility.encodeSpecial(notifyStatus)
            + "\"");
        sb.append(",notifyStatusStr:\""
            + YHUtility.encodeSpecial(notifyStatusStr) + "\"");
        sb.append(",deptName:\"" + YHUtility.encodeSpecial(deptName) + "\"");
        sb.append(",format:\"" + format + "\"");
        sb.append(",top:\"" + top + "\"");
        sb.append(",topDays:\"" + topDays + "\"");
        sb.append(",sendTime:\"" + sendTime + "\"");
        sb.append(",beginDate:\"" + beginDate + "\"");
        sb.append(",endDate:\"" + endDateStr + "\"");
        // YHOut.println(sb.toString()+"%%%%%%%%%%%%%%%%%%");
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    sb.append("}");
    // returnMap.put("listData", notifyManagerList);
    // returnMap.put("pageData", sb.toString());
    return sb.toString();
  }

  // 批准或不批准进入的方法
  public boolean operation(Connection dbConn, YHPerson loginUser,
      YHNotify notify, String operation, String mailRemind) throws Exception {
    ResultSet rs = null;
    Statement st = null;
    YHORM orm = new YHORM();
    String changeStateSql = "";
    boolean success = false;
    // Date currentDate = new Date();
    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // String currentDateStr = sdf.format(currentDate);
    String top = "";
    if ("0".equals(operation)) {// 0--不批准1--批准
      changeStateSql = "update oa_notify set PUBLISH='3',AUDITER='"
          + loginUser.getSeqId() +
          "',AUDIT_DATE=" + YHDBUtility.currDateTime() + ",REASON='"
          + YHUtility.encodeLike(notify.getReason()) +
          "' where SEQ_ID='" + notify.getSeqId() + "'";
      try {
        st = dbConn.createStatement();
        success = st.execute(changeStateSql);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        YHDBUtility.close(st, rs, log);
      }
      YHSmsBack smsBack = new YHSmsBack();
      String content = "您提交的公告通知，标题：" + notify.getSubject() + "审批未通过，原因是："
          + notify.getReason();
      String remindUrl = "/core/funcs/notify/manage/index.jsp?seqId="
          + notify.getSeqId() + "&openFlag=0";
      smsBack.setContent(content);
      smsBack.setFromId(loginUser.getSeqId());
      smsBack.setSmsType("1");
      smsBack.setToId(notify.getFromId());
      smsBack.setRemindUrl(remindUrl);
      YHSmsUtil.smsBack(dbConn, smsBack);
    } else {
      if ("on".equals(notify.getTop())) {
        top = "1";
      } else {
        top = "0";
      }
      notify.setPublish("1");
      notify.setTop(top);
      notify.setAuditer(Integer.toString(loginUser.getSeqId()));
      notify.setAuditDate(new Date());
      try {
        orm.updateSingle(dbConn, notify);// 批准，（update）
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        YHDBUtility.close(st, rs, log);
      }
      // changeStateSql =
      // "update NOTIFY set PUBLISH='1',AUDITER='"+loginUser.getSeqId()
      // +"',AUDIT_DATE=sysdate,TOP='"+top+"',TOP_DAYS='"+notify.getTopDays()
      // +"',SEND_TIME=to_date('"+sdf.format(notify.getSendTime())+"','yyyy-mm-dd'),END_DATE=to_date('"+sdf.format(notify.getEndDate())
      // +"','yyyy-mm-dd'),BEGIN_DATE=to_date('"+sdf.format(notify.getBeginDate())+"','yyyy-mm-dd') where SEQ_ID='"+notify.getSeqId()+"'";

      // ///////////////////////////////一下为发送内部短信////////////////////////////////////////
      YHSmsBack smsBack = new YHSmsBack();
      String queryFWStr = "";
      String toIdFW = "";
      if ("0".equals(notify.getToId()) || "ALL_DEPT".equals(notify.getToId())) {// 如果是全体部门
        queryFWStr = "select SEQ_ID from PERSON where NOT_LOGIN!='1'";
      } else {
        queryFWStr = "select SEQ_ID from PERSON where NOT_LOGIN!='1'";
        String toId = notify.getToId();
        if (toId != null && !"".equals(toId.trim())) {
          String[] toIds = toId.split(",");
          toId = "";
          for (int j = 0; j < toIds.length; j++) {
            toId += toIds[j] + ",";
          }
          toId = toId.substring(0, toId.length() - 1);
        }
        if (!"".equals(toId) && toId != null) {
          queryFWStr = queryFWStr + " and (DEPT_ID in (" + toId + ")";
        }
        String privId = notify.getPrivId();
        if (privId != null && !"".equals(privId.trim())) {
          String[] privIds = privId.split(",");
          privId = "";
          for (int j = 0; j < privIds.length; j++) {
            privId += privIds[j] + ",";
          }
          privId = privId.substring(0, privId.length() - 1);
        }
        if (!"".equals(privId) && privId != null) {
          queryFWStr = queryFWStr + " or USER_PRIV in (" + privId + ")";
        }
        String userId = notify.getUserId();
        if (userId != null && !"".equals(userId.trim())) {
          String[] userIds = userId.split(",");
          userId = "";
          for (int j = 0; j < userIds.length; j++) {
            userId += userIds[j] + ",";
          }
          userId = userId.substring(0, userId.length() - 1);
        }
        if (!"".equals(userId) && userId != null) {
          queryFWStr = queryFWStr + " or SEQ_ID in (" + userId + ")";
        }
        queryFWStr = queryFWStr + ")";
      }
      Statement stmtFW = null;
      ResultSet rsFW = null;
      try {
        stmtFW = dbConn.createStatement();
        rsFW = stmtFW.executeQuery(queryFWStr);
        while (rsFW.next()) {
          toIdFW = toIdFW + rsFW.getString("SEQ_ID") + ",";// 得到范围内的人员id串
        }
      } catch (Exception e) {
        // TODO Auto-generated catch block
        throw e;
      } finally {
        YHDBUtility.close(stmtFW, rsFW, log);
      }
      // 判duan是否要提醒
      if ("on".equalsIgnoreCase(mailRemind)) {
        String content = "您提交的公告通知，标题：" + notify.getSubject() + "审批已通过。";
        String remindUrl = "/core/funcs/notify/show/readNotify.jsp?seqId="
            + notify.getSeqId() + "&openFlag=1";
        smsBack.setContent(content);
        smsBack.setFromId(loginUser.getSeqId());
        smsBack.setSmsType("1");
        smsBack.setToId(notify.getFromId());
        smsBack.setRemindUrl(remindUrl);
        if (!"".equals(toIdFW.trim()) && toIdFW != null
            && toIdFW.contains(",") == true) {
          YHSmsUtil.smsBack(dbConn, smsBack);
        }
      }
      // ------------提醒所有在发布范围内的人-------------
      if ("on".equalsIgnoreCase(mailRemind)) {
        YHSmsBack smsBack2 = new YHSmsBack();
        String content = "请查看公告通知！\n标题：" + notify.getSubject();
        String remindUrl = "/core/funcs/notify/show/readNotify.jsp?seqId="
            + notify.getSeqId() + "&openFlag=1";
        smsBack2.setContent(content);
        smsBack2.setFromId(loginUser.getSeqId());
        smsBack2.setRemindUrl(remindUrl);
        smsBack2.setSmsType("1");
        smsBack2.setToId(toIdFW);
        if (!"".equals(toIdFW.trim()) && toIdFW != null
            && toIdFW.contains(",") == true) {
          YHSmsUtil.smsBack(dbConn, smsBack2);
        }
      }
    }
    return success;
  }

}
