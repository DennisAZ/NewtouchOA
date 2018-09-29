<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String byEvaluStaffs = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("byEvaluStaffs")));
  String approvePerson = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("approvePerson")));
  String postName = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("postName")));
  String getMethod = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("getMethod")));
  String reportTime1 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("reportTime1")));
  String reportTime2 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("reportTime2")));
  String receiveTime1 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("receiveTime1")));
  String receiveTime2 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("receiveTime2")));
  String employPost = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("employPost")));
  String employCompany = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("employCompany")));
  String remark = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("remark")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>职称评定查询 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffTitleEvaluation/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffTitleEvaluation/js/staffTitleEvaluationLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
 	var param = "";
  param = "byEvaluStaffs=" + encodeURIComponent("<%=byEvaluStaffs%>");
  param += "&approvePerson=" + encodeURIComponent("<%=approvePerson%>");
  param += "&postName=" + encodeURIComponent("<%=postName%>");
  param += "&getMethod=" + encodeURIComponent("<%=getMethod%>");
  param += "&reportTime1=" + encodeURIComponent("<%=reportTime1%>");
  param += "&reportTime2=" + encodeURIComponent("<%=reportTime2%>");
  param += "&receiveTime1=" + encodeURIComponent("<%=receiveTime1%>");
  param += "&receiveTime2=" + encodeURIComponent("<%=receiveTime2%>");
  param += "&employPost=" + encodeURIComponent("<%=employPost%>");
  param += "&employCompany=" + encodeURIComponent("<%=employCompany%>");
  param += "&remark=" + encodeURIComponent("<%=remark%>");
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/titleEvaluation/act/YHHrStaffTitleEvaluationAct/queryTitleEvaluationListJson.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"byEvaluStaffs",  width: '15%', text:"评定对象" ,align: 'center'},
       {type:"data", name:"approvePerson",  width: '15%', text:"批准人" ,align: 'center'},
       {type:"data", name:"postName",  width: '15%', text:"获取职称" ,align: 'center'},
       {type:"data", name:"getMethod",  width: '15%', text:"获取方式" ,align: 'center' ,render:titleEvaluationItemFunc},
       {type:"data", name:"receiveTime",  width: '10%', text:"获取时间" ,align: 'center' ,render:splitDateFunc},
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
    WarningMsrg('无符合条件的职称评定信息', 'msrg');
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;职称评定信息查询结果 </span>
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
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/hr/manage/staffTitleEvaluation/query.jsp';">&nbsp;&nbsp;
</div>

<div id="msrg">
</div>
</body>
</html>