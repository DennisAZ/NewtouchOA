package yh.subsys.oa.finance.logic;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.finance.data.YHFinanceApplyRecord;
public class YHFinanceApplyRecordLogic {
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public String applySelect(Connection dbConn,Map request,String claimerName,String momey,String item,Date statrTime,Date endTime,String chequeAccount,String financeDirectorName) throws Exception {
    String sql = "select f.SEQ_ID,p.USER_NAME,f.APPLY_MONEY,f.CHEQUE_ACCOUNT,f.APPLY_MEMO,f.APPLY_ITEM,f.APPLY_DATE,"
      + "f.RUN_ID,f.EXPENSE,f.IS_PRINT,f.APPLY_YEAR,f.BUDGET_ID,f.OPERATOR,f.OPERATE_DATE,f.DEPT_DIRECTOR," 
      + "f.DEPT_DIRECTOR_DATE,f.DEPT_DIRECTOR_CONTENT,f.FINANCE_DIRECTOR,f.FINANCE_DIRECTOR_DATE,"
      + "f.FINANCE_DIRECTOR_CONTENT,f.FINANCE_SIGNATORY,f.SIGN_DATE,f.SIGN_TYPE"
      + " from oa_capital_aplly_log f "
      + " left outer join PERSON p on f.APPLY_CLAIMER = p.SEQ_ID where 1=1 and f.SIGN_TYPE=1";
    if (!YHUtility.isNullorEmpty(claimerName)) {
      sql += " and f.APPLY_CLAIMER ='" + claimerName + "'";
    }
    if (!YHUtility.isNullorEmpty(momey)) {
      sql += " and f.APPLY_MONEY >=" + momey ;
    }
    if (!YHUtility.isNullorEmpty(item)) {
      sql += " and f.APPLY_ITEM like '%" + YHUtility.encodeLike(item) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(chequeAccount)) {
      sql += " and f.CHEQUE_ACCOUNT='" + YHUtility.encodeLike(chequeAccount) + "'";
    }
    if (!YHUtility.isNullorEmpty(financeDirectorName)) {
      sql += " and f.FINANCE_DIRECTOR='" + financeDirectorName + "'";
    }
    if (statrTime != null) {
      String str =  YHDBUtility.getDateFilter("f.APPLY_DATE", YHUtility.getDateTimeStr(statrTime), ">=");
      sql += " and " + str;
    }
    if (endTime != null) {
      String str =  YHDBUtility.getDateFilter("f.APPLY_DATE", YHUtility.getDateTimeStr(endTime), "<=");
      sql += " and " + str;
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public String applySelectByBudgetId(Connection dbConn,Map request,String budgetId) throws Exception {
    String sql = "select f.SEQ_ID as SEQ_ID,p.USER_NAME as APPLY_NAME"
      + ",f.APPLY_DATE as APPLY_DATE,b.BUDGET_ITEM as BUDGET_ITEM, f.APPLY_MEMO as APPLY_MEMO"
      + ",f.CHEQUE_ACCOUNT as CHEQUE_ACCOUNT,f.APPLY_MONEY as APPLY_MONEY,f.EXPENSE as EXPENSE , f.RUN_ID as RUN_ID"
      + " from oa_capital_aplly_log f left outer join oa_ration_apply b on f.BUDGET_ID = b.SEQ_ID "
      +" left outer join PERSON p on f.APPLY_CLAIMER = p.SEQ_ID where f.SIGN_TYPE=1 and f.BUDGET_ID in('" + budgetId +"')";
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public String applySelect2(Connection dbConn,Map request,String claimerName,String momey,String item,Date statrTime,Date endTime,String chequeAccount,String financeDirectorName,int year,String deptId) throws Exception {
    String sql = "select f.SEQ_ID,p.USER_NAME,f.APPLY_MONEY,f.CHEQUE_ACCOUNT,f.APPLY_MEMO,f.APPLY_ITEM,f.APPLY_DATE,"
      + "f.RUN_ID,f.EXPENSE,f.IS_PRINT,f.APPLY_YEAR,f.BUDGET_ID,f.OPERATOR,f.OPERATE_DATE,f.DEPT_DIRECTOR," 
      + "f.DEPT_DIRECTOR_DATE,f.DEPT_DIRECTOR_CONTENT,f.FINANCE_DIRECTOR,f.FINANCE_DIRECTOR_DATE,"
      + "f.FINANCE_DIRECTOR_CONTENT,f.FINANCE_SIGNATORY,f.SIGN_DATE,f.SIGN_TYPE"
      + " from oa_capital_aplly_log f "
      + " left outer join PERSON p on f.APPLY_CLAIMER = p.SEQ_ID where 1=1 and f.SIGN_TYPE=1";
    if (!YHUtility.isNullorEmpty(claimerName)) {
      sql += " and f.APPLY_CLAIMER ='" + claimerName + "'";
    }
    if (!YHUtility.isNullorEmpty(momey)) {
      sql += " and f.APPLY_MONEY >=" + momey;
    }
    if (!YHUtility.isNullorEmpty(item)) {
      sql += " and f.APPLY_ITEM like '%" + YHUtility.encodeLike(item) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(chequeAccount)) {
      sql += " and f.CHEQUE_ACCOUNT='" + YHUtility.encodeLike(chequeAccount) + "'";
    }
    if (!YHUtility.isNullorEmpty(financeDirectorName)) {
      sql += " and f.FINANCE_DIRECTOR='" + financeDirectorName + "'";
    }
    if (statrTime != null) {
      String str =  YHDBUtility.getDateFilter("f.APPLY_DATE", YHUtility.getDateTimeStr(statrTime), ">=");
      sql += " and " + str;
    }
    if (endTime != null) {
      String str =  YHDBUtility.getDateFilter("f.APPLY_DATE", YHUtility.getDateTimeStr(endTime), "<=");
      sql += " and " + str;
    }
    if (year > 0) {
      sql += " and f.APPLY_YEAR=" + year;
    }
    if (!YHUtility.isNullorEmpty(deptId)) {
      sql += " and f.DEPT_ID='" + deptId + "'";
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
  /***
   * 根据条件查询数据
   * @return
   * @throws Exception 
   */
  public List<YHFinanceApplyRecord> printSeqId(Connection dbConn,String seqIdStr) throws Exception {
    String seqId = "";
    for (int i = 0 ; i < seqIdStr.split(",").length; i++) {
      if (i == seqIdStr.split(",").length - 1) {
        seqId += seqIdStr.split(",")[i];
      } else {
        seqId += seqIdStr.split(",")[i] + ",";
      }
    }
    String sql = "select p.SEQ_ID as SEQ_ID,p.APPLY_CLAIMER as APPLY_CLAIMER,p.APPLY_MONEY as APPLY_MONEY,"
      + "p.CHEQUE_ACCOUNT as CHEQUE_ACCOUNT,p.APPLY_MEMO as APPLY_MEMO,p.APPLY_ITEM as APPLY_ITEM,p.APPLY_DATE as APPLY_DATE,"
      + "p.RUN_ID as RUN_ID,p.EXPENSE as EXPENSE,p.IS_PRINT as IS_PRINT,p.APPLY_YEAR as APPLY_YEAR,p.BUDGET_ID as BUDGET_ID,"
      + "p.OPERATOR as OPERATOR,p.OPERATE_DATE as OPERATE_DATE,p.DEPT_DIRECTOR as DEPT_DIRECTOR," 
      + "p.DEPT_DIRECTOR_DATE as DEPT_DIRECTOR_DATE,p.DEPT_DIRECTOR_CONTENT as DEPT_DIRECTOR_CONTENT,"
      + "p.FINANCE_DIRECTOR as FINANCE_DIRECTOR,p.FINANCE_DIRECTOR_DATE as FINANCE_DIRECTOR_DATE,"
      + "p.FINANCE_DIRECTOR_CONTENT as FINANCE_DIRECTOR_CONTENT,p.FINANCE_SIGNATORY as FINANCE_SIGNATORY,"
      + "p.SIGN_DATE as SIGN_DATE,p.SIGN_TYPE as SIGN_TYPE,"
      + "b.DEPT_NAME as deptName,x.seq_id,x.user_name as user_name,pe.user_name as peName,pr.user_name as prName "
      + " from oa_capital_aplly_log p "
      + " left outer join person x on p.apply_claimer=x.seq_id "
      + " left outer join person pe on p.DEPT_DIRECTOR=pe.seq_id "
      + " left outer join person pr on p.FINANCE_DIRECTOR=pr.seq_id"
      + " left outer join oa_department b on x.dept_id=b.seq_id where p.SEQ_ID in (" + seqId + ")";
    ResultSet rs = null;
    PreparedStatement stmt = null ; 
    YHFinanceApplyRecord record = null;
    List<YHFinanceApplyRecord> list = new ArrayList<YHFinanceApplyRecord>();                                                                  
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        record = new YHFinanceApplyRecord(); 
        record.setSeqId(rs.getInt("SEQ_ID"));
        record.setApplyClaimer(rs.getString("user_name"));
        record.setApplyMoney(rs.getDouble("APPLY_MONEY"));
        record.setChequeAccount(rs.getString("CHEQUE_ACCOUNT"));
        record.setApplyMemo(rs.getString("APPLY_MEMO"));
        record.setApplyItem(rs.getString("APPLY_ITEM"));
        record.setApplyDate(rs.getDate("APPLY_DATE"));
        record.setRunId(rs.getInt("RUN_ID"));
        record.setExpense(rs.getString("EXPENSE")); 
        record.setIsPrint(rs.getString("IS_PRINT"));
        record.setApplyYear(rs.getInt("APPLY_YEAR"));
        record.setBudgetId(rs.getString("BUDGET_ID"));
        record.setOperator(rs.getString("OPERATOR"));
        record.setOperateDate(rs.getDate("OPERATE_DATE"));
        record.setFinanceDirector(rs.getString("prName"));
        record.setDeptDirector(rs.getString("peName"));
        record.setDeptDirectorContent(rs.getString("deptName"));
        list.add(record);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return list;
  }
  
  /***
   * 根据条件修改打印状态数据
   * @return
   * @throws Exception 
   */
  public void updateExpense(Connection dbConn,String seqIdStr) throws Exception {
    String seqId = "";
    for (int i = 0 ; i < seqIdStr.split(",").length; i++) {
      if (i == seqIdStr.split(",").length - 1) {
        seqId += seqIdStr.split(",")[i];
      } else {
        seqId += seqIdStr.split(",")[i] + ",";
      }
    }
    String sql = "update oa_capital_aplly_log set IS_PRINT='1' where SEQ_ID in (" + seqId + ")";
    ResultSet rs = null;
    PreparedStatement stmt = null ;                                                                 
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
  
  /***
   * 根据条件修改报销状态数据

   * @return
   * @throws Exception 
   */
  public void updateExpense2(Connection dbConn,String seqIdStr) throws Exception {
    String seqId = "";
    for (int i = 0 ; i < seqIdStr.split(",").length; i++) {
      if (i == seqIdStr.split(",").length - 1) {
        seqId += seqIdStr.split(",")[i];
      } else {
        seqId += seqIdStr.split(",")[i] + ",";
      }
    }
    String sql = "update oa_capital_aplly_log set expense='1' where SEQ_ID in (" + seqId + ")";
    ResultSet rs = null;
    PreparedStatement stmt = null ;                                                                 
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.executeUpdate();
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
  /***
   * 根据budget_id得到领用金额的总额

   * @return
   * @throws Exception 
   */
  public String chequeTotalByBudgetId (Connection dbConn,String budgetId) throws Exception {
    String sql = "select sum(APPLY_MONEY) as TOTAL from oa_capital_aplly_log where BUDGET_ID in ('" + budgetId + "')";
    ResultSet rs = null;
    PreparedStatement stmt = null ;  
    String chequeTotal = "0.00";
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      if(rs.next()){
        if( rs.getString("TOTAL")!=null){
          chequeTotal = rs.getString("TOTAL");
        }
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return chequeTotal;
  }
  /***
   * 工作流

   * @return
   * @throws Exception 
   */
  public void addFlow(Connection dbConn,YHFinanceApplyRecord record) {
    String sql = "insert into oa_capital_aplly_log(APPLY_CLAIMER,APPLY_DATE,APPLY_YEAR,APPLY_ITEM,BUDGET_ID,APPLY_MONEY,"
      + "APPLY_MEMO,OPERATOR,OPERATE_DATE,DEPT_DIRECTOR,DEPT_DIRECTOR_DATE,"
      + "DEPT_DIRECTOR_CONTENT,FINANCE_DIRECTOR,FINANCE_DIRECTOR_DATE,FINANCE_DIRECTOR_CONTENT,"
      + "FINANCE_SIGNATORY,SIGN_DATE,RUN_ID,SIGN_TYPE,CHEQUE_ACCOUNT,"
      + "EXPENSE,IS_PRINT,DEPT_ID,APPLY_PROJECT,CHARGE_ID) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    ResultSet rs = null;
    PreparedStatement stmt = null;
    try {
      stmt= dbConn.prepareStatement(sql);
      stmt.setString(1,record.getApplyClaimer());
      stmt.setDate(2,(Date)record.getApplyDate());
      stmt.setInt(3,record.getApplyYear());
      stmt.setString(4,record.getApplyItem());
      stmt.setString(5,record.getBudgetId());
      stmt.setDouble(6,record.getApplyMoney());
      stmt.setString(7,record.getApplyMemo());
      stmt.setString(8,record.getOperator());
      stmt.setDate(9,(Date)record.getOperateDate());
      stmt.setString(10,record.getDeptDirector());
      stmt.setDate(11,(Date)record.getDeptDirectorDate());
      stmt.setString(12,record.getDeptDirectorContent());
      stmt.setString(13,record.getFinanceDirector());
      stmt.setDate(14,(Date)record.getFinanceDirectorDate());
      stmt.setString(15,record.getFinanceDirectorContent());
      stmt.setString(16,record.getFinanceSignatory());
      stmt.setDate(17,(Date)record.getSignDate());
      stmt.setInt(18,record.getRunId());
      stmt.setInt(19,1);
      stmt.setString(20,record.getChequeAccount());
      stmt.setString(21,"0");
      stmt.setString(22,"0");
      stmt.setString(23,record.getDeptId());
      stmt.setString(24,record.getApplyProject());
      stmt.setString(25,"0");
      stmt.executeUpdate();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
}
