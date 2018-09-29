package yh.core.funcs.utilapps.info.bus.logic;

import java.sql.Connection;

import yh.core.funcs.utilapps.info.bus.data.YHBus;
import yh.core.util.db.YHORM;

public class YHBusLogic {
  
  public void addBus(Connection conn,YHBus bus) throws Exception{
    try{
      YHORM orm = new YHORM();
      orm.saveSingle(conn, bus);
    }catch(Exception ex) {
      throw ex;
    }
  }
  
  public void updateBus(Connection conn,YHBus bus) throws Exception{
    try{
      YHORM orm = new YHORM();
      orm.updateSingle(conn, bus);
    }catch(Exception ex) {
      throw ex;
    }
  }
}
