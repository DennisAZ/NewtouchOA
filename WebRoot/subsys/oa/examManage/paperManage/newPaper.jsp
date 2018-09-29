<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>试卷管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/paperManageLogic.js"></script>
<script type="text/javascript">

function doInit(){
  var mgrSec = new SelectMgr();
  mgrSec.addSelect({cntrlId: "roomId"
              , tableName: "EXAM_QUIZ_SET"
              , codeField: "SEQ_ID"
              , nameField: "ROOM_NAME"
              , value: "0", isMustFill: "1"
              , filterField: " "
              , filterValue: ''
              , order: ""
              , reloadBy: ""
              , actionUrl: ""
              });
  mgrSec.loadData();
  mgrSec.bindData2Cntrl();
  
}

function doSubmit(){
  var reg = /^[0-9]*$/;
  if($("paperTitle").value.trim() == ""){ 
    alert("试卷标题不能为空！");
    $("paperTitle").focus();
    $("paperTitle").select();
    return false;
  }
  if($("paperGrade").value.trim() == ""){ 
    alert("试卷总分不能为空！");
    $("paperGrade").focus();
    $("paperGrade").select();
    return false;
  }
  if(!reg.test($("paperGrade").value)){
  	alert("试卷总分只能输入整数！");
  	$("paperGrade").focus();
  	$("paperGrade").select();
  	return false;
  }
  if($("questionsCount").value.trim() == ""){ 
    alert("试题数量不能为空！");
    $("questionsCount").focus();
    $("questionsCount").select();
    return false;
  }
  if($("questionsCount").value == "0"){ 
    alert("试题数量不能为零！");
    $("questionsCount").focus();
    return false;
  }
  if(!reg.test($("questionsCount").value)){
  	alert("试题数量只能输入整数！");
  	$("questionsCount").focus();
  	$("questionsCount").select();
  	return false;
  }
  if($("paperTimes").value.trim() == ""){ 
    alert("考试时长不能为空！");
    $("paperTimes").focus();
    $("paperTimes").select();
    return false;
  }
  if(!reg.test($("paperTimes").value)){
  	alert("考试时长只能输入整数！");
  	$("paperTimes").focus();
  	$("paperTimes").select();
  	return false;
  }
  if($("roomId").value == ""){ 
    alert("所属题库不能为空！");
    $("roomId").focus();
    return false;
  }

  //var paperSeqId = getPaperSeqId();
  //var roomId = getRoomId(paperSeqId);
  var roomId = $("roomId").value;
  var questionsCount = $("questionsCount").value;
  var data = isCount(roomId, questionsCount);
  if(data == "1"){
    alert("错误 ,所选试题数量超出范围！");
    $("questionsCount").focus();
    $("questionsCount").select();
    return false;
  }else{
    doCommit();
  }

}

function doCommit(){
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/examManage/act/YHExamPaperAct/addPaper.act";
  var rtJson = getJsonRs(url,pars);
  if(rtJson.rtState == "0"){
    window.location.href = "<%=contextPath %>/subsys/oa/examManage/paperManage/paperDetails.jsp";
  }else{
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;新建试卷</span>
    </td>
  </tr>
</table>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1">
  <table class="TableList" width="70%"  align="center">
   <tr class="TableHeader">
      <td colspan="4" nowrap>
                  基本信息
      </td>
 </tr>
    <tr>
      <td width="11%" nowrap class="TableData">试卷标题：<font style='color:red'>*</font></td>
      <td width="39%" class="TableData">
        <input type=text name="paperTitle" id="paperTitle" class="BigInput" size=30 maxlength="200" value="">
      </td>
      <td width="15%" nowrap class="TableData">试卷总分：<font style='color:red'>*</font></td>
      <td width="35%" class="TableData">
        <input type=text name="paperGrade" id="paperGrade" class="BigInput" size=10 maxlength="13" value="">
      </td>
    </tr>
 <tr>
      <td nowrap class="TableData">试题数量：<font style='color:red'>*</font></td>
      <td class="TableData">
        <input type=text name="questionsCount" id="questionsCount" class="BigInput" size=10 maxlength="13" value="">
      </td>
      <td nowrap class="TableData">考试时长：<font style='color:red'>*</font></td>
      <td class="TableData">
        <input type=text name="paperTimes" id="paperTimes" class="BigInput" size=10 maxlength="3" value=""> 分钟
      </td>
    </tr>
 <tr>
      <td nowrap class="TableData"> 所属题库：</td>
      <td class="TableData">
        <select name="roomId" id="roomId">

        </select>
      </td>
      <td nowrap class="TableData">试卷说明：</td>
      <td class="TableData">
        <textarea name="paperDesc" id="paperDesc" class="BigInput" cols="30" rows="2"></textarea>
      </td>
    </tr>
   <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
        <input type="button" value="保存" class="BigButton" onClick="doSubmit()">
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>