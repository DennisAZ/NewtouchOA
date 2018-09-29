package yh.core.module.dept_select.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.data.YHDepartment;
import yh.core.funcs.modulepriv.data.YHModulePriv;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;


public class YHDeptSelectAct
{
  private static Logger log = Logger.getLogger(YHDeptSelectAct.class);
/*
  public String getTree(HttpServletRequest request, 
  	HttpServletResponse response)throws Exception
  {
    String idStr = request.getParameter("id");
    int id = 0;
    if(idStr != null && !"".equals(idStr))
    {
      id = Integer.parseInt(idStr);
    }
    response.setCharacterEncoding("UTF-8");
    response.setContentType("text/xml");
    response.setHeader("Cache-Control", "no-cache");
    PrintWriter out = response.getWriter();
    out.print("<?xml version=\'1.0\' encoding=\'utf-8'?>");
    out.print("<menus>");
    Connection dbConn = null;
    try
    {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request
      	.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("deptId", id);
      List<YHPerson> list = orm.loadListSingle(dbConn, YHPerson.class, map);
      for(YHPerson d : list)
      {
        out.print("<menu>");
        out.print("<id>" + d.getSeqId() + "</id>");
        out.print("<name>" + d.getUserId() + "</name>");
        out.print("<parentId>" + d.getDeptId() + "</parentId>");
        out.print("<isHaveChild>0</isHaveChild>");
        out.print("</menu>");
      }
      map.remove("deptId");
      map.put("deptParent", id);
      List<YHDepartment> deptList = orm.loadListSingle(dbConn, YHDepartment.class, map);
      for(YHDepartment t : deptList)
      {
        out.print("<menu>");
        out.print("<id>" + t.getSeqId() + "</id>");
        out.print("<name>" + t.getDeptName() + "</name>");
        out.print("<parentId>" + t.getDeptParent() + "</parentId>");
        out.print("<isHaveChild>" + IsHaveChild(request, response, String.valueOf(t.getSeqId())) + "</isHaveChild>");
        out.print("</menu>");      
      }    
      out.print("<parentNodeId>" + id + "</parentNodeId>");
      out.print("<count>" + (list.size()+deptList.size()) + "</count>");
      out.print("</menus>");
      out.flush();
      out.close();
      //dbConn.close();
    }
    catch(Exception ex)
    {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
*/
  public int IsHaveChild(HttpServletRequest request,
      HttpServletResponse response, int id)throws Exception
  {
    Connection dbConn = null;
    try
    {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      Map map = new HashMap();
      map.put("DEPT_PARENT", id);
      List<YHDepartment> list = orm.loadListSingle(dbConn, YHDepartment.class, map);
      if(list.size() > 0)
      {
        return 1;
      }
      else
      {
        return 0;
      }
    }
    catch (Exception ex)
    {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
  
  public String getTree(HttpServletRequest request, 
  		HttpServletResponse response) throws Exception
  {
		String idStr = request.getParameter("DEPT_PAR_ID");
		int id = 0;
		if (idStr != null && !"".equals(idStr))
		{
		  id = Integer.parseInt(idStr);
		}
		Connection dbConn = null;
		try
		{
		  YHRequestDbConn requestDbConn = (YHRequestDbConn) request
		      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  dbConn = requestDbConn.getSysDbConn();
		  YHORM orm = new YHORM();
		  Map map = new HashMap();
		  map.put("DEPT_PARENT", id);
		  List<YHDepartment> list = orm.loadListSingle(dbConn, YHDepartment.class, map);
		  StringBuffer buf = new StringBuffer("[");
		  for (YHDepartment d : list)
		  {
		    int nodeId = d.getSeqId();
		    String name = d.getDeptName();
		    int isHaveChild = IsHaveChild(request, response, d.getSeqId());
		    String extData = "";
		    String imgAddress = "/yh/core/styles/style1/img/dtree/node_dept.gif";
		    buf.append("{");
		    buf.append("nodeId:\"" + nodeId + "\"");
		    buf.append(",name:\"" + name + "\"");
		    buf.append(",isHaveChild:" + isHaveChild + "");
		    buf.append(",extData:\"" + extData + "\"");
		    buf.append(",imgAddress:\"" + imgAddress + "\"");
		    buf.append("},");
		  }
		  buf.deleteCharAt(buf.length() - 1);
		  buf.append("]");
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		  request.setAttribute(YHActionKeys.RET_MSRG, "成功返回结果！");
		  request.setAttribute(YHActionKeys.RET_DATA, buf.toString());
		}
		catch (Exception ex)
		{
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		  request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
		  throw ex;
		}
		return "/core/inc/rtjson.jsp";
}

  public String selectDept(HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    response.setContentType("text/html;charset=UTF-8");
    request.setCharacterEncoding("UTF-8");
    String id = request.getParameter("DEPT_PAR_ID");
    String TO_ID = request.getParameter("TO_ID");
    String TO_NAME = request.getParameter("TO_NAME");
    String MODULE_ID = request.getParameter("MODULE_ID");
    String USER_SEQ_ID = request.getParameter("USER_SEQ_ID");
    String USER_DEPT = request.getParameter("USER_DEPT");
    String deptLocal = new String(request.getParameter("deptLocal").getBytes("ISO-8859-1"), "UTF-8");
    try
    {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();

      //System.out.println("datadebug>>>>>>>>>>>>>>>" + MODULE_ID + "==========" + USER_SEQ_ID);
      String[] query = {"MODULE_ID=" + MODULE_ID, "USER_SEQ_ID=" + USER_SEQ_ID};
      ArrayList<YHModulePriv> modulePriv = (ArrayList<YHModulePriv>)orm.loadListSingle(dbConn, YHModulePriv.class, query);
     	YHModulePriv tmp = (YHModulePriv)modulePriv.get(0);
     	String deptPriv = "0";//tmp.getDeptPriv();
     	//System.out.println("datadebug>>>>>>>>>>>>>>>" + tmp.getDeptPriv());
     	query[0] = "DEPT_ID=" + id;
     	if(deptPriv == null || deptPriv.equals("0"))
     	{
     		if(USER_DEPT.equals(id))
     		{
     			query[1] = "1=1";
     		}
     		else
     		{
       		query[1] = "1=0";
     		}
     	}
     	else if(deptPriv.equals("1"))
     	{
     		query[1] = "1=1";
     	}
     	else if(deptPriv.equals("2"))
     	{
     		query[1] = "DEPT_ID IN (" + tmp.getDeptId() + ")";
     	}
     	else
     	{
     		query[1] = "1=1";
     	}      
      ArrayList<YHPerson> personList = (ArrayList<YHPerson>)orm.loadListSingle(dbConn, YHPerson.class, query);
      request.setAttribute("PERSON_LIST", personList);
    }
    catch(Exception ex)
    {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "submit failed");
      throw ex;
    }
    return "/core/module/user_select/user.jsp?TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME + "&LOCAL=" + deptLocal;
  }
  public String getDept(HttpServletRequest request, 
  		HttpServletResponse response) throws Exception
  {
		String idStr = request.getParameter("DEPT_PAR_ID");
		int id = 0;
		if (idStr != null && !"".equals(idStr))
		{
		  id = Integer.parseInt(idStr);
		}
    String TO_ID = request.getParameter("TO_ID");
    String TO_NAME = request.getParameter("TO_NAME");
    String MODULE_ID = request.getParameter("MODULE_ID");
    String USER_SEQ_ID = request.getParameter("USER_SEQ_ID");
    String USER_DEPT = request.getParameter("USER_DEPT");
    try
    {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request
      	.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
      //System.out.println("datadebug>>>>>>>>>>>>>>>" + MODULE_ID + "==========" + USER_SEQ_ID);
      String[] query = {"MODULE_ID=" + MODULE_ID, "USER_SEQ_ID=" + USER_SEQ_ID};
      ArrayList<YHModulePriv> modulePriv = (ArrayList<YHModulePriv>)orm.loadListSingle(dbConn, YHModulePriv.class, query);
     	YHModulePriv tmp = (YHModulePriv)modulePriv.get(0);
     	String deptPriv = tmp.getDeptPriv();
     	//System.out.println("datadebug>>>>>>>>>>>>>>>" + tmp.getDeptPriv());
     	query[0] = "DEPT_PARENT=" + id;
     	if(deptPriv == null || deptPriv.equals("0"))
     	{
     		query[1] = "SEQ_ID=" + USER_DEPT;
     	}
     	else if(deptPriv.equals("1"))
     	{
     		query[1] = "1=1";
     	}
     	else if(deptPriv.equals("2"))
     	{
     		query[1] = "SEQ_ID IN (" + tmp.getDeptId() + ")";
     	}
     	else
     	{
     		query[1] = "1=1";
     	}
      ArrayList<YHDepartment> deptList = (ArrayList<YHDepartment>)orm.loadListSingle(dbConn, YHDepartment.class, query);
      request.setAttribute("DEPT_LIST", deptList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取出数据");
    }
    catch(Exception ex)
    {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/module/dept_select/dept_list.jsp?TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME;
  }

}
