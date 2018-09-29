package yh.core.menumodel.act;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.menumodel.data.YHSysFunction;
import yh.core.menumodel.data.YHSysMenu;
import yh.core.menumodel.logic.YHSysMenuLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHDTJ;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHSysMenuAct {
  ArrayList<YHSysFunction> functionList = null;
  YHSysMenuLogic menuLogic = new YHSysMenuLogic();
  public String listSysMenu(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      ArrayList<YHSysMenu> menuList = null;
      menuList = menuLogic.getSysMenuList(dbConn);
      
      request.setAttribute("menuList", menuList);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/menumodel/sysmenulist.jsp";
  }
}
