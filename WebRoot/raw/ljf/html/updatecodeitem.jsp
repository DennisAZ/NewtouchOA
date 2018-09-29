<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="raw.ljf.curd.*" %>
<%@ include file="/core/inc/header.jsp" %>
<%
  String classNo = request.getParameter("classNo");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑CODE_ITEM</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
function doInit() {
	var mgr = new SelectMgr();
	mgr.addSelect({cntrlId: "classNo", tableName: "CODE_CLASS", codeField: "CLASS_NO", nameField: "CLASS_DESC", value: "<%=classNo%>", isMustFill: "1", filterField: "", filterValue: "", order: "", reloadBy: "", actionUrl: ""});
	mgr.loadData();	
	mgr.bindData2Cntrl();
}

function commitItem() {
  var url = "/yh/raw/ljf/curd/YHUpdateSonTableAct/updateSonTable.act";
  var rtJson = getJsonRs(url, mergeQueryString($("form3")));
  
  if (rtJson.rtState == "0") {
    alert(rtJson.rtMsrg);
    parent.navigateFrame.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function goback() {
  window.location.href="/yh/raw/ljf/curd/YHSonTableAct/getSonTable.act?classNo=<%=classNo%>";
}

</script>
</head>
<body onload="doInit()">
<%
  YHCodeItem code = (YHCodeItem)request.getAttribute("codeItem");
%>
<form method="post" name="form3" id="form3">
  <h2>修改CODE_ITEM的信息</h2>
  <input type="hidden" name="sqlId" value="<%=code.getSqlId() %>"/>
      主分类:&nbsp;&nbsp;&nbsp;<select name="classNo" id="classNo"></select><br></br>
  classCode:<input type="text" name="classCode" value="<%=code.getClassCode() %>"/><br/>
  sortNo:&nbsp;&nbsp;&nbsp;<input type="text" name="sortNo" value="<%=code.getSortNo() %>"/><br/>
  classDesc:<input type="text" name="classDesc" value="<%=code.getClassDesc() %>"/><br/>

  <input type="button" value="提交" onclick="commitItem()">
  <input type="button" value="返回" onclick="goback()">
</form>
</body>
</html>