<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="yh.core.funcs.notify.data.YHNotify"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公告管理</title>
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
       <span class="big3"> 管理公告通知</span>
       &nbsp;
       <select name="type" class="BigSelect" onChange="javascript:;">
          <option value="0">所有类型</option>
          
          <option value="">无类型</option>
       </select>
    </td>
    <td>
     <div id="pagebar">
<div class="pgPanel">
<div>每页<select id="pageLen"  onchange="javascript:;">
<option value="5"  selected>5</option>
<option value="10">10</option>
    <option value="15">15</option>
    <option value="20">20</option>
</select>条</div>
 <div class="separator"></div>
 <div id="pgFirst" title="" class="pgBtn pgFirst pgFirstDisabled">
 </div>
 <div id="pgPrev" title="" class="pgBtn pgPrev pgPrevDisabled">
 </div><div class="separator">
 </div><div>第 
 <input onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" id="pageIndex" type="text" title="" value="1" size="5" class="SmallInput pgCurrentPage"> 页 / 共

  <span id="pageCount" class="pgTotalPage"></span> 页</div>
  <div class="separator"></div>
  <div title="下页" id="pgNext" class="pgBtn pgNext pgNextDisabled">
  </div>
  <div title="" id="pgLast" class="pgBtn pgLast pgLastDisabled">
  </div><div class="separator">
  </div><div id="freshLoad" title="刷新" class="pgBtn pgRefresh" onclick="javascript:;">
  </div><div class="separator"></div>
  <div id="pgSearchInfo" class="pgSearchInfo"></div>
  
  </div></div>
    </td>
  </tr>
</table>
<table class="TableList" width="100%">
  <tr class="TableHeader">
      <td nowrap align="center">选择</td>
      <td nowrap align="center">发布人</td>
      <td nowrap align="center">类型</td>
      <td nowrap align="center">发布范围</td>
      <td nowrap align="center" onClick="javascript:;" style="cursor:pointer;"><u>标题</u></td>
      <td nowrap align="center" onClick="javascript:;" style="cursor:pointer;"><u>创建时间</u></td>
      <td nowrap align="center" onClick="javascript:;" style="cursor:pointer;"><u>生效日期</u></td>
      <td nowrap align="center" onClick="javascript:;" style="cursor:pointer;"><u>终止日期</u></td>
      <td nowrap align="center">状态</td>
      <td nowrap align="center">操作</td>
    </tr>
    	<%
    	Collection notifyList = (Collection)request.getAttribute("notifyManagerList");
			for(Iterator it = notifyList.iterator();it.hasNext();)
			{
				YHNotify notify = (YHNotify)it.next();		
		%>
		<tr>
           <td>&nbsp;<input type="checkbox" name="email_select" value="<%=notify.getSeqId()%>" onClick="check_one(self);">
           <td nowrap align="center"><u title="部门：<%=notify.getFromId()%>" style="cursor:pointer"><%=notify.getFromId()%></u></td>
      <td nowrap align="center"><%=notify.getTypeId()%></td>
      <td style="cursor:pointer" title="" onClick="javascript:;"></td>
      <td><a href=javascript:; title=""><%=notify.getSubject()%></a> 
      </td>
      <td align="center"><%=notify.getSendTime()%></a></td>
      <td nowrap align="center"><%=notify.getBeginDate()%></td>
      <td nowrap align="center"><%=notify.getEndDate()%></td>
      <td nowrap align="center"></td>
      <td>
      <a href="javascript:;" title="查阅情况"> 查阅情况</a>&nbsp;
      <a href=""> 修改</a>
      <a href="manage.php?NOTIFY_ID=<?=$NOTIFY_ID?>&OPERATION=1&start=<?=$start?>"> 立即生效</a>
      
      </td>
    </tr>
		  <%
	   }
	 %>
   <tbody id="dataBody"></tbody>
   
    <tr class="TableControl">
      <td colspan="10">
      <input type="checkbox" name="allbox" id="allbox_for" onClick="javascript:checkAll(this);"><label for="allbox_for">全选</label> &nbsp;
      <a href="javascript:delete_mail($F('pageIndex') , $F('pageLen'));" title="删除所选公告通知"><img src="/core/funcs/notify/images/delete.gif" align="absMiddle">删除所选公告</a>&nbsp;
      <a href="javascript:;" title="批量取消置顶"><img src="/core/funcs/notify/images/user_group.gif" align="absMiddle">取消置顶</a>&nbsp;
      <a href="javascript:delete_all();" title="删除所有自己发布的公告通知"><img src="/core/funcs/notify/images/delete.gif" align="absMiddle">删除全部公告</a>&nbsp;
      </td>
   </tr>
</table>


<div id="userpriv" style="width: 600px; height: 300px;">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.person.data.YHUserPriv"/>
</div>
</body>
</html>