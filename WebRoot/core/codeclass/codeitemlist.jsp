<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");//codeClass的SEQId，后台传过来
  if(seqId == null){
    seqId = "";
  }
  String classNo = request.getParameter("classNo");
  String classDesc = (String)request.getAttribute("classDesc"); 
  //out.print(classDesc+":"+classNo);
%>
<%@ page import="java.util.*,yh.core.codeclass.data.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列出下一级</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>

<script>
function delCodeItem(itemId) {
  var url = "<%=contextPath %>/yh/core/codeclass/act/YHCodeClassAct/deleteCodeItem.act";
  var rtJson = getJsonRs(url, "sqlId=" + itemId);
  if (rtJson.rtState == "0") {
    //alert(rtJson.rtMsrg);
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
<body >
	<div>
	<img src="<%=contextPath %>/core/styles/imgs/green_arrow.gif"></img>
	<font size="3">代码项设置</font>
	</div>
 <br>
    <table class="TableBlock" align="center" width="300">  
    <tr class="TableHeader">
      <th colspan="4"><%=classNo %>:<%=classDesc %></th>
    </tr>
    <tr class="TableData">
      <td align="center" colspan="4" align="left">
      <a class="ToolBtn2" href="<%=contextPath %>/core/codeclass/codeiteminput.jsp?classNo=<%=classNo %>&codeClassId=<%=seqId %>" target="contentFrame">
        <span>
        <img align="absmiddle" src="<%=contextPath %>/core/styles/imgs/green_plus.gif"></img>
		        添加代码项
        </span>
      </a>   
      </td>
    </tr>
    <%
      List<YHCodeItem> codeList = null;
      codeList = (List<YHCodeItem>)request.getAttribute("codeList");
      for(int i = 0; i < codeList.size(); i++) {
        YHCodeItem code = codeList.get(i);
    %>	
	<tr class="<%=((i % 2 == 0) ? "TableLine1" : "TableLine2") %>">
	  <td><%=code.getClassCode() %></td>
	  <td><%=code.getClassDesc() %></td>
	  
	  <td>
		<a href="<%=contextPath %>/core/codeclass/codeiteminput.jsp?sqlId=<%=code.getSqlId() %>&classNo=<%=classNo %>&codeClassId=<%=seqId %>" >编辑</a>
	  </td>
	  <td>
	    <a href="javascript:delCodeItem('<%=code.getSqlId() %>');" onclick="return confirmDel()">删除</a>
	  </td>
	</tr>
    <%
      }
    %>
    </table>
</body>
</html>