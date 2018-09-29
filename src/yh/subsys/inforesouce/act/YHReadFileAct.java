package yh.subsys.inforesouce.act;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.subsys.inforesouce.util.YHAjaxUtil;
import yh.subsys.inforesouce.util.YHOutURLUtil;

public class YHReadFileAct{
  
  /**
   * 返回第一层tag图
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  
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
    int PageSize=10;
    String basePath = YHSysProps.getString("signFileServiceUrl");
    String url = basePath + "/GetArticleListByKeyID?KeyID="+KeyID+"&nStartPage="+Page+"&nPageSize="+PageSize;
    String keyContent = null;
    try {
      keyContent = YHOutURLUtil.getContent(url);
    }catch(Exception ex) {
      keyContent = "{\"rtState\":0,\"rtData\":[{\"KeyID\":\"2\",\"Keyword\":\"软件环境\",\"nTimes\":\"1\"},{\"KeyID\":\"3\",\"Keyword\":\"目录\",\"nTimes\":\"1\"},{\"KeyID\":\"4\",\"Keyword\":\"入口\",\"nTimes\":\"1\"},{\"KeyID\":\"5\",\"Keyword\":\"标记\",\"nTimes\":\"1\"}]}";
    }
    try{
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
    String url = basePath + "/GetKeyword?KeyIDs="+keyID;
    String keyContent = null;
    try {
      keyContent = YHOutURLUtil.getContent(url);
    }catch(Exception ex) {
      keyContent = "{\"rtState\":0,\"rtData\":[{\"KeyID\":\"2\",\"Keyword\":\"软件环境\",\"nTimes\":\"1\"},{\"KeyID\":\"3\",\"Keyword\":\"目录\",\"nTimes\":\"1\"},{\"KeyID\":\"4\",\"Keyword\":\"入口\",\"nTimes\":\"1\"},{\"KeyID\":\"5\",\"Keyword\":\"标记\",\"nTimes\":\"1\"}]}";
    }
    try{
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
    String keyID = request.getParameter("keyID"); 
    String startPage = request.getParameter("startPage");
    String pageSize =  request.getParameter("pageSize");
    if(YHUtility.isNullorEmpty(pageSize)){
      pageSize = "10";
    }
    if(YHUtility.isNullorEmpty(startPage)){
      startPage = "0";
    }
    String basePath = YHSysProps.getString("signFileServiceUrl");
    String url = basePath + "/GetArticleListByKeyIDs?KeyIDs="+keyID+"&StartPage="+startPage + "&PageSize="+pageSize;
    try{
      String files = null;
      try {
        files = YHOutURLUtil.getContent(url);
      }catch(Exception ex) {
        files = "{\"rtState\":0,\"rtData\":[{\"SEQ_ID\":\"1361\",\"FILE_ID\":\"1007_31ea43ff0a96af6e4c2e09ff182eccb5\",\"FILE_PATH\":\"D:\\project\\yh\\attach\\notify\\1007\\31ea43ff0a96af6e4c2e09ff182eccb5_日志操作的23种类型.doc\"},{\"SEQ_ID\":\"1362\",\"FILE_ID\":\"1007_aa5751168f4bc18f40a1a1a7bccbb2ca\",\"FILE_PATH\":\"D:\\project\\yh\\attach\\notify\\1007\\aa5751168f4bc18f40a1a1a7bccbb2ca_08-系统测试大纲及记录.doc\"},{\"SEQ_ID\":\"1327\",\"FILE_ID\":\"1007_02de62ed89b822db98f17dbe8e852bc8\",\"FILE_PATH\":\"D:\\project\\yh\\attach\\news\\1007\\02de62ed89b822db98f17dbe8e852bc8_08-系统测试大纲及记录.doc\"},{\"SEQ_ID\":\"1322\",\"FILE_ID\":\"1007_17874b1f161148a2f2b6702144ed73a4\",\"FILE_PATH\":\"D:\\project\\yh\\attach\\news\\1007\\17874b1f161148a2f2b6702144ed73a4_YHBUG---QWX.docx\"},{\"SEQ_ID\":\"1324\",\"FILE_ID\":\"1007_7c976394186ca61eb2eeb7a6351bc4ef\",\"FILE_PATH\":\"D:\\project\\yh\\attach\\notify\\1007\\7c976394186ca61eb2eeb7a6351bc4ef_投标书 -技术上.doc\"},{\"SEQ_ID\":\"1328\",\"FILE_ID\":\"1007_c87242f4135758c1b9e23b24ae4049e4\",\"FILE_PATH\":\"D:\\project\\yh\\attach\\news\\1007\\c87242f4135758c1b9e23b24ae4049e4_新闻.doc\"},{\"SEQ_ID\":\"1329\",\"FILE_ID\":\"1007_7aeb9520c960ef283bc9456ab52a0ee5\",\"FILE_PATH\":\"D:\\project\\yh\\attach\\news\\1007\\7aeb9520c960ef283bc9456ab52a0ee5_100705会议纪要.docx\"},{\"SEQ_ID\":\"1330\",\"FILE_ID\":\"1007_9880c06980c0acc58d5d0a9db3b569bc\",\"FILE_PATH\":\"D:\\project\\yh\\attach\\news\\1007\\9880c06980c0acc58d5d0a9db3b569bc_以导入表格.xlsx\"},{\"SEQ_ID\":\"1331\",\"FILE_ID\":\"1007_998b99c2957dbe8bd1628af46be17439\",\"FILE_PATH\":\"D:\\project\\yh\\attach\\news\\1007\\998b99c2957dbe8bd1628af46be17439_信息资源管理.docx\"},{\"SEQ_ID\":\"1333\",\"FILE_ID\":\"1007_0c5c2334ca7e2d6ec39cf5890b41ff15\",\"FILE_PATH\":\"D:\\project\\yh\\attach\\notify\\1007\\0c5c2334ca7e2d6ec39cf5890b41ff15_aa.docx\"}]}";
      }
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
    String url = basePath + "/GetRelationArticleList?FILE_ID=" + fileId;
    try{      
      String files = null;
      try {
        files = YHOutURLUtil.getContent(url);
      }catch(Exception ex) {
        files = "{\"rtState\":0,\"rtData\":[]}";
      }
      YHAjaxUtil.ajax(files, response);
    } catch (Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return null;
  }
  
  public static String getEncoding(String str) {
    String temp = null;
    if (str != null && !str.equals("")) {
      try {
        temp = new String(str.getBytes("utf-8"), "utf-8");
      } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return temp;

  }
  public String  getArticleTags()throws Exception{
    String url = "C:\\File.txt";   
          String strs = "";   
          try {   
               FileReader read = new FileReader(new File(url));   
               StringBuffer sb = new StringBuffer();   
               
               char ch[] = new char[1024];   
               int d = read.read(ch);   
               while(d!=-1){   
                  String str = new String(ch,0,d);   
                  sb.append(str);   
                  d = read.read(ch);   
                }   
             String stringSb = sb.toString();
            }catch (FileNotFoundException e) {   
               e.printStackTrace();   
            } catch (IOException e) {   
              e.printStackTrace();   
            }   
                return strs ;   
            } 
  
  
  public static void main(String a[]) throws Exception   
      {   
    YHReadFileAct util = new YHReadFileAct();   
          String cc = util.getArticleTags(); 
          System.out.println("hahha:::::"+cc);
    }

 
}
