<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>公告通知查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var field = 'SEND_TIME'; 
var ascDesc = '1';
var pageCount = 0;
function checkForm()
{     
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
    loadData(ascDesc,field);
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

/*function refush(){
  if (!trim($F('pageIndex'))) {
    alert('请输入有效的页码!');
    $('pageIndex').value = "";
    $('pageIndex').focus();
    return;
  }
  if ($F('pageIndex') > pageCount){
     alert('请输入有效的页码!');
     $('pageIndex').focus();
     $('pageIndex').select();
     return;
  }
  loadData(ascDesc,field);
}
*/
function loadData( ascDesc , field){
//	  $('freshLoad').className = "pgBtn pgLoad";
	  //$('pageIndex').value = pageIndex;
//	  $('pgSearchInfo').innerHTML = "加载数据中,请稍后.....";
	  var url = "<%=contextPath%>/yh/core/funcs/notify/act/YHNotifyShowAct/queryNotify.act?ascDesc="+ascDesc+"&field="+field;
	  var json = getJsonRs(url, mergeQueryString($("form1")));
	  document.getElementById("queryParams").style.display = "none";
	  document.getElementById("queryResult").style.display = "";
	  if(json.rtState == "0"){
	    var rtData = json.rtData;
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
	  var td = "<td align='center'><u title='部门："+data.deptName+"' style='cursor:pointer'>"+data.fromName+"</u></td>"
	            + "<td align='center'>" + data.typeName +"</td>"
	            +"<td><a href=javascript:open_notify('" + data.seqId + "','" + data.format + "'); title='"+data.subjectTitle+"'>"+data.subject+"</a></td>"
	            + "<td style='cursor:pointer' title='"+title+"'>"+str+"</td>"
	            + " <td nowrap align='center'>" + data.sendTime.substring(0,19) +"</td>"
	            + "<td nowrap align='center'>" + data.notifyStatusStr +"</td>";
	           
	  var className = "TableLine2" ;    
	  if(i%2 == 0){
	    className = "TableLine1" ;
	  }
	  var tr = new Element("tr" , {"class" : className}).update(td);
	  $('dataBody').appendChild(tr);  
	  
	}

function delete_news(seqId)
{
 msg='确认要删除该项公告吗？';
 if(window.confirm(msg))
 {
  url="/yh/yh/core/funcs/news/act/YHNewsHandleAct/deleteNewById.act?seqId="+seqId;
  window.location.href=url;
 }
}
function re_news(seqId)
{
 URL="/yh/core/funcs/news/show/reNews.jsp?seqId="+seqId+"&manage=1";
 myleft=(screen.availWidth-500)/2;
 window.open(URL,"read_news","height=500,width=550,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes");
}

function ClearUser(TO_ID, TO_NAME){
	  if(TO_ID=="" || TO_ID=="undefined" || TO_ID== null){
		TO_ID="TO_ID";
		TO_NAME="TO_NAME";
	  }
	  document.getElementsByName(TO_ID)[0].value="";
	  document.getElementsByName(TO_NAME)[0].value="";
}

function open_notify(seqId,format)
{
 URL="/yh/core/funcs/notify/show/readNotify.jsp?isManage=0&seqId="+seqId;
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

function back() {
	window.location.href = "/yh/core/funcs/notify/show/notifyQuery.jsp";
}
</script>
</head>
<body onload="doInit();">
<div id="queryParams">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath%>/search.gif"><span class="big3">公告查询</span>
    </td>
  </tr>
</table>

<form  action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="550" align="center">
    <tr>
      <td nowrap class="TableData">发布人：</td>
      <td class="TableData">
        <input type="hidden" id="user" name="fromId" value="">
        <textarea cols=30 id="userDesc" name="fromIdName" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectUser(['user', 'userDesc'] , 5);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('fromId','fromIdName')">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 格式：</td>
      <td class="TableData">
      <select name="format" class="BigSelect">
        <option value="" selected>全部</option>
        <option value="0">普通格式</option>
        <option value="1">MHT格式</option>
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
      <td nowrap class="TableData" width="100"> 标题：</td>
      <td class="TableData">
        <input type="text" name="subject" size="33" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 发布日期：</td>
      <td class="TableData">
       <input type="text" id = "beginDate" name="beginDate" class="BigInput" size="10" maxlength="10" value="" >
        <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
        至 <input type="text" id = "endDate" name="endDate" class="BigInput" size="10" maxlength="10" value="" >
        <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 内容：</td>
      <td class="TableData">
        <input type="text" name="content" size="33" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
       <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.notify.data.YHNotify"/>
        <input type="button" value="查询" class="BigButton" onClick="checkForm();">&nbsp;&nbsp;
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
      <td nowrap align="center">发布人</td>
      <td nowrap align="center">类型</td>
      <td nowrap align="center">标题</td>
      <td nowrap align="center">发布范围</td>
      <td nowrap align="center" id="sendTime" name="sendTime" style="">发布日期</td>
      <td nowrap align="center">状态</td>
   </tr>
    <tbody id="dataBody"></tbody>
 </table>
 </div>
 <div id="noData" style="display:none">
 <TABLE class=MessageBox width=280 align=center>
<TBODY>
<TR>
<TD class="msg info">
<DIV class=content 
style="FONT-SIZE: 12pt">无符合条件的公告通知</DIV></TD></TR></TBODY></TABLE>
</div>
<INPUT style="margin: 10px auto;display: block;" class=BigButton onclick="back();" type=button value=返回> 
</div>
<script type="text/Javascript">
function bindValidDtFunc() {
	bindAssertDateTimePrcBatch([{id:"beginDate", type:"d"}, {id:"endDate", type:"d"}]);
}

bindValidDtFunc();

</script>
</body>
</html>