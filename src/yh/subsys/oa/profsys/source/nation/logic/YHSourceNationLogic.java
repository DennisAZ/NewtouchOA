package yh.subsys.oa.profsys.source.nation.logic;
import java.sql.Connection;
import java.util.Map;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.source.nation.data.YHSourceNation;

public class YHSourceNationLogic {
  /**
   * 新建国家
   * 
   * @return
   * @throws Exception
   */
  public static void addNation(Connection dbConn, YHSourceNation nation) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, nation);
  }
  /**
   * 修改国家
   * 
   * @return
   * @throws Exception
   */
  public static void updateNation(Connection dbConn, YHSourceNation nation) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, nation);
  }

  /**
   * 查询国家详情信息
   * 
   * @return
   * @throws Exception
   */
  public static YHSourceNation showDetail(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    YHSourceNation nation = (YHSourceNation)orm.loadObjSingle(dbConn,YHSourceNation.class,seqId);
    return nation;
  }
  /**
   * 删除国家详情信息
   * 
   * @return
   * @throws Exception
   */
  public static void deleteInfo(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn,YHSourceNation.class,seqId);
  } 
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public static String selectNation(Connection dbConn,Map request,YHSourceNation nation) throws Exception {
    String sql = "select SEQ_ID,NAT_NUM,NAT_NAME,NAT_STATUS,NAT_CUSTOM"
      + ",NAT_BACKGROUND,NAT_NOTE,ATTACHMENT_ID,ATTACHMENT_NAME"
      + "  FROM oa_src_nation where 1=1 ";
    if (!YHUtility.isNullorEmpty(nation.getNatNum())) {
      sql += " and NAT_NUM like '%" +  YHUtility.encodeLike(nation.getNatNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(nation.getNatName())) {
      sql += " and NAT_NAME like '%" +  YHUtility.encodeLike(nation.getNatName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(nation.getNatStatus())) {
      sql += " and NAT_STATUS like '%" +  YHUtility.encodeLike(nation.getNatStatus()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(nation.getNatCustom())) {
      sql += " and NAT_CUSTOM like '%" +  YHUtility.encodeLike(nation.getNatCustom()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(nation.getNatBackground())) {
      sql += " and NAT_BACKGROUND like '%" +  YHUtility.encodeLike(nation.getNatBackground()) + "%' " + YHDBUtility.escapeLike();
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
}
