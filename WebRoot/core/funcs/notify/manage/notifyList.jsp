<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公告管理</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>

<script type="text/javascript">
 var field = 'SEND_TIME'; 
 var ascDesc = '1';
 var pageCount = 0;
function doInit(){
    var index = $('pageIndex').value;
    if(index > 1){
      $('pageIndex').value =1;
    }
    checkShunXu();
	  loadType();	  
	  loadData($F('pageIndex') , $F('pageLen'),$("selTypeId").value,ascDesc,field);
}
function loadType(){
	var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/getnotifyType.act';
	var json = getJsonRs(url);
	  if(json.rtState == "0"){
		  var rtData = json.rtData;
		  var typeData = rtData.typeData;
		  $("typeId").options.length=0; 
		    $("typeId").options.add(new Option("所有类型","0"));
		    if(typeData.length > 0){
		    	for(var i = 0 ;i < typeData.length ;i ++){
		            var optionStr = typeData[i];
		            $("typeId").options.add(new Option(optionStr.typeDesc,optionStr.typeId)); 
		          }
			    }
		    $("typeId").options.add(new Option("无类型","")); 
	}
	  if($("selTypeId").value==""){
		  $("typeId").options[$("typeId").options.length-1].selected='selected';
		}else{
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


function loadData(pageIndex , showLength , typeId , ascDesc , field){
      checkShunXu();
//$('freshLoad').className = "pgBtn pgLoad";
	  //$('pageIndex').value = pageIndex;
//	  $('pgSearchInfo').innerHTML = "加载数据中,请稍后.....";
      var index = $('pageIndex').value;
      if(!index){
        $('pageIndex').value = '1';
        pageIndex = 1;
      }
	  var par = "pageIndex="+pageIndex+"&showLength=" +showLength+"&type="+typeId+"&ascDesc="+ascDesc+"&field="+field;
	  var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/getnotifyManagerList.act';
	  var json = getJsonRs(url , par);
	  
	  if(json.rtState == "0"){
	    var rtData = json.rtData;
	    var pageData = rtData.pageData;
	    var listData = rtData.listData;
	    pageCount = pageData.pageCount;
	    var recordCount = pageData.recordCount;
	    var pgStartRecord = pageData.pgStartRecord;
	    var pgEndRecord = pageData.pgEndRecord;
//	    var pgSearchInfo = "共&nbsp;"+ recordCount +"&nbsp;条记录，显示第&nbsp;<span class=\"pgStartRecord\">"+pgStartRecord+"</span>&nbsp;条&nbsp;-&nbsp;第&nbsp;<span class=\"pgEndRecord\">"+pgEndRecord+"</span>&nbsp;条记录";
//	    $('pgSearchInfo').innerHTML = pgSearchInfo;
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
	        addRow(data, i,pageIndex,showLength);
	      }
	      $('hasData').show();
	      $('noData').hide();
	      $('pagebar').style.display = "";
	    }else{
	      $('hasData').hide();
	      $('noData').show();
	      $('pagebar').style.display = "none";
	      $('msgInfo').update('无公告数据');
	    }
	    
	    $('freshLoad').className = "pgBtn pgRefresh"; 
	  }else{
	    document.body.innerHTML = json.rtMsrg;
	  } 
	}
function addEvent(index,pageCount,type){
	  var pageLen = $F('pageLen');
	  var pageIndex = parseInt(index);
	  if(pageIndex == pageCount){
	    $('pgNext').onclick = function(){};
	    $('pgLast').onclick = function(){};
	    $('pgPrev').onclick = function(){
	      $('pageIndex').value = pageIndex - 1;
	      loadData(pageIndex - 1 , pageLen ,type,ascDesc,field);};
	    $('pgFirst').onclick = function(){
	      $('pageIndex').value = 1;
	      loadData('1' , pageLen ,type,ascDesc,field);};
	  }else if(pageIndex == 1){
	    $('pgPrev').onclick = function(){};
	    $('pgFirst').onclick = function(){};
	    $('pgNext').onclick = function(){
		    if($F('pageIndex')>pageCount){
               alert('请输入有效的页码!');
               return;
			}
	      $('pageIndex').value = pageIndex + 1;
	      loadData(pageIndex + 1 ,pageLen ,type,ascDesc,field);};
	    $('pgLast').onclick = function(){
	      $('pageIndex').value = pageCount;
	      loadData(pageCount , pageLen ,type,ascDesc,field);};
	  }else{
		  if(pageIndex < 1){
			  pageIndex = 1;
			  $("pageIndex").value = pageIndex;
			  $('pgNext').className = "pgBtn pgNext pgNextDisabled";
			  $('pgLast').className = "pgBtn pgLast pgLastDisabled";
			  $('pgPrev').className = "pgBtn pgPrev pgPrevDisabled";
		    $('pgFirst').className = "pgBtn pgFirst pgFirstDisabled";
			}else{
	    $('pgNext').onclick = function(){
	    	if($F('pageIndex')>pageCount){
	               alert('请输入有效的页码!');
	               return;
			}
	      $('pageIndex').value = pageIndex + 1;
	      loadData(pageIndex + 1 , pageLen ,type,ascDesc,field);};
	    $('pgLast').onclick = function(){
	      $('pageIndex').value = pageCount;
	      loadData(pageCount ,pageLen ,type,ascDesc,field);};
	    $('pgPrev').onclick = function(){
	      $('pageIndex').value = pageIndex - 1;
	      loadData(pageIndex - 1 , pageLen ,type,ascDesc,field);};
	    $('pgFirst').onclick = function(){
	      $('pageIndex').value = 1;
	      loadData(1 , pageLen ,type,ascDesc,field);};
	  }
	  }
	}

function removeAllChildren(parentNode){
	  parentNode = $(parentNode);
	  while(parentNode.firstChild){
	    var oldNode = parentNode.removeChild(parentNode.firstChild);
	    oldNode = null;
	  }
	}

function addRow(data , i,pageIndex,showLength){
      var toNameTitle = data.toNameTitle;
      var toNameStr = data.toNameStr;
      var privNameTitle = data.privNameTitle;
      var privNameStr = data.privNameStr;
      var userNameTitle = data.userNameTitle;
      var userNameStr = data.userNameStr;
      var title = toNameTitle + privNameTitle + userNameTitle;
      var str = toNameStr + privNameStr + userNameStr;      
	  var td = "<td>&nbsp;<input type='checkbox' name='deleteFlag' value='" + data.seqId +"'>"+"</td>"
	            + "<td nowrap align='center'><u title='部门:" + data.deptName + "' style='cursor:pointer'>"+data.fromName+"</u></td>"
	            + "<td nowrap align='center'>" + data.typeName +"</td>"
	            + "<td style='cursor:pointer' title='" + title  +"' onClick='show_toobject(" + data.seqId + ");'>" +str + "</td>"
	            + "<td><a href=javascript:open_notify('" + data.seqId + "','" + data.format + "'); title='" + data.subjectTitle + "'>" +data.subject + "</a></td>"
	            + " <td align='center'>"+ data.sendTime.substring(0,19) +"</td>"
	            + " <td nowrap align='center'>" + data.beginDate +"</td>"
	            + "<td nowrap align='center'>" + data.endDate +"</td>";
	  var notifyStatusStr = data.notifyStatusStr;
	  if(notifyStatusStr=="") {
		td = td + "<td nowrap align='center'>" + data.publishDesc + "</td>";
	  }else {
        td = td + "<td nowrap align='center'>" + notifyStatusStr + "</td>";
	  }
	  td = td + "<td><a href=javascript:show_reader('" + data.seqId + "'); title='查阅情况'> 查阅情况</a>&nbsp;<a href='/yh/core/funcs/notify/manage/notifyAdd.jsp?seqId=" + data.seqId + "'> 修改</a>";
      var notifyStatus = data.notifyStatus;
      var publish = data.publish;
      if(notifyStatus=="1"&&publish=="1"){
          td = td + "&nbsp;<a href='/yh/yh/core/funcs/notify/act/YHNotifyHandleAct/changeState.act?seqId="+data.seqId+"&pageIndex="+pageIndex+"&showLength="+showLength+"&operation=1'> 立即生效</a>"
      }else if(notifyStatus=="2"&&publish=="1"){
    	  td = td + "&nbsp;<a href='/yh/yh/core/funcs/notify/act/YHNotifyHandleAct/changeState.act?seqId="+data.seqId+"&pageIndex="+pageIndex+"&showLength="+showLength+"&operation=2'>终止</a>"
      }else if(notifyStatus=="3"&&publish=="1"){
    	  td = td + "&nbsp;<a href='/yh/yh/core/funcs/notify/act/YHNotifyHandleAct/changeState.act?seqId="+data.seqId+"&pageIndex="+pageIndex+"&showLength="+showLength+"&operation=3'>生效</a>"
      }
      td = td + "</td>";
      var className = "TableLine2" ;    
	  if(i%2 == 0){
	    className = "TableLine1" ;
	  }
	  var tr = new Element("tr" , {"class" : className}).update(td);
	  $('dataBody').appendChild(tr);  
	  
	//  addMoreEvent($('more_' + data.seqId) , data.seqId); 
	}

function checkAll(field) {
	  var deleteFlags = document.getElementsByName("deleteFlag");
	  for(var i = 0; i < deleteFlags.length; i++) {
	    deleteFlags[i].checked = field.checked;
	  }
	}
function delete_mail(pageIndex,showLength)
{  
  var deleteAllFlags = document.getElementsByName("deleteFlag");
  var delete_str="";
  for(i=0;i<deleteAllFlags.length;i++)
  {

	  if(deleteAllFlags[i].checked) {
		  delete_str += deleteAllFlags[i].value + "," ;   //SMS_BODY  seqID
	 }	
  }
  if(delete_str=="")
  {
     alert("要删除公告通知，请至少选择其中一条。");
     return;
  }

  msg='确认要删除所选公告通知吗？';
  if(window.confirm(msg))
  {   
	  var par = 'pageIndex='+pageIndex+'&showLength=' +showLength+'&delete_str=' +delete_str;

      var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/deleteCheckNotify.act';
	  var json = getJsonRs(url,par);
	  if (json.rtState == "0"){
		  window.location.reload();
		} else{
	      alert(json.rtMsrg);
	    }
  }
}

function cancel_top(pageIndex,showLength)
{  
  var deleteAllFlags = document.getElementsByName("deleteFlag");
  var delete_str="";
  for(i=0;i<deleteAllFlags.length;i++)
  {

	  if(deleteAllFlags[i].checked) {
		  delete_str += deleteAllFlags[i].value + "," ;   //SMS_BODY  seqID
	 }	
  }
  if(delete_str=="")
  {
     alert("要取消置顶，请至少选择其中一条。");
     return;
  }

  msg='确认要取消置顶吗？';
  if(window.confirm(msg))
  {   
	  var par = 'pageIndex='+pageIndex+'&showLength=' +showLength+'&delete_str=' +delete_str;

      var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/cancelTop.act';
	  var json = getJsonRs(url,par);
	  if (json.rtState == "0"){
		  window.location.reload();
		} else{
	      alert(json.rtMsrg);
	    }
  }
}

function delete_all()
{
 msg='确认要删除所有公告通知吗？\n删除后将不可恢复，确认删除请输入大写字母“OK”';
 if(window.prompt(msg,"") == "OK")
 { 
	var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/deleteAllNotify.act';
	var json = getJsonRs(url);
	if (json.rtState == "0"){
		window.location.reload();
	} else{
      alert(json.rtMsrg);
    }
 }
}

function show_toobject(seqId)
{
  URL="/yh/core/funcs/notify/manage/showObject.jsp?seqId="+seqId;
  myleft=(screen.availWidth-500)/2;
  window.open(URL,"read_notify","height=250,width=600,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}

function my_affair(seqId)
{
  myleft=(screen.availWidth-250)/2;
  mytop=(screen.availHeight-200)/2;
  URL="/yh/core/funcs/notify/manage/note.jsp?seqId="+seqId;
  window.open(URL,"","height=200,width=250,status=0,toolbar=no,menubar=no,location=no,scrollbars=auto,resizable=no,top="+mytop+",left="+myleft);
}

function open_notify(seqId,format)
{
 URL="/yh/core/funcs/notify/show/readNotify.jsp?isManage=1&seqId="+seqId;
 myleft=(screen.availWidth-780)/2;
 mytop=100
 mywidth=780;
 myheight=500;
 if(format=="1")
 {
    myleft=0;
    mytop=0
    mywidth=screen.availWidth-15;
    myheight=screen.availHeight-60;
 }
 window.open(URL,"公告通知","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function show_reader(seqId)
{
  URL="/yh/core/funcs/notify/show/showReader.jsp?seqId="+seqId;
  myleft=(screen.availWidth-500)/2;
  window.open(URL,"read_notify","height=500,width=700,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}

function order_by(fieldtemp)
{
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

function checkShunXu(){
  if(field=='SUBJECT'){
    if(ascDesc=='0') {
       $('subject').update("<u>标题</u><img border=0 src=\"<%=imgPath%>/arrow_up.gif\" width=\"11\" height=\"10\">");
       $('sendTime').update("<u>创建时间</u>");$('beginDate').update("<u>生效日期</u>");$('endDate').update("<u>终止日期</u>");
    }else {
   	 $('subject').update("<u>标题</u><img border=0 src=\"<%=imgPath%>/arrow_down.gif\" width=\"11\" height=\"10\">"); 
   	 $('sendTime').update("<u>创建时间</u>");$('beginDate').update("<u>生效日期</u>");$('endDate').update("<u>终止日期</u>");
       }
}else if(field=='SEND_TIME'){
 if(ascDesc=='0') {
         $('sendTime').update("<u>创建时间</u><img border=0 src=\"<%=imgPath%>/arrow_up.gif\" width=\"11\" height=\"10\">");
         $('subject').update("<u>标题</u>");$('beginDate').update("<u>生效日期</u>");$('endDate').update("<u>终止日期</u>");
      }else {
     	 $('sendTime').update("<u>创建时间</u><img border=0 src=\"<%=imgPath%>/arrow_down.gif\" width=\"11\" height=\"10\">"); 
     	 $('subject').update("<u>标题</u>");$('beginDate').update("<u>生效日期</u>");$('endDate').update("<u>终止日期</u>");
     }
}else if(field=='BEGIN_DATE'){
 if(ascDesc=='0') {
         $('beginDate').update("<u>生效日期</u><img border=0 src=\"<%=imgPath%>/arrow_up.gif\" width=\"11\" height=\"10\">");
         $('subject').update("<u>标题</u>");$('sendTime').update("<u>创建时间</u>");$('endDate').update("<u>终止日期</u>");
      }else {
     	 $('beginDate').update("<u>生效日期</u><img border=0 src=\"<%=imgPath%>/arrow_down.gif\" width=\"11\" height=\"10\">"); 
     	 $('subject').update("<u>标题</u>");$('sendTime').update("<u>创建时间</u>");$('endDate').update("<u>终止日期</u>");
     }
}else if(field=='END_DATE'){
 if(ascDesc=='0') {
         $('endDate').update("<u>终止日期</u><img border=0 src=\"<%=imgPath%>/arrow_up.gif\" width=\"11\" height=\"10\">");
         $('subject').update("<u>标题</u>");$('sendTime').update("<u>创建时间</u>");$('beginDate').update("<u>生效日期</u>");
      }else {
     	 $('endDate').update("<u>终止日期</u><img border=0 src=\"<%=imgPath%>/arrow_down.gif\" width=\"11\" height=\"10\">");
     	 $('subject').update("<u>标题</u>");$('sendTime').update("<u>创建时间</u>");$('beginDate').update("<u>生效日期</u>"); 
     }
}
}

function selChange(pageIndex , showLength ,typeId ,  field){
  ascDesc ='1';
  $("selTypeId").value = typeId;
  loadData(pageIndex , showLength ,typeId , ascDesc, field);
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
</script>
</head>
<body onload="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">
       <img src="<%=imgPath%>/notify_open.gif" WIDTH="22" HEIGHT="20" align="absmiddle">
       <span class="big3"> 管理公告通知</span>
       &nbsp;
       <select id="typeId" name="typeId" class="BigSelect" onChange="javascript:selChange($F('pageIndex') , $F('pageLen'),$('typeId').value,field);return false;">
       </select>
    </td>
    <td>

<div id="pagebar">
<div class="pgPanel">
<div>每页<select id="pageLen"  onchange="javascript:doInit();">
<option value="5" > 5</option>
<option value="10" selected>10</option>
    <option value="15">15</option>
    <option value="20">20</option>
</select>条</div>
 <div class="separator"></div>
 <div id="pgFirst" title="" class="pgBtn pgFirst pgFirstDisabled">
 </div>
 <div id="pgPrev" title="" class="pgBtn pgPrev pgPrevDisabled">
 </div><div class="separator">
 </div><div>第 
 <input onblur="checkPage();" onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" id="pageIndex" type="text" title="" value="1" size="5" class="SmallInput pgCurrentPage"> 页 / 共


  <span id="pageCount" class="pgTotalPage"></span> 页</div>
  <div class="separator"></div>
  <div title="下页" id="pgNext" class="pgBtn pgNext pgNextDisabled">
  </div>
  <div title="" id="pgLast" class="pgBtn pgLast pgLastDisabled">
  </div><div class="separator">
  </div><div id="freshLoad" title="刷新" class="pgBtn pgRefresh" onclick="javascript:refush();">
  </div><div class="separator"></div>
  <div id="pgSearchInfo" class="pgSearchInfo"></div>
  
  </div></div>
    </td>
  </tr>
</table>
<div id="hasData">
<table class="TableList" width="100%">
  <tr class="TableHeader">
      <td nowrap align="center">选择</td>
      <td nowrap align="center">发布人</td>
      <td nowrap align="center">类型</td>
      <td nowrap align="center">发布范围</td>
      <td nowrap align="center" id="subject"  name="subject" onClick="javascript:order_by('SUBJECT');" style="cursor:pointer;"><u>标题</u></td>
      <td nowrap align="center" id="sendTime" name="sendTime" onClick="javascript:order_by('SEND_TIME');" style="cursor:pointer;"><u>创建时间</u></td>
      <td nowrap align="center" id="beginDate" name="beginDate" onClick="javascript:order_by('BEGIN_DATE');" style="cursor:pointer;"><u>生效日期</u></td>
      <td nowrap align="center" id="endDate" name="endDate" onClick="javascript:order_by('END_DATE');" style="cursor:pointer;"><u>终止日期</u></td>
      <td nowrap align="center">状态</td>
      <td nowrap align="center" width='120'>操作</td>
    </tr>
    
   <tbody id="dataBody"></tbody>
   
    <tr class="TableControl">
      <td colspan="10">
      <input type="checkbox" name="allbox" id="allbox_for" onClick="javascript:checkAll(this);"><label for="allbox_for">全选</label> &nbsp;
      <a href="javascript:delete_mail($F('pageIndex') , $F('pageLen'));" title="删除所选公告通知"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选公告</a>&nbsp;
      <a href="javascript:cancel_top($F('pageIndex') , $F('pageLen'));" title="批量取消置顶"><img src="<%=imgPath%>/user_group.gif" align="absMiddle">取消置顶</a>&nbsp;
      <a href="javascript:delete_all();" title="删除所有自己发布的公告通知"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除全部公告</a>&nbsp;
      </td>
   </tr>
</table>
</div>
<div id="noData" align=center style="display:none">
   <table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="msgInfo" class="msg info"> 暂无公告数据
            </td>
        </tr>
    </tbody>
</table>
</div>
<div id="userpriv" style="width: 600px; height: 300px;">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.person.data.YHUserPriv"/>
</div>
<input type="hidden"  id="selTypeId"  value="0"/>
</body>
</html>