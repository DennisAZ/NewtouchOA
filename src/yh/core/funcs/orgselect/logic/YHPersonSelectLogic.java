package yh.core.funcs.orgselect.logic;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yh.core.funcs.person.data.YHPerson;
import yh.core.util.db.YHORM;

public class YHPersonSelectLogic {
  /**
   * 根据部门Id取得人员列表
   * @param dbConn
   * @param deptId
   * @return
   * @throws Exception
   */
  public List<YHPerson> getPersonsByDept(Connection dbConn, int deptId) throws Exception{
    List<YHPerson> list = new ArrayList();
    Map filters = new HashMap();
    filters.put("DEPT_ID", deptId);
    YHORM orm = new YHORM();
    list  = orm.loadListSingle(dbConn ,YHPerson.class , filters);
    return  list;
  }
}
