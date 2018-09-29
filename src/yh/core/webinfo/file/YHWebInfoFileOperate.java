package yh.core.webinfo.file;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yh.core.util.file.YHFileConst;
import yh.core.util.file.YHFileUtility;
import yh.core.webinfo.dto.YHWebInfo;
import yh.core.webinfo.dto.YHWebInfoAttachment;
import yh.core.webinfo.util.YHWebInfoByTimeComparatorUtility;

public class YHWebInfoFileOperate {
  public static String PAGECATALOGPATH  = "/pages";
  public static String INFOCATALOGPATH = "/pagesinfo";
  public static String ATTACHMENTPATH = "/attachment";
  public static String IMAGEPATH = "/img";
  public synchronized static void addWebInfoFile(YHWebInfo webInfo,String contextRealPath) throws Exception{
    String name = "";
    File pageCatalog = new File(contextRealPath + PAGECATALOGPATH);
    if(!pageCatalog.exists()){
      pageCatalog.mkdirs();
    }
    if(pageCatalog.listFiles().length==0){
      name = "0000";
    }else{
      int maxInt = getMaxNameFile(contextRealPath + PAGECATALOGPATH);
      maxInt++;
      name =  String.valueOf(maxInt);
      if(name.length()==1){
        name = "000"+name;
      }else if(name.length()==2){
        name = "00"+name;
      }else if(name.length()==3){
        name = "0"+name;
      }
    }
    String filePath = contextRealPath + PAGECATALOGPATH+"/" + name + ".html";
    List<String> rtList = new ArrayList();
    
    rtList.add("webInfoUser=" + webInfo.getWebInfoUser());
    rtList.add("webInfoTitle=" + webInfo.getWebInfoTitle());
    rtList.add("webInfoDate=" + webInfo.getWebInfoDate());
    rtList.add("webInfoKeyWord=" + webInfo.getWebInfoKeyWord());
   
    rtList.add("fileCount=" + webInfo.getFileCount());
    
    List<YHWebInfoAttachment> list = webInfo.getAttachments();
    for(int i = 0; i < webInfo.getFileCount(); i++ ){
      YHWebInfoAttachment a = list.get(i);
      rtList.add("fileName-" + (i+1) + "=" + a.getName());
      rtList.add("filePath-" + (i+1) + "=" + a.getFilePath());
    }
    
    String fileInfoPath = contextRealPath+INFOCATALOGPATH+"/" + name + ".txt";
  
    YHFileUtility.storeString2File(filePath, webInfo.getContent());
    YHFileUtility.storeArray2Line(fileInfoPath, rtList, "UTF-8");
  
  }
  public static List getWebInfoList(String contextRealPath,String searchStr) throws Exception{
    List list = new ArrayList();
    String pagesPath = contextRealPath + PAGECATALOGPATH;
    String infoPath = contextRealPath + INFOCATALOGPATH;
  
    File catalog = new File(pagesPath);
    if(!catalog.exists()){
      catalog.mkdirs();
    }
    catalog = new File(pagesPath);
    File[] files = catalog.listFiles();
  
    int count = files.length;

    for(int i = 0 ;i < count ;i++ ){
      Map<String,String> map = new HashMap();
      File pageFile = files[i];
      String filePath = pageFile.getPath();
      if (!filePath.endsWith("html")) {
        continue;
      }
      String fileName = YHFileUtility.getFileNameNoExt(filePath);
      String infoFilePath = infoPath + "/" + fileName+".txt";
      YHFileUtility.load2Map(infoFilePath, map);
      
      String webInfoKeyWord = map.get("webInfoKeyWord");
      
      Pattern p = Pattern.compile(searchStr); 
      
      Matcher m = p.matcher(webInfoKeyWord); 
      
      boolean rs = m.find();
      if(rs){
        YHWebInfo webInfo = new YHWebInfo();
        String content = new String(YHFileUtility.loadLine2Buff(filePath));
        
        webInfo.setWebInfoDate(map.get("webInfoDate"));
        webInfo.setWebInfoKeyWord(map.get("webInfoKeyWord"));
        webInfo.setWebInfoTitle(map.get("webInfoTitle"));
        webInfo.setWebInfoUser(map.get("webInfoUser"));
        
        int fileCount = Integer.parseInt(map.get("fileCount")) ;
        for(int j = 1 ; j <= fileCount ; j++){
          YHWebInfoAttachment a = new YHWebInfoAttachment();
          a.setFilePath(map.get("filePath-"+j));
          a.setName(map.get("fileName-"+j));
          webInfo.addAttachment(a);
        }
        webInfo.setFileName(fileName);
        webInfo.setContent(content);
        list.add(webInfo);
      }
      Collections.sort(list,new YHWebInfoByTimeComparatorUtility());
    }
    return list;
  }
  
  public static int getMaxNameFile(String dirName) throws IOException{
    File catalog = new File(dirName);
    if(!catalog.exists()){
      catalog.mkdirs();
    }
    String[] fileString = catalog.list();
    int max = 0;
    for(String s : fileString){
      int pointIndex = s.lastIndexOf(YHFileConst.PATH_POINT);
      String name = s.substring(0, pointIndex);
      if(name.matches("[0-9]{4}")){
        int i = Integer.parseInt(name);
        if(i>max){
          max = i;
         }
      }
    }
    return max;
  }
  public static void main(String args[]){

  }
  public static YHWebInfo getWebInfoByName(String contextRealPath, String name) throws Exception {
    // TODO Auto-generated method stub
    String pagePath = contextRealPath + PAGECATALOGPATH + "/" + name +".html";
    String infoPath = contextRealPath + INFOCATALOGPATH + "/" + name +".txt";
    
    YHWebInfo webInfo = new YHWebInfo();
    webInfo.setFileName(name);
    String content = new String(YHFileUtility.loadLine2Buff(pagePath));
    webInfo.setContent(content);
    
    Map<String,String> map = new HashMap();
    YHFileUtility.load2Map(infoPath, map);
  
    
    webInfo.setWebInfoDate(map.get("webInfoDate"));
    webInfo.setWebInfoKeyWord(map.get("webInfoKeyWord"));
    webInfo.setWebInfoTitle(map.get("webInfoTitle"));
    webInfo.setWebInfoUser(map.get("webInfoUser"));
    
    int fileCount = Integer.parseInt(map.get("fileCount")) ;
    for(int j = 1 ; j <= fileCount ; j++){
      YHWebInfoAttachment a = new YHWebInfoAttachment();
      a.setFilePath(map.get("filePath-"+j));
      a.setName(map.get("fileName-"+j));
      webInfo.addAttachment(a);
    }
    return webInfo;
  }
  public synchronized static void updateWebInfoFile(YHWebInfo webInfo, String contextRealPath) throws Exception {
    // TODO Auto-generated method stub
    String name = webInfo.getFileName();
    File pageCatalog = new File(contextRealPath + PAGECATALOGPATH);
    if(!pageCatalog.exists()){
      pageCatalog.mkdirs();
    }
    
    String filePath = contextRealPath + PAGECATALOGPATH+"/" + name + ".html";
    List<String> rtList = new ArrayList();
    
    rtList.add("webInfoUser=" + webInfo.getWebInfoUser());
    rtList.add("webInfoTitle=" + webInfo.getWebInfoTitle());
    rtList.add("webInfoDate=" + webInfo.getWebInfoDate());
    rtList.add("webInfoKeyWord=" + webInfo.getWebInfoKeyWord());
    
    rtList.add("fileCount=" + webInfo.getFileCount());
    
    List<YHWebInfoAttachment> list = webInfo.getAttachments();
    for(int i = 0; i < webInfo.getFileCount(); i++ ){
      YHWebInfoAttachment a = list.get(i);
      rtList.add("fileName-" + (i+1) + "=" + a.getName());
      rtList.add("filePath-" + (i+1) + "=" + a.getFilePath());
    }
    
    String fileInfoPath = contextRealPath+INFOCATALOGPATH+"/" + name + ".txt";
    File file = new File(filePath);
    if(file.exists()){
      YHFileUtility.storeString2File(filePath, webInfo.getContent());
    }
    file = new File(fileInfoPath);
    if(file.exists()){
      YHFileUtility.storeArray2Line(fileInfoPath, rtList, "UTF-8");
    }
  }
  public static void deleteWebInfoFile(String contextRealPath, String name) throws Exception {
    // TODO Auto-generated method stub
    String pagePath = contextRealPath + PAGECATALOGPATH + "/" + name +".html";
    String infoPath = contextRealPath + INFOCATALOGPATH + "/" + name +".txt";
    Map<String,String> map = new HashMap();
    YHFileUtility.load2Map(infoPath, map);
    
    int fileCount = Integer.parseInt(map.get("fileCount")) ;
    for(int j = 1 ; j <= fileCount ; j++){
       String path = map.get("filePath-"+j);
       deleteAttachment(contextRealPath , path);
    }
    File file = new File(pagePath);
    if(file.exists()){
      YHFileUtility.deleteAll(pagePath);
    }
    file = new File(infoPath);
    if(file.exists()){
      YHFileUtility.deleteAll(infoPath);
    }
  }
  public static void deleteAttachment(String contextRealPath , String filePath){
    String pagePath = contextRealPath + ATTACHMENTPATH + "/" + filePath;
    File file = new File(pagePath);
    
    if(file.exists()){
      YHFileUtility.deleteAll(pagePath);
    }
  }
  public static void deleteFileAttachment(String contextRealPath,
      String filePath, String fileName) throws Exception {
    // TODO Auto-generated method stub
    String infoPath = contextRealPath + INFOCATALOGPATH + "/" + fileName +".txt";
    Map<String,String> map = new HashMap();
    YHFileUtility.load2Map(infoPath, map);
    
    int fileCount = Integer.parseInt(map.get("fileCount")) ;
    for(int j = 1 ; j <= fileCount ; j++){
      if(map.get("filePath-"+j).equals(filePath)){
        map.remove("filePath-"+j);
        map.remove("fileName-"+j);
        map.put("fileCount", String.valueOf(fileCount - 1));
        break;
      }
    }
    List list = map2List(map);
    YHFileUtility.storeArray2Line(infoPath, list);
  }
  public static List map2List(Map<String,String> map){
    List list = new ArrayList();
    Set<String> fields = map.keySet(); 
    for(String field : fields)
    list.add(field + "=" + map.get(field));
    return list;
  }
  
}


