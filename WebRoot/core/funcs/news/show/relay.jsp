<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  String parentId = request.getParameter("parentId");
  String anonymityYnstr = request.getParameter("anonymityYn");
  if (parentId == null) {
    parentId = "";
  } 
  String newsId = request.getParameter("newsId");
  if (newsId == null) {
    newsId = "";
  }
 
  int anonymityYn  = 0;  
  if(anonymityYnstr != null){
    anonymityYn = Integer.parseInt(anonymityYnstr);  
  }
  
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
  String userName = loginUser.getUserName();
  if (userName == null) {
    userName = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>回复评论</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>

<script type="text/javascript">
var anonymityYn = <%=anonymityYn%>;
 function validate() {
   var content = document.getElementById('content').value;
   if(content==""){
      alert("评论内容不能为空！")
      return;
   }
   if(anonymityYn ==1){ //如果是匿名评论，判断匿名是否为空，如果为空，则返回
     var isNull = $('nickName').value;
     
     if(isNull== null || isNull=="" || isNull.trim()==""){
       if($('authorName').checked){
         $("form1").submit();
         return;
       }else{
 	      alert("匿名名称不能为空！");
 	      $('nickName').focus();
 	      return;
       }
       return (false);
     }else{
 			$("form1").submit();
 			return ;
     }
    }else if(anonymityYn =='0'){
      $("form1").submit();
    }
 }

 function addComment() {
	  //$("form1").action = contextPath + "/yh/core/funcs/news/act/YHNewsShowAct/addNewsComment.act";
	 	  
	  if(document.form1.content.value=="" || document.form1.content.value==null)
	  { alert("评论的内容不能为空！");
	    return (false);
	  }else{
	    $("form1").submit(); 
	  }
	}


</script>
</head>
<body onload="">
<form action="<%=contextPath %>/yh/core/funcs/news/act/YHNewsShowAct/relayComment.act"  method="post" id="form1" name="form1" >
  <table class="TableBlock" width="95%" align="center">
     <tr>
      <td class="TableHeader" colspan="2">
        <img src="<%=imgPath%>/green_arrow.gif"> 发表评论：
      </td>
    </tr>
    <tr>
      <td align="center" class="TableData">内容：</td>
      <td class="TableData">
        <textarea cols="45" id="content" name="content" rows="5" class="BigInput" wrap="on"></textarea>
      </td>
    </tr>
    <tr>
      <td align="center" class="TableData">署名：</td>
      <td class="TableData">
       <%
       	if(anonymityYn==0){//实名
       	  %>
       	   <input type="radio" id="authorName" name="authorName" checked>
       	   <input type="text"  id="userName" name="userName" size="10" maxlength="25" class="BigStatic" value="<%=userName%>" readonly>
       	  <%
       	}else{//匿名
       	 %>
     	   <input type="radio" id="authorName" name="authorName" checked value="0">
       	 <input type="text"  id="userName" name="userName" size="10" maxlength="25" class="BigStatic" value="<%=userName%>" readonly>
       	 <input type="radio" name="authorName" value="nickName" checked value="nickName">昵称
         <input type="text" id="nickName" name="nickName" size="10" maxlength="25" class="BigInput" value="">
     	  <%
       	}
       %> 
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td nowrap colspan="2">
        <input type="hidden" value="<%=parentId%>" name="parentId">
         <input type="hidden" value="<%=newsId%>" name="newsId">
        <input type="hidden" value="" name="manage">
        <input type="button" value="发表" onclick="javascript:addComment();return false;" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>