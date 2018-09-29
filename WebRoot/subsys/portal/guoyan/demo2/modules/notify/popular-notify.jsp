<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<% 
 String type = request.getParameter("typeId");
 String otherType = "1"; 
 if(type.equals("1")){
   otherType = "2";
 }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>无标题文档</title>
<link href="../../style/css/css.css" rel="stylesheet" type="text/css" />
<link href="../../style/css/css-other.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/page.css">

<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}
-->
</style>

<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/portal.js"></script>
<title>Insert title here</title>
<script type="text/javascript"><!--
var pageSize = 5;
var totalRecord  = 0;
var pageInfoS = null;
var type = '<%=type%>';
function doInit(){
  if(type == 1){
    $('title').innerHTML = "公告通知";
    $('yzvtz-til').innerHTML = "一周情况";
   }else{
     $('title').innerHTML = "一周情况";
     $('yzvtz-til').innerHTML = "公告通知";
   }
  totalRecord = loadData(0,pageSize);
  var cfgs = {
      dataAction: "",
      container: "pageInfo",
      pageSize:pageSize,
      loadData:loadDataAction,
      totalRecord:totalRecord
    };
  pageInfoS = new YHJsPagePilot(cfgs);
  if(totalRecord > 0){
    pageInfoS.show();
  }
  loadLeaderData('ldhd',7);
  loadNotify1Data('yzvtz','<%=otherType%>');
}

function loadData(pageIndex,pageSize){
  var param = "pageSize=" + pageSize + "&pageIndex=" + pageIndex + "&newsType=<%=type%>";
  var url =  contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridModuleNotify/loadDataPage.act";
  var rtJson = getJsonRs(url,param);
  //alert(rsText);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    $('dataContent').innerHTML = "";
    for(var i = 0 ; i < data.pageData.length; i++){
      var news = data.pageData[i];
      var newId = news.newId;
      var subject =  news.subject;
      var newsTime = news.newsTime.substring(0,10);
      var hrefUrl = "article.jsp?newsId=" + newId;
      var liHtml = "<li><a href=\"" + hrefUrl + "\"> " + subject + "</a>   " + newsTime + "</li>";
      $('dataContent').insert(liHtml,"bottom");
    }
  }
  return data.totalRecord;
}

function loadDataAction(obj){
  var pageNo = obj.pageIndex;
  var pageSize = obj.pageSize;
  totalRecord = loadData(pageNo,pageSize);
}
</script>
</head>

<body onload="doInit()">
  <div class="frame-box-three">
     <div class="box-three-a">
	   <div class="three-a-up">
         <span class="aweektxt" id="title">一周情况</span>
	   </div>
	     <div class="three-a-down" >
		    <ul id="dataContent">
			</ul>
		  <div class="next-page" id="pageInfo"></div>
		 </div>
	 </div>
	 <div class="box-three-b">
	  <div class="box-three-b-popular">
	     <div class="popular-up">
		    <span class="popular-span-tit">领导活动</span>
			<a href="../leaderact/popular-leadnew.jsp" class="popular-span-more"><img src="../../images/more.gif" width="36" height="17" border="0" /></a>		 </div>
		 <div class="clear"></div>
		 <div class="popular-dw">
		     <ul id="ldhd">
			   
			 </ul>
		 </div>
	  </div>
	  <div class="box-13px"></div>
	  <div class="box-three-b-popular">
	      <div class="popular-up">
		    <span class="popular-span-tit" id="yzvtz-til"></span>
			<a href="popular-notify.jsp?typeId=<%=otherType %>" class="popular-span-more"><img src="../../images/more.gif" width="36" height="17" border="0" /></a>
		 </div>
		 <div class="clear"></div>
		 <div class="popular-dw">
		     <ul id="yzvtz">
			 </ul>
		  </div>
	  </div>
     </div>
</div>

</body>
</html>
