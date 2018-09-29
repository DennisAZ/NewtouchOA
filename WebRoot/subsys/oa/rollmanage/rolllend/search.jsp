<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
  String roomName = request.getParameter("roomName")== null ? "" : YHUtility.encodeSpecial(request.getParameter("roomName"));
  String rollName = request.getParameter("rollName")== null ? "" : YHUtility.encodeSpecial(request.getParameter("rollName"));
  String fileCode = request.getParameter("fileCode") == null ? "" : YHUtility.encodeSpecial(request.getParameter("fileCode"));
  String fileSubject = request.getParameter("fileSubject") == null ? "" : YHUtility.encodeSpecial(request.getParameter("fileSubject")) ;
  String fileTitle = request.getParameter("fileTitle") == null ? "" : YHUtility.encodeSpecial(request.getParameter("fileTitle"));
  String fileTitleo = request.getParameter("fileTitleo") == null ? "" : YHUtility.encodeSpecial(request.getParameter("fileTitleo"));
  String sendUnit = request.getParameter("sendUnit") == null ? "" : YHUtility.encodeSpecial(request.getParameter("sendUnit"));
  String remark = request.getParameter("remark") == null ? "" : YHUtility.encodeSpecial(request.getParameter("remark"));
  
  String sendTimeMin = request.getParameter("sendTime_Min") == null ? "" : YHUtility.encodeSpecial(request.getParameter("sendTime_Min"));
  String sendTimeMax = request.getParameter("sendTime_Max") == null ? "" : YHUtility.encodeSpecial(request.getParameter("sendTime_Max"));
  
  String issueNum = request.getParameter("issueNum") == null ? "" : YHUtility.encodeSpecial(request.getParameter("issueNum"));  
  String fileYear = request.getParameter("fileYear") == null ? "" : YHUtility.encodeSpecial(request.getParameter("fileYear"));
  String fileWord = request.getParameter("fileWord") == null ? "" : YHUtility.encodeSpecial(request.getParameter("fileWord"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.net.URLEncoder"%><html>
<head>
<title>借阅记录</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rollfilelogic.js"></script>
<script type="text/javascript">

var pageMgr = null;
function doInit(){
	 var param = "";
	 param = "roomName=" + encodeURIComponent("<%=roomName%>") ;
	 param += "&rollName=" + encodeURIComponent("<%=rollName%>") ;
	 param += "&fileCode=" + encodeURIComponent("<%=fileCode%>") ;	 
	 param += "&fileSubject=" + encodeURIComponent("<%=fileSubject%>") ;
	 param += "&fileTitle=" + encodeURIComponent("<%=fileTitle%>") ;
	 param += "&fileTitleo=" + encodeURIComponent("<%=fileTitleo%>") ;
	 param += "&sendUnit=" + encodeURIComponent("<%=sendUnit%>") ;
	 param += "&remark=" + encodeURIComponent("<%=remark%>") ;
	 param += "&sendTimeMin=" + encodeURIComponent("<%=sendTimeMin%>") ;
	 param += "&sendTimeMax=" + encodeURIComponent("<%=sendTimeMax%>") ;
	 param += "&fileWord=" + encodeURIComponent("<%=fileWord%>") ;
	 param += "&fileYear=" + encodeURIComponent("<%=fileYear%>") ;
	 param += "&issueNum=" + encodeURIComponent("<%=issueNum%>") ;
	 
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsLendAct/queryLendFileJosn.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"文件id", dataType:"int"},
         {type:"data", name:"fileCode",  width: '10%', text:"文件号", render:rmsFileCodeFunc1, sortDef:{type: 0, direct:"desc"}},       
         {type:"data", name:"fileTitle",  width: '10%', text:"文件标题", render:rollFileFunc,sortDef:{type: 0, direct:"desc"}},
         {type:"data", name:"secret",  width: '10%', text:"密级", render:secretFunc}, 
         {type:"data", name:"sendUnit",  width: '10%', text:"发文单位", render:rollFileFunc,sortDef:{type: 0, direct:"desc"}}, 
         {type:"data", name:"sendDate",  width: '10%', text:"发文时间", render:sendDateFunc,sortDef:{type: 0, direct:"desc"}}, 
         {type:"data", name:"urgency",  width: '15%', text:"紧急等级", render:urgencyFunc}, 
         {type:"hidden", name:"rollId", text:"案卷id", dataType:"int"},
         {type:"selfdef", text:"操作", width: '15%',render:lendFileopts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      count = total;
      showCntrl('listContainer');
      var mrs = " 共 "+ total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      //WarningMsrg('无文件', 'msrg');
      $("msrg").show();
    }
}



</script>
</head>
<body topmargin="5" onload="doInit();">
<div id="showTitleDiv" style="">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="middle"><span class="big3"> 文件查询结果
</span>&nbsp;
    </td>
  </tr>
</table>
<br>
</div>
<div id="listContainer" style="display:none;width:100%;"></div>

<div id="delOpt" style="display:none">
	<br><center><input type="button" class="BigButton" value="返回" onclick="javascript:location.href='<%=contextPath %>/subsys/oa/rollmanage/rolllend/query.jsp'"></center>
</div>


<div id="msrg" style="display: none">
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无符合条件的文件</div>
    </td>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="javascript:location.href='<%=contextPath %>/subsys/oa/rollmanage/rolllend/query.jsp'"></center>
</div>

<div id="lendFileResultDiv" style="display: none">
<table class="MessageBox" align="center" width="280">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">案卷借阅成功！</div>
    </td>
  </tr>
</table>
<center><input type="button" value="返回" class="BigButton" onclick="showAllInfo();"></center>
</div>






</body>
</html>