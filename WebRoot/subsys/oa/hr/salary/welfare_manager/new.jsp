<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM");
String date=curTime.format(new Date());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建员工福利信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/salary/welfare_manager/js/welfaremanageLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function doInit(){
	getSecretFlag("HR_WELFARE_ITEM","welfareItem");
	//getSecretFlag("HR_STAFF_CARE1","welfareItem");
	setDate();	
	$("welfareMonth").value="<%=date%>";
}


//日期
function setDate(){
  var date1Parameters = {
     inputId:'paymentDate',
     property:{isHaveTime:false}
     ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
}

function doSubmit()
{
	
  if(CheckForm()){
    $("form1").submit();
  }
 
}

function CheckForm()
{ 
	if($('staffName').value=="")
   { 
      alert("请选择单位员工！");
      return (false);
   }
  if($("welfareItem").value=="")
   { 
      alert("福利项目不能为空！");
      return (false);
   }
   var month=$("welfareMonth").value;

   if(!CheckMonth(month)){
   
      return (false);
   }
   var money=$("welfarePayment").value;

   if(!isNumbers(money)){
        alert("福利金额必须为数字");
      return (false);
   }
  
 return (true);
}

function CheckMonth(month){
 var strArray = month.split("-"); 

 if (strArray.length!=2) { 
    alert('工资月份格式不对！');
	return false; 
  } 
  if(strArray[0]>2030 ||strArray[0]<2000){
    alert('年份必须在2000到2030之间！');
    return false;
  }
  if(strArray[1]>12 || strArray[1]<0){
    alert('月份必须在0到12之间！');
	return false;
  }
  
return true;
}





</script>
</head>
<body onLoad="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 新建员工福利信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/salary/welfare_manager/act/YHHrWelfareManageAct/addWelfareInfo.act"  method="post" name="form1" id="form1" >
<table class="TableBlock" width="50%" align="center">
	<tr>
		<td nowrap class="TableData">单位员工：<span  style="color:#FF0000" >*</span></td>
      <td class="TableData">
        <input type="hidden" name="staffId" id="staffId" value="" >
        <input type="text" name="staffName" id="staffName" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['staffId', 'staffName'],null,null,1);">选择</a>
      </td>
 	  <td nowrap class="TableData">福利项目：<span  style="color:#FF0000" >*</span></td>
      <td class="TableData">
      	 <select name="welfareItem" id="welfareItem"  title="福利项目可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">福利项目&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
    </td>
	</tr>
	<tr>
    <td nowrap class="TableData">发放日期：</td>
     <td class="TableData">
     <input type="text"  name="paymentDate" id="paymentDate" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
    <td nowrap class="TableData">工资月份：<span  style="color:#FF0000" >*</span></td>
     <td class="TableData">
       <input name="welfareMonth" id="welfareMonth" size="15" class="BigInput" value="" >
     </td>
  </tr>
	<tr>
    <td nowrap class="TableData">福利金额：</td>
     <td class="TableData">
       <INPUT type="text"name="welfarePayment"  id="welfarePayment" class=BigInput size="15" value="">&nbsp;元
     </td>
    <td nowrap class="TableData">是否纳税：</td>
     <td class="TableData">
       <select name="taxAffares" title="">
          <option value="" >是否纳税&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
          <option value="0">否</option>
          <option value="1">是</option>
        </select>
     </td>
  </tr>
	<tr>
    <td nowrap class="TableData">发放物品：</td>
     <td class="TableData" colspan=3>
       <textarea name="freeGift" cols="70" rows="5" class="BigInput" value=""></textarea>
     </td>
  </tr>
  <tr>
    <td nowrap class="TableData">备注：</td>
     <td class="TableData" colspan=3>
       <textarea name="remark" cols="70" rows="6" class="BigInput" value=""></textarea>
     </td>
  </tr> 
	<tr align="center" class="TableControl">
    <td colspan=4 nowrap>
       <input  type="button"  onClick="doSubmit()" value="保存" class="BigButton">
     </td>
  </tr>
</table>
</form>

</body>
</html>