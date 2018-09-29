var baseContentUrl = contextPath + moduleContextPath + "/flowrun/list/inputform";
var requestUrl = contextPath +moduleSrcPath+"/act/YHWorkHandleAct";
var isOnloadFinish = false;//是否加载完成
/**
 * 初始化函数
 * @return
 */
function doInit(){
 var jso = [
             {title:"表单", useTextContent:true, onload:showDiv.bind(window, "formDiv"), contentUrl:"", imgUrl:imgPath + "/form.gif", useIframe:false}
             ,{title:"附件", useTextContent:true, onload:showDiv.bind(window, "attachDiv"), contentUrl:"", imgUrl:imgPath +  "/attach.gif", useIframe:false}
             ,{title:"会签", useTextContent:true, onload:showDiv.bind(window, "feedBackDiv"), contentUrl:"", imgUrl:imgPath +  "/sign.gif", useIframe:false}
             ];
  buildTab(jso, 'workHandleDiv',1);//实例化标签页
  //取得办理时的相关数据主要分为三种：表单数据，附件信息，会签意见
  var url = requestUrl + "/getHandlerData.act";
  var json = getJsonRs(url , 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs );
  if(json.rtState == '0'){
    $('formDiv').innerHTML = json.rtData.formMsg;
    // 然后处理附件，会签意见    isOnloadFinish = true;
  }
}
/**
 *点击标签时，执行的函数。这里比较特殊，因为在第一次点击标签时会通过ajax请求一个地址并且执行onload指向的函数，但我这里把地址指向一个空地址：contentUrl:"",所以不会去请求地址，只会执行下面这个函数
 */
function showDiv(){
  var div = arguments[0];
  if(div == "formDiv"){
    $('formDiv').show();
    $('attachDiv').show();
    $('feedBackDiv').show();
  }else if(div == 'attachDiv'){
    $('formDiv').hide();
    $('attachDiv').show();
    $('feedBackDiv').hide();
  }else{
    $('formDiv').hide();
    $('attachDiv').hide();
    $('feedBackDiv').show();
  }
}
/**
 * 保存表单.跟据flag来指定下一步跳转页面
 * @param flag 0-代表一般保存,1-代表转交下一步,2-保存后返回.
 * @return
 */
function saveForm(flag){
  //是否加载完成
  if(isOnloadFinish){
    var url = requestUrl + "/saveFormData.act";
    var json = getJsonRs(url , $('workFlowForm').serialize());
    if(json.rtState == "0"){
      if(flag == 0){
        alert(json.rtMsrg);
        $("cancleSpan").hide();//隐藏取消BUTTON
        $("comebackSpan").show();//显示返回BUTTON
      }else if(flag == 1){//转交下一步
        location = '../turn/turnnext.jsp?runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs;
      }else if(flag == 2){//保存返加列表
        alert(json.rtMsrg);
        location = "";
      }
    }else{
      document.body.innerHTML = json.rtMsrg;
    } 
  }else{
    alert("页面正在努力地加载数据,请稍后.....");
  }
} 