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
<title>团组信息</title>
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
<script type="text/javascript" src="<%=contextPath%>/subsys/jtgwjh/receiveDoc/js/jhDocReceive.js"></script>
<script type="text/javascript" >

function delGroup(seqId){
  msg = "确定要删除所选的记录吗？删除后不可恢复！";
  if(window.confirm(msg)) {
    var requestURL = "<%=contextPath%>/yh/subsys/servicehall/visaformprint/act/YHHPrintGroupAct/delGroupById.act?seqId=" + seqId ; 
    var json=getJsonRs(requestURL); 
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    }
    alert("删除成功！");
    window.location.reload();
  }
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
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/queryGroupManage.act?status=4";
   cfgs = {
    dataAction: url,
    container: "projectDiv",
    paramFunc: getParam,
    afterShow:getTotal,
    colums: [
       {type:"selfdef",name:"email_select", text:"选择",align:"center", width:"3%",render:toCheck},
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"docTitle", text:"文件标题", width: "30%",align:"center"},
       {type:"text", name:"docCode", text:"文号", width: "7%",align:"center"},
       {type:"text", name:"docType", text:"类型", width: "6%",align:"center",render:toType},
       {type:"text", name:"sendDeptName", text:"发送单位", width: "8%",align:"center"},
       {type:"text", name:"sendDatetime", text:"发送时间", width: "8%",align:"center",render:toDate} ,
       {type:"text", name:"returnReason", text:"回退原因", width: "8%",align:"center"}]
    // {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"11%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total>0){

  }else{
    $("projectDiv").style.display = "none";
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件信息!</div></td></tr>"
        );
    $('returnNull').update(table); 
  }
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
      $('returnNull').style.display = "none";
     }else{
       $("projectDiv").style.display = "none";
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
function toOpts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href=\"javascript:print("+seqId+",0);\" >转发</a>&nbsp;"
          +"<a href='javascript:print("+seqId+");' >打印</a>&nbsp;";
}
function print(seqId){
  alert("开发中...");
}


function receiveAll(){
	var idStrs = get_checked().trim(); 
  if(!idStrs) { 
    alert("要签收记录，请至少选择其中一个。"); 
   return; 
  } 
	if(idStrs){
		idStrs = idStrs.substr(0,idStrs.length -1);
	}
	var msg = "确定要签收所选的记录吗？";
  if(window.confirm(msg)) {
    var requestURL = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/updateStatus.act?seqId=" + idStrs + "&status=1" ; 
    var json=getJsonRs(requestURL); 
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    }
    alert("签收成功！");
    window.location.reload();
  }
}


/** 
* 导出数据 
* @return 
*/ 
function   delAll(){ 
  var idStrs = get_checked().trim(); 
  if(!idStrs) { 
    alert("请至少选择其中一个。"); 
   return; 
  } 
  if(idStrs){
    idStrs = idStrs.substr(0,idStrs.length -1);
  }
  

  if(window.confirm("确认要删除所选记录")){ 
    var requestURL = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/deleteByseqIds.act?seqId=" + idStrs  ; 
    var json=getJsonRs(requestURL); 
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    }
    alert("删除成功！");
    window.location.reload();
  } 
}


function doOnload(){
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
  insertTr(table);
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


  mynewcell.colSpan="6";
  mynewcell.innerHTML = "<label for='allbox_for'>全选</label> &nbsp;&nbsp;"
    +"<input type='button' value='删除' title='删除将选记录' class='BigButton' onClick='javascript:delAll();;'>&nbsp;";

}


/*
 * 导出法国名单表

 */
function exportListExcel(){
  var idStrs = get_checked().trim(); 
  if(!idStrs) { 
    alert("要导出记录，请至少选择其中一个。"); 
   return; 
  } 
  if(idStrs){
    idStrs = idStrs.substr(0,idStrs.length -1);
  }
  if(window.confirm("确认要导出所选记录")){ 
    var requestURLStr = contextPath + "/yh/subsys/servicehall/visaformprint/act/YHHPrintGroupAct"; 
    var url = requestURLStr + "/exportListToExcel.act"; 
    location =url +"?seqIdStr=" + idStrs; 
  }
}



</script> 
</head>
<body class="" topmargin="5" onload="doOnload();">
<form action="" id="form1" name="form1">
<table border="0" width="90%" cellspacing="0" cellpadding="3"
  class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif"
      align="middle">&nbsp;<span class="big3">回退公文</span></td>
  </tr>
</table>
<br>
<table class="TableBlock" align="center" >
<tr>
           <td nowrap class="TableContent">文件标题： </td>
      <td class="TableData">
        <input type="text" name="docTitle" id="docTitle" value="" size="25">
 
      </td>
         <td nowrap class="TableContent">文号： </td>
      <td class="TableData">
        <input type="text" name="docNo" id="docNo" value="" size="8">
 
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
<br>
<div id="returnNull"></div>

</body>
 
</thml>