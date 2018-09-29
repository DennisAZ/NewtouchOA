<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>BOM清单管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<!-- 封装表单的数据提交 -->
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
<script> 
var pageMgr = null;
function doInit(){
     var url = "<%=contextPath %>/SpringR/produce/getExamList/"+new Date().getTime();
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"code",  width: '10%', text:"编号",render:recordCenterFunc},  
	         {type:"hidden", name:"plan_code", text:"生产计划编号"}, 
	         {type:"data", name:"pp_id",  width: '10%', text:"生产计划",render:showProducePlanCenterFunc},  
	         {type:"data", name:"status",  width: '10%', text:"状态",render:selectStatusFunc},
	         {type:"data", name:"create_time",  width: '10%', text:"创建时间",render:recordCenterFunc},
	         {type:"hidden", name:"remark",  width: '1%', text:"备注",render:recordCenterFunc},
	         {type:"selfdef", text:"操作", width: '20%',render:opts}]
	    };
	    pageMgr = new YHJsPage(cfgs);
	    pageMgr.show();
	    var total = pageMgr.pageInfo.totalRecord;
	    if(total){
	      showCntrl('listContainer');
	      var mrs = " 共 " + total + " 条记录 ！";
	      //showCntrl('delOpt');
	    }else{
	      WarningMsrg('无符合条件的信息', 'msrg');
	    }
}

function showProducePlanCenterFunc(cellData, recordIndex, columIndex) {
	var plan_code = this.getCellData(recordIndex, "plan_code");
	var pp_id = this.getCellData(recordIndex, "pp_id");
	var aStr = "<a href='#' onclick=\"showPlan('"+pp_id+"')\">"+plan_code+"</a>"
	return "<center>" + aStr + "</center>";
}
function showPlan(planId){
	var url = "planDetail.jsp?planId="+planId+"&flag=1";
	window.open(url);
}
function showDetail(planId){
	var url = "showWHExam.jsp?planId="+planId;
	window.open(url);
}
function opts(cellData, recordIndex, columIndex){
	var planId = this.getCellData(recordIndex, "pp_id");
	return "<center><a href=\"javascript:void(0)\" onclick=\"showDetail('" + planId + "');\" >查看</a></center>";
}

function selectStatusFunc(cellData){
	var rtStr="";
	var url = "<%=contextPath %>/SpringR/warehouse/getStatusName?status="+cellData;
	jQuery.ajax({
	   type : 'POST',
	   async:false,
	   url : url,
	   success : function(jsonData){  
				rtStr = "<center>"+jsonData+"</center>";
	   }
	 });
	return rtStr;
}
function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;BOM清单管理</span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>


<div id="msrg">
</div>
</body>
</html>