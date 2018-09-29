<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<%
SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM");
String date=curTime.format(new Date());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建验收方案通知单</title>
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

<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/salary/welfare_manager/js/welfaremanageLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function doInit(){

	setDate();	

}


//日期
function setDate(){
  var date1Parameters = {
     inputId:'repairDate',
     property:{isHaveTime:false}
     ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
    var date2Parameters = {
     inputId:'repairDate2',
     property:{isHaveTime:false}
     ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
    var date3Parameters = {
     inputId:'repairDate3',
     property:{isHaveTime:false}
     ,bindToBtn:'date3'
  };
  new Calendar(date3Parameters);
    var date4Parameters = {
     inputId:'repairDate4',
     property:{isHaveTime:false}
     ,bindToBtn:'date4'
  };
  new Calendar(date4Parameters);

    var date5Parameters = {
     inputId:'repairDate5',
     property:{isHaveTime:false}
     ,bindToBtn:'date5'
  };
  new Calendar(date5Parameters);
  
      var date6Parameters = {
     inputId:'repairDate6',
     property:{isHaveTime:false}
     ,bindToBtn:'date6'
  };
  new Calendar(date6Parameters);
  
        var date7Parameters = {
     inputId:'repairDate7',
     property:{isHaveTime:false}
     ,bindToBtn:'date7'
  };
  new Calendar(date7Parameters);
  
        var date8Parameters = {
     inputId:'repairDate8',
     property:{isHaveTime:false}
     ,bindToBtn:'date8'
  };
  new Calendar(date8Parameters);
  
        var date9Parameters = {
     inputId:'repairDate9',
     property:{isHaveTime:false}
     ,bindToBtn:'date9'
  };
  new Calendar(date9Parameters);
  
        var date10Parameters = {
     inputId:'repairDate10',
     property:{isHaveTime:false}
     ,bindToBtn:'date10'
  };
  new Calendar(date10Parameters);
  
          var date11Parameters = {
     inputId:'repairDate11',
     property:{isHaveTime:false}
     ,bindToBtn:'date11'
  };
  new Calendar(date11Parameters);
  
  
  
}


function zhuxiu(){
  $("personId2").value=$("repairPersonId").value;
  $("personName2").value=$("repairPersonName").value;
	
}

function fuxiu(){
	  $("personId3").value=$("personId1").value;
	  $("personName3").value=$("personName1").value;
	  
	}


</script>
</head>
<body onLoad="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">新建验收方案通知单&nbsp;&nbsp;    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/salary/welfare_manager/act/YHHrWelfareManageAct/addWelfareInfo.act"  method="post" name="form1" id="form1" >
<table class="TableBlock" width="80%" align="center">
	<tr>
		<td nowrap class="TableData">送修通知单号：</td>
      <td class="TableContent" colspan="5" >
        <input type="text" name="staffName" id="staffName" size="50" >       
      </td>
	</tr>
	<tr>
    <td nowrap class="TableData">送修部队：</td>
     <td class="TableContent" colspan="5">
     <input type="text"  size="50"   >
     </td>
  </tr>
	<tr>
    <td nowrap class="TableData">承修部门：</td>
     <td class="TableContent" colspan="2" >
         <input  type="hidden"  name="deptId1" id="deptId1"   >
		  <input  type="text"  name="deptName1" id="deptName1" class="BigStatic" readonly size="15" >
	     <a href="javascript:;" class="orgAdd" onClick="selectDept(['deptId1', 'deptName1']);">选择</a>
     </td>
    <td nowrap class="TableData">送修时间：</td>
     <td class="TableContent" colspan="2" >
      <input type="text"  name="repairDate" id="repairDate" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date1" align="middle" src="<%=imgPath%>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
  </tr>
   <tr>
   <td class="TableData">主修人：</td>
      <td class="TableContent" colspan="5">
	     <input  type="hidden"  name="personId10" id="personId10"   >
		  <input  type="text"  name="personName10" id="personName10" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['personId10', 'personName10']);">选择</a>
	  </td>
   </tr>
	<tr>
    <td  class="TableData" colspan="3" align="center">装备名称</td>
     <td class="TableData" align="center" >
      装备编号
     </td>
	<td class="TableData" align="center" colspan=2>
      修理等级
     </td>
  </tr>
  	<tr>
    <td  class="TableContent" colspan="3" align="center">
	<input type="text"  size="40">
	</td>
     <td class="TableContent" align="center" >
 	<input type="text" >
     </td>
	<td class="TableContent" align="center" colspan=2>
      <select id="sds">
	  <option value="0">大</option>
	   <option value="1">中</option>
	    <option value="0">小</option>
	  </select>
     </td>
  </tr>
  <tr>
      <td nowrap class="TableData">检验日期：</td>
     <td class="TableContent" colspan="2">
         <input type="text"  name="repairDate4" id="repairDate4" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date4" align="middle" src="<%=imgPath%>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
    <td nowrap class="TableData">总工：</td>
     <td class="TableContent" colspan="2">
      <input  type="hidden"  name="personId6" id="personId6"   >
		  <input  type="text"  name="personName6" id="personName6" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['personId6', 'personName6']);">选择</a>
	    </td>
</tr>
  <tr>
  <td colspan="2">签发机关（盖章）</td><td colspan="4"></td>
  </tr>
  <tr><td colspan="6" ></td></tr>
  <tr><td colspan="6" ></td></tr>
    <tr><td colspan="6" ></td></tr>
  <tr>
  <td colspan="4"></td><td colspan="2" align="center">业务处</td>
  </tr> 
    <tr>
	<td colspan="6" ></td>
	</tr>
  <tr>
  <td colspan="6" ></td>
  </tr>
    <tr>
	<td colspan="6" ></td>
	</tr>
  <tr>
  <td colspan="4"></td><td colspan="2" align="center">签发日期</td>
  </tr> 
  <tr>
  <td>承办人</td><td colspan="5"></td>
  </tr>
    <tr>
  <td colspan="4"></td><td colspan="2" align="center">有效时间</td>
  </tr> 
</table>
</form>
 <div align="center">
 <br>
 <br>
  <input type="button" value="返  回" onclick="history.go(-1)" class="BigButton"></input>
 <input type="button" value="提  交" onclick="" class="BigButton"></input>
 </div>
</body>
</html>