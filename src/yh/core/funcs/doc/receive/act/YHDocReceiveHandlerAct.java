package yh.core.funcs.doc.receive.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.doc.data.YHDocFlowProcess;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.logic.YHFlowProcessLogic;
import yh.core.funcs.doc.logic.YHFlowRunLogic;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.doc.receive.logic.YHDocRegisterLogic;
import yh.core.funcs.doc.util.YHFlowRunUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHDocReceiveHandlerAct{
  public final static byte[] loc = new byte[1];
  /**
   * 老的创建工作
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String createWork(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
   String flowIdStr = request.getParameter("flowId");
   
   Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowProcessLogic fpl = new YHFlowProcessLogic();
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      YHFlowRunUtility fru = new YHFlowRunUtility();
      int flowId = Integer.parseInt(flowIdStr);
      String seqId = request.getParameter("seqId");
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      List<YHDocFlowProcess> list = fpl.getFlowProcessByFlowId(flowId , dbConn);
      YHDocFlowType flowType = flowTypeLogic.getFlowTypeById(flowId , dbConn);
      //取得第一步

      boolean flag = YHWorkFlowUtility.checkPriv(flowType, list, loginUser , dbConn);
      //如果第一步为空，以及检查出没有权限则提示

      if ( flag ) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程新建权限，请与OA管理员联系");
      }else{
        //查询是否为重名的
        YHFlowRunLogic frl = new YHFlowRunLogic();
        String runName = frl.getRunName(flowType, loginUser , dbConn , false) ;
        //重名
        if(frl.isExist(runName, flowId , dbConn)){ 
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "输入的工作名称/文号与之前的工作重复，请重新设置.");
        }else{
          int runId = fru.createNewWork(dbConn, flowId, loginUser, request.getParameterMap() , "", "");
          String sql = "update oa_officialdoc_recv set RUN_ID = "+ runId +",STATUS=1  where SEQ_ID=" + seqId;
          PreparedStatement ps = null;
          try{
            ps = dbConn.prepareStatement(sql);
            ps.executeUpdate();
          } catch (SQLException e){      
            throw e;
          }finally{
            YHDBUtility.close(ps, null, null);
          }
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "新建成功!");
          request.setAttribute(YHActionKeys.RET_DATA, "{runId:" + runId + ",flowId:" + flowId + "}");
        }
      }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 新的创建工作
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String createWorkNew(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
   String flowIdStr = request.getParameter("flowId");
   
   Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowProcessLogic fpl = new YHFlowProcessLogic();
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      YHFlowRunUtility fru = new YHFlowRunUtility();
      int flowId = Integer.parseInt(flowIdStr);
      String seqId = request.getParameter("seqId");
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      List<YHDocFlowProcess> list = fpl.getFlowProcessByFlowId(flowId , dbConn);
      YHDocFlowType flowType = flowTypeLogic.getFlowTypeById(flowId , dbConn);
      //取得第一步
      
      String attach = request.getParameter("attid");
      
      boolean flag = YHWorkFlowUtility.checkPriv(flowType, list, loginUser , dbConn);
      //如果第一步为空，以及检查出没有权限则提示
      synchronized(loc) {
        if ( flag ) {
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程新建权限，请与OA管理员联系");
        }else{
          YHDocRegisterLogic logic1 = new YHDocRegisterLogic();
          String att[] = logic1.getAttach(dbConn, seqId);
          String ids = att[0];
          String[] idss = ids.split(",");
          String[] names = att[1].split("\\*");
          String news = "";
          String newsId = "";
          for (int i = 0 ;i < idss.length ; i++) {
            String ss = idss[i];
            if (!YHUtility.isNullorEmpty(ss) 
                && YHWorkFlowUtility.findId(attach, ss) ) {
              news += names[i] + "*";
              newsId += ss + ",";
            }
          }
          if (news.endsWith("*")) {
            news = news.substring(0, news.length() - 1);
          }
          if (newsId.endsWith(",")) {
            newsId = newsId.substring(0, newsId.length() - 1);
          }
          int runId = fru.createNewWork(dbConn, flowId, loginUser, request.getParameterMap() , newsId , news);
          if(runId == 0){ 
            request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
            request.setAttribute(YHActionKeys.RET_MSRG, "输入的工作名称/文号与之前的工作重复，请重新设置.");
          }else{
            String sql = "update oa_officialdoc_rec_register set RUN_ID = "+ runId +" where SEQ_ID=" + seqId;
            PreparedStatement ps = null;
            try{
              ps = dbConn.prepareStatement(sql);
              ps.executeUpdate();
            } catch (SQLException e){      
              throw e;
            }finally{
              YHDBUtility.close(ps, null, null);
            }
            request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
            request.setAttribute(YHActionKeys.RET_MSRG, "新建成功!");
            request.setAttribute(YHActionKeys.RET_DATA, "{runId:" + runId + ",flowId:" + flowId + "}");
          }
        dbConn.commit();
        }
      }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
