<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1 
	response.setHeader("Pragma","no-cache"); //HTTP 1.0 
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择测试页面</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript">
function SelectDept(MODULE_ID, TO_ID, TO_NAME)
{
	MODULE_ID = document.getElementById("test").value;
  URL="/yh/core/module/dept_select?MODULE_ID=" + MODULE_ID + "&TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME 
  	+ "&USER_SEQ_ID=181" + "&USER_DEPT=135";
  openDialog(URL, 400, 350);
}
function SelectPriv(MODULE_ID, TO_ID, TO_NAME)
{
	MODULE_ID = document.getElementById("test").value;
  URL= "<%=contextPath%>/yh/core/module/priv_select/act/YHPrivSelectAct/getPriv.act?&TO_ID=" + TO_ID 
  	+ "&TO_NAME=" + TO_NAME + "&MODULE_ID=" + MODULE_ID + "&USER_SEQ_ID=181" + "&USER_PRIV=5";
  openDialog(URL, 250, 350);
}
function SelectUser(MODULE_ID, TO_ID, TO_NAME)
{
  URL = "/yh/core/module/user_select?MODULE_ID=" + MODULE_ID + "&TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME 
  	+ "&USER_SEQ_ID=181" + "&USER_PRIV=5" + "&USER_DEPT=135";
  openDialog(URL, 400, 350);
}
function ClearUser()
{
  var args = $(arguments);
  for(var i = 0; i < args.length; i++ )
  {
    var cntrl = $(args[i]);
    if(cntrl)
    {
      if (cntrl.tagName.toLowerCase() == "td" || cntrl.tagName.toLowerCase() == "div"
          || cntrl.tagName.toLowerCase() == "span")
      {
        cntrl.innerHTML =  '';
      }
      else
      {
        cntrl.value ='';
      }
    }
  }
}
function changeRange()
{
   if (document.getElementById("rang_role").style.display == "none")
   {
      document.getElementById("rang_role").style.display = "";
      document.getElementById("rang_user").style.display = "";
      document.getElementById("href_txt").innerHTML = "隐藏按人员或角色发布";
   }
   else
   {
      document.getElementById("rang_role").style.display = "none";
      document.getElementById("rang_user").style.display = "none";
      document.getElementById("href_txt").innerHTML = "按人员或角色发布";
   }
}
function openDialog(URL, width, height, loc_x, loc_y)
{
	if(!loc_x)
	{
	  loc_x = (screen.width - width) / 2;
	}
	if(!loc_y)
	{
	  loc_y = (screen.height - height) / 2;
	}
  if(navigator.appName.indexOf("Microsoft") == 0)
  {
    window.showModalDialog(URL, self, "edge:raised;scroll:1;status:0;help:0;resizable:1;dialogWidth:" + width + "px;dialogHeight:" + height 
        + "px;dialogTop:" + loc_y + "px;dialogLeft:" + loc_x + "px", true);
  }
  else
  {
    window.open(URL, self, "height=" + height + ",width=" + width + ",status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=" 
        + loc_y + ",left=" + loc_x + ",resizable=yes,modal=yes,dependent=yes,dialog=yes,minimizable=no", true);
  }
}
</script>
</head>
<body onload="">
<form enctype="multipart/form-data" action="add.php" method="post" name="form1">
输入模块号<input id="test" type= "text" value="6"><br>
<table class="TableBlock" width="95%" align="center">
	<tbody>
  	<tr>
    	<td class="TableData" nowrap="nowrap">&nbsp;按部门发布：<br>&nbsp;<a href="javascript:;" id="href_txt" onclick="changeRange();">隐藏按人员或角色发布</a></td>
      <td class="TableData">
      	<input id="DEPT_ID" value="" type="hidden">
        <textarea cols="40" id="DEPT_NAME" rows="2" class="BigStatic" wrap="yes" readonly="readonly"></textarea>
        <a href="javascript:;" class="orgAdd" onclick="SelectDept('6', 'DEPT_ID', 'DEPT_NAME')">添加</a>
	      <a href="javascript:;" class="orgClear" onclick="ClearUser('DEPT_ID', 'DEPT_NAME')">清空</a>
       	&nbsp;&nbsp;&nbsp;&nbsp;
      </td>
    </tr>
		<tr id="rang_user" style="">
      <td class="TableData" nowrap="nowrap">&nbsp;按人员发布：</td>
      <td class="TableData">
        <input id="USER_ID" value="" type="hidden">
        <textarea cols="40" id="USER_NAME" rows="2" class="BigStatic" wrap="yes" readonly="readonly"></textarea>
        <a href="javascript:;" class="orgAdd" onclick="SelectUser('6','USER_ID', 'USER_NAME')">添加</a>
        <a href="javascript:;" class="orgClear" onclick="ClearUser('USER_ID', 'USER_NAME')">清空</a>
      </td>
   </tr>
   <tr id="rang_role" style="">
      <td class="TableData" nowrap="nowrap">&nbsp;按角色发布：</td>
      <td class="TableData">
        <input id="PRIV_ID" value="" type="hidden">
        <textarea cols="40" id="PRIV_NAME" rows="2" class="BigStatic" wrap="yes" readonly="readonly"></textarea>
        <a href="javascript:;" class="orgAdd" onclick="SelectPriv('6', 'PRIV_ID', 'PRIV_NAME')">添加</a>
        <a href="javascript:;" class="orgClear" onclick="ClearUser('PRIV_ID', 'PRIV_NAME')">清空</a><br>
				 发布范围取部门、人员和角色的并集
      </td>
   </tr>
  </tbody>
</table>
  <input name="FORMAT" value="0" type="hidden">
</form>
</body>
</html>