<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>客户列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css">
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

<script type="text/javascript">
var pageMgr = null;
function doInit(){
     var url = "<%=contextPath %>/SpringR/saleOrder/custList/"+new Date().getTime();
	  var cfgs = {
	      dataAction: url,
	      container: "listDiv",
	      colums: [
	        
	         {type:"data", name:"corCode",  width: '10%', text:"客户编号",render:recordCenterFunc},    
	         {type:"data", name:"corName",  width: '10%', text:"客户名称",render:recordCenterFunc},
	         {type:"data", name:"corMne",  width: '10%', text:"客户别名",render:recordCenterFunc},    
	         {type:"data", name:"corPhone",  width: '10%', text:"电话",render:recordCenterFunc},
	         {type:"data", name:"corCellPhone",  width: '10%', text:"手机",render:recordCenterFunc},    
	         {type:"data", name:"corAddress",  width: '10%', text:"地址",render:recordCenterFunc},
	         {type:"data", name:"corEmail",  width: '10%', text:"Email",render:recordCenterFunc},
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
	      WarningMsrg('无符合条件的客户信息', 'msrg');
	    }
}


function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}
function opts(cellData, recordIndex, columIndex){
	var corName = this.getCellData(recordIndex, "corName");

	var corCode = this.getCellData(recordIndex, "corCode");
	return "<center><a href=\"javascript:void(0)\" onclick=\"add('" + corName +","+ corCode+"');\" >添加</a></center>";
}

function add(corName,corCode){
	var corCust=corName+","+corCode;
 	window.parent.returnValue=corCust;
 	window.parent.close();
  } 
</script>
</head>
<body topmargin="5" onload="doInit()">

<div id="listRecords" style="width:450px">
	<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" >
	  <tr>
	    <td class="Big">
	    <img src="<%=imgPath%>/infofind.gif" align="absMiddle">
	    <span class="big3">客户列表</span>
	      
	    </td>
	  </tr>
	</table>
	<div id="listDiv" align="left"></div>
	<table border="0" width="450px" cellspacing="0" cellpadding="4" class="small" style="margin-left:37px;">
	  <tr class="TableControl">
	    <td class="Big">
	      <input type="checkbox" onclick="checkAll(this)" >&nbsp&nbsp&nbsp;<span>全选</span>&nbsp&nbsp&nbsp;
	      <input type="button" class="BigButton" value="添加" onClick="return add()">
	    </td>
	  </tr>
	</table>
</div>

</body>
</html>