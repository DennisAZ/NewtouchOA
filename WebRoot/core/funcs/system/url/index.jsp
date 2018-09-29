<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>公共网址设置</title>
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
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">

function doInit(){
  var mgr = new SelectMgr();
  mgr.addSelect({cntrlId: "subType"
    , tableName: "CODE_ITEM"
      , codeField: "CLASS_CODE"
        , nameField: "CLASS_DESC"
          , value: "", isMustFill: "1"
            , filterField: "CLASS_NO"
              , filterValue: 'RSS_TYPE'
                , order: ""
                  , reloadBy: ""
                    , actionUrl: ""
                      });
  mgr.loadData();
  mgr.bindData2Cntrl();
}

function commit(){
  var urlType = document.getElementById("urlType").value;
  var subType = document.getElementById("subType").value;
  var urlNo = document.getElementById("urlNo").value;
  var urls = document.getElementById("url").value;
  var urlDesc = document.getElementById("urlDesc").value;
  
  var url = "<%=contextPath%>/yh/core/funcs/system/url/act/YHUrlAct/addPublicUrl.act";
  var rtJson = getJsonRs(url, "urlType="+urlType+"&subType="+subType+"&urlNo="+urlNo+"&url="+urls+"&urlDesc="+urlDesc);
  if(rtJson.rtState == "0"){
    alert(rsText);
    window.location.reload();
  }else{
    alert(rtJson.rtMsrg);
  }
}

function changeType(){
  var trStr = document.getElementById("rssTypeTr");
  if(document.getElementById("urlType").value == "1"){
    trStr.style.display = '';
  }else if(document.getElementById("urlType").value == ""){
    trStr.style.display = 'none';
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 添加公共网址</span>
    </td>
  </tr>
</table>

<table class="TableBlock" width="90%" align="center">
  <form method="post" name="form1" id="form1">
   <tr>
    <td nowrap class="TableData">类型：</td>
    <td nowrap class="TableData">
      <select name="urlType" id="urlType" class="BigSelect" onchange="changeType();">
        <option value="">普通网址</option>
        <option value="1">RSS订阅</option>
      </select>
    </td>
   </tr>
   <tr id="rssTypeTr" style="display:none;">
    <td nowrap class="TableData">RSS类别：</td>
    <td nowrap class="TableData">
      <select name="subType" class="BigSelect" id="subType">
      </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">序号：</td>
    <td nowrap class="TableData">
        <input type="text" name="urlNo" id="urlNo" class="BigInput" size="10" maxlength="25">
    </td>
   <tr>
    <td nowrap class="TableData">说明：</td>
    <td nowrap class="TableData">
        <input type="text" name="urlDesc" id="urlDesc" class="BigInput" size="25" maxlength="200">
 <!--   <input type="button" id="btnGetTitle" class="SmallButton" value="根据RSS地址获取" onclick="get_title();" style="display:<?if($URL_TYPE=="")echo "none";?>;">
        <br><span id="loading" style="display:none;" class="TextColor2"><img src='/images/loading.gif' height='20' width='20' align='absMiddle'> 正在获取……</span>--> 
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">网址：</td>
    <td nowrap class="TableData">
        <input type="text" name="url" id="url" class="BigInput" size="50" maxlength="200" value="http://">
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" name="USER" value="">
        <input type="button" value="添加" class="BigButton" title="添加网址" name="button" OnClick="commit()">
    </td>
  </form>
</table>

<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3"> 管理公共网址</span>
    </td>
  </tr>
</table>

<br>
<div align="center">
<table class="TableList" width="90%">
    <tr class="TableData">
      <td nowrap align="center"><?=$URL_NO?></td>
      <td nowrap align="center"><?=$URL_DESC?></td>
      <td><A href="<?=$URL?>" target="_blank"><?=$URL?></A></td>
      <td nowrap align="center"><?=$URL_TYPE_DESC?></td>
      <td nowrap align="center"><?=$SUB_TYPE_DESC?></td>
      <td nowrap align="center" width="80">
      <a href="edit.jsp"> 编辑</a>
      <a href="javascript:deleteUrl('<?=$URL_ID?>');"> 删除</a>
      </td>
    </tr>
    <thead class="TableHeader">
      <td nowrap align="center">序号</td>
      <td nowrap align="center">说明</td>
      <td nowrap align="center">网址</td>
      <td nowrap align="center">类别</td>
      <td nowrap align="center">子类别</td>
      <td nowrap align="center">操作</td>
    </thead>
    <thead class="TableControl">
      <td nowrap align="center" colspan="5">
      <input type="button" class="BigButton" OnClick="javascript:deleteAll();" value="全部删除">
      </td>
    </thead>
</table>
</div>
</body>
</html>