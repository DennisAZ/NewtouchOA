<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑网址</title>
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
<script type="text/javascript">
var seqId = "<%=seqId%>";

function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/system/address/act/YHAddressAct/getEditGroup.act?seqId="+seqId;
 // var rtJson = getJsonRs(url);
 // if(rtJson.rtState == "0"){
    
 // }else{
//    alert(rtJson.rtMsrg); 
//  }
}

function commit(){
  
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 编辑网址</span>
    </td>
  </tr>
</table>

<table class="TableBlock" width="90%" align="center">
  <form action="update.php"  method="post" name="form1" onsubmit="return CheckForm();">
   <tr>
    <td nowrap class="TableData">类型：</td>
    <td nowrap class="TableData">
      <select name="URL_TYPE" class="BigSelect" onchange="change_type();">
        <option value="">普通网址</option>
        <option value="1"<?if($URL_TYPE!="")echo " selected";?>>RSS订阅</option>
      </select>
    </td>
   </tr>
   <tr id="RSS_TYPE_TR" style="display:<?if($URL_TYPE=="")echo "none";?>;">
    <td nowrap class="TableData">RSS类别：</td>
    <td nowrap class="TableData">
      <select name="SUB_TYPE" class="BigSelect">
        <?=code_list("RSS_TYPE",$SUB_TYPE);?>
      </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">序号：</td>
    <td nowrap class="TableData">
        <input type="text" name="URL_NO" class="BigInput" size="10" maxlength="25" value="<?=$URL_NO?>">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">说明：</td>
    <td nowrap class="TableData">
        <input type="text" name="URL_DESC" class="BigInput" size="25" maxlength="200" value="<?=htmlspecialchars($URL_DESC)?>">
        <input type="button" id="btnGetTitle" class="SmallButton" value="根据RSS地址获取" onclick="get_title();" style="display:<?if($URL_TYPE=="")echo "none";?>;">
        <br><span id="loading" style="display:none;" class="TextColor2"><img src='/images/loading.gif' height='20' width='20' align='absMiddle'> 正在获取……</span>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">网址：</td>
    <td nowrap class="TableData">
        <input type="text" name="URL" class="BigInput" size="50" maxlength="200" value="<?=htmlspecialchars($URL)?>">
    </td>
   </tr>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" value="<?=$URL_ID?>" name="URL_ID">
        <input type="submit" value="确定" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="location='index.php'">
    </td>
  </form>
</table>
</body>
</html>