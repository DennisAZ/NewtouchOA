<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String param=request.getParameter("param");
	if(param==null||"".equals(param)){
		param="-1";
	}
 %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>产品库存管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<!-- 封装表单的数据提交 -->
<style type="text/css">
p{display: none;position:absolute;right: 200px;top:70px;width: 0px solid #c30;width: 150px;height: 100px;background: #ccc;overflow: hidden;}
</style>
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
var param="<%=param%>";
function doInit(){
     var url = "<%=contextPath %>/SpringR/db/getDBMsg?param="+encodeURIComponent(param);
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"dbId", text:"id"},
	         {type:"hidden", name:"proId", text:"proId"},
	         {type:"hidden", name:"whId", text:"whId"},
	         {type:"data", name:"name",  width: '15%', text:"仓库名称",render:recordCenterFunc},    
	         {type:"hidden", name:"psName",text:"仓库类型"},
	         {type:"data", name:"pro_code", width: '10%', text:"产品编号",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}},   
	         {type:"data", name:"pro_name", width: '20%', text:"产品名称",render:recordCenterFunc},   
	         {type:"data", name:"batch", width: '15%', text:"批次编号",render:recordCenterFunc},   
	         {type:"data", name:"invalid_time", width: '10%', text:"失效日期",render:recordCenterFunc},   
	         {type:"data", name:"price", width: '10%', text:"入库单价",render:recordCenterFunc},   
	         {type:"data", name:"num", width: '5%', text:"数量",render:recordCenterFunc},   
	         {type:"hidden", name:"remark",text:"备注"},
	        {type:"selfdef", text:"操作", width: '15%',render:opts}]
	    };
	    pageMgr = new YHJsPage(cfgs);
	    pageMgr.show();
	    var total = pageMgr.pageInfo.totalRecord;
	    if(total){
	      showCntrl('listContainer');
	      var mrs = " 共 " + total + " 条记录 ！";
	      //showCntrl('delOpt');
	    }else{
	      WarningMsrg('无信息展示', 'msrg');
	    }
	    showStyle();
	    
	    selectOption(); //查询条件参数的传递
	    
	    toWhName(); //查询仓库名称
}

function remarkFoalt(cellData, recordIndex, columIndex){
	var dbId = this.getCellData(recordIndex, "dbId");
	var objString = this.getCellData(recordIndex, "remark");
	var objLength=cellData.length;
if(objLength>5){
		return "<center><a   id='showRemark"+dbId+"' class='showRemark' title='"+objString+"'  href=\"javascript:void(0)\"  >"+cellData.substring(0,5)+"...</a></center>"
}	
	
}
function showRemark(remark,dbId){
	var divs="<p id='"+dbId+"'>"+remark+"</p>";
	jQuery('#showRemark'+dbId).append(divs);	
	jQuery('#'+dbId).show();	
		jQuery('.showRemark').mouseout(function(){
		jQuery('#'+dbId).hide();	
	});
}
function selectOption(){
     var selects = document.getElementById("styleId");
     var arrStr = new Array();
	arrStr = param.split(',');
	 for(var i = 0; i < selects.length; i++){
    var prc = selects[i];
    if(arrStr[1] == prc.value){
      prc.selected = true;
    }
  }
}

function toWhName(){
	var arrStr = new Array();
	arrStr = param.split(',');
	if(arrStr.length == 1){
	 jQuery('#whName').val("");
	}else{
	 jQuery('#whName').val(arrStr[0]);
	 if(arrStr[2]!="0")
	 jQuery('#proName').val(arrStr[2]);
	}
    return ;
}


function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}
function selectData(cellData) {
	var rtStr = "";
	var url = "<%=contextPath %>/SpringR/warehouse/getWHTypeName?type="+cellData;
	jQuery.ajax({
	   type : 'POST',
	   async:false,
	   url : url,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				var names="";
				jQuery(json).each(function(i,item){
					if(names != ""){
						names+=",";
					}
					names+=item.name;
				});
				rtStr = "<center>"+names+"</center>";
			}
			else{
				rtStr = "<center></center>";
			}
	   }
	 });
	return rtStr;
}
function opts(cellData, recordIndex, columIndex){
	var id = this.getCellData(recordIndex, "dbId");
	var whId = this.getCellData(recordIndex, "whId");
	var proId = this.getCellData(recordIndex, "proId");
	var batch = this.getCellData(recordIndex, "batch");
	return "<center><a href=\"javascript:void(0)\" onclick=\"editDB('" + id + "');\" >编辑</a>&nbsp&nbsp<a href=\"javascript:void(0)\" onclick=\"deleteDB('" + id + "');\" >删除</a>&nbsp&nbsp<a href=\"javascript:void(0)\" onclick=\"dbLog('"+whId+"','"+proId+"','"+batch+"');\" >查看记录</a></center>";
}

function editDB(id){
	openDialogResize('dbmodify.jsp?id='+id,  520, 400);
}
function dbLog(whId,proId,batch){
	window.location.href='dbDetailLog.jsp?whId='+whId+'&proId='+proId+'&batch='+batch;
}
function deleteDB(id){
	var url = "<%=contextPath %>/SpringR/db/deleteDBById?id="+id;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "0"){
		    alert("删除成功");
		    window.location.reload();
		  }
		  else{
			   alert("删除失败"); 
		  }
	   }
	 });
}
function short(){
	var param="";
	var whName=jQuery("#whName").val();
	var proName=jQuery("#proName").val();
	if(proName==""||proName==null){
		proName="0";
	}
	selectWhName = whName;
	var styleId=jQuery("#styleId").val();
	selectedStyleId = styleId;
	if((whName==null||whName=="")&&(styleId=="0")&&(proName==""||proName==null)){
		param="";
	}else{
		param=whName+","+styleId+","+proName;
	}
		window.location.href='dbManage.jsp?param='+encodeURIComponent(param);
	}
	
function showStyle(){
var url = "<%=contextPath %>/SpringR/warehouse/getStyle/"+new Date().getTime();
	jQuery.ajax({
	   type : 'POST',
	   async:false,
	   url : url,
	   success : function(jsonData){   
			var data = JSON.stringify(jsonData);
			var json = eval("(" + data + ")");
			if(json.length > 0){
				jQuery(json).each(function(i,item){
						jQuery('#styleId').append("<option  value='"+item.id+"' >"+item.name+"</option>");
				});
			}
	   }
	 });
}
function excelDb(){
	window.parent.location.href="dbImport.jsp";
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3"
			class="small">
     
			<tr>
				<td  align="right">仓库名称：</td>
				<td align="center"><input type="text" id="whName"  name="whName"  value="" class="BigInput"></td>
				<td  align="right">产品名称：</td>
				<td align="center"><input type="text" id="proName"  name="proName"  value="" class="BigInput"></td>
				<td nowrap class="TableData">选择仓库：</td>
    	 	    <td class="TableData" >
       		         <select id="styleId" name="styleId">
       		         	<option value="0">全部</option>
      	 	         </select>
     		    </td>
				<td><input type="button" value="搜索"  class="BigButton"  onclick="short()"/> </td> 
				<td><input type="button" value="批量导入"  class="BigButton"  onclick="excelDb()"/> </td> 
				
			</tr>
			
		</table>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;
         <a href="javascript:deleteAll();" title="删除所选仓库信息"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选仓库信息</a>&nbsp;
      </td>
 </tr>
</table>
</div>

<div id="msrg">
</div>
</body>
</html>