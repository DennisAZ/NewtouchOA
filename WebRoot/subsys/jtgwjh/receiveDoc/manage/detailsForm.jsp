<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
SimpleDateFormat sf = new SimpleDateFormat("yyyy");
String dayTime = sf.format(new Date());
String seqId = request.getParameter("seqId") == null ? "0" : request.getParameter("seqId");
String headerStr = "公文信息详情";
if(!seqId.equals("0")){
  headerStr = "收文详情";
}

%>
<html>
<head>
<title>公文信息详情</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">



</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td>
    </td>
  </tr>
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"><%=headerStr %></span>&nbsp;&nbsp;
    </td>
  </tr>
</table>

<form id="form1" name="form1">
	<table class="TableBlock" width="80%" align="center">
		 <tr>

      <td nowrap class="TableContent">发文单位： </td>
      <td class="TableData"colspan="5"  id="sendDeptName" name="sendDeptName" >
      </td>
	  </tr>
	  <tr>
      <td nowrap class="TableContent">文件标题： </td>
      <td class="TableData"colspan="5" name="docTitle" id="docTitle" >
     
      </td>
	  </tr>
	  <tr>
      <td nowrap class="TableContent">文件类型： </td>
      <td class="TableData" style="width: 147px;"  id="docType" >

      </td>
	    <td nowrap class="TableContent"  >文件种类： </td>
	    <td class="TableData" id="docKind" >
     
	    </td>
      <td nowrap class="TableContent" >紧急程度： </td>
      <td class="TableData"  id="urgentType"  >
       
      </td>
	  </tr>
    <tr>
      <td nowrap class="TableContent">密级： </td>
      <td class="TableData"  id="securityLevel">
       
      </td>
      <td nowrap class="TableContent">保密期限： </td>
      <td class="TableData"   id="securityTime" >
     
      </td>
      <td nowrap class="TableContent">打印份数： </td>
      <td class="TableData" id="printCount" >
       
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">文号： </td>
      <td class="TableData" id="docNo" >
    
      </td>
      <td nowrap class="TableContent">文件页数： </td>
      <td class="TableData" id="pageCount" >
        
      </td>
      <td nowrap class="TableContent">附件数： </td>
      <td class="TableData"id="attachCount">

      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">备注： </td>
      <td class="TableData" colspan="5" id="remark">

      </td>
    </tr>
    
  
       <tr id="attr_tr">
      <td nowrap class="TableContent">附件文档：</td>
      <td class="TableData" colspan="5">

      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId"></input>
        <span id="attr">无附件</span>
      </td>
    </tr>
  
    <tr align="center" class="TableControl">
      <td colspan='6' nowrap>
      <input type="hidden" name="dtoClass" value="yh.subsys.jtgwjh.docReceive.data.YHJhDocrecvInfo">
         <input type="hidden" id="seqId" name="seqId" value="">
     <input type="hidden" id="moduel" name="moduel" value="docReceive">
        <input type="hidden" name="careContent" id="careContent" value="">

        <input type="button" value="关闭" onclick="window.close();" class="BigButton">
       
      </td>
    </tr>
  </table>
</form>

<br>

</body>