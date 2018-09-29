<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
function readOffice(){}
function editOffice(){}
function doInit() {
  var param = "";
  param = encodeURI(param);
  var url =  contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    colums: [
       {type:"hidden", name:"seqId", width: 40, dataType:"int"},
       {type:"data", name:"logTime", text:"时间", width: 80},   
       {type:"data", name:"logUid", text:"人员", width: 300},
       {type:"data", name:"logType", text:"操作", width: 250},
       {type:"data", name:"sendTime", text:"IP", width: 150}   
       ]
  };
  var pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('total').innerHTML = total;
    showCntrl('listContainer'); 
  }else{
    WarningMsrg('收件箱中无邮件！', 'msrg');
 }
}
</script>
</head>
<body>
<div id="listContainer"></div>
</body>
</html>