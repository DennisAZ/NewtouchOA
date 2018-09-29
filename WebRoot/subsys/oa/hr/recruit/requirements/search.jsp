<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String requNo = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("requNo")));
  String requJob= YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("requJob")));
  String requNum= YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("requNum")));
  String requDeptId= YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("requDeptId")));
  String startTime= YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("startTime")));
  String endTime= YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("endTime")));
 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>招聘需求查询 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/recruit/requirements/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/recruit/requirements/js/recruitRequirementsLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
 	var param = "";
  param = "requNo=" + encodeURIComponent("<%=requNo%>");
  param += "&requNum=" + encodeURIComponent("<%=requNum%>");
  param += "&requJob=" + encodeURIComponent("<%=requJob%>");
  param += "&requDeptId=" + encodeURIComponent("<%=requDeptId%>");
  param += "&startTime=" + encodeURIComponent("<%=startTime%>");
  param += "&endTime=" + encodeURIComponent("<%=endTime%>");

  var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/requirements/act/YHHrRecruitRequirementsAct/queryRecruitRequirementsListJson.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"requNo",  width: '10%', text:"需求编号" ,align: 'center'},
       {type:"data", name:"requJob",  width: '10%', text:"需求岗位" ,align: 'center' },
       {type:"data", name:"requNum",  width: '10%', text:"需求人数" ,align: 'center' },
       {type:"data", name:"requDept",  width: '10%', text:"需求部门" ,align: 'center' ,render:deptFunc},
       {type:"data", name:"requTime",  width: '10%', text:"用工日期" ,align: 'center' ,render:splitDateFunc},
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
    WarningMsrg('无符合条件的招聘需求信息', 'msrg');
  }
}

function deptFunc(cellData, recordIndex, columIndex){
  var url = contextPath + "/yh/subsys/oa/hr/recruit/requirements/act/YHHrRecruitRequirementsAct/getDeptName.act";
  var rtJson = getJsonRs(url, "deptIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;招聘需求信息查询结果 </span>
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
  <input type="button" value="返回" class="BigButton" onClick="window.history.go(-1);">&nbsp;&nbsp;
</div>

<div id="msrg">
</div>
</body>
</html>