<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人才档案查询</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css"><script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/orgselect.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/recruitment/js/recruitmentListLogic.js"></script>
<script type="text/javascript">

function doInit(){
//职称
  getSelectedCode("PRESENT_POSITION","PRESENT_POSITION");
//员工类型
  getSelectedCode("STAFF_OCCUPATION","TYPE");
//部门
  deptFunc("department");
  setDate();
}

//日期
function setDate(){
  var date1Parameters = {
    inputId:'ASS_PASS_TIME_START',
    property:{isHaveTime:false},
    bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
  
  var date2Parameters = {
    inputId:'ASS_PASS_TIME_END',
    property:{isHaveTime:false},
    bindToBtn:'date2'
  };
  new Calendar(date2Parameters);

}

function checkForm() {
   /* if ($("planName").value.trim() == "") {
    alert("计划名称不能为空");
    $("planName").focus();
    $("planName").select();
    return false;
  }
   */
  return true;
}

//获取部门列表
function deptFunc(deptDiv){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/recruitment/act/YHHrRecruitRecruitmentAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById(deptDiv);
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}


function doSubmit(){
  var query = $("form1").serialize();
  location = "<%=contextPath%>/subsys/oa/hr/recruit/recruitment/search.jsp?"+query;
}

</script>
</head>
<body onLoad="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3">招聘录用查询</span></td>
  </tr>
</table>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1" onSubmit="return CheckForm();">
<table class="TableBlock" width="600" align="center">
   <tr>
      <td nowrap class="TableData">计划编号：</td>
      <td class="TableData" >
        <INPUT type="text"name="PLAN_NO" id="PLAN_NO" class=BigInput size="15" >
       </td>
      <td nowrap class="TableData">应聘人姓名：</td>
      <td class="TableData">
        <INPUT type="text" name="APPLYER_NAME" id="APPLYER_NAME" class=BigInput size="15"> 
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">OA中用户名：</td>
      <td class="TableData" colspan=3>
        <INPUT type="text" name="OA_NAME" id="OA_NAME"  class=BigInput size="15"> 
      </td>
    </tr>
    <tr>
      </td>
       <td nowrap class="TableData">招聘岗位：</td>
      <td class="TableData" >
        <INPUT type="text" name="JOB_STATUS" id="JOB_STATUS"  class=BigInput size="15" >
       <td nowrap class="TableData">录用负责人：</td>
      <td class="TableData">
       <INPUT type="text" name="ASSESSING_OFFICER_NAME"  id="ASSESSING_OFFICER_NAME" class=BigStatic size="15" readonly value="">
       <input type="hidden" name="ASSESSING_OFFICER" id="ASSESSING_OFFICER"  value="">
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['ASSESSING_OFFICER', 'ASSESSING_OFFICER_NAME'])">选择</a>
      </td>                                             
    </tr>
    <tr>
      <td nowrap class="TableData">录入日期：</td>
      <td class="TableData" colspan='3'>
        <input type="text" name="ASS_PASS_TIME_START" id="ASS_PASS_TIME_START" readonly  size="10" maxlength="10" class="BigInput" value="" /> 
       <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
        至
        <input type="text" name="ASS_PASS_TIME_END" id="ASS_PASS_TIME_END"  size="10" maxlength="10" readonly class="BigInput" value="" />
       <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
       <tr>
      <td nowrap class="TableData">招聘部门：</td>
      <td class="TableData" colspan=3>
      <select id="department" name="department" >
        <option value="">请选择</option>
      </select>
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData" width="100">员工类型</td>
      <td class="TableData">
        <select name="TYPE" id="TYPE" >
        <option value="">请选择</option>     
        </select>     
      </td>   
      <td nowrap class="TableData">行政等级：</td>
      <td class="TableData" colspan=3>
        <INPUT type="text"name="ADMINISTRATION_LEVEL" id="ADMINISTRATION_LEVEL" class=BigInput size="15" >
    </tr> 
    <tr>
       <td nowrap class="TableData">职务：</td>
      <td class="TableData" >
        <INPUT type="text"name="JOB_POSITION" id="JOB_POSITION" class=BigInput size="15" >
      <td nowrap class="TableData" width="100">职称：</td>
      <td class="TableData"  width="180">
        <select name="PRESENT_POSITION" id="PRESENT_POSITION" >
          <option value="">请选择</option>
      
        </select>
      </td>
    </tr> 
     <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan=3>
        <textarea name="REMARK" id="REMARK"  cols="66" rows="5" class="BigInput" value=""></textarea>
      </td>
    </tr>
    <tr align="center" class="TableControl">
        <td colspan="6" nowrap>
          <input type="button" value="查询"  onClick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        </td>
    </tr>     
</table>
</form>

</table>
</body>
</html>