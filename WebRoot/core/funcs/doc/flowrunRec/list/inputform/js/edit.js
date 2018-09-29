var baseContentUrl = contextPath + "/core/funcs/doc/flowrun/list/inputform";
var requestUrl = contextPath + "/yh/core/funcs/doc/act/YHFormEditAct";
var isOnloadFinish = false;//是否加载完成
var runName = "";
var opFlag = 0;
var isHaveSign = false;
var sign_info_object="";
var isLoadWebSign = false;
var isPrint = false;
/**
 * 初始化函数 * @return
 */
function doInit(){
  var jso = [{title:"表单", onload:showDiv.bind(window, "formDiv"), imgUrl:imgPath + "/form.gif"}
             ,{title:"附件", onload:showDiv.bind(window, "attachDiv"), imgUrl:imgPath +  "/attach.gif"}
             ];
  //实例化标签页
  //取得办理时的相关数据主要分为三种：表单数据，附件信息，会签意见,flowRunPrcs
  var url = requestUrl + "/getEditData.act";
  var json = getJsonRs(url , 'runId=' + runId + '&flowId=' + flowId  );
  if(json.rtState == '0'){
  	//是否允许会签
    runName = json.rtData.runName;
    runName = runName.replace('"' , '&quot;');
    var str =  "<div style='color:#000'>&nbsp;&nbsp;&nbsp;&nbsp;No."+ runId  ;
    str += '&nbsp;&nbsp;';
    str += "<input  class=\"SmallInput\" type=text value=\""+runName+"\" id=\"runNameStr\" name=\"runNameStr\"/>";
    str += "&nbsp;&nbsp;<input type=button value='修改' class=SmallButtonW onclick=updateRunName()>";
    str += '</div>';
    buildTab(jso, 'workHandleDiv',1 , str);
    
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
		
  	var flowType = json.rtData.flowType;//流程类型，1-固定流程，2-自由流程
  	var flowDoc = json.rtData.flowDoc;
  	
  	//允许上传附件
  	if (flowDoc == 1) {
  	  // 然后处理附件
      getAttachments();
  	} else {
  	  $('hasData').hide();
  	  $('noAttachMsg').update("此流程禁止公共附件!");
  	  $('noData').show();
  	}
    top.subwin = window;
  }
}

/**
 *点击标签时，执行的函数。这里比较特殊，因为在第一次点击标签时会通过ajax请求一个地址并且执行onload指向的函数，但我这里把地址指向一个空地址：contentUrl:"",所以不会去请求地址，只会执行下面这个函数 */
function showDiv(){
  var div = arguments[0];
  if(div == "formDiv"){
    $('formDiv').show();
    $('attachDiv').show();
  }else{
    $('formDiv').hide();
    $('attachDiv').show();
  }
}
/**
 * 保存表单.跟据flag来指定下一步跳转页面
 * @param flag 0-代表一般保存,1-代表转交下一步,2-保存后返回.
 * @return
 */
function saveForm(flag){
  //是否加载完成
  var url = requestUrl + "/saveFormData.act";
  LVsubmit();//设置列表的值
  if (isIE4 && isHaveSign) {
    WebSign_Submit();
  }
 
  var json = getJsonRs(url , $('workFlowForm').serialize());
  if(json.rtState == "0"){
    alert('保存成功！');
  }else{
    document.body.innerHTML = json.rtMsrg;
    window.onerror = killErrors; 
  } 
} 

function removeAllChild(node){
	var trs = $(node).childNodes;
  for(i = trs.length - 1; i >= 0; i--) {
	  $(node).removeChild(trs[i]);
  } 
}


function uploadAttach(){
  $("formFile").submit();
}
function selectSign(){
	alert('系统正在开发中...');
}
/**
 * 取得所有的附件
 */
function getAttachments(){
	var param = 'runId=' + runId + '&flowId=' + flowId;
  var url = requestUrl + "/getAttachments.act";
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
  		$('noAttachments').show();
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
	var imgSrc = getAttachImage(ext);
	//createDiv(event  ,this , \""+ attachment.attachmentId +"\" , \""+ attachment.attachmentName +"\" , \""+ attachment.privStr +"\")
	var str = "<a href='javascript:' onmouseover='createDiv(event  ,this , \""+ attachment.attachmentId +"\" , \""+ attachment.attachmentName +"\" , \""+ attachment.ext +"\")'><img src='" + imgSrc + "'/>&nbsp;" + attachment.attachmentName + "</a>";
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
  menuD.push( down );
  menuD.push( save );
  menuD.push( del );
  if (isMedia(attachName) || isVideo(attachName)){
  	menuD.push( play );
  }else if( findIsIn(docEnd , ext ) ){
		menuD.push( read );
		menuD.push( edit );
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
  var url = contextPath + "/yh/core/funcs/doc/act/YHAttachmentAct/delAttachment.act";
  var json = getJsonRs(url , param);
  if(json.rtState == '0'){
    getAttachments();
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
function handleSingleUpload(returnState , tmp){
  $('ATTACHMENT_div').innerHTML = "";
  $('ATTACHMENT_upload_div').hide();
  removeFiles("formFile");
  if (returnState == '1') {
    alert(tmp);
    return ;
  }
  getAttachments();
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
	}
	var param = 'runId=' + runId 
		 + '&flowId=' + flowId
		 + "&newType=" + $F('newType')
		 + "&newName=" + $F('newName');
  var url = requestUrl + "/createAttachment.act";
  var json = getJsonRs(url , param);
	if (json.rtState == '0') {
		getAttachments();
	} else {
		alert(json.rtMsrg);
	}
}

function updateRunName(){
  var runName = $('runNameStr').value;
  if (!runName) {
    alert('流程名不能为空 ！')
    $('runNameStr').focus();
    return ;
  }
  var url = contextPath +  "/yh/core/funcs/doc/act/YHFlowRunAct/updateRunName.act";
  var json = getJsonRs(url, "runName=" +runName + "&runId=" + runId );
  if (json.rtState == '0') {
    alert(json.rtMsrg) ;
  } 
}

function killErrors() { 
  return true; 
} 
function restoreFile(i) {
  var attachmentName = $('ATTACH_NAME' + i).value;
  var attachmentDir = $('ATTACH_DIR' + i).value;
  var diskId = $('DISK_ID' + i).value;
  var param = 'runId=' + runId 
  + '&flowId=' + flowId 
  + "&attachmentName=" + attachmentName 
  + "&attachmentDir=" + attachmentDir
  + "&diskId=" + diskId
  + "&isEdit=true";
  if (attachmentName) {
    var url = contextPath + "/yh/core/funcs/doc/act/YHAttachmentAct/restoreFile.act";;
    var json = getJsonRs(url , param);
    getAttachments();
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
//window.onerror = killErrors; 