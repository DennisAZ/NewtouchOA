<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
 <% 
  String runId = request.getParameter("runId");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收文情况 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffCare/js/util.js"></script>
<script> 
var runId = <%=runId %>;
function doInit(){
  var url = contextPath+"/yh/core/funcs/doc/act/YHMyWorkAct/getRecvInfo.act?runId="+runId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var listStr = rtJson.rtData;
    if(listStr.length > 0){
      $('flow_table').style.display = "";
    }
    else{
      $('flow_table2').style.display = "";
    }
    for(var i = 0 ; i < listStr.length ; i++){
      var statusStr = "";
      switch(listStr[i].sendStatus){
        case 0 :statusStr = "未签收";break;
        case 1 :statusStr = "已签收";break;
        case 2 :statusStr = "未登记";break;
      }
      var td = "<td align=center>" + listStr[i].sponsor +"</td>"
             + "<td align=center>" + statusStr +"</td>"
      var tr = new Element("tr" ,{"class" : "TableLine2"});
      $('dataBody').appendChild(tr);  
      tr.update(td);
    }
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/new_email[1].gif" align="absMiddle"><span class="big3">&nbsp;收文详情  </span>
   </td>
 </tr>
</table>
<br>
<table id="flow_table" border="0" width="100%" class="TableList"  style="clear:both;table-layout:fixed;display:none;" >
  <tr class="TableTr" id="flowTableHeader">
    <td class="TableHeader"  nowrap align="center" width=200><b>收文部门</b></td>
    <td class="TableHeader"  nowrap align="center" width=200><b>接收情况</b></td>
  </tr>
  <tbody id="dataBody"></tbody>
</table>

<table id="flow_table2" class="MessageBox" align="center" width="320" style="display:none;">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">无收文</div>
    </td>
  </tr>
</table>
</body>
</html>