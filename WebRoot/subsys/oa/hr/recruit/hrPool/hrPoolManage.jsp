<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人才档案管理</title>
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
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/hrPool/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/hrPool/js/hrPoolLogic.js"></script>
<script type="text/javascript">
function doInit(){
	var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/hrPool/act/YHHrRecruitPoolAct/getHrPoolListJson.act";
	var cfgs = {
	    dataAction: url,
	    container: "listContainer",
	    sortIndex: 1,
	    sortDirect: "desc",
	    colums: [
				 {type:"selfdef", text:"选择", width: '3%', render:checkBoxRender},
	       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
	       {type:"data", name:"planName",  width: '10%', text:"计划名称", render:infoCenterFunc},
	       {type:"data", name:"employeeName",  width: '10%', text:"应聘者姓名", render:infoCenterFunc},
	       {type:"data", name:"employeeBirth",  width: '10%', text:"出生日期", render:splitDateFunc},
	       {type:"data", name:"employeePhone",  width: '10%', text:"联系电话", render:infoCenterFunc},
	       {type:"data", name:"employeeHighestSchool",  width: '10%', text:"学历", render:getCodeNameFunc},
	       {type:"data", name:"employeeMajor",  width: '10%', text:"专业", render:getCodeNameFunc},
	       {type:"data", name:"position",  width: '10%', text:"应聘岗位", render:getCodeNameFunc},
	       {type:"data", name:"addTime",  width: '10%', text:"应聘时间", render:splitDateFunc},
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
	    WarningMsrg('无操作记录！', 'msrg');
	  }
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" WIDTH="22" HEIGHT="22" align="middle"><span class="big3">&nbsp;管理人才档案</span>
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
				<a href="javascript:delete_all()" title="删除所选人才档案"><img src="<%=imgPath %>/delete.gif" align="middle">&nbsp;删除所选记录</a>
			</td>
		</tr>
	</table>
</div>
<div id="msrg"></div>

</body>
</html>