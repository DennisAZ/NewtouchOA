<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.profsys.data.YHProject"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
%>
<html>
<head>
<title>项目基本信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var statrTime = "<%=sf.format(new Date())%>";
var endTime = "<%=sf.format(new Date())%>";
var oFCKeditor = new FCKeditor('PROFSYS_CONTENT');
var upload_limit=1,limit_type="php,php3,php4,php5,";
//图片插入到正文
function InsertImage(src) { 
  var oEditor = FCKeditorAPI.GetInstance('PROFSYS_CONTENT') ; //FCK实例 
  if (oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) { 
  oEditor.InsertHtml( "<img src = '"+ src + "'/>") ; 
  } 
}

function doForm() {
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHProjectAct/showDetail.act?seqId=" + seqId;
  var json = getJsonRs(url);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  //alert(rsText);
  if (prc.seqId) {
    $("projNum").value = prc.projNum;
    $("projStatus2").value = prc.projStatus;
    $("budgetId").value = prc.budgetId;
    $("projLeader").value = prc.projLeader;
    $("projStartTime").value = prc.projStartTime.substr(0,10);
    $("projEndTime").value = prc.projEndTime.substr(0,10);
    selectVal(prc.projVisitType);
    selectVal2(prc.projActiveType);
    $("purposeCountry").value = prc.purposeCountry;
    $("countryTotal").value = prc.countryTotal;
    $("pTotal").value = prc.pTotal;
    $("pYx").value = prc.pYx;
    $("pCouncil").value = prc.pCouncil;
    $("pGuest").value = prc.pGuest;
    $("projNote").value = prc.projNote;
    $("PROFSYS_CONTENT").value = prc.projNote;//FCK
    $("printStatus").value = prc.printStatus;
    $("attachmentName").value = prc.attachmentName;
    $("attachmentId").value = prc.attachmentId;
    $("deptId").value = prc.deptId;
    $("projType").value = prc.projType;
    attachMenuUtil("showAtt","profsys",null,$('attachmentName').value ,$('attachmentId').value,false);
  }
  doVal();
}
//各种不一样条件
function doVal() {
  //自动 判断状态
  var selectObj = $("projStatus");
  //alert("取出时间" + $("projStartTime").value);
  //alert("当前时间" +statrTime);
  var myOption = document.createElement("option");
  if ($("projStatus2").value == "1") {
    myOption.value = "1";
    myOption.text = "已结束";
  }
  if ($("projStatus2").value == "0" && ($("projStartTime").value <= statrTime && $("projEndTime").value > endTime)) {
    myOption.value = "0";
    myOption.text = "进行中";
  }
  if ($("projStatus2").value == "0" && ($("projStartTime").value > statrTime && $("projEndTime").value > endTime)) {
    myOption.value = "0";
    myOption.text = "准备中";
  }
  if ($("projStatus2").value == "0" && endTime >= $("projEndTime").value) {
    myOption.value = "0";
    myOption.text = "已结束";
  }
  selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
  if ($("budgetId").value != "") {
    bindDesc([{cntrlId:"budgetId",dsDef:"BUDGET_APPLY,SEQ_ID,BUDGET_ITEM"}]);
  }
  if ($("projLeader").value != "") {
    bindDesc([{cntrlId:"projLeader",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
}
//选中
function selectVal(visitType) {
  var otypo = document.getElementById("projVisitType");
  for (var i = 0; i < otypo.options.length; i++) {
      if (otypo.options[i].value == visitType) {
        otypo.options[i].selected = true;
      }
  }
}
//选中
function selectVal2(activeType) {
  var otypo = document.getElementById("projActiveType");
  for (var i = 0; i < otypo.options.length; i++) {
      if (otypo.options[i].value == activeType) {
        otypo.options[i].selected = true;
      }
  }
}
//初始化加载····
function doInit() {
  //时间
  var parameters = {
      inputId:'projStartTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
  var parameters = {
      inputId:'projEndTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
  getActiveType();
  getVisitType();
  doForm();
}
//表单验证
function checkForm() {
   if ($("projNum").value == "") {
     alert("项目编号必填!");
     $("projNum").focus();
     $("projNum").select();
     return false;
  }
   if ($("budgetIdDesc").value == "") {
    alert("团组名称必填!");
    $("budgetIdDesc").focus();
    $("budgetIdDesc").select();
    return false;
   }
   if ($("projLeaderDesc").value == "") {
     alert("负责人必填!");
     $("projLeaderDesc").focus();
     $("projLeaderDesc").select();
     return false;
   }
   if ($("projStartTime").value >= $("projEndTime").value) {
     alert("起始时间不能大于等于结束时间!");
     $("projEndTime").focus();
     $("projEndTime").select();
     return false;
   }
   if (!isNumber($("countryTotal").value) && $("countryTotal").value != "") {
     alert("出访国家总数应为整数!");
     $("countryTotal").focus();
     $("countryTotal").select();
     return false;
   }
   if (!isNumber($("pTotal").value) && $("pTotal").value != "") {
     alert("参与总人数应为整数!");
     $("pTotal").focus();
     $("pTotal").select();
     return false;
   }
   if (!isNumber($("pYx").value) && $("pYx").value != "") {
     alert("参与外办人员应为整数!");
     $("pYx").focus();
     $("pYx").select();
     return false;
   }
   if (!isNumber($("pCouncil").value) && $("pCouncil").value != "") {
     alert("参与理事人数应为整数!");
     $("pCouncil").focus();
     $("pCouncil").select();
     return false;
   }
   if (!isNumber($("pGuest").value) && $("pGuest").value != "") {
     alert("参与外宾人数应为整数!");
     $("pGuest").focus();
     $("pGuest").select();
     return false;
   }
   return true;
}
//提交申请
var isUploadBackFun = false;
function checkForm2() {
  var FCK = FCKeditorAPI.GetInstance('PROFSYS_CONTENT'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行

  var FORM_MODE = FCK.EditingArea.Mode;
  // 获取编辑区域的常量——源文件模式
  var editingAreaFrame = document.getElementById('PROFSYS_CONTENT___Frame');
  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
  if(FORM_MODE == editModeSourceConst) {
    FCK.Commands.GetCommand('Source').Execute();
  } 
  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
  document.getElementById("PROFSYS_CONTENT").value = FORM_HTML;
  document.getElementById("projNote").value = FORM_HTML;
  if (checkForm()) {
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      return ;
    }
    var pars = $('form1').serialize() ;
    var requestURL="<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHProjectAct/updateProject.act";
    var json=getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      alert("修改成功!");
      window.location.reload();
    }
  }
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

//国家
var userRetNameArray=null;var deptRetNameArray=null;var roleRetNameArray=null;var userExternalRetNameArray=null;
function selectCounrty(retArray,moduleId,privNoFlag,privOp){
  roleRetNameArray=retArray;
  var url=contextPath+"/subsys/oa/profsys/getCountry.jsp";
  var has=false;if(moduleId){url += "?moduleId=" + moduleId;
  if(!privNoFlag){
    privNoFlag=0;
  }
  url += "&privNoFlag=" + privNoFlag;has = true;}
  if(privOp){
    if(has){
      url += "&privOp=" + privOp;
    }else{
      url += "?privOp=" + privOp;
    }
  }
 openDialog(url,280,400);
}
//附件上传
function upload_attach(){
  if(checkForm()){
    $("btnFormFile").click();
  }  
}
function handleSingleUpload(rtState,rtMsrg,rtData) {
  var data = rtData.evalJSON(); 
  $('attachmentId').value += "," + data.attrId;
  $('attachmentName').value += "*" + data.attrName;
  attachMenuUtil("showAtt","profsys",null,$('attachmentName').value ,$('attachmentId').value,false);
  removeAllFile();
  if (isUploadBackFun) {
    checkForm2();
    isUploadBackFun = false;
   }
}
//有附件，也执行上传附件
function jugeFile(){
  var formDom  = document.getElementById("formFile");
  var inputDoms  = formDom.getElementsByTagName("input"); 
  for(var i=0; i<inputDoms.length; i++){
    var idval = inputDoms[i].id;
    if(idval.indexOf("ATTACHMENT")!=-1){
      return true;
    }
  } 
  return false; 
}
//上传后可以显示浮动菜单
function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
  var attachNameOld = $('attachmentName').value; 
  var attachIdOld = $('attachmentId').value; 
  var attachNameArrays = attachNameOld.split("*"); 
  var attachIdArrays = attachIdOld.split(","); 
  var attaName = ""; 
  var attaId = ""; 
  for (var i = 0 ; i < attachNameArrays.length ; i++) {
    if (!attachIdArrays[i] || attachIdArrays[i] == attachId) { 
        continue; 
    }
    attaName += attachNameArrays[i] + "*"; 
    attaId += attachIdArrays[i] + ","; 
  }
  $('attachmentId').value = attaId; 
  $('attachmentName').value = attaName;
  if (checkForm()) {
    var pars = $('form1').serialize() ;
    var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHProjectAct/updateProject.act";
    var json = getJsonRs(url,pars);
    if(json.rtState == "1") {
      alert(json.rtMsrg);
      return false;
    }
    return true;
  }
}
</script>
</head>
<body onLoad="doInit()" class="bodycolor">
<form id="form1" name="form1" >
<input type="hidden" name="dtoClass" value="yh.subsys.oa.profsys.data.YHProject">
<input type="hidden" name="seqId" id="seqId" value="<%=seqId%>">
<input type="hidden" name="printStatus" id="printStatus" value="0">
<input type="hidden" id="projNote" name="projNote" value="">
<input type="hidden" name="deptId" id="deptId" value="">
<input type="hidden" name="projType" id="projType" value="">
 <table class="TableBlock" border="0" width="80%" align="center">
    <tr>
      <td nowrap class="TableContent" width="90">项目编号：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="projNum" id="projNum" value="" size=20>
       </td>
      <td nowrap class="TableContent">项目状态：</td>
      <td nowrap class="TableData">
      <input type="hidden" name="projStatus2" id="projStatus2">
        <select name="projStatus" id="projStatus">
         </select>
           &nbsp;<font color="red">注：根据时间自动判断</font>
      </td>               
    </tr>    <tr>
      <td nowrap class="TableContent" width="90">团组名称：<span style="color:red">*</span></td>
      <td nowrap class="TableData" colspan="3">
      <input type="hidden" id="budgetId" name="budgetId" value="" >
      <input type="text" class="BigStatic" name="budgetIdDesc" id="budgetIdDesc" value="" size=40 readonly>
      <a href="javascript:toBudget('budgetId','budgetIdDesc');">选择数据</a>  
    </td>
    </tr> 
    <tr>
     <td nowrap class="TableContent" width="90">负责人：<span style="color:red">*</span></td>
      <td nowrap class="TableData" colspan="3">
      <input type="hidden" name="projLeader" id="projLeader"  value="">
      <input type="text" name="projLeaderDesc" id="projLeaderDesc" value=""  class="BigStatic" readonly>
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['projLeader','projLeaderDesc']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('projLeader').value='';$('projLeaderDesc').value='';">清空</a>
    </td> 
    </tr> 
    <tr>
      <td nowrap class="TableContent">出访类别：</td>
      <td nowrap class="TableData">
        <select name="projVisitType" id="projVisitType">
         <option value="">--请选择类别--</option>
        </select>
      </td>
      <td nowrap class="TableContent">项目类别：</td>
      <td nowrap class="TableData">
        <select name="projActiveType" id="projActiveType">
         <option value="">--请选择类别--</option>
        </select>
      </td>     
      </tr>
      <tr>
      <td nowrap class="TableContent">起始时间：<span style="color:red">*</span></td>
      <td nowrap class="TableData"> 
        <INPUT type="text" readonly name="projStartTime" id="projStartTime" class=BigInput size="10" value="">
        <img src="<%=imgPath%>/calendar.gif" align="absMiddle" id="date1" name="date1" border="0" style="cursor:pointer">
      </td>
      <td nowrap class="TableContent">结束时间：<span style="color:red">*</span></td>
      <td nowrap class="TableData">  
        <INPUT type="text" readonly name="projEndTime" id="projEndTime" class=BigInput size="10" value="">
        <img src="<%=imgPath%>/calendar.gif" id="date2" name="date2" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
     <tr>
      <td nowrap class="TableContent">出访国家：</td>
      <td nowrap class="TableData"  colspan="3">
        <input type="hidden" name="purposeCountryId" id="purposeCountryId" value="">
        <textarea name="purposeCountry" id="purposeCountry" cols="40" rows="2" class="BigStatic" wrap="yes"></textarea>
  <a href="javascript:;" class="orgAdd" onClick="javascript:selectCounrty(['purposeCountryId','purposeCountry']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('purposeCountryId').value='';$('purposeCountry').value='';">清空</a>
        <br><font color="red">注：如需手动填写，请用顿号（、）隔开，形如：法国、英国、美国、</font>
      </td>    
    </tr>
    <tr>
      <td nowrap class="TableContent">出访国家总数：</td>
      <td nowrap class="TableData"  colspan="3">
        <input type="text" class="BigInput" name="countryTotal" id="countryTotal" value="" size=40 maxlength="9">
      </td>    
    </tr>
    <tr>
      <td nowrap class="TableContent">参与总人数：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="pTotal" id="pTotal" value="" size=20 maxlength="9">
      </td>    
      <td nowrap class="TableContent">参与外办人员：</td>
      <td nowrap class="TableData">
       <input type="text" class="BigInput" name="pYx" id="pYx" value="" size=20 maxlength="9">
     </td>    
     </tr>
    <tr>
      <td nowrap class="TableContent">参与理事人数：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="pCouncil" id="pCouncil" value="" size=20 maxlength="9">
      </td>    
      <td nowrap class="TableContent">参与外宾人数：</td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="pGuest" id="pGuest" value="" size=20 maxlength="9">
      </td>    
    </tr>
    <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3">
<div><script language=JavaScript>    
         oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
         oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
         oFCKeditor.Height = "300px";
         oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
         oFCKeditor.ToolbarSet="DiaryBar";
         oFCKeditor.Create();
         </script>
      <input type="hidden" id="PROFSYS_CONTENT" name="PROFSYS_CONTENT" value="">
</div>
</td>
    </tr>
      <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData" colspan="3">
      <input type="hidden" id="attachmentName" name="attachmentName"></input>
       <input type="hidden" id="attachmentId" name="attachmentId"></input>
        <span id="showAtt"></span>
      </td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData" colspan="3">
        <input type="hidden" id="moduel" name="moduel" value="profsys">
       <script>ShowAddFile();ShowAddImage();</script>
     <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script> 
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
      <input type="hidden" name="OP" value="">
      <input type="hidden" name="PROJ_ID" id="PROJ_ID" value="36">
      <input type="hidden" name="EDIT_FLAG" value="">
      <input type="button" value="保存" class="BigButton" onclick="javascript:checkForm2()">&nbsp;
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();parent.window.close()">&nbsp;
      </td>
  </tr>
 </table>
</form>
<!--  -->
<form id="formFile" action="<%=contextPath %>/yh/subsys/oa/profsys/act/out/YHProjectAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe> 
</body>
</html>