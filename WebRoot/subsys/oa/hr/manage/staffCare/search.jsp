<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String careType = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("careType")));
  String byCareStaffs = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("byCareStaffs")));
  String careDate1 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("careDate1")));
  String careDate2 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("careDate2")));
  String careFees1 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("careFees1")));
  String careFees2 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("careFees2")));
  String participants = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("participants")));
  String careContent = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("careContent")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工关怀查询 </title>
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
  param = "careType=" + encodeURIComponent("<%=careType%>");
  param += "&byCareStaffs=" + encodeURIComponent("<%=byCareStaffs%>");
  param += "&careDate1=" + encodeURIComponent("<%=careDate1%>");
  param += "&careDate2=" + encodeURIComponent("<%=careDate2%>");
  param += "&careFees1=" + encodeURIComponent("<%=careFees1%>");
  param += "&careFees2=" + encodeURIComponent("<%=careFees2%>");
  param += "&participants=" + encodeURIComponent("<%=participants%>");
  param += "&careContent=" + encodeURIComponent("<%=careContent%>");
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/care/act/YHHrStaffCareAct/queryCareListJson.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"careType",  width: '20%', text:"关怀类型" ,align: 'center' ,render:careItemFunc},
       {type:"data", name:"byCareStaffs",  width: '20%', text:"被关怀员工" ,align: 'center' ,render:staffNameFunc},
       {type:"data", name:"careFees",  width: '20%', text:"关怀开支费用" ,align: 'center' ,render:careFeesRMB},
       {type:"data", name:"participants",  width: '10%', text:"参与人" ,align: 'center' ,render:staffNameFunc},
       {type:"data", name:"careDate",  width: '10%', text:"关怀日期" ,align: 'center' ,render:splitDateFunc},
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
    WarningMsrg('无符合条件的员工关怀信息', 'msrg');
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