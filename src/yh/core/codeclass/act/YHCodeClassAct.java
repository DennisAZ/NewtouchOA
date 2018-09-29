package yh.core.codeclass.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.codeclass.data.YHCodeClass;
import yh.core.codeclass.data.YHCodeItem;
import yh.core.codeclass.logic.YHCodeClassLogic;
import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;
public class YHCodeClassAct {
  private static Logger log = Logger.getLogger(YHCodeClassAct.class);
  
  public String listCodeClass(HttpServletRequest request,
      HttpServletResponse response) throws Exception {   
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null; 
    List<YHCodeClass> codeClassList = null;
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String queryStr = "select SEQ_ID, CLASS_NO, SORT_NO, CLASS_DESC, CLASS_LEVEL from oa_kind_dict";
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(queryStr);
      YHCodeClass codeClass = null;
      codeClassList = new ArrayList<YHCodeClass>();
      
      while (rs.next()){
        codeClass = new YHCodeClass();
        codeClass.setSqlId(rs.getInt("SEQ_ID"));
        codeClass.setClassNo(rs.getString("CLASS_NO"));
        codeClass.setSortNo(rs.getString("SORT_NO"));
        codeClass.setClassDesc(rs.getString("CLASS_DESC"));
        codeClass.setClassLevel(rs.getString("CLASS_LEVEL"));
        codeClassList.add(codeClass);
      }
  
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }finally {
        YHDBUtility.close(stmt, rs, log);
    }
    
    request.setAttribute("codeClassList", codeClassList);
    return "/core/codeclass/codeclasslist.jsp";
  }
  
  public String listCodeItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null; 
    List<YHCodeItem> codeList = null;
    String classNo = "";
    String seqId  = request.getParameter("seqId");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      if(seqId!=null&&!seqId.equals("")){
        YHCodeClassLogic tccl = new YHCodeClassLogic();
        YHCodeClass codeClass = tccl.selectCodeClassById(dbConn, seqId);
        classNo = codeClass.getClassNo();
        String classDesc = codeClass.getClassDesc();
        request.setAttribute("classDesc", classDesc);
        classNo = classNo.replace("'", "''");
        String queryStr = "select SEQ_ID, CLASS_NO, SORT_NO, CLASS_DESC, CLASS_CODE from oa_kind_dict_item where CLASS_NO = '" + classNo + "'";
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(queryStr);
        YHCodeItem codeItem = null;
        codeList = new ArrayList<YHCodeItem>();
        
        while(rs.next()){
          codeItem = new YHCodeItem();
          codeItem.setSqlId(rs.getInt("SEQ_ID"));
          codeItem.setClassNo(rs.getString("CLASS_NO"));
          codeItem.setSortNo(rs.getString("SORT_NO"));
          codeItem.setClassDesc(rs.getString("CLASS_DESC"));
          codeItem.setClassCode(rs.getString("CLASS_CODE"));
          codeList.add(codeItem);
        } 
        
       
      }

    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
    request.setAttribute("codeList", codeList);
    return "/core/codeclass/codeitemlist.jsp?classNo=" + classNo+"&seqId="+seqId;
  }
  
  public String deleteCodeClass(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("utf-8");
    String sqlId = request.getParameter("sqlId");
    String classNo = request.getParameter("classNo");
    Connection dbConn = null;
    Statement stmt = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      stmt = dbConn.createStatement();
      if(sqlId!=null&&!sqlId.equals("")){
        YHCodeClassLogic tccl = new YHCodeClassLogic();
        YHCodeClass codeClass = tccl.selectCodeClassById(dbConn, sqlId);
        if(codeClass!=null){
          classNo = codeClass.getClassNo().replace("'", "''");
          String sql = "delete from oa_kind_dict_item where CLASS_NO = '" + classNo + "'";
          stmt.executeUpdate(sql);
      
          sql = "delete from oa_kind_dict where SEQ_ID = " + sqlId;
          stmt.executeUpdate(sql);
        }
      }
     
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除主分类成功！");  
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }finally {
        YHDBUtility.close(stmt, null, log);
    }
    return "/core/inc/rtjson.jsp";
  }

  public String deleteCodeItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String sqlId = request.getParameter("sqlId");
    Connection dbConn = null;
    Statement stmt = null;
 
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String queryStr = "delete from oa_kind_dict_item where SEQ_ID= " + sqlId;
      stmt = dbConn.createStatement();
      stmt.executeUpdate(queryStr);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "删除成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }finally {
        YHDBUtility.close(stmt, null, log);
    } 
    return "/core/inc/rtjson.jsp";
  }
  
  public String getCodeClass(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      
      String sqlId = request.getParameter("sqlId");
      YHCodeClass codeClass = null;
      
      Connection dbConn = null;
      Statement stmt = null;
      ResultSet rs = null; 
      
      try {
        String queryStr = "select SEQ_ID, CLASS_NO, SORT_NO,CLASS_DESC, CLASS_LEVEL from oa_kind_dict where SEQ_ID= " + sqlId;
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(queryStr);
       
        if(rs.next()){
          codeClass = new YHCodeClass();
          codeClass.setSqlId(rs.getInt(1));
          codeClass.setClassNo(rs.getString(2));
          codeClass.setSortNo(rs.getString(3));
          codeClass.setClassDesc(rs.getString(4));
          codeClass.setClassLevel(rs.getString(5));
        }
        String data = "{sqlId:" + codeClass.getSqlId() + ", classNo:\"" + YHUtility.encodeSpecial(codeClass.getClassNo()) + "\", sortNo:\""+  YHUtility.encodeSpecial(codeClass.getSortNo()) +"\", classDesc:\"" + YHUtility.encodeSpecial(codeClass.getClassDesc())+ "\", classLevel:\"" +YHUtility.encodeSpecial(codeClass.getClassLevel()) +"\"}";
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功获取主分类的数据");
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }finally {
        YHDBUtility.close(stmt, rs, log);
      }
      return "/core/inc/rtjson.jsp";
    }

  public String getCodeItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
      
      String sqlId = request.getParameter("sqlId");
      YHCodeItem codeItem = null;
      
      Connection dbConn = null;
      Statement stmt = null;
      ResultSet rs = null; 
      
      try {
        YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        
        String queryStr = "select SEQ_ID, CLASS_NO, CLASS_CODE, SORT_NO, CLASS_DESC from oa_kind_dict_item where SEQ_ID= " + sqlId;
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(queryStr);
       
        if (rs.next()) {
          codeItem = new YHCodeItem();
          codeItem.setSqlId(rs.getInt("SEQ_ID"));
          codeItem.setClassNo(rs.getString("CLASS_NO"));
          codeItem.setClassCode(rs.getString("CLASS_CODE"));
          codeItem.setSortNo(rs.getString("SORT_NO"));
          codeItem.setClassDesc(rs.getString("CLASS_DESC"));
        }
        String data = "{sqlId:" + codeItem.getSqlId() + ", classNo:\"" + YHUtility.encodeSpecial(codeItem.getClassNo()) + "\", classCode:\""+ YHUtility.encodeSpecial(codeItem.getClassCode()) + "\", sortNo:\""+ codeItem.getSortNo() +"\", classDesc:\"" +YHUtility.encodeSpecial(codeItem.getClassDesc()) +"\"}";
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG,"成功获取代码项的数据");
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }catch(Exception ex) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
        throw ex;
      }finally {
        YHDBUtility.close(stmt, rs, log);
      }
      return "/core/inc/rtjson.jsp";
  }
  
  public String addCodeClass(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String classNo = request.getParameter("classNo");
    String sortNo = request.getParameter("sortNo");
    String classDesc = request.getParameter("classDesc");
    String classLevel = request.getParameter("classLevel");

    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null; 

    if(classNo == null || "".equals(classNo)) {
      return "/core/inc/rtjson.jsp";
    }
    
    if(sortNo == null) {
      return "/core/inc/rtjson.jsp";
    }
    
    if(classDesc == null) {
      return "/core/inc/rtjson.jsp";
    }
    
    PreparedStatement pstmt = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      stmt = dbConn.createStatement();
      classNo = classNo.replace("'", "''");
      String sql = "select count(*) from oa_kind_dict where CLASS_NO = '" + classNo + "'";
      rs = stmt.executeQuery(sql);
      long count = 0;
      if(rs.next()){      
        count = rs.getLong(1);
      }
      if(count == 1) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "编码重复, 编码不能重复");
        return "/core/inc/rtjson.jsp";
      }
      
      String queryStr = "insert into oa_kind_dict (CLASS_NO, SORT_NO, CLASS_DESC, CLASS_LEVEL) values(?, ?, ?, ?)";
      pstmt = dbConn.prepareStatement(queryStr);
      pstmt.setString(1, classNo);
      pstmt.setString(2, sortNo);
      pstmt.setString(3, classDesc);
      pstmt.setString(4, classLevel);
      pstmt.executeUpdate();
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "主分类添加成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }finally {
        YHDBUtility.close(stmt,null,log);
    }    
    return "/core/inc/rtjson.jsp";   
  }
  
  public String addCodeItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String classNo = request.getParameter("classNo");
    String classCode = request.getParameter("classCode");
    String sortNo = request.getParameter("sortNo");
    String classDesc = request.getParameter("classDesc");
    
    Connection dbConn = null;
    Statement stmt = null; 
    ResultSet rs = null; 
    PreparedStatement pstmt = null;
    
    if(classCode == null) {
      return "/core/inc/rtjson.jsp";
    }
    if(sortNo == null) {
      return "/core/inc/rtjson.jsp";
    }
    
    if(classDesc == null) {
      return "/core/inc/rtjson.jsp";
    }
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      //classCode = classCode.replace("'", "''");
      //classNo = classNo.replace("'", "''");
      String sql = "select count(*) from oa_kind_dict_item where CLASS_CODE = '" +  classCode.replace("'", "''") + "' and CLASS_NO = '" + classNo.replace("'", "''") +"'";
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      long count = 0;
      if(rs.next()){      
        count = rs.getLong(1);
      }
      if(count == 1) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "代码编号重复, 代码编号不能重复");
        return "/core/inc/rtjson.jsp";
      }
      
      String queryStr = "insert into oa_kind_dict_item(CLASS_NO, CLASS_CODE, SORT_NO, CLASS_DESC) values(?, ?, ?, ?)";
      pstmt = dbConn.prepareStatement(queryStr);
      pstmt.setString(1, classNo);
      pstmt.setString(2, classCode);
      pstmt.setString(3, sortNo);
      pstmt.setString(4, classDesc);
      pstmt.executeUpdate();
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "代码项添加成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }finally {
      YHDBUtility.close(pstmt,null,log);
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String updateCodeClass(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String classNofirst = request.getParameter("classNofirst"); 
    String classNo = request.getParameter("classNo");
    String classDesc = request.getParameter("classDesc");
    String sortNo = request.getParameter("sortNo");
    String classLevel = request.getParameter("classLevel");
    
    Connection dbConn = null;
    PreparedStatement pstmt = null;
    Statement stmt = null;
    ResultSet rs = null; 

    if(classNo == null || "".equals(classNo)) {
      return "/core/inc/rtjson.jsp";
    }
    
    if(sortNo == null) {
      return "/core/inc/rtjson.jsp";
    }
    
    if(classDesc == null) {
      return "/core/inc/rtjson.jsp";
    }
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      if(classNo.equals(classNofirst)) {
        String updateStr = "update oa_kind_dict set SORT_NO = ?, CLASS_DESC = ? , CLASS_LEVEL = ? where CLASS_NO = ?";
        pstmt = dbConn.prepareStatement(updateStr);
        pstmt.setString(1, sortNo);
        pstmt.setString(2, classDesc);
        pstmt.setString(3, classLevel);
        pstmt.setString(4, classNo);
        pstmt.executeUpdate();
        
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "修改主分类成功！");  
        return "/core/inc/rtjson.jsp";
      }
      String sql = "select count(*) from oa_kind_dict where CLASS_NO = '" + classNo.replace("'", "''") + "'";
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      long count = 0;
      if(rs.next()){
        count = rs.getLong(1);
      }       
      if(count > 0) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "编码重复, 编码不能重复");
        return "/core/inc/rtjson.jsp";
      }
      
      String sqlStr = "insert into oa_kind_dict (CLASS_NO, SORT_NO, CLASS_DESC, CLASS_LEVEL) values(?, ?, ?, ?)";
      pstmt = dbConn.prepareStatement(sqlStr);
      pstmt.setString(1, classNo);
      pstmt.setString(2, classDesc);
      pstmt.setString(3, sortNo);
      pstmt.setString(4, classLevel);
      pstmt.executeUpdate();
      
      String updateStr = "update oa_kind_dict_item set CLASS_NO = ? where CLASS_NO = ?";
      pstmt = dbConn.prepareStatement(updateStr);
      pstmt.setString(1, classNo);
      pstmt.setString(2, classNofirst);
      pstmt.executeUpdate();
      classNofirst = classNofirst.replace("'", "'"); 
      String deleteStr = "delete from oa_kind_dict where CLASS_NO = '" + classNofirst + "'";
      stmt.executeUpdate(deleteStr);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改主子表成功！");  
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }finally {
      YHDBUtility.close(pstmt,null,log);
    }    
    return "/core/inc/rtjson.jsp"; 
  }

  
  public String updateCodeItem(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null; 
    
    String sqlId = request.getParameter("sqlId");  
    String classNo = request.getParameter("classNo");
    String classCode = request.getParameter("classCode");
    String classCodeOld = request.getParameter("classCodeOld");
    String sortNo = request.getParameter("sortNo");
    String classDesc = request.getParameter("classDesc");
 

    if(classCode == null) {
      return "/core/inc/rtjson.jsp";
    }
    
    if(sortNo == null) {
      return "/core/inc/rtjson.jsp";
    }
    
    if(classDesc == null) {
      return "/core/inc/rtjson.jsp";
    }
    
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      if(YHUtility.isInteger(sqlId)){
        if(classCode.equals(classCodeOld)) {
          String updateStr = "update oa_kind_dict_item set CLASS_NO = ?, SORT_NO = ?, CLASS_DESC = ? where SEQ_ID = ?";
          pstmt = dbConn.prepareStatement(updateStr);
          pstmt.setString(1, classNo);
          pstmt.setString(2, sortNo);
          pstmt.setString(3, classDesc);
          pstmt.setInt(4, Integer.parseInt(sqlId));
          pstmt.executeUpdate();
          
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "修改代码项成功！");  
          return "/core/inc/rtjson.jsp";
        }
         
        String sql = "select count(*) from oa_kind_dict_item where CLASS_CODE = '" + classCode.replace("'", "''") + "' and CLASS_NO = '" + classNo.replace("'", "''") +"'";
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(sql);
        long count = 0;
        if(rs.next()){      
          count = rs.getLong(1);
        }
        if(count == 1) {
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "代码编号重复, 代码编号不能重复");
          return "/core/inc/rtjson.jsp";
        }
        
        String updateStr = "update oa_kind_dict_item set CLASS_NO = ?, CLASS_CODE = ?, SORT_NO = ?, CLASS_DESC = ? where SEQ_ID = ?";
        
        pstmt = dbConn.prepareStatement(updateStr);
        pstmt.setString(1, classNo);
        pstmt.setString(2, classCode);
        pstmt.setString(3, sortNo);
        pstmt.setString(4, classDesc);
       
        pstmt.setInt(5, Integer.parseInt(sqlId));
        pstmt.executeUpdate();
        
      }
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "改代码编号后修改代码项成功！");  
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }finally {
        YHDBUtility.close(pstmt,null,log);
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 

   * 获取代码   by No
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getCodeItemByNo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String parentNo = request.getParameter("parentNo") == null ? "" :  request.getParameter("parentNo");
      YHCodeClassLogic codeLogic = new YHCodeClassLogic();
      String data = "[";
      List<YHCodeItem> itemList = new ArrayList<YHCodeItem>();
      itemList = codeLogic.getCodeItem(dbConn, parentNo);
      for (int i = 0; i < itemList.size(); i++) {
        YHCodeItem item = itemList.get(i);
        data = data + YHFOM.toJson(item) + ",";
      }
      if (itemList.size() > 0) {
        data = data.substring(0, data.length() - 1);
      }
      data = data + "]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "添加成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
