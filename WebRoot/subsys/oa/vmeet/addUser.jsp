<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<% 
 String seqId=request.getParameter("seqId");
 String userName=request.getParameter("userName");
%>

<head>
<title>邀请</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>

<script Language="JavaScript">
var userName='<%=userName%>';
var seqId=<%=seqId%>;
function doInit(){
	 $("smsContent").innerHTML="请参加["+userName+"]主持的视频会议，点击短信链接进入。";
}

function editUsers(){
    var toId= $("TO_ID").value;
    var content=$("smsContent").value;
    if(toId=="" || content=="" ){
      alert("请填写完整新建会议信息！");
      return false;
    }
    var url = contextPath + "/yh/subsys/oa/vmeet/act/YHVMeetAct/editUsers.act";
    var rtJsons = getJsonRs(url,"toId="+toId+"&content="+content+"&seqId="+seqId);
    if (rtJsons.rtState == "0") {
     
      alert("会议邀请已经发出！");
      window.close();
    } else {
      alert(rtJsons.rtMsrg); 
    }
    
 }



</script>
</head>

<body class="bodycolor" topmargin="5" onLoad="doInit();">

 <table class="TableBlock" id="newVM" width="90%" align="center">
  <form action="new.php"  method="post" name="form1" onsubmit="return CheckForm();">
    <tr>
      <td class="TableHeader">
        邀请参会人员
      </td>
    </tr>
    <tr>
      <td class="TableData">
        <input type="hidden" name="TO_ID" id="TO_ID" value="">
        <textarea cols=50 name="TO_NAME" id="TO_NAME" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['TO_ID', 'TO_NAME'])">添加</a>
        <a href="javascript:;" class="orgClear" onClick="clearUser('1')">清空</a>
      </td>
    </tr>
    <tr>
      <td class="TableHeader">
        会议邀请短信内容
      </td>
    </tr>
    <tr>
      <td class="TableData">
        <textarea cols=67 name="CONTENT" rows="3" id="smsContent" class="BigInput" wrap="on"></textarea>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" onclick="editUsers();" value="新建视频会议" class="BigButtonC">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>

</body>
</html>