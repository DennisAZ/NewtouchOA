var pageSize = 5;
function loadleaderData(pageIndex){
  if(!pageIndex){
    pageIndex = 0;
  }
  var param = "pageSize=" + pageSize + "&pageIndex=" + pageIndex;
  var url =  contextPath + "/yh/subsys/portal/guoyan/leaderact/act/YHLeaderactAct/listDataPage.act";
  var rtJson = getJsonRs(url,param);
  alert(rsText);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    var obj = $('dataContent');
    //$('dataContent').innerHTML = "";
    for(var i = 0 ; i < data.pageData.length; i++){
      var news = data.pageData[i];
      var newId = news.newId;
      var subject =  news.subject;
      var newsTime = news.newsTime.substring(0,10);
      var readUrl = contextPath + "/subsys/portal/guoyan/leaderact/readNews.jsp?newId=" + newId;
      var liHtml = "<tr><td height=\"20\">· <a href=\"" + readUrl + "\"> " + subject + "</a>  </td> <td class=\"font_03\"> " + newsTime + "</td></tr>";
      $('dataContent').insert(liHtml,"bottom");
    }
  }
  return data.totalRecord;
}