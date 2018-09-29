package yh.subsys.inforesouce.data;

import java.util.ArrayList;
import java.util.List;
import yh.core.funcs.news.data.YHNewsCont;
import yh.core.util.YHUtility;

/**
 * 图片管理
 * @author qwx110
 *
 */
public class YHImageManage{
  private int seqId;  
  private int newsId;   //新闻id
  private String description;  //摘要
  private String subject;     //新闻题目
  private List<String> imgPaths;//所有的图片路径
  private String attachmentId;
  private String attachmentName;
  private static String imgBasePath = "/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?";
  private static String[] imageType = {"gif","jpg","jpeg","png","bmp","iff","jp2","jpx","jb2","jpc","xbm","wbmp"};
  private String baseContext = "";
  public String getBaseContext(){
    return baseContext;
  }
  public void setBaseContext(String baseContext){
    this.baseContext = baseContext;
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
  public int getSeqId(){
    return seqId;
  }
  public void setSeqId(int seqId){
    this.seqId = seqId;
  }
  public int getNewsId(){
    return newsId;
  }
  public void setNewsId(int newsId){
    this.newsId = newsId;
  }
  public String getDescription(){
    if(YHUtility.isNullorEmpty(description) || "null".equalsIgnoreCase(description)){
      description = "";
    }
    
    return description;
  }
  public void setDescription(String description){
    this.description = description;
  }
  public String getSubject(){
    if(YHUtility.isNullorEmpty(subject) || "null".equalsIgnoreCase(subject)){
      subject = "";
    }
    return subject;
  }
  public void setSubject(String subject){
    this.subject = subject;
  }
  public List<String> getImgPaths() throws Exception{
    return getImagPaths();
  }
  
  public String toJson() throws Exception{
    StringBuffer sb = new StringBuffer();
     sb.append("{");
       sb.append("\"seqId\":").append("\"").append(seqId).append("\"").append(",");
       sb.append("\"newsId\":").append("\"").append(newsId).append("\"").append(",");
       sb.append("\"description\":").append("\"").append(YHUtility.encodeSpecial(description)).append("\"").append(",");
       sb.append("\"subject\":").append("\"").append(YHUtility.encodeSpecial(subject)).append("\"").append(",");
       sb.append("\"imgPaths\":").append(listToString(getImgPaths()));
     sb.append("}");
    return sb.toString();
  }
 /**
  * 把一个List转化为json
  * @param imgPaths
  * @return
  */
  public String listToString(List<String> imgPaths){
    StringBuffer sb = new StringBuffer();
    sb.append("[");
    if(imgPaths != null){
      int size = imgPaths.size();
      for(int i=0; i<size; i++){
        sb.append("\"").append((imgPaths.get(i))).append("\"");
        if(i < size -1 ){
          sb.append(",");
        }
      }
  }
    sb.append("]");
   return sb.toString();
  }
  
  public List<String> getImagPaths() throws Exception{   
    String[] attrName = null;
    String[] attrId = null;
    List<String> paths = new ArrayList<String>();    
    if(!YHUtility.isNullorEmpty(attachmentName) && !YHUtility.isNullorEmpty(attachmentId)){
        attrName = attachmentName.split("[*]");
        attrId = attachmentId.split(",");   
        int len = attrId.length > 8 ?8:attrId.length;
        for(int i=0; i<len; i++){
          if(!YHUtility.isNullorEmpty(attrName[i]) && !YHUtility.isNullorEmpty(attrId[i]) && isPicture(attrName[i])){
            String absolutePate = getBaseContext() + imgBasePath + "attachmentId="+attrId[i]+"&module=" + YHNewsCont.MODULE + "&attachmentName="+YHUtility.encodeURL(attrName[i]);
            paths.add(absolutePate);
          }
        }
      
    }   
    return paths;
  }
  
  /**
   * 判断是不是图片
   * @param pName
   * @return
   */
  public boolean isPicture(String pName){
    for(int i=0; i<imageType.length; i++){
      if(pName.endsWith("."+imageType[i])){
        return true;
      }
    }
    return false;
  }
  
  public static void main(String[] args){
    String test = "<P>111111111</P>";
    YHImageManage img = new YHImageManage();
    img.setDescription(test);
    System.out.println(img.getDescription());
    String pp = "aa.jpg";
    String ppp = "dsfdjpg";
    System.out.println(img.isPicture(ppp));
  }
} 
