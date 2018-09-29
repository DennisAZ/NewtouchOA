package com.psit.struts.util;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

/**
 * spring工具类 <br>
 * create_date: Mar 10, 2011,10:49:19 AM<br>
 * @author rabbit
 */
public class SpringContextTool implements ApplicationContextAware {  
	/**Spring应用上下文环境*/
    private static ApplicationContext applicationContext;
    
    /**实现ApplicationContextAware接口的回调方法，设置上下文环境 
     * @param applicationContext 
     */
    public void setApplicationContext(ApplicationContext applicationContext) {  
        SpringContextTool.applicationContext = applicationContext;  
    }  
    
    public static ApplicationContext getApplicationContext(){
    	return SpringContextTool.applicationContext;
    }
    
    public static SessionFactory getSessionFactory() {  
        Assert.notNull(applicationContext, "applicationContext is null，请确保spring容器正常启动");  
        return (SessionFactory) applicationContext.getBean("sessionFactory");  
    }  
      
    public static ClassMetadata getClassMetadata(Class<?> cls) {  
        return getSessionFactory().getClassMetadata(cls);  
    }  
}  