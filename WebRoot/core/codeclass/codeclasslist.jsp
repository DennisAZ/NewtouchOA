<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.*,yh.core.codeclass.data.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示CODE_CLASS的信息</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script>
function delCodeClass(itemId) {
  var url = "<%=contextPath %>/yh/core/codeclass/act/YHCodeClassAct/deleteCodeClass.act";
  var rtJson = getJsonRs(url, "sqlId=" + itemId );
  if (rtJson.rtState == "0") {
    window.location.reload();
    parent.contentFrame.location.href = "<%=contextPath %>/core/codeclass/codeiteminput.jsp";
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
<body>
    <div>
    <img src="<%=contextPath %>/core/styles/imgs/green_arrow.gif"></img>
    <font size="3">代码主分类设置</font>
    </div>
    <br>
    <div style="width: 100%; text-align: center;">
    <a class="ToolBtn2" href="<%=contextPath %>/core/codeclass/codeclassinput.jsp" target="contentFrame">
      <span><img align="absmiddle" src="<%=contextPath %>/core/styles/imgs/green_plus.gif"></img>添加代码主分类</span>
    </a>
    </div>
    <br>
    <table class="TableBlock" align="center" width="100%">
    <tr class="TableHeader">
<%-- 
    	<th nowrap="nowrap">编码</th>
--%>
    	<td nowrap="nowrap">描述</td>
    	<td nowrap="nowrap">编辑</td>
    	<td nowrap="nowrap">下一级</td>
    	<td nowrap="nowrap">删除</td>
    </tr>
    <%
      ArrayList<YHCodeClass> codeClassList = null;
      codeClassList = (ArrayList<YHCodeClass>)request.getAttribute("codeClassList");
      for(int i = 0; i < codeClassList.size(); i++) {
        YHCodeClass code = codeClassList.get(i);
        String classNo = code.getClassNo() ;
        String classDesc = code.getClassDesc();
        if (classDesc.length() > 8) {
          classDesc = classDesc.substring(0, 8);
        }
    %>	
	<tr class="<%=((i % 2 == 0) ? "TableLine1" : "TableLine2") %>">
 <%-- 
	  <td width="80px;" style="word-break : break-all; "><%=code.getClassNo() %></td>
 --%>
	  <td ><%=classDesc %></td>	  
	  <td>	
		<a href="<%=contextPath %>/core/codeclass/codeclassinput.jsp?sqlId=<%=code.getSqlId()%>"  target="contentFrame">编辑</a> 
	  </td>
	  <td>	   
        <a href="<%=contextPath %>/yh/core/codeclass/act/YHCodeClassAct/listCodeItem.act?seqId=<%=code.getSqlId() %>"  target="contentFrame">下一级</a>
	  </td>
	  <td>
	    <%
	    String key = code.getClassLevel();
	    if(key != null && "1".equals(key)) { %>
	   	    <a href="javascript:delCodeClass('<%=code.getSqlId() %>');" onclick="return confirmDel()">删除</a>	
	    <%} %>
	  </td>
	</tr>
    <%
      }
    %>
    </table>
  </body>
</html>