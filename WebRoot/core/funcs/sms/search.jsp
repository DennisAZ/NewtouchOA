<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.sms.data.YHSmsBody, yh.core.funcs.sms.logic.YHSmsLogic" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%@ page import="java.text.*" %>
<%@ include file="/core/inc/header.jsp" %>
<%
  String toId = request.getParameter("toId");
  if(toId == null){
    toId = "";
  }
  String change = request.getParameter("change");
  if(change == null){
    change = "";
  }
  
  ArrayList<YHSmsBody> bodyList = (ArrayList<YHSmsBody>)request.getAttribute("bodySearchList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>已发送短信</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/grid.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/cmp/grid.css" />
<script type="text/javascript">
var toId = "<%=toId%>";
var change = "<%=change%>";
  function doInit() {
    if(change=="sendPerson"){
    }else{
    }
    var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/search.act";
    var rtJson = getJsonRs(url, "toId=" + toId);
    var content = document.getElementById("content");
    var fromId = document.getElementById("fromId");
    var sendTime = document.getElementById("sendTime");
    var seqId = document.getElementById("seqId");
    //divCont.innerHTML = contentDetail;
    if (rtJson.rtState == "0") {
      bindJson2Cntrl(rtJson.rtData);
      for(var i = 0; i < rtJson.rtData.length; i++){
      //  seqId.value = rtJson.rtData[i].content; 
       // content.innerHTML = rtJson.rtData[i].content; 
       // fromId.innerHTML = rtJson.rtData[i].fromId; 
       // sendTime.innerHTML = rtJson.rtData[i].sendTime; 
      }
      //alert(rtJson.rtMsrg); 
      //window.location.reload();
    }else {
      alert(rtJson.rtMsrg); 
    } 
  }
  
  function confirmDel() {
    if(confirm("确认删除！")) {
      return true;
    }else {
      return false;
    }
  }

  function deleteSms(seqId) {
    if(!confirmDel()) {
  	  return ;
    }
        
    var url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/deleteSms.act";
    var rtJson = getJsonRs(url, "seqId=" + seqId);
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg); 
      window.location.reload();
    }else {
      alert(rtJson.rtMsrg); 
    }
  }

  function dispatchSms(seqId) {
    window.location.href = "<%=contextPath %>/core/funcs/sms/sendsms.jsp?seqId=" + seqId;
  }

  function checkAll(field) {
    var deleteFlags = document.getElementsByName("deleteFlag");
    for(var i = 0; i < deleteFlags.length; i++) {
      deleteFlags[i].checked = field.checked;
    }
  }

  function deleteAllCheckBoxs() {
    if(!confirmDel()) {
  	  return ;
    }
    
    var deleteAllFlags = document.getElementsByName("deleteFlag");
    var flag = false;
    var idStrs = "";
    for(var i = 0; i < deleteAllFlags.length; i++) {
	  if(deleteAllFlags[i].checked) {
		idStrs += deleteAllFlags[i].value + "," ;
        flag = true;
	  }	  
    }

    if(!flag) {
      alert("请选择需要删除的内部短信！");
      return;
    }
    
    var url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/deleteAllSms.act";
    var rtJson = getJsonRs(url, "idStrs=" + idStrs);
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg); 
      window.location.reload();
      //var url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/listAllSms.act";
      //getJsonRs(url, null);
    }else {
      alert(rtJson.rtMsrg); 
    } 
  }

</script>
</head>
<body topmargin="5" onload="doInit()">
<table cellscpacing="1" cellpadding="3" width="100%">
  <tr class="TableHeader">
    <td nowrap align="center">选择</td>
    <td nowrap align="center">发送人</td>
    <td nowrap align="center">内容</td>
    <td nowrap align="center">发送时间</td>
    <td nowrap align="center">提醒</td>
    <td nowrap align="center">操作</td>
  </tr>
  <%
     for(int i = 0; i < bodyList.size(); i++) {
       //YHPageModel pageModelWant = wantedPageModelList.get(i);
       YHSmsBody sb = bodyList.get(i);
       //YHSmsLogic smsLogicWant = new YHSmsLogic();
       int smsSeqId = 0;
       String userId = "";

   %>
   <tr>
     <td>
       <input type="checkbox" name="deleteFlag" value="" id="seqId"/>
     </td>
     <td nowrap align="center" id="fromId">
     </td>
     <td id="content">
       <img src="<%=imgPath%>/collapsed.gif" align="absMiddle" title="点击展开消息记录" onclick="show_msg(4);" style="cursor:pointer;" id="img4">
       <img src="<%=imgPath%>/1.gif" align="absMiddle" width="15" height="15" alt="个人短信">
       
     </td>
     <td nowrap align="center" id="sendTime">
     <%=new SimpleDateFormat("yyyy-MM-dd").format(sb.getSendTime())%>
     </td>
     <td nowrap align="center">
     <img src="<%=imgPath%>/email_open.gif" title="收信人已读">
     </td>
   </tr>  
   <%
     }
   %>
</table>
  <div>
     <input type="checkbox" id="checkAll" name="checkAll" onclick="checkAll(this)"/><label for="checkAll">全选</label>&nbsp;&nbsp;&nbsp;&nbsp;
     <input type="button" onclick="deleteAllCheckBoxs()" value="删除"/>
  </div>
  <div align="center">
  <input type="button"  value="返回" class="BigButton" onClick="location='<%=contextPath %>/core/funcs/sms/index.jsp'">
</div>
  
</body>
</html>