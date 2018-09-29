<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.subsys.oa.book.data.*"%>
<%@ page  import="java.util.List"%>
<html>
<head>
<title>图书类别管理</title>
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
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"><!--
/** 
 * 替换s1为s2 
 */ 
 String.prototype.replaceAll = function(s1,s2){ 
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
function CheckForm(){
  var typeName = $("typeName").value;
  if(typeName.replaceAll(" ","")!="" && typeName != "null"){
	  $("form1").action = contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/addBookType.act";
	  document.form1.submit();
	  return true;
  }else{
    alert("图书类型不能为空");
    return false;
  }
}
function doClick(delBookId){
  var msg ="确定要删除该类别吗？";
  if(!window.confirm(msg)){
  }else{
   // var bookTypeId = $("bookTypeId").value;
    if(delBookId!="" && delBookId!="null"){
      $("form1").action = contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/deleteBookType.act?bookId="+delBookId;  
      document.form1.submit(); 
      return true;
    }
  }
}
function doInit(){
  //$("form1").action = contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/findBookType.act";
  //document.form1.submit();
  //return true;
 // /yh/subsys/oa/book/act/YHBookTypeAct/findBookType.act
}
--></script>
  <% 
     List<YHBookType> booktype = (List<YHBookType>)request.getAttribute("booktype");
  %>
</head>
<body class="bodycolor" topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 添加图书类别</span>
    </td>
  </tr>
</table>
<form action=""  method="post" name="form1" id="form1" enctype="multipart/form-data">
<table class="TableBlock"  width="400"  align="center" >
  
   <tr>
     <td nowrap class="TableData">类别名称：<font color="red">*</font></td>
     <td nowrap class="TableData">
        <input type="text" name="typeName" id="typeName" class="BigInput" size="25" maxlength="100">&nbsp;
        <input type="hidden" name="typeId" id="typeId" class="BigInput" size="25" maxlength="100">
     </td>
   </tr>
   <tr>
     <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" onclick="CheckForm();" value="添加" class="BigButton" title="添加类别">
     </td>
   </tr>
 
</table>
 </form>
<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
<table border="0" width="100%" cellspacing="0" cellpadding="3" >
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="absmiddle"><span class="big3"> 图书类别管理</span>
    </td>
  </tr>
</table>
<br>
<div align="center">
  <table  width="400"  class="TableList">
   <thead class="TableHeader">
     <tr>
      <td nowrap align="center">图书类别</td>
      <td nowrap align="center">操作</td>
      </tr>
   </thead>
   <%
     if(booktype!=null && booktype.size()>0){
       for(int i = 0; i < booktype.size(); i++){
   %>
    <tr class="TableLine1">
		    <td align="center"><%=booktype.get(i).getTypeName()%></td>
		    <td nowrap align="center" width="80">
	      <a href="<%=contextPath%>/yh/subsys/oa/book/act/YHBookTypeAct/editBookType.act?bookId=<%=booktype.get(i).getSeqId()%>"> 编辑</a>
	  <a href="javascript:void(0);" onclick="doClick(<%=booktype.get(i).getSeqId() %>);">删除</a>
	     </tr>   
    <%   
       }
     }
   %> 
   
  </table>
</div>
</body>
</html>