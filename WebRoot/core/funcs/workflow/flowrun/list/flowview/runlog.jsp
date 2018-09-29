<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
String flowId = request.getParameter("flowId");
String runId = request.getParameter("runId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作流程日志</title>
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
var flowId = '<%=flowId%>';
var runId = '<%=runId%>';
var requestURL;
var logList = [{prcsId:1,prcsName:'aaaaa',userName:'刘涵',time:'2008-01-21 23:09:11',content:'ddd'},{prcsId:1,prcsName:'aaaaa',userName:'刘涵',time:'2008-01-21 23:09:11',content:'ddd'},{prcsId:1,prcsName:'aaaaa',userName:'刘涵',time:'2008-01-21 23:09:11',content:'ddd'},{prcsId:1,prcsName:'aaaaa',userName:'刘涵',time:'2008-01-21 23:09:11',content:'ddd'}];
function doInit(){
  requestURL = contextPath + "/yh/core/funcs/workflow/act/YHMyWorkAct";
  var url = requestURL + "/getLogList.act";
  var json = getJsonRs(url , 'flowId=' + flowId + "&runId=" + runId);
  if(json.rtState == '1'){
    showMessage(json.rtMsrg);
    return ;
  }
  var flag = json.rtData.flag;//固定流程
  logList = json.rtData.logList;
  //提示
  if(logList.length <= 0 ){
    showMessage("无流程日志!");
    return ;
  }
  //表头
  var tr = new Element('tr',{'class':'TableHeader'});
  var td = "";
  
  if(flag){
    td = "<td align=center width=80>步骤序号</td><td align=center>步骤名称</td><td align=center>相关人员</td><td align=center  width=200>发生时间</td><td align=center>内容</td>";
  }else{
    td = "<td align=center width=80>步骤序号</td><td align=center>相关人员</td><td align=center  width=200>发生时间</td><td align=center>内容</td>";
  }
  tr.update(td);
  $('listTbody').appendChild(tr);
  
  for(var i = 0 ;i < logList.length ; i++){
    var log = logList[i];
    createTr(log , flag , i);
  }  
}

function createTr(log , flag , i){
  var clazz = 'TableLine2';
  //是最后一行的话颜色不一样
  if(i%2 == 0){
		clazz = 'TableLine1';
  }
  var tr = new Element("tr",{'class':clazz});
  //第一列
  var td = new Element("td").update(log.prcsId);
  td.align = 'center';
  tr.appendChild(td);
  //第二列
  if(flag){
    var td2 = new Element("td").update(log.prcsName);
    td2.align = 'center';
    tr.appendChild(td2);
  }
  //第三列
  var td3 = new Element("td").update(log.userName);
  td3.align = 'center';
  tr.appendChild(td3);
	//第四列
  var td4 = new Element("td").update(log.time);
  td4.align = 'center';
  td4.width = '200px';
  tr.appendChild(td4);
  //第五列
  var td5 = new Element("td").update(log.content);
  tr.appendChild(td5);
  $('listTbody').appendChild(tr);
}
function showMessage(msg){
  $('content').hide();
  $('messageContent').update(msg);
  $('message').show();
}
</script>
</head>

<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/workflow.gif" align="absmiddle"><span class="big3"> 流程日志</span>&nbsp;
    </td>
  </tr>
</table>
<div id="content">
<table width='700px'  class="TableList">
<tbody id="listTbody">

</tbody>
</table>
</div>
<div id="message"  align=center style="display:none">
<div align=center>
<table width="300" class="MessageBox">
<tbody>
<tr>
    <td class="msg info" style='font-size:11pt'><div id="messageContent"></div></td>
</tr>
</tbody></table>
</div>
</div>
</body>
</html>