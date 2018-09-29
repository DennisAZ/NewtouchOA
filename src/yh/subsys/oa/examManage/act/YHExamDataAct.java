package yh.subsys.oa.examManage.act;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.examManage.data.YHExamData;
import yh.subsys.oa.examManage.data.YHExamFlow;
import yh.subsys.oa.examManage.data.YHExamPaper;
import yh.subsys.oa.examManage.data.YHExamQuiz;
import yh.subsys.oa.examManage.logic.YHExamDataLogic;
import yh.subsys.oa.examManage.logic.YHExamFlowLogic;
import yh.subsys.oa.examManage.logic.YHPageData;

public class YHExamDataAct {
  /**
   * 新建
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
      String paperTimes = request.getParameter("paperTimes");
      String userId = request.getParameter("userId");
      if(!YHUtility.isInteger(userId)){
        userId = user.getSeqId() + "";
      }
      if(!YHUtility.isInteger(paperTimes)){
        paperTimes = "0";
      }
      Date curDate = new Date();
      Calendar cal = Calendar.getInstance(); 
      cal.add(Calendar.MINUTE, Integer.parseInt(paperTimes));
      //System.out.println(YHUtility.getDateTimeStr(cal.getTime()));
      int  seqId = 0;
      String dataStr = "";
      if(YHUtility.isInteger(flowId)){
        //先判断是否已经考试了
        YHExamDataLogic dataLogic = new YHExamDataLogic();
        YHExamData data = null;
        String[] str = {"FLOW_ID=" + flowId ,"PARTICIPANT = '" + userId+"'" };
        List<YHExamData> dataList = dataLogic.selectData(dbConn, str);
        if( dataList.size()>0){
          data = dataList.get(0);
          seqId = data.getSeqId();
        }else{
          data = new YHExamData();
          data.setFlowId(Integer.parseInt(flowId));
          data.setStartTime(new Date());
          data.setEndTime(cal.getTime());
          data.setExamed("0");
          data.setParticipant(user.getSeqId()+"");
          data.setRankDate(new Date());
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
   * 得到在先考试的一条记录ByFlowId and PARTICIPANT考试人id
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
      if(!YHUtility.isNullorEmpty(flowId)){
        YHExamDataLogic dataLogic = new YHExamDataLogic();
        String[] str = {"FLOW_ID=" + flowId ,"PARTICIPANT = '" + userId+"' order by RANK_DATE desc" };
        YHExamFlowLogic flowLogic = new YHExamFlowLogic();
        YHExamFlow flow = flowLogic.showFlow(dbConn, flowId);//查出考试信息
        int quizTotal = 1;
        int paperGrade = 0;
        if(flow !=null){
          YHExamPaper paper = dataLogic.getParerBySeqId(dbConn, flow.getPaperId());//查询试卷
          if(paper != null){
            paperGrade = paper.getPaperGrade();
            if(!YHUtility.isNullorEmpty(paper.getQuestionsList())){
              quizTotal = paper.getQuestionsList().split(",").length;
            }
          }
        }
        List<YHExamData> dataList = dataLogic.selectData(dbConn, str);
        int userScore = 0;
        for (int i = 0; i < dataList.size(); i++) { 
          if(!YHUtility.isNullorEmpty(dataList.get(0).getScore())){
            String[] scoreArray = dataList.get(0).getScore().split(",");
            int count = 0;
            for (int j = 0; j < scoreArray.length; j++) {
              if(scoreArray[j].equals("1")){
                count++;
              }
            }
            userScore = count*paperGrade/quizTotal;
          }
          data = data + YHFOM.toJson(dataList.get(i)).substring(0, YHFOM.toJson(dataList.get(i)).length()-1) + ",userScore:\"" + userScore +"\"},";
        }
        if( dataList.size()>0){
          data = data.substring(0, data.length()-1);
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
   * 得到在线考试分页
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getExmaOnline(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String flowId = request.getParameter("flowId");
      String currPage = request.getParameter("currPage");
      if(YHUtility.isNullorEmpty(currPage)){
        currPage = "1";
      }
      String data = "[";
      String pageInfo = "";
      if(YHUtility.isInteger(flowId)){
        YHExamFlowLogic flowLogic = new YHExamFlowLogic();
        YHExamDataLogic dataLogic = new YHExamDataLogic();
        YHExamFlow flow = flowLogic.showFlow(dbConn, flowId);//查出考试信息
        if(flow !=null){
          
          YHExamPaper paper = dataLogic.getParerBySeqId(dbConn, flow.getPaperId());//查询试卷
          if(paper != null){
            String QUIZSeqId = paper.getQuestionsList();
            if(!YHUtility.isNullorEmpty(QUIZSeqId)){
              if(QUIZSeqId.endsWith(",")){
                QUIZSeqId = QUIZSeqId.substring(0, QUIZSeqId.length()-1);
              }
              //得到试题的总数
              int count =  dataLogic.selectQuizCount(dbConn, QUIZSeqId);//QUIZSeqId.split(",").length;//
              YHPageData pageData = new YHPageData(5,count,Integer.parseInt(currPage));
              if (count > 0) {
                List DataAllList = dataLogic.selectQuizSeqId(dbConn, QUIZSeqId);
                String seqIds = "";
                for (long i = pageData.getFirstResult(); i < pageData.getLastResult(); i++) {
                  seqIds = seqIds + DataAllList.get((int)i) + ",";
                }
                seqIds = seqIds.substring(0, seqIds.length() - 1);
                String[] str = {"SEQ_ID in(" + seqIds + ")"};
                List<YHExamQuiz> quizList  = dataLogic.getQuiz(dbConn, str);//得到试题列表
                for (int i = 0; i < quizList.size(); i++) {
                  data = data + YHFOM.toJson(quizList.get(i)) + ",";
                }
                if(quizList.size()>0){
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
   * 得到在线考试分页---试题
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getExmaQuiz(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String flowId = request.getParameter("flowId");
      String currPage = request.getParameter("currPage");
      if(YHUtility.isNullorEmpty(currPage)){
        currPage = "1";
      }
      String data = "[";
      String pageInfo = "";
      String seqId = "";
      if(YHUtility.isInteger(flowId)){
        YHExamFlowLogic flowLogic = new YHExamFlowLogic();
        YHExamDataLogic dataLogic = new YHExamDataLogic();
        YHExamFlow flow = flowLogic.showFlow(dbConn, flowId);//查出考试信息
        if(flow !=null){
          
          YHExamPaper paper = dataLogic.getParerBySeqId(dbConn, flow.getPaperId());//查询试卷
          if(paper != null){
            String QUIZSeqId = paper.getQuestionsList();
            if(!YHUtility.isNullorEmpty(QUIZSeqId)){
              if(QUIZSeqId.endsWith(",")){
                QUIZSeqId = QUIZSeqId.substring(0, QUIZSeqId.length()-1);
              }
              //得到试题的总数
              int count =  dataLogic.selectQuizCount(dbConn, QUIZSeqId);//QUIZSeqId.split(",").length;//
              YHPageData pageData = new YHPageData(5,count,Integer.parseInt(currPage));
              if (count > 0) {
                List DataAllList = dataLogic.selectQuizSeqId(dbConn, QUIZSeqId);
                String seqIds = "";
                for (long i = pageData.getFirstResult(); i < pageData.getLastResult(); i++) {
                  seqIds = seqIds + DataAllList.get((int)i) + ",";
                }
                seqIds = seqIds.substring(0, seqIds.length() - 1);
                String[] str = {"SEQ_ID in(" + seqIds + ")  order by SEQ_ID " };
                List<YHExamQuiz> quizList  = dataLogic.getQuiz(dbConn, str);//得到试题列表
                for (int i = 0; i < quizList.size(); i++) {
                  data = data + YHFOM.toJson(quizList.get(i)) + ",";
                }
                if(quizList.size()>0){
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
       
      }
      if(pageInfo.equals("")){
        pageInfo = "{}";
      }
      data = data + "]";
      String allData = "{pageInfo:" + pageInfo + ",quizList:" + data + "}";
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
   * 得到在线考试分页---提交上下页
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String ExmaData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String quizIds = request.getParameter("quizIds");
      String quizIdpage = request.getParameter("quizIdpage");
      String seqId = request.getParameter("seqId");
      String isSubmit =  request.getParameter("type");
      String currPage = request.getParameter("currPage");
      int pageSize = 5;
      if(YHUtility.isNullorEmpty(quizIds)){
        quizIds = "";
      }
      if(YHUtility.isNullorEmpty(isSubmit)){
        isSubmit = "";
      }
      if(!YHUtility.isInteger(currPage)){
        currPage = "1";
      }
      if(YHUtility.isInteger(seqId)){      
        YHExamDataLogic dataLogic = new YHExamDataLogic();
        YHExamData data = dataLogic.selectDataById(dbConn, seqId);//得到考试
        String oldScore = data.getScore();//得到以前的考试对错
        String oldAnswer = data.getAnswer();//得到以前的考试答题
        if(YHUtility.isNullorEmpty(oldScore)){
          oldScore = "";
        }
        if(YHUtility.isNullorEmpty(oldAnswer)){
          oldAnswer = "";
        }
        String[] oldS = getStr(oldScore, Integer.parseInt(currPage), pageSize);//取得前面字段和后面字段
        String[] oldA = getStr(oldAnswer, Integer.parseInt(currPage), pageSize);//取得前面字段和后面字段
        if(data!= null){
          String score = "";
          String answe = "";
          if(!YHUtility.isNullorEmpty(quizIdpage)){
            if(quizIdpage.endsWith(",")){
              quizIdpage = quizIdpage.substring(0, quizIdpage.length()-1);
            }
            String[] str = {"SEQ_ID in(" + quizIdpage + ")"};
            List<YHExamQuiz> quizList  = dataLogic.getQuiz(dbConn, str);//得到试题列表
            String[] quidArray = quizIds.split(",");//选中的ID
            for (int i = 0; i < quizList.size(); i++) {
              YHExamQuiz quiz = quizList.get(i);
              String quizAnswers = quiz.getAnswers();
              String isScore = "0";
              String isAnswe = " ";
              if(YHUtility.isNullorEmpty(quizAnswers)){
                quizAnswers = "";
              }
              String quizSeqId = quiz.getSeqId()+"";
              for (int j = 0; j < quidArray.length; j++) {
                String quidStr = quidArray[j];//seqId_A
                String[] quidStrArray =  quidStr.split("_");
                if(quidStrArray.length>=2){
                  String quidSeqId = quidStrArray[0];//得到选中的试题ID
                  String quidAnswers = quidStrArray[1];//得到选中的试题的多填写的答案
                  if(quizSeqId.equals(quidSeqId)&& !quidAnswers.equals("")){
                    if(quizAnswers.equals(quidAnswers)){
                      isScore = "1";//是正确答案
                    }
                    isAnswe = quidAnswers;
                  }
                }
              }
              score = score + isScore + ",";
              answe = answe + isAnswe + ",";
            }
         /*   if(quizList.size()>0&&score.endsWith(",")&&answe.endsWith(",")){
              score = score.substring(0, score.length()-1);
              answe = answe.substring(0, answe.length()-1);
            }*/
          }
          
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
          dataLogic.updateDate(dbConn, seqId, score, answe);
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
   *查询所有考试信息的记录数--考试在线
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectCount(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
      String dayTime = sf.format(new Date());
      String count = YHExamFlowLogic.getCount(dbConn, request.getParameterMap(),dayTime);
      String data = "{count:\"" + count + "\"}" ;
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
      if(pageTemp> strArray.length){
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
  public String getStr2(String str,int currPage,int pageSize){
    String newStr = "";
    String newPre = "";
    String newEnd= "";
    if(!YHUtility.isNullorEmpty(str)){
      String[] strArray = str.split(",");
      if(strArray.length == 0){
        strArray = new String[str.length()];
      }
      int pageTemp = 0;
      if((currPage-1)*pageSize + pageSize> strArray.length){
        pageTemp = strArray.length;
      }else{
        pageTemp = (currPage-1)*pageSize;
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

    return newStr;
  }
}
