package yh.subsys.oa.examManage.logic;
import java.sql.Connection;
import java.util.Map;
import org.apache.log4j.Logger;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.examManage.data.YHExamQuiz;

public class YHExamQuizLogic {
  private static Logger log = Logger.getLogger(YHExamQuizLogic.class);

  /**
   * 新建试卷
   * @param dbConn
   * @param quiz
   * @throws Exception
   */
  public void addQuiz(Connection dbConn, YHExamQuiz quiz) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, quiz);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  /**
   * 得到试卷BySeqId   ---syl
   * @param dbConn
   * @param quiz
   * @throws Exception
   */
  public YHExamQuiz selectQuizById(Connection dbConn,int seqId) throws Exception {
    YHExamQuiz quiz = null;
    try {
      YHORM orm = new YHORM();
      quiz = (YHExamQuiz) orm.loadObjSingle(dbConn, YHExamQuiz.class, seqId);
    } catch (Exception ex) {
      throw ex;
    }
    return quiz;
  }

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页--lz
   * @return
   * @throws Exception 
   */
  public static String selectQuiz(Connection dbConn,Map request,String roomId) throws Exception {
    String sql = "select quiz.SEQ_ID,quiz.ROOM_ID,qset.ROOM_NAME,quiz.QUESTIONS_TYPE"
      + ",quiz.QUESTIONS_RANK,quiz.QUESTIONS,quiz.ANSWERS FROM oa_testing_question quiz "
      + " left outer join oa_testing_question_set qset on qset.seq_id = quiz.ROOM_ID "
      + " WHERE 1=1 ";
    if (!roomId.equals("0")) {
      sql += " and quiz.ROOM_ID=" + roomId;
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 删除--lz
   * @return
   * @throws Exception 
   */
  public static void deleteQuiz(Connection dbConn,String roomId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn,YHExamQuiz.class,Integer.parseInt(roomId));
  }
  /***
   * 修改--lz
   * @return
   * @throws Exception 
   */
  public static void updateQuiz(Connection dbConn,YHExamQuiz quiz) throws Exception {
    YHORM orm = new YHORM();
    orm.updateComplex(dbConn, quiz);
  }
  /**
   * 查询--lz
   * 
   * @return
   * @throws Exception
   */
  public static YHExamQuiz showQuiz(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    YHExamQuiz quiz = (YHExamQuiz)orm.loadObjComplex(dbConn,YHExamQuiz.class,Integer.parseInt(seqId));
    return quiz;
  }
}
