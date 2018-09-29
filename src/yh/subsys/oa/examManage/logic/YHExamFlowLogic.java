package yh.subsys.oa.examManage.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.person.data.YHPerson;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.examManage.data.YHExamData;
import yh.subsys.oa.examManage.data.YHExamFlow;
import yh.subsys.oa.examManage.data.YHExamPaper;

public class YHExamFlowLogic {
  private static Logger log = Logger.getLogger(YHExamFlowLogic.class);
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页--lz
   * @return
   * @throws Exception 
   */
  public static String selectFlow(Connection dbConn,Map request,String dayTime) throws Exception {
    String sql = "select ex.SEQ_ID,ex.FLOW_TITLE,ex.PARTICIPANT,son.PAPER_TIMES"
      + ",ex.BEGIN_DATE,ex.END_DATE,ex.PAPER_ID,ex.FLOW_DESC,ex.FLOW_FLAG"
      + ",ex.SEND_TIME,ex.RANKMAN,ex.ANONYMITY from oa_testing_flow ex "
      + " left outer join oa_testing_paper son on son.seq_id = ex.PAPER_ID "
      + " WHERE 1=1 ";
    sql += " and " + YHDBUtility.getDateFilter("ex.BEGIN_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(dayTime)), "<=");
    sql += " and (" + YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(dayTime)), ">")
    + "or ex.END_DATE is null)  order by ex.SEND_TIME desc ";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页--lz
   * @return
   * @throws Exception 
   */
  public static String selectList(Connection dbConn,Map request,YHExamFlow flow, String beginDate1,String endDate1,String cd) throws Exception {
    String sql = "select ex.SEQ_ID,ex.FLOW_TITLE,ex.PARTICIPANT,son.PAPER_TIMES"
      + ",ex.BEGIN_DATE,ex.END_DATE,ex.PAPER_ID,ex.FLOW_DESC,ex.FLOW_FLAG"
      + ",ex.SEND_TIME,ex.RANKMAN,ex.ANONYMITY from oa_testing_flow ex "
      + " left outer join oa_testing_paper son on son.seq_id = ex.PAPER_ID "
      + " WHERE 1=1 ";
    if (!YHUtility.isNullorEmpty(flow.getFlowTitle())) {
      sql += " and ex.FLOW_TITLE like '%" + YHDBUtility.escapeLike(flow.getFlowTitle()) + "%' " + YHDBUtility.escapeLike();
    }
    if (flow.getPaperId() > 0) {
      sql += " and ex.PAPER_ID=" + flow.getPaperId();
    }
    if (!YHUtility.isNullorEmpty(flow.getParticipant())) {
      sql += " and " + YHDBUtility.findInSet(flow.getParticipant(),"ex.PARTICIPANT");
    }
    if (flow.getBeginDate() != null) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.BEGIN_DATE", YHUtility.getDateTimeStr(flow.getBeginDate()), ">=");
    }
    if (!YHUtility.isNullorEmpty(beginDate1)) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.BEGIN_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(beginDate1)), "<=");
    }
    if (flow.getEndDate() != null) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(flow.getEndDate()), ">=");
    }
    if (!YHUtility.isNullorEmpty(endDate1)) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(endDate1)), "<=");
    }
    if (cd.equals("2")) {
      sql += " and " +  YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(new Date()), "<=") + " and  ex.END_DATE is not null ";
    }
    sql += " order by ex.SEND_TIME desc ";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  /**
   * 新建--lz
   * 
   * @return
   * @throws Exception
   */
  public static void add(Connection dbConn,YHExamFlow flow) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, flow);
  }

  /**
   * 删除--lz
   * 
   * @return
   * @throws Exception
   */
  public static void deleteFlow(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn,YHExamFlow.class,Integer.parseInt(seqId));
  }


  /**
   * 查询--lz
   * 
   * @return
   * @throws Exception
   */
  public static YHExamFlow showFlow(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    YHExamFlow flow = (YHExamFlow)orm.loadObjComplex(dbConn,YHExamFlow.class,Integer.parseInt(seqId));
    return flow;
  }
  /**
   * 修改--lz
   * 
   * @return
   * @throws Exception
   */
  public static void updateFlow(Connection dbConn,YHExamFlow flow) throws Exception {
    YHORM orm = new YHORM();
    orm.updateComplex(dbConn, flow);
  }
  /**
   * 参加考试人员查询--lz
   * 
   * @return
   * @throws Exception
   */
  public static List<YHPerson> showMan(Connection dbConn,String participant) throws Exception {
    String sql = "select son.SEQ_ID,son.USER_NAME as userName,dep.DEPT_NAME as deptName "
      + ",priv.PRIV_NAME as privName FROM PERSON son "
      + " left outer join oa_department dep on dep.SEQ_ID = son.DEPT_ID "
      + " left outer join USER_PRIV priv on priv.SEQ_ID = son.USER_PRIV "
      + " WHERE son.SEQ_ID in (" + participant +")";
    List<YHPerson> list = new ArrayList<YHPerson>();
    YHPerson  person = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        person = new YHPerson();
        person.setUserName(rs.getString("userName"));
        person.setUserId(rs.getString("deptName"));
        person.setUserPriv(rs.getString("privName"));
        list.add(person);
      }
    }catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs,log);
    }
    return list;
  }


  /**
   * 参加考试人员查询--lz
   * 
   * @return
   * @throws Exception
   */
  public static YHPerson showPerson(Connection dbConn,String participant) throws Exception {
    String sql = "select son.SEQ_ID as seqId,son.USER_NAME as userName,dep.DEPT_NAME as deptName "
      + ",priv.PRIV_NAME as privName FROM PERSON son "
      + " left outer join oa_department dep on dep.SEQ_ID = son.DEPT_ID "
      + " left outer join USER_PRIV priv on priv.SEQ_ID = son.USER_PRIV "
      + " WHERE son.SEQ_ID =" + participant;
    YHPerson  person = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        person = new YHPerson();
        person.setSeqId(rs.getInt("seqId"));
        person.setUserName(rs.getString("userName"));
        person.setUserId(rs.getString("deptName"));
        person.setUserPriv(rs.getString("privName"));
      }
    }catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs,log);
    }
    return person;
  }
  /**
   * 立即终止--lz
   * 
   * @return
   * @throws Exception
   */
  public static void updateStatus(Connection dbConn,String seqId,String endTime) throws Exception {
    String sql = " update oa_testing_flow set end_date=? where seq_id=? ";
    PreparedStatement ps = null;
    ps = dbConn.prepareStatement(sql);
    try {
      ps.setDate(1,YHUtility.parseSqlDate(endTime));
      ps.setInt(2,Integer.parseInt(seqId));
      ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps,null,log);
    }
  }
  /***
   * 数据导出--lz
   * @return
   * @throws Exception 
   * @throws Exception 
   */
  public static ArrayList<YHDbRecord> getDbRecord(Connection dbConn,List<YHExamData> list,int paperGrade,int count) throws Exception{
    ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
    YHDbRecord dbrec = null;
    YHExamData examDate = new YHExamData();
    if (list.size() <= 0) {
      dbrec = new YHDbRecord();
      dbrec.addField("部门","");
      dbrec.addField("人员","");
      dbrec.addField("角色","");
      dbrec.addField("分数","");
      dbL.add(dbrec);
    } else {
      for (int i = 0; i < list.size(); i ++) {
        examDate = list.get(i);
        int num = 0;
        if (!YHUtility.isNullorEmpty(examDate.getParticipant())) {
          YHPerson person = showPerson(dbConn,examDate.getParticipant());
          dbrec = new YHDbRecord();
          dbrec.addField("部门",person.getUserId());
          dbrec.addField("人员",person.getUserName());
          dbrec.addField("角色",person.getUserPriv());
          //答对多少题
          if (examDate.getScore().split(",").length > 0) {
            for (int j = 0; j < examDate.getScore().split(",").length; j ++) {
              if (examDate.getScore().split(",")[j].equals("1")) {
                num ++;
              }
            }
          }
          dbrec.addField("分数",paperGrade*num/count);
          dbL.add(dbrec);
        }
      }
    }
    return dbL;
  }

  /***
   * 数据导出,取试卷ID，考试分数,题数--lz
   * @return
   * @throws Exception 
   * @throws Exception
   */
  public static YHExamPaper selectPaper(Connection dbConn,String seqId) throws Exception{
    YHORM orm = new YHORM();
    YHExamPaper paper = (YHExamPaper)orm.loadObjComplex(dbConn, YHExamPaper.class,Integer.parseInt(seqId));
    return paper;
  }

  /***
   * 数据导出,取试卷考试人,答对数量--lz
   * @return
   * @throws Exception 
   * @throws Exception 
   */
  public static List<YHExamData> selectListData(Connection dbConn,String str[]) throws Exception{
    YHORM orm = new YHORM();
    List<YHExamData> examData = orm.loadListSingle(dbConn,YHExamData.class,str);
    return examData;
  }

  /***
   * 考试结果统计数据--lz
   * @return
   * @throws Exception 
   */
  public static String selectQIZ(Connection dbConn,Map request,String paperId,String questionsList) throws Exception {
    String sql = "select SEQ_ID,QUESTIONS,QUESTIONS_RANK,QUESTIONS_TYPE,ANSWERS FROM oa_testing_question "
      + " WHERE SEQ_ID in (" + questionsList + ")";
    //    String sql = "select ex.SEQ_ID,quiz.QUESTIONS,quiz.QUESTIONS_RANK"
    //      + ",quiz.QUESTIONS_TYPE,quiz.ANSWERS from EXAM_PAPER ex "
    //      + " left outer join EXAM_QUIZ quiz on quiz.ROOM_ID = ex.ROOM_ID "
    //      //+ " left outer join EXAM_DATA data on data.FLOW_ID =" + flowId
    //      + " WHERE 1=1 and ex.SEQ_ID=" + paperId ;
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 考试结果统计数据(试题查询)--lz
   * @return
   * @throws Exception 
   */
  public static String selectQuestionsList(Connection dbConn,String paperId) throws Exception {
    String sql = "select QUESTIONS_LIST from oa_testing_paper "
      + " WHERE SEQ_ID='" + paperId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String questionsList = "0";
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        if(!YHUtility.isNullorEmpty(rs.getString("QUESTIONS_LIST"))){
          questionsList = rs.getString("QUESTIONS_LIST");
        }
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return questionsList;
  }

  /***
   *取试卷标题--lz
   * @return
   * @throws Exception 
   */
  public static YHExamPaper showTitle(Connection dbConn,String paperId) throws Exception {
    YHORM orm = new YHORM();
    return (YHExamPaper)orm.loadObjSingle(dbConn,YHExamPaper.class,Integer.parseInt(paperId));
  }
  /***
   * 查题--lz
   * @return
   * @throws Exception 
   * @throws Exception 
   */
  public static List<YHExamFlow> showMan2(Connection dbConn,List<YHExamData> list,int paperGrade,int count) throws Exception{
    YHExamData examDate = new YHExamData();
    List<YHExamFlow> examFlow = new ArrayList<YHExamFlow>();
    YHExamFlow flow = null;
    for (int i = 0; i < list.size(); i ++) {
      examDate = list.get(i);
      int num = 0;
      if (!YHUtility.isNullorEmpty(examDate.getParticipant())) {
        YHPerson person = showPerson(dbConn,examDate.getParticipant());
        flow = new YHExamFlow();
        flow.setParticipant(person.getUserId());//部门
        flow.setFlowTitle(person.getUserName());//部人员
        flow.setRankman(person.getUserPriv());//角色
        flow.setFlowDesc(String.valueOf(person.getSeqId()));//ID
        //答对多少题
        if (examDate.getScore().split(",").length > 0) {
          for (int j = 0; j < examDate.getScore().split(",").length; j ++) {
            if (examDate.getScore().split(",")[j].equals("1")) {
              num ++;
            }
          }
        }
        flow.setFlowFlag(String.valueOf(paperGrade*num/count));//分数
        examFlow.add(flow);
      } 
    }
    return examFlow;
  }

  /**
   * 查询考试次数 -lz
   * @param dbConn
   * @param tableName
   * @return
   * @throws Exception
   */
  public static String showCount(Connection dbConn,int seqId) throws Exception{
    String count = "0";
    String sql = "select count(*) from oa_testing_data where flow_id=?";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      ps.setInt(1,seqId);
      rs = ps.executeQuery();
      if (rs.next()) {
        if(!YHUtility.isNullorEmpty(rs.getString(1))){
          count = rs.getString(1);
        }
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return count;
  }
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页--syl
   * @return
   * @throws Exception 
   */
  public static String selectFlowOnLine(Connection dbConn,Map request,String dayTime,String userId) throws Exception {
    String sql = "select ex.SEQ_ID,ex.FLOW_TITLE,ex.PARTICIPANT,son.PAPER_TIMES"
      + ",ex.BEGIN_DATE,ex.END_DATE,son.PAPER_GRADE,ex.FLOW_DESC"
      + ",ex.SEND_TIME,ex.RANKMAN,ex.ANONYMITY from oa_testing_flow ex "
      + " left outer join oa_testing_paper son on son.seq_id = ex.PAPER_ID "
      + " WHERE 1=1 ";
    sql += " and " + YHDBUtility.getDateFilter("ex.BEGIN_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(dayTime)), "<=");
    sql += " and (" + YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(dayTime)), ">")
    + " or ex.END_DATE is null) and " + YHDBUtility.findInSet(userId, "ex.PARTICIPANT")+ " order by ex.SEND_TIME desc ";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    //(find_in_set('$LOGIN_USER_ID',PARTICIPANT)) 
    // and BEGIN_DATE<='$CUR_DATE' and (END_DATE>='$CUR_DATE' or END_DATE is null) 
    //order by SEND_TIME desc";
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /**
   * 根据条件查询数据的记录树--syl
   * @param dbConn
   * @param tableName
   * @return
   * @throws Exception
   */
  public static String getCount(Connection dbConn,Map request,String dayTime) throws Exception{
    String count = "0";
    String sql = "select count(*) from oa_testing_flow ex "
      + " left outer join oa_testing_paper son on son.seq_id = ex.PAPER_ID "
      + " WHERE 1=1 ";
    sql += " and " + YHDBUtility.getDateFilter("ex.BEGIN_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(dayTime)), "<=");
    sql += " and (" + YHDBUtility.getDateFilter("ex.END_DATE", YHUtility.getDateTimeStr(YHUtility.parseDate(dayTime)), ">")
    + " or ex.END_DATE is null)";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        if(!YHUtility.isNullorEmpty(rs.getString(1))){
          count = rs.getString(1);
        }
      }
    } catch (Exception e) {
      throw e;
    } finally{
      YHDBUtility.close(ps, rs, log);
    }
    return count;
  }
}
