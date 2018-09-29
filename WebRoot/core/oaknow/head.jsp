<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String contextPath4 = request.getContextPath();
%>
<!--search begin  -->
<script type="text/javascript">
function gotoIndex(){
  window.location.href = "<%=contextPath4%>/yh/core/oaknow/act/YHOAKnowAct/OAKnowIndex.act";
 }
function checkVal(){
 	var val = $("textValue").value;
 	if(val == ""|| val==null){ 
 		alert("搜索内容不能为空！");
 		document.getElementById("textValue").focus();
 		return;
 	}else{
 		document.getElementById("form1").submit();
 	}
  }
</script>
	 <form name="form1" id="form1" action='<%=contextPath4%>/yh/core/oaknow/act/YHOASeachAct/findResolveStatus.act' method='post'>
	 <div class="searchcss">
	 	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:14px;">
    <tr>
	      <td><a onclick="gotoIndex();" class="logo">OA知道</a>
	      	<input type="text" id="textValue" onmouseover="this.focus()" onfocus="this.select()" name="question" size="35" class="askincss2" />	 	      
	 	      <input type="button" onclick="checkVal();" value="搜 索"  class="BigButton" />
	 	      &nbsp;&nbsp;<a href="<%=contextPath4%>/yh/core/oaknow/act/YHOAKnowPanelAct/mainPanel.act">管理面板</a>
	 	       <input type="hidden" value="" name="currNo" id="currNo"/>
          <input type="hidden" value="resolve" name="flag" id="flag"/>
	      </td>
	  </tr>
	  </table>
	 </div>
	</form>
	<!--search end  -->