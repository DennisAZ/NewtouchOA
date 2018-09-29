<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建装备维修计划</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">

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
	  //$("personId3").value=$("personId1").value;
	 // $("personName3").value=$("personName1").value;
	}


</script>
</head>
<body onLoad="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">新建装备维修计划&nbsp;&nbsp;    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/salary/welfare_manager/act/YHHrWelfareManageAct/addWelfareInfo.act"  method="post" name="form1" id="form1" >
<table class="TableBlock" width="80%" align="center">
	<tr>
		<td nowrap class="TableData">送修通知单号：</td>
      <td class="TableContent">
        <input type="text" name="staffName" id="staffName"  readonly size="15" >       
      </td>
 	  <td nowrap class="TableData">主修人：</td>
      <td class="TableContent">
      	  <input  type="hidden"  name="repairPersonId" id="repairPersonId"   >
		  <input  type="text"  name="repairPersonName" id="repairPersonName" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['repairPersonId', 'repairPersonName']);zhuxiu();">选择</a>
    </td>
	 	<td nowrap class="TableData">辅修人：</td>
      <td class="TableContent" >
      	  <input  type="hidden"  name="personId1" id="personId1"   >
		  <textarea cols="20" readonly class="BigStaic" rows="1" id="personName1" name="perosnName1"></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['personId1', 'personName1']);fuxiu();">选择</a>
    </td>
	</tr>
	<tr>
    <td nowrap class="TableData">送修部队：</td>
     <td class="TableContent" colspan="5">
     <input type="text" width="1200"   >
     </td>
  </tr>
	<tr>
    <td nowrap class="TableData">承修部门：</td>
     <td class="TableContent" colspan="2">
         <input  type="hidden"  name="deptId1" id="deptId1"   >
		  <input  type="text"  name="deptName1" id="deptName1" class="BigStatic" readonly size="15" >
	     <a href="javascript:;" class="orgAdd" onClick="selectDept(['deptId1', 'deptName1']);">选择</a>
     </td>
    <td nowrap class="TableData">送修时间：</td>
     <td class="TableContent" colspan="2">
      <input type="text"  name="repairDate" id="repairDate" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date1" align="middle" src="<%=imgPath%>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
  </tr>
	<tr>
    <td nowrap class="TableData">维修流程</td>
     <td class="TableData" align="center" colspan=3>
      预计范围时间
     </td>
	<td class="TableData" align="center" colspan=2>
      参与人员
     </td>
  </tr>
  <tr>
    <td nowrap class="TableData">装备检测：</td>
     <td class="TableContent" >
      <input type="text"  name="repairDate2" id="repairDate2" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
	 <td class="TableData">至</td>
	  <td class="TableContent" >
      <input type="text"  name="repairDate3" id="repairDate3" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
	  <td class="TableContent"  colspan="2">
      		  <input  type="hidden"  name="personId2" id="personId2"   >
		  <input  type="text"  name="personName2" id="personName2" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['personId2', 'personName2']);">选择</a>
    </td>
  </tr> <tr>
      <td nowrap class="TableData">装备维修：</td>
     <td class="TableContent" >
      <input type="text"  name="repairDate4" id="repairDate4" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date4" align="middle" src="<%=imgPath%>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
	 <td class="TableData">至</td>
	      <td class="TableContent" >
      <input type="text"  name="repairDate5" id="repairDate5" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date5" align="middle" src="<%=imgPath%>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
	  <td class="TableContent" colspan="2">
      	  <input  type="hidden"  name="personId4" id="personId4"   >
		  <input  type="text"  name="personName4" id="personName4" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['personId4', 'personName4']);">选择</a>
    </td>
  </tr> <tr>
      <td nowrap class="TableData">装备质检：</td>
     <td class="TableContent" >
      <input type="text"  name="repairDate6" id="repairDate6" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date6" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
	 <td class="TableData">至</td>
	      <td class="TableContent" >
      <input type="text"  name="repairDate7" id="repairDate7" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date7" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
	  <td class="TableContent"  colspan="2">
      	   <input  type="hidden"  name="personId6" id="personId6"   >
		  <input  type="text"  name="personName6" id="personName6" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['personId6', 'personName6']);">选择</a>
    </td>
  </tr> <tr>
      <td nowrap class="TableData">整机质检：</td>
     <td class="TableContent" >
      <input type="text"  name="repairDate8" id="repairDate8" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date8" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
	 <td class="TableData">至</td>
	      <td class="TableContent" >
      <input type="text"  name="repairDate9" id="repairDate9" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date9" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
	  <td class="TableContent"  colspan="2">
      	   <input  type="hidden"  name="personId8" id="personId8"   >
		  <input  type="text"  name="personName8" id="personName8" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['personId8', 'personName8']);">选择</a>
    </td>
  </tr> <tr>
      <td nowrap class="TableData">装备试验：</td>
     <td class="TableContent" >
      <input type="text"  name="repairDate10" id="repairDate10" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date10" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
	 <td class="TableData">至</td>
	      <td class="TableContent" >
      <input type="text"  name="repairDate11" id="repairDate11" size="11" maxlength="10"  class="BigInput" value=""  readonly="true">
        <img id="date11" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     </td>
	  <td class="TableContent"  colspan="2">
     <input  type="hidden"  name="personId10" id="personId10"   >
		  <input  type="text"  name="personName10" id="personName10" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['personId10', 'personName10']);">选择</a>
    </td>
  </tr> 
  <tr>
   <td class="TableData" colspan="3" align="left">装备名称：<input type="text" width="900"></td>
    <td class="TableData"  align="left">装备编号：<input type="text"></td>
   <td class="TableData" colspan="2" align="left">修理等级：<select name="select">
   <option value="0">小修</option>
    <option value="1">中修</option>
   <option value="2">大修</option>
   </select></td>
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
 <div  align="center">
 <br>
 <br>
  <input type="button" value="返  回" onclick="history.go(-1)" class="BigButton"></input>
 <input type="button" value="下一步" class="BigButton"></input>
 </div>
</body>
</html>