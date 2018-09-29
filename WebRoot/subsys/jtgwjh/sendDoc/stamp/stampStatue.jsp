<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>盖章情况详细信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/src/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/sendLogic.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/subsys/jtgwjh/sendDoc/js/logic.js"></script>
<script type="text/javascript">
function doInit(){
	var url = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/getDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
    
    
	  //盖章情况列表
    if(data.stampComplete == 1){
      var data4 =  data.data4;
      for(var i = 0; i < data4.length; i++){
        var td0 = $C('td');
        td0.align = 'center';
        td0.innerHTML = i+1;        
        
        var td1 = $C('td');
        td1.align = 'center';
        td1.innerHTML = data4[i].userName;
        
        var td2 = $C('td');
        td2.align = 'center';
        if(data4[i].stampType == 0){
	        td2.innerHTML = '主办';
        }
        else{
          td2.innerHTML = '协办';
        }
        
        var td3 = $C('td');
        td3.align = 'center';
        if(data4[i].stampStatus == 0){
	        td3.innerHTML = '待盖章';
        }
        else{
          td3.innerHTML = '已盖章';
        }
        
        var td4 = $C('td');
        td4.align = 'center';
        td4.innerHTML = data4[i].stampTime;
        
        var tr = $C('tr');
        tr.id = "print_tr_" + i;
        tr.name = "print_tr_" + i;
        tr.className = 'TableData';
        
        tr.appendChild(td0);
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        $('showStampTable').appendChild(tr);
      }
    }
	}else{
		alert(rtJson.rtMsrg);
	}
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 盖章情况详细信息</span><br>
    </td>
  </tr>
</table>

<br>
  
<table class="TableBlock" width="90%" align="center">
<thead>
  <tr class="TableHeader">
    <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:50px;">序号</td>
    <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:270px;">盖章人员</td>
    <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:100px;">盖章类型</td>
    <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:100px;">盖章状态</td>
    <td align="center" nowrap style="background-color: rgb(242, 242, 242);padding-left: 0px;width:250px;">盖章时间</td>
  </tr>
</thead>
<tbody id="showStampTable"></tbody>
</table>
  
</body>
</html>