<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String tPlanName = request.getParameter("tPlanName")== null ? "" :   YHUtility.encodeSpecial(request.getParameter("tPlanName"));
  String tChannel = request.getParameter("tChannel")== null ? "" :   YHUtility.encodeSpecial(request.getParameter("tChannel"));
  String tCourseTypes = request.getParameter("tCourseTypes") == null ? "" : YHUtility.encodeSpecial(request.getParameter("tCourseTypes"));
  String tAddress = request.getParameter("tAddress") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("tAddress"));
  String tInstitutionName = request.getParameter("tInstitutionName") == null ? "" : YHUtility.encodeSpecial(request.getParameter("tInstitutionName"));
  String courseStartDate1 = request.getParameter("courseStartDate1") == null ? "" : YHUtility.encodeSpecial(request.getParameter("courseStartDate1"));
  String courseStartDate2 = request.getParameter("courseStartDate2") == null ? "" : YHUtility.encodeSpecial(request.getParameter("courseStartDate2"));

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理培训计划 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/trainingPlanllogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
 	var param = "";
  param = "tPlanName=" + encodeURIComponent("<%=tPlanName%>");
  param += "&tChannel=" + encodeURIComponent("<%=tChannel%>");
  param += "&tCourseTypes=" + encodeURIComponent("<%=tCourseTypes%>");
  param += "&tAddress=" + encodeURIComponent("<%=tAddress%>");
  param += "&tInstitutionName=" + encodeURIComponent("<%=tInstitutionName%>");
  param += "&courseStartDate1=" + encodeURIComponent("<%=courseStartDate1%>");
  param += "&courseStartDate2=" + encodeURIComponent("<%=courseStartDate2%>");
  var url = "<%=contextPath%>/yh/subsys/oa/training/act/YHHrTrainingPlanAct/queryTrainingPlanListJson.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
				 {type:"selfdef", text:"选择", width: '3%', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"tPlanNo",  width: '10%', text:"培训计划编号", render:trainingCenterFunc},       
         {type:"data", name:"tPlanName",  width: '10%', text:"培训计划名称", render:trainingCenterFunc},
         {type:"data", name:"tChannel",  width: '25%', text:"培训渠道", render:tChannelFunc},
         {type:"data", name:"tCourseTypes",  width: '10%', text:"培训形式", render:tCourseTypesFunc},
         {type:"selfdef", text:"操作", width: '15%',render:optsList}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      //$("totalSpan").innerHTML = total;
      showCntrl('listContainer');
      //var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
      $("returnDiv").show();
    }else{
      //$("spanDiv").style.display = 'none';
      $("msrg").show();
    }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" width="22" height="18"><span class="big3" id="statusSpan">&nbsp;培训计划查询结果  </span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
	<table class="TableList" width="100%">
		<tr class="TableControl">
			<td colspan="9"> 
				<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
				<a href="javascript:delete_all()" title="删除所选培训计划信息"><img src="<%=imgPath %>/delete.gif" align="middle">&nbsp;删除</a>
			</td>
		</tr>
	</table>
</div>
<div id="returnDiv" style="display:none" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/training/plan/queryTrainingPlan.jsp';">&nbsp;&nbsp;
</div>
<div id="msrg" style="display: none">
<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无符合条件的培训计划信息！</div>
    </td>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="javascript:location.href='<%=contextPath %>/subsys/oa/training/plan/queryTrainingPlan.jsp'"></center>
</div>

</body>
</html>