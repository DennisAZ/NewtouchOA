package yh.subsys.oa.vote.act;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.system.selattach.util.YHSelAttachUtil;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.vote.data.YHVoteItem;
import yh.subsys.oa.vote.data.YHVoteTitle;
import yh.subsys.oa.vote.logic.YHVoteDataLogic;
import yh.subsys.oa.vote.logic.YHVoteItemLogic;
import yh.subsys.oa.vote.logic.YHVoteTitleLogic;

public class YHVoteTitleAct {
  /**
   *查询所有(分页)通用列表显示数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectTitle(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = YHVoteTitleLogic.selectTitle(dbConn, request.getParameterMap());
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
   * 删除根据选中的seqId字符串--SYL
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public String deleteVote(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userPriv = user.getUserPriv();
      if(YHUtility.isNullorEmpty(userPriv)){
        userPriv = "";
      }
      //处理seqIds
      String seqIds = request.getParameter("seqIds");
      YHVoteItemLogic itemLogic = new YHVoteItemLogic();
      YHVoteTitleLogic titleLogic = new  YHVoteTitleLogic(); 
      if(!YHUtility.isNullorEmpty(seqIds)){
        seqIds = seqIds.substring(0,seqIds.length() - 1);
        String queryStr = "SEQ_ID in(" + seqIds + ")";
        if(!userPriv.equals("1")){
          queryStr =  "SEQ_ID in(" + seqIds + ") and FROM_ID='" + user.getUserId() + "'";
        }
        String[] str = {queryStr};
        List<YHVoteTitle> titleList = titleLogic.selectTitle(dbConn, str);//根据选中的seq_id字符串得到投票记录
        //得到新seqId字符串
        String newSeqId = "";
        for (int i = 0; i <titleList.size(); i++) {
          newSeqId = newSeqId + titleList.get(i).getSeqId()+ ",";
        }
        if (titleList.size() > 0){
          newSeqId = newSeqId.substring(0, newSeqId.length()-1);
        }
        //根据ID字符串得到他们的所有子投票
        String[] childStr = {"PARENT_ID in (" + newSeqId + ")"};
        List<YHVoteTitle> childTitleList = titleLogic.selectTitle(dbConn, childStr);  
        //得到子投票的seq_id
        String childSeqId = "";
        for (int i = 0; i <childTitleList.size(); i++) {
          childSeqId = childSeqId + childTitleList.get(i).getSeqId()+ ",";
        }
        if(childTitleList.size()>0){
          childSeqId = childSeqId.substring(0, childSeqId.length()-1);
        }
        //得到所有的投票和子投票seq_id字符串
        String newSeqIds = newSeqId; 
        if(!YHUtility.isNullorEmpty(childSeqId)){
          newSeqIds = newSeqIds + "," + childSeqId;
        }
        //得到所有的投票项目
        String[] itemStr = {"vote_id in(" + newSeqIds + ")"};
        List<YHVoteItem> itemList = itemLogic.selectItem(dbConn, itemStr);
        //得到投票项目的seq_id
        String itemSeqId = "";
        for (int i = 0; i <itemList.size(); i++) {
          itemSeqId = itemSeqId + itemList.get(i).getSeqId()+ ",";
        }
        if(itemList.size()>0){
          itemSeqId = itemSeqId.substring(0, itemSeqId.length()-1);
        }
        //删除所有vote_data表中item_id等于
        YHVoteDataLogic dataLogic = new YHVoteDataLogic();
        if (!YHUtility.isNullorEmpty(itemSeqId)) {
          dataLogic.delDataByItemIds(dbConn, itemSeqId,"1");//field不等于0
        }
        //删除所有type是文本输入的vote_data表
        dataLogic.delDataByItemIds(dbConn, newSeqIds, "0");//field等于0
        //删除所有的投票项目
        itemLogic.delItemByVoteIds(dbConn, newSeqIds);
        //删除所有的投票以及子投票
        titleLogic.delTitleBySeqIds(dbConn, newSeqIds);
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
  /**
   * 删除全部--SYL
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public String deleteAllVote(HttpServletRequest request,
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
      //String seqIds = request.getParameter("seqIds");
      YHVoteItemLogic itemLogic = new YHVoteItemLogic();
      YHVoteTitleLogic titleLogic = new  YHVoteTitleLogic(); 
      YHVoteDataLogic dataLogic = new  YHVoteDataLogic(); 
      if(userPriv.equals("1")){
        dataLogic.delAllData(dbConn);
        itemLogic.delAllItem(dbConn);
        titleLogic.delAllTitle(dbConn);
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

  /**
   * 清空--SYL
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public String clearVote(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userPriv = user.getUserPriv();
      if(YHUtility.isNullorEmpty(userPriv)){
        userPriv = "";
      }
      String seqIds = request.getParameter("seqIds");
      YHVoteItemLogic itemLogic = new YHVoteItemLogic();
      YHVoteTitleLogic titleLogic = new  YHVoteTitleLogic(); 
      if(!YHUtility.isNullorEmpty(seqIds)){
        seqIds = seqIds.substring(0,seqIds.length() - 1);
        String queryStr = "SEQ_ID in(" + seqIds + ") and PARENT_ID=0";
        if(!userPriv.equals("1")){
          queryStr =  "SEQ_ID in(" + seqIds + ")  and FROM_ID='" + user.getUserId() + "'";
        }
        String[] str = {queryStr};
        List<YHVoteTitle> titleList = titleLogic.selectTitle(dbConn, str);//根据选中的seq_id字符串得到投票记录
        //得到新seqId字符串
        String newSeqId = "";
        for (int i = 0; i <titleList.size(); i++) {
          newSeqId = newSeqId + titleList.get(i).getSeqId()+ ",";
        }
        if(titleList.size()>0){
          newSeqId = newSeqId.substring(0, newSeqId.length()-1);
        }
        //根据ID字符串得到他们的所有子投票
        String[] childStr = {"PARENT_ID in (" + newSeqId + ")"};
        List<YHVoteTitle> childTitleList = titleLogic.selectTitle(dbConn, childStr);  
        //得到子投票的seq_id
        String childSeqId = "";
        for (int i = 0; i <childTitleList.size(); i++) {
          childSeqId = childSeqId + childTitleList.get(i).getSeqId()+ ",";
        }
        if(childTitleList.size() > 0){
          childSeqId = childSeqId.substring(0, childSeqId.length()-1);
        }
        //得到所有的投票和子投票seq_id字符串
        String newSeqIds = newSeqId; 
        if(!YHUtility.isNullorEmpty(childSeqId)){
          newSeqIds = newSeqIds + "," + childSeqId;
        }
        //得到所有的投票项目
        String[] itemStr = {"vote_id in(" + newSeqIds + ")"};
        List<YHVoteItem> itemList = itemLogic.selectItem(dbConn, itemStr);
        //得到投票项目的seq_id
        String itemSeqId = "";
        for (int i = 0; i <itemList.size(); i++) {
          itemSeqId = itemSeqId + itemList.get(i).getSeqId()+ ",";
        }
        if(itemList.size()>0){
          itemSeqId = itemSeqId.substring(0, itemSeqId.length()-1);
        }
        //删除所有vote_data表中item_id等于
        YHVoteDataLogic dataLogic = new YHVoteDataLogic();
        if (!YHUtility.isNullorEmpty(itemSeqId)) {
          dataLogic.delDataByItemIds(dbConn, itemSeqId,"1");//field不等于0
        }
        //删除所有type是文本输入的vote_data表        dataLogic.delDataByItemIds(dbConn, newSeqIds, "0");//field等于0
        //更新所有的投票项目
        itemLogic.updateItemByVoteIds(dbConn, newSeqIds);
        //更新的投票以及子投票
        titleLogic.updateTitleBySeqIds(dbConn, newSeqIds);
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

  /**
   * 克隆--SYL
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public String clonVote(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String userPriv = user.getUserPriv();
      if(YHUtility.isNullorEmpty(userPriv)){
        userPriv = "";
      }
      String seqIds = request.getParameter("seqIds");
      YHVoteItemLogic itemLogic = new YHVoteItemLogic();
      YHVoteTitleLogic titleLogic = new  YHVoteTitleLogic(); 
      if(!YHUtility.isNullorEmpty(seqIds)){
        seqIds = seqIds.substring(0,seqIds.length() - 1);
        //克隆父投票

        String queryStr = "SEQ_ID in(" + seqIds + ") and PARENT_ID=0";
        if(!userPriv.equals("1")){
          queryStr =  "SEQ_ID in(" + seqIds + ")  and FROM_ID='" + user.getUserId() + "'";
        }
        String[] str = {queryStr};
        List<YHVoteTitle> titleList = titleLogic.selectTitle(dbConn, str);//根据选中的seq_id字符串得到投票记录

        /*        //得到新seqId字符串

        String newSeqId = "";
        for (int i = 0; i <titleList.size(); i++) {
          newSeqId = newSeqId + titleList.get(i).getSeqId()+ ",";
        }
        if(titleList.size()>0){
          newSeqId = newSeqId.substring(0, newSeqId.length()-1);
        }*/
        for (int i = 0; i < titleList.size(); i++) {
          YHVoteTitle title = titleList.get(i);
          title.setReaders("");
          String newId = titleLogic.addVote(dbConn, title);//新建并返回SEQ_ID
          //得到所有的投票项目
          String[] itemStr = {"vote_id in(" + title.getSeqId() + ")"};
          List<YHVoteItem> itemList = itemLogic.selectItem(dbConn, itemStr);
          for (int j = 0; j < itemList.size(); j++) {
            YHVoteItem item = itemList.get(j);
            YHVoteItem newItem = new  YHVoteItem();
            newItem.setVoteId(Integer.parseInt(newId));
            newItem.setItemName(item.getItemName());
            itemLogic.addItem(dbConn, newItem);//新建投票项目

          }

          //克隆子投票

          //根据ID字符串得到他们的所有子投票
          String[] childStr = {"PARENT_ID in (" + title.getSeqId() + ")"};
          List<YHVoteTitle> childTitleList = titleLogic.selectTitle(dbConn, childStr);  
          for (int j = 0; j < childTitleList.size(); j++) {
            YHVoteTitle Childtitle = childTitleList.get(j);
            YHVoteTitle newTitle = new YHVoteTitle();
            newTitle.setFromId(Childtitle.getFromId());
            newTitle.setParentId(Integer.parseInt(newId));
            newTitle.setSubject(Childtitle.getSubject());
            newTitle.setContent(Childtitle.getContent());
            newTitle.setSendTime(Childtitle.getSendTime());
            newTitle.setEndDate(Childtitle.getEndDate());
            newTitle.setType(Childtitle.getType());
            newTitle.setMaxNum(Childtitle.getMaxNum());
            newTitle.setMinNum(Childtitle.getMinNum());
            newTitle.setTop(Childtitle.getTop());
            String childNewId = titleLogic.addVote(dbConn, newTitle);//新建并返回SEQ_ID
            //得到所有的投票项目
            String[] ChildItemStr = {"vote_id in(" + title.getSeqId() + ")"};
            List<YHVoteItem> ChildItemList = itemLogic.selectItem(dbConn, ChildItemStr);
            for (int k = 0; k < ChildItemList.size(); k++) {
              YHVoteItem item = ChildItemList.get(k);
              YHVoteItem newItem = new  YHVoteItem();
              newItem.setVoteId(Integer.parseInt(childNewId));
              newItem.setItemName(item.getItemName());
              itemLogic.addItem(dbConn, newItem);//新建投票项目
            }
          }
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


  /**
   * 更新取消置顶--SYL
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public String updateNoTopVote(HttpServletRequest request,
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
      String seqIds = request.getParameter("seqIds");
      YHVoteTitleLogic titleLogic = new  YHVoteTitleLogic(); 
      if(!YHUtility.isNullorEmpty(seqIds)){
        seqIds = seqIds.substring(0,seqIds.length() - 1);
        String queryStr = "SEQ_ID in(" + seqIds + ") and PARENT_ID=0";
        if(!userPriv.equals("1")){
          queryStr =  "SEQ_ID in(" + seqIds + ")  and FROM_ID='" + user.getUserId() + "'";
        }
        String[] str = {queryStr};
        List<YHVoteTitle> titleList = titleLogic.selectTitle(dbConn, str);//根据选中的seq_id字符串得到投票记录
        //得到新seqId字符串
        String newSeqId = "";
        for (int i = 0; i <titleList.size(); i++) {
          newSeqId = newSeqId + titleList.get(i).getSeqId()+ ",";
        }
        if(titleList.size()>0){
          newSeqId = newSeqId.substring(0, newSeqId.length()-1);
        }
        titleLogic.updateNoTopBySeqIds(dbConn, seqIds);
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

  /**
   * 查询vote_title byId---syl
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public String selectVoteById(HttpServletRequest request,
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
      if(YHUtility.isInteger(seqId)){
        YHPersonLogic personLogic = new YHPersonLogic();
        String[] str = {"SEQ_ID = " + seqId ,"PUBLISH='1'" ,"("
            + YHDBUtility.findInSet("ALL_DEPT","TO_ID")
            + " or " + YHDBUtility.findInSet("0","TO_ID")
            + " or " + YHDBUtility.findInSet(user.getDeptId()+"","TO_ID")
            + " or " + YHDBUtility.findInSet(user.getSeqId()+"", "USER_ID")
            + " or " + YHDBUtility.findInSet(user.getUserPriv(), "PRIV_ID") + ")"
            ,YHDBUtility.getDateFilter("BEGIN_DATE", YHUtility.getCurDateTimeStr(), "<=")
            ,"(" + YHDBUtility.getDateFilter("END_DATE", YHUtility.getCurDateTimeStr(), ">=")
            +" or END_DATE is null)"};

        List<YHVoteTitle> titleList = titleLogic.selectTitle(dbConn, str);//根据选中的seq_id字符串得到投票记录

        if(titleList.size()>0){
          String fromName = "";
          String deptName = "";

          if(!YHUtility.isNullorEmpty(titleList.get(0).getFromId())){
            fromName = YHUtility.encodeSpecial(personLogic.getNameBySeqIdStr(titleList.get(0).getFromId(), dbConn));
            YHPerson person = personLogic.getPerson(dbConn, titleList.get(0).getFromId());
            deptName = YHUtility.encodeSpecial(personLogic.getDeptName(dbConn, person.getDeptId()));
          }
          data = YHFOM.toJson(titleList.get(0)).toString().substring(0, YHFOM.toJson(titleList.get(0)).toString().length()-1) + ",fromName:\"" + fromName +"\",deptName:\""+ deptName + "\"}";
        }
      }
      if(data.equals("")){
        data = "{}";
      }
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
  /**
   * 查询vote_title byId---syl
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public String selectVoteById2(HttpServletRequest request,
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
      if(YHUtility.isInteger(seqId)){
        YHVoteTitle title = titleLogic.selectVoteById(dbConn, Integer.parseInt(seqId));
        if(title !=null){
          data = YHFOM.toJson(title).toString();
        }
      }
      if(data.equals("")){
        data = "{}";
      }
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
  
  /**
	 * 桌面显示投票
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getDeskVotes(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		Statement stmt = null;
	    ResultSet rs = null;
		StringBuffer sb = new StringBuffer("[");
		YHPerson person = (YHPerson) request.getSession().getAttribute(
				YHConst.LOGIN_USER);
		
		String sql = "select vt.SEQ_ID,vt.FROM_ID,de.DEPT_NAME,p.USER_NAME"
		      + ",vt.SUBJECT_MAIN,vt.ANONYMITY,vt.BEGIN_DATE,vt.END_DATE"
		      +",vt.TYPE,vt.VIEW_PRIV,vt.PUBLISH,vt.READERS,vt.TOP"
		      + " FROM oa_vote_title vt"
		      + " left outer join person p on vt.FROM_ID = p.SEQ_ID"
		      + " left outer join oa_department de on de.seq_id = p.DEPT_ID"
		      + " where PARENT_ID=0 and PUBLISH='1' " 
		      + " and ("
		      + YHDBUtility.findInSet("ALL_DEPT","vt.TO_ID")
		      + " or " + YHDBUtility.findInSet("0","vt.TO_ID")
		      + " or " + YHDBUtility.findInSet(person.getDeptId()+"","vt.TO_ID")
		      + " or " + YHDBUtility.findInSet(person.getSeqId()+"", "vt.USER_ID")
		      + " or " + YHDBUtility.findInSet(person.getUserPriv(), "vt.PRIV_ID") + ")"

		      + " and " + YHDBUtility.getDateFilter("vt.BEGIN_DATE", YHUtility.getCurDateTimeStr(), "<=")
		      + " and " + "(" +YHDBUtility.getDateFilter("vt.END_DATE", YHUtility.getCurDateTimeStr(), ">")
		      +" or vt.END_DATE is null)  order by vt.TOP desc,vt.BEGIN_DATE desc,vt.SEND_TIME desc limit 10";
		try{	
		YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			stmt = dbConn.createStatement();
		    rs = stmt.executeQuery(sql);
		    int count = 0;
		    while(rs.next())
		    {
		    	  count++;
		    	  sb.append("{");
	              sb.append("seqId:" + rs.getInt("SEQ_ID"));
	              sb.append(",subject:\"" + YHUtility.encodeSpecial(rs.getString("SUBJECT_MAIN")) + "\"");
	              sb.append(",starTime:\"" + rs.getDate("BEGIN_DATE") + "\"");
	              sb.append("},");
		    }
		    if(count>0) {
	            sb.deleteCharAt(sb.length() - 1); 
	            }
	          sb.append("]");
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	          request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
	          request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
	    } catch (Exception ex) {
	      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
	      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	      request.setAttribute(YHActionKeys.RET_MSRG, message);
	      throw ex;
	    }
	    return "/core/inc/rtjson.jsp";
	}
	
  /**
   *查询所有(分页)通用列表显示数据--syl  个人事务
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectVoteToCurrent(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = YHVoteTitleLogic.selectVoteToCurrent(dbConn, request.getParameterMap(),person.getSeqId(),person.getDeptId(),person.getUserPriv());
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
   *查询所有(分页)通用列表显示数据--syl  个人事务
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectVoteToHistory(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = YHVoteTitleLogic.selectVoteToHistory(dbConn, request.getParameterMap(),person.getSeqId(),person.getDeptId(),person.getUserPriv());
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
   * 新建项目
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addVote(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVoteTitle title = (YHVoteTitle)YHFOM.build(request.getParameterMap());
      //文件柜中上传附件
      String attachmentName = request.getParameter("attachmentName");
      String attachmentId = request.getParameter("attachmentId");
      YHSelAttachUtil sel = new YHSelAttachUtil(request,"profsys");
      String attachNewId = sel.getAttachIdToString(",");
      String attachNewName = sel.getAttachNameToString("*");
      if(!"".equals(attachNewId) && !"".equals(attachmentId) &&  !attachmentId.trim().endsWith(",")){
        attachmentId += ",";
      }
      attachmentId += attachNewId;
      if(!"".equals(attachNewName) && !"".equals(attachmentName)  && !attachmentName.trim().endsWith("*")){
        attachmentName += "*";
      }
      attachmentName += attachNewName;

      title.setAttachmentId(attachmentId);
      title.setAttachmentName(attachmentName);
      if (title.getBeginDate() == null) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        title.setBeginDate(YHUtility.parseSqlDate(sf.format(new Date())));
      }
      String date = YHVoteTitleLogic.addVote(dbConn,title);
      date = "{seqId:" + date + "}";
      request.setAttribute(YHActionKeys.RET_DATA,date);
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
   * 单文件附件上传
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String uploadFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Map<String, String> attr = null;
    String attrId = (fileForm.getParameter("attachmentId")== null )? "":fileForm.getParameter("attachmentId");
    String attrName = (fileForm.getParameter("attachmentName")== null )? "":fileForm.getParameter("attachmentName");
    String data = "";
    try{
      YHVoteTitleLogic titleLogic = new YHVoteTitleLogic();
      attr = titleLogic.fileUploadLogic(fileForm);
      Set<String> keys = attr.keySet();
      for (String key : keys){
        String value = attr.get(key);
        if(attrId != null && !"".equals(attrId)){
          if(!(attrId.trim()).endsWith(",")){
            attrId += ",";
          }
          if(!(attrName.trim()).endsWith("*")){
            attrName += "*";
          }
        }
        attrId += key + ",";
        attrName += value + "*";
      }
      data = "{attrId:\"" + attrId + "\",attrName:\"" + attrName + "\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "文件上传失败");
      throw e;
    }
    return "/core/inc/rtuploadfile.jsp";
  }


  /**
   *查询所有(分页)通用列表显示数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectVote(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String data = YHVoteTitleLogic.selectVote(dbConn, request.getParameterMap(),person.getSeqId(),person.getUserPriv());
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
   *seqId串转换成privName,userName,deptName串
   */
  public String strString(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String tableName = request.getParameter("tableName");
      String tdName = request.getParameter("tdName");
      String data = YHVoteTitleLogic.strString(dbConn,seqId,tableName,tdName);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 修改项目
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateVote(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHVoteTitle title = (YHVoteTitle)YHFOM.build(request.getParameterMap());

      //文件柜中上传附件
      String attachmentName = request.getParameter("attachmentName");
      String attachmentId = request.getParameter("attachmentId");
      YHSelAttachUtil sel = new YHSelAttachUtil(request,"profsys");
      String attachNewId = sel.getAttachIdToString(",");
      String attachNewName = sel.getAttachNameToString("*");
      if(!"".equals(attachNewId) && !"".equals(attachmentId) &&  !attachmentId.trim().endsWith(",")){
        attachmentId += ",";
      }
      attachmentId += attachNewId;
      if(!"".equals(attachNewName) && !"".equals(attachmentName)  && !attachmentName.trim().endsWith("*")){
        attachmentName += "*";
      }
      attachmentName += attachNewName;
      title.setAttachmentId(attachmentId);
      title.setAttachmentName(attachmentName);
      if (title.getBeginDate() == null) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        title.setBeginDate(YHUtility.parseSqlDate(sf.format(new Date())));
      }
      YHVoteTitleLogic.updateVote(dbConn, title);

      String strSeqId = null;
      String smsSJ = request.getParameter("smsSJ");//手机短信
      String smsflag = request.getParameter("smsflag");//内部短信
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      if (title.getPublish().equals("1") && title.getTop().equals("1")) {
        if (title.getToId().equals("0") || title.getToId().equals("ALL_DEPT")) {
          strSeqId = YHVoteTitleLogic.strSeqId(dbConn,"",title.getToId(),"");//部门为ALL_DEPT，0
        }
        if (!title.getToId().equals("0") && !title.getToId().equals("ALL_DEPT")) {
          strSeqId = YHVoteTitleLogic.strSeqId(dbConn,title.getUserId(),title.getToId(),title.getPrivId());
        }
        if (smsflag.equals("1")) {
          YHSmsBack sb = new YHSmsBack();
          sb.setSmsType("11");
          sb.setContent("请查看投票！\n 标题：" + title.getSubject());
          sb.setSendDate(new java.util.Date());
          sb.setFromId(person.getSeqId());
          sb.setToId(strSeqId);
          sb.setRemindUrl("/subsys/oa/vote/show/readVote.jsp?seqId=" + title.getSeqId() + "&openFlag=1&openWidth=780&openHeight=500");
          YHSmsUtil.smsBack(dbConn,sb);
        }
        //手机消息提醒
        if (smsSJ.equals("1")) {
          YHMobileSms2Logic sb2 = new YHMobileSms2Logic();
          sb2.remindByMobileSms(dbConn,strSeqId,person.getSeqId(),"请查看投票！\n 标题：" + title.getSubject(),new java.util.Date());
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * 详细信息
   * */
  public String showDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHVoteTitle title = null;
      if (!YHUtility.isNullorEmpty(seqId)) {
        title = YHVoteTitleLogic.showDetail(dbConn, Integer.parseInt(seqId));
      }
      //定义数组将数据保存到Json中
      String data = "";
      if(title != null) {
        data = data + YHFOM.toJson(title);
        data = data.replaceAll("\\n", "");
        data = data.replaceAll("\\r", "");
      }
      data = data + "";
      if(data.equals("")){
        data = "{}";
      }
      //保存查询数据是否成功，保存date
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * 查询投票项seqId
   * */
  public String selectId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      if (!YHUtility.isNullorEmpty(seqId)) {
        String[] str = {"VOTE_ID='" + seqId + "'"};
        //定义数组将数据保存到Json中
        List<YHVoteItem> list = YHVoteItemLogic.selectItem(dbConn,str);
        //遍历返回的list，将数据保存到Json中
        String data = "[";
        YHVoteItem item = new YHVoteItem();; 
        for (int i = 0; i < list.size(); i++) {
          item = list.get(i);
          data = data + YHFOM.toJson(item).toString()+",";
        }
        if(list.size()>0){
          data = data.substring(0, data.length()-1);
        }
        data = data.replaceAll("\\n", "");
        data = data.replaceAll("\\r", "");
        data = data + "]";
        //保存查询数据是否成功，保存date
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * 查询子投票项seqId
   * */
  public String selectId2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      if (!YHUtility.isNullorEmpty(seqId)) {
        String[] str = {"PARENT_ID='" + seqId + "'"};
        //定义数组将数据保存到Json中
        List<YHVoteTitle> list = YHVoteTitleLogic.selectTitle(dbConn, str);
        //遍历返回的list，将数据保存到Json中
        String data = "[";
        YHVoteTitle title = new YHVoteTitle();; 
        for (int i = 0; i < list.size(); i++) {
          title = list.get(i);
          data = data + YHFOM.toJson(title).toString()+",";
        }
        if(list.size()>0){
          data = data.substring(0, data.length()-1);
        }
        data = data.replaceAll("\\n", "");
        data = data.replaceAll("\\r", "");
        data = data + "]";
        //保存查询数据是否成功，保存date
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *查询所有(分页)通用列表显示数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String showVote(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String parentId = request.getParameter("seqId");
      String data = YHVoteTitleLogic.showVote(dbConn,request.getParameterMap(),parentId);
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
   *立即生效,立即终止,恢复终止
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateBeginDate(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String tdName = request.getParameter("tdName");
      String dayTime = request.getParameter("dayTime");
      if (!YHUtility.isNullorEmpty(seqId)) {
        if (!YHUtility.isNullorEmpty(dayTime)) {
          YHVoteTitleLogic.updateBeginDate(dbConn,Integer.parseInt(seqId),tdName,YHUtility.parseSqlDate(dayTime));
        }
        if (YHUtility.isNullorEmpty(dayTime)) {
          YHVoteTitleLogic.updateBeginDate(dbConn,Integer.parseInt(seqId),tdName,null);
        }
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }

  /**
   *立即发布
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updatePublish(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String publish = request.getParameter("publish");
      if (!YHUtility.isNullorEmpty(seqId)) {
        YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
        YHVoteTitleLogic.updatePublish(dbConn,Integer.parseInt(seqId),publish ,loginUser.getSeqId() );
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   *更新投票人

   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateReaders(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String seqId = request.getParameter("seqId");
      String data = "";
      String isReader = "0";
      if (YHUtility.isInteger(seqId)) {
        YHVoteTitle title = YHVoteTitleLogic.showDetail(dbConn, Integer.parseInt(seqId));
        if (!YHUtility.isNullorEmpty(title.getReaders())) {
          String[] readersArray = title.getReaders().split(",");
          for (int i = 0; i < readersArray.length; i++) {
            if (readersArray[i].equals(person.getSeqId())) {
              isReader = "1";
              break;
            }
          }
        }
        if (isReader.equals("0")) {
          String readers = "";
          if(!YHUtility.isNullorEmpty(title.getReaders())){
            if(title.getReaders().endsWith(",")){
              readers = title.getReaders() + person.getSeqId();
            }else{
              readers = title.getReaders() + "," + person.getSeqId();
            }
          }else{
            readers = person.getSeqId() + "";
          }
          YHVoteTitleLogic.updateReaders(dbConn, Integer.parseInt(seqId), readers);
        }
      }
      data = "{isReader:" + isReader + "}";
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *导出投票数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String outVote (HttpServletRequest request,HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      OutputStream ops = null;
      String fileName = URLEncoder.encode("投票信息.xls","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();

      YHVoteTitleLogic titleLogic = new YHVoteTitleLogic();
      String seqId = request.getParameter("seqId");
      YHVoteTitle voteItem = null;
      List<YHVoteTitle> listItem = new ArrayList<YHVoteTitle>();
      if(!YHUtility.isNullorEmpty(seqId)){
        //父级投票项的投票项---- 一级标题只有一个
        YHVoteTitle title = YHVoteTitleLogic.showDetail(dbConn,Integer.parseInt(seqId));//根据选中的seq_id字符串得到投票记录
        String str[] = {"VOTE_ID='" + seqId +"'"};
        List<YHVoteItem> item = YHVoteItemLogic.selectItem(dbConn,str);
        YHVoteItem itemGet = new YHVoteItem();     
        if (item.size() > 0 && title != null) {
          for (int i = 0; i < item.size(); i ++) {
            itemGet = item.get(i);
            //循环添加数据
            voteItem = new YHVoteTitle();
            if (i > 0) {
              voteItem.setSubject("");
            } else {
              voteItem.setSubject(title.getSubject());
            }
            voteItem.setFromId(itemGet.getVoteUser());
            voteItem.setParentId(itemGet.getVoteCount());
            voteItem.setContent((i + 1) + "、" + itemGet.getItemName());
            listItem.add(voteItem);
          }
        }
        if (item.size() <= 0 && title != null) {
          //循环添加数据
          voteItem = new YHVoteTitle();
          voteItem.setSubject(title.getSubject());
          voteItem.setFromId("");
          voteItem.setParentId(0);
          voteItem.setContent("");
          listItem.add(voteItem);
        }
        //子投票项的投票项----二级标题可有多个
        String str2[] = {"PARENT_ID='" + seqId +"'"};
        List<YHVoteTitle> titleList = titleLogic.selectTitle(dbConn,str2);//根据选中的seq_id字符串得到投票记录
        YHVoteTitle voteTile = new YHVoteTitle();
        YHVoteItem itemGet2 = new YHVoteItem();
        if (titleList.size() > 0) {
          for (int i = 0; i < titleList.size(); i ++) {
            voteTile = titleList.get(i);
            String strItem[] = {"VOTE_ID='" + voteTile.getSeqId() +"'"};
            List<YHVoteItem> itemList = YHVoteItemLogic.selectItem(dbConn,strItem);
            if (itemList.size() > 0) {
              for (int j = 0; j < itemList.size(); j ++) {
                itemGet2 = itemList.get(j);
                //循环添加数据
                voteItem = new YHVoteTitle();
                if (j > 0) {
                  voteItem.setSubject("");
                } else {
                  voteItem.setSubject(voteTile.getSubject());
                }
                voteItem.setFromId(itemGet2.getVoteUser());
                voteItem.setParentId(itemGet2.getVoteCount());
                voteItem.setContent((j + 1) + "、"+ itemGet2.getItemName());
                listItem.add(voteItem);
              }
            } else {
              //循环添加数据
              voteItem = new YHVoteTitle();
              voteItem.setSubject(voteTile.getSubject());
              voteItem.setFromId("");
              voteItem.setParentId(0);
              voteItem.setContent("");
              listItem.add(voteItem);
            }
          }
        }
        ArrayList<YHDbRecord> dbL = YHVoteTitleLogic.getDbRecord(dbConn,listItem);
        YHJExcelUtil.writeExc(ops, dbL);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "导出数据成功！");
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "导出数据失败");
      throw e;
    }
    return null;
  }
}
