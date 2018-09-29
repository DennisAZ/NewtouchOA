<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%
		String status=request.getParameter("status");
	if("".equals(status)||status==null){
		status="in";
	}
	String beginTime=request.getParameter("beginTime");
	if("".equals(beginTime)||beginTime==null){
		beginTime="";
	}
	String endTime=request.getParameter("endTime");
	if("".equals(endTime)||endTime==null){
		endTime="";
	}
	String name=request.getParameter("name");
	if("".equals(name)||name==null){
		name="";
	}
	String bankId=request.getParameter("bankId");
	if("".equals(bankId)||bankId==null){
		bankId="";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>银行卡交易往来账务</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
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
var  status="<%=status%>";
var  beginTime="<%=beginTime%>";
var  endTime="<%=endTime%>";
var  name="<%=name%>";
var  bankId="<%=bankId%>";
function doInit(){
initTime();
     var url = "<%=contextPath %>/SpringR/finance/bankRecord?status="+status+"&beginTime="+beginTime+"&endTime="+endTime+"&name="+encodeURIComponent(name)+"&bankId="+bankId;
	  var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"data", name:"code",  width: '15%', text:"编号",render:recordCenterFunc},    
	         {type:"data", name:"bankName",  width: '15%', text:"银行名称",render:recordCenterFunc},    
	         {type:"data", name:"account",  width: '20%', text:"银行卡号",render:recordCenterFunc},    
	         {type:"data", name:"customer",  width: '15%', text:"客户名称",render:recordCenterFunc},    
	         {type:"data", name:"money",  width: '15%', text:"金额",render:recordCenterFunc},    
	         {type:"data", name:"date",  width: '15%', text:"时间",render:recordCenterFunc},
	         {type:"data", name:"person",  width: '15%', text:"操作人",render:recordCenterFunc}]      
	    };
	    pageMgr = new YHJsPage(cfgs);
	    pageMgr.show();
	    var total = pageMgr.pageInfo.totalRecord;
	    if(total){
	      showCntrl('listContainer');
	      var mrs = " 共 " + total + " 条记录 ！";
	    }else{
	      WarningMsrg('无信息展示', 'msrg');
	    }
	    
	    selectOption(); //查询条件参数的传递
	    
	    toQueryTime(); //查询时间
}

function selectOption(){
     var selects = document.getElementById("status");
	 for(var i = 0; i < selects.length; i++){
    var prc = selects[i];
    if(status == prc.value){
      prc.selected = true;
    }
  }
}

function toQueryTime(){
	jQuery('#beginTime').val(beginTime);
    jQuery('#endTime').val(endTime);
    jQuery('#name').val(name);
    return ;
}
function showManage(){
if (checkForm()){

 var statuss=jQuery('#status').val();
 if(statuss==-4){
 	alert("请先选择状态！");
 	return false;
 }
 var beginTime=jQuery('#beginTime').val();
 var endTime=jQuery('#endTime').val();
 var name=jQuery('#name').val();
 window.location = "bankRecord.jsp?status="+statuss+"&beginTime="+beginTime+"&endTime="+endTime+"&name="+encodeURIComponent(name);
	 }
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

function recordCenterFunc(cellData) {
	return "<center>" + cellData + "</center>";
}





</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
 	 <td class="TableData" >
 	状态:
			<select id="status" name="status" >
				<option value="in"> 收款账务</option>
				<option value="out">出款账务</option>
			</select>
      </td>
      <td class="TableData">客户或者供货商
      	   <input type="text" id="name" name="name"  class="BigInput" >
      </td>
      <td nowrap class="TableData">起始时间： 
        <input type="text" id="beginTime" name="beginTime" size="14" maxlength="14" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">截止时间：
        <input type="text" id="endTime" name="endTime" size="14" maxlength="14" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="endTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td> 
      <td><input type="button" value="搜索" onclick="showManage()"  class="BigButton" ></td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;
         <a href="javascript:deleteAll();" title="删除所选信息"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选信息</a>&nbsp;
      </td>
 </tr>
</table>
</div>

<div id="msrg">
</div>
</body>
</html>