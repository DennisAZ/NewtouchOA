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

public class YHScoreDataAct {
  public static final String attachmentFolder = "scoreData";
  private YHScoreDataLogic logic = new YHScoreDataLogic();
  
  public String getScoreFlowData(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String seqId = request.getParameter("flowId");
      YHORM orm = new YHORM();
      YHScoreFlow flow = (YHScoreFlow) orm.loadObjSingle(dbConn, YHScoreFlow.class, Integer.parseInt(seqId));
      String userStr = flow.getParticipant();
      String flowFlag = flow.getFlowFlag();
      String userIdStr = "";
      String moduleId = "";
      int privNoFlag = 1;
      YHMyPriv mp = new YHMyPriv();
      String userIdStrFunc = "";
      mp = YHPrivUtil.getMyPriv(dbConn, person, moduleId, privNoFlag);
      if("0".equals(flowFlag)){
        if(String.valueOf(person.getSeqId()).equals(userStr)){
          userIdStr = "0";
        }else{
          userIdStr = userStr;
        }
      }else{
        HashMap map1 = null;
        List<Map> list1 = new ArrayList();
        String[] filters1 = new String[]{"SEQ_ID IN (" + userStr + ")" };
        List funcList1 = new ArrayList();
        funcList1.add("person");
        map1 = (HashMap)orm.loadDataSingle(dbConn, funcList1, filters1);
        list1.addAll((List<Map>) map1.get("PERSON"));
        for(Map ms : list1){
          String deptId = String.valueOf(ms.get("deptId"));
          String userId = String.valueOf(ms.get("seqId"));
          if(String.valueOf(person.getSeqId()).equals(userId)){
            continue;
          }
          if (YHPrivUtil.isDeptPriv(dbConn, Integer.parseInt(deptId), mp, person)) {
            if(!"".equals(userIdStr)){
              userIdStr += ",";
            }
            userIdStr += userId;
          }
        }
      }
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      if(YHUtility.isNullorEmpty(userIdStr)){
        userIdStr = "0";
      }
      String[] filters = new String[]{"SEQ_ID IN (" + userIdStr + ")" };
      List funcList = new ArrayList();
      funcList.add("person");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("PERSON"));
      for(Map ms : list){
        String bool = "";
        boolean data = this.logic.getOperationFlag(dbConn, person, String.valueOf(ms.get("seqId")), Integer.parseInt(seqId));
        if(data){
          bool = "1";
        }else{
          bool = "0";
        }
        sb.append("{");
        sb.append("seqId:\"" + ms.get("seqId") + "\"");
        sb.append(",userId:\"" + (ms.get("userId") == null ? "" : ms.get("userId")) + "\"");
        sb.append(",userName:\"" + (ms.get("userName") == null ? "" : ms.get("userName")) + "\"");
        sb.append(",deptId:\"" + (ms.get("deptId") == null ? "" : ms.get("deptId")) + "\"");
        sb.append(",userPriv:\"" + (ms.get("userPriv") == null ? "" : ms.get("userPriv")) + "\"");
        sb.append(",img:\"" + bool + "\"");
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
      String groupId = request.getParameter("groupId");
      YHORM orm = new YHORM();

      ArrayList<YHPerson> persons = new ArrayList<YHPerson>();
      String userIdStr = "";
      String moduleId = "";
      int privNoFlag = 1;
      YHMyPriv mp = new YHMyPriv();
      String userIdStrFunc = "";
      YHOrgSelect2Logic osl = new YHOrgSelect2Logic();
      mp = YHPrivUtil.getMyPriv(dbConn, person, moduleId, privNoFlag);
      if (person.getDeptId() != 0) {
        persons = osl.getDeptUser(dbConn, person.getDeptId() , false);
      }
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
        sb.append(",img:\"" + "1" + "\"");
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
   * 新建--》取对象---syl
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String addData(HttpServletRequest request,
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
      int  seqId = 0;
      String dataStr = "";
      if(YHUtility.isInteger(flowId)){
        //先判断是否已经考核过了
        YHScoreDataLogic dataLogic = new YHScoreDataLogic();
        YHScoreData data = null;
        String[] str = {"FLOW_ID=" + flowId ,"PARTICIPANT = '" + userId+"'" };
        List<YHScoreData> dataList = dataLogic.selectData(dbConn, str);
        if( dataList.size()>0){
          data = dataList.get(0);
          seqId = data.getSeqId();
        }else{
          data = new YHScoreData();
          data.setFlowId(Integer.parseInt(flowId));
          data.setParticipant(user.getSeqId()+"");
          data.setRankman(user.getSeqId()+"");
          data.setRankDate(new Date());
          data.setFlowId(Integer.parseInt(flowId));
          seqId =  dataLogic.addData(dbConn, data);
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
   * 提交上下页--记录考核信息
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String scoreData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String quizIds = request.getParameter("quizIds");//选中的ID
      String quizIdpage = request.getParameter("quizIdpage");//当页的所有题目seqId字符串
      String seqId = request.getParameter("seqId");//咱没用上
      String isSubmit =  request.getParameter("type");
      String currPage = request.getParameter("currPage");
      
      String flowId = request.getParameter("flowId");
      String userId = request.getParameter("userId");
      String checkFlag = request.getParameter("checkFlag");
      String dataStr = "";
      int pageSize = 5;
      if(YHUtility.isInteger(flowId)&& YHUtility.isInteger(userId) ){
        //先判断是否已经考核过了
        YHScoreDataLogic dataLogic = new YHScoreDataLogic();
        YHScoreAnswerLogic answerLogic = new YHScoreAnswerLogic();
        YHScoreData data = null;
        String[] str = {"FLOW_ID=" + flowId ,"PARTICIPANT = '" + userId+"'" };
        List<YHScoreData> dataList = dataLogic.selectData(dbConn, str);
        
        if(YHUtility.isNullorEmpty(quizIds)){
          quizIds = "";
        }
        if(YHUtility.isNullorEmpty(isSubmit)){
          isSubmit = "";
        }
        if(!YHUtility.isInteger(currPage)){
          currPage = "1";
        }
        String score = "";
        String answe = "";
        String memo = "";
   
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
        if( dataList.size()>0){
          data = dataList.get(0);
          String oldScore = data.getScore();//得到以前的考核分数
          String oldAnswer = data.getAnswer();//得到以前的考核的答案          String oldMemo = data.getMemo();//得到以前的考核批准；
          if(YHUtility.isNullorEmpty(oldScore)){
            oldScore = "";
          }
          if(YHUtility.isNullorEmpty(oldAnswer)){
            oldAnswer = "";
          }
          if(YHUtility.isNullorEmpty(oldMemo)){
            oldMemo = "";
          }
          String[] oldS = getStr(oldScore, Integer.parseInt(currPage), pageSize);//取得前面字段和后面字段          String[] oldA = getStr(oldAnswer, Integer.parseInt(currPage), pageSize);//取得前面字段和后面字段          String[] oldM = getStr(oldMemo, Integer.parseInt(currPage), pageSize);//取得前面字段和后面字段          
          //更新data表
          if(YHUtility.isNullorEmpty(oldS[0])){
            if(!YHUtility.isNullorEmpty(oldS[1])){
              if(score.endsWith(",")){
                score =  score +oldS[1];
              }else{
                score =  score + ","+oldS[1];
              }
            }
          }else{
            if(!YHUtility.isNullorEmpty(oldS[1])){
              if(score.endsWith(",")){
                score = oldS[0] + score +oldS[1];
              }else{
                score = oldS[0] + score + ","+oldS[1];
              }
            }else{
              score = oldS[0] + score;
            }
          }
          
          if(YHUtility.isNullorEmpty(oldA[0])){
            if(!YHUtility.isNullorEmpty(oldA[1])){
              if(answe.endsWith(",")){
                answe =  answe +oldA[1];
              }else{
                answe =  answe + ","+oldA[1];
              }  
            }
          }else{
            if(!YHUtility.isNullorEmpty(oldA[1])){
              if(answe.endsWith(",")){
                answe = oldA[0] + answe +oldA[1];
              }else{
                answe = oldA[0] + answe + ","+oldA[1];
              }
            }else{
              answe = oldA[0] + answe;
            }
          }
          if(YHUtility.isNullorEmpty(oldM[0])){
            if(!YHUtility.isNullorEmpty(oldM[1])){
              if(memo.endsWith(",")){
                memo =  memo +oldM[1];
              }else{
                memo =  memo + ","+oldM[1];
              }  
            }
          }else{
            if(!YHUtility.isNullorEmpty(oldM[1])){
              if(memo.endsWith(",")){
                memo = oldM[0] + memo +oldM[1];
              }else{
                memo = oldM[0] + memo + ","+oldM[1];
              }
            }else{
              memo = oldM[0] + memo;
            }
          }
         dataLogic.updateDate(dbConn, data.getSeqId() + "", score, answe,memo);  
        }else{
          data = new YHScoreData();
          data.setFlowId(Integer.parseInt(flowId));
          data.setParticipant(userId);
          data.setRankman(user.getSeqId()+"");
          data.setRankDate(new Date());
          data.setAnswer(answe);
          data.setMemo(memo);
          data.setScore(score);
          data.setFlowId(Integer.parseInt(flowId));
          data.setCheckFlag(checkFlag);
          dataLogic.addData(dbConn, data);
        }
      }
      String data = "{isSubmit:\"" + isSubmit + "\"}";
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
      String quizIdpage = request.getParameter("quizIdpage");//当页的所有题目seqId字符串
      String isSubmit =  request.getParameter("type");
      String currPage = request.getParameter("currPage");
      
      String groupId = request.getParameter("groupId");
      String userId = request.getParameter("userId");
      String checkFlag = request.getParameter("checkFlag");
      String year = request.getParameter("year");
      String month = request.getParameter("month");
      int pageSize = 5;
      if(YHUtility.isInteger(groupId)&& YHUtility.isInteger(userId) ){
        //先判断是否已经考核过了
        YHScoreDataLogic dataLogic = new YHScoreDataLogic();
        YHScoreAnswerLogic answerLogic = new YHScoreAnswerLogic();
        YHScoreShow data = null;
        String[] str = {"GROUP_ID=" + groupId ,"PARTICIPANT = '" + userId+"'" };
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
        String score = "";
        String answe = "";
        String memo = "";
   
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
        if( dataList.size()>0){
          data = dataList.get(0);
          String oldScore = data.getScore();//得到以前的考核分数
          String oldAnswer = data.getAnswer();//得到以前的考核的答案
          String oldMemo = data.getMemo();//得到以前的考核批准；

          if(YHUtility.isNullorEmpty(oldScore)){
            oldScore = "";
          }
          if(YHUtility.isNullorEmpty(oldAnswer)){
            oldAnswer = "";
          }
          if(YHUtility.isNullorEmpty(oldMemo)){
            oldMemo = "";
          }
          String[] oldS = getStr(oldScore, Integer.parseInt(currPage), pageSize);//取得前面字段和后面字段
          String[] oldA = getStr(oldAnswer, Integer.parseInt(currPage), pageSize);//取得前面字段和后面字段
          String[] oldM = getStr(oldMemo, Integer.parseInt(currPage), pageSize);//取得前面字段和后面字段
          
          //更新data表

          if(YHUtility.isNullorEmpty(oldS[0])){
            if(!YHUtility.isNullorEmpty(oldS[1])){
              if(score.endsWith(",")){
                score =  score +oldS[1];
              }else{
                score =  score + ","+oldS[1];
              }
            }
          }else{
            if(!YHUtility.isNullorEmpty(oldS[1])){
              if(score.endsWith(",")){
                score = oldS[0] + score +oldS[1];
              }else{
                score = oldS[0] + score + ","+oldS[1];
              }
            }else{
              score = oldS[0] + score;
            }
          }
          
          if(YHUtility.isNullorEmpty(oldA[0])){
            if(!YHUtility.isNullorEmpty(oldA[1])){
              if(answe.endsWith(",")){
                answe =  answe +oldA[1];
              }else{
                answe =  answe + ","+oldA[1];
              }  
            }
          }else{
            if(!YHUtility.isNullorEmpty(oldA[1])){
              if(answe.endsWith(",")){
                answe = oldA[0] + answe +oldA[1];
              }else{
                answe = oldA[0] + answe + ","+oldA[1];
              }
            }else{
              answe = oldA[0] + answe;
            }
          }
          if(YHUtility.isNullorEmpty(oldM[0])){
            if(!YHUtility.isNullorEmpty(oldM[1])){
              if(memo.endsWith(",")){
                memo =  memo +oldM[1];
              }else{
                memo =  memo + ","+oldM[1];
              }  
            }
          }else{
            if(!YHUtility.isNullorEmpty(oldM[1])){
              if(memo.endsWith(",")){
                memo = oldM[0] + memo +oldM[1];
              }else{
                memo = oldM[0] + memo + ","+oldM[1];
              }
            }else{
              memo = oldM[0] + memo;
            }
          }
         dataLogic.updateDate2(dbConn, data.getSeqId() + "", score, answe,memo);  
        }else{
          data = new YHScoreShow();
          data.setParticipant(userId);
          data.setRankman(user.getSeqId()+"");
          data.setRankDate(new Date());
          data.setAnswer(answe);
          data.setMemo(memo);
          data.setScore(score);
          data.setScoreTime(year + "-" + month);
          data.setGroupId(Integer.parseInt(groupId));
          data.setCheckFlag(checkFlag);
          dataLogic.addData2(dbConn, data);
        }
      }
      String data = "{isSubmit:\"" + isSubmit + "\"}";
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
      String flowId = request.getParameter("flowId");
      boolean bool = this.logic.getOperationFlag(dbConn, person, userId, Integer.parseInt(flowId));
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
      String flowIdStr = request.getParameter("flowId");
      String participant = request.getParameter("participant");
      String checkFlag = request.getParameter("checkFlag");
      String score = request.getParameter("score");
      String memo = request.getParameter("memo");
      String rankDate = YHUtility.getCurDateTimeStr();
      int flowId = 0;
      if(!YHUtility.isNullorEmpty(flowIdStr)){
        flowId = Integer.parseInt(flowIdStr);
      }
      this.logic.addScoreData(dbConn, flowId, String.valueOf(person.getSeqId()), participant, score, memo, checkFlag);
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
      String flowIdStr = request.getParameter("flowId");
      String userId = request.getParameter("userId");
      int flowId = 0;
      if(!YHUtility.isNullorEmpty(flowIdStr)){
        flowId = Integer.parseInt(flowIdStr);
      }
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"FLOW_ID = " + flowId + " and RANKMAN = '" + person.getSeqId() + "' and PARTICIPANT = '" + userId + "'" };
      List funcList = new ArrayList();
      funcList.add("scoreData");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_SCORE_DATA"));
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
      String flowIdStr = request.getParameter("flowId");
      String participant = request.getParameter("participant");
      String checkFlag = request.getParameter("checkFlag");
      String score = request.getParameter("score");
      String memo = request.getParameter("memo");
      int flowId = 0;
      if(!YHUtility.isNullorEmpty(flowIdStr)){
        flowId = Integer.parseInt(flowIdStr);
      }
      this.logic.updateScoreDate(dbConn, flowId, score, memo, participant, person, checkFlag);
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
  
  /**
   * 获取日程数据ByUserId 和开始时间，结束时间---syl
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCalendar(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //String beginDate = request.getParameter("beginDate");
      //String endDate = request.getParameter("endDate");
      String date = request.getParameter("date");
      String userId = request.getParameter("userId");
      int flowId = 0;
      String data= "[";
      if(YHUtility.isInteger(userId)){
        YHCalendarLogic calendarLogic = new YHCalendarLogic();
        //SELECT * from oa_schedule where USER_ID='$USER_ID' and CAL_TYPE!='2' and to_days(CAL_TIME)=to_days('$YEAR-$MONTH-$DAY') order by CAL_TIME";
        String[] str = {"USER_ID = '" + userId + "'","CAL_TYPE <> '2'",YHDBUtility.getDateFilter("CAL_TIME", date, ">="),YHDBUtility.getDateFilter("CAL_TIME", date + " 23:59:59", "<=") + " order by CAL_TIME"};
        List<YHCalendar> calendarList = calendarLogic.selectCalendarByList(dbConn, str);
        for (int i = 0; i < calendarList.size(); i++) {
          YHCalendar calendar = calendarList.get(i);
          YHPersonLogic tpl = new YHPersonLogic();
          String managerName =  tpl.getNameBySeqIdStr(calendar.getManagerId(),dbConn);
          if (managerName != null && !managerName.equals("")) {
            managerName = managerName.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\r", "").replace("\n", "");
          }
          data = data+ YHFOM.toJson(calendar).substring(0,YHFOM.toJson(calendar).length() - 1) + ",managerName:\""+ managerName + "\"},";
        }
        if(calendarList.size() > 0){
          data = data.substring(0, data.length() -1);
        }
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  /**
   * 获取用户名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getUserName(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userId = request.getParameter("userId");
      String userName = "";
      if(YHUtility.isInteger(userId)){
        YHPersonLogic tpl = new YHPersonLogic();
        userName = tpl.getNameBySeqIdStr(userId,dbConn);
      }
      String data = "{userName:\" "+ userName +"\",loginUserId:\""+person.getSeqId()+"\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getItemName(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String groupId = request.getParameter("groupId");

      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      List list = this.logic.getItemName(dbConn, groupId);
      for(int i = 0; i < list.size(); i++){
        sb.append("{");
        sb.append("itemName:\"" + YHUtility.encodeSpecial(String.valueOf(list.get(i))) + "\"");
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
  
  public String getScoreDataList(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
//      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String flowIdStr = request.getParameter("flowId");
      int flowId = 0;
      if(!YHUtility.isNullorEmpty(flowIdStr)){
        flowId = Integer.parseInt(flowIdStr);
      }
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      boolean isHaveFlag = false;
      List<YHScoreData> list = this.logic.scoreDataList(dbConn, flowId);
      for(int i = 0; i < list.size(); i++){
      	YHPerson persons = this.logic.showPerson(dbConn,list.get(i).getRankman());
      	if (persons!=null) {
      	  sb.append("{");
          sb.append("deptName:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(list.get(i).getAnswer())) + "\"");
          sb.append(",userName:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(list.get(i).getParticipant())) + "\"");
          sb.append(",userPriv:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(list.get(i).getCheckFlag())) + "\"");
          sb.append(",rankMan:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(persons.getUserName())) + "\"");
          sb.append(",privName:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(persons.getUserPriv())) + "\"");
          sb.append(",memo:\"" + YHUtility.encodeSpecial(YHUtility.null2Empty(list.get(i).getMemo())) + "\"");
          sb.append(",score:\"" + list.get(i).getScore() + "\"");
          sb.append("},");
          isHaveFlag = true;
				}
      }
      if (isHaveFlag) {
      	sb = sb.deleteCharAt(sb.length() - 1); 
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
  
}
