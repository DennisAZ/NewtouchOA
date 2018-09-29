package yh.subsys.oa.examManage.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import org.apache.log4j.Logger;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.examManage.data.YHExamPaper;

public class YHExamPaperLogic {
  private static Logger log = Logger.getLogger(YHExamPaperLogic.class);

  /**
   * 新建试卷--cc
   * @param dbConn
   * @param paper
   * @throws Exception
   */
  public void addPaper(Connection dbConn, YHExamPaper paper) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, paper);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  /**
   * 获取指定试卷包含的试题列表--cc
   * @param dbConn
   * @param request
   * @param person
   * @param paperSeqId
   * @return
   * @throws Exception
   */
  public String getExamPaperListJson(Connection dbConn, Map request, YHPerson person, int paperSeqId) throws Exception {
    String seqIdStr = getExamQuizSeqIdStr(dbConn, paperSeqId);
    if(YHUtility.isNullorEmpty(seqIdStr)){
      seqIdStr = "-1";
    }
    String sql = "select " 
              + " SEQ_ID" 
              + ", ROOM_ID" 
              + ", QUESTIONS_TYPE" 
              + ", QUESTIONS_RANK" 
              + ", QUESTIONS" 
              + " from oa_testing_question where SEQ_ID IN ("+seqIdStr+") order by SEQ_ID";
      
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
    return pageDataList.toJson();
  }
  
  public String getExamPaperTitleJson(Connection dbConn, Map request, YHPerson person) throws Exception {
  
    String sql = "select " 
              + " SEQ_ID" 
              + ", PAPER_TITLE" 
              + ", PAPER_DESC" 
              + ", PAPER_TIMES"
              + ", PAPER_GRADE" 
              + ", QUESTIONS_COUNT" 
              + ", SEND_DATE" 
              + " from oa_testing_paper where 1=1 order by SEND_DATE";
      
      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
    
    return pageDataList.toJson();
  }
  
  /**
   * 获取最新插入（EXAM_PAPER表）的数据--cc
   * @param conn
   * @return
   * @throws Exception
   */
  public int getExmaPaperSeqId(Connection conn) throws Exception{
    String sql = "select Max(SEQ_ID) FROM oa_testing_paper";
    PreparedStatement pstmt =null;
    ResultSet rs  = null;
    try{
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      while(rs.next()){
        return rs.getInt(1);
      }
        return 0;
    } catch (Exception e){
      throw e;
    } finally {
      YHDBUtility.close(pstmt, rs, null);
    }
  }
  
  /**
   * 查询ID  --cc
   * @param str
   * @param id
   * @param reg
   * @return
   */
  public boolean findId(String str , int id, String reg){
    String[] strs = str.split(reg);
    for (int i = 0; i < strs.length; i++) {
      if (YHUtility.isInteger(strs[i])) {
        int tempId = Integer.parseInt(strs[i]);
        if(tempId == id){
          return true;
        }
      }
    }
    return false;
  }
  
  /**
   * 获取EXAM_PAPER表中的QUESTIONS_LIST(试题ID串)--cc
   * @param conn
   * @param paperSeqId
   * @return
   * @throws Exception
   */
  public String getQuestionsListLogic(Connection conn , int paperSeqId) throws Exception{
    String result = "";
    String sql = " select QUESTIONS_LIST from oa_testing_paper where SEQ_ID = " + paperSeqId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 获取试题数量--cc
   * @param conn
   * @param paperSeqId
   * @return
   * @throws Exception
   */
  public String getQuestionsCountLogic(Connection conn , int paperSeqId) throws Exception{
    String result = "";
    String sql = " select QUESTIONS_COUNT from oa_testing_paper where SEQ_ID = " + paperSeqId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 获取所选试题ID串--cc
   * @param conn
   * @param paperSeqId
   * @return
   * @throws Exception
   */
  public String getQuestionslistLogic(Connection conn , int paperSeqId) throws Exception{
    String result = "";
    String sql = " select QUESTIONS_LIST from oa_testing_paper where SEQ_ID = " + paperSeqId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 获取符合条件的ID串(EXAM_QUIZ表)--cc
   * @param conn
   * @param paperSeqId
   * @return
   * @throws Exception
   */
  public String getExamQuizSeqIdStr(Connection conn, int paperSeqId) throws Exception {
    String result = "";
    int count = 0;
    String questionsStr = getQuestionsListLogic(conn, paperSeqId);
    String sql = "select " 
             + " oa_testing_question.SEQ_ID" 
             + " from oa_testing_question, oa_testing_question_set, oa_testing_paper where oa_testing_paper.SEQ_ID = " + paperSeqId + " and oa_testing_question.ROOM_ID = oa_testing_question_set.SEQ_ID";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        String seqIdStr = String.valueOf(rs.getInt("SEQ_ID"));
        if (findId(questionsStr , rs.getInt("SEQ_ID"), ",")) {
          count++;
          if (!"".equals(result)) {
            result += ",";
          }
          result += seqIdStr;
        }
      }
      if (count >= 1) {
        return result;
      } else {
        return "";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 获取所属题库名称(多表查询)--cc
   * @param conn
   * @param paperSeqId
   * @return
   * @throws Exception
   */
  public String getRoomNameLogic(Connection conn, int roomId) throws Exception {
    String result = "";
    String sql = " select oa_testing_question_set.ROOM_NAME from oa_testing_question_set,oa_testing_paper where oa_testing_paper.SEQ_ID = " + roomId + " and oa_testing_paper.ROOM_ID=oa_testing_question_set.SEQ_ID";
    //String sql = " select ROOM_NAME from EXAM_QUIZ_SET where SEQ_ID = " + roomId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 获取所属题库名称(单表查询)--cc
   * @param conn
   * @param paperSeqId
   * @return
   * @throws Exception
   */
  public String getRoomNameSingleLogic(Connection conn, int roomId) throws Exception {
    String result = "";
    String sql = " select ROOM_NAME from oa_testing_question_set where SEQ_ID = " + roomId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 获取试卷管理表（EXAM_PAPER）中的roomId--cc
   * @param conn
   * @param paperSeqId
   * @return
   * @throws Exception
   */
  public String getRoomIdLogic(Connection conn, int paperSeqId) throws Exception {
    String result = "";
    String sql = " select ROOM_ID from oa_testing_paper where SEQ_ID = " + paperSeqId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 获取指定试题--cc
   * @param dbConn
   * @param questionCount
   * @param roomId
   * @param questionsRank
   * @param questionsType
   * @return
   * @throws Exception
   */
  public ArrayList<YHExamPaper> getExamPaperList(Connection dbConn,
      String questionCount, int roomId, String questionsRank, String questionsType) throws Exception {
    String whereStr = "";
    Statement stmt = null;
    ResultSet rs = null;
    YHExamPaper paper = null;
    ArrayList<YHExamPaper> paperList = new ArrayList<YHExamPaper>();
    if(!YHUtility.isNullorEmpty(questionsRank)){
      whereStr += " and QUESTIONS_RANK = '" + questionsRank + "'";
    }
    if(!YHUtility.isNullorEmpty(questionsType)){
      whereStr += " and QUESTIONS_TYPE = '" + questionsType + "'";
    }
    try {
      stmt = dbConn.createStatement();
      String sql = "select "
                + "SEQ_ID"
                + " from oa_testing_question where ROOM_ID='" + roomId + "'" + whereStr + "";

      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        paper = new YHExamPaper();
        paper.setSeqId(rs.getInt("SEQ_ID"));
        paperList.add(paper);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return paperList;
  }

  
  
  public static void main1(String[] args) {
    int[] intRet = new int[6];
    int intRd = 0;   //存放随机数
    int count = 0;   //记录生成的随机数个数
    int flag = 0;    //是否已经生成过标志
    int[] reset = new int[3];
    String seqIdStr = "";
    while(count < 3){
      Random rdm = new Random(System.currentTimeMillis());
      intRd = Math.abs(rdm.nextInt(intRet.length)) + 1;
      System.out.println(intRd);
      reset[count] = intRet[intRd];
      for(int i = 0; i < count; i++){
        if(reset[i] == intRet[intRd]){
          flag = 1;
          break;
        }else{
          flag = 0;
        }
      }
      if(flag == 0){
        //reset[count] = intRet[intRd];
        seqIdStr = seqIdStr + String.valueOf(intRet[intRd]) +",";
        count++;
       }
      
    }
    System.out.println(seqIdStr);
    for(int t = 0; t < count; t++){
      //System.out.println(t+"->"+intRet[t]);
    }
    //return null;
  }
 
  /**
   * 随机选题--cc
   * @param argsStr  试题表的SeqId串数组
   * @param count    选题个数
   * @return
   */
  public static String[] getIntLogic(String[] argsStr, int count) { 
    String[] strRt = {};
    if(argsStr.length != 0){
      strRt = new String[count];
      for (int i = 0; i < count; i++) { 
        int intRb = (int)(Math.random()*(argsStr.length - i)); 
        strRt[i] = argsStr[intRb]; 
        argsStr[intRb] = argsStr[argsStr.length - i - 1]; 
      } 
    }else{
      strRt = new String[0];
    }
    return strRt; 
  } 
  
  /**
   * 修改试卷中考试的试题(用于随机选题后的试题seqId串)--cc
   * @param dbConn
   * @param seqId
   * @param questionStr
   * @throws Exception
   */
  public static void updateQuestionList(Connection dbConn, int seqId, String[] questionStr, YHExamPaper paper) throws Exception {
    String questionsList = "";
    String strs = "";
    int questionsCount = paper.getQuestionsCount();
    int paperGrade = 0;
    if(questionsCount > 0){
      paperGrade = paper.getPaperGrade();
    }
    int aveCore = paperGrade/questionsCount;
    String quesCore = "";
    for (int i = 0; i < questionStr.length; i++) { 
      if (!"".equals(quesCore)) {
        quesCore += ",";
      }
      if(!"".equals(questionsList)){
        questionsList += ",";
      }
      quesCore += String.valueOf(aveCore);
      questionsList += questionStr[i];
    } 

    String sql = "update oa_testing_paper set QUESTIONS_LIST = ?, QUESTIONS_SCORE = ? where SEQ_ID = ?";
    PreparedStatement ps = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, questionsList);
      ps.setString(2, quesCore);
      ps.setInt(3, seqId);
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 删除一条记录--cc
   * @param conn
   * @param seqId
   * @throws Exception
   */
  public void deleteSingle(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.deleteSingle(conn, YHExamPaper.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  public YHExamPaper getExamPaperDetail(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHExamPaper) orm.loadObjSingle(conn, YHExamPaper.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  /**
   * 修改试卷--cc
   * @param conn
   * @param record
   * @throws Exception
   */
  public void updateExamPaper(Connection conn, YHExamPaper record) throws Exception {
    try {
        YHORM orm = new YHORM();
        orm.updateSingle(conn, record);
      } catch (Exception ex) {
        throw ex;
      } finally {
    }
  }
  
  /**
   * 判断试卷是否正被使用--cc
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public boolean useredByPaper(Connection dbConn, int seqId)
  throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      
      String sql = "SELECT count(*) from oa_testing_flow where PAPER_ID = " + seqId + " and (" + YHDBUtility.getDateFilter("END_DATE", YHUtility.getCurDateTimeStr(), ">=") + " or (END_DATE is null or END_DATE = '')) ";
      rs = stmt.executeQuery(sql);
      long count = 0;
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count == 1) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 判断所选试题数量是否溢出
   * @param dbConn
   * @param roomId
   * @return
   * @throws Exception
   */
  public long isCount(Connection dbConn, String roomId)
  throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    long count = 0;
    String whereStr = "";
    try {
      if(!YHUtility.isNullorEmpty(roomId)){
        whereStr = " and ROOM_ID = '" + roomId + "'";
      }
      stmt = dbConn.createStatement();
      
      String sql = "SELECT count(*) from oa_testing_question where 1=1" + whereStr;
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        count = rs.getLong(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return count;
  }
}
