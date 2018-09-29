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
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
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

<SCRIPT>
function check_all()
{
 for (i=0;i<document.all("email_select").length;i++)
 {
   if(document.all("allbox").checked)
      document.all("email_select").item(i).checked=true;
   else
      document.all("email_select").item(i).checked=false;
 }

 if(i==0)
 {
   if(document.all("allbox").checked)
      document.all("email_select").checked=true;
   else
      document.all("email_select").checked=false;
 }
}

function check_one(el)
{
   if(!el.checked)
      document.all("allbox").checked=false;
}


function order_by(field,asc_desc)
{
  window.location="index.php?PAGE_START=<?=$PAGE_START?>&BOX_ID=<?=$BOX_ID?>&FIELD="+field+"&ASC_DESC="+asc_desc;
}

</SCRIPT>
<script type="text/javascript">
var pageMgr = null;
function doInit() {
  var param = "boxId=0";
  param = encodeURI(param);
  var url =  contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/listNewBox.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    moduleName:"email",
    colums: [
       {type:"hidden", name:"emailBodyId", dataType:"int"},
       {type:"hidden", name:"emailId", dataType:"int"},
       {type:"data", name:"fromId", text:"发信人", width: "12%",render:userRenderByIn, sortDef:{type:3,direct:"asc"}},   
       {type:"data", name:"subject", text:"主题", width: "40%"},
       {type:"hidden", name:"attachId"},
       {type:"data", name:"attach", text:"附件", width: "25%", dataType:"attach"},
       {type:"data", name:"sendTime", text:"发送时间", width: "15%", dataType:"dateTime",format:'yyyy-MM-dd HH:mm', sortDef:{type:0,direct:"desc"}},    
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
    showCntrl('listContainer');
    showCntrl('head');
  }else{
    WarningMsrg('无新邮件！', 'msrg');
  }
}
function subjectRender(cellData, recordIndex, columIndex){
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
  var html = bindStatus(emailBodyId,2,emailId);
  var cellDataStr = cellData.toString();
  var url = contextPath + "/core/funcs/email/inbox/read_email/index.jsp";
  if(isWebmail == "1"){
    url = contextPath + "/core/funcs/email/webbox/read_email/index.jsp";
  }
  return "<a href=\"" + url + "?mailId="+emailId+"&seqId=" + emailBodyId + "&mailId=" + emailId + "&total=" + totalRecord + "&recordIndex=" + recordTotalIndex + "&boxId=0" + "\">" + cellData + "</a>&nbsp;&nbsp;" + importHtml + "&nbsp;"  + html;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="98%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big" height="40px"><img src="<%=imgPath%>/cmp/email/nm.png" WIDTH="20" HEIGHT="20" align="absmiddle"><span class="big3"> 未读邮件</span></td>
    <td valign="bottom" id="head" style="display:none"><span class="small1">共&nbsp;<span class="big4" id="total"></span>&nbsp;封新邮件</td>
</table>
<div id="listContainer" style="display:none;width:98%;"></div>
<div id="msrg"></div>
</body>

<script>
function get_checked() {
	checked_str="";
	for(i=0;i<document.all("email_select").length;i++) {
	  el=document.all("email_select").item(i);
	  if(el.checked){  
		  val=el.value;
	    checked_str += val + ",";
	  }
	}
  if(i == 0) {
    el = document.all("email_select");
    if(el.checked) { 
      val=el.value;
      checked_str += val + ",";
    }
  }
  return checked_str;
}
function change_box() {
  delete_str = get_checked();
  if(delete_str == "") {
    alert("要转移邮件，请至少选择其中一封。");
    document.form1.reset();
    return;
  }
  box_id=document.all("BOX_ID").value;
  url="../change_box.php?DELETE_STR="+ delete_str +"&BOX_ID="+box_id+"&BOX_ID_OLD=<?=$BOX_ID?>&PAGE_START=<?=$PAGE_START?>&FIELD=<?=$FIELD?>&ASC_DESC=<?=$ASC_DESC?>";
  location = url;
}

function delete_mail() {
  delete_str = get_checked();
  if(delete_str == "") {
     alert("要删除邮件，请至少选择其中一封。");
     return;
  }
  msg='确认要删除所选邮件吗？';
  if(window.confirm(msg)) {
    var url = "<%=contextPath %>/yh/core/funcs/email/act/YHInnerEMailAct/deletM.act?bodyId="+ delete_str + "&deType=1&PAGE_START=&BOX_ID=&FIELD=&ASC_DESC=";
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      location = "<%=contextPath%>/yh/core/funcs/email/act/YHInnerEMailAct/listInBox.act?boxId=0";
    } else{
      location = "<%=contextPath%>/yh/core/funcs/email/act/YHInnerEMailAct/listInBox.act?boxId=0";
      alert(rtJson.rtMsrg);
    }
  }
}
function delete_mail2() {
  delete_str=get_checked();
  if(delete_str == "") {
     alert("要永久删除邮件，请至少选择其中一封。");
     return;
  }
  msg='确认要永久删除所选邮件吗？';
  if(window.confirm(msg)) {
    url="delete2.php?DELETE_STR="+ delete_str +"&PAGE_START=<?=$PAGE_START?>&BOX_ID=<?=$BOX_ID?>&FIELD=<?=$FIELD?>&ASC_DESC=<?=$ASC_DESC?>";
    location=url;
  }
}
function export_mail() {
  delete_str=get_checked();
  if(delete_str == "") {
    alert("要导出邮件，请至少选择其中一封。");
    return;
  }
  window.open('../export_bat.php?DELETE_STR='+ delete_str);
}
function export_eml() {
  delete_str = get_checked();
  if(delete_str == "") {
    alert("要导出邮件，请至少选择其中一封");
    return;
  }
  window.location='../export.php?DELETE_STR='+ delete_str;
}
function read_mail() {
  read_str = get_checked();
  if(read_str == "") {
     alert("要将邮件标记为已读，请至少选择其中一封。");
     return;
  }
  msg='确认要将所选邮件标记为已读吗？';
  if(window.confirm(msg)) {
    url = "read_ok.php?READ_STR="+ read_str +"&PAGE_START=<?=$PAGE_START?>&BOX_ID=<?=$BOX_ID?>&FIELD=<?=$FIELD?>&ASC_DESC=<?=$ASC_DESC?>";
    location = url;
  }
}
function del_mail() {
  msg='确认要删除所有已读邮件？';
  if(window.confirm(msg)) {
    url = "del_email.php?BOX_ID=<?=$BOX_ID?>&PAGE_START=<?=$PAGE_START?>&FIELD=<?=$FIELD?>&ASC_DESC=<?=$ASC_DESC?>";
    location = url;
  }
}
</script>