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
import yh.subsys.oa.asset.data.YHCpcptl;
import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHCpCptlRecordLogic {
  private static Logger log = Logger.getLogger(YHCpCptlRecordLogic.class);

  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception
   */
  public String listSelect(Connection dbConn,Map request,YHCpCptlInfo cp,YHCpCptlRecord cptlRecord) throws Exception {
    String sql = "select p.SEQ_ID as seqId,up.CPTL_NO as cptlNo,up.CPTL_NAME as cptlName,cpa.TYPE_NAME as typeName,"
      + "up.CPTL_QTY as cptlQty,p.cpre_place as cprePlace,p.CPRE_USER as cpreUser,up.KEEPER as keeper,"
      + "p.CPRE_MEMO as cpreMemo,p.RUN_ID as runId,p.CPTL_ID as cptlId,p.CPRE_FLAG as cpreFlag,p.DEPT_ID as deptId,"
      + "p.CPRE_QTY as cpreQty,up.SEQ_ID as seqId2,up.TYPE_ID as typeId,up.USE_FOR as useFor,up.CPTL_SPEC as cptlSpec,"
      + "up.USE_STATE as useState,up.USE_USER as useUser"
      + " from oa_asset_record p "
      + " left outer join oa_asset_info up on p.CPTL_ID = up.SEQ_ID "
      + " left outer join oa_asset_type cpa on cpa.SEQ_ID=up.CPTL_SPEC "   
      + " where p.CPRE_FLAG=" + cptlRecord.getCpreFlag();
    
    if (cptlRecord.getDeptId() > 0) {
      sql += " and p.DEPT_ID=" + cptlRecord.getDeptId();
    }
    if (!YHUtility.isNullorEmpty(cp.getCptlName())) {
      sql += " and up.CPTL_NAME like '%" + YHDBUtility.escapeLike(cp.getCptlName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getCptlSpec())) {
      sql += " and up.CPTL_SPEC='" + cp.getCptlSpec() + "'";
    }
    if (!YHUtility.isNullorEmpty(cp.getTypeId())) {
      sql += " and up.TYPE_ID='" + cp.getTypeId()+ "'";
    }
    if (!YHUtility.isNullorEmpty(cp.getUseFor())) {
      sql += " and up.USE_FOR='" + cp.getUseFor() + "'";
    }
    if (!YHUtility.isNullorEmpty(cp.getUseState())) {
      sql += " and up.USE_STATE='" + cp.getUseState() + "'";
    }
    if (!YHUtility.isNullorEmpty(cp.getUseUser())) {
      sql += " and p.CPRE_USER like '%" + YHDBUtility.escapeLike(cp.getUseUser()) + "%' " + YHDBUtility.escapeLike();
    }
    sql += "  order by p.SEQ_ID desc";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 查询数据
   * @return
   * @throws Exception 
   */
  public List<YHCpcptl> getCpreFlag1(Connection dbConn,int seqId) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    List<YHCpcptl> list = new ArrayList<YHCpcptl>();
    YHCpcptl cpcptl = null;
    String sql = "select p.SEQ_ID as seqId,p.CPTL_ID as cptlId,p.CPRE_USER as cpreUser,p.CPRE_QTY as cpreQty,"
      + "p.CPRE_MEMO as cpreMemo,p.CPRE_FLAG as cpreFlag,p.DEPT_ID as deptId,p.RUN_ID as runId,"
      + "up.SEQ_ID as seqId2,up.CPTL_NO as cptlNo,up.CPTL_NAME as cptlName,up.CPTL_SPEC as cptlSpec,"
      + "up.CPTL_QTY as cptlQty,up.KEEPER as keeper,up.TYPE_ID as typeId,up.USE_FOR as useFor,"
      + "up.USE_STATE as useState,up.USE_USER as useUser,up.USE_DEPT as useDept,p.cpre_place as safekeeping "
      + " from oa_asset_record p "
      + " left outer join oa_asset_info up on p.CPTL_ID = up.SEQ_ID "
      + " where p.CPRE_FLAG=1 and up.SEQ_ID=" + seqId;
    try {
      stmt = dbConn.prepareStatement(sql);
      //System.out.println(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        cpcptl = new YHCpcptl();
        cpcptl.setSeqId(rs.getInt("seqId"));
        cpcptl.setCptlId(rs.getString("cptlId"));
        cpcptl.setCptlNo(rs.getString("cptlNo"));
        cpcptl.setCptlName(rs.getString("cptlName"));
        cpcptl.setCptlSpec(rs.getString("cptlSpec"));
        cpcptl.setCptlQty(rs.getInt("cpreQty"));
        cpcptl.setUseDept(rs.getString("safekeeping"));
        cpcptl.setCpreUser(rs.getString("cpreUser"));
        cpcptl.setKeeper(rs.getString("keeper"));
        cpcptl.setCpreMemo(rs.getString("cpreMemo"));
        cpcptl.setDeptId(rs.getString("deptId")); 
        cpcptl.setTypeId(rs.getString("typeId"));
        cpcptl.setUseFor(rs.getString("useFor"));
        cpcptl.setUseState(rs.getString("useState"));
        cpcptl.setUseUser(rs.getString("useUser"));
        cpcptl.setCpreFlag(rs.getString("cpreFlag"));
        cpcptl.setRunId(rs.getInt("runId"));
        list.add(cpcptl);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt,rs,log);
    }
    return list;  
  }
  /***
   * 查询数据
   * @return
   * @throws Exception 
   */
  public String getName(Connection dbConn,int seqId) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    String sql = "select CPTL_NAME from oa_asset_info where SEQ_ID=" + seqId;
    String name = null;
    try {
      stmt = dbConn.prepareStatement(sql);
      //System.out.println(sql);
      rs = stmt.executeQuery();
      if (rs.next()) {
        name = rs.getString("CPTL_NAME");
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt,rs,log);
    }
    return name;  
  }


  /***
   * 查询数据
   * @return
   * @throws Exception 
   */
  public List<YHCpcptl> getCpreFlag2(Connection dbConn,int seqId) throws Exception {
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    List<YHCpcptl> list = new ArrayList<YHCpcptl>();
    YHCpcptl cpcptl = null;
    String sql = "select p.SEQ_ID as seqId,p.CPTL_ID as cptlId,p.CPRE_USER as cpreUser,p.CPRE_QTY as cpreQty,"
      + "p.CPRE_MEMO as cpreMemo,p.CPRE_FLAG as cpreFlag,p.DEPT_ID as deptId,p.RUN_ID as runId,p.CPRE_KEEPER as keeper,"
      + "up.SEQ_ID as seqId2,up.CPTL_NO as cptlNo,up.CPTL_NAME as cptlName,up.CPTL_SPEC as cptlSpec,"
      + "up.CPTL_QTY as cptlQty,up.TYPE_ID as typeId,up.USE_FOR as useFor,"
      + "up.USE_STATE as useState,up.USE_USER as useUser,up.USE_DEPT as useDept,p.cpre_place as safekeeping "
      + " from oa_asset_record p "
      + " left outer join oa_asset_info up on p.CPTL_ID=up.SEQ_ID "
      + " where p.CPRE_FLAG=2 and up.SEQ_ID=" + seqId;
    try {
      stmt = dbConn.prepareStatement(sql);
      //System.out.println(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        cpcptl = new YHCpcptl();
        cpcptl.setSeqId(rs.getInt("seqId"));
        cpcptl.setCptlId(rs.getString("cptlId"));
        cpcptl.setCptlNo(rs.getString("cptlNo"));
        cpcptl.setCptlName(rs.getString("cptlName"));
        cpcptl.setCptlSpec(rs.getString("cptlSpec"));
        cpcptl.setCptlQty(rs.getInt("cpreQty"));
        cpcptl.setUseDept(rs.getString("safekeeping"));
        cpcptl.setCpreUser(rs.getString("cpreUser"));
        cpcptl.setKeeper(rs.getString("keeper"));
        cpcptl.setCpreMemo(rs.getString("cpreMemo"));
        cpcptl.setDeptId(rs.getString("deptId")); 
        cpcptl.setTypeId(rs.getString("typeId"));
        cpcptl.setUseFor(rs.getString("useFor"));
        cpcptl.setUseState(rs.getString("useState"));
        cpcptl.setUseUser(rs.getString("useUser"));
        cpcptl.setCpreFlag(rs.getString("cpreFlag"));
        cpcptl.setRunId(rs.getInt("runId"));
        list.add(cpcptl);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt,rs,log);
    }
    return list;  
  }

  /***
   * 查询数据
   * @return
   * @throws Exception 
   */
  public void getDelete(Connection dbConn,int seqId) throws Exception {
    PreparedStatement stmt = null ;
    String sql = "delete from oa_asset_record where cptl_id=" + seqId;
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.execute();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt,null,log);
    } 
  }

  /***
   * 查询导出数据
   * @return
   * @throws Exception 
   */
  public List<YHCpcptl> importOut(Connection dbConn,YHCpCptlInfo cp,YHCpCptlRecord cptlRecord) throws Exception {
    YHCpcptl cpcptl = null;
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    List<YHCpcptl> list = new ArrayList<YHCpcptl>();    
    String sql = "select p.SEQ_ID as seqId,p.CPTL_ID as cptlId,per.USER_NAME as cpreUser,"
      + "p.CPRE_MEMO as cpreMemo,p.CPRE_FLAG as cpreFlag,p.DEPT_ID as deptId,p.CPRE_QTY as cpreQty,"
      + "up.SEQ_ID as seqId2,up.CPTL_NO as cptlNo,up.CPTL_NAME as cptlName,cpa.TYPE_NAME as cptlSpec,"
      + "up.CPTL_QTY as cptlQty,per2.USER_NAME as keeper,up.TYPE_ID as typeId,up.USE_FOR as useFor,"
      + "up.USE_STATE as useState,up.USE_USER as useUser,up.USE_DEPT as useDept,up.safekeeping as safekeeping"
      + " from oa_asset_record p "
      + " left outer join oa_asset_info up on p.CPTL_ID = up.SEQ_ID "
      + " left outer join oa_asset_type cpa on cpa.SEQ_ID=up.CPTL_SPEC "
      + " left outer join person per on per.SEQ_ID=p.CPRE_USER "
      + " left outer join person per2 on per2.SEQ_ID=up.KEEPER "
      + " where p.CPRE_FLAG=" + cptlRecord.getCpreFlag();
    if (cptlRecord.getDeptId() > 0) {
      sql += " and p.DEPT_ID=" + cptlRecord.getDeptId();
    }
    if (!YHUtility.isNullorEmpty(cp.getCptlName())) {
      //String cptlName = cp.getCptlName().replaceAll("'", "''").replace("\\", "\\\\").replace("%","\\%").replace("_", "\\_").replace("％", "\\％");
      sql += " and up.CPTL_NAME like '%" + YHDBUtility.escapeLike(cp.getCptlName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(cp.getCptlSpec())) {
      sql += " and up.CPTL_SPEC='" + cp.getCptlSpec() + "'";
    }
    if (!YHUtility.isNullorEmpty(cp.getTypeId())) {
      sql += " and up.TYPE_ID='" + cp.getTypeId() + "'";
    }
    if (!YHUtility.isNullorEmpty(cp.getUseFor())) {
      sql += " and up.USE_FOR='" + cp.getUseFor() + "'";
    }
    if (!YHUtility.isNullorEmpty(cp.getUseState())) {
      sql += " and up.USE_STATE='" + cp.getUseState() + "'";
    }
    if (!YHUtility.isNullorEmpty(cp.getUseUser())) {
      //String useUser = cp.getUseUser().replaceAll("'","''").replace("\\", "\\\\").replace("%","\\%").replace("_", "\\_").replace("％", "\\％");
      sql += " and p.CPRE_USER like '%" + YHDBUtility.escapeLike(cp.getUseUser()) + "%' " + YHDBUtility.escapeLike();
    }
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        cpcptl = new YHCpcptl();
        cpcptl.setSeqId(rs.getInt("seqId"));
        cpcptl.setCptlId(rs.getString("cptlId"));
        cpcptl.setCptlNo(rs.getString("cptlNo"));
        cpcptl.setCptlName(rs.getString("cptlName"));
        cpcptl.setCptlSpec(rs.getString("cptlSpec"));
        cpcptl.setCptlQty(rs.getInt("cpreQty"));
        cpcptl.setUseDept(rs.getString("safekeeping"));
        cpcptl.setCpreUser(rs.getString("cpreUser"));
        cpcptl.setKeeper(rs.getString("keeper"));
        cpcptl.setCpreMemo(rs.getString("cpreMemo"));
        cpcptl.setDeptId(rs.getString("deptId")); 
        cpcptl.setTypeId(rs.getString("typeId"));
        cpcptl.setUseFor(rs.getString("useFor"));
        cpcptl.setUseState(rs.getString("useState"));
        cpcptl.setUseUser(rs.getString("useUser"));
        cpcptl.setCpreFlag(rs.getString("cpreFlag"));
        list.add(cpcptl);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt,rs,log);
    }
    return list;  
  }
  
  //分类代码,去除重复数据
  public static List<YHCpCptlInfo> typeList(Connection dbConn) {
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    YHCpCptlInfo cpInfo = null;
    List<YHCpCptlInfo> list = new ArrayList<YHCpCptlInfo>();
    String sql = "SELECT TYPE_ID from oa_asset_info group by TYPE_ID";
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        cpInfo =  new YHCpCptlInfo();
        cpInfo.setTypeId(rs.getString("TYPE_ID"));
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

//使用方向,去除重复数据
  public static List<YHCpCptlInfo> useStateList(Connection dbConn) {
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    YHCpCptlInfo cpInfo = null;
    List<YHCpCptlInfo> list = new ArrayList<YHCpCptlInfo>();
    String sql = "SELECT use_state from oa_asset_info group by use_state";
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        cpInfo =  new YHCpCptlInfo();
        cpInfo.setUseState(rs.getString("use_state"));
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
   * 修改数据
   * @return
   * @throws Exception 
   */
  public void getUpdate(Connection dbConn,YHCpCptlInfo info) throws Exception {
    PreparedStatement stmt = null ;
    String sql = "update oa_asset_info set use_user=?,use_state=?,use_dept=? where seq_id=?";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,info.getUseUser());
      stmt.setString(2,info.getUseState());
      stmt.setString(3,info.getUseDept());
      stmt.setInt(4,info.getSeqId());
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt,null,log);
    } 
  }
  //导出数据
  public ArrayList<YHDbRecord> getDbRecord(List<YHCpcptl> list){
    //System.out.println(worklist.size());
    ArrayList<YHDbRecord> dbL = new ArrayList<YHDbRecord>();
    YHCpcptl cp = new YHCpcptl();
    for (int i = 0; i < list.size(); i++) {
      cp = list.get(i);
      YHDbRecord dbrec = new YHDbRecord();
      dbrec.addField("编号",cp.getCptlNo());
      dbrec.addField("固定资产名称",cp.getCptlName());
      dbrec.addField("规格型号",cp.getCptlSpec());
      dbrec.addField("数量  ",cp.getCptlQty());
      dbrec.addField("使用地点 ",cp.getUseDept());
      dbrec.addField("使用人签字  ",cp.getCpreUser());
      dbrec.addField("专管员签字",cp.getKeeper());
      dbrec.addField("备注",cp.getCpreMemo());
      dbL.add(dbrec);
    }
    return dbL;
  }
  /***
   * 修改数据
   * @return
   * @throws Exception 
   */
  public static void editAsset(Connection dbConn,YHCpCptlRecord record) throws Exception {
    PreparedStatement stmt = null ;
    String sql = "update oa_asset_record set cpre_memo=?,cpre_place=?,cpre_reason=?,dept_id=? where seq_id=?";
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1, record.getCpreMemo());
      stmt.setString(2,record.getCprePlace());
      stmt.setString(3,record.getCpreReason());
      stmt.setInt(4,record.getDeptId());
      stmt.setInt(5,record.getSeqId());
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt,null,log);
    } 
  }
}
