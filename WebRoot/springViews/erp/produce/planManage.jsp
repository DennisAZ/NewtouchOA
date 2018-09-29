<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>生产批计划管理</title>
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
     var url = "<%=contextPath %>/SpringR/produce/getPlanList/"+new Date().getTime();
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"code",  width: '10%', text:"计划单号",render:recordCenterFunc},  
	          {type:"hidden", name:"ppid", text:"ppid"},
	         {type:"data", name:"pnCode",  width: '10%', text:"所属通知单",render:recordCenterFunc},    
	         {type:"hidden", name:"person_id",  width: '1%', text:"人员id",render:recordCenterFunc},
	         {type:"hidden", name:"person",  width: '1%', text:"创建人",render:recordCenterFunc},
	         {type:"data", name:"create_time",  width: '10%', text:"创建时间",render:recordCenterFunc},
	         {type:"data", name:"status",  width: '10%', text:"状态",render:selectStatusFunc},
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
	      WarningMsrg('无符合条件的生产计划信息', 'msrg');
	    }
}

function opts(cellData, recordIndex, columIndex){
	var planId = this.getCellData(recordIndex, "id");
	var code = this.getCellData(recordIndex, "code");
	var status = this.getCellData(recordIndex, "status");
	if(status == "0"){
		return "<center><a href=\"javascript:void(0)\" onclick=\"createFlowCheck('" + planId + "','"+code+"');\" >提交审核</a>&nbsp;<a href=\"javascript:void(0)\" onclick=\"planDetail('" + planId + "');\" >查看</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + planId + "');\" >编辑</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" + planId + "');\" >删除</a></center>";
	}else{
		return "<center><a href=\"javascript:void(0)\" onclick=\"planDetail('" + planId + "');\" >查看</a></center>";
	}
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


function edit(id){
	window.location.href='planUpdate.jsp?planId='+id;
}
function planDetail(id){
	window.location.href='planDetail.jsp?planId='+id;
}
function deletes(id){
	var url = "<%=contextPath %>/SpringR/produce/deletePlan?planId="+id;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("删除成功");
		    window.location.reload();
		  }
		  else{
			   alert("删除失败"); 
		  }
	   }
	 });
}

function createFlowCheck(id,code){
	var para = "KEY="+id+"&CODE="+code;
	createNewWork(para,false);
}

function createNewWork(para , isNotOpenWindow){
  var url = contextPath + "/yh/subsys/oa/saleOrder/act/YHERPApplyAct/planApply.act";
  var json = getJsonRs(url ,  para);
  if(json.rtState == "0"){
    var url2 =  json.rtData.url;
    if (isNotOpenWindow) {
      location.href = url2;
    } else {
      window.open(url2);
    }
  }else{
    alert(json.rtMsrg);
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;生产计划管理</span>
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