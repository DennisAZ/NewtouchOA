<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  List<Map<String,String>> affairList = (List<Map<String,String>>)request.getAttribute("affairList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>周期性事务</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript">

function check_all(){
  for (i=0;i<document.getElementsByName("email_select").length;i++){
    if(document.getElementsByName("allbox")[0].checked)
      document.getElementsByName("email_select").item(i).checked=true;
    else
      document.getElementsByName("email_select").item(i).checked=false;
   }
  if(i==0){
    if(document.getElementsByName("allbox")[0].checked)
      document.getElementsByName("email_select").checked=true;
    else
      document.getElementsByName("email_select").checked=false;
   }
}
function check_one(el){
   if(!el.checked)
      document.getElementsByName("allbox")[0].checked=false;
}
function get_checked(){
  checked_str="";
  for(i=0;i<document.getElementsByName("email_select").length;i++){
    el=document.getElementsByName("email_select").item(i);
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  if(i==0){
    el=document.getElementsByName("email_select");
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  return checked_str;
}
function delete_mail(){
  delete_str=get_checked();
  delete_str = delete_str.substr(0,delete_str.length-1);
  if(delete_str==""){
    alert("要删除事务，请至少选择其中一条。");
    return;
  }
  msg='确认要删除所选事务吗？';
  if(window.confirm(msg)){
    window.location = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/deleteAffair.act?seqIds="+delete_str;
  }
}
function mynote(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/affairnote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");
}
function delete_affair(seqId){
  var msg='确认要删除该事务吗？';
  if(window.confirm(msg)) {
    var requestURL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/deleteAffairById.act?seqId="+seqId;
    var rtJson = getJsonRs(requestURL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.location = "<%=contextPath%>/core/funcs/calendar/Cycaffair.jsp";
  }
}
function update_affair(seqId){
   window.location = "<%=contextPath%>/core/funcs/calendar/editaffair.jsp?seqId="+seqId;
}
</script>
</head>
<body class="" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"> 周期性事务查询结果</span><br>
    </td>
  </tr>
</table>&nbsp;

<%if(affairList!=null&&affairList.size()>0){ 
%>
<table class="TableList" width="95%" align="center">
    <thead class="TableHeader">
      <td width="40" align="center">选择</td>
      <td nowrap align="center">开始时间</td>
      <td nowrap align="center">结束时间</td>
      <td nowrap align="center">提醒类型</td>
      <td nowrap align="center">提醒日期</td>
      <td nowrap align="center">提醒时间</td>
      <td nowrap align="center">事务内容</td>
      <td nowrap align="center" width="70">操作</td>
    </thead>
    <%for(int i=0;i<affairList.size();i++){ 
        Map<String,String> affair = affairList.get(i);
        String endTime = affair.get("endTime");
        String weekNames[] = {"周一","周二","周三","周四","周五","周六","周日"};
        String typeNames[] = {"按日提醒","按周提醒","按月提醒","按年提醒"};
        String userId = "";
        String managerId = "";
        if(affair.get("endTime")==null){
          endTime = "";
        }else{
          endTime = endTime.substring(0,19);
        }
        String remindDate = "";
        if(affair.get("type").equals("3")){
          remindDate = weekNames[Integer.parseInt(affair.get("remindDate"))-1];
        }
        if(affair.get("type").equals("4")){
          remindDate = affair.get("remindDate")+"日";
        }
        if(affair.get("type").equals("5")){
          remindDate = affair.get("remindDate").split("-")[0]+"月"+ affair.get("remindDate").split("-")[1]+"日";
        }
        String content = affair.get("content");
        if(affair.get("content")==null){
          content = "";
        }
        if(affair.get("userId")!=null){
          userId = affair.get("userId");
        }
        if(affair.get("managerId")!=null&&!affair.get("managerId").trim().equals("")){
          managerId = affair.get("managerId");
        }
        if(managerId.trim().equals("")||managerId.equals(userId)){
         %>
     <tr class="TableData">
      <td>&nbsp;<input type="checkbox" name="email_select" value="<%=affair.get("seqId") %>" onClick="check_one(self);">
      <td nowrap align="center"><%=affair.get("beginTime").substring(0,19) %></td>
      <td nowrap align="center"><%=endTime %></td>
      <td nowrap align="center"><%=typeNames[Integer.parseInt(affair.get("type"))-2] %></td>
      <td nowrap align="center"><%=remindDate %></td>
      <td nowrap align="center"><%=affair.get("remindTime") %></td>
      <td title=""><span class="type2">&nbsp</span><a href="#" onclick="mynote(<%=affair.get("seqId") %>);"><%=content %></a></td>
      <td nowrap align="center">
          <a href="javascript:update_affair(<%=affair.get("seqId") %>);">修改</a>&nbsp;
          <a href="javascript:delete_affair(<%=affair.get("seqId") %>);"> 删除</a>
      </td>
     </tr>
        <%
        }else{
          
         %>
     <tr class="TableData">
      <td>&nbsp;
      <td nowrap align="center"><%=affair.get("beginTime").substring(0,19) %></td>
      <td nowrap align="center"><%=endTime %></td>
      <td nowrap align="center"><%=typeNames[Integer.parseInt(affair.get("type"))-2] %></td>
      <td nowrap align="center"><%=remindDate %></td>
      <td nowrap align="center"><%=affair.get("remindTime") %></td>
      <td title=""><span class="type2">&nbsp</span><a href="#" onclick="mynote(<%=affair.get("seqId") %>);"><%=content %></a></td>
      <td nowrap align="center">
         <a href="#" onclick="mynote(<%=affair.get("seqId") %>);">查看</a>
      </td>
    </tr>     
     <%
        }
    } %>
    <tr class="TableControl">
      <td colspan="8">&nbsp;<input type="checkbox" name="allbox" id="allbox_for" onClick="check_all();">
      <label for="allbox_for">全选</label>&nbsp;
      <a href="javascript:delete_mail();" title="删除所选任务"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除</a>&nbsp;
     </td>
    </tr>
  </table>
<%}else{ %>
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无符合条件的事务安排</div>
    </td>
  </tr>
</table>
<%} %>
<br>
<center> <input type="button" class="BigButton" value="返回" onclick="history.go(-1);"> </center>
</body>

</html>
