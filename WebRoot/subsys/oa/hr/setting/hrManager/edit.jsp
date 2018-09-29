<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
String deptIdStr = request.getParameter("deptId");
if(YHUtility.isNullorEmpty(deptIdStr)){
	deptIdStr = "-1";
}

%>
 
<html>
<head>
<title>人力资源管理员设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript">
var requestURI = "<%=contextPath%>/yh/subsys/oa/hr/setting/act/YHHrManagerAct";
function doInit(){
	//var deptId = "<%%>"
	getDeptName();
	var url = requestURI + "/getHrManagerIdStr.act";
	var rtJson = getJsonRs(url,"deptId=<%=deptIdStr%>" );
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		if(data.deptHrManager){
			$("deptHrManager").value = data.deptHrManager;
			if($("deptHrManager") && $("deptHrManager").value.trim()){
				bindDesc([{cntrlId:"deptHrManager", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
			}
		}
	}else{
		alert(rtJson.rtMsrg); 
		return ;
	}
	
}

function getDeptName(){
	if($("deptId") && $("deptId").value.trim() && $("deptId").value != "0" && $("deptId").value != "ALL_DEPT"){
		bindDesc([{cntrlId:"deptId", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
	}else if($("deptId") && ($("deptId").value == "0" || $("deptId").value == "ALL_DEPT")){
		$("deptId").value = "0";
		$("deptIdDesc").innerHTML = "全体部门";
	}
}

function doSubmit(){
	var pars = Form.serialize($('form1'));
	var url = requestURI + "/editHrManager.act";
	var rtJson = getJsonRs(url,pars);
	if(rtJson.rtState == "0"){
		location.href = contextPath + "/subsys/oa/hr/setting/hrManager/manage.jsp";
	}else{
		alert(rtJson.rtMsrg); 
	}
	

	
}

</script>


</head>
 
<body class="bodycolor" topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 人力资源管理员设置</span>
    </td>
  </tr>
</table>
 <form id="form1" name="form1"> 
<table class="TableBlock" width="450" align="center" >
   <tr>
    <td nowrap class="TableData">部门名称：</td>
    <td nowrap class="TableData">
        <input type="text" name="deptIdDesc" id="deptIdDesc" class="BigInput" size="20" maxlength="100" readonly="readonly"  value="">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">人力资源管理员：</td>
      <td class="TableData">
        <input type="hidden" name="deptHrManager" id="deptHrManager" value="">
        <textarea cols="22" name="deptHrManagerDesc" id="deptHrManagerDesc" rows="5" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['deptHrManager','deptHrManagerDesc'])">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('deptHrManager').value='';$('deptHrManagerDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" value="<%=deptIdStr %>" name="deptId" id="deptId">
        <input type="button" value="确定" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="javascript:location.href ='<%=contextPath %>/subsys/oa/hr/setting/hrManager/manage.jsp'">
    </td>
</table>
</form>
</body>
</html>