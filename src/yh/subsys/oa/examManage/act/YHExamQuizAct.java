package yh.subsys.oa.examManage.act;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.examManage.data.YHExamQuiz;
import yh.subsys.oa.examManage.logic.YHExamQuizLogic;

public class YHExamQuizAct {

  public static final String attachmentFolder = "examManage";
  private YHExamQuizLogic logic = new YHExamQuizLogic();

  /**
   * 新建试题
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addQuiz(HttpServletRequest request,
      HttpServletResponse response) throws Exception{

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map<String,String[]> map = request.getParameterMap();
      String answers = request.getParameter("answers");
      YHExamQuiz quiz = (YHExamQuiz) YHFOM.build(map, YHExamQuiz.class, "");
      quiz.setAnswers(answers.toUpperCase());
      this.logic.addQuiz(dbConn, quiz);
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
   * 得到试题BySeqId ---syl
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getQuizById(HttpServletRequest request,
      HttpServletResponse response) throws Exception{

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String data = "";
      YHExamQuizLogic quizLogic = new  YHExamQuizLogic();
      if(YHUtility.isInteger(seqId)){
        YHExamQuiz quiz = quizLogic.selectQuizById(dbConn, Integer.parseInt(seqId));
        if(quiz != null){
          data = YHFOM.toJson(quiz).toString();
        }
      }
      if(data.equals("")){
        data = "{}";
      }
      request.setAttribute(YHActionKeys.RET_DATA, data); 
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
   *查询所有(分页)通用列表显示数据--lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String selectQuiz(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String roomId = request.getParameter("roomId");
      String data = YHExamQuizLogic.selectQuiz(dbConn,request.getParameterMap(),roomId);
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
   *删除--lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteQuiz(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      if (!YHUtility.isNullorEmpty(seqId)) {
        YHExamQuizLogic.deleteQuiz(dbConn,seqId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *修改--lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateQuiz(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHExamQuiz quiz = (YHExamQuiz)YHFOM.build(request.getParameterMap());
      YHExamQuizLogic.updateQuiz(dbConn, quiz);//修改数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   *查询--lz
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String showQuiz(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      //定义数组将数据保存到Json中
      String data = "";
      if (!YHUtility.isNullorEmpty(seqId)) {
        YHExamQuiz flow = (YHExamQuiz)YHExamQuizLogic.showQuiz(dbConn, seqId);
        if(flow != null) {
          data = data + YHFOM.toJson(flow);
          data = data.replaceAll("\\n", "");
          data = data.replaceAll("\\r", "");
        }
        data = data + "";
        if(data.equals("")){
          data = "{}";
        }
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
}
