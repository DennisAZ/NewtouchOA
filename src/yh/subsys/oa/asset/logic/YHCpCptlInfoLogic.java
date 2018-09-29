package yh.subsys.oa.asset.logic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import yh.subsys.oa.asset.data.YHCpCptlInfo;
import yh.subsys.oa.asset.data.YHCpCptlRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHCpCptlInfoLogic {
  private static Logger log = Logger.getLogger(YHCpCptlInfoLogic.class);
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public String assetSelect(Connection dbConn,Map request,YHCpCptlInfo cp,String cpreFlag) throws Exception {
    String sql = "select cp.SEQ_ID,cp.CPTL_NO,cpa.TYPE_NAME,cp.CPTL_NAME,cp.TYPE_ID,cp.USE_STATE,cp.USE_FOR,"
      + "cp.CPTL_VAL,cp.CPTL_QTY,cp.CREATE_DATE,cp.SAFEKEEPING,cp.KEEPER,cp.REMARK,cp.LIST_DATE,cp.CPTL_SPEC,"
      + "cp.NO_DEAL,cp.IN_FINANCE,cp.USE_USER,cp.AFTER_INDATE,cp.GET_DATE,cp.USE_DEPT from oa_asset_info cp"
      + " left outer join oa_asset_type cpa on cpa.SEQ_ID=cp.CPTL_SPEC where 1=1 ";
    if (cpreFlag.equals("1")) {
      sql += " and (cp.USE_USER is null or cp.USE_USER='' ) "; 
    }
    if (cpreFlag.equals("2")) {
      sql += " and (cp.USE_USER is not null or cp.USE_USER<> '' ) ";  
    }
    if (!YHUtility.isNullorEmpty(cp.getCptlNo())) {
      //String cptlNo = cp.getCptlNo().replaceAll("'","''").replace("\\", "\\\\").replace("%","\\%").replace("_", "\\_").replace("％", "\\％");
      sql += " and cp.CPTL_NO like '%" + YHDBUtility.escapeLike(cp.getCptlNo()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getCptlName())) {
      //String cptlName = cp.getCptlName().replaceAll("'","''").replace("\\", "\\\\").replace("%","\\%").replace("_", "\\_").replace("％", "\\％");
      sql += " and cp.CPTL_NAME like '%" + YHDBUtility.escapeLike(cp.getCptlName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getCptlSpec())) {
      //String cptlSpec = cp.getCptlSpec().replaceAll("'","''").replace("\\", "\\\\").replace("%","\\%").replace("_", "\\_").replace("％", "\\％");
      sql += " and cp.CPTL_SPEC like '%" + YHDBUtility.escapeLike(cp.getCptlSpec()) + "%' " + YHDBUtility.escapeLike();
    }
    if (cp.getSeqId() > 0) {
      sql += " and cp.SEQ_ID like '%" + cp.getSeqId() + "%'";
    }
    if (!YHUtility.isNullorEmpty(cp.getTypeId())) {
      // String typeId = cp.getTypeId().replaceAll("'","''").replace("\\", "\\\\").replace("%","\\%").replace("_", "\\_").replace("％", "\\％");
      sql += " and cp.TYPE_ID like '%" + YHDBUtility.escapeLike(cp.getTypeId()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getUseState())) {
      //String useState = cp.getUseState().replaceAll("'","''").replace("\\", "\\\\").replace("%","\\%").replace("_", "\\_").replace("％", "\\％");
      sql += " and cp.USE_STATE like '%" + YHDBUtility.escapeLike(cp.getUseState()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getUseFor())) {
      //String useFor = cp.getUseFor().replaceAll("'","''").replace("\\", "\\\\").replace("%","\\%").replace("_", "\\_").replace("％", "\\％");
      sql += " and cp.USE_FOR like '%" + YHDBUtility.escapeLike(cp.getUseFor()) + "%' " + YHDBUtility.escapeLike();
    }
    sql += "  order by cp.SEQ_ID desc ";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 根据条件修改数据
   * @return
   * @throws Exception 
   */
  public void asset(Connection dbConn,YHCpCptlInfo cp) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    try {
      String seqId = selectDept(dbConn,cp.getUseDept());
      String useId = selectUser(dbConn,cp.getUseUser());
      String sql = "update oa_asset_info set USE_DEPT='" + seqId + "'"
      + ",USE_USER='"+ useId + "',USE_STATE='3',NO_DEAL='1',IN_FINANCE='1'  where SEQ_ID=" + cp.getSeqId();
      stmt = dbConn.prepareStatement(sql);
      stmt.executeUpdate();
    } catch (Exception e) {
      //System.out.println(e.getMessage());
    } finally {
      YHDBUtility.close(stmt,rs,log);
    }
  }
  
  /***
   * 根据查询数据，部门ID
   * @return
   * @throws Exception 
   */
  public String selectDept(Connection dbConn,String deptName) throws Exception {
    ResultSet rs = null;
    String seqId = "";
    PreparedStatement stmt = null ;
    try {
      String sql = "select seq_id,dept_name from oa_department where dept_name=?";
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,deptName);
      rs = stmt.executeQuery();
      if (rs.next()) {
        seqId = rs.getString("seq_id");
      }
    } catch (Exception e) {
      //System.out.println(e.getMessage());
    } finally {
      YHDBUtility.close(stmt,rs,log);
    }
    return seqId;
  }
  
  /***
   * 根据查询数据，用户Id
   * @return
   * @throws Exception 
   */
  public String selectUser(Connection dbConn,String useName) throws Exception {
    ResultSet rs = null;
    String seqId = "";
    PreparedStatement stmt = null ;
    try {
      String sql = "select seq_id,user_name from person where user_name=?";
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,useName);
      rs = stmt.executeQuery();
      if (rs.next()) {
        seqId = rs.getString("seq_id");
      }
    } catch (Exception e) {
      //System.out.println(e.getMessage());
    } finally {
      YHDBUtility.close(stmt,rs,log);
    }
    return seqId;
  }

  /***
   * 根据条件修改数据
   * @return
   * @throws Exception 
   */
  public void udpateAsset(Connection dbConn,YHCpCptlInfo cp) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    try {
      String keeper = selectUser(dbConn,cp.getKeeper());
      String sql = "update oa_asset_info set "
        + "USE_DEPT='',USE_USER='',USE_STATE='1',NO_DEAL='1',IN_FINANCE='1' ";
      if (!YHUtility.isNullorEmpty(cp.getKeeper())) {
        sql += ",KEEPER='" + keeper + "'";
      }
      if (!YHUtility.isNullorEmpty(cp.getRemark())) {
        sql += ",REMARK='" + cp.getRemark().replace("'", "''") + "'";
      }
      sql += "  where SEQ_ID=" + cp.getSeqId();
      stmt = dbConn.prepareStatement(sql);
      stmt.executeUpdate();
    } catch (Exception e) {
      //System.out.println(e.getMessage());
    } finally {
      YHDBUtility.close(stmt,rs,log);
    }
  }

  /***
   * 增加领用单
   * @return
   * @throws Exception 
   */
  public void assetRunId(Connection dbConn,YHCpCptlRecord record) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    String user = selectUser(dbConn,record.getCpreUser());//名字
    String keeper = selectUser(dbConn,record.getCpreKeeper());//名字
    try { 
      String sql = "insert into oa_asset_record(CPTL_ID,CPRE_QTY,CPRE_USER,"
        + "CPRE_DATE,CPRE_RECORDER,CPRE_FLAG,RUN_ID,CPRE_KEEPER,CPRE_MEMO,DEPT_ID)values(?,?,?,?,?,?,?,?,?,?)";
      stmt = dbConn.prepareStatement(sql);
      stmt.setInt(1,record.getCptlId());
      stmt.setInt(2,record.getCpreQty());
      stmt.setString(3,user);
      stmt.setDate(4,record.getCpreDate());
      stmt.setString(5,user);
      stmt.setString(6,record.getCpreFlag());
      stmt.setInt(7,record.getRunId());
      stmt.setString(8,keeper);
      stmt.setString(9,record.getCpreMemo());
      stmt.setInt(10,record.getDeptId());
      stmt.executeUpdate();
    } catch (Exception e) {
      //System.out.println(e.getMessage());
    } finally {
      YHDBUtility.close(stmt,rs,log);
    }
  }

  public static List<YHCpCptlInfo> nameList(Connection dbConn,String useFlag) {
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    YHCpCptlInfo cpInfo = null;
    List<YHCpCptlInfo> list = new ArrayList<YHCpCptlInfo>();
    String sql = "SELECT SEQ_ID,CPTL_NAME,CPTL_NO,CPTL_SPEC from oa_asset_info where 1=1";
    if (useFlag.equals("1")) {
      sql += " and USE_USER is null or USE_USER='' ";
    }
    if (useFlag.equals("2")) {
      sql += " and (USE_USER is not null or USE_USER<> '' ) ";
    }
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        cpInfo =  new YHCpCptlInfo();
        cpInfo.setSeqId(rs.getInt("SEQ_ID"));
        cpInfo.setCptlName(rs.getString("CPTL_NAME"));
        cpInfo.setCptlNo(rs.getString("CPTL_NO"));
        cpInfo.setCptlSpec(rs.getString("CPTL_SPEC"));
        list.add(cpInfo);
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(stmt,null,log);
    } 
    return list;
  }

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public String cpcpSelect(Connection dbConn,Map request,String name) throws Exception {
    String sql = "select cp.SEQ_ID,cp.CPTL_NO,cp.CPTL_NAME,cp.CPTL_VAL,cp.CPTL_QTY,cpa.TYPE_NAME,"
      + "cp.LIST_DATE,cp.TYPE_ID,cp.CREATE_DATE,"
      + "cp.SAFEKEEPING,cp.KEEPER,cp.REMARK,cp.NO_DEAL,cp.IN_FINANCE,cp.USE_USER,cp.USE_STATE,cp.USE_FOR,"
      + "cp.AFTER_INDATE,cp.GET_DATE,cp.USE_DEPT,cp.CPTL_SPEC from oa_asset_info cp"
      + " left outer join oa_asset_type cpa on cpa.SEQ_ID=cp.CPTL_SPEC where cp.USE_USER='" + name + "'"
      + "  order by cp.LIST_DATE desc ";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public String querySelect(Connection dbConn,Map request,YHCpCptlInfo cp,double getCptlValMax) throws Exception {
    String sql = "select cp.SEQ_ID,cp.CPTL_NO,cp.CPTL_NAME,cp.CPTL_VAL,cp.CPTL_QTY,cpa.TYPE_NAME,"
      + "cp.LIST_DATE,cp.USE_USER,cp.TYPE_ID,cp.CREATE_DATE,"
      + "cp.SAFEKEEPING,cp.KEEPER,cp.REMARK,cp.NO_DEAL,cp.IN_FINANCE,cp.USE_STATE,cp.USE_FOR,"
      + "cp.AFTER_INDATE,cp.GET_DATE,cp.USE_DEPT,cp.CPTL_SPEC from oa_asset_info cp"
      + " left outer join oa_asset_type cpa on cpa.SEQ_ID=cp.CPTL_SPEC where 1=1 ";
    if (!YHUtility.isNullorEmpty(cp.getCptlNo())) {
      sql += " and cp.CPTL_NO like '%" + YHDBUtility.escapeLike(cp.getCptlNo()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getCptlName())) {
      sql += " and cp.CPTL_NAME like '%" + YHDBUtility.escapeLike(cp.getCptlName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getCptlSpec())) {
      sql += " and cp.CPTL_SPEC='" + cp.getCptlSpec() + "'";
    }
    if (cp.getCptlVal() > 0) {
      sql += " and cp.CPTL_VAL >= " + cp.getCptlVal();
    }
    if (getCptlValMax > 0) {
      sql += " and cp.CPTL_VAL <= " + getCptlValMax;
    }
    if (cp.getListDate() != null) {
      String str =  YHDBUtility.getDateFilter("cp.LIST_DATE", YHUtility.getDateTimeStr(cp.getListDate()), ">=");
      sql += " and " + str;
    }
    if (cp.getGetDate() != null) {
      String str =  YHDBUtility.getDateFilter("cp.LIST_DATE", YHUtility.getDateTimeStr(cp.getGetDate()), "<=");
      sql += " and " + str;
    }
    if (!YHUtility.isNullorEmpty(cp.getKeeper())) {
      sql += " and cp.KEEPER like '%" + YHDBUtility.escapeLike(cp.getKeeper()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getSafekeeping())) {
      sql += " and cp.SAFEKEEPING like '%" + YHDBUtility.escapeLike(cp.getSafekeeping()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getRemark())) {
      sql += " and cp.REMARK like '%" + YHDBUtility.escapeLike(cp.getRemark()) + "%' " + YHDBUtility.escapeLike();
    }
    sql += "  order by cp.LIST_DATE desc ";
    
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public String applySelect(Connection dbConn,Map request) throws Exception {
    String sql = "select cp.SEQ_ID,cp.CPTL_NO,cp.CPTL_NAME,cp.CPTL_VAL,cp.CPTL_QTY,cpa.TYPE_NAME,"
      + "cp.LIST_DATE,cp.TYPE_ID,cp.CREATE_DATE,"
      + "cp.SAFEKEEPING,cp.KEEPER,cp.REMARK,cp.NO_DEAL,cp.IN_FINANCE,cp.USE_USER,cp.USE_STATE,cp.USE_FOR,"
      + "cp.AFTER_INDATE,cp.GET_DATE,cp.USE_DEPT,cp.CPTL_SPEC from oa_asset_info cp"
      + " left outer join oa_asset_type cpa on cpa.SEQ_ID=cp.CPTL_SPEC where cp.USE_USER is null or cp.USE_USER='' "
      + "  order by cp.LIST_DATE desc ";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public String listSelect(Connection dbConn,Map request,YHCpCptlInfo cp,double getCptlValMax) throws Exception {
    String sql = "select cp.SEQ_ID,cp.CPTL_NO,cp.CPTL_NAME,cp.CPTL_VAL,cp.CPTL_QTY,cpa.TYPE_NAME,"
      + "cp.LIST_DATE,cp.USE_USER,cp.TYPE_ID,cp.CREATE_DATE,"
      + "cp.SAFEKEEPING,cp.KEEPER,cp.REMARK,cp.NO_DEAL,cp.IN_FINANCE,cp.USE_STATE,cp.USE_FOR,"
      + "cp.CPTL_SPEC,cp.AFTER_INDATE,cp.GET_DATE,cp.USE_DEPT from oa_asset_info cp"
      + " left outer join oa_asset_type cpa on cpa.SEQ_ID=cp.CPTL_SPEC where 1=1 ";
    if (!YHUtility.isNullorEmpty(cp.getCptlNo())) {
      //sql += " and bu.BUDGET_ITEM like '%" + YHUtility.encodeLike(chargeItem) + "%' " + YHDBUtility.escapeLike();
     // String useState = cp.getCptlNo().replaceAll("'","''").replace("\\", "\\\\").replace("%","\\%").replace("_", "\\_").replace("％", "\\％");
      //sql += " and cp.CPTL_NO like '%" + useState + "%' ";
      sql += " and cp.CPTL_NO like '%" + YHDBUtility.escapeLike(cp.getCptlNo()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getCptlName())) {
      sql += " and cp.CPTL_NAME like '%" + YHDBUtility.escapeLike(cp.getCptlName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getCptlSpec())) {
      sql += " and CPTL_SPEC='" + cp.getCptlSpec() + "'";
    }
    if (cp.getCptlVal() > 0) {
      sql += " and cp.CPTL_VAL >= " + cp.getCptlVal();
    }
    if (getCptlValMax > 0) {
      sql += " and cp.CPTL_VAL <= " + getCptlValMax;
    }    
    if (cp.getListDate() != null) {
      String str =  YHDBUtility.getDateFilter("cp.LIST_DATE", YHUtility.getDateTimeStr(cp.getListDate()), ">=");
      sql += " and " + str;
    }
    if (cp.getGetDate() != null) {
      String str =  YHDBUtility.getDateFilter("cp.LIST_DATE", YHUtility.getDateTimeStr(cp.getGetDate()), "<=");
      sql += " and " + str;
    }
    
    if (!YHUtility.isNullorEmpty(cp.getKeeper())) {
      sql += " and cp.KEEPER like '%" + YHDBUtility.escapeLike(cp.getKeeper()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getSafekeeping())) {
      sql += " and cp.SAFEKEEPING like '%" + YHDBUtility.escapeLike(cp.getSafekeeping()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getRemark())) {
      sql += " and cp.REMARK like '%" + YHDBUtility.escapeLike(cp.getRemark()) + "%' " + YHDBUtility.escapeLike();
    }
    sql += "  order by cp.LIST_DATE desc ";

    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  //seqId串转换成NAME串
  public String getName(Connection dbConn,String seqId) throws Exception {
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    String sql = "select seq_id,user_name from  person where seq_id =" + seqId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String name = "";
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        name += rs.getString("user_name");
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return name;
  }
  
  //seqId串转换成NAME串
  public String getDept(Connection dbConn,String seqId) throws Exception {
    if (YHUtility.isNullorEmpty(seqId)) {
      seqId = "0";
    }
    String sql = "select seq_id,dept_name from oa_department where seq_id =" + seqId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String name = "";
    try{
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        name += rs.getString("dept_name");
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(ps, rs, null);
    }
    return name;
  }
}
