<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<head>
<title>工作流</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="js/index1.js"></script>
<script type="text/javascript">
var pageMgr = null;
var menuData2 = [{ name:'<div style="padding-top:5px;margin-left:10px">全部<div>',action:set_status,extData:'0'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">未开始<div>',action:set_status,extData:'1'}
,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">进行中<div>',action:set_status,extData:'2'}
,{ name:'<div style="color:#FF0000;padding-top:5px;margin-left:10px">已超时<div>',action:set_status,extData:'3'}
,{ name:'<div style="color:#00AA00;padding-top:5px;margin-left:10px">已完成<div>',action:set_status,extData:'4'}
]

/**
 * @recordIndex         当前页面记录索引，从0开始计数
 */
function test1(recordIndex) {
  alert(this.getRecord(recordIndex).seqId);
}
/**
 * @recordIndex         当前页面记录索引，从0开始计数
 */
function test2(recordIndex) {
  alert("test2>>" + recordIndex);
}
/**
 * 取得查询参数
 */
function getQueryParam() {
  return $("form1").serialize();
}
/**
 * 自定义描画函数，以分页对象为thisObject执行该函数
 * @cellData            单元格数据
 * @recordIndex         当前页面记录索引，从0开始计数
 * @columIndex          栏目索引
 */
function render(cellData, recordIndex, columIndex) {
  //alert(cellData);
  if (cellData > 200) {
    return "<span id=\"span_" + recordIndex + "_" + columIndex + "\" style=\"color:red\">" + cellData + "</span>";
  }else {
    return "<span id=\"span_" + recordIndex + "_" + columIndex + "\" style=\"color:green\">" + cellData + "</span>";
  }
}
/**
 * 自定义事件绑定，与自定义描画函数配合使用
 * @cellData            单元格数据
 * @recordIndex         当前页面记录索引，从0开始计数
 * @columIndex          栏目索引
 */
function bindAction(cellData, recordIndex, columIndex) {
  var cntrl = $("span_" + recordIndex + "_" + columIndex);
  cntrl.observe("click", function(){alert(this.pageInfo.pageIndex);}.bind(this));
}

/**
 * 绑定到栏目标题的函数
 * @columeIndex          栏目的索引
 */
function bindTitleAction(columIndex) {
  var cntrl = $("headCell_" + columIndex);
  cntrl.observe('click', function(){
      var recordCnt = this.getRecordCnt();
      var tmpStr = "";
      for (var i = 0; i < recordCnt; i++) {
        tmpStr += this.getCellData(i, "seqId");
        if (i < recordCnt - 1) {
          tmpStr += ",";
        }
      }
      alert(tmpStr);
    }.bind(this));
}
/**
 * 获取图标
 * @cellData            单元格数据
 * @recordIndex         当前页面记录索引，从0开始计数
 * @columIndex          栏目索引
 */
function getIcon(cellData, recordIndex, columIndex) {
  var srcPath = null;
  if (cellData > 250) {
    srcPath = imgPath + "/arrow_up.gif";
  }else {
    srcPath = imgPath + "/1.gif";
  }
  return "<img src=\"" + srcPath + "\" />";
}
/**
 * 显示隐藏
 */
function switchIt() {
  if (pageMgr.isDisp) {
    pageMgr.hide();
  }else {
    pageMgr.show();
  }
}
function getStyle(cellData, recordIndex, columIndex) {
  var record = this.getRecord(recordIndex);
  if (record["isEmptyPass"] == "1") {
    return {color: "#FF0000"};
  }else {
    return {color: "#00FF00"};
  }
}
function doInit() {
  var cfgs = {
    dataAction: contextPath + "/yh/rad/docs/act/YHTestPageAction/getPage.act",
    container: "listContainer",
    paramFunc: getQueryParam,
    sortIndex: 3,
    sortDirect: "asc",
    showRecordCnt: true,
    colums: [{type:"check", width: "10%"},
       //{type:"data", name:"seqId", text:"顺序号", width: 150, dataType:"int", floatMenu:menuData2, iconFunc: getIcon},
       {type:"hidden", name:"seqId"},
       {type:"data", name:"fromId", text:"发送人", width: "10%", dataType:"int", render:render, bindAction:bindAction, bindTitleAction: bindTitleAction, selfTitleStyle:{cursor:"pointer", textDecoration:"underline"}},       
       {type:"data", name:"sendTime", styleFunc:getStyle, text:"发送时间", width: "30%", dataType:"date", sortDef:{type: 0, direct:"desc"}},
       {type:"data", name:"attach", text:"附件", width: "30%", dataType:"attach", sortDef:{type: 0, direct:"asc"}},
       {type:"hidden",name:"attachId"},
       {type:"opts", width: "15%", opts:[{clickFunc:test1, text:"操作1"},{clickFunc:test2, text:"操作2"},{floatMenu:menuData2, text:"更多&gt;&gt;"}]}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}
</script>
</head>
<body onload="doInit()">
<form name="form1" id="form1">
名字<input type="text" name="name" id="name"></input>
<input type="button" onclick="switchIt();" value="显示/隐藏" name="btnShow" id="btnShow"></input>
</form>
<div id="listContainer">
</div>
</body>
</html>