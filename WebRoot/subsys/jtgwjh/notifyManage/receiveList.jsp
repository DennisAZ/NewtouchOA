<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
String dayTime = sf.format(new Date());
%>
<html>

<head>
<title>待处理文件</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/jtgwjh/notifyManage/js/jhNotifyReceive.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/notifyManage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/notifyManage/js/notifyLogic.js"></script>
<script type="text/javascript" >
function update(seqId){
  var URL = "<%=contextPath%>/subsys/jtgwjh/notifyManage/modify.jsp?seqId="+seqId;
  window.location.href = URL;
}

function checkForm(){
  if($("sendDate").value != '' && !isValidDateStr($("sendDate").value)){
    alert("起始日期格式不对,应形如 2010-02-01");
    document.getElementById("sendDate").focus();
    document.getElementById("sendDate").select();
    return false;
   }
  if($("sendDate2").value != '' && !isValidDateStr($("sendDate2").value)){
    alert("结束日期格式不对,应形如 2010-02-01");
    document.getElementById("sendDate2").focus();
    document.getElementById("sendDate2").select();
    return false;
  }
  return true;
}
var pageMgr = null;
var cfgs = null;

function selectGroupManage(){
  pYxTotal = 0;
  pAllTotal = 0;
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/notifyManage/act/YHJhNotifyInfoAct/queryGroupManage.act";
   cfgs = {
    dataAction: url,
    container: "projectDiv",
    paramFunc: getParam,
    afterShow:getTotal,
    colums: [
	   {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"title", text:"公告标题", width: "25%",align:"left",render:toNotifyTitle},
       {type:"text", name:"sendDeptName", text:"发送单位", width: "8%",align:"center"},
       {type:"text", name:"sendDatetime", text:"发送时间", width: "8%",align:"center",render:toDate},
       {type:"selfdef", text:"操作", width: '15%',render:opts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
      $("projectDiv").style.display = "";
      $('delOpt').style.display = "";
      $('returnNull').style.display = "none";
  }else{
    $("projectDiv").style.display = "none";
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件信息!</div></td></tr>"
        );
    $('returnNull').update(table); 
  }
}

function opts(cellData, recordIndex, columIndex){
	  var seqId = this.getCellData(recordIndex,"seqId");
	  return "<center>"
	        + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>&nbsp;"
	        + "</center>";
	}
function getGroup(){
  if(checkForm()){
    if(!pageMgr){
      pageMgr = new YHJsPage(cfgs);
      pageMgr.show();
    }else{
      pageMgr.search();
    }
    var total = pageMgr.pageInfo.totalRecord;
    if(total>0){
      $("projectDiv").style.display = "";
      $('delOpt').style.display = "";
      $('returnNull').style.display = "none";
     }else{
       $("projectDiv").style.display = "none";
       $('delOpt').style.display = "none";
       var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
           + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件信息!</div></td></tr>"
           );
       $('returnNull').style.display = "";
       $('returnNull').update(table); 
     }
  }
}
function getParam(){
  var queryParam = $("form1").serialize();
  return queryParam;
}
function qq(cellData, recordIndex, columInde){
  alert("开发中...");
}


function doOnload(){
	selectGroupManage();
  if(checkForm()){
    selectGroupManage();
  }
  //时间
  var parameters = {
      inputId:'sendDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);

  var parameters = {
      inputId:'sendDate2',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
}
function getTotal(){
  var table = pageMgr.getDataTableDom();
 // insertTr(table);
}
function insertTr(table){
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行


  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列


  mynewcell.align='center';
  mynewcell.innerHTML = " <input type='checkbox' name='allbox' id='allbox_for' onClick='check_all();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列


  mynewcell.colSpan="5";
  mynewcell.innerHTML = "<label for='allbox_for'>全选</label> &nbsp;&nbsp;"
    +"<input type='button' value='签收' title='签收所选记录' class='BigButton' onClick='javascript:receiveAll();;'>&nbsp;";
}


</script> 
</head>
<body class="" topmargin="5" onload="doOnload();">
<form action="" id="form1" name="form1">
<table border="0" width="90%" cellspacing="0" cellpadding="3"
  class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif"
      align="middle">&nbsp;<span class="big3">已收公告列表</span></td>
  </tr>
</table>
<br>
<table class="TableBlock" align="center" >
<tr>
      <td nowrap class="TableContent">标题公告： </td>
      <td class="TableData">
        <input type="text" name="title" id="title" value="" size="25">
      </td>
     <td nowrap class="TableContent">发送日期： </td>
    <td nowrap class="TableData" >
        <input type="text" name="sendDate" id="sendDate" class="BigInput" size="8" maxlength="10" value="" >&nbsp;
        <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    &nbsp;至&nbsp;
       <input type="text" name="sendDate2" id="sendDate2" class="BigInput" size="8" maxlength="10" value="" >&nbsp;
       <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
        
        
    </td>
   </tr>
   
   <tr class="TableControl">
   	<td colspan="6" align="center" >  &nbsp;   &nbsp;  &nbsp;
   		<input type="button"  value="查询" class="BigButton" onClick="getGroup();"> &nbsp;  &nbsp; &nbsp;
   		<input type="button"  value="刷新" class="BigButton" onClick="location.reload();">
   	</td>
   </tr>
 </table>
 </form>
 <br>
<div id="projectDiv"></div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;
         <a href="javascript:deleteAll();" title="删除所选记录"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选记录</a>&nbsp;
      </td>
 </tr>
</table>
</div>
<div id="returnNull"></div>

</body>
 
</html>