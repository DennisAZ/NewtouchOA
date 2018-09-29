<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<%
String toId = request.getParameter("toId");
String copyToId = request.getParameter("copyToId");
String content = request.getParameter("content");
String beginDate = request.getParameter("beginDate");
String endDate = request.getParameter("endDate");
%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信查询</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function delete_one(SMS_ID)
{
 msg='确认要删除该内部短信吗？';
 if(window.confirm(msg))
 {
  URL= contextPath + "/yh/core/funcs/system/resManage/act/YHSmsResManageAct/deleteSms.act?SMS_ID=" + SMS_ID;
  var json = getJsonRs(URL , "smsId=" + SMS_ID);
  if (json.rtState == "0") {
    $("dataList").update("");
    doInit();
  }
 }
}
function show_sms(SMS_ID)
{  
  var actionUrl = contextPath + "/core/funcs/sms/showcontent.jsp?seqId=" + SMS_ID;
  openWindow(actionUrl, "查看短信正文", '360', '160')
}
var  toId = "<%=toId %>";
var copyToId = "<%=copyToId%>";
var content = "<%=content%>";
var beginDate = "<%=beginDate%>";
var endDate = "<%=endDate%>";
function doInit() {
  var param = "toId=" + toId + "&copyToId=" + copyToId + "&content=" + content + "&beginDate="+beginDate + "&endDate=" + endDate;
  var url = contextPath + "/yh/core/funcs/system/resManage/act/YHSmsResManageAct/searchSms.act";
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
      addTr(data);
    }
   $("table").show();
  }
}

function addTr(data) {
  var smsId = data.smsId;
  var bodyId = data.bodyId;
  var fromName = data.fromName;
  var toName = data.toName;
  var sendTime = data.sendTime;
  var content = data.content;
  if (!content) {
    content = "....";
  }
  var tr = new Element("tr" , {'class' : 'TableData'});
  $("dataList").appendChild(tr);
  td = new Element("td");
  td.align = 'center';
  td.update(fromName);
  tr.appendChild(td);

  td = new Element("td");
  td.align = 'center';
  td.update(toName);
  tr.appendChild(td);

  td = new Element("td");
  td.align = 'center';
  var tmp = "<a href=\"javascript:show_sms("+bodyId+");\">" + content + "</a>";
  td.update(tmp);
  tr.appendChild(td);
  
  td = new Element("td");
  td.align = 'center';
  td.update(sendTime);
  tr.appendChild(td);

  td = new Element("td");
  td.align = 'center';
  var str = "<a href=\"javascript:delete_one("+smsId+");\">删除</a>";
  td.update(str);
  tr.appendChild(td);
}
</script>
</head>
<body onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/infofind.gif" align="absmiddle"><span class="big3"> 查询结果</span><input type="button" value="返回 " class="SmallButton" onclick='location.href = "resquery.jsp"'/>
    </td>
  </tr>
</table>
<table id="table" style="display:none" class="TableList" width="100%">
  <tr class="TableHeader" align="center">
      <td width="80">发信人</td>
      <td width="80">收信人</td>
      <td>内容</td>
      <td width="130">发送时间 <img border=0 src="<%=imgPath %>/arrow_down.gif" width="11" height="10"></td>
      <td nowrap align="center">操作</td>
    </tr>
    <tbody id="dataList">
    </tbody>
    </table>
    <div id="noData" align=center style="display:none">
   <table class="MessageBox" width="250">
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