package yh.core.funcs.doc.receive.data;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;

public class YHDocConst{
   public static String filePath = YHSysProps.getAttachPath() + File.separator+  YHWorkFlowConst.MODULE;
   public static String configPath = YHWorkFlowConst.MODULE_CONTEXT_PATH + "/workflowUtility/doc_config.properties";
   public static String remindType = "70";
   /**
    * 收文类型
    */
   public static String SECRET_GRADE = "SECRET_GRADE";
   public static String DOC_TYPE = "REC_DOC_TYPE";
   public static String DOC_RECEIVE_FLOWTYPE = "DOC_RECEIVE_FLOWTYPE";
   public static String DOC_TITLE = "DOC_TITLE";
   public static String DOC_RECEIVE_FLOW_SORT = "DOC_RECEIVE_FLOW_SORT";
   public static String DOC_SEND_FLOW_SORT = "DOC_SEND_FLOW_SORT";
   public static String SEND_DOC_NUM = "SEND_DOC_NUM";
   public static String DOC_SEND_UNIT = "DOC_SEND_UNIT";
   
   
   public static String getProp(String webrootPath , String key) throws Exception {
     Properties p = new Properties();
     String strs = "";
     try {
       p.load(new InputStreamReader(new FileInputStream(new File(webrootPath + configPath)) , "UTF-8"));
       strs = p.getProperty(key);
     } catch (Exception e) {
       throw e;
     } 
     return strs;
   }
   public static String[] parseStr2Arr(String data){
     if(!YHUtility.isNullorEmpty(data)){
       String[] d = data.split(",");
       return d;
     }
     return null;
   }
}
