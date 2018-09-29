package yh.subsys.oa.hr.score.act;

import java.sql.Connection;
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
import yh.core.funcs.diary.logic.YHMyPriv;
import yh.core.funcs.diary.logic.YHPrivUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.module.org_select.logic.YHOrgSelect2Logic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.score.data.YHScoreAnswer;
import yh.subsys.oa.hr.score.data.YHScoreData;
import yh.subsys.oa.hr.score.data.YHScoreFlow;
import yh.subsys.oa.hr.score.data.YHScoreShow;
import yh.subsys.oa.hr.score.logic.YHScoreAnswerLogic;
import yh.subsys.oa.hr.score.logic.YHScoreDataLogic;
import yh.subsys.oa.hr.score.logic.YHScoreShowLogic;

public class YHScoreShowAct {
  public static final String attachmentFolder = "scoreShow";
  private YHScoreShowLogic logic = new YHScoreShowLogic();
  
  /**
   * 获取本部门下的人员列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getScoreFlowMonthData(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String seqId = "245";//request.getParameter("flowId");
      YHORM orm = new YHORM();

      ArrayList<YHPerson> persons = new ArrayList<YHPerson>();
      String userIdStr = "";
      String moduleId = "";
      int privNoFlag = 2;
      YHMyPriv mp = new YHMyPriv();
      String userIdStrFunc = "";
      YHOrgSelect2Logic osl = new YHOrgSelect2Logic();
      mp = YHPrivUtil.getMyPriv(dbConn, person, moduleId, privNoFlag);
      if (person.getDeptId() != 0) {
        persons = osl.getDeptUser(dbConn, person.getDeptId()  , false);
      }
      HashMap map = null;
 //     ArrayList<YHPerson> personList = (ArrayList<YHPerson>) orm.loadListSingle(dbConn, YHPerson.class, map);
      StringBuffer sb = new StringBuffer("[");
      for (YHPerson per : persons) {
        int seqIds = per.getSeqId();
        if (!YHPrivUtil.isUserPriv(dbConn, seqIds, mp, person)) {
          continue;
        }
        if(seqIds == person.getSeqId()){
          continue;
        }
        sb.append("{");
        sb.append("seqId:\"" + per.getSeqId() + "\"");
        sb.append(",userId:\"" + (per.getUserId() == null ? "" : per.getUserId()) + "\"");
        sb.append(",userName:\"" + (per.getUserName() == null ? "" : per.getUserName()) + "\"");
        sb.append(",deptId:\"" + (per.getDeptId()) + "\"");
        sb.append(",userPriv:\"" + (per.getUserPriv() == null ? "" : per.getUserPriv()) + "\"");
        //sb.append(",img:\"" + "1" + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1); 

      sb.append("]");
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
 
  /**
   * 新建--》取对象---syl  ----外办绩效考核
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String addData2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String flowId = request.getParameter("flowId");
      String groupId = request.getParameter("groupId");
      String userId = request.getParameter("userId");
      if(!YHUtility.isInteger(userId)){
        userId = user.getSeqId() + "";
      }
      int  seqId = 0;
      String dataStr = "";
      if(YHUtility.isInteger(groupId)){
        //先判断是否已经考核过了
        YHScoreDataLogic dataLogic = new YHScoreDataLogic();
        YHScoreShow data = null;
        String[] str = {"GROUP_ID=" + flowId ,"PARTICIPANT = '" + userId+"'" };
        List<YHScoreShow> dataList = dataLogic.selectData2(dbConn, str);
        if( dataList.size()>0){
          data = dataList.get(0);
          seqId = data.getSeqId();
        }else{
          data = new YHScoreShow();
          data.setGroupId(Integer.parseInt(groupId));
          data.setParticipant(user.getSeqId()+"");
          data.setRankman(user.getSeqId()+"");
          data.setRankDate(new Date());
          seqId =  dataLogic.addData2(dbConn, data);
          data.setSeqId(seqId);
        }
        dataStr = YHFOM.toJson(data).toString();
      }
      if(dataStr.equals("")){
        dataStr = "{}";
      }
      request.setAttribute(YHActionKeys.RET_DATA,dataStr);
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
   * 提交上下页--记录考核信息----外办
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String scoreData2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String quizIds = request.getParameter("quizIds");//选中的ID
      String quizIdpage = request.getParameter("quizIdpage");//当页的所有题目seqId字符串      String isSubmit =  request.getParameter("type");
      String currPage = request.getParameter("currPage");
      String groupId = request.getParameter("groupId");
      String userId = request.getParameter("userId");
      String checkFlag = request.getParameter("checkFlag");
      String checkEnd = request.getParameter("checkEnd");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String ymd = year + "-" + month;
      YHScoreShowLogic dataLogic = new YHScoreShowLogic();
      YHScoreAnswerLogic answerLogic = new YHScoreAnswerLogic();
      YHScoreShow data = null;
      String score = "";
      String answe = "";
      String memo = "";
      int pageSize = 5;
        if(YHUtility.isInteger(groupId) && YHUtility.isInteger(userId)){
          //先判断是否已经考核过了
          
          String[] str = {"GROUP_ID=" + groupId + " and PARTICIPANT = '" + userId + "' and SCORE_TIME = '"+ymd+"'"};
          List<YHScoreShow> dataList = dataLogic.selectData2(dbConn, str);
          if(YHUtility.isNullorEmpty(quizIds)){
            quizIds = "";
          }
          if(YHUtility.isNullorEmpty(isSubmit)){
            isSubmit = "";
          }
          if(!YHUtility.isInteger(currPage)){
            currPage = "1";
          }
          if(!YHUtility.isNullorEmpty(quizIdpage)){
            if(quizIdpage.endsWith(",")){
              quizIdpage = quizIdpage.substring(0, quizIdpage.length()-1);
            }
            String[] str2 = {"SEQ_ID in(" + quizIdpage + ")"};
            List<YHScoreAnswer> quizList  = answerLogic.getAnswerByGroupId(dbConn, str2);//得到题目
            String[] quidArray = quizIds.split(",");//选中的ID
            for (int i = 0; i < quizList.size(); i++) {
              YHScoreAnswer quiz = quizList.get(i);
              String isScore = " ";
              String isAnswe = " ";
              String isMemo = " ";
              isMemo = request.getParameter("memo_" + quiz.getSeqId());
              if(YHUtility.isNullorEmpty(isMemo)){
                isMemo = " ";
              }else{
                isMemo = isMemo.replace(",", "，");
              }
              String quizSeqId = quiz.getSeqId()+"";
              for (int j = 0; j < quidArray.length; j++) {
                String quidStr = quidArray[j];//seqId_A
                String[] quidStrArray =  quidStr.split("_");
                if(quidStrArray.length>=2){
                  String quidSeqId = quidStrArray[0];//得到选中的试题ID
                  String quidAnswers = quidStrArray[1];//得到选中的试题的多填写的答案
                  if(quizSeqId.equals(quidSeqId)&& !quidAnswers.equals("")){
                    if(quidAnswers.equals("A")){
                      isScore = quiz.getScoreA();
                      isAnswe = quidAnswers;
                    }
                    if(quidAnswers.equals("B")){
                      isScore = quiz.getScoreB();
                      isAnswe = quidAnswers;
                    }
                    if(quidAnswers.equals("C")){
                      isScore = quiz.getScoreC();
                      isAnswe = quidAnswers;
                    }
                    if(quidAnswers.equals("D")){
                      isScore = quiz.getScoreD();
                      isAnswe = quidAnswers;
                    }
                    if(quidAnswers.equals("E")){
                      isScore = quiz.getScoreE();
                      isAnswe = quidAnswers;
                    }
                    if(quidAnswers.equals("F")){
                      isScore = quiz.getScoreF();
                      isAnswe = quidAnswers;
                    }
                  }
                }
              }
              score = score + isScore + ",";
              answe = answe + isAnswe + ",";
              memo = memo  +  isMemo + ",";
            }
          }
          if(dataList.size() > 0){
            data = dataList.get(0);
            String oldScore = data.getScore();//得到以前的考核分数
            String oldAnswer = data.getAnswer();//得到以前的考核的答案            String oldMemo = data.getMemo();//得到以前的考核批准；            if(YHUtility.isNullorEmpty(oldScore)){
              oldScore = "";
            }
            if(YHUtility.isNullorEmpty(oldAnswer)){
              oldAnswer = "";
            }
            if(YHUtility.isNullorEmpty(oldMemo)){
              oldMemo = "";
            }
            String[] oldS = getStr(oldScore, Integer.parseInt(currPage), pageSize);//取得前面字段和后面字段            String[] oldA = getStr(oldAnswer, Integer.parseInt(currPage), pageSize);//取得前面字段和后面字段            String[] oldM = getStr(oldMemo, Integer.parseInt(currPage), pageSize);//取得前面字段和后面字段            //更新data表  
            if(YHUtility.isNullorEmpty(oldS[0])){
              if(!YHUtility.isNullorEmpty(oldS[1])){
                if(score.endsWith(",")){
                  score = score + oldS[1];
                }else{
                  score = score + ","+ oldS[1];
                }
              }
            }else{
              if(!YHUtility.isNullorEmpty(oldS[1])){
                if(score.endsWith(",")){
                  score = oldS[0] + score + oldS[1];
                }else{
                  score = oldS[0] + score + ","+ oldS[1];
                }
              }else{
                score = oldS[0] + score;
              }
            }
            if(YHUtility.isNullorEmpty(oldA[0])){
              if(!YHUtility.isNullorEmpty(oldA[1])){
                if(answe.endsWith(",")){
                  answe =  answe + oldA[1];
                }else{
                  answe =  answe + ","+ oldA[1];
                }  
              }
            }else{
              if(!YHUtility.isNullorEmpty(oldA[1])){
                if(answe.endsWith(",")){
                  answe = oldA[0] + answe + oldA[1];
                }else{
                  answe = oldA[0] + answe + ","+ oldA[1];
                }
              }else{
                answe = oldA[0] + answe;
              }
            }
            if(YHUtility.isNullorEmpty(oldM[0])){
              if(!YHUtility.isNullorEmpty(oldM[1])){
                if(memo.endsWith(",")){
                  memo =  memo + oldM[1];
                }else{
                  memo =  memo + ","+ oldM[1];
                }  
              }
            }else{
              if(!YHUtility.isNullorEmpty(oldM[1])){
                if(memo.endsWith(",")){
                  memo = oldM[0] + memo + oldM[1];
                }else{
                  memo = oldM[0] + memo + ","+ oldM[1];
                }
              }else{
                memo = oldM[0] + memo;
              }
            }
           dataLogic.updateDate2(dbConn, data.getSeqId() + "", score, answe, memo, checkEnd);  
          }else{
            data = new YHScoreShow();
            data.setParticipant(userId);
            data.setRankman(user.getSeqId() + "");
            data.setRankDate(new Date());
            data.setAnswer(answe);
            data.setMemo(memo);
            data.setScore(score);
            data.setGroupId(Integer.parseInt(groupId));
            data.setCheckFlag(checkFlag);
            data.setCheckEnd(checkEnd);
            data.setScoreTime(ymd);
            dataLogic.addData2(dbConn, data);
          }
        }
      
      String dataStr = "{isSubmit:\"" + checkEnd + "\"}";
      request.setAttribute(YHActionKeys.RET_DATA,dataStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String[] getStr(String str,int currPage,int pageSize){
    String[] newStr = new String[2];
    String newPre = "";
    String newEnd= "";
    if(!YHUtility.isNullorEmpty(str)){
      String[] strArray = str.split(",");
      if(strArray.length == 0){
        strArray = new String[str.length()];
      }
      int pageTemp = 0;
      pageTemp = (currPage-1)*pageSize;
      if(pageTemp>strArray.length){
        pageTemp = strArray.length;
      }
      for (int i = 0; i < pageTemp; i++) {
        String perTest = strArray[i];
        if(YHUtility.isNullorEmpty(perTest)){
          perTest = "";
        }
        newPre = newPre + perTest + ",";
      }
      for (int i = (pageTemp+pageSize); i <strArray.length; i++) {
        String endTest = strArray[i];
        if(YHUtility.isNullorEmpty(endTest)){
          endTest = "";
        }
        newEnd = newEnd  + endTest + ",";
      }
    }
    newStr[0]= newPre;
    newStr[1]= newEnd;
    return newStr;
  }
  
  /**
   * 获取手填考核数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getScoreDataStr(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String flowIdStr = request.getParameter("groupId");
      String userId = request.getParameter("userId");
      int groupId = 0;
      if(!YHUtility.isNullorEmpty(flowIdStr)){
        groupId = Integer.parseInt(flowIdStr);
      }
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"GROUP_ID = " + groupId + " and RANKMAN = '" + person.getSeqId() + "' and PARTICIPANT = '" + userId + "'" };
      List funcList = new ArrayList();
      funcList.add("scoreShow");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_SCORE_SHOW"));
      for(Map ms : list){
        String memo = (String) ms.get("memo");
        memo = YHUtility.encodeSpecial(YHUtility.null2Empty(memo));
        sb.append("{");
        sb.append("seqId:\"" + ms.get("seqId") + "\"");
        sb.append(",score:\"" + (ms.get("score") == null ? "" : ms.get("score")) + "\"");
        sb.append(",memo:\"" + memo + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1); 
      if(list.size() == 0){
        sb = new StringBuffer("[");
      }
      sb.append("]");
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
  
  /**
   * 添加考核数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addScoreData(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String groupIdStr = request.getParameter("groupId");
      String participant = request.getParameter("participant");
      String checkFlag = request.getParameter("checkFlag");
      String score = request.getParameter("score");
      String memo = request.getParameter("memo");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      String rankDate = YHUtility.getCurDateTimeStr();
      int groupId = 0;
      if(!YHUtility.isNullorEmpty(groupIdStr)){
        groupId = Integer.parseInt(groupIdStr);
      }
      this.logic.addScoreData(dbConn, groupId, String.valueOf(person.getSeqId()), participant, score, memo, checkFlag, year, month);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      //request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 修改考核数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateScoreData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String groupIdStr = request.getParameter("groupId");
      String participant = request.getParameter("participant");
      String checkFlag = request.getParameter("checkFlag");
      String score = request.getParameter("score");
      String memo = request.getParameter("memo");
      int groupId = 0;
      if(!YHUtility.isNullorEmpty(groupIdStr)){
        groupId = Integer.parseInt(groupIdStr);
      }
      this.logic.updateScoreDate(dbConn, groupId, score, memo, participant, person, checkFlag);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"添加成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取查看工作日志、工作安排--wyw
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getGroupRefer(HttpServletRequest request,HttpServletResponse response) throws Exception{
    String groupIdStr = request.getParameter("groupId");
    int groupId = 0;
    if (!YHUtility.isNullorEmpty(groupIdStr)) {
      groupId = Integer.parseInt(groupIdStr);
    }
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = this.logic.getGroupReferLogic(dbConn,groupId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"获取数据成功");
      request.setAttribute(YHActionKeys.RET_DATA,data);
      
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
    
  }
  
  public String getSelectOption(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    String userIdStr = request.getParameter("userId"); 
    if (YHUtility.isNullorEmpty(userIdStr)) { 
      userIdStr = ""; 
    } 
    Connection dbConn; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR); 
      dbConn = requestDbConn.getSysDbConn(); 
      String data = this.logic.getScoreGroupSelect(dbConn, userIdStr); 
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！"); 
      request.setAttribute(YHActionKeys.RET_DATA, data); 
    } catch (Exception e) { 
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
    return "/core/inc/rtjson.jsp"; 
  }
  
  /**
   * 获取groupId
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getGroupId(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String roleId = request.getParameter("roleId");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = this.logic.getGroupId(dbConn, roleId);
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
   * 获取个人考核项目分数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getScoreShow(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userId = request.getParameter("userId");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      double directorScore = this.logic.getScoreShow(dbConn, year, month, userId);
      String data = String.valueOf(directorScore);
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
   * 判断人员考核数据录入 时是否有数据 如果有data=1,没有data=0
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getOperationFlag(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String userId = request.getParameter("userId");
      String groupIdStr = request.getParameter("groupId");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      int groupId = 0;
      if(!YHUtility.isNullorEmpty(groupIdStr)){
        groupId = Integer.parseInt(groupIdStr);
      }
      boolean bool = this.logic.getOperationFlag(dbConn, person, userId, groupId, year, month);
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
   * 获取是选择考核还是手动填写考核项目 1-选择，0-手动填写
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getGroupFlag(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String groupIdStr = request.getParameter("groupId");
      int groupId = 0;
      if(!YHUtility.isNullorEmpty(groupIdStr)){
        groupId = Integer.parseInt(groupIdStr);
      }
      String data = this.logic.getGroupFlag(dbConn, groupId);
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
   * 获取是选择考核还是手动填写考核项目 1-选择，0-手动填写
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCheckEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String groupIdStr = request.getParameter("groupId");
      String userIdStr = request.getParameter("userId");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      int groupId = 0;
      if(!YHUtility.isNullorEmpty(groupIdStr)){
        groupId = Integer.parseInt(groupIdStr);
      }
      if(YHUtility.isNullorEmpty(userIdStr)){
        userIdStr = "0";
      }
      String data = this.logic.getCheckEnd(dbConn, groupId, userIdStr, year, month);
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
   * 获取考核指标集标题
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getScoreGroupName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String groupId = request.getParameter("seqId");
      int seqId = 0;
      if(!YHUtility.isNullorEmpty(groupId)){
        seqId = Integer.parseInt(groupId);
      }
      String data = this.logic.getScoreGroupName(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + YHUtility.encodeSpecial(data) + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
