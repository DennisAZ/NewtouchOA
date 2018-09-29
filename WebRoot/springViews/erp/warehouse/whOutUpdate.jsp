<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	
	String orderNum=request.getParameter("orderNum");
	if("".equals(orderNum)){
		orderNum="-1";
	}

	String proId=request.getParameter("proId");
	if("".equals(proId)){
		proId="0";
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
<script  type="text/javascript"  src="obejctJs.js"></script>
<script  type="text/javascript"  src="map.js"></script>
<script> 
var proId="<%=proId%>";
var orderNum="<%=orderNum%>";
orderNum=parseFloat(orderNum);

var parentWindowObj;

var pageMgr = null;
function doInit(){	
	parentWindowObj=window.dialogArguments;
	
     var url = "<%=contextPath %>/SpringR/warehouse/getWhByProdId?proId="+proId;
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	      	 {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender1},
	         {type:"hidden", name:"dbId", text:"dbId"},
	         {type:"hidden", name:"whId", text:"whId"},
	         {type:"hidden", name:"proId", text:"proId"},
	         {type:"data", name:"whName",  width: '10%', text:"仓库名称",render:recordCenterFunc},    
	         {type:"data", name:"pro_code", width: '10%', text:"产品编号",render:recordCenterFunc},   
	         {type:"data", name:"pro_name",  width: '20%', text:"产品名称",render:recordCenterFunc},
	         {type:"data", name:"batch",  width: '15%', text:"批次编号",render:recordCenterFunc},
	         {type:"data", name:"num",  width: '10%', text:"库存数量",render:recordCenterFunc},
	         {type:"data", name:"invalid_time",  width: '15%', text:"失效时间",render:recordCenterFunc},
	         {type:"selfdef", text:"出库数量", width: '15%',render:opts}]
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
	return "<center><input id='"+dbId+"' vaule='' type=\"text\" style='height:16px;width:50px;' onblur=\"compare(this.id)\" /></center>";
}
var map = new Map();
function compare(id){
    var val =jQuery('#'+id).val();
    if(val != ''){
      if(checkDouble(id)){
   		var number = parseFloat(val);
		if(map.containsKey(id))
		{
			map.removeByKey(id);
			map.put(id,number);
		}
		else{
			map.put(id,number);
		}
		document.getElementById(id+'_2').checked = true;
      }
    }
}

   function checkDouble(id){
	   var megx = /^\d+(\.\d+)?$/;
	   var number = jQuery('#'+id).val();
	   if(!megx.test(number)){
		   alert('请输入正确的数');
		   jQuery('#'+id).val('');
		   return false;
	   }
	   return true;
   }
   
function checkBoxRender1(cellData, recordIndex, columIndex) {
	var dbId = this.getCellData(recordIndex, "dbId");
	var whId = this.getCellData(recordIndex, "whId");
	var proId = this.getCellData(recordIndex, "proId");
	var whName = this.getCellData(recordIndex, "whName");
	var proName = this.getCellData(recordIndex, "proName");
	var proCode = this.getCellData(recordIndex, "proCode");
	var batch = this.getCellData(recordIndex, "batch");
	var para = dbId+","+whId+","+proId+","+whName+","+batch;
	return "<center><input type=\"checkbox\" id=\""+dbId+"_2\" name=\"deleteFlag\" value=\"" + para +"\" onclick=\"checkSelf()\" ></center>";
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
	if(checkMags("deleteFlag"))
	{
 		window.close();
	}
}

function checkMags(cntrlId) {
	var checkArray = jQuery("input[name=\"deleteFlag\"]");
	var num = 0;
	for(var i=0;i<map.size();i++){
		num = num + map.element(i).value;
	}
	if(num > orderNum){
		alert("您输入的数量大于需要的数量！");
		return false;
	}
	for (var i = 0; i < checkArray.length; i++) {
		if (checkArray[i].name == cntrlId && checkArray[i].checked) {
			var valueArray = checkArray[i].value.split(",");
			var number= jQuery('#'+valueArray[0]).val();
			if(number==""||number==null){
				alert("被选择项需求数量没有填写！");				
				return false;
			} 
			var tds="";
			//判断当前库存是不是已经添加，如果是，则累加
			var dbNumMap = parentWindowObj["dbNumMap"];
			var key = valueArray[0]+","+valueArray[1]+","+valueArray[2]+","+valueArray[4];
			if(dbNumMap.containsKey(key)){
				var n = dbNumMap.get(key);
				n = parseFloat(n)+parseFloat(number);
				dbNumMap.removeByKey(key);
				dbNumMap.put(key,n);
				parentWindowObj["dbNumMap"] = dbNumMap;//重新赋值
				var obj = parentWindowObj.document.getElementById(key);
				obj.parentNode.removeChild(obj);
				//parentWindowObj.jQuery('#'+key).remove();
				tds="<a id='"+key+"'>"+valueArray[3]+"("+valueArray[4]+")["+n+"]<img  style='cursor:pointer' align='absmiddle' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes('"+key+"','"+valueArray[2]+"','"+n+"')\" title='删除'></a>";
				parentWindowObj.jQuery('#'+valueArray[2]+'Number').val(n);//累加新添加的数量
			}else{
				dbNumMap.put(key,number);//没有则新增
				tds="<a id='"+key+"'>"+valueArray[3]+"("+valueArray[4]+")["+number+"]<img  style='cursor:pointer' align='absmiddle' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes('"+key+"','"+valueArray[2]+"','"+number+"')\" title='删除'></a>";
				parentWindowObj.jQuery('#'+valueArray[2]+'Number').val(number);//累加新添加的数量
			}
			parentWindowObj.jQuery('#add_'+valueArray[2]).append(tds);
			}
		}
		return true;
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
<div id="listContainer" style="display:none;width:100%;">
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