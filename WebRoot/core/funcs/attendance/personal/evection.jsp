<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>出差</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
function newEvection(){
  window.location.href = "<%=contextPath%>/core/funcs/attendance/personal/newEvection.jsp";
}
function histroyEvectionJsp(){
  window.location.href = "<%=contextPath%>/core/funcs/attendance/personal/histroyEvection.jsp";
}
function deteleEvection(seqId){
  var msg = "你确定要删除该出差记录吗?";
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendEvectionAct/deleteEvectionById.act?seqId=" + seqId;
    var rtJson = getJsonRs(requestURL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
   // window.location.reload();
    window.location.href = "<%=contextPath%>/core/funcs/attendance/personal/evection.jsp";
  }
}
function updateEvection(seqId){
  window.location.href = "<%=contextPath%>/core/funcs/attendance/personal/editEvection.jsp?seqId=" + seqId;
}
function returnEvection(seqId){
  var URL="<%=contextPath%>/core/funcs/attendance/personal/editEvectionback.jsp?seqId=" + seqId;
  myleft=(screen.availWidth-780)/2;
  mytop=100;
  mywidth=650;
  myheight=400;
  window.open(URL,"returnEvection","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}
function doOnload(){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendEvectionAct/selectEvection.act";
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prcsJson = rtJson.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tboday'><tr class='TableHeader'><td nowrap align='center'>出差地区</td>"
      + "<td nowrap align='center'>出差原因</td>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>结束时间</td>"
      + "<td nowrap align='center'>审批人员</td>"
      + "<td nowrap align='center'>状态</td>"
      + "<td nowrap align='center'>操作</td></tr></tbody>");
    $('listDiv').appendChild(table); 
    for(var i =0;i< prcsJson.length;i++){
      var tr = new Element('tr',{"class":"TableData"});
      $('tboday').appendChild(tr);
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var leaderId = prcs.leaderId;
      var leaderName = prcs.leaderName;
      var evectionDate1 = prcs.evectionDate1;
      var evectionDate2 = prcs.evectionDate2;
      var evectionDest = prcs.evectionDest;
      var allow =prcs.allow;
      var status = prcs.status;
      var reason = prcs.reason;
      var status = prcs.status;
      var reason = prcs.reason;

      var isHookRun = prcs.isHookRun;
      var flowId = prcs.flowId;

      var opts= "<a href='#' title='提醒：修改提交后，需要重新审批' onclick='updateEvection(" + seqId + ")'>修改</a>&nbsp;&nbsp; "
                 + "<a href='#' onclick = 'deteleEvection(" + seqId + ")'>删除</a>";
      var opts1="<a href='#' title='提醒：修改提交后，需要重新审批' onclick='updateEvection(" + seqId + ")'>修改</a>&nbsp;&nbsp; ";
      if(isHookRun!=0){
    	  leaderName = "<a href='javascript:void(0)' onclick='formView("+isHookRun+" , "+ flowId +")'>查看流程</a>";
        opts="<a href='#' onclick = 'deteleEvection(" + seqId + ")'>删除</a>";
        opts1="";
            }
      
      if(status == "1"){
         if(allow == "0"){
           tr.update("<td  align='center'>" + evectionDest + "</td>"
              + "<td align='center'>" + reason + "</td>"
              + "<td nowrap align='center'>" + evectionDate1.substr(0,10) + "</td>"
              + "<td nowrap align='center'>" + evectionDate2.substr(0,10) + "</td>"
              + "<td nowrap align='center'>" + leaderName + "</td>"
              + "<td nowrap align='center'>待批 </td>"
              + "<td nowrap align='center'>" 
              +opts
              + "</td>"
            );
         }else if(allow == "2"){
           tr.update("<td align='center'>" + evectionDest + "</td>"
               + "<td  align='center'>" + reason + "</td>"
               + "<td nowrap align='center'>" + evectionDate1.substr(0,10) + "</td>"
               + "<td nowrap align='center'>" + evectionDate2.substr(0,10) + "</td>"
               + "<td nowrap align='center'>" + leaderName + "</td>"
               + "<td nowrap align='center'  title='原因：\n"+reason+"'>未批准 </td>"
               + "<td nowrap align='center'>" 
               + opts
               + "</td>"
             );
         }else if(allow == "1"){
           tr.update("<td align='center'>" + evectionDest + "</td>"
               + "<td align='center'>" + reason + "</td>"
               + "<td nowrap align='center'>" + evectionDate1.substr(0,10) + "</td>"
               + "<td nowrap align='center'>" + evectionDate2.substr(0,10) + "</td>"
               + "<td nowrap align='center'>" + leaderName + "</td>"
               + "<td nowrap align='center'>已批准 </td>"
               + "<td nowrap align='center'>" 
               +opts1
               + "<a href='#' onclick = 'returnEvection(" + seqId + ")'>出差归来</a>"
               + "</td>"
             );
         }
      }
    }
  }
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;出差登记</span><br>
    </td>
  </tr>
</table>
<br>
<div align="center">
<input type="button"  value="新建出差登记" class="BigButtonC" onClick="newEvection();" title="新建出差登记">&nbsp;&nbsp;
<input type="button"  value="出差历史记录" class="BigButtonC" onClick="histroyEvectionJsp();" title="查看过往的出差记录">
</div>
<br>
<br>
<div align="center" id="listDiv">
</div>
</body>
</html>