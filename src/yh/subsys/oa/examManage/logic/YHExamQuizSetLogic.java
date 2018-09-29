package yh.subsys.oa.examManage.logic;
import java.sql.Connection;
import java.util.Map;
import org.apache.log4j.Logger;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.examManage.data.YHExamQuizSet;

public class YHExamQuizSetLogic {
  private static Logger log = Logger.getLogger(YHExamQuizSetLogic.class);

  /**
   * 新建题库
   * @param dbConn
   * @param quiz
   * @throws Exception
   */
  public void addBank(Connection dbConn, YHExamQuizSet quiz) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, quiz);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  /**
   * 获取题库列表--cc
   * @param dbConn
   * @param request
   * @return
   * @throws Exception
   */
  public String getExamQuizSetList(Connection dbConn, Map request) throws Exception {
    String sql = "select " 
              + " SEQ_ID" 
              + ", ROOM_CODE" 
              + ", ROOM_NAME" 
              + ", ROOM_DESC" 
              + " from oa_testing_question_set order by ROOM_CODE";
      
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
    return pageDataList.toJson();
  }
  
  /**
   * 删除题库--cc
   * @param conn
   * @param seqId
   * @throws Exception
   */
  public void deleteSingle(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.deleteSingle(conn, YHExamQuizSet.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  /**
   * 获取题库详情(修改题库)--cc
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHExamQuizSet getExamQuizSetDetail(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHExamQuizSet) orm.loadObjSingle(conn, YHExamQuizSet.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  /**
   * 修改题库--cc
   * @param conn
   * @param record
   * @throws Exception
   */
  public void updateExamQuizSet(Connection conn, YHExamQuizSet record) throws Exception {
    try {
        YHORM orm = new YHORM();
        orm.updateSingle(conn, record);
      } catch (Exception ex) {
        throw ex;
      } finally {
    }
  }
}
