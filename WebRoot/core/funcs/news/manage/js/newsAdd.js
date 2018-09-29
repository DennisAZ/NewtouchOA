function uploadSuccessOver(file, serverData){
  try {
    var progress = new FileProgress(file, this.customSettings.progressTarget);
    progress.toggleCancel(false);
    var json = null;
    json = serverData.evalJSON();
    if (json.state == "1") {
      progress.setError();
      progress.setStatus("上传失败：" + serverData.substr(5));
      var stats=this.getStats();
      stats.successful_uploads--;
      stats.upload_errors++;
      this.setStats(stats);
    }else {
      $('attachmentId').value += json.data.attachmentId;
      $('attachmentName').value += json.data.attachmentName;
      var attachmentIds = $("attachmentId").value;
      var attachmentName = $("attachmentName").value;
      var ensize =  $('ensize').value;
      if(ensize){
        $('ensize').value =(json.data.size + parseInt(ensize));
      }else {
        $('ensize').value =json.data.size ;
      }//附件大小
      attachMenuUtil("showAtt","news",null,$('attachmentName').value ,$('attachmentId').value,false);
    }
  }catch(ex) {
    this.debug(ex);
  }
}

/**
 * 处理附件的显示
 * @param cntrlId
 * @return
 */
function showAttach(attrIds,attrNames,cntrId){
  var reStr = "<div id='attrDiv'>";
  var ym = "";
  var attrId = ""
  var attrIdArrays = attrIds.split(",");
  var attrNameArrays = attrNames.split("*");
  for(var i = 0 ; i <= attrIdArrays.length; i++){
    if(!attrIdArrays[i]){
      continue;
    }
    var key = attrIdArrays[i];
    var attrName = attrNameArrays[i];
    var value = attrName.substring( attrName.indexOf("_")+1, attrName.length);
    //attachMenuUtil("showAtt","news",null,$('attachmentName').value ,$('attachmentId').value,false);
    
    //reStr += "<a href=\"javascript:downFile(\'" + key + "\',\'" + value + "\');\" title=\"" + value + "\">" + value + "</a><br>";
    //MODULE=email&amp;YM=1001&amp;ATTACHMENT_ID=216664316&amp;ATTACHMENT_NAME=SoftMgrUninst.exe
  }
  reStr += "</div>";
  if(cntrId){
    $(cntrId).innerHTML = reStr;
  }else{
    document.write(reStr);
  }
}

/**
 * 文件下载
 * @param attrId
 * @param name
 * @return
 */
function downFile(attrId,name){
  var ym = attrId.split("_")[0];
  var attrId = attrId.split("_")[1];
  var attrName = name;
  var aPath = ym + "/" + attrId + "_" + attrName;
  var url = contextPath + '/getFile?uploadFileNameServer=core/funcs/email/attachment/' + aPath;
  window.open(url);
}

function upload_attach_group() {
  saveNewsByUp();
}

function upload_attach() {
  $("btnFormFile").click();
}

/**
 * 处理文件上传
 */
function handleSingleUpload(rtState, rtMsrg, rtData) {
  if (rtState != 0) {
    alert(rtMsrg);
    return;
  }
  var data = rtData.evalJSON(); 
  $('attachmentId').value +=  data.attrId;
  $('attachmentName').value +=  data.attrName;   
  var  selfdefMenu = {
    office:["downFile","dump","read","edit","deleteFile"], 
    img:["downFile","dump","play","deleteFile"],
    music:["downFile","dump","play","deleteFile"],  
    video:["downFile","dump","play","deleteFile"], 
    others:["downFile","dump","deleteFile"]
  }
  attachMenuSelfUtil("showAtt","news",$('attachmentName').value ,$('attachmentId').value, '','','',selfdefMenu);
  removeAllFile();
  if (isUploadBackFun == true) {
    sendForm(savePar);
    isUploadBackFun = false;
  }
}

/**
 * 
 * @return
 */
function saveNewsByUp(){
  var FCK = FCKeditorAPI.GetInstance('Econtent'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
  var FORM_MODE = FCK.EditingArea.Mode;
 
  //获取编辑区域的常量——源文件模式
  var editingAreaFrame = document.getElementById('Econtent___Frame');
  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
  if(FORM_MODE == editModeSourceConst)
  {
    FCK.Commands.GetCommand( 'Source' ).Execute();
  } 
  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
  var textStr = FORM_HTML;
  var url = "/yh/yh/core/funcs/news/act/YHNewsHandleAct/saveNewsByUp.act";
  document.newsForm.content.value = textStr;
  var typeId = document.newsForm.typeId.value;
  if(typeId=="0"){
    document.newsForm.typeId.value = defaultType;
    }
  if(CheckForm()){
    $("newsForm").action = url;
    $("newsForm").submit();
  }
}
function changeRange() {
  if(document.getElementById("href_txt").innerText=="按人员或角色发布"){//
    document.getElementById("rang_role").style.display="";            //显示角色
    document.getElementById("rang_user").style.display="";            //显示人员
    document.getElementById("href_txt").innerText="隐藏按人员或角色发布";
  }else if(document.getElementById("href_txt").innerText=="隐藏按人员或角色发布"){
    document.getElementById("rang_role").style.display="none";            //隐藏角色
    document.getElementById("rang_user").style.display="none";            //隐藏人员
    $("user").value = "";$("userDesc").value = "";
    $("role").value = "";$("roleDesc").value = "";
    document.getElementById("href_txt").innerText="按人员或角色发布"
  }else if(document.getElementById("href_txt").innerText=="按照角色发布"){  //显示角色
    $('rang_role').setStyle({'display':'table'});    
    document.getElementById("href_txt").innerText="隐藏角色发布";
    
  }else if(document.getElementById("href_txt").innerText=="隐藏角色发布"){  //隐藏角色  
    $("role").value = "";$("roleDesc").value = "";
    document.getElementById("rang_role").style.display="none"; 
    document.getElementById("href_txt").innerText="按照角色发布";   
  }else if(document.getElementById("href_txt").innerText == "按照人员发布"){//显示人员
    document.getElementById("rang_user").style.display="";  
    document.getElementById("href_txt").innerText = "隐藏人员发布";    
  }else if(document.getElementById("href_txt").innerText == "隐藏人员发布"){//隐藏人员
    $("user").value = "";$("userDesc").value = "";
    document.getElementById("rang_user").style.display="none";   
    document.getElementById("href_txt").innerText = "按照人员发布";
  }
}

function CheckForm()
{
  if(document.newsForm.subject.value.trim()=="请输入新闻标题...") {
    document.newsForm.subject.value="";
  }
  if(document.newsForm.subject.value.trim()=="") { 
    alert("新闻的标题不能为空！");
    document.newsForm.subject.focus();
    return false;
  }
  if(document.newsForm.dept.value==""&&document.newsForm.role.value==""&&document.newsForm.user.value=="") { 
      alert("请指定发布范围！");
      return false;
  }
  if(document.newsForm.urlAdd.value.replace("http://",'')=="" && document.newsForm.format.value=="2") { 
    alert("请指定超级链接地址 ！");
    document.newsForm.urlAdd.value = "";
    document.newsForm.urlAdd.focus();
    return (false);
  }
  if(document.newsForm.format.value=="1") {     
    var attachmentName = document.newsForm.attachmentName.value;
    if(!attachmentName){
      alert("请选择mht或其他文件！");
      return (false);
    }    
  }
  if(document.newsForm.format.value=="0"){
    var checkNull;
    if (isTouchDevice) {
      checkNull = $("contentTextarea").value;
    }
    else {
      var FCK = FCKeditorAPI.GetInstance('Econtent'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
      var FORM_MODE = FCK.EditingArea.Mode; 
      var editingAreaFrame = document.getElementById('Econtent___Frame');
      var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
      if(FORM_MODE == editModeSourceConst) {
        FCK.Commands.GetCommand( 'Source' ).Execute();
      }
      checkNull = FCK.EditingArea.Window.document.body.innerText; 
    }
    if(!checkNull) {
      alert("新闻内容不能为空！");
      return false;
    }
  }
  return (true);
}

function sendForm(publish) { 
  var actionSize = $('actionSize').value;
  var actionLight = $('actionLight').value;
  var actionFont = $('actionFont').value;
  var actionLights = $('actionLights').value;
  var actionColor = $('actionColor').value;
  var actionFlag = $('actionLightFlag').value;
  
  var subjectFont = "font-family:" + actionFont + ";font-size:" + actionSize + ";color:" + actionColor + ";filter:" + actionFlag + "(Direction=120, color=" + actionLights + ");";
  $("subjectFont").value = subjectFont;
  
  if (jugeFile()) {         //如果有没有上传的文件，则进行上传
    $("formFile").submit();
    isUploadBackFun = true;
    savePar = publish;
    return;
  }
  document.newsForm.publish.value=publish;
  if (CheckForm()) {
    document.newsForm.op.value = publish=="0" ? "0" : "1";
    if(publish != 2) {        
      savenews(); 
    }
  }
}
function savenews(){
  
  var textStr;
  if (isTouchDevice) {
    textStr = $("contentTextarea").value;
  }
  else {
    var FCK = FCKeditorAPI.GetInstance('Econtent'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
    var FORM_MODE = FCK.EditingArea.Mode;
    //获取编辑区域的常量——源文件模式
    var editingAreaFrame = document.getElementById('Econtent___Frame');
    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
    if(FORM_MODE == editModeSourceConst) {
      FCK.Commands.GetCommand( 'Source' ).Execute();
    } 
    var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
    textStr = FORM_HTML;
  }
  
  var url = "/yh/yh/core/funcs/news/act/YHNewsHandleAct/addNews.act";
  var typeId = document.newsForm.typeId.value;
  if(typeId == "0") {
    document.newsForm.typeId.value = defaultType;
  }
  document.newsForm.content.value = textStr;
  $("newsForm").action = url;
  $("newsForm").submit();
}

function showMenuStatus(event){
  var menu = new Menu({bindTo:'status' , menuData:menuData2 , attachCtrl:true});
  menu.show(event);
}

var menuData2 = [{ name:'<div style="color:#0000FF;">普通格式<div>',action:changeFormat,extData:'0'}
  ,{ name:'<div style="color:#0000FF;">MHT格式<div>',action:changeFormat,extData:'1'}
  ,{ name:'<div style="color:#0000FF;">超级链接格式<div>',action:changeFormat,extData:'2'}];
function changeFormat(){
  var status = arguments[2];
  var statusName = "选择格式";
  if (status == '1') {
    statusName = "MHT格式";
    document.getElementById("add_image").style.display="none";
    document.getElementById("editor").style.display="none";
    document.getElementById("attachment1").style.display="";
    document.getElementById("attr_tr").style.display="";
    document.getElementById("fileShowId").style.display="";
    document.getElementById("url_address").style.display="none";
    document.getElementById("format").value="1";
  }
  if(status == '0') {
    statusName = "普通格式";
    document.getElementById("add_image").style.display="";
    document.getElementById("editor").style.display="";
      document.getElementById("attachment1").style.display="";
      document.getElementById("attr_tr").style.display="";
      document.getElementById("fileShowId").style.display="";
      document.getElementById("url_address").style.display="none";
      document.getElementById("format").value="0";
  }
  if(status == '2') {
    statusName = "超级链接格式";
    document.getElementById("editor").style.display="none";
    document.getElementById("attachment1").style.display="none";
    document.getElementById("attr_tr").style.display="none";
    document.getElementById("fileShowId").style.display="none";
    document.getElementById("url_address").style.display="";
    document.getElementById("urlAdd").value="http://";
    document.getElementById("format").value="2";
  }
  document.getElementById("name").innerHTML=statusName;
}

function goBack() {
  window.location.href = contextPath + "/core/funcs/news/manage/newsList.jsp";
}

function ClearUser(TO_ID, TO_NAME){
  if(!TO_ID){
    TO_ID="TO_ID";
    TO_NAME="TO_NAME";
  }
  document.getElementsByName(TO_ID)[0].value="";
  document.getElementsByName(TO_NAME)[0].value="";
}

//判断是否要显示短信提醒 
function getSysRemind(){ 
  var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=14"; 
  var rtJson = getJsonRs(requestUrl); 
  if(rtJson.rtState == "1"){ 
     alert(rtJson.rtMsrg); 
     return ; 
  } 
  var prc = rtJson.rtData; 
  var allowRemind = prc.allowRemind; 
  var defaultRemind = prc.defaultRemind;
  if(allowRemind=='2'){ 
    $("smsRemindDiv").style.display = 'none'; 
  }else{ 
    if(defaultRemind=='1'){ 
      $("mailRemind").checked = true; 
    } 
  } 
//return prc; 
 }

//浮动菜单文件的删除 
function deleteAttachBackHand(attachName,attachId,attrchIndex){ 
  var url= contextPath + "/yh/core/funcs/news/act/YHNewsHandleAct/delFloatFile.act?attachId=" + attachId +"&attachName=" + attachName ; 
  if (seqId) {
    url += "&seqId=" + seqId;
  }
  var json=getJsonRs(encodeURI(url)); 
  if(json.rtState =='1'){ 
    alert(json.rtMsrg); 
    return false; 
  }else { 
    prcsJson=json.rtData; 
    var updateFlag=prcsJson.updateFlag; 
    if(updateFlag){ 
      var ids = $('attachmentId').value ;
      if (!ids) {
        ids = ""; 
      }
      var names =$('attachmentName').value;
      if (!names) {
        names = ""; 
      }
      var idss = ids.split(",");
      var namess = names.split("*");
     
      var newId = getStr(idss , attachId , ",");
      var newname = getStr(namess , attachName , "*");  
     
      $('attachmentId').value = newId;
      $('attachmentName').value = newname;
      return true; 
   }else{ 
     return false; 
   }  
  } 
}
function getStr(ids , id , split) {
  var str = "";
  for (var i = 0 ; i< ids.length ;i ++){
    var tmp = ids[i];
    if (tmp) {
      if (tmp != id) {
        str += tmp + split;
      }
    }
  }
  return str;
}
/** 
*js代码 
*是否显示手机短信提醒 
*/ 
function moblieSmsRemind(remidDiv,remind){ 
  var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=14"; 
  var rtJson = getJsonRs(requestUrl); 
  if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  var prc = rtJson.rtData; 
  moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中   
  if(moblieRemindFlag == '2'){ 
    $(remidDiv).style.display = ''; 
    $(remind).checked = true; 
  }else if(moblieRemindFlag == '1'){ 
    $(remidDiv).style.display = ''; 
    $(remind).checked = false; 
  }else{ 
    $(remidDiv).style.display = 'none'; 
  }
}