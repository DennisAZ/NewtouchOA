<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
    <%@ page import="java.util.*,java.text.*" %>
<%@ page import="yh.core.funcs.email.data.*" %>
<%
  String queryType = request.getParameter("queryType");
  String boxId = request.getParameter("boxId");
  String startDateStr = request.getParameter("startDate") == null ? "" :   request.getParameter("startDate");
  String endDateStr = request.getParameter("endDate") == null ? "" :   request.getParameter("endDate") ;
  String userId = request.getParameter("userId") == null ? "" :  request.getParameter("userId");
  String attachmentName = request.getParameter("attachmentName") == null ? "" :   request.getParameter("attachmentName") ;
  String subject = request.getParameter("subject") == null ? "" :   request.getParameter("subject") ;
  String key1 = request.getParameter("key1") == null ? "" :   request.getParameter("key1") ;
  String key2 = request.getParameter("key2") == null ? "" :   request.getParameter("key2") ;
  String key3 = request.getParameter("key3") == null ? "" :  request.getParameter("key3");
  String boxName = request.getParameter("boxName") == null ? "收件箱" :  request.getParameter("boxName");
  String mailStatus = request.getParameter("mailStatus") == null ? "" :  request.getParameter("mailStatus");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="noscrollX">
<head>
<title>内部邮件</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/email.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript">
var pageMgr = null;
var boxId = "<%=boxId%>";
var queryType = "<%=queryType%>";
var d1 = [
          {name:'全部邮件' ,action:getEmailList , type:'1' , email:''}
          ,{name:'内部邮件',action:getEmailList , type:'2' , email:''}
          ,{name:'外部邮件' ,action:getEmailList , type:'3' , email:''}
        ];

function moreBtn(e) {
  var d = [
	  {name:' 附件打包下载',action: bachDownByList,icon:imgPath + '/download.gif'},
	  {name:' 删除所有已读邮件',action:function(){
	    delForShortCut(1,1,'<%=boxId %>','删除所有已读邮件')
	  },icon:imgPath + '//cmp/email/inbox/delete.gif'}
  ];
  var menu = new Menu({
    bindTo: $("more"),
     menuData: d,
     attachCtrl:true
   }, {
     border:'1px solid #CCCCCC',
     position :'absolute',
     "backgroundColor": "#fff",
     "lineHeight": "30px",
     "textAlign": "left",
     padding:'5px',
     display:"block"
  });
  menu.show(e);
}
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
    showCntrl2('listContainer');
    showCntrl2('optM');
    showCntrl2('head');
    getNewDelCount2(1,'<%=boxId%>',"newMail");
  }else{
    WarningMsrg('<%=boxName%> 中无邮件！', 'msrg');
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
  bindBoxName();
  initSelectBox();
  doSearch();
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
function doSearch() {
  var param = "";
  if(queryType){
    param =  "boxId=" + boxId + "&queryType=<%=queryType%>&startDate=<%=startDateStr%>&mailStatus=<%=mailStatus%>&endDate=<%=endDateStr%>&subject=<%=subject%>&key1=<%=key1%>&key2=<%=key2%>&key3=<%=key3%>&userId=<%=userId%>";
  } else {
    param =  "boxId=" + boxId ;
  }
  param = encodeURI(param);
  var url =  contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/listInBox.act?" + param;
  var selAll = '<input type="checkbox" name="allbox" id="allbox" onClick="checkSearchAll();">';
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    moduleName:"email",
    paramFunc: getParam,
    colums: [
       {type:"selfdef", text:selAll, width: "8%", align: "center", render:checkBoxRender},
       {type:"hidden", name:"emailBodyId"},
       {type:"hidden", name:"emailId"},
       {type:"data", name:"fromId", text:"发信人", width: "12%",render:userRenderByIn, sortDef:{type:3,direct:"asc"}},   
       {type:"data", name:"subject", text:"主题[点击前为新邮件]", width: "30%"},
       {type:"hidden", name:"attachId"},
       {type:"data", name:"attach", text:"附件", width: "20%", dataType:"attach"},
       {type:"data", name:"sendTime", text:"发送时间", width: "18%", dataType:"dateTime",format:'yyyy-MM-dd HH:mm', sortDef:{type:0,direct:"desc"}},    
       {type:"data", name:"ensize", text:"大小", width:  "8%",dataType:"int",render:mailSizeRender},
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
    showCntrl('listContainer');
    showCntrl('optM');
    showCntrl('head');
    getNewDelCount(1,'<%=boxId%>',"newMail");
  }else{
    $("optBar").hide();
    WarningMsrg('<%=boxName%> 中无邮件！', 'msrg');
  }
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
function subjectRender(cellData, recordIndex, columIndex,r){
  //alert(recordIndex);
  var totalRecord = this.pageInfo.totalRecord;
  var recordTotalIndex = this.pageInfo.pageIndex * this.pageInfo.pageSize + recordIndex;
  var emailBodyId = this.getCellData(recordIndex,"emailBodyId");
  var emailId = this.getCellData(recordIndex,"emailId");
  var importValue = this.getCellData(recordIndex,"important");
  var isWebmail = this.getCellData(recordIndex,"isWebmail");
  var importHtml = "";
  if(importValue == "1"){
    importHtml = "<font color=\"red\">重要</font>";
  }else if(importValue == "2"){
    importHtml = "<font color=\"red\">非常重要</font>";
  }
  var cellDataStr = cellData.toString();
  var html = "";
  html = bindStatus(emailBodyId,2,emailId);
  var url = contextPath + "/core/funcs/email/inbox/read_email/index.jsp";
  if(isWebmail == "1"){
    url = contextPath + "/core/funcs/email/webbox/read_email/index.jsp";
  }
  return "<a href=\"" + url + "?mailId="+emailId+"&seqId=" + emailBodyId + "&total=" + totalRecord + "&recordIndex=" + recordTotalIndex + "&boxId=<%=boxId%>\">" + cellData + "</a>&nbsp;&nbsp;"  +  importHtml + "&nbsp;" + html ;
}

function initSelectBox(){
  var url = contextPath + "/yh/core/funcs/email/act/YHEmailBoxAct/getSelfForLi.act";
  var rtJson = getJsonRs(url);
  var selectObj =  $('selectBoxId');
  if(rtJson.rtState == "0"){
    for(var i = 0 ; i < rtJson.rtData.length ;i++){
     var str = "<option value=\"" + rtJson.rtData[i].box.seqId + "\" ";
     if(boxId && boxId == rtJson.rtData[i].box.seqId){
       str += " selected "; 
     }
     str += ">" + rtJson.rtData[i].box.boxName + "</option>";
     selectObj.insert(str,'bottom');
    }
    if(boxId == 0){
      selectObj.options[0].selected = true;
    }else {
      for(var i = 1 ; i < selectObj.options.length; i ++){
         if(selectObj.options[i].value == boxId){
           selectObj.options[i].selected = true;
         }
       }
     }
   }
}

function bindBoxName(){
  if(boxId == "0"){
    $('boxName').innerHTML = "收件箱";
  }else{
    var url = contextPath + "/yh/core/funcs/email/act/YHEmailBoxAct/getBoxName.act?boxId="+boxId;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      var boxName = rtJson.rtData.boxName;
      $('boxName').innerHTML = boxName;
    }
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table  border="0" width="98%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big" height="40px"><img src="<%=imgPath%>/cmp/email/inbox.gif" WIDTH="20" HEIGHT="20" align="absmiddle">&nbsp;&nbsp;<span class="big3" id="boxName"> <%=boxName%></span></td>
    </tr>
</table>
<form id="opForm"  >
<input type="hidden"  name="type" id="type" value="1">
<input type="hidden"  name="email" id="email" value="">
<table id="optBar" cellspacing="0" cellpadding="0" border="0" class="controlbar">
  <tr>
    <td class="controlbar-center">
      <a href="javascript:void(0)" id="allEmail" class="new_dropdown" onclick="allEmailBtn(event)">全部邮件
		<img src='<%=imgPath%>/subpages/menu_downarrow.png' style="margin-bottom:2px;" /></a>
      <a href="javascript:readMail();" title="标记为已读"><img src="<%=imgPath%>/cmp/email/inbox/email_open.gif" align="absMiddle">&nbsp;已读</a>
      <a href="javascript:deleteMail(1);" title="删除所选邮件"><img src="<%=imgPath%>/cmp/email/inbox/delete.gif" align="absMiddle">&nbsp;删除</a>
      <a href="javascript:deleteMail(2);" title="永久删除所选邮件"><img src="<%=imgPath%>/cmp/email/inbox/delete.gif" align="absMiddle">&nbsp;销毁</a>
      <a href="javascript:exportMail();" title="批量导出"><img src="<%=imgPath%>/cmp/email/inbox/xls.gif" align="absMiddle">&nbsp;导出Excel</a>
      <a href="javascript:exportEml();" title="单独导出"><img src="<%=imgPath%>/cmp/email/inbox/eml.gif" align="absMiddle">&nbsp;导出eml</a>
	  <a href="javascript:void(0)" id="more" class="new_dropdown" onclick="moreBtn(event)">更多
		<img src='<%=imgPath%>/subpages/menu_downarrow.png' style="margin-bottom:2px;" />
	  </a>
            <span> 转到
		   <select id="selectBoxId"  onChange="changeBox(this);">
		     <option value=0>收件箱</option>
		   </select>
		   <span>
		   
    </td>
  </tr>
</table>
</form>
<br>
<div id="listContainer" style="display:none;width:98%"></div>
<div id="optM" style="display:none;width:98%">
</div>
<div id="msrg">
<span class="small1">共&nbsp;<span class="big4" id="total"></span>&nbsp;封，<span id="newMail" class="big4"></span>&nbsp;封为新邮件</span>
</div>
</body>
</html>
