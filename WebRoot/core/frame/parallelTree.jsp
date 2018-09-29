<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
String onlineRefStr = YHSysProps.getString("$ONLINE_REF_SEC");
if (onlineRefStr == null || "".equals(onlineRefStr.trim())) {
  onlineRefStr = "3600";
}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线人员(平行列表)</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/Javascript">	
var menuFlag = 0;
var deptStr = "";
function doInit(){
  var onlineRef = '<%=onlineRefStr%>';
  if (isNaN(onlineRef)) {
    onlineRef = 120;
  }
  setInterval(reload, onlineRef * 1000);
  
  $("menuFlag").value = 0; 
  var url = "<%=contextPath%>/yh/core/module/org_select/act/YHOrgSelectAct/getParallelTree.act";
  var rtJson = getJsonRs(url);
  var flag = 0;
  
  var deptFlag = "";
  if(rtJson.rtState == "0"){
    $("tableAll").update("<tbody id='tbody'><tr class='TableHeader' onclick='listExpand();' style=cursor:pointer>"
      + "<td width='100' align='center' colspan=3><span id='expendText'>全部收缩</span></tr>"
      + "<tbody>");
    $('userAll').appendChild($("tableAll"));
 
  	for(var i = 0; i < rtJson.rtData.length; i++){
  	  flag++;
      var name = rtJson.rtData[i].name;
      var imgPath = rtJson.rtData[i].imgPath;
      var deptName = rtJson.rtData[i].deptName;
      var userId = rtJson.rtData[i].userId;
      var deptId = rtJson.rtData[i].deptId;
      var title = rtJson.rtData[i].title;
      if(deptFlag != deptId){
        if(deptStr != ""){
          deptStr += ",";
        }
        deptStr += deptId;
        $("tableAll").insert("<tbody><tr class='TableHeader' onclick='deptExpand(" + deptId + ");' style=cursor:pointer>"
            + "<td nowrap width='100' align='center' colspan=3 >" + deptName + "</td>"
            + "</tr><tbody>");
      }
      $("tableAll").insert("<tbody id='tbody_" + deptId + "'><tbody>");
  	  var trColor = (flag % 2 == 0) ? "TableLine1" : "TableLine2";
  	  var tr = new Element('tr',{'class':trColor});	
  	  $("tbody_"+deptId).appendChild(tr);	
      tr.insert("<td align='center' title='" + deptName + "'>"					
	    + imgPath + "</td><td align='align' title='"+title+"'>"		
	    + "<a href="	
	    + "javascript:selfInfo('"+userId+"');"
	    + ">"+name+"</td><td align='align' title='" + deptName + "'>"
	    + "<a href="	
	    + "javascript:smsFunc('"+userId+"');"
	    + ">消息</a>&nbsp;"
	    + "<a href="	
	    + "javascript:emailFunc('"+userId+"');"
	    + ">邮件</a>&nbsp;"
	    + "</td>"	
     			
      );
      deptFlag = deptId;
  	}
  }else{
  	alert(rtJson.rtMsrg); 
  }
}

function selfInfo(id){
	
	   //dispParts("/yh/core/funcs/userinfo/index.jsp?userId="+id+"&windows=1",0);
	  
	    if (top.openUrl) {
	     top.openUrl({
	       url: contextPath + "/core/funcs/userinfo/index.jsp?userId="+id+"&windows=1",
	       text: '用户信息'
	     });
	    }
	    else {
	      top.dispParts(contextPath + "/core/funcs/userinfo/index.jsp?userId="+id+"&windows=1");
	    }
	
}

function emailFunc(id) {
  var url = "<%=contextPath%>/core/funcs/email/new/emailBack.jsp?toId="+id;
  openDialog(url,'800', '650');
}

function smsFunc(id) {
  var fromId = id;
  var url = "<%=contextPath%>/core/funcs/sms/smsback.jsp?fromId="+fromId;
  openDialog(url,'700', '300');
}

function deptExpand(deptId){
  if ($("tbody_"+deptId).style.display == 'none'){
    $("tbody_"+deptId).style.display='';
  }else{
    $("tbody_"+deptId).style.display = 'none';
  }
}

function listExpand(){
  var deptFunc = deptStr.split(',');;
  for(var i = 0; i < deptFunc.length; i++){
    if($("menuFlag").value == 1){
      $("tbody_" + deptFunc[i]).style.display = '';
    }else{
      $("tbody_" + deptFunc[i]).style.display = 'none';
    }
  }
  if($("menuFlag").value == 1){
    $("listDiv").style.display = '';
    $("expendText").innerHTML = "全部收缩";
  }else{
    $("listDiv").style.display = 'none';
    $("expendText").innerHTML = "全部展开";
  }
  $("menuFlag").value = (1 - $("menuFlag").value);
}

function reload() {
  window.location.reload();
}
</script>
</head>
<body onload="doInit()" style="background:transparent;width: 168px; position: relative; top: -10px;">
<input type="hidden" name="menuFlag" id="menuFlag" value="">
<div id="userAll" align="center">
<table id="tableAll" width="100%", class="TableBlock", align="center">

</table>
</div>
<div id="deptDiv" align="center"></div>
<div id="listDiv" align="center"></div>
</body>
</html>