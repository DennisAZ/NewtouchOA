<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
  YHPerson  loginUser = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
  String userName = loginUser.getUserName();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看新闻</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>

<script type="text/javascript">
var seqId = "<%=seqId%>";
var userSeqId = null;
var newsId = null;
var userLoginName = "<%=userName %>";
var anonymityYn;
function doInit(){
	  loadData('0','5');
}
function loadData(pageIndex , showLength){
  var par = "pageIndex="+pageIndex+"&showLength=" +showLength+"&seqId="+seqId;
  var url =contextPath+'/yh/core/funcs/news/act/YHNewsShowAct/getnewsShowList.act';
  var json = getJsonRs(url , par);
  if (json.rtState == "0") {
    var rtData = json.rtData;	    
    var publish = rtData.publish;
    var pageData = rtData.pageData;
    var listData = rtData.listData;
    var subject = rtData.subject;
    $("subject").innerHTML = rtData.subject;
    $("subject").setStyle(rtData.subjectFont);
    var format = rtData.format;
    newsId = rtData.newsId;
    var content = rtData.content;
    var attachFile = rtData.attachFile;
    var ym = rtData.ym;
    var commentCount = rtData.commentCount;
    anonymityYn = rtData.anonymityYn;
    var format = rtData.format;
 
    if(publish=='2'){
      $("publish").hide();
    }
    if(anonymityYn==1) {		  
      document.getElementById("anonymityYn_span").style.display ="";
      document.getElementById("showAllButton").style.display ="none";
      document.getElementById("authorName2").checked = true;
    }
    if(anonymityYn==2){
      document.getElementById("anonymityYn").style.display = "none";
      document.getElementById("anonymityYnClose").style.display = "";
    }
    userSeqId = rtData.userSeqId;
    var nickName = rtData.loginNickName;
    if(!nickName && nickName!="null" && nickName!=""){
      document.getElementById("nickName").value = nickName;
    }
    //userLoginName = rtData.userLoginName;
    document.getElementById("userName").value = userLoginName;
    var title = "<u title='部门：" + rtData.userDeptName + "' style='cursor:pointer'>" + rtData.userLoginName
        + "</u>&nbsp;&nbsp;" + "发布于：<i>" + rtData.newsTime.substring(0,19) + "</i>&nbsp;&nbsp;点击" + rtData.clickCount + "次";
    $("title").update(title);
    if(format == "0"){
      $("content").update(content);
    }
    if(format == "1"){ 
      var attachFrame ="";   
      if(isHaveMht(attachFile, rtData.attachmentId)){    
		    attachFrame = "<iframe id=mhtFrame src='<%=contextPath%>/getFile?module=news&ym="+ym+"&attachFile=" + encodeURIComponent(findMht(attachFile, rtData.attachmentId)) + "'  width='100%' height='100%' style='overflow:auto'></iframe>";
      }else{
        attachFrame = "<iframe width='100%' height='100%' style='overflow:auto'></iframe>";
      }
      $("content").style.height = '600';
      $("content").update(attachFrame);
    }
    if (format == "2") {
      window.location.href = rtData.content;
      var urlStr = rtData.content.trim();
      if (!urlStr) {
        window.location.href = contextPath + "/core/inc/empty.html";
      }else if (urlStr.indexOf("http://") == 0) {
        window.location.href = urlStr;
      }else {
        window.location.href = "http://" + urlStr;
      }
      return;
    }
    var attrIds = rtData.attachmentId;
    var attrNames = rtData.attachmentName;
    $('attachmentName').value = attrNames;
    $('attachmentId').value = attrIds;
    attachMenuUtil("showAtt","news",null,$('attachmentName').value ,$('attachmentId').value,true, true,'', false, true);
    var table = $('table2');

    var league0 = table.insertRow(0);
    var leagueTD0 = league0.insertCell(0);
    leagueTD0.className = "TableHeader";
    leagueTD0.colspan = "2";
    leagueTD0.align = "center";
    leagueTD0.innerHTML = "最新5条评论";
    if(listData.length > 0) {
      for(var i = listData.length - 1; i >=0; i--) {
        var data = listData[i];
        addRow(data, i);
	    }
	  }else {
      var league0 = table.insertRow(1);
      var leagueTD0 = league0.insertCell(0);
      leagueTD0.className = "TableData Content";
	         leagueTD0.align = "left";
	         leagueTD0.innerHTML = "暂无评论 ";
    }
  }else { //json.rtState == "0"
    var temp =' <div style="" id="noData">';
    temp += ' <table width="280" align="center" class="MessageBox">'
    temp += '<tbody>';
    temp += '<tr>';
    temp += '<td class="msg info">';
    temp += '<div style="font-size: 12pt;" class="content">'+ json.rtMsrg +'</div></td></tr></tbody></table>'
    temp += '</div>';		  
    document.body.innerHTML = temp;
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
	  var td2 = "&nbsp;" + data.content;
	  if(data.parentId!=0) {
          td2 = td2 + "<br><hr width='95%'><b>[原贴]</b><br>" + data.content1;
      }
 
      var td3 = "<a href='/yh/core/funcs/news/show/relay.jsp?parentId=" + data.sqlcommentId + "&newsId="+newsId+"&anonymityYn="+anonymityYn+"' style='text-decoration:underline'>回复本贴</a>&nbsp;&nbsp;&nbsp;";
      if(userSeqId==data.userId) {
         //td3 = td3 + "<a href='/yh/yh/core/funcs/news/act/YHNewsShowAct/deleteComment.act?commentId="+data.sqlcommentId+"&newsId="+newsId+"' style='text-decoration:underline'>删除</a>&nbsp;&nbsp;&nbsp;"
    	  td3 = td3 + "<a href=javascript:alertMsg('"+ data.sqlcommentId +"','"+ newsId +"') style='text-decoration:underline'>删除</a>&nbsp;&nbsp;&nbsp;"
      }
      td3 = td3 + "  回复数：" + data.relayCount;
	  var table = $('table2');
    
      var league1 = table.insertRow(1);
      var leagueTD1 = league1.insertCell(0);
      leagueTD1.className = "TableContent";
      leagueTD1.innerHTML = "&nbsp;&nbsp;" + data.userName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发表时间："+data.reTime.substring(0,19);

      var league2 = table.insertRow(2);
      league2.height = "40";
      var leagueTD2 = league2.insertCell(0);
      leagueTD2.className = "TableData";
      leagueTD2.innerHTML = td2;

      var league3 = table.insertRow(3);
      var leagueTD3 = league3.insertCell(0);
      leagueTD3.className = "TableControl";
      leagueTD3.align = "right";
      leagueTD3.innerHTML = td3;
}

function CheckForm()
{
   if(document.form1.content.value=="")
   { alert("评论的内容不能为空！");
     return (false);
   }
   var authorName1 = document.getElementById('authorName1');
//   if(authorName1.checked && document.form1.nickName.value=="")
//   { alert("昵称不能为空！");
//     return (false);
//   }
   if(anonymityYn ==1){ //如果是匿名评论，判断匿名是否为空，如果为空，则返回
     var isNull = $('nickName').value;
     
     if(isNull== null || isNull=="" || isNull.trim()==""){
       if($('authorName').checked){
         $("form1").submit();
         return;
       }else{
 	      alert("匿名名称不能为空！");
 	      $('nickName').focus();
 	      return false;
       }
       return (false);
     }else{
 			$("form1").submit();
 			return ;
     }
    }else if(anonymityYn =='0'){      
      $("form1").submit();
    }
   return (true);
}

function addComment() {
  $("form1").action = contextPath + "/yh/core/funcs/news/act/YHNewsShowAct/addNewsComment.act";
  var content = $("content").value;
  
  if(document.form1.content.value=="" || document.form1.content.value==null)
  { alert("评论的内容不能为空！");
    return (false);
  }else{
    $("form1").submit(); 
  }
}
/*
function findMht(attachFile){
  var profix = attachFile.split("_")[0].split(",");
  var lastfix = attachFile.split("_")[1];
  var fileNames = lastfix.split("*");
  for(var i=0; i<fileNames.length; i++){
    if(fileNames[i].toLowerCase().indexOf(".mht")!=-1){      
				return profix[i]+"_"+fileNames[i];
    }
  }
}

function isHaveMht(attachFile){
 var flag = findMht(attachFile);
 if(flag){
    return true;
 }
 return false;
}*/
function findMht(attachFile, attachFileIds){
	   var profix = attachFile.split("_")[0].split(",");
	   var lastfix = attachFile.split("_")[1];
	   var fileNames = lastfix.split("*");  
	 
	   var attId = attachFileIds.split(",");
	   for(var i=0; i<fileNames.length; i++){
	     if(fileNames[i].toLowerCase().indexOf(".mht")!=-1){   
	    	 if(attId[i]){
	           ym = attId[i].substring(0, attId[i].indexOf("_"));
	         }
				return profix[i]+"_"+fileNames[i];
	     }
	   }
	}

	function isHaveMht(attachFile, attachFileIds){
	  var flag = findMht(attachFile, attachFileIds);
	  if(flag){
	     return true;
	  }
	  return false;
	}
</script>
</head>
<body onload="doInit();" topmargin="5">
<table class="TableBlock" width="100%" id="table1">
	<tr height="45px;">
      <td class="TableHeader" align="center"><span id="subject" style=""></span></td>
    </tr>
    <tr>
      <td class="TableContent" align="right" id="title">
      </td>
    </tr>
    <tr>
      <td class="TableData Content" height="150" id="content">
    
      </td>
    </tr>
    <tr align="left">
     <td class="TableData" id="showAttachment">
        <input type="hidden" id="attachmentId" name="attachmentId">
        <input type="hidden" id="attachmentName" name="attachmentName">
        <input type="hidden" id="ensize" name="ensize">
        <span id="showAtt">
        </span>
      </td>
     </tr>
</table>
  <br>
  <div id="anonymityYn">
 <table class="TableBlock" width="100%" align="center" id="table2">
 </table>
<br>
<form method="post" id="form1" name="form1">
  <table class="TableBlock" width="100%" align="center">
     <tr>
      <td class="TableHeader" colspan="2">
        <img src="<%=imgPath%>/green_arrow.gif"> 发表评论：
      </td>
    </tr>
    <tr>
      <td align="center" class="TableData">内容：</td>
      <td class="TableData">
        <textarea cols="57" id="content" name="content" rows="5" class="BigInput" wrap="on"></textarea>
      </td>
    </tr>
    <tr>
      <td align="center" class="TableData">署名：</td>
      <td class="TableData">
        <input type="radio" id="authorName" name="authorName"  checked value="0">
        <input type="text"  id="userName" name="userName" size="10" maxlength="25" class="BigStatic" value="" readonly>
        <span id="anonymityYn_span" style="display:none">
        <input type="radio" id="authorName2" name="authorName" value="nickName">昵称
        <input type="text" id="nickName" name="nickName" size="10" maxlength="25" class="BigInput" value="">
        </span>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td nowrap colspan="2">
        <input type="hidden" value="<%=seqId%>" name="newsId" id="newsId">
        <input type="hidden" value="" name="manage">
        <input type="button" id="publish" value="发表" onclick="addComment();return false;" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="查看所有评论" class="BigButtonC" onClick="javascript:window.location='reNews.jsp?seqId=<%=seqId%>';" id="showAllButton">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
      </td>
    </tr>
   </table>
  </form>
  </div>
  <div align="center">
    <input id="anonymityYnClose" style="display:none;" type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
  </div>
</body>
</html>