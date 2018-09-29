package yh.core.funcs.jexcel.logic;

import java.util.ArrayList;

import yh.core.data.YHDbRecord;

public class YHExportLogic {
  
  public ArrayList<YHDbRecord> getDbRecord(){
    ArrayList<YHDbRecord > dbL = new ArrayList<YHDbRecord>();
    for (int i = 0; i < 10; i++) {
      YHDbRecord dbrec = new YHDbRecord();
      dbrec.addField("好", "1" + i);
      dbrec.addField("好1", "1");
      dbrec.addField("好2", "1");
      dbrec.addField("好43", "1");
      dbrec.addField("好4", "1");
      dbrec.addField("好5", "1");
      dbrec.addField("好6", "1");
      dbrec.addField("好7", "1");
      dbL.add(dbrec);
    }
    return dbL;
  }
}
