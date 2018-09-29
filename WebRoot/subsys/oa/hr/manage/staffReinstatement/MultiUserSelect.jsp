<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@ page import="yh.core.funcs.person.data.YHPerson,yh.core.funcs.workflow.util.YHWorkFlowUtility"  %>
<%
YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
int deptId = loginUser.getDeptId();
String single = request.getParameter("isSingle");
boolean isSingle = false;
if (single != null && !"".equals(single)) {
  isSingle = Boolean.valueOf(single);
}
String moduleId = request.getParameter("moduleId");
if (moduleId == null) {
  moduleId = "";
}
String privNoFlag = request.getParameter("privNoFlag");
if (privNoFlag == null || "".equals(privNoFlag)) {
  privNoFlag = "0";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人员选择(离职)</title>
<base target="_self">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/orgselect.css">
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/subsys/oa/hr/manage/staffReinstatement/js/MultiUserSelect.js"></script>
<script type="text/javascript">
var isSingle = <%=isSingle%>;//是否是单用户选择
var deptId = <%=deptId%>;
var ctroltime=null,key="";
var moduleId = "<%=moduleId%>";
var privNoFlag = "<%=privNoFlag%>";

function CheckSend()
{
  var kword = $("kword");
  if(kword.value=="按用户名或姓名搜索...")
     kword.value="";
  if(kword.value=="" && $('search_icon').src.indexOf(imgPath + "/quicksearch.gif")==-1)
  {
     $('search_icon').src= imgPath +  "/quicksearch.gif";
  }
  if(key!=kword.value && kword.value!="")
  {
     key=kword.value;
     doSearch(key);
     if($('search_icon').src.indexOf(imgPath + "/quicksearch.gif")>=0)
     {
         $('search_icon').src= imgPath + "/closesearch.gif";
         $('search_icon').title="清除关键字";
         $('search_icon').onclick=function(){kword.value='按用户名或姓名搜索...';$('search_icon').src= imgPath + "/quicksearch.gif";$('search_icon').title="";$('search_icon').onclick=null;};
     }
  }
  ctroltime=setTimeout(CheckSend,100);
}
</script>
</head>
<%
String notViewUser = loginUser.getNotViewUser();
if ("1".equals(notViewUser)) {
  String content = "无查看用户的权限";
  String msg = YHWorkFlowUtility.Message(content,0);
  msg = "<body><div align=center>" + msg + "<div></body></html>";
  out.print(msg);
  return ;
}
%>
<body onload="doInit()" style="padding-right:0px">
<div id="left">
<div style="border:1px solid #000000;background:#FFFFFF;">
  <input type="text" id="kword" name="kword" value="按用户名或姓名搜索..."  onfocus="ctroltime=setTimeout(CheckSend,100);" onblur="clearTimeout(ctroltime);if(this.value=='')this.value='按用户名或姓名搜索...';"  class="SmallInput" style="border:0px; color:#A0A0A0;width:145px;"><img id="search_icon" src="<%=imgPath %>/quicksearch.gif" align=absmiddle style="cursor:pointer;">
</div>
</div>
<div id="right">
 
</div>
<div style="position:absolute;top:330px;left:245px;height:30px;width:60px;">
  <input type=button class="SmallButtonW" value="确定" onclick="window.close()"/>
</div>
</body>
</html>