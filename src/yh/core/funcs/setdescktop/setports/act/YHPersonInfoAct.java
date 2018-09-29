package yh.core.funcs.setdescktop.setports.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.setdescktop.setports.logic.YHPersonInfoLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;

public class YHPersonInfoAct {
  
  private YHPersonInfoLogic logic = new YHPersonInfoLogic();
  
  public String getPersonInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      yh.core.funcs.setdescktop.setports.data.YHPerson pi = logic.getPersonInfo(dbConn, user.getSeqId());
      request.setAttribute(YHActionKeys.RET_DATA, YHFOM.toJson(pi).toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功查询桌面属性");
    }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  public String updatePersonInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      
      if(request.getSession().getAttribute("LOGIN_USER") == null){
        return null;
      }
      yh.core.funcs.setdescktop.setports.data.YHPerson pi = (yh.core.funcs.setdescktop.setports.data.YHPerson)YHFOM.build(request.getParameterMap());
      
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      if(user.getSeqId() != pi.getSeqId()){
        return null;
      }
      pi.setMobilNoHidden("on".equals(pi.getMobilNoHidden())?"1":"0");
      
      logic.updatePersonInfo(dbConn, pi);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功修改");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }

}
