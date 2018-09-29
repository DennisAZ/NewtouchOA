<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
 String type = request.getParameter("type");
 String sendTime = request.getParameter("sendTime");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>按类型显示公告通知</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>

<script type="text/javascript">
var sendTime = "<%=sendTime%>";
var type = "<%=type%>";
var field = 'SEND_TIME'; 
var ascDesc = '1';
var pageCount = 0;
function doInit(){
  var beginParameters = {
    inputId:'sendTime',
    property:{isHaveTime:false},
    bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);      
  loadType();
  var index = $('pageIndex').value;
  if (index > 1) {
    $('pageIndex').value = 1;
  }
  if(type != 'null'){
    $("typeId").value = type;
  }
  loadData($F('pageIndex'), $F('pageLen'),$("selTypeId").value,ascDesc,field);
}

function checkShunXu() {
  if(field=='SUBJECT'){
    if(ascDesc=='0') {
       $('subject').update("<u>标题</u><img border=0 src=\"<%=imgPath%>/arrow_up.gif\" width=\"11\" height=\"10\">");
       $('sendTimetemp').update("<u>发布时间</u>");
    }else {
      $('subject').update("<u>标题</u><img border=0 src=\"<%=imgPath%>/arrow_down.gif\" width=\"11\" height=\"10\">"); 
      $('sendTimetemp').update("<u>发布时间</u>");
    }
  }else if(field=='SEND_TIME') {
    if (ascDesc == '0') {
            $('sendTimetemp').update("<u>发布时间</u><img border=0 src=\"<%=imgPath%>/arrow_up.gif\" width=\"11\" height=\"10\">");
            $('subject').update("<u>标题</u>");
    }else {
      $('sendTimetemp').update("<u>发布时间</u><img border=0 src=\"<%=imgPath%>/arrow_down.gif\" width=\"11\" height=\"10\">"); 
      $('subject').update("<u>标题</u>");
    }
  }
}

function loadType(){
  var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/getnotifyType.act';
  var json = getJsonRs(url);
  if(json.rtState == "0") {
    var rtData = json.rtData;
    var typeData = rtData.typeData;
    $("typeId").options.length=0; 
    $("typeId").options.add(new Option("所有类型","0"));
    if(typeData.length > 0) {
      for(var i = 0 ;i < typeData.length ;i ++){
        var optionStr = typeData[i];
        $("typeId").options.add(new Option(optionStr.typeDesc,optionStr.typeId)); 
      }
    }
    $("typeId").options.add(new Option("无类型","")); 
  }
  if($("selTypeId").value == ""){
    $("typeId").options[$("typeId").options.length-1].selected='selected';
  }else {
    $("typeId").value = $("selTypeId").value;
  }
}

function refush(){
  if (!trim($F('pageIndex'))) {
    alert('请输入有效的页码!');
    $('pageIndex').value = "";
    $('pageIndex').focus();
    return;
  }
  if ($F('pageIndex') > pageCount){
    alert('请输入有效的页码!');
    $('pageIndex').focus();
    $('pageIndex').select();
    return;
  }
  if ($F('pageIndex') == 0){
    alert('请输入有效的页码!');
    $('pageIndex').focus();
    $('pageIndex').select();
    return;
  }
  loadData($F('pageIndex') , $F('pageLen'),$('typeId').value,ascDesc,field);
} 

function checkPage(){
  var nm = $("pageIndex").value;
  if(!isNumber(nm)){
    alert("请输入整数！"); 
    $("pageIndex").focus();
    $("pageIndex").select();
    return false; 
  }
}

function selChange(pageIndex , showLength ,typeId ,  field){
  ascDesc ='1';
  $("selTypeId").value = typeId;
  if(pageIndex > 1){
    pageIndex = 1;
    $("pageIndex").value = '1';
  }
  loadData(pageIndex , showLength ,typeId , ascDesc, field);
}

function loadData(pageIndex , showLength , typeId , ascDesc , field){
  checkShunXu();
  var index = $('pageIndex').value;
  if(!index.trim()){
    $('pageIndex').value = '1';
    pageIndex = 1;
  }
  var par = "pageIndex="+pageIndex+"&showLength=" +showLength+"&type="+typeId+"&ascDesc="+ascDesc+"&field="+field;
  if (type) {
    par = par + "&type="+type;
  }
  if(sendTime){
    par = par + "&sendTime="+sendTime;
  }
  var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyShowAct/getnotifyShowList.act';
  var json = getJsonRs(url , par);
  if (json.rtState == "0") {
    var rtData = json.rtData;
    var pageData = rtData.pageData;
    var listData = rtData.listData;
    pageCount = pageData.pageCount;
    var recordCount = pageData.recordCount;
    var pgStartRecord = pageData.pgStartRecord;
    var pgEndRecord = pageData.pgEndRecord;
    $('pageCount').innerHTML = pageCount;
    if(pageIndex == pageCount){
      $('pgNext').className = "pgBtn pgNext pgNextDisabled";
      $('pgLast').className = "pgBtn pgLast pgLastDisabled";
    }else{
      $('pgNext').className = "pgBtn pgNext pgNext";
      $('pgLast').className = "pgBtn pgLast pgLast";
    }
    if(pageIndex == 1){
      $('pgPrev').className = "pgBtn pgPrev pgPrevDisabled";
      $('pgFirst').className = "pgBtn pgFirst pgFirstDisabled";
    }else{
      $('pgPrev').className = "pgBtn  pgPrev pgPrev";
      $('pgFirst').className = "pgBtn pgFirst pgFirst";
    }
    addEvent( pageIndex , pageCount,typeId);
    removeAllChildren($('dataBody'));
    if(listData.length > 0){
      for(var i = 0 ;i < listData.length ;i ++){
        var data = listData[i];
        addRow(data, i);
      }
      $('hasData').show();
      $('pagebar').show();
      $('noData').hide();
    }else {
      $('hasData').hide();
      $('pagebar').hide();
      $('noData').show();
    }
    $('freshLoad').className = "pgBtn pgRefresh"; 
  }else {
    $('hasData').hide();
    $('pagebar').hide();
    $('noData').show();
    $('msgInfo').update(json.rtMsrg);
  } 
}
function addEvent(index,pageCount,type){
  var pageLen = $F('pageLen');
  var pageIndex = parseInt(index);
  if (pageIndex == pageCount) {
    $('pgNext').onclick = function(){};
    $('pgLast').onclick = function(){};
    $('pgPrev').onclick = function() {
      $('pageIndex').value = pageIndex - 1;
      loadData(pageIndex - 1 , pageLen ,type,ascDesc,'SEQ_ID');};
    $('pgFirst').onclick = function(){
      $('pageIndex').value = 1;
      loadData('1' , pageLen ,type,ascDesc,'SEQ_ID');};
  }else if(pageIndex == 1){
    $('pgPrev').onclick = function(){};
    $('pgFirst').onclick = function(){};
    $('pgNext').onclick = function(){
      if ($F('pageIndex') > pageCount){
        alert('请输入有效的页码!');
        return;
      }
      $('pageIndex').value = pageIndex + 1;
      loadData(pageIndex + 1 ,pageLen ,type,ascDesc,'SEQ_ID');};
    $('pgLast').onclick = function(){
      $('pageIndex').value = pageCount;
      loadData(pageCount , pageLen ,type,ascDesc,'SEQ_ID');};
  }else{
    if(pageIndex < 1) {
      pageIndex = 1;
      $("pageIndex").value = pageIndex;
      $('pgNext').className = "pgBtn pgNext pgNextDisabled";
      $('pgLast').className = "pgBtn pgLast pgLastDisabled";
      $('pgPrev').className = "pgBtn pgPrev pgPrevDisabled";
      $('pgFirst').className = "pgBtn pgFirst pgFirstDisabled";
    }else {
      $('pgNext').onclick = function(){
        if($F('pageIndex') > pageCount) {
          alert('请输入有效的页码!');
          return;
        }
        $('pageIndex').value = pageIndex + 1;
        loadData(pageIndex + 1 , pageLen ,type,ascDesc,'SEQ_ID');};
      $('pgLast').onclick = function() {
        $('pageIndex').value = pageCount;
        loadData(pageCount ,pageLen ,type,ascDesc,'SEQ_ID');};
      $('pgPrev').onclick = function(){
        $('pageIndex').value = pageIndex - 1;
        loadData(pageIndex - 1 , pageLen ,type,ascDesc,'SEQ_ID');};
      $('pgFirst').onclick = function(){
        $('pageIndex').value = 1;
        loadData(1 , pageLen ,type,ascDesc,'SEQ_ID');};
    }
  }
}
function addRow(data , i){
  var toNameTitle = data.toNameTitle;
  var toNameStr = data.toNameStr;
  var privNameTitle = data.privNameTitle;
  var privNameStr = data.privNameStr;
  var userNameTitle = data.userNameTitle;
  var userNameStr = data.userNameStr;
  var title = toNameTitle + privNameTitle + userNameTitle;
  var str = toNameStr + privNameStr + userNameStr;
  var td =  "<td align='center'><u title='部门：" + data.deptName + "' style='cursor:pointer'>" + data.fromName + "</u></td>"
    + " <td align='center'>"+ data.typeName +"</td>"
    + " <td nowrap>";
  if (data.iread == 'no') {
        td += "<img src='<%=imgPath%>/systop/mail.png' align='absMiddle' border='0'>";
  }
	td += "<a href=javascript:open_notify('" + data.seqId + "','" + data.format + "'); title='" + data.subjectTitle + "'>" + data.subject;

  td += "</a></td>";
  td += "<td style='cursor:pointer' title='"+title+"'>" + str +"</td>";
  td += " <td nowrap align='center'>" + data.sendTime.substring(0,19) +"</td>";
  var noread = "";
  if (data.iread == 'no') {
	noread = " newInfo";
  }
  var className = "TableLine2" ;    
  if(i%2 == 0){
    className = "TableLine1" ;
  }
  var tr = new Element("tr" , {"class" : className + noread}).update(td);
  $('dataBody').appendChild(tr);  
}

function read_all() {
  msg='确认要标记所有公告为已读吗？';
  if (window.confirm(msg)) {
    url="/yh/yh/core/funcs/notify/act/YHNotifyShowAct/changeNoReadAll.act?forward=all";
    window.location = url;
  }
}

function removeAllChildren(parentNode){
  parentNode = $(parentNode);
  while(parentNode.firstChild) {
    var oldNode = parentNode.removeChild(parentNode.firstChild);
    oldNode = null;
  }
}

function open_notify(seqId,format) {
  URL="/yh/core/funcs/notify/show/readNotify.jsp?isManage=0&seqId="+seqId;
  myleft=(screen.availWidth-780)/2;
  mytop=100
  mywidth=780;
  myheight=500;
  if(format=="1") {
    myleft=0;
    mytop=0
    mywidth=screen.availWidth-15;
    myheight=screen.availHeight-60;
  }
  window.open(URL,"公告通知","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}


function show_toobject(seqId) {
  URL="/yh/core/funcs/notify/manage/showObject.jsp?seqId="+seqId;
  myleft=(screen.availWidth-500)/2;
  window.open(URL,"read_notify","height=250,width=600,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}

function my_affair(seqId) {
  myleft=(screen.availWidth-250)/2;
  mytop=(screen.availHeight-200)/2;
  URL="/yh/core/funcs/notify/manage/note.jsp?seqId="+seqId;
  window.open(URL,"","height=200,width=250,status=0,toolbar=no,menubar=no,location=no,scrollbars=auto,resizable=no,top="+mytop+",left="+myleft);
}

function show_reader(seqId) {
  URL="/yh/core/funcs/notify/show/showReader.jsp?seqId="+seqId;
  myleft=(screen.availWidth-500)/2;
  window.open(URL,"read_notify","height=500,width=700,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}

function change_time() {
  type = document.getElementById("typeId").value;
  sendTime = document.getElementById("sendTime").value;
  if(sendTime == "选择发布日期"){
    sendTime = '';
  }
  var regex = /^((\d{2}(([02468][048])|([13579][26]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|([1-2][0-9])))))|(\d{2}(([02468][1235679])|([13579][01345789]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))/; //日期部分
  if(sendTime != "") {
    if (!regex.test(sendTime)&&sendTime!="选择发布日期") {
      alert("输入的日期格式错误");
      return false;
    }
  }
  doInit();
}

function order_by(fieldtemp) {
  if(field==fieldtemp) {
    if(ascDesc == '1'){
      ascDesc = '0';
    }else{
      ascDesc = '1';
    }
  }else {
    field = fieldtemp;
    ascDesc = '1';
  }
  doInit();
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">
       <img src="<%=imgPath%>/notify_open.gif" WIDTH="22" HEIGHT="20" align="absmiddle">
       <span class="big3">公告通知</span>
       <select id="typeId" name="typeId" class="BigSelect" onChange="javascript:selChange($F('pageIndex') , $F('pageLen'),$('typeId').value,field);">
       </select>
       <input type="text" id = "sendTime" name="sendTime" class="BigInput" size="10" maxlength="10" value="" title="选择发布日期">
        <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
        <input type="button" class=BigButton value="确定" onClick="javascript:change_time();">
    </td>
    <td>
<div id="pagebar">
<div class="pgPanel">
<div>每页<select id="pageLen"  onchange="javascript:doInit();">
<option value="5"  selected>5</option>
<option value="10">10</option>
    <option value="15">15</option>
    <option value="20">20</option>
</select>条</div>
 <div class="separator"></div>
 <div id="pgFirst" title="" class="pgBtn pgFirst pgFirstDisabled">
 </div>
 <div id="pgPrev" title="" class="pgBtn pgPrev pgPrevDisabled">
 </div><div class="separator">
 </div><div>第 
 <input onblur="checkPage();" onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" id="pageIndex" type="text" title="" value="1" size="5" class="SmallInput pgCurrentPage"> 页 / 共  <span id="pageCount" class="pgTotalPage"></span> 页</div>
  <div class="separator"></div>
  <div title="下页" id="pgNext" class="pgBtn pgNext pgNextDisabled">
  </div>
  <div title="" id="pgLast" class="pgBtn pgLast pgLastDisabled">
  </div><div class="separator">
  </div><div id="freshLoad" title="刷新" class="pgBtn pgRefresh" onclick="javascript:refush();">
  </div></div>
    </td>
  </tr>
</table>
<div id="hasData">
<table class="TableList" width="100%">
 <tr class="TableHeader">
      <td nowrap align="center">发布人</td>
      <td nowrap align="center">类型</td>
      <td nowrap align="center" id="subject" name="subject" onClick="javascript:order_by('SUBJECT');" style="cursor:pointer;"><u>标题</u></td>
      <td nowrap align="center">发布范围</td>
      <td nowrap align="center" id="sendTimetemp" name="sendTimetemp" onClick="javascript:order_by('SEND_TIME');" style="cursor:pointer;"><u>发布时间</u></td>
    </tr>
   <tbody id="dataBody"></tbody>
</table>
<br>
<table class="TableBlock" width="100%" align="center">
  <tr>
      <td class="TableContent" nowrap align="center" width="80"><b>快捷操作：</b></td>
      <td class="TableControl" nowrap>&nbsp;
   <a href="javascript:read_all();" title="标记所有公告为已读"><img src="<%=imgPath%>/email_open.gif" align="absMiddle" border="0"> 标记所有公告通知为已读</a>&nbsp;&nbsp;
   <!--<a href="javascript:order_by('SEND_TIME')" title="按时间排序"><img src="<%=imgPath%>/email_open.gif" align="absMiddle" border="0"> 按时间排序</a>&nbsp;&nbsp;
   <a href="javascript:" title="按置顶排序"><img src="<%=imgPath%>/email_open.gif" align="absMiddle" border="0"> 按置顶排序</a>-->&nbsp;&nbsp;
      </td>
</tr>
</table>
</div>
<div id="noData" align=center style="display:none">
   <table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="msgInfo" class="msg info">无公告通知
            </td>
        </tr>
    </tbody>
</table>
</div>
<input type="hidden"  id="selTypeId"  value="0"/>
<script>
function bindValidDtFunc1() {
  bindAssertDateTimePrcBatch([{id:"sendTime", type:"d"}]);
}
bindValidDtFunc1();
</script>
</body>
</html>