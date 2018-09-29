package com.psit.struts.util;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 得到spring配置文件数据源（单例） <br>
 * create_date: Jan 7, 2011,12:26:39 PM<br>
 * @author rabbit
 */
public class GetAppXml {
	private String driverName = "";
	private String dbUrl = "";
	private String dbUser = "";
	private String dbPassword = "";
	private static GetAppXml instance = new GetAppXml(); //实例化私有静态对象
	/**
	* 私有构造函数，不能在外部被实例化
	*/
	/* 解析xml文件方式读取配置文件
	private GetAppXml() {
		//读取配置文件
		String webClassPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String rootPath = webClassPath.substring(0,webClassPath.lastIndexOf("classes"));
		SAXReader reader = new SAXReader();
		String url =  rootPath+"applicationContext.xml";
		Document document;
		try {
			document = reader.read(new File(url));
			Element root = document.getRootElement();
			List<Element> beanNodes = root.elements("bean");
			Iterator<Element> beanIt = beanNodes.iterator();

			while(beanIt.hasNext()){
				Element beanEl = beanIt.next();
				if(beanEl.attribute("id")!=null&&beanEl.attribute("id").getValue().equals("dataSource")){
					List<Element> proNodes = beanEl.elements("property");
					Iterator<Element> proIt = proNodes.iterator();
					while(proIt.hasNext()){
						Element proEl = proIt.next();
						if(proEl.attribute("name").getValue().equals("driverClassName")){
							driverName = proEl.attribute("value").getValue();
						}
						else if(proEl.attribute("name").getValue().equals("url")){
							dbUrl = proEl.attribute("value").getValue();
						}
						else if(proEl.attribute("name").getValue().equals("username")){
							dbUser = proEl.attribute("value").getValue();
						}
						else if(proEl.attribute("name").getValue().equals("password")){
							dbPassword = proEl.attribute("value").getValue();
						}
					}
					break;
				}
				
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}*/
	private GetAppXml() {
		//读取配置文件
		com.mchange.v2.c3p0.ComboPooledDataSource t = (com.mchange.v2.c3p0.ComboPooledDataSource)SpringContextTool.getApplicationContext().getBean("dataSource");
		driverName = t.getDriverClass();
		dbUrl = t.getJdbcUrl();
		dbUser = t.getUser();
		dbPassword = t.getPassword();
	}
	
	public static GetAppXml getInstance(){
		return instance;
	}

	public String getDriverName() {
		return driverName;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}
}
