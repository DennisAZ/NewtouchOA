<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
  String seqId = request.getParameter("seqId") == null ? "" : request.getParameter("seqId") ;
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/wordmodel/js/wordLogic.js"></script>
<title>Insert title here</title>
<script type="text/javascript"><!--
var id = "<%=seqId %>";
function doInit(){
  if(id){
    showModel(id);
   }
}
function doSubmit(){
  if(!$('modelName').value){
    alert("模板名称不能为空！");
    $('modelName').focus();
    $('modelName').select();
    return ;
  }
  if(id){
    $("form1").action = contextPath + "/yh/core/funcs/system/wordmoudel/act/YHWordModelAct/updateWordModel.act";
    if(!$('ATTACHMENT').value && !$('attachmentName').value ){
      alert("请选择要上传的模板文件！ ");
      $('ATTACHMENT').focus();
      $('ATTACHMENT').select();
      return;
    }
    var fileName = $('ATTACHMENT').value;
    var index = fileName.lastIndexOf(".");
    var extName = "";
    if (index >= 0) {
      extName = fileName.substring(index + 1).toLowerCase();
    }
    if($('ATTACHMENT').value && extName != "dot"){
      alert("选择的文件不是模板文件,请选择模板文件(dot文件)!");
      $('ATTACHMENT').focus();
      $('ATTACHMENT').select();
      return ;
    }
  }else{
    if(!$('ATTACHMENT').value){
      alert("请选择要上传的模板文件！ ");
      $('ATTACHMENT').focus();
      $('ATTACHMENT').select();
      return;
    }
    var fileName = $('ATTACHMENT').value;
    var index = fileName.lastIndexOf(".");
    var extName = "";
    if (index >= 0) {
      extName = fileName.substring(index + 1).toLowerCase();
    }
    if(extName != "dot"){
      alert("选择的文件不是模板文件,请选择模板文件(dot文件)!");
      $('ATTACHMENT').focus();
      $('ATTACHMENT').select();
      return ;
    }
  }
  $("form1").submit();
}
--></script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">上传模板</span>
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath %>/yh/core/funcs/system/wordmoudel/act/YHWordModelAct/saveWordModel.act" method="post" name="form1" id="form1">
<table class="TableBlock" width="60%" align="center">
    <tr>
    	<td nowrap class="TableContent" align="center">模板名称:<font color="red">*</font></td>
    	<td class="TableData">
    		<input type="text" size=40 name="modelName" id="modelName" class="BigInput" value="">
    	</td>
    </tr>
 <tr>
      <td nowrap class="TableContent" align="center">授权范围：<br>（人员）</td>
      <td class="TableData">
        <input type="hidden" name="user" id="user" value="">
        <textarea cols=40 name="userDesc" id="userDesc" rows=4 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="Clear('user','userDesc');">清空</a>
      </td>
   </tr>
 <tr>
      <td nowrap class="TableContent" align="center">授权范围：<br>（部门）</td>
      <td class="TableData">
        <input type="hidden" name="dept" id="dept" value="">
        <textarea cols=40 name="deptDesc" id="deptDesc" rows=4 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectDept();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="Clear('dept','deptDesc');">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableContent" align="center">授权范围：<br>（角色）</td>
      <td class="TableData">
        <input type="hidden" name="role" id="role" value="">
        <textarea cols=40 name="roleDesc" id="roleDesc" rows=4 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="Clear('role','roleDesc');">清空</a>
      </td>
   </tr>
   <tr>
   	 <td nowrap class="TableContent" align="center">模板文件：<font color="red">*</font></td>
   	 <td class="TableData">
   	    <div id="attachmentNameShow"></div>
   	   <input type="file" name="ATTACHMENT" size=40 class="BigInput" id="ATTACHMENT" >
   	   <input type="hidden" id="ATTACHMENT_ID_OLD"  name="ATTACHMENT_ID_OLD" value="">
   	   <input type="hidden" id="attachmentName"  name="attachmentName" value="">
   	   <input type="hidden" id="attachmentId"  name="attachmentId" value="">
	   <input type="hidden" id="ATTACHMENT_NAME_OLD"  name="ATTACHMENT_NAME_OLD" value="">	  
   	</td>
   </tr>
   <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" id="seqId" name="seqId" value="">
        <input type="button" value="保存" class="BigButton" onclick="doSubmit()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="window.location='<%=contextPath %>/core/funcs/system/wordmodel/index.jsp';">
      </td>
   </tr>
</table>
</form>
</body>
</html>