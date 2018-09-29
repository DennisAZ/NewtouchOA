<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>试卷管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/paperManageLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamPaperAct/getExamPaperTitleJson.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"paperTitle",  width: '20%', text:"试卷标题", render:paperCenterFunc},       
         {type:"data", name:"paperDesc",  width: '10%', text:"试卷说明", render:paperCenterFunc},
         {type:"data", name:"paperTimes",  width: '10%', text:"考试时间", render:paperCenterFunc},
         {type:"data", name:"paperGrade",  width: '10%', text:"总分", render:paperCenterFunc},
         {type:"data", name:"questionsCount",  width: '10%', text:"试题数量", render:paperCenterFunc},
         {type:"data", name:"sendDate",  width: '10%', text:"出题日期", render:sendDateFunc},
         {type:"selfdef", text:"操作", width: '20%',render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      $("numDiv").update("共<span class='big4'>&nbsp;" + total + "</span>&nbsp;条")
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('尚未定义试卷', 'msrg');
    }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" align="absmiddle"><span class="big3"> 管理试卷</span><br>
    </td>
  </tr>
</table>
<div align="center" id="numDiv"> </div>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
</div>

<div id="msrg">
</div>
</body>
</html>