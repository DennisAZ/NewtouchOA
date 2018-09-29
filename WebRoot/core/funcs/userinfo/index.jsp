<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String userId=request.getParameter("userId");
  String windows=request.getParameter("windows");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看用户信息</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">

var login_func_str=""; //登录者菜单权限var userId = "<%=userId %>";
var isShow = false;
//获取权限            
  var url = "<%=contextPath%>/yh/core/funcs/userinfo/act/YHUserInfoAct/getFuncStrAct.act";
  var rtJson = getJsonRs(url  , "userId=" + userId);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    login_func_str=data.login_funcs_str;
    isShow = data.isShow;
   }

//  menu_id   0516	日程安排查询   0228	工作日志    
  var calendar=0;
  var diary=0; 
  if(login_func_str.indexOf("0516")!=-1) calendar=1;
  if(login_func_str.indexOf("0228")!=-1) diary=1;

//构建菜单
  var jso1 = [
           {title:"个人信息", contentUrl:"<%=contextPath%>/core/funcs/userinfo/person.jsp?userId=<%=userId%>&windows=<%=windows%>", imgUrl: "<%=imgPath%>/address.gif", useIframe:true}
		   ];
		   
  var jso2 = [
           {title:"个人信息", contentUrl:"<%=contextPath%>/core/funcs/userinfo/person.jsp?userId=<%=userId%>&windows=<%=windows%>", imgUrl: "<%=imgPath%>/address.gif", useIframe:true}
          ,{title:"共享日志", contentUrl:"<%=contextPath%>/core/funcs/userinfo/diary.jsp?userId=<%=userId%>&windows=<%=windows%>", imgUrl: "<%=imgPath%>/share_diary.gif", useIframe:true}
		   ];	
  if (isShow) {
    jso2.push({title:"日程安排", contentUrl:"<%=contextPath%>/core/funcs/userinfo/calendar.jsp?userId=<%=userId%>&windows=<%=windows%>", imgUrl: "", useIframe:true});
  }	   
		   
  var jso3 = [
           {title:"个人信息", contentUrl:"<%=contextPath%>/core/funcs/userinfo/person.jsp?userId=<%=userId%>&windows=<%=windows%>", imgUrl: "<%=imgPath%>/address.gif", useIframe:true}
		   ];		   
  if (isShow) {
    jso3.push({title:"日程安排", contentUrl:"<%=contextPath%>/core/funcs/userinfo/calendar.jsp?userId=<%=userId%>&windows=<%=windows%>", imgUrl: "", useIframe:true});
  }	   
  var jso4 = [
           {title:"个人信息", contentUrl:"<%=contextPath%>/core/funcs/userinfo/person.jsp?userId=<%=userId%>&windows=<%=windows%>", imgUrl: "<%=imgPath%>/address.gif", useIframe:true}
          ,{title:"共享日志", contentUrl:"<%=contextPath%>/core/funcs/userinfo/diary.jsp?userId=<%=userId%>&windows=<%=windows%>", imgUrl: "<%=imgPath%>/share_diary.gif", useIframe:true}        
		   ];	
		   
var jso=null;
	if(calendar==0 && diary==0){
	     jso=jso1;
	}else if(calendar==0 && diary ==1){
	     jso=jso4;
	}
	else if(calendar==1 && diary==0){
	     jso=jso3;
	}
	else if(calendar==1 && diary==1){
	      jso=jso2;
	}
		   
		   	   
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="buildTab(jso, 'smsdiv', 800);">
</body>
</html>
