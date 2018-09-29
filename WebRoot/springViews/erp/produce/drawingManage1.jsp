<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="oa.spring.util.ContractCont"%>
<%
	String pro_id = request.getParameter("pro_id");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>生产图纸管理</title>
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
<script type="text/javascript" src="<%=contextPath %>/springViews/erp/produce/attachMenu.js"></script>
<script> 
var parentWindowObjs="";
var pageMgr = null;
function doInit(){
parentWindowObjs = window.dialogArguments;
     var url = "<%=contextPath %>/SpringR/produce/drawingProManage?pro_id=<%=pro_id%>";
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"data", name:"code",  width: '15%', text:"图号",render:recordCenterFunc},    
	         {type:"data", name:"pro_id",  width: '15%', text:"所属产品",render:selectProductFunc},    
	         {type:"data", name:"dt_id",  width: '15%', text:"图纸类别",render:selectDrawingTypeFunc},    
	         {type:"data", name:"drawing_version",  width: '15%', text:"所用版本",render:recordCenterFunc},
	         {type:"data", name:"is_using",  width: '15%', text:"当前是否使用",render:options}]
	    };
	    pageMgr = new YHJsPage(cfgs);
	    pageMgr.show();
	    var total = pageMgr.pageInfo.totalRecord;
	    if(total){
	      showCntrl('listContainer');
	      var mrs = " 共 " + total + " 条记录 ！";
	      //showCntrl('delOpt');
	    }else{
	      WarningMsrg('无记录信息显示', 'msrg');
	    }
}

function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}

function selectProductFunc(proId){
    var pro_name = "";
	var url = "<%=contextPath %>/SpringR/produce/selectProductByProId?proId="+proId;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
		  var data = JSON.stringify(jsonData);
		  var json = eval("(" + data + ")");
		  pro_name = json.pro_name;
	   }
	 });
	  return "<center>" + pro_name + "</center>";
}

function selectDrawingTypeFunc(dt_id){
    var drawing_type_name = "";
	var url = "<%=contextPath %>/SpringR/produce/selectDrawingTypeByDtId?dt_id="+dt_id;
	jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
		  var data = JSON.stringify(jsonData);
		  var json = eval("(" + data + ")");
		  drawing_type_name = json.name;
	   }
	 });
	  return "<center>" + drawing_type_name + "</center>";
	}

	  function options(cellData, recordIndex, columIndex){
		var is_using = this.getCellData(recordIndex, "is_using");
		var drawingId = this.getCellData(recordIndex, "id");
		var pro_id = this.getCellData(recordIndex, "pro_id");
		if(is_using == '1'){
			return "<center>是</center>";
		}	  
		if(is_using == '0'){
			return "<center><a href='#' onclick=\"setUsing('"+drawingId+"','"+pro_id+"')\"><font color='red'>设置为当前版本</font></a></center>";
		}	  
	  }
	  

function changeUsing(pro_id){
	 var url = "<%=contextPath %>/SpringR/product/changeDrawUsing?pro_id="+pro_id;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
		 }
	  });
}
function setUsing(drawingId,pro_id)
{
	changeUsing(pro_id);
	var url = "<%=contextPath %>/SpringR/product/setDrawingUsing?drawingId="+drawingId;
	 jQuery.ajax({
	   type : 'POST',
	   url : url,
	   async:false,
	   success : function(jsonData){   
	   			alert("设置成功");
	   			parentWindowObjs.location.assign("nchooseProduct.jsp");
				self.close();
		 }
	  });
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
<input type="hidden" id="attachmentId" name="attachmentId"/>
<input type="hidden" id="attachmentName" name="attachmentName"/>
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;生产图纸管理</span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>


<div id="msrg">
</div>
</body>
</html>