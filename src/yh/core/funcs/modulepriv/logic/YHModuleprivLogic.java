package yh.core.funcs.modulepriv.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.funcs.modulepriv.data.YHModulePriv;
import yh.core.funcs.person.data.YHPerson;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
public class YHModuleprivLogic {
  private static Logger log = Logger.getLogger(YHModuleprivLogic.class);
 
  public boolean findToId(String object,String object2){
    boolean temp = false;
    if(object != null && !"".equals(object)){
      String[] toIds = object.split(",");
      for(int j = 0 ;j < toIds.length ; j++){
        String toIdTemp =  toIds[j];
        if(toIdTemp.equals(object2)){
          temp = true;
          break;
        }
      }
    }
    return temp;
  }
  
//  function check_id($STRING,$ID,$FLAG)
//  {
//     $MY_ARRAY=explode(",",$ID);
//     $ARRAY_COUNT=sizeof($MY_ARRAY);
//     if($MY_ARRAY[$ARRAY_COUNT-1]=="")$ARRAY_COUNT--;
//     for($I=0;$I<$ARRAY_COUNT;$I++)
//     {
//       if($FLAG)
//       {
//          if(find_id($STRING,$MY_ARRAY[$I]))
//             $ID_STR.=$MY_ARRAY[$I].",";
//       }
//       else
//       {
//          if(!find_id($STRING,$MY_ARRAY[$I]))
//             $ID_STR.=$MY_ARRAY[$I].",";
//       }
//     }
//     return $ID_STR;
//  }
  public String checkId(String object,String idObject,boolean flag) {
    String toIdStr = "";
    if(!"".equals(idObject)&&idObject!=null){
      String[] myArray = idObject.split(",");
      int arrayCount = myArray.length;
      if("".equals(myArray[arrayCount-1])) {
        arrayCount --;
      }
      for(int i = 0;i < arrayCount;i ++){
        if(flag){
          if(findToId(object,myArray[i])) {
            toIdStr = toIdStr + myArray[i] + ",";
          }
        }else {
          if(!findToId(object,myArray[i])) {
            toIdStr = toIdStr + myArray[i] + ",";
          }
        }
      }
    }
    return toIdStr;
  }
  /*
   * 查询person表里要设置权限的人，满足以下条件的用户都查出来
   *   不是系统管理员的（他不用设置）
   *   应用到其他用户--“所属角色”里指定的角色相关用户（注："所在部门"条件在while循环里再加，这里可能不好加）
   *   当前正在编辑的用户
   */
  public void queryNeedSetMoudle(Connection conn,YHModulePriv modulepriv,String apply_to_priv,String apply_to_dept,String apply_to_module)throws Exception {
    Statement stmt = null;
    Statement setModulestmt = null;
    ResultSet rs = null;
    ResultSet setModulers = null;
    String userPriv = null;
    YHPerson person = null;
    String dept_priv = modulepriv.getDeptPriv();
    String role_priv = modulepriv.getRolePriv();
    String dept_id = modulepriv.getDeptId();
    String priv_id = modulepriv.getPrivId();
    String user_id = modulepriv.getUserId();
    int userSeqId = modulepriv.getUserSeqId();
    int userSeqIdtemp = 0;
    int seq_id = 0;
    List list = new ArrayList();
    String[] applyModules = apply_to_module.split(",");
   
    //将应用到其他用户所属角色2,5, 转换成'2','5'
    if(apply_to_priv != null && !"".equals(apply_to_priv)){
      String[] applyToPrivs = apply_to_priv.split(",");
      apply_to_priv = "";
      for(int i = 0 ;i < applyToPrivs.length ; i++){
        apply_to_priv +=  "'" + applyToPrivs[i] + "'" + ",";
      }
      apply_to_priv = apply_to_priv.substring(0, apply_to_priv.length() - 1);
    }
    
    try{   
 //     String query1Str = "select SEQ_ID,DEPT_ID,DEPT_ID_OTHER from PERSON p where p.USER_ID!='admin' and (p.USER_PRIV in ("+apply_to_priv+")  or p.SEQ_ID='"+userSeqId+"')";
      String queryStr = "select SEQ_ID,DEPT_ID,DEPT_ID_OTHER from PERSON p where p.USER_ID!='admin' and (";
      if(!"".equals(apply_to_priv)&&apply_to_priv!=null) {
        queryStr = queryStr + "p.USER_PRIV in ("+apply_to_priv+")  or ";
      }
      queryStr = queryStr + "p.SEQ_ID='"+userSeqId+"')";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      while(rs.next()) {
        if(userSeqId!=rs.getInt("SEQ_ID")&&!"".equals(apply_to_dept)&&apply_to_dept!="ALL_DEPT"&&!findToId(apply_to_dept,rs.getString("DEPT_ID"))&&"".equals(checkId(apply_to_dept,rs.getString("DEPT_ID_OTHER"),true))) { //不是当前正在操作的用户          continue;
        }
        userSeqIdtemp = rs.getInt("SEQ_ID");
//        System.out.println(seqIdtemp);
//        userIdtemp = userSeqIdtemp;
        for(int i=0;i<applyModules.length;i++) {
          String moduleIdtemp =  applyModules[i].trim();
          if("".equals(moduleIdtemp)) {
            continue;
          }         
          if(!("".equals(dept_priv)||"".equals(role_priv))) {
            String setmoduleStr = "select * from oa_function_priv m where m.USER_SEQ_ID='"+userSeqIdtemp+"' and m.MODULE_ID='"+moduleIdtemp+"'";
            setModulestmt = conn.createStatement();
            setModulers = setModulestmt.executeQuery(setmoduleStr);
            if(setModulers.next()) {
              seq_id = setModulers.getInt("SEQ_ID");
              if("4".equals(dept_priv)) {
                user_id = Integer.toString(userSeqIdtemp);
              }
                String query="update oa_function_priv set DEPT_PRIV='"+dept_priv+"',ROLE_PRIV='"+role_priv+"',DEPT_ID='"+dept_id+"',PRIV_ID='"+priv_id+"',USER_ID='"+user_id+"' where USER_SEQ_ID='"+userSeqIdtemp+"' and MODULE_ID='"+moduleIdtemp+"'";
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
            
            }else {
              if("4".equals(dept_priv)) {
                user_id = Integer.toString(userSeqIdtemp);
              }
                String query = "insert into oa_function_priv (MODULE_ID,DEPT_PRIV,ROLE_PRIV,DEPT_ID,PRIV_ID,USER_SEQ_ID,USER_ID) values ('"+moduleIdtemp+"','"+dept_priv+"','"+role_priv+"','"+dept_id+"','"+priv_id+"','"+userSeqIdtemp+"','"+user_id+"')";
                stmt = conn.createStatement();
                stmt.execute(query);
            }
          }else {
            String deleteSql = "delete from oa_function_priv where USER_SEQ_ID='"+userSeqIdtemp+"' and MODULE_ID='"+moduleIdtemp+"'";
            stmt = conn.createStatement();
            stmt.execute(deleteSql);
          }
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  public static List<YHModulePriv> selectModulePriv(Connection dbConn,String userId) throws Exception{
    List<YHModulePriv> moduleList = new ArrayList<YHModulePriv>();
    String[] str = {"USER_SEQ_ID = " + userId ,"MODULE_ID = 3"};
    YHORM orm = new YHORM();
    moduleList  =  orm.loadListSingle(dbConn, YHModulePriv.class, str);
    return moduleList;
  }
}
