<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>

<%
	String whId=request.getParameter("whId")==null?"":request.getParameter("whId");
	String proId=request.getParameter("proId")==null?"":request.getParameter("proId");
	String batch=request.getParameter("batch")==null?"":request.getParameter("batch");
	
	String beginTime=request.getParameter("beginTime")==null?"":request.getParameter("beginTime");
	String endTime=request.getParameter("endTime")==null?"":request.getParameter("endTime");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>库存日志管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
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
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script> 
var pageMgr = null;
var  beginTime="<%=beginTime%>";
var  endTime="<%=endTime%>";
function doInit(){
  initTime();
	 var url = "<%=contextPath %>/SpringR/db/getDBDetail?whId=<%=whId%>&proId=<%=proId%>&batch=<%=batch%>&beginTime="+beginTime+"&endTime="+endTime;
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"data", name:"name",  width: '15%', text:"仓库名称",render:recordCenterFunc},    
	         {type:"data", name:"pro_code", width: '10%', text:"产品编号",render:recordCenterFunc},   
	         {type:"data", name:"pro_name", width: '20%', text:"产品名称",render:recordCenterFunc},   
	         {type:"data", name:"batch", width: '15%', text:"批次编号",render:recordCenterFunc},   
	         {type:"data", name:"flag", width: '20%', text:"出入描述",render:showFlagCenterFunc},
	         {type:"data", name:"number", width: '5%', text:"数量",render:recordCenterFunc},   
	         {type:"data", name:"time", width: '20%', text:"日期",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}}
	         ]   
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
	    toQueryTime();
	
}
function showFlagCenterFunc(cellData) {
	if(cellData == '1'){
		return "<center>采购入库</center>";
	}else if(cellData == '2'){
		return "<center>手动新建入库</center>";
	}else if(cellData == '3'){
		return "<center>销售出库</center>";
	}else if(cellData == '4'){
		return "<center>销售退货入库</center>";
	}else if(cellData == '5'){
		return "<center>采购退货出库</center>";
	}else if(cellData == '6'){
		return "<center>库存损耗出库</center>";
	}else if(cellData == '7'){
		return "<center>生产领料出库</center>";
	}else if(cellData == '8'){
		return "<center>生产请检入库</center>";
	}else if(cellData == '9'){
		return "<center>生产退料出库</center>";
	}
}
function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}
function toQueryTime(){
	jQuery('#beginTime').val(beginTime);
    jQuery('#endTime').val(endTime);
    return ;
}

function showManage(){
 var beginTime=jQuery('#beginTime').val();
 var endTime=jQuery('#endTime').val();
 window.location = "dbDetailLog.jsp?whId=<%=whId%>&proId=<%=proId%>&batch=<%=batch%>&beginTime="+beginTime+"&endTime="+endTime;
 }

function initTime(){
  var beginTimePara = {
      inputId:'beginTime',
      property:{isHaveTime:false},
      bindToBtn:'beginTimeImg'
  };
  new Calendar(beginTimePara);
  
  var endTimePara = {
      inputId:'endTime',
      property:{isHaveTime:false},
      bindToBtn:'endTimeImg'
  };
  
  new Calendar(endTimePara);

  var date = new Date();
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;库存日志管理</span>
   </td>
   
  
 </tr>
 <tr>
      <td nowrap class="TableData">起始时间： 
        <input type="text" id="beginTime" name="beginTime" size="19" maxlength="19" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">截止时间：
        <input type="text" id="endTime" name="endTime" size="19" maxlength="19" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="endTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td> 
      <td><input type="button" value="搜索" onclick="showManage()"  class="BigButton" ></td>
      <td><input type="button" value="返回" onclick="javascript:window.history.go(-1)"  class="BigButton" ></td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>


<div id="msrg">
</div>
</body>
</html>