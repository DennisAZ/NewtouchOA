<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>添加贵宾信息</title>
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
<script Language="JavaScript"> 
var upload_limit=1,limit_type="php,php3,php4,php5,";	
var oFCNote = new FCKeditor('guestNote');
//图片插入到正文
function getContent(id,fckId){
  var oEditor = FCKeditorAPI.GetInstance(fckId) ;
  $(id).value=oEditor.GetXHTML();
}
function InsertImage(src) { 
  var oFCNote = FCKeditorAPI.GetInstance('guestNote') ; //FCK实例 
  if (oFCNote.EditMode == FCK_EDITMODE_WYSIWYG ) { 
    oFCNote.InsertHtml( "<img src = '"+ src + "'/>") ; 
  } 
}
function checkForm() {
  if ($("guestNum").value == "") {
    alert("贵宾编号必填!");
    $("guestNum").focus();
    $("guestNum").select();
    return false;
  }
  if ($("guestType").value == "") {
    alert("贵宾类型必填!");
    $("guestType").focus();
    $("guestType").select();
    return false;
  }
  if ($("guestName").value == "") {
    alert("贵宾名称必填!");
    $("guestName").focus();
    $("guestName").select();
    return false;
  }
  if ($("guestUnit").value == "") {
    alert("贵宾所在单位必填!");
    $("guestUnit").focus();
    $("guestUnit").select();
    return false;
  }
  if($("guestAttendTime").value>$("guestLeaveTime").value){
    alert("来会时间不能大于离会时间!");
    $("guestLeaveTime").focus();
    $("guestLeaveTime").select();
    return false;
  }
  return true;
}
var isUploadBackFun = false;
function doInit() {
  if (checkForm()) {
    getContent('guestNote','guestNote');
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
     return ;
    }
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/guest/act/YHGuestAct/addUpdateGuest.act"; 
    var json=getJsonRs(requestURL,pars); 
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      var prc = json.rtData;
       alert("保存成功!");
      window.location.reload();
    }
  }
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
  attachMenuUtil("attr","guest",null,$('attachmentName').value ,$('attachmentId').value,false);
  removeAllFile();
  if (isUploadBackFun) {
    doInit();
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
  return true;
}
function LoadWindow1(kname,codeid){
  URL="../codedefine.php?kname="+kname+"&codeid="+codeid;
  myleft=(screen.availWidth-650)/2;
  window.open(URL,"formul_edit","height=300,width=550,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}
function doOnload() {
//时间
  getQuestType();
  var parameters = {
      inputId:'guestAttendTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
  var parameters = {
      inputId:'guestLeaveTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
}
function getQuestType(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectAct/getCodeItem.act?classNo=GUEST_TYPE"; 
  var json=getJsonRs(requestURL); 
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prcs = json.rtData; 
  var selectObj = $("guestType");
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
</script>
</head>
<body  topmargin="5" onload="doOnload();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small" style="padding:10px;">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3"> 添加贵宾信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<form  action="#"  method="post" id="form1" name="form1">
    <input type="hidden" name="dtoClass" value="yh.subsys.oa.guest.data.YHGuest">
            <input type="hidden" id="moduel" name="moduel" value="guest">
<table class="TableBlock" width="80%" align="center">
    <tr>
  		<td nowrap class="TableContent" >贵宾编号：<span style="color:red">*</span></td>
  	  <td nowrap class="TableData">
  	  	<input type="text" class="BigInput" id="guestNum" name="guestNum" size=20 value="">
  	  </td>  	  	
  		<td nowrap class="TableContent" >贵宾类型：<span style="color:red">*</span></td>
  	  <td nowrap class="TableData">
  	  	<select id="guestType" name="guestType">
        </select>
贵宾类型可在
“系统管理”->“系统代码设置”模块设置 
    </td>  	
  	</tr>
    <tr>
    	<td nowrap class="TableContent">贵宾姓名：<span style="color:red">*</span></td>
  	  <td nowrap class="TableData">
  	  	<input type="text" class="BigInput" id="guestName" name="guestName" size=20>
  	  </td>
  		<td nowrap class="TableContent" width="90">是否用餐：</td>
  	  <td class="TableData">
  	  <select id="guestDiner" name="guestDiner">
        <option value="0">否</option>
        <option value="1">是</option>
      </select>
      </td>    	
	  </tr>
	  <tr>
   		<td nowrap class="TableContent">贵宾所在单位：<span style="color:red">*</span></td>
  	  <td nowrap class="TableData">
  	  	<input type="text" id="guestUnit" class="BigInput" name="guestUnit" size=20>
  	  </td>
  		<td nowrap class="TableContent" >贵宾联系电话：</td>
  	  <td nowrap class="TableData">
  	  <input type="text" id="guestPhone" name="guestPhone" size="20" class="BigInput">
       </td> 
    </tr>   
	  <tr>
  		<td nowrap class="TableContent">来会时间：</td>
      <td nowrap class="TableData">  
        <INPUT type="text" readonly id="guestAttendTime" name="guestAttendTime" class=BigInput size="10">
        <img id="date1" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
      <td nowrap class="TableContent">离会时间：</td>
      <td nowrap class="TableData">  
        <INPUT type="text" readonly id="guestLeaveTime" name="guestLeaveTime" class=BigInput size="10">
        <img id="date2" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
   		<td nowrap class="TableContent">数据录入人：</td>
  	  <td nowrap class="TableData" colspan="3">
  	    <input type="hidden" id="guestCreator" name="guestCreator" value="">
  	  <input type="text" id="guestCreatorName" name="guestCreatorName" size="20" class="BigInput" readonly>
    <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['guestCreator','guestCreatorName']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('guestCreator').value='';$('guestCreatorName').value='';">清空</a>
	  </td> 
  	</tr>
  	<tr>
  		<td nowrap class="TableContent">接待部门：</td>
  	  <td class="TableData" colspan="3">
			<input type="hidden" id="guestDept" name="guestDept">
        <textarea cols="40" id="guestDeptName" name="guestDeptName" rows="2" style="overflow-y:auto;" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['guestDept','guestDeptName']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('guestDept').value='';$('guestDeptName').value='';">清空</a>
  	  </td>
  	</tr>   
  	<tr>
  		<td nowrap class="TableContent">备注：</td>
  	  <td class="TableData" colspan="3">
		 <div><script language=JavaScript>    
         oFCNote.Config["CustomConfigurationsPath"] = contextPath + "/core/funcs/diary/js/fckconfig.js"; 
         oFCNote.BasePath = "/yh/core/js/cmp/fck/fckeditor/";  
         oFCNote.Height = "150px";
         oFCNote.SkinPath = oFCNote.BasePath + 'skins/silver/' ; 
         oFCNote.ToolbarSet="DiaryBar";
         oFCNote.Create();
         </script>
      </div>
      <input type="hidden" id="guestNote" name="guestNote">
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
       <script>ShowAddFile();ShowAddImage();</script>
	   <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script>
       <input type="hidden" id="ATTACHMENT_ID_OLD"  name="ATTACHMENT_ID_OLD" value="">
	   <input type="hidden" id="ATTACHMENT_NAME_OLD"  name="ATTACHMENT_NAME_OLD" value="">	   
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="5" nowrap>
        <input type="hidden" name="seqId" id="seqId" value="">
        <input type="button" value="保存" class="BigButton" onclick="doInit();">&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
				<input type="button" value="返回" class="BigButton" onclick="history.go(-1);">
		</td>
    </tr>
  </table>
  
  <input type="hidden" name="FORMAT" value="0">
</form>
 <form id="formFile" action="<%=contextPath%>/yh/subsys/oa/guest/act/YHGuestAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe width="0" height="0" name="commintFrame" id="commintFrame"></iframe>
</body>
</html>
