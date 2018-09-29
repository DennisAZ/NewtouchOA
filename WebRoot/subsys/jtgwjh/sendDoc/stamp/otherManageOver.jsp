<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>协办待盖章列表</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/sendLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendStampAct/getListJsonOtherOver.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"docTitle",  width: '15%', text:"文件标题" ,align: 'left' ,render: renderDocTitle},
       {type:"data", name:"docType",  width: '8%', text:"文件类型" ,align: 'center' ,render: renderDocType},
       {type:"data", name:"urgentType",  width: '8%', text:"紧急程度" ,align: 'center' ,render: renderUrgentType},
       {type:"data", name:"securityLevel",  width: '8%', text:"密级" ,align: 'center' ,render: renderSecurityLevel},
       {type:"data", name:"docNo",  width: '10%', text:"文号" ,align: 'center' },
       {type:"hidden", name:"reciveDept",  width: '10%', text:"联网接收单位" ,align: 'center' },
       {type:"data", name:"reciveDeptDept",  width: '10%', text:"联网接收单位" ,align: 'center' },
       {type:"data", name:"createDatetime",  width: '10%', text:"创建时间" ,align: 'center' ,render: renderCreateDatetime}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无已盖章发文信息', 'msrg');
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
  return "<center>"
        + "<div><a href=javascript:detail(" + seqId + ")>详细信息</a>&nbsp;"
        + "</center>";
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absMiddle"><span class="big3">&nbsp;协办待盖章列表 </span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
</div>

<div id="msrg">
</div>
</body>
</html>