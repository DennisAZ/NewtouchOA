package yh.core.oaknow.act;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.oaknow.data.YHCategoriesType;
import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.logic.YHOAKnowInputLogic;
import yh.core.oaknow.logic.YHOAKnowLogic;
import yh.core.oaknow.util.YHOAToJsonUtil;

/**
 * 知道录入
 * @author qwx110
 *
 */
public class YHOAKnowInputAct{
  private  YHOAKnowLogic oaLogicIndex = new YHOAKnowLogic();
  private YHOAKnowInputLogic inputLogic = new YHOAKnowInputLogic();
  /**
   * 跳转到知道录入
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String oaInput(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
      //YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      dbConn = requestDbConn.getSysDbConn();
      List<YHCategoriesType>  types = oaLogicIndex.findKind(dbConn);    
      request.setAttribute("toJson", YHOAToJsonUtil.toJsonTwo((types)));
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }    
    return "/core/oaknow/panel/oauserinput.jsp";
  }
  /**
   * 知道录入
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String input(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);   
    YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHOAAsk ask = new YHOAAsk();
      String askName = request.getParameter("ask");
      String categorieid = request.getParameter("typeId");
      String content = request.getParameter("content");
      String tab = request.getParameter("tab");    
      String answer = request.getParameter("answer");
      ask.setAsk(askName);
      ask.setCreator(user.getSeqId()+"");
      ask.setAskComment(content);
      ask.setTypeId(Integer.parseInt(categorieid));
      ask.setCreateDate(new Date());
      ask.setReplyKeyWord(tab);
      ask.setAnswer(answer);
      int id = inputLogic.insertNewAsk(dbConn, ask);
      List<YHCategoriesType>  types = oaLogicIndex.findKind(dbConn);    
      request.setAttribute("toJson", YHOAToJsonUtil.toJsonTwo((types)));
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw e;
    }  
    return "/core/oaknow/panel/oauserinput.jsp";
  }
}
