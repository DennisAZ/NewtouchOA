package yh.core.funcs.doc.logic;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析html文件
 * @author TTlang
 *
 */
public class YHFlowFormViewLogic{

  public static Matcher praserHTML(String regex,String htmlCode ,int start){
    Pattern pattern = Pattern.compile(regex);  
    Matcher matcher = pattern.matcher(htmlCode);  
    //debug
    //System.out.println("需要解析的字符串 ：" + htmlCode);
    //System.out.println("解析的格式 ：" + regex);
    if(start == -1){
      matcher.find();
    }else{
      matcher.find(start);
    }
    return matcher;
  }
  
  public static void praserHTML(String startRegex,String endRegex,String htmlCode){
    //Pattern pattern = Pattern.compile(startRegex);  
    //Matcher matcher = pattern.matcher(htmlCode);  
    //debug
    ArrayList<String> list = new ArrayList<String>();
    //System.out.println("需要解析的字符串 ：" + htmlCode);
    //System.out.println("起始解析的格式 ：" + startRegex);
    //System.out.println("结束解析的格式 ：" + endRegex);
    int start = -1;
    int len = htmlCode.length();
    String newHtmlCode = htmlCode;
    while( start < len ){
      Matcher matcher = praserHTML(startRegex,htmlCode ,start);
      int strStart = matcher.start();
      int matStart = matcher.end() ;
      matcher = praserHTML(endRegex,htmlCode ,matStart);
  
      int strEnd = matcher.end();
      //System.out.println("匹配的字符串为 ： " + htmlCode.substring(strStart, strEnd)); 
      String htmldom =  htmlCode.substring(strStart, strEnd);
      matcher = praserHTML("name",htmldom ,-1);
      String name = htmldom.substring(matcher.end() + 1, matcher.end() + 7);
      newHtmlCode =  matcher.replaceAll("{" + name + "}");
      start = strEnd;
      //System.out.println("解析后的字符串为 ： " +newHtmlCode);
    }  
  }
  public static void praserHTMLT(String regex,String htmlCode ){
    
    Pattern pattern = Pattern.compile(regex);  
    Matcher matcher = pattern.matcher(htmlCode);
    //System.out.println("需要解析的字符串 ：" + htmlCode);
    //System.out.println("起始解析的格式 ：" + regex);
    boolean found = false; 
    int start = 0;
    int end = -1 ;
    while (matcher.find()) {
      end = matcher.start();
      //System.out.println("起始位置：" + start + " ， 结束位置 ： " + end + " ,匹配字符：\"" + matcher.group() +"\"");
      if(end != start){
        String arr = htmlCode.substring(start + 1, end);
        //System.out.println(arr);
        String[] arrs = arr.split("=");
        for (String string : arrs){
          //System.out.println(string);
        }
      }
      found = true; 
      start = matcher.start();
    }  
    String arr = htmlCode.substring(end + 1, htmlCode.length());
    //System.out.println(arr);
    if (!found) {  
      //System.out.printf("No match found.%n");  
    }
  }
}
