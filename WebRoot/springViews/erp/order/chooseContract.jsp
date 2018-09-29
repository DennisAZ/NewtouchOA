<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<html>
	<head>
	<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="pragma" content="no-cache" />
		<link rel="stylesheet" href="<%=cssPath%>/style.css">
		<link rel="stylesheet" href="<%=cssPath%>/page.css">
		<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css">
		<!-- 封装表单的数据提交 -->
		<script type="text/javascript"src="<%=contextPath%>/springViews/js/jquery-1.4.2.js">
		jQuery.noConflict();
		</script>
		<script type="text/javascript" src="<%=contextPath%>/springViews/js/json.js" /></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
		<script> 
		
function doInit(){
	 var url = "<%=contextPath %>/SpringR/contract/contractManage/"+new Date().getTime();
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"code",  width: '20%', text:"合同编号",render:recordCenterFunc},    
	         {type:"data", name:"name",  width: '20%', text:"合同名称",render:recordCenterFunc},    
	         {type:"data", name:"type",  width: '20%', text:"合同类型",render:recordCenterFunc},
	         {type:"data", name:"remark",  width: '20%', text:"备注",render:recordCenterFunc},
	         {type:"hidden", name:"attachmentId",  width: '20%', text:"合同uuid",render:recordCenterFunc},
	         {type:"hidden", name:"attachmentName",  width: '20%', text:"合同文件名字",render:recordCenterFunc},
	         {type:"selfdef", text:"操作", width: '20%',render:opts}]
	    };
   var  pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无信息','listContainer', 'msrg');
    }
}
function recordCenterFunc(cellData){
  return "<center>" + cellData + "</center>";
}

function opts(cellData, recordIndex, columIndex){
	var id = this.getCellData(recordIndex, "id");
	var code = this.getCellData(recordIndex, "code");
	return "<center><a href=\"javascript:void(0)\" onclick=\"choose('" + id + "','" + code + "');\" >选择</a></center>";
}

function choose(id,code){
	
var str = "<a href='#' onclick='showContract(\""+id+"\")'>"+code+"</a>";
	var parentWindowObj = window.dialogArguments;
	var arr = parentWindowObj["arr"];
	parentWindowObj.$(arr[0]).innerHTML="";
	parentWindowObj.$(arr[0]).innerHTML=str;
	parentWindowObj.$(arr[1]).value=id;
	window.close();
}
function openAdd(){
	openDialogResize('contractAdd.jsp',  800, 600);
}
</script>
	</head>
	<body topmargin="5" onload="doInit()">
	<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;合同选择</span>
   </td>
   <td class="Big">
   <input type="button" value="添加" class="BigButton" onclick="openAdd();">
   </td>
 </tr>
</table>
		<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
</div>

<div id="msrg">
</div>
	</body>
</html>