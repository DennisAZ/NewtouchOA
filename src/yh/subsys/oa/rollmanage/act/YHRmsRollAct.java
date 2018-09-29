package yh.subsys.oa.rollmanage.act;


import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.address.data.YHAddress;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.rollmanage.data.YHRmsRoll;
import yh.subsys.oa.rollmanage.logic.YHRmsRollLogic;

public class YHRmsRollAct {
  private YHRmsRollLogic logic = new YHRmsRollLogic();
  private static Logger log = Logger.getLogger(YHRmsRollAct.class);

  /**
   * 新建案卷
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
  public String addRoll(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<String,String[]> map = request.getParameterMap();
      Date time = new Date();
      YHRmsRoll rmsRoll = (YHRmsRoll) YHFOM.build(map, YHRmsRoll.class, "");
      int priority=Integer.parseInt(request.getParameter("priority"));
      rmsRoll.setAddUser(String.valueOf(person.getSeqId()));
      rmsRoll.setAddTime(time);
      rmsRoll.setStatus("0");
      rmsRoll.setPriority(priority);
      this.logic.add(dbConn, rmsRoll);
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
   * 所属部门下拉框
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

      YHDeptLogic deptLogic = new YHDeptLogic();
      String data = "";
      data = deptLogic.getDeptTreeJson(0, dbConn);
      if(YHUtility.isNullorEmpty(data)){
        data = "[]";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取卷库管理列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRmsRollJosn(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String seqId = request.getParameter("seqId");
      String flag = request.getParameter("flag");
      String data = this.logic.getRmsRollJson(dbConn,request.getParameterMap(), person, seqId , flag);
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  public String getRmsRollDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHRmsRoll rmsRoll = (YHRmsRoll)this.logic.getRmsRollDetail(dbConn, Integer.parseInt(seqId));
      if (rmsRoll == null){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
        request.setAttribute(YHActionKeys.RET_MSRG, "案卷不存在");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(rmsRoll);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功"); 
      request.setAttribute(YHActionKeys.RET_DATA, data.toString()); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  /**
   * 编辑案卷
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String update(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String,String[]> map = request.getParameterMap();
      YHRmsRoll rmsRoll = (YHRmsRoll) YHFOM.build(map, YHRmsRoll.class, "");
      //rmsRollRoom.setModUser(modUser)
      this.logic.updateRmsRoll(dbConn, rmsRoll);
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
   * 删除一条案卷记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteSingle(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      this.logic.updateRmsFile(dbConn, Integer.parseInt(seqId));
      this.logic.deleteSingle(dbConn, Integer.parseInt(seqId));
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteContact(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("sumStrs");
      this.logic.updateAllRoll(dbConn, seqIdStr);
      this.logic.deleteAllRoll(dbConn, seqIdStr);
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
   * 获取所属卷库
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRmsRollRoomName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("seqId");
      int seqId = Integer.parseInt(seqIdStr);
      String data = this.logic.getRmsRollRoomNameLogic(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + YHUtility.encodeSpecial(data) + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 更改案卷状态
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateStatusFlag(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String seqIdStr = request.getParameter("seqId");
      int seqId = Integer.parseInt(seqIdStr);
      String statusFlag = request.getParameter("statusFlag");
      String data = this.logic.getRmsRollRoomNameLogic(dbConn, seqId);
      if(YHUtility.isNullorEmpty(statusFlag)){
        statusFlag = "0";
      }else{
        if("0".equals(statusFlag)){
          statusFlag = "1";
        }else if("1".equals(statusFlag)){
          statusFlag = "0";
        }else{
          statusFlag = "0";
        }
      }
      Map m =new HashMap();
      Date time = new Date();
      m.put("seqId", seqId);
      m.put("status", statusFlag);
      m.put("modUser", String.valueOf(person.getSeqId()));
      m.put("modTime", YHUtility.getCurDateTimeStr());
      YHORM orm = new YHORM();
      orm.updateSingle(dbConn, "rmsRoll", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 获取小编码表内容
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCodeName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String classCode = request.getParameter("classCode");
      String classNo = request.getParameter("classNo");
      String data = this.logic.getCodeNameLogic(dbConn, classCode, classNo);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 取得文件列表
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRmsFileJosn(HttpServletRequest request, HttpServletResponse response) throws Exception {

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int rollId = Integer.parseInt(request.getParameter("seqId"));
      String data = this.logic.getRmsFileJosn(dbConn, request.getParameterMap(), person, rollId);

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
   * 查询出select中的案卷
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRmsRollSelect(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      
      String[] filters2 = new String[]{"STATUS = 0"};
      List funcList = new ArrayList();
      funcList.add("rmsRoll");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters2);
      list.addAll((List<Map>) map.get("OA_ARCHIVES_VOLUME"));
      int flag = 0;
      for(Map ms : list){
        String rollName = (String) ms.get("rollName");
        if(!YHUtility.isNullorEmpty(rollName)){
          rollName = YHUtility.encodeSpecial(rollName);
        }
        String rollCode = (String) ms.get("rollCode");
        if(!YHUtility.isNullorEmpty(rollName)){
          rollCode = YHUtility.encodeSpecial(rollCode);
        }
        sb.append("{");
        sb.append("seqId:\"" + ms.get("seqId") + "\"");
        sb.append(",rollCode:\"" + (ms.get("rollCode") == null ? "" : rollCode) + "\"");
        sb.append(",rollName:\"" + (ms.get("rollName") == null ? "" : rollName) + "\"");
        sb.append("},");
      }
      if(sb.length() > 1){
        sb.deleteCharAt(sb.length() - 1); 
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
   * 查询归档期限的值
   */
  public String getDeadlineRollSelect(HttpServletRequest request,
	      HttpServletResponse response) throws Exception{
	  Connection dbConn = null;
	    try{
	      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
	      dbConn = requestDbConn.getSysDbConn();
	      StringBuffer sb = new StringBuffer("[");
	      
	      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
	      String sql = "";
//	      if (person.isAdminRole()) {
	        sql = "select * from oa_kind_dict_item where CLASS_NO = 'DEADLINE'";
//	      } else {
//	        sql = "select * from code_item where CLASS_NO = 'DEADLINE' AND DEPT_ID = '" + person.getDeptId() + "'";
//	      }
	      PreparedStatement ps = null;
	      ResultSet rs = null;
	      try {
	        ps = dbConn.prepareStatement(sql);
	        rs = ps.executeQuery() ;
	        while (rs.next()) {
	          String classDesc =   YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("CLASS_DESC")));
	          String classCode =  YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("CLASS_CODE")));
	          int seqId = rs.getInt("SEQ_ID");
	          sb.append("{");
	          sb.append("seqId:\"" + seqId + "\"");
	          sb.append(",classCode:\"" + classCode + "\"");
	          sb.append(",classDesc:\"" + classDesc + "\"");
	          sb.append("},");
	        }
	      } catch (Exception e) {
	        throw e;
	      } finally {
	        YHDBUtility.close(ps, rs, null);
	      }
	      if(sb.length() > 1){
	        sb.deleteCharAt(sb.length() - 1); 
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
   * 查询出select中的案卷
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRmsRollSelect2(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      StringBuffer sb = new StringBuffer("[");
      
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      String sql = "";
      if (person.isAdminRole()) {
        sql = "select * from oa_archives_volume where STATUS = '0' order by (case when PRIORITY is null then 99999 else PRIORITY end) ";
      } else {
        sql = "select * from oa_archives_volume where STATUS = '0' AND DEPT_ID = '" + person.getDeptId() + "' order by (case when PRIORITY is null then 99999 else PRIORITY end) ";
      }
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
        ps = dbConn.prepareStatement(sql);
        rs = ps.executeQuery() ;
        while (rs.next()) {
          String rollName =   YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("ROLL_NAME")));
          String rollCode =  YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("ROLL_CODE")));
          int seqId = rs.getInt("SEQ_ID");
          sb.append("{");
          sb.append("seqId:\"" + seqId + "\"");
          sb.append(",rollCode:\"" + rollCode + "\"");
          sb.append(",rollName:\"" + rollName + "\"");
          sb.append("},");
        }
      } catch (Exception e) {
        throw e;
      } finally {
        YHDBUtility.close(ps, rs, null);
      }
      if(sb.length() > 1){
        sb.deleteCharAt(sb.length() - 1); 
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
   * 移卷转至
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String changeRmsRollSelect(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqStr = request.getParameter("seqIds");
      int rollId = Integer.parseInt(request.getParameter("rollId"));
      this.logic.changeRmsRollSelect(dbConn, seqStr, rollId);
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 导出到EXCEL表格中


   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportToExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
    response.setCharacterEncoding(YHConst.CSV_FILE_CODE);
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String seqIdStr = request.getParameter("seqIdStr");
      String fileName = URLEncoder.encode("文件档案.csv","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ArrayList<YHDbRecord > dbL = this.logic.toExportRmsFileData(conn, seqIdStr);
      YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return null;
  }
  
  public String getSearchRmsRoll(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    ArrayList<YHAddress> addressList = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String rollCode = YHDBUtility.escapeLike(request.getParameter("rollCode"));
      String rollName = YHDBUtility.escapeLike(request.getParameter("rollName"));
      String roomId = YHDBUtility.escapeLike(request.getParameter("roomId"));
      String years = YHDBUtility.escapeLike(request.getParameter("years"));
      String beginDate0 = YHDBUtility.escapeLike(request.getParameter("beginDate0"));
      String beginDate1 = YHDBUtility.escapeLike(request.getParameter("beginDate1"));
      String endDate0 = YHDBUtility.escapeLike(request.getParameter("endDate0"));
      String endDate1 = YHDBUtility.escapeLike(request.getParameter("endDate1"));
      String secret = YHDBUtility.escapeLike(request.getParameter("secret"));
      String deadline0 = YHDBUtility.escapeLike(request.getParameter("deadline0"));
      String deadline1 = YHDBUtility.escapeLike(request.getParameter("deadline1"));
      String categoryNo = YHDBUtility.escapeLike(request.getParameter("categoryNo"));
      String catalogNo = YHDBUtility.escapeLike(request.getParameter("catalogNo"));
      String archiveNo = YHDBUtility.escapeLike(request.getParameter("archiveNo"));
      String boxNo = YHDBUtility.escapeLike(request.getParameter("boxNo"));
      String microNo = YHDBUtility.escapeLike(request.getParameter("microNo"));
      String certificateKind = YHDBUtility.escapeLike(request.getParameter("certificateKind"));
      String certificateStart0 = YHDBUtility.escapeLike(request.getParameter("certificateStart0"));
      String certificateStart1 = YHDBUtility.escapeLike(request.getParameter("certificateStart1"));
      String certificateEnd0 = YHDBUtility.escapeLike(request.getParameter("certificateEnd0"));
      String certificateEnd1 = YHDBUtility.escapeLike(request.getParameter("certificateEnd1"));
      String rollPage0 = YHDBUtility.escapeLike(request.getParameter("rollPage0"));
      String rollPage1 = YHDBUtility.escapeLike(request.getParameter("rollPage1"));
      String deptId = YHDBUtility.escapeLike(request.getParameter("deptId"));
      String remark = YHDBUtility.escapeLike(request.getParameter("remark"));
      String data = "";
      data = this.logic.getRmsRollSearchJson(dbConn, request.getParameterMap(), person, rollCode, rollName, roomId, years, beginDate0, beginDate1, endDate0, endDate1,
          secret, deadline0, deadline1, categoryNo, catalogNo, archiveNo, boxNo, microNo, certificateKind, certificateStart0, certificateStart1, rollPage0, rollPage1,
          deptId, remark, certificateEnd0, certificateEnd1);
     
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  
}
