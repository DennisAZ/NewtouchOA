package yh.plugins.workflow;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.ResultSet;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.asset.data.YHCpCptlInfo;
import yh.subsys.oa.asset.data.YHCpCptlRecord;
import yh.subsys.oa.asset.logic.YHCpCptlInfoLogic;
public class YHAsset implements YHIWFPlugin{
  /**
   * 领用单
   * 
   * 
   * */
  public String after(HttpServletRequest request, HttpServletResponse response) {
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
      YHFlowRunData rd =  wf.getFlowRunData(dbConn, flowId, runId, "固定资产名称");
      String seqId = rd.getItemData();//序列号

      YHFlowRunData rd2 =  wf.getFlowRunData(dbConn, flowId, runId, "领用数量");
      String cptlQty2 = rd2.getItemData();//实际填入的数量
      YHFlowRunData rd3 =  wf.getFlowRunData(dbConn, flowId, runId, "现有数量");
      String cptlQty = rd3.getItemData();//实际存在的数量 

      YHFlowRunData rd5 =  wf.getFlowRunData(dbConn, flowId, runId, "原因");
      String cpreMemo = rd5.getItemData();
      
      String useUser = "";//person.getUserName();//当前申请人      String sql=" select begin_user from oa_fl_run where run_id='"+runId+"'";
      Statement stmt=dbConn.createStatement();
      ResultSet rs= stmt.executeQuery(sql);
      if(rs.next()){
        useUser=rs.getString("begin_user");
      }
      
      YHORM orm = new YHORM();//orm映射数据库
      YHPerson perName = (YHPerson)orm.loadObjComplex(dbConn,YHPerson.class,Integer.parseInt(useUser));
      YHDepartment dep = (YHDepartment)orm.loadObjComplex(dbConn, YHDepartment.class, perName.getDeptId());

      boolean flage = true;
      int num =0;
      try {
        num = Integer.parseInt(cptlQty2);
      } catch (Exception ex) {
        flage = false;
      }
      if (flage) {
        if (Integer.parseInt(cptlQty) >= Integer.parseInt(cptlQty2)) {
          YHCpCptlInfo cp = new YHCpCptlInfo();
          cp.setSeqId(Integer.parseInt(seqId));
          cp.setCptlQty(Integer.parseInt(cptlQty2));
          cp.setUseUser(perName.getUserName());
          cp.setUseDept( dep.getDeptName());
          /////////////////////
          SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
          String newDate = sf.format(new Date());
          YHCpCptlInfoLogic cpLogic = new YHCpCptlInfoLogic();

          YHCpCptlRecord record = new YHCpCptlRecord();
          record.setCptlId(Integer.parseInt(seqId));
          record.setRunId(runId);
          record.setCpreUser(perName.getUserName());
          record.setCpreDate(java.sql.Date.valueOf(newDate));
          record.setCpreRecorder(perName.getUserId());
          record.setCpreFlag("1");
          record.setCpreQty(Integer.parseInt(cptlQty2));         
          record.setCpreMemo(cpreMemo);
          record.setDeptId(perName.getDeptId());

          cpLogic.asset(dbConn,cp);
          cpLogic.assetRunId(dbConn,record);
          mage = null;
        }else {
          mage = "实际申请数量大于现有数据";
        }
      }else {
        mage = "返库单数量不是数字或数量过大!";
      }
    } catch(Exception ex) {
      throw ex;
    }
    return mage;
  }

}
