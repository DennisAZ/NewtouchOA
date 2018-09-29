package yh.subsys.oa.giftProduct.outstock.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import yh.subsys.oa.giftProduct.instock.data.YHGiftInstock;
import yh.subsys.oa.giftProduct.instock.logic.YHGiftInstockLogic;
import yh.subsys.oa.giftProduct.outstock.data.YHGiftOutstock;
import yh.subsys.oa.giftProduct.outstock.logic.YHGiftOutstockLogic;

public class YHGiftOutstockAct {
  /*
   * 新建礼品管理
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addGiftOutstock(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      Date curDate = new Date();
      String transUser = request.getParameter("user");
      String giftId = request.getParameter("giftId");
      String transQty = request.getParameter("transQty");
      String type = "2";
      if(transUser!=null&&!transUser.equals("")&&giftId!=null){
        YHGiftOutstock giftOutstock = (YHGiftOutstock) YHFOM.build(request.getParameterMap());
        giftOutstock.setTransDate(curDate);
        giftOutstock.setTransUser(transUser);
        //giftOutstock.setGiftId(Integer.parseInt(giftId.split(",")[0]));
        giftOutstock.setOperator(String.valueOf(userId));
        if(!YHUtility.isInteger(transQty)){
          giftOutstock.setTransQty(0);
        }
        int qty = giftOutstock.getTransQty();
        YHGiftInstockLogic instockLogic = new YHGiftInstockLogic();
        YHGiftInstock instock = instockLogic.selectGiftInstockById(dbConn, Integer.parseInt(giftId));
        if(instock!=null){
          //查询库存量
          int useTransTotal = instockLogic.selectGiftQty(dbConn, instock.getSeqId());
          int useGiftQty = instock.getGiftQty()-useTransTotal;
          if(qty<= instock.getGiftQty()){
            type = "1";
            YHGiftOutstockLogic  giftLogic = new YHGiftOutstockLogic();
            int seqId = giftLogic.addGiftOutstock(dbConn, giftOutstock);
            //可存数量也要减掉领用数量
            instockLogic.updateInstockById(dbConn, giftId, qty);
          }
        }
   
      
      }
      String data = "{type:\""+type+"\"}";
      //String data = "{seqId:\"" + seqId + "\",giftName:\""+ giftOutstock.getGiftName()+"\"}";
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
  /**
   * 查询今日领用的礼品记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getOutstockByToday(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHGiftOutstockLogic  giftLogic = new YHGiftOutstockLogic();
      YHPersonLogic perLogic = new YHPersonLogic();
      List<Map<String ,String>> giftList = new ArrayList<Map<String ,String>>();
      giftList = giftLogic.getGiftOutstockToday(dbConn, dateFormat.format(new Date()),userId);
      String data = getJson(giftList);
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
  public String getJson(List<Map<String,String>> mapList){
    StringBuffer buffer=new StringBuffer("["); 
    for(Map<String, String> equipmentsMap:mapList){ 
    buffer.append("{"); 
    Set<String>keySet=equipmentsMap.keySet(); 
    for(String mapStr:keySet){ 
      //System.out.println(mapStr + ":>>>>>>>>>>>>" + equipmentsMap.get(mapStr)); 
      String name=equipmentsMap.get(mapStr); 
      if(name!=null){
        name =name.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
      }
     /* if(mapStr!=null&&mapStr.equals("seqId")){
        
      }*/
      buffer.append( mapStr+":\"" + (name==null? "":name) + "\","); 
    } 
    buffer.deleteCharAt(buffer.length()-1); 
    buffer.append("},"); 
    }
    buffer.deleteCharAt(buffer.length()-1); 
    if (mapList.size()>0) { 
      buffer.append("]"); 
    }else { 
      buffer.append("[]"); 
    }
    String data = buffer.toString();
    //System.out.println(data);
    return data;
  }
  public String getJson(Map<String,String> map){
    StringBuffer buffer=new StringBuffer(""); 
    buffer.append("{"); 
    Set<String>keySet=map.keySet(); 
    for(String mapStr:keySet){ 
      //System.out.println(mapStr + ":>>>>>>>>>>>>" + map.get(mapStr)); 
      String name=map.get(mapStr); 
      if(name!=null){
        name =name.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
      }
      buffer.append( mapStr+":\"" + (name==null? "":name) + "\","); 

    }
    buffer.deleteCharAt(buffer.length()-1); 
    buffer.append("}"); 
    String data = buffer.toString();
    //System.out.println(data);
    return data;
  }
  /**
   * 删除outstock
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delOutstockById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      if(seqId!=null&&!seqId.equals("")){
        YHGiftOutstockLogic  giftLogic = new YHGiftOutstockLogic();
        //得到本次领用的礼品个数-退回的个数
        YHGiftOutstock outstock = giftLogic.selectOutstockById(dbConn,Integer.parseInt(seqId));
        int transQty = outstock.getTransQty();
        int transFlag = outstock.getTransFlag();
        //得到本次真正领用的礼品个数
        int useTran = 0;
        useTran = transQty-transFlag;
        if(useTran>0){
          //给还原 礼品入库
         YHGiftInstockLogic instockLogic = new YHGiftInstockLogic();
         instockLogic.updateInstockByIdBack(dbConn, outstock.getGiftId(), useTran);
        }
        giftLogic.delOutstockById(dbConn, Integer.parseInt(seqId));
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 查询outstock 退库
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getOutstockByIdBack(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String data = "";
      if(seqId!=null&&!seqId.equals("")){
        YHGiftOutstockLogic  giftLogic = new YHGiftOutstockLogic();
        Map<String,String> map = giftLogic.getOutstockByIdBack(dbConn, Integer.parseInt(seqId));
        data = getJson(map);
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
   * 查询outstock ById编辑
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getOutstockById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String data = "";
      if(seqId!=null&&!seqId.equals("")){
        YHGiftOutstockLogic  giftLogic = new YHGiftOutstockLogic();
        YHGiftOutstock outstock = new YHGiftOutstock();
        Map<String ,String> map = giftLogic.selectGiftOutstockById(dbConn, Integer.parseInt(seqId));
        if(outstock!=null){
          data = getJson(map);
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
   * 更新礼品领用
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateGiftstock(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String transFlag = request.getParameter("transFlag");
      String data = "";
      if(seqId!=null&&!seqId.equals("")&&transFlag!=null&&!transFlag.equals("")){
        YHGiftOutstockLogic  giftLogic = new YHGiftOutstockLogic();
        //得到领用表
        YHGiftOutstock outstock = giftLogic.selectOutstockById(dbConn,Integer.parseInt(seqId));
        if(outstock!=null){
          //领用表改变
          giftLogic.updateGiftBack(dbConn, Integer.parseInt(seqId), transFlag);
          //改变礼品表
          //给还原 礼品入库
          YHGiftInstockLogic instockLogic = new YHGiftInstockLogic();
         int useTran = 0;
         instockLogic.updateInstockByIdBack(dbConn, outstock.getGiftId(),Integer.parseInt(transFlag));
        }
       
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      //request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 更新礼品领用详细信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateGiftstockInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String newTransQty = request.getParameter("transQty");//新的领用数量
      String transUses = request.getParameter("transUses");
      String transMemo = request.getParameter("transMemo");
      String userId = request.getParameter("user");
      String data = "";
      String Type = "1";//1：正常
      String giftQty = "0";
      if(!YHUtility.isNullorEmpty(seqId)){
        YHGiftOutstockLogic  outLogic = new YHGiftOutstockLogic();
        //得到领用表
        YHGiftOutstock outstock = outLogic.selectOutstockById(dbConn,Integer.parseInt(seqId));
        if(outstock!=null&&!YHUtility.isNullorEmpty(newTransQty)){
          int transQty = outstock.getTransQty();//原来的本次领用的数量
          int transFlag = outstock.getTransFlag();//原来的本次领用的退回数量
          int transUes = transQty - transFlag;//原来实际领用量
          if(Integer.parseInt(newTransQty)<transFlag){
            Type = "2";//不能小于退回数量
          }else{ 
            YHGiftInstockLogic instockLogic = new YHGiftInstockLogic();
            YHGiftInstock instock = instockLogic.selectGiftInstockById(dbConn,outstock.getGiftId());//得到礼品
            if(instock!=null){
              //查询库存量
              int useTransTotal = instock.getGiftQty();//instockLogic.selectGiftQty(dbConn, instock.getSeqId());
              int newUseTransTotal = useTransTotal + transUes;//得到库存总量
              if(Integer.parseInt(newTransQty)>newUseTransTotal){
                Type = "3";//超出库存
                giftQty = newUseTransTotal + "";
              }else{
                //领用表改变
                 outstock.setTransUser(userId);
                 outstock.setTransQty(Integer.parseInt(newTransQty));
                 outstock.setTransMemo(transMemo);
                 outstock.setTransUses(transUses);
                 outLogic.updateGiftOutstock(dbConn, outstock);
                 //给还原 礼品入库
                 int useTotal = transQty - Integer.parseInt(newTransQty);
                 instockLogic.updateInstockByIdBack(dbConn, outstock.getGiftId(),useTotal);
              }
            }
          
          }    
        } 
      }
      data = "{type:" + Type +  ",giftQty:" + giftQty + "}";
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
   * 查询领用的礼品记录根据用户ID
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String  getOutstockByUserId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      //int userId = user.getSeqId();
      String userId = request.getParameter("userId");
      YHGiftOutstockLogic  giftLogic = new YHGiftOutstockLogic();
      YHPersonLogic perLogic = new YHPersonLogic();
      List<Map<String ,String>> giftList = new ArrayList<Map<String ,String>>();
      if(userId!=null&&!userId.equals("")){
        giftList = giftLogic.getGiftOutstockByUserId(dbConn,userId);
      }
      String data = getJson(giftList);
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
  public String queryGiftByTemp(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String giftName = request.getParameter("giftName");
      String giftType = request.getParameter("giftType");
      String giftId = request.getParameter("giftId");
      String fromDate = request.getParameter("fromDate");
      String toDate = request.getParameter("toDate");
      if(giftName==null){
        giftName = "";
      }
      if(giftType==null){
        giftType = "";
      }
      if(giftId==null){
        giftId = "";
      }
      if(fromDate==null){
        fromDate = "";
      } 
      if(toDate==null){
        toDate = "";
      } 
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHGiftOutstockLogic  giftLogic = new YHGiftOutstockLogic();
      String data = giftLogic.toSearchData(dbConn,request.getParameterMap(),giftType,giftId,giftName,fromDate,toDate);
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
   * 采购物品报表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectReport(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String giftType = request.getParameter("giftType");
      String giftId = request.getParameter("giftId");
      String fromDate = request.getParameter("fromDate");
      String toDate = request.getParameter("toDate");
      if(giftType==null){
        giftType = "";
      }
      if(giftId==null){
        giftId = "";
      }
      if(fromDate==null){
        fromDate = "";
      } 
      if(toDate==null){
        toDate = "";
      } 
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHGiftOutstockLogic  giftLogic = new YHGiftOutstockLogic();
      List<Map<String,String>> list = giftLogic.selectReport(dbConn, giftType, giftId, fromDate, toDate);
      String data = getJson(list);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 物品总表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectProductInfo(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String giftType = request.getParameter("giftType");
      String giftId = request.getParameter("giftId");
      String fromDate = request.getParameter("fromDate");
      String toDate = request.getParameter("toDate");
      if(giftType==null){
        giftType = "";
      }
      if(giftId==null){
        giftId = "";
      }
      if(fromDate==null){
        fromDate = "";
      } 
      if(toDate==null){
        toDate = "";
      } 
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHGiftOutstockLogic  giftLogic = new YHGiftOutstockLogic();
      List<Map<String,String>> list = giftLogic.selectProductInfo(dbConn, giftType, giftId, fromDate, toDate);
      String data = getJson(list);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 部门领用汇总
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getGiftOutByDeptId(HttpServletRequest request,
      HttpServletResponse response)throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String giftType = request.getParameter("giftType");
      String giftId = request.getParameter("giftId");
      String fromDate = request.getParameter("fromDate");
      String toDate = request.getParameter("toDate");
      String deptId = request.getParameter("deptId");
      if(giftType==null){
        giftType = "";
      }
      if(giftId==null){
        giftId = "";
      }
      if(fromDate==null){
        fromDate = "";
      } 
      if(toDate==null){
        toDate = "";
      } 
      
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      YHGiftOutstockLogic  giftLogic = new YHGiftOutstockLogic();
      String data  = "";
      if(deptId!=null&&!deptId.equals("")){
        List<Map<String,String>> list = giftLogic.getGiftOutByDeptId(dbConn, giftType, giftId, fromDate, toDate, deptId);
        data = getJson(list);
      }
      if(data.equals("")){
        data = "[]";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
}
