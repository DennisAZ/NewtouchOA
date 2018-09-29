<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="<%=cssPath%>/style.css">
		<link rel="stylesheet" href="<%=cssPath%>/page.css">
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
var pageMgr = null;
var parentWindowObj;
function doInit(){
	parentWindowObj = window.dialogArguments;
	var url = "<%=contextPath %>/SpringR/produce/getWareHouse";
	 var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	     	 {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender1},
	         {type:"hidden", name:"wh_id", text:"wh_id"},
	         {type:"data", name:"wh_name",  width: '20%', text:"仓库名称",render:recordCenterFunc},    
	         {type:"data", name:"type_name",  width: '20%', text:"仓库类型",render:recordCenterFunc},
	          {type:"data", name:"address",  width: '55%', text:"仓库地址",render:recordCenterFunc}]
	    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无仓库信息', 'msrg');
    }
}
function checkBoxRender1(cellData, recordIndex, columIndex) {
	var wh_id = this.getCellData(recordIndex, "wh_id");
	var wh_name = this.getCellData(recordIndex, "wh_name");
	var para = wh_id+","+wh_name;
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
  	var str = parentWindowObj.jQuery('#'+array[0]).val();
  	if(str != ""){
  		parentWindowObj.jQuery('#'+array[0]).val(str+','+ids);
  	}else{
		parentWindowObj.jQuery('#'+array[0]).val(ids);
	}
	window.close();
}
var ids = "";
function checkMags(cntrlId) {
	var checkArray = jQuery("input[name=\"deleteFlag\"]");
	for (var i = 0; i < checkArray.length; i++) {
		if (checkArray[i].name == cntrlId && checkArray[i].checked) {
			var valueArray = checkArray[i].value.split(",");
			var id = valueArray[0];
			//判断当前仓库是否已经添加过
			var flag = 0;
			var array = parentWindowObj["nameArray"];
			var str = parentWindowObj.jQuery('#'+array[0]).val();
			if(str != ""){
				var arr = str.split(",");
				for(var x=0;x<arr.length;x++){
					if(id == arr[x]){
						alert("'"+valueArray[1]+"'已经被选择！");
						flag = 1;
					}
				}				
			}
			if(flag == 1){
				continue;
			}
			var tds = "<tr><td align='center' nowrap><span id='"+id+"Name'>"+valueArray[1]+"</span></td>";//仓库名称
			tds += "<td align='center' nowrap><input type='text' id='"+id+"Price'  class='BigInput' size='10' maxlength='10' onblur=\"checkFloat('"+id+"','Price')\" value='0'></td>";//单价
			tds += "<td align='center' nowrap><input type='text' id='"+id+"Number' class='BigInput' size='10' maxlength='10' onblur=\"checkFloat('"+id+"','Number')\" value='0'></td>";//数量
			tds += "<td align='center' nowrap><input type='text' id='"+id+"Batch' class='BigInput' size='20' maxlength='20' onblur=\"checkBatch('"+id+"')\" title='建议使用当前日期+产品编号：如20130822TH001'></td>";//批次编号
			tds += "<td align='center' nowrap><input type='text' id='"+id+"Invalid' size='10' maxlength='20' class='BigInput' value=''><img src='<%=imgPath%>/calendar.gif' id='"+id+"InvalidImg' align='absMiddle' border='0' style='cursor:pointer' > </td>";
			tds += "<td align='center' nowrap><img  align='absmiddle' style='cursor:pointer' title='删除' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes(this,'"+id+"')\"/></td></tr>";
			parentWindowObj.jQuery('#pro_table').append(tds);
			if (ids != "") {
				ids += ",";
			}
			ids += id;
		}
	}
}

</script>
	</head>
	<body topmargin="5" onload="doInit()">
		<br>
<div id="listContainer" style="display:none;width:100%;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls"  onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
         <input type="button" value="确定" class="BigButton" onclick="return submitAll();">
      </td>
 </tr>
</table>
</div>

<div id="msrg">
</div>
	</body>
</html>