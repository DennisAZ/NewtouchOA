<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>生产工艺管理</title>
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
     var url = "<%=contextPath %>/SpringR/produce/getCrafts/"+new Date().getTime();
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"id", text:"id"},
	         {type:"hidden", name:"pro_id",  width: '10%', text:"pro_id"},  
	         {type:"data", name:"pro_code",  width: '10%', text:"产品编号",render:recordCenterFunc},    
	         {type:"data", name:"pro_name",  width: '20%', text:"产品名称",render:recordCenterFunc},
	         {type:"data", name:"crafts_version",  width: '10%', text:"版本号",render:recordCenterFunc},
	         {type:"data", name:"is_using",  width: '10%', text:"是否为当前版本",render:options},    
	         {type:"data", name:"remark",  width: '10%', text:"备注",render:recordCenterFunc},
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
	      WarningMsrg('无符合条件的生产工艺信息', 'msrg');
	    }
}

function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}

function opts(cellData, recordIndex, columIndex){
	var craftsId = this.getCellData(recordIndex, "id");
	var pro_id = this.getCellData(recordIndex, "pro_id");
	var pro_name = this.getCellData(recordIndex, "pro_name");
	var crafts_version = this.getCellData(recordIndex, "crafts_version");
		
	return "<center><a href=\"javascript:void(0)\" onclick=\"producessDetail('" + craftsId + "','"+pro_name+"','"+crafts_version+"');\" >查看工序</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"craftsDetail('" + craftsId + "');\" >查看</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"edit('" + craftsId + "');\" >编辑</a>&nbsp<a href=\"javascript:void(0)\" onclick=\"deletes('" + craftsId + "');\" >删除</a></center>";
}

function options(cellData, recordIndex, columIndex){
	var status = this.getCellData(recordIndex, "is_using");
	if(status==1){
	return "<center>是</center>";
	}else if(status==0){
		return "<center>否</center>";
	}
	}

function edit(id){
	window.location.href='craftsEdit.jsp?craftsId='+id;
}
function craftsDetail(id){
	window.location.href='craftsDetail.jsp?craftsId='+id;
}
function producessDetail(id,pro_name,crafts_version){
	window.location.href='processManage2.jsp?craftsId='+id+'&pro_name='+pro_name+"&crafts_version="+crafts_version;
}
function deletes(id){
	var url = "<%=contextPath %>/SpringR/produce/deleteProduce?craftsId="+id;
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
function newCreate(){
	window.location.href="processAdd.jsp";
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;生产工艺管理</span>
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