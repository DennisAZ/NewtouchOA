package yh.plugins.workflow;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;

/**
 * 工作流插件接口
 * @author jpt
 *
 */
public class CopyAttach implements YHIWFPlugin{
  /**
   * 节点执行前执行
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String before(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection conn = requestDbConn.getSysDbConn();
      String runIdStr = request.getParameter("runId");
      copyAttah(  conn , runIdStr ,  "c:\\");
    } catch(Exception ex) {
      throw ex;
    }
    return null;
  }
  public void copyAttah( Connection conn ,String runId , String toPath) throws Exception {
    String query = "select ATTACHMENT_ID , ATTACHMENT_NAME from oa_fl_run where RUN_ID = " + runId;
    Statement stm = null;
    ResultSet rs = null;
    String attachmentId = "";
    String attachmentName = "";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        attachmentId = rs.getString("ATTACHMENT_ID");
        attachmentName = rs.getString("ATTACHMENT_NAME");
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    this.copyAttach(attachmentId, attachmentName, toPath);
  }
  public static String filePath = YHSysProps.getAttachPath() + "\\workflow";
  public void copyAttachSingle(String attId , String attName , String toPath) throws Exception {
    int index = attId.indexOf("_");
    String hard = "";
    String str = "";
    if (index > 0) {
      hard = attId.substring(0, index);
      str = attId.substring(index + 1);
    } else {
      hard = "all";
      str = attId;
    }
    String path = filePath + "\\"  + hard + "\\" + str + "_" + attName;
    File  category = new File(toPath) ;
    if (!category.exists()) {
      category.mkdirs();
    }
    if (!toPath.endsWith("\\")) {
      toPath += "\\";
    }
    toPath += attName;
    YHFileUtility.copyFile(path, toPath);
  }
  public void copyAttach(String attId , String attName, String toPath) throws Exception {
    String newAttId = "";
    if (attId == null) {
      return ;
    }
    String[] attIds = attId.split(",");
    String[] attNames = attName.split("\\*");
    for(int i = 0 ;i < attIds.length ;i ++){
      String tmp = attIds[i];
      if ("".equals(tmp)) {
        continue;
      }
      String attN = attNames[i];
      this.copyAttachSingle(tmp, attN , toPath);
    }
  }
  /**
   * 节点执行完毕执行
   * @param request
   * @param response
   * @return
   */
  public String after(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //System.out.println("------------结束啦");
    return null;
  }
}
