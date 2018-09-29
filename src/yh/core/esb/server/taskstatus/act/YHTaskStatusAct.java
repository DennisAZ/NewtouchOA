package yh.core.esb.server.taskstatus.act;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
public class YHTaskStatusAct {
  
  public String getUploadPage(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 
    String queryType = request.getParameter("queryType");
    String startTime =  YHUtility.null2Empty(request.getParameter("startTime"));
    String endTime =  YHUtility.null2Empty(request.getParameter("endTime"));
    String startNo = YHUtility.null2Empty(request.getParameter("startNo"));
    String endNo = YHUtility.null2Empty(request.getParameter("endNo"));
    
    
    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      
      String sql = "select" +
          " t.SEQ_ID," +
          " t.GUID," +
          " t.FROM_ID," +
          " t.FILE_PATH," +
          " t.TO_ID," +
          " t.STATUS," +
          " t.FAILED_MESSAGE," +
          " t.CREATE_TIME," +
          " t.COMPLETE_TIME" +
          " , 1 " + 
          " from ESB_TRANSFER t" +
          " where TYPE = '0' ";
      Date date  = new Date();
      if (YHUtility.isNullorEmpty(queryType) || "0".equals(queryType)) {
         
         String dateStr = YHUtility.getDateTimeStr(date).split(" ")[0];
         startTime =   dateStr +   " 00:00:00";
         endTime =  dateStr + " 23:59:59";
         //确定日期时间
      } else if ("1".equals(queryType))  {
         
        //三天
      } else if ("2".equals(queryType)) {
        long time = date.getTime();
        long time2 = time - 24 * 60 * 60 * 1000 * 3;
        Date d = new Date(time2);
        String dateStr = YHUtility.getDateTimeStr(d).split(" ")[0];
        startTime = dateStr  +   " 00:00:00";
        //一周时间
      } else if ("3".equals(queryType)) {
        long time = date.getTime();
        int day = date.getDay();
        if (day == 0 ) {
          day = 7;
        }
        long time2 = time - 24 * 60 * 60 * 1000 * (day - 1);
        Date d = new Date(time2);
        String dateStr = YHUtility.getDateTimeStr(d).split(" ")[0];
        startTime = dateStr  +   " 00:00:00";
        //一月
      } else if ("4".equals(queryType)) {
        String dateStr = YHUtility.getDateTimeStr(date).split(" ")[0];
        int last = dateStr.lastIndexOf("-");
        dateStr = dateStr.substring(0 , last);
        startTime = dateStr  +   "-01 00:00:00";
      }
      if (!YHUtility.isNullorEmpty(startTime)) {
        String dbDateF1 = YHDBUtility.getDateFilter("t.CREATE_TIME", startTime, " >= ");
        sql += " and " + dbDateF1;
      }
      if (!YHUtility.isNullorEmpty(endTime)) {
        String dbDateF2 = YHDBUtility.getDateFilter("t.CREATE_TIME", endTime, " <= ");
        sql += " and " + dbDateF2;
      }
      
      if (!YHUtility.isNullorEmpty(startNo) && YHUtility.isInteger(startNo)) {
        sql += " and t.SEQ_ID >= " + startNo ;
      }
      if (!YHUtility.isNullorEmpty(endNo) && YHUtility.isInteger(endNo)) {
        sql += " and t.SEQ_ID <= " + endNo ;
      }
      
      sql += " order by t.SEQ_ID desc ";
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, 
      queryParam, 
      sql);
      for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
        YHDbRecord record = pageDataList.getRecord(i);
        Date d1 = (Date)record.getValueByName("createTime");
        Date d2 = (Date)record.getValueByName("completetime");
        long dtime1 = 0 ;
        long dtime2 = 0 ;
        long timeUsed = 0;
        String ss = "";
        if (d1 != null) {
          dtime1 = d1.getTime();
          if (d2 != null) {
            dtime2 = d2.getTime();
            timeUsed = (dtime2 - dtime1);
            
            long day = timeUsed/(24*60*60*1000); 
            long hour = (timeUsed/(60*60*1000)-day*24); 
            long min = ((timeUsed/(60*1000))-day*24*60-hour*60); 
            long s = (timeUsed/1000-day*24*60*60-hour*60*60-min*60);
            if(day > 0){
              ss = day + "天";
            }
            if(hour>0){
              ss +=hour + "时";
            }
            if(min>0){
              ss +=min + "分";
            }
            if(s>0){
              ss +=s + "秒";
            }
          }
        } 
        record.updateField("time", ss);
      }
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 
  
      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
  
  public String getDownloadPage(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 
    String queryType = request.getParameter("queryType");
    String startTime = YHUtility.null2Empty(request.getParameter("startTime"));
    String endTime = YHUtility.null2Empty(request.getParameter("endTime"));
    String startNo = YHUtility.null2Empty(request.getParameter("startNo"));
    String endNo = YHUtility.null2Empty(request.getParameter("endNo"));
    
    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      String sql = "select" +
          " t.SEQ_ID," +
          " t.GUID," +
          " t.FROM_ID," +
      		" t.FILE_PATH," +
      		" s.TO_ID," +
      		" s.STATUS," +
      		" s.FAILED_MESSAGE," +
      		" s.CREATE_TIME," +
      		" s.COMPLETE_TIME" +
      		" , 1 " + 
      		" from ESB_TRANSFER_STATUS s" +
      		" join ESB_TRANSFER t on s.TRANS_ID = t.GUID" +
      		" where TYPE = '0'" ;
      
      Date date  = new Date();
      if (YHUtility.isNullorEmpty(queryType) || "0".equals(queryType)) {
         
         String dateStr = YHUtility.getDateTimeStr(date).split(" ")[0];
         startTime =   dateStr +   " 00:00:00";
         endTime =  dateStr + " 23:59:59";
         
         String dbDateF1 = YHDBUtility.getDateFilter("t.CREATE_TIME", startTime, " >= ");
         sql += " and (" + dbDateF1 + " or t.CREATE_TIME IS NULL) ";
         String dbDateF2 = YHDBUtility.getDateFilter("t.CREATE_TIME", endTime, " <= ");
         sql += " and (" + dbDateF2 + "  or t.CREATE_TIME IS NULL) ";
         //确定日期时间
      } else {
        if ("1".equals(queryType))  {
          
          //三天
        } else if ("2".equals(queryType)) {
          long time = date.getTime();
          long time2 = time - 24 * 60 * 60 * 1000 * 3;
          Date d = new Date(time2);
          String dateStr = YHUtility.getDateTimeStr(d).split(" ")[0];
          startTime = dateStr  +   " 00:00:00";
          //一周时间
        } else if ("3".equals(queryType)) {
          long time = date.getTime();
          int day = date.getDay();
          if (day == 0 ) {
            day = 7;
          }
          long time2 = time - 24 * 60 * 60 * 1000 * (day - 1);
          Date d = new Date(time2);
          String dateStr = YHUtility.getDateTimeStr(d).split(" ")[0];
          startTime = dateStr  +   " 00:00:00";
          //一月
        } else if ("4".equals(queryType)) {
          String dateStr = YHUtility.getDateTimeStr(date).split(" ")[0];
          int last = dateStr.lastIndexOf("-");
          dateStr = dateStr.substring(0 , last);
          startTime = dateStr  +   "-01 00:00:00";
        }
        if (!YHUtility.isNullorEmpty(startTime)) {
          String dbDateF1 = YHDBUtility.getDateFilter("t.CREATE_TIME", startTime, " >= ");
          sql += " and " + dbDateF1;
        }
        if (!YHUtility.isNullorEmpty(endTime)) {
          String dbDateF2 = YHDBUtility.getDateFilter("t.CREATE_TIME", endTime, " <= ");
          sql += " and " + dbDateF2;
        }
      }
      
      if (!YHUtility.isNullorEmpty(startNo) && YHUtility.isInteger(startNo)) {
        sql += " and t.SEQ_ID >= " + startNo ;
      }
      if (!YHUtility.isNullorEmpty(endNo) && YHUtility.isInteger(endNo)) {
        sql += " and t.SEQ_ID <= " + endNo ;
      }
      
      sql += " order by t.SEQ_ID desc ";
      
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, 
      queryParam, 
      sql);
      for (int i = 0 ;i < pageDataList.getRecordCnt() ; i++) {
        YHDbRecord record = pageDataList.getRecord(i);
        Date d1 = (Date)record.getValueByName("createTime");
        Date d2 = (Date)record.getValueByName("completetime");
        long dtime1 = 0 ;
        long dtime2 = 0 ;
        long timeUsed = 0;
        String ss = "";
        if (d1 != null) {
          dtime1 = d1.getTime();
          if (d2 != null) {
            dtime2 = d2.getTime();
            timeUsed = (dtime2 - dtime1);
            
            long day = timeUsed/(24*60*60*1000); 
            long hour = (timeUsed/(60*60*1000)-day*24); 
            long min = ((timeUsed/(60*1000))-day*24*60-hour*60); 
            long s = (timeUsed/1000-day*24*60*60-hour*60*60-min*60);
            if(day > 0){
              ss = day + "天";
            }
            if(hour>0){
              ss +=hour + "时";
            }
            if(min>0){
              ss +=min + "分";
            }
            if(s>0){
              ss +=s + "秒";
            }
          }
        } 
        record.updateField("time", ss);
      }
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 
  
      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
  public String expDown(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 
    response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
    String queryType = request.getParameter("queryType");
    String startTime = YHUtility.null2Empty(request.getParameter("startTime"));
    String endTime = YHUtility.null2Empty(request.getParameter("endTime"));
    String startNo = YHUtility.null2Empty(request.getParameter("startNo"));
    String endNo = YHUtility.null2Empty(request.getParameter("endNo"));
    
    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      String sql = "select" +
          " t.SEQ_ID," +
          " t.GUID," +
          " t.FROM_ID," +
          " t.FILE_PATH," +
          " s.TO_ID," +
          " s.STATUS," +
          " s.FAILED_MESSAGE," +
          " s.CREATE_TIME," +
          " s.COMPLETE_TIME" +
          " , 1 " + 
          " from ESB_TRANSFER_STATUS s" +
          " join ESB_TRANSFER t on s.TRANS_ID = t.GUID" +
          " where TYPE = '0'" ;
      
      Date date  = new Date();
      if (YHUtility.isNullorEmpty(queryType) || "0".equals(queryType)) {
         
         String dateStr = YHUtility.getDateTimeStr(date).split(" ")[0];
         startTime =   dateStr +   " 00:00:00";
         endTime =  dateStr + " 23:59:59";
         
         String dbDateF1 = YHDBUtility.getDateFilter("t.CREATE_TIME", startTime, " >= ");
         sql += " and (" + dbDateF1 + " or t.CREATE_TIME IS NULL) ";
         String dbDateF2 = YHDBUtility.getDateFilter("t.CREATE_TIME", endTime, " <= ");
         sql += " and (" + dbDateF2 + "  or t.CREATE_TIME IS NULL) ";
         //确定日期时间
      } else {
        if ("1".equals(queryType))  {
          
          //三天
        } else if ("2".equals(queryType)) {
          long time = date.getTime();
          long time2 = time - 24 * 60 * 60 * 1000 * 3;
          Date d = new Date(time2);
          String dateStr = YHUtility.getDateTimeStr(d).split(" ")[0];
          startTime = dateStr  +   " 00:00:00";
          //一周时间
        } else if ("3".equals(queryType)) {
          long time = date.getTime();
          int day = date.getDay();
          if (day == 0 ) {
            day = 7;
          }
          long time2 = time - 24 * 60 * 60 * 1000 * (day - 1);
          Date d = new Date(time2);
          String dateStr = YHUtility.getDateTimeStr(d).split(" ")[0];
          startTime = dateStr  +   " 00:00:00";
          //一月
        } else if ("4".equals(queryType)) {
          String dateStr = YHUtility.getDateTimeStr(date).split(" ")[0];
          int last = dateStr.lastIndexOf("-");
          dateStr = dateStr.substring(0 , last);
          startTime = dateStr  +   "-01 00:00:00";
        }
        if (!YHUtility.isNullorEmpty(startTime)) {
          String dbDateF1 = YHDBUtility.getDateFilter("t.CREATE_TIME", startTime, " >= ");
          sql += " and " + dbDateF1;
        }
        if (!YHUtility.isNullorEmpty(endTime)) {
          String dbDateF2 = YHDBUtility.getDateFilter("t.CREATE_TIME", endTime, " <= ");
          sql += " and " + dbDateF2;
        }
      }
      
      if (!YHUtility.isNullorEmpty(startNo) && YHUtility.isInteger(startNo)) {
        sql += " and t.SEQ_ID >= " + startNo ;
      }
      if (!YHUtility.isNullorEmpty(endNo) && YHUtility.isInteger(endNo)) {
        sql += " and t.SEQ_ID <= " + endNo ;
      }
      
      sql += " order by t.SEQ_ID desc ";
      
      ArrayList<YHDbRecord> dbL = this.toExportData(dbConn, sql);
      String fileName = URLEncoder.encode("下载.csv","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      YHCSVUtil.CVSWrite(response.getWriter(), dbL); 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return null; 
  }
  public String expUpload(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 
    response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
    String queryType = request.getParameter("queryType");
    String startTime =  YHUtility.null2Empty(request.getParameter("startTime"));
    String endTime =  YHUtility.null2Empty(request.getParameter("endTime"));
    String startNo = YHUtility.null2Empty(request.getParameter("startNo"));
    String endNo = YHUtility.null2Empty(request.getParameter("endNo"));
    
    
    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      
      String sql = "select" +
          " t.SEQ_ID," +
          " t.GUID," +
          " t.FROM_ID," +
          " t.FILE_PATH," +
          " t.TO_ID," +
          " t.STATUS," +
          " t.FAILED_MESSAGE," +
          " t.CREATE_TIME," +
          " t.COMPLETE_TIME" +
          " , 1 " + 
          " from ESB_TRANSFER t" +
          " where TYPE = '0' ";
      Date date  = new Date();
      if (YHUtility.isNullorEmpty(queryType) || "0".equals(queryType)) {
         
         String dateStr = YHUtility.getDateTimeStr(date).split(" ")[0];
         startTime =   dateStr +   " 00:00:00";
         endTime =  dateStr + " 23:59:59";
         //确定日期时间
      } else if ("1".equals(queryType))  {
         
        //三天
      } else if ("2".equals(queryType)) {
        long time = date.getTime();
        long time2 = time - 24 * 60 * 60 * 1000 * 3;
        Date d = new Date(time2);
        String dateStr = YHUtility.getDateTimeStr(d).split(" ")[0];
        startTime = dateStr  +   " 00:00:00";
        //一周时间
      } else if ("3".equals(queryType)) {
        long time = date.getTime();
        int day = date.getDay();
        if (day == 0 ) {
          day = 7;
        }
        long time2 = time - 24 * 60 * 60 * 1000 * (day - 1);
        Date d = new Date(time2);
        String dateStr = YHUtility.getDateTimeStr(d).split(" ")[0];
        startTime = dateStr  +   " 00:00:00";
        //一月
      } else if ("4".equals(queryType)) {
        String dateStr = YHUtility.getDateTimeStr(date).split(" ")[0];
        int last = dateStr.lastIndexOf("-");
        dateStr = dateStr.substring(0 , last);
        startTime = dateStr  +   "-01 00:00:00";
      }
      if (!YHUtility.isNullorEmpty(startTime)) {
        String dbDateF1 = YHDBUtility.getDateFilter("t.CREATE_TIME", startTime, " >= ");
        sql += " and " + dbDateF1;
      }
      if (!YHUtility.isNullorEmpty(endTime)) {
        String dbDateF2 = YHDBUtility.getDateFilter("t.CREATE_TIME", endTime, " <= ");
        sql += " and " + dbDateF2;
      }
      
      if (!YHUtility.isNullorEmpty(startNo) && YHUtility.isInteger(startNo)) {
        sql += " and t.SEQ_ID >= " + startNo ;
      }
      if (!YHUtility.isNullorEmpty(endNo) && YHUtility.isInteger(endNo)) {
        sql += " and t.SEQ_ID <= " + endNo ;
      }
      
      sql += " order by t.SEQ_ID desc ";
      ArrayList<YHDbRecord> dbL = this.toExportData(dbConn, sql);
      String fileName = URLEncoder.encode("上传.csv","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      YHCSVUtil.CVSWrite(response.getWriter(), dbL); 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return null; 
  }
  public ArrayList<YHDbRecord> toExportData(Connection conn,String sql) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    try {
      stmt = conn.createStatement();
    rs = stmt.executeQuery(sql);
    while (rs.next()) {
      YHDbRecord record = new YHDbRecord();
      int seqId = rs.getInt("SEQ_ID");
      Date d1 = rs.getTimestamp("CREATE_TIME");
      Date d2 = rs.getTimestamp("COMPLETE_TIME");
      
      long dtime1 = 0 ;
      long dtime2 = 0 ;
      String ss = "";
      if (d1 != null) {
        dtime1 = d1.getTime();
        if (d2 != null) {
          dtime2 = d2.getTime();
          ss = ((dtime2 - dtime1) / 1000) + "";
        }
      } 
      record.addField("SEQ_ID", seqId + "");
      record.addField("guid", rs.getString("GUID"));
      record.addField("发送时间",YHUtility.getDateTimeStr(d1));
      record.addField("完成时间",YHUtility.getDateTimeStr(d2));
      record.addField("用时",ss);
      result.add(record);
    }
  } catch (Exception ex) {
    throw ex;
  } finally {
    YHDBUtility.close(stmt, rs,null);
  }
  return result;
}
}
