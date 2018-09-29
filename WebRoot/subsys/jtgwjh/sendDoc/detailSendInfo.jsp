<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发送详细信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/src/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/sendLogic.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/subsys/jtgwjh/sendDoc/js/logic.js"></script>
<script type="text/javascript">
var requestUrl = "<%=contextPath%>/yh/core/esb/client/act/YHEsbConfigAct";
function doInit(){
  var url =  requestUrl + "/isOnline.act";
  var rtJson = getJsonRsAsyn(url , null , updateState);
  
	var url = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/getDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
    
	  //发送情况列表
    if(data.sendDatetime){
      var data3 =  data.data3;
      for(var i = 0; i < data3.length; i++){
        var td0 = $C('td');
        td0.align = 'center';
        td0.innerHTML = i+1;
        
        var td1 = $C('td');
        td1.align = 'center';
        td1.innerHTML = data3[i].reciveDeptDesc;
        
        var td2 = $C('td');
        td2.align = 'center';
        //状态：-2本地失败-1发送失败，0-草拟，1-待发，2-发送中，3-上传完毕，4-已接收
        switch(data3[i].status){
          case  '-3': td2.innerHTML = '接收失败';break;
          case  '-2': td2.innerHTML = '本地失败';break;
          case  '-1': td2.innerHTML = '发送失败';break;
          case   '0': td2.innerHTML = '草拟';   break;
          case   '1': td2.innerHTML = '待发';   break;
          case   '2': td2.innerHTML = '发送中'; break;
          case   '3': td2.innerHTML = '上传完毕';break;
          case   '4': td2.innerHTML = '对方已接收';  break;
          case   '5': td2.innerHTML = '已重发';  break;
          case   '6': td2.innerHTML = '对方已签收';  break;
        }
        
        var td3 = $C('td');
        td3.align = 'center';
        td3.innerHTML = data3[i].processTime;
        
        var td4 = $C('td');
        td4.align = 'center';
        if(data3[i].status == '-2' || data3[i].status == '-1' || data3[i].status == '-3'){
          td4.innerHTML = '<a href="javascript:void(0);" onclick="reSend(\''+data3[i].guid+'\',\''+data3[i].reciveDept+'\')">重新发送</a>';
        }
        else{
	        td4.innerHTML = '';
        }
        
        var tr = $C('tr');
        tr.id = "print_tr_" + i;
        tr.name = "print_tr_" + i;
        tr.className = 'TableData';
        
        tr.appendChild(td0);
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        $('showSendTable').appendChild(tr);
      }
    }
	}else{
		alert(rtJson.rtMsrg);
	}
}

function reSend(guid, toId){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/sendDocInfoTasks.act";
  var rtJson = getJsonRs(url, "guid="+guid+"&toId="+toId);
  if(rtJson.rtState == "0"){
    alert("已重新加入发送队列中！");
    window.location.reload();
  }
  else{
    alert(rtJson.rtMsrg);
  }
}

function updateState(rtJson) {
  var isline = false;
  if(rtJson.rtState == "0"){
    if (rtJson.rtData) {
      isline = true;
    } 
  } else {
    isline = false;
  }
  if (isline) {
    $('state').update("<img  src=\"../images/a1.gif\" align=\"absmiddle\">已连接");
  } else {
    $('state').update("<img  src=\"../images/a0.gif\" align=\"absmiddle\">未连接");
  }
}

function myrefresh(){
  window.location.reload();
}

setTimeout('myrefresh()',1000*10); //指定1秒刷新一次
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 发送情况信息</span><br>
    </td>
  </tr>
</table>
<br>
<table class="TableBlock" width="350" align="center">
  <tr class="TableData">
    <td nowrap class="TableData" align="center" style="width:200px;">数据交换平台连接状态：</td>
    <td align="center">
       <span id="state"><img  src="../images/a0.gif" align="absmiddle">未连接</span>
    </td>
  </tr>
</table>

<br>
<table align="center" class="TableBlock" >
<thead>
  <tr class="TableHeader">
    <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:50px;">序号</td>
    <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:250px;">接收单位</td>
    <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:90px;">状态</td>
    <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:150px;">接收时间</td>
    <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:70px;">操作</td>
  </tr>
</thead>
<tbody id="showSendTable"></tbody>
</table>
</body>
</html>