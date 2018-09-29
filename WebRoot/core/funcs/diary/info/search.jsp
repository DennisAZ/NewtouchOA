<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String startDateStr = request.getParameter("startDate") == null ? "" :   request.getParameter("startDate");
  String endDateStr = request.getParameter("endDate") == null ? "" :   request.getParameter("endDate") ;
  String userId = request.getParameter("userId") == null ? "" :  request.getParameter("userId");
  String subject = request.getParameter("subject") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("subject")) ;
  String key1 = request.getParameter("key1") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("key1") );
  String key2 = request.getParameter("key2") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("key2")) ;
  String key3 = request.getParameter("key3") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("key3"));
  String diaType = request.getParameter("diaType") == null ? "" :  request.getParameter("diaType") ;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<title>日志查询</title>
<script type="text/javascript">
function bindsubjectRenderAction() {
  
}
var pageMgr = null;
function doInit() {
  $('title').innerHTML = "[" + getUserNameById('<%=userId%>') + " - 工作日志查询]";
  var param = "startDate=<%=startDateStr%>&endDate=<%=endDateStr%>&subject=<%=subject%>&key1=<%=key1%>&key2=<%=key2%>&key3=<%=key3%>&userId=<%=userId%>";
  param = encodeURI(param);
  var url =  contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/searchDiary.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    moduleName:"diary",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", width: 150, dataType:"int"},
       {type:"data", name:"diaDate", text:"日期", width: 150, dataType:"date"},       
       {type:"data", name:"subject", text:"日志标题", width: 150,render:subjectRender, bindAction:bindsubjectRenderAction},
       {type:"data", name:"attach", text:"附件", width: 350, dataType:"attach"},
       {type:"hidden", name:"attachId"},
       {type:"selfdef", text:"操作",width: 200, render:optRender}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 "+ total + " 条记录 ！";
    WarningMsrg(mrs, 'msrg');
  }else{
    WarningMsrg('无日志记录！', 'msrg');
  }
}

function deptRender(cellData, recordIndex, columIndex) {
  //alert(cellData);
  var deptName = getDeptNameById(cellData);
  return "<span id=\"span_" + recordIndex + "_" + columIndex + "\">" + deptName + "</span>";
}
function userRender(cellData, recordIndex, columIndex) {
  //alert(cellData);
  var deptName = getUserNameById(cellData);
  return "<span id=\"span_" + recordIndex + "_" + columIndex + "\">" + deptName + "</span>";
}
function subjectRender(cellData, recordIndex, columIndex) {
  //alert(cellData);
  var diaId = this.getCellData(recordIndex,"seqId");
  
  return "<A  id=\"sub_" + recordIndex + "_" + columIndex + "\" href=\"javascript:commentEdit('" + diaId + "','" + <%=userId%> + "');\">" + cellData + "</A >";
}
function optRender(cellData, recordIndex, columIndex) {
  //alert(cellData);
  var diaId = this.getCellData(recordIndex,"seqId");
  var userId = '<%=userId%>';
  var subject = this.getCellData(recordIndex,"subject");
  //var deptName = getDeptNameById(cellData);
  return "<a id=\"a1_" + recordIndex + "_" + columIndex + "\" href=\"javascript:share('"+ diaId + "','" + userId + "');\">指定共享</a>" + "&nbsp;" + 
  "<a id=\"a2_" + recordIndex + "_" + columIndex + "\" href=\"javascript:commentEdit('"+ diaId + "','" + userId + "');\">点评</a>";
}
function goBack(userId){
  location = contextPath + "/core/funcs/diary/info/userDiary.jsp?userId=" + userId;
}
</script>
</head>
<body onload="doInit()">
<div align="center" class="Big1">
<b><span id="title"></span></b>
</div>
<br>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="18" HEIGHT="18" align="absmiddle"><span class="big3"> 查询结果</span>
    </td>
  </tr>
</table>
<div id="listContainer" style="display:none">
</div>
<div id="msrg">
</div>
<center>
 <input type="button" class="SmallButton" onclick="javascript:goBack('<%=userId%>')" value="返回"></input>
</center>
</body>
</html>