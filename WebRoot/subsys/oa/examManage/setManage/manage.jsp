<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>题库管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/examQuizSetLogic.js"></script>
<script> 

var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamQuizSetAct/getExamQuizSetList.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"roomCode",  width: '20%', text:"题库编号", render:examQuizSetCenterFunc},       
         {type:"data", name:"roomName",  width: '20%', text:"题库名称", render:examQuizSetCenterFunc},
         {type:"data", name:"roomDesc",  width: '20%', text:"题库说明", render:examQuizSetCenterFunc},
         {type:"selfdef", text:"操作", width: '20%',render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      $("numDiv").update("共<span class='big4'>&nbsp;" + total + "</span>&nbsp;条")
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
    }else{
      WarningMsrg('尚未定义题库', 'msrg');
    }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" align="absmiddle"><span class="big3"> 题库管理</span><br>
    </td>
  </tr>
</table>
 <div align="center" id="numDiv"> </div>
<div id="listContainer" style="display:none;width:100;">
</div>

<div id="msrg">
</div>
</body>
</html>