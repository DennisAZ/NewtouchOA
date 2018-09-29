<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String templateName = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("templateName")));
  String templateFileName = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("templateFileName")));
  String templateType = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("templateType")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>模板查询 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffCare/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffCare/js/staffCareLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
 	var param = "";
  param = "templateName=" + encodeURIComponent("<%=templateName%>");
  param += "&templateFileName=" + encodeURIComponent("<%=templateFileName%>");
  param += "&templateType=" + encodeURIComponent("<%=templateType%>");
  var url = "<%=contextPath%>/yh/cms/template/act/YHTemplateAct/queryTemplate.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
      {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
      {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
      {type:"data", name:"templateName",  width: '20%', text:"模板名称" ,align: 'center'},
      {type:"data", name:"templateFileName",  width: '20%', text:"模板文件名" ,align: 'center'},
      {type:"data", name:"templateTypeName",  width: '20%', text:"模板类型" ,align: 'center'},
      {type:"hidden", name:"templateType", text:"模板Id", dataType:"int"},
      {type:"selfdef", text:"操作", width: '15%',render:opts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
    showCntrl('backDiv');
  }else{
    WarningMsrg('无模板信息', 'msrg');
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;员工关怀信息查询结果 </span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;
         <a href="javascript:deleteAll();" title="删除所选记录"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选记录</a>&nbsp;
      </td>
 </tr>
</table>
</div>
<div id="backDiv" style="display:none" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/hr/manage/staffCare/query.jsp';">&nbsp;&nbsp;
</div>

<div id="msrg">
</div>
</body>
</html>