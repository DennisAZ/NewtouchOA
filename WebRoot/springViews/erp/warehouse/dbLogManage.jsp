<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>

<%
	String flag=request.getParameter("flag");
	if("".equals(flag)||flag==null){
		flag="3";
	}
	String beginTime=request.getParameter("beginTime");
	if("".equals(beginTime)||beginTime==null){
		beginTime="";
	}
	String endTime=request.getParameter("endTime");
	if("".equals(endTime)||endTime==null){
		endTime="";
	}
	String proName=request.getParameter("proName");
	if("".equals(proName)||proName==null){
		proName="";
	}
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
var  flag="<%=flag%>";
var  beginTime="<%=beginTime%>";
var  endTime="<%=endTime%>";
var  proName="<%=proName%>";
function doInit(){
  initTime();
	 var url = "<%=contextPath %>/SpringR/db/getDBLog?flag="+flag+"&beginTime="+beginTime+"&endTime="+endTime+"&proName="+encodeURIComponent(proName);
	  if(flag=="<%=oa.spring.util.StaticData.DB%>"){
	  	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
     	   sortIndex: 2,
      	  sortDirect: "desc",
	      colums: [
	      	 {type:"hidden", name:"remark", width: '15%', text:"备注",render:recordCenterFunc},
	             {type:"hidden", name:"dbId",  width: '10%', text:"dbId",render:recordCenterFunc},
	             {type:"hidden", name:"proId",  width: '10%', text:"proId",render:recordCenterFunc},
	             {type:"hidden", name:"whId",  width: '10%', text:"whId",render:recordCenterFunc},
	             {type:"data", name:"name",  width: '10%', text:"仓库名称",render:recordCenterFunc},
	              {type:"selfdef", name:"op",  width: '5%', text:"库存操作",render:selectOpt},    
	             {type:"selfdef", name:"opType",  width: '8%', text:"操作类型",render:selectType},
	        	 {type:"data", name:"pro_code", width: '10%', text:"产品编号",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}},   
	        	 {type:"data", name:"pro_name", width: '10%', text:"产品名称",render:recordCenterFunc},   
	        	 {type:"data", name:"price", width: '8%', text:"价格",render:recordCenterFunc},   
	        	 {type:"data", name:"num", width: '5%', text:"数量",render:recordCenterFunc},   
	        	 {type:"data", name:"time", width: '15%', text:"日期",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}},
	        	 {type:"selfdef", name:"optss", width: '15%', text:"修改详细",render:optss}
	        	  
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
	  
	  }else{
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
     	   sortIndex: 2,
      	  sortDirect: "desc",
	      colums: [
	         {type:"hidden", name:"remark", width: '15%', text:"备注",render:recordCenterFunc},
	             {type:"hidden", name:"id",  width: '10%', text:"typeId",render:recordCenterFunc},
	             {type:"data", name:"name",  width: '10%', text:"仓库名称",render:recordCenterFunc},
	              {type:"selfdef", name:"op",  width: '5%', text:"库存操作",render:selectOpt},    
	             {type:"selfdef", name:"opType",  width: '8%', text:"操作类型",render:selectType},
	             {type:"data", name:"name",  width: '10%', text:"操作编号",render:codeOpt,sortDef:{type:0, direct:"asc"}},
	        	 {type:"data", name:"pro_code", width: '10%', text:"产品编号",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}},   
	        	 {type:"data", name:"pro_name", width: '10%', text:"产品名称",render:recordCenterFunc},   
	        	 {type:"data", name:"price", width: '8%', text:"价格",render:recordCenterFunc},   
	        	 {type:"data", name:"num", width: '5%', text:"数量",render:recordCenterFunc},   
	        	 {type:"data", name:"time", width: '15%', text:"日期",render:recordCenterFunc,sortDef:{type:0, direct:"asc"}},
	        	
	        	 {type:"selfdef", name:"optss", width: '15%', text:"修改详细",render:optss}
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
	}
	 selectOption(); //查询条件参数的传递
	 toQueryTime();
	 jQuery("#proName").val(proName);
}
function selectOption(){
     var selects = document.getElementById("flag");
	 for(var i = 0; i < selects.length; i++){
    var prc = selects[i];
    if(flag == prc.value){
      prc.selected = true;
    }
  }
}
function optss(cellData, recordIndex, columIndex){
	var objString = this.getCellData(recordIndex, "remark");
	var objLength=objString.length;
if(objLength>5){
		return "<center><a    title='"+objString+"'  href=\"javascript:void(0)\"  >"+objString.substring(0,5)+"...</a></center>"
}	
	
}
function toQueryTime(){
	jQuery('#beginTime').val(beginTime);
    jQuery('#endTime').val(endTime);
    return ;
}
function typeDetail(url){
	openDialogResize(url,  520, 400);
}
function codeOpt(cellData, recordIndex, columIndex){
var typeId = this.getCellData(recordIndex, "id");
	if(flag=="<%=oa.spring.util.StaticData.PUR%>"){
			var url="detailPur.jsp?pDeId="+typeId;
			return "<center><a  href=\"javascript:void(0)\" style=\"color:red\" onclick=\"typeDetail('" +url + "');\" >"+cellData+"</a></center>";
		}else if(flag=="<%=oa.spring.util.StaticData.SALE%>"){
			var url="showDetials.jsp?pod_id="+typeId;
			 	return "<center><a href=\"javascript:void(0)\" style=\"color:red\" onclick=\"typeDetail('" +url + "');\" >"+cellData+"</a></center>";
		
		}else if(flag=="<%=oa.spring.util.StaticData.SALE_RETURN%>"){
				var url="showReturnDetial.jsp?retId="+typeId;
			 	return "<center><a href=\"javascript:void(0)\" style=\"color:red\" onclick=\"typeDetail('" +url + "');\" >"+cellData+"</a></center>";
	
		}else if(flag=="<%=oa.spring.util.StaticData.PUR_RETURN%>"){
				var url="showReturnDetial.jsp?retId="+typeId;
			 	return "<center><a href=\"javascript:void(0)\" style=\"color:red\" onclick=\"typeDetail('" +url + "');\" >"+cellData+"</a></center>";
		
		}else if(flag=="<%=oa.spring.util.StaticData.LOSS%>"){
			    var url="lossDetail.jsp?id="+typeId;
			 	return "<center><a href=\"javascript:void(0)\" style=\"color:red\" onclick=\"typeDetail('" +url + "');\" >"+cellData+"</a></center>";
		
		}else if(flag=="<%=oa.spring.util.StaticData.PRODUCE_PICK%>"){
				var url="showbom.jsp?planId="+typeId+"&type='1'";
			 	return "<center><a href=\"javascript:void(0)\" style=\"color:red\" onclick=\"typeDetail('" +url + "');\" >"+cellData+"</a></center>";
		
		}else if(flag=="<%=oa.spring.util.StaticData.CHECK_IN%>"){
			    var url="showWHExam.jsp?planId="+typeId;
			 	return "<center><a href=\"javascript:void(0)\" style=\"color:red\" onclick=\"typeDetail('" +url + "');\" >"+cellData+"</a></center>";
		
		}else if(flag=="<%=oa.spring.util.StaticData.PRODUCE_RETURN%>"){
			 	var url="showbom.jsp?planId="+typeId+"&type='1'";
			 	return "<center><a href=\"javascript:void(0)\" style=\"color:red\" onclick=\"typeDetail('" +url + "');\" >"+cellData+"</a></center>";
		}
}
function selectOpt(cellData, recordIndex, columIndex){
		if(flag=="<%=oa.spring.util.StaticData.PUR%>"||flag=="<%=oa.spring.util.StaticData.DB%>"||flag=="<%=oa.spring.util.StaticData.CHECK_IN%>"||flag=="<%=oa.spring.util.StaticData.PRODUCE_RETURN%>"){
			return "<center>入库</center>";
		}else {
			return "<center>出库</center>";
		}
}
function selectType(cellData, recordIndex, columIndex){
		if(flag=="<%=oa.spring.util.StaticData.PUR%>"){
			return "<center>采购入库</center>";
		}else if(flag=="<%=oa.spring.util.StaticData.SALE%>"){
			return "<center>销售出库</center>";
		
		}else if(flag=="<%=oa.spring.util.StaticData.SALE_RETURN%>"){
			return "<center>销售退货</center>";
	
		}else if(flag=="<%=oa.spring.util.StaticData.DB%>"){
			return "<center>直接入库</center>";
	
		}else if(flag=="<%=oa.spring.util.StaticData.PUR_RETURN%>"){
			return "<center>采购退货</center>";
		
		}else if(flag=="<%=oa.spring.util.StaticData.LOSS%>"){
			return "<center>库存损耗</center>";
		
		}else if(flag=="<%=oa.spring.util.StaticData.PRODUCE_PICK%>"){
			return "<center>生产领料</center>";
		
		}else if(flag=="<%=oa.spring.util.StaticData.CHECK_IN%>"){
			return "<center>生产请检</center>";
		
		}else if(flag=="<%=oa.spring.util.StaticData.PRODUCE_RETURN%>"){
			return "<center>生产退料</center>";
		}
}
function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}

function showManage(){
 var beginTime=jQuery('#beginTime').val();
 var endTime=jQuery('#endTime').val();
 var flag=jQuery('#flag').val();
 var proName=jQuery('#proName').val();
 if(flag=="-4"){
 	alert("请先选择操作类型！");
 	return false;
 }
 window.location = "dbLogManage.jsp?flag="+flag+"&beginTime="+beginTime+"&endTime="+endTime+"&proName="+encodeURIComponent(proName);
 }
function checkForm(){
  var beginTime = $F('beginTime');
  var endTime = $F('endTime');
  if (endTime && beginTime && beginTime > endTime){ 
    alert("结束时间不能小于起始时间！");
    return false;
  }
  return true;
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
  <td class="TableData" >
 	状态:
			<select id="flag" name="flag" >
				<option value="<%=oa.spring.util.StaticData.DB%>">直接入库</option>
				<option value="<%=oa.spring.util.StaticData.PUR%>">采购入库</option>
				<option value="<%=oa.spring.util.StaticData.SALE%>">销售出库</option>
				<option value="<%=oa.spring.util.StaticData.SALE_RETURN%>">退货入库</option>
				<option value="<%=oa.spring.util.StaticData.PUR_RETURN%>">退货出库</option>
				<option value="<%=oa.spring.util.StaticData.LOSS%>">库存损耗</option>
				<option value="<%=oa.spring.util.StaticData.PRODUCE_PICK%>">生产领料</option>
				<option value="<%=oa.spring.util.StaticData.PRODUCE_RETURN%>">生产退料</option>
				<option value="<%=oa.spring.util.StaticData.CHECK_IN%>">请检入库</option>
			</select>
      </td>
          <td nowrap class="TableData">产品名称： 
          	   <input type="text" id="proName" name="proName" size="12" maxlength="12" class="BigInput" value="" >
          </td>
      <td nowrap class="TableData">起始时间： 
        <input type="text" id="beginTime" name="beginTime" size="12" maxlength="12" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">截止时间：
        <input type="text" id="endTime" name="endTime" size="12" maxlength="12" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="endTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td> 
      <td><input type="button" value="搜索" onclick="showManage()"  class="BigButton" ></td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>


<div id="msrg">
</div>
</body>
</html>