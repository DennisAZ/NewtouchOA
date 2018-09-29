package yh.subsys.portal.bjca.logic;

import java.sql.Connection;

import com.bjca.uums.client.bean.LoginInformation;
import com.bjca.uums.client.bean.PersonInformation;

import yh.subsys.portal.bjca.util.YHBjcaUtil;
import yh.user.api.core.db.YHDbconnWrap;
/**
 * Bjca数据同步接口
 * @author Think
 *
 */
public class YHBjcaSynchronization {

  /**
   * Bjca数据同步方法
   * @param operateID 操作符
   * @param operateCode 编码(用户的32位编码)
   * @param operateType(类型，一般不需要考虑)
   * @return
   */
  public boolean SynchronizedUserInfo(int operateID, String operateCode,
      String operateType)throws Exception{
    YHDbconnWrap dbUtil = new YHDbconnWrap();
    boolean result = false;
    Connection conn = null;
    try {
      conn = dbUtil.getSysDbConn();
      if (operateID == 11 || operateID == 12 || operateID == 13) {
        // 增加用户
        if(operateID == 11 || operateID == 12){
          PersonInformation personInfo = YHBjcaUtil.getPersonInformationById(operateCode);
          LoginInformation loginInfo = YHBjcaUtil.getLoginUserInfoByUserId(operateCode);
          //System.out.println(YHBjcaUtil.isExisPerson(conn, operateCode));
          if(YHBjcaUtil.isExisPerson(conn, operateCode) != -1 || YHBjcaUtil.isExisPersonByAdmin(conn, loginInfo.getLoginName()) != -1){
            YHBjcaUtil.modifyUser(conn, personInfo, loginInfo);
          }else{
            YHBjcaUtil.addUser(conn, personInfo, loginInfo);
          }
        }else{
          if(YHBjcaUtil.isExisPerson(conn, operateCode) != -1){
            YHBjcaUtil.deleteUser(conn, operateCode);
          }
        }
        result = true;
      }else if(operateID == 41 || operateID == 42 || operateID == 43){
        // 增加部门
        if(operateID == 41 || operateID == 42){
         
          if(YHBjcaUtil.getYHDeptIdBySynDeptCode(conn, operateCode) != -1){
            YHBjcaUtil.modifyDept(conn, operateCode);
          }else{
            YHBjcaUtil.addDept(conn, operateCode);
          }
        }else{
          if(YHBjcaUtil.getYHDeptIdBySynDeptCode(conn, operateCode) != -1){
            YHBjcaUtil.deleteDept(conn, operateCode);
          }
        }
        result = true;
      }
      conn.commit();
    } catch (Exception e) {
      conn.rollback();
      throw e;
    } finally {
      dbUtil.closeAllDbConns();
    }
    return result;
  }
}
