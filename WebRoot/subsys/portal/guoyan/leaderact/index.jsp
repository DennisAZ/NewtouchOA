<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="<%=contextPath %>/subsys/portal/guoyan/style/css/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/leaderact/js/leaderact.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
var pageSize = 5;
var totalRecord  = 0;
function doInit(){
  loadleaderData(0,pageSize);
}

function loadData(pageIndex,pageSize){
  var param = "pageSize=" + pageSize + "&pageIndex=" + pageIndex;
  var url =  contextPath + "/yh/subsys/portal/guoyan/leaderact/act/YHLeaderactAct/listDataPage.act";
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    $('dataContent').innerHTML = "";
    for(var i = 0 ; i < data.pageData.length; i++){
      var news = data.pageData[i];
      var newId = news.newId;
      var subject =  news.subject;
      var newsTime = news.newsTime.substring(0,10);
      var liHtml = "<li><a href=\"readNews.jsp?newId=" + newId + "\"> " + subject + "</a>   " + newsTime + "</li>";
      $('dataContent').insert(liHtml,"bottom");
    }
  }
  return data.totalRecord;
}
function loadDataAction(obj){
  var pageNo = obj.pageIndex;
  var pageSize = obj.pageSize;
  loadleaderData(pageNo,pageSize);
}
</script>
</head>
<body onload=doInit()>
  <ul id="dataContent"> </ul>
</body>
</html>