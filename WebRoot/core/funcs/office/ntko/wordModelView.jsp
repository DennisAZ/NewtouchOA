<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String secOcMark = request.getParameter("secOcMark") == null ? "" : request.getParameter("secOcMark");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <link rel="stylesheet" href = "<%=cssPath%>/style.css">
 <script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/office/ntko/tangerUtil.js"></script>
<SCRIPT> <!--
var secOcMark = "<%=secOcMark%>";
var menu_enter="";
 
function borderize_on(e)
{
 color="#708DDF";
 source3=event.srcElement
 
 if(source3.className=="menulines" && source3!=menu_enter)
    source3.style.backgroundColor=color;
}
 
function borderize_on1(e)
{
 for (i=0; i<document.all.length; i++)
 { document.all(i).style.borderColor="";
   document.all(i).style.backgroundColor="";
   document.all(i).style.color="";
   document.all(i).style.fontWeight="";
 }
 
 color="#003FBF";
 source3=event.srcElement
 
 if(source3.className=="menulines")
 { source3.style.borderColor="black";
   source3.style.backgroundColor=color;
   source3.style.color="white";
   source3.style.fontWeight="bold";
 }
 
 menu_enter=source3;
}
 
function borderize_off(e)
{
 source4=event.srcElement
 
 if(source4.className=="menulines" && source4!=menu_enter)
    {source4.style.backgroundColor="";
     source4.style.borderColor="";
    }
}
 
//-->
</SCRIPT>
<script Language="JavaScript"> 
var parent_window =window.opener;
 
function click_model(attrId,attrName){
  parent_window.TANGER_OCX_AddDocHeader(attrId,attrName);
  if(secOcMark == "3"){
    parent_window.TANGER_OCX_SetMarkModify(false);
  }   
  window.close();
}
function doInit(){
  var url = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/getWordModel.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == '0'){
    if(rtJson.rtData.length <= 0){
      $('tables').style.display = 'none';
      WarningMsrg("没有定义模板","msrg","blank");
      return;
    }
    for(var i = 0 ; i < rtJson.rtData.length ; i++){
       var obj = rtJson.rtData[i];
       var attrId = obj.attachmentId;
       var attrName = obj.attachmentName;
       var docName = obj.docName;
       var table = $('tables');
       var rows = $('tables').insertRow(table.rows.length);
       var showAttrName = attrName.substring(0,attrName.lastIndexOf('.'));
       rows.className = "TableData";
       var inners = "<td class=\"menulines\" align=\"center\" "
         + "onclick=\"javascript:click_model(\'" + attrId + "\',\'" + attrName + "\')\""
         + "style=\"cursor:pointer\">"
     	 + docName + " </td>";
       $(rows).insert(inners,'content');
    }
  }else{
    alert(rtJson.rtMsrg);
  }
}
</script>
</head>
 
<body  topmargin="5" onload="doInit()">
 
<table id="tables" class="TableBlock" width="100%" onMouseover="borderize_on(event)" onMouseout="borderize_off(event)" onclick="borderize_on1(event)">
<tr class="TableHeader">
  <td align="center"><b>选择模版</b></td>
</tr>
</table>
<div id="msrg"></div>
</body>
</html>