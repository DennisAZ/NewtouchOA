package yh.core.funcs.specialflag.act;


import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.specialflag.data.YHSpecialFlag;
import yh.core.funcs.specialflag.logic.YHSpecialFlagLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHSpecialFlagAct {
  private static Logger log = Logger.getLogger(YHSpecialFlagAct.class);
  
  YHSpecialFlagLogic flagLogic = new YHSpecialFlagLogic();
  
  public String getSpecialFlag(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("utf-8");
    Connection dbConn = null;
    String seqId = request.getParameter("seqId");
    String data = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
  
      YHSpecialFlag flag = null;
      
      YHORM orm = new YHORM();
      flag = (YHSpecialFlag)orm.loadObjSingle(dbConn, YHSpecialFlag.class, Integer.parseInt(seqId));
      if(flag == null) {
        flag = new YHSpecialFlag();
      }
      
      StringBuffer sb = new StringBuffer("[");
      sb.append("{");
      sb.append("flagSort:\"" + flag.getFlagSort() + "\"");
      sb.append(",flagCode:\"" + flag.getFlagCode() + "\"");
      sb.append(",flagDesc:\"" + flag.getFlagDesc() + "\"");
      sb.append("}");
      sb.append("]");
      data = sb.toString();
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getMaxFlagCode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("utf-8");
    Connection dbConn = null;
    String flagCode = request.getParameter("");
    String sort = request.getParameter("sort");
    String data = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int flagCodeMax = 0;
      flagCodeMax = flagLogic.getMaxFlagCode(dbConn, sort);
  
      StringBuffer sb = new StringBuffer("[");
      sb.append("{");
      sb.append("code:\"" + flagCodeMax + "\"");
      sb.append("}");
      sb.append("]");
      data = sb.toString();
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);

    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteSpecialFlag(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");   
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      
      Map map = new HashMap();
      map.put("seqId", seqId);
      
      YHORM orm = new YHORM();
      orm.deleteSingle(dbConn, "specialFlag", map);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除标记");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String addSpecialFlag(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String flagCode = null;
    String flagDesc = null;
    String flagSort = null;
    int num = 0;
    String data = null;
    try {
      YHRequestDbConn requestDbConn 
                      = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      dbConn = requestDbConn.getSysDbConn();
      
      YHSpecialFlag flag = (YHSpecialFlag)YHFOM.build(request.getParameterMap());
      
      flagCode = flag.getFlagCode();
      if(flagCode == null || "".equals(flagCode)) {
        return "/core/inc/rtjson.jsp";
      }
      
      flagDesc = flag.getFlagDesc();
      if(flagDesc == null || "".equals(flagDesc)) {
        return "/core/inc/rtjson.jsp";
      }
      
      flagSort = flag.getFlagSort();
      num = flagLogic.selectFlag(dbConn, flagCode, flagSort);
      if(num >= 1) {       
        StringBuffer sb = new StringBuffer("[");
        sb.append("{");
        sb.append("num:\"" + num + "\"");
        sb.append("}");
        sb.append("]");
        data = sb.toString();
        
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "标记编号重复, 标记编号不能重复");
        request.setAttribute(YHActionKeys.RET_DATA, data);
        return "/core/inc/rtjson.jsp";
      }else if(num == 0) {
        StringBuffer sb = new StringBuffer("[");
        sb.append("{");
        sb.append("num:\"" + num + "\"");
        sb.append("}");
        sb.append("]");
        data = sb.toString();
        
        YHORM orm = new YHORM();
        orm.saveSingle(dbConn, flag);
        
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功添加标记");
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateSpecialFlag(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String flagCodeOld = request.getParameter("flagCodeOld");
    String flagSort = null;
    String flagCode = null;
    String flagDesc = null;
    int num = 0;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      dbConn = requestDbConn.getSysDbConn();
      
      YHSpecialFlag flag = (YHSpecialFlag)YHFOM.build(request.getParameterMap());
           
      flagCode = flag.getFlagCode();
      if(flagCode == null || "".equals(flagCode)) {
        return "/core/inc/rtjson.jsp";
      }
      
      flagDesc = flag.getFlagDesc();
      if(flagDesc == null || "".equals(flagDesc)) {
        return "/core/inc/rtjson.jsp";
      }
      
      YHORM orm = new YHORM();
      if(flagCodeOld.equals(flagCode)) {
        orm.updateSingle(dbConn, flag);        
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功编辑标记");
        return "/core/inc/rtjson.jsp";
      }
      
      flagSort = flag.getFlagSort();
      num = flagLogic.selectFlag(dbConn, flagCode, flagSort);
      if(num >= 1) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "标记编号重复, 标记编号不能重复");
        return "/core/inc/rtjson.jsp";
      }
      orm.updateSingle(dbConn, flag);

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功编辑标记");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";    
  }
}
