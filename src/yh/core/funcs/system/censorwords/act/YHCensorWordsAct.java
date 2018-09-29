package yh.core.funcs.system.censorwords.act;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.system.censorwords.data.YHCensorWords;
import yh.core.funcs.system.censorwords.logic.YHCensorWordsLogic;
import yh.core.funcs.system.extuser.logic.YHExtUserLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHCensorWordsAct {
  private static Logger log = Logger.getLogger(YHCensorWordsAct.class);
  public String addSingleWords(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      response.setContentType("text/html;charset=UTF-8");
      request.setCharacterEncoding("UTF-8");
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String find = request.getParameter("find"); 
      String replacement = request.getParameter("replacement");
      if(replacement.equals("") || replacement.equals("null")){
        replacement = "**";
      }
      Map m =new HashMap();
      m.put("userId", seqId);
      m.put("find", find);
      m.put("replacement", replacement);
      YHORM t = new YHORM();
      YHCensorWordsLogic cwLogic = new YHCensorWordsLogic();
      if(cwLogic.existsCensorWords(dbConn, find)){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "错误  词语"+find+"已存在，请重新填写！");
        return "/core/inc/rtjson.jsp";
      }else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
      }
      t.saveSingle(dbConn, "censorWords", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"添加成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateSingleWords(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic orgLogic = new YHCensorWordsLogic();
      String find = request.getParameter("find");
      String replacement = request.getParameter("replacement");
    
      YHORM t = new YHORM();
      Map m =new HashMap();
      m.put("find", find);
      m.put("replacement", replacement);
      orgLogic.updateSingleWords(dbConn, find, replacement);
      //t.updateSingle(dbConn, "censorWords", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateMore1Words(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic cwLogic = new YHCensorWordsLogic();
      String find = request.getParameter("find");
      String replacement = request.getParameter("replacement");
      YHORM t = new YHORM();
      Map m =new HashMap();
      m.put("find", find);
      m.put("replacement", replacement);
      m.put("userId", userId);
      if(cwLogic.existsCensorWords(dbConn, find)){
        cwLogic.updateSingleWords(dbConn, find, replacement);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        //request.setAttribute(YHActionKeys.RET_MSRG, "错误  词语"+find+"以存在，请重新填写！");
        //return "/core/inc/rtjson.jsp";
      }else{
        t.saveSingle(dbConn, "censorWords", m);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
      }
      //t.updateSingle(dbConn, "censorWords", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateMore99Words(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
     
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic cwLogic = new YHCensorWordsLogic();
      String find = request.getParameter("find");
      String replacement = request.getParameter("replacement");
      List<Map> list = new ArrayList();
      YHORM t = new YHORM();
      Map m =new HashMap();
      m.put("find", find);
      m.put("replacement", replacement);
      
      String censorVal = request.getParameter("censorVal");
      //System.out.println(censorVal+"XXXXXXXXXXXXXXXXXXXXx");
      //String censorSum[] = censorVal.split(",");
     // String findStr = "";
     // for(int x = 0; x < censorSum.length; x++){
      //  findStr = censorSum[x];
     // }
      if(cwLogic.existsCensorWords(dbConn, find)){
        //request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        //return "/core/funcs/system/censorwords/new/import.jsp?find="+find+"&replacement="+replacement;
        //System.out.println(find+"PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
        
        YHORM orm = new YHORM();
        HashMap map = null;
        StringBuffer sb = new StringBuffer("[");
        String[] filters = new String[]{"FIND="+find};
        List funcList = new ArrayList();
        funcList.add("censorWords");
        map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
        list.addAll((List<Map>) map.get("OA_EXAMINE_WORDS"));
        if(list.size() > 1){
          for(Map ms : list){
            sb.append("{");
            sb.append("find:\"" + ms.get("find") + "\"");
            sb.append(",replacement:\"" + ms.get("replacement") + "\"");
            sb.append("},");
          }
          sb.deleteCharAt(sb.length() - 1); 
        }else{
          for(Map ms : list){
            sb.append("{");
            sb.append("find:\"" + ms.get("find") + "\"");
            sb.append(",replacement:\"" + ms.get("replacement") + "\"");
            sb.append("}");
          }
        }    
        sb.append("]");
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
        request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
        //return "/core/inc/rtjson.jsp";
      }else{
        t.saveSingle(dbConn, "censorWords", m);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功");
      }
      //t.updateSingle(dbConn, "censorWords", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/system/censorwords/new/import.jsp";
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateMore0Words(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic cwLogic = new YHCensorWordsLogic();
      String find = request.getParameter("find");
      String replacement = request.getParameter("replacement");
      List<Map> list = new ArrayList();
      String findStrs = "";
      String replacementStrs = "";
      String sumStr = "";
      YHORM t = new YHORM();
      Map m =new HashMap();
      StringBuffer sb = new StringBuffer("[");
      String censorVal = request.getParameter("find");
      //System.out.println(censorVal+"XXXXXXXXXXXXXXXXXXXXx");
      YHORM orm = new YHORM();
      HashMap map = null;
      String censorSum[] = censorVal.split(",");
      String findStr = "";
      String finds = "";
      int okCount = 0;
      int errCount = 0;
      String replacements = "";
      for(int x = 0; x < censorSum.length; x++){
        findStr = censorSum[x];
        if(censorSum[x].indexOf("=")!=-1){
          finds = censorSum[x].substring(0,censorSum[x].indexOf("="));
          replacements = censorSum[x].substring(censorSum[x].indexOf("=")+1, censorSum[x].length());
        }else{
          finds = findStr;
          replacements = "";
        }
        if(cwLogic.existsCensorWords(dbConn, finds)){
          errCount++;
          findStrs += finds + ",";
          replacementStrs += replacements + ",";
          if(replacements!=""){
            sumStr += finds+"="+replacements+",";
          }else{
            sumStr += finds;
          }
          continue;
        }else{
          okCount++;
          m.put("find", finds);
          m.put("replacement", replacements);
          m.put("userId", userId);
          t.saveSingle(dbConn, "censorWords", m);
        }
      }
      //System.out.println(sumStr+"YYYYYYYYYYYYY");
      String str[] = sumStr.split(",");
      String reStr = "";
      for(int i = 0; i < str.length; i++){
        reStr = str[i];
        sb.append("{");
        sb.append("find:\"" + reStr + "\"");
        sb.append(",errCount:\"" + errCount + "\"");
        sb.append(",okCount:\"" + okCount + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1); 
      sb.append("]");
      //System.out.println(sb);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/funcs/system/censorwords/new/import.jsp";
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateMore2Words(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic orgLogic = new YHCensorWordsLogic();
      String find = request.getParameter("find");
      String replacement = request.getParameter("replacement");
    
      YHORM t = new YHORM();
      Map m =new HashMap();
      m.put("find", find);
      m.put("userId", userId);
      m.put("replacement", replacement);
      //orgLogic.deleteAllWords(dbConn);
      t.saveSingle(dbConn, "censorWords", m);
      //orgLogic.updateSingleWords(dbConn, find, replacement);
      //t.updateSingle(dbConn, "censorWords", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteMore2Words(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic orgLogic = new YHCensorWordsLogic();
      orgLogic.deleteAllWords(dbConn);
      //orgLogic.updateSingleWords(dbConn, find, replacement);
      //t.updateSingle(dbConn, "censorWords", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"词语批量添加成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getCensorWords(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      userName = person.getUserName();
      YHCensorWordsLogic extLogic = new YHCensorWordsLogic();
      ArrayList<YHCensorWords> wordList = new ArrayList<YHCensorWords>();
      wordList = extLogic.getCensorWords(dbConn);
      request.setAttribute("wordList", wordList);
      request.setAttribute("userName", userName);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/inc/rtjson.jsp";
    return "/core/funcs/system/censorwords/manage/index.jsp";
  }
  
  public String getCensorWordsSearch(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String userName = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = person.getSeqId();
      String find = request.getParameter("find");
      if(!YHUtility.isNullorEmpty(find)){
        find = YHDBUtility.escapeLike(find);
      }
      String replacement = request.getParameter("replacement");
      if(!YHUtility.isNullorEmpty(replacement)){
        replacement = YHDBUtility.escapeLike(replacement);
      }
      
      userName = person.getUserName();
      YHCensorWordsLogic extLogic = new YHCensorWordsLogic();
      ArrayList<YHCensorWords> wordList = new ArrayList<YHCensorWords>();
      wordList = extLogic.getCensorWordsSearch(dbConn, seqId, find, replacement);
      request.setAttribute("wordList", wordList);
      request.setAttribute("userName", userName);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/inc/rtjson.jsp";
    return "/core/funcs/system/censorwords/query/search.jsp";
  }
  
  public String getCount(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHExtUserLogic extLogic = new YHExtUserLogic();
      long sum = 0;
      sum = extLogic.existsCount(dbConn, 0);
      StringBuffer sb = new StringBuffer("[");
      sb.append("{");
      sb.append("sum:\"" + sum + "\"");
      sb.append("}");
      sb.append("]");
      request.setAttribute("extListSum", sum);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
    //return "/core/funcs/system/extuser/manage.jsp";
  }
  
  public String getCensorWordsId(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"SEQ_ID="+seqId};
      List funcList = new ArrayList();
      funcList.add("censorWords");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      List<Map> list = (List<Map>) map.get("OA_EXAMINE_WORDS");
      for(Map m : list) {
        String find = (String) m.get("find");
        if(!YHUtility.isNullorEmpty(find)){
          find = find.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
        }
        String replacement = (String) m.get("replacement");
        if(!YHUtility.isNullorEmpty(replacement)){
          replacement = replacement.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "");
        }
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",userId:\"" + m.get("userId") + "\"");
        sb.append(",find:\"" + find + "\"");
        sb.append(",replacement:\"" + replacement + "\"");
        sb.append("}");
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
  
  public String updateCensorWords(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic orgLogic = new YHCensorWordsLogic();
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      String find = request.getParameter("find");
      String replacement = request.getParameter("replacement");
    
      YHORM t = new YHORM();
      Map m =new HashMap();
      m.put("seqId", seqId);
      m.put("userId", userId);
      m.put("find", find);
      m.put("replacement", replacement);
      t.updateSingle(dbConn, "censorWords", m);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"工作日志设置已修改");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteCensorWords(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("idStrs");
      YHORM t = new YHORM();
      Map m =new HashMap();
      m.put("seqId", seqId);
      //t.deleteSingle(dbConn, "extUser", m);
      YHCensorWordsLogic wordsLogic = new YHCensorWordsLogic();
      wordsLogic.deleteAll(dbConn, seqId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String deleteSearchWords(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    int c = 0;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      String find = request.getParameter("find");
      String replacement = request.getParameter("replacement");
      
      YHCensorWordsLogic extLogic = new YHCensorWordsLogic();
      ArrayList<YHCensorWords> wordList = new ArrayList<YHCensorWords>();
      wordList = extLogic.getCensorWords(dbConn);
      int a = wordList.size();
      YHCensorWordsLogic wordsLogic = new YHCensorWordsLogic();
      wordsLogic.deleteSearch(dbConn, userId, find, replacement);
      
      YHCensorWordsLogic extLogicEnd = new YHCensorWordsLogic();
      ArrayList<YHCensorWords> wordListEnd = new ArrayList<YHCensorWords>();
      wordListEnd = extLogicEnd.getCensorWords(dbConn);
      int b = wordListEnd.size();
      c = a - b;
      //request.setAttribute("wordList", wordList);
      //request.setAttribute("wordListEnd", wordListEnd);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功取出数据");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    //return "/core/inc/rtjson.jsp";
    return "/core/funcs/system/censorwords/query/deletequery.jsp?c="+c;
  }
  
  public String deleteAllCensorWords(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = person.getSeqId();
      YHCensorWordsLogic wordsLogic = new YHCensorWordsLogic();
      if(userId == 196){//管理员seqId
        wordsLogic.deleteAllWords(dbConn);
      }else{
        wordsLogic.deleteAllFast(dbConn, userId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除数据");
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
    String data = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String userIdStr = request.getParameter("userId");
      int userId = Integer.parseInt(userIdStr);
      YHPersonLogic dl = new YHPersonLogic();
      data = dl.getUserNameLogic(dbConn, userId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,"\"" + data + "\"");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String exportToTxt(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    PrintWriter pw = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int userId = person.getSeqId();
      String find = request.getParameter("find");
      String replacement = request.getParameter("replacement");
      Calendar cal = Calendar.getInstance();        
      java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");        
      String logTime = sdf.format(cal.getTime()); 
      
      String fileName = URLEncoder.encode("词语过滤"+logTime+".txt","UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      pw = response.getWriter();
      String txtStr = "";
      YHCensorWordsLogic cwl = new YHCensorWordsLogic();
      ArrayList<YHCensorWords> list = cwl.getCensorWordsTxtList(conn, userId, find, replacement);
      if(list.size() == 0){
        txtStr = "";
      }else{
        for(int i = 0; i < list.size(); i++){
          String findStr = list.get(i).getFind();
          String replaceStr = list.get(i).getReplacement();
          txtStr += findStr + "=" + replaceStr + "\r\n";
        }
      }
      pw.write(txtStr);
      pw.flush();
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      pw.close();
    }
    return null;
  }
  
}
