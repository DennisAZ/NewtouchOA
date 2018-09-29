function delDocAction() {
  var param = 'runId=' + runId;
  var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDocAct/delDoc.act";
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
/**
 * 对转交下一步进行重写。

 */
var TN = window.turnNextPage;
window.turnNextPage = TN2;
function TN2() {
  if (isPigeonhole && !hasPigeonhole) {
    top.win = window;
    top.dd = new top.YH.Window({
      'title' : '归档',
     'draggable': false,
      'type': 'iframe',
      'width': 800,
      'height': 400,
      'modal':true,
      'src': contextPath + "/subsys/inforesource/docmgr/roll/newFile.jsp?runId=" + runId +"&prcsId=" + prcsId
    });
    top.dd.show();
  } else {
    turn();
  }
}
function closePage() {
  top.dd.destroy();
  turn();
}
function turn() {
  isNew = 0;
  mouse_is_out = false;
  parent.location = contextPath + '/core/funcs/workflow/flowrun/list/turn/turnnext.jsp?skin='+skin+'&sortId='+sortId+'&runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs;
}
//对保存会签方法重写
var mySaveFeedback = window.saveFeedback;
window.saveFeedback = oldSaveFeedback;
function oldSaveFeedback(seqId) {
  mySaveFeedback(seqId);
  if (docCreate) {
    var param = 'runId=' + runId
    + '&flowId=' + flowId 
    + '&prcsId=' + prcsId;
    if(seqId){
      param += "&seqId=" + seqId; 
    }
    var url =  contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDocAct/saveDocCreateTime.act";
    getJsonRs(url , param);
  }
}