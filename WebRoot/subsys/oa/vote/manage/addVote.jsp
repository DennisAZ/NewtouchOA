<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<html>
<head>
<title>投票管理</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
//表单验证
function checkForm() {
  if ($("subject").value == "") {
    alert("投票的标题不能为空！");
    $("subject").focus();
    $("subject").select();
    return false;
  }
  if ($("maxNum").value != "0" && !isNumber($("maxNum").value)) {
    alert("最多选择项只能为数字！");
    $("maxNum").focus();
    $("maxNum").select();
    return false;
  }
  if ($("minNum").value != "0" && !isNumber($("minNum").value)) {
    alert("最少选择项只能为数字！");
    $("minNum").focus();
    $("minNum").select();
    return false;
  }
  if ($("maxNum").value != "0" && $("minNum").value != "0" && ($("minNum").value > $("maxNum").value)) {
    alert("最少项不能大于最多项！");
    $("minNum").focus();
    $("minNum").select();
    return false;
  }
  return true;
}
//类型多选
function changeType() {
  if ($("type").value == "1") {
    $("maxNumDesc").style.display = "";
  }else {
    $("maxNumDesc").style.display = "none";
  }
}
//数据添加
function checkForm2() {
  if (checkForm()) {
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/addVote.act";
    var json = getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      window.location.href = "<%=contextPath%>/subsys/oa/vote/manage/vote.jsp?seqId=" + seqId;
    }
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5px">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3"> 新建投票</span>
    </td>
  </tr>
</table>
<form id="form1" name="form1">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.vote.data.YHVoteTitle">
<input type="hidden" name="fromId" id="fromId" value="<%=person.getSeqId()%>">
<input type="hidden" name="sendTime" id="sendTime" value="<%=sf.format(new Date()) %>">
<input type="hidden" name="publish" id="publish" value="0">
<input type="hidden" name="parentId" id="parentId" value="<%=seqId%>">
<input type="hidden" name="peaders" id="peaders" value="">
<input type="hidden" id="attachmentName" name="attachmentName" value="">
       <input type="hidden" id="attachmentId" name="attachmentId" value="">
<table class="TableBlock" width="70%" align="center">
<tr>
   <td nowrap class="TableData"> 标题：<font color="red">*</font></td>
   <td class="TableData">
     <input type="text" name="subject" id="subject" size="42" maxlength="100" class="BigInput" value="">
   </td>
</tr>
    <tr>
      <td nowrap class="TableData">投票描述：</td>
      <td class="TableData">
        <textarea name="content" id="content" class="BigInput" cols="50" rows="3"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 排序号：</td>
      <td class="TableData">
        <input type="text" name="voteNo" id="voteNo" size="10" maxlength="20" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 类型：</td>
      <td class="TableData">
        <select name="type" id="type" onChange="changeType();">
          <option value="0">单选</option>
          <option value="1">多选</option>
          <option value="2">文本输入</option>
        </select>
        <span id="maxNumDesc" style="display:none">最多允许选择 
        <input type="text" name="maxNum" id="maxNum" value="0" size="2" maxlength="2" class="SmallInput"> 项，&nbsp;最少允许选择 
        <input type="text" name="minNum" id="minNum" value="0" size="2" maxlength="2" class="SmallInput"> 项，0则不限制</span>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="保存" class="BigButton" onClick="javascript:checkForm2();">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="javascript:history.back()">
      </td>
    </tr>
  </table>
</form>
</body>
</html>