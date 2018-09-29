<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%
	String shortName=request.getParameter("shortName");
	if(shortName==null||shortName.equals("")){
			shortName="-1";
			}
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
		<script> 
var pageMgr = null;
var parentWindowObj;
function doInit(){
	parentWindowObj = window.dialogArguments;
	
 doShort();
}
var shortName="<%=shortName%>";
function doShort(){
	var url = "<%=contextPath %>/SpringR/product/productList?shortName="+shortName;
	 var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	     	 {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender1},
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"proCode",  width: '10%', text:"产品编号",render:recordCenterFunc},    
	         {type:"data", name:"proName",  width: '10%', text:"名称",render:recordCenterFunc},
	             {type:"data", name:"shortName",  width: '10%', text:"产品简称",render:recordCenterFunc},
	         {type:"data", name:"proType",  width: '10%', text:"产品规格",render:recordCenterFunc},    
	         {type:"data", name:"proPrice",  width: '10%', text:"标准单价",render:recordCenterFunc},
	         {type:"data", name:"uName",  width: '10%', text:"单位",render:recordCenterFunc},    
	         {type:"data", name:"ptName",  width: '10%', text:"类别",render:recordCenterFunc},
	         {type:"data", name:"psName",  width: '10%', text:"所属大类",render:recordCenterFunc},
	         {type:"data", name:"pRemark",  width: '10%', text:"备注",render:recordCenterFunc}]
	    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无符合条件的产品信息', 'msrg');
    }
}
function checkBoxRender1(cellData, recordIndex, columIndex) {
	var id = this.getCellData(recordIndex, "id");
	var proCode = this.getCellData(recordIndex, "proCode");
	var proName = this.getCellData(recordIndex, "proName");
	var proType = this.getCellData(recordIndex, "proType");
	var uName = this.getCellData(recordIndex, "uName");
	var para = id+","+proCode+","+proName+","+proType+","+uName;
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
	parentWindowObj.jQuery('#'+array[0]).val(ids);
	window.close();
}
var ids = "";
function checkMags(cntrlId) {
	var checkArray = jQuery("input[name=\"deleteFlag\"]");
	for (var i = 0; i < checkArray.length; i++) {
		if (checkArray[i].name == cntrlId && checkArray[i].checked) {
			var valueArray = checkArray[i].value.split(",");
			var id = valueArray[0];
			var tds = "<tr><td align='center' nowrap>"+valueArray[1]+"</td>";//产品编号
			tds += "<td align='center' nowrap>"+valueArray[2]+"</td>";//产品名称
			tds += "<td align='center' nowrap>"+valueArray[3]+"</td>";//规格型号
			tds += "<td align='center' nowrap>"+valueArray[4]+"</td>";//计量单位
			tds += "<td align='center' nowrap><input type='text' id='"+id+"Price'  class='BigInput' size='10' maxlength='10' onblur=\"checkFloat('"+id+"')\"></td>";//单价
			tds += "<td align='center' nowrap><input type='text' id='"+id+"Number' class='BigInput' size='10' maxlength='10' onblur=\"checkInt('"+id+"')\"></td>";//数量
			tds += "<td align='center' nowrap><input type='text' id='"+id+"Total' class='BigInput' size='10' maxlength='10' onclick=\"accMul('"+id+"')\" title='点击自动计算' readOnly></td>";//总额
			tds += "<td align='center' nowrap><input type='button' value='删除' class='BigButton' onclick=\"deletes(this,'"+id+"')\"></td></tr>";
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