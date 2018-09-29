package yh.subsys.oa.profsys.source.person.logic;
import java.sql.Connection;
import java.sql.Date;
import java.util.Map;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.profsys.source.person.data.YHSourcePerson;

public class YHSourcePersonLogic {
  /**
   * 新建人士
   * 
   * @return
   * @throws Exception
   */
  public static void addPerson(Connection dbConn, YHSourcePerson person) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, person);
  }
  /**
   * 修改人士
   * 
   * @return
   * @throws Exception
   */
  public static void updatePerson(Connection dbConn, YHSourcePerson person) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, person);
  }
  
  /**
   * 查询详情信息
   * 
   * @return
   * @throws Exception
   */
  public static YHSourcePerson showDetail(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    YHSourcePerson person = (YHSourcePerson)orm.loadObjSingle(dbConn,YHSourcePerson.class,seqId);
    return person;
  }
  /**
   * 删除详情信息
   * 
   * @return
   * @throws Exception
   */
  public static void deleteInfo(Connection dbConn,int seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn,YHSourcePerson.class,seqId);
  } 
  /***
   * 根据条件查询数据,通用列表显示数据,实现分页
   * @return
   * @throws Exception 
   */
  public static String selectPerson(Connection dbConn,Map request,YHSourcePerson person,Date birthDay) throws Exception {
    String sql = "select SEQ_ID,PER_NUM,PER_NAME,PER_SEX,PER_NATION"
      + ",PER_POSITION,PER_VOCATION,PER_BIRTHDAY,PER_RESUME,PER_EXPERIENCE,PER_NOTE"
      + ",ATTACHMENT_ID,ATTACHMENT_NAME from oa_src_person"
      + " where 1=1 ";
    if (!YHUtility.isNullorEmpty(person.getPerNum())) {
      sql += " and PER_NUM like '%" +  YHUtility.encodeLike(person.getPerNum()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(person.getPerPosition())) {
      sql += " and " + YHDBUtility.findInSet(person.getPerPosition(), "PER_POSITION");
      //sql += " and PER_POSITION like '%" +  YHUtility.encodeLike(person.getPerPosition()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(person.getPerNation())) {
      sql += " and PER_NATION like '%" +  YHUtility.encodeLike(person.getPerNation()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(person.getPerVocation())) {
      sql += " and PER_VOCATION like '%" +  YHUtility.encodeLike(person.getPerVocation()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(person.getPerName())) {
      sql += " and PER_NAME like '%" +  YHUtility.encodeLike(person.getPerName()) + "%' " + YHDBUtility.escapeLike();
    }
    if (!YHUtility.isNullorEmpty(person.getPerSex())) {
      sql += " and PER_SEX ='" + person.getPerSex() + "'";
    }
    if (person.getPerBirthday() != null) {
      String str =  YHDBUtility.getDateFilter("PER_BIRTHDAY", YHUtility.getDateTimeStr(person.getPerBirthday()), ">=");
      sql += " and " + str;
    }
    if (birthDay != null) {
      String str =  YHDBUtility.getDateFilter("PER_BIRTHDAY", YHUtility.getDateTimeStr(birthDay), "<=");
      sql += " and " + str;
    }
    YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request);
    YHPageDataList pageDataList = YHPageLoader.loadPageList(dbConn,queryParam,sql);
    return pageDataList.toJson();
  }
}
