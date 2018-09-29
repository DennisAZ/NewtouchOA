package yh.core.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.global.YHSysKeys;
import yh.core.util.file.YHFileUtility;
import yh.core.util.YHUtility;

public class YHFileOutputServlet extends HttpServlet {
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
   
    String fileName = request.getParameter(YHSysKeys.OUT_PUT_FILE);
    if (fileName == null) {
      fileName = (String)request.getAttribute(YHSysKeys.OUT_PUT_FILE);
    }
    if (YHUtility.isNullorEmpty(fileName)) {
      String timeStr = YHUtility.getCurDateTimeStr("yyyyMMddhhmmss");
      fileName = timeStr + ".text";
    }
    List dataList = (List)request.getAttribute(YHSysKeys.OUT_PUT_FILE_DATA_LIST);
    String charSet = (String)request.getAttribute(YHSysKeys.OUT_PUT_CHAR_SET);
    if (charSet == null) {
      charSet = "GBK";
    }
    if (dataList != null) {
      response.setContentType("text/plain;charset=" + charSet);      
    }else {
      if (fileName.toLowerCase().endsWith(".pdf")) {
        response.setContentType("application/pdf");
      }else {
        response.setContentType("application/octet-stream");
      }
    }
    
    response.setHeader("Content-Disposition", "attachment;filename="
        + YHFileUtility.getFileName(fileName));    
    if (dataList != null) {
      PrintWriter writer = response.getWriter();
      for (int i = 0; i < dataList.size(); i++) {
        String dataLine = (String)dataList.get(i);
        writer.write(dataLine + "\r\n");
      }
      writer.flush();
      return;
    }
    
    InputStream in = null;
    OutputStream out = null;
    try {      
      in = new FileInputStream(fileName);
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
