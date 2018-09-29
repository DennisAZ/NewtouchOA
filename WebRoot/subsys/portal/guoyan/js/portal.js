function loadNotify1Data(cntrlId,notifyType){
  var param = "type=" + notifyType +"&limit=8"+"&paramName=newsId,subject,newsTime&start=0&orderBy=SEND_TIME&sort=desc";
  var url =  contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridModuleNotify/loadGridData.act";
  var rtJson = getJsonRs(url,param);
  var data = rtJson;
  var href = contextPath + "/subsys/portal/guoyan/por/modules/notify/article.jsp";
  setDataToLi(data,cntrlId,href);
}
function loadLeaderData(cntrlId,notifyType){
  var param = "type=" + notifyType +"&limit=8"+"&paramName=newsId,subject,newsTime&start=0&orderBy=NEWS_TIME&sort=desc";
  var url =  contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridModule/loadGridData.act";
  var rtJson = getJsonRs(url,param);
  var data = rtJson;
  var href = contextPath + "/subsys/portal/guoyan/por/modules/leaderact/article.jsp";
  setDataToLi(data,cntrlId,href);
}
/**
 *修改于20101019
 * @param data
 * @param cntrlId
 * @param href
 * @param strLen
 * @param showDate
 * @return
 */
function setDataToLi(data,cntrlId,href,strLen,showDate){
  $(cntrlId).innerHTML = "";
  for(var i = 0 ; i < data.length; i++){
    var news = data[i];
    var newId = news.newId;
    var subject =  news.subject;
    var newsTime = news.newsTime.substring(0,10);
    var hrefUrl = href + "?newsId=" + newId;
    var showSub = subject;
    if(!strLen){
      strLen = 14;
    }
    if(subject.length > strLen){
      showSub = subject.substring(0,strLen) + "...";
    }
    var dateStr = "";
    if(showDate){
      var newsTime = news.newsTime.substring(0,10);
      dateStr = newsTime;
    }
    var liHtml = "<li><a href=\"" + hrefUrl + "\" title=\""+ subject +"\"> " + showSub + "</a> <span>" + dateStr + "</span></li>";
    $(cntrlId).insert(liHtml,"bottom");
  }
}
/**
 * 修改于20101019
 * @param cntrlId
 * @param ruleName
 * @param strLen
 * @param showDate
 * @return
 */
function loadLeaderActive(cntrlId,ruleName,strLen,showDate){
  var param = "start=0&ruleName=" + ruleName;
  var url =  contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridNormal/loadGridData.act";
  var rtJson = getJsonRs(url,param);
  var data = rtJson;
  var href = contextPath + "/subsys/portal/guoyan/por/modules/leaderact/article.jsp";
  setDataToLi(data,cntrlId,href,strLen,showDate);
}
/**
*一周情况
*/
function loadWeekNews(cntrlId,ruleName,strLen,showDate){
  var param = "start=0&ruleName=" + ruleName;
  var url =  contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridNormal/loadGridData.act";
  var rtJson = getJsonRs(url,param);
  var data = rtJson;
  var href = contextPath + "/subsys/portal/guoyan/por/modules/zxxx/zxxx.jsp";
  setDataToLi(data,cntrlId,href,strLen,showDate);
}
function getUserIdByName(userName){
  var param = "userName=" + userName; 
  var url =  contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridModule/getUserIdByName.act";
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }else{
    return "";
  }
}
function getDeptIdByName(deptName){
  var param = "deptName=" + deptName; 
  var url =  contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridModule/getDeptIdByName.act";
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }else{
    return "-1";
  }
}
function getUserNameById(userId){
  var param = "userId=" + userId; 
  var url =  contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridModule/getUserNameById.act";
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }else{
    return "-1";
  }
}

function autoHeight2(obj){
  obj = $(obj);
  if(!obj){
    return;
  }
  var id = obj.id;
  var subWeb = document.frames ? document.frames[id].document : obj.contentDocument;   
  if(obj != null && subWeb != null)
  {  
    var autoS = 600;
     if( subWeb.body.scrollHeight > 600){
       autoS = subWeb.body.scrollHeight;
     }
     obj.height = autoS + 'px';
  }   
}

function imageHandler(content){
  var url = contextPath + "/yh/subsys/portal/guoyan/module/act/YHPortalGridModule/downloadImage.act";
  var contentHandling = content.replace(/\/inc\/attach.php/g,url);
  return contentHandling;
}