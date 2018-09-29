<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
	String employeeName = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("EMPLOYEE_NAME")));
	String planName = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("PLAN_NAME")));
	String planNo = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("PLAN_NO")));
	String position = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("POSITION")));
	String employeeMajor = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("EMPLOYEE_MAJOR")));
	
	String employeePhone = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("EMPLOYEE_PHONE")));
	String transactorStep = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("transactorStep")));
	String nextTransaStep1 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("nextTransaStep1")));
	String nextTransaStep2 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("nextTransaStep2")));
	String nextTransaStep3 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("nextTransaStep3")));
	String nextTransaStep4 = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("nextTransaStep4")));
	String status = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("STATUS")));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>招聘筛选查询结果</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/filter/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/filter/js/hrfilterLogic.js"></script>
<script type="text/javascript">
function doInit(){
	var param = "";
	param = "employeeName=" + encodeURIComponent("<%=employeeName%>");
	param += "&planName=" + encodeURIComponent("<%=planName%>");
	param += "&planNo=" + encodeURIComponent("<%=planNo%>");
	param += "&position=" + encodeURIComponent("<%=position%>");
	param += "&employeeMajor=" + encodeURIComponent("<%=employeeMajor%>");
	
	param += "&employeePhone=" + encodeURIComponent("<%=employeePhone%>");
	param += "&transactorStep=" + encodeURIComponent("<%=transactorStep%>");
	param += "&nextTransaStep1=" + encodeURIComponent("<%=nextTransaStep1%>");
	param += "&nextTransaStep2=" + encodeURIComponent("<%=nextTransaStep2%>");
	param += "&nextTransaStep3=" + encodeURIComponent("<%=nextTransaStep3%>");
	param += "&nextTransaStep4=" + encodeURIComponent("<%=nextTransaStep4%>");
	param += "&status=" + encodeURIComponent("<%=status%>");
	
	var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct/queryFilterListJson.act?" + param;
	var cfgs = {
	    dataAction: url,
	    container: "listContainer",
	    sortIndex: 1,
	    sortDirect: "desc",
	    colums: [
				 {type:"selfdef", text:"选择", width: '3%', render:checkBoxRender},
	       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
	       {type:"data", name:"employeeName",  width: '20%', text:"应聘者姓名", render:employeeNameFunc},
	       {type:"data", name:"position",  width: '20%', text:"应聘岗位", render:infoCenterFunc},
	       {type:"data", name:"employeeMajor",  width: '10%', text:"所学专业", render:infoCenterFunc},
	       {type:"data", name:"employeePhone",  width: '10%', text:"联系电话", render:infoCenterFunc},
	       {type:"data", name:"transactorStep",  width: '20%', text:"发起人", render:transactorStepFunc},
	       {type:"hidden", name:"endFlag",  width: '10%', text:"标记", render:infoCenterFunc},
	       {type:"selfdef", text:"操作", width: '5',render:optsList}]
	  };
	  pageMgr = new YHJsPage(cfgs);
	  pageMgr.show();
	  var total = pageMgr.pageInfo.totalRecord;
	  if(total){
	    showCntrl('listContainer');
	    var mrs = " 共 " + total + " 条记录 ！";
	    showCntrl('delOpt');
	  }else{
	    WarningMsrg('无符合条件的招聘筛选信息！', 'msrg');
	  }
}

</script>
</head>
<body onload="doInit();">
	<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/infofind.gif" align="middle"><span class="big3">招聘筛选查询结果</span><br>
    	</td>
	</tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;"></div>
<div id="delOpt" style="display:none">
	<table class="TableList" width="100%">
		<tr class="TableControl">
			<td colspan="9"> 
				<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
				<a href="javascript:delete_all()" title="删除所选招聘筛选"><img src="<%=imgPath %>/delete.gif" align="middle">&nbsp;删除</a>
			</td>
		</tr>
	</table>
</div>
<div id="msrg"></div>
<br><center><input type="button" class="BigButton" value="返回" onclick="location.href = '<%=contextPath %>/subsys/oa/hr/recruit/filter/query.jsp'"></center>


</body>
</html>