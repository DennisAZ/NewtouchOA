<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>离职人员</title>
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
function doInit(){
	var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/staffInfo/act/YHHrDimissionAct/getDimissionListJson.act";
	var cfgs = {
	    dataAction: url,
	    container: "listContainer",
	    sortIndex: 1,
	    sortDirect: "desc",
	    colums: [
	       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
	       {type:"data", name:"deptId",  width: '20%', text:"原属部门", render:deptIdFunc},
	       {type:"data", name:"userName",  width: '20%', text:"姓名", render:infoCenterFunc},
	       {type:"data", name:"staffSex",  width: '10%', text:"性别", render:staffSexFunc},
	       {type:"data", name:"staffBirth",  width: '15%', text:"出生年月", render:staffBirthFunc},
	       {type:"data", name:"userId",  width: '20%', text:"离职原因", render:dimissionCause},
	       {type:"selfdef", text:"操作", width: '5',render:opts}]
	  };
	  pageMgr = new YHJsPage(cfgs);
	  pageMgr.show();
	  var total = pageMgr.pageInfo.totalRecord;
	  if(total){
	    showCntrl('listContainer');
	    var mrs = " 共 " + total + " 条记录 ！";
	    $("totalDiv").innerHTML = total;
	    $("spanDiv").show();
	    showCntrl('delOpt');
	  }else{
	    WarningMsrg('无离职人员！', 'msrg');
	  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/hrms.gif" HEIGHT="20"><span class="big3">&nbsp;离职人员</span><br />
    </td>
    
    <td valign="bottom" class="small1">
    <div id="spanDiv" style="display: none;">共&nbsp;<span class="big4" id="totalDiv"></span>&nbsp;条信息</div>
   </td>

    
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="msrg">
</div>
<br>
</body>
</html>