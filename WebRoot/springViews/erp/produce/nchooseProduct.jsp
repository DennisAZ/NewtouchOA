<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%
	String param=request.getParameter("param");
	if(param==null||param.equals("")){
		param="-4";
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
var param="<%=param%>";
function doInit(){
	parentWindowObj = window.dialogArguments;
	var url = "<%=contextPath %>/SpringR/product/selectProduct?param="+param;
	 var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	       colums: [
	     	 {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender1},
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"proCode",  width: '10%', text:"产品编号",render:recordCenterFunc},    
	         {type:"data", name:"proName",  width: '10%', text:"名称",render:recordCenterFunc},
	          {type:"hidden", name:"shortName",  width: '10%', text:"产品简称",render:recordCenterFunc},
	         {type:"hidden", name:"proType",  width: '1%', text:"产品规格",render:recordCenterFunc},    
	         {type:"hidden", name:"proPrice",  width: '1%', text:"标准单价",render:recordCenterFunc},
	         {type:"hidden", name:"uName",  width: '10%', text:"单位",render:recordCenterFunc},    
	         {type:"data", name:"ptName",  width: '10%', text:"所属大类",render:recordCenterFunc},
	         {type:"data", name:"psName",  width: '10%', text:"类别",render:recordCenterFunc},
	         {type:"hidden", name:"pRemark",  width: '1%', text:"备注",render:recordCenterFunc},
	         {type:"hidden", name:"craftsId",  text:"工艺id"},
	         {type:"data", name:"version",  width: '10%', text:"工艺版本号",render:craftsFunc},
	         {type:"hidden", name:"gy_is_using",  width: '10%', text:"是否当前使用"},
	         {type:"hidden", name:"drawingId",  text:"图纸id"},
	         {type:"data", name:"drawing",  width: '10%', text:"图纸版本号",render:drawingFunc},
	         {type:"hidden", name:"tz_is_using",  width: '10%', text:"是否当前使用"}
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
      WarningMsrg('无符合条件的产品信息', 'msrg');
    }
}
	
function checkBoxRender1(cellData, recordIndex, columIndex) {
	var id = this.getCellData(recordIndex, "id");
	var cpId = this.getCellData(recordIndex, "cpId");
	var proCode = this.getCellData(recordIndex, "proCode");
	var proName = this.getCellData(recordIndex, "proName");
	var proType = this.getCellData(recordIndex, "proType");
	var uName = this.getCellData(recordIndex, "uName");
	var proPrice = this.getCellData(recordIndex, "proPrice");
	var craftsId = this.getCellData(recordIndex, "craftsId");
	var drawingId = this.getCellData(recordIndex, "drawingId");
	var version = this.getCellData(recordIndex, "version");
	var drawing = this.getCellData(recordIndex, "drawing");
	var para = id+","+proCode+","+proName+","+proType+","+uName+","+cpId+","+proPrice+","+craftsId+","+version+","+drawingId+","+drawing;
	return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + para +"\" onclick=\"checkSelf()\" ></center>";
}
function recordCenterFunc(cellData){
  return "<center>" + cellData + "</center>";
}
function craftsFunc(cellData, recordIndex, columIndex){
	var id = this.getCellData(recordIndex, "id");
	var version = this.getCellData(recordIndex, "version");
  	return "<center><a href='#' onclick=\"editVersion('1','"+id+"')\" title='查看版本记录'>" + version + "</a></center>";
}
function drawingFunc(cellData, recordIndex, columIndex){
	var id = this.getCellData(recordIndex, "id");
	var drawing = this.getCellData(recordIndex, "drawing");
  return "<center><a href='#' onclick=\"editVersion('2','"+id+"')\" title='查看版本记录'>" + drawing + "</a></center>";
}

function editVersion(flag,id){
	if(flag == '1')
	{
		//工艺编辑
		var url="craftsManage1.jsp?pro_id="+id;
	 	openDialogResize(url,800, 500);
	}
	else{
		//图纸编辑
		var url="drawingManage1.jsp?pro_id="+id;
	 	openDialogResize(url,800, 500);
	}
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
			var flag = 0;
			//判断当前产品是否已经添加过
			var array = parentWindowObj["nameArray"];
			var str = parentWindowObj.jQuery('#'+array[0]).val();
			if(str != ""){
				var arr = str.split(",");
				for(var x=0;x<arr.length;x++){
					if(id == arr[x]){
						alert("'"+valueArray[2]+"'已经被选择！");
						flag = 1;
					}
				}				
			}
			if(flag == 1){
				continue;
			}
			var tds = "<tr><td align='center' nowrap>"+valueArray[1]+"</td>";//产品编号
			tds += "<td align='center' nowrap>"+valueArray[2]+"</td>";//产品名称
			tds += "<td align='center' nowrap>"+valueArray[3]+"</td>";//规格型号
			tds += "<td align='center' nowrap>"+valueArray[4]+"</td>";//计量单位
			tds += "<td align='center' nowrap><input type='text' id='"+id+"Number' class='BigInput' size='10' maxlength='10' onblur=\"checkFloat('"+id+"')\"></td>";//数量
			tds += "<td align='center' nowrap><input type='text' id='"+id+"beginTime' name='"+id+"beginTime' size='19' maxlength='19' class='BigInput' value=''><img src='<%=imgPath%>/calendar.gif' id='"+id+"beginTimeImg' align='absMiddle' border='0' style='cursor:pointer' > </td>";
			tds += "<td align='center' nowrap><a href='#' style='color:red' id='"+id+"crafts' onclick=\"showCrafts('"+valueArray[7]+"','"+valueArray[2]+"')\">"+valueArray[8]+"</a></td>";//工艺版本
			tds += "<td align='center' nowrap><a href='#' style='color:red' id='"+id+"crafts' onclick=\"showDrawings('"+valueArray[9]+"')\">"+valueArray[10]+"</a></td>";//图纸版本
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