package yh.user.taiji.workflow;

import java.sql.Connection;
import java.sql.SQLException;

import yh.user.api.core.db.YHDbconnWrap;
/**
 * 取得工作流列表接口
 * @author Think
 *
 */
public class YHWorkListService {
  /**
   * 查询工作列表
   * @param userId 查询的人员
   * @param returnLength 返回的长度
   * @param type 0,取得所以有工作,1,为待办工作,2,为办理的工作,3,为已办结的工作
   * @param startDateStr 工作开始时间 格式为2010-11-17
   * @param endDateStr 工作结束时间格式为2010-11-17
   * @param flowIdStr 根据流程id号进行查询
   * @param runName 根据流程名进行模糊查询 
   * @param flowStatus 根据工作流的状态进行查询，0,为正在执行中,1,为已经结束
   * @param flowQueryType 查询的范围，0,为查询所有的,1,我发起的,2,我经办的,3,我管理的,4,我关注的,5,指定发起人,默认为我经办的。
   * @param toId 查询范围为指定发起人时的发起人id(是人的seqId)。
   * @return
   */
  public String[][] getWorkList(String userId  , int returnLength  , String type  , String startDateStr ,String endDateStr , String flowIdStr ,String runName ,String flowStatus){
    YHDbconnWrap dbUtil = new YHDbconnWrap();
    Connection dbConn = null;
    try {
      dbConn = dbUtil.getSysDbConn();
      YHWorkListLogic logic = new YHWorkListLogic();
      String[][] list = 
        logic.getHandleList(dbConn, userId  ,  returnLength  ,  type  ,  startDateStr , endDateStr ,  flowIdStr , runName , flowStatus , "" , "" );
      
      return list;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      dbUtil.closeAllDbConns();
    }
    return null;
  }
  
}
