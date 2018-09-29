package yh.subsys.oa.giftProduct.instock.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.codeclass.data.YHCodeItem;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.giftProduct.instock.data.YHGiftInstock;
import yh.subsys.oa.giftProduct.instock.logic.YHGiftInstockLogic;
import yh.subsys.oa.giftProduct.outstock.logic.YHGiftOutstockLogic;

public class YHGiftInstockAct {
  /*
   * 查询所有礼品类型，树状
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getGiftTypeTree(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String GIFT_PROTYPE = request.getParameter("GIFT_PROTYPE");
      String id = request.getParameter("id");
      //根据seqId（codeClass） 得到所有的codeItem
      
      YHGiftInstockLogic giftLogic = new YHGiftInstockLogic();
      StringBuffer sb = new StringBuffer("");
      if(GIFT_PROTYPE!=null&&!GIFT_PROTYPE.equals("")){
        //YHCodeClass codeClass = giftLogic.getCodeClass(dbConn, Integer.parseInt(seqId));
        //if(codeClass!=null&&codeClass.getClassNo()!=null&&!codeClass.getClassNo().trim().equals("")){
          //String[] str = {"CLASS_NO = '" +  codeClass.getClassNo() + "' order by SORT_NO"};
          List<YHCodeItem> itemList = giftLogic.getCodeItem(dbConn,GIFT_PROTYPE);
          sb.append("[");
          if(id!=null&&!id.equals("")&&!id.equals("0")){
     /*       for (int i = 0; i < itemList.size(); i++) {
              YHCodeItem codeItem = itemList.get(i);
              //判断礼品类型有没有入库礼品
              List<YHGiftInstock> giftList = new ArrayList<YHGiftInstock>();
              String[] giftType = {"GIFT_TYPE = '" + codeItem.getSqlId()+"'"};
              giftList = giftLogic.selectGiftInstock(dbConn, giftType);
              String extData = "";
              if(giftList.size()>0){
                for (int j = 0; j < giftList.size(); j++) {
                  YHGiftInstock gift = new YHGiftInstock();
                  gift = giftList.get(j);;
                  String imgAddress = "/yh/core/styles/style1/img/4[1].gif";
                  sb.append("{");
                  sb.append("nodeId:\"" + gift.getSeqId()+",gift" + "\"");
                  sb.append(",name:\"" + gift.getGiftName() + "\"");
                  sb.append(",isHaveChild:" + 0+ "");
                  sb.append(",imgAddress:\"" + imgAddress + "\"");
                  sb.append(",extData:\"" + extData + "\"");
                  sb.append("},");
                }
             
              }
       
            }*/
            List<YHGiftInstock> giftList = new ArrayList<YHGiftInstock>();
            String[] giftType = {"GIFT_TYPE = '" + id+"'"};
            giftList = giftLogic.selectGiftInstock(dbConn, giftType);
            String extData = "";
            if(giftList.size()>0){
              for (int j = 0; j < giftList.size(); j++) {
                YHGiftInstock gift = new YHGiftInstock();
                gift = giftList.get(j);
                String giftName = gift.getGiftName();
                if(giftName!=null){
                  giftName = giftName.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
                  
                }                String imgAddress = "/yh/core/styles/style1/img/4[1].gif";
                sb.append("{");
                sb.append("nodeId:\"" + gift.getSeqId()+",gift" + "\"");
                sb.append(",name:\"" +giftName+ "\"");
                sb.append(",isHaveChild:" + 0+ "");
                sb.append(",imgAddress:\"" + imgAddress + "\"");
                sb.append(",extData:\"" + extData + "\"");
                sb.append("},");
              }
           
            }
            sb.deleteCharAt(sb.length() - 1); 
            sb.append("]");
          }else{
            for (int i = 0; i < itemList.size(); i++) {
              YHCodeItem codeItem = itemList.get(i);
              //判断礼品类型有没有入库礼品
              List<YHGiftInstock> giftList = new ArrayList<YHGiftInstock>();
              String[] giftType = {"GIFT_TYPE = '" + codeItem.getSqlId()+"'"};
              giftList = giftLogic.selectGiftInstock(dbConn, giftType);
              String extData = "";
              String classDesc =  codeItem.getClassDesc();
              if(classDesc!=null){
                classDesc = classDesc.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
              }
              if(giftList.size()>0){
                 sb.append("{");
                sb.append("nodeId:\"" + codeItem.getSqlId() + "\"");
                sb.append(",name:\"" + classDesc + "\"");
                sb.append(",isHaveChild:" + 1+ "");
          
                sb.append(",extData:\"" + extData + "\"");
                sb.append("},");
              }else{

                sb.append("{");
                sb.append("nodeId:\"" + codeItem.getSqlId() + "\"");
                sb.append(",name:\"" + classDesc + "\"");
                sb.append(",isHaveChild:" + 0+ "");
                sb.append(",extData:\"" + extData + "\"");
                sb.append("},");
              }
            
            }
            if(itemList.size()>0){
              sb.deleteCharAt(sb.length() - 1); 
            }
            sb.append("]");
          }
        }
      String data = sb.toString();
      if(data.equals("")){
       data = "[]";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到礼品的所有类型
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCodeItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      //String seqId = request.getParameter("seqId");
      String GIFT_PROTYPE = request.getParameter("GIFT_PROTYPE");
      //根据seqId（codeClass） 得到所有的codeItem
      
      YHGiftInstockLogic giftLogic = new YHGiftInstockLogic();
      String data = "[";
      List<YHCodeItem> itemList = new ArrayList<YHCodeItem>();
      if(GIFT_PROTYPE!=null&&!GIFT_PROTYPE.equals("")){
        //YHCodeClass codeClass = giftLogic.getCodeClass(dbConn, Integer.parseInt(seqId));
       // if(codeClass!=null&&codeClass.getClassNo()!=null&&!codeClass.getClassNo().trim().equals("")){
          //String[] str = {"CLASS_NO = '" +  codeClass.getClassNo() + "' order by SORT_NO"};
          itemList = giftLogic.getCodeItem(dbConn,"GIFT_PROTYPE");
          for (int i = 0; i < itemList.size(); i++) {
            YHCodeItem item = itemList.get(i);
            data = data + YHFOM.toJson(item) + ",";
          }

      }
 
      if(itemList.size()>0){
       data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到礼品的类ById
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCodeItemById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      //根据seqId（codeClass） 得到所有的codeItem
      
      YHGiftInstockLogic giftLogic = new YHGiftInstockLogic();
      String data = "";
      List<YHCodeItem> itemList = new ArrayList<YHCodeItem>();
      if(seqId!=null&&!seqId.equals("")){
        YHCodeItem codeItem = giftLogic.getCodeItemById(dbConn, seqId);
        data = YHFOM.toJson(codeItem).toString();
      }
      if(data.equals("")){
        data = "{}";
      }
  
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 更新礼品的类ById
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateCodeItemById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String seqId = request.getParameter("seqId");
      String classCode = request.getParameter("codeNo");
      String sortNo = request.getParameter("codeOrder");
      String classDesc = request.getParameter("codeName");
      YHGiftInstockLogic giftLogic = new YHGiftInstockLogic();
      if(seqId!=null&&!seqId.equals("")){
        giftLogic.updateCodeItemById(dbConn, seqId, classCode, sortNo, classDesc);
      }
  
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
     // request.setAttribute(YHActionKeys.RET_DATA, dat);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 添加礼品类型
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addCodeItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String classCode = request.getParameter("codeNo");
      String sortNo = request.getParameter("codeOrder");
      String classDesc = request.getParameter("codeName");
      String GIFT_PROTYPE = request.getParameter("GIFT_PROTYPE");
      YHGiftInstockLogic giftLogic = new YHGiftInstockLogic();
      boolean bl = giftLogic.checkCodeClass(dbConn, classDesc);
      String data = "";
      if(bl){
        if(GIFT_PROTYPE!=null&&!GIFT_PROTYPE.equals("")){
          //YHCodeClass codeClass = giftLogic.getCodeClass(dbConn, Integer.parseInt(codeClassSeqId));
          int seqId = giftLogic.addCodeItem(dbConn,classCode,GIFT_PROTYPE,sortNo,classDesc);
          data = "{addType:1}";
        }
     
      }else{
        data = "{addType:2}";//重复
      }
      
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /*
   * 新建入库礼品
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addGiftInstock(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      Date curDate = new Date();
      String giftPrice = request.getParameter("giftPrice");
      String giftQty = request.getParameter("giftQty");
      
      YHGiftInstock giftInstock = (YHGiftInstock) YHFOM.build(request.getParameterMap());
      giftInstock.setCreateDate(curDate);
      if(!YHUtility.isNumber(giftPrice)){
        giftInstock.setGiftPrice(0);
      }
      if(!YHUtility.isNumber(giftQty)){
        giftInstock.setGiftQty(0);
      }
      YHGiftInstockLogic  giftLogic = new YHGiftInstockLogic();
    
      int seqId = giftLogic.addGiftInstock(dbConn, giftInstock);
      String giftName = "";
      if(giftInstock.getGiftName()!= null){
        giftName = giftInstock.getGiftName().replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "").replace("\r","");
      }
      String data = "{seqId:\"" + seqId + "\",giftName:\""+ giftName+"\"}";
    /*    //短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("5");
        sb.setContent("请查看日程安排！内容："+content);
        sb.setRemindUrl("/yh/core/funcs/calendar/mynote.jsp?seqId="+maxSeqId+"&openFlag=1&openWidth=300&openHeight=250");
        sb.setToId(String.valueOf(userId));
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);*/
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /*
   * 新建入库礼品
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addGiftInstockWork(HttpServletRequest request,
      HttpServletResponse response,YHGiftInstock giftInstock) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = new Date();
      SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");
      String curDateStr = dateFormat2.format(date);
      giftInstock.setGiftCode(curDateStr);
      YHGiftInstockLogic  giftLogic = new YHGiftInstockLogic();
    
      int seqId = giftLogic.addGiftInstock(dbConn, giftInstock);
      String data = "{seqId:\"" + seqId + "\",giftName:\""+ giftInstock.getGiftName()+"\"}";
    /*    //短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("5");
        sb.setContent("请查看日程安排！内容："+content);
        sb.setRemindUrl("/yh/core/funcs/calendar/mynote.jsp?seqId="+maxSeqId+"&openFlag=1&openWidth=300&openHeight=250");
        sb.setToId(String.valueOf(userId));
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);*/
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /*
   * 更新
   *入库礼品
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String upateGiftInstock(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      Date curDate = new Date();
      String giftType = request.getParameter("giftType");
      YHGiftInstock giftInstock = (YHGiftInstock) YHFOM.build(request.getParameterMap());
      giftInstock.setCreateDate(curDate);
      String seqIdStr = request.getParameter("seqId");
      YHGiftInstockLogic  giftLogic = new YHGiftInstockLogic();
      if(seqIdStr!=null&&!seqIdStr.equals("")){
        giftInstock.setSeqId(Integer.parseInt(seqIdStr));
        giftLogic.updateGiftInstock(dbConn, giftInstock);
      }
     

    /*    //短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("5");
        sb.setContent("请查看日程安排！内容："+content);
        sb.setRemindUrl("/yh/core/funcs/calendar/mynote.jsp?seqId="+maxSeqId+"&openFlag=1&openWidth=300&openHeight=250");
        sb.setToId(String.valueOf(userId));
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);*/
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /*
   * 删除入库礼品byId
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delGiftInstockById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      Date curDate = new Date();
      String seqIdStr = request.getParameter("seqId");
      YHGiftInstockLogic  giftLogic = new YHGiftInstockLogic();
      YHGiftOutstockLogic  outLogic = new YHGiftOutstockLogic();
      if(seqIdStr!=null&&!seqIdStr.equals("")){
        giftLogic.delGiftInstockById(dbConn, Integer.parseInt(seqIdStr));
        outLogic.delOutstock(dbConn, seqIdStr);
      }
     

    /*    //短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("5");
        sb.setContent("请查看日程安排！内容："+content);
        sb.setRemindUrl("/yh/core/funcs/calendar/mynote.jsp?seqId="+maxSeqId+"&openFlag=1&openWidth=300&openHeight=250");
        sb.setToId(String.valueOf(userId));
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);*/
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /*
   * 删除入库礼品byId
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delGiftInstock(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      Date curDate = new Date();
      String seqIds = request.getParameter("seqIds");
      YHGiftInstockLogic  giftLogic = new YHGiftInstockLogic();
      YHGiftOutstockLogic  outLogic = new YHGiftOutstockLogic();
      if(seqIds!=null&&!seqIds.equals("")){
        giftLogic.delGiftInstock(dbConn,seqIds);
        outLogic.delOutstock(dbConn, seqIds);
      }
     

    /*    //短信smsType, content, remindUrl, toId, fromId
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType("5");
        sb.setContent("请查看日程安排！内容："+content);
        sb.setRemindUrl("/yh/core/funcs/calendar/mynote.jsp?seqId="+maxSeqId+"&openFlag=1&openWidth=300&openHeight=250");
        sb.setToId(String.valueOf(userId));
        sb.setFromId(userId);
        YHSmsUtil.smsBack(dbConn, sb);*/
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, "data");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询单个礼品
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getInstockById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHGiftInstockLogic  giftLogic = new YHGiftInstockLogic();
      String seqId = request.getParameter("seqId");
      YHPersonLogic perLogic = new YHPersonLogic();
      String data = "";
      if(seqId!=null&&!seqId.equals("")){
        YHGiftInstock instock  = giftLogic.selectGiftInstockById(dbConn, Integer.parseInt(seqId));
        if(instock!=null){
          String creatorName = "";
          String giftKeeperName = "";
          if(instock.getGiftCreator()!=null){
            creatorName =  YHUtility.encodeSpecial(perLogic.getNameBySeqIdStr(instock.getGiftCreator(), dbConn));
          }
          if(instock.getGiftKeeper()!=null){
            giftKeeperName = YHUtility.encodeSpecial(perLogic.getNameBySeqIdStr(instock.getGiftKeeper(), dbConn));
          }
          data = YHFOM.toJson(instock).toString().substring(0,  YHFOM.toJson(instock).toString().length()-1) + ",giftCreatorName:\""+ creatorName + "\"" + ",giftKeeperName:\"" +giftKeeperName + "\"}";
        }
      }
      if(data.equals("")){
       data = "{}"; 
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 根据用户的管理权限得到所有部门（）
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectDeptToGift(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      String userPriv = user.getUserPriv();//角色
      String postpriv = user.getPostPriv();//管理范围
      String postDept = user.getPostDept();//管理范围指定部门
      int userDeptId = user.getDeptId();
      String userName = user.getUserName();
      YHDeptLogic deptLogic = new YHDeptLogic();
      String userDeptName = deptLogic.getNameByIdStr(String.valueOf(userDeptId), dbConn);
      String data = "";
      if(userPriv!=null&&userPriv.equals("1")&&user.getUserId().trim().equals("admin")){//假如是系统管理员的都快要看得到.而且是ADMIN用户
        data =  deptLogic.getDeptTreeJson(0,dbConn) ;
        
      }else{
        if(postpriv.equals("0")){
         // data = "[{text:\"" + userDeptName + "\",value:" + userDeptId + "}]";
          String[] postDeptArray = {String.valueOf(userDeptId)};
          data =  "[" + deptLogic.getDeptTreeJson(0,dbConn,postDeptArray)+ "]";
        }
        if(postpriv.equals("1")){
          data =  deptLogic.getDeptTreeJson(0,dbConn) ;
        }
        if(postpriv.equals("2")){
          if(postDept==null||postDept.equals("")){
            data = "[]";
          }else{
             String[] postDeptArray = postDept.split(",");
             data =  "[" + deptLogic.getDeptTreeJson(0,dbConn,postDeptArray)+ "]";

          }
        }
      }
      if(data.equals("")){
        data = "[]";
      }
      //System.out.println(data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,String.valueOf(userDeptId)+","+userName);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *查询所有的礼品(分页)
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryAllGift(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String giftName = request.getParameter("giftName");
      String giftDesc = request.getParameter("giftDesc");
      String giftCode = request.getParameter("giftCode");
      String giftType = request.getParameter("giftType");
      if(giftName==null){
        giftName = "";
      }
      if(giftDesc==null){
        giftDesc = "";
      }
      if(giftCode==null){
        giftCode = "";
      }
      if(giftType==null){
        giftType = "";
      } 
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHGiftInstockLogic  giftLogic = new YHGiftInstockLogic();
      String data = giftLogic.toSearchData(dbConn,request.getParameterMap(),giftName,giftDesc,giftCode,giftType);
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
   * 查询礼品按礼品类型查找
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getInstockByGiftType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHGiftInstockLogic  giftLogic = new YHGiftInstockLogic();
      String giftType = request.getParameter("giftType");
      YHPersonLogic perLogic = new YHPersonLogic();
      String data = "[";
      List<YHGiftInstock> giftList = new ArrayList<YHGiftInstock>();
      if(giftType!=null){
        giftList  = giftLogic.selectGiftInstock(dbConn, giftType);
        for (int i = 0; i < giftList.size(); i++) {
          YHGiftInstock instock = giftList.get(i);
          int seqId = instock.getSeqId();
          //查询库存量
          int useTransTotal = giftLogic.selectGiftQty(dbConn, seqId);
          int useGiftQty = instock.getGiftQty()-useTransTotal;
          data = data + YHFOM.toJson(instock).toString().substring(0, YHFOM.toJson(instock).toString().length()-1)+",useGiftQty:"+useGiftQty + "},";
        }
       /* if(instock!=null){
          String creatorName = "";
          String giftKeeperName = "";
          if(instock.getGiftCreator()!=null){
            creatorName = perLogic.getNameBySeqIdStr(instock.getGiftCreator(), dbConn);
          }
          if(instock.getGiftKeeper()!=null){
            giftKeeperName = perLogic.getNameBySeqIdStr(instock.getGiftKeeper(), dbConn);;
          }
          data = YHFOM.toJson(instock).toString().substring(0,  YHFOM.toJson(instock).toString().length()-1) + ",giftCreatorName:\""+ creatorName + "\"" + ",giftKeeperName:\"" +giftKeeperName + "\"}";
        }*/
      }
      if(giftList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 查询礼品按礼品名称查找
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectGiftInstockByName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHGiftInstockLogic  giftLogic = new YHGiftInstockLogic();
      String giftName = request.getParameter("giftName");
      YHPersonLogic perLogic = new YHPersonLogic();
      String data = "[";
      List<YHGiftInstock> giftList = new ArrayList<YHGiftInstock>();
      if(giftName!=null){
        giftList  = giftLogic.selectGiftInstockByName(dbConn, giftName);
        for (int i = 0; i < giftList.size(); i++) {
          YHGiftInstock instock = giftList.get(i);
          data = data + YHFOM.toJson(instock).toString() + ",";
        }
       /* if(instock!=null){
          String creatorName = "";
          String giftKeeperName = "";
          if(instock.getGiftCreator()!=null){
            creatorName = perLogic.getNameBySeqIdStr(instock.getGiftCreator(), dbConn);
          }
          if(instock.getGiftKeeper()!=null){
            giftKeeperName = perLogic.getNameBySeqIdStr(instock.getGiftKeeper(), dbConn);;
          }
          data = YHFOM.toJson(instock).toString().substring(0,  YHFOM.toJson(instock).toString().length()-1) + ",giftCreatorName:\""+ creatorName + "\"" + ",giftKeeperName:\"" +giftKeeperName + "\"}";
        }*/
      }
      if(giftList.size()>0){
        data = data.substring(0, data.length()-1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
