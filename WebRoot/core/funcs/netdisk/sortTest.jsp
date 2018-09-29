<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqIdStr=request.getParameter("seqId");
	String pathId = request.getParameter("DISK_ID");
	int seqId=0;
	
	if (pathId == null){
		pathId = (String)request.getAttribute("DISK_ID");
	}
	if(seqIdStr!=null){
	  seqId=Integer.parseInt(seqIdStr);
	}
	
	
	

	
	

	
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>排序测试</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="js/index1.js"></script>
<script type="text/javascript">

function doInit() {
  var cfgs = {
    dataAction: contextPath + "/yh/core/funcs/netdisk/act/YHNetDiskAct/getNetDiskInfoPage.act?seqId=<%=seqId%>&DISK_ID=<%=pathId%>",
    container: "listContainer",
    paramFunc: '',
    sortIndex: 3,
    sortDirect: "asc",
    colums: [{type:"check",  width: "10%"},    

             //{type:"data", name:"filePath", text:"顺序号", width: 150, dataType:"int", floatMenu:menuData2, iconFunc: getIcon},
             {type:"hidden", name:"filePath"},
             {type:"data", name:"fileName", text:"文件名", width: "20" ,dataType:"text", render:render, bindAction:bindAction, sortDef:{type: 0, direct:"asc"}},
             {type:"data", name:"fileSpace", styleFunc:getStyle, text:"大小", width: "20%",align:"center", dataType:"int", sortDef:{type: 0, direct:"desc"}},       
             {type:"data", name:"fileType", styleFunc:getStyle, text:"类型", width: "20%", align:"center", dataType:"text", sortDef:{type: 0, direct:"desc"}},
             {type:"data", name:"fileModifyTime", styleFunc:getStyle, text:"修改时间", width: "20%", dataType:"date", sortDef:{type: 0, direct:"desc"}}
             ] 
       
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}



/**
 * 自定义描画函数，以分页对象为thisObject执行该函数

 * @cellData            单元格数据

 * @recordIndex         当前页面记录索引，从0开始计数

 * @columIndex          栏目索引
 */
function render(cellData, recordIndex, columIndex) {
  //alert(cellData);
  if (cellData > 200) {
    return "<span id=\"span_" + recordIndex + "_" + columIndex + "\" style=\"color:red\">" + cellData + "</span>";
  }else {
    return "<span id=\"span_" + recordIndex + "_" + columIndex + "\" style=\"color:green\">" + cellData + "</span>";
  }
}

/**
 * 自定义事件绑定，与自定义描画函数配合使用
 * @cellData            单元格数据

 * @recordIndex         当前页面记录索引，从0开始计数

 * @columIndex          栏目索引
 */
function bindAction(cellData, recordIndex, columIndex) {
  var cntrl = $("span_" + recordIndex + "_" + columIndex);
  cntrl.observe("click", function(){alert(this.pageInfo.pageIndex);}.bind(this));
}


function getStyle(cellData, recordIndex, columIndex) {
  var record = this.getRecord(recordIndex);
  if (record["isEmptyPass"] == "1") {
    return {color: "#FF0000"};
  }else {
    return {color: "#00FF00"};
  }
}

/**
 * @recordIndex         当前页面记录索引，从0开始计数

 */
function test1(recordIndex) {
  alert(this.getRecord(recordIndex).seqId);
}

</script>
</head>
<body onload="doInit();">
<div id="listContainer"></div>

</body>
</html>