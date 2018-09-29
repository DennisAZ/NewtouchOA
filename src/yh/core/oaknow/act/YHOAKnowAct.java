package yh.core.oaknow.act;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.oaknow.data.YHCategoriesType;
import yh.core.oaknow.data.YHOAAsk;
import yh.core.oaknow.data.YHOAKnowUser;
import yh.core.oaknow.logic.YHOAAskQuestionLogic;
import yh.core.oaknow.logic.YHOAKnowLogic;
import yh.core.oaknow.logic.YHOAKnowMyPanelLogic;
import yh.core.oaknow.util.YHAjaxUtil;
import yh.core.oaknow.util.YHCountUtil;
import yh.core.oaknow.util.YHDateFormatUtil;
import yh.core.oaknow.util.YHPageUtil;
import yh.core.oaknow.util.YHStringUtil;
import yh.core.oaknow.util.YHOAToJsonUtil;
import yh.core.util.YHUtility;
/**
 * OA知道首页
 * @author qwx110
 *
 */
public class YHOAKnowAct{
    private  YHOAKnowLogic oaLogic = new YHOAKnowLogic();
    private YHOAAskQuestionLogic aqLogic = new YHOAAskQuestionLogic();
    private YHOAKnowMyPanelLogic panelLogic = new YHOAKnowMyPanelLogic();
    private  YHPageUtil pu = new YHPageUtil();
    private static Logger log = Logger
    .getLogger("yh.core.act.YHOAKnowAct");
    /**
     * OA知道首页
     * @param request
     * @param response
     * @return
     * @throws Exception 
     */
    public String OAKnowIndex(HttpServletRequest request, HttpServletResponse response) throws Exception{
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      try{
        dbConn = requestDbConn.getSysDbConn();
        int count = oaLogic.findRegCount(dbConn);                //注册的用户数
        List<YHOAKnowUser> users = oaLogic.findJiFenBang(dbConn);//注册用户的列表  
        List<YHCategoriesType>  types = oaLogic.findKind(dbConn);//问题的分类        
        int hadResolvedCont = oaLogic.hadResolved(dbConn, 1);    //已经解决的问题
        int hadNoResolvedCont = oaLogic.hadResolved(dbConn, 0);  //待解决的问题
        //YHOut.println(hadNoResolvedCont+"------------------------------");
        List<YHOAAsk> askList = oaLogic.findGoodAnswer(dbConn);  //精彩问题推荐        
        List<YHOAAsk> noResolvedList = oaLogic.findNoResolvedAsk(dbConn);//没有解决的问题
        //YHOut.println(noResolvedList.size()+"------------------------------");
        List<YHOAAsk> resolvedList = oaLogic.findResolvedAsk(dbConn);  //最近解决的问题       
        YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");//获得登陆用户
        List<YHOAAsk> myAsk = oaLogic.findMyAsk(dbConn, user.getSeqId()+"");//我的问题
       // YHOut.println(myAsk.toString());
        String oaName = panelLogic.findOAName(dbConn).trim();        
        request.setAttribute("oaName", oaName);  
        int clock = YHCountUtil.getInstance().readCount(request);
        request.setAttribute("clock", clock);    
        request.setAttribute("types", types);
        request.setAttribute("count", count);
        request.setAttribute("users", users);
        request.setAttribute("hadResolvedCont", hadResolvedCont);
        request.setAttribute("hadNoResolvedCont", hadNoResolvedCont);
        request.setAttribute("askList", askList);
        request.setAttribute("noResolvedList", noResolvedList);
        request.setAttribute("resolvedList", resolvedList);
        request.setAttribute("myAsk", myAsk);
      } catch (Exception e){ 
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
        request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
        throw e;
      }
      return "/core/oaknow/oaknowindex.jsp";
    }
  
    /**
     * goonask.jsp
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @throws SQLException
     */
    public String askQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception, SQLException{
      String question = request.getParameter("questions");
      YHOAAsk  ask = new YHOAAsk();
      ask.setAsk(question);
      ask.setReplyKeyWord(question);
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);     
      try{
        dbConn = requestDbConn.getSysDbConn();
        List<YHOAAsk> asksList = oaLogic.referenceQuestion(dbConn, question);
        List<YHCategoriesType>  types = oaLogic.findKind(dbConn);
        String jsonString = YHOAToJsonUtil.toJsonTwo(types);
        String oaName = panelLogic.findOAName(dbConn).trim();        
        request.setAttribute("oaName", oaName);
        request.setAttribute("ask", ask);
        request.setAttribute("asktitle", YHStringUtil.toChange(ask.getAsk()));
        request.setAttribute("askkey", YHStringUtil.toChange(ask.getAsk()));
        request.setAttribute("asksList", asksList);
        request.setAttribute("jsonString", jsonString);
      }catch(Exception e){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
        request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
        throw e; 
      }
      return "/core/oaknow/goonask.jsp";
    }
    
    /**
     * 保存新问题
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @throws SQLException
     */
    public String saveAsk(HttpServletRequest request, HttpServletResponse response)throws Exception, SQLException{
      try{
        Connection dbConn = null;
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
        dbConn = requestDbConn.getSysDbConn();
        YHOAAsk ask = new YHOAAsk();
        YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
        int typeId = -1;
        String title = request.getParameter("title");//题目
        String keyword = request.getParameter("keyword");//标签
        String tid = request.getParameter("typeId");
        if(tid =="" || tid == null){
          typeId = -1;
        }else{
          typeId = Integer.parseInt(tid);
        }
        String content = request.getParameter("content");//内容
        ask.setCreator(user.getSeqId()+"");
        ask.setAsk(title);
        ask.setReplyKeyWord(keyword);
        ask.setTypeId(typeId);
        ask.setAskComment(content);
        ask.setCreateDateStr(YHDateFormatUtil.dateFormat(new Date()));
        ask.setCreateDate(new Date());
        int id = oaLogic.saveAsk(dbConn, ask);
        YHAjaxUtil.ajax(id, response);
      } catch (Exception e){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
        throw e; 
      }      
      return null;
    }
    /**
     * 提问分页
     * @return
     * @throws Exception 
     */
    public String askQuestionByPage(HttpServletRequest request, HttpServletResponse response) throws Exception{
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      try{
        dbConn = requestDbConn.getSysDbConn();
        String askName = request.getParameter("title").trim();//题目
        String currNo = request.getParameter("currNo");//当前页
        if(YHStringUtil.isEmpty(currNo)){
          currNo = "1";
        }
        //askName = URLDecoder.decode(askName,"UTF-8");
        int total = aqLogic.findAsksCount(dbConn, askName);   //总的元素数
        pu.setPageSize(10);
        pu.setElementsCount(total);
        pu.setCurrentPage(Integer.parseInt(currNo));
        String asksJson = aqLogic.findAsks(dbConn, askName, pu);
        String newJson = toJson(asksJson, pu);
        YHAjaxUtil.ajax(newJson, response);
      }  catch (Exception e){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
        throw e; 
      }
      return null;
    }
    /**
     * 工具方法
     * @param pages  页面上显示的记录
     * @param pageNo 分页
     * @return
     */
    public String toJson(String pages, YHPageUtil pu){
      StringBuffer sb = new StringBuffer();
      sb.append("{").append("page:").append(pages).append(",");
      sb.append("currNo:").append(pu.getCurrentPage()).append(",");
      sb.append("totalNo:").append(pu.getPagesCount());
      sb.append("}");
      //YHOut.println(sb.toString());
      return sb.toString();
    }
    
    public String ajaxOaDesk(HttpServletRequest request, HttpServletResponse response)throws Exception{
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);    
      try{
        dbConn = requestDbConn.getSysDbConn();
        String asks = oaLogic.oaDesk(dbConn);
        YHAjaxUtil.ajax(asks, response);
      }catch(Exception e){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
        throw e; 
      }
      return null;
    }
}
