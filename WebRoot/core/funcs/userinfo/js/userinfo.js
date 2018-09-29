var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var isUploadBackFun = false;
/**
 * 单附件上传

 * @return
 */
function upload_attach() {
  $("formFile").submit();
}
/**
 * 显示已阅读人员

 * 
 * @param diaryId
 * @return
 */
function showReader(diaryId){
  // 到开一个弹出窗口

  //replyComment();
  var url = contextPath + '/core/funcs/diary/comment/showRead.jsp?diaId=' + diaryId;
  openWindow(url,'查看阅读人员',746,420);
}
/**
 * 查看日志正文
 * 
 * @param diaryId
 * @return
 */
function showDiaDetaile(diaryId){
  // 到开一个弹出窗口

  reader(diaryId);
  window.location = contextPath + '/core/funcs/diary/comment/index.jsp?diaId=' + diaryId;  
}
function showDiaDetaileComment(diaryId,type){
  // 到开一个弹出窗口

  reader(diaryId);
  var diaryBody = window.parent.diaryBody;
  diaryBody.window.location = contextPath + '/core/funcs/diary/comment/index.jsp?diaId=' + diaryId + '&type=' + type;  
}
function showShareComment(diaryId,type){
  // 到开一个弹出窗口

  //reader(diaryId);
  var diaryBody = window.parent.diaryBody;
  diaryBody.window.location  =  contextPath + '/core/funcs/diary/comment/shareindex.jsp?diaId=' + diaryId + '&type=' + type;  
}
/**
 * 编辑日志
 * @param diaryId
 * @return
 */
function editDiary(diaryId){
  // 到开一个弹出窗口

  window.location = contextPath + '/core/funcs/diary/new/edit.jsp?diaId=' + diaryId; 
}
/**
 * 工作日志保存 基于AJAX
 * 
 * @return
 */
function saveDiaryByAjax(form){
   var dateStr = $(form).diaDate.value; 
    if(isLocked(dateStr)){
      alert('此时间段已被锁');
      return;
    }
    var FCK = FCKeditorAPI.GetInstance('DIARY_CONTENT'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行

															// by dq 090521
    var FORM_MODE = FCK.EditingArea.Mode;
   
    // 获取编辑区域的常量——源文件模式
    var editingAreaFrame = document.getElementById('DIARY_CONTENT___Frame');
    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
    if(FORM_MODE == editModeSourceConst){
      FCK.Commands.GetCommand( 'Source' ).Execute();
    } 
   // $("formName").value = formName;
    var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
    var textStr = FORM_HTML;
    textStr = textStr.replace(/[[\n\r\f]/g,"");
    var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/saveByAjax.act";
   
    if(textStr){
      $(form).content.value = textStr;
    }else{
      alert("日志类容不能为空！")
      return;
    }
    var rtJson = getJsonRs(url,mergeQueryString($("form1")));
    if(rtJson.rtState == '0'){
      alert("保存成功!");
      return 1;
    }else{
      alert(rtJson.rtMsrg);
    }
}
/**
 * 工作日志保存 基于AJAX
 * 
 * @return
 */
function updateDiaryByAjax(form){
    var FCK = FCKeditorAPI.GetInstance('DIARY_CONTENT'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行

                              // by dq 090521
    var FORM_MODE = FCK.EditingArea.Mode;
   
    // 获取编辑区域的常量——源文件模式
    var editingAreaFrame = document.getElementById('DIARY_CONTENT___Frame');
    var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
    if(FORM_MODE == editModeSourceConst){
      FCK.Commands.GetCommand( 'Source' ).Execute();
    } 
   // $("formName").value = formName;
    var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
    var textStr = FORM_HTML;
    
    textStr = textStr.replace(/[[\n\r\f]/g,"");
    var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/updateByAjax.act";
   
    if(textStr){
      $(form).content.value = textStr;
    }else{
      $(form).content.value = " ";
    }
    var rtJson = getJsonRs(url,mergeQueryString($("form1")));
    if(rtJson.rtState == '0'){
      return true;
    }else{
      return false;
    }
}
/**
 * 编辑日志回复
 * @param form
 * @return
 */
function updateCommentReply(form){
  var FCK = FCKeditorAPI.GetInstance('DIARY_CONTENT'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行

  // by dq 090521
  var FORM_MODE = FCK.EditingArea.Mode;
  
  // 获取编辑区域的常量——源文件模式
  var editingAreaFrame = document.getElementById('DIARY_CONTENT___Frame');
  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
  if(FORM_MODE == editModeSourceConst){
  FCK.Commands.GetCommand( 'Source' ).Execute();
  } 
  // $("formName").value = formName;
  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
  var textStr = FORM_HTML;
  textStr = textStr.replace(/[[\n\r\f]/g,"");
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryCommentAct/updateCommentReply.act";
  
  if(textStr){
    $(form).replyComment.value = textStr;
  }else{
    alert('回复内容不能为空！');
    return;
  }
  var rtJson = getJsonRs(url,mergeQueryString($("form1")));
  if(rtJson.rtState == '0'){
  alert("保存成功!");
  return 1;
  }else{
  alert(rtJson.rtMsrg);
  }
}
/**
 * 取得最近十条

 * 
 * @return
 */
function getDiaryShare(div,userId){
  var url = contextPath + "/yh/core/funcs/userinfo/act/YHUserInfoAct/getDiaryShare.act";
  var param="userId="+userId;
  var rtJson = getJsonRs(url,param);
  var news = "";
  if(rtJson.rtState == '0'){
	  for(var i = 0; i < rtJson.rtData.length ; i++){
		  news += bindOneNews(rtJson.rtData[i]);
	  }
	  $(div).innerHTML = news;
	  if(news){
	    showOpreater(div);
	    for(var i = 0; i < rtJson.rtData.length ; i++){
        if(rtJson.rtData[i].data.attachmentId){
          var attrCntrl = "attr_"+ rtJson.rtData[i].data.seqId;
          showAttach(rtJson.rtData[i].data.attachmentId,rtJson.rtData[i].data.attachmentName,attrCntrl,true)
        }
      }
	  }else{
	    WarningMsrg('无共享日志！',div);
	  }
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 *根据时间的日志

 * @return
 */
function lastEntryByDate(div,date){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/listDiaryByDate.act";
  var rtJson = getJsonRs(url,"DiaDateDiary=" + date);
  var news = "";
  if(rtJson.rtState == '0'){
    if(rtJson.rtData.length > 0){
      for(var i = 0; i < rtJson.rtData.length ; i++){
        news += bindOneNews(rtJson.rtData[i]);
      }
      if(news){
        $(div).insert(news,'content');
        if(!isLocked(date)){
          showOpreater(div);
        }
        for(var i = 0; i < rtJson.rtData.length ; i++){
          if(rtJson.rtData[i].data.attachmentId){
            var attrCntrl = "attr_"+ rtJson.rtData[i].data.seqId;
            showAttach(rtJson.rtData[i].data.attachmentId,rtJson.rtData[i].data.attachmentName,attrCntrl,true)
           }
         }
       }
     }else {
       WarningMsrg('无日志记录！',div);
     }
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 根据ID取得日志正文
 * @return
 */
function diaDetaile(id,type,user,isEdit){
  if(!isEdit){
    isEdit = true;
  }else{
    isEdit = false;
  }
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/getDiaDiaryById.act";
  var rtJson = getJsonRs(url,"diaId=" + id);
  var news = "";
  if(rtJson.rtState == '0'){
    var obj = rtJson.rtData;
    obj.diaDate = obj.diaDate.substring(0,10);
    bindJson2Cntrl(obj);
    if(!type){
      if(obj.diaType == '1'){  
        $('diaryType').insert('工作日志');
      }else {
        $('diaryType').insert('个人日志');
      }
    }
    if(obj.attachmentId){
      var attrCntrl = "attr";
      showAttach(obj.attachmentId,obj.attachmentName,attrCntrl,isEdit);
      if($('attr_tr')){
        $('attr_tr').style.display = ''; 
      }
      if($('attrDiv')){
        $('attrDiv').style.display = ''; 
      }
    }
    if(user){
      $(user).value = rtJson.rtData.toId;
    }
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 根据ID取得日志正文
 * @return
 */
function shareDiaDetaile(id,type,user,isEdit){
  if(!isEdit){
    isEdit = true;
  }else{
    isEdit = false;
  }
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/getDiaDiaryById.act";
  var rtJson = getJsonRs(url,"diaId=" + id);
  var news = "";
  if(rtJson.rtState == '0'){
    var obj = rtJson.rtData;
    bindJson2Cntrl(obj);
    $('userName').innerHTML =  getUserNameById(obj.userId);
    if(!type){
      if(obj.diaType == '1'){  
        $('diaryType').insert('工作日志');
      }else {
        $('diaryType').insert('个人日志');
      }
    }
    if(obj.attachmentId){
      var attrCntrl = "attr";
      showAttach(obj.attachmentId,obj.attachmentName,attrCntrl,isEdit);
      if($('attr_tr')){
        $('attr_tr').style.display = ''; 
      }
      if($('attrDiv')){
        $('attrDiv').style.display = ''; 
      }
    }
    if(user){
      $(user).value = rtJson.rtData.toId;
    }
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 根据ID取得日志评论正文
 * @return
 */
function diaCommentDetaile(id){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryCommentAct/getDiaCommentDetaile.act";
  var rtJson = getJsonRs(url,"commentId=" + id);
  //alert(rsText);
  var news = "";
  if(rtJson.rtState == '0'){
    var obj = rtJson.rtData;
    bindJson2Cntrl(obj);
    bindDesc([{cntrlId:"userId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 根据ID取得日志回复正文
 * @return
 */
function diaCommentReplyDetaile(id){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryCommentAct/getCommentReply.act";
  var rtJson = getJsonRs(url,"replyId=" + id);
  var news = "";
  if(rtJson.rtState == '0'){
    var obj = rtJson.rtData;
    bindJson2Cntrl(obj);
    $('commentReplyId').value = obj.seqId;
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 删除日志
 * @return
 */
function deleteAll(){
  var ids = selectCheckBox();
  if(!ids){
    alert('请选择要删除的日志！');
    return;
  }
  if(!confirm("确认删除！")) {
    return ;
  }
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/deleteDia.act";
  var rtJson = getJsonRs(url,"diaIds=" + ids);
  if(rtJson.rtState == '0'){
    location.reload();
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 删除日志
 * @return
 */
function deleteDiary(ids){
  if(!ids){
    alert('请选择要删除的日志！');
    return;
  }
  if(!confirm("确认删除！")) {
    return ;
  }
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/deleteDia.act";
  var rtJson = getJsonRs(url,"diaIds=" + ids);
  if(rtJson.rtState == '0'){
    location.reload();
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 *查看指定日期是否上锁
 * @return
 */
function isLocked(dateStr){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/isLock.act";
  var rtJson = getJsonRs(url,"date=" + dateStr);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 取得十条员工记录，员工排序号在自己之后的
 * @return
 */
function getLastTenByUser(cntrlId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/lastTenByAllUser.act";
  var rtJson = getJsonRs(url);
  var news = "";
  if(rtJson.rtState == '0'){
    for(var i = 0; i < rtJson.rtData.length ; i++){
      news += bindOneNews2Info(rtJson.rtData[i]);
    }
    if(news){
      $(cntrlId).insert(news,'content');
      /*alert(news);
      $(cntrlId).innerHTML = news;*/
    }else{
      WarningMsrg('无日志记录！',cntrlId);
    }
    
    for(var i = 0; i < rtJson.rtData.length ; i++){
      listComment(rtJson.rtData[i].data.seqId,true);
      reader(rtJson.rtData[i].data.seqId);
      if(rtJson.rtData[i].data.attachmentId.trim()){
        var attrCntrl = "attr_"+ rtJson.rtData[i].data.seqId;
        showAttach(rtJson.rtData[i].data.attachmentId,rtJson.rtData[i].data.attachmentName,attrCntrl,true,rtJson.rtData[i].data.seqId)
      }
    }
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 日志查询中取得指定用户的最近十篇工作日志工作日志

 * @param userId 用户Id
 * @param cntrlId 显示类容的控件或控件Id
 * @return
 */
function getTenDiaryByUser(userId,cntrlId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/lastTenByUser.act";
  var rtJson = getJsonRs(url,"userId=" + userId);
  var news = "";
  if(rtJson.rtState == '0'){
    for(var i = 0; i < rtJson.rtData.length ; i++){
      news += bindOneNews2Info(rtJson.rtData[i]);
    }
    if(news){
      //$(cntrlId).insert(news,'content');
      $(cntrlId).innerHTML = news;
    }else{
      WarningMsrg('无日志记录！',cntrlId);
    }
    for(var i = 0; i < rtJson.rtData.length ; i++){
      listComment(rtJson.rtData[i].data.seqId,true);
      reader(rtJson.rtData[i].data.seqId);
      if(rtJson.rtData[i].data.attachmentId){
        var attrCntrl = "attr_"+ rtJson.rtData[i].data.seqId;
        showAttach(rtJson.rtData[i].data.attachmentId,rtJson.rtData[i].data.attachmentName,attrCntrl,true)
      }
    }
  }else{
    alert(rtJson.rtMsrg);
  }
}

/**
 * 共享日志/点评日志
 * @param userId 用户Id
 * @param cntrlId 显示类容的控件或控件Id
 * @return
 */
function showDiaryByIds(diaIds,cntrlId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/loadDiaryById.act";
  var rtJson = getJsonRs(url,"diaId=" + diaIds);
  var news = "";
  if(rtJson.rtState == '0'){
    //alert(rtJson.rtData.length);
    for(var i = 0; i < rtJson.rtData.length ; i++){
      
      //alert(rtJson.rtData[i].seqId);
      news += bindOneNews(rtJson.rtData[i],true);
    }
    if(news){
      //$(cntrlId).insert(news,'content');
      $(cntrlId).innerHTML = news;
    }else{
      WarningMsrg('无日志记录！',cntrlId);
    }
    for(var i = 0; i < rtJson.rtData.length ; i++){
      listComment(rtJson.rtData[i].data.seqId,true);
      if(rtJson.rtData[i].data.attachmentId){
        var attrCntrl = "attr_"+ rtJson.rtData[i].data.seqId;
        showAttach(rtJson.rtData[i].data.attachmentId,rtJson.rtData[i].data.attachmentName,attrCntrl,true,i)
      }
    }
  }else{
    alert(rtJson.rtMsrg);
  }
}

function toListDiary(userId){
  if(userId.charAt(0) == "r"){
     userId = userId.substring(1,userId.length);
  }else{
    return;
  }
  var diaryBody = window.parent.diaryBody;
  diaryBody.window.location = contextPath + "/core/funcs/diary/info/userDiary.jsp?userId=" + userId;
}
/**
 * 指定共享范围
 * @param diaryId 日志ID
 * @param userId 用户id
 * @return
 */
function share(diaryId,userId,subject){
  reader(diaryId);
  if(!subject){
    subject = getSubjectById(diaryId);
  }
  window.location = contextPath + '/core/funcs/diary/info/share.jsp?diaId=' + diaryId + "&userId=" + userId + "&subject=" + encodeURIComponent(subject);
}

function getSubjectById(diaryId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/getSubject.act?diaId=" + diaryId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    return rtJson.rtData;
  }else{
    return "";
  }
}
/**
 * 点评日志
 *@param diaryId 日志ID
 * @param userId 用户ID
 * @return
 */
function commentEdit(diaryId,userId){
  reader(diaryId);
  window.location = contextPath + '/core/funcs/diary/info/comment.jsp?diaId=' + diaryId + "&userId=" + userId;
}
/**
 * 保存评论
 * @param form
 * @return
 */
function saveComment(form){
  var FCK = FCKeditorAPI.GetInstance('DIARY_CONTENT'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行

                            // by dq 090521
  var FORM_MODE = FCK.EditingArea.Mode;
  // 获取编辑区域的常量——源文件模式
  var editingAreaFrame = document.getElementById('DIARY_CONTENT___Frame');
  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
  if(FORM_MODE == editModeSourceConst){
    FCK.Commands.GetCommand( 'Source' ).Execute();
  } 
 // $("formName").value = formName;
  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
  var textStr = FORM_HTML;
  textStr = textStr.replace(/[[\n\r\f]/g,"");
  if(textStr){
    $(form).content.value = textStr;
  }else{
    alert('评论内容不能为空！');
    return;
  }
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryCommentAct/saveComment.act";
  var rtJson = getJsonRs(url,mergeQueryString($(form)));
  if(rtJson.rtState == '0'){
    alert("保存成功!");
    return 1;
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 保存评论回复
 * @param form
 * @return
 */
function saveCommentReply(form){
  var FCK = FCKeditorAPI.GetInstance('DIARY_CONTENT'); // 获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行

                            // by dq 090521
  var FORM_MODE = FCK.EditingArea.Mode;
  // 获取编辑区域的常量——源文件模式
  var editingAreaFrame = document.getElementById('DIARY_CONTENT___Frame');
  var editModeSourceConst = editingAreaFrame.contentWindow.FCK_EDITMODE_SOURCE;// 常量FCK_EDITMODE_SOURCE的定义位置_source/fckconstants.js
  if(FORM_MODE == editModeSourceConst){
    FCK.Commands.GetCommand( 'Source' ).Execute();
  } 
 // $("formName").value = formName;
  var FORM_HTML = FCK.EditingArea.Window.document.body.innerHTML;
  var textStr = FORM_HTML;
  textStr = textStr.replace(/[[\n\r\f]/g,"");
  if(textStr){
    $(form).replyComment.value = textStr;
  }else{
    alert('回复内容不能为空！');
    return;
  }
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryCommentAct/saveCommentReply.act";
  var rtJson = getJsonRs(url,mergeQueryString($(form)));
  if(rtJson.rtState == '0'){
    alert("保存成功!");
    return 1;
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 列出所有评论

 * @param diaId
 * @param isDel 是否添加删除的操作

 * @param isShowOp 是否添加员工操作
 * @return
 */
function listComment(diaId,isDel,isShowOp,isUser){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryCommentAct/listComment.act";
  var rtJson = getJsonRs(url,"diaId=" + diaId);
  if(rtJson.rtState == '0'){
    var cntrlId = "comment_" + diaId ;
    if($(cntrlId) && rtJson.rtData.length){
      $(cntrlId).insert("<b>[以下是点评内容]</b><br />",'top');
    }
    for(var i = 0 ; i < rtJson.rtData.length; i++){
      var bool = isDel && (rtJson.rtData[i].isLoginUser == 1);
      bindComment2Json(cntrlId,i + 1, rtJson.rtData[i].comment,bool,isShowOp);
      var replyCntrl = "reply_" + rtJson.rtData[i].comment.seqId ;
      for(var j = 0 ; j < rtJson.rtData[i].commentReply.length; j++){
        bindCommentReply(replyCntrl, rtJson.rtData[i].commentReply[j],isUser);
      }
    }
  }else{
    alert(rtJson.rtMsrg);
  }
}
function bindComment2Json(cntrlId,index,json,isDel,isShowOp){
  bindComment(cntrlId,index,json.seqId,json.userId,json.sendTime,json.commentFlag,json.content,isDel,isShowOp);
}
/**
 * 导航菜单中展示已被评论的日志
 * @return
 */
function showCommentDiary(cntrlId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/listCommentedDiary.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == '0'){
    var news = "";
    for(var i = 0 ; i < rtJson.rtData.length; i++){
      var show = "";
      if(rtJson.rtData[i].subject.length >= 20){
        show = rtJson.rtData[i].subject.substr(0,20) + "...";
      }else{
        show = rtJson.rtData[i].subject;
      }
      news += "<ul><li><a title=\"" +  rtJson.rtData[i].subject + "\" href=\"javascript:showDiaDetaileComment(\'" + rtJson.rtData[i].id + "\')\">" + show + "</a></li></ul>";
    }
    $(cntrlId).insert(news,'content');
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 导航菜单中展示已共享的日志

 * @return
 */
function showShareDiary(cntrlId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/listShareDiary.act";
  var rtJson = getJsonRs(url);
  var diaIds = "";
  if(rtJson.rtState == '0'){
    var news = "";
    for(var i = 0 ; i < rtJson.rtData.length; i++){
      if(i > 4){
        if(diaIds != ""){
          diaIds += ",";
        }
        diaIds += rtJson.rtData[i].id;
        continue;
      }
      var show = "";
      if(rtJson.rtData[i].subject.length >= 8){
        show = rtJson.rtData[i].subject.substr(0,8) + "...";
      }else{
        show = rtJson.rtData[i].subject;
      }
      news += "<ul><li><a title=\"" +  rtJson.rtData[i].subject + "\" href=\"javascript:showShareComment(\'" + rtJson.rtData[i].id + "\',1)\">" + show + "</a></li></ul>";
    }
    if(diaIds){
      news += "<ul><li><a href=\"" + contextPath + "/core/funcs/diary/shareOrCom.jsp?diaIds=" + diaIds + "\"  target=\"diaryBody\" >更多......</a></li></ul>";
    }
    $(cntrlId).insert(news,'content');
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 删除日志评论
 * @param id 评论ID
 * @return
 */
function deleteComment(id){
  if(!confirm("确认删除！")) {
    return ;
  }
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryCommentAct/deleteComment.act";
  var rtJson = getJsonRs(url , "commentId=" +  id);
  if(rtJson.rtState == '0'){
    alert('删除成功！');
  }else{
    alert(rtJson.rtMsrg);
  }
  location.reload();
}
/**
 * 删除日志评论回复
 * @param id 评论回复ID
 * @return
 */
function deleteCommentReply(id){
  if(!confirm("确认删除！")) {
    return ;
  }
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryCommentAct/deleteCommentReply.act";
  var rtJson = getJsonRs(url , "replyId=" +  id);
  if(rtJson.rtState == '0'){
    alert('删除成功！');
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 评论回复
 * @param commentId 评论Id
 * @return
 */
function replyComment(commentId,seqId,type){
  doCommentReaded(commentId);
  var url = contextPath + "/core/funcs/diary/comment/replyComment.jsp?commentId=" + commentId;
  if(seqId){
    url += "&replyId=" + seqId;
  }
  if(type){
    url += "&type=" + type; 
  }
  openWindow(url,'平论回复',700,500);
}
/**
 * 评论回复
 * @param commentId 评论Id
 * @return
 */
function commentReaded(commentId){
  doCommentReaded(commentId);
  location.reload();
}
/**
 * 评论回复
 * @param commentId 评论Id
 * @return
 */
function doCommentReaded(commentId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryCommentAct/commentReaded.act";
  var rtJson = getJsonRs(url , "commentId=" +  commentId);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg);
  }
}
/**
 * 删除回复
 * @param replyId
 * @return
 */
function deleteReply(replyId){
  if(!confirm("确认删除！")) {
    return ;
  }
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryCommentAct/deleteReply.act";
  var rtJson = getJsonRs(url , "replyId=" +  replyId);
  if(rtJson.rtState == '0'){
    alert('删除成功！');
  }else{
    alert(rtJson.rtMsrg);
  }
  location.reload();
}
/**
 * 设定共享范围
 * @param form
 * @return
 */
function setShare(form,msrgCntrl){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/setShare.act";
  var rtJson = getJsonRs(url , $(form).serialize());
  if(rtJson.rtState == "0"){
    WarningMsrg(rtJson.rtMsrg,msrgCntrl,"info");
  }else {
    WarningMsrg(rtJson.rtMsrg,msrgCntrl,"error");
  }
}
/**
 * 取得指定ID的共享范围

 * @param diaId
 * @return
 */
function getShare(diaId,contrlId,msrgCntrl){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/getShare.act";
  var rtJson = getJsonRs(url , "diaId=" +  diaId);
  if(rtJson.rtState  == "0"){
    if(rtJson.rtData.trim()){
      $(contrlId).value = rtJson.rtData ;
      bindDesc([{cntrlId:contrlId, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
  }else {
    WarningMsrg(rtJson.rtMsrg,msrgCntrl,"error");
  }
}
/**
 * 判断日志是否被评论

 * @param diaId
 * @return
 */
function isComment(diaId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/isComment.act";
  var rtJson = getJsonRs(url , "diaId=" +  diaId);
  if(rtJson.rtState  == "0"){
    return rtJson.rtData;
  }else {
    alert(rtJson.rtMsrg)
  }
}
/**
 * 保存阅读了此日志的人员

 * @param diaId
 * @return
 */
function reader(diaId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/reader.act";
  var rtJson = getJsonRs(url , "diaId=" +  diaId);
  if(rtJson.rtState  == "1"){
    alert(rtJson.rtMsrg)
  }
}
//判断是否要显示短信提醒 
function getSysRemind(remidDiv,remind){ 
  var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=13"; 
  var rtJson = getJsonRs(requestUrl); 
    if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  var prc = rtJson.rtData; 
  var allowRemind = prc.allowRemind;;//是否允许显示 
  var defaultRemind = prc.defaultRemind;;//是否默认选中 
  var mobileRemind = prc.mobileRemind;//手机默认选中 
  if(allowRemind=='2'){ 
    $(remidDiv).style.display = 'none'; 
  }else{ 
    if(defaultRemind=='1'){ 
      $(remind).checked = true; 
    } 
  } 
  //return prc; 
}
//判断是否要显示短信提醒 
function moblieSmsRemind(remidDiv,remind){ 
  var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=13"; 
  var rtJson = getJsonRs(requestUrl); 
    if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  var prc = rtJson.rtData; 
  var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
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
function getUserInfo(){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/getUserInfo.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }else{
    return "";
  }
}
function InsertImage(src){ 
  //var oEditor = FCKeditorAPI.GetInstance('Econtent') ; 
  //if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG ) { 
 //   oEditor.InsertHtml( "<img src='"+ src + "'/>") ; 
 //  } 
}

function  jugeFile(){
  var formDom  = document.getElementById("formFile");
  var inputDoms  = formDom.getElementsByTagName("input"); 
  for(var i=0; i<inputDoms.length; i++){
    var idval = inputDoms[i].id;
    if(idval.indexOf("ATTACHMENT")!=-1){
      return true;
    }
  } 
  return false; 
}

function getUserPriv(userPriv){
  var url = contextPath + "/yh/core/funcs/userinfo/act/YHUserInfoAct/getUserPrivAct.act";
  var param="userPriv="+userPriv;
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == '0'){
    var data=rtJson.rtData;
	return data.userName;
	
  }else{
    return "";
  }
	}


function getDept(deptId){
  var url = contextPath + "/yh/core/funcs/userinfo/act/YHUserInfoAct/getDeptAct.act";
  var param="deptId="+deptId;
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == '0'){
    var data=rtJson.rtData;
	return data.deptName;
	
  }else{
    return "";
  }
	}
	
	

function getAvatar(photo,sex){ 
   var avatar="";
 if(photo!=""){
   photo="/yh/core/styles/imgs/avatar/"+photo+".gif";
   
  var url = contextPath + "/yh/core/funcs/userinfo/act/YHUserInfoAct/getAuatarExitAct.act";
  var param="photo="+photo;
  var rtJson = getJsonRs(url,param);
   if(rtJson.rtState == '0'){
    var data=rtJson.rtData;
	  
	if(data.exit!='1'){
		if(sex=="男") { photo="/yh/core/styles/imgs/avatar/1.gif";}
	   else if(sex=="女") {photo="/yh/core/styles/imgs/avatar/g.gif";}
		}
	
  }

 }else{
	  	if(sex=="男") { photo="/yh/core/styles/imgs/avatar/1.gif";}
	   else if(sex=="女") {photo="/yh/core/styles/imgs/avatar/g.gif";}
		
   }
   avatar="<img src="+photo + " border='0'  width='150'  ></img>";

   if(photo=="" && sex=="") {avatar="<img src='/yh/core/styles/imgs/avatar/1.gif' border='0'  width='150'  ></img>";}

  return avatar;
}


function getOnlineLevel(level){
 
   var ONLINE_HOURS = Math.floor(level / 3600);;
   var ONLINE_MINS = Math.floor((level % 3600) / 60);
   var ONLINE_LEVEL = Math.floor(Math.sqrt(Math.floor(ONLINE_HOURS / 8) + 4) - 2);
   var LEVEL_CROWN = Math.floor(ONLINE_LEVEL/64);
   var LEVEL_SUN = Math.floor((ONLINE_LEVEL%64)/16);
   var LEVEL_MOON = Math.floor((ONLINE_LEVEL % 16)/4);
   var LEVEL_STAR = Math.floor(ONLINE_LEVEL % 4);
    var RETURN_STR="";
   RETURN_STR+=str_repeat('<img src="/yh/core/styles/userinfo/time_crown.gif" align="absMiddle" />', LEVEL_CROWN);
   RETURN_STR+=str_repeat('<img src="/yh/core/styles/userinfo/time_sun.gif" align="absMiddle" />', LEVEL_SUN);
   RETURN_STR+=str_repeat('<img src="/yh/core/styles/userinfo/time_moon.gif" align="absMiddle" />', LEVEL_MOON);
   RETURN_STR+=str_repeat('<img src="/yh/core/styles/userinfo/time_star.gif" align="absMiddle" />', LEVEL_STAR);
   return '<span title="在线时长：'+ONLINE_HOURS+'小时 '+ONLINE_MINS+'分钟  等级：'+ONLINE_LEVEL+'">'+RETURN_STR+'</span>';
 

}

function str_repeat(str,num){
  var reStr="";
   for(i=0;i<=num;i++){
   reStr+=str;
   }
  return reStr;
}

function getDeptNo(deptId){
  var url = contextPath + "/yh/core/funcs/userinfo/act/YHUserInfoAct/getDeptNoAct.act";
  var param="deptId="+deptId;
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == '0'){
    var data=rtJson.rtData;
	  return data;
  }else{
    return "";
  }
}



function getUserPrivOtherName(id){
if(id!=""){
  var url = contextPath + "/yh/core/funcs/userinfo/act/YHUserInfoAct/getUserPrivOtherNameAct.act";
  var param="id="+id;
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == '0'){
    var data=rtJson.rtData;
	return data.name;
	
  }else{
    return "";
  }
}else{
  return "无";
}

}



   function getOnStatus(userId,status){
      var statusStr="";
     var url = contextPath + "/yh/core/funcs/userinfo/act/YHUserInfoAct/getOnStatusAct.act";
     var param="userId="+userId;
     var rtJson = getJsonRs(url,param);
   if(rtJson.rtState == '0'){
     var data=rtJson.rtData;
	     
      if(data.status=="0"){
	       statusStr = "离线";
		   return "<span class='oa_status_"+data.status+"'>"+statusStr+"</span>";
	  }else{
	           if(status== "1") 
                    statusStr = "在线";
               else if(status == "2")
                    statusStr = "忙碌";
               else if(status == "3")
                    statusStr = "离开";
               return "<span class='oa_status_"+status+"'>"+statusStr+"</span>";
	        }
      }else{
          return "";
      }
	
   }

function getSexName(sex){ 
     if(sex=="1") { return "女"; }
     else if(sex=="0") { return "男";}
     else return "";
}