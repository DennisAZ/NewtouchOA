/**
 * 
 */
package oa.spring.util;

import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import oa.spring.base.IService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * @author lanjinsheng
 *
 */
public class ServiceFactory {
	public final static String  TESTSERVICE="testService";
	private static ServiceFactory serviceFactory=null;
	private static ApplicationContext factory=null;
	private ServiceFactory()
	{
			factory=new ClassPathXmlApplicationContext("classpath:*.xml");

	}
	private ServiceFactory(String xmls)
	{
			factory=new ClassPathXmlApplicationContext(xmls);

	}
   private  ApplicationContext getSpringFactory(){
	   return ServiceFactory.factory;
   }
   public  static ServiceFactory getServiceFactoryInstance(){
	   if(serviceFactory==null)
	   {
		   serviceFactory=  new ServiceFactory();
	   }
	   return serviceFactory;
   }
   public  static ServiceFactory getServiceFactoryInstance(String contextXmls){
	   if(serviceFactory==null)
	   {
		   serviceFactory=  new ServiceFactory(contextXmls);
	   }
	   return serviceFactory;
   }
	public  IService getServiceByName(String name){
		Object obj=factory.getBean(name);
		if (obj!=null)
			return (IService)obj;
		 return null;
	}
	public void ExceptionLogRecord(SQLException e,int type){
		
	}
	public static IService getServiceByName(HttpServletRequest request,String name){
		ServletContext servletContext = request.getSession().getServletContext();
		 
		ApplicationContext context1 = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		Object obj=context1.getBean(name);
		if (obj!=null)
			return (IService)obj;
		 return null;
	}

	
}
