package yh.subsys.oa.hr.score.logic;

import java.sql.Connection;
import java.util.Map;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.score.data.YHScoreGroup;

public class YHScoreGroupLogic {
  
  /**
   * 考核指标集管理列表--cc
   * @param dbConn
   * @param request
   * @return
   * @throws Exception
   */
  public String getScoreGroupList(Connection dbConn, Map request) throws Exception {
    String sql = "select " 
             + "  SEQ_ID" 
             + ", GROUP_FLAG" 
             + ", GROUP_NAME" 
             + ", GROUP_DESC" 
             + " from oa_score_team order by SEQ_ID DESC";
    YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
    return pageDataList.toJson();
  }
  
  /**
   * 新建考核指标集--cc
   * @param dbConn
   * @param scoreGroup
   * @throws Exception
   */
  public void addScoreGroup(Connection dbConn, YHScoreGroup scoreGroup) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, scoreGroup);
    } catch (Exception ex) {
      throw ex;
    } finally {

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
      orm.deleteSingle(conn, YHScoreGroup.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {
    }
  }
  
  /**
   * 获取考核指标集管理一条记录 --cc
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public YHScoreGroup getScoreGroupDetail(Connection conn, int seqId) throws Exception {
    try {
      YHORM orm = new YHORM();
      return (YHScoreGroup) orm.loadObjSingle(conn, YHScoreGroup.class, seqId);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }

  /**
   * 编辑考核指标集管理 --cc
   * @param conn
   * @param scoreGroup
   * @throws Exception
   */
  public void updateScoreGroup(Connection conn, YHScoreGroup scoreGroup) throws Exception {
    try {
        YHORM orm = new YHORM();
        orm.updateSingle(conn, scoreGroup);
      } catch (Exception ex) {
        throw ex;
      } finally {
    }
  }
}
