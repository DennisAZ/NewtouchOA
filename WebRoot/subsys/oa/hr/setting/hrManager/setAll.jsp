<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
 
<html>
<head>
<title>批量设置人力资源管理员</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript">
function doSubmit(){
	var pars = Form.serialize($('form1'));
	var url = "<%=contextPath%>/yh/subsys/oa/hr/setting/act/YHHrManagerAct/setBatchValue.act";
	var rtJson = getJsonRs(url,pars);
	if(rtJson.rtState == "0"){
		location.href = contextPath + "/subsys/oa/hr/setting/hrManager/manage.jsp";
	}else{
		alert(rtJson.rtMsrg); 
	}
}

</script>
</head>
<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 批量设置人力资源管理员</span>
    </td>
  </tr>
</table>
 <form id="form1"  name="form1">  
<table class="TableBlock" width="600" align="center" >
   <tr>
    <td nowrap class="TableData">请选择人员：</td>
    <td nowrap class="TableData">
      <input type="hidden" name="user"  id="user" value="">
      <textarea cols="50" name="userName" id="userName" rows="5" class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectUser(['user', 'userName'])">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userName').value='';">清空</a>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">请选择部门：</td>
      <td class="TableData">
        <input type="hidden" name="deptStr" id="deptStr" value="">
        <textarea cols=50 name="deptStrName" id="deptStrName" rows=5 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['deptStr','deptStrName']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('deptStr').value='';$('deptStrName').value='';">清空</a>
      </td>
   </tr>
   <tr>
    <td nowrap class="TableData">操作：</td>
      <td class="TableData">
        <input type="radio" name="operation" value="0" id="OPERATION0" checked><label for="OPERATION0">批量添加</label>
        <input type="radio" name="operation" value="1" id="OPERATION1"><label for="OPERATION1">批量删除</label>
      </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="button" value="确定" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
      <input type="reset" value="清空" class="BigButton">
    </td>
    </tr>
</table>
  </form>
</body>
</html>