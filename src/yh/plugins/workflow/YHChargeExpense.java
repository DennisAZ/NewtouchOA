package yh.plugins.workflow;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;
import yh.subsys.oa.asset.data.YHCpCptlInfo;
import yh.subsys.oa.asset.data.YHCpCptlRecord;
import yh.subsys.oa.asset.logic.YHCpCptlInfoLogic;

public class YHChargeExpense implements YHIWFPlugin{

  public String after(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    // TODO Auto-generated method stub
    return null;
  }

  public String before(HttpServletRequest request, HttpServletResponse response) throws Exception {
  String mage = "";
  try {
    YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request
    .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    Connection dbConn = requestDbConn.getSysDbConn();
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    String prcsIdStr = request.getParameter("prcsId");
    String flowPrcsStr = request.getParameter("flowPrcs");
    
    int runId = Integer.parseInt(runIdStr);
    int prcsId = Integer.parseInt(prcsIdStr);
    int flowId = Integer.parseInt(flowIdStr);
    int flowPrcs = Integer.parseInt(flowPrcsStr);

    YHFlowRunUtility wf = new YHFlowRunUtility();
    YHFlowRunData rd =  wf.getFlowRunData(dbConn, flowId, runId, "领用人姓名");
    String seqId = rd.getItemData();//序列号

    
    YHORM orm = new YHORM();//orm映射数据库
    YHPerson perName = (YHPerson)orm.loadObjComplex(dbConn,YHPerson.class,person.getSeqId());
    YHDepartment dep = (YHDepartment)orm.loadObjComplex(dbConn, YHDepartment.class,perName.getDeptId());
    String useDept = dep.getDeptName();
   
  } catch(Exception ex) {
    throw ex;
  }
  return mage;
  }

}
