var optionStrs = null;
var fckTimer = null; 
var contentTemp = null;
var typeId = "";
var subject = null;
var dept = null;
var user = null;
var role = null;
var beginDate = null;
var endDate = null;
var content = null;
var publish = null;
var print = null;
var format = null;
var download = null;
var typeData = null;
var notifyAuditingSingle = null;
var allowRemind = null;
var defaultRemind = null;
var ATTACH_DIR = "";
var DISK_ID = "";
var ATTACH_NAME = "";
var urlAdd = null;
var auditer = "";
var topdaysdd=0;
var topd = 0;
var isUploadBackFun = false;
var savePar ;
var moblieRemindFlag = null;
function doInit(){
  if(seqId != null && seqId !=""){
    $("notifytitle").update("修改公告通知");
  }
  var beginParameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'};
  new Calendar(beginParameters);
  var myDate=new Date();
  var month = '';
  var date = '';
  if ((parseInt(myDate.getMonth())+1) < 10) {
    month = '0' +  new String(parseInt(myDate.getMonth())+1);
  }else{
    month = myDate.getMonth() +1;
  } 
  if((parseInt(myDate.getDate())+1)<=10){
    date = '0' +  new String(parseInt(myDate.getDate()));
  }else {
    date = myDate.getDate();
  }
  var todadys= myDate.getFullYear()+'-'+month+'-'+date;
  document.getElementById('beginDate').value = todadys;
  var endParameters = {
    inputId:'endDate',
    property:{isHaveTime: false},
    bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
  var sendTimeParameters = {
    inputId:'sendTime',
    property:{isHaveTime:true},
    bindToBtn:'sendTimeImg'
  };
  new Calendar(sendTimeParameters);

  var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/beforeAddNotify.act';
  var json = getJsonRs(url);
  if (json.rtState == "0") {
    var rtData = json.rtData;
    notifyAuditingSingle = rtData.notifyAuditingSingle;
    typeData = rtData.typeData;
    optionStrs = rtData.optionStr;
    if(typeData.length > 0) {
      for(var i = 0; i < typeData.length; i++) {
        var optionStr = typeData[i];
        $("typeId").options.add(new Option(optionStr.typeDesc,optionStr.typeId)); 
      }
      defaultType = typeData[0].typeId;
    }
    defaultType = "";
    if ("1"==notifyAuditingSingle) {//审批
      document.getElementById("notifyAuditingSingleFlow").style.display = "";//提交审批按钮打开
      document.getElementById("notifyAuditingSingle").style.display = "none";
      document.getElementById("notifyAuditingSingleTop").style.display = "none";
      document.getElementById("notifyAuditingSinglepublish").style.display = "none";//发布按钮影藏
    }
  }else{
    document.body.innerHTML = json.rtMsrg;
  } 
  if(seqId) { //是修改    document.getElementById("editBack").style.display = "";
    document.getElementById("sendTimeTr").style.display = "";
    var url = contextPath + "/yh/core/funcs/notify/act/YHNotifyHandleAct/editNotify.act?seqId="+seqId;
    var json = getJsonRs(url);
    if (json.rtState == "0") {
      bindJson2Cntrl(json.rtData);
      document.getElementById("beginDate").value = json.rtData.beginDate.substring(0,10);
      document.getElementById("endDate").value = json.rtData.endDate.substring(0,10);
      document.getElementById("sendTime").value = json.rtData.sendTime.substring(0,19);

      var rtData = json.rtData;
      bannerFone(rtData.subjectFont);
      var toId = rtData.toId;
      var userId = rtData.userId;
      var privId = rtData.privId;    
      auditer = rtData.auditer;
      topdaysdd = rtData.topDays;
      topd = rtData.top;
      if(userId){
        if(privId){  //显示用户，显示角色
          document.getElementById("href_txt").innerText = "";
          $("user").value = userId;
          $("role").value = privId;
          document.getElementById("rang_user").style.display = "";
          document.getElementById("rang_role").style.display = "";
        }else{      //显示用户，不显示角色
          document.getElementById("href_txt").innerText = "按照角色发布";
          $("user").value = userId;
          $("role").value = "";
          document.getElementById("rang_user").style.display = "";
          document.getElementById("rang_role").style.display = "none";
        }
      }else{//    不显示用户，显示角色
        if(privId){
          document.getElementById("href_txt").innerText = "按照人员发布";
          $("user").value = "";
          $("role").value = privId;
          document.getElementById("rang_user").style.display = "none";
          document.getElementById("rang_role").style.display = "";
        }else{  //不显示用户，不显示角色          document.getElementById("href_txt").innerText = "按人员或角色发布";
          $("user").value = "";$("role").value = "";
          document.getElementById("rang_user").style.display = "none";
          document.getElementById("rang_role").style.display = "none";
        }
      }
      if(toId != "" || userId != "" || privId != ""){
        if(isFw != '1'){
          if(toId && toId.trim() && toId!=0 && toId!='ALL_DEPT'){
            bindDesc([{cntrlId:"dept", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
          }else{
        	  if((toId == 0 || toId == 'ALL_DEPT') && toId != "") {
	            $('dept').value = 0;
	            $('deptDesc').value = "全体部门";
        	  }
          }
          bindDesc([{cntrlId:"user", dsDef:"PERSON,SEQ_ID,USER_NAME"},
            {cntrlId:"role", dsDef:"USER_PRIV,SEQ_ID,PRIV_NAME"}]);
        }else {
          document.getElementById("dept").value = "";
          document.getElementById("user").value = "";
          document.getElementById("role").value = "";
        }
      }else {
        document.getElementById("dept").value = "";
        document.getElementById("user").value = "";
        document.getElementById("role").value = "";
      }
      if (isFw == '1') {
        document.getElementById("rang_user").style.display = "";
        document.getElementById("rang_role").style.display = "";
        document.getElementById("href_txt").innerText = "隐藏按人员或角色发布";
        document.getElementById("seqId").value = '';    
      }
      var rtData = json.rtData;
      var download = rtData.download;
      var format = rtData.format;          
      var formatName = "普通格式";
      if (format == '1') {
        formatName = "MHT格式";
        document.getElementById("add_image").style.display="none";
      }
      if (format == '2') {
        formatName = "超级链接";
        document.getElementById("attr_tr").style.display="none";  
        document.getElementById("fileShowId").style.display="none";            
      }
      document.getElementById("status").value = format;
      document.getElementById("name").innerHTML = formatName;
      typeId = rtData.typeId;
      if(typeId.trim() == '') {
        $("typeId")[0].text = '无类型';
      }else{
        $("typeId").value = typeId;
      }
      if(format=='1') {
        document.getElementById("editor").style.display="none";
      }
      if (format == '2') {
        document.getElementById("editor").style.display="none";
        document.getElementById("url_address").style.display="";
        document.getElementById("urlAdd").value= rtData.content;
        document.getElementById("content").value = rtData.content;
      }
      if(download == '1') {
        $('download').checked = 'checked';
      }
      var print = rtData.print;
      if (print == '1') {
        $('print').checked = 'checked';
      }
      var top = rtData.top;
      if(top == '1') {
        $('top').checked = 'checked';
      }
      var attrIds = rtData.attachmentId;
      var attrNames = rtData.attachmentName;
      $('attachmentName').value = attrNames;
      $('attachmentId').value = attrIds;
      attachMenuUtil("showAtt","notify",null,$('attachmentName').value ,$('attachmentId').value,false);
     
//      if (!isTouchDevice) {
//        var FORM_MODE = FCK.EditingArea.Mode; //获取编辑区域的常量——源文件模式 
//        var editingAreaFrame = document.getElementById("Econtent___Frame"); 
//        var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js 
//        if (FORM_MODE == editModeSourceConst) { 
//          FCK.Commands.GetCommand( 'Source' ).Execute(); 
//        } 
//        FCK.EditingArea.Window.document.body.innerHTML = rtData.content;
//      }
//      else {
//        $("contentTextarea").value = rtData.content;
//      }
    }else {
      alert(json.rtMsrg); 
    }
  }
  getSysRemind();
  moblieSmsRemind('remidDiv','remind');  
}


function showAttachment(event){
  var menu = new Menu({bindTo:'attachment' , menuData:menuData3 , attachCtrl:true});
  menu.show(event);
}

function uploadSuccessOver(file, serverData){
  try {
    var progress = new FileProgress(file, this.customSettings.progressTarget);
    progress.toggleCancel(false);
    var json = null;
    json = serverData.evalJSON();
    if(json.state=="1") {
      progress.setError();
      progress.setStatus("上传失败：" + serverData.substr(5));
       
      var stats=this.getStats();
      stats.successful_uploads--;
      stats.upload_errors++;
      this.setStats(stats);
    } else {
      $('attachmentId').value += json.data.attachmentId;
      $('attachmentName').value += json.data.attachmentName;
      var attachmentIds = $("attachmentId").value;
      var attachmentName = $("attachmentName").value;
      var ensize =  $('ensize').value;
      if(ensize){
        $('ensize').value =(json.data.size + parseInt(ensize));
      }else{
        $('ensize').value =json.data.size ;
      }//附件大小
       //showAttach(attachmentIds,attachmentName,"showAtt");
      attachMenuUtil("showAtt","notify",null,$('attachmentName').value ,$('attachmentId').value,false);
    }
  } catch (ex) {
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
  for (var i = 0; i <= attrIdArrays.length; i++) {
    if(!attrIdArrays[i]) {
      continue;
    }
    var key = attrIdArrays[i];
    var attrName = attrNameArrays[i];
    var value = attrName.substring( attrName.indexOf("_")+1, attrName.length);
    attachMenuUtil("showAtt", "notify", null, $('attachmentName').value, $('attachmentId').value, false);
  }
  reStr += "</div>";
  if(cntrId) {
    $(cntrId).innerHTML = reStr;
  }else{
    document.write(reStr);
  }
}

function upload_attach_group() {   
  saveNotifyByUp();
}

function upload_attach() {
  $("formFile").submit();
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
    office:["downFile", "dump", "read", "edit", "deleteFile"], 
    img:["downFile", "dump", "play", "deleteFile"],
    music:["downFile", "dump", "play", "deleteFile"],  
    video:["downFile", "dump", "play", "deleteFile"], 
    others:["downFile", "dump", "deleteFile"]};
  attachMenuSelfUtil("showAtt", "notify", $('attachmentName').value, $('attachmentId').value, '', '', '', selfdefMenu);
  removeAllFile();
  if (isUploadBackFun) {
    sendForm(savePar);
    isUploadBackFun = false;
  }
}
/**
 * 
 * @return
 */
function saveNotifyByUp(){
  var textStr = "";
  var url = contextPath + "/yh/core/funcs/notify/act/YHNotifyHandleAct/saveNotifyByUp.act";
  document.notifyForm.content.value = textStr;
  if(CheckForm()) {
    $("notifyForm").action = url;
    $("notifyForm").submit();
  }
}

function ClearUser(TO_ID, TO_NAME) {
  if(!TO_ID){
    TO_ID = "TO_ID";
    TO_NAME = "TO_NAME";
  }
  document.getElementsByName(TO_ID)[0].value = "";
  document.getElementsByName(TO_NAME)[0].value = "";
}
function changeRange(){ 
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
    //document.getElementById("rang_role").style.display="";   
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

function CheckForm() {
  if(document.notifyForm.format.value == "1") { 
    var attachmentName = document.notifyForm.attachmentName.value;
    if(!attachmentName) {
       alert("请选择mht文件！");
       return (false);
    }
  }  
  if(document.notifyForm.subject.value.trim() == "请输入公告标题...") {
      document.notifyForm.subject.value="";
  }
  if(document.notifyForm.subject.value.trim()=="") { 
    alert("公告通知的标题不能为空！");
    document.notifyForm.subject.value = "";
    document.notifyForm.subject.focus();
    return (false);
  }
  if(document.notifyForm.dept.value==""&&document.notifyForm.role.value==""&&document.notifyForm.user.value=="") { 
    alert("请指定发布范围！");
    return (false);
  }
  var regex = /^((\d{2}(([02468][048])|([13579][26]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|([1-2][0-9])))))|(\d{2}(([02468][1235679])|([13579][01345789]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))/; //日期部分
  if(document.notifyForm.beginDate.value != "") {
    if(!regex.test(document.notifyForm.beginDate.value)){
      alert("输入的日期格式错误");
      document.notifyForm.beginDate.value = "";
      document.notifyForm.beginDate.focus();
      return false;
    }
  }else {
    var date = new Date();//当前日期
    var currDate = "";
    if(navigator.userAgent.indexOf("Firefox")>0){
		  currDate = date.getYear() + 1900 + "-" + appendBefore((date.getMonth()+1), 2, "0") + "-" + appendBefore(date.getDate(), 2, "0");
    }else{
	    currDate = date.getYear() + "-" + appendBefore((date.getMonth()+1), 2, "0") + "-" + appendBefore(date.getDate(), 2, "0");  
    }
    if(document.notifyForm.endDate.value < currDate){
	    alert("终止日期不能小于当前日期");
	    return (false);
    }
  }
  if(document.notifyForm.endDate.value != "") { 
       if(!regex.test(document.notifyForm.endDate.value)){
       alert("输入的日期格式错误");
      document.notifyForm.endDate.value = "";
       document.notifyForm.endDate.focus();
         return false;
       }
  }
  if(document.notifyForm.endDate.value){
    if(document.notifyForm.endDate.value<document.notifyForm.beginDate.value) {
      alert("生效日期不能晚于终止日期");
      return (false);
    }
  }
  if(document.notifyForm.urlAdd.value.replace("http://",'')=="" && document.notifyForm.format.value=="2") { 
    alert("请指定超级链接地址 ！");
    document.notifyForm.urlAdd.focus();
    return (false);
  }
  if(document.notifyForm.print.value=="on" || document.notifyForm.print.value=="1") {
     document.notifyForm.print.value='1';
  }else {
     document.notifyForm.print.value='0';
  }
  if(document.notifyForm.download.value=="on" || document.notifyForm.download.value=="1") {
     document.notifyForm.download.value='1';
  }else {
     document.notifyForm.download.value='0';
  }
  if(document.notifyForm.top.value == "on" || document.notifyForm.top.value == "1") {
     document.notifyForm.top.value = '1';
  }else {
     document.notifyForm.top.value='0';
  }
  if(document.notifyForm.topDays.value=="") {
    document.notifyForm.topDays.value='0';
    document.notifyForm.op.value="1";
  }
  if(document.notifyForm.format.value == "0") {
//    if (isTouchDevice) {
//      if(!$("contentTextarea").value){
//        alert("公告内容不能为空！");
//        return false;
//      }
//    }
//    else {
//      var FCK = FCKeditorAPI.GetInstance('Econtent'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
//      var FORM_MODE = FCK.EditingArea.Mode; 
//      var editingAreaFrame = document.getElementById('Econtent___Frame');
//      var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
//      if (FORM_MODE == editModeSourceConst) {
//        FCK.Commands.GetCommand( 'Source' ).Execute();
//      }
//      var checkNull = FCK.EditingArea.Window.document.body.innerText; 
//      var checkNull2 = FCK.EditingArea.Window.document.body.innerHTML; 
//      if(!checkNull && checkNull2.indexOf("<img") == -1 && checkNull2.indexOf("<IMG") == -1){
//        alert("公告内容不能为空！");
//        return false;
//      }
//    }
  }
  return true;
}




function sendForm(publish)
{ 
  
  var actionSize = $('actionSize').value;
  var actionLight = $('actionLight').value;
  var actionFont = $('actionFont').value;
  var actionLights = $('actionLights').value;
  var actionColor = $('actionColor').value;
  var actionFlag = $('actionLightFlag').value;
  
  var subjectFont = "font-family:" + actionFont + ";font-size:" + actionSize + ";color:" + actionColor + ";filter:" + actionFlag + "(Direction=120, color=" + actionLights + ");";
  $("subjectFont").value = subjectFont;
  
  if (!isTouchDevice) {
//    var FCK = FCKeditorAPI.GetInstance('Econtent'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
//    var FORM_MODE = FCK.EditingArea.Mode;
//    
//    //获取编辑区域的常量——源文件模式
//    var editingAreaFrame = document.getElementById('Econtent___Frame');
//    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;//常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
//    if(FORM_MODE == editModeSourceConst) {
//      FCK.Commands.GetCommand( 'Source' ).Execute();
//    } 
//    var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
//    var checkNull = FCK.EditingArea.Window.document.body.innerText;  
//    var textStr = FORM_HTML;
    var textStr = "";
    $('content').value = textStr;    
  }
  else {
    $("content").value = $("contentTextarea").value;
  }

  document.notifyForm.publish.value=publish;
  if (CheckForm()) {
    document.notifyForm.op.value = (publish=="0" ? "0" : "1");
    if(publish !=2 ) {
      var top = $("top").checked;
      if (top) {    	   
        var mn = $("topDays2").value;
        if (!isInteger(mn) && mn!="0") {
          alert("请填写整数！");
          $("topDays2").focus();
          $("topDays2").select();
          return false;
        }else if (isInteger(mn) && parseInt(mn )< 0) {
          alert("请填写正整数！");
          $("topDays2").focus();
          $("topDays2").select();
          return false;
        }
      }else{
        $("topDays2").value = "0";
      }
      saveNotify();
    }else{
      subject = $('subject').value;
      dept = $('dept').value;
      user = $('user').value;
      role = $('role').value;
      beginDate = $('beginDate').value;
      endDate = $('endDate').value;
      attachmentId = $('attachmentId').value;
      attachmentName = $('attachmentName').value;
      content = $('content').value;       
      publish = $('publish').value;      
      if($('print').checked){
        $('prnt').value='1';
      }else{
        $('prnt').value='0';
      }
      if($('download').checked){
        $('down').value='1';
      }else{
        $('down').value='0';
      }
      format = $('format').value;               
      DISK_ID = $('DISK_ID').value;
      ATTACH_DIR = $('ATTACH_DIR').value;
      ATTACH_NAME = $('ATTACH_NAME').value;
      urlAdd = $('urlAdd').value; 
       _edit(publish,content,format);
     }
  }   
}

function _edit(publish,content,format){ 
  var contents = "<form enctype='multipart/form-data' action='"+ contextPath +"/yh/core/funcs/notify/act/YHNotifyHandleAct/addNotify.act'" 
    + "method='post' name='test' id='test'><div align=center>"
    + "<input type='hidden' name='typeId' value='"+typeId+"'>"
    + "<input type='hidden' name='seqId' value='"+seqId+"'>"
    + "<input type='hidden' name='subject' value='"+ toUtf8Uri(subject) +"'>"
    + "<input type='hidden' name='toId' value='"+dept+"'>"
    + "<input type='hidden' name='urlAdd' value='"+urlAdd+"'>"
    + "<input type='hidden' name='userId' value='"+user+"'>"
    + "<input type='hidden' name='privId' value='"+role+"'>"
    + "<input type='hidden' name='beginDate' value='"+beginDate+"'>"
    + "<input type='hidden' name='endDate' value='"+endDate+"'>"
    + "<input type='hidden' id='attachmentId' name='attachmentId' value='"+attachmentId+"'>"
    + "<input type='hidden'  id='attachmentName' name='attachmentName' value='"+attachmentName+"'>"
    + "<input type='hidden' name='ATTACH_DIR' value='"+ATTACH_DIR+"'>"
    + "<input type='hidden' name='DISK_ID' value='"+DISK_ID+"'>"
    + "<input type='hidden' name='ATTACH_NAME' value='"+ATTACH_NAME+"'>"
    + "<input type='hidden' name='content' value='"+ toUtf8Uri(content) +"'>"
    + "<input type='hidden' name='publish' value='"+publish+"'>"
    + "<input type='hidden' id='prnt' name='print' value='"+$('prnt').value+"'>"
    + "<input type='hidden' name='format' value='"+format+"'>"
    + "<input type='hidden' id='down' name='download' value='"+$('down').value+"'>"            
    + "<table cellpadding='0' border='0' cellspacing='0' class='TableBlock'><tr class='TableData'><td>&nbsp;审批人：</td>"
    + "<td align=left><select id='auditer' name='auditer' class='BigSelect'>";
  if (optionStrs.length > 0) {
    for(var i = 0; i < optionStrs.length; i++) {
      var optionStr = optionStrs[i];
      if (auditer == optionStr.value) {
        contentTemp = "<option value='"+optionStr.value+"' selected>"+optionStr.name+"</option>";
      }else{
        contentTemp = "<option value='"+optionStr.value+"'>"+optionStr.name+"</option>";  
      }
      contents = contents + contentTemp;
    }
  } 
  contents = contents + "</select></td></tr>" + "<tr class='TableData'><td>&nbsp;提醒审批人：</td><td align='left'>";
  if(allowRemind != '2' && defaultRemind == '1') {
    contents = contents + "<span id='smsRemindDiv'><input type='checkbox' name='mailRemind' id='mailRemind' checked><label for='download'>使用内部短信提醒   </label>&nbsp;&nbsp;</span>";
  }else if(allowRemind!='2'&&defaultRemind!='1'){
    contents = contents + "<span id='smsRemindDiv'><input type='checkbox' name='mailRemind' id='mailRemind'><label for='download'>使用内部短信提醒   </label>&nbsp;&nbsp;</span>";
  }
  if (moblieRemindFlag =='2') {
    contents += "<span id='remidDiv'><input type='checkbox' name='remind' id='remind' checked><label for='download'></label>手机短信提醒</span>";
  }else if (moblieRemindFlag == '1') {
    contents += "<span id='remidDiv'><input type='checkbox' name='remind' id='remind'><label for='download'></label>手机短信提醒</span>";
  }
  contents = contents+"</td></tr>";
  if(topd == '1'){
    contents+="<tr class='TableData'><td>&nbsp;置顶：</td><td align='left'><input type='checkbox'  name='top' id='top' checked><label for='top'>使公告通知置顶，显示为重要</label></td></tr>";
  }else{
    topdaysdd =0;
    contents+= "<tr class='TableData'><td>&nbsp;置顶：</td><td align='left'><input type='checkbox'  name='top' id='top'><label for='top'>使公告通知置顶，显示为重要</label></td></tr>";
  }
  contents+= "<tr class='TableData'><td>&nbsp;</td><td><input type='text'  name='topDays' id='topDays' size='3' maxlength='4' class='BigInput' value='"+ topdaysdd +"'>&nbsp;天后结束置顶，0表示一直置顶</td></tr>";
  contents+= "<tr class='TableControl'><td colspan='2' nowrap><input class='BigButton' onClick='javaScript:SetNums();' type='button' value='确定'/>&nbsp;&nbsp;";
  contents+= "<input class='BigButton' onClick='javaScript:winClose();' type='button' value='取消'/></td></tr></table></div></form>";
  alertWin('提交审批', contents  , 350, 6*22 + 100);
} 


function SetNums() { 
  var mn = $("topDays").value;
  if (!isInteger(mn) && mn!="0") {
    alert("请填写整数！");
    $("topDays").focus();
    $("topDays").select();
    return false;
  }else if(isInteger(mn) && parseInt(mn)<0){
    alert("请填写正整数！");
    $("topDays").focus();
    $("topDays").select();
    return false;
  }
  
  if($('test').top.value=="on" || $('test').top.value=="1") {
    $('test').top.value='1';
  }else {
    $('test').top.value='0';
  }
  document.test.typeId.value = $('typeId').value; 
  $('test').submit();
}

function saveNotify(){
  var url = contextPath + "/yh/core/funcs/notify/act/YHNotifyHandleAct/addFlowNotify.act";
  $("notifyForm").action = url;
  $("notifyForm").submit();
}

function goBack() {
  window.location.href = "/yh/core/funcs/notify/manage/notifyList.jsp";
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
  if(status=='1'){
    statusName = "MHT格式";
    document.getElementById("add_image").style.display="none";
    document.getElementById("editor").style.display="none";
      document.getElementById("attachment1").style.display="";
      document.getElementById("attr_tr").style.display="";
      document.getElementById("fileShowId").style.display="";
      document.getElementById("url_address").style.display="none";
      document.getElementById("format").value="1";
  }
  if(status=='0'){
    statusName = "普通格式";
    document.getElementById("editor").style.display="";
    document.getElementById("attachment1").style.display="";
    document.getElementById("url_address").style.display="none";
    document.getElementById("attr_tr").style.display="";
    document.getElementById("fileShowId").style.display="";
    document.getElementById("format").value="0";
    document.getElementById("add_image").style.display="";
  }
  if(status=='2'){
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


//判断是否要显示短信提醒 
function getSysRemind(){ 
  var requestUrl =  contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=1"; 
  var rtJson = getJsonRs(requestUrl); 
  if(rtJson.rtState == "1"){ 
     alert(rtJson.rtMsrg); 
     return ; 
  } 
  var prc = rtJson.rtData; 
  allowRemind = prc.allowRemind; 
  defaultRemind = prc.defaultRemind; 

  if(allowRemind == '2') { 
    $("smsRemindDiv").style.display = 'none'; 
  }else { 
    if(defaultRemind == '1') { 
      $("mailRemind").checked = true; 
    } 
  }
}

/** 
*js代码 
*是否显示手机短信提醒 
*/ 
function moblieSmsRemind(remidDiv,remind){ 
  var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=1"; 
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

function resetTime(){
  var  currentDate = new Date();
  currentDate = currentDate.format('yyyy-MM-dd hh:mm:ss');
  document.notifyForm.sendTime.value = currentDate;
}

//浮动菜单文件的删除 
function deleteAttachBackHand(attachName,attachId,attrchIndex) {
  var url= contextPath + "/yh/core/funcs/notify/act/YHNotifyHandleAct/delFloatFile.act?attachId=" + attachId +"&attachName=" + attachName ; 
  if (seqId) {
    url += "&seqId=" + seqId;
  }
  var json=getJsonRs(encodeURI(url)); 
  if(json.rtState =='1'){ 
    alert(json.rtMsrg); 
    return false; 
  }else{ 
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
  for (var i = 0 ; i< ids.length;i++){
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
 * 查找最大置顶时间
 * @return
 */
function getManageTopDay(){
	var requestUrl =  contextPath + "/yh/core/funcs/notify/act/YHNotifyHandleAct/getManageTopDays.act"; 
  var rtJson = getJsonRs(requestUrl); 
  if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return 0; 
  } 
  var prc = rtJson.rtData; 
  return prc;
}
