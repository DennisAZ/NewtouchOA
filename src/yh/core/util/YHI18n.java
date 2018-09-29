package yh.core.util;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import yh.core.global.YHMessageKeys;
import yh.core.servlet.YHServletUtility;

public class YHI18n {
  /**
   * 缺省资源文件名称
   */
  private static final String DEFAULT_I18_RESOURCES = 
    "com.td.common.i18n.messages";
  
  /**
   * 应用上下文
   */
  private ServletContext servletContext = null;
  /**
   * 客户端请求
   */
  private HttpServletRequest request = null;
  /**
   * JspClass
   */
  private Class jspClass = null;
  /**
   * 资源邦定
   */
  private ResourceBundle resources = null;
  
  /**
   * 判断是否是中文环境
   * @return
   */
  public boolean isChineseContext() {
    return lm(YHMessageKeys.COMMON_INSERT_OK).indexOf("successfully") < 0;
  }
  /**
   * 缺省构造方法
   *
   */
  public YHI18n() {
    this(DEFAULT_I18_RESOURCES, Locale.CHINA);
  }
  
  /**
   * 构造方法
   * @param resrcName        资源邦定名称
   */
  public YHI18n(String resrcName) {    
    this(resrcName, Locale.getDefault());
  }
  
  /**
   * 构造方法
   * @param resrcName        资源邦定名称
   */
  public YHI18n(Locale currentLocale) {
    this(DEFAULT_I18_RESOURCES, currentLocale);
  }
  
  /**
   * 构造方法
   * @param resrcName        资源名称
   * @param language         语言
   * @param country          国家
   */
  public YHI18n(String resrcName, String language, String country) {
    this(resrcName, new Locale(language, country));
  }
  
  /**
   * 构造方法
   * @param resrcName          资源名称
   * @param currentLocale      当前的本地化对象
   */
  public YHI18n(String resrcName, Locale currentLocale) {
    this.resources = 
      ResourceBundle.getBundle(resrcName, currentLocale);
  }
  
  /**
   * 构造函数
   * @param servletContext
   * @param request
   */
  public YHI18n(ServletContext servletContext,
      HttpServletRequest request,
      Class jspClass) {
    
    this.servletContext = servletContext;
    this.request = request;
    this.jspClass = jspClass;
  }
  
  /**
   * 取得消息名称
   * @param cl
   * @param postFix
   */
  public static String ln(Class cl, String postFix) {
    return cl.getName() + "_" + postFix;
  }
  
  /**
   * 取得消息名称
   * @param cl
   * @param postFix
   */
//  public static String ln(Class cl, String[] fixArray) {
//    StringBuffer rtBuff = new StringBuffer();
//    for (int i = 0; i < fixArray.length; i++) {
//      if (fixArray[i].charAt(0) == TDCTokenSplit.TOKEN_SPLIT_DOLLAR) {
//        rtBuff.append(cl.getName() + "_" + fixArray[i].substring(1));        
//      }else {
//        rtBuff.append(fixArray[i]);
//      }
//      if (i < fixArray.length - 1) {
//        rtBuff.append("$");
//      }
//    }
//    
//    return rtBuff.toString();
//  }
  
  /**
   * 转换名称
   * @param jspName
   * @return  =org.apache.jsp.vouch.rtvouch_jsp => jsp.acctblns.datalist.jsp
   */
  private static String trnsNameJsp(String jspName) {
    StringBuffer rtBuff = new StringBuffer();
    
    rtBuff.append(jspName.substring("org.apache.".length(), jspName.length() - 4));
    rtBuff.append(".jsp");
    return rtBuff.toString();
  }
  
  /**
   * 取得消息名称
   * @param cl
   * @param postFix
   */
  public String lnj(String postFix) {
    StringBuffer rtBuff = new StringBuffer();
    
    rtBuff.append(trnsNameJsp(jspClass.getName()) + "_" + postFix);

    return rtBuff.toString();
  }
  
  /**
   * 取得消息名称
   * @param cl
   * @param postFix
   */
//  public String lnj(String[] fixArray) {
//    StringBuffer rtBuff = new StringBuffer();
//    for (int i = 0; i < fixArray.length; i++) {
//      if (fixArray[i].charAt(0) == TDCTokenSplit.TOKEN_SPLIT_DOLLAR) {
//        rtBuff.append(trnsNameJsp(jspClass.getName()) + "_" + fixArray[i].substring(1));        
//      }else {
//        rtBuff.append(fixArray[i]);
//      }
//      if (i < fixArray.length - 1) {
//        rtBuff.append("$");
//      }
//    }
//    
//    return rtBuff.toString();
//  }
  
  /**
   * 桌面程序中本地化
   * @param cl
   * @param postFix
   * @return
   */
  public String lmd(Class cl, String postFix) {
    return this.lm(ln(cl, postFix));
  }
  
  /**
   * 取得本地字符串
   * @param rscId
   * @param msrg
   * @return
   */
  public String ls(String msrg, String valueStr) {
    return ls(null, msrg, new String[]{valueStr});
  }
  
  /**
   * 取得本地字符串
   * @param rscId
   * @param msrg
   * @return
   */
  public String ls(String msrg, String[] valueArray) {
    return ls(null, msrg, valueArray);
  }
  
  /**
   * 取得本地字符串
   * @param rscId
   * @param msrg
   * @return
   */
  public String ls(String rscId, String msrg, String[] valueArray) {
    if (msrg == null) {
      return "";
    }
    String rtStr = null;
    if (this.resources == null) {
      rtStr = YHServletUtility.getLocaleMsrg(
        servletContext, request, rscId, msrg);
    }else {
      rtStr = this.resources.getString(msrg);
    }
    if (rtStr == null) {
      rtStr = msrg;
    }
    return YHRegexpUtility.assignVar(rtStr, valueArray);
  }
  
  /**
   * 取得本地化的消息
   * @param msrg           消息
   * @return
   */
  public String lm(String msrg) {
    
    return lm(null, msrg);
  }
  
  /**
   * 取得本地化的消息
   * @param rscId          资源ID号
   * @param msrg           消息
   * @return
   */
  public String lm(String rscId, String msrg) {
    
    if (msrg == null) {
      return "";
    }
    String[] msrgArray = msrg.split("\\" +
        String.valueOf(YHTokenSplit.TOKEN_SPLIT_DOLLAR));
    
    StringBuffer rtBuff = new StringBuffer();
    
    for (int i = 0; i < msrgArray.length; i++) {
      String rtStr = null;
      if (this.resources == null) {
        rtStr = YHServletUtility.getLocaleMsrg(
          servletContext, request, rscId, msrgArray[i]);
      }else {
        rtStr = this.resources.getString(msrg);
      }
      if (rtStr != null) {
        rtBuff.append(rtStr);
      }
    }
    
    return rtBuff.toString().trim();
  }
  
  /**
   * 取得本地化的消息
   * @param rscId          资源ID号
   * @param msrg           消息
   * @return
   */
  public String lmj(String msrg) {
    
    return lmj(null, msrg);
  }
  
  /**
   * 取得本地化的消息
   * @param rscId          资源ID号
   * @param msrg           消息
   * @return
   */
  public String lmj(String rscId,
      String msrg) {
   
    StringBuffer rtBuff = new StringBuffer();
    
    rtBuff.append(YHServletUtility.getLocaleMsrg(
        servletContext, request, rscId, lnj(msrg)));
    
    return rtBuff.toString();
  }
  
  /**
   * 取得本地化的消息
   * @param rscId          资源ID号
   * @param msrg           消息
   * @return
   */
  public String lmk(String msrg) {
    
    return lmk(null, msrg);
  }
  
  /**
   * 取得本地化的消息
   * @param rscId          资源ID号
   * @param msrg           消息
   * @return
   */
  public String lmk(String rscId,
      String msrg) {
   
    StringBuffer rtBuff = new StringBuffer();
    
    rtBuff.append(YHServletUtility.getLocaleMsrg(
        servletContext, request, rscId, msrg));
    
    return rtBuff.toString();
  }
}
