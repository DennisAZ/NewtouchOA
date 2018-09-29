package yh.core.funcs.doc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;

import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowFormItem;
import yh.core.funcs.doc.data.YHDocFlowProcess;
import yh.core.funcs.doc.data.YHDocRun;
import yh.core.funcs.doc.data.YHDocFlowRunData;
import yh.core.funcs.doc.logic.YHFlowRunLogLogic;
import yh.core.funcs.doc.util.sort.YHUserSortComparatorUtility;
import yh.core.module.org_select.logic.YHOrgSelectLogic;
import yh.core.util.YHOut;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHTurnConditionUtility {
  /**
   * 主要用于子流程返回父流程
   * @param user
   * @param fp
   * @param flowRun
   * @param conn
   * @return
   * @throws Exception
   */
  public String userSelect2(Connection conn,  YHPerson user , String prcsBack , int flowId ,String autoUserOp,String ids) throws Exception{
    String prcsOpUser = "";
    String prcsOpUserName = "";
    String prcsUserAuto = "";
    String prcsUserName = "";
    boolean isAutoSelect = false;
    String prcsUser = "";
    String prcsDept ="" ;
    String prcsPriv = "" ;
    String prcsNewUserId = "";
    String prcsNewUserName = "";
    int prcsNewDeptId= 0;
    String prcsNewUserPriv = "";
    String prcsNewUserPrivOther = "";
    
    StringBuffer sb = new StringBuffer();
    String query2  = "SELECT * from "+ YHWorkFlowConst.FLOW_PROCESS +" where FLOW_SEQ_ID='"+ flowId +"' and PRCS_ID='"+prcsBack+"'";
    Statement stm2 = null;
    ResultSet rs2 = null;
    
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query2);
      if (rs2.next()){
        prcsUser = rs2.getString("PRCS_USER");
        prcsDept = rs2.getString("PRCS_DEPT");
        prcsPriv = rs2.getString("PRCS_PRIV");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
  //默认经办人
    if (!YHUtility.isNullorEmpty(ids) && !YHUtility.isNullorEmpty(autoUserOp) ) {
      if (autoUserOp.endsWith(",")) {
        autoUserOp = prcsOpUser.substring(0, autoUserOp.length() - 1);
      }
      YHORM orm = new YHORM();
      YHPerson person = (YHPerson) orm.loadObjSingle(conn, YHPerson.class, Integer.parseInt(autoUserOp));
      if(person != null){
        prcsNewUserId = autoUserOp;
        prcsNewUserName  = person.getUserName();
        prcsNewDeptId = person.getDeptId();
        prcsNewUserPriv = person.getUserPriv();
        prcsNewUserPrivOther = person.getUserPrivOther();
        
        //是否有经办权限
        if("ALL_DEPT".equals(prcsDept) || "0".equals(prcsDept) 
            || YHWorkFlowUtility.findId(prcsUser , prcsNewUserId)
            || YHWorkFlowUtility.findId(prcsDept , String.valueOf(prcsNewDeptId))
            || YHWorkFlowUtility.findId(prcsPriv , prcsNewUserPriv)
            || YHWorkFlowUtility.privOther(prcsPriv , prcsNewUserPrivOther) == 1){
          prcsOpUser = prcsNewUserId;
          prcsOpUserName = prcsNewUserName ;
        }
      }
    }
    if (ids.endsWith(",")) {
      ids =  ids.substring(0, ids.length() - 1);
    }
    if (!"".equals(ids)) {
      String findSql  = "SELECT SEQ_ID,DEPT_ID,USER_PRIV,USER_NAME,USER_PRIV_OTHER from PERSON where SEQ_ID IN (" + ids + ")";
      Statement stm9 = null;
      ResultSet rs9 = null;
      try {
        stm9 = conn.createStatement();
        rs9 = stm9.executeQuery(findSql);
        while(rs9.next()){
          String userId = rs9.getString("SEQ_ID");
          int deptId = rs9.getInt("DEPT_ID");
          String userPriv = rs9.getString("USER_PRIV");
          String userName = rs9.getString("USER_NAME");
          String userPrivOther = rs9.getString("USER_PRIV_OTHER");
       
          if("ALL_DEPT".equals(prcsDept) || "0".equals(prcsDept) 
              || YHWorkFlowUtility.findId(prcsUser , userId)
              || YHWorkFlowUtility.findId(prcsDept , String.valueOf(deptId))
              || YHWorkFlowUtility.findId(prcsPriv , userPriv)
              || YHWorkFlowUtility.privOther(prcsPriv , userPrivOther) == 1){
            prcsUserAuto += userId + ",";
            prcsUserName += userName + ",";
          }
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm9, rs9, null); 
      }
      isAutoSelect = true;
    }
    sb.append ( "isAutoSelect:"+ isAutoSelect +",prcsOpUser:'" + prcsOpUser
        + "',prcsOpUserName:'" + prcsOpUserName 
        + "',prcsUser:'" + prcsUserAuto + "',prcsUserName:'" + prcsUserName + "'" ) ;
    return sb.toString();
  }
  /**
   * 取得自动选择的人员的相关数据
   * @param user 登陆用户
   * @param fp 下一步的一个流程
   * @param flowRun 当前的一个流程实例 
   * @return
   * @throws Exception
   */
  public String userSelect( YHPerson user , YHDocFlowProcess fp , YHDocRun flowRun , Connection conn ) throws Exception{
    YHORM orm = new YHORM();
    StringBuffer sb = new StringBuffer();
    String prcsUser = fp.getPrcsUser() == null ? "" : fp.getPrcsUser();
    String prcsDept = YHOrgSelectLogic.changeDept(conn, fp.getPrcsDept()); 
    prcsDept = prcsDept == null ? "" : prcsDept;
    String prcsPriv = fp.getPrcsPriv() == null ? "" : fp.getPrcsPriv();
    String prcsNewUserId = "";
    String prcsNewUserName = "";
    int prcsNewDeptId= 0;
    String prcsNewUserPriv = "";
    String prcsNewUserPrivOther = "";
    
    String prcsOpUser = "";
    String prcsOpUserName = "";
    String prcsUserAuto = "";
    String prcsUserName = "";
    boolean isAutoSelect = false;
    //子流程没处理
    if ( fp.getChildFlow() != 0 ) {
      String query2  = "SELECT * from "+ YHWorkFlowConst.FLOW_PROCESS +" where FLOW_SEQ_ID='"+ fp.getChildFlow() +"' and PRCS_ID='1'";
      Statement stm2 = null;
      ResultSet rs2 = null;
      
      try {
        stm2 = conn.createStatement();
        rs2 = stm2.executeQuery(query2);
        if (rs2.next()){
          //prcsIdNext = "1";
          prcsUser = rs2.getString("PRCS_USER");
          prcsDept = rs2.getString("PRCS_DEPT");
          prcsPriv = rs2.getString("PRCS_PRIV");
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm2, rs2, null); 
      }
      sb.append ( "isAutoSelect:"+ isAutoSelect +",prcsOpUser:'" + prcsOpUser
          + "',prcsOpUserName:'" + prcsOpUserName 
          + "',prcsUser:'" + prcsUserAuto + "',prcsUserName:'" + prcsUserName + "'" ) ;
      return sb.toString();
    }
    //是否设置经办人,没有设置,则返回noPriv:true;
    if("".equals(prcsUser) && "".equals(prcsDept) && "".equals(prcsPriv)){
      return "noPriv:true";
    }
    //自动选择流程发起人
    if ( fp.getAutoType() != null && fp.getAutoType().equals("1") ) {
      if ( flowRun != null ) {
        int userId = flowRun.getBeginUser();
        YHPerson person = (YHPerson) orm.loadObjSingle(conn, YHPerson.class, userId);
        if(person != null){
          prcsNewUserId = String.valueOf(userId);
          prcsNewUserName  = person.getUserName();
          prcsNewDeptId = person.getDeptId();
          prcsNewUserPriv = person.getUserPriv();
          prcsNewUserPrivOther = person.getUserPrivOther();
        }
      }
      //是否有经办权限
      if("ALL_DEPT".equals(prcsDept) 
          || YHWorkFlowUtility.findId(prcsUser , prcsNewUserId)
          || YHWorkFlowUtility.findId(prcsDept , String.valueOf(prcsNewDeptId))
          || YHWorkFlowUtility.findId(prcsPriv , prcsNewUserPriv)
          || YHWorkFlowUtility.privOther(prcsPriv , prcsNewUserPrivOther) == 1){
        prcsOpUser = prcsNewUserId;
        prcsOpUserName = prcsNewUserName ;
        prcsUserAuto = prcsNewUserId + ",";
        prcsUserName = prcsNewUserName + ",";
      }
      isAutoSelect = true;
    } else if( fp.getAutoType() != null && fp.getAutoType().equals("3")){
      String autoUser = fp.getAutoUser();
      String autoUserOp = fp.getAutoUserOp();
      //默认主办人
      if(autoUser != null && !"".equals(autoUser)){
        if(autoUserOp != null && !"".equals(autoUserOp)){
          if (autoUserOp.endsWith(",")) {
            autoUserOp = autoUserOp.substring(0, autoUserOp.length() - 1);
          }
          YHPerson person = (YHPerson) orm.loadObjSingle(conn, YHPerson.class, Integer.parseInt(autoUserOp));
          if(person != null){
            prcsNewUserId = autoUserOp;
            prcsNewUserName  = person.getUserName();
            prcsNewDeptId = person.getDeptId();
            prcsNewUserPriv = person.getUserPriv();
            prcsNewUserPrivOther = person.getUserPrivOther();
            
            //是否有经办权限
            if("ALL_DEPT".equals(prcsDept) 
                || YHWorkFlowUtility.findId(prcsUser , prcsNewUserId)
                || YHWorkFlowUtility.findId(prcsDept , String.valueOf(prcsNewDeptId))
                || YHWorkFlowUtility.findId(prcsPriv , prcsNewUserPriv)
                || YHWorkFlowUtility.privOther(prcsPriv , prcsNewUserPrivOther) == 1){
              prcsOpUser = prcsNewUserId;
              prcsOpUserName = prcsNewUserName ;
            }
          }
        }
        //默认经办人
        //String findSql = YHWorkFlowUtility.createFindSql("SEQ_ID", fp.getAutoUser());
        String ids = fp.getAutoUser();
        if (ids.endsWith(",")) {
          ids =  ids.substring(0, ids.length() - 1);
        }
        String findSql  = "SELECT SEQ_ID,DEPT_ID,USER_PRIV,USER_NAME,USER_PRIV_OTHER from PERSON where SEQ_ID IN (" + ids + ")";
        
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(findSql);
          while(rs.next()){
            String userId = rs.getString("SEQ_ID");
            int deptId = rs.getInt("DEPT_ID");
            String userPriv = rs.getString("USER_PRIV");
            String userName = rs.getString("USER_NAME");
            String userPrivOther = rs.getString("USER_PRIV_OTHER");
         
            if("ALL_DEPT".equals(prcsDept) 
                || YHWorkFlowUtility.findId(prcsUser , userId)
                || YHWorkFlowUtility.findId(prcsDept , String.valueOf(deptId))
                || YHWorkFlowUtility.findId(prcsPriv , userPriv)
                || YHWorkFlowUtility.privOther(prcsPriv , userPrivOther) == 1){
              prcsUserAuto += userId + ",";
              prcsUserName += userName + ",";
            }
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
        isAutoSelect = true;
      }
    //自动选择部门
    }  else if( "2".equals(fp.getAutoType()) || "4".equals(fp.getAutoType()) || "5".equals(fp.getAutoType()) || "6".equals(fp.getAutoType())) {
      int autoBaseUser = fp.getAutoBaseUser();
      int baseDeptId = user.getDeptId();
      int tmpDeptId = 0 ;
      if (autoBaseUser != 0) {
        //查出争对步骤的主办人员

        String query = "select "
          + " USER_ID"
          + " FROM "+ YHWorkFlowConst.FLOW_RUN_PRCS +" "
          + " WHERE RUN_ID =" + flowRun.getRunId()
          + " AND FLOW_PRCS = '" + fp.getAutoBaseUser() + "'"
          + " AND OP_FLAG = 1 "
          + " ORDER BY PRCS_ID ";
        String baseUserId = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query);
          if(rs.next()){
            baseUserId = rs.getString("USER_ID");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
        if (!YHUtility.isNullorEmpty(baseUserId)) {
          query = "select " 
            + " DEPT_ID " 
            + " FROM PERSON WHERE " 
            + " SEQ_ID=" + baseUserId;
          Statement stm2 = null;
          ResultSet rs2 = null;
          try {
            stm2 = conn.createStatement();
            rs2 = stm2.executeQuery(query);
            if(rs2.next()){
              baseDeptId = rs2.getInt("DEPT_ID");
            }
          } catch(Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm2, rs2, null); 
          }
        }
        //autoDeptId = baseDeptId;
      } 
      if("2".equals(fp.getAutoType())){
        tmpDeptId = baseDeptId;
      } else if ("4".equals(fp.getAutoType()) || "6".equals(fp.getAutoType()) ) {
        YHPrcsRoleUtility util = new YHPrcsRoleUtility();
        tmpDeptId =  util.getDeptParent(baseDeptId, 1, conn);
      } else if ("5".equals(fp.getAutoType())) {
        YHPrcsRoleUtility util = new YHPrcsRoleUtility();
        tmpDeptId =  util.getDeptParent(baseDeptId, 0, conn);
      }
      YHDeptLogic deptLogic = new YHDeptLogic();
      YHDepartment dept = deptLogic.getDepartmentById(tmpDeptId , conn);
      String manager = "";
      if(dept != null && dept.getManager() != null){
        manager = dept.getManager();
      }
      if ("4".equals(fp.getAutoType()) || "6".equals(fp.getAutoType()) ) {
        String query = "SELECT LEADER1,LEADER2 FROM oa_department WHERE SEQ_ID='"+baseDeptId+"'";
        Statement stm2 = null;
        ResultSet rs2 = null;
        String leader1 = "";
        String leader2 = "";
        try {
          stm2 = conn.createStatement();
          rs2 = stm2.executeQuery(query);
          if(rs2.next()){
            leader1 = rs2.getString("LEADER1");
            leader2 = rs2.getString("LEADER2");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm2, rs2, null); 
        }
        if (!YHUtility.isNullorEmpty(leader1) && "4".equals(fp.getAutoType())) {
          manager = leader1;
        }
        if (!YHUtility.isNullorEmpty(leader2) && "6".equals(fp.getAutoType())) {
          manager = leader2;
        }
      } 
      
      if (!"".equals(manager) && manager != null) {
        //以后做的
        String query = YHWorkFlowUtility.createFindSql("SEQ_ID", manager);
        query = "SELECT " 
          + " SEQ_ID"
          + " ,DEPT_ID" 
          + " ,USER_PRIV"
          + " ,USER_NAME "
          + " ,USER_PRIV_OTHER"
          + " FROM PERSON  WHERE  "
          + query ;
         // + ") and USER_PRIV.SEQ_ID = p.USER_PRIV"
          //+  "  order by USER_PRIV.PRIV_NO , p.USER_NO DESC  ,p.SEQ_ID";
        Statement stm5 = null;
        ResultSet rs5 = null;
        try {
          stm5 = conn.createStatement();
          rs5 = stm5.executeQuery(query);
          while(rs5.next()){
            int userId = rs5.getInt("SEQ_ID");
            int deptId = rs5.getInt("DEPT_ID");
            String userPriv = rs5.getString("USER_PRIV");
            String userName = rs5.getString("USER_NAME");
            String userPrivOther = rs5.getString("USER_PRIV_OTHER");
         
            if("ALL_DEPT".equals(prcsDept) 
                || YHWorkFlowUtility.findId(prcsUser , String.valueOf(userId))
                || YHWorkFlowUtility.findId(prcsDept , String.valueOf(deptId))
                || YHWorkFlowUtility.findId(prcsPriv , userPriv)
                || YHWorkFlowUtility.privOther(prcsPriv , userPrivOther) == 1){
              prcsUserAuto += userId + ",";
              prcsUserName += userName + ",";
            }
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm5, rs5, null); 
        }
        //取第一个作为主办人
        if(!"".equals(prcsUserAuto)){
          prcsOpUser = prcsUserAuto.split(",")[0];
          prcsOpUserName = prcsUserName.split(",")[0];
        }
      } else {
        //注意 tmpDeptId
        String query = "SELECT " 
          + " p.SEQ_ID as userId"
          + ",p.USER_NAME" 
          + ",p.USER_PRIV_OTHER " 
          + ",u.SEQ_ID "
          + " from PERSON p,USER_PRIV u where"
          + " p.USER_PRIV = u.SEQ_ID " 
          + " and DEPT_ID = "+ tmpDeptId
          + " and p.SEQ_ID !=" + user.getSeqId()
          + " order by PRIV_NO,USER_NO,USER_NAME";
        
        Statement stm3 = null;
        ResultSet rs3 = null;
        try {
          stm3 = conn.createStatement();
          rs3 = stm3.executeQuery(query);
          String userPrivMax = "";
          
          while(rs3.next()){
            String userId = String.valueOf(rs3.getInt("userId"));
            String userName = rs3.getString("USER_NAME");
            String userPriv = rs3.getString("SEQ_ID");
            String userPrivOther = rs3.getString("USER_PRIV_OTHER");
            
            if("ALL_DEPT".equals(prcsDept) 
                || YHWorkFlowUtility.findId(prcsUser , userId)
                || YHWorkFlowUtility.findId(prcsDept , String.valueOf(user.getDeptId()))
                || YHWorkFlowUtility.findId(prcsPriv , userPriv)
                || YHWorkFlowUtility.privOther(prcsPriv , userPrivOther) == 1){
              if ("".equals(userPrivMax)) {
                prcsOpUser = userId;
                prcsOpUserName = userName;
                prcsUserAuto += userId + ",";
                prcsUserName += userName + ",";
                userPrivMax = userPriv;
              } else if (userPrivMax.equals(userPriv)) { 
                prcsUserAuto += userId + ",";
                prcsUserName += userName + ",";
              }
            }
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm3, rs3, null); 
        }
      }
      isAutoSelect = true;
      //按照表单选择
    }else if( fp.getAutoType() != null && "7".equals(fp.getAutoType())) {
      String formUserStr = "";
      if(YHWorkFlowUtility.isNumeric(fp.getAutoUser())){
        Map map = new HashMap();
        map.put("RUN_ID", flowRun.getRunId());
        map.put("ITEM_ID", Integer.parseInt(fp.getAutoUser()));
        YHDocFlowRunData frd = (YHDocFlowRunData) orm.loadObjSingle(conn, YHDocFlowRunData.class, map);
        String itemData = "";
        if(frd != null){
          itemData = frd.getItemData();
        }
        List<YHPerson> list = new ArrayList();
        if (itemData != null && !"".equals(itemData)) {
          itemData = itemData.replaceAll("，", ",");
          //按照名字查询
          String query = "SELECT SEQ_ID "
            +" ,DEPT_ID "
            +" ,USER_PRIV "
            +" ,USER_NAME "
            +" ,USER_PRIV_OTHER FROM PERSON WHERE "
            + YHWorkFlowUtility.createFindSql("USER_NAME", itemData);
          
          Statement stm = null;
          ResultSet rs = null;
          try {
            stm = conn.createStatement();
            rs = stm.executeQuery(query);
            while(rs.next()){
              YHPerson tmp = new YHPerson();
              tmp.setSeqId(rs.getInt("SEQ_ID"));
              tmp.setDeptId(rs.getInt("DEPT_ID"));
              tmp.setUserPriv(rs.getString("USER_PRIV"));
              tmp.setUserName(rs.getString("USER_NAME"));
              tmp.setUserPrivOther(rs.getString("USER_PRIV_OTHER"));
              list.add(tmp);
            }
          } catch(Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm, rs, null); 
          }
          
          //按USER_ID查询
          if(list.size() <= 0 ){
            String newId = "";
            String[] newIds = itemData.split(",");
            for (String id : newIds) {
              //容错处理,如果有不是数字的时候．．就会出异常
              if (YHUtility.isInteger(id)) {
                newId += id + ",";
              }
            }
            if (newId.endsWith(",")) {
              newId = newId.substring(0, newId.length() - 1);
            }
            if (!"".equals(newId)) {
              query = "SELECT SEQ_ID "
              +" ,DEPT_ID "
              +" ,USER_PRIV "
              +" ,USER_NAME "
              +" ,USER_PRIV_OTHER  "
              +" FROM Person WHERE "
                + " SEQ_ID in (" + newId + ")";
              Statement stm2 = null;
              ResultSet rs2 = null;
              try {
                stm2 = conn.createStatement();
                rs2 = stm2.executeQuery(query);
                while(rs2.next()){
                  YHPerson tmp = new YHPerson();
                  tmp.setSeqId(rs2.getInt("SEQ_ID"));
                  tmp.setDeptId(rs2.getInt("DEPT_ID"));
                  tmp.setUserPriv(rs2.getString("USER_PRIV"));
                  tmp.setUserName(rs2.getString("USER_NAME"));
                  tmp.setUserPrivOther(rs2.getString("USER_PRIV_OTHER"));
                  list.add(tmp);
                }
              } catch(Exception ex) {
                throw ex;
              } finally {
                YHDBUtility.close(stm2, rs2, null); 
              }
            }
          }
        }
        
        if(list.size() > 0){
          formUserStr = itemData;
          Collections.sort(list,new YHUserSortComparatorUtility(formUserStr));
        }
        prcsUserAuto = prcsUserName = "";
        for(YHPerson tmp : list){
          if("ALL_DEPT".equals(prcsDept) 
              || YHWorkFlowUtility.findId(prcsUser , String.valueOf(tmp.getSeqId()))
              || YHWorkFlowUtility.findId(prcsDept , String.valueOf(tmp.getDeptId()))
              || YHWorkFlowUtility.findId(prcsPriv , tmp.getUserPriv())
              || YHWorkFlowUtility.privOther(prcsPriv , tmp.getUserPrivOther()) == 1){
            prcsUserAuto += tmp.getSeqId() + ",";
            prcsUserName += tmp.getUserName() + ",";
          }
        }
        
        if(prcsUserAuto != null && !"".equals(prcsUserAuto)){
          //取第一个默认为主办人
          prcsOpUser = prcsUserAuto.split(",")[0];
          prcsOpUserName = prcsUserName.split(",")[0];
        }
      }
      isAutoSelect = true;
    //不是自动选 择,
    } else if( "20".equals(fp.getAutoType()) ) {
    
    int baseDeptId = user.getDeptId();
    int tmpDeptId = baseDeptId;
    
    String manager = getUserByDeptRole(tmpDeptId , fp.getAutoSelectRole() , conn);
    if (!"".equals(manager) && manager != null) {
      //以后做的
      String query = YHWorkFlowUtility.createFindSql("SEQ_ID", manager);
      query = "SELECT " 
        + " SEQ_ID"
        + " ,DEPT_ID" 
        + " ,USER_PRIV"
        + " ,USER_NAME "
        + " ,USER_PRIV_OTHER"
        + " FROM PERSON  WHERE  "
        + query ;
       // + ") and USER_PRIV.SEQ_ID = p.USER_PRIV"
        //+  "  order by USER_PRIV.PRIV_NO , p.USER_NO DESC  ,p.SEQ_ID";
      Statement stm5 = null;
      ResultSet rs5 = null;
      try {
        stm5 = conn.createStatement();
        rs5 = stm5.executeQuery(query);
        while(rs5.next()){
          int userId = rs5.getInt("SEQ_ID");
          int deptId = rs5.getInt("DEPT_ID");
          String userPriv = rs5.getString("USER_PRIV");
          String userName = rs5.getString("USER_NAME");
          String userPrivOther = rs5.getString("USER_PRIV_OTHER");
       
          if("ALL_DEPT".equals(prcsDept) 
              || YHWorkFlowUtility.findId(prcsUser , String.valueOf(userId))
              || YHWorkFlowUtility.findId(prcsDept , String.valueOf(deptId))
              || YHWorkFlowUtility.findId(prcsPriv , userPriv)
              || YHWorkFlowUtility.privOther(prcsPriv , userPrivOther) == 1){
            prcsUserAuto += userId + ",";
            prcsUserName += userName + ",";
          }
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm5, rs5, null); 
      }
      //取第一个作为主办人
      if(!"".equals(prcsUserAuto)){
        prcsOpUser = prcsUserAuto.split(",")[0];
        prcsOpUserName = prcsUserName.split(",")[0];
      }
    } else {
      //注意 tmpDeptId
      String query = "SELECT " 
        + " p.SEQ_ID as userId"
        + ",p.USER_NAME" 
        + ",p.USER_PRIV_OTHER " 
        + ",u.SEQ_ID "
        + " from PERSON p,USER_PRIV u where"
        + " p.USER_PRIV = u.SEQ_ID " 
        + " and DEPT_ID = "+ tmpDeptId
        + " and p.SEQ_ID !=" + user.getSeqId()
        + " order by PRIV_NO,USER_NO,USER_NAME";
      
      Statement stm3 = null;
      ResultSet rs3 = null;
      try {
        stm3 = conn.createStatement();
        rs3 = stm3.executeQuery(query);
        String userPrivMax = "";
        
        while(rs3.next()){
          String userId = String.valueOf(rs3.getInt("userId"));
          String userName = rs3.getString("USER_NAME");
          String userPriv = rs3.getString("SEQ_ID");
          String userPrivOther = rs3.getString("USER_PRIV_OTHER");
          
          if("ALL_DEPT".equals(prcsDept) 
              || YHWorkFlowUtility.findId(prcsUser , userId)
              || YHWorkFlowUtility.findId(prcsDept , String.valueOf(user.getDeptId()))
              || YHWorkFlowUtility.findId(prcsPriv , userPriv)
              || YHWorkFlowUtility.privOther(prcsPriv , userPrivOther) == 1){
            if ("".equals(userPrivMax)) {
              prcsOpUser = userId;
              prcsOpUserName = userName;
              prcsUserAuto += userId + ",";
              prcsUserName += userName + ",";
              userPrivMax = userPriv;
            } else if (userPrivMax.equals(userPriv)) { 
              prcsUserAuto += userId + ",";
              prcsUserName += userName + ",";
            }
          }
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm3, rs3, null); 
      }
    }
    isAutoSelect = true;
    //按照表单选择
  }else if(!"".equals(prcsUser) && "".equals(prcsPriv)){
      String[] aPrcsUser = prcsUser.split(",");
      if(aPrcsUser.length == 1){
        prcsUserAuto = prcsUser;
        if(prcsUserAuto.endsWith(",")){
          prcsOpUser = prcsUserAuto.substring(0, prcsUserAuto.length() -1);
        }else{
          prcsOpUser = prcsUserAuto;
        }
        YHPerson tmp = (YHPerson) orm.loadObjSingle(conn, YHPerson.class,  Integer.parseInt(aPrcsUser[0]));
        if(tmp != null){
          prcsUserName = tmp.getUserName() + ",";
          prcsOpUserName = tmp.getUserName();
        }
      }
    }
    sb.append ( "isAutoSelect:"+ isAutoSelect +",prcsOpUser:'" + prcsOpUser
        + "',prcsOpUserName:'" + prcsOpUserName 
        + "',prcsUser:'" + prcsUserAuto + "',prcsUserName:'" + prcsUserName + "'" ) ;
    return sb.toString();
  }
  public String checkCondition(Map formData  , String condition , String conditionSet  ) {
    //是否验证转入条件
    String conditionSetValue = "";
    String conditionSetDesc = "";
    String notPass = "";
    if (conditionSetValue == null) {
      conditionSetValue = "";
    }
    if (condition == null) {
      condition = "";
    }
    conditionSet = conditionSetValue;
    conditionSetDesc = conditionSetValue;
   //条件为空直接退出

    if ("".equals(condition) || condition == null ) {
      return "setOk";
    }
    //准备条件运算结果表达式，以及该表达式的文字描述

    //在表达式的文字描述中，将逻辑运算符翻译成文字
    conditionSetDesc = conditionSetDesc.replaceAll("OR" , "或者");
    conditionSetDesc = conditionSetDesc.replaceAll("AND" , "并且");
    conditionSetDesc = conditionSetDesc.replaceAll("!" , "非");
    //把条件串解析开，放到数组里
    String [] conArray = condition.split(",");
    //送代对应条件列表中每个条件

    int count = 0 ;
    for (int i = 0 ;i < conArray.length ;i ++) {
      String rule = conArray[i];
      rule = rule.trim();
      //.replaceAll(" ", "");//去掉条件中的空格
      //如果该条件为空，则结束本次循环，处理下一个条件
      if ("".equals(rule)) {
        continue;
      }
    //解析条件，把条件中的左侧项目、条件运算符、右侧项目分别放到三个变量里
      String[] ruleArray = rule.split("'");
      String itemTitle = "";
      String itemCon = "";
      String itemValue = "";
      if (ruleArray.length == 2) {
        itemTitle = ruleArray[1];
      } else if ( ruleArray.length == 3 ) {
        itemTitle = ruleArray[1];
        itemCon = ruleArray[2];
      } else if ( ruleArray.length == 4 ) {
        itemTitle = ruleArray[1];
        itemCon = ruleArray[2];
        itemValue = ruleArray[3];
      } 
      /**
       * 以下字段在表单数据$FORM_DATA中没有，需要专门处理，获取这些字段的值并将其赋值到$FORM_DATA中

       * 
       */
      //条件判断，如果符合条件，则checkPass置成true 
      boolean checkPass = false;
      String itemConDesc = "";
      String value = (String) formData.get(itemTitle);
      if (value == null) {
        value = "";
      }
      if (value != null && itemCon.indexOf("include") != - 1) {
        itemConDesc =  "包含";
        if (value.indexOf(itemValue) != -1) {
          checkPass = true;
        }
      } else if (itemCon.indexOf("exclude") != - 1) {
        itemConDesc =  "不包含";
        if (value.indexOf(itemValue) == -1) {
          checkPass = true;
        }
        //类型判断条件
      } else if (value != null && itemCon.indexOf("==") != -1) {
        boolean flag = true;
        if (itemCon.indexOf("!==") != -1) {
          flag = false;
        }
        itemConDesc = flag ? "类型为" : "类型 不能为";
        if ("数值".equals(itemValue) 
            && flag == YHUtility.isInteger(value)) {
          checkPass = true;//2010-12-08
        } else if ("日期".equals(itemValue)
            && flag == YHUtility.isDay(value)) {
          checkPass = true;
        } else if ("日期+时间".equals(itemValue)
            && flag == YHUtility.isDayTime(value)) {
          checkPass = true;
        } 
      } else if (value != null 
          && itemCon.indexOf(">=") != -1) {
        Double iValue = 0.0;
        itemConDesc = "大于等于";
        if (YHUtility.isNumber(itemValue)) {
          if (YHUtility.isNumber(value)) {
            iValue = Double.parseDouble(value);
          }
          Double value1 = Double.parseDouble(itemValue);
          if (iValue >= value1) {
            checkPass = true;
          }
        } 
      } else if (value != null && itemCon.indexOf("<=") != -1 ) {
        itemConDesc = "小于等于";
        Double iValue = 0.0;
        if (YHUtility.isInteger(itemValue)) {
          if (YHUtility.isNumber(value)) {
            iValue = Double.parseDouble(value);
          }
          Double value1 = Double.parseDouble(itemValue);
          if (iValue <= value1) {
            checkPass = true;
          }
        }
      } else if (value != null && itemCon.indexOf("<>") != -1) {
        itemConDesc = "不等于";
        if (!value.equals(itemValue)) {
          checkPass = true;
        }
      } else if (value != null 
          && itemCon.indexOf(">") != -1) {
        itemConDesc = "大于";
        Double iValue = 0.0;
        if (YHUtility.isNumber(itemValue)) {
          if (YHUtility.isNumber(value)) {
            iValue =  Double.parseDouble(value);
          }
          Double value1 = Double.parseDouble(itemValue);
          if (iValue > value1) {
            checkPass = true;
          }
        }
      } else if (value != null 
          && itemCon.indexOf("<") != -1) {
        itemConDesc = "小于";
        Double iValue = 0.0;
        if (YHUtility.isNumber(itemValue)) {
          if (YHUtility.isNumber(value)) {
            iValue = Double.parseDouble(value);
          }
          Double value1 = Double.parseDouble(itemValue);
          if (iValue < value1) {
            checkPass = true;
          }
        }
      } else if ("=".equals(itemCon)) {
        itemConDesc = "等于";
        if (value == null) {
          value = "";
        }
        if (value.equals(itemValue)) {
          checkPass = true;
        }
      } else {
        itemCon = "";
      }
      //生成条件（rule）的描述setDesc
      if ("".equals(itemValue)) {
        itemValue = "空";
      }
      String setDesc = itemTitle + itemConDesc + itemValue;
      //检查两种逻辑表达式的错误，报错“表达式错误……”

      
      if (!checkPass) {
        if ("".equals(itemCon) || "".equals(itemTitle)) {
          notPass = "条件表达式错误：<br>" + rule + "<br>";
          return notPass;
        } else {
          if ("".equals(conditionSet)) {
            notPass = "不符合条件：<br>" + setDesc + "</br>";
            return notPass;
          }
        }
      } 
      //生成条件（$RULE）的运算结果值$SET_VALUE（0-假，1-真），这个值将代入条件公式
     
      //形成条件公式值运算和条件公式描述
      count = i + 1;
      conditionSetValue = conditionSetValue.replaceAll("\\["+ count +"\\]", String.valueOf(checkPass));
      conditionSetDesc = conditionSetDesc.replaceAll("\\["+ count +"\\]", setDesc);
    }
    if (!"".equals(conditionSet)
        && conditionSet != null) {
      //条件公式值运算判断

      conditionSetValue = conditionSetValue.replaceAll("AND", "&&");
      conditionSetValue = conditionSetValue.replaceAll("OR", "||");
      Boolean result = false;
      try {
        Expression e = ExpressionFactory.createExpression(conditionSetValue);    
        JexlContext jc = JexlHelper.createContext();    
        result = (Boolean) e.evaluate(jc); 
      } catch (Exception ex) {
        return notPass = "条件公式错误：<br>" + conditionSetValue;
      }
      if (result == null || !result) {
        notPass = "条件公式条件不满足";
      } else {
        notPass = "setOk" ; 
      }
    } else {
      notPass = "setOk" ; 
    }
    return notPass;
  }
  public String checkCondition(YHPerson user ,Map formData , YHDocFlowProcess fp  , boolean isIn , int runId , int prcsId , int flowPrcs, Connection conn) throws Exception {
    //是否验证转入条件
    String condition = "";
    String conditionSetValue = "";
    String conditionSetDesc = "";
    String conditionSet = "";
    String notPass = "";
    String conditionDesc = YHUtility.null2Empty(fp.getConditionDesc());
    if (isIn) {
      condition = fp.getPrcsIn();
      conditionSetValue = fp.getPrcsInSet();
    } else {
      condition = fp.getPrcsOut();
      conditionSetValue = fp.getPrcsOutSet();
    }
    if (conditionSetValue == null) {
      conditionSetValue = "";
    }
    if (condition == null) {
      condition = "";
    }
    conditionSet = conditionSetValue;
    conditionSetDesc = conditionSetValue;
   //条件为空直接退出
    if ("".equals(condition) || condition == null ) {
      return "setOk";
    }
    //准备条件运算结果表达式，以及该表达式的文字描述
    //在表达式的文字描述中，将逻辑运算符翻译成文字
    conditionSetDesc = conditionSetDesc.replaceAll("OR" , "或者");
    conditionSetDesc = conditionSetDesc.replaceAll("AND" , "并且");
    conditionSetDesc = conditionSetDesc.replaceAll("!" , "非");
    //把条件串解析开，放到数组里
    String [] conArray = condition.split(",");
    //送代对应条件列表中每个条件
    int count = 0 ;
    for (int i = 0 ;i < conArray.length ;i ++) {
      String rule = conArray[i];
      rule = rule.trim();
      //.replaceAll(" ", "");//去掉条件中的空格
      //如果该条件为空，则结束本次循环，处理下一个条件      if ("".equals(rule)) {
        continue;
      }
    //解析条件，把条件中的左侧项目、条件运算符、右侧项目分别放到三个变量里
      String[] ruleArray = rule.split("'");
      String itemTitle = "";
      String itemCon = "";
      String itemValue = "";
      if (ruleArray.length == 2) {
        itemTitle = ruleArray[1];
      } else if ( ruleArray.length == 3 ) {
        itemTitle = ruleArray[1];
        itemCon = ruleArray[2];
      } else if ( ruleArray.length == 4 ) {
        itemTitle = ruleArray[1];
        itemCon = ruleArray[2];
        itemValue = ruleArray[3];
      } 
      /**
       * 以下字段在表单数据$FORM_DATA中没有，需要专门处理，获取这些字段的值并将其赋值到$FORM_DATA中
       * 
       */
      if (itemTitle.indexOf("[主办人会签意见]") != -1 
          || itemTitle.indexOf("[从办人会签意见]") != -1) {
        String query  = "SELECT USER_ID " 
          + " from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" where  " 
          + " RUN_ID=" + runId
          + " and PRCS_ID=" + prcsId;
        if(itemTitle.indexOf("[主办人会签意见]") != -1) {
          query += " and OP_FLAG=1";
        } else {
          query += " and OP_FLAG=0";
        }
        String feedContent = "";
        int prcsUser = 0;
        
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query);
          while (rs.next()) {
            prcsUser = rs.getInt("USER_ID");
            String query1 = "select CONTENT from "+ YHWorkFlowConst.FLOW_RUN_FEEDBACK +" where "
              + " RUN_ID = " + runId
              + " and PRCS_ID=" + prcsId
              + " and USER_ID=" + prcsUser; 
            Statement stm2 = null;
            ResultSet rs2 = null;
            try {
              stm2 = conn.createStatement();
              rs2 = stm2.executeQuery(query1);
              while (rs2.next()) {
                feedContent += rs2.getString("CONTENT");
              }
            } catch(Exception ex) {
              throw ex;
            } finally {
              YHDBUtility.close(stm2, rs2, null); 
            }
          }
          if (prcsUser != 0) {
            formData.put(itemTitle , feedContent);
          } else {
            formData.put(itemTitle , "无-从-办-人");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
      } else if (itemTitle.indexOf("[公共附件名称]") != -1) {
        String query = "select ATTACHMENT_NAME "
          + " from "+ YHWorkFlowConst.FLOW_RUN +" where  "
          + " RUN_ID=" + runId;
        String attachmentName = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query);
          if (rs.next() && rs.getString("ATTACHMENT_NAME") != null) {
            attachmentName = rs.getString("ATTACHMENT_NAME").trim();
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
        formData.put(itemTitle, attachmentName);
      } else if (itemTitle.indexOf("[公共附件个数]") != -1) {
        String query = "select "
          + " ATTACHMENT_ID"
          + " from "+ YHWorkFlowConst.FLOW_RUN +" where "
          + " RUN_ID=" + runId;
        
        String attachmentId = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(query);
          if (rs.next() && rs.getString("ATTACHMENT_ID") != null ) {
            attachmentId = rs.getString("ATTACHMENT_ID").trim();
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
        int c = 0 ;
        if (!"".equals(attachmentId)) {
          if (attachmentId.endsWith(",")) {
            attachmentId.substring(0, attachmentId.length() - 1);
          }
          c = attachmentId.split(",").length ;
        }
        formData.put(itemTitle, String.valueOf(c));
      } else if (itemTitle.indexOf("[当前步骤号]") != -1) {
        formData.put(itemTitle, String.valueOf(prcsId));
      } else if (itemTitle.indexOf("[当前流程设计步骤号]") != -1) {
        formData.put(itemTitle,  String.valueOf(flowPrcs));
      } else if (itemTitle.indexOf("[当前主办人姓名]") != -1) {
        formData.put(itemTitle, user.getUserName());
      } else if (itemTitle.indexOf("[当前主办人角色]") != -1) {
        String queryPriv = "select PRIV_NAME " 
          + " from USER_PRIV where "
          + " SEQ_ID = " + user.getUserPriv();
        String privName = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(queryPriv);
          if (rs.next()) {
            privName = rs.getString("PRIV_NAME");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
        formData.put(itemTitle, privName);
      } else if (itemTitle.indexOf("[当前主办人角色号]") != -1) {
        String queryPriv = "select PRIV_NO " 
          + " from USER_PRIV where "
          + " SEQ_ID = " + user.getUserPriv();
        String privName = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(queryPriv);
          if (rs.next()) {
            privName = rs.getString("PRIV_NO");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
        formData.put(itemTitle, privName);
      } else if (itemTitle.indexOf("[当前主办人部门]") != -1) {
        String queryDept = "select DEPT_NAME " 
          + " from oa_department where "
          + " SEQ_ID = " + user.getDeptId();
        String deptName = "";
        Statement stm = null;
        ResultSet rs = null;
        try {
          stm = conn.createStatement();
          rs = stm.executeQuery(queryDept);
          if (rs.next()) {
            deptName = rs.getString("DEPT_NAME");
            if (deptName == null) {
              deptName = "";
            }
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
        formData.put(itemTitle, deptName);
      } else if (itemTitle.indexOf("[当前主办人上级部门]") != -1) {
        String deptName = "";
        YHDeptLogic logic = new YHDeptLogic();
        YHDepartment dp = logic.getDepartmentById(user.getDeptId(), conn);
        if (dp.getDeptParent() == 0) {
          deptName = dp.getDeptName();
        } else {
          deptName = logic.getNameById(dp.getDeptParent(), conn);
        }
        if (deptName == null) {
          deptName = "";
        }
        formData.put(itemTitle, deptName);
      } else if (itemTitle.indexOf("[当前主办人辅助角色]") != -1) {
        String otherPriv = user.getUserPrivOther();
        if (otherPriv != null && !"".equals(otherPriv.trim())) {
          String conSql = YHWorkFlowUtility.createFindSql("SEQ_ID", otherPriv);
          String query = "SELECT PRIV_NAME from USER_PRIV WHERE " + conSql;
          String privNames = "";
          Statement stm = null;
          ResultSet rs = null;
          try {
            stm = conn.createStatement();
            rs = stm.executeQuery(query);
            while (rs.next()) {
              String priv = rs.getString("PRIV_NAME");
              if (priv != null && !"".equals(priv)) {
                privNames += priv + ",";
              }
            }
          } catch(Exception ex) {
            throw ex;
          } finally {
            YHDBUtility.close(stm, rs, null); 
          }
          formData.put(itemTitle, privNames);
        } else {
          formData.put(itemTitle, "");
        }
      }
      //条件判断，如果符合条件，则checkPass置成true 
      boolean checkPass = false;
      String itemConDesc = "";
      String value = (String) formData.get(itemTitle);
      if (value == null) {
        value = "";
      }
      if (value != null && itemCon.indexOf("include") != - 1) {
        itemConDesc =  "包含";
        if (value.indexOf(itemValue) != -1) {
          checkPass = true;
        }
      } else if (itemCon.indexOf("exclude") != - 1) {
        itemConDesc =  "不包含";
        if (value.indexOf(itemValue) == -1) {
          checkPass = true;
        }
        //类型判断条件
      } else if (value != null && itemCon.indexOf("==") != -1) {
        boolean flag = true;
        if (itemCon.indexOf("!==") != -1) {
          flag = false;
        }
        itemConDesc = flag ? "类型为" : "类型 不能为";
        if ("数值".equals(itemValue) 
            && flag == YHUtility.isInteger(value)) {
          checkPass = true;//2010-12-08
        } else if ("日期".equals(itemValue)
            && flag == YHUtility.isDay(value)) {
          checkPass = true;
        } else if ("日期+时间".equals(itemValue)
            && flag == YHUtility.isDayTime(value)) {
          checkPass = true;
        } 
      } else if (value != null 
          && itemCon.indexOf(">=") != -1) {
        Double iValue = 0.0;
        itemConDesc = "大于等于";
        if (YHUtility.isNumber(itemValue)) {
          if (YHUtility.isNumber(value)) {
            iValue = Double.parseDouble(value);
          }
          Double value1 = Double.parseDouble(itemValue);
          if (iValue >= value1) {
            checkPass = true;
          }
        } 
      } else if (value != null && itemCon.indexOf("<=") != -1 ) {
        itemConDesc = "小于等于";
        Double iValue = 0.0;
        if (YHUtility.isInteger(itemValue)) {
          if (YHUtility.isNumber(value)) {
            iValue = Double.parseDouble(value);
          }
          Double value1 = Double.parseDouble(itemValue);
          if (iValue <= value1) {
            checkPass = true;
          }
        }
      } else if (value != null && itemCon.indexOf("<>") != -1) {
        itemConDesc = "不等于";
        if (!value.equals(itemValue)) {
          checkPass = true;
        }
      } else if (value != null 
          && itemCon.indexOf(">") != -1) {
        itemConDesc = "大于";
        Double iValue = 0.0;
        if (YHUtility.isNumber(itemValue)) {
          if (YHUtility.isNumber(value)) {
            iValue =  Double.parseDouble(value);
          }
          Double value1 = Double.parseDouble(itemValue);
          if (iValue > value1) {
            checkPass = true;
          }
        }
      } else if (value != null 
          && itemCon.indexOf("<") != -1) {
        itemConDesc = "小于";
        Double iValue = 0.0;
        if (YHUtility.isNumber(itemValue)) {
          if (YHUtility.isNumber(value)) {
            iValue = Double.parseDouble(value);
          }
          Double value1 = Double.parseDouble(itemValue);
          if (iValue < value1) {
            checkPass = true;
          }
        }
      } else if ("=".equals(itemCon)) {
        itemConDesc = "等于";
        if (value == null) {
          value = "";
        }
        if (value.equals(itemValue)) {
          checkPass = true;
        }
      } else {
        itemCon = "";
      }
      //生成条件（rule）的描述setDesc
      if ("".equals(itemValue)) {
        itemValue = "空";
      }
      String setDesc = itemTitle + itemConDesc + itemValue;
      //检查两种逻辑表达式的错误，报错“表达式错误……”
      
      if (!checkPass) {
        if ("".equals(itemCon) || "".equals(itemTitle)) {
          notPass = "条件表达式错误：<br>" + rule + "<br>";
          return notPass;
        } else {
          if ("".equals(conditionSet)) {
            notPass = "不符合条件：<br>" + setDesc + "</br>";
            return notPass;
          }
        }
      } 
      //生成条件（$RULE）的运算结果值$SET_VALUE（0-假，1-真），这个值将代入条件公式
     
      //形成条件公式值运算和条件公式描述
      count = i + 1;
      conditionSetValue = conditionSetValue.replaceAll("\\["+ count +"\\]", String.valueOf(checkPass));
      conditionSetDesc = conditionSetDesc.replaceAll("\\["+ count +"\\]", setDesc);
    }
    if (!"".equals(conditionSet)
        && conditionSet != null) {
      //条件公式值运算判断
      conditionSetValue = conditionSetValue.replaceAll("AND", "&&");
      conditionSetValue = conditionSetValue.replaceAll("OR", "||");
      Boolean result = false;
      try {
        Expression e = ExpressionFactory.createExpression(conditionSetValue);    
        JexlContext jc = JexlHelper.createContext();    
        result = (Boolean) e.evaluate(jc); 
      } catch (Exception ex) {
        return notPass = "条件公式错误：<br>" + conditionSetValue;
      }
      if (result == null || !result) {
        conditionDesc = conditionDesc.replaceAll("[\n-\r]", "");
        if ("".equals(conditionDesc) 
          || conditionDesc == null) {
          notPass = "不符合条件公式：<br>" + conditionSetDesc + "<br>";
        } else {
          notPass = conditionDesc;
        }
      } else {
        notPass = "setOk" ; 
      }
    } else {
      notPass = "setOk" ; 
    }
    return notPass;
  }
  public Map getForm(int formId , int runId , Connection conn) throws Exception {
    Map map = new HashMap();
    map.put("FORM_ID", formId);
    YHORM orm = new YHORM();
    List<YHDocFlowFormItem> items = orm.loadListSingle(conn, YHDocFlowFormItem.class, map);
    //----- 取表单数据 --------
    Map formData = new HashMap();
    String  query1 = "select ITEM_ID" 
      + " ,ITEM_DATA " 
      + " from "+ YHWorkFlowConst.FLOW_RUN_DATA +" where "
      + " RUN_ID = " + runId + " order by ITEM_ID";
    Map tmpMap = new HashMap();
    Statement stm2 = null;
    ResultSet rs2 = null;
    try {
      stm2 = conn.createStatement();
      rs2 = stm2.executeQuery(query1);
      while (rs2.next()) {
        int itemId = rs2.getInt("ITEM_ID");
        String itemData = rs2.getString("ITEM_DATA");
        tmpMap.put(itemId, itemData);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm2, rs2, null); 
    }
    
    String  query = "select PRINT_MODEL_SHORT" 
      + " from "+ YHWorkFlowConst.FLOW_FORM_TYPE +" where "
      + " SEQ_ID = " + formId;
    String printModel = "";
    Statement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if(rs.next()){
        printModel = rs.getString("PRINT_MODEL_SHORT");
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    
    for (YHDocFlowFormItem tmp : items) {
      String tag = tmp.getTag();
      String element = tmp.getContent();
      String value = tmp.getValue();
      String title = tmp.getTitle();
      String clazz = tmp.getClazz();
      int itemId = tmp.getItemId();
      
      if (!"DATE".equals(clazz) 
          && !"USER".equals(clazz)) {
        String itemData = (String) tmpMap.get(itemId);
        formData.put(title, itemData);
        String str = "NAAAALOaxuKqfjdcOLp66SG1uokBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==";
        if (str.equals(itemData)) {
          formData.put(title, "");
        }
      }
    }
    return formData;
  }
  /**
   * 
   * @param prcsUser
   * @param flowId
   * @param runId
   * @param prcsId
   * @param flowPrcs
   * @param opUser
   * @param otherMap
   * @param conn
   * @return
   * @throws Exception
   */
  public String turnOther(String prcsUser , int flowId , int runId , int prcsId , String flowPrcs , String opUser , Map otherMap , Connection conn) throws Exception {
    Date curDate = new Date();
    String[] uArray = prcsUser.split(",");
    for (int i = 0 ; i< uArray.length ; i ++) {
      String tmp = uArray[i];
      if (tmp != null && !"".equals(tmp)) {
        String query = "select * from "+ YHWorkFlowConst.FLOW_RULE +" where " 
          + " FLOW_ID=" + flowId 
          + " and USER_ID="+ tmp 
          +" and STATUS = 1";
        Statement stmt = null;
        ResultSet rs = null;
        try {
          stmt = conn.createStatement();
          rs = stmt.executeQuery(query);
          while (rs.next()) {
            java.sql.Date bgDate = rs.getDate("BEGIN_DATE");
            java.sql.Date endDate = rs.getDate("END_DATE");
            int toId = rs.getInt("TO_ID");
            
            int status = 0 ;
            if (bgDate != null && endDate != null) {
              int con1 =curDate.compareTo(bgDate);
              int con2 =  curDate.compareTo(endDate);
              if (con1 >= 0 && con2 <=0) {
                status = 1;
              }
            } else if (bgDate != null) {
              int con1 =curDate.compareTo(bgDate);
              if (con1 >= 0) {
                status = 1;
              }
            } else if (endDate != null) {
              int con2 =  curDate.compareTo(endDate);
              if (con2 <= 0) {
                status = 1;
              }
            } else {
              status = 1;
            }
            otherMap.put(String.valueOf(toId) , tmp);
            if (status == 1) {
              if (!opUser.equals(tmp)) {
                String userName = this.getUserName(toId ,  conn);
                String content = "根据自动委托规则把工作委托给[" + userName + "]";
                YHFlowRunLogLogic logic = new YHFlowRunLogLogic();
                int iFlowPrcs = 0 ;
                if (YHUtility.isInteger(flowPrcs)) {
                  iFlowPrcs = Integer.parseInt(flowPrcs);
                }
                logic.runLog(runId, prcsId + 1, iFlowPrcs, Integer.parseInt(tmp), 2, content, "", conn);
              }
              uArray[i] =String.valueOf(toId);
            }
          }
        } catch (Exception ex) {
           throw ex;
        } finally {
          YHDBUtility.close(stmt, rs, null);
        }
      }
    }
    String outputStr = "";
    for (int i = 0 ; i< uArray.length ; i ++) {
      String tmp = uArray[i];
      if (tmp != null && !"".equals(tmp)) {
        if (i == uArray.length - 1) {
          outputStr += tmp;
        } else {
          outputStr += tmp + ",";
        }
      }
    }
    return outputStr;
  }
  public String getUserName(int userId , Connection conn) throws Exception {
    String query = "select USER_NAME from PERSON where SEQ_ID=" + userId;
    String userName = "";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      if (rs.next()) {
        userName = rs.getString("USER_NAME");
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt , rs , null);
    }
    return userName;
  }
  public String getUserByDeptRole(int deptId , String roleId , Connection conn) throws Exception {
    String query = "select PERSON.SEQ_ID from PERSON , USER_PRIV where USER_PRIV.SEQ_ID = PERSON.USER_PRIV AND (DEPT_ID ='" + deptId + "' OR " + YHDBUtility.findInSet(String.valueOf(deptId), "DEPT_ID_OTHER") + ")"
    + " and (USER_PRIV = '" + roleId + "' OR " + YHDBUtility.findInSet(roleId, "USER_PRIV_OTHER") + ")  order by USER_PRIV.PRIV_NO , PERSON.USER_NO DESC  ,PERSON.SEQ_ID";
    String manager = "";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.createStatement();
      rs = stmt.executeQuery(query);
      while (rs.next()) {
        manager += rs.getString("SEQ_ID") + ",";
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt , rs , null);
    }
    manager = YHWorkFlowUtility.getOutOfTail(manager);
    return manager;
  }
  public static void main(String[] args ) {
    Double result = 0d;
    try {
      Expression e = ExpressionFactory.createExpression("0.0+10-9");    
      JexlContext jc = JexlHelper.createContext();    
      result = (Double) e.evaluate(jc); 
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    YHOut.print(result);
  }
}

