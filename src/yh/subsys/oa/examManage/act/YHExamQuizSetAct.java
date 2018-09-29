package yh.subsys.oa.examManage.act;
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
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.examManage.data.YHExamQuizSet;
import yh.subsys.oa.examManage.logic.YHExamQuizSetLogic;

public class YHExamQuizSetAct {

  public static final String attachmentFolder = "examManage";
  private YHExamQuizSetLogic logic = new YHExamQuizSetLogic();
  
  /**
   * 新建题库--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addBank(HttpServletRequest request,
       HttpServletResponse response) throws Exception{
      
     Connection dbConn = null;
     try {
       YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
       dbConn = requestDbConn.getSysDbConn();
       YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       Map<String,String[]> map = request.getParameterMap();
       YHExamQuizSet quiz = (YHExamQuizSet) YHFOM.build(map, YHExamQuizSet.class, "");
       this.logic.addBank(dbConn, quiz);
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
   * 获取题库列表--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getExamQuizSetList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = this.logic.getExamQuizSetList(dbConn, request.getParameterMap());
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
   * 删除题库--cc
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
  
  /**
   * 获取题库详情(修改题库)--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getExamQuizSetDetail(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      if(YHUtility.isNullorEmpty(seqId)){
        seqId = "0";
      }
      YHExamQuizSet quiz = (YHExamQuizSet)this.logic.getExamQuizSetDetail(dbConn, Integer.parseInt(seqId));
      if (quiz == null){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
        request.setAttribute(YHActionKeys.RET_MSRG, "该试卷不存在");
        return "/core/inc/rtjson.jsp";
      }
      StringBuffer data = YHFOM.toJson(quiz);
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
   * 修改题库--cc
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateExamQuizSet(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String,String[]> map = request.getParameterMap();
      YHExamQuizSet record = (YHExamQuizSet) YHFOM.build(map, YHExamQuizSet.class, "");
      this.logic.updateExamQuizSet(dbConn, record);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功"); 
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    }
    return "/core/inc/rtjson.jsp";
  }
}
