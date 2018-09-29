package yh.subsys.inforesouce.act;
import yh.core.global.YHConst;
import yh.core.global.YHBeanKeys;
import java.sql.Connection;
import yh.core.util.YHUtility;
import yh.core.global.YHActionKeys;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.subsys.inforesouce.data.YHKengine;
import yh.subsys.inforesouce.logic.YHAFileMateLogic;
import yh.subsys.inforesouce.logic.YHMateTypeLogic;
import yh.subsys.inforesouce.util.YHFileMateConstUtil;

/**
 * 查找文件的主题词，组织机构名，人名，地名等<br>
 * 调用YHFileMateConstUtil 常量类、YHMateTypeLogic、YHAFileMateLogic等类
 * @see yh.subsys.inforesouce.util.YHFileMateConstUtil
 * @see yh.subsys.inforesouce.logic.YHAFileMateLogic
 * @see yh.subsys.inforesouce.logic.YHAFileMateLogic
 * @author qwx110
 *
 */
public class YHAFileMateAct{
  
  /**
   * <fieldset>
   * <legend>
   * 查找文件的主题词，组织机构名，人名，地名等<br></legend>
   * <p>通过文件id返回文件的主题词，组织机构名，人名，地名；<br>
   * 调用YHMateTypeLogic的findName返回主题词，组织机构名，人名，地名等元数据的编号<br>
   * 调用YHAFileMateLogic的findString方法返回文件对应的主题词，组织机构名，人名，地名等
   * </p>
   * <fieldset>
   * @see yh.subsys.inforesouce.logic.YHMateTypeLogic#findName(Connection, String)
   * @see yh.subsys.inforesouce.logic.YHAFileMateLogic#findString(int, String, String, String, String, String, Connection)
   * @param request
   * @param response
   * @return "/core/inc/rtjson.jsp"
   * @throws Exception
   */
  public String findAmate(HttpServletRequest request, HttpServletResponse response) throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    Connection dbConn = null;
   
    int seqId;
    String userName = null ;
    String areaName = null;
    String org = null;
    String subJect= null;
    String keyWord = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      YHMateTypeLogic logic = new YHMateTypeLogic();
      YHAFileMateLogic alogic = new YHAFileMateLogic();
      String attachmentId = request.getParameter("attachmentId");//文件Id
      seqId = logic.findKengine(dbConn, attachmentId);    
      userName = logic.findName(dbConn, YHFileMateConstUtil.userName);
      areaName = logic.findName(dbConn, YHFileMateConstUtil.areaName);
      org = logic.findName(dbConn, YHFileMateConstUtil.Org);
      subJect = logic.findName(dbConn, YHFileMateConstUtil.subJect);
      keyWord = logic.findName(dbConn, YHFileMateConstUtil.keyWord);
      YHKengine ki = alogic.findString(seqId,userName,areaName,org,subJect,keyWord,dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回摘要内容");
      request.setAttribute(YHActionKeys.RET_DATA,  toJson(ki) );
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    } 
    return "/core/inc/rtjson.jsp";
  }
  
  public String toJson(YHKengine engine){
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    if(engine != null){
       if(!YHUtility.isNullorEmpty(engine.getUserName())){
         sb.append("name:").append("\"").append(YHUtility.encodeSpecial(subAString(engine.getUserName()))).append("\"").append(",");
       }else{
         sb.append("name:").append("''").append(",");
       }
       if(!YHUtility.isNullorEmpty(engine.getAreaName())){
         sb.append("area:").append("\"").append(YHUtility.encodeSpecial(subAString(engine.getAreaName()))).append("\"").append(",");
       }else{
         sb.append("area:").append("''").append(",");
       }
       if(!YHUtility.isNullorEmpty(engine.getOrgName())){
         sb.append("orge:").append("\"").append(YHUtility.encodeSpecial(subAString(engine.getOrgName()))).append("\"");
       }else{
         sb.append("orge:").append("''");
       }
    }
    sb.append("}");
    return sb.toString();
  }
  
  public String subAString(String befor){
    if(!YHUtility.isNullorEmpty(befor)){
      String[] after = befor.split(",");
      if(after.length < 3){
        return befor;
      }else{
        for(int i=0; i<3; i++){
          String temp= "";
          if(i < 2){
            temp += after[i] + ",";
          }else{
            temp += after[i];
          }
          return temp;
        }
      }
    }
    return null;
  }
  
  /**
   * 返回文件名和moudle
   * @param request
   * @param response
   * @see yh.subsys.inforesouce.logic.YHAFileMateLogic#findFileNameAndMoudle(Connection, String)
   * @return "/core/inc/rtjson.jsp"
   * @throws Exception
   */
  public String findFileNameAndMoudle(HttpServletRequest request, HttpServletResponse response) throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      String attachmentId = request.getParameter("attachmentId");//文件Id
      YHAFileMateLogic alogic = new YHAFileMateLogic();
      String param = alogic.findFileNameAndMoudle(dbConn, attachmentId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);     
      request.setAttribute(YHActionKeys.RET_DATA,  param );
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    } 
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 返回文件的人名地名组织机构名
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getAFileMate(HttpServletRequest request, HttpServletResponse response)throws Exception{
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    Connection dbConn = null;
    try{
      dbConn = requestDbConn.getSysDbConn();
      String attachmentId = request.getParameter("attachmentId");//文件Id
      YHAFileMateLogic alogic = new YHAFileMateLogic();
      String json = alogic.mateJson(dbConn, attachmentId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "");
      request.setAttribute(YHActionKeys.RET_DATA,  json );
    } catch (Exception ex){
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
