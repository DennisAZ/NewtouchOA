<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.*,raw.ljf.curd.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示页面</title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script>
function selectCode(itemId) {
  var url = "/yh/raw/ljf/curd/YHSelectCodeAct/selectCode.act";
  var rtJson = getJsonRs(url, "sqlId=" + itemId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg);
  }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
function getItem(itemNo) {
  var url = "/yh/raw/ljf/curd/YHSonTableAct/getSonTable.act";
  var rtJson = getJsonRs(url, "classNo=" + itemNo);
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg);
  }
}

function confirmDel() {
  if(confirm("确认删除！")) {
    return true;
  }else {
    return false;
  }
}
</script>
</head>
  <body bgcolor="#89a3e4" text="#ff00">
    <a href="/yh/raw/ljf/html/add.jsp" target="contentFrame">添加CODE_CLASS</a>
    <table border="1">
    <%
      List<YHCodeClass> codeList = null;
      codeList = (List<YHCodeClass>)request.getAttribute("codeList");
      for(Iterator it = codeList.iterator(); it.hasNext();) {
        YHCodeClass code = (YHCodeClass)it.next();
    %>	
	<tr>
	  <td><%=code.getClassNo() %></td>
	  <td><%=code.getClassDesc() %></td>
	  
	  <td>
	
		<a href="/yh/raw/ljf/curd/YHSelectCodeAct/selectCode.act?sqlId=<%=code.getSqlId() %>"  target="contentFrame">编辑</a> 
	  </td>
	  <td>
	   
        <a href="/yh/raw/ljf/curd/YHSonTableAct/getSonTable.act?classNo=<%=code.getClassNo() %>"  target="contentFrame">下一级</a>

	  </td>
	  <td>
	    <%
	    String key = code.getClassLevel();
	    int i = Integer.parseInt(key);
	    if( i== 1) { %>
		    <a href="/yh/raw/ljf/curd/YHDeleteCodeAct/deleteCode.act?sqlId=<%=code.getSqlId() %>&classNo=<%=code.getClassNo() %>" onclick="return confirmDel()">删除</a>
	   	    	
	    <%} %>
	  </td>
	</tr>
    <%
      }
    %>
    </table>
  </body>
</html>