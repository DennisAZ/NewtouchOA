package yh.core.util.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.crypto.Cipher;

import sun.misc.BASE64Encoder;
import yh.core.data.YHAuthKeys;
import yh.core.exps.YHInvalidParamException;
import yh.core.global.YHConst;
import yh.core.global.YHTokenConst;
import yh.core.util.YHSecurityUtility;
import yh.core.util.YHUtility;

public class YHFileUtility {
  /**
   * 扩展哈希表
   */
  private static Map extMap = new HashMap();
  
  /**
   * 设置扩展
   * @param key
   * @param value
   */
  public static void setExt(Object key, Object value) {
    extMap.put(key, value);
  }
  /**
   * 清除扩展
   */
  public static void removeExt(Object key) {
    extMap.remove(key);
  }
  
  /**
   * 拼接文件路径和文件名称
   * @param path
   * @param name
   * @return         全路径名称
   */
  public static String appendFileName(String path, String name) {
    if (YHUtility.isNullorEmpty(path)) {
      return name;
    }
    String lastChar = path.substring(path.length() - 1);
    if (lastChar.equals(YHTokenConst.FILE_SEPERATOR)) {
      return path + name;
    }
    return path + YHTokenConst.FILE_SEPERATOR + name;
  }
  
  /**
   * 把文件中的内容读入数组，每行作为一个元素
   * @param file
   * @return
   * @throws Exception
   */
  public static void loadLine2Array(String fileName,
      int startLine, int endLine, List rtList) throws Exception {
    loadLine2Array(fileName,
        startLine, endLine, rtList, YHConst.DEFAULT_CODE);
  }
  
  /**
   * 把文件中的内容读入数组，每行作为一个元素
   * @param file
   * @return
   * @throws Exception
   */
  public static void loadLine2Array(String fileName,
      int startLine, int endLine, List rtList, String charSet) throws Exception {
    
    InputStream ins = null;
    try {
      File file = new File(fileName);
      if (!file.exists()) {
        return;
      }
      ins = new FileInputStream(file);
      loadLine2Array(ins, startLine, endLine, rtList, charSet);
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (ins != null) {
          ins.close();
        }
      }catch(Exception ex) {
      }
    }
  }
  
  /**
   * 把文件中的内容读入数组，每行作为一个元素
   * @param file
   * @return
   * @throws Exception
   */
  public static void loadLine2Array(String fileName,
      int startLine, List rtList) throws Exception {
    
    loadLine2Array(fileName, startLine, Integer.MAX_VALUE, rtList, YHConst.DEFAULT_CODE);
  }

  /**
   * 把文件中的内容读入数组，每行作为一个元素
   * @param file
   * @return
   * @throws Exception
   */
  public static void loadLine2Array(String fileName, List rtList) throws Exception {
    loadLine2Array(fileName, 0, Integer.MAX_VALUE, rtList, YHConst.DEFAULT_CODE);
  }
  
  /**
   * 把文件中的内容读入数组，每行作为一个元素
   * @param file
   * @return
   * @throws Exception
   */
  public static void loadLine2Array(String fileName, List rtList, String charSet) throws Exception {
    loadLine2Array(fileName, 0, Integer.MAX_VALUE, rtList, charSet);
  }
  
  /**
   * 把文件中的内容读入数组，每行作为一个元素
   * @param file
   * @return
   * @throws Exception
   */
  public static void storeString2File(String fileName, String content) throws Exception {
    OutputStream outs = null;
    try {
      File file = new File(fileName);  
      File outDir = file.getParentFile();
      if (!outDir.exists()) {
        outDir.mkdirs();
      }
      if (!file.exists()) {
        file.createNewFile();
      }
      if (!file.canWrite()) {
        file.setWritable(true);
      }
      outs = new FileOutputStream(file);
      storeString2File(outs, content, YHConst.DEFAULT_CODE);
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (outs != null) {
          outs.close();
        }
      }catch(Exception ex) {
      }
    }
  }
  
  /**
   * 把文件中的内容读入数组，每行作为一个元素
   * @param file
   * @return
   * @throws Exception
   */
  public static void storeArray2Line(String fileName, List rtList) throws Exception {
    storeArray2Line(fileName, rtList, YHConst.DEFAULT_CODE);
  }
  
  /**
   * 把文件中的内容读入数组，每行作为一个元素
   * @param file
   * @return
   * @throws Exception
   */
  public static void storeArray2Line(String fileName, List rtList, String charSet) throws Exception {
    OutputStream outs = null;
    try {
      File file = new File(fileName);
      File outDir = file.getParentFile();
      if (!outDir.exists()) {
        outDir.mkdirs();
      }
      if (!file.exists()) {
        file.createNewFile();
      }
      if (!file.canWrite()) {
        file.setWritable(true);
      }
      outs = new FileOutputStream(file);
      storeArray2Line(outs, rtList, charSet);
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (outs != null) {
          outs.close();
        }
      }catch(Exception ex) {
        //System.out.println(ex.getMessage());
      }
    }
  }
  
  /**
   * 从文件输出到流
   * @param file
   * @param out
   */
  public static void copyFile(String file, OutputStream out) {
    InputStream in = null;
    try {      
      in = new FileInputStream(file);
      byte[] buff = new byte[1024];
      int readLength = 0;
      while ((readLength = in.read(buff)) > 0) {        
         out.write(buff, 0, readLength);
      }
      out.flush();
    }catch(Exception ex) {
      ex.printStackTrace();
    }finally {
      try {
        if (in != null) {
          in.close();
        }
      }catch(Exception ex) {
        ex.printStackTrace();
      }
    }
  }
  
  /**
   * 拷贝文件后删除   * @param srcFile
   * @param destFile
   * @throws Exception
   */
  public static void xcopyFile(String srcFile, String destFile) throws Exception {
    copyFile(srcFile, destFile, null, true, false);    
  }
  
  /**
   * 拷贝文件
   * @param srcFile    源文件   * @param destFile   目标文件
   */
  public static void copyFile(String srcFile, String destFile) 
    throws Exception {
    copyFile(srcFile, destFile, null, false, false);
  }
  
  /**
   * 拷贝文件
   * @param srcFile    源文件   * @param destFile   目标文件
   */
  private static void copyFile(
      String srcFile, 
      String destFile, 
      List msrgList,
      boolean isDelete,
      boolean setModifyTime) throws Exception {
    
    copyFile0(srcFile, destFile, msrgList, isDelete, setModifyTime, null, null, 0, 0);
  }
  
  /**
   * 拷贝文件
   * @param srcFile    源文件   * @param destFile   目标文件
   */
  public static void encryptFile(
      String srcFile) throws Exception {
    String destFile = srcFile + "_encrypt";
    copyFileEncrypt(srcFile, destFile);
    new File(srcFile).delete();
    new File(destFile).renameTo(new File(srcFile));
  }
  /**
   * 拷贝文件
   * @param srcFile    源文件   * @param destFile   目标文件
   */
  public static void decryptFile(
      String srcFile) throws Exception {
    String destFile = srcFile + "_decrypt";
    copyFileDecrypt(srcFile, destFile);
    new File(srcFile).delete();
    new File(destFile).renameTo(new File(srcFile));
  }
  
  /**
   * 拷贝文件
   * @param srcFile    源文件   * @param destFile   目标文件
   */
  public static void copyFileEncrypt(
      String srcFile, 
      String destFile) throws Exception {
    
    copyFile0(
        srcFile,
        destFile,
        null,
        false,
        false,
        new BASE64Encoder().encode(getFileName(srcFile).getBytes()).toCharArray(),
        YHAuthKeys.getSalt(null),
        YHAuthKeys.getItCnt(null),
        Cipher.ENCRYPT_MODE);
  }
  
  /**
   * 拷贝文件
   * @param srcFile    源文件   * @param destFile   目标文件
   */
  public static void copyFileDecrypt(
      String srcFile, 
      String destFile) throws Exception {
    
    copyFile0(
        srcFile,
        destFile,
        null,
        false,
        false,
        new BASE64Encoder().encode(getFileName(srcFile).getBytes()).toCharArray(),
        YHAuthKeys.getSalt(null),
        YHAuthKeys.getItCnt(null),
        Cipher.DECRYPT_MODE);
  }
      
  
  /**
   * 拷贝文件
   * @param srcFile    源文件   * @param destFile   目标文件
   */
  private static void copyFile0(
      String srcFile, 
      String destFile, 
      List msrgList,
      boolean isDelete,
      boolean setModifyTime,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {    
    
    InputStream ins = null;
    OutputStream outs = null;
    File inFile = null;
    try {
      inFile = new File(srcFile);
      File outFile = new File(destFile);
      
      if (!inFile.exists()) {
        return;
      }
      
      File outDir = outFile.getParentFile();
      if (!outDir.exists()) {
        outDir.mkdirs();
      }
      
      if (!outFile.exists()) {
        outFile.createNewFile();
      }
      if (!outFile.canWrite()) {
        outFile.setWritable(true);
      }
      ins = new FileInputStream(inFile);
      if (passWord != null) {
        Cipher cipher = (Cipher)extMap.get("cipher");
        if (cipher != null) {
          ins = YHSecurityUtility.buildPassWordInputStream(cipher, ins);
        }else {
          ins = YHSecurityUtility.buildPassWordInputStream(
              passWord, salt, itCnt, mode, ins);
        }
      }
      outs = new FileOutputStream(outFile);
      
      //输出消息列表
      if (msrgList != null) {
        msrgList.add("Copy " + srcFile + " to " + destFile);
      }
      
      byte[] buff = new byte[YHConst.K];
      int readLength = 0;
      while ((readLength = ins.read(buff)) > 0) {
        outs.write(buff, 0, readLength);
        outs.flush();
      }
      ins.close();
      outs.close();

      if (setModifyTime) {
        outFile.setLastModified(inFile.lastModified());
      }
      if (isDelete && inFile.exists()) {
        //输出消息列表
        if (msrgList != null) {
          msrgList.add(srcFile + " is deleted after being copyed");
        }
        inFile.delete();
      }      
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (ins != null) {
          ins.close();
        }
      }catch(Exception ex) {
        //System.out.println(ex.getMessage());
      }
      try {
        if (outs != null) {
          outs.close();
        }
      }catch(Exception ex) {
        //System.out.println(ex.getMessage());
      }      
    }
  }
  
  /**
   * 把输入流保存到文件   * @param srcFile    源文件   * @param destFile   目标文件
   */
  public static void storeFileFromStream(
      InputStream ins, 
      String destFile)throws Exception {
    storeFileFromStream(ins, destFile, null,
        null,
        -1,
        -1);
  }
  
  /**
   * 把输入流保存到文件   * @param srcFile    源文件   * @param destFile   目标文件
   */
  public static void storeFileFromStreamDecrypt(
      String pass,
      InputStream ins, 
      String destFile)throws Exception {
    storeFileFromStream(ins, destFile,
        new BASE64Encoder().encode(pass.getBytes()).toCharArray(),
        YHAuthKeys.getSalt(null),
        YHAuthKeys.getItCnt(null),
        Cipher.DECRYPT_MODE);
  }
  
  /**
   *  把字节数组保存到文件
   * @param fileName
   * @param srcBuf
   * @throws Exception
   */
  public static void storBytes2File(String fileName,
      byte[] srcBuf) throws Exception {
    storBytes2File(fileName, srcBuf, 0, srcBuf.length);
  }
  
  /**
   *  把字节数组保存到文件
   * @param fileName
   * @param srcBuf
   * @throws Exception
   */
  public static void storBytes2File(String fileName,
      byte[] srcBuf, int offset, int length) throws Exception {
    OutputStream outs = null;
    try {
      File outFile = new File(fileName);
      File outDir = outFile.getParentFile();
      if (!outDir.exists()) {
        outDir.mkdirs();
      }
      if (!outFile.exists()) {
        outFile.createNewFile();
      }
      if (!outFile.canWrite()) {
        outFile.setWritable(true);
      }
      outs = new FileOutputStream(outFile);
      
      outs.write(srcBuf, offset, length);
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (outs != null) {
          outs.close();
        }
      }catch(Exception ex) {
        //System.out.println(ex.getMessage());
      }      
    }
  }
  
  /**
   * 把输入流保存到文件   * @param srcFile    源文件   * @param destFile   目标文件
   */
  public static void storeFileFromStream(
      InputStream ins, 
      String destFile,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {    
    
    OutputStream outs = null;
    try {
      File outFile = new File(destFile);
      
      File outDir = outFile.getParentFile();
      if (!outDir.exists()) {
        outDir.mkdirs();
      }      
      if (!outFile.exists()) {
        outFile.createNewFile();
      }
      if (!outFile.canWrite()) {
        outFile.setWritable(true);
      }
      if (passWord != null) {
        ins = YHSecurityUtility.buildPassWordInputStream(
            passWord, salt, itCnt, mode, ins);
      }
      outs = new FileOutputStream(outFile);
      
      byte[] buff = new byte[YHConst.K];
      int readLength = 0;
      while ((readLength = ins.read(buff)) > 0) {
        outs.write(buff, 0, readLength);
        outs.flush();
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (outs != null) {
          outs.close();
        }
      }catch(Exception ex) {
        //System.out.println(ex.getMessage());
      }      
    }
  }
  
  /**
   * 提取更新文件
   * @param srcDirName           源文件目录   * @param destDirName          目标文件输出目录
   * @param fileFilter           文件过滤对象
   * @param msrgList             消息列表
   * @param cpDateStr            比较时间
   * @throws Exception
   */
  public static void extractUpdateFile(
      String srcDirName,
      String destDirName,
      List msrgList,
      String cpDateStr) throws Exception {
    
    Date cpDate = YHUtility.parseDate(cpDateStr);
    copyDir(srcDirName, destDirName, msrgList,
        new YHTimeFileFilter(cpDate, true),
        false, true);
  }
  
  /**
   * 目录拷贝
   * @param srcDirName           源文件目录名
   * @param destDirName          目标输出目录名   * @param msrgList             消息列表
   * @param filter               文件过滤器   * @throws Exception
   */
  public static void copyDirCiph(String srcDirName,
      String destDirName, 
      List msrgList,
      FileFilter filter,
      boolean copyEmptyDir,
      boolean setModifyTime,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {
    
    copyDirImp0(srcDirName, destDirName, msrgList,
        filter, false, copyEmptyDir, setModifyTime,
        passWord, salt, itCnt, mode);
  }
  
  /**
   * 目录拷贝
   * @param srcDirName           源文件目录名
   * @param destDirName          目标输出目录名
   * @throws Exception
   */
  public static void copyDir(String srcDirName,
      String destDirName) throws Exception {
    
    copyDirImp(srcDirName, destDirName, null, null, false, true, true);
  }
  
  /**
   * 目录拷贝
   * @param srcDirName           源文件目录名
   * @param destDirName          目标输出目录名
   * @param msrgList             消息列表
   * @param filter               文件过滤器   * @throws Exception
   */
  public static void copyDir(String srcDirName,
      String destDirName, 
      List msrgList,
      FileFilter filter,
      boolean copyEmptyDir,
      boolean setModifyTime) throws Exception {
    
    copyDirImp(srcDirName, destDirName, msrgList, filter, false, copyEmptyDir, setModifyTime);
  }
  
  /**
   * 目录拷贝，删除源文件
   * @param srcDirName           源文件目录名
   * @param destDirName          目标输出目录名
   * @param msrgList             消息列表
   * @param filter               文件过滤器
   * @throws Exception
   */
  public static void xcopyDir(String srcDirName,
      String destDirName, 
      List msrgList,
      FileFilter filter,
      boolean copyEmptyDir,
      boolean setModifyTime) throws Exception {
    
    copyDirImp(srcDirName, destDirName, msrgList, filter, true, copyEmptyDir, setModifyTime);
  }
  
  /**
   * 目录拷贝
   * @param srcDirName           源文件目录名
   * @param destDirName          目标输出目录名
   * @param msrgList             消息列表
   * @param filter               文件过滤器
   * @throws Exception
   */
  private static void copyDirImp(String srcDirName,
      String destDirName, 
      List msrgList,
      FileFilter filter,
      boolean isDelete,
      boolean copyEmptyDir,
      boolean setModifyTime) throws Exception {
    
    copyDirImp0(srcDirName, destDirName, msrgList, filter, isDelete, copyEmptyDir, setModifyTime, null, null, 0, 0);
  }
 
  /**
   * 目录拷贝
   * @param srcDirName           源文件目录名
   * @param destDirName          目标输出目录名
   * @param msrgList             消息列表
   * @param filter               文件过滤器   * @throws Exception
   */
  private static void copyDirImp0(String srcDirName,
      String destDirName, 
      List msrgList,
      FileFilter filter,
      boolean isDelete,
      boolean copyEmptyDir,
      boolean setModifyTime,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {
    
    File srcDir = new File(srcDirName);
    if (!srcDir.exists() || !srcDir.isDirectory()) {
      throw new YHInvalidParamException("源文件目录输入不正确！");
    }
    
    String[] fileNameArray = srcDir.list();
    for (int i = 0; i < fileNameArray.length; i++) {
      String srcFileName = appendFileName(srcDirName, fileNameArray[i]);
      String destFileName = appendFileName(destDirName, fileNameArray[i]);
      File srcFile = new File(srcFileName);
      if (srcFile.isFile()) {
        if (filter == null) {
          if (passWord == null) {
            copyFile(srcFileName, destFileName, msrgList, isDelete, setModifyTime);
          }else {
            copyFile0(srcFileName, destFileName, msrgList, isDelete, setModifyTime, passWord, salt, itCnt, mode);
          }
        }else if (filter.accept(srcFile)) {
          copyFile0(srcFileName, destFileName, msrgList, isDelete, setModifyTime, passWord, salt, itCnt, mode);
        }
      }else if (srcFile.isDirectory()) {
        String[] subDirFileArray = srcFile.list();
        if (subDirFileArray.length < 1) {
          if (copyEmptyDir) {
            new File(destFileName).mkdirs();
          }
        }else {
          copyDirImp0(srcFileName, destFileName, msrgList, filter, isDelete, copyEmptyDir, setModifyTime, passWord, salt, itCnt, mode);
          File destFile = new File(destFileName);
          String[] fileList = destFile.list();
          if (fileList == null || fileList.length < 1) {
            destFile.delete();
          }
        }
      }
    }    
  }
  
  /**
   * 把流中的内容读入到数组之中，每行作为一个元素   * @param in
   * @return
   * @throws Exception
   */
  public static void loadLine2Array(InputStream in, 
      int startLine,
      int endLine,
      List rtList,
      String charSet) throws Exception {
    
    LineNumberReader reader = new LineNumberReader(new InputStreamReader(in, charSet));
    String str = null;
    for (int i = 0; (str = reader.readLine()) != null; i++) {
      if (i < startLine) {
        continue;
      }
      if (i > endLine) {
        break;
      }
      rtList.add(str);
    }
  }
  
  /**
   * 把文件加载到字节数组
   * @param fileName
   */
  public static byte[] loadFile2Bytes(String fileName) throws Exception {
    InputStream in = null;
    try {
      File file = new File(fileName);
      if (!file.exists()) {
        return null;
      }
      in = new FileInputStream(fileName);
      int fileLength = (int)file.length();
      byte[] buff = new byte[fileLength];
      in.read(buff, 0, fileLength);
      
      return buff;
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (in != null) {
          in.close();
        }
      }catch(Exception ex) {        
      }
    }
  }
  
  /**
   * 把流中的内容读入到字符串缓冲对象中   * @param in                  数据流   * @param startLine           起始行号
   * @return
   * @throws Exception
   */
  public static StringBuffer loadLine2Buff(
      InputStream in,
      int startLine,
      int endLine,
      String encode) throws Exception {
    
    StringBuffer rtBuffer = new StringBuffer();
    
    encode = encode == null ? "UTF-8" : encode;
    LineNumberReader reader = new LineNumberReader(new InputStreamReader(in, encode));
    String str = null;
    for (int i = 0; (str = reader.readLine()) != null; i++) {
      if (i < startLine) {
        continue;
      }
      if (i > endLine) {
        break;
      }
      rtBuffer.append(str);
      rtBuffer.append("\n");
    }
    if (rtBuffer.length() > 1) {
      rtBuffer.delete(rtBuffer.length() - 1, rtBuffer.length());
    }
    
    return rtBuffer;
  }
  
  /**
   * 把文件中的内容读入数组，每行作为一个元素   * @param file
   * @param startLine           起始行号
   * @return
   * @throws Exception
   */
  public static StringBuffer loadLine2Buff(String fileName,
      int startLine,
      int endLine,
      String encode) throws Exception {
    
    StringBuffer rtBuffer = null;
    
    InputStream ins = null;
    try {
      File file = new File(fileName);
      if (!file.exists()) {
        return rtBuffer;
      }
      ins = new FileInputStream(file);
      rtBuffer = loadLine2Buff(ins, startLine, endLine, encode);
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (ins != null) {
          ins.close();
        }
      }catch(Exception ex) {
        //System.out.println(ex.getMessage());
      }
    }
    return rtBuffer;
  }
  
  /**
   * 把文件中的内容读入数组，每行作为一个元素
   * @param file
   * @param startLine           起始行号
   * @return
   * @throws Exception
   */
  public static StringBuffer loadLine2Buff(String fileName,
      int startLine, String encode) throws Exception {
    
    return loadLine2Buff(fileName, startLine, Integer.MAX_VALUE, encode);
  }
  /**
   * 把文件中的内容读入数组，每行作为一个元素
   * @param file
   * @param startLine           起始行号
   * @return
   * @throws Exception
   */
  public static StringBuffer loadLine2Buff(String fileName,
      int startLine) throws Exception {
    
    return loadLine2Buff(fileName, startLine, Integer.MAX_VALUE, YHConst.DEFAULT_CODE);
  }
  
  /**
   * 把文件中的内容读入数组，每行作为一个元素
   * @param file
   * @param startLine           起始行号
   * @return
   * @throws Exception
   */
  public static StringBuffer loadLine2Buff(String fileName, String encode) throws Exception {
    
    return loadLine2Buff(fileName, 0, Integer.MAX_VALUE, encode);
  }
  
  /**
   * 把文件中的内容读入数组，每行作为一个元素
   * @param file
   * @param startLine           起始行号
   * @return
   * @throws Exception
   */
  public static StringBuffer loadLine2Buff(String fileName) throws Exception {
    
    return loadLine2Buff(fileName, 0, Integer.MAX_VALUE, YHConst.DEFAULT_CODE);
  }
  
  /**
   * 加载属性文件到Map中   * @param fileName            属性文件路径
   * @param rtMap               返回哈希表   * @throws Exception
   */
  public static void load2Map(String fileName, Map rtMap) throws Exception {
    ArrayList propList = new ArrayList();
    
    loadLine2Array(fileName, propList);
    for (int i = 0; i < propList.size(); i++) {
      String line = propList.get(i).toString().trim();
      if (line.length() < 1 || line.startsWith("#")) {
        continue;
      }
      int tmpIndex = line.indexOf("=");
      if (tmpIndex < 0) {
        continue;
      }
      String name = line.substring(0, tmpIndex).trim();
      String value = line.substring(tmpIndex + 1).trim();
      rtMap.put(name, value);
    }
  }
  
  /**
   * 把数组中的内如输出到流中，每个元素作为一行
   * @param out
   * @param list
   * @throws Exception
   */
  public static void storeArray2Line(OutputStream out, List list, String charSet) throws Exception {
    if (list == null) {
      return;
    }
    OutputStreamWriter writer = new OutputStreamWriter(out, charSet);
    try {
      for (int i = 0; i < list.size(); i++) {      
        writer.write(list.get(i).toString());
        writer.write("\r\n");
        writer.flush();
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (writer != null) {
          writer.close();
        }
      }catch(Exception ex) {        
      }
    }
  }
  
  /**
   * 把数组中的内如输出到流中，每个元素作为一行
   * @param out
   * @param list
   * @throws Exception
   */
  public static void storeString2File(OutputStream out, String str, String charSet) throws Exception {
    if (str == null) {
      return;
    }
    
    OutputStreamWriter writer = null;
    try {
      if (charSet != null) {
        writer = new OutputStreamWriter(out, charSet);
      }else {
        writer = new OutputStreamWriter(out);
      }
      writer.write(str);
      writer.flush();
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (writer != null) {
          writer.close();
        }
      }catch(Exception ex) {        
      }
    }
  }
  
  /**
   * 取得文件不带扩展名部分的名称
   * @param filePath
   * @return
   */
  public static String getFileNameNoExt(String filePath) {
    if (filePath == null) {
      return null;
    }
    String rtName = getFileName(filePath);
    int pointIndex = rtName.lastIndexOf(YHFileConst.PATH_POINT);
    if (pointIndex < 0) {
      return rtName;
    }
    if (pointIndex == 0) {
     return ""; 
    }
    return rtName.substring(0, pointIndex);
  }
  
  /**
   * 取得文件扩展名
   * @return
   */
  public static String getFileExtName(String filePath) {
    if (filePath == null) {
      return "";
    }
    String rtName = getFileName(filePath);
    int pointIndex = rtName.lastIndexOf(YHFileConst.PATH_POINT);
    if (pointIndex < 0) {
      return "";
    }
    if (pointIndex == rtName.length() - 1) {
      return "";
    }
    return rtName.substring(pointIndex + 1, rtName.length());
  }
  
  /**
   * 取得文件名，带扩展名部分
   * @param filePath
   * @return
   */
  public static String getFileName(String filePath) {
    if (filePath == null) {
      return null;
    }
    int startIndex = 0;
    if (filePath.lastIndexOf(YHFileConst.PATH_SPLIT_FILE_WIN) >= 0) {
      startIndex = filePath.lastIndexOf(YHFileConst.PATH_SPLIT_FILE_WIN) + 1;
    }else if (filePath.lastIndexOf(YHFileConst.PATH_SPLIT_URL) >= 0) {
      startIndex = filePath.lastIndexOf(YHFileConst.PATH_SPLIT_URL) + 1;
    }
    
    return filePath.substring(startIndex, filePath.length());
  }
  
  /**
   * 取得文件的路径，不包含文件名
   * @return
   */
  public static String getFilePath(String fileName) {
    if (fileName == null) {
      return "";
    }
    int tmpIndex = -1;
    if (fileName.lastIndexOf(YHFileConst.PATH_SPLIT_FILE_WIN) >= 0) {
      tmpIndex = fileName.lastIndexOf(YHFileConst.PATH_SPLIT_FILE_WIN);
    }else if (fileName.lastIndexOf(YHFileConst.PATH_SPLIT_URL) >= 0) {
      tmpIndex = fileName.lastIndexOf(YHFileConst.PATH_SPLIT_URL);
    }
    if (tmpIndex < 0) {
      return "";
    }
    return fileName.substring(0, tmpIndex);
  }
  
  /**
   * 在文件中执行字符串替换
   * @param file
   * @param srcStr
   * @param destStr
   */
  public static void replaceInFile(
      String fileName,
      String srcStr,
      String destStr) throws Exception {
    
    List rules = new ArrayList();
    rules.add(new String[]{srcStr, destStr});
    
    replaceInFile(fileName, rules);
  }
  
  /**
   * 替换文件中的文本
   * @param fileName
   * @param rules
   * @throws Exception
   */
  public static void replaceInFile(
      String fileName,
      List rules) throws Exception {
    
    try {
      ArrayList lineList = new ArrayList();
      loadLine2Array(fileName, lineList);
      for (int i = 0; i < lineList.size(); i++) {
        String lineStr = (String)lineList.get(i);
        for (int j = 0; j < rules.size(); j++) {
          String[] ruleArray = (String[])rules.get(j);          
          if (lineStr.indexOf(ruleArray[0]) >= 0) {
            lineStr = lineStr.replace(ruleArray[0], ruleArray[1]);
            lineList.set(i, lineStr);
          }
        }
      }
      storeArray2Line(fileName, lineList);
    }catch(Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 从文件中加载到字节数组中
   * @param fileName
   * @param passWord
   * @param salt            null=不加密；!null=加密
   * @param itCnt           迭代次数
   * @param mode            解密/加密
   * @return
   * @throws Exception
   */
  public static byte[] loadByteArrayFromFile(
      String fileName,
      char[] passWord,
      byte[] salt,
      int itCnt,
      int mode) throws Exception {
    
    ByteArrayOutputStream out = null;
    InputStream in = null; 
    try {
      in = new BufferedInputStream(
          new FileInputStream(fileName));
      if (salt != null) {
        in = YHSecurityUtility.buildPassWordInputStream(
            passWord, salt, itCnt, mode, in);
      }
      
      byte[] buff = new byte[YHConst.DEFAULT_IO_BUFF_SIZE];
      int readLen = 0;
      out = new ByteArrayOutputStream();
      while ((readLen = in.read(buff)) > 0) {
        out.write(buff, 0, readLen);
        out.flush();
      }
      
      return out.toByteArray();
    }catch(Exception ex) {
      throw ex;
    }finally {
      try {
        if (in != null) {
          in.close();
        }
      }catch(Exception ex) {        
      }
      try {
        if (out != null) {
          out.close();
        }
      }catch(Exception ex) {        
      }
    }
  }
  
  /**
   * 删除指定文件或者目录
   * @param file
   */
  public static void deleteAll(String file) {
    deleteAll(new File(file));
  }
  
  /**
   * 删除指定文件或者目录
   * @param file
   */
  public static void deleteAll(File file) {
    if (file.isFile()) {
      file.delete();
      return;
    }
    File[] fileList = file.listFiles();
    for (int i = 0; fileList != null && i < fileList.length; i++) {
      deleteAll(fileList[i]);
    }
    file.delete();
  }
  
  /**
   * 删除文件列表
   * @param fileList
   * @throws Exception
   */
  public static void deleteFile(List fileList) throws Exception {
    deleteFile(fileList, null);
  }
  
  /**
   * 删除文件列表
   * @param fileList
   * @throws Exception
   */
  public static void deleteFile(List fileList, String rootDir) throws Exception {
    if (fileList == null || fileList.size() < 1) {
      return;
    }
    for (int i = 0; i < fileList.size(); i++) {
      String currPath = (String)fileList.get(i);
      if (rootDir != null) {
        currPath = rootDir + File.separator + fileList.get(i);
      }
      YHFileUtility.deleteAll(currPath);
    }
  }
  
  /**
   * 把Unix形式的文件路径转换成Windows形式的路径
   * @param fileName
   * @return
   */
  public static String windows2Unix(String fileName) {
    if (fileName == null) {
      return "";
    }
    return fileName.replace('\\', '/');
  }
  
  /**
   * 把Windows形式的文件路径转换成Unix形式的路径
   * @param fileName
   * @return
   */
  public static String unix2Windows(String fileName) {
    if (fileName == null) {
      return "";
    }
    return fileName.replace('/', '\\');
  }
  
  /**
   * 输出文件名称
   * @param  fileName
   * @param  outputFile
   */
  public static void outPutFileName(
      String fileName,
      String outputFile) throws Exception {
    
    ArrayList fileList = new ArrayList();
    outPutFileName(new File(fileName), fileList);
    
    if (outputFile != null) {
      storeArray2Line(outputFile, fileList);
    }else {
      for (int i = 0; i < fileList.size(); i++) {
        //System.out.println(fileList.get(i).toString());
      }
    }
  }
  
  /**
   * 输出文件
   * @param fileName
   */
  private static void outPutFileName(
      File file, ArrayList fileList) throws Exception {
    
    if (!file.exists()) {
      return;
    }
    if (file.isFile()) {
      fileList.add(file.getAbsolutePath());
    }else if (file.isDirectory()) {
      File[] files = file.listFiles();
      for (int i = 0; i < files.length; i++) {
        outPutFileName(files[i], fileList);
      }
    }
  }
  
  /**
   * 判断两个文件是否完全一致
   * @param file1         比较文件1
   * @param file2         比较文件2
   * @return
   */
  public static boolean isFileEqual(String file1, String file2) throws Exception {
    return isFileEqual(new File(file1), new File(file2));
  }
  
  /**
   * 判断两个文件是否完全一致
   * @param file1         比较文件1
   * @param file2         比较文件2
   * @return
   */
  public static boolean isFileEqual(File f1, File f2) throws Exception {
    boolean rtBool = false;
    InputStream in1 = null;
    InputStream in2 = null;
    
    try {
      in1 = new FileInputStream(f1);
      in2 = new FileInputStream(f2);
      if (f1.length() != f2.length()) {
        return false;
      }
      if (f1.lastModified() == f2.lastModified()) {
        return true;
      }
      byte[] buf1 = new byte[YHConst.K];
      byte[] buf2 = new byte[YHConst.K];
      for (int i = 0; i < YHConst.K; i++) {
        buf1[i] = 0;
        buf2[i] = 0;
      }
      while (true) {
        int len1 = in1.read(buf1);
        int len2 = in2.read(buf2);        
        if (len1 != len2) {
          return false;
        }
        if (len1 < 1) {
          break;
        }
        if (!Arrays.equals(buf1, buf2)) {
          return false;
        }
      }
      
      rtBool = true;
    }catch(Exception ex) {
      throw ex;
    }finally {
      if (in1 != null) {
        try {
          in1.close();
        }catch(Exception ex) {          
        }
      }
      if (in2 != null) {
        try {
          in2.close();
        }catch(Exception ex) {          
        }
      }
    }
    return rtBool;
  }
  
  /**
   * 从srcDir中Copy destDir中不存在的文件。例如srcDir/1.doc在destDir中不存在，则将1.docCopy到destDir中
   * @param srcDir
   * @param destdir
   * @throws Exception
   */
  public static void copyNotExists(String srcDir, String destDir, String outDir, List msrgList) throws Exception {
    if (YHUtility.isNullorEmpty(srcDir) || YHUtility.isNullorEmpty(destDir)) {
      return;
    }
    File srcDirFile = new File(srcDir);
    if (!srcDirFile.isDirectory() || !srcDirFile.exists()) {
      return;
    }
    
    String[] fileNameArray = srcDirFile.list();
    for (int i = 0; i < fileNameArray.length; i++) {
      String srcFilePath = srcDir + File.separator + fileNameArray[i];
      String destFilePath = destDir + File.separator + fileNameArray[i];
      String outFilePath = outDir + File.separator + fileNameArray[i];
      File currFile = new File(srcFilePath);
      File currDestFile = new File(destFilePath);
      if (currFile.isFile()) {        
        if (!currDestFile.exists()) {
          copyFile(srcFilePath, outFilePath);
          if (msrgList != null) {
            msrgList.add("Add file>>" + fileNameArray[i] + " to " + outDir);
          }
        }
      }else {
        copyNotExists(srcFilePath, destFilePath, outFilePath, msrgList);
      }
    }
  }
  
  /**
   * Copy与原目录中不同的文件
   * @param newDir                     新的需要发布的目录
   * @param oldDir                     原来的目录
   * @param outDir                     输出目录
   * @param msrgList                   输出消息
   * @throws Exception
   */
  public static void copyDirDiff(String newDir, 
      String oldDir, String outDir, List msrgList, boolean copyEmpyDir) throws Exception {
    YHFileDifFilter filter = new YHFileDifFilter(newDir, oldDir);
    copyDir(newDir, outDir, msrgList, filter, copyEmpyDir, true);
  }
  
  /**
   * 设置文件修改时间
   * @param fileName
   * @param time
   */
  public static void setLastModified(String fileName, long time) {
    if (YHUtility.isNullorEmpty(fileName)) {
      return;
    }
    File file = new File(fileName);
    setLastModified(file, time);
  }
  
  /**
   * 设置文件修改时间
   * @param fileName
   * @param time
   */
  public static void setLastModified(File file, long time) {
    if (!file.exists()) {
      return;
    }
    if (file.isFile()) {
      file.setLastModified(time);
    }else {
      File[] fileList = file.listFiles();
      for (int i = 0; i < fileList.length; i++) {
        setLastModified(fileList[i], time);
      }
    }
  }
  
  /**
   * 设置文件是否可写-是
   * @param fileName
   * @param time
   */
  public static void setWritable(String fileName) {
    if (YHUtility.isNullorEmpty(fileName)) {
      return;
    }
    File file = new File(fileName);
    setWritable(file, true);
  }
  /**
   * 设置文件是否可写
   * @param fileName
   * @param time
   */
  public static void setWritable(String fileName, boolean writable) {
    if (YHUtility.isNullorEmpty(fileName)) {
      return;
    }
    File file = new File(fileName);
    setWritable(file, writable);
  }
  
  /**
   * 设置文件修改时间
   * @param fileName
   * @param time
   */
  public static void setWritable(File file, boolean writable) {
    if (!file.exists()) {
      return;
    }
    if (file.isFile()) {
      file.setWritable(writable);
    }else {
      File[] fileList = file.listFiles();
      for (int i = 0; i < fileList.length; i++) {
        setWritable(fileList[i], writable);
      }
    }
  }
  
  /**
   * 取得目录下最大的文件名
   * @param dirDName           目录名
   * @return
   */
  public static String getMaxNameFile(String dirDName) {
    return getMaxNameFile(new File(dirDName), null);
  }
  /**
   * 取得目录下最小的文件名
   * @param dirDName           目录名
   * @return
   */
  public static String getMinNameFile(String dirDName) {
    return getMinNameFile(new File(dirDName), null);
  }
  
  /**
   * 取得目录下最大的文件名
   * @param dir           目录名
   * @return
   */
  public static String getMaxNameFile(File dir) {
    String[] fileArray = sortFileList(dir, null);
    if (fileArray == null || fileArray.length < 1) {
      return null;
    }
    return fileArray[0];
  }
  /**
   * 取得目录下最小的文件名
   * @param dir           目录名
   * @return
   */
  public static String getMinNameFile(File dir) {
    String[] fileArray = sortFileList(dir, false, null, null);
    if (fileArray == null || fileArray.length < 1) {
      return null;
    }
    return fileArray[0];
  }
  
  /**
   * 对某个目录下的文件或者子目录名称排序
   * @param dirDName        目录全路径名称
   * @return
   */
  public static String[] sortFileList(String dirDName) {
    return sortFileList(dirDName, true, null);
  }
  /**
   * 对某个目录下的文件或者子目录名称排序 
   * @param file               文件
   * @param direct             方向
   * @return
   */
  public static String[] sortFileList(File file) {
    return sortFileList(file, true, null, null);
  }
  
  /**
   * 对某个目录下的文件或者子目录名称排序
   * @param dirDName        目录全路径名称
   * @param isAsc          方向
   * @return
   */
  public static String[] sortFileList(String dirDName, boolean isAsc) {
    File file = new File(dirDName);
    return sortFileList(file, isAsc, null, null);
  }
  
  /**
   * 取得目录下最大的文件名
   * @param dirDName           目录名
   * @return
   */
  public static String getMaxNameFile(String dirDName, String prefix) {
    return getMaxNameFile(new File(dirDName), prefix);
  }
  /**
   * 取得目录下最小的文件名
   * @param dirDName           目录名
   * @return
   */
  public static String getMinNameFile(String dirDName, String prefix) {
    return getMinNameFile(new File(dirDName), prefix);
  }
  
  /**
   * 取得目录下最大的文件名
   * @param dir           目录名
   * @return
   */
  public static String getMaxNameFile(File dir, String prefix) {
    String[] fileArray = sortFileList(dir, prefix);
    if (fileArray == null || fileArray.length < 1) {
      return null;
    }
    return fileArray[0];
  }
  /**
   * 取得目录下最小的文件名
   * @param dir           目录名
   * @return
   */
  public static String getMinNameFile(File dir, String prefix) {
    String[] fileArray = sortFileList(dir, false, prefix, null);
    if (fileArray == null || fileArray.length < 1) {
      return null;
    }
    return fileArray[0];
  }
  
  /**
   * 对某个目录下的文件或者子目录名称排序
   * @param dirDName        目录全路径名称
   * @return
   */
  public static String[] sortFileList(String dirDName, String prefix) {
    return sortFileList(dirDName, true, prefix);
  }
  /**
   * 对某个目录下的文件或者子目录名称排序 
   * @param file               文件
   * @param direct             方向
   * @return
   */
  public static String[] sortFileList(File file, String prefix) {
    return sortFileList(file, true, prefix, null);
  }
  
  /**
   * 对某个目录下的文件或者子目录名称排序
   * @param dirDName        目录全路径名称
   * @param isAsc          方向
   * @return
   */
  public static String[] sortFileList(String dirDName, boolean isAsc, String prefix) {
    File file = new File(dirDName);
    return sortFileList(file, isAsc, prefix, null);
  }
  
  /**
   * 对某个目录下的文件或者子目录名称排序 
   * @param file               文件
   * @param direct             方向
   * @return
   */
  public static String[] sortFileList(File file, boolean isAsc) {
    return sortFileList(file, isAsc, null, null);
  }
  /**
   * 对某个目录下的文件或者子目录名称排序 
   * @param file               文件
   * @param direct             方向
   * @return
   */
  public static String[] sortFileList(File file,
      boolean isAsc, String prefix, String postfix) {
    if (file == null || !file.exists() || !file.isDirectory()) {
      return null;
    }
    TreeSet set = new TreeSet();
    String[] fileArray = file.list();
    for (int i = 0; i < fileArray.length; i++) {
      set.add(fileArray[i]);
    }
    List list = new ArrayList();
    Iterator iObj = set.iterator();
    while (iObj.hasNext()) {
      String name = iObj.next().toString();
      if (prefix != null && !name.startsWith(prefix)) {
        continue;
      }
      if (postfix != null && !name.endsWith(postfix)) {
        continue;
      }
      list.add(name);
    }
    int elemCnt = list.size();
    String[] rtArray = new String[elemCnt];
    if (isAsc) {
      for (int i = 0; i < elemCnt; i++) {
        rtArray[i] = (String)list.get(i);
      }
    }else {
      int index = 0;
      for (int i = elemCnt - 1; i >= 0; i--) {
        rtArray[index++] = (String)list.get(i);
      }
    }
    return rtArray;
  }
}
