package yh.subsys.oa.examManage.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.examManage.data.YHExamData;
import yh.subsys.oa.examManage.data.YHExamPaper;
import yh.subsys.oa.examManage.data.YHExamQuiz;

public class YHExamDataLogic {
  private static Logger log = Logger.getLogger(YHExamDataLogic.class);
  /**
   * 新建
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static int addData(Connection dbConn,YHExamData data) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, data);
    return getMaSeqId(dbConn, "oa_testing_data");
  }
  public static int getMaSeqId(Connection dbConn,String tableName)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    int maxSeqId = 0;
    String sql = "select max(SEQ_ID) as SEQ_ID from " + tableName;
    try{
     stmt = dbConn.createStatement();
     rs = stmt.executeQuery(sql);
     if(rs.next()){
       maxSeqId = rs.getInt("SEQ_ID");
     }
      
    }catch(Exception ex) {
       throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return maxSeqId;
  }
  /**
   * 编辑 
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void updateData(Connection dbConn,YHExamData data) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, data);
  }
  /**
   *  查询
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static List<YHExamData> selectData(Connection dbConn,String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHExamData> dataList = new ArrayList<YHExamData>();
    dataList = orm.loadListSingle(dbConn, YHExamData.class, str);
    return dataList;
  }
  /**
   *  查询ById
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static YHExamData selectDataById(Connection dbConn,String seqId)  throws Exception {
    YHORM orm = new YHORM();
    YHExamData data = (YHExamData) orm.loadObjSingle(dbConn, YHExamData.class, Integer.parseInt(seqId));
    return data;
  }
  /**
   * 删除BySeqId
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void delDataById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHExamData.class, Integer.parseInt(seqId));
  }
  /**
   * 删除ByItemIds
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static void updateDate(Connection dbConn,String seqId,String score,String answer) throws Exception {
   Statement stmt = null;
   ResultSet rs = null;
   String sql = "update oa_testing_data set SCORE = '" + score + "' ,ANSWER = '" + answer + "' ,EXAMED ='1'  where seq_id = " + seqId ;
   try {
     stmt = dbConn.createStatement();
     stmt.executeUpdate(sql);
   }catch(Exception ex) {
     throw ex;
   }finally {
     YHDBUtility.close(stmt, rs, log);
   }
  }
  
  /**
   * 试卷管理表BySeqId
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static YHExamPaper getParerBySeqId(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    YHExamPaper paper =(YHExamPaper)orm.loadObjSingle(dbConn, YHExamPaper.class, seqId);
    return paper;
  }
  /**
   * 得到试题列表
   * @param dbConn
   * @param item
   * @return
   * @throws Exception
   */
  public static List<YHExamQuiz> getQuiz(Connection dbConn,String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHExamQuiz> quizList =(List<YHExamQuiz>)orm.loadListSingle(dbConn, YHExamQuiz.class, str);
    return quizList;
  }
  public int selectQuizCount(Connection dbConn ,String seqIds) throws Exception {
    String sql = "select count(*) from oa_testing_question where SEQ_ID in (" + seqIds + ")";
    Statement st = dbConn.createStatement();
    ResultSet rs = st.executeQuery(sql);
    int count = 0;
    try {
      if (rs.next()) {
        if (!YHUtility.isNullorEmpty(rs.getString(1))) {
          count = rs.getInt(1);
        }
      }
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    } finally {
      rs.close();
      st.close();
    }
    return count;
  }

  public List selectQuizSeqId(Connection dbConn,String seqIds) throws Exception {
    String sql = "select seq_id from oa_testing_question where SEQ_ID in (" + seqIds + ")";
    Statement st = dbConn.createStatement();
    ResultSet rs = st.executeQuery(sql);
    List seqIdList = new ArrayList();
    try {
      while (rs.next()) {
        seqIdList.add(rs.getString(1));
      }
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    } finally {
      rs.close();
      st.close();
    }
    return seqIdList;
  }
}
