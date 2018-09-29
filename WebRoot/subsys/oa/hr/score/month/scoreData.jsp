<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>考核录入</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/scoreItemLogic.js"></script>
<script type="text/javascript">
var seqIdStr = "";
function doInit(){
  //var flowId = ${param.flowId};
  var userId = ${param.userId};
  var groupId = ${param.groupId};
  $("userNameId").value = ${param.userId};
  if($("userNameId") && $("userNameId").value.trim()){
  	bindDesc([{cntrlId:"userNameId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreFlowAct/getScoreDataInsert.act?groupId=${param.groupId}";
  var rtJson = getJsonRs(url);
  var flag = 0;
  if(rtJson.rtState == "0"){
    var table = new Element('table',{ "width":"60%","class":"TableList","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader'>"
				+"<td nowrap width='' align='center'>考核项目</td>"
				+"<td nowrap width='' align='center'>分数</td>"				
				+"<td nowrap width='' align='center'>批注</td></tr><tbody>");
		$('listDiv').appendChild(table);
  	for(var i = 0; i < rtJson.rtData.length; i++){
      flag++;
      var seqId = rtJson.rtData[i].seqId;
      seqIdStr += seqId + ","; 
  	  var itemName = rtJson.rtData[i].itemName;
  	  var groupId = rtJson.rtData[i].groupId;
  	  var min = rtJson.rtData[i].min.trim();
  	  var max = rtJson.rtData[i].max.trim();
  	  var re1 = /\'/gi; 
  	  itemName = itemName.replace(re1,"&lsquo;"); 
  	  var tr=new Element('tr',{'class':'TableLine1'});			
			table.firstChild.appendChild(tr);
			tr.update("<td align='center'><span id='itemName_"+seqId+"'></span>(<span id='min_"+seqId+"'></span>～<span id='max_"+seqId+"'></span>)</td>" 
				+ "<td align='center'><input type='text' name='score_"+seqId+"' id='score_"+seqId+"' class='SmallInput' size='3' maxlength='5' value=''></td>" 
				+ "<td align='center'><input type='text' name='memo_"+seqId+"' id='memo_"+seqId+"' class='SmallInput' size='30' maxlength='14' value=\" \" ></td>"
			);
			$("min_"+seqId).innerHTML = min;
			$("max_"+seqId).innerHTML = max;
			$("itemName_"+seqId).innerHTML = itemName;
  	}
  	var tr1=new Element('tr',{'class':'TableLine1'});
		table.firstChild.appendChild(tr1);
		tr1.update("<td align='left' colspan='3' >"
				+ getGroupRefer()
				+ "</td>"
		);
  	var data = getScoreOperationData(groupId, userId);
  	if(data == "1"){
  	  getScoreDataStr();
    }
    if(flag == "0"){
      $("buttonDiv").style.display = 'none';  
      $("infoDiv").style.display = '';
    }
  }else{
  	alert(rtJson.rtMsrg); 
  }
}

function getGroupRefer(){
	var str = "";
	var userNameIdDesc = "";
	if($("userNameIdDesc").innerHTML){
		userNameIdDesc = $("userNameIdDesc").innerHTML; 
	}
	var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreShowAct/getGroupRefer.act?groupId=${param.groupId}";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var prcs = rtJson.rtData;
		var groupRefer = prcs.groupRefer;
		if(groupRefer == "both" ){
			str = "<a href=\"javascript:;\" onClick=\"window.open('<%=contextPath %>/core/funcs/diary/info/userDiary.jsp?userId=${param.userId}','','height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=60,resizable=yes');\">查看" + userNameIdDesc +"工作日志</a>&nbsp;&nbsp;"
					+ "<a href=\"javascript:;\" onClick=\"window.open('<%=contextPath%>/subsys/oa/hr/score/submit/arrangeWork/index.jsp?userId=${param.userId}','','height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=60,resizable=yes');\">查看" + userNameIdDesc +"工作安排</a>&nbsp;&nbsp;";
		}
		if(groupRefer == "diary" ){
			str = "<a href=\"javascript:;\" onClick=\"window.open('<%=contextPath %>/core/funcs/diary/info/userDiary.jsp?userId=${param.userId}','','height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=60,resizable=yes');\">查看" + userNameIdDesc +"工作日志</a>";
		}
		if(groupRefer == "calendar" ){
			str = "<a href=\"javascript:;\" onClick=\"window.open('<%=contextPath%>/subsys/oa/hr/score/submit/arrangeWork/index.jsp?userId=${param.userId}','','height=500,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=60,resizable=yes');\">查看" + userNameIdDesc +"工作安排</a>&nbsp;&nbsp;";
		}
	}else{
		alert(rtJson.rtMsrg);
	}
	return str;
}


function getScoreDataStr(){
  var groupId = ${param.groupId};
  var userId = ${param.userId};
  var seqStr = seqIdStr.split(',');
  var score = "";
  var memo = "";
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreShowAct/getScoreDataStr.act?groupId=${param.groupId}";
  var rtJson = getJsonRs(url, "userId="+userId);
  if(rtJson.rtState == "0"){
    for(var i = 0; i < rtJson.rtData.length; i++){
      var seqId = rtJson.rtData[i].seqId;
      score = rtJson.rtData[i].score;
      memo = rtJson.rtData[i].memo;
    }
    var memoStr = memo.split(',');
    var scoreStr = score.split(',');
    for(var x = 0; x < seqStr.length-1; x++){
      var seqId = seqStr[x];
      $("memo_"+seqId).value = memoStr[x] || '';
      $("score_"+seqId).value = scoreStr[x] || '';
    }
  }else{
   alert(rtJson.rtMsrg); 
  }
}

function doSubmit(){
  var groupId = ${param.groupId};
  var userId = ${param.userId};
  var seqStr = seqIdStr.split(',');
  var data = getScoreOperationData(groupId, userId);
  var score = "";
  var memo = "";
  for(var i = 0; i < seqStr.length-1; i++){
    var seqId = seqStr[i];
    if($("score_"+seqId).value){
    	var itemName = $("itemName_"+seqId).innerHTML;
      if(!isNumbers($("score_"+seqId).value)){
      	alert("格式错误,"+itemName+" 输入的分值 应为100.00");
        $("score_"+seqId).select()
        $("score_"+seqId).focus();
        return;
      }
      if(parseInt($("score_"+seqId).value) > parseInt($("max_"+seqId).innerHTML) || parseInt($("score_"+seqId).value) < parseInt($("min_"+seqId).innerHTML)){
        alert("错误,"+itemName+" 输入的分值超出范围");
        $("score_"+seqId).select()
        $("score_"+seqId).focus();
        return;
      }
    }
    score += $("score_"+seqId).value + ",";
    memo += $("memo_"+seqId).value.replace(/,/g,"，") + ",";
  }
  
  if(data == "1"){
    update(score,memo);
  }else{
    add(score,memo);
  }
  parent.file_tree.location.reload();
}

function add(score,memo){
  var checkFlag = ${param.checkFlag};
  //var flowId = ${param.flowId};
  var groupId = ${param.groupId};
  var year = ${param.year};
  var month = ${param.month};
  var userId = ${param.userId};
  var requestURL = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreShowAct/addScoreData.act";
  var rtJson = getJsonRs(requestURL,"groupId="+groupId+"&participant="+userId+"&score="+score+"&memo=" + encodeURIComponent(memo) + "&checkFlag="+checkFlag+"&year="+year+"&month="+month);
  if (rtJson.rtState == '1') { 
    alert(rtJson.rtMsrg); 
    return ; 
  } else {
    window.location.reload();
  }
}

function update(score,memo){
  var checkFlag = ${param.checkFlag};
  //var flowId = ${param.flowId};
  var groupId = ${param.groupId};
  var userId = ${param.userId};
  var requestURL = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreShowAct/updateScoreData.act";
  var rtJson = getJsonRs(requestURL,"groupId="+groupId+"&participant="+userId+"&score="+score+"&memo=" + encodeURIComponent(memo) + "&seqIdStr="+seqIdStr+"&checkFlag="+checkFlag);
  if (rtJson.rtState == '1') { 
    alert(rtJson.rtMsrg); 
    return ; 
  } else {
    window.location.reload();
  }
}

function getScoreOperationData(groupId, userId){
  var year = ${param.year};
  var month = ${param.month};
  var url = "<%=contextPath %>/yh/subsys/oa/hr/score/act/YHScoreShowAct/getOperationFlag.act?seqId=${param.seqId}";
  var rtJson = getJsonRs(url, "groupId="+groupId+"&userId="+userId+"&year="+year+"&month="+month);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    return data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}
function toCalendar(userId){
  var URL = "<%=contextPath%>/subsys/oa/hr/score/submit/arrangeWork/index.jsp?userId="+userId;
  myleft = (screen.availWidth-780)/2 ;
  window.open(URL,"read_vote","height=600,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}

function returnBlank(){
  //var flowId = ${param.flowId};
  var groupId = ${param.groupId};
  location = "<%=contextPath%>/subsys/oa/hr/score/month/blank.jsp?groupId="+groupId;
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">
    	<input type="hidden" name="userNameId" id="userNameId" value="">
    	<img src="<%=imgPath%>/salary.gif" align="middle" width=22 height=20><span class="big3">&nbsp;考核数据录入(<span id="userNameIdDesc" ></span>)</span>
    </td>
  </tr>
</table>
<div id="buttonDiv">
 <div id="listDiv" align="center"></div>
    <form method="post" name="form1" id="form1" action="" onSubmit="">
    <table width="60%" align="center" class="TableList">
 <tfoot class="TableFooter">
    <td colspan="4">
   <input type="hidden" name="seqId" id="seqId">  
   <div align="center">
     <input type="button" name="submit" class="BigButton" value="添加" onclick="doSubmit();">&nbsp;&nbsp;
     <input type="button" name="submit" class="BigButton" value="取消" onclick="returnBlank();">
     </div>
     </td>
  </tfoot>
  </table>
  </form>
  </div>
<div id="infoDiv" style="display:none">
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">尚未创建考核项目！</div>
    </td>
  </tr>
</table>
</div>
</body>
</html>