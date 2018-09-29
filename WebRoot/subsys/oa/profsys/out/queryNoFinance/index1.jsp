<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>出访信息检索 </title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
//初始化加载
function doInit() {
  getVisitType();
  getActiveType();
  activeType();
  doTime();
}
//出访类别
function getVisitType(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectAct/getCodeItem.act?classNo=PROJ_VISIT_TYPE1"; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prcs = json.rtData; 
  var selectObj = $("projVisitType");
  for(var i = 0 ; i < prcs.length ; i++){
    var prc = prcs[i];
    var seqId = prc.sqlId;
    var classNO = prc.classNo;
    var calssDesc = prc.classDesc;
    var myOption = document.createElement("option");
    myOption.value = seqId;
    myOption.text = calssDesc;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
//项目类别
function getActiveType() {
  var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectAct/getCodeItem.act?classNo=PROJ_ACTIVE_TYPE1"; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prcs = json.rtData; 
  var selectObj = $("projActiveType");
  for(var i = 0 ; i < prcs.length ; i++){
    var prc = prcs[i];
    var seqId = prc.sqlId;
    var classNO = prc.classNo;
    var calssDesc = prc.classDesc;
    var myOption = document.createElement("option");
    myOption.value = seqId;
    myOption.text = calssDesc;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}

//项目类别
function activeType() {
  var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectAct/getCodeItem.act?classNo=PROJ_ACTIVE_TYPE"; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prcs = json.rtData; 
  var selectObj = $("activeType");
  for(var i = 0 ; i < prcs.length ; i++){
    var prc = prcs[i];
    var seqId = prc.sqlId;
    var classNO = prc.classNo;
    var calssDesc = prc.classDesc;
    var myOption = document.createElement("option");
    myOption.value = seqId;
    myOption.text = calssDesc;
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  }
}
function doTime() {
  //时间
  var parameters = {
      inputId:'projStartTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'projStartTime1',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'projEndTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date3'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'projEndTime1',
      property:{isHaveTime:false}
      ,bindToBtn:'date4'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'memBirth',
      property:{isHaveTime:false}
      ,bindToBtn:'date5'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'startTime',
      property:{isHaveTime:true}
      ,bindToBtn:'date6'
  };
  new Calendar(parameters);

  var parameters = {
      inputId:'startTime1',
      property:{isHaveTime:true}
      ,bindToBtn:'date7'
  };
  new Calendar(parameters);

  var parameters = {
      inputId:'endTime',
      property:{isHaveTime:true}
      ,bindToBtn:'date8'
  };
  new Calendar(parameters);

  var parameters = {
      inputId:'endTime1',
      property:{isHaveTime:true}
      ,bindToBtn:'date9'
  };
  new Calendar(parameters);

  var parameters = {
      inputId:'commTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date10'
  };
  new Calendar(parameters);
}
//基本信息查询
function checkForm() {
  var pars = $('form1').serialize() ;
  var param = encodeURI(pars);
  var url = "<%=contextPath %>/subsys/oa/profsys/out/queryNoFinance/list.jsp?" + param;
  window.location = url;
}
//项目成员
function checkForm2() {
  var pars = $('form1').serialize();
  var param = encodeURI(pars);
  var url = "<%=contextPath%>/subsys/oa/profsys/out/queryNoFinance/listMem.jsp?" + param;
  window.location = url;
}
//项目日程
function checkForm3() {
  var pars = $('form1').serialize();
  var param = encodeURI(pars);
  var url = "<%=contextPath%>/subsys/oa/profsys/out/queryNoFinance/listCalendar.jsp?" + param;
  window.location = url;
}
//会议纪要
function checkForm4() {
  var pars = $('form1').serialize();
  var param = encodeURI(pars);
  var url = "<%=contextPath%>/subsys/oa/profsys/out/queryNoFinance/listComm.jsp?" + param;
  window.location = url;
}
//文档
function checkForm5() {
  var pars = $('form1').serialize();
  var param = encodeURI(pars);
  var url = "<%=contextPath%>/subsys/oa/profsys/out/queryNoFinance/listFile.jsp?" + param;
  window.location = url;
}
//团组名称
var budgetIdField = null;
var budgetNameField = null;
function toBudget(budgetId,budgetName){
  budgetIdField = budgetId;
  budgetNameField = budgetName;
  var URL= contextPath + "/subsys/oa/profsys/budgetlist.jsp";
  openDialogResize(URL , 650, 500);
 // window.open(URL,this,"height=355px,width=320px,directories=no,menubar=no,toolbar=no,status=no,scrollbars=yes,location=no,top="+loc_y+",left="+loc_x+"");
}
</script>
</head>
<body onLoad="doInit();">
<form name="form1" id="form1">
<input type="hidden" value="1" name="projType" id="projType">
<input type="hidden" value="1" name="projMemType" id="projMemType">
<input type="hidden" value="1" name="projCalendarType" id="projCalendarType">
<input type="hidden" value="1" name="projCommType" id="projCommType">
<input type="hidden" value="1" name="projFileType" id="projFileType">
<div align="left" style="margin:8px;margin-top:15px;">
<fieldset style="padding-bottom:5px;padding-left: 10xp;padding-right: 10px">
   <legend class="small" align=left>
      <b>项目基本信息</b>
  </legend>
     <table cellspacing="1" class="small" align="left" cellpadding="3" width="100%">
       <tr>
         <td nowrap class="TableData">项目编号：</td>
         <td nowrap class="TableData">
         <input type="text" class="BigInput" name="projNum" id="projNum" size="18" maxlength="18"><span id="check_msg"></span>
         </td>
         <td nowrap class="TableData">起始时间：</td>
         <td nowrap class="TableData">
         <INPUT type="text" class="BigInput" name="projStartTime" id="projStartTime" size="10" readonly>
         <img src="<%=imgPath%>/calendar.gif" id="date1" align="absMiddle" border="0" style="cursor:pointer">
         &nbsp;至
         <INPUT type="text" class="BigInput" name="projStartTime1" id="projStartTime1" size="10" readonly>
         <img src="<%=imgPath%>/calendar.gif" id="date2" align="absMiddle" border="0" style="cursor:pointer">  
         </td>        
      </tr>
     <tr>
      <td nowrap class="TableData">团组名称：</td>
      <td nowrap class="TableData">
      <input type="hidden" class="BigInput" id="budgetId" name="budgetId" size="18" maxlength="18" readonly>
      <input type="text" class="BigStatic" name="budgetIdDesc" id="budgetIdDesc" value="" size=40 readonly>
      <a href="javascript:toBudget('budgetId','budgetIdDesc');">选择数据</a> 
      </td>
      <td nowrap class="TableData">结束时间：</td>
      <td nowrap class="TableData">
      <INPUT type="text" class="BigInput" name="projEndTime" id="projEndTime" size="10" readonly>
     <img src="<%=imgPath%>/calendar.gif" id="date3" align="absMiddle" border="0" style="cursor:pointer">
     &nbsp;至
     <INPUT type="text" class="BigInput" name="projEndTime1" id="projEndTime1" size="10" readonly>
     <img src="<%=imgPath%>/calendar.gif" id="date4" align="absMiddle" border="0" style="cursor:pointer">
     </td>    
     </tr>
       <tr>
       <td nowrap class="TableData">项目类别：</td>
      <td nowrap class="TableData">
        <select name="projActiveType" id="projActiveType">
         <option value="">--请选择类别--</option>
        </select>
      </td>
      <td nowrap class="TableData">负责人：</td>
      <td nowrap class="TableData">
      <input type="hidden" name="projLeader" id="projLeader" size="10">
      <input type="text" name="projLeaderName" id="projLeaderName" class="BigInput" readonly value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['projLeader','projLeaderName']);">选择</a>
      <a href="javascript:;" class="orgClear" onClick="$('projLeader').value='';$('projLeaderName').value='';">清空</a>
    </td>       
    </tr>
     <tr>
      <td nowrap class="TableData">出访类别：</td>
      <td nowrap class="TableData">
        <select name="projVisitType" id="projVisitType">
               <option value="">--请选择类别--</option>
        </select>
      </td>    
     </tr>
      <tr>
         <td nowrap class="TableData" colspan="6" align="right">
          <input value="查询" type="button" class="SmallButton" title="查询" onClick="checkForm()">
         </td>
       </tr>
     </table>
</fieldset>
</div>
 
<div align="left" style="margin:8px;margin-top:15px;">
<fieldset style="padding-bottom:5px;padding-left: 10xp;padding-right: 10px">
   <legend class="small" align=left>
      <b>项目成员信息</b>
  </legend>
<table cellspacing="1" class="small" align="left" cellpadding="3" width="100%">
   <tr>
    <td nowrap class="TableData">成员编号：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memNum" id="memNum" size="10">
      </td>
    <td nowrap class="TableData">职务：</td>
      <td nowrap class="TableData">
      <textarea cols=20 name="memPositionDesc" id="memPositionDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <INPUT type="hidden" name="memPosition" id="memPosition" value="">
      <a href="javascript:;" class="orgAdd" onClick="selectRole(['memPosition','memPositionDesc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('memPosition').value='';$('memPositionDesc').value='';">清空 </a>
        </td>
     </tr>
  <tr>
      <td nowrap class="TableData">姓名：</td>
      <td nowrap class="TableData">
         <input type="text" name="memName" id="memName" value=""  size=20>
        <INPUT type="hidden" name="memNameId" id="memNameId">
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['memNameId','memName']);">选择</a>
        <a href="javascript:;" class="orgClear" onClick="$('memNameId').value='';$('memName').value='';">清空 </a>
      </td>
      <td nowrap class="TableData" width="90">性别：</td>
      <td nowrap class="TableData">
        <select name="memSex" id="memSex">
         <option value="">请选择类别</option>
         <option value="1">男</option>
         <option value="2">女</option>
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">出生年月： </td>
      <td nowrap class="TableData"> 
        <INPUT type="text" readonly name="memBirth" id="memBirth" class=BigInput size="10">
        <img src="<%=imgPath%>/calendar.gif" id="date5" name="date5" align="absMiddle" border="0" style="cursor:pointer">
      </td> 
    <td nowrap class="TableData" width="90">证件号码：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="memIdNum"  id="memIdNum" size="18"><span id="check_msg"></span>
      </td>
    </tr>
   <tr>
    <td nowrap class="TableData"  colspan="6" align="right">
    <input value="查询" type="button" onClick="checkForm2()" class="SmallButton" title="查询">
    </td>
  </tr>
</table>
</fieldset>
</div>
 
<div align="left" style="margin:8px;margin-top:15px;">
<fieldset style="padding-bottom:5px;padding-left: 10xp;padding-right: 10px">
   <legend class="small" align=left>
      <b>项目日程信息</b>
  </legend>
<table cellspacing="1" class="small" align="left" cellpadding="3" width="100%">
  <tr>
    <td nowrap class="TableData"> 活动类别：</td>
      <td nowrap class="TableData">
        <select name="activeType" id="activeType">
          <option value="">请选择类别</option>
        </select>
      </td>
     <td nowrap class="TableData"> 活动内容：</td>
     <td nowrap class="TableData">
     <input type="text" class="BigInput" name="activeContent" id="activeContent" size="15">
    </td>  
   </tr>
  <tr>    
   <td nowrap class="TableData" width="90">活动负责人：</td>
      <td nowrap class="TableData">
      <input type="hidden" name="activeLeader" id="activeLeader">
      <input type="text" name="activeLeaderName" id="activeLeaderName" size="10" class="BigInput" readonly>
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['activeLeader','activeLeaderName']);">选择</a>
      <a href="javascript:;" class="orgClear" onClick="$('activeLeader').value='';$('activeLeaderName').value='';">清空 </a>
    </td> 
   <td nowrap class="TableData" >活动参与人：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="activePartner" id="activePartner" size="15"><span id="check_msg"></span>
   </td>    
    </tr>
    <tr>
    <td nowrap class="TableData"> 起始时间：</td>
      <td nowrap class="TableData" colspan="3">
        <input type="text" class="BigInput" name="startTime" id="startTime" size="16" maxlength="16" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="date6" align="absMiddle" border="0" style="cursor:pointer">
       &nbsp;至
       <input type="text" class="BigInput" name="startTime1" id="startTime1" size="16" maxlength="16" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="date7" align="absMiddle" border="0" style="cursor:pointer">
      </td>
  </tr>
  <tr>
  <td nowrap class="TableData"> 结束时间：</td>
      <td nowrap class="TableData" colspan="3">
        <input type="text" class="BigInput" name="endTime" id="endTime"  size="16" maxlength="16" readonly>
        <img src="<%=imgPath%>/calendar.gif"  id="date8" align="absMiddle" border="0" style="cursor:pointer">
      &nbsp;至
      <input type="text" class="BigInput" name="endTime1" id="endTime1" size="16" maxlength="16" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="date9" align="absMiddle" border="0" style="cursor:pointer">
      </td>
  </tr>
   <tr>
    <td nowrap class="TableData"  colspan="6" align="right">
    <input value="查询" type="button" onClick="checkForm3();" class="SmallButton" title="查询" name="button">
    </td>
 </tr>
 </table>
</fieldset>
</div>
 
<div align="left" style="margin:8px;margin-top:15px;">
<fieldset style="padding-bottom:5px;padding-left: 10xp;padding-right: 10px">
   <legend class="small" align=left>
      <b>项目会谈纪要信息</b>
  </legend>
<table cellspacing="1" class="small" align="left" cellpadding="3" width="100%">
  <tr>
    <td nowrap class="TableData">纪要编号：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="commNum" id="commNum" size="10">
      </td>
    <td nowrap class="TableData">中方人员：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" id="commMemCn" name="commMemCn" size="10">
      </td>
      <td nowrap class="TableData">外方人员：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="commMemFn" id="commMemFn" size="10">
      </td> 
   </tr>
  <tr>
    <td nowrap class="TableData">纪要名称：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" id="commName" name="commName" size="10">
      </td>
    <td nowrap class="TableData">时间：</td>
      <td nowrap class="TableData"> 
        <INPUT type="text" readonly name="commTime" id="commTime" class=BigInput size="10">
        <img src="<%=imgPath%>/calendar.gif" id="date10" align="absMiddle" border="0" style="cursor:pointer">
      </td>
      <td nowrap class="TableData">地点：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" id="commPlace" name="commPlace" size=15><span id="check_msg"></span>
      </td>   
  </tr>
  <tr>
    <td nowrap class="TableData"  colspan="6" align="right">
    <input value="查询" type="button" onClick="checkForm4();" class="SmallButton" title="查询" name="button">
    </td>
 </tr>
 </table>
</fieldset>
</div>
 
<div align="left" style="margin:8px;margin-top:15px;">
<fieldset style="padding-bottom:5px;padding-left: 10xp;padding-right: 10px">
   <legend class="small" align=left>
      <b>项目相关文档信息</b>
  </legend>
<table cellspacing="1" class="small" align="left" cellpadding="3" width="100%">
  <tr>
    <td nowrap class="TableData">文档编号：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="fileNum" id="fileNum" size="10">
      </td>     
    <td nowrap class="TableData">文档名称：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" id="fileName" name="fileName" size="10">
      </td> 
      <td nowrap class="TableData">文档类别：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="fileType" id="fileType" size="10">
      </td>
   </tr>
  <tr>
    <td nowrap class="TableData">创建人：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="fileCreator" id="fileCreator" size=10 readonly><span id="check_msg"></span>
        <INPUT type="hidden" name="projCreator" id="projCreator">
         <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['projCreator','fileCreator']);">选择</a>
      <a href="javascript:;" class="orgClear" onClick="$('projCreator').value='';$('fileCreator').value='';">清空 </a>
      </td> 
      <td nowrap class="TableData">文档主题词：</td>
      <td nowrap class="TableData" colspan="3">
        <textarea name="fileTitle" id="fileTitle" cols="30" rows="1" class="BigInput"></textarea>
      </td>     
  </tr>
   <tr>
    <td nowrap class="TableData"  colspan="6" align="right">
    <input value="查询" type="button" onClick="checkForm5();" class="SmallButton" title="查询" name="button">
    </td>
 </tr>
</table>
</fieldset>
</div>
<br>
<table align="center">
    <tr>
      <td>
        <input type="button" value="返回" class="SmallButton" onclick="javascript:history.back()">
      </td>
   </tr>
</table>
</form>
</body>
</html>
