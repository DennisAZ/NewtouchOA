<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="oa.core.funcs.bbs.act.BbsService,oa.core.funcs.bbs.act.BbsComment,oa.core.funcs.bbs.act.BBSUtil" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html><head><script type="text/javascript">
/** 常量定义 **/
var TDJSCONST = {
  YES: 1,
  NO: 0
};
/** 变量定义 **/
var contextPath = "/yh";
var imgPath = "/yh/core/styles/style1/img";
var ssoUrlGPower = "";
var limitUploadFiles = "jsp,java,jspx,exe"
var signFileServiceUrl = "http://192.168.0.5:9000/BjfaoWeb/TitleSign";
var isOnlineEval = "0";
var useSearchFunc = "1";
var maxUploadSize = 500;
var isDev = "0";
var ostheme = "1";
</script>




<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看bbs</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/yh/core/styles/style1/css/style.css">
<link rel="stylesheet" href="/yh/core/styles/style1/css/cmp/tab.css" type="text/css">
<script type="text/javascript" src="/yh/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="/yh/core/js/prototype.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="/yh/core/js/datastructs.js"></script>
<script type="text/javascript" src="/yh/core/js/sys.js"></script>
<script type="text/javascript" src="/yh/core/js/smartclient.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/attachMenu.js"></script>

<script type="text/javascript">
var seqId = "1979";
var userSeqId = null;
var newsId = null;
var userLoginName = "admin";
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
		    attachFrame = "<iframe id=mhtFrame src='/yh/getFile?module=news&ym="+ym+"&attachFile=" + encodeURIComponent(findMht(attachFile, rtData.attachmentId)) + "'  width='100%' height='100%' style='overflow:auto'></iframe>";
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
<body onload="" topmargin="5">

<%
HttpSession s =request.getSession();

Object o=s.getAttribute("LOGIN_USER");
String boardName=request.getParameter("boardName");
String boardId=request.getParameter("boardId");
String commentId=request.getParameter("commentId");

BbsComment bbsC=new BbsService().getUserBBSCommentDetailByCommentId(request,o,commentId);
List bdlist=bbsC.getReplyComments();
%>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tbody><tr>
   <td background="/yh/core/funcs/system/netdisk/images/dian1.gif" width="100%"></td>
 </tr>
</tbody></table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tbody><tr>
    <td class="Big"><img src="/yh/core/funcs/system/netdisk/images/notify_open.gif" align="middle"><span class="big3">讨论区>><%=request.getParameter("boardName")%>>>查看帖子</span>
    </td>
  </tr>
</tbody></table>
<table class="TableBlock" width="100%" id="table1">
	<tbody>
	<tr height="45px;">
      <td class="" align="right">
      <span>
      <%String hoster=request.getSession().getAttribute(BBSUtil.bbsHoster)==null?null:request.getSession().getAttribute(BBSUtil.bbsHoster).toString();%>
      <%if(hoster!=null && hoster.equals("1")){ %>
      <%if(bbsC.getLockYn().equals("0")) {%>
      <input type="button" id="st" value="锁贴" onclick="javascript:window.location.href='commentDetail.jsp?flag=l&&commentId=<%=bbsC.getCommentId()%>&&boardId=<%=request.getParameter("boardId")%>&&boardName=<%=request.getParameter("boardName")%>'" class="BigButton">&nbsp;&nbsp;
        <%}else{ %>
      <input type="button" id="st" value="解锁" onclick="javascript:window.location.href='commentDetail.jsp?flag=l&&commentId=<%=bbsC.getCommentId()%>&&boardId=<%=request.getParameter("boardId")%>&&boardName=<%=request.getParameter("boardName")%>'" class="BigButton">&nbsp;&nbsp;
     <%} 
     }
     %>
        <input type="button" value="返回" class="BigButton" onclick="window.location.href='bbsBoardComment.jsp?boardId=<%=request.getParameter("boardId")%>&&boardName=<%=request.getParameter("boardName")%>'"></span></td>
    </tr>
	<tr height="45px;">
      <td class="TableHeader" align="left"><span id="subject" style="font-family: 字体; ">
      
      标题：<%=bbsC.getSubject() %></span> </td>
    </tr>
    <tr>
      <td class="TableContent" align="right" id="title"><u> <%=bbsC.getAuthorName() %></u>&nbsp;&nbsp;发布于：<i> <%=bbsC.getSubmitTime() %></i>&nbsp;&nbsp;点击 <%=bbsC.getReadCont() %>次</td>
    </tr>
    <tr>
      <td class="TableData Content" height="150" id="content">
      <%=bbsC.getContent() %>
  
      </td>
    </tr>
    <tr align="left">
     <td class="TableData" id="showAttachment">
        <div>
       
        
        &nbsp;
        <%String []aid=bbsC.getAttachmentIdList();
         String []ain=bbsC.getAttachmentNameList();
         
          if(aid!=null && ain!=null && aid.length>0){
           int j=aid.length;
          if(aid.length>ain.length){
          	j=ain.length;
          }
          for(int i=0;i<j;i++){
          if(ain[i]!=null && !ain[i].equals(""))
         { 
          String attEnd=BBSUtil.returnGif(ain[i]);
         %>
         <img src="/yh/core/styles/style1/img/fileExt/<%=attEnd%>.gif" style="width:12px;height:12px">
         <a href="down.jsp?attachId=<%=aid[i]%>&&attachName=<%=ain[i]%>&&attEnd=<%=attEnd%>">
           <%=ain[i]%>
         </a>
         <br>
        <%
         if(attEnd.equalsIgnoreCase("jpeg") || attEnd.equalsIgnoreCase("jpg") || attEnd.equalsIgnoreCase("gif") || attEnd.equalsIgnoreCase("png"))
         {
         %>
         <img src="image.jsp?attachId=<%=aid[i]%>&&attachName=<%=ain[i]%>&&attEnd=<%=attEnd%>" border="0" width="600">
         <br>
         <%}
        
        }
        }
        }
         %>
        
        </div>
        
      </td>
     </tr>
 <%if(bbsC.getLockYn().equalsIgnoreCase("0")){ %>
     <tr><td class="TableControl" align="right">
 <a href="addBBS.jsp?commentId=<%=commentId%>&&boardId=<%=boardId%>&&boardName=<%=boardName%>&&subject=<%=bbsC.getSubject()%>" style="text-decoration:underline">回复本贴</a>
  <%if(bbsC.getIsAuthor()==1){
    if(Integer.parseInt(bbsC.getReplyCont())==0){
  %>
  
  &nbsp;&nbsp;&nbsp;
 <a href="editBBS.jsp?commentId=<%=commentId%>&&boardId=<%=boardId%>&&boardName=<%=boardName%>">编辑</a>
 &nbsp;&nbsp;&nbsp;
 <a href="deleteComment.jsp?commentId=<%=bbsC.getCommentId()%>&&boardId=<%=boardId%>&&boardName=<%=boardName%>&&flag=c">删除</a>
<%
}
}%>
</td>
 </tr>
 <%}%>
</tbody></table>
  <br>
  <div id="anonymityYn">
 <table class="TableBlock" width="100%" align="center" id="table2">
 <tbody>
 <%for(int i=0;i<bdlist.size();i++){
 BbsComment bbsR=(BbsComment)bdlist.get(i);
 %>
 <tr><td class="TableContent">&nbsp;&nbsp;
 <u style="cursor:hand"><%=(i+1)+"#  "+bbsR.getAuthorName()%></u>
 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发表时间：<%=bbsR.getSubmitTime()%></td>
 </tr>
 <tr>
 
 <td class="TableData">&nbsp;
  <%=bbsR.getSubject()%>
  <p>
 <%=bbsR.getContent()%>
 <p>
  &nbsp;
        <%String []rid=bbsR.getAttachmentIdList();
         String []rin=bbsR.getAttachmentNameList();
         
          if(rid!=null && rin!=null && rid.length>0){
           int j=rid.length;
          if(rid.length>rin.length){
          	j=rin.length;
          }
          for(int p=0;p<j;p++){
          if(rin[p]!=null && !rin[p].equals(""))
         { 
          String attEnd=BBSUtil.returnGif(rin[p]);
         %>
         <img src="/yh/core/styles/style1/img/fileExt/<%=attEnd%>.gif" style="width:12px;height:12px">
         <a href="down.jsp?attachId=<%=rid[p]%>&&attachName=<%=rin[p]%>&&attEnd=<%=attEnd%>">
           <%=rin[p]%>
         </a>
         <br>
        <%
         if(attEnd.equalsIgnoreCase("jpeg") || attEnd.equalsIgnoreCase("jpg") || attEnd.equalsIgnoreCase("gif") || attEnd.equalsIgnoreCase("png"))
         {
         %>
         <img src="image.jsp?attachId=<%=rid[p]%>&&attachName=<%=rin[p]%>&&attEnd=<%=attEnd%>" border="0" width="600">
         <br>
         <%}
        
        }
        }
        }
         %>
 </td>
 
 </tr>
  <%if(bbsC.getLockYn().equalsIgnoreCase("0")){ %>
 <tr><td class="TableControl" align="right">
 <a href="addBBS.jsp?commentId=<%=commentId%>&&boardId=<%=boardId%>&&boardName=<%=boardName%>&&subject=<%=bbsC.getSubject()%>" style="text-decoration:underline">回复本贴</a>
   <%if(bbsR.getIsAuthor()==1){%>
   &nbsp;&nbsp;&nbsp;
 <a href="editBBS.jsp?commentId=<%=bbsR.getCommentId()%>&&boardId=<%=boardId%>&&boardName=<%=boardName%>">编辑</a>
 &nbsp;&nbsp;&nbsp;
 <a href="deleteComment.jsp?commentId=<%=bbsR.getCommentId()%>&&parent=<%=bbsR.getParent()%>&&boardId=<%=boardId%>&&boardName=<%=boardName%>&&flag=r">删除</a>
 <%}%>
 </td>
 </tr>
 
 <%}
 } %>
 </tbody></table>
<br>
 <%if(bbsC.getLockYn().equalsIgnoreCase("0")){ %>
<form enctype="multipart/form-data" action="addBBSSubmit.jsp" method="post" name="bbsForm" id="bbsForm">
<input type="hidden" name="boardId" id="boardId" value="<%=request.getParameter("boardId") %>"/>
<input type="hidden" name="boardName" id="boardName" value="<%=request.getParameter("boardName") %>"/>
<input type="hidden" value="<%=commentId %>" name="parent" id="parent">
  <table class="TableBlock" width="100%" align="center">
     <tbody><tr>
      <td class="TableHeader" colspan="2">
        <img src="/yh/core/styles/style1/img/green_arrow.gif"> 快速回复：

      </td>
    </tr>
    <tr>
      <td align="center" class="TableData">标题：</td>
      <td class="TableData">
         <input type="text" name="subject" id="subject" size="55" maxlength="200" value="Re:<%=bbsC.getSubject()%>">
      </td>
    </tr>
    <tr>
      <td align="center" class="TableData">内容：</td>
      <td class="TableData">
        <textarea cols="57" id="Econtent" name="Econtent" rows="5" class="BigInput" wrap="on"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap="" class="TableData">署名：</td>
      <td class="TableData">
      <%String anony=(request.getSession().getAttribute(BBSUtil.bbsAnonyKey)).toString(); %>
        <input type="radio" name="AUTHOR_NAME" value="1" checked onclick="set_name(1)">
        <input type="text" name="USER_NAME" size="10" maxlength="25" class="BigStatic" value="<%=BBSUtil.getSessionProperty(request,"userName")%>" readonly="">
        <%if(anony.equals("1")){ %>
        <input type="radio" name="AUTHOR_NAME" value="2" checked="" onclick="set_name(2)">昵称
        <input type="text" name="NICK_NAME" size="10" maxlength="25" class="BigInput" value="<%=BBSUtil.getSessionProperty(request,"userName")%>">
        <%} %>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td nowrap="" colspan="2">
        <input type="button" id="publish" value="回复" onclick="bbsForm.submit();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="window.location.href='bbsBoardComment.jsp?boardId=<%=request.getParameter("boardId")%>&&boardName=<%=request.getParameter("boardName")%>'">
      </td>
    </tr>
   </tbody></table>
  </form>
  <%}%>
  </div>


</body></html>