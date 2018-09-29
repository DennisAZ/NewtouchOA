<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.core.oaknow.data.*"%>
<html>
<head>
<title>OA知道</title>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript">
	function checkValue(){
		var va = document.getElementById("questions").value;
		if(!va || !va.trim()){
				alert("提问问题不能为空！");
				return false;
		}else{
			document.getElementById("form2").submit();
		}
	}
</script>
</head>
<body  topmargin="5">
<div class="bodydiv">
	<br /><div class="askbody">
	 <%@ include file="head.jsp" %> 	
  <div class="navbar">
  	<a href="javascript:void(0);" onclick="gotoIndex();">OA知道</a>&nbsp;&raquo;&nbsp;提问
  </div>
  <div class="RoundedCorner">
  <b class="rtop"><b class="r1"></b><b class="r2"></b><b class="r3"></b><b class="r4"></b></b>
  &nbsp;&nbsp;提问
  <b class="rbottom"><b class="r4"></b><b class="r3"></b><b class="r2"></b><b class="r1"></b></b>
  </div>
  <br /> <br /> <br />

  <div class="ascss">
  	<form name="form2" id="form2" action='<%=contextPath %>/yh/core/oaknow/act/YHOAKnowAct/askQuestion.act' method='post'>
  	   请输入您的问题：<input type="text" name="questions" size="30" class="BigInput" id="questions" value="" />
  	   <input type="button"  onclick="checkValue();" value="下一步" class="BigButton" />
    </form>
  </div>
</div></div>
<input type="hidden" name="REMINDACT" id="REMINDACT" value="" />
<div id="overlay"></div>
<div id="p" class="loginbox" style="width:402px;height:250;"></div>
</body>
</html>