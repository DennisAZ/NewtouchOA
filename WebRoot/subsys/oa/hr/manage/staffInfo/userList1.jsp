<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String deptId = request.getParameter("deptId");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户档案列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/manage/staffInfo/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/manage/staffInfo/js/staffInfoListLogic.js"></script>
<script type="text/javascript">
var deptId = "<%=deptId%>"
function doInit(){
	showDeptName();
	var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct/getStaffInfoListJson.act?deptId=<%=deptId%>";
	var cfgs = {
	    dataAction: url,
	    container: "listContainer",
	    sortIndex: 1,
	    sortDirect: "desc",
	    colums: [
	       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
	       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
	       {type:"data", name:"staffName",  width: '10%', text:"姓名", render:infoCenterFunc},
	       {type:"data", name:"staffSex",  width: '20%', text:"性别", render:staffSexFunc},
	       {type:"data", name:"staffBirth",  width: '15%', text:"出生年月", render:staffBirthFunc},
	       {type:"data", name:"staffNationality",  width: '10%', text:"民族", render:infoCenterFunc},
	       {type:"data", name:"staffNativePlace",  width: '10%', text:"籍贯", render:staffNativePlaceFunc},
	       {type:"data", name:"staffPoliticalStatus",  width: '10%', text:"政治面貌", render:staffPoliticalStatusFunc},
	       {type:"data", name:"staffCardNo",  width: '15%', text:"身份证号码", render:infoCenterFunc},
	       {type:"selfdef", text:"操作", width: '5',render:opts}]
	  };
	  pageMgr = new YHJsPage(cfgs);
	  pageMgr.show();
	  var total = pageMgr.pageInfo.totalRecord;
	  if(total){
	    showCntrl('listContainer');
	    var mrs = " 共 " + total + " 条记录 ！";
	    showCntrl('delOpt');
	  }else{
	    WarningMsrg('该部门尚未定义人事档案信息！', 'msrg');
	  }
}
function showDeptName(){
	if($("deptId") && $("deptId").value.trim() && $("deptId").value != "0" && $("deptId").value != "deptId"){
		bindDesc([{cntrlId:"deptId", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
	}else if($("deptId") && ($("deptId").value == "0" || $("deptId").value == "ALL_DEPT")){
		$("deptId").value = "0";
		$("deptIdDesc").innerHTML = "全体部门";
	}
	if($("deptIdDesc")){
		$("deptIdDesc1").innerHTML = $("deptIdDesc").innerHTML;
	}
}


function newInfoFunc(){
  location = "<%=contextPath%>/subsys/oa/hr/manage/staffInfo/newInfo.jsp?deptId=<%=deptId%>";
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
  <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="middle"><span class="big3" id=""> 新建用户档案 （<span id="deptIdDesc"></span>）</span>
  </td>
 </tr>
</table>
<div align="center">
  <input type="button" value="新建用户档案" class="BigButtonC" title="新建用户档案" onclick="newInfoFunc();">
</div>
<br>
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="middle"><span class="big3"> [<span id="deptIdDesc1"></span>]用户档案列表</span>
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
         <a href="javascript:deleteInfo();" title="删除所选记录"><img src="<%=imgPath%>/delete.gif" align="middle">删除</a>&nbsp;
         <a href="javascript:deleteAll('<%=deptId %>');" title="删除所选记录"><img src="<%=imgPath%>/delete.gif" align="middle">删除全部</a>&nbsp;
      </td>
 </tr>
</table>
</div>

<div id="msrg">
</div>
<input type="hidden" name="deptId" id="deptId" class="BigStatic" value="<%=deptId %>">
</body>
</html>