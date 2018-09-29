<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.*,raw.ljf.curd.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列出下一级</title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script>
/**
 * 删除代码项目
 */
function delItem(itemId) {
  var url = "/yh/raw/ljf/curd/YHDeleteCodeItemAct/deleteCodeItem.act";
  var rtJson = getJsonRs(url, "sqlId=" + itemId);
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
<body bgcolor="#89a3e4" text="#ff0000">
    <%
      String classNo = request.getParameter("classNo");
    %>
   
    <table border="1">  
    <tr>
      <th colspan="4"><%=classNo %></th>
    </tr>
    <tr>
      <th colspan="4">
      <a href="/yh/raw/ljf/html/addcodeitem.jsp?classNo=<%=classNo %>" target="contentFrame">添加CODE_ITEM</a>   
      </th>
    </tr>

    <%
      List<YHCodeItem> codeList = null;
      codeList = (List<YHCodeItem>)request.getAttribute("codeList");
      for(Iterator it = codeList.iterator(); it.hasNext();) {
        YHCodeItem code = (YHCodeItem)it.next();
    %>	
	<tr>
	  <td><%=code.getClassCode() %></td>
	  <td><%=code.getClassDesc() %></td>
	  
	  <td>
		<a href="/yh/raw/ljf/curd/YHSelectCodeItemAct/selectCodeItem.act?sqlId=<%=code.getSqlId() %>" target="contentFrame">编辑</a>
	  </td>
	  <td>
	    <a href="javascript:delItem('<%=code.getSqlId() %>');"  target="contentFrame" onclick="return confirmDel()">删除</a>
	  </td>
	</tr>
    <%
      }
    %>
    </table>
</body>
</html>