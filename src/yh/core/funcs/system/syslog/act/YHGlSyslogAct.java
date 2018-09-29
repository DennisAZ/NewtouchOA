package yh.core.funcs.system.syslog.act;

import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHExportlogLogic;
import yh.core.funcs.system.syslog.logic.YHGlSyslogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;

public class YHGlSyslogAct {
  private static Logger log = Logger
      .getLogger(" yh.core.funcs.system.syslog.act.YHSysLogAct");
  /**
   * Request
   */
  private HttpServletRequest request = null;
  /**
   * 文件列表
   */
  private List fileList = new ArrayList();
  /**
   * 文件哈希表
   * 
   * {文件表单名，FileItem}
   */
  private HashMap fileMap = new HashMap();
  /**
   * 普通表单参数 全局的配置属性信息，这样的信息应该尽量的少，因为需要常驻内存
   */

  private static Properties props = null;

  /**
   * 取得系统配置属性
   */
  public static String getProp(String key) {
    if (props == null) {
      return null;
    }
    String prop = (String) props.getProperty(key); // ???
    if (prop != null) {
      prop = prop.trim();
    }
    return prop;
  }

  /**
   * 取得字符串类型的值
   * @param key
   * @return
   */
  public static String getString(String key) {
    return getProp(key);
  }

  /**
   * 文件上传临时目录
   */
  public static final String FILE_UPLOAD_TEMP_DIR = "fileUploadTempDir";
  /**
   * rootDir路径
   */
  public static final String ROOT_DIR = "rootDir";
  private HashMap<String, String> paramMap = new HashMap();
  /**
   * 管理日志查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getMyGlSysLog(HttpServletRequest request,
     HttpServletResponse response) throws Exception {
     Connection dbConn = null;
     YHPerson loginUser = null;
     String type = request.getParameter("TYPE");
     String users = request.getParameter("user");
     String statrtime = request.getParameter("statrTime");
     String endtime = request.getParameter("endTime");
     String ip = request.getParameter("IP");
     String remark = request.getParameter("REMARK");
     String copytime = request.getParameter("COPY_TIME");
     String radio = request.getParameter("OPERATION");
   try {
        String str = "";
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson personLogin = (YHPerson) request.getSession().getAttribute(
        "LOGIN_USER");
        YHGlSyslogLogic syslog = new YHGlSyslogLogic();
        //YHOut.println("endtime::"+endtime);
        str = syslog.getglsyslog(type, users, statrtime, endtime, ip, remark,
        copytime, dbConn, personLogin);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功取出日志");
        request.setAttribute("data", str); // 到search.jsp 页面 去 接收data 对象
        request.setAttribute(YHActionKeys.RET_DATA, str);
   } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      request.setAttribute(YHActionKeys.FORWARD_PATH, "/core/inc/error.jsp");
      throw ex;
   }
    return "/core/funcs/system/syslog/search.jsp";
  }

  /**
   * 系统日志导出
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String SysExport(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    String type = request.getParameter("TYPE");
    String users = request.getParameter("user");
    String statrtime = request.getParameter("statrTime");
    String endtime = request.getParameter("endTime");
    String ip = request.getParameter("IP");
    String remark = request.getParameter("REMARK");
    String copytime = request.getParameter("COPY_TIME");
    String radio = request.getParameter("OPERATION");
    OutputStream ops = null;
    Connection conn = null;
    try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        YHPerson personLogin = (YHPerson) request.getSession().getAttribute(
        "LOGIN_USER");
        conn = requestDbConn.getSysDbConn();
        String fileName = URLEncoder.encode("系统日志.xls", "UTF-8");
        fileName = fileName.replaceAll("\\+", "%20");
        response.setHeader("Cache-control", "private");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Cache-Control","maxage=3600");
        response.setHeader("Pragma","public");
        response.setHeader("Content-disposition", "attachment; filename=\""
            + fileName + "\"");
        ops = response.getOutputStream();
        YHExportlogLogic exportlog = new YHExportlogLogic();
        ArrayList<YHDbRecord> dbL = exportlog.sysexportlog(type, users,
        statrtime, endtime, ip, remark, copytime, conn, personLogin);
        YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      throw ex;
    }finally{
      ops.close();
    }
     return null;
  }

  /*
   * //系统日志导入
   * public String SysImport(HttpServletRequest request,
   * HttpServletResponse response) throws Exception { Connection dbConn = null;
   * YHPerson loginUser = null; //System.out.println("sss"); YHFileUploadForm
   * fileForm = new YHFileUploadForm();
   * fileForm.parseUploadRequest(request);//请求客户端 请求的信息
   * 
   * InputStream is =
   * fileForm.getInputStream((String)fileForm.iterateFileFields().next());
   * String soundFile =
   * fileForm.getFileName((String)fileForm.iterateFileFields().next());
   * List<String[]> list = null; if(soundFile.endsWith(".xls")){
   * //System.out.println(soundFile); list = this.parseExcelPoixlsx(is); } else{
   * list = this.parseExcelPoixlsx(is); } // Connection dbConn = null; try {
   * YHRequestDbConn requestDbConn =
   * (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
   * dbConn = requestDbConn.getSysDbConn(); YHPerson user =
   * (YHPerson)request.getSession().getAttribute("LOGIN_USER"); int count = 0;
   * list.remove(0); for (int i = 1;i < list.size(); i++){ String[] s =
   * list.get(i); } for(String[] s : list){ YHBilingual bi = new YHBilingual();
   * bi.setCnName(s[0]); bi.setEnName(s[1]); bi.setEntryDate(new
   * Date(System.currentTimeMillis()));
   * 
   * // this.logic.addBilingual(dbConn, bi); YHExportlogLogic importlog = new
   * YHExportlogLogic(); DateFormat format = new
   * SimpleDateFormat("yyyy-MM-dd hh:ss"); String name = s[0];
   * 
   * int userId = importlog.insertimport(name, dbConn, user); String date =
   * s[1]; //System.out.println(date); // Timestamp time = (Timestamp)
   * format.parse(date); // Double time = Double.parseDouble(date);
   * //System.out.println(time); //date = String.valueOf(time); String Ip =
   * s[2]; String type = s[3]; String typeValue = getValue(type); String rmark =
   * s[4]; //System.out.println(rmark);
   * if(rmark.equals(0.0)||rmark.equals("0.0")|| rmark==""||rmark==null){
   * rmark=""; } importlog.sysimportlog(userId, date, Ip, typeValue, rmark,
   * dbConn, user); } request.setAttribute("msg","插入" + count + "条记录");
   * 
   * }catch(Exception ex) { request.setAttribute("msg", ex.getMessage());
   * 
   * throw ex; }
   * 
   * return "/subsys/infomgr/bilingual/success.jsp"; // return
   * "/core/inc/rtjson.jsp"; // return "/core/funcs/system/syslog/export.jsp"; }
   */
  /**
   * 解析EXCEL,并返回装有中文名称和英文名称的二维数组的list 使用POI,支持中文文件名,不支持office2007
   * 
   * @param is
   * @param path
   * @return
   * @throws IOException
   */
  /*
   * private List<String[]> parseExcelPoixlsx(InputStream is) throws
   * IOException{ //POIFSFileSystem fs=new POIFSFileSystem(is); DateFormat
   * format = new SimpleDateFormat( "yyyy-MM-dd hh:ss"); XSSFWorkbook wb = new
   * XSSFWorkbook(is);//获得工作薄 对象 XSSFSheet sheet = wb.getSheetAt(0); // 获得第一个工作薄
   * List<String[]> list = new ArrayList<String[]>(); for(Iterator it =
   * sheet.rowIterator();it.hasNext();){ XSSFRow r = (XSSFRow)it.next(); //
   * 工作薄的行
   * 
   * if (r.getCell(0)!= null && r.getCell(1)!= null && r.getCell(2)!=null &&
   * r.getCell(3)!=null){ String[] cells = new String[5];
   * 
   * for(int i = 0; i <= 4; i++){ int tmp = r.getCell(i).getCellType(); if
   * (r.getCell(i).getCellType() == 1){ cells[i] =
   * r.getCell(i).getStringCellValue(); // String fiveName =
   * r.getCell(5).getStringCellValue(); } else if(r.getCell(i).getCellType() ==
   * 3){ cells[i] = String.valueOf(r.getCell(i).getNumericCellValue()); }else
   * if(r.getCell(i).getCellType()==0){ SimpleDateFormat sdf = new
   * SimpleDateFormat("yyyy-MM-dd hh:mm"); cells[i] =
   * sdf.format((r.getCell(i).getDateCellValue())); } }
   * 
   * list.add(cells); String cnName = r.getCell(0).getStringCellValue(); //
   * 获得第一行
   * 
   * String enName = r.getCell(1).getStringCellValue();// 获得第二行
   * 
   * String threeName = r.getCell(2).getStringCellValue(); String fourName =
   * r.getCell(3).getStringCellValue(); String fiveName = ""; // list.add(new
   * String[]{cnName,enName,threeName,fourName,fiveName}); } else { continue; }
   * } return list; } private List<String[]> parseExcelPoixls(InputStream is)
   * throws IOException{ HSSFWorkbook wb = new HSSFWorkbook(is); HSSFSheet sheet
   * = wb.getSheetAt(0); List<String[]> list = new ArrayList<String[]>();
   * for(Iterator it = sheet.rowIterator();it.hasNext();){ HSSFRow r =
   * (HSSFRow)it.next(); if (r.getCell((short) 1)!= null && r.getCell((short)
   * 2)!= null && r.getCell((short) 3)!=null && r.getCell((short) 4)!=null){
   * String cnName = r.getCell((short) 1).getStringCellValue(); String enName =
   * r.getCell((short) 2).getStringCellValue(); String threeName =
   * r.getCell((short) 3).getStringCellValue(); String fourName =
   * r.getCell((short) 4).getStringCellValue(); String fiveName =
   * r.getCell((short) 5).getStringCellValue(); list.add(new
   * String[]{cnName,enName,threeName,fourName,fiveName}); } else { continue; }
   * } return list; }
   */
  /**
   * 23 种类型转换
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public static Map<String, String> getMap() {
    Map map = new HashMap();
    map.put("登陆日志", "1");
    map.put("登陆密码错误", "2");
    map.put("添加部门", "3");
    map.put("编辑部门", "4");
    map.put("删除部门", "5");
    map.put("添加用户", "6");
    map.put("编辑用户", "7");
    map.put("删除用户", "8");
    map.put("非法IP登录", "9");
    map.put("错误用户名", "10");
    map.put("admin清空密码", "11");
    map.put("系统资源回收", "12");
    map.put("考勤数据管理", "13");
    map.put("修改登录密码", "14");
    map.put("公告通知管理", "15");
    map.put("公共文件柜", "16");
    map.put("网络硬盘", "17");
    map.put("软件注册", "18");
    map.put("用户批量设置", "19");
    map.put("培训课程管理", "20");
    map.put("用户KEY验证失败", "21");
    map.put("退出系统", "22");
    map.put("员工离职", "23");
    return map;
  }

  public String getValue(String key) {
    // 获得Map 中的Key
    return getMap().get(key);
  }
  /**
   * 默认解析协议
   * @param request
   * 客户端请求
   * @throws Exception
   */
  public void parseUploadRequest(HttpServletRequest request) throws Exception {
    this.request = request;
    File tmpFile = new File(getUploadCatchPath());
    if (!tmpFile.exists()) {
        tmpFile.mkdirs(); // ？？？？
    }
    parseUploadRequest(request, YHSysProps
        .getInt(YHSysPropKeys.MAX_UPLOAD_FILE_SIZE), 10 * YHConst.K, YHSysProps
        .getString("fileUploadTempDir"), YHConst.DEFAULT_CODE);
  }
  /**
   * 取得附件上传缓存路径
   * @return
   */
  public static String getUploadCatchPath() {
    String cachPath = getString(FILE_UPLOAD_TEMP_DIR); // ??? getString
    if (cachPath == null) {
        cachPath = "D:\\YH\\catch";
    }
    // 相对路径
    if (cachPath.indexOf(":") != 1 && !cachPath.startsWith("/")) { // ???
      cachPath = getString(ROOT_DIR + "\\" + cachPath);
    }
    return cachPath;
  }
  
  public void parseUploadRequest(HttpServletRequest request, int maxSize,
    int buffSize, String tempPath, String charSet) throws Exception {
    DiskFileUpload fu = new DiskFileUpload();
    fu.setHeaderEncoding(charSet);
    // 设置允许用户上传文件大小,单位:字节
    fu.setSizeMax(maxSize * YHConst.M);
    // maximum size that will be stored in memory?
    // 设置最多只允许在内存中存储的数据,单位:字节
    fu.setSizeThreshold(buffSize);
    // 设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录
    if (!YHUtility.isNullorEmpty(tempPath)) {
      fu.setRepositoryPath(tempPath);
    }
    // 开始读取上传信息    List fieldList = fu.parseRequest(request);
    Iterator iter = fieldList.iterator();
    while (iter.hasNext()) {
      FileItem item = (FileItem) iter.next();
      // 文件字段
      if (!item.isFormField()) {
        fileList.add(item);
        fileMap.put(item.getFieldName(), item);
        // 普通表单字段      } else {
        if (charSet != null) {
            paramMap.put(item.getFieldName(), item.getString(charSet));
        } else {
            paramMap.put(item.getFieldName(), item.getString());
        }
      }
    }
  }

 
  /**
   * 删除所选信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteChecklog(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String deleteStr = request.getParameter("delete_str");
    YHPerson loginperson = (YHPerson) request.getSession().getAttribute(
        "LOGIN_USER");
    //int loginUserId = person.getSeqId();
    try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHGlSyslogLogic syslog = new YHGlSyslogLogic();
        boolean str = syslog.deleteChecknews(dbConn, loginperson, deleteStr);
       /* System.out.println(str);
         YHNewsManageLogic newsManageLogic = new YHNewsManageLogic();
         boolean success =newsManageLogic.deleteChecknews(dbConn,
         Integer.toString(loginUserId), deleteStr);
        */      
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据");
    } catch (Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 删除所you信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteAllChecklog(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    String copytime = request.getParameter("COPY_TIME");
    Connection dbConn = null;
    YHPerson loginperson = (YHPerson) request.getSession().getAttribute(
    "LOGIN_USER");
    try {
          YHRequestDbConn requestDbConn = (YHRequestDbConn) request
              .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          YHGlSyslogLogic syslog = new YHGlSyslogLogic();
          boolean str = false;
          if (loginperson.isAdmin()) {
            str = syslog.deleteAll(copytime, dbConn, loginperson);
          }
          String st = String.valueOf(str);
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据");
          request.setAttribute(YHActionKeys.RET_DATA, st);
    } catch (Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   *根据单选按钮来删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteRadio(HttpServletRequest request,
    HttpServletResponse response) throws Exception {
    YHPerson loginperson = (YHPerson) request.getSession().getAttribute(
        "LOGIN_USER");
    Connection dbConn = null;
    YHPerson loginUser = null;
    String type = request.getParameter("TYPE");
    String users = request.getParameter("user");
    String statrtime = request.getParameter("statrTime");
    String endtime = request.getParameter("endTime");
    String ip = request.getParameter("IP");
    String remark = request.getParameter("REMARK");
    String copytime = request.getParameter("COPY_TIME");
    String radio = request.getParameter("OPERATION");
    try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request
            .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        YHPerson personLogin = (YHPerson) request.getSession().getAttribute("233");
        String str = null;
        YHGlSyslogLogic syslog = new YHGlSyslogLogic();
        if (loginperson.isAdmin() || loginperson.isAdminRole()) {
            str = syslog.deletRadio(type, users, statrtime, endtime, ip, remark,
            copytime, dbConn, personLogin);
         }
         request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
         request.setAttribute(YHActionKeys.RET_MSRG, "成功取出日志");
         request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  public static void main(String[] args) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    // Timestamp time = (Timestamp) format.parse(date);
    String ds = format.format(new Date("Thu Jun 10 13:16:02 CST 2010"));
  }
}
