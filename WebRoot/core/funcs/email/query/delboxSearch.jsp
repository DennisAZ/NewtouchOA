<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
    <%@ page import="java.util.*,java.text.*" %>
    <%@ page import="yh.core.funcs.email.data.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
  String boxId = request.getParameter("boxId");
  String startDateStr = request.getParameter("startTime") == null ? "" :   request.getParameter("startTime");
  String endDateStr = request.getParameter("endTime") == null ? "" :   request.getParameter("endTime") ;
  String userId = request.getParameter("userId") == null ? "" :  request.getParameter("userId");
  String attachmentName = request.getParameter("attachmentName") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("attachmentName")) ;
  String subject = request.getParameter("subject") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("subject")) ;
  String key1 = request.getParameter("key1") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("key1")) ;
  String key2 = request.getParameter("key2") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("key2"));
  String key3 = request.getParameter("key3") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("key3"));
  String mailStatus = request.getParameter("mailStatus") == null ? "" :  request.getParameter("mailStatus");
%>
<title>内部邮件</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/email.css" type="text/css" />
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
var pageMgr = null;
var boxId = "<%=boxId%>";
var queryType = "1";
function doInit() {
  if(queryType){
    param =  "boxId=" + boxId + "&queryType=" + queryType + "&startDate=<%=startDateStr%>&mailStatus=<%=mailStatus%>&endDate=<%=endDateStr%>&subject=<%=subject%>&key1=<%=key1%>&key2=<%=key2%>&key3=<%=key3%>&userId=<%=userId%>&attachmentName=<%=attachmentName%>";
  } else {
    param =  "boxId=" + boxId;
  }
  param = encodeURI(param);
  var url =  contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/listDelBox.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    moduleName:"email",
    colums: [
             {type:"selfdef", align: "center", text: '<input type="checkbox" name="allbox" id="allbox" onClick="checkSearchAll();">', width: "8%",render:checkBoxRender},
             {type:"hidden", name:"emailBodyId", dataType:"int"},
             {type:"hidden", name:"emailId", dataType:"int"},
             {type:"data", name:"fromId", text:"发信人", width: "12%",render:userRenderByIn},   
             {type:"data", name:"subject", text:"主题", width: "32%",render:subjectRender},
             {type:"hidden", name:"attachId"},
             {type:"data", name:"attach", text:"附件", width:"22", dataType:"attach"},
             {type:"data", name:"sendTime", text:"发送时间", width: "18%", dataType:"dateTime",format:'yyyy-MM-dd HH:mm'},    
             {type:"data", name:"ensize", text:"大小", width: "8",dataType:"int",render:mailSizeRender},
             {type:"hidden", name:"important"},
             {type:"hidden", name:"isWebmail"},
             {type:"hidden", name:"webFromMail"}
             ]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    showCntrl('optM');
  }else{
    $("optBar").hide();
    WarningMsrg('未找到符合条件的邮件！', 'msrg');
  }
}
function subjectRender(cellData, recordIndex, columIndex){
  var emailBodyId = this.getCellData(recordIndex,"emailBodyId");
  var emailId = this.getCellData(recordIndex,"emailId");
  var isWebmail = this.getCellData(recordIndex,"isWebmail");
  var cellDataStr = cellData.toString();
  var html = "";
  html = bindStatus(emailBodyId,2);
  return "<a href=\"" + contextPath + "/core/funcs/email/delbox/read_email/index.jsp?isQuery=1&seqId=" + emailBodyId +  "&isWebmail=" + isWebmail + "&mailId=" + emailId + "\">" + cellData + "</a>&nbsp;&nbsp;" + html;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="98%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big" height="40px"><img src="<%=imgPath%>/cmp/email/inbox.gif" WIDTH="20" HEIGHT="20" align="absmiddle">&nbsp;&nbsp;<span class="big3"> 已删除邮件箱  --- 查询结果</span></td>
    </tr>
</table>
<table id="optBar" cellspacing="0" cellpadding="0" border="0" class="controlbar">
  <tr>
    <td class="controlbar-left"></td>
    <td class="controlbar-center">
	    <a title=恢复该邮件到删除前的状态 href="javascript:reDeleteMail(5);"><IMG src="<%=imgPath%>/cmp/email/inmail.gif" align=absMiddle>恢复</a>&nbsp;
	    <a href="javascript:deleteMail(2);" title="永久删除所选邮件"><img src="<%=imgPath%>/cmp/email/inbox/delete.gif" align="absMiddle">永久删除</a>&nbsp;
	    <a href="javascript:exportMail();" title="批量导出"><img src="<%=imgPath%>/cmp/email/inbox/xls.gif" align="absMiddle">导出Excel</a>&nbsp;
	    <a href="javascript:exportEml();" title="单独导出"><img src="<%=imgPath%>/cmp/email/inbox/eml.gif" align="absMiddle">导出eml</a>&nbsp;
      <a title=清空已删除邮件箱 href="javascript:delForShortCut(2,2,'','清空已删除邮件箱');"><IMG src="<%=imgPath%>/cmp/email/inbox/delete.gif">&nbsp;清空已删除邮件箱</a>
    </td>
    <td class="controlbar-right"></td>
  </tr>
</table>
<br/>
<div id="listContainer" style="display:none;width:98%"></div>
<div id="optM" style="display:none;width:100%">
</div>
<div id="msrg"></div>
<div style="height:5px"></div>
<center><input type="button" class="BigButton" value="返回" onclick="javascript:location='<%=contextPath %>/core/funcs/email/query/index.jsp;'"></center>
</body>
</html>
