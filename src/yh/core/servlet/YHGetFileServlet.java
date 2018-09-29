package yh.core.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import yh.core.data.YHProps;
import yh.core.global.YHActionKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;

/**
 * 取得服务器端文件
 * @author jpt
 *
 */
public class YHGetFileServlet extends HttpServlet {
  /**
   * <p>Process an HTTP "GET" request.</p>
   *
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception occurs
   */
  public void doGet(HttpServletRequest request,
            HttpServletResponse response)
      throws IOException, ServletException {
   
//    HttpSession session = request.getSession();
//    String fileNameOrgn = request.getParameter(YHActionKeys.UPLOAD_FILE_NAME_ORGN);
    String fileNameSrver = null;
    String module = request.getParameter("module");
    if (!YHUtility.isNullorEmpty(module)) {
      String ym = request.getParameter("ym");
      String attachFile = request.getParameter("attachFile");
      //attachFile = YHUtility.transferCode(attachFile, "ISO-8859-1", "UTF-8");
      fileNameSrver = YHSysProps.getAttachPath() + File.separator + module  + File.separator +  ym  + File.separator +  attachFile;
    }
    if (fileNameSrver == null) {
      fileNameSrver = request.getParameter(YHActionKeys.UPLOAD_FILE_NAME_SERVER);
      //fileNameSrver = YHUtility.transferCode(fileNameSrver, "ISO-8859-1", "UTF-8");
    }
    if (fileNameSrver == null) {
      fileNameSrver = (String)request.getAttribute(YHActionKeys.UPLOAD_FILE_NAME_SERVER);
      //fileNameSrver = YHUtility.transferCode(fileNameSrver, "ISO-8859-1", "UTF-8");
    }    
    String filePath = fileNameSrver.replace("/", File.separator);
    //System.out.println(filePath);
    if (filePath.indexOf(":") != 1 && !filePath.startsWith("/")) {
      filePath = YHServletUtility.getWebAppDir(this.getServletContext())
        + fileNameSrver.replace("/", "\\");
    }
    long fileLength = new File(filePath).length();
    String lowerCaseFileName = fileNameSrver.toLowerCase();
    String retName = null;
    int tmpIndex = fileNameSrver.lastIndexOf(File.separator );
    if (tmpIndex < 0) {
      tmpIndex = fileNameSrver.lastIndexOf("/");
    }
    if (tmpIndex < 0) {
      retName = fileNameSrver;
    }else {
      retName = fileNameSrver.substring(tmpIndex + 1);
    }
    if (lowerCaseFileName.endsWith(".pdf")) {
      response.setContentType("application/pdf");
    }else if (lowerCaseFileName.endsWith(".txt")
        || lowerCaseFileName.endsWith(".text")) {
      response.setContentType("plain/text");
      response.setHeader("Content-Disposition", "attachment;filename=" + retName);
    }else if (lowerCaseFileName.endsWith(".mht")) {
      response.setHeader("Cache-control", "private");
      response.setContentType("application/octet-stream");      
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Content-Length", String.valueOf(fileLength));
      retName = URLEncoder.encode(retName, "UTF-8");
      response.setHeader("Content-Disposition", "filename=" + retName);
    }else {
      response.setContentType("application/octet-stream");
      response.setHeader("Content-Disposition", "attachment;filename=" + retName);
    }
    
    //response.setHeader("Content-Disposition", "attachment;filename=" + fileNameOrgn);    
    InputStream in = null;
    OutputStream out = null;
    try {      
      in = new FileInputStream(filePath);
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
  }
  
  /**
   * <p>Process an HTTP "GET" request.</p>
   *
   * @param request The servlet request we are processing
   * @param response The servlet response we are creating
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception occurs
   */
  public void doPost(HttpServletRequest request,
            HttpServletResponse response)
      throws IOException, ServletException {
    doGet(request, response);
  }
}
