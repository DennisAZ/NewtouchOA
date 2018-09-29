<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%
String reqId=request.getParameter("reqId")==null?"":request.getParameter("reqId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
		<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
		<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
		<script> 
var pageMgr = null;
var reqId="<%=reqId%>";
var parentWindowObj;
function doInit(){
	parentWindowObj = window.dialogArguments;
	if(reqId!=0){
	var url = "<%=contextPath %>/SpringR/purchase/suppList?reqId="+reqId;
	 var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"supId", text:"supId"},
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"supCode",  width: '5%', text:"供货商编号",render:recordCenterFunc},    
	         {type:"hidden", name:"supContactMan",  width: '10%', text:"联系人",render:recordCenterFunc},    
	         {type:"data", name:"supName",  width: '5%', text:"名称",render:recordCenterFunc},
	         {type:"hidden", name:"supPhone",  width: '5%', text:"电话",render:recordCenterFunc},    
	         {type:"data", name:"supMob",  width: '5%', text:"手机",render:recordCenterFunc},
	          {type:"hidden", name:"supFex",  width: '5%', text:"传真",render:recordCenterFunc},
	          {type:"data", name:"supEmail",  width: '5%', text:"邮箱",render:recordCenterFunc},
	          {type:"data", name:"supNet",  width: '10%', text:"网址",render:recordCenterFunc},
	          {type:"hidden", name:"supQq",  text:"QQ",render:recordCenterFunc},
	          {type:"data", name:"supAdd",  width: '10%', text:"地址",render:recordCenterFunc},
	          {type:"data", name:"supZipCode",  width: '5%', text:"邮编",render:recordCenterFunc},
	          {type:"data", name:"supProd",  width: '10%', text:"产品",render:recordCenterFunc},
	          {type:"hidden", name:"supBank",   text:"开户行",render:recordCenterFunc},
	          {type:"hidden", name:"supBankName",   text:"开户名称",render:recordCenterFunc},
	          {type:"hidden", name:"supBankCode",  text:"开户账号",render:recordCenterFunc},
	          {type:"hidden", name:"supRemark",  text:"备注",render:recordCenterFunc},
	       	   {type:"selfdef", text:"操作", width: '10%',render:opts}]
	    };
    
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无符合条件的供货商信息', 'msrg');
    }
 }else{
 	var url = "<%=contextPath %>/SpringR/purchase/suppList?reqId="+reqId;
	 var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"supCode",  width: '5%', text:"供货商编号",render:recordCenterFunc},    
	         {type:"hidden", name:"supContactMan",  width: '10%', text:"联系人",render:recordCenterFunc},    
	         {type:"data", name:"supName",  width: '5%', text:"名称",render:recordCenterFunc},
	         {type:"hidden", name:"supPhone",  width: '5%', text:"电话",render:recordCenterFunc},    
	         {type:"data", name:"supMob",  width: '5%', text:"手机",render:recordCenterFunc},
	          {type:"hidden", name:"supFex",  width: '5%', text:"传真",render:recordCenterFunc},
	          {type:"data", name:"supEmail",  width: '5%', text:"邮箱",render:recordCenterFunc},
	          {type:"data", name:"supNet",  width: '10%', text:"网址",render:recordCenterFunc},
	          {type:"hidden", name:"supQq",  text:"QQ",render:recordCenterFunc},
	          {type:"data", name:"supAdd",  width: '10%', text:"地址",render:recordCenterFunc},
	          {type:"data", name:"supZipCode",  width: '5%', text:"邮编",render:recordCenterFunc},
	          {type:"data", name:"supProd",  width: '10%', text:"产品",render:recordCenterFunc},
	          {type:"hidden", name:"supBank",   text:"开户行",render:recordCenterFunc},
	          {type:"hidden", name:"supBankName",   text:"开户名称",render:recordCenterFunc},
	          {type:"hidden", name:"supBankCode",  text:"开户账号",render:recordCenterFunc},
	          {type:"hidden", name:"supRemark",  text:"备注",render:recordCenterFunc},
	       	   {type:"selfdef", text:"操作", width: '10%',render:opts}]
	    };
    
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无符合条件的供货商信息', 'msrg');
    }
 }
 
}

function recordCenterFunc(cellData){
  return "<center>" + cellData + "</center>";
}

function opts(cellData, recordIndex, columIndex){
	var id = this.getCellData(recordIndex, "id");
	var supRemark = this.getCellData(recordIndex, "supRemark");
	var supContactMan = this.getCellData(recordIndex, "supContactMan");
	var supName = this.getCellData(recordIndex, "supName");
	var supPhone = this.getCellData(recordIndex, "supPhone");
	var supMob = this.getCellData(recordIndex, "supMob");
	var supFex = this.getCellData(recordIndex, "supFex");
	var supEmail = this.getCellData(recordIndex, "supEmail");
	var supNet = this.getCellData(recordIndex, "supNet");
	var supQq = this.getCellData(recordIndex, "supQq");
	var supAdd = this.getCellData(recordIndex, "supAdd");
	var supZipCode = this.getCellData(recordIndex, "supZipCode");
	var supProd = this.getCellData(recordIndex, "supProd");
	var supBank = this.getCellData(recordIndex, "supBank");
	var supBankName = this.getCellData(recordIndex, "supBankName");
	var supBankCode = this.getCellData(recordIndex, "supBankCode");
	var supCode = this.getCellData(recordIndex, "supCode");
	var parm=id+","+supRemark+","+supContactMan+","+supName+","+supPhone+","+supMob+","+supFex+","+supEmail	+","+supNet+","+supQq+","+supAdd+","+supZipCode+","+supProd+","+supBank+","+supBankName+","+supBankCode+","+supCode;
	
	return "<center><a href=\"javascript:void(0)\" onclick=\"add('" +parm+"');\" >添加</a></center>";
}
function add(parm){
	window.parent.returnValue=parm;
 	window.parent.close();
}

</script>
	</head>
	<body topmargin="5" onload="doInit()">
		<table border="0" width="100%" cellspacing="0" cellpadding="3"
			class="small">
			<tr>
				<td class="Big">
					<img src="<%=imgPath%>/infofind.gif" align="absMiddle">
					<span class="big3">&nbsp;
						供货商选择</span>
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