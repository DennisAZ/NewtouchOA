<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.CallableStatement"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="yh.core.global.YHBeanKeys"%>
<%@page import="yh.core.util.YHUtility"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@page import="yh.core.util.db.YHDBUtility"%>
<%@page import="yh.core.data.YHRequestDbConn"%>
<%@page import="yh.core.util.file.YHFileUtility"%>
<%@page import="java.sql.ResultSetMetaData"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>执行查询</title>
<script type="text/javascript">
function exeSelect() {
  if (!document.getElementById("sql")) {
    alert("请输入Sql语句!");
    document.getElementById("sql").focus();
    return;
  }
  document.getElementById("form1").submit();
}
</script>
</head>
<body>
<form name="form1" id="form1" action="selectex.jsp" method="post" target="output">
<input type="hidden" name="action" value="exeSql">
<textarea name="sql"  id="sql" rows="10" cols="40"></textarea>
<input onclick="exeSelect();" type="button" name="btnExeFuncs" value="查询"></input>
</form>
<iframe name="output" id="output" width="100%" height="400px"></iframe>
</body>
</html>