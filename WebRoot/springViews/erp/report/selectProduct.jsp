<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%
	String param=request.getParameter("param");
	if(param==null||param.equals("")){
		param="-1";
			}
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<title>产品列表</title>
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
		<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
		<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
		<script> 
var pageMgr = null;
var parentWindowObj;
var arrMainPro="";
function doInit(){
	parentWindowObj = window.dialogArguments;
	var url = "<%=contextPath %>/SpringR/product/getCpProduct/"+new Date().getTime();
	 var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"cpId", text:"cpId"},
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"proCode",  width: '10%', text:"产品编号",render:recordCenterFunc},    
	         {type:"data", name:"proName",  width: '10%', text:"名称",render:recordCenterFunc},
   	         {type:"data", name:"proPrice",  width: '10%', text:"标准单价",render:recordCenterFunc},
	         {type:"data", name:"uName",  width: '10%', text:"类别",render:recordCenterFunc},    
	         {type:"data", name:"ptName",  width: '10%', text:"单位",render:recordCenterFunc},
	         {type:"data", name:"psName",  width: '10%', text:"所属大类",render:recordCenterFunc},
  			{type:"selfdef", text:"操作", width: '20%',render:opts}]
	    };
    
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无符合条件的疫苗信息', 'msrg');
    }
 
}
function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}
function opts(cellData, recordIndex, columIndex){
	var id = this.getCellData(recordIndex, "id");
	var proName = this.getCellData(recordIndex, "proName");
	return "<center><a href=\"javascript:void(0)\" onclick=\"add('" + id+"','"+proName +"');\" ><font color='red'>+添加</font></a></center>";
}
function add(id,proName){
	var str=id+","+proName;
 	window.parent.returnValue=str;
 	window.parent.close();
  } 
</script>
	</head>
	<body topmargin="5" onload="doInit()">
		<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" >
</div>

<div id="msrg">
</div>
	</body>
</html>