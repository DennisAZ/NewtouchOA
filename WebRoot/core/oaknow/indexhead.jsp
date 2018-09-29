<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String contextPath5 = request.getContextPath();
%>
<!--search begin  -->
<script type="text/javascript">
 function gotoIndex(){
  window.location.href = contextPath + "/yh/core/oaknow/act/YHOAKnowAct/OAKnowIndex.act";
 }
 function checkVal(){
	var val = $("textValue").value;
	if(val == ""){ 
		alert("搜索内容不能为空！");
		document.getElementById("textValue").focus();
		return;
	}else{
		document.getElementById("form1").submit();
	}
 }
</script>
	 <form name="form1" id="form1" action='<%=contextPath5%>/yh/core/oaknow/act/YHOASeachAct/findResolveStatus.act' method='post'>
	 <div class="searchcss">
	 	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:14px;">
    <tr>
	      <td><a onclick="gotoIndex();" class="logo">${oaName}</a>
	      	<input id="textValue" type="text" onmouseover="this.focus()" onfocus="this.select()" name="question" class="askincss2" size="35" value="${askName}"/>
	 	      <input type="button" onclick="checkVal();"  value="搜 索"  class="BigButton" />
	 	      <input type="button" value="提 问" onclick=location.href="<%=contextPath5%>/core/oaknow/ask.jsp" class="BigButton" />
                                     &nbsp;&nbsp;<a href="<%=contextPath5%>/yh/core/oaknow/act/YHOAKnowPanelAct/mainPanel.act">管理面板</a>
                                     &nbsp;&nbsp;&nbsp;&nbsp;访问计数：${clock}
          <input type="hidden" value="" name="currNo" id="currNo"/>
          <input type="hidden" value="resolve" name="flag" id="flag"/>
	     </td>
	  </tr>
	  </table>
	 </DIV>
	</form>
	<!--search end  -->