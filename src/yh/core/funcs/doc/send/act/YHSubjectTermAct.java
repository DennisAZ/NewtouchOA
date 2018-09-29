package yh.core.funcs.doc.send.act;

import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.jexcel.util.YHCSVUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.syslog.logic.YHSysLogLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHLogConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;
import yh.core.funcs.doc.send.data.YHSubjectTerm;
import yh.core.funcs.doc.send.logic.YHSubjectTermLogic;

public class YHSubjectTermAct {
  public String insertWord(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String word=request.getParameter("word");
      int parentId=Integer.parseInt(request.getParameter("parentId"));
      int sortNo=Integer.parseInt(request.getParameter("sortNo"));
      int typeFlag=Integer.parseInt(request.getParameter("typeFlag"));
      YHSubjectTerm st = new YHSubjectTerm();
      st.setParentId(parentId);
      st.setWord(word);
      st.setSortNo(sortNo);
      st.setTypeFlag(typeFlag);
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, st);
      YHSubjectTerm st1 = new YHSubjectTerm();
      YHSubjectTermLogic stl = new YHSubjectTermLogic();
      st1=stl.getMaxSeqId(dbConn);
      int nodeId=st1.getSeqId();
      String name=st1.getWord();
      int typeFlag1 = st1.getTypeFlag();
      String data = "[{nodeId:\"" + nodeId + "\",name:\"" + name + "\",typeFlag:\""+typeFlag1+"\" }]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
  	  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
  	  request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
  	  throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
	
  public String updateWord(HttpServletRequest request,HttpServletResponse response) throws Exception{
	  Connection dbConn=null;
	  YHORM orm = new YHORM();
	  try{ 
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqIdStr = request.getParameter("treeId");
      String word=request.getParameter("word");
      int parentId=Integer.parseInt(request.getParameter("parentId"));
      int sortNo=Integer.parseInt(request.getParameter("sortNo"));
      int typeFlag=Integer.parseInt(request.getParameter("typeFlag"));
      int seqId = 0;
      if (!YHUtility.isNullorEmpty(seqIdStr)) {
        seqId = Integer.parseInt(seqIdStr);
      }
      YHSubjectTerm st = (YHSubjectTerm) orm.loadObjSingle(dbConn, YHSubjectTerm.class, seqId);
      st.setParentId(parentId);
      st.setSortNo(sortNo);
      st.setTypeFlag(typeFlag);
      st.setWord(word);
      orm.updateSingle(dbConn,st);
      int nodeId=st.getSeqId();
      String name=st.getWord();
      int typeFlag1 = st.getTypeFlag();
      String data = "[{nodeId:\"" + nodeId + "\",name:\"" + name + "\",typeFlag:\""+typeFlag1+"\" }]";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功修改数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
	  } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
	  }
	  return "/core/inc/rtjson.jsp";
  }
  public String getDept(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      int treeId = Integer.parseInt(request.getParameter("treeId"));
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String data = null;
      YHORM orm = new YHORM();
      Object obj = orm.loadObjSingle(dbConn, YHSubjectTerm.class, treeId);
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
	
  public String selectWordToAttendance(HttpServletRequest request,HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson user = (YHPerson) request.getSession().getAttribute( YHConst.LOGIN_USER);
      YHSubjectTermLogic wordLogic = new YHSubjectTermLogic();
      int userDeptIdFunc = Integer.parseInt(request.getParameter("userDeptId"));
      String data = "";
      data = wordLogic.getWordTreeJson1(0, dbConn, userDeptIdFunc);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex) {
	    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
	    throw ex;
	  }
    return "/core/inc/rtjson.jsp";
  }
  
  public static boolean findId(String str, String id) {
	  if(str == null || id == null || "".equals(str) || "".equals(id)){
      return false;
    }
    String[] aStr = str.split(",");
    for(String tmp : aStr){
      if(tmp.equals(id)){
        return true;
      }
    }
	  return false;
  }
  public String deleteDept(HttpServletRequest request,HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
		int seqId = Integer.parseInt(request.getParameter("treeId"));
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    dbConn = requestDbConn.getSysDbConn();
    YHSubjectTerm st = new YHSubjectTerm();
    st.setSeqId(seqId);
    YHORM orm = new YHORM();
    YHSubjectTermLogic wordlogic = new YHSubjectTermLogic();
    List lista = new ArrayList();
    List lidd = wordlogic.deleteWordMul(dbConn, seqId);
    lidd.add(st);
    for(int i = 0; i < lidd.size(); i++){
      YHSubjectTerm deptent = (YHSubjectTerm) lidd.get(i);
      String[] filters = new String[]{"DEPT_ID = " + deptent.getSeqId() + ""};
      List<YHPerson> listPer = orm.loadListSingle(dbConn, YHPerson.class, filters);
      for(int x = 0; x < listPer.size(); x++){
        YHPerson per = (YHPerson) listPer.get(x);
        if(per.isAdmin()){
          per.setDeptId(0);
          orm.updateSingle(dbConn, per);
          continue;
        }else{
          wordlogic.deleteDepPerson(dbConn, deptent.getSeqId());
        }
      }
      wordlogic.deleteWord(dbConn, deptent.getSeqId());
    }
	    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
	    request.setAttribute(YHActionKeys.RET_MSRG, "成功删除数据库的数据");
    }catch(Exception ex){
	    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
	    request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
	    throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }	
  
  /**
   * 导出到EXCEL表格中
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportToExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
    response.setCharacterEncoding("GBK");
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      String fileName = URLEncoder.encode("主题词.csv", "UTF-8");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      YHSubjectTermLogic ieml = new YHSubjectTermLogic();
      ArrayList<YHDbRecord > dbL = ieml.toExportWordData(conn);
      YHCSVUtil.CVSWrite(response.getWriter(), dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
    }
    return null;
  }
  
  /**
   * 导入到EXCEL表格中
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String importWord(HttpServletRequest request,HttpServletResponse response) throws Exception{
    InputStream is = null;
    Connection conn = null;
    String data = null;
    int isCount = 0;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      is = fileForm.getInputStream();
      ArrayList<YHDbRecord> drl = YHCSVUtil.CVSReader(is,"gbk");
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      StringBuffer sb = new StringBuffer("[");
      YHSubjectTermLogic dl = new YHSubjectTermLogic();
      String word = "";
      String parentId = "";
      String sortNo="";
      String typeFlag="";
      String infoStr= "";
      String color = "red";
      int typeFlagNo=0;
      int deptParentNo = 0;
      Map map = new HashMap();
      String remark = "成功导入主题词：";
      boolean hasSucess = false;
      for(int i = 0; i < drl.size(); i++){
        word = (String) drl.get(i).getValueByName("主题词");
        if(YHUtility.isNullorEmpty(word)){
          continue;
        }
        word = getOutOf(word);
        parentId = getOutOf((String) drl.get(i).getValueByName("类别"));  
        sortNo = getOutOf((String) drl.get(i).getValueByName("排序号")); 
        typeFlag = getOutOf((String) drl.get(i).getValueByName("类型")); 
        if(YHUtility.isNullorEmpty(typeFlag)){
          typeFlag="主题词";
        }
        infoStr = "导入失败,主题词 " + word + " 已经存在";
        if(dl.existsWordName(conn, word)){
          color = "red";
          infoStr = "导入失败,主题词" + word + " 已经存在";
          sb.append("{");
          sb.append("word:\"" + (word == null ? "" : word)+ "\"");
          sb.append(",parentId:\"" + (parentId == null ? "" : parentId) + "\"");
          sb.append(",sortNo:\"" + (sortNo == null ? "0" : sortNo)+ "\"");
          sb.append(",typeFlag:\"" + (typeFlag == null ? "" : typeFlag)+ "\"");
          sb.append(",info:\"" + (infoStr == null ? "" : infoStr) + "\"");
          sb.append(",color:\"" + (color == null ? "" : color) + "\"");
          sb.append("},");
        }else{
          if(YHUtility.isNullorEmpty(parentId) && "主题词".equals(typeFlag)){
            color = "red";
            infoStr = "导入失败,主题词" + word + "的类别为空";
            sb.append("{");
            sb.append("word:\"" + (word == null ? "" : word)+ "\"");
            sb.append(",parentId:\"" + (parentId == null ? "" : parentId) + "\"");
            sb.append(",sortNo:\"" + (sortNo == null ? "0" : sortNo)+ "\"");
            sb.append(",typeFlag:\"" + (typeFlag == null ? "主题词" : typeFlag)+ "\"");
            sb.append(",info:\"" + (infoStr == null ? "" : infoStr) + "\"");
            sb.append(",color:\"" + (color == null ? "" : color) + "\"");
            sb.append("},");
          }else{
            isCount++;
            infoStr = "成功";
            color = "black";
            sb.append("{");
            sb.append("word:\"" + (word == null ? "" : word) + "\"");
            sb.append(",parentId:\"" + (parentId == null ? "" : parentId) + "\"");
            sb.append(",sortNo:\"" + (sortNo == null ? "0" : sortNo)+ "\"");
            sb.append(",typeFlag:\"" + (typeFlag == null ? "主题词" : typeFlag)+ "\"");
            sb.append(",info:\"" + (infoStr == null ? "" : infoStr) + "\"");
            sb.append(",color:\"" + (color == null ? "" : color) + "\"");
            sb.append("},");
            if(YHUtility.isNullorEmpty(parentId)){
              deptParentNo = 0;
            }else{
              deptParentNo = dl.getWordIdLogic(conn, parentId);
            }
            typeFlagNo=1;
            if("类别".equals(typeFlag)){
              typeFlagNo=0;
            }
            dl.saveWord(conn, word, deptParentNo,sortNo,typeFlagNo);
            remark += word + ",";
            hasSucess = true;
          }
        }
      }
      if (sb.charAt(sb.length() - 1) == ','){
        sb.deleteCharAt(sb.length() - 1);
      }
      sb.append("]");
      data = sb.toString();
      if (hasSucess) {
        YHSysLogLogic.addSysLog(conn, YHLogConst.ADD_DEPT, remark, person.getSeqId(), request.getRemoteAddr());
      }
      request.setAttribute("contentList", data);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    } 
    return "/subsys/inforesource/docmgr/docword/importword.jsp?data="+data+"&isCount="+isCount;
  }  
  public String getOutOf(String str) {
    if (str != null) {
      str = str.replace("'", "");
      str = str.replace("\"", "");
      str = str.replace("\\", "");
      str = str.replaceAll("\n", "");
      str = str.replaceAll("\r", "");
    }
    return str;
  }
  
  public String getAjaxCheck(HttpServletRequest request,HttpServletResponse response) throws Exception{
    Connection dbConn=null;
    try{
      YHRequestDbConn requestDbConn=(YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn=requestDbConn.getSysDbConn();
      String parentId=request.getParameter("parentId");
      String typeFlag = request.getParameter("typeFlag");
      String word = request.getParameter("word");
      YHSubjectTermLogic stl = new YHSubjectTermLogic();
      String data="";
      data=stl.getAjaxCheckLogic(dbConn,parentId,typeFlag,word);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSeqId(HttpServletRequest request,HttpServletResponse response) throws Exception{
    Connection dbConn=null;
    Statement stmt = null;
    ResultSet rs = null;
    String data="";
    try{
      YHRequestDbConn requestDbConn=(YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn=requestDbConn.getSysDbConn();
      String query="SELECT seq_id FROM oa_topic_term WHERE type_flag = 0 ORDER BY seq_id asc";
      stmt = dbConn.createStatement();
      rs=stmt.executeQuery(query);
      while(rs.next()){
        data = data + rs.getInt("seq_id")+",";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" +data+ "\"" );
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
