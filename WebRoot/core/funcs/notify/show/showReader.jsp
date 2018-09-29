<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
<html>
<%
String seqId = request.getParameter("seqId");
if(seqId==null) {
  seqId = "";
}
%>
<head>
<title>公告通知查阅情况</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/Javascript" src="/yh/core/js/datastructs.js"></script>
<script type="text/Javascript" src="/yh/core/js/sys.js"></script>
<script type="text/Javascript" src="/yh/core/js/prototype.js"></script>
<script type="text/Javascript" src="/yh/core/js/smartclient.js"></script>
<script type="text/Javascript" src="/yh/core/js/cmp/select.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="/yh/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
 var seqId = "<%=seqId%>";
 var i = 0;
 var readCount = 0;
 var unReadCount = 0;
 var td1 = "";
function doInit(){
	  loadData();
}
function loadData(){
	  var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyShowAct/showReader.act?seqId='+seqId;
	  var json = getJsonRs(url);
	 // alert(rsText);
	  if(json.rtState == "0"){
	    var rtData = json.rtData;
	    var listData = rtData.listData;
	    $('subject').update(rtData.subject);
	    var sendTime = " <u title='部门:"+rtData.deptName+"' style='cursor:pointer'> "+rtData.providerName+"</u>&nbsp;&nbsp;发布于：<i>"+rtData.newsTime.substring(0,19)+"</i></u>";	
        $('sendTime').update(sendTime);

	      if(listData.length > 0){
		      for( i = 0 ;i < listData.length ;i ++){
		        var data = listData[i];
		        addRow(data, i);
		       
		      }
		    }
	      $('content').update(td1);
	      $('readCount').update(readCount);
	      $('unReadCount').update(unReadCount);
	  }else{
	    document.body.innerHTML = json.rtMsrg;
	  } 
	}

function addRow(data , i){
    td1 = td1 + "<tr><td class='TableData' align='left'>&nbsp;"+data.deptName+"</td>"
            +"<td class='TableData'>&nbsp;"+data.userNameStr+"</td>"
            +"<td class='TableData'>&nbsp;"+data.unUser+"</td>";
      
   var read = data.userNameStr;
	 readCount = readCount + read.split(",").length-1;

	  var unRead = data.unUser;
	 unReadCount = unReadCount + unRead.split(",").length-1;
	}

function delete_reader()
{
 msg='确认要清空查阅情况吗？';
 if(window.confirm(msg))
 {
  URL= contextPath+"/yh/core/funcs/notify/act/YHNotifyShowAct/deleteReader.act?seqId=" + seqId;
  window.location=URL;
 }
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/sms/sms_type1.gif" align="absmiddle"><span class="big3"> 查阅情况</span>
      &nbsp;&nbsp;&nbsp;<input type="button" value="清空查阅情况" class="BigButtonC" onclick="delete_reader();">
    </td>
    </tr>
</table>
<table class="TableBlock" width="100%" align="center" id="showReader">
   <tHead>
    <tr>
      <td align="center" class="TableHeader" colSpan="3" id="subject"></td>
    </tr>
    <tr>
     <td align="right" class="TableContent" colSpan="3" id="sendTime"></td>
    </tr>
    <tr>
     <td align="center" class="TableContent">部门/成员单位</td>
     <td align="center" class="TableContent">已读人员</td>
     <td align="center" class="TableContent">未读人员</td>
    </tr>
   </tHead>
   <tBody id="content">
   </tBody>
   <tFoot>
    <tr class="TableControl">
     <td align="center"><b>合计：</b></td>
     <td align="center" id="readCount"></td>
     <td align="center" id="unReadCount"></td>
    </tr>
   </tFoot>
  </table>


</body>
</html>