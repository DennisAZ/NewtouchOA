<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
  String boxId =  request.getParameter("boxId");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<title>邮箱管理</title>
<script Language="JavaScript">
function CheckForm(){
  if($('boxNo').value==""){ 
    alert("邮箱序号不能为空！");
    $('boxNo').focus();
    return false;
  }
  if(checkNum($('boxNo').value)){ 
    alert("邮箱序号必须为数值！");
    $('boxNo').focus();
    $('boxNo').select();
    return false;
  }
  if($('boxNo').value < 1){
    alert("邮箱序号必须大于0！");
    $('boxNo').focus();
    $('boxNo').select();
    return false;
   }
  if($('boxName').value==""){
    alert("邮箱名称不能为空！");
    $('boxName').focus();
    return false;
  }
  if(checkStr($('boxName').value)){
    alert("邮箱名称不能包含:[\",\',&,%,#,\\] 等特殊字符！");
    $('boxName').focus();
    $('boxName').select();
    return false;
  }
  if(isBoxNameExist($('boxName').value,$('seqId').value)){
    alert("邮箱名称不能重复！");
    $('boxName').focus();
    $('boxName').select();
    return false;
   }
  return true;
}

function checkNum(str)
{
   var re=/\D/;
   return str.match(re);
}
function checkStr(str){
  var re=/["'&%#\\]/;
  return str.match(re);
}
function doSuBmit(){
  if(CheckForm()){
    updateBox();
    //parent.boxMan.location.reload();
  }
}
function SetEmailNums(UserID,BoxName,BOX_NO,BOX_ID)
{
   var CountValue=document.all(BoxName).value;
   if(CountValue=="")
   {
      alert("请输入数值");
      return false;
   }
   if(checkNum(CountValue))
   {
      alert("显示封数必须是数字");
      return false;
   }

   if(parseInt(CountValue)<=0 || parseInt(CountValue)>=1000)
   {
      alert("显示封数必须在1-1000之间");
      return false;
   }
   window.location="setCount.php?CountValue="+CountValue+"&BoxName="+BoxName+"&BOX_ID="+BOX_ID+"&BOX_NO="+BOX_NO;
}

function doInit(){
  var boxId = "<%=boxId%>";
  getBoxById(boxId);
}
</script>
</head>

<body topmargin="5" onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/cmp/email/notify_new.gif" align="absmiddle"><span class="big3"> 添加邮件箱</span>
    </td>
  </tr>
</table>
<form id="form1" name="form1" >
<table class = "TableBlock" width="450" align="center" >
   <tr>
    <td nowrap class="TableData">排序号：</td>
    <td nowrap class="TableData">
        <input type="text" id="boxNo" name="boxNo" class="BigInput" size="25" maxlength="25">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">名称：</td>
    <td nowrap class="TableData">
        <input type="text" id="boxName" name="boxName" class="BigInput" size="25" maxlength="25">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
       <input type="hidden" value="" id="seqId" name="seqId"></input>
       <input type="hidden" value="" id="userId" name="userId"></input>
       <input type="button" value="修改" class="BigButton" title="修改" onclick="doSuBmit();">
       <input type="button" value="返回" class="BigButton" title="返回" onclick="history.back()">
    </td>
</table>
</form>
</body>
</html>