<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>未读新闻</title>
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
var field = 'NEWS_TIME'; 
var ascDesc = '1';
var pageCount = 0;
function doInit(){
  var index = $('pageIndex').value;
  if(index > 1){
    $('pageIndex').value =1;
  }
	  loadType();
	  loadData($F('pageIndex') , $F('pageLen'),$("selTypeId").value,ascDesc,field);
}

function checkShunXu(){
  if(field=='SUBJECT'){
    if(ascDesc=='0') {
       $('subject').update("<u>标题</u><img border=0 src=\"<%=imgPath%>/arrow_up.gif\" width=\"11\" height=\"10\">");
       $('newsTime').update("<u>发布时间</u>");$('clickCount').update("<u>点击次数</u>");
    }else {
   	 $('subject').update("<u>标题</u><img border=0 src=\"<%=imgPath%>/arrow_down.gif\" width=\"11\" height=\"10\">"); 
   	 $('newsTime').update("<u>发布时间</u>");$('clickCount').update("<u>点击次数</u>");
       }
}else if(field=='NEWS_TIME'){
 if(ascDesc=='0') {
         $('newsTime').update("<u>发布时间</u><img border=0 src=\"<%=imgPath%>/arrow_up.gif\" width=\"11\" height=\"10\">");
         $('subject').update("<u>标题</u>");$('clickCount').update("<u>点击次数</u>");
      }else {
     	 $('newsTime').update("<u>发布时间</u><img border=0 src=\"<%=imgPath%>/arrow_down.gif\" width=\"11\" height=\"10\">"); 
     	 $('subject').update("<u>标题</u>");$('clickCount').update("<u>点击次数</u>");
     }
}else if(field=='CLICK_COUNT'){
 if(ascDesc=='0') {
         $('clickCount').update("<u>点击次数</u><img border=0 src=\"<%=imgPath%>/arrow_up.gif\" width=\"11\" height=\"10\">");
         $('subject').update("<u>标题</u>");$('newsTime').update("<u>发布时间</u>");
      }else {
     	 $('clickCount').update("<u>点击次数</u><img border=0 src=\"<%=imgPath%>/arrow_down.gif\" width=\"11\" height=\"10\">"); 
     	 $('subject').update("<u>标题</u>");$('newsTime').update("<u>发布时间</u>");
     }
}
}

function loadType(){
	var url =contextPath+'/yh/core/funcs/news/act/YHNewsHandleAct/getnewsType.act';
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
//	  $('freshLoad').className = "pgBtn pgLoad";
	  //$('pageIndex').value = pageIndex;
//	  $('pgSearchInfo').innerHTML = "加载数据中,请稍后.....";
	  var par = "pageIndex="+pageIndex+"&showLength=" +showLength+"&type="+typeId+"&ascDesc="+ascDesc+"&field="+field;
	  var url =contextPath+'/yh/core/funcs/news/act/YHNewsShowAct/getnewsNoReadList.act';
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
		        addRow(data, i);
		       
		      }
		      $('hasData').show();
		      $('pagebar').show();
			  $('noData').hide();
		    }else{
		    	$('hasData').hide();
		    	$('pagebar').hide();
				  $('noData').show();
		    }
		    
		    $('freshLoad').className = "pgBtn pgRefresh"; 
		  }else{
			  $('hasData').hide();
			  $('pagebar').hide();
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
function addRow(data , i){
	 var td =  "<td><a href=javascript:open_news('" + data.seqId + "','" + data.format + "'); title='" + data.subjectTitle + "'>" + data.subject + "</a><img src='<%=imgPath%>/email_new.gif' align='absMiddle' border='0'></td>"
               + " <td align='center'>"+ data.typeName +"</td>"
               + " <td nowrap align='center'>" + data.newsTime.substring(0,19) +"</td>"
               + "<td nowrap align='center'>" + data.clickCount +"</td>"
               + " <td nowrap align='center'>" + data.commentCount +"</td>"
               + "<td nowrap align='center'>";
    var anonymityYn = data.anonymityYn;
    if(anonymityYn!="2") {
      td = td + "<a href='javascript:re_news("+data.seqId+");'> 评论</a>&nbsp;";
    }          
    td = td + "</td>";
    var className = "TableLine2" ;    
    if(i%2 == 0){
      className = "TableLine1" ;
    }
    var tr = new Element("tr" , {"class" : className}).update(td);
   $('dataBody').appendChild(tr);  
}

function re_news(seqId)
{
 URL="/yh/core/funcs/news/show/reNews.jsp?seqId="+seqId+"&manage=1";
 myleft=(screen.availWidth-500)/2;
 window.open(URL,"read_news","height=500,width=550,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes");
}

function read_all()
{
msg='确认要标记所有新闻为已读吗？';
if(window.confirm(msg))
{
  url="/yh/yh/core/funcs/news/act/YHNewsShowAct/changeNoReadAll.act";
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

function open_news(seqId,format)
{
 URL="/yh/core/funcs/news/show/readNews.jsp?seqId="+seqId;
 myleft=(screen.availWidth-780)/2;
 mytop=100
 mywidth=780;
 myheight=500;
 if(format=="1")
 {
    myleft=0;
    mytop=0
    mywidth=screen.availWidth-10;
    myheight=screen.availHeight-40;
 }
 window.open(URL,"read_news","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function read_all()
{
  msg='确认要标记所有新闻为已读吗？';
  if(window.confirm(msg))
  {
    url="/yh/yh/core/funcs/news/act/YHNewsShowAct/changeNoReadAll.act";
    location=url;
  }
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
</script>
</head>
<body onload="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">
       <img src="<%=imgPath%>/notify_open.gif" WIDTH="22" HEIGHT="20" align="absmiddle">
       <span class="big3">未读新闻</span>
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
      <td nowrap align="center" id="subject" name="subject" onClick="javascript:order_by('SUBJECT');" style="cursor:pointer;"><u>标题</u></td>
      <td nowrap align="center">类型</td>
      <td nowrap align="center" id="newsTime" name="newsTime" onClick="javascript:order_by('NEWS_TIME');" style="cursor:pointer;"><u>发布时间</u></td>
      <td nowrap align="center" id="clickCount" name="clickCount" onClick="javascript:order_by('CLICK_COUNT');" style="cursor:pointer;"><u>点击次数</u></td>
      <td nowrap align="center">评论(条)</td>
      <td nowrap align="center">新闻评论</td>
    </tr>
   <tbody id="dataBody"></tbody>
</table>

<br>
<table class="TableBlock" width="100%" align="center">
  <tr>
      <td class="TableContent" nowrap align="center" width="80"><b>快捷操作：</b></td>
      <td class="TableControl" nowrap>&nbsp;
   <a href="javascript:read_all();" title="标记所有新闻为已读"><img src="<%=imgPath%>/email_open.gif" align="absMiddle" border="0"> 标记所有新闻为已读</a>&nbsp;&nbsp;
      </td>
</tr>
</table>
</div>
<div id="noData" align=center style="display:none">
   <table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="msgInfo" class="msg info">无未读的新闻
            </td>
        </tr>
    </tbody>
</table>
</div>
<input type="hidden"  id="selTypeId"  value="0"/>
</body>
</html>