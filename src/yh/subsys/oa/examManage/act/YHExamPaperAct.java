package yh.subsys.oa.examManage.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.examManage.data.YHExamPaper;
import yh.subsys.oa.examManage.logic.YHExamPaperLogic;

public class YHExamPaperAct {
  public static final String attachmentFolder = "examManage";
  private YHExamPaperLogic logic = new YHExamPaperLogic();
  
  /**
   * 新建试卷--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addPaper(HttpServletRequest request,
       HttpServletResponse response) throws Exception{
      
     Connection dbConn = null;
     try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       Map<String,String[]> map = request.getParameterMap();
       YHExamPaper paper = (YHExamPaper) YHFOM.build(map, YHExamPaper.class, "");
       Date curTime = new Date();
       paper.setSendDate(curTime);
       paper.setUserId(String.valueOf(person.getSeqId()));
       this.logic.addPaper(dbConn, paper);
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
       request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
     } catch (Exception e) {
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
       request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
       throw e; 
     }
     
     return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取最新插入试卷的seqId--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPaperSeqId(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int paperSeqId = this.logic.getExmaPaperSeqId(dbConn);
      String data = String.valueOf(paperSeqId);
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
   * 获取试题数量--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getQuestionsCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("paperSeqId");
      String data = this.logic.getQuestionsCountLogic(dbConn, Integer.parseInt(seqId));
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
   * 获取所选试题ID串--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getQuestionsList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("paperSeqId");
      String data = this.logic.getQuestionsListLogic(dbConn, Integer.parseInt(seqId));
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
   * 获取指定试卷包含的试题列表--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getExamPaperListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int paperSeqId = Integer.parseInt(request.getParameter("paperSeqId"));
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getExamPaperListJson(dbConn, request.getParameterMap(), person, paperSeqId);
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
   * 获取所属题库名称(联合查询)--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRoomName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String paperSeqIdStr = request.getParameter("paperSeqId");
      int roomId = 0;
      if(!YHUtility.isNullorEmpty(paperSeqIdStr)){
        roomId = Integer.parseInt(paperSeqIdStr);
      }
      String data = this.logic.getRoomNameLogic(dbConn, roomId);
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
   * 获取所属题库名称(单表查询)--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRoomNameSingle(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String roomIdStr = request.getParameter("roomId");
      int roomId = 0;
      if(!YHUtility.isNullorEmpty(roomIdStr)){
        roomId = Integer.parseInt(roomIdStr);
      }
      String data = this.logic.getRoomNameSingleLogic(dbConn, roomId);
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
   * 在试卷管理表（EXAM_PAPER）中根据seqId查询roomId
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRoomId(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String paperSeqIdStr = request.getParameter("paperSeqId");
      int paperSeqId = 0;
      if(!YHUtility.isNullorEmpty(paperSeqIdStr)){
        paperSeqId = Integer.parseInt(paperSeqIdStr);
      }
      String data = this.logic.getRoomIdLogic(dbConn, paperSeqId);
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
   * 自动选题
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAutoTopics(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String questionCount = request.getParameter("questionCount");
      String questionsRank = request.getParameter("questionsRank");
      String questionsType = request.getParameter("questionsType");
      String paperSeqId = request.getParameter("paperSeqId");
      String data = "";
      if(YHUtility.isNullorEmpty(paperSeqId)){
        paperSeqId = "0";
      }
      YHORM orm = new YHORM();
      YHExamPaper paper = (YHExamPaper) orm.loadObjSingle(dbConn, YHExamPaper.class, Integer.parseInt(paperSeqId));
      ArrayList<YHExamPaper> paperList = this.logic.getExamPaperList(dbConn, questionCount, paper.getRoomId(), questionsRank, questionsType);
      int curNum = paperList.size();
      int questionsCount = paper.getQuestionsCount();
      if(curNum >= questionsCount){
        String[] intRet = new String[paperList.size()];
        for(int i = 0; i < paperList.size(); i++){
          intRet[i] = String.valueOf(paperList.get(i).getSeqId());
        }
        String[] seqIdStr = this.logic.getIntLogic(intRet, paper.getQuestionsCount());
        this.logic.updateQuestionList(dbConn, Integer.parseInt(paperSeqId), seqIdStr, paper);
        data = "0";
      }else{
        data = "1";
      }
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 管理试卷列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getExamPaperTitleJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = this.logic.getExamPaperTitleJson(dbConn, request.getParameterMap(), person);
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
   * 删除一条记录--cc
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
      
      this.logic.deleteSingle(dbConn, Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getExamPaperDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      if(YHUtility.isNullorEmpty(seqId)){
        seqId = "0";
      }
      YHExamPaper paper = (YHExamPaper)this.logic.getExamPaperDetail(dbConn, Integer.parseInt(seqId));
      if (paper == null){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
        request.setAttribute(YHActionKeys.RET_MSRG, "该试卷不存在");
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
   * 修改试卷--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateExamPaper(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String,String[]> map = request.getParameterMap();
      YHExamPaper record = (YHExamPaper) YHFOM.build(map, YHExamPaper.class, "");
      this.logic.updateExamPaper(dbConn, record);
      
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
   * 判断试卷是否正被使用--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String useredByPaper(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("seqId");
      int seqId = 0;
      if(!YHUtility.isNullorEmpty(seqIdStr)){
        seqId = Integer.parseInt(seqIdStr);
      }
      boolean bool = this.logic.useredByPaper(dbConn, seqId);
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
   * 判断所选试题数量是否溢出--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String isCount(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String roomId = request.getParameter("roomId");
      String data = "";
      long questionsCount = 0;
      String questionStr = request.getParameter("questionsCount");
      if(!YHUtility.isNullorEmpty(questionStr)){
        questionsCount = Long.parseLong(questionStr);
      }
      long count = this.logic.isCount(dbConn, roomId);
      if(questionsCount > count){
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
   * 获取手动选题后的试题列表--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSelectManualJson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String roomId = request.getParameter("roomId");
      String questionsType = request.getParameter("questionsType");
      String questionsRank = request.getParameter("questionsRank");
      String whereStr = "";
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      if(!YHUtility.isNullorEmpty(questionsRank)){
        whereStr += " and QUESTIONS_RANK = '" + questionsRank + "'";
      }
      if(!YHUtility.isNullorEmpty(questionsType)){
        whereStr += " and QUESTIONS_TYPE = '" + questionsType + "'";
      }
      String[] filters = new String[]{"ROOM_ID=" + roomId + "" + whereStr + ""};
      List funcList = new ArrayList();
      funcList.add("examQuiz");
      
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_TESTING_QUESTION"));
      for(Map ms : list){
        String questions = (String) ms.get("questions");
        questions = YHUtility.encodeSpecial(questions);
        sb.append("{");
        sb.append("seqId:\"" + (ms.get("seqId") == null ? "" : ms.get("seqId")) + "\"");
        sb.append(",questionsType:\"" + (ms.get("questionsType") == null ? "" : ms.get("questionsType")) + "\"");
        sb.append(",questionsRank:\"" + (ms.get("questionsRank") == null ? "" : ms.get("questionsRank")) + "\"");
        sb.append(",questions:\"" + (ms.get("questions") == null ? "" :questions) + "\"");
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
   * 手动选题－保存--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateSelectManual(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String,String[]> map = request.getParameterMap();
      YHORM orm = new YHORM();
      YHExamPaper record = (YHExamPaper) YHFOM.build(map, YHExamPaper.class, "");
      int seqId = record.getSeqId();
      YHExamPaper paper = (YHExamPaper) orm.loadObjSingle(dbConn, YHExamPaper.class, seqId);
      int paperGrade = 0;
      int questionsCount = 0;
      paperGrade = paper.getPaperGrade();
      String questionsList = record.getQuestionsList();
      String[] questStr = questionsList.split(",");
      for(int i = 0; i < questStr.length; i++){
        questionsCount++;
      }
      int aveCore = paperGrade/questionsCount;
      String quesCore = "";
      for(int i = 0; i < questStr.length; i++){
        if (!"".equals(quesCore)) {
          quesCore += ",";
        }
        quesCore += String.valueOf(aveCore);
      }
      
      Map m =new HashMap();
      m.put("seqId", seqId);
      m.put("questionsList", questionsList);
      m.put("questionsScore", quesCore);
      
      orm.updateSingle(dbConn, "examPaper", m);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
}
