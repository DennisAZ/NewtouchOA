<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.subsys.oa.book.data.*"%>
<html>
<head>
<title>图书类别编辑</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
 <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript">
/** 
 * 替换s1为s2 
 */ 
 String.prototype.replaceAll = function(s1,s2){ 
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
	function CheckForm(){
	// var typeName = $("TYPE_NAME").value;
	 var typeName = document.getElementById("TYPE_NAME").value;
	 var typeId = document.getElementById("TYPE_ID").value;
	 if(typeName.replaceAll(" ","") != "" && typeName !="null"){
	   
	   $("form1").action = contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/updateBookType.act?typeName="+typeName+"&&typeId="+typeId;
	   document.form1.submit();
	   return true;
	  }else{
	     alert("图书类别名称不能为空");
	     return false;
	  }
	}
	function gotoIndex(){
    window.location.href =contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/findBookType.act";
	}
</script>
  <%
  YHBookType bookType = (YHBookType)request.getAttribute("bookType");
  
  %>
</head>
<body class="bodycolor" topmargin="5" onload="">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20"><span class="big3"> 图书类别编辑</span>
    </td>
  </tr>
</table>
<form action=""  method="post" name="form1" id="form1">
 <table class="TableBlock"  width="450" align="center" >
  
   <tr>
	    <td nowrap class="TableData">类别名称：<font color="red">*</font></td>
	    <td nowrap class="TableData">
	        <input type="text" name="TYPE_NAME" id="TYPE_NAME" class="BigInput" size="25" maxlength="100" value="<%=bookType.getTypeName()%>">&nbsp;
	    </td>
   </tr>
    <tr>
	    <td nowrap  class="TableControl" colspan="2" align="center">
	        <input type="hidden" value="<%=bookType.getSeqId() %>" name="TYPE_ID" id ="TYPE_ID">
	        <input type="button" value="确定" onclick="CheckForm()" class="BigButton">&nbsp;&nbsp;
	        <input type="button" value="返回" class="BigButton" onclick="javascript:gotoIndex();return false;">
	    </td>
	   </tr>
   
 </table>
 </form>
</body>
</html>