<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%
	String  proId=request.getParameter("proId");
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
var parentWindowObj;
var proId="<%=proId%>";
var flag="";
function doInit(){
	parentWindowObj = window.dialogArguments;
	var url="<%=contextPath%>/SpringR/purchase/supplierList?proId="+proId;
	 var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	    	  {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender1},
	         {type:"hidden", name:"sup_id", text:"id"},
	         {type:"data", name:"supName",  width: '25%', text:"供货商名称",render:recordCenterFunc},    
	         {type:"data", name:"shortName",  width: '10%', text:"产品简称",render:recordCenterFunc},    
	         {type:"data", name:"supPhone",  width: '15%', text:"电话",render:recordCenterFunc},    
	         {type:"data", name:"supMob",  width: '15%', text:"手机",render:recordCenterFunc},
	         {type:"data", name:"price",  width: '10%', text:"单价",render:recordCenterFunc},
	         {type:"data", name:"lastDate",  width: '10%', text:"最后更新时间",render:recordCenterFunc},
	         {type:"data", name:"hasTax",  width: '10%', text:"是否含税率",render:recordCenterFunc}
	         ]
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

	
 
 function checkBoxRender1(cellData, recordIndex, columIndex) {
	var id = this.getCellData(recordIndex, "sup_id");
	var supName = this.getCellData(recordIndex, "supName");
	var price = this.getCellData(recordIndex, "price");
	var para = id+","+supName+","+price;
	return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + para +"\" onclick=\"checkSelf()\" ></center>";
}
 function recordCenterFunc(cellData){
  return "<center>" + cellData + "</center>";
}
function checkAll(field) {
	var deleteFlags = document.getElementsByName("deleteFlag");
	for (var i = 0; i < deleteFlags.length; i++) {
		deleteFlags[i].checked = field.checked;
	}
}
function checkSelf() {
	var allCheck = jQuery("#checkAlls");
	if (allCheck.checked) {
		allCheck.checked = false;
	}
}
function submitAll() {
		checkMags("deleteFlag");
		window.parent.returnValue=arr;
 		window.close();
}

var arr=new Array();
function checkMags(cntrlId) {
	var checkArray = jQuery("input[name=\"deleteFlag\"]");
	for (var i = 0; i < checkArray.length; i++) {
		if (checkArray[i].name == cntrlId && checkArray[i].checked) {
			var valueArray = checkArray[i].value.split(",");
			arr.push(proId+","+valueArray[0]);
			var tds="<a id='"+valueArray[0]+"'>"+valueArray[1]+"["+valueArray[2]+"]<img style='cursor:pointer' align='absmiddle' title='删除' src='/yh/core/styles/style1/img/remove.png' onclick=\"deleteSup('"+proId+"','"+valueArray[0]+"')\"></a>";
			parentWindowObj.jQuery('#'+proId).append(tds);
		}
	}
}
</script>
	</head>
	<body topmargin="5" onload="doInit()">
		<input type="hidden" id="productId" name="productId" />
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
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         <input type="button" value="确定" class="BigButton" onclick="return submitAll();">
      </td>
 </tr>
</table>
</div>

<div id="msrg">
</div>

	</body>
</html>