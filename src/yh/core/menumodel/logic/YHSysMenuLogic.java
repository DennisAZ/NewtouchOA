package yh.core.menumodel.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import yh.core.menumodel.data.YHSysMenu;
import yh.core.util.db.YHORM;
public class YHSysMenuLogic {
  public ArrayList<YHSysMenu> getSysMenuList(Connection conn) throws Exception{
    ArrayList<YHSysMenu> menuList = null;
    YHORM orm = new YHORM();
    Map m = null;
    menuList = (ArrayList<YHSysMenu>)orm.loadListSingle(conn, YHSysMenu.class, m);
    return menuList;
  } 
}
