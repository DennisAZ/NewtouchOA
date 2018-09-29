package yh.core.esb.frontend.oa;

import java.rmi.RemoteException;

import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;

import yh.core.esb.client.service.OAWebservice;
import yh.core.esb.common.util.ClientPropertiesUtil;
import yh.core.esb.common.util.YHEsbUtil;

public class YHESBMessageServiceCaller {
	private static Logger log = Logger.getLogger(YHESBMessageServiceCaller.class);

	public String recvMessage(String filePath, String guid, String fromId) throws Exception {
	  String isLocal = ClientPropertiesUtil.getProp("isLocal");
    if ("1".equals(isLocal)) {
      OAWebservice oa = new OAWebservice();
      return oa.recvMessage(filePath, guid, fromId);
    } else {
  	  try {
  			String serviceUrl = ClientPropertiesUtil.getWebServiceUri().replace("?WSDL", "");
  			Service service = new Service(); 
  			Call call = (Call) service.createCall(); 
  			call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
  			call.setOperationName("recvMessage"); 
  			call.addParameter("filePath", XMLType.XSD_STRING, ParameterMode.IN); 
  			call.addParameter("guid", XMLType.XSD_STRING, ParameterMode.IN);
  			call.addParameter("fromId", XMLType.XSD_STRING, ParameterMode.IN);
  			call.setReturnType(XMLType.XSD_STRING); 
  			String ret = (String) call.invoke(new Object[] {filePath, guid, fromId});
  			YHEsbUtil.println(ret);
  			return ret;
  		} catch (Exception e) {
  			//e.printStackTrace();
  			log.error("recvMessage - 调用web服务异常,异常信息:" + e.getMessage());
  			return "web服务异常:" + e.getMessage();
  		}
    }
	}

  /**
   * 
   * @param guid
   * @param state 1 -上传成功 -2 文件发送失败! 2 对方文件下载成功
   * @param to
   * @return
   * @throws RemoteException
   */
	public String updateState(String guid, int state, String to) throws RemoteException {
	  String isLocal = ClientPropertiesUtil.getProp("isLocal");
	  if ("1".equals(isLocal)) {
	    OAWebservice oa = new OAWebservice();
	    return oa.updateState(guid, state, to);
	  } else {
	    try {
	      String serviceUrl = ClientPropertiesUtil.getWebServiceUri().replace("?WSDL", "");
	      Service service = new Service(); 
	      Call call = (Call) service.createCall(); 
	      call.setTargetEndpointAddress(new java.net.URL(serviceUrl)); 
	      call.setOperationName("updateState"); 
	      call.addParameter("guid", XMLType.XSD_STRING, ParameterMode.IN); 
	      call.addParameter("state", XMLType.XSD_INT, ParameterMode.IN);
	      call.addParameter("to", XMLType.XSD_STRING, ParameterMode.IN);
	      call.setReturnType(XMLType.XSD_STRING); 
	      String ret = (String) call.invoke(new Object[] {guid, state, to});
	      YHEsbUtil.println(ret);
	      return ret;
	    } catch (Exception e) {
	      //e.printStackTrace();
	      log.error("updateState - 调用web服务异常,异常信息:" + e.getMessage());
	      return "web服务异常:" + e.getMessage();
	    }
	  }
  }
}
