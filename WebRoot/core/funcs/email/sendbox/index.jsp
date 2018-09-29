<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
        <%@ page import="java.util.*,java.text.*" %>
    <%@ page import="yh.core.funcs.email.data.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<script type="text/javascript">

function moreBtn(e) {
  var d = [
    {name:' 删除所有收件人已销毁邮件',action: function() {
      delForShortCut(3,1,'','删除所有收件人已销毁邮件');
    },icon:imgPath + '/cmp/email/inbox/delete.gif'},
    {name:' 删除所有收件人未读邮件',action: function() {
      delForShortCut(3,2,'','删除所有收件人未读邮件');
    },icon:imgPath + '/cmp/email/inbox/delete.gif'},
    {name:' 删除所有收件人已读邮件',action: function() {
      delForShortCut(3,3,'','删除所有收件人已读邮件');
    },icon:imgPath + '/cmp/email/inbox/delete.gif'}
  ];
  var menu = new Menu({
    bindTo: $("more"),
     menuData: d,
     attachCtrl:true
   }, {
     border:'1px solid #CCCCCC',
     position :'absolute',
     "backgroundColor": "#fff",
     "textAlign": "left",
     "lineHeight": "30px",
     padding:'5px',
     display:"block"
  });
  menu.show(e);
}


var pageMgr = null;
function doInit() {
  var param = "boxId=0";
  param = encodeURI(param);
  var url =  contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/listSendBox.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    moduleName:"email",
    colums: [
       {type:"selfdef", text: '<input type="checkbox" name="allbox" id="allbox" onClick="checkSearchAll();">', align: "center", width: "8%",render:checkBoxRender},
       {type:"hidden", name:"emailBodyId"},
       {type:"data", name:"toId", text:"收信人", width: "12%",render:userRender},
       {type:"hidden", name:"copyToId"},   
       {type:"hidden", name:"secretToId"}, 
       {type:"data", name:"subject", text:"主题", width: "20%",render:subjectRender},
       {type:"hidden", name:"attachId"},
       {type:"data", name:"attach", text:"附件", width: "15%", dataType:"attach"},
       {type:"data", name:"sendTime", text:"发送时间", width: "15%", dataType:"dateTime",format:'yyyy-MM-dd HH:mm'},    
       {type:"data", name:"ensize", text:"大小", width: "8%",dataType:"int",render:mailSizeRender},
       {type:"selfdef", width: "13%",text:"操作", render:optRender},
       {type:"hidden", name:"important"}
       ]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('total').innerHTML = total;
    getNewDelCount(3,0,"delMail");
    showCntrl('listContainer');
    showCntrl('optM');
    showCntrl('head');
    
  }else{
    $("optBar").hide();
    WarningMsrg('已发送邮件箱中无邮件！', 'msrg');
  }
}
function subjectRender(cellData, recordIndex, columIndex){
  var totalRecord = this.pageInfo.totalRecord;
  var recordTotalIndex = this.pageInfo.pageIndex * this.pageInfo.pageSize + recordIndex;
  var emailBodyId = this.getCellData(recordIndex,"emailBodyId");
  var importValue = this.getCellData(recordIndex,"important");
  var importHtml = "";
  if(importValue == "1"){
    importHtml = "<font color=\"red\">重要</font>";
  }else if(importValue == "2"){
    importHtml = "<font color=\"red\">非常重要</font>";
  }
 return "<a href=\"" + contextPath + "/core/funcs/email/sendbox/read_email/index.jsp?seqId=" + emailBodyId + "&total=" + totalRecord + "&recordIndex=" + recordTotalIndex + "\">" + cellData + "</a>" + "&nbsp;" + importHtml;
}
function optRender(cellData, recordIndex, columIndex){
  var emailBodyId = this.getCellData(recordIndex,"emailBodyId");
  var status = mailStatus(emailBodyId,1);
  var html = "";
  var cellDataStr = this.getCellData(recordIndex,"toId").toString();
  if( cellDataStr.indexOf(",") == -1){
    if(status == "2"){
      html += "<A href=\"" + contextPath + "/core/funcs/email/new/index.jsp?sendEdit=1&seqId=" + emailBodyId + "\">编辑</A>&nbsp;&nbsp;"; 
     }
     html += "<A href=\"" + contextPath + "/core/funcs/email/new/index.jsp?resend=1&emailId=" + emailBodyId + "\">再次发送</A>&nbsp;";
  }
 return html;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="98%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big" height="40px"><img src="<%=imgPath%>/cmp/email/sentbox.gif" WIDTH="20" HEIGHT="20" align="absmiddle">&nbsp;&nbsp;<span class="big3"> 已发送邮件箱</span></td>
    </tr>
</table>

<table id="optBar" cellspacing="0" cellpadding="0" border="0" class="controlbar">
  <tr>
    <td class="controlbar-left"></td>
    <td class="controlbar-center">
			<a href="javascript:deleteMail(3);" title="删除所选邮件"><img src="<%=imgPath%>/cmp/email/inbox/delete.gif" align="absMiddle">&nbsp;删除</a>
			<a href="javascript:exportMail();" title="批量导出"><img src="<%=imgPath%>/cmp/email/inbox//xls.gif" align="absMiddle">&nbsp;导出Excel</a>
			<a href="javascript:exportEml();" title="单独导出"><img src="<%=imgPath%>/cmp/email/inbox/eml.gif" align="absMiddle">&nbsp;导出eml</a>
			<a href="javascript:bachDownByList();" title="附件打包下载"><img src="<%=imgPath%>/download.gif" align="absMiddle">&nbsp;附件下载</a>
      <a href="javascript:void(0)" id="more" class="new_dropdown" onclick="moreBtn(event)">更多<img src='<%=imgPath%>/subpages/menu_downarrow.png' style="margin-bottom:2px;" /></a>
    </td>
    <td class="controlbar-right"></td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:98%"></div>
<div id="optM"  style="display:none;width:98%">
</div>
<div id="msrg">
<span class="small1">共&nbsp;<span class="big4" id="total"></span>&nbsp;封，<span id="delMail" class="big4"></span>封&nbsp;收件人已删除</span>
</div>
</body>
