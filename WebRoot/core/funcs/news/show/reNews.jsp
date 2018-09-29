<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<%
//seqId 为空标示新建评论，不为空表示修改评论
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
  String userName = request.getParameter("userName");
  if (userName == null) {
    userName = "";
  }
  YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新闻评论</title>
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

<script type="text/javascript"><!--
var seqId = "<%=seqId%>";
var userSeqId = null;
var newsId = null;
var userLoginName = null;
var anonymityYn=0;
function doInit(){
//	  loadFlowType();
	  loadData($F('pageIndex') , '10');
}
function loadData(pageIndex , showLength){  
    $("table").update("");
	  var par = "pageIndex="+pageIndex+"&showLength=" +showLength+"&seqId="+seqId;
	  var url =contextPath+'/yh/core/funcs/news/act/YHNewsShowAct/getnewsManagerList.act';	  
	  var json = getJsonRs(url , par);
	 
	  if(json.rtState == "1"){
	    $("msg").innerHTML = json.rtMsrg;
		}		  
	  if(json.rtState == "0"){	   
	    var rtData = json.rtData;
	    var pageData = rtData.pageData;
	    var listData = rtData.listData;
	    var subject = rtData.subject;
	    var format = rtData.format;	
	    var publish = rtData.publish;  	     
	    newsId = rtData.newsId;
	    var commentCount = rtData.commentCount;
//	    if(commentCount==0) {
//           document.getElementById("showComment").style.display ="none";
//		}
	    anonymityYn = rtData.anonymityYn;	   
     
	   if(publish == '2'){
        $("msg").innerHTML = "此新闻已终止,无法查看!";
		 } 
		if(anonymityYn==1) {
		   document.getElementById("anonymityYn").style.display ="";
		   document.getElementById("authorName2").checked = true;
		}
	    userSeqId = rtData.userSeqId;
	    document.getElementById("authorName").value = userSeqId;
	    var nickName = rtData.loginNickName;	    
	    if(!nickName && nickName!="null" && nickName!="") {	      
	       document.getElementById("nickName").value = nickName;
	    }
	    userLoginName = rtData.userLoginName;
	    document.getElementById("userName").value = userLoginName;
	  
	    $('subject').update(subject);
	    var pageCount = pageData.pageCount;
	    var recordCount = pageData.recordCount;
	    $('recordCount').update(recordCount);
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
	    addEvent( pageIndex , pageCount);
	    if(listData.length > 0){
	      for(var i = listData.length ;i > 0 ;i --){
	        var data = listData[i-1];
	        addRow(data, i);	       
	      }
	    }else{
	      $('showComment').style.display = "none";
	      $('hiddenComment').style.display = "";
//	      $('msgInfo').update('无新闻数据');
	    }
	  }else{
		  $('hasData').style.display = "none";
	      $('noData').style.display = "";
	  } 
	}
function addEvent(index,pageCount){
	  var pageIndex = parseInt(index);
	  if(pageIndex == pageCount){
	    $('pgNext').onclick = function(){};
	    $('pgLast').onclick = function(){};
	    $('pgPrev').onclick = function(){
	      $('pageIndex').value = pageIndex - 1;
	      loadData(pageIndex - 1 , '10');};
	    $('pgFirst').onclick = function(){
	      $('pageIndex').value = 1;
	      loadData('1' , '10');};
	  }else if(pageIndex == 1){
	    $('pgPrev').onclick = function(){};
	    $('pgFirst').onclick = function(){};
	    $('pgNext').onclick = function(){
	      $('pageIndex').value = pageIndex + 1;
	      loadData(pageIndex + 1 ,'10');};
	    $('pgLast').onclick = function(){
	      $('pageIndex').value = pageCount;
	      loadData(pageCount ,'10');};
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
		      $('pageIndex').value = pageIndex + 1;
		      loadData(pageIndex + 1 ,'10');};
		    $('pgLast').onclick = function(){
		      $('pageIndex').value = pageCount;
		      loadData(pageCount ,'10');};
		    $('pgPrev').onclick = function(){
		      $('pageIndex').value = pageIndex - 1;
		      loadData(pageIndex - 1 , '10');};
		    $('pgFirst').onclick = function(){
		      $('pageIndex').value = 1;
		      loadData(1 , '10');};
		  }
	  }
	}

function alertMsg(commentId, newsId){
	var msg='确认要删除此评论吗？';
	if(window.confirm(msg))
	{   
	    var url =contextPath+'/yh/core/funcs/news/act/YHNewsShowAct/deleteComment.act?commentId=' + commentId + "&newsId="+newsId;
	    window.location.href = url;
	}
}

function addRow(data , i){
//	  var td1 = "<td nowrap align='center' class='TableHeader'>&nbsp;" + data.userName +"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发表时间："+data.reTime+"</td></br>";
	  var td2 = "&nbsp;" + data.content.replace("<", "&lt;").replace(">", "&gt;");
	 
	  if(data.parentId!=0) {
          td2 = td2 + "<br><hr width='95%'><b>[原贴]</b><br>" + data.content1;
      }
	  var td3 = "<a href=/yh/core/funcs/news/show/relay.jsp?parentId=" + data.sqlcommentId + "&newsId="+newsId+"&userName="+userLoginName+"&anonymityYn="+anonymityYn+" style='text-decoration:underline'>回复本贴</a>&nbsp;&nbsp;&nbsp;";
      if(userSeqId==data.userId || <%=person.isAdmin()%> || <%=person.isAdminRole()%>) {
         
    	  //td3 = td3 + "<a href='/yh/yh/core/funcs/news/act/YHNewsShowAct/deleteComment.act?commentId="+data.sqlcommentId+"&newsId="+newsId+"' style='text-decoration:underline'>删除</a>&nbsp;&nbsp;&nbsp;"
    	  td3 = td3 + "<a href=javascript:alertMsg('"+ data.sqlcommentId +"','"+ newsId +"') style='text-decoration:underline'>删除</a>&nbsp;&nbsp;&nbsp;"
      }
      td3 = td3 + "  回复数：" + data.relayCount;
	  
//	  var className = "TableLine2" ;    
//	  if(i%2 == 0){
//	    className = "TableLine1" ;
//	  }
//	  var tr1 = new Element("tr1" , {"class" : className}).update(td1);
//	  var tr2 = new Element("tr2" , {"class" : className}).update(td2);
//	  var tr3 = new Element("tr3" , {"class" : className}).update(td3);
//	  $('dataBody').appendChild(tr1); 
//	  $('dataBody').appendChild(tr2);  
//	  $('dataBody').appendChild(tr3);
	 var table = $('table');
     var league = table.insertRow(0);
     
     var leagueTD1 = league.insertCell(0);
     leagueTD1.className = "TableHeader";
     leagueTD1.align = "center";
     leagueTD1.innerHTML = "&nbsp;" + data.userName +"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发表时间："+data.reTime.substring(0,19);

     var league2 = table.insertRow(1);
     league2.height = "40";
     var leagueTD2 = league2.insertCell(0);
     leagueTD2.className = "TableData";     
     leagueTD2.innerHTML = td2; 
        
     var league3 = table.insertRow(2);
     var leagueTD3 = league3.insertCell(0);
     leagueTD3.className = "TableControl";
     leagueTD3.align = "right";
     leagueTD3.innerHTML = td3;
	}
function CheckForm()
{  
  $("form1").action = contextPath + "/yh/core/funcs/news/act/YHNewsShowAct/addNewsComment.act";
   if(document.form1.content.value=="")
   { alert("评论的内容不能为空！");
     return (false);
   }
   var authorName1 = document.getElementById('authorName1');
   if(anonymityYn =='1'){ //如果是匿名评论，判断匿名是否为空，如果为空，则返回
    var isNull = $('nickName').value;
    

   if(isNull== null || isNull=="" || isNull.trim()==""){
      if($('authorName').checked){
        $("form1").submit();
        return true;
      }else{
	      alert("匿名名称不能为空！");
	      $('nickName').focus();
	      return;
      }
      return (false);
    }else{      
			$("form1").submit();
			return true;
    }
   }else if(anonymityYn =='0'){
     $("form1").submit();
   }
//   if(authorName1.checked && document.form1.nickName.value=="")
//   { alert("昵称不能为空！");
//     return (false);
//   }

   return (true);
}

/*function addComment() {
  if (!CheckForm()) {
    return false;
  }
  $("form1").action = contextPath + "/yh/core/funcs/news/act/YHNewsShowAct/addNewsComment.act";
  $("form1").submit();
  $("form1").action = "";
}*/

function addComment() {
	  $("form1").action = contextPath + "/yh/core/funcs/news/act/YHNewsShowAct/addNewsComment.act";
	 
	  
	  if(document.form1.content.value=="" || document.form1.content.value==null)
	  { alert("评论的内容不能为空！");
	    return (false);
	  }else{
	    $("form1").submit(); 
	  }
	}


function refush(){
	var pageCount = $('pageCount').innerHTML;
	  if (!trim($F('pageIndex'))) {
	    alert('请输入有效的页码!');
	    $('pageIndex').value = "";
	    $('pageIndex').focus();
	    return;
	  }
	  
	  if (parseInt($F('pageIndex')) > parseInt(pageCount)){
	     alert('请输入有效的页码!');
	     $('pageIndex').focus();
	     $('pageIndex').select();
	     return;
	  }
	  if (parseInt($F('pageIndex')) == 0){
	    alert('请输入有效的页码!');
	    $('pageIndex').focus();
	    $('pageIndex').select();
	    return;
	 }
		 //loadData($F('pageIndex') , $F('pageLen'));
	  loadData($F('pageIndex') , '10');
	} 
--></script>
</head>
<body onload="doInit();">

<div id="hasData">
<table width=100% border=0 cellspacing=0 cellpadding=0>
  <tr height="40">
    <td align=center><span class="big3">原文 </span><a href="<%=contextPath %>/core/funcs/news/show/readNews.jsp?seqId=<%=seqId%>" style="TEXT-DECORATION:underline"><span id="subject" class="big3"></span></a></span></td>
  </tr>
</table>

<div id="showComment">
<table width=95% border=0 cellspacing=0 cellpadding=2 align="center" class="small1">
 <tr>
 <td>
  相关评论<span id="recordCount"></span>条/每页<span id="recordCount">10</span>条
 </td>
 <td>
 <div id="pagebar">
<div class="pgPanel">
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
  </div>
  <div id="freshLoad" title="刷新" class="pgBtn pgRefresh" onclick="javascript:refush();">
  </div><div class="separator"></div>
  
  <div id="pgSearchInfo" class="pgSearchInfo"></div>
  
  </div></div>
    </td>
  </tr>
</table>
<!-- 评论列表 -->
<table class="TableBlock" width="95%" align="center" class="small" id="table">
</table>
 </div>
 <div id="hiddenComment" style="display:none">
 <TABLE class=MessageBox width=280 align=center>
<TBODY>
<TR>
<TD class="msg info">
<DIV class=content 
style="FONT-SIZE: 12pt">暂无评论</DIV></TD></TR></TBODY></TABLE>
 </div>
<br>
<form method="post" id="form1" name="form1">
  <table class="TableBlock" width="95%" align="center">
     <tr>
      <td class="TableHeader" colspan="2">
        <img src="<%=imgPath%>/green_arrow.gif"> 发表评论：
      </td>
    </tr>
    <tr>
      <td align="center" class="TableData">内容：</td>
      <td class="TableData">
        <textarea cols="45" name="content" id="content" rows="5" class="BigInput" wrap="on"></textarea>
      </td>
    </tr>
    <tr>
      <td align="center" class="TableData">署名：</td>
      <td class="TableData">
        <input type="radio" id="authorName" name="authorName"  checked value="0">
        <input type="text"  id="userName" name="userName" size="10" maxlength="25" class="BigStatic" value="<%=userName%>" readonly>
        <span id="anonymityYn"  style="display:none">
        <input type="radio" id="authorName2"  name="authorName" value="nickName">昵称
        <input type="text" id="nickName" name="nickName" size="10" maxlength="25" class="SmallInput" value="">        
        </span>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td nowrap colspan="2">
        <input type="hidden" value="<%=seqId%>" name="newsId">
        <input type="hidden" value="" name="manage">
        <input type="button" value="发表" onclick="addComment();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</div>
<div id="noData" style="display:none">
<TABLE class=MessageBox width=280 align=center>
<TBODY>
<TR>
<TD class="msg info">
<H4 class=title>错误</H4>
<DIV class=content id="msg" style="FONT-SIZE: 12pt; width: 180px;">此新闻无评论！</DIV></TD></TR></TBODY></TABLE>
</div>
</body>
</html>