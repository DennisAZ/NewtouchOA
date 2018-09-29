<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>签章管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href="<%=cssPath%>/style.css"/>
<style>
.TableList {
  border:0 solid #3063A8;
  font-size:9pt;
  line-height:25px;
}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var pageMgr;
function doInit(){
  
  var requestURL = "<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHBilingualAct/getPage.act";
  var cfgs = {
    dataAction: requestURL,
    container: "listDiv",
    colums: [
       {type:"hidden", name:"seqId", text:"ID", width:100},
       {type:"text", name:"type", text:"类别", width:50,render:typeRender,align:'center'},
       {type:"text", name:"cnName", text:"中文名称", width:150,align:'center'},
       {type:"text", name:"enName", text:"英文名称", width:150,align:'center'},
       {type:"text", name:"soundFile", text:"英文语音文件", width:150,align:'center',render:soundRender},
       {type:"text", name:"entryUser", text:"录入人", width:80,align:'center'},
       {type:"text", name:"entryDate", text:"录入日期", width:80,align:'center',dataType:'date',format:'YY-MON-DD'},
       {type:"text", name:"enable", text:"是否启用", width:80,align:'center',render:enableRender}
       ]
  };
  
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}

function typeRender(cellData, recordIndex, columInde){
  switch(cellData){
  case '0':return "职务职称";
  case '1':return "菜谱";
  case '2':return "标识标准";
  default:return "";
  };
}


function enableRender(cellData, recordIndex, columInde){
  switch(cellData){
  case '0':return "<font color='red'>不启用</font>";
  case '1':return "启用";
  default:return "";
  };
}


function soundRender(cellData, recordIndex, columInde){
  if (cellData){
    var renderData = '<img src="images/sound.png" style="cursor:pointer;" onclick="playSound(' + recordIndex + ')"></img>';
    return renderData;
  }
  else{
    return '';
  }
}

function playSound(recordIndex){
  var snd = $('snd');
  var soundSrc = pageMgr.getCellData(recordIndex,"soundFile");
  if (soundSrc != '' && typeof soundSrc != undefined){
    snd.src = '<%=contextPath%>/bilingual/' + soundSrc.replace(/^[\s\S]+\\/g, "");
  }
}
function search(){
  para = "?cnName=" + encodeURI(encodeURI($('cnName').value)) + "&enName=" + $('enName').value + '&type=' + $('type').value;
  pageMgr.dataAction = "<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHBilingualAct/searchPage.act" + para;
  pageMgr.refreshAll();
}

</script>
</head>
<body onload="doInit()">
<bgsound border="0" id="snd" loop="0" src=""></bgsound>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;双语标示查询</span>&nbsp;
    </td>
  </tr>
</table>
<table width="400px" class="TableList" align="center" >
      <td nowrap class="TableData" width=100> 标示牌类型：</td>
     <td nowrap class="TableData" width=110>
        <select name="type" id="type">
         <option></option>
  <option value="0">职务职称</option>
  <option value="1">菜谱</option>
  <option value="2">标识标准</option>
        </select>
      </td>
    <td nowrap class="TableData" width=100> 中文名称：</td>
      <td nowrap class="TableData" width=220>
        <input type="text" class="BigInput" size=30  name="cnName" id="cnName">
      </td>
      <td nowrap class="TableData" width=100> 英文名称：</td>
      <td nowrap class="TableData" width=220>
        <input type="text" class="BigInput" size=30  name="enName" id="enName" value="">
      </td>
     <td class="TableControl" align="center" nowrap >
       <input type="button" value="查询" class="BigButton" onclick="search()">
     </td>
  </table>
 
<table border="0" width="400px" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;双语标示列表</span>&nbsp;
    </td>
  </tr>
</table>
<div id="listDiv" align="center"></div>
</body>
</html>