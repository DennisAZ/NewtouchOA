<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%
  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
  String curDateStr = sf.format(new Date());
  String seqId = request.getParameter("seqId");
  if(seqId == null){
    seqId = "";
  }
%>
<html>
<head>
<title>项目基本信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
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
var upload_limit=1,limit_type="php,php3,php4,php5,";
var oFCKeditor = new FCKeditor('PROFSYS_LIST_CONTENT');//初始化
var oFCKeditorNote = new FCKeditor('PROFSYS_NOTE_CONTENT');
//图片插入到正文

function InsertImage(src) { 
  var oEditor = FCKeditorAPI.GetInstance('PROFSYS_LIST_CONTENT') ; //FCK实例 
  if (oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) { 
  oEditor.InsertHtml( "<img src = '"+ src + "'/>") ; 
  }  
  var oEditor = FCKeditorAPI.GetInstance('PROFSYS_NOTE_CONTENT') ; //FCK实例 
  if (oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) { 
  oEditor.InsertHtml( "<img src = '"+ src + "'/>") ; 
  } 
}
function getContent(id,fckId){
  var oEditor = FCKeditorAPI.GetInstance(fckId) ;
  $(id).value=oEditor.GetXHTML();
}

var  selfdefMenu = {
  	office:["downFile","dump","read","deleteFile"], 
    img:["downFile","dump","play","deleteFile"],  
    music:["downFile","dump","play","deleteFile"],  
    video:["downFile","dump","play","deleteFile"], 
    others:["downFile","dump","deleteFile"]
	}

function doOnloadFile(){
  var attr = $("attr");
  var seqId  = $("seqId").value;
  attachMenuSelfUtil(attr,"profsys",$('attachmentName').value ,$('attachmentId').value, '','',seqId,selfdefMenu);
}
var isUploadBackFun = false;
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
  attachMenuUtil("attr","profsys",null,$('attachmentName').value ,$('attachmentId').value,false);
  removeAllFile();
  if(isUploadBackFun){//返回后再次执行
    doInit();
  }
  return true;
}
//有附件，也执行上传附件

function jugeFile() {
  var formDom  = document.getElementById("formFile");
  var inputDoms  = formDom.getElementsByTagName("input"); 
  for (var i=0; i<inputDoms.length; i++) {
  var idval = inputDoms[i].id;
  if(idval.indexOf("ATTACHMENT") != -1){
    return true;
  }
} 
return false; 
}
//上传后可以显示浮动菜单
var isDel = false;
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
isDel = true;
doInit();
return true;
}

//表单验证
function checkForm() {
   if ($("projNum").value == "") {
     alert("活动编号必填!");
     $("projNum").focus();
     $("projNum").select();
     return false;
  }
   if ($("projGroupName").value == "") {
    alert("活动名称必填!");
    $("projGroupName").focus();
    $("projGroupName").select();
    return false;
   }
   if ($("projLeaderName").value == "") {
     alert("负责人必填!");
     $("projLeaderName").focus();
     $("projLeaderName").select();
     return false;
   }
   if ($("projStartTime").value >= $("projEndTime").value) {
     alert("开始时间不能大于等于结束时间!");
     $("projEndTime").focus();
     $("projEndTime").select();
     return false;
   }
   if ($("countryTotal").value!= '' && !isNumber($("countryTotal").value)) {
     alert("来访国家总数应为整数!");
     $("countryTotal").focus();
     $("countryTotal").select();
     return false;
   }
   if ($("pTotal").value != '' &&!isNumber($("pTotal").value)) {
     alert("参与总人数应为整数!");
     $("pTotal").focus();
     $("pTotal").select();
     return false;
   }
   if ($("pYx").value!=''&& !isNumber($("pYx").value)) {
     alert("参与外办人员应为整数!");
     $("pYx").focus();
     $("pYx").select();
     return false;
   }
   if ($("pCouncil").value !='' && !isNumber($("pCouncil").value)) {
     alert("参与理事人数应为整数!");
     $("pCouncil").focus();
     $("pCouncil").select();
     return false;
   }
   if ($("pGuest").value !='' &&!isNumber($("pGuest").value)) {
     alert("参与外宾人数应为整数!");
     $("pGuest").focus();
     $("pGuest").select();
     return false;
   }
   return true;
}

function doOnload() {
  getActiveType();
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
  var seqId = '<%=seqId%>';
  if(parent.projId && parent.projId != ''){
    seqId = parent.projId;
  }
  if(seqId != ''){
    parent.projId = seqId;
    //给其他tab页放开 
    var tabs = parent.tabs;
    showTabs(tabs);
    getProjectById(seqId);
  }
}
function showTabs(tabs){
  tabs.setDisable(false , 1);
  tabs.setDisable(false , 2);
  tabs.setDisable(false , 3);
  tabs.setDisable(false , 4);
}
function doInit(){
  if(checkForm()){
    getContent('projList','PROFSYS_LIST_CONTENT');
    getContent('projNote','PROFSYS_NOTE_CONTENT');
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
     return ;
    }
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectAct/addUpdateProject.act"; 
    var json=getJsonRs(requestURL,pars); 
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      var prc = json.rtData;
      parent.projId = prc.seqId;
      if(!isDel){
        alert("保存成功!");
      }
      window.location.reload();
    }
  }
}
function doProjStatus(projStatus) {
  //自动 判断状态
  var selectObj = $("projStatus");
  var curDateStr = '<%=curDateStr%>';
  var projArriveTime = $("projStartTime").value;
  var projLeaveTime = $("projEndTime").value;
  var projStatusStr = "";
  if(projArriveTime != '' && projLeaveTime != '' ){
    if(curDateStr>projLeaveTime){
      projStatusStr = "已接待";
    }else if(curDateStr<projArriveTime){
      projStatusStr = "准备中";
    }else{
      projStatusStr = "接待中";
    }
  }
  //alert("取出时间" + $("projStartTime").value);
  //alert("当前时间" +statrTime);
  var myOption = document.createElement("option");
  if(projStatus =='1'){
    myOption.value = "1";
    myOption.text = "已结束";
  }else{
    myOption.value = "0";
    myOption.text = projStatusStr;
  }
  selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
}
function getProjectById(seqId){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/in/YHInProjectAct/getProjectById.act?seqId=" + seqId; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData; 
  if(prc.seqId){
    var seqId = prc.seqId;
    $("projNum").value = prc.projNum;
    $("printStatus").value = prc.printStatus;

    $("budgetId").value = prc.budgetId;
    $("projGroupName").value = prc.projGroupName;
    $("projTitle").value = prc.projTitle;
    $("projStartTime").value = prc.projStartTime.substr(0,10);
    $("projEndTime").value = prc.projEndTime.substr(0,10);
    doProjStatus(prc.projStatus);
    $("countryTotal").value = prc.countryTotal;
    $("projLeader").value = prc.projLeader;
    $("projLeaderName").value = prc.projLeaderName;
    $("pTotal").value = prc.pTotal;
    $("pYx").value = prc.pYx;
    $("pCouncil").value = prc.pCouncil;
    $("pGuest").value = prc.pGuest;
    $("purposeCountry").value = prc.purposeCountry;
    $("projOrganizer").value = prc.projOrganizer;
    $("projOperator").value = prc.projOperator;
    $("projSponsor").value = prc.projSponsor;
    
    $("PROFSYS_LIST_CONTENT").value = prc.projList;//FCK
    $("PROFSYS_NOTE_CONTENT").value = prc.projNote;//FCK
    $("attachmentName").value = prc.attachmentName;
    $("attachmentId").value = prc.attachmentId;
    if(prc.attachmentId != ''){
      doOnloadFile();
    }
  }
}
var budgetIdField = null;
var budgetNameField = null;
function toBudget(budgetId,budgetName){
  budgetIdField = budgetId;
  budgetNameField = budgetName;
  var URL= contextPath + "/subsys/oa/profsys/budgetlist.jsp";
  openDialogResize(URL , 650, 500);
 // window.open(URL,this,"height=355px,width=320px,directories=no,menubar=no,toolbar=no,status=no,scrollbars=yes,location=no,top="+loc_y+",left="+loc_x+"");
}
function getActiveType(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectAct/getCodeItem.act?classNo=PROJ_ACTIVE_TYPE2"; 
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
var userRetNameArray=null;var deptRetNameArray=null;var roleRetNameArray=null;var userExternalRetNameArray=null;
function selectCounrty(retArray,moduleId,privNoFlag,privOp){
  roleRetNameArray=retArray;
  var url=contextPath+"/subsys/oa/profsys/getCountry.jsp";
  var has=false;if(moduleId){url+="?moduleId="+moduleId;
  if(!privNoFlag){
    privNoFlag=0;
  }
  url+="&privNoFlag="+privNoFlag;has=true;}
  if(privOp){
    if(has){
      url+="&privOp="+privOp;
    }else{
      url+="?privOp="+privOp;
    }
  }
 openDialog(url,280,400);
}
</script>
</head>
<body onLoad="doOnload();">
 <form action="#" method="post" name="form1" id="form1"  onSubmit="return checkForm();">

 <table class="TableBlock" border="0" width="80%" align="center">
    <tr>
      <td nowrap class="TableContent" width="90">活动编号：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" name="projNum" id="projNum" value="" size=20 maxLength="20">
       </td>
      <td nowrap class="TableContent">活动状态：</td>
      <td nowrap class="TableData">
        <select name="projStatus" id="projStatus">
         </select>
           &nbsp;<font color="red">注：根据时间自动判断</font> 
      </td>               
    </tr>    <tr>
      <td nowrap class="TableContent" width="90">活动名称：<span style="color:red">*</span></td>
      <td nowrap class="TableData" colspan="3">
      <input type="hidden" id="budgetId" name="budgetId" value="24" >
      <input type="text" class="BigStatic" name="projGroupName" id="projGroupName" value="" size=40>
      <a href="#" onclick="toBudget('budgetId','projGroupName')">选择数据</a>  
    </td>
    </tr>
    <tr>
    <td nowrap class="TableContent">活动主题：</td>
      <td nowrap class="TableData"  colspan="3">
         <input type="text" name="projTitle" id="projTitle" value="" size=40>
      </td> 
    <tr>
    <td nowrap class="TableContent">活动类别：</td>
      <td nowrap class="TableData">
        <select id="projActiveType" name="projActiveType">
         <option value="">请选择类别</option>
        </select>
      </td>
     <td nowrap class="TableContent" width="90">负责人：<span style="color:red">*</span></td>
      <td nowrap class="TableData">
      <input type="hidden" name="projLeader" id="projLeader"  value="">
      <input type="text" name="projLeaderName" id="projLeaderName" value=""  class="BigStatic" readonly>
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['projLeader','projLeaderName']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('projLeader').value='';$('projLeaderName').value='';">清空</a>
    </td> 
    </tr> 
      <tr>
      <td nowrap class="TableContent">开始时间：<span style="color:red">*</span></td>
      <td nowrap class="TableData"> 
        <INPUT type="text" readonly name="projStartTime" id="projStartTime" class=BigInput size="10" value="<%=sf.format(new Date())%>">
        <img src="<%=imgPath%>/calendar.gif" align="absMiddle" id="date1" name="date1" border="0" style="cursor:pointer">
      </td>
      <td nowrap class="TableContent">结束时间：<span style="color:red">*</span></td>
      <td nowrap class="TableData">  
        <INPUT type="text" readonly name="projEndTime" id="projEndTime" class=BigInput size="10" value="<%=sf.format(new Date()) %>">
        <img src="<%=imgPath%>/calendar.gif" id="date2" name="date2" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
     <tr>
      <td nowrap class="TableContent">参与国家：</td>
      <td nowrap class="TableData"  colspan="3">
        <input type="hidden" name="purposeCountryId" id="purposeCountryId" value="">
        <textarea name="purposeCountry" id="purposeCountry" cols="40" rows="2" style="overflow-y:auto;" wrap="yes"></textarea>
  <a href="javascript:;" class="orgAdd" onClick="javascript:selectCounrty(['purposeCountryId','purposeCountry']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('purposeCountry').value='';$('purposeCountryId').value='';">清空</a>
        <br><font color="red">注：如需手动填写，请用顿号（、）隔开，形如：法国、英国、美国</font>
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
      <td nowrap class="TableContent">主办单位：</td>
      <td class="TableData" colspan="3">
         <textarea name="projOrganizer" id="projOrganizer"  cols="40" rows="2" style="overflow-y:auto;" class="BigInput" wrap="yes"></textarea>   
         </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">承办单位：</td>
      <td class="TableData" colspan="3">
          <textarea name="projOperator" id="projOperator" cols="40" rows="2" style="overflow-y:auto;" class="BigInput" wrap="yes"></textarea>  
         </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">支持单位：</td>
      <td class="TableData" colspan="3">
          <textarea name="projSponsor" id="projSponsor" cols="40" rows="2" style="overflow-y:auto;" class="BigInput" wrap="yes"></textarea>  
         </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">活动人员构成：</td>
      <td nowrap class="TableData" colspan="3">
<div><script language=JavaScript>    
         oFCKeditor.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
         oFCKeditor.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
         oFCKeditor.Height = "300px";
         oFCKeditor.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
         oFCKeditor.ToolbarSet="DiaryBar";
         oFCKeditor.Create();
         </script>
      <input type="hidden" id="projList" name="projList" value="">
</div>
     </td>
    </tr> 
    <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3">
<div><script language=JavaScript>    
         oFCKeditorNote.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
         oFCKeditorNote.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
         oFCKeditorNote.Height = "300px";
         oFCKeditorNote.SkinPath = oFCKeditor.BasePath + 'skins/silver/' ; 
         oFCKeditorNote.ToolbarSet="DiaryBar";
         oFCKeditorNote.Create();
         </script>
      <input type="hidden" id="projNote" name="projNote" value="">
</div>
</td>
    </tr>
      <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData" colspan="3">

      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId"></input>
        <span id="attr">无附件</span>
      </td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData" colspan="3">
        <input type="hidden" id="moduel" name="moduel" value="profsys">
       <script>ShowAddFile();ShowAddImage();</script>
	   <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
       <input type="hidden" id="ATTACHMENT_ID_OLD"  name="ATTACHMENT_ID_OLD" value="">
	   <input type="hidden" id="ATTACHMENT_NAME_OLD"  name="ATTACHMENT_NAME_OLD" value="">	   
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
      <input type="hidden" name="OP" value="">
      <input type="hidden" name="projType" id="projType" value="2">
      <input type="hidden" name="seqId" id="seqId" value="<%=seqId %>">
      <input type="hidden" name="printStatus" id="printStatus" value="0">
      <input type="hidden" name="dtoClass" value="yh.subsys.oa.profsys.data.YHProject">
      <input type="button" value="保存" class="BigButton"  onclick="doInit();">&nbsp;
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();   parent.window.close()">&nbsp;
      </td>
  </tr>
 </table>
</form>
<form id="formFile" action="<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectCalendarAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe> 
</body>
</html>
