package yh.core.module.priv_select.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.modulepriv.data.YHModulePriv;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHActionKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHORM;

public class YHPrivSelectAct
{
	private static Logger log = Logger.getLogger(YHPrivSelectAct.class);
	
	public String getPriv(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
    String TO_ID = request.getParameter("TO_ID");
    String TO_NAME = request.getParameter("TO_NAME");
    String MODULE_ID = request.getParameter("MODULE_ID");
    String USER_SEQ_ID = request.getParameter("USER_SEQ_ID");
    String USER_PRIV = request.getParameter("USER_PRIV");
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
     	String deptPriv = tmp.getRolePriv();
     	//System.out.println("datadebug>>>>>>>>>>>>>>>" + tmp.getDeptPriv());
     	if(deptPriv.equals("0"))
     	{
     		query[0] = "PRIV_NO>" + USER_PRIV;
     	}
     	else if(deptPriv.equals("1"))
     	{
     		query[0] = "PRIV_NO>=" + USER_PRIV;
     	}
     	else if(deptPriv.equals("2"))
     	{
     		query[0] = "1=1";
     	}
     	else
     	{
     		query[0] = "PRIV_NO IN (" + tmp.getPrivId() + ")";
     	}
     	query[1] = "1=1";
      ArrayList<YHUserPriv> userPrivList = (ArrayList<YHUserPriv>)orm.loadListSingle(dbConn, YHUserPriv.class, query);
			request.setAttribute("USER_PRIV", userPrivList);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
		}
		catch(Exception ex)
		{
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询失败");
			throw ex;
		}
		return "/core/module/priv_select/index.jsp?TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME;
	}
	public String getUserPriv(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
    String TO_ID = request.getParameter("TO_ID");
    String TO_NAME = request.getParameter("TO_NAME");
		try
		{
			YHRequestDbConn requestDbConn = (YHRequestDbConn)request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			Connection dbConn = requestDbConn.getSysDbConn();
			YHORM orm = new YHORM();
			ArrayList<YHUserPriv> userPrivList = (ArrayList<YHUserPriv>)orm
				.loadListSingle(dbConn, YHUserPriv.class, new HashMap());
      StringBuffer data = new StringBuffer("[");
      Iterator item = userPrivList.iterator();
      while(item.hasNext())
      {
      	YHUserPriv userPriv = (YHUserPriv)item.next();
        data.append("{");
        data.append("privName:\"" + userPriv.getPrivName() + "\"");
        //System.out.println(">>>>>>>>>>>>>+++++++++>>>>>>>>>>>" + userPriv.getPrivName());
        data.append(",privNo:\"" + userPriv.getPrivNo() + "\"");
        data.append("},");
      }
      if(data.lastIndexOf(",") == (data.length() - 1))
      {
      	data.deleteCharAt(data.length() - 1);
      }
      data.append("]");
      //System.out.println(data.toString());
			request.setAttribute(YHActionKeys.RET_DATA, data.toString());
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
		}
		catch(Exception ex)
		{
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询失败");
			throw ex;
		}
		return "/core/inc/rtjson.jsp";
	}
	public String selectPriv(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		String PRIV_ID = request.getParameter("PRIV_ID");
    String TO_ID = request.getParameter("TO_ID");
    String TO_NAME = request.getParameter("TO_NAME");
    String MODULE_ID = request.getParameter("MODULE_ID");
    String USER_SEQ_ID = request.getParameter("USER_SEQ_ID");
    String USER_PRIV = request.getParameter("USER_PRIV");
    String PRIV_NAME = new String(request.getParameter("PRIV_NAME").getBytes("ISO-8859-1"), "UTF-8");
		if(PRIV_ID.equals("4"))
		{
			PRIV_ID = "121";
		}
		else if(PRIV_ID.equals("5"))
		{
			PRIV_ID = "123";
		}
		else if(PRIV_ID.equals("2"))
		{
			PRIV_ID = "72";
		}
		else
		{
			PRIV_ID = "97";
		}
		try
		{
			YHRequestDbConn requestDbConn = (YHRequestDbConn)request
				.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			Connection dbConn = requestDbConn.getSysDbConn();
			YHORM orm = new YHORM();

      //System.out.println("new1debug>>>>>>>>>>>>>>>" + MODULE_ID + "==========" + USER_SEQ_ID);
      String[] query = {"MODULE_ID=" + MODULE_ID, "USER_SEQ_ID=" + USER_SEQ_ID};
      ArrayList<YHModulePriv> modulePriv = (ArrayList<YHModulePriv>)orm.loadListSingle(dbConn, YHModulePriv.class, query);
     	YHModulePriv tmp = (YHModulePriv)modulePriv.get(0);
     	String rolePriv = "0";//tmp.getRolePriv();
     	//System.out.println("new1debug>>>>>>>>>>>>>>>" + rolePriv);
     	USER_PRIV = "100";
     	if(rolePriv.equals("0"))
     	{
     		query[0] = "USER_PRIV<" + USER_PRIV;
     	}
     	else if(rolePriv.equals("1"))
     	{
     		query[0] = "USER_PRIV<=" + USER_PRIV;
     	}
     	else if(rolePriv.equals("2"))
     	{
     		query[0] = "1=1";
     	}
     	else
     	{
     		query[0] = "USER_PRIV IN (" + tmp.getPrivId() + ")";
     	}
     	query[1] = "USER_PRIV=" + PRIV_ID;

			ArrayList<YHPerson> personList = (ArrayList<YHPerson>)orm
				.loadListSingle(dbConn, YHPerson.class, query);
			request.setAttribute("PERSON_LIST", personList);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "查询成功");
		}
		catch(Exception ex)
		{
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, "查询失败");
			throw ex;
		}
		return "/core/module/user_select/user.jsp?TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME + "&LOCAL=" + PRIV_NAME;
	}
}
