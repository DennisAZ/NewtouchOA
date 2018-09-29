package yh.core.funcs.setdescktop.setports.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.portal.data.YHPort;
import yh.core.funcs.setdescktop.setports.logic.YHDesktopDefineLogic;
import yh.core.funcs.setdescktop.setports.logic.YHMytableLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHDesktopDefineAct {

  private YHDesktopDefineLogic ddl = new YHDesktopDefineLogic();

  /**
   * 获取SYS_PARA表DESKTOP_SELF_DEFINE的五种属性值
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDesktopProperties(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      String properties = ddl.getDesktopProperties(dbConn);

      request.setAttribute(YHActionKeys.RET_DATA, properties);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询桌面属性");

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 设置SYS_PARA表DESKTOP_SELF_DEFINE的五种属性值
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setDesktopProperties(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String desktopPos = "on".equals(request.getParameter("DESKTOP_POS")) ? "POS,"
        : "";
    String desktopWidth = "on".equals(request.getParameter("DESKTOP_WIDTH")) ? "WIDTH,"
        : "";
    String desktopLines = "on".equals(request.getParameter("DESKTOP_LINES")) ? "LINES,"
        : "";
    String desktopScroll = "on".equals(request.getParameter("DESKTOP_SCROLL")) ? "SCROLL,"
        : "";
    String desktopExpand = "on".equals(request.getParameter("DESKTOP_EXPAND")) ? "EXPAND,"
        : "";
    String desktopLeftWidth = request.getParameter("DESKTOP_LEFT_WIDTH");
    String desktopModuleLines = request.getParameter("DESKTOP_MODULE_LINES");
    String desktopModuleScroll = request.getParameter("DESKTOP_MODULE_SCROLL");
    String desktopModuleSetAll = request.getParameter("DESKTOP_MODULE_SET_ALL");

    Map<String, String> map = new HashMap<String, String>();

    map.put("DESKTOP_POS", desktopPos);
    map.put("DESKTOP_WIDTH", desktopWidth);
    map.put("DESKTOP_LINES", desktopLines);
    map.put("DESKTOP_SCROLL", desktopScroll);
    map.put("DESKTOP_EXPAND", desktopExpand);
    map.put("DESKTOP_LEFT_WIDTH", desktopLeftWidth);
    map.put("DESKTOP_MODULE_LINES", desktopModuleLines);
    map.put("DESKTOP_MODULE_SCROLL", desktopModuleScroll);
    map.put("DESKTOP_MODULE_SET_ALL", desktopModuleSetAll);

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      ddl.setDesktopProperties(dbConn, map);

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功设置桌面属性");

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }

    return "/core/funcs/portal/portlet/success.jsp";
  }

  /**
   * 获得user的桌面配置,显示项和未选项
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDesktopConfig(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String pos = request.getParameter("pos");
    YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      YHMytableLogic ml = new YHMytableLogic();

      //所有可选和必选的模块
      List<YHPort> list = ml.listMytableByViewType(dbConn);
      this.ddl.setDesktopConfigToUser(dbConn, user);

      StringBuffer selectedSb = new StringBuffer("selected:[");
      StringBuffer disselectedSb = new StringBuffer("{disselected:[");

      List<YHPort> selected = new ArrayList<YHPort>();

      // 查询用户MYTABLE_LEFT和MYTABLE_RIGHT属性和MYTABLE中的数据项进行对比整理得用户桌面显示项和备选项

      String mytable = "";
      String mytableOther = "";
      if ("l".equals(pos)) {
        mytable = user.getMytableLeft();
        mytableOther = user.getMytableRight();
      } else if ("r".equals(pos)) {
        mytable = user.getMytableRight();
        mytableOther = user.getMytableLeft();
      } else {
        return null;
      }

      if (mytableOther == null) {
        mytableOther = "";
      }

      if (mytable == null) {
        mytable = "";
      }

      final String mytableIdstr = "," + mytable + ",";

      List<String> userSelIdList = Arrays.asList(mytable.split(","));
      List<String> mytableOtherList = Arrays.asList(mytableOther.split(","));
      for (YHPort m : list) {
        if (YHUtility.isNullorEmpty(m.getModulePos())) {
          m.setModulePos("l");
        }
        //用户设置了显示
        if (userSelIdList.contains(String.valueOf(m.getSeqId()))) {
          selected.add(m);
        } else if ("2".equals(m.getViewType())) {
          //必选的模块
          if (pos.equals(m.getModulePos())) {
            if (!mytableOtherList.contains(String.valueOf(m.getSeqId()))) {
              selected.add(m);
            }
          }
        } else if (!mytableOtherList.contains(String.valueOf(m.getSeqId()))) {
          disselectedSb.append(this.toJson(m));
          disselectedSb.append(",");
        }
      }

      Collections.sort(selected, new Comparator<YHPort>() {

        public int compare(YHPort arg0, YHPort arg1) {
          int index0 = mytableIdstr.indexOf("," + arg0.getSeqId() + ",");
          int index1 = mytableIdstr.indexOf("," + arg1.getSeqId() + ",");
          return index0 - index1;
        }
      });

      for (YHPort m : selected) {
        selectedSb.append(this.toJson(m));
        selectedSb.append(",");
      }
      if (selectedSb.charAt(selectedSb.length() - 1) == ',') {
        selectedSb.deleteCharAt(selectedSb.length() - 1);
      }

      if (disselectedSb.charAt(disselectedSb.length() - 1) == ',') {
        disselectedSb.deleteCharAt(disselectedSb.length() - 1);
      }
      disselectedSb.append("],");
      selectedSb.append("]}");
      StringBuffer sb = new StringBuffer(disselectedSb);
      sb.append(selectedSb);

      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功设置桌面属性");

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 获得桌面配置,显示项和未选项,为所有用户设置桌面时使用
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getDesktopConfigForAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String pos = request.getParameter("pos");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      YHMytableLogic ml = new YHMytableLogic();

      List<YHPort> list = ml.listMytableByViewType(dbConn);

      StringBuffer selectedSb = new StringBuffer("selected:[");
      StringBuffer disselectedSb = new StringBuffer("{disselected:[");

      if (pos == null) {
        pos = "l";
      }

      for (YHPort m : list) {
        if (YHUtility.isNullorEmpty(m.getModulePos())) {
          m.setModulePos("l");
        }
        if (m != null && (pos.equals(m.getModulePos()) || "1".equals(m.getViewType()))) {
          if ("2".equals(m.getViewType())) {
            selectedSb.append(this.toJson(m));
            selectedSb.append(",");
          } else {
            disselectedSb.append(this.toJson(m));
            disselectedSb.append(",");
          }
        }
      }
      if (selectedSb.charAt(selectedSb.length() - 1) == ',') {
        selectedSb.deleteCharAt(selectedSb.length() - 1);
      }

      if (disselectedSb.charAt(disselectedSb.length() - 1) == ',') {
        disselectedSb.deleteCharAt(disselectedSb.length() - 1);
      }
      disselectedSb.append("],");
      selectedSb.append("]}");
      StringBuffer sb = new StringBuffer(disselectedSb);
      sb.append(selectedSb);

      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功设置桌面属性");

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }

  /**
   * 设置用户桌面显示项
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setDesktopPortal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String tableLeft = request.getParameter("tableLeft");
    String tableRight = request.getParameter("tableRight");

    if (tableLeft == null) {
      tableLeft = "";
    }

    if (tableRight == null) {
      tableRight = "";
    }

    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession()
          .getAttribute("LOGIN_USER");// 获得登陆用户

      user.setMytableLeft(tableLeft);
      user.setMytableRight(tableRight);

      this.ddl.setUserMytable(dbConn, user);

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功设置桌面属性");

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }

    return "/core/inc/rtjson.jsp";
  }

  /**
   * 设置用户桌面显示项
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setUserMytable(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String pos = request.getParameter("pos");
    String mytable = request.getParameter("mytable");
    YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");// 获得登陆用户
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();

      // mytable属性为cancel时,代表用户未设置任何信息
      if (!"cancel".equals(mytable)) {
        if ("l".equals(pos)) {
          user.setMytableLeft(mytable == null ? "" : mytable);
          this.ddl.setUserMytable(dbConn, user);
        } else if ("r".equals(pos)) {
          user.setMytableRight(mytable == null ? "" : mytable);
          this.ddl.setUserMytable(dbConn, user);
        }
      }

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功设置桌面属性");

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }

    return "/core/inc/rtjson.jsp";
  }

  /**
   * 我的设置应用到其他用户(mytable_left和mytable_right)
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String setMineToOthers(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String userId = request.getParameter("userId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession()
          .getAttribute("LOGIN_USER");

      if (userId != null) {
        for (String s : userId.split(",")) {
          try {
            this.ddl.setMineToOthers(dbConn, Integer.parseInt(s), user);
          } catch (NumberFormatException e) {

          }
        }
      }

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功设置桌面属性");

    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }

    return "/core/inc/rtjson.jsp";
  }

  /**
   * 自定义的把YHMytable对象转化为被选选中组件的可用json格式数据函数
   * 
   * @param m
   * @return
   */
  private StringBuffer toJson(YHPort m) {
    if (m.getStatus() == -1) {
      return null;
    }
    StringBuffer sb = new StringBuffer("{value:\"");
    sb.append(m.getSeqId());
    sb.append("\",text:\"");
    sb.append(m.getFileName().replaceAll(".js", ""));
    if ("2".equals(m.getViewType())) {
      sb.append("[必选]\",isMustSelect:true");
    } else {
      sb.append("\"");
    }
    sb.append("}");
    return sb;
  }
}
