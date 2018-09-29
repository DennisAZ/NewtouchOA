package yh.subsys.oa.finance.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.finance.data.YHBudgetDeptTotal;

public class YHBudgetDeptTotalLogic {
  private static Logger log = Logger.getLogger(YHBudgetDeptTotalLogic.class);
  public int addBudgetTotal(Connection dbConn,YHBudgetDeptTotal budgetTotal) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, budgetTotal);
    return 0;//YHCalendarLogic.getMaSeqId(dbConn, "GIFT_INSTOCK");
  }
  public List<YHBudgetDeptTotal> selectBudgetTotal(Connection dbConn,String str[]) throws Exception {
    List<YHBudgetDeptTotal> totalList = new ArrayList<YHBudgetDeptTotal>();
    YHORM orm = new YHORM();
    totalList = orm.loadListSingle(dbConn, YHBudgetDeptTotal.class,str );
    return totalList;//YHCalendarLogic.getMaSeqId(dbConn, "GIFT_INSTOCK");
  }
  public void updateTotal(Connection dbConn,String deptId,String year,String money) throws Exception {
    YHORM orm = new YHORM();
    Statement stmt = null;
    ResultSet rs = null; 
    String budgetMoneyTotal = "0";
    money = money.replace(",", "");
    try {
      String sql = "update oa_ration_dept_total set TOTAL = TOTAL + "+money +" where DEPT_ID='" + deptId + "' and CUR_YEAR = " + year;  ;
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页根据预算的SEQ_ID (BUDGET_ID)
   * @return
   * @throws Exception 
   */
  public  List<Map<String,String>> expenseSelectByBudgetId(Connection dbConn,Map request,String budgetId) throws Exception {
    List<Map<String,String>> expenseList = new ArrayList<Map<String,String>>();
    String sql = "select p.SEQ_ID,b.DEPT_NAME,sn.USER_NAME,bu.BUDGET_ITEM,p.CHARGE_MONEY,p.CHARGE_DATE"
      + ",snp.USER_NAME,p.RUN_ID,p.IS_PRINT,p.EXPENSE,p.CHARGE_MEMO"
      + " from oa_cost_charge p left outer join oa_department b on p.DEPT_ID=b.SEQ_ID "
      + " left outer join person sn on sn.SEQ_ID=p.CHARGE_USER " 
      + " left outer join person snp on snp.SEQ_ID=p.FINANCE_AUDIT_USER " 
      + " left outer join oa_ration_apply bu on bu.seq_Id=p.BUDGET_ID where p.BUDGET_ID = '" + budgetId + "' ";//and (MAKE_WASTE = '0' or MAKE_WASTE is null)";
   // YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
  // YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    //return pageDataList.toJson();
    Statement stmt = null;
    ResultSet rs = null; 
    String budgetMoneyTotal = "0";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while(rs.next()){
        Map<String,String> map = new HashMap<String,String>();
        map.put("seqId",rs.getString(1));
        map.put("deptName",rs.getString(2));
        map.put("userName",rs.getString(3));
        map.put("budgetItem",rs.getString(4));
        map.put("chargeMoney",rs.getString(5));
        map.put("chargeDate",YHUtility.getDateTimeStr(rs.getTimestamp(6)));
        map.put("financeAuditUser",rs.getString(7));
        map.put("runId",rs.getString(8));
        map.put("isPrint",rs.getString(9));
        map.put("expense",rs.getString(10));
        map.put("chargeMemo",rs.getString(11));
        expenseList.add(map);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }

    return expenseList;
  }
}
