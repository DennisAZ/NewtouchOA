<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
  
<title>Insert title here</title>
<script type="text/javascript">
var jso = [
   {title:"数据库管理", contentUrl:"<%=contextPath%>/yh/core/funcs/mysqldb/act/YHMySqldbAct/listTableInfo.act",  useIframe:true}
    ,{title:"数据库脚本导入", contentUrl:"<%=contextPath%>/core/funcs/mysqldb/import.jsp", useIframe:true}
    ,{title:"数据库备份", contentUrl:"<%=contextPath%>/core/funcs/mysqldb/backup.jsp",  useIframe:true}
    ,{title:"在线人员", contentUrl:"<%=contextPath%>/core/funcs/mysqldb/onlineuser.jsp", useIframe:true}
 ];
function doInit(){
  if(checkIsMysql()){
    buildTab(jso, 'dbmanager');
  }else{
    WarningMsrg("此功能只支持MYSQL数据库!", "dbmanager","info" );
  }
}
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"360\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}
function checkIsMysql(){
  var url = contextPath + "/yh/core/funcs/mysqldb/act/YHMySqldbAct/checkDb.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == '0'){
    return rtJson.rtData.isMysql;
  }else{
    return false;
  }
}
</script>
</head>
<body onload="doInit()">
 <div id = "dbmanager"></div>
</body>
</html>