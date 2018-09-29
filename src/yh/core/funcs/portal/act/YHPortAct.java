package yh.core.funcs.portal.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.portal.data.YHPort;
import yh.core.funcs.portal.logic.YHPortalLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;


public class YHPortAct {
  private String sp = System.getProperty("file.separator");
  private String webPath = "core" + sp 
    + "funcs" + sp 
    + "portal" + sp 
    + "modules" + sp;
  
  public void unzip(File zipPath, String filePath) throws IOException {
    try {
      ZipFile zipFile = new ZipFile(zipPath, "GBK");
      Enumeration e = zipFile.getEntries();
      ZipEntry ze = null;
      File folder = new File(filePath);
      folder.mkdir();
      while (e.hasMoreElements()) {
        ze = (ZipEntry) e.nextElement();
        if (ze.isDirectory()) {
          new File(filePath + ze.getName()).mkdir();
        }
        else {
          InputStream is = zipFile.getInputStream(ze);
          String name = ze.getName();
          //File f = new File(filePath + new String(ze.getName().getBytes("ISO8859_1"), "gbk"));
          File f = new File(filePath + ze.getName());
          FileOutputStream fout = new FileOutputStream(f);
          byte[] b = new byte[1024];
          int i = 0;
          while((i = is.read(b)) > 0) {
            fout.write(b, 0, i);
          }
          is.close();
          fout.flush();
          fout.close();
        }
      }
      zipFile.close();
    } catch (FileNotFoundException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    }
  }
  
  public void zip(ZipOutputStream out, File file) throws Exception { 
    try {
      byte[] buf = new byte[1024];
      if (file.isDirectory()) {
        zip(out, file.listFiles());
      }
      else {
        FileInputStream in = new FileInputStream(file);
        out.putNextEntry(new org.apache.tools.zip.ZipEntry(file.getName()));
        int len;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
        out.closeEntry();
        in.close();
      }
      out.flush();
    } catch (FileNotFoundException e) {
      
    } catch (Exception e) {
      throw e;
    }
  }
  
  public void zip(ZipOutputStream out, List<String> filePath) throws Exception {
    for (String s : filePath) {
      File file = new File(s);
      zip(out, file);
    }
  }
  
  public void zip(ZipOutputStream out, File[] files) throws Exception {
    for (File file : files) {
      zip(out, file);
    }
  }
  
  public void zip(String zipFile, List<String> files) throws Exception {
    FileOutputStream fos = new FileOutputStream(zipFile);
    zip(fos, files);
  }
  
  public void zip(OutputStream fos, List<String> files) throws Exception {
    ZipOutputStream out = new ZipOutputStream(fos);
    out.setEncoding("GBK");
    zip(out, files);
    out.close();
  }
  
  /**
   * 导入模块
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String importPorts(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    int amount = 0;
    int status = 0;
    try {
      Connection dbConn = null;
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      
      InputStream is = fileForm.getInputStream((String)fileForm.iterateFileFields().next());
      
      String type = fileForm.getParameter("type");
      SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
      String name = df.format(new Date());
      
      String zipPath = request.getSession().getServletContext().getRealPath(sp) 
      + webPath + "zip" + sp;
      
      File zipFile = new File(zipPath + name + ".zip");
      FileOutputStream fos = new FileOutputStream(zipFile);
      byte[] b = new byte[1024];
      int i = 0;
      while((i = is.read(b)) > 0) {
        fos.write(b, 0, i);
      }
      is.close();
      fos.flush();
      fos.close();
      unzip(zipFile, zipPath + name + sp);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      File zipFolder = new File(zipPath + name);
      YHPortalLogic logic = new YHPortalLogic();
      
      String portPath = request.getSession().getServletContext().getRealPath(sp) + webPath;
      for (File f : zipFolder.listFiles()) {
        if (logic.existPort(dbConn, f.getName())) {
          f.delete();
          continue;
        }
        YHPort port = new YHPort();
        port.setFileName(f.getName());
        port.setDeptId("0");
        logic.newPort(dbConn, port);
        FileOutputStream o = new FileOutputStream(portPath + f.getName());
        FileInputStream in = new FileInputStream(zipPath + name + sp + f.getName());
        
        int j = 0;
        while((j = in.read(b)) > 0) {
          o.write(b, 0, j);
        }
        in.close();
        o.flush();
        o.close();
        f.delete();
        amount++;
      }
      zipFolder.delete();
      zipFile.delete();
    }catch(Exception ex) {
      status = 1;
    }
    return "/core/funcs/portal/importsuccess.jsp?status=" + status + "&amount=" + amount;
  }
  
  /**
   * 导出模块
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportPorts(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String ports = request.getParameter("portsStr");
    try {
      if (YHUtility.isNullorEmpty(ports)) {
        return "/core/inc/rtjson.jsp";
      }
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      YHPortalLogic logic = new YHPortalLogic();
      List<String> files = new ArrayList<String>();
      String portPath = request.getSession().getServletContext().getRealPath(sp) 
      + webPath;
      
      for (String port : ports.split(",")) {
        YHPort p = null;
        try {
          p = logic.queryPort(dbConn, Integer.parseInt(port));
        } catch (NumberFormatException e) {
          continue;
        }
        if (p == null) {
          continue;
        }
        files.add(portPath + p.getFileName());
      }
      SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
      response.setHeader("Content-disposition", "attachment; filename=export-"
          + df.format(new Date()) + ".zip");
      response.setHeader("Cache-Control",
      "must-revalidate, post-check=0, pre-check=0,private, max-age=0");
      response.setHeader("Content-Type", "application/octet-stream");
      response.setHeader("Content-Type", "application/force-download");
      response.setHeader("Pragma", "public");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
      OutputStream out = response.getOutputStream();
      zip(out, files);
      out.flush();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "";
  }
  
  /**
   * 列出用户拥有权限的所有模块
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listAllPorts(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

//      YHPerson user = (YHPerson) request.getSession()
//          .getAttribute("LOGIN_USER");// 获得登陆用户

      String sql = "select SEQ_ID, FILE_NAME, VIEW_TYPE, MODULE_POS from PORT order by SEQ_ID";

      YHPageQueryParam queryParam = (YHPageQueryParam) YHFOM.build(request
          .getParameterMap());
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,
          queryParam, sql);

      PrintWriter pw = response.getWriter();
      pw.println(pageDataList.toJson());
      pw.flush();
      pw.close();
      return null;
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
  }
  
  /**
   * 删除模块
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String delete(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String ports = request.getParameter("ports");
      
      YHPortalLogic logic = new YHPortalLogic();
      
      for (String port : ports.split(",")) {
        try {
          logic.deletePort(dbConn, Integer.parseInt(port));
        } catch (NumberFormatException e) {
          continue;
        }
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询属性");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}