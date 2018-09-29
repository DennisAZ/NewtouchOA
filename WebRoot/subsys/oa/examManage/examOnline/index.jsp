<%@ page language="java" import=" yh.core.funcs.person.data.YHPerson" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  //判断是否为管理员
  //判断是否自己是审批人员

  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  int userId = user.getSeqId();
  String userName = user.getUserName();
  if(userName == null){
    userName = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>进行考试</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="/yh/subsys/oa/examManage/js/infoPub.js"></script>
<script type="text/javascript">
function doInit(){
  selectCount();
  var url = "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamFlowAct/selectFlowToOnLine.act";
  cfgs = {
   dataAction: url,
   container: "giftList",
   colums: [
      {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
      {type:"text", name:"flowTitle", text:"考试名称", width: "10%",align:"center"},
      {type:"text", name:"participant", text:"参加考试人", width: "8%",align:"center",render:toUserName},
      {type:"text", name:"paperTimes", text:"考试时长（分钟）", width: "10%",align:"center"},
      {type:"text", name:"beginDate", text:"生效日期",sortDef:{type:0, direct:"desc"}, width: "6%",align:"center",render:toBeginDate},
      {type:"text", name:"endDate", text:"终止日期", width: "6%",align:"center",render:toEndDate},
      {type:"text", name:"score", text:"满分", width: "6%",align:"center"},
      {type:"selfdef", name:"randDate", text:"考试日期", width: "6%",align:"center",render:toRandDate},
      {type:"selfdef", name:"score", text:"成绩", width: "6%",align:"center",render:toScore},
      {type:"selfdef", name:"caoZuo", text:"操作", width: "6%",align:"center",render:toOpt}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
       + "<td class='msg info'><div class='content' style='font-size:12pt'>无已发布的考试信息!</div></td></tr>"
       );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  }
}
function toUserName(){
  return '<%=userName%>';
}
function findId(readers){
  var userId = '<%=userId%>';
  var arrayReaders = readers.split(",");
  for(var i= 0; i<arrayReaders.length;i++){
    if(arrayReaders[i]==userId){
       return true;
    }
  }
  return false;
}
//分数
function toOpt(cellData, recordIndex,columInde){
  var userId = '<%=userId%>';
  var seqId =  this.getCellData(recordIndex,"seqId");
  var paperTimes =  this.getCellData(recordIndex,"paperTimes");
  var prcs = getData(seqId);
  if(prcs.length>0){
    var prc = prcs[0];
    if(prc.examed=='1'){
      return "<a href=\"javascript:queryExam("+seqId+",'"+paperTimes+"')\">查卷</a>";
    }else{
      return "<a href=\"javascript:examLine("+seqId+",'"+paperTimes+"')\">继续考试</a>";
    }
    //return prc.score;
  }else{ 
   return "<a href=\"javascript:examLine("+seqId+",'"+paperTimes+"');\">考试</a>";
  }
}
//分数
function toScore(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var prcs = getData(seqId);
  if(prcs.length>0){
    var prc = prcs[0];
    return prc.userScore;
  }else{ 
   return "0";
  }
}
//开始时间
function toBeginDate(cellData, recordIndex,columInde){
  var beginDate =  this.getCellData(recordIndex,"beginDate");
  return beginDate.substr(0,10);
}
//开始时间
function toEndDate(cellData, recordIndex,columInde){
  var endDate =  this.getCellData(recordIndex,"endDate");
  return endDate.substr(0,10);
}
//考试时间
function toRandDate(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var prcs = getData(seqId);
  if(prcs.length>0){
    var prc = prcs[0];
    return prc.rankDate.substr(0,10);
  }else{ 
   return "";
  }
}
function getData(flowId){
  var url= "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamDataAct/getDataByFlowId.act?flowId=" + flowId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prcs = rtJson.rtData;
  return prcs;
}
function examLine(seqId,paperTimes){
  //alert(paperTimes);
  var URL = "<%=contextPath%>/subsys/oa/examManage/examOnline/scoreIndex.jsp?flowId=" + seqId + "&paperTimes="+paperTimes;
  myleft=(screen.availWidth-780)/2;
  window.open(URL,"read_vote","height=500,width=780,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}
function queryExam(seqId,paperTimes){
  var URL = "<%=contextPath%>/subsys/oa/examManage/examOnline/queryExam/scoreIndex.jsp?flowId=" + seqId + "&paperTimes="+paperTimes;
  window.location.href = URL;
}
function selectCount(){
  var url= "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamDataAct/selectCount.act";
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prc = rtJson.rtData;
  if(prc.count>0){
    $("count").innerHTML  = " 共 <font  color='red'>"+prc.count+" </font>条";
  }

}
</script>
</head>
<body topmargin="5px" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/exam_manage.gif" WIDTH="22" HEIGHT="22" align="absmiddle"><span class="big3">&nbsp;考试列表</span>
    </td>
</tr>
</table>
 <center><span id="count"></span></center>
<div id="giftList"></div>
<div id="returnNull">
</div>

</body>
</html>
