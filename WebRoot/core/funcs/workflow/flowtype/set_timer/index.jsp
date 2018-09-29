<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String flowId = request.getParameter("flowId"); 
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>定时启动任务</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
function delete_affair(TASK_ID)
{
 var msg='确认要删除该定时任务吗？';
 if(window.confirm(msg))
 {
   _get("del_schema.php","TASK_ID="+TASK_ID,function(req){ 
   	 if(req.responseText!="+OK") 
   	   alert("删除时发生错误，请重试");  
   	 else 
   	 {
   	 	$("TID_"+TASK_ID).removeNode(true);
   	 	if($("tbl_list").rows.length==1)
   	 	{
   	 	  $("tbl_list").removeNode();
   	 	  $("msg_info").style.display = "";
   	  }
     }
   });
 }
}
function doInit(){
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowTimerAct/getTimers.act";      
  var json = getJsonRs(url ,"flowId=<%=flowId %>");   
  if(json.rtState == "0"){ 
    if (json.rtData.length <= 0) {
      $('tbl_list').hide();
      $('noData').show();
    }  
    for (var i = 0 ;i < json.rtData.length ; i++) {
      addRow(i , json.rtData[i]);
    }
  } 
}
function addRow(i ,data) {
 var type = data.type;
 var privUser = data.privUser;
 var remindDate = data.remindDate;
 var remindTime = data.remindTime;
 var seqId = data.seqId;
 
 var  td = "<td align=center>" + type +"</td>"
  + "<td>" + privUser +"</td>"
  + "<td>" + remindDate +"</td>"
  + "<td>" + remindTime +"</td>"
  + "<td><input type='button' class=\"SmallButton\" value=\"删除\" onclick='del("+seqId+",this)'>" 
  + "&nbsp;&nbsp;<input class=\"SmallButton\"  type='button' value=\"修改\" onclick=\"javascript:location.href='new_timer.jsp?flowId=<%=flowId %>&seqId="+seqId+"'\"></td>";
  
var className = "TableLine2" ;    
if(i%2 == 0){
className = "TableLine1" ;
}

var tr = new Element("tr" , {"class" : className});
$('tbodyList').appendChild(tr);  
tr.update(td);
}
function del(seqId , obj) {
  if (confirm("确认删除！")) {
    var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowTimerAct/delTimer.act";      
    var json = getJsonRs(url ,"seqId=" + seqId);   
    if(json.rtState == "0"){  
      alert(json.rtMsrg);
      tr = obj.parentNode.parentNode;
      tr.parentNode.removeChild(tr);
    } 
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="../img/task.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 新建定时启动任务</span><br>
    </td>
  </tr>
</table>

<div align="center">
<input type="button" value="新建定时任务" class="BigButton" onClick="location='new_timer.jsp?flowId=<%=flowId %>';" title="新建定时任务">
</div>

<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="../img/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="../img/task.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 定时启动任务管理</span>
    </td>
  </tr>
</table>

<br>
 <table class="TableList" id="tbl_list" width="95%">
  <thead class="TableHeader">
  <tr>
      <td nowrap align="center" width="70">定时类型</td>
	  <td align="cetner">流程发起人</td>
      <td nowrap align="center" width="80">发起日期</td>
      <td nowrap align="center" width="80">发起时间</td>
      <td nowrap align="center" width="200">操作</td>
      </tr>
   </thead>
    <tbody id="tbodyList">
   </tbody>
   </table>
   <div id="noData" align=center style="display:none">
   <table class="MessageBox" width="250">
    <tbody>
        <tr>
            <td id="msgInfo" class="msg info">无定时启动任务
            </td>
        </tr>
    </tbody>
</table>
<div>
</body>
</html>
