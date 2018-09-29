 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="/yh/core/js/orgselect.js" ></script>
<script type="text/javascript">
var is_moz = (navigator.product == 'Gecko') && userAgent.substr(userAgent.indexOf('firefox') + 8, 3);
function getOpenner(){
   if(is_moz){
     return parent.opener;
   }else{
     return parent.dialogArguments;
   }
}
var parent_window = getOpenner();
var vuDeptField = null;
var vuDeptNameField = null
function doOnload(){
  parent_window = getOpenner();
  vuDeptField = parent_window.$(parent_window.vuDeptField);//初始化对象
  vuDeptNameField = parent_window.$(parent_window.vuDeptNameField);//初始化对象
  getDeptId();
}
function getDeptId(){
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/info/act/YHInfoAct/selectDeptToAttendance.act?deptId="
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  if(prcs.length>0){
    var table = "<table class='TableList' width='90%'>";

    for(var i=0;i<prcs.length;i++){
      var prc = prcs[i];
      var deptId =  prc.value;
      var deptIdDesc = prc.text;
      var tr = new Element('tr',{});
      var re1 = /\'/gi;
      deptIdDesc =deptIdDesc.replace(re1,"&lsquo;");
      table = table + "<tr><td nowrap align='left' onclick='toDept(\""+deptId+"\",\""+deptIdDesc+"\");'>"+deptIdDesc+"</td></tr>";
    // tr.update("<td nowrap align='left' >"+deptIdDesc+"</td>");
    } 
    table = table + "</table>";
    $("bodyDiv").update(table);
  }
}
function toDept(seqId,deptIdDesc){
//上级页面更新数据
  vuDeptField.value = seqId;
  vuDeptNameField.value = deptIdDesc;
  close();
}
</script>
<body topmargin="1" leftmargin="0" onload="doOnload();">
 <div id="bodyDiv" align="center"></div>
</body>
</html>
