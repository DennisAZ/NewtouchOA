<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>考核指标集定义</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/scoreLogic.js"></script>
<script type="text/javascript">

function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreGroupAct/getScoreGroupList.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"hidden", name:"groupFlag", text:"顺序号", dataType:"int"},
         {type:"data", name:"groupName",  width: '16%', text:"考核指标集名称", render:scoreCenterFunc},       
         {type:"data", name:"groupDesc",  width: '10%', text:"考核指标集描述", render:scoreCenterFunc},
         {type:"selfdef", text:"操作", width: '24%',render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      $("numDiv").update("共<span class='big4'>&nbsp;" + total + "</span>&nbsp;条")
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无考核指标集', 'msrg');
    }
}

function getHrSetUserLogin(){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/hrManager/act/YHHrSetOtherAct/getHrSetUserLogin.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    if(rtJson.rtData == 1){
      document.getElementById("yesOther1").checked = true;
    }else if(rtJson.rtData == 0){
      document.getElementById("yesOther2").checked = true;
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function doSubmit(){
  if($("groupName").value.trim() == ""){ 
    alert("考核指标集名称不能空！！！");
    $("groupName").focus();
    $("groupName").select();
	return false;
  }
  if($("role").value.trim() == ""){ 
    alert("范围(角色)不能空！！！");
    $("roleDesc").focus();
    $("roleDesc").select();
	return false;
  }
  checkDiary();
  checkCalendar();
  var fill = document.getElementsByName("fillId");
  var fillFlag = "";
  for(var i = 0; i < fill.length; i++){
    if(fill[i].checked)
      fillFlag = fill[i].value;
  } 
  $("groupFlag").value = fillFlag;
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/hr/score/act/YHScoreGroupAct/addScoreGroup.act";
  var rtJson = getJsonRs(url,pars);
  if(rtJson.rtState == "0"){
    window.location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" WIDTH="22" HEIGHT="22" align="absmiddle"><span class="big3"> 新建考核指标集</span><br>
    </td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1">
 <table width="450" align="center" class="TableBlock">
    <tr>
      <td nowrap class="TableData">考核指标集名称：<font style='color:red'>*</font></td>
      <td class="TableData">
         <INPUT type="text" name="groupName" id="groupName" class=BigInput size="20">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">范围(角色)：<font style='color:red'>*</font></td>
      <td class="TableData">
        <input type="hidden" name="role" id="role" value="">
        <textarea cols="30" name="roleDesc" id="roleDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
      </td>
   </tr>
    <tr>
      <td nowrap class="TableData">考核指标集描述：</td>
      <td class="TableData">
        <textarea name="groupDesc" id="groupDesc" cols="45" rows="3" class="BigInput"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">考核指标集明细填写形式：</td>
      <td class="TableData">
       <input type="radio" name="fillId" id="fillId1" value="0" checked><label for="fillId1">直接填写</label>&nbsp;&nbsp;<input type="radio" name="fillId" id="fillId2" value="1"><label for="fillId2">选择</label>    
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">设定考核依据模块：</td>
      <td class="TableData">
       <input type="checkbox" name="diaryId" id="diaryId"><label for="diaryId">个人工作日志</label>&nbsp;&nbsp;<input type="checkbox" name="calendarId" id="calendarId"><label for="calendarId">个人日程安排</label>    
      </td>
    </tr>
    <tfoot align="center" class="TableFooter">
      <td colspan="2" nowrap>
      <input type="hidden" name="groupFlag" id="groupFlag">  
        <input type="button" value="新建" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;
      </td>
    </tfoot>
</table>
</form>
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/score.gif" WIDTH="20" HEIGHT="20" align="absmiddle"><span class="big3"> 考核指标集管理</span><br>
    </td>

  </tr>
</table>
<br>
 <div align="center" id="numDiv"> </div>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">

</div>
<div id="msrg">
</div>
</body>
</html>