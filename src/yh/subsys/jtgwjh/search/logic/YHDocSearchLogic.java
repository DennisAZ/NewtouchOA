package yh.subsys.jtgwjh.search.logic;


import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;


public class YHDocSearchLogic {
  private static Logger log = Logger.getLogger(YHDocSearchLogic.class);
  
  /**
   * 发文登记列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getSendJsonLogic(Connection dbConn, Map request1, YHPerson person,HttpServletRequest request) throws Exception {
    try {
      
      String docTitle = request.getParameter("docTitle");
      String docType = request.getParameter("docType");
      String docKind = request.getParameter("docKind");
      String docNo = request.getParameter("docNo");
      String urgentType = request.getParameter("urgentType");
      String securityLevel = request.getParameter("securityLevel");
      String reciveDept = request.getParameter("reciveDept");
      String sendDate1 = request.getParameter("sendDate1");
      String sendDate2 = request.getParameter("sendDate2");
      String sendDate3 = request.getParameter("sendDate3");
      String sendDate4 = request.getParameter("sendDate4");
      
      String whereStr = "";
      if (!YHUtility.isNullorEmpty(docTitle)){
        whereStr += " and c1.DOC_TITLE like '%" + YHDBUtility.escapeLike(docTitle) + "%'";
      }
      if (!YHUtility.isNullorEmpty(docType)){
        whereStr += " and c1.DOC_TYPE ='" + YHDBUtility.escapeLike(docType) + "'";
      }
      if (!YHUtility.isNullorEmpty(docKind)){
        whereStr += " and c1.DOC_KIND ='" + YHDBUtility.escapeLike(docKind) + "'";
      }
      if (!YHUtility.isNullorEmpty(docNo)){
        whereStr += " and c1.DOC_NO like '%" + YHDBUtility.escapeLike(docNo) + "%'";
      }
      if (!YHUtility.isNullorEmpty(urgentType)){
        whereStr += " and c1.URGENT_TYPE ='" + YHDBUtility.escapeLike(urgentType) + "'";
      }
      if (!YHUtility.isNullorEmpty(securityLevel)){
        whereStr += " and c1.SECURITY_LEVEL ='" + YHDBUtility.escapeLike(securityLevel) + "'";
      }
      if (!YHUtility.isNullorEmpty(sendDate1)) {
        whereStr += " and " + YHDBUtility.getDateFilter("c1.CREATE_DATETIME", sendDate1, ">=");
      }
      if (!YHUtility.isNullorEmpty(sendDate2)) {
        whereStr += " and " + YHDBUtility.getDateFilter("c1.CREATE_DATETIME", sendDate2 + " 23:59:59", "<=");
      }
      if (!YHUtility.isNullorEmpty(sendDate3)) {
        whereStr += " and " + YHDBUtility.getDateFilter("c1.SEND_DATETIME", sendDate3, ">=");
      }
      if (!YHUtility.isNullorEmpty(sendDate4)) {
        whereStr += " and " + YHDBUtility.getDateFilter("c1.SEND_DATETIME", sendDate4 + " 23:59:59", "<=");
      }
      if (!YHUtility.isNullorEmpty(reciveDept)) {
        whereStr += " and (";
        String reciveDeptArray[] = reciveDept.split(",");
        for(int i = 0; i < reciveDeptArray.length; i++){
          if(i == 0){
            whereStr += YHDBUtility.findInSet(reciveDeptArray[i], "c1.RECIVE_DEPT");
          }
          else{
            whereStr += " or" + YHDBUtility.findInSet(reciveDeptArray[i], "c1.RECIVE_DEPT");
          }
        }
        whereStr += ")";
      }
      String sql = " SELECT SEQ_ID, DOC_TITLE, DOC_TYPE, URGENT_TYPE, SECURITY_LEVEL, DOC_NO, "
                 + " RECIVE_DEPT, RECIVE_DEPT_DESC, CREATE_DATETIME, IS_STAMP, STAMP_COMPLETE, '0' STATUS, SEND_DATETIME "
                 + " FROM jh_docsend_info c1 "
                 + " where 1=1 " + whereStr;
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request1);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 收文登记列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getReciveJsonLogic(Connection dbConn, Map request1, YHPerson person,HttpServletRequest request) throws Exception {
    try {
      
      String docTitle = request.getParameter("docTitle");
      String docType = request.getParameter("docType");
      String docKind = request.getParameter("docKind");
      String docNo = request.getParameter("docNo");
      String urgentType = request.getParameter("urgentType");
      String securityLevel = request.getParameter("securityLevel");
      String sendDept = request.getParameter("sendDept");
      String sendDate1 = request.getParameter("sendDate1");
      String sendDate2 = request.getParameter("sendDate2");
      String sendDate3 = request.getParameter("sendDate3");
      String sendDate4 = request.getParameter("sendDate4");
      
      String handStatus = request.getParameter("handStatus");
      String whereStr = "";
      if (!YHUtility.isNullorEmpty(docTitle)){
        whereStr += " and c1.DOC_TITLE like '%" + YHDBUtility.escapeLike(docTitle) + "%'";
      }
      if (!YHUtility.isNullorEmpty(docType)){
        whereStr += " and c1.DOC_TYPE ='" + YHDBUtility.escapeLike(docType) + "'";
      }
      if (!YHUtility.isNullorEmpty(handStatus)){
        whereStr += " and c1.hand_status ='" + handStatus + "'";
      }
      if (!YHUtility.isNullorEmpty(docKind)){
        whereStr += " and c1.DOC_KIND ='" + YHDBUtility.escapeLike(docKind) + "'";
      }
      if (!YHUtility.isNullorEmpty(docNo)){
        whereStr += " and c1.DOC_NO like '%" + YHDBUtility.escapeLike(docNo) + "%'";
      }
      if (!YHUtility.isNullorEmpty(urgentType)){
        whereStr += " and c1.URGENT_TYPE ='" + YHDBUtility.escapeLike(urgentType) + "'";
      }
      if (!YHUtility.isNullorEmpty(securityLevel)){
        whereStr += " and c1.SECURITY_LEVEL ='" + YHDBUtility.escapeLike(securityLevel) + "'";
      }
      if (!YHUtility.isNullorEmpty(sendDate1)) {
        whereStr += " and " + YHDBUtility.getDateFilter("c1.SEND_DATETIME", sendDate1, ">=");
      }
      if (!YHUtility.isNullorEmpty(sendDate2)) {
        whereStr += " and " + YHDBUtility.getDateFilter("c1.SEND_DATETIME", sendDate2 + " 23:59:59", "<=");
      }
      if (!YHUtility.isNullorEmpty(sendDate3)) {
        whereStr += " and " + YHDBUtility.getDateFilter("c1.RECEIVE_DATETIME", sendDate3, ">=");
      }
      if (!YHUtility.isNullorEmpty(sendDate4)) {
        whereStr += " and " + YHDBUtility.getDateFilter("c1.RECEIVE_DATETIME", sendDate4 + " 23:59:59", "<=");
      }
      if (!YHUtility.isNullorEmpty(sendDept)){
        if(sendDept.endsWith(",")){
          sendDept = sendDept.substring(0, sendDept.length() -1);
        }
        whereStr += " and SEND_DEPT in ("+sendDept+") ";
      }
      String sql = " SELECT SEQ_ID, DOC_TITLE, DOC_TYPE, URGENT_TYPE, SECURITY_LEVEL, DOC_NO, "
                 + " SEND_DEPT, SEND_DEPT_NAME, SEND_DATETIME, STATUS,hand_status, RECEIVE_DATETIME "
                 + " FROM jh_docrecv_info c1 "
                 + " where 1=1 " + whereStr;
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request1);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
  
  /**
   * 用印情况列表
   * 
   * @param dbConn
   * @param request
   * @param person
   * @return
   * @throws Exception
   */
  public String getStampJsonLogic(Connection dbConn, Map request1, YHPerson person,HttpServletRequest request) throws Exception {
    try {
      
      String stampType = request.getParameter("stampType");
      String stampUser = request.getParameter("stampUser");
      String sendDate1 = request.getParameter("sendDate1");
      String sendDate2 = request.getParameter("sendDate2");
      
      String whereStr = "";
      if (!YHUtility.isNullorEmpty(stampType)){
        whereStr += " and c1.STAMP_TYPE ='" + YHDBUtility.escapeLike(stampType) + "'";
      }
      if (!YHUtility.isNullorEmpty(sendDate1)) {
        whereStr += " and " + YHDBUtility.getDateFilter("c1.STAMP_TIME", sendDate1, ">=");
      }
      if (!YHUtility.isNullorEmpty(sendDate2)) {
        whereStr += " and " + YHDBUtility.getDateFilter("c1.STAMP_TIME", sendDate2 + " 23:59:59", "<=");
      }
      if (!YHUtility.isNullorEmpty(stampUser)){
        String str = "";
        String stampUserArray[] = stampUser.split(",");
        for(int i = 0; i < stampUserArray.length; i++){
          str += "'" + stampUserArray[i] + "',";
        }
        str = str.substring(0, str.length() - 1);
        whereStr += " and USER in ("+str+") ";
      }
      String sql = " SELECT c2.SEQ_ID, c2.DOC_TITLE, c1.STAMP_TYPE, c1.DEPT_NAME, c1.USER_NAME, c1.STAMP_TIME "
                 + " FROM jh_docsend_stamps c1 "
                 + " join jh_docsend_info c2 on c1.DOC_SEND_INFO_ID = c2.SEQ_ID "
                 + " where STAMP_TIME is not null " + whereStr;
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request1);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      return pageDataList.toJson();
    } catch (Exception e) {
      throw e;
    }
  }
}
