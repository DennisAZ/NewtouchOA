<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="yh.core.funcs.doc.data.YHDocFlowType,java.util.List"%>
  <%@ include file="/core/inc/header.jsp" %> 
  <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
   <%
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String skin = request.getParameter("skin");
String skinJs = "messages";
if (skin != null && !"".equals(skin)) {
  skinJs = "messages_" + skin;
} else {
  skin = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>常用工作流程</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
	<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
   <script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
   <script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/skin.js"></script>
   <script type="text/javascript">
   var sortId = "<%=sortId%>";
   function doInit(){
     skinObjectToSpan(flowrun_new_flowtop);
     //异步请求,取得取数据最近的工作列表,json格式为:{rtState:"0|1", rtMsrg:"some message", rtData: [{title:'ddddddd',flowId:8,flowName:'ddd',runId:1,fileNo:'文件1',beginTime:'2003-1-12'},{title:'ddd',flowId:8,flowName:'ddd',runId:1,fileNo:'文件1',beginTime:'2003-1-12'}]};
     requestURL = contextPath + "<%=moduleSrcPath %>/act/YHFlowProcessAct";
     var url =  contextPath + "<%=moduleSrcPath %>/act/YHFlowRunAct/getRunList.act?sortId=<%=sortId%>";
     var json = getJsonRs(url);
     if(json.rtState == '1'){
       var message = json.rtMsrg;
       var div = new Element("div").update(message);
       div.align = 'center';
       document.body.appendChild(div);
       return ;
     }
     
     var flowList = json.rtData;
     if(flowList.length > 0){
       for(var i = 0 ; i < flowList.length ;i++){
   	     var flow = flowList[i];
   	     var title = flow.title;
   	     var flowName = flow.flowName;
   	     var flowId = flow.flowId;
   	  	 var runId = flow.runId;
   	     var fileNo = flow.fileNo;
   	     var beginTime = flow.beginTime;
         title += "&#10;流程名：" + flowName;
         
   	     var td1 = "<td class='auto' title='"+ title +"' ><b>";
   	     //固定流程
   	     if (flow.flowType == 1) {
   	       td1 += "&nbsp;<a href='javascript:viewGraph("+ flowId +");'>"+ flowName + "</a>";
   	     } else {
		   td1 += "&nbsp;" + flowName;
   	     }
         
   	     td1 += "</b></td>";
         
   	     var td2 = "<td><a href='javascript:formView("+ runId +"," + flowId +");'><b>流水号" + runId + "</b> "+ fileNo +" </a><br>	"+ beginTime +" </td>";
         var td3 = "<td nowrap align='center'>"
           + "<input type='button'  value='快速新建' class='SmallButtonW' name='back' onClick='createWork("+ flowId +")'>"
           +  "<input type='button'  value='新建向导' class='SmallButtonW' name='back' onClick='location=\"edit.jsp?flowId="+ flowId +"&sortId=<%=sortId%>&skin=<%=skin%>\";'>"
           + "</td>";
     
        var tr = new Element('tr',{'font-size':'10pt'});
   	  if(i%2 == 0){
   		  tr.className = "TableLine2";
	    }else{
		    tr.className = "TableLine1";
 		  }
   	  tr.onmouseover = function(){
  	    this.style.backgroundColor = '#D9E5F1';
  	  }
  	  tr.onmouseout = function(){
  		  this.style.backgroundColor = '';
  	  }
   	  $('tbodyList').appendChild(tr); 
   	  tr.update(td1 + td2 + td3);
   	 }
     }else{
       //提示
       $('tableListDiv').hide();
       $('noWorkDiv').show();
     }
   }
   var requestUrl  = contextPath + "<%=moduleSrcPath %>/act/YHFlowRunAct";
   function createWork(flowId){
     var url = requestUrl + "/createNewWork.act";
     var json = getJsonRs(url , "flowId=" + flowId);
     if(json.rtState == "0"){
   	   parent.location = contextPath + "<%=moduleContextPath %>/flowrun/list/inputform/index.jsp?skin=<%=skin %>&sortId="+ sortId +"&runId=" + json.rtData + "&flowId=" + flowId + "&prcsId=1&flowPrcs=1&isNew=1";
     }else{
       alert(json.rtMsrg);
     }
   }
   </script>
   </head>

   <body style="margin:0px;padding:0px" onload="doInit()" >
   <table  border="0" width="80%" cellspacing="0" cellpadding="3" style="margin:18px 0px;">
     <tr>
       <td class="Big"><img src="<%=imgPath %>/workflow_new_work.png" align="absbottom">&nbsp;<span class="big3"  id="span1"> </span><br>
       </td>
     </tr>
   </table>
<div id="noWorkDiv" align=center style="display:none">
<table width="300" class="MessageBox"><tbody><tr><td class="msg info"><span id="spanMsg"></span></td></tr></tbody></table></div>
<div id="tableListDiv">
       <table border="0" width="100%" class="TableList" align="center">
         <tr class="TableHeader">
           <td align="center" width=150>流程图</td>
           <td align="center"><span id="span2"></span></td>
           <td nowrap align="center" width=200>操作</td>
         </tr>
         <tbody id="tbodyList"></tbody>
     </table>
</div>
   </body>
   </html>