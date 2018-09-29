<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
String flowId = request.getParameter("flowId") == null ? "" :  request.getParameter("flowId");
String userId = request.getParameter("userId") == null ? "" :  request.getParameter("userId");
String currPage =  request.getParameter("currPage") == null ? "" :  request.getParameter("currPage"); 
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>

<script type="text/javascript">

var tree = null;
function doInit(){	
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
  var currentPageIndex = 1;
  var totalPageNum = 1;
  if(pageInfo.currentPageIndex){
    currentPageIndex = pageInfo.currentPageIndex;
    totalPageNum = pageInfo.totalPageNum;
  }
  // alert(pageInfo);
   var prcs = JsonData.data;
   //var exmaData =  addData();
   var exmaTime = "";
  // if(exmaData.seqId){
    // exmaTime = "<tr class='TableHeader' align='center'><td>开始时间</td></tr><tr align='center'><td style='color:red;'>"+exmaData.startTime.substr(0,19)+"</td></tr>"
    // + "<tr class='TableHeader' align='center'><td>结束时间</td></tr><tr align='center'><td style='color:red;'>"+exmaData.endTime.substr(0,19)+"<input type='hidden' id='endTime' name='endTime' value='"+exmaData.endTime.substr(0,19)+"'></td></tr>";
  // }
   var table = "<table border='0' cellspacing='1' width='100%' class='small' cellpadding='3'align='center'><tbody id='tbody'>"+exmaTime+"<tr class='TableHeader' align='center'>"
     + "<td  >第 <span  style='color:red;'>"+currentPageIndex+"</span> 页，共  <span  style='color:red;'>"+totalPageNum +"</span> 页</td></tr>";
   if(currentPageIndex <= 1){
     table =  table + "<tr align='center'><td><input type='button' class='SmallInput' value='  第一页↑  ' disabled></td></tr>"
              + "<tr align='center'><td><input type='button' class='SmallInput' value='  前一页↑  ' disabled></td></tr>" ;
   }else{
     table =  table + "<tr align='center'><td><input type='button' class='SmallInput' value='  第一页↑  ' onclick='turnPage(1)'></td></tr>"
             + "<tr align='center'><td><input type='button' class='SmallInput' value='  前一页↑  ' onclick='turnPage("+(parseInt(currentPageIndex,10) -1)+")'></td></tr>" ;
  }
   if(currentPageIndex == totalPageNum){
     table =  table + "<tr align='center'><td><input type='button' class='SmallInput' value='  下一页↓  ' disabled></td></tr>"
     + "<tr align='center'><td><input type='button' class='SmallInput' value='  末一页↓  ' disabled></td></tr>" ;

   }else{
     table =  table + "<tr align='center'><td><input type='button' class='SmallInput' value='  下一页↓  ' onclick='turnPage("+(parseInt(currentPageIndex,10) +1)+")'></td></tr>"
     + "<tr align='center'><td><input type='button' class='SmallInput' value='  末一页↓  ' onclick='turnPage("+totalPageNum+")'></td></tr>" ;
  }
   table =  table + "<tr align='center'><td><input type='button' class='SmallInput' value='   提交       ' onclick='funcExit("+currentPageIndex+");'></td></tr>";
   
   table = table + "</tbody></table>";
   $("tableDiv").innerHTML = table;
  // timeview(exmaData.endTime.substr(0,19));
}
function addData(){
  var flowId = '<%=flowId%>';
  var userId = '<%=userId%>';
  var url= "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamDataAct/addData.act?flowId=" + flowId + "&userId="+userId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prc = rtJson.rtData;
  return prc;
 // seqId = prc.seqId;
}
function turnPage(currPage){
  //parent.hrms.form1.currpage.value=curpage;
  parent.hrms.doInit();
  parent.hrms.location="<%=contextPath %>/subsys/oa/hr/score/submit/scoreList.jsp?flowId=<%=flowId %>&userId=<%=userId %>&currPage="+currPage;
 
 document.location.href ="<%=contextPath %>/subsys/oa/hr/score/submit/left.jsp?flowId=<%=flowId %>&userId=<%=userId %>&currPage="+currPage;
 //document.form1.submit();
}
function funcExit(curpage){
  //parent.hrms.form1.NEXT_PAGE.value=curpage;

  //msg='';
 //if(window.confirm(msg)){
  //URL="index.php";
    parent.hrms.doInit();
    parent.parent.file_tree.location.reload();
    parent.hrms.location.reload();
    parent.window.location.reload();
 //}
 //parent.hrms.location='exam.php?FLOW_ID=<?=$FLOW_ID?>&PAPER_ID=<?=$PAPER_ID?>&CUR_PAGE='+CUR_PAGE;
}
</script>
</head>
<body onload="doInit();">
<br></br>
<div id="tableDiv">

</div>


</body>
</html>