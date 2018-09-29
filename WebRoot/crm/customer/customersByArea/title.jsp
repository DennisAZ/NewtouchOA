<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/grid.js"></script>
<script type="text/Javascript"> 
function doInit(){
  var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getPostPriv.act";
  var rtJsons = getJsonRs(urls);
  if(rtJsons.rtState == '0'){
    if(rtJsons.rtData == "1"){
      document.getElementById("newUser").innerHTML = "全体";
    }else if(rtJsons.rtData == "2"){
      document.getElementById("newUser").innerHTML = "指定部门";
    }else{
      var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getPostPrivOther.act";
      var rtJson = getJsonRs(url);
      if(rtJson.rtState == '0'){
        document.getElementById("newUser").innerHTML = rtJson.rtData;
      }else{
        alert(rtJson.rtMsrg); 
      }
    }
  }else{
    alert(rtJsons.rtMsrg); 
  }
  
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;根据区域查询客户</span>
    </td>
    </tr>
</table>
</body>
</html>