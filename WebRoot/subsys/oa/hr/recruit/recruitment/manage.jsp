<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>招聘录用信息</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/recruitment/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/recruitment/js/recruitmentListLogic.js"></script>
<script type="text/javascript">
function doInit(){
	var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/recruitment/act/YHHrRecruitRecruitmentAct/getHrRecruitListJson.act";
	var cfgs = {
	    dataAction: url,
	    container: "listContainer",
	    sortIndex: 1,
	    sortDirect: "desc",
	    colums: [
				 {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
	       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
	       {type:"data", name:"planNo",  width: '10%', text:"计划编号", render:infoCenterFunc},
	       {type:"data", name:"jobStatus",  width: '20%', text:"招聘岗位", render:infoCenterFunc},
	       {type:"data", name:"applyerName",  width: '20%', text:"应聘人姓名", render:infoCenterFunc},
	       {type:"data", name:"assessingOfficer",  width: '20%', text:"录用负责人", render:getPersonNameFunc},
	       {type:"data", name:"assPassTime",  width: '10%', text:"录入日期", render:splitDateFunc},
	       {type:"hidden", name:"oaName",  width: '10%', text:"oa用户名"},
	       {type:"hidden", name:"expertId",  width: '10%', text:"建档案id"},
	       {type:"selfdef", text:"操作", width: '250',render:optsList}]
	  };
	  pageMgr = new YHJsPage(cfgs);
	  pageMgr.show();
	  var total = pageMgr.pageInfo.totalRecord;
	  if(total){
	    showCntrl('listContainer');
	    var mrs = " 共 " + total + " 条记录 ！";
	    showCntrl('delOpt');
	  }else{
	    WarningMsrg('无操作记录！', 'msrg');
	  }
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/infofind.gif" WIDTH="22" HEIGHT="22" align="middle"><span class="big3">&nbsp;招聘录用信息</span>
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
				<a href="javascript:delete_all()" title="删除所选招聘录用信息"><img src="<%=imgPath %>/delete.gif" align="middle">&nbsp;删除所选招聘录用信息</a>
			</td>
		</tr>
	</table>
</div>
<div id="msrg"></div>

</body>
</html>