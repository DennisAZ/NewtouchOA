package yh.subsys.jtgwjh.sealmanage.act;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHExtDept;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.jtgwjh.sealmanage.data.YHJhSeal;
import yh.subsys.jtgwjh.sealmanage.data.YHJhSealLog;
import yh.subsys.jtgwjh.sealmanage.logic.YHJhSealLogLogic;
import yh.subsys.jtgwjh.sealmanage.logic.YHJhSealLogic;

public class YHJhSealAct {
  private static Logger log = Logger.getLogger(YHJhSealAct.class);
  YHJhSealLogic logic = new YHJhSealLogic();
  /**
   * 印章权限管理列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getSealList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String sealId = request.getParameter("sealId");
      String sealName = request.getParameter("sealName");
      sealName = YHDBUtility.escapeLike(sealName);
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      String data = "";
      YHJhSealLogic sl = new YHJhSealLogic();
      if(!YHUtility.isNullorEmpty(sealId) || !YHUtility.isNullorEmpty(sealName) || !YHUtility.isNullorEmpty(beginTime) || !YHUtility.isNullorEmpty(endTime)){
        data = sl.getSearchList(dbConn,request.getParameterMap(), sealId, sealName, beginTime, endTime);
      }else{
        data = sl.getSealList(dbConn,request.getParameterMap());
      }
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
  
  public String getSearchList(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String sealId = request.getParameter("sealId");
      String sealName = request.getParameter("sealName");
      String beginTime = request.getParameter("beginTime");
      String endTime = request.getParameter("endTime");
      YHJhSealLogic sl = new YHJhSealLogic();
      String data = sl.getSearchList(dbConn,request.getParameterMap(), sealId, sealName, beginTime, endTime);
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
  
  public String addPerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    int num = 0;
    Map map = new HashMap();
    String userIdOld = "";
    String userId = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      
        String userName = request.getParameter("userName");
        String userPriv = request.getParameter("userPriv");
        String userPrivOther = request.getParameter("role");
        
        map.put("userId" , userId);
        map.put("userName" , userName);
        map.put("userPriv" , userPriv);
       
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn , "sealLog" , map);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功添加人员");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateSealPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map map = new HashMap();
    Map mapSealLog = new HashMap();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int loginSeqId = person.getSeqId();
      String userName=person.getUserName();
      String loginSeqIds = String.valueOf(loginSeqId);
      YHJhSealLogic sl = new YHJhSealLogic();
      YHJhSeal seal  = new YHJhSeal();
      YHJhSealLog slLog = new YHJhSealLog();
        String seqId = request.getParameter("seqId");
        String userStr = request.getParameter("userStr");
        //String userName = request.getParameter("userName");
        String deptName = request.getParameter("deptName");
        String deptStr = request.getParameter("deptStr");
        String sealName=sl.getSealName(dbConn, seqId);
        map.put("seqId" , seqId);
        map.put("userStr" , userStr);
        map.put("deptStr", deptStr);
        seal.setSeqId(Integer.parseInt(seqId));
        seal.setUserStr(userStr);
        seal.setDeptStr(deptStr);
      YHORM orm = new YHORM();
      //orm.updateSingle(dbConn, "seal", map);
      orm.updateSingle(dbConn, seal);
      Calendar cal = Calendar.getInstance();
      java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        
      String logTime = sdf.format(cal.getTime());
      String sealId = sl.getSealId(dbConn, Integer.parseInt(seqId));
      slLog.setsId(sealId);
      slLog.setSealName(sealName);
      slLog.setLogType("setseal");
      slLog.setLogTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(logTime));
      slLog.setResult("授权给:"+deptName);
      slLog.setIpAdd(request.getRemoteAddr());
      slLog.setUserId(loginSeqIds);
      slLog.setUserName(userName);
      //orm.saveSingle(dbConn , "sealLog" , map);
      orm.saveSingle(dbConn, slLog);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功添加人员");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取用户名称
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getUserName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = "";
      String userIdStr = request.getParameter("userId");
      YHJhSealLogic dl = new YHJhSealLogic();
      if(!YHUtility.isNullorEmpty(userIdStr)){
        data = dl.getUserNameLogic(dbConn, userIdStr);
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSealPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String seqIds = request.getParameter("seqId");
      int seqId = Integer.parseInt(seqIds);
      List<Map> list = new ArrayList();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"SEQ_ID="+seqId};
     // List funcList = new ArrayList();
     // funcList.add("seal");
     // map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      
      String[] Str = {"SEQ_ID="+seqId};
      List<YHJhSeal> list2 = YHJhSealLogic.selectSeal(dbConn, Str);
    //  list.addAll((List<Map>) map.get("SEAL"));
      if(list.size() > 1){
        for(YHJhSeal ms : list2){
          sb.append("{");
          sb.append("seqId:\"" + ms.getSeqId()+ "\"");
          sb.append(",userStr:\"" + YHUtility.encodeSpecial(ms.getUserStr()) + "\"");
          sb.append(",deptStr:\"" +  YHUtility.encodeSpecial(ms.getDeptStr()) + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(YHJhSeal ms : list2){
          sb.append("{");
          sb.append("seqId:\"" + ms.getSeqId()+ "\"");
          sb.append(",userStr:\"" + YHUtility.encodeSpecial(ms.getUserStr()) + "\"");
          sb.append(",deptStr:\"" +  YHUtility.encodeSpecial(ms.getDeptStr()) + "\"");
         
          sb.append("}");
        }
      }    
      sb.append("]");
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
   * 删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteSeal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      String sumStrs = request.getParameter("sumStrs");
      YHJhSealLogic pl = new YHJhSealLogic();
      if(!YHUtility.isNullorEmpty(sumStrs)){
        String seal [] = pl.selectSeal(dbConn, sumStrs);
        pl.deleteSeal(dbConn, sumStrs);
        //删除印章添加印章日志
        YHJhSealLogLogic dl = new YHJhSealLogLogic();
        YHJhSealLog sl = new YHJhSealLog();
        sl.setLogTime(new Date());
        sl.setLogType("delseal");
        sl.setSealName(seal[1]);
        sl.setsId(seal[0]);
        sl.setResult("批量删除印章；印章名称为" + seal[1]);
        sl.setMacAdd("");
        sl.setIpAdd(request.getRemoteAddr());
        sl.setUserId(seqId + "");
        sl.setUserName(person.getUserName());
        dl.addSealLog(dbConn, sl);
      }
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取印章后四位ID
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String getCounter(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = "";
      String SEAL_ID = request.getParameter("SEAL_ID");
      YHJhSealLogic dl = new YHJhSealLogic();
      if(!YHUtility.isNullorEmpty(SEAL_ID)){
        data = dl.getCounterLogic(dbConn, SEAL_ID);
      }
      //System.out.println(data+"NMNMNMNMNMN");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String addSeal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map map = new HashMap();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      YHJhSeal sls = new YHJhSeal();
      String sealId = request.getParameter("sealId");
      String sealName = request.getParameter("sealName");
      //String certStr = request.getParameter("cetrStr");
      //sealName = YHUtility.encodeSpecial(sealName);
//      if(!YHUtility.isNullorEmpty(sealName)){
//        sealName = sealName.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
//      }
      String sealData = request.getParameter("sealData");
      //System.out.println(sealData);
      String deptId = request.getParameter("deptId");
      String isFlag = request.getParameter("isFlag");
      YHDeptTreeLogic logic = new YHDeptTreeLogic();
      String deptName=logic.getDeptName(dbConn, deptId);
      Calendar cal = Calendar.getInstance();        
      java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        
      String createTime = sdf.format(cal.getTime());    
   
      sls.setSealId(sealId);
      sls.setSealName(sealName);
      //sls.setCertStr(certStr);
      sls.setSealData(sealData);
      sls.setOutDeptId(deptId);
      sls.setOutDeptName(deptName);
     // sls.setDeptId(deptId);
      sls.setIsFlag(isFlag);
      sls.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime));
       
      YHORM orm = new YHORM();
      //orm.saveSingle(dbConn , "seal" , map);
      orm.saveSingle(dbConn , sls);
      
      
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功添加人员");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "图片大小超过了64K不能新建！");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 根据用户的管理权限得到所有部门（考勤统计）

   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectDeptToAttendance(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      //int deptId = Integer.parseInt(request.getParameter("deptId"));
      int userId = user.getSeqId();
      String userPriv = user.getUserPriv();// 角色
      String postpriv = user.getPostPriv();// 管理范围
      String postDept = user.getPostDept();// 管理范围指定部门
      int userDeptId = user.getDeptId();
      YHDeptLogic deptLogic = new YHDeptLogic();
     // boolean isAdminRole = user.isAdminRole();
      boolean isAdmin = user.isAdmin();
      String data = "";
      if (YHUtility.isNullorEmpty(postpriv) || isAdmin) {
        if (isAdmin && postpriv.equals("1")) {
          data = deptLogic.getDeptTreeJson(0, dbConn);
        } else {
          String[] postDeptArray = {String.valueOf(userDeptId)};
          data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
              + "]";
        }
      } else {
        if (postpriv.equals("0")) {
          // data = "[{text:\"" + userDeptName + "\",value:" + userDeptId + "}]";
            String[] postDeptArray = {String.valueOf(userDeptId)};
            data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
                + "]";
         }
         if (postpriv.equals("1")) {
           data = deptLogic.getDeptTreeJson(0, dbConn);
         }
         if (postpriv.equals("2")) {
           if (postDept == null || postDept.equals("")) {
             data = "[]";
           } else {
             String[] postDeptArray = postDept.split(",");
             data = "[" + deptLogic.getDeptTreeJson(0, dbConn, postDeptArray)
                 + "]";

           }
         }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, String.valueOf(userDeptId)
          + "," + postpriv);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getShowInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map map = new HashMap();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("seqId");
      int seqId = Integer.parseInt(seqIdStr);
      String data = "";
      YHJhSealLogic sl = new YHJhSealLogic();
      data = sl.getShowInfo(dbConn, seqId);
      //System.out.println(data+"JKJKLK");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 根据外部ID（32字符串）字符串获取名称
   * syl
   */
  
  public String getOutDeptNameByDeptIds(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map map = new HashMap();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      String deptIds = request.getParameter("deptIds");
      String dept = "";
      String deptName = "";
      if(!YHUtility.isNullorEmpty(deptIds)){
        String[] esbDept = YHDeptTreeLogic.getEsbUsers2(dbConn, deptIds);
        if(esbDept.length == 2 && !YHUtility.isNullorEmpty(esbDept[0]) && !YHUtility.isNullorEmpty(esbDept[1])){
          dept = esbDept[0];
          deptName = esbDept[1];
        }
      }
      String data = "{esbDept:\"" + YHUtility.encodeSpecial(dept) + "\",esbDeptName:\"" + YHUtility.encodeSpecial(deptName)  + "\"}";
      //System.out.println(data+"JKJKLK");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 部门Id（ESB客户端--》外部组织机构单位Id）获取本地有权限的公章
   * 
   * syl
   */
  
  public String getPrivSealByDeptId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map map = new HashMap();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      String webroot = request.getRealPath("/");
      YHEsbClientConfig config = YHEsbClientConfig.builder(webroot + YHEsbConst.CONFIG_PATH) ;
      String userId = config.getUserId();//client
      YHDeptTreeLogic dtl = new YHDeptTreeLogic();
      YHExtDept ed = dtl.getDeptByEsbUser(dbConn, userId);//根据发送部门，查询外部组织机构
      String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
      List<YHJhSeal> list = null;
      if(!YHUtility.isNullorEmpty(seqId)){
        String[] str = {YHDBUtility.findInSet(ed.getDeptId()+"", "DEPT_STR"),"SEQ_ID=" + seqId};
      }else{
        String[] str = {YHDBUtility.findInSet(ed.getDeptId()+"", "DEPT_STR")};
        list = logic.selectSeal(dbConn, str);
      }
     
      String data = "[";
      for (int i = 0; i < list.size(); i++) {
        data = data + YHFOM.toJson(list.get(i)) + ",";
      }
      if(list.size() > 0){
        data = data.substring(0, data.length() -1 );
      }
      data = data + "]";
      //System.out.println(data+"JKJKLK");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 启用或停用
   */
  
  public String doIsFlag(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Map map = new HashMap();
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String seqId = request.getParameter("seqId");
      String isFlag = request.getParameter("isFlag");
      String ip=request.getRemoteAddr();
      String logType="writeseal";
      YHJhSealLogLogic logLogic=new YHJhSealLogLogic();
      YHJhSeal seal=logic.getSeal(dbConn,seqId);
      if(YHUtility.isInteger(seqId)){
        logic.updateIsFlag(dbConn, seqId, isFlag);
        String result = "恢复启用印章";
        if(isFlag.equals("0")){
          result = "停用印章";
        }
        
        logLogic.addSealInfoLog(dbConn, person, seal, logType, ip,result);
      }
     
      //System.out.println(data+"JKJKLK");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  
  /**
   * 触发盖章生成印章文件
   */
  
  public String stampSeal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      String BASE64DATA = request.getParameter("BASE64DATA");
      String seqId = request.getParameter("seqId");
      BASE64Decoder de = new BASE64Decoder();
      response.setContentType("application/octet-stream");      
      response.setHeader("Content-Disposition", "attachment;filename=seal.sel");
      if(YHUtility.isInteger(seqId)){
       // BASE64DATA = "TEFFU84DAABxpHlJAQDZnFtFQKFCvsKVBgAAADcAAADuEQAAAAAAAP4NAADkD8qMK1LgEl1ktCXvL4Id+4JNw92eABwStNIWJoejuQIh4NDntKz40YusweYvT2Y2WHhlBzPWu8y/JyJTFuPqt5+EjwNKnTYmya4yugoW7r53iQXt0lLF3haxL6Yzy4efHRY2Hk2vpkmL/SjFORCvhMwv4/s+TRzbPIpRec/ct+gn5NwiGm92C67MPTJLiiR53sfxTV94AsL6SwLUyNDah6j/L2fOkzaaJ2L1ZBgYqqXhT+uwlWx7ZAkEB34A7Cfef6Fpzp7JARyAlUzG8Z88FbbyFFlb8XupxKrYyTlSB8FYATxk3N/CE9ZoA0hKKYvSTfgAlUrs3X3xSGuWcDRh+uZCAlaovtNPNayfxBL96Gah3f9ZXFjSzSHTaX1LnOK9E2SNVUnCcWMCkObOk4XDVF/IFEnaEKLNj3S8wXsDwuCONnWz4Ah44uXC2BO5bI2vuAc324yzwh6BRBmIe5wx0fftnFG565R5693JCgY35VZBJdqWiSAEXxo+aMtfrrRVnNFckPRMzKJxBTZe6CSGLGcUnAQPYn0zehAMlqa6bYs6Ldip2sHyAoxezlCn9ooYYkR8jdWb7RzX8aOT86agQn+B1tjF+NW6casgKdSIaYvgFoMNOZ1Em8S38eTFHLzG6H+FobqwfdejXDYx8aDqOoKU2yRsaW/7SgV7O4WJC3ayqcYGBhZ6V5OYZB2NX+3XI3oodMnFFd/bcKFa4T4QegW1uQJCEB9Kv1jNgbwp8B+C6sMIqf6xTD4yqq6BzsGepSRKsHAE64p30DWk5jXwYtO0XqOK6CycUlN2GkiqoWoBVG84P0faDOkFBC4G0tZsYPNvdPQu/5cCVtjilb8bDm5X5pIMajezIr9bIcG8TLqArvncC3jKQzWgDBJUA3+wXj6avFnhRuJIyKj2Ca5iasjf4sb5f0FM80GcOfgOcfl51BKPKl4/d7oZWBxtwZY0duzkvgFLANCPROi/lAmrPTLh+68VsxYTHuWUb3mnhqneDKQqDCoAm0vUbW/ekHfG3lnYdHrSh7WtPopBMAwlDNkYkquQzlRrk8pNHuSTMZ6xUhWIdhUCsx/sq7lmCFwu3TsbymsIgHQNXcWjmWdmXU0z+6bN7DKERa+g5682NbP3WzIk9MSJ6pLXqW6e3mqmI0yhrSpOkfJ8MOOE0Dfe0fmwLuoDy7i19Tmj2nPuqtZfxWCkL569yp2i8fOp0tiV8TLnJZ6h2eyJgR7WoIL0AAA=";
       // BASE64DATA = "TEFFUwcDAAA4qUn8fVk9IoHrplAun57vBgAAAGkAAAA/EgAAAAAAAEIQAAC9DebDYOtGYlufu64Y7/gzZ68lJ8Vb2cinukJNVDoSRjCFrNmWHINPVF2v0YxOHkboTN/c29j0DxNDsEX36M1F44T3PF6CSww1J8td ZhIY";

        BASE64DATA  = logic.getShowInfo(dbConn, Integer.parseInt(seqId));
         
      //  BASE64DATA ="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";
        byte[] b = de.decodeBuffer(BASE64DATA);
        
        YHFileUtility.storBytes2File("D:\\test.sel", b);   
        OutputStream out = response.getOutputStream();
        
        int readLength = 0;
        out.write(b,0,b.length);
        out.flush();
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return  null;
  }
  
  /**
   * 获取外部组织机构
   */
  
  public String getExpt(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      YHDeptTreeLogic deptLogic = new YHDeptTreeLogic();
      
      
      String data =  deptLogic.getDeptTreeJson(0,dbConn) ;
    //System.out.println(data+"JKJKLK");
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_DATA,data);
  }catch(Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
  }
  return "/core/inc/rtjson.jsp";
}

  /**
   * 获取印章
   */
  
  public String selectBMP(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
   
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String fileName = "D:/test/test.csv";
      byte[] b = YHFileUtility.loadFile2Bytes(fileName);
      File file = new File(fileName);
     InputStream is =new BufferedInputStream(new FileInputStream(file));
     ArrayList<YHDbRecord> drl = YHCSVUtil.CVSReader(is);
      // ArrayList<YHDbRecord> drl = YHJExcelUtil.readExc(is,true); 
      String data = "[";
      for (int i = 0; i < drl.size(); i++) {
        YHDbRecord rd = drl.get(i);
         String unitCode = rd.getValueByName("单位代码") == null ? "" : (String)rd.getValueByName("单位代码");
        String sealName = rd.getValueByName("印章名称") == null ? "" : (String)rd.getValueByName("印章名称");
        String sealFilePath= rd.getValueByName("印章图片路径") == null ? "" : (String)rd.getValueByName("印章图片路径");
        String sealID= rd.getValueByName("印章编号") == null ? "01" : (String)rd.getValueByName("印章编号");
        String sealWidth= rd.getValueByName("印章宽度") == null ? "" : (String)rd.getValueByName("印章宽度");
        String sealHeight= rd.getValueByName("印章高度") == null ? "" : (String)rd.getValueByName("印章高度");
        
        if(sealID.length() == 1){
          sealID = "0" + sealID;
        }

        data = data + "{unitCode:\"" + YHUtility.encodeSpecial(unitCode) + "\","
           + "sealName:\"" + YHUtility.encodeSpecial(sealName) + "\","
             + "sealFilePath:\"" + YHUtility.encodeSpecial(sealFilePath) + "\""
               + ",sealID:\"" + YHUtility.encodeSpecial(sealID) + "\""
             + ",sealWidth:\"" + YHUtility.encodeSpecial(sealWidth) + "\"" 
              + ",sealHeight:\"" + YHUtility.encodeSpecial(sealHeight) + "\"" 
           + "},";
      }
      if(!data.equals("[")){
        data = data.substring(0, data.length() -1 );
      }
      data = data + "]";
    //System.out.println(data+"JKJKLK");
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_DATA,data);
  }catch(Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
  }
  return "/core/inc/rtjson.jsp";
}
  
  
  /**
   * 根据单位名称获取单位Id
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectDeptByName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptName = request.getParameter("deptName");
      String[] str = { "dept_name like '%" + YHDBUtility.escapeLike(deptName.trim()) + "%' " + YHDBUtility.escapeLike() + " order by dept_code" };
      
      List<YHExtDept> codeList = YHDeptTreeLogic.select(dbConn, str);
      String data = "{count:" + codeList.size() + ",lis:[";
      int i = 0;
      for (; i < codeList.size() && i < 10; i++) {
        YHExtDept code = codeList.get(i);
        data += "{id:'" + code.getDeptId() + "',string:'" + code.getDeptName() + "'},";
        // data = data + YHFOM.toJson(code) + ",";
      }
      if (i > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]}";
      request.setAttribute(YHActionKeys.RET_DATA, data);
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
   * 根据单位名称名称反查对象
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectDeptName(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deptName = request.getParameter("deptName");
      deptName = deptName.replace("'", "''");
      String[] str = { "dept_name ='" + deptName.trim() + "'" };
      List<YHExtDept> codeList = YHDeptTreeLogic.select(dbConn, str);
      String data = "[";
      int i = 0;
      for (; i < codeList.size() && i < 10; i++) {
        YHExtDept code = codeList.get(i);
        data += "{id:'" + code.getDeptId() 
            + "',deptName:'" + code.getDeptName()
           + "',deptCode:'" + code.getDeptCode()+ "'},";
        // data = data + YHFOM.toJson(code) + ",";
      }
      if (i > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加数据成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  
  public static void main(String[] args) throws Exception {
    BASE64Decoder decoder = new BASE64Decoder();
    String seal = "TEFFU84DAABxpHlJAQDZnFtFQKFCvsKVBgAAADcAAADuEQAAAAAAAP4NAADkD8qMK1LgEl1ktCXvL4Id+4JNw92eABwStNIWJoejuQIh4NDntKz40YusweYvT2Y2WHhlBzPWu8y/JyJTFuPqt5+EjwNKnTYmya4yugoW7r53iQXt0lLF3haxL6Yzy4efHRY2Hk2vpkmL/SjFORCvhMwv4/s+TRzbPIpRec/ct+gn5NwiGm92C67MPTJLiiR53sfxTV94AsL6SwLUyNDah6j/L2fOkzaaJ2L1ZBgYqqXhT+uwlWx7ZAkEB34A7Cfef6Fpzp7JARyAlUzG8Z88FbbyFFlb8XupxKrYyTlSB8FYATxk3N/CE9ZoA0hKKYvSTfgAlUrs3X3xSGuWcDRh+uZCAlaovtNPNayfxBL96Gah3f9ZXFjSzSHTaX1LnOK9E2SNVUnCcWMCkObOk4XDVF/IFEnaEKLNj3S8wXsDwuCONnWz4Ah44uXC2BO5bI2vuAc324yzwh6BRBmIe5wx0fftnFG565R5693JCgY35VZBJdqWiSAEXxo+aMtfrrRVnNFckPRMzKJxBTZe6CSGLGcUnAQPYn0zehAMlqa6bYs6Ldip2sHyAoxezlCn9ooYYkR8jdWb7RzX8aOT86agQn+B1tjF+NW6casgKdSIaYvgFoMNOZ1Em8S38eTFHLzG6H+FobqwfdejXDYx8aDqOoKU2yRsaW/7SgV7O4WJC3ayqcYGBhZ6V5OYZB2NX+3XI3oodMnFFd/bcKFa4T4QegW1uQJCEB9Kv1jNgbwp8B+C6sMIqf6xTD4yqq6BzsGepSRKsHAE64p30DWk5jXwYtO0XqOK6CycUlN2GkiqoWoBVG84P0faDOkFBC4G0tZsYPNvdPQu/5cCVtjilb8bDm5X5pIMajezIr9bIcG8TLqArvncC3jKQzWgDBJUA3+wXj6avFnhRuJIyKj2Ca5iasjf4sb5f0FM80GcOfgOcfl51BKPKl4/d7oZWBxtwZY0duzkvgFLANCPROi/lAmrPTLh+68VsxYTHuWUb3mnhqneDKQqDCoAm0vUbW/ekHfG3lnYdHrSh7WtPopBMAwlDNkYkquQzlRrk8pNHuSTMZ6xUhWIdhUCsx/sq7lmCFwu3TsbymsIgHQNXcWjmWdmXU0z+6bN7DKERa+g5682NbP3WzIk9MSJ6pLXqW6e3mqmI0yhrSpOkfJ8MOOE0Dfe0fmwLuoDy7i19Tmj2nPuqtZfxWCkL569yp2i8fOp0tiV8TLnJZ6h2eyJgR7WoIL0AAA=";
      byte[] b = decoder.decodeBuffer(seal);
      YHFileUtility.storBytes2File("C:\\yh\\sdc.sel", b);
  }
}
