<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
int styleIndex = 1;
String stylePath = contextPath + "/core/styles/style" + styleIndex;
String cssPath = stylePath + "/css";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML xmlns:vml="urn:schemas-microsoft-com:vml">
<HEAD>
<title>流程设计</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<OBJECT id="vmlRender" classid="CLSID:10072CEC-8CC1-11D1-986E-00A0C955B42E" VIEWASTEXT></OBJECT>
<STYLE>
oval {FONT-SIZE: 12px;behavior: url(#default#VML);}
shadow {FONT-SIZE: 12px;behavior: url(#default#VML);}
textbox {FONT-SIZE: 12px;behavior: url(#default#VML);}
roundrect {FONT-SIZE: 12px;behavior: url(#default#VML);}
shapetype {FONT-SIZE: 12px;behavior: url(#default#VML);}
stroke {FONT-SIZE: 12px;behavior: url(#default#VML);}
path {FONT-SIZE: 12px;behavior: url(#default#VML);}
shape {FONT-SIZE: 12px;behavior: url(#default#VML);}
line {FONT-SIZE: 12px;behavior: url(#default#VML);}
</STYLE>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>

<script language="JavaScript" src="flowdesigner.js"></script>
<script language="JavaScript" src="openWin.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script language="JavaScript">

function getMsg(){
	 var graphtype = jQuery('#graphtype').val();
	 var requestURL = "<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowProcessAct";
  	 var url = requestURL + "/getGraphSetting.act";
  	 var json = getJsonRs(url , 'graphtype='+graphtype);
  	 
  	 if(json.rtState == '0'){
  	 	jQuery('#width').val(json.rtData.width);
  	 	jQuery('#height').val(json.rtData.height);
  	}
  	 else{
   		alert(json.rtMsrg);
  	}
}
 function submitGraph(){
	 var width = jQuery('#width').val();
	 if(width==""){
	 	alert("请输入宽度的值");
	 	return ;
	 }
	 var height = jQuery('#height').val();
	 if(height==""){
	 	alert("请输入高度的值");
	 	return ;
	 }
	 var graphtype = jQuery('#graphtype').val();
	 var param = "width="+width+"&height="+height+"&graphtype="+graphtype
	 var requestURL = "<%=contextPath %>/yh/core/funcs/workflow/act/YHFlowProcessAct";
  	 var url = requestURL + "/saveGraphSetting.act";
  	 var json = getJsonRs(url , param);
  	 
  	 if(json.rtState == '0'){
   		alert("保存成功");
  	}
  	 else{
   		alert(json.rtMsrg);
  	}
 }

 function showGraph(){
	 var width = jQuery('#width').val();
	 var height = jQuery('#height').val();
	 var text = jQuery('#testStr').val();
	 var graphtype = jQuery('#graphtype').val();
	 if(graphtype == 'oval'){
	 	createOval(width,height,text);
	 }
	 if(graphtype == 'roundrect'){
		 createRoundrect(width,height,text);
	 }
	 if(graphtype == 'diamond'){
		 createDiamond(width,height,text);
	 }
 }
 function createDiamond(width,height,text){
  document.getElementById('showGraph').innerHTML="";
  var sShapetype = "<shapetype id='type1' coordsize='21600,21600' o:spt='4' path='m10800,l,10800,10800,21600,21600,10800xe'/>";
  var sStroke = "<stroke joinstyle='miter'/>";
  var sPath = "<path gradientshapeok='t' o:connecttype='rect' textboxrect='5400,5400,16200,16200'/>";
    
  var shapetype = document.createElement(sShapetype);
  var stroke = document.createElement(sStroke);
  var path = document.createElement(sPath);
  
  shapetype.appendChild(stroke);
  shapetype.appendChild(path);
  jQuery('#showGraph').append(shapetype);
  
  var sShape = "<shape type='#type1' id='1' table_id='1' flowType='1' passCount='0'  flowTitle='"
    + text + "' flowFlag='0'  readOnly='0' fillcolor='gray' style='LEFT: 400; TOP:200; WIDTH: "+width+"; POSITION: absolute; HEIGHT: "+height+";z-index:1;vertical-align:middle;cursor:pointer;TEXT-ALIGN:center;' />";
  var sShadow = "<shadow on='T' type='single' color='#b3b3b3' offset='3px,3px'/>";
  var sTextbox = "<textbox inset='0pt,0pt,0pt,0pt' onselectstart='return false;'></vml:textbox>";
  var shape = document.createElement(sShape); 
  var shadow = document.createElement(sShadow);
  var textbox = document.createElement(sTextbox);
  
  shape.appendChild(shadow);
  shape.appendChild(textbox);
  textbox.innerHTML = "<b>3</b><br>" + text;
  jQuery('#showGraph').append(shape);
}

 function createRoundrect(width,height,text){
 document.getElementById('showGraph').innerHTML="";
  var sRoundrect = "<roundrect inset='2pt,2pt,2pt,2pt' id='1' table_id='1' flowType='1' passCount='0' flowTitle='"
    + text + "' flowFlag='0'  readOnly='0'  receiverID=''  receiverName=''  fillcolor='green' style='LEFT:400; TOP:200; WIDTH: "+width+"; POSITION: absolute; HEIGHT: "+height+";vertical-align:middle;cursor:pointer;TEXT-ALIGN:center;z-index:1' arcsize='4321f' coordsize='21600,21600'/>";
  var sShadow = "<shadow on='T' type='single' color='#b3b3b3' offset='3px,3px'/>";
  var sTextbox = "<textbox inset='1pt,2pt,1pt,1pt' onselectstart='return false;'></vml:textbox>";
  
  var roundrect = document.createElement(sRoundrect);
  var shadow = document.createElement(sShadow);
  var textbox = document.createElement(sTextbox);
  

  roundrect.appendChild(shadow);
  roundrect.appendChild(textbox);
  textbox.innerHTML = "<b>2</b><br>" + text;
  jQuery('#showGraph').append(roundrect);
}

 function createOval(width,height,text){
 document.getElementById('showGraph').innerHTML="";
  var sOval = "<oval id=3 fillcolor='red'  style='LEFT: 400; TOP: 200;WIDTH: "+width+"; POSITION: absolute; HEIGHT: "+height+";vertical-align:middle;cursor:pointer;TEXT-ALIGN:center;z-index:1' arcsize='4321f' coordsize='21600,21600'/>"; 
  var sShadow = "<shadow on='T' type='single' color='#b3b3b3' offset='3px,3px'/>";
  var sTextbox = "<textbox inset='1pt,2pt,1pt,1pt' onselectstart='return false;'></vml:textbox>";

  var oval = document.createElement(sOval);
  var shadow = document.createElement(sShadow);
  var textbox = document.createElement(sTextbox);
  

  oval.appendChild(shadow);
  oval.appendChild(textbox);
  textbox.innerHTML = "<b>1</b><br>"+text;
  jQuery('#showGraph').append(oval);
}
</script>
</HEAD>
<BODY onload="getMsg();">
<table class="TableBlock" width="50%" align="center">
    <tr>
      <td nowrap class="TableData">图形：</td>
       <td class="TableData" colspan="2">
			<select id="graphtype" onchange="getMsg()">
				<option value="oval">oval</option>
				<option value="roundrect">roundrect</option>
				<option value="diamond">diamond</option>
			</select>
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData">宽度(width)：</td>
       <td class="TableData" colspan="2">
 		<input type="text" name="width" id="width"  class="BigInput" />参考值：120
      </td>
     
    </tr>
    <tr>
    	 <td nowrap class="TableData">高度(height)：</td>
       <td class="TableData" colspan="2">
 			<input type="text" name="height" id="height"  class="BigInput" />参考值：60
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">输入测试文件：</td>
       <td class="TableData" colspan="2">
       <input type="text" name="testStr" id="testStr"  class="BigInput" value="会议纪要审核"/>
      </td>
    </tr>
    
			<tr>
		      <td class="TableData" colspan="3" >
		      	<div align="center">
		        	 <input type="button" value="提交" class="BigButton" onclick="submitGraph();">
		        	 <input type="button" value="测试" class="BigButton" onclick="showGraph();">
		        </div>
		      </td>
   		   </tr>
  </table>
  <div id="showGraph"></div>
</BODY>
</HTML>