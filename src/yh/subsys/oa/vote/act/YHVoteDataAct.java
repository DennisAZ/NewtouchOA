package yh.subsys.oa.vote.act;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.vote.data.YHVoteData;
import yh.subsys.oa.vote.data.YHVoteTitle;
import yh.subsys.oa.vote.logic.YHVoteDataLogic;
import yh.subsys.oa.vote.logic.YHVoteItemLogic;
import yh.subsys.oa.vote.logic.YHVoteTitleLogic;

public class YHVoteDataAct {
  /**
   * 新建
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String addData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String itemId = request.getParameter("itemId");
      String fieldName = request.getParameter("fieldName");
      String fieldData = request.getParameter("fieldData");
      YHVoteDataLogic dataLogic = new YHVoteDataLogic();
      if(YHUtility.isInteger(itemId)){
        YHVoteData data = new YHVoteData();
        data.setFieldName(fieldName);
        data.setItemId(Integer.parseInt(itemId));
        data.setFieldData(fieldData);
        dataLogic.addData(dbConn, data);
      }
     // request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询ById
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getDataByItemId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String itemId = request.getParameter("itemId");
      String fieldName = request.getParameter("fieldName");
      if(YHUtility.isNullorEmpty(fieldName)){
        fieldName = "0";
      }
      YHVoteDataLogic dataLogic = new YHVoteDataLogic();
      String dataStr = "[";
      if(YHUtility.isInteger(itemId)){
        List<YHVoteData> dataList = dataLogic.selectDataByItemId(dbConn, itemId,fieldName);
        for (int i = 0; i < dataList.size(); i++) {
          dataStr = dataStr + YHFOM.toJson(dataList.get(i)).toString() + ",";
        }
        if(!dataStr.equals("[")){
          dataStr = dataStr.substring(0, dataStr.length()-1);
        }
      }
      dataStr = dataStr + "]";
      request.setAttribute(YHActionKeys.RET_DATA,dataStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询投票人数情况
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public String selectReadersInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String userPriv = user.getUserPriv();
      if(YHUtility.isNullorEmpty(userPriv)){
        userPriv = "";
      }
      String seqId = request.getParameter("seqId");
      YHVoteItemLogic itemLogic = new YHVoteItemLogic();
      YHVoteTitleLogic titleLogic = new  YHVoteTitleLogic(); 
      YHVoteDataLogic dataLogic = new  YHVoteDataLogic(); 
      String data = "";
      String personCount = "0";//所有投票的人数
      String readerCount = "0";//已经投票的人数
      if(YHUtility.isInteger(seqId)){
        YHVoteTitle title = titleLogic.selectVoteById(dbConn, Integer.parseInt(seqId));
        if(title !=null){
          String deptId = YHUtility.isNullorEmpty(title.getToId()) ? "" : title.getToId();
          String privId = YHUtility.isNullorEmpty(title.getPrivId()) ? "" : title.getPrivId(); 
          String userId = YHUtility.isNullorEmpty(title.getUserId()) ? "" : title.getUserId(); 
          
          personCount = titleLogic.getPersonCount(dbConn, userId, deptId, privId);
          if(!YHUtility.isNullorEmpty(title.getReaders())){
            String[] readersArray = title.getReaders().split(",");
            readerCount = readersArray.length + "";
          }
        }
      }
      data = "{personCount:\"" +personCount + "\",readerCount:\"" + readerCount +"\"}";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, user.getSeqId()+"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
