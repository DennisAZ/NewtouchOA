package yh.core.funcs.wizardtool.act;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.funcs.wizardtool.logic.YHHtmlParser;
import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.util.file.YHFileConst;
import yh.core.util.file.YHFileUtility;

public class YHWizardToolAct {
  private static Logger log = Logger.getLogger(YHWizardToolAct.class);
  
  public String getFolder(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try{
      List<File> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      File file = new File("D:\\workspace");
      this.loadFile2Json(file, "0", sb);
      this.loadFiles2List(file , sb);
      sb.deleteCharAt(sb.length()-1);
      sb.append("]");
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "数据返回成功");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try{
      String path = request.getParameter("path");
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "数据返回成功");
      request.setAttribute(YHActionKeys.RET_DATA, YHFileUtility.loadLine2Buff(path, 0).toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public void loadFiles2List(File dir,StringBuffer sb) throws Exception{
    File[] fs = dir.listFiles();
    String parentId = dir.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\");
    
    for(int i=0; i<fs.length; i++){
      this.loadFile2Json(fs[i], parentId, sb);
      if(fs[i].isDirectory()){
        try{
          loadFiles2List(fs[i],sb) ;
        }catch(Exception e){}
      }
    }
  }
  public void loadFile2Json(File file , String parentId , StringBuffer sb){
    String imgAddress = "";
    String nodeId = this.getFileNoExt(file);
    sb.append("{");
    sb.append("nodeId:\"" + file.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\") + "\"");
    sb.append(",name:\"" + nodeId + "\"");
    sb.append(",parentId:\"" + parentId + "\"");
    sb.append(",isHaveChild:" +( (file.isDirectory()&&file.listFiles().length > 0)  ? 1 : 0 )+ "");
    sb.append(",imgAddress:\"" 
        + ( file.isDirectory() ? imgAddress : "/yh/core/styles/style1/img/dtree/file.jpg" )
        + "\"");
    sb.append("},");
  }
  public String getFileNoExt(File file){
    String  rtName = file.getName();
    int pointIndex = rtName.lastIndexOf(YHFileConst.PATH_POINT);
    String noExt = "";
    if (pointIndex < 0) {
      noExt = rtName;
    }else if (pointIndex != 0) {
      noExt = rtName.substring(0, pointIndex);
    }
    return noExt;
  }
  public String previewPage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      try {
        String htmlContent = new String(request.getParameter("htmlContent").getBytes("ISO-8859-1"),"UTF-8");
        if(htmlContent ==  null || "".equals(htmlContent)){
          htmlContent = "";
        }
        htmlContent = htmlContent.replaceAll("\"", "'");
        htmlContent = htmlContent.replaceAll("[\n-\r]", "");
        
        Map<String,String> dataMap = new HashMap();
        Map<String,Object> map = request.getParameterMap();
        StringBuffer sb = new StringBuffer("{htmlContent:\"" + htmlContent + "\",data:{");
        for(String key: map.keySet()){
          if(key.indexOf("Data") != -1){
            String str = new String(request.getParameter(key).getBytes("ISO-8859-1"),"UTF-8");
            dataMap.put(key, str);
            str = str.replaceAll("\"", "\\\\\"");
            sb.append("\"" + key + "\":\"" + str + "\",");
          }
        }
        if(dataMap.size() > 0){
          sb.deleteCharAt( sb.length() - 1 );
        }
        sb.append("}}");
        YHHtmlParser tp = new YHHtmlParser();
        htmlContent = htmlContent.replaceAll("&", "&amp;");
        String contextPath = request.getContextPath();
        htmlContent = tp.parseToHtml(htmlContent, contextPath, dataMap);
       // System.out.print(htmlContent);
        request.setAttribute("htmlContent", htmlContent);
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
        throw ex;
      }finally {
        
      }
      return "/core/funcs/wizardtool/previewpage.jsp";
    } 
  public String deploy(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      try {
        String htmlContent = new String(request.getParameter("htmlContent").getBytes("ISO-8859-1"),"UTF-8");
        String deployPath = request.getParameter("deployPath").replaceAll("\\\\", "\\\\\\\\");
        if(htmlContent ==  null || "".equals(htmlContent)){
          htmlContent = "";
        }
        htmlContent = htmlContent.replaceAll("\"", "'");
        htmlContent = htmlContent.replaceAll("[\n-\r]", "");
        
        Map<String,String> dataMap = new HashMap();
        Map<String,Object> map = request.getParameterMap();
        StringBuffer sb = new StringBuffer("{htmlContent:\"" + htmlContent + "\",data:{");
        for(String key: map.keySet()){
          if(key.equals("htmlContent")){
            
          }else{
            String str = new String(request.getParameter(key).getBytes("ISO-8859-1"),"UTF-8");
            dataMap.put(key, str);
            str = str.replaceAll("\"", "\\\\\"");
            
            sb.append("\"" + key + "\":\"" + str + "\",");
          }
        }
        if(dataMap.size() > 0){
          sb.deleteCharAt( sb.length() - 1 );
        }
        sb.append("}}");
       // YHFileUtility.storeString2File("E:\\workspace\\lh\\e.html", sb.toString());
        YHHtmlParser tp = new YHHtmlParser();
        htmlContent = htmlContent.replaceAll("&", "&amp;");
        htmlContent = tp.parseToHtml(htmlContent, request.getContextPath(), dataMap);
       
        //YHFileUtility.storeString2File(deployPath, htmlContent);
        
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");    
        response.setHeader("Cache-Control", "no-cache");   
        PrintWriter out = response.getWriter();
        out.print("<div style='color:red'>发布成功</div>");
        out.flush();
        out.close();
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
        throw ex;
      }finally {
        
      }
      return null;
    } 
  public String newFolder(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      try{
        String path = request.getParameter("path");
        String  newPath = path + "\\new File";
        
        File file = new File(newPath);
        int i = 0;
        while(file.exists()){
          i++;
          newPath = path + "\\new File" + i;
          file = new File(newPath);
        }
        file.mkdir();
        StringBuffer sb = new StringBuffer();
        this.loadFile2Json(file, path.replaceAll("\\\\", "\\\\\\\\"), sb);
        sb.deleteCharAt(sb.length() -1);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "数据返回成功");
        request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
  public String deleteFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      try{
        String path = request.getParameter("path");
        File file = new File(path);
        if(file.exists()){
          file.delete();
        }
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "数据返回成功");
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
        throw ex;
      }
      return "/core/inc/rtjson.jsp";
    }
  public String reName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try{
      String path = request.getParameter("path");
      String newName = request.getParameter("newName");
      
      // String nameNoExt = YHFileUtility.getFileNameNoExt(path);
      String noNamePath = YHFileUtility.getFilePath(path);
      String ext = YHFileUtility.getFileExtName(path);
      if(!"".equals(ext)){
        newName += "." + ext;
      }
      String newPath = noNamePath + "\\" + newName;
      File newFile = new File(newPath);
      if(!newFile.exists()){
        File file = new File(path);
        if(file.exists()){
          file.renameTo(newFile);
        }
        StringBuffer sb = new StringBuffer();
        String parentPath = newFile.getParent();
        this.loadFile2Json(newFile, parentPath.replaceAll("\\\\", "\\\\\\\\"), sb);
        
        sb.deleteCharAt(sb.length() -1);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "数据返回成功");
        request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      }else{
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "该文件已存在，请重新命名");
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "系统错误");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String newPage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try{
      String path = request.getParameter("path");
      String  newPath = path + "\\newpage.html";
      
      File file = new File(newPath);
      int i = 0;
      while(file.exists()){
        i++;
        newPath = path + "\\newpage" + i + ".html";
        file = new File(newPath);
      }
      YHFileUtility.storeString2File(newPath, "{htmlContent:\"\",data:{}}");
      
      StringBuffer sb = new StringBuffer();
      this.loadFile2Json(file, path.replaceAll("\\\\", "\\\\\\\\"), sb);
      sb.deleteCharAt(sb.length() - 1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "数据返回成功");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "系统错误");
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String downPage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      InputStream in = null;
      OutputStream out = null;
      try{
        String path = request.getParameter("path");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + YHFileUtility.getFileName(path));
        in = new FileInputStream(path);
        out = response.getOutputStream();
        byte[] buff = new byte[1024];
        int readLength = 0;
        while ((readLength = in.read(buff)) > 0) {        
           out.write(buff, 0, readLength);
        }
        out.flush();
      }catch(Exception ex) {
        ex.printStackTrace();
      }finally {
        try {
          if (in != null) {
            in.close();
          }
        }catch(Exception ex) {
          ex.printStackTrace();
        }
      }
      return null;
  }
  public String deployToLocation(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    InputStream in = null;
    OutputStream out = null;
    try{
      String path = request.getParameter("path");
      StringBuffer sb = YHFileUtility.loadLine2Buff(path);
      
      String tempSb = sb.substring("{htmlContent:\"".length(),sb.indexOf("\",data:{"));
      YHHtmlParser tp = new YHHtmlParser();
      tempSb = tempSb.replaceAll("&", "&amp;");
      Map dataMap = new HashMap();
      String sb2 = sb.substring(sb.lastIndexOf("\",data:{"),sb.length() - 2);
      String sb3 = sb2.substring("\",data:{".length());
      if(!sb3.equals("")){
        sb3 = sb3.substring(1, sb3.length() - 1);
        String[] dataStrs = sb3.split("\",\"");
        for(int i = 0 ; i < dataStrs.length ; i++ ){
          String s = dataStrs[i];
          String[] unitStr = s.split("\":\"");
          dataMap.put(unitStr[0] , unitStr[1].replaceAll("\\\\\"", "\""));
        }
      }
      
      
      String htmlContent = tp.parseToHtml(tempSb, request.getContextPath(), dataMap);
      
      
      response.setContentType("application/octet-stream");
      response.setHeader("Content-Disposition", "attachment;filename=" + YHFileUtility.getFileName(path));
      in = new ByteArrayInputStream(htmlContent.getBytes("UTF-8")) ;
      out = response.getOutputStream();
      byte[] buff = new byte[1024];
      int readLength = 0;
      while ((readLength = in.read(buff)) > 0) {        
         out.write(buff, 0, readLength);
      }
      out.flush();
    }catch(Exception ex) {
      ex.printStackTrace();
    }finally {
      try {
        if (in != null) {
          in.close();
        }
      }catch(Exception ex) {
        ex.printStackTrace();
      }
    }
    return null;
  }
  public String savePage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try {
      String path = request.getParameter("path");
      String htmlContent = new String(request.getParameter("htmlContent").getBytes("ISO-8859-1"),"UTF-8");
      if(htmlContent ==  null || "".equals(htmlContent)){
        htmlContent = "";
      }
      htmlContent = htmlContent.replaceAll("\"", "'");
      htmlContent = htmlContent.replaceAll("[\n-\r]", "");
      
      Map<String,Object> map = request.getParameterMap();
      StringBuffer sb = new StringBuffer("{htmlContent:\"" + htmlContent + "\",data:{");
      int i = 0 ;
      for(String key: map.keySet()){
        if(key.indexOf("Data") != -1){
          String str = new String(request.getParameter(key).getBytes("ISO-8859-1"),"UTF-8");
          i++;
          str = str.replaceAll("\"", "\\\\\"");
          sb.append("\"" + key + "\":\"" + str + "\",");
        }
      }
      if(i > 0){
        sb.deleteCharAt( sb.length() - 1 );
      }
      sb.append("}}");
      
      YHFileUtility.storeString2File(path, sb.toString());
      
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html");    
      response.setHeader("Cache-Control", "no-cache");   
      PrintWriter out = response.getWriter();
      out.print("<body onload=\"alert('保存成功！')\"></body>");
      out.flush();
      out.close();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "登录失败");
      throw ex;
    }finally {
      
    }
    return null;
  }
  public static void main(String[] args) throws Exception{
    String path = "D:\\workspace\\lh\\aae.html";
    StringBuffer sb = YHFileUtility.loadLine2Buff(path);
    
    String tempSb = sb.substring("{htmlContent:\"".length(),sb.indexOf("\",data:{"));
    YHHtmlParser tp = new YHHtmlParser();
    tempSb = tempSb.replaceAll("&", "&amp;");
    Map dataMap = new HashMap();
    String sb2 = sb.substring(sb.lastIndexOf("\",data:{"),sb.length() - 2);
    String sb3 = sb2.substring("\",data:{".length());
    sb3 = sb3.substring(1, sb3.length() - 1);
    String[] dataStrs = sb3.split("\",\"");
    for(String s : dataStrs){
      String[] unitStr = s.split("\":\"");
      
      //System.out.println(unitStr[0]);
      //System.out.println(unitStr[1]);
      dataMap.put(unitStr[0], unitStr[1]);
    }
   // sb3 = sb3.substring(1, sb3.length() - 1);
   // String htmlContent = tp.parseToHtml(tempSb, "/yh", dataMap);
    
    //System.out.println(htmlContent);
  }
  
}
