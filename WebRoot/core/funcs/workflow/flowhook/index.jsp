<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>业务引擎设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/flowhook/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/flowhook/js/HookLogic.js"></script>
<script>  
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/workflow/act/YHFlowHookAct/getHookListJson.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"hmodule",  width: '10%', text:"模块" ,align: 'center' },
       {type:"data", name:"hname",  width: '10%', text:"名称" ,align: 'center' },
       {type:"data", name:"hdesc",  width: '20%', text:"描述" ,align: 'center' },
       {type:"data", name:"flowName",  width: '10%', text:"流程名称" ,align: 'center',render:getFlowName },
       {type:"data", name:"map",  width: '10%', text:"数据映射" ,align: 'center', render:getMap },
       {type:"data", name:"plugin",  width: '10%', text:"插件" ,align: 'center' },
       {type:"data", name:"state",  width: '5%', text:"状态" ,align: 'center' ,render:getStatus},
       {type:"hidden", name:"system", text:"system", dataType:"int"},
       {type:"hidden", name:"mapName"},
       {type:"selfdef", text:"操作", width: '10%',render:opts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无业务引擎', 'msrg');
  }
}

</script>
</head>
<body topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absMiddle"><span class="big3">&nbsp;业务引擎设置</span>
   &nbsp<input type="button" class="BigButton" onclick="LoadDialogWindow('<%=contextPath %>/core/funcs/workflow/flowhook/add.jsp',self,document.body.scrollLeft+event.clientX-event.offsetX+200,document.body.scrollTop+event.clientY-event.offsetY+280, 600, 450)" value="添加">
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