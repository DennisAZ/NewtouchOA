<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
String flowId = request.getParameter("flowId") == null ? "" :  request.getParameter("flowId");
String paperTimes = request.getParameter("paperTimes") == null ? "" :  request.getParameter("paperTimes");
String currPage =  request.getParameter("currPage") == null ? "" :  request.getParameter("currPage"); 
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线考试</title>
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
  var paperTimes = '<%=paperTimes%>';
  var currPage = '<%=currPage%>';
  var url= "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamDataAct/getExmaQuiz.act?flowId=" + flowId + "&paperTimes="+paperTimes+"&currPage="+currPage;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  //alert(rsText);
  
  var exmaData =  addData();
  if(exmaData.seqId){
    $("seqId").value = exmaData.seqId;
  }
  var JsonData = rtJson.rtData;
  var  pageInfo = JsonData.pageInfo;
  var prcs = JsonData.quizList;

  var data = getData(); 
  var answer = "";
  if(data.length>0){
    answer = data[0].answer;
  }

  //alert(answer);
  var answerArray = "";
  if(answer != ''){
    answerArray = answer.split(",");
  }
  var newArray = new Array();
  var index = 0;
  var pageSize = 5;
  for(var i=(parseInt(currPage,10)-1)*pageSize+0; i< (parseInt(currPage,10)-1)*pageSize+pageSize; i++){
    newArray[index] = answerArray[i];
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
      tr.update( "<td align='left' style='word-break:break-all'>"+(i+1)+ "." + quiz.questions + "</td>");

      var tr2 = new Element('tr',{});
      $("tbody").appendChild(tr2);
      var quizStr = "";
      var type = quiz.questionsType;
      var typeStr = "radio";
      if(type =='1'){
        typeStr = "checkbox";
      }
      if(quiz.answerA != ''){
        quizStr = quizStr +"<input type='"+typeStr+"'   name='ANSWER"+seqId+"' id='ANSWER_A"+seqId+"' value='A'/><label for='ANSWER_A"+seqId+"' >"+quiz.answerA+"</label><br>";
      }
      if(quiz.answerB != ''){
        quizStr = quizStr +"<input type='"+typeStr+"'   name='ANSWER"+seqId+"' id='ANSWER_B"+seqId+"' value='B' /><label for='ANSWER_B"+seqId+"'>"+quiz.answerB+"</label><br>";

      }
      if(quiz.answerC != ''){
        quizStr = quizStr +"<input type='"+typeStr+"'   name='ANSWER"+seqId+"' id='ANSWER_C"+seqId+"' value='C'/><label for='ANSWER_C"+seqId+"' >"+quiz.answerC+"</label><br>";
      }
      if(quiz.answerD != ''){
        quizStr = quizStr +"<input type='"+typeStr+"'   name='ANSWER"+seqId+"' id='ANSWER_D"+seqId+"' value='D'/><label for='ANSWER_D"+seqId+"'>"+quiz.answerD+"</label><br>";
      }
      if(quiz.answerE != ''){
        quizStr = quizStr +"<input type='"+typeStr+"'   name='ANSWER"+seqId+"' id='ANSWER_E"+seqId+"' value='E' /><label for='ANSWER_E"+seqId+"'>"+quiz.answerE+"</label><br>";
      }
      if(quizStr != ''){
        quizStr = quizStr.substr(0,quizStr.length-4);
      }
      tr2.update( "<td nowrap class='TableData'>"+quizStr+"</td>");
      toChecked(type,1,newArray[i],seqId);
    }
    seqIdPage = seqIdPage.substr(0,seqIdPage.length-1);
 
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>考试信息未选择试题!</div></td></tr>"
        );
    $("form1").appendChild(table);
  }
  $("quizIdPage").value = seqIdPage;
  if(data.length>0){
    timeview(data[0].endTime.substr(0,19));
  }
}
function toChecked(type,score,answer,seqId){
  if(!answer){
    answer = "";
  }
  if(type == '1'){//多选
    if(answer != ''){
      var answerArray = answer.split("");
      //alert(answerArray);
      for(var i = 0;i<answerArray.length;i++){
        if(answerArray[i]=='A'){
          $("ANSWER_A"+seqId).checked = true;
        }
        if(answerArray[i]=='B'){
          $("ANSWER_B"+seqId).checked = true;
        }
        if(answerArray[i]=='C'){
          $("ANSWER_C"+seqId).checked = true;
        }
        if(answerArray[i]=='D'){
          $("ANSWER_D"+seqId).checked = true;
        }
        if(answerArray[i]=='E'){
          $("ANSWER_E"+seqId).checked = true;
        }
      }
    }
  }else{//单选
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
  }
}
function addData(){
  var flowId = '<%=flowId%>';
  var paperTimes = '<%=paperTimes%>';
  var url= "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamDataAct/addData.act?flowId=" + flowId + "&paperTimes="+paperTimes;
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
  var paperTimes = '<%=paperTimes%>';
  var url= "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamDataAct/getDataByFlowId.act?flowId=" + flowId + "&paperTimes="+paperTimes;
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
  var queryParam = $("form1").serialize();
  var flowId = '<%=flowId%>';
  var paperTimes = '<%=paperTimes%>';
  var url= "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamDataAct/ExmaData.act?flowId=" + flowId + "&paperTimes="+paperTimes+"&"+queryParam;
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

function timeview(endTime){

  var currTime = getCurrDateStrTime();
  //alert(endTime+":"+currTime);
  //alert(currTime>=endTime);
  if(currTime>=endTime){
  	autoExit();
  }
  window.setTimeout("timeview('"+endTime+"')", 1000 );
}

/**
 * 得到今天的日期串yyyy-MM-dd HH:mm:ss
 * @return
 */
function getCurrDateStrTime(){
  var currDate = new Date();
  var currDay = currDate.getDate();  
  var currMonth = currDate.getMonth()+1;
  var currYear = currDate.getFullYear(); 
 // var currTime = currDate.toLocaleTimeString();

  var hour = currDate.getHours(); //获取当前小时数(0-23)
  var min = currDate.getMinutes(); //获取当前分钟数(0-59)
  var second = currDate.getSeconds(); //获取当前秒数(0-59)
  if(currMonth<10){
    currMonth = "0"+currMonth;
  }
  if(currDay<10){
    currDay = "0"+currDay;
  }
  if(hour<10){
    hour = "0"+hour;
  }
  if(min<10){
    min = "0"+min;
  }
  if(second<10){
    second = "0"+second;
  }
  return currYear+"-"+currMonth+"-"+currDay+" "+hour + ":" + min + ":" + second;
}
function autoExit(){
  doInit();
  alert('考试时间已到，考试结束！');  
  parent.window.opener.location.reload();
  parent.window.close();
}
</script>
</head>
<body onload="doOnload();">
<br></br>
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3">参加考试</span>
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