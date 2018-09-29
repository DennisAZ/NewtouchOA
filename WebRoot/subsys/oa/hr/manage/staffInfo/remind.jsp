<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>退休人员查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/manage/staffInfo/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/manage/staffInfo/js/staffInfoListLogic.js"></script>
<script type="text/javascript">

function doInit(){
	setDate();
	var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/staffInfo/act/YHRetireQueryAct/getThisMonthInfoListJson.act";
	var cfgs = {
	    dataAction: url,
	    container: "listContainer",
	    sortIndex: 1,
	    sortDirect: "desc",
	    colums: [
	       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
	       {type:"data", name:"deptId",  width: '20%', text:"部门", render:deptIdFunc},
	       {type:"data", name:"staffName",  width: '20%', text:"姓名", render:infoCenterFunc},
	       {type:"data", name:"staffSex",  width: '20%', text:"性别", render:staffSexFunc},
	       {type:"data", name:"staffBirth",  width: '20%', text:"出生年月", render:staffBirthFunc},
	       {type:"selfdef", text:"操作", width: '20%',render:detailOpts}]
	  };
	  pageMgr = new YHJsPage(cfgs);
	  pageMgr.show();
	  var total = pageMgr.pageInfo.totalRecord;
	  if(total){
	    showCntrl('listContainer');
	    var mrs = " 共 " + total + " 条记录 ！";
	    showCntrl('delOpt');
	  }else{
	    WarningMsrg('本月无退休人员！', 'msrg');
	  }
}

//日期
function setDate(){
	var date1Parameters = {
		inputId:'queryDate1',
		property:{isHaveTime:false},
		bindToBtn:'date1'
	};
	new Calendar(date1Parameters);
	
	var date2Parameters = {
		inputId:'queryDate2',
		property:{isHaveTime:false},
		bindToBtn:'date2'
	};
	new Calendar(date2Parameters);
}

function queryRetire(){
	var queryDate1 = $("queryDate1").value.trim();
	var queryDate2 = $("queryDate2").value.trim();
	if(queryDate1 == "" && queryDate2 == "" ){
		alert("请确定时间范围");
		return;
	}
	var url = contextPath + "/subsys/oa/hr/manage/staffInfo/retire.jsp?queryDate1=" + queryDate1 + "&queryDate2="+ queryDate2;
	newWindow(url,'500','500');
}

function clearValue(str){
	if(str){
		str.value = "";
	}
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit();">
<form method="post" name="form1" enctype="multipart/form-data" action="#">
<table border="0" width="100%" cellpadding="3" cellspacing="1" align="center" >
  <tr>
    <td class="TableHeader">
    &nbsp;历史退休人员查询&nbsp;
     从     <input type="text" name="queryDate1" id="queryDate1" size="10" maxlength="10" readonly="readonly" onfocus="clearValue(this);" class="BigInput" value="" >
     <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     至     <input type="text" name="queryDate2" id="queryDate2" size="10" maxlength="10" readonly="readonly" onfocus="clearValue(this);" class="BigInput" value="" >
     <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
     <input type="button" value="确定" onclick="queryRetire();" class="SmallButton">&nbsp;&nbsp;
   </td>
  </tr>
</table>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
</form>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="msrg">
</div>
</body>
</html>