<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>最近联系人</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=contextPath%>/core/frame/ispirit/n12/org/style/smsbox.css">
<style type="text/css">

.TableData sub-module-item{
  cursor:pointer;
 
}



body {
background-color:#DAEAF2;
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
</style>

<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/frame/ispirit/n12/js/ipanel_org.js" ></script>
<script type="text/Javascript">

function doInit(){
  
  getGroupData() ;
}

function getGroupData() {
    var url = "<%=contextPath%>/yh/core/funcs/system/ispirit/n12/org/act/YHIsPiritOrgAct/getModuleData.act";
    var rtJson = getJsonRs(url,"MODULE=im_group");
    if (rtJson.rtState == "0") {
     // alert(rtJson.rtData.data);
     $("data").innerHTML=rtJson.rtData.data;
      
    }else {
      alert(rtJson.rtMsrg); 
    }
  }

function onClickSentGroupMsg(group_id,group_name){
   send_im_group_msg(group_id, group_name);
}

</script>


</head>
<body onLoad="doInit();">
<div id="data"></div>
</body>
</html>