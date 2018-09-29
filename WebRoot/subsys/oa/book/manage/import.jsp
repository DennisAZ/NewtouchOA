<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.subsys.oa.book.data.*"%>
<%@ page  import="java.util.List"%>
<html>
<head>
<title>新建图书</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}

.MessageBox .msg{
   height:90px;
}
.MessageBox .msg .left{
   width:20px;
   background:url('images/message_center_bg.png') left top repeat-y;
}
.MessageBox .msg .center{
   padding-left:90px;
   font-size:16px;
   background-color:#357ece;
   background-color/*\**/:#2b73c7\9;
}
.MessageBox .msg .center .msg-content{
   color:#fff;
   margin:20px 12px 20px 6px;
}
.MessageBox .msg .right{
   width:20px;
   background:url('images/message_center_bg.png') right top repeat-y;
}
/* 表头 */
.MessageBox .head-no-title{
   height:16px;
}
.MessageBox .head-no-title .left{
   width:20px;
   background:url('images/message_bg.png') left -96px no-repeat;
}
.MessageBox .head-no-title .center{
   background:url('images/message_bg.png') 0px -112px repeat-x;
}
.MessageBox .head-no-title .right{
   width:20px;
   background:url('images/message_bg.png') right -96px no-repeat;
}
.MessageBox .head .center .title{
   float:left;
   color:#fff;
   font-size:16px;
   font-weight:bold;
   margin-top:5px;
}

</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
  function Back(){
    window.location.href= contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/jinruBookType.act";
  }
  /*
  导入图书
*/
  function checkForm() {
    if (document.getElementById("csvFile").value == "") {
       alert("请选择要导入的文件！");
       return false;
    }
    if (document.getElementById("csvFile").value != "") {
      var fileTemp = document.getElementById("csvFile").value,fileName;
      var pos;
      pos = fileTemp.lastIndexOf("\\");
      fileName = fileTemp.substring(pos+1,fileTemp.length);
      var numLeg = fileName.lastIndexOf(".");
      document.getElementById("fileName").value = fileName;
      if (fileName.substring(numLeg+1,fileName.length) != "csv") {
        alert("只能导入CSV文件!");
        return false;
      }
    }

    document.form1.action = contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/importBookTypeinfo.act";
    document.form1.submit();
    return true;
  }
 /*
   导入图书
 
  function importBook(){
   document.form1.action = contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/importBookTypeinfo.act";
   document.form1.submit();
   return true;
  }
  */
  function templetImport(){
    document.form1.action = contextPath + "/yh/subsys/oa/book/act/YHBookTypeAct/templetImport.act";
    document.form1.submit();
    return true;
  }
  
</script>

</head>
<body class="bodycolor" topmargin="5">

  <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
      <td class="Big"><img src="<%=imgPath %>/book.gif" align="absmiddle"><span class="big3"> 图书信息导入</span><br>
      </td>
    </tr>
  </table>
  <br>
  <br>

  <div align="center" class="Big1">
  <b>请指定用于导入的CSV文件：</b>
  <form name="form1" id="form1" method="post" action="#" enctype="multipart/form-data" onsubmit="return CheckForm();">
    <input type="file" name="csvFile" id="csvFile" class="BigInput" size="30">
    <input type="hidden" name="fileName" id="fileName">
    <input type="hidden" name="GROUP_ID"  id="GROUP_ID" value="">
    <input type="button" onclick="checkForm();" value="导入" class="SmallButton">
  </form>

  <br>
   请使用图书信息模板导入数据！<a href="#" onClick="javascript:templetImport(); return false;">图书信息导入模板下载</a>
  <br>

<table width="500" align="center" class="MessageBox">
   <tbody><tr class="head" style="color:blue">
      <td class="left" ></td>
      <td class="center" style="">
         <div class="title">导入说明</div>
      </td>
      <td class="right"></td>
   </tr>
   <tr class="msg">
      <td class="left"></td>
      <td class="center info">
         <div class="msg-content">导入的文件请使用模板文件导入，借阅状态未借出用0表示，借出用1表示。借阅范围如果所填写的部门不是全体部门，本部门，并且在数据库中找不到此部门则均以本部门处理，即借阅范围可以是以下几种情况：1.全体部门2.本部门3.系统部,销售部,市场部 (注意逗号为英文状态下的)。</div>
      </td>
      <td class="right"></td>
   </tr>
   <tr class="foot">
      <td class="left"></td>
      <td class="center"></td>
      <td class="right"></td>
   </tr>
</tbody></table>
 <input type="button" onclick="Back();" class="SmallButton" value="返回">
</div>
</body>
</html>