<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>外出登记</title>
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
function addOut(){
  window.location.href = "<%=contextPath%>/core/funcs/attendance/personal/addOut.jsp";
}
function deleteOut(seqId){
  var msg='确认要删除该维护信息吗？';
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendOutAct/deleteOutById.act?seqId=" + seqId;
    var rtJson = getJsonRs(requestURL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    //window.location.reload();
    window.location.href = "<%=contextPath%>/core/funcs/attendance/personal/out.jsp";
  }
}
function historyOut(){
  window.location.href = "<%=contextPath%>/core/funcs/attendance/personal/historyOut.jsp";
}
function updateStatus(seqId,status){
   URL="<%=contextPath%>/core/funcs/attendance/personal/editOutback.jsp?seqId=" + seqId + "&status=" + status;
   myleft=(screen.availWidth-780)/2;
   mytop=100;
   mywidth=650;
   myheight=400;
   window.open(URL,"updateStatus","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}
function updateOut(seqId){
  window.location = "<%=contextPath%>/core/funcs/attendance/personal/editOut.jsp?seqId=" + seqId ;
}
function doOnload(){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendOutAct/selectOut.act";
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prcsJson = rtJson.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tboday'><tr class='TableHeader'><td nowrap align='center'>申请时间</td>"
      + "<td nowrap align='center'>审批人员</td>"
      + "<td nowrap align='center'>外出原因</td>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>结束时间</td>"
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
      var outType = prcs.outType;
      var createDate = prcs.createDate;
      var submitTime = prcs.submitTime;
      var outTime1 = prcs.outTime1;
      var outTime2 = prcs.outTime2;
      var allow =prcs.allow;
      var status = prcs.status;
      var reason = prcs.reason;
      var isHookRun = prcs.isHookRun;
      var flowId = prcs.flowId;
      var opts="<a href='#' onclick = 'updateOut(" + seqId + ")' title='提醒：修改提交后，需要重新审批'>修改</a> &nbsp;&nbsp;";
       if(isHookRun!="0"){
         opts="";
    	   leaderName = "<a href='javascript:void(0)' onclick='formView("+isHookRun+" , "+ flowId +")'>查看流程</a>";
           }
      
      if(seqId != ""){  
        if(allow == "0"){
          tr.update("<td nowrap align='center'>" + createDate + "</td>"
              + "<td nowrap align='center'>" + leaderName + "</td>"
              + "<td  align='center'>" + outType + "</td>"
              + "<td nowrap align='center'>" + submitTime.substr(0,16) + "</td>"
              + "<td nowrap align='center'>" + submitTime.substr(0,11) +  outTime2 + "</td>"
              + "<td nowrap align='center'> 待批 </td>"
              + "<td nowrap align='center'>" 
              + opts
              + "<a href='#' onclick = 'deleteOut(" + seqId + ")'>删除</a>"
              + "</td>");
          }else if(allow == '1' && status == "0"){
            tr.update("<td nowrap align='center'>" + createDate + "</td>"
                + "<td nowrap align='center'>" + leaderName + "</td>"
                + "<td align='center'>" + outType + "</td>"
                + "<td nowrap align='center'>" + submitTime.substr(0,16) + "</td>"
                + "<td nowrap align='center'>" + submitTime.substr(0,11) +  outTime2 + "</td>"
                + "<td nowrap align='center'>已批准 </td>"
                + "<td nowrap align='center'>" 
                + opts
                + "<a href='#' onclick = 'updateStatus(" + seqId + ",1)'>外出归来</a>"
                + "</td>");
          }else if(allow == "2"){
            tr.update("<td nowrap align='center'>" + createDate + "</td>"
                + "<td nowrap align='center'>" + leaderName + "</td>"
                + "<td align='center'>" + outType + "</td>"
                + "<td nowrap align='center'>" + submitTime.substr(0,16) + "</td>"
                + "<td nowrap align='center'>" + submitTime.substr(0,11) +  outTime2 + "</td>"
                + "<td nowrap align='center' title='原因：\n"+reason+"'>未批准 </td>"
                + "<td nowrap align='center'>"  
                + opts
                + "<a href='#' onclick = 'deleteOut(" + seqId + ")'>删除</a>"
                + "</td>" );
         }
      } 
    }
  }
}
</script>
</head>
<body class="" topmargin="5" onload = "doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;外出登记</span><br>
    </td>
  </tr>
</table>
<br>
<div align="center">
<input type="button"  value="外出登记" class="BigButton" onClick="addOut();" title="新建外出登记">&nbsp;&nbsp;
<input type="button"  value="外出历史记录" class="BigButtonC" onClick="historyOut();" title="查看过往的外出记录">
</div>
<br>
<br>
<div id = "listDiv"  align="center"></div>
</body>
</html>