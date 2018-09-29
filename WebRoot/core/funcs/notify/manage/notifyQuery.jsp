<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.core.funcs.person.data.*"%>
<%
 YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; earset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var field = 'SEND_TIME'; 
var ascDesc = '1';
function checkForm()
{
	var operation1 = document.getElementById("operation1");
	var operation2 = document.getElementById("operation2");
	var operation3 = document.getElementById("operation3");
	var regex = /^((\d{2}(([02468][048])|([13579][26]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|([1-2][0-9])))))|(\d{2}(([02468][1235679])|([13579][01345789]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))/; //日期部分
	   if(document.form1.beginDate.value!=""){
	     if(!regex.test(document.form1.beginDate.value)){
	   	 alert("输入的日期格式错误");
	   	 document.form1.beginDate.value = "";
	   	 document.form1.beginDate.focus();
	       return false;
	     }
	   }
	   if(document.form1.endDate.value!=""){
	       if(!regex.test(document.form1.endDate.value)){
	     	 alert("输入的日期格式错误");
	     	document.form1.endDate.value = "";
	     	 document.form1.endDate.focus();
	         return false;
	       }
	  }

   if(document.form1.beginDate.value>document.form1.endDate.value && document.form1.endDate.value !="") {
	   alert("起始日期不能大于结束日期");
	   return false;
   }
   if(operation1.checked){
	    loadData();
   }else if(operation2.checked)
   {
   	  msg='删除后不能恢复，确认删除吗？';
      if(window.confirm(msg)){
         document.form1.action = contextPath + "/yh/core/funcs/notify/act/YHNotifyHandleAct/deleteSelNotify.act";  
         $("form1").submit(); 
      }   
   }else if(operation3.checked){     
      document.form1.action= contextPath + "/yh/core/funcs/notify/act/YHNotifyHandleAct/toExcel.act";
      $("form1").submit();
   }
   return true;
}

function loadData(){
//	  $('freshLoad').className = "pgBtn pgLoad";
	  //$('pageIndex').value = pageIndex;
//	  $('pgSearchInfo').innerHTML = "加载数据中,请稍后.....";
	  var url = "<%=contextPath%>/yh/core/funcs/notify/act/YHNotifyHandleAct/queryNotify.act";
	  var json = getJsonRs(url, mergeQueryString($("form1")));
	  document.getElementById("queryParams").style.display = "none";
	  document.getElementById("queryResult").style.display = "";
	  $("dataBody").update();
	  if(json.rtState == "0"){
	    var rtData = json.rtData;
	    var pageData = rtData.pageData;
	    var listData = rtData.listData;
	    if(listData.length > 0){
	      for(var i = 0 ;i < listData.length ;i ++){
	        var data = listData[i];
	        addRow(data, i);
	       
	      }
	    }else{
	      $('hasData').style.display = "none";
	      $('noData').style.display = "";
	    }
	  }else{
	    document.body.innerHTML = json.rtMsrg;
	  } 
	}

function addRow(data , i){
  var toNameTitle = data.toNameTitle;
  var toNameStr = data.toNameStr;
  var privNameTitle = data.privNameTitle;
  var privNameStr = data.privNameStr;
  var userNameTitle = data.userNameTitle;
  var userNameStr = data.userNameStr;
  var title = toNameTitle + privNameTitle + userNameTitle;
  var str = toNameStr + privNameStr + userNameStr;  
	 var td = "<td>&nbsp;<input type='checkbox' name='deleteFlag' value='" + data.seqId +"'>"+"</td>"
            + "<td nowrap align='center'><u title='部门:" + data.deptName + "' style='cursor:pointer'>"+data.fromName+"</u></td>"
            + "<td nowrap align='center'>" + data.typeName +"</td>"
            + "<td style='cursor:pointer' title='" + data.toNameTitle  +"' onClick='show_toobject(" + data.seqId + ");'>" + str + "</td>"
            + "<td><a href=javascript:open_notify('" + data.seqId + "','" + data.format + "'); title='" + data.subjectTitle + "'>" + data.subject + "</a></td>"
            + " <td align='left'>"+ data.sendTime.substring(0,19) +"</td>"
            + " <td nowrap align='center'>" + data.beginDate +"</td>"
            + "<td nowrap align='center'>" + data.endDate +"</td>";
    var notifyStatusStr = data.notifyStatusStr;
    if(notifyStatusStr=="") {
      td = td + "<td nowrap align='center'>" + data.publishDesc + "</td>";
    }else {
      td = td + "<td nowrap align='center'>" + notifyStatusStr + "</td>";
    }
    td = td + "<td><a href='javascript:show_reader(" + data.seqId + ");' title='查阅情况'> 查阅情况</a>&nbsp;<a href='/yh/core/funcs/notify/manage/notifyAdd.jsp?seqId=" + data.seqId + "'> 修改</a>";
    var notifyStatus = data.notifyStatus;
    var publish = data.publish;
    if(notifyStatus=="1"&&publish=="1"){
        td = td + "&nbsp;<a href='/yh/yh/core/funcs/notify/act/YHNotifyHandleAct/changeState.act?seqId="+data.seqId+"&operation=1'> 立即生效</a>"
    }else if(notifyStatus=="2"&&publish=="1"){
  	  td = td + "&nbsp;<a href='/yh/yh/core/funcs/notify/act/YHNotifyHandleAct/changeState.act?seqId="+data.seqId+"&operation=2'>终止</a>"
    }else if(notifyStatus=="3"&&publish=="1"){
  	  td = td + "&nbsp;<a href='/yh/yh/core/funcs/notify/act/YHNotifyHandleAct/changeState.act?seqId="+data.seqId+"&operation=3'>生效</a>"
    }
	           
	  var className = "TableLine2" ;    
	  if(i%2 == 0){
	    className = "TableLine1" ;
	  }
	  var tr = new Element("tr" , {"class" : className}).update(td);
	  $('dataBody').appendChild(tr);  
	  
	}


function doInit(){
	 var beginParameters = {
		      inputId:'beginDate',
		      property:{isHaveTime:false}
		      ,bindToBtn:'beginDateImg'
		  };
		  new Calendar(beginParameters);
		  var endParameters = {
		      inputId:'endDate',
		      property:{isHaveTime:false}
		      ,bindToBtn:'endDateImg'
		  };
		  new Calendar(endParameters);

		  var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/getnotifyType.act';
		  var json = getJsonRs(url);
		  if (json.rtState == "0"){
			  var rtData = json.rtData;
				var typeData = rtData.typeData;
				 $("typeId").options.add(new Option('全部',''));
				 if(typeData.length > 0){
				     for(var i = 0 ;i < typeData.length ;i ++){
				       var optionStr = typeData[i];
				       $("typeId").options.add(new Option(optionStr.typeDesc,optionStr.typeId)); 
				     }
				  }
			} else{
		      alert(json.rtMsrg);
		    }	  
}


function ClearUser(TO_ID, TO_NAME){
	  if(TO_ID=="" || TO_ID=="undefined" || TO_ID== null){
		TO_ID="TO_ID";
		TO_NAME="TO_NAME";
	  }
	  document.getElementsByName(TO_ID)[0].value="";
	  document.getElementsByName(TO_NAME)[0].value="";
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
		  window.location.href = "<%=contextPath%>/core/funcs/notify/manage/notifyList.jsp";
		} else{
	      alert(json.rtMsrg);
	    }
  }
}

function cancel_top()
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
     alert("要取消置顶，请至少选择其中一条。");
     return;
  }

  msg='确认要取消置顶所选公告通知吗？';
  if(window.confirm(msg))
  {   
	 // var par = 'pageIndex='+pageIndex+'&showLength=' +showLength+'&delete_str=' +delete_str;

      var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/cancelTop.act?delete_str='+delete_str;
	  var json = getJsonRs(url);
	  if (json.rtState == "0"){
		  window.location.href = "<%=contextPath%>/core/funcs/notify/manage/notifyQuery.jsp";
		} else{
	      alert(json.rtMsrg);
	    }
  }
}

function end_notify()
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
     alert("要终止公告，请至少选择其中一条。");
     return;
  }

  msg='确认要终止所选公告通知吗？';
  if(window.confirm(msg))
  {   
	  var par = 'delete_str=' +delete_str + '&operation=2';
      var url =contextPath + '/yh/core/funcs/notify/act/YHNotifyHandleAct/changeStateGroup.act';
	  var json = getJsonRs(url,par);
	  if (json.rtState == "0"){
		  window.location.href = "<%=contextPath%>/core/funcs/notify/manage/notifyList.jsp";
		} else{
	      alert(json.rtMsrg);
	    }
  }
}


function back() {
	
	window.location.href = "/yh/core/funcs/notify/manage/notifyQuery.jsp";
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

function show_toobject(seqId)
{
  URL="/yh/core/funcs/notify/manage/showObject.jsp?seqId="+seqId;
  myleft=(screen.availWidth-500)/2;
  window.open(URL,"read_notify","height=250,width=600,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}

function my_affair(seqId)
{
  myleft=(screen.availWidth-250)/2;
  mytop=(screen.availHeight-200)/2;
  URL="/yh/core/funcs/notify/manage/note.jsp?seqId="+seqId;
  window.open(URL,"","height=200,width=250,status=0,toolbar=no,menubar=no,location=no,scrollbars=auto,resizable=no,top="+mytop+",left="+myleft);
}

function open_notify(seqId,format)
{
 URL="/yh/core/funcs/notify/show/readNotify.jsp?isManage=1&seqId="+seqId;
 myleft=(screen.availWidth-780)/2;
 mytop=100
 mywidth=780;
 myheight=500;
 if(format=="1")
 {
    myleft=0;
    mytop=0
    mywidth=screen.availWidth-15;
    myheight=screen.availHeight-60;
 }
 window.open(URL,"公告通知","height="+myheight+",width="+mywidth+",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function show_reader(seqId)
{
  URL="/yh/core/funcs/notify/show/showReader.jsp?seqId="+seqId;
  myleft=(screen.availWidth-500)/2;
  window.open(URL,"read_notify","height=500,width=700,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}

function checkAll(field) {
	  var deleteFlags = document.getElementsByName("deleteFlag");
	  for(var i = 0; i < deleteFlags.length; i++) {
	    deleteFlags[i].checked = field.checked;
	  }
	}
</script>
</head>
<body onload="doInit();">
<div id="queryParams">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath%>/search.gif"><span class="big3"> 公告通知查询</span>
    </td>
  </tr>
</table>

<form method="post" id="form1" name="form1" onsubmit="return checkForm();">
<table class="TableBlock" width="550" align="center">
   <%if(user.isAdminRole()){%>
    <tr>
      <td nowrap class="TableData">发布人:</td>
      <td class="TableData">
        <input type="hidden" id="user" name="fromId" value="">
        <textarea cols=30 id="userDesc" name="fromName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectUser(['user', 'userDesc'] , 5);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('fromId','fromName')">清空</a>
      </td>
    </tr>
   <%}%>   
    <tr>
      <td nowrap class="TableData" width="100"> 格式：</td>
      <td class="TableData">
      <select name="format" class="BigSelect">
        <option value="" selected>全部</option>
        <option value="0">普通格式</option>
        <option value="1">MHT格式</option>
        <option value="2">超级链接</option>
      </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 类型：</td>
      <td class="TableData">
        <select id="typeId" name="typeId" class="BigSelect">
        </select>&nbsp;
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 发布状态：</td>
      <td class="TableData">
      <select name="publish" class="BigSelect">
        <option value="" selected>全部</option>
        <option value="0">未发布</option>
        <option value="1">已发布</option>
      </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 是否置顶：</td>
      <td class="TableData">
      <select name="top" class="BigSelect">
        <option value="" selected>全部</option>
        <option value="0">未置顶</option>
        <option value="1">已置顶</option>
      </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 标题：</td>
      <td class="TableData">
        <input type="text" name="subject" size="33" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 发布日期：</td>
      <td class="TableData">
        <input type="text" id = "beginDate" name="beginDate" class="BigInput" size="10" maxlength="10"  value="">
        <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
                    至&nbsp;
       <input type="text" id = "endDate" name="endDate" class="BigInput" size="10" maxlength="10"  value="">
        <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 内容：</td>
      <td class="TableData">
        <input type="text" name="content" size="33" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 生效状态：</td>
      <td class="TableData">
      <select name="stat" class="BigSelect">
        <option value="" selected>全部</option>
        <option value="1">待生效</option>
        <option value="2">已生效</option>
        <option value="3">已终止</option>
      </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 操作：</td>
      <td class="TableData">
        <input type="radio" name="operation" id="operation1" value="1" checked><label for="operation1">查询</label>
        <input type="radio" name="operation" id="operation2" value="2"><label for="operation2">删除</label>
        <input type="radio" name="operation" id="operation3" value="3"><label for="operation3">导出excel文件</label>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
         <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.notify.data.YHNotify"/>
         <input type="button" value="确定" class="BigButton" onClick="checkForm();">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</div>
<div id="queryResult" style="display:none">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath%>/search.gif"><span class="big3"> 公告查询结果</span>
    </td>
  </tr>
</table>

<div id="hasData">
  <table class="TableList" width="100%">
    <tr class="TableHeader">
      <td nowrap align="center">选择</td>
      <td nowrap align="center">发布人</td>
      <td nowrap align="center">类型</td>
      <td nowrap align="center">发布范围</td>
      <td nowrap align="center">标题</td>
      <td nowrap align="center">创建时间</td>
      <td nowrap align="center">生效日期</td>
      <td nowrap align="center">终止日期</td>
      <td nowrap align="center">状态</td>
      <td nowrap align="center" width='120'>操作</td>
    </tr>
    <tbody id="dataBody"></tbody>
     <tr class="TableControl">
      <td colspan="10">
      <input type="checkbox" name="allbox" id="allbox_for" onClick="javascript:checkAll(this);"><label for="allbox_for">全选</label> &nbsp;
      <a href="javascript:delete_mail();" title="删除所选公告通知"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选公告</a>&nbsp;
      <a href="javascript:cancel_top();" title="批量取消置顶"><img src="<%=imgPath%>/user_group.gif" align="absMiddle">取消置顶</a>&nbsp;
      <a href="javascript:end_notify();" title="批量终止所选公告" ><img src="<%=imgPath%>/inout.gif" align="absMiddle">终止 </a>&nbsp;
      </td>
   </tr>
  </table>
  <br><center><input type="button" class="BigButton" value="返回" onclick="goBack();return false;"></center>
  <script>
    function goBack(){
     window.location.href= contextPath + "/core/funcs/notify/manage/notifyQuery.jsp";
    }
  </script>
 </div>
 <div id="noData" style="display:none">
 <TABLE class=MessageBox width=280 align=center>
<TBODY>
<TR>
<TD class="msg info">
<DIV class=content 
style="FONT-SIZE: 12pt">无符合条件的公告</DIV></TD></TR></TBODY></TABLE>
<DIV align=center>
<INPUT class=BigButton onclick="back();" type=button value=返回> 
</DIV>
</div>
</div>
<script>
function bindValidDtFunc() {
	bindAssertDateTimePrcBatch([{id:"beginDate", type:"d"}, {id:"endDate", type:"d"}]);
}
bindValidDtFunc();
</script>
</body>
</html>