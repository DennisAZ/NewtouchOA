<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>待审批公告</title>
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
  if(pageIndex > 1){
    pageIndex = 1;
    $("pageIndex").value = '1';
  }
  loadData(pageIndex , showLength ,typeId , ascDesc, field);
}

function loadData(pageIndex , showLength , typeId , ascDesc , field){
  checkShunXu();
//	  $('freshLoad').className = "pgBtn pgLoad";
	  //$('pageIndex').value = pageIndex;
//	  $('pgSearchInfo').innerHTML = "加载数据中,请稍后.....";
  var index = $('pageIndex').value;
  if(!index){
    $('pageIndex').value = '1';
    pageIndex = 1;
  }
	  var par = "pageIndex="+pageIndex+"&showLength=" +showLength+"&type="+typeId+"&ascDesc="+ascDesc+"&field="+field;	  
	  var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyAuditingAct/getUnAuditedList.act';
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
	    
	      addEvent(pageIndex , pageCount,typeId);
	    
	    //removeAllChildren($('dataBody'));	    
	    $('dataBody').update("");
	    if(listData.length > 0){
		     $('hasData').show();
		   	 $('pagebar').show();
		   	 $('noData').hide();		    	
		      for(var i = 0 ;i < listData.length ;i ++){
		        var data = listData[i];
		        addRow(data, i);		       
		      }
		    }else{
		    	 $('hasData').hide();
		    	 $('pagebar').hide();		    	
				   $('noData').show();				  
		    }		    
		    $('freshLoad').className = "pgBtn pgRefresh"; 
		  }else{
			  $('hasData').hide();
			  $('noData').show();
			  $('msgInfo').update(json.rtMsrg);
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
	  }else {
		 
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
function addRow(data , i){
	 var toNameTitle = data.toNameTitle;
     var toNameStr = data.toNameStr;
     var privNameTitle = data.privNameTitle;
     var privNameStr = data.privNameStr;
     var userNameTitle = data.userNameTitle;
     var userNameStr = data.userNameStr;
     var title = toNameTitle + privNameTitle + userNameTitle;
     var str = toNameStr + privNameStr + userNameStr;
	  var td =  "<td nowrap align='center' style='cursor:pointer'  title='部门："+data.deptName+"'>"+data.fromName+"</td>"
	            + " <td align='center'>"+ data.typeName +"</td>"
	            + " <td style='cursor:pointer' title='" + title +"'>"+str+"</td>"
	            + " <td><a href=javascript:open_notify('" + data.seqId + "','" + data.format + "'); title='" + data.subjectTitle +"'>"+data.subject+"</a></td>"
	            + " <td nowrap align='center'>" + data.sendTime.substring(0,19) +"</td>"
	            + " <td nowrap align='center'>" + data.beginDate +"</td>"
	            + "<td nowrap align='center'>" + data.endDate +"</td>"
	            + " <td nowrap align='center'> <a href='/yh/core/funcs/notify/auditing/pass.jsp?seqId="+data.seqId+"'>批准</a>"
	            + "&nbsp;&nbsp;<a href='/yh/core/funcs/notify/auditing/unPass.jsp?seqId="+data.seqId+"'>不批准</a></td>";
	          
	  var className = "TableLine2" ;    
	  if(i%2 == 0){
	    className = "TableLine1" ;
	  }
	  var tr = new Element("tr" , {"class" : className}).update(td);
	  $('dataBody').appendChild(tr);  
	  
	}

function read_all()
{
msg='确认要标记所有新闻为已读吗？';
if(window.confirm(msg))
{
  url= contextPath + "/yh/core/funcs/news/act/YHNewsShowAct/changeNoReadAll.act";
  location=url;
}
}

function removeAllChildren(parentNode){
  parentNode = $(parentNode);
  while(parentNode.firstChild){
    var oldNode = parentNode.removeChild(parentNode.firstChild);
    oldNode = null;
  }
}

function show_toobject(seqId)
{
  URL= contextPath + "/core/funcs/notify/manage/showObject.jsp?seqId="+seqId;
  myleft=(screen.availWidth-500)/2;
  window.open(URL,"read_notify","height=250,width=600,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}

function my_affair(seqId)
{
  myleft=(screen.availWidth-250)/2;
  mytop=(screen.availHeight-200)/2;
  URL= contextPath + "/core/funcs/notify/manage/note.jsp?seqId="+seqId;
  window.open(URL,"","height=200,width=250,status=0,toolbar=no,menubar=no,location=no,scrollbars=auto,resizable=no,top="+mytop+",left="+myleft);
}

function open_notify(seqId,format)
{
 URL= contextPath + "/core/funcs/notify/show/readNotify.jsp?isManage=1&seqId="+seqId;
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
  URL= contextPath + "/core/funcs/notify/show/showReader.jsp?seqId="+seqId;
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
       <span class="big3"> 待审批公告通知 </span>
       &nbsp;
       <select id="typeId" name="typeId" class="BigSelect" onChange="javascript:selChange($F('pageIndex') , $F('pageLen'),$('typeId').value,'SEQ_ID');">
       </select>
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
      <td nowrap align="center">发布人</td>
      <td nowrap align="center">类型</td>
      <td nowrap align="center">发布范围</td>
     <td nowrap align="center" id="subject" name="subject" onClick="javascript:order_by('SUBJECT');" style="cursor:pointer;"><u>标题</u></td>
      <td nowrap align="center" id="sendTime" name="sendTime" onClick="javascript:order_by('SEND_TIME');" style="cursor:pointer;"><u>创建时间</u></td>
      <td nowrap align="center" id="beginDate" name="beginDate" onClick="javascript:order_by('BEGIN_DATE');" style="cursor:pointer;"><u>生效日期</u></td>
      <td nowrap align="center" id="endDate" name="endDate" onClick="javascript:order_by('END_DATE');" style="cursor:pointer;"><u>终止日期</u></td>
      <td nowrap align="center">操作</td>
    </tr>
   <tbody id="dataBody"></tbody>
</table>
</div>
<div id="noData" align=center style="display:none">
   <table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="msgInfo" class="msg info"> 暂无待批公告
            </td>
        </tr>
    </tbody>
</table>
</div>
<input type="hidden"  id="selTypeId"  value="0"/>
</body>
</html>