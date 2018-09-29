package yh.core.esb.client.service;

import java.io.File;
import java.sql.Connection;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;

import yh.core.esb.client.data.YHDocSendMessage;
import yh.core.esb.client.data.YHEsbConst;
import yh.core.esb.client.data.YHEsbMessage;
import yh.core.esb.client.logic.YHDeptTreeLogic;
import yh.core.esb.client.logic.YHEsbClientUtility;
import yh.core.esb.client.logic.YHObjectUtility;
import yh.core.esb.client.update.logic.YHUpdateClientLogic;
import yh.core.esb.server.update.logic.YHUpdateServerLogic;
import yh.core.funcs.doc.send.logic.YHDocSendLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;
import yh.subsys.jtgwjh.docReceive.logic.YHUnZipRarFile;
import yh.subsys.jtgwjh.notifyManage.logic.YHNotifyZipRarFile;
import yh.subsys.jtgwjh.sealmanage.logic.YHJhSealLogic;
import yh.subsys.jtgwjh.task.logic.YHJhTaskLogLogic;
import yh.user.api.core.db.YHDbconnWrap;

public class OAWebservice {
  public  String updateState(String guid, int state, String to) {
    Connection dbConn = null;
    try {
      YHDbconnWrap dbUtil = new YHDbconnWrap();
      dbConn = dbUtil.getSysDbConn();
      
      //更新公章、组织部门、公告同步状态
      String updateCount = YHJhTaskLogLogic.updateSealStatus(dbConn, guid, state, to);//返回空则不是公章、组织部门、公告
      
      if(YHUtility.isNullorEmpty(updateCount)){//更新公文发送状态
        updateCount = yh.subsys.jtgwjh.docSend.logic.YHDocSendLogic.updateSendDocStatus(dbConn, guid, state, to);//返回空则不是公文发送状态回执，不停
      }
      if(YHUtility.isNullorEmpty(updateCount)){//其他
    	  updateCount = YHUpdateServerLogic.updateUpdateStatus(dbConn,guid,state,to);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      YHDBUtility.closeDbConn(dbConn, null);
    }
    return "state: " + guid + " -- " + state;
  }
  
  public String recvMessage(String filePath, String guid, String from) throws Exception {
    File file = new File(filePath);
    String fileName = file.getName();
    if (fileName.endsWith("xml")) {
      YHDbconnWrap dbUtil = new YHDbconnWrap();
      Connection dbConn = dbUtil.getSysDbConn();
      if(fileName.startsWith("JHMESSAGEDATA")){//公文签收
        yh.subsys.jtgwjh.docSend.logic.YHDocSendLogic.parseXML(filePath, dbConn, from, guid);
      }else if(fileName.startsWith("JH_SYN_SEAL_")){//更新公章
        YHJhSealLogic.parseXML(filePath, dbConn, from, guid);
      }else if(fileName.startsWith("JH_UPDATE_")){//系统更新
    	YHUpdateServerLogic.paseXML(filePath,dbConn,from,guid);
      }
      else{
        StringBuffer sb = YHFileUtility.loadLine2Buff(filePath);
        YHEsbMessage message = YHEsbMessage.xmlToObj(sb.toString());
        if (YHEsbConst.SYS_DEPT.equals(message.getMessage())) {
          YHDeptTreeLogic logic = new YHDeptTreeLogic();
          logic.updateDept(message.getData());
          return null;
        }
      }
    
    }
    if (fileName.endsWith("zip")) {
      try {
        Map<String , ByteArrayBuffer> map = YHEsbClientUtility.getFileList(filePath);
        ByteArrayBuffer bb = map.get(YHEsbMessage.KEY_MESSAGE_FILE);
        if (bb != null) {
          String xml  = new String(bb.toByteArray());
          YHEsbMessage message = YHEsbMessage.xmlToObj(xml);
          if (message.getMessage().equals(YHDocSendMessage.KEY_SEND_DOC_MESSAGE)) {
            YHDocSendMessage dsm = (YHDocSendMessage) YHObjectUtility.readObject(message.getData());
            YHDocSendLogic docSendLogic = new YHDocSendLogic();
            docSendLogic.receiveFormEsb(dsm , map.get(dsm.getDocName()));
          }
        }
        else{
         System.out.println(fileName);
          if(fileName.startsWith("JHDATA_")){//公文接收
            YHUnZipRarFile.unZipFileXml(filePath,from,guid);
          }else if(fileName.startsWith("JHNOTIFY_")){//公告接收
            YHNotifyZipRarFile.unZipFileXml(filePath,from,guid);
          }else if(fileName.startsWith("ServerUpdate_")){   //升级包接收
        	  YHUpdateClientLogic.unZipFileXml(filePath,from,guid);
          }
        }
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
    return "RECVOK" + guid;
  }
}
