package yh.core.webinfo.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class YHWebInfo {
  private List<YHWebInfoAttachment> attachments = new ArrayList<YHWebInfoAttachment>();
  

  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
 
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  private String webInfoTitle;
  private String fileName;
  private String webInfoUser;
  public String getWebInfoTitle() {
    return webInfoTitle;
  }
  public void setWebInfoTitle(String webInfoTitle) {
    this.webInfoTitle = webInfoTitle;
  }
  public String getWebInfoUser() {
    return webInfoUser;
  }
  public void setWebInfoUser(String webInfoUser) {
    this.webInfoUser = webInfoUser;
  }
  public String getWebInfoDate() {
    return webInfoDate;
  }
  public void setWebInfoDate(String webInfoDate) {
    this.webInfoDate = webInfoDate;
  }
  public String getWebInfoKeyWord() {
    return webInfoKeyWord;
  }
  public void setWebInfoKeyWord(String webInfoKeyWord) {
    this.webInfoKeyWord = webInfoKeyWord;
  }
  public void setAttachments(List<YHWebInfoAttachment> attachments) {
    this.attachments = attachments;
  }
  private String webInfoDate;
  private String webInfoKeyWord;
  private String content;
  
  public StringBuffer toJSON(boolean isAllContent){
    StringBuffer sb = new StringBuffer("{");
    sb.append("\"fileName\":\"" + this.fileName);
    sb.append("\",\"webInfoUser\":\"" + this.webInfoUser);
    sb.append("\",\"webInfoDate\":\"" + this.webInfoDate);
    sb.append("\",\"webInfoTitle\":\"" + this.webInfoTitle);
    sb.append("\",\"webInfoKeyWord\":\"" + this.webInfoKeyWord);

    if(isAllContent){
      sb.append("\",\"content\":\"" + escape(this.content ));
    }else{
     sb.append("\",\"content\":\"" + escape(YHWebInfo.subStringHTML(this.content, 400, "。。。。")));
    }
    
    sb.append("\",\"attachments\":[");
    for(YHWebInfoAttachment a : this.attachments){
      sb.append(a.toJSON());
      sb.append(",");
    }
    if(this.attachments.size() > 0)
      sb.deleteCharAt(sb.length()-1);
    sb.append("]");
    sb.append("}");
    return sb;
  }
  public String escape(String str){
      int i;
      char j;
      StringBuffer tmp = new StringBuffer();
      tmp.ensureCapacity(str.length()*6);
      for (i=0;i<str.length() ;i++ )
      {
       j = str.charAt(i);
       if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
        tmp.append(j);
       else if (j<256)
        {
        tmp.append( "%" );
        if (j<16)
         tmp.append( "0" );
        tmp.append( Integer.toString(j,16) );
        }
        else
        {
        tmp.append( "%u" );
        tmp.append( Integer.toString(j,16) );
        }
      }
      return tmp.toString();
  }
  public void addAttachment(YHWebInfoAttachment a){
    attachments.add(a);
  }
  public void removeAttachment(String filePath){
    for(YHWebInfoAttachment a : attachments){
      if(a.getFilePath().equals(filePath)){
        attachments.remove(a);
      }
    }
  }
  public List<YHWebInfoAttachment> getAttachments(){
    return this.attachments;
  }
  public int getFileCount(){
    return this.attachments.size();
  }
  public static String subStringHTML(String param,int length,String end) {
    StringBuffer result = new StringBuffer();
    int n = 0;
    char temp;
    boolean isCode = false; //是不是HTML代码
    boolean isHTML = false; //是不是HTML特殊字符,如 
    for (int i = 0; i < param.length(); i++) {
      temp = param.charAt(i);
      if (temp == '<') {
        isCode = true;
      }
      else if (temp == '&') {
        isHTML = true;
      }
      else if (temp == '>' && isCode) {
        n = n - 1;
        isCode = false;
      }
      else if (temp == ';' && isHTML) {
        isHTML = false;
      }
      if (!isCode && !isHTML) {
        n = n + 1;
        //UNICODE码字符占两个字节
        if ( (temp + "").getBytes().length > 1) {
          n = n + 1;
        }
      }
      result.append(temp);
        if (n >= length) {
          break;
        }
      }
   
      result.append(end);

    //取出截取字符串中的HTML标记
    String temp_result = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
    //去掉不需要结素标记的HTML标记
    temp_result = temp_result.replaceAll("< (AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]* >",


                                         "");


    //去掉成对的HTML标记
    temp_result=temp_result.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>","$2");
    //用正则表达式取出标记
    Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>");
    Matcher m = p.matcher(temp_result);
    List endHTML = new ArrayList();
    while (m.find()) {
      endHTML.add(m.group(1));
    }


    //补全不成对的HTML标记


    for (int i = endHTML.size() - 1; i >= 0; i--) {


      result.append("</");


      result.append(endHTML.get(i));


      result.append(">");


     }

    return result.toString();
  }

}
