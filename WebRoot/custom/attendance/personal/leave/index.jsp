<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>请假</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
window.setTimeout('this.location.reload();',120000);
function histroyLeave(){
  window.location.href = "<%=contextPath%>/custom/attendance/personal/leave/histroyLeave.jsp";
}
function deleteLeave(seqId){
  var msg='确认要删除该请假信息吗？';
  if(window.confirm(msg)){
    var requestURL =  "<%=contextPath%>/yh/custom/attendance/act/YHPersonalLeaveAct/deleteLeaveById.act?seqId=" + seqId;
    var rtJson = getJsonRs(requestURL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.location.reload();
  }
}
function updateLeave(seqId){
  window.location.href = "<%=contextPath%>/custom/attendance/personal/leave/editLeave.jsp?seqId=" + seqId;
}
function distroyLeave(seqId,allow){
  var checkLeave = 2;
  if($("moblieSmsRemind_leave_"+seqId)){
    if($("moblieSmsRemind_leave_"+seqId).checked){
      checkLeave = 1;
    }
  }
  window.location.href = "<%=contextPath%>/yh/custom/attendance/act/YHPersonalLeaveAct/updateAllow.act?seqId=" + seqId + "&allow=" + allow+"&checkLeave="+checkLeave;
}
function doOnload(){
  var moblieRemindFlag = getMoblieSmsRemind();
  var requestURL = "<%=contextPath%>/yh/custom/attendance/act/YHPersonalLeaveAct/selectLeave.act";
  var rtJson = getJsonRs(requestURL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prcsJson = rtJson.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tboday'><tr class='TableListHeader'><td nowrap align='center'>请假原因</td>"
      + "<td nowrap align='center'>审批人员</td>"
      + "<td nowrap align='center'>申请时间</td>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>结束时间</td>"
      + "<td nowrap align='center'>状态</td>"
      + "<td nowrap align='center'>操作</td></tr></tbody>");
    $('listDiv').appendChild(table); 
    for(var i =0;i< prcsJson.length;i++){
      var tr = new Element('tr');
      $('tboday').appendChild(tr);
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var leaderId = prcs.leaderId;
      var leaderName = prcs.leaderName;
      var leaveType = prcs.leaveType;
      var leaveDate1 = prcs.leaveDate1;
      var leaveDate2 = prcs.leaveDate2;
      var allow =prcs.allow;
      var status = prcs.status;
      var reason = prcs.reason;
      var destroyTime = prcs.destroyTime;
      var moblieRemindStr = "";
      if(moblieRemindFlag==2){
        moblieRemindStr = " <span id='moblieSmsRemindDiv'><input type='checkbox' id='moblieSmsRemind_leave_"+seqId+"' name='moblieSmsRemind_leave_"+seqId+"' checked ><label for='smsRemind'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      if(moblieRemindFlag==1){
        moblieRemindStr = " <span id='moblieSmsRemindDiv'><input type='checkbox' id='moblieSmsRemind_leave_"+seqId+"' name='moblieSmsRemind_leave_"+seqId+"'  ><label for='smsRemind'>使用手机短信提醒</label>&nbsp;&nbsp;</span>";
       }
      if(!destroyTime){   
      }else{
        var destroyTime="";
      }
      var annualLeave = prcs.annualLeave;
      if(status == "1"){
         if(allow == "0"){
           tr.update("<td align='left' >" + leaveType + "</td>"
              + "<td nowrap align='center'>" + leaderName + "</td>"
              + "<td nowrap align='center'>" + prcs.applyTime + "</td>"
              + "<td nowrap align='center'>" + leaveDate1 + "</td>"
              + "<td nowrap align='center'>" + leaveDate2 + "</td>"
        
              + "<td nowrap align='center'>待批 </td>"
              + "<td nowrap align='center'>" 
              + "<a href='#' title='提醒：修改提交后，需要重新审批' onclick='updateLeave(" + seqId + ")'>修改</a>&nbsp; "
              + "<a href='#' onclick = 'deleteLeave(" + seqId + ")'>删除</a>"
              + "</td>"
            );
         }else if(allow == "1"){
           tr.update("<td align='left'>" + leaveType + "</td>"
               + "<td nowrap align='center'>" + leaderName + "</td>"
               + "<td nowrap align='center'>" + prcs.applyTime + "</td>"
               + "<td nowrap align='center'>" + leaveDate1 + "</td>"
               + "<td nowrap align='center'>" + leaveDate2 + "</td>"
               + "<td nowrap align='center'>已批准 </td>"
               + "<td nowrap align='center'>" 
               + "<a href='#' title='提醒：修改提交后，需要重新审批' onclick='updateLeave(" + seqId + ")'>修改</a> "
               + moblieRemindStr
               + "<a href='#' onclick = 'distroyLeave(" + seqId + ",3)'>申请销假 </a>"
               + "</td>"
            );
         }else if(allow == "2"){
           tr.update("<td align='left'>" + leaveType + "</td>"
               + "<td nowrap align='center'>" + leaderName + "</td>"
               + "<td nowrap align='center'>" + prcs.applyTime + "</td>"
               + "<td nowrap align='center'>" + leaveDate1 + "</td>"
               + "<td nowrap align='center'>" + leaveDate2 + "</td>"
               + "<td nowrap align='center' title='原因：\n"+reason+"' >未批准 </td>"
               + "<td nowrap align='center'>"
               + "<a href='#' title='提醒：修改提交后，需要重新审批' onclick='updateLeave(" + seqId + ")'>修改 &nbsp; </a> "
               + "<a href='#' onclick = 'deleteLeave(" + seqId + ")'>删除</a>"
               + "</td>"
             );
          }else if(allow == "3"){
            tr.update("<td align='left'>" + leaveType + "</td>"
               + "<td nowrap align='center'>" + leaderName + "</td>"
               + "<td nowrap align='center'>" + prcs.applyTime + "</td>"
               + "<td nowrap align='center'>" + leaveDate1 + "</td>"
               + "<td nowrap align='center'>" + leaveDate2 + "</td>"
               + "<td nowrap align='center'>申请销假</td> "
               + "<td nowrap align='center'>" 
               + "<a href='#' title='提醒：修改提交后，需要重新审批' onclick='updateLeave(" + seqId + ")'>修改</a>"
               + "</td>"
            );
         }
      }
    }
  }
}
function addLeaveJsp(){
  window.location.href = "<%=contextPath%>/custom/attendance/personal/leave/addLeave.jsp";
}
/** 
*js代码 
*是否显示手机短信提醒 
*/ 
function getMoblieSmsRemind(){ 
  var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=6"; 
  var rtJson = getJsonRs(requestUrl); 
  if(rtJson.rtState == "1"){ 
  alert(rtJson.rtMsrg); 
  return ; 
  } 
  var prc = rtJson.rtData; 
  var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
  return moblieRemindFlag;
  if(moblieRemindFlag == '2'){ //显示并选中
  //$(remidDiv).style.display = ''; 
  //$(remind).checked = true; 
  }else if(moblieRemindFlag == '1'){ //显示不选中
  //$(remidDiv).style.display = ''; 
  //$(remind).checked = false; 
  }else{ 
  //$(remidDiv).style.display = 'none'; //直接不显示
  } 
} 
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;请假登记 </span><br>
    </td>
  </tr>
</table>
<br>
<div align="center">
<input type="button"  value="新建请假登记" class="BigButtonC" onClick="addLeaveJsp();" title="新建请假登记">&nbsp;&nbsp;
<input type="button"  value="请假历史记录" class="BigButtonC" onClick="histroyLeave();" title="查看过往的请假记录">
<br>
<br>
<div align="center" id ="listDiv">
</div>
</div>
</body>
</html>

