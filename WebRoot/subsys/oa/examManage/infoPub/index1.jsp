<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>考试信息</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/infoPub.js"></script>
<script type="text/javascript">
function doInitFlow(){
  var url = "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamFlowAct/selectFlow.act";
   cfgs = {
    dataAction: url,
    container: "giftList",
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"flowTitle", text:"考试名称", width: "6%",align:"center"},
       {type:"text", name:"participant", text:"参加考试人", width: "6%",align:"center",render:toStr},
       {type:"text", name:"paperTimes", text:"考试时长（分钟）", width: "6%",align:"center"},
       {type:"text", name:"beginDate", text:"生效日期", width: "6%",align:"center",render:toBenginDate,sortDef:{type:0, direct:"desc"}},
       {type:"text", name:"endDate", text:"终止日期", width: "6%",align:"center",render:toEndDate},
       {type:"hidden", name:"paperId", text:"paperId", width: "1%",align:"center"},
       {type:"text", name:"caoZuo", text:"操作", width: "10%",align:"center",render:toCaoZuo}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无已发布的考试信息!</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  } else {
    $("toDiv").update("共<span class='big4'>&nbsp;" + total + "</span>&nbsp;条")
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInitFlow();">
 <div>
 <img src="<%=imgPath %>/exam_manage.gif" align="absmiddle" WIDTH="18" HEIGHT="18">
 <span class="big3">&nbsp;管理考试信息</span></div>
 <div align="center" id="toDiv"> </div>
<div id="giftList"></div>
<div id="returnNull"></div>
</body>
</html>