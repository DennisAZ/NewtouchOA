<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.core.funcs.person.data.YHPerson"%>
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
<%
  YHPerson user = (YHPerson)request.getAttribute("user");
%>
<script type="text/javascript">
	function goHome(){
		window. parent.location.href = "<%=contextPath%>/yh/core/oaknow/act/YHOAKnowAct/OAKnowIndex.act";
	}
</script>
</head>
<body class="mbodycolor" topmargin="5">	

<div class="mb">
   <div class="menubg">
   	 <a href="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/leftPanel.act" target="mainpanel">我的问题</a>
   </div> 
   <div class="menubg">
   	 <a href="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/findMyReferenceAsks.act" target="mainpanel">我的参与</a>
   </div> 
   <%
   	if(user.isAdminRole() || "admin".equals(user.getUserId())){  //oa管理员
   	 %>
   	 	 <div class="menubg">
   	      <a href="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowManageAct/gotoManage.act?ask=&startTime=&endTime=&status" target="mainpanel">知道管理</a>
       </div>   
       <div class="menubg">
   	      <a href="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowInputAct/oaInput.act" target="mainpanel">知道录入</a>
        </div> 
   	 <%    	  
   	}
   %>      
  
   <%
   	  if("admin".equals(user.getUserId())){
   	    %>
   	    <div class="menubg">
   	      <a href="<%=contextPath%>/yh/core/oaknow/act/YHCategoriesAct/goToCategoty.act?seqId=" target="mainpanel">知道分类</a>
        </div>
		   <div class="menubg">
		   	 <a href='<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/userManage.act?userKey=' target="mainpanel">用户管理</a>
		   </div>
		   <div class="menubg">
		   	 <a href="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/findOAName.act" target="mainpanel">系统设置</a>
		   </div>   
   	    <%
   	  }
   %>
   <div class="menubg">
   	 <a href="javascript:goHome();">知道首页</a>
   </div>  
</div>	
</body>
</html>

