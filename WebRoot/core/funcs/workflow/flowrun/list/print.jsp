<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.workflow.act.YHWorkHandleAct" %>
<%

String flowView = request.getParameter("flowView");

Cookie newCookie =new Cookie("flowViewCookie", flowView);
response.addCookie(newCookie);

String runId = request.getParameter("runId");
String flowId = request.getParameter("flowId");
YHWorkHandleAct act = new YHWorkHandleAct();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>表单打印</title>
<link rel="stylesheet" href ="<%=cssPath %>/workflow.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/workflow/flowform/util/dateUtil.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/workflow/flowform/util/praserUtil.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="js/print.js"></script>
<script type="text/javascript">
var runId = '<%=runId%>';
var flowId = '<%=flowId%>';
var flowView = '<%=flowView%>';
</script>
</head>
<body onload="doInit()">
<div align="center" id="del_flag" style="display:none;position:absolute;width:128;height:128;background:url(<%=imgPath %>/run_deleted.png) no-repeat center center;filter:alpha(opacity=30)">
</div>
<div id="form" style="display:none;margin-top:5px;margic-bottom:5px;padding-bottom:5px">

</div>

<div id="attachment" style="display:none;padding-bottom:5px">
 <table width='100%'  class="TableList">
<tr class=TableHeader><td colspan=3>公共附件</td></tr>
<tbody id="attachmentsList">

</tbody>
</table>
</div>

<div id="feedBack" style="display:none;padding-bottom:5px">
<table  class="TableList" width='100%'>
<tr class=TableHeader><td colspan=3>会签与点评</td></tr>
<tbody id="feedbackList">

</tbody>
</table>
</div>
<div id="prcss" style="display:none;padding-bottom:5px">
<table width='100%' class="TableList">
<tr class=TableHeader><td colspan=3>流程图</td></tr>
<tbody id="listTbody">

</tbody>
</table>
</div>

<%@ include file="/core/funcs/workflow/websign/ver.jsp" %>

<script>
var DWebSignSeal=document.getElementById("DWebSignSeal");
var sign_str = "";
var sign_check={};
function GetDataStr(item) {
  if(typeof item == 'undefined') {
    return; 
  }
  var str="";
  var separator = "::";  // 分隔符
  var TO_VAL = sign_check[item];
 
  if(TO_VAL!="") {
    var item_array = TO_VAL.split(",");
    for (i=0; i < item_array.length; i++) {
      var MyValue="";
      var obj =$(item_array[i]);
      
      if (obj) {
        if(obj.type=="checkbox"){
          if(obj.checked==true) {
            MyValue="on";
          } else {
            MyValue="";
          }
        } else {
          MyValue=obj.value;
        }
        if(MyValue.indexOf("&quot;")>=0) MyValue.replace("/&quot;/g","'");   //处理双引号
    		if(MyValue.indexOf("&#039;")>=0) MyValue.replace("/&#039;/g","'");   //处理单引号
    		str += obj.name + "separator" + MyValue + "\n";
      }
    }
  }
  return str;
}
function LoadSignData() {
  try{
    sign_arr=sign_str.split(",");
    for(var i=0;i<sign_arr.length;i++) {
  	if(sign_arr[i]) {
        DWebSignSeal.SetStoreData($(sign_arr[i]).value);
  	}
    }
    DWebSignSeal.ShowWebSeals();
  
    var str= "";
    var strObjectName ;
    strObjectName = DWebSignSeal.FindSeal("",0);
    while (strObjectName) {
  	if(strObjectName.indexOf("_hw")>0) {
  	  item_arr = strObjectName.split("_hw");
  	} else if(strObjectName.indexOf("_seal")>0) {
  	  item_arr = strObjectName.split("_seal");
  	}
  	if(item_arr) {
  	  str = GetDataStr(item_arr[0]);
  	  DWebSignSeal.SetSealSignData(strObjectName,str);
  	  DWebSignSeal.SetMenuItem(strObjectName,4);
      }
  	   strObjectName = DWebSignSeal.FindSeal(strObjectName,0);
    }
  }catch(e) {

  }
}
</script>

</body>
</html>