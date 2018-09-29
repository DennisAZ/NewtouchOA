package yh.core.funcs.system.act.imp;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.logic.YHSystemLogic;

public interface YHLoginValidator {
  public static Logger log = Logger.getLogger("yh.core.funcs.system.act.imp.YHLoginValidator");
  public static final YHSystemLogic logic = new YHSystemLogic();
  public boolean isValid(HttpServletRequest request, YHPerson person, Connection conn) throws Exception;
  public void addSysLog(HttpServletRequest request, YHPerson person, Connection conn) throws Exception;
  public String getValidatorType();
  public int getValidatorCode();
  public String getValidatorMsg();
}
