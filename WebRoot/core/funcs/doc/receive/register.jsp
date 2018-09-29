<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="yh.core.funcs.doc.receive.data.YHDocConst"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%
YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER"); 
String webroot = request.getRealPath("/");
%>
<head>
<title>收文登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css"/>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/Javascript">

var cfgs = null;
var pageMgr = null;
function doInit(){
  var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocSignAct/getRegList.act";
  cfgs = {
      dataAction: url,
      container: "container",
      moduleName:"doc",
      colums:[
              {type:"data", name:"fromDept", text:"发送部门", width:"30%", dataType:"dateTime"},
              {type:"data", name:"sendTime", text:"发送时间", width:"15%", dataType:"dateTime",format:'yyyy-mm-dd HH:MM:ss'},
              {type:"data", name:"signTime", text:"签收时间", width:"15%", dataType:"dateTime",format:'yyyy-mm-dd HH:MM:ss'},
              {type:"data", name:"status", text:"状态", width:"10%", render:statusRender},
              {type:"hidden", name:"isOut"},
              {type:"hidden", name:"seqId"},
              {type:"selfdef",text:"操作", width:"20%",render:opRender}]
    };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}

function opRender(cellData, recordIndex, columIndex){
  var status = this.getCellData(recordIndex,"status");
  var seqId = this.getCellData(recordIndex,"seqId");
  var status = this.getCellData(recordIndex,"status");
  var result = "";
  if (status == '0') {
    result += "<input class='SmallButton' onclick='cancel("+seqId+","+status+" )' type='button' value='签收'/>"
  } 
  
  result += "&nbsp;&nbsp; <input class='SmallButton' onclick='cancel("+seqId+","+status+" )' type='button' value='查看详情'/>"
  result +="&nbsp;&nbsp;<input class='SmallButton' onclick='cancel("+seqId+","+status+" )' type='button' value='退回'/>";
  return result;
}
function cancel(seqId , status){
  
}
function statusRender(cellData, recordIndex, columIndex){
  var status = this.getCellData(recordIndex,"status");
  if (status == '0') {
    return "未签收";
  }
  return "";
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif"/><span class="big3">收文登记</span>
    </td>
  </tr>
</table>

<div id="container"></div>
</body>
</html>