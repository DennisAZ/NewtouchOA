<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<html>
<head>
<%
String flag = request.getParameter("flag");
%>
<title>选择文件字</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<style>
.menulines{}
</style>

<script>

var menu_enter="";
var flagClose = <%= flag%>;

function borderize_on(dwId){
  var source3 = $('dw_'+dwId);
  if(source3.style.color == "white"){ 
	  return;
  }
  color="#708DDF";
  source3=event.srcElement
  if(source3.className=="menulines" && source3!=menu_enter)
    source3.style.backgroundColor=color;
}

function borderize_on1(dwId){
  var source3 = $('dw_'+dwId);
  if(source3.style.color != "white"){ 
    source3.style.borderColor="black";
    source3.style.backgroundColor="#003FBF";
    source3.style.color="white";
    source3.style.fontWeight="bold";
  }
  else{
	  source3.style.borderColor="";
    source3.style.backgroundColor="";
    source3.style.color="";
    source3.style.fontWeight="";
  }
  menu_enter = source3;
}

function borderize_off(dwId){
  var source3 = $('dw_'+dwId);
  if(source3.style.color == "white"){ 
    return;
  }
  source4=event.srcElement
  if(source4.className=="menulines" && source4!=menu_enter){
    source4.style.backgroundColor="";
    source4.style.borderColor="";
  }
}

//-->
</SCRIPT>
<script Language="JavaScript">
var parent_window = parent.dialogArguments;

function addPlanName(dwId, dwName){
	borderize_on1(dwId);
  var parentDocumentsFont =  parent_window.form1.documentsFont.value;
  var flag1 = (","+parentDocumentsFont+",").indexOf(","+dwId+",");
  if( flag1 > 0 || parentDocumentsFont.startsWith(dwId)){
	  parent_window.form1.documentsFont.value = parentDocumentsFont.substring(0,flag1) + parentDocumentsFont.substring(flag1+dwId.length,parentDocumentsFont.length);
  }
  else{
	  if(parentDocumentsFont == ""){
		  parent_window.form1.documentsFont.value = parentDocumentsFont + dwId;
		}
	  else{
	    parent_window.form1.documentsFont.value = parentDocumentsFont + "," + dwId;
	  }
  }

  var parentDocumentsFontDesc = parent_window.form1.documentsFontDesc.value;
  var flag2 = (","+parentDocumentsFontDesc+",").indexOf(","+dwName+",");
  if(flag2 > 0 || parentDocumentsFontDesc.startsWith(dwName)){
	  parent_window.form1.documentsFontDesc.value = parentDocumentsFontDesc.substring(0,flag2) + parentDocumentsFontDesc.substring(flag2+dwName.length,parentDocumentsFontDesc.length);
  }
  else{
	  if(parentDocumentsFontDesc == ""){
		  parent_window.form1.documentsFontDesc.value = parentDocumentsFontDesc + dwName;
	  }
	  else{
	    parent_window.form1.documentsFontDesc.value = parentDocumentsFontDesc + "," + dwName;
	  }
  }
  if(flagClose == "1"){
	  window.close();
  }
}

/**
 * 获取代码名称
 * @param seqId
 * @return
 */
function selectCodeById(seqId) {
	var str = "";
	var requestURLStr = contextPath + "/yh/core/funcs/workflow/act/YHDocumentsTypeAct/getDocumentsTypeById.act?seqId=" + seqId;
	var rtJson = getJsonRs(requestURLStr);
	//alert(rsText);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	if(prcs.documentsName){
		str = prcs.documentsName;
	}
	return str;
}


function doinit(){
  var parent_window = parent.dialogArguments;
  getTrainingAjaxFunc();
}

function getTrainingAjaxFunc(condition){
  var requestURI = contextPath + "/yh/core/funcs/workflow/act/YHDocumentsTypeAct";
  var url = requestURI + "/getDocumentsTypeListSelect.act?condition="+encodeURIComponent(condition);
  var rtjson = getJsonRs(url); 
  //alert(rsText);
  if(rtjson.rtState == '0'){
    var data = rtjson.rtData;
    var dataLength = data.length; 
    if(dataLength > 0){
      var trainingStr = "";
      $("msrg").update("");
      $("TableControl").show();
      $("tableHead").show();
      for(var i = 0; i < dataLength; i++){
        var dwId = data[i].dwId;
        var dwName = data[i].dwName;
        var tInstitutionName = "";
        trainingStr += "<tr class=\"TableControl\">";
        trainingStr += "<td class=\"menulines\" id=\"dw_"+dwId+"\" name=\"dw_"+dwId+"\" align=\"center\" style=\"cursor:pointer\" onMouseover=\"borderize_on("+dwId+")\" onMouseout=\"borderize_off("+dwId+")\" onclick=javascript:addPlanName('" + dwId.escapeHTML().replace(/\\/g,"\\\\").replace(/'/g,"\\\'").replace(/\"/g,"\\\"") + "','" + dwName.escapeHTML().replace(/\\/g,"\\\\").replace(/'/g,"\\\'").replace(/\"/g,"\\\"") + "') >";
        trainingStr += dwName.escapeHTML() + "</td>";
        trainingStr += "</tr>";   
      }
      $("tableList").update(trainingStr);
    }else{  
      var trainingInfo="";
      trainingInfo +='<table align="center" width="300" class="MessageBox">';
      trainingInfo += '<tbody>';
      trainingInfo +=    '<tr>';
      trainingInfo +=    '<td class="msg info">';
      trainingInfo +=       '<h4 class="title" align="left">提示</h4>';
      trainingInfo +=       '<div style="font-size: 12pt;" class="content">无文件字信息</div>';
      trainingInfo +=    '</td>';
      trainingInfo += '</tr>';
      trainingInfo += '</tbody>';
      trainingInfo += '</table>'  ;      
      $("msrg").update(trainingInfo);
      $("TableControl").hide();
      $("tableHead").hide();
    }
  }
}
</script>
</head >
<body onload="doinit();" class="bodycolor"  topmargin="5">
<table class="TableList"  id="tableHead" width="95%" align="center">
<thead class="TableControl" id="TableControl">
  <th bgcolor="#d6e7ef" align="center"><b>选择文件字名称（最多显示50条）</b></th>
</thead>
<tbody id="tableList"> 
</tbody>
</table>
<div id="msrg"></div>
</body>
</html>
