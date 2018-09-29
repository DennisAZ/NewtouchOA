<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/Menu.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript"> 


function doInit(){
	

  }
 
function checkForm(){
	if($("groupName").value==""){
	  	alert("群名称不能为空");
	   	return false;
	}
	 if($("groupNo").value==""){
		     alert("群排序号不能为空");
		     return false;
	  }
   if($("groupSubject").value==""){
	        alert("群主题不能为空");
	        return false;
	  }
   if($("groupUid").value==""){
         alert("群成员不能为空");
         return false;
 }
	
	return true;
}


 
</script>
<body onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3">
  <tr>
    <td><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"><span class="big1" id="newTitle">新建群</span>&nbsp;&nbsp;
     </td>
  </tr>
</table>
<form  action="<%=contextPath%>/yh/core/funcs/system/ispirit/n12/group/act/YHImGroupAct/addGroup.act"  onSubmit=" return checkForm() " >
<table class="TableBlock" width="70%" align="center">
   <tr class="TableData">
      <td>群名称：</td>
      <td><input type='text' id='groupName' name='groupName'><font color='red'> *必填</font></td>
   </tr>
   <tr class="TableData">
      <td>群排序号：</td>
      <td><input type='text' size="6" id='groupNo' name='groupNo'><font color='red'> *必填</font></td>
   </tr>
    <tr class="TableData">
    <td>群主题：</td>
    <td><input type='text' size="15" id='groupSubject' name='groupSubject'><font color='red'> *必填</font></td>
   </tr>
   <tr class="TableData">
      <td>请选择群成员：</td>
      <td>
        <input type='hidden' id='groupUid' name='groupUid'>
        <textarea name='userName' id='userName' cols='30' rows='4' class='BigStatic'></textarea>
        <a href="javascript:;" class="orgAdd" onclick="selectUser(['groupUid','userName'])">添加</a>
        <a href="javascript:;" class="orgClear" onclick=" $('groupUid').value='',$('userName').value='' ">清空</a>
        <font color='red'> *必填</font>
      </td>
   </tr>
    <tr class="TableData">
    <td>群介绍：</td>
    <td><textarea id="groupIntroduction" col="40" row="10" name="groupIntroduction"></textarea></td>
   </tr>
  <tr class="TableData">
    <td>群备注：</td>
    <td><textarea id="groupRemark" col="40" row="10" name="groupRemark"></textarea></td>
   </tr>
   
   <tr class="TableData">
      <td colspan='2' align='center'><input type='submit' class='BigButton' value='保存'/> </td>
   </tr>
   
 </table>
 </form>
</body>

</html>