package yh.subsys.inforesouce.act;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;
import yh.subsys.inforesouce.util.YHAjaxUtil;
import yh.subsys.inforesouce.util.YHOutURLUtil;
import yh.subsys.inforesouce.util.YHTempFileUtil;

public class YHOutURLAct{
  YHTempFileUtil fu =  YHTempFileUtil.getInstance();
  /**
   * 返回第一层tag图
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String firstLevelTag(HttpServletRequest request, HttpServletResponse response) throws Exception{   
    String basePath = YHSysProps.getString("signFileServiceUrl");
    String url = basePath + "/TitleSign/GetFirstLevelTagList";
    String content = null; 
      try{
        content = YHOutURLUtil.getContent(url);       
        YHAjaxUtil.ajax(content, response);        
      } catch (Exception e){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
        throw e;      
      }  
    
    return null;
  }
  /**
   * 返回指定主题词的文档列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String articleListByKeyID(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String KeyID = request.getParameter("KeyID"); 
    int Page=0;
    int PageSize=8;
    String basePath = YHSysProps.getString("signFileServiceUrl");
    String url = basePath + "/TitleSign/GetArticleListByKeyID?KeyID="+KeyID+"&nStartPage="+Page+"&nPageSize="+PageSize;
    String keyContent = null;  
    
      try{
        keyContent = YHOutURLUtil.getContent(url); 
        YHAjaxUtil.ajax(keyContent, response);
      }catch(Exception e){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
        throw e;
      }
    
    return null;
  }
  /**
   * 显示tag 云图的层次关系
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String Keyword(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String keyID = request.getParameter("keyID");     
    String basePath = YHSysProps.getString("signFileServiceUrl");
    if(!YHUtility.isNullorEmpty(keyID)){
      keyID = keyID.substring(0, keyID.lastIndexOf(",")==-1?keyID.length():keyID.lastIndexOf(","));
    }
    String url = basePath + "/TitleSign/GetKeyword?KeyIDs="+keyID;
    String keyContent = null;  
      try{
        keyContent = YHOutURLUtil.getContent(url); 
        YHAjaxUtil.ajax(keyContent, response);
      }catch(Exception e){
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
        throw e;
      }
    
    return null;
  }
 
  
  /**
   * 回与指定主题词相关的文档列表(多个主题词KeyID之间用逗号隔开) 
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String  getArticleListByKeyIDs(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String keyID = request.getParameter("KeyIDs"); 
    String useRealData = YHSysProps.getString("useSignFileService");
    String startPage = request.getParameter("nStartPage");
    String pageSize =  request.getParameter("nPageSize");
    if(YHUtility.isNullorEmpty(pageSize)){
      pageSize = "8";
    }
    if(YHUtility.isNullorEmpty(startPage)){
      startPage = "0";
    }
    String basePath = YHSysProps.getString("signFileServiceUrl");
    String url = basePath + "/TitleSign/GetArticleListByKeyIDs?KeyIDs="+keyID+"&nStartPage="+startPage + "&nPageSize="+pageSize;
    String files = null;
  
        try{
         // String files = null;     
          files = YHOutURLUtil.getContent(url);       
          YHAjaxUtil.ajax(files, response);
        } catch (Exception e){
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
          throw e;
        }
    
    return null;
  }
  
  /**
   * 返回指定文档的相关文档列表 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRelationArticleList(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String fileId = request.getParameter("fileId");
    String basePath = YHSysProps.getString("signFileServiceUrl");
    String url = basePath + "/TitleSign/GetRelationArticleList?FILE_ID=" + fileId;
    String files = null;
 
        try{
          files = YHOutURLUtil.getContent(url);
          YHAjaxUtil.ajax(files, response);
        } catch (Exception e){
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
          throw e;
        }
    
    return null;
  }
  
  /**
   * 返回指定文档的主题词列表 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String  getArticleTags(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String fileId = request.getParameter("fileId");
    String basePath = YHSysProps.getString("signFileServiceUrl");
    String url = basePath + "/TitleSign/GetArticleTags?FILE_ID=" + fileId;
    try{
      String titles = null;
        titles = YHOutURLUtil.getContent(url);
      YHAjaxUtil.ajax(titles, response);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return null;   
  }
  
  /**
   * 返回全文检索结果列表
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFullTextDocList(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String startPage = request.getParameter("nStartPage");
    String pageSize =  request.getParameter("nPageSize");
    String q =  request.getParameter("q");
     q=YHUtility.decodeURL(q);
    if(YHUtility.isNullorEmpty(pageSize)){
      pageSize = "8";
    }
    if(YHUtility.isNullorEmpty(startPage)){
      startPage = "0";
    }
    String basePath = YHSysProps.getString("signFileServiceUrl");
    String url = basePath + "/FullText/GetFullTextDocList?q="+ URLEncoder.encode(q, "UTF-8") + "&nStartPage="+startPage+"&nPageSize="+pageSize;
    try{
      long start = System.currentTimeMillis();
      String files = YHOutURLUtil.getContent(url);
      YHAjaxUtil.ajax(files, response);
      long end = System.currentTimeMillis();
      //YHOut.println("调用getFullTextDocList全文检索用时："+(end-start)+" ms");
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }  
    return null;
  }
  public String getRelationWords(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String q =  request.getParameter("q");
    String start = request.getParameter("start");
    String end = request.getParameter("end");
    String type = request.getParameter("type");
    if (YHUtility.isNullorEmpty(type)) {
      type = "0";
    }
    q=YHUtility.decodeURL(q);
    String basePath = YHSysProps.getString("signFileServiceUrl");
    String url = basePath + "/FullText/GetRelationWords?q="+ URLEncoder.encode(q, "UTF-8") +"&type=" + type + "&start=" + start + "&end=" + end;
    try{
      String files = YHOutURLUtil.getContent(url);
      YHAjaxUtil.ajax(files, response);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }  
    return null;
  }
  public String getFullText(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String q = request.getParameter("q");
    String limit =  request.getParameter("limit");
    q=YHUtility.decodeURL(q);
    String basePath = YHSysProps.getString("signFileServiceUrl");
    
    String url = basePath + "/FullText/GetFullTextSuggest?q="+ URLEncoder.encode(q, "UTF-8") + "&limit="+limit;
    try{
      String files = YHOutURLUtil.getContent(url);
      YHAjaxUtil.ajax(files, response);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }  
    return null;
  }
  public String getColChartData(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String q = request.getParameter("q");
    String type =  request.getParameter("type");
    q=YHUtility.decodeURL(q);
    String basePath = YHSysProps.getString("signFileServiceUrl");
    
    String url = basePath + "/FullText/GetFullTextSuggest?q="+ URLEncoder.encode(q, "UTF-8") ;
    try{
      String files = YHOutURLUtil.getContent(url);
      YHAjaxUtil.ajax(files, response);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }  
    return null;
  }
  public String getLineChartData(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String q = request.getParameter("q");
    q=YHUtility.decodeURL(q);
    String basePath = YHSysProps.getString("signFileServiceUrl");
    
    String url = basePath + "/FullText/GetFullTextSuggest?q="+ URLEncoder.encode(q, "UTF-8") ;
    try{
      String files = YHOutURLUtil.getContent(url);
      YHAjaxUtil.ajax(files, response);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }  
    return null;
  }
  public String getDoc(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String fileId = request.getParameter("fileId");
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String query = "select FILE_PATH from oa_seal_attach WHERE FILE_ID = '"+fileId+"'";
      Statement stm = null;
      ResultSet rs = null;
      String filePath = "";
      try {
        stm = dbConn.createStatement();
        rs = stm.executeQuery(query);
        if (rs.next()) {
          filePath = rs.getString("FILE_PATH");
          filePath = filePath.replace("\\", "/");
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null);
      }
      String name = YHFileUtility.getFileNameNoExt(filePath);
      request.setAttribute(YHActionKeys.RET_DATA, "'"+filePath+"'");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, name);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 返回人名，地名，组织机构名, 关键词
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String titleSignFile(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String fileId = request.getParameter("attachmentId");
    String basePath = YHSysProps.getString("signFileServiceUrl");
    String url = basePath + "/TitleSign/TitleSignFile?FILE_ID=" + fileId;
    try{
      long start = System.currentTimeMillis();
      String title = YHOutURLUtil.getContent(url);
      YHAjaxUtil.ajax(title, response);
      long end = System.currentTimeMillis();
      //YHOut.println("返回人名，地名，组织机构名, 关键词 titleSignFile用时：" + (end-start) +" ms");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  /**
   * 返回热点人物
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings("deprecation")
  public String  getHotPersonOfMonth(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String yearMonth = request.getParameter("yearmonth");
    String nmax = request.getParameter("nMax");
    if(YHUtility.isNullorEmpty(yearMonth)){
      Date dat = new Date();
      int year = dat.getYear() + 1900;
      int month = dat.getMonth() + 1;
      if(month < 10){
        yearMonth = year+"0"+month;
      }else{
        yearMonth = year+""+month;
      }
    }
    if(YHUtility.isNullorEmpty(nmax)){
      nmax = "6";
    }
    String baseUrl = YHSysProps.getString("signFileServiceUrl");
    String url = baseUrl + "/TagIt/GetHotPersonOfMonth?YearMonth=" + yearMonth + "&nMax=" + nmax;
    try{
      String title = YHOutURLUtil.getContent(url);
      YHAjaxUtil.ajax(title, response);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  /**
   * 返回热点地区
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String  getHotAddressOfMonth(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String yearMonth = request.getParameter("yearmonth");
    String nmax = request.getParameter("nMax");
    if(YHUtility.isNullorEmpty(yearMonth)){
      Date dat = new Date();
      int year = dat.getYear() + 1900;
      int month = dat.getMonth() + 1;
      if(month < 10){
        yearMonth = year+"0"+month;
      }else{
        yearMonth = year+""+month;
      }
    }
    if(YHUtility.isNullorEmpty(nmax)){
      nmax = "6";
    }
    String baseUrl = YHSysProps.getString("signFileServiceUrl");
    String url = baseUrl + "/TagIt/GetHotAddressOfMonth?YearMonth=" + yearMonth + "&nMax=" + nmax;
    try{
      String title = YHOutURLUtil.getContent(url);
      YHAjaxUtil.ajax(title, response);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 返回热点组织机构
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String  getHotOrganizationOfMonth(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String yearMonth = request.getParameter("yearmonth");
    String nmax = request.getParameter("nMax");
    if(YHUtility.isNullorEmpty(yearMonth)){
      Date dat = new Date();
      int year = dat.getYear() + 1900;
      int month = dat.getMonth() + 1;
      if(month < 10){
        yearMonth = year+"0"+month;
      }else{
        yearMonth = year+""+month;
      }
    }
    if(YHUtility.isNullorEmpty(nmax)){
      nmax = "6";
    }
    String baseUrl = YHSysProps.getString("signFileServiceUrl");
    String url = baseUrl + "/TagIt/GetHotOrganizationOfMonth?YearMonth=" + yearMonth + "&nMax=" + nmax;
    try{
      String title = YHOutURLUtil.getContent(url);
      YHAjaxUtil.ajax(title, response);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 返回热点组织主题词
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String  getHotKeywordOfMonth(HttpServletRequest request, HttpServletResponse response) throws Exception{
    String yearMonth = request.getParameter("yearmonth");
    String nmax = request.getParameter("nMax");
    if(YHUtility.isNullorEmpty(yearMonth)){
      Date dat = new Date();
      int year = dat.getYear() + 1900;
      int month = dat.getMonth() + 1;
      if(month < 10){
        yearMonth = year+"0"+month;
      }else{
        yearMonth = year+""+month;
      }
    }
    if(YHUtility.isNullorEmpty(nmax)){
      nmax = "6";
    }
    String baseUrl = YHSysProps.getString("signFileServiceUrl");
    String url = baseUrl + "/TagIt/GetHotKeywordOfMonth?YearMonth=" + yearMonth + "&nMax=" + nmax;
    try{
      String title = YHOutURLUtil.getContent(url);
      YHAjaxUtil.ajax(title, response);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  public String personTag(HttpServletRequest request, HttpServletResponse response)throws Exception{
    String dataStr = "{'name':['张三', '李四'], 'address':['北京','南京'], 'org':[]}";
    YHAjaxUtil.ajax(dataStr, response);
    return null;
  }
}
