package yh.core.esb.server.demo.act;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.esb.client.data.YHEsbClientConfig;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.service.YHWSCaller;
import yh.core.esb.common.util.ClientPropertiesUtil;
import yh.core.esb.frontend.YHEsbFrontend;
import yh.core.esb.frontend.services.YHEsbService;
import yh.core.esb.server.act.YHRangeUploadAct;
import yh.core.esb.server.user.data.TdUser;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHPageLoader;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class ESBDemoAct {
  public YHWSCaller caller = new YHWSCaller();
  String path = YHRangeUploadAct.UPLOAD_PATH + File.separator + "ESB-CACHE";
  String recePath = "d:\\ESB-CACHE";
  
  public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    try {
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      
      String toId = fileForm.getParameter("toId");
      if(false == YHEsbFrontend.isOnline()){
        if(YHEsbFrontend.login() != 0){
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "连接esb服务器失败！");
        }
      }
      Iterator<String> iKeys = fileForm.iterateFileFields();
      String attachmentNameStr = "";
      String attachmentIdStr = "";
      
      String savePath = "";
      if (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        savePath = path + File.separator + fileName;
        File parentFile = new File(path);
        if (!parentFile.exists()) {
          parentFile.mkdir();
        }
        fileForm.saveFile(savePath);
        
        
        YHEsbClientConfig config = YHEsbClientConfig.builder(request.getRealPath("/") + YHEsbConst.CONFIG_PATH) ;
        YHEsbService service = new YHEsbService();
        service.send(savePath, toId, config.getToken(), "test", "测试哦");
        //caller.setWS_PATH(config.getWS_PATH());
       // caller.send(savePath, toId , "" ,config.getToken() );
        //esbService.send(savePath, toId);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "加入任务队列成功！");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/esb/server/demo/success.jsp";
  }
  
  public String receiveFilePage(HttpServletRequest request, HttpServletResponse response) throws Exception { 
    int count = 0;
    StringBuffer pageData = new StringBuffer();
    File cache = new File(recePath);
    if(cache.isDirectory()){
      File[] dir = cache.listFiles();
      for (int i = 0; i < dir.length; i++) {
        if(dir[i].isDirectory()){
          File[] files = dir[i].listFiles();
          for (int j = 0; j < files.length; j++) {
            File file = files[j];
            count++;
            
            //文件大小
            String module = "B";
            long size = file.length();
            if(file.length() >= 1024){
              module = "KB";
              size = file.length()/1024;
              if(size >= 1024){
                module = "MB";
                size = size/1024;
              }
            }
            module = size + module;
            
            //文件创建时间
            Calendar cd = Calendar.getInstance();
            cd.setTimeInMillis(file.lastModified());
            String month = calculate(cd.get(Calendar.MONTH)+1);
            String day = calculate(cd.get(Calendar.DAY_OF_MONTH));
            String hour = calculate(cd.get(Calendar.HOUR_OF_DAY));
            String minute = calculate(cd.get(Calendar.MINUTE));
            String second = calculate(cd.get(Calendar.SECOND));
            String time = cd.get(Calendar.YEAR) +"-"+ month +"-"+ day +" "+ hour +":"+ minute +":"+ second;
            pageData.append("{fileName:\""+file.getName()+"\",fileLength:\""+module+"\",fileTime:\""+time+"\"},");
          }
        }
      }
    }
    
    StringBuffer sb = new StringBuffer();
    if(count == 0){
      sb.append("{totalRecord:"+count+",pageData:[]}");
    }
    else{
      sb.append("{totalRecord:"+count+",pageData:[");
      sb.append(pageData.substring(0, pageData.length()-1)+"]}");
    }
    
    
//    Connection dbConn = null; 
//    try { 
//      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
//      dbConn = requestDbConn.getSysDbConn(); 
//      TdUser tdUser = (TdUser)request.getSession().getAttribute("ESB_LOGIN_USER");
//      if(tdUser == null){
//        YHEsbService esbService = new YHEsbService();
//        esbService.login();
//        tdUser = (TdUser)request.getAttribute("ESB_LOGIN_USER");
//      }
//      String sql = "select" +
//          " t.FROM_ID," +
//          " t.FILE_PATH," +
//          " s.TO_ID," +
//          " s.STATUS" +
//          " from ESB_TRANSFER_STATUS s" +
//          ", ESB_TRANSFER t" +
//          " where TYPE = '0'" +
//          " and s.to_id="+ClientPropertiesUtil.getProp("usercode")+
//          " order by s.SEQ_ID desc";
//      
//      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
//      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, queryParam, sql);
      
      PrintWriter pw = response.getWriter(); 
      pw.println(sb.toString()); 
      pw.flush(); 
  
      return null; 
//    }catch (Exception e) {
//      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
//      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
//      throw e; 
//    } 
  }
  
  private String calculate(int temp){
    return temp > 9 ? String.valueOf(temp) : "0"+temp;
  }
}
