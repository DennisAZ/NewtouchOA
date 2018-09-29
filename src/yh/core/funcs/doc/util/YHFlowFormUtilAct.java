package yh.core.funcs.doc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.data.YHDocFlowFormType;
import yh.core.funcs.doc.util.YHFlowFormLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHFlowFormUtilAct {
  private static Logger log = Logger.getLogger(YHFlowFormUtilAct.class);
  public String getPerson(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      ArrayList<YHPerson> perList = null;
      List<Map> list = new ArrayList();
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      List listLocal = ffl.getPerson(dbConn);
      for(int i = 0; i<listLocal.size(); i++){
        listLocal.get(i);
        String[] filterPer = new String[]{"SEQ_ID="+listLocal.get(i)};
        List funcList = new ArrayList();
        funcList.add("person");
        map = (HashMap)orm.loadDataSingle(dbConn, funcList, filterPer);
        list.addAll((List<Map>) map.get("PERSON"));
      }
      if(list.size() > 1){
        for(Map m : list){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map m : list){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append("}");
        }
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDepartment(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      List<Map> list = new ArrayList();
      String[] filters = new String[]{"1 = 1 order by SEQ_ID ASC"};
      List funcList = new ArrayList();
      funcList.add("department");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_DEPARTMENT"));
      if(list.size() > 1){
        for(Map m : list) {
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",deptName:\"" + m.get("deptName") + "\"");
          sb.append("},");
        } 
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map m : list) {
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",deptName:\"" + m.get("deptName") + "\"");
          sb.append("}");
        } 
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getUpDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      List<Map> list = new ArrayList();
      String[] filters = new String[]{"1 = 1 order by SEQ_ID ASC"};
      List funcList = new ArrayList();
      funcList.add("person");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("PERSON"));
      for(Map m : list) {
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",userName:\"" + m.get("userName") + "\"");
        sb.append("},");
      }       
      sb.deleteCharAt(sb.length() - 1);       
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDeptParent(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LONGIN_USER");
      int seqId = 175; //int seqId = person.getSeqId();
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      String[] filter = null;
      String[] filterPart = null;
      String deptStc = "";
      String func = "";
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"SEQ_ID="+seqId};
      List funcList = new ArrayList();
      funcList.add("person");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("PERSON"));
      for(Map m : list){
        m.get("deptId");
        filter = new String[]{"SEQ_ID="+m.get("deptId")};
      }  
      List deptList = new ArrayList();
      deptList.add("department");
      map = (HashMap)orm.loadDataSingle(dbConn, deptList, filter);
      List<Map> listD = (List<Map>) map.get("OA_DEPARTMENT");
      YHFlowFormLogic dtt = new YHFlowFormLogic();
      List<Map> listPerson = new ArrayList();
      for(Map m : listD){
        m.get("deptParent");  //6
        m.get("seqId");    //7
        m.get("manager");
        if(m.get("deptParent").equals(0)){
          if(m.get("manager") == null){
            List listbbb = dtt.deptLocal(dbConn, m.get("seqId"),seqId);
            for(int i = 0; i<listbbb.size(); i++){
              listbbb.get(i);
              String[] filterPer = new String[]{"SEQ_ID="+listbbb.get(i)};
              List funcLists = new ArrayList();
              funcLists.add("person");
              map = (HashMap)orm.loadDataSingle(dbConn, funcLists, filterPer);
              listPerson.addAll((List<Map>) map.get("PERSON"));
            }
          }else{
            deptStc = dtt.deleteDeptd(dbConn, m.get("seqId"));
            String[]str = deptStc.split(",");
            for(int i = 0; i < str.length; i++){
              func = str[i];
              String[] filterPer = new String[]{"SEQ_ID="+func};
              List funcLists = new ArrayList();
              funcLists.add("person");
              map = (HashMap)orm.loadDataSingle(dbConn, funcLists, filterPer);
              listPerson.addAll((List<Map>) map.get("PERSON"));
            }
          }
        }else{
          filterPart = new String[]{"SEQ_ID=" + m.get("deptParent")};
          List listPart = new ArrayList();
          listPart.add("department");
          map = (HashMap)orm.loadDataSingle(dbConn, listPart, filterPart);
          List<Map> listDeptParent = (List<Map>) map.get("OA_DEPARTMENT");
          for(Map md : listDeptParent){
            md.get("deptParent");  //14
            md.get("manager"); //null
            md.get("seqId");   // 116
            if(md.get("manager") == null){
              String[] filterParent = new String[]{"DEPT_ID=" + md.get("seqId")};
              List funcListParent = new ArrayList();
              funcListParent.add("person");
              map = (HashMap)orm.loadDataSingle(dbConn, funcListParent, filterParent);
              listPerson.addAll((List<Map>) map.get("PERSON"));
            }else{
              String deptParentStr = String.valueOf(md.get("manager"));
              String[] str = deptParentStr.split(",");
              for(int i = 0; i < str.length; i++){
                func = str[i];
                String[] filterPer = new String[]{"SEQ_ID="+func};
                List funcLists = new ArrayList();
                funcLists.add("person");
                map = (HashMap)orm.loadDataSingle(dbConn, funcLists, filterPer);
                listPerson.addAll((List<Map>) map.get("PERSON"));
              }
            }
          }
 //         String[]str = deptStc.split(",");
//          for(int i = 0; i < str.length; i++){
//            func = str[i];
 //           String[] filterPer = new String[]{"SEQ_ID="+func};
 //           List funcLists = new ArrayList();
//            funcLists.add("person");
 //           map = (HashMap)orm.loadDataSingle(dbConn, funcLists, filterPer);
 //           System.out.println("mapffffffffff:"+map);
 //           listPerson.addAll((List<Map>) map.get("PERSON"));
 //         }
        }
      }
      if(listPerson.size()>1){
        for(Map m : listPerson){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append("},");
        }       
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map m : listPerson){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append("}");
        } 
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDeptLocal(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LONGIN_USER");
      int seqId = 181; //int seqId = person.getSeqId();
      YHORM orm = new YHORM();
      HashMap map = null;
      String[] filter = null;
      String[] filterPart = null;
      List<Map> list = new ArrayList();
      String deptStc = "";
      String func = "";
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"SEQ_ID=" + seqId};
      List funcList = new ArrayList();
      funcList.add("person");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("PERSON"));
      for(Map m : list){
        m.get("deptId");
        filter = new String[]{"SEQ_ID="+m.get("deptId")};
      }  
      List deptList = new ArrayList();
      deptList.add("department");
      map = (HashMap)orm.loadDataSingle(dbConn, deptList, filter);
      List<Map> listD = (List<Map>) map.get("OA_DEPARTMENT");
      YHFlowFormLogic dtt = new YHFlowFormLogic();
      List<Map> listPerson = new ArrayList();
      for(Map m : listD){
        m.get("deptParent");
        m.get("manager");
        m.get("seqId");
        if(m.get("manager") == null){
          List listLocal = dtt.deptLocal(dbConn, m.get("seqId"),seqId);
          for(int i = 0; i<listLocal.size(); i++){
            listLocal.get(i);
            String[] filterPer = new String[]{"SEQ_ID=" + listLocal.get(i)};
            List funcLists = new ArrayList();
            funcLists.add("person");
            map = (HashMap)orm.loadDataSingle(dbConn, funcLists, filterPer);
            listPerson.addAll((List<Map>) map.get("PERSON"));
          }
        }else{
          filterPart = new String[]{"SEQ_ID="+m.get("seqId")};
          deptStc = dtt.deleteDeptd(dbConn, m.get("seqId"));
          String[]str = deptStc.split(",");
          for(int i = 0; i < str.length; i++){
            func = str[i];
            String[] filterPer = new String[]{"SEQ_ID=" + func};
            List funcLists = new ArrayList();
            funcLists.add("person");
            map = (HashMap)orm.loadDataSingle(dbConn, funcLists, filterPer);
            listPerson.addAll((List<Map>) map.get("PERSON"));
          }
        }
      }
      for(Map m : listPerson){
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",userName:\"" + m.get("userName") + "\"");
        sb.append("},");
      }       
      sb.deleteCharAt(sb.length() - 1);       
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDeptFirst(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int deptId = 7; //int deptId = person.getDeptId();
      YHORM orm = new YHORM();
      HashMap map = null;
      String[] filter = null;
      List<Map> list = new ArrayList();
      List<Map> listD = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      String[] filters = new String[]{"SEQ_ID=" + ffl.deptFirstLogic(dbConn, deptId)};
      List funcList = new ArrayList();
      funcList.add("department");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list = (List<Map>) map.get("OA_DEPARTMENT");
      for(Map m : list){
        m.get("deptId");
        m.get("manager");
        if(m.get("manager") == null){
          for(int i = 0; i < ffl.getDeptLocalInput(dbConn, ffl.deptFirstLogic(dbConn, deptId)).size(); i++){
            List listFirst = ffl.getDeptLocalInput(dbConn, ffl.deptFirstLogic(dbConn, deptId));
            String[] filterPer = new String[]{"SEQ_ID=" + listFirst.get(i)};
            List listPer = new ArrayList();
            listPer.add("person");
            map = (HashMap)orm.loadDataSingle(dbConn, listPer, filterPer);
            listD.addAll((List<Map>) map.get("PERSON"));
          }
//          String[] filterPer = new String[]{"DEPT_ID=" + ffl.deptFirstLogic(dbConn, deptId)};
//          List listPer = new ArrayList();
//          listPer.add("person");
//          map = (HashMap)orm.loadDataSingle(dbConn, listPer, filterPer);
//          listD.addAll((List<Map>) map.get("PERSON"));
        }else{
          String str = String.valueOf(m.get("manager"));
          String[] deptManagetStr = str.split(",");
          for(int i = 0; i < deptManagetStr.length; i++){
            String arrayStr = deptManagetStr[i];
            filter = new String[]{"SEQ_ID=" + Integer.parseInt(arrayStr)};
            List deptList = new ArrayList();
            deptList.add("person");
            map = (HashMap)orm.loadDataSingle(dbConn, deptList, filter);
            listD.addAll((List<Map>) map.get("PERSON"));
          }
        }
      } 
      if(listD.size()>1){
        for(Map m : listD){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append("},");
        }  
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map m : listD){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append("}");
        }
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getUserName(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    Connection dbConn = null;
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      YHORM orm = new YHORM();
      Object obj = orm.loadObjSingle(dbConn, YHDocFlowFormType.class, seqId);
      data = YHFOM.toJson(obj).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getUserPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = 98; //int seqId = Integer.parseInt(person.getUserPriv());
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      String[] filters = new String[]{"SEQ_ID=" + seqId};
      List funcList = new ArrayList();
      funcList.add("userPriv");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("USER_PRIV"));
      for(Map m : list){
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",dprivName:\"" + m.get("privName") + "\"");
        sb.append("}");
      }       
      //sb.deleteCharAt(sb.length() - 1);       
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSelectData(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      HashMap map = null;
      StringBuffer sb = new StringBuffer("[");
      List<Map> list = new ArrayList();
      String[] filters = new String[]{"1 = 1 order by PRIV_NO, PRIV_NAME ASC"};
      List funcList = new ArrayList();
      funcList.add("userPriv");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("USER_PRIV"));
      if(list.size()>1){
        for(Map m : list){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",privNo:\"" + m.get("privNo") + "\"");
          sb.append(",privName:\"" + m.get("privName") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1);      
      }else{
        for(Map m : list){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",privNo:\"" + m.get("privNo") + "\"");
          sb.append(",privName:\"" + m.get("privName") + "\"");
          sb.append("}");
        }
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDeptLocalInput(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = 175; //int seqId = person.getSeqId();
      int deptId = 14; //int deptId = person.getDeptId();
      YHORM orm = new YHORM();
      HashMap map = null;
      String managerSeqId = null;
      String[] filterPart = null;
      List<Map> list = new ArrayList();
      List<Map> listD = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      String[] filters = new String[]{"SEQ_ID=" + deptId};  // +"AND NOT SEQ_ID=" + seqId
      List funcList = new ArrayList();
      funcList.add("department");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_DEPARTMENT"));
      for(Map md : list){
        if(md.get("manager") == null){  
          for(int i = 0; i < ffl.getDeptLocalInput(dbConn, deptId).size(); i++){
            List listInput = ffl.getDeptLocalInput(dbConn, deptId);
            filterPart = new String[]{"SEQ_ID=" + listInput.get(0)};
            List funcListD = new ArrayList();
            funcListD.add("person");
            map = (HashMap)orm.loadDataSingle(dbConn, funcListD, filterPart);
            listD.addAll((List<Map>) map.get("PERSON"));
          }
        }else{
          String str = String.valueOf(md.get("manager"));
          String[] managerStr = str.split(",");
          for(int a = 0; a < managerStr.length; a++){
            managerSeqId = managerStr[0];
          }
          filterPart = new String[]{"SEQ_ID=" + Integer.parseInt(managerSeqId)};
          List funcListD = new ArrayList();
          funcListD.add("person");
          map = (HashMap)orm.loadDataSingle(dbConn, funcListD, filterPart);
          listD.addAll((List<Map>) map.get("PERSON"));
        }
      }
      if(listD.size() > 1){
        for(Map m : listD){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1);  
      }else{
        for(Map m : listD){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append("}");
        }
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getFormName(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    Connection dbConn = null;
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = 53; //person.getSeqId();
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      YHORM orm = new YHORM();
      Object obj = orm.loadObjSingle(dbConn, YHDocFlowFormType.class, seqId);
      data = YHFOM.toJson(obj).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDeptNameLong(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    Connection dbConn = null;
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = 6; //int seqId = person.getDeptId();
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      HashMap map = null;
      YHORM orm = new YHORM();
      List<Map> listLong = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      List list = new ArrayList();
      List listDeptLong = ffl.getDeptNameLong(dbConn, seqId);
      for(int i = listDeptLong.size() - 1; i >= 0; i--){
        String[] filter = new String[]{"SEQ_ID=" + listDeptLong.get(i)};
        List funLong = new ArrayList();
        funLong.add("department");
        map = (HashMap)orm.loadDataSingle(dbConn, funLong, filter);
        listLong.addAll((List<Map>) map.get("OA_DEPARTMENT"));
      }
      if(listLong.size() > 1){
        for(Map m : listLong){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",deptName:\"" + m.get("deptName") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1);
      }else{
        for(Map m : listLong){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",deptName:\"" + m.get("deptName") + "\"");
          sb.append("}");
        }
      }
      sb.append("]");
      //System.out.println(sb);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDeptNameShort(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    Connection dbConn = null;
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = 6; //int seqId = person.getDeptId();
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      YHORM orm = new YHORM();
      Object obj = orm.loadObjSingle(dbConn, YHDepartment.class, seqId);
      data = YHFOM.toJson(obj).toString();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDeptParentInput(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = 175;  //int seqId = person.getSeqId();
      int deptId = 6;   //int deptId = person.getDeptId();
      YHORM orm = new YHORM();
      HashMap map = null;
      String[] filter = null;
      String[] filterPart = null;
      String deptStc = "";
      String func = "";
      StringBuffer sb = new StringBuffer("[");
      filter = new String[]{"SEQ_ID=" + deptId};
      List deptList = new ArrayList();
      deptList.add("department");
      map = (HashMap)orm.loadDataSingle(dbConn, deptList, filter);
      List<Map> listD = (List<Map>) map.get("OA_DEPARTMENT");
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      List<Map> listPerson = new ArrayList();
      for(Map m : listD){
        m.get("deptParent");  //6
        m.get("seqId");    //7
        m.get("manager");
        if(m.get("deptParent").equals(0)){
          if(m.get("manager") == null){ 
            for(int i = 0; i < ffl.getDeptLocalInput(dbConn, m.get("seqId")).size(); i++){
              List listInput = ffl.getDeptLocalInput(dbConn, m.get("seqId"));
              filterPart = new String[]{"SEQ_ID=" + listInput.get(0)};
              List funcListD = new ArrayList();
              funcListD.add("person");
              map = (HashMap)orm.loadDataSingle(dbConn, funcListD, filterPart);
              listPerson.addAll((List<Map>) map.get("PERSON"));
            }
          }else{
            deptStc = ffl.deleteDeptd(dbConn, m.get("seqId"));
            String[]str = deptStc.split(",");
            for(int i = 0; i < str.length; i++){
              func = str[0];
              String[] filterPer = new String[]{"SEQ_ID=" + func};
              List funcLists = new ArrayList();
              funcLists.add("person");
              map = (HashMap)orm.loadDataSingle(dbConn, funcLists, filterPer);
              listPerson.addAll((List<Map>) map.get("PERSON"));
            }
          }
        }else{
          filterPart = new String[]{"SEQ_ID=" + m.get("deptParent")};
          List listPart = new ArrayList();
          listPart.add("department");
          map = (HashMap)orm.loadDataSingle(dbConn, listPart, filterPart);
          List<Map> listDeptParent = (List<Map>) map.get("OA_DEPARTMENT");
          for(Map md : listDeptParent){
            md.get("deptParent");  //14
            md.get("manager"); //null
            md.get("seqId");   // 116
            if(md.get("manager") == null){
              for(int i = 0; i < ffl.getDeptLocalInput(dbConn, md.get("seqId")).size(); i++){
                List listInput = ffl.getDeptLocalInput(dbConn, md.get("seqId"));
                filterPart = new String[]{"SEQ_ID=" + listInput.get(0)};
                List funcListD = new ArrayList();
                funcListD.add("person");
                map = (HashMap)orm.loadDataSingle(dbConn, funcListD, filterPart);
                listPerson.addAll((List<Map>) map.get("PERSON"));
              }
             // String[] filterParent = new String[]{"DEPT_ID=" + md.get("manager")};
             // List funcListParent = new ArrayList();
              //funcListParent.add("person");
              //map = (HashMap)orm.loadDataSingle(dbConn, funcListParent, filterParent);
             // listPerson.addAll((List<Map>) map.get("PERSON"));
            }else{
              String deptParentStr = String.valueOf(md.get("manager"));
              String[] str = deptParentStr.split(",");
              for(int i = 0; i < str.length; i++){
                func = str[0];
                String[] filterPer = new String[]{"SEQ_ID=" + func};
                List funcLists = new ArrayList();
                funcLists.add("person");
                map = (HashMap)orm.loadDataSingle(dbConn, funcLists, filterPer);
                listPerson.addAll((List<Map>) map.get("PERSON"));
              }
            }
          }
        }
      }
      if(listPerson.size() > 1){
        for(Map m : listPerson){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append("},");
        } 
        sb.deleteCharAt(sb.length() - 1);  
      }else{
        for(Map m : listPerson){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append("}");
        } 
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getDeptFirstInput(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = 175;  //person.getSeqId();
      int deptId = 7;   //person.getDeptId();
      YHORM orm = new YHORM();
      HashMap map = null;
      String[] filter = null;
      String[] filterPart = null;
      List<Map> list = new ArrayList();
      List<Map> listD = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      String[] filters = new String[]{"SEQ_ID=" + ffl.deptFirstLogic(dbConn, deptId)};
      List funcList = new ArrayList();
      funcList.add("department");
      map = (HashMap)orm.loadDataSingle(dbConn, funcList, filters);
      list.addAll((List<Map>) map.get("OA_DEPARTMENT"));
      for(Map m : list){
        m.get("deptId");
        m.get("manager");
        if(m.get("manager") == null){ 
          for(int i = 0; i < ffl.getDeptLocalInput(dbConn,  m.get("seqId")).size(); i++){
            List listInput = ffl.getDeptLocalInput(dbConn,  m.get("seqId"));
            filterPart = new String[]{"SEQ_ID=" + listInput.get(0)};
            List funcListD = new ArrayList();
            funcListD.add("person");
            map = (HashMap)orm.loadDataSingle(dbConn, funcListD, filterPart);
            //System.out.println(map);
            listD.addAll((List<Map>) map.get("PERSON"));
          }
        }else{
          String str = String.valueOf(m.get("manager"));
          String[] deptManagetStr = str.split(",");
          for(int i = 0; i < deptManagetStr.length; i++){
            String arrayStr = deptManagetStr[0];
            filter = new String[]{"SEQ_ID=" + Integer.parseInt(arrayStr)};
            List deptList = new ArrayList();
            deptList.add("person");
            map = (HashMap)orm.loadDataSingle(dbConn, deptList, filter);
            listD.addAll((List<Map>) map.get("PERSON"));
          }
        }
      } 
      if(listD.size() > 1){
        for(Map m : listD){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append("},");
        } 
        sb.deleteCharAt(sb.length() - 1); 
      }else{
        for(Map m : listD){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",userName:\"" + m.get("userName") + "\"");
          sb.append("}");
        } 
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateJsForm(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      String script = request.getParameter("script");
      int seqId = Integer.parseInt(request.getParameter("seqId"));    
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      script = script.replaceAll("'", "''");
      String query = "update "+ YHWorkFlowConst.FLOW_FORM_TYPE +" set SCRIPT = '"+ script +"' where SEQ_ID = " + seqId;
      Statement stm = null;
      try {
        stm = dbConn.createStatement();
        stm.executeUpdate(query);
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, null, null); 
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateCssForm(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      String css = request.getParameter("css");
      int seqId = Integer.parseInt(request.getParameter("seqId"));    
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      css = css.replaceAll("'", "''");
      String query = "update "+ YHWorkFlowConst.FLOW_FORM_TYPE +" set CSS = '"+ css +"' where SEQ_ID = " + seqId;
      Statement stm = null;
      try {
        stm = dbConn.createStatement();
        stm.executeUpdate(query);
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, null, null); 
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getJsForm(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      String sSeqId = request.getParameter("seqId");
      int seqId = Integer.parseInt(sSeqId);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String query = "select SCRIPT from "+ YHWorkFlowConst.FLOW_FORM_TYPE +" where SEQ_ID = " + seqId;
      String data = "";
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = dbConn.createStatement();
        rs = stm.executeQuery(query);
        if (rs.next()) {
          data = rs.getString("SCRIPT");
          if (data == null) {
            data = "";
          }
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null); 
      }
      request.setAttribute("script", data);
      request.setAttribute("seqId", sSeqId);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowform/editor/plugins/NJs/njs.jsp";
  }
  public String getCssForm(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      String sSeqId = request.getParameter("seqId");
      int seqId = Integer.parseInt(sSeqId);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String query = "select CSS from "+ YHWorkFlowConst.FLOW_FORM_TYPE +" where SEQ_ID = " + seqId;
      String data = "";
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = dbConn.createStatement();
        rs = stm.executeQuery(query);
        if (rs.next()) {
          data = rs.getString("CSS");
          if (data == null) {
            data = "";
          }
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null); 
      }
      request.setAttribute("css", data);
      request.setAttribute("seqId", sSeqId);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowform/editor/plugins/NCss/ncss.jsp";
  }
  public String getRunName(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    Connection dbConn = null;
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = 3; // FlowRun 表中的      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      HashMap map = null;
      YHORM orm = new YHORM();
      List<Map> listLong = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      List list = new ArrayList();
      String[] filter = new String[]{"SEQ_ID=" + seqId};
      List funLong = new ArrayList();
      funLong.add("flowRun");
      map = (HashMap)orm.loadDataSingle(dbConn, funLong, filter);
      listLong.addAll((List<Map>) map.get("OA_FL_RUN"));
      if(listLong.size() > 1){
        for(Map m : listLong){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",runName:\"" + m.get("runName") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1);
      }else{
        for(Map m : listLong){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",runName:\"" + m.get("runName") + "\"");
          sb.append("}");
        }
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getBeginTime(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    Connection dbConn = null;
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = 3; // FlowRun 表中的
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      HashMap map = null;
      YHORM orm = new YHORM();
      List<Map> listLong = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      List list = new ArrayList();
      String[] filter = new String[]{"SEQ_ID=" + seqId};
      List funLong = new ArrayList();
      funLong.add("flowRun");
      map = (HashMap)orm.loadDataSingle(dbConn, funLong, filter);
      listLong.addAll((List<Map>) map.get("OA_FL_RUN"));
      if(listLong.size() > 1){
        for(Map m : listLong){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",beginTime:\"" + m.get("beginTime") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1);
      }else{
        for(Map m : listLong){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",beginTime:\"" + m.get("beginTime") + "\"");
          sb.append("}");
        }
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getEndTime(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    Connection dbConn = null;
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = 3; // FlowRun 表中的
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      HashMap map = null;
      YHORM orm = new YHORM();
      List<Map> listLong = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      List list = new ArrayList();
      String[] filter = new String[]{"SEQ_ID=" + seqId};
      List funLong = new ArrayList();
      funLong.add("flowRun");
      map = (HashMap)orm.loadDataSingle(dbConn, funLong, filter);
      listLong.addAll((List<Map>) map.get("OA_FL_RUN"));
      if(listLong.size() > 1){
        for(Map m : listLong){
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",endTime:\"" + m.get("endTime") + "\"");
          sb.append("},");
        }
        sb.deleteCharAt(sb.length() - 1);
      }else{
        for(Map m : listLong){
         // String str = String.valueOf(m.get("endTime"));
         // if(m.get("endTime")==null){
         //   str = "";
         // }
          sb.append("{");
          sb.append("seqId:\"" + m.get("seqId") + "\"");
          sb.append(",endTime:\"" + m.get("endTime") + "\"");
          sb.append("}");
        }
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getRunId(HttpServletRequest request, HttpServletResponse response)
  throws Exception{
    Connection dbConn = null;
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = 3; // FlowRun 表中的
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      HashMap map = null;
      YHORM orm = new YHORM();
      List<Map> listLong = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      List list = new ArrayList();
      String[] filter = new String[]{"SEQ_ID=" + seqId};
      List funLong = new ArrayList();
      funLong.add("flowRun");
      map = (HashMap)orm.loadDataSingle(dbConn, funLong, filter);
      listLong.addAll((List<Map>) map.get("OA_FL_RUN"));
      for(Map m : listLong){
         // String str = String.valueOf(m.get("endTime"));
         // if(m.get("endTime")==null){
         //   str = "";
         // }
        sb.append("{");
        sb.append("seqId:\"" + m.get("seqId") + "\"");
        sb.append(",runId:\"" + m.get("runId") + "\"");
        sb.append("}");
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSeqList(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sql = "select form_name,css from "+ YHWorkFlowConst.FLOW_FORM_TYPE ;
      String a = sql.substring(sql.indexOf("t")+1,sql.indexOf("from"));
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      List list = ffl.getSeqList(dbConn, sql);
      StringBuffer sb = new StringBuffer("[");
      for(int i = 0; i < list.size(); i++){
        sb.append("{");
        sb.append("seqId:\"" + list.get(i) + "\"");
        sb.append(",formName:\"" + list.get(i) + "\"");
        sb.append("},");
      }
      sb.deleteCharAt(sb.length() - 1);
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSeq(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seq = request.getParameter("seq");
      //System.out.println("YYYYYYYYYY"+seq);
      String sqls = seq.replaceAll("`","'");
      //System.out.println("KKKKKKKKKKKKKKKKK"+sqls);
      String sql = "select form_name,css from "+ YHWorkFlowConst.FLOW_FORM_TYPE +" where SEQ_ID=63";
      //System.out.println(sql);
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      String name = ffl.getSeq(dbConn, sqls);
      StringBuffer sb = new StringBuffer("[");
      sb.append("{");
      sb.append("formName:\"" + name + "\"");
      sb.append("}");
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSql(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seq = request.getParameter("sql");
      String sqls = seq.replaceAll("`","'");
      //String sql = "select form_name,css from flow_form_type where SEQ_ID=63";
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      ffl.getSql(dbConn, seq);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功：SQL语句测试正常！");
      //request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getPrcsList(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int seqId = 34; //FLOW_PROCESS表中的
      YHORM orm = new YHORM();
      HashMap map = null;
      List<Map> list = new ArrayList();
      StringBuffer sb = new StringBuffer("[");
      YHFlowFormLogic ffl = new YHFlowFormLogic();
      String strPrcs = ffl.getPrcsList(dbConn, seqId);
      String[] prcsStr = strPrcs.split(",");
      for(int i = 0; i < prcsStr.length; i++){
        String funcs = prcsStr[i];
        sb.append("{");
        sb.append("seqId:\"" + prcsStr[i] + "\"");
        sb.append(",prcsUser:\"" + prcsStr[i] + "\"");
        sb.append("},");
      }       
      sb.deleteCharAt(sb.length() - 1);       
      sb.append("]");
      //System.out.println(sb.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public static void main(String[] args) {
    String data = "function dd(){\r\n}";
    //System.out.println(data);
    //data = data.replaceAll("\'", "\\\\'");
    data = data.replaceAll("\\n", "\\\\n");
    data = data.replaceAll("\\r", "\\\\r");
    
    //System.out.println(data);
  }
}
