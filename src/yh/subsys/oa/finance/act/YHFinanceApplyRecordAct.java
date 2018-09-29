package yh.subsys.oa.finance.act;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.finance.data.YHFinanceApplyRecord;
import yh.subsys.oa.finance.logic.YHFinanceApplyRecordLogic;
public class YHFinanceApplyRecordAct {
  /**
   *查询所有(分页)通用列表显示数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String applySelect(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String claimerName = request.getParameter("claimerName");
      String momey = request.getParameter("money");
      String item = request.getParameter("item");
      String aTime = request.getParameter("statrTime");
      String aEndTime = request.getParameter("endTime");
      String chequeAccount = request.getParameter("chequeAccount");
      String financeDirectorName = request.getParameter("financeDirectorName");

      Date statrTime = null;
      Date endTime = null;
      if (!YHUtility.isNullorEmpty(aTime)) {
        statrTime = Date.valueOf(aTime);
      }
      if (!YHUtility.isNullorEmpty(aEndTime)) {
        endTime = Date.valueOf(aEndTime);
      }
      YHFinanceApplyRecordLogic gift = new YHFinanceApplyRecordLogic();
      String data = gift.applySelect(dbConn,request.getParameterMap(),claimerName,momey,item,statrTime,endTime,chequeAccount,financeDirectorName);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  /**
   *查询所有(分页)通用列表显示数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String applySelect2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String claimerName = request.getParameter("claimerName");
      String momey = request.getParameter("money").toString();
      String item = request.getParameter("item");
      String aTime = request.getParameter("statrTime");
      String aEndTime = request.getParameter("endTime");
      String chequeAccount = request.getParameter("chequeAccount");
      String financeDirectorName = request.getParameter("financeDirectorName");
      String year2 = request.getParameter("year");
      String deptId = request.getParameter("deptId");
      Date statrTime = null;
      Date endTime = null;
      int year = 0;
      if (!YHUtility.isNullorEmpty(aTime)) {
        statrTime = Date.valueOf(aTime);
      }
      if (!YHUtility.isNullorEmpty(aEndTime)) {
        endTime = Date.valueOf(aEndTime);
      }
      if (!YHUtility.isNullorEmpty(year2)) {
        year = Integer.parseInt(year2);
      }

      YHFinanceApplyRecordLogic gift = new YHFinanceApplyRecordLogic();
      String data = gift.applySelect2(dbConn,request.getParameterMap(),claimerName,momey,item,statrTime,endTime,chequeAccount,financeDirectorName,year,deptId);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   *根据budgetId查询(分页)通用列表显示数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String applySelectByBudgetId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String budgetId = request.getParameter("budgetId");
      if(budgetId==null){
        budgetId = "";
      }
      YHFinanceApplyRecordLogic gift = new YHFinanceApplyRecordLogic();
      String data = gift.applySelectByBudgetId(dbConn,request.getParameterMap(),budgetId);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delCheque(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      YHORM orm = new YHORM();//orm映射数据库
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHFinanceApplyRecord record = new YHFinanceApplyRecord();
      record.setSeqId(seqId);
      orm.deleteSingle(dbConn, record);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据成功！");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 根据ID查询数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String chequeDetail(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      YHORM orm = new YHORM();//orm映射数据库
      int seqId = Integer.parseInt(request.getParameter("seqId")); 
      YHFinanceApplyRecord record = (YHFinanceApplyRecord)orm.loadObjSingle(dbConn,YHFinanceApplyRecord.class,seqId);
      request.setAttribute("record",record);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    }
    return "/subsys/oa/finance/cheque/news/chequeDetail.jsp";
  }
  /**
   * 添加数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String addRecord(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      YHFinanceApplyRecord record = (YHFinanceApplyRecord) YHFOM.build(request.getParameterMap());
      YHORM orm = new YHORM();//orm映射数据库
      orm.saveSingle(dbConn,record);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 添加数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String printOut(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("printStr");
      YHFinanceApplyRecordLogic recordLogic = new YHFinanceApplyRecordLogic();
      recordLogic.updateExpense(dbConn,seqIdStr);
      List<YHFinanceApplyRecord> records = recordLogic.printSeqId(dbConn,seqIdStr);
      request.setAttribute("records",records);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据失败");
      throw e;
    }
    return "/subsys/oa/finance/cheque/news/print.jsp";
  }
  
  /**
   * 根据ID查询数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String editCheque(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      YHORM orm = new YHORM();//orm映射数据库
      int seqId = Integer.parseInt(request.getParameter("seqId")); 
      YHFinanceApplyRecord record = (YHFinanceApplyRecord)orm.loadObjSingle(dbConn,YHFinanceApplyRecord.class,seqId);
      request.setAttribute("record",record);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据失败");
      throw e;
    }
    return "/subsys/oa/finance/cheque/news/editCheque.jsp";
  }
  
  /**
   * 根据ID修改数据
   * @param request
   * @param response
   * @throws Exception
   */
  public String editRecord(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {   
      //数据库的连接
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //将表单form1映射到YHTest实体类
      YHORM orm = new YHORM();//orm映射数据库
      YHFinanceApplyRecord record = (YHFinanceApplyRecord)YHFOM.build(request.getParameterMap());
      orm.updateComplex(dbConn, record);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据失败");
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
}
