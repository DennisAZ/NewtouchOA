package yh.subsys.jtgwjh.docReceive.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import srvSeal.SrvSealUtil;
import yh.core.funcs.diary.logic.YHDiaryUtil;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.file.YHZipFileTool;

public class YHAipToJNI {
  /**
   * 利用JNI控制AIP板式文件
   * 
   * @param filePath
   *          ： AIP正文路径
   * @param printCount
   *          ： 打印份数
   * @param stratNo
   *          ：开始编号
   * @param endNo
   *          :结束编号
   * @return
   */
  public static String AipToJNI(String filePath, String printCount,
      String stratNo, String endNo,String printSign) {
    SrvSealUtil ssu = new SrvSealUtil();
    int nObjID = ssu.openObj("", 0, 0);// SrvSealUtil.java
    int l = ssu.login(nObjID, 4, "HWSEALDEMOXX", "");// SrvSealUtil.java
    if(!YHUtility.isInteger(printCount) || printCount.equals("0") || stratNo.equals("-")|| endNo.equals("-")){
      printCount = "0";
      stratNo = "000";
      endNo = "000";
    }
    String AipData = "STRDATA:printCount=" + printCount + "\r\n"
        + "printStratNo=" + stratNo + "\r\nprintEndNo=" + endNo + "\r\nprintSign=" + printSign + "\r\n" ;
    int ap = ssu.addPage(nObjID, filePath, AipData);// SrvSealUtil.java
    int s = ssu.saveFile(nObjID, filePath, "aip");// SrvSealUtil.java
    return "";
  }

  /**
   * 利用JNI控制AIP板式文件 修改打印份数和编号
   * 
   * @param filePath
   *          ： AIP正文路径
   * @param printCount
   *          ： 打印份数
   * @param stratNo
   *          ：开始编号
   * @param endNo
   *          :结束编号
   * @return
   */
  public static String UpdateAipToJNI(String filePath, String printCount,
      String stratNo, String endNo) {
    SrvSealUtil ssu = new SrvSealUtil();
    if (YHUtility.isNumber(stratNo)) {
      stratNo = YHAipToJNI.getAutoStr(Integer.parseInt(stratNo), 3);
    }
    File file = new File(filePath);
    if (file.exists()) {
      int nObjID = ssu.openObj("", 0, 0);// SrvSealUtil.java
      int l = ssu.login(nObjID, 4, "HWSEALDEMOXX", "");// SrvSealUtil.java
      String AipData = "STRDATA:printCount=" + printCount + "\r\n"
          + "printStratNo=" + stratNo + "\r\n";
      int ap = ssu.addPage(nObjID, filePath, AipData);// SrvSealUtil.java
      int s = ssu.saveFile(nObjID, filePath, "aip");// SrvSealUtil.java
    }

    return "";
  }
  public static void main(String[] args) throws UnsupportedEncodingException {
     AipToJNI3("D:\\temp\\test.docx", "2", "", "");
    // String attach = "sad asd 四大阿斯";
    // String test= new String(attach.getBytes("utf-8"),"utf-8");
    // System.out.println(test);
    


  }

  public static String AipToJNI3(String filePath, String printCount,
      String stratNo, String endNo) {
    File file = new File(filePath);
    if (file.exists()) {
      SrvSealUtil ssu = new SrvSealUtil();
      int nObjID = ssu.openObj("", 0, 0);// SrvSealUtil.java
      int l = ssu.login(nObjID, 4, "HWSEALDEMOXX", "");// SrvSealUtil.java
      String AipData = "printCount:" + printCount + "\r\n" + "printStratNo="
          + stratNo + "\r\n" + "printEndNo=" + endNo + "\r\n";
      int ap = ssu.addPage(nObjID, filePath, "");// SrvSealUtil.java
      int s = ssu.saveFile(nObjID, "D:\\temp\\ttt.doc", "doc");// SrvSealUtil.java
    }

    return "";
  }


/**
   * 利用JNI控制AIP板式文件
   * 
   * @param filePath
   *          ： AIP正文路径
   * @param printCount
   *          ： 打印份数
   * @param stratNo
   *          ：开始编号
   * @param endNo
   *          :结束编号
   * @return
   */
  public static String AipToJNI2(String filePath, String printCount,
      String stratNo, String endNo) {
    File file = new File(filePath);
    if (file.exists()) {
      SrvSealUtil ssu = new SrvSealUtil();
      int nObjID = ssu.openObj("", 0, 0);// SrvSealUtil.java
      int l = ssu.login(nObjID, 4, "HWSEALDEMOXX", "");// SrvSealUtil.java
      String AipData = "printCount:" + printCount + "\r\n" + "printStratNo="
          + stratNo + "\r\n" + "printEndNo=" + endNo + "\r\n";
      int ap = ssu.addPage(nObjID, filePath, AipData);// SrvSealUtil.java
      int s = ssu.saveFile(nObjID, "D:\\t.aip", "aip");// SrvSealUtil.java
    }

    return "";
  }

  /**
   * 脱密开始 脱密处理附件；首先讲脱密的AIP保存在脱密的文件夹中
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public String saveAIP(HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    String filePath = request.getParameter("filePath") == null ? "" : request
        .getParameter("filePath");
    String fileName = request.getParameter("fileName") == null ? "" : request
        .getParameter("fileName");
    String unloadPath = request.getParameter("unloadPath") == null ? "test"
        : request.getParameter("unloadPath");// 临时压缩ZIP路径

    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String modulPath = "TUOMI";
    Map<String, String> attr = fileUploadAIP(fileForm, modulPath, unloadPath);
    return "";
  }

  /**
   * 处理上传附件，脱密下载
   * 
   * @param request
   *          HttpServletRequest
   * @param 脱密模版保存路径
   *          ：String modulPath 下载文件夹：String unloadPath
   * @return
   * @throws Exception
   */
  public static Map<String, String> fileUploadAIP(YHFileUploadForm fileForm,
      String modulPath, String unloadPath) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    try {
      Calendar cld = Calendar.getInstance();
      int year = cld.get(Calendar.YEAR) % 100;
      int month = cld.get(Calendar.MONTH) + 1;
      String mon = month >= 10 ? month + "" : "0" + month;
      String hard = year + mon;
      Iterator<String> iKeys = fileForm.iterateFileFields();
      System.out.println(fileForm.getParameter("moduel"));
      //
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        String fileNameV = fileName;
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }
        // 首先保存AIP文件，保存在脱密的临时目录下
        String rand = YHDiaryUtil.getRondom();
        fileName = rand + "_" + fileName;
        while (YHDiaryUtil.getExist(YHSysProps.getAttachPath() + File.separator
            + hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        result.put(hard + "_" + rand, fileNameV);
        fileForm.saveFile(fieldName, YHSysProps.getAttachPath()
            + File.separator + modulPath + File.separator + fileName);

        // 转wor的或者PDF
        AipToPDF(YHSysProps.getAttachPath() + File.separator + modulPath
            + File.separator + fileName, YHSysProps.getAttachPath()
            + File.separator + "PDF" + File.separator + unloadPath
            + File.separator + rand + ".doc");
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }

  /*
   * 脱密下载，执行最后一个正文附件，供其下载 脱密结束
   */
  public String uloadFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    OutputStream ops = null;
    InputStream is = null;
    String unloadPath = request.getParameter("unloadPath") == null ? "test"
        : request.getParameter("unloadPath");// 下载临时文件夹名
    try {
      String ZIPFilePath = YHSysProps.getAttachPath() + File.separator + "PDF"
          + File.separator + unloadPath;
      YHZipFileTool.doZip(ZIPFilePath, ZIPFilePath + ".zip");// 打包
      File file = new File(ZIPFilePath + ".zip");
      String fileNameTemp = URLEncoder.encode("脱密文件.zip", "UTF-8");
      // String fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control", "private");
      response.setContentType("application/octet-stream");
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Content-disposition", "attachment; filename=\""
          + fileNameTemp + "\"");

      is = new FileInputStream(file);
      ops = response.getOutputStream();
      if (is != null) {
        byte[] buff = new byte[8192];
        int byteread = 0;
        while ((byteread = is.read(buff)) != -1) {
          ops.write(buff, 0, byteread);
          ops.flush();

        }
        if (is != null) {
          is.close();
        }
      }
    } catch (Exception e) {
      throw e;
    }
    // response.getOutputStream().print("aaaa");
    return null;
  }

  /**
   * 
   * @param filePath
   *          :文件路径
   * @param savePath
   *          :文件保存路径
   * @return
   */
  public static String AipToPDF(String filePath, String savePath) {
    SrvSealUtil ssu = new SrvSealUtil();
    File file = new File(savePath);
    File parenFile = file.getParentFile();
    if (!parenFile.exists()) {
      parenFile.mkdirs();
    }

    int nObjID = ssu.openObj("", 0, 0);// SrvSealUtil.java
    int l = ssu.login(nObjID, 4, "HWSEALDEMOXX", "");// SrvSealUtil.java
    int ap = ssu.addPage(nObjID, filePath, "");// SrvSealUtil.java
    int s = ssu.saveFile(nObjID, savePath, "doc");// SrvSealUtil.java //
                                                  // 保存word或者PDF
    return "";
  }

  /**
   * 转存 start
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String ZhuanCunsaveAIP(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    String filePath = request.getParameter("filePath") == null ? "" : request
        .getParameter("filePath");
    String fileName = request.getParameter("fileName") == null ? "" : request
        .getParameter("fileName");
    String module = request.getParameter("module") == null ? "docReceive"
        : request.getParameter("module");

    YHFileUploadForm fileForm = new YHFileUploadForm();

    fileForm.parseUploadRequest(request);
    YHJhDocrecvInfoLogic logic = new YHJhDocrecvInfoLogic();
    String modulPath = "TUOMI";
    Map<String, String> attr = fileUploadAIPZhuanCun(fileForm, modulPath,
        filePath, module, fileName);
    // request.setCharacterEncoding("utf-8");
    // response.setCharacterEncoding("utf-8");

    if (attr != null) {
      Set<String> keys = attr.keySet();
      for (String key : keys) {
        String value = attr.get(key);
        String attach = key + "`~" + value;
        String test = new String(attach.getBytes("GBK"), "ISO8859-1");
        response.getOutputStream().print(test);// new
                                               // String(attach.getBytes(),"ISO 8859-1")
      }
    }
    /*
     * 
     */
    // response.getOutputStream().print("aaaa");
    return "";
  }

  /**
   * 处理上传附件，脱密下载
   * 
   * 
   * 转存
   * 
   * @param request
   *          HttpServletRequest
   * @param 脱密模版保存路径
   *          ：String modulPath 下载文件夹：String unloadPath
   * @return
   * @throws Exception
   */
  public static Map<String, String> fileUploadAIPZhuanCun(
      YHFileUploadForm fileForm, String modulPath, String savePath,
      String module, String fileOldName) throws Exception {
    Map<String, String> result = new HashMap<String, String>();
    try {
      Calendar cld = Calendar.getInstance();
      File file = new File(savePath);
      File pFile = file.getParentFile();
      String parentPath = pFile.getPath();// 获取父路径
      String hard = parentPath.substring(parentPath.lastIndexOf("\\") + 1,
          parentPath.length());
      Iterator<String> iKeys = fileForm.iterateFileFields();
      System.out.println(fileForm.getParameter("moduel"));
      // 保存附件
      while (iKeys.hasNext()) {
        String fieldName = iKeys.next();
        String fileName = fileForm.getFileName(fieldName);
        String fileNameV = fileName;
        if (YHUtility.isNullorEmpty(fileName)) {
          continue;
        }

        String rand = YHDiaryUtil.getRondom();
        fileName = rand + "_" + fileName;
        while (YHDiaryUtil.getExist(YHSysProps.getAttachPath() + File.separator
            + hard, fileName)) {
          rand = YHDiaryUtil.getRondom();
          fileName = rand + "_" + fileName;
        }
        fileOldName = fileOldName.substring(0, fileOldName.lastIndexOf("."));

        result.put(hard + "_" + rand, fileOldName + ".doc");
        fileForm.saveFile(fieldName, YHSysProps.getAttachPath()
            + File.separator + modulPath + File.separator + fileName);

        // 转wor的或者PDF
        AipToPDF(YHSysProps.getAttachPath() + File.separator + modulPath
            + File.separator + fileName, parentPath + File.separator + rand
            + "_" + fileOldName + ".doc");
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }

  /**
   * 字符串前面补0
   * 
   * @param number
   * @param i
   * @return
   */
  public static String getAutoStr(int number, int i) {
    // 待测试数据

    String str = "0";
    // 得到一个NumberFormat的实例

    NumberFormat nf = NumberFormat.getInstance();
    // 设置是否使用分组
    nf.setGroupingUsed(false);
    // 设置最大整数位数

    nf.setMaximumIntegerDigits(i);
    // 设置最小整数位数
    nf.setMinimumIntegerDigits(i);
    str = nf.format(number);
    return str;
  }
}
