package yh.subsys.oa.vote.act;

import java.net.URLDecoder;
import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.vote.data.YHVoteItem;
import yh.subsys.oa.vote.data.YHVoteTitle;
import yh.subsys.oa.vote.logic.YHVoteItemLogic;
import yh.subsys.oa.vote.logic.YHVoteTitleLogic;

public class YHVoteItemAct {
  /**
   * 新建
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String addItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String itemName = URLDecoder.decode(request.getParameter("itemName"),"utf-8");
      String voteId = request.getParameter("voteId");
      YHVoteItemLogic itemLogic = new YHVoteItemLogic();
      if(YHUtility.isInteger(voteId)){
        YHVoteItem item = new YHVoteItem();
        item.setItemName(itemName);
        item.setVoteId(Integer.parseInt(voteId));
        itemLogic.addItem(dbConn, item);
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
   * 更新
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String updateItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String seqId = request.getParameter("seqId");
      
      String itemName = request.getParameter("itemName");
      YHVoteItemLogic itemLogic = new YHVoteItemLogic();
      if(YHUtility.isInteger(seqId)){
        itemLogic.updateItem(dbConn, seqId, itemName);
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
   * 删除itemById
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String deleteItemById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String seqId = request.getParameter("seqId");
      YHVoteItemLogic itemLogic = new YHVoteItemLogic();
      if(YHUtility.isInteger(seqId)){
        itemLogic.delItemById(dbConn, seqId);
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
   * 根据vote_id查询所有投票项目
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String selectItemByVoteId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER); 
      String voteId = request.getParameter("voteId");
      YHVoteItemLogic itemLogic = new YHVoteItemLogic();
      String data = "[";
      long totalCount  = 0;
      long maxCount = 0;
      String count = "0,0";
      if(YHUtility.isInteger(voteId)){
        String[] str = {"vote_id = " + voteId + " order by SEQ_ID "};
        List<YHVoteItem> itemList = itemLogic.selectItem(dbConn, str);
        for (int i = 0; i < itemList.size(); i++) {
          String voteUserName = "";
          YHPersonLogic personLogic = new YHPersonLogic();
          if(!YHUtility.isNullorEmpty(itemList.get(i).getVoteUser())){
            voteUserName = YHUtility.encodeSpecial(personLogic.getNameBySeqIdStr(itemList.get(i).getVoteUser(), dbConn));
          }
          data = data + YHFOM.toJson(itemList.get(i)).toString().substring(0,YHFOM.toJson(itemList.get(i)).toString().length()-1 ) +",voteUserName:\"" +voteUserName + "\"},";
         // totalCount = totalCount + itemList.get(i).getVoteCount();
        }
        if(itemList.size()>0){
          data = data.substring(0,data.length()-1);
        }
        
        count = itemLogic.getCount(dbConn, voteId);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_DATA,data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, count);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 根据seq_Id
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getItemBySeqId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String seqId = request.getParameter("seqId");
      YHVoteItemLogic itemLogic = new YHVoteItemLogic();
      String data = "";
      if(YHUtility.isInteger(seqId)){
       YHVoteItem item = itemLogic.selectItemById(dbConn, seqId);
       if(item != null){
         data = data + YHFOM.toJson(item).toString();
       }
      }
      if(data.equals("")){
        data = "{}";
      }
      request.setAttribute(YHActionKeys.RET_DATA,data);
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
   * 根据parentId查询所有 子投票
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getChiidVoteByParent(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String parentId = request.getParameter("parentId");
      YHVoteTitleLogic titleLogic = new YHVoteTitleLogic();
      String data = "[";
      if(YHUtility.isInteger(parentId)){
        String[] childStr = {"PARENT_ID = " + parentId };
        List<YHVoteTitle> childTitleList = titleLogic.selectTitle(dbConn, childStr);  
        for (int i = 0; i < childTitleList.size(); i++) {
          data = data + YHFOM.toJson(childTitleList.get(i)) + ",";
        }
        if(childTitleList.size()>0){
          data = data.substring(0,data.length()-1);
        }
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_DATA,data);
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
   * 投票项目更新数据
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String updateUserId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String seqIds = request.getParameter("seqIds");
      String anonymity = request.getParameter("anonymity");
      YHVoteItemLogic itemLogic = new YHVoteItemLogic();
      if(!YHUtility.isNullorEmpty(seqIds)){
        if(seqIds.endsWith(",")){
          seqIds = seqIds.substring(0, seqIds.length()-1);
        }
        String[] str = {"SEQ_ID in(" + seqIds + ")" };
        List<YHVoteItem> itemList = itemLogic.selectItem(dbConn, str);  
        for (int i = 0; i < itemList.size(); i++) {
          YHVoteItem item = itemList.get(i);
          String voteUser = "";
          if(!YHUtility.isNullorEmpty(item.getVoteUser())){
            if(item.getVoteUser().endsWith(",")){
              voteUser = item.getVoteUser() + user.getSeqId();
            }else{
              voteUser = item.getVoteUser() + "," + user.getSeqId();
            }
          }else{
            voteUser = user.getSeqId() + "";
          }
          itemLogic.updateItemUserId(dbConn, item.getSeqId(), anonymity, voteUser);
        }
        
      }

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
