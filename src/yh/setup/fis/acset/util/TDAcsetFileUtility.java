//Source file: D:\\project\\td_erp\\src\\com\\td\\acctset\\estab\\TDFileIODisposal.java

/*
 * 创建日期 2006-8-15
 * author cly
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package yh.setup.fis.acset.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.setup.fis.acset.data.TDCCodeRecord;
/**
 * @author cly
 * 
 */
public class TDAcsetFileUtility {
  
  private static String DECIMAL = "DECIMAL";
  private  String B_TYPES[] = null;
  
  // log
  private static Logger log = Logger
      .getLogger("chly.com.td.acset.business.TDAcsetFile");

  /**
   * 读取文本文件全部内容
   * @param strFileName -
   *          文件路径+文件名称
   * @param strFileBuffer -
   *          读取的文件内容
   * @return Boolean
   * @roseuid 4428CFBC0167
   */
  public static StringBuffer readTextFile2Buffer(
      String acctYear,
      int rateLength,
      int rateDecimalLength,
      int moneyLength,
      int moneyDecimalLength,
      String strFileName,
      String B_TYPES[]) {
    // BufferedInputStream in = null;
    InputStreamReader in = null;
    LineNumberReader lineReader = null;
    StringBuffer strBuffer = new StringBuffer("");

    try {
      in = new InputStreamReader(new FileInputStream(strFileName), "UTF-8");
      lineReader = new LineNumberReader(in); 
      
      char[] buff = new char[1024];
      int length = 0;


      String strTemp = null;
      while ((strTemp = lineReader.readLine()) != null) {
        
        
        strBuffer.append(" \r\n ");
        int posDecimal = 0;
        int posLeft = 0;
        int posRight = 0;
        String strTempLeft = null;
        String strTempRight = null;
        String strTempMiddle = null;
        //insert语句中可能有敏感词，需要排除
        if(strTemp.toUpperCase().indexOf(DECIMAL) >= 0 && strTemp.toUpperCase().indexOf("INSERT") < 0){
          posLeft = strTemp.indexOf("(");
          strTempLeft = strTemp.substring(0, posLeft);
          posRight = strTemp.indexOf(")");
          strTempRight = strTemp.substring(posRight + 1);          
          for(int i = 0; i < B_TYPES.length; i++ ){
            if( strTemp.toUpperCase().indexOf(B_TYPES[i]) >= 0 ){
              strTempMiddle = " (" + rateLength + ", " + rateDecimalLength + ") ";
            }
          }
          if (YHUtility.isNullorEmpty(strTempMiddle)){
            strTempMiddle = " (" + moneyLength + ", " + moneyDecimalLength + ") ";
          }
          strTemp = strTempLeft + strTempMiddle + strTempRight; 
        }
        
        strBuffer.append(strTemp);
      }
      strBuffer.append(" \r\n ");
      
      
      
//      
//        strBuffer.append(" \r\n ");
//        
////        if(strTemp.indexOf("\\*, \\*A\\*)")>0) {
////          if (log.isDebugEnabled()) {
////            log.debug("come to readTextFile2Buffer   : " + "(" + moneyLength + ", " + moneyDecimalLength + ")");
////          }          
////        }
//
//        //替换  (*, *A*)替换金额精度 (*, *B*)替换汇率精度
//        
//        
//        strTemp=strTemp.replaceAll("\\*, \\*A\\*","" + moneyLength + ", " + moneyDecimalLength + "");        
//        strTemp=strTemp.replaceAll("\\*, \\*B\\*","" + rateLength + ", " + rateDecimalLength + "");     
////已经被替换,先插入,再按照配置文件,对有AcctYear的表进行更新.
////        strTemp=strTemp.replaceAll("\\*YEAR\\*", "" + acctYear + "");   
//        
//        strBuffer.append(strTemp);
//      }
//      strBuffer.append(" \r\n ");
    } catch (Exception ex) {
//      if (log.isDebugEnabled()) {
//        log.debug("come to Exception   : " +ex );
//      }    
    } finally {
      
//      if (log.isDebugEnabled()) {
//        log.debug("come to end  readTextFile2Buffer error  : "  );
//      }    
      
      try {
        if (in != null) {
          in.close();
        }
      } catch (Exception ex) {
      }
    }
//    if (log.isDebugEnabled()) {
//      log.debug("come to end  readTextFile2Buffer  : "  );
//    }    
    return strBuffer;
  }

  /**
   * 写入文本文件全部内容
   * 
   * @param strFileName -
   *          文件路径+文件名称
   * @param strFileBuffer -
   *          写入内容
   * @return String
   * @roseuid 4428D0D801D4
   */
  public static boolean writeTextFileFromBuffer(String strFileName,
      StringBuffer strFileBuffer) {
    OutputStreamWriter out = null;

    try {      
      out = new OutputStreamWriter(new FileOutputStream(strFileName), "UTF-8");
      out.write(strFileBuffer.toString(), 0, strFileBuffer.length());
      out.close();
    } catch (Exception ex) {
      return false;
    }finally {
      try {
        if (out != null) {
          out.close();
        }
      }catch(Exception ex) {        
      }
    }
    return true;
  }

  /**
   * 
   * @param str
   * @return
   */
  public static String getStr(String str) {
    try {
      String temp_p = str;
      byte[] temp_t = temp_p.getBytes("ISO-8859-1");
      String temp = new String(temp_t);
      return temp;
    } catch (Exception e) {
    }
    return "";
  }
  
  /**
   * 创建以指定目录为根节点的目录树
   * @param dirPath     指定目录
   * @nodeList          目录树的前序遍历
   * @return
   */
  public static void buildFileTree(File file,
      ArrayList nodeList,
      String code,
      String parentPath,
      boolean branchOnly) {

    if (parentPath == null) {
      parentPath = "";
    }
    String path = parentPath + "/" + file.getName();
    if (!branchOnly || (branchOnly && file.isDirectory())) {
      TDCCodeRecord codeRecord = new TDCCodeRecord();
      codeRecord.setCode(code);
      codeRecord.setDesc(file.getName());
      codeRecord.setName(file.getName());
      codeRecord.setFieldValue(path);
      if (code.indexOf("-") > 0) {
        int tmpIndex = code.lastIndexOf("-");
        String parentCode = code.substring(0, tmpIndex);
        codeRecord.setParentCode(parentCode);
      }else {
        codeRecord.setParentCode("-1");
      }
      nodeList.add(codeRecord);
    }
    
    if (file.isDirectory()) {
      File[] subFileArray = file.listFiles();
      
      for (int i = 0; i < subFileArray.length; i++) {
        File subFile = subFileArray[i];
        buildFileTree(subFile, nodeList, code + "-" + i, path, branchOnly);
      }
    }
  }

}
