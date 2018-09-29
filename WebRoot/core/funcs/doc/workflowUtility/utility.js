/**
 * 打开流程设计器
 * @param flowId
 * @return
 */
function openFlowDesign(flowId) {
  var url = contextPath + moduleContextPath + "/flowdesign/index.jsp?flowId=" + flowId;
  window.open(url,"flow_design","height=600,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=10,left=10,resizable=yes");
 // showWindow(url,'流程设计器',800,600);
}
/**
 * 预览表单
 * @param formId
 * @return
 */
function viewForm(formId) { 
  var url = contextPath  + moduleContextPath + "/flowform/formView.jsp?seqId=" + formId;
  window.open(url ,"form_view","menubar=0,toolbar=0,status=0,resizable=1,left=0,top=0,scrollbars=1,width="+(screen.availWidth-10)+",height="+(screen.availHeight-50)+"\"");
}
/**
 * 打开表单设计器
 * @param formId
 * @return
 */
function openFormDesign(formId) {
  var url = contextPath  + moduleContextPath + "/flowform/main2.jsp?seqId=" + formId;
  window.open(url,"form_design","menubar=0,toolbar=0,scrollbars=no,status=0,resizable=1");
}
/**
 * 添加或者修改步骤
 * @param flowId
 * @param prcsId
 * @return
 */
function editProcess(flowId , prcsId) {
  var url = contextPath + moduleContextPath + "/flowdesign/viewlist/setproperty/index.jsp?flowId="+flowId;
  var title = "添加步骤";
  if (prcsId) {
    url += "&seqId=" + prcsId;
    title = "修改步骤";
  }
  loc_x=(screen.availWidth-700)/2;
  loc_y=(screen.availHeight-700)/2;
  window.open(url,"set_process","height=500,width=850,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}
/**
 * 设置步骤相关项
 * @param url
 * @return
 */
function loadProcessWindow(url , title){
  loc_x=(screen.availWidth-700)/2;
  loc_y=(screen.availHeight-700)/2;
  window.open(url,"set_process","height=500,width=700,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}
/**
 * 关闭SetProcess窗口
 * @return
 */
function closeWindow() {
  //if (opener) {
    //opener.location.reload();
  //}
  window.close();
}
/**
 * 查看流程图
 * @param flowId
 * @return
 */
function viewGraph(flowId) {
  var url = contextPath  + moduleContextPath + "/flowrun/list/viewgraph/index.jsp?flowId=" + flowId;
  if (window.navigator.appVersion.match(/9./i) =='9.') {
    url = contextPath + "/core/funcs/doc/flowrun/list/viewgraph/index3.jsp?flowId=" + flowId;
 }
  myleft=(screen.availWidth-800)/2;
  window.open(url,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=500,left="+myleft+",top=50");
}
/**
 * 打印表单
 * @param runId
 * @param flowId
 * @return
 */
function formView(runId , flowId) {
  var url = contextPath  + moduleContextPath + "/flowrun/list/print/index.jsp?runId="+runId+"&flowId="+flowId;
  window.open(url ,"","status=0,toolbar=no,menubar=no,width="+(screen.availWidth-12)+",height="+(screen.availHeight-38)+",location=no,scrollbars=yes,resizable=yes,left=0,top=0");
}
 /**
  * 打印收文表单
  * @param runId
  * @param flowId
  * @return
  */
 function formView2(runId , flowId) {
   var url = contextPath  + moduleContextPath + "/flowrunRec/list/print/index.jsp?runId="+runId+"&flowId="+flowId;
   window.open(url ,"","status=0,toolbar=no,menubar=no,width="+(screen.availWidth-12)+",height="+(screen.availHeight-38)+",location=no,scrollbars=yes,resizable=yes,left=0,top=0");
 }
/**
 * 打印表单
 * @param runId
 * @param flowId
 * @return
 */
function formViewByRunId(runId , flowName) {
  var url = contextPath  + moduleSrcPath + "/act/YHFlowRunAct/getFlowIdByRunId.act";
  var  par = 'runId=' + runId;
  var json = getJsonRs(url ,  par);
  if (json.rtState == '0') {
    var flowId = json.rtData ;
    formView2(runId , flowId);
  }
}
/**
 * 实际流程图
 * @param runId
 * @param flowId
 * @param title
 * @return
 */
function flowView(runId , flowId , title , sortId , skin , flag) {
  var url = contextPath  + moduleContextPath + "/flowrun/list/flowview/index.jsp?runId="+runId+"&flowId="+flowId;
  if (sortId) {
    url += "&sortId=" + sortId; 
  }
  if (skin) {
    url += "&skin=" + skin; 
  }
  if (flag) {
    url += "&flag=" + flag; 
  }
  
  
  myleft=(screen.availWidth-800)/2;
  window.open(url,runId,"status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=950,height=400,left="+myleft+",top=100");
}
function showSign(feedId) {
  var url = contextPath  + moduleContextPath + "/flowrun/list/signInfo.jsp?feedId=" + feedId;
  openDialog(url,  800, 600);
  //showModalWindow(url , "会签意见(手写或签章)" , "signWindow" ,800,600 , false);
}
var imgEnd = ['gif' , 'png' , 'jpg' , 'bmp'];
var docEnd = ['doc' , 'docx' , 'xlsx' , 'xls' , 'ppt' , 'pptx'];
/**
 * 附件显示时的图标
 * @param ext
 * @return
 */
function getAttachImage(ext) {
  var imgSrc = "";
  if(ext == 'gif' || ext == 'jpg' || ext == 'bmp' || ext == 'png'){
    imgSrc = imgPath + "/gif.gif";
  }else if(ext == 'doc' || ext == 'docx'){
    imgSrc = imgPath + "/doc.gif";
  }else if(ext == 'xlsx' || ext == 'xls'){
    imgSrc = imgPath + "/xls.gif";
  }else if(ext == 'ppt' || ext == 'pptx'){
    imgSrc = imgPath + "/ppt.gif";
  }else if(ext == 'exe'){
    imgSrc = imgPath + "/exe.gif";
  }else if(ext == 'chm'){
    imgSrc = imgPath + "/chm.gif";
  }else if(ext== 'txt'){
    imgSrc = imgPath + "/txt.gif";
  }else if (ext == 'rar' || ext == 'zip') {
    imgSrc = imgPath + "/rar.gif";
  } else{
    imgSrc = imgPath + "/defaut.gif";
  }
  return imgSrc;
}
/**
 * 阅读
 * @return
 */
function readAction(){
  var down = arguments[3];
  var attachmentId = down.attachmentId;
  var attachmentName = down.attachmentName;
  office(attachmentName,attachmentId,"doc",7);
}
/**
 * 打印
 * @return
 */
function printAction() {
  var down = arguments[3];
  var attachmentId = down.attachmentId;
  var attachmentName = down.attachmentName;
  office(attachmentName,attachmentId,"doc",5,"",1);
}
/**
 * 编辑
 * @return
 */
function editAction(){
  var down = arguments[3];
  var attachmentId = down.attachmentId;
  var attachmentName = down.attachmentName;
  office(attachmentName,attachmentId,"doc",4);
}
/**
 * 下载
 * @return
 */
function downAction(){
  var down = arguments[3];
  var attachmentId = down.attachmentId;
  var attachmentName = down.attachmentName;
  downLoadFile(attachmentName ,  attachmentId, "doc");
}

/**
 * 转存
 * @return
 */
function saveAction(){
  var down = arguments[3];
  var attachmentId = down.attachmentId;
  var attachmentName = down.attachmentName;
  archived(attachmentName,attachmentId,"doc");
}
/**
 * 转存处理函数
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function archived(attachName,attachId,moudle){
  var URL = contextPath + "/core/funcs/savefile/index.jsp?attachId=" + attachId + "&attachName=" + attachName +"&module=" + moudle;
  var loc_x = screen.availWidth/2-200;
  var loc_y = screen.availHeight/2-90;
  window.open(encodeURI(URL),null,"height=180,width=400,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}
/**
 * 预览
 * @return
 */
function previewAction(){
  
} 
/**
 * 播放
 * @return
 */
function playAction(){
  var down = arguments[3];
  var attachId = down.attachmentId;
  var attachName = down.attachmentName;
  var mediaType = isMedia(attachName);
  var video = isVideo(attachName);
  var param = encodeURI("attachmentName=" + attachName + "&attachmentId=" + attachId + "&moudle=workflow&mediaType=" + mediaType + "&video=" + video);
  var url = contextPath + "/core/module/mediaplayer/index.jsp?" + param;
  url = encodeURI(url);
  openWindow(url,'在线播放器',900,600);
}

function isMedia(fileName){
  var MEDIA_REAL_TYPE = ["rm", "rmvb","ram","ra", "mpa", "mpv", "mps","m2v", "m1v", "mpe", "mov", "smi"]
  var MEDIA_MS_TYPE = ["wmv", "asf", "mp3", "mpg", "mpeg", "mp4", "avi", "wmv", "wma", "wav", "dat"];
  var MEDIA_FLASH_TYPE = ["flv", "fla"];
  var DIRECT_VIEW_TYPE = ["jpg", "jpeg", "bmp", "gif", "png", "xml", "xhtml", "html", "htm", "mid", "mht", "pdf", "swf"];
  var index = fileName.lastIndexOf(".");
  var extName = "";

  if (index >= 0) {
    extName = fileName.substring(index + 1).toLowerCase();
  }
  if(MEDIA_REAL_TYPE.contains(extName))
     return 1;
  if(MEDIA_MS_TYPE.contains(extName))
     return 2;
  if(MEDIA_FLASH_TYPE.contains(extName))
     return 4;
  if(DIRECT_VIEW_TYPE.contains(extName))
     return 3;
  return 0;
}

function isVideo(fileName){
 var videoType = ["mp4", "mpg","mpeg","avi", "wmv", "asf", "dat"];
 var index = fileName.lastIndexOf(".");
 var extName = "";
 if (index >= 0) {
   extName = fileName.substring(index + 1).toLowerCase();
 }
 if(videoType.contains(extName)){
   return 1;
 }else{
  return 0;
 }
}
function getSortIdByName(sortName){
  var url = contextPath  + moduleSrcPath + "/act/YHFlowSortAct/getSortId.act";
  var json = getJsonRs(url , "sortName=" + sortName);
  if(json.rtState == "0"){
    return json.rtData;
  }
  return '0';
}
function getSortIdsByName(sortName){
  if (sortName) {
    var url = contextPath  + moduleSrcPath + "/act/YHFlowSortAct/getSortIds.act";
    var json = getJsonRs(url , "sortName=" + sortName);
    if(json.rtState == "0"){
      return json.rtData;
    }
  } else {
    return "";
  }
}

function check_condition_formula(inputObj) {
  var formula = inputObj.value;
  var tmpLeftArray = formula.split("[");
  var tmpRightArray = formula.split("]");
  if (tmpLeftArray.length != tmpRightArray.length) {
    alert("条件公式左右中括号个数不相等！");
    return;
  }

  var tmpLeftArray = formula.split("{");
  var tmpRightArray = formula.split("}");
  
  if (tmpLeftArray.length != tmpRightArray.length) {
    alert("条件公式左右大括号个数不相等！");
    return;
  }
} 
