<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>最近新闻公告列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
    	body {
			background-color:#FFF;
			text-align:left;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    </head>
  
  <body>
    <logic:notEmpty name='allNews'>
        <logic:iterate id='allNews' name='allNews'>
            <div class="modCon">
                <img src="images/content/blueArr.gif"/>
                <logic:notEmpty name="allNews" property="newType">
                    <span class="brown">[${allNews.newType}]</span>
                </logic:notEmpty><a href="messageAction.do?op=showNewsInfo&newCode=${allNews.newCode}&isEdit=0" target="_blank">${allNews.newTitle}</a>
                <logic:equal value="0" name="allNews" property="newIstop">
                    <img src="images/content/upButton.gif" alt="置顶"/>
                </logic:equal>
                <img id="newIcon<%=count%>" src="images/gif/new.gif" style="display:none"/>
                <span class="gray small"><label id="ndate<%=count%>"></label></span>
           </div>
           <script type="text/javascript">
                dateFormat('ndate','${allNews.newDate}',<%=count%>);
                
                //今日发布的显示new图标
                if('${allNews.newDate}'.substring(0,10)=='${today}'){
                    $("newIcon"+<%=count%>).style.display="";
                }
            </script>
            <% count++; %>
        </logic:iterate>
    </logic:notEmpty>
    <logic:empty name='allNews'>
        <div class="gray">&nbsp;&nbsp;最近没有新闻公告</div>
    </logic:empty>
  </body>
</html>
