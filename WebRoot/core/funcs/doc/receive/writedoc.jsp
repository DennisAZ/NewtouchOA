<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
  <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ page import="yh.subsys.oa.book.data.*"%>
<%@ page  import="java.util.List,java.util.Set,java.util.Map, yh.core.funcs.doc.receive.data.YHDocReceive, yh.core.funcs.doc.receive.data.YHDocConst"%>
<%
 List<YHDocReceive> docs = (List)request.getAttribute("docs");
 String flag = (String)request.getAttribute("ftype");
 YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
 String webroot = request.getRealPath("/");
 Map<String , String> docRecFlowType = (Map)request.getAttribute("flowType");
 Set<String> set = docRecFlowType.keySet();
 String sortIds =  (String)request.getAttribute("sortId");
 String skin = "receive";
%>
<html>
<head>
<title></title>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/notify/js/openWin.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/workflow.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/utility.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/doc/receive/js/common.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/Javascript">
function read(seqId){
  window.location.href = contextPath + "/yh/core/funcs/doc/receive/act/YHDocReceiveAct/updateReadDocReceive.act?seqId=" + seqId;
  return true;
}
function cost_balance(seqId){
  var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocReceiveAct/findDocFileAjax.act?seqId="+seqId;
  var rtJson = getJsonRs(url); 
 
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData[0];
  var prcs2 = rtJson.rtData[1];
  
  var docNo = prcs.docNo;
  var grade = prcs.confLevel;
  var migrade = prcs.confLevelName;
  var reDate = prcs.resDate;
  var sponsor = prcs.sponsor;
  var fromUnits = prcs.fromUnits;
  var oppDocNo = prcs.oppDocNo;
  var title = prcs.title;
  var pishi = prcs.instruct;
  var runId = prcs.runId;
  var toUserName = prcs.toUserName;
  var deptName = prcs2.deptName;
  var status = prcs.status;
  var param = '等级='+ grade + "&密级="+ encodeURIComponent(migrade) +"&收文日期="+ reDate +"&承办人="+encodeURIComponent(toUserName)+
             "&来文单位="+encodeURIComponent(fromUnits)+"&原文编号="+encodeURIComponent(oppDocNo)+"&标题="+encodeURIComponent(title)+
             "&领导批示="+encodeURIComponent(pishi)+"&收文ID="+seqId +"&文号="+ docNo+ "&联系人="+encodeURIComponent('<%=user.getUserName()%>')+
             "&部门="+encodeURIComponent(deptName) ;
  if(runId != null && runId != "" && runId > 0){
    formViewByRunId(runId);
    return;
  }else{
    var constName = $('flowTypeSelect_' + seqId).value;
    if (constName) {
      createNewWork2(constName,param , true , seqId);
    }else {
      alert("请选择流程！");
    }
  }
}

function createNewWork2(flowId , par , isNotOpenWindow , seqId){
  var url = contextPath +   moduleSrcPath + "/receive/act/YHDocReceiveHandlerAct/createWork.act?seqId=" + seqId;
  if (par) {
    par = "flowId=" + flowId + "&" + par;
  } else {
    par = 'flowId=' + flowId;
  }
  var json = getJsonRs(url ,  par);
  if(json.rtState == "0"){
    var runId = json.rtData.runId;
    var url2 =   contextPath +   moduleContextPath +"/flowrunRec/list/inputform/index.jsp?skin=<%=skin %>&sortId="+ sortId +"&runId=" + runId + "&flowId=" + flowId + "&prcsId=1&flowPrcs=1&isNew=1";
    if (isNotOpenWindow) {
      parent.location.href = url2;
    } else {
      window.open(url2);
    }
  
  }else{
    alert(json.rtMsrg);
  }
}
var sortId = "<%=sortIds %>";
</script>
</head>
 
<body class="bodycolor" topmargin="5">
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
<tr>
  <td class="Big"><img src="<%=imgPath %>/book.gif" width="22" height="18"><span class="big3"> 
          收文承办
  </span>&nbsp;
  </td>
  <td valign="bottom" class="small1" align="center">共<span class="big4">&nbsp;<%=docs.size() %></span>&nbsp;条记录
  </td>
</tr>
</table>
 
<table class="TableList" width="95%" align="center">
<tr class="TableHeader">
  <td nowrap align="center">标题&nbsp;</td>
  <td nowrap align="center">来文文号&nbsp;</td>  
  <td nowrap align="center">份数&nbsp;</td>
  <td nowrap align="center">来文单位&nbsp;</td>
  <td nowrap align="center">来文正文&nbsp;</td>
  <td nowrap align="center">密级&nbsp;</td>
  <td nowrap align="center">类型&nbsp;</td>
  <td nowrap align="center">登记人&nbsp;</td>
  <td nowrap align="center">步骤&nbsp;</td>
  <td nowrap align="center" >操作&nbsp;</td>
</tr>
<%
  for(int i=0; i<docs.size(); i++){%>
    <tr class="TableLine1">
		  <td align="center"><%=docs.get(i).getTitle() %>&nbsp;</td>
		  <td align="center"><%=docs.get(i).getOppdocNo() %>&nbsp;</td>
		  <td align="center"><%=docs.get(i).getCopies() %>&nbsp;</td>
		  <td align="center"><%=docs.get(i).getFromUnits() %>&nbsp;</td>
      
      <% 
      String docName = docs.get(i).getRecDocName() ;
      String docId = docs.get(i).getRecDocId();
      %>
      <td align="center" >
      <div  id="showAtt<%=docs.get(i).getSeq_id() %>"></div>
        <% 
        if (!YHUtility.isNullorEmpty(docId)) {
          %>
      <script type="text/javascript">
        attachMenuUtil("showAtt<%=docs.get(i).getSeq_id() %>","doc",null,"<%=docName %>","<%=docId %>",true,"<%=docs.get(i).getSeq_id() %>");
      </script>
       <% } else { out.print("无"); }  %>
      </td>
		  <td align="center"><%=docs.get(i).getConfLevelName(webroot) %>&nbsp;</td>
		  <td align="center"><%=docs.get(i).getDocTypeName(webroot) %>&nbsp;</td>     
		  <td align="center"><%=docs.get(i).getFromUserName() %>&nbsp;</td> 
		  <%if(docs.get(i).getRunId()!=null && docs.get(i).getRunId()!=""){%>
		  <td align="center"><a href="javascript:flowView('<%=docs.get(i).getNext().getRunId() %>','<%=docs.get(i).getNext().getFlowId() %>','','');"><%=docs.get(i).getNext().getPrcsName() %></a>&nbsp;</td>
		  <%}else{%>
		    <td align="center"><font color="red"><%=docs.get(i).getNext().getPrcsName() %></font>&nbsp;</td> 
		  <%}%> 
		  <td nowrap align="center">
		     <%
		       if(1 == (docs.get(i).getSendStauts())){%>
                <%if(docs.get(i).getRunId()==null || docs.get(i).getRunId()==""){
                  int seqId = docs.get(i).getSeq_id();
                %>选择流程：<select id="flowTypeSelect_<%=seqId %>"> <%
                out.print("<option value=\"\">请选择流程</option>" );
                for (String ss : set) {
                  if (!"".equals(ss)) {
                    out.print("<option value=\"" + ss + "\">" + docRecFlowType.get(ss) + "</option>" );
                  }
                }
                %>
                </select>
                <% } %>
		         <a href="javascript:void(0);" onclick="cost_balance('<%=docs.get(i).getSeq_id() %>'); return false;"><%if(docs.get(i).getRunId()==null || docs.get(i).getRunId()==""){%>承办<%}else{%>预览<%}%></a>	&nbsp;	    
		      <%}%>&nbsp;
	    </td>
   </tr>
 <%}%>
</table>
</body>
</html>

