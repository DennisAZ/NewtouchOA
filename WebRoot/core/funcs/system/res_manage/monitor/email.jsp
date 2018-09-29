<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<%
String toId = request.getParameter("toId");
String copyToId = request.getParameter("copyToId");
String subject = request.getParameter("subject");
String attachmentName = request.getParameter("attachmentName");
String content = request.getParameter("content");
String beginDate = request.getParameter("beginDate");
String endDate = request.getParameter("endDate");
%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>邮件查询</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function delete_mail(EMAIL_ID)
{
 delete_str=get_checked();
 if(delete_str=="")
 {
   alert("请至少选择一封");
   return;
 }
 msg='确认要删除所选的邮件吗？';
 if(window.confirm(msg)){
   var url = contextPath + "/yh/core/funcs/system/resManage/act/YHEmailResManageAct/deleteEmail.act";;
   var json = getJsonRs(url , "emailId=" + delete_str);
   if (json.rtState == "0") {
     $("dataList").update("");
     doInit();
   }
 }
}
function check_all()
{
 var checkboxs = document.getElementsByTagName("input");
 for (i=0;i<checkboxs.length;i++)
 { 
   var checkbox = checkboxs[i];
   if (checkbox.name == "email_select") {
      if ($('allbox').checked) {
        checkbox.checked = true;
      } else {
        checkbox.checked = false;
      }
   }
 }
}

function check_one(el)
{
   if(!el.checked)
      $("allbox").checked=false;
}
function get_checked()
{
  var checkboxs = document.getElementsByTagName("input");
  var checked_str = "";
  for (i=0;i<checkboxs.length;i++)
  { 
    var el = checkboxs[i];
    if (el.name == "email_select") {
      if (el.checked) {  
         val = el.value;
         checked_str += val + ",";
      }
    }
  }
  return checked_str;
}
var  toId = "<%=toId %>";
var copyToId = "<%=copyToId%>";
var subject = "<%=subject%>";
var attachmentName = "<%=attachmentName%>";
var content = "<%=content%>";
var beginDate = "<%=beginDate%>";
var endDate = "<%=endDate%>";
function doInit() {
  var param = "toId=" + toId + "&copyToId=" + copyToId + "&subject=" + subject + "&attachmentName=" + attachmentName + "&content=" + content + "&beginDate="+beginDate + "&endDate=" + endDate;
  var url = contextPath + "/yh/core/funcs/system/resManage/act/YHEmailResManageAct/searchEmail.act";
  var json = getJsonRs(url , param);
  if (json.rtState == "0") {
    var list = json.rtData;
    
    if (list.length <= 0) {
      $("noData").show();
      $("table").hide();
      return ;
    } 
   for (var i = 0 ;i < list.length ; i++ ) {
      var data = list[i];
      addTr(data , i);
    }
   $("table").show();
  }
}

function addTr(data , i) {
  var subject1 = data.subject
  var emailId = data.emailId;
  var statusDesc = data.statusDesc;
  var deleteFlag = data.deleteFlag ;
  var fromName = data.fromName;
  var toName = data.toName;
  var attId = data.attId;
  var attName = data.attName;
  var sendTime = data.sendTime;
  var tr = new Element("tr" , {'class' : 'TableData'});
  $("dataList").appendChild(tr);
  var td = new Element("td");
  td.update("&nbsp;<input type=\"checkbox\" name=\"email_select\" value=\"" + emailId + "\" onClick=\"check_one(self);\">");
  tr.appendChild(td);
  
  td = new Element("td");
  td.align = 'center';
  if(statusDesc == '0')
    statusDesc ="<img src=\"<%=imgPath %>/email_close.gif\" alt=\"收件人未读\">";
  else
    statusDesc ="<img src=\"<%=imgPath %>/email_open.gif\" alt=\"收件人已读\">";

  td.update(statusDesc);
  tr.appendChild(td);

  td = new Element("td");
  td.align = 'center';
  tmp = "" ;
  if (deleteFlag == "2") {
    tmp = "<img src='<%=imgPath %>/email_delete.gif' align='absMiddle' alt='发件人已删除'>";
  }
  var str = fromName + tmp
  td.update(str);
  tr.appendChild(td);

  td = new Element("td");
  td.align = 'center';
  tmp = "" ;
  if (deleteFlag == "1") {
    tmp = "<img src='<%=imgPath %>/email_delete.gif' align='absMiddle' alt='收件人已删除'>";
  }
  str = toName + tmp
  td.update(str);
  tr.appendChild(td);
  
  td = new Element("td");
  str = "<a href=\"readEmail.jsp?emailId=" + emailId + "\">" + subject1 + "</a>";
  td.update(str);
  tr.appendChild(td);

  
  td = new Element("td");
  td.align = 'center';
  td.update("<div id='attr_" + i + "'></div>");
  tr.appendChild(td);
  
  td = new Element("td");
  td.align = 'center';
  td.update(sendTime);
  tr.appendChild(td);
  var  selfdefMenu = {
    	office:["downFile","dump","read"], 
      img:["downFile","dump","play"],  
      music:["downFile","dump","play"],  
	    video:["downFile","dump","play"], 
	    others:["downFile","dump"]
		}
  attachMenuSelfUtil("attr_"+i,"email",attName ,attId, i,'',emailId,selfdefMenu);
}
</script>
</head>
<body onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/infofind.gif" align="absmiddle"><span class="big3"> 查询结果</span>
    <input type="button" value="返回 " class="SmallButton" onclick='location.href = "resquery.jsp"'/>
    </td>
  </tr>
</table>
<form action="">
<table id="table" style="display:none" class="TableList" width="100%">
  <tr class="TableHeader" align="center">
      <td width="40">选择</td>
      <td width="40">状态</td>
      <td>发件人</td>
      <td>收件人</td>
      <td>主题</td>
      <td>附件</td>
      <td width="120">发送时间 <img border=0 src="<%=imgPath %>/arrow_down.gif" width="11" height="10"></td>
    </tr>
    <tbody id="dataList">
    </tbody>
    <tr class="TableData">
      <td colspan="10">
         &nbsp;<input type="checkbox" name="allbox" id="allbox" onClick="check_all();"><label for="allbox_for">全选</label>
         &nbsp;<input type="button"  value="删除" class="SmallButton" onClick="delete_mail();" title="删除所选邮件"> &nbsp;
      </td>
    </tr>
    </table>
    </form>
    <div id="noData" align=center style="display:none">
   <table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="msgInfo" class="msg info">无符合条件的查询结果
            </td>
        </tr>
    </tbody>
</table>
<div><input type="button" value="返回 " class="SmallButton" onclick='location.href = "resquery.jsp"'/></div>
</div>
</body>
</html>