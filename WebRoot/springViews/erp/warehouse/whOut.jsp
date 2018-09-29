<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String proId=request.getParameter("proId");
	if("".equals(proId)){
		proId="0";
	}
	String orderNum=request.getParameter("orderNum");
	if("".equals(orderNum)){
		orderNum="0";
	}
	String dbLogId=request.getParameter("dbId");
	if("".equals(dbLogId)||dbLogId==null){
		dbLogId="0";
	}
	String id=request.getParameter("id");
	if("".equals(id)||id==null){
		id="0";
	}
	String price=request.getParameter("price");
	if("".equals(price)||price==null){
		price="0";
	}
 %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>仓库管理</title>
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
var parentWindowObj;
var proId="<%=proId%>";
var orderNum="<%=orderNum%>";
var dbLogId="<%=dbLogId%>";
var id="<%=id%>";
var price="<%=price%>";
function doInit(){
		parentWindowObj = window.dialogArguments;
     var url = "<%=contextPath %>/SpringR/warehouse/getWhByProdId?proId="+proId;
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	      	 {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender1},
	         {type:"hidden", name:"dbId", text:"dbId"},
	         {type:"hidden", name:"whId", text:"whId"},
	         {type:"hidden", name:"proId", text:"proId"},
	         {type:"data", name:"whName",  width: '20%', text:"仓库名称",render:recordCenterFunc},    
	         {type:"data", name:"type", width: '20%', text:"仓库类型",render:recordCenterFunc},   
	         {type:"hidden", name:"proCode",  width: '1%',text:"产品编号",render:recordCenterFunc},
	         {type:"data", name:"proName",  width: '10%', text:"产品名称",render:recordCenterFunc},
	         {type:"data", name:"price",  width: '10%', text:"价格区间",render:recordCenterFunc},
	         {type:"data", name:"num",  width: '10%', text:"库存数量",render:recordCenterFunc},
	          {type:"selfdef", text:"操作", width: '15%',render:opts}]
	    };
	    pageMgr = new YHJsPage(cfgs);
	    pageMgr.show();
	    var total = pageMgr.pageInfo.totalRecord;
	    if(total){
	      showCntrl('listContainer');
	      var mrs = " 共 " + total + " 条记录 ！";
	      showCntrl('delOpt');
	    }else{
	      WarningMsrg('无信息展示', 'msrg');
	    }
}

function opts(cellData, recordIndex, columIndex){
	var dbId = this.getCellData(recordIndex, "dbId");
	return "<center><input id='"+dbId+"' type=\"text\" style='height:16px;width:50px;' onblur=\"compare(this.id)\" /></center>";
}
function compare(id){
    var number=	jQuery('#'+id).val();
    orderNum=parseFloat(orderNum);
    number=parseFloat(number);
	if(number>orderNum){
		alert("您输入的数量大于需要发货的数量！");
		jQuery('#'+id).val("");
		return false;
	}else{
		orderNum=orderNum-number;
	}
}
function checkBoxRender1(cellData, recordIndex, columIndex) {
	var dbId = this.getCellData(recordIndex, "dbId");
	var whId = this.getCellData(recordIndex, "whId");
	var proId = this.getCellData(recordIndex, "proId");
	var whName = this.getCellData(recordIndex, "whName");
	var para = dbId+","+whId+","+proId+","+whName;
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
	
  	var array = parentWindowObj["nameArray"];
	window.close();
}
var dbId = "";
var whId = "";
var whName = "";
var num="";
var whNames="";
var newProId="";
var newProIds="";
var newPrice="";
function checkMags(cntrlId) {
			
	var checkArray = jQuery("input[name=\"deleteFlag\"]");
	for (var i = 0; i < checkArray.length; i++) {
		if (checkArray[i].name == cntrlId && checkArray[i].checked) {
			var valueArray = checkArray[i].value.split(",");
			var nums= jQuery('#'+valueArray[0]).val();
			if (num != "") {
				num += ",";
			}
			num +=nums;
			if(nums==""||nums==null){
				alert("请确保您选择的仓库输入了需要出库的数量！！");				
				return false;
			} 
		whNames=valueArray[3]+"["+nums+"]";
		
			if (whName != "") {
				whName += ",";
			}
			whName += whNames;
			if (dbId != "") {
				dbId += ",";
			}
			dbId += valueArray[0];
			if (whId != "") {
				whId += ",";
			}
			whId += valueArray[1];
			if (newProId != "") {
				newProId += ",";
			}
			newProId+=valueArray[2]
			
			if (newProIds != "") {
				newProIds += ",";
			}
			newProIds+=id;
			if (newPrice != "") {
				newPrice += ",";
			}
			newPrice+=price;
			
			}}
				
			
			if("0"==dbLogId){
			jQuery('#de'+proId).remove();
			 var tds = "<td align='center' nowrap id='de"+proId+"' colspan='3'><input type='hidden' id='"+proId+"dbId' value='"+dbId+"' ><input type='hidden' id='"+proId+"num' value='"+num+"' ><input type='hidden' id='"+proId+"whId' value='"+whId+"' ><input type='hidden' id='"+proId+"whName' value='"+whName+"' ><textarea>"+whName+"</textarea></td>";//仓库信息
			parentWindowObj.jQuery('#db'+proId).append(tds);
			}else{
			jQuery('#de'+dbLogId).remove();
			 var tds = "<td align='center' nowrap id='de"+dbLogId+"' colspan='3'><input type='hidden' id='"+dbLogId+"dbId' value='"+dbId+"' ><input type='hidden' id='"+dbLogId+"newProId' value='"+newProId+"' ><input type='hidden' id='"+dbLogId+"newProIds' value='"+newProIds+"' >"
			 tds+=" <input type='hidden' id='"+dbLogId+"newPrice' value='"+newPrice+"' ><input type='hidden' id='"+dbLogId+"num' value='"+num+"' ><input type='hidden' id='"+dbLogId+"whId' value='"+whId+"' ><input type='hidden' id='"+dbLogId+"whName' value='"+whName+"' ><textarea>"+whName+"</textarea></td>";//仓库信息
				parentWindowObj.jQuery('#db'+dbLogId).append(tds);
			}	
}


</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;选择仓库</span>
   </td>
   <td class="Big">
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