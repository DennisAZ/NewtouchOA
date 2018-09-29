package yh.core.esb.test.act;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import yh.core.data.YHRequestDbConn;
import yh.core.esb.common.util.ClientPropertiesUtil;
import yh.core.esb.common.util.YHEsbUtil;
import yh.core.esb.frontend.YHEsbFrontend;
import yh.core.esb.server.user.data.TdUser;
import yh.core.esb.test.logic.YHEsbTesterLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.load.YHConfigLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHEsbTesterAct {
  public static final String TEST_FOLDER = "E:\\esb\\test";
  
  public String test(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
//      long start = System.currentTimeMillis();
//      new YHEsbTesterAct().test(new String[] {"192.168.0.102:8089", "pjn-pc:8089"}, 1000 * 60 * 2);
//      long uEnd = System.currentTimeMillis();
//      
//      new YHEsbTesterLogic().stat(dbConn, new File("e:\\esb\\log.txt"), start, uEnd);
//      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
//      request.setAttribute(YHActionKeys.RET_MSRG, "测试成功");
      
      
      
      String folder = request.getParameter("folder");
      String time = request.getParameter("time");
      String to = request.getParameter("toId");
      String log = request.getParameter("log");
      
      if (YHUtility.isNullorEmpty(to)) {
        to = "ALL_USERS";
      }
      
      File testFolder;
      if (YHUtility.isNullorEmpty(folder)) {
        testFolder = new File(TEST_FOLDER);
      }
      else {
        testFolder = new File(folder);
      }
      
      int ms = 0;
      try {
        ms = (int)(Float.parseFloat(time) * 60 * 1000);
      } catch (NumberFormatException e) {
        ms = 30 * 1000;
      }
      
      YHEsbTesterLogic logic = new YHEsbTesterLogic();
      
      if (!YHUtility.isNullorEmpty(log)) {
        logic.test(dbConn, new File("D:\\log.log"), testFolder, to, 1, ms);
      }
      else {
        logic.test(testFolder, to, ms);
      } 
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "测试成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String clear(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHEsbTesterLogic logic = new YHEsbTesterLogic();
      logic.clearDB(dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "测试成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  public String compute(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHEsbTesterLogic logic = new YHEsbTesterLogic();
      int downloadCount = logic.getDownloadNo(dbConn);
      int uploadCount = logic.getUploadNo(dbConn);
      int uploadedCount = logic.getUploadSuccessfulNo(dbConn);
      int downloadedCount = logic.getDownloadSuccessfulNo(dbConn);

      String data = String.format("{\"uc\": \"%d\", \"udc\": \"%d\", \"dc\": \"%d\", \"ddc\": \"%d\"}"
          , uploadCount
          , uploadedCount
          , downloadCount
          , downloadedCount);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "测试成功");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public void test(String[] clients, final long time) throws InterruptedException {
    ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
    for (final String s : clients) {
      if (!YHUtility.isNullorEmpty(s)) {
        Runnable r = new Runnable() {
          public void run() {
            try {
              String serviceUrl = "http://" + s + "/yh/services/OAWebservice";
              Service service = new Service(); 
              Call call = (Call) service.createCall(); 
              call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
              call.setOperationName("test"); 
              call.addParameter("time", XMLType.XSD_LONG, ParameterMode.IN); 
              call.setReturnType(XMLType.XSD_BOOLEAN); 
              boolean ret = false;
              ret = (Boolean) call.invoke(new Object[] {time});
              YHEsbUtil.println(ret ? "调用客户端测试程序成功" : "调用客户端测试程序失败");
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        };
        pool.submit(r);
      }
    }
    
    pool.shutdown();
    while(!pool.awaitTermination(60, TimeUnit.SECONDS)) {
      Thread.sleep(1000);
    }
  }
  
  public static void main(String[] args) throws Exception {
    String installPath = "D:\\project\\yh";
    //加载数据库配置信息compressJs
    String sysConfFile = installPath + "\\webroot\\yh\\WEB-INF\\config\\sysconfig.properties";
    YHSysProps.setProps(YHConfigLoader.loadSysProps(sysConfFile));
    String selfConfFile = installPath + "\\webroot\\yh\\WEB-INF\\config\\selfconfig.properties";
    YHSysProps.addProps(YHConfigLoader.loadSysProps(selfConfFile));
    
    YHDBUtility db = new YHDBUtility();
    Connection dbConn = db.getConnection(false, "YH");
    long start = System.currentTimeMillis();
    new YHEsbTesterAct().test(new String[] {"192.168.0.102:8089", "pjn-pc:8089", "192.168.0.155"}, 1000 * 60 * 2);
    long uEnd = System.currentTimeMillis();
    new YHEsbTesterLogic().stat(dbConn, new File("e:\\esb\\log.txt"), start, uEnd);
    System.out.println("----");
    
  }
}
