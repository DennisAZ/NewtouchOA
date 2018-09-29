<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
    <%@page import="java.text.*"%>
	 <%@ include file="/core/inc/header.jsp" %>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<head>
	<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
	<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/syslog/js/glsyslog.js"></script> 
	<title>日志管理</title>
	<script><!--
	// 根据条件删除
	function CheckForm()
	{    
	  if(document.form1.statrTime.value=="" && document.form1.endTime.value=="")
	  {  alert("起始时间和结束时间不能同时为空！");
	     return false;
	  }
	   var beginDate = document.getElementById('statrTime');
	   var endDate = document.getElementById("endTime");
	   var beginInt;
	   var endInt;
	   var beginArray = beginDate.value.split("-");
	   var endArray = endDate.value.split("-");
	   for(var i = 0 ; i<beginArray.length; i++){
		     beginInt = parseInt(" " + beginArray[i]+ "",10);  
		     endInt = parseInt(" " + endArray[i]+ "",10);
		     if((beginInt - endInt) > 0){
			       alert("起始日期不能大于结束日期!");
			       endDate.focus();
			       endDate.select();
			       return false;
		     }else if(beginInt - endInt<0){
		         break;
		     }  
	   }
	// IP 校验
	   if($F('IP')){ 
		   var pattern=/^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/; 
		   flag_ip=pattern.exec($F('IP')); // 或test($F('IP')) 都是校验公用的方法
		
		   if(!flag_ip) 
		   { 
		     alert("IP地址输入非法!"); 
		   	 return false;
		   }
	   }
	   var start = $("statrTime").value;
		  var end = $("endTime").value;
		  if(start != null && start !="" ){
			  if(!(/^(\d{4})(-|\/)(\d{2})\2(\d{2}) (\d{1,2}):(\d{2}):(\d{2})$/).exec(start)){
			      alert("起始时间格式不正确,如1999-10-10 00:00:00");
			      return false;
			    }
		  }if(end!=null && end!=""){
		    if(!(/^(\d{4})(-|\/)(\d{1,2})\2(\d{2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/).exec(end)){
		      alert("结束时间格式不正确,如1999-10-10 00:00:00");
		      return false;
		    }
		  }
	  if(document.form1.OP_EXPORT.checked){  //当点击单选按钮导出时  会提示		  var src=contextPath+"/yh/core/funcs/system/syslog/act/YHGlSyslogAct/SysExport.act";
		  document.form1.action=src;
		  document.form1.submit();
		  return true;
	  }
    if(document.form1.OP_QUERY.checked){
	    $("form1").action = contextPath + "/yh/core/funcs/system/syslog/act/YHGlSyslogAct/getMyGlSysLog.act";
	    document.form1.submit();
	    return true;
	  }
		/* 系统日志导入 
		if(document.form1.OP_IMPORT.checked){
		var src=contextPath+"/yh/core/funcs/system/syslog/act/YHGlSyslogAct/SysImport.act";
		document.form1.action=src;
		document.form1.submit();
		alert("系统开发中、、、");
		return false;
		}
		*/
	   
	   if (document.form1.OP_DELETE.checked) { //当你选中 单选按钮删除时
	      if(!window.confirm("确定要删除吗，删除后将不可恢复！")) {
	        return false;
	      }
	      doInit3();
	   }
	   return false;
	}
  // 根据单选按钮来删除
	function doInit3(){
	  var queryParam = $("form1").serialize();
	  var tmp = queryParam;
	  var url = contextPath+'/yh/core/funcs/system/syslog/act/YHGlSyslogAct/deleteRadio.act';
	  var json = getJsonRs(url,tmp); 
	
	   if (json.rtState == "0"){
		    var rtData = json.rtData;
		    var cou = rtData; 
				window.location.href="/yh/core/funcs/system/syslog/deletecount.jsp?cou=" + cou; //到deletecount.jsp 页面接收cou
	 		} else{
		    alert("没有符合条件数据删除！！");
		}
	}
	/*
	function checkForm(){ 
	  alert("222");
	  var queryParam = $("form1").serialize();
	  doInit3(queryParam); 
	}
	*/
	function doInit(){ 
	  getLogType();
	  //var url = contextPath+'/yh/core/funcs/system/syslog/act/YHSysLogSaveAct/getsysradio.act';
	  // var json = getJsonRs(url); 
	 // document.all.submit="";
	 //$("form1").action='/yh/yh/core/funcs/system/syslog/act/YHSysLogSaveAct/getsysradio.act';
	  //$("form1").submit();
	 /*  xin gong neng */
	  var data = <%= request.getAttribute("data")%>;  //获得act中data的对象
	   var td = "<td nowrap class=\"TableData\">日志表:</td>";
	   td+= "<td nowrap class=\"TableData\"><input type=radio name=COPY_TIME id=COPY_TIME checked><label for=COPE_TIME_>当前日志</label></input>";
     /*   
	   if(data!=null){
		    for(var i=0; i<data.length; i++){
		    var lengs = data[i];
		    var tableName = lengs.substring(lengs.indexOf("_2")+1,lengs.length);
		    //alert(lengs+":::"+tableName); 如果加这个功能可以打开以下日志
		   //td+= "<input type=radio name=COPY_TIME value="+tableName+" id=COPY_TIME_"+tableName+"><label for=COPY_TIME_"+tableName+">"+tableName+"</label></input>";
		   }
	 } */
	   td+= "</td>";
	   var tr = new Element("tr",{});
	   $('dataBody').appendChild(tr);  
	   tr.update(td);
	
	  var beginParameters = {
	      inputId:'statrTime',
	      property:{isHaveTime:true}, //isHaveTime:false 为false没有添加时钟
	      bindToBtn:'beginDateImg'
	        };
	  new Calendar(beginParameters);
	  var endParameters = {
	      inputId:'endTime',
	      property:{isHaveTime:true},
	       bindToBtn:'endDateImg'
	        };
	  new Calendar(endParameters);
	}
	// 系统日志文档
	function copy_log(){
	  alert("该系统正在开发中...");
	 // var msg = '确认要存档系统日志吗？';
	  //if(window.confirm(msg))
	  //window.location="copy.jsp";
	  //window.location="/yh/core/funcs/system/syslog/copy.jsp";
	  return false;
	}
	// 清空系统日志
	function delete_log(){
	  msg='确认要删除所选日志吗？';
	if(window.confirm(msg)){
	   // var par = 'delete_str='+delete_str;
	   var queryParam = $("form").serialize();
	   var tmp = queryParam;
	   var url = contextPath+'/yh/core/funcs/system/syslog/act/YHGlSyslogAct/deleteAllChecklog.act';
	   var json = getJsonRs(url,tmp); 
	   if (json.rtState == "0"){
	  		 var rtData = json.rtData;
	       var dele = rtData;
	  		 window.location.reload();//返回当前页面
	  	//xin gongneng	window.location="/yh/core/funcs/system/syslog/delete_all.jsp?right="+dele;
	    } else{
	  	      alert(json.rtMsrg);
	  	}
	  }
	}
	function ShowDialog(id){
	  
	  $('overlay').style.display = 'block';
	  $(id).style.display = 'block';
	//BackCompat 标准兼容模式关闭。	//当document.compatMode等于BackCompat时，浏览器客户区宽度是document.body.clientWidth；	//当document.compatMode等于CSS1Compat时，浏览器客户区宽度是document.documentElement.clientWidth
	  var bb =(document.compatMode &&　document.compatMode !="BackCompat")?document.documentElement:document.body;
	  if(document.compatMode && document.compatMode!="BackCompat"){  
		   $('overlay').style.width = bb.scrollWidth+"px"; // 网页正文全文宽： document.body.scrollWidth;
		   $('overlay').style.height = bb.scrollHeight+"px"; //网页正文全文高： document.body.scrollHeight;
	    }
	  else{
	      $('overlay').style.width = bb.scrollWidth+"px";
	      $('overlay').style.height = bb.scrollHeight+"px";
	   }
	//网页可见区域宽： document.body.offsetWidth   (包括边线的宽);
	//  网页可见区域高： document.body.offsetHeight  (包括边线的高);
	  $(id).style.left = ((bb.offsetWidth - $(id).offsetWidth)/2)+"px";   
	  $(id).style.top  = (90 + bb.scrollTop)+"px";//网页被卷去的高： document.body.scrollTop;
	  doInit2();
	}
	function doInit2(){
		  var data = <%= request.getAttribute("data")%>;
		  var td = "<td class=\"TableData\">请选择要清空的表:</td>";
		  td+= "<td nowrap class=\"TableData\"><input type=radio name=COPY_TIME id=COPY_TIME checked><label for=COPE_TIME_>当前日志</label></input>";
		  if(data!=null){
			   for(var i=0; i<data.length; i++){
			      var lengs = data[i];
			      var tableName = lengs.substring(lengs.indexOf("_2")+1,lengs.length);
			      // alert(lengs+":::"+tableName);
			      td+= "<input type=radio name=COPY_TIME value="+tableName+" id=COPY_TIME_"+tableName+"><label for=COPY_TIME_"+tableName+">"+tableName+"</label></input>";
			  }
		}
		  td+= "</td>";
		  var tr = new Element("tr",{});
		  $('dataBodys').appendChild(tr);  
		  tr.update(td);
	}
	function HideDialog(id)
	{   //alert("sss");
	   $("overlay").style.display = 'none';
	   $(id).style.display = 'none';
	}
	
	
	function getLogType(){
	  var requestURL = "<%=contextPath%>/yh/core/codeclass/act/YHCodeClassAct/getCodeItemByNo.act"; 
	  var json=getJsonRs(requestURL,{parentNo:'SYS_LOG'}); 
	  if(json.rtState == '1'){ 
	    alert(json.rtMsrg); 
	    return ; 
	  } 
	  var prcs = json.rtData; 
	  var selectObj = $("TYPE");
	  for(var i = 0 ; i < prcs.length ; i++){
	    var prc = prcs[i];
	    var seqId = prc.sqlId;
	    var classNO = prc.classNo;
	    var classCode = prc.classCode;
	    var calssDesc = prc.classDesc;
	    var myOption = document.createElement("option");
	    myOption.value = classCode;
	    
	    myOption.text = calssDesc;
	    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
	  }
	}
--></script>
	</head>
	<body onload="doInit()">
	<table border="0"  cellspacing="0" cellpadding="3" class="small">
	  <tr>
	    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22"  align="absmiddle"><span class="big3"> 系统日志查询</span>
	    </td>
	  </tr>
	</table>
	<form name="form1" id="form1"  action ="">
		<table class="TableBlock" width="400" align="center">
		  
		  <tbody id="dataBody"> </tbody>  
		 <!--   <tr>
		    <td nowrap class="TableData">日志表：</td>
		    <td class="TableData">
		      <input type="radio" name="COPY_TIME" value="" id="COPY_TIME" checked>
		      <label for="COPY_TIME_">当前日志</label>&nbsp;
		    </td>
		  </tr>
		-->
		  <tr>
			  <td nowrap class="TableData">日志类型：</td>
			  <td class="TableData">
			    <select name="TYPE" id="TYPE" class="BigSelect">
					  <option value="">所有日志</option>
					
				  </select>
			  </td>
		  </tr>
		  <tr>
		  
		  <td nowrap class="TableData">用户：</td>
		    <td class="TableData">
		      <input type="hidden" name="user" id ="user" value="">
		      <textarea cols=23 name="userDesc" id="userDesc" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
		      <a href="javascript:;" class="orgAdd" onClick="selectUser(['user', 'userDesc']);">添加</a>
		      <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
		    </td>
		  </tr>
		  <tr>
		    <td nowrap class="TableData">起始时间：</td>
		    <td class="TableData">
			    <input type="text" name="statrTime" id="statrTime" size="20" maxlength="20" class="BigInput">
			    <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
			  <!--   <img src="/images/menu/clock.gif" align="absMiddle" border="0" style="cursor:pointer" onclick="td_clock('form1.BEGIN_DATE');">  -->
		    </td>
		  </tr>
		  <tr>
		    <td nowrap class="TableData">截止时间：</td>
			    <td class="TableData"> 
				     <%
				       String time = YHUtility.getCurDateTimeStr();
				     %>
				    <input type="text" id="endTime" name="endTime" size="20" maxlength="20" class="BigInput" value="<%=time%>">
				    <img  id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
				    <!-- <img src="/images/menu/clock.gif" align="absMiddle" border="0" style="cursor:pointer" onclick="td_clock('form1.END_DATE');"> -->
			    </td>
		  </tr>
		  <tr>
		    <td nowrap class="TableData">IP地址：</td>
		    <td class="TableData">
		      <input type="text" name="IP" id="IP" class="BigInput" size="20">
		    </td>
		  </tr>
		  <tr>
		    <td nowrap class="TableData">备注：</td>
		    <td class="TableData">
		      <input type="text" name="REMARK" id="REMARK" class="BigInput" size="20">
		    </td>
		  </tr>
		  <tr>
		    <td nowrap class="TableData">操作类型：</td>
		    <td class="TableData">
			     <input type="radio" name="OPERATION" value="0" id="OP_QUERY" checked><label for="OP_QUERY">查询</label>&nbsp;&nbsp;
			     <input type="radio" name="OPERATION" value="2" id="OP_EXPORT"><label for="OP_EXPORT">导出</label>&nbsp;&nbsp;
			     <!--   <input type="radio" name="OPERATION" value="3" id="OP_IMPORT"><label for="OP_IMPORT">导入</label>  -->
			    <!--  <input type="radio" name="OPERATION" value="1" id="OP_DELETE"><label for="OP_DELETE">删除</label> -->
		    </td>
		  </tr>
			  <tr >
			    <td nowrap class="TableControl" colspan="2" align="center">
				    <input id="queryBtn" type="button"  onclick="CheckForm();" class="BigButton"  value="确定">
				    <!-- <input type="submit" value="确定" class="BigButton" title="" name="button"> -->
			    </td>
			  </tr>
			</table>
		</form>
		<br>
		<table  style="display:none" width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
		  <tr>
		    <td background="/images/dian1.gif" ></td>
		  </tr>
		</table>
	 
		<table  style="display:none" border="0"  cellspacing="0" cellpadding="3" class="small">
		  <tr>
		    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22"  align="absmiddle"><span class="big3"> 系统日志存档</span>
		    </td>
		  </tr>
		</table>
		<table  style="display:none" class="MessageBox" align="center" width="500">
		  <tr>
		    <td class="msg info">
		      <div class="content" style="font-size:12pt">系统日志结转是将现有日志表复制一份，并以当前日期命名，然后清空当前日志表，以提高效率和稳定性。</div>
		    </td>
		  </tr>
		</table>
		<table style="display:none" align="center"><tr ><td >
		<input type="button" value="系统日志存档" class="BigButtonC" onclick="javascript:copy_log();">
		</td></tr></table>
		<br>
		<br>
		<form name="form" id="form">
		<!-- 	<table border="0"  cellspacing="0" cellpadding="3" class="small">
		  <tr>
		    <td class="Big"><img src="<%=imgPath %>/system.gif" WIDTH="22"  align="absmiddle"><span class="big3"> 清空系统日志</span>
		    </td>
		  </tr>
		</table>
	  <br>
	<table align="center" ><tr ><td >
		<input type="button" value="清空系统日志" class="BigButtonC" onclick="javascript:delete_log();">
		</td></tr></table>
		 -->
		 <!--    xin gong neng  
		<div align="center">
		   <input type="button" value="清空系统日志" class="BigButtonC" onclick="ShowDialog('ModalDialog');">
		</div>
		 
		<div id="overlay"></div>
		<div id="ModalDialog" class="ModalDialog" style="width:500px;">
		  <div class="header"><span id="title" class="title">清空系统日志</span><a class="operation" href="javascript:HideDialog('ModalDialog');"><img src="<%=imgPath %>/close.png"/></a></div>
		
		  <div id="body" class="body" style="padding:10px;">
		  
		     <table>
		     <tbody id="dataBodys"> </tbody>
		     </table>
		  </div>
		 
		  <div id="footer" class="footer">
		    <input class="BigButton" onclick="delete_log();" type="button" value="确定"/>&nbsp;&nbsp;
		    <input class="BigButton" onclick="HideDialog('ModalDialog')" type="button" value="取消"/>
		  </div>
		</div>  
		-->
		</form>
		</body>
		</html>