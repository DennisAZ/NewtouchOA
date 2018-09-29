<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>标示纠错管理</title>
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
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var pageMgr;
function doInit(){
  var requestURL = "<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHCorrectionAct/getPage.act";
  var cfgs = {
    dataAction: requestURL,
    container: "listDiv",
    colums: [
         {type:"hidden", name:"seqId", text:"ID", width:100},
         {type:"text", name:"content", text:"标示错误内容", width:100,align:'center'},
         {type:"text", name:"picture", text:"标示错误图片", width:100,align:'center',render:pictureRender},
         {type:"text", name:"changes", text:"建议更改为", width:100,align:'center'},
         {type:"text", name:"type", text:"标示牌类型", width:80,align:'center',render:typeRender},
         {type:"text", name:"location", text:"标示牌所在区县", width:110,align:'center',render:addRender},
         {type:"text", name:"correctDate", text:"检查日期", width:80,align:'center',dataType:'date',format:'YY-MON-DD'},
         {type:"text", name:"correcter", text:"纠错人姓名", width:80,align:'center'},
         {type:"text", name:"workplace", text:"纠错人所在单位/院校", width:135,align:'center'},
         {type:"text", name:"email", text:"Email", width:120,align:'center'},
         {type:"text", name:"address", text:"标示牌具体位置", width:100,align:'center'},
         {type:"text", name:"tel", text:"纠错人联系电话", width:100,align:'center'},
         {type:"text", name:"flag", text:"经过确认", width:80,align:'center',render:confirmRender}]
  };
  
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  calendarInit();
}


/**
 * 初始化时间组件
 */
function calendarInit(){
  var beginParameters = {
      inputId:'correctDate',
      property:{isHaveTime:false},
      bindToBtn:'correctDateImg'
  };
  new Calendar(beginParameters);
}

function typeRender(cellData, recordIndex, columInde){
  switch(cellData){
  case '0':return "英文标识";
  case '1':return "英文菜单";
  case '2':return "组织结构职务职称";
  default:return "";
  };
}


function confirmRender(cellData, recordIndex, columInde){
  switch(cellData){
  case '0':return "<font color='red'>否</font>";
  case '1':return "是";
  default:return "";
  };
}


function addRender(cellData, recordIndex, columInde){
  switch(cellData){
  case '0':return "昌平区";
  case '1':return "朝阳区";
  case '2':return "崇文区";
  case '3':return "大兴区";
  case '4':return "东城区";
  case '5':return "房山区";
  case '6':return "丰台区";
  case '7':return "海淀区";
  case '8':return "怀柔区";
  case '9':return "门头沟区";
  case '10':return "密云县";
  case '11':return "平谷区";
  case '12':return "石景山区";
  case '13':return "顺义区";
  case '14':return "通州区";
  case '15':return "西城区";
  case '16':return "宣武区";
  case '17':return "延庆县";
  default:return "";
  };
}

function pictureRender(cellData, recordIndex, columInde){
  if (cellData) {
    return '<a href="<%=contextPath%>/bilingual/correction/' + encodeURIComponent(cellData) + '" target="_blank"><img src="<%=contextPath%>/bilingual/correction/thumb-' + cellData + '"></a>';
  }
  else {
    return '暂无图片';
  }
}

function search(){
  para = "?content=" + encodeURI(encodeURI($('content').value)) +
    "&address=" + encodeURI(encodeURI($('address').value)) +
    "&correcter=" + encodeURI(encodeURI($('correcter').value)) +
    "&tel=" + $('tel').value +
    "&type=" + $('type').value +
    "&correctDate=" + $('correctDate').value +
    "&location=" + $('location').value;
  pageMgr.dataAction = "<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHCorrectionAct/searchPage.act" + para;
  pageMgr.search();
}

</script>
</head>
<body topmargin="5" onload="doInit()" onunload="hideWindow()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;标示纠错查询</span>&nbsp;
    </td>
  </tr>
</table>
<table style="width:400px;" class="TableList" align="center" >
  <tr>
    <td nowrap class="TableData" width=100> 标示错误内容：</td>
    <td nowrap class="TableData" width=220 colspan="3">
      <input type="text" class="BigInput" id="content" name="content" size="78">
    </td>
    
  </tr>
  <tr>
    <td nowrap class="TableData" width=100> 标示牌类型：</td>
    <td nowrap class="TableData" width=220>
      <select name="type" id="type">
       <option value=""></option>
  <option value="0">英文标识</option>
  <option value="1">英文菜单</option>
  <option value="2">组织结构职务职称</option>
      </select>
    </td>
    <td nowrap class="TableData" width=100> 标示牌所在区县：</td>
    <td nowrap class="TableData" width=220>
      <select name="location" id="location">
         <option value=""></option>
         <option value="0">昌平区</option>
         <option value="1">朝阳区</option>
         <option value="2">崇文区</option>
         <option value="3">大兴区</option>
         <option value="4">东城区</option>
         <option value="5">房山区</option>
         <option value="6">丰台区</option>
         <option value="7">海淀区</option>
         <option value="8">怀柔区</option>
         <option value="9">门头沟区</option>
         <option value="10">密云县</option>
         <option value="11">平谷区</option>
         <option value="12">石景山区</option>
         <option value="13">顺义区</option>
         <option value="14">通州区</option>
         <option value="15">西城区</option>
         <option value="16">宣武区</option>
         <option value="17">延庆县</option>
      </select>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData" width=100> 标示牌具体位置：</td>
    <td nowrap class="TableData" width=220>
      <input type="text" class="BigInput" size=30  name="address" id="address" value="">
    </td>
    <td nowrap class="TableData" width=100> 检查日期：</td>
    <td nowrap class="TableData" width=220>
      <input type="text" id="correctDate" name="correctDate" readonly="true"/>
      <img border="0" align="absMiddle" style="" src="<%=imgPath%>/calendar.gif" id="correctDateImg">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData" width=100> 纠错人姓名：</td>
    <td nowrap class="TableData" width=220>
      <input type="text" class="BigInput" size=30  name="correcter" id="correcter" value="">
    </td>
    <td nowrap class="TableData" width=100> 纠错人联系电话：</td>
    <td nowrap class="TableData" width=220>
      <input type="text" class="BigInput" size=30  name="tel" id="tel" value="">
    </td>
  </tr>
  </tr>
  <tr class="TableControl" align="center" >
     <td nowrap  colspan="4">
       <input type="button" value="查询" class="BigButton" onclick="search()"/>
     </td>
   </tr>
</table>
 
<table border="0" width="400px" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;标示纠错列表</span>&nbsp;
    </td>
  </tr>
</table>
<div id="msrg" align="center"></div>
<div id="controlDiv" align="center"></div>
<div id="listDiv" align="center"></div>
</body>
</html>