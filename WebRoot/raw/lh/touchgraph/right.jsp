<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String subject = request.getParameter("subject");
if (subject == null ) {
  subject = "";
}
String id = request.getParameter("id");
if (id == null) {
  id = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文章列表</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var id = "<%=id%>";
var subject = "<%=subject%>";

var sss = [{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}];
var nodes = [[{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}
,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}]
               ,[{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}]
                      ,[{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}]
                           ,[{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}]
                                ,[{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '},{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '} ,{title:"搜搜问问",content:'提供论坛、网页、图片、音乐等类型搜索服务。', info:'<a href="#">www.soso.com</a> 2010-8-10 '}]];
function doInit() {
  <% if (!"".equals(subject)) { %>
  //sss = nodes.<%=subject%>;
  var i =  Math.random();
  var index = Math.ceil( i * 5 ) - 1;
  sss = nodes[index];
  <% } %>
  for (var i = 0 ;i < sss.length;i ++) {
    var tmp = sss[i];
    addRow(tmp , i);
  }
  var name =  parent.main.centerNode.nodeName ;
  $('tooltip').update("&nbsp;和" + name + "相关的文件");
}
function addRow(node , i) {
  var div = new Element("div");
  div.id = "doc-" + i;
  div.style.paddingTop = "10px";
  var div2 = new Element("div");
  div2.id = "title-" + i;
  div2.style.fontSize = "12pt";
  div2.update("<a href='#'>" + node.title + "</a>");
  div.appendChild(div2);
  var div3 = new Element("div");
  div3.id = "content-" + i;
  div3.style.paddingTop = "3px";
  div3.update(node.content);
  div.appendChild(div3);
  var div4 =new Element("div");
  div4.style.paddingTop = "2px";
  div4.update(node.info);
  div.appendChild(div4);
  $('list').appendChild(div);
}
</script>
</head>
<body onload="doInit()" style="">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img  WIDTH="16" HEIGHT="16" align="absmiddle" src="<%=imgPath %>/notify_new.gif"/><span class="big3"  id="tooltip"></span>
    </td>
  </tr>
</table>
<div id="list" style="border-top:1px solid #b2d235">
</div>
</body>
</html>