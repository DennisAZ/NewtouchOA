<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑员工福利信息</title>
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

function doInit(){
  	getSecretFlag("HR_WELFARE_ITEM","welfareItem");
	setDate();

    var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/welfare_manager/act/YHHrWelfareManageAct/getWelfareDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0")
	{
		var data = rtJson.rtData;
		$('staffId').value=data.staffName;
	     
		 data.staffName=staffNameFunc(data.staffName);
       
		data.paymentDate =data.paymentDate.substr(0,10);
		bindJson2Cntrl(rtJson.rtData); 
     } else{
         alert(rtJson.rtMsrg); 
     }
}

//获取员工姓名
function staffNameFunc(name){ 
  var url = contextPath + "/yh/subsys/oa/hr/manage/act/YHHrStaffIncentiveAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + name);
 
  if (rtJson.rtState == "0") {
    return  rtJson.rtData ;
  } else {
    alert(rtJson.rtMsrg); 
  }
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
  if($("welfareMonth").value=="")
   { 
      alert("工资月份不能为空！");
      return (false);
   }
 return (true);
}


</script>
</head>
<body onLoad="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 编辑员工福利信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/salary/welfare_manager/act/YHHrWelfareManageAct/updateWelfareInfo.act"  method="post" name="form1" id="form1" >
<table class="TableBlock" width="50%" align="center">
	<tr>
		<td nowrap class="TableContent">单位员工：<span  style="color:#FF0000" >*</span></td>
      <td class="TableData">
	    <input type="hidden" name="seqId" id="seqId" value="" >
        <input type="hidden" name="staffId" id="staffId" value="" >
        <input type="text" name="staffName" id="staffName" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['staffId', 'staffName'],null,null,1);">选择</a>
      </td>
 	  <td nowrap class="TableContent">福利项目：<span  style="color:#FF0000" >*</span></td>
      <td class="TableData">
      	 <select name="welfareItem" id="welfareItem"  title="福利项目可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">福利项目&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
    </td>
	</tr>
	<tr>
    <td nowrap class="TableContent">发放日期：</td>
     <td class="TableData">
     <input type="text" name="paymentDate" id="paymentDate" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
    <td nowrap class="TableContent">工资月份：<span  style="color:#FF0000" >*</span></td>
     <td class="TableData">
       <input name="welfareMonth" id="welfareMonth" size="15" class="BigInput" value="">
     </td>
  </tr>
	<tr>
    <td nowrap class="TableContent">福利金额：</td>
     <td class="TableData">
       <INPUT type="text"name="welfarePayment"  id="welfarePayment" class=BigInput size="15" value="">&nbsp;元
     </td>
    <td nowrap class="TableContent">是否纳税：</td>
     <td class="TableData">
       <select name="taxAffares" style="background: white;" title="">
          <option value="" >是否纳税&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
          <option value="0">否</option>
          <option value="1">是</option>
        </select>
     </td>
  </tr>
	<tr>
    <td nowrap class="TableContent">发放物品：</td>
     <td class="TableData" colspan=3>
       <textarea name="freeGift" cols="70" rows="5" class="BigInput" value=""></textarea>
     </td>
  </tr>
  <tr>
    <td nowrap class="TableContent">备注：</td>
     <td class="TableData" colspan=3>
       <textarea name="remark" cols="70" rows="6" class="BigInput" value=""></textarea>
     </td>
  </tr> 
	<tr align="center" class="TableControl">
    <td colspan=4 nowrap>
       <input  type="button"  onClick="doSubmit()" value="保存" class="BigButton">
	   <input  type="button"  onClick="window.history.go(-1)" value="返回" class="BigButton">
     </td>
  </tr>
</table>
</form>

</body>
</html>