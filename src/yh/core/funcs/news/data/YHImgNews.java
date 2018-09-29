package yh.core.funcs.news.data;



import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import yh.core.global.YHSysProps;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;


/**
 * 图片新闻
 * @author qwx110
 *
 */
public class YHImgNews{
  
  private int seqId;
  private String subject;
  private String attachmentId;
  private String attachmentName;
  private String filePath;
  private Timestamp newsTime;
  private String content;
  private String nextTitle;
  private int nextId;
  private static String[] imageType = {"gif","jpg","jpeg","png","bmp","iff","jp2","jpx","jb2","jpc","xbm","wbmp"};
  public String getNextTitle() {
    return nextTitle;
  }

  public void setNextTitle(String nextTitle) {
    this.nextTitle = nextTitle;
  }

  public int getNextId() {
    return nextId;
  }

  public void setNextId(int nextId) {
    this.nextId = nextId;
  }

  public Timestamp getNewsTime() {
    return newsTime;
  }

  public void setNewsTime(Timestamp newsTime) {
    this.newsTime = newsTime;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  private List<String> imagPaths;
  private List<String> imagAbsPaths;  //图片的绝对路径如：D:/ddd/ddd/aa.jpg
  public static String profix = System.getProperty("file.separator");
  public static final String BASE_PATH = YHSysProps.getAttachPath()+ profix +"news" + profix;
  public String getFilePath(){
    return filePath;
  }

  public void setFilePath(String filePath){
    this.filePath = filePath;
  }

  
  public int getSeqId(){
    return seqId;
  }

  public void setSeqId(int seqId){
    this.seqId = seqId;
  }


  public String getSubject(){
    return subject;
  }

  public void setSubject(String subject){
    this.subject = subject;
  }

  public String getAttachmentId(){
    return attachmentId;
  }

  public void setAttachmentId(String attachmentId){
    this.attachmentId = attachmentId;
  }

  public String getAttachmentName(){
    return attachmentName;
  }

  public void setAttachmentName(String attachmentName){
    this.attachmentName = attachmentName;
  }

  public List<String> getImagPaths() throws Exception{   
    String[] attrName = null;
    String[] attrId = null;
    List<String> paths = new ArrayList<String>();
    if(!YHUtility.isNullorEmpty(attachmentName) && !YHUtility.isNullorEmpty(attachmentId)){
      attrName = attachmentName.split("[*]");
      attrId = attachmentId.split(",");
      for(int i=0; i<attrId.length; i++){
        if(!YHUtility.isNullorEmpty(attrName[i]) && !YHUtility.isNullorEmpty(attrId[i]) && isPicture(attrName[i])){
          String absolutePate = "attachmentId="+attrId[i]+"&module=" + YHNewsCont.MODULE + "&attachmentName="+YHUtility.encodeURL(attrName[i]);
          paths.add(absolutePate);
        }
      }
    }   
    return paths;
  }
  
  /**
   * 包含了图片的绝对路径如:d:\aa\aa\aa.jpg
   * @return
   * @throws Exception 
   */
  public List<String> getAbsImagPath() throws Exception{
    String[] attrName = null;
    String[] attrId = null;
    List<String> paths = new ArrayList<String>();
    if(!YHUtility.isNullorEmpty(attachmentName) && !YHUtility.isNullorEmpty(attachmentId)){
      attrName = attachmentName.split("[*]");
      attrId = attachmentId.split(",");
      for(int i=0; i<attrId.length; i++){
        if(!YHUtility.isNullorEmpty(attrName[i]) && !YHUtility.isNullorEmpty(attrId[i]) &&  isPicture(attrName[i])){
          String attId = attrId[i].replace("_", profix);
          String absolutePate = BASE_PATH + attId +"_" +  YHUtility.encodeURL(YHUtility.encodeURL((attrName[i])));
          paths.add(absolutePate);
        }
      }      
    }
    return paths;
  }
  
  
  /**
   * 把一个YHImgNews类转化为json串的格式
   * @param basePath
   * @return
   * @throws Exception 
   */
  public String toJson() throws Exception{
    StringBuffer sb = new StringBuffer();
    sb.append("{");
       sb.append("seqId:").append(seqId).append(",");
       sb.append("subject:").append("\"").append(YHUtility.encodeSpecial(subject)).append("\"").append(",");
       sb.append("imgPaths:").append("[");
         List<String> list = getImagPaths();
         if(list != null && list.size() >0){
           for(int i=0; i<list.size(); i++){
             sb.append("{");
               sb.append("path:").append("\"").append(list.get(i)).append("\"");
             if(i < list.size()-1){
               sb.append("}").append(",");
             }else{
               sb.append("}");
             }
           }
         }
       sb.append("]");
    sb.append("}");
    return sb.toString();
  }
  
  /**
   * 把一个YHImgNews类转化为json串的格式
   * @return
   * @throws Exception
   */
  public String toJson2() throws Exception{
    StringBuffer sb = new StringBuffer();
    sb.append("{");
       sb.append("seqId:").append(seqId).append(",");
       sb.append("subject:").append("\"").append(YHUtility.encodeSpecial(subject)).append("\"").append(",");
       sb.append("imgPaths:").append("[");
         List<String> list = getAbsImagPath();
         if(list != null && list.size() >0){
           for(int i=0; i<list.size(); i++){
             sb.append("{");
               sb.append("path:").append("\"").append(YHUtility.encodeSpecial(list.get(i))).append("\"");
             if(i < list.size()-1){
               sb.append("}").append(",");
             }else{
               sb.append("}");
             }
           }
         }
       sb.append("]");
    sb.append("}");
    return sb.toString();
  }
  
  public String getPicPath(String attrName, String attrId ){
    String attId = attrId.replace("_", profix);
    String absolutePate = BASE_PATH + attId +"_" + attrName;
    return absolutePate;
  }
  
  public String getSmallPicPath(String attrName, String attrId ){
    int index = attrId.indexOf("_");
    String tmp = attrId.substring(0 , index);
    String attId = attrId.substring(index + 1);
    String path = BASE_PATH  + tmp + profix + "smallPic" ;
    File file = new File(path);
    if (!file.exists()) {
      file.mkdir();
    }
    String absolutePate = path + profix+ attId +"_" + attrName;
    return absolutePate;
  }
  public boolean isPicture(String pName){
    for(int i=0; i<imageType.length; i++){
      if(pName.endsWith("."+imageType[i])){
        return true;
      }
    }
    return false;
  }
}
