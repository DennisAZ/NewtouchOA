<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.data.YHRequestDbConn" %>
<%@ page import="yh.core.global.YHBeanKeys"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.core.util.db.YHORM" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="javax.servlet.http.Cookie" %>
<%
 
 /*  String Cookie_Str=request.getParameter("cs");
  Cookie cookie = new Cookie("userName","");
  response.addCookie(cookie);
 */
  String url=request.getParameter("url");
  String uid=request.getParameter("uid");
if(!"".equals(url)){
  YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  if(person==null){
  person =new YHPerson();
  if( null==uid || uid.length()<1) {
    person.setUserName("aa");
  }else{
    YHORM orm = new YHORM();
    YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
    Connection dbConn = requestDbConn.getSysDbConn();
    person=(YHPerson)orm.loadObjSingle(dbConn, YHPerson.class, Integer.parseInt(uid));
      
  }
  request.getSession().setAttribute(YHConst.LOGIN_USER,person);
 }
}

%>

<script type="text/javascript">
var url='<%=url%>';
location.href=url;
</script>
