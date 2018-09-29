<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<html>
<head>
<title>投票管理</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
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
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/system/interface/js/interfaceLogic.js"></script>
<script type="text/Javascript">
var beginEnd = "<%=sf.format(new Date())%>";
function doInit() {
  //时间
  var parameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'endDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
}
//是否允许匿名
function onAnonymity() {
  if ($("anonymityCheck").checked) {
    $("anonymity").value = "1";
  } else {
    $("anonymity").value = "0";
  }
}
//置顶
function onTop() {
  if ($("topCheck").checked) {
    $("top").value = "1";
  } else {
    $("top").value = "0";
  }
}
//表单验证
function checkForm() {
  if ($("subjectMain").value == "") {
    alert("总投票的标题不能为空！");
    $("subjectMain").focus();
    $("subjectMain").select();
    return false;
  }
  if ($("subject").value == "") {
    alert("投票的标题不能为空！");
    $("subject").focus();
    $("subject").select();
    return false;
  }
  if ($("toName").value == ""  &&  $("privName").value == "" && $("userName").value == "") {
    alert("请至少指定一种发布范围！");
    $("toName").focus();
    $("toName").select();
    return false;
  }
  if ($("maxNum").value != "0" && !isNumber($("maxNum").value)) {
    alert("最多选择项只能为数字！");
    $("maxNum").focus();
    $("maxNum").select();
    return false;
  }
  if ($("minNum").value != "0" && !isNumber($("minNum").value)) {
    alert("最少选择项只能为数字！");
    $("minNum").focus();
    $("minNum").select();
    return false;
  }
  if ($("maxNum").value != "0" && $("minNum").value != "0" && ($("minNum").value > $("maxNum").value)) {
    alert("最少项不能大于最多项！");
    $("minNum").focus();
    $("minNum").select();
    return false;
  }
  if ($("beginDate").value != "" && $("endDate").value != "" && ($("beginDate").value >= $("endDate").value)) {
    alert("起始时间不能大于等于终止时间！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  if ($("endDate").value != "" && ($("endDate").value <= beginEnd)) {
    alert("终止时间不能小于等于当天时间！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  return true;
}
//类型多选
function changeType() {
  if ($("type").value == "1") {
    $("maxNumDesc").style.display = "";
  }else {
    $("maxNumDesc").style.display = "none";
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
  attachMenuUtil("showAtt","vote",null,$('attachmentName').value ,$('attachmentId').value,false);
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
  return true;
}
//数据添加
function checkForm2() {
  if (checkForm()) {
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      return ;
    }
    
    var actionSize = $('actionSize').value;
    var actionLight = $('actionLight').value;
    var actionFont = $('actionFont').value;
    var actionLights = $('actionLights').value;
    var actionColor = $('actionColor').value;
    var actionFlag = $('actionLightFlag').value;
    
    var subjectFont = "font-family:" + actionFont + ";font-size:" + actionSize + ";color:" + actionColor + ";filter:" + actionFlag + "(Direction=120, color=" + actionLights + ");";
    $("subjectFont").value = subjectFont;
    
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/addVote.act";
    var json = getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      var prc = json.rtData;
      parent.seqId = prc.seqId;
      window.location.href = "<%=contextPath%>/subsys/oa/vote/manage/add.jsp";
    }
  }
}
function showSubject(){
  var actionSize = $('actionSize').value;
  var actionLight = $('actionLight').value;
  var actionFont = $('actionFont').value;
  var actionLights = $('actionLights').value;
  var actionColor = $('actionColor').value;
  var actionFlag = $('actionLightFlag').value;
  
  var subjectFont = "font-family:" + actionFont + ";font-size:" + actionSize + ";color:" + actionColor + ";filter:" + actionFlag + "(Direction=120, color=" + actionLights + ");";
  $('subjectMainShow').innerHTML = $('subjectMain').value;
  $('subjectMainShow').setStyle(subjectFont);
  
  $('subject_tr').style.display = "";
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit()">
<span class="big3">&nbsp;<img src="<%=imgPath %>/vote.gif" align="absmiddle">&nbsp;新建投票</span> 
<br>
<form method="post" name="form1" id="form1">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.vote.data.YHVoteTitle">
<input type="hidden" name="fromId" id="fromId" value="<%=person.getSeqId()%>">
<input type="hidden" name="sendTime" id="sendTime" value="<%=sf.format(new Date()) %>">
<input type="hidden" name="publish" id="publish" value="0">
<input type="hidden" name="voteNo" id="voteNo" value="0">
<input type="hidden" name="parentId" id="parentId" value="0">
<input type="hidden" name="peaders" id="peaders" value="">
<table class="TableBlock" width="70%" align="center">
<tr>
   <td nowrap class="TableData"> 总标题：<font color="red">*</font></td>
   <td class="TableData">
     <input type="text" name="subjectMain" id="subjectMain" size="42" maxlength="100" class="BigInput" value="">
   </td>
</tr>
<tr id="subject_tr" style="display: none;height:50px;">
   <td nowrap class="TableData" >预览</td>
   <td class="TableData">
     <span id="subjectMainShow"></span>
   </td>
</tr>
<tr>
  <td nowrap class="TableData">总标题样式：</td>
  <td nowrap class="TableData">
      <input type="hidden" name="FONT_FAMILY" value="">
      <input type="hidden" name="FONT_SIZE" value="">
      <input type="hidden" name="FONT_COLOR" value="">
      <input type="hidden" name="FONT_FILTER" value="">
  <span style="padding-top:5px;padding-left:10px;">
   <a id="showMeunA" href="#" onclick="showFont(event);"><span id="actionNameFont">字体</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
  </span>
  <span style="padding-top:5px;padding-left:10px;">
   <a id="showMeunB" href="#" onclick="showSize(event);"><span id="actionNameSize">大小</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
  </span>
  <span style="padding-top:5px;padding-left:10px;">
   <a id="showMeunC" href="#" onclick="showColor(event);"><span id="actionNameColor">颜色</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
  </span>
  <span style="padding-top:5px;padding-left:10px;display:none;">
   <a id="showMeunD" href="#" onclick="showLight(event);"><span id="actionNameLight">效果</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
  </span>
  <span style="padding-top:5px;padding-left:10px;"><a href="#" onclick="showSubject();">预览</a></span>
  <input type="hidden" name="actionFont" id="actionFont" value="">
  <input type="hidden" name="actionSize" id="actionSize" value="">
  <input type="hidden" name="actionLight" id="actionLight" value="">
  <input type="hidden" name="actionLights" id="actionLights" value="">
  <input type="hidden" name="actionColor" id="actionColor" value="">
  <input type="hidden" name="actionLightFlag" id="actionLightFlag" value="">
  <input type="hidden" name="subjectFont" id="subjectFont" value="">
  </td>
</tr>
<tr>
   <td nowrap class="TableData"> 标题：<font color="red">*</font></td>
   <td class="TableData">
     <input type="text" name="subject" id="subject" size="42" maxlength="100" class="BigInput" value="">
     	<span style="color: #151515">&nbsp;&nbsp;&nbsp;&nbsp;<b>此处为投票第一项标题</b></span>
   </td>
</tr>
    <tr>
      <td nowrap class="TableData">发布范围（部门）：</td>
      <td class="TableData">
        <input type="hidden" name="toId" id="toId" value="">
        <textarea cols=38 name="toName" id="toName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['toId','toName'] , 7);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="$('toId').value='';$('toName').value='';">清空</a>
        </td>
    </tr>
    <tr>
      <td nowrap class="TableData">发布范围（角色）：</td>
      <td class="TableData">
        <input type="hidden" name="privId" id="privId" value="">
        <textarea cols=38 name="privName" id="privName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectRole(['privId','privName'], 7);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('privId').value='';$('privName').value='';">清空</a>
       </td>
   </tr>
   <tr>
      <td nowrap class="TableData">发布范围（人员）：</td>
      <td class="TableData">
        <input type="hidden" name="userId" id="userId" value="">
        <textarea cols=38 name="userName" id="userName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['userId', 'userName'], 7)">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('userId').value='';$('userName').value='';">清空</a>
      </td>
   </tr>
    <tr>
      <td nowrap class="TableData">投票描述：</td>
      <td class="TableData">
        <textarea name="content" id="content" class="BigInput" cols="50" rows="3"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 类型：</td>
      <td class="TableData">
        <select name="type" id="type" onChange="changeType()">
          <option value="0" >单选</option>
          <option value="1" >多选</option>
          <option value="2" >文本输入</option>
        </select>
        <span id="maxNumDesc" style="display:none">最多允许选择 
        <input type="text" name="maxNum" id="maxNum" value="0" size="2" maxlength="2" class="SmallInput"> 项，&nbsp;最少允许选择 
        <input type="text" name="minNum" id="minNum" value="0" size="2" maxlength="2" class="SmallInput"> 项，0则不限制</span>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 查看投票结果：</td>
      <td class="TableData">
        <select name="viewPriv" id="viewPriv">
          <option value="0" >投票后允许查看</option>
          <option value="1" >投票前允许查看</option>
          <option value="2" >不允许查看</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 匿名投票：</td>
      <td class="TableData">
        <input type="checkbox" name="anonymityCheck" id="anonymityCheck" onClick="onAnonymity();"><label>允许匿名投票</label>
        <input type="hidden" name="anonymity" id="anonymity" value="0">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 有效期：</td>
      <td class="TableData">
        生效日期：<input type="text" name="beginDate" id="beginDate" size="10" maxlength="10" class="BigInput" value="" readonly="readonly">
         <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">        
        为空为立即生效<br>
        终止日期：<input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value="" readonly="readonly">
<img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">    
        为空为手动终止
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 置顶：</td>
      <td class="TableData"><input type="checkbox" name="topCheck" id="topCheck" onClick="onTop();"><label>使投票置顶，显示为重要</label>
<input type="hidden" name="top" id="top" value="0">   
      </td>
    </tr>
    <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData">
      <input type="hidden" id="attachmentName" name="attachmentName" value=""></input>
       <input type="hidden" id="attachmentId" name="attachmentId" value=""></input>
        <span id="showAtt">无附件</span>
      </td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData">
        <input type="hidden" id="moduel" name="moduel" value="vote">
       <script>ShowAddFile();</script>
     <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script> 
      </td>
    </tr>
        <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="保存" class="BigButton" onClick="checkForm2();">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
<form id="formFile" action="<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe> 
</body>
</html>