package yh.subsys.oa.hr.score.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.score.data.YHScoreAnswer;

public class YHScoreAnswerLogic {
  
  /**
   * 添加选择方式考核信息--cc
   * @param dbConn
   * @param scoreAnswer
   * @throws Exception
   */
  public void addScoreAnswer(Connection dbConn, YHScoreAnswer scoreAnswer) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, scoreAnswer);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  /**
   * 获取选择方式考核指标集明细 列表--cc
   * @param dbConn
   * @param request
   * @param itemId
   * @return
   * @throws Exception
   */
  public String getScoreAnswerList(Connection dbConn, Map request, int itemId) throws Exception {
    String sql = "select " 
             + "  SEQ_ID" 
             + ", ITEM_NAME" 
             + " from oa_score_answer where GROUP_ID = '" + itemId + "' order by SEQ_ID DESC";
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
    return pageDataList.toJson();
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
      orm.deleteSingle(conn, YHScoreAnswer.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  /**
   * 获取选择方式考核项目选项信息--cc
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHScoreAnswer getScoreAnswerDetail(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHScoreAnswer) orm.loadObjSingle(conn, YHScoreAnswer.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  /**
   * 编辑选择方式考核项目选项信息--cc
   * @param conn
   * @param scoreGroup
   * @throws Exception
   */
  public void updateScoreAnswer(Connection conn, YHScoreAnswer scoreAnswer) throws Exception {
    try {
        YHORM orm = new YHORM();
        orm.updateSingle(conn, scoreAnswer);
      } catch (Exception ex) {
        throw ex;
      } finally {
    }
  }
  /**
   * 根据考核任务Seq_Id得到 相关联的考核指标明细做分页
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public List<YHScoreAnswer> getAnswerByGroupId(Connection dbConn,String[] str)throws Exception {
    YHORM orm = new YHORM();
    List<YHScoreAnswer>  itemList = new ArrayList<YHScoreAnswer>();
    itemList = orm.loadListSingle(dbConn, YHScoreAnswer.class, str);
    return itemList;
  }
  /**
   * 根据考核任务Seq_Id得到 相关联的考核指标明细的总数
   * @param dbConn
   * @param seqId
   * @return
   * @throws Exception
   */
  public int selectAnswerCount(Connection dbConn ,int groupId) throws Exception {
    String sql = "select count(*) from oa_score_answer where GROUP_ID = " + groupId ;
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
}
