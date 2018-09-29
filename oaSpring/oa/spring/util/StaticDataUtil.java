package oa.spring.util;

import java.io.File;


/**
 * 静态常量数据类
 * 
 * @author shenrm
 *
 */
public class StaticDataUtil implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1958735743086399798L;
	
	/**
	   * 开帐文件存在应用服务器上的路径
	   */
	  public static final String LOCK_FILE_PATH = ""+File.separator+"WEB-INF"+File.separator+"classes"+File.separator+"";
	  
	/**
	 * 判断系统是否开账的标识,默认是未开账 0
	 */
	public static int sysInitDataFlag = 0; //系统级别的变量,系统还没有进行数据的初始化操作
	
	/**
	 * 接收文件存入Tomcat的路径
	 */
	public static String LOCK_FILE_PATH1 = ""; //系统级别的变量
	
	/**
	 * 判断是否开帐的文件名称
	 */
	 public static String  flagFileName = "YH-00-00-00-00.lock";
	 
	 /**
	  * 文件存入Tomcat下的文件路径
	  */
	 public static String  TOMCAT_PATH = "";
	 
	 public static String  INIT_PAGE = "/springViews/erp/initData/leadPage.jsp";
	
}
