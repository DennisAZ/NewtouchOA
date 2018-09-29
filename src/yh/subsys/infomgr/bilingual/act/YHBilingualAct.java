package yh.subsys.infomgr.bilingual.act;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jxl.Sheet;
import jxl.Workbook;

import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.subsys.infomgr.bilingual.data.YHBilingual;
import yh.subsys.infomgr.bilingual.logic.YHBilingualLogic;

public class YHBilingualAct {
  public static final String BILINGUAL_PATH = "\\bilingual";
  
  private YHBilingualLogic logic = new YHBilingualLogic();
   
  /**
   * 增加记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String addBilingual(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    YHFileUploadForm fileForm = new YHFileUploadForm();
    
    fileForm.parseUploadRequest(request);
    
    Iterator<String> iKeys = fileForm.iterateFileFields();
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      String fileName = fileForm.getFileName(fieldName);
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      String sp = System.getProperty("file.separator");
      fileForm.saveFile(fieldName, request.getSession().getServletContext().getRealPath(sp) + YHBilingualAct.BILINGUAL_PATH + "\\" + fileName);
    }
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map map = fileForm.getParamMap();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      YHBilingual bi = (YHBilingual) YHFOM.build(map, YHBilingual.class, "");
      
      bi.setEntryUser(user.getSeqId());
      Date d = new Date(System.currentTimeMillis());
      bi.setEntryDate(d);
      bi.setEnable("0");
      
      this.logic.addBilingual(dbConn, bi);
      request.setAttribute("msg", "添加成功");
    }catch(Exception ex) {
      request.setAttribute("msg", "添加未成功");
      throw ex;
    }finally{
      return "/subsys/infomgr/bilingual/success.jsp";
    }
  }
  
  /**
   * 修改一条记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String modifyBilingual(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    YHFileUploadForm fileForm = new YHFileUploadForm();
    
    fileForm.parseUploadRequest(request);
    
    String fileExists = fileForm.getExists(YHBilingualAct.BILINGUAL_PATH);
    Iterator<String> iKeys = fileForm.iterateFileFields();
    while (iKeys.hasNext()) {
      String fieldName = iKeys.next();
      String fileName = fileForm.getFileName(fieldName);
      if (YHUtility.isNullorEmpty(fileName)) {
        continue;
      }
      String sp = System.getProperty("file.separator");
      fileForm.saveFile(fieldName, request.getSession().getServletContext().getRealPath(sp) + YHBilingualAct.BILINGUAL_PATH + "\\" + fileName);
    }
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      Map map = fileForm.getParamMap();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      
      YHBilingual bi = (YHBilingual) YHFOM.build(map, YHBilingual.class, "");
      
      //判断是否修改文件
      if(!"on".equals((String)map.get("isSoundFile"))){
        bi.setSoundFile((String)map.get("formfile"));
      }
      bi.setEntryUser(user.getSeqId());
      Date d = new Date(System.currentTimeMillis());
      bi.setEntryDate(d);
      
      this.logic.modifyBilingual(dbConn, bi);
      request.setAttribute("msg", "修改成功");
    }catch(Exception ex) {
      request.setAttribute("msg", "修改未成功");
      throw ex;
    }finally{
      return "/subsys/infomgr/bilingual/success.jsp";
    }
  }
  
  /**
   * 启用/不启用
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setEnable(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    
    String seqId = request.getParameter("seqId");
    String enable = request.getParameter("enable");
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      this.logic.setEnable(dbConn, Integer.parseInt(seqId), enable);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"设置成功");
      
    }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /** 
  * 取得双语标示的信息,并分页  * @param request 
  * @param response 
  * @return 
  * @throws Exception 
  */ 
  public String getPage(HttpServletRequest request, 
  HttpServletResponse response) throws Exception { 

    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
    
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, 
      queryParam, 
      "select SEQ_ID" +
      ",TYPE" +
      ",CN_NAME" +
      ",EN_NAME" +
      ",SOUND_FILE" +
      ",(select USER_NAME from PERSON where SEQ_ID = ENTRY_USER) as ENTRY_USER" +
      ",ENTRY_DATE" +
      ",ENABLE from BILINGUAL" +
      " order by ENTRY_DATE desc"); 
    
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 
  
      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
  
  /**
   * 通过中文名称/英文名称查找记录,并分页
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String searchPage(HttpServletRequest request, 
      HttpServletResponse response) throws Exception { 
    
    String enName = request.getParameter("enName");
    enName = enName == null ? "" : enName;
    
    String type = request.getParameter("type");
    type = type == null ? "" : type;
    
    String cnName = request.getParameter("cnName");
    cnName = cnName == null ? "" : cnName;
    cnName = java.net.URLDecoder.decode(cnName, "utf-8");
    
    Connection dbConn = null; 
    try { 
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn(); 
      String sql = "select SEQ_ID" +
        ",TYPE" +
        ",CN_NAME" +
        ",EN_NAME" +
        ",SOUND_FILE" +
        ",(select USER_NAME from PERSON where SEQ_ID = ENTRY_USER) as ENTRY_USER" +
        ",ENTRY_DATE" +
        ",ENABLE from BILINGUAL" +
        " where CN_NAME like '%" + YHDBUtility.escapeLike(cnName) +
        "%'" +
        " and TYPE like '%" + YHDBUtility.escapeLike(type) +
        "%'" +
        " and EN_NAME like '%" + YHDBUtility.escapeLike(enName) +
        "%'" +
        " order by ENTRY_DATE desc";
      
      //System.out.println(sql);
      
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request.getParameterMap()); 
      YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn, 
          queryParam, 
          sql); 
      
      PrintWriter pw = response.getWriter(); 
      pw.println(pageDataList.toJson()); 
      pw.flush(); 
      
      return null; 
    }catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR); 
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage()); 
      throw e; 
    } 
  }
  
  /**
   * 删除记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteRecord(HttpServletRequest request,
          HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      this.logic.deleteRecord(dbConn, Integer.parseInt(seqId));
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG,"成功删除记录");
      
    }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
    }
    
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 批量导入
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String batchAdd(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    
    InputStream is = fileForm.getInputStream((String)fileForm.iterateFileFields().next());
    String soundFile = fileForm.getFileName((String)fileForm.iterateFileFields().next());
    
    String type = fileForm.getParameter("type");
    // 通过jxl解析EXCEL
    List<String[]> list = null;
    
    if(soundFile.endsWith(".xlsx")){
      list = this.parseExcelPoixlsx(is);
    }
    else{
      list = this.parseExcelPoixls(is);
    }
    
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int count = 0;
      for(String[] s : list){
        YHBilingual bi = new YHBilingual();
        bi.setCnName(s[0]);
        bi.setEnName(s[1]);
        
        //待修改
        bi.setType(type);
        bi.setEnable("0");
        bi.setEntryDate(new Date(System.currentTimeMillis()));
        bi.setEntryUser(user.getSeqId());
        this.logic.addBilingual(dbConn, bi);
        count++;
      }
      request.setAttribute("msg","插入" + count + "条记录");
      
    }catch(Exception ex) {
      request.setAttribute("msg", ex.getMessage());
      throw ex;
    }
    
    return "/subsys/infomgr/bilingual/success.jsp";
  }
  
  /**
   * 按id查询单条记录
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryRecord(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHBilingual bi = (YHBilingual)this.logic.queryRecord(dbConn, Integer.parseInt(seqId));
      
      request.setAttribute(YHActionKeys.RET_DATA, YHFOM.toJson(bi).toString()); 
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功"); 
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK); 
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage()); 
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  
  
  /**
   * 解析EXCEL,并返回装有中文名称和英文名称的二维数组的list
   * 使用jxl,不支持office2007
   * @param is
   * @param path
   * @return
   * @throws Exception
   */
  private List<String[]> parseExcelJxl(InputStream is) throws Exception{
    Workbook workbook = Workbook.getWorkbook(is);
    Sheet sheet = workbook.getSheet(0);
    List<String[]> list = new ArrayList<String[]>();
    
    int rows = sheet.getRows(); 
    int row = 1;
    while (row < rows){
      String cnName = sheet.getCell(1,row).getContents().trim();
      String enName = sheet.getCell(2,row).getContents().trim();
      if ("".equals(cnName) || "".equals(enName)){
        break;
      }
      
      String[] f = new String[2];
      f[0] = cnName;
      f[1] = enName;
      list.add(f);
      
      row++;
    }
    
    workbook.close();
    is.close();
    return list;
  }
  
  /**
   * 解析EXCEL,并返回装有中文名称和英文名称的二维数组的list
   * 使用POI,支持中文文件名,不支持office2007
   * @param is
   * @param path
   * @return
   * @throws IOException 
   */
  private List<String[]> parseExcelPoixlsx(InputStream is) throws IOException{
    
    //POIFSFileSystem fs=new POIFSFileSystem(is);
    XSSFWorkbook wb = new XSSFWorkbook(is);
    XSSFSheet sheet = wb.getSheetAt(0); 
    List<String[]> list = new ArrayList<String[]>();
    for(Iterator it = sheet.rowIterator();it.hasNext();){
      XSSFRow r = (XSSFRow)it.next();
      if (r.getCell(1) != null && r.getCell(2) != null){
        String cnName = r.getCell(1).getStringCellValue();
        String enName = r.getCell(2).getStringCellValue();
        list.add(new String[]{cnName,enName});
      }
      else {
        continue;
      }
    }
    return list;
  }
  
  private List<String[]> parseExcelPoixls(InputStream is) throws IOException{
    HSSFWorkbook wb = new HSSFWorkbook(is);
    HSSFSheet sheet = wb.getSheetAt(0); 
    List<String[]> list = new ArrayList<String[]>();
    for(Iterator it = sheet.rowIterator();it.hasNext();){
      HSSFRow r = (HSSFRow)it.next();
      if (r.getCell((short) 1) != null && r.getCell((short) 2) != null){
        String cnName = r.getCell((short) 1).getStringCellValue();
        String enName = r.getCell((short) 2).getStringCellValue();
        list.add(new String[]{cnName,enName});
      }
      else {
        continue;
      }
    }
    return list;
  }
}