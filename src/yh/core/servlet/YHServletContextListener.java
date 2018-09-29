package yh.core.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class YHServletContextListener implements ServletContextListener {
  private static Logger log = Logger.getLogger(YHServletContextListener.class);
  public void contextInitialized(ServletContextEvent event) {
  }

  public void contextDestroyed(ServletContextEvent event) {
  }
}
