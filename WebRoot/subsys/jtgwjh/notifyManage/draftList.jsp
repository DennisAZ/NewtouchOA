<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发文登记列表</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/notifyManage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/notifyManage/js/notifyLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/notifyManage/act/YHJhNotifyInfoAct/getListJson.act?publish=0";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    paramFunc: getParam,
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"guId", text:"唯一标示"},
       {type:"data", name:"notifyTitle",  width: '15%', text:"公告标题" ,align: 'left' ,render: renderNotifyTitle},
       {type:"hidden", name:"reciveDept",  width: '10%', text:"接收单位" ,align: 'center' },
       {type:"data", name:"reciveDeptName",  width: '10%', text:"接收单位" ,align: 'center' },
       {type:"data", name:"createDate",  width: '10%', text:"创建时间" ,align: 'center' ,render: renderCreateDate},
       {type:"data", name:"publish",  width: '5%', text:"状态" ,align: 'center' ,render:changePublish},
       {type:"selfdef",  width:'5%', text:"发送详情" ,align: 'center' ,render:renderDetail},
       {type:"selfdef", text:"操作", width: '15%',render:opts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无公告信息', 'msrg');
  }
}


/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var publish= this.getCellData(recordIndex,"publish");
  var sendStr = "<a href=javascript:sendSingle(" + seqId + ")>发送</a>";
  if(publish==0){
  return "<center>"
        + "<a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;"
        + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>&nbsp;"
        + sendStr
        + "</center>";
  }
  else
	  return  "<center>"
	  //+ "<a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;"
      + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>&nbsp;"
     // + "<a href=javascript:sendSingle(" + seqId + ")>重发</a>";
      + "</center>";
}

function getGroup(){
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }
  else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $("listContainer").style.display = "";
    $('delOpt').style.display = "";
    $('msrg').style.display = "none";
  }else{
    $('msrg').style.display = "";
    $("listContainer").style.display = "none";
    $('delOpt').style.display = "none";
    WarningMsrg('无公告信息', 'msrg');
  }
}

function getParam(){
  var queryParam = $("form1").serialize();
  return queryParam;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absMiddle"><span class="big3">&nbsp;公告列表 </span>
   </td>
 </tr>
</table>
<br>
<form action="" id="form1" name="form1">

</form>
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