<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>我的视频会议</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/vmeet/js/vmeet.js"></script>

<script Language="JavaScript">

function doInit(){
  getVMeetPriv();
  getBeginVM();
  getInvitedVM();
}

</script>
</head>

<body class="bodycolor" topmargin="5" onLoad="doInit();">

<table border="0" id="setPrivTitle" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vmeet.gif" WIDTH="30" HEIGHT="30" align="absmiddle"><span class="big3">设置新建权限</span>
    </td>
  </tr>
</table>

 <table id="addpriv" class="TableBlock" width="500" align="center">
  <form   method="post" name="form2">
    <tr>
      <td class="TableHeader">
        新建权限
      </td>
    </tr>
    <tr>
      <td class="TableData">
        <input type="hidden" name="TO_ID2" id="TO_ID2" value="">
        <textarea cols=45 name="TO_NAME2" id="TO_NAME2" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['TO_ID2', 'TO_NAME2'])">添加</a>
        <a href="javascript:;" class="orgClear" onClick="clearUser('2')">清空</a>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" onclick=" savePriv(); " value="保存" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>

 </table>
   </form>
<table border="0" id="newVMtitle" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vmeet.gif" WIDTH="30" HEIGHT="30" align="absmiddle"><span class="big3">新建视频会议</span>
    </td>
    <td align="right"></td>
  </tr>
</table>

 <table class="TableBlock" id="newVM" width="500" align="center">
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
        <input type="button" onclick="saveVMeet();" value="新建视频会议" class="BigButtonC">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vmeet.gif" WIDTH="30" HEIGHT="30" align="absmiddle"><span class="big3">最近建立的视频会议</span>
    </td>
  </tr>
</table>

<table class="TableBlock" id="beginVM" width="95%" align="center">
  <tr class="TableHeader">
      <td nowrap align="center">开始时间</td>
      <td nowrap align="center">参会信息</td>
      <td nowrap align="center">操作</td>
    </tr>
   <tbody id="beginVMTbody"></tbody>

</table>
<div align="center" style="display:none" id="showBeginData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>无新建会议</div></td>
  </tr>
  </table>
           
</div>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vmeet.gif" WIDTH="30" HEIGHT="30" align="absmiddle"><span class="big3">最近受邀参加的视频会议</span>
    </td>
  </tr>
</table>


<table class="TableBlock" id="invitedVM" width="95%" align="center">
  <tr class="TableHeader">
      <td nowrap align="center">开始时间</td>
      <td nowrap align="center">参会信息</td>
      <td nowrap align="center">操作</td>
    </tr>
<tbody id="invitedVMTbody"></tbody>
</table>
<div align="center" style="display:none" id="showinvitedData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>无受邀请会议</div></td>
  </tr>
  </table>
           
</div>

</body>
</html>