<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>已发发文列表</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/sendLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
  setDate();
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/getListJsonComplete.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    paramFunc: getParam,
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"docTitle",  width: '15%', text:"文件标题" ,align: 'left' ,render: renderDocTitle},
       {type:"data", name:"docType",  width: '8%', text:"文件类型" ,align: 'center' ,render: renderDocType},
       {type:"hidden", name:"urgentType",  width: '8%', text:"紧急程度" ,align: 'center' ,render: renderUrgentType},
       {type:"hidden", name:"securityLevel",  width: '8%', text:"密级" ,align: 'center' ,render: renderSecurityLevel},
       {type:"data", name:"docNo",  width: '10%', text:"文号" ,align: 'center' },
       {type:"hidden", name:"reciveDept",  width: '10%', text:"联网接收单位" ,align: 'center' },
       {type:"data", name:"reciveDeptDept",  width: '15%', text:"联网接收单位" ,align: 'center' ,render: renderReciveDeptDesc},
       {type:"hidden", name:"createDatetime",  width: '10%', text:"创建时间" ,align: 'center' ,render: renderCreateDatetime},
       {type:"data", name:"sendDatetime",  width: '10%', text:"发送时间" ,align: 'center' ,render: renderCreateDatetime},
       {type:"data", name:"status",  width: '10%', text:"发送情况" ,align: 'center' ,render: renderStatus},
       {type:"hidden", name:"tasks", text:"总任务数", dataType:"int"},
       {type:"hidden", name:"sendComplete", text:"发送成功数", dataType:"int"},
       {type:"hidden", name:"complete", text:"完成数", dataType:"int"}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无已发发文信息', 'msrg');
  }
  $('docTitle').focus();
}

function setDate(){
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

function renderStatus(cellData, recordIndex, columIndex){
  var tasks = this.getCellData(recordIndex,"tasks");
  var complete = this.getCellData(recordIndex,"complete");
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<div><a href=javascript:detailSendInfo(" + seqId + ")>"+complete+"/"+tasks+"</a>";
}

/**
 * 发送详细信息
 */
function detailSendInfo(seqId){
  var URL = contextPath + "/subsys/jtgwjh/sendDoc/detailSendInfo.jsp?seqId=" + seqId;
  newWindow(URL,'820', '500');
}

function getGroup(){
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }
  else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $("listContainer").style.display = "";
    $('delOpt').style.display = "";
    $('msrg').style.display = "none";
  }else{
    $('msrg').style.display = "";
    $("listContainer").style.display = "none";
    $('delOpt').style.display = "none";
    WarningMsrg('无已发发文信息', 'msrg');
  }
}

function getParam(){
  var queryParam = $("form1").serialize();
  return queryParam;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absMiddle"><span class="big3">&nbsp;已发发文列表 </span>
   </td>
 </tr>
</table>
<br>

<form action="" id="form1" name="form1">
<table class="TableBlock" align="center" >
  <tr>
    <td nowrap class="TableContent">文件标题： </td>
    <td class="TableData">
      <input type="text" name="docTitle" id="docTitle" value="" size="37">
    </td>
    <td nowrap class="TableContent">文号： </td>
    <td class="TableData">
      <input type="text" name="docNo" id="docNo" value="" size="8">
    </td>
    <td nowrap class="TableContent">文件类型： </td>
    <td class="TableData">
      <select id="docType" name="docType" style="width:71px;">
        <option value=''></option>
        <option value='0'>普通</option>
        <option value='1'>公文</option>
      </select>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent">发送日期： </td>
    <td nowrap class="TableData" >
      <input type="text" name="sendDate" id="sendDate" class="BigInput" size="8" maxlength="10" value="" readonly>&nbsp;
      <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    &nbsp;至&nbsp;
      <input type="text" name="sendDate2" id="sendDate2" class="BigInput" size="8" maxlength="10" value="" readonly>&nbsp;
      <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    </td>
    <td nowrap class="TableContent">紧急程度： </td>
    <td class="TableData">
      <select id="urgentType" name="urgentType" style="width:71px;">
        <option value=''></option>
        <option value='0'>一般</option>
        <option value='1'>紧急</option>
        <option value='2'>特急</option>
      </select>
    </td>
    <td nowrap class="TableContent">密级： </td>
    <td nowrap class="TableData" >
      <select id="securityLevel" name="securityLevel" style="width:71px;">
        <option value=''></option>
        <option value='0'>普通</option>
        <option value='1'>内部</option>
        <option value='2'>秘密</option>
        <option value='3'>机密</option>
      </select>
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

<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
</div>

<div id="msrg">
</div>
</body>
</html>