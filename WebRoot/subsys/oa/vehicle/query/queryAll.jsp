<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>车辆申请查询</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/sms/js/smsutil.js"></script>
<script type="text/javascript">
function exportImport() {
  if (checkTime()) {
    var vuStatus = $("vuStatus").value;
    var vId = $("vId").value;
    var vuDriver = $("vuDriver").value;
    var vuRequestDateMin = $("vuRequestDateMin").value;
    var vuRequestDateMax = $("vuRequestDateMax").value;
    var vuUser = $("vuUser").value;
    var vuDept = $("vuDept").value;
    var vuStartMin = $("vuStartMin").value;
    var vuStartMax = $("vuStartMax").value;
    var vuEndMin = $("vuEndMin").value;
    var vuEndMax = $("vuEndMax").value;
    var vuProposer = $("vuProposer").value;
    var vuReason = $("vuReason").value;
    var vuRemark = $("vuRemark").value;
    var vuOperator = $("vuOperator").value;
  
    var param2 = "vuStatus=" + vuStatus + "&vId="+ vId + "&vuDriver=" + vuDriver + "&vuRequestDateMin=" + vuRequestDateMin + "&vuRequestDateMax=" + vuRequestDateMax
      + "&vuUser=" + vuUser + "&vuDept=" + vuDept + "&vuStartMin=" + vuStartMin + "&vuStartMax=" + vuStartMax + "&vuEndMin=" + vuEndMin
      + "&vuEndMax=" + vuEndMax + "&vuProposer=" + vuProposer + "&vuReason=" + encodeURIComponent(vuReason) + "&vuRemark=" + encodeURIComponent(vuRemark) + "&vuOperator=" + vuOperator;
    var param = param2;
    var url = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHExportAct/exportXls.act?" + param;
    window.location = url;
  }
}
function checkForm() {
  if (checkTime()) {
    var pars = $('form1').serialize() ;
    var param = encodeURI(pars);
    var url = "<%=contextPath %>/subsys/oa/vehicle/query/search.jsp?" + param;
    window.location = url;
  }
}
function doInit() {
  selectNum();
  doName();
  doTime();
}
function doTime() {
  //时间
  var parameters = {
      inputId:'vuRequestDateMin',
      property:{isHaveTime:true}
      ,bindToBtn:'requestDate1'
  };
  new Calendar(parameters);
  var parameters = {
      inputId:'vuRequestDateMax',
      property:{isHaveTime:true}
      ,bindToBtn:'requestDate2'
  };
  new Calendar(parameters);
  var parameters = {
      inputId:'vuStartMin',
      property:{isHaveTime:true}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(parameters);
  var parameters = {
      inputId:'vuStartMax',
      property:{isHaveTime:true}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(parameters);
  var parameters = {
      inputId:'vuEndMin',
      property:{isHaveTime:true}
      ,bindToBtn:'endImg1'
  };
  new Calendar(parameters);
  var parameters = {
      inputId:'vuEndMax',
      property:{isHaveTime:true}
      ,bindToBtn:'endImg2'
  };
  new Calendar(parameters);
}
//部门
var vuDeptField = null;
var vuDeptNameField = null;
function getDept(vuDept,vuDeptName){
  vuDeptField = vuDept;
  vuDeptNameField = vuDeptName;
  var URL= contextPath + "/subsys/oa/vehicle/getDept.jsp";
  openDialogResize(URL , 300, 355);
}

//车牌号
function selectNum() {
  var requestURL="<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleAct/selectVehicle.act";
  var json=getJsonRs(requestURL);
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prcsJson = json.rtData; 
  var select = document.getElementById("vId"); 
  for (var i = 0;i < prcsJson.length; i++) { 
    var option = document.createElement("option");
    option.value = prcsJson[i].seqId//seqId
    option.text = prcsJson[i].vNum;//车牌号
    var selectObj = $('vId');
    selectObj.options.add(option, selectObj.options ? selectObj.options.length : 0); 
  }
}

//调度人员名称
function doName() {
  var requestURLs = "<%=contextPath%>/yh/subsys/oa/vehicle/act/YHVehicleOperatorAct/selectManagerPerson.act"; 
  var rtJson = getJsonRs(requestURLs); 
  if(rtJson.rtState == '1') {
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var prcsJsons = rtJson.rtData; 
  var selects = document.getElementById("vuOperator"); 
  for(var i = 0;i < prcsJsons.length; i++){ 
    var option = document.createElement("option"); 
    option.value = prcsJsons[i].seqId; 
    option.text = prcsJsons[i].userName;
    var selectObj = $("vuOperator");
    selectObj.options.add(option,selectObj.options ? selectObj.options.length : 0); 
  }
}
function checkTime() {
  if ($("vuRequestDateMin").value != "" && checkDateTime("vuRequestDateMin") == false) {
    alert("申请日期格式不对，请输入形如：2010-10-10 12:12:12");
    document.getElementById("vuRequestDateMin").focus();
    document.getElementById("vuRequestDateMin").select();
    return (false);
  }
  if ($("vuRequestDateMax").value != "" && checkDateTime("vuRequestDateMax") == false) {
    alert("申请日期格式不对，请输入形如：2010-10-10 12:12:12");
    document.getElementById("vuRequestDateMax").focus();
    document.getElementById("vuRequestDateMax").select();
    return (false);
  }
  if ($("vuStartMin").value != "" && checkDateTime("vuStartMin") == false) {
    alert("起始日期格式不对，请输入形如：2010-10-10 12:12:12");
    document.getElementById("vuStartMin").focus();
    document.getElementById("vuStartMin").select();
    return (false);
  }
  if ($("vuStartMax").value != "" && checkDateTime("vuStartMax") == false) {
    alert("起始日期格式不对，请输入形如：2010-10-10 12:12:12");
    document.getElementById("vuStartMax").focus();
    document.getElementById("vuStartMax").select();
    return (false);
  }
  if ($("vuEndMin").value != "" && checkDateTime("vuEndMin") == false) {
    alert("结束日期格式不对，请输入形如：2010-10-10 12:12:12");
    document.getElementById("vuEndMin").focus();
    document.getElementById("vuEndMin").select();
    return (false);
  }
  if ($("vuEndMax").value != "" && checkDateTime("vuEndMax") == false) {
    alert("结束日期格式不对，请输入形如：2010-10-10 12:12:12");
    document.getElementById("vuEndMax").focus();
    document.getElementById("vuEndMax").select();
    return (false);
  }
  return true;
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<br>
 <form id="form1" name="form1">
<table class="TableTop" width="500" align="center">
    <tr>
    <td class="left"></td>
      <td class="center"><img src="<%=imgPath%>/green_arrow.gif"> 请指定查询条件：</td>
      <td class="right"></td>
    </tr>
  </table>
  <table class="TableBlock no-top-border" width="500" align="center">
    <tr>
      <td nowrap class="TableData" width="80"> 状　　态：</td>
      <td class="TableData">
        <select name="vuStatus" id="vuStatus">
          <option value=""></option>
          <option value="0">待批</option>
          <option value="1">已准</option>
          <option value="2">使用中</option>
          <option value="3">未准</option>
          <option value="4">结束</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 车 牌 号：</td>
      <td class="TableData">
        <select name="vId" id="vId">
          <option value=""></option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 司　　机：</td>
      <td class="TableData">
      <input type="hidden" name="vuDriver" id="vuDriver" value="">   
        <input type="text" name="vuDriverDesc" id="vuDriverDesc"  size="13" class="BigStatic" value="" readonly>&nbsp;
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['vuDriver','vuDriverDesc']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('vuDriver').value='';$('vuDriverDesc').value='';">清空</a>
      </td>
    </tr>    
    <tr>
      <td nowrap class="TableData" width="80"> 申请日期：</td>
      <td class="TableData">
        <input type="text" name="vuRequestDateMin" id="vuRequestDateMin"  class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif"  id="requestDate1" name="requestDate1" border="0" align="absMiddle" style="cursor:pointer">
        至
        <input type="text" name="vuRequestDateMax" id="vuRequestDateMax" class="BigInput" value="">
       <img src="<%=imgPath%>/calendar.gif"  id="requestDate2" name="requestDate2" border="0" align="absMiddle" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 用 车 人：</td>
      <td class="TableData">
         <input type="hidden" name="vuUser" id="vuUser" value="">   
        <input type="text" name="vuUserDesc" id="vuUserDesc"  size="13" class="BigStatic" value="" readonly>&nbsp;
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['vuUser','vuUserDesc']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('vuUser').value='';$('vuUserDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 用车部门：</td>
      <td class="TableData">
         <input type="hidden" name="vuDept" id="vuDept" value="">
        <input type="text" name="vuDeptName" id="vuDeptName" value="" class=BigStatic size=20 maxlength=100 readonly>
        <a href="javascript:;" class="orgAdd" onClick="getDept('vuDept','vuDeptName');">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('vuDept').value='';$('vuDeptName').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 起始日期：</td>
      <td class="TableData">
        <input type="text" name="vuStartMin" id="vuStartMin"  class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif"  id="beginDateImg" name="beginDateImg" border="0" align="absMiddle" style="cursor:pointer">
     至
        <input type="text" name="vuStartMax" id="vuStartMax" class="BigInput" value="">
       <img src="<%=imgPath%>/calendar.gif"  id="endDateImg" name="endDateImg" border="0" align="absMiddle" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 结束日期：</td>
      <td class="TableData">
        <input type="text" name="vuEndMin" id="vuEndMin" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif"  id="endImg1" name="endImg1" border="0" align="absMiddle" style="cursor:pointer">
       至
        <input type="text" name="vuEndMax" id="vuEndMax" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif"  id="endImg2" name="endImg2" border="0" align="absMiddle" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 申 请 人：</td>
      <td class="TableData">
        <input type="hidden" name="vuProposer" id="vuProposer" value="">   
        <input type="text" name="vuProposerDesc" id="vuProposerDesc"  size="13" class="BigStatic" value="" readonly>&nbsp;
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['vuProposer','vuProposerDesc']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('vuProposer').value='';$('vuProposerDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 调 度 员：</td>
      <td class="TableData">
        <select name="vuOperator" id="vuOperator">
          <option value=""></option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 事　　由：</td>
      <td class="TableData">
        <input type="text" name="vuReason" id="vuReason" class="BigInput" size="30">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="80"> 备　　注：</td>
      <td class="TableData">
        <input type="text" name="vuRemark" id="vuRemark" class="BigInput" size="30">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="查询" class="BigButton" onClick="checkForm()">&nbsp;&nbsp;
        <input type="button" value="导出" class="BigButton" onclick="exportImport();">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
