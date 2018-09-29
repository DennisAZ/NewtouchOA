function delDocAction() {
  var param = 'runId=' + runId;
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocAct/delDoc.act";
  var json = getJsonRs(url , param);
  if(json.rtState == '0'){
    _docId = "";
    _docName = "";
  }
}
/**
 * 工作办理界面对取消进行重写,主要是支持删除工作时同时删除正文
 */
var C = window.cancel;
window.cancel = B;
function B() {
  C();
  delDocAction();
}
function turn() {
  isNew = 0;
  mouse_is_out = false;
  parent.location = contextPath + '/core/funcs/doc/flowrun/list/turn/turnnext.jsp?skin='+skin+'&sortId='+sortId+'&runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs;
}
//对保存会签方法重写var mySaveFeedback = window.saveFeedback;
window.saveFeedback = oldSaveFeedback;
function oldSaveFeedback(seqId , flag) {
  mySaveFeedback(seqId , flag);
  if (docCreate) {
    var param = 'runId=' + runId
    + '&flowId=' + flowId 
    + '&prcsId=' + prcsId;
    if(seqId){
      param += "&seqId=" + seqId; 
    }
    var url =  contextPath + "/yh/core/funcs/doc/send/act/YHDocAct/saveDocCreateTime.act";
    getJsonRs(url , param);
  }
}