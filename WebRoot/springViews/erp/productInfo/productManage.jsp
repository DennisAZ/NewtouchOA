<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>

<%
	String param=request.getParameter("param");
	if("".equals(param)||param==null){
		param="-1";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>产品信息管理</title>
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
var param="<%=param%>";
function doInit(){
     var url = "<%=contextPath %>/SpringR/product/productList?param="+encodeURIComponent(param);
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"proCode",  width: '15%', text:"产品编号",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}},    
	         {type:"data", name:"proName",  width: '15%', text:"名称",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}},
	         {type:"hidden", name:"shortName",  width: '10%', text:"产品简称",render:recordCenterFunc},
	         {type:"data", name:"proType",  width: '10%', text:"产品规格",render:recordCenterFunc},    
	         {type:"data", name:"proPrice",  width: '10%', text:"标准单价",render:recordCenterFunc},
	         {type:"data", name:"uName",  width: '10%', text:"单位",render:recordCenterFunc},   
	           {type:"data", name:"psName",  width: '10%', text:"所属大类",render:recordCenterFunc}, 
	         {type:"data", name:"ptName",  width: '10%', text:"类别",render:recordCenterFunc},
	          {type:"hidden", name:"pRemark",  width: '10%', text:"备注",render:recordCenterFunc},
	         {type:"selfdef", text:"操作", width: '20%',render:opts}]
	    };
	    pageMgr = new YHJsPage(cfgs);
	    pageMgr.show();
	    var total = pageMgr.pageInfo.totalRecord;
	    if(total){
	      showCntrl('listContainer');
	      var mrs = " 共 " + total + " 条记录 ！";
	      //showCntrl('delOpt');
	    }else{
	      WarningMsrg('无符合条件的产品信息', 'msrg');
	    }
}

function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}

function opts(cellData, recordIndex, columIndex){
	var proCode = this.getCellData(recordIndex, "id");
	return "<center><a href=\"javascript:void(0)\" onclick=\"edit('" + proCode + "');\" >编辑</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" + proCode + "');\" >删除</a></center>";
}

function edit(id){
	window.parent.location.href='proAdd.jsp?id='+id;
}
function deletes(id){
	var url = "<%=contextPath %>/SpringR/product/productDelete?id="+id;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   success : function(jsonData){   
		  if(jsonData == "2"){
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
         <a href="javascript:deleteAll();" title="删除所选产品所属大类信息"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选产品所属大类信息</a>&nbsp;
      </td>
 </tr>
</table>
</div>

<div id="msrg">
</div>
</body>
</html>