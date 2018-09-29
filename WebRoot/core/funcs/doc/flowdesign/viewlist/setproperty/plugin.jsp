<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
    <%@ include file="/core/inc/header.jsp" %>
    <%@ page import="java.util.List,yh.core.funcs.doc.data.YHPlugin,yh.core.funcs.doc.util.YHWorkFlowUtility" %>
    <%
    List<YHPlugin> list = (List<YHPlugin>)request.getAttribute("files");
    %>
<html>
	<head>
	<title>选择流程插件</title>	
  <link rel="stylesheet" href = "<%=cssPath %>/style.css">
    <script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
  <script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
  <script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
  <script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
	<script type="text/javascript">
		function hilight(el)
		{
			if(el.style.backgroundColor=="#dbf1d4" ||
			 /* firefox获取的样式表背景颜色值 */
			    el.style.backgroundColor=="rgb(219, 241, 212)")
			  el.style.backgroundColor="#ffffff";
			else
				el.style.backgroundColor="#dbf1d4";
	  }
	  function filldata(event)
	  {
	  	var obj=event.srcElement? event.srcElement.parentElement : event.target.parentNode;
	  	var parentWindowObj = window.dialogArguments;
	  	parentWindowObj.$('plugin').value = obj.cells[0].innerHTML;
	  	window.close();
	  }
	</script>
	</head>
<body topmargin="5" leftmargin="5">
<%
if (list.size() >0 ) {
%>
<table border="1" class="TableBlock">
	<tr class="TableHeader">
		<td width="80">文件名</td><td width="120">插件名称</td><td width="120">流程说明</td>
	</tr>
<%
for (YHPlugin pl : list) {
%>
  <tr onmouseover="javascript:hilight(this)" onmouseout="javascript:hilight(this)" onclick="javascript:filldata(event)">
    <td><%=pl.getPluginFile() %></td>

    <td><%=pl.getPluginName() %></td>
    <td><%=pl.getPluginDesc() %></td></tr>
<%} %>
</table>
<% } else {

  String message = YHWorkFlowUtility.Message("没有定义插件！", 0);
  out.print(message);
}%>
</body>
</html>