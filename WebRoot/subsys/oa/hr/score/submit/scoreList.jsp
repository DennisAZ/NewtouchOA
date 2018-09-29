<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
String flowId = request.getParameter("flowId") == null ? "" :  request.getParameter("flowId");
String userId = request.getParameter("userId") == null ? "" :  request.getParameter("userId");
String currPage =  request.getParameter("currPage") == null ? "" :  request.getParameter("currPage"); 
String checkFlag = request.getParameter("checkFlag") == null ? "" :  request.getParameter("checkFlag");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>考核数据 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript">

var tree = null;
function doOnload(){	
  var flowId='<%=flowId%>';
  var userId = '<%=userId%>';
  var currPage = '<%=currPage%>';
  var url= "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreFlowAct/getScoreOnline.act?flowId=" + flowId + "&userId="+userId+"&currPage="+currPage;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var JsonData = rtJson.rtData;
  var  pageInfo = JsonData.pageInfo;
  var prcs = JsonData.data;
  //alert(rsText);
  
  var data = getData(); 
  var answer = "";
  var memo = "";
  if(data.length>0){
    answer = data[0].answer;
    memo = data[0].memo;
  }
  var answerArray = new Array();
  var memoArray = new Array();
  if(answer != ''){
    answerArray = answer.split(",");
  }
  if(memo != ''){
    memoArray = memo.split(",");
  }
  var newArray = new Array();
  var newMemoArray = new Array();
  var index = 0;
  var pageSize = 5;
  //alert(memoArray);
  for(var i=(parseInt(currPage,10)-1)*pageSize+0; i< (parseInt(currPage,10)-1)*pageSize+pageSize; i++){
    newArray[index] = answerArray[i];
    newMemoArray[index] = memoArray[i];
    index++;
  }
  var seqIdPage = "";
  if(prcs.length>0){
    var table = new Element('table',{ "class":"TableList", "width":"100%"}).update("<tbody id='tbody'></tbody>");
    $("form1").appendChild(table);
     for(var i =0 ;i < prcs.length ; i++){
      var quiz = prcs[i];
      var seqId = quiz.seqId;
      seqIdPage = seqIdPage + seqId + ",";
      var tr = new Element('tr',{"class":"TableHeader"});
      $("tbody").appendChild(tr);
      tr.update( "<td align='left' style='word-break:break-all'>"+(i+1)+ "." + quiz.itemName + "</td>");

      var tr2 = new Element('tr',{});
      $("tbody").appendChild(tr2);
      var quizStr = "";
      var typeStr = "radio";
      if(quiz.answerA != ''){
        quizStr = quizStr +"<input type='"+typeStr+"'   name='ANSWER"+seqId+"' id='ANSWER_A"+seqId+"' value='A'/><label for='ANSWER_A"+seqId+"' >"+quiz.answerA+"</label> &nbsp;<font color='red'>("+quiz.scoreA+"分)</font><br>";
      }
      if(quiz.answerB != ''){
        quizStr = quizStr +"<input type='"+typeStr+"'   name='ANSWER"+seqId+"' id='ANSWER_B"+seqId+"' value='B' /><label for='ANSWER_B"+seqId+"'>"+quiz.answerB+"</label> &nbsp;<font color='red'>("+quiz.scoreB+"分)</font><br>";

      }
      if(quiz.answerC != ''){
        quizStr = quizStr +"<input type='"+typeStr+"'   name='ANSWER"+seqId+"' id='ANSWER_C"+seqId+"' value='C'/><label for='ANSWER_C"+seqId+"' >"+quiz.answerC+"</label> &nbsp;<font color='red'>("+quiz.scoreC+"分)</font><br>";
      }
      if(quiz.answerD != ''){
        quizStr = quizStr +"<input type='"+typeStr+"'   name='ANSWER"+seqId+"' id='ANSWER_D"+seqId+"' value='D'/><label for='ANSWER_D"+seqId+"'>"+quiz.answerD+"</label> &nbsp;<font color='red'>("+quiz.scoreD+"分)</font><br>";
      }
      if(quiz.answerE != ''){
        quizStr = quizStr +"<input type='"+typeStr+"'   name='ANSWER"+seqId+"' id='ANSWER_E"+seqId+"' value='E' /><label for='ANSWER_E"+seqId+"'>"+quiz.answerE+"</label> &nbsp;<font color='red'>("+quiz.scoreE+"分)</font><br>";
      }
      if(quiz.answerF != ''){
        quizStr = quizStr +"<input type='"+typeStr+"'   name='ANSWER"+seqId+"' id='ANSWER_F"+seqId+"' value='F' /><label for='ANSWER_F"+seqId+"'>"+quiz.answerF+"</label> &nbsp;<font color='red'>("+quiz.scoreF+"分)</font><br>";
      }
      if(quizStr != ''){
        //quizStr = quizStr.substr(0,quizStr.length-4);
        quizStr = quizStr + "<font size='4' color='red'>批注：</font><input type='text' id='memo_"+seqId+"' name='memo_"+seqId+"'>";
      }
      tr2.update( "<td nowrap class='TableData'>"+quizStr+"</td>");
      toChecked(1,newArray[i],newMemoArray[i],seqId);
    }
    seqIdPage = seqIdPage.substr(0,seqIdPage.length-1);
 
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>没有考核题目!</div></td></tr>"
        );
    $("form1").appendChild(table);
  }
  $("quizIdPage").value = seqIdPage;
}
function toChecked(score,answer,memo,seqId){
  if(!answer){
    answer = "";
  }
  if(!memo){
    memo = "";
  }
   if(answer=='A'){
     $("ANSWER_A"+seqId).checked = true;
   }
   if(answer=='B'){
     $("ANSWER_B"+seqId).checked = true;
   }
   if(answer=='C'){
     $("ANSWER_C"+seqId).checked = true;
   }
   if(answer=='D'){
     $("ANSWER_D"+seqId).checked = true;
   }
   if(answer=='E'){
     $("ANSWER_E"+seqId).checked = true;
   }
   if(answer=='F'){
     $("ANSWER_F"+seqId).checked = true;
   }
   if($("memo_"+seqId)){
     $("memo_"+seqId).value = memo;
   }
}
function addData(){
  var flowId = '<%=flowId%>';
  var userId = '<%=userId%>';
  var checkFlag = '<%=checkFlag%>';
  var url= "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamDataAct/addData.act?flowId=" + flowId + "&userId="+userId+"&checkFlag="+checkFlag;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prc = rtJson.rtData;
  return prc;
 // seqId = prc.seqId;
}
function getData(){
  var flowId = '<%=flowId%>';
  var userId = '<%=userId%>';
  var url= "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreFlowAct/getDataByFlowId.act?flowId=" + flowId + "&userId="+userId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prc = rtJson.rtData;
  return prc;
}
//提交
function doInit(){
  getValue();
  var checkFlag = '<%=checkFlag%>';
  var queryParam = $("form1").serialize();
  var flowId = '<%=flowId%>';
  var userId = '<%=userId%>';
  var url= "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreDataAct/scoreData.act?flowId=" + flowId + "&checkFlag=" +checkFlag+ "&userId="+userId+"&"+queryParam;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prc = rtJson.rtData;
  if(prc.isSubmit=='1'){
    alert("提交成功！");
  }
}
//得到点击的seqId和答案
function getValue(){
  var quizIdPage = "";
  if( $("quizIdPage")){
    quizIdPage  = $("quizIdPage").value;
  }
  var indexStr = "";
  if(quizIdPage != ''){
    var quizIdPageArray = quizIdPage.split(",");
  
    for(var i = 0; i <quizIdPageArray.length; i++){
      var ANSWER = document.getElementsByName("ANSWER"+quizIdPageArray[i]);
      var seqIdStr = "";
      var ANSWERStr = "";
      for(var j = 0; j< ANSWER.length; j ++){
        if(ANSWER.item(j).checked == true){
          ANSWERStr = ANSWERStr + ANSWER.item(j).value;
        }
      }
      if(ANSWERStr != ''){
        seqIdStr = quizIdPageArray[i] + "_" + ANSWERStr  ;
      }
      if(seqIdStr != ''){
        indexStr = indexStr + seqIdStr + ",";
      }
    }
    if(indexStr != ''){
      indexStr = indexStr.substr(0,indexStr.length-1);
    }
  }
  //alert(indexStr+"------------------");
 document.form1.quizIds.value=indexStr;
}
//得到点击的seqId和答案
function AddValue(seqId,index){
  var quizIds= document.form1.quizIds.value;
  var indexStr = "";
  if($("ANSWER_"+index+seqId).type =="checkbox"){
    for (i=0; i< document.getElementsByName("ANSWER"+seqId).length; i++){
       if(document.getElementsByName("ANSWER"+seqId).item(i).checked == true){
         var checkbox_id = document.getElementsByName("ANSWER"+seqId).item(i).value;
         indexStr = indexStr + checkbox_id;
       }
    }
    
 }else if($("ANSWER_"+index+seqId).type=="radio") {
    for (i=0; i< document.getElementsByName("ANSWER"+seqId).length; i++){
        var radio_id = document.getElementsByName("ANSWER"+seqId).item(i).value;
        indexStr = radio_id;
    }
 }
  indexStr = seqId + "_" + indexStr;//seqId_A或者seqId_ABC
  if(quizIds != ''){
    if(quizIds.substr(quizIds.length-1,quizIds.length)==','){
      quizIds = quizIds.substr(0,quizIds.length-1);
    }
    var quizIdArray = quizIds.split(",");
    for(var i =0; i<quizIdArray.length;i++){

    }
  }else{
    quizIds = indexStr;
 }

  //alert(quizIds+"------------------");
 document.form1.quizIds.value=quizIds;
}

</script>
</head>
<body onload="doOnload();">
<br></br>
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/salary.gif" align="absmiddle"><span class="big3">&nbsp;考核数据录入</span>
    </td>
  </tr>
</table>
 
  
<form action="#" method="post" name="form1" id="form1">

<input type="hidden" id="seqId" name="seqId" value=""></input>
<input type="hidden" id="currPage" name="currPage" value="<%=currPage %>"></input>
<input type="hidden" id="quizIdPage" name="quizIdpage" value=""></input>
<input type="hidden" id="quizIds" name="quizIds" value=""></input>
</form>
</body>
</html>