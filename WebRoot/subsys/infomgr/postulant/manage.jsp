<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>团队志愿者报名管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href="<%=cssPath%>/style.css"/>
<style>
.TableList {
  border:0 solid #3063A8;
  font-size:9pt;
  line-height:25px;
}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var pageMgr;
var flag = ${param.flag};
function doInit(){
  var requestURL = "<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHPostulantAct/getPage.act?flag=" + flag;

  var opts;

  if (flag == 0){
    opts = [
      {clickFunc:useRecord, text:"录用"},
      {clickFunc:unuseRecord, text:"暂不录用"},
      {clickFunc:updateRecord, text:"查看/修改"},
      {clickFunc:deleteRecord, text:"删除 "}
    ];
  }
  else {
    opts = [
      {clickFunc:updateRecord, text:"查看/修改"},
      {clickFunc:deleteRecord, text:"删除 "}
    ];
  }

  var cfgs = {
    dataAction: requestURL,
    container: "listDiv",
    colums: [
       {type:"hidden", name:"seqId", text:"ID", width:100},
       {type:"hidden", name:"postulantId", text:"IDs", width:100},
       {type:"text", name:"name", text:"团体名称", width:120,align:'center'},
       {type:"text", name:"amount", text:"人数", width:50,align:'center'},
       {type:"text", name:"languageKind", text:"外语语种", width:80,align:'center'},
       {type:"text", name:"languageLevel", text:"外语水平", width:80,align:'center'},
       {type:"text", name:"principal", text:"负责人", width:80,align:'center'},
       {type:"text", name:"principalDuty", text:"负责人职务", width:80,align:'center'},
       {type:"text", name:"principalUnit", text:"负责人单位", width:120,align:'center'},
       {type:"text", name:"principalTel", text:"联系电话", width:85,align:'center'},
       {type:"text", name:"principalContact", text:"紧急联系方式", width:100,align:'center'},
       {type:"text", name:"services", text:"可提供服务", width:100,align:'center',render:servicesRender},
       {type:"text", name:"serveTimeWeekDay", text:"工作日服务时间", width:110,align:'center',render:serveTimeRender},
       {type:"text", name:"serveTimeWeekEnd", text:"周末服务时间", width:100,align:'center',render:serveTimeEndRender},
       {type:"text", name:"recordSource", text:"信息来源", width:80,align:'center'},
       {type:"opts", width: 200, align:'center',opts: opts
       }]
  };
  
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  calendarInit();
}


/**
 * 初始化时间组件
 */
function calendarInit(){
  var beginParameters = {
      inputId:'correctDate',
      property:{isHaveTime:false},
      bindToBtn:'correctDateImg'
  };
  new Calendar(beginParameters);
}

function serveTimeRender(cellData, recordIndex, columInde){
  var serveTimeWeekday = this.getCellData(recordIndex,"serveTimeWeekDay");
  if(serveTimeWeekday.substr(1, serveTimeWeekday.length) == "0"){
    return "9:00-12:00";
  }else if(serveTimeWeekday.substr(1, serveTimeWeekday.length) == "1"){
    return "14:00-17:00";
  }else if(serveTimeWeekday.substr(1, serveTimeWeekday.length) == "2"){
    return "9:00-17:00";
  }else{
    return "";
  }
}

function serveTimeEndRender(cellData, recordIndex, columInde){
  var serveTimeWeekEnd = this.getCellData(recordIndex,"serveTimeWeekEnd");
  if(serveTimeWeekEnd.substr(1, serveTimeWeekEnd.length) == "0"){
    return "9:00-12:00";
  }else if(serveTimeWeekEnd.substr(1, serveTimeWeekEnd.length) == "1"){
    return "14:00-17:00";
  }else if(serveTimeWeekEnd.substr(1, serveTimeWeekEnd.length) == "2"){
    return "9:00-17:00";
  }else{
    return "";
  }
}

function updateRecord(recordIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var postulantId = this.getCellData(recordIndex,"postulantId");
  var url = "<%=contextPath %>/subsys/infomgr/postulant/edit.jsp?flag=${param.flag}&seqId=" + seqId +"&postulantId=" + postulantId;
  window.location.href = url;
}

function useRecord(recordIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  updateFlag(1, seqId);
}

function unuseRecord(recordIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  updateFlag(2, seqId);
}

function deleteRecord(recordIndex){

  if (!confirm("确定要删除本条记录吗?"))
    return;

  var seqId = this.getCellData(recordIndex,"seqId");
  var delJson = getJsonRs("<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHPostulantAct/delete.act?seqId=" + seqId);

  if (delJson.rtState == "0") {
    this.refreshAll();
  }else {
    alert(delJson.rtMsrg);
  }
}

var servicesArray = ['公共英语标识纠错','行业/社区外语服务','全市大型外语活动','其他服务'];

function servicesRender(cellData, recordIndex, columInde) {
  var data = '';
  if (cellData) {
    cellData.split(",").each(function(e, i){
      switch (e) {
      case '1': data += '公共英语标识纠错,'; break;
      case '2': data += '行业/社区外语服务,'; break;
      case '3': data += '全市大型外语活动,'; break;
      case '4': data += '其他服务,'; break;
      default: ;
      }
    });
  }

  return data;
}

function updateFlag(flag, seqId){
  var delJson = getJsonRs("<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHPostulantAct/updateFlag.act?seqId=" + seqId + "&flag=" + flag);

  if (delJson.rtState == "0") {
    pageMgr.refreshAll();
  }else {
    alert(delJson.rtMsrg);
  }
}

function search(){

  $('services').value = '';
  ['services1','services2','services3','services4'].each(function(e, i){
    if ($(e).checked){
      $('services').value += (( i + 1 ) + ',');
    }
  });
  
  para = "&name=" + encodeURI(encodeURI($('name').value)) +
    "&principal=" + encodeURI(encodeURI($('principal').value)) +
    "&services=" + $('services').value +
    "&languageKind=" + encodeURI(encodeURI($('languageKind').value)) +
    "&servicesOther=" + encodeURI(encodeURI($('servicesOther').value)) +
    "&languageLevel=" + encodeURI(encodeURI($('languageLevel').value));
  pageMgr.dataAction = "<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHPostulantAct/getPage.act?flag=" + flag + para;
  pageMgr.search();
}

function showServiceOther(t){
  if (t.checked){
    $('servicesOther').show();
    $('servicesOther').select();
  }
  else {
    $('servicesOther').hide();
    $('servicesOther').value = '';
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()" onunload="hideWindow()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;团队志愿者报名管理</span>&nbsp;
    </td>
  </tr>
</table>
<table style="width:400px;" class="TableList" align="center" >
  <tr>
    <td nowrap class="TableData" width=100> 志愿者团体名称：</td>
    <td nowrap class="TableData" width=220>
      <input type="text" class="BigInput" size=30  name="name" id="name" value="">
    </td>
    <td nowrap class="TableData" width=100> 团体负责人姓名：</td>
    <td nowrap class="TableData" width=220>
      <input type="text" class="BigInput" size=30  name="principal" id="principal" value="">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData" width=100> 外语语种：</td>
    <td nowrap class="TableData" width=220>
      <input type="text" class="BigInput" size=30  name="languageKind" id="languageKind" value="">
    </td>
    <td nowrap class="TableData" width=100> 外语水平：</td>
    <td nowrap class="TableData" width=220>
      <input type="text" class="BigInput" size=30  name="languageLevel" id="languageLevel" value="">
    </td>
  </tr>
  <tr>
    <td class="TableData">能提供的服务项目</td>
    <td class="TableData" colspan="3">
    <label for="services1"><input type="checkbox" id="services1" />
    1:公共英语标识纠错</label>
    <label for="services2"><input type="checkbox" id="services2" />
    2:行业/社区外语服务</label>
    <label for="services3"><input type="checkbox" id="services3" />
    3:全市大型外语活动</label>
    <p><label for="services4">
    <input type="checkbox" id="services4" onclick="showServiceOther(this)" />
    4:其他请说明</label>
    <input name="services" id="services" type="hidden"/>
    <input name="servicesOther" id="servicesOther" style="display:none;" type="text" class="BigInput" size="21" />
    </p></td>
  </tr>
  <tr class="TableControl" align="center" >
     <td nowrap  colspan="4">
       <input type="button" value="查询" class="BigButton" onclick="search()"/>
     </td>
   </tr>
</table>
 
<table border="0" width="400px" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;团队志愿者报名列表</span>&nbsp;
    </td>
  </tr>
</table>
<div id="msrg" align="center"></div>
<div id="controlDiv" align="center"></div>
<div id="listDiv" align="center"></div>
</body>
</html>