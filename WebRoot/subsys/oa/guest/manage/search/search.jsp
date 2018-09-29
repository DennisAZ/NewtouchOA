<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String guestNum = request.getParameter("guestNum") == null ? "" : request.getParameter("guestNum");
String guestType = request.getParameter("guestType") == null ? "" : request.getParameter("guestType");
String guestName = request.getParameter("guestName") == null ? "" : request.getParameter("guestName");
String guestDiner = request.getParameter("guestDiner") == null ? "" : request.getParameter("guestDiner");
String guestUnit = request.getParameter("guestUnit") == null ? "" : request.getParameter("guestUnit");
String guestPhone = request.getParameter("guestPhone") == null ? "" : request.getParameter("guestPhone");
String guestAttendTime = request.getParameter("guestAttendTime") == null ? "" : request.getParameter("guestAttendTime");
String guestAttendTime1 = request.getParameter("guestAttendTime1") == null ? "" : request.getParameter("guestAttendTime1");
String guestLeaveTime = request.getParameter("guestLeaveTime") == null ? "" : request.getParameter("guestLeaveTime");
String guestLeaveTime1 = request.getParameter("guestLeaveTime1") == null ? "" : request.getParameter("guestLeaveTime1");
String guestCreator = request.getParameter("guestCreator") == null ? "" : request.getParameter("guestCreator");
String guestDept = request.getParameter("guestDept") == null ? "" : request.getParameter("guestDept");
String guestNote = request.getParameter("guestNote") == null ? "" : request.getParameter("guestNote");
%>
<html>
<head>
<title>贵宾信息查询列表</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/subsys/oa/profsys/js/profsys.js" ></script>
<script type="text/javascript">
var pageMgr = null;
var cfgs = null;
function doInit(){
  var param = "guestNum=<%=guestNum%>&guestType=<%=guestType%>&guestName=<%=guestName%>"
    + "&guestDiner=<%=guestDiner%>&guestUnit=<%=guestUnit%>&guestPhone<%=guestPhone%>&guestAttendTime=<%=guestAttendTime%>"
    + "&guestAttendTime1=<%=guestAttendTime1%>&guestLeaveTime=<%=guestLeaveTime%>&guestLeaveTime1=<%=guestLeaveTime1%>"
    + "&guestCreator=<%=guestCreator%>&guestDept=<%=guestDept%>&guestNote=<%=guestNote%>";
  var url = "<%=contextPath%>/yh/subsys/oa/guest/act/YHGuestAct/queryGuestTerm.act?" + param;
   cfgs = {
    dataAction: url,
    container: "guestList",
    colums: [
             {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
             {type:"text", name:"guestNum", text:"贵宾编号 ", width: "6%",align:"center",sortDef:{type:0, direct:"asc"}},
             {type:"text", name:"guestType", text:"贵宾类型 ", width: "6%",align:"center",sortDef:{type:0, direct:"asc"}},
             {type:"text", name:"guestName", text:"贵宾名称 ", width: "5%",align:"center",sortDef:{type:0, direct:"asc"}},
             {type:"text", name:"guestUnit", text:"贵宾所在单位 ", width: "5%",align:"center",sortDef:{type:0, direct:"asc"}},
             {type:"hidden", name:"guestphone", text:"贵宾联系电话 ", width: "5%",align:"center",sortDef:{type:0, direct:"asc"}},
             {type:"text", name:"deptId", text:"接待部门 ", width: "7%",align:"center",render:toDept,sortDef:{type:0, direct:"asc"}},
             {type:"text", name:"guestAttendTime", text:"来会时间  ", width: "5%",align:"center",render:toAttendDate, sortDef:{type:0, direct:"asc"}},
             {type:"text", name:"guestLeaveTime", text:"离会时间 ", width: "5%",align:"center",render:toLeaveDate,sortDef:{type:0, direct:"asc"}},
             {type:"text", name:"guestDiner", text:"是否用餐  ", width: "5%",align:"center",render:toDiner,sortDef:{type:0, direct:"asc"}},
             {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"6%",render:toOpts}]
        };
     pageMgr = new YHJsPage(cfgs);
     pageMgr.show();
     var total = pageMgr.pageInfo.totalRecord;
     if(total>0){
     }else{
       $("guestList").style.display = "none";
       var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
          + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件信息!</div></td></tr>"
          );
         $('returnNull').update(table); 
     }
}
function toOpts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return   "<a href='#' onclick='showGuest(" + seqId + ");'>详细信息</a> ";
}
function toAttendDate(cellData, recordIndex, columInde){
  var guestAttendTime = this.getCellData(recordIndex,"guestAttendTime");
  return guestAttendTime.substr(0,10);
}
function toLeaveDate(cellData, recordIndex, columInde){
 var guestLeaveTime = this.getCellData(recordIndex,"guestLeaveTime");
 return guestLeaveTime.substr(0,10);
}
function toDiner(cellData, recordIndex, columInde){
 var guestDiner = this.getCellData(recordIndex,"guestDiner");
 var guestDinerStr = "否";
 if(guestDiner=='1'){
   guestDinerStr = "是";
 }
 return guestDinerStr;
}
function toDept(cellData, recordIndex, columInde){
 var deptId = this.getCellData(recordIndex,"deptId");
 var requestURL = "<%=contextPath%>/yh/subsys/oa/guest/act/YHGuestAct/getDeptNameBySeqIds.act?deptId="+deptId; 
 var json=getJsonRs(requestURL); 
 if (json.rtState == '1') { 
   alert(json.rtMsrg); 
   return ; 
 }
 var prc = json.rtData;
 if(prc.deptName){
   return prc.deptName;
 }
 return "";
}
function showGuest(seqId) {
  var myleft = (screen.availWidth - 800)/2;
  window.open("<%=contextPath%>/subsys/oa/guest/manage/new/showGuest.jsp?seqId="+seqId,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=500,left=" + myleft + ",top=50");
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit()">
<br>
<br>
<div id="guestList" style="padding-left: 10px; padding-right: 10px"></div>
<div id="returnNull">
</div>
<table align="center">
    <tr>
      <td>
        <input type="button" value="返回" class="BigButton" onclick="javascript:history.back()">
      </td>
   </tr>
</table>
</body>
</html>
