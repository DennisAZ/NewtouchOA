function createNewWork(flowName , par , isNotOpenWindow){
  var url = contextPath +   moduleSrcPath + "/act/YHFlowRunAct/createWork.act";
  if (par) {
    par = "flowName=" + flowName + "&" + par;
  } else {
    par = 'flowName=' + flowName;
  }
  var json = getJsonRs(url ,  par);
  if(json.rtState == "0"){
    var flowId = json.rtData.flowId;
    var runId = json.rtData.runId;
    var url2 =   contextPath +   moduleContextPath +"/flowrun/list/inputform/index.jsp?runId=" + runId + "&flowId=" + flowId + "&prcsId=1&flowPrcs=1&isNew=1";
    if (isNotOpenWindow) {
      location.href = url2;
    } else {
      window.open(url2);
    }
  
  }else{
    alert(json.rtMsrg);
  }
}
function clearPrcs(flowId , runId , prcsId , flowPrcs) {
  var url = contextPath  +   moduleSrcPath +"/act/YHDoClearAct/doPrcsClear.act";
  var par = "flowId=" + flowId + "&runId=" + runId + "&prcsId=" + prcsId + "&flowPrcs=" + flowPrcs;
  var json = getJsonRs(url , par);
}
