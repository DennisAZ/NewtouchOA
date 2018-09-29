<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
String flowId = request.getParameter("flowId");
String runId = request.getParameter("runId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>步骤列表</title>
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
var prcsJson;
var flowId = '<%=flowId%>';
var runId = '<%=runId%>';
var requestURL;
function doInit(){
  requestURL = contextPath + "<%=moduleSrcPath %>/act/YHMyWorkAct";
  var url = requestURL + "/getPrcsList.act";
  var json = getJsonRs(url , 'flowId=' + flowId + "&runId=" + runId);
  if(json.rtState == '1'){
    $('content').hide();
    $('message').update(json.rtMsrg);
    $('message').show();
    return ;
  }
  document.title = '流程图-'+ json.rtData.runMsg.flowName +'-('+ runId +')-' + json.rtData.runMsg.runName;
  prcsJson = json.rtData.prcsList;
  if (json.rtData.timeToId) {
    $('timeToId').value = json.rtData.timeToId;
  }
  for(var i = 0 ;i < prcsJson.length ; i++){
    var prcs = prcsJson[i];
    createTr(prcs , (i + 1 == prcsJson.length ? true : false));
  } 
  if(json.rtData.runMsg.isEnd){
    var tr = new Element("tr",{'class':'TableHeader'});
    var td = new Element("td").update('流程结束');
    td.colSpan = '3';
    td.align = 'center';
    tr.appendChild(td);
    $('listTbody').appendChild(tr);
  }
}

function createTr(prcs1 , isLast){
  var clazz = 'TableLine2';
  //是最后一行的话颜色不一样  if(isLast){
    clazz = 'TableLine1';
  }
  var tr = new Element("tr",{'class':clazz});
  //第一列  var td = new Element("td").update("第<font color=red>"+ prcs1.prcsId +"</font>步");
  
  tr.appendChild(td);
  var list = prcs1.list;
  if (list.length > 0) {
    td.rowSpan = list.length; 
  }
  $('listTbody').appendChild(tr);
  for (var j = 0 ;j < list.length ;j++) {
    //第二列
    var prcs = list[j];
    var td2 = new Element("td").update("<img src='"+ imgPath +"/arrow_down.gif'/>&nbsp;序号"+ prcs.flowPrcs +"：" + prcs.prcsName);
    if (j != 0) {
      tr = new Element("tr",{'class':clazz});
    }
    tr.appendChild(td2);
    //第三列
    var title =  "";
    for(var i = 0 ; i < prcs.user.length ;i++){
      var user = prcs.user[i];
      var prcsUserName = "";
      if(user.isOp){
        prcsUserName = '<span style="text-decoration:underline;font-weight:bold;color:red;cursor:pointer"  title="部门：'+user.deptName+'">'+ user.userName +' 主办</span>';
      }else{
        prcsUserName = '<span style="text-decoration:underline;font-weight:bold;cursor:pointer" title="部门：'+user.deptName+'">'+ user.userName +'</span>';
      }
      var state = user.state;
      if(state == 1){
        title += "<img src='"+ imgPath +"/email_close.gif'  align='absmiddle'/>&nbsp;" + prcsUserName + "&nbsp;[<font color=green>未接收办理</font>]";
      //办理中
      }else if(state == 2){
        title += "<img src='"+ imgPath +"/email_open.gif'  align='absmiddle'/>&nbsp;" + prcsUserName + "&nbsp;[<font color=green>办理中,已用时：" + user.timeUsed + "</font>]";
        
        if(user.timeOutFlag){
          title += '<br><span style="color:red">限时'+ user.timeOut +"小时," + user.timeUsed + '</span>';
        }
        title += "<br> 开始于：" + user.beginTime;
      //已转交下步
      }else if(state == 3){
        title += "<img  src='"+ imgPath +"/flow_next.gif' align='absmiddle'>&nbsp;"+ prcsUserName +"&nbsp;[<font color=green>已转交下步,用时：" + user.timeUsed + "</font>]";
        title += "<br> 开始于：" + user.beginTime;
        if(user.deliverTime){
          title += "<br> 结束于：" + user.deliverTime;
        }
        //已办结
      }else if(state == 4){
        title += "<img  src='"+ imgPath +"/flow_next.gif' align='absmiddle'>&nbsp;"+ prcsUserName +"&nbsp;[<font color=green>已办结,用时：" + user.timeUsed + "</font>]";
        title += "<br>开始于：" + user.beginTime;
        if(user.deliverTime){
          title += "<br> 结束于：" + user.deliverTime;
        }
      }else if(state == 5){
        title += prcsUserName + "&nbsp;[预设经办人]";
      }
      title += "<br><br>";
    }
    var td3 = new Element("td").update(title);
    tr.appendChild(td3);
    if (j != 0) {
      $('listTbody').appendChild(tr);
    }
  }
  
  
}
</script>
</head>

<body onload="doInit()">
<div id="content">
<table width='700px' class="TableList">
<tr class=TableHeader><td colspan=3 align=center>流程开始</td></tr>
<tbody id="listTbody">

</tbody>
</table>
</div>
<div id="message"   align=center style="display:none">
</div>
<input type="hidden" value="" name="timeToId" id="timeToId">
</body>
</html>