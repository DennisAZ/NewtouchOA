package yh.subsys.oa.hr.score.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.hr.score.data.YHScoreItem;
import yh.subsys.oa.hr.score.logic.YHScoreItemLogic;

public class YHScoreItemAct {
  public static final String attachmentFolder = "scoreFlow";
  private YHScoreItemLogic logic = new YHScoreItemLogic();
  
  /**
   * 获取考核指标集明细列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getScoreItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("seqId");
      int seqId = 0;
      if(!YHUtility.isNullorEmpty(seqIdStr)){
        seqId = Integer.parseInt(seqIdStr);
      }
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"GROUP_ID=" + seqId};
      List funcList = new ArrayList();
      funcList.add("scoreItem");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_SCORE_ITEM"));
      
      for(Map ms : list){
        String itemName = (String) ms.get("itemName");
        if(!YHUtility.isNullorEmpty(itemName)){
          itemName = itemName.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
        }
        sb.append("{");
        sb.append("seqId:\"" + ms.get("seqId") + "\"");
        sb.append(",itemName:\"" + (ms.get("itemName") == null ? "" : itemName) + "\"");
        sb.append(",groupId:\"" + (ms.get("groupId") == null ? "" : ms.get("groupId")) + "\"");
        sb.append(",min:\"" + (ms.get("min") == null ? "" : ms.get("min")) + "\"");
        sb.append(",max:\"" + (ms.get("max") == null ? "" : ms.get("max")) + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1); 
      if (list.size() == 0) {
        sb = new StringBuffer("[");
      }
      sb.append("]");
      //System.out.println(sb);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 添加考核指标集明细记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addScoreItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
     
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String,String[]> map = request.getParameterMap();
      String seqId = request.getParameter("seqId");
      String itemName = request.getParameter("itemName");
      YHScoreItem scoreFlow = (YHScoreItem) YHFOM.build(map, YHScoreItem.class, "");
      scoreFlow.setGroupId(Integer.parseInt(seqId));
      this.logic.addScoreFlow(dbConn, scoreFlow);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 删除考核指标集明细一条记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      this.logic.deleteItem(dbConn, Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 修改考核指标集明细--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateScoreItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String,String[]> map = request.getParameterMap();
      String seqId = request.getParameter("seqId");
      String groupId = request.getParameter("groupId");
      String itemName = request.getParameter("itemName");
      String min = request.getParameter("min");
      String max = request.getParameter("max");
      YHScoreItem scoreItem = (YHScoreItem) YHFOM.build(map, YHScoreItem.class, "");
      scoreItem.setSeqId(Integer.parseInt(seqId));
      scoreItem.setGroupId(Integer.parseInt(groupId));
      scoreItem.setMin(Double.parseDouble(min));
      scoreItem.setMax(Double.parseDouble(max));
      scoreItem.setItemName(itemName);
      this.logic.updateScoreItem(dbConn, scoreItem);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
}
