package yh.subsys.jtgwjh.ntko.act;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.doc.logic.YHAttachmentLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.subsys.jtgwjh.docSend.data.YHJhDocsendInfo;
import yh.subsys.jtgwjh.ntko.logic.YHDocLogic;
import yh.subsys.oa.rollmanage.logic.YHRmsFileLogic;

/**
 * @author yyb
 *
 */
public class YHDocAct {
  
  private YHRmsFileLogic logic = new YHRmsFileLogic();
  
  public String getBookmark(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String seqId = request.getParameter("seqId");
    if(YHUtility.isNullorEmpty(seqId)){
      seqId = "0";
    }
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHDocLogic logic = new YHDocLogic();
      String root = request.getRealPath("/");
      String docStyle = logic.getDocStyle1(root);
      String content = logic.getContentStyle(root);
      
      YHORM orm = new YHORM();
      YHJhDocsendInfo docsendInfo = (YHJhDocsendInfo)orm.loadObjSingle(dbConn, YHJhDocsendInfo.class, Integer.parseInt(seqId));
      String docStyleThis = "";
      if(docsendInfo != null && docsendInfo.getDocStyle() != null){
        docStyleThis = docsendInfo.getDocStyle();
      }
      
      String str = "{docStyle:"+ docStyle +",content:"+content+",docStyleThis:\""+docStyleThis+"\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getRunData(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int runId = Integer.parseInt(request.getParameter("runId"));
      YHDocLogic logic = new YHDocLogic();
      String root = request.getRealPath("/");
      String doc = logic.getBookmark(runId, dbConn);
      String str = "{runData:" + doc + "}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String delDoc(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      YHORM orm = new YHORM();
      YHJhDocsendInfo docsendInfo = (YHJhDocsendInfo)orm.loadObjSingle(dbConn, YHJhDocsendInfo.class, Integer.parseInt(seqId));

      if(docsendInfo != null){
        YHDocLogic.deleteAttachement(docsendInfo.getMainDocId(), docsendInfo.getMainDocName());
        
        String mainDocId = docsendInfo.getMainDocId();
        String attachmentId = docsendInfo.getAttachmentId();
        String attachmentName = docsendInfo.getAttachmentName();
        String attachmentIdArray[] = attachmentId.split(",");
        String attachmentNameArray[] = attachmentName.split("\\*");
        String attaId = "";
        String attaName = "";
        for(int i = 0; i < attachmentIdArray.length; i++){
          if(mainDocId.equals(attachmentIdArray[i])){
            continue;
          }
          attaId += attachmentIdArray[i] + ",";
          attaName += attachmentNameArray[i] + "*";
        }
        
        docsendInfo.setMainDocId("");
        docsendInfo.setMainDocName("");
        docsendInfo.setAttachmentId(attaId);
        docsendInfo.setAttachmentName(attaName);
        docsendInfo.setDocStyle("");
        orm.updateSingle(dbConn, docsendInfo);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功 ");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String saveDocStyle(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      String docStyle = request.getParameter("docStyle");
      YHORM orm = new YHORM();
      YHJhDocsendInfo docsendInfo = (YHJhDocsendInfo)orm.loadObjSingle(dbConn, YHJhDocsendInfo.class, Integer.parseInt(seqId));
      docsendInfo.setDocStyle(docStyle);
      orm.updateSingle(dbConn, docsendInfo);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
}
