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
import yh.subsys.oa.finance.data.YHChargeExpense;

public class YHChargeExpenseLogic {
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public String expenseSelect(Connection dbConn,Map request,String deptId,String chargeUser,Date statrTime,Date endTime,String chargeItem,String financeAuditUser,String chargeMoney,String expense) throws Exception {   
    String sql = "select p.SEQ_ID,b.DEPT_NAME,sn.user_name,bu.BUDGET_ITEM,p.CHARGE_MONEY,p.CHARGE_DATE,"
      + "p.RUN_ID,p.IS_PRINT,p.EXPENSE,p.OF_EX,p.CHARGE_ITEM,p.CHARGE_MEMO,p.DEPT_AUDIT_USER,p.DEPT_AUDIT_DATE,"
      + "p.DEPT_AUDIT_CONTENT,p.FINANCE_AUDIT_USER,p.FINANCE_AUDIT_DATE,p.FINANCE_AUDIT_CONTENT,"
      + "p.CHARGE_YEAR,p.COST_ID,p.PROJ_SIGN,p.BUDGET_ID,p.MAKE_WASTE,p.SETTLE_FLAG"
      + " from oa_cost_charge p left outer join oa_department b on p.DEPT_ID=b.SEQ_ID "
      + " left outer join person sn on sn.SEQ_ID=p.CHARGE_USER "
      + " left outer join oa_ration_apply bu on bu.seq_Id=p.BUDGET_ID where 1=1 ";
    if (!YHUtility.isNullorEmpty(expense) && !expense.equals("3")) {
      sql += " and p.EXPENSE='" + expense + "'";
    }
    if (expense.equals("3")) {
      sql += " and p.OF_EX='1'";
    }
    if (!YHUtility.isNullorEmpty(deptId)) {
      sql += " and p.DEPT_ID='" + deptId + "'";
    }
    if (!YHUtility.isNullorEmpty(chargeItem)) {
      sql += " and bu.BUDGET_ITEM like '%" + YHUtility.encodeLike(chargeItem) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(chargeUser)) {
      sql += " and p.CHARGE_USER='" + chargeUser + "'";
    }
    if (!YHUtility.isNullorEmpty(financeAuditUser)) {
      sql += " and p.FINANCE_AUDIT_USER='" + financeAuditUser + "'";
    }
    if (!YHUtility.isNullorEmpty(chargeMoney)) {
      sql += " and p.CHARGE_MONEY >=" + chargeMoney;
    }
    if (statrTime != null) {
      String str =  YHDBUtility.getDateFilter("p.CHARGE_DATE", YHUtility.getDateTimeStr(statrTime), ">=");
      sql += " and " + str;
    }
    if (endTime != null) {
      String str =  YHDBUtility.getDateFilter("p.CHARGE_DATE", YHUtility.getDateTimeStr(endTime), "<=");
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
  public String expenseSelect2(Connection dbConn,Map request,String deptId,String chargeUser,Date statrTime,Date endTime,String chargeItem,String financeAuditUser,String chargeMoney,String expense,int year) throws Exception {
    String sql = "select p.SEQ_ID,b.DEPT_NAME,sn.user_name,bu.BUDGET_ITEM,p.CHARGE_MONEY,p.CHARGE_DATE,"
      + "p.RUN_ID,p.IS_PRINT,p.EXPENSE,p.OF_EX,p.CHARGE_ITEM,p.CHARGE_MEMO,p.DEPT_AUDIT_USER,p.DEPT_AUDIT_DATE,"
      + "p.DEPT_AUDIT_CONTENT,p.FINANCE_AUDIT_USER,p.FINANCE_AUDIT_DATE,p.FINANCE_AUDIT_CONTENT,"
      + "p.CHARGE_YEAR,p.COST_ID,p.PROJ_SIGN,p.BUDGET_ID,p.MAKE_WASTE,p.SETTLE_FLAG"
      + " from oa_cost_charge p left outer join oa_department b on p.DEPT_ID=b.SEQ_ID "
      + " left outer join person sn on sn.SEQ_ID=p.CHARGE_USER " 
      + " left outer join oa_ration_apply bu on bu.seq_Id=p.BUDGET_ID where 1=1 ";
    //    if (!YHUtility.isNullorEmpty(expense) && !expense.equals("3")) {
    //      sql += " and EXPENSE=" + expense;
    //    }
    //    if (expense.equals("3")) {
    //      sql += " and OF_EX=1";
    //    }
    if (!YHUtility.isNullorEmpty(deptId)) {
      sql += " and p.DEPT_ID='" + deptId + "'";
    }
    if (!YHUtility.isNullorEmpty(chargeItem)) {
      sql += " and bu.BUDGET_ITEM like '%" + YHUtility.encodeLike(chargeItem) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(chargeUser)) {
      sql += " and p.CHARGE_USER='" + chargeUser + "'";
    }
    if (!YHUtility.isNullorEmpty(financeAuditUser)) {
      sql += " and p.FINANCE_AUDIT_USER='" + financeAuditUser + "'";
    }
    if (!YHUtility.isNullorEmpty(chargeMoney)) {
      sql += " and p.CHARGE_MONEY >=" + chargeMoney;
    }
    if (statrTime != null) {
      String str =  YHDBUtility.getDateFilter("p.CHARGE_DATE", YHUtility.getDateTimeStr(statrTime), ">=");
      sql += " and " + str;
    }
    if (endTime != null) {
      String str =  YHDBUtility.getDateFilter("p.CHARGE_DATE", YHUtility.getDateTimeStr(endTime), "<=");
      sql += " and " + str;
    }
    if (year > 0) {
      sql += " and p.CHARGE_YEAR=" + year;
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }

  /***
   * 根据条件修改状态数据
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
    String sql = "update oa_cost_charge set expense='1' where SEQ_ID in (" + seqId + ")";
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
   * 根据条件修改状态数据
   * @return
   * @throws Exception 
   */
  public void makeWaste(Connection dbConn,String seqIdStr) throws Exception {
    String seqId = "";
    for (int i = 0 ; i < seqIdStr.split(",").length; i++) {
      if (i == seqIdStr.split(",").length - 1) {
        seqId += seqIdStr.split(",")[i];
      } else {
        seqId += seqIdStr.split(",")[i] + ",";
      }
    }
    String sql = "update oa_cost_charge set OF_EX='1' where SEQ_ID in (" + seqId + ")";
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
   * 根据条件修改状态数据
   * @return
   * @throws Exception 
   */
  public void updatePrint(Connection dbConn,String seqIdStr) throws Exception {
    String seqId = "";
    for (int i = 0 ; i < seqIdStr.split(",").length; i++) {
      if (i == seqIdStr.split(",").length - 1) {
        seqId += seqIdStr.split(",")[i];
      } else {
        seqId += seqIdStr.split(",")[i] + ",";
      }
    }
    String sql = "update oa_cost_charge set IS_PRINT='1' where SEQ_ID in (" + seqId + ")";
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
   * 总金额
   * @return
   * @throws Exception 
   */
  public double sunMoney(Connection dbConn,String deptId,int year) throws Exception {
    String sql = "select sum(charge_money) from oa_cost_charge where dept_id=? and charge_year=?";
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    double money = 0.00;
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,deptId);
      stmt.setInt(2,year);
      rs = stmt.executeQuery();
      if(rs.next()){
        money = rs.getDouble(1);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return money;
  }
  /***
   * 根据budgetId得到总金额

   * @return
   * @throws Exception 
   */
  public double sunMoneyByBudgetId(Connection dbConn,String budgetId) throws Exception {
    String sql = "select sum(charge_money) from oa_cost_charge where BUDGET_ID=? and (MAKE_WASTE = '0' or MAKE_WASTE is null )";
    ResultSet rs = null;
    PreparedStatement stmt = null ;
    double money = 0.00;
    try {
      stmt = dbConn.prepareStatement(sql);
      stmt.setString(1,budgetId);
      rs = stmt.executeQuery();
      if(rs.next()){
        money = rs.getDouble(1);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return money;
  }

  /***
   * 根据条件查询数据
   * @return
   * @throws Exception 
   */
  public List<YHChargeExpense> printSeqId(Connection dbConn,String seqIdStr) throws Exception {
    String seqId = "";
    for (int i = 0 ; i < seqIdStr.split(",").length; i++) {
      if (i == seqIdStr.split(",").length - 1) {
        seqId += seqIdStr.split(",")[i];
      } else {
        seqId += seqIdStr.split(",")[i] + ",";
      }
    }
    String sql = "select p.SEQ_ID as seqId,b.SEQ_ID,b.DEPT_NAME as beptName,sn.SEQ_ID,sn.user_name as userName,"
      + "bu.seq_Id,bu.BUDGET_ITEM as budgetItem,p.CHARGE_DATE as chargeDate,p.CHARGE_ITEM as chargeItem,p.BUDGET_ID"
      + " from oa_cost_charge p "
      + " left outer join oa_department b on p.DEPT_ID=b.SEQ_ID "
      + " left outer join person sn  on sn.SEQ_ID=p.CHARGE_USER " 
      + " left outer join oa_ration_apply bu on bu.seq_Id=p.BUDGET_ID where p.SEQ_ID in (" + seqId +")";
    ResultSet rs = null;
    PreparedStatement stmt = null ; 
    YHChargeExpense expense = null;
    List<YHChargeExpense> list = new ArrayList<YHChargeExpense>();                                                                  
    try {
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        expense = new YHChargeExpense(); 
        expense.setSeqId(rs.getInt("seqId"));
        expense.setDeptId(rs.getString("beptName"));
        expense.setChargeDate(rs.getDate("chargeDate"));
        expense.setChargeUser(rs.getString("userName"));
        expense.setChargeMemo(rs.getString("budgetItem"));
        expense.setChargeItem(rs.getString("chargeItem"));
        list.add(expense);
      }
    }catch (Exception e) {
      throw e;
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
    return list;
  }
  /***
   * 工作流


   * @return
   * @throws Exception 
   */
  public void addFlow(Connection dbConn,YHChargeExpense expense) {
    String sql = "insert into oa_cost_charge(DEPT_ID,CHARGE_USER,CHARGE_DATE,CHARGE_ITEM,CHARGE_MONEY," 
      + "CHARGE_MEMO,DEPT_AUDIT_USER,DEPT_AUDIT_DATE,DEPT_AUDIT_CONTENT,"
      + "FINANCE_AUDIT_USER,FINANCE_AUDIT_DATE,FINANCE_AUDIT_CONTENT,RUN_ID,"
      + "CHARGE_YEAR,PROJ_ID,COST_ID,PROJ_SIGN,IS_PRINT,EXPENSE,OF_EX,BUDGET_ID,MAKE_WASTE,SETTLE_FLAG)" 
      + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    ResultSet rs = null;
    PreparedStatement stmt = null;
    try {
      stmt= dbConn.prepareStatement(sql);
      stmt.setString(1,expense.getDeptId());
      stmt.setString(2,expense.getChargeUser());
      stmt.setDate(3,(Date)expense.getChargeDate());
      stmt.setString(4,expense.getChargeItem());
      stmt.setDouble(5,expense.getChargeMoney());
      stmt.setString(6,expense.getChargeMemo());
      stmt.setString(7,expense.getDeptAuditUser());
      stmt.setDate(8,(Date)expense.getDeptAuditDate());
      stmt.setString(9,expense.getDeptAuditContent());
      stmt.setString(10,expense.getFinanceAuditUser());
      stmt.setDate(11,(Date)expense.getFinanceAuditDate());
      stmt.setString(12,expense.getFinanceAuditContent());
      stmt.setInt(13,expense.getRunId());
      stmt.setInt(14,expense.getChargeYear());
      stmt.setInt(15,expense.getProjId());
      stmt.setInt(16,expense.getCostId());
      stmt.setInt(17,expense.getProjSign());
      stmt.setString(18,"0");
      stmt.setString(19,expense.getExpense());
      stmt.setString(20,"0");
      stmt.setString(21,expense.getBudgetId());
      stmt.setString(22,"0");
      stmt.setString(23,"0");
      stmt.executeUpdate();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }finally {
      YHDBUtility.close(stmt, rs, null);
    }
  }
}
