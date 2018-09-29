<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="yh.core.funcs.news.data.YHNews"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新闻查询</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>

<script type="text/javascript">
function checkAll(field) {
	  var deleteFlags = document.getElementsByName("deleteFlag");
	  for(var i = 0; i < deleteFlags.length; i++) {
	    deleteFlags[i].checked = field.checked;
	  }
	}
function delete_mail(pageIndex,showLength)
{  
  var deleteAllFlags = document.getElementsByName("deleteFlag");
  var delete_str="";
  for(i=0;i<deleteAllFlags.length;i++)
  {

	  if(deleteAllFlags[i].checked) {
		  delete_str += deleteAllFlags[i].value + "," ;   //SMS_BODY  seqID
	 }	
  }
  if(delete_str=="")
  {
     alert("要删除公告通知，请至少选择其中一条。");
     return;
  }

  msg='确认要删除所选公告通知吗？';
  if(window.confirm(msg))
  {   
	  var par = 'pageIndex='+pageIndex+'&showLength=' +showLength+'&delete_str=' +delete_str;

      var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/deleteCheckNotify.act';
	  var json = getJsonRs(url,par);
	  if (json.rtState == "0"){
		  window.location.reload();
		} else{
	      alert(json.rtMsrg);
	    }
  }
}
function delete_all()
{
 msg='确认要删除所有公告通知吗？\n删除后将不可恢复，确认删除请输入大写字母“OK”';
 if(window.prompt(msg,"") == "OK")
 { 
	var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/deleteAllNotify.act';
	var json = getJsonRs(url);
	if (json.rtState == "0"){
		window.location.reload();
	} else{
      alert(json.rtMsrg);
    }
 }
}
</script>
</head>
<body>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">
       <img src="<%=imgPath%>/notify_open.gif" WIDTH="22" HEIGHT="20" align="absmiddle">
       <span class="big3"> 新闻查询结果</span>
       &nbsp;
    </td>
  </tr>
</table>
<table class="TableList" width="100%">
  <tr class="TableHeader">
      <td nowrap align="center">标题</td>
      <td nowrap align="center">类型</td>
      <td nowrap align="center">发布人</td>
      <td nowrap align="center">发布时间 </td>
      <td nowrap align="center">点击次数</td>
      <td nowrap align="center">评论(条)</td>
      <td nowrap align="center">操作</td>
    </tr>
    	<%
    	Collection newsManagerList = (Collection)request.getAttribute("newsManagerList");
			for(Iterator it = newsManagerList.iterator();it.hasNext();)
			{
				YHNews notify = (YHNews)it.next();		
		%>
		<tr>
           <td align="center"><a href=javascript:open_news('');><%=notify.getSubject()%></a></td>
          <td align="center"><%=notify.getTypeId()%></td>
      <td nowrap align="center"><u style="cursor:pointer"><%=notify.getProvider()%></u></td>
      <td nowrap align="center"><%=notify.getNewsTime()%></td>
      <td nowrap align="center"><%=notify.getClickCount()%></td>
      <td nowrap align="center"><%=notify.getClickCount()%></td>
      <td nowrap align="center">
      <a href="modify.php?NEWS_ID=<?=$NEWS_ID?>&start=<?=$start?>"> 修改</a>
      <a href="javascript:delete_news('<?=$NEWS_ID?>','<?=$start?>');"> 删除</a>
       <a href="javascript:re_news('<?=$NEWS_ID?>');"> 管理评论</a>
    </tr>
		  <%
		  
	   }
	 %>
   <tbody id="dataBody"></tbody>
</table>


<div id="userpriv" style="width: 600px; height: 300px;">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.person.data.YHUserPriv"/>
</div>
</body>
</html>