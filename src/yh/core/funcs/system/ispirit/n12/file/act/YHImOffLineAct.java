package yh.core.funcs.system.ispirit.n12.file.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;


import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.ispirit.n12.file.data.YHImOfflineFile;
import yh.core.funcs.system.ispirit.n12.file.logic.YHImOffLineLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;

public class YHImOffLineAct {
  
  /**
   * 单文件附件上传
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
public String uploadFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      Connection dbConn = null;
  try{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    Map<String, String> attr = null;
    String attrId = "";
    String attrName = "";
    String dest_id = (fileForm.getParameter("DEST_UID")== null )? "":fileForm.getParameter("DEST_UID");
  //  String NAME = (fileForm.getParameter("NAME")== null )? "":fileForm.getParameter("NAME");
    long file_size=fileForm.getFileSize("ATTACHMENT");


    YHImOffLineLogic imOffLineLogic = new YHImOffLineLogic();
    attr = imOffLineLogic.fileUploadLogic(fileForm, YHSysProps.getAttachPath());
    Set<String> keys = attr.keySet();
    for (String key : keys){
      String value = attr.get(key);
      if(attrId != null && !"".equals(attrId)){
        if(!(attrId.trim()).endsWith(",")){
          attrId += ",";
        }
        if(!(attrName.trim()).endsWith("*")){
          attrName += "*";
        }
      }
      attrId += key + ",";
      attrName += value + "*";
    }
    String msg="";
    if("0".equals(dest_id)){
      msg="文件上传失败";
    }else{  //加入记录
      String maxId = imOffLineLogic.addOffLineFile(dbConn, person, dest_id, attrId, attrName, file_size+""); 
      msg="+OK"+maxId;
    }

    
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Cache-Control","private");
    //response.setHeader("Accept-Ranges","bytes");
    PrintWriter out = response.getWriter();
    out.print(msg);
    out.flush();
  
  }catch (SizeLimitExceededException ex) {
  } catch (Exception e){
    e.printStackTrace();
  }
  return null;
}

/***
 *添加离线文件记录 
 */
/*public String addOffLineFile(Connection dbConn,YHPerson person,String destUid,String attrId,String attrName,String fileSize ) throws Exception {
 try{
    YHImOffLineLogic imOffLineLogic = new YHImOffLineLogic();
    imOffLineLogic.addOffLineFile(dbConn, person, destUid, attrId, attrName, fileSize);
    
    
  } catch (Exception ex) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
    throw ex;
  }
 return "/core/inc/rtjson.jsp";
 }*/

 /***
  * 获取文件路径
  */
public String downFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
  Connection dbConn = null;
 try{
   YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   dbConn = requestDbConn.getSysDbConn();
   String fId=request.getParameter("ID");
   YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
   YHImOffLineLogic imOffLineLogic = new YHImOffLineLogic();
   String Filepath=imOffLineLogic.getFileName(dbConn, person, fId);
  // System.out.println(Filepath);
  // response.sendRedirect(url);//下载文件
    
   if(!YHUtility.isNullorEmpty(Filepath)){
     
     String fileName = Filepath.substring(Filepath.indexOf(File.separator)+1, Filepath.length());
     fileName= fileName.substring(fileName.indexOf("_")+1, fileName.length());
     fileName = URLEncoder.encode(fileName,"UTF-8");
     if (fileName.length() > 150) {
       fileName =new String(fileName.getBytes("GB2312") , "ISO-8859-1");
     }
     fileName = fileName.replaceAll("\\+", "%20");
     
     
     File file = new File(Filepath);
     InputStream  is = new FileInputStream(file);
     OutputStream ops = null;
  
   

     response.setContentType("application/octet-stream");
     response.setHeader("Accept-Ranges","bytes");
     response.setHeader("Pragma","public");
     response.setHeader("Cache-Control","maxage=3600");
     response.setHeader("Accept-Length",String.valueOf(file.length()));
     response.setHeader("Content-Length",String.valueOf(file.length()));
     response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
     
     ops = response.getOutputStream();
     if(is != null){
       byte[] buff = new byte[8192];
       int byteread = 0;
       while( (byteread = is.read(buff)) != -1){
         ops.write(buff,0,byteread);
         ops.flush();
       }
     }
     if (is != null) {
       is.close();
     }
     //删除附件  记录
     file.delete();
     
     Statement stmt=null;
     String sql=" delete from oa_im_offline_attach where id='"+fId+"' ";
     stmt = dbConn.createStatement();
     stmt.execute(sql);
   }else{
     String errorStr="文件已被删除或转移";  
     response.setCharacterEncoding("UTF-8");
     response.setContentType("text/html;charset=UTF-8");
     response.setHeader("Cache-Control","private");
     //response.setHeader("Accept-Ranges","bytes");
     PrintWriter out = response.getWriter();
     out.print(errorStr);
     out.flush();
   }
   
  }catch (Exception ex) {
   
    ex.printStackTrace();
  }
    
return null;
}

}



