<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="yh.core.oaknow.data.*"%>
<%@ page  import="yh.core.funcs.person.data.YHPerson"%>
<html> 
<head> 
<%
YHPerson person = (YHPerson)request.getAttribute("user");
%>
<title>修改用户信息</title> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>

<script type="text/javascript">
	function change(){
	  var queryParam = $("form1").serialize();
	  var zhi = $('score').value;
	  if(!isNumber(zhi)){
			alert("输入的不是数字，请重新输入！");
			document.getElementById("score").focus();
			$("score").select();
			return false;
		}
	  var rtJson = getJsonRs("<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/updatePerson.act", queryParam);
	  if(rtJson.rtState == "1"){  //回答失败
	    alert("修改失败！");
	    return;
	  }else{
	    alert("修改成功");     //回答成功
	    //window.opener.location.reload();
	    window.opener.document.getElementById("form1").submit();
	   }
	}
</script>
</head> 
<body topmargin="5"> 
	<div class="gt">修改用户信息</div> 
  <div style="text-align:center;"> 
  	  <form id="form1" name="form1" action="" method='post'> 
     	<TABLE class="atb atb2" width="92%"> 
        	<TR> 
        		<TD>姓名：</TD> 
        		<TD><input type="text" name="username" id="username" class="upcss1" value="${user.userName}" readonly style="background:#E0E0E0;border: 1px solid #C0BBB4;" /></TD> 
       		</TR> 
        	<TR> 
        		<TD>积分：</TD> 
        		<TD><input type="text" name="score" id="score" class="upcss1" value="${user.score}" /></TD> 
       		</TR> 
       		<%if(!person.isAdmin()){ %>
        	<TR> 
        		<TD>用户类型：</TD> 
        		<TD> 
        		<%
        			if(person.isAdminRole()){
        			  %>
        			  <input type="radio" name="tderflag" value="1" checked>管理员
        			  <input type="radio" name="tderflag" value="0">普通用户
        			  <%
        			}else{
        			  %>
        			  <input type="radio" name="tderflag" value="1" >管理员
        			  <input type="radio" name="tderflag" value="0" checked>普通用户
        			  <% 
        			}       		
        		%>
        			
        		</TD> 
       		</TR> 
       		<%}else{
       		  %>
       		  <input type="hidden" name="tderflag" value="1" />
       		<%}%>
       		
      </TABLE> 
      <div class="opbar" style="width:92%">
      	<input type="hidden" name="userId" value="${user.seqId}"> 
      	<input type="button" class="BigButton" value="提交" onclick="change();"/>&nbsp;&nbsp;&nbsp;&nbsp;
      	<input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();"> 
      </div> 
      </form> 
  </div> 
<br><br><div align=center style='font-size:9pt;color:gray'></div></body> 
</html>