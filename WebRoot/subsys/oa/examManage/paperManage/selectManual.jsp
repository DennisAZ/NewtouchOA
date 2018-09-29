<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>手工选题</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/paperManageLogic.js"></script>
<script type="text/javascript">

function doInit(){
  var questionsRank = "${param.questionsRank}";
  var questionsType = "${param.questionsType}";
  $("seqId").value = "${param.paperSeqId}";
  var questionsList = getQuestionsList($("seqId").value);
  var count = 0;
  var count2 = 0;
  var questionsListStr = questionsList.split(',');
  for(var x = 0; x < questionsListStr.length; x++){
    count2++;
  }
  var url = "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamPaperAct/getSelectManualJson.act?roomId=${param.roomId}";
  var rtJson = getJsonRs(url, "questionsType="+questionsType+"&questionsRank="+questionsRank);
  if(rtJson.rtState == "0"){
    var table = new Element('table',{ "width":"100%","class":"TableBlock","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
				+"<td nowrap width='40' align='center'>选择</td>"
				+"<td nowrap width='80' align='center'>题型</td>"				
				+"<td nowrap align='center'>难度</td>"				
				+"<td nowrap align='center'>题目</td></tr><tbody>");
		$('listDiv').appendChild(table);

  	for(var i = 0; i < rtJson.rtData.length; i++){
  	  count++;
      var seqId = rtJson.rtData[i].seqId;
  	  var questionsType = rtJson.rtData[i].questionsType;
  	  var questionsRank = rtJson.rtData[i].questionsRank;
  	  var questions = rtJson.rtData[i].questions;
      if(questionsType == "0"){
        questionsType = "单选";
      }else{
        questionsType = "多选";
      }

      if(questionsRank == "0"){
        questionsRank = "低";
      }else if(questionsRank == "1"){
        questionsRank = "中";
      }else{
        questionsRank = "高";
      }
  	  var tr = new Element('tr',{'class':'TableLine1'});			
			table.firstChild.appendChild(tr);
			tr.update("<td align='center'>"					
			  + "<input type='checkbox' id='deleteFlag_"+seqId+"' name='deleteFlag' value="+seqId+"></td><td align='center'>"
				+ questionsType + "</td><td align='center'>"					
				+ questionsRank + "</td><td align='center'>"					
				+ questions + "</td>"		
				+ "<input type='hidden' id='text_"+seqId+"' value="+seqId+">"				
			);

	  var questionsListStr = questionsList.split(',');
      for(var x = 0; x < questionsListStr.length; x++){
        if(seqId == questionsListStr[x]){
          $("deleteFlag_"+seqId).checked = true;
        }
      }
  	}
   if(count == "0"){
     $("numCount").innerHTML = count;
     $("isCount").innerHTML = count;
     $("questionsCount").innerHTML = count;
     $("countDiv").style.display = "none";
   }else{
     $("numCount").innerHTML = count;
     $("isCount").innerHTML = count2;
     $("questionsCount").innerHTML = "${param.questionsCount}";
   }
   
  }else{
  	alert(rtJson.rtMsrg); 
  }
  if(count == 0){
    var div = document.getElementById("noCheck");
    div.style.display = "";
    var allTable = document.getElementById("allTable");
    allTable.style.display = "none";
  }
}

function doSubmit(){
  var textFlag = "";
  var idStrsVal = checkMags('deleteFlag');
  if (!idStrsVal) {
    alert("请至少选择其中一个。");
    return;
  }
  var ids = idStrsVal.split(',');
  var counts = 0;
  for(var i = 0; i < ids.length; i++){
    counts++;
  }
  if(counts > $("questionsCount").innerHTML){
    alert("已选题目的数量为 " + counts + "，超过预设题目数量 " + $("questionsCount").innerHTML + " 的限制。");
    return false;
  }
  
  $("questionsList").value = idStrsVal;
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/examManage/act/YHExamPaperAct/updateSelectManual.act";
  var rtJson = getJsonRs(url,pars);
  if(rtJson.rtState == "0"){
    window.location.reload();
    var parentWindow = window.opener;
    parentWindow.reloadFunc();
    //var paperSeqId = "${param.paperSeqId}";
    //parent.location.reload() = "<%=contextPath %>/subsys/oa/examManage/paperManage/manage.jsp?paperSeqId="+paperSeqId
  }else{
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body onload="doInit();" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" align="absmiddle"><span class="big3"> 手工选题</span><br>
    </td>
    <td id="countDiv" valign="bottom" class="small1" style="display:''">共<span id="numCount"></span>题,已选<span id="isCount"></span>题,最多选<span id="questionsCount"></span>题    </td>
  </tr>
</table>
<br>

<div id="noCheck" style="display:none;">
<table class="MessageBox" align="center" width="330">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">该条件下无已发布的试题</div>
    </td>
  </tr>
</table>
</div>
<div id="allTable" style="display:'';">
<div id="listDiv" align="center"></div>
 <table id="beSortTable" class="TableList" width="100%">
   <tr class='TableControl'>
     <td colspan='10'>
       &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
     </td>
   </tr>
</table>
<div align="center">
<br>
   <input type="button" value="保存" class="SmallButton" title="保存已选试题" id="Submit1" onClick="doSubmit();">
</div>
</div>
<form action=""  method="post" name="form1" id="form1">
 <input type="hidden" name="seqId" id="seqId" value="">
    <input type="hidden" name="roomId" id="roomId" value="">
    <input type="hidden" name="questionsList" id="questionsList" value="">
    <input type="hidden" name="questionsScore" id="questionsScore" value="">
</form>


</body>
</html>