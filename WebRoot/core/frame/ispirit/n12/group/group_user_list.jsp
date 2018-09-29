<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%

 String gid="";
//System.out.println(" 22222222:" + gid); 
java.util.Enumeration params = request.getParameterNames(); 
while(params.hasMoreElements()) { 
String current_param = (String)params.nextElement(); 
//System.out.print("参数名:" + current_param); 
String[] values=request.getParameterValues(current_param); 
for (int i=0;i<values.length;i++) 
  gid= values[i];
//System.out.println(" 参数值:" + values[i]); 
} 
 


%>


<style type="text/css">

body{
  background-color:#E8EBF2;
} 
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/Menu.js" ></script>
<script type="text/Javascript"> 
var gid=<%=gid%>;

function doInit(){
	getUserList(gid);

  }
  setInterval(reload, 12000);

  function getUserList(gId){
	  var text="";
	  var url = "<%=contextPath%>/yh/core/funcs/system/ispirit/n12/org/act/YHIsPiritOrgAct/getGroupUserList.act";
	  var rtJson = getJsonRs(url,"groupId="+gId);
	  if (rtJson.rtState == "0") {
		  var onLineUser=rtJson.rtData.onLine;
		  var offLineUser=rtJson.rtData.offLine;
		  //在线人员
		  for(var i=0;i<onLineUser.length;i++){
			  text+= "<div style='cursor:hand' onclick=\"window.external.OA_SMS('"+onLineUser[i].uId+"', '"+onLineUser[i].uName+"','SEND_MSG');\">"+onLineUser[i].uIcon+onLineUser[i].uName+"</div>";

		  }
		  //离线人员
		  for(var i=0;i<offLineUser.length;i++){
		       text+= "<div style='cursor:hand' onclick=\"window.external.OA_SMS('"+offLineUser[i].uId+"', '"+offLineUser[i].uName+"','SEND_MSG');\">"+offLineUser[i].uIcon+offLineUser[i].uName+"</div>";

		  }
		  $("userList").innerHTML=text;
		//  alert(text);
	  }else{
		  
	  }
	  
  }

function reload() {
  window.location.reload();
}
</script>
<body style=" background-color:#E8EBF2; " onload="doInit()" >
<div  id="userList"></div>

</body>
</html>