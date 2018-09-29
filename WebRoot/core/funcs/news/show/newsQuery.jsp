<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var field = 'NEWS_TIME'; 
var ascDesc = '1';
var pageCount = 0;
function checkForm()
{   
	var regex = /^((\d{2}(([02468][048])|([13579][26]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|([1-2][0-9])))))|(\d{2}(([02468][1235679])|([13579][01345789]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))/; //日期部分
    if(document.form1.beginDate.value!=""){
      if(!regex.test(document.form1.beginDate.value)){
    	 alert("输入的日期格式错误");
    	 document.form1.beginDate.value = "";
    	 document.form1.beginDate.focus();
        return false;
      }
    }
    if(document.form1.endDate.value!=""){
        if(!regex.test(document.form1.endDate.value)){
      	 alert("输入的日期格式错误");
      	document.form1.endDate.value = "";
      	 document.form1.endDate.focus();
          return false;
        }
      }
    if(document.form1.beginDate.value>document.form1.endDate.value && document.form1.endDate.value != "") {
 	   alert("起始日期不能大于结束日期");
 	   return false;
    }
   loadData($F('pageIndex') , $F('pageLen'),ascDesc,field);
}
function selectUser() {
 	var URL="<%=contextPath %>/core/funcs/orgselect/MultiUserSelect.jsp";
  	openDialog(URL,'500', '500');
}
function doInit(){
  var index = $('pageIndex').value;
  if(index > 1){
    $('pageIndex').value =1;
  }
	 var beginParameters = {
		      inputId:'beginDate',
		      property:{isHaveTime:false}
		      ,bindToBtn:'beginDateImg'
		  };
		  new Calendar(beginParameters);
		  var endParameters = {
		      inputId:'endDate',
		      property:{isHaveTime:false}
		      ,bindToBtn:'endDateImg'
		  };
		  new Calendar(endParameters);
		  if(field=='NEWS_TIME'){
			  if(ascDesc=='0') {
		            $('sendTime').update("<u>创建时间</u><img border=0 src=\"<%=imgPath%>/arrow_up.gif\" width=\"11\" height=\"10\">");
		         }else {
		        	 $('sendTime').update("<u>创建时间</u><img border=0 src=\"<%=imgPath%>/arrow_down.gif\" width=\"11\" height=\"10\">"); 
		        }
	    }
		  var url =contextPath+'/yh/core/funcs/news/act/YHNewsHandleAct/getnewsType.act';
		  var json = getJsonRs(url);
		  if (json.rtState == "0"){
			  var rtData = json.rtData;
				var typeData = rtData.typeData;
				$("typeId").options.add(new Option("所有类型","0")); //添加全部新闻和新闻查询中类型不一致 shenrm
				 if(typeData.length > 0){
				     for(var i = 0 ;i < typeData.length ;i ++){
				       var optionStr = typeData[i];
				       $("typeId").options.add(new Option(optionStr.typeDesc,optionStr.typeId)); 
				     }
				  }
				  //添加全部新闻和新闻查询中类型不一致 shenrm
				  $("typeId").options.add(new Option('无类型',""));
			} else{
		      alert(json.rtMsrg);
		    }	  
}

function selChange(pageIndex , showLength ,typeId ,  field){
  ascDesc ='1';
  if(pageIndex > 1){
    pageIndex = 1;
    $("pageIndex").value = '1';
  }
  loadData(pageIndex , showLength ,typeId , ascDesc, field);
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

function loadData(pageIndex , showLength, ascDesc , field){
//	  $('freshLoad').className = "pgBtn pgLoad";
	  //$('pageIndex').value = pageIndex;
//	  $('pgSearchInfo').innerHTML = "加载数据中,请稍后.....";
  var index = $('pageIndex').value;
  if(!index){
    $('pageIndex').value = '1';
    pageIndex = 1;
  }
	  var url = "<%=contextPath%>/yh/core/funcs/news/act/YHNewsShowAct/queryNews.act?pageIndex="+ pageIndex +"&showLength="+showLength+"&ascDesc="+ascDesc+"&field="+field;
	  var json = getJsonRs(url, mergeQueryString($("form1")));
	  document.getElementById("queryParams").style.display = "none";
	  document.getElementById("queryResult").style.display = "";
	  if(json.rtState == "0"){
	    var rtData = json.rtData;
	    var pageData = rtData.pageData;
	    var listData = rtData.listData;
	    pageCount = pageData.pageCount;
	    var recordCount = pageData.recordCount;
	    var pgStartRecord = pageData.pgStartRecord;
	    var pgEndRecord = pageData.pgEndRecord;
	    var pgSearchInfo = "共&nbsp;"+ recordCount +"&nbsp;条记录，显示第&nbsp;<span class=\"pgStartRecord\">"+pgStartRecord+"</span>&nbsp;条&nbsp;-&nbsp;第&nbsp;<span class=\"pgEndRecord\">"+pgEndRecord+"</span>&nbsp;条记录";
	    $('pgSearchInfo').innerHTML = pgSearchInfo;
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
	    addEvent( pageIndex , pageCount);
	    removeAllChildren($('dataBody'));
	    if(listData.length > 0){
	      for(var i = 0 ;i < listData.length ;i ++){
	        var data = listData[i];
	        addRow(data, i);
	      }
	    }else{		   
	      $('hasData').style.display = "none";
	      
	      $('noData').style.display = "";	     
//	      alert("fdas");
	    }
	    
	    $('freshLoad').className = "pgBtn pgRefresh"; 
	  }else{
		  $('hasData').style.display = "none";
	    $('noData').style.display = "";	 	   
		  $('noData').update(json.rtMsrg);
	  } 
	}
function addEvent(index,pageCount){
	  var pageLen = $F('pageLen');
	  var pageIndex = parseInt(index);
	  if(pageIndex == pageCount){
	    $('pgNext').onclick = function(){};
	    $('pgLast').onclick = function(){};
	    $('pgPrev').onclick = function(){
	      $('pageIndex').value = pageIndex - 1;
	      loadData(pageIndex - 1 , pageLen ,ascDesc,field);};
	    $('pgFirst').onclick = function(){
	      $('pageIndex').value = 1;
	      loadData('1' , pageLen,ascDesc,field);};
	  }else if(pageIndex == 1){
	    $('pgPrev').onclick = function(){};
	    $('pgFirst').onclick = function(){};
	    $('pgNext').onclick = function(){
	    	if($F('pageIndex')>pageCount){
	               alert('请输入有效的页码!');
	               return;
				}
	      $('pageIndex').value = pageIndex + 1;
	      loadData(pageIndex + 1 ,pageLen,ascDesc,field);};
	    $('pgLast').onclick = function(){
	      $('pageIndex').value = pageCount;
	      loadData(pageCount , pageLen,ascDesc,field);};
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
	      loadData(pageIndex + 1 , pageLen,ascDesc,field);};
	    $('pgLast').onclick = function(){
	      $('pageIndex').value = pageCount;
	      loadData(pageCount ,pageLen,ascDesc,field);};
	    $('pgPrev').onclick = function(){
	      $('pageIndex').value = pageIndex - 1;
	      loadData(pageIndex - 1 ,pageLen,ascDesc,field);};
	    $('pgFirst').onclick = function(){
	      $('pageIndex').value = 1;
	      loadData(1 ,pageLen,ascDesc,field);};
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

function addRow(data , i){
	  var td = "<td><a href=javascript:open_news('" + data.seqId + "','" + data.format + "'); title='"+data.subjectTitle+"'>"+data.subject+"</a>";
	  if(data.isRead=='0'){
         td = td + "<img src='<%=imgPath%>/email_new.gif' align='absMiddle' border='0'>";
	   }
	   td =  td +"</td>"
	            + "<td align='center'>" + data.typeName +"</td>"
	            + "<td nowrap align='center'>"+data.newsTime.substring(0,19)+"</td>"
	            + " <td nowrap align='center'>" + data.clickCount +"</td>"
	            + "<td nowrap align='center'>" + data.commentCount +"</td>";
	           
	  var className = "TableLine2" ;    
	  if(i%2 == 0){
	    className = "TableLine1" ;
	  }
	  var tr = new Element("tr" , {"class" : className}).update(td);
	  $('dataBody').appendChild(tr);  
	  
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

function delete_news(seqId)
{
 msg='确认要删除该项新闻吗？';
 if(window.confirm(msg))
 {
  url="/yh/yh/core/funcs/news/act/YHNewsHandleAct/deleteNewById.act?seqId="+seqId;
  window.location.href=url;
 }
}
function re_news(seqId)
{
 URL="/yh/core/funcs/news/show/reNews.jsp?seqId="+seqId+"&manage=1";
 myleft=(screen.availWidth-500)/2;
 window.open(URL,"read_news","height=500,width=550,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes");
}

function ClearUser(TO_ID, TO_NAME){
	  if(TO_ID=="" || TO_ID=="undefined" || TO_ID== null){
		TO_ID="TO_ID";
		TO_NAME="TO_NAME";
	  }
	  document.getElementsByName(TO_ID)[0].value="";
	  document.getElementsByName(TO_NAME)[0].value="";
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
    if(field=='NEWS_TIME'){
		  if(ascDesc=='0') {
	            $('sendTime').update("<u>创建时间</u><img border=0 src=\"<%=imgPath%>/arrow_up.gif\" width=\"11\" height=\"10\">");
	         }else {
	        	 $('sendTime').update("<u>创建时间</u><img border=0 src=\"<%=imgPath%>/arrow_down.gif\" width=\"11\" height=\"10\">"); 
	        }
    }
    loadData($F('pageIndex') , $F('pageLen'),ascDesc,field);
}
</script>
</head>
<body onload="doInit();">
<div id="queryParams">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath%>/search.gif"><span class="big3"> 新闻查询</span>
    </td>
  </tr>
</table>

<form  action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="550" align="center">
    <tr>
      <td nowrap class="TableData">发布人：</td>
      <td class="TableData">
        <input type="hidden" id="user" name="provider" value="">
        <textarea cols=30 id="userDesc" name="providerName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectUser(['user', 'userDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('provider','providerName')">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 格式：</td>
      <td class="TableData">
      <select name="format" class="BigSelect">
        <option value="" selected>全部</option>
        <option value="0">普通格式</option>
        <option value="1">MHT格式</option>
        <option value="2">超链接格式</option>
      </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 类型：</td>
      <td class="TableData"> 
        <select id="typeId" name="typeId" class="BigSelect">
        </select>&nbsp;
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 标题：</td>
      <td class="TableData">
        <input type="text" name="subject" size="33" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 发布日期：</td>
      <td class="TableData">
       <input type="text" id = "beginDate" name="beginDate" class="BigInput" size="10" maxlength="10"  value="">
        <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
        至 <input type="text" id = "endDate" name="endDate" class="BigInput" size="10" maxlength="10"  value="">
        <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 内容：</td>
      <td class="TableData">
        <input type="text" name="content" size="33" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
       <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.news.data.YHNews"/>
        <input type="button" value="查询" class="BigButton" onClick="checkForm();">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</div>
<div id="queryResult" style="display:none">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath%>/search.gif"><span class="big3"> 新闻查询结果</span>
    </td>
  </tr>
</table>

<div id="hasData">
<div id="pagebar">
<div class="pgPanel">
<div>每页<select id="pageLen"  onchange="javascript: selChange($F('pageIndex') , $F('pageLen'),'',field);">
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
 <input onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" id="pageIndex" type="text" title="" value="1" size="5" class="SmallInput pgCurrentPage"> 页 / 共



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
<table class="TableList" width="100%">
 <tr class="TableHeader">
      <td nowrap align="center">标题</td>
      <td nowrap align="center">类型</td>
      <td nowrap align="center" id="sendTime" name="sendTime" onClick="javascript:order_by('NEWS_TIME');" style="cursor:pointer;"><u>发布时间</u></td>
      <td nowrap align="center">点击次数</td>
      <td nowrap align="center">评论(条)</td>
   </tr>
    <tbody id="dataBody"></tbody>
 </table>
 <input style="display: block; margin: 10px auto;" type="button" class="BigButton" value="返回" onclick="javascript:goBack(); return false;">
 </div>
 <div id="noData" style="display:none">
 <TABLE id="showmsg" class=MessageBox width=280 align=center>
<TBODY>
<TR>
<TD class="msg info" id="msg info">
<DIV class=content 
style="FONT-SIZE: 12pt">无符合条件的新闻</DIV></TD></TR></TBODY></TABLE>
</div>
</div>
</body>
<script type="text/javascript">
	function goBack(){
    window.location.href = "<%=contextPath%>/core/funcs/news/show/newsQuery.jsp";
  }
</script>
<script>
function bindValidDtFunc() {
	bindAssertDateTimePrcBatch([{id:"beginDate", type:"d"}, {id:"endDate", type:"d"}]);
}

bindValidDtFunc();

</script>
</html>