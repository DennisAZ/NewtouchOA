<%@ page import="java.sql.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.text.*"%>
<%@ page import="yh.core.data.YHRequestDbConn"%>
<%@ page import="yh.core.global.YHBeanKeys"%>
<%
YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);

Connection conn=requestDbConn.getSysDbConn();
 conn.setAutoCommit(true);
%>