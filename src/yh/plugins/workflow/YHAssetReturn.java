package yh.plugins.workflow;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
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

public class YHAssetReturn  implements YHIWFPlugin{
  /**
   * 返库单
   * 
   * 
   * */
  public String after(HttpServletRequest request, HttpServletResponse response) {
    return null;
  }

  public String before(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
      String seqId = "0";
      if (rd != null) {
         seqId = rd.getItemData();//序列号
      }
      if (!YHUtility.isInteger(seqId)) {
        seqId = "0";
      }
      YHFlowRunData rd2 =  wf.getFlowRunData(dbConn, flowId, runId, "返库数量");
      String cptlQty = rd2.getItemData();//实际填入的数
      if (!YHUtility.isInteger(cptlQty)) {
        cptlQty = "0";
      }
      YHFlowRunData rd3 =  wf.getFlowRunData(dbConn, flowId, runId, "使用部室负责人");
      String cpreKeeper = rd3.getItemData();//使用部室负责人
      YHFlowRunData rd4 =  wf.getFlowRunData(dbConn, flowId, runId, "使用部室专管员");
      String keeper = rd4.getItemData();//使用部室专管员
      String userName = "";
      if(!YHUtility.isNullorEmpty(keeper)) {
        userName = keeper;
      }
      YHFlowRunData rd5 =  wf.getFlowRunData(dbConn, flowId, runId, "备注");
      String cpreMemo = rd5.getItemData();//备注
      boolean flage = true;
      int num =0;
      try {
        num = Integer.parseInt(cptlQty);
      } catch (Exception ex) {
        flage = false;
      }
      if (num > 0 && flage && num < 100) {
        YHCpCptlInfo cp = new YHCpCptlInfo();
        cp.setSeqId(Integer.parseInt(seqId));
        cp.setCptlQty(Integer.parseInt(cptlQty));
        cp.setKeeper(userName);
        cp.setRemark(cpreMemo);

        YHCpCptlInfoLogic cpLogic = new YHCpCptlInfoLogic();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String newDate = sf.format(new Date());
        YHCpCptlRecord record = new YHCpCptlRecord();

        record.setCptlId(Integer.parseInt(seqId));
        record.setRunId(runId);
        record.setCpreUser(userName);
        record.setCpreDate(java.sql.Date.valueOf(newDate));
        record.setCpreRecorder(userName);
        record.setCpreFlag("2");
        record.setCpreQty(Integer.parseInt(cptlQty));         
        record.setCpreKeeper(cpreKeeper);
        record.setCpreMemo(cpreMemo);
        record.setDeptId(0);

        cpLogic.udpateAsset(dbConn,cp);
        cpLogic.assetRunId(dbConn,record);
        mage = null;
      }
      if (!flage) {
        mage = "返库单数量不是数字或数量过大!";
      }
      if ((num <= 0 || num >= 100) && flage) {
        mage = "实际填入的数量过大或不能为0!";
      }
    } catch (Exception e) {
      throw e;
    }
    return mage;
  }

}
