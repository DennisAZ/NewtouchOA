var baseContentUrl = contextPath + moduleContextPath + "/flowrun/list/inputform";
var requestUrl = contextPath +moduleSrcPath+"/act/YHWorkHandleAct";
var isOnloadFinish = false;//是否加载完成
var feedbackUrl = contextPath +moduleSrcPath+"/act/YHFeedbackAct";
var attachPrivWrite = false; //附件是否可写
var attachPriv = "";//文档控制
var feedbackFlag = 0;//默认0允许会签,1-禁止会签,2-强制会签
var isAllowedSubmit = false; //允许提交。主要用于强制会签var tooltipMsg2 = "";
var isPrint = false;
var dispAip = "";
var dispAipFile = "";
//主要是解决表单上传附件同步问题
var saveFormUploadFlagTmp ;//是在保存表单的时候由于上传附件，会缓存一些参数变量var notSaveFeedBackTmp ; 

//附件上传的一些全局变量
var isSaveAttachmentUpload = false;
//关于会签附件上传的一些全局变量
var isSaveFormUpload = false;//是在保存表单的时候会签附件的上传后保存
var isSaveFeedUpload = false;//是在保存会签意思的时候上传后保存
//-----
var isFinishRunUpload = false;

var runName = "";
var opFlag = 0;
var isHaveSign = false;
var sign_info_object="";
var isLoadWebSign = false;
var focusUserName = "";//关注人员
var flowType = 1;
var operate = parent.operate;
var reg = /['"]/g;
var tabStyle = 0; //默认为横向标签页
var formload = true;
var isNoTab = 0;
/**
* 初始化函数

* @return
*/
function doInit() {
 this.setInterval("test()",1000);
 var obj = $("docIframe1");
 var h = document.viewport.getDimensions().height - 80 ;
 obj.style.height = h + "px";
 obj.style.width = "100%";
 obj = $("docIframe");
 obj.style.height = h + "px";
 obj.style.width = "100%";
 obj = $("doc_send_div");
 obj.style.height = h + "px";
 obj.style.width = "100%";
}
function test() {
 if (operate.isLoaded) {
   doInit1();
   operate.isLoaded = false;
   formload = false;
 } else {
   if (formload) {
     doInit1();
     operate.isLoaded = false;
     formload = false;
   }
 }
}
function doInit1(){
  skinObjectToSpan(flowrun_list_inputform_main);
  var jso = [{id:'main',title:tableTitle, onload:showDiv.bind(window, "formDiv"), imgUrl:imgPath + "/form.gif"}];
  //实例化标签页
  //取得办理时的相关数据主要分为三种：表单数据，附件信息，会签意见,flowRunPrcs
  var url = requestUrl + "/getHandlerData.act";
  var json = getJsonRs(url , 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs + '&isWriteLog=' + isWriteLog );
  if(json.rtState == '0'){
    //是否允许会签
    runName = json.rtData.runName;
    var flowRunPrcs = json.rtData.flowRunPrcs;
    var filePrivWrite = flowRunPrcs.filePrivWrite;
    /*
    var str =  "<span style='color:#000'>&nbsp;&nbsp;&nbsp;&nbsp;<b>No."+ runId + "</b>" ;
    if (filePrivWrite) {
      runName = runName.replace('"' , '&quot;');
      str += '&nbsp;&nbsp;';
      str += "<input  class=\"SmallInput\" type=text value=\""+runName+"\" id=\"runNameStr\" name=\"runNameStr\"/>";
      str += "&nbsp;&nbsp;<input type=button value='修改' class=SmallButtonW onclick=updateRunName()>";
    } else {
      str += "<b>" + runName + "</b>" ;
    }
    if (isNew == 1) {
      str += "&nbsp;&nbsp;<span id='outlineSpan' style='color:blue;cursor:pointer' onclick='getOutline(this)'><b>草稿箱</b></span>";
    }
    
    str += '</span>';
    */
    var stmp = "查看正文";
    if (docPriv.indexOf("2") != -1) {
      stmp = "编辑正文";
    } 
    var str = "<div style='color:#000;width:100%;text-align:right'><input id='docButton' type=button value='"+stmp+"' class=SmallButtonW onclick='showDocDiv()'></div>"
    dispAip = flowRunPrcs.dispAip;
    if (dispAip) {
      jso.push({id:'disp',title:"发文处理单", onload:showDiv.bind(window, "disAip"), imgUrl:imgPath +  "/aip.gif"});  
    }
    jso.push({id:'doc',title:"正文", onload:showDocDiv.bind(window), imgUrl:imgPath + "/form.gif"});
    jso.push({id:'feedback',title:"审批意见", onload:showDiv.bind(window, "feedBackDiv"), imgUrl:imgPath +  "/sign.gif"});  
    jso.push({id:'log',title:"流转日志", onload:showDiv.bind(window, "div-extend"), imgUrl:imgPath +  "/edit.gif"}); 
    
    opFlag = flowRunPrcs.opFlag;//是否为主办1-为主办，0-会签
    if (opFlag != "0") {
      jso.push({id:'turn',title:"转交", onload:showDiv.bind(window, "turn"), imgUrl:imgPath +  "/edit.gif"}); 
    }
    if ( flowRunPrcs.hasSend) {
      jso.push({id:'send',title:"发送公文", onload:showDiv.bind(window, "send"), imgUrl:imgPath +  "/edit.gif"}); 
    }
    dispAipFile =flowRunPrcs.dispFile;
    buildTab(jso, 'workHandleDiv',1);
    
    
    if (json.rtData.formMsg) {
      window.style = json.rtData.css;
      if (window.style) {
        var elmSty = document.createElement('STYLE');
        elmSty.setAttribute("type", "text/css");
        if (elmSty.styleSheet) { 
          elmSty.styleSheet.cssText=window.style;  
        } else { 
          elmSty.appendChild(document.createTextNode(styCss));  
        } 
        document.getElementsByTagName("head")[0].appendChild(elmSty); 
      }
      try {
        $('formDiv').insert(json.rtData.formMsg);
      } catch(e) {
        
      }
      var js = json.rtData.js;
      if (js) {
        try {
          window.execScript(js);
        } catch(e) {
        }
      }
    } else {
      $('noFormData').show();
    }
    attachPrivWrite = flowRunPrcs.attachPrivWrite;
    feedbackFlag = flowRunPrcs.feedbackFlag;
    var allowBack = flowRunPrcs.allowBack;//是否允许回退0-不允许回退，1-允许退回上一步，2-允许返回以前的步骤

    flowType = flowRunPrcs.flowType;//流程类型，1-固定流程，2-自由流程
    var flowDoc = flowRunPrcs.flowDoc;
    
    if (isNew == 0) {
      operate.$('comebackSpan').show();
    } else {
      operate.$('cancleSpan').show();
    }
    if (opFlag == '0') {
      operate.$('turnSpan').hide();
      operate.$('saveSpan').hide();
      operate.$('saveAndBackSpan').hide();
      operate.$('cancleSpan').hide();
      operate.$('comebackSpan').show();
      operate.$('handlerCompleteSpan').show();
    } else {
      if (isNew == 0) {
        operate.$('comebackSpan').show();
      }
      operate.$('handlerCompleteSpan').hide();
      operate.$('turnSpan').show();
      //如果是自由流程时添加单击事件,显示结束工作按扭
      if (flowType == 2) {
        var haveEndBut = flowRunPrcs.hasEnd;
        if (haveEndBut) {
          operate.$('endSpan').show();
          var par = "runId=" + runId + "&flowId=" + flowId + "&prcsId=" + prcsId; 
          operate.$('endButton').onclick = function (){
            stop(par);
          }
        }
        operate.$('turnNextButton').onclick = function() {
          saveForm(3);
        }
      }
      operate.$('saveSpan').show();
      operate.$('saveAndBackSpan').show();
      //允许回退，并且不是第一步

      if (allowBack != 0 && prcsId != 1) {
        var backSpan = operate.$('backSpan');
        backSpan.show();//回退
        //取到回退按钮
        var button = backSpan.firstChild;
        //设置click事件,传入回退方式allowBack,1-允许退回上一步，2-允许返回以前的步骤
        $(button).observe('click' , back.bindAsEventListener(this,allowBack));
      }
      if (prcsId != '1') {
        operate.$('addUser').show();
      }
    }
    
    //允许上传附件
    if (flowDoc == 1) {
      // 然后处理附件，会签意见
      getAttachments();
      //附件不可写
      if( !attachPrivWrite && flowType != 2){
        $('attachmentTbody').hide();
        attachPriv = "4,";
      } else {
        attachPriv = flowRunPrcs.attachPriv;
        if (!findStr(attachPriv , 1)) {
          $('attachmentTbody').hide();
        } 
      }
    } else {
      $('hasData').hide();
      
      $('noAttachMsg').update("此流程禁止公共附件!");
      $('noData').show();
    }
    
    if (feedbackFlag == 1) {
      $('feedbackTable').hide();
      $('forbidDiv').show();
    } else {
      loadFeedback();
    } 
    if (feedbackFlag == 2){
       $('mustFeedbackDiv').update('本步骤为强制会签，非主办人必须填写会签意见 ');
    }
    focusUserName = flowRunPrcs.focusUserName;
    showFocus();
    
    $('workFlowForm').style.display = '';
    operate.$('wpiroot').style.display = '';
    isOnloadFinish = true;
  } else {
    $('erreMsg').update(json.rtMsrg);
    $('erreMsgDiv').style.display = '';
  }
}
function getOutline(span) {
  var url = requestUrl + "/getOutline.act?flowId=" + flowId + "&runId=" + runId;
  var style = {border:'1px solid #69F'
    ,width:'200px'
      ,position :'absolute'
        ,backgroundColor:'#FFFFFF'
          ,padding:'5px'
            ,display:"block"};
  var menu = new Menu({bindTo: span ,requestUrl:url , attachCtrl: true,hasScoll:true} , style);
  menu.show();
}
function useOutline() {
  var item =  arguments[3]
  var runId2 = item.runId ;
  var url = requestUrl + "/cancelRun.act";
  var json = getJsonRs(url , 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs);
  if(json.rtState == '0'){
    mouse_is_out = false;
    parent.location.href = contextPath + moduleContextPath + "/flowrun/list/inputform/index.jsp?runId="+runId2+"&flowId="+ flowId +"&prcsId=1&flowPrcs=1&isNew=&sortId="+sortId+"&skin=" + skin;
  }
}
function loadSign () {
  if (isIE4 && isHaveSign) {
    LoadSignData();
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
    if (dispAip) {
      $('disAip').hide();
    }
    $('div-extend').hide();
    $('div-extend1').hide();
    $('doc_send_div').hide();
  } else if (div == 'div-extend') {
    $('formDiv').hide();
    $('attachDiv').hide();
    $('feedBackDiv').hide();
    if (dispAip) {
      $('disAip').hide();
    }
    $('div-extend1').hide();
    $('div-extend').show();
    $('doc_send_div').hide();
  } else if (div == "feedBackDiv") {
    $('formDiv').hide();
    $('attachDiv').hide();
    $('feedBackDiv').show();
    $('div-extend').hide();
    if (dispAip) {
      $('disAip').hide();
    }
    $('div-extend1').hide();
    $('doc_send_div').hide();
  } else if (div == "turn") {
    $('formDiv').hide();
    $('attachDiv').hide();
    $('feedBackDiv').hide();
    $('div-extend').hide();
    if (dispAip) {
      $('disAip').hide();
    }
    $('div-extend1').show();
    $('doc_send_div').hide();
    saveForm(4);
    var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocAct/getRollState.act";
    var json = getJsonRs(url , "runId=" + runId + "&flowId=" + flowId + "&flowPrcs=" +flowPrcs);
    var flag = false;
    if (json.rtState == "0") {
      flag = json.rtData;
    }
    if (flag) {
      var url2 = contextPath + "/core/funcs/doc/send/roll.jsp?runId=" + runId  + "&prcsId=" + prcsId  + "&flowId=" + flowId+ "&flowPrcs=" + flowPrcs ;
      if (top && top.YH && top.YH.Window) {
        top.win = window;
        top.dd = new top.YH.Window({
          'title' : '归档',
          'draggable': false,
          'type': 'iframe',
          'width': 800,
          'height': 400,
          'modal':true,
          'src': url2
        });
        top.dd.show();
      } else {
        var resultValue = window.showModalDialog(url2,null,'dialogWidth=800px;dialogHeight=600px;help:no;status:no;');  
        mouse_is_out = false;
      }
    }
    document.frames['docIframe1'].location.href = contextPath + "/core/funcs/doc/flowrun/list/turn/turnnext.jsp?runId="+runId+"&flowId="+flowId+"&prcsId="+prcsId+"&flowPrcs=" + flowPrcs;
  } else if (div == "send") {
    $('formDiv').hide();
    $('attachDiv').hide();
    $('feedBackDiv').hide();
    if (dispAip) {
      $('disAip').hide();
    }
    $('div-extend1').hide();
    $('div-extend').hide();
    $('doc_send_div').show();
  } else {
    $('formDiv').hide();
    $('attachDiv').hide();
    $('feedBackDiv').hide();
    $('div-extend').hide();
    if (dispAip) {
      $('disAip').show();
    }
    $('div-extend1').hide();
    $('doc_send_div').hide();
  }
}
/**
 * 保存表单.跟据flag来指定下一步跳转页面

 * @param flag 0-代表一般保存,1-代表转交下一步,2-保存后返回,3-自由流程转交下一步,4-一般保存没有提示
 * @return
 */
function saveForm(flag , notSaveFeedBack){
  //是否加载完成
  if(isOnloadFinish){
    var attach = $('ATTACHMENT1_div').innerHTML;
    if (attach.trim()) {
      isSaveFormUpload = true;
      saveFormUploadFlagTmp = flag;
      notSaveFeedBackTmp = notSaveFeedBack;
      uploadFeedAttach();
      return null;
    }
    var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
    var attachemntId = $('attachmentIds').value;
    if (!notSaveFeedBack) {
      signSubmit();
      if (oEditor.GetXHTML() 
          || $('signData').value 
          || attachemntId) {
        saveFeedback(null, true);
      }
    }
    
    var attach2 = $('ATTACHMENT_div').innerHTML;
    if (attach2.trim()) {
      isSaveAttachmentUpload = true;
      saveFormUploadFlagTmp = flag;
      notSaveFeedBackTmp = notSaveFeedBack;
      uploadAttach1();
      return null;
    }
    var url = requestUrl + "/saveFormData.act";
    LVsubmit();//设置列表的值    if (dispAip) {
      aip_upload();
    }
    if (isIE4 && isHaveSign) {
      WebSign_Submit();
    }
    var docName = "";
    var inputs = document.getElementsByTagName("input");
    for (var i = 0 ;i < inputs.length ;i ++) {
      var input = inputs[i];
      if (input && input.title == '标题') {
        docName = input.value;
      }
    }
    if (!docName) {
      docName = "新建文档";
    }
    docName += ".doc";
    var url2 = contextPath + "/yh/core/funcs/doc/send/act/YHDocAct/reNameDoc.act";
    var json = getJsonRs(url2 , "runId=" + runId + "&docName=" + encodeURIComponent(docName));
    if (json.rtState == '0') {
      _docId = json.rtData;
      _docName = docName;
    }
    var json = getJsonRs(url , $('workFlowForm').serialize());
    if(json.rtState == "0"){
      if(flag == 0){
        alert(json.rtMsrg);
        savedHandler();
      } else if (flag == 1) {//转交下一步        turnNextPage();
      } else if(flag == 2) {//保存返加列表
        saveAndComeback();
      } else if(flag == 3) {//自由流程转交下一步        isNew = 0;
        mouse_is_out = false;
        parent.location = contextPath + moduleContextPath + '/flowrun/list/turn/turnnextfree.jsp?skin='+skin+'&sortId='+sortId+'&runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs;
      } else if (flag == 4) {
        savedHandler();
      }
    }else{
      document.body.innerHTML = json.rtMsrg;
    } 
  }else{
    alert("页面正在努力地加载数据,请稍后.....");
  }
} 
/**
 * 一般保存后的处理函数
 * @return
 */
function savedHandler() {
  isNew = 0;
  operate.$("cancleSpan").hide();//隐藏取消BUTTON
  operate.$("comebackSpan").show();//显示返回BUTTON
  if ($('outlineSpan')) {
    $('outlineSpan').hide();
  }
}
/**
 * 下一步接口(针对固定流程)，可以在message.js里对其重写，扩展
 * @return
 */
function turnNextPage() {
  isNew = 0;
  mouse_is_out = false;
  parent.location = contextPath + moduleContextPath + '/flowrun/list/turn/turnnext.jsp?skin='+skin+'&sortId='+sortId+'&runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs;
}
/**
 * 保存并返回，可以在message.js里对其重写，扩展
 * @return
 */
function saveAndComeback() {
  isNew = 0;
  mouse_is_out = false;
  parent.location = contextPath + moduleContextPath + "/flowrun/list/index.jsp?sortId=" + sortId + "&skin=" + skin;
}
function finishRunHandler(){
  var attach = $('ATTACHMENT1_div').innerHTML;
  if (attach.trim()) {
    isFinishRunUpload = true;
    uploadFeedAttach();
    return null;
  }
  var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
  var attachemntId = $('attachmentIds').value;
  signSubmit();
  if (oEditor.GetXHTML() 
      || $('signData').value 
      || attachemntId) {
    saveFeedback(null , true);
  }
  var attach2 = $('ATTACHMENT_div').innerHTML;
  if (attach2.trim()) {
    isFinishRunUpload = true;
    uploadAttach1();
    return null;
  }
  if ( !isAllowedSubmit && feedbackFlag == 2) {
    alert('本步骤为强制会签，请填写会签意见');
    return ;
  }
  var url = contextPath +moduleSrcPath+"/act/YHFeedbackAct/handlerFinish.act";
  var json = getJsonRs(url , 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs);
  if(json.rtState == '0'){
    mouse_is_out = false;
    if(json.rtData.turnPage == '0'){
      mouse_is_out = false;
      parent.location = contextPath + moduleContextPath + "/flowrun/list/turn/turnnextfree.jsp?skin="+skin+"&sortId=" + sortId+'&runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs;
      //如果是无主办人会签，且他是最后一个会签    } else if (json.rtData.turnPage == '1'){
      mouse_is_out = false;
      parent.location = contextPath + moduleContextPath + '/flowrun/list/turn/turnnext1.jsp?skin='+skin+'&sortId='+sortId+'&runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs;
    } else {
      mouse_is_out = false;
      parent.location =  contextPath + moduleContextPath + "/flowrun/list/index.jsp?skin="+ skin +"&sortId=" + sortId;
    }
  }
}
/**
 * 办理完毕
 */
function finishRun()
{
  if(window.confirm(tooltipMsg1))
  { 
    finishRunHandler();
  }
}
function cancel() {
  var url = requestUrl + "/cancelRun.act";
  var json = getJsonRs(url , 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs);
  if(json.rtState == '0'){
    mouse_is_out = false;
    parent.location = contextPath + moduleContextPath + "/flowrun/list/index.jsp?skin="+ skin +"&sortId=" + sortId;
  }else{
    document.body.innerHTML = json.rtMsrg;
  }
}

function cancelRun(flag){
  if(window.confirm(tooltipMsg2)){ 
    cancel();
  } else {
    //如果是离开此页面时
    if (flag) {
      mouse_is_out = false;
    }
  }
}
/**
 * 加载feedback
 */
function loadFeedback(){
  var param = 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + "&flowPrcs=" + flowPrcs;
  var url = feedbackUrl +  "/getFeedbacks.act";
  var json = getJsonRs(url , param);
  if(json.rtState == '0'){
    var feedbacks = json.rtData;
    removeAllChild('feedbackList');
    isAllowedSubmit = false;//主要应用 于强制会签
    for(var i = 0 ;i < feedbacks.length ;i ++){
      var feedback = feedbacks[i];
      addFeedback(feedback);
    }
  }
}
/**
 * 保存会签
 */
function saveFeedback(seqId , flag){
  var attach = $('ATTACHMENT1_div').innerHTML;
  if (attach.trim()) {
    isSaveFeedUpload = true;
    uploadFeedAttach();
    return null;
  }
  
  var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
  var str = signSubmit();
  //如果没有点上传文件．但有需要上传的文件．会自动上传
  
  var attachmentId = $('attachmentIds').value;
  var attachmentName = $('attachmentNames').value;
  //如果没有填内容，且写会签，且没上传文件
  if (!oEditor.GetXHTML() 
      && !$('signData').value && !attachmentId) {
    alert('会签内容为空!'); 
    return ;
  }
  var content = encodeURIComponent(oEditor.GetXHTML());
  var param = 'runId=' + runId
    + '&flowId=' + flowId 
    + '&flowPrcs=' + flowPrcs
    + '&prcsId=' + prcsId
    + '&content='+ content 
    + "&attachmentId=" + attachmentId
    + "&attachmentName=" + attachmentName;
  if(seqId){
    param += "&seqId=" + seqId; 
  }
  var signData = $('signData').value;
  if (signData) {
    param += "&signData=" + signData;
  }
  var url =  feedbackUrl + "/saveFeedback.act";
  var json = getJsonRs(url , param);
  if(json.rtState == '0'){
    if (flag) {
      loadFeedback();
      //去掉印章
      var sealNames = str.split(";");
      for (var i = 0 ;i < sealNames.length ;i++) {
        if (sealNames[i]) {
          DWebSignSeal.DelSeal(sealNames[i]);
        }
      }
      $('signData').value = "";
      oEditor.SetData("");
      $('attachmentIds').value = '';
      $('attachmentNames').value = '';
      $('feedAttachments').update("");
      $('feedAttachments').hide();
    } else {
      if ( opFlag != "0") {
        saveForm(4 , true);
      }
      this.location.reload();
    }
  }else{
    alert(json.rtMsrg);
  }
}
function removeAllChild(node){
  var trs = $(node).childNodes;
  for(i = trs.length - 1; i >= 0; i--) {
    $(node).removeChild(trs[i]);
  } 
}
/**
 * 添加一个会签

 */
function addFeedback(feedback){
  //强制会签用  if(feedback.userId == userId && prcsId == feedback.prcsId){
    isAllowedSubmit = true;
  }
  var tr = new Element('tr' , {'class' : 'TableData'});
  $('feedbackList').appendChild(tr);
  var td = new Element('td',{'align' : 'left','width' : '100%'});
  tr.appendChild(td);
  var title = "第" + feedback.prcsId + "步&nbsp;&nbsp;"+ feedback.prcsName 
  + "&nbsp;&nbsp;<a href='javascript:' title='"  
  + feedback.deptName +"'>" 
  + feedback.userName + "</a>&nbsp;&nbsp;"  
  + feedback.time + "&nbsp;&nbsp;";
  if (feedback.hasSignData) {
    title += "<img src='" + imgPath +"/focus.gif' style=\"cursor:pointer\" alt=\"查看手写签章\" align=\"absmiddle\" onClick=\"showSign('"+feedback.seqId+"');\">&nbsp;";
  }
  if(feedback.opPriv){
    title += "<img style='cursor:pointer' src='" + imgPath +"/edit.gif' onclick='editFeedback(this,"+ feedback.seqId +")'/>&nbsp;&nbsp;<img  style='cursor:pointer' src='"+imgPath+"/remove.png' onclick='delFeedback("+feedback.seqId+")'/>";
  }
  var content = feedback.content;
  var attachment = "";
  var div = new Element('div');
  td.appendChild(div);
  div.innerHTML = title;
  
  div = new Element('div');
  td.appendChild(div);
  div.id = 'feedback-' + feedback.seqId;
  div.style.lineHeight = "normal";
  div.innerHTML = content;
  
  for (var i = 0 ; i < feedback.attachment.length ;i ++) {
    var tmp = feedback.attachment[i];
    if (tmp) {
      div = new Element('div');
      td.appendChild(div);
      var str = addFeedBackAttach(tmp , feedback.opPriv , feedback.seqId);
      div.update(str);
    }
  }
}
function addFeedBackAttach(a , opPriv , feedId) {
  var ext = a.ext;
  var imgStr = getAttachImage(ext);
  if (opPriv != 1) {
    opPriv = 0;
  }
  var str = "<a href='javascript:void(0)' onclick='return false;' onmouseover='createAttachDiv(event  ,this , \""+ a.attachmentId +"\" , \""+ a.attachmentName +"\" , \""+ a.ext +"\" , "+ opPriv +" , "+feedId+")'><img src='" + imgStr + "'/>&nbsp;" + a.attachmentName + "</a>";
  return str;
 }
/**
 * 删除会签
 */
function delFeedback(seqId){
  if(confirm("确认删除此会签意见吗?")){
    var param = 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId;
    param += '&seqId=' + seqId;
    var url = feedbackUrl +  "/delFeedback.act";
    var json = getJsonRs(url , param);
    if(json.rtState == '0'){
      alert(json.rtMsrg);
      if ( opFlag != "0") {
        saveForm(4 , true);
      }
      this.location.reload();
      //loadFeedback();
    }else{
      alert(json.rtMsrg);
    }
  }
}
/**
 * 编辑会签
 */
function editFeedback(event,seqId){
 // top.subWin = window;
  var url = contextPath + moduleContextPath + "/flowrun/list/inputform/feedbackedit.jsp?seqId="
    + seqId + '&runId=' 
    + runId + '&flowId=' 
    + flowId + '&prcsId=' + prcsId;
  loc_x=(screen.availWidth-600)/2;
  loc_y=event.clientY;
  window.open(url
    , 'newwindow'
        , "status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=550,height=300,left="+loc_x+",top="+loc_y
    ); 
}
function uploadAttach1()
{ 
  $("formFile").submit();
}
function selectSign(){
  alert('系统正在开发中...');
}
/**
 * 取得所有的附件
 */
function getAttachments(){
  var param = 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId +"&flowPrcs=" + flowPrcs;
  var url = contextPath +moduleSrcPath+"/act/YHAttachmentAct/getAttachments.act";
  var json = getJsonRs(url , param);
  if(json.rtState == '0'){
    var attachment = json.rtData;
    removeAllChild('attachmentsList');
    
    if(attachment.length > 0){
      $('noAttachments').hide();
      for(var i = 0 ;i < attachment.length ;i ++){
        var attach = attachment[i];
        addAttachment(attach);
      }
    }else{
      //附件不可写      if ( !attachPrivWrite  && flowType != 2){
        $('hasData').hide();
        $('noData').show();
      }else{
        $('noAttachments').show();
      }
    }
  }
}
/**
 * 添加附件
 * attachment:{attachmentName:'', attachmentId:'',ext:''}
 */
function addAttachment(attachment){
  var tr = new Element('tr' , {'class' : 'TableData'});
  $('attachmentsList').appendChild(tr);
  var td = new Element('td',{'align' : 'left','width' : '100%'});
  tr.appendChild(td);
  var ext = attachment.ext;
  var imgStr = getAttachImage(ext);
  //createDiv(event  ,this , \""+ attachment.attachmentId +"\" , \""+ attachment.attachmentName +"\" , \""+ attachment.privStr +"\")
  var str = "<a href='javascript:void(0)' onclick='return false;' onmouseover='createDiv(event  ,this , \""+ attachment.attachmentId +"\" , \""+ attachment.attachmentName +"\" , \""+ attachment.ext +"\")'><img src='" + imgStr + "'/>&nbsp;" + attachment.attachmentName + "</a>";
  td.innerHTML = str; 
}
/**
 * 创建右建菜单
 * 
 */
function createDiv(event , node , attachId , attachName , ext){
  var down = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">下载<div>',action:downAction ,extData: ""};
  var save = { attachmentId:attachId , attachmentName: attachName ,name:'<div style="padding-top:5px;margin-left:10px">转存<div>',action:saveAction,extData: ""};
  var del = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">删除<div>',action:delAction,extData: ""};
  var read = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">阅读<div>',action:readAction,extData: ""};
  var print = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">阅读<div>',action:printAction,extData: ""};
  var edit = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">编辑<div>',action:editAction,extData: ""};
  var preview = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">预览<div>',action:previewAction,extData: ""};
  var play = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">播放<div>',action:playAction,extData: ""};
  
  var menuD = [];
  if (isMedia(attachName) || isVideo(attachName)){
    menuD.push( down );
    menuD.push( save );
    if ( attachPrivWrite ) {
      menuD.push( play );
      menuD.push( del ) ;
    }
  }else if( findIsIn(docEnd , ext ) ){
    if (findStr(attachPriv , 5) || findStr(attachPriv , 2)) {
      menuD.push( print );
    } else {
      menuD.push( read );
    }
    if (findStr(attachPriv , 4)) {
      menuD.push( down );
      menuD.push( save );
    }
    if ( attachPrivWrite ) {
      if (findStr(attachPriv , 2)) {
        menuD.push( edit );
      }
      if (findStr(attachPriv , 3)) {
        menuD.push( del ) ;
      }
    }
  } else {
    menuD.push( down );
    menuD.push( save );
    if ( attachPrivWrite ) {
      menuD.push( del ) ;
    }
  }
  var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:node , menuData:menuD , attachCtrl:true},divStyle);
  menu.show(event);
}
/**
 * 删除附件
 * @return
 */
function delAction(){
  var down = arguments[3];
  var attachmentId = down.attachmentId;
  var attachmentName = down.attachmentName;
  var param = 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId +"&flowPrcs=" + flowPrcs + "&attachmentId=" + attachmentId + "&attachmentName=" + attachmentName;
  var url = contextPath +moduleSrcPath+"/act/YHAttachmentAct/delAttachment.act";
  var json = getJsonRs(url , param);
  if(json.rtState == '0'){
    getAttachments();
  }else{
    alert(json.rtMsrg);
  }
}
/**
 * 创建会签附件右建菜单
 * 
 */
function createAttachDiv(event , node , attachId , attachName , ext , opPiv , seqId){
  var down = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">下载<div>',action:downAction ,extData: ""};
  var save = { attachmentId:attachId , attachmentName: attachName ,name:'<div style="padding-top:5px;margin-left:10px">转存<div>',action:saveAction,extData: ""};
  var del = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">删除<div>',action:delAttachAction,extData: "" , feedId:seqId};
  var read = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">阅读<div>',action:readAction,extData: ""};
  var print = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">阅读<div>',action:printAction,extData: ""};
  var edit = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">编辑<div>',action:editAction,extData: ""};
  var preview = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">预览<div>',action:previewAction,extData: ""};
  var play = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">播放<div>',action:playAction,extData: ""};
  var menuD = [];
  if ( findIsIn ( imgEnd , ext)){
    menuD.push( down );
    menuD.push( save );
    if ( opPiv == 1 ) {
      menuD.push( play );
      menuD.push( del ) ;
    }
  }else if( findIsIn(docEnd , ext ) ){
    menuD.push( read );
    menuD.push( down );
    menuD.push( save );
    if ( opPiv == 1 ) {
      menuD.push( edit );
      menuD.push( del ) ;
    }
  } else {
    menuD.push( down );
    menuD.push( save );
    if ( opPiv == 1 ) {
      menuD.push( del ) ;
    }
  }
  var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:node , menuData:menuD , attachCtrl:true},divStyle);
  menu.show(event);
}
/**
 * 删除会签意见附件
 * @return
 */
function delAttachAction() {
  var feed = arguments[3];
  var attachmentId = feed.attachmentId;
  var attachmentName = feed.attachmentName;
  var param = "attachmentId="
    + attachmentId
    + "&attachmentName=" 
    + attachmentName + "&feedId=" + feed.feedId;
  var url = contextPath +moduleSrcPath+"/act/YHAttachmentAct/delFeedbackAttachment.act";
  var json = getJsonRs(url , param);
  if(json.rtState == '0'){
    loadFeedback();
  }else{
    alert(json.rtMsrg);
  }
}

/**
 * 查看后缀ext是否在exts中

 */
function findIsIn(exts , ext){
  for(var i = 0 ;i < exts.length ; i++){
    var tmp = exts[i];
    if(tmp == ext){
      return true;
    }
  }
  return false;
}
function findStr(str , s) {
  if (!str) {
    return false;
  }
  var ss = str.split(",");
  return findIsIn(ss, s);
}

function newAttach(){
  if(!$('newType').present()){
    alert('请选择文件类型！');
    return ;
  }
  if(!$('newName').present()){
    alert('文件名不能为空！');
    $('newName').focus();
    return ;
  } else {
    var reg = /[\\/\:*?"<>|]+/;
    var aValue = $('newName').value;
    if (reg.test(aValue)){
      alert('文件名不能包括如下字符:\\/:*?"<>|');
      $('newName').focus();
      return ;
    }
  }
  var param = 'runId=' + runId 
     + '&flowId=' + flowId
     + '&prcsId=' + prcsId 
     + "&flowPrcs=" + flowPrcs 
     + "&newType=" + $F('newType')
     + "&newName=" + $F('newName');
  var url = contextPath +moduleSrcPath+"/act/YHAttachmentAct/createAttachment.act";
  var json = getJsonRs(url , param);
  if (json.rtState == '0') {
    getAttachments();
    var attach = json.rtData;
    if (attach) {
      office(attach.attachmentName,attach.attachmentId,"doc",4);
    }
  } else {
    //alert(json.rtMsrg);
  }
}
function checkLodeFinish(){
  
}
/**
 * 回退
 */
function back(){
  //1-允许退回上一步，2-允许返回以前的步骤
  var allowBack = arguments[1];
  if (allowBack == 1) {
    if (!confirm('确认要回退给上一步经办人吗？')) {
      return ;
    } else {
      backTo(prcsId-1);
    }
  } else {
    var url = contextPath + moduleContextPath + "/flowrun/list/inputform/goBack.jsp?sortId="+ sortId +"&runId=" + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + "&flowPrcs=" + flowPrcs;
    openDialog(url, 470, 400);
  }
}
/**
 * 回退到
 */
function backTo(id , flag){
  saveForm(4);//保存后不作任何处理
  var param = 'runId=' + runId 
      + '&flowId=' + flowId 
      +'&prcsId=' + prcsId 
      +"&flowPrcs=" + flowPrcs 
      + "&prcsIdPre=" + id;
    var url = contextPath +moduleSrcPath+"/act/YHWorkTurnAct/backTo.act?sortId=" + sortId;
  var json = getJsonRs(url , param);
  if (!flag) {
    if (json.rtState == '0') {
      mouse_is_out = false;
      parent.location = contextPath + moduleContextPath + "/flowrun/list/index.jsp?sortId=" + sortId;
    } else {
       alert(json.rtMsrg);
    }
  }
 
}

function updateRunName(){
  var runName = $('runNameStr').value;
  if (!runName) {
    alert('流程名不能为空 ！')
    $('runNameStr').focus();
    return ;
  }
  var url = contextPath +moduleSrcPath+"/act/YHFlowRunAct/updateRunName.act";
  var json = getJsonRs(url, "runName=" +runName + "&runId=" + runId );
  if (json.rtState == '0') {
    alert(json.rtMsrg) ;
  } 
}
function setSignCookie(flag) {
  if (flag) {
    $('writeButton').show();
    $('signButton').show();
  } else {
    $('writeButton').hide();
    $('signButton').hide();
  }
}
function WebSignHandWritePop() {
  var DWebSignSeal=document.getElementById("DWebSignSeal");
  try {
    DWebSignSeal.SetCurrUser(userId + "["+ userName +"]");
    DWebSignSeal.SetSignData("-");
    DWebSignSeal.SetSignData("+DATA:中国兵器工业信息中心");
    DWebSignSeal.SetPosition(10,10,"SIGN_INFO_POS");
    var obj_name=DWebSignSeal.HandWritePop(0,255,0,0,0,"SIGN_INFO");
    DWebSignSeal.SetMenuItem(obj_name,5);
  } catch (err) {
   // alert('websign控件没有加载成功！')
  }
}
function WebSignAddSeal(item,seal_id) {
  var DWebSignSeal = document.getElementById("DWebSignSeal");
  try {
    DWebSignSeal.SetCurrUser(userId + "["+ userName +"]");
    DWebSignSeal.SetSignData("-");
    DWebSignSeal.SetSignData("+DATA:中国兵器工业信息中心");
    DWebSignSeal.SetPosition(10,10,"SIGN_INFO_POS");
    if (sealForm == 1 )  {
      DWebSignSeal.addSeal("", "SIGN_INFO");
    } else {
      if(typeof seal_id=="undefined") {
        show_seal(item,"WebSignAddSeal");
      } else {    
        var URL = "http://"+ host +moduleSrcPath+"/act/YHFlowFormViewAct/getSeal.act?id=" + seal_id; 
        var obj_name = DWebSignSeal.AddSeal(URL, "SIGN_INFO");
      }
    }
    DWebSignSeal.SetMenuItem(obj_name,5);
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
}
function signSubmit() {
  var DWebSignSeal=document.getElementById("DWebSignSeal");
  var sing_info_str="";
  try {
   var strObjectName = DWebSignSeal.FindSeal("",0);
   while(strObjectName)  {
      //判断是属于会签意见签章 并且会签名称不能是宏标记里调用显示的，即新添加的手写或签章
      if(strObjectName.indexOf("SIGN_INFO")>=0 && sign_info_object.indexOf(strObjectName+",")<0) {
        sing_info_str += strObjectName+";";
      }
      strObjectName = DWebSignSeal.FindSeal(strObjectName,0);
    }
    if(sing_info_str!="") {
      $('signData').value=DWebSignSeal.GetStoreDataEx(sing_info_str);
    } else {
      $('signData').value="";
    }
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
  return sing_info_str;
}
function killErrors() { 
  return true; 
} 
//window.onerror = killErrors; 
function myAlert(text , width , height) {
  var div = new Element("div").update("<div style='padding:3px'>" + text + "</div>");
  with (div.style) {
    backgroundColor = '#F08080';
    color = '#FFF';
    fontWeight = 'bold';
    position = 'absolute';
    textAlign = 'center';
    fontSize = '12pt';
    border = '1px solid #A020F0'
  }
  div.style.height = height  + "px" ;
  div.style.left = (document.viewport.getWidth() - width)/2 + "px";
  div.style.top  =  (document.viewport.getHeight() - height)/2 + "px"; 
  document.body.appendChild(div);
  setTimeout(getOutDiv.bind(this,div) , 3000);
}
function getOutDiv() {
  var obj = arguments[0];
  document.body.removeChild(obj);
}
function showFocus(isButton){
  if (focusUserName) {
    myAlert( tooltipMsg3 + "：[" + focusUserName + "]", 400 ,40);
  } else {
    //如果是点击的按扭，还要提示
    if (isButton) {
      myAlert(tooltipMsg4, 400 ,40);
    }
  }
}
//主要处理失败的情况
var isFeedUpload2 = false;
function uploadFeedAttach() {
  isFeedUpload2 = true;
  $("ATTACHMENT1_formFile").submit();
}
function removeFiles(formId){
  var inputtmp =  $(formId);
  var inputs = inputtmp.getElementsByTagName("INPUT");
  var attach = [];
  for(var i = 0 ;i < inputs.length ; i++) {
    var input = inputs[i];
    if (input.type == 'file') {
      attach.push(input);
    }
  }
  for(var i = 0 ;i < attach.length ; i++) {
    var input = attach[i];
    if (input) {
      inputtmp.removeChild(input); 
    }
  }
}
/**
 * 
 */
function handleSingleUpload(returnState , tmp , objStr){
  if (returnState == '1') {
    alert(tmp);
    if (!isFeedUpload2) {
      $('ATTACHMENT_div').innerHTML = "";
      $('ATTACHMENT_upload_div').hide();
      removeFiles("formFile");
      if (isSaveAttachmentUpload) {
        isSaveAttachmentUpload = false;
      } else if (isFinishRunUpload) {
        isFinishRunUpload = false;
      } 
    } else {
      $('ATTACHMENT1_div').innerHTML = "";
      $('ATTACHMENT1_upload_div').hide();
      removeFiles("ATTACHMENT1_formFile");
      if (isSaveFeedUpload) {
        //主要是解决上传后同步问题
        isSaveFeedUpload = false;
      } else if (isSaveFormUpload) {
        isSaveFormUpload = false;
      } else if (isFinishRunUpload) {
        isFinishRunUpload = false;
      } 
      isFeedUpload2 = false;
    }
    return ;
  }
  if (!objStr) {
    $('ATTACHMENT_div').innerHTML = "";
    $('ATTACHMENT_upload_div').hide();
    removeFiles("formFile");
    getAttachments();
    if (isSaveAttachmentUpload) {
      isSaveAttachmentUpload = false;
      saveForm(saveFormUploadFlagTmp , notSaveFeedBackTmp);
    } else if (isFinishRunUpload) {
      isFinishRunUpload = false;
      finishRunHandler();
    }
  } else {
    eval(objStr);
    $('ATTACHMENT1_div').innerHTML = "";
    $('ATTACHMENT1_upload_div').hide();
    var id = obj.id;
    var name = obj.name;
    addFeedAttach(id , name);
    removeFiles("ATTACHMENT1_formFile");
    if (isSaveFeedUpload) {
      //主要是解决上传后同步问题
      saveFeedback();
      isSaveFeedUpload = false;
    } else if (isSaveFormUpload) {
      isSaveFormUpload = false;
      saveForm(saveFormUploadFlagTmp , notSaveFeedBackTmp);
    } else if (isFinishRunUpload) {
      isFinishRunUpload = false;
      finishRunHandler();
    }
    isFeedUpload2 = false;
  }
}
 function addFeedAttach(id , name) {
   var tmpId = $('attachmentIds').value;
   var tmpName = $('attachmentNames').value;
   
   $('feedAttachments').show();
   var tmp = $('feedAttachments').innerHTML.trim() ;
   var arra = name.split("*");
   var ids = id.split(",");
   for(var i = 0 ;i < arra.length ; i++) {
     var str = arra[i];
     if (str) {
       var id1 = ids[i];
       tmp += "<span id='feedattach-"+id1+"'><img src='" + imgPath +"/attach.png'>" + str + "<img onclick=\"removeFeedAttache('"+id1+"','"+str+"')\" src='" + imgPath +"/remove.png'>;</span>";
     }
   }
   $('feedAttachments').update(tmp);
   $('attachmentIds').value = tmpId + id;
   $('attachmentNames').value = tmpName + name;
 }
function restoreFile(i) {
  var attachmentName = $('ATTACH_NAME' + i).value;
  var attachmentDir = $('ATTACH_DIR' + i).value;
  var diskId = $('DISK_ID' + i).value;
  var param = 'runId=' + runId 
  + '&flowId=' + flowId 
  + '&prcsId=' + prcsId 
  + "&flowPrcs=" + flowPrcs 
  + "&diskId=" + diskId
  + "&attachmentName=" + attachmentName 
  + "&attachmentDir=" + attachmentDir;
  if (i == 1) {
    if (attachmentName) {
      var url = contextPath +moduleSrcPath+"/act/YHAttachmentAct/restoreFile.act?isFeedAttach=true";;
      var json = getJsonRs(url , param);
      if (json.rtState == '0') {
        obj = json.rtData;
        var id = obj.id;
        var name = obj.name;
        addFeedAttach(id , name);
      }
    }
  } else {
    if (attachmentName) {
      var url = contextPath +moduleSrcPath+"/act/YHAttachmentAct/restoreFile.act";;
      var json = getJsonRs(url , param);
      getAttachments();
    }
  }
  $('SelFileDiv' + i).update("");
  $('ATTACH_NAME' + i).value = "";
  $('ATTACH_DIR' + i).value = "";
  $('DISK_ID' + i).value = "";
}
var cursor = "";
function LoadSignDataSign(data,count,content,version)
{
  var vDWebSignSeal=document.getElementById("DWebSignSeal");
  try {
    if(!vDWebSignSeal)
       return;
    vDWebSignSeal.SetStoreData(data);
    var strObjectName ;
    strObjectName = vDWebSignSeal.FindSeal(cursor,0);
    while(strObjectName  != "")
    {
      if(strObjectName.indexOf("SIGN_INFO")>=0)
      {
         vDWebSignSeal.MoveSealPosition(strObjectName,0, -30, "personal_sign"+count);
         break;
      }
      strObjectName = vDWebSignSeal.FindSeal(strObjectName,0);
    }
    vDWebSignSeal.ShowWebSeals();
    if(version==0)
      vDWebSignSeal.SetSealSignData (strObjectName,"中国兵器工业信息中心");
    else
      vDWebSignSeal.SetSealSignData (strObjectName,content);
    vDWebSignSeal.SetMenuItem(strObjectName,4);
    sign_info_object += strObjectName+",";
    strObjectName = vDWebSignSeal.FindSeal(strObjectName,0);
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
}
/**
 * 结束工作:条件-如果是自由流程显示结束工作按扭
 */
function stop(par){
  if(window.confirm(tooltipMsg5)) {
    var url = contextPath+moduleSrcPath+'/act/YHFreeFlowTypeAct/stop.act';
    var json = getJsonRs(url , par);
    if (json.rtState == '0') {
      mouse_is_out = false;
      parent.location = contextPath + moduleContextPath + "/flowrun/list/index.jsp?sortId=" +sortId;
    } else {
      alert(json.rtMsrg);
    }
  }
}
function removeFeedAttache(id , name) {
  $('feedAttachments').removeChild($('feedattach-' + id));
  var tmpId = $('attachmentIds').value;
  var tmpName = $('attachmentNames').value;
  var ids = tmpId.split(",");
  var names = tmpName.split("*");
  
  var newId = "";
  var newName = "";
  for (var i = 0 ; i < ids.length ; i++) {
    var is = ids[i];
    if (is != id) {
      newId += is + ",";
      newName += names[i] + "*";
    }
  }
  $('attachmentIds').value = newId;
  $('attachmentNames').value = newName;
}
/**
 * 清除会签内容
 * @return
 */
function clearFeedbackContent() {
  var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
  oEditor.SetData("");
}
function addUser() {
  var url = contextPath + moduleContextPath + "/flowrun/list/inputform/addUser.jsp?skin="+skin+"&sortId="+ sortId +"&runId=" + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + "&flowPrcs=" + flowPrcs;
  openDialog(url, 270, 100);
}
function quickLoad(e) {
  var itemData = e.value;
  var itemId = e.name;
  loc_x=(screen.availWidth-500)/2;
  loc_y=event.clientY;
  window.open("quickLoad.jsp?flowId="+flowId+"&runId="+runId+"&itemId="+itemId+"&itemData=" + itemData,"quick_load","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=600,height=400,left="+loc_x+",top="+loc_y);
}
function getCtrlByTitle(title) {
  var tag = document.getElementsByTagName("INPUT");
  for (var i = 0 ;i< tag.length ;i++) {
    var t = tag[i];
    if (t.title == title) {
      return t;
    }
  }
}
function closePage() {
  if (top && top.dd)
      top.dd.destroy();
}                   