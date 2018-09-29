<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  String host = request.getServerName() + ":" + request.getServerPort() + request.getContextPath() ;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预览表单</title>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/adapter/prototype/ext-prototype-adapter.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/ext-all.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/frame/ux/Window.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%><%=moduleContextPath %>/flowform/util/dateUtil.js" ></script>
<script type="text/Javascript" src="<%=contextPath%><%=moduleContextPath %>/flowform/util/praserUtil.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var formId = "<%=seqId%>";
var host = "<%=host%>";
var isPrint = false;
function doInit() {
  var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowFormViewAct/getFormView.act";
  var json = getJsonRs(url,"seqId=" + seqId);
  if (json.rtState = "0") {
    
    window.style = json.rtData.css;
    if (window.style) {
      var elmSty = document.createElement('STYLE');
      elmSty.setAttribute("type", "text/css");
      if (elmSty.styleSheet) { 
        elmSty.styleSheet.cssText=window.style;  
      } else { 
        elmSty.appendChild(document.createTextNode(styCss));  
      } 
      document.getElementsByTagName("head")[0].appendChild(elmSty); 
    }
    try {
      $('fromDiv').insert(json.rtData.form);
    } catch(e) {
       
    }
    var js = json.rtData.js;
    if (js) {
      try {
        window.execScript(js);
      } catch(e) {
        
      }
    }
  }
  top.subwin = window;
}
function killErrors() { 
  return true; 
} 
//window.onerror = killErrors; 
</script>
</head>
<body class="" topmargin="5" onload="doInit()">
<form enctype="multipart/form-data" name="form1" id=form1>
<div id="fromDiv" style="margin-left:3px"></div>
</form>
<%@ include file="/core/funcs/doc/websign/ver.jsp" %>
<script>
var DWebSignSeal=document.getElementById("DWebSignSeal");
var sign_str = "";
//var sign_arr=sign_str.split(",");
var sealForm = 1;
var TO_VAL="";
try {
  DWebSignSeal.SetCurrUser("<%=loginUser.getSeqId()%>[<%=loginUser.getUserName()%>]");
} catch (e) {
}
function addSeal(item,seal_id) {
  try {
    if(DWebSignSeal.FindSeal(item+"_seal",2)!="") {
  	alert("您已经签章，请先删除已有签章！");
  	return;
    }
    SetStore();
    DWebSignSeal.SetPosition(200,200,"SIGN_POS_"+item);
   
    if (sealForm == 1 ) {
      DWebSignSeal.addSeal("", item+"_seal");
    } else {
      if(typeof seal_id=="undefined") {
        show_seal(item,"addSeal");
      } else {
    	  var URL = "http://"+ host +"<%=moduleSrcPath %>/act/YHFlowFormViewAct/getSeal.act?id=" + seal_id; 
    	  DWebSignSeal.AddSeal(URL, item+"_seal");
      }
    }
    DWebSignSeal.SetSealSignData(item+"_seal",str); 
    DWebSignSeal.SetMenuItem(item+"_seal",261);
  } catch(e) {
    
  }
}

function handWrite(item) {
  try {
    if(DWebSignSeal.FindSeal(item+"_hw",2)!="") {
      alert("您已经签章，请先删除已有签章！");
      return;
    }
    SetStore();
    DWebSignSeal.SetPosition(0,0,"SIGN_POS_"+item);
    DWebSignSeal.HandWrite(0,255,item+"_hw");
  
    DWebSignSeal.SetSealSignData(item+"_hw",str);
    DWebSignSeal.SetMenuItem(item+"_hw",261);
  } catch(e) {

  }
}
function GetDataStr(){
  try{
	var str="";
	var separator = "::";  // 分隔符
	if (TO_VAL!="") {
  	  for (i=0; i < document.form1.length; i++) {
  		if (document.all.form1.elements[i].type == "checkbox") {
  		  if(document.all.form1.elements[i].checked) {
  		    MyValue="on";
  		  } else {
  		    MyValue="";
  		  }
  		} else {
          MyValue=document.all.form1.elements[i].value;
  		}
  		MyTitle=document.all.form1.elements[i].title;
  		if(MyTitle!="" && (TO_VAL.indexOf(","+MyTitle+",")>0 || TO_VAL.indexOf(MyTitle+",")==0))
  		   str += document.all.form1.elements[i].name + "separator" + MyValue + "\n"
  	}
  }
  return str;
} catch(e) {

}
}

function SetStore() {
  try{
    str= GetDataStr();
    DWebSignSeal.SetSignData("-");
    DWebSignSeal.SetSignData("+DATA:" + str);
  } catch(e) {

  }
}
</script>
</body>
</html>