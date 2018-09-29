<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>批量更新档案</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffInfo/js/staffInfoLogic.js"></script>
<script type="text/javascript">
function doInit(){
//在职状态
	getSelectedCode("WORK_STATUS","workStatus");
}
function changeit(sel){
	if(sel=="overwrite")	{
	document.getElementById("HOLIDAY").style.display="";
	document.getElementById("WORK").style.display="";
	//document.getElementById("USERDEF").style.display="";
	}else	{
	document.getElementById("HOLIDAY").style.display="none";
	document.getElementById("WORK").style.display="none";
	//document.getElementById("USERDEF").style.display="none";
	}
}

function checkForm(){
	if($("staffName").value.trim() == ""){
		alert("请添加更新对象！");
		$("staffNameDesc").focus();
		return false;
	}
	var leaveType = $("leaveType").value.trim();
	if(leaveType){
		if(!isNumber(leaveType)){
			alert("天数必须为数字！");
			$("leaveType").focus();
			return false;
		}
		if(leaveType<0 || leaveType>365){
			alert("天数不能小于0或大于365");
			$("leaveType").focus();
			return false;
		}
	}
	if($("selectitem").value.trim() != "-1" && $("tContext").value.trim() == ""){
		alert("请输入您要更新的内容！");
		$("tContext").focus();
		return false;
	}
	return true;
	
}



function doSubmit(){
	if(checkForm()){
		var pars = Form.serialize($('form1'));
		var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/staffInfo/act/YHBatchUpdateStaffInfoAct/batchUpdateInfo.act";
		var rtJson = getJsonRs(url,pars);
		if(rtJson.rtState == "0"){
			$("showFormDiv").hide();
			$("remindDiv").show();
			//location.href = contextPath + "/subsys/oa/hr/setting/hrManager/manage.jsp";
		}else{
			alert(rtJson.rtMsrg); 
		}
	}
}
function showForm(){
	window.location.reload();
}


</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit();">
<div id = "showFormDiv" >
<form  action="" method="post" name="form1" id="form1">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/hrms.gif" HEIGHT="20">
     <span class="big3">批量更新档案
     </span>
    </td>
  </tr>
</table>

<table align="center" width="90%" class="TableBlock">
	<tr>
     <td nowrap class="TableData"><b>选择更新对象：</b></td>
		 <td nowrap class="TableData" colspan="3"> 
		  <input type="hidden" name="staffName" id="staffName" value="">
      <textarea cols=50 name="staffNameDesc" id="staffNameDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectUser(['staffName', 'staffNameDesc'],null,null,1);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('staffName').value='';$('staffNameDesc').value='';">清空</a>
		</td>
	</tr>
  <tr>
    <td nowrap class="TableData"><b>更新方式：</b></td>
	  <td colspan="3" class="TableData">
	  <input type="radio" name="mode" id="mode1" value="overwrite" checked="checked" onclick="javascript:changeit(this.value)"><label for="mode1">改写</label>
	  <input type="radio" name="mode" id="mode2" value="append" onclick="javascript:changeit(this.value)"><label for="mode2">追加</label>
	  </td>
	</tr>
  <tr>
      <td nowrap class="TableData" colspan="4"><b>更新内容：</b></td>
  </tr>
  <tr class="TableData" id="HOLIDAY" style="display:none;">
	  <td colspan="4" class="TableData">&nbsp;年假天数：
	  	<input type="text" name="leaveType" id="leaveType" class="BigInput" size="6" maxlength="3"> 天</td>
    </tr>
  <tr class="TableData" id="WORK">
	  <td colspan="4" class="TableData">&nbsp;在职状态：
	  	<select name="workStatus" id="workStatus" class="SmallSelect">
	  	  <option value=""></option>
	  	</select>
  </tr>
	<tr>
      <td nowrap class="TableData" colspan="4"> 
    	<select name="selectitem" id="selectitem" class="SmallSelect">
        <option value="-1">选择要更新的字段</option>
        <option value=certificate >职务情况</option>
        <option value="surety">担保记录</option>
        <option value="insure">社保缴纳情况</option>
        <option value="bodyExamim">体检记录</option>
        <option value="remark">备 注</option>
      </select>
      <textarea cols="45" name="tContext" id="tContext" rows="2" class="BigInput" wrap="on"></textarea>
		</td>
	</tr>	
  <tr>
    <td class="TableHeader" colspan="6" >
     <?=get_field_table(get_field_html("HR_STAFF_INFO","$USER_ID"))?>
    </td>
  </tr>
    <tfoot align="center" class="TableFooter">
      <td nowrap colspan="4" align="center">
        <input type="hidden" value="<?=$DEPT_ID?>" name="DEPT_ID">
		<input type="button" value="保存" class="BigButton" onClick="doSubmit()">&nbsp;&nbsp;
      </td>
    </tfoot>
    </table>
</form>
</div>

<div id="remindDiv" style="display: none">
<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">批量更新成功！</div>
    </td>
  </tr>
</table>
<br><center>
	 <input type="button" class="BigButton" value="返回" onclick="showForm();">
</center>
</div>

</body>
</html>
