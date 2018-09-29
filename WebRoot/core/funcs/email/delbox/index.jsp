<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
    <%@ page import="java.util.*,java.text.*" %>
    <%@ page import="yh.core.funcs.email.data.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="noscrollX">
<head>
<title>内部邮件</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/email.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<script type="text/javascript">
var isDelPage = true;
var pageMgr = null;

var d1 = [
          {name:'全部邮件' ,action:getEmailList , type:'1' , email:''}
          ,{name:'内部邮件',action:getEmailList , type:'2' , email:''}
          ,{name:'外部邮件' ,action:getEmailList , type:'3' , email:''}
        ];
function getEmailList() {
  var name = arguments[3].name;
  $("allEmail").innerHTML = name;
  $('type').value = arguments[3].type;
  $('email').value = arguments[3].email;
  pageMgr.search();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('msrg').innerHTML = "<span class=\"small1\">共&nbsp;<span class=\"big4\" id=\"total\"></span>&nbsp;封，<span id=\"newMail\" class=\"big4\"></span>&nbsp;封为新邮件</span>";
    $('total').innerHTML = total;
    getNewDelCount2(2,0,"newMail");
    showCntrl2('listContainer');
    showCntrl2('optM');
    showCntrl2('head');
  }else{
    WarningMsrg('已删除邮件箱无邮件记录!', 'msrg');
  }
}
function allEmailBtn(e) {
  var menu = new Menu({
    bindTo: $("allEmail"),
     menuData: d1,
     attachCtrl:true
   }, {
     border:'1px solid #CCCCCC',
     position :'absolute',
     "backgroundColor": "#fff",
     "lineHeight": "30px",
     "textAlign": "left",
     padding:'5px',
     display:"block",
     width:'150px'
  });
  menu.show(e);
}


function doInit() {
  var param = "boxId=0";
  param = encodeURI(param);
  var url =  contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/listDelBox.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    moduleName:"email",
    paramFunc: getParam,
    colums: [
       {type:"selfdef", align: "center", text: '<input type="checkbox" name="allbox" id="allbox" onClick="checkSearchAll();">', width: "8%",render:checkBoxRender},
       {type:"hidden", name:"emailBodyId", dataType:"int"},
       {type:"hidden", name:"emailId", dataType:"int"},
       {type:"data", name:"fromId", text:"发信人", width: "12%",render:userRenderByIn2},   
       {type:"data", name:"subject", text:"主题", width: "32%",render:subjectRender},
       {type:"hidden", name:"attachId"},
       {type:"data", name:"attach", text:"附件", width:"22%", dataType:"attach"},
       {type:"data", name:"sendTime", text:"发送时间", width: "18%", dataType:"dateTime",format:'yyyy-MM-dd HH:mm'},    
       {type:"data", name:"ensize", text:"大小", width: "8%",dataType:"int",render:mailSizeRender},
       {type:"hidden", name:"important"},
       {type:"hidden", name:"isWebmail"},
       {type:"hidden", name:"webFromMail"}
       ]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('total').innerHTML = total;
    getNewDelCount(2,0,"newMail");
    showCntrl('listContainer');
    showCntrl('optM');
    showCntrl('head');
  }else{
    $("optBar").hide();
    WarningMsrg('已删除邮件箱无邮件记录!', 'msrg');
  }
  listWebInfo();
}
function listWebInfo(){
  var url = contextPath + "/yh/core/funcs/email/act/YHWebmailAct/listWebmailInfo.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    if (rtJson.rtData.length > 0) {
      d1.push("-");
      for(var i = 0 ; i < rtJson.rtData.length; i ++){
        var eamilcontent = rtJson.rtData[i].email;
        if (eamilcontent.length > 25) {
          eamilcontent = eamilcontent.substring(0 , 25) + "...";  
        }
        d1.push({name:eamilcontent ,action:getEmailList , type:'3' , email: rtJson.rtData[i].seqId})
      }
    }
    
  }
}
function getParam(){
  queryParam = "type=" + $('type').value + "&email=" + $('email').value;
  return queryParam;
}
/**
* 隐藏显示控件
* 
* @param cntrlId
* @return
*/
function showCntrl2(cntrlId) {
  if ($(cntrlId)) {
    $(cntrlId).show();
  }
}
function subjectRender(cellData, recordIndex, columIndex){
  var totalRecord = this.pageInfo.totalRecord;
  var recordTotalIndex = this.pageInfo.pageIndex * this.pageInfo.pageSize + recordIndex;
  var emailBodyId = this.getCellData(recordIndex,"emailBodyId");
  var emailId = this.getCellData(recordIndex,"emailId");
  var importValue = this.getCellData(recordIndex,"important");
  var isWebmail = this.getCellData(recordIndex,"isWebmail");
  if(!isWebmail){
    isWebmail = 0;
  }
  var importHtml = "";
  if(importValue == "1"){
    importHtml = "<font color=\"red\">重要</font>";
  }else if(importValue == "2"){
    importHtml = "<font color=\"red\">非常重要</font>";
  }
  var cellDataStr = cellData.toString();
  var html = "";
  html = bindStatus(emailBodyId,2);
  return "<a href=\"" + contextPath + "/core/funcs/email/delbox/read_email/index.jsp?seqId=" + emailBodyId + "&isWebmail=" + isWebmail + "&mailId=" + emailId + "&total=" + totalRecord + "&recordIndex=" + recordTotalIndex + "\">" + cellData + "</a>&nbsp;&nbsp;" + importHtml + "&nbsp;" + html;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="98%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big" height="40px"><img src="<%=imgPath%>/cmp/email/inbox.gif" WIDTH="20" HEIGHT="20" align="absmiddle">&nbsp;&nbsp;<span class="big3"> 已删除邮件箱</span></td>
  </tr>
</table>
<form id="opForm"  >
<input type="hidden"  name="type" id="type" value="1">
<input type="hidden"  name="email" id="email" value="">
<table id="optBar" cellspacing="0" cellpadding="0" border="0" class="controlbar">
  <tr>
    <td class="controlbar-left"></td>
    <td class="controlbar-center">
    <a href="javascript:void(0)" id="allEmail" class="new_dropdown" onclick="allEmailBtn(event)">全部邮件<img src='<%=imgPath%>/subpages/menu_downarrow.png' style="margin-bottom:2px;" /></a>
      <A title=恢复该邮件到删除前的状态 href="javascript:reDeleteMail(5);"><IMG src="<%=imgPath%>/cmp/email/inmail.gif" align=absMiddle>&nbsp;恢复</a>
      <a href="javascript:deleteMail(2);" title="永久删除所选邮件"><img src="<%=imgPath%>/cmp/email/inbox/delete.gif" align="absMiddle">&nbsp;销毁</a>
      <a href="javascript:exportMail();" title="批量导出"><img src="<%=imgPath%>/cmp/email/inbox/xls.gif" align="absMiddle">&nbsp;导出Excel</a>
      <a href="javascript:exportEml();" title="单独导出"><img src="<%=imgPath%>/cmp/email/inbox/eml.gif" align="absMiddle">&nbsp;导出eml</a>
      <a href="javascript:bachDownByList();" title="附件打包下载"><img src="<%=imgPath%>/download.gif" align="absMiddle">&nbsp;附件下载</a>
      <a title=清空已删除邮件箱 href="javascript:delForShortCut(2,2,'','清空已删除邮件箱');"><img align="absMiddle" src="<%=imgPath%>/cmp/email/inbox/delete.gif">&nbsp;清空已删除邮件箱</a>
    </td>
    <td class="controlbar-right"></td>
  </tr>
</table>
</form>
<br>
<div id="listContainer" style="display:none;width:98%" ></div>
<div id="optM" style="display:none;width:98%">
</div>
<div id="msrg">
<span class="small1">共&nbsp;<span class="big4"  id="total"></span>&nbsp;封，<span id="newMail" class="big4"></span>&nbsp;封为新邮件</span>
</div>
</body>
</html>
