<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>


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
function doInit(){
	
     var url = "<%=contextPath %>/SpringR/warehouse/getWareHouse/"+new Date().getTime();
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
		  sortColumIndex : 1,
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"name",  width: '20%', text:"仓库名称",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}},    
	         {type:"data", name:"address", width: '20%', text:"仓库地址",render:recordCenterFunc},   
	         {type:"data", name:"type",  width: '20%',text:"仓库类型",render:selectData},
	         {type:"data", name:"remark",  width: '10%', text:"备注",render:recordCenterFunc},
        	 {type:"selfdef",width: '15%', text:"仓库管理员",render:getWHAdmin},
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
}

function getWHAdmin(cellData, recordIndex, columIndex){
	var rtStr = "";
	var id = this.getCellData(recordIndex, "id");
	var url = "<%=contextPath %>/SpringR/warehouse/getWHAdmin?id="+id;
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
					names+=item.userName;
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
	var id = this.getCellData(recordIndex, "id");
	return "<center><a href=\"javascript:void(0)\" onclick=\"editWH('" + id + "');\" >编辑</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"deleteWH('" + id + "');\" >删除</a></center>";
}

function editWH(id){
	window.location.href='addwh.jsp?id='+id;
}
function deleteWH(id){
	var url = "<%=contextPath %>/SpringR/warehouse/deleteWareHouseById?id="+id;
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