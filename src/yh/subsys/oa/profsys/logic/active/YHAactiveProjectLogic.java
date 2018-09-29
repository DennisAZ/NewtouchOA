package yh.subsys.oa.profsys.logic.active;

import java.sql.Connection;
import java.util.Map;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHAactiveProjectLogic {
  /**
   * 分页列表
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public String toSearchData(Connection conn,Map request,String projType,String projNum
      ,String projActiveType,String projStartTime1,String projStartTime2,
      String projGropName,String projVisitType,String projEndTime1,String projEndTime2,
      String projLeader,String deptId,String managerStr,String projStatus) throws Exception{
    String sql = "select p.SEQ_ID,p.PROJ_NUM,p.PROJ_GROUP_NAME,p.DEPT_ID,dep.DEPT_NAME"
      + ",pn.USER_NAME,ci.CLASS_DESC,p.PROJ_START_TIME,p.PROJ_END_TIME,p.P_YX,p.P_TOTAL,p.PRINT_STATUS"
      + " from oa_project p left outer join oa_department dep on p.DEPT_ID = dep.SEQ_ID"
      + " left outer join oa_kind_dict_item ci on p.PROJ_ACTIVE_TYPE = ci.SEQ_ID "
      + " left outer join PERSON pn on p.PROJ_LEADER = pn.SEQ_ID where p.PROJ_TYPE = '" + projType + "'";
    if(!YHUtility.isNullorEmpty(projStatus)){
      if(projStatus.equals("0")){
        sql = sql + " and (p.PROJ_STATUS = '0' or p.PROJ_STATUS is null)";
      }else{
        sql = sql + " and p.PROJ_STATUS = '" + projStatus + "'";
      }
    }
    if(!YHUtility.isNullorEmpty(managerStr)){
      sql = sql + " and p.DEPT_ID " + managerStr;
    }
    if(!YHUtility.isNullorEmpty(projNum)){
      sql = sql + " and p.PROJ_NUM like '%" + YHDBUtility.escapeLike(projNum) + "%'" + YHDBUtility.escapeLike() ;
    }
    if(!YHUtility.isNullorEmpty(projActiveType)){
      sql = sql + " and p.PROJ_ACTIVE_TYPE = '" + projActiveType + "'";
    }
    
    if(!YHUtility.isNullorEmpty(projStartTime1)){
      sql = sql + " and " + YHDBUtility.getDateFilter("p.PROJ_START_TIME",projStartTime1, ">=");
    }
    if(!YHUtility.isNullorEmpty(projStartTime2)){
      sql = sql + " and " + YHDBUtility.getDateFilter("p.PROJ_START_TIME",projStartTime2 + " 23:59:59", "<=");
    }
    if(!YHUtility.isNullorEmpty(projGropName)){
      sql = sql + " and p.PROJ_GROUP_NAME like '%" + YHDBUtility.escapeLike(projGropName) + "%'" + YHDBUtility.escapeLike() ;
    }
    if(!YHUtility.isNullorEmpty(projVisitType)){
      sql = sql + " and p.PROJ_VISIT_TYPE ='" + projVisitType + "'";
    }
    
    if(!YHUtility.isNullorEmpty(projEndTime1)){
      sql = sql + " and " + YHDBUtility.getDateFilter("p.PROJ_END_TIME",projEndTime1, ">=");
    }
    if(!YHUtility.isNullorEmpty(projStartTime2)){
      sql = sql + " and " + YHDBUtility.getDateFilter("p.PROJ_END_TIME",projEndTime2 + " 23:59:59", "<=");
    }
    if(!YHUtility.isNullorEmpty(projLeader)){
      sql = sql + " and p.PROJ_LEADER = '" + projLeader + "'";
    }
    if(!YHUtility.isNullorEmpty(deptId)){
      sql = sql + " and p.DEPT_ID in(" + deptId + ")";
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
    return pageDataList.toJson();
  }
}
