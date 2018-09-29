<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link href="<%=contextPath %>/subsys/portal/guoyan/style/css/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/leaderact/js/leaderact.js"></script>
<title>Insert title here</title>
<script type="text/javascript"><!--
var pageSize = 5;
var totalRecord  = 0;
var pageInfoS = null;
function doInit(){
  totalRecord = loadleaderData(0,pageSize);
  var cfgs = {
      dataAction: "",
      container: "pageInfo",
      pageSize:pageSize,
      loadData:loadDataAction,
      totalRecord:totalRecord
    };
  pageInfoS = new YHJsPagePilot(cfgs);
  pageInfoS.show();
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
  totalRecord = loadleaderData(pageNo,pageSize);
}
--></script>
</head>
<body onload=doInit()>
      <table width="225" border="0" cellpadding="0" cellspacing="0" class="border" style="width: 664px">
        <tr>
          <td width="10" align="center" background="<%=contextPath %>/subsys/portal/guoyan/style/img/menu_bg1.gif" class="font_01"><img src="<%=contextPath %>/subsys/portal/guoyan/style/img/icon_keyword.gif" width="16" height="16" /></td>
          <td width="158" height="25" background="<%=contextPath %>/subsys/portal/guoyan/style/img/menu_bg1.gif" class="font_01"> 领导出行</td>
          <td width="10" background="<%=contextPath %>/subsys/portal/guoyan/style/img/menu_bg1.gif" class="font_01"><a href="<%=contextPath %>/subsys/portal/guoyan/leaderact/list.jsp"><img src="<%=contextPath %>/subsys/portal/guoyan/style/img/more.gif" width="29" height="11" border="0" /></a></td>
        </tr>
        <tr>
          <td height="150" colspan="3">
          <table width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td height="5" colspan="2"></td>
            </tr>
            <tbody id="dataContent"></tbody>
          </table>
          </td>
        </tr>
        <tr>
        <td colspan="3">
          <div id="pageInfo"></div>
          </td>
         </tr>
      </table>

</body>
</html>