/**
 * 用户
 * @return
 */
function getUserInfo(el,pageIndex,pageSize){
  var keyWord = $('search').value;
  if(!pageIndex){
    pageIndex = 0;
  }
  if(!pageSize){
    pageSize = 10;
  }
  var contentHtml = "";
  if(!keyWord){
    contentHtml = "请输入关键词进行搜索";
    $('module_1').innerHTML = contentHtml;
    return;
  }
  var importantInfoArray = new Array();
  var generalInfoArray = new Array();
  var url = contextPath + "/yh/core/funcs/search/act/YHFrameSerach/searchUser.act";
  var param = "keyWord=" + keyWord + "&pageStart=" + pageIndex + "&pageNum=" + pageSize;
  var rtJson = getJsonRs(url,param);
  var totalRecords = 0;
  totalRecords = rtJson.rtData.recordTotal;
  for(var i = 0 ; i < rtJson.rtData.records.length;i++){
    var userInfo = rtJson.rtData.records[i];
    importantInfoArray.push(["部门",userInfo.deptId]);
    importantInfoArray.push(["角色",userInfo.privName]);
    if(userInfo.mobilNo){
      importantInfoArray.push(["手机",userInfo.mobilNo]);
    }
    if(userInfo.telNoDept){
      importantInfoArray.push(["工作电话1",userInfo.telNoDept]);
    }
    if(userInfo.email){
      importantInfoArray.push(["Email",userInfo.email]);
    }
    if(userInfo.oicq){
      importantInfoArray.push(["QQ",userInfo.oicq]);
    }
    if(userInfo.birthday){
      var birthday = userInfo.birthday.substr(0,10);
      generalInfoArray.push(["生日",birthday]);
    }
    if(userInfo.telNoHome){
      generalInfoArray.push(["家庭电话",userInfo.telNoHome]);
    }
    if(userInfo.bpNo){
      generalInfoArray.push(["工作电话2",userInfo.bpNo]);
    }
    if(userInfo.remark){
      generalInfoArray.push(["备注",userInfo.remark]);
    }
    
    var sex =  "女";
    if ( userInfo.sex == 0 ){
      sex = "男";
    }
   var showStr = organize_summary_info(importantInfoArray,generalInfoArray,"");
   contentHtml +=  "<div class=\"module_ser\"><a href=\"javascript:openUser('"+ userInfo.seqId + "');\" class=\"header\" align=\"center\"><u>" + userInfo.userName + "</u> (" + sex + ")</a><div class=\"module_body\" style=\"padding-top:5px;\"></div>" + showStr + "</div>\n";
   importantInfoArray = new Array();
   generalInfoArray = new Array();
  }
  if(!contentHtml){
    contentHtml = "没有找到与“" + keyWord + "” 相关的内容";
    $('module_1').innerHTML = contentHtml;
    return;
  }
  $('module_1').innerHTML = contentHtml;
  $('module_1').innerHTML += pagePanel(el,pageIndex,pageSize,totalRecords,"getUserInfo");
}
/**
 * 内部邮件
 * @return
 */
function getEmialInfo(el,pageIndex,pageSize){
  var keyWord = $('search').value;
  if(!pageIndex){
    pageIndex = 0;
  }
  if(!pageSize){
    pageSize = 10;
  }
  var contentHtml = "";
  if(!keyWord){
    contentHtml = "请输入关键词进行搜索";
    $('module_2').innerHTML = contentHtml;
    return;
  }
  var importantInfoArray = new Array();
  var generalInfoArray = new Array();
  var url = contextPath + "/yh/core/funcs/search/act/YHFrameSerach/searchEmail.act";
  var param = "keyWord=" + keyWord + "&pageStart=" + pageIndex + "&pageNum=" + pageSize;
  var rtJson = getJsonRs(url,param);
  var totalRecords = 0;
  totalRecords = rtJson.rtData.recordTotal;
  for(var i = 0 ; i < rtJson.rtData.records.length;i++){
    var emailInfo = rtJson.rtData.records[i];
    var content = emailInfo.content.stripTags();
    if(emailInfo.content){
      importantInfoArray.push(["邮件内容",content]);
    }
    if(emailInfo.sendTime){
      importantInfoArray.push(["邮件日期",emailInfo.sendTime]);
    }
   var showStr = organize_summary_info(importantInfoArray,"","");
   contentHtml +=  "<div class=\"module_ser\"><a href=\"javascript:openEmail('"+emailInfo.emialId+"','"+emailInfo.bodyId+"');\" class=\"header\"><u>邮件主题：" + emailInfo.subject + "</u></a><div class=\"module_body\">" + showStr + "</div></div>\n";
   importantInfoArray = new Array();
  }
  if(!contentHtml){
    contentHtml = "没有找到与“" + keyWord + "” 相关的内容";
    $('module_2').innerHTML = contentHtml;
    return;
  }
  $('module_2').innerHTML = contentHtml;
  $('module_2').innerHTML += pagePanel(el,pageIndex,pageSize,totalRecords,"getEmialInfo");
}
/**
 * 公告通知
 * @return
 */
function getNotifyInfo(el,pageIndex,pageSize){
  var keyWord = $('search').value;
  if(!pageIndex){
    pageIndex = 0;
  }
  if(!pageSize){
    pageSize = 10;
  }
  var contentHtml = "";
  if(!keyWord){
    contentHtml = "请输入关键词进行搜索";
    $('module_3').innerHTML = contentHtml;
    return;
  }
  var importantInfoArray = new Array();
  var generalInfoArray = new Array();
  var url = contextPath + "/yh/core/funcs/search/act/YHFrameSerach/searchNotify.act";
  var param = "keyWord=" + keyWord + "&pageStart=" + pageIndex + "&pageNum=" + pageSize;
  var rtJson = getJsonRs(url,param);
  var totalRecords = 0;
  totalRecords = rtJson.rtData.recordTotal;
  for(var i = 0 ; i < rtJson.rtData.records.length;i++){
    var notifyInfo = rtJson.rtData.records[i];
    var content = notifyInfo.content.stripTags();
    if(notifyInfo.fromId){
      importantInfoArray.push(["发布人",notifyInfo.fromId]);
    }
    if(notifyInfo.typeId){
      importantInfoArray.push(["类型",notifyInfo.typeId]);
    }
    if(notifyInfo.beginDate){
      var beginDate = notifyInfo.beginDate.substr(0,10);
      importantInfoArray.push(["发布时间",beginDate]);
    }
    if(content){
      importantInfoArray.push(["内容",content]);
    }
   var showStr = organize_summary_info(importantInfoArray,"","");
   contentHtml +=  "<div class=\"module_ser\"><a href=\"javascript:openNotify('"+notifyInfo.notifyId+"','" + notifyInfo.format + "');\" class=\"header\"><u>" + notifyInfo.subject + "</u></a><div class=\"module_body\">" + showStr + "</div></div>\n";
   importantInfoArray = new Array();
  }
  if(!contentHtml){
    contentHtml = "没有找到与“" + keyWord + "” 相关的内容";
    $('module_3').innerHTML = contentHtml;
    return;
  }
  $('module_3').innerHTML = contentHtml;
  $('module_3').innerHTML += pagePanel(el,pageIndex,pageSize,totalRecords,"getNotifyInfo");
}
/**
 * 
 * @return
 */
function getAddressInfo(el,pageIndex,pageSize){
  var keyWord = $('search').value;
  if(!pageIndex){
    pageIndex = 0;
  }
  if(!pageSize){
    pageSize = 10;
  }
  var contentHtml = "";
  if(!keyWord){
    contentHtml = "请输入关键词进行搜索";
    $('module_4').innerHTML = contentHtml;
    return;
  }
  var importantInfoArray = new Array();
  var generalInfoArray = new Array();
  var url = contextPath + "/yh/core/funcs/search/act/YHFrameSerach/searchAddress.act";
  var param = "keyWord=" + keyWord + "&pageStart=" + pageIndex + "&pageNum=" + pageSize;
  var rtJson = getJsonRs(url,param);
  var totalRecords = 0;
  totalRecords = rtJson.rtData.recordTotal;
  for(var i = 0 ; i < rtJson.rtData.records.length;i++){
    var addressInfo = rtJson.rtData.records[i];
    var sex = "";
    if(addressInfo.sex == "0"){
      sex = "男";
    }else if(addressInfo.sex == "1"){
      sex = "女";
    }
    if(addressInfo.sex){
      importantInfoArray.push(["性别",sex]);
    }
    if(addressInfo.nickName){
      importantInfoArray.push(["昵称",addressInfo.nickName]);
    }
    if(addressInfo.deptName){
      importantInfoArray.push(["单位名称",addressInfo.deptName]);
    }
    if(addressInfo.telNoDept){
      importantInfoArray.push(["工作电话1",addressInfo.telNoDept]);
    }
    if(addressInfo.telNoHome){
      importantInfoArray.push(["家庭电话",addressInfo.telNoHome]);
    }
    if(addressInfo.mobilNo){
      importantInfoArray.push(["手机",addressInfo.mobilNo]);
    }
    if(addressInfo.email){
      importantInfoArray.push(["email",addressInfo.email]);
    }
   var showStr = organize_summary_info(importantInfoArray,"","");
   contentHtml +=  "<div class=\"module_ser\"><a href=\"javascript:openAddress('"+addressInfo.addId+"');\" class=\"header\"><u>" + addressInfo.psName + "</u></a><div class=\"module_body\">" + showStr + "</div></div>\n";
   importantInfoArray = new Array();
  }
  if(!contentHtml){
    contentHtml = "没有找到与“" + keyWord + "” 相关的内容";
    $('module_4').innerHTML = contentHtml;
    return;
  }
  $('module_4').innerHTML = contentHtml;
  $('module_4').innerHTML += pagePanel(el,pageIndex,pageSize,totalRecords,"getAddressInfo");
}

function getFileFolderInfo(el,pageIndex,pageSize){
  var keyWord = $('search').value;
  if(!pageIndex){
    pageIndex = 0;
  }
  if(!pageSize){
    pageSize = 10;
  }
  var contentHtml = "";
  if(!keyWord){
    contentHtml = "请输入关键词进行搜索";
    $('module_5').innerHTML = contentHtml;
    return;
  }
  var importantInfoArray = new Array();
  var generalInfoArray = new Array();
  var url = contextPath + "/yh/core/funcs/search/act/YHFrameSerach/searchFileFolder.act";
  var param = "keyWord=" + keyWord + "&pageStart=" + pageIndex + "&pageNum=" + pageSize;
  var rtJson = getJsonRs(url,param);
  var totalRecords = 0;
  totalRecords = rtJson.rtData.recordTotal;
  for(var i = 0 ; i < rtJson.rtData.records.length;i++){
    var fileFolder = rtJson.rtData.records[i];
    if(fileFolder.sendTime){
      var sendTime = fileFolder.sendTime.substr(0,10);
      importantInfoArray.push(["发布时间",sendTime]);
    }
    if(fileFolder.attachmentName){
      var attachs = fileFolder.attachmentName.replace(/\*/g,",");
      importantInfoArray.push(["附件",attachs]);
    }
    if(fileFolder.content){
      importantInfoArray.push(["文件内容",fileFolder.content]);
    }
   var showStr = organize_summary_info(importantInfoArray,"","");
   contentHtml +=  "<div class=\"module_ser\"><a href=\"javascript:openFileFloder('" + fileFolder.contentId + "','" + fileFolder.sortId + "');\" class=\"header\"><u>" + fileFolder.subject + "</u></a><div class=\"module_body\">" + showStr + "</div></div>\n";
   importantInfoArray = new Array();
  }
  if(!contentHtml){
    contentHtml = "没有找到与“" + keyWord + "” 相关的内容";
    $('module_5').innerHTML = contentHtml;
    return;
  }
  $('module_5').innerHTML = contentHtml;
  $('module_5').innerHTML += pagePanel(el,pageIndex,pageSize,totalRecords,"getFileFolderInfo");
}
/**
 * 
 * @return
 */
function getFlowWorkInfo(el,pageIndex,pageSize){
  var keyWord = $('search').value;
  if(!pageIndex){
    pageIndex = 0;
  }
  if(!pageSize){
    pageSize = 10;
  }
  var contentHtml = "";
  if(!keyWord){
    contentHtml = "请输入关键词进行搜索";
    $('module_6').innerHTML = contentHtml;
    return;
  }
  var importantInfoArray = new Array();
  var generalInfoArray = new Array();
  var url = contextPath + "/yh/core/funcs/search/act/YHFrameSerach/searchWorkFlow.act";
  var param = "keyWord=" + keyWord + "&pageStart=" + pageIndex + "&pageNum=" + pageSize;
  var rtJson = getJsonRs(url,param);
  var totalRecords = 0;
  totalRecords = rtJson.rtData.totalRecord;
  for(var i = 0 ; i < rtJson.rtData.pageData.length;i++){
    var fileFolder = rtJson.rtData.pageData[i];
    if(fileFolder.runId){
      importantInfoArray.push(["流水号",fileFolder.runId]);
    }
    if(fileFolder.beginTime){
      var beginTime = fileFolder.beginTime.substr(0,19);
      importantInfoArray.push(["开始时间",beginTime]);
    }
    if(fileFolder.attachmentName){
      var attachs = fileFolder.attachmentName.replace(/\*/g,",");
      importantInfoArray.push(["公共附件",attachs]);
    }
   var showStr = organize_summary_info(importantInfoArray,"","");
   contentHtml +=  "<div class=\"module_ser\"><a href=\"javascript:openWorkFlow('" + fileFolder.runId + "','" + fileFolder.flowTypeId+ "');\" class=\"header\"><u>" + fileFolder.runName + "</u></a><div class=\"module_body\">" + showStr + "</div></div>\n";
   importantInfoArray = new Array();
  }
  if(!contentHtml){
    contentHtml = "没有找到与“" + keyWord + "” 相关的内容";
    $('module_6').innerHTML = contentHtml;
    return;
  }
  $('module_6').innerHTML = contentHtml;
  $('module_6').innerHTML += pagePanel(el,pageIndex,pageSize,totalRecords,"getFlowWorkInfo");
}
/**
 * 信息组装
 * @param importantInfoArray
 * @param generalInfoArray
 * @param infoType
 * @return
 */
function organize_summary_info(importantInfoArray, generalInfoArray, infoType) {
  var summaryInfoStr = "";
  //组装重要信息
  if ( importantInfoArray && importantInfoArray.length > 0 ) {
    for(var i = 0 ; i < importantInfoArray.length ; i ++){
      summaryInfoStr += "<font color=darkorange>" + importantInfoArray[i][0] + "：</font>\n"
        + "<font color=blue>" + importantInfoArray[i][1]+ "</font><br>\n";
    }
  }
  //组装一般信息
  if ( generalInfoArray && generalInfoArray.length > 0 ) {
    summaryInfoStr += "<table width=90% style='font-size:9pt;'><tr><td width='28%'><hr></td><td><b><font color=lightslategray>其他信息</font></b></td><td width='35%'><hr></td></tr></table>\n";

    for(var i = 0 ; i < generalInfoArray.length ; i ++){
      summaryInfoStr += "<font color=black>" + generalInfoArray[i][0]  + "：</font>\n"
        + "<font color=black>" + generalInfoArray[i][1]  +"</font><BR>\n";
    }
  }
  return summaryInfoStr;
}

function pagePanel(el,pageIndex,pageSize,totalRecord,fun){
  var totalPage = (totalRecord % pageSize == 0) ? Math.floor(totalRecord/pageSize) : Math.floor(totalRecord/pageSize) + 1;
  if(pageIndex <= 0){
    pageIndex = 0;
  }
  if(pageIndex >= totalPage - 1){
    pageIndex = totalPage - 1;
  }
  var html = "<div align=center>";
  if(totalPage >1 && pageIndex > 0){
    html += "<A class=page href=\"javascript:" + fun + "(''," + (pageIndex - 1) + "," + pageSize + ")\">&lt;上一页</A>&nbsp;";
  }
  if(totalPage > 1 && pageIndex < totalPage - 1){
    html += "&nbsp;<A class=page href=\"javascript:" + fun + "(''," + (pageIndex + 1) + "," + pageSize + ")\">下一页&gt;</A>";
  }
  html += "</div>"
  return html;
}
/**
 * 用户档案
 * @param userId
 * @return
 */
function openUser(userId){
  var url = contextPath + "/core/funcs/userinfo/person.jsp?userId=" + userId;
  window.open(url);
}
/**
 * 内部邮件
 * @param emailId
 * @param emailBodyId
 * @return
 */
function openEmail(emailId,emailBodyId){
  var url = contextPath + "/core/funcs/email/inbox/read_email/index.jsp?mailId=" + emailId + "&seqId=" + emailBodyId ;
  openWindow(url);
}
/**
 * 公告通知
 * @param notifyId
 * @return
 */
function openNotify(seqId,format){
  var url = contextPath + "/core/funcs/notify/show/readNotify.jsp?isManage=0&seqId="+seqId; 
  var  myleft=(screen.availWidth-780)/2; 
  var mytop=100 
  var mywidth=780; 
  var myheight=500; 
  if(format == "1") { 
    myleft=0; 
    mytop=0 
    mywidth=screen.availWidth-15; 
    myheight=screen.availHeight-60; 
  } 
  window.open(url,"搜索查看","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes"); 
}
/**
 * 通讯簿
 * @param addId
 * @return
 */
function openAddress(addId){
  var url = contextPath + "/core/funcs/address/private/address/adddetail.jsp?seqId=" + addId
  openWindow(url);
}
/**
 * 文件柜
 * @param contentId
 * @param sortId
 * @return
 */
function openFileFloder(contentId,sortId){
  var url = contextPath + "/core/funcs/filefolder/read.jsp?sortId=" + sortId + "&contentId=" + contentId;
  openWindow(url);
}
/**
 * 工作流
 * @param runId
 * @param flowId
 * @return
 */
function openWorkFlow(runId,flowId){
  formView(runId,flowId);
}

function openWindow(url){
  var  myleft=(screen.availWidth-780)/2; 
  var mytop=100 
  var mywidth=780; 
  var myheight=500; 
  window.open(url,"搜索查看","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes"); 
}