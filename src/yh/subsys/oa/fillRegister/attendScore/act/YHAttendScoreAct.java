package yh.subsys.oa.fillRegister.attendScore.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.fillRegister.attendScore.data.YHAttendScore;
import yh.subsys.oa.fillRegister.attendScore.logic.YHAttendScoreLogic;


public class YHAttendScoreAct {
  public static final String attachmentFolder = "attendScore";
  private YHAttendScoreLogic logic = new YHAttendScoreLogic();
  
  public String addRecord(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
     
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      Map<String,String[]> map = request.getParameterMap();
      YHAttendScore record = (YHAttendScore) YHFOM.build(map, YHAttendScore.class, "");
      String stUserId = record.getAssessingOfficer();
      String[] staffUserIdStr = stUserId.split(",");
      for(int i = 0; i < staffUserIdStr.length; i++){
//        record.setCreateUserId(String.valueOf(person.getSeqId()));
//        record.setCreateDeptId(person.getDeptId());
//        record.setAbroadUserId(staffUserIdStr[i]);
        this.logic.add(dbConn, record);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
}
