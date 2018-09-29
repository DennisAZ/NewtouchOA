<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%
	String cusId=request.getParameter("cusId");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>联系人列表</title>
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

<script type="text/javascript">
var pageMgr = null;
function doInit(){
     var url = "<%=contextPath %>/SpringR/saleOrder/custContact?cusId="+<%=cusId%>;
	  var cfgs = {
	      dataAction: url,
	      container: "listDiv",
	      colums: [
	        {type:"data", name:"conId",  width: '10%', text:"联系人编号",render:recordCenterFunc},   
	         {type:"data", name:"conName",  width: '10%', text:"联系人名称",render:recordCenterFunc},    
	         {type:"data", name:"conSex",  width: '10%', text:"联系人性别",render:recordCenterFunc},
	         {type:"data", name:"conDep",  width: '10%', text:"联系人所在部门",render:recordCenterFunc},    
	         {type:"data", name:"conPos",  width: '10%', text:"职位",render:recordCenterFunc},
	         {type:"data", name:"conAdd",  width: '10%', text:"地址",render:recordCenterFunc},    
	         {type:"data", name:"conPhone",  width: '10%', text:"手机",render:recordCenterFunc},
	         {type:"data", name:"conWorkPhone",  width: '10%', text:"电话",render:recordCenterFunc},
	         {type:"data", name:"conEmail",  width: '10%', text:"邮箱",render:recordCenterFunc},
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
	      WarningMsrg('无符合条件的联系人信息', 'msrg');
	    }
}


function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}
function opts(cellData, recordIndex, columIndex){
	var conName = this.getCellData(recordIndex, "conName");
	var conId = this.getCellData(recordIndex, "conId");
	var conPhone = this.getCellData(recordIndex, "conPhone");
	var conWorkPhone = this.getCellData(recordIndex, "conWorkPhone");
	var conAdd = this.getCellData(recordIndex, "conAdd");
	var conEmail = this.getCellData(recordIndex, "conEmail");
	return "<center><a href=\"javascript:void(0)\" onclick=\"add('" + conName+"','"+conId +"','"+conPhone+"','"+conWorkPhone+"','"+conAdd+ "','"+conEmail+"');\" >添加</a></center>";
}

function add(conName,conId,conPhone,conWorkPhone,conAdd,conEmail){
	var str=conName+","+conId+","+conPhone+","+conWorkPhone+","+conAdd+","+conEmail;
 	window.parent.returnValue=str;
 	window.parent.close();
  } 
</script>
</head>
<body topmargin="5" onload="doInit()">

<div id="listRecords" style="width:450px">
	<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" >
	  <tr>
	    <td class="Big">
	    <img src="<%=imgPath%>/infofind.gif" align="absMiddle">
	    <span class="big3">联系人列表</span>
	      
	    </td>
	  </tr>
	</table>
	<div id="listDiv" align="left"></div>
	
</div>

</body>
</html>