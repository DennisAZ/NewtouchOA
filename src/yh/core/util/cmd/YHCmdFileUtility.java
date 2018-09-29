package yh.core.util.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import com.yahoo.platform.yui.compressor.JarClassLoader;
import com.yahoo.platform.yui.compressor.YUICompressor;

import yh.core.global.YHConst;
import yh.core.util.YHGuid;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUtility;
import yh.core.util.file.YHZipFileUtility;

/**
 * 命令用文件相关工具类
 * @author jpt
 *
 */
public class YHCmdFileUtility {
  private static final List<String> lastCharArray = new ArrayList<String>();
  private static final List<String> firstCharArray = new ArrayList<String>();
  
  static {
    lastCharArray.add("{");
    lastCharArray.add("}");
    lastCharArray.add("(");
    lastCharArray.add(")");
    lastCharArray.add("<");
    lastCharArray.add(">");
    lastCharArray.add("+");
    lastCharArray.add("-");
    lastCharArray.add("*");
    lastCharArray.add("/");
    lastCharArray.add("%");
    //lastCharArray.add("\"");
    //lastCharArray.add("\'");
    lastCharArray.add(":");
    lastCharArray.add(",");
    lastCharArray.add("[");
    lastCharArray.add("]");
    lastCharArray.add("=");
    lastCharArray.add("&");
    lastCharArray.add("|");
    lastCharArray.add("?");
    lastCharArray.add(".");
//    firstCharArray.add("+");
//    firstCharArray.add("-");
//    firstCharArray.add("*");
//    firstCharArray.add("/");
//    firstCharArray.add("%");
//    firstCharArray.add("}");
//    firstCharArray.add("|");
//    firstCharArray.add("]");
//    firstCharArray.add(")");
//    firstCharArray.add("?");
//    firstCharArray.add(":");
//    firstCharArray.add("&");
//    firstCharArray.add(".");
  }
  
  public static void main(String[] args) throws Exception {
//    transCode(new File("D:\\MYOA\\webroot\\mt"), ".php", "UTF-8", "GBK");
    //compactSrc("D:\\project\\yh\\webroot\\yh\\core\\js\\sys.js", ".js");
    //checkSemicolon("D:\\tmp\\js", "D:\\tmp\\semicolon.txt", ".js");
    //D:\\YH\\webroot\\yh\\core\\js\\cmp\\fck\\fckeditor\\editor\\dialog\\fck_spellerpages\\spellerpages\\controlWindow.js
    //checkSemicolon("D:\\YH\\webroot\\yh\\core\\js\\cmp\\fck\\fckeditor\\editor\\dialog\\fck_spellerpages\\spellerpages\\controlWindow.js", "D:\\tmp\\semicolon.txt", ".js");
//    System.out.println("java>>" + getLineCnt(new File("D:\\project\\yh\\src"), ".java"));
//    System.out.println("jsp>>" + getLineCnt(new File("D:\\project\\yh\\webroot"), ".jsp"));
//    System.out.println("js>>" + getLineCnt(new File("D:\\project\\yh\\webroot"), ".js"));
//    System.out.println("css>>" + getLineCnt(new File("D:\\project\\yh\\webroot"), ".css"));
//    
//    System.out.println("oa>>php>>" + getLineCnt(new File("D:\\project\\oacode"), ".php"));
//    System.out.println("oa>>js>>" + getLineCnt(new File("D:\\project\\oacode"), ".js"));
//    System.out.println("oa>>css>>" + getLineCnt(new File("D:\\project\\oacode"), ".css"));
//    splitExcel("D:\\tmp\\splitexcel");
//    randomTime("D:\\tmp\\splitexcel\\output");
//    txt2Word("D:\\tmp\\txt2word");
//    transCode(new File("D:\\project\\yherp\\src"), ".properties", "GBK", "UTF-8");
//    transCode(new File("D:\\project\\yherp\\webroot\\yherp\\sqlfiles"), ".txt", "GBK", "UTF-8");
    transCode(new File("D:\\project\\yh\\webroot\\yhwebsite"), ".txt", "UTF-8", "GBK");
  }
  
  /**
   * 
   * @param excelPath
   * @param wordPath
   * @throws Exception
   */
  public static void randomTime(String filePath) throws Exception {
    File pathFile = new File(filePath);
    File[] fileList = pathFile.listFiles();
    long yearSpan = YHConst.DT_Y * 3;
    long currTime = System.currentTimeMillis();
    for (int i = 0; i < fileList.length; i++) {
      String fileName = fileList[i].getName().toLowerCase();
      if (!fileList[i].isFile()) {
        continue;
      }
      if (!fileName.endsWith(".doc") && !fileName.endsWith(".docx")) {
        continue;
      }
      long rdNum = (long)(yearSpan * Math.random());
      long rdTime = currTime - rdNum;
      fileList[i].setLastModified(rdTime);
    }
  }
  
  /**
   * 
   * @param excelPath
   * @param wordPath
   * @throws Exception
   */
  public static void txt2Word(String workPath) throws Exception {
    int maxCnt = 0;
    int fileCnt = 0;
    long startTime = System.currentTimeMillis();
    try {
      String txtPath = workPath + "\\text2";
      String wordPath = workPath + "\\output";
      
      //YHFileUtility.deleteAll(wordPath);
      File txtFile = new File(txtPath);
      String[] fileArray = txtFile.list();
      for (int i = 0; i < fileArray.length; i++) {
        String fileName = fileArray[i];
        if (!fileName.toLowerCase().endsWith(".txt")) {
          continue;
        }
        if (maxCnt > 0 && i > maxCnt) {
          break;
        }
        if (i > 0 && i % 1000 == 0) {
          System.out.println("已经处理了>>" + i + "个文件");
        }
        StringBuffer txtBuf = YHFileUtility.loadLine2Buff(txtPath + "\\" + fileName, "GBK");
        String title = null;
        String body = null;
        
        int p1 = txtBuf.indexOf("新闻标题:");
        if (p1 < 0) {
          continue;
        }
        int p2 = txtBuf.indexOf("新闻作者:", p1);
        if (p1 >= 0 && p2 > p1) {
          title = txtBuf.substring(p1 + 5, p2);
        }
        p1 = txtBuf.indexOf("新闻正文:");
        if (p1 < 0) {
          continue;
        }
        p2 = txtBuf.indexOf("新闻关键字:", p1);
        if (p1 >= 0 && p2 > p1) {
          body = txtBuf.substring(p1, p2);
        }
        title = title.replace("\\r\\n", "").replace("\\n", "").replace("\\r", "");
        title = "<![CDATA[" + title +  "]]>";
        body = "<![CDATA[" + body +  "]]>";
        String wordFile = wordPath + "\\" + YHGuid.getRawGuid() + "_文档" + i + ".docx";
        genWordFile(workPath, wordFile, title, body);
        fileCnt++;
      }
    }catch (IOException e) { 
      e.printStackTrace(); 
    }finally {
    }
    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;
    System.out.println("fileCnt>>" + fileCnt);
    System.out.println("totalTime>>" + totalTime);
    if (fileCnt > 0) {
      System.out.println("perFile>>" + (totalTime / fileCnt));
    }
  }
  
  /**
   * 
   * @param excelPath
   * @param wordPath
   * @throws Exception
   */
  public static void splitExcel(String workPath) throws Exception {
    int maxCnt = 0;
    int fileCnt = 0;
    long startTime = System.currentTimeMillis();
    try {
      String excelPath = workPath + "\\src.xlsx";
      String wordPath = workPath + "\\output";
      
      YHFileUtility.deleteAll(wordPath);
      
      XSSFWorkbook xwb = new XSSFWorkbook(excelPath);  
      XSSFSheet sheet = xwb.getSheetAt(0); 
      XSSFRow row = null;
      XSSFCell cell1 = null;
      XSSFCell cell2 = null;      
      for (int i = 1; i < sheet.getLastRowNum(); i++) {
        if (maxCnt > 0 && i > maxCnt) {
          break;
        }
        if (i > 0 && i % 1000 == 0) {
          System.out.println("已经处理了>>" + i + "个文件");
        }
        row = sheet.getRow(i);
        cell1 = row.getCell(0);
        cell2 = row.getCell(1);
        String cell1Text = cell1.getStringCellValue();
        String cell2Text = cell2.getStringCellValue();
        if (YHUtility.isNullorEmpty(cell1Text) || YHUtility.isNullorEmpty(cell2Text)) {
          continue;
        }
        cell1Text = "<![CDATA[" + cell1Text +  "]]>";
        cell2Text = "<![CDATA[" + cell2Text +  "]]>";
        String wordFile = wordPath + "\\" + YHGuid.getRawGuid() + "_文档" + i + ".docx";
        genWordFile(workPath, wordFile, cell1Text, cell2Text);
        fileCnt++;
      } 
    }catch (IOException e) { 
      e.printStackTrace(); 
    }finally {
    }
    long endTime = System.currentTimeMillis();
    long totalTime = endTime - startTime;
    System.out.println("fileCnt>>" + fileCnt);
    System.out.println("totalTime>>" + totalTime);
    if (fileCnt > 0) {
      System.out.println("perFile>>" + (totalTime / fileCnt));
    }
  }
  
  /**
   * 生成Word文件
   * @param workPath           工作目录
   * @param title      
   * @param body
   */
  private static void genWordFile(String workPath, String wordFile, String title, String body) throws Exception {
    String txtFile = workPath + "\\temp\\word\\document.xml";
    String tmpFile = workPath + "\\document.src";
    
    YHFileUtility.copyFile(tmpFile, txtFile);
    List rules = new ArrayList();
    rules.add(new String[]{"@1", title});
    rules.add(new String[]{"@2", body});
    YHFileUtility.replaceInFile(txtFile, rules);
    
    YHZipFileUtility.doZip(workPath + "\\temp", wordFile);
  }
  
  /**
   * 读取Word
   * @param filePath
   * @return
   * @throws Exception
   */
  public static String extractWord(String filePath) throws Exception {
    if (filePath == null) {
      return "";
    }
    File file = new File(filePath);
    if (!file.exists() || !file.isFile()) {
      return "";
    }
    if (filePath.toLowerCase().endsWith(".docx")) {
      try {
        org.apache.poi.xwpf.extractor.XWPFWordExtractor docx = new XWPFWordExtractor(POIXMLDocument.openPackage(filePath)); 
        return docx.getText();
      }catch(Exception ex) {        
      }
    }else if (filePath.toLowerCase().endsWith(".doc")) {
      try {
        POIFSFileSystem fs = null;
        fs = new POIFSFileSystem(new FileInputStream(filePath)); 
        HWPFDocument doc = new HWPFDocument(fs);
        WordExtractor we = new WordExtractor(doc);
        String str = we.getText() ;
      }catch(Exception ex) {        
      }
    }
    return "";
  }
  
  /**
   * 文件转码
   * @param file
   * @param exName
   * @throws Exception
   */
  public static void transCode(String filePath, String exName, String fromCode, String toCode) throws Exception {
    transCode(new File(filePath), exName, fromCode, toCode);
  }
  /**
   * 文件转码
   * @param file
   * @param exName
   * @throws Exception
   */
  public static void transCode(File file, String exName, String fromCode, String toCode) throws Exception {
    if (file == null || !file.exists()) {
      return;
    }
    if (file.isFile()) {
      if (!file.getName().endsWith(exName)) {
        return;
      }
      List lineList = new ArrayList();
      YHFileUtility.loadLine2Array(file.getAbsolutePath(), lineList, fromCode);
      YHFileUtility.storeArray2Line(file.getAbsolutePath(), lineList, toCode);
    }else if (file.isDirectory()) {
      File[] fileArray = file.listFiles();
      for (int i = 0; i < fileArray.length; i++) {
        transCode(fileArray[i], exName, fromCode, toCode);
      }
    }
  }
  
  /**
   * 检查不添加分号结束的语句
   * @param file
   * @param rtMap
   * @throws Exception
   */
  public static void checkSemicolon(String srcFilePath, String outFilePath, String extName) throws Exception {
    Map<String, List> rtMap = new LinkedHashMap<String, List>();
    checkSemicolon(new File(srcFilePath), rtMap, extName);
    if (rtMap.size() < 1) {
      return;
    }
    List<String> outList = new ArrayList<String>();
    Iterator<String> iKeys = rtMap.keySet().iterator();
    while (iKeys.hasNext()) {
      String key = iKeys.next();
      String value = rtMap.get(key).toString();
      outList.add(key + " >> " + value);
    }
    YHFileUtility.storeArray2Line(outFilePath, outList);
  }
  
  /**
   * 清理文件内容
   * @param file
   * @throws Exception
   */
  public static void clearFile(String file) throws Exception {
    clearFile(new File(file), ".jsp");
  }
  /**
   * 清理文件内容
   * @param file
   * @param exName
   * @throws Exception
   */
  public static void clearFile(File file, String exName) throws Exception {
    if (file == null || !file.exists()) {
      return;
    }
    if (file.isFile()) {
      if (!file.getName().endsWith(exName)) {
        return;
      }
      YHFileUtility.storeString2File(file.getAbsolutePath(), "");
    }else if (file.isDirectory()) {
      File[] fileArray = file.listFiles();
      for (int i = 0; i < fileArray.length; i++) {
        clearFile(fileArray[i], exName);
      }
    }
  }
  
  /**
   * 取得代码文件的行数
   * @param file
   * @param rtMap
   * @throws Exception
   */
  public static long getLineCnt(File file, String exName) throws Exception {
    if (file == null || !file.exists()) {
      return 0;
    }
    long lineCnt = 0;
    if (file.isFile()) {
      String fileName = file.getName();
      if (!fileName.endsWith(exName)) {
        return 0;
      }
      List<String> lineList = new ArrayList<String>();
      YHFileUtility.loadLine2Array(file.getAbsolutePath(), lineList);
      
      return lineList.size();
    }else if (file.isDirectory()) {
      File[] fileArray = file.listFiles();
      for (int i = 0; i < fileArray.length; i++) {
        lineCnt += getLineCnt(fileArray[i], exName);
      }
    }
    return lineCnt;
  }
  /**
   * 检查不添加分号结束的语句
   * @param file
   * @param rtMap
   * @throws Exception
   */
  public static void checkSemicolon(File file, Map<String, List> rtMap, String exName) throws Exception {
    if (file == null || !file.exists()) {
      return;
    }
    if (file.isFile()) {
      String fileName = file.getName();
      if (!fileName.endsWith(exName)) {
        return;
      }
      System.out.println(file.getAbsolutePath());
      List<String> lineList = new ArrayList<String>();
      YHFileUtility.loadLine2Array(file.getAbsolutePath(), lineList);
      deleteCommentSingLine(lineList);
      deleteCommentMulLine(lineList, "/*", "*/");
      trimeLine(lineList);
      List<Integer> lineNumList = new ArrayList<Integer>();
      checkSemicolon(lineList, lineNumList);
      if (lineNumList.size() > 0) {
        rtMap.put(file.getAbsolutePath(), lineNumList);
      }
    }else if (file.isDirectory()) {
      File[] fileArray = file.listFiles();
      for (int i = 0; i < fileArray.length; i++) {
        checkSemicolon(fileArray[i], rtMap, exName);
      }
    }
  }
  
  public static void compressJs(String srcFile, String destFile) throws Exception {
    ClassLoader loader = new JarClassLoader();
    Thread.currentThread().setContextClassLoader(loader);
    Class c = loader.loadClass(YUICompressor.class.getName());
    Method main = c.getMethod("main", new Class[] {java.lang.String[].class});
    String[] args = new String[]{"--type", "js", "--charset", "UTF-8", "-o", destFile, "--nomunge", srcFile};
    main.invoke(null, new Object[] {args});
  }
  
  /**
   * 压缩Js代码
   * @param srcPath
   * @param destPath
   * @throws Exception
   */
  public static void compactJsInpath(String srcPath, String destPath) throws Exception {
    if (srcPath == null) {
      return;
    }
    File srcPathFile = new File(srcPath);
    if (srcPathFile.isFile()) {
      if (srcPath.endsWith(".js")) {
        System.out.println(srcPath);
        YHFileUtility.storeString2File(destPath, "");
        compressJs(srcPath, destPath);
      }
    }else {
      String[] fileArray = srcPathFile.list();
      for (int i = 0; i < fileArray.length; i++) {
        String fromFile = srcPath + "\\" + fileArray[i];
        String toFile = destPath + "\\" + fileArray[i];
        compactJsInpath(fromFile, toFile);
      }
    }
  }
  
  /**
   * 压缩代码
   * @param file
   * @param extName
   * @throws Exception
   */
  public static void compactSrc(String filePath, String extName) throws Exception {
    compactSrc(new File(filePath), extName);
  }
  
  /**
   * 压缩代码
   * @param file
   * @param extName
   * @throws Exception
   */
  public static void compactSrc(File file, String extName) throws Exception {
    if (file == null || !file.exists()) {
      return;
    }
    if (file.isFile()) {
      String fileName = file.getName();
      if (!fileName.endsWith(extName)) {
        return;
      }

      List lineList = new ArrayList<String>();
      YHFileUtility.loadLine2Array(file.getAbsolutePath(), lineList);
      deleteCommentSingLine(lineList);
      deleteCommentMulLine(lineList, "/*", "*/");
      if (extName != null && extName.equalsIgnoreCase(".jsp")) {
        deleteCommentMulLine(lineList, "<%--", "--%>");
      }
      trimeLine(lineList);
      deleteBlankLine(lineList);
      storeArray2Line(file.getAbsolutePath(), lineList);
    }else if (file.isDirectory()) {
      File[] fileArray = file.listFiles();
      for (int i = 0; i < fileArray.length; i++) {
        compactSrc(fileArray[i], extName);
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
  private static void storeArray2Line(String fileName, List rtList, String charSet) throws Exception {
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
   * 把数组中的内如输出到流中，每个元素作为一行

   * @param out
   * @param list
   * @throws Exception
   */
  public static void storeArray2Line(OutputStream out, List<String> list, String charSet) throws Exception {
    if (list == null) {
      return;
    }
    OutputStreamWriter writer = new OutputStreamWriter(out, charSet);
    try {
      for (int i = 0; i < list.size(); i++) {
        String lineStr = list.get(i).trim();
        if (lineStr.length() < 1) {
          continue;
        }
        String nextStr = "";
        if (i < list.size() - 1) {
          nextStr = list.get(i + 1).trim();
        }
        writer.write(lineStr);
        String lastStr = lineStr.substring(lineStr.length() - 1);
        String firstStr = "";
        if (nextStr.length() > 0) {
          firstStr = nextStr.substring(0, 1);
        }
        if (lastStr.endsWith(";") || lastCharArray.contains(lastStr)) {
        }else if (nextStr.startsWith("var") || firstCharArray.contains(firstStr)) {          
        }else {
          writer.write("\r\n");
        }
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
   * 检查文件内容
   * @param fileLineList
   * @param outList
   */
  private static void checkSemicolon(List<String> fileLineList,
      List<Integer> outList) {
    for (int i = 0; i < fileLineList.size(); i++) {
      String lineStr = fileLineList.get(i).trim();
      if (lineStr.length() < 1
          || lineStr.equals("else")
          || lineStr.equals("try")
          || lineStr.equals("catch")
          || lineStr.equals("if")
          || lineStr.equals("case")
          || lineStr.equals("while")
          || lineStr.equals("do")
          || lineStr.equals("finally")
          || lineStr.endsWith(";")) {
        continue;
      }
      String nextStr = "";
      for (int j = i + 1; j < fileLineList.size(); j++) {
        nextStr = fileLineList.get(j).trim();
        if (nextStr.length() > 0) {
          break;
        }
      }
      String lastStr = lineStr.substring(lineStr.length() - 1);
      String firstStr = "";
      if (nextStr.length() > 0) {
        firstStr = nextStr.substring(0, 1);
      }
      if (!lastCharArray.contains(lastStr)) {
        if (nextStr.startsWith("var")) {
          outList.add(new Integer(i + 1));
          continue;
        }
      }
      if (!lastCharArray.contains(lastStr) && !firstCharArray.contains(firstStr)) {
        outList.add(new Integer(i + 1));
        continue;
      }
    }
  }
  
  /**
   * 删除注释- 单行
   * @param fileName
   * @throws Exception
   */
  private static void deleteCommentSingLine(List<String> lineList) {
    int p1 = -1;
    for (int i = 0; i < lineList.size(); i++) {
      String lineStr = lineList.get(i).trim();
      if (lineStr.length() < 2) {
        continue;
      }
      if (lineStr.substring(0, 2).equals("//")) {
        lineList.set(i, "");
        continue;
      }
      p1 = lineStr.indexOf("//");
      if (p1 > 0) {
        lineList.set(i, lineStr.substring(0, p1).trim());
        continue;
      }
    }
  }
  
  /**
   * 是否是在双引号包围
   * @param str
   * @param pos
   * @return
   */
  private static boolean isInQuote(String str, int pos) {
    boolean hasBegin = false;
    for (int i = pos - 1; i >= 0; i--) {
      char currChar = str.charAt(i);
      if (currChar == '\"' || currChar == '\'') {
        hasBegin = true;
        break;
      }
    }
    if (!hasBegin) {
      return false;
    }
    for (int i = pos + 1; i <= (str.length() - 1); i++) {
      char currChar = str.charAt(i);
      if (currChar == '\"' || currChar == '\'') {
        return true;
      }
    }
    return false;
  }
  
  /**
   * 删除注释- 多行
   * @param fileName
   * @throws Exception
   */
  private static void deleteCommentMulLine(List<String> lineList, String startTag, String endTag) {
    int p1 = -1;
    int p2 = -1;
    int loopCnt = 0;
    for (int i = 0; i < lineList.size();) {
      String lineStr = lineList.get(i).trim();
      p1 = lineStr.indexOf(startTag);
      p2 = lineStr.indexOf(endTag);
      if (p1 >= 0) {
        if (isInQuote(lineStr, p1)) {
          i++;
          continue;
        }
        if (p2 > 0) {
          lineList.set(i, (lineStr.substring(0, p1).trim() + lineStr.substring(p2 + endTag.length())).trim());
          //i++;，这里不应该跳过，应该继续检查该行中是否有第二个这样的注释
          continue;
        }
        //关闭标签不是同一行的情况
        lineList.set(i, "");
        for (int j = i + 1; j < lineList.size(); j++) {
          String lineStr2 = lineList.get(j).trim();
          p2 = lineStr2.indexOf(endTag);
          if (p2 >= 0) {
            lineList.set(j, lineStr2.substring(p2 + endTag.length()).trim());
            i = j; //这里不应该跳过，应该继续检查该行中是否有第二个这样的注释
            break;
          }else {
            lineList.set(j, "");
            i++;
          }
        }
      }else {
        i++;
      }
    }
  }
  /**
   * 删除行的前后空格
   * @param fileName
   * @throws Exception
   */
  private static void trimeLine(List<String> lineList) {
    for (int i = lineList.size() - 1; i >= 0; i--) {
      String lineStr = lineList.get(i).trim();
      lineList.set(i, lineStr);
    }
  }
  /**
   * 删除空白行
   * @param fileName
   * @throws Exception
   */
  private static void deleteBlankLine(List<String> lineList) {
    for (int i = lineList.size() - 1; i >= 0; i--) {
      String lineStr = lineList.get(i);
      if (lineStr.length() < 1) {
        lineList.remove(i);
      }
    }
  }
}
