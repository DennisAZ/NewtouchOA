package yh.rad.dsdef.logic.praserI;

import java.sql.Connection;
import java.util.ArrayList;

import yh.core.data.YHDsField;

public interface YHColumnPraserI {
  public void execPhyicsSql(Connection conn,String tableName,ArrayList<YHDsField> dsFields) throws Exception;
}
