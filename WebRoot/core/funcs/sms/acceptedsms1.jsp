<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.sms.data.YHSmsBody, yh.core.funcs.sms.logic.YHSmsLogic" %>
<%@ page import="yh.core.funcs.sms.data.YHSmsContent" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%@ page import="java.text.*" %>
<%@ include file="/core/inc/header.jsp" %>

<%
  String pageNoString = request.getParameter("pageNo");
  int pageNo;
  if (pageNoString != null && !"".equals(pageNoString)) {
	pageNo = Integer.parseInt(pageNoString);
  }else {
	pageNo = 1;
  }
  int pageSize = 5;
  
  ArrayList<YHSmsContent> contentList = (ArrayList<YHSmsContent>)request.getAttribute("contentList");
  out.println(contentList);
  YHSmsContent smsContent = new YHSmsContent();
  YHSmsLogic smsLogic = new YHSmsLogic();
  
  int totalSmsNo = contentList.size();
  int totalPages = smsLogic.getTotalPages(totalSmsNo, pageSize);
  
  int firstPageNo = smsLogic.getFirstPageNo();
  int previousPageNo = smsLogic.getPreviousPageNo(pageNo);
  int nextPageNo = smsLogic.getNextPageNo(pageNo);
  int lastPageNo = smsLogic.getLastPageNo();
  
  int startPosition = (pageNo - 1) * pageSize;
  int endPosition = 0;
  if(totalSmsNo < (pageNo * pageSize)) {
    endPosition = totalSmsNo;
  }else {
    endPosition = pageNo * pageSize;
  }
    
  ArrayList<YHSmsContent> smsContentList = new ArrayList<YHSmsContent>();
  for(int i = startPosition; i < endPosition; i++){
    smsContent = smsContentList.get(i);
    smsContentList.add(smsContent);
  }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>已接收提醒</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/grid.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/cmp/grid.css" />
<script type="text/javascript">
  var firstPageNo = "<%=firstPageNo%>";
  var previousPageNo = "<%=previousPageNo%>";
  var nextPageNo = "<%=nextPageNo%>";
  var lastPageNo = "<%=lastPageNo%>";
  function doInit() {
    var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/getTotalSmsNum.act";
    var rtJson = getJsonRs(url, null); 

    if (rtJson.rtState == "0") {
      document.getElementById("code").innerHTML = rtJson.rtData.totalNum;
    }else {
      alert(rtJson.rtMsrg); 
    }
  }
  
  function confirmDel() {
    if(confirm("确认删除！")) {
      return true;
    }else {
      return false;
    }
  }

  function deleteSms(seqId) {
    if(!confirmDel()) {
  	  return ;
    }
        
    var url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/deleteSms.act";
    var rtJson = getJsonRs(url, "seqId=" + seqId);
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg); 
      window.location.reload();
    }else {
      alert(rtJson.rtMsrg); 
    }
  }

  function dispatchSms(seqId) {
    window.location.href = "<%=contextPath %>/core/funcs/sms/sendsms.jsp?seqId=" + seqId;
  }

  function checkAll(field) {
    var deleteFlags = document.getElementsByName("deleteFlag");
    for(var i = 0; i < deleteFlags.length; i++) {
      deleteFlags[i].checked = field.checked;
    }
  }

  function deleteAllCheckBoxs() {
    if(!confirmDel()) {
  	  return ;
    }
    
    var deleteAllFlags = document.getElementsByName("deleteFlag");
    var flag = false;
    var idStrs = "";
    for(var i = 0; i < deleteAllFlags.length; i++) {
	  if(deleteAllFlags[i].checked) {
		idStrs += deleteAllFlags[i].value + "," ;
        flag = true;
	  }	  
    }

    if(!flag) {
      alert("请选择需要删除的内部提醒！");
      return;
    }
    
    var url = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/deleteAllSms.act";
    var rtJson = getJsonRs(url, "idStrs=" + idStrs);
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg); 
      window.location.reload();
    }else {
      alert(rtJson.rtMsrg); 
    } 
  }

  function firstPage(){
    window.location.href = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/acceptedSms.act?pageNo=" + firstPageNo;
  }
  
  function previousPage(){
    window.location.href = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/acceptedSms.act?pageNo=" + previousPageNo;
  }

  function nextPage(){
    window.location.href = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/acceptedSms.act?pageNo=" + nextPageNo;
  }

  function lastPage(){
    window.location.href = "<%=contextPath %>/yh/core/funcs/sms/act/YHSmsAct/acceptedSms.act?pageNo=" + lastPageNo;
  }
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit()">
<table cellscpacing="1" cellpadding="3" width="100%">
  <tr>
    <td class="Big">
      <img src="/yh/raw/ljf/imgs/msg_fwd.gif"></img><span class="big3"> 已接收的提醒</span>
    </td>
    <td>共有<span style="color:red" id="code"></span>条短提醒</td>
    <td>
      <a href="javascript:firstPage();"><img src="/yh/raw/ljf/imgs/firstpage.gif"></img>首页</a>&nbsp;&nbsp;
      <a href="javascript:previousPage();"><img src="/yh/raw/ljf/imgs/previouspage.gif"></img>上页</a>&nbsp;&nbsp;
      <a href="javascript:nextPage();"><img src="/yh/raw/ljf/imgs/nextpage.gif"></img>下页</a>&nbsp;&nbsp;
      <a href="javascript:lastPage();"><img src="/yh/raw/ljf/imgs/lastpage.gif"></img>尾页</a>
    </td>
  </tr>
</table>

<table cellscpacing="1" cellpadding="3" width="100%">
  <tr class="TableHeader">
    <td>选择</td>
    <td>发送人</td>
    <td>内容</td>
    <td>发送时间</td>
    <td>操作</td>
  </tr>
  
   <%
     for(int i = 0; i < smsContentList.size(); i++) {
       YHSmsContent smsContentWant = smsContentList.get(i);
   %>
   <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">
     <td>
       <input type="checkbox" name="deleteFlag" value=""/>
     </td>
     <td>
       
       <%=smsContentWant.getFromId()%>
     </td>
     
     <td>
       <a href="javascript:showContent('');"><%=(smsContentWant.getContent().length() < 15) ? smsContentWant.getContent() : smsContentWant.getContent().substring(0, 15)%></a>
     </td>
     
     <td>
       <%=new SimpleDateFormat("yyyy-MM-dd").format(smsContentWant.getSendTime())%>
     </td>
     
    
   </tr>  
   <%
     }
   %>
</table>

  <div>
     <input type="checkbox" id="checkAll" name="checkAll" onclick="checkAll(this)"/><label for="checkAll">全选</label>&nbsp;&nbsp;&nbsp;&nbsp;
     <input type="button" onclick="deleteAllCheckBoxs()" value="删除"/>
  </div>
</body>
</html>