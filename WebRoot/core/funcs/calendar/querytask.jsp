<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  List<Map<String,String>> taskList = (List<Map<String,String>>)request.getAttribute("taskList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>任务查询</title>
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
function deleteTaskById(seqId){
  msg='确认要删除该任务吗？';
  if(window.confirm(msg)) {
    var requestURL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHTaskAct/deleteTaskById.act?seqId="+seqId;
    var rtJson = getJsonRs(requestURL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.location = "<%=contextPath%>/core/funcs/calendar/task.jsp";
  }
}
function taskNote(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/tasknote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}
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
function deleteTask(){
  delete_str=get_checked();
  delete_str = delete_str.substr(0,delete_str.length-1);
  if(delete_str==""){
    alert("要删除事务，请至少选择其中一条。");
    return;
  }
  msg='确认要删除所选事务吗？';
  if(window.confirm(msg)){
    window.location = "<%=contextPath%>/yh/core/funcs/calendar/act/YHTaskAct/deleteTask.act?seqIds="+delete_str;
  }
}
</script>
</head>
<body class="" topmargin="5">


<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"> 任务查询结果</span>
    </td>
  </tr>
</table>&nbsp;
<%if(taskList!=null&&taskList.size()>0){ %>
<table class="TableList"  width="100%" align="center">
    <tr class="TableHeader">
      <td nowrap align="center">选择</td>
      <td nowrap align="center">序号</td>
      <td nowrap align="center">开始时间</td>
      <td nowrap align="center">结束时间</td>
      <td nowrap align="center">任务类型</td>
      <td nowrap align="center">任务标题</td>
      <td nowrap align="center">状态</td>
      <td nowrap align="center">任务内容</td>
      <td nowrap align="center">操作</td>
    </tr>
    <%for(int i=0;i<taskList.size();i++){
        Map<String,String> task = taskList.get(i);
        String beginDate = task.get("beginDate");
        String endDate = task.get("endDate");
        String typeStatus = task.get("taskStatus");
        String userId = "";
        String managerId = "";
        int typeStatusInt = 0;
        String typeStatusNames[] = {"未开始","进行中","已完成","等待其他人","已推辞"};
        if(task.get("beginDate")==null){
          beginDate = "";
        }else{
          beginDate = beginDate.substring(0,10);
        }
        if(task.get("endDate")==null){
          endDate = "";
        }else{
          endDate = endDate.substring(0,10);
        }
        String taskTypeName = "工作";
        if(task.get("taskType").equals("2")){
          taskTypeName = "个人 ";
        }
        if(task.get("taskStatus")!=null&&!task.get("taskStatus").equals("")){
          typeStatusInt = Integer.parseInt(typeStatus);
        }
        String content = "";
        if(task.get("content")!=null){
          content = task.get("content");
        }
        if(task.get("userId")!=null){
          userId = task.get("userId");
        }
        if(task.get("managerId")!=null&&!task.get("managerId").trim().equals("")){
          managerId = task.get("managerId");
        }
        if(managerId.trim().equals("")||managerId.equals(userId)){
          
        %>
        
           <tr class="TableData">
      <td>&nbsp;<input type="checkbox" name="email_select" value="<%=task.get("seqId") %>" onClick="check_one(self);">
      <td nowrap align="center"><%=task.get("taskNo") %></td>	
      <td nowrap align="center"><%=beginDate %></td>
      <td nowrap align="center"><%=endDate %></td>
      <td nowrap align="center"><%=taskTypeName %></td>
      <td nowrap align="center"><%=task.get("subject") %></td>
      <td nowrap align="center"><%=typeStatusNames[typeStatusInt] %></td>
      <td align="center" title=""><span class="important" title="<%=typeStatusNames[typeStatusInt] %></span>">&nbsp</span><a href="javascript:taskNote(<%=task.get("seqId") %>);"><%=content %></a></td>
      <td nowrap align="center">
          <a href="<%=contextPath%>/core/funcs/calendar/edittask.jsp?seqId=<%=task.get("seqId") %>"> 修改</a>
          <a href="javascript:deleteTaskById(<%=task.get("seqId") %>);"> 删除</a>
      </td>
          </tr>
          <%
        }else{
          
          %>
           <tr class="TableData">
      <td>&nbsp;
      <td nowrap align="center"><%=task.get("taskNo") %></td>	
      <td nowrap align="center"><%=beginDate %></td>
      <td nowrap align="center"><%=endDate %></td>
      <td nowrap align="center"><%=taskTypeName %></td>
      <td nowrap align="center"><%=task.get("subject") %></td>
      <td nowrap align="center"><%=typeStatusNames[typeStatusInt] %></td>
      <td align="center" title=""><span class="important" title="<%=typeStatusNames[typeStatusInt] %></span>">&nbsp</span><a href="javascript:taskNote(<%=task.get("seqId") %>);"><%=content %></a></td>
      <td nowrap align="center">
          <a href="<%=contextPath%>/core/funcs/calendar/edittask.jsp?seqId=<%=task.get("seqId") %>"> 修改</a>
      </td> 
         </tr>
          <%
        }
      %>

    <%} %>
 <tr class="TableControl">
    <td colspan="9">&nbsp;<input type="checkbox" name="allbox" id="allbox_for" onClick="check_all();">
      <label for="allbox_for">全选</label>&nbsp;
      <a href="javascript:deleteTask();" title="删除所选任务"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除</a>&nbsp;
    </td>
  </tr>
</table>&nbsp
<%}else {%>
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无符合条件的任务安排</div>
    </td>
  </tr>
</table>
<%} %>
<center> <input type="button" class="BigButton" value="返回" onclick="history.go(-1);"> </center>
</body>

</html>
