<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %> 
<%
String runId = request.getParameter("runId");
String flowId = request.getParameter("flowId");
String prcsId = request.getParameter("prcsId");
String flowPrcs = request.getParameter("flowPrcs");
String allowBack = request.getParameter("allowBack");
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String skin = request.getParameter("skin");
if (skin == null) {
  skin = "";
}
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>回退</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script  type="text/Javascript">
var parentWindowObj = window.dialogArguments;
var runId = '<%=runId%>';
var flowId = '<%=flowId%>';
var prcsId = '<%=prcsId%>';
var flowPrcs = '<%=flowPrcs%>';
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
var requestUrl = contextPath + "/yh/core/funcs/workflow/act/YHWorkHandleAct";
var allowBack = "<%=allowBack %>";
function doInit() {
//取步骤列表
 if (allowBack != '1') {
   var param = 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId ;
   var url = requestUrl + "/getPreRunPrcs.act";
   var json = getJsonRs(url , param);
   if (json.rtState != '0') {
     alert(json.rtMsrg);
     return ;
   }
   var prcsList = json.rtData;
   var tmp = new Array();
   var i = 0 ;
   for (; i <  prcsList.length;i ++) {
     var prcs = prcsList[i];
     var la = "<label>"
     var radio = "<input type=\"radio\" name=\"selectedPrcs\" value=\""+ prcs.id +"\" id=\"selectedPrcs_"+ i +"\" />";
     la +=  radio  + " 步骤名：" + prcs.prcsName;
     la += "</label><br/>";
     tmp[i] = la;
   }
   $('prcsList').update(tmp.join(""));
 } else {
   $('prcsListTitle').hide();
   $('prcsList').hide();
 }}
/**
 * 回退到

 */
function backTo(id){
  var content = document.getElementById("CONTENT").innerHTML;
  if (allowBack != '1') {
    if (!id) {
      var radios = $('prcsList').getElementsByTagName('input');
      for (var i = 0 ;i < radios.length ;i++) {
        var radio = radios[i];
        if(radio.checked){
          id = radio.value;
        }
      }
    }
    if (!id) {
      alert("请选择流程步骤");
      return ;
    }
    parentWindowObj.backTo(id , true , content);
  } else {
    parentWindowObj.backTo(prcsId - 1 , true , content);
  }
  var par = contextPath + "/core/funcs/workflow/flowrun/list/index.jsp?skin="+ skin +"&sortId=" + sortId;
  parentWindowObj.mouse_is_out = false;
  parentWindowObj.parent.location = par;
  myClose();
}
function myClose() {
  close();
}
</script>
</head>
<body onload="doInit()">
<div style="height:5px"></div>
 <div align=center><table class="TableList" width="300px">
    <tr class=TableHeader  id='prcsListTitle'><td align=left><img src='<%=imgPath %>/green_arrow.gif'/>请选择回退步骤：</td></tr>
    <tr><td align=left id='prcsList'></td></tr>
    <tr>
      <td nowrap class="TableHeader">
      <img src="<%=imgPath %>/green_arrow.gif" align="absMiddle" WIDTH="20" HEIGHT="18"> 请输入会签意见：
      </td>
    </tr>
    <tr>
      <td>
        <textarea id="CONTENT" name="CONTENT" cols="40" rows="3"></textarea>
      </td>
    </tr>
    <tr><td align=center><input value='确认' type='button'  class="SmallButtonW" onclick='backTo()'>&nbsp;&nbsp;<input value='关闭' type='button' class="SmallButtonW"  onclick="myClose()"></td></tr>
    </table></div>
</body>
</html>