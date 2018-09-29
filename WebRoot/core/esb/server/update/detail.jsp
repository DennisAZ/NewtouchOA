<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String logSeqId = request.getParameter("seqId");
  if (logSeqId == null) {
    logSeqId = "";
  }
  String  type = request.getParameter("type") == null ? "" : request.getParameter("type");
  
  String headStr = "同步公章";
  if(type.equals("1")){
    headStr = "同步外部组织机构";
  }else if(type.equals("3")){
    headStr = "发布公告";
  }
  
  
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>系统更新详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var logSeqId = "<%=logSeqId%>";
var headStr = "<%=headStr%>";
/**
 * 获取最近10条同步公章信息
 */
function getTaskLogInfo(logSeqId){
  var url = "<%=contextPath%>/yh/core/esb/server/update/act/YHUpdateServerAct/getUpdateDetail.act";
  var rtJson = getJsonRs(url,{sendType:2,logSeqId:logSeqId});
  if (rtJson.rtState == "0") {
    var prcs = rtJson.rtData;
    if(prcs.length > 0){
      var table = new Element('table',{"class":'TableList',"align":"center","width":"90%"}).update("<tbody id='tbody'>"
          +"<tr class='TableHeader'><td>序号</td><td>状态</td><td>接收单位</td><td>操作时间</td>"+

          "</tbody>");
      $("taskList").insert(table);
      for(var i = 0;i<prcs.length;i++){
        var prc = prcs[i];
        var logSeqId = prc.logSeqId;
        var status = prc.updateStatus;
        var tr = new Element('tr',{"class":"TableData"});
        $("tbody").insert(tr);
        var statusStr = "发送中";
        if(status == "1"){
          statusStr = "发送成功";
        }else if(status == "-1"){
          statusStr = "发送失败";
        }else if(status == "2"){
          statusStr = "接收成功";
        }else if(status == "-2"){
          statusStr = "接收失败";
        }else if(status == "-4"){
          statusStr = "下载失败";
        }else if(status == "3"){
        	statusStr = "更新成功";
        }else if(status == "-3"){
        	statusStr = "更新失败"
        }
        var td = new Element('td',{"align":"center"}).update(i+1);
        //var td2 = new Element('td',{"align":"center"}).update(prc.userName);
        var td6 = new Element('td',{"align":"center"}).update(statusStr);
       // var td3 = new Element('td',{"align":"center"}).update(prc.fromDeptName);
        var td4 = new Element('td',{"align":"center"}).update(prc.clientName);
        var td5 = new Element('td',{"align":"center"}).update(prc.doneTime.substring(0,19));
       
      
        tr.insert(td);
       // tr.insert(td2);
        tr.insert(td6);
        //tr.insert(td3);
        tr.insert(td4);
        tr.insert(td5);
 
      }
    }else{
      
      var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
	        + "<td class='msg info'><div class='content' style='font-size:12pt'>没有同步更新数据!</div></td></tr>"
	        );
       $("Rnull").update(table);
    }
  }else {
    alert(rtJson.rtMsrg); 
  }
   
}

function doOnload(){
  getTaskLogInfo(logSeqId);
}
</script>
</head>
<body topmargin="5" onload="doOnload();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;更新详情</span>
    </td>
    
    <td class="Big">   </td>
  </tr>
</table>
<br></br>

<div id="taskList">
   
</div>
<div id="Rnull">

</div>


<div align="center" style="padding-top:20px;" > <input type="button" id="" class="BigButton" name="" value="关闭" onclick="window.close();"></div>
</body>
</html>