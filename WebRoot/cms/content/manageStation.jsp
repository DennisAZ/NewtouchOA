<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String stationId = request.getParameter("stationId");
String columnId = request.getParameter("columnId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>管理文章</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/cms/content/js/contentLogic.js"></script>
<script> 
var pageMgr = null;
var stationId = '<%=stationId %>';
var columnId = '<%=columnId %>';;
function doInit(){
  var url = "<%=contextPath%>/yh/cms/content/act/YHContentAct/getContentList.act?stationId="+stationId+"&columnId="+columnId;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"contentName",  width: '30%', text:"文章标题" ,align: 'center', render: titleDetail},
       {type:"hidden", name:"createId",   text:"创建者ID" ,dataType:"int"},
       {type:"data", name:"userName",  width: '10%', text:"创建者" ,align: 'center'},
       {type:"data", name:"columnName",  width: '10%', text:"所属栏目" ,align: 'center'},
       {type:"data", name:"contentDate", width: '12%', text:"发布时间" ,align: 'center' ,render: dateRender},
       {type:"data", name:"contentStatus",  width: '5%', text:"状态" ,align: 'center' ,render: stringRender},
       {type:"hidden", name:"contentIndex", text:"调序" ,dataType:"int"},
       {type:"hidden", name:"contentTop",   text:"置顶" ,dataType:"int"},
       {type:"hidden", name:"visitUser", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"editUserContent", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"approvalUserContent", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"releaseUserContent", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"recevieUserContent", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"orderContent", text:"顺序号", dataType:"int"}]
       //{type:"selfdef", text:"操作", width: '15%',render:opts1}
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无内容信息', 'msrg');
  }
}

/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opts1(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
	var contentStatus = this.getCellData(recordIndex,"contentStatus");
	var control = "";
	var update = "<a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;";
	switch(contentStatus){
	  case 0 : control = update;break;
	  case 1 : control = update;break;
	  case 2 : control = update;break;
	  case 3 : break;
	  case 4 : control = update;break;
	}
	return "<center>"
				+ control
				+ "</center>";
}

function titleDetail(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href=javascript:toDetail(" + seqId + ")>"+cellData+"</a>";
}

function toDetail(seqId){
  var URL = contextPath + "/cms/content/detail.jsp?seqId=" + seqId;
  newWindow(URL,'820', '500');
}

function dateRender(cellData, recordIndex, columIndex){
  if(cellData){
    return cellData.substring(0,19);
  }
  return cellData;
}

function stringRender(cellData, recordIndex, columIndex){
  var str = "";
  switch(cellData){
  	case 0 : str = "新搞";break;
  	case 1 : str = "已编";break;
  	case 2 : str = "已否";break;
  	case 3 : str = "已签";break;
  	case 4 : str = "已撤";break;
  	case 5 : str = "已发";break;
  }
  return str;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absMiddle"><span class="big3">&nbsp;管理内容 </span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;
         <a href="javascript:deleteAll();" title="删除所选记录"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选记录</a>&nbsp;
      </td>
 </tr>
</table>
</div>

<div id="msrg">
</div>
</body>
</html>