<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String k=request.getParameter("k");
	if("".equals(k)){
		k="-1";
	}
	String orderNum=request.getParameter("orderNum");

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
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
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
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
<script  type="text/javascript"  src="obejctJs1.js"></script>
<script> 
var pageMgr = null;
var parentWindowObj;
var k="<%=k%>";
var proId="<%=proId%>";
var arrMainPro="";
var orderNum="<%=orderNum%>";
   orderNum=parseFloat(orderNum);
function doInit(){
		
		parentWindowObj=window.dialogArguments;
		arrMainPro=parentWindowObj["arrMainPro"];
     	var url = "<%=contextPath %>/SpringR/warehouse/getWhProdId/"+new Date().getTime();
	  	var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	      	 {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender1},
	         {type:"hidden", name:"whId", text:"whId"},
	         {type:"data", name:"whName",  width: '10%', text:"仓库名称",render:recordCenterFunc},    
	         {type:"data", name:"type", width: '10%', text:"仓库类型",render:recordCenterFunc},   
	         {type:"selfdef", text:"入库数量", width: '15%',render:opts},
	             {type:"selfdef", text:"入库批次号", width: '10%',render:optBatch},
	             {type:"selfdef", text:"产品有效期", width: '10%',render:optDate}
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
	      WarningMsrg('无信息展示', 'msrg');
	    }
	    initTime();
}
 function initTime(){
	var arrDb=dbIds.split(",");
	for(var i=0;i<arrDb.length;i++){ 
  	var beginTimePara = {
      inputId:arrDb[i]+"date",
     property:{isHaveTime:false},
      bindToBtn:arrDb[i]+"dateImage"
  };
  new Calendar(beginTimePara);
  }
}

function opts(cellData, recordIndex, columIndex){
	var whId = this.getCellData(recordIndex, "whId");
	if(orderNum==null||orderNum==""){
	return "<center><input id='"+whId+"' type=\"text\" style='height:16px;width:50px;' onblur=\"checkFloatNums('"+whId+"')\" value='' /></center>";
	}else{
	return "<center><input id='"+whId+"' type=\"text\" style='height:16px;width:50px;' onblur=\"checkFloatNums('"+whId+"')\" value='' /></center>";
	}
}

function optBatch(cellData, recordIndex, columIndex){
	var whId = this.getCellData(recordIndex, "whId");
	var batchs=proId+whId;
	return "<center><input type='text' id='"+batchs+"Batch' class='BigInput'  onBlur=\"selectBatch('"+whId+"',this.id)\"  ></center>";
}
var  dbIds="";//全局dbId
function optDate(cellData, recordIndex, columIndex){
	var whId = this.getCellData(recordIndex, "whId");
	if(dbIds!=""){
		dbIds+=",";
	}
	dbIds+=(proId+whId);
	var dbDate=proId+whId;
	return "<center> <input type='text' id='"+dbDate+"date' name='date' size='12' maxlength='12' class='BigInput'  readOnly><img src='<%=imgPath%>/calendar.gif' id='"+dbDate+"dateImage' align='absMiddle' border='0' style='cursor:pointer' ></center>";
}
	function selectBatch(whId,batchId){
	var batchs=jQuery('#'+batchId).val();
	 var url="<%=contextPath %>/SpringR/warehouse/getBatch?batch="+batchs+"&whId="+whId;
	jQuery.ajax({
		type:'POST',
		url:url,
		success:function(jsonData){
		var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.batch!=undefined){
				alert("该批次此仓库已经存在！");
				jQuery('#'+batchId).val();
				return false;
			}
		}
	});
	}

  function checkFloatNums(id){
	   var megx = /^\d+(\.\d+)?$/;
	   var number = jQuery('#'+id).val();
	   if(!megx.test(number)){
		   alert('请输入正确的数');
		   jQuery('#'+id).val('');
		   return;
	   }
   }
function checkBoxRender1(cellData, recordIndex, columIndex) {
	var whId = this.getCellData(recordIndex, "whId");
	var whName = this.getCellData(recordIndex, "whName");
	var para = whId+","+proId+","+whName;
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
	if(	parentWindowObj.jQuery('#dbIds').val()==""||parentWindowObj.jQuery('#dbIds').val()==null){
							parentWindowObj.jQuery('#dbIds').val(dbIds);
	}else{
							parentWindowObj.jQuery('#dbIds').val(parentWindowObj.jQuery('#dbIds').val()+","+dbIds);
		
	}
	
  	var array = parentWindowObj["nameArray"];
	window.close();
}
var num="";
function checkMags(cntrlId) {
var selectTotalProductNum=0;
			var len=0;
	var checkArray = jQuery("input[name=\"deleteFlag\"]");
	for (var i = 0; i < checkArray.length; i++) {
		if (checkArray[i].name == cntrlId && checkArray[i].checked) {
			var valueArray = checkArray[i].value.split(",");
			var nums= jQuery('#'+valueArray[0]).val();
			var batch= jQuery('#'+proId+valueArray[0]+"Batch").val();
			var inDate= jQuery('#'+proId+valueArray[0]+"date").val();
			if (num != "") {
				num += ",";
			}
			num +=nums;
			if(nums==""||nums==null){
				alert("请确保您选择的仓库输入了需要出库的数量！！");				
				return false;
			} 
						
							var  cfgs={
									wh_id:valueArray[0],
									number:nums,
									batch:batch,
									invalid_time:inDate
									}
							selectTotalProductNum+=parseFloat(nums);
						    var productOut=new ProductOut(cfgs);
							arrMainPro[k].productOut.push(productOut);
							var add="add"+proId;
							var radd=proId+len;
							var tds="<input type='hidden' id='"+proId+valueArray[0]+"batch' value='"+batch+"'><input type='hidden' id='"+proId+valueArray[0]+"inDates' value='"+inDate+"'><a id='"+radd+"' title='[批次号："+batch+"]--[有效时间："+inDate+"]'  style='cursor:pointer'>"+valueArray[2]+"["+nums+"]<img  align='absmiddle' style='cursor:pointer'  title='删除' src='/yh/core/styles/style1/img/remove.png' onclick=\"deletes('"+proId+"',"+len+")\"></a>";
							parentWindowObj.jQuery('#'+add).append(tds);
							if(parentWindowObj.jQuery('#'+proId).val()==null||parentWindowObj.jQuery('#'+proId).val()==""){
							parentWindowObj.jQuery('#'+proId).val(selectTotalProductNum);
							}else{
							parentWindowObj.jQuery('#'+proId).val(parseFloat(parentWindowObj.jQuery('#'+proId).val())+parseFloat(selectTotalProductNum));
							}
							
							len++;
				
			}
			
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