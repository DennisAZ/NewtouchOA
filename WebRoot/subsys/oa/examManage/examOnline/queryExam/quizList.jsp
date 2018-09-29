<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
String flowId = request.getParameter("flowId") == null ? "" :  request.getParameter("flowId");
String paperTimes = request.getParameter("paperTimes") == null ? "" :  request.getParameter("paperTimes");
String currPage =  request.getParameter("currPage") == null ? "" :  request.getParameter("currPage"); 
String userId = request.getParameter("userId") == null ? "" :  request.getParameter("userId");
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

<script type="text/javascript"><!--

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
  var score = "";
  if(data.length>0){
    answer = data[0].answer;
    score = data[0].score;
  }
  var answerArray = "";
  if(answer != ''){
    answerArray = answer.split(",");
  }
  var scoreArray = "";
  if(score != ''){
    scoreArray = score.split(",");
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
      var ANSWERS = quiz.answers;
      var tr = new Element('tr',{"class":"TableHeader"});
      $("tbody").appendChild(tr);
      var yesNoStr = "<img src='<%=imgPath%>/subsys/no.png'/>";
      var yesNo = "0";
      if(newArray[i] != '' && newArray[i] != null &&newArray[i]==ANSWERS){
        yesNoStr = "<img src='<%=imgPath%>/subsys/yes.png'/>";
        yesNo = "1";
      }
    
      tr.update( "<td align='left' style='word-break:break-all'>"+(i+1)+ "." + quiz.questions + yesNoStr + "</td>");

      var tr2 = new Element('tr',{});
      $("tbody").appendChild(tr2);
      var quizStr = "";
      var type = quiz.questionsType;
      var typeStr = "radio";
      if(type =='1'){
        typeStr = "checkbox";
      }
      if(quiz.answerA != ''){
        quizStr = quizStr +"<input type='"+typeStr+"' disabled   name='ANSWER"+seqId+"' id='ANSWER_A"+seqId+"' value='A'><label for='ANSWER_A"+seqId+"' >"+quiz.answerA+"</label><span id='ANSWER_A_"+seqId+"' ></span><br>";
      }
      if(quiz.answerB != ''){
        quizStr = quizStr +"<input type='"+typeStr+"' disabled   name='ANSWER"+seqId+"' id='ANSWER_B"+seqId+"' value='B' ><label for='ANSWER_B"+seqId+"'>"+quiz.answerB+"</label><span id='ANSWER_B_"+seqId+"' ></span><br>";

      }
      if(quiz.answerC != ''){
        quizStr = quizStr +"<input type='"+typeStr+"'  disabled  name='ANSWER"+seqId+"' id='ANSWER_C"+seqId+"' value='C'><label for='ANSWER_C"+seqId+"' >"+quiz.answerC+"</label><span id='ANSWER_C_"+seqId+"' ></span><br>";
      }
      if(quiz.answerD != ''){
        quizStr = quizStr +"<input type='"+typeStr+"' disabled   name='ANSWER"+seqId+"' id='ANSWER_D"+seqId+"' value='D'><label for='ANSWER_D"+seqId+"' >"+quiz.answerD+"</label><span id='ANSWER_D_"+seqId+"' ></span><br>";
      }
      if(quiz.answerE != ''){
        quizStr = quizStr +"<input type='"+typeStr+"' disabled   name='ANSWER"+seqId+"' id='ANSWER_E"+seqId+"' value='E' ><label for='ANSWER_E"+seqId+"'>"+quiz.answerE+"</label><span id='ANSWER_E_"+seqId+"' ></span><br>";
      }
      if(quizStr != ''){
        quizStr = quizStr.substr(0,quizStr.length-4);
      }
      tr2.update( "<td nowrap class='TableData'>"+quizStr+"</td>");
      toChecked(type,1,newArray[i],seqId);
      toYesNo(type,1,ANSWERS,seqId,yesNo);
    }
    seqIdPage = seqIdPage.substr(0,seqIdPage.length-1);
 
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>考试信息未选择试题!</div></td></tr>"
        );
    $("form1").appendChild(table);
  }
 // $("quizIdPage").value = seqIdPage;
}
function toChecked(type,score,answer,seqId){
  if(!answer){
    answer = "";
  }
  if(type == '1'){//多选
    if(answer != ''){
      var answerArray = answer.split("");
     // alert(answerArray);
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
function toYesNo(type,score,answer,seqId,yesNo){
  if(!answer){
    answer = "";
  }
  if(yesNo == '0'){
    if(type == '1'){//多选
      var answerArray = answer.split("");
      for(var i = 0;i<answerArray.length;i++){
        if(answerArray[i]=='A'){
          $("ANSWER_A_"+seqId).innerHTML = "<font color='red'>(正确答案)</font>";
        }
        if(answerArray[i]=='B'){
          $("ANSWER_B_"+seqId).innerHTML = "<font color='red'>(正确答案)</font>";
        }
        if(answerArray[i]=='C'){
          $("ANSWER_C_"+seqId).innerHTML = "<font color='red'>(正确答案)</font>";
        }
        if(answerArray[i]=='D'){
          $("ANSWER_D_"+seqId).innerHTML = "<font color='red'>(正确答案)</font>";
        }
        if(answerArray[i]=='E'){
          $("ANSWER_E_"+seqId).innerHTML = "<font color='red'>(正确答案)</font>";
        }
      }
    }else{//单选
     if(answer=='A'){
       $("ANSWER_A_"+seqId).innerHTML = "<font font='red'>(正确答案)</font>";
     }
     if(answer=='B'){
       $("ANSWER_B_"+seqId).innerHTML = "<font font='red'>(正确答案)</font>";
     }
     if(answer=='C'){
       $("ANSWER_C_"+seqId).innerHTML = "<font font='red'>(正确答案)</font>";
     }
     if(answer=='D'){
       $("ANSWER_D_"+seqId).innerHTML = "<font font='red'>(正确答案)</font>";
     }
     if(answer=='E'){
       $("ANSWER_E_"+seqId).innerHTML = "<font font='red'>(正确答案)</font>";
     }
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
function getQuiz(seqId){
  var url= "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamQuizAct/getQuizById.act?seqId=" + seqId ;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prc = rtJson.rtData;
  return prc;
}
function getData(){
  var flowId = '<%=flowId%>';
  var paperTimes = '<%=paperTimes%>';
  var url= "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamDataAct/getDataByFlowId.act?flowId=" + flowId + "&paperTimes="+paperTimes+"&userId=<%=userId%>";
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prc = rtJson.rtData;
  return prc;
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