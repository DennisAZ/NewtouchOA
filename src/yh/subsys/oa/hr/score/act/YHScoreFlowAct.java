package yh.subsys.oa.hr.score.act;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.examManage.logic.YHPageData;
import yh.subsys.oa.hr.score.data.YHScoreAnswer;
import yh.subsys.oa.hr.score.data.YHScoreData;
import yh.subsys.oa.hr.score.data.YHScoreFlow;
import yh.subsys.oa.hr.score.data.YHScoreGroup;
import yh.subsys.oa.hr.score.data.YHScoreShow;
import yh.subsys.oa.hr.score.logic.YHScoreAnswerLogic;
import yh.subsys.oa.hr.score.logic.YHScoreDataLogic;
import yh.subsys.oa.hr.score.logic.YHScoreFlowLogic;
import yh.subsys.oa.hr.score.logic.YHScoreGroupLogic;
import yh.subsys.oa.hr.score.logic.YHScoreItemLogic;


public class YHScoreFlowAct {
  public static final String attachmentFolder = "scoreFlow";
  private YHScoreFlowLogic logic = new YHScoreFlowLogic();
  /**
   *查询所有(分页)通用列表显示数据--考核任务管理
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectFlow(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = YHScoreFlowLogic.selectFlow(dbConn, request.getParameterMap());
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   *查询所有(分页)通用列表显示数据--进行考核
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectFlow2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
      String dayTime = sf.format(new Date());
      String data = YHScoreFlowLogic.selectFlow2(dbConn, request.getParameterMap(),dayTime,String.valueOf(person.getSeqId()));
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  /**
   *查询所有(分页)通用列表显示数据--查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String flowTitle = request.getParameter("flowTitle");
      String rankman = request.getParameter("rankman");
      String groupId = request.getParameter("groupId");
      String participant = request.getParameter("participant");
      String beginDate = request.getParameter("beginDate");
      String beginDate1 = request.getParameter("beginDate1");
      String endDate = request.getParameter("endDate");
      String endDate1 = request.getParameter("endDate1");
      String cd = request.getParameter("cd");

      YHScoreFlow flow = new YHScoreFlow();
      flow.setFlowTitle(flowTitle);
      flow.setParticipant(participant);
      flow.setRankman(rankman);
      if (!YHUtility.isNullorEmpty(groupId)) {
        flow.setGroupId(Integer.parseInt(groupId));
      }
      if (!YHUtility.isNullorEmpty(beginDate)) {
        flow.setBeginDate(YHUtility.parseDate(beginDate));
      }
      if (!YHUtility.isNullorEmpty(endDate)) {
        flow.setEndDate(YHUtility.parseDate(endDate));
      }
      String data = YHScoreFlowLogic.selectList(dbConn, request.getParameterMap(), flow, beginDate1, endDate1, cd);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 导出分数明细
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String excelDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      OutputStream ops = null;
      String fileName = URLEncoder.encode("考核分数明细报表.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      String seqId = request.getParameter("seqId");
      String groupId = request.getParameter("groupId");
      //List<YHScoreFlow> list = YHScoreFlowLogic.expList(dbConn,seqId);
      //ArrayList<YHDbRecord> dbL = YHScoreFlowLogic.getDbRecord(dbConn,list);
      
      List<YHScoreFlow> scoreDatalist = YHScoreFlowLogic.expScoreDataList(dbConn,seqId,groupId);
      List<YHScoreFlow> scoreItemlist = YHScoreFlowLogic.expScoreItemList(dbConn,seqId,groupId);
      
      ArrayList<YHDbRecord> dbL = YHScoreFlowLogic.getDbRecord(dbConn,scoreDatalist,scoreItemlist);
      
      YHJExcelUtil.writeExc(ops,dbL);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "导出数据成功！");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "导出数据失败");
      throw e;
    }
    return null;
  }
  
  /**
   * 导出总分
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String excelExport(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      OutputStream ops = null;
      String fileName = URLEncoder.encode("考核报表.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      String seqId = request.getParameter("seqId");
      String groupId = request.getParameter("groupId");
      //List<YHScoreFlow> list = YHScoreFlowLogic.expList2(dbConn,seqId);
      //ArrayList<YHDbRecord> dbL = YHScoreFlowLogic.getDbRecord2(dbConn,list);
      
      List<YHScoreFlow> scoreDatalist = YHScoreFlowLogic.expScoreDataList(dbConn,seqId,groupId);
      List<YHScoreFlow> scoreItemlist = YHScoreFlowLogic.expScoreItemList(dbConn,seqId,groupId);
      
      ArrayList<YHDbRecord> dbL = YHScoreFlowLogic.getDbRecord2(dbConn,scoreDatalist,scoreItemlist);
      
      YHJExcelUtil.writeExc(ops,dbL);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "导出数据成功！");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "导出数据失败");
      throw e;
    }
    return null;
  }
  /**
   * 得到考核分页-syl
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public String getScoreOnline(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String flowId = request.getParameter("flowId");//
      String currPage = request.getParameter("currPage");
      if(YHUtility.isNullorEmpty(currPage)){
        currPage = "1";
      }
      String data = "[";
      String pageInfo = "";

      if(YHUtility.isInteger(flowId)){
        YHScoreFlowLogic flowLogic = new YHScoreFlowLogic();
        YHScoreGroupLogic groupLogic = new YHScoreGroupLogic();
        YHScoreItemLogic itemLogic = new YHScoreItemLogic();
        YHScoreAnswerLogic answerLogic = new YHScoreAnswerLogic();
        YHScoreFlow flow = flowLogic.getFlowById(dbConn, flowId);//查出考核信息
        if(flow !=null){   
          YHScoreGroup group = groupLogic.getScoreGroupDetail(dbConn,flow.getGroupId());//查询考核任务
          if(group != null){
            int groupId = group.getSeqId();
            //得到考核指标明细的总数
            int count =  answerLogic.selectAnswerCount(dbConn, groupId);
            YHPageData pageData = new YHPageData(5,count,Integer.parseInt(currPage));
            if (count > 0) {
              String[] str = {"GROUP_ID = " + groupId};
              List< YHScoreAnswer> DataAllList = answerLogic.getAnswerByGroupId(dbConn, str);
              String seqIds = "";
              for (long i = pageData.getFirstResult(); i < pageData.getLastResult(); i++) {
                seqIds = seqIds + DataAllList.get((int)i).getSeqId() + ",";
              }
              seqIds = seqIds.substring(0, seqIds.length() - 1);
              String[] str2 = {"SEQ_ID in(" + seqIds + ")"};
              List<YHScoreAnswer> answerList  = answerLogic.getAnswerByGroupId(dbConn, str2);//得到分页列表
              for (int i = 0; i < answerList.size(); i++) {
                data = data + YHFOM.toJson(answerList.get(i)) + ",";
              }
              if(answerList.size()>0){
                data = data.substring(0, data.length()-1);
              }

            } else {

            }
            pageInfo = "{pageSize:\"" + pageData.getPageSize() + "\",hasPrev:\"" 
            + pageData.isHasPrev() +"\",hasNext:\""+ pageData.isHasNext()
            +"\",beginPageIndex:\"" +pageData.getBeginPageIndex() 
            + "\",endPageIndex:\"" + pageData.getEndPageIndex()
            +"\",currentPageIndex:\"" + pageData.getCurrentPageIndex()
            +"\",totalPageNum:\"" + pageData.getTotalPageNum()+ "\"}";
          }
        }
      }
      if(pageInfo.equals("")){
        pageInfo = "{}";
      }
      data = data + "]";
      String allData = "{pageInfo:" + pageInfo + ",data:" + data + "}";
      request.setAttribute(YHActionKeys.RET_DATA,allData);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到考核分页-syl
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public String getScoreOnline2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String flowId = request.getParameter("flowId");//
      String groupId = request.getParameter("groupId");//
      String currPage = request.getParameter("currPage");
      if(YHUtility.isNullorEmpty(currPage)){
        currPage = "1";
      }
      String data = "[";
      String pageInfo = "";
      
      if(YHUtility.isInteger(groupId)){
        YHScoreFlowLogic flowLogic = new YHScoreFlowLogic();
        YHScoreGroupLogic groupLogic = new YHScoreGroupLogic();
        YHScoreItemLogic itemLogic = new YHScoreItemLogic();
        YHScoreAnswerLogic answerLogic = new YHScoreAnswerLogic();
       // YHScoreFlow flow = flowLogic.getFlowById(dbConn, flowId);//查出考核信息
       // if(flow !=null){   
         // YHScoreGroup group = groupLogic.getScoreGroupDetail(dbConn,flow.getGroupId());//查询考核任务
         // if(group != null){
          //  int groupId = group.getSeqId();
            //得到考核指标明细的总数
            int count =  answerLogic.selectAnswerCount(dbConn, Integer.parseInt(groupId));
            YHPageData pageData = new YHPageData(5,count,Integer.parseInt(currPage));
            if (count > 0) {
              String[] str = {"GROUP_ID = " + groupId};
              List< YHScoreAnswer> DataAllList = answerLogic.getAnswerByGroupId(dbConn, str);
              String seqIds = "";
              for (long i = pageData.getFirstResult(); i < pageData.getLastResult(); i++) {
                seqIds = seqIds + DataAllList.get((int)i).getSeqId() + ",";
              }
              seqIds = seqIds.substring(0, seqIds.length() - 1);
              String[] str2 = {"SEQ_ID in(" + seqIds + ")"};
              List<YHScoreAnswer> answerList  = answerLogic.getAnswerByGroupId(dbConn, str2);//得到分页列表
              for (int i = 0; i < answerList.size(); i++) {
                data = data + YHFOM.toJson(answerList.get(i)) + ",";
              }
              if(answerList.size()>0){
                data = data.substring(0, data.length()-1);
              }

            } else {

            }
            pageInfo = "{pageSize:\"" + pageData.getPageSize() + "\",hasPrev:\"" 
            + pageData.isHasPrev() +"\",hasNext:\""+ pageData.isHasNext()
            +"\",beginPageIndex:\"" +pageData.getBeginPageIndex() 
            + "\",endPageIndex:\"" + pageData.getEndPageIndex()
            +"\",currentPageIndex:\"" + pageData.getCurrentPageIndex()
            +"\",totalPageNum:\"" + pageData.getTotalPageNum()+ "\"}";
        //  }
        //}
      }
      if(pageInfo.equals("")){
        pageInfo = "{}";
      }
      data = data + "]";
      String allData = "{pageInfo:" + pageInfo + ",data:" + data + "}";
      request.setAttribute(YHActionKeys.RET_DATA,allData);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到在先考试的一条记录ByFlowId and PARTICIPANT考试人id--syl
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getDataByFlowId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String flowId = request.getParameter("flowId");
      String userId = request.getParameter("userId");
      if(!YHUtility.isInteger(userId)){
        userId = user.getSeqId() + "";
      }
      String data = "[";
      if(YHUtility.isInteger(flowId)&& YHUtility.isInteger(userId)){
        YHScoreDataLogic dataLogic = new YHScoreDataLogic();
        String[] str = {"FLOW_ID=" + flowId ,"PARTICIPANT = '" + userId+"'" };
        List<YHScoreData> dataList = dataLogic.selectData(dbConn, str);
        int userScore = 0;
        for (int i = 0; i < dataList.size(); i++) { 
          data = data + YHFOM.toJson(dataList.get(0));
          break;
        }
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到在先考试的一条记录ByFlowId and PARTICIPANT考试人id--syl
   * ---外办
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getDataByFlowId2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String groupId = request.getParameter("groupId");
      String userId = request.getParameter("userId");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String ymd = year + "-" + month;
      if(!YHUtility.isInteger(userId)){
        userId = user.getSeqId() + "";
      }
      String data = "[";
      if(YHUtility.isInteger(groupId)&& YHUtility.isInteger(userId)){
        YHScoreDataLogic dataLogic = new YHScoreDataLogic();
        String[] str = {"GROUP_ID=" + groupId + " and PARTICIPANT = '" + userId + "' and SCORE_TIME = '"+ymd+"'"};
        List<YHScoreShow> dataList = dataLogic.selectData2(dbConn, str);
        int userScore = 0;
        for (int i = 0; i < dataList.size(); i++) { 
          data = data + YHFOM.toJson(dataList.get(0));
          break;
        }
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String add(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<String,String[]> map = request.getParameterMap();
      YHScoreFlow scoreFlow = (YHScoreFlow) YHFOM.build(map, YHScoreFlow.class, "");

      if (scoreFlow.getBeginDate() == null) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        scoreFlow.setBeginDate(YHUtility.parseSqlDate(sf.format(new Date())));
      }
      this.logic.addScoreFlow(dbConn, scoreFlow);//添加数据

      String smsSJ = request.getParameter("smsSJ");//手机短信
      String smsflag = request.getParameter("smsflag");//内部短信
      if (smsflag.equals("1")) {
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("15");
        sb.setContent("请查看考核任务！\n标题：" + scoreFlow.getFlowTitle());
        sb.setSendDate(new java.util.Date());
        sb.setFromId(person.getSeqId());
        sb.setToId(scoreFlow.getParticipant());
        sb.setRemindUrl("/subsys/oa/hr/score/flow/index1.jsp&openFlag=1&openWidth=820&openHeight=600");
        YHSmsUtil.smsBack(dbConn,sb);
      }
      //手机消息提醒
      if (smsSJ.equals("1")) {
        YHMobileSms2Logic sb2 = new YHMobileSms2Logic();
        sb2.remindByMobileSms(dbConn,scoreFlow.getParticipant(),person.getSeqId(),"请查看考核任务！\n标题：" + scoreFlow.getFlowTitle() ,new java.util.Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 删除考核任务--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteSingle(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      this.logic.deleteSingleScoreData(dbConn, seqId);
      this.logic.deleteSingleScoreFlow(dbConn, Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取编辑考核任务 信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getScoreFlowDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      if(YHUtility.isNullorEmpty(seqId)){
        seqId = "0";
      }
      YHScoreFlow paper = (YHScoreFlow)this.logic.getScoreFlowDetail(dbConn, Integer.parseInt(seqId));
      if (paper == null){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
        request.setAttribute(YHActionKeys.RET_MSRG, "该考核任务 不存在");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(paper);
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
   * 编辑考核任务信息 并发短信
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateScoreFlow(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<String,String[]> map = request.getParameterMap();
      YHScoreFlow scoreFlow = (YHScoreFlow) YHFOM.build(map, YHScoreFlow.class, "");
      this.logic.updateScoreFlow(dbConn, scoreFlow);
      String smsSJ = request.getParameter("smsSJ");//手机短信
      String smsflag = request.getParameter("smsflag");//内部短信
      if (smsflag.equals("1")) {
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("15");
        sb.setContent("请查看考核任务！\n标题：" + scoreFlow.getFlowTitle());
        sb.setSendDate(new java.util.Date());
        sb.setFromId(person.getSeqId());
        sb.setToId(scoreFlow.getParticipant());
        sb.setRemindUrl("/subsys/oa/hr/score/flow/index1.jsp&openFlag=1&openWidth=820&openHeight=600");
        YHSmsUtil.smsBack(dbConn,sb);
      }
      //手机消息提醒
      if (smsSJ.equals("1")) {
        YHMobileSms2Logic sb2 = new YHMobileSms2Logic();
        sb2.remindByMobileSms(dbConn,scoreFlow.getParticipant(),person.getSeqId(),"请查看考核任务！\n标题：" + scoreFlow.getFlowTitle() ,new java.util.Date());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   *立即生效,立即终止,恢复终止
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateBeginDate(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String tdName = request.getParameter("tdName");
      String dayTime = request.getParameter("dayTime");
      if (!YHUtility.isNullorEmpty(seqId)) {
        if (!YHUtility.isNullorEmpty(dayTime)) {
          this.logic.updateBeginDate(dbConn,Integer.parseInt(seqId),tdName,YHUtility.parseSqlDate(dayTime));
        }
        if (YHUtility.isNullorEmpty(dayTime)) {
          this.logic.updateBeginDate(dbConn,Integer.parseInt(seqId),tdName,null);
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功"); 
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取考核指标明细表(SCORE_ITEM)中的信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getScoreFlowData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String groupFlag = request.getParameter("groupFlag");
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"GROUP_ID=" + Integer.parseInt(seqId)};
      List funcList = new ArrayList();
      
      if("0".equals(groupFlag)){
        funcList.add("scoreItem");
        map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
        list.addAll((List<Map>) map.get("OA_SCORE_ITEM"));
        for(Map ms : list){
          String itemName = (String) ms.get("itemName");
          if(!YHUtility.isNullorEmpty(itemName)){
            itemName = itemName.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
          }
          sb.append("{");
          sb.append("seqId:\"" + ms.get("seqId") + "\"");
          sb.append(",itemName:\"" + (ms.get("itemName") == null ? "" : itemName) + "\"");
          sb.append(",groupId:\"" + (ms.get("groupId") == null ? "" : ms.get("groupId")) + "\"");
          sb.append(",min:\"" + (ms.get("min") == null ? "" : ms.get("min")) + "\"");
          sb.append(",max:\"" + (ms.get("max") == null ? "" : ms.get("max")) + "\"");
          sb.append("},");
        }
      }else{
        funcList.add("scoreAnswer");
        map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
        list.addAll((List<Map>) map.get("OA_SCORE_ANSWER"));
        for(Map ms : list){
          String itemName = (String) ms.get("itemName");
          if(!YHUtility.isNullorEmpty(itemName)){
            itemName = itemName.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
          }
          sb.append("{");
          sb.append("seqId:\"" + ms.get("seqId") + "\"");
          sb.append(",itemName:\"" + (ms.get("itemName") == null ? "" : itemName) + "\"");
          sb.append(",groupId:\"" + (ms.get("groupId") == null ? "" : ms.get("groupId")) + "\"");
          sb.append("},");
        }
      }
      
      sb.deleteCharAt(sb.length() - 1); 
      if (list.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getGroupName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String paperSeqIdStr = request.getParameter("seqId");
      int seqId = 0;
      if(!YHUtility.isNullorEmpty(paperSeqIdStr)){
        seqId = Integer.parseInt(paperSeqIdStr);
      }
      String data = this.logic.getGroupDescLogic(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + YHUtility.encodeSpecial(data) + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取月考核－0、年考核－1标记 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCheckFlag(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String paperSeqIdStr = request.getParameter("flowId");
      int seqId = 0;
      if(!YHUtility.isNullorEmpty(paperSeqIdStr)){
        seqId = Integer.parseInt(paperSeqIdStr);
      }
      String data = this.logic.getCheckFlagLogic(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取考核任务标题
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFlowTitleName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String flowId = request.getParameter("seqId");
      int seqId = 0;
      if(!YHUtility.isNullorEmpty(flowId)){
        seqId = Integer.parseInt(flowId);
      }
      String data = this.logic.getFlowTitleName(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + YHUtility.encodeSpecial(data) + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取设定考核依据模块工作日志和日程
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getGroupRefer(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String groupId = request.getParameter("seqId");
      int seqId = 0;
      if(!YHUtility.isNullorEmpty(groupId)){
        seqId = Integer.parseInt(groupId);
      }
      String data = this.logic.getGroupRefer(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + YHUtility.encodeSpecial(data) + "\"");
    } catch (Exception ex) {
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
   * @return
   * @throws Exception
   */
  public String getScoreDataInsert(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("groupId");
      int seqId = 0;
      if(!YHUtility.isNullorEmpty(seqIdStr)){
        seqId = Integer.parseInt(seqIdStr);
      }
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"GROUP_ID=" + seqId};
      List funcList = new ArrayList();
      funcList.add("scoreItem");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_SCORE_ITEM"));
      
      for(Map ms : list){
        String itemName = (String) ms.get("itemName");
        if(!YHUtility.isNullorEmpty(itemName)){
          itemName = itemName.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
        }
        sb.append("{");
        sb.append("seqId:\"" + ms.get("seqId") + "\"");
        sb.append(",itemName:\"" + (ms.get("itemName") == null ? "" : itemName) + "\"");
        sb.append(",groupId:\"" + (ms.get("groupId") == null ? "" : ms.get("groupId")) + "\"");
        sb.append(",min:\"" + (ms.get("min") == null ? "" : ms.get("min")) + "\"");
        sb.append(",max:\"" + (ms.get("max") == null ? "" : ms.get("max")) + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1); 
      if (list.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String showMan(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String participant = request.getParameter("participant");
      if (!YHUtility.isNullorEmpty(participant)) {
        List<YHPerson> list = this.logic.showMan(dbConn, participant);
        String data = "[";
        YHPerson person = new YHPerson(); 
        for (int i = 0; i < list.size(); i++) {
          person = list.get(i);
          data = data + YHFOM.toJson(person).toString()+",";
        }
        if(list.size() > 0){
          data = data.substring(0, data.length() - 1);
        }
        data = data + "]";
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getFinishFlag(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String userId = request.getParameter("userId");
      String flowId = request.getParameter("flowId");
      boolean bool = this.logic.getFinishFlag(dbConn, userId, Integer.parseInt(flowId));
      String data = "";
      if(bool){
        data = "1";
      }else{
        data = "0";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 判断考核指标集是否已经被应用
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getScoreFlowFlag(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String groupId = request.getParameter("groupId");
      boolean bool = this.logic.getScoreFlowFlag(dbConn, groupId);
      String data = "";
      if(bool){
        data = "1";
      }else{
        data = "0";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

}
